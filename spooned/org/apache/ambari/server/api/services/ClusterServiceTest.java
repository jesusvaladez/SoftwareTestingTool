package org.apache.ambari.server.api.services;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.UriInfo;
import org.easymock.EasyMock;
public class ClusterServiceTest extends org.apache.ambari.server.api.services.BaseServiceTest {
    @java.lang.Override
    public java.util.List<org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation> getTestInvocations() throws java.lang.Exception {
        java.util.List<org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation> listInvocations = new java.util.ArrayList<>();
        org.apache.ambari.server.orm.dao.ClusterDAO clusterDAO = org.easymock.EasyMock.createNiceMock(org.apache.ambari.server.orm.dao.ClusterDAO.class);
        org.apache.ambari.server.orm.dao.HostDAO hostDAO = org.easymock.EasyMock.createNiceMock(org.apache.ambari.server.orm.dao.HostDAO.class);
        org.easymock.EasyMock.expect(clusterDAO.findAll()).andReturn(new java.util.ArrayList<>()).atLeastOnce();
        org.easymock.EasyMock.expect(hostDAO.findAll()).andReturn(new java.util.ArrayList<>()).atLeastOnce();
        org.easymock.EasyMock.replay(clusterDAO, hostDAO);
        org.apache.ambari.server.state.Clusters clusters = new org.apache.ambari.server.api.services.ClusterServiceTest.TestClusters(clusterDAO, org.easymock.EasyMock.createNiceMock(org.apache.ambari.server.state.cluster.ClusterFactory.class), hostDAO, org.easymock.EasyMock.createNiceMock(org.apache.ambari.server.state.host.HostFactory.class));
        org.apache.ambari.server.api.services.ClusterService clusterService;
        java.lang.reflect.Method m;
        java.lang.Object[] args;
        clusterService = new org.apache.ambari.server.api.services.ClusterServiceTest.TestClusterService(clusters, "clusterName");
        m = clusterService.getClass().getMethod("getCluster", java.lang.String.class, javax.ws.rs.core.HttpHeaders.class, javax.ws.rs.core.UriInfo.class, java.lang.String.class);
        args = new java.lang.Object[]{ null, getHttpHeaders(), getUriInfo(), "clusterName" };
        listInvocations.add(new org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation(org.apache.ambari.server.api.services.Request.Type.GET, clusterService, m, args, null));
        clusterService = new org.apache.ambari.server.api.services.ClusterServiceTest.TestClusterService(clusters, null);
        m = clusterService.getClass().getMethod("getClusters", java.lang.String.class, javax.ws.rs.core.HttpHeaders.class, javax.ws.rs.core.UriInfo.class);
        args = new java.lang.Object[]{ null, getHttpHeaders(), getUriInfo() };
        listInvocations.add(new org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation(org.apache.ambari.server.api.services.Request.Type.GET, clusterService, m, args, null));
        clusterService = new org.apache.ambari.server.api.services.ClusterServiceTest.TestClusterService(clusters, "clusterName");
        m = clusterService.getClass().getMethod("createCluster", java.lang.String.class, javax.ws.rs.core.HttpHeaders.class, javax.ws.rs.core.UriInfo.class, java.lang.String.class);
        args = new java.lang.Object[]{ "body", getHttpHeaders(), getUriInfo(), "clusterName" };
        listInvocations.add(new org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation(org.apache.ambari.server.api.services.Request.Type.POST, clusterService, m, args, "body"));
        clusterService = new org.apache.ambari.server.api.services.ClusterServiceTest.TestClusterService(clusters, "clusterName");
        m = clusterService.getClass().getMethod("updateCluster", java.lang.String.class, javax.ws.rs.core.HttpHeaders.class, javax.ws.rs.core.UriInfo.class, java.lang.String.class);
        args = new java.lang.Object[]{ "body", getHttpHeaders(), getUriInfo(), "clusterName" };
        listInvocations.add(new org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation(org.apache.ambari.server.api.services.Request.Type.PUT, clusterService, m, args, "body"));
        clusterService = new org.apache.ambari.server.api.services.ClusterServiceTest.TestClusterService(clusters, "clusterName");
        m = clusterService.getClass().getMethod("deleteCluster", javax.ws.rs.core.HttpHeaders.class, javax.ws.rs.core.UriInfo.class, java.lang.String.class);
        args = new java.lang.Object[]{ getHttpHeaders(), getUriInfo(), "clusterName" };
        listInvocations.add(new org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation(org.apache.ambari.server.api.services.Request.Type.DELETE, clusterService, m, args, null));
        clusterService = new org.apache.ambari.server.api.services.ClusterServiceTest.TestClusterService(clusters, "clusterName");
        m = clusterService.getClass().getMethod("createClusterArtifact", java.lang.String.class, javax.ws.rs.core.HttpHeaders.class, javax.ws.rs.core.UriInfo.class, java.lang.String.class, java.lang.String.class);
        args = new java.lang.Object[]{ "body", getHttpHeaders(), getUriInfo(), "clusterName", "artifactName" };
        listInvocations.add(new org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation(org.apache.ambari.server.api.services.Request.Type.POST, clusterService, m, args, "body"));
        clusterService = new org.apache.ambari.server.api.services.ClusterServiceTest.TestClusterService(clusters, "clusterName");
        m = clusterService.getClass().getMethod("getClusterArtifact", java.lang.String.class, javax.ws.rs.core.HttpHeaders.class, javax.ws.rs.core.UriInfo.class, java.lang.String.class, java.lang.String.class);
        args = new java.lang.Object[]{ "body", getHttpHeaders(), getUriInfo(), "clusterName", "artifact_name" };
        listInvocations.add(new org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation(org.apache.ambari.server.api.services.Request.Type.GET, clusterService, m, args, "body"));
        clusterService = new org.apache.ambari.server.api.services.ClusterServiceTest.TestClusterService(clusters, "clusterName");
        m = clusterService.getClass().getMethod("getClusterArtifacts", java.lang.String.class, javax.ws.rs.core.HttpHeaders.class, javax.ws.rs.core.UriInfo.class, java.lang.String.class);
        args = new java.lang.Object[]{ "body", getHttpHeaders(), getUriInfo(), "clusterName" };
        listInvocations.add(new org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation(org.apache.ambari.server.api.services.Request.Type.GET, clusterService, m, args, "body"));
        clusterService = new org.apache.ambari.server.api.services.ClusterServiceTest.TestClusterService(clusters, "clusterName");
        m = clusterService.getClass().getMethod("updateClusterArtifact", java.lang.String.class, javax.ws.rs.core.HttpHeaders.class, javax.ws.rs.core.UriInfo.class, java.lang.String.class, java.lang.String.class);
        args = new java.lang.Object[]{ "body", getHttpHeaders(), getUriInfo(), "clusterName", "artifactName" };
        listInvocations.add(new org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation(org.apache.ambari.server.api.services.Request.Type.PUT, clusterService, m, args, "body"));
        clusterService = new org.apache.ambari.server.api.services.ClusterServiceTest.TestClusterService(clusters, "clusterName");
        m = clusterService.getClass().getMethod("updateClusterArtifacts", java.lang.String.class, javax.ws.rs.core.HttpHeaders.class, javax.ws.rs.core.UriInfo.class, java.lang.String.class);
        args = new java.lang.Object[]{ "body", getHttpHeaders(), getUriInfo(), "clusterName" };
        listInvocations.add(new org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation(org.apache.ambari.server.api.services.Request.Type.PUT, clusterService, m, args, "body"));
        clusterService = new org.apache.ambari.server.api.services.ClusterServiceTest.TestClusterService(clusters, "clusterName");
        m = clusterService.getClass().getMethod("deleteClusterArtifact", java.lang.String.class, javax.ws.rs.core.HttpHeaders.class, javax.ws.rs.core.UriInfo.class, java.lang.String.class, java.lang.String.class);
        args = new java.lang.Object[]{ "body", getHttpHeaders(), getUriInfo(), "clusterName", "artifactName" };
        listInvocations.add(new org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation(org.apache.ambari.server.api.services.Request.Type.DELETE, clusterService, m, args, "body"));
        clusterService = new org.apache.ambari.server.api.services.ClusterServiceTest.TestClusterService(clusters, "clusterName");
        m = clusterService.getClass().getMethod("deleteClusterArtifacts", java.lang.String.class, javax.ws.rs.core.HttpHeaders.class, javax.ws.rs.core.UriInfo.class, java.lang.String.class);
        args = new java.lang.Object[]{ "body", getHttpHeaders(), getUriInfo(), "clusterName" };
        listInvocations.add(new org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation(org.apache.ambari.server.api.services.Request.Type.DELETE, clusterService, m, args, "body"));
        return listInvocations;
    }

    private class TestClusterService extends org.apache.ambari.server.api.services.ClusterService {
        private java.lang.String m_clusterId;

        private TestClusterService(org.apache.ambari.server.state.Clusters clusters, java.lang.String clusterId) {
            super(clusters);
            m_clusterId = clusterId;
        }

        @java.lang.Override
        org.apache.ambari.server.api.resources.ResourceInstance createClusterResource(java.lang.String clusterName) {
            org.junit.Assert.assertEquals(m_clusterId, clusterName);
            return getTestResource();
        }

        @java.lang.Override
        org.apache.ambari.server.api.resources.ResourceInstance createArtifactResource(java.lang.String clusterName, java.lang.String artifactName) {
            org.junit.Assert.assertEquals(m_clusterId, clusterName);
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

    private class TestClusters extends org.apache.ambari.server.state.cluster.ClustersImpl {
        public TestClusters(org.apache.ambari.server.orm.dao.ClusterDAO clusterDAO, org.apache.ambari.server.state.cluster.ClusterFactory clusterFactory, org.apache.ambari.server.orm.dao.HostDAO hostDAO, org.apache.ambari.server.state.host.HostFactory hostFactory) {
            super(clusterDAO, clusterFactory, hostDAO, hostFactory);
        }

        @java.lang.Override
        public boolean checkPermission(java.lang.String clusterName, boolean readOnly) {
            return true;
        }
    }
}