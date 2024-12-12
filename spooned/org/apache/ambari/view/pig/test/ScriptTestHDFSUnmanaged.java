package org.apache.ambari.view.pig.test;
import javax.ws.rs.core.Response;
import org.json.simple.JSONObject;
import static org.easymock.EasyMock.*;
public class ScriptTestHDFSUnmanaged extends org.apache.ambari.view.pig.HDFSTest {
    private org.apache.ambari.view.pig.resources.scripts.ScriptService scriptService;

    @org.junit.Rule
    public org.junit.rules.ExpectedException thrown = org.junit.rules.ExpectedException.none();

    @org.junit.BeforeClass
    public static void startUp() throws java.lang.Exception {
        org.apache.ambari.view.pig.HDFSTest.startUp();
    }

    @org.junit.AfterClass
    public static void shutDown() throws java.lang.Exception {
        org.apache.ambari.view.pig.HDFSTest.shutDown();
        org.apache.ambari.view.utils.UserLocal.dropAllConnections(org.apache.ambari.view.utils.hdfs.HdfsApi.class);
    }

    @java.lang.Override
    @org.junit.Before
    public void setUp() throws java.lang.Exception {
        super.setUp();
        handler = createNiceMock(org.apache.ambari.view.ViewResourceHandler.class);
        context = createNiceMock(org.apache.ambari.view.ViewContext.class);
        org.apache.ambari.view.utils.UserLocal.dropAllConnections(org.apache.ambari.view.utils.hdfs.HdfsApi.class);
        org.apache.ambari.view.pig.persistence.utils.StorageUtil.dropAllConnections();
    }

    @org.junit.Test
    public void createScriptAutoCreateNoScriptsPath() throws java.io.IOException, java.lang.InterruptedException {
        java.util.Map<java.lang.String, java.lang.String> properties = new java.util.HashMap<java.lang.String, java.lang.String>();
        org.apache.ambari.view.pig.BasePigTest.baseDir = new java.io.File(org.apache.ambari.view.pig.BasePigTest.DATA_DIRECTORY).getAbsoluteFile();
        org.apache.ambari.view.pig.BasePigTest.pigStorageFile = new java.io.File("./target/BasePigTest/storage.dat").getAbsoluteFile();
        properties.put("dataworker.storagePath", org.apache.ambari.view.pig.BasePigTest.pigStorageFile.toString());
        properties.put("webhdfs.url", org.apache.ambari.view.pig.HDFSTest.hdfsURI);
        expect(context.getProperties()).andReturn(properties).anyTimes();
        expect(context.getUsername()).andReturn("ambari-qa").anyTimes();
        replay(handler, context);
        scriptService = org.apache.ambari.view.pig.BasePigTest.getService(org.apache.ambari.view.pig.resources.scripts.ScriptService.class, handler, context);
        thrown.expect(org.apache.ambari.view.pig.utils.MisconfigurationFormattedException.class);
        doCreateScript("Test", null);
    }

    @org.junit.Test
    public void createScriptAutoCreateUsername() throws java.io.IOException, java.lang.InterruptedException {
        java.util.Map<java.lang.String, java.lang.String> properties = new java.util.HashMap<java.lang.String, java.lang.String>();
        org.apache.ambari.view.pig.BasePigTest.baseDir = new java.io.File(org.apache.ambari.view.pig.BasePigTest.DATA_DIRECTORY).getAbsoluteFile();
        org.apache.ambari.view.pig.BasePigTest.pigStorageFile = new java.io.File("./target/BasePigTest/storage.dat").getAbsoluteFile();
        properties.put("dataworker.storagePath", org.apache.ambari.view.pig.BasePigTest.pigStorageFile.toString());
        properties.put("scripts.dir", "/tmp/.pigscripts");
        properties.put("webhdfs.url", org.apache.ambari.view.pig.HDFSTest.hdfsURI);
        expect(context.getProperties()).andReturn(properties).anyTimes();
        expect(context.getUsername()).andReturn("ambari-qa").anyTimes();
        replay(handler, context);
        scriptService = org.apache.ambari.view.pig.BasePigTest.getService(org.apache.ambari.view.pig.resources.scripts.ScriptService.class, handler, context);
        javax.ws.rs.core.Response createdScript = doCreateScript("Test", null);
        java.lang.String createdScriptPath = ((org.apache.ambari.view.pig.resources.scripts.models.PigScript) (((org.json.simple.JSONObject) (createdScript.getEntity())).get("script"))).getPigScript();
        org.junit.Assert.assertTrue(createdScriptPath.startsWith("/tmp/.pigscripts/"));
        properties.put("dataworker.username", "luke");
        javax.ws.rs.core.Response createdScript2 = doCreateScript("Test", null);
        java.lang.String createdScriptPath2 = ((org.apache.ambari.view.pig.resources.scripts.models.PigScript) (((org.json.simple.JSONObject) (createdScript2.getEntity())).get("script"))).getPigScript();
        org.junit.Assert.assertTrue(createdScriptPath2.startsWith("/tmp/.pigscripts/"));
    }

    @org.junit.Test
    public void createScriptAutoCreateNoStoragePath() throws java.io.IOException, java.lang.InterruptedException {
        java.util.Map<java.lang.String, java.lang.String> properties = new java.util.HashMap<java.lang.String, java.lang.String>();
        org.apache.ambari.view.pig.BasePigTest.baseDir = new java.io.File(org.apache.ambari.view.pig.BasePigTest.DATA_DIRECTORY).getAbsoluteFile();
        org.apache.ambari.view.pig.BasePigTest.pigStorageFile = new java.io.File("./target/BasePigTest/storage.dat").getAbsoluteFile();
        properties.put("scripts.dir", "/tmp/.pigscripts");
        properties.put("webhdfs.url", org.apache.ambari.view.pig.HDFSTest.hdfsURI);
        expect(context.getProperties()).andReturn(properties).anyTimes();
        expect(context.getUsername()).andReturn("ambari-qa").anyTimes();
        replay(handler, context);
        org.apache.ambari.view.pig.persistence.Storage storage = org.apache.ambari.view.pig.persistence.utils.StorageUtil.getInstance(context).getStorage();
        org.junit.Assert.assertEquals(org.apache.ambari.view.pig.persistence.DataStoreStorage.class.getSimpleName(), storage.getClass().getSimpleName());
    }

    @org.junit.Test
    public void hdfsApiNoUsernameProvided() throws java.io.IOException, java.lang.InterruptedException {
        java.util.Map<java.lang.String, java.lang.String> properties = new java.util.HashMap<java.lang.String, java.lang.String>();
        properties.put("webhdfs.url", org.apache.ambari.view.pig.HDFSTest.hdfsURI);
        expect(context.getProperties()).andReturn(properties).anyTimes();
        expect(context.getUsername()).andReturn("ambari-qa").anyTimes();
        replay(context);
        org.junit.Assert.assertEquals("ambari-qa", org.apache.ambari.view.utils.hdfs.HdfsUtil.getHdfsUsername(context));
        properties.put("webhdfs.username", "luke");
        org.junit.Assert.assertEquals("luke", org.apache.ambari.view.utils.hdfs.HdfsUtil.getHdfsUsername(context));
    }

    private javax.ws.rs.core.Response doCreateScript(java.lang.String title, java.lang.String path) {
        return org.apache.ambari.view.pig.test.ScriptTest.doCreateScript(title, path, scriptService);
    }
}