package org.apache.ambari.server.state.services;
public class RetryUpgradeActionServiceTest {
    private com.google.inject.Injector injector;

    private org.apache.ambari.server.state.Clusters clusters;

    private org.apache.ambari.server.orm.dao.RepositoryVersionDAO repoVersionDAO;

    private org.apache.ambari.server.orm.dao.UpgradeDAO upgradeDAO;

    private org.apache.ambari.server.orm.dao.RequestDAO requestDAO;

    private org.apache.ambari.server.orm.dao.StageDAO stageDAO;

    private org.apache.ambari.server.orm.dao.HostRoleCommandDAO hostRoleCommandDAO;

    private org.apache.ambari.server.orm.OrmTestHelper helper;

    java.lang.String clusterName = "c1";

    org.apache.ambari.server.state.Cluster cluster;

    org.apache.ambari.server.state.StackId stack220 = new org.apache.ambari.server.state.StackId("HDP-2.2.0");

    org.apache.ambari.server.orm.entities.StackEntity stackEntity220;

    java.lang.Long upgradeRequestId = 1L;

    java.lang.Long stageId = 1L;

    @org.junit.Before
    public void before() throws java.lang.Exception {
        injector = com.google.inject.Guice.createInjector(new org.apache.ambari.server.orm.InMemoryDefaultTestModule());
        injector.getInstance(org.apache.ambari.server.orm.GuiceJpaInitializer.class);
        clusters = injector.getInstance(org.apache.ambari.server.state.Clusters.class);
        repoVersionDAO = injector.getInstance(org.apache.ambari.server.orm.dao.RepositoryVersionDAO.class);
        upgradeDAO = injector.getInstance(org.apache.ambari.server.orm.dao.UpgradeDAO.class);
        requestDAO = injector.getInstance(org.apache.ambari.server.orm.dao.RequestDAO.class);
        stageDAO = injector.getInstance(org.apache.ambari.server.orm.dao.StageDAO.class);
        hostRoleCommandDAO = injector.getInstance(org.apache.ambari.server.orm.dao.HostRoleCommandDAO.class);
        helper = injector.getInstance(org.apache.ambari.server.orm.OrmTestHelper.class);
        stackEntity220 = helper.createStack(stack220);
    }

    @org.junit.After
    public void teardown() throws org.apache.ambari.server.AmbariException, java.sql.SQLException {
        org.apache.ambari.server.H2DatabaseCleaner.clearDatabaseAndStopPersistenceService(injector);
    }

    @org.junit.Test
    public void test() throws java.lang.Exception {
        int timeoutMins = 1;
        org.apache.ambari.server.state.services.RetryUpgradeActionService service = injector.getInstance(org.apache.ambari.server.state.services.RetryUpgradeActionService.class);
        service.startUp();
        service.runOneIteration();
        createCluster();
        service.setMaxTimeout(timeoutMins);
        service.runOneIteration();
        prepareUpgrade();
        service.runOneIteration();
        java.util.List<org.apache.ambari.server.orm.entities.HostRoleCommandEntity> commands = hostRoleCommandDAO.findAll();
        org.junit.Assert.assertTrue(!commands.isEmpty());
        for (org.apache.ambari.server.orm.entities.HostRoleCommandEntity hrc : commands) {
            if (hrc.getStatus() == org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING) {
                org.junit.Assert.fail("Did not expect any HostRoleCommands to be PENDING");
            }
        }
        org.apache.ambari.server.orm.entities.StageEntityPK primaryKey = new org.apache.ambari.server.orm.entities.StageEntityPK();
        primaryKey.setRequestId(upgradeRequestId);
        primaryKey.setStageId(stageId);
        org.apache.ambari.server.orm.entities.StageEntity stageEntity = stageDAO.findByPK(primaryKey);
        org.apache.ambari.server.orm.entities.HostRoleCommandEntity hrc2 = new org.apache.ambari.server.orm.entities.HostRoleCommandEntity();
        hrc2.setStage(stageEntity);
        hrc2.setStatus(org.apache.ambari.server.actionmanager.HostRoleStatus.HOLDING_FAILED);
        hrc2.setRole(org.apache.ambari.server.Role.ZOOKEEPER_SERVER);
        hrc2.setRoleCommand(org.apache.ambari.server.RoleCommand.RESTART);
        hrc2.setRetryAllowed(false);
        hrc2.setAutoSkipOnFailure(false);
        stageEntity.getHostRoleCommands().add(hrc2);
        hostRoleCommandDAO.create(hrc2);
        stageDAO.merge(stageEntity);
        service.runOneIteration();
        commands = hostRoleCommandDAO.findAll();
        org.junit.Assert.assertTrue((!commands.isEmpty()) && (commands.size() == 2));
        for (org.apache.ambari.server.orm.entities.HostRoleCommandEntity hrc : commands) {
            if (hrc.getStatus() == org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING) {
                org.junit.Assert.fail("Did not expect any HostRoleCommands to be PENDING");
            }
        }
        long now = java.lang.System.currentTimeMillis();
        hrc2.setRetryAllowed(true);
        hrc2.setOriginalStartTime(now);
        hostRoleCommandDAO.merge(hrc2);
        service.runOneIteration();
        org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING, hostRoleCommandDAO.findByPK(hrc2.getTaskId()).getStatus());
        hrc2.setStatus(org.apache.ambari.server.actionmanager.HostRoleStatus.HOLDING_TIMEDOUT);
        hrc2.setRetryAllowed(true);
        hrc2.setOriginalStartTime(-1L);
        hrc2.setStartTime(-1L);
        hrc2.setLastAttemptTime(-1L);
        hrc2.setEndTime(-1L);
        hrc2.setAttemptCount(((short) (0)));
        hostRoleCommandDAO.merge(hrc2);
        service.runOneIteration();
        org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING, hostRoleCommandDAO.findByPK(hrc2.getTaskId()).getStatus());
        now = java.lang.System.currentTimeMillis();
        hrc2.setOriginalStartTime((now - (timeoutMins * 60000)) - 1);
        hrc2.setStatus(org.apache.ambari.server.actionmanager.HostRoleStatus.HOLDING_FAILED);
        hostRoleCommandDAO.merge(hrc2);
        service.runOneIteration();
        org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.HostRoleStatus.HOLDING_FAILED, hostRoleCommandDAO.findByPK(hrc2.getTaskId()).getStatus());
        now = java.lang.System.currentTimeMillis();
        hrc2.setOriginalStartTime(now);
        hrc2.setStatus(org.apache.ambari.server.actionmanager.HostRoleStatus.HOLDING_FAILED);
        hrc2.setCustomCommandName("org.apache.ambari.server.serveraction.upgrades.FinalizeUpgradeAction");
        hostRoleCommandDAO.merge(hrc2);
        service.runOneIteration();
        org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.HostRoleStatus.HOLDING_FAILED, hostRoleCommandDAO.findByPK(hrc2.getTaskId()).getStatus());
    }

    private void createCluster() throws org.apache.ambari.server.AmbariException {
        clusters.addCluster(clusterName, stack220);
        cluster = clusters.getCluster("c1");
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repoVersionEntity = new org.apache.ambari.server.orm.entities.RepositoryVersionEntity();
        repoVersionEntity.setDisplayName("Initial Version");
        repoVersionEntity.addRepoOsEntities(new java.util.ArrayList<>());
        repoVersionEntity.setStack(stackEntity220);
        repoVersionEntity.setVersion("2.2.0.0");
        repoVersionDAO.create(repoVersionEntity);
        helper.getOrCreateRepositoryVersion(stack220, stack220.getStackVersion());
    }

    private void prepareUpgrade() throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repoVersionEntity = new org.apache.ambari.server.orm.entities.RepositoryVersionEntity();
        repoVersionEntity.setDisplayName("Version to Upgrade To");
        repoVersionEntity.addRepoOsEntities(new java.util.ArrayList<>());
        repoVersionEntity.setStack(stackEntity220);
        repoVersionEntity.setVersion("2.2.0.1");
        repoVersionDAO.create(repoVersionEntity);
        helper.getOrCreateRepositoryVersion(stack220, stack220.getStackVersion());
        org.apache.ambari.server.orm.entities.RequestEntity requestEntity = new org.apache.ambari.server.orm.entities.RequestEntity();
        requestEntity.setRequestId(upgradeRequestId);
        requestEntity.setClusterId(cluster.getClusterId());
        requestDAO.create(requestEntity);
        org.apache.ambari.server.orm.entities.StageEntity stageEntity = new org.apache.ambari.server.orm.entities.StageEntity();
        stageEntity.setRequest(requestEntity);
        stageEntity.setClusterId(cluster.getClusterId());
        stageEntity.setRequestId(upgradeRequestId);
        stageEntity.setStageId(stageId);
        requestEntity.setStages(java.util.Collections.singletonList(stageEntity));
        stageDAO.create(stageEntity);
        requestDAO.merge(requestEntity);
        org.apache.ambari.server.orm.entities.UpgradeEntity upgrade = new org.apache.ambari.server.orm.entities.UpgradeEntity();
        upgrade.setId(1L);
        upgrade.setRequestEntity(requestEntity);
        upgrade.setClusterId(cluster.getClusterId());
        upgrade.setUpgradePackage("some-name");
        upgrade.setUpgradePackStackId(new org.apache.ambari.server.state.StackId(((java.lang.String) (null))));
        upgrade.setUpgradeType(org.apache.ambari.spi.upgrade.UpgradeType.ROLLING);
        upgrade.setDirection(org.apache.ambari.server.stack.upgrade.Direction.UPGRADE);
        upgrade.setRepositoryVersion(repoVersionEntity);
        upgradeDAO.create(upgrade);
        cluster.setUpgradeEntity(upgrade);
        org.apache.ambari.server.orm.entities.HostRoleCommandEntity hrc1 = new org.apache.ambari.server.orm.entities.HostRoleCommandEntity();
        hrc1.setStage(stageEntity);
        hrc1.setStatus(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED);
        hrc1.setRole(org.apache.ambari.server.Role.ZOOKEEPER_SERVER);
        hrc1.setRoleCommand(org.apache.ambari.server.RoleCommand.RESTART);
        stageEntity.setHostRoleCommands(new java.util.ArrayList<>());
        stageEntity.getHostRoleCommands().add(hrc1);
        hostRoleCommandDAO.create(hrc1);
        stageDAO.merge(stageEntity);
    }
}