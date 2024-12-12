package org.apache.ambari.server.api.services;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.UriInfo;
public class HostStackVersionServiceTest extends org.apache.ambari.server.api.services.BaseServiceTest {
    public java.util.List<org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation> getTestInvocations() throws java.lang.Exception {
        java.util.List<org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation> listInvocations = new java.util.ArrayList<>();
        org.apache.ambari.server.api.services.HostStackVersionService hostStackVersionService;
        java.lang.reflect.Method m;
        java.lang.Object[] args;
        hostStackVersionService = new org.apache.ambari.server.api.services.HostStackVersionServiceTest.TestHostStackVersionService("host", "cluster");
        m = hostStackVersionService.getClass().getMethod("getHostStackVersions", javax.ws.rs.core.HttpHeaders.class, javax.ws.rs.core.UriInfo.class);
        args = new java.lang.Object[]{ getHttpHeaders(), getUriInfo() };
        listInvocations.add(new org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation(org.apache.ambari.server.api.services.Request.Type.GET, hostStackVersionService, m, args, null));
        hostStackVersionService = new org.apache.ambari.server.api.services.HostStackVersionServiceTest.TestHostStackVersionService("host", "cluster");
        m = hostStackVersionService.getClass().getMethod("getHostStackVersion", javax.ws.rs.core.HttpHeaders.class, javax.ws.rs.core.UriInfo.class, java.lang.String.class);
        args = new java.lang.Object[]{ getHttpHeaders(), getUriInfo(), "1" };
        listInvocations.add(new org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation(org.apache.ambari.server.api.services.Request.Type.GET, hostStackVersionService, m, args, null));
        return listInvocations;
    }

    private class TestHostStackVersionService extends org.apache.ambari.server.api.services.HostStackVersionService {
        public TestHostStackVersionService(java.lang.String hostName, java.lang.String clusterName) {
            super(hostName, clusterName);
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