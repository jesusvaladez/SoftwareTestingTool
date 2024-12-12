package org.apache.ambari.server.orm.dao;
public class StageDAOTest {
    private com.google.inject.Injector injector;

    private org.apache.ambari.server.orm.dao.StageDAO stageDao;

    @org.junit.Before
    public void setup() throws java.lang.Exception {
        injector = com.google.inject.Guice.createInjector(new org.apache.ambari.server.orm.InMemoryDefaultTestModule());
        injector.getInstance(org.apache.ambari.server.orm.GuiceJpaInitializer.class);
        stageDao = injector.getInstance(org.apache.ambari.server.orm.dao.StageDAO.class);
        org.apache.ambari.server.orm.OrmTestHelper helper = injector.getInstance(org.apache.ambari.server.orm.OrmTestHelper.class);
        java.lang.Long clusterId = helper.createCluster();
        org.apache.ambari.server.orm.dao.RequestDAO requestDao = injector.getInstance(org.apache.ambari.server.orm.dao.RequestDAO.class);
        org.apache.ambari.server.orm.entities.RequestEntity requestEntity = new org.apache.ambari.server.orm.entities.RequestEntity();
        requestEntity.setClusterId(clusterId);
        requestEntity.setStartTime(1000L);
        requestEntity.setEndTime(1200L);
        requestEntity.setRequestId(99L);
        requestDao.create(requestEntity);
        for (int i = 0; i < 5; i++) {
            org.apache.ambari.server.orm.entities.StageEntity definition = new org.apache.ambari.server.orm.entities.StageEntity();
            definition.setClusterId(clusterId);
            definition.setRequestId(99L);
            definition.setStageId(((long) (100 + i)));
            definition.setLogInfo("log info for " + i);
            definition.setRequestContext("request context for " + i);
            definition.setRequest(requestEntity);
            stageDao.create(definition);
        }
        java.util.List<org.apache.ambari.server.orm.entities.StageEntity> definitions = stageDao.findAll();
        org.junit.Assert.assertNotNull(definitions);
        org.junit.Assert.assertEquals(5, definitions.size());
    }

    @org.junit.After
    public void teardown() throws org.apache.ambari.server.AmbariException, java.sql.SQLException {
        org.apache.ambari.server.H2DatabaseCleaner.clearDatabaseAndStopPersistenceService(injector);
        injector = null;
    }

    @org.junit.Test
    public void testStagePredicate() throws java.lang.Exception {
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.StageResourceProvider.STAGE_CLUSTER_NAME).equals("c1").toPredicate();
        java.util.List<org.apache.ambari.server.orm.entities.StageEntity> entities = stageDao.findAll(org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(), predicate);
        org.junit.Assert.assertEquals(5, entities.size());
        predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.StageResourceProvider.STAGE_CONTEXT).equals("request context for 3").or().property(org.apache.ambari.server.controller.internal.StageResourceProvider.STAGE_CONTEXT).equals("request context for 4").toPredicate();
        entities = stageDao.findAll(org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(), predicate);
        org.junit.Assert.assertEquals(2, entities.size());
    }

    @org.junit.Test
    public void testStageSorting() throws java.lang.Exception {
        java.util.List<org.apache.ambari.server.controller.spi.SortRequestProperty> sortProperties = new java.util.ArrayList<>();
        org.apache.ambari.server.controller.spi.SortRequest sortRequest = new org.apache.ambari.server.controller.internal.SortRequestImpl(sortProperties);
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.StageResourceProvider.STAGE_CLUSTER_NAME).equals("c1").toPredicate();
        sortProperties.add(new org.apache.ambari.server.controller.spi.SortRequestProperty(org.apache.ambari.server.controller.internal.StageResourceProvider.STAGE_LOG_INFO, org.apache.ambari.server.controller.spi.SortRequest.Order.ASC));
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(new java.util.HashSet<>(java.util.Arrays.asList()), null, null, null, sortRequest);
        java.util.List<org.apache.ambari.server.orm.entities.StageEntity> entities = stageDao.findAll(request, predicate);
        org.junit.Assert.assertEquals(5, entities.size());
        java.lang.String lastInfo = null;
        for (org.apache.ambari.server.orm.entities.StageEntity entity : entities) {
            if (lastInfo == null) {
                lastInfo = entity.getLogInfo();
                continue;
            }
            java.lang.String currentInfo = entity.getLogInfo();
            org.junit.Assert.assertTrue(lastInfo.compareTo(currentInfo) <= 0);
            lastInfo = currentInfo;
        }
        sortProperties.clear();
        sortProperties.add(new org.apache.ambari.server.controller.spi.SortRequestProperty(org.apache.ambari.server.controller.internal.StageResourceProvider.STAGE_LOG_INFO, org.apache.ambari.server.controller.spi.SortRequest.Order.DESC));
        entities = stageDao.findAll(request, predicate);
        org.junit.Assert.assertEquals(5, entities.size());
        lastInfo = null;
        for (org.apache.ambari.server.orm.entities.StageEntity entity : entities) {
            if (null == lastInfo) {
                lastInfo = entity.getLogInfo();
                continue;
            }
            java.lang.String currentInfo = entity.getLogInfo();
            org.junit.Assert.assertTrue(lastInfo.compareTo(currentInfo) >= 0);
            lastInfo = currentInfo;
        }
    }
}