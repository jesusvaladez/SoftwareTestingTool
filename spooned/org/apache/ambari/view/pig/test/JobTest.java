package org.apache.ambari.view.pig.test;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.easymock.EasyMock;
import org.json.simple.JSONObject;
import static org.easymock.EasyMock.*;
public class JobTest extends org.apache.ambari.view.pig.BasePigTest {
    private org.apache.ambari.view.pig.resources.jobs.JobService jobService;

    @org.junit.Rule
    public org.junit.rules.ExpectedException thrown = org.junit.rules.ExpectedException.none();

    @org.junit.BeforeClass
    public static void startUp() throws java.lang.Exception {
        org.apache.ambari.view.pig.BasePigTest.startUp();
    }

    @org.junit.AfterClass
    public static void shutDown() throws java.lang.Exception {
        org.apache.ambari.view.pig.BasePigTest.shutDown();
    }

    @java.lang.Override
    @org.junit.Before
    public void setUp() throws java.lang.Exception {
        super.setUp();
        jobService = org.apache.ambari.view.pig.BasePigTest.getService(org.apache.ambari.view.pig.resources.jobs.JobService.class, handler, context);
    }

    @java.lang.Override
    @org.junit.After
    public void tearDown() throws java.lang.Exception {
        super.tearDown();
        org.apache.ambari.view.utils.UserLocal.dropAllConnections(org.apache.ambari.view.pig.templeton.client.TempletonApi.class);
        org.apache.ambari.view.utils.UserLocal.dropAllConnections(org.apache.ambari.view.utils.hdfs.HdfsApi.class);
    }

    public static javax.ws.rs.core.Response doCreateJob(java.lang.String title, java.lang.String pigScript, java.lang.String templetonArguments, org.apache.ambari.view.pig.resources.jobs.JobService jobService) {
        return org.apache.ambari.view.pig.test.JobTest.doCreateJob(title, pigScript, templetonArguments, null, null, jobService);
    }

    public static javax.ws.rs.core.Response doCreateJob(java.lang.String title, java.lang.String pigScript, java.lang.String templetonArguments, java.lang.String forcedContent, java.lang.String scriptId, org.apache.ambari.view.pig.resources.jobs.JobService jobService) {
        org.apache.ambari.view.pig.resources.jobs.JobService.PigJobRequest request = new org.apache.ambari.view.pig.resources.jobs.JobService.PigJobRequest();
        request.job = new org.apache.ambari.view.pig.resources.jobs.models.PigJob();
        request.job.setTitle(title);
        request.job.setPigScript(pigScript);
        request.job.setTempletonArguments(templetonArguments);
        request.job.setForcedContent(forcedContent);
        request.job.setScriptId(scriptId);
        javax.ws.rs.core.UriInfo uriInfo = createNiceMock(javax.ws.rs.core.UriInfo.class);
        java.net.URI uri = javax.ws.rs.core.UriBuilder.fromUri("http://host/a/b").build();
        expect(uriInfo.getAbsolutePath()).andReturn(uri);
        javax.servlet.http.HttpServletResponse resp_obj = createStrictMock(javax.servlet.http.HttpServletResponse.class);
        resp_obj.setHeader(eq("Location"), anyString());
        replay(uriInfo, resp_obj);
        return jobService.runJob(request, resp_obj, uriInfo);
    }

    @org.junit.Test
    public void testSubmitJob() throws java.lang.Exception {
        org.apache.ambari.view.utils.hdfs.HdfsApi hdfsApi = createNiceMock(org.apache.ambari.view.utils.hdfs.HdfsApi.class);
        hdfsApi.copy(eq("/tmp/script.pig"), startsWith("/tmp/.pigjobs/"));
        java.io.ByteArrayOutputStream do_stream = new java.io.ByteArrayOutputStream();
        org.apache.hadoop.fs.FSDataOutputStream stream = new org.apache.hadoop.fs.FSDataOutputStream(do_stream, null);
        expect(hdfsApi.create(anyString(), eq(true))).andReturn(stream);
        replay(hdfsApi);
        org.apache.ambari.view.pig.utils.UserLocalObjects.setHdfsApi(hdfsApi, context);
        org.apache.ambari.view.pig.templeton.client.TempletonApi api = createNiceMock(org.apache.ambari.view.pig.templeton.client.TempletonApi.class);
        org.apache.ambari.view.pig.utils.UserLocalObjects.setTempletonApi(api, context);
        org.apache.ambari.view.pig.templeton.client.TempletonApi.JobData data = api.new JobData();
        data.id = "job_1466418324742_0005";
        expect(api.runPigQuery(((java.io.File) (anyObject())), anyString(), eq("-useHCatalog"))).andReturn(data);
        replay(api);
        javax.ws.rs.core.Response response = org.apache.ambari.view.pig.test.JobTest.doCreateJob("Test", "/tmp/script.pig", "-useHCatalog", jobService);
        org.junit.Assert.assertEquals("-useHCatalog", do_stream.toString());
        org.junit.Assert.assertEquals(201, response.getStatus());
        org.json.simple.JSONObject obj = ((org.json.simple.JSONObject) (response.getEntity()));
        org.junit.Assert.assertTrue(obj.containsKey("job"));
        org.junit.Assert.assertNotNull(((org.apache.ambari.view.pig.resources.jobs.models.PigJob) (obj.get("job"))).getId());
        org.junit.Assert.assertFalse(((org.apache.ambari.view.pig.resources.jobs.models.PigJob) (obj.get("job"))).getId().isEmpty());
        org.junit.Assert.assertTrue(((org.apache.ambari.view.pig.resources.jobs.models.PigJob) (obj.get("job"))).getStatusDir().startsWith("/tmp/.pigjobs/test"));
        org.apache.ambari.view.pig.resources.jobs.models.PigJob job = ((org.apache.ambari.view.pig.resources.jobs.models.PigJob) (obj.get("job")));
        org.junit.Assert.assertEquals(org.apache.ambari.view.pig.resources.jobs.models.PigJob.PIG_JOB_STATE_SUBMITTED, job.getStatus());
        org.junit.Assert.assertTrue(job.isInProgress());
    }

    @org.junit.Test
    public void testListJobs() throws java.lang.Exception {
        org.apache.ambari.view.utils.hdfs.HdfsApi hdfsApi = createNiceMock(org.apache.ambari.view.utils.hdfs.HdfsApi.class);
        hdfsApi.copy(eq("/tmp/script.pig"), startsWith("/tmp/.pigjobs/"));
        java.io.ByteArrayOutputStream do_stream = new java.io.ByteArrayOutputStream();
        org.apache.hadoop.fs.FSDataOutputStream stream = new org.apache.hadoop.fs.FSDataOutputStream(do_stream, null);
        expect(hdfsApi.create(anyString(), eq(true))).andReturn(stream).anyTimes();
        replay(hdfsApi);
        org.apache.ambari.view.pig.utils.UserLocalObjects.setHdfsApi(hdfsApi, context);
        org.apache.ambari.view.pig.templeton.client.TempletonApi api = createNiceMock(org.apache.ambari.view.pig.templeton.client.TempletonApi.class);
        org.apache.ambari.view.pig.utils.UserLocalObjects.setTempletonApi(api, context);
        org.apache.ambari.view.pig.templeton.client.TempletonApi.JobData data = api.new JobData();
        data.id = "job_1466418324742_0005";
        expect(api.runPigQuery(((java.io.File) (anyObject())), anyString(), ((java.lang.String) (isNull())))).andReturn(data).anyTimes();
        replay(api);
        javax.ws.rs.core.Response response = org.apache.ambari.view.pig.test.JobTest.doCreateJob("Test", "/tmp/script.pig", null, null, "x42", jobService);
        org.junit.Assert.assertEquals(201, response.getStatus());
        response = org.apache.ambari.view.pig.test.JobTest.doCreateJob("Test", "/tmp/script.pig", null, null, "x42", jobService);
        org.junit.Assert.assertEquals(201, response.getStatus());
        response = org.apache.ambari.view.pig.test.JobTest.doCreateJob("Test", "/tmp/script.pig", null, null, "100", jobService);
        org.junit.Assert.assertEquals(201, response.getStatus());
        response = jobService.getJobList("x42");
        org.junit.Assert.assertEquals(200, response.getStatus());
        org.json.simple.JSONObject obj = ((org.json.simple.JSONObject) (response.getEntity()));
        org.junit.Assert.assertTrue(obj.containsKey("jobs"));
        org.junit.Assert.assertEquals(2, ((java.util.List) (obj.get("jobs"))).size());
        response = jobService.getJobList(null);
        org.junit.Assert.assertEquals(200, response.getStatus());
        obj = ((org.json.simple.JSONObject) (response.getEntity()));
        org.junit.Assert.assertTrue(obj.containsKey("jobs"));
        org.junit.Assert.assertTrue(((java.util.List) (obj.get("jobs"))).size() > 2);
    }

    @org.junit.Test
    public void testSubmitJobUsernameProvided() throws java.lang.Exception {
        org.apache.ambari.view.utils.hdfs.HdfsApi hdfsApi = createNiceMock(org.apache.ambari.view.utils.hdfs.HdfsApi.class);
        hdfsApi.copy(eq("/tmp/script.pig"), startsWith("/tmp/.pigjobs/"));
        java.io.ByteArrayOutputStream do_stream = new java.io.ByteArrayOutputStream();
        org.apache.hadoop.fs.FSDataOutputStream stream = new org.apache.hadoop.fs.FSDataOutputStream(do_stream, null);
        expect(hdfsApi.create(anyString(), eq(true))).andReturn(stream);
        replay(hdfsApi);
        org.apache.ambari.view.pig.utils.UserLocalObjects.setHdfsApi(hdfsApi, context);
        org.apache.ambari.view.pig.templeton.client.TempletonApi api = createNiceMock(org.apache.ambari.view.pig.templeton.client.TempletonApi.class);
        org.apache.ambari.view.pig.utils.UserLocalObjects.setTempletonApi(api, context);
        org.apache.ambari.view.pig.templeton.client.TempletonApi.JobData data = api.new JobData();
        data.id = "job_1466418324742_0005";
        expect(api.runPigQuery(((java.io.File) (anyObject())), anyString(), eq("-useHCatalog"))).andReturn(data);
        replay(api);
        properties.put("dataworker.username", "luke");
        javax.ws.rs.core.Response response = org.apache.ambari.view.pig.test.JobTest.doCreateJob("Test", "/tmp/script.pig", "-useHCatalog", jobService);
        org.json.simple.JSONObject obj = ((org.json.simple.JSONObject) (response.getEntity()));
        org.junit.Assert.assertTrue(obj.containsKey("job"));
        org.junit.Assert.assertTrue(((org.apache.ambari.view.pig.resources.jobs.models.PigJob) (obj.get("job"))).getStatusDir().startsWith("/tmp/.pigjobs/test"));
    }

    @org.junit.Test
    public void testSubmitJobNoArguments() throws java.lang.Exception {
        org.apache.ambari.view.utils.hdfs.HdfsApi hdfsApi = createNiceMock(org.apache.ambari.view.utils.hdfs.HdfsApi.class);
        hdfsApi.copy(eq("/tmp/script.pig"), startsWith("/tmp/.pigjobs/"));
        java.io.ByteArrayOutputStream do_stream = new java.io.ByteArrayOutputStream();
        org.apache.hadoop.fs.FSDataOutputStream stream = new org.apache.hadoop.fs.FSDataOutputStream(do_stream, null);
        expect(hdfsApi.create(anyString(), eq(true))).andReturn(stream);
        replay(hdfsApi);
        org.apache.ambari.view.pig.utils.UserLocalObjects.setHdfsApi(hdfsApi, context);
        org.apache.ambari.view.pig.templeton.client.TempletonApi api = createNiceMock(org.apache.ambari.view.pig.templeton.client.TempletonApi.class);
        org.apache.ambari.view.pig.utils.UserLocalObjects.setTempletonApi(api, context);
        org.apache.ambari.view.pig.templeton.client.TempletonApi.JobData data = api.new JobData();
        data.id = "job_1466418324742_0005";
        expect(api.runPigQuery(((java.io.File) (anyObject())), anyString(), ((java.lang.String) (isNull())))).andReturn(data);
        replay(api);
        javax.ws.rs.core.Response response = org.apache.ambari.view.pig.test.JobTest.doCreateJob("Test", "/tmp/script.pig", null, jobService);
        org.junit.Assert.assertEquals("", do_stream.toString());
        org.junit.Assert.assertEquals(201, response.getStatus());
        org.json.simple.JSONObject obj = ((org.json.simple.JSONObject) (response.getEntity()));
        org.junit.Assert.assertTrue(obj.containsKey("job"));
        org.junit.Assert.assertNotNull(((org.apache.ambari.view.pig.resources.jobs.models.PigJob) (obj.get("job"))).getId());
        org.junit.Assert.assertFalse(((org.apache.ambari.view.pig.resources.jobs.models.PigJob) (obj.get("job"))).getId().isEmpty());
        org.junit.Assert.assertTrue(((org.apache.ambari.view.pig.resources.jobs.models.PigJob) (obj.get("job"))).getStatusDir().startsWith("/tmp/.pigjobs/test"));
        org.apache.ambari.view.pig.resources.jobs.models.PigJob job = ((org.apache.ambari.view.pig.resources.jobs.models.PigJob) (obj.get("job")));
        org.junit.Assert.assertEquals(org.apache.ambari.view.pig.resources.jobs.models.PigJob.PIG_JOB_STATE_SUBMITTED, job.getStatus());
        org.junit.Assert.assertTrue(job.isInProgress());
    }

    @org.junit.Test
    public void testSubmitJobNoFile() throws java.lang.Exception {
        org.apache.ambari.view.utils.hdfs.HdfsApi hdfsApi = createNiceMock(org.apache.ambari.view.utils.hdfs.HdfsApi.class);
        hdfsApi.copy(eq("/tmp/script.pig"), startsWith("/tmp/.pigjobs/"));
        java.io.ByteArrayOutputStream do_stream = new java.io.ByteArrayOutputStream();
        org.apache.hadoop.fs.FSDataOutputStream stream = new org.apache.hadoop.fs.FSDataOutputStream(do_stream, null);
        expect(hdfsApi.create(anyString(), eq(true))).andReturn(stream);
        replay(hdfsApi);
        org.apache.ambari.view.pig.utils.UserLocalObjects.setHdfsApi(hdfsApi, context);
        org.apache.ambari.view.pig.templeton.client.TempletonApi api = createNiceMock(org.apache.ambari.view.pig.templeton.client.TempletonApi.class);
        org.apache.ambari.view.pig.utils.UserLocalObjects.setTempletonApi(api, context);
        org.apache.ambari.view.pig.templeton.client.TempletonApi.JobData data = api.new JobData();
        expect(api.runPigQuery(((java.io.File) (anyObject())), anyString(), eq("-useHCatalog"))).andReturn(data);
        replay(api);
        thrown.expect(org.apache.ambari.view.pig.utils.ServiceFormattedException.class);
        org.apache.ambari.view.pig.test.JobTest.doCreateJob("Test", null, "-useHCatalog", jobService);
    }

    @org.junit.Test
    public void testSubmitJobForcedContent() throws java.lang.Exception {
        org.apache.ambari.view.utils.hdfs.HdfsApi hdfsApi = createNiceMock(org.apache.ambari.view.utils.hdfs.HdfsApi.class);
        java.io.ByteArrayOutputStream baScriptStream = new java.io.ByteArrayOutputStream();
        java.io.ByteArrayOutputStream baTempletonArgsStream = new java.io.ByteArrayOutputStream();
        org.apache.hadoop.fs.FSDataOutputStream scriptStream = new org.apache.hadoop.fs.FSDataOutputStream(baScriptStream, null);
        org.apache.hadoop.fs.FSDataOutputStream templetonArgsStream = new org.apache.hadoop.fs.FSDataOutputStream(baTempletonArgsStream, null);
        expect(hdfsApi.create(endsWith("script.pig"), eq(true))).andReturn(scriptStream);
        expect(hdfsApi.create(endsWith("params"), eq(true))).andReturn(templetonArgsStream);
        replay(hdfsApi);
        org.apache.ambari.view.pig.utils.UserLocalObjects.setHdfsApi(hdfsApi, context);
        org.apache.ambari.view.pig.templeton.client.TempletonApi api = createNiceMock(org.apache.ambari.view.pig.templeton.client.TempletonApi.class);
        org.apache.ambari.view.pig.utils.UserLocalObjects.setTempletonApi(api, context);
        org.apache.ambari.view.pig.templeton.client.TempletonApi.JobData data = api.new JobData();
        data.id = "job_1466418324742_0005";
        expect(api.runPigQuery(((java.io.File) (anyObject())), anyString(), eq("-useHCatalog"))).andReturn(data);
        replay(api);
        javax.ws.rs.core.Response response = org.apache.ambari.view.pig.test.JobTest.doCreateJob("Test", null, "-useHCatalog", "pwd", null, jobService);
        org.junit.Assert.assertEquals(201, response.getStatus());
        org.junit.Assert.assertEquals("-useHCatalog", baTempletonArgsStream.toString());
        org.junit.Assert.assertEquals("pwd", baScriptStream.toString());
    }

    @org.junit.Test
    public void testSubmitJobNoTitle() throws java.lang.Exception {
        org.apache.ambari.view.utils.hdfs.HdfsApi hdfsApi = createNiceMock(org.apache.ambari.view.utils.hdfs.HdfsApi.class);
        hdfsApi.copy(eq("/tmp/script.pig"), startsWith("/tmp/.pigjobs/"));
        java.io.ByteArrayOutputStream do_stream = new java.io.ByteArrayOutputStream();
        org.apache.hadoop.fs.FSDataOutputStream stream = new org.apache.hadoop.fs.FSDataOutputStream(do_stream, null);
        expect(hdfsApi.create(anyString(), eq(true))).andReturn(stream);
        replay(hdfsApi);
        org.apache.ambari.view.pig.utils.UserLocalObjects.setHdfsApi(hdfsApi, context);
        org.apache.ambari.view.pig.templeton.client.TempletonApi api = createNiceMock(org.apache.ambari.view.pig.templeton.client.TempletonApi.class);
        org.apache.ambari.view.pig.utils.UserLocalObjects.setTempletonApi(api, context);
        org.apache.ambari.view.pig.templeton.client.TempletonApi.JobData data = api.new JobData();
        expect(api.runPigQuery(((java.io.File) (anyObject())), anyString(), eq("-useHCatalog"))).andReturn(data);
        replay(api);
        thrown.expect(org.apache.ambari.view.pig.utils.BadRequestFormattedException.class);
        org.apache.ambari.view.pig.test.JobTest.doCreateJob(null, "/tmp/1.pig", "-useHCatalog", jobService);
    }

    @org.junit.Test
    public void testSubmitJobFailed() throws java.lang.Exception {
        org.apache.ambari.view.utils.hdfs.HdfsApi hdfsApi = createNiceMock(org.apache.ambari.view.utils.hdfs.HdfsApi.class);
        hdfsApi.copy(eq("/tmp/script.pig"), startsWith("/tmp/.pigjobs/"));
        org.easymock.EasyMock.expectLastCall().andThrow(new org.apache.ambari.view.utils.hdfs.HdfsApiException("Copy failed"));
        java.io.ByteArrayOutputStream do_stream = new java.io.ByteArrayOutputStream();
        org.apache.hadoop.fs.FSDataOutputStream stream = new org.apache.hadoop.fs.FSDataOutputStream(do_stream, null);
        expect(hdfsApi.create(anyString(), eq(true))).andReturn(stream);
        replay(hdfsApi);
        org.apache.ambari.view.pig.utils.UserLocalObjects.setHdfsApi(hdfsApi, context);
        org.apache.ambari.view.pig.templeton.client.TempletonApi api = createNiceMock(org.apache.ambari.view.pig.templeton.client.TempletonApi.class);
        org.apache.ambari.view.pig.utils.UserLocalObjects.setTempletonApi(api, context);
        org.apache.ambari.view.pig.templeton.client.TempletonApi.JobData data = api.new JobData();
        expect(api.runPigQuery(((java.io.File) (anyObject())), anyString(), eq("-useHCatalog"))).andReturn(data);
        replay(api);
        thrown.expect(org.apache.ambari.view.pig.utils.ServiceFormattedException.class);
        org.apache.ambari.view.pig.test.JobTest.doCreateJob("Test", "/tmp/script.pig", "-useHCatalog", jobService);
    }

    @org.junit.Test
    public void testSubmitJobTempletonError() throws java.lang.Exception {
        org.apache.ambari.view.utils.hdfs.HdfsApi hdfsApi = createNiceMock(org.apache.ambari.view.utils.hdfs.HdfsApi.class);
        hdfsApi.copy(eq("/tmp/script.pig"), startsWith("/tmp/.pigjobs/"));
        java.io.ByteArrayOutputStream do_stream = new java.io.ByteArrayOutputStream();
        org.apache.hadoop.fs.FSDataOutputStream stream = new org.apache.hadoop.fs.FSDataOutputStream(do_stream, null);
        expect(hdfsApi.create(anyString(), eq(true))).andReturn(stream);
        replay(hdfsApi);
        org.apache.ambari.view.pig.utils.UserLocalObjects.setHdfsApi(hdfsApi, context);
        org.apache.ambari.view.pig.templeton.client.TempletonApi api = createNiceMock(org.apache.ambari.view.pig.templeton.client.TempletonApi.class);
        org.apache.ambari.view.pig.utils.UserLocalObjects.setTempletonApi(api, context);
        org.apache.ambari.view.pig.templeton.client.TempletonApi.JobData data = api.new JobData();
        expect(api.runPigQuery(((java.io.File) (anyObject())), anyString(), eq("-useHCatalog"))).andThrow(new java.io.IOException());
        replay(api);
        thrown.expect(org.apache.ambari.view.pig.utils.ServiceFormattedException.class);
        org.apache.ambari.view.pig.test.JobTest.doCreateJob("Test", "/tmp/script.pig", "-useHCatalog", jobService);
    }

    @org.junit.Test
    public void testKillJobNoRemove() throws java.lang.Exception {
        org.apache.ambari.view.utils.hdfs.HdfsApi hdfsApi = createNiceMock(org.apache.ambari.view.utils.hdfs.HdfsApi.class);
        hdfsApi.copy(eq("/tmp/script.pig"), startsWith("/tmp/.pigjobs/"));
        java.io.ByteArrayOutputStream do_stream = new java.io.ByteArrayOutputStream();
        org.apache.hadoop.fs.FSDataOutputStream stream = new org.apache.hadoop.fs.FSDataOutputStream(do_stream, null);
        expect(hdfsApi.create(anyString(), eq(true))).andReturn(stream);
        replay(hdfsApi);
        org.apache.ambari.view.pig.utils.UserLocalObjects.setHdfsApi(hdfsApi, context);
        org.apache.ambari.view.pig.templeton.client.TempletonApi api = createStrictMock(org.apache.ambari.view.pig.templeton.client.TempletonApi.class);
        org.apache.ambari.view.pig.utils.UserLocalObjects.setTempletonApi(api, context);
        org.apache.ambari.view.pig.templeton.client.TempletonApi.JobData data = api.new JobData();
        data.id = "job_id_##";
        expect(api.runPigQuery(((java.io.File) (anyObject())), anyString(), eq("-useHCatalog"))).andReturn(data);
        replay(api);
        javax.ws.rs.core.Response response = org.apache.ambari.view.pig.test.JobTest.doCreateJob("Test", "/tmp/script.pig", "-useHCatalog", jobService);
        org.junit.Assert.assertEquals(201, response.getStatus());
        reset(api);
        api.killJob(eq("job_id_##"));
        expect(api.checkJob(anyString())).andReturn(api.new JobInfo()).anyTimes();
        replay(api);
        org.json.simple.JSONObject obj = ((org.json.simple.JSONObject) (response.getEntity()));
        org.apache.ambari.view.pig.resources.jobs.models.PigJob job = ((org.apache.ambari.view.pig.resources.jobs.models.PigJob) (obj.get("job")));
        response = jobService.killJob(job.getId(), null);
        org.junit.Assert.assertEquals(204, response.getStatus());
        response = jobService.getJob(job.getId());
        org.junit.Assert.assertEquals(200, response.getStatus());
    }

    @org.junit.Test
    public void testKillJobWithRemove() throws java.lang.Exception {
        org.apache.ambari.view.utils.hdfs.HdfsApi hdfsApi = createNiceMock(org.apache.ambari.view.utils.hdfs.HdfsApi.class);
        hdfsApi.copy(eq("/tmp/script.pig"), startsWith("/tmp/.pigjobs/"));
        java.io.ByteArrayOutputStream do_stream = new java.io.ByteArrayOutputStream();
        org.apache.hadoop.fs.FSDataOutputStream stream = new org.apache.hadoop.fs.FSDataOutputStream(do_stream, null);
        expect(hdfsApi.create(anyString(), eq(true))).andReturn(stream);
        replay(hdfsApi);
        org.apache.ambari.view.pig.utils.UserLocalObjects.setHdfsApi(hdfsApi, context);
        org.apache.ambari.view.pig.templeton.client.TempletonApi api = createStrictMock(org.apache.ambari.view.pig.templeton.client.TempletonApi.class);
        org.apache.ambari.view.pig.utils.UserLocalObjects.setTempletonApi(api, context);
        org.apache.ambari.view.pig.templeton.client.TempletonApi.JobData data = api.new JobData();
        data.id = "job_id_##";
        expect(api.runPigQuery(((java.io.File) (anyObject())), anyString(), eq("-useHCatalog"))).andReturn(data);
        replay(api);
        javax.ws.rs.core.Response response = org.apache.ambari.view.pig.test.JobTest.doCreateJob("Test", "/tmp/script.pig", "-useHCatalog", jobService);
        org.junit.Assert.assertEquals(201, response.getStatus());
        reset(api);
        api.killJob(eq("job_id_##"));
        expect(api.checkJob(anyString())).andReturn(api.new JobInfo()).anyTimes();
        replay(api);
        org.json.simple.JSONObject obj = ((org.json.simple.JSONObject) (response.getEntity()));
        org.apache.ambari.view.pig.resources.jobs.models.PigJob job = ((org.apache.ambari.view.pig.resources.jobs.models.PigJob) (obj.get("job")));
        response = jobService.killJob(job.getId(), "true");
        org.junit.Assert.assertEquals(204, response.getStatus());
        thrown.expect(org.apache.ambari.view.pig.utils.NotFoundFormattedException.class);
        jobService.getJob(job.getId());
    }

    @org.junit.Test
    public void testJobStatusFlow() throws java.lang.Exception {
        org.apache.ambari.view.utils.hdfs.HdfsApi hdfsApi = createNiceMock(org.apache.ambari.view.utils.hdfs.HdfsApi.class);
        hdfsApi.copy(eq("/tmp/script.pig"), startsWith("/tmp/.pigjobs/"));
        java.io.ByteArrayOutputStream do_stream = new java.io.ByteArrayOutputStream();
        org.apache.hadoop.fs.FSDataOutputStream stream = new org.apache.hadoop.fs.FSDataOutputStream(do_stream, null);
        expect(hdfsApi.create(anyString(), eq(true))).andReturn(stream);
        replay(hdfsApi);
        org.apache.ambari.view.pig.utils.UserLocalObjects.setHdfsApi(hdfsApi, context);
        org.apache.ambari.view.pig.templeton.client.TempletonApi api = createNiceMock(org.apache.ambari.view.pig.templeton.client.TempletonApi.class);
        org.apache.ambari.view.pig.utils.UserLocalObjects.setTempletonApi(api, context);
        org.apache.ambari.view.pig.templeton.client.TempletonApi.JobData data = api.new JobData();
        data.id = "job_id_#";
        expect(api.runPigQuery(((java.io.File) (anyObject())), anyString(), eq("-useHCatalog"))).andReturn(data);
        replay(api);
        javax.ws.rs.core.Response response = org.apache.ambari.view.pig.test.JobTest.doCreateJob("Test", "/tmp/script.pig", "-useHCatalog", jobService);
        org.junit.Assert.assertEquals("-useHCatalog", do_stream.toString());
        org.junit.Assert.assertEquals(201, response.getStatus());
        org.apache.ambari.view.pig.resources.jobs.models.PigJob job = ((org.apache.ambari.view.pig.resources.jobs.models.PigJob) (((org.json.simple.JSONObject) (response.getEntity())).get("job")));
        org.junit.Assert.assertEquals(org.apache.ambari.view.pig.resources.jobs.models.PigJob.PIG_JOB_STATE_SUBMITTED, job.getStatus());
        org.junit.Assert.assertTrue(job.isInProgress());
        reset(api);
        org.apache.ambari.view.pig.templeton.client.TempletonApi.JobInfo info = api.new JobInfo();
        expect(api.checkJob(eq("job_id_#"))).andReturn(info);
        replay(api);
        response = jobService.getJob(job.getId());
        org.junit.Assert.assertEquals(200, response.getStatus());
        job = ((org.apache.ambari.view.pig.resources.jobs.models.PigJob) (((org.json.simple.JSONObject) (response.getEntity())).get("job")));
        org.junit.Assert.assertEquals(org.apache.ambari.view.pig.resources.jobs.models.PigJob.PIG_JOB_STATE_SUBMITTED, job.getStatus());
        reset(api);
        info = api.new JobInfo();
        info.status = new java.util.HashMap<java.lang.String, java.lang.Object>();
        info.status.put("runState", ((double) (org.apache.ambari.view.pig.resources.jobs.JobResourceManager.RUN_STATE_RUNNING)));
        info.percentComplete = "30% complete";
        expect(api.checkJob(eq("job_id_#"))).andReturn(info);
        replay(api);
        response = jobService.getJob(job.getId());
        org.junit.Assert.assertEquals(200, response.getStatus());
        job = ((org.apache.ambari.view.pig.resources.jobs.models.PigJob) (((org.json.simple.JSONObject) (response.getEntity())).get("job")));
        org.junit.Assert.assertEquals(org.apache.ambari.view.pig.resources.jobs.models.PigJob.PIG_JOB_STATE_RUNNING, job.getStatus());
        org.junit.Assert.assertTrue(job.isInProgress());
        org.junit.Assert.assertEquals(30, ((java.lang.Object) (job.getPercentComplete())));
        reset(api);
        info = api.new JobInfo();
        info.status = new java.util.HashMap<java.lang.String, java.lang.Object>();
        info.status.put("runState", ((double) (org.apache.ambari.view.pig.resources.jobs.JobResourceManager.RUN_STATE_SUCCEEDED)));
        expect(api.checkJob(eq("job_id_#"))).andReturn(info);
        replay(api);
        response = jobService.getJob(job.getId());
        org.junit.Assert.assertEquals(200, response.getStatus());
        job = ((org.apache.ambari.view.pig.resources.jobs.models.PigJob) (((org.json.simple.JSONObject) (response.getEntity())).get("job")));
        org.junit.Assert.assertEquals(org.apache.ambari.view.pig.resources.jobs.models.PigJob.PIG_JOB_STATE_COMPLETED, job.getStatus());
        org.junit.Assert.assertFalse(job.isInProgress());
        org.junit.Assert.assertNull(job.getPercentComplete());
        reset(api);
        info = api.new JobInfo();
        info.status = new java.util.HashMap<java.lang.String, java.lang.Object>();
        info.status.put("runState", ((double) (org.apache.ambari.view.pig.resources.jobs.JobResourceManager.RUN_STATE_PREP)));
        expect(api.checkJob(eq("job_id_#"))).andReturn(info);
        replay(api);
        response = jobService.getJob(job.getId());
        org.junit.Assert.assertEquals(200, response.getStatus());
        job = ((org.apache.ambari.view.pig.resources.jobs.models.PigJob) (((org.json.simple.JSONObject) (response.getEntity())).get("job")));
        org.junit.Assert.assertEquals(org.apache.ambari.view.pig.resources.jobs.models.PigJob.PIG_JOB_STATE_RUNNING, job.getStatus());
        reset(api);
        info = api.new JobInfo();
        info.status = new java.util.HashMap<java.lang.String, java.lang.Object>();
        info.status.put("runState", ((double) (org.apache.ambari.view.pig.resources.jobs.JobResourceManager.RUN_STATE_FAILED)));
        expect(api.checkJob(eq("job_id_#"))).andReturn(info);
        replay(api);
        response = jobService.getJob(job.getId());
        org.junit.Assert.assertEquals(200, response.getStatus());
        job = ((org.apache.ambari.view.pig.resources.jobs.models.PigJob) (((org.json.simple.JSONObject) (response.getEntity())).get("job")));
        org.junit.Assert.assertEquals(org.apache.ambari.view.pig.resources.jobs.models.PigJob.PIG_JOB_STATE_FAILED, job.getStatus());
        org.junit.Assert.assertFalse(job.isInProgress());
        reset(api);
        info = api.new JobInfo();
        info.status = new java.util.HashMap<java.lang.String, java.lang.Object>();
        info.status.put("runState", ((double) (org.apache.ambari.view.pig.resources.jobs.JobResourceManager.RUN_STATE_KILLED)));
        expect(api.checkJob(eq("job_id_#"))).andReturn(info);
        replay(api);
        response = jobService.getJob(job.getId());
        org.junit.Assert.assertEquals(200, response.getStatus());
        job = ((org.apache.ambari.view.pig.resources.jobs.models.PigJob) (((org.json.simple.JSONObject) (response.getEntity())).get("job")));
        org.junit.Assert.assertEquals(org.apache.ambari.view.pig.resources.jobs.models.PigJob.PIG_JOB_STATE_KILLED, job.getStatus());
        org.junit.Assert.assertFalse(job.isInProgress());
    }
}