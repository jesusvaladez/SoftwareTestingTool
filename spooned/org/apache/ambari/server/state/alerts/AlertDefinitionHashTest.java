package org.apache.ambari.server.state.alerts;
import org.easymock.EasyMock;
import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.expect;
@org.junit.experimental.categories.Category({ category.AlertTest.class })
public class AlertDefinitionHashTest extends junit.framework.TestCase {
    private org.apache.ambari.server.state.alert.AlertDefinitionHash m_hash;

    private org.apache.ambari.server.state.Clusters m_mockClusters;

    private org.apache.ambari.server.state.Cluster m_mockCluster;

    private org.apache.ambari.server.orm.dao.AlertDefinitionDAO m_mockDao;

    private com.google.inject.Injector m_injector;

    private static final java.lang.String CLUSTERNAME = "cluster1";

    private static final java.lang.String HOSTNAME = "c6401.ambari.apache.org";

    private java.util.List<org.apache.ambari.server.orm.entities.AlertDefinitionEntity> m_agentDefinitions;

    private org.apache.ambari.server.orm.entities.AlertDefinitionEntity m_hdfsService;

    org.apache.ambari.server.orm.entities.AlertDefinitionEntity m_hdfsHost;

    private org.apache.ambari.server.state.ConfigHelper m_configHelper;

    @java.lang.Override
    @org.junit.Before
    @java.lang.SuppressWarnings("unchecked")
    protected void setUp() throws java.lang.Exception {
        super.setUp();
        m_injector = com.google.inject.Guice.createInjector(com.google.inject.util.Modules.override(new org.apache.ambari.server.orm.InMemoryDefaultTestModule()).with(new org.apache.ambari.server.state.alerts.AlertDefinitionHashTest.MockModule()));
        m_mockClusters = m_injector.getInstance(org.apache.ambari.server.state.Clusters.class);
        m_mockCluster = m_injector.getInstance(org.apache.ambari.server.state.Cluster.class);
        m_mockDao = m_injector.getInstance(org.apache.ambari.server.orm.dao.AlertDefinitionDAO.class);
        java.util.List<org.apache.ambari.server.state.ServiceComponentHost> serviceComponentHosts = new java.util.ArrayList<>();
        org.apache.ambari.server.state.ServiceComponentHost sch = org.easymock.EasyMock.createNiceMock(org.apache.ambari.server.state.ServiceComponentHost.class);
        EasyMock.expect(sch.getServiceName()).andReturn("HDFS").anyTimes();
        EasyMock.expect(sch.getServiceComponentName()).andReturn("NAMENODE").anyTimes();
        EasyMock.expect(sch.getHostName()).andReturn(org.apache.ambari.server.state.alerts.AlertDefinitionHashTest.HOSTNAME).anyTimes();
        org.easymock.EasyMock.replay(sch);
        serviceComponentHosts.add(sch);
        sch = org.easymock.EasyMock.createNiceMock(org.apache.ambari.server.state.ServiceComponentHost.class);
        EasyMock.expect(sch.getServiceName()).andReturn("HDFS").anyTimes();
        EasyMock.expect(sch.getServiceComponentName()).andReturn("DATANODE").anyTimes();
        EasyMock.expect(sch.getHostName()).andReturn(org.apache.ambari.server.state.alerts.AlertDefinitionHashTest.HOSTNAME).anyTimes();
        org.easymock.EasyMock.replay(sch);
        serviceComponentHosts.add(sch);
        java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceComponentHost> mapComponentHosts = new java.util.HashMap<>();
        org.apache.ambari.server.state.ServiceComponentHost host = org.easymock.EasyMock.createNiceMock(org.apache.ambari.server.state.ServiceComponentHost.class);
        EasyMock.expect(host.getHostName()).andReturn(org.apache.ambari.server.state.alerts.AlertDefinitionHashTest.HOSTNAME).anyTimes();
        mapComponentHosts.put(org.apache.ambari.server.state.alerts.AlertDefinitionHashTest.HOSTNAME, host);
        java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceComponent> serviceComponents = new java.util.HashMap<>();
        org.apache.ambari.server.state.ServiceComponent namenode = org.easymock.EasyMock.createNiceMock(org.apache.ambari.server.state.ServiceComponent.class);
        EasyMock.expect(namenode.getServiceComponentHosts()).andReturn(mapComponentHosts).anyTimes();
        EasyMock.expect(namenode.isMasterComponent()).andReturn(true).anyTimes();
        serviceComponents.put("NAMENODE", namenode);
        java.util.Map<java.lang.String, org.apache.ambari.server.state.Service> services = new java.util.HashMap<>();
        java.lang.String hdfsName = "HDFS";
        org.apache.ambari.server.state.Service hdfs = org.easymock.EasyMock.createNiceMock(org.apache.ambari.server.state.Service.class);
        EasyMock.expect(hdfs.getName()).andReturn("HDFS").anyTimes();
        EasyMock.expect(hdfs.getServiceComponents()).andReturn(serviceComponents).anyTimes();
        services.put(hdfsName, hdfs);
        org.easymock.EasyMock.replay(hdfs, host, namenode);
        EasyMock.expect(m_mockClusters.getCluster(((java.lang.String) (EasyMock.anyObject())))).andReturn(m_mockCluster).atLeastOnce();
        EasyMock.expect(m_mockClusters.getClusterById(org.easymock.EasyMock.anyInt())).andReturn(m_mockCluster).atLeastOnce();
        java.util.Map<java.lang.String, org.apache.ambari.server.state.Host> clusterHosts = new java.util.HashMap<>();
        clusterHosts.put(org.apache.ambari.server.state.alerts.AlertDefinitionHashTest.HOSTNAME, null);
        EasyMock.expect(m_mockClusters.getHostsForCluster(org.easymock.EasyMock.eq(org.apache.ambari.server.state.alerts.AlertDefinitionHashTest.CLUSTERNAME))).andReturn(clusterHosts).anyTimes();
        EasyMock.expect(m_mockCluster.getClusterId()).andReturn(java.lang.Long.valueOf(1)).anyTimes();
        EasyMock.expect(m_mockCluster.getClusterName()).andReturn(org.apache.ambari.server.state.alerts.AlertDefinitionHashTest.CLUSTERNAME).anyTimes();
        EasyMock.expect(m_mockCluster.getServices()).andReturn(services).anyTimes();
        EasyMock.expect(m_mockCluster.getServiceComponentHosts(org.easymock.EasyMock.anyObject(java.lang.String.class))).andReturn(serviceComponentHosts).anyTimes();
        m_hdfsService = new org.apache.ambari.server.orm.entities.AlertDefinitionEntity();
        m_hdfsService.setDefinitionId(1L);
        m_hdfsService.setClusterId(1L);
        m_hdfsService.setHash(java.util.UUID.randomUUID().toString());
        m_hdfsService.setServiceName("HDFS");
        m_hdfsService.setComponentName("NAMENODE");
        m_hdfsService.setScope(org.apache.ambari.server.state.alert.Scope.SERVICE);
        m_hdfsService.setScheduleInterval(1);
        m_hdfsHost = new org.apache.ambari.server.orm.entities.AlertDefinitionEntity();
        m_hdfsHost.setDefinitionId(2L);
        m_hdfsHost.setClusterId(1L);
        m_hdfsHost.setHash(java.util.UUID.randomUUID().toString());
        m_hdfsHost.setServiceName("HDFS");
        m_hdfsHost.setComponentName("DATANODE");
        m_hdfsHost.setScope(org.apache.ambari.server.state.alert.Scope.HOST);
        m_hdfsHost.setScheduleInterval(1);
        org.apache.ambari.server.orm.entities.AlertDefinitionEntity agentScoped = new org.apache.ambari.server.orm.entities.AlertDefinitionEntity();
        agentScoped.setDefinitionId(3L);
        agentScoped.setClusterId(1L);
        agentScoped.setHash(java.util.UUID.randomUUID().toString());
        agentScoped.setServiceName("AMBARI");
        agentScoped.setComponentName("AMBARI_AGENT");
        agentScoped.setScope(org.apache.ambari.server.state.alert.Scope.HOST);
        agentScoped.setScheduleInterval(1);
        org.easymock.EasyMock.expect(m_mockDao.findByServiceMaster(org.easymock.EasyMock.anyInt(), ((java.util.Set<java.lang.String>) (org.easymock.EasyMock.anyObject())))).andReturn(java.util.Collections.singletonList(m_hdfsService)).anyTimes();
        org.easymock.EasyMock.expect(m_mockDao.findByServiceComponent(org.easymock.EasyMock.anyInt(), org.easymock.EasyMock.anyObject(java.lang.String.class), org.easymock.EasyMock.anyObject(java.lang.String.class))).andReturn(java.util.Collections.singletonList(m_hdfsHost)).anyTimes();
        m_agentDefinitions = new java.util.ArrayList<>();
        m_agentDefinitions.add(agentScoped);
        org.easymock.EasyMock.expect(m_mockDao.findAgentScoped(org.easymock.EasyMock.anyInt())).andReturn(m_agentDefinitions).anyTimes();
        org.easymock.EasyMock.replay(m_mockClusters, m_mockCluster, m_mockDao);
        m_hash = m_injector.getInstance(org.apache.ambari.server.state.alert.AlertDefinitionHash.class);
        m_configHelper = m_injector.getInstance(org.apache.ambari.server.state.ConfigHelper.class);
        org.easymock.EasyMock.expect(m_configHelper.getEffectiveDesiredTags(((org.apache.ambari.server.state.Cluster) (EasyMock.anyObject())), org.easymock.EasyMock.anyString())).andReturn(new java.util.HashMap<>()).anyTimes();
        org.easymock.EasyMock.expect(m_configHelper.getEffectiveConfigProperties(((org.apache.ambari.server.state.Cluster) (EasyMock.anyObject())), ((java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>) (EasyMock.anyObject())))).andReturn(new java.util.HashMap<>()).anyTimes();
        org.easymock.EasyMock.replay(m_configHelper);
    }

    @java.lang.Override
    @org.junit.After
    protected void tearDown() throws java.lang.Exception {
        super.tearDown();
    }

    @org.junit.Test
    public void testGetHash() {
        java.lang.String hash = m_hash.getHash(org.apache.ambari.server.state.alerts.AlertDefinitionHashTest.CLUSTERNAME, org.apache.ambari.server.state.alerts.AlertDefinitionHashTest.HOSTNAME);
        junit.framework.Assert.assertNotNull(hash);
        junit.framework.Assert.assertNotSame(org.apache.ambari.server.state.alert.AlertDefinitionHash.NULL_MD5_HASH, hash);
        junit.framework.Assert.assertEquals(hash, m_hash.getHash(org.apache.ambari.server.state.alerts.AlertDefinitionHashTest.CLUSTERNAME, org.apache.ambari.server.state.alerts.AlertDefinitionHashTest.HOSTNAME));
    }

    @org.junit.Test
    public void testGetAlertDefinitions() {
        java.util.List<org.apache.ambari.server.state.alert.AlertDefinition> definitions = m_hash.getAlertDefinitions(org.apache.ambari.server.state.alerts.AlertDefinitionHashTest.CLUSTERNAME, org.apache.ambari.server.state.alerts.AlertDefinitionHashTest.HOSTNAME);
        junit.framework.Assert.assertEquals(3, definitions.size());
    }

    @org.junit.Test
    public void testInvalidateAll() {
        java.lang.String hash = m_hash.getHash(org.apache.ambari.server.state.alerts.AlertDefinitionHashTest.CLUSTERNAME, org.apache.ambari.server.state.alerts.AlertDefinitionHashTest.HOSTNAME);
        junit.framework.Assert.assertNotNull(hash);
        m_hash.invalidateAll();
        java.lang.String newHash = m_hash.getHash(org.apache.ambari.server.state.alerts.AlertDefinitionHashTest.CLUSTERNAME, org.apache.ambari.server.state.alerts.AlertDefinitionHashTest.HOSTNAME);
        junit.framework.Assert.assertEquals(hash, newHash);
        m_hash.invalidateAll();
        org.apache.ambari.server.orm.entities.AlertDefinitionEntity agentScoped = new org.apache.ambari.server.orm.entities.AlertDefinitionEntity();
        agentScoped.setDefinitionId(java.lang.System.currentTimeMillis());
        agentScoped.setClusterId(1L);
        agentScoped.setHash(java.util.UUID.randomUUID().toString());
        agentScoped.setServiceName("AMBARI");
        agentScoped.setComponentName("AMBARI_AGENT");
        agentScoped.setScope(org.apache.ambari.server.state.alert.Scope.HOST);
        agentScoped.setScheduleInterval(1);
        m_agentDefinitions.add(agentScoped);
        newHash = m_hash.getHash(org.apache.ambari.server.state.alerts.AlertDefinitionHashTest.CLUSTERNAME, org.apache.ambari.server.state.alerts.AlertDefinitionHashTest.HOSTNAME);
        junit.framework.Assert.assertNotSame(hash, newHash);
    }

    @org.junit.Test
    public void testIsHashCached() {
        junit.framework.Assert.assertFalse(m_hash.isHashCached(org.apache.ambari.server.state.alerts.AlertDefinitionHashTest.CLUSTERNAME, org.apache.ambari.server.state.alerts.AlertDefinitionHashTest.HOSTNAME));
        java.lang.String hash = m_hash.getHash(org.apache.ambari.server.state.alerts.AlertDefinitionHashTest.CLUSTERNAME, org.apache.ambari.server.state.alerts.AlertDefinitionHashTest.HOSTNAME);
        junit.framework.Assert.assertNotNull(hash);
        junit.framework.Assert.assertTrue(m_hash.isHashCached(org.apache.ambari.server.state.alerts.AlertDefinitionHashTest.CLUSTERNAME, org.apache.ambari.server.state.alerts.AlertDefinitionHashTest.HOSTNAME));
        m_hash.invalidate(org.apache.ambari.server.state.alerts.AlertDefinitionHashTest.HOSTNAME);
        junit.framework.Assert.assertFalse(m_hash.isHashCached(org.apache.ambari.server.state.alerts.AlertDefinitionHashTest.CLUSTERNAME, org.apache.ambari.server.state.alerts.AlertDefinitionHashTest.HOSTNAME));
        hash = m_hash.getHash(org.apache.ambari.server.state.alerts.AlertDefinitionHashTest.CLUSTERNAME, org.apache.ambari.server.state.alerts.AlertDefinitionHashTest.HOSTNAME);
        junit.framework.Assert.assertNotNull(hash);
        junit.framework.Assert.assertTrue(m_hash.isHashCached(org.apache.ambari.server.state.alerts.AlertDefinitionHashTest.CLUSTERNAME, org.apache.ambari.server.state.alerts.AlertDefinitionHashTest.HOSTNAME));
        m_hash.invalidateAll();
        junit.framework.Assert.assertFalse(m_hash.isHashCached(org.apache.ambari.server.state.alerts.AlertDefinitionHashTest.CLUSTERNAME, org.apache.ambari.server.state.alerts.AlertDefinitionHashTest.HOSTNAME));
        hash = m_hash.getHash(org.apache.ambari.server.state.alerts.AlertDefinitionHashTest.CLUSTERNAME, org.apache.ambari.server.state.alerts.AlertDefinitionHashTest.HOSTNAME);
        junit.framework.Assert.assertNotNull(hash);
        junit.framework.Assert.assertTrue(m_hash.isHashCached(org.apache.ambari.server.state.alerts.AlertDefinitionHashTest.CLUSTERNAME, org.apache.ambari.server.state.alerts.AlertDefinitionHashTest.HOSTNAME));
    }

    @org.junit.Test
    public void testInvalidateHosts() {
        junit.framework.Assert.assertFalse(m_hash.isHashCached(org.apache.ambari.server.state.alerts.AlertDefinitionHashTest.CLUSTERNAME, org.apache.ambari.server.state.alerts.AlertDefinitionHashTest.HOSTNAME));
        java.lang.String hash = m_hash.getHash(org.apache.ambari.server.state.alerts.AlertDefinitionHashTest.CLUSTERNAME, org.apache.ambari.server.state.alerts.AlertDefinitionHashTest.HOSTNAME);
        junit.framework.Assert.assertNotNull(hash);
        junit.framework.Assert.assertTrue(m_hash.isHashCached(org.apache.ambari.server.state.alerts.AlertDefinitionHashTest.CLUSTERNAME, org.apache.ambari.server.state.alerts.AlertDefinitionHashTest.HOSTNAME));
        java.util.Set<java.lang.String> invalidatedHosts = m_hash.invalidateHosts(m_hdfsHost);
        junit.framework.Assert.assertFalse(m_hash.isHashCached(org.apache.ambari.server.state.alerts.AlertDefinitionHashTest.CLUSTERNAME, org.apache.ambari.server.state.alerts.AlertDefinitionHashTest.HOSTNAME));
        junit.framework.Assert.assertNotNull(invalidatedHosts);
        junit.framework.Assert.assertEquals(1, invalidatedHosts.size());
        junit.framework.Assert.assertTrue(invalidatedHosts.contains(org.apache.ambari.server.state.alerts.AlertDefinitionHashTest.HOSTNAME));
    }

    @org.junit.Test
    public void testInvalidateHost() {
        junit.framework.Assert.assertFalse(m_hash.isHashCached(org.apache.ambari.server.state.alerts.AlertDefinitionHashTest.CLUSTERNAME, org.apache.ambari.server.state.alerts.AlertDefinitionHashTest.HOSTNAME));
        junit.framework.Assert.assertFalse(m_hash.isHashCached("foo", org.apache.ambari.server.state.alerts.AlertDefinitionHashTest.HOSTNAME));
        java.lang.String hash = m_hash.getHash(org.apache.ambari.server.state.alerts.AlertDefinitionHashTest.CLUSTERNAME, org.apache.ambari.server.state.alerts.AlertDefinitionHashTest.HOSTNAME);
        junit.framework.Assert.assertNotNull(hash);
        junit.framework.Assert.assertTrue(m_hash.isHashCached(org.apache.ambari.server.state.alerts.AlertDefinitionHashTest.CLUSTERNAME, org.apache.ambari.server.state.alerts.AlertDefinitionHashTest.HOSTNAME));
        junit.framework.Assert.assertFalse(m_hash.isHashCached("foo", org.apache.ambari.server.state.alerts.AlertDefinitionHashTest.HOSTNAME));
        m_hash.invalidate("foo", org.apache.ambari.server.state.alerts.AlertDefinitionHashTest.HOSTNAME);
        junit.framework.Assert.assertTrue(m_hash.isHashCached(org.apache.ambari.server.state.alerts.AlertDefinitionHashTest.CLUSTERNAME, org.apache.ambari.server.state.alerts.AlertDefinitionHashTest.HOSTNAME));
        junit.framework.Assert.assertFalse(m_hash.isHashCached("foo", org.apache.ambari.server.state.alerts.AlertDefinitionHashTest.HOSTNAME));
        m_hash.invalidateAll();
        junit.framework.Assert.assertFalse(m_hash.isHashCached(org.apache.ambari.server.state.alerts.AlertDefinitionHashTest.CLUSTERNAME, org.apache.ambari.server.state.alerts.AlertDefinitionHashTest.HOSTNAME));
        junit.framework.Assert.assertFalse(m_hash.isHashCached("foo", org.apache.ambari.server.state.alerts.AlertDefinitionHashTest.HOSTNAME));
    }

    @org.junit.Test
    public void testAggregateIgnored() {
        java.util.Set<java.lang.String> associatedHosts = m_hash.getAssociatedHosts(m_mockCluster, org.apache.ambari.server.state.alert.SourceType.AGGREGATE, "definitionName", "HDFS", null);
        junit.framework.Assert.assertEquals(0, associatedHosts.size());
        associatedHosts = m_hash.getAssociatedHosts(m_mockCluster, org.apache.ambari.server.state.alert.SourceType.PORT, "definitionName", "HDFS", null);
        junit.framework.Assert.assertEquals(1, associatedHosts.size());
    }

    @org.junit.Test
    public void testHashingAlgorithm() throws java.lang.Exception {
        java.util.List<java.lang.String> uuids = new java.util.ArrayList<>();
        uuids.add(m_hdfsService.getHash());
        uuids.add(m_hdfsHost.getHash());
        for (org.apache.ambari.server.orm.entities.AlertDefinitionEntity entity : m_agentDefinitions) {
            uuids.add(entity.getHash());
        }
        java.util.Collections.sort(uuids);
        java.security.MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
        for (java.lang.String uuid : uuids) {
            digest.update(uuid.getBytes());
        }
        byte[] hashBytes = digest.digest();
        java.lang.String expected = org.apache.commons.codec.binary.Hex.encodeHexString(hashBytes);
        junit.framework.Assert.assertEquals(expected, m_hash.getHash(org.apache.ambari.server.state.alerts.AlertDefinitionHashTest.CLUSTERNAME, org.apache.ambari.server.state.alerts.AlertDefinitionHashTest.HOSTNAME));
    }

    private class MockModule implements com.google.inject.Module {
        @java.lang.Override
        public void configure(com.google.inject.Binder binder) {
            org.apache.ambari.server.state.Cluster cluster = org.easymock.EasyMock.createNiceMock(org.apache.ambari.server.state.Cluster.class);
            org.easymock.EasyMock.expect(cluster.getAllConfigs()).andReturn(new java.util.ArrayList<>()).anyTimes();
            binder.bind(org.apache.ambari.server.state.Clusters.class).toInstance(org.easymock.EasyMock.createNiceMock(org.apache.ambari.server.state.Clusters.class));
            binder.bind(org.apache.ambari.server.state.Cluster.class).toInstance(cluster);
            binder.bind(org.apache.ambari.server.orm.dao.AlertDefinitionDAO.class).toInstance(org.easymock.EasyMock.createNiceMock(org.apache.ambari.server.orm.dao.AlertDefinitionDAO.class));
            binder.bind(org.apache.ambari.server.state.ConfigHelper.class).toInstance(org.easymock.EasyMock.createNiceMock(org.apache.ambari.server.state.ConfigHelper.class));
        }
    }
}