package org.apache.ambari.server.resources;
import static org.easymock.EasyMock.createNiceMock;
public class TestResources extends junit.framework.TestCase {
    private static org.apache.ambari.server.resources.ResourceManager resMan;

    private static final java.lang.String RESOURCE_FILE_NAME = "resources.ext";

    private static final java.lang.String RESOURCE_FILE_CONTENT = "CONTENT";

    com.google.inject.Injector injector;

    private org.junit.rules.TemporaryFolder tempFolder = new org.junit.rules.TemporaryFolder();

    private java.io.File resourceFile;

    protected java.util.Properties buildTestProperties() {
        java.util.Properties properties = new java.util.Properties();
        try {
            tempFolder.create();
            properties.setProperty(org.apache.ambari.server.configuration.Configuration.SRVR_KSTR_DIR.getKey(), tempFolder.getRoot().getAbsolutePath());
            properties.setProperty(org.apache.ambari.server.configuration.Configuration.RESOURCES_DIR.getKey(), tempFolder.getRoot().getAbsolutePath());
            resourceFile = tempFolder.newFile(org.apache.ambari.server.resources.TestResources.RESOURCE_FILE_NAME);
            org.apache.commons.io.FileUtils.writeStringToFile(resourceFile, org.apache.ambari.server.resources.TestResources.RESOURCE_FILE_CONTENT, java.nio.charset.Charset.defaultCharset());
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
        return properties;
    }

    protected java.lang.reflect.Constructor<org.apache.ambari.server.configuration.Configuration> getConfigurationConstructor() {
        try {
            return org.apache.ambari.server.configuration.Configuration.class.getConstructor(java.util.Properties.class);
        } catch (java.lang.NoSuchMethodException e) {
            throw new java.lang.RuntimeException("Expected constructor not found in Configuration.java", e);
        }
    }

    private class ResourceModule extends com.google.inject.AbstractModule {
        @java.lang.Override
        protected void configure() {
            bind(java.util.Properties.class).toInstance(buildTestProperties());
            bind(org.apache.ambari.server.configuration.Configuration.class).toConstructor(getConfigurationConstructor());
            bind(org.apache.ambari.server.state.stack.OsFamily.class).toInstance(EasyMock.createNiceMock(org.apache.ambari.server.state.stack.OsFamily.class));
            requestStaticInjection(org.apache.ambari.server.resources.TestResources.class);
        }
    }

    @com.google.inject.Inject
    static void init(org.apache.ambari.server.resources.ResourceManager instance) {
        org.apache.ambari.server.resources.TestResources.resMan = instance;
    }

    @java.lang.Override
    @org.junit.Before
    public void setUp() throws java.io.IOException {
        injector = com.google.inject.Guice.createInjector(new org.apache.ambari.server.resources.TestResources.ResourceModule());
        org.apache.ambari.server.resources.TestResources.resMan = injector.getInstance(org.apache.ambari.server.resources.ResourceManager.class);
    }

    @java.lang.Override
    @org.junit.After
    public void tearDown() throws java.io.IOException {
        resourceFile.delete();
        tempFolder.delete();
    }

    @org.junit.Test
    public void testGetResource() throws java.lang.Exception {
        java.io.File resFile = org.apache.ambari.server.resources.TestResources.resMan.getResource(resourceFile.getName());
        junit.framework.Assert.assertTrue(resFile.exists());
        java.lang.String resContent = org.apache.commons.io.FileUtils.readFileToString(resFile, java.nio.charset.Charset.defaultCharset());
        junit.framework.Assert.assertEquals(resContent, org.apache.ambari.server.resources.TestResources.RESOURCE_FILE_CONTENT);
    }
}