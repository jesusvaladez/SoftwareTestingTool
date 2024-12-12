package org.apache.ambari.server.stack;
import javax.annotation.Nullable;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
public class StackDirectory extends org.apache.ambari.server.stack.StackDefinitionDirectory {
    public static final java.lang.String SERVICE_CONFIG_FOLDER_NAME = "configuration";

    public static final java.lang.String SERVICE_PROPERTIES_FOLDER_NAME = "properties";

    public static final java.lang.String SERVICE_THEMES_FOLDER_NAME = "themes";

    public static final java.lang.String SERVICE_QUICKLINKS_CONFIGURATIONS_FOLDER_NAME = "quicklinks";

    public static final java.lang.String SERVICE_CONFIG_FILE_NAME_POSTFIX = ".xml";

    public static final java.lang.String RCO_FILE_NAME = "role_command_order.json";

    public static final java.lang.String SERVICE_METRIC_FILE_NAME = "metrics.json";

    public static final java.lang.String SERVICE_ALERT_FILE_NAME = "alerts.json";

    public static final java.lang.String SERVICE_ADVISOR_FILE_NAME = "service_advisor.py";

    public static final java.lang.String LIB_FOLDER_NAME = "lib";

    public static final java.lang.String KERBEROS_DESCRIPTOR_PRECONFIGURE_FILE_NAME = "kerberos_preconfigure.json";

    public static final java.lang.String SERVICE_THEME_FILE_NAME = "theme.json";

    private java.lang.String upgradesDir;

    private java.lang.String rcoFilePath;

    private java.lang.String libraryDir;

    private java.lang.String kerberosDescriptorPreconfigureFilePath;

    private org.apache.ambari.server.state.stack.RepositoryXml repoFile;

    private org.apache.ambari.server.state.stack.StackRoleCommandOrder roleCommandOrder;

    private java.lang.String repoDir;

    private java.util.Collection<org.apache.ambari.server.stack.ServiceDirectory> serviceDirectories;

    private java.util.Map<java.lang.String, org.apache.ambari.server.stack.upgrade.UpgradePack> upgradePacks;

    private org.apache.ambari.server.stack.upgrade.ConfigUpgradePack configUpgradePack;

    private org.apache.ambari.server.state.stack.StackMetainfoXml metaInfoXml;

    private java.net.URLClassLoader libraryClassLoader;

    org.apache.ambari.server.stack.ModuleFileUnmarshaller unmarshaller = new org.apache.ambari.server.stack.ModuleFileUnmarshaller();

    public static final java.io.FilenameFilter FILENAME_FILTER = new java.io.FilenameFilter() {
        @java.lang.Override
        public boolean accept(java.io.File dir, java.lang.String s) {
            return !(s.equals(".svn") || s.equals(".git"));
        }
    };

    private static final java.lang.String REPOSITORY_FOLDER_NAME = "repos";

    private static final java.lang.String STACK_METAINFO_FILE_NAME = "metainfo.xml";

    private static final java.lang.String UPGRADE_PACK_FOLDER_NAME = "upgrades";

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.stack.StackDirectory.class);

    public StackDirectory(java.lang.String directory) throws org.apache.ambari.server.AmbariException {
        super(directory);
        parsePath();
    }

    public java.lang.String getStackDirName() {
        return getDirectory().getParentFile().getName();
    }

    public java.lang.String getUpgradesDir() {
        return upgradesDir;
    }

    public java.lang.String getRcoFilePath() {
        return rcoFilePath;
    }

    public java.lang.String getLibraryPath() {
        return libraryDir;
    }

    public java.lang.String getKerberosDescriptorPreconfigureFilePath() {
        return kerberosDescriptorPreconfigureFilePath;
    }

    public java.lang.String getRepoDir() {
        return repoDir;
    }

    public org.apache.ambari.server.state.stack.RepositoryXml getRepoFile() {
        return repoFile;
    }

    public org.apache.ambari.server.state.stack.StackMetainfoXml getMetaInfoFile() {
        return metaInfoXml;
    }

    public java.util.Collection<org.apache.ambari.server.stack.ServiceDirectory> getServiceDirectories() {
        return serviceDirectories;
    }

    public java.util.Map<java.lang.String, org.apache.ambari.server.stack.upgrade.UpgradePack> getUpgradePacks() {
        return upgradePacks;
    }

    public org.apache.ambari.server.stack.upgrade.ConfigUpgradePack getConfigUpgradePack() {
        return configUpgradePack;
    }

    public org.apache.ambari.server.state.stack.StackRoleCommandOrder getRoleCommandOrder() {
        return roleCommandOrder;
    }

    @javax.annotation.Nullable
    public java.net.URLClassLoader getLibraryClassLoader() {
        return libraryClassLoader;
    }

    private void parsePath() throws org.apache.ambari.server.AmbariException {
        java.util.Collection<java.lang.String> subDirs = java.util.Arrays.asList(directory.list());
        if (subDirs.contains(org.apache.ambari.server.stack.StackDirectory.RCO_FILE_NAME)) {
            rcoFilePath = (getAbsolutePath() + java.io.File.separator) + org.apache.ambari.server.stack.StackDirectory.RCO_FILE_NAME;
        }
        if (subDirs.contains(org.apache.ambari.server.stack.StackDirectory.LIB_FOLDER_NAME)) {
            libraryDir = (getAbsolutePath() + java.io.File.separator) + org.apache.ambari.server.stack.StackDirectory.LIB_FOLDER_NAME;
        }
        if (subDirs.contains(org.apache.ambari.server.stack.StackDirectory.KERBEROS_DESCRIPTOR_PRECONFIGURE_FILE_NAME)) {
            kerberosDescriptorPreconfigureFilePath = (getAbsolutePath() + java.io.File.separator) + org.apache.ambari.server.stack.StackDirectory.KERBEROS_DESCRIPTOR_PRECONFIGURE_FILE_NAME;
        }
        parseUpgradePacks(subDirs);
        parseServiceDirectories(subDirs);
        parseRepoFile(subDirs);
        parseMetaInfoFile();
        parseRoleCommandOrder();
        parseLibraryClassLoader();
    }

    private void parseRepoFile(java.util.Collection<java.lang.String> subDirs) {
        org.apache.ambari.server.stack.RepositoryFolderAndXml repoDirAndXml = org.apache.ambari.server.stack.RepoUtil.parseRepoFile(directory, subDirs, unmarshaller);
        repoDir = repoDirAndXml.repoDir.orNull();
        repoFile = repoDirAndXml.repoXml.orNull();
        if ((repoFile == null) || (!repoFile.isValid())) {
            org.apache.ambari.server.stack.StackDirectory.LOG.warn(((((((("No repository information defined for " + ", stackName=") + getStackDirName()) + ", stackVersion=") + getPath()) + ", repoFolder=") + getPath()) + java.io.File.separator) + org.apache.ambari.server.stack.StackDirectory.REPOSITORY_FOLDER_NAME);
        }
    }

    private void parseMetaInfoFile() throws org.apache.ambari.server.AmbariException {
        java.io.File stackMetaInfoFile = new java.io.File((getAbsolutePath() + java.io.File.separator) + org.apache.ambari.server.stack.StackDirectory.STACK_METAINFO_FILE_NAME);
        if (stackMetaInfoFile.exists()) {
            if (org.apache.ambari.server.stack.StackDirectory.LOG.isDebugEnabled()) {
                org.apache.ambari.server.stack.StackDirectory.LOG.debug("Reading stack version metainfo from file {}", stackMetaInfoFile.getAbsolutePath());
            }
            try {
                metaInfoXml = unmarshaller.unmarshal(org.apache.ambari.server.state.stack.StackMetainfoXml.class, stackMetaInfoFile);
            } catch (java.lang.Exception e) {
                metaInfoXml = new org.apache.ambari.server.state.stack.StackMetainfoXml();
                metaInfoXml.setValid(false);
                java.lang.String msg = "Unable to parse stack metainfo.xml file at location: " + stackMetaInfoFile.getAbsolutePath();
                metaInfoXml.addError(msg);
                org.apache.ambari.server.stack.StackDirectory.LOG.warn(msg);
            }
        }
    }

    private void parseServiceDirectories(java.util.Collection<java.lang.String> subDirs) throws org.apache.ambari.server.AmbariException {
        java.util.Collection<org.apache.ambari.server.stack.ServiceDirectory> dirs = new java.util.HashSet<>();
        if (subDirs.contains(org.apache.ambari.server.stack.ServiceDirectory.SERVICES_FOLDER_NAME)) {
            java.lang.String servicesDir = (getAbsolutePath() + java.io.File.separator) + org.apache.ambari.server.stack.ServiceDirectory.SERVICES_FOLDER_NAME;
            java.io.File baseServiceDir = new java.io.File(servicesDir);
            java.io.File[] serviceFolders = baseServiceDir.listFiles(org.apache.ambari.server.stack.StackDirectory.FILENAME_FILTER);
            if (serviceFolders != null) {
                for (java.io.File d : serviceFolders) {
                    if (d.isDirectory()) {
                        try {
                            dirs.add(new org.apache.ambari.server.stack.StackServiceDirectory(d.getAbsolutePath()));
                        } catch (org.apache.ambari.server.AmbariException e) {
                            org.apache.ambari.server.stack.StackDirectory.LOG.warn(java.lang.String.format("Unable to parse stack definition service at '%s'.  Ignoring service. : %s", d.getAbsolutePath(), e.toString()));
                        }
                    }
                }
            }
        }
        if (dirs.isEmpty()) {
            org.apache.ambari.server.stack.StackDirectory.LOG.info(("The stack defined at '" + getAbsolutePath()) + "' contains no services");
        }
        serviceDirectories = dirs;
    }

    private void parseUpgradePacks(java.util.Collection<java.lang.String> subDirs) throws org.apache.ambari.server.AmbariException {
        java.util.Map<java.lang.String, org.apache.ambari.server.stack.upgrade.UpgradePack> upgradeMap = new java.util.HashMap<>();
        org.apache.ambari.server.stack.upgrade.ConfigUpgradePack configUpgradePack = null;
        if (subDirs.contains(org.apache.ambari.server.stack.StackDirectory.UPGRADE_PACK_FOLDER_NAME)) {
            java.io.File f = new java.io.File((getAbsolutePath() + java.io.File.separator) + org.apache.ambari.server.stack.StackDirectory.UPGRADE_PACK_FOLDER_NAME);
            if (f.isDirectory()) {
                upgradesDir = f.getAbsolutePath();
                for (java.io.File upgradeFile : f.listFiles(org.apache.ambari.server.stack.StackDefinitionDirectory.XML_FILENAME_FILTER)) {
                    if (upgradeFile.getName().toLowerCase().startsWith(org.apache.ambari.server.stack.StackDefinitionDirectory.CONFIG_UPGRADE_XML_FILENAME_PREFIX)) {
                        if (configUpgradePack == null) {
                            if (upgradeFile.length() != 0) {
                                configUpgradePack = parseConfigUpgradePack(upgradeFile);
                            }
                        } else {
                            throw new org.apache.ambari.server.AmbariException(java.lang.String.format("There are multiple files with name like %s" + upgradeFile.getAbsolutePath()));
                        }
                    } else {
                        java.lang.String upgradePackName = org.apache.commons.io.FilenameUtils.removeExtension(upgradeFile.getName());
                        if (upgradeFile.length() != 0) {
                            org.apache.ambari.server.stack.upgrade.UpgradePack pack = parseUpgradePack(upgradePackName, upgradeFile);
                            pack.setName(upgradePackName);
                            upgradeMap.put(upgradePackName, pack);
                        }
                    }
                }
            }
        }
        if (upgradesDir == null) {
            org.apache.ambari.server.stack.StackDirectory.LOG.info("Stack '{}' doesn't contain an upgrade directory ", getPath());
        }
        if (!upgradeMap.isEmpty()) {
            upgradePacks = upgradeMap;
        }
        if (configUpgradePack != null) {
            this.configUpgradePack = configUpgradePack;
        } else {
            org.apache.ambari.server.stack.upgrade.ConfigUpgradePack emptyConfigUpgradePack = new org.apache.ambari.server.stack.upgrade.ConfigUpgradePack();
            emptyConfigUpgradePack.services = new java.util.ArrayList<>();
            this.configUpgradePack = emptyConfigUpgradePack;
            org.apache.ambari.server.stack.StackDirectory.LOG.info("Stack '{}' doesn't contain config upgrade pack file", getPath());
        }
    }

    private org.apache.ambari.server.stack.upgrade.UpgradePack parseUpgradePack(final java.lang.String packName, java.io.File upgradeFile) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.stack.upgrade.UpgradePack pack = null;
        try {
            pack = unmarshaller.unmarshal(org.apache.ambari.server.stack.upgrade.UpgradePack.class, upgradeFile);
            pack.setName(packName);
        } catch (java.lang.Exception e) {
            if (upgradeFile == null) {
                throw new org.apache.ambari.server.AmbariException("Null upgrade pack");
            }
            throw new org.apache.ambari.server.AmbariException("Unable to parse stack upgrade file at location: " + upgradeFile.getAbsolutePath(), e);
        }
        return pack;
    }

    private org.apache.ambari.server.stack.upgrade.ConfigUpgradePack parseConfigUpgradePack(java.io.File upgradeFile) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.stack.upgrade.ConfigUpgradePack pack = null;
        try {
            pack = unmarshaller.unmarshal(org.apache.ambari.server.stack.upgrade.ConfigUpgradePack.class, upgradeFile);
        } catch (java.lang.Exception e) {
            if (upgradeFile == null) {
                throw new org.apache.ambari.server.AmbariException("Null config upgrade pack");
            }
            throw new org.apache.ambari.server.AmbariException("Unable to parse stack upgrade file at location: " + upgradeFile.getAbsolutePath(), e);
        }
        return pack;
    }

    private void parseRoleCommandOrder() {
        java.util.HashMap<java.lang.String, java.lang.Object> result = null;
        org.codehaus.jackson.map.ObjectMapper mapper = new org.codehaus.jackson.map.ObjectMapper();
        try {
            org.codehaus.jackson.type.TypeReference<java.util.Map<java.lang.String, java.lang.Object>> rcoElementTypeReference = new org.codehaus.jackson.type.TypeReference<java.util.Map<java.lang.String, java.lang.Object>>() {};
            if (rcoFilePath != null) {
                java.io.File file = new java.io.File(rcoFilePath);
                result = mapper.readValue(file, rcoElementTypeReference);
                org.apache.ambari.server.stack.StackDirectory.LOG.info("Role command order info was loaded from file: {}", file.getAbsolutePath());
            } else {
                org.apache.ambari.server.stack.StackDirectory.LOG.info("Stack '{}' doesn't contain role command order file", getPath());
                result = new java.util.HashMap<>();
            }
            roleCommandOrder = new org.apache.ambari.server.state.stack.StackRoleCommandOrder(result);
            if (org.apache.ambari.server.stack.StackDirectory.LOG.isDebugEnabled()) {
                org.apache.ambari.server.stack.StackDirectory.LOG.debug("Role Command Order for {}", rcoFilePath);
                roleCommandOrder.printRoleCommandOrder(org.apache.ambari.server.stack.StackDirectory.LOG);
            }
        } catch (java.io.IOException e) {
            org.apache.ambari.server.stack.StackDirectory.LOG.error(java.lang.String.format("Can not read role command order info %s", rcoFilePath), e);
        }
    }

    private void parseLibraryClassLoader() {
        if (null == libraryDir) {
            return;
        }
        java.nio.file.Path libraryPath = java.nio.file.Paths.get(libraryDir);
        if (java.nio.file.Files.notExists(libraryPath) || (!java.nio.file.Files.isDirectory(libraryPath))) {
            return;
        }
        try {
            java.util.List<java.net.URI> jarUris = java.nio.file.Files.list(libraryPath).filter(file -> file.toString().endsWith(".jar")).map(java.nio.file.Path::toUri).collect(java.util.stream.Collectors.toList());
            java.util.List<java.net.URL> jarUrls = new java.util.ArrayList<>(jarUris.size());
            for (java.net.URI jarUri : jarUris) {
                try {
                    jarUrls.add(jarUri.toURL());
                } catch (java.net.MalformedURLException malformedURLException) {
                    org.apache.ambari.server.stack.StackDirectory.LOG.error("Unable to load the stack library {}", jarUri, malformedURLException);
                }
            }
            java.net.URL[] jarUrlArray = new java.net.URL[jarUris.size()];
            libraryClassLoader = new java.net.URLClassLoader(jarUrls.toArray(jarUrlArray), org.springframework.util.ClassUtils.getDefaultClassLoader());
        } catch (java.io.IOException ioException) {
            org.apache.ambari.server.stack.StackDirectory.LOG.error("Unable to load libraries from {}", libraryPath, ioException);
        }
    }
}