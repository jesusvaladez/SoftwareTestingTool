package org.apache.ambari.server.stack;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
public abstract class ServiceDirectory extends org.apache.ambari.server.stack.StackDefinitionDirectory {
    private java.util.Map<java.lang.String, java.io.File> metricsFileMap = new java.util.HashMap<>();

    private java.io.File advisorFile;

    private java.io.File alertsFile;

    private java.io.File themeFile;

    private java.io.File kerberosDescriptorFile;

    private java.io.File rcoFile;

    private org.apache.ambari.server.state.stack.StackRoleCommandOrder roleCommandOrder;

    private java.util.Map<java.lang.String, java.io.File> widgetsDescriptorFileMap = new java.util.HashMap<>();

    protected java.lang.String packageDir;

    protected java.io.File upgradesDir;

    protected java.io.File checksDir;

    protected java.io.File serverActionsDir;

    private org.apache.ambari.server.state.stack.ServiceMetainfoXml metaInfoXml;

    public static final java.lang.String SERVICES_FOLDER_NAME = "services";

    protected static final java.lang.String PACKAGE_FOLDER_NAME = "package";

    protected static final java.lang.String UPGRADES_FOLDER_NAME = "upgrades";

    protected static final java.lang.String CHECKS_FOLDER_NAME = "checks";

    protected static final java.lang.String SERVER_ACTIONS_FOLDER_NAME = "server_actions";

    private static final java.lang.String SERVICE_METAINFO_FILE_NAME = "metainfo.xml";

    org.apache.ambari.server.stack.ModuleFileUnmarshaller unmarshaller = new org.apache.ambari.server.stack.ModuleFileUnmarshaller();

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.stack.ServiceDirectory.class);

    public ServiceDirectory(java.lang.String servicePath) throws org.apache.ambari.server.AmbariException {
        super(servicePath);
        parsePath();
    }

    public java.lang.String getPackageDir() {
        return packageDir;
    }

    public java.io.File getUpgradesDir() {
        return upgradesDir;
    }

    public java.io.File getChecksDir() {
        return checksDir;
    }

    public java.io.File getServerActionsDir() {
        return serverActionsDir;
    }

    public java.io.File getMetricsFile(java.lang.String serviceName) {
        return metricsFileMap.get(serviceName);
    }

    public java.io.File getAdvisorFile() {
        return advisorFile;
    }

    public abstract java.lang.String getAdvisorName(java.lang.String serviceName);

    public java.io.File getAlertsFile() {
        return alertsFile;
    }

    public java.io.File getThemeFile() {
        return themeFile;
    }

    public java.io.File getKerberosDescriptorFile() {
        return kerberosDescriptorFile;
    }

    public java.io.File getWidgetsDescriptorFile(java.lang.String serviceName) {
        return widgetsDescriptorFileMap.get(serviceName);
    }

    public org.apache.ambari.server.state.stack.ServiceMetainfoXml getMetaInfoFile() {
        return metaInfoXml;
    }

    public org.apache.ambari.server.state.stack.StackRoleCommandOrder getRoleCommandOrder() {
        return roleCommandOrder;
    }

    protected void parsePath() throws org.apache.ambari.server.AmbariException {
        calculateDirectories(getStack(), getService());
        parseMetaInfoFile();
        java.io.File af = new java.io.File(directory, org.apache.ambari.server.stack.StackDirectory.SERVICE_ALERT_FILE_NAME);
        alertsFile = (af.exists()) ? af : null;
        java.io.File kdf = new java.io.File(directory, org.apache.ambari.server.api.services.AmbariMetaInfo.KERBEROS_DESCRIPTOR_FILE_NAME);
        kerberosDescriptorFile = (kdf.exists()) ? kdf : null;
        java.io.File rco = new java.io.File(directory, org.apache.ambari.server.stack.StackDirectory.RCO_FILE_NAME);
        if (rco.exists()) {
            rcoFile = rco;
            parseRoleCommandOrder();
        }
        if (metaInfoXml.getServices() != null) {
            for (org.apache.ambari.server.state.ServiceInfo serviceInfo : metaInfoXml.getServices()) {
                java.io.File mf = new java.io.File(directory, serviceInfo.getMetricsFileName());
                metricsFileMap.put(serviceInfo.getName(), mf.exists() ? mf : null);
                java.io.File wdf = new java.io.File(directory, serviceInfo.getWidgetsFileName());
                widgetsDescriptorFileMap.put(serviceInfo.getName(), wdf.exists() ? wdf : null);
            }
        }
        java.io.File advFile = new java.io.File(directory, org.apache.ambari.server.stack.StackDirectory.SERVICE_ADVISOR_FILE_NAME);
        advisorFile = (advFile.exists()) ? advFile : null;
        java.io.File themeFile = new java.io.File(directory, org.apache.ambari.server.stack.StackDirectory.SERVICE_THEME_FILE_NAME);
        this.themeFile = (themeFile.exists()) ? themeFile : null;
    }

    public abstract java.lang.String getService();

    public abstract java.lang.String getStack();

    protected void calculateDirectories(java.lang.String stack, java.lang.String service) {
        calculatePackageDirectory(stack, service);
        calculateUpgradesDirectory(stack, service);
        calculateChecksDirectory(stack, service);
        calculateServerActionsDirectory(stack, service);
    }

    protected java.io.File resolveDirectory(java.lang.String directoryName, java.lang.String stack, java.lang.String service) {
        java.io.File directory = new java.io.File((getAbsolutePath() + java.io.File.separator) + directoryName);
        if (directory.isDirectory()) {
            java.lang.String[] files = directory.list();
            int fileCount = files.length;
            if (fileCount > 0) {
                org.apache.ambari.server.stack.ServiceDirectory.LOG.debug("Service {} folder for service {} in {} has been resolved to {}", directoryName, service, stack, directory);
                return directory;
            } else {
                org.apache.ambari.server.stack.ServiceDirectory.LOG.debug("Service folder {} is empty.", directory);
            }
        } else {
            org.apache.ambari.server.stack.ServiceDirectory.LOG.debug("Service folder {}does not exist.", directory);
        }
        return null;
    }

    protected java.lang.String resolveRelativeDirectoryPathString(java.io.File resourcesDir, java.lang.String directoryName, java.lang.String stack, java.lang.String service) {
        java.io.File dir = resolveDirectory(directoryName, stack, service);
        if (dir != null) {
            return dir.getPath().substring(resourcesDir.getPath().length() + 1);
        }
        return null;
    }

    protected abstract java.io.File getResourcesDirectory();

    protected void calculatePackageDirectory(java.lang.String stack, java.lang.String service) {
        packageDir = resolveRelativeDirectoryPathString(getResourcesDirectory(), org.apache.ambari.server.stack.ServiceDirectory.PACKAGE_FOLDER_NAME, stack, service);
    }

    protected void calculateUpgradesDirectory(java.lang.String stack, java.lang.String service) {
        upgradesDir = resolveDirectory(org.apache.ambari.server.stack.ServiceDirectory.UPGRADES_FOLDER_NAME, stack, service);
    }

    protected void calculateChecksDirectory(java.lang.String stack, java.lang.String service) {
        checksDir = resolveDirectory(org.apache.ambari.server.stack.ServiceDirectory.CHECKS_FOLDER_NAME, stack, service);
    }

    protected void calculateServerActionsDirectory(java.lang.String stack, java.lang.String service) {
        serverActionsDir = resolveDirectory(org.apache.ambari.server.stack.ServiceDirectory.SERVER_ACTIONS_FOLDER_NAME, stack, service);
    }

    protected void parseMetaInfoFile() throws org.apache.ambari.server.AmbariException {
        java.io.File f = new java.io.File((getAbsolutePath() + java.io.File.separator) + org.apache.ambari.server.stack.ServiceDirectory.SERVICE_METAINFO_FILE_NAME);
        if (!f.exists()) {
            throw new org.apache.ambari.server.AmbariException(java.lang.String.format("Stack Definition Service at '%s' doesn't contain a metainfo.xml file", f.getAbsolutePath()));
        }
        try {
            metaInfoXml = unmarshaller.unmarshal(org.apache.ambari.server.state.stack.ServiceMetainfoXml.class, f);
        } catch (java.lang.Exception e) {
            metaInfoXml = new org.apache.ambari.server.state.stack.ServiceMetainfoXml();
            metaInfoXml.setValid(false);
            java.lang.String msg = java.lang.String.format("Unable to parse service metainfo.xml file '%s' ", f.getAbsolutePath());
            metaInfoXml.addError(msg);
            org.apache.ambari.server.stack.ServiceDirectory.LOG.warn(msg, e);
            metaInfoXml.setSchemaVersion(getAbsolutePath().replace(f.getParentFile().getParentFile().getParent() + java.io.File.separator, ""));
        }
    }

    private void parseRoleCommandOrder() {
        if (rcoFile == null)
            return;

        try {
            org.codehaus.jackson.map.ObjectMapper mapper = new org.codehaus.jackson.map.ObjectMapper();
            org.codehaus.jackson.type.TypeReference<java.util.Map<java.lang.String, java.lang.Object>> rcoElementTypeReference = new org.codehaus.jackson.type.TypeReference<java.util.Map<java.lang.String, java.lang.Object>>() {};
            java.util.HashMap<java.lang.String, java.lang.Object> result = mapper.readValue(rcoFile, rcoElementTypeReference);
            org.apache.ambari.server.stack.ServiceDirectory.LOG.info("Role command order info was loaded from file: {}", rcoFile.getAbsolutePath());
            roleCommandOrder = new org.apache.ambari.server.state.stack.StackRoleCommandOrder(result);
            if (org.apache.ambari.server.stack.ServiceDirectory.LOG.isDebugEnabled()) {
                org.apache.ambari.server.stack.ServiceDirectory.LOG.debug("Role Command Order for {}", rcoFile.getAbsolutePath());
                roleCommandOrder.printRoleCommandOrder(org.apache.ambari.server.stack.ServiceDirectory.LOG);
            }
        } catch (java.io.IOException e) {
            org.apache.ambari.server.stack.ServiceDirectory.LOG.error(java.lang.String.format("Can not read role command order info %s", rcoFile.getAbsolutePath()), e);
        }
    }
}