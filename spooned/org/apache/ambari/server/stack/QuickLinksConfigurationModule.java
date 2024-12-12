package org.apache.ambari.server.stack;
import org.codehaus.jackson.map.ObjectMapper;
public class QuickLinksConfigurationModule extends org.apache.ambari.server.stack.BaseModule<org.apache.ambari.server.stack.QuickLinksConfigurationModule, org.apache.ambari.server.state.QuickLinksConfigurationInfo> implements org.apache.ambari.server.stack.Validable {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.stack.QuickLinksConfigurationModule.class);

    private static final org.codehaus.jackson.map.ObjectMapper mapper = new org.codehaus.jackson.map.ObjectMapper();

    public static final java.lang.String QUICKLINKS_CONFIGURATION_KEY = "QuickLinksConfiguration";

    private org.apache.ambari.server.state.QuickLinksConfigurationInfo moduleInfo;

    private boolean valid = true;

    private java.util.Set<java.lang.String> errors = new java.util.HashSet<>();

    public QuickLinksConfigurationModule(java.io.File quickLinksConfigurationFile) {
        this(quickLinksConfigurationFile, new org.apache.ambari.server.state.QuickLinksConfigurationInfo());
    }

    public QuickLinksConfigurationModule(java.io.File quickLinksConfigurationFile, org.apache.ambari.server.state.QuickLinksConfigurationInfo moduleInfo) {
        this.moduleInfo = moduleInfo;
        if ((!moduleInfo.isDeleted()) && (quickLinksConfigurationFile != null)) {
            org.apache.ambari.server.stack.QuickLinksConfigurationModule.LOG.debug("Looking for quicklinks in {}", quickLinksConfigurationFile.getAbsolutePath());
            java.io.FileReader reader = null;
            try {
                reader = new java.io.FileReader(quickLinksConfigurationFile);
            } catch (java.io.FileNotFoundException e) {
                org.apache.ambari.server.stack.QuickLinksConfigurationModule.LOG.error("Quick links file not found");
            }
            try {
                org.apache.ambari.server.state.quicklinks.QuickLinks quickLinksConfig = org.apache.ambari.server.stack.QuickLinksConfigurationModule.mapper.readValue(reader, org.apache.ambari.server.state.quicklinks.QuickLinks.class);
                java.util.Map<java.lang.String, org.apache.ambari.server.state.quicklinks.QuickLinks> map = new java.util.HashMap<>();
                map.put(org.apache.ambari.server.stack.QuickLinksConfigurationModule.QUICKLINKS_CONFIGURATION_KEY, quickLinksConfig);
                moduleInfo.setQuickLinksConfigurationMap(map);
                org.apache.ambari.server.stack.QuickLinksConfigurationModule.LOG.debug("Loaded quicklinks configuration: {}", moduleInfo);
            } catch (java.io.IOException e) {
                java.lang.String errorMessage = java.lang.String.format("Unable to parse quicklinks configuration file %s", quickLinksConfigurationFile.getAbsolutePath());
                org.apache.ambari.server.stack.QuickLinksConfigurationModule.LOG.error(errorMessage, e);
                setValid(false);
                addError(errorMessage);
            }
        }
    }

    public QuickLinksConfigurationModule(org.apache.ambari.server.state.QuickLinksConfigurationInfo moduleInfo) {
        this.moduleInfo = moduleInfo;
    }

    @java.lang.Override
    public void resolve(org.apache.ambari.server.stack.QuickLinksConfigurationModule parent, java.util.Map<java.lang.String, org.apache.ambari.server.stack.StackModule> allStacks, java.util.Map<java.lang.String, org.apache.ambari.server.stack.ServiceModule> commonServices, java.util.Map<java.lang.String, org.apache.ambari.server.stack.ExtensionModule> allExtensions) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.QuickLinksConfigurationInfo parentModuleInfo = parent.getModuleInfo();
        if ((parent.getModuleInfo() != null) && (!moduleInfo.isDeleted())) {
            if ((moduleInfo.getQuickLinksConfigurationMap() == null) || moduleInfo.getQuickLinksConfigurationMap().isEmpty()) {
                moduleInfo.setQuickLinksConfigurationMap(parentModuleInfo.getQuickLinksConfigurationMap());
            } else if ((parentModuleInfo.getQuickLinksConfigurationMap() != null) && (!parentModuleInfo.getQuickLinksConfigurationMap().isEmpty())) {
                org.apache.ambari.server.state.quicklinks.QuickLinks child = moduleInfo.getQuickLinksConfigurationMap().get(org.apache.ambari.server.stack.QuickLinksConfigurationModule.QUICKLINKS_CONFIGURATION_KEY);
                org.apache.ambari.server.state.quicklinks.QuickLinks parentConfig = parentModuleInfo.getQuickLinksConfigurationMap().get(org.apache.ambari.server.stack.QuickLinksConfigurationModule.QUICKLINKS_CONFIGURATION_KEY);
                child.mergeWithParent(parentConfig);
            }
        }
    }

    @java.lang.Override
    public org.apache.ambari.server.state.QuickLinksConfigurationInfo getModuleInfo() {
        return moduleInfo;
    }

    @java.lang.Override
    public boolean isDeleted() {
        return false;
    }

    @java.lang.Override
    public java.lang.String getId() {
        return moduleInfo.getFileName();
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
        errors.add(error);
    }

    @java.lang.Override
    public void addErrors(java.util.Collection<java.lang.String> errors) {
        this.errors.addAll(errors);
    }

    @java.lang.Override
    public java.util.Collection<java.lang.String> getErrors() {
        return errors;
    }
}