package org.apache.ambari.server.view;
import com.google.inject.persist.Transactional;
import javax.xml.bind.JAXBException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.PropertyConfigurator;
@javax.inject.Singleton
public class ViewRegistry {
    private static final java.lang.String EXTRACTED_ARCHIVES_DIR = "work";

    private static final java.lang.String EXTRACT_COMMAND = "extract";

    private static final java.lang.String ALL_VIEWS_REG_EXP = ".*";

    protected static final int DEFAULT_REQUEST_CONNECT_TIMEOUT = 5000;

    protected static final int DEFAULT_REQUEST_READ_TIMEOUT = 10000;

    private static final java.lang.String VIEW_AMBARI_VERSION_REGEXP = "^((\\d+\\.)?)*(\\*|\\d+)$";

    private static final java.lang.String VIEW_LOG_FILE = "view.log4j.properties";

    private static final java.lang.String AMBARI_LOG_FILE = "log4j.properties";

    private static final java.lang.String LOG4J = "log4j.";

    public static final java.lang.String API_PREFIX = "/api/v1/clusters/";

    public static final java.lang.String DEFAULT_AUTO_INSTANCE_URL = "auto_instance";

    private static java.util.concurrent.ExecutorService executorService;

    private java.util.Map<java.lang.String, org.apache.ambari.server.orm.entities.ViewEntity> viewDefinitions = new java.util.HashMap<>();

    private java.util.Map<org.apache.ambari.server.orm.entities.ViewEntity, java.util.Map<java.lang.String, org.apache.ambari.server.orm.entities.ViewInstanceEntity>> viewInstanceDefinitions = new java.util.HashMap<>();

    private final java.util.Map<java.lang.String, java.util.Set<org.apache.ambari.server.api.resources.SubResourceDefinition>> subResourceDefinitionsMap = new java.util.concurrent.ConcurrentHashMap<>();

    private final java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, org.apache.ambari.server.controller.spi.ResourceProvider> resourceProviders = new java.util.concurrent.ConcurrentHashMap<>();

    private final java.util.Map<java.lang.String, java.util.Set<org.apache.ambari.view.events.Listener>> listeners = new java.util.concurrent.ConcurrentHashMap<>();

    private static org.apache.ambari.server.view.ViewRegistry singleton;

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.view.ViewRegistry.class);

    protected org.apache.ambari.server.view.ViewDataMigrationUtility viewDataMigrationUtility;

    @javax.inject.Inject
    org.apache.ambari.server.orm.dao.ViewDAO viewDAO;

    @javax.inject.Inject
    org.apache.ambari.server.orm.dao.ViewInstanceDAO instanceDAO;

    @javax.inject.Inject
    org.apache.ambari.server.orm.dao.UserDAO userDAO;

    @javax.inject.Inject
    org.apache.ambari.server.orm.dao.MemberDAO memberDAO;

    @javax.inject.Inject
    org.apache.ambari.server.orm.dao.PrivilegeDAO privilegeDAO;

    @javax.inject.Inject
    org.apache.ambari.server.security.SecurityHelper securityHelper;

    @javax.inject.Inject
    org.apache.ambari.server.orm.dao.ResourceDAO resourceDAO;

    @javax.inject.Inject
    org.apache.ambari.server.orm.dao.ResourceTypeDAO resourceTypeDAO;

    @javax.inject.Inject
    org.apache.ambari.server.orm.dao.PrincipalDAO principalDAO;

    @javax.inject.Inject
    org.apache.ambari.server.orm.dao.PermissionDAO permissionDAO;

    @javax.inject.Inject
    javax.inject.Provider<org.apache.ambari.server.state.Clusters> clustersProvider;

    @javax.inject.Inject
    javax.inject.Provider<org.apache.ambari.server.api.services.AmbariMetaInfo> ambariMetaInfoProvider;

    @javax.inject.Inject
    org.apache.ambari.server.configuration.Configuration configuration;

    @javax.inject.Inject
    org.apache.ambari.server.view.ViewInstanceHandlerList handlerList;

    @javax.inject.Inject
    org.apache.ambari.server.view.ViewExtractor extractor;

    @javax.inject.Inject
    org.apache.ambari.server.view.ViewArchiveUtility archiveUtility;

    @javax.inject.Inject
    org.apache.ambari.server.controller.AmbariSessionManager ambariSessionManager;

    @javax.inject.Inject
    org.apache.ambari.server.view.RemoteAmbariClusterRegistry remoteAmbariClusterRegistry;

    @javax.inject.Inject
    org.apache.ambari.server.orm.dao.RemoteAmbariClusterDAO remoteAmbariClusterDAO;

    @javax.inject.Inject
    org.apache.ambari.server.orm.dao.ViewURLDAO viewURLDAO;

    @javax.inject.Inject
    org.apache.ambari.server.view.ViewInstanceOperationHandler viewInstanceOperationHandler;

    @javax.inject.Inject
    public ViewRegistry(org.apache.ambari.server.events.publishers.AmbariEventPublisher publisher) {
        publisher.register(this);
    }

    public static void main(java.lang.String[] args) {
        if (args.length >= 2) {
            if (args[0].equals(org.apache.ambari.server.view.ViewRegistry.EXTRACT_COMMAND)) {
                java.lang.String archivePath = args[1];
                org.apache.ambari.server.view.ViewRegistry.ViewModule viewModule = new org.apache.ambari.server.view.ViewRegistry.ViewModule();
                try {
                    if (org.apache.ambari.server.view.ViewRegistry.extractViewArchive(archivePath, viewModule, true)) {
                        java.lang.System.exit(0);
                    }
                } catch (java.lang.Exception e) {
                    java.lang.String msg = ("Caught exception extracting view archive " + archivePath) + ".";
                    org.apache.ambari.server.view.ViewRegistry.LOG.error(msg, e);
                    java.lang.System.exit(2);
                }
            }
        }
        java.lang.System.exit(1);
    }

    public java.util.Collection<org.apache.ambari.server.orm.entities.ViewEntity> getDefinitions() {
        return viewDefinitions.values();
    }

    public org.apache.ambari.server.orm.entities.ViewEntity getDefinition(java.lang.String viewName, java.lang.String version) {
        return getDefinition(org.apache.ambari.server.orm.entities.ViewEntity.getViewName(viewName, version));
    }

    public org.apache.ambari.server.orm.entities.ViewEntity getDefinition(org.apache.ambari.server.orm.entities.ResourceTypeEntity resourceTypeEntity) {
        for (org.apache.ambari.server.orm.entities.ViewEntity viewEntity : viewDefinitions.values()) {
            if (viewEntity.isDeployed()) {
                if (viewEntity.getResourceType().equals(resourceTypeEntity)) {
                    return viewEntity;
                }
            }
        }
        return null;
    }

    public void addDefinition(org.apache.ambari.server.orm.entities.ViewEntity definition) {
        viewDefinitions.put(definition.getName(), definition);
    }

    public java.util.Collection<org.apache.ambari.server.orm.entities.ViewInstanceEntity> getInstanceDefinitions(org.apache.ambari.server.orm.entities.ViewEntity definition) {
        if (definition != null) {
            java.util.Map<java.lang.String, org.apache.ambari.server.orm.entities.ViewInstanceEntity> instanceEntityMap = viewInstanceDefinitions.get(definition);
            if (instanceEntityMap != null) {
                return instanceEntityMap.values();
            }
        }
        return java.util.Collections.emptyList();
    }

    public org.apache.ambari.server.orm.entities.ViewInstanceEntity getInstanceDefinition(java.lang.String viewName, java.lang.String version, java.lang.String instanceName) {
        java.util.Map<java.lang.String, org.apache.ambari.server.orm.entities.ViewInstanceEntity> viewInstanceDefinitionMap = viewInstanceDefinitions.get(getDefinition(viewName, version));
        return viewInstanceDefinitionMap == null ? null : viewInstanceDefinitionMap.get(instanceName);
    }

    public void addInstanceDefinition(org.apache.ambari.server.orm.entities.ViewEntity definition, org.apache.ambari.server.orm.entities.ViewInstanceEntity instanceDefinition) {
        java.util.Map<java.lang.String, org.apache.ambari.server.orm.entities.ViewInstanceEntity> instanceDefinitions = viewInstanceDefinitions.get(definition);
        if (instanceDefinitions == null) {
            instanceDefinitions = new java.util.HashMap<>();
            viewInstanceDefinitions.put(definition, instanceDefinitions);
        }
        org.apache.ambari.view.View view = definition.getView();
        if (view != null) {
            view.onCreate(instanceDefinition);
        }
        instanceDefinitions.put(instanceDefinition.getName(), instanceDefinition);
    }

    public void removeInstanceDefinition(org.apache.ambari.server.orm.entities.ViewEntity definition, java.lang.String instanceName) {
        java.util.Map<java.lang.String, org.apache.ambari.server.orm.entities.ViewInstanceEntity> instanceDefinitions = viewInstanceDefinitions.get(definition);
        if (instanceDefinitions != null) {
            org.apache.ambari.server.orm.entities.ViewInstanceEntity instanceDefinition = instanceDefinitions.get(instanceName);
            if (instanceDefinition != null) {
                org.apache.ambari.view.View view = definition.getView();
                if (view != null) {
                    view.onDestroy(instanceDefinition);
                }
                instanceDefinitions.remove(instanceName);
            }
        }
    }

    public static void initInstance(org.apache.ambari.server.view.ViewRegistry singleton) {
        org.apache.ambari.server.view.ViewRegistry.singleton = singleton;
    }

    public static org.apache.ambari.server.view.ViewRegistry getInstance() {
        return org.apache.ambari.server.view.ViewRegistry.singleton;
    }

    public java.util.Set<org.apache.ambari.server.api.resources.SubResourceDefinition> getSubResourceDefinitions(java.lang.String viewName, java.lang.String version) {
        viewName = org.apache.ambari.server.orm.entities.ViewEntity.getViewName(viewName, version);
        return subResourceDefinitionsMap.get(viewName);
    }

    public void readViewArchives() {
        boolean systemViewsOnly = configuration.extractViewsAfterClusterConfig() && clustersProvider.get().getClusters().isEmpty();
        org.apache.ambari.server.view.ViewRegistry.LOG.info("Triggering loading of [{}] views", systemViewsOnly ? "SYSTEM" : "ALL");
        readViewArchives(systemViewsOnly, false, org.apache.ambari.server.view.ViewRegistry.ALL_VIEWS_REG_EXP);
    }

    public void readViewArchives(java.lang.String viewNameRegExp) {
        readViewArchives(false, false, viewNameRegExp);
    }

    public boolean instanceExists(org.apache.ambari.server.orm.entities.ViewInstanceEntity instanceEntity) {
        org.apache.ambari.server.orm.entities.ViewEntity viewEntity = getDefinition(instanceEntity.getViewName());
        return (viewEntity != null) && (getInstanceDefinition(viewEntity.getCommonName(), viewEntity.getVersion(), instanceEntity.getName()) != null);
    }

    public void installViewInstance(org.apache.ambari.server.orm.entities.ViewInstanceEntity instanceEntity) throws org.apache.ambari.server.view.validation.ValidationException, java.lang.IllegalArgumentException, org.apache.ambari.view.SystemException {
        org.apache.ambari.server.orm.entities.ViewEntity viewEntity = getDefinition(instanceEntity.getViewName());
        if (viewEntity != null) {
            java.lang.String instanceName = instanceEntity.getName();
            java.lang.String viewName = viewEntity.getCommonName();
            java.lang.String version = viewEntity.getVersion();
            if (getInstanceDefinition(viewName, version, instanceName) == null) {
                if (org.apache.ambari.server.view.ViewRegistry.LOG.isDebugEnabled()) {
                    org.apache.ambari.server.view.ViewRegistry.LOG.debug("Creating view instance {}/{}/{}", viewName, version, instanceName);
                }
                instanceEntity.validate(viewEntity, org.apache.ambari.view.validation.Validator.ValidationContext.PRE_CREATE);
                org.apache.ambari.server.view.ViewRegistry.setPersistenceEntities(instanceEntity);
                org.apache.ambari.server.orm.entities.ViewInstanceEntity persistedInstance = mergeViewInstance(instanceEntity, viewEntity.getResourceType());
                instanceEntity.setViewInstanceId(persistedInstance.getViewInstanceId());
                syncViewInstance(instanceEntity, persistedInstance);
                try {
                    bindViewInstance(viewEntity, instanceEntity);
                } catch (java.lang.Exception e) {
                    java.lang.String message = "Caught exception installing view instance.";
                    org.apache.ambari.server.view.ViewRegistry.LOG.error(message, e);
                    throw new java.lang.IllegalStateException(message, e);
                }
                addInstanceDefinition(viewEntity, instanceEntity);
                handlerList.addViewInstance(instanceEntity);
            }
        } else {
            java.lang.String message = ("Attempt to install an instance for an unknown view " + instanceEntity.getViewName()) + ".";
            org.apache.ambari.server.view.ViewRegistry.LOG.error(message);
            throw new java.lang.IllegalArgumentException(message);
        }
    }

    public void updateViewInstance(org.apache.ambari.server.orm.entities.ViewInstanceEntity instanceEntity) throws org.apache.ambari.server.view.validation.ValidationException, org.apache.ambari.view.SystemException {
        org.apache.ambari.server.orm.entities.ViewEntity viewEntity = getDefinition(instanceEntity.getViewName());
        if (viewEntity != null) {
            instanceEntity.validate(viewEntity, org.apache.ambari.view.validation.Validator.ValidationContext.PRE_UPDATE);
            instanceDAO.merge(instanceEntity);
            syncViewInstance(instanceEntity);
        }
    }

    public void updateView(org.apache.ambari.server.orm.entities.ViewInstanceEntity instanceEntity) {
        org.apache.ambari.server.orm.entities.ViewEntity viewEntity = getDefinition(instanceEntity.getViewName());
        if ((null != viewEntity) && (null != viewEntity.getView())) {
            viewEntity.getView().onUpdate(instanceEntity);
        }
    }

    public org.apache.ambari.server.orm.entities.ViewInstanceEntity getViewInstanceEntity(java.lang.String viewName, java.lang.String instanceName) {
        return instanceDAO.findByName(viewName, instanceName);
    }

    @com.google.inject.persist.Transactional
    public void uninstallViewInstance(org.apache.ambari.server.orm.entities.ViewInstanceEntity instanceEntity) throws java.lang.IllegalStateException {
        try {
            viewInstanceOperationHandler.uninstallViewInstance(instanceEntity);
            updateCaches(instanceEntity);
        } catch (java.lang.IllegalStateException illegalStateExcpetion) {
            org.apache.ambari.server.view.ViewRegistry.LOG.error("Exception occurred while uninstalling view : {}", instanceEntity, illegalStateExcpetion);
            throw illegalStateExcpetion;
        }
    }

    private void updateCaches(org.apache.ambari.server.orm.entities.ViewInstanceEntity instanceEntity) {
        org.apache.ambari.server.orm.entities.ViewEntity viewEntity = getDefinition(instanceEntity.getViewName());
        viewEntity.removeInstanceDefinition(instanceEntity.getInstanceName());
        removeInstanceDefinition(viewEntity, instanceEntity.getInstanceName());
        handlerList.removeViewInstance(instanceEntity);
    }

    @com.google.inject.persist.Transactional
    public void removeInstanceData(org.apache.ambari.server.orm.entities.ViewInstanceEntity instanceEntity, java.lang.String key) {
        org.apache.ambari.server.orm.entities.ViewInstanceDataEntity dataEntity = instanceEntity.getInstanceData(key);
        if (dataEntity != null) {
            instanceDAO.removeData(dataEntity);
        }
        instanceEntity.removeInstanceData(key);
        instanceDAO.merge(instanceEntity);
    }

    @com.google.inject.persist.Transactional
    public void copyPrivileges(org.apache.ambari.server.orm.entities.ViewInstanceEntity sourceInstanceEntity, org.apache.ambari.server.orm.entities.ViewInstanceEntity targetInstanceEntity) {
        org.apache.ambari.server.view.ViewRegistry.LOG.debug("Copy all privileges from {} to {}", sourceInstanceEntity.getName(), targetInstanceEntity.getName());
        java.util.List<org.apache.ambari.server.orm.entities.PrivilegeEntity> targetInstancePrivileges = privilegeDAO.findByResourceId(targetInstanceEntity.getResource().getId());
        if (targetInstancePrivileges.size() > 0) {
            org.apache.ambari.server.view.ViewRegistry.LOG.warn("Target instance {} already has privileges assigned, these will not be deleted. Manual clean up may be needed", targetInstanceEntity.getName());
        }
        java.util.List<org.apache.ambari.server.orm.entities.PrivilegeEntity> sourceInstancePrivileges = privilegeDAO.findByResourceId(sourceInstanceEntity.getResource().getId());
        for (org.apache.ambari.server.orm.entities.PrivilegeEntity sourcePrivilege : sourceInstancePrivileges) {
            org.apache.ambari.server.orm.entities.PrivilegeEntity targetPrivilege = new org.apache.ambari.server.orm.entities.PrivilegeEntity();
            targetPrivilege.setPrincipal(sourcePrivilege.getPrincipal());
            targetPrivilege.setResource(targetInstanceEntity.getResource());
            targetPrivilege.setPermission(sourcePrivilege.getPermission());
            try {
                privilegeDAO.create(targetPrivilege);
                targetPrivilege.getPrincipal().getPrivileges().add(sourcePrivilege);
            } catch (java.lang.Exception e) {
                org.apache.ambari.server.view.ViewRegistry.LOG.warn("Could not migrate privilege {} ", targetPrivilege);
                org.apache.ambari.server.view.ViewRegistry.LOG.error("Caught exception", e);
            }
        }
    }

    public void fireEvent(org.apache.ambari.view.events.Event event) {
        org.apache.ambari.view.ViewDefinition subject = event.getViewSubject();
        fireEvent(event, subject.getViewName());
        fireEvent(event, org.apache.ambari.server.orm.entities.ViewEntity.getViewName(subject.getViewName(), subject.getVersion()));
    }

    public synchronized void registerListener(org.apache.ambari.view.events.Listener listener, java.lang.String viewName, java.lang.String viewVersion) {
        java.lang.String name = (viewVersion == null) ? viewName : org.apache.ambari.server.orm.entities.ViewEntity.getViewName(viewName, viewVersion);
        java.util.Set<org.apache.ambari.view.events.Listener> listeners = this.listeners.get(name);
        if (listeners == null) {
            listeners = com.google.common.collect.Sets.newSetFromMap(new java.util.concurrent.ConcurrentHashMap<org.apache.ambari.view.events.Listener, java.lang.Boolean>());
            this.listeners.put(name, listeners);
        }
        listeners.add(listener);
    }

    public synchronized void unregisterListener(org.apache.ambari.view.events.Listener listener, java.lang.String viewName, java.lang.String viewVersion) {
        java.lang.String name = (viewVersion == null) ? viewName : org.apache.ambari.server.orm.entities.ViewEntity.getViewName(viewName, viewVersion);
        java.util.Set<org.apache.ambari.view.events.Listener> listeners = this.listeners.get(name);
        if (listeners != null) {
            listeners.remove(listener);
        }
    }

    public boolean hasPermission(org.apache.ambari.server.orm.entities.PermissionEntity permissionEntity, org.apache.ambari.server.orm.entities.ResourceEntity resourceEntity, java.lang.String userName) {
        org.apache.ambari.server.orm.entities.UserEntity userEntity = userDAO.findUserByName(userName);
        if (userEntity == null) {
            return false;
        }
        if (privilegeDAO.exists(userEntity.getPrincipal(), resourceEntity, permissionEntity)) {
            return true;
        }
        java.util.List<org.apache.ambari.server.orm.entities.MemberEntity> memberEntities = memberDAO.findAllMembersByUser(userEntity);
        for (org.apache.ambari.server.orm.entities.MemberEntity memberEntity : memberEntities) {
            org.apache.ambari.server.orm.entities.GroupEntity groupEntity = memberEntity.getGroup();
            if (privilegeDAO.exists(groupEntity.getPrincipal(), resourceEntity, permissionEntity)) {
                return true;
            }
        }
        return false;
    }

    public boolean checkPermission(java.lang.String viewName, java.lang.String version, java.lang.String instanceName, boolean readOnly) {
        org.apache.ambari.server.orm.entities.ViewInstanceEntity instanceEntity = (instanceName == null) ? null : getInstanceDefinition(viewName, version, instanceName);
        return checkPermission(instanceEntity, readOnly);
    }

    public boolean checkPermission(org.apache.ambari.server.orm.entities.ViewInstanceEntity instanceEntity, boolean readOnly) {
        org.apache.ambari.server.orm.entities.ResourceEntity resourceEntity = (instanceEntity == null) ? null : instanceEntity.getResource();
        return ((resourceEntity == null) && readOnly) || checkAuthorization(resourceEntity);
    }

    public boolean checkAdmin() {
        return checkAuthorization(null);
    }

    public boolean includeDefinition(org.apache.ambari.server.orm.entities.ViewEntity definitionEntity) {
        if (checkPermission(null, false)) {
            return true;
        }
        for (org.apache.ambari.server.orm.entities.ViewInstanceEntity instanceEntity : definitionEntity.getInstances()) {
            if (checkPermission(instanceEntity, true)) {
                return true;
            }
        }
        return false;
    }

    public void setViewInstanceProperties(org.apache.ambari.server.orm.entities.ViewInstanceEntity instanceEntity, java.util.Map<java.lang.String, java.lang.String> properties, org.apache.ambari.server.view.configuration.ViewConfig viewConfig, java.lang.ClassLoader classLoader) throws org.apache.ambari.view.SystemException {
        try {
            org.apache.ambari.view.Masker masker = org.apache.ambari.server.view.ViewRegistry.getMasker(viewConfig.getMaskerClass(classLoader));
            java.util.Map<java.lang.String, org.apache.ambari.server.view.configuration.ParameterConfig> parameterConfigMap = new java.util.HashMap<>();
            for (org.apache.ambari.server.view.configuration.ParameterConfig paramConfig : viewConfig.getParameters()) {
                parameterConfigMap.put(paramConfig.getName(), paramConfig);
            }
            for (java.util.Map.Entry<java.lang.String, java.lang.String> entry : properties.entrySet()) {
                java.lang.String name = entry.getKey();
                java.lang.String value = entry.getValue();
                org.apache.ambari.server.view.configuration.ParameterConfig parameterConfig = parameterConfigMap.get(name);
                if ((parameterConfig != null) && parameterConfig.isMasked()) {
                    value = masker.mask(value);
                }
                instanceEntity.putProperty(name, value);
            }
        } catch (java.lang.Exception e) {
            throw new org.apache.ambari.view.SystemException("Caught exception while setting instance property.", e);
        }
    }

    public org.apache.ambari.view.cluster.Cluster getCluster(org.apache.ambari.view.ViewInstanceDefinition viewInstance) {
        if (viewInstance != null) {
            java.lang.Long clusterId = viewInstance.getClusterHandle();
            if ((clusterId != null) && (viewInstance.getClusterType() == org.apache.ambari.view.ClusterType.LOCAL_AMBARI)) {
                try {
                    return new org.apache.ambari.server.view.ClusterImpl(clustersProvider.get().getCluster(clusterId));
                } catch (org.apache.ambari.server.AmbariException e) {
                    org.apache.ambari.server.view.ViewRegistry.LOG.error("Could not find the cluster identified by {}.", clusterId);
                    throw new org.apache.ambari.server.view.IllegalClusterException(e);
                }
            } else if ((clusterId != null) && (viewInstance.getClusterType() == org.apache.ambari.view.ClusterType.REMOTE_AMBARI)) {
                try {
                    return remoteAmbariClusterRegistry.get(clusterId);
                } catch (java.net.MalformedURLException e) {
                    org.apache.ambari.server.view.ViewRegistry.LOG.error("Remote Cluster with id={} had invalid URL.", clusterId, e);
                    throw new org.apache.ambari.server.view.IllegalClusterException(e);
                } catch (org.apache.ambari.server.ClusterNotFoundException e) {
                    org.apache.ambari.server.view.ViewRegistry.LOG.error("Cannot get Remote Cluster with id={}.", clusterId, e);
                    throw new org.apache.ambari.server.view.IllegalClusterException(e);
                }
            }
        }
        return null;
    }

    @com.google.common.eventbus.Subscribe
    @com.google.common.eventbus.AllowConcurrentEvents
    public void onAmbariEvent(org.apache.ambari.server.events.ServiceInstalledEvent event) {
        org.apache.ambari.server.state.Clusters clusters = clustersProvider.get();
        java.lang.Long clusterId = event.getClusterId();
        try {
            org.apache.ambari.server.state.Cluster cluster = clusters.getClusterById(clusterId);
            java.lang.String clusterName = cluster.getClusterName();
            java.util.Set<org.apache.ambari.server.state.StackId> stackIds = new java.util.HashSet<>();
            java.util.Set<java.lang.String> serviceNames = cluster.getServices().keySet();
            for (java.lang.String serviceName : serviceNames) {
                org.apache.ambari.server.state.Service service = cluster.getService(serviceName);
                stackIds.add(service.getDesiredStackId());
            }
            for (org.apache.ambari.server.orm.entities.ViewEntity viewEntity : getDefinitions()) {
                java.lang.String viewName = viewEntity.getName();
                org.apache.ambari.server.view.configuration.ViewConfig viewConfig = viewEntity.getConfiguration();
                org.apache.ambari.server.view.configuration.AutoInstanceConfig autoConfig = viewConfig.getAutoInstance();
                java.util.Collection<java.lang.String> roles = com.google.common.collect.Lists.newArrayList();
                if ((autoConfig != null) && (!org.apache.commons.collections.CollectionUtils.isEmpty(autoConfig.getRoles()))) {
                    roles.addAll(autoConfig.getRoles());
                }
                for (org.apache.ambari.server.state.StackId stackId : stackIds) {
                    try {
                        if (checkAutoInstanceConfig(autoConfig, stackId, event.getServiceName(), serviceNames)) {
                            installAutoInstance(clusterId, clusterName, cluster.getService(event.getServiceName()), viewEntity, viewName, viewConfig, autoConfig, roles);
                        }
                    } catch (java.lang.Exception e) {
                        org.apache.ambari.server.view.ViewRegistry.LOG.error((((("Can't auto create instance of view " + viewName) + " for cluster ") + clusterName) + ".  Caught exception :") + e.getMessage(), e);
                    }
                }
            }
        } catch (org.apache.ambari.server.AmbariException e) {
            org.apache.ambari.server.view.ViewRegistry.LOG.warn(("Unknown cluster id " + clusterId) + ".");
        }
    }

    private void installAutoInstance(java.lang.Long clusterId, java.lang.String clusterName, org.apache.ambari.server.state.Service service, org.apache.ambari.server.orm.entities.ViewEntity viewEntity, java.lang.String viewName, org.apache.ambari.server.view.configuration.ViewConfig viewConfig, org.apache.ambari.server.view.configuration.AutoInstanceConfig autoConfig, java.util.Collection<java.lang.String> roles) throws org.apache.ambari.view.SystemException, org.apache.ambari.server.view.validation.ValidationException {
        org.apache.ambari.server.view.ViewRegistry.LOG.info(((("Auto creating instance of view " + viewName) + " for cluster ") + clusterName) + ".");
        org.apache.ambari.server.orm.entities.ViewInstanceEntity viewInstanceEntity = createViewInstanceEntity(viewEntity, viewConfig, autoConfig);
        updateHiveLLAPSettingsIfRequired(viewInstanceEntity, service);
        viewInstanceEntity.setClusterHandle(clusterId);
        installViewInstance(viewInstanceEntity);
        setViewInstanceRoleAccess(viewInstanceEntity, roles);
        try {
            setViewUrl(viewInstanceEntity);
        } catch (java.lang.Exception urlCreateException) {
            org.apache.ambari.server.view.ViewRegistry.LOG.error("Error while creating an auto URL for the view instance {}, Url should be created in view instance settings", viewInstanceEntity.getViewName());
            org.apache.ambari.server.view.ViewRegistry.LOG.error("View URL creation error ", urlCreateException);
        }
    }

    private void updateHiveLLAPSettingsIfRequired(org.apache.ambari.server.orm.entities.ViewInstanceEntity viewInstanceEntity, org.apache.ambari.server.state.Service service) {
        java.lang.String INTERACTIVE_KEY = "use.hive.interactive.mode";
        java.lang.String LLAP_COMPONENT_NAME = "HIVE_SERVER_INTERACTIVE";
        java.lang.String viewVersion = viewInstanceEntity.getViewDefinition().getVersion();
        java.lang.String viewName = viewInstanceEntity.getViewDefinition().getViewName();
        if ((!viewName.equalsIgnoreCase("HIVE")) || viewVersion.equalsIgnoreCase("1.0.0")) {
            return;
        }
        try {
            org.apache.ambari.server.state.ServiceComponent component = service.getServiceComponent(LLAP_COMPONENT_NAME);
            if (component.getServiceComponentHosts().size() == 0) {
                return;
            }
            for (java.util.Map.Entry<java.lang.String, java.lang.String> property : viewInstanceEntity.getPropertyMap().entrySet()) {
                if (INTERACTIVE_KEY.equals(property.getKey()) && (!"true".equalsIgnoreCase(property.getValue()))) {
                    org.apache.ambari.server.orm.entities.ViewInstancePropertyEntity propertyEntity = new org.apache.ambari.server.orm.entities.ViewInstancePropertyEntity();
                    propertyEntity.setViewInstanceName(viewInstanceEntity.getName());
                    propertyEntity.setViewName(viewInstanceEntity.getViewName());
                    propertyEntity.setName(INTERACTIVE_KEY);
                    propertyEntity.setValue("true");
                    propertyEntity.setViewInstanceEntity(viewInstanceEntity);
                    viewInstanceEntity.getProperties().add(propertyEntity);
                }
            }
        } catch (org.apache.ambari.server.AmbariException e) {
            org.apache.ambari.server.view.ViewRegistry.LOG.error("Failed to update '{}' parameter for viewName: {}, version: {}. Exception: {}", INTERACTIVE_KEY, viewName, viewVersion, e);
        }
    }

    private java.lang.String getUrlName(org.apache.ambari.server.orm.entities.ViewInstanceEntity viewInstanceEntity) {
        return (viewInstanceEntity.getViewEntity().getCommonName().toLowerCase() + "_") + viewInstanceEntity.getInstanceName().toLowerCase();
    }

    private void setViewUrl(org.apache.ambari.server.orm.entities.ViewInstanceEntity instanceEntity) {
        org.apache.ambari.server.orm.entities.ViewInstanceEntity viewInstanceEntity = instanceDAO.findByName(instanceEntity.getViewName(), instanceEntity.getInstanceName());
        com.google.common.base.Preconditions.checkNotNull(viewInstanceEntity);
        org.apache.ambari.server.orm.entities.ViewURLEntity viewUrl = viewInstanceEntity.getViewUrl();
        if (viewUrl != null) {
            org.apache.ambari.server.view.ViewRegistry.LOG.warn("Url exists for the auto instance {}, new url will not be created", viewInstanceEntity.getViewName());
            return;
        }
        java.lang.String urlName = getUrlName(viewInstanceEntity);
        com.google.common.base.Optional<org.apache.ambari.server.orm.entities.ViewURLEntity> existingUrl = viewURLDAO.findByName(urlName);
        org.apache.ambari.server.orm.entities.ViewURLEntity urlEntity = new org.apache.ambari.server.orm.entities.ViewURLEntity();
        urlEntity.setUrlName(urlName);
        urlEntity.setUrlSuffix(viewInstanceEntity.getInstanceName().toLowerCase());
        org.apache.ambari.server.orm.entities.ViewURLEntity toSaveOrUpdate = existingUrl.or(urlEntity);
        toSaveOrUpdate.setViewInstanceEntity(viewInstanceEntity);
        if (existingUrl.isPresent()) {
            org.apache.ambari.server.view.ViewRegistry.LOG.info("Url already present for {}", viewInstanceEntity.getViewName());
            viewURLDAO.update(toSaveOrUpdate);
        } else {
            org.apache.ambari.server.view.ViewRegistry.LOG.info("Creating a new URL for auto instance {}", viewInstanceEntity.getViewName());
            viewURLDAO.save(urlEntity);
        }
        viewInstanceEntity.setViewUrl(urlEntity);
        try {
            updateViewInstance(viewInstanceEntity);
        } catch (java.lang.Exception ex) {
            org.apache.ambari.server.view.ViewRegistry.LOG.error("Could not update the view instance with new URL, removing URL", ex);
            com.google.common.base.Optional<org.apache.ambari.server.orm.entities.ViewURLEntity> viewURLDAOByName = viewURLDAO.findByName(urlName);
            if (viewURLDAOByName.isPresent())
                viewURLDAO.delete(viewURLDAOByName.get());

        }
    }

    @com.google.common.eventbus.Subscribe
    public void onClusterConfigFinishedEvent(org.apache.ambari.server.events.ClusterConfigFinishedEvent event) {
        if (configuration.extractViewsAfterClusterConfig()) {
            org.apache.ambari.server.view.ViewRegistry.LOG.info("Trigger extracting NON-SYSTEM views; cluster [{}] ...", event.getClusterName());
            readNonSystemViewViewArchives();
            org.apache.ambari.server.view.ViewRegistry.LOG.info("Trigger extracting NON-SYSTEM views; cluster [{}] DONE.", event.getClusterName());
        }
    }

    private boolean checkAutoInstanceConfig(org.apache.ambari.server.view.configuration.AutoInstanceConfig autoConfig, org.apache.ambari.server.state.StackId stackId, java.lang.String serviceName, java.util.Set<java.lang.String> serviceNames) {
        if (autoConfig != null) {
            java.util.List<java.lang.String> autoCreateServices = autoConfig.getServices();
            if (((autoCreateServices != null) && autoCreateServices.contains(serviceName)) && serviceNames.containsAll(autoCreateServices)) {
                java.lang.String configStackId = autoConfig.getStackId();
                if (configStackId != null) {
                    if (configStackId.equals("*")) {
                        return true;
                    }
                    org.apache.ambari.server.state.StackId id = new org.apache.ambari.server.state.StackId(configStackId);
                    if (id.getStackName().equals(stackId.getStackName())) {
                        java.lang.String stackVersion = stackId.getStackVersion();
                        java.lang.String configStackVersion = id.getStackVersion();
                        int compVal = 0;
                        int index = configStackVersion.indexOf('*');
                        if (index == (-1)) {
                            compVal = org.apache.ambari.server.utils.VersionUtils.compareVersions(configStackVersion, stackVersion);
                        } else if (index > 0) {
                            java.lang.String[] parts = configStackVersion.substring(0, index).split("\\.");
                            compVal = org.apache.ambari.server.utils.VersionUtils.compareVersions(configStackVersion, stackVersion, parts.length);
                        }
                        return compVal == 0;
                    }
                }
            }
        }
        return false;
    }

    protected void clear() {
        viewDefinitions.clear();
        viewInstanceDefinitions.clear();
        subResourceDefinitionsMap.clear();
        listeners.clear();
    }

    protected java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, org.apache.ambari.server.controller.spi.ResourceProvider> getResourceProviders() {
        return resourceProviders;
    }

    private org.apache.ambari.server.orm.entities.ViewEntity getDefinition(java.lang.String viewName) {
        return viewDefinitions.get(viewName);
    }

    protected org.apache.ambari.server.orm.entities.ViewEntity setupViewDefinition(org.apache.ambari.server.orm.entities.ViewEntity viewDefinition, java.lang.ClassLoader cl) throws java.lang.ClassNotFoundException, java.beans.IntrospectionException {
        org.apache.ambari.server.view.configuration.ViewConfig viewConfig = viewDefinition.getConfiguration();
        viewDefinition.setClassLoader(cl);
        java.util.List<org.apache.ambari.server.view.configuration.ParameterConfig> parameterConfigurations = viewConfig.getParameters();
        java.util.Collection<org.apache.ambari.server.orm.entities.ViewParameterEntity> parameters = new java.util.HashSet<>();
        java.lang.String viewName = viewDefinition.getName();
        for (org.apache.ambari.server.view.configuration.ParameterConfig parameterConfiguration : parameterConfigurations) {
            org.apache.ambari.server.orm.entities.ViewParameterEntity viewParameterEntity = new org.apache.ambari.server.orm.entities.ViewParameterEntity();
            viewParameterEntity.setViewName(viewName);
            viewParameterEntity.setName(parameterConfiguration.getName());
            viewParameterEntity.setDescription(parameterConfiguration.getDescription());
            viewParameterEntity.setLabel(parameterConfiguration.getLabel());
            viewParameterEntity.setPlaceholder(parameterConfiguration.getPlaceholder());
            viewParameterEntity.setDefaultValue(parameterConfiguration.getDefaultValue());
            viewParameterEntity.setClusterConfig(parameterConfiguration.getClusterConfig());
            viewParameterEntity.setRequired(parameterConfiguration.isRequired());
            viewParameterEntity.setMasked(parameterConfiguration.isMasked());
            viewParameterEntity.setViewEntity(viewDefinition);
            parameters.add(viewParameterEntity);
        }
        viewDefinition.setParameters(parameters);
        java.util.List<org.apache.ambari.server.view.configuration.ResourceConfig> resourceConfigurations = viewConfig.getResources();
        org.apache.ambari.server.controller.spi.Resource.Type externalResourceType = viewDefinition.getExternalResourceType();
        org.apache.ambari.server.view.ViewExternalSubResourceProvider viewExternalSubResourceProvider = new org.apache.ambari.server.view.ViewExternalSubResourceProvider(externalResourceType, viewDefinition);
        viewDefinition.addResourceProvider(externalResourceType, viewExternalSubResourceProvider);
        resourceProviders.put(externalResourceType, viewExternalSubResourceProvider);
        org.apache.ambari.server.api.resources.ResourceInstanceFactoryImpl.addResourceDefinition(externalResourceType, new org.apache.ambari.server.api.resources.ViewExternalSubResourceDefinition(externalResourceType));
        java.util.Collection<org.apache.ambari.server.orm.entities.ViewResourceEntity> resources = new java.util.HashSet<>();
        for (org.apache.ambari.server.view.configuration.ResourceConfig resourceConfiguration : resourceConfigurations) {
            org.apache.ambari.server.orm.entities.ViewResourceEntity viewResourceEntity = new org.apache.ambari.server.orm.entities.ViewResourceEntity();
            viewResourceEntity.setViewName(viewName);
            viewResourceEntity.setName(resourceConfiguration.getName());
            viewResourceEntity.setPluralName(resourceConfiguration.getPluralName());
            viewResourceEntity.setIdProperty(resourceConfiguration.getIdProperty());
            viewResourceEntity.setResource(resourceConfiguration.getResource());
            viewResourceEntity.setService(resourceConfiguration.getService());
            viewResourceEntity.setProvider(resourceConfiguration.getProvider());
            viewResourceEntity.setSubResourceNames(resourceConfiguration.getSubResourceNames());
            viewResourceEntity.setViewEntity(viewDefinition);
            org.apache.ambari.server.view.ViewSubResourceDefinition resourceDefinition = new org.apache.ambari.server.view.ViewSubResourceDefinition(viewDefinition, resourceConfiguration);
            viewDefinition.addResourceDefinition(resourceDefinition);
            org.apache.ambari.server.controller.spi.Resource.Type type = resourceDefinition.getType();
            viewDefinition.addResourceConfiguration(type, resourceConfiguration);
            if (resourceConfiguration.isExternal()) {
                viewExternalSubResourceProvider.addResourceName(resourceConfiguration.getName());
            } else {
                org.apache.ambari.server.api.resources.ResourceInstanceFactoryImpl.addResourceDefinition(type, resourceDefinition);
                java.lang.Class<?> clazz = resourceConfiguration.getResourceClass(cl);
                java.lang.String idProperty = resourceConfiguration.getIdProperty();
                org.apache.ambari.server.view.ViewSubResourceProvider provider = new org.apache.ambari.server.view.ViewSubResourceProvider(type, clazz, idProperty, viewDefinition);
                viewDefinition.addResourceProvider(type, provider);
                resourceProviders.put(type, provider);
                resources.add(viewResourceEntity);
            }
            viewDefinition.setResources(resources);
        }
        org.apache.ambari.server.orm.entities.ResourceTypeEntity resourceTypeEntity = new org.apache.ambari.server.orm.entities.ResourceTypeEntity();
        resourceTypeEntity.setName(viewName);
        viewDefinition.setResourceType(resourceTypeEntity);
        java.util.List<org.apache.ambari.server.view.configuration.PermissionConfig> permissionConfigurations = viewConfig.getPermissions();
        java.util.Collection<org.apache.ambari.server.orm.entities.PermissionEntity> permissions = new java.util.HashSet<>();
        for (org.apache.ambari.server.view.configuration.PermissionConfig permissionConfiguration : permissionConfigurations) {
            org.apache.ambari.server.orm.entities.PermissionEntity permissionEntity = new org.apache.ambari.server.orm.entities.PermissionEntity();
            permissionEntity.setPermissionName(permissionConfiguration.getName());
            permissionEntity.setResourceType(resourceTypeEntity);
            permissions.add(permissionEntity);
        }
        viewDefinition.setPermissions(permissions);
        org.apache.ambari.view.View view = null;
        if (viewConfig.getView() != null) {
            view = org.apache.ambari.server.view.ViewRegistry.getView(viewConfig.getViewClass(cl), new org.apache.ambari.server.view.ViewContextImpl(viewDefinition, this));
        }
        viewDefinition.setView(view);
        org.apache.ambari.view.validation.Validator validator = null;
        if (viewConfig.getValidator() != null) {
            validator = org.apache.ambari.server.view.ViewRegistry.getValidator(viewConfig.getValidatorClass(cl), new org.apache.ambari.server.view.ViewContextImpl(viewDefinition, this));
        }
        viewDefinition.setValidator(validator);
        viewDefinition.setMask(viewConfig.getMasker());
        java.util.Set<org.apache.ambari.server.api.resources.SubResourceDefinition> subResourceDefinitions = new java.util.HashSet<>();
        for (org.apache.ambari.server.controller.spi.Resource.Type type : viewDefinition.getViewResourceTypes()) {
            subResourceDefinitions.add(new org.apache.ambari.server.api.resources.SubResourceDefinition(type));
        }
        subResourceDefinitionsMap.put(viewName, subResourceDefinitions);
        return viewDefinition;
    }

    protected org.apache.ambari.server.orm.entities.ViewInstanceEntity createViewInstanceDefinition(org.apache.ambari.server.view.configuration.ViewConfig viewConfig, org.apache.ambari.server.orm.entities.ViewEntity viewDefinition, org.apache.ambari.server.view.configuration.InstanceConfig instanceConfig) throws org.apache.ambari.server.view.validation.ValidationException, java.lang.ClassNotFoundException, org.apache.ambari.view.SystemException {
        org.apache.ambari.server.orm.entities.ViewInstanceEntity viewInstanceDefinition = createViewInstanceEntity(viewDefinition, viewConfig, instanceConfig);
        viewInstanceDefinition.validate(viewDefinition, org.apache.ambari.view.validation.Validator.ValidationContext.PRE_CREATE);
        bindViewInstance(viewDefinition, viewInstanceDefinition);
        return viewInstanceDefinition;
    }

    private org.apache.ambari.server.orm.entities.ViewInstanceEntity createViewInstanceEntity(org.apache.ambari.server.orm.entities.ViewEntity viewDefinition, org.apache.ambari.server.view.configuration.ViewConfig viewConfig, org.apache.ambari.server.view.configuration.InstanceConfig instanceConfig) throws org.apache.ambari.view.SystemException {
        org.apache.ambari.server.orm.entities.ViewInstanceEntity viewInstanceDefinition = new org.apache.ambari.server.orm.entities.ViewInstanceEntity(viewDefinition, instanceConfig);
        java.util.Map<java.lang.String, java.lang.String> properties = new java.util.HashMap<>();
        for (org.apache.ambari.server.view.configuration.PropertyConfig propertyConfig : instanceConfig.getProperties()) {
            properties.put(propertyConfig.getKey(), propertyConfig.getValue());
        }
        setViewInstanceProperties(viewInstanceDefinition, properties, viewConfig, viewDefinition.getClassLoader());
        org.apache.ambari.server.view.ViewRegistry.setPersistenceEntities(viewInstanceDefinition);
        return viewInstanceDefinition;
    }

    protected void bindViewInstance(org.apache.ambari.server.orm.entities.ViewEntity viewDefinition, org.apache.ambari.server.orm.entities.ViewInstanceEntity viewInstanceDefinition) throws java.lang.ClassNotFoundException {
        viewInstanceDefinition.setViewEntity(viewDefinition);
        org.apache.ambari.view.ViewContext viewInstanceContext = new org.apache.ambari.server.view.ViewContextImpl(viewInstanceDefinition, this);
        org.apache.ambari.server.api.services.views.ViewExternalSubResourceService externalSubResourceService = new org.apache.ambari.server.api.services.views.ViewExternalSubResourceService(viewDefinition.getExternalResourceType(), viewInstanceDefinition);
        viewInstanceDefinition.addService(org.apache.ambari.server.view.configuration.ResourceConfig.EXTERNAL_RESOURCE_PLURAL_NAME, externalSubResourceService);
        java.util.Collection<org.apache.ambari.server.view.ViewSubResourceDefinition> resourceDefinitions = viewDefinition.getResourceDefinitions().values();
        for (org.apache.ambari.server.view.ViewSubResourceDefinition resourceDefinition : resourceDefinitions) {
            org.apache.ambari.server.controller.spi.Resource.Type type = resourceDefinition.getType();
            org.apache.ambari.server.view.configuration.ResourceConfig resourceConfig = resourceDefinition.getResourceConfiguration();
            org.apache.ambari.view.ViewResourceHandler viewResourceService = new org.apache.ambari.server.api.services.views.ViewSubResourceService(type, viewInstanceDefinition);
            java.lang.ClassLoader cl = viewDefinition.getClassLoader();
            java.lang.Object service = org.apache.ambari.server.view.ViewRegistry.getService(resourceConfig.getServiceClass(cl), viewResourceService, viewInstanceContext);
            if (resourceConfig.isExternal()) {
                externalSubResourceService.addResourceService(resourceConfig.getName(), service);
            } else {
                viewInstanceDefinition.addService(viewDefinition.getResourceDefinition(type).getPluralName(), service);
                viewInstanceDefinition.addResourceProvider(type, org.apache.ambari.server.view.ViewRegistry.getProvider(resourceConfig.getProviderClass(cl), viewInstanceContext));
            }
        }
        viewDefinition.addInstanceDefinition(viewInstanceDefinition);
    }

    private static void setPersistenceEntities(org.apache.ambari.server.orm.entities.ViewInstanceEntity viewInstanceDefinition) {
        org.apache.ambari.server.orm.entities.ViewEntity viewDefinition = viewInstanceDefinition.getViewEntity();
        org.apache.ambari.server.view.configuration.ViewConfig viewConfig = viewDefinition.getConfiguration();
        java.util.Collection<org.apache.ambari.server.orm.entities.ViewEntityEntity> entities = new java.util.HashSet<>();
        if (viewConfig != null) {
            org.apache.ambari.server.view.configuration.PersistenceConfig persistenceConfig = viewConfig.getPersistence();
            if (persistenceConfig != null) {
                for (org.apache.ambari.server.view.configuration.EntityConfig entityConfiguration : persistenceConfig.getEntities()) {
                    org.apache.ambari.server.orm.entities.ViewEntityEntity viewEntityEntity = new org.apache.ambari.server.orm.entities.ViewEntityEntity();
                    viewEntityEntity.setViewName(viewDefinition.getName());
                    viewEntityEntity.setViewInstanceName(viewInstanceDefinition.getName());
                    viewEntityEntity.setClassName(entityConfiguration.getClassName());
                    viewEntityEntity.setIdProperty(entityConfiguration.getIdProperty());
                    viewEntityEntity.setViewInstance(viewInstanceDefinition);
                    entities.add(viewEntityEntity);
                }
            }
        }
        viewInstanceDefinition.setEntities(entities);
    }

    private static <T> T getService(java.lang.Class<T> clazz, final org.apache.ambari.view.ViewResourceHandler viewResourceHandler, final org.apache.ambari.view.ViewContext viewInstanceContext) {
        com.google.inject.Injector viewInstanceInjector = com.google.inject.Guice.createInjector(new com.google.inject.AbstractModule() {
            @java.lang.Override
            protected void configure() {
                bind(org.apache.ambari.view.ViewResourceHandler.class).toInstance(viewResourceHandler);
                bind(org.apache.ambari.view.ViewContext.class).toInstance(viewInstanceContext);
            }
        });
        return viewInstanceInjector.getInstance(clazz);
    }

    private static org.apache.ambari.view.ResourceProvider getProvider(java.lang.Class<? extends org.apache.ambari.view.ResourceProvider> clazz, final org.apache.ambari.view.ViewContext viewInstanceContext) {
        com.google.inject.Injector viewInstanceInjector = com.google.inject.Guice.createInjector(new com.google.inject.AbstractModule() {
            @java.lang.Override
            protected void configure() {
                bind(org.apache.ambari.view.ViewContext.class).toInstance(viewInstanceContext);
            }
        });
        return viewInstanceInjector.getInstance(clazz);
    }

    private static org.apache.ambari.view.View getView(java.lang.Class<? extends org.apache.ambari.view.View> clazz, final org.apache.ambari.view.ViewContext viewContext) {
        com.google.inject.Injector viewInstanceInjector = com.google.inject.Guice.createInjector(new com.google.inject.AbstractModule() {
            @java.lang.Override
            protected void configure() {
                bind(org.apache.ambari.view.ViewContext.class).toInstance(viewContext);
            }
        });
        return viewInstanceInjector.getInstance(clazz);
    }

    private static org.apache.ambari.view.validation.Validator getValidator(java.lang.Class<? extends org.apache.ambari.view.validation.Validator> clazz, final org.apache.ambari.view.ViewContext viewContext) {
        com.google.inject.Injector viewInstanceInjector = com.google.inject.Guice.createInjector(new com.google.inject.AbstractModule() {
            @java.lang.Override
            protected void configure() {
                bind(org.apache.ambari.view.ViewContext.class).toInstance(viewContext);
            }
        });
        return viewInstanceInjector.getInstance(clazz);
    }

    private static org.apache.ambari.view.Masker getMasker(java.lang.Class<? extends org.apache.ambari.view.Masker> clazz) {
        try {
            return clazz.newInstance();
        } catch (java.lang.Exception e) {
            org.apache.ambari.server.view.ViewRegistry.LOG.error("Could not create masker instance", e);
        }
        return null;
    }

    private void removeUndeployedViews() {
        for (org.apache.ambari.server.orm.entities.ViewEntity viewEntity : viewDAO.findAll()) {
            java.lang.String name = viewEntity.getName();
            if (!org.apache.ambari.server.view.ViewRegistry.getInstance().viewDefinitions.containsKey(name)) {
                try {
                    viewDAO.remove(viewEntity);
                } catch (java.lang.Exception e) {
                    org.apache.ambari.server.view.ViewRegistry.LOG.error("Caught exception undeploying view " + viewEntity.getName(), e);
                }
            }
        }
    }

    private void syncView(org.apache.ambari.server.orm.entities.ViewEntity view, java.util.Set<org.apache.ambari.server.orm.entities.ViewInstanceEntity> instanceDefinitions) throws java.lang.Exception {
        java.lang.String viewName = view.getName();
        org.apache.ambari.server.orm.entities.ViewEntity persistedView = viewDAO.findByName(viewName);
        if (org.apache.ambari.server.view.ViewRegistry.LOG.isDebugEnabled()) {
            org.apache.ambari.server.view.ViewRegistry.LOG.debug("Syncing view {}.", viewName);
        }
        if (persistedView == null) {
            if (org.apache.ambari.server.view.ViewRegistry.LOG.isDebugEnabled()) {
                org.apache.ambari.server.view.ViewRegistry.LOG.debug("Creating view {}.", viewName);
            }
            org.apache.ambari.server.orm.entities.ResourceTypeEntity resourceType = resourceTypeDAO.merge(view.getResourceType());
            for (org.apache.ambari.server.orm.entities.ViewInstanceEntity instance : view.getInstances()) {
                instance.setResource(createViewInstanceResource(resourceType));
            }
            view.setResourceType(resourceType);
            persistedView = viewDAO.merge(view);
        }
        view.setResourceType(persistedView.getResourceType());
        view.setPermissions(persistedView.getPermissions());
        for (org.apache.ambari.server.orm.entities.ViewInstanceEntity persistedInstance : persistedView.getInstances()) {
            java.lang.String instanceName = persistedInstance.getName();
            org.apache.ambari.server.orm.entities.ViewInstanceEntity instance = view.getInstanceDefinition(instanceName);
            if (instance == null) {
                if (persistedInstance.isXmlDriven()) {
                    instanceDAO.remove(persistedInstance);
                } else {
                    instanceDAO.merge(persistedInstance);
                    bindViewInstance(view, persistedInstance);
                    instanceDefinitions.add(persistedInstance);
                }
            } else {
                syncViewInstance(instance, persistedInstance);
            }
        }
        if (org.apache.ambari.server.view.ViewRegistry.LOG.isDebugEnabled()) {
            org.apache.ambari.server.view.ViewRegistry.LOG.debug("Syncing view {} complete.", viewName);
        }
    }

    private void syncViewInstance(org.apache.ambari.server.orm.entities.ViewInstanceEntity instanceEntity) {
        java.lang.String viewName = instanceEntity.getViewDefinition().getViewName();
        java.lang.String version = instanceEntity.getViewDefinition().getVersion();
        java.lang.String instanceName = instanceEntity.getInstanceName();
        org.apache.ambari.server.orm.entities.ViewInstanceEntity registryEntry = getInstanceDefinition(viewName, version, instanceName);
        if (registryEntry != null) {
            syncViewInstance(registryEntry, instanceEntity);
        }
    }

    private void syncViewInstance(org.apache.ambari.server.orm.entities.ViewInstanceEntity instance1, org.apache.ambari.server.orm.entities.ViewInstanceEntity instance2) {
        instance1.setLabel(instance2.getLabel());
        instance1.setDescription(instance2.getDescription());
        instance1.setViewUrl(instance2.getViewUrl());
        instance1.setVisible(instance2.isVisible());
        instance1.setResource(instance2.getResource());
        instance1.setViewInstanceId(instance2.getViewInstanceId());
        instance1.setClusterHandle(instance2.getClusterHandle());
        instance1.setClusterType(instance2.getClusterType());
        instance1.setData(instance2.getData());
        instance1.setEntities(instance2.getEntities());
        instance1.setProperties(instance2.getProperties());
    }

    @com.google.inject.persist.Transactional
    org.apache.ambari.server.orm.entities.ViewInstanceEntity mergeViewInstance(org.apache.ambari.server.orm.entities.ViewInstanceEntity instanceEntity, org.apache.ambari.server.orm.entities.ResourceTypeEntity resourceTypeEntity) {
        instanceEntity.setResource(createViewInstanceResource(resourceTypeEntity));
        return instanceDAO.merge(instanceEntity);
    }

    private org.apache.ambari.server.orm.entities.ResourceEntity createViewInstanceResource(org.apache.ambari.server.orm.entities.ResourceTypeEntity resourceTypeEntity) {
        org.apache.ambari.server.orm.entities.ResourceEntity resourceEntity = new org.apache.ambari.server.orm.entities.ResourceEntity();
        resourceEntity.setResourceType(resourceTypeEntity);
        resourceDAO.create(resourceEntity);
        return resourceEntity;
    }

    private void fireEvent(org.apache.ambari.view.events.Event event, java.lang.String viewName) {
        java.util.Set<org.apache.ambari.view.events.Listener> listeners = this.listeners.get(viewName);
        if (listeners != null) {
            for (org.apache.ambari.view.events.Listener listener : listeners) {
                listener.notify(event);
            }
        }
    }

    private boolean checkAuthorization(org.apache.ambari.server.orm.entities.ResourceEntity resourceEntity) {
        java.lang.Long resourceId = (resourceEntity == null) ? null : resourceEntity.getId();
        return resourceId == null ? org.apache.ambari.server.security.authorization.AuthorizationHelper.isAuthorized(org.apache.ambari.server.security.authorization.ResourceType.AMBARI, null, org.apache.ambari.server.security.authorization.RoleAuthorization.AMBARI_MANAGE_VIEWS) : org.apache.ambari.server.security.authorization.AuthorizationHelper.isAuthorized(org.apache.ambari.server.security.authorization.ResourceType.VIEW, resourceId, org.apache.ambari.server.security.authorization.RoleAuthorization.VIEW_USE);
    }

    protected void onDeploy(org.apache.ambari.server.orm.entities.ViewEntity definition) {
        org.apache.ambari.view.View view = definition.getView();
        if (view != null) {
            view.onDeploy(definition);
        }
    }

    public void readViewArchive(java.nio.file.Path path) {
        java.io.File viewDir = configuration.getViewsDir();
        java.lang.String extractedArchivesPath = (viewDir.getAbsolutePath() + java.io.File.separator) + org.apache.ambari.server.view.ViewRegistry.EXTRACTED_ARCHIVES_DIR;
        java.io.File archiveFile = path.toAbsolutePath().toFile();
        if (extractor.ensureExtractedArchiveDirectory(extractedArchivesPath)) {
            try {
                final org.apache.ambari.server.view.configuration.ViewConfig viewConfig = archiveUtility.getViewConfigFromArchive(archiveFile);
                java.lang.String viewName = org.apache.ambari.server.orm.entities.ViewEntity.getViewName(viewConfig.getName(), viewConfig.getVersion());
                final java.lang.String extractedArchiveDirPath = (extractedArchivesPath + java.io.File.separator) + viewName;
                final java.io.File extractedArchiveDirFile = archiveUtility.getFile(extractedArchiveDirPath);
                final org.apache.ambari.server.orm.entities.ViewEntity viewDefinition = new org.apache.ambari.server.orm.entities.ViewEntity(viewConfig, configuration, extractedArchiveDirPath);
                addDefinition(viewDefinition);
                readViewArchive(viewDefinition, archiveFile, extractedArchiveDirFile, ambariMetaInfoProvider.get().getServerVersion());
            } catch (java.lang.Exception e) {
                org.apache.ambari.server.view.ViewRegistry.LOG.error("Could not process archive at path " + path, e);
            }
        }
    }

    private void readNonSystemViewViewArchives() {
        try {
            java.io.File viewDir = configuration.getViewsDir();
            java.lang.String extractedArchivesPath = (viewDir.getAbsolutePath() + java.io.File.separator) + org.apache.ambari.server.view.ViewRegistry.EXTRACTED_ARCHIVES_DIR;
            java.io.File[] files = viewDir.listFiles();
            if (files != null) {
                final java.lang.String serverVersion = ambariMetaInfoProvider.get().getServerVersion();
                final java.util.concurrent.ExecutorService executorService = org.apache.ambari.server.view.ViewRegistry.getExecutorService(configuration);
                for (final java.io.File archiveFile : files) {
                    if (!archiveFile.isDirectory()) {
                        try {
                            final org.apache.ambari.server.view.configuration.ViewConfig viewConfig = archiveUtility.getViewConfigFromArchive(archiveFile);
                            java.lang.String commonName = viewConfig.getName();
                            java.lang.String version = viewConfig.getVersion();
                            java.lang.String viewName = org.apache.ambari.server.orm.entities.ViewEntity.getViewName(commonName, version);
                            final java.lang.String extractedArchiveDirPath = (extractedArchivesPath + java.io.File.separator) + viewName;
                            final java.io.File extractedArchiveDirFile = archiveUtility.getFile(extractedArchiveDirPath);
                            final org.apache.ambari.server.orm.entities.ViewEntity viewDefinition = new org.apache.ambari.server.orm.entities.ViewEntity(viewConfig, configuration, extractedArchiveDirPath);
                            boolean systemView = viewDefinition.isSystem();
                            if (!systemView) {
                                addDefinition(viewDefinition);
                                executorService.submit(new java.lang.Runnable() {
                                    @java.lang.Override
                                    public void run() {
                                        readViewArchive(viewDefinition, archiveFile, extractedArchiveDirFile, serverVersion);
                                        migrateDataFromPreviousVersion(viewDefinition, serverVersion);
                                    }
                                });
                            }
                        } catch (java.lang.Exception e) {
                            java.lang.String msg = "Caught exception reading view archive " + archiveFile.getAbsolutePath();
                            org.apache.ambari.server.view.ViewRegistry.LOG.error(msg, e);
                        }
                    }
                }
            }
        } catch (java.lang.Exception e) {
            org.apache.ambari.server.view.ViewRegistry.LOG.error("Caught exception reading view archives.", e);
        }
    }

    private void readViewArchives(boolean systemOnly, boolean useExecutor, java.lang.String viewNameRegExp) {
        try {
            java.io.File viewDir = configuration.getViewsDir();
            java.lang.String extractedArchivesPath = (viewDir.getAbsolutePath() + java.io.File.separator) + org.apache.ambari.server.view.ViewRegistry.EXTRACTED_ARCHIVES_DIR;
            if (extractor.ensureExtractedArchiveDirectory(extractedArchivesPath)) {
                java.io.File[] files = viewDir.listFiles();
                if (files != null) {
                    java.util.Set<java.lang.Runnable> extractionRunnables = new java.util.HashSet<>();
                    final java.lang.String serverVersion = ambariMetaInfoProvider.get().getServerVersion();
                    for (final java.io.File archiveFile : files) {
                        if (!archiveFile.isDirectory()) {
                            try {
                                final org.apache.ambari.server.view.configuration.ViewConfig viewConfig = archiveUtility.getViewConfigFromArchive(archiveFile);
                                java.lang.String commonName = viewConfig.getName();
                                java.lang.String version = viewConfig.getVersion();
                                java.lang.String viewName = org.apache.ambari.server.orm.entities.ViewEntity.getViewName(commonName, version);
                                if (!viewName.matches(viewNameRegExp)) {
                                    continue;
                                }
                                final java.lang.String extractedArchiveDirPath = (extractedArchivesPath + java.io.File.separator) + viewName;
                                final java.io.File extractedArchiveDirFile = archiveUtility.getFile(extractedArchiveDirPath);
                                final org.apache.ambari.server.orm.entities.ViewEntity viewDefinition = new org.apache.ambari.server.orm.entities.ViewEntity(viewConfig, configuration, extractedArchiveDirPath);
                                boolean systemView = viewDefinition.isSystem();
                                if ((!systemOnly) || systemView) {
                                    addDefinition(viewDefinition);
                                    if ((systemView || (!useExecutor)) || extractedArchiveDirFile.exists()) {
                                        readViewArchive(viewDefinition, archiveFile, extractedArchiveDirFile, serverVersion);
                                    } else {
                                        extractionRunnables.add(new java.lang.Runnable() {
                                            @java.lang.Override
                                            public void run() {
                                                readViewArchive(viewDefinition, archiveFile, extractedArchiveDirFile, serverVersion);
                                                migrateDataFromPreviousVersion(viewDefinition, serverVersion);
                                            }
                                        });
                                    }
                                }
                            } catch (java.lang.Exception e) {
                                java.lang.String msg = "Caught exception reading view archive " + archiveFile.getAbsolutePath();
                                org.apache.ambari.server.view.ViewRegistry.LOG.error(msg, e);
                            }
                        }
                    }
                    for (org.apache.ambari.server.orm.entities.ViewEntity view : getDefinitions()) {
                        if (view.getStatus() == org.apache.ambari.view.ViewDefinition.ViewStatus.DEPLOYED) {
                            migrateDataFromPreviousVersion(view, serverVersion);
                        }
                    }
                    if (useExecutor && (extractionRunnables.size() > 0)) {
                        final java.util.concurrent.ExecutorService executorService = org.apache.ambari.server.view.ViewRegistry.getExecutorService(configuration);
                        for (java.lang.Runnable runnable : extractionRunnables) {
                            executorService.submit(runnable);
                        }
                    }
                    if (configuration.isViewRemoveUndeployedEnabled()) {
                        removeUndeployedViews();
                    }
                }
            } else {
                org.apache.ambari.server.view.ViewRegistry.LOG.error(("Could not create extracted view archive directory " + extractedArchivesPath) + ".");
            }
        } catch (java.lang.Exception e) {
            org.apache.ambari.server.view.ViewRegistry.LOG.error("Caught exception reading view archives.", e);
        }
    }

    private synchronized void readViewArchive(org.apache.ambari.server.orm.entities.ViewEntity viewDefinition, java.io.File archiveFile, java.io.File extractedArchiveDirFile, java.lang.String serverVersion) {
        setViewStatus(viewDefinition, org.apache.ambari.view.ViewDefinition.ViewStatus.DEPLOYING, ("Deploying " + extractedArchiveDirFile) + ".");
        java.lang.String extractedArchiveDirPath = extractedArchiveDirFile.getAbsolutePath();
        org.apache.ambari.server.view.ViewRegistry.LOG.info(("Reading view archive " + archiveFile) + ".");
        try {
            java.util.List<java.io.File> additionalPaths = org.apache.ambari.server.view.ViewRegistry.getViewsAdditionalClasspath(configuration);
            java.lang.ClassLoader cl = extractor.extractViewArchive(viewDefinition, archiveFile, extractedArchiveDirFile, additionalPaths);
            configureViewLogging(viewDefinition, cl);
            org.apache.ambari.server.view.configuration.ViewConfig viewConfig = archiveUtility.getViewConfigFromExtractedArchive(extractedArchiveDirPath, configuration.isViewValidationEnabled());
            viewDefinition.setConfiguration(viewConfig);
            if (checkViewVersions(viewDefinition, serverVersion)) {
                setupViewDefinition(viewDefinition, cl);
                java.util.Set<org.apache.ambari.server.orm.entities.ViewInstanceEntity> instanceDefinitions = new java.util.HashSet<>();
                for (org.apache.ambari.server.view.configuration.InstanceConfig instanceConfig : viewConfig.getInstances()) {
                    org.apache.ambari.server.orm.entities.ViewInstanceEntity instanceEntity = createViewInstanceDefinition(viewConfig, viewDefinition, instanceConfig);
                    instanceEntity.setXmlDriven(true);
                    instanceDefinitions.add(instanceEntity);
                }
                persistView(viewDefinition, instanceDefinitions);
                if (getDefinition(viewDefinition.getViewName(), viewDefinition.getVersion()) != null) {
                    addAutoInstanceDefinition(viewDefinition);
                }
                setViewStatus(viewDefinition, org.apache.ambari.view.ViewDefinition.ViewStatus.DEPLOYED, ("Deployed " + extractedArchiveDirPath) + ".");
                org.apache.ambari.server.view.ViewRegistry.LOG.info(("View deployed: " + viewDefinition.getName()) + ".");
            }
        } catch (java.lang.Throwable e) {
            java.lang.String msg = "Caught exception loading view " + viewDefinition.getName();
            setViewStatus(viewDefinition, org.apache.ambari.view.ViewDefinition.ViewStatus.ERROR, (msg + " : ") + e.getMessage());
            org.apache.ambari.server.view.ViewRegistry.LOG.error(msg, e);
        }
    }

    private static java.util.List<java.io.File> getViewsAdditionalClasspath(org.apache.ambari.server.configuration.Configuration configuration) {
        java.lang.String viewsAdditionalClasspath = configuration.getViewsAdditionalClasspath();
        java.util.List<java.io.File> additionalPaths = new java.util.LinkedList<>();
        if ((null != viewsAdditionalClasspath) && (!viewsAdditionalClasspath.trim().isEmpty())) {
            java.lang.String[] paths = viewsAdditionalClasspath.trim().split(",");
            for (java.lang.String path : paths) {
                if ((null != path) && (!path.trim().isEmpty()))
                    additionalPaths.add(new java.io.File(path));

            }
        }
        return additionalPaths;
    }

    private void migrateDataFromPreviousVersion(org.apache.ambari.server.orm.entities.ViewEntity viewDefinition, java.lang.String serverVersion) {
        if (!viewDefinitions.containsKey(viewDefinition.getName())) {
            org.apache.ambari.server.view.ViewRegistry.LOG.debug("Cancel auto migration of not loaded view: {}.", viewDefinition.getName());
            return;
        }
        try {
            for (org.apache.ambari.server.orm.entities.ViewInstanceEntity instance : viewDefinition.getInstances()) {
                org.apache.ambari.server.view.ViewRegistry.LOG.debug("Try to migrate the data from previous version of: {}/{}.", viewDefinition.getName(), instance.getInstanceName());
                org.apache.ambari.server.orm.entities.ViewInstanceEntity latestUnregisteredView = getLatestUnregisteredInstance(serverVersion, instance);
                if (latestUnregisteredView != null) {
                    java.lang.String instanceName = (instance.getViewEntity().getName() + "/") + instance.getName();
                    try {
                        org.apache.ambari.server.view.ViewRegistry.LOG.info((((("Found previous version of the view instance " + instanceName) + ": ") + latestUnregisteredView.getViewEntity().getName()) + "/") + latestUnregisteredView.getName());
                        getViewDataMigrationUtility().migrateData(instance, latestUnregisteredView, true);
                        org.apache.ambari.server.view.ViewRegistry.LOG.info(("View data migrated: " + viewDefinition.getName()) + ".");
                    } catch (org.apache.ambari.view.migration.ViewDataMigrationException e) {
                        org.apache.ambari.server.view.ViewRegistry.LOG.error("Error occurred during migration", e);
                    }
                }
            }
        } catch (java.lang.Exception e) {
            java.lang.String msg = "Caught exception migrating data in view " + viewDefinition.getName();
            setViewStatus(viewDefinition, org.apache.ambari.view.ViewDefinition.ViewStatus.ERROR, (msg + " : ") + e.getMessage());
            org.apache.ambari.server.view.ViewRegistry.LOG.error(msg, e);
        }
    }

    private void configureViewLogging(org.apache.ambari.server.orm.entities.ViewEntity viewDefinition, java.lang.ClassLoader cl) {
        java.io.InputStream viewLog4jStream = cl.getResourceAsStream(org.apache.ambari.server.view.ViewRegistry.VIEW_LOG_FILE);
        java.io.InputStream ambariLog4jStream = null;
        if (null != viewLog4jStream) {
            try {
                java.util.Properties viewLog4jConfig = new java.util.Properties();
                viewLog4jConfig.load(viewLog4jStream);
                org.apache.ambari.server.view.ViewRegistry.LOG.info("setting up logging for view {} as per property file {}", viewDefinition.getName(), org.apache.ambari.server.view.ViewRegistry.VIEW_LOG_FILE);
                ambariLog4jStream = cl.getResourceAsStream(org.apache.ambari.server.view.ViewRegistry.AMBARI_LOG_FILE);
                if (null != ambariLog4jStream) {
                    java.util.Properties ambariLog4jConfig = new java.util.Properties();
                    ambariLog4jConfig.load(ambariLog4jStream);
                    for (java.lang.Object property : ambariLog4jConfig.keySet()) {
                        java.lang.String prop = ((java.lang.String) (property));
                        if (prop.startsWith(org.apache.ambari.server.view.ViewRegistry.LOG4J)) {
                            viewLog4jConfig.remove(prop);
                        } else {
                            viewLog4jConfig.put(prop, ambariLog4jConfig.getProperty(prop));
                        }
                    }
                }
                org.apache.log4j.PropertyConfigurator.configure(viewLog4jConfig);
            } catch (java.io.IOException e) {
                org.apache.ambari.server.view.ViewRegistry.LOG.error("Error occurred while configuring logs for {}", viewDefinition.getName());
            } finally {
                org.apache.ambari.server.utils.Closeables.closeSilently(ambariLog4jStream);
                org.apache.ambari.server.utils.Closeables.closeSilently(viewLog4jStream);
            }
        }
    }

    private void addAutoInstanceDefinition(org.apache.ambari.server.orm.entities.ViewEntity viewEntity) {
        org.apache.ambari.server.view.configuration.ViewConfig viewConfig = viewEntity.getConfiguration();
        java.lang.String viewName = viewEntity.getViewName();
        org.apache.ambari.server.view.configuration.AutoInstanceConfig autoInstanceConfig = viewConfig.getAutoInstance();
        if (autoInstanceConfig == null) {
            return;
        }
        java.util.List<java.lang.String> services = autoInstanceConfig.getServices();
        java.util.Collection<java.lang.String> roles = autoInstanceConfig.getRoles();
        java.util.Map<java.lang.String, org.apache.ambari.server.state.Cluster> allClusters = clustersProvider.get().getClusters();
        for (org.apache.ambari.server.state.Cluster cluster : allClusters.values()) {
            java.lang.String clusterName = cluster.getClusterName();
            java.lang.Long clusterId = cluster.getClusterId();
            java.util.Set<java.lang.String> serviceNames = cluster.getServices().keySet();
            for (java.lang.String service : services) {
                try {
                    org.apache.ambari.server.state.Service svc = cluster.getService(service);
                    org.apache.ambari.server.state.StackId stackId = svc.getDesiredStackId();
                    if (checkAutoInstanceConfig(autoInstanceConfig, stackId, service, serviceNames)) {
                        installAutoInstance(clusterId, clusterName, cluster.getService(service), viewEntity, viewName, viewConfig, autoInstanceConfig, roles);
                    }
                } catch (java.lang.Exception e) {
                    org.apache.ambari.server.view.ViewRegistry.LOG.error((((("Can't auto create instance of view " + viewName) + " for cluster ") + clusterName) + ".  Caught exception :") + e.getMessage(), e);
                }
            }
        }
    }

    @com.google.inject.persist.Transactional
    protected void setViewInstanceRoleAccess(org.apache.ambari.server.orm.entities.ViewInstanceEntity viewInstanceEntity, java.util.Collection<java.lang.String> roles) {
        if ((roles != null) && (!roles.isEmpty())) {
            org.apache.ambari.server.orm.entities.PermissionEntity permissionViewUser = permissionDAO.findViewUsePermission();
            org.apache.ambari.server.orm.entities.ResourceEntity resourceEntity = viewInstanceEntity.getResource();
            if (null == resourceEntity) {
                resourceEntity = instanceDAO.findResourceForViewInstance(viewInstanceEntity.getViewName(), viewInstanceEntity.getInstanceName());
            }
            if (permissionViewUser == null) {
                org.apache.ambari.server.view.ViewRegistry.LOG.error("Missing the {} role.  Access to view cannot be set.", org.apache.ambari.server.orm.entities.PermissionEntity.VIEW_USER_PERMISSION_NAME, viewInstanceEntity.getName());
            } else {
                for (java.lang.String role : roles) {
                    org.apache.ambari.server.orm.entities.PermissionEntity permissionRole = permissionDAO.findByName(role);
                    if (permissionRole == null) {
                        org.apache.ambari.server.view.ViewRegistry.LOG.warn("Invalid role {} encountered while setting access to view {}, Ignoring.", role, viewInstanceEntity.getName());
                    } else {
                        org.apache.ambari.server.orm.entities.PrincipalEntity principalRole = permissionRole.getPrincipal();
                        if (principalRole == null) {
                            org.apache.ambari.server.view.ViewRegistry.LOG.warn("Missing principal ID for role {} encountered while setting access to view {}. Ignoring.", role, viewInstanceEntity.getName());
                        } else if (!privilegeDAO.exists(principalRole, resourceEntity, permissionViewUser)) {
                            org.apache.ambari.server.orm.entities.PrivilegeEntity privilegeEntity = new org.apache.ambari.server.orm.entities.PrivilegeEntity();
                            privilegeEntity.setPermission(permissionViewUser);
                            privilegeEntity.setPrincipal(principalRole);
                            privilegeEntity.setResource(resourceEntity);
                            privilegeDAO.create(privilegeEntity);
                        }
                    }
                }
            }
        }
    }

    protected boolean checkViewVersions(org.apache.ambari.server.orm.entities.ViewEntity view, java.lang.String serverVersion) {
        org.apache.ambari.server.view.configuration.ViewConfig config = view.getConfiguration();
        return checkViewVersion(view, config.getMinAmbariVersion(), serverVersion, "minimum", -1, "less than") && checkViewVersion(view, config.getMaxAmbariVersion(), serverVersion, "maximum", 1, "greater than");
    }

    private boolean checkViewVersion(org.apache.ambari.server.orm.entities.ViewEntity view, java.lang.String version, java.lang.String serverVersion, java.lang.String label, int errValue, java.lang.String errMsg) {
        if ((version != null) && (!version.isEmpty())) {
            if (!version.matches(org.apache.ambari.server.view.ViewRegistry.VIEW_AMBARI_VERSION_REGEXP)) {
                java.lang.String msg = ((((("The configured " + label) + " Ambari version ") + version) + " for view ") + view.getName()) + " is not valid.";
                setViewStatus(view, org.apache.ambari.view.ViewDefinition.ViewStatus.ERROR, msg);
                org.apache.ambari.server.view.ViewRegistry.LOG.error(msg);
                return false;
            }
            int index = version.indexOf('*');
            int compVal = (index == (-1)) ? org.apache.ambari.server.utils.VersionUtils.compareVersions(serverVersion, version) : index > 0 ? org.apache.ambari.server.utils.VersionUtils.compareVersions(serverVersion, version.substring(0, index), index) : 0;
            if (compVal == errValue) {
                java.lang.String msg = (((((((("The Ambari server version " + serverVersion) + " is ") + errMsg) + " the configured ") + label) + " Ambari version ") + version) + " for view ") + view.getName();
                setViewStatus(view, org.apache.ambari.view.ViewDefinition.ViewStatus.ERROR, msg);
                org.apache.ambari.server.view.ViewRegistry.LOG.error(msg);
                return false;
            }
        }
        return true;
    }

    @com.google.inject.persist.Transactional
    void persistView(org.apache.ambari.server.orm.entities.ViewEntity viewDefinition, java.util.Set<org.apache.ambari.server.orm.entities.ViewInstanceEntity> instanceDefinitions) throws java.lang.Exception {
        syncView(viewDefinition, instanceDefinitions);
        onDeploy(viewDefinition);
        for (org.apache.ambari.server.orm.entities.ViewInstanceEntity instanceEntity : instanceDefinitions) {
            addInstanceDefinition(viewDefinition, instanceEntity);
            handlerList.addViewInstance(instanceEntity);
        }
    }

    protected static boolean extractViewArchive(java.lang.String archivePath, org.apache.ambari.server.view.ViewRegistry.ViewModule viewModule, boolean systemOnly) throws java.lang.Exception {
        com.google.inject.Injector injector = com.google.inject.Guice.createInjector(viewModule);
        org.apache.ambari.server.view.ViewExtractor extractor = injector.getInstance(org.apache.ambari.server.view.ViewExtractor.class);
        org.apache.ambari.server.view.ViewArchiveUtility archiveUtility = injector.getInstance(org.apache.ambari.server.view.ViewArchiveUtility.class);
        org.apache.ambari.server.configuration.Configuration configuration = injector.getInstance(org.apache.ambari.server.configuration.Configuration.class);
        java.io.File viewDir = configuration.getViewsDir();
        java.lang.String extractedArchivesPath = (viewDir.getAbsolutePath() + java.io.File.separator) + org.apache.ambari.server.view.ViewRegistry.EXTRACTED_ARCHIVES_DIR;
        if (extractor.ensureExtractedArchiveDirectory(extractedArchivesPath)) {
            java.io.File archiveFile = archiveUtility.getFile(archivePath);
            org.apache.ambari.server.view.configuration.ViewConfig viewConfig = archiveUtility.getViewConfigFromArchive(archiveFile);
            java.lang.String commonName = viewConfig.getName();
            java.lang.String version = viewConfig.getVersion();
            java.lang.String viewName = org.apache.ambari.server.orm.entities.ViewEntity.getViewName(commonName, version);
            java.lang.String extractedArchiveDirPath = (extractedArchivesPath + java.io.File.separator) + viewName;
            java.io.File extractedArchiveDirFile = archiveUtility.getFile(extractedArchiveDirPath);
            if (!extractedArchiveDirFile.exists()) {
                org.apache.ambari.server.orm.entities.ViewEntity viewDefinition = new org.apache.ambari.server.orm.entities.ViewEntity(viewConfig, configuration, extractedArchiveDirPath);
                if ((!systemOnly) || viewDefinition.isSystem()) {
                    java.lang.ClassLoader classLoader = null;
                    try {
                        java.util.List<java.io.File> additionalPaths = org.apache.ambari.server.view.ViewRegistry.getViewsAdditionalClasspath(configuration);
                        classLoader = extractor.extractViewArchive(viewDefinition, archiveFile, extractedArchiveDirFile, additionalPaths);
                        return true;
                    } finally {
                        if (classLoader instanceof java.io.Closeable) {
                            org.apache.ambari.server.utils.Closeables.closeSilently(((java.io.Closeable) (classLoader)));
                        }
                    }
                }
            }
        }
        return false;
    }

    private void setViewStatus(org.apache.ambari.server.orm.entities.ViewEntity viewDefinition, org.apache.ambari.view.ViewDefinition.ViewStatus status, java.lang.String statusDetail) {
        viewDefinition.setStatus(status);
        viewDefinition.setStatusDetail(statusDetail);
    }

    private static synchronized java.util.concurrent.ExecutorService getExecutorService(org.apache.ambari.server.configuration.Configuration configuration) {
        if (org.apache.ambari.server.view.ViewRegistry.executorService == null) {
            java.util.concurrent.LinkedBlockingQueue<java.lang.Runnable> queue = new java.util.concurrent.LinkedBlockingQueue<>();
            java.util.concurrent.ThreadPoolExecutor threadPoolExecutor = new java.util.concurrent.ThreadPoolExecutor(configuration.getViewExtractionThreadPoolCoreSize(), configuration.getViewExtractionThreadPoolMaxSize(), configuration.getViewExtractionThreadPoolTimeout(), java.util.concurrent.TimeUnit.MILLISECONDS, queue);
            threadPoolExecutor.allowCoreThreadTimeOut(true);
            org.apache.ambari.server.view.ViewRegistry.executorService = threadPoolExecutor;
        }
        return org.apache.ambari.server.view.ViewRegistry.executorService;
    }

    protected org.apache.ambari.server.view.ViewURLStreamProvider createURLStreamProvider(org.apache.ambari.view.ViewContext viewContext) {
        org.apache.ambari.server.configuration.ComponentSSLConfiguration sslConfiguration = org.apache.ambari.server.configuration.ComponentSSLConfiguration.instance();
        org.apache.ambari.server.controller.internal.URLStreamProvider streamProvider = new org.apache.ambari.server.controller.internal.URLStreamProvider(configuration.getRequestConnectTimeout(), configuration.getRequestReadTimeout(), sslConfiguration.getTruststorePath(), sslConfiguration.getTruststorePassword(), sslConfiguration.getTruststoreType());
        return new org.apache.ambari.server.view.ViewURLStreamProvider(viewContext, streamProvider);
    }

    protected org.apache.ambari.server.view.ViewAmbariStreamProvider createAmbariStreamProvider() {
        org.apache.ambari.server.configuration.ComponentSSLConfiguration sslConfiguration = org.apache.ambari.server.configuration.ComponentSSLConfiguration.instance();
        org.apache.ambari.server.controller.internal.URLStreamProvider streamProvider = new org.apache.ambari.server.controller.internal.URLStreamProvider(configuration.getViewAmbariRequestConnectTimeout(), configuration.getViewAmbariRequestReadTimeout(), sslConfiguration.getTruststorePath(), sslConfiguration.getTruststorePassword(), sslConfiguration.getTruststoreType());
        return new org.apache.ambari.server.view.ViewAmbariStreamProvider(streamProvider, ambariSessionManager, org.apache.ambari.server.controller.AmbariServer.getController());
    }

    protected org.apache.ambari.view.AmbariStreamProvider createRemoteAmbariStreamProvider(java.lang.Long clusterId) {
        org.apache.ambari.server.orm.entities.RemoteAmbariClusterEntity clusterEntity = remoteAmbariClusterDAO.findById(clusterId);
        if (clusterEntity != null) {
            return new org.apache.ambari.server.view.RemoteAmbariStreamProvider(getBaseurl(clusterEntity.getUrl()), clusterEntity.getUsername(), clusterEntity.getPassword(), configuration.getViewAmbariRequestConnectTimeout(), configuration.getViewAmbariRequestReadTimeout());
        }
        return null;
    }

    private java.lang.String getBaseurl(java.lang.String url) {
        int index = url.indexOf(org.apache.ambari.server.view.ViewRegistry.API_PREFIX);
        return url.substring(0, index);
    }

    private org.apache.ambari.server.orm.entities.ViewInstanceEntity getLatestUnregisteredInstance(java.lang.String serverVersion, org.apache.ambari.server.orm.entities.ViewInstanceEntity instance) throws javax.xml.bind.JAXBException, java.io.IOException, org.xml.sax.SAXException {
        java.io.File viewDir = configuration.getViewsDir();
        java.lang.String extractedArchivesPath = (viewDir.getAbsolutePath() + java.io.File.separator) + org.apache.ambari.server.view.ViewRegistry.EXTRACTED_ARCHIVES_DIR;
        java.io.File extractedArchivesDir = new java.io.File(extractedArchivesPath);
        java.io.File[] extractedArchives = extractedArchivesDir.listFiles();
        java.util.Map<org.apache.ambari.server.orm.entities.ViewInstanceEntity, java.lang.Long> unregInstancesTimestamps = new java.util.HashMap<>();
        if (extractedArchives != null) {
            for (java.io.File archiveDir : extractedArchives) {
                if (archiveDir.isDirectory()) {
                    org.apache.ambari.server.view.configuration.ViewConfig uViewConfig = archiveUtility.getViewConfigFromExtractedArchive(archiveDir.getPath(), false);
                    if (!uViewConfig.isSystem()) {
                        if (!uViewConfig.getName().equals(instance.getViewEntity().getViewName())) {
                            continue;
                        }
                        if (viewDefinitions.containsKey(org.apache.ambari.server.orm.entities.ViewEntity.getViewName(uViewConfig.getName(), uViewConfig.getVersion()))) {
                            continue;
                        }
                        org.apache.ambari.server.view.ViewRegistry.LOG.debug("Unregistered extracted view found: {}", archiveDir.getPath());
                        org.apache.ambari.server.orm.entities.ViewEntity uViewDefinition = new org.apache.ambari.server.orm.entities.ViewEntity(uViewConfig, configuration, archiveDir.getPath());
                        readViewArchive(uViewDefinition, archiveDir, archiveDir, serverVersion);
                        for (org.apache.ambari.server.orm.entities.ViewInstanceEntity instanceEntity : uViewDefinition.getInstances()) {
                            org.apache.ambari.server.view.ViewRegistry.LOG.debug("{} instance found: {}", uViewDefinition.getName(), instanceEntity.getInstanceName());
                            unregInstancesTimestamps.put(instanceEntity, archiveDir.lastModified());
                        }
                    }
                }
            }
        }
        long latestPrevInstanceTimestamp = 0;
        org.apache.ambari.server.orm.entities.ViewInstanceEntity latestPrevInstance = null;
        for (org.apache.ambari.server.orm.entities.ViewInstanceEntity unregInstance : unregInstancesTimestamps.keySet()) {
            if (unregInstance.getName().equals(instance.getName())) {
                if (unregInstancesTimestamps.get(unregInstance) > latestPrevInstanceTimestamp) {
                    latestPrevInstance = unregInstance;
                    latestPrevInstanceTimestamp = unregInstancesTimestamps.get(latestPrevInstance);
                }
            }
        }
        if (latestPrevInstance != null) {
            org.apache.ambari.server.view.ViewRegistry.LOG.debug("Previous version of {}/{} found: {}/{}", instance.getViewEntity().getName(), instance.getName(), latestPrevInstance.getViewEntity().getName(), latestPrevInstance.getName());
        } else {
            org.apache.ambari.server.view.ViewRegistry.LOG.debug("Previous version of {}/{} not found", instance.getViewEntity().getName(), instance.getName());
        }
        return latestPrevInstance;
    }

    protected org.apache.ambari.server.view.ViewDataMigrationUtility getViewDataMigrationUtility() {
        if (viewDataMigrationUtility == null) {
            viewDataMigrationUtility = new org.apache.ambari.server.view.ViewDataMigrationUtility(this);
        }
        return viewDataMigrationUtility;
    }

    protected void setViewDataMigrationUtility(org.apache.ambari.server.view.ViewDataMigrationUtility viewDataMigrationUtility) {
        this.viewDataMigrationUtility = viewDataMigrationUtility;
    }

    protected static class ViewModule extends com.google.inject.AbstractModule {
        @java.lang.Override
        protected void configure() {
            org.apache.ambari.server.configuration.Configuration configuration = new org.apache.ambari.server.configuration.Configuration();
            bind(org.apache.ambari.server.configuration.Configuration.class).toInstance(configuration);
            bind(org.apache.ambari.server.state.stack.OsFamily.class).toInstance(new org.apache.ambari.server.state.stack.OsFamily(configuration));
        }
    }
}