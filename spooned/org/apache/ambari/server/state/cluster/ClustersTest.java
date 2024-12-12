package org.apache.ambari.server.state.cluster;
import javax.persistence.EntityManager;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
public class ClustersTest {
    private org.apache.ambari.server.state.Clusters clusters;

    private com.google.inject.Injector injector;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.OrmTestHelper helper;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.HostDAO hostDAO;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.TopologyRequestDAO topologyRequestDAO;

    @com.google.inject.Inject
    private org.apache.ambari.server.topology.PersistedState persistedState;

    @org.junit.Before
    public void setup() throws java.lang.Exception {
        injector = com.google.inject.Guice.createInjector(com.google.inject.util.Modules.override(new org.apache.ambari.server.orm.InMemoryDefaultTestModule()).with(new org.apache.ambari.server.state.cluster.ClustersTest.MockModule()));
        injector.getInstance(org.apache.ambari.server.orm.GuiceJpaInitializer.class);
        clusters = injector.getInstance(org.apache.ambari.server.state.Clusters.class);
        injector.injectMembers(this);
    }

    @org.junit.After
    public void teardown() throws org.apache.ambari.server.AmbariException, java.sql.SQLException {
        org.apache.ambari.server.H2DatabaseCleaner.clearDatabaseAndStopPersistenceService(injector);
    }

    private void setOsFamily(org.apache.ambari.server.state.Host host, java.lang.String osFamily, java.lang.String osVersion) {
        java.util.Map<java.lang.String, java.lang.String> hostAttributes = new java.util.HashMap<>();
        hostAttributes.put("os_family", osFamily);
        hostAttributes.put("os_release_version", osVersion);
        host.setHostAttributes(hostAttributes);
    }

    @org.junit.Test
    public void testGetInvalidCluster() throws org.apache.ambari.server.AmbariException {
        try {
            clusters.getCluster("foo");
            org.junit.Assert.fail("Exception should be thrown on invalid get");
        } catch (org.apache.ambari.server.ClusterNotFoundException e) {
        }
    }

    @org.junit.Test
    public void testAddAndGetCluster() throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.StackId stackId = new org.apache.ambari.server.state.StackId("HDP-2.1.1");
        helper.createStack(stackId);
        java.lang.String c1 = "foo";
        java.lang.String c2 = "foo";
        clusters.addCluster(c1, stackId);
        try {
            clusters.addCluster(c1, stackId);
            org.junit.Assert.fail("Exception should be thrown on invalid add");
        } catch (org.apache.ambari.server.AmbariException e) {
        }
        try {
            clusters.addCluster(c2, stackId);
            org.junit.Assert.fail("Exception should be thrown on invalid add");
        } catch (org.apache.ambari.server.AmbariException e) {
        }
        c2 = "foo2";
        clusters.addCluster(c2, stackId);
        junit.framework.Assert.assertNotNull(clusters.getCluster(c1));
        junit.framework.Assert.assertNotNull(clusters.getCluster(c2));
        junit.framework.Assert.assertEquals(c1, clusters.getCluster(c1).getClusterName());
        junit.framework.Assert.assertEquals(c2, clusters.getCluster(c2).getClusterName());
        java.util.Map<java.lang.String, org.apache.ambari.server.state.Cluster> verifyClusters = clusters.getClusters();
        junit.framework.Assert.assertTrue(verifyClusters.containsKey(c1));
        junit.framework.Assert.assertTrue(verifyClusters.containsKey(c2));
        junit.framework.Assert.assertNotNull(verifyClusters.get(c1));
        junit.framework.Assert.assertNotNull(verifyClusters.get(c2));
        org.apache.ambari.server.state.Cluster c = clusters.getCluster(c1);
        c.setClusterName("foobar");
        long cId = c.getClusterId();
        org.apache.ambari.server.state.Cluster changed = clusters.getCluster("foobar");
        junit.framework.Assert.assertNotNull(changed);
        junit.framework.Assert.assertEquals(cId, changed.getClusterId());
        junit.framework.Assert.assertEquals("foobar", clusters.getClusterById(cId).getClusterName());
    }

    @org.junit.Test
    public void testAddAndGetClusterWithSecurityType() throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.StackId stackId = new org.apache.ambari.server.state.StackId("HDP-2.1.1");
        helper.createStack(stackId);
        java.lang.String c1 = "foo";
        org.apache.ambari.server.state.SecurityType securityType = org.apache.ambari.server.state.SecurityType.KERBEROS;
        clusters.addCluster(c1, stackId, securityType);
        junit.framework.Assert.assertNotNull(clusters.getCluster(c1));
        junit.framework.Assert.assertEquals(c1, clusters.getCluster(c1).getClusterName());
        junit.framework.Assert.assertEquals(securityType, clusters.getCluster(c1).getSecurityType());
    }

    @org.junit.Test
    public void testAddAndGetHost() throws org.apache.ambari.server.AmbariException {
        java.lang.String h1 = "h1";
        java.lang.String h2 = "h2";
        java.lang.String h3 = "h3";
        clusters.addHost(h1);
        try {
            clusters.addHost(h1);
            org.junit.Assert.fail("Expected exception on duplicate host entry");
        } catch (java.lang.Exception e) {
        }
        clusters.addHost(h2);
        clusters.addHost(h3);
        java.util.List<org.apache.ambari.server.state.Host> hosts = clusters.getHosts();
        junit.framework.Assert.assertEquals(3, hosts.size());
        junit.framework.Assert.assertNotNull(clusters.getHost(h1));
        junit.framework.Assert.assertNotNull(clusters.getHost(h2));
        junit.framework.Assert.assertNotNull(clusters.getHost(h3));
        org.apache.ambari.server.state.Host h = clusters.getHost(h2);
        junit.framework.Assert.assertNotNull(h);
        try {
            clusters.getHost("foo");
            org.junit.Assert.fail("Expected error for unknown host");
        } catch (org.apache.ambari.server.HostNotFoundException e) {
        }
    }

    @org.junit.Test
    public void testClusterHostMapping() throws org.apache.ambari.server.AmbariException {
        java.lang.String c1 = "c1";
        java.lang.String c2 = "c2";
        java.lang.String h1 = "h1";
        java.lang.String h2 = "h2";
        java.lang.String h3 = "h3";
        java.lang.String h4 = "h4";
        try {
            clusters.mapHostToCluster(h1, c1);
            org.junit.Assert.fail("Expected exception for invalid cluster/host");
        } catch (java.lang.Exception e) {
        }
        org.apache.ambari.server.state.StackId stackId = new org.apache.ambari.server.state.StackId("HDP-0.1");
        helper.createStack(stackId);
        clusters.addCluster(c1, stackId);
        clusters.addCluster(c2, stackId);
        org.apache.ambari.server.state.Cluster cluster1 = clusters.getCluster(c1);
        org.apache.ambari.server.state.Cluster cluster2 = clusters.getCluster(c2);
        junit.framework.Assert.assertNotNull(clusters.getCluster(c1));
        junit.framework.Assert.assertNotNull(clusters.getCluster(c2));
        cluster1.setDesiredStackVersion(stackId);
        helper.getOrCreateRepositoryVersion(stackId, stackId.getStackVersion());
        try {
            clusters.mapHostToCluster(h1, c1);
            org.junit.Assert.fail("Expected exception for invalid host");
        } catch (java.lang.Exception e) {
        }
        clusters.addHost(h1);
        clusters.addHost(h2);
        clusters.addHost(h3);
        junit.framework.Assert.assertNotNull(clusters.getHost(h1));
        setOsFamily(clusters.getHost(h1), "redhat", "6.4");
        setOsFamily(clusters.getHost(h2), "redhat", "5.9");
        setOsFamily(clusters.getHost(h3), "redhat", "6.4");
        try {
            clusters.getClustersForHost(h4);
            org.junit.Assert.fail("Expected exception for invalid host");
        } catch (org.apache.ambari.server.HostNotFoundException e) {
        }
        java.util.Set<org.apache.ambari.server.state.Cluster> c = clusters.getClustersForHost(h3);
        junit.framework.Assert.assertEquals(0, c.size());
        clusters.mapHostToCluster(h1, c1);
        clusters.mapHostToCluster(h2, c1);
        try {
            clusters.mapHostToCluster(h1, c1);
            org.junit.Assert.fail("Expected exception for duplicate");
        } catch (org.apache.ambari.server.DuplicateResourceException e) {
        }
        org.apache.ambari.server.state.Cluster c3 = ((org.apache.ambari.server.state.Cluster) (clusters.getClustersForHost(h1).toArray()[0]));
        org.apache.ambari.server.state.Cluster c4 = ((org.apache.ambari.server.state.Cluster) (clusters.getClustersForHost(h2).toArray()[0]));
        junit.framework.Assert.assertEquals(c3, c4);
        java.util.Set<java.lang.String> hostnames = new java.util.HashSet<>();
        hostnames.add(h1);
        hostnames.add(h2);
        clusters.mapAndPublishHostsToCluster(hostnames, c2);
        c = clusters.getClustersForHost(h1);
        junit.framework.Assert.assertEquals(2, c.size());
        c = clusters.getClustersForHost(h2);
        junit.framework.Assert.assertEquals(2, c.size());
        java.util.Map<java.lang.String, org.apache.ambari.server.state.Host> hostsForC1 = clusters.getHostsForCluster(c1);
        junit.framework.Assert.assertEquals(2, hostsForC1.size());
        junit.framework.Assert.assertTrue(hostsForC1.containsKey(h1));
        junit.framework.Assert.assertTrue(hostsForC1.containsKey(h2));
        junit.framework.Assert.assertNotNull(hostsForC1.get(h1));
        junit.framework.Assert.assertNotNull(hostsForC1.get(h2));
    }

    @org.junit.Test
    public void testDebugDump() throws org.apache.ambari.server.AmbariException {
        java.lang.String c1 = "c1";
        java.lang.String c2 = "c2";
        java.lang.String h1 = "h1";
        java.lang.String h2 = "h2";
        java.lang.String h3 = "h3";
        org.apache.ambari.server.state.StackId stackId = new org.apache.ambari.server.state.StackId("HDP-0.1");
        helper.createStack(stackId);
        clusters.addCluster(c1, stackId);
        clusters.addCluster(c2, stackId);
        org.apache.ambari.server.state.Cluster cluster1 = clusters.getCluster(c1);
        org.apache.ambari.server.state.Cluster cluster2 = clusters.getCluster(c2);
        junit.framework.Assert.assertNotNull(clusters.getCluster(c1));
        junit.framework.Assert.assertNotNull(clusters.getCluster(c2));
        helper.getOrCreateRepositoryVersion(stackId, stackId.getStackVersion());
        clusters.addHost(h1);
        clusters.addHost(h2);
        clusters.addHost(h3);
        setOsFamily(clusters.getHost(h1), "redhat", "6.4");
        setOsFamily(clusters.getHost(h2), "redhat", "5.9");
        setOsFamily(clusters.getHost(h3), "redhat", "6.4");
        clusters.mapHostToCluster(h1, c1);
        clusters.mapHostToCluster(h2, c1);
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        clusters.debugDump(sb);
    }

    @org.junit.Test
    public void testDeleteCluster() throws java.lang.Exception {
        java.lang.String c1 = "c1";
        final java.lang.String h1 = "h1";
        final java.lang.String h2 = "h2";
        org.apache.ambari.server.state.StackId stackId = new org.apache.ambari.server.state.StackId("HDP-0.1");
        helper.createStack(stackId);
        clusters.addCluster(c1, stackId);
        org.apache.ambari.server.state.Cluster cluster = clusters.getCluster(c1);
        cluster.setDesiredStackVersion(stackId);
        cluster.setCurrentStackVersion(stackId);
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion = helper.getOrCreateRepositoryVersion(stackId, stackId.getStackVersion());
        final org.apache.ambari.server.state.Config config1 = injector.getInstance(org.apache.ambari.server.state.ConfigFactory.class).createNew(cluster, "t1", "1", new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put("prop1", "val1");
            }
        }, new java.util.HashMap<>());
        org.apache.ambari.server.state.Config config2 = injector.getInstance(org.apache.ambari.server.state.ConfigFactory.class).createNew(cluster, "t1", "2", new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put("prop2", "val2");
            }
        }, new java.util.HashMap<>());
        cluster.addDesiredConfig("_test", java.util.Collections.singleton(config1));
        clusters.addHost(h1);
        clusters.addHost(h2);
        org.apache.ambari.server.state.Host host1 = clusters.getHost(h1);
        org.apache.ambari.server.state.Host host2 = clusters.getHost(h2);
        setOsFamily(host1, "centos", "5.9");
        setOsFamily(host2, "centos", "5.9");
        clusters.mapAndPublishHostsToCluster(new java.util.HashSet<java.lang.String>() {
            {
                addAll(java.util.Arrays.asList(h1, h2));
            }
        }, c1);
        clusters.updateHostMappings(host1);
        clusters.updateHostMappings(host2);
        host1.addDesiredConfig(cluster.getClusterId(), true, "_test", config2);
        org.apache.ambari.server.state.Service hdfs = cluster.addService("HDFS", repositoryVersion);
        junit.framework.Assert.assertNotNull(injector.getInstance(org.apache.ambari.server.orm.dao.ClusterServiceDAO.class).findByClusterAndServiceNames(c1, "HDFS"));
        org.apache.ambari.server.state.ServiceComponent nameNode = hdfs.addServiceComponent("NAMENODE");
        org.apache.ambari.server.state.ServiceComponent dataNode = hdfs.addServiceComponent("DATANODE");
        org.apache.ambari.server.state.ServiceComponent serviceCheckNode = hdfs.addServiceComponent("HDFS_CLIENT");
        org.apache.ambari.server.state.ServiceComponentHost nameNodeHost = nameNode.addServiceComponentHost(h1);
        org.apache.ambari.server.orm.entities.HostEntity nameNodeHostEntity = hostDAO.findByName(nameNodeHost.getHostName());
        junit.framework.Assert.assertNotNull(nameNodeHostEntity);
        org.apache.ambari.server.state.ServiceComponentHost dataNodeHost = dataNode.addServiceComponentHost(h2);
        org.apache.ambari.server.state.ServiceComponentHost serviceCheckNodeHost = serviceCheckNode.addServiceComponentHost(h2);
        serviceCheckNodeHost.setState(org.apache.ambari.server.state.State.UNKNOWN);
        junit.framework.Assert.assertNotNull(injector.getInstance(org.apache.ambari.server.orm.dao.HostComponentStateDAO.class).findByIndex(nameNodeHost.getClusterId(), nameNodeHost.getServiceName(), nameNodeHost.getServiceComponentName(), nameNodeHostEntity.getHostId()));
        junit.framework.Assert.assertNotNull(injector.getInstance(org.apache.ambari.server.orm.dao.HostComponentDesiredStateDAO.class).findByIndex(nameNodeHost.getClusterId(), nameNodeHost.getServiceName(), nameNodeHost.getServiceComponentName(), nameNodeHostEntity.getHostId()));
        junit.framework.Assert.assertEquals(2, injector.getProvider(javax.persistence.EntityManager.class).get().createQuery("SELECT config FROM ClusterConfigEntity config").getResultList().size());
        junit.framework.Assert.assertEquals(1, injector.getProvider(javax.persistence.EntityManager.class).get().createQuery("SELECT state FROM ClusterStateEntity state").getResultList().size());
        junit.framework.Assert.assertEquals(1, injector.getProvider(javax.persistence.EntityManager.class).get().createQuery("SELECT config FROM ClusterConfigEntity config WHERE config.selected = 1").getResultList().size());
        org.apache.ambari.server.topology.Blueprint bp = EasyMock.createNiceMock(org.apache.ambari.server.topology.Blueprint.class);
        EasyMock.expect(bp.getName()).andReturn("TestBluePrint").anyTimes();
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(com.google.common.collect.Maps.newHashMap(), com.google.common.collect.Maps.newHashMap());
        java.util.Map<java.lang.String, org.apache.ambari.server.topology.HostGroupInfo> hostGroups = com.google.common.collect.Maps.newHashMap();
        org.apache.ambari.server.controller.internal.ProvisionClusterRequest topologyRequest = EasyMock.createNiceMock(org.apache.ambari.server.controller.internal.ProvisionClusterRequest.class);
        EasyMock.expect(topologyRequest.getType()).andReturn(org.apache.ambari.server.topology.TopologyRequest.Type.PROVISION).anyTimes();
        EasyMock.expect(topologyRequest.getBlueprint()).andReturn(bp).anyTimes();
        EasyMock.expect(topologyRequest.getClusterId()).andReturn(cluster.getClusterId()).anyTimes();
        EasyMock.expect(topologyRequest.getConfiguration()).andReturn(clusterConfig).anyTimes();
        EasyMock.expect(topologyRequest.getDescription()).andReturn("Test description").anyTimes();
        EasyMock.expect(topologyRequest.getHostGroupInfo()).andReturn(hostGroups).anyTimes();
        EasyMock.replay(bp, topologyRequest);
        persistedState.persistTopologyRequest(topologyRequest);
        junit.framework.Assert.assertEquals(1, topologyRequestDAO.findByClusterId(cluster.getClusterId()).size());
        clusters.deleteCluster(c1);
        junit.framework.Assert.assertEquals(2, hostDAO.findAll().size());
        junit.framework.Assert.assertNull(injector.getInstance(org.apache.ambari.server.orm.dao.HostComponentStateDAO.class).findByIndex(nameNodeHost.getClusterId(), nameNodeHost.getServiceName(), nameNodeHost.getServiceComponentName(), nameNodeHostEntity.getHostId()));
        junit.framework.Assert.assertNull(injector.getInstance(org.apache.ambari.server.orm.dao.HostComponentDesiredStateDAO.class).findByIndex(nameNodeHost.getClusterId(), nameNodeHost.getServiceName(), nameNodeHost.getServiceComponentName(), nameNodeHostEntity.getHostId()));
        junit.framework.Assert.assertEquals(0, injector.getProvider(javax.persistence.EntityManager.class).get().createQuery("SELECT config FROM ClusterConfigEntity config").getResultList().size());
        junit.framework.Assert.assertEquals(0, injector.getProvider(javax.persistence.EntityManager.class).get().createQuery("SELECT state FROM ClusterStateEntity state").getResultList().size());
        junit.framework.Assert.assertEquals(0, topologyRequestDAO.findByClusterId(cluster.getClusterId()).size());
    }

    @org.junit.Test
    public void testNullHostNamesInTopologyRequests() throws org.apache.ambari.server.AmbariException {
        final java.lang.String hostName = "myhost";
        final java.lang.String clusterName = "mycluster";
        org.apache.ambari.server.state.Cluster cluster = createCluster(clusterName);
        addHostToCluster(hostName, clusterName);
        addHostToCluster(hostName + "2", clusterName);
        addHostToCluster(hostName + "3", clusterName);
        createTopologyRequest(cluster, hostName);
        clusters.deleteHost(hostName);
        for (org.apache.ambari.server.state.Host h : cluster.getHosts()) {
            if (hostName.equals(h.getHostName())) {
                junit.framework.Assert.fail("Host is expected to be deleted");
            }
        }
    }

    @org.junit.Test
    public void testHostRegistrationPopulatesIdMapping() throws java.lang.Exception {
        java.lang.String clusterName = java.util.UUID.randomUUID().toString();
        java.lang.String hostName = java.util.UUID.randomUUID().toString();
        org.apache.ambari.server.utils.EventBusSynchronizer.synchronizeAmbariEventPublisher(injector);
        org.apache.ambari.server.state.Cluster cluster = createCluster(clusterName);
        junit.framework.Assert.assertNotNull(cluster);
        addHostToCluster(hostName, clusterName);
        org.apache.ambari.server.state.Host host = clusters.getHost(hostName);
        junit.framework.Assert.assertNotNull(host);
        long currentTime = java.lang.System.currentTimeMillis();
        org.apache.ambari.server.state.host.HostRegistrationRequestEvent registrationEvent = new org.apache.ambari.server.state.host.HostRegistrationRequestEvent(host.getHostName(), new org.apache.ambari.server.state.AgentVersion(""), currentTime, new org.apache.ambari.server.agent.HostInfo(), new org.apache.ambari.server.agent.AgentEnv(), currentTime);
        host.handleEvent(registrationEvent);
        java.lang.Long hostId = host.getHostId();
        junit.framework.Assert.assertNotNull(hostId);
        host = clusters.getHostById(hostId);
        junit.framework.Assert.assertNotNull(host);
    }

    private void createTopologyRequest(org.apache.ambari.server.state.Cluster cluster, java.lang.String hostName) {
        final java.lang.String groupName = "MyHostGroup";
        org.apache.ambari.server.topology.Blueprint bp = EasyMock.createNiceMock(org.apache.ambari.server.topology.Blueprint.class);
        EasyMock.expect(bp.getName()).andReturn("TestBluePrint").anyTimes();
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(com.google.common.collect.Maps.newHashMap(), com.google.common.collect.Maps.newHashMap());
        java.util.Map<java.lang.String, org.apache.ambari.server.topology.HostGroupInfo> hostGroups = new java.util.HashMap<>();
        org.apache.ambari.server.topology.HostGroupInfo hostGroupInfo = new org.apache.ambari.server.topology.HostGroupInfo(groupName);
        hostGroupInfo.setConfiguration(clusterConfig);
        hostGroupInfo.addHost(hostName);
        hostGroupInfo.addHost(hostName + "2");
        hostGroupInfo.addHost(hostName + "3");
        hostGroups.put(groupName, hostGroupInfo);
        org.apache.ambari.server.controller.internal.ProvisionClusterRequest topologyRequest = EasyMock.createNiceMock(org.apache.ambari.server.controller.internal.ProvisionClusterRequest.class);
        EasyMock.expect(topologyRequest.getType()).andReturn(org.apache.ambari.server.topology.TopologyRequest.Type.PROVISION).anyTimes();
        EasyMock.expect(topologyRequest.getBlueprint()).andReturn(bp).anyTimes();
        EasyMock.expect(topologyRequest.getClusterId()).andReturn(cluster.getClusterId()).anyTimes();
        EasyMock.expect(topologyRequest.getConfiguration()).andReturn(clusterConfig).anyTimes();
        EasyMock.expect(topologyRequest.getDescription()).andReturn("Test description").anyTimes();
        EasyMock.expect(topologyRequest.getHostGroupInfo()).andReturn(hostGroups).anyTimes();
        EasyMock.replay(bp, topologyRequest);
        persistedState.persistTopologyRequest(topologyRequest);
        createTopologyLogicalRequest(cluster, hostName);
    }

    private org.apache.ambari.server.topology.HostRequest createHostRequest(long hrId, java.lang.String hostName) {
        org.apache.ambari.server.topology.HostRequest hr = EasyMock.createNiceMock(org.apache.ambari.server.topology.HostRequest.class);
        EasyMock.expect(hr.getId()).andReturn(hrId).anyTimes();
        EasyMock.expect(hr.getHostgroupName()).andReturn("MyHostGroup").anyTimes();
        EasyMock.expect(hr.getHostName()).andReturn(hostName).anyTimes();
        EasyMock.expect(hr.getStageId()).andReturn(1L);
        EasyMock.expect(hr.getTopologyTasks()).andReturn(java.util.Collections.emptyList());
        EasyMock.replay(hr);
        return hr;
    }

    private void createTopologyLogicalRequest(org.apache.ambari.server.state.Cluster cluster, java.lang.String hostName) {
        java.util.Collection<org.apache.ambari.server.topology.HostRequest> hostRequests = new java.util.ArrayList<>();
        hostRequests.add(createHostRequest(1L, null));
        hostRequests.add(createHostRequest(2L, hostName));
        hostRequests.add(createHostRequest(3L, null));
        hostRequests.add(createHostRequest(4L, hostName + "2"));
        hostRequests.add(createHostRequest(5L, null));
        hostRequests.add(createHostRequest(6L, hostName + "3"));
        java.lang.Long requestId = topologyRequestDAO.findByClusterId(cluster.getClusterId()).get(0).getId();
        org.apache.ambari.server.topology.LogicalRequest logicalRequest = EasyMock.createNiceMock(org.apache.ambari.server.topology.LogicalRequest.class);
        EasyMock.expect(logicalRequest.getHostRequests()).andReturn(hostRequests).anyTimes();
        EasyMock.expect(logicalRequest.getRequestContext()).andReturn("Description").anyTimes();
        EasyMock.expect(logicalRequest.getRequestId()).andReturn(1L).anyTimes();
        EasyMock.replay(logicalRequest);
        persistedState.persistLogicalRequest(logicalRequest, requestId);
    }

    private void addHostToCluster(java.lang.String hostName, java.lang.String clusterName) throws org.apache.ambari.server.AmbariException {
        clusters.addHost(hostName);
        org.apache.ambari.server.state.Host host = clusters.getHost(hostName);
        setOsFamily(clusters.getHost(hostName), "centos", "5.9");
        java.util.Set<java.lang.String> hostnames = new java.util.HashSet<>();
        hostnames.add(hostName);
        clusters.mapAndPublishHostsToCluster(hostnames, clusterName);
    }

    private org.apache.ambari.server.state.Cluster createCluster(java.lang.String clusterName) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.StackId stackId = new org.apache.ambari.server.state.StackId("HDP-0.1");
        helper.createStack(stackId);
        clusters.addCluster(clusterName, stackId);
        return clusters.getCluster(clusterName);
    }

    private static class MockModule implements com.google.inject.Module {
        @java.lang.Override
        public void configure(com.google.inject.Binder binder) {
            binder.bind(org.apache.ambari.server.topology.TopologyManager.class).toInstance(EasyMock.createNiceMock(org.apache.ambari.server.topology.TopologyManager.class));
        }
    }
}