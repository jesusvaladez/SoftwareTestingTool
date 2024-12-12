package org.apache.ambari.server.api;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import org.easymock.EasyMockSupport;
import static org.easymock.EasyMock.expect;
public class AmbariErrorHandlerIT extends org.easymock.EasyMockSupport {
    private com.google.gson.Gson gson = new com.google.gson.Gson();

    @org.junit.Test
    public void testErrorWithJetty() throws java.lang.Exception {
        org.eclipse.jetty.server.Server server = new org.eclipse.jetty.server.Server(0);
        org.apache.ambari.server.security.authentication.jwt.JwtAuthenticationPropertiesProvider propertiesProvider = createNiceMock(org.apache.ambari.server.security.authentication.jwt.JwtAuthenticationPropertiesProvider.class);
        EasyMock.expect(propertiesProvider.get()).andReturn(null).anyTimes();
        replayAll();
        org.eclipse.jetty.servlet.ServletContextHandler root = new org.eclipse.jetty.servlet.ServletContextHandler(server, "/", org.eclipse.jetty.servlet.ServletContextHandler.SECURITY | org.eclipse.jetty.servlet.ServletContextHandler.SESSIONS);
        root.addServlet(org.apache.ambari.server.api.AmbariErrorHandlerIT.HelloServlet.class, "/hello");
        root.addServlet(org.eclipse.jetty.servlet.DefaultServlet.class, "/");
        root.setErrorHandler(new org.apache.ambari.server.api.AmbariErrorHandler(gson, propertiesProvider));
        server.start();
        int localPort = ((org.eclipse.jetty.server.ServerConnector) (server.getConnectors()[0])).getLocalPort();
        com.sun.jersey.api.client.Client client = new com.sun.jersey.api.client.Client();
        com.sun.jersey.api.client.WebResource resource = client.resource(("http://localhost:" + localPort) + "/");
        com.sun.jersey.api.client.ClientResponse successResponse = resource.path("hello").get(com.sun.jersey.api.client.ClientResponse.class);
        org.junit.Assert.assertEquals(javax.servlet.http.HttpServletResponse.SC_OK, successResponse.getStatus());
        com.sun.jersey.api.client.ClientResponse failResponse = resource.path("fail").get(com.sun.jersey.api.client.ClientResponse.class);
        org.junit.Assert.assertEquals(javax.servlet.http.HttpServletResponse.SC_NOT_FOUND, failResponse.getStatus());
        try {
            java.lang.String response = failResponse.getEntity(java.lang.String.class);
            java.lang.System.out.println(response);
            java.util.Map map;
            map = gson.fromJson(response, java.util.Map.class);
            java.lang.System.out.println(map);
            org.junit.Assert.assertNotNull("Incorrect response status", map.get("status"));
            org.junit.Assert.assertNotNull("Incorrect response message", map.get("message"));
        } catch (com.google.gson.JsonSyntaxException e1) {
            org.junit.Assert.fail("Incorrect response");
        }
        server.stop();
        verifyAll();
    }

    @java.lang.SuppressWarnings("serial")
    public static class HelloServlet extends javax.servlet.http.HttpServlet {
        @java.lang.Override
        protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, java.io.IOException {
            response.setContentType("text/html");
            response.setStatus(javax.servlet.http.HttpServletResponse.SC_OK);
            response.getWriter().println("hello");
        }
    }
}