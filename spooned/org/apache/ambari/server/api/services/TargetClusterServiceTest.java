package org.apache.ambari.server.api.services;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.UriInfo;
public class TargetClusterServiceTest extends org.apache.ambari.server.api.services.BaseServiceTest {
    public java.util.List<org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation> getTestInvocations() throws java.lang.Exception {
        java.util.List<org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation> listInvocations = new java.util.ArrayList<>();
        org.apache.ambari.server.api.services.TargetClusterService targetClusterService = new org.apache.ambari.server.api.services.TargetClusterServiceTest.TestTargetClusterService("targetClusterName");
        java.lang.reflect.Method m = targetClusterService.getClass().getMethod("getTargetCluster", java.lang.String.class, javax.ws.rs.core.HttpHeaders.class, javax.ws.rs.core.UriInfo.class, java.lang.String.class);
        java.lang.Object[] args = new java.lang.Object[]{ null, getHttpHeaders(), getUriInfo(), "targetClusterName" };
        listInvocations.add(new org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation(org.apache.ambari.server.api.services.Request.Type.GET, targetClusterService, m, args, null));
        targetClusterService = new org.apache.ambari.server.api.services.TargetClusterServiceTest.TestTargetClusterService(null);
        m = targetClusterService.getClass().getMethod("getTargetClusters", java.lang.String.class, javax.ws.rs.core.HttpHeaders.class, javax.ws.rs.core.UriInfo.class);
        args = new java.lang.Object[]{ null, getHttpHeaders(), getUriInfo() };
        listInvocations.add(new org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation(org.apache.ambari.server.api.services.Request.Type.GET, targetClusterService, m, args, null));
        targetClusterService = new org.apache.ambari.server.api.services.TargetClusterServiceTest.TestTargetClusterService("targetClusterName");
        m = targetClusterService.getClass().getMethod("createTargetCluster", java.lang.String.class, javax.ws.rs.core.HttpHeaders.class, javax.ws.rs.core.UriInfo.class, java.lang.String.class);
        args = new java.lang.Object[]{ "body", getHttpHeaders(), getUriInfo(), "targetClusterName" };
        listInvocations.add(new org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation(org.apache.ambari.server.api.services.Request.Type.POST, targetClusterService, m, args, "body"));
        targetClusterService = new org.apache.ambari.server.api.services.TargetClusterServiceTest.TestTargetClusterService("targetClusterName");
        m = targetClusterService.getClass().getMethod("updateTargetCluster", java.lang.String.class, javax.ws.rs.core.HttpHeaders.class, javax.ws.rs.core.UriInfo.class, java.lang.String.class);
        args = new java.lang.Object[]{ "body", getHttpHeaders(), getUriInfo(), "targetClusterName" };
        listInvocations.add(new org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation(org.apache.ambari.server.api.services.Request.Type.PUT, targetClusterService, m, args, "body"));
        targetClusterService = new org.apache.ambari.server.api.services.TargetClusterServiceTest.TestTargetClusterService("targetClusterName");
        m = targetClusterService.getClass().getMethod("deleteTargetCluster", javax.ws.rs.core.HttpHeaders.class, javax.ws.rs.core.UriInfo.class, java.lang.String.class);
        args = new java.lang.Object[]{ getHttpHeaders(), getUriInfo(), "targetClusterName" };
        listInvocations.add(new org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation(org.apache.ambari.server.api.services.Request.Type.DELETE, targetClusterService, m, args, null));
        return listInvocations;
    }

    private class TestTargetClusterService extends org.apache.ambari.server.api.services.TargetClusterService {
        private java.lang.String m_targetClusterId;

        private TestTargetClusterService(java.lang.String targetClusterId) {
            m_targetClusterId = targetClusterId;
        }

        @java.lang.Override
        org.apache.ambari.server.api.resources.ResourceInstance createTargetClusterResource(java.lang.String targetClusterName) {
            org.junit.Assert.assertEquals(m_targetClusterId, targetClusterName);
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