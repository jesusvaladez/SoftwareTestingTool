package org.apache.ambari.server.api.services;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.UriInfo;
public class RootServiceServiceTest extends org.apache.ambari.server.api.services.BaseServiceTest {
    @java.lang.Override
    public java.util.List<org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation> getTestInvocations() throws java.lang.Exception {
        java.util.List<org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation> listInvocations = new java.util.ArrayList<>();
        org.apache.ambari.server.api.services.RootServiceService service = new org.apache.ambari.server.api.services.RootServiceServiceTest.TestRootServiceService(null, null, null);
        java.lang.reflect.Method m = service.getClass().getMethod("getRootServices", java.lang.String.class, javax.ws.rs.core.HttpHeaders.class, javax.ws.rs.core.UriInfo.class);
        java.lang.Object[] args = new java.lang.Object[]{ null, getHttpHeaders(), getUriInfo() };
        listInvocations.add(new org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation(org.apache.ambari.server.api.services.Request.Type.GET, service, m, args, null));
        service = new org.apache.ambari.server.api.services.RootServiceServiceTest.TestRootServiceService("AMBARI", null, null);
        m = service.getClass().getMethod("getRootService", java.lang.String.class, javax.ws.rs.core.HttpHeaders.class, javax.ws.rs.core.UriInfo.class, java.lang.String.class);
        args = new java.lang.Object[]{ null, getHttpHeaders(), getUriInfo(), "AMBARI" };
        listInvocations.add(new org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation(org.apache.ambari.server.api.services.Request.Type.GET, service, m, args, null));
        service = new org.apache.ambari.server.api.services.RootServiceServiceTest.TestRootServiceService("AMBARI", null, null);
        m = service.getClass().getMethod("getRootServiceComponents", java.lang.String.class, javax.ws.rs.core.HttpHeaders.class, javax.ws.rs.core.UriInfo.class, java.lang.String.class);
        args = new java.lang.Object[]{ null, getHttpHeaders(), getUriInfo(), "AMBARI" };
        listInvocations.add(new org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation(org.apache.ambari.server.api.services.Request.Type.GET, service, m, args, null));
        service = new org.apache.ambari.server.api.services.RootServiceServiceTest.TestRootServiceService("AMBARI", "AMBARI_SERVER", null);
        m = service.getClass().getMethod("getRootServiceComponent", java.lang.String.class, javax.ws.rs.core.HttpHeaders.class, javax.ws.rs.core.UriInfo.class, java.lang.String.class, java.lang.String.class);
        args = new java.lang.Object[]{ null, getHttpHeaders(), getUriInfo(), "AMBARI", "AMBARI_SERVER" };
        listInvocations.add(new org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation(org.apache.ambari.server.api.services.Request.Type.GET, service, m, args, null));
        service = new org.apache.ambari.server.api.services.RootServiceServiceTest.TestRootServiceService("AMBARI", "AMBARI_SERVER", null);
        m = service.getClass().getMethod("getRootServiceComponentHosts", java.lang.String.class, javax.ws.rs.core.HttpHeaders.class, javax.ws.rs.core.UriInfo.class, java.lang.String.class, java.lang.String.class);
        args = new java.lang.Object[]{ null, getHttpHeaders(), getUriInfo(), "AMBARI", "AMBARI_SERVER" };
        listInvocations.add(new org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation(org.apache.ambari.server.api.services.Request.Type.GET, service, m, args, null));
        service = new org.apache.ambari.server.api.services.RootServiceServiceTest.TestRootServiceService("AMBARI", "AMBARI_SERVER", null);
        m = service.getClass().getMethod("getRootHosts", java.lang.String.class, javax.ws.rs.core.HttpHeaders.class, javax.ws.rs.core.UriInfo.class);
        args = new java.lang.Object[]{ null, getHttpHeaders(), getUriInfo() };
        listInvocations.add(new org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation(org.apache.ambari.server.api.services.Request.Type.GET, service, m, args, null));
        service = new org.apache.ambari.server.api.services.RootServiceServiceTest.TestRootServiceService("AMBARI", "AMBARI_SERVER", "host1");
        m = service.getClass().getMethod("getRootHost", java.lang.String.class, javax.ws.rs.core.HttpHeaders.class, javax.ws.rs.core.UriInfo.class, java.lang.String.class);
        args = new java.lang.Object[]{ null, getHttpHeaders(), getUriInfo(), "host1" };
        listInvocations.add(new org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation(org.apache.ambari.server.api.services.Request.Type.GET, service, m, args, null));
        return listInvocations;
    }

    private class TestRootServiceService extends org.apache.ambari.server.api.services.RootServiceService {
        private java.lang.String m_serviceName;

        private java.lang.String m_componentName;

        private java.lang.String m_hostName;

        private TestRootServiceService(java.lang.String serviceName, java.lang.String componentName, java.lang.String hostName) {
            m_serviceName = serviceName;
            m_componentName = componentName;
            m_hostName = hostName;
        }

        @java.lang.Override
        protected org.apache.ambari.server.api.resources.ResourceInstance createServiceResource(java.lang.String serviceName) {
            org.junit.Assert.assertEquals(m_serviceName, serviceName);
            return getTestResource();
        }

        @java.lang.Override
        protected org.apache.ambari.server.api.resources.ResourceInstance createServiceComponentResource(java.lang.String serviceName, java.lang.String componentName) {
            org.junit.Assert.assertEquals(m_serviceName, serviceName);
            org.junit.Assert.assertEquals(m_componentName, componentName);
            return getTestResource();
        }

        @java.lang.Override
        protected org.apache.ambari.server.api.resources.ResourceInstance createHostComponentResource(java.lang.String serviceName, java.lang.String hostName, java.lang.String componentName) {
            org.junit.Assert.assertEquals(m_serviceName, serviceName);
            org.junit.Assert.assertEquals(m_hostName, hostName);
            org.junit.Assert.assertEquals(m_componentName, componentName);
            return getTestResource();
        }

        @java.lang.Override
        protected org.apache.ambari.server.api.resources.ResourceInstance createHostResource(java.lang.String hostName) {
            org.junit.Assert.assertEquals(m_hostName, hostName);
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