package org.apache.ambari.server.orm.dao;
public class RequestDAOTest {
    private com.google.inject.Injector injector;

    private org.apache.ambari.server.orm.dao.ClusterDAO clusterDAO;

    private org.apache.ambari.server.orm.dao.StageDAO stageDAO;

    private org.apache.ambari.server.orm.dao.HostRoleCommandDAO hostRoleCommandDAO;

    private org.apache.ambari.server.orm.dao.HostDAO hostDAO;

    private org.apache.ambari.server.orm.dao.RequestDAO requestDAO;

    @org.junit.Before
    public void setup() throws java.lang.Exception {
        injector = com.google.inject.Guice.createInjector(new org.apache.ambari.server.orm.InMemoryDefaultTestModule());
        injector.getInstance(org.apache.ambari.server.orm.GuiceJpaInitializer.class);
        injector.getInstance(org.apache.ambari.server.api.services.AmbariMetaInfo.class);
        clusterDAO = injector.getInstance(org.apache.ambari.server.orm.dao.ClusterDAO.class);
        stageDAO = injector.getInstance(org.apache.ambari.server.orm.dao.StageDAO.class);
        hostRoleCommandDAO = injector.getInstance(org.apache.ambari.server.orm.dao.HostRoleCommandDAO.class);
        hostDAO = injector.getInstance(org.apache.ambari.server.orm.dao.HostDAO.class);
        requestDAO = injector.getInstance(org.apache.ambari.server.orm.dao.RequestDAO.class);
    }

    @org.junit.After
    public void teardown() throws org.apache.ambari.server.AmbariException, java.sql.SQLException {
        org.apache.ambari.server.H2DatabaseCleaner.clearDatabaseAndStopPersistenceService(injector);
    }

    @org.junit.Test
    public void testFindAll() throws java.lang.Exception {
        org.apache.ambari.server.orm.dao.RequestDAO dao = injector.getInstance(org.apache.ambari.server.orm.dao.RequestDAO.class);
        java.util.Set<java.lang.Long> set = java.util.Collections.emptySet();
        java.util.List<org.apache.ambari.server.orm.entities.RequestEntity> list = dao.findByPks(set);
        org.junit.Assert.assertEquals(0, list.size());
    }

    @org.junit.Test
    public void testFindAllRequestIds() throws java.lang.Exception {
        org.apache.ambari.server.orm.dao.RequestDAO dao = injector.getInstance(org.apache.ambari.server.orm.dao.RequestDAO.class);
        java.util.List<java.lang.Long> list = dao.findAllRequestIds(0, true);
        org.junit.Assert.assertEquals(0, list.size());
    }

    @org.junit.Test
    public void testCalculatedStatus() throws java.lang.Exception {
        createGraph();
        org.apache.ambari.server.orm.entities.RequestEntity requestEntity = requestDAO.findByPK(100L);
        org.apache.ambari.server.controller.internal.CalculatedStatus calc1 = org.apache.ambari.server.controller.internal.CalculatedStatus.statusFromStageEntities(requestEntity.getStages());
        java.util.Map<java.lang.Long, org.apache.ambari.server.orm.dao.HostRoleCommandStatusSummaryDTO> map = hostRoleCommandDAO.findAggregateCounts(100L);
        org.apache.ambari.server.controller.internal.CalculatedStatus calc2 = org.apache.ambari.server.controller.internal.CalculatedStatus.statusFromStageSummary(map, map.keySet());
        org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.HostRoleStatus.IN_PROGRESS, calc1.getStatus());
        org.junit.Assert.assertEquals(calc1.getStatus(), calc2.getStatus());
        org.junit.Assert.assertEquals(calc1.getPercent(), calc2.getPercent(), 0.01);
        java.util.Set<java.lang.Long> group = new java.util.HashSet<>();
        group.add(2L);
        group.add(3L);
        group.add(4L);
        java.util.List<org.apache.ambari.server.orm.entities.StageEntity> stages = new java.util.ArrayList<>();
        org.apache.ambari.server.orm.entities.StageEntityPK primaryKey = new org.apache.ambari.server.orm.entities.StageEntityPK();
        primaryKey.setRequestId(requestEntity.getRequestId());
        primaryKey.setStageId(2L);
        org.apache.ambari.server.orm.entities.StageEntity stage = stageDAO.findByPK(primaryKey);
        org.junit.Assert.assertNotNull(stage);
        stages.add(stage);
        primaryKey.setStageId(3L);
        stage = stageDAO.findByPK(primaryKey);
        org.junit.Assert.assertNotNull(stage);
        stages.add(stage);
        primaryKey.setStageId(4L);
        stage = stageDAO.findByPK(primaryKey);
        org.junit.Assert.assertNotNull(stage);
        stages.add(stage);
        org.apache.ambari.server.controller.internal.CalculatedStatus calc3 = org.apache.ambari.server.controller.internal.CalculatedStatus.statusFromStageEntities(stages);
        org.apache.ambari.server.controller.internal.CalculatedStatus calc4 = org.apache.ambari.server.controller.internal.CalculatedStatus.statusFromStageSummary(map, group);
        org.junit.Assert.assertEquals(100.0, calc3.getPercent(), 0.01);
        org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, calc3.getStatus());
        org.junit.Assert.assertEquals(calc3.getPercent(), calc4.getPercent(), 0.01);
        org.junit.Assert.assertEquals(calc3.getStatus(), calc4.getStatus());
    }

    private void createGraph() {
        org.apache.ambari.server.orm.OrmTestHelper helper = injector.getInstance(org.apache.ambari.server.orm.OrmTestHelper.class);
        helper.createDefaultData();
        java.lang.Long requestId = java.lang.Long.valueOf(100L);
        java.lang.String hostName = "test_host1";
        org.apache.ambari.server.orm.entities.ResourceTypeEntity resourceTypeEntity = new org.apache.ambari.server.orm.entities.ResourceTypeEntity();
        resourceTypeEntity.setId(org.apache.ambari.server.security.authorization.ResourceType.CLUSTER.getId());
        resourceTypeEntity.setName(org.apache.ambari.server.security.authorization.ResourceType.CLUSTER.name());
        org.apache.ambari.server.orm.entities.ResourceEntity resourceEntity = new org.apache.ambari.server.orm.entities.ResourceEntity();
        resourceEntity.setResourceType(resourceTypeEntity);
        org.apache.ambari.server.orm.entities.ClusterEntity clusterEntity = clusterDAO.findByName("test_cluster1");
        org.apache.ambari.server.orm.entities.RequestEntity requestEntity = new org.apache.ambari.server.orm.entities.RequestEntity();
        requestEntity.setRequestId(requestId);
        requestEntity.setClusterId(clusterEntity.getClusterId());
        requestEntity.setStages(new java.util.ArrayList<>());
        requestDAO.create(requestEntity);
        org.apache.ambari.server.orm.entities.HostEntity host = hostDAO.findByName(hostName);
        host.setHostRoleCommandEntities(new java.util.ArrayList<>());
        long stageId = 1L;
        stageId = createStages(stageId, 3, host, requestEntity, org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, false);
        stageId = createStages(stageId, 1, host, requestEntity, org.apache.ambari.server.actionmanager.HostRoleStatus.FAILED, true);
        stageId = createStages(stageId, 1, host, requestEntity, org.apache.ambari.server.actionmanager.HostRoleStatus.IN_PROGRESS, false);
        stageId = createStages(stageId, 3, host, requestEntity, org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING, false);
        requestDAO.merge(requestEntity);
    }

    private long createStages(long startStageId, int count, org.apache.ambari.server.orm.entities.HostEntity he, org.apache.ambari.server.orm.entities.RequestEntity re, org.apache.ambari.server.actionmanager.HostRoleStatus status, boolean skipStage) {
        long stageId = startStageId;
        org.apache.ambari.server.orm.entities.ClusterEntity clusterEntity = clusterDAO.findByName("test_cluster1");
        for (int i = 0; i < count; i++) {
            org.apache.ambari.server.orm.entities.StageEntity stageEntity = new org.apache.ambari.server.orm.entities.StageEntity();
            stageEntity.setClusterId(clusterEntity.getClusterId());
            stageEntity.setRequest(re);
            stageEntity.setStageId(stageId);
            stageEntity.setHostRoleCommands(new java.util.ArrayList<>());
            stageEntity.setSkippable(skipStage);
            stageDAO.create(stageEntity);
            re.getStages().add(stageEntity);
            org.apache.ambari.server.orm.entities.HostRoleCommandEntity commandEntity = new org.apache.ambari.server.orm.entities.HostRoleCommandEntity();
            commandEntity.setRequestId(re.getRequestId());
            commandEntity.setStageId(stageId);
            commandEntity.setRoleCommand(org.apache.ambari.server.RoleCommand.INSTALL);
            commandEntity.setStatus(status);
            commandEntity.setRole(org.apache.ambari.server.Role.DATANODE);
            commandEntity.setHostEntity(he);
            commandEntity.setStage(stageEntity);
            hostRoleCommandDAO.create(commandEntity);
            he.getHostRoleCommandEntities().add(commandEntity);
            he = hostDAO.merge(he);
            stageEntity.getHostRoleCommands().add(commandEntity);
            stageDAO.merge(stageEntity);
            stageId++;
        }
        return stageId;
    }
}