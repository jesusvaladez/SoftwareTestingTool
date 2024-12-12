package org.apache.ambari.view.pig.test;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import org.json.simple.JSONObject;
import static org.easymock.EasyMock.*;
import static org.easymock.EasyMock.replay;
public class UDFTest extends org.apache.ambari.view.pig.BasePigTest {
    @org.junit.Rule
    public org.junit.rules.ExpectedException thrown = org.junit.rules.ExpectedException.none();

    private org.apache.ambari.view.pig.resources.udf.UDFService udfService;

    @java.lang.Override
    @org.junit.Before
    public void setUp() throws java.lang.Exception {
        super.setUp();
        udfService = org.apache.ambari.view.pig.BasePigTest.getService(org.apache.ambari.view.pig.resources.udf.UDFService.class, handler, context);
    }

    private javax.ws.rs.core.Response doCreateUDF() {
        org.apache.ambari.view.pig.resources.udf.UDFService.UDFRequest request = new org.apache.ambari.view.pig.resources.udf.UDFService.UDFRequest();
        request.udf = new org.apache.ambari.view.pig.resources.udf.models.UDF();
        request.udf.setPath("/tmp/udf.jar");
        request.udf.setName("TestUDF");
        javax.ws.rs.core.UriInfo uriInfo = createNiceMock(javax.ws.rs.core.UriInfo.class);
        java.net.URI uri = javax.ws.rs.core.UriBuilder.fromUri("http://host/a/b").build();
        expect(uriInfo.getAbsolutePath()).andReturn(uri);
        javax.servlet.http.HttpServletResponse resp_obj = createNiceMock(javax.servlet.http.HttpServletResponse.class);
        resp_obj.setHeader(eq("Location"), anyString());
        EasyMock.replay(uriInfo, resp_obj);
        return udfService.createUDF(request, resp_obj, uriInfo);
    }

    @org.junit.Test
    public void createUDF() {
        javax.ws.rs.core.Response response = doCreateUDF();
        org.junit.Assert.assertEquals(201, response.getStatus());
        org.json.simple.JSONObject obj = ((org.json.simple.JSONObject) (response.getEntity()));
        org.junit.Assert.assertTrue(obj.containsKey("udf"));
        org.junit.Assert.assertNotNull(((org.apache.ambari.view.pig.resources.udf.models.UDF) (obj.get("udf"))).getId());
        org.junit.Assert.assertFalse(((org.apache.ambari.view.pig.resources.udf.models.UDF) (obj.get("udf"))).getId().isEmpty());
    }

    @org.junit.Test
    public void udfNotFound() {
        thrown.expect(org.apache.ambari.view.pig.utils.NotFoundFormattedException.class);
        udfService.getUDF("4242");
    }

    @org.junit.Test
    public void updateUDF() {
        javax.ws.rs.core.Response createdUDF = doCreateUDF();
        java.lang.String createdUdfId = ((org.apache.ambari.view.pig.resources.udf.models.UDF) (((org.json.simple.JSONObject) (createdUDF.getEntity())).get("udf"))).getId();
        org.apache.ambari.view.pig.resources.udf.UDFService.UDFRequest request = new org.apache.ambari.view.pig.resources.udf.UDFService.UDFRequest();
        request.udf = new org.apache.ambari.view.pig.resources.udf.models.UDF();
        request.udf.setPath("/tmp/updatedUDF.jar");
        request.udf.setName("TestUDF2");
        javax.ws.rs.core.Response response = udfService.updateUDF(request, createdUdfId);
        org.junit.Assert.assertEquals(204, response.getStatus());
        javax.ws.rs.core.Response response2 = udfService.getUDF(createdUdfId);
        org.junit.Assert.assertEquals(200, response2.getStatus());
        org.json.simple.JSONObject obj = ((org.json.simple.JSONObject) (response2.getEntity()));
        org.junit.Assert.assertTrue(obj.containsKey("udf"));
        org.junit.Assert.assertEquals(((org.apache.ambari.view.pig.resources.udf.models.UDF) (obj.get("udf"))).getName(), request.udf.getName());
        org.junit.Assert.assertEquals(((org.apache.ambari.view.pig.resources.udf.models.UDF) (obj.get("udf"))).getPath(), request.udf.getPath());
    }

    @org.junit.Test
    public void deleteUDF() {
        javax.ws.rs.core.Response createdUDF = doCreateUDF();
        java.lang.String createdUdfId = ((org.apache.ambari.view.pig.resources.udf.models.UDF) (((org.json.simple.JSONObject) (createdUDF.getEntity())).get("udf"))).getId();
        javax.ws.rs.core.Response response = udfService.deleteUDF(createdUdfId);
        org.junit.Assert.assertEquals(204, response.getStatus());
        thrown.expect(org.apache.ambari.view.pig.utils.NotFoundFormattedException.class);
        udfService.getUDF(createdUdfId);
    }
}