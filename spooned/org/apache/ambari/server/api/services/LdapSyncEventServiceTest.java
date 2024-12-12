package org.apache.ambari.server.api.services;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.UriInfo;
public class LdapSyncEventServiceTest extends org.apache.ambari.server.api.services.BaseServiceTest {
    public java.util.List<org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation> getTestInvocations() throws java.lang.Exception {
        java.util.List<org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation> listInvocations = new java.util.ArrayList<>();
        org.apache.ambari.server.api.services.LdapSyncEventService permissionService = new org.apache.ambari.server.api.services.LdapSyncEventServiceTest.TestLdapSyncEventService();
        java.lang.reflect.Method m = permissionService.getClass().getMethod("getEvent", javax.ws.rs.core.HttpHeaders.class, javax.ws.rs.core.UriInfo.class, java.lang.String.class);
        java.lang.Object[] args = new java.lang.Object[]{ getHttpHeaders(), getUriInfo(), "1" };
        listInvocations.add(new org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation(org.apache.ambari.server.api.services.Request.Type.GET, permissionService, m, args, null));
        permissionService = new org.apache.ambari.server.api.services.LdapSyncEventServiceTest.TestLdapSyncEventService();
        m = permissionService.getClass().getMethod("getEvents", javax.ws.rs.core.HttpHeaders.class, javax.ws.rs.core.UriInfo.class);
        args = new java.lang.Object[]{ getHttpHeaders(), getUriInfo() };
        listInvocations.add(new org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation(org.apache.ambari.server.api.services.Request.Type.GET, permissionService, m, args, null));
        permissionService = new org.apache.ambari.server.api.services.LdapSyncEventServiceTest.TestLdapSyncEventService();
        m = permissionService.getClass().getMethod("createEvent", java.lang.String.class, javax.ws.rs.core.HttpHeaders.class, javax.ws.rs.core.UriInfo.class);
        args = new java.lang.Object[]{ "body", getHttpHeaders(), getUriInfo() };
        listInvocations.add(new org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation(org.apache.ambari.server.api.services.Request.Type.POST, permissionService, m, args, "body"));
        permissionService = new org.apache.ambari.server.api.services.LdapSyncEventServiceTest.TestLdapSyncEventService();
        m = permissionService.getClass().getMethod("deleteEvent", javax.ws.rs.core.HttpHeaders.class, javax.ws.rs.core.UriInfo.class, java.lang.String.class);
        args = new java.lang.Object[]{ getHttpHeaders(), getUriInfo(), "1" };
        listInvocations.add(new org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation(org.apache.ambari.server.api.services.Request.Type.DELETE, permissionService, m, args, null));
        return listInvocations;
    }

    private class TestLdapSyncEventService extends org.apache.ambari.server.api.services.LdapSyncEventService {
        private TestLdapSyncEventService() {
            super();
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