package org.apache.ambari.server.orm.dao;
import com.google.inject.persist.UnitOfWork;
public class AlertDefinitionDAOTest {
    static java.util.Calendar calendar = java.util.Calendar.getInstance(java.util.TimeZone.getTimeZone("UTC"));

    com.google.inject.Injector injector;

    java.lang.Long clusterId;

    org.apache.ambari.server.orm.dao.AlertDefinitionDAO dao;

    org.apache.ambari.server.orm.dao.AlertsDAO alertsDao;

    org.apache.ambari.server.orm.dao.AlertDispatchDAO dispatchDao;

    org.apache.ambari.server.orm.OrmTestHelper helper;

    @org.junit.Before
    public void setup() throws java.lang.Exception {
        injector = com.google.inject.Guice.createInjector(new org.apache.ambari.server.orm.InMemoryDefaultTestModule());
        injector.getInstance(org.apache.ambari.server.orm.GuiceJpaInitializer.class);
        injector.getInstance(com.google.inject.persist.UnitOfWork.class).begin();
        dispatchDao = injector.getInstance(org.apache.ambari.server.orm.dao.AlertDispatchDAO.class);
        dao = injector.getInstance(org.apache.ambari.server.orm.dao.AlertDefinitionDAO.class);
        alertsDao = injector.getInstance(org.apache.ambari.server.orm.dao.AlertsDAO.class);
        helper = injector.getInstance(org.apache.ambari.server.orm.OrmTestHelper.class);
        clusterId = helper.createCluster();
        helper.createDefaultAlertGroups(clusterId);
        int i = 0;
        for (; i < 8; i++) {
            org.apache.ambari.server.orm.entities.AlertDefinitionEntity definition = new org.apache.ambari.server.orm.entities.AlertDefinitionEntity();
            definition.setDefinitionName("Alert Definition " + i);
            definition.setServiceName("HDFS");
            definition.setComponentName(null);
            definition.setClusterId(clusterId);
            definition.setHash(java.util.UUID.randomUUID().toString());
            definition.setScheduleInterval(60);
            definition.setScope(org.apache.ambari.server.state.alert.Scope.SERVICE);
            definition.setSource("{\"type\" : \"SCRIPT\"}");
            definition.setSourceType(org.apache.ambari.server.state.alert.SourceType.SCRIPT);
            dao.create(definition);
        }
        for (; i < 10; i++) {
            org.apache.ambari.server.orm.entities.AlertDefinitionEntity definition = new org.apache.ambari.server.orm.entities.AlertDefinitionEntity();
            definition.setDefinitionName("Alert Definition " + i);
            definition.setServiceName("HDFS");
            if (i == 9) {
                definition.setComponentName("NAMENODE");
            } else {
                definition.setComponentName("DATANODE");
            }
            definition.setClusterId(clusterId);
            definition.setHash(java.util.UUID.randomUUID().toString());
            definition.setScheduleInterval(60);
            definition.setScope(org.apache.ambari.server.state.alert.Scope.SERVICE);
            definition.setSource("{\"type\" : \"SCRIPT\"}");
            definition.setSourceType(org.apache.ambari.server.state.alert.SourceType.SCRIPT);
            dao.create(definition);
        }
        for (; i < 12; i++) {
            org.apache.ambari.server.orm.entities.AlertDefinitionEntity definition = new org.apache.ambari.server.orm.entities.AlertDefinitionEntity();
            definition.setDefinitionName("Alert Definition " + i);
            definition.setServiceName("OOZIE");
            definition.setComponentName("OOZIE_SERVER");
            definition.setClusterId(clusterId);
            definition.setHash(java.util.UUID.randomUUID().toString());
            definition.setScheduleInterval(60);
            definition.setScope(org.apache.ambari.server.state.alert.Scope.HOST);
            definition.setSource("{\"type\" : \"SCRIPT\"}");
            definition.setSourceType(org.apache.ambari.server.state.alert.SourceType.SCRIPT);
            dao.create(definition);
        }
        for (; i < 15; i++) {
            org.apache.ambari.server.orm.entities.AlertDefinitionEntity definition = new org.apache.ambari.server.orm.entities.AlertDefinitionEntity();
            definition.setDefinitionName("Alert Definition " + i);
            definition.setServiceName(org.apache.ambari.server.controller.RootService.AMBARI.name());
            definition.setComponentName(org.apache.ambari.server.controller.RootComponent.AMBARI_AGENT.name());
            definition.setClusterId(clusterId);
            definition.setHash(java.util.UUID.randomUUID().toString());
            definition.setScheduleInterval(60);
            definition.setScope(org.apache.ambari.server.state.alert.Scope.HOST);
            definition.setSource("{\"type\" : \"SCRIPT\"}");
            definition.setSourceType(org.apache.ambari.server.state.alert.SourceType.SCRIPT);
            dao.create(definition);
        }
    }

    @org.junit.After
    public void teardown() throws java.lang.Exception {
        injector.getInstance(com.google.inject.persist.UnitOfWork.class).end();
        org.apache.ambari.server.H2DatabaseCleaner.clearDatabaseAndStopPersistenceService(injector);
        injector = null;
    }

    @org.junit.Test
    public void testFindByName() {
        java.util.List<org.apache.ambari.server.orm.entities.AlertDefinitionEntity> definitions = dao.findAll();
        org.junit.Assert.assertNotNull(definitions);
        org.apache.ambari.server.orm.entities.AlertDefinitionEntity definition = definitions.get(2);
        org.apache.ambari.server.orm.entities.AlertDefinitionEntity retrieved = dao.findByName(definition.getClusterId(), definition.getDefinitionName());
        org.junit.Assert.assertEquals(definition, retrieved);
    }

    @org.junit.Test
    public void testFindAll() {
        java.util.List<org.apache.ambari.server.orm.entities.AlertDefinitionEntity> definitions = dao.findAll();
        org.junit.Assert.assertNotNull(definitions);
        org.junit.Assert.assertEquals(15, definitions.size());
    }

    @org.junit.Test
    public void testFindAllEnabled() {
        java.util.List<org.apache.ambari.server.orm.entities.AlertDefinitionEntity> definitions = dao.findAll();
        org.junit.Assert.assertNotNull(definitions);
        org.junit.Assert.assertEquals(15, definitions.size());
        java.util.List<org.apache.ambari.server.orm.entities.AlertDefinitionEntity> enabledDefinitions = dao.findAllEnabled(clusterId);
        org.junit.Assert.assertNotNull(enabledDefinitions);
        org.junit.Assert.assertEquals(definitions.size(), enabledDefinitions.size());
        enabledDefinitions.get(0).setEnabled(false);
        dao.merge(enabledDefinitions.get(0));
        enabledDefinitions = dao.findAllEnabled(clusterId);
        org.junit.Assert.assertNotNull(enabledDefinitions);
        org.junit.Assert.assertEquals(definitions.size() - 1, enabledDefinitions.size());
    }

    @org.junit.Test
    public void testFindById() {
        java.util.List<org.apache.ambari.server.orm.entities.AlertDefinitionEntity> definitions = dao.findAll();
        org.junit.Assert.assertNotNull(definitions);
        org.apache.ambari.server.orm.entities.AlertDefinitionEntity definition = definitions.get(2);
        org.apache.ambari.server.orm.entities.AlertDefinitionEntity retrieved = dao.findById(definition.getDefinitionId());
        org.junit.Assert.assertEquals(definition, retrieved);
    }

    @org.junit.Test
    public void testFindByIds() {
        java.util.List<org.apache.ambari.server.orm.entities.AlertDefinitionEntity> definitions = dao.findAll();
        java.util.List<java.lang.Long> ids = new java.util.ArrayList<>();
        ids.add(definitions.get(0).getDefinitionId());
        ids.add(definitions.get(1).getDefinitionId());
        ids.add(99999L);
        definitions = dao.findByIds(ids);
        org.junit.Assert.assertEquals(2, definitions.size());
    }

    @org.junit.Test
    public void testFindByService() {
        java.util.List<org.apache.ambari.server.orm.entities.AlertDefinitionEntity> definitions = dao.findByService(clusterId, "HDFS");
        org.junit.Assert.assertNotNull(definitions);
        org.junit.Assert.assertEquals(10, definitions.size());
        definitions = dao.findByService(clusterId, "YARN");
        org.junit.Assert.assertNotNull(definitions);
        org.junit.Assert.assertEquals(0, definitions.size());
    }

    @org.junit.Test
    public void testFindByServiceComponent() {
        java.util.List<org.apache.ambari.server.orm.entities.AlertDefinitionEntity> definitions = dao.findByServiceComponent(clusterId, "OOZIE", "OOZIE_SERVER");
        org.junit.Assert.assertNotNull(definitions);
        org.junit.Assert.assertEquals(2, definitions.size());
    }

    @org.junit.Test
    public void testFindAgentScoped() {
        java.util.List<org.apache.ambari.server.orm.entities.AlertDefinitionEntity> definitions = dao.findAgentScoped(clusterId);
        org.junit.Assert.assertNotNull(definitions);
        org.junit.Assert.assertEquals(3, definitions.size());
    }

    @org.junit.Test
    public void testRefresh() {
    }

    @org.junit.Test
    public void testCreate() {
    }

    @org.junit.Test
    public void testMerge() {
    }

    @org.junit.Test
    public void testRemove() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.AlertDefinitionEntity definition = helper.createAlertDefinition(clusterId);
        definition = dao.findById(definition.getDefinitionId());
        org.junit.Assert.assertNotNull(definition);
        dao.remove(definition);
        definition = dao.findById(definition.getDefinitionId());
        org.junit.Assert.assertNull(definition);
    }

    @org.junit.Test
    public void testCascadeDelete() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.AlertDefinitionEntity definition = helper.createAlertDefinition(clusterId);
        org.apache.ambari.server.orm.entities.AlertGroupEntity group = helper.createAlertGroup(clusterId, null);
        group.addAlertDefinition(definition);
        dispatchDao.merge(group);
        org.apache.ambari.server.orm.entities.AlertHistoryEntity history = new org.apache.ambari.server.orm.entities.AlertHistoryEntity();
        history.setServiceName(definition.getServiceName());
        history.setClusterId(clusterId);
        history.setAlertDefinition(definition);
        history.setAlertLabel("Label");
        history.setAlertState(org.apache.ambari.server.state.AlertState.OK);
        history.setAlertText("Alert Text");
        history.setAlertTimestamp(org.apache.ambari.server.orm.dao.AlertDefinitionDAOTest.calendar.getTimeInMillis());
        alertsDao.create(history);
        org.apache.ambari.server.orm.entities.AlertCurrentEntity current = new org.apache.ambari.server.orm.entities.AlertCurrentEntity();
        current.setAlertHistory(history);
        current.setLatestTimestamp(new java.util.Date().getTime());
        current.setOriginalTimestamp(new java.util.Date().getTime() - 10800000);
        current.setMaintenanceState(org.apache.ambari.server.state.MaintenanceState.OFF);
        alertsDao.create(current);
        org.apache.ambari.server.orm.entities.AlertNoticeEntity notice = new org.apache.ambari.server.orm.entities.AlertNoticeEntity();
        notice.setAlertHistory(history);
        notice.setAlertTarget(helper.createAlertTarget());
        notice.setNotifyState(org.apache.ambari.server.state.NotificationState.PENDING);
        notice.setUuid(java.util.UUID.randomUUID().toString());
        dispatchDao.create(notice);
        group = dispatchDao.findGroupById(group.getGroupId());
        org.junit.Assert.assertNotNull(group);
        org.junit.Assert.assertNotNull(group.getAlertDefinitions());
        org.junit.Assert.assertEquals(1, group.getAlertDefinitions().size());
        history = alertsDao.findById(history.getAlertId());
        org.junit.Assert.assertNotNull(history);
        current = alertsDao.findCurrentById(current.getAlertId());
        org.junit.Assert.assertNotNull(current);
        org.junit.Assert.assertNotNull(current.getAlertHistory());
        notice = dispatchDao.findNoticeById(notice.getNotificationId());
        org.junit.Assert.assertNotNull(notice);
        org.junit.Assert.assertNotNull(notice.getAlertHistory());
        org.junit.Assert.assertNotNull(notice.getAlertTarget());
        definition = dao.findById(definition.getDefinitionId());
        dao.refresh(definition);
        dao.remove(definition);
        notice = dispatchDao.findNoticeById(notice.getNotificationId());
        org.junit.Assert.assertNull(notice);
        current = alertsDao.findCurrentById(current.getAlertId());
        org.junit.Assert.assertNull(current);
        history = alertsDao.findById(history.getAlertId());
        org.junit.Assert.assertNull(history);
        group = dispatchDao.findGroupById(group.getGroupId());
        org.junit.Assert.assertNotNull(group);
        org.junit.Assert.assertNotNull(group.getAlertDefinitions());
        org.junit.Assert.assertEquals(0, group.getAlertDefinitions().size());
    }

    @org.junit.Test
    public void testCascadeDeleteForCluster() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.AlertDefinitionEntity definition = helper.createAlertDefinition(clusterId);
        definition = dao.findById(definition.getDefinitionId());
        dao.refresh(definition);
        org.apache.ambari.server.orm.dao.ClusterDAO clusterDAO = injector.getInstance(org.apache.ambari.server.orm.dao.ClusterDAO.class);
        org.apache.ambari.server.orm.entities.ClusterEntity clusterEntity = clusterDAO.findById(clusterId);
        clusterDAO.refresh(clusterEntity);
        org.apache.ambari.server.state.Clusters clusters = injector.getInstance(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.state.Cluster cluster = clusters.getClusterById(clusterId);
        cluster.delete();
        org.junit.Assert.assertNull(clusterDAO.findById(clusterId));
        org.junit.Assert.assertNull(dao.findById(definition.getDefinitionId()));
        org.junit.Assert.assertEquals(0, dispatchDao.findAllGroups(clusterId).size());
    }

    @org.junit.Test
    public void testNestedClusterEntity() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.AlertDefinitionEntity definition = new org.apache.ambari.server.orm.entities.AlertDefinitionEntity();
        definition.setDefinitionName("nested-cluster-entity-test");
        definition.setServiceName("HDFS");
        definition.setComponentName(null);
        definition.setClusterId(clusterId);
        definition.setHash(java.util.UUID.randomUUID().toString());
        definition.setScheduleInterval(60);
        definition.setScope(org.apache.ambari.server.state.alert.Scope.SERVICE);
        definition.setSource("{\"type\" : \"SCRIPT\"}");
        definition.setSourceType(org.apache.ambari.server.state.alert.SourceType.SCRIPT);
        dao.create(definition);
        definition = dao.findById(definition.getDefinitionId());
        org.junit.Assert.assertNotNull(definition.getCluster());
        org.junit.Assert.assertEquals(clusterId, definition.getCluster().getClusterId());
    }

    @org.junit.Test
    public void testBatchDeleteOfNoticeEntities() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.AlertDefinitionEntity definition = helper.createAlertDefinition(clusterId);
        org.apache.ambari.server.orm.entities.AlertGroupEntity group = helper.createAlertGroup(clusterId, null);
        group.addAlertDefinition(definition);
        dispatchDao.merge(group);
        for (int i = 0; i < 1500; i++) {
            org.apache.ambari.server.orm.entities.AlertHistoryEntity history = new org.apache.ambari.server.orm.entities.AlertHistoryEntity();
            history.setServiceName(definition.getServiceName());
            history.setClusterId(clusterId);
            history.setAlertDefinition(definition);
            history.setAlertLabel("Label");
            history.setAlertState(org.apache.ambari.server.state.AlertState.OK);
            history.setAlertText("Alert Text");
            history.setAlertTimestamp(org.apache.ambari.server.orm.dao.AlertDefinitionDAOTest.calendar.getTimeInMillis());
            alertsDao.create(history);
            org.apache.ambari.server.orm.entities.AlertCurrentEntity current = new org.apache.ambari.server.orm.entities.AlertCurrentEntity();
            current.setAlertHistory(history);
            current.setLatestTimestamp(new java.util.Date().getTime());
            current.setOriginalTimestamp(new java.util.Date().getTime() - 10800000);
            current.setMaintenanceState(org.apache.ambari.server.state.MaintenanceState.OFF);
            alertsDao.create(current);
            org.apache.ambari.server.orm.entities.AlertNoticeEntity notice = new org.apache.ambari.server.orm.entities.AlertNoticeEntity();
            notice.setAlertHistory(history);
            notice.setAlertTarget(helper.createAlertTarget());
            notice.setNotifyState(org.apache.ambari.server.state.NotificationState.PENDING);
            notice.setUuid(java.util.UUID.randomUUID().toString());
            dispatchDao.create(notice);
        }
        group = dispatchDao.findGroupById(group.getGroupId());
        org.junit.Assert.assertNotNull(group);
        org.junit.Assert.assertNotNull(group.getAlertDefinitions());
        org.junit.Assert.assertEquals(1, group.getAlertDefinitions().size());
        java.util.List<org.apache.ambari.server.orm.entities.AlertHistoryEntity> historyEntities = alertsDao.findAll();
        org.junit.Assert.assertEquals(1500, historyEntities.size());
        java.util.List<org.apache.ambari.server.orm.entities.AlertCurrentEntity> currentEntities = alertsDao.findCurrentByDefinitionId(definition.getDefinitionId());
        org.junit.Assert.assertNotNull(currentEntities);
        org.junit.Assert.assertEquals(1500, currentEntities.size());
        java.util.List<org.apache.ambari.server.orm.entities.AlertNoticeEntity> noticeEntities = dispatchDao.findAllNotices();
        junit.framework.Assert.assertEquals(1500, noticeEntities.size());
        definition = dao.findById(definition.getDefinitionId());
        dao.refresh(definition);
        dao.remove(definition);
        java.util.List<org.apache.ambari.server.orm.entities.AlertNoticeEntity> notices = dispatchDao.findAllNotices();
        org.junit.Assert.assertTrue(notices.isEmpty());
        currentEntities = alertsDao.findCurrentByDefinitionId(definition.getDefinitionId());
        org.junit.Assert.assertTrue((currentEntities == null) || currentEntities.isEmpty());
        historyEntities = alertsDao.findAll();
        org.junit.Assert.assertTrue((historyEntities == null) || historyEntities.isEmpty());
        group = dispatchDao.findGroupById(group.getGroupId());
        org.junit.Assert.assertNotNull(group);
        org.junit.Assert.assertNotNull(group.getAlertDefinitions());
        org.junit.Assert.assertEquals(0, group.getAlertDefinitions().size());
    }
}