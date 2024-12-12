package org.apache.ambari.server.api.services;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.UriInfo;
public class ExtensionsServiceTest extends org.apache.ambari.server.api.services.BaseServiceTest {
    @java.lang.Override
    public java.util.List<org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation> getTestInvocations() throws java.lang.Exception {
        java.util.List<org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation> listInvocations = new java.util.ArrayList<>();
        org.apache.ambari.server.api.services.ExtensionsService service = new org.apache.ambari.server.api.services.ExtensionsServiceTest.TestExtensionsService("extensionName", null);
        java.lang.reflect.Method m = service.getClass().getMethod("getExtension", java.lang.String.class, javax.ws.rs.core.HttpHeaders.class, javax.ws.rs.core.UriInfo.class, java.lang.String.class);
        java.lang.Object[] args = new java.lang.Object[]{ null, getHttpHeaders(), getUriInfo(), "extensionName" };
        listInvocations.add(new org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation(org.apache.ambari.server.api.services.Request.Type.GET, service, m, args, null));
        service = new org.apache.ambari.server.api.services.ExtensionsServiceTest.TestExtensionsService(null, null);
        m = service.getClass().getMethod("getExtensions", java.lang.String.class, javax.ws.rs.core.HttpHeaders.class, javax.ws.rs.core.UriInfo.class);
        args = new java.lang.Object[]{ null, getHttpHeaders(), getUriInfo() };
        listInvocations.add(new org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation(org.apache.ambari.server.api.services.Request.Type.GET, service, m, args, null));
        service = new org.apache.ambari.server.api.services.ExtensionsServiceTest.TestExtensionsService("extensionName", "extensionVersion");
        m = service.getClass().getMethod("getExtensionVersion", java.lang.String.class, javax.ws.rs.core.HttpHeaders.class, javax.ws.rs.core.UriInfo.class, java.lang.String.class, java.lang.String.class);
        args = new java.lang.Object[]{ null, getHttpHeaders(), getUriInfo(), "extensionName", "extensionVersion" };
        listInvocations.add(new org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation(org.apache.ambari.server.api.services.Request.Type.GET, service, m, args, null));
        service = new org.apache.ambari.server.api.services.ExtensionsServiceTest.TestExtensionsService("extensionName", null);
        m = service.getClass().getMethod("getExtensionVersions", java.lang.String.class, javax.ws.rs.core.HttpHeaders.class, javax.ws.rs.core.UriInfo.class, java.lang.String.class);
        args = new java.lang.Object[]{ null, getHttpHeaders(), getUriInfo(), "extensionName" };
        listInvocations.add(new org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation(org.apache.ambari.server.api.services.Request.Type.GET, service, m, args, null));
        return listInvocations;
    }

    private class TestExtensionsService extends org.apache.ambari.server.api.services.ExtensionsService {
        private java.lang.String m_extensionId;

        private java.lang.String m_extensionVersion;

        private TestExtensionsService(java.lang.String extensionName, java.lang.String extensionVersion) {
            m_extensionId = extensionName;
            m_extensionVersion = extensionVersion;
        }

        @java.lang.Override
        org.apache.ambari.server.api.resources.ResourceInstance createExtensionResource(java.lang.String extensionName) {
            org.junit.Assert.assertEquals(m_extensionId, extensionName);
            return getTestResource();
        }

        @java.lang.Override
        org.apache.ambari.server.api.resources.ResourceInstance createExtensionVersionResource(java.lang.String extensionName, java.lang.String extensionVersion) {
            org.junit.Assert.assertEquals(m_extensionId, extensionName);
            org.junit.Assert.assertEquals(m_extensionVersion, extensionVersion);
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