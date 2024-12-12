package org.apache.ambari.server.api.services;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.UriInfo;
public class ValidationServiceTest extends org.apache.ambari.server.api.services.BaseServiceTest {
    @java.lang.Override
    public java.util.List<org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation> getTestInvocations() throws java.lang.Exception {
        java.util.List<org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation> listInvocations = new java.util.ArrayList<>();
        org.apache.ambari.server.api.services.ValidationService service = new org.apache.ambari.server.api.services.ValidationServiceTest.TestValidationService("stackName", "stackVersion");
        java.lang.reflect.Method m = service.getClass().getMethod("getValidation", java.lang.String.class, javax.ws.rs.core.HttpHeaders.class, javax.ws.rs.core.UriInfo.class, java.lang.String.class, java.lang.String.class);
        java.lang.Object[] args = new java.lang.Object[]{ "body", getHttpHeaders(), getUriInfo(), "stackName", "stackVersion" };
        listInvocations.add(new org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation(org.apache.ambari.server.api.services.Request.Type.POST, service, m, args, "body"));
        return listInvocations;
    }

    private class TestValidationService extends org.apache.ambari.server.api.services.ValidationService {
        private java.lang.String stackName;

        private java.lang.String stackVersion;

        private TestValidationService(java.lang.String stackName, java.lang.String stackVersion) {
            super();
            this.stackName = stackName;
            this.stackVersion = stackVersion;
        }

        @java.lang.Override
        org.apache.ambari.server.api.resources.ResourceInstance createValidationResource(java.lang.String stackName, java.lang.String stackVersion) {
            org.junit.Assert.assertEquals(this.stackName, stackName);
            org.junit.Assert.assertEquals(this.stackVersion, stackVersion);
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