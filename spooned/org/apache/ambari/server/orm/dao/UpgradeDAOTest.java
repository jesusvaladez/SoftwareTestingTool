package org.apache.ambari.server.orm.dao;
public class UpgradeDAOTest {
    private com.google.inject.Injector injector;

    private java.lang.Long clusterId;

    private org.apache.ambari.server.orm.dao.UpgradeDAO dao;

    private org.apache.ambari.server.orm.dao.RequestDAO requestDAO;

    private org.apache.ambari.server.orm.OrmTestHelper helper;

    org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion2200;

    org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion2500;

    org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion2511;

    @org.junit.Before
    public void setup() throws java.lang.Exception {
        injector = com.google.inject.Guice.createInjector(new org.apache.ambari.server.orm.InMemoryDefaultTestModule());
        org.apache.ambari.server.H2DatabaseCleaner.resetSequences(injector);
        injector.getInstance(org.apache.ambari.server.orm.GuiceJpaInitializer.class);
        dao = injector.getInstance(org.apache.ambari.server.orm.dao.UpgradeDAO.class);
        requestDAO = injector.getInstance(org.apache.ambari.server.orm.dao.RequestDAO.class);
        helper = injector.getInstance(org.apache.ambari.server.orm.OrmTestHelper.class);
        clusterId = helper.createCluster();
        org.apache.ambari.server.orm.entities.RequestEntity requestEntity = new org.apache.ambari.server.orm.entities.RequestEntity();
        requestEntity.setRequestId(99L);
        requestEntity.setClusterId(clusterId.longValue());
        requestEntity.setStatus(org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING);
        requestEntity.setStages(new java.util.ArrayList<>());
        requestDAO.create(requestEntity);
        repositoryVersion2200 = helper.getOrCreateRepositoryVersion(new org.apache.ambari.server.state.StackId("HDP", "2.2.0"), "2.2.0.0-1234");
        repositoryVersion2500 = helper.getOrCreateRepositoryVersion(new org.apache.ambari.server.state.StackId("HDP", "2.5.0"), "2.5.0.0-4567");
        repositoryVersion2511 = helper.getOrCreateRepositoryVersion(new org.apache.ambari.server.state.StackId("HDP", "2.5.0"), "2.5.1.1-4567");
        org.apache.ambari.server.orm.entities.UpgradeEntity entity = new org.apache.ambari.server.orm.entities.UpgradeEntity();
        entity.setClusterId(clusterId.longValue());
        entity.setRequestEntity(requestEntity);
        entity.setRepositoryVersion(repositoryVersion2200);
        entity.setUpgradeType(org.apache.ambari.spi.upgrade.UpgradeType.ROLLING);
        entity.setUpgradePackage("test-upgrade");
        entity.setUpgradePackStackId(new org.apache.ambari.server.state.StackId(((java.lang.String) (null))));
        entity.setDowngradeAllowed(true);
        org.apache.ambari.server.orm.entities.UpgradeGroupEntity group = new org.apache.ambari.server.orm.entities.UpgradeGroupEntity();
        group.setName("group_name");
        group.setTitle("group title");
        java.util.List<org.apache.ambari.server.orm.entities.UpgradeItemEntity> items = new java.util.ArrayList<>();
        org.apache.ambari.server.orm.entities.UpgradeItemEntity item = new org.apache.ambari.server.orm.entities.UpgradeItemEntity();
        item.setState(org.apache.ambari.server.state.UpgradeState.IN_PROGRESS);
        item.setStageId(java.lang.Long.valueOf(1L));
        items.add(item);
        item = new org.apache.ambari.server.orm.entities.UpgradeItemEntity();
        item.setState(org.apache.ambari.server.state.UpgradeState.COMPLETE);
        item.setStageId(java.lang.Long.valueOf(1L));
        items.add(item);
        group.setItems(items);
        entity.setUpgradeGroups(java.util.Collections.singletonList(group));
        dao.create(entity);
    }

    @org.junit.After
    public void teardown() throws org.apache.ambari.server.AmbariException, java.sql.SQLException {
        org.apache.ambari.server.H2DatabaseCleaner.clearDatabaseAndStopPersistenceService(injector);
        injector = null;
    }

    @org.junit.Test
    public void testFindForCluster() throws java.lang.Exception {
        java.util.List<org.apache.ambari.server.orm.entities.UpgradeEntity> items = dao.findUpgrades(clusterId.longValue());
        org.junit.Assert.assertEquals(1, items.size());
    }

    @org.junit.Test
    public void testFindUpgrade() throws java.lang.Exception {
        java.util.List<org.apache.ambari.server.orm.entities.UpgradeEntity> items = dao.findUpgrades(clusterId.longValue());
        org.junit.Assert.assertTrue(items.size() > 0);
        org.apache.ambari.server.orm.entities.UpgradeEntity entity = dao.findUpgrade(items.get(0).getId().longValue());
        org.junit.Assert.assertNotNull(entity);
        org.junit.Assert.assertEquals(1, entity.getUpgradeGroups().size());
        org.apache.ambari.server.orm.entities.UpgradeGroupEntity group = dao.findUpgradeGroup(entity.getUpgradeGroups().get(0).getId().longValue());
        org.junit.Assert.assertNotNull(group);
        org.junit.Assert.assertNotSame(entity.getUpgradeGroups().get(0), group);
        org.junit.Assert.assertEquals("group_name", group.getName());
        org.junit.Assert.assertEquals("group title", group.getTitle());
    }

    @org.junit.Test
    public void testFindLastUpgradeForCluster() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.RequestEntity requestEntity = new org.apache.ambari.server.orm.entities.RequestEntity();
        requestEntity.setRequestId(1L);
        requestEntity.setClusterId(clusterId.longValue());
        requestEntity.setStatus(org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING);
        requestEntity.setStages(new java.util.ArrayList<>());
        requestDAO.create(requestEntity);
        org.apache.ambari.server.orm.entities.UpgradeEntity entity1 = new org.apache.ambari.server.orm.entities.UpgradeEntity();
        entity1.setId(11L);
        entity1.setClusterId(clusterId.longValue());
        entity1.setDirection(org.apache.ambari.server.stack.upgrade.Direction.UPGRADE);
        entity1.setRequestEntity(requestEntity);
        entity1.setRepositoryVersion(repositoryVersion2500);
        entity1.setUpgradeType(org.apache.ambari.spi.upgrade.UpgradeType.ROLLING);
        entity1.setUpgradePackage("test-upgrade");
        entity1.setUpgradePackStackId(new org.apache.ambari.server.state.StackId(((java.lang.String) (null))));
        entity1.setDowngradeAllowed(true);
        dao.create(entity1);
        org.apache.ambari.server.orm.entities.UpgradeEntity entity2 = new org.apache.ambari.server.orm.entities.UpgradeEntity();
        entity2.setId(22L);
        entity2.setClusterId(clusterId.longValue());
        entity2.setDirection(org.apache.ambari.server.stack.upgrade.Direction.DOWNGRADE);
        entity2.setRequestEntity(requestEntity);
        entity2.setRepositoryVersion(repositoryVersion2200);
        entity2.setUpgradeType(org.apache.ambari.spi.upgrade.UpgradeType.ROLLING);
        entity2.setUpgradePackage("test-upgrade");
        entity2.setUpgradePackStackId(new org.apache.ambari.server.state.StackId(((java.lang.String) (null))));
        entity2.setDowngradeAllowed(true);
        dao.create(entity2);
        org.apache.ambari.server.orm.entities.UpgradeEntity entity3 = new org.apache.ambari.server.orm.entities.UpgradeEntity();
        entity3.setId(33L);
        entity3.setClusterId(clusterId.longValue());
        entity3.setDirection(org.apache.ambari.server.stack.upgrade.Direction.UPGRADE);
        entity3.setRequestEntity(requestEntity);
        entity3.setRepositoryVersion(repositoryVersion2511);
        entity3.setUpgradeType(org.apache.ambari.spi.upgrade.UpgradeType.ROLLING);
        entity3.setUpgradePackage("test-upgrade");
        entity3.setUpgradePackStackId(new org.apache.ambari.server.state.StackId(((java.lang.String) (null))));
        entity3.setDowngradeAllowed(true);
        dao.create(entity3);
        org.apache.ambari.server.orm.entities.UpgradeEntity lastUpgradeForCluster = dao.findLastUpgradeForCluster(clusterId.longValue(), org.apache.ambari.server.stack.upgrade.Direction.UPGRADE);
        org.junit.Assert.assertNotNull(lastUpgradeForCluster);
        org.junit.Assert.assertEquals(33L, ((long) (lastUpgradeForCluster.getId())));
    }

    @org.junit.Test
    public void testUpdatableColumns() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.RequestEntity requestEntity = new org.apache.ambari.server.orm.entities.RequestEntity();
        requestEntity.setRequestId(1L);
        requestEntity.setClusterId(clusterId.longValue());
        requestEntity.setStatus(org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING);
        requestEntity.setStages(new java.util.ArrayList<>());
        requestDAO.create(requestEntity);
        org.apache.ambari.server.orm.entities.UpgradeEntity upgradeEntity = new org.apache.ambari.server.orm.entities.UpgradeEntity();
        upgradeEntity.setId(11L);
        upgradeEntity.setClusterId(clusterId.longValue());
        upgradeEntity.setDirection(org.apache.ambari.server.stack.upgrade.Direction.UPGRADE);
        upgradeEntity.setRequestEntity(requestEntity);
        upgradeEntity.setRepositoryVersion(repositoryVersion2500);
        upgradeEntity.setUpgradeType(org.apache.ambari.spi.upgrade.UpgradeType.ROLLING);
        upgradeEntity.setUpgradePackage("test-upgrade");
        upgradeEntity.setUpgradePackStackId(new org.apache.ambari.server.state.StackId(((java.lang.String) (null))));
        dao.create(upgradeEntity);
        org.apache.ambari.server.orm.entities.UpgradeEntity lastUpgradeForCluster = dao.findLastUpgradeForCluster(1, org.apache.ambari.server.stack.upgrade.Direction.UPGRADE);
        org.junit.Assert.assertFalse(lastUpgradeForCluster.isComponentFailureAutoSkipped());
        org.junit.Assert.assertFalse(lastUpgradeForCluster.isServiceCheckFailureAutoSkipped());
        lastUpgradeForCluster.setAutoSkipComponentFailures(true);
        lastUpgradeForCluster.setAutoSkipServiceCheckFailures(true);
        dao.merge(lastUpgradeForCluster);
        lastUpgradeForCluster = dao.findLastUpgradeForCluster(1, org.apache.ambari.server.stack.upgrade.Direction.UPGRADE);
        org.junit.Assert.assertTrue(lastUpgradeForCluster.isComponentFailureAutoSkipped());
        org.junit.Assert.assertTrue(lastUpgradeForCluster.isServiceCheckFailureAutoSkipped());
    }

    @org.junit.Test
    public void testFindRevertableUpgrade() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.UpgradeEntity revertable = dao.findRevertable(1L);
        org.apache.ambari.server.orm.entities.UpgradeEntity revertableViaJPQL = dao.findRevertableUsingJPQL(1L);
        org.junit.Assert.assertEquals(null, revertable);
        org.junit.Assert.assertEquals(null, revertableViaJPQL);
        org.apache.ambari.server.orm.entities.RequestEntity requestEntity = new org.apache.ambari.server.orm.entities.RequestEntity();
        requestEntity.setRequestId(1L);
        requestEntity.setClusterId(clusterId.longValue());
        requestEntity.setStatus(org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING);
        requestEntity.setStages(new java.util.ArrayList<org.apache.ambari.server.orm.entities.StageEntity>());
        requestDAO.create(requestEntity);
        org.apache.ambari.server.orm.entities.UpgradeEntity entity1 = new org.apache.ambari.server.orm.entities.UpgradeEntity();
        entity1.setId(11L);
        entity1.setClusterId(clusterId.longValue());
        entity1.setDirection(org.apache.ambari.server.stack.upgrade.Direction.UPGRADE);
        entity1.setRequestEntity(requestEntity);
        entity1.setRepositoryVersion(repositoryVersion2500);
        entity1.setUpgradeType(org.apache.ambari.spi.upgrade.UpgradeType.ROLLING);
        entity1.setUpgradePackage("test-upgrade");
        entity1.setUpgradePackStackId(new org.apache.ambari.server.state.StackId(((java.lang.String) (null))));
        entity1.setDowngradeAllowed(true);
        entity1.setOrchestration(org.apache.ambari.spi.RepositoryType.PATCH);
        entity1.setRevertAllowed(true);
        dao.create(entity1);
        revertable = dao.findRevertable(1L);
        revertableViaJPQL = dao.findRevertableUsingJPQL(1L);
        org.junit.Assert.assertEquals(revertable.getId(), entity1.getId());
        org.junit.Assert.assertEquals(revertableViaJPQL.getId(), entity1.getId());
        org.apache.ambari.server.orm.entities.UpgradeEntity entity2 = new org.apache.ambari.server.orm.entities.UpgradeEntity();
        entity2.setId(22L);
        entity2.setClusterId(clusterId.longValue());
        entity2.setDirection(org.apache.ambari.server.stack.upgrade.Direction.UPGRADE);
        entity2.setRequestEntity(requestEntity);
        entity2.setRepositoryVersion(repositoryVersion2511);
        entity2.setUpgradeType(org.apache.ambari.spi.upgrade.UpgradeType.ROLLING);
        entity2.setUpgradePackage("test-upgrade");
        entity2.setUpgradePackStackId(new org.apache.ambari.server.state.StackId(((java.lang.String) (null))));
        entity2.setDowngradeAllowed(true);
        entity2.setOrchestration(org.apache.ambari.spi.RepositoryType.MAINT);
        entity2.setRevertAllowed(true);
        dao.create(entity2);
        revertable = dao.findRevertable(1L);
        revertableViaJPQL = dao.findRevertableUsingJPQL(1L);
        org.junit.Assert.assertEquals(revertable.getId(), entity2.getId());
        org.junit.Assert.assertEquals(revertableViaJPQL.getId(), entity2.getId());
        entity2.setRevertAllowed(false);
        entity2 = dao.merge(entity2);
        org.apache.ambari.server.orm.entities.UpgradeEntity entity3 = new org.apache.ambari.server.orm.entities.UpgradeEntity();
        entity3.setId(33L);
        entity3.setClusterId(clusterId.longValue());
        entity3.setDirection(org.apache.ambari.server.stack.upgrade.Direction.DOWNGRADE);
        entity3.setRequestEntity(requestEntity);
        entity3.setRepositoryVersion(repositoryVersion2511);
        entity3.setUpgradeType(org.apache.ambari.spi.upgrade.UpgradeType.ROLLING);
        entity3.setUpgradePackage("test-upgrade");
        entity3.setUpgradePackStackId(new org.apache.ambari.server.state.StackId(((java.lang.String) (null))));
        entity3.setOrchestration(org.apache.ambari.spi.RepositoryType.MAINT);
        entity3.setDowngradeAllowed(false);
        dao.create(entity3);
        revertable = dao.findRevertable(1L);
        revertableViaJPQL = dao.findRevertableUsingJPQL(1L);
        org.junit.Assert.assertEquals(revertable.getId(), entity1.getId());
        org.junit.Assert.assertEquals(revertableViaJPQL.getId(), entity1.getId());
        entity1.setRevertAllowed(false);
        entity1 = dao.merge(entity1);
        org.apache.ambari.server.orm.entities.UpgradeEntity entity4 = new org.apache.ambari.server.orm.entities.UpgradeEntity();
        entity4.setId(44L);
        entity4.setClusterId(clusterId.longValue());
        entity4.setDirection(org.apache.ambari.server.stack.upgrade.Direction.DOWNGRADE);
        entity4.setRequestEntity(requestEntity);
        entity4.setRepositoryVersion(repositoryVersion2500);
        entity4.setUpgradeType(org.apache.ambari.spi.upgrade.UpgradeType.ROLLING);
        entity4.setUpgradePackage("test-upgrade");
        entity4.setUpgradePackStackId(new org.apache.ambari.server.state.StackId(((java.lang.String) (null))));
        entity4.setOrchestration(org.apache.ambari.spi.RepositoryType.MAINT);
        entity4.setDowngradeAllowed(false);
        dao.create(entity4);
        revertable = dao.findRevertable(1L);
        revertableViaJPQL = dao.findRevertableUsingJPQL(1L);
        org.junit.Assert.assertEquals(null, revertable);
        org.junit.Assert.assertEquals(null, revertableViaJPQL);
    }
}