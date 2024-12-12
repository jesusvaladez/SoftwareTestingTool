package org.apache.ambari.server.controller.internal;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
public class HttpPropertyProviderTest {
    private static final java.lang.String PROPERTY_ID_CLUSTER_NAME = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "cluster_name");

    private static final java.lang.String PROPERTY_ID_HOST_NAME = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "host_name");

    private static final java.lang.String PROPERTY_ID_PUBLIC_HOST_NAME = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "public_host_name");

    private static final java.lang.String PROPERTY_ID_COMPONENT_NAME = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "component_name");

    private static final java.lang.String PROPERTY_ID_STALE_CONFIGS = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "stale_configs");

    private static final java.util.Map<java.lang.String, java.util.List<org.apache.ambari.server.controller.internal.HttpPropertyProvider.HttpPropertyRequest>> HTTP_PROPERTY_REQUESTS = new java.util.HashMap<>();

    static {
        HTTP_PROPERTY_REQUESTS.put("RESOURCEMANAGER", java.util.Collections.singletonList(new org.apache.ambari.server.controller.internal.ResourceManagerHttpPropertyRequest()));
        HTTP_PROPERTY_REQUESTS.put("ATLAS_SERVER", java.util.Collections.singletonList(new org.apache.ambari.server.controller.internal.AtlasServerHttpPropertyRequest()));
    }

    @org.junit.Test
    public void testReadResourceManager() throws java.lang.Exception {
        org.apache.ambari.server.controller.internal.HttpPropertyProviderTest.TestStreamProvider streamProvider = new org.apache.ambari.server.controller.internal.HttpPropertyProviderTest.TestStreamProvider(false);
        org.apache.ambari.server.state.Clusters clusters = EasyMock.createNiceMock(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.state.Cluster cluster = EasyMock.createNiceMock(org.apache.ambari.server.state.Cluster.class);
        org.apache.ambari.server.state.Config config1 = EasyMock.createNiceMock(org.apache.ambari.server.state.Config.class);
        org.apache.ambari.server.state.Config config2 = EasyMock.createNiceMock(org.apache.ambari.server.state.Config.class);
        java.util.Map<java.lang.String, java.lang.String> map = new java.util.HashMap<>();
        map.put("yarn.http.policy", "HTTPS_ONLY");
        map.put("yarn.resourcemanager.webapp.https.address", "ec2-54-234-33-50.compute-1.amazonaws.com:8999");
        map.put("yarn.resourcemanager.webapp.address", "ec2-54-234-33-50.compute-1.amazonaws.com:8088");
        EasyMock.expect(clusters.getCluster("testCluster")).andReturn(cluster);
        EasyMock.expect(cluster.getDesiredConfigByType("yarn-site")).andReturn(config1).anyTimes();
        EasyMock.expect(cluster.getDesiredConfigByType("core-site")).andReturn(config2).anyTimes();
        EasyMock.expect(config1.getProperties()).andReturn(map).anyTimes();
        EasyMock.expect(config2.getProperties()).andReturn(new java.util.HashMap<>()).anyTimes();
        EasyMock.replay(clusters, cluster, config1, config2);
        org.apache.ambari.server.controller.internal.HttpPropertyProvider propProvider = new org.apache.ambari.server.controller.internal.HttpPropertyProvider(streamProvider, clusters, org.apache.ambari.server.controller.internal.HttpPropertyProviderTest.PROPERTY_ID_CLUSTER_NAME, org.apache.ambari.server.controller.internal.HttpPropertyProviderTest.PROPERTY_ID_HOST_NAME, org.apache.ambari.server.controller.internal.HttpPropertyProviderTest.PROPERTY_ID_PUBLIC_HOST_NAME, org.apache.ambari.server.controller.internal.HttpPropertyProviderTest.PROPERTY_ID_COMPONENT_NAME, org.apache.ambari.server.controller.internal.HttpPropertyProviderTest.HTTP_PROPERTY_REQUESTS);
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent);
        resource.setProperty(org.apache.ambari.server.controller.internal.HttpPropertyProviderTest.PROPERTY_ID_HOST_NAME, "ec2-54-234-33-50.compute-1.amazonaws.com");
        resource.setProperty(org.apache.ambari.server.controller.internal.HttpPropertyProviderTest.PROPERTY_ID_PUBLIC_HOST_NAME, "ec2-54-234-33-50.compute-1.amazonaws.com");
        resource.setProperty(org.apache.ambari.server.controller.internal.HttpPropertyProviderTest.PROPERTY_ID_CLUSTER_NAME, "testCluster");
        resource.setProperty(org.apache.ambari.server.controller.internal.HttpPropertyProviderTest.PROPERTY_ID_COMPONENT_NAME, "RESOURCEMANAGER");
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(java.util.Collections.emptySet());
        propProvider.populateResources(java.util.Collections.singleton(resource), request, null);
        org.junit.Assert.assertTrue(resource.getPropertiesMap().get("HostRoles").get("ha_state").equals("ACTIVE"));
        org.junit.Assert.assertTrue(streamProvider.getLastSpec().equals("https://ec2-54-234-33-50.compute-1.amazonaws.com:8999" + "/ws/v1/cluster/info"));
    }

    @org.junit.Test
    public void testReadResourceManagerHA() throws java.lang.Exception {
        org.apache.ambari.server.controller.internal.HttpPropertyProviderTest.TestStreamProvider streamProvider = new org.apache.ambari.server.controller.internal.HttpPropertyProviderTest.TestStreamProvider(false);
        org.apache.ambari.server.state.Clusters clusters = EasyMock.createNiceMock(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.state.Cluster cluster = EasyMock.createNiceMock(org.apache.ambari.server.state.Cluster.class);
        org.apache.ambari.server.state.Config config1 = EasyMock.createNiceMock(org.apache.ambari.server.state.Config.class);
        org.apache.ambari.server.state.Config config2 = EasyMock.createNiceMock(org.apache.ambari.server.state.Config.class);
        java.util.Map<java.lang.String, java.lang.String> map = new java.util.HashMap<>();
        map.put("yarn.http.policy", "HTTPS_ONLY");
        map.put("yarn.resourcemanager.ha.rm-ids", "rm1,rm2");
        map.put("yarn.resourcemanager.hostname.rm1", "lc6402.ambari.apache.org");
        map.put("yarn.resourcemanager.hostname.rm2", "lc6403.ambari.apache.org");
        map.put("yarn.resourcemanager.webapp.address.rm1", "lc6402.ambari.apache.org:8099");
        map.put("yarn.resourcemanager.webapp.address.rm2", "lc6403.ambari.apache.org:8099");
        map.put("yarn.resourcemanager.webapp.https.address.rm1", "lc6402.ambari.apache.org:8066");
        map.put("yarn.resourcemanager.webapp.https.address.rm2", "lc6403.ambari.apache.org:8066");
        EasyMock.expect(clusters.getCluster("testCluster")).andReturn(cluster);
        EasyMock.expect(cluster.getDesiredConfigByType("yarn-site")).andReturn(config1).anyTimes();
        EasyMock.expect(cluster.getDesiredConfigByType("core-site")).andReturn(config2).anyTimes();
        EasyMock.expect(config1.getProperties()).andReturn(map).anyTimes();
        EasyMock.expect(config2.getProperties()).andReturn(new java.util.HashMap<>()).anyTimes();
        EasyMock.replay(clusters, cluster, config1, config2);
        org.apache.ambari.server.controller.internal.HttpPropertyProvider propProvider = new org.apache.ambari.server.controller.internal.HttpPropertyProvider(streamProvider, clusters, org.apache.ambari.server.controller.internal.HttpPropertyProviderTest.PROPERTY_ID_CLUSTER_NAME, org.apache.ambari.server.controller.internal.HttpPropertyProviderTest.PROPERTY_ID_HOST_NAME, org.apache.ambari.server.controller.internal.HttpPropertyProviderTest.PROPERTY_ID_PUBLIC_HOST_NAME, org.apache.ambari.server.controller.internal.HttpPropertyProviderTest.PROPERTY_ID_COMPONENT_NAME, org.apache.ambari.server.controller.internal.HttpPropertyProviderTest.HTTP_PROPERTY_REQUESTS);
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent);
        resource.setProperty(org.apache.ambari.server.controller.internal.HttpPropertyProviderTest.PROPERTY_ID_HOST_NAME, "lc6402.ambari.apache.org");
        resource.setProperty(org.apache.ambari.server.controller.internal.HttpPropertyProviderTest.PROPERTY_ID_PUBLIC_HOST_NAME, "lc6402.ambari.apache.org");
        resource.setProperty(org.apache.ambari.server.controller.internal.HttpPropertyProviderTest.PROPERTY_ID_CLUSTER_NAME, "testCluster");
        resource.setProperty(org.apache.ambari.server.controller.internal.HttpPropertyProviderTest.PROPERTY_ID_COMPONENT_NAME, "RESOURCEMANAGER");
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(java.util.Collections.emptySet());
        propProvider.populateResources(java.util.Collections.singleton(resource), request, null);
        org.junit.Assert.assertTrue(resource.getPropertiesMap().get("HostRoles").get("ha_state").equals("ACTIVE"));
        org.junit.Assert.assertTrue(streamProvider.getLastSpec().equals("https://lc6402.ambari.apache.org:8066" + "/ws/v1/cluster/info"));
    }

    @org.junit.Test
    public void testPopulateResources_atlasServer() throws java.lang.Exception {
        org.apache.ambari.server.controller.internal.HttpPropertyProviderTest.TestStreamProvider streamProvider = new org.apache.ambari.server.controller.internal.HttpPropertyProviderTest.TestStreamProvider("{\"Status\":\"ACTIVE\"}", false);
        org.apache.ambari.server.state.Clusters clusters = EasyMock.createNiceMock(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.state.Cluster cluster = EasyMock.createNiceMock(org.apache.ambari.server.state.Cluster.class);
        org.apache.ambari.server.state.Config config1 = EasyMock.createNiceMock(org.apache.ambari.server.state.Config.class);
        java.util.Map<java.lang.String, java.lang.String> map = new java.util.HashMap<>();
        map.put("atlas.enableTLS", "false");
        map.put("atlas.server.http.port", "21000");
        EasyMock.expect(clusters.getCluster("testCluster")).andReturn(cluster);
        EasyMock.expect(cluster.getDesiredConfigByType("application-properties")).andReturn(config1).anyTimes();
        EasyMock.expect(config1.getProperties()).andReturn(map).anyTimes();
        EasyMock.replay(clusters, cluster, config1);
        org.apache.ambari.server.controller.internal.HttpPropertyProvider propProvider = new org.apache.ambari.server.controller.internal.HttpPropertyProvider(streamProvider, clusters, org.apache.ambari.server.controller.internal.HttpPropertyProviderTest.PROPERTY_ID_CLUSTER_NAME, org.apache.ambari.server.controller.internal.HttpPropertyProviderTest.PROPERTY_ID_HOST_NAME, org.apache.ambari.server.controller.internal.HttpPropertyProviderTest.PROPERTY_ID_PUBLIC_HOST_NAME, org.apache.ambari.server.controller.internal.HttpPropertyProviderTest.PROPERTY_ID_COMPONENT_NAME, org.apache.ambari.server.controller.internal.HttpPropertyProviderTest.HTTP_PROPERTY_REQUESTS);
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent);
        resource.setProperty(org.apache.ambari.server.controller.internal.HttpPropertyProviderTest.PROPERTY_ID_CLUSTER_NAME, "testCluster");
        resource.setProperty(org.apache.ambari.server.controller.internal.HttpPropertyProviderTest.PROPERTY_ID_HOST_NAME, "ec2-54-234-33-50.compute-1.amazonaws.com");
        resource.setProperty(org.apache.ambari.server.controller.internal.HttpPropertyProviderTest.PROPERTY_ID_PUBLIC_HOST_NAME, "ec2-54-234-33-50.compute-1.amazonaws.com");
        resource.setProperty(org.apache.ambari.server.controller.internal.HttpPropertyProviderTest.PROPERTY_ID_COMPONENT_NAME, "ATLAS_SERVER");
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(java.util.Collections.emptySet());
        propProvider.populateResources(java.util.Collections.singleton(resource), request, null);
        org.junit.Assert.assertEquals("ACTIVE", resource.getPropertiesMap().get("HostRoles").get("ha_state"));
        org.junit.Assert.assertEquals("http://ec2-54-234-33-50.compute-1.amazonaws.com:21000/api/atlas/admin/status", streamProvider.getLastSpec());
    }

    @org.junit.Test
    public void testPopulateResources_atlasServer_https() throws java.lang.Exception {
        org.apache.ambari.server.controller.internal.HttpPropertyProviderTest.TestStreamProvider streamProvider = new org.apache.ambari.server.controller.internal.HttpPropertyProviderTest.TestStreamProvider("{\"Status\":\"ACTIVE\"}", false);
        org.apache.ambari.server.state.Clusters clusters = EasyMock.createNiceMock(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.state.Cluster cluster = EasyMock.createNiceMock(org.apache.ambari.server.state.Cluster.class);
        org.apache.ambari.server.state.Config config1 = EasyMock.createNiceMock(org.apache.ambari.server.state.Config.class);
        java.util.Map<java.lang.String, java.lang.String> map = new java.util.HashMap<>();
        map.put("atlas.enableTLS", "true");
        map.put("atlas.server.https.port", "21443");
        EasyMock.expect(clusters.getCluster("testCluster")).andReturn(cluster);
        EasyMock.expect(cluster.getDesiredConfigByType("application-properties")).andReturn(config1).anyTimes();
        EasyMock.expect(config1.getProperties()).andReturn(map).anyTimes();
        EasyMock.replay(clusters, cluster, config1);
        org.apache.ambari.server.controller.internal.HttpPropertyProvider propProvider = new org.apache.ambari.server.controller.internal.HttpPropertyProvider(streamProvider, clusters, org.apache.ambari.server.controller.internal.HttpPropertyProviderTest.PROPERTY_ID_CLUSTER_NAME, org.apache.ambari.server.controller.internal.HttpPropertyProviderTest.PROPERTY_ID_HOST_NAME, org.apache.ambari.server.controller.internal.HttpPropertyProviderTest.PROPERTY_ID_PUBLIC_HOST_NAME, org.apache.ambari.server.controller.internal.HttpPropertyProviderTest.PROPERTY_ID_COMPONENT_NAME, org.apache.ambari.server.controller.internal.HttpPropertyProviderTest.HTTP_PROPERTY_REQUESTS);
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent);
        resource.setProperty(org.apache.ambari.server.controller.internal.HttpPropertyProviderTest.PROPERTY_ID_CLUSTER_NAME, "testCluster");
        resource.setProperty(org.apache.ambari.server.controller.internal.HttpPropertyProviderTest.PROPERTY_ID_HOST_NAME, "ec2-54-234-33-50.compute-1.amazonaws.com");
        resource.setProperty(org.apache.ambari.server.controller.internal.HttpPropertyProviderTest.PROPERTY_ID_PUBLIC_HOST_NAME, "ec2-54-234-33-50.compute-1.amazonaws.com");
        resource.setProperty(org.apache.ambari.server.controller.internal.HttpPropertyProviderTest.PROPERTY_ID_COMPONENT_NAME, "ATLAS_SERVER");
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(java.util.Collections.emptySet());
        propProvider.populateResources(java.util.Collections.singleton(resource), request, null);
        org.junit.Assert.assertEquals("ACTIVE", resource.getPropertiesMap().get("HostRoles").get("ha_state"));
        org.junit.Assert.assertEquals("https://ec2-54-234-33-50.compute-1.amazonaws.com:21443/api/atlas/admin/status", streamProvider.getLastSpec());
    }

    @org.junit.Test
    public void testReadGangliaServer() throws java.lang.Exception {
        org.apache.ambari.server.controller.spi.Resource resource = doPopulate("GANGLIA_SERVER", java.util.Collections.emptySet(), new org.apache.ambari.server.controller.internal.HttpPropertyProviderTest.TestStreamProvider(false));
        org.junit.Assert.assertNull(resource.getPropertyValue(org.apache.ambari.server.controller.internal.HttpPropertyProviderTest.PROPERTY_ID_STALE_CONFIGS));
    }

    private org.apache.ambari.server.controller.spi.Resource doPopulate(java.lang.String componentName, java.util.Set<java.lang.String> requestProperties, org.apache.ambari.server.controller.utilities.StreamProvider streamProvider) throws java.lang.Exception {
        org.apache.ambari.server.state.Clusters clusters = EasyMock.createNiceMock(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.controller.internal.HttpPropertyProvider propProvider = new org.apache.ambari.server.controller.internal.HttpPropertyProvider(streamProvider, clusters, org.apache.ambari.server.controller.internal.HttpPropertyProviderTest.PROPERTY_ID_CLUSTER_NAME, org.apache.ambari.server.controller.internal.HttpPropertyProviderTest.PROPERTY_ID_HOST_NAME, org.apache.ambari.server.controller.internal.HttpPropertyProviderTest.PROPERTY_ID_PUBLIC_HOST_NAME, org.apache.ambari.server.controller.internal.HttpPropertyProviderTest.PROPERTY_ID_COMPONENT_NAME, org.apache.ambari.server.controller.internal.HttpPropertyProviderTest.HTTP_PROPERTY_REQUESTS);
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent);
        resource.setProperty(org.apache.ambari.server.controller.internal.HttpPropertyProviderTest.PROPERTY_ID_HOST_NAME, "ec2-54-234-33-50.compute-1.amazonaws.com");
        resource.setProperty(org.apache.ambari.server.controller.internal.HttpPropertyProviderTest.PROPERTY_ID_PUBLIC_HOST_NAME, "ec2-54-234-33-50.compute-1.amazonaws.com");
        resource.setProperty(org.apache.ambari.server.controller.internal.HttpPropertyProviderTest.PROPERTY_ID_CLUSTER_NAME, "testCluster");
        resource.setProperty(org.apache.ambari.server.controller.internal.HttpPropertyProviderTest.PROPERTY_ID_COMPONENT_NAME, componentName);
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(requestProperties);
        propProvider.populateResources(java.util.Collections.singleton(resource), request, null);
        return resource;
    }

    private static class TestStreamProvider implements org.apache.ambari.server.controller.utilities.StreamProvider {
        private boolean throwError = false;

        private java.lang.String lastSpec = null;

        private boolean isLastSpecUpdated;

        private java.lang.String responseStr = "{\"alerts\": [{\"Alert Body\": \"Body\"}],\"clusterInfo\": {\"haState\": \"ACTIVE\"}," + " \"hostcounts\": {\"up_hosts\":\"1\", \"down_hosts\":\"0\"}}";

        private TestStreamProvider(boolean throwErr) {
            throwError = throwErr;
        }

        private TestStreamProvider(java.lang.String responseStr, boolean throwErr) {
            this.responseStr = responseStr;
            throwError = throwErr;
        }

        @java.lang.Override
        public java.io.InputStream readFrom(java.lang.String spec) throws java.io.IOException {
            if (!isLastSpecUpdated) {
                lastSpec = spec;
            }
            isLastSpecUpdated = false;
            if (throwError) {
                throw new java.io.IOException("Fake error");
            }
            return new java.io.ByteArrayInputStream(responseStr.getBytes("UTF-8"));
        }

        public java.lang.String getLastSpec() {
            return lastSpec;
        }

        @java.lang.Override
        public java.io.InputStream readFrom(java.lang.String spec, java.lang.String requestMethod, java.lang.String params) throws java.io.IOException {
            lastSpec = (spec + "?") + params;
            isLastSpecUpdated = true;
            return readFrom(spec);
        }
    }
}