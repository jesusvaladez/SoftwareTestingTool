package org.apache.ambari.view.filebrowser;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataBodyPart;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileUtil;
import org.apache.hadoop.hdfs.MiniDFSCluster;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
public class FilebrowserTest {
    private org.apache.ambari.view.ViewResourceHandler handler;

    private org.apache.ambari.view.ViewContext context;

    private javax.ws.rs.core.HttpHeaders httpHeaders;

    private javax.ws.rs.core.UriInfo uriInfo;

    private java.util.Map<java.lang.String, java.lang.String> properties;

    private org.apache.ambari.view.filebrowser.FileBrowserService fileBrowserService;

    private org.apache.hadoop.hdfs.MiniDFSCluster hdfsCluster;

    public static final java.lang.String BASE_URI = "http://localhost:8084/myapp/";

    @org.junit.Before
    public void setUp() throws java.lang.Exception {
        handler = EasyMock.createNiceMock(org.apache.ambari.view.ViewResourceHandler.class);
        context = EasyMock.createNiceMock(org.apache.ambari.view.ViewContext.class);
        httpHeaders = EasyMock.createNiceMock(javax.ws.rs.core.HttpHeaders.class);
        uriInfo = EasyMock.createNiceMock(javax.ws.rs.core.UriInfo.class);
        properties = new java.util.HashMap<java.lang.String, java.lang.String>();
        java.io.File baseDir = new java.io.File("./target/hdfs/" + "FilebrowserTest").getAbsoluteFile();
        org.apache.hadoop.fs.FileUtil.fullyDelete(baseDir);
        org.apache.hadoop.conf.Configuration conf = new org.apache.hadoop.conf.Configuration();
        conf.set(MiniDFSCluster.HDFS_MINIDFS_BASEDIR, baseDir.getAbsolutePath());
        conf.set(("hadoop.proxyuser." + java.lang.System.getProperty("user.name")) + ".groups", "*");
        conf.set(("hadoop.proxyuser." + java.lang.System.getProperty("user.name")) + ".hosts", "*");
        org.apache.hadoop.hdfs.MiniDFSCluster.Builder builder = new org.apache.hadoop.hdfs.MiniDFSCluster.Builder(conf);
        hdfsCluster = builder.build();
        java.lang.String hdfsURI = hdfsCluster.getURI() + "/";
        properties.put("webhdfs.url", hdfsURI);
        EasyMock.expect(context.getProperties()).andReturn(properties).anyTimes();
        EasyMock.expect(context.getUsername()).andReturn(java.lang.System.getProperty("user.name")).anyTimes();
        EasyMock.replay(handler, context, httpHeaders, uriInfo);
        fileBrowserService = org.apache.ambari.view.filebrowser.FilebrowserTest.getService(org.apache.ambari.view.filebrowser.FileBrowserService.class, handler, context);
        org.apache.ambari.view.commons.hdfs.FileOperationService.MkdirRequest request = new org.apache.ambari.view.commons.hdfs.FileOperationService.MkdirRequest();
        request.path = "/tmp";
        fileBrowserService.fileOps().mkdir(request);
    }

    @org.junit.After
    public void tearDown() {
        hdfsCluster.shutdown();
    }

    @org.junit.Test
    public void testListDir() throws java.lang.Exception {
        org.apache.ambari.view.commons.hdfs.FileOperationService.MkdirRequest request = new org.apache.ambari.view.commons.hdfs.FileOperationService.MkdirRequest();
        request.path = "/tmp1";
        fileBrowserService.fileOps().mkdir(request);
        javax.ws.rs.core.Response response = fileBrowserService.fileOps().listdir("/", null);
        org.json.simple.JSONObject responseObject = ((org.json.simple.JSONObject) (response.getEntity()));
        org.json.simple.JSONArray statuses = ((org.json.simple.JSONArray) (responseObject.get("files")));
        java.lang.System.out.println(response.getEntity());
        org.junit.Assert.assertEquals(200, response.getStatus());
        org.junit.Assert.assertTrue(statuses.size() > 0);
        java.lang.System.out.println(statuses);
    }

    private javax.ws.rs.core.Response uploadFile(java.lang.String path, java.lang.String fileName, java.lang.String fileExtension, java.lang.String fileContent) throws java.lang.Exception {
        java.io.File tempFile = java.io.File.createTempFile(fileName, fileExtension);
        java.io.BufferedWriter bw = new java.io.BufferedWriter(new java.io.FileWriter(tempFile));
        bw.write(fileContent);
        bw.close();
        java.io.InputStream content = new java.io.FileInputStream(tempFile);
        com.sun.jersey.multipart.FormDataBodyPart inputStreamBody = new com.sun.jersey.multipart.FormDataBodyPart(com.sun.jersey.core.header.FormDataContentDisposition.name("file").fileName(fileName + fileExtension).build(), content, javax.ws.rs.core.MediaType.APPLICATION_OCTET_STREAM_TYPE);
        javax.ws.rs.core.Response response = fileBrowserService.upload().uploadFile(content, inputStreamBody.getFormDataContentDisposition(), "/tmp/");
        return response;
    }

    @org.junit.Test
    public void testUploadFile() throws java.lang.Exception {
        javax.ws.rs.core.Response response = uploadFile("/tmp/", "testUpload", ".tmp", "Hello world");
        org.junit.Assert.assertEquals(200, response.getStatus());
        javax.ws.rs.core.Response listdir = fileBrowserService.fileOps().listdir("/tmp", null);
        org.json.simple.JSONObject responseObject = ((org.json.simple.JSONObject) (listdir.getEntity()));
        org.json.simple.JSONArray statuses = ((org.json.simple.JSONArray) (responseObject.get("files")));
        java.lang.System.out.println(statuses.size());
        javax.ws.rs.core.Response response2 = fileBrowserService.download().browse("/tmp/testUpload.tmp", false, false, httpHeaders, uriInfo);
        org.junit.Assert.assertEquals(200, response2.getStatus());
    }

    private void createDirectoryWithFiles(java.lang.String dirPath) throws java.lang.Exception {
        org.apache.ambari.view.commons.hdfs.FileOperationService.MkdirRequest request = new org.apache.ambari.view.commons.hdfs.FileOperationService.MkdirRequest();
        request.path = dirPath;
        java.io.File file = new java.io.File(dirPath);
        java.lang.String fileName = file.getName();
        fileBrowserService.fileOps().mkdir(request);
        for (int i = 0; i < 10; i++) {
            uploadFile(dirPath, fileName + i, ".txt", "Hello world" + i);
        }
    }

    @org.junit.Test
    public void testStreamingGzip() throws java.lang.Exception {
        java.lang.String gzipDir = "/tmp/testGzip";
        createDirectoryWithFiles(gzipDir);
        org.apache.ambari.view.filebrowser.DownloadService.DownloadRequest dr = new org.apache.ambari.view.filebrowser.DownloadService.DownloadRequest();
        dr.entries = new java.lang.String[]{ gzipDir };
        javax.ws.rs.core.Response result = fileBrowserService.download().downloadGZip(dr);
    }

    @org.junit.Test
    public void testStreamingDownloadGzipName() throws java.lang.Exception {
        java.lang.String gzipDir = "/tmp/testGzip1";
        createDirectoryWithFiles(gzipDir);
        validateDownloadZipName(new java.lang.String[]{ gzipDir }, "testGzip1.zip");
        validateDownloadZipName(new java.lang.String[]{ gzipDir + "/testGzip11.txt" }, "testGzip11.txt.zip");
        java.lang.String gzipDir2 = "/tmp/testGzip2";
        createDirectoryWithFiles(gzipDir2);
        validateDownloadZipName(new java.lang.String[]{ gzipDir, gzipDir2 }, "hdfs.zip");
        validateDownloadZipName(new java.lang.String[]{ gzipDir + "/testGzip11", gzipDir + "/testGzip12" }, "hdfs.zip");
        validateDownloadZipName(new java.lang.String[]{ gzipDir + "/testGzip11", gzipDir2 + "/testGzip21" }, "hdfs.zip");
    }

    private void validateDownloadZipName(java.lang.String[] entries, java.lang.String downloadedFileName) {
        org.apache.ambari.view.filebrowser.DownloadService.DownloadRequest dr = new org.apache.ambari.view.filebrowser.DownloadService.DownloadRequest();
        dr.entries = entries;
        javax.ws.rs.core.Response result = fileBrowserService.download().downloadGZip(dr);
        java.util.List<java.lang.Object> contentDisposition = result.getMetadata().get("Content-Disposition");
        org.junit.Assert.assertEquals(("inline; filename=\"" + downloadedFileName) + "\"", contentDisposition.get(0));
    }

    @org.junit.Test
    public void testUsername() throws java.lang.Exception {
        org.junit.Assert.assertEquals(java.lang.System.getProperty("user.name"), fileBrowserService.upload().getDoAsUsername(context));
        properties.put("webhdfs.username", "test-user");
        org.junit.Assert.assertEquals("test-user", fileBrowserService.upload().getDoAsUsername(context));
    }

    private static <T> T getService(java.lang.Class<T> clazz, final org.apache.ambari.view.ViewResourceHandler viewResourceHandler, final org.apache.ambari.view.ViewContext viewInstanceContext) {
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