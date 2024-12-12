package org.apache.ambari.server.api.services;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.UriInfo;
import org.easymock.EasyMock;
public class ClusterStackVersionServiceTest extends org.apache.ambari.server.api.services.BaseServiceTest {
    public java.util.List<org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation> getTestInvocations() throws java.lang.Exception {
        java.util.List<org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation> listInvocations = new java.util.ArrayList<>();
        org.apache.ambari.server.api.services.ClusterStackVersionService clusterStackVersionService;
        java.lang.reflect.Method m;
        java.lang.Object[] args;
        clusterStackVersionService = new org.apache.ambari.server.api.services.ClusterStackVersionServiceTest.TestClusterStackVersionService("cluster");
        m = clusterStackVersionService.getClass().getMethod("getClusterStackVersions", javax.ws.rs.core.HttpHeaders.class, javax.ws.rs.core.UriInfo.class);
        args = new java.lang.Object[]{ getHttpHeaders(), getUriInfo() };
        listInvocations.add(new org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation(org.apache.ambari.server.api.services.Request.Type.GET, clusterStackVersionService, m, args, null));
        clusterStackVersionService = new org.apache.ambari.server.api.services.ClusterStackVersionServiceTest.TestClusterStackVersionService("cluster");
        m = clusterStackVersionService.getClass().getMethod("getClusterStackVersion", javax.ws.rs.core.HttpHeaders.class, javax.ws.rs.core.UriInfo.class, java.lang.String.class);
        args = new java.lang.Object[]{ getHttpHeaders(), getUriInfo(), "1" };
        listInvocations.add(new org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation(org.apache.ambari.server.api.services.Request.Type.GET, clusterStackVersionService, m, args, null));
        return listInvocations;
    }

    @org.junit.Test
    public void testGetRepositoryVersionService() {
        org.apache.ambari.server.api.services.ClusterStackVersionService clusterStackVersionService = new org.apache.ambari.server.api.services.ClusterStackVersionServiceTest.TestClusterStackVersionService("cluster");
        org.apache.ambari.server.api.services.RepositoryVersionService rvs = clusterStackVersionService.getRepositoryVersionService(org.easymock.EasyMock.createMock(javax.ws.rs.core.Request.class), "1");
        junit.framework.TestCase.assertNotNull(rvs);
    }

    private class TestClusterStackVersionService extends org.apache.ambari.server.api.services.ClusterStackVersionService {
        public TestClusterStackVersionService(java.lang.String clusterName) {
            super(clusterName);
        }

        @java.lang.Override
        protected org.apache.ambari.server.api.resources.ResourceInstance createResource(org.apache.ambari.server.controller.spi.Resource.Type type, java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> mapIds) {
            return getTestResource();
        }

        @java.lang.Override
        org.apache.ambari.server.api.services.RequestFactory getRequestFactory() {
            return getTestRequestFactory();
        }

        @java.lang.Override
        protected org.apache.ambari.server.api.services.parsers.RequestBodyParser getBodyParser() {
            return getTestBodyParser();
        }

        @java.lang.Override
        protected org.apache.ambari.server.api.services.serializers.ResultSerializer getResultSerializer() {
            return getTestResultSerializer();
        }
    }
}