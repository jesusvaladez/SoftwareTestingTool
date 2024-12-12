package org.apache.ambari.server.api.services;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.UriInfo;
public class HostKerberosIdentityServiceTest extends org.apache.ambari.server.api.services.BaseServiceTest {
    public java.util.List<org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation> getTestInvocations() throws java.lang.Exception {
        java.util.List<org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation> listInvocations = new java.util.ArrayList<>();
        org.apache.ambari.server.api.services.HostKerberosIdentityService service = new org.apache.ambari.server.api.services.HostKerberosIdentityServiceTest.TestHostKerberosIdentityService("clusterName", "hostName", "identityId");
        java.lang.reflect.Method m = service.getClass().getMethod("getKerberosIdentity", java.lang.String.class, javax.ws.rs.core.HttpHeaders.class, javax.ws.rs.core.UriInfo.class, java.lang.String.class, java.lang.String.class);
        java.lang.Object[] args = new java.lang.Object[]{ null, getHttpHeaders(), getUriInfo(), "identityId", null };
        listInvocations.add(new org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation(org.apache.ambari.server.api.services.Request.Type.GET, service, m, args, null));
        service = new org.apache.ambari.server.api.services.HostKerberosIdentityServiceTest.TestHostKerberosIdentityService("clusterName", "hostName", null);
        m = service.getClass().getMethod("getKerberosIdentities", java.lang.String.class, javax.ws.rs.core.HttpHeaders.class, javax.ws.rs.core.UriInfo.class, java.lang.String.class);
        args = new java.lang.Object[]{ null, getHttpHeaders(), getUriInfo(), null };
        listInvocations.add(new org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation(org.apache.ambari.server.api.services.Request.Type.GET, service, m, args, null));
        return listInvocations;
    }

    private class TestHostKerberosIdentityService extends org.apache.ambari.server.api.services.HostKerberosIdentityService {
        private java.lang.String clusterId;

        private java.lang.String hostId;

        private java.lang.String identityId;

        private TestHostKerberosIdentityService(java.lang.String clusterId, java.lang.String hostId, java.lang.String identityId) {
            super(clusterId, hostId);
            this.clusterId = clusterId;
            this.hostId = hostId;
            this.identityId = identityId;
        }

        @java.lang.Override
        org.apache.ambari.server.api.resources.ResourceInstance createResource(java.lang.String clusterId, java.lang.String hostId, java.lang.String identityId) {
            org.junit.Assert.assertEquals(this.clusterId, clusterId);
            org.junit.Assert.assertEquals(this.hostId, hostId);
            org.junit.Assert.assertEquals(this.identityId, identityId);
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