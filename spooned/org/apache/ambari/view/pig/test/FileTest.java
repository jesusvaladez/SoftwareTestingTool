package org.apache.ambari.view.pig.test;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import org.json.simple.JSONObject;
import static org.easymock.EasyMock.*;
public class FileTest extends org.apache.ambari.view.pig.HDFSTest {
    private static final int PAGINATOR_PAGE_SIZE = 4;

    private org.apache.ambari.view.pig.resources.files.FileService fileService;

    @org.junit.Rule
    public org.junit.rules.ExpectedException thrown = org.junit.rules.ExpectedException.none();

    @java.lang.Override
    @org.junit.Before
    public void setUp() throws java.lang.Exception {
        super.setUp();
        fileService = org.apache.ambari.view.pig.BasePigTest.getService(org.apache.ambari.view.pig.resources.files.FileService.class, handler, context);
        org.apache.ambari.view.pig.utils.FilePaginator.setPageSize(org.apache.ambari.view.pig.test.FileTest.PAGINATOR_PAGE_SIZE);
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

    private javax.ws.rs.core.Response doCreateFile() throws java.io.IOException, java.lang.InterruptedException {
        replay(handler, context);
        return doCreateFile("luke", "i'm your father");
    }

    private javax.ws.rs.core.Response doCreateFile(java.lang.String name, java.lang.String content) throws java.io.IOException, java.lang.InterruptedException {
        return doCreateFile(name, content, "/tmp/");
    }

    private javax.ws.rs.core.Response doCreateFile(java.lang.String name, java.lang.String content, java.lang.String filePath) throws java.io.IOException, java.lang.InterruptedException {
        org.apache.ambari.view.pig.resources.files.FileService.FileResourceRequest request = new org.apache.ambari.view.pig.resources.files.FileService.FileResourceRequest();
        request.file = new org.apache.ambari.view.pig.resources.files.FileResource();
        request.file.setFilePath(filePath + name);
        request.file.setFileContent(content);
        javax.servlet.http.HttpServletResponse resp_obj = createNiceMock(javax.servlet.http.HttpServletResponse.class);
        resp_obj.setHeader(eq("Location"), anyString());
        javax.ws.rs.core.UriInfo uriInfo = createNiceMock(javax.ws.rs.core.UriInfo.class);
        java.net.URI uri = javax.ws.rs.core.UriBuilder.fromUri("http://host/a/b").build();
        expect(uriInfo.getAbsolutePath()).andReturn(uri);
        replay(resp_obj, uriInfo);
        return fileService.createFile(request, resp_obj, uriInfo);
    }

    @org.junit.Test
    public void testCreateFile() throws java.io.IOException, java.lang.InterruptedException {
        java.lang.String name = java.util.UUID.randomUUID().toString().replaceAll("-", "");
        javax.ws.rs.core.Response response = doCreateFile(name, "12323");
        org.junit.Assert.assertEquals(204, response.getStatus());
        java.lang.String name2 = java.util.UUID.randomUUID().toString().replaceAll("-", "");
        javax.ws.rs.core.Response response2 = doCreateFile(name2, "12323");
        org.junit.Assert.assertEquals(204, response2.getStatus());
    }

    @org.junit.Test
    public void testCreateFilePathNotExists() throws java.io.IOException, java.lang.InterruptedException {
        javax.ws.rs.core.Response response = doCreateFile("Luke", null, "/non/existent/path/");
        org.junit.Assert.assertEquals(204, response.getStatus());
        javax.ws.rs.core.Response response2 = doCreateFile("Leia", null, "/tmp/");
        org.junit.Assert.assertEquals(204, response2.getStatus());
        thrown.expect(org.apache.ambari.view.pig.utils.ServiceFormattedException.class);
        javax.ws.rs.core.Response response3 = doCreateFile("Leia", null, "/tmp/");
        org.junit.Assert.assertEquals(400, response3.getStatus());
    }

    @org.junit.Test
    public void testUpdateFileContent() throws java.lang.Exception {
        java.lang.String name = java.util.UUID.randomUUID().toString().replaceAll("-", "");
        java.lang.String filePath = "/tmp/" + name;
        javax.ws.rs.core.Response createdFile = doCreateFile(name, "some content");
        org.apache.ambari.view.pig.resources.files.FileService.FileResourceRequest request = new org.apache.ambari.view.pig.resources.files.FileService.FileResourceRequest();
        request.file = new org.apache.ambari.view.pig.resources.files.FileResource();
        request.file.setFilePath(filePath);
        request.file.setFileContent("1234567890");
        javax.ws.rs.core.Response response = fileService.updateFile(request, filePath);
        org.junit.Assert.assertEquals(204, response.getStatus());
        javax.ws.rs.core.Response response2 = fileService.getFile(filePath, 0L, null);
        org.junit.Assert.assertEquals(200, response2.getStatus());
        org.json.simple.JSONObject obj = ((org.json.simple.JSONObject) (response2.getEntity()));
        org.junit.Assert.assertTrue(obj.containsKey("file"));
        org.junit.Assert.assertEquals("1234", ((org.apache.ambari.view.pig.resources.files.FileResource) (obj.get("file"))).getFileContent());
    }

    @org.junit.Test
    public void testPagination() throws java.lang.Exception {
        java.lang.String name = java.util.UUID.randomUUID().toString().replaceAll("-", "");
        java.lang.String filePath = "/tmp/" + name;
        doCreateFile(name, "1234567890");
        javax.ws.rs.core.Response response = fileService.getFile(filePath, 0L, null);
        org.junit.Assert.assertEquals(200, response.getStatus());
        org.json.simple.JSONObject obj = ((org.json.simple.JSONObject) (response.getEntity()));
        org.junit.Assert.assertTrue(obj.containsKey("file"));
        org.junit.Assert.assertEquals("1234", ((org.apache.ambari.view.pig.resources.files.FileResource) (obj.get("file"))).getFileContent());
        org.junit.Assert.assertEquals(3, ((org.apache.ambari.view.pig.resources.files.FileResource) (obj.get("file"))).getPageCount());
        org.junit.Assert.assertEquals(0, ((org.apache.ambari.view.pig.resources.files.FileResource) (obj.get("file"))).getPage());
        org.junit.Assert.assertTrue(((org.apache.ambari.view.pig.resources.files.FileResource) (obj.get("file"))).isHasNext());
        org.junit.Assert.assertEquals(filePath, ((org.apache.ambari.view.pig.resources.files.FileResource) (obj.get("file"))).getFilePath());
        response = fileService.getFile(filePath, 1L, null);
        org.junit.Assert.assertEquals(200, response.getStatus());
        obj = ((org.json.simple.JSONObject) (response.getEntity()));
        org.junit.Assert.assertEquals("5678", ((org.apache.ambari.view.pig.resources.files.FileResource) (obj.get("file"))).getFileContent());
        org.junit.Assert.assertEquals(1, ((org.apache.ambari.view.pig.resources.files.FileResource) (obj.get("file"))).getPage());
        org.junit.Assert.assertTrue(((org.apache.ambari.view.pig.resources.files.FileResource) (obj.get("file"))).isHasNext());
        response = fileService.getFile(filePath, 2L, null);
        org.junit.Assert.assertEquals(200, response.getStatus());
        obj = ((org.json.simple.JSONObject) (response.getEntity()));
        org.junit.Assert.assertEquals("90", ((org.apache.ambari.view.pig.resources.files.FileResource) (obj.get("file"))).getFileContent());
        org.junit.Assert.assertEquals(2, ((org.apache.ambari.view.pig.resources.files.FileResource) (obj.get("file"))).getPage());
        org.junit.Assert.assertFalse(((org.apache.ambari.view.pig.resources.files.FileResource) (obj.get("file"))).isHasNext());
        thrown.expect(org.apache.ambari.view.pig.utils.BadRequestFormattedException.class);
        fileService.getFile(filePath, 3L, null);
    }

    @org.junit.Test
    public void testZeroLengthFile() throws java.lang.Exception {
        java.lang.String name = java.util.UUID.randomUUID().toString().replaceAll("-", "");
        java.lang.String filePath = "/tmp/" + name;
        doCreateFile(name, "");
        javax.ws.rs.core.Response response = fileService.getFile(filePath, 0L, null);
        org.junit.Assert.assertEquals(200, response.getStatus());
        org.json.simple.JSONObject obj = ((org.json.simple.JSONObject) (response.getEntity()));
        org.junit.Assert.assertEquals("", ((org.apache.ambari.view.pig.resources.files.FileResource) (obj.get("file"))).getFileContent());
        org.junit.Assert.assertEquals(0, ((org.apache.ambari.view.pig.resources.files.FileResource) (obj.get("file"))).getPage());
        org.junit.Assert.assertFalse(((org.apache.ambari.view.pig.resources.files.FileResource) (obj.get("file"))).isHasNext());
    }

    @org.junit.Test
    public void testFileNotFound() throws java.io.IOException, java.lang.InterruptedException {
        thrown.expect(org.apache.ambari.view.pig.utils.NotFoundFormattedException.class);
        fileService.getFile("/tmp/notExistentFile", 2L, null);
    }

    @org.junit.Test
    public void testDeleteFile() throws java.io.IOException, java.lang.InterruptedException {
        java.lang.String name = java.util.UUID.randomUUID().toString().replaceAll("-", "");
        java.lang.String filePath = "/tmp/" + name;
        javax.ws.rs.core.Response createdFile = doCreateFile(name, "some content");
        javax.ws.rs.core.Response response = fileService.deleteFile(filePath);
        org.junit.Assert.assertEquals(204, response.getStatus());
        thrown.expect(org.apache.ambari.view.pig.utils.NotFoundFormattedException.class);
        fileService.getFile(filePath, 0L, null);
    }
}