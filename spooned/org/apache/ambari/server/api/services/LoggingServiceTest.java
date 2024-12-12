package org.apache.ambari.server.api.services;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import org.easymock.EasyMockSupport;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import static org.easymock.EasyMock.expect;
public class LoggingServiceTest {
    @org.junit.Before
    @org.junit.After
    public void clearAuthentication() {
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(null);
    }

    @org.junit.Test
    public void testGetSearchEngineWhenLogSearchNotRunningAsAdministrator() throws java.lang.Exception {
        testGetSearchEngineWhenLogSearchNotRunning(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator(), true);
    }

    @org.junit.Test
    public void testGetSearchEngineWhenLogSearchNotRunningAsClusterAdministrator() throws java.lang.Exception {
        testGetSearchEngineWhenLogSearchNotRunning(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator(), true);
    }

    @org.junit.Test
    public void testGetSearchEngineWhenLogSearchNotRunningAsClusterOperator() throws java.lang.Exception {
        testGetSearchEngineWhenLogSearchNotRunning(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterOperator(), true);
    }

    @org.junit.Test
    public void testGetSearchEngineWhenLogSearchNotRunningAsServiceAdministrator() throws java.lang.Exception {
        testGetSearchEngineWhenLogSearchNotRunning(org.apache.ambari.server.security.TestAuthenticationFactory.createServiceAdministrator(), true);
    }

    @org.junit.Test
    public void testGetSearchEngineWhenLogSearchNotRunningAsServiceOperator() throws java.lang.Exception {
        testGetSearchEngineWhenLogSearchNotRunning(org.apache.ambari.server.security.TestAuthenticationFactory.createServiceOperator(), false);
    }

    @org.junit.Test
    public void testGetSearchEngineWhenLogSearchNotRunningAsClusterUser() throws java.lang.Exception {
        testGetSearchEngineWhenLogSearchNotRunning(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterUser(), false);
    }

    private void testGetSearchEngineWhenLogSearchNotRunning(org.springframework.security.core.Authentication authentication, boolean shouldBeAuthorized) throws java.lang.Exception {
        final java.lang.String expectedClusterName = "clusterone";
        final java.lang.String expectedErrorMessage = "LogSearch is not currently available.  If LogSearch is deployed in this cluster, please verify that the LogSearch services are running.";
        org.easymock.EasyMockSupport mockSupport = new org.easymock.EasyMockSupport();
        org.apache.ambari.server.api.services.LoggingService.ControllerFactory controllerFactoryMock = mockSupport.createMock(org.apache.ambari.server.api.services.LoggingService.ControllerFactory.class);
        org.apache.ambari.server.controller.AmbariManagementController controllerMock = mockSupport.createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.state.Clusters clustersMock = mockSupport.createMock(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.state.Cluster clusterMock = mockSupport.createMock(org.apache.ambari.server.state.Cluster.class);
        org.apache.ambari.server.controller.logging.LoggingRequestHelperFactory helperFactoryMock = mockSupport.createMock(org.apache.ambari.server.controller.logging.LoggingRequestHelperFactory.class);
        javax.ws.rs.core.UriInfo uriInfoMock = mockSupport.createMock(javax.ws.rs.core.UriInfo.class);
        if (shouldBeAuthorized) {
            EasyMock.expect(uriInfoMock.getQueryParameters()).andReturn(new com.sun.jersey.core.util.MultivaluedMapImpl()).atLeastOnce();
            EasyMock.expect(helperFactoryMock.getHelper(controllerMock, expectedClusterName)).andReturn(null).atLeastOnce();
        }
        EasyMock.expect(controllerFactoryMock.getController()).andReturn(controllerMock).atLeastOnce();
        EasyMock.expect(controllerMock.getClusters()).andReturn(clustersMock).once();
        EasyMock.expect(clustersMock.getCluster(expectedClusterName)).andReturn(clusterMock).once();
        EasyMock.expect(clusterMock.getResourceId()).andReturn(4L).once();
        mockSupport.replayAll();
        org.apache.ambari.server.security.authorization.AuthorizationHelperInitializer.viewInstanceDAOReturningNull();
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        org.apache.ambari.server.api.services.LoggingService loggingService = new org.apache.ambari.server.api.services.LoggingService(expectedClusterName, controllerFactoryMock);
        loggingService.setLoggingRequestHelperFactory(helperFactoryMock);
        javax.ws.rs.core.Response resource = loggingService.getSearchEngine("", null, uriInfoMock);
        org.junit.Assert.assertNotNull("The response returned by the LoggingService should not have been null", resource);
        if (shouldBeAuthorized) {
            org.junit.Assert.assertEquals("An OK status should have been returned", java.net.HttpURLConnection.HTTP_NOT_FOUND, resource.getStatus());
            org.junit.Assert.assertNotNull("A non-null Entity should have been returned", resource.getEntity());
            org.junit.Assert.assertEquals("Expected error message was not included in the response", expectedErrorMessage, resource.getEntity());
        } else {
            org.junit.Assert.assertEquals("A FORBIDDEN status should have been returned", java.net.HttpURLConnection.HTTP_FORBIDDEN, resource.getStatus());
        }
        mockSupport.verifyAll();
    }
}