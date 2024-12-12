package org.apache.ambari.server.orm.dao;
public class ConfigGroupDAOTest {
    private com.google.inject.Injector injector;

    private org.apache.ambari.server.orm.dao.ConfigGroupDAO configGroupDAO;

    private org.apache.ambari.server.orm.dao.ClusterDAO clusterDAO;

    private org.apache.ambari.server.orm.dao.ConfigGroupConfigMappingDAO configGroupConfigMappingDAO;

    private org.apache.ambari.server.orm.dao.ConfigGroupHostMappingDAO configGroupHostMappingDAO;

    private org.apache.ambari.server.orm.dao.HostDAO hostDAO;

    private org.apache.ambari.server.orm.dao.ResourceTypeDAO resourceTypeDAO;

    private org.apache.ambari.server.orm.dao.StackDAO stackDAO;

    private org.apache.ambari.server.state.host.HostFactory hostFactory;

    @org.junit.Before
    public void setup() throws java.lang.Exception {
        injector = com.google.inject.Guice.createInjector(new org.apache.ambari.server.orm.InMemoryDefaultTestModule());
        injector.getInstance(org.apache.ambari.server.orm.GuiceJpaInitializer.class);
        injector.getInstance(org.apache.ambari.server.api.services.AmbariMetaInfo.class);
        stackDAO = injector.getInstance(org.apache.ambari.server.orm.dao.StackDAO.class);
        clusterDAO = injector.getInstance(org.apache.ambari.server.orm.dao.ClusterDAO.class);
        configGroupDAO = injector.getInstance(org.apache.ambari.server.orm.dao.ConfigGroupDAO.class);
        configGroupConfigMappingDAO = injector.getInstance(org.apache.ambari.server.orm.dao.ConfigGroupConfigMappingDAO.class);
        configGroupHostMappingDAO = injector.getInstance(org.apache.ambari.server.orm.dao.ConfigGroupHostMappingDAO.class);
        hostDAO = injector.getInstance(org.apache.ambari.server.orm.dao.HostDAO.class);
        resourceTypeDAO = injector.getInstance(org.apache.ambari.server.orm.dao.ResourceTypeDAO.class);
        hostFactory = injector.getInstance(org.apache.ambari.server.state.host.HostFactory.class);
    }

    @org.junit.After
    public void teardown() throws org.apache.ambari.server.AmbariException, java.sql.SQLException {
        org.apache.ambari.server.H2DatabaseCleaner.clearDatabaseAndStopPersistenceService(injector);
    }

    private org.apache.ambari.server.orm.entities.ConfigGroupEntity createConfigGroup(java.lang.String clusterName, java.lang.String groupName, java.lang.String tag, java.lang.String desc, java.util.List<org.apache.ambari.server.orm.entities.HostEntity> hosts, java.util.List<org.apache.ambari.server.orm.entities.ClusterConfigEntity> configs) throws java.lang.Exception {
        org.apache.ambari.server.state.Clusters clusters = injector.getInstance(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.orm.entities.StackEntity stackEntity = stackDAO.find("HDP", "0.1");
        clusters.addCluster(clusterName, new org.apache.ambari.server.state.StackId(stackEntity));
        org.apache.ambari.server.orm.entities.ClusterEntity clusterEntity = clusterDAO.findByName(clusterName);
        org.apache.ambari.server.orm.entities.ConfigGroupEntity configGroupEntity = new org.apache.ambari.server.orm.entities.ConfigGroupEntity();
        configGroupEntity.setClusterEntity(clusterEntity);
        configGroupEntity.setClusterId(clusterEntity.getClusterId());
        configGroupEntity.setGroupName(groupName);
        configGroupEntity.setDescription(desc);
        configGroupEntity.setTag(tag);
        configGroupDAO.create(configGroupEntity);
        if ((hosts != null) && (!hosts.isEmpty())) {
            java.util.List<org.apache.ambari.server.orm.entities.ConfigGroupHostMappingEntity> hostMappingEntities = new java.util.ArrayList<>();
            for (org.apache.ambari.server.orm.entities.HostEntity host : hosts) {
                host.setClusterEntities(java.util.Arrays.asList(clusterEntity));
                hostDAO.create(host);
                org.apache.ambari.server.orm.entities.ConfigGroupHostMappingEntity hostMappingEntity = new org.apache.ambari.server.orm.entities.ConfigGroupHostMappingEntity();
                hostMappingEntity.setHostId(host.getHostId());
                hostMappingEntity.setHostEntity(host);
                hostMappingEntity.setConfigGroupEntity(configGroupEntity);
                hostMappingEntity.setConfigGroupId(configGroupEntity.getGroupId());
                hostMappingEntities.add(hostMappingEntity);
                configGroupHostMappingDAO.create(hostMappingEntity);
            }
            configGroupEntity.setConfigGroupHostMappingEntities(hostMappingEntities);
            configGroupDAO.merge(configGroupEntity);
        }
        if ((configs != null) && (!configs.isEmpty())) {
            java.util.List<org.apache.ambari.server.orm.entities.ConfigGroupConfigMappingEntity> configMappingEntities = new java.util.ArrayList<>();
            for (org.apache.ambari.server.orm.entities.ClusterConfigEntity config : configs) {
                config.setClusterEntity(clusterEntity);
                config.setClusterId(clusterEntity.getClusterId());
                clusterDAO.createConfig(config);
                org.apache.ambari.server.orm.entities.ConfigGroupConfigMappingEntity configMappingEntity = new org.apache.ambari.server.orm.entities.ConfigGroupConfigMappingEntity();
                configMappingEntity.setClusterId(clusterEntity.getClusterId());
                configMappingEntity.setClusterConfigEntity(config);
                configMappingEntity.setConfigGroupEntity(configGroupEntity);
                configMappingEntity.setConfigGroupId(configGroupEntity.getGroupId());
                configMappingEntity.setVersionTag(config.getTag());
                configMappingEntity.setConfigType(config.getType());
                configMappingEntity.setTimestamp(java.lang.System.currentTimeMillis());
                configMappingEntities.add(configMappingEntity);
                configGroupConfigMappingDAO.create(configMappingEntity);
            }
            configGroupEntity.setConfigGroupConfigMappingEntities(configMappingEntities);
            configGroupDAO.merge(configGroupEntity);
        }
        return configGroupEntity;
    }

    @org.junit.Test
    public void testCreatePlaneJaneCG() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.ConfigGroupEntity configGroupEntity = createConfigGroup("c1", "hdfs-1", "HDFS", "some description", null, null);
        java.lang.Long clusterId = clusterDAO.findByName("c1").getClusterId();
        junit.framework.Assert.assertNotNull(configGroupEntity);
        junit.framework.Assert.assertEquals("c1", configGroupEntity.getClusterEntity().getClusterName());
        junit.framework.Assert.assertEquals(clusterId, configGroupEntity.getClusterEntity().getClusterId());
        junit.framework.Assert.assertEquals("hdfs-1", configGroupEntity.getGroupName());
        junit.framework.Assert.assertEquals("HDFS", configGroupEntity.getTag());
        junit.framework.Assert.assertEquals("some description", configGroupEntity.getDescription());
    }

    @org.junit.Test
    public void testFindByTag() throws java.lang.Exception {
        createConfigGroup("c1", "hdfs-1", "HDFS", "some description", null, null);
        java.util.List<org.apache.ambari.server.orm.entities.ConfigGroupEntity> configGroupEntities = configGroupDAO.findAllByTag("HDFS");
        java.lang.Long clusterId = clusterDAO.findByName("c1").getClusterId();
        junit.framework.Assert.assertNotNull(configGroupEntities);
        org.apache.ambari.server.orm.entities.ConfigGroupEntity configGroupEntity = configGroupEntities.get(0);
        junit.framework.Assert.assertNotNull(configGroupEntity);
        junit.framework.Assert.assertEquals("c1", configGroupEntity.getClusterEntity().getClusterName());
        junit.framework.Assert.assertEquals(clusterId, configGroupEntity.getClusterEntity().getClusterId());
        junit.framework.Assert.assertEquals("hdfs-1", configGroupEntity.getGroupName());
        junit.framework.Assert.assertEquals("HDFS", configGroupEntity.getTag());
        junit.framework.Assert.assertEquals("some description", configGroupEntity.getDescription());
    }

    @org.junit.Test
    public void testFindByName() throws java.lang.Exception {
        createConfigGroup("c1", "hdfs-1", "HDFS", "some description", null, null);
        org.apache.ambari.server.orm.entities.ConfigGroupEntity configGroupEntity = configGroupDAO.findByName("hdfs-1");
        java.lang.Long clusterId = clusterDAO.findByName("c1").getClusterId();
        junit.framework.Assert.assertNotNull(configGroupEntity);
        junit.framework.Assert.assertEquals("c1", configGroupEntity.getClusterEntity().getClusterName());
        junit.framework.Assert.assertEquals(clusterId, configGroupEntity.getClusterEntity().getClusterId());
        junit.framework.Assert.assertEquals("hdfs-1", configGroupEntity.getGroupName());
        junit.framework.Assert.assertEquals("HDFS", configGroupEntity.getTag());
        junit.framework.Assert.assertEquals("some description", configGroupEntity.getDescription());
    }

    @org.junit.Test
    public void testFindByHost() throws java.lang.Exception {
        java.util.List<org.apache.ambari.server.orm.entities.HostEntity> hosts = new java.util.ArrayList<>();
        org.apache.ambari.server.orm.entities.HostEntity hostEntity = new org.apache.ambari.server.orm.entities.HostEntity();
        hostEntity.setHostName("h1");
        hostEntity.setOsType("centOS");
        hosts.add(hostEntity);
        org.apache.ambari.server.orm.entities.ConfigGroupEntity configGroupEntity = createConfigGroup("c1", "hdfs-1", "HDFS", "some description", hosts, null);
        junit.framework.Assert.assertNotNull(hostEntity.getHostId());
        junit.framework.Assert.assertNotNull(configGroupEntity);
        junit.framework.Assert.assertTrue(configGroupEntity.getConfigGroupHostMappingEntities().size() > 0);
        junit.framework.Assert.assertNotNull(configGroupEntity.getConfigGroupHostMappingEntities().iterator().next());
        java.util.Set<org.apache.ambari.server.orm.cache.ConfigGroupHostMapping> hostMappingEntities = configGroupHostMappingDAO.findByHostId(hostEntity.getHostId());
        junit.framework.Assert.assertNotNull(hostMappingEntities);
        for (org.apache.ambari.server.orm.cache.ConfigGroupHostMapping hostMappingEntity : hostMappingEntities) {
            junit.framework.Assert.assertEquals(hostEntity.getHostId(), hostMappingEntity.getHostId());
            junit.framework.Assert.assertEquals("centOS", hostMappingEntity.getHost().getOsType());
        }
    }

    @org.junit.Test
    public void testFindConfigsByGroup() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.StackEntity stackEntity = stackDAO.find("HDP", "0.1");
        org.apache.ambari.server.orm.entities.ClusterConfigEntity configEntity = new org.apache.ambari.server.orm.entities.ClusterConfigEntity();
        configEntity.setType("core-site");
        configEntity.setTag("version1");
        configEntity.setData("someData");
        configEntity.setAttributes("someAttributes");
        configEntity.setStack(stackEntity);
        java.util.List<org.apache.ambari.server.orm.entities.ClusterConfigEntity> configEntities = new java.util.ArrayList<>();
        configEntities.add(configEntity);
        org.apache.ambari.server.orm.entities.ConfigGroupEntity configGroupEntity = createConfigGroup("c1", "hdfs-1", "HDFS", "some description", null, configEntities);
        junit.framework.Assert.assertNotNull(configGroupEntity);
        junit.framework.Assert.assertTrue(configGroupEntity.getConfigGroupConfigMappingEntities().size() > 0);
        java.util.List<org.apache.ambari.server.orm.entities.ConfigGroupConfigMappingEntity> configMappingEntities = configGroupConfigMappingDAO.findByGroup(configGroupEntity.getGroupId());
        junit.framework.Assert.assertNotNull(configEntities);
        junit.framework.Assert.assertEquals("core-site", configEntities.get(0).getType());
        junit.framework.Assert.assertEquals("version1", configEntities.get(0).getTag());
        junit.framework.Assert.assertEquals("someData", configEntities.get(0).getData());
        junit.framework.Assert.assertEquals("someAttributes", configEntities.get(0).getAttributes());
    }
}