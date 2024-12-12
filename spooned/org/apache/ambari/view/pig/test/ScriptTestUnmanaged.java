package org.apache.ambari.view.pig.test;
import javax.ws.rs.core.Response;
import static org.easymock.EasyMock.*;
public class ScriptTestUnmanaged extends org.apache.ambari.view.pig.BasePigTest {
    @org.junit.Rule
    public org.junit.rules.ExpectedException thrown = org.junit.rules.ExpectedException.none();

    private org.apache.ambari.view.pig.resources.scripts.ScriptService scriptService;

    private java.io.File pigStorageFile;

    private java.io.File baseDir;

    @org.junit.AfterClass
    public static void shutDown() throws java.lang.Exception {
        org.apache.ambari.view.utils.UserLocal.dropAllConnections(org.apache.ambari.view.utils.hdfs.HdfsApi.class);
    }

    @org.junit.Before
    public void setUp() throws java.lang.Exception {
        handler = createNiceMock(org.apache.ambari.view.ViewResourceHandler.class);
        context = createNiceMock(org.apache.ambari.view.ViewContext.class);
        baseDir = new java.io.File(org.apache.ambari.view.pig.BasePigTest.DATA_DIRECTORY).getAbsoluteFile();
        pigStorageFile = new java.io.File("./target/BasePigTest/storage.dat").getAbsoluteFile();
    }

    private javax.ws.rs.core.Response doCreateScript(java.lang.String title, java.lang.String path) {
        return org.apache.ambari.view.pig.test.ScriptTest.doCreateScript(title, path, scriptService);
    }

    @org.junit.Test
    public void createScriptAutoCreateNoDefaultFS() {
        java.util.Map<java.lang.String, java.lang.String> properties = new java.util.HashMap<java.lang.String, java.lang.String>();
        properties.put("dataworker.storagePath", pigStorageFile.toString());
        properties.put("scripts.dir", "/tmp/.pigscripts");
        expect(context.getProperties()).andReturn(properties).anyTimes();
        expect(context.getUsername()).andReturn("ambari-qa").anyTimes();
        replay(handler, context);
        scriptService = org.apache.ambari.view.pig.BasePigTest.getService(org.apache.ambari.view.pig.resources.scripts.ScriptService.class, handler, context);
        thrown.expect(org.apache.ambari.view.pig.utils.ServiceFormattedException.class);
        doCreateScript("Test", null);
    }
}