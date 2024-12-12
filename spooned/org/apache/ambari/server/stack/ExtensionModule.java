package org.apache.ambari.server.stack;
public class ExtensionModule extends org.apache.ambari.server.stack.BaseModule<org.apache.ambari.server.stack.ExtensionModule, org.apache.ambari.server.state.ExtensionInfo> implements org.apache.ambari.server.stack.Validable {
    private org.apache.ambari.server.stack.StackContext stackContext;

    private java.util.Map<java.lang.String, org.apache.ambari.server.stack.ConfigurationModule> configurationModules = new java.util.HashMap<>();

    private java.util.Map<java.lang.String, org.apache.ambari.server.stack.ServiceModule> serviceModules = new java.util.HashMap<>();

    private org.apache.ambari.server.state.ExtensionInfo extensionInfo;

    private org.apache.ambari.server.stack.ExtensionDirectory extensionDirectory;

    private java.lang.String id;

    protected boolean valid = true;

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.stack.ExtensionModule.class);

    public ExtensionModule(org.apache.ambari.server.stack.ExtensionDirectory extensionDirectory, org.apache.ambari.server.stack.StackContext stackContext) {
        this.extensionDirectory = extensionDirectory;
        this.stackContext = stackContext;
        this.extensionInfo = new org.apache.ambari.server.state.ExtensionInfo();
        populateExtensionInfo();
    }

    public java.util.Map<java.lang.String, org.apache.ambari.server.stack.ServiceModule> getServiceModules() {
        return serviceModules;
    }

    @java.lang.Override
    public void resolve(org.apache.ambari.server.stack.ExtensionModule parentModule, java.util.Map<java.lang.String, org.apache.ambari.server.stack.StackModule> allStacks, java.util.Map<java.lang.String, org.apache.ambari.server.stack.ServiceModule> commonServices, java.util.Map<java.lang.String, org.apache.ambari.server.stack.ExtensionModule> extensions) throws org.apache.ambari.server.AmbariException {
        moduleState = org.apache.ambari.server.stack.ModuleState.VISITED;
        checkExtensionName(allStacks);
        java.lang.String parentVersion = extensionInfo.getParentExtensionVersion();
        mergeServicesWithExplicitParent(allStacks, commonServices, extensions);
        if (parentVersion != null) {
            mergeExtensionWithParent(parentVersion, allStacks, commonServices, extensions);
        }
        moduleState = org.apache.ambari.server.stack.ModuleState.RESOLVED;
    }

    @java.lang.Override
    public org.apache.ambari.server.state.ExtensionInfo getModuleInfo() {
        return extensionInfo;
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
    }

    public org.apache.ambari.server.stack.ExtensionDirectory getExtensionDirectory() {
        return extensionDirectory;
    }

    private void mergeExtensionWithParent(java.lang.String parentVersion, java.util.Map<java.lang.String, org.apache.ambari.server.stack.StackModule> allStacks, java.util.Map<java.lang.String, org.apache.ambari.server.stack.ServiceModule> commonServices, java.util.Map<java.lang.String, org.apache.ambari.server.stack.ExtensionModule> extensions) throws org.apache.ambari.server.AmbariException {
        java.lang.String parentExtensionKey = (extensionInfo.getName() + org.apache.ambari.server.stack.StackManager.PATH_DELIMITER) + parentVersion;
        org.apache.ambari.server.stack.ExtensionModule parentExtension = extensions.get(parentExtensionKey);
        if (parentExtension == null) {
            throw new org.apache.ambari.server.AmbariException(((("Extension '" + extensionInfo.getName()) + ":") + extensionInfo.getVersion()) + "' specifies a parent that doesn't exist");
        }
        resolveExtension(parentExtension, allStacks, commonServices, extensions);
        mergeServicesWithParent(parentExtension, allStacks, commonServices, extensions);
    }

    private void mergeServicesWithParent(org.apache.ambari.server.stack.ExtensionModule parentExtension, java.util.Map<java.lang.String, org.apache.ambari.server.stack.StackModule> allStacks, java.util.Map<java.lang.String, org.apache.ambari.server.stack.ServiceModule> commonServices, java.util.Map<java.lang.String, org.apache.ambari.server.stack.ExtensionModule> extensions) throws org.apache.ambari.server.AmbariException {
        extensionInfo.getServices().clear();
        org.apache.ambari.server.stack.ExtensionModule.LOG.info("***Merging extension services with parent: " + parentExtension.getId());
        java.util.Collection<org.apache.ambari.server.stack.ServiceModule> mergedModules = mergeChildModules(allStacks, commonServices, extensions, serviceModules, parentExtension.serviceModules);
        for (org.apache.ambari.server.stack.ServiceModule module : mergedModules) {
            if (!module.isDeleted()) {
                serviceModules.put(module.getId(), module);
                extensionInfo.getServices().add(module.getModuleInfo());
            }
        }
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
        if (isCommonServiceParent(parent)) {
            org.apache.ambari.server.stack.ExtensionModule.LOG.info("merging with common service: " + service.getModuleInfo().getName());
            mergeServiceWithCommonServiceParent(service, parent, allStacks, commonServices, extensions);
            org.apache.ambari.server.stack.ExtensionModule.LOG.info("display name: " + service.getModuleInfo().getDisplayName());
        } else {
            throw new org.apache.ambari.server.AmbariException(((((((("The service '" + service.getModuleInfo().getName()) + "' in extension '") + extensionInfo.getName()) + ":") + extensionInfo.getVersion()) + "' extends an invalid parent: '") + parent) + "'");
        }
    }

    private void checkExtensionName(java.util.Map<java.lang.String, org.apache.ambari.server.stack.StackModule> allStacks) throws org.apache.ambari.server.AmbariException {
        java.lang.String name = extensionInfo.getName();
        for (org.apache.ambari.server.stack.StackModule stack : allStacks.values()) {
            java.lang.String stackName = stack.getModuleInfo().getName();
            if (name.equals(stackName)) {
                throw new org.apache.ambari.server.AmbariException(("The extension '" + name) + "' has a name which matches a stack name");
            }
        }
    }

    private boolean isCommonServiceParent(java.lang.String parent) {
        return ((parent != null) && (!parent.isEmpty())) && parent.split(org.apache.ambari.server.stack.StackManager.PATH_DELIMITER)[0].equalsIgnoreCase(org.apache.ambari.server.stack.StackManager.COMMON_SERVICES);
    }

    private void mergeServiceWithCommonServiceParent(org.apache.ambari.server.stack.ServiceModule service, java.lang.String parent, java.util.Map<java.lang.String, org.apache.ambari.server.stack.StackModule> allStacks, java.util.Map<java.lang.String, org.apache.ambari.server.stack.ServiceModule> commonServices, java.util.Map<java.lang.String, org.apache.ambari.server.stack.ExtensionModule> extensions) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.ServiceInfo serviceInfo = service.getModuleInfo();
        java.lang.String[] parentToks = parent.split(org.apache.ambari.server.stack.StackManager.PATH_DELIMITER);
        if ((parentToks.length != 3) || (!parentToks[0].equalsIgnoreCase(org.apache.ambari.server.stack.StackManager.COMMON_SERVICES))) {
            throw new org.apache.ambari.server.AmbariException(((((((("The service '" + serviceInfo.getName()) + "' in extension '") + extensionInfo.getName()) + ":") + extensionInfo.getVersion()) + "' extends an invalid parent: '") + parent) + "'");
        }
        java.lang.String baseServiceKey = (parentToks[1] + org.apache.ambari.server.stack.StackManager.PATH_DELIMITER) + parentToks[2];
        org.apache.ambari.server.stack.ServiceModule baseService = commonServices.get(baseServiceKey);
        if (baseService == null) {
            setValid(false);
            extensionInfo.setValid(false);
            java.lang.String error = ((((((("The service '" + serviceInfo.getName()) + "' in extension '") + extensionInfo.getName()) + ":") + extensionInfo.getVersion()) + "' extends a non-existent service: '") + parent) + "'";
            addError(error);
            extensionInfo.addError(error);
        } else if (baseService.isValid()) {
            service.resolveExplicit(baseService, allStacks, commonServices, extensions);
        } else {
            setValid(false);
            extensionInfo.setValid(false);
            addErrors(baseService.getErrors());
            extensionInfo.addErrors(baseService.getErrors());
        }
    }

    private void populateExtensionInfo() {
        extensionInfo.setName(extensionDirectory.getExtensionDirName());
        extensionInfo.setVersion(extensionDirectory.getName());
        id = java.lang.String.format("%s:%s", extensionInfo.getName(), extensionInfo.getVersion());
        org.apache.ambari.server.stack.ExtensionModule.LOG.debug("Adding new extension to known extensions, extensionName = {}, extensionVersion = {}", extensionInfo.getName(), extensionInfo.getVersion());
        org.apache.ambari.server.state.stack.ExtensionMetainfoXml emx = extensionDirectory.getMetaInfoFile();
        if (emx != null) {
            if (!emx.isValid()) {
                extensionInfo.setValid(false);
                extensionInfo.addErrors(emx.getErrors());
            }
            extensionInfo.setParentExtensionVersion(emx.getExtends());
            extensionInfo.setStacks(emx.getStacks());
            extensionInfo.setExtensions(emx.getExtensions());
            extensionInfo.setActive(emx.getVersion().isActive());
            extensionInfo.setAutoLink(emx.isAutoLink());
        }
        try {
            populateServices();
            if (!extensionInfo.isValid()) {
                setValid(false);
                addErrors(extensionInfo.getErrors());
            }
        } catch (java.lang.Exception e) {
            java.lang.String error = (("Exception caught while populating services for extension: " + extensionInfo.getName()) + "-") + extensionInfo.getVersion();
            setValid(false);
            extensionInfo.setValid(false);
            addError(error);
            extensionInfo.addError(error);
            org.apache.ambari.server.stack.ExtensionModule.LOG.error(error);
        }
    }

    private void populateServices() throws org.apache.ambari.server.AmbariException {
        for (org.apache.ambari.server.stack.ServiceDirectory serviceDir : extensionDirectory.getServiceDirectories()) {
            populateService(serviceDir);
        }
    }

    private void populateService(org.apache.ambari.server.stack.ServiceDirectory serviceDirectory) {
        java.util.Collection<org.apache.ambari.server.stack.ServiceModule> serviceModules = new java.util.ArrayList<>();
        org.apache.ambari.server.state.stack.ServiceMetainfoXml metaInfoXml = serviceDirectory.getMetaInfoFile();
        if (!metaInfoXml.isValid()) {
            extensionInfo.setValid(metaInfoXml.isValid());
            setValid(metaInfoXml.isValid());
            extensionInfo.addErrors(metaInfoXml.getErrors());
            addErrors(metaInfoXml.getErrors());
            return;
        }
        java.util.List<org.apache.ambari.server.state.ServiceInfo> serviceInfos = metaInfoXml.getServices();
        for (org.apache.ambari.server.state.ServiceInfo serviceInfo : serviceInfos) {
            org.apache.ambari.server.stack.ServiceModule serviceModule = new org.apache.ambari.server.stack.ServiceModule(stackContext, serviceInfo, serviceDirectory);
            serviceModules.add(serviceModule);
            if (!serviceModule.isValid()) {
                extensionInfo.setValid(false);
                setValid(false);
                extensionInfo.addErrors(serviceModule.getErrors());
                addErrors(serviceModule.getErrors());
            }
        }
        addServices(serviceModules);
    }

    private void resolveExtension(org.apache.ambari.server.stack.ExtensionModule parentExtension, java.util.Map<java.lang.String, org.apache.ambari.server.stack.StackModule> allStacks, java.util.Map<java.lang.String, org.apache.ambari.server.stack.ServiceModule> commonServices, java.util.Map<java.lang.String, org.apache.ambari.server.stack.ExtensionModule> extensions) throws org.apache.ambari.server.AmbariException {
        if (parentExtension.getModuleState() == org.apache.ambari.server.stack.ModuleState.INIT) {
            parentExtension.resolve(null, allStacks, commonServices, extensions);
        } else if (parentExtension.getModuleState() == org.apache.ambari.server.stack.ModuleState.VISITED) {
            throw new org.apache.ambari.server.AmbariException("Cycle detected while parsing extension definition");
        }
        if ((!parentExtension.isValid()) || ((parentExtension.getModuleInfo() != null) && (!parentExtension.getModuleInfo().isValid()))) {
            setValid(parentExtension.isValid());
            extensionInfo.setValid(parentExtension.extensionInfo.isValid());
            addErrors(parentExtension.getErrors());
            extensionInfo.addErrors(parentExtension.getErrors());
        }
    }

    private void addService(org.apache.ambari.server.stack.ServiceModule service) {
        org.apache.ambari.server.state.ServiceInfo serviceInfo = service.getModuleInfo();
        java.lang.Object previousValue = serviceModules.put(service.getId(), service);
        if (previousValue == null) {
            extensionInfo.getServices().add(serviceInfo);
        }
    }

    private void addServices(java.util.Collection<org.apache.ambari.server.stack.ServiceModule> services) {
        for (org.apache.ambari.server.stack.ServiceModule service : services) {
            addService(service);
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
    public java.util.Collection<java.lang.String> getErrors() {
        return errorSet;
    }

    @java.lang.Override
    public void addError(java.lang.String error) {
        errorSet.add(error);
    }

    @java.lang.Override
    public void addErrors(java.util.Collection<java.lang.String> errors) {
        this.errorSet.addAll(errors);
    }
}