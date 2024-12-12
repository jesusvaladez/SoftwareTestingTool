package org.apache.ambari.server.api.services;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.UriInfo;
public class ServiceServiceTest extends org.apache.ambari.server.api.services.BaseServiceTest {
    public java.util.List<org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation> getTestInvocations() throws java.lang.Exception {
        java.util.List<org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation> listInvocations = new java.util.ArrayList<>();
        org.apache.ambari.server.api.services.ServiceService service = new org.apache.ambari.server.api.services.ServiceServiceTest.TestServiceService("clusterName", "serviceName");
        java.lang.reflect.Method m = service.getClass().getMethod("getService", java.lang.String.class, javax.ws.rs.core.HttpHeaders.class, javax.ws.rs.core.UriInfo.class, java.lang.String.class);
        java.lang.Object[] args = new java.lang.Object[]{ null, getHttpHeaders(), getUriInfo(), "serviceName" };
        listInvocations.add(new org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation(org.apache.ambari.server.api.services.Request.Type.GET, service, m, args, null));
        service = new org.apache.ambari.server.api.services.ServiceServiceTest.TestServiceService("clusterName", null);
        m = service.getClass().getMethod("getServices", java.lang.String.class, javax.ws.rs.core.HttpHeaders.class, javax.ws.rs.core.UriInfo.class);
        args = new java.lang.Object[]{ null, getHttpHeaders(), getUriInfo() };
        listInvocations.add(new org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation(org.apache.ambari.server.api.services.Request.Type.GET, service, m, args, null));
        service = new org.apache.ambari.server.api.services.ServiceServiceTest.TestServiceService("clusterName", "serviceName");
        m = service.getClass().getMethod("createService", java.lang.String.class, javax.ws.rs.core.HttpHeaders.class, javax.ws.rs.core.UriInfo.class, java.lang.String.class);
        args = new java.lang.Object[]{ "body", getHttpHeaders(), getUriInfo(), "serviceName" };
        listInvocations.add(new org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation(org.apache.ambari.server.api.services.Request.Type.POST, service, m, args, "body"));
        service = new org.apache.ambari.server.api.services.ServiceServiceTest.TestServiceService("clusterName", null);
        m = service.getClass().getMethod("createServices", java.lang.String.class, javax.ws.rs.core.HttpHeaders.class, javax.ws.rs.core.UriInfo.class);
        args = new java.lang.Object[]{ "body", getHttpHeaders(), getUriInfo() };
        listInvocations.add(new org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation(org.apache.ambari.server.api.services.Request.Type.POST, service, m, args, "body"));
        service = new org.apache.ambari.server.api.services.ServiceServiceTest.TestServiceService("clusterName", "serviceName");
        m = service.getClass().getMethod("updateService", java.lang.String.class, javax.ws.rs.core.HttpHeaders.class, javax.ws.rs.core.UriInfo.class, java.lang.String.class);
        args = new java.lang.Object[]{ "body", getHttpHeaders(), getUriInfo(), "serviceName" };
        listInvocations.add(new org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation(org.apache.ambari.server.api.services.Request.Type.PUT, service, m, args, "body"));
        service = new org.apache.ambari.server.api.services.ServiceServiceTest.TestServiceService("clusterName", null);
        m = service.getClass().getMethod("updateServices", java.lang.String.class, javax.ws.rs.core.HttpHeaders.class, javax.ws.rs.core.UriInfo.class);
        args = new java.lang.Object[]{ "body", getHttpHeaders(), getUriInfo() };
        listInvocations.add(new org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation(org.apache.ambari.server.api.services.Request.Type.PUT, service, m, args, "body"));
        service = new org.apache.ambari.server.api.services.ServiceServiceTest.TestServiceService("clusterName", "serviceName");
        m = service.getClass().getMethod("deleteService", javax.ws.rs.core.HttpHeaders.class, javax.ws.rs.core.UriInfo.class, java.lang.String.class);
        args = new java.lang.Object[]{ getHttpHeaders(), getUriInfo(), "serviceName" };
        listInvocations.add(new org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation(org.apache.ambari.server.api.services.Request.Type.DELETE, service, m, args, null));
        service = new org.apache.ambari.server.api.services.ServiceServiceTest.TestServiceService("clusterName", "serviceName", "artifactName");
        m = service.getClass().getMethod("createArtifact", java.lang.String.class, javax.ws.rs.core.HttpHeaders.class, javax.ws.rs.core.UriInfo.class, java.lang.String.class, java.lang.String.class);
        args = new java.lang.Object[]{ "body", getHttpHeaders(), getUriInfo(), "serviceName", "artifactName" };
        listInvocations.add(new org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation(org.apache.ambari.server.api.services.Request.Type.POST, service, m, args, "body"));
        service = new org.apache.ambari.server.api.services.ServiceServiceTest.TestServiceService("clusterName", "serviceName", "artifactName");
        m = service.getClass().getMethod("getArtifact", java.lang.String.class, javax.ws.rs.core.HttpHeaders.class, javax.ws.rs.core.UriInfo.class, java.lang.String.class, java.lang.String.class);
        args = new java.lang.Object[]{ "body", getHttpHeaders(), getUriInfo(), "serviceName", "artifactName" };
        listInvocations.add(new org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation(org.apache.ambari.server.api.services.Request.Type.GET, service, m, args, "body"));
        service = new org.apache.ambari.server.api.services.ServiceServiceTest.TestServiceService("clusterName", "serviceName");
        m = service.getClass().getMethod("getArtifacts", java.lang.String.class, javax.ws.rs.core.HttpHeaders.class, javax.ws.rs.core.UriInfo.class, java.lang.String.class);
        args = new java.lang.Object[]{ "body", getHttpHeaders(), getUriInfo(), "serviceName" };
        listInvocations.add(new org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation(org.apache.ambari.server.api.services.Request.Type.GET, service, m, args, "body"));
        service = new org.apache.ambari.server.api.services.ServiceServiceTest.TestServiceService("clusterName", "serviceName", "artifactName");
        m = service.getClass().getMethod("updateArtifact", java.lang.String.class, javax.ws.rs.core.HttpHeaders.class, javax.ws.rs.core.UriInfo.class, java.lang.String.class, java.lang.String.class);
        args = new java.lang.Object[]{ "body", getHttpHeaders(), getUriInfo(), "serviceName", "artifactName" };
        listInvocations.add(new org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation(org.apache.ambari.server.api.services.Request.Type.PUT, service, m, args, "body"));
        service = new org.apache.ambari.server.api.services.ServiceServiceTest.TestServiceService("clusterName", "serviceName");
        m = service.getClass().getMethod("updateArtifacts", java.lang.String.class, javax.ws.rs.core.HttpHeaders.class, javax.ws.rs.core.UriInfo.class, java.lang.String.class);
        args = new java.lang.Object[]{ "body", getHttpHeaders(), getUriInfo(), "serviceName" };
        listInvocations.add(new org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation(org.apache.ambari.server.api.services.Request.Type.PUT, service, m, args, "body"));
        service = new org.apache.ambari.server.api.services.ServiceServiceTest.TestServiceService("clusterName", "serviceName", "artifactName");
        m = service.getClass().getMethod("deleteArtifact", java.lang.String.class, javax.ws.rs.core.HttpHeaders.class, javax.ws.rs.core.UriInfo.class, java.lang.String.class, java.lang.String.class);
        args = new java.lang.Object[]{ "body", getHttpHeaders(), getUriInfo(), "serviceName", "artifactName" };
        listInvocations.add(new org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation(org.apache.ambari.server.api.services.Request.Type.DELETE, service, m, args, "body"));
        service = new org.apache.ambari.server.api.services.ServiceServiceTest.TestServiceService("clusterName", "serviceName");
        m = service.getClass().getMethod("deleteArtifacts", java.lang.String.class, javax.ws.rs.core.HttpHeaders.class, javax.ws.rs.core.UriInfo.class, java.lang.String.class);
        args = new java.lang.Object[]{ "body", getHttpHeaders(), getUriInfo(), "serviceName" };
        listInvocations.add(new org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation(org.apache.ambari.server.api.services.Request.Type.DELETE, service, m, args, "body"));
        return listInvocations;
    }

    private class TestServiceService extends org.apache.ambari.server.api.services.ServiceService {
        private java.lang.String m_clusterId;

        private java.lang.String m_serviceId;

        private java.lang.String m_artifact_id;

        private TestServiceService(java.lang.String clusterId, java.lang.String serviceId) {
            super(clusterId);
            m_clusterId = clusterId;
            m_serviceId = serviceId;
        }

        private TestServiceService(java.lang.String clusterId, java.lang.String serviceId, java.lang.String artifactId) {
            super(clusterId);
            m_clusterId = clusterId;
            m_serviceId = serviceId;
            m_artifact_id = artifactId;
        }

        @java.lang.Override
        org.apache.ambari.server.api.resources.ResourceInstance createServiceResource(java.lang.String clusterName, java.lang.String serviceName) {
            org.junit.Assert.assertEquals(m_clusterId, clusterName);
            org.junit.Assert.assertEquals(m_serviceId, serviceName);
            return getTestResource();
        }

        @java.lang.Override
        org.apache.ambari.server.api.resources.ResourceInstance createArtifactResource(java.lang.String clusterName, java.lang.String serviceName, java.lang.String artifactName) {
            org.junit.Assert.assertEquals(m_clusterId, clusterName);
            org.junit.Assert.assertEquals(m_serviceId, serviceName);
            org.junit.Assert.assertEquals(m_artifact_id, artifactName);
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