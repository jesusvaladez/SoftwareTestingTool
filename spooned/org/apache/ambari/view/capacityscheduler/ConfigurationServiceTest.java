package org.apache.ambari.view.capacityscheduler;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import org.easymock.EasyMock;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import static org.easymock.EasyMock.*;
public class ConfigurationServiceTest {
    private org.apache.ambari.view.ViewContext context;

    private javax.ws.rs.core.HttpHeaders httpHeaders;

    private javax.ws.rs.core.UriInfo uriInfo;

    private org.apache.ambari.view.cluster.Cluster ambariCluster;

    private java.util.Map<java.lang.String, java.lang.String> properties;

    private org.apache.ambari.view.capacityscheduler.ConfigurationService configurationService;

    public static final java.lang.String BASE_URI = "http://localhost:8084/myapp/";

    @org.junit.Before
    public void setUp() throws java.lang.Exception {
        context = createNiceMock(org.apache.ambari.view.ViewContext.class);
        httpHeaders = createNiceMock(javax.ws.rs.core.HttpHeaders.class);
        ambariCluster = createNiceMock(org.apache.ambari.view.cluster.Cluster.class);
        org.easymock.EasyMock.expect(ambariCluster.getConfigurationValue("ranger-yarn-plugin-properties", "ranger-yarn-plugin-enabled")).andReturn("Yes").anyTimes();
        org.easymock.EasyMock.expect(context.getCluster()).andReturn(ambariCluster).anyTimes();
        org.easymock.EasyMock.expect(context.getProperties()).andReturn(properties).anyTimes();
        org.easymock.EasyMock.replay(context);
        org.easymock.EasyMock.replay(ambariCluster);
        java.lang.System.out.println("context.getProperties() : " + context.getProperties());
        configurationService = new org.apache.ambari.view.capacityscheduler.ConfigurationService(context);
    }

    @org.junit.After
    public void tearDown() {
    }

    @org.junit.Test
    public void testRightConfigurationValue() {
        javax.ws.rs.core.Response response = configurationService.getConfigurationValue("ranger-yarn-plugin-properties", "ranger-yarn-plugin-enabled");
        org.json.simple.JSONObject jsonObject = ((org.json.simple.JSONObject) (response.getEntity()));
        org.json.simple.JSONArray arr = ((org.json.simple.JSONArray) (jsonObject.get("configs")));
        org.junit.Assert.assertEquals(arr.size(), 1);
        org.json.simple.JSONObject obj = ((org.json.simple.JSONObject) (arr.get(0)));
        org.junit.Assert.assertEquals(obj.get("siteName"), "ranger-yarn-plugin-properties");
        org.junit.Assert.assertEquals(obj.get("configName"), "ranger-yarn-plugin-enabled");
        org.junit.Assert.assertEquals(obj.get("configValue"), "Yes");
    }

    @org.junit.Test
    public void testExceptionOnWrongConfigurationValue() {
        javax.ws.rs.core.Response response = configurationService.getConfigurationValue("random-site", "random-key");
        org.json.simple.JSONObject jsonObject = ((org.json.simple.JSONObject) (response.getEntity()));
        org.json.simple.JSONArray arr = ((org.json.simple.JSONArray) (jsonObject.get("configs")));
        org.junit.Assert.assertEquals(arr.size(), 1);
        org.json.simple.JSONObject obj = ((org.json.simple.JSONObject) (arr.get(0)));
        org.junit.Assert.assertEquals(obj.get("siteName"), "random-site");
        org.junit.Assert.assertEquals(obj.get("configName"), "random-key");
        org.junit.Assert.assertEquals(obj.get("configValue"), null);
    }
}