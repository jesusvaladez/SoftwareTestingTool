package org.apache.ambari.server.state.cluster;
import org.apache.commons.collections.MapUtils;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.createMockBuilder;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
public class ClusterImplTest {
    private static com.google.inject.Injector injector;

    private static org.apache.ambari.server.state.Clusters clusters;

    private static org.apache.ambari.server.orm.OrmTestHelper ormTestHelper;

    @org.junit.BeforeClass
    public static void setUpClass() throws java.lang.Exception {
        org.apache.ambari.server.state.cluster.ClusterImplTest.injector = com.google.inject.Guice.createInjector(new org.apache.ambari.server.orm.InMemoryDefaultTestModule());
        org.apache.ambari.server.state.cluster.ClusterImplTest.injector.getInstance(org.apache.ambari.server.orm.GuiceJpaInitializer.class);
        org.apache.ambari.server.state.cluster.ClusterImplTest.clusters = org.apache.ambari.server.state.cluster.ClusterImplTest.injector.getInstance(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.state.cluster.ClusterImplTest.ormTestHelper = org.apache.ambari.server.state.cluster.ClusterImplTest.injector.getInstance(org.apache.ambari.server.orm.OrmTestHelper.class);
    }

    @org.junit.AfterClass
    public static void teardown() throws org.apache.ambari.server.AmbariException, java.sql.SQLException {
        org.apache.ambari.server.H2DatabaseCleaner.clearDatabaseAndStopPersistenceService(org.apache.ambari.server.state.cluster.ClusterImplTest.injector);
    }

    @org.junit.Test
    public void testAddSessionAttributes() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.lang.Object> attributes = new java.util.HashMap<>();
        attributes.put("foo", "bar");
        org.apache.ambari.server.controller.AmbariSessionManager sessionManager = EasyMock.createMock(org.apache.ambari.server.controller.AmbariSessionManager.class);
        org.apache.ambari.server.state.cluster.ClusterImpl cluster = EasyMock.createMockBuilder(org.apache.ambari.server.state.cluster.ClusterImpl.class).addMockedMethod("getSessionManager").addMockedMethod("getClusterName").addMockedMethod("getSessionAttributes").createMock();
        EasyMock.expect(cluster.getSessionManager()).andReturn(sessionManager);
        EasyMock.expect(cluster.getClusterName()).andReturn("c1");
        EasyMock.expect(cluster.getSessionAttributes()).andReturn(attributes);
        sessionManager.setAttribute("cluster_session_attributes:c1", attributes);
        EasyMock.replay(sessionManager, cluster);
        cluster.addSessionAttributes(attributes);
        EasyMock.verify(sessionManager, cluster);
    }

    @org.junit.Test
    public void testSetSessionAttribute() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.lang.Object> attributes = new java.util.HashMap<>();
        attributes.put("foo", "bar");
        attributes.put("foo2", "bar2");
        java.util.Map<java.lang.String, java.lang.Object> updatedAttributes = new java.util.HashMap<>(attributes);
        updatedAttributes.put("foo2", "updated value");
        java.util.Map<java.lang.String, java.lang.Object> addedAttributes = new java.util.HashMap<>(updatedAttributes);
        updatedAttributes.put("foo3", "added value");
        org.apache.ambari.server.controller.AmbariSessionManager sessionManager = EasyMock.createMock(org.apache.ambari.server.controller.AmbariSessionManager.class);
        org.apache.ambari.server.state.cluster.ClusterImpl cluster = EasyMock.createMockBuilder(org.apache.ambari.server.state.cluster.ClusterImpl.class).addMockedMethod("getSessionManager").addMockedMethod("getClusterName").addMockedMethod("getSessionAttributes").createMock();
        EasyMock.expect(cluster.getSessionManager()).andReturn(sessionManager);
        EasyMock.expect(cluster.getClusterName()).andReturn("c1");
        EasyMock.expect(cluster.getSessionAttributes()).andReturn(attributes);
        sessionManager.setAttribute("cluster_session_attributes:c1", updatedAttributes);
        EasyMock.expectLastCall().once();
        EasyMock.expect(cluster.getSessionManager()).andReturn(sessionManager);
        EasyMock.expect(cluster.getClusterName()).andReturn("c1");
        EasyMock.expect(cluster.getSessionAttributes()).andReturn(updatedAttributes);
        sessionManager.setAttribute("cluster_session_attributes:c1", addedAttributes);
        EasyMock.expectLastCall().once();
        EasyMock.replay(sessionManager, cluster);
        cluster.setSessionAttribute("foo2", "updated value");
        cluster.setSessionAttribute("foo3", "added value");
        EasyMock.verify(sessionManager, cluster);
    }

    @org.junit.Test
    public void testRemoveSessionAttribute() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.lang.Object> attributes = new java.util.HashMap<>();
        attributes.put("foo", "bar");
        attributes.put("foo2", "bar2");
        java.util.Map<java.lang.String, java.lang.Object> trimmedAttributes = new java.util.HashMap<>(attributes);
        trimmedAttributes.remove("foo2");
        org.apache.ambari.server.controller.AmbariSessionManager sessionManager = EasyMock.createMock(org.apache.ambari.server.controller.AmbariSessionManager.class);
        org.apache.ambari.server.state.cluster.ClusterImpl cluster = EasyMock.createMockBuilder(org.apache.ambari.server.state.cluster.ClusterImpl.class).addMockedMethod("getSessionManager").addMockedMethod("getClusterName").addMockedMethod("getSessionAttributes").createMock();
        EasyMock.expect(cluster.getSessionManager()).andReturn(sessionManager);
        EasyMock.expect(cluster.getClusterName()).andReturn("c1");
        EasyMock.expect(cluster.getSessionAttributes()).andReturn(attributes);
        sessionManager.setAttribute("cluster_session_attributes:c1", trimmedAttributes);
        EasyMock.expectLastCall().once();
        EasyMock.replay(sessionManager, cluster);
        cluster.removeSessionAttribute("foo2");
        EasyMock.verify(sessionManager, cluster);
    }

    @org.junit.Test
    public void testGetSessionAttributes() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.lang.Object> attributes = new java.util.HashMap<>();
        attributes.put("foo", "bar");
        org.apache.ambari.server.controller.AmbariSessionManager sessionManager = EasyMock.createMock(org.apache.ambari.server.controller.AmbariSessionManager.class);
        org.apache.ambari.server.state.cluster.ClusterImpl cluster = EasyMock.createMockBuilder(org.apache.ambari.server.state.cluster.ClusterImpl.class).addMockedMethod("getSessionManager").addMockedMethod("getClusterName").createMock();
        EasyMock.expect(cluster.getSessionManager()).andReturn(sessionManager).anyTimes();
        EasyMock.expect(cluster.getClusterName()).andReturn("c1").anyTimes();
        EasyMock.expect(sessionManager.getAttribute("cluster_session_attributes:c1")).andReturn(attributes);
        EasyMock.expect(sessionManager.getAttribute("cluster_session_attributes:c1")).andReturn(null);
        EasyMock.replay(sessionManager, cluster);
        org.junit.Assert.assertEquals(attributes, cluster.getSessionAttributes());
        org.junit.Assert.assertEquals(java.util.Collections.<java.lang.String, java.lang.Object>emptyMap(), cluster.getSessionAttributes());
        EasyMock.verify(sessionManager, cluster);
    }

    @org.junit.Test
    public void testDeleteService() throws java.lang.Exception {
        java.lang.String serviceToDelete = "TEZ";
        java.lang.String clusterName = "TEST_CLUSTER";
        java.lang.String hostName1 = "HOST1";
        java.lang.String hostName2 = "HOST2";
        java.lang.String stackVersion = "HDP-2.1.1";
        java.lang.String repoVersion = "2.1.1-1234";
        org.apache.ambari.server.state.StackId stackId = new org.apache.ambari.server.state.StackId(stackVersion);
        org.apache.ambari.server.state.cluster.ClusterImplTest.ormTestHelper.createStack(stackId);
        org.apache.ambari.server.state.cluster.ClusterImplTest.clusters.addCluster(clusterName, stackId);
        org.apache.ambari.server.state.Cluster cluster = org.apache.ambari.server.state.cluster.ClusterImplTest.clusters.getCluster(clusterName);
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion = org.apache.ambari.server.state.cluster.ClusterImplTest.ormTestHelper.getOrCreateRepositoryVersion(new org.apache.ambari.server.state.StackId(stackVersion), repoVersion);
        org.apache.ambari.server.state.cluster.ClusterImplTest.clusters.addHost(hostName1);
        org.apache.ambari.server.state.cluster.ClusterImplTest.clusters.addHost(hostName2);
        org.apache.ambari.server.state.Host host1 = org.apache.ambari.server.state.cluster.ClusterImplTest.clusters.getHost(hostName1);
        host1.setHostAttributes(com.google.common.collect.ImmutableMap.of("os_family", "centos", "os_release_version", "6.0"));
        org.apache.ambari.server.state.Host host2 = org.apache.ambari.server.state.cluster.ClusterImplTest.clusters.getHost(hostName2);
        host2.setHostAttributes(com.google.common.collect.ImmutableMap.of("os_family", "centos", "os_release_version", "6.0"));
        org.apache.ambari.server.state.cluster.ClusterImplTest.clusters.mapAndPublishHostsToCluster(com.google.common.collect.Sets.newHashSet(hostName1, hostName2), clusterName);
        org.apache.ambari.server.state.Service hdfs = cluster.addService("HDFS", repositoryVersion);
        org.apache.ambari.server.state.ServiceComponent nameNode = hdfs.addServiceComponent("NAMENODE");
        nameNode.addServiceComponentHost(hostName1);
        org.apache.ambari.server.state.ServiceComponent dataNode = hdfs.addServiceComponent("DATANODE");
        dataNode.addServiceComponentHost(hostName1);
        dataNode.addServiceComponentHost(hostName2);
        org.apache.ambari.server.state.ServiceComponent hdfsClient = hdfs.addServiceComponent("HDFS_CLIENT");
        hdfsClient.addServiceComponentHost(hostName1);
        hdfsClient.addServiceComponentHost(hostName2);
        org.apache.ambari.server.state.Service tez = cluster.addService(serviceToDelete, repositoryVersion);
        org.apache.ambari.server.state.ServiceComponent tezClient = tez.addServiceComponent("TEZ_CLIENT");
        org.apache.ambari.server.state.ServiceComponentHost tezClientHost1 = tezClient.addServiceComponentHost(hostName1);
        org.apache.ambari.server.state.ServiceComponentHost tezClientHost2 = tezClient.addServiceComponentHost(hostName2);
        cluster.deleteService(serviceToDelete, new org.apache.ambari.server.controller.internal.DeleteHostComponentStatusMetaData());
        org.junit.Assert.assertFalse("Deleted service should be removed from the service collection !", cluster.getServices().containsKey(serviceToDelete));
        org.junit.Assert.assertEquals("All components of the deleted service should be removed from all hosts", 0, cluster.getServiceComponentHosts(serviceToDelete, null).size());
        boolean checkHost1 = !cluster.getServiceComponentHosts(hostName1).contains(tezClientHost1);
        boolean checkHost2 = !cluster.getServiceComponentHosts(hostName2).contains(tezClientHost2);
        org.junit.Assert.assertTrue("All components of the deleted service should be removed from all hosts", checkHost1 && checkHost2);
    }

    @org.junit.Test
    public void testDeleteHost() throws java.lang.Exception {
        java.lang.String clusterName = "TEST_DELETE_HOST";
        java.lang.String hostName1 = "HOSTNAME1";
        java.lang.String hostName2 = "HOSTNAME2";
        java.lang.String hostToDelete = hostName2;
        org.apache.ambari.server.state.StackId stackId = new org.apache.ambari.server.state.StackId("HDP-2.1.1");
        org.apache.ambari.server.state.cluster.ClusterImplTest.ormTestHelper.createStack(stackId);
        org.apache.ambari.server.state.cluster.ClusterImplTest.clusters.addCluster(clusterName, stackId);
        org.apache.ambari.server.state.Cluster cluster = org.apache.ambari.server.state.cluster.ClusterImplTest.clusters.getCluster(clusterName);
        org.apache.ambari.server.state.cluster.ClusterImplTest.clusters.addHost(hostName1);
        org.apache.ambari.server.state.cluster.ClusterImplTest.clusters.addHost(hostName2);
        org.apache.ambari.server.state.Host host1 = org.apache.ambari.server.state.cluster.ClusterImplTest.clusters.getHost(hostName1);
        host1.setHostAttributes(com.google.common.collect.ImmutableMap.of("os_family", "centos", "os_release_version", "6.0"));
        org.apache.ambari.server.state.Host host2 = org.apache.ambari.server.state.cluster.ClusterImplTest.clusters.getHost(hostName2);
        host2.setHostAttributes(com.google.common.collect.ImmutableMap.of("os_family", "centos", "os_release_version", "6.0"));
        org.apache.ambari.server.state.cluster.ClusterImplTest.clusters.mapAndPublishHostsToCluster(com.google.common.collect.Sets.newHashSet(hostName1, hostName2), clusterName);
        org.apache.ambari.server.state.cluster.ClusterImplTest.clusters.deleteHost(hostToDelete);
        org.junit.Assert.assertTrue(org.apache.ambari.server.state.cluster.ClusterImplTest.clusters.getClustersForHost(hostToDelete).isEmpty());
        org.junit.Assert.assertFalse(org.apache.ambari.server.state.cluster.ClusterImplTest.clusters.getHostsForCluster(clusterName).containsKey(hostToDelete));
        org.junit.Assert.assertFalse(cluster.getHosts().contains(hostToDelete));
        try {
            org.apache.ambari.server.state.cluster.ClusterImplTest.clusters.getHost(hostToDelete);
            org.junit.Assert.fail("getHost(hostName) should throw Exception when invoked for deleted host !");
        } catch (org.apache.ambari.server.HostNotFoundException e) {
        }
    }

    @org.junit.Test
    public void testGetClusterSize() throws java.lang.Exception {
        java.lang.String clusterName = "TEST_CLUSTER_SIZE";
        java.lang.String hostName1 = "host1";
        java.lang.String hostName2 = "host2";
        org.apache.ambari.server.state.StackId stackId = new org.apache.ambari.server.state.StackId("HDP", "2.1.1");
        org.apache.ambari.server.state.cluster.ClusterImplTest.ormTestHelper.createStack(stackId);
        org.apache.ambari.server.state.cluster.ClusterImplTest.clusters.addCluster(clusterName, stackId);
        org.apache.ambari.server.state.Cluster cluster = org.apache.ambari.server.state.cluster.ClusterImplTest.clusters.getCluster(clusterName);
        org.apache.ambari.server.state.cluster.ClusterImplTest.clusters.addHost(hostName1);
        org.apache.ambari.server.state.cluster.ClusterImplTest.clusters.addHost(hostName2);
        org.apache.ambari.server.state.Host host1 = org.apache.ambari.server.state.cluster.ClusterImplTest.clusters.getHost(hostName1);
        host1.setHostAttributes(com.google.common.collect.ImmutableMap.of("os_family", "centos", "os_release_version", "6.0"));
        org.apache.ambari.server.state.Host host2 = org.apache.ambari.server.state.cluster.ClusterImplTest.clusters.getHost(hostName2);
        host2.setHostAttributes(com.google.common.collect.ImmutableMap.of("os_family", "centos", "os_release_version", "6.0"));
        org.apache.ambari.server.state.cluster.ClusterImplTest.clusters.mapAndPublishHostsToCluster(com.google.common.collect.Sets.newHashSet(hostName1, hostName2), clusterName);
        int clusterSize = cluster.getClusterSize();
        org.junit.Assert.assertEquals(2, clusterSize);
    }

    @org.junit.Test
    public void testGetConfigGroupsByServiceName() throws org.apache.ambari.server.AmbariException {
        java.lang.String clusterName = "TEST_CONFIG_GROUPS";
        java.lang.String hostName1 = "HOSTNAME1";
        java.lang.String hostName2 = "HOSTNAME2";
        java.lang.String hostToDelete = hostName2;
        org.apache.ambari.server.state.StackId stackId = new org.apache.ambari.server.state.StackId("HDP-2.1.1");
        java.lang.String serviceToCheckName = "serviceName1";
        java.lang.String serviceNotToCheckName = "serviceName2";
        org.apache.ambari.server.state.cluster.ClusterImplTest.ormTestHelper.createStack(stackId);
        org.apache.ambari.server.state.cluster.ClusterImplTest.clusters.addCluster(clusterName, stackId);
        org.apache.ambari.server.state.Cluster cluster = org.apache.ambari.server.state.cluster.ClusterImplTest.clusters.getCluster(clusterName);
        org.apache.ambari.server.state.configgroup.ConfigGroup serviceConfigGroup1 = EasyMock.createNiceMock(org.apache.ambari.server.state.configgroup.ConfigGroup.class);
        org.apache.ambari.server.state.configgroup.ConfigGroup serviceConfigGroup2 = EasyMock.createNiceMock(org.apache.ambari.server.state.configgroup.ConfigGroup.class);
        EasyMock.expect(serviceConfigGroup1.getId()).andReturn(1L).anyTimes();
        EasyMock.expect(serviceConfigGroup2.getId()).andReturn(2L).anyTimes();
        EasyMock.expect(serviceConfigGroup1.getServiceName()).andReturn(serviceToCheckName).anyTimes();
        EasyMock.expect(serviceConfigGroup2.getServiceName()).andReturn(serviceNotToCheckName).anyTimes();
        EasyMock.replay(serviceConfigGroup1, serviceConfigGroup2);
        cluster.addConfigGroup(serviceConfigGroup1);
        cluster.addConfigGroup(serviceConfigGroup2);
        java.util.Map<java.lang.Long, org.apache.ambari.server.state.configgroup.ConfigGroup> configGroupsToCheck = cluster.getConfigGroupsByServiceName(serviceToCheckName);
        org.junit.Assert.assertFalse(org.apache.commons.collections.MapUtils.isEmpty(configGroupsToCheck));
        org.junit.Assert.assertEquals(1L, configGroupsToCheck.size());
        org.junit.Assert.assertTrue(configGroupsToCheck.keySet().contains(1L));
        org.junit.Assert.assertEquals(serviceToCheckName, configGroupsToCheck.get(1L).getServiceName());
        EasyMock.verify(serviceConfigGroup1, serviceConfigGroup2);
    }
}