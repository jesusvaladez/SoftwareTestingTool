package org.apache.ambari.server.stack;
import org.apache.commons.lang.StringUtils;
public class StackModule extends org.apache.ambari.server.stack.BaseModule<org.apache.ambari.server.stack.StackModule, org.apache.ambari.server.state.StackInfo> implements org.apache.ambari.server.stack.Validable {
    private org.apache.ambari.server.stack.StackContext stackContext;

    private java.util.Map<java.lang.String, org.apache.ambari.server.stack.ConfigurationModule> configurationModules = new java.util.HashMap<>();

    private java.util.Map<java.lang.String, org.apache.ambari.server.stack.ServiceModule> serviceModules = new java.util.HashMap<>();

    private java.util.Map<java.lang.String, org.apache.ambari.server.stack.ExtensionModule> extensionModules = new java.util.HashMap<>();

    private org.apache.ambari.server.state.StackInfo stackInfo;

    private org.apache.ambari.server.stack.StackDirectory stackDirectory;

    private java.lang.String id;

    protected boolean valid = true;

    org.apache.ambari.server.stack.ModuleFileUnmarshaller unmarshaller = new org.apache.ambari.server.stack.ModuleFileUnmarshaller();

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.stack.StackModule.class);

    public StackModule(org.apache.ambari.server.stack.StackDirectory stackDirectory, org.apache.ambari.server.stack.StackContext stackContext) {
        this.stackDirectory = stackDirectory;
        this.stackContext = stackContext;
        stackInfo = new org.apache.ambari.server.state.StackInfo();
        populateStackInfo();
    }

    public java.util.Map<java.lang.String, org.apache.ambari.server.stack.ServiceModule> getServiceModules() {
        return serviceModules;
    }

    public java.util.Map<java.lang.String, org.apache.ambari.server.stack.ExtensionModule> getExtensionModules() {
        return extensionModules;
    }

    @java.lang.Override
    public void resolve(org.apache.ambari.server.stack.StackModule parentModule, java.util.Map<java.lang.String, org.apache.ambari.server.stack.StackModule> allStacks, java.util.Map<java.lang.String, org.apache.ambari.server.stack.ServiceModule> commonServices, java.util.Map<java.lang.String, org.apache.ambari.server.stack.ExtensionModule> extensions) throws org.apache.ambari.server.AmbariException {
        moduleState = org.apache.ambari.server.stack.ModuleState.VISITED;
        org.apache.ambari.server.stack.StackModule.LOG.info(java.lang.String.format("Resolve: %s:%s", stackInfo.getName(), stackInfo.getVersion()));
        java.lang.String parentVersion = stackInfo.getParentStackVersion();
        mergeServicesWithExplicitParent(allStacks, commonServices, extensions);
        addExtensionServices();
        if (parentVersion != null) {
            mergeStackWithParent(parentVersion, allStacks, commonServices, extensions);
        }
        for (org.apache.ambari.server.state.ExtensionInfo extension : stackInfo.getExtensions()) {
            java.lang.String extensionKey = (extension.getName() + org.apache.ambari.server.stack.StackManager.PATH_DELIMITER) + extension.getVersion();
            org.apache.ambari.server.stack.ExtensionModule extensionModule = extensions.get(extensionKey);
            if (extensionModule == null) {
                throw new org.apache.ambari.server.AmbariException(((((("Extension '" + stackInfo.getName()) + ":") + stackInfo.getVersion()) + "' specifies an extension ") + extensionKey) + " that doesn't exist");
            }
            mergeStackWithExtension(extensionModule, allStacks, commonServices, extensions);
        }
        processUpgradePacks();
        processRepositories();
        processPropertyDependencies();
        validateBulkCommandComponents(allStacks);
        moduleState = org.apache.ambari.server.stack.ModuleState.RESOLVED;
    }

    @java.lang.Override
    public org.apache.ambari.server.state.StackInfo getModuleInfo() {
        return stackInfo;
    }

    @java.lang.Override
    public boolean isDeleted() {
        return false;
    }

    @java.lang.Override
    public java.lang.String getId() {
        return id;
    }

    @java.lang.Override
    public void finalizeModule() {
        finalizeChildModules(serviceModules.values());
        finalizeChildModules(configurationModules.values());
        for (org.apache.ambari.server.stack.ServiceModule module : serviceModules.values()) {
            mergeRoleCommandOrder(module);
        }
        java.util.List<java.lang.String> servicesWithNoConfigs = new java.util.ArrayList<>();
        for (org.apache.ambari.server.stack.ServiceModule serviceModule : serviceModules.values()) {
            if (!serviceModule.hasConfigs()) {
                servicesWithNoConfigs.add(serviceModule.getId());
            }
        }
        stackInfo.setServicesWithNoConfigs(servicesWithNoConfigs);
    }

    public org.apache.ambari.server.stack.StackDirectory getStackDirectory() {
        return stackDirectory;
    }

    private void mergeStackWithParent(java.lang.String parentVersion, java.util.Map<java.lang.String, org.apache.ambari.server.stack.StackModule> allStacks, java.util.Map<java.lang.String, org.apache.ambari.server.stack.ServiceModule> commonServices, java.util.Map<java.lang.String, org.apache.ambari.server.stack.ExtensionModule> extensions) throws org.apache.ambari.server.AmbariException {
        java.lang.String parentStackKey = (stackInfo.getName() + org.apache.ambari.server.stack.StackManager.PATH_DELIMITER) + parentVersion;
        org.apache.ambari.server.stack.StackModule parentStack = allStacks.get(parentStackKey);
        if (parentStack == null) {
            throw new org.apache.ambari.server.AmbariException(((("Stack '" + stackInfo.getName()) + ":") + stackInfo.getVersion()) + "' specifies a parent that doesn't exist");
        }
        resolveStack(parentStack, allStacks, commonServices, extensions);
        mergeConfigurations(parentStack, allStacks, commonServices, extensions);
        mergeRoleCommandOrder(parentStack);
        if (stackInfo.getKerberosDescriptorPreConfigurationFileLocation() == null) {
            stackInfo.setKerberosDescriptorPreConfigurationFileLocation(parentStack.getModuleInfo().getKerberosDescriptorPreConfigurationFileLocation());
        }
        mergeServicesWithParent(parentStack, allStacks, commonServices, extensions);
    }

    private void mergeStackWithExtension(org.apache.ambari.server.stack.ExtensionModule extension, java.util.Map<java.lang.String, org.apache.ambari.server.stack.StackModule> allStacks, java.util.Map<java.lang.String, org.apache.ambari.server.stack.ServiceModule> commonServices, java.util.Map<java.lang.String, org.apache.ambari.server.stack.ExtensionModule> extensions) throws org.apache.ambari.server.AmbariException {
    }

    private void mergeServicesWithParent(org.apache.ambari.server.stack.StackModule parentStack, java.util.Map<java.lang.String, org.apache.ambari.server.stack.StackModule> allStacks, java.util.Map<java.lang.String, org.apache.ambari.server.stack.ServiceModule> commonServices, java.util.Map<java.lang.String, org.apache.ambari.server.stack.ExtensionModule> extensions) throws org.apache.ambari.server.AmbariException {
        stackInfo.getServices().clear();
        java.util.Collection<org.apache.ambari.server.stack.ServiceModule> mergedModules = mergeChildModules(allStacks, commonServices, extensions, serviceModules, parentStack.serviceModules);
        java.util.List<java.lang.String> removedServices = new java.util.ArrayList<>();
        for (org.apache.ambari.server.stack.ServiceModule module : mergedModules) {
            if (module.isDeleted()) {
                removedServices.add(module.getId());
            } else {
                serviceModules.put(module.getId(), module);
                stackInfo.getServices().add(module.getModuleInfo());
            }
        }
        stackInfo.setRemovedServices(removedServices);
    }

    private void mergeServicesWithExplicitParent(java.util.Map<java.lang.String, org.apache.ambari.server.stack.StackModule> allStacks, java.util.Map<java.lang.String, org.apache.ambari.server.stack.ServiceModule> commonServices, java.util.Map<java.lang.String, org.apache.ambari.server.stack.ExtensionModule> extensions) throws org.apache.ambari.server.AmbariException {
        for (org.apache.ambari.server.stack.ServiceModule service : serviceModules.values()) {
            org.apache.ambari.server.state.ServiceInfo serviceInfo = service.getModuleInfo();
            java.lang.String parent = serviceInfo.getParent();
            if (parent != null) {
                mergeServiceWithExplicitParent(service, parent, allStacks, commonServices, extensions);
            }
        }
    }

    private void mergeServiceWithExplicitParent(org.apache.ambari.server.stack.ServiceModule service, java.lang.String parent, java.util.Map<java.lang.String, org.apache.ambari.server.stack.StackModule> allStacks, java.util.Map<java.lang.String, org.apache.ambari.server.stack.ServiceModule> commonServices, java.util.Map<java.lang.String, org.apache.ambari.server.stack.ExtensionModule> extensions) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.stack.StackModule.LOG.info(java.lang.String.format("Merge service %s with explicit parent: %s", service.getModuleInfo().getName(), parent));
        if (isCommonServiceParent(parent)) {
            mergeServiceWithCommonServiceParent(service, parent, allStacks, commonServices, extensions);
        } else if (isExtensionServiceParent(parent)) {
            mergeServiceWithExtensionServiceParent(service, parent, allStacks, commonServices, extensions);
        } else {
            mergeServiceWithStackServiceParent(service, parent, allStacks, commonServices, extensions);
        }
    }

    private boolean isCommonServiceParent(java.lang.String parent) {
        return ((parent != null) && (!parent.isEmpty())) && parent.split(org.apache.ambari.server.stack.StackManager.PATH_DELIMITER)[0].equalsIgnoreCase(org.apache.ambari.server.stack.StackManager.COMMON_SERVICES);
    }

    private boolean isExtensionServiceParent(java.lang.String parent) {
        return ((parent != null) && (!parent.isEmpty())) && parent.split(org.apache.ambari.server.stack.StackManager.PATH_DELIMITER)[0].equalsIgnoreCase(org.apache.ambari.server.stack.StackManager.EXTENSIONS);
    }

    private void addExtensionServices() throws org.apache.ambari.server.AmbariException {
        for (org.apache.ambari.server.stack.ExtensionModule extension : extensionModules.values()) {
            for (java.util.Map.Entry<java.lang.String, org.apache.ambari.server.stack.ServiceModule> entry : extension.getServiceModules().entrySet()) {
                serviceModules.put(entry.getKey(), entry.getValue());
            }
            stackInfo.addExtension(extension.getModuleInfo());
        }
    }

    private void mergeServiceWithCommonServiceParent(org.apache.ambari.server.stack.ServiceModule service, java.lang.String parent, java.util.Map<java.lang.String, org.apache.ambari.server.stack.StackModule> allStacks, java.util.Map<java.lang.String, org.apache.ambari.server.stack.ServiceModule> commonServices, java.util.Map<java.lang.String, org.apache.ambari.server.stack.ExtensionModule> extensions) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.ServiceInfo serviceInfo = service.getModuleInfo();
        java.lang.String[] parentToks = parent.split(org.apache.ambari.server.stack.StackManager.PATH_DELIMITER);
        if ((parentToks.length != 3) || (!parentToks[0].equalsIgnoreCase(org.apache.ambari.server.stack.StackManager.COMMON_SERVICES))) {
            throw new org.apache.ambari.server.AmbariException(((((((("The service '" + serviceInfo.getName()) + "' in stack '") + stackInfo.getName()) + ":") + stackInfo.getVersion()) + "' extends an invalid parent: '") + parent) + "'");
        }
        java.lang.String baseServiceKey = (parentToks[1] + org.apache.ambari.server.stack.StackManager.PATH_DELIMITER) + parentToks[2];
        org.apache.ambari.server.stack.ServiceModule baseService = commonServices.get(baseServiceKey);
        if (baseService == null) {
            setValid(false);
            stackInfo.setValid(false);
            java.lang.String error = ((((((("The service '" + serviceInfo.getName()) + "' in stack '") + stackInfo.getName()) + ":") + stackInfo.getVersion()) + "' extends a non-existent service: '") + parent) + "'";
            addError(error);
            stackInfo.addError(error);
        } else if (baseService.isValid()) {
            service.resolveExplicit(baseService, allStacks, commonServices, extensions);
        } else {
            setValid(false);
            stackInfo.setValid(false);
            addErrors(baseService.getErrors());
            stackInfo.addErrors(baseService.getErrors());
        }
    }

    private void mergeServiceWithExtensionServiceParent(org.apache.ambari.server.stack.ServiceModule service, java.lang.String parent, java.util.Map<java.lang.String, org.apache.ambari.server.stack.StackModule> allStacks, java.util.Map<java.lang.String, org.apache.ambari.server.stack.ServiceModule> commonServices, java.util.Map<java.lang.String, org.apache.ambari.server.stack.ExtensionModule> extensions) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.ServiceInfo serviceInfo = service.getModuleInfo();
        java.lang.String[] parentToks = parent.split(org.apache.ambari.server.stack.StackManager.PATH_DELIMITER);
        if ((parentToks.length != 4) || (!parentToks[0].equalsIgnoreCase(org.apache.ambari.server.stack.StackManager.EXTENSIONS))) {
            throw new org.apache.ambari.server.AmbariException(((((((("The service '" + serviceInfo.getName()) + "' in stack '") + stackInfo.getName()) + ":") + stackInfo.getVersion()) + "' extends an invalid parent: '") + parent) + "'");
        }
        java.lang.String extensionKey = (parentToks[1] + org.apache.ambari.server.stack.StackManager.PATH_DELIMITER) + parentToks[2];
        org.apache.ambari.server.stack.ExtensionModule extension = extensions.get(extensionKey);
        if ((extension == null) || (!extension.isValid())) {
            setValid(false);
            addError(((((((("The service '" + serviceInfo.getName()) + "' in stack '") + stackInfo.getName()) + ":") + stackInfo.getVersion()) + "' extends a non-existent service: '") + parent) + "'");
        } else {
            resolveExtension(extension, allStacks, commonServices, extensions);
            org.apache.ambari.server.stack.ServiceModule parentService = extension.getServiceModules().get(parentToks[3]);
            if ((parentService == null) || (!parentService.isValid())) {
                setValid(false);
                addError(((((((("The service '" + serviceInfo.getName()) + "' in stack '") + stackInfo.getName()) + ":") + stackInfo.getVersion()) + "' extends a non-existent service: '") + parent) + "'");
            } else {
                service.resolve(parentService, allStacks, commonServices, extensions);
            }
        }
    }

    private void mergeServiceWithStackServiceParent(org.apache.ambari.server.stack.ServiceModule service, java.lang.String parent, java.util.Map<java.lang.String, org.apache.ambari.server.stack.StackModule> allStacks, java.util.Map<java.lang.String, org.apache.ambari.server.stack.ServiceModule> commonServices, java.util.Map<java.lang.String, org.apache.ambari.server.stack.ExtensionModule> extensions) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.ServiceInfo serviceInfo = service.getModuleInfo();
        java.lang.String[] parentToks = parent.split(org.apache.ambari.server.stack.StackManager.PATH_DELIMITER);
        if (((parentToks.length != 3) || parentToks[0].equalsIgnoreCase(org.apache.ambari.server.stack.StackManager.EXTENSIONS)) || parentToks[0].equalsIgnoreCase(org.apache.ambari.server.stack.StackManager.COMMON_SERVICES)) {
            throw new org.apache.ambari.server.AmbariException(((((((("The service '" + serviceInfo.getName()) + "' in stack '") + stackInfo.getName()) + ":") + stackInfo.getVersion()) + "' extends an invalid parent: '") + parent) + "'");
        }
        java.lang.String baseStackKey = (parentToks[0] + org.apache.ambari.server.stack.StackManager.PATH_DELIMITER) + parentToks[1];
        org.apache.ambari.server.stack.StackModule baseStack = allStacks.get(baseStackKey);
        if (baseStack == null) {
            throw new org.apache.ambari.server.AmbariException(((((((("The service '" + serviceInfo.getName()) + "' in stack '") + stackInfo.getName()) + ":") + stackInfo.getVersion()) + "' extends a service in a non-existent stack: '") + baseStackKey) + "'");
        }
        resolveStack(baseStack, allStacks, commonServices, extensions);
        org.apache.ambari.server.stack.ServiceModule baseService = baseStack.serviceModules.get(parentToks[2]);
        if (baseService == null) {
            throw new org.apache.ambari.server.AmbariException(((((((("The service '" + serviceInfo.getName()) + "' in stack '") + stackInfo.getName()) + ":") + stackInfo.getVersion()) + "' extends a non-existent service: '") + parent) + "'");
        }
        service.resolveExplicit(baseService, allStacks, commonServices, extensions);
    }

    private void populateStackInfo() {
        stackInfo.setName(stackDirectory.getStackDirName());
        stackInfo.setVersion(stackDirectory.getName());
        id = java.lang.String.format("%s:%s", stackInfo.getName(), stackInfo.getVersion());
        org.apache.ambari.server.stack.StackModule.LOG.debug("Adding new stack to known stacks, stackName = {}, stackVersion = {}", stackInfo.getName(), stackInfo.getVersion());
        org.apache.ambari.server.state.stack.StackMetainfoXml smx = stackDirectory.getMetaInfoFile();
        if (smx != null) {
            if (!smx.isValid()) {
                stackInfo.setValid(false);
                stackInfo.addErrors(smx.getErrors());
            }
            stackInfo.setMinJdk(smx.getMinJdk());
            stackInfo.setMaxJdk(smx.getMaxJdk());
            stackInfo.setActive(smx.getVersion().isActive());
            stackInfo.setParentStackVersion(smx.getExtends());
            stackInfo.setRcoFileLocation(stackDirectory.getRcoFilePath());
            stackInfo.setKerberosDescriptorPreConfigurationFileLocation(stackDirectory.getKerberosDescriptorPreconfigureFilePath());
            stackInfo.setUpgradesFolder(stackDirectory.getUpgradesDir());
            stackInfo.setUpgradePacks(stackDirectory.getUpgradePacks());
            stackInfo.setConfigUpgradePack(stackDirectory.getConfigUpgradePack());
            stackInfo.setRoleCommandOrder(stackDirectory.getRoleCommandOrder());
            stackInfo.setReleaseVersionClass(smx.getVersion().getReleaseVersion());
            stackInfo.setLibraryClassLoader(stackDirectory.getLibraryClassLoader());
            populateConfigurationModules();
        }
        try {
            org.apache.ambari.server.state.stack.RepositoryXml rxml = stackDirectory.getRepoFile();
            if ((rxml != null) && (!rxml.isValid())) {
                stackInfo.setValid(false);
                stackInfo.addErrors(rxml.getErrors());
            }
            populateServices();
            if (!stackInfo.isValid()) {
                setValid(false);
                addErrors(stackInfo.getErrors());
            }
        } catch (java.lang.Exception e) {
            java.lang.String error = (("Exception caught while populating services for stack: " + stackInfo.getName()) + "-") + stackInfo.getVersion();
            setValid(false);
            stackInfo.setValid(false);
            addError(error);
            stackInfo.addError(error);
            org.apache.ambari.server.stack.StackModule.LOG.error(error);
        }
    }

    private void populateServices() throws org.apache.ambari.server.AmbariException {
        for (org.apache.ambari.server.stack.ServiceDirectory serviceDir : stackDirectory.getServiceDirectories()) {
            populateService(serviceDir);
        }
    }

    private void populateService(org.apache.ambari.server.stack.ServiceDirectory serviceDirectory) {
        java.util.Collection<org.apache.ambari.server.stack.ServiceModule> serviceModules = new java.util.ArrayList<>();
        org.apache.ambari.server.state.stack.ServiceMetainfoXml metaInfoXml = serviceDirectory.getMetaInfoFile();
        if (!metaInfoXml.isValid()) {
            stackInfo.setValid(metaInfoXml.isValid());
            setValid(metaInfoXml.isValid());
            stackInfo.addErrors(metaInfoXml.getErrors());
            addErrors(metaInfoXml.getErrors());
            return;
        }
        java.util.List<org.apache.ambari.server.state.ServiceInfo> serviceInfos = metaInfoXml.getServices();
        for (org.apache.ambari.server.state.ServiceInfo serviceInfo : serviceInfos) {
            org.apache.ambari.server.stack.ServiceModule serviceModule = new org.apache.ambari.server.stack.ServiceModule(stackContext, serviceInfo, serviceDirectory);
            serviceModules.add(serviceModule);
            if (!serviceModule.isValid()) {
                stackInfo.setValid(false);
                setValid(false);
                stackInfo.addErrors(serviceModule.getErrors());
                addErrors(serviceModule.getErrors());
            }
        }
        addServices(serviceModules);
    }

    private void populateConfigurationModules() {
        org.apache.ambari.server.stack.ConfigurationDirectory configDirectory = stackDirectory.getConfigurationDirectory(org.apache.ambari.server.stack.StackDirectory.SERVICE_CONFIG_FOLDER_NAME, org.apache.ambari.server.stack.StackDirectory.SERVICE_PROPERTIES_FOLDER_NAME);
        if (configDirectory != null) {
            for (org.apache.ambari.server.stack.ConfigurationModule config : configDirectory.getConfigurationModules()) {
                if (stackInfo.isValid()) {
                    stackInfo.setValid(config.isValid());
                    stackInfo.addErrors(config.getErrors());
                }
                stackInfo.getProperties().addAll(config.getModuleInfo().getProperties());
                stackInfo.setConfigTypeAttributes(config.getConfigType(), config.getModuleInfo().getAttributes());
                configurationModules.put(config.getConfigType(), config);
            }
        }
    }

    private void mergeConfigurations(org.apache.ambari.server.stack.StackModule parent, java.util.Map<java.lang.String, org.apache.ambari.server.stack.StackModule> allStacks, java.util.Map<java.lang.String, org.apache.ambari.server.stack.ServiceModule> commonServices, java.util.Map<java.lang.String, org.apache.ambari.server.stack.ExtensionModule> extensions) throws org.apache.ambari.server.AmbariException {
        stackInfo.getProperties().clear();
        stackInfo.setAllConfigAttributes(new java.util.HashMap<>());
        java.util.Collection<org.apache.ambari.server.stack.ConfigurationModule> mergedModules = mergeChildModules(allStacks, commonServices, extensions, configurationModules, parent.configurationModules);
        for (org.apache.ambari.server.stack.ConfigurationModule module : mergedModules) {
            if (!module.isDeleted()) {
                configurationModules.put(module.getId(), module);
                stackInfo.getProperties().addAll(module.getModuleInfo().getProperties());
                stackInfo.setConfigTypeAttributes(module.getConfigType(), module.getModuleInfo().getAttributes());
            }
        }
    }

    private void resolveStack(org.apache.ambari.server.stack.StackModule stackToBeResolved, java.util.Map<java.lang.String, org.apache.ambari.server.stack.StackModule> allStacks, java.util.Map<java.lang.String, org.apache.ambari.server.stack.ServiceModule> commonServices, java.util.Map<java.lang.String, org.apache.ambari.server.stack.ExtensionModule> extensions) throws org.apache.ambari.server.AmbariException {
        if (stackToBeResolved.getModuleState() == org.apache.ambari.server.stack.ModuleState.INIT) {
            stackToBeResolved.resolve(null, allStacks, commonServices, extensions);
        } else if (stackToBeResolved.getModuleState() == org.apache.ambari.server.stack.ModuleState.VISITED) {
            throw new org.apache.ambari.server.AmbariException("Cycle detected while parsing stack definition");
        }
        if ((!stackToBeResolved.isValid()) || ((stackToBeResolved.getModuleInfo() != null) && (!stackToBeResolved.getModuleInfo().isValid()))) {
            setValid(stackToBeResolved.isValid());
            stackInfo.setValid(stackToBeResolved.stackInfo.isValid());
            addErrors(stackToBeResolved.getErrors());
            stackInfo.addErrors(stackToBeResolved.getErrors());
        }
    }

    private void resolveExtension(org.apache.ambari.server.stack.ExtensionModule extension, java.util.Map<java.lang.String, org.apache.ambari.server.stack.StackModule> allStacks, java.util.Map<java.lang.String, org.apache.ambari.server.stack.ServiceModule> commonServices, java.util.Map<java.lang.String, org.apache.ambari.server.stack.ExtensionModule> extensions) throws org.apache.ambari.server.AmbariException {
        if (extension.getModuleState() == org.apache.ambari.server.stack.ModuleState.INIT) {
            extension.resolve(null, allStacks, commonServices, extensions);
        } else if (extension.getModuleState() == org.apache.ambari.server.stack.ModuleState.VISITED) {
            throw new org.apache.ambari.server.AmbariException("Cycle detected while parsing extension definition");
        }
        if ((!extension.isValid()) || ((extension.getModuleInfo() != null) && (!extension.getModuleInfo().isValid()))) {
            setValid(false);
            addError("Stack includes an invalid extension: " + extension.getModuleInfo().getName());
        }
    }

    private void addService(org.apache.ambari.server.stack.ServiceModule service) {
        org.apache.ambari.server.state.ServiceInfo serviceInfo = service.getModuleInfo();
        java.lang.Object previousValue = serviceModules.put(service.getId(), service);
        if (previousValue == null) {
            stackInfo.getServices().add(serviceInfo);
        }
    }

    private void addServices(java.util.Collection<org.apache.ambari.server.stack.ServiceModule> services) {
        for (org.apache.ambari.server.stack.ServiceModule service : services) {
            addService(service);
        }
    }

    private void processPropertyDependencies() {
        java.util.Map<org.apache.ambari.server.state.PropertyDependencyInfo, java.util.Set<org.apache.ambari.server.state.PropertyDependencyInfo>> dependedByMap = new java.util.HashMap<>();
        for (org.apache.ambari.server.stack.ServiceModule serviceModule : serviceModules.values()) {
            java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> componentRefreshCommandsMap = new java.util.HashMap();
            for (org.apache.ambari.server.state.PropertyInfo pi : serviceModule.getModuleInfo().getProperties()) {
                for (org.apache.ambari.server.state.PropertyDependencyInfo pdi : pi.getDependsOnProperties()) {
                    java.lang.String type = org.apache.ambari.server.state.ConfigHelper.fileNameToConfigType(pi.getFilename());
                    java.lang.String name = pi.getName();
                    org.apache.ambari.server.state.PropertyDependencyInfo propertyDependency = new org.apache.ambari.server.state.PropertyDependencyInfo(type, name);
                    if (dependedByMap.keySet().contains(pdi)) {
                        dependedByMap.get(pdi).add(propertyDependency);
                    } else {
                        java.util.Set<org.apache.ambari.server.state.PropertyDependencyInfo> newDependenciesSet = new java.util.HashSet<>();
                        newDependenciesSet.add(propertyDependency);
                        dependedByMap.put(pdi, newDependenciesSet);
                    }
                }
                if ((pi.getSupportedRefreshCommands() != null) && (pi.getSupportedRefreshCommands().size() > 0)) {
                    java.lang.String type = org.apache.ambari.server.state.ConfigHelper.fileNameToConfigType(pi.getFilename());
                    java.lang.String propertyName = (type + "/") + pi.getName();
                    java.util.Map<java.lang.String, java.lang.String> refreshCommandPropertyMap = componentRefreshCommandsMap.get(propertyName);
                    for (org.apache.ambari.server.state.RefreshCommand refreshCommand : pi.getSupportedRefreshCommands()) {
                        java.lang.String componentName = refreshCommand.getComponentName();
                        if (refreshCommandPropertyMap == null) {
                            refreshCommandPropertyMap = new java.util.HashMap<>();
                            componentRefreshCommandsMap.put(propertyName, refreshCommandPropertyMap);
                        }
                        refreshCommandPropertyMap.put(componentName, refreshCommand.getCommand());
                    }
                }
            }
            stackInfo.getRefreshCommandConfiguration().addRefreshCommands(componentRefreshCommandsMap);
        }
        for (org.apache.ambari.server.stack.ServiceModule serviceModule : serviceModules.values()) {
            addDependedByProperties(dependedByMap, serviceModule.getModuleInfo().getProperties());
        }
        addDependedByProperties(dependedByMap, stackInfo.getProperties());
    }

    private void addDependedByProperties(java.util.Map<org.apache.ambari.server.state.PropertyDependencyInfo, java.util.Set<org.apache.ambari.server.state.PropertyDependencyInfo>> dependedByMap, java.util.Collection<org.apache.ambari.server.state.PropertyInfo> properties) {
        for (org.apache.ambari.server.state.PropertyInfo pi : properties) {
            java.lang.String type = org.apache.ambari.server.state.ConfigHelper.fileNameToConfigType(pi.getFilename());
            java.lang.String name = pi.getName();
            java.util.Set<org.apache.ambari.server.state.PropertyDependencyInfo> set = dependedByMap.remove(new org.apache.ambari.server.state.PropertyDependencyInfo(type, name));
            if (set != null) {
                pi.getDependedByProperties().addAll(set);
            }
        }
    }

    private void processUpgradePacks() throws org.apache.ambari.server.AmbariException {
        if (stackInfo.getUpgradePacks() == null) {
            return;
        }
        for (org.apache.ambari.server.stack.upgrade.UpgradePack pack : stackInfo.getUpgradePacks().values()) {
            java.util.List<org.apache.ambari.server.stack.upgrade.UpgradePack> servicePacks = new java.util.ArrayList<>();
            for (org.apache.ambari.server.stack.ServiceModule module : serviceModules.values()) {
                java.io.File upgradesFolder = module.getModuleInfo().getServiceUpgradesFolder();
                if (upgradesFolder != null) {
                    org.apache.ambari.server.stack.upgrade.UpgradePack servicePack = getServiceUpgradePack(pack, upgradesFolder);
                    if (servicePack != null) {
                        servicePacks.add(servicePack);
                    }
                }
            }
            if (servicePacks.size() > 0) {
                org.apache.ambari.server.stack.StackModule.LOG.info("Merging service specific upgrades for pack: " + pack.getName());
                mergeUpgradePack(pack, servicePacks);
            }
        }
        org.apache.ambari.server.stack.upgrade.ConfigUpgradePack configPack = stackInfo.getConfigUpgradePack();
        if (configPack == null) {
            return;
        }
        for (org.apache.ambari.server.stack.ServiceModule module : serviceModules.values()) {
            java.io.File upgradesFolder = module.getModuleInfo().getServiceUpgradesFolder();
            if (upgradesFolder != null) {
                mergeConfigUpgradePack(configPack, upgradesFolder);
            }
        }
    }

    private void mergeConfigUpgradePack(org.apache.ambari.server.stack.upgrade.ConfigUpgradePack pack, java.io.File upgradesFolder) throws org.apache.ambari.server.AmbariException {
        java.io.File stackFolder = new java.io.File(upgradesFolder, stackInfo.getName());
        java.io.File versionFolder = new java.io.File(stackFolder, stackInfo.getVersion());
        java.io.File serviceConfig = new java.io.File(versionFolder, org.apache.ambari.server.stack.StackDefinitionDirectory.CONFIG_UPGRADE_XML_FILENAME_PREFIX);
        if (!serviceConfig.exists()) {
            return;
        }
        try {
            org.apache.ambari.server.stack.upgrade.ConfigUpgradePack serviceConfigPack = unmarshaller.unmarshal(org.apache.ambari.server.stack.upgrade.ConfigUpgradePack.class, serviceConfig);
            pack.services.addAll(serviceConfigPack.services);
        } catch (java.lang.Exception e) {
            throw new org.apache.ambari.server.AmbariException("Unable to parse service config upgrade file at location: " + serviceConfig.getAbsolutePath(), e);
        }
    }

    private org.apache.ambari.server.stack.upgrade.UpgradePack getServiceUpgradePack(org.apache.ambari.server.stack.upgrade.UpgradePack pack, java.io.File upgradesFolder) throws org.apache.ambari.server.AmbariException {
        java.io.File stackFolder = new java.io.File(upgradesFolder, stackInfo.getName());
        java.io.File versionFolder = new java.io.File(stackFolder, stackInfo.getVersion());
        java.io.File servicePackFile = new java.io.File(versionFolder, pack.getName() + ".xml");
        org.apache.ambari.server.stack.StackModule.LOG.info("Service folder: " + servicePackFile.getAbsolutePath());
        if (servicePackFile.exists()) {
            return parseServiceUpgradePack(pack, servicePackFile);
        } else {
            org.apache.ambari.server.stack.upgrade.UpgradePack child = findServiceUpgradePack(pack, stackFolder);
            return null == child ? null : parseServiceUpgradePack(pack, child);
        }
    }

    private void mergeUpgradePack(org.apache.ambari.server.stack.upgrade.UpgradePack pack, java.util.List<org.apache.ambari.server.stack.upgrade.UpgradePack> servicePacks) throws org.apache.ambari.server.AmbariException {
        java.util.List<org.apache.ambari.server.stack.upgrade.Grouping> originalGroups = pack.getAllGroups();
        java.util.Map<java.lang.String, java.util.List<org.apache.ambari.server.stack.upgrade.Grouping>> allGroupMap = new java.util.HashMap<>();
        for (org.apache.ambari.server.stack.upgrade.Grouping group : originalGroups) {
            java.util.List<org.apache.ambari.server.stack.upgrade.Grouping> list = new java.util.ArrayList<>();
            list.add(group);
            allGroupMap.put(group.name, list);
        }
        for (org.apache.ambari.server.stack.upgrade.UpgradePack servicePack : servicePacks) {
            for (org.apache.ambari.server.stack.upgrade.Grouping group : servicePack.getAllGroups()) {
                if (servicePack.isAllTarget() && (!allGroupMap.keySet().contains(group.addAfterGroup))) {
                    org.apache.ambari.server.stack.StackModule.LOG.warn("Service Upgrade Pack specified after-group of {}, but that is not found in {}", group.addAfterGroup, org.apache.commons.lang.StringUtils.join(allGroupMap.keySet(), ','));
                    continue;
                }
                if (allGroupMap.containsKey(group.name)) {
                    java.util.List<org.apache.ambari.server.stack.upgrade.Grouping> list = allGroupMap.get(group.name);
                    org.apache.ambari.server.stack.upgrade.Grouping first = list.get(0);
                    if (!first.getClass().equals(group.getClass())) {
                        throw new org.apache.ambari.server.AmbariException((("Expected class: " + first.getClass()) + " instead of ") + group.getClass());
                    }
                    if ((group.addAfterGroupEntry == null) && (first.addAfterGroupEntry != null)) {
                        list.add(0, group);
                    } else {
                        list.add(group);
                    }
                } else {
                    java.util.List<org.apache.ambari.server.stack.upgrade.Grouping> list = new java.util.ArrayList<>();
                    list.add(group);
                    allGroupMap.put(group.name, list);
                }
            }
        }
        java.util.Map<java.lang.String, org.apache.ambari.server.stack.upgrade.Grouping> mergedGroupMap = new java.util.HashMap<>();
        for (java.lang.String key : allGroupMap.keySet()) {
            java.util.Iterator<org.apache.ambari.server.stack.upgrade.Grouping> iterator = allGroupMap.get(key).iterator();
            org.apache.ambari.server.stack.upgrade.Grouping group = iterator.next();
            if (iterator.hasNext()) {
                group.merge(iterator);
            }
            mergedGroupMap.put(key, group);
        }
        orderGroups(originalGroups, mergedGroupMap);
    }

    private void orderGroups(java.util.List<org.apache.ambari.server.stack.upgrade.Grouping> groups, java.util.Map<java.lang.String, org.apache.ambari.server.stack.upgrade.Grouping> mergedGroupMap) throws org.apache.ambari.server.AmbariException {
        java.util.Map<java.lang.String, java.util.List<org.apache.ambari.server.stack.upgrade.Grouping>> skippedGroups = new java.util.HashMap<>();
        for (java.util.Map.Entry<java.lang.String, org.apache.ambari.server.stack.upgrade.Grouping> entry : mergedGroupMap.entrySet()) {
            org.apache.ambari.server.stack.upgrade.Grouping group = entry.getValue();
            if (!groups.contains(group)) {
                boolean added = addGrouping(groups, group);
                if (added) {
                    addSkippedGroup(groups, skippedGroups, group);
                } else {
                    java.util.List<org.apache.ambari.server.stack.upgrade.Grouping> tmp = null;
                    if (skippedGroups.containsKey(group.addAfterGroup)) {
                        tmp = skippedGroups.get(group.addAfterGroup);
                    } else {
                        tmp = new java.util.ArrayList<>();
                        skippedGroups.put(group.addAfterGroup, tmp);
                    }
                    tmp.add(group);
                }
            }
        }
        if (!skippedGroups.isEmpty()) {
            throw new org.apache.ambari.server.AmbariException("Missing groups: " + skippedGroups.keySet());
        }
    }

    private boolean addGrouping(java.util.List<org.apache.ambari.server.stack.upgrade.Grouping> groups, org.apache.ambari.server.stack.upgrade.Grouping group) throws org.apache.ambari.server.AmbariException {
        if (group.addAfterGroup == null) {
            throw new org.apache.ambari.server.AmbariException(("Group " + group.name) + " needs to specify which group it should come after");
        } else {
            for (int index = groups.size() - 1; index >= 0; index--) {
                java.lang.String name = groups.get(index).name;
                if (name.equals(group.addAfterGroup)) {
                    groups.add(index + 1, group);
                    org.apache.ambari.server.stack.StackModule.LOG.debug("Added group/after: {}/{}", group.name, group.addAfterGroup);
                    return true;
                }
            }
        }
        return false;
    }

    private void addSkippedGroup(java.util.List<org.apache.ambari.server.stack.upgrade.Grouping> groups, java.util.Map<java.lang.String, java.util.List<org.apache.ambari.server.stack.upgrade.Grouping>> skippedGroups, org.apache.ambari.server.stack.upgrade.Grouping groupJustAdded) throws org.apache.ambari.server.AmbariException {
        if (skippedGroups.containsKey(groupJustAdded.name)) {
            java.util.List<org.apache.ambari.server.stack.upgrade.Grouping> groupsToAdd = skippedGroups.remove(groupJustAdded.name);
            for (org.apache.ambari.server.stack.upgrade.Grouping group : groupsToAdd) {
                boolean added = addGrouping(groups, group);
                if (added) {
                    addSkippedGroup(groups, skippedGroups, group);
                } else {
                    throw new org.apache.ambari.server.AmbariException("Failed to add group " + group.name);
                }
            }
        }
    }

    private org.apache.ambari.server.stack.upgrade.UpgradePack findServiceUpgradePack(org.apache.ambari.server.stack.upgrade.UpgradePack base, java.io.File upgradeStackDirectory) {
        if ((!upgradeStackDirectory.exists()) || (!upgradeStackDirectory.isDirectory())) {
            return null;
        }
        java.io.File[] upgradeFiles = upgradeStackDirectory.listFiles(org.apache.ambari.server.stack.StackDirectory.XML_FILENAME_FILTER);
        if (0 == upgradeFiles.length) {
            return null;
        }
        for (java.io.File f : upgradeFiles) {
            try {
                org.apache.ambari.server.stack.upgrade.UpgradePack upgradePack = unmarshaller.unmarshal(org.apache.ambari.server.stack.upgrade.UpgradePack.class, f);
                if (upgradePack.isAllTarget() && (upgradePack.getType() == base.getType())) {
                    return upgradePack;
                }
            } catch (java.lang.Exception e) {
                org.apache.ambari.server.stack.StackModule.LOG.warn("File {} does not appear to be an upgrade pack and will be skipped ({})", f.getAbsolutePath(), e.getMessage());
            }
        }
        return null;
    }

    private org.apache.ambari.server.stack.upgrade.UpgradePack parseServiceUpgradePack(org.apache.ambari.server.stack.upgrade.UpgradePack parent, java.io.File serviceFile) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.stack.upgrade.UpgradePack pack = null;
        try {
            pack = unmarshaller.unmarshal(org.apache.ambari.server.stack.upgrade.UpgradePack.class, serviceFile);
        } catch (java.lang.Exception e) {
            throw new org.apache.ambari.server.AmbariException("Unable to parse service upgrade file at location: " + serviceFile.getAbsolutePath(), e);
        }
        return parseServiceUpgradePack(parent, pack);
    }

    private org.apache.ambari.server.stack.upgrade.UpgradePack parseServiceUpgradePack(org.apache.ambari.server.stack.upgrade.UpgradePack parent, org.apache.ambari.server.stack.upgrade.UpgradePack child) {
        parent.mergePrerequisiteChecks(child);
        parent.mergeProcessing(child);
        return child;
    }

    private void processRepositories() throws org.apache.ambari.server.AmbariException {
        java.util.List<org.apache.ambari.server.state.RepositoryInfo> stackRepos = java.util.Collections.emptyList();
        org.apache.ambari.server.state.stack.RepositoryXml rxml = stackDirectory.getRepoFile();
        if (null != rxml) {
            stackInfo.setRepositoryXml(rxml);
            org.apache.ambari.server.stack.StackModule.LOG.debug("Adding repositories to stack, stackName={}, stackVersion={}, repoFolder={}", stackInfo.getName(), stackInfo.getVersion(), stackDirectory.getRepoDir());
            stackRepos = rxml.getRepositories();
            stackInfo.getRepositories().addAll(stackRepos);
        }
        org.apache.ambari.server.stack.StackModule.LOG.debug("Process service custom repositories");
        java.util.Collection<org.apache.ambari.server.state.RepositoryInfo> serviceRepos = getUniqueServiceRepos(stackRepos);
        stackInfo.getRepositories().addAll(serviceRepos);
        if (((null != rxml) && (null != rxml.getLatestURI())) && (stackRepos.size() > 0)) {
            registerRepoUpdateTask(rxml);
        }
    }

    private void registerRepoUpdateTask(org.apache.ambari.server.state.stack.RepositoryXml rxml) {
        java.lang.String latest = rxml.getLatestURI();
        if (org.apache.commons.lang.StringUtils.isBlank(latest)) {
            return;
        }
        java.net.URI uri = org.apache.ambari.server.stack.StackModule.getURI(this, latest);
        if (null == uri) {
            org.apache.ambari.server.stack.StackModule.LOG.warn("Could not determine how to load stack {}-{} latest definition for {}", stackInfo.getName(), stackInfo.getVersion(), latest);
            return;
        }
        stackContext.registerRepoUpdateTask(uri, this);
    }

    public static java.net.URI getURI(org.apache.ambari.server.stack.StackModule module, java.lang.String uriString) {
        java.net.URI uri = null;
        if (uriString.startsWith("http")) {
            try {
                uri = new java.net.URI(uriString);
            } catch (java.net.URISyntaxException e) {
            }
        } else if ('.' == uriString.charAt(0)) {
            uri = new java.io.File(module.getStackDirectory().getRepoDir(), uriString).toURI();
        } else {
            uri = new java.io.File(uriString).toURI();
        }
        return uri;
    }

    @org.apache.ambari.annotations.Experimental(feature = org.apache.ambari.annotations.ExperimentalFeature.CUSTOM_SERVICE_REPOS, comment = "Remove logic for handling custom service repos after enabling multi-mpack cluster deployment")
    private java.util.Collection<org.apache.ambari.server.state.RepositoryInfo> getUniqueServiceRepos(java.util.List<org.apache.ambari.server.state.RepositoryInfo> stackRepos) {
        java.util.List<org.apache.ambari.server.state.RepositoryInfo> serviceRepos = getAllServiceRepos();
        com.google.common.collect.ImmutableListMultimap<java.lang.String, org.apache.ambari.server.state.RepositoryInfo> serviceReposByOsType = com.google.common.collect.Multimaps.index(serviceRepos, org.apache.ambari.server.state.RepositoryInfo.GET_OSTYPE_FUNCTION);
        com.google.common.collect.ImmutableListMultimap<java.lang.String, org.apache.ambari.server.state.RepositoryInfo> stackReposByOsType = com.google.common.collect.Multimaps.index(stackRepos, org.apache.ambari.server.state.RepositoryInfo.GET_OSTYPE_FUNCTION);
        java.util.Map<java.lang.String, org.apache.ambari.server.state.RepositoryInfo> uniqueServiceRepos = new java.util.HashMap<>();
        for (java.lang.String osType : serviceReposByOsType.keySet()) {
            java.util.List<org.apache.ambari.server.state.RepositoryInfo> stackReposForOsType = (stackReposByOsType.containsKey(osType)) ? stackReposByOsType.get(osType) : java.util.Collections.emptyList();
            java.util.List<org.apache.ambari.server.state.RepositoryInfo> serviceReposForOsType = serviceReposByOsType.get(osType);
            java.util.Set<java.lang.String> stackRepoNames = com.google.common.collect.ImmutableSet.copyOf(com.google.common.collect.Lists.transform(stackReposForOsType, org.apache.ambari.server.state.RepositoryInfo.GET_REPO_NAME_FUNCTION));
            java.util.Set<java.lang.String> stackRepoUrls = com.google.common.collect.ImmutableSet.copyOf(com.google.common.collect.Lists.transform(stackReposForOsType, org.apache.ambari.server.state.RepositoryInfo.SAFE_GET_BASE_URL_FUNCTION));
            java.util.Set<java.lang.String> duplicateServiceRepoNames = org.apache.ambari.server.stack.StackModule.findDuplicates(serviceReposForOsType, org.apache.ambari.server.state.RepositoryInfo.GET_REPO_NAME_FUNCTION);
            java.util.Set<java.lang.String> duplicateServiceRepoUrls = org.apache.ambari.server.stack.StackModule.findDuplicates(serviceReposForOsType, org.apache.ambari.server.state.RepositoryInfo.SAFE_GET_BASE_URL_FUNCTION);
            for (org.apache.ambari.server.state.RepositoryInfo repo : serviceReposForOsType) {
                if (stackRepoUrls.contains(repo.getBaseUrl())) {
                    org.apache.ambari.server.stack.StackModule.LOG.warn("Service repo has a base url that is identical to that of a stack repo: {}", repo);
                } else if (duplicateServiceRepoUrls.contains(repo.getBaseUrl())) {
                    org.apache.ambari.server.stack.StackModule.LOG.warn("Service repo has a base url that is identical to that of another service repo: {}", repo);
                }
                if (stackRepoNames.contains(repo.getRepoName())) {
                    org.apache.ambari.server.stack.StackModule.LOG.warn("Discarding service repository with the same name as one of the stack repos: {}", repo);
                } else if (duplicateServiceRepoNames.contains(repo.getRepoName())) {
                    org.apache.ambari.server.stack.StackModule.LOG.warn("Discarding service repository with duplicate name and different content: {}", repo);
                } else {
                    java.lang.String key = (((repo.getOsType() + "-") + repo.getRepoName()) + "-") + repo.getRepoId();
                    if (uniqueServiceRepos.containsKey(key)) {
                        uniqueServiceRepos.get(key).getApplicableServices().addAll(repo.getApplicableServices());
                    } else {
                        uniqueServiceRepos.put(key, repo);
                    }
                }
            }
        }
        return uniqueServiceRepos.values();
    }

    private static java.util.Set<java.lang.String> findDuplicates(java.util.List<org.apache.ambari.server.state.RepositoryInfo> input, com.google.common.base.Function<org.apache.ambari.server.state.RepositoryInfo, java.lang.String> keyExtractor) {
        com.google.common.collect.ListMultimap<java.lang.String, org.apache.ambari.server.state.RepositoryInfo> itemsByKey = com.google.common.collect.Multimaps.index(input, keyExtractor);
        java.util.Set<java.lang.String> duplicates = new java.util.HashSet<>();
        for (java.util.Map.Entry<java.lang.String, java.util.Collection<org.apache.ambari.server.state.RepositoryInfo>> entry : itemsByKey.asMap().entrySet()) {
            if (entry.getValue().size() > 1) {
                java.util.Set<org.apache.ambari.server.state.RepositoryInfo> differingItems = new java.util.HashSet<>();
                differingItems.addAll(entry.getValue());
                if (differingItems.size() > 1) {
                    duplicates.add(entry.getKey());
                }
            }
        }
        return duplicates;
    }

    private java.util.List<org.apache.ambari.server.state.RepositoryInfo> getAllServiceRepos() {
        java.util.List<org.apache.ambari.server.state.RepositoryInfo> repos = new java.util.ArrayList<>();
        for (org.apache.ambari.server.stack.ServiceModule sm : serviceModules.values()) {
            org.apache.ambari.server.stack.ServiceDirectory sd = sm.getServiceDirectory();
            if (sd instanceof org.apache.ambari.server.stack.StackServiceDirectory) {
                org.apache.ambari.server.stack.StackServiceDirectory ssd = ((org.apache.ambari.server.stack.StackServiceDirectory) (sd));
                org.apache.ambari.server.state.stack.RepositoryXml serviceRepoXml = ssd.getRepoFile();
                if (null != serviceRepoXml) {
                    java.util.List<org.apache.ambari.server.state.RepositoryInfo> serviceRepos = serviceRepoXml.getRepositories();
                    for (org.apache.ambari.server.state.RepositoryInfo serviceRepo : serviceRepos) {
                        serviceRepo.getApplicableServices().add(sm.getId());
                    }
                    repos.addAll(serviceRepos);
                    if (null != serviceRepoXml.getLatestURI()) {
                        registerRepoUpdateTask(serviceRepoXml);
                    }
                }
            }
        }
        return repos;
    }

    private void mergeRoleCommandOrder(org.apache.ambari.server.stack.StackModule parentStack) {
        stackInfo.getRoleCommandOrder().merge(parentStack.stackInfo.getRoleCommandOrder());
    }

    private void mergeRoleCommandOrder(org.apache.ambari.server.stack.ServiceModule service) {
        if (service.getModuleInfo().getRoleCommandOrder() == null) {
            return;
        }
        stackInfo.getRoleCommandOrder().merge(service.getModuleInfo().getRoleCommandOrder(), true);
        if (org.apache.ambari.server.stack.StackModule.LOG.isDebugEnabled()) {
            org.apache.ambari.server.stack.StackModule.LOG.debug("Role Command Order for {}-{} service {}", stackInfo.getName(), stackInfo.getVersion(), service.getModuleInfo().getName());
            stackInfo.getRoleCommandOrder().printRoleCommandOrder(org.apache.ambari.server.stack.StackModule.LOG);
        }
    }

    private void validateBulkCommandComponents(java.util.Map<java.lang.String, org.apache.ambari.server.stack.StackModule> allStacks) {
        if (null != stackInfo) {
            java.lang.String currentStackId = (stackInfo.getName() + org.apache.ambari.server.stack.StackManager.PATH_DELIMITER) + stackInfo.getVersion();
            org.apache.ambari.server.stack.StackModule.LOG.debug("Validate bulk command components for: {}", currentStackId);
            org.apache.ambari.server.stack.StackModule currentStack = allStacks.get(currentStackId);
            if (null != currentStack) {
                for (org.apache.ambari.server.stack.ServiceModule serviceModule : currentStack.getServiceModules().values()) {
                    org.apache.ambari.server.state.ServiceInfo service = serviceModule.getModuleInfo();
                    for (org.apache.ambari.server.state.ComponentInfo component : service.getComponents()) {
                        org.apache.ambari.server.state.BulkCommandDefinition bcd = component.getBulkCommandDefinition();
                        if ((null != bcd) && (null != bcd.getMasterComponent())) {
                            java.lang.String name = bcd.getMasterComponent();
                            org.apache.ambari.server.state.ComponentInfo targetComponent = service.getComponentByName(name);
                            if (null == targetComponent) {
                                java.lang.String serviceName = service.getName();
                                org.apache.ambari.server.stack.StackModule.LOG.error(java.lang.String.format("%s bulk command section for service %s in stack %s references a component %s which doesn't exist.", component.getName(), serviceName, currentStackId, name));
                            }
                        }
                    }
                }
            }
        }
    }

    @java.lang.Override
    public boolean isValid() {
        return valid;
    }

    @java.lang.Override
    public void setValid(boolean valid) {
        this.valid = valid;
    }

    private java.util.Set<java.lang.String> errorSet = new java.util.HashSet<>();

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
}