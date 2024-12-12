package org.apache.ambari.server.api.services;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.UriInfo;
public class StacksServiceTest extends org.apache.ambari.server.api.services.BaseServiceTest {
    @java.lang.Override
    public java.util.List<org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation> getTestInvocations() throws java.lang.Exception {
        java.util.List<org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation> listInvocations = new java.util.ArrayList<>();
        org.apache.ambari.server.api.services.StacksService service = new org.apache.ambari.server.api.services.StacksServiceTest.TestStacksService("stackName", null);
        java.lang.reflect.Method m = service.getClass().getMethod("getStack", java.lang.String.class, javax.ws.rs.core.HttpHeaders.class, javax.ws.rs.core.UriInfo.class, java.lang.String.class);
        java.lang.Object[] args = new java.lang.Object[]{ null, getHttpHeaders(), getUriInfo(), "stackName" };
        listInvocations.add(new org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation(org.apache.ambari.server.api.services.Request.Type.GET, service, m, args, null));
        service = new org.apache.ambari.server.api.services.StacksServiceTest.TestStacksService(null, null);
        m = service.getClass().getMethod("getStacks", java.lang.String.class, javax.ws.rs.core.HttpHeaders.class, javax.ws.rs.core.UriInfo.class);
        args = new java.lang.Object[]{ null, getHttpHeaders(), getUriInfo() };
        listInvocations.add(new org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation(org.apache.ambari.server.api.services.Request.Type.GET, service, m, args, null));
        service = new org.apache.ambari.server.api.services.StacksServiceTest.TestStacksService("stackName", "stackVersion");
        m = service.getClass().getMethod("getStackVersion", java.lang.String.class, javax.ws.rs.core.HttpHeaders.class, javax.ws.rs.core.UriInfo.class, java.lang.String.class, java.lang.String.class);
        args = new java.lang.Object[]{ null, getHttpHeaders(), getUriInfo(), "stackName", "stackVersion" };
        listInvocations.add(new org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation(org.apache.ambari.server.api.services.Request.Type.GET, service, m, args, null));
        service = new org.apache.ambari.server.api.services.StacksServiceTest.TestStacksService("stackName", null);
        m = service.getClass().getMethod("getStackVersions", java.lang.String.class, javax.ws.rs.core.HttpHeaders.class, javax.ws.rs.core.UriInfo.class, java.lang.String.class);
        args = new java.lang.Object[]{ null, getHttpHeaders(), getUriInfo(), "stackName" };
        listInvocations.add(new org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation(org.apache.ambari.server.api.services.Request.Type.GET, service, m, args, null));
        service = new org.apache.ambari.server.api.services.StacksServiceTest.TestStacksService("stackName", null);
        m = service.getClass().getMethod("getStackService", java.lang.String.class, javax.ws.rs.core.HttpHeaders.class, javax.ws.rs.core.UriInfo.class, java.lang.String.class, java.lang.String.class, java.lang.String.class);
        args = new java.lang.Object[]{ null, getHttpHeaders(), getUriInfo(), "stackName", "stackVersion", "service-name" };
        listInvocations.add(new org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation(org.apache.ambari.server.api.services.Request.Type.GET, service, m, args, null));
        service = new org.apache.ambari.server.api.services.StacksServiceTest.TestStacksService("stackName", null);
        m = service.getClass().getMethod("getStackServices", java.lang.String.class, javax.ws.rs.core.HttpHeaders.class, javax.ws.rs.core.UriInfo.class, java.lang.String.class, java.lang.String.class);
        args = new java.lang.Object[]{ null, getHttpHeaders(), getUriInfo(), "stackName", "stackVersion" };
        listInvocations.add(new org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation(org.apache.ambari.server.api.services.Request.Type.GET, service, m, args, null));
        service = new org.apache.ambari.server.api.services.StacksServiceTest.TestStacksService("stackName", "stackVersion");
        m = service.getClass().getMethod("getStackConfiguration", java.lang.String.class, javax.ws.rs.core.HttpHeaders.class, javax.ws.rs.core.UriInfo.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class);
        args = new java.lang.Object[]{ null, getHttpHeaders(), getUriInfo(), "stackName", "stackVersion", "service-name", "property-name" };
        listInvocations.add(new org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation(org.apache.ambari.server.api.services.Request.Type.GET, service, m, args, null));
        service = new org.apache.ambari.server.api.services.StacksServiceTest.TestStacksService("stackName", null);
        m = service.getClass().getMethod("getStackConfigurations", java.lang.String.class, javax.ws.rs.core.HttpHeaders.class, javax.ws.rs.core.UriInfo.class, java.lang.String.class, java.lang.String.class, java.lang.String.class);
        args = new java.lang.Object[]{ null, getHttpHeaders(), getUriInfo(), "stackName", "stackVersion", "service-name" };
        listInvocations.add(new org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation(org.apache.ambari.server.api.services.Request.Type.GET, service, m, args, null));
        service = new org.apache.ambari.server.api.services.StacksServiceTest.TestStacksService("stackName", "stackVersion");
        m = service.getClass().getMethod("getServiceComponent", java.lang.String.class, javax.ws.rs.core.HttpHeaders.class, javax.ws.rs.core.UriInfo.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class);
        args = new java.lang.Object[]{ null, getHttpHeaders(), getUriInfo(), "stackName", "stackVersion", "service-name", "component-name" };
        listInvocations.add(new org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation(org.apache.ambari.server.api.services.Request.Type.GET, service, m, args, null));
        service = new org.apache.ambari.server.api.services.StacksServiceTest.TestStacksService("stackName", "stackVersion");
        m = service.getClass().getMethod("getServiceComponents", java.lang.String.class, javax.ws.rs.core.HttpHeaders.class, javax.ws.rs.core.UriInfo.class, java.lang.String.class, java.lang.String.class, java.lang.String.class);
        args = new java.lang.Object[]{ null, getHttpHeaders(), getUriInfo(), "stackName", "stackVersion", "service-name" };
        listInvocations.add(new org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation(org.apache.ambari.server.api.services.Request.Type.GET, service, m, args, null));
        service = new org.apache.ambari.server.api.services.StacksServiceTest.TestStacksService("stackName", null);
        m = service.getClass().getMethod("getStackArtifacts", java.lang.String.class, javax.ws.rs.core.HttpHeaders.class, javax.ws.rs.core.UriInfo.class, java.lang.String.class, java.lang.String.class);
        args = new java.lang.Object[]{ null, getHttpHeaders(), getUriInfo(), "stackName", "stackVersion" };
        listInvocations.add(new org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation(org.apache.ambari.server.api.services.Request.Type.GET, service, m, args, null));
        service = new org.apache.ambari.server.api.services.StacksServiceTest.TestStacksService("stackName", null);
        m = service.getClass().getMethod("getStackArtifact", java.lang.String.class, javax.ws.rs.core.HttpHeaders.class, javax.ws.rs.core.UriInfo.class, java.lang.String.class, java.lang.String.class, java.lang.String.class);
        args = new java.lang.Object[]{ null, getHttpHeaders(), getUriInfo(), "stackName", "stackVersion", "artifact-name" };
        listInvocations.add(new org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation(org.apache.ambari.server.api.services.Request.Type.GET, service, m, args, null));
        service = new org.apache.ambari.server.api.services.StacksServiceTest.TestStacksService("stackName", "stackVersion");
        m = service.getClass().getMethod("getStackServiceArtifacts", java.lang.String.class, javax.ws.rs.core.HttpHeaders.class, javax.ws.rs.core.UriInfo.class, java.lang.String.class, java.lang.String.class, java.lang.String.class);
        args = new java.lang.Object[]{ null, getHttpHeaders(), getUriInfo(), "stackName", "stackVersion", "service-name" };
        listInvocations.add(new org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation(org.apache.ambari.server.api.services.Request.Type.GET, service, m, args, null));
        service = new org.apache.ambari.server.api.services.StacksServiceTest.TestStacksService("stackName", "stackVersion");
        m = service.getClass().getMethod("getStackServiceArtifact", java.lang.String.class, javax.ws.rs.core.HttpHeaders.class, javax.ws.rs.core.UriInfo.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class);
        args = new java.lang.Object[]{ null, getHttpHeaders(), getUriInfo(), "stackName", "stackVersion", "service-name", "artifact-name" };
        listInvocations.add(new org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation(org.apache.ambari.server.api.services.Request.Type.GET, service, m, args, null));
        return listInvocations;
    }

    private class TestStacksService extends org.apache.ambari.server.api.services.StacksService {
        private java.lang.String m_stackId;

        private java.lang.String m_stackVersion;

        private TestStacksService(java.lang.String stackName, java.lang.String stackVersion) {
            m_stackId = stackName;
            m_stackVersion = stackVersion;
        }

        @java.lang.Override
        org.apache.ambari.server.api.resources.ResourceInstance createStackResource(java.lang.String stackName) {
            org.junit.Assert.assertEquals(m_stackId, stackName);
            return getTestResource();
        }

        @java.lang.Override
        org.apache.ambari.server.api.resources.ResourceInstance createStackVersionResource(java.lang.String stackName, java.lang.String stackVersion) {
            org.junit.Assert.assertEquals(m_stackId, stackName);
            org.junit.Assert.assertEquals(m_stackVersion, stackVersion);
            return getTestResource();
        }

        @java.lang.Override
        org.apache.ambari.server.api.resources.ResourceInstance createStackServiceResource(java.lang.String stackName, java.lang.String stackVersion, java.lang.String serviceName) {
            return getTestResource();
        }

        @java.lang.Override
        org.apache.ambari.server.api.resources.ResourceInstance createStackConfigurationResource(java.lang.String stackName, java.lang.String stackVersion, java.lang.String serviceName, java.lang.String propertyName) {
            return getTestResource();
        }

        @java.lang.Override
        org.apache.ambari.server.api.resources.ResourceInstance createStackServiceComponentResource(java.lang.String stackName, java.lang.String stackVersion, java.lang.String serviceName, java.lang.String componentName) {
            return getTestResource();
        }

        @java.lang.Override
        org.apache.ambari.server.api.resources.ResourceInstance createStackArtifactsResource(java.lang.String stackName, java.lang.String stackVersion, java.lang.String artifactName) {
            return getTestResource();
        }

        @java.lang.Override
        org.apache.ambari.server.api.resources.ResourceInstance createStackServiceArtifactsResource(java.lang.String stackName, java.lang.String stackVersion, java.lang.String serviceName, java.lang.String artifactName) {
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