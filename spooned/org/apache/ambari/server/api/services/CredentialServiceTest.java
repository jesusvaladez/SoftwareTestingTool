package org.apache.ambari.server.api.services;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.UriInfo;
public class CredentialServiceTest extends org.apache.ambari.server.api.services.BaseServiceTest {
    public java.util.List<org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation> getTestInvocations() throws java.lang.Exception {
        java.util.List<org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation> listInvocations = new java.util.ArrayList<>();
        org.apache.ambari.server.api.services.CredentialService CredentialService = new org.apache.ambari.server.api.services.CredentialServiceTest.TestCredentialService("alias");
        java.lang.reflect.Method m = CredentialService.getClass().getMethod("getCredential", javax.ws.rs.core.HttpHeaders.class, javax.ws.rs.core.UriInfo.class, java.lang.String.class);
        java.lang.Object[] args = new java.lang.Object[]{ getHttpHeaders(), getUriInfo(), "alias" };
        listInvocations.add(new org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation(org.apache.ambari.server.api.services.Request.Type.GET, CredentialService, m, args, null));
        CredentialService = new org.apache.ambari.server.api.services.CredentialServiceTest.TestCredentialService(null);
        m = CredentialService.getClass().getMethod("getCredentials", javax.ws.rs.core.HttpHeaders.class, javax.ws.rs.core.UriInfo.class);
        args = new java.lang.Object[]{ getHttpHeaders(), getUriInfo() };
        listInvocations.add(new org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation(org.apache.ambari.server.api.services.Request.Type.GET, CredentialService, m, args, null));
        CredentialService = new org.apache.ambari.server.api.services.CredentialServiceTest.TestCredentialService("alias");
        m = CredentialService.getClass().getMethod("createCredential", java.lang.String.class, javax.ws.rs.core.HttpHeaders.class, javax.ws.rs.core.UriInfo.class, java.lang.String.class);
        args = new java.lang.Object[]{ "body", getHttpHeaders(), getUriInfo(), "alias" };
        listInvocations.add(new org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation(org.apache.ambari.server.api.services.Request.Type.POST, CredentialService, m, args, "body"));
        CredentialService = new org.apache.ambari.server.api.services.CredentialServiceTest.TestCredentialService("alias");
        m = CredentialService.getClass().getMethod("deleteCredential", javax.ws.rs.core.HttpHeaders.class, javax.ws.rs.core.UriInfo.class, java.lang.String.class);
        args = new java.lang.Object[]{ getHttpHeaders(), getUriInfo(), "alias" };
        listInvocations.add(new org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation(org.apache.ambari.server.api.services.Request.Type.DELETE, CredentialService, m, args, null));
        return listInvocations;
    }

    private class TestCredentialService extends org.apache.ambari.server.api.services.CredentialService {
        private java.lang.String alias;

        private TestCredentialService(java.lang.String alias) {
            super("C1");
            this.alias = alias;
        }

        @java.lang.Override
        org.apache.ambari.server.api.resources.ResourceInstance createCredentialResource(java.lang.String alias) {
            org.junit.Assert.assertEquals(this.alias, alias);
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