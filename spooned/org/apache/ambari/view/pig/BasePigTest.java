package org.apache.ambari.view.pig;
import org.apache.hadoop.fs.FileUtil;
import org.apache.hadoop.security.UserGroupInformation;
import static org.easymock.EasyMock.*;
public abstract class BasePigTest {
    protected org.apache.ambari.view.ViewResourceHandler handler;

    protected org.apache.ambari.view.ViewContext context;

    protected static java.io.File pigStorageFile;

    protected static java.io.File baseDir;

    protected java.util.Map<java.lang.String, java.lang.String> properties;

    protected static java.lang.String DATA_DIRECTORY = "./target/PigTest";

    @org.junit.BeforeClass
    public static void startUp() throws java.lang.Exception {
        java.io.File baseDir = new java.io.File(org.apache.ambari.view.pig.BasePigTest.DATA_DIRECTORY).getAbsoluteFile();
        org.apache.hadoop.fs.FileUtil.fullyDelete(baseDir);
    }

    @org.junit.AfterClass
    public static void shutDown() throws java.lang.Exception {
    }

    @org.junit.Before
    public void setUp() throws java.lang.Exception {
        handler = createNiceMock(org.apache.ambari.view.ViewResourceHandler.class);
        context = createNiceMock(org.apache.ambari.view.ViewContext.class);
        properties = new java.util.HashMap<java.lang.String, java.lang.String>();
        org.apache.ambari.view.pig.BasePigTest.baseDir = new java.io.File(org.apache.ambari.view.pig.BasePigTest.DATA_DIRECTORY).getAbsoluteFile();
        org.apache.ambari.view.pig.BasePigTest.pigStorageFile = new java.io.File("./target/PigTest/storage.dat").getAbsoluteFile();
        properties.put("dataworker.storagePath", org.apache.ambari.view.pig.BasePigTest.pigStorageFile.toString());
        properties.put("webhdfs.url", "webhdfs://host:1234");
        properties.put("webhcat.hostname", "localhost/templeton/v1");
        properties.put("webhcat.port", "50111");
        properties.put("webhcat.username", "admin");
        properties.put("scripts.dir", "/tmp/.pigscripts");
        properties.put("jobs.dir", "/tmp/.pigjobs");
        setupProperties(properties, org.apache.ambari.view.pig.BasePigTest.baseDir);
        expect(context.getProperties()).andReturn(properties).anyTimes();
        expect(context.getUsername()).andReturn("ambari-qa").anyTimes();
        expect(context.getInstanceName()).andReturn("MyPig").anyTimes();
        replay(handler, context);
    }

    protected void setupProperties(java.util.Map<java.lang.String, java.lang.String> properties, java.io.File baseDir) throws java.lang.Exception {
    }

    @org.junit.After
    public void tearDown() throws java.lang.Exception {
    }

    protected static <T> T getService(java.lang.Class<T> clazz, final org.apache.ambari.view.ViewResourceHandler viewResourceHandler, final org.apache.ambari.view.ViewContext viewInstanceContext) {
        com.google.inject.Injector viewInstanceInjector = com.google.inject.Guice.createInjector(new com.google.inject.AbstractModule() {
            @java.lang.Override
            protected void configure() {
                bind(org.apache.ambari.view.ViewResourceHandler.class).toInstance(viewResourceHandler);
                bind(org.apache.ambari.view.ViewContext.class).toInstance(viewInstanceContext);
            }
        });
        return viewInstanceInjector.getInstance(clazz);
    }
}