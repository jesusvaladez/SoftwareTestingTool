package org.apache.ambari.server.api.services;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.UriInfo;
public class ClusterPrivilegeServiceTest extends org.apache.ambari.server.api.services.BaseServiceTest {
    public java.util.List<org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation> getTestInvocations() throws java.lang.Exception {
        java.util.List<org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation> listInvocations = new java.util.ArrayList<>();
        org.apache.ambari.server.api.services.ClusterPrivilegeService privilegeService = new org.apache.ambari.server.api.services.ClusterPrivilegeServiceTest.TestClusterPrivilegeService("c1");
        java.lang.reflect.Method m = privilegeService.getClass().getMethod("getPrivilege", javax.ws.rs.core.HttpHeaders.class, javax.ws.rs.core.UriInfo.class, java.lang.String.class);
        java.lang.Object[] args = new java.lang.Object[]{ getHttpHeaders(), getUriInfo(), "privilegename" };
        listInvocations.add(new org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation(org.apache.ambari.server.api.services.Request.Type.GET, privilegeService, m, args, null));
        privilegeService = new org.apache.ambari.server.api.services.ClusterPrivilegeServiceTest.TestClusterPrivilegeService("c1");
        m = privilegeService.getClass().getMethod("getPrivileges", javax.ws.rs.core.HttpHeaders.class, javax.ws.rs.core.UriInfo.class);
        args = new java.lang.Object[]{ getHttpHeaders(), getUriInfo() };
        listInvocations.add(new org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation(org.apache.ambari.server.api.services.Request.Type.GET, privilegeService, m, args, null));
        privilegeService = new org.apache.ambari.server.api.services.ClusterPrivilegeServiceTest.TestClusterPrivilegeService("c1");
        m = privilegeService.getClass().getMethod("createPrivilege", java.lang.String.class, javax.ws.rs.core.HttpHeaders.class, javax.ws.rs.core.UriInfo.class);
        args = new java.lang.Object[]{ "body", getHttpHeaders(), getUriInfo() };
        listInvocations.add(new org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation(org.apache.ambari.server.api.services.Request.Type.POST, privilegeService, m, args, "body"));
        privilegeService = new org.apache.ambari.server.api.services.ClusterPrivilegeServiceTest.TestClusterPrivilegeService("c1");
        m = privilegeService.getClass().getMethod("deletePrivilege", javax.ws.rs.core.HttpHeaders.class, javax.ws.rs.core.UriInfo.class, java.lang.String.class);
        args = new java.lang.Object[]{ getHttpHeaders(), getUriInfo(), "privilegename" };
        listInvocations.add(new org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation(org.apache.ambari.server.api.services.Request.Type.DELETE, privilegeService, m, args, null));
        return listInvocations;
    }

    private class TestClusterPrivilegeService extends org.apache.ambari.server.api.services.ClusterPrivilegeService {
        private TestClusterPrivilegeService(java.lang.String clusterName) {
            super(clusterName);
        }

        @java.lang.Override
        protected org.apache.ambari.server.api.resources.ResourceInstance createPrivilegeResource(java.lang.String clusterName) {
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