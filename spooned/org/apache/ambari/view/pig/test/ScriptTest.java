package org.apache.ambari.view.pig.test;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import org.json.simple.JSONObject;
import static org.easymock.EasyMock.*;
public class ScriptTest extends org.apache.ambari.view.pig.HDFSTest {
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
        scriptService = org.apache.ambari.view.pig.BasePigTest.getService(org.apache.ambari.view.pig.resources.scripts.ScriptService.class, handler, context);
    }

    @java.lang.Override
    protected void setupProperties(java.util.Map<java.lang.String, java.lang.String> properties, java.io.File baseDir) throws java.lang.Exception {
        super.setupProperties(properties, baseDir);
        properties.put("scripts.dir", "/tmp/.pigscripts");
    }

    private javax.ws.rs.core.Response doCreateScript() {
        return org.apache.ambari.view.pig.test.ScriptTest.doCreateScript("Luke", "/tmp/luke.pig", scriptService);
    }

    public static javax.ws.rs.core.Response doCreateScript(java.lang.String title, java.lang.String path, org.apache.ambari.view.pig.resources.scripts.ScriptService scriptService) {
        org.apache.ambari.view.pig.resources.scripts.ScriptService.PigScriptRequest request = new org.apache.ambari.view.pig.resources.scripts.ScriptService.PigScriptRequest();
        request.script = new org.apache.ambari.view.pig.resources.scripts.models.PigScript();
        request.script.setTitle(title);
        request.script.setPigScript(path);
        javax.ws.rs.core.UriInfo uriInfo = createNiceMock(javax.ws.rs.core.UriInfo.class);
        java.net.URI uri = javax.ws.rs.core.UriBuilder.fromUri("http://host/a/b").build();
        expect(uriInfo.getAbsolutePath()).andReturn(uri);
        javax.servlet.http.HttpServletResponse resp_obj = createNiceMock(javax.servlet.http.HttpServletResponse.class);
        resp_obj.setHeader(eq("Location"), anyString());
        replay(uriInfo, resp_obj);
        return scriptService.saveScript(request, resp_obj, uriInfo);
    }

    private javax.ws.rs.core.Response doCreateScript(java.lang.String title, java.lang.String path) {
        return org.apache.ambari.view.pig.test.ScriptTest.doCreateScript(title, path, scriptService);
    }

    @org.junit.Test
    public void createScript() {
        javax.ws.rs.core.Response response = doCreateScript();
        org.junit.Assert.assertEquals(201, response.getStatus());
        org.json.simple.JSONObject obj = ((org.json.simple.JSONObject) (response.getEntity()));
        org.junit.Assert.assertTrue(obj.containsKey("script"));
        org.junit.Assert.assertNotNull(((org.apache.ambari.view.pig.resources.scripts.models.PigScript) (obj.get("script"))).getId());
        org.junit.Assert.assertFalse(((org.apache.ambari.view.pig.resources.scripts.models.PigScript) (obj.get("script"))).getId().isEmpty());
    }

    @org.junit.Test
    public void createScriptAutoCreate() {
        javax.ws.rs.core.Response response = doCreateScript("Test", null);
        org.junit.Assert.assertEquals(201, response.getStatus());
        org.json.simple.JSONObject obj = ((org.json.simple.JSONObject) (response.getEntity()));
        org.junit.Assert.assertTrue(obj.containsKey("script"));
        org.junit.Assert.assertNotNull(((org.apache.ambari.view.pig.resources.scripts.models.PigScript) (obj.get("script"))).getId());
        org.junit.Assert.assertFalse(((org.apache.ambari.view.pig.resources.scripts.models.PigScript) (obj.get("script"))).getId().isEmpty());
        org.junit.Assert.assertFalse(((org.apache.ambari.view.pig.resources.scripts.models.PigScript) (obj.get("script"))).getPigScript().isEmpty());
    }

    @org.junit.Test
    public void scriptNotFound() {
        thrown.expect(org.apache.ambari.view.pig.utils.NotFoundFormattedException.class);
        scriptService.getScript("4242");
    }

    @org.junit.Test
    public void updateScript() {
        javax.ws.rs.core.Response createdScript = doCreateScript();
        java.lang.String createdScriptId = ((org.apache.ambari.view.pig.resources.scripts.models.PigScript) (((org.json.simple.JSONObject) (createdScript.getEntity())).get("script"))).getId();
        org.apache.ambari.view.pig.resources.scripts.ScriptService.PigScriptRequest request = new org.apache.ambari.view.pig.resources.scripts.ScriptService.PigScriptRequest();
        request.script = new org.apache.ambari.view.pig.resources.scripts.models.PigScript();
        request.script.setTitle("Updated Script");
        javax.ws.rs.core.Response response = scriptService.updateScript(request, createdScriptId);
        org.junit.Assert.assertEquals(204, response.getStatus());
        javax.ws.rs.core.Response response2 = scriptService.getScript(createdScriptId);
        org.junit.Assert.assertEquals(200, response2.getStatus());
        org.json.simple.JSONObject obj = ((org.json.simple.JSONObject) (response2.getEntity()));
        org.junit.Assert.assertTrue(obj.containsKey("script"));
        org.junit.Assert.assertEquals(((org.apache.ambari.view.pig.resources.scripts.models.PigScript) (obj.get("script"))).getTitle(), request.script.getTitle());
    }

    @org.junit.Test
    public void deleteScript() {
        javax.ws.rs.core.Response createdScript = doCreateScript();
        java.lang.String createdScriptId = ((org.apache.ambari.view.pig.resources.scripts.models.PigScript) (((org.json.simple.JSONObject) (createdScript.getEntity())).get("script"))).getId();
        javax.ws.rs.core.Response response = scriptService.deleteScript(createdScriptId);
        org.junit.Assert.assertEquals(204, response.getStatus());
        thrown.expect(org.apache.ambari.view.pig.utils.NotFoundFormattedException.class);
        scriptService.getScript(createdScriptId);
    }

    @org.junit.Test
    public void listScripts() {
        javax.ws.rs.core.Response createdScript1 = doCreateScript("Title 1", "/path/to/file.pig");
        javax.ws.rs.core.Response createdScript2 = doCreateScript("Title 2", "/path/to/file.pig");
        java.lang.String createdScriptId = ((org.apache.ambari.view.pig.resources.scripts.models.PigScript) (((org.json.simple.JSONObject) (createdScript1.getEntity())).get("script"))).getId();
        javax.ws.rs.core.Response response = scriptService.getScriptList();
        org.junit.Assert.assertEquals(200, response.getStatus());
        org.json.simple.JSONObject obj = ((org.json.simple.JSONObject) (response.getEntity()));
        org.junit.Assert.assertTrue(obj.containsKey("scripts"));
        java.util.List<org.apache.ambari.view.pig.resources.scripts.models.PigScript> scripts = ((java.util.List<org.apache.ambari.view.pig.resources.scripts.models.PigScript>) (obj.get("scripts")));
        boolean containsTitle = false;
        for (org.apache.ambari.view.pig.resources.scripts.models.PigScript script : scripts)
            containsTitle = containsTitle || (script.getTitle().compareTo("Title 1") == 0);

        org.junit.Assert.assertTrue(containsTitle);
        containsTitle = false;
        for (org.apache.ambari.view.pig.resources.scripts.models.PigScript script : scripts)
            containsTitle = containsTitle || (script.getTitle().compareTo("Title 2") == 0);

        org.junit.Assert.assertTrue(containsTitle);
    }
}