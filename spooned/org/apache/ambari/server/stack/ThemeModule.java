package org.apache.ambari.server.stack;
import org.codehaus.jackson.map.ObjectMapper;
public class ThemeModule extends org.apache.ambari.server.stack.BaseModule<org.apache.ambari.server.stack.ThemeModule, org.apache.ambari.server.state.ThemeInfo> implements org.apache.ambari.server.stack.Validable {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.stack.ThemeModule.class);

    private static final org.codehaus.jackson.map.ObjectMapper mapper = new org.codehaus.jackson.map.ObjectMapper();

    public static final java.lang.String THEME_KEY = "Theme";

    private org.apache.ambari.server.state.ThemeInfo moduleInfo;

    private boolean valid = true;

    private java.util.Set<java.lang.String> errors = new java.util.HashSet<>();

    public ThemeModule(java.io.File themeFile) {
        this(themeFile, new org.apache.ambari.server.state.ThemeInfo());
    }

    public ThemeModule(java.io.File themeFile, org.apache.ambari.server.state.ThemeInfo moduleInfo) {
        this.moduleInfo = moduleInfo;
        if ((!moduleInfo.isDeleted()) && (themeFile != null)) {
            org.apache.ambari.server.stack.ThemeModule.LOG.debug("Looking for theme in {}", themeFile.getAbsolutePath());
            java.io.FileReader reader = null;
            try {
                reader = new java.io.FileReader(themeFile);
            } catch (java.io.FileNotFoundException e) {
                org.apache.ambari.server.stack.ThemeModule.LOG.error("Theme file not found");
            }
            try {
                org.apache.ambari.server.state.theme.Theme theme = org.apache.ambari.server.stack.ThemeModule.mapper.readValue(reader, org.apache.ambari.server.state.theme.Theme.class);
                java.util.Map<java.lang.String, org.apache.ambari.server.state.theme.Theme> map = new java.util.HashMap<>();
                map.put(org.apache.ambari.server.stack.ThemeModule.THEME_KEY, theme);
                moduleInfo.setThemeMap(map);
                org.apache.ambari.server.stack.ThemeModule.LOG.debug("Loaded theme: {}", moduleInfo);
            } catch (java.io.IOException e) {
                org.apache.ambari.server.stack.ThemeModule.LOG.error("Unable to parse theme file ", e);
                setValid(false);
                addError("Unable to parse theme file " + themeFile);
            }
        }
    }

    public ThemeModule(org.apache.ambari.server.state.ThemeInfo moduleInfo) {
        this.moduleInfo = moduleInfo;
    }

    @java.lang.Override
    public void resolve(org.apache.ambari.server.stack.ThemeModule parent, java.util.Map<java.lang.String, org.apache.ambari.server.stack.StackModule> allStacks, java.util.Map<java.lang.String, org.apache.ambari.server.stack.ServiceModule> commonServices, java.util.Map<java.lang.String, org.apache.ambari.server.stack.ExtensionModule> extensions) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.ThemeInfo parentModuleInfo = parent.getModuleInfo();
        if ((parent.getModuleInfo() != null) && (!moduleInfo.isDeleted())) {
            if ((moduleInfo.getThemeMap() == null) || moduleInfo.getThemeMap().isEmpty()) {
                moduleInfo.setThemeMap(parentModuleInfo.getThemeMap());
            } else if ((parentModuleInfo.getThemeMap() != null) && (!parentModuleInfo.getThemeMap().isEmpty())) {
                org.apache.ambari.server.state.theme.Theme childTheme = moduleInfo.getThemeMap().get(org.apache.ambari.server.stack.ThemeModule.THEME_KEY);
                org.apache.ambari.server.state.theme.Theme parentTheme = parentModuleInfo.getThemeMap().get(org.apache.ambari.server.stack.ThemeModule.THEME_KEY);
                childTheme.mergeWithParent(parentTheme);
            }
        }
    }

    @java.lang.Override
    public org.apache.ambari.server.state.ThemeInfo getModuleInfo() {
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