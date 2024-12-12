package org.apache.ambari.server.orm.dao;
import com.google.inject.persist.UnitOfWork;
import javax.persistence.EntityManager;
public class AlertDispatchDAOTest {
    private static final java.lang.String HOSTNAME = "c6401.ambari.apache.org";

    private org.apache.ambari.server.state.Clusters m_clusters;

    private org.apache.ambari.server.state.Cluster m_cluster;

    private com.google.inject.Injector m_injector;

    private org.apache.ambari.server.orm.dao.AlertDispatchDAO m_dao;

    private org.apache.ambari.server.orm.dao.AlertDefinitionDAO m_definitionDao;

    private org.apache.ambari.server.orm.dao.AlertsDAO m_alertsDao;

    private org.apache.ambari.server.orm.OrmTestHelper m_helper;

    private org.apache.ambari.server.state.ServiceFactory m_serviceFactory;

    private org.apache.ambari.server.state.ServiceComponentFactory m_componentFactory;

    private org.apache.ambari.server.state.ServiceComponentHostFactory m_schFactory;

    private org.apache.ambari.server.orm.AlertDaoHelper m_alertHelper;

    @org.junit.Before
    public void setup() throws java.lang.Exception {
        m_injector = com.google.inject.Guice.createInjector(new org.apache.ambari.server.orm.InMemoryDefaultTestModule());
        m_injector.getInstance(org.apache.ambari.server.orm.GuiceJpaInitializer.class);
        m_injector.getInstance(com.google.inject.persist.UnitOfWork.class).begin();
        m_dao = m_injector.getInstance(org.apache.ambari.server.orm.dao.AlertDispatchDAO.class);
        m_alertsDao = m_injector.getInstance(org.apache.ambari.server.orm.dao.AlertsDAO.class);
        m_definitionDao = m_injector.getInstance(org.apache.ambari.server.orm.dao.AlertDefinitionDAO.class);
        m_helper = m_injector.getInstance(org.apache.ambari.server.orm.OrmTestHelper.class);
        m_serviceFactory = m_injector.getInstance(org.apache.ambari.server.state.ServiceFactory.class);
        m_componentFactory = m_injector.getInstance(org.apache.ambari.server.state.ServiceComponentFactory.class);
        m_schFactory = m_injector.getInstance(org.apache.ambari.server.state.ServiceComponentHostFactory.class);
        m_clusters = m_injector.getInstance(org.apache.ambari.server.state.Clusters.class);
        m_alertHelper = m_injector.getInstance(org.apache.ambari.server.orm.AlertDaoHelper.class);
        org.apache.ambari.server.utils.EventBusSynchronizer.synchronizeAmbariEventPublisher(m_injector);
        m_cluster = m_clusters.getClusterById(m_helper.createCluster());
        m_helper.initializeClusterWithStack(m_cluster);
    }

    @org.junit.After
    public void teardown() throws java.lang.Exception {
        m_injector.getInstance(com.google.inject.persist.UnitOfWork.class).end();
        org.apache.ambari.server.H2DatabaseCleaner.clearDatabase(m_injector.getProvider(javax.persistence.EntityManager.class).get());
    }

    private void initTestData() throws java.lang.Exception {
        java.util.Set<org.apache.ambari.server.orm.entities.AlertTargetEntity> targets = createTargets(1);
        for (int i = 0; i < 2; i++) {
            org.apache.ambari.server.orm.entities.AlertGroupEntity group = new org.apache.ambari.server.orm.entities.AlertGroupEntity();
            group.setDefault(false);
            group.setGroupName("Group Name " + i);
            group.setClusterId(m_cluster.getClusterId());
            for (org.apache.ambari.server.orm.entities.AlertTargetEntity alertTarget : targets) {
                group.addAlertTarget(alertTarget);
            }
            m_dao.create(group);
        }
    }

    @org.junit.Test
    public void testFindTargets() throws java.lang.Exception {
        initTestData();
        java.util.List<org.apache.ambari.server.orm.entities.AlertTargetEntity> targets = m_dao.findAllTargets();
        org.junit.Assert.assertNotNull(targets);
        org.junit.Assert.assertEquals(1, targets.size());
        java.util.List<java.lang.Long> ids = new java.util.ArrayList<>();
        ids.add(targets.get(0).getTargetId());
        ids.add(99999L);
        targets = m_dao.findTargetsById(ids);
        org.junit.Assert.assertEquals(1, targets.size());
        org.apache.ambari.server.orm.entities.AlertTargetEntity target = targets.get(0);
        org.apache.ambari.server.orm.entities.AlertTargetEntity actual = m_dao.findTargetByName(target.getTargetName());
        org.junit.Assert.assertEquals(target, actual);
    }

    @org.junit.Test
    public void testCreateAndFindAllGlobalTargets() throws java.lang.Exception {
        java.util.List<org.apache.ambari.server.orm.entities.AlertTargetEntity> targets = m_dao.findAllGlobalTargets();
        org.junit.Assert.assertNotNull(targets);
        org.junit.Assert.assertEquals(0, targets.size());
        org.apache.ambari.server.orm.entities.AlertTargetEntity target = m_helper.createGlobalAlertTarget();
        m_helper.createGlobalAlertTarget();
        m_helper.createGlobalAlertTarget();
        targets = m_dao.findAllGlobalTargets();
        org.junit.Assert.assertTrue(target.isGlobal());
        org.junit.Assert.assertEquals(3, targets.size());
        m_dao.findTargetByName(target.getTargetName());
        org.junit.Assert.assertTrue(target.isGlobal());
    }

    @org.junit.Test
    public void testFindGroups() throws java.lang.Exception {
        initTestData();
        java.util.List<org.apache.ambari.server.orm.entities.AlertGroupEntity> groups = m_dao.findAllGroups();
        org.junit.Assert.assertNotNull(groups);
        org.junit.Assert.assertEquals(2, groups.size());
        org.apache.ambari.server.orm.entities.AlertGroupEntity group = groups.get(1);
        org.apache.ambari.server.orm.entities.AlertGroupEntity actual = m_dao.findGroupByName(group.getClusterId(), group.getGroupName());
        org.junit.Assert.assertEquals(group, actual);
        java.util.List<java.lang.Long> ids = new java.util.ArrayList<>();
        ids.add(groups.get(0).getGroupId());
        ids.add(groups.get(1).getGroupId());
        ids.add(99999L);
        groups = m_dao.findGroupsById(ids);
        org.junit.Assert.assertEquals(2, groups.size());
        for (org.apache.ambari.server.orm.entities.AlertGroupEntity alertGroupEntity : groups) {
            org.junit.Assert.assertFalse(alertGroupEntity.isDefault());
        }
        org.apache.ambari.server.state.Cluster cluster = m_helper.buildNewCluster(m_clusters, m_serviceFactory, m_componentFactory, m_schFactory, org.apache.ambari.server.orm.dao.AlertDispatchDAOTest.HOSTNAME);
        org.apache.ambari.server.orm.entities.AlertGroupEntity hdfsGroup = m_dao.findDefaultServiceGroup(cluster.getClusterId(), "HDFS");
        org.junit.Assert.assertNotNull(hdfsGroup);
        org.junit.Assert.assertTrue(hdfsGroup.isDefault());
    }

    @org.junit.Test
    public void testCreateUpdateRemoveGroup() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.AlertTargetEntity target = m_helper.createAlertTarget();
        java.util.Set<org.apache.ambari.server.orm.entities.AlertTargetEntity> targets = new java.util.HashSet<>();
        targets.add(target);
        org.apache.ambari.server.orm.entities.AlertGroupEntity group = m_helper.createAlertGroup(m_cluster.getClusterId(), targets);
        org.apache.ambari.server.orm.entities.AlertGroupEntity actual = m_dao.findGroupById(group.getGroupId());
        org.junit.Assert.assertNotNull(group);
        org.junit.Assert.assertEquals(group.getGroupName(), actual.getGroupName());
        org.junit.Assert.assertEquals(group.isDefault(), actual.isDefault());
        org.junit.Assert.assertEquals(group.getAlertTargets(), actual.getAlertTargets());
        org.junit.Assert.assertEquals(group.getAlertDefinitions(), actual.getAlertDefinitions());
        org.apache.ambari.server.orm.entities.AlertGroupEntity group1 = m_helper.createAlertGroup(m_cluster.getClusterId(), null);
        java.lang.String groupName = group1.getGroupName();
        group1 = m_dao.findGroupById(group1.getGroupId());
        group1.setGroupName(groupName + "FOO");
        group1.setDefault(true);
        m_dao.merge(group1);
        group = m_dao.findGroupById(group1.getGroupId());
        org.junit.Assert.assertEquals(groupName + "FOO", group1.getGroupName());
        org.junit.Assert.assertEquals(true, group1.isDefault());
        org.junit.Assert.assertEquals(0, group1.getAlertDefinitions().size());
        org.junit.Assert.assertEquals(0, group1.getAlertTargets().size());
        group1.addAlertTarget(target);
        m_dao.merge(group);
        group1 = m_dao.findGroupById(group1.getGroupId());
        org.junit.Assert.assertEquals(targets, group1.getAlertTargets());
        m_dao.remove(group);
        group = m_dao.findGroupById(group.getGroupId());
        org.junit.Assert.assertNull(group);
        target = m_dao.findTargetById(target.getTargetId());
        org.junit.Assert.assertNotNull(target);
        org.junit.Assert.assertEquals(1, m_dao.findAllTargets().size());
    }

    @org.junit.Test
    public void testCreateAndRemoveTarget() throws java.lang.Exception {
        int targetCount = m_dao.findAllTargets().size();
        org.apache.ambari.server.orm.entities.AlertTargetEntity target = m_helper.createAlertTarget();
        java.util.Set<org.apache.ambari.server.orm.entities.AlertTargetEntity> targets = new java.util.HashSet<>();
        targets.add(target);
        org.apache.ambari.server.orm.entities.AlertGroupEntity group = m_helper.createAlertGroup(m_cluster.getClusterId(), targets);
        org.apache.ambari.server.orm.entities.AlertTargetEntity actual = m_dao.findTargetById(target.getTargetId());
        org.junit.Assert.assertNotNull(actual);
        org.junit.Assert.assertEquals(target.getTargetName(), actual.getTargetName());
        org.junit.Assert.assertEquals(target.getDescription(), actual.getDescription());
        org.junit.Assert.assertEquals(target.getNotificationType(), actual.getNotificationType());
        org.junit.Assert.assertEquals(target.getProperties(), actual.getProperties());
        org.junit.Assert.assertEquals(false, actual.isGlobal());
        org.junit.Assert.assertNotNull(actual.getAlertGroups());
        java.util.Iterator<org.apache.ambari.server.orm.entities.AlertGroupEntity> iterator = actual.getAlertGroups().iterator();
        org.apache.ambari.server.orm.entities.AlertGroupEntity actualGroup = iterator.next();
        org.junit.Assert.assertEquals(group, actualGroup);
        org.junit.Assert.assertEquals(targetCount + 1, m_dao.findAllTargets().size());
        m_dao.remove(target);
        target = m_dao.findTargetById(target.getTargetId());
        org.junit.Assert.assertNull(target);
    }

    @org.junit.Test
    public void testGlobalTargetAssociations() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.AlertGroupEntity group = m_helper.createAlertGroup(m_cluster.getClusterId(), null);
        group = m_dao.findGroupById(group.getGroupId());
        org.junit.Assert.assertNotNull(group);
        org.junit.Assert.assertEquals(0, group.getAlertTargets().size());
        org.apache.ambari.server.orm.entities.AlertTargetEntity target = m_helper.createGlobalAlertTarget();
        org.junit.Assert.assertTrue(target.isGlobal());
        group = m_dao.findGroupById(group.getGroupId());
        org.junit.Assert.assertNotNull(group);
        org.junit.Assert.assertEquals(1, group.getAlertTargets().size());
        java.util.List<org.apache.ambari.server.orm.entities.AlertGroupEntity> groups = m_dao.findAllGroups();
        target = m_dao.findTargetById(target.getTargetId());
        org.junit.Assert.assertEquals(groups.size(), target.getAlertGroups().size());
        m_dao.remove(target);
        group = m_dao.findGroupById(group.getGroupId());
        org.junit.Assert.assertNotNull(group);
        org.junit.Assert.assertEquals(0, group.getAlertTargets().size());
    }

    @org.junit.Test
    public void testGlobalTargetAssociatedWithNewGroup() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.AlertTargetEntity target1 = m_helper.createGlobalAlertTarget();
        org.apache.ambari.server.orm.entities.AlertTargetEntity target2 = m_helper.createGlobalAlertTarget();
        org.junit.Assert.assertTrue(target1.isGlobal());
        org.junit.Assert.assertTrue(target2.isGlobal());
        org.apache.ambari.server.orm.entities.AlertGroupEntity group = m_helper.createAlertGroup(m_cluster.getClusterId(), null);
        group = m_dao.findGroupById(group.getGroupId());
        org.junit.Assert.assertNotNull(group);
        org.junit.Assert.assertEquals(2, group.getAlertTargets().size());
        java.util.Iterator<org.apache.ambari.server.orm.entities.AlertTargetEntity> iterator = group.getAlertTargets().iterator();
        org.apache.ambari.server.orm.entities.AlertTargetEntity groupTarget1 = iterator.next();
        org.apache.ambari.server.orm.entities.AlertTargetEntity groupTarget2 = iterator.next();
        org.junit.Assert.assertTrue(groupTarget1.isGlobal());
        org.junit.Assert.assertTrue(groupTarget2.isGlobal());
    }

    @org.junit.Test
    public void testDeleteTargetWithNotices() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.AlertTargetEntity target = m_helper.createAlertTarget();
        java.util.List<org.apache.ambari.server.orm.entities.AlertDefinitionEntity> definitions = createDefinitions();
        org.apache.ambari.server.orm.entities.AlertDefinitionEntity definition = definitions.get(0);
        org.apache.ambari.server.orm.entities.AlertHistoryEntity history = new org.apache.ambari.server.orm.entities.AlertHistoryEntity();
        history.setServiceName(definition.getServiceName());
        history.setClusterId(m_cluster.getClusterId());
        history.setAlertDefinition(definition);
        history.setAlertLabel("Label");
        history.setAlertState(org.apache.ambari.server.state.AlertState.OK);
        history.setAlertText("Alert Text");
        history.setAlertTimestamp(java.lang.System.currentTimeMillis());
        m_alertsDao.create(history);
        org.apache.ambari.server.orm.entities.AlertNoticeEntity notice = new org.apache.ambari.server.orm.entities.AlertNoticeEntity();
        notice.setUuid(java.util.UUID.randomUUID().toString());
        notice.setAlertTarget(target);
        notice.setAlertHistory(history);
        notice.setNotifyState(org.apache.ambari.server.state.NotificationState.PENDING);
        m_dao.create(notice);
        notice = m_dao.findNoticeById(notice.getNotificationId());
        org.junit.Assert.assertEquals(target.getTargetId(), notice.getAlertTarget().getTargetId());
        target = m_dao.findTargetById(target.getTargetId());
        m_dao.remove(target);
        notice = m_dao.findNoticeById(notice.getNotificationId());
        org.junit.Assert.assertNull(notice);
    }

    @org.junit.Test
    public void testDeleteAssociatedTarget() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.AlertTargetEntity target = m_helper.createAlertTarget();
        java.util.Set<org.apache.ambari.server.orm.entities.AlertTargetEntity> targets = new java.util.HashSet<>();
        targets.add(target);
        org.apache.ambari.server.orm.entities.AlertGroupEntity group = m_helper.createAlertGroup(m_cluster.getClusterId(), targets);
        org.junit.Assert.assertEquals(1, group.getAlertTargets().size());
        target = m_dao.findTargetById(target.getTargetId());
        m_dao.refresh(target);
        org.junit.Assert.assertNotNull(target);
        org.junit.Assert.assertEquals(1, target.getAlertGroups().size());
        m_dao.remove(target);
        target = m_dao.findTargetById(target.getTargetId());
        org.junit.Assert.assertNull(target);
        group = m_dao.findGroupById(group.getGroupId());
        org.junit.Assert.assertNotNull(group);
        org.junit.Assert.assertEquals(0, group.getAlertTargets().size());
    }

    @org.junit.Test
    public void testFindGroupsByDefinition() throws java.lang.Exception {
        java.util.List<org.apache.ambari.server.orm.entities.AlertDefinitionEntity> definitions = createDefinitions();
        org.apache.ambari.server.orm.entities.AlertGroupEntity group = m_helper.createAlertGroup(m_cluster.getClusterId(), null);
        group = m_dao.findGroupById(group.getGroupId());
        org.junit.Assert.assertNotNull(group);
        for (org.apache.ambari.server.orm.entities.AlertDefinitionEntity definition : definitions) {
            group.addAlertDefinition(definition);
        }
        m_dao.merge(group);
        group = m_dao.findGroupByName(m_cluster.getClusterId(), group.getGroupName());
        org.junit.Assert.assertEquals(definitions.size(), group.getAlertDefinitions().size());
        for (org.apache.ambari.server.orm.entities.AlertDefinitionEntity definition : definitions) {
            java.util.List<org.apache.ambari.server.orm.entities.AlertGroupEntity> groups = m_dao.findGroupsByDefinition(definition);
            org.junit.Assert.assertEquals(2, groups.size());
        }
    }

    @org.junit.Test
    public void testFindTargetsViaGroupsByDefinition() throws java.lang.Exception {
        initTestData();
        java.util.List<org.apache.ambari.server.orm.entities.AlertDefinitionEntity> definitions = createDefinitions();
        org.apache.ambari.server.orm.entities.AlertGroupEntity group = m_helper.createAlertGroup(m_cluster.getClusterId(), null);
        group = m_dao.findGroupById(group.getGroupId());
        org.junit.Assert.assertNotNull(group);
        org.apache.ambari.server.orm.entities.AlertDefinitionEntity definition = definitions.get(0);
        group.addAlertDefinition(definition);
        m_dao.merge(group);
        java.util.List<org.apache.ambari.server.orm.entities.AlertTargetEntity> targets = m_dao.findAllTargets();
        org.apache.ambari.server.orm.entities.AlertTargetEntity target = targets.get(0);
        java.util.Set<org.apache.ambari.server.orm.entities.AlertTargetEntity> setTargets = java.util.Collections.singleton(target);
        group.setAlertTargets(setTargets);
        m_dao.merge(group);
        java.util.List<org.apache.ambari.server.orm.entities.AlertGroupEntity> groups = m_dao.findGroupsByDefinition(definition);
        org.junit.Assert.assertEquals(2, groups.size());
        group = groups.get(groups.indexOf(group));
        org.junit.Assert.assertEquals(1, group.getAlertTargets().size());
        org.junit.Assert.assertEquals(target.getTargetId(), group.getAlertTargets().iterator().next().getTargetId());
    }

    @org.junit.Test
    public void testFindNoticeByUuid() throws java.lang.Exception {
        java.util.List<org.apache.ambari.server.orm.entities.AlertDefinitionEntity> definitions = createDefinitions();
        org.apache.ambari.server.orm.entities.AlertDefinitionEntity definition = definitions.get(0);
        org.apache.ambari.server.orm.entities.AlertHistoryEntity history = new org.apache.ambari.server.orm.entities.AlertHistoryEntity();
        history.setServiceName(definition.getServiceName());
        history.setClusterId(m_cluster.getClusterId());
        history.setAlertDefinition(definition);
        history.setAlertLabel("Label");
        history.setAlertState(org.apache.ambari.server.state.AlertState.OK);
        history.setAlertText("Alert Text");
        history.setAlertTimestamp(java.lang.System.currentTimeMillis());
        m_alertsDao.create(history);
        org.apache.ambari.server.orm.entities.AlertTargetEntity target = m_helper.createAlertTarget();
        org.apache.ambari.server.orm.entities.AlertNoticeEntity notice = new org.apache.ambari.server.orm.entities.AlertNoticeEntity();
        notice.setUuid(java.util.UUID.randomUUID().toString());
        notice.setAlertTarget(target);
        notice.setAlertHistory(history);
        notice.setNotifyState(org.apache.ambari.server.state.NotificationState.PENDING);
        m_dao.create(notice);
        org.apache.ambari.server.orm.entities.AlertNoticeEntity actual = m_dao.findNoticeByUuid(notice.getUuid());
        org.junit.Assert.assertEquals(notice.getNotificationId(), actual.getNotificationId());
        org.junit.Assert.assertNull(m_dao.findNoticeByUuid("DEADBEEF"));
    }

    @org.junit.Test
    public void testAlertNoticePredicate() throws java.lang.Exception {
        m_helper.addHost(m_clusters, m_cluster, org.apache.ambari.server.orm.dao.AlertDispatchDAOTest.HOSTNAME);
        m_helper.installHdfsService(m_cluster, m_serviceFactory, m_componentFactory, m_schFactory, org.apache.ambari.server.orm.dao.AlertDispatchDAOTest.HOSTNAME);
        m_helper.installYarnService(m_cluster, m_serviceFactory, m_componentFactory, m_schFactory, org.apache.ambari.server.orm.dao.AlertDispatchDAOTest.HOSTNAME);
        m_alertHelper.populateData(m_cluster);
        org.apache.ambari.server.controller.spi.Predicate clusterPredicate = null;
        org.apache.ambari.server.controller.spi.Predicate hdfsPredicate = null;
        org.apache.ambari.server.controller.spi.Predicate yarnPredicate = null;
        org.apache.ambari.server.controller.spi.Predicate adminPredicate = null;
        org.apache.ambari.server.controller.spi.Predicate adminOrOperatorPredicate = null;
        org.apache.ambari.server.controller.spi.Predicate pendingPredicate = null;
        org.apache.ambari.server.controller.spi.Predicate noticeIdPredicate = null;
        clusterPredicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.AlertNoticeResourceProvider.ALERT_NOTICE_CLUSTER_NAME).equals("c1").toPredicate();
        hdfsPredicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.AlertNoticeResourceProvider.ALERT_NOTICE_SERVICE_NAME).equals("HDFS").toPredicate();
        yarnPredicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.AlertNoticeResourceProvider.ALERT_NOTICE_SERVICE_NAME).equals("YARN").toPredicate();
        adminPredicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.AlertNoticeResourceProvider.ALERT_NOTICE_TARGET_NAME).equals("Administrators").toPredicate();
        adminOrOperatorPredicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.AlertNoticeResourceProvider.ALERT_NOTICE_TARGET_NAME).equals("Administrators").or().property(org.apache.ambari.server.controller.internal.AlertNoticeResourceProvider.ALERT_NOTICE_TARGET_NAME).equals("Operators").toPredicate();
        pendingPredicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.AlertNoticeResourceProvider.ALERT_NOTICE_STATE).equals(org.apache.ambari.server.state.NotificationState.PENDING.name()).toPredicate();
        org.apache.ambari.server.controller.AlertNoticeRequest request = new org.apache.ambari.server.controller.AlertNoticeRequest();
        request.Predicate = clusterPredicate;
        java.util.List<org.apache.ambari.server.orm.entities.AlertNoticeEntity> notices = m_dao.findAllNotices(request);
        org.junit.Assert.assertEquals(3, notices.size());
        request.Predicate = hdfsPredicate;
        notices = m_dao.findAllNotices(request);
        org.junit.Assert.assertEquals(2, notices.size());
        request.Predicate = yarnPredicate;
        notices = m_dao.findAllNotices(request);
        org.junit.Assert.assertEquals(1, notices.size());
        request.Predicate = adminPredicate;
        notices = m_dao.findAllNotices(request);
        org.junit.Assert.assertEquals(2, notices.size());
        request.Predicate = adminOrOperatorPredicate;
        notices = m_dao.findAllNotices(request);
        org.junit.Assert.assertEquals(3, notices.size());
        request.Predicate = pendingPredicate;
        notices = m_dao.findAllNotices(request);
        org.junit.Assert.assertEquals(1, notices.size());
        noticeIdPredicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.AlertNoticeResourceProvider.ALERT_NOTICE_ID).equals(notices.get(0).getNotificationId()).toPredicate();
        request.Predicate = noticeIdPredicate;
        notices = m_dao.findAllNotices(request);
        org.junit.Assert.assertEquals(1, notices.size());
    }

    @org.junit.Test
    public void testAlertNoticePagination() throws java.lang.Exception {
        m_helper.addHost(m_clusters, m_cluster, org.apache.ambari.server.orm.dao.AlertDispatchDAOTest.HOSTNAME);
        m_helper.installHdfsService(m_cluster, m_serviceFactory, m_componentFactory, m_schFactory, org.apache.ambari.server.orm.dao.AlertDispatchDAOTest.HOSTNAME);
        m_helper.installYarnService(m_cluster, m_serviceFactory, m_componentFactory, m_schFactory, org.apache.ambari.server.orm.dao.AlertDispatchDAOTest.HOSTNAME);
        m_alertHelper.populateData(m_cluster);
        org.apache.ambari.server.controller.AlertNoticeRequest request = new org.apache.ambari.server.controller.AlertNoticeRequest();
        request.Pagination = null;
        java.util.List<org.apache.ambari.server.orm.entities.AlertNoticeEntity> notices = m_dao.findAllNotices(request);
        org.junit.Assert.assertEquals(3, notices.size());
        request.Pagination = new org.apache.ambari.server.controller.internal.PageRequestImpl(org.apache.ambari.server.controller.spi.PageRequest.StartingPoint.Beginning, 2, 0, null, null);
        notices = m_dao.findAllNotices(request);
        org.junit.Assert.assertEquals(2, notices.size());
        request.Pagination = new org.apache.ambari.server.controller.internal.PageRequestImpl(org.apache.ambari.server.controller.spi.PageRequest.StartingPoint.Beginning, 1, 2, null, null);
        notices = m_dao.findAllNotices(request);
        org.junit.Assert.assertEquals(1, notices.size());
        request.Pagination = new org.apache.ambari.server.controller.internal.PageRequestImpl(org.apache.ambari.server.controller.spi.PageRequest.StartingPoint.Beginning, 1, 3, null, null);
        notices = m_dao.findAllNotices(request);
        org.junit.Assert.assertEquals(0, notices.size());
    }

    @org.junit.Test
    public void testAlertNoticeSorting() throws java.lang.Exception {
        m_helper.addHost(m_clusters, m_cluster, org.apache.ambari.server.orm.dao.AlertDispatchDAOTest.HOSTNAME);
        m_helper.installHdfsService(m_cluster, m_serviceFactory, m_componentFactory, m_schFactory, org.apache.ambari.server.orm.dao.AlertDispatchDAOTest.HOSTNAME);
        m_helper.installYarnService(m_cluster, m_serviceFactory, m_componentFactory, m_schFactory, org.apache.ambari.server.orm.dao.AlertDispatchDAOTest.HOSTNAME);
        m_alertHelper.populateData(m_cluster);
        java.util.List<org.apache.ambari.server.controller.spi.SortRequestProperty> sortProperties = new java.util.ArrayList<>();
        org.apache.ambari.server.controller.spi.SortRequest sortRequest = new org.apache.ambari.server.controller.internal.SortRequestImpl(sortProperties);
        org.apache.ambari.server.controller.AlertNoticeRequest request = new org.apache.ambari.server.controller.AlertNoticeRequest();
        request.Sort = sortRequest;
        org.apache.ambari.server.controller.spi.Predicate clusterPredicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.AlertNoticeResourceProvider.ALERT_NOTICE_CLUSTER_NAME).equals("c1").toPredicate();
        request.Predicate = clusterPredicate;
        sortProperties.add(new org.apache.ambari.server.controller.spi.SortRequestProperty(org.apache.ambari.server.controller.internal.AlertNoticeResourceProvider.ALERT_NOTICE_ID, org.apache.ambari.server.controller.spi.SortRequest.Order.ASC));
        java.util.List<org.apache.ambari.server.orm.entities.AlertNoticeEntity> notices = m_dao.findAllNotices(request);
        org.junit.Assert.assertEquals(3, notices.size());
        long lastId = 0L;
        for (org.apache.ambari.server.orm.entities.AlertNoticeEntity notice : notices) {
            if (lastId == 0L) {
                lastId = notice.getNotificationId();
                continue;
            }
            long currentId = notice.getNotificationId();
            org.junit.Assert.assertTrue(lastId < currentId);
            lastId = currentId;
        }
        sortProperties.clear();
        sortProperties.add(new org.apache.ambari.server.controller.spi.SortRequestProperty(org.apache.ambari.server.controller.internal.AlertNoticeResourceProvider.ALERT_NOTICE_ID, org.apache.ambari.server.controller.spi.SortRequest.Order.DESC));
        notices = m_dao.findAllNotices(request);
        org.junit.Assert.assertEquals(3, notices.size());
        lastId = 0L;
        for (org.apache.ambari.server.orm.entities.AlertNoticeEntity notice : notices) {
            if (lastId == 0L) {
                lastId = notice.getNotificationId();
                continue;
            }
            long currentId = notice.getNotificationId();
            org.junit.Assert.assertTrue(lastId > currentId);
            lastId = currentId;
        }
    }

    @org.junit.Test
    public void testDefaultGroupAutomaticCreation() throws java.lang.Exception {
        m_helper.addHost(m_clusters, m_cluster, org.apache.ambari.server.orm.dao.AlertDispatchDAOTest.HOSTNAME);
        m_helper.installHdfsService(m_cluster, m_serviceFactory, m_componentFactory, m_schFactory, org.apache.ambari.server.orm.dao.AlertDispatchDAOTest.HOSTNAME);
        org.apache.ambari.server.orm.entities.AlertGroupEntity hdfsGroup = m_dao.findDefaultServiceGroup(m_cluster.getClusterId(), "HDFS");
        m_dao.remove(hdfsGroup);
        hdfsGroup = m_dao.findDefaultServiceGroup(m_cluster.getClusterId(), "HDFS");
        org.junit.Assert.assertNull(hdfsGroup);
        org.apache.ambari.server.orm.entities.AlertDefinitionEntity datanodeProcess = new org.apache.ambari.server.orm.entities.AlertDefinitionEntity();
        datanodeProcess.setClusterId(m_cluster.getClusterId());
        datanodeProcess.setDefinitionName("datanode_process");
        datanodeProcess.setServiceName("HDFS");
        datanodeProcess.setComponentName("DATANODE");
        datanodeProcess.setHash(java.util.UUID.randomUUID().toString());
        datanodeProcess.setScheduleInterval(60);
        datanodeProcess.setScope(org.apache.ambari.server.state.alert.Scope.SERVICE);
        datanodeProcess.setSource("{\"type\" : \"SCRIPT\"}");
        datanodeProcess.setSourceType(org.apache.ambari.server.state.alert.SourceType.SCRIPT);
        m_definitionDao.create(datanodeProcess);
        hdfsGroup = m_dao.findDefaultServiceGroup(m_cluster.getClusterId(), "HDFS");
        org.junit.Assert.assertNotNull(hdfsGroup);
        org.junit.Assert.assertTrue(hdfsGroup.isDefault());
    }

    @org.junit.Test(expected = org.apache.ambari.server.AmbariException.class)
    public void testDefaultGroupInvalidServiceNoCreation() throws java.lang.Exception {
        initTestData();
        m_helper.addHost(m_clusters, m_cluster, org.apache.ambari.server.orm.dao.AlertDispatchDAOTest.HOSTNAME);
        m_helper.installHdfsService(m_cluster, m_serviceFactory, m_componentFactory, m_schFactory, org.apache.ambari.server.orm.dao.AlertDispatchDAOTest.HOSTNAME);
        org.junit.Assert.assertEquals(3, m_dao.findAllGroups().size());
        org.apache.ambari.server.orm.entities.AlertDefinitionEntity datanodeProcess = new org.apache.ambari.server.orm.entities.AlertDefinitionEntity();
        datanodeProcess.setClusterId(m_cluster.getClusterId());
        datanodeProcess.setDefinitionName("datanode_process");
        datanodeProcess.setServiceName("INVALID");
        datanodeProcess.setComponentName("DATANODE");
        datanodeProcess.setHash(java.util.UUID.randomUUID().toString());
        datanodeProcess.setScheduleInterval(60);
        datanodeProcess.setScope(org.apache.ambari.server.state.alert.Scope.SERVICE);
        datanodeProcess.setSource("{\"type\" : \"SCRIPT\"}");
        datanodeProcess.setSourceType(org.apache.ambari.server.state.alert.SourceType.SCRIPT);
        try {
            m_definitionDao.create(datanodeProcess);
        } finally {
            org.junit.Assert.assertEquals(3, m_dao.findAllGroups().size());
        }
    }

    private java.util.List<org.apache.ambari.server.orm.entities.AlertDefinitionEntity> createDefinitions() throws java.lang.Exception {
        m_helper.addHost(m_clusters, m_cluster, org.apache.ambari.server.orm.dao.AlertDispatchDAOTest.HOSTNAME);
        m_helper.installYarnService(m_cluster, m_serviceFactory, m_componentFactory, m_schFactory, org.apache.ambari.server.orm.dao.AlertDispatchDAOTest.HOSTNAME);
        java.util.List<org.apache.ambari.server.orm.entities.AlertDefinitionEntity> alertDefinitionEntities = new java.util.ArrayList<>();
        for (int i = 0; i < 2; i++) {
            org.apache.ambari.server.orm.entities.AlertDefinitionEntity definition = new org.apache.ambari.server.orm.entities.AlertDefinitionEntity();
            definition.setDefinitionName("Alert Definition " + i);
            definition.setServiceName("YARN");
            definition.setComponentName(null);
            definition.setClusterId(m_cluster.getClusterId());
            definition.setHash(java.util.UUID.randomUUID().toString());
            definition.setScheduleInterval(60);
            definition.setScope(org.apache.ambari.server.state.alert.Scope.SERVICE);
            definition.setSource("{\"type\" : \"SCRIPT\"}");
            definition.setSourceType(org.apache.ambari.server.state.alert.SourceType.SCRIPT);
            m_definitionDao.create(definition);
            alertDefinitionEntities.add(definition);
        }
        return alertDefinitionEntities;
    }

    private java.util.Set<org.apache.ambari.server.orm.entities.AlertTargetEntity> createTargets(int numberOfTargets) throws java.lang.Exception {
        java.util.Set<org.apache.ambari.server.orm.entities.AlertTargetEntity> targets = new java.util.HashSet<>();
        for (int i = 0; i < numberOfTargets; i++) {
            org.apache.ambari.server.orm.entities.AlertTargetEntity target = new org.apache.ambari.server.orm.entities.AlertTargetEntity();
            target.setDescription("Target Description " + i);
            target.setNotificationType("EMAIL");
            target.setProperties("Target Properties " + i);
            target.setTargetName("Target Name " + i);
            m_dao.create(target);
            targets.add(target);
        }
        return targets;
    }

    @org.junit.Test
    public void testGroupDefinitions() throws java.lang.Exception {
        java.util.List<org.apache.ambari.server.orm.entities.AlertDefinitionEntity> definitions = createDefinitions();
        org.apache.ambari.server.orm.entities.AlertGroupEntity group = m_helper.createAlertGroup(m_cluster.getClusterId(), null);
        group = m_dao.findGroupById(group.getGroupId());
        org.junit.Assert.assertNotNull(group);
        for (org.apache.ambari.server.orm.entities.AlertDefinitionEntity definition : definitions) {
            group.addAlertDefinition(definition);
        }
        m_dao.merge(group);
        group = m_dao.findGroupByName(m_cluster.getClusterId(), group.getGroupName());
        org.junit.Assert.assertEquals(definitions.size(), group.getAlertDefinitions().size());
        for (org.apache.ambari.server.orm.entities.AlertDefinitionEntity definition : definitions) {
            org.junit.Assert.assertTrue(group.getAlertDefinitions().contains(definition));
        }
        m_definitionDao.refresh(definitions.get(0));
        m_definitionDao.remove(definitions.get(0));
        definitions.remove(0);
        group = m_dao.findGroupByName(m_cluster.getClusterId(), group.getGroupName());
        org.junit.Assert.assertEquals(definitions.size(), group.getAlertDefinitions().size());
        for (org.apache.ambari.server.orm.entities.AlertDefinitionEntity definition : definitions) {
            org.junit.Assert.assertTrue(group.getAlertDefinitions().contains(definition));
        }
    }

    @org.junit.Test
    public void testConcurrentGroupModification() throws java.lang.Exception {
        createDefinitions();
        org.apache.ambari.server.orm.entities.AlertGroupEntity group = m_helper.createAlertGroup(m_cluster.getClusterId(), null);
        final java.util.Set<org.apache.ambari.server.orm.entities.AlertTargetEntity> targets = createTargets(100);
        group.setAlertTargets(targets);
        group = m_dao.merge(group);
        final class AlertGroupWriterThread extends java.lang.Thread {
            private org.apache.ambari.server.orm.entities.AlertGroupEntity group;

            @java.lang.Override
            public void run() {
                for (int i = 0; i < 1000; i++) {
                    group.setAlertTargets(new java.util.HashSet<>(targets));
                }
            }
        }
        java.util.List<java.lang.Thread> threads = new java.util.ArrayList<>();
        for (int i = 0; i < 5; i++) {
            AlertGroupWriterThread thread = new AlertGroupWriterThread();
            threads.add(thread);
            thread.group = group;
            thread.start();
        }
        for (java.lang.Thread thread : threads) {
            thread.join();
        }
    }
}