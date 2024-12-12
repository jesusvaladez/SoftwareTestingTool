package org.apache.ambari.server.orm.entities;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
@javax.persistence.Table(name = "viewinstance", uniqueConstraints = @javax.persistence.UniqueConstraint(name = "UQ_viewinstance_name", columnNames = { "view_name", "name" }))
@javax.persistence.NamedQueries({ @javax.persistence.NamedQuery(name = "allViewInstances", query = "SELECT viewInstance FROM ViewInstanceEntity viewInstance"), @javax.persistence.NamedQuery(name = "viewInstanceByResourceId", query = "SELECT viewInstance FROM ViewInstanceEntity viewInstance " + "WHERE viewInstance.resource.id=:resourceId"), @javax.persistence.NamedQuery(name = "getResourceIdByViewInstance", query = "SELECT viewInstance.resource FROM ViewInstanceEntity viewInstance " + "WHERE viewInstance.viewName = :viewName AND viewInstance.name = :instanceName") })
@javax.persistence.TableGenerator(name = "view_instance_id_generator", table = "ambari_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_value", pkColumnValue = "view_instance_id_seq", initialValue = 1)
@javax.persistence.Entity
public class ViewInstanceEntity implements org.apache.ambari.view.ViewInstanceDefinition {
    @javax.persistence.Id
    @javax.persistence.Column(name = "view_instance_id", nullable = false)
    @javax.persistence.GeneratedValue(strategy = javax.persistence.GenerationType.TABLE, generator = "view_instance_id_generator")
    private java.lang.Long viewInstanceId;

    @javax.persistence.Column(name = "view_name", nullable = false, insertable = false, updatable = false)
    private java.lang.String viewName;

    @javax.persistence.Column(name = "name", nullable = false, insertable = true, updatable = false)
    private java.lang.String name;

    @javax.persistence.Column
    @javax.persistence.Basic
    private java.lang.String label;

    @javax.persistence.Column
    @javax.persistence.Basic
    private java.lang.String description;

    @javax.persistence.Column(name = "cluster_handle", nullable = true)
    private java.lang.Long clusterHandle;

    @javax.persistence.Enumerated(javax.persistence.EnumType.STRING)
    @javax.persistence.Column(name = "cluster_type", nullable = false, length = 100)
    private org.apache.ambari.view.ClusterType clusterType = org.apache.ambari.view.ClusterType.LOCAL_AMBARI;

    @javax.persistence.Column
    @javax.persistence.Basic
    private char visible;

    @javax.persistence.Column
    @javax.persistence.Basic
    private java.lang.String icon;

    @javax.persistence.OneToOne(cascade = javax.persistence.CascadeType.ALL)
    @javax.persistence.JoinColumns({ @javax.persistence.JoinColumn(name = "short_url", referencedColumnName = "url_id", nullable = true) })
    private org.apache.ambari.server.orm.entities.ViewURLEntity viewUrl;

    @javax.persistence.Column
    @javax.persistence.Basic
    private java.lang.String icon64;

    @javax.persistence.Column(name = "xml_driven")
    @javax.persistence.Basic
    private char xmlDriven = 'N';

    @javax.persistence.Column(name = "alter_names", nullable = false)
    @javax.persistence.Basic
    private java.lang.Integer alterNames;

    @javax.persistence.OneToMany(cascade = javax.persistence.CascadeType.ALL, mappedBy = "viewInstance")
    private java.util.Collection<org.apache.ambari.server.orm.entities.ViewInstancePropertyEntity> properties = new java.util.HashSet<>();

    @javax.persistence.OneToMany(cascade = javax.persistence.CascadeType.ALL, mappedBy = "viewInstance")
    private java.util.Collection<org.apache.ambari.server.orm.entities.ViewInstanceDataEntity> data = new java.util.HashSet<>();

    @javax.persistence.OneToMany(cascade = javax.persistence.CascadeType.ALL, mappedBy = "viewInstance")
    private java.util.Collection<org.apache.ambari.server.orm.entities.ViewEntityEntity> entities = new java.util.HashSet<>();

    @javax.persistence.ManyToOne
    @javax.persistence.JoinColumn(name = "view_name", referencedColumnName = "view_name", nullable = false)
    private org.apache.ambari.server.orm.entities.ViewEntity view;

    @javax.persistence.OneToOne(cascade = javax.persistence.CascadeType.ALL)
    @javax.persistence.JoinColumns({ @javax.persistence.JoinColumn(name = "resource_id", referencedColumnName = "resource_id", nullable = false) })
    private org.apache.ambari.server.orm.entities.ResourceEntity resource;

    @javax.persistence.Transient
    private final org.apache.ambari.server.view.configuration.InstanceConfig instanceConfig;

    @javax.persistence.Transient
    private final java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, org.apache.ambari.view.ResourceProvider> resourceProviders = new java.util.HashMap<>();

    @javax.persistence.Transient
    private final java.util.Map<java.lang.String, java.lang.Object> services = new java.util.HashMap<>();

    @javax.persistence.Transient
    private org.apache.ambari.server.security.SecurityHelper securityHelper = org.apache.ambari.server.security.SecurityHelperImpl.getInstance();

    @javax.persistence.Transient
    private org.apache.ambari.view.migration.ViewDataMigrator dataMigrator;

    public ViewInstanceEntity() {
        instanceConfig = null;
        alterNames = 1;
    }

    public ViewInstanceEntity(org.apache.ambari.server.orm.entities.ViewEntity view, org.apache.ambari.server.view.configuration.InstanceConfig instanceConfig) {
        name = instanceConfig.getName();
        this.instanceConfig = instanceConfig;
        this.view = view;
        viewName = view.getName();
        description = instanceConfig.getDescription();
        clusterHandle = null;
        visible = (instanceConfig.isVisible()) ? 'Y' : 'N';
        alterNames = 1;
        clusterType = org.apache.ambari.view.ClusterType.LOCAL_AMBARI;
        java.lang.String label = instanceConfig.getLabel();
        this.label = ((label == null) || (label.length() == 0)) ? view.getLabel() : label;
        java.lang.String icon = instanceConfig.getIcon();
        this.icon = ((icon == null) || (icon.length() == 0)) ? view.getIcon() : icon;
        java.lang.String icon64 = instanceConfig.getIcon64();
        this.icon64 = ((icon64 == null) || (icon64.length() == 0)) ? view.getIcon64() : icon64;
    }

    public ViewInstanceEntity(org.apache.ambari.server.orm.entities.ViewEntity view, java.lang.String name) {
        this(view, name, view.getLabel());
    }

    public ViewInstanceEntity(org.apache.ambari.server.orm.entities.ViewEntity view, java.lang.String name, java.lang.String label) {
        this.name = name;
        instanceConfig = null;
        this.view = view;
        viewName = view.getName();
        description = null;
        clusterHandle = null;
        visible = 'Y';
        alterNames = 1;
        this.label = label;
    }

    @java.lang.Override
    public java.lang.String getInstanceName() {
        return name;
    }

    @java.lang.Override
    public java.lang.String getViewName() {
        return viewName;
    }

    @java.lang.Override
    public java.util.Map<java.lang.String, java.lang.String> getPropertyMap() {
        java.util.Map<java.lang.String, java.lang.String> propertyMap = new java.util.HashMap<>();
        for (org.apache.ambari.server.orm.entities.ViewInstancePropertyEntity viewInstancePropertyEntity : getProperties()) {
            propertyMap.put(viewInstancePropertyEntity.getName(), viewInstancePropertyEntity.getValue());
        }
        for (org.apache.ambari.server.orm.entities.ViewParameterEntity viewParameterEntity : view.getParameters()) {
            java.lang.String parameterName = viewParameterEntity.getName();
            if (!propertyMap.containsKey(parameterName)) {
                propertyMap.put(parameterName, viewParameterEntity.getDefaultValue());
            }
        }
        return propertyMap;
    }

    @java.lang.Override
    public java.util.Map<java.lang.String, java.lang.String> getInstanceDataMap() {
        java.util.Map<java.lang.String, java.lang.String> applicationData = new java.util.HashMap<>();
        java.lang.String user = getCurrentUserName();
        for (org.apache.ambari.server.orm.entities.ViewInstanceDataEntity viewInstanceDataEntity : data) {
            if (viewInstanceDataEntity.getUser().equals(user)) {
                applicationData.put(viewInstanceDataEntity.getName(), viewInstanceDataEntity.getValue());
            }
        }
        return applicationData;
    }

    @java.lang.Override
    public org.apache.ambari.view.ViewDefinition getViewDefinition() {
        return view;
    }

    @java.lang.Override
    public java.lang.String getLabel() {
        return label;
    }

    @java.lang.Override
    public java.lang.String getDescription() {
        return description;
    }

    @java.lang.Override
    public java.lang.Long getClusterHandle() {
        return clusterHandle;
    }

    @java.lang.Override
    public boolean isVisible() {
        return (visible == 'y') || (visible == 'Y');
    }

    public java.lang.Long getViewInstanceId() {
        return viewInstanceId;
    }

    public void setViewInstanceId(java.lang.Long viewInstanceId) {
        this.viewInstanceId = viewInstanceId;
    }

    public void setViewName(java.lang.String viewName) {
        this.viewName = viewName;
    }

    public java.lang.String getName() {
        return name;
    }

    public void setName(java.lang.String name) {
        this.name = name;
    }

    public void setLabel(java.lang.String label) {
        this.label = label;
    }

    public void setDescription(java.lang.String description) {
        this.description = description;
    }

    public void setClusterHandle(java.lang.Long clusterHandle) {
        this.clusterHandle = clusterHandle;
    }

    @java.lang.Override
    public org.apache.ambari.view.ClusterType getClusterType() {
        return clusterType;
    }

    public void setClusterType(org.apache.ambari.view.ClusterType clusterType) {
        this.clusterType = clusterType;
    }

    public void setVisible(boolean visible) {
        this.visible = (visible) ? 'Y' : 'N';
    }

    public java.lang.String getIcon() {
        return icon;
    }

    public void setIcon(java.lang.String icon) {
        this.icon = icon;
    }

    public java.lang.String getIcon64() {
        return icon64;
    }

    public void setIcon64(java.lang.String icon64) {
        this.icon64 = icon64;
    }

    public boolean isXmlDriven() {
        return (xmlDriven == 'y') || (xmlDriven == 'Y');
    }

    public void setXmlDriven(boolean xmlDriven) {
        this.xmlDriven = (xmlDriven) ? 'Y' : 'N';
    }

    public boolean alterNames() {
        return alterNames == 1;
    }

    public void setAlterNames(boolean alterNames) {
        this.alterNames = (alterNames) ? 1 : 0;
    }

    public java.util.Collection<org.apache.ambari.server.orm.entities.ViewInstancePropertyEntity> getProperties() {
        return properties;
    }

    public void putProperty(java.lang.String key, java.lang.String value) {
        removeProperty(key);
        org.apache.ambari.server.orm.entities.ViewInstancePropertyEntity viewInstancePropertyEntity = new org.apache.ambari.server.orm.entities.ViewInstancePropertyEntity();
        viewInstancePropertyEntity.setViewName(viewName);
        viewInstancePropertyEntity.setViewInstanceName(name);
        viewInstancePropertyEntity.setName(key);
        viewInstancePropertyEntity.setValue(value);
        viewInstancePropertyEntity.setViewInstanceEntity(this);
        properties.add(viewInstancePropertyEntity);
    }

    public void removeProperty(java.lang.String key) {
        org.apache.ambari.server.orm.entities.ViewInstancePropertyEntity entity = getProperty(key);
        if (entity != null) {
            properties.remove(entity);
        }
    }

    public org.apache.ambari.server.orm.entities.ViewInstancePropertyEntity getProperty(java.lang.String key) {
        for (org.apache.ambari.server.orm.entities.ViewInstancePropertyEntity viewInstancePropertyEntity : properties) {
            if (viewInstancePropertyEntity.getName().equals(key)) {
                return viewInstancePropertyEntity;
            }
        }
        return null;
    }

    public void setProperties(java.util.Collection<org.apache.ambari.server.orm.entities.ViewInstancePropertyEntity> properties) {
        this.properties = properties;
    }

    public java.util.Collection<org.apache.ambari.server.orm.entities.ViewInstanceDataEntity> getData() {
        return data;
    }

    public void setData(java.util.Collection<org.apache.ambari.server.orm.entities.ViewInstanceDataEntity> data) {
        this.data = data;
    }

    public java.util.Collection<org.apache.ambari.server.orm.entities.ViewEntityEntity> getEntities() {
        return entities;
    }

    public void setEntities(java.util.Collection<org.apache.ambari.server.orm.entities.ViewEntityEntity> entities) {
        this.entities = entities;
    }

    public void putInstanceData(java.lang.String key, java.lang.String value) {
        removeInstanceData(key);
        org.apache.ambari.server.orm.entities.ViewInstanceDataEntity viewInstanceDataEntity = new org.apache.ambari.server.orm.entities.ViewInstanceDataEntity();
        viewInstanceDataEntity.setViewName(viewName);
        viewInstanceDataEntity.setViewInstanceName(name);
        viewInstanceDataEntity.setName(key);
        viewInstanceDataEntity.setUser(getCurrentUserName());
        viewInstanceDataEntity.setValue(value);
        viewInstanceDataEntity.setViewInstanceEntity(this);
        data.add(viewInstanceDataEntity);
    }

    public void removeInstanceData(java.lang.String key) {
        org.apache.ambari.server.orm.entities.ViewInstanceDataEntity entity = getInstanceData(key);
        if (entity != null) {
            data.remove(entity);
        }
    }

    public org.apache.ambari.server.orm.entities.ViewInstanceDataEntity getInstanceData(java.lang.String key) {
        java.lang.String user = getCurrentUserName();
        for (org.apache.ambari.server.orm.entities.ViewInstanceDataEntity viewInstanceDataEntity : data) {
            if (viewInstanceDataEntity.getName().equals(key) && viewInstanceDataEntity.getUser().equals(user)) {
                return viewInstanceDataEntity;
            }
        }
        return null;
    }

    public org.apache.ambari.server.orm.entities.ViewEntity getViewEntity() {
        return view;
    }

    public void setViewEntity(org.apache.ambari.server.orm.entities.ViewEntity view) {
        this.view = view;
    }

    public org.apache.ambari.server.view.configuration.InstanceConfig getConfiguration() {
        return instanceConfig;
    }

    public void addResourceProvider(org.apache.ambari.server.controller.spi.Resource.Type type, org.apache.ambari.view.ResourceProvider provider) {
        resourceProviders.put(type, provider);
    }

    public org.apache.ambari.view.ResourceProvider getResourceProvider(org.apache.ambari.server.controller.spi.Resource.Type type) {
        return resourceProviders.get(type);
    }

    public org.apache.ambari.view.ResourceProvider getResourceProvider(java.lang.String type) {
        java.lang.String typeName = view.getQualifiedResourceTypeName(type);
        return resourceProviders.get(org.apache.ambari.server.controller.spi.Resource.Type.valueOf(typeName));
    }

    public void addService(java.lang.String pluralName, java.lang.Object service) {
        services.put(pluralName, service);
    }

    public java.lang.Object getService(java.lang.String pluralName) {
        return services.get(pluralName);
    }

    public java.lang.String getContextPath() {
        return org.apache.ambari.server.orm.entities.ViewInstanceEntity.getContextPath(view.getCommonName(), view.getVersion(), getName());
    }

    public static java.lang.String getContextPath(java.lang.String viewName, java.lang.String version, java.lang.String viewInstanceName) {
        return ((((org.apache.ambari.server.security.authorization.AmbariAuthorizationFilter.VIEWS_CONTEXT_PATH_PREFIX + viewName) + "/") + version) + "/") + viewInstanceName;
    }

    public java.lang.String getUsername() {
        return org.apache.ambari.server.security.authorization.AuthorizationHelper.resolveLoginAliasToUserName(securityHelper.getCurrentUserName());
    }

    public org.apache.ambari.server.orm.entities.ResourceEntity getResource() {
        return resource;
    }

    public void setResource(org.apache.ambari.server.orm.entities.ResourceEntity resource) {
        this.resource = resource;
    }

    public org.apache.ambari.view.migration.ViewDataMigrator getDataMigrator(org.apache.ambari.view.migration.ViewDataMigrationContext dataMigrationContext) throws java.lang.ClassNotFoundException {
        if (view != null) {
            if ((dataMigrator == null) && (view.getConfiguration().getDataMigrator() != null)) {
                java.lang.ClassLoader cl = view.getClassLoader();
                dataMigrator = org.apache.ambari.server.orm.entities.ViewInstanceEntity.getDataMigrator(view.getConfiguration().getDataMigratorClass(cl), new org.apache.ambari.server.view.ViewContextImpl(view, org.apache.ambari.server.view.ViewRegistry.getInstance()), dataMigrationContext);
            }
        }
        return dataMigrator;
    }

    private static org.apache.ambari.view.migration.ViewDataMigrator getDataMigrator(java.lang.Class<? extends org.apache.ambari.view.migration.ViewDataMigrator> clazz, final org.apache.ambari.view.ViewContext viewContext, final org.apache.ambari.view.migration.ViewDataMigrationContext dataMigrationContext) {
        com.google.inject.Injector viewInstanceInjector = com.google.inject.Guice.createInjector(new com.google.inject.AbstractModule() {
            @java.lang.Override
            protected void configure() {
                bind(org.apache.ambari.view.ViewContext.class).toInstance(viewContext);
                bind(org.apache.ambari.view.migration.ViewDataMigrationContext.class).toInstance(dataMigrationContext);
            }
        });
        return viewInstanceInjector.getInstance(clazz);
    }

    public void validate(org.apache.ambari.server.orm.entities.ViewEntity viewEntity, org.apache.ambari.view.validation.Validator.ValidationContext context) throws org.apache.ambari.server.view.validation.ValidationException {
        org.apache.ambari.server.view.validation.InstanceValidationResultImpl result = getValidationResult(viewEntity, context);
        if (!result.isValid()) {
            throw new org.apache.ambari.server.view.validation.ValidationException(result.toJson());
        }
    }

    public org.apache.ambari.server.view.validation.InstanceValidationResultImpl getValidationResult(org.apache.ambari.server.orm.entities.ViewEntity viewEntity, org.apache.ambari.view.validation.Validator.ValidationContext context) throws java.lang.IllegalStateException {
        java.util.Map<java.lang.String, org.apache.ambari.view.validation.ValidationResult> propertyResults = new java.util.HashMap<>();
        if (context.equals(org.apache.ambari.view.validation.Validator.ValidationContext.PRE_CREATE) || context.equals(org.apache.ambari.view.validation.Validator.ValidationContext.PRE_UPDATE)) {
            java.util.Set<java.lang.String> requiredParameterNames = new java.util.HashSet<>();
            for (org.apache.ambari.server.orm.entities.ViewParameterEntity parameter : viewEntity.getParameters()) {
                if (parameter.isRequired()) {
                    if (parameter.getClusterConfig() == null) {
                        requiredParameterNames.add(parameter.getName());
                    }
                }
            }
            java.util.Map<java.lang.String, java.lang.String> propertyMap = getPropertyMap();
            for (java.util.Map.Entry<java.lang.String, java.lang.String> entry : propertyMap.entrySet()) {
                if (entry.getValue() != null) {
                    requiredParameterNames.remove(entry.getKey());
                }
            }
            for (java.lang.String requiredParameterName : requiredParameterNames) {
                propertyResults.put(requiredParameterName, new org.apache.ambari.server.view.validation.ValidationResultImpl(false, ("No property values exist for the required parameter " + requiredParameterName) + "."));
            }
        }
        org.apache.ambari.view.validation.ValidationResult instanceResult = null;
        org.apache.ambari.view.validation.Validator validator = viewEntity.getValidator();
        if (validator != null) {
            instanceResult = validator.validateInstance(this, context);
            for (java.lang.String property : getPropertyMap().keySet()) {
                if (!propertyResults.containsKey(property)) {
                    propertyResults.put(property, org.apache.ambari.server.view.validation.ValidationResultImpl.create(validator.validateProperty(property, this, context)));
                }
            }
        }
        return new org.apache.ambari.server.view.validation.InstanceValidationResultImpl(org.apache.ambari.server.view.validation.ValidationResultImpl.create(instanceResult), propertyResults);
    }

    public java.lang.String getCurrentUserName() {
        java.lang.String currentUserName = getUsername();
        return (currentUserName == null) || (currentUserName.length() == 0) ? " " : currentUserName;
    }

    protected void setSecurityHelper(org.apache.ambari.server.security.SecurityHelper securityHelper) {
        this.securityHelper = securityHelper;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if ((o == null) || (getClass() != o.getClass())) {
            return false;
        }
        org.apache.ambari.server.orm.entities.ViewInstanceEntity that = ((org.apache.ambari.server.orm.entities.ViewInstanceEntity) (o));
        return name.equals(that.name) && viewName.equals(that.viewName);
    }

    @java.lang.Override
    public int hashCode() {
        int result = viewName.hashCode();
        result = (31 * result) + name.hashCode();
        return result;
    }

    public org.apache.ambari.server.orm.entities.ViewURLEntity getViewUrl() {
        return viewUrl;
    }

    public void setViewUrl(org.apache.ambari.server.orm.entities.ViewURLEntity viewUrl) {
        this.viewUrl = viewUrl;
    }

    public void clearUrl() {
        viewUrl = null;
    }

    public static class ViewInstanceVersionDTO {
        private final java.lang.String viewName;

        private final java.lang.String version;

        private final java.lang.String instanceName;

        public ViewInstanceVersionDTO(java.lang.String viewName, java.lang.String version, java.lang.String instanceName) {
            this.viewName = viewName;
            this.version = version;
            this.instanceName = instanceName;
        }

        public java.lang.String getViewName() {
            return viewName;
        }

        public java.lang.String getVersion() {
            return version;
        }

        public java.lang.String getInstanceName() {
            return instanceName;
        }
    }

    @java.lang.Override
    public java.lang.String toString() {
        return ((((((((((("ViewInstanceEntity{" + "viewInstanceId=") + viewInstanceId) + ", viewName='") + viewName) + '\'') + ", name='") + name) + '\'') + ", label='") + label) + '\'') + '}';
    }
}