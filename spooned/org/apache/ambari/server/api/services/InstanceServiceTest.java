package org.apache.ambari.server.api.services;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.UriInfo;
public class InstanceServiceTest extends org.apache.ambari.server.api.services.BaseServiceTest {
    public java.util.List<org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation> getTestInvocations() throws java.lang.Exception {
        java.util.List<org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation> listInvocations = new java.util.ArrayList<>();
        org.apache.ambari.server.api.services.InstanceService service = new org.apache.ambari.server.api.services.InstanceServiceTest.TestInstanceService("clusterName", "instanceName");
        java.lang.reflect.Method m = service.getClass().getMethod("getInstance", java.lang.String.class, javax.ws.rs.core.HttpHeaders.class, javax.ws.rs.core.UriInfo.class, java.lang.String.class);
        java.lang.Object[] args = new java.lang.Object[]{ null, getHttpHeaders(), getUriInfo(), "instanceName" };
        listInvocations.add(new org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation(org.apache.ambari.server.api.services.Request.Type.GET, service, m, args, null));
        service = new org.apache.ambari.server.api.services.InstanceServiceTest.TestInstanceService("clusterName", null);
        m = service.getClass().getMethod("getInstances", java.lang.String.class, javax.ws.rs.core.HttpHeaders.class, javax.ws.rs.core.UriInfo.class);
        args = new java.lang.Object[]{ null, getHttpHeaders(), getUriInfo() };
        listInvocations.add(new org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation(org.apache.ambari.server.api.services.Request.Type.GET, service, m, args, null));
        service = new org.apache.ambari.server.api.services.InstanceServiceTest.TestInstanceService("clusterName", "instanceName");
        m = service.getClass().getMethod("createInstance", java.lang.String.class, javax.ws.rs.core.HttpHeaders.class, javax.ws.rs.core.UriInfo.class, java.lang.String.class);
        args = new java.lang.Object[]{ "body", getHttpHeaders(), getUriInfo(), "instanceName" };
        listInvocations.add(new org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation(org.apache.ambari.server.api.services.Request.Type.POST, service, m, args, "body"));
        service = new org.apache.ambari.server.api.services.InstanceServiceTest.TestInstanceService("clusterName", "instanceName");
        m = service.getClass().getMethod("updateInstance", java.lang.String.class, javax.ws.rs.core.HttpHeaders.class, javax.ws.rs.core.UriInfo.class, java.lang.String.class);
        args = new java.lang.Object[]{ "body", getHttpHeaders(), getUriInfo(), "instanceName" };
        listInvocations.add(new org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation(org.apache.ambari.server.api.services.Request.Type.PUT, service, m, args, "body"));
        service = new org.apache.ambari.server.api.services.InstanceServiceTest.TestInstanceService("clusterName", "instanceName");
        m = service.getClass().getMethod("deleteInstance", javax.ws.rs.core.HttpHeaders.class, javax.ws.rs.core.UriInfo.class, java.lang.String.class);
        args = new java.lang.Object[]{ getHttpHeaders(), getUriInfo(), "instanceName" };
        listInvocations.add(new org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation(org.apache.ambari.server.api.services.Request.Type.DELETE, service, m, args, null));
        return listInvocations;
    }

    private class TestInstanceService extends org.apache.ambari.server.api.services.InstanceService {
        private java.lang.String m_feedName;

        private java.lang.String m_instanceId;

        private TestInstanceService(java.lang.String feedName, java.lang.String instanceId) {
            super(feedName);
            m_feedName = feedName;
            m_instanceId = instanceId;
        }

        @java.lang.Override
        org.apache.ambari.server.api.resources.ResourceInstance createInstanceResource(java.lang.String feedName, java.lang.String instanceID, javax.ws.rs.core.UriInfo ui) {
            org.junit.Assert.assertEquals(m_feedName, feedName);
            org.junit.Assert.assertEquals(m_instanceId, instanceID);
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