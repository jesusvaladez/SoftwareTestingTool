package org.apache.ambari.server.stack;
public abstract class StackDefinitionDirectory {
    protected static final java.io.FilenameFilter XML_FILENAME_FILTER = new java.io.FilenameFilter() {
        @java.lang.Override
        public boolean accept(java.io.File folder, java.lang.String fileName) {
            return fileName.toLowerCase().endsWith(".xml");
        }
    };

    public static final java.lang.String CONFIG_UPGRADE_XML_FILENAME_PREFIX = "config-upgrade.xml";

    protected java.io.File directory;

    public StackDefinitionDirectory(java.lang.String directory) {
        this.directory = new java.io.File(directory);
    }

    public org.apache.ambari.server.stack.ConfigurationDirectory getConfigurationDirectory(java.lang.String directoryName, java.lang.String propertiesDirectoryName) {
        org.apache.ambari.server.stack.ConfigurationDirectory configDirectory = null;
        java.io.File configDirFile = new java.io.File((directory.getAbsolutePath() + java.io.File.separator) + directoryName);
        java.io.File propertiesDirFile = new java.io.File((directory.getAbsolutePath() + java.io.File.separator) + propertiesDirectoryName);
        if (configDirFile.exists() && configDirFile.isDirectory()) {
            if (propertiesDirFile.exists() && propertiesDirFile.isDirectory()) {
                configDirectory = new org.apache.ambari.server.stack.ConfigurationDirectory(configDirFile.getAbsolutePath(), propertiesDirFile.getAbsolutePath());
            } else {
                configDirectory = new org.apache.ambari.server.stack.ConfigurationDirectory(configDirFile.getAbsolutePath(), null);
            }
        }
        return configDirectory;
    }

    public java.lang.String getPath() {
        return directory.getPath();
    }

    public java.lang.String getAbsolutePath() {
        return directory.getAbsolutePath();
    }

    public java.lang.String getName() {
        return directory.getName();
    }

    protected java.io.File getDirectory() {
        return directory;
    }
}