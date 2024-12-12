package org.apache.ambari.server.api.services;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.UriInfo;
public class HostComponentServiceTest extends org.apache.ambari.server.api.services.BaseServiceTest {
    public java.util.List<org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation> getTestInvocations() throws java.lang.Exception {
        java.util.List<org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation> listInvocations = new java.util.ArrayList<>();
        org.apache.ambari.server.api.services.HostComponentService componentService = new org.apache.ambari.server.api.services.HostComponentServiceTest.TestHostComponentService("clusterName", "serviceName", "componentName");
        java.lang.reflect.Method m = componentService.getClass().getMethod("getHostComponent", java.lang.String.class, javax.ws.rs.core.HttpHeaders.class, javax.ws.rs.core.UriInfo.class, java.lang.String.class, java.lang.String.class);
        java.lang.Object[] args = new java.lang.Object[]{ null, getHttpHeaders(), getUriInfo(), "componentName", null };
        listInvocations.add(new org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation(org.apache.ambari.server.api.services.Request.Type.GET, componentService, m, args, null));
        componentService = new org.apache.ambari.server.api.services.HostComponentServiceTest.TestHostComponentService("clusterName", "serviceName", null);
        m = componentService.getClass().getMethod("getHostComponents", java.lang.String.class, javax.ws.rs.core.HttpHeaders.class, javax.ws.rs.core.UriInfo.class, java.lang.String.class);
        args = new java.lang.Object[]{ null, getHttpHeaders(), getUriInfo(), null };
        listInvocations.add(new org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation(org.apache.ambari.server.api.services.Request.Type.GET, componentService, m, args, null));
        componentService = new org.apache.ambari.server.api.services.HostComponentServiceTest.TestHostComponentService("clusterName", "serviceName", "componentName");
        m = componentService.getClass().getMethod("createHostComponent", java.lang.String.class, javax.ws.rs.core.HttpHeaders.class, javax.ws.rs.core.UriInfo.class, java.lang.String.class);
        args = new java.lang.Object[]{ "body", getHttpHeaders(), getUriInfo(), "componentName" };
        listInvocations.add(new org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation(org.apache.ambari.server.api.services.Request.Type.POST, componentService, m, args, "body"));
        componentService = new org.apache.ambari.server.api.services.HostComponentServiceTest.TestHostComponentService("clusterName", "serviceName", null);
        m = componentService.getClass().getMethod("createHostComponents", java.lang.String.class, javax.ws.rs.core.HttpHeaders.class, javax.ws.rs.core.UriInfo.class);
        args = new java.lang.Object[]{ "body", getHttpHeaders(), getUriInfo() };
        listInvocations.add(new org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation(org.apache.ambari.server.api.services.Request.Type.POST, componentService, m, args, "body"));
        componentService = new org.apache.ambari.server.api.services.HostComponentServiceTest.TestHostComponentService("clusterName", "serviceName", "componentName");
        m = componentService.getClass().getMethod("updateHostComponent", java.lang.String.class, javax.ws.rs.core.HttpHeaders.class, javax.ws.rs.core.UriInfo.class, java.lang.String.class);
        args = new java.lang.Object[]{ "body", getHttpHeaders(), getUriInfo(), "componentName" };
        listInvocations.add(new org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation(org.apache.ambari.server.api.services.Request.Type.PUT, componentService, m, args, "body"));
        componentService = new org.apache.ambari.server.api.services.HostComponentServiceTest.TestHostComponentService("clusterName", "serviceName", null);
        m = componentService.getClass().getMethod("updateHostComponents", java.lang.String.class, javax.ws.rs.core.HttpHeaders.class, javax.ws.rs.core.UriInfo.class);
        args = new java.lang.Object[]{ "body", getHttpHeaders(), getUriInfo() };
        listInvocations.add(new org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation(org.apache.ambari.server.api.services.Request.Type.PUT, componentService, m, args, "body"));
        componentService = new org.apache.ambari.server.api.services.HostComponentServiceTest.TestHostComponentService("clusterName", "serviceName", "componentName");
        m = componentService.getClass().getMethod("deleteHostComponent", javax.ws.rs.core.HttpHeaders.class, javax.ws.rs.core.UriInfo.class, java.lang.String.class);
        args = new java.lang.Object[]{ getHttpHeaders(), getUriInfo(), "componentName" };
        listInvocations.add(new org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation(org.apache.ambari.server.api.services.Request.Type.DELETE, componentService, m, args, null));
        return listInvocations;
    }

    private class TestHostComponentService extends org.apache.ambari.server.api.services.HostComponentService {
        private java.lang.String m_clusterId;

        private java.lang.String m_hostId;

        private java.lang.String m_hostComponentId;

        private TestHostComponentService(java.lang.String clusterId, java.lang.String hostId, java.lang.String hostComponentId) {
            super(clusterId, hostId);
            m_clusterId = clusterId;
            m_hostId = hostId;
            m_hostComponentId = hostComponentId;
        }

        @java.lang.Override
        org.apache.ambari.server.api.resources.ResourceInstance createHostComponentResource(java.lang.String clusterName, java.lang.String hostName, java.lang.String hostComponentName) {
            org.junit.Assert.assertEquals(m_clusterId, clusterName);
            org.junit.Assert.assertEquals(m_hostId, hostName);
            org.junit.Assert.assertEquals(m_hostComponentId, hostComponentName);
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