package org.apache.ambari.server.api.services;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.UriInfo;
public class ConfigGroupServiceTest extends org.apache.ambari.server.api.services.BaseServiceTest {
    @java.lang.Override
    public java.util.List<org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation> getTestInvocations() throws java.lang.Exception {
        java.util.List<org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation> listInvocations = new java.util.ArrayList<>();
        org.apache.ambari.server.api.services.ConfigGroupService configGroupService = new org.apache.ambari.server.api.services.ConfigGroupServiceTest.TestConfigGroupService("clusterName", null);
        java.lang.reflect.Method m = configGroupService.getClass().getMethod("getConfigGroups", java.lang.String.class, javax.ws.rs.core.HttpHeaders.class, javax.ws.rs.core.UriInfo.class);
        java.lang.Object[] args = new java.lang.Object[]{ null, getHttpHeaders(), getUriInfo() };
        listInvocations.add(new org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation(org.apache.ambari.server.api.services.Request.Type.GET, configGroupService, m, args, null));
        configGroupService = new org.apache.ambari.server.api.services.ConfigGroupServiceTest.TestConfigGroupService("clusterName", "groupId");
        m = configGroupService.getClass().getMethod("getConfigGroup", java.lang.String.class, javax.ws.rs.core.HttpHeaders.class, javax.ws.rs.core.UriInfo.class, java.lang.String.class);
        args = new java.lang.Object[]{ null, getHttpHeaders(), getUriInfo(), "groupId" };
        listInvocations.add(new org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation(org.apache.ambari.server.api.services.Request.Type.GET, configGroupService, m, args, null));
        configGroupService = new org.apache.ambari.server.api.services.ConfigGroupServiceTest.TestConfigGroupService("clusterName", null);
        m = configGroupService.getClass().getMethod("createConfigGroup", java.lang.String.class, javax.ws.rs.core.HttpHeaders.class, javax.ws.rs.core.UriInfo.class);
        args = new java.lang.Object[]{ "body", getHttpHeaders(), getUriInfo() };
        listInvocations.add(new org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation(org.apache.ambari.server.api.services.Request.Type.POST, configGroupService, m, args, "body"));
        configGroupService = new org.apache.ambari.server.api.services.ConfigGroupServiceTest.TestConfigGroupService("clusterName", "groupId");
        m = configGroupService.getClass().getMethod("deleteConfigGroup", javax.ws.rs.core.HttpHeaders.class, javax.ws.rs.core.UriInfo.class, java.lang.String.class);
        args = new java.lang.Object[]{ getHttpHeaders(), getUriInfo(), "groupId" };
        listInvocations.add(new org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation(org.apache.ambari.server.api.services.Request.Type.DELETE, configGroupService, m, args, null));
        configGroupService = new org.apache.ambari.server.api.services.ConfigGroupServiceTest.TestConfigGroupService("clusterName", "groupId");
        m = configGroupService.getClass().getMethod("updateConfigGroup", java.lang.String.class, javax.ws.rs.core.HttpHeaders.class, javax.ws.rs.core.UriInfo.class, java.lang.String.class);
        args = new java.lang.Object[]{ "body", getHttpHeaders(), getUriInfo(), "groupId" };
        listInvocations.add(new org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation(org.apache.ambari.server.api.services.Request.Type.PUT, configGroupService, m, args, "body"));
        return listInvocations;
    }

    private class TestConfigGroupService extends org.apache.ambari.server.api.services.ConfigGroupService {
        private java.lang.String m_clusterName;

        private java.lang.String m_groupId;

        public TestConfigGroupService(java.lang.String m_clusterName, java.lang.String groupId) {
            super(m_clusterName);
            this.m_clusterName = m_clusterName;
            this.m_groupId = groupId;
        }

        @java.lang.Override
        org.apache.ambari.server.api.resources.ResourceInstance createConfigGroupResource(java.lang.String clusterName, java.lang.String groupId) {
            junit.framework.Assert.assertEquals(m_clusterName, clusterName);
            junit.framework.Assert.assertEquals(m_groupId, groupId);
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