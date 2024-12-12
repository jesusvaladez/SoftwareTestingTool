package org.apache.ambari.server.orm.entities;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
@javax.persistence.Table(name = "viewmain")
@javax.persistence.NamedQuery(name = "allViews", query = "SELECT view FROM ViewEntity view")
@javax.persistence.Entity
public class ViewEntity implements org.apache.ambari.view.ViewDefinition {
    public static final java.lang.String AMBARI_ONLY = "AMBARI-ONLY";

    @javax.persistence.Id
    @javax.persistence.Column(name = "view_name", nullable = false, insertable = true, updatable = false, unique = true, length = 100)
    private java.lang.String name;

    @javax.persistence.Column
    @javax.persistence.Basic
    private java.lang.String label;

    @javax.persistence.Column
    @javax.persistence.Basic
    private java.lang.String description;

    @javax.persistence.Column
    @javax.persistence.Basic
    private java.lang.String icon;

    @javax.persistence.Column
    @javax.persistence.Basic
    private java.lang.String icon64;

    @javax.persistence.Column
    @javax.persistence.Basic
    private java.lang.String version;

    @javax.persistence.Column
    @javax.persistence.Basic
    private java.lang.String build;

    @javax.persistence.Column
    @javax.persistence.Basic
    private java.lang.String archive;

    @javax.persistence.Column
    @javax.persistence.Basic
    private java.lang.String mask;

    @javax.persistence.Column(name = "system_view")
    @javax.persistence.Basic
    private java.lang.Integer system;

    @javax.persistence.OneToMany(cascade = javax.persistence.CascadeType.ALL, mappedBy = "view")
    private java.util.Collection<org.apache.ambari.server.orm.entities.ViewParameterEntity> parameters = new java.util.HashSet<>();

    @javax.persistence.OneToMany(cascade = javax.persistence.CascadeType.ALL, mappedBy = "view")
    private java.util.Collection<org.apache.ambari.server.orm.entities.ViewResourceEntity> resources = new java.util.HashSet<>();

    @javax.persistence.OneToMany(cascade = javax.persistence.CascadeType.ALL, mappedBy = "view")
    private java.util.Collection<org.apache.ambari.server.orm.entities.ViewInstanceEntity> instances = new java.util.HashSet<>();

    @javax.persistence.OneToMany(cascade = javax.persistence.CascadeType.ALL)
    @javax.persistence.JoinColumns({ @javax.persistence.JoinColumn(name = "resource_type_id", referencedColumnName = "resource_type_id", nullable = false) })
    private java.util.Collection<org.apache.ambari.server.orm.entities.PermissionEntity> permissions = new java.util.HashSet<>();

    @javax.persistence.ManyToOne(cascade = javax.persistence.CascadeType.ALL)
    @javax.persistence.JoinColumns({ @javax.persistence.JoinColumn(name = "resource_type_id", referencedColumnName = "resource_type_id", nullable = false) })
    private org.apache.ambari.server.orm.entities.ResourceTypeEntity resourceType;

    @javax.persistence.Transient
    private org.apache.ambari.server.view.configuration.ViewConfig configuration;

    @javax.persistence.Transient
    private final org.apache.ambari.server.configuration.Configuration ambariConfiguration;

    @javax.persistence.Transient
    private final org.apache.ambari.server.controller.spi.Resource.Type externalResourceType;

    @javax.persistence.Transient
    private java.lang.ClassLoader classLoader = null;

    @javax.persistence.Transient
    private final java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, org.apache.ambari.server.controller.spi.ResourceProvider> resourceProviders = new java.util.HashMap<>();

    @javax.persistence.Transient
    private final java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, org.apache.ambari.server.view.ViewSubResourceDefinition> resourceDefinitions = new java.util.HashMap<>();

    @javax.persistence.Transient
    private final java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, org.apache.ambari.server.view.configuration.ResourceConfig> resourceConfigurations = new java.util.HashMap<>();

    @javax.persistence.Transient
    private java.lang.String commonName = null;

    @javax.persistence.Transient
    private org.apache.ambari.view.View view = null;

    @javax.persistence.Transient
    private org.apache.ambari.view.validation.Validator validator = null;

    @javax.persistence.Transient
    private org.apache.ambari.view.ViewDefinition.ViewStatus status = org.apache.ambari.view.ViewDefinition.ViewStatus.PENDING;

    @javax.persistence.Transient
    private java.lang.String statusDetail;

    @javax.persistence.Transient
    private boolean clusterConfigurable;

    public ViewEntity() {
        this.configuration = null;
        this.ambariConfiguration = null;
        this.archive = null;
        this.externalResourceType = null;
        this.system = 0;
        this.clusterConfigurable = false;
    }

    public ViewEntity(org.apache.ambari.server.view.configuration.ViewConfig configuration, org.apache.ambari.server.configuration.Configuration ambariConfiguration, java.lang.String archivePath) {
        setConfiguration(configuration);
        this.ambariConfiguration = ambariConfiguration;
        this.archive = archivePath;
        java.lang.String version = configuration.getVersion();
        this.name = org.apache.ambari.server.orm.entities.ViewEntity.getViewName(configuration.getName(), version);
        this.label = configuration.getLabel();
        this.description = configuration.getDescription();
        this.version = version;
        this.build = configuration.getBuild();
        this.mask = configuration.getMasker();
        this.icon = configuration.getIcon();
        this.icon64 = configuration.getIcon64();
        this.system = (configuration.isSystem()) ? 1 : 0;
        this.externalResourceType = new org.apache.ambari.server.controller.spi.Resource.Type(getQualifiedResourceTypeName(org.apache.ambari.server.view.configuration.ResourceConfig.EXTERNAL_RESOURCE_PLURAL_NAME));
    }

    @java.lang.Override
    public java.lang.String getViewName() {
        return getCommonName();
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
    public java.lang.String getVersion() {
        return version;
    }

    @java.lang.Override
    public java.lang.String getBuild() {
        return build;
    }

    @java.lang.Override
    public org.apache.ambari.view.ViewDefinition.ViewStatus getStatus() {
        return status;
    }

    @java.lang.Override
    public java.lang.String getStatusDetail() {
        return statusDetail;
    }

    @java.lang.Override
    public java.lang.String getMask() {
        return mask;
    }

    public java.lang.String getName() {
        return name;
    }

    public void setName(java.lang.String name) {
        this.name = name;
    }

    public synchronized java.lang.String getCommonName() {
        if (commonName == null) {
            commonName = name.replaceAll("\\{(.+)\\}", "");
        }
        return commonName;
    }

    public void setLabel(java.lang.String label) {
        this.label = label;
    }

    public void setDescription(java.lang.String description) {
        this.description = description;
    }

    public void setVersion(java.lang.String version) {
        this.version = version;
    }

    public void setBuild(java.lang.String build) {
        this.build = build;
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

    public java.util.Collection<org.apache.ambari.server.orm.entities.ViewParameterEntity> getParameters() {
        return parameters;
    }

    public void setParameters(java.util.Collection<org.apache.ambari.server.orm.entities.ViewParameterEntity> parameters) {
        this.parameters = parameters;
    }

    public java.util.Collection<org.apache.ambari.server.orm.entities.PermissionEntity> getPermissions() {
        return permissions;
    }

    public void setPermissions(java.util.Collection<org.apache.ambari.server.orm.entities.PermissionEntity> permissions) {
        this.permissions = permissions;
    }

    public org.apache.ambari.server.orm.entities.PermissionEntity getPermission(java.lang.String permissionName) {
        for (org.apache.ambari.server.orm.entities.PermissionEntity permissionEntity : permissions) {
            if (permissionEntity.getPermissionName().equals(permissionName)) {
                return permissionEntity;
            }
        }
        return null;
    }

    public java.util.Collection<org.apache.ambari.server.orm.entities.ViewResourceEntity> getResources() {
        return resources;
    }

    public void setResources(java.util.Collection<org.apache.ambari.server.orm.entities.ViewResourceEntity> resources) {
        this.resources = resources;
    }

    public java.util.Collection<org.apache.ambari.server.orm.entities.ViewInstanceEntity> getInstances() {
        return instances;
    }

    public void setInstances(java.util.Collection<org.apache.ambari.server.orm.entities.ViewInstanceEntity> instances) {
        this.instances = instances;
    }

    public void addInstanceDefinition(org.apache.ambari.server.orm.entities.ViewInstanceEntity viewInstanceDefinition) {
        removeInstanceDefinition(viewInstanceDefinition.getName());
        instances.add(viewInstanceDefinition);
    }

    public void removeInstanceDefinition(java.lang.String instanceName) {
        org.apache.ambari.server.orm.entities.ViewInstanceEntity entity = getInstanceDefinition(instanceName);
        if (entity != null) {
            instances.remove(entity);
        }
    }

    public org.apache.ambari.server.orm.entities.ViewInstanceEntity getInstanceDefinition(java.lang.String instanceName) {
        for (org.apache.ambari.server.orm.entities.ViewInstanceEntity viewInstanceEntity : instances) {
            if (viewInstanceEntity.getName().equals(instanceName)) {
                return viewInstanceEntity;
            }
        }
        return null;
    }

    public java.lang.String getArchive() {
        return archive;
    }

    public void setArchive(java.lang.String archive) {
        this.archive = archive;
    }

    public java.lang.String getAmbariProperty(java.lang.String key) {
        return ambariConfiguration.getProperty(key);
    }

    public org.apache.ambari.server.configuration.Configuration getAmbariConfiguration() {
        return ambariConfiguration;
    }

    public java.lang.String getQualifiedResourceTypeName(java.lang.String resourceTypeName) {
        return (getName() + "/") + resourceTypeName;
    }

    public org.apache.ambari.server.controller.spi.Resource.Type getExternalResourceType() {
        return externalResourceType;
    }

    public java.lang.ClassLoader getClassLoader() {
        return classLoader;
    }

    public void setClassLoader(java.lang.ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public void addResourceProvider(org.apache.ambari.server.controller.spi.Resource.Type type, org.apache.ambari.server.controller.spi.ResourceProvider provider) {
        resourceProviders.put(type, provider);
    }

    public org.apache.ambari.server.controller.spi.ResourceProvider getResourceProvider(org.apache.ambari.server.controller.spi.Resource.Type type) {
        return resourceProviders.get(type);
    }

    public void addResourceDefinition(org.apache.ambari.server.view.ViewSubResourceDefinition definition) {
        resourceDefinitions.put(definition.getType(), definition);
    }

    public org.apache.ambari.server.view.ViewSubResourceDefinition getResourceDefinition(org.apache.ambari.server.controller.spi.Resource.Type type) {
        return resourceDefinitions.get(type);
    }

    public java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, org.apache.ambari.server.view.ViewSubResourceDefinition> getResourceDefinitions() {
        return resourceDefinitions;
    }

    public void addResourceConfiguration(org.apache.ambari.server.controller.spi.Resource.Type type, org.apache.ambari.server.view.configuration.ResourceConfig config) {
        resourceConfigurations.put(type, config);
    }

    public java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, org.apache.ambari.server.view.configuration.ResourceConfig> getResourceConfigurations() {
        return resourceConfigurations;
    }

    public java.util.Set<org.apache.ambari.server.controller.spi.Resource.Type> getViewResourceTypes() {
        return resourceProviders.keySet();
    }

    public void setConfiguration(org.apache.ambari.server.view.configuration.ViewConfig configuration) {
        this.configuration = configuration;
        this.clusterConfigurable = false;
        if ((configuration.getClusterConfigOptions() != null) && configuration.getClusterConfigOptions().equals(org.apache.ambari.server.orm.entities.ViewEntity.AMBARI_ONLY)) {
            this.clusterConfigurable = true;
            return;
        }
        for (org.apache.ambari.server.view.configuration.ParameterConfig parameterConfig : configuration.getParameters()) {
            java.lang.String clusterConfig = parameterConfig.getClusterConfig();
            if ((clusterConfig != null) && (!clusterConfig.isEmpty())) {
                this.clusterConfigurable = true;
                return;
            }
        }
    }

    public org.apache.ambari.server.view.configuration.ViewConfig getConfiguration() {
        return configuration;
    }

    public void setView(org.apache.ambari.view.View view) {
        this.view = view;
    }

    public org.apache.ambari.view.View getView() {
        return view;
    }

    public void setValidator(org.apache.ambari.view.validation.Validator validator) {
        this.validator = validator;
    }

    public org.apache.ambari.view.validation.Validator getValidator() {
        return validator;
    }

    public boolean hasValidator() {
        return validator != null;
    }

    public void setMask(java.lang.String mask) {
        this.mask = mask;
    }

    public boolean isSystem() {
        return system == 1;
    }

    public void setSystem(boolean required) {
        this.system = (required) ? 1 : 0;
    }

    public org.apache.ambari.server.orm.entities.ResourceTypeEntity getResourceType() {
        return resourceType;
    }

    public void setResourceType(org.apache.ambari.server.orm.entities.ResourceTypeEntity resourceType) {
        this.resourceType = resourceType;
    }

    public void setStatus(org.apache.ambari.view.ViewDefinition.ViewStatus status) {
        this.status = status;
    }

    public void setStatusDetail(java.lang.String statusDetail) {
        this.statusDetail = statusDetail;
    }

    public boolean isClusterConfigurable() {
        return clusterConfigurable;
    }

    public boolean isDeployed() {
        return status.equals(org.apache.ambari.view.ViewDefinition.ViewStatus.DEPLOYED);
    }

    public static java.lang.String getViewName(java.lang.String name, java.lang.String version) {
        return ((name + "{") + version) + "}";
    }

    @java.lang.Override
    public java.lang.String toString() {
        return ((((((((("ViewEntity{" + "name='") + name) + '\'') + ", label='") + label) + '\'') + ", description='") + description) + '\'') + '}';
    }
}