package org.apache.ambari.server.orm;
import javax.persistence.EntityManager;
import javax.persistence.RollbackException;
public class TestOrmImpl extends org.junit.Assert {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.orm.TestOrmImpl.class);

    @com.google.inject.Inject
    private com.google.inject.Injector injector;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.StackDAO stackDAO;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.ResourceTypeDAO resourceTypeDAO;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.ClusterDAO clusterDAO;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.OrmTestHelper ormTestHelper;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.ClusterServiceDAO clusterServiceDAO;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.HostRoleCommandDAO hostRoleCommandDAO;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.HostDAO hostDAO;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.StageDAO stageDAO;

    @com.google.inject.Inject
    private javax.persistence.EntityManager entityManager;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.RequestDAO requestDAO;

    @org.junit.Before
    public void setup() {
        injector = com.google.inject.Guice.createInjector(new org.apache.ambari.server.orm.InMemoryDefaultTestModule());
        injector.getInstance(org.apache.ambari.server.orm.GuiceJpaInitializer.class);
        injector.injectMembers(this);
        injector.getInstance(org.apache.ambari.server.api.services.AmbariMetaInfo.class);
        ormTestHelper.createDefaultData();
    }

    @org.junit.After
    public void teardown() throws org.apache.ambari.server.AmbariException, java.sql.SQLException {
        org.apache.ambari.server.H2DatabaseCleaner.clearDatabaseAndStopPersistenceService(injector);
    }

    @org.junit.Test
    public void testEmptyPersistentCollection() {
        java.lang.String testClusterName = "test_cluster2";
        org.apache.ambari.server.orm.entities.ResourceTypeEntity resourceTypeEntity = resourceTypeDAO.findById(org.apache.ambari.server.security.authorization.ResourceType.CLUSTER.getId());
        if (resourceTypeEntity == null) {
            resourceTypeEntity = new org.apache.ambari.server.orm.entities.ResourceTypeEntity();
            resourceTypeEntity.setId(org.apache.ambari.server.security.authorization.ResourceType.CLUSTER.getId());
            resourceTypeEntity.setName(org.apache.ambari.server.security.authorization.ResourceType.CLUSTER.name());
            resourceTypeEntity = resourceTypeDAO.merge(resourceTypeEntity);
        }
        org.apache.ambari.server.orm.entities.StackEntity stackEntity = stackDAO.find("HDP", "2.2.0");
        org.apache.ambari.server.orm.entities.ResourceEntity resourceEntity = new org.apache.ambari.server.orm.entities.ResourceEntity();
        resourceEntity.setResourceType(resourceTypeEntity);
        org.apache.ambari.server.orm.entities.ClusterEntity clusterEntity = new org.apache.ambari.server.orm.entities.ClusterEntity();
        clusterEntity.setClusterName(testClusterName);
        clusterEntity.setResource(resourceEntity);
        clusterEntity.setDesiredStack(stackEntity);
        clusterDAO.create(clusterEntity);
        clusterEntity = clusterDAO.findByName(clusterEntity.getClusterName());
        org.junit.Assert.assertTrue("empty relation wasn't instantiated", clusterEntity.getHostEntities() != null);
    }

    @org.junit.Test(expected = javax.persistence.RollbackException.class)
    public void testRollbackException() throws java.lang.Throwable {
        ormTestHelper.performTransactionMarkedForRollback();
    }

    @org.junit.Test
    public void testSafeRollback() {
        java.lang.String testClusterName = "don't save";
        javax.persistence.EntityManager entityManager = ormTestHelper.getEntityManager();
        entityManager.getTransaction().begin();
        org.apache.ambari.server.orm.entities.ClusterEntity clusterEntity = new org.apache.ambari.server.orm.entities.ClusterEntity();
        clusterEntity.setClusterName(testClusterName);
        entityManager.persist(clusterEntity);
        entityManager.getTransaction().rollback();
        org.junit.Assert.assertNull("transaction was not rolled back", clusterDAO.findByName(testClusterName));
    }

    @org.junit.Test
    public void testAutoIncrementedField() {
        java.util.Date currentTime = new java.util.Date();
        java.lang.String serviceName = "MapReduce1";
        java.lang.String clusterName = "test_cluster1";
        createService(currentTime, serviceName, clusterName);
        org.apache.ambari.server.orm.entities.ClusterServiceEntity clusterServiceEntity = clusterServiceDAO.findByClusterAndServiceNames(clusterName, serviceName);
        clusterServiceDAO.remove(clusterServiceEntity);
        org.junit.Assert.assertNull(clusterServiceDAO.findByClusterAndServiceNames(clusterName, serviceName));
    }

    private void createService(java.util.Date currentTime, java.lang.String serviceName, java.lang.String clusterName) {
        org.apache.ambari.server.orm.entities.ClusterEntity cluster = clusterDAO.findByName(clusterName);
        org.apache.ambari.server.orm.entities.ClusterServiceEntity clusterServiceEntity = new org.apache.ambari.server.orm.entities.ClusterServiceEntity();
        clusterServiceEntity.setClusterEntity(cluster);
        clusterServiceEntity.setServiceName(serviceName);
        cluster.getClusterServiceEntities().add(clusterServiceEntity);
        clusterServiceDAO.create(clusterServiceEntity);
        clusterDAO.merge(cluster);
        clusterServiceEntity = clusterServiceDAO.findByClusterAndServiceNames(clusterName, serviceName);
        org.junit.Assert.assertNotNull(clusterServiceEntity);
        clusterServiceDAO.merge(clusterServiceEntity);
    }

    @org.junit.Test
    public void testCascadeRemoveFail() {
        java.util.Date currentTime = new java.util.Date();
        java.lang.String serviceName = "MapReduce2";
        java.lang.String clusterName = "test_cluster1";
        createService(currentTime, serviceName, clusterName);
        org.apache.ambari.server.orm.entities.ClusterServiceEntity clusterServiceEntity = clusterServiceDAO.findByClusterAndServiceNames(clusterName, serviceName);
        clusterServiceDAO.remove(clusterServiceEntity);
        org.junit.Assert.assertNull(clusterServiceDAO.findByClusterAndServiceNames(clusterName, serviceName));
    }

    @org.junit.Test
    public void testSortedCommands() {
        injector.getInstance(org.apache.ambari.server.orm.OrmTestHelper.class).createStageCommands();
        org.apache.ambari.server.orm.dao.HostRoleCommandDAO hostRoleCommandDAO = injector.getInstance(org.apache.ambari.server.orm.dao.HostRoleCommandDAO.class);
        org.apache.ambari.server.orm.dao.HostDAO hostDAO = injector.getInstance(org.apache.ambari.server.orm.dao.HostDAO.class);
        org.apache.ambari.server.orm.dao.StageDAO stageDAO = injector.getInstance(org.apache.ambari.server.orm.dao.StageDAO.class);
        java.util.List<org.apache.ambari.server.orm.entities.HostRoleCommandEntity> list = hostRoleCommandDAO.findSortedCommandsByStageAndHost(stageDAO.findByActionId("1-1"), hostDAO.findByName("test_host1"));
        org.apache.ambari.server.orm.TestOrmImpl.log.info("command '{}' - taskId '{}' ", list.get(0).getRoleCommand(), list.get(0).getTaskId());
        org.apache.ambari.server.orm.TestOrmImpl.log.info("command '{}' - taskId '{}'", list.get(1).getRoleCommand(), list.get(1).getTaskId());
        org.junit.Assert.assertTrue(list.get(0).getTaskId() < list.get(1).getTaskId());
    }

    @org.junit.Test
    public void testFindHostsByStage() {
        ormTestHelper.createStageCommands();
        org.apache.ambari.server.orm.entities.StageEntity stageEntity = stageDAO.findByActionId("1-1");
        org.apache.ambari.server.orm.TestOrmImpl.log.info((("StageEntity {} {}" + stageEntity.getRequestId()) + " ") + stageEntity.getStageId());
        java.util.List<org.apache.ambari.server.orm.entities.HostEntity> hosts = hostDAO.findByStage(stageEntity);
        org.junit.Assert.assertEquals(2, hosts.size());
    }

    @org.junit.Test
    public void testAbortHostRoleCommands() {
        ormTestHelper.createStageCommands();
        int result = hostRoleCommandDAO.updateStatusByRequestId(1L, org.apache.ambari.server.actionmanager.HostRoleStatus.ABORTED, java.util.Arrays.asList(org.apache.ambari.server.actionmanager.HostRoleStatus.QUEUED, org.apache.ambari.server.actionmanager.HostRoleStatus.IN_PROGRESS, org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING));
        java.util.List<org.apache.ambari.server.orm.entities.HostRoleCommandEntity> commandEntities = hostRoleCommandDAO.findByRequest(1L);
        int count = 0;
        for (org.apache.ambari.server.orm.entities.HostRoleCommandEntity commandEntity : commandEntities) {
            if (commandEntity.getStatus() == org.apache.ambari.server.actionmanager.HostRoleStatus.ABORTED) {
                count++;
            }
        }
        org.junit.Assert.assertEquals("Exactly two commands should be in aborted state", 2, count);
    }

    @org.junit.Test
    public void testFindStageByHostRole() {
        ormTestHelper.createStageCommands();
        java.util.List<org.apache.ambari.server.orm.entities.HostRoleCommandEntity> list = hostRoleCommandDAO.findByHostRole("test_host1", 1L, 1L, org.apache.ambari.server.Role.DATANODE.toString());
        org.junit.Assert.assertEquals(1, list.size());
    }

    @org.junit.Test
    public void testLastRequestId() {
        ormTestHelper.createStageCommands();
        org.apache.ambari.server.orm.dao.RequestDAO requestDAO = injector.getInstance(org.apache.ambari.server.orm.dao.RequestDAO.class);
        org.apache.ambari.server.orm.entities.RequestEntity requestEntity = requestDAO.findByPK(1L);
        java.util.List<org.apache.ambari.server.orm.entities.StageEntity> stageEntities = new java.util.ArrayList<>();
        org.apache.ambari.server.orm.entities.StageEntity stageEntity = new org.apache.ambari.server.orm.entities.StageEntity();
        stageEntity.setClusterId(clusterDAO.findByName("test_cluster1").getClusterId());
        stageEntity.setRequest(requestEntity);
        stageEntity.setStageId(2L);
        stageDAO.create(stageEntity);
        org.apache.ambari.server.orm.entities.StageEntity stageEntity2 = new org.apache.ambari.server.orm.entities.StageEntity();
        stageEntity2.setClusterId(clusterDAO.findByName("test_cluster1").getClusterId());
        stageEntity2.setRequest(requestEntity);
        stageEntity2.setRequestId(1L);
        stageEntity2.setStageId(3L);
        stageDAO.create(stageEntity2);
        stageEntities.add(stageEntity);
        stageEntities.add(stageEntity2);
        requestEntity.setStages(stageEntities);
        requestDAO.merge(requestEntity);
        org.junit.Assert.assertEquals(1L, stageDAO.getLastRequestId());
    }

    @org.junit.Test
    public void testConcurrentModification() throws java.lang.InterruptedException {
        final org.apache.ambari.server.orm.entities.StackEntity stackEntity = stackDAO.find("HDP", "2.2.0");
        org.apache.ambari.server.orm.entities.ResourceTypeEntity resourceTypeEntity = resourceTypeDAO.findById(org.apache.ambari.server.security.authorization.ResourceType.CLUSTER.getId());
        if (resourceTypeEntity == null) {
            resourceTypeEntity = new org.apache.ambari.server.orm.entities.ResourceTypeEntity();
            resourceTypeEntity.setId(org.apache.ambari.server.security.authorization.ResourceType.CLUSTER.getId());
            resourceTypeEntity.setName(org.apache.ambari.server.security.authorization.ResourceType.CLUSTER.name());
            resourceTypeEntity = resourceTypeDAO.merge(resourceTypeEntity);
        }
        org.apache.ambari.server.orm.entities.ResourceEntity resourceEntity = new org.apache.ambari.server.orm.entities.ResourceEntity();
        resourceEntity.setResourceType(resourceTypeEntity);
        org.apache.ambari.server.orm.entities.ClusterEntity clusterEntity = new org.apache.ambari.server.orm.entities.ClusterEntity();
        clusterEntity.setClusterName("cluster1");
        clusterEntity.setResource(resourceEntity);
        clusterEntity.setDesiredStack(stackEntity);
        clusterDAO.create(clusterEntity);
        clusterEntity = clusterDAO.findById(clusterEntity.getClusterId());
        org.junit.Assert.assertEquals("cluster1", clusterEntity.getClusterName());
        java.lang.Thread thread = new java.lang.Thread() {
            @java.lang.Override
            public void run() {
                org.apache.ambari.server.orm.entities.ClusterEntity clusterEntity1 = clusterDAO.findByName("cluster1");
                clusterEntity1.setClusterName("anotherName");
                clusterDAO.merge(clusterEntity1);
                clusterEntity1 = clusterDAO.findById(clusterEntity1.getClusterId());
                org.junit.Assert.assertEquals("anotherName", clusterEntity1.getClusterName());
                entityManager.clear();
            }
        };
        thread.start();
        thread.join();
        entityManager.clear();
        clusterEntity = clusterDAO.findById(clusterEntity.getClusterId());
        org.junit.Assert.assertEquals("anotherName", clusterEntity.getClusterName());
        thread = new java.lang.Thread() {
            @java.lang.Override
            public void run() {
                clusterDAO.removeByName("anotherName");
                entityManager.clear();
            }
        };
        thread.start();
        thread.join();
        entityManager.clear();
        org.junit.Assert.assertNull(clusterDAO.findById(clusterEntity.getClusterId()));
        java.util.List<org.apache.ambari.server.orm.entities.ClusterEntity> result = clusterDAO.findAll();
        final org.apache.ambari.server.orm.entities.ResourceTypeEntity finalResourceTypeEntity = resourceTypeEntity;
        thread = new java.lang.Thread() {
            @java.lang.Override
            public void run() {
                org.apache.ambari.server.orm.entities.ResourceEntity resourceEntity = new org.apache.ambari.server.orm.entities.ResourceEntity();
                resourceEntity.setResourceType(finalResourceTypeEntity);
                org.apache.ambari.server.orm.entities.ClusterEntity temp = new org.apache.ambari.server.orm.entities.ClusterEntity();
                temp.setClusterName("temp_cluster");
                temp.setResource(resourceEntity);
                temp.setDesiredStack(stackEntity);
                clusterDAO.create(temp);
            }
        };
        thread.start();
        thread.join();
        org.junit.Assert.assertEquals(result.size() + 1, (result = clusterDAO.findAll()).size());
        thread = new java.lang.Thread() {
            @java.lang.Override
            public void run() {
                org.apache.ambari.server.orm.entities.ResourceEntity resourceEntity = new org.apache.ambari.server.orm.entities.ResourceEntity();
                resourceEntity.setResourceType(finalResourceTypeEntity);
                org.apache.ambari.server.orm.entities.ClusterEntity temp = new org.apache.ambari.server.orm.entities.ClusterEntity();
                temp.setClusterName("temp_cluster2");
                temp.setResource(resourceEntity);
                temp.setDesiredStack(stackEntity);
                clusterDAO.create(temp);
            }
        };
        thread.start();
        thread.join();
        org.junit.Assert.assertEquals(result.size() + 1, clusterDAO.findAll().size());
    }
}