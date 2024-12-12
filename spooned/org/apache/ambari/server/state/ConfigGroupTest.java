package org.apache.ambari.server.state;
import com.google.inject.persist.Transactional;
public class ConfigGroupTest {
    private org.apache.ambari.server.state.Clusters clusters;

    private org.apache.ambari.server.state.Cluster cluster;

    private java.lang.String clusterName;

    private com.google.inject.Injector injector;

    private org.apache.ambari.server.state.configgroup.ConfigGroupFactory configGroupFactory;

    private org.apache.ambari.server.state.ConfigFactory configFactory;

    private org.apache.ambari.server.orm.dao.ConfigGroupDAO configGroupDAO;

    private org.apache.ambari.server.orm.dao.ConfigGroupHostMappingDAO configGroupHostMappingDAO;

    @org.junit.Before
    public void setup() throws java.lang.Exception {
        injector = com.google.inject.Guice.createInjector(new org.apache.ambari.server.orm.InMemoryDefaultTestModule());
        injector.getInstance(org.apache.ambari.server.orm.GuiceJpaInitializer.class);
        clusters = injector.getInstance(org.apache.ambari.server.state.Clusters.class);
        configFactory = injector.getInstance(org.apache.ambari.server.state.ConfigFactory.class);
        configGroupFactory = injector.getInstance(org.apache.ambari.server.state.configgroup.ConfigGroupFactory.class);
        configGroupDAO = injector.getInstance(org.apache.ambari.server.orm.dao.ConfigGroupDAO.class);
        configGroupHostMappingDAO = injector.getInstance(org.apache.ambari.server.orm.dao.ConfigGroupHostMappingDAO.class);
        org.apache.ambari.server.state.StackId stackId = new org.apache.ambari.server.state.StackId("HDP-0.1");
        org.apache.ambari.server.orm.OrmTestHelper helper = injector.getInstance(org.apache.ambari.server.orm.OrmTestHelper.class);
        helper.createStack(stackId);
        clusterName = "foo";
        clusters.addCluster(clusterName, stackId);
        cluster = clusters.getCluster(clusterName);
        junit.framework.Assert.assertNotNull(cluster);
        clusters.addHost("h1");
        clusters.addHost("h2");
        junit.framework.Assert.assertNotNull(clusters.getHost("h1"));
        junit.framework.Assert.assertNotNull(clusters.getHost("h2"));
    }

    @org.junit.After
    public void teardown() throws org.apache.ambari.server.AmbariException, java.sql.SQLException {
        org.apache.ambari.server.H2DatabaseCleaner.clearDatabaseAndStopPersistenceService(injector);
    }

    @com.google.inject.persist.Transactional
    org.apache.ambari.server.state.configgroup.ConfigGroup createConfigGroup() throws org.apache.ambari.server.AmbariException {
        java.util.Map<java.lang.String, java.lang.String> properties = new java.util.HashMap<>();
        properties.put("a", "b");
        properties.put("c", "d");
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> propertiesAttributes = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> attributes = new java.util.HashMap<>();
        attributes.put("a", "true");
        propertiesAttributes.put("final", attributes);
        org.apache.ambari.server.state.Config config = configFactory.createNew(cluster, "hdfs-site", "testversion", properties, propertiesAttributes);
        org.apache.ambari.server.state.Host host = clusters.getHost("h1");
        java.util.Map<java.lang.String, org.apache.ambari.server.state.Config> configs = new java.util.HashMap<>();
        java.util.Map<java.lang.Long, org.apache.ambari.server.state.Host> hosts = new java.util.HashMap<>();
        configs.put(config.getType(), config);
        hosts.put(host.getHostId(), host);
        org.apache.ambari.server.state.configgroup.ConfigGroup configGroup = configGroupFactory.createNew(cluster, "HDFS", "cg-test", "HDFS", "New HDFS configs for h1", configs, hosts);
        cluster.addConfigGroup(configGroup);
        return configGroup;
    }

    @org.junit.Test
    public void testCreateNewConfigGroup() throws java.lang.Exception {
        org.apache.ambari.server.state.configgroup.ConfigGroup configGroup = createConfigGroup();
        junit.framework.Assert.assertNotNull(configGroup);
        org.apache.ambari.server.orm.entities.ConfigGroupEntity configGroupEntity = configGroupDAO.findByName("cg-test");
        junit.framework.Assert.assertNotNull(configGroupEntity);
        junit.framework.Assert.assertEquals("HDFS", configGroupEntity.getTag());
        org.apache.ambari.server.orm.entities.ConfigGroupConfigMappingEntity configMappingEntity = configGroupEntity.getConfigGroupConfigMappingEntities().iterator().next();
        junit.framework.Assert.assertNotNull(configMappingEntity);
        junit.framework.Assert.assertEquals("hdfs-site", configMappingEntity.getConfigType());
        junit.framework.Assert.assertEquals("testversion", configMappingEntity.getVersionTag());
        junit.framework.Assert.assertNotNull(configMappingEntity.getClusterConfigEntity());
        junit.framework.Assert.assertTrue(configMappingEntity.getClusterConfigEntity().getData().contains("a"));
        junit.framework.Assert.assertEquals("{\"final\":{\"a\":\"true\"}}", configMappingEntity.getClusterConfigEntity().getAttributes());
        org.apache.ambari.server.orm.entities.ConfigGroupHostMappingEntity hostMappingEntity = configGroupEntity.getConfigGroupHostMappingEntities().iterator().next();
        junit.framework.Assert.assertNotNull(hostMappingEntity);
        junit.framework.Assert.assertEquals("h1", hostMappingEntity.getHostname());
    }

    @org.junit.Test
    @com.google.inject.persist.Transactional
    public void testUpdateConfigGroup() throws java.lang.Exception {
        org.apache.ambari.server.state.configgroup.ConfigGroup configGroup = createConfigGroup();
        junit.framework.Assert.assertNotNull(configGroup);
        org.apache.ambari.server.orm.entities.ConfigGroupEntity configGroupEntity = configGroupDAO.findById(configGroup.getId());
        junit.framework.Assert.assertNotNull(configGroupEntity);
        configGroup = configGroupFactory.createExisting(cluster, configGroupEntity);
        org.apache.ambari.server.state.Host host = clusters.getHost("h2");
        configGroup.addHost(host);
        junit.framework.Assert.assertEquals(2, configGroup.getHosts().values().size());
        java.util.Map<java.lang.String, java.lang.String> properties = new java.util.HashMap<>();
        properties.put("key1", "value1");
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> propertiesAttributes = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> attributes = new java.util.HashMap<>();
        attributes.put("key1", "true");
        propertiesAttributes.put("final", attributes);
        org.apache.ambari.server.state.Config config = configFactory.createNew(cluster, "test-site", "version100", properties, propertiesAttributes);
        java.util.Map<java.lang.String, org.apache.ambari.server.state.Config> newConfigurations = new java.util.HashMap<>(configGroup.getConfigurations());
        newConfigurations.put(config.getType(), config);
        configGroup.setConfigurations(newConfigurations);
        junit.framework.Assert.assertEquals(2, configGroup.getConfigurations().values().size());
        configGroupEntity = configGroupDAO.findById(configGroup.getId());
        junit.framework.Assert.assertEquals(2, configGroupEntity.getConfigGroupConfigMappingEntities().size());
        configGroup.setName("NewName");
        configGroup.setDescription("NewDesc");
        configGroup.setTag("NewTag");
        configGroupEntity = configGroupDAO.findByName("NewName");
        junit.framework.Assert.assertNotNull(configGroupEntity);
        junit.framework.Assert.assertEquals(2, configGroupEntity.getConfigGroupHostMappingEntities().size());
        junit.framework.Assert.assertEquals(2, configGroupEntity.getConfigGroupConfigMappingEntities().size());
        junit.framework.Assert.assertEquals("NewTag", configGroupEntity.getTag());
        junit.framework.Assert.assertEquals("NewDesc", configGroupEntity.getDescription());
        junit.framework.Assert.assertNotNull(cluster.getConfig("test-site", "version100"));
        org.apache.ambari.server.orm.entities.ConfigGroupConfigMappingEntity configMappingEntity = null;
        java.lang.Object[] array = configGroupEntity.getConfigGroupConfigMappingEntities().toArray();
        for (java.lang.Object o : array) {
            if ("test-site".equals(((org.apache.ambari.server.orm.entities.ConfigGroupConfigMappingEntity) (o)).getConfigType())) {
                configMappingEntity = ((org.apache.ambari.server.orm.entities.ConfigGroupConfigMappingEntity) (o));
                break;
            }
        }
        junit.framework.Assert.assertNotNull(configMappingEntity);
        junit.framework.Assert.assertTrue(configMappingEntity.getClusterConfigEntity().getData().contains("{\"key1\":\"value1\"}"));
        junit.framework.Assert.assertEquals("{\"final\":{\"key1\":\"true\"}}", configMappingEntity.getClusterConfigEntity().getAttributes());
    }

    @org.junit.Test
    public void testDeleteConfigGroup() throws java.lang.Exception {
        org.apache.ambari.server.state.configgroup.ConfigGroup configGroup = createConfigGroup();
        junit.framework.Assert.assertNotNull(configGroup);
        java.lang.Long id = configGroup.getId();
        configGroup.delete();
        junit.framework.Assert.assertNull(configGroupDAO.findById(id));
    }

    @org.junit.Test
    public void testRemoveHost() throws java.lang.Exception {
        org.apache.ambari.server.state.configgroup.ConfigGroup configGroup = createConfigGroup();
        junit.framework.Assert.assertNotNull(configGroup);
        java.lang.Long id = configGroup.getId();
        configGroup = cluster.getConfigGroups().get(id);
        junit.framework.Assert.assertNotNull(configGroup);
        long hostId = clusters.getHost("h1").getHostId();
        clusters.unmapHostFromCluster("h1", clusterName);
        junit.framework.Assert.assertNull(clusters.getHostsForCluster(clusterName).get("h1"));
        junit.framework.Assert.assertNotNull(configGroupHostMappingDAO.findByHostId(hostId));
        junit.framework.Assert.assertTrue(configGroupHostMappingDAO.findByHostId(hostId).isEmpty());
        junit.framework.Assert.assertFalse(configGroup.getHosts().containsKey(hostId));
    }

    @org.junit.Test
    public void testGetConfigGroup() throws java.lang.Exception {
        org.apache.ambari.server.state.configgroup.ConfigGroup configGroup = createConfigGroup();
        junit.framework.Assert.assertNotNull(configGroup);
        junit.framework.Assert.assertNotNull(cluster.getConfigGroups().get(configGroup.getId()));
        org.apache.ambari.server.orm.entities.ConfigGroupEntity configGroupEntity = configGroupDAO.findById(configGroup.getId());
        java.util.Collection<org.apache.ambari.server.orm.entities.ConfigGroupConfigMappingEntity> configMappingEntities = configGroupEntity.getConfigGroupConfigMappingEntities();
        java.util.Collection<org.apache.ambari.server.orm.entities.ConfigGroupHostMappingEntity> hostMappingEntities = configGroupEntity.getConfigGroupHostMappingEntities();
        junit.framework.Assert.assertEquals(configGroup.getId(), configGroupEntity.getGroupId());
        junit.framework.Assert.assertEquals(configGroup.getTag(), configGroupEntity.getTag());
        junit.framework.Assert.assertNotNull(configMappingEntities);
        junit.framework.Assert.assertNotNull(hostMappingEntities);
        junit.framework.Assert.assertEquals("h1", hostMappingEntities.iterator().next().getHostname());
        org.apache.ambari.server.orm.entities.ConfigGroupConfigMappingEntity configMappingEntity = configMappingEntities.iterator().next();
        junit.framework.Assert.assertEquals("hdfs-site", configMappingEntity.getConfigType());
        junit.framework.Assert.assertEquals("testversion", configMappingEntity.getVersionTag());
    }
}