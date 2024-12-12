package org.apache.ambari.server.view;
public class ViewExtractor {
    private static final java.lang.String ARCHIVE_CLASSES_DIR = "WEB-INF/classes";

    private static final java.lang.String ARCHIVE_LIB_DIR = "WEB-INF/lib";

    private static final int BUFFER_SIZE = 1024;

    @javax.inject.Inject
    org.apache.ambari.server.view.ViewArchiveUtility archiveUtility;

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.view.ViewExtractor.class);

    public java.lang.ClassLoader extractViewArchive(org.apache.ambari.server.orm.entities.ViewEntity view, java.io.File viewArchive, java.io.File archiveDir, java.util.List<java.io.File> viewsAdditionalClasspath) throws org.apache.ambari.server.view.ViewExtractor.ExtractionException {
        java.lang.String archivePath = archiveDir.getAbsolutePath();
        try {
            if ((archiveDir.exists() && (viewArchive != null)) && (viewArchive.lastModified() > archiveDir.lastModified())) {
                org.apache.commons.io.FileUtils.deleteDirectory(archiveDir);
            }
            if (!archiveDir.exists()) {
                java.lang.String msg = ("Creating archive folder " + archivePath) + ".";
                view.setStatusDetail(msg);
                org.apache.ambari.server.view.ViewExtractor.LOG.info(msg);
                if (archiveDir.mkdir()) {
                    java.util.jar.JarInputStream jarInputStream = archiveUtility.getJarFileStream(viewArchive);
                    try {
                        msg = ("Extracting files from " + viewArchive.getName()) + ".";
                        view.setStatusDetail(msg);
                        org.apache.ambari.server.view.ViewExtractor.LOG.info(msg);
                        java.io.File metaInfDir = archiveUtility.getFile((archivePath + java.io.File.separator) + "META-INF");
                        if (!metaInfDir.mkdir()) {
                            msg = "Could not create archive META-INF directory.";
                            view.setStatusDetail(msg);
                            org.apache.ambari.server.view.ViewExtractor.LOG.error(msg);
                            throw new org.apache.ambari.server.view.ViewExtractor.ExtractionException(msg);
                        }
                        java.util.jar.JarEntry jarEntry;
                        while ((jarEntry = jarInputStream.getNextJarEntry()) != null) {
                            try {
                                java.lang.String entryPath = (archivePath + java.io.File.separator) + jarEntry.getName();
                                org.apache.ambari.server.view.ViewExtractor.LOG.debug("Extracting {}", entryPath);
                                java.io.File entryFile = archiveUtility.getFile(entryPath);
                                if (jarEntry.isDirectory()) {
                                    if (!entryFile.exists()) {
                                        org.apache.ambari.server.view.ViewExtractor.LOG.debug("Making directory {}", entryPath);
                                        if (!entryFile.mkdir()) {
                                            msg = ("Could not create archive entry directory " + entryPath) + ".";
                                            view.setStatusDetail(msg);
                                            org.apache.ambari.server.view.ViewExtractor.LOG.error(msg);
                                            throw new org.apache.ambari.server.view.ViewExtractor.ExtractionException(msg);
                                        }
                                    }
                                } else {
                                    java.io.FileOutputStream fos = archiveUtility.getFileOutputStream(entryFile);
                                    try {
                                        org.apache.ambari.server.view.ViewExtractor.LOG.debug("Begin copying from {} to {}", jarEntry.getName(), entryPath);
                                        byte[] buffer = new byte[org.apache.ambari.server.view.ViewExtractor.BUFFER_SIZE];
                                        int n;
                                        while ((n = jarInputStream.read(buffer)) > (-1)) {
                                            fos.write(buffer, 0, n);
                                        } 
                                        org.apache.ambari.server.view.ViewExtractor.LOG.debug("Finish copying from {} to {}", jarEntry.getName(), entryPath);
                                    } finally {
                                        fos.flush();
                                        fos.close();
                                    }
                                }
                            } finally {
                                jarInputStream.closeEntry();
                            }
                        } 
                    } finally {
                        jarInputStream.close();
                    }
                } else {
                    msg = ("Could not create archive directory " + archivePath) + ".";
                    view.setStatusDetail(msg);
                    org.apache.ambari.server.view.ViewExtractor.LOG.error(msg);
                    throw new org.apache.ambari.server.view.ViewExtractor.ExtractionException(msg);
                }
            }
            org.apache.ambari.server.view.configuration.ViewConfig viewConfig = archiveUtility.getViewConfigFromExtractedArchive(archivePath, false);
            return getArchiveClassLoader(viewConfig, archiveDir, viewsAdditionalClasspath);
        } catch (java.lang.Exception e) {
            java.lang.String msg = ("Caught exception trying to extract the view archive " + archivePath) + ".";
            view.setStatusDetail(msg);
            org.apache.ambari.server.view.ViewExtractor.LOG.error(msg, e);
            throw new org.apache.ambari.server.view.ViewExtractor.ExtractionException(msg, e);
        }
    }

    public boolean ensureExtractedArchiveDirectory(java.lang.String extractedArchivesPath) {
        java.io.File extractedArchiveDir = archiveUtility.getFile(extractedArchivesPath);
        return extractedArchiveDir.exists() || extractedArchiveDir.mkdir();
    }

    private java.lang.ClassLoader getArchiveClassLoader(org.apache.ambari.server.view.configuration.ViewConfig viewConfig, java.io.File archiveDir, java.util.List<java.io.File> viewsAdditionalClasspath) throws java.io.IOException {
        java.lang.String archivePath = archiveDir.getAbsolutePath();
        java.util.List<java.net.URL> urlList = new java.util.LinkedList<>();
        java.lang.String classesPath = (archivePath + java.io.File.separator) + org.apache.ambari.server.view.ViewExtractor.ARCHIVE_CLASSES_DIR;
        java.io.File classesDir = archiveUtility.getFile(classesPath);
        if (classesDir.exists()) {
            urlList.add(classesDir.toURI().toURL());
        }
        for (java.io.File file : viewsAdditionalClasspath) {
            if (file.isDirectory()) {
                addDirToClasspath(urlList, file);
            } else if (file.isFile()) {
                urlList.add(file.toURI().toURL());
            }
        }
        java.lang.String libPath = (archivePath + java.io.File.separator) + org.apache.ambari.server.view.ViewExtractor.ARCHIVE_LIB_DIR;
        java.io.File libDir = archiveUtility.getFile(libPath);
        addDirToClasspath(urlList, libDir);
        urlList.add(archiveDir.toURI().toURL());
        org.apache.ambari.server.view.ViewExtractor.LOG.trace("classpath for view {} is : {}", viewConfig.getName(), urlList);
        return new org.apache.ambari.server.view.ViewClassLoader(viewConfig, urlList.toArray(new java.net.URL[urlList.size()]));
    }

    private void addDirToClasspath(java.util.List<java.net.URL> urlList, java.io.File libDir) throws java.net.MalformedURLException {
        if (libDir.exists()) {
            java.io.File[] files = libDir.listFiles();
            if (files != null) {
                for (final java.io.File fileEntry : files) {
                    if (!fileEntry.isDirectory()) {
                        urlList.add(fileEntry.toURI().toURL());
                    }
                }
            }
        }
    }

    public static class ExtractionException extends java.lang.Exception {
        public ExtractionException(java.lang.String msg) {
            super(msg);
        }

        public ExtractionException(java.lang.String msg, java.lang.Throwable throwable) {
            super(msg, throwable);
        }
    }
}