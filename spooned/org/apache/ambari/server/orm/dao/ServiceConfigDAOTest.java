package org.apache.ambari.server.orm.dao;
public class ServiceConfigDAOTest {
    private static final org.apache.ambari.server.state.StackId HDP_01 = new org.apache.ambari.server.state.StackId("HDP", "0.1");

    private static final org.apache.ambari.server.state.StackId HDP_02 = new org.apache.ambari.server.state.StackId("HDP", "0.2");

    private com.google.inject.Injector injector;

    private org.apache.ambari.server.orm.dao.ServiceConfigDAO serviceConfigDAO;

    private org.apache.ambari.server.orm.dao.ClusterDAO clusterDAO;

    private org.apache.ambari.server.orm.dao.ResourceTypeDAO resourceTypeDAO;

    private org.apache.ambari.server.orm.dao.StackDAO stackDAO;

    private org.apache.ambari.server.orm.dao.ConfigGroupDAO configGroupDAO;

    private org.apache.ambari.server.orm.dao.ConfigGroupConfigMappingDAO configGroupConfigMappingDAO;

    @org.junit.Before
    public void setup() throws java.lang.Exception {
        injector = com.google.inject.Guice.createInjector(new org.apache.ambari.server.orm.InMemoryDefaultTestModule());
        injector.getInstance(org.apache.ambari.server.orm.GuiceJpaInitializer.class);
        injector.getInstance(org.apache.ambari.server.api.services.AmbariMetaInfo.class);
        clusterDAO = injector.getInstance(org.apache.ambari.server.orm.dao.ClusterDAO.class);
        stackDAO = injector.getInstance(org.apache.ambari.server.orm.dao.StackDAO.class);
        serviceConfigDAO = injector.getInstance(org.apache.ambari.server.orm.dao.ServiceConfigDAO.class);
        resourceTypeDAO = injector.getInstance(org.apache.ambari.server.orm.dao.ResourceTypeDAO.class);
        configGroupDAO = injector.getInstance(org.apache.ambari.server.orm.dao.ConfigGroupDAO.class);
        configGroupConfigMappingDAO = injector.getInstance(org.apache.ambari.server.orm.dao.ConfigGroupConfigMappingDAO.class);
    }

    @org.junit.After
    public void teardown() throws org.apache.ambari.server.AmbariException, java.sql.SQLException {
        org.apache.ambari.server.H2DatabaseCleaner.clearDatabaseAndStopPersistenceService(injector);
    }

    private org.apache.ambari.server.orm.entities.ServiceConfigEntity createServiceConfig(java.lang.String serviceName, java.lang.String userName, java.lang.Long version, java.lang.Long serviceConfigId, java.lang.Long createTimestamp, java.util.List<org.apache.ambari.server.orm.entities.ClusterConfigEntity> clusterConfigEntities) throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.ServiceConfigEntity serviceConfigEntity = prepareServiceConfig(serviceName, userName, version, serviceConfigId, createTimestamp, clusterConfigEntities);
        serviceConfigDAO.create(serviceConfigEntity);
        return serviceConfigEntity;
    }

    private org.apache.ambari.server.orm.entities.ServiceConfigEntity createServiceConfigWithGroup(java.lang.String serviceName, java.lang.String userName, java.lang.Long version, java.lang.Long serviceConfigId, java.lang.Long createTimestamp, java.util.List<org.apache.ambari.server.orm.entities.ClusterConfigEntity> clusterConfigEntities, java.lang.Long groupId) throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.ServiceConfigEntity serviceConfigEntity = prepareServiceConfig(serviceName, userName, version, serviceConfigId, createTimestamp, clusterConfigEntities);
        serviceConfigEntity.setGroupId(groupId);
        serviceConfigDAO.create(serviceConfigEntity);
        return serviceConfigEntity;
    }

    private org.apache.ambari.server.orm.entities.ServiceConfigEntity prepareServiceConfig(java.lang.String serviceName, java.lang.String userName, java.lang.Long version, java.lang.Long serviceConfigId, java.lang.Long createTimestamp, java.util.List<org.apache.ambari.server.orm.entities.ClusterConfigEntity> clusterConfigEntities) throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.ResourceTypeEntity resourceTypeEntity = resourceTypeDAO.findById(org.apache.ambari.server.security.authorization.ResourceType.CLUSTER.getId());
        if (resourceTypeEntity == null) {
            resourceTypeEntity = new org.apache.ambari.server.orm.entities.ResourceTypeEntity();
            resourceTypeEntity.setId(org.apache.ambari.server.security.authorization.ResourceType.CLUSTER.getId());
            resourceTypeEntity.setName(org.apache.ambari.server.security.authorization.ResourceType.CLUSTER.name());
            resourceTypeEntity = resourceTypeDAO.merge(resourceTypeEntity);
        }
        org.apache.ambari.server.orm.entities.ResourceEntity resourceEntity = new org.apache.ambari.server.orm.entities.ResourceEntity();
        resourceEntity.setResourceType(resourceTypeEntity);
        org.apache.ambari.server.orm.entities.ClusterEntity clusterEntity = clusterDAO.findByName("c1");
        if (clusterEntity == null) {
            org.apache.ambari.server.orm.entities.StackEntity stackEntity = stackDAO.find(org.apache.ambari.server.orm.dao.ServiceConfigDAOTest.HDP_01.getStackName(), org.apache.ambari.server.orm.dao.ServiceConfigDAOTest.HDP_01.getStackVersion());
            clusterEntity = new org.apache.ambari.server.orm.entities.ClusterEntity();
            clusterEntity.setClusterName("c1");
            clusterEntity.setResource(resourceEntity);
            clusterEntity.setDesiredStack(stackEntity);
            clusterDAO.create(clusterEntity);
        }
        org.apache.ambari.server.orm.entities.ServiceConfigEntity serviceConfigEntity = new org.apache.ambari.server.orm.entities.ServiceConfigEntity();
        serviceConfigEntity.setServiceName(serviceName);
        serviceConfigEntity.setUser(userName);
        serviceConfigEntity.setVersion(version);
        serviceConfigEntity.setServiceConfigId(serviceConfigId);
        serviceConfigEntity.setClusterId(clusterEntity.getClusterId());
        serviceConfigEntity.setCreateTimestamp(createTimestamp);
        serviceConfigEntity.setClusterConfigEntities(clusterConfigEntities);
        serviceConfigEntity.setClusterEntity(clusterEntity);
        serviceConfigEntity.setStack(clusterEntity.getDesiredStack());
        return serviceConfigEntity;
    }

    @org.junit.Test
    public void testCreateServiceConfigVersion() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.ServiceConfigEntity serviceConfigEntity = createServiceConfig("HDFS", "admin", 1L, 1L, 1111L, null);
        java.lang.Long clusterId = clusterDAO.findByName("c1").getClusterId();
        org.junit.Assert.assertNotNull(serviceConfigEntity);
        org.junit.Assert.assertEquals("c1", serviceConfigEntity.getClusterEntity().getClusterName());
        org.junit.Assert.assertEquals(clusterId, serviceConfigEntity.getClusterEntity().getClusterId());
        org.junit.Assert.assertEquals("HDFS", serviceConfigEntity.getServiceName());
        org.junit.Assert.assertEquals(java.lang.Long.valueOf(1111L), serviceConfigEntity.getCreateTimestamp());
        org.junit.Assert.assertEquals("admin", serviceConfigEntity.getUser());
        org.junit.Assert.assertEquals(java.lang.Long.valueOf(1), serviceConfigEntity.getVersion());
        org.junit.Assert.assertTrue(serviceConfigEntity.getClusterConfigEntities().isEmpty());
        org.junit.Assert.assertNotNull(serviceConfigEntity.getServiceConfigId());
    }

    @org.junit.Test
    public void testFindServiceConfigEntity() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.ServiceConfigEntity sce = createServiceConfig("HDFS", "admin", 1L, 1L, 1111L, null);
        org.apache.ambari.server.orm.entities.ServiceConfigEntity serviceConfigEntity = serviceConfigDAO.find(sce.getServiceConfigId());
        java.lang.Long clusterId = clusterDAO.findByName("c1").getClusterId();
        org.junit.Assert.assertNotNull(serviceConfigEntity);
        org.junit.Assert.assertEquals("c1", serviceConfigEntity.getClusterEntity().getClusterName());
        org.junit.Assert.assertEquals(clusterId, serviceConfigEntity.getClusterEntity().getClusterId());
        org.junit.Assert.assertEquals("HDFS", serviceConfigEntity.getServiceName());
        org.junit.Assert.assertEquals(java.lang.Long.valueOf(1111L), serviceConfigEntity.getCreateTimestamp());
        org.junit.Assert.assertEquals("admin", serviceConfigEntity.getUser());
        org.junit.Assert.assertEquals(java.lang.Long.valueOf(1), serviceConfigEntity.getVersion());
        org.junit.Assert.assertTrue(serviceConfigEntity.getClusterConfigEntities().isEmpty());
        org.junit.Assert.assertNotNull(serviceConfigEntity.getServiceConfigId());
    }

    @org.junit.Test
    public void testFindByServiceAndVersion() throws java.lang.Exception {
        createServiceConfig("HDFS", "admin", 1L, 1L, 1111L, null);
        org.apache.ambari.server.orm.entities.ServiceConfigEntity serviceConfigEntity = serviceConfigDAO.findByServiceAndVersion("HDFS", 1L);
        java.lang.Long clusterId = clusterDAO.findByName("c1").getClusterId();
        org.junit.Assert.assertNotNull(serviceConfigEntity);
        org.junit.Assert.assertEquals("c1", serviceConfigEntity.getClusterEntity().getClusterName());
        org.junit.Assert.assertEquals(clusterId, serviceConfigEntity.getClusterEntity().getClusterId());
        org.junit.Assert.assertEquals("HDFS", serviceConfigEntity.getServiceName());
        org.junit.Assert.assertEquals(java.lang.Long.valueOf(1111L), serviceConfigEntity.getCreateTimestamp());
        org.junit.Assert.assertEquals("admin", serviceConfigEntity.getUser());
        org.junit.Assert.assertEquals(java.lang.Long.valueOf(1), serviceConfigEntity.getVersion());
        org.junit.Assert.assertTrue(serviceConfigEntity.getClusterConfigEntities().isEmpty());
        org.junit.Assert.assertNotNull(serviceConfigEntity.getServiceConfigId());
    }

    @org.junit.Test
    public void testFindMaxVersions() throws java.lang.Exception {
        createServiceConfig("HDFS", "admin", 1L, 1L, 1111L, null);
        createServiceConfig("HDFS", "admin", 2L, 2L, 2222L, null);
        createServiceConfig("YARN", "admin", 1L, 3L, 3333L, null);
        long hdfsVersion = serviceConfigDAO.findNextServiceConfigVersion(clusterDAO.findByName("c1").getClusterId(), "HDFS");
        long yarnVersion = serviceConfigDAO.findNextServiceConfigVersion(clusterDAO.findByName("c1").getClusterId(), "YARN");
        org.junit.Assert.assertEquals(3, hdfsVersion);
        org.junit.Assert.assertEquals(2, yarnVersion);
    }

    @org.junit.Test
    public void testGetLastServiceConfigs() throws java.lang.Exception {
        createServiceConfig("HDFS", "admin", 1L, 1L, 1111L, null);
        createServiceConfig("HDFS", "admin", 2L, 2L, 2222L, null);
        createServiceConfig("YARN", "admin", 1L, 3L, 3333L, null);
        java.util.List<org.apache.ambari.server.orm.entities.ServiceConfigEntity> serviceConfigEntities = serviceConfigDAO.getLastServiceConfigs(clusterDAO.findByName("c1").getClusterId());
        org.junit.Assert.assertNotNull(serviceConfigEntities);
        org.junit.Assert.assertEquals(2, serviceConfigEntities.size());
        java.lang.Long clusterId = clusterDAO.findByName("c1").getClusterId();
        for (org.apache.ambari.server.orm.entities.ServiceConfigEntity sce : serviceConfigEntities) {
            if ("HDFS".equals(sce.getServiceName())) {
                org.junit.Assert.assertEquals("c1", sce.getClusterEntity().getClusterName());
                org.junit.Assert.assertEquals(clusterId, sce.getClusterEntity().getClusterId());
                org.junit.Assert.assertEquals(java.lang.Long.valueOf(2222L), sce.getCreateTimestamp());
                org.junit.Assert.assertEquals(java.lang.Long.valueOf(2), sce.getVersion());
                org.junit.Assert.assertTrue(sce.getClusterConfigEntities().isEmpty());
                org.junit.Assert.assertNotNull(sce.getServiceConfigId());
            }
            if ("YARN".equals(sce.getServiceName())) {
                org.junit.Assert.assertEquals("c1", sce.getClusterEntity().getClusterName());
                org.junit.Assert.assertEquals(clusterId, sce.getClusterEntity().getClusterId());
                org.junit.Assert.assertEquals(java.lang.Long.valueOf(3333L), sce.getCreateTimestamp());
                org.junit.Assert.assertEquals(java.lang.Long.valueOf(1), sce.getVersion());
                org.junit.Assert.assertTrue(sce.getClusterConfigEntities().isEmpty());
                org.junit.Assert.assertNotNull(sce.getServiceConfigId());
            }
            org.junit.Assert.assertEquals("admin", sce.getUser());
        }
    }

    @org.junit.Test
    public void testGetLastServiceConfigsForService() throws java.lang.Exception {
        java.lang.String serviceName = "HDFS";
        org.apache.ambari.server.state.Clusters clusters = injector.getInstance(org.apache.ambari.server.state.Clusters.class);
        clusters.addCluster("c1", org.apache.ambari.server.orm.dao.ServiceConfigDAOTest.HDP_01);
        org.apache.ambari.server.orm.entities.ConfigGroupEntity configGroupEntity1 = new org.apache.ambari.server.orm.entities.ConfigGroupEntity();
        org.apache.ambari.server.orm.entities.ClusterEntity clusterEntity = clusterDAO.findByName("c1");
        configGroupEntity1.setClusterEntity(clusterEntity);
        configGroupEntity1.setClusterId(clusterEntity.getClusterId());
        configGroupEntity1.setGroupName("group1");
        configGroupEntity1.setDescription("group1_desc");
        configGroupEntity1.setTag("HDFS");
        configGroupEntity1.setServiceName("HDFS");
        configGroupDAO.create(configGroupEntity1);
        org.apache.ambari.server.orm.entities.ConfigGroupEntity group1 = configGroupDAO.findByName("group1");
        org.apache.ambari.server.orm.entities.ConfigGroupEntity configGroupEntity2 = new org.apache.ambari.server.orm.entities.ConfigGroupEntity();
        configGroupEntity2.setClusterEntity(clusterEntity);
        configGroupEntity2.setClusterId(clusterEntity.getClusterId());
        configGroupEntity2.setGroupName("group2");
        configGroupEntity2.setDescription("group2_desc");
        configGroupEntity2.setTag("HDFS");
        configGroupEntity2.setServiceName("HDFS");
        configGroupDAO.create(configGroupEntity2);
        org.apache.ambari.server.orm.entities.ConfigGroupEntity group2 = configGroupDAO.findByName("group2");
        createServiceConfig(serviceName, "admin", 1L, 1L, 1111L, null);
        createServiceConfig(serviceName, "admin", 2L, 2L, 1010L, null);
        createServiceConfigWithGroup(serviceName, "admin", 3L, 3L, 2222L, null, group1.getGroupId());
        createServiceConfigWithGroup(serviceName, "admin", 5L, 5L, 3333L, null, group2.getGroupId());
        createServiceConfigWithGroup(serviceName, "admin", 4L, 4L, 3330L, null, group2.getGroupId());
        java.util.List<org.apache.ambari.server.orm.entities.ServiceConfigEntity> serviceConfigEntities = serviceConfigDAO.getLastServiceConfigsForService(clusterDAO.findByName("c1").getClusterId(), serviceName);
        org.junit.Assert.assertNotNull(serviceConfigEntities);
        org.junit.Assert.assertEquals(3, serviceConfigEntities.size());
        for (org.apache.ambari.server.orm.entities.ServiceConfigEntity sce : serviceConfigEntities) {
            if ((sce.getGroupId() != null) && sce.getGroupId().equals(group2.getGroupId())) {
                org.junit.Assert.assertEquals(sce.getVersion(), java.lang.Long.valueOf(5L));
            }
        }
    }

    @org.junit.Test
    public void testGetLastServiceConfig() throws java.lang.Exception {
        createServiceConfig("HDFS", "admin", 1L, 1L, 1111L, null);
        createServiceConfig("HDFS", "admin", 2L, 2L, 2222L, null);
        createServiceConfig("YARN", "admin", 1L, 3L, 3333L, null);
        java.lang.Long clusterId = clusterDAO.findByName("c1").getClusterId();
        org.apache.ambari.server.orm.entities.ServiceConfigEntity serviceConfigEntity = serviceConfigDAO.getLastServiceConfig(clusterId, "HDFS");
        org.junit.Assert.assertNotNull(serviceConfigEntity);
        org.junit.Assert.assertEquals("c1", serviceConfigEntity.getClusterEntity().getClusterName());
        org.junit.Assert.assertEquals(clusterId, serviceConfigEntity.getClusterEntity().getClusterId());
        org.junit.Assert.assertEquals("HDFS", serviceConfigEntity.getServiceName());
        org.junit.Assert.assertEquals(java.lang.Long.valueOf(2222L), serviceConfigEntity.getCreateTimestamp());
        org.junit.Assert.assertEquals("admin", serviceConfigEntity.getUser());
        org.junit.Assert.assertEquals(java.lang.Long.valueOf(2), serviceConfigEntity.getVersion());
        org.junit.Assert.assertTrue(serviceConfigEntity.getClusterConfigEntities().isEmpty());
        org.junit.Assert.assertNotNull(serviceConfigEntity.getServiceConfigId());
    }

    @org.junit.Test
    public void testGetServiceConfigs() throws java.lang.Exception {
        createServiceConfig("HDFS", "admin", 1L, 1L, 1111L, null);
        createServiceConfig("HDFS", "admin", 2L, 2L, 2222L, null);
        createServiceConfig("YARN", "admin", 1L, 3L, 3333L, null);
        java.lang.Long clusterId = clusterDAO.findByName("c1").getClusterId();
        java.util.List<org.apache.ambari.server.orm.entities.ServiceConfigEntity> serviceConfigEntities = serviceConfigDAO.getServiceConfigs(clusterId);
        org.junit.Assert.assertNotNull(serviceConfigEntities);
        org.junit.Assert.assertEquals(3, serviceConfigEntities.size());
        for (org.apache.ambari.server.orm.entities.ServiceConfigEntity sce : serviceConfigEntities) {
            if ("HDFS".equals(sce.getServiceName()) && (sce.getVersion() == 1)) {
                org.junit.Assert.assertEquals("c1", sce.getClusterEntity().getClusterName());
                org.junit.Assert.assertEquals(clusterId, sce.getClusterEntity().getClusterId());
                org.junit.Assert.assertEquals(java.lang.Long.valueOf(1111L), sce.getCreateTimestamp());
                org.junit.Assert.assertTrue(sce.getClusterConfigEntities().isEmpty());
                org.junit.Assert.assertNotNull(sce.getServiceConfigId());
            } else if ("HDFS".equals(sce.getServiceName()) && (sce.getVersion() == 2)) {
                org.junit.Assert.assertEquals("c1", sce.getClusterEntity().getClusterName());
                org.junit.Assert.assertEquals(clusterId, sce.getClusterEntity().getClusterId());
                org.junit.Assert.assertEquals(java.lang.Long.valueOf(2222L), sce.getCreateTimestamp());
                org.junit.Assert.assertTrue(sce.getClusterConfigEntities().isEmpty());
                org.junit.Assert.assertNotNull(sce.getServiceConfigId());
            } else if ("YARN".equals(sce.getServiceName())) {
                org.junit.Assert.assertEquals("c1", sce.getClusterEntity().getClusterName());
                org.junit.Assert.assertEquals(clusterId, sce.getClusterEntity().getClusterId());
                org.junit.Assert.assertEquals(java.lang.Long.valueOf(3333L), sce.getCreateTimestamp());
                org.junit.Assert.assertEquals(java.lang.Long.valueOf(1), sce.getVersion());
                org.junit.Assert.assertTrue(sce.getClusterConfigEntities().isEmpty());
                org.junit.Assert.assertNotNull(sce.getServiceConfigId());
            } else {
                org.junit.Assert.fail();
            }
            org.junit.Assert.assertEquals("admin", sce.getUser());
        }
    }

    @org.junit.Test
    public void testGetAllServiceConfigs() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.ServiceConfigEntity serviceConfigEntity = null;
        serviceConfigEntity = createServiceConfig("HDFS", "admin", 1L, 1L, 10L, null);
        serviceConfigEntity = createServiceConfig("HDFS", "admin", 2L, 2L, 20L, null);
        serviceConfigEntity = createServiceConfig("HDFS", "admin", 3L, 3L, 30L, null);
        serviceConfigEntity = createServiceConfig("YARN", "admin", 1L, 4L, 40L, null);
        long clusterId = serviceConfigEntity.getClusterId();
        java.util.List<org.apache.ambari.server.orm.entities.ServiceConfigEntity> serviceConfigs = serviceConfigDAO.getServiceConfigsForServiceAndStack(clusterId, org.apache.ambari.server.orm.dao.ServiceConfigDAOTest.HDP_01, "HDFS");
        org.junit.Assert.assertEquals(3, serviceConfigs.size());
        serviceConfigs = serviceConfigDAO.getServiceConfigsForServiceAndStack(clusterId, org.apache.ambari.server.orm.dao.ServiceConfigDAOTest.HDP_01, "YARN");
        org.junit.Assert.assertEquals(1, serviceConfigs.size());
        serviceConfigs = serviceConfigDAO.getServiceConfigsForServiceAndStack(clusterId, org.apache.ambari.server.orm.dao.ServiceConfigDAOTest.HDP_02, "HDFS");
        org.junit.Assert.assertEquals(0, serviceConfigs.size());
    }

    @org.junit.Test
    public void testGetLatestServiceConfigs() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.ServiceConfigEntity serviceConfigEntity = null;
        serviceConfigEntity = createServiceConfig("HDFS", "admin", 1L, 1L, 10L, null);
        serviceConfigEntity = createServiceConfig("HDFS", "admin", 2L, 2L, 20L, null);
        serviceConfigEntity = createServiceConfig("HDFS", "admin", 3L, 3L, 30L, null);
        serviceConfigEntity = createServiceConfig("YARN", "admin", 1L, 4L, 40L, null);
        org.apache.ambari.server.orm.entities.StackEntity stackEntity = stackDAO.find(org.apache.ambari.server.orm.dao.ServiceConfigDAOTest.HDP_02.getStackName(), org.apache.ambari.server.orm.dao.ServiceConfigDAOTest.HDP_02.getStackVersion());
        org.apache.ambari.server.orm.entities.ClusterEntity clusterEntity = serviceConfigEntity.getClusterEntity();
        clusterEntity.setDesiredStack(stackEntity);
        clusterDAO.merge(clusterEntity);
        org.apache.ambari.server.orm.entities.ConfigGroupEntity configGroupEntity1 = new org.apache.ambari.server.orm.entities.ConfigGroupEntity();
        configGroupEntity1.setClusterEntity(clusterEntity);
        configGroupEntity1.setClusterId(clusterEntity.getClusterId());
        configGroupEntity1.setGroupName("group1");
        configGroupEntity1.setDescription("group1_desc");
        configGroupEntity1.setTag("HDFS");
        configGroupEntity1.setServiceName("HDFS");
        configGroupDAO.create(configGroupEntity1);
        org.apache.ambari.server.orm.entities.ConfigGroupEntity group1 = configGroupDAO.findByName("group1");
        createServiceConfigWithGroup("HDFS", "admin", 3L, 8L, 2222L, null, group1.getGroupId());
        serviceConfigEntity = createServiceConfig("HDFS", "admin", 4L, 5L, 50L, null);
        serviceConfigEntity = createServiceConfig("HDFS", "admin", 5L, 6L, 60L, null);
        serviceConfigEntity = createServiceConfig("YARN", "admin", 2L, 7L, 70L, null);
        long clusterId = serviceConfigEntity.getClusterId();
        java.util.List<org.apache.ambari.server.orm.entities.ServiceConfigEntity> serviceConfigs = serviceConfigDAO.getLatestServiceConfigs(clusterId, org.apache.ambari.server.orm.dao.ServiceConfigDAOTest.HDP_01);
        org.junit.Assert.assertEquals(3, serviceConfigs.size());
        configGroupDAO.remove(configGroupEntity1);
        serviceConfigs = serviceConfigDAO.getLatestServiceConfigs(clusterId, org.apache.ambari.server.orm.dao.ServiceConfigDAOTest.HDP_02);
        org.junit.Assert.assertEquals(2, serviceConfigs.size());
    }

    @org.junit.Test
    public void testConfiguration() throws java.lang.Exception {
        initClusterEntities();
        org.apache.ambari.server.orm.entities.ClusterEntity clusterEntity = clusterDAO.findByName("c1");
        org.junit.Assert.assertTrue(!clusterEntity.getClusterConfigEntities().isEmpty());
        org.junit.Assert.assertEquals(5, clusterEntity.getClusterConfigEntities().size());
    }

    @org.junit.Test
    public void testGetLatestClusterConfigsByStack() throws java.lang.Exception {
        initClusterEntities();
        org.apache.ambari.server.orm.entities.ClusterEntity clusterEntity = clusterDAO.findByName("c1");
        java.util.List<org.apache.ambari.server.orm.entities.ClusterConfigEntity> clusterConfigEntities = clusterDAO.getLatestConfigurations(clusterEntity.getClusterId(), org.apache.ambari.server.orm.dao.ServiceConfigDAOTest.HDP_01);
        org.junit.Assert.assertEquals(1, clusterConfigEntities.size());
        org.apache.ambari.server.orm.entities.ClusterConfigEntity entity = clusterConfigEntities.get(0);
        org.junit.Assert.assertEquals("version3", entity.getTag());
        org.junit.Assert.assertEquals("oozie-site", entity.getType());
        org.junit.Assert.assertFalse(entity.isSelected());
        clusterConfigEntities = clusterDAO.getLatestConfigurations(clusterEntity.getClusterId(), org.apache.ambari.server.orm.dao.ServiceConfigDAOTest.HDP_02);
        org.junit.Assert.assertEquals(1, clusterConfigEntities.size());
        entity = clusterConfigEntities.get(0);
        org.junit.Assert.assertEquals("version5", entity.getTag());
        org.junit.Assert.assertEquals("oozie-site", entity.getType());
        org.junit.Assert.assertTrue(entity.isSelected());
    }

    @org.junit.Test
    public void testGetLatestClusterConfigsWithTypes() throws java.lang.Exception {
        initClusterEntities();
        org.apache.ambari.server.orm.entities.ClusterEntity clusterEntity = clusterDAO.findByName("c1");
        java.util.List<org.apache.ambari.server.orm.entities.ClusterConfigEntity> entities = clusterDAO.getLatestConfigurationsWithTypes(clusterEntity.getClusterId(), org.apache.ambari.server.orm.dao.ServiceConfigDAOTest.HDP_01, java.util.Arrays.asList("oozie-site"));
        org.junit.Assert.assertEquals(1, entities.size());
        entities = clusterDAO.getLatestConfigurationsWithTypes(clusterEntity.getClusterId(), org.apache.ambari.server.orm.dao.ServiceConfigDAOTest.HDP_01, java.util.Arrays.asList("no-such-type"));
        org.junit.Assert.assertTrue(entities.isEmpty());
        entities = clusterDAO.getLatestConfigurationsWithTypes(clusterEntity.getClusterId(), org.apache.ambari.server.orm.dao.ServiceConfigDAOTest.HDP_01, java.util.Collections.emptyList());
        org.junit.Assert.assertTrue(entities.isEmpty());
    }

    @org.junit.Test
    public void testGetClusterConfigsByStackCG() throws java.lang.Exception {
        initClusterEntitiesWithConfigGroups();
        org.apache.ambari.server.orm.entities.ClusterEntity clusterEntity = clusterDAO.findByName("c1");
        java.util.List<org.apache.ambari.server.orm.entities.ConfigGroupEntity> configGroupEntities = configGroupDAO.findAllByTag("OOZIE");
        java.lang.Long clusterId = clusterDAO.findByName("c1").getClusterId();
        org.junit.Assert.assertNotNull(configGroupEntities);
        org.apache.ambari.server.orm.entities.ConfigGroupEntity configGroupEntity = configGroupEntities.get(0);
        org.junit.Assert.assertNotNull(configGroupEntity);
        org.junit.Assert.assertEquals("c1", configGroupEntity.getClusterEntity().getClusterName());
        org.junit.Assert.assertEquals(clusterId, configGroupEntity.getClusterEntity().getClusterId());
        org.junit.Assert.assertEquals("oozie_server", configGroupEntity.getGroupName());
        org.junit.Assert.assertEquals("OOZIE", configGroupEntity.getTag());
        org.junit.Assert.assertEquals("oozie server", configGroupEntity.getDescription());
        java.util.List<org.apache.ambari.server.orm.entities.ClusterConfigEntity> clusterConfigEntities = clusterDAO.getEnabledConfigsByStack(clusterEntity.getClusterId(), org.apache.ambari.server.orm.dao.ServiceConfigDAOTest.HDP_01);
        org.junit.Assert.assertEquals(1, clusterConfigEntities.size());
        org.apache.ambari.server.orm.entities.ClusterConfigEntity configEntity = clusterConfigEntities.get(0);
        org.junit.Assert.assertEquals("version2", configEntity.getTag());
        org.junit.Assert.assertEquals("oozie-site", configEntity.getType());
        org.junit.Assert.assertTrue(configEntity.isSelected());
        clusterConfigEntities = clusterDAO.getLatestConfigurations(clusterEntity.getClusterId(), org.apache.ambari.server.orm.dao.ServiceConfigDAOTest.HDP_01);
        configEntity = clusterConfigEntities.get(0);
        org.junit.Assert.assertEquals("version2", configEntity.getTag());
        org.junit.Assert.assertEquals("oozie-site", configEntity.getType());
        org.junit.Assert.assertTrue(configEntity.isSelected());
    }

    @org.junit.Test
    public void testGetEnabledClusterConfigByStack() throws java.lang.Exception {
        org.apache.ambari.server.state.Clusters clusters = injector.getInstance(org.apache.ambari.server.state.Clusters.class);
        clusters.addCluster("c1", org.apache.ambari.server.orm.dao.ServiceConfigDAOTest.HDP_01);
        org.apache.ambari.server.state.Cluster cluster = clusters.getCluster("c1");
        initClusterEntities();
        java.util.Collection<org.apache.ambari.server.orm.entities.ClusterConfigEntity> latestConfigs = clusterDAO.getEnabledConfigsByStack(cluster.getClusterId(), org.apache.ambari.server.orm.dao.ServiceConfigDAOTest.HDP_02);
        org.junit.Assert.assertEquals(1, latestConfigs.size());
        for (org.apache.ambari.server.orm.entities.ClusterConfigEntity e : latestConfigs) {
            org.junit.Assert.assertEquals("version5", e.getTag());
            org.junit.Assert.assertEquals("oozie-site", e.getType());
        }
    }

    @org.junit.Test
    public void testGetLatestClusterConfigByStackCG() throws java.lang.Exception {
        org.apache.ambari.server.state.Clusters clusters = injector.getInstance(org.apache.ambari.server.state.Clusters.class);
        clusters.addCluster("c1", org.apache.ambari.server.orm.dao.ServiceConfigDAOTest.HDP_01);
        org.apache.ambari.server.state.Cluster cluster = clusters.getCluster("c1");
        initClusterEntitiesWithConfigGroups();
        java.util.Collection<org.apache.ambari.server.orm.entities.ClusterConfigEntity> latestConfigs = clusterDAO.getEnabledConfigsByStack(cluster.getClusterId(), org.apache.ambari.server.orm.dao.ServiceConfigDAOTest.HDP_01);
        org.junit.Assert.assertEquals(1, latestConfigs.size());
        for (org.apache.ambari.server.orm.entities.ClusterConfigEntity e : latestConfigs) {
            org.junit.Assert.assertEquals("version2", e.getTag());
            org.junit.Assert.assertEquals("oozie-site", e.getType());
        }
    }

    @org.junit.Test
    public void testGetLastServiceConfigsForServiceWhenAConfigGroupIsDeleted() throws java.lang.Exception {
        org.apache.ambari.server.state.Clusters clusters = injector.getInstance(org.apache.ambari.server.state.Clusters.class);
        clusters.addCluster("c1", org.apache.ambari.server.orm.dao.ServiceConfigDAOTest.HDP_01);
        initClusterEntitiesWithConfigGroups();
        org.apache.ambari.server.orm.entities.ConfigGroupEntity configGroupEntity1 = new org.apache.ambari.server.orm.entities.ConfigGroupEntity();
        org.apache.ambari.server.orm.entities.ClusterEntity clusterEntity = clusterDAO.findByName("c1");
        java.lang.Long clusterId = clusterEntity.getClusterId();
        configGroupEntity1.setClusterEntity(clusterEntity);
        configGroupEntity1.setClusterId(clusterEntity.getClusterId());
        configGroupEntity1.setGroupName("toTestDeleteGroup_OOZIE");
        configGroupEntity1.setDescription("toTestDeleteGroup_OOZIE_DESC");
        configGroupEntity1.setTag("OOZIE");
        configGroupEntity1.setServiceName("OOZIE");
        configGroupDAO.create(configGroupEntity1);
        org.apache.ambari.server.orm.entities.ConfigGroupEntity testDeleteGroup_OOZIE = configGroupDAO.findByName("toTestDeleteGroup_OOZIE");
        createServiceConfigWithGroup("OOZIE", "", 2L, 2L, java.lang.System.currentTimeMillis(), null, testDeleteGroup_OOZIE.getGroupId());
        java.util.Collection<org.apache.ambari.server.orm.entities.ServiceConfigEntity> serviceConfigEntityList = serviceConfigDAO.getLastServiceConfigsForService(clusterId, "OOZIE");
        org.junit.Assert.assertEquals(2, serviceConfigEntityList.size());
        configGroupDAO.remove(configGroupEntity1);
        serviceConfigEntityList = serviceConfigDAO.getLastServiceConfigsForService(clusterId, "OOZIE");
        org.junit.Assert.assertEquals(1, serviceConfigEntityList.size());
    }

    private void initClusterEntities() throws java.lang.Exception {
        java.lang.String userName = "admin";
        org.apache.ambari.server.orm.entities.ServiceConfigEntity oozieServiceConfigEntity = createServiceConfig("OOZIE", userName, 1L, 1L, java.lang.System.currentTimeMillis(), null);
        org.apache.ambari.server.orm.entities.ClusterEntity clusterEntity = oozieServiceConfigEntity.getClusterEntity();
        java.lang.Long clusterId = clusterEntity.getClusterId();
        if (null == clusterId) {
            clusterId = 1L;
            clusterEntity.setClusterId(clusterId);
            clusterEntity = clusterDAO.merge(clusterEntity);
        }
        org.apache.ambari.server.orm.entities.StackEntity stackEntityHDP01 = stackDAO.find(org.apache.ambari.server.orm.dao.ServiceConfigDAOTest.HDP_01.getStackName(), org.apache.ambari.server.orm.dao.ServiceConfigDAOTest.HDP_01.getStackVersion());
        org.apache.ambari.server.orm.entities.StackEntity stackEntityHDP02 = stackDAO.find(org.apache.ambari.server.orm.dao.ServiceConfigDAOTest.HDP_02.getStackName(), org.apache.ambari.server.orm.dao.ServiceConfigDAOTest.HDP_02.getStackVersion());
        java.lang.String oozieSite = "oozie-site";
        int configsToCreate = 5;
        for (int i = 1; i <= configsToCreate; i++) {
            java.lang.Thread.sleep(1);
            org.apache.ambari.server.orm.entities.ClusterConfigEntity entity = new org.apache.ambari.server.orm.entities.ClusterConfigEntity();
            entity.setClusterEntity(clusterEntity);
            entity.setClusterId(clusterEntity.getClusterId());
            entity.setType(oozieSite);
            entity.setVersion(java.lang.Long.valueOf(i));
            entity.setTag("version" + i);
            entity.setTimestamp(new java.util.Date().getTime());
            entity.setSelected(true);
            entity.setSelected(false);
            entity.setStack(stackEntityHDP01);
            if (i >= 4) {
                entity.setStack(stackEntityHDP02);
                if (i == configsToCreate) {
                    entity.setSelected(true);
                }
            }
            entity.setData("");
            clusterDAO.createConfig(entity);
            clusterEntity.getClusterConfigEntities().add(entity);
            clusterDAO.merge(clusterEntity);
        }
    }

    private void initClusterEntitiesWithConfigGroups() throws java.lang.Exception {
        java.lang.String userName = "admin";
        org.apache.ambari.server.orm.entities.ServiceConfigEntity oozieServiceConfigEntity = createServiceConfig("OOZIE", userName, 1L, 1L, java.lang.System.currentTimeMillis(), null);
        org.apache.ambari.server.orm.entities.ClusterEntity clusterEntity = oozieServiceConfigEntity.getClusterEntity();
        java.lang.Long clusterId = clusterEntity.getClusterId();
        if (null == clusterId) {
            clusterId = 1L;
            clusterEntity.setClusterId(clusterId);
            clusterEntity = clusterDAO.merge(clusterEntity);
        }
        org.apache.ambari.server.orm.entities.StackEntity stackEntityHDP01 = stackDAO.find(org.apache.ambari.server.orm.dao.ServiceConfigDAOTest.HDP_01.getStackName(), org.apache.ambari.server.orm.dao.ServiceConfigDAOTest.HDP_01.getStackVersion());
        java.lang.String oozieSite = "oozie-site";
        int count = 2;
        for (int i = 1; i <= count; i++) {
            java.lang.Thread.sleep(1);
            org.apache.ambari.server.orm.entities.ClusterConfigEntity entity = new org.apache.ambari.server.orm.entities.ClusterConfigEntity();
            entity.setClusterEntity(clusterEntity);
            entity.setClusterId(clusterEntity.getClusterId());
            entity.setType(oozieSite);
            entity.setVersion(java.lang.Long.valueOf(i));
            entity.setTag("version" + i);
            entity.setTimestamp(new java.util.Date().getTime());
            entity.setStack(stackEntityHDP01);
            entity.setData("");
            entity.setSelected(false);
            if (i == count) {
                entity.setSelected(true);
            }
            clusterDAO.createConfig(entity);
            clusterEntity.getClusterConfigEntities().add(entity);
            clusterDAO.merge(clusterEntity);
        }
        org.apache.ambari.server.orm.entities.ResourceTypeEntity resourceTypeEntity = resourceTypeDAO.findById(org.apache.ambari.server.security.authorization.ResourceType.CLUSTER.getId());
        if (resourceTypeEntity == null) {
            resourceTypeEntity = new org.apache.ambari.server.orm.entities.ResourceTypeEntity();
            resourceTypeEntity.setId(org.apache.ambari.server.security.authorization.ResourceType.CLUSTER.getId());
            resourceTypeEntity.setName(org.apache.ambari.server.security.authorization.ResourceType.CLUSTER.name());
            resourceTypeEntity = resourceTypeDAO.merge(resourceTypeEntity);
        }
        org.apache.ambari.server.orm.entities.ResourceEntity resourceEntity = new org.apache.ambari.server.orm.entities.ResourceEntity();
        resourceEntity.setResourceType(resourceTypeEntity);
        org.apache.ambari.server.orm.entities.ConfigGroupEntity configGroupEntity = new org.apache.ambari.server.orm.entities.ConfigGroupEntity();
        configGroupEntity.setClusterEntity(clusterEntity);
        configGroupEntity.setClusterId(clusterEntity.getClusterId());
        configGroupEntity.setGroupName("oozie_server");
        configGroupEntity.setDescription("oozie server");
        configGroupEntity.setTag("OOZIE");
        configGroupDAO.create(configGroupEntity);
        org.apache.ambari.server.orm.entities.ClusterConfigEntity configEntityForGroup = new org.apache.ambari.server.orm.entities.ClusterConfigEntity();
        configEntityForGroup.setSelected(false);
        configEntityForGroup.setType("oozie-site");
        configEntityForGroup.setTag("version3");
        configEntityForGroup.setData("someData");
        configEntityForGroup.setAttributes("someAttributes");
        configEntityForGroup.setStack(stackEntityHDP01);
        java.util.List<org.apache.ambari.server.orm.entities.ClusterConfigEntity> configEntitiesForGroup = new java.util.ArrayList<>();
        configEntitiesForGroup.add(configEntityForGroup);
        java.util.List<org.apache.ambari.server.orm.entities.ConfigGroupConfigMappingEntity> configMappingEntities = new java.util.ArrayList<>();
        for (org.apache.ambari.server.orm.entities.ClusterConfigEntity config : configEntitiesForGroup) {
            config.setClusterEntity(clusterEntity);
            config.setClusterId(clusterEntity.getClusterId());
            clusterDAO.createConfig(config);
            java.lang.Thread.sleep(1);
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
}