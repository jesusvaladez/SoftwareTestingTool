package org.apache.ambari.server.orm.dao;
import javax.persistence.EntityManager;
public class HostRoleCommandDAOTest {
    private com.google.inject.Injector m_injector;

    private org.apache.ambari.server.orm.dao.ClusterDAO m_clusterDAO;

    private org.apache.ambari.server.orm.dao.StageDAO m_stageDAO;

    private org.apache.ambari.server.orm.dao.HostRoleCommandDAO m_hostRoleCommandDAO;

    private org.apache.ambari.server.orm.dao.HostDAO m_hostDAO;

    private org.apache.ambari.server.orm.dao.RequestDAO m_requestDAO;

    @org.junit.Before
    public void setup() throws java.lang.Exception {
        m_injector = com.google.inject.Guice.createInjector(new org.apache.ambari.server.orm.InMemoryDefaultTestModule());
        m_injector.getInstance(org.apache.ambari.server.orm.GuiceJpaInitializer.class);
        m_injector.getInstance(org.apache.ambari.server.api.services.AmbariMetaInfo.class);
        m_clusterDAO = m_injector.getInstance(org.apache.ambari.server.orm.dao.ClusterDAO.class);
        m_stageDAO = m_injector.getInstance(org.apache.ambari.server.orm.dao.StageDAO.class);
        m_hostRoleCommandDAO = m_injector.getInstance(org.apache.ambari.server.orm.dao.HostRoleCommandDAO.class);
        m_hostDAO = m_injector.getInstance(org.apache.ambari.server.orm.dao.HostDAO.class);
        m_requestDAO = m_injector.getInstance(org.apache.ambari.server.orm.dao.RequestDAO.class);
    }

    @org.junit.After
    public void teardown() throws org.apache.ambari.server.AmbariException, java.sql.SQLException {
        org.apache.ambari.server.H2DatabaseCleaner.clearDatabase(m_injector.getProvider(javax.persistence.EntityManager.class).get());
    }

    @org.junit.Test
    public void testFindTasksBetweenStages() {
        org.apache.ambari.server.orm.OrmTestHelper helper = m_injector.getInstance(org.apache.ambari.server.orm.OrmTestHelper.class);
        helper.createDefaultData();
        java.lang.Long requestId = java.lang.Long.valueOf(100L);
        org.apache.ambari.server.orm.entities.ClusterEntity clusterEntity = m_clusterDAO.findByName("test_cluster1");
        org.apache.ambari.server.orm.entities.RequestEntity requestEntity = new org.apache.ambari.server.orm.entities.RequestEntity();
        requestEntity.setRequestId(requestId);
        requestEntity.setClusterId(clusterEntity.getClusterId());
        requestEntity.setStages(new java.util.ArrayList<>());
        m_requestDAO.create(requestEntity);
        java.util.concurrent.atomic.AtomicLong stageId = new java.util.concurrent.atomic.AtomicLong(1);
        org.apache.ambari.server.orm.entities.HostEntity host = m_hostDAO.findByName("test_host1");
        host.setHostRoleCommandEntities(new java.util.ArrayList<>());
        createStage(stageId.getAndIncrement(), 3, host, requestEntity, org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED);
        createStage(stageId.getAndIncrement(), 2, host, requestEntity, org.apache.ambari.server.actionmanager.HostRoleStatus.SKIPPED_FAILED);
        createStage(stageId.getAndIncrement(), 1, host, requestEntity, org.apache.ambari.server.actionmanager.HostRoleStatus.ABORTED);
        java.util.List<org.apache.ambari.server.orm.entities.HostRoleCommandEntity> tasks = m_hostRoleCommandDAO.findByStatusBetweenStages(requestId, org.apache.ambari.server.actionmanager.HostRoleStatus.SKIPPED_FAILED, 1, 3);
        junit.framework.Assert.assertEquals(2, tasks.size());
        tasks = m_hostRoleCommandDAO.findByStatusBetweenStages(requestId, org.apache.ambari.server.actionmanager.HostRoleStatus.SKIPPED_FAILED, 1, 1);
        junit.framework.Assert.assertEquals(0, tasks.size());
    }

    @org.junit.Test
    public void testAutoSkipSupport() {
        org.apache.ambari.server.orm.OrmTestHelper helper = m_injector.getInstance(org.apache.ambari.server.orm.OrmTestHelper.class);
        helper.createDefaultData();
        java.lang.Long requestId = java.lang.Long.valueOf(100L);
        org.apache.ambari.server.orm.entities.ClusterEntity clusterEntity = m_clusterDAO.findByName("test_cluster1");
        org.apache.ambari.server.orm.entities.RequestEntity requestEntity = new org.apache.ambari.server.orm.entities.RequestEntity();
        requestEntity.setRequestId(requestId);
        requestEntity.setClusterId(clusterEntity.getClusterId());
        requestEntity.setStages(new java.util.ArrayList<>());
        m_requestDAO.create(requestEntity);
        java.util.concurrent.atomic.AtomicLong stageId = new java.util.concurrent.atomic.AtomicLong(1);
        org.apache.ambari.server.orm.entities.HostEntity host = m_hostDAO.findByName("test_host1");
        host.setHostRoleCommandEntities(new java.util.ArrayList<>());
        long stageIdAutoSkipAll = stageId.getAndIncrement();
        createStage(stageIdAutoSkipAll, 3, host, requestEntity, org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING, true, true, true);
        java.util.List<org.apache.ambari.server.orm.entities.HostRoleCommandEntity> tasks = m_hostRoleCommandDAO.findByRequest(requestId);
        for (org.apache.ambari.server.orm.entities.HostRoleCommandEntity task : tasks) {
            junit.framework.Assert.assertTrue(task.isFailureAutoSkipped());
        }
        long stageIdSkippableButNoAutoSkip = stageId.getAndIncrement();
        createStage(stageIdSkippableButNoAutoSkip, 3, host, requestEntity, org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING, true, false, true);
        tasks = m_hostRoleCommandDAO.findByRequest(requestId);
        for (org.apache.ambari.server.orm.entities.HostRoleCommandEntity task : tasks) {
            org.apache.ambari.server.orm.entities.StageEntity stage = task.getStage();
            if (stage.getStageId() == stageIdAutoSkipAll) {
                junit.framework.Assert.assertTrue(task.isFailureAutoSkipped());
            } else if (stage.getStageId() == stageIdSkippableButNoAutoSkip) {
                junit.framework.Assert.assertFalse(task.isFailureAutoSkipped());
            }
        }
        m_hostRoleCommandDAO.updateAutomaticSkipOnFailure(requestId, false, false);
        tasks = m_hostRoleCommandDAO.findByRequest(requestId);
        for (org.apache.ambari.server.orm.entities.HostRoleCommandEntity task : tasks) {
            junit.framework.Assert.assertFalse(task.isFailureAutoSkipped());
        }
    }

    private void createStage(long startStageId, int count, org.apache.ambari.server.orm.entities.HostEntity hostEntity, org.apache.ambari.server.orm.entities.RequestEntity requestEntity, org.apache.ambari.server.actionmanager.HostRoleStatus status) {
        createStage(startStageId, count, hostEntity, requestEntity, status, false, false, false);
    }

    private void createStage(long startStageId, int count, org.apache.ambari.server.orm.entities.HostEntity hostEntity, org.apache.ambari.server.orm.entities.RequestEntity requestEntity, org.apache.ambari.server.actionmanager.HostRoleStatus status, boolean skipStage, boolean supportsAutoSkipOnFailure, boolean autoSkipFailedCommandsInStage) {
        long stageId = startStageId;
        org.apache.ambari.server.orm.entities.ClusterEntity clusterEntity = m_clusterDAO.findByName("test_cluster1");
        org.apache.ambari.server.orm.entities.StageEntity stageEntity = new org.apache.ambari.server.orm.entities.StageEntity();
        stageEntity.setClusterId(clusterEntity.getClusterId());
        stageEntity.setRequest(requestEntity);
        stageEntity.setStageId(stageId);
        stageEntity.setHostRoleCommands(new java.util.ArrayList<>());
        stageEntity.setSkippable(skipStage);
        stageEntity.setAutoSkipFailureSupported(supportsAutoSkipOnFailure);
        m_stageDAO.create(stageEntity);
        requestEntity.getStages().add(stageEntity);
        for (int i = 0; i < count; i++) {
            org.apache.ambari.server.orm.entities.HostRoleCommandEntity commandEntity = new org.apache.ambari.server.orm.entities.HostRoleCommandEntity();
            commandEntity.setRequestId(requestEntity.getRequestId());
            commandEntity.setStageId(stageId);
            commandEntity.setRoleCommand(org.apache.ambari.server.RoleCommand.INSTALL);
            commandEntity.setStatus(status);
            commandEntity.setRole(org.apache.ambari.server.Role.DATANODE);
            commandEntity.setHostEntity(hostEntity);
            commandEntity.setStage(stageEntity);
            commandEntity.setAutoSkipOnFailure((autoSkipFailedCommandsInStage && skipStage) && supportsAutoSkipOnFailure);
            m_hostRoleCommandDAO.create(commandEntity);
            hostEntity.getHostRoleCommandEntities().add(commandEntity);
            hostEntity = m_hostDAO.merge(hostEntity);
            stageEntity.getHostRoleCommands().add(commandEntity);
            m_stageDAO.merge(stageEntity);
        }
    }
}