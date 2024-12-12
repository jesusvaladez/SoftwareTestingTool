package org.apache.ambari.server.state;
import javax.annotation.Nullable;
public class StackInfo implements java.lang.Comparable<org.apache.ambari.server.state.StackInfo> , org.apache.ambari.server.stack.Validable {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.state.StackInfo.class);

    private java.lang.String minJdk;

    private java.lang.String maxJdk;

    private java.lang.String name;

    private java.lang.String version;

    private java.lang.String minUpgradeVersion;

    private boolean active;

    private java.lang.String rcoFileLocation;

    private java.lang.String kerberosDescriptorPreConfigurationFileLocation;

    private java.util.List<org.apache.ambari.server.state.RepositoryInfo> repositories;

    private java.util.Collection<org.apache.ambari.server.state.ServiceInfo> services;

    private java.util.Collection<org.apache.ambari.server.state.ExtensionInfo> extensions;

    private java.lang.String parentStackVersion;

    private java.util.List<org.apache.ambari.server.state.PropertyInfo> properties;

    private java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>> configTypes;

    private java.util.Map<java.lang.String, org.apache.ambari.server.stack.upgrade.UpgradePack> upgradePacks;

    private org.apache.ambari.server.stack.upgrade.ConfigUpgradePack configUpgradePack;

    private org.apache.ambari.server.state.stack.StackRoleCommandOrder roleCommandOrder;

    private boolean valid = true;

    private java.util.Map<java.lang.String, java.util.Map<org.apache.ambari.server.state.PropertyInfo.PropertyType, java.util.Set<java.lang.String>>> propertiesTypesCache = java.util.Collections.synchronizedMap(new java.util.HashMap<java.lang.String, java.util.Map<org.apache.ambari.server.state.PropertyInfo.PropertyType, java.util.Set<java.lang.String>>>());

    private java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>> configPropertyAttributes = null;

    private java.lang.String upgradesFolder = null;

    private volatile java.util.Map<java.lang.String, org.apache.ambari.server.state.PropertyInfo> requiredProperties;

    private java.util.Map<java.lang.String, org.apache.ambari.server.state.repository.VersionDefinitionXml> versionDefinitions = new java.util.concurrent.ConcurrentHashMap<>();

    private java.util.Set<java.lang.String> errorSet = new java.util.HashSet<>();

    private org.apache.ambari.server.state.stack.RepositoryXml repoXml = null;

    private org.apache.ambari.server.state.repository.VersionDefinitionXml latestVersion = null;

    private java.lang.String releaseVersionClass = null;

    private java.net.URLClassLoader libraryClassLoader = null;

    private java.util.List<java.lang.String> removedServices = new java.util.ArrayList<>();

    private java.util.List<java.lang.String> servicesWithNoConfigs = new java.util.ArrayList<>();

    private org.apache.ambari.server.state.RefreshCommandConfiguration refreshCommandConfiguration = new org.apache.ambari.server.state.RefreshCommandConfiguration();

    public java.lang.String getMinJdk() {
        return minJdk;
    }

    public void setMinJdk(java.lang.String minJdk) {
        this.minJdk = minJdk;
    }

    public java.lang.String getMaxJdk() {
        return maxJdk;
    }

    public void setMaxJdk(java.lang.String maxJdk) {
        this.maxJdk = maxJdk;
    }

    public void setReleaseVersionClass(java.lang.String className) {
        releaseVersionClass = className;
    }

    @java.lang.Override
    public boolean isValid() {
        return valid;
    }

    @java.lang.Override
    public void setValid(boolean valid) {
        this.valid = valid;
    }

    @java.lang.Override
    public void addError(java.lang.String error) {
        errorSet.add(error);
    }

    @java.lang.Override
    public java.util.Collection<java.lang.String> getErrors() {
        return errorSet;
    }

    @java.lang.Override
    public void addErrors(java.util.Collection<java.lang.String> errors) {
        errorSet.addAll(errors);
    }

    public java.lang.String getName() {
        return name;
    }

    public void setName(java.lang.String name) {
        this.name = name;
    }

    public java.lang.String getVersion() {
        return version;
    }

    public void setVersion(java.lang.String version) {
        this.version = version;
    }

    public java.util.List<org.apache.ambari.server.state.RepositoryInfo> getRepositories() {
        if (repositories == null) {
            repositories = new java.util.ArrayList<>();
        }
        return repositories;
    }

    public com.google.common.collect.ListMultimap<java.lang.String, org.apache.ambari.server.state.RepositoryInfo> getRepositoriesByOs() {
        return com.google.common.collect.Multimaps.index(getRepositories(), org.apache.ambari.server.state.RepositoryInfo.GET_OSTYPE_FUNCTION);
    }

    public synchronized java.util.Collection<org.apache.ambari.server.state.ServiceInfo> getServices() {
        if (services == null) {
            services = new java.util.ArrayList<>();
        }
        return services;
    }

    public org.apache.ambari.server.state.ServiceInfo getService(java.lang.String name) {
        java.util.Collection<org.apache.ambari.server.state.ServiceInfo> services = getServices();
        for (org.apache.ambari.server.state.ServiceInfo service : services) {
            if (service.getName().equals(name)) {
                return service;
            }
        }
        return null;
    }

    public synchronized void setServices(java.util.Collection<org.apache.ambari.server.state.ServiceInfo> services) {
        this.services = services;
    }

    public synchronized java.util.Collection<org.apache.ambari.server.state.ExtensionInfo> getExtensions() {
        if (extensions == null) {
            extensions = new java.util.ArrayList<>();
        }
        return extensions;
    }

    public org.apache.ambari.server.state.ExtensionInfo getExtension(java.lang.String name) {
        java.util.Collection<org.apache.ambari.server.state.ExtensionInfo> extensions = getExtensions();
        for (org.apache.ambari.server.state.ExtensionInfo extension : extensions) {
            if (extension.getName().equals(name)) {
                return extension;
            }
        }
        return null;
    }

    public org.apache.ambari.server.state.ExtensionInfo getExtensionByService(java.lang.String serviceName) {
        java.util.Collection<org.apache.ambari.server.state.ExtensionInfo> extensions = getExtensions();
        for (org.apache.ambari.server.state.ExtensionInfo extension : extensions) {
            java.util.Collection<org.apache.ambari.server.state.ServiceInfo> services = extension.getServices();
            for (org.apache.ambari.server.state.ServiceInfo service : services) {
                if (service.getName().equals(serviceName)) {
                    return extension;
                }
            }
        }
        return null;
    }

    public void addExtension(org.apache.ambari.server.state.ExtensionInfo extension) {
        java.util.Collection<org.apache.ambari.server.state.ExtensionInfo> extensions = getExtensions();
        extensions.add(extension);
        java.util.Collection<org.apache.ambari.server.state.ServiceInfo> services = getServices();
        services.addAll(extension.getServices());
    }

    public void removeExtension(org.apache.ambari.server.state.ExtensionInfo extension) {
        java.util.Collection<org.apache.ambari.server.state.ExtensionInfo> extensions = getExtensions();
        extensions.remove(extension);
        java.util.Collection<org.apache.ambari.server.state.ServiceInfo> services = getServices();
        for (org.apache.ambari.server.state.ServiceInfo service : extension.getServices()) {
            services.remove(service);
        }
    }

    public java.util.List<org.apache.ambari.server.state.PropertyInfo> getProperties() {
        if (properties == null) {
            properties = new java.util.ArrayList<>();
        }
        return properties;
    }

    public void setProperties(java.util.List<org.apache.ambari.server.state.PropertyInfo> properties) {
        this.properties = properties;
    }

    public synchronized java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>> getConfigTypeAttributes() {
        return configTypes == null ? java.util.Collections.emptyMap() : java.util.Collections.unmodifiableMap(configTypes);
    }

    public synchronized void setConfigTypeAttributes(java.lang.String type, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> typeAttributes) {
        if (configTypes == null) {
            configTypes = new java.util.HashMap<>();
        }
        configTypes.put(type, typeAttributes);
    }

    public synchronized void setAllConfigAttributes(java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>> types) {
        configTypes = new java.util.HashMap<>();
        for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>> entry : types.entrySet()) {
            setConfigTypeAttributes(entry.getKey(), entry.getValue());
        }
    }

    @java.lang.Override
    public java.lang.String toString() {
        java.lang.StringBuilder sb = new java.lang.StringBuilder((((((("Stack name:" + name) + "\nversion:") + version) + "\nactive:") + active) + " \nvalid:") + isValid());
        if (services != null) {
            sb.append("\n\t\tService:");
            for (org.apache.ambari.server.state.ServiceInfo service : services) {
                sb.append("\t\t");
                sb.append(service);
            }
        }
        if (repositories != null) {
            sb.append("\n\t\tRepositories:");
            for (org.apache.ambari.server.state.RepositoryInfo repository : repositories) {
                sb.append("\t\t");
                sb.append(repository);
            }
        }
        return sb.toString();
    }

    @java.lang.Override
    public int hashCode() {
        return (31 + name.hashCode()) + version.hashCode();
    }

    @java.lang.Override
    public boolean equals(java.lang.Object obj) {
        if (!(obj instanceof org.apache.ambari.server.state.StackInfo)) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        org.apache.ambari.server.state.StackInfo stackInfo = ((org.apache.ambari.server.state.StackInfo) (obj));
        return getName().equals(stackInfo.getName()) && getVersion().equals(stackInfo.getVersion());
    }

    public org.apache.ambari.server.controller.StackVersionResponse convertToResponse() {
        java.util.Collection<org.apache.ambari.server.state.ServiceInfo> serviceInfos = getServices();
        java.util.Collection<java.io.File> serviceDescriptorFiles = new java.util.HashSet<>();
        if (serviceInfos != null) {
            for (org.apache.ambari.server.state.ServiceInfo serviceInfo : serviceInfos) {
                java.io.File file = serviceInfo.getKerberosDescriptorFile();
                if (file != null) {
                    serviceDescriptorFiles.add(file);
                }
            }
        }
        return new org.apache.ambari.server.controller.StackVersionResponse(getVersion(), isActive(), getParentStackVersion(), getConfigTypeAttributes(), serviceDescriptorFiles, null == upgradePacks ? java.util.Collections.emptySet() : upgradePacks.keySet(), isValid(), getErrors(), getMinJdk(), getMaxJdk());
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public java.lang.String getParentStackVersion() {
        return parentStackVersion;
    }

    public void setParentStackVersion(java.lang.String parentStackVersion) {
        this.parentStackVersion = parentStackVersion;
    }

    public org.apache.ambari.server.state.stack.StackRoleCommandOrder getRoleCommandOrder() {
        return roleCommandOrder;
    }

    public void setRoleCommandOrder(org.apache.ambari.server.state.stack.StackRoleCommandOrder roleCommandOrder) {
        this.roleCommandOrder = roleCommandOrder;
    }

    public java.lang.String getRcoFileLocation() {
        return rcoFileLocation;
    }

    public void setRcoFileLocation(java.lang.String rcoFileLocation) {
        this.rcoFileLocation = rcoFileLocation;
    }

    public java.lang.String getKerberosDescriptorPreConfigurationFileLocation() {
        return kerberosDescriptorPreConfigurationFileLocation;
    }

    public void setKerberosDescriptorPreConfigurationFileLocation(java.lang.String kerberosDescriptorPreConfigurationFileLocation) {
        this.kerberosDescriptorPreConfigurationFileLocation = kerberosDescriptorPreConfigurationFileLocation;
    }

    public void setUpgradesFolder(java.lang.String path) {
        upgradesFolder = path;
    }

    public java.lang.String getUpgradesFolder() {
        return upgradesFolder;
    }

    public java.util.Map<java.lang.String, org.apache.ambari.server.stack.upgrade.UpgradePack> getUpgradePacks() {
        return upgradePacks;
    }

    public void setUpgradePacks(java.util.Map<java.lang.String, org.apache.ambari.server.stack.upgrade.UpgradePack> upgradePacks) {
        if (null != upgradePacks) {
            upgradePacks.values().forEach(pack -> {
                pack.setOwnerStackId(new org.apache.ambari.server.state.StackId(this));
            });
        }
        this.upgradePacks = upgradePacks;
    }

    public org.apache.ambari.server.stack.upgrade.ConfigUpgradePack getConfigUpgradePack() {
        return configUpgradePack;
    }

    public void setConfigUpgradePack(org.apache.ambari.server.stack.upgrade.ConfigUpgradePack configUpgradePack) {
        this.configUpgradePack = configUpgradePack;
    }

    @java.lang.Override
    public int compareTo(org.apache.ambari.server.state.StackInfo o) {
        if (name.equals(o.name)) {
            return org.apache.ambari.server.utils.VersionUtils.compareVersions(version, o.version);
        }
        return name.compareTo(o.name);
    }

    public java.util.Map<java.lang.String, org.apache.ambari.server.state.PropertyInfo> getRequiredProperties() {
        java.util.Map<java.lang.String, org.apache.ambari.server.state.PropertyInfo> result = requiredProperties;
        if (result == null) {
            synchronized(this) {
                result = requiredProperties;
                if (result == null) {
                    requiredProperties = result = new java.util.HashMap<>();
                    java.util.List<org.apache.ambari.server.state.PropertyInfo> properties = getProperties();
                    for (org.apache.ambari.server.state.PropertyInfo propertyInfo : properties) {
                        if (propertyInfo.isRequireInput()) {
                            result.put(propertyInfo.getName(), propertyInfo);
                        }
                    }
                }
            }
        }
        return result;
    }

    public java.util.Map<org.apache.ambari.server.state.PropertyInfo.PropertyType, java.util.Set<java.lang.String>> getConfigPropertiesTypes(java.lang.String configType) {
        if (!propertiesTypesCache.containsKey(configType)) {
            java.util.Map<org.apache.ambari.server.state.PropertyInfo.PropertyType, java.util.Set<java.lang.String>> propertiesTypes = new java.util.HashMap<>();
            java.util.Collection<org.apache.ambari.server.state.ServiceInfo> services = getServices();
            for (org.apache.ambari.server.state.ServiceInfo serviceInfo : services) {
                for (org.apache.ambari.server.state.PropertyInfo propertyInfo : serviceInfo.getProperties()) {
                    if (propertyInfo.getFilename().contains(configType) && (!propertyInfo.getPropertyTypes().isEmpty())) {
                        java.util.Set<org.apache.ambari.server.state.PropertyInfo.PropertyType> types = propertyInfo.getPropertyTypes();
                        for (org.apache.ambari.server.state.PropertyInfo.PropertyType propertyType : types) {
                            if (!propertiesTypes.containsKey(propertyType)) {
                                propertiesTypes.put(propertyType, new java.util.HashSet<>());
                            }
                            propertiesTypes.get(propertyType).add(propertyInfo.getName());
                        }
                    }
                }
            }
            propertiesTypesCache.put(configType, propertiesTypes);
        }
        return propertiesTypesCache.get(configType);
    }

    public synchronized java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> getDefaultConfigAttributesForConfigType(java.lang.String configType) {
        if (configPropertyAttributes == null) {
            configPropertyAttributes = getDefaultConfigAttributes();
        }
        if (configPropertyAttributes.containsKey(configType)) {
            return configPropertyAttributes.get(configType);
        }
        return null;
    }

    private java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>> getDefaultConfigAttributes() {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>> result = new java.util.HashMap<>();
        for (org.apache.ambari.server.state.ServiceInfo si : services) {
            for (org.apache.ambari.server.state.PropertyInfo pi : si.getProperties()) {
                java.lang.String propertyConfigType = com.google.common.io.Files.getNameWithoutExtension(pi.getFilename());
                java.lang.String propertyName = pi.getName();
                java.lang.String hidden = pi.getPropertyValueAttributes().getHidden();
                if (hidden != null) {
                    if (!result.containsKey(propertyConfigType)) {
                        result.put(propertyConfigType, new java.util.HashMap<>());
                    }
                    if (!result.get(propertyConfigType).containsKey("hidden")) {
                        result.get(propertyConfigType).put("hidden", new java.util.HashMap<>());
                    }
                    result.get(propertyConfigType).get("hidden").put(propertyName, hidden);
                }
            }
        }
        return result;
    }

    public void addVersionDefinition(java.lang.String key, org.apache.ambari.server.state.repository.VersionDefinitionXml xml) {
        versionDefinitions.put(key, xml);
    }

    public java.util.Collection<org.apache.ambari.server.state.repository.VersionDefinitionXml> getVersionDefinitions() {
        return versionDefinitions.values();
    }

    public void setRepositoryXml(org.apache.ambari.server.state.stack.RepositoryXml rxml) {
        repoXml = rxml;
    }

    public org.apache.ambari.server.state.stack.RepositoryXml getRepositoryXml() {
        return repoXml;
    }

    public java.util.List<java.lang.String> getRemovedServices() {
        return removedServices;
    }

    public void setRemovedServices(java.util.List<java.lang.String> removedServices) {
        this.removedServices = removedServices;
    }

    public java.util.List<java.lang.String> getServicesWithNoConfigs() {
        return servicesWithNoConfigs;
    }

    public void setServicesWithNoConfigs(java.util.List<java.lang.String> servicesWithNoConfigs) {
        this.servicesWithNoConfigs = servicesWithNoConfigs;
    }

    public void setLatestVersionDefinition(org.apache.ambari.server.state.repository.VersionDefinitionXml xml) {
        latestVersion = xml;
    }

    public org.apache.ambari.server.state.repository.VersionDefinitionXml getLatestVersionDefinition() {
        return latestVersion;
    }

    public org.apache.ambari.server.state.RefreshCommandConfiguration getRefreshCommandConfiguration() {
        return refreshCommandConfiguration;
    }

    public void setRefreshCommandConfiguration(org.apache.ambari.server.state.RefreshCommandConfiguration refreshCommandConfiguration) {
        this.refreshCommandConfiguration = refreshCommandConfiguration;
    }

    public java.util.Set<java.lang.String> getServiceNames() {
        return getServices().stream().map(org.apache.ambari.server.state.ServiceInfo::getName).collect(java.util.stream.Collectors.toSet());
    }

    public org.apache.ambari.spi.stack.StackReleaseVersion getReleaseVersion() {
        if (org.apache.commons.lang3.StringUtils.isNotEmpty(releaseVersionClass)) {
            try {
                return getLibraryInstance(releaseVersionClass);
            } catch (java.lang.Exception e) {
                org.apache.ambari.server.state.StackInfo.LOG.error("Could not create stack release instance.  Using default. {}", e.getMessage());
                return new org.apache.ambari.server.state.repository.DefaultStackVersion();
            }
        } else {
            return new org.apache.ambari.server.state.repository.DefaultStackVersion();
        }
    }

    @javax.annotation.Nullable
    public java.net.URLClassLoader getLibraryClassLoader() {
        return libraryClassLoader;
    }

    public void setLibraryClassLoader(java.net.URLClassLoader libraryClassLoader) {
        this.libraryClassLoader = libraryClassLoader;
    }

    public <T> T getLibraryInstance(java.lang.String className) throws java.lang.Exception {
        return getLibraryInstance(null, className);
    }

    @java.lang.SuppressWarnings("unchecked")
    public <T> T getLibraryInstance(com.google.inject.Injector injector, java.lang.String className) throws java.lang.Exception {
        java.lang.Class<? extends T> clazz;
        if (null != libraryClassLoader) {
            clazz = ((java.lang.Class<? extends T>) (libraryClassLoader.loadClass(className)));
        } else {
            clazz = ((java.lang.Class<? extends T>) (java.lang.Class.forName(className)));
        }
        return null == injector ? clazz.newInstance() : injector.getInstance(clazz);
    }
}