package org.apache.ambari.server.stack;
public class ExtensionDirectory extends org.apache.ambari.server.stack.StackDefinitionDirectory {
    private java.util.Collection<org.apache.ambari.server.stack.ServiceDirectory> serviceDirectories;

    private org.apache.ambari.server.state.stack.ExtensionMetainfoXml metaInfoXml;

    org.apache.ambari.server.stack.ModuleFileUnmarshaller unmarshaller = new org.apache.ambari.server.stack.ModuleFileUnmarshaller();

    public static final java.lang.String EXTENSIONS_FOLDER_NAME = "extensions";

    private static final java.lang.String EXTENSION_METAINFO_FILE_NAME = "metainfo.xml";

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.stack.ExtensionDirectory.class);

    public ExtensionDirectory(java.lang.String directory) throws org.apache.ambari.server.AmbariException {
        super(directory);
        parsePath();
    }

    public java.lang.String getExtensionDirName() {
        return getDirectory().getParentFile().getName();
    }

    public org.apache.ambari.server.state.stack.ExtensionMetainfoXml getMetaInfoFile() {
        return metaInfoXml;
    }

    public java.util.Collection<org.apache.ambari.server.stack.ServiceDirectory> getServiceDirectories() {
        return serviceDirectories;
    }

    private void parsePath() throws org.apache.ambari.server.AmbariException {
        java.util.Collection<java.lang.String> subDirs = java.util.Arrays.asList(directory.list());
        parseServiceDirectories(subDirs);
        parseMetaInfoFile();
    }

    private void parseMetaInfoFile() throws org.apache.ambari.server.AmbariException {
        java.io.File extensionMetaInfoFile = new java.io.File((getAbsolutePath() + java.io.File.separator) + org.apache.ambari.server.stack.ExtensionDirectory.EXTENSION_METAINFO_FILE_NAME);
        if (extensionMetaInfoFile.exists()) {
            if (org.apache.ambari.server.stack.ExtensionDirectory.LOG.isDebugEnabled()) {
                org.apache.ambari.server.stack.ExtensionDirectory.LOG.debug("Reading extension version metainfo from file {}", extensionMetaInfoFile.getAbsolutePath());
            }
            try {
                metaInfoXml = unmarshaller.unmarshal(org.apache.ambari.server.state.stack.ExtensionMetainfoXml.class, extensionMetaInfoFile);
            } catch (java.lang.Exception e) {
                metaInfoXml = new org.apache.ambari.server.state.stack.ExtensionMetainfoXml();
                metaInfoXml.setValid(false);
                metaInfoXml.addError("Unable to parse extension metainfo.xml file at location: " + extensionMetaInfoFile.getAbsolutePath());
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
                            org.apache.ambari.server.stack.ExtensionDirectory.LOG.warn(java.lang.String.format("Unable to parse extension definition service at '%s'.  Ignoring service. : %s", d.getAbsolutePath(), e.toString()));
                        }
                    }
                }
            }
        }
        if (dirs.isEmpty()) {
            org.apache.ambari.server.stack.ExtensionDirectory.LOG.info(("The extension defined at '" + getAbsolutePath()) + "' contains no services");
        }
        serviceDirectories = dirs;
    }
}