package org.apache.ambari.server.view;
import javax.xml.bind.JAXBException;
import org.easymock.EasyMockSupport;
import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.reset;
import static org.easymock.EasyMock.verify;
public class ViewExtractorTest extends org.easymock.EasyMockSupport {
    private final java.io.File extractedArchiveDir = createNiceMock(java.io.File.class);

    private final java.io.File viewArchive = createNiceMock(java.io.File.class);

    private final java.io.File archiveDir = createNiceMock(java.io.File.class);

    private final java.io.File entryFile = createNiceMock(java.io.File.class);

    private final java.io.File classesDir = createNiceMock(java.io.File.class);

    private final java.io.File libDir = createNiceMock(java.io.File.class);

    private final java.io.File metaInfDir = createNiceMock(java.io.File.class);

    private final java.io.File metaInfManifest = createNiceMock(java.io.File.class);

    private final java.util.jar.JarInputStream viewJarFile = createNiceMock(java.util.jar.JarInputStream.class);

    private final java.util.jar.JarEntry jarEntry = createNiceMock(java.util.jar.JarEntry.class);

    private final java.io.FileOutputStream fos = createMock(java.io.FileOutputStream.class);

    private final org.apache.ambari.server.configuration.Configuration configuration = createNiceMock(org.apache.ambari.server.configuration.Configuration.class);

    private final java.io.File viewDir = createNiceMock(java.io.File.class);

    private final java.io.File fileEntry = createNiceMock(java.io.File.class);

    private final org.apache.ambari.server.orm.dao.ViewDAO viewDAO = createMock(org.apache.ambari.server.orm.dao.ViewDAO.class);

    @org.junit.Before
    public void resetGlobalMocks() {
        resetAll();
    }

    @org.junit.Test
    public void testExtractViewArchive() throws java.lang.Exception {
        java.io.File addDirPath = createNiceMock(java.io.File.class);
        java.io.File addDirPathFile1 = createNiceMock(java.io.File.class);
        java.io.File addDirPathFile2 = createNiceMock(java.io.File.class);
        java.io.File addDirPath2 = createNiceMock(java.io.File.class);
        java.io.File addFilePath = createNiceMock(java.io.File.class);
        java.util.List<java.io.File> viewsAdditionalClasspath = java.util.Arrays.asList(addDirPath, addDirPath2, addFilePath);
        org.apache.ambari.server.orm.entities.ResourceTypeEntity resourceTypeEntity = new org.apache.ambari.server.orm.entities.ResourceTypeEntity();
        resourceTypeEntity.setId(10);
        resourceTypeEntity.setName("MY_VIEW{1.0.0}");
        org.apache.ambari.server.orm.entities.ViewEntity viewDefinition = org.apache.ambari.server.orm.entities.ViewEntityTest.getViewEntity();
        viewDefinition.setResourceType(resourceTypeEntity);
        EasyMock.expect(configuration.getViewExtractionThreadPoolCoreSize()).andReturn(2).anyTimes();
        EasyMock.expect(configuration.getViewExtractionThreadPoolMaxSize()).andReturn(3).anyTimes();
        EasyMock.expect(configuration.getViewExtractionThreadPoolTimeout()).andReturn(10000L).anyTimes();
        if (java.lang.System.getProperty("os.name").contains("Windows")) {
            EasyMock.expect(viewArchive.getAbsolutePath()).andReturn("\\var\\lib\\ambari-server\\resources\\views\\work\\MY_VIEW{1.0.0}").anyTimes();
            EasyMock.expect(metaInfManifest.getAbsolutePath()).andReturn("\\var\\lib\\ambari-server\\resources\\views\\work\\MY_VIEW{1.0.0}\\META-INF\\MANIFEST.MF").anyTimes();
            EasyMock.expect(archiveDir.getAbsolutePath()).andReturn("\\var\\lib\\ambari-server\\resources\\views\\work\\MY_VIEW{1.0.0}").anyTimes();
        } else {
            EasyMock.expect(viewArchive.getAbsolutePath()).andReturn("/var/lib/ambari-server/resources/views/work/MY_VIEW{1.0.0}").anyTimes();
            EasyMock.expect(metaInfManifest.getAbsolutePath()).andReturn("/var/lib/ambari-server/resources/views/work/MY_VIEW{1.0.0}/META-INF/MANIFEST.MF").anyTimes();
            EasyMock.expect(archiveDir.getAbsolutePath()).andReturn("/var/lib/ambari-server/resources/views/work/MY_VIEW{1.0.0}").anyTimes();
        }
        EasyMock.expect(archiveDir.exists()).andReturn(false);
        EasyMock.expect(archiveDir.mkdir()).andReturn(true);
        EasyMock.expect(archiveDir.toURI()).andReturn(new java.net.URI("file:./"));
        EasyMock.expect(metaInfDir.mkdir()).andReturn(true);
        EasyMock.expect(viewJarFile.getNextJarEntry()).andReturn(jarEntry);
        EasyMock.expect(viewJarFile.getNextJarEntry()).andReturn(null);
        EasyMock.expect(jarEntry.getName()).andReturn("view.xml");
        EasyMock.expect(viewJarFile.read(EasyMock.anyObject(byte[].class))).andReturn(10);
        EasyMock.expect(viewJarFile.read(EasyMock.anyObject(byte[].class))).andReturn(-1);
        fos.write(EasyMock.anyObject(byte[].class), EasyMock.eq(0), EasyMock.eq(10));
        fos.flush();
        fos.close();
        viewJarFile.closeEntry();
        viewJarFile.close();
        EasyMock.expect(classesDir.exists()).andReturn(true);
        EasyMock.expect(classesDir.toURI()).andReturn(new java.net.URI("file:./"));
        EasyMock.expect(libDir.exists()).andReturn(true);
        EasyMock.expect(libDir.listFiles()).andReturn(new java.io.File[]{ fileEntry });
        EasyMock.expect(fileEntry.toURI()).andReturn(new java.net.URI("file:./"));
        EasyMock.expect(addDirPath.isDirectory()).andReturn(true);
        EasyMock.expect(addDirPath.exists()).andReturn(true);
        EasyMock.expect(addDirPath.listFiles()).andReturn(new java.io.File[]{ addDirPathFile1, addDirPathFile2 });
        EasyMock.expect(addDirPathFile1.isDirectory()).andReturn(false);
        EasyMock.expect(addDirPathFile1.toURI()).andReturn(new java.net.URI("file://file1"));
        EasyMock.expect(addDirPathFile2.isDirectory()).andReturn(false);
        EasyMock.expect(addDirPathFile2.toURI()).andReturn(new java.net.URI("file://file2"));
        EasyMock.expect(addDirPath2.isDirectory()).andReturn(true);
        EasyMock.expect(addDirPath2.exists()).andReturn(true);
        EasyMock.expect(addDirPath2.listFiles()).andReturn(new java.io.File[]{  });
        EasyMock.expect(addFilePath.isDirectory()).andReturn(false);
        EasyMock.expect(addFilePath.isFile()).andReturn(true);
        EasyMock.expect(addFilePath.toURI()).andReturn(new java.net.URI("file://file3"));
        replayAll();
        org.apache.ambari.server.view.ViewExtractor viewExtractor = getViewExtractor(viewDefinition);
        viewExtractor.extractViewArchive(viewDefinition, viewArchive, archiveDir, viewsAdditionalClasspath);
        verifyAll();
    }

    @org.junit.Test
    public void testEnsureExtractedArchiveDirectory() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.ResourceTypeEntity resourceTypeEntity = new org.apache.ambari.server.orm.entities.ResourceTypeEntity();
        resourceTypeEntity.setId(10);
        resourceTypeEntity.setName("MY_VIEW{1.0.0}");
        org.apache.ambari.server.orm.entities.ViewEntity viewDefinition = org.apache.ambari.server.orm.entities.ViewEntityTest.getViewEntity();
        viewDefinition.setResourceType(resourceTypeEntity);
        EasyMock.expect(extractedArchiveDir.exists()).andReturn(true);
        replayAll();
        org.apache.ambari.server.view.ViewExtractor viewExtractor = getViewExtractor(viewDefinition);
        if (java.lang.System.getProperty("os.name").contains("Windows")) {
            org.junit.Assert.assertTrue(viewExtractor.ensureExtractedArchiveDirectory("\\var\\lib\\ambari-server\\resources\\views\\work"));
        } else {
            org.junit.Assert.assertTrue(viewExtractor.ensureExtractedArchiveDirectory("/var/lib/ambari-server/resources/views/work"));
        }
        verifyAll();
        EasyMock.reset(extractedArchiveDir);
        EasyMock.expect(extractedArchiveDir.exists()).andReturn(false);
        EasyMock.expect(extractedArchiveDir.mkdir()).andReturn(true);
        EasyMock.replay(extractedArchiveDir);
        viewExtractor = getViewExtractor(viewDefinition);
        if (java.lang.System.getProperty("os.name").contains("Windows")) {
            org.junit.Assert.assertTrue(viewExtractor.ensureExtractedArchiveDirectory("\\var\\lib\\ambari-server\\resources\\views\\work"));
        } else {
            org.junit.Assert.assertTrue(viewExtractor.ensureExtractedArchiveDirectory("/var/lib/ambari-server/resources/views/work"));
        }
        EasyMock.verify(extractedArchiveDir);
        EasyMock.reset(extractedArchiveDir);
        EasyMock.expect(extractedArchiveDir.exists()).andReturn(false);
        EasyMock.expect(extractedArchiveDir.mkdir()).andReturn(false);
        EasyMock.replay(extractedArchiveDir);
        viewExtractor = getViewExtractor(viewDefinition);
        if (java.lang.System.getProperty("os.name").contains("Windows")) {
            org.junit.Assert.assertFalse(viewExtractor.ensureExtractedArchiveDirectory("\\var\\lib\\ambari-server\\resources\\views\\work"));
        } else {
            org.junit.Assert.assertFalse(viewExtractor.ensureExtractedArchiveDirectory("/var/lib/ambari-server/resources/views/work"));
        }
        EasyMock.verify(extractedArchiveDir);
    }

    private org.apache.ambari.server.view.ViewExtractor getViewExtractor(org.apache.ambari.server.orm.entities.ViewEntity viewDefinition) throws java.lang.Exception {
        java.util.Map<java.io.File, org.apache.ambari.server.view.configuration.ViewConfig> viewConfigs = java.util.Collections.singletonMap(viewArchive, viewDefinition.getConfiguration());
        java.util.Map<java.lang.String, java.io.File> files = new java.util.HashMap<>();
        if (java.lang.System.getProperty("os.name").contains("Windows")) {
            files.put("\\var\\lib\\ambari-server\\resources\\views\\work\\MY_VIEW{1.0.0}\\META-INF\\MANIFEST.MF", metaInfManifest);
            files.put("\\var\\lib\\ambari-server\\resources\\views\\work\\MY_VIEW{1.0.0}\\META-INF", metaInfDir);
            files.put("\\var\\lib\\ambari-server\\resources\\views\\work", extractedArchiveDir);
            files.put("\\var\\lib\\ambari-server\\resources\\views\\work\\MY_VIEW{1.0.0}", archiveDir);
            files.put("\\var\\lib\\ambari-server\\resources\\views\\work\\MY_VIEW{1.0.0}\\view.xml", entryFile);
            files.put("\\var\\lib\\ambari-server\\resources\\views\\work\\MY_VIEW{1.0.0}\\WEB-INF/classes", classesDir);
            files.put("\\var\\lib\\ambari-server\\resources\\views\\work\\MY_VIEW{1.0.0}\\WEB-INF/lib", libDir);
        } else {
            files.put("/var/lib/ambari-server/resources/views/work/MY_VIEW{1.0.0}/META-INF/MANIFEST.MF", metaInfManifest);
            files.put("/var/lib/ambari-server/resources/views/work/MY_VIEW{1.0.0}/META-INF", metaInfDir);
            files.put("/var/lib/ambari-server/resources/views/work", extractedArchiveDir);
            files.put("/var/lib/ambari-server/resources/views/work/MY_VIEW{1.0.0}", archiveDir);
            files.put("/var/lib/ambari-server/resources/views/work/MY_VIEW{1.0.0}/view.xml", entryFile);
            files.put("/var/lib/ambari-server/resources/views/work/MY_VIEW{1.0.0}/WEB-INF/classes", classesDir);
            files.put("/var/lib/ambari-server/resources/views/work/MY_VIEW{1.0.0}/WEB-INF/lib", libDir);
        }
        java.util.Map<java.io.File, java.io.FileOutputStream> outputStreams = new java.util.HashMap<>();
        outputStreams.put(entryFile, fos);
        java.util.Map<java.io.File, java.util.jar.JarInputStream> jarFiles = new java.util.HashMap<>();
        jarFiles.put(viewArchive, viewJarFile);
        org.apache.ambari.server.view.ViewExtractorTest.TestViewArchiveUtility archiveUtility = new org.apache.ambari.server.view.ViewExtractorTest.TestViewArchiveUtility(viewConfigs, files, outputStreams, jarFiles);
        org.apache.ambari.server.view.ViewExtractor viewExtractor = new org.apache.ambari.server.view.ViewExtractor();
        viewExtractor.archiveUtility = archiveUtility;
        return viewExtractor;
    }

    public static class TestViewArchiveUtility extends org.apache.ambari.server.view.ViewArchiveUtility {
        private final java.util.Map<java.io.File, org.apache.ambari.server.view.configuration.ViewConfig> viewConfigs;

        private final java.util.Map<java.lang.String, java.io.File> files;

        private final java.util.Map<java.io.File, java.io.FileOutputStream> outputStreams;

        private final java.util.Map<java.io.File, java.util.jar.JarInputStream> jarFiles;

        public TestViewArchiveUtility(java.util.Map<java.io.File, org.apache.ambari.server.view.configuration.ViewConfig> viewConfigs, java.util.Map<java.lang.String, java.io.File> files, java.util.Map<java.io.File, java.io.FileOutputStream> outputStreams, java.util.Map<java.io.File, java.util.jar.JarInputStream> jarFiles) {
            this.viewConfigs = viewConfigs;
            this.files = files;
            this.outputStreams = outputStreams;
            this.jarFiles = jarFiles;
        }

        @java.lang.Override
        public org.apache.ambari.server.view.configuration.ViewConfig getViewConfigFromArchive(java.io.File archiveFile) throws java.net.MalformedURLException, javax.xml.bind.JAXBException {
            return viewConfigs.get(archiveFile);
        }

        @java.lang.Override
        public org.apache.ambari.server.view.configuration.ViewConfig getViewConfigFromExtractedArchive(java.lang.String archivePath, boolean validate) throws javax.xml.bind.JAXBException, java.io.FileNotFoundException {
            for (java.io.File viewConfigKey : viewConfigs.keySet()) {
                if (viewConfigKey.getAbsolutePath().equals(archivePath)) {
                    return viewConfigs.get(viewConfigKey);
                }
            }
            return null;
        }

        @java.lang.Override
        public java.io.File getFile(java.lang.String path) {
            return files.get(path);
        }

        @java.lang.Override
        public java.io.FileOutputStream getFileOutputStream(java.io.File file) throws java.io.FileNotFoundException {
            return outputStreams.get(file);
        }

        @java.lang.Override
        public java.util.jar.JarInputStream getJarFileStream(java.io.File file) throws java.io.IOException {
            return jarFiles.get(file);
        }
    }
}