package org.apache.ambari.view.pig.test;
import javax.ws.rs.core.Response;
import org.json.simple.JSONObject;
public class HelpTest extends org.apache.ambari.view.pig.HDFSTest {
    private org.apache.ambari.view.pig.services.HelpService helpService;

    @java.lang.Override
    @org.junit.Before
    public void setUp() throws java.lang.Exception {
        super.setUp();
        helpService = new org.apache.ambari.view.pig.services.HelpService(context, handler);
    }

    @org.junit.BeforeClass
    public static void startUp() throws java.lang.Exception {
        org.apache.ambari.view.pig.HDFSTest.startUp();
    }

    @org.junit.AfterClass
    public static void shutDown() throws java.lang.Exception {
        org.apache.ambari.view.pig.HDFSTest.shutDown();
        org.apache.ambari.view.utils.UserLocal.dropAllConnections(org.apache.ambari.view.utils.hdfs.HdfsApi.class);
    }

    @org.junit.Test
    public void configTest() {
        javax.ws.rs.core.Response response = helpService.config();
        org.junit.Assert.assertEquals(200, response.getStatus());
        org.json.simple.JSONObject obj = ((org.json.simple.JSONObject) (response.getEntity()));
        org.junit.Assert.assertTrue(obj.containsKey("webhdfs.url"));
        org.junit.Assert.assertEquals(org.apache.ambari.view.pig.HDFSTest.hdfsURI, obj.get("webhdfs.url"));
    }
}