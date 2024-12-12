package org.apache.ambari.server.api.services;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.UriInfo;
public class HostServiceTest extends org.apache.ambari.server.api.services.BaseServiceTest {
    public java.util.List<org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation> getTestInvocations() throws java.lang.Exception {
        java.util.List<org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation> listInvocations = new java.util.ArrayList<>();
        org.apache.ambari.server.api.services.HostService service = new org.apache.ambari.server.api.services.HostServiceTest.TestHostService("clusterName", "hostName");
        java.lang.reflect.Method m = service.getClass().getMethod("getHost", java.lang.String.class, javax.ws.rs.core.HttpHeaders.class, javax.ws.rs.core.UriInfo.class, java.lang.String.class);
        java.lang.Object[] args = new java.lang.Object[]{ null, getHttpHeaders(), getUriInfo(), "hostName" };
        listInvocations.add(new org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation(org.apache.ambari.server.api.services.Request.Type.GET, service, m, args, null));
        service = new org.apache.ambari.server.api.services.HostServiceTest.TestHostService("clusterName", null);
        m = service.getClass().getMethod("getHosts", java.lang.String.class, javax.ws.rs.core.HttpHeaders.class, javax.ws.rs.core.UriInfo.class);
        args = new java.lang.Object[]{ null, getHttpHeaders(), getUriInfo() };
        listInvocations.add(new org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation(org.apache.ambari.server.api.services.Request.Type.GET, service, m, args, null));
        service = new org.apache.ambari.server.api.services.HostServiceTest.TestHostService("clusterName", "hostName");
        m = service.getClass().getMethod("createHost", java.lang.String.class, javax.ws.rs.core.HttpHeaders.class, javax.ws.rs.core.UriInfo.class, java.lang.String.class);
        args = new java.lang.Object[]{ "body", getHttpHeaders(), getUriInfo(), "hostName" };
        listInvocations.add(new org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation(org.apache.ambari.server.api.services.Request.Type.POST, service, m, args, "body"));
        service = new org.apache.ambari.server.api.services.HostServiceTest.TestHostService("clusterName", null);
        m = service.getClass().getMethod("createHosts", java.lang.String.class, javax.ws.rs.core.HttpHeaders.class, javax.ws.rs.core.UriInfo.class);
        args = new java.lang.Object[]{ "body", getHttpHeaders(), getUriInfo() };
        listInvocations.add(new org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation(org.apache.ambari.server.api.services.Request.Type.POST, service, m, args, "body"));
        service = new org.apache.ambari.server.api.services.HostServiceTest.TestHostService("clusterName", "hostName");
        m = service.getClass().getMethod("updateHost", java.lang.String.class, javax.ws.rs.core.HttpHeaders.class, javax.ws.rs.core.UriInfo.class, java.lang.String.class);
        args = new java.lang.Object[]{ "body", getHttpHeaders(), getUriInfo(), "hostName" };
        listInvocations.add(new org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation(org.apache.ambari.server.api.services.Request.Type.PUT, service, m, args, "body"));
        service = new org.apache.ambari.server.api.services.HostServiceTest.TestHostService("clusterName", null);
        m = service.getClass().getMethod("updateHosts", java.lang.String.class, javax.ws.rs.core.HttpHeaders.class, javax.ws.rs.core.UriInfo.class);
        args = new java.lang.Object[]{ "body", getHttpHeaders(), getUriInfo() };
        listInvocations.add(new org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation(org.apache.ambari.server.api.services.Request.Type.PUT, service, m, args, "body"));
        service = new org.apache.ambari.server.api.services.HostServiceTest.TestHostService("clusterName", "hostName");
        m = service.getClass().getMethod("deleteHost", javax.ws.rs.core.HttpHeaders.class, javax.ws.rs.core.UriInfo.class, java.lang.String.class);
        args = new java.lang.Object[]{ getHttpHeaders(), getUriInfo(), "hostName" };
        listInvocations.add(new org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation(org.apache.ambari.server.api.services.Request.Type.DELETE, service, m, args, null));
        return listInvocations;
    }

    private class TestHostService extends org.apache.ambari.server.api.services.HostService {
        private java.lang.String m_clusterId;

        private java.lang.String m_hostId;

        private TestHostService(java.lang.String clusterId, java.lang.String hostId) {
            super(clusterId);
            m_clusterId = clusterId;
            m_hostId = hostId;
        }

        @java.lang.Override
        protected org.apache.ambari.server.api.resources.ResourceInstance createHostResource(java.lang.String clusterName, java.lang.String hostName) {
            org.junit.Assert.assertEquals(m_clusterId, clusterName);
            org.junit.Assert.assertEquals(m_hostId, hostName);
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