package org.apache.ambari.server.stack;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import javax.annotation.Nullable;
public class StackManager {
    public static final java.lang.String PROPERTY_SCHEMA_PATH = "configuration-schema.xsd";

    public static final java.lang.String PATH_DELIMITER = "/";

    public static final java.lang.String COMMON_SERVICES = "common-services";

    public static final java.lang.String EXTENSIONS = "extensions";

    public static final java.lang.String METAINFO_FILE_NAME = "metainfo.xml";

    private org.apache.ambari.server.stack.StackContext stackContext;

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.stack.StackManager.class);

    protected java.util.NavigableMap<java.lang.String, org.apache.ambari.server.state.StackInfo> stackMap = new java.util.TreeMap<>();

    protected java.util.Map<java.lang.String, org.apache.ambari.server.stack.ServiceModule> commonServiceModules;

    protected java.util.Map<java.lang.String, org.apache.ambari.server.stack.StackModule> stackModules;

    protected java.util.Map<java.lang.String, org.apache.ambari.server.stack.ExtensionModule> extensionModules;

    private java.util.Map<java.lang.String, org.apache.ambari.server.state.ExtensionInfo> extensionMap = new java.util.HashMap<>();

    private org.apache.ambari.server.controller.AmbariManagementHelper helper;

    @com.google.inject.assistedinject.AssistedInject
    public StackManager(@com.google.inject.assistedinject.Assisted("stackRoot")
    java.io.File stackRoot, @com.google.inject.assistedinject.Assisted("commonServicesRoot")
    @javax.annotation.Nullable
    java.io.File commonServicesRoot, @com.google.inject.assistedinject.Assisted("extensionRoot")
    @javax.annotation.Nullable
    java.io.File extensionRoot, @com.google.inject.assistedinject.Assisted
    org.apache.ambari.server.state.stack.OsFamily osFamily, @com.google.inject.assistedinject.Assisted
    boolean validate, org.apache.ambari.server.orm.dao.MetainfoDAO metaInfoDAO, org.apache.ambari.server.metadata.ActionMetadata actionMetadata, org.apache.ambari.server.orm.dao.StackDAO stackDao, org.apache.ambari.server.orm.dao.ExtensionDAO extensionDao, org.apache.ambari.server.orm.dao.ExtensionLinkDAO linkDao, org.apache.ambari.server.controller.AmbariManagementHelper helper) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.stack.StackManager.LOG.info("Initializing the stack manager...");
        if (validate) {
            validateStackDirectory(stackRoot);
            validateCommonServicesDirectory(commonServicesRoot);
            validateExtensionDirectory(extensionRoot);
        }
        stackMap = new java.util.TreeMap<>();
        stackContext = new org.apache.ambari.server.stack.StackContext(metaInfoDAO, actionMetadata, osFamily);
        extensionMap = new java.util.HashMap<>();
        this.helper = helper;
        parseDirectories(stackRoot, commonServicesRoot, extensionRoot);
        for (org.apache.ambari.server.stack.StackModule module : stackModules.values()) {
            org.apache.ambari.server.state.StackInfo stack = module.getModuleInfo();
            java.util.List<org.apache.ambari.server.orm.entities.ExtensionLinkEntity> entities = linkDao.findByStack(stack.getName(), stack.getVersion());
            for (org.apache.ambari.server.orm.entities.ExtensionLinkEntity entity : entities) {
                java.lang.String name = entity.getExtension().getExtensionName();
                java.lang.String version = entity.getExtension().getExtensionVersion();
                java.lang.String key = (name + org.apache.ambari.server.stack.StackManager.PATH_DELIMITER) + version;
                org.apache.ambari.server.stack.ExtensionModule extensionModule = extensionModules.get(key);
                if (extensionModule != null) {
                    org.apache.ambari.server.stack.StackManager.LOG.info((((((("Adding extension to stack/version: " + stack.getName()) + "/") + stack.getVersion()) + " extension/version: ") + name) + "/") + version);
                    module.getExtensionModules().put(key, extensionModule);
                }
            }
        }
        fullyResolveCommonServices(stackModules, commonServiceModules, extensionModules);
        fullyResolveExtensions(stackModules, commonServiceModules, extensionModules);
        fullyResolveStacks(stackModules, commonServiceModules, extensionModules);
        populateDB(stackDao, extensionDao);
    }

    protected void parseDirectories(java.io.File stackRoot, java.io.File commonServicesRoot, java.io.File extensionRoot) throws org.apache.ambari.server.AmbariException {
        commonServiceModules = parseCommonServicesDirectory(commonServicesRoot);
        stackModules = parseStackDirectory(stackRoot);
        org.apache.ambari.server.stack.StackManager.LOG.info("About to parse extension directories");
        extensionModules = parseExtensionDirectory(extensionRoot);
    }

    private void populateDB(org.apache.ambari.server.orm.dao.StackDAO stackDao, org.apache.ambari.server.orm.dao.ExtensionDAO extensionDao) throws org.apache.ambari.server.AmbariException {
        java.util.Collection<org.apache.ambari.server.state.StackInfo> stacks = getStacks();
        for (org.apache.ambari.server.state.StackInfo stack : stacks) {
            java.lang.String stackName = stack.getName();
            java.lang.String stackVersion = stack.getVersion();
            if (stackDao.find(stackName, stackVersion) == null) {
                org.apache.ambari.server.stack.StackManager.LOG.info("Adding stack {}-{} to the database", stackName, stackVersion);
                org.apache.ambari.server.orm.entities.StackEntity stackEntity = new org.apache.ambari.server.orm.entities.StackEntity();
                stackEntity.setStackName(stackName);
                stackEntity.setStackVersion(stackVersion);
                stackDao.create(stackEntity);
            }
        }
        java.util.Collection<org.apache.ambari.server.state.ExtensionInfo> extensions = getExtensions();
        for (org.apache.ambari.server.state.ExtensionInfo extension : extensions) {
            java.lang.String extensionName = extension.getName();
            java.lang.String extensionVersion = extension.getVersion();
            if (extensionDao.find(extensionName, extensionVersion) == null) {
                org.apache.ambari.server.stack.StackManager.LOG.info("Adding extension {}-{} to the database", extensionName, extensionVersion);
                org.apache.ambari.server.orm.entities.ExtensionEntity extensionEntity = new org.apache.ambari.server.orm.entities.ExtensionEntity();
                extensionEntity.setExtensionName(extensionName);
                extensionEntity.setExtensionVersion(extensionVersion);
                extensionDao.create(extensionEntity);
            }
        }
        createLinks();
    }

    private void createLinks() {
        org.apache.ambari.server.stack.StackManager.LOG.info("Creating links");
        java.util.Collection<org.apache.ambari.server.state.ExtensionInfo> extensions = getExtensions();
        java.util.Set<java.lang.String> names = new java.util.HashSet<>();
        for (org.apache.ambari.server.state.ExtensionInfo extension : extensions) {
            names.add(extension.getName());
        }
        for (java.lang.String name : names) {
            createLinksForExtension(name);
        }
    }

    private void createLinksForExtension(java.lang.String name) {
        java.util.Collection<org.apache.ambari.server.state.ExtensionInfo> collection = getExtensions(name);
        java.util.List<org.apache.ambari.server.state.ExtensionInfo> extensions = new java.util.ArrayList<>(collection.size());
        extensions.addAll(collection);
        try {
            helper.createExtensionLinks(this, extensions);
        } catch (org.apache.ambari.server.AmbariException e) {
            java.lang.String msg = java.lang.String.format("Failed to create link for extension: %s with exception: %s", name, e.getMessage());
            org.apache.ambari.server.stack.StackManager.LOG.error(msg);
        }
    }

    public org.apache.ambari.server.state.StackInfo getStack(java.lang.String name, java.lang.String version) {
        return stackMap.get((name + org.apache.ambari.server.stack.StackManager.PATH_DELIMITER) + version);
    }

    public java.util.Collection<org.apache.ambari.server.state.StackInfo> getStacks(java.lang.String name) {
        java.util.Collection<org.apache.ambari.server.state.StackInfo> stacks = new java.util.HashSet<>();
        for (org.apache.ambari.server.state.StackInfo stack : stackMap.values()) {
            if (stack.getName().equals(name)) {
                stacks.add(stack);
            }
        }
        return stacks;
    }

    public java.util.Map<java.lang.String, java.util.List<org.apache.ambari.server.state.StackInfo>> getStacksByName() {
        java.util.Map<java.lang.String, java.util.List<org.apache.ambari.server.state.StackInfo>> stacks = new java.util.HashMap<>();
        for (org.apache.ambari.server.state.StackInfo stack : stackMap.values()) {
            java.util.List<org.apache.ambari.server.state.StackInfo> list = stacks.get(stack.getName());
            if (list == null) {
                list = new java.util.ArrayList<>();
                stacks.put(stack.getName(), list);
            }
            list.add(stack);
        }
        return stacks;
    }

    public java.util.Collection<org.apache.ambari.server.state.StackInfo> getStacks() {
        return stackMap.values();
    }

    public org.apache.ambari.server.state.ExtensionInfo getExtension(java.lang.String name, java.lang.String version) {
        return extensionMap.get((name + org.apache.ambari.server.stack.StackManager.PATH_DELIMITER) + version);
    }

    public java.util.Collection<org.apache.ambari.server.state.ExtensionInfo> getExtensions(java.lang.String name) {
        java.util.Collection<org.apache.ambari.server.state.ExtensionInfo> extensions = new java.util.HashSet<>();
        for (org.apache.ambari.server.state.ExtensionInfo extension : extensionMap.values()) {
            if (extension.getName().equals(name)) {
                extensions.add(extension);
            }
        }
        return extensions;
    }

    public java.util.Collection<org.apache.ambari.server.state.ExtensionInfo> getExtensions() {
        return extensionMap.values();
    }

    public boolean haveAllRepoUrlsBeenResolved() {
        return stackContext.haveAllRepoTasksCompleted();
    }

    private void fullyResolveStacks(java.util.Map<java.lang.String, org.apache.ambari.server.stack.StackModule> stackModules, java.util.Map<java.lang.String, org.apache.ambari.server.stack.ServiceModule> commonServiceModules, java.util.Map<java.lang.String, org.apache.ambari.server.stack.ExtensionModule> extensions) throws org.apache.ambari.server.AmbariException {
        for (org.apache.ambari.server.stack.StackModule stack : stackModules.values()) {
            if (stack.getModuleState() == org.apache.ambari.server.stack.ModuleState.INIT) {
                stack.resolve(null, stackModules, commonServiceModules, extensions);
            }
        }
        for (org.apache.ambari.server.stack.ServiceModule commonService : commonServiceModules.values()) {
            commonService.finalizeModule();
        }
        for (org.apache.ambari.server.stack.ExtensionModule extension : extensions.values()) {
            extension.finalizeModule();
        }
        for (org.apache.ambari.server.stack.StackModule stack : stackModules.values()) {
            stack.finalizeModule();
        }
        stackContext.executeRepoTasks();
    }

    private void fullyResolveCommonServices(java.util.Map<java.lang.String, org.apache.ambari.server.stack.StackModule> stackModules, java.util.Map<java.lang.String, org.apache.ambari.server.stack.ServiceModule> commonServiceModules, java.util.Map<java.lang.String, org.apache.ambari.server.stack.ExtensionModule> extensions) throws org.apache.ambari.server.AmbariException {
        for (org.apache.ambari.server.stack.ServiceModule commonService : commonServiceModules.values()) {
            if (commonService.getModuleState() == org.apache.ambari.server.stack.ModuleState.INIT) {
                commonService.resolveCommonService(stackModules, commonServiceModules, extensions);
            }
        }
    }

    private void fullyResolveExtensions(java.util.Map<java.lang.String, org.apache.ambari.server.stack.StackModule> stackModules, java.util.Map<java.lang.String, org.apache.ambari.server.stack.ServiceModule> commonServiceModules, java.util.Map<java.lang.String, org.apache.ambari.server.stack.ExtensionModule> extensionModules) throws org.apache.ambari.server.AmbariException {
        for (org.apache.ambari.server.stack.ExtensionModule extensionModule : extensionModules.values()) {
            if (extensionModule.getModuleState() == org.apache.ambari.server.stack.ModuleState.INIT) {
                extensionModule.resolve(null, stackModules, commonServiceModules, extensionModules);
            }
        }
    }

    private void validateCommonServicesDirectory(java.io.File commonServicesRoot) throws org.apache.ambari.server.AmbariException {
        if (commonServicesRoot != null) {
            org.apache.ambari.server.stack.StackManager.LOG.info("Validating common services directory {} ...", commonServicesRoot);
            java.lang.String commonServicesRootAbsolutePath = commonServicesRoot.getAbsolutePath();
            if (org.apache.ambari.server.stack.StackManager.LOG.isDebugEnabled()) {
                org.apache.ambari.server.stack.StackManager.LOG.debug("Loading common services information, commonServicesRoot = {}", commonServicesRootAbsolutePath);
            }
            if ((!commonServicesRoot.isDirectory()) && (!commonServicesRoot.exists())) {
                throw new org.apache.ambari.server.AmbariException(((("" + org.apache.ambari.server.configuration.Configuration.COMMON_SERVICES_DIR_PATH) + " should be a directory with common services") + ", commonServicesRoot = ") + commonServicesRootAbsolutePath);
            }
        }
    }

    private void validateStackDirectory(java.io.File stackRoot) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.stack.StackManager.LOG.info("Validating stack directory {} ...", stackRoot);
        java.lang.String stackRootAbsPath = stackRoot.getAbsolutePath();
        if (org.apache.ambari.server.stack.StackManager.LOG.isDebugEnabled()) {
            org.apache.ambari.server.stack.StackManager.LOG.debug("Loading stack information, stackRoot = {}", stackRootAbsPath);
        }
        if ((!stackRoot.isDirectory()) && (!stackRoot.exists())) {
            throw new org.apache.ambari.server.AmbariException(((("" + org.apache.ambari.server.configuration.Configuration.METADATA_DIR_PATH) + " should be a directory with stack") + ", stackRoot = ") + stackRootAbsPath);
        }
        javax.xml.validation.Validator validator = org.apache.ambari.server.stack.StackManager.getPropertySchemaValidator();
        org.apache.ambari.server.stack.StackManager.validateAllPropertyXmlsInFolderRecursively(stackRoot, validator);
    }

    public static javax.xml.validation.Validator getPropertySchemaValidator() throws org.apache.ambari.server.AmbariException {
        javax.xml.validation.SchemaFactory factory = javax.xml.validation.SchemaFactory.newInstance(javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI);
        javax.xml.validation.Schema schema;
        java.lang.ClassLoader classLoader = org.apache.ambari.server.stack.StackManager.class.getClassLoader();
        try {
            schema = factory.newSchema(classLoader.getResource(org.apache.ambari.server.stack.StackManager.PROPERTY_SCHEMA_PATH));
        } catch (org.xml.sax.SAXException e) {
            throw new org.apache.ambari.server.AmbariException(java.lang.String.format("Failed to parse property schema file %s", org.apache.ambari.server.stack.StackManager.PROPERTY_SCHEMA_PATH), e);
        }
        return schema.newValidator();
    }

    public static void validateAllPropertyXmlsInFolderRecursively(java.io.File stackRoot, javax.xml.validation.Validator validator) throws org.apache.ambari.server.AmbariException {
        java.util.Collection<java.io.File> files = org.apache.commons.io.FileUtils.listFiles(stackRoot, new java.lang.String[]{ "xml" }, true);
        for (java.io.File file : files) {
            try {
                if (file.getParentFile().getName().contains("configuration")) {
                    validator.validate(new javax.xml.transform.stream.StreamSource(file));
                }
            } catch (java.lang.Exception e) {
                java.lang.String msg = java.lang.String.format("File %s didn't pass the validation. Error message is : %s", file.getAbsolutePath(), e.getMessage());
                org.apache.ambari.server.stack.StackManager.LOG.error(msg);
                throw new org.apache.ambari.server.AmbariException(msg);
            }
        }
    }

    private void validateExtensionDirectory(java.io.File extensionRoot) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.stack.StackManager.LOG.info("Validating extension directory {} ...", extensionRoot);
        if (extensionRoot == null) {
            return;
        }
        java.lang.String extensionRootAbsPath = extensionRoot.getAbsolutePath();
        if (org.apache.ambari.server.stack.StackManager.LOG.isDebugEnabled()) {
            org.apache.ambari.server.stack.StackManager.LOG.debug("Loading extension information, extensionRoot = {}", extensionRootAbsPath);
        }
        if (extensionRoot.exists() && (!extensionRoot.isDirectory())) {
            throw new org.apache.ambari.server.AmbariException(((("" + org.apache.ambari.server.configuration.Configuration.METADATA_DIR_PATH) + " should be a directory") + ", extensionRoot = ") + extensionRootAbsPath);
        }
    }

    private java.util.Map<java.lang.String, org.apache.ambari.server.stack.ServiceModule> parseCommonServicesDirectory(java.io.File commonServicesRoot) throws org.apache.ambari.server.AmbariException {
        java.util.Map<java.lang.String, org.apache.ambari.server.stack.ServiceModule> commonServiceModules = new java.util.HashMap<>();
        if (commonServicesRoot != null) {
            java.io.File[] commonServiceFiles = commonServicesRoot.listFiles(org.apache.ambari.server.stack.StackDirectory.FILENAME_FILTER);
            for (java.io.File commonService : commonServiceFiles) {
                if (commonService.isFile()) {
                    continue;
                }
                for (java.io.File serviceFolder : commonService.listFiles(org.apache.ambari.server.stack.StackDirectory.FILENAME_FILTER)) {
                    org.apache.ambari.server.stack.ServiceDirectory serviceDirectory = new org.apache.ambari.server.stack.CommonServiceDirectory(serviceFolder.getPath());
                    org.apache.ambari.server.state.stack.ServiceMetainfoXml metaInfoXml = serviceDirectory.getMetaInfoFile();
                    if (metaInfoXml != null) {
                        if (metaInfoXml.isValid()) {
                            for (org.apache.ambari.server.state.ServiceInfo serviceInfo : metaInfoXml.getServices()) {
                                org.apache.ambari.server.stack.ServiceModule serviceModule = new org.apache.ambari.server.stack.ServiceModule(stackContext, serviceInfo, serviceDirectory, true);
                                java.lang.String commonServiceKey = (serviceInfo.getName() + org.apache.ambari.server.stack.StackManager.PATH_DELIMITER) + serviceInfo.getVersion();
                                commonServiceModules.put(commonServiceKey, serviceModule);
                            }
                        } else {
                            org.apache.ambari.server.stack.ServiceModule serviceModule = new org.apache.ambari.server.stack.ServiceModule(stackContext, new org.apache.ambari.server.state.ServiceInfo(), serviceDirectory, true);
                            serviceModule.setValid(false);
                            serviceModule.addErrors(metaInfoXml.getErrors());
                            commonServiceModules.put(metaInfoXml.getSchemaVersion(), serviceModule);
                            metaInfoXml.setSchemaVersion(null);
                        }
                    }
                }
            }
        }
        return commonServiceModules;
    }

    private java.util.Map<java.lang.String, org.apache.ambari.server.stack.StackModule> parseStackDirectory(java.io.File stackRoot) throws org.apache.ambari.server.AmbariException {
        java.util.Map<java.lang.String, org.apache.ambari.server.stack.StackModule> stackModules = new java.util.HashMap<>();
        java.io.File[] stackFiles = stackRoot.listFiles(org.apache.ambari.server.stack.StackDirectory.FILENAME_FILTER);
        for (java.io.File stack : stackFiles) {
            if (stack.isFile()) {
                continue;
            }
            for (java.io.File stackFolder : stack.listFiles(org.apache.ambari.server.stack.StackDirectory.FILENAME_FILTER)) {
                if (stackFolder.isFile()) {
                    continue;
                }
                java.lang.String stackName = stackFolder.getParentFile().getName();
                java.lang.String stackVersion = stackFolder.getName();
                org.apache.ambari.server.stack.StackModule stackModule = new org.apache.ambari.server.stack.StackModule(new org.apache.ambari.server.stack.StackDirectory(stackFolder.getPath()), stackContext);
                java.lang.String stackKey = (stackName + org.apache.ambari.server.stack.StackManager.PATH_DELIMITER) + stackVersion;
                stackModules.put(stackKey, stackModule);
                stackMap.put(stackKey, stackModule.getModuleInfo());
            }
        }
        if (stackMap.isEmpty()) {
            throw new org.apache.ambari.server.AmbariException(("Unable to find stack definitions under " + "stackRoot = ") + stackRoot.getAbsolutePath());
        }
        return stackModules;
    }

    public void linkStackToExtension(org.apache.ambari.server.state.StackInfo stack, org.apache.ambari.server.state.ExtensionInfo extension) throws org.apache.ambari.server.AmbariException {
        stack.addExtension(extension);
    }

    public void unlinkStackAndExtension(org.apache.ambari.server.state.StackInfo stack, org.apache.ambari.server.state.ExtensionInfo extension) throws org.apache.ambari.server.AmbariException {
        stack.removeExtension(extension);
    }

    private java.util.Map<java.lang.String, org.apache.ambari.server.stack.ExtensionModule> parseExtensionDirectory(java.io.File extensionRoot) throws org.apache.ambari.server.AmbariException {
        java.util.Map<java.lang.String, org.apache.ambari.server.stack.ExtensionModule> extensionModules = new java.util.HashMap<>();
        if ((extensionRoot == null) || (!extensionRoot.exists())) {
            return extensionModules;
        }
        java.io.File[] extensionFiles = extensionRoot.listFiles(org.apache.ambari.server.stack.StackDirectory.FILENAME_FILTER);
        for (java.io.File extensionNameFolder : extensionFiles) {
            if (extensionNameFolder.isFile()) {
                continue;
            }
            for (java.io.File extensionVersionFolder : extensionNameFolder.listFiles(org.apache.ambari.server.stack.StackDirectory.FILENAME_FILTER)) {
                if (extensionVersionFolder.isFile()) {
                    continue;
                }
                java.lang.String extensionName = extensionNameFolder.getName();
                java.lang.String extensionVersion = extensionVersionFolder.getName();
                org.apache.ambari.server.stack.ExtensionModule extensionModule = new org.apache.ambari.server.stack.ExtensionModule(new org.apache.ambari.server.stack.ExtensionDirectory(extensionVersionFolder.getPath()), stackContext);
                java.lang.String extensionKey = (extensionName + org.apache.ambari.server.stack.StackManager.PATH_DELIMITER) + extensionVersion;
                extensionModules.put(extensionKey, extensionModule);
                extensionMap.put(extensionKey, extensionModule.getModuleInfo());
            }
        }
        if (stackMap.isEmpty()) {
            throw new org.apache.ambari.server.AmbariException(("Unable to find extension definitions under " + "extensionRoot = ") + extensionRoot.getAbsolutePath());
        }
        return extensionModules;
    }

    public void removeStack(org.apache.ambari.server.orm.entities.StackEntity stackEntity) {
        java.lang.String stackKey = (stackEntity.getStackName() + org.apache.ambari.server.stack.StackManager.PATH_DELIMITER) + stackEntity.getStackVersion();
        stackMap.remove(stackKey);
    }
}