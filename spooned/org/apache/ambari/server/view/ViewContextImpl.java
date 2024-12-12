package org.apache.ambari.server.view;
import com.google.inject.persist.Transactional;
import org.apache.directory.api.util.Strings;
import org.apache.hadoop.security.authentication.util.KerberosName;
import org.apache.hadoop.security.authentication.util.KerberosUtil;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.exception.ParseErrorException;
public class ViewContextImpl implements org.apache.ambari.view.ViewContext , org.apache.ambari.view.ViewController {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.view.ViewContextImpl.class);

    public static final java.lang.String HADOOP_SECURITY_AUTH_TO_LOCAL = "hadoop.security.auth_to_local";

    public static final java.lang.String CORE_SITE = "core-site";

    public static final java.lang.String HDFS_AUTH_TO_LOCAL = "hdfs.auth_to_local";

    private final org.apache.ambari.server.orm.entities.ViewEntity viewEntity;

    private final org.apache.ambari.server.orm.entities.ViewInstanceEntity viewInstanceEntity;

    private final org.apache.ambari.server.view.ViewRegistry viewRegistry;

    private org.apache.ambari.view.DataStore dataStore = null;

    private org.apache.ambari.view.Masker masker;

    private final org.apache.velocity.VelocityContext velocityContext;

    public ViewContextImpl(org.apache.ambari.server.orm.entities.ViewInstanceEntity viewInstanceEntity, org.apache.ambari.server.view.ViewRegistry viewRegistry) {
        this(viewInstanceEntity.getViewEntity(), viewInstanceEntity, viewRegistry);
    }

    public ViewContextImpl(org.apache.ambari.server.orm.entities.ViewEntity viewEntity, org.apache.ambari.server.view.ViewRegistry viewRegistry) {
        this(viewEntity, null, viewRegistry);
    }

    private ViewContextImpl(org.apache.ambari.server.orm.entities.ViewEntity viewEntity, org.apache.ambari.server.orm.entities.ViewInstanceEntity viewInstanceEntity, org.apache.ambari.server.view.ViewRegistry viewRegistry) {
        this.viewEntity = viewEntity;
        this.viewInstanceEntity = viewInstanceEntity;
        this.viewRegistry = viewRegistry;
        this.masker = getMasker(viewEntity.getClassLoader(), viewEntity.getConfiguration());
        this.velocityContext = initVelocityContext();
    }

    @java.lang.Override
    public java.lang.String getViewName() {
        return viewEntity.getCommonName();
    }

    @java.lang.Override
    public org.apache.ambari.view.ViewDefinition getViewDefinition() {
        return viewEntity;
    }

    @java.lang.Override
    public java.lang.String getInstanceName() {
        return viewInstanceEntity == null ? null : viewInstanceEntity.getName();
    }

    @java.lang.Override
    public org.apache.ambari.view.ViewInstanceDefinition getViewInstanceDefinition() {
        return viewInstanceEntity;
    }

    @java.lang.Override
    public java.util.Map<java.lang.String, java.lang.String> getProperties() {
        if (viewInstanceEntity == null) {
            return null;
        } else {
            return java.util.Collections.unmodifiableMap(getPropertyValues());
        }
    }

    @com.google.inject.persist.Transactional
    @java.lang.Override
    public void putInstanceData(java.lang.String key, java.lang.String value) {
        checkInstance();
        org.apache.ambari.server.orm.entities.ViewInstanceEntity updateInstance = viewRegistry.getViewInstanceEntity(viewInstanceEntity.getViewName(), viewInstanceEntity.getInstanceName());
        if (updateInstance != null) {
            updateInstance.putInstanceData(key, value);
            try {
                viewRegistry.updateViewInstance(updateInstance);
            } catch (org.apache.ambari.view.SystemException e) {
                java.lang.String msg = "Caught exception updating the view instance.";
                org.apache.ambari.server.view.ViewContextImpl.LOG.error(msg, e);
                throw new java.lang.IllegalStateException(msg, e);
            } catch (org.apache.ambari.server.view.validation.ValidationException e) {
                throw new java.lang.IllegalArgumentException(e.getMessage());
            }
        }
    }

    @java.lang.Override
    public java.lang.String getInstanceData(java.lang.String key) {
        return viewInstanceEntity == null ? null : viewInstanceEntity.getInstanceDataMap().get(key);
    }

    @java.lang.Override
    public java.util.Map<java.lang.String, java.lang.String> getInstanceData() {
        return viewInstanceEntity == null ? null : java.util.Collections.unmodifiableMap(viewInstanceEntity.getInstanceDataMap());
    }

    @java.lang.Override
    public void removeInstanceData(java.lang.String key) {
        checkInstance();
        viewRegistry.removeInstanceData(viewInstanceEntity, key);
    }

    @java.lang.Override
    public java.lang.String getAmbariProperty(java.lang.String key) {
        return viewInstanceEntity == null ? null : viewInstanceEntity.getViewEntity().getAmbariProperty(key);
    }

    @java.lang.Override
    public org.apache.ambari.view.ResourceProvider<?> getResourceProvider(java.lang.String type) {
        return viewInstanceEntity == null ? null : viewInstanceEntity.getResourceProvider(type);
    }

    @java.lang.Override
    public java.lang.String getUsername() {
        java.lang.String shortName = getLoggedinUser();
        try {
            java.lang.String authToLocalRules = getAuthToLocalRules();
            java.lang.String defaultRealm = org.apache.hadoop.security.authentication.util.KerberosUtil.getDefaultRealm();
            if (org.apache.directory.api.util.Strings.isNotEmpty(authToLocalRules) && org.apache.directory.api.util.Strings.isNotEmpty(defaultRealm)) {
                synchronized(org.apache.hadoop.security.authentication.util.KerberosName.class) {
                    org.apache.hadoop.security.authentication.util.KerberosName.setRules(authToLocalRules);
                    shortName = new org.apache.hadoop.security.authentication.util.KerberosName((shortName + "@") + defaultRealm).getShortName();
                }
            }
        } catch (java.lang.reflect.InvocationTargetException e) {
            org.apache.ambari.server.view.ViewContextImpl.LOG.debug("Failed to get default realm", e);
        } catch (java.lang.Exception e) {
            org.apache.ambari.server.view.ViewContextImpl.LOG.warn("Failed to apply auth_to_local rules. " + e.getMessage());
            org.apache.ambari.server.view.ViewContextImpl.LOG.debug("Failed to apply auth_to_local rules", e);
        }
        return shortName;
    }

    private java.lang.String getAuthToLocalRules() {
        org.apache.ambari.view.cluster.Cluster cluster = getCluster();
        java.lang.String authToLocalRules = null;
        if (cluster != null) {
            authToLocalRules = cluster.getConfigurationValue(org.apache.ambari.server.view.ViewContextImpl.CORE_SITE, org.apache.ambari.server.view.ViewContextImpl.HADOOP_SECURITY_AUTH_TO_LOCAL);
        } else if (viewInstanceEntity != null) {
            authToLocalRules = viewInstanceEntity.getPropertyMap().get(org.apache.ambari.server.view.ViewContextImpl.HDFS_AUTH_TO_LOCAL);
        }
        return authToLocalRules;
    }

    @java.lang.Override
    public java.lang.String getLoggedinUser() {
        return viewInstanceEntity != null ? viewInstanceEntity.getUsername() : null;
    }

    @java.lang.Override
    public void hasPermission(java.lang.String userName, java.lang.String permissionName) throws org.apache.ambari.view.SecurityException {
        if ((userName == null) || (userName.length() == 0)) {
            throw new org.apache.ambari.view.SecurityException("No user name specified.");
        }
        if ((permissionName == null) || (permissionName.length() == 0)) {
            throw new org.apache.ambari.view.SecurityException("No permission name specified.");
        }
        if (viewInstanceEntity == null) {
            throw new org.apache.ambari.view.SecurityException("There is no instance associated with the view context");
        }
        org.apache.ambari.server.orm.entities.PermissionEntity permissionEntity = viewEntity.getPermission(permissionName);
        if (permissionEntity == null) {
            throw new org.apache.ambari.view.SecurityException((("The permission " + permissionName) + " is not defined for ") + viewEntity.getName());
        }
        if (!viewRegistry.hasPermission(permissionEntity, viewInstanceEntity.getResource(), userName)) {
            throw new org.apache.ambari.view.SecurityException((("The user " + userName) + " has not been granted permission ") + permissionName);
        }
    }

    @java.lang.Override
    public org.apache.ambari.view.URLStreamProvider getURLStreamProvider() {
        return viewRegistry.createURLStreamProvider(this);
    }

    @java.lang.Override
    public org.apache.ambari.view.URLConnectionProvider getURLConnectionProvider() {
        return viewRegistry.createURLStreamProvider(this);
    }

    @java.lang.Override
    public synchronized org.apache.ambari.view.AmbariStreamProvider getAmbariStreamProvider() {
        return viewRegistry.createAmbariStreamProvider();
    }

    @java.lang.Override
    public org.apache.ambari.view.AmbariStreamProvider getAmbariClusterStreamProvider() {
        java.lang.Long clusterHandle = viewInstanceEntity.getClusterHandle();
        org.apache.ambari.view.ClusterType clusterType = viewInstanceEntity.getClusterType();
        org.apache.ambari.view.AmbariStreamProvider clusterStreamProvider = null;
        if ((clusterHandle != null) && (clusterType == org.apache.ambari.view.ClusterType.LOCAL_AMBARI)) {
            clusterStreamProvider = getAmbariStreamProvider();
        } else if ((clusterHandle != null) && (clusterType == org.apache.ambari.view.ClusterType.REMOTE_AMBARI)) {
            clusterStreamProvider = viewRegistry.createRemoteAmbariStreamProvider(clusterHandle);
        }
        return clusterStreamProvider;
    }

    @java.lang.Override
    public synchronized org.apache.ambari.view.DataStore getDataStore() {
        if (viewInstanceEntity != null) {
            if (dataStore == null) {
                com.google.inject.Injector injector = com.google.inject.Guice.createInjector(new org.apache.ambari.server.view.persistence.DataStoreModule(viewInstanceEntity));
                dataStore = injector.getInstance(org.apache.ambari.server.view.persistence.DataStoreImpl.class);
            }
        }
        return dataStore;
    }

    @java.lang.Override
    public java.util.Collection<org.apache.ambari.view.ViewDefinition> getViewDefinitions() {
        return java.util.Collections.unmodifiableCollection(viewRegistry.getDefinitions());
    }

    @java.lang.Override
    public java.util.Collection<org.apache.ambari.view.ViewInstanceDefinition> getViewInstanceDefinitions() {
        java.util.Collection<org.apache.ambari.server.orm.entities.ViewInstanceEntity> instanceDefinitions = new java.util.HashSet<>();
        for (org.apache.ambari.server.orm.entities.ViewEntity viewEntity : viewRegistry.getDefinitions()) {
            instanceDefinitions.addAll(viewRegistry.getInstanceDefinitions(viewEntity));
        }
        return java.util.Collections.unmodifiableCollection(instanceDefinitions);
    }

    @java.lang.Override
    public org.apache.ambari.view.ViewController getController() {
        return this;
    }

    @java.lang.Override
    public org.apache.ambari.server.view.HttpImpersonatorImpl getHttpImpersonator() {
        return new org.apache.ambari.server.view.HttpImpersonatorImpl(this);
    }

    @java.lang.Override
    public org.apache.ambari.view.ImpersonatorSetting getImpersonatorSetting() {
        return new org.apache.ambari.server.view.ImpersonatorSettingImpl(this);
    }

    @java.lang.Override
    public org.apache.ambari.view.cluster.Cluster getCluster() {
        return viewRegistry.getCluster(viewInstanceEntity);
    }

    @java.lang.Override
    public void fireEvent(java.lang.String eventId, java.util.Map<java.lang.String, java.lang.String> eventProperties) {
        org.apache.ambari.view.events.Event event = (viewInstanceEntity == null) ? new org.apache.ambari.server.view.events.EventImpl(eventId, eventProperties, viewEntity) : new org.apache.ambari.server.view.events.EventImpl(eventId, eventProperties, viewInstanceEntity);
        viewRegistry.fireEvent(event);
    }

    @java.lang.Override
    public void registerListener(org.apache.ambari.view.events.Listener listener, java.lang.String viewName) {
        viewRegistry.registerListener(listener, viewName, null);
    }

    @java.lang.Override
    public void registerListener(org.apache.ambari.view.events.Listener listener, java.lang.String viewName, java.lang.String viewVersion) {
        viewRegistry.registerListener(listener, viewName, viewVersion);
    }

    @java.lang.Override
    public void unregisterListener(org.apache.ambari.view.events.Listener listener, java.lang.String viewName) {
        viewRegistry.unregisterListener(listener, viewName, null);
    }

    @java.lang.Override
    public void unregisterListener(org.apache.ambari.view.events.Listener listener, java.lang.String viewName, java.lang.String viewVersion) {
        viewRegistry.unregisterListener(listener, viewName, viewVersion);
    }

    private void checkInstance() {
        if (viewInstanceEntity == null) {
            throw new java.lang.IllegalStateException("No instance is associated with the context.");
        }
    }

    private org.apache.ambari.view.Masker getMasker(java.lang.ClassLoader cl, org.apache.ambari.server.view.configuration.ViewConfig viewConfig) {
        try {
            return viewConfig.getMaskerClass(cl).newInstance();
        } catch (java.lang.Exception e) {
            throw new java.lang.InstantiationError("Could not create masker instance.");
        }
    }

    private java.util.Map<java.lang.String, java.lang.String> getPropertyValues() {
        java.util.Map<java.lang.String, java.lang.String> properties = viewInstanceEntity.getPropertyMap();
        java.util.Map<java.lang.String, org.apache.ambari.server.view.configuration.ParameterConfig> parameters = new java.util.HashMap<>();
        for (org.apache.ambari.server.view.configuration.ParameterConfig paramConfig : viewEntity.getConfiguration().getParameters()) {
            parameters.put(paramConfig.getName(), paramConfig);
        }
        org.apache.ambari.view.cluster.Cluster cluster = getCluster();
        for (java.util.Map.Entry<java.lang.String, java.lang.String> entry : properties.entrySet()) {
            java.lang.String propertyName = entry.getKey();
            java.lang.String propertyValue = entry.getValue();
            org.apache.ambari.server.view.configuration.ParameterConfig parameterConfig = parameters.get(propertyName);
            if (parameterConfig != null) {
                java.lang.String clusterConfig = parameterConfig.getClusterConfig();
                if ((clusterConfig != null) && (cluster != null)) {
                    propertyValue = org.apache.ambari.server.view.ViewContextImpl.getClusterConfigurationValue(cluster, clusterConfig);
                } else if (parameterConfig.isMasked()) {
                    try {
                        propertyValue = masker.unmask(propertyValue);
                    } catch (org.apache.ambari.view.MaskException e) {
                        org.apache.ambari.server.view.ViewContextImpl.LOG.error("Failed to unmask view property", e);
                    }
                }
            }
            properties.put(propertyName, evaluatePropertyTemplates(propertyValue));
        }
        return properties;
    }

    private static java.lang.String getClusterConfigurationValue(org.apache.ambari.view.cluster.Cluster cluster, java.lang.String clusterConfig) {
        if (clusterConfig != null) {
            java.lang.String[] parts = clusterConfig.split("/");
            if (parts.length == 2) {
                return cluster.getConfigurationValue(parts[0], parts[1]);
            }
        }
        return null;
    }

    private java.lang.String evaluatePropertyTemplates(java.lang.String rawValue) {
        if (rawValue != null) {
            try {
                java.io.Writer templateWriter = new java.io.StringWriter();
                org.apache.velocity.app.Velocity.evaluate(velocityContext, templateWriter, rawValue, rawValue);
                return templateWriter.toString();
            } catch (org.apache.velocity.exception.ParseErrorException e) {
                org.apache.ambari.server.view.ViewContextImpl.LOG.warn(java.lang.String.format("Error during parsing '%s' parameter. Leaving original value.", rawValue));
            }
        }
        return rawValue;
    }

    private org.apache.velocity.VelocityContext initVelocityContext() {
        org.apache.velocity.VelocityContext context = new org.apache.velocity.VelocityContext();
        context.put("username", new org.apache.ambari.server.view.ViewContextImpl.ParameterResolver() {
            @java.lang.Override
            protected java.lang.String getValue() {
                return viewContext.getUsername();
            }
        });
        context.put("viewName", new org.apache.ambari.server.view.ViewContextImpl.ParameterResolver() {
            @java.lang.Override
            protected java.lang.String getValue() {
                return viewContext.getViewName();
            }
        });
        context.put("instanceName", new org.apache.ambari.server.view.ViewContextImpl.ParameterResolver() {
            @java.lang.Override
            protected java.lang.String getValue() {
                return viewContext.getInstanceName();
            }
        });
        context.put("loggedinUser", new org.apache.ambari.server.view.ViewContextImpl.ParameterResolver() {
            @java.lang.Override
            protected java.lang.String getValue() {
                return viewContext.getLoggedinUser();
            }
        });
        return context;
    }

    private abstract class ParameterResolver {
        protected final org.apache.ambari.view.ViewContext viewContext = ViewContextImpl.this;

        protected abstract java.lang.String getValue();

        @java.lang.Override
        public java.lang.String toString() {
            java.lang.String value = getValue();
            return value == null ? "" : value;
        }
    }
}