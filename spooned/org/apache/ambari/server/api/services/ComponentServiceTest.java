package org.apache.ambari.server.api.services;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.UriInfo;
public class ComponentServiceTest extends org.apache.ambari.server.api.services.BaseServiceTest {
    public java.util.List<org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation> getTestInvocations() throws java.lang.Exception {
        java.util.List<org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation> listInvocations = new java.util.ArrayList<>();
        org.apache.ambari.server.api.services.ComponentService service = new org.apache.ambari.server.api.services.ComponentServiceTest.TestComponentService("clusterName", "serviceName", "componentName");
        java.lang.reflect.Method m = service.getClass().getMethod("getComponent", java.lang.String.class, javax.ws.rs.core.HttpHeaders.class, javax.ws.rs.core.UriInfo.class, java.lang.String.class, java.lang.String.class);
        java.lang.Object[] args = new java.lang.Object[]{ null, getHttpHeaders(), getUriInfo(), "componentName", null };
        listInvocations.add(new org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation(org.apache.ambari.server.api.services.Request.Type.GET, service, m, args, null));
        service = new org.apache.ambari.server.api.services.ComponentServiceTest.TestComponentService("clusterName", "serviceName", null);
        m = service.getClass().getMethod("getComponents", java.lang.String.class, javax.ws.rs.core.HttpHeaders.class, javax.ws.rs.core.UriInfo.class, java.lang.String.class);
        args = new java.lang.Object[]{ null, getHttpHeaders(), getUriInfo(), null };
        listInvocations.add(new org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation(org.apache.ambari.server.api.services.Request.Type.GET, service, m, args, null));
        service = new org.apache.ambari.server.api.services.ComponentServiceTest.TestComponentService("clusterName", "serviceName", "componentName");
        m = service.getClass().getMethod("createComponent", java.lang.String.class, javax.ws.rs.core.HttpHeaders.class, javax.ws.rs.core.UriInfo.class, java.lang.String.class);
        args = new java.lang.Object[]{ "body", getHttpHeaders(), getUriInfo(), "componentName" };
        listInvocations.add(new org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation(org.apache.ambari.server.api.services.Request.Type.POST, service, m, args, "body"));
        service = new org.apache.ambari.server.api.services.ComponentServiceTest.TestComponentService("clusterName", "serviceName", null);
        m = service.getClass().getMethod("createComponents", java.lang.String.class, javax.ws.rs.core.HttpHeaders.class, javax.ws.rs.core.UriInfo.class);
        args = new java.lang.Object[]{ "body", getHttpHeaders(), getUriInfo() };
        listInvocations.add(new org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation(org.apache.ambari.server.api.services.Request.Type.POST, service, m, args, "body"));
        service = new org.apache.ambari.server.api.services.ComponentServiceTest.TestComponentService("clusterName", "serviceName", "componentName");
        m = service.getClass().getMethod("updateComponent", java.lang.String.class, javax.ws.rs.core.HttpHeaders.class, javax.ws.rs.core.UriInfo.class, java.lang.String.class);
        args = new java.lang.Object[]{ "body", getHttpHeaders(), getUriInfo(), "componentName" };
        listInvocations.add(new org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation(org.apache.ambari.server.api.services.Request.Type.PUT, service, m, args, "body"));
        service = new org.apache.ambari.server.api.services.ComponentServiceTest.TestComponentService("clusterName", "serviceName", null);
        m = service.getClass().getMethod("updateComponents", java.lang.String.class, javax.ws.rs.core.HttpHeaders.class, javax.ws.rs.core.UriInfo.class);
        args = new java.lang.Object[]{ "body", getHttpHeaders(), getUriInfo() };
        listInvocations.add(new org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation(org.apache.ambari.server.api.services.Request.Type.PUT, service, m, args, "body"));
        service = new org.apache.ambari.server.api.services.ComponentServiceTest.TestComponentService("clusterName", "serviceName", "componentName");
        m = service.getClass().getMethod("deleteComponent", javax.ws.rs.core.HttpHeaders.class, javax.ws.rs.core.UriInfo.class, java.lang.String.class);
        args = new java.lang.Object[]{ getHttpHeaders(), getUriInfo(), "componentName" };
        listInvocations.add(new org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation(org.apache.ambari.server.api.services.Request.Type.DELETE, service, m, args, null));
        return listInvocations;
    }

    private class TestComponentService extends org.apache.ambari.server.api.services.ComponentService {
        private java.lang.String m_clusterId;

        private java.lang.String m_serviceId;

        private java.lang.String m_componentId;

        private TestComponentService(java.lang.String clusterId, java.lang.String serviceId, java.lang.String componentId) {
            super(clusterId, serviceId);
            m_clusterId = clusterId;
            m_serviceId = serviceId;
            m_componentId = componentId;
        }

        @java.lang.Override
        org.apache.ambari.server.api.resources.ResourceInstance createComponentResource(java.lang.String clusterName, java.lang.String serviceName, java.lang.String componentName) {
            org.junit.Assert.assertEquals(m_clusterId, clusterName);
            org.junit.Assert.assertEquals(m_serviceId, serviceName);
            org.junit.Assert.assertEquals(m_componentId, componentName);
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