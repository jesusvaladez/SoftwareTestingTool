package org.apache.ambari.server.api.services;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.UriInfo;
public class RoleAuthorizationServiceTest extends org.apache.ambari.server.api.services.BaseServiceTest {
    public java.util.List<org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation> getTestInvocations() throws java.lang.Exception {
        java.util.List<org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation> listInvocations = new java.util.ArrayList<>();
        org.apache.ambari.server.api.services.RoleAuthorizationService service = new org.apache.ambari.server.api.services.RoleAuthorizationServiceTest.TestRoleAuthorizationService("id");
        java.lang.reflect.Method m = service.getClass().getMethod("getAuthorization", javax.ws.rs.core.HttpHeaders.class, javax.ws.rs.core.UriInfo.class, java.lang.String.class);
        java.lang.Object[] args = new java.lang.Object[]{ getHttpHeaders(), getUriInfo(), "id" };
        listInvocations.add(new org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation(org.apache.ambari.server.api.services.Request.Type.GET, service, m, args, null));
        service = new org.apache.ambari.server.api.services.RoleAuthorizationServiceTest.TestRoleAuthorizationService(null);
        m = service.getClass().getMethod("getAuthorizations", javax.ws.rs.core.HttpHeaders.class, javax.ws.rs.core.UriInfo.class);
        args = new java.lang.Object[]{ getHttpHeaders(), getUriInfo() };
        listInvocations.add(new org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation(org.apache.ambari.server.api.services.Request.Type.GET, service, m, args, null));
        return listInvocations;
    }

    private class TestRoleAuthorizationService extends org.apache.ambari.server.api.services.RoleAuthorizationService {
        private java.lang.String id;

        private TestRoleAuthorizationService(java.lang.String id) {
            this.id = id;
        }

        @java.lang.Override
        protected org.apache.ambari.server.api.resources.ResourceInstance createAuthorizationResource(java.lang.String id) {
            org.junit.Assert.assertEquals(this.id, id);
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