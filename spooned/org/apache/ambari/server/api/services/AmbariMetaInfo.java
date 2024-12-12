package org.apache.ambari.server.api.services;
import javax.xml.bind.JAXBException;
import org.apache.commons.lang.StringUtils;
import static org.apache.ambari.server.controller.spi.Resource.InternalType.Component;
import static org.apache.ambari.server.controller.spi.Resource.InternalType.HostComponent;
import static org.apache.ambari.server.controller.utilities.PropertyHelper.AGGREGATE_FUNCTION_IDENTIFIERS;
@com.google.inject.Singleton
public class AmbariMetaInfo {
    public static final java.lang.String ANY_OS = "any";

    public static final java.lang.String SCHEMA_VERSION_2 = "2.0";

    public static final java.lang.String KERBEROS_DESCRIPTOR_FILE_NAME = "kerberos.json";

    public static final java.lang.String WIDGETS_DESCRIPTOR_FILE_NAME = "widgets.json";

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.api.services.AmbariMetaInfo.class);

    @com.google.inject.Inject
    private org.apache.ambari.server.state.stack.OsFamily osFamily;

    private java.util.List<java.lang.String> ALL_SUPPORTED_OS;

    private final org.apache.ambari.server.customactions.ActionDefinitionManager adManager = new org.apache.ambari.server.customactions.ActionDefinitionManager();

    private java.lang.String serverVersion = "undefined";

    private java.io.File stackRoot;

    private java.io.File commonServicesRoot;

    private java.io.File extensionsRoot;

    private java.io.File serverVersionFile;

    private java.io.File commonWidgetsDescriptorFile;

    private java.io.File customActionRoot;

    private java.lang.String commonKerberosDescriptorFileLocation;

    java.util.Map<java.lang.String, org.apache.ambari.server.state.repository.VersionDefinitionXml> versionDefinitions = null;

    private java.io.File mpacksV2Staging;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.AlertDefinitionDAO alertDefinitionDao;

    @com.google.inject.Inject
    private org.apache.ambari.server.state.alert.AlertDefinitionFactory alertDefinitionFactory;

    @com.google.inject.Inject
    private org.apache.ambari.server.metadata.AmbariServiceAlertDefinitions ambariServiceAlertDefinitions;

    @com.google.inject.Inject
    private org.apache.ambari.server.events.publishers.AmbariEventPublisher eventPublisher;

    @com.google.inject.Inject
    private org.apache.ambari.server.state.kerberos.KerberosDescriptorFactory kerberosDescriptorFactory;

    @com.google.inject.Inject
    private org.apache.ambari.server.state.kerberos.KerberosServiceDescriptorFactory kerberosServiceDescriptorFactory;

    @com.google.inject.Inject
    private org.apache.ambari.server.stack.StackManagerFactory stackManagerFactory;

    private org.apache.ambari.server.stack.StackManager stackManager;

    @com.google.inject.Inject
    private org.apache.ambari.server.mpack.MpackManagerFactory mpackManagerFactory;

    private org.apache.ambari.server.mpack.MpackManager mpackManager;

    private org.apache.ambari.server.configuration.Configuration conf;

    @com.google.inject.Inject
    public AmbariMetaInfo(org.apache.ambari.server.configuration.Configuration conf) throws java.lang.Exception {
        this.conf = conf;
        java.lang.String stackPath = conf.getMetadataPath();
        stackRoot = new java.io.File(stackPath);
        java.lang.String commonServicesPath = conf.getCommonServicesPath();
        if ((commonServicesPath != null) && (!commonServicesPath.isEmpty())) {
            commonServicesRoot = new java.io.File(commonServicesPath);
        }
        java.lang.String extensionsPath = conf.getExtensionsPath();
        if ((extensionsPath != null) && (!extensionsPath.isEmpty())) {
            extensionsRoot = new java.io.File(extensionsPath);
        }
        java.lang.String serverVersionFilePath = conf.getServerVersionFilePath();
        serverVersionFile = new java.io.File(serverVersionFilePath);
        customActionRoot = new java.io.File(conf.getCustomActionDefinitionPath());
        commonKerberosDescriptorFileLocation = new java.io.File(conf.getResourceDirPath(), org.apache.ambari.server.api.services.AmbariMetaInfo.KERBEROS_DESCRIPTOR_FILE_NAME).getAbsolutePath();
        commonWidgetsDescriptorFile = new java.io.File(conf.getResourceDirPath(), org.apache.ambari.server.api.services.AmbariMetaInfo.WIDGETS_DESCRIPTOR_FILE_NAME);
        java.lang.String mpackV2StagingPath = conf.getMpacksV2StagingPath();
        mpacksV2Staging = new java.io.File(mpackV2StagingPath);
    }

    @com.google.inject.Inject
    public void init() throws java.lang.Exception {
        ALL_SUPPORTED_OS = new java.util.ArrayList<>(osFamily.os_list());
        readServerVersion();
        stackManager = stackManagerFactory.create(stackRoot, commonServicesRoot, extensionsRoot, osFamily, false);
        mpackManager = mpackManagerFactory.create(mpacksV2Staging, stackRoot);
        getCustomActionDefinitions(customActionRoot);
    }

    public org.apache.ambari.server.stack.StackManager getStackManager() {
        return stackManager;
    }

    public org.apache.ambari.server.mpack.MpackManager getMpackManager() {
        return mpackManager;
    }

    public java.util.List<org.apache.ambari.server.state.ComponentInfo> getComponentsByService(java.lang.String stackName, java.lang.String version, java.lang.String serviceName) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.ServiceInfo service;
        try {
            service = getService(stackName, version, serviceName);
        } catch (org.apache.ambari.server.StackAccessException e) {
            throw new org.apache.ambari.server.ParentObjectNotFoundException((((("Parent Service resource doesn't exist. stackName=" + stackName) + ", stackVersion=") + version) + ", serviceName=") + serviceName);
        }
        return service.getComponents();
    }

    public org.apache.ambari.server.state.ComponentInfo getComponent(java.lang.String stackName, java.lang.String version, java.lang.String serviceName, java.lang.String componentName) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.ComponentInfo component = getService(stackName, version, serviceName).getComponentByName(componentName);
        if (component == null) {
            throw new org.apache.ambari.server.StackAccessException((((((("stackName=" + stackName) + ", stackVersion=") + version) + ", serviceName=") + serviceName) + ", componentName=") + componentName);
        }
        return component;
    }

    public java.util.List<org.apache.ambari.server.state.DependencyInfo> getComponentDependencies(java.lang.String stackName, java.lang.String version, java.lang.String service, java.lang.String component) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.ComponentInfo componentInfo;
        try {
            componentInfo = getComponent(stackName, version, service, component);
        } catch (org.apache.ambari.server.StackAccessException e) {
            throw new org.apache.ambari.server.ParentObjectNotFoundException("Parent Component resource doesn't exist", e);
        }
        return componentInfo.getDependencies();
    }

    public org.apache.ambari.server.state.DependencyInfo getComponentDependency(java.lang.String stackName, java.lang.String version, java.lang.String service, java.lang.String component, java.lang.String dependencyName) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.DependencyInfo foundDependency = null;
        java.util.List<org.apache.ambari.server.state.DependencyInfo> componentDependencies = getComponentDependencies(stackName, version, service, component);
        java.util.Iterator<org.apache.ambari.server.state.DependencyInfo> iter = componentDependencies.iterator();
        while ((foundDependency == null) && iter.hasNext()) {
            org.apache.ambari.server.state.DependencyInfo dependency = iter.next();
            if (dependencyName.equals(dependency.getComponentName())) {
                foundDependency = dependency;
            }
        } 
        if (foundDependency == null) {
            throw new org.apache.ambari.server.StackAccessException((((((((("stackName=" + stackName) + ", stackVersion= ") + version) + ", stackService=") + service) + ", stackComponent= ") + component) + ", dependency=") + dependencyName);
        }
        return foundDependency;
    }

    public java.util.Map<java.lang.String, java.util.List<org.apache.ambari.server.state.RepositoryInfo>> getRepository(java.lang.String stackName, java.lang.String version) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.StackInfo stack = getStack(stackName, version);
        java.util.List<org.apache.ambari.server.state.RepositoryInfo> repository = stack.getRepositories();
        java.util.Map<java.lang.String, java.util.List<org.apache.ambari.server.state.RepositoryInfo>> reposResult = new java.util.HashMap<>();
        for (org.apache.ambari.server.state.RepositoryInfo repo : repository) {
            if (!reposResult.containsKey(repo.getOsType())) {
                reposResult.put(repo.getOsType(), new java.util.ArrayList<>());
            }
            reposResult.get(repo.getOsType()).add(repo);
        }
        return reposResult;
    }

    public java.util.List<org.apache.ambari.server.state.RepositoryInfo> getRepositories(java.lang.String stackName, java.lang.String version, java.lang.String osType) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.StackInfo stack = getStack(stackName, version);
        java.util.List<org.apache.ambari.server.state.RepositoryInfo> repositories = stack.getRepositories();
        java.util.List<org.apache.ambari.server.state.RepositoryInfo> repositoriesResult = new java.util.ArrayList<>();
        for (org.apache.ambari.server.state.RepositoryInfo repository : repositories) {
            if (repository.getOsType().equals(osType)) {
                repositoriesResult.add(repository);
            }
        }
        return repositoriesResult;
    }

    public org.apache.ambari.server.state.RepositoryInfo getRepository(java.lang.String stackName, java.lang.String version, java.lang.String osType, java.lang.String repoId) throws org.apache.ambari.server.AmbariException {
        java.util.List<org.apache.ambari.server.state.RepositoryInfo> repositories = getRepositories(stackName, version, osType);
        if (repositories.size() == 0) {
            throw new org.apache.ambari.server.StackAccessException((((((("stackName=" + stackName) + ", stackVersion=") + version) + ", osType=") + osType) + ", repoId=") + repoId);
        }
        org.apache.ambari.server.state.RepositoryInfo repoResult = null;
        for (org.apache.ambari.server.state.RepositoryInfo repository : repositories) {
            if (repository.getRepoId().equals(repoId)) {
                repoResult = repository;
            }
        }
        if (repoResult == null) {
            throw new org.apache.ambari.server.StackAccessException((((((("stackName=" + stackName) + ", stackVersion= ") + version) + ", osType=") + osType) + ", repoId= ") + repoId);
        }
        return repoResult;
    }

    public boolean isSupportedStack(java.lang.String stackName, java.lang.String version) {
        try {
            getStack(stackName, version);
            return true;
        } catch (org.apache.ambari.server.AmbariException e) {
            return false;
        }
    }

    public boolean isValidService(java.lang.String stackName, java.lang.String version, java.lang.String serviceName) {
        try {
            getService(stackName, version, serviceName);
            return true;
        } catch (org.apache.ambari.server.AmbariException e) {
            return false;
        }
    }

    public boolean isValidServiceComponent(java.lang.String stackName, java.lang.String version, java.lang.String serviceName, java.lang.String componentName) {
        try {
            getService(stackName, version, serviceName).getComponentByName(componentName);
            return true;
        } catch (org.apache.ambari.server.AmbariException e) {
            return false;
        }
    }

    public java.lang.String getComponentToService(java.lang.String stackName, java.lang.String version, java.lang.String componentName) throws org.apache.ambari.server.AmbariException {
        if (org.apache.ambari.server.api.services.AmbariMetaInfo.LOG.isDebugEnabled()) {
            org.apache.ambari.server.api.services.AmbariMetaInfo.LOG.debug("Looking for service for component, stackName={}, stackVersion={}, componentName={}", stackName, version, componentName);
        }
        java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceInfo> services = getServices(stackName, version);
        java.lang.String retService = null;
        if ((services == null) || services.isEmpty()) {
            return retService;
        }
        for (java.util.Map.Entry<java.lang.String, org.apache.ambari.server.state.ServiceInfo> entry : services.entrySet()) {
            org.apache.ambari.server.state.ComponentInfo vu = entry.getValue().getComponentByName(componentName);
            if (vu != null) {
                retService = entry.getKey();
                break;
            }
        }
        return retService;
    }

    public java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceInfo> getServices(java.lang.String stackName, java.lang.String version) throws org.apache.ambari.server.AmbariException {
        java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceInfo> servicesInfoResult = new java.util.HashMap<>();
        java.util.Collection<org.apache.ambari.server.state.ServiceInfo> services;
        org.apache.ambari.server.state.StackInfo stack;
        try {
            stack = getStack(stackName, version);
        } catch (org.apache.ambari.server.StackAccessException e) {
            throw new org.apache.ambari.server.ParentObjectNotFoundException("Parent Stack Version resource doesn't exist", e);
        }
        services = stack.getServices();
        if (services != null) {
            for (org.apache.ambari.server.state.ServiceInfo service : services) {
                servicesInfoResult.put(service.getName(), service);
            }
        }
        return servicesInfoResult;
    }

    public org.apache.ambari.server.state.ServiceInfo getService(org.apache.ambari.server.state.Service service) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.StackId stackId = service.getDesiredStackId();
        return getService(stackId.getStackName(), stackId.getStackVersion(), service.getName());
    }

    public org.apache.ambari.server.state.ServiceInfo getService(java.lang.String stackName, java.lang.String version, java.lang.String serviceName) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.ServiceInfo service = getStack(stackName, version).getService(serviceName);
        if (service == null) {
            throw new org.apache.ambari.server.StackAccessException((((("stackName=" + stackName) + ", stackVersion=") + version) + ", serviceName=") + serviceName);
        }
        return service;
    }

    public boolean isServiceRemovedInStack(java.lang.String stackName, java.lang.String version, java.lang.String serviceName) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.StackInfo stack = getStack(stackName, version);
        java.util.List<java.lang.String> removedServices = stack.getRemovedServices();
        return removedServices.contains(serviceName);
    }

    public boolean isServiceWithNoConfigs(java.lang.String stackName, java.lang.String version, java.lang.String serviceName) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.StackInfo stack = getStack(stackName, version);
        java.util.List<java.lang.String> servicesWithNoConfigs = stack.getServicesWithNoConfigs();
        return servicesWithNoConfigs.contains(serviceName);
    }

    public java.util.Collection<java.lang.String> getMonitoringServiceNames(java.lang.String stackName, java.lang.String version) throws org.apache.ambari.server.AmbariException {
        java.util.List<java.lang.String> monitoringServices = new java.util.ArrayList<>();
        for (org.apache.ambari.server.state.ServiceInfo service : getServices(stackName, version).values()) {
            if ((service.isMonitoringService() != null) && service.isMonitoringService()) {
                monitoringServices.add(service.getName());
            }
        }
        return monitoringServices;
    }

    public java.util.Set<java.lang.String> getRestartRequiredServicesNames(java.lang.String stackName, java.lang.String version) throws org.apache.ambari.server.AmbariException {
        java.util.HashSet<java.lang.String> needRestartServices = new java.util.HashSet<>();
        java.util.Collection<org.apache.ambari.server.state.ServiceInfo> serviceInfos = getServices(stackName, version).values();
        for (org.apache.ambari.server.state.ServiceInfo service : serviceInfos) {
            java.lang.Boolean restartRequiredAfterChange = service.isRestartRequiredAfterChange();
            if ((restartRequiredAfterChange != null) && restartRequiredAfterChange) {
                needRestartServices.add(service.getName());
            }
        }
        return needRestartServices;
    }

    public java.util.Set<java.lang.String> getRackSensitiveServicesNames(java.lang.String stackName, java.lang.String version) throws org.apache.ambari.server.AmbariException {
        java.util.HashSet<java.lang.String> needRestartServices = new java.util.HashSet<>();
        java.util.Collection<org.apache.ambari.server.state.ServiceInfo> serviceInfos = getServices(stackName, version).values();
        for (org.apache.ambari.server.state.ServiceInfo service : serviceInfos) {
            java.lang.Boolean restartRequiredAfterRackChange = service.isRestartRequiredAfterRackChange();
            if ((restartRequiredAfterRackChange != null) && restartRequiredAfterRackChange) {
                needRestartServices.add(service.getName());
            }
        }
        return needRestartServices;
    }

    public java.util.Collection<org.apache.ambari.server.state.StackInfo> getStacks() {
        return stackManager.getStacks();
    }

    public org.apache.ambari.server.controller.MpackResponse registerMpack(org.apache.ambari.server.controller.MpackRequest mpackRequest) throws java.io.IOException, org.apache.ambari.server.controller.spi.ResourceAlreadyExistsException {
        if (versionDefinitions != null) {
            versionDefinitions.clear();
        }
        return mpackManager.registerMpack(mpackRequest);
    }

    public java.util.List<org.apache.ambari.server.state.Module> getModules(java.lang.Long mpackId) {
        return mpackManager.getModules(mpackId);
    }

    public java.util.Collection<org.apache.ambari.server.state.StackInfo> getStacks(java.lang.String stackName) throws org.apache.ambari.server.AmbariException {
        java.util.Collection<org.apache.ambari.server.state.StackInfo> stacks = stackManager.getStacks(stackName);
        if (stacks.isEmpty()) {
            throw new org.apache.ambari.server.StackAccessException("stackName=" + stackName);
        }
        return stacks;
    }

    public org.apache.ambari.server.state.StackInfo getStack(org.apache.ambari.server.state.StackId stackId) throws org.apache.ambari.server.AmbariException {
        return getStack(stackId.getStackName(), stackId.getStackVersion());
    }

    public org.apache.ambari.server.state.StackInfo getStack(java.lang.String stackName, java.lang.String version) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.StackInfo stackInfoResult = stackManager.getStack(stackName, version);
        if (stackInfoResult == null) {
            throw new org.apache.ambari.server.StackAccessException(((("Stack " + stackName) + " ") + version) + " is not found in Ambari metainfo");
        }
        return stackInfoResult;
    }

    public java.util.List<java.lang.String> getStackParentVersions(java.lang.String stackName, java.lang.String version) {
        java.util.List<java.lang.String> parents = new java.util.ArrayList<>();
        try {
            org.apache.ambari.server.state.StackInfo stackInfo = getStack(stackName, version);
            java.lang.String parentVersion = stackInfo.getParentStackVersion();
            if (parentVersion != null) {
                parents.add(parentVersion);
                parents.addAll(getStackParentVersions(stackName, parentVersion));
            }
        } catch (org.apache.ambari.server.AmbariException e) {
        }
        return parents;
    }

    public java.util.Collection<org.apache.ambari.server.state.ExtensionInfo> getExtensions() {
        return stackManager.getExtensions();
    }

    public java.util.Collection<org.apache.ambari.server.state.ExtensionInfo> getExtensions(java.lang.String extensionName) throws org.apache.ambari.server.AmbariException {
        java.util.Collection<org.apache.ambari.server.state.ExtensionInfo> extensions = stackManager.getExtensions(extensionName);
        if (extensions.isEmpty()) {
            throw new org.apache.ambari.server.StackAccessException("extensionName=" + extensionName);
        }
        return extensions;
    }

    public org.apache.ambari.server.state.ExtensionInfo getExtension(java.lang.String extensionName, java.lang.String version) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.ExtensionInfo result = stackManager.getExtension(extensionName, version);
        if (result == null) {
            throw new org.apache.ambari.server.StackAccessException(((("Extension " + extensionName) + " ") + version) + " is not found in Ambari metainfo");
        }
        return result;
    }

    public java.util.Set<org.apache.ambari.server.state.PropertyInfo> getServiceProperties(java.lang.String stackName, java.lang.String version, java.lang.String serviceName) throws org.apache.ambari.server.AmbariException {
        return new java.util.HashSet<>(getService(stackName, version, serviceName).getProperties());
    }

    public java.util.Set<org.apache.ambari.server.state.PropertyInfo> getStackProperties(java.lang.String stackName, java.lang.String version) throws org.apache.ambari.server.AmbariException {
        return new java.util.HashSet<>(getStack(stackName, version).getProperties());
    }

    public java.util.Set<org.apache.ambari.server.state.PropertyInfo> getPropertiesByName(java.lang.String stackName, java.lang.String version, java.lang.String serviceName, java.lang.String propertyName) throws org.apache.ambari.server.AmbariException {
        java.util.Set<org.apache.ambari.server.state.PropertyInfo> properties = (serviceName == null) ? getStackProperties(stackName, version) : getServiceProperties(stackName, version, serviceName);
        if (properties.size() == 0) {
            throw new org.apache.ambari.server.StackAccessException((((((("stackName=" + stackName) + ", stackVersion=") + version) + ", serviceName=") + serviceName) + ", propertyName=") + propertyName);
        }
        java.util.Set<org.apache.ambari.server.state.PropertyInfo> propertyResult = new java.util.HashSet<>();
        for (org.apache.ambari.server.state.PropertyInfo property : properties) {
            if (property.getName().equals(propertyName)) {
                propertyResult.add(property);
            }
        }
        if (propertyResult.isEmpty()) {
            throw new org.apache.ambari.server.StackAccessException((((((("stackName=" + stackName) + ", stackVersion=") + version) + ", serviceName=") + serviceName) + ", propertyName=") + propertyName);
        }
        return propertyResult;
    }

    public java.util.Set<org.apache.ambari.server.state.PropertyInfo> getStackPropertiesByName(java.lang.String stackName, java.lang.String version, java.lang.String propertyName) throws org.apache.ambari.server.AmbariException {
        java.util.Set<org.apache.ambari.server.state.PropertyInfo> properties = getStackProperties(stackName, version);
        if (properties.size() == 0) {
            throw new org.apache.ambari.server.StackAccessException((((("stackName=" + stackName) + ", stackVersion=") + version) + ", propertyName=") + propertyName);
        }
        java.util.Set<org.apache.ambari.server.state.PropertyInfo> propertyResult = new java.util.HashSet<>();
        for (org.apache.ambari.server.state.PropertyInfo property : properties) {
            if (property.getName().equals(propertyName)) {
                propertyResult.add(property);
            }
        }
        if (propertyResult.isEmpty()) {
            throw new org.apache.ambari.server.StackAccessException((((("stackName=" + stackName) + ", stackVersion=") + version) + ", propertyName=") + propertyName);
        }
        return propertyResult;
    }

    public java.util.Set<org.apache.ambari.server.state.OperatingSystemInfo> getOperatingSystems(java.lang.String stackName, java.lang.String version) throws org.apache.ambari.server.AmbariException {
        java.util.Set<org.apache.ambari.server.state.OperatingSystemInfo> operatingSystems = new java.util.HashSet<>();
        org.apache.ambari.server.state.StackInfo stack = getStack(stackName, version);
        java.util.List<org.apache.ambari.server.state.RepositoryInfo> repositories = stack.getRepositories();
        for (org.apache.ambari.server.state.RepositoryInfo repository : repositories) {
            operatingSystems.add(new org.apache.ambari.server.state.OperatingSystemInfo(repository.getOsType()));
        }
        return operatingSystems;
    }

    public org.apache.ambari.server.state.OperatingSystemInfo getOperatingSystem(java.lang.String stackName, java.lang.String version, java.lang.String osType) throws org.apache.ambari.server.AmbariException {
        java.util.Set<org.apache.ambari.server.state.OperatingSystemInfo> operatingSystems = getOperatingSystems(stackName, version);
        if (operatingSystems.size() == 0) {
            throw new org.apache.ambari.server.StackAccessException((((("stackName=" + stackName) + ", stackVersion=") + version) + ", osType=") + osType);
        }
        org.apache.ambari.server.state.OperatingSystemInfo resultOperatingSystem = null;
        for (org.apache.ambari.server.state.OperatingSystemInfo operatingSystem : operatingSystems) {
            if (operatingSystem.getOsType().equals(osType)) {
                resultOperatingSystem = operatingSystem;
                break;
            }
        }
        if (resultOperatingSystem == null) {
            throw new org.apache.ambari.server.StackAccessException((((("stackName=" + stackName) + ", stackVersion=") + version) + ", osType=") + osType);
        }
        return resultOperatingSystem;
    }

    private void readServerVersion() throws java.lang.Exception {
        java.io.File versionFile = serverVersionFile;
        if (!versionFile.exists()) {
            throw new org.apache.ambari.server.AmbariException("Server version file does not exist.");
        }
        java.util.Scanner scanner = new java.util.Scanner(versionFile);
        serverVersion = scanner.useDelimiter("\\Z").next();
        scanner.close();
    }

    private void getCustomActionDefinitions(java.io.File customActionDefinitionRoot) throws javax.xml.bind.JAXBException, org.apache.ambari.server.AmbariException {
        if (customActionDefinitionRoot != null) {
            org.apache.ambari.server.api.services.AmbariMetaInfo.LOG.debug("Loading custom action definitions from {}", customActionDefinitionRoot.getAbsolutePath());
            if (customActionDefinitionRoot.exists() && customActionDefinitionRoot.isDirectory()) {
                adManager.readCustomActionDefinitions(customActionDefinitionRoot);
            } else {
                org.apache.ambari.server.api.services.AmbariMetaInfo.LOG.debug("No action definitions found at {}", customActionDefinitionRoot.getAbsolutePath());
            }
        }
    }

    public java.util.List<org.apache.ambari.server.customactions.ActionDefinition> getAllActionDefinition() {
        return adManager.getAllActionDefinition();
    }

    public org.apache.ambari.server.customactions.ActionDefinition getActionDefinition(java.lang.String name) {
        return adManager.getActionDefinition(name);
    }

    public void addActionDefinition(org.apache.ambari.server.customactions.ActionDefinition ad) throws org.apache.ambari.server.AmbariException {
        adManager.addActionDefinition(ad);
    }

    public java.lang.String getServerVersion() {
        return serverVersion;
    }

    public boolean isOsSupported(java.lang.String osType) {
        return ALL_SUPPORTED_OS.contains(osType);
    }

    public java.io.File getStackRoot() {
        return stackRoot;
    }

    public java.io.File getExtensionsRoot() {
        return extensionsRoot;
    }

    public java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.List<org.apache.ambari.server.state.stack.MetricDefinition>>> getServiceMetrics(java.lang.String stackName, java.lang.String stackVersion, java.lang.String serviceName) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.ServiceInfo svc = getService(stackName, stackVersion, serviceName);
        if ((null == svc.getMetricsFile()) || (!svc.getMetricsFile().exists())) {
            org.apache.ambari.server.api.services.AmbariMetaInfo.LOG.debug("Metrics file for {}/{}/{} not found.", stackName, stackVersion, serviceName);
            return null;
        }
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.List<org.apache.ambari.server.state.stack.MetricDefinition>>> map = svc.getMetrics();
        if (null == map) {
            java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.List<org.apache.ambari.server.state.stack.MetricDefinition>>>>() {}.getType();
            com.google.gson.Gson gson = new com.google.gson.Gson();
            try {
                map = gson.fromJson(new java.io.FileReader(svc.getMetricsFile()), type);
                svc.setMetrics(processMetricDefinition(map));
            } catch (java.lang.Exception e) {
                org.apache.ambari.server.api.services.AmbariMetaInfo.LOG.error("Could not read the metrics file", e);
                throw new org.apache.ambari.server.AmbariException("Could not read metrics file", e);
            }
        }
        return map;
    }

    private java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.List<org.apache.ambari.server.state.stack.MetricDefinition>>> processMetricDefinition(java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.List<org.apache.ambari.server.state.stack.MetricDefinition>>> metricMap) {
        if (!metricMap.isEmpty()) {
            for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, java.util.List<org.apache.ambari.server.state.stack.MetricDefinition>>> componentMetricDefEntry : metricMap.entrySet()) {
                java.lang.String componentName = componentMetricDefEntry.getKey();
                for (java.util.Map.Entry<java.lang.String, java.util.List<org.apache.ambari.server.state.stack.MetricDefinition>> metricDefEntry : componentMetricDefEntry.getValue().entrySet()) {
                    for (org.apache.ambari.server.state.stack.MetricDefinition metricDefinition : metricDefEntry.getValue()) {
                        for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.state.stack.Metric>> metricByCategory : metricDefinition.getMetricsByCategory().entrySet()) {
                            java.util.Iterator<java.util.Map.Entry<java.lang.String, org.apache.ambari.server.state.stack.Metric>> iterator = metricByCategory.getValue().entrySet().iterator();
                            java.util.Map<java.lang.String, org.apache.ambari.server.state.stack.Metric> newMetricsToAdd = new java.util.HashMap<>();
                            while (iterator.hasNext()) {
                                java.util.Map.Entry<java.lang.String, org.apache.ambari.server.state.stack.Metric> metricEntry = iterator.next();
                                java.util.Map<java.lang.String, org.apache.ambari.server.state.stack.Metric> processedMetrics = org.apache.ambari.server.controller.utilities.PropertyHelper.processRpcMetricDefinition(metricDefinition.getType(), componentName, metricEntry.getKey(), metricEntry.getValue());
                                if (processedMetrics != null) {
                                    iterator.remove();
                                    newMetricsToAdd.putAll(processedMetrics);
                                } else {
                                    processedMetrics = java.util.Collections.singletonMap(metricEntry.getKey(), metricEntry.getValue());
                                }
                                if (metricDefinition.getType().equals("ganglia") && (metricDefEntry.getKey().equals(org.apache.ambari.server.controller.spi.Resource.InternalType.Component.name()) || metricDefEntry.getKey().equals(org.apache.ambari.server.controller.spi.Resource.InternalType.HostComponent.name()))) {
                                    for (java.util.Map.Entry<java.lang.String, org.apache.ambari.server.state.stack.Metric> processedMetric : processedMetrics.entrySet()) {
                                        newMetricsToAdd.putAll(getAggregateFunctionMetrics(processedMetric.getKey(), processedMetric.getValue()));
                                    }
                                }
                            } 
                            metricByCategory.getValue().putAll(newMetricsToAdd);
                        }
                    }
                }
            }
        }
        return metricMap;
    }

    private java.util.Map<java.lang.String, org.apache.ambari.server.state.stack.Metric> getAggregateFunctionMetrics(java.lang.String metricName, org.apache.ambari.server.state.stack.Metric currentMetric) {
        java.util.Map<java.lang.String, org.apache.ambari.server.state.stack.Metric> newMetrics = new java.util.HashMap<>();
        if (!org.apache.ambari.server.controller.utilities.PropertyHelper.hasAggregateFunctionSuffix(currentMetric.getName())) {
            for (java.lang.String identifierToAdd : org.apache.ambari.server.controller.utilities.PropertyHelper.AGGREGATE_FUNCTION_IDENTIFIERS) {
                java.lang.String newMetricKey = metricName + identifierToAdd;
                org.apache.ambari.server.state.stack.Metric newMetric = new org.apache.ambari.server.state.stack.Metric(currentMetric.getName() + identifierToAdd, currentMetric.isPointInTime(), currentMetric.isTemporal(), currentMetric.isAmsHostMetric(), currentMetric.getUnit());
                newMetrics.put(newMetricKey, newMetric);
            }
        }
        return newMetrics;
    }

    public java.util.List<org.apache.ambari.server.state.stack.MetricDefinition> getMetrics(java.lang.String stackName, java.lang.String stackVersion, java.lang.String serviceName, java.lang.String componentName, java.lang.String metricType) throws org.apache.ambari.server.AmbariException {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.List<org.apache.ambari.server.state.stack.MetricDefinition>>> map = getServiceMetrics(stackName, stackVersion, serviceName);
        if ((map != null) && map.containsKey(componentName)) {
            if (map.get(componentName).containsKey(metricType)) {
                return map.get(componentName).get(metricType);
            }
        }
        return null;
    }

    public java.util.Set<org.apache.ambari.server.state.alert.AlertDefinition> getAlertDefinitions(java.lang.String stackName, java.lang.String stackVersion, java.lang.String serviceName) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.ServiceInfo svc = getService(stackName, stackVersion, serviceName);
        return getAlertDefinitions(svc);
    }

    public java.util.Set<org.apache.ambari.server.state.alert.AlertDefinition> getAlertDefinitions(org.apache.ambari.server.state.ServiceInfo service) throws org.apache.ambari.server.AmbariException {
        java.io.File alertsFile = service.getAlertsFile();
        if ((null == alertsFile) || (!alertsFile.exists())) {
            org.apache.ambari.server.api.services.AmbariMetaInfo.LOG.debug("Alerts file for {}/{} not found.", service.getSchemaVersion(), service.getName());
            return java.util.Collections.emptySet();
        }
        return alertDefinitionFactory.getAlertDefinitions(alertsFile, service.getName());
    }

    private java.util.List<org.apache.ambari.server.orm.entities.AlertDefinitionEntity> getDefinitionsForMerge(java.util.List<org.apache.ambari.server.state.alert.AlertDefinition> definitions, long clusterId, java.util.Map<java.lang.String, org.apache.ambari.server.orm.entities.AlertDefinitionEntity> mappedEntities) {
        java.util.List<org.apache.ambari.server.orm.entities.AlertDefinitionEntity> definitionsForMerge = new java.util.ArrayList<>();
        for (org.apache.ambari.server.state.alert.AlertDefinition definition : definitions) {
            org.apache.ambari.server.orm.entities.AlertDefinitionEntity entity = mappedEntities.get(definition.getName());
            if (null == entity) {
                entity = alertDefinitionFactory.coerce(clusterId, definition);
                definitionsForMerge.add(entity);
            }
        }
        return definitionsForMerge;
    }

    public void reconcileAlertDefinitions(org.apache.ambari.server.state.Clusters clusters, boolean updateScriptPaths) throws org.apache.ambari.server.AmbariException {
        java.util.Map<java.lang.String, org.apache.ambari.server.state.Cluster> clusterMap = clusters.getClusters();
        if ((null == clusterMap) || (clusterMap.size() == 0)) {
            return;
        }
        for (org.apache.ambari.server.state.Cluster cluster : clusterMap.values()) {
            reconcileAlertDefinitions(cluster, updateScriptPaths);
        }
    }

    public void reconcileAlertDefinitions(org.apache.ambari.server.state.Cluster cluster, boolean updateScriptPaths) throws org.apache.ambari.server.AmbariException {
        if (null == cluster) {
            return;
        }
        long clusterId = cluster.getClusterId();
        java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceInfo> stackServiceMap = new java.util.HashMap<>();
        java.util.Map<java.lang.String, org.apache.ambari.server.state.ComponentInfo> stackComponentMap = new java.util.HashMap<>();
        java.util.List<org.apache.ambari.server.state.alert.AlertDefinition> stackDefinitions = new java.util.ArrayList<>(50);
        for (org.apache.ambari.server.state.Service service : cluster.getServices().values()) {
            org.apache.ambari.server.state.ServiceInfo stackService = getService(service.getDesiredStackId().getStackName(), service.getDesiredStackId().getStackVersion(), service.getName());
            if (null == stackService) {
                continue;
            }
            stackServiceMap.put(stackService.getName(), stackService);
            java.util.List<org.apache.ambari.server.state.ComponentInfo> components = stackService.getComponents();
            for (org.apache.ambari.server.state.ComponentInfo component : components) {
                stackComponentMap.put(component.getName(), component);
            }
            java.util.Set<org.apache.ambari.server.state.alert.AlertDefinition> serviceDefinitions = getAlertDefinitions(stackService);
            stackDefinitions.addAll(serviceDefinitions);
        }
        java.util.List<org.apache.ambari.server.orm.entities.AlertDefinitionEntity> persist = new java.util.ArrayList<>();
        java.util.List<org.apache.ambari.server.orm.entities.AlertDefinitionEntity> entities = alertDefinitionDao.findAll(clusterId);
        java.util.Map<java.lang.String, org.apache.ambari.server.orm.entities.AlertDefinitionEntity> mappedEntities = new java.util.HashMap<>(100);
        for (org.apache.ambari.server.orm.entities.AlertDefinitionEntity entity : entities) {
            mappedEntities.put(entity.getDefinitionName(), entity);
        }
        for (org.apache.ambari.server.state.alert.AlertDefinition stackDefinition : stackDefinitions) {
            org.apache.ambari.server.orm.entities.AlertDefinitionEntity entity = mappedEntities.get(stackDefinition.getName());
            if (null == entity) {
                entity = alertDefinitionFactory.coerce(clusterId, stackDefinition);
                persist.add(entity);
                continue;
            }
            org.apache.ambari.server.state.alert.AlertDefinition databaseDefinition = alertDefinitionFactory.coerce(entity);
            if (!stackDefinition.deeplyEquals(databaseDefinition)) {
                if (updateScriptPaths) {
                    org.apache.ambari.server.state.alert.Source databaseSource = databaseDefinition.getSource();
                    org.apache.ambari.server.state.alert.Source stackSource = stackDefinition.getSource();
                    if ((databaseSource.getType() == org.apache.ambari.server.state.alert.SourceType.SCRIPT) && (stackSource.getType() == org.apache.ambari.server.state.alert.SourceType.SCRIPT)) {
                        org.apache.ambari.server.state.alert.ScriptSource databaseScript = ((org.apache.ambari.server.state.alert.ScriptSource) (databaseSource));
                        org.apache.ambari.server.state.alert.ScriptSource stackScript = ((org.apache.ambari.server.state.alert.ScriptSource) (stackSource));
                        java.lang.String oldPath = databaseScript.getPath();
                        java.lang.String newPath = stackScript.getPath();
                        if (!java.util.Objects.equals(oldPath, newPath)) {
                            databaseScript.setPath(newPath);
                            entity = alertDefinitionFactory.mergeSource(databaseScript, entity);
                            persist.add(entity);
                            org.apache.ambari.server.api.services.AmbariMetaInfo.LOG.info("Updating script path for the alert named {} from '{}' to '{}'", stackDefinition.getName(), oldPath, newPath);
                        }
                    }
                } else {
                    org.apache.ambari.server.api.services.AmbariMetaInfo.LOG.debug("The alert named {} has been modified from the stack definition and will not be merged", stackDefinition.getName());
                }
            }
        }
        persist.addAll(getDefinitionsForMerge(ambariServiceAlertDefinitions.getAgentDefinitions(), clusterId, mappedEntities));
        persist.addAll(getDefinitionsForMerge(ambariServiceAlertDefinitions.getServerDefinitions(), clusterId, mappedEntities));
        for (org.apache.ambari.server.orm.entities.AlertDefinitionEntity entity : persist) {
            org.apache.ambari.server.api.services.AmbariMetaInfo.LOG.debug("Merging Alert Definition {} into the database", entity.getDefinitionName());
            alertDefinitionDao.createOrUpdate(entity);
        }
        for (org.apache.ambari.server.orm.entities.AlertDefinitionEntity def : alertDefinitionDao.findAll(cluster.getClusterId())) {
            org.apache.ambari.server.state.alert.AlertDefinition realDef = alertDefinitionFactory.coerce(def);
            org.apache.ambari.server.events.AlertDefinitionRegistrationEvent event = new org.apache.ambari.server.events.AlertDefinitionRegistrationEvent(cluster.getClusterId(), realDef);
            eventPublisher.publish(event);
        }
        java.util.List<org.apache.ambari.server.orm.entities.AlertDefinitionEntity> definitions = alertDefinitionDao.findAllEnabled(clusterId);
        java.util.List<org.apache.ambari.server.orm.entities.AlertDefinitionEntity> definitionsToDisable = new java.util.ArrayList<>();
        for (org.apache.ambari.server.orm.entities.AlertDefinitionEntity definition : definitions) {
            java.lang.String serviceName = definition.getServiceName();
            java.lang.String componentName = definition.getComponentName();
            if (org.apache.ambari.server.controller.RootService.AMBARI.name().equals(serviceName)) {
                continue;
            }
            if (!stackServiceMap.containsKey(serviceName)) {
                org.apache.ambari.server.api.services.AmbariMetaInfo.LOG.info("The {} service has been marked as deleted for cluster {}, disabling alert {}", serviceName, cluster.getClusterName(), definition.getDefinitionName());
                definitionsToDisable.add(definition);
            } else if ((null != componentName) && (!stackComponentMap.containsKey(componentName))) {
                org.apache.ambari.server.state.StackId stackId = cluster.getService(serviceName).getDesiredStackId();
                org.apache.ambari.server.api.services.AmbariMetaInfo.LOG.info("The {} component {} has been marked as deleted for stack {}, disabling alert {}", serviceName, componentName, stackId, definition.getDefinitionName());
                definitionsToDisable.add(definition);
            }
        }
        for (org.apache.ambari.server.orm.entities.AlertDefinitionEntity definition : definitionsToDisable) {
            definition.setEnabled(false);
            alertDefinitionDao.merge(definition);
            eventPublisher.publish(new org.apache.ambari.server.events.AlertDefinitionDisabledEvent(clusterId, definition.getDefinitionId(), definition.getDefinitionName()));
        }
    }

    public java.util.Map<java.lang.String, org.apache.ambari.server.stack.upgrade.UpgradePack> getUpgradePacks(java.lang.String stackName, java.lang.String stackVersion) {
        try {
            org.apache.ambari.server.state.StackInfo stack = getStack(stackName, stackVersion);
            return stack.getUpgradePacks() == null ? java.util.Collections.emptyMap() : stack.getUpgradePacks();
        } catch (org.apache.ambari.server.AmbariException e) {
            org.apache.ambari.server.api.services.AmbariMetaInfo.LOG.debug("Cannot load upgrade packs for non-existent stack {}-{}", stackName, stackVersion, e);
        }
        return java.util.Collections.emptyMap();
    }

    public org.apache.ambari.server.stack.upgrade.ConfigUpgradePack getConfigUpgradePack(java.lang.String stackName, java.lang.String stackVersion) {
        try {
            org.apache.ambari.server.state.StackInfo stack = getStack(stackName, stackVersion);
            return stack.getConfigUpgradePack();
        } catch (org.apache.ambari.server.AmbariException e) {
            org.apache.ambari.server.api.services.AmbariMetaInfo.LOG.debug("Cannot load config upgrade pack for non-existent stack {}-{}", stackName, stackVersion, e);
            return null;
        }
    }

    public org.apache.ambari.server.state.kerberos.KerberosDescriptor getKerberosDescriptor(java.lang.String stackName, java.lang.String stackVersion, boolean includePreconfigureData) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.StackInfo stackInfo = getStack(stackName, stackVersion);
        org.apache.ambari.server.state.kerberos.KerberosDescriptor kerberosDescriptor = readKerberosDescriptorFromFile(getCommonKerberosDescriptorFileLocation());
        if (kerberosDescriptor == null) {
            org.apache.ambari.server.api.services.AmbariMetaInfo.LOG.warn("Couldn't read common Kerberos descriptor with path {%s}", getCommonKerberosDescriptorFileLocation());
            kerberosDescriptor = new org.apache.ambari.server.state.kerberos.KerberosDescriptor();
        }
        if (includePreconfigureData) {
            org.apache.ambari.server.state.kerberos.KerberosDescriptor preConfigureKerberosDescriptor = readKerberosDescriptorFromFile(stackInfo.getKerberosDescriptorPreConfigurationFileLocation());
            if (preConfigureKerberosDescriptor != null) {
                java.util.Map<java.lang.String, org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor> serviceDescriptors = preConfigureKerberosDescriptor.getServices();
                if (serviceDescriptors != null) {
                    for (org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor serviceDescriptor : serviceDescriptors.values()) {
                        serviceDescriptor.setPreconfigure(true);
                    }
                }
                kerberosDescriptor.update(preConfigureKerberosDescriptor);
            }
        }
        java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceInfo> services = getServices(stackName, stackVersion);
        if (services != null) {
            for (org.apache.ambari.server.state.ServiceInfo service : services.values()) {
                org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor[] serviceDescriptors = getKerberosDescriptor(service);
                if (serviceDescriptors != null) {
                    for (org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor serviceDescriptor : serviceDescriptors) {
                        kerberosDescriptor.putService(serviceDescriptor);
                    }
                }
            }
        }
        return kerberosDescriptor;
    }

    protected java.lang.String getCommonKerberosDescriptorFileLocation() {
        return commonKerberosDescriptorFileLocation;
    }

    public org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor[] getKerberosDescriptor(org.apache.ambari.server.state.ServiceInfo serviceInfo) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor[] kerberosServiceDescriptors = null;
        java.io.File kerberosFile = (serviceInfo == null) ? null : serviceInfo.getKerberosDescriptorFile();
        if (kerberosFile != null) {
            try {
                kerberosServiceDescriptors = kerberosServiceDescriptorFactory.createInstances(kerberosFile);
            } catch (java.lang.Exception e) {
                org.apache.ambari.server.api.services.AmbariMetaInfo.LOG.error("Could not read the kerberos descriptor file", e);
                throw new org.apache.ambari.server.AmbariException("Could not read kerberos descriptor file", e);
            }
        }
        return kerberosServiceDescriptors;
    }

    public java.util.Map<java.lang.String, java.lang.String> getAmbariServerProperties() {
        return conf.getAmbariProperties();
    }

    private synchronized void ensureVersionDefinitions() {
        if (null != versionDefinitions) {
            if (versionDefinitions.size() > 0)
                return;

        }
        versionDefinitions = new java.util.HashMap<>();
        for (org.apache.ambari.server.state.StackInfo stack : getStacks()) {
            if (stack.isActive() && stack.isValid()) {
                for (org.apache.ambari.server.state.repository.VersionDefinitionXml definition : stack.getVersionDefinitions()) {
                    versionDefinitions.put(java.lang.String.format("%s-%s-%s", stack.getName(), stack.getVersion(), definition.release.version), definition);
                }
                try {
                    org.apache.ambari.server.state.repository.VersionDefinitionXml xml = stack.getLatestVersionDefinition();
                    if (null == xml) {
                        xml = org.apache.ambari.server.state.repository.VersionDefinitionXml.build(stack);
                    }
                    versionDefinitions.put(java.lang.String.format("%s-%s", stack.getName(), stack.getVersion()), xml);
                } catch (java.lang.Exception e) {
                    org.apache.ambari.server.api.services.AmbariMetaInfo.LOG.warn("Could not make a stack VDF for {}-{}: {}", stack.getName(), stack.getVersion(), e.getMessage());
                }
            } else {
                org.apache.ambari.server.state.StackId stackId = new org.apache.ambari.server.state.StackId(stack);
                if (!stack.isValid()) {
                    org.apache.ambari.server.api.services.AmbariMetaInfo.LOG.info("Stack {} is not valid, skipping VDF: {}", stackId, org.apache.commons.lang.StringUtils.join(stack.getErrors(), "; "));
                } else if (!stack.isActive()) {
                    org.apache.ambari.server.api.services.AmbariMetaInfo.LOG.info("Stack {} is not active, skipping VDF", stackId);
                }
            }
        }
    }

    public org.apache.ambari.server.state.repository.VersionDefinitionXml getVersionDefinition(java.lang.String versionDefinitionId) {
        ensureVersionDefinitions();
        return versionDefinitions.get(versionDefinitionId);
    }

    public java.util.Map<java.lang.String, org.apache.ambari.server.state.repository.VersionDefinitionXml> getVersionDefinitions() {
        ensureVersionDefinitions();
        return versionDefinitions;
    }

    org.apache.ambari.server.state.kerberos.KerberosDescriptor readKerberosDescriptorFromFile(java.lang.String fileLocation) throws org.apache.ambari.server.AmbariException {
        if (!org.apache.commons.lang.StringUtils.isEmpty(fileLocation)) {
            java.io.File file = new java.io.File(fileLocation);
            if (file.canRead()) {
                try {
                    return kerberosDescriptorFactory.createInstance(file);
                } catch (java.io.IOException e) {
                    throw new org.apache.ambari.server.AmbariException(java.lang.String.format("Failed to parse Kerberos descriptor file %s", file.getAbsolutePath()), e);
                }
            } else {
                throw new org.apache.ambari.server.AmbariException(java.lang.String.format("Unable to read Kerberos descriptor file %s", file.getAbsolutePath()));
            }
        } else {
            org.apache.ambari.server.api.services.AmbariMetaInfo.LOG.debug("Missing path to Kerberos descriptor, returning null");
        }
        return null;
    }

    public java.io.File getCommonWidgetsDescriptorFile() {
        return commonWidgetsDescriptorFile;
    }

    public void removeMpack(org.apache.ambari.server.orm.entities.MpackEntity mpackEntity, org.apache.ambari.server.orm.entities.StackEntity stackEntity) throws java.io.IOException {
        if (versionDefinitions != null) {
            versionDefinitions.clear();
        }
        boolean stackDelete = mpackManager.removeMpack(mpackEntity, stackEntity);
        if (stackDelete) {
            stackManager.removeStack(stackEntity);
        }
    }

    public java.util.Collection<org.apache.ambari.server.state.Mpack> getMpacks() {
        if (mpackManager.getMpackMap() != null) {
            return mpackManager.getMpackMap().values();
        }
        return java.util.Collections.emptySet();
    }

    public org.apache.ambari.server.state.Mpack getMpack(java.lang.Long mpackId) {
        if ((mpackManager.getMpackMap() != null) && mpackManager.getMpackMap().containsKey(mpackId)) {
            return mpackManager.getMpackMap().get(mpackId);
        }
        return null;
    }
}