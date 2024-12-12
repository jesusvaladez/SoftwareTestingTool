package org.apache.ambari.server.bootstrap;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.spi.container.servlet.ServletContainer;
import com.sun.jersey.test.framework.JerseyTest;
import com.sun.jersey.test.framework.WebAppDescriptor;
import javax.ws.rs.core.MediaType;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
public class BootStrapResourceTest extends com.sun.jersey.test.framework.JerseyTest {
    static java.lang.String PACKAGE_NAME = "org.apache.ambari.server.api.rest";

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.bootstrap.BootStrapResourceTest.class);

    com.google.inject.Injector injector;

    org.apache.ambari.server.bootstrap.BootStrapImpl bsImpl;

    public BootStrapResourceTest() {
        super(new com.sun.jersey.test.framework.WebAppDescriptor.Builder(org.apache.ambari.server.bootstrap.BootStrapResourceTest.PACKAGE_NAME).servletClass(com.sun.jersey.spi.container.servlet.ServletContainer.class).build());
    }

    public class MockModule extends com.google.inject.AbstractModule {
        @java.lang.Override
        protected void configure() {
            org.apache.ambari.server.bootstrap.BootStrapImpl bsImpl = Mockito.mock(org.apache.ambari.server.bootstrap.BootStrapImpl.class);
            Mockito.when(bsImpl.getStatus(0)).thenReturn(generateDummyBSStatus());
            Mockito.when(bsImpl.runBootStrap(Matchers.any(org.apache.ambari.server.bootstrap.SshHostInfo.class))).thenReturn(generateBSResponse());
            bind(org.apache.ambari.server.bootstrap.BootStrapImpl.class).toInstance(bsImpl);
            requestStaticInjection(org.apache.ambari.server.api.rest.BootStrapResource.class);
        }
    }

    @java.lang.Override
    public void setUp() throws java.lang.Exception {
        super.setUp();
        injector = com.google.inject.Guice.createInjector(new org.apache.ambari.server.bootstrap.BootStrapResourceTest.MockModule());
    }

    @java.lang.Override
    protected int getPort(int defaultPort) {
        try (java.net.ServerSocket socket = new java.net.ServerSocket(0)) {
            return socket.getLocalPort();
        } catch (java.io.IOException e) {
        }
        return defaultPort;
    }

    protected org.codehaus.jettison.json.JSONObject createDummySshInfo() throws org.codehaus.jettison.json.JSONException {
        org.codehaus.jettison.json.JSONObject json = new org.codehaus.jettison.json.JSONObject();
        json.put("sshkey", "awesome");
        json.put("hosts", new java.util.ArrayList<java.lang.String>());
        return json;
    }

    protected org.apache.ambari.server.bootstrap.BSResponse generateBSResponse() {
        org.apache.ambari.server.bootstrap.BSResponse response = new org.apache.ambari.server.bootstrap.BSResponse();
        response.setLog("Logging");
        response.setRequestId(1);
        response.setStatus(org.apache.ambari.server.bootstrap.BSResponse.BSRunStat.OK);
        return response;
    }

    protected org.apache.ambari.server.bootstrap.BootStrapStatus generateDummyBSStatus() {
        org.apache.ambari.server.bootstrap.BootStrapStatus status = new org.apache.ambari.server.bootstrap.BootStrapStatus();
        status.setLog("Logging ");
        status.setStatus(org.apache.ambari.server.bootstrap.BootStrapStatus.BSStat.ERROR);
        status.setHostsStatus(new java.util.ArrayList<>());
        return status;
    }

    @org.junit.Test
    public void bootStrapGet() throws com.sun.jersey.api.client.UniformInterfaceException, org.codehaus.jettison.json.JSONException {
        com.sun.jersey.api.client.WebResource webResource = resource();
        org.apache.ambari.server.bootstrap.BootStrapStatus status = webResource.path("/bootstrap/0").type(MediaType.APPLICATION_JSON).get(org.apache.ambari.server.bootstrap.BootStrapStatus.class);
        org.apache.ambari.server.bootstrap.BootStrapResourceTest.LOG.info((("GET Response from the API " + status.getLog()) + " ") + status.getStatus());
        junit.framework.Assert.assertEquals(status.getStatus(), org.apache.ambari.server.bootstrap.BootStrapStatus.BSStat.ERROR);
    }

    @org.junit.Test
    public void bootStrapPost() throws com.sun.jersey.api.client.UniformInterfaceException, org.codehaus.jettison.json.JSONException {
        com.sun.jersey.api.client.WebResource webResource = resource();
        org.codehaus.jettison.json.JSONObject object = webResource.path("/bootstrap").type(MediaType.APPLICATION_JSON).post(org.codehaus.jettison.json.JSONObject.class, createDummySshInfo());
        junit.framework.Assert.assertEquals("OK", object.get("status"));
    }
}