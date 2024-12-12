package org.apache.ambari.server.orm.dao;
import com.google.inject.persist.UnitOfWork;
import javax.persistence.EntityManager;
import org.easymock.EasyMock;
public class AlertsDAOTest {
    static final java.lang.String HOSTNAME = "c6401.ambari.apache.org";

    static final java.util.Calendar calendar = java.util.Calendar.getInstance(java.util.TimeZone.getTimeZone("UTC"));

    private org.apache.ambari.server.state.Clusters m_clusters;

    private org.apache.ambari.server.state.Cluster m_cluster;

    private com.google.inject.Injector m_injector;

    private org.apache.ambari.server.orm.OrmTestHelper m_helper;

    private org.apache.ambari.server.orm.dao.AlertsDAO m_dao;

    private org.apache.ambari.server.orm.dao.AlertDefinitionDAO m_definitionDao;

    private org.apache.ambari.server.state.ServiceFactory m_serviceFactory;

    private org.apache.ambari.server.state.ServiceComponentFactory m_componentFactory;

    private org.apache.ambari.server.state.ServiceComponentHostFactory m_schFactory;

    private org.apache.ambari.server.orm.AlertDaoHelper m_alertHelper;

    @org.junit.Before
    public void setup() throws java.lang.Exception {
        m_injector = com.google.inject.Guice.createInjector(com.google.inject.util.Modules.override(new org.apache.ambari.server.orm.InMemoryDefaultTestModule()).with(new org.apache.ambari.server.orm.dao.AlertsDAOTest.MockModule()));
        m_injector.getInstance(org.apache.ambari.server.orm.GuiceJpaInitializer.class);
        m_injector.getInstance(com.google.inject.persist.UnitOfWork.class).begin();
        m_helper = m_injector.getInstance(org.apache.ambari.server.orm.OrmTestHelper.class);
        m_dao = m_injector.getInstance(org.apache.ambari.server.orm.dao.AlertsDAO.class);
        m_definitionDao = m_injector.getInstance(org.apache.ambari.server.orm.dao.AlertDefinitionDAO.class);
        m_serviceFactory = m_injector.getInstance(org.apache.ambari.server.state.ServiceFactory.class);
        m_componentFactory = m_injector.getInstance(org.apache.ambari.server.state.ServiceComponentFactory.class);
        m_schFactory = m_injector.getInstance(org.apache.ambari.server.state.ServiceComponentHostFactory.class);
        m_clusters = m_injector.getInstance(org.apache.ambari.server.state.Clusters.class);
        m_alertHelper = m_injector.getInstance(org.apache.ambari.server.orm.AlertDaoHelper.class);
        org.apache.ambari.server.utils.EventBusSynchronizer.synchronizeAmbariEventPublisher(m_injector);
        org.apache.ambari.server.utils.EventBusSynchronizer.synchronizeAlertEventPublisher(m_injector);
        org.apache.ambari.server.utils.EventBusSynchronizer.synchronizeSTOMPUpdatePublisher(m_injector);
        m_cluster = m_clusters.getClusterById(m_helper.createCluster());
        m_helper.initializeClusterWithStack(m_cluster);
        m_helper.addHost(m_clusters, m_cluster, org.apache.ambari.server.orm.dao.AlertsDAOTest.HOSTNAME);
        m_helper.installYarnService(m_cluster, m_serviceFactory, m_componentFactory, m_schFactory, org.apache.ambari.server.orm.dao.AlertsDAOTest.HOSTNAME);
        for (int i = 0; i < 5; i++) {
            org.apache.ambari.server.orm.entities.AlertDefinitionEntity definition = new org.apache.ambari.server.orm.entities.AlertDefinitionEntity();
            definition.setDefinitionName("Alert Definition " + i);
            definition.setServiceName("YARN");
            definition.setComponentName("Component " + i);
            definition.setClusterId(m_cluster.getClusterId());
            definition.setHash(java.util.UUID.randomUUID().toString());
            definition.setScheduleInterval(java.lang.Integer.valueOf(60));
            definition.setScope(org.apache.ambari.server.state.alert.Scope.SERVICE);
            definition.setSource("{\"type\" : \"SCRIPT\"}");
            definition.setSourceType(org.apache.ambari.server.state.alert.SourceType.SCRIPT);
            m_definitionDao.create(definition);
        }
        java.util.List<org.apache.ambari.server.orm.entities.AlertDefinitionEntity> definitions = m_definitionDao.findAll();
        org.junit.Assert.assertNotNull(definitions);
        org.junit.Assert.assertEquals(5, definitions.size());
        org.apache.ambari.server.orm.dao.AlertsDAOTest.calendar.clear();
        org.apache.ambari.server.orm.dao.AlertsDAOTest.calendar.set(2014, java.util.Calendar.JANUARY, 1);
        for (org.apache.ambari.server.orm.entities.AlertDefinitionEntity definition : definitions) {
            for (int i = 0; i < 10; i++) {
                org.apache.ambari.server.orm.entities.AlertHistoryEntity history = new org.apache.ambari.server.orm.entities.AlertHistoryEntity();
                history.setServiceName(definition.getServiceName());
                history.setClusterId(m_cluster.getClusterId());
                history.setAlertDefinition(definition);
                history.setAlertLabel((definition.getDefinitionName() + " ") + i);
                history.setAlertText((definition.getDefinitionName() + " ") + i);
                history.setAlertTimestamp(org.apache.ambari.server.orm.dao.AlertsDAOTest.calendar.getTimeInMillis());
                history.setComponentName(definition.getComponentName());
                history.setHostName("h1");
                history.setAlertState(org.apache.ambari.server.state.AlertState.OK);
                if ((i == 0) || (i == 5)) {
                    history.setAlertState(org.apache.ambari.server.state.AlertState.CRITICAL);
                }
                org.apache.ambari.server.orm.dao.AlertsDAOTest.calendar.add(java.util.Calendar.DATE, 1);
                m_dao.create(history);
            }
        }
        for (org.apache.ambari.server.orm.entities.AlertDefinitionEntity definition : definitions) {
            java.util.List<org.apache.ambari.server.orm.entities.AlertHistoryEntity> alerts = m_dao.findAll();
            org.apache.ambari.server.orm.entities.AlertHistoryEntity history = null;
            for (org.apache.ambari.server.orm.entities.AlertHistoryEntity alert : alerts) {
                if (definition.equals(alert.getAlertDefinition())) {
                    history = alert;
                }
            }
            org.junit.Assert.assertNotNull(history);
            org.apache.ambari.server.orm.entities.AlertCurrentEntity current = new org.apache.ambari.server.orm.entities.AlertCurrentEntity();
            current.setAlertHistory(history);
            current.setLatestTimestamp(new java.util.Date().getTime());
            current.setOriginalTimestamp(new java.util.Date().getTime() - 10800000);
            current.setMaintenanceState(org.apache.ambari.server.state.MaintenanceState.OFF);
            m_dao.create(current);
        }
    }

    @org.junit.After
    public void teardown() throws java.lang.Exception {
        m_injector.getInstance(com.google.inject.persist.UnitOfWork.class).end();
        org.apache.ambari.server.H2DatabaseCleaner.clearDatabase(m_injector.getProvider(javax.persistence.EntityManager.class).get());
        m_injector = null;
    }

    @org.junit.Test
    public void testFindAll() {
        java.util.List<org.apache.ambari.server.orm.entities.AlertHistoryEntity> alerts = m_dao.findAll(m_cluster.getClusterId());
        org.junit.Assert.assertNotNull(alerts);
        org.junit.Assert.assertEquals(50, alerts.size());
    }

    @org.junit.Test
    public void testFindAllCurrent() {
        java.util.List<org.apache.ambari.server.orm.entities.AlertCurrentEntity> currentAlerts = m_dao.findCurrent();
        org.junit.Assert.assertNotNull(currentAlerts);
        org.junit.Assert.assertEquals(5, currentAlerts.size());
    }

    @org.junit.Test
    public void testFindCurrentByDefinitionId() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.AlertDefinitionEntity definition = new org.apache.ambari.server.orm.entities.AlertDefinitionEntity();
        definition.setDefinitionName("Foo Definition");
        definition.setServiceName("YARN");
        definition.setComponentName("NODEMANAGER");
        definition.setClusterId(m_cluster.getClusterId());
        definition.setHash(java.util.UUID.randomUUID().toString());
        definition.setScheduleInterval(java.lang.Integer.valueOf(60));
        definition.setScope(org.apache.ambari.server.state.alert.Scope.HOST);
        definition.setSource("{\"type\" : \"SCRIPT\"}");
        definition.setSourceType(org.apache.ambari.server.state.alert.SourceType.SCRIPT);
        m_definitionDao.create(definition);
        org.apache.ambari.server.orm.entities.AlertHistoryEntity history = new org.apache.ambari.server.orm.entities.AlertHistoryEntity();
        history.setServiceName(definition.getServiceName());
        history.setClusterId(m_cluster.getClusterId());
        history.setAlertDefinition(definition);
        history.setAlertLabel(definition.getDefinitionName());
        history.setAlertText(definition.getDefinitionName());
        history.setAlertTimestamp(java.lang.Long.valueOf(1L));
        history.setHostName("h1");
        history.setAlertState(org.apache.ambari.server.state.AlertState.OK);
        m_dao.create(history);
        org.apache.ambari.server.orm.entities.AlertCurrentEntity current = new org.apache.ambari.server.orm.entities.AlertCurrentEntity();
        current.setOriginalTimestamp(1L);
        current.setLatestTimestamp(2L);
        current.setAlertHistory(history);
        m_dao.create(current);
        java.util.List<org.apache.ambari.server.orm.entities.AlertCurrentEntity> currentAlerts = m_dao.findCurrentByDefinitionId(definition.getDefinitionId());
        org.junit.Assert.assertEquals(1, currentAlerts.size());
        org.apache.ambari.server.orm.entities.AlertHistoryEntity history2 = new org.apache.ambari.server.orm.entities.AlertHistoryEntity();
        history2.setServiceName(definition.getServiceName());
        history2.setClusterId(m_cluster.getClusterId());
        history2.setAlertDefinition(definition);
        history2.setAlertLabel(definition.getDefinitionName());
        history2.setAlertText(definition.getDefinitionName());
        history2.setAlertTimestamp(java.lang.Long.valueOf(1L));
        history2.setHostName("h2");
        history2.setAlertState(org.apache.ambari.server.state.AlertState.OK);
        m_dao.create(history);
        org.apache.ambari.server.orm.entities.AlertCurrentEntity current2 = new org.apache.ambari.server.orm.entities.AlertCurrentEntity();
        current2.setOriginalTimestamp(1L);
        current2.setLatestTimestamp(2L);
        current2.setAlertHistory(history2);
        m_dao.create(current2);
        currentAlerts = m_dao.findCurrentByDefinitionId(definition.getDefinitionId());
        org.junit.Assert.assertEquals(2, currentAlerts.size());
    }

    @org.junit.Test
    public void testFindCurrentByService() {
        java.util.List<org.apache.ambari.server.orm.entities.AlertCurrentEntity> currentAlerts = m_dao.findCurrent();
        int currentAlertExpectedCount = currentAlerts.size();
        org.junit.Assert.assertEquals(5, currentAlertExpectedCount);
        org.apache.ambari.server.orm.entities.AlertCurrentEntity current = currentAlerts.get(0);
        org.apache.ambari.server.orm.entities.AlertHistoryEntity history = current.getAlertHistory();
        org.junit.Assert.assertNotNull(history);
        currentAlerts = m_dao.findCurrentByService(m_cluster.getClusterId(), history.getServiceName());
        org.junit.Assert.assertNotNull(currentAlerts);
        org.junit.Assert.assertEquals(currentAlertExpectedCount, currentAlerts.size());
        currentAlerts = m_dao.findCurrentByService(m_cluster.getClusterId(), "foo");
        org.junit.Assert.assertNotNull(currentAlerts);
        org.junit.Assert.assertEquals(0, currentAlerts.size());
    }

    @org.junit.Test
    public void testFindCurrentByHost() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.AlertDefinitionEntity hostDef = new org.apache.ambari.server.orm.entities.AlertDefinitionEntity();
        hostDef.setDefinitionName("Host Alert Definition ");
        hostDef.setServiceName("YARN");
        hostDef.setComponentName(null);
        hostDef.setClusterId(m_cluster.getClusterId());
        hostDef.setHash(java.util.UUID.randomUUID().toString());
        hostDef.setScheduleInterval(java.lang.Integer.valueOf(60));
        hostDef.setScope(org.apache.ambari.server.state.alert.Scope.HOST);
        hostDef.setSource("{\"type\" : \"SCRIPT\"}");
        hostDef.setSourceType(org.apache.ambari.server.state.alert.SourceType.SCRIPT);
        m_definitionDao.create(hostDef);
        org.apache.ambari.server.orm.entities.AlertHistoryEntity history = new org.apache.ambari.server.orm.entities.AlertHistoryEntity();
        history.setServiceName(hostDef.getServiceName());
        history.setClusterId(m_cluster.getClusterId());
        history.setAlertDefinition(hostDef);
        history.setAlertLabel(hostDef.getDefinitionName());
        history.setAlertText(hostDef.getDefinitionName());
        history.setAlertTimestamp(java.lang.Long.valueOf(1L));
        history.setHostName(org.apache.ambari.server.orm.dao.AlertsDAOTest.HOSTNAME);
        history.setAlertState(org.apache.ambari.server.state.AlertState.OK);
        m_dao.create(history);
        org.apache.ambari.server.orm.entities.AlertCurrentEntity current = new org.apache.ambari.server.orm.entities.AlertCurrentEntity();
        current.setOriginalTimestamp(1L);
        current.setLatestTimestamp(2L);
        current.setAlertHistory(history);
        m_dao.create(current);
        org.apache.ambari.server.controller.spi.Predicate hostPredicate = null;
        hostPredicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.AlertResourceProvider.ALERT_HOST).equals(org.apache.ambari.server.orm.dao.AlertsDAOTest.HOSTNAME).toPredicate();
        org.apache.ambari.server.controller.AlertCurrentRequest request = new org.apache.ambari.server.controller.AlertCurrentRequest();
        request.Predicate = hostPredicate;
        java.util.List<org.apache.ambari.server.orm.entities.AlertCurrentEntity> currentAlerts = m_dao.findAll(request);
        org.junit.Assert.assertNotNull(currentAlerts);
        org.junit.Assert.assertEquals(1, currentAlerts.size());
        hostPredicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.AlertResourceProvider.ALERT_HOST).equals("invalid.apache.org").toPredicate();
        request = new org.apache.ambari.server.controller.AlertCurrentRequest();
        request.Predicate = hostPredicate;
        currentAlerts = m_dao.findAll(request);
        org.junit.Assert.assertNotNull(currentAlerts);
        org.junit.Assert.assertEquals(0, currentAlerts.size());
    }

    @org.junit.Test
    public void testAlertCurrentPredicate() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.AlertDefinitionEntity definition = m_definitionDao.findByName(m_cluster.getClusterId(), "Alert Definition 0");
        org.junit.Assert.assertNotNull(definition);
        org.apache.ambari.server.controller.spi.Predicate definitionIdPredicate = null;
        org.apache.ambari.server.controller.spi.Predicate hdfsPredicate = null;
        org.apache.ambari.server.controller.spi.Predicate yarnPredicate = null;
        definitionIdPredicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.AlertResourceProvider.ALERT_DEFINITION_ID).equals(definition.getDefinitionId()).toPredicate();
        org.apache.ambari.server.controller.AlertCurrentRequest request = new org.apache.ambari.server.controller.AlertCurrentRequest();
        request.Predicate = definitionIdPredicate;
        java.util.List<org.apache.ambari.server.orm.entities.AlertCurrentEntity> currentAlerts = m_dao.findAll(request);
        org.junit.Assert.assertEquals(1, currentAlerts.size());
        hdfsPredicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.AlertResourceProvider.ALERT_SERVICE).equals("HDFS").toPredicate();
        yarnPredicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.AlertResourceProvider.ALERT_SERVICE).equals("YARN").toPredicate();
        request.Predicate = yarnPredicate;
        currentAlerts = m_dao.findAll(request);
        org.junit.Assert.assertEquals(5, currentAlerts.size());
        request.Predicate = hdfsPredicate;
        currentAlerts = m_dao.findAll(request);
        org.junit.Assert.assertEquals(0, currentAlerts.size());
    }

    @org.junit.Test
    public void testAlertCurrentSorting() throws java.lang.Exception {
        org.apache.ambari.server.controller.AlertCurrentRequest request = new org.apache.ambari.server.controller.AlertCurrentRequest();
        org.apache.ambari.server.controller.spi.Predicate clusterPredicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.AlertResourceProvider.ALERT_CLUSTER_NAME).equals(m_cluster.getClusterName()).toPredicate();
        request.Predicate = clusterPredicate;
        org.apache.ambari.server.controller.spi.SortRequestProperty sortRequestProperty = new org.apache.ambari.server.controller.spi.SortRequestProperty(org.apache.ambari.server.controller.internal.AlertResourceProvider.ALERT_ID, org.apache.ambari.server.controller.spi.SortRequest.Order.ASC);
        request.Sort = new org.apache.ambari.server.controller.internal.SortRequestImpl(java.util.Collections.singletonList(sortRequestProperty));
        java.util.List<org.apache.ambari.server.orm.entities.AlertCurrentEntity> currentAlerts = m_dao.findAll(request);
        org.junit.Assert.assertTrue(currentAlerts.size() >= 5);
        long lastId = java.lang.Long.MIN_VALUE;
        for (org.apache.ambari.server.orm.entities.AlertCurrentEntity alert : currentAlerts) {
            org.junit.Assert.assertTrue(lastId < alert.getAlertId());
            lastId = alert.getAlertId();
        }
        sortRequestProperty = new org.apache.ambari.server.controller.spi.SortRequestProperty(org.apache.ambari.server.controller.internal.AlertResourceProvider.ALERT_ID, org.apache.ambari.server.controller.spi.SortRequest.Order.DESC);
        request.Sort = new org.apache.ambari.server.controller.internal.SortRequestImpl(java.util.Collections.singletonList(sortRequestProperty));
        currentAlerts = m_dao.findAll(request);
        org.junit.Assert.assertTrue(currentAlerts.size() >= 5);
        lastId = java.lang.Long.MAX_VALUE;
        for (org.apache.ambari.server.orm.entities.AlertCurrentEntity alert : currentAlerts) {
            org.junit.Assert.assertTrue(lastId > alert.getAlertId());
            lastId = alert.getAlertId();
        }
    }

    @org.junit.Test
    public void testAlertCurrentUpdatesViaHistory() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.AlertDefinitionEntity hostDef = new org.apache.ambari.server.orm.entities.AlertDefinitionEntity();
        hostDef.setDefinitionName("Host Alert Definition ");
        hostDef.setServiceName("YARN");
        hostDef.setComponentName(null);
        hostDef.setClusterId(m_cluster.getClusterId());
        hostDef.setHash(java.util.UUID.randomUUID().toString());
        hostDef.setScheduleInterval(java.lang.Integer.valueOf(60));
        hostDef.setScope(org.apache.ambari.server.state.alert.Scope.HOST);
        hostDef.setSource("{\"type\" : \"SCRIPT\"}");
        hostDef.setSourceType(org.apache.ambari.server.state.alert.SourceType.SCRIPT);
        m_definitionDao.create(hostDef);
        org.apache.ambari.server.orm.entities.AlertHistoryEntity history = new org.apache.ambari.server.orm.entities.AlertHistoryEntity();
        history.setServiceName(hostDef.getServiceName());
        history.setClusterId(m_cluster.getClusterId());
        history.setAlertDefinition(hostDef);
        history.setAlertLabel(hostDef.getDefinitionName());
        history.setAlertText(hostDef.getDefinitionName());
        history.setAlertTimestamp(java.lang.Long.valueOf(1L));
        history.setHostName("h2");
        history.setAlertState(org.apache.ambari.server.state.AlertState.OK);
        m_dao.create(history);
        org.apache.ambari.server.orm.entities.AlertCurrentEntity current = new org.apache.ambari.server.orm.entities.AlertCurrentEntity();
        current.setOriginalTimestamp(1L);
        current.setLatestTimestamp(2L);
        current.setAlertHistory(history);
        m_dao.create(current);
        org.junit.Assert.assertEquals(history.getAlertText(), current.getLatestText());
        history.setAlertText("foobar!");
        current.setAlertHistory(history);
        org.junit.Assert.assertEquals(history.getAlertText(), current.getLatestText());
    }

    @org.junit.Test
    public void testFindByState() {
        java.util.List<org.apache.ambari.server.state.AlertState> allStates = new java.util.ArrayList<>();
        allStates.add(org.apache.ambari.server.state.AlertState.OK);
        allStates.add(org.apache.ambari.server.state.AlertState.WARNING);
        allStates.add(org.apache.ambari.server.state.AlertState.CRITICAL);
        java.util.List<org.apache.ambari.server.orm.entities.AlertHistoryEntity> history = m_dao.findAll(m_cluster.getClusterId(), allStates);
        org.junit.Assert.assertNotNull(history);
        org.junit.Assert.assertEquals(50, history.size());
        history = m_dao.findAll(m_cluster.getClusterId(), java.util.Collections.singletonList(org.apache.ambari.server.state.AlertState.OK));
        org.junit.Assert.assertNotNull(history);
        org.junit.Assert.assertEquals(40, history.size());
        history = m_dao.findAll(m_cluster.getClusterId(), java.util.Collections.singletonList(org.apache.ambari.server.state.AlertState.CRITICAL));
        org.junit.Assert.assertNotNull(history);
        org.junit.Assert.assertEquals(10, history.size());
        history = m_dao.findAll(m_cluster.getClusterId(), java.util.Collections.singletonList(org.apache.ambari.server.state.AlertState.WARNING));
        org.junit.Assert.assertNotNull(history);
        org.junit.Assert.assertEquals(0, history.size());
    }

    @org.junit.Test
    public void testFindByDate() {
        org.apache.ambari.server.orm.dao.AlertsDAOTest.calendar.clear();
        org.apache.ambari.server.orm.dao.AlertsDAOTest.calendar.set(2014, java.util.Calendar.JANUARY, 1);
        java.util.List<org.apache.ambari.server.orm.entities.AlertHistoryEntity> history = m_dao.findAll(m_cluster.getClusterId(), org.apache.ambari.server.orm.dao.AlertsDAOTest.calendar.getTime(), null);
        org.junit.Assert.assertNotNull(history);
        org.junit.Assert.assertEquals(50, history.size());
        history = m_dao.findAll(m_cluster.getClusterId(), null, org.apache.ambari.server.orm.dao.AlertsDAOTest.calendar.getTime());
        org.junit.Assert.assertNotNull(history);
        org.junit.Assert.assertEquals(1, history.size());
        org.apache.ambari.server.orm.dao.AlertsDAOTest.calendar.set(2014, java.util.Calendar.JANUARY, 5);
        java.util.Date startDate = org.apache.ambari.server.orm.dao.AlertsDAOTest.calendar.getTime();
        org.apache.ambari.server.orm.dao.AlertsDAOTest.calendar.set(2014, java.util.Calendar.JANUARY, 10);
        java.util.Date endDate = org.apache.ambari.server.orm.dao.AlertsDAOTest.calendar.getTime();
        history = m_dao.findAll(m_cluster.getClusterId(), startDate, endDate);
        org.junit.Assert.assertNotNull(history);
        org.junit.Assert.assertEquals(6, history.size());
        org.apache.ambari.server.orm.dao.AlertsDAOTest.calendar.set(2014, java.util.Calendar.MARCH, 5);
        history = m_dao.findAll(m_cluster.getClusterId(), org.apache.ambari.server.orm.dao.AlertsDAOTest.calendar.getTime(), null);
        org.junit.Assert.assertNotNull(history);
        org.junit.Assert.assertEquals(0, history.size());
        history = m_dao.findAll(m_cluster.getClusterId(), endDate, startDate);
        org.junit.Assert.assertNotNull(history);
        org.junit.Assert.assertEquals(0, history.size());
    }

    @org.junit.Test
    public void testFindCurrentByHostAndName() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.AlertCurrentEntity entity = m_dao.findCurrentByHostAndName(m_cluster.getClusterId(), "h2", "Alert Definition 1");
        org.junit.Assert.assertNull(entity);
        entity = m_dao.findCurrentByHostAndName(m_cluster.getClusterId(), "h1", "Alert Definition 1");
        org.junit.Assert.assertNotNull(entity);
        org.junit.Assert.assertNotNull(entity.getAlertHistory());
        org.junit.Assert.assertNotNull(entity.getAlertHistory().getAlertDefinition());
    }

    @org.junit.Test
    public void testFindCurrentSummary() throws java.lang.Exception {
        org.apache.ambari.server.orm.dao.AlertSummaryDTO summary = m_dao.findCurrentCounts(m_cluster.getClusterId(), null, null);
        org.junit.Assert.assertEquals(5, summary.getOkCount());
        org.apache.ambari.server.orm.entities.AlertHistoryEntity h1 = m_dao.findCurrentByCluster(m_cluster.getClusterId()).get(2).getAlertHistory();
        org.apache.ambari.server.orm.entities.AlertHistoryEntity h2 = m_dao.findCurrentByCluster(m_cluster.getClusterId()).get(3).getAlertHistory();
        org.apache.ambari.server.orm.entities.AlertHistoryEntity h3 = m_dao.findCurrentByCluster(m_cluster.getClusterId()).get(4).getAlertHistory();
        h1.setAlertState(org.apache.ambari.server.state.AlertState.WARNING);
        m_dao.merge(h1);
        h2.setAlertState(org.apache.ambari.server.state.AlertState.CRITICAL);
        m_dao.merge(h2);
        h3.setAlertState(org.apache.ambari.server.state.AlertState.UNKNOWN);
        m_dao.merge(h3);
        int ok = 0;
        int warn = 0;
        int crit = 0;
        int unk = 0;
        int maintenance = 0;
        java.util.List<org.apache.ambari.server.orm.entities.AlertCurrentEntity> currents = m_dao.findCurrentByCluster(m_cluster.getClusterId());
        for (org.apache.ambari.server.orm.entities.AlertCurrentEntity current : currents) {
            if (current.getMaintenanceState() != org.apache.ambari.server.state.MaintenanceState.OFF) {
                maintenance++;
                continue;
            }
            switch (current.getAlertHistory().getAlertState()) {
                case CRITICAL :
                    crit++;
                    break;
                case OK :
                    ok++;
                    break;
                case UNKNOWN :
                    unk++;
                    break;
                default :
                    warn++;
                    break;
            }
        }
        summary = m_dao.findCurrentCounts(m_cluster.getClusterId(), null, null);
        org.junit.Assert.assertEquals(ok, summary.getOkCount());
        org.junit.Assert.assertEquals(warn, summary.getWarningCount());
        org.junit.Assert.assertEquals(crit, summary.getCriticalCount());
        org.junit.Assert.assertEquals(unk, summary.getUnknownCount());
        org.junit.Assert.assertEquals(maintenance, summary.getMaintenanceCount());
        org.junit.Assert.assertEquals(2, summary.getOkCount());
        org.junit.Assert.assertEquals(1, summary.getWarningCount());
        org.junit.Assert.assertEquals(1, summary.getCriticalCount());
        org.junit.Assert.assertEquals(1, summary.getUnknownCount());
        org.junit.Assert.assertEquals(0, summary.getMaintenanceCount());
        summary = m_dao.findCurrentCounts(m_cluster.getClusterId(), "YARN", null);
        org.junit.Assert.assertEquals(2, summary.getOkCount());
        org.junit.Assert.assertEquals(1, summary.getWarningCount());
        org.junit.Assert.assertEquals(1, summary.getCriticalCount());
        org.junit.Assert.assertEquals(1, summary.getUnknownCount());
        summary = m_dao.findCurrentCounts(m_cluster.getClusterId(), null, "h1");
        org.junit.Assert.assertEquals(2, summary.getOkCount());
        org.junit.Assert.assertEquals(1, summary.getWarningCount());
        org.junit.Assert.assertEquals(1, summary.getCriticalCount());
        org.junit.Assert.assertEquals(1, summary.getUnknownCount());
        org.junit.Assert.assertEquals(0, summary.getMaintenanceCount());
        summary = m_dao.findCurrentCounts(m_cluster.getClusterId(), "foo", null);
        org.junit.Assert.assertEquals(0, summary.getOkCount());
        org.junit.Assert.assertEquals(0, summary.getWarningCount());
        org.junit.Assert.assertEquals(0, summary.getCriticalCount());
        org.junit.Assert.assertEquals(0, summary.getUnknownCount());
        org.junit.Assert.assertEquals(0, summary.getMaintenanceCount());
        for (org.apache.ambari.server.orm.entities.AlertCurrentEntity current : currents) {
            if (current.getAlertHistory().getAlertState() == org.apache.ambari.server.state.AlertState.WARNING) {
                current.setMaintenanceState(org.apache.ambari.server.state.MaintenanceState.ON);
                m_dao.merge(current);
            }
        }
        summary = m_dao.findCurrentCounts(m_cluster.getClusterId(), null, null);
        org.junit.Assert.assertEquals(2, summary.getOkCount());
        org.junit.Assert.assertEquals(0, summary.getWarningCount());
        org.junit.Assert.assertEquals(1, summary.getCriticalCount());
        org.junit.Assert.assertEquals(1, summary.getUnknownCount());
        org.junit.Assert.assertEquals(1, summary.getMaintenanceCount());
    }

    @org.junit.Test
    public void testFindCurrentPerHostSummary() throws java.lang.Exception {
        m_helper.addHost(m_clusters, m_cluster, "h2");
        java.util.List<org.apache.ambari.server.orm.entities.AlertDefinitionEntity> definitions = m_definitionDao.findAll();
        org.apache.ambari.server.orm.entities.AlertDefinitionEntity definition = definitions.get(0);
        org.apache.ambari.server.orm.entities.AlertHistoryEntity h2CriticalHistory = new org.apache.ambari.server.orm.entities.AlertHistoryEntity();
        h2CriticalHistory.setServiceName(definition.getServiceName());
        h2CriticalHistory.setClusterId(m_cluster.getClusterId());
        h2CriticalHistory.setAlertDefinition(definition);
        h2CriticalHistory.setAlertLabel(definition.getDefinitionName() + " h2");
        h2CriticalHistory.setAlertText(definition.getDefinitionName() + " h2");
        h2CriticalHistory.setAlertTimestamp(org.apache.ambari.server.orm.dao.AlertsDAOTest.calendar.getTimeInMillis());
        h2CriticalHistory.setComponentName(definition.getComponentName());
        h2CriticalHistory.setHostName("h2");
        h2CriticalHistory.setAlertState(org.apache.ambari.server.state.AlertState.CRITICAL);
        m_dao.create(h2CriticalHistory);
        org.apache.ambari.server.orm.entities.AlertCurrentEntity h2CriticalCurrent = new org.apache.ambari.server.orm.entities.AlertCurrentEntity();
        h2CriticalCurrent.setAlertHistory(h2CriticalHistory);
        h2CriticalCurrent.setLatestTimestamp(new java.util.Date().getTime());
        h2CriticalCurrent.setOriginalTimestamp(new java.util.Date().getTime() - 10800000);
        h2CriticalCurrent.setMaintenanceState(org.apache.ambari.server.state.MaintenanceState.OFF);
        m_dao.create(h2CriticalCurrent);
        try {
            long clusterId = m_cluster.getClusterId();
            org.apache.ambari.server.orm.dao.AlertSummaryDTO summary = m_dao.findCurrentCounts(clusterId, null, null);
            org.junit.Assert.assertEquals(5, summary.getOkCount());
            org.apache.ambari.server.orm.entities.AlertHistoryEntity h1 = m_dao.findCurrentByCluster(clusterId).get(2).getAlertHistory();
            org.apache.ambari.server.orm.entities.AlertHistoryEntity h2 = m_dao.findCurrentByCluster(clusterId).get(3).getAlertHistory();
            org.apache.ambari.server.orm.entities.AlertHistoryEntity h3 = m_dao.findCurrentByCluster(clusterId).get(4).getAlertHistory();
            h1.setAlertState(org.apache.ambari.server.state.AlertState.WARNING);
            m_dao.merge(h1);
            h2.setAlertState(org.apache.ambari.server.state.AlertState.CRITICAL);
            m_dao.merge(h2);
            h3.setAlertState(org.apache.ambari.server.state.AlertState.UNKNOWN);
            m_dao.merge(h3);
            java.util.Map<java.lang.String, org.apache.ambari.server.orm.dao.AlertSummaryDTO> perHostSummary = m_dao.findCurrentPerHostCounts(clusterId);
            org.apache.ambari.server.orm.dao.AlertSummaryDTO h1summary = m_dao.findCurrentCounts(clusterId, null, "h1");
            org.junit.Assert.assertEquals(2, h1summary.getOkCount());
            org.junit.Assert.assertEquals(1, h1summary.getWarningCount());
            org.junit.Assert.assertEquals(1, h1summary.getCriticalCount());
            org.junit.Assert.assertEquals(1, h1summary.getUnknownCount());
            org.junit.Assert.assertEquals(0, h1summary.getMaintenanceCount());
            org.apache.ambari.server.orm.dao.AlertSummaryDTO h2summary = m_dao.findCurrentCounts(clusterId, null, "h2");
            org.junit.Assert.assertEquals(0, h2summary.getOkCount());
            org.junit.Assert.assertEquals(0, h2summary.getWarningCount());
            org.junit.Assert.assertEquals(1, h2summary.getCriticalCount());
            org.junit.Assert.assertEquals(0, h2summary.getUnknownCount());
            org.junit.Assert.assertEquals(0, h2summary.getMaintenanceCount());
            org.apache.ambari.server.orm.dao.AlertSummaryDTO h1PerHostSummary = perHostSummary.get("h1");
            org.junit.Assert.assertEquals(h1PerHostSummary.getOkCount(), h1summary.getOkCount());
            org.junit.Assert.assertEquals(h1PerHostSummary.getWarningCount(), h1summary.getWarningCount());
            org.junit.Assert.assertEquals(h1PerHostSummary.getCriticalCount(), h1summary.getCriticalCount());
            org.junit.Assert.assertEquals(h1PerHostSummary.getUnknownCount(), h1summary.getUnknownCount());
            org.junit.Assert.assertEquals(h1PerHostSummary.getMaintenanceCount(), h1summary.getMaintenanceCount());
            org.apache.ambari.server.orm.dao.AlertSummaryDTO h2PerHostSummary = perHostSummary.get("h2");
            org.junit.Assert.assertEquals(h2PerHostSummary.getOkCount(), h2summary.getOkCount());
            org.junit.Assert.assertEquals(h2PerHostSummary.getWarningCount(), h2summary.getWarningCount());
            org.junit.Assert.assertEquals(h2PerHostSummary.getCriticalCount(), h2summary.getCriticalCount());
            org.junit.Assert.assertEquals(h2PerHostSummary.getUnknownCount(), h2summary.getUnknownCount());
            org.junit.Assert.assertEquals(h2PerHostSummary.getMaintenanceCount(), h2summary.getMaintenanceCount());
        } finally {
            m_dao.remove(h2CriticalCurrent);
            m_dao.remove(h2CriticalHistory);
            m_clusters.unmapHostFromCluster("h2", m_cluster.getClusterName());
        }
    }

    @org.junit.Test
    public void testFindCurrentHostSummary() throws java.lang.Exception {
        org.apache.ambari.server.orm.dao.AlertHostSummaryDTO summary = m_dao.findCurrentHostCounts(m_cluster.getClusterId());
        org.junit.Assert.assertEquals(0, summary.getWarningCount());
        org.junit.Assert.assertEquals(0, summary.getCriticalCount());
        org.junit.Assert.assertEquals(0, summary.getUnknownCount());
        org.junit.Assert.assertEquals(1, summary.getOkCount());
        org.apache.ambari.server.orm.entities.AlertHistoryEntity history1 = m_dao.findCurrentByCluster(m_cluster.getClusterId()).get(1).getAlertHistory();
        history1.setAlertState(org.apache.ambari.server.state.AlertState.WARNING);
        m_dao.merge(history1);
        summary = m_dao.findCurrentHostCounts(m_cluster.getClusterId());
        org.junit.Assert.assertEquals(1, summary.getWarningCount());
        org.junit.Assert.assertEquals(0, summary.getCriticalCount());
        org.junit.Assert.assertEquals(0, summary.getUnknownCount());
        org.junit.Assert.assertEquals(0, summary.getOkCount());
        history1.setAlertState(org.apache.ambari.server.state.AlertState.CRITICAL);
        m_dao.merge(history1);
        summary = m_dao.findCurrentHostCounts(m_cluster.getClusterId());
        org.junit.Assert.assertEquals(0, summary.getWarningCount());
        org.junit.Assert.assertEquals(1, summary.getCriticalCount());
        org.junit.Assert.assertEquals(0, summary.getUnknownCount());
        org.junit.Assert.assertEquals(0, summary.getOkCount());
        org.apache.ambari.server.orm.entities.AlertHistoryEntity history2 = m_dao.findCurrentByCluster(m_cluster.getClusterId()).get(2).getAlertHistory();
        history2.setHostName(history2.getHostName() + "-foo");
        m_dao.merge(history2);
        summary = m_dao.findCurrentHostCounts(m_cluster.getClusterId());
        org.junit.Assert.assertEquals(0, summary.getWarningCount());
        org.junit.Assert.assertEquals(1, summary.getCriticalCount());
        org.junit.Assert.assertEquals(0, summary.getUnknownCount());
        org.junit.Assert.assertEquals(1, summary.getOkCount());
        org.apache.ambari.server.orm.entities.AlertHistoryEntity history3 = m_dao.findCurrentByCluster(m_cluster.getClusterId()).get(3).getAlertHistory();
        history3.setHostName(history3.getHostName() + "-bar");
        m_dao.merge(history3);
        summary = m_dao.findCurrentHostCounts(m_cluster.getClusterId());
        org.junit.Assert.assertEquals(0, summary.getWarningCount());
        org.junit.Assert.assertEquals(1, summary.getCriticalCount());
        org.junit.Assert.assertEquals(0, summary.getUnknownCount());
        org.junit.Assert.assertEquals(2, summary.getOkCount());
        org.apache.ambari.server.orm.entities.AlertHistoryEntity history4 = m_dao.findCurrentByCluster(m_cluster.getClusterId()).get(4).getAlertHistory();
        history4.setHostName(history4.getHostName() + "-baz");
        history4.setAlertState(org.apache.ambari.server.state.AlertState.UNKNOWN);
        m_dao.merge(history3);
        summary = m_dao.findCurrentHostCounts(m_cluster.getClusterId());
        org.junit.Assert.assertEquals(0, summary.getWarningCount());
        org.junit.Assert.assertEquals(1, summary.getCriticalCount());
        org.junit.Assert.assertEquals(1, summary.getUnknownCount());
        org.junit.Assert.assertEquals(2, summary.getOkCount());
        org.apache.ambari.server.orm.entities.AlertCurrentEntity current4 = m_dao.findCurrentByCluster(m_cluster.getClusterId()).get(4);
        current4.setMaintenanceState(org.apache.ambari.server.state.MaintenanceState.ON);
        m_dao.merge(current4);
        summary = m_dao.findCurrentHostCounts(m_cluster.getClusterId());
        org.junit.Assert.assertEquals(0, summary.getWarningCount());
        org.junit.Assert.assertEquals(1, summary.getCriticalCount());
        org.junit.Assert.assertEquals(0, summary.getUnknownCount());
        org.junit.Assert.assertEquals(3, summary.getOkCount());
        java.util.List<org.apache.ambari.server.orm.entities.AlertCurrentEntity> currents = m_dao.findCurrentByCluster(m_cluster.getClusterId());
        for (org.apache.ambari.server.orm.entities.AlertCurrentEntity current : currents) {
            current.setMaintenanceState(org.apache.ambari.server.state.MaintenanceState.ON);
            m_dao.merge(current);
        }
        summary = m_dao.findCurrentHostCounts(m_cluster.getClusterId());
        org.junit.Assert.assertEquals(0, summary.getWarningCount());
        org.junit.Assert.assertEquals(0, summary.getCriticalCount());
        org.junit.Assert.assertEquals(0, summary.getUnknownCount());
        org.junit.Assert.assertEquals(4, summary.getOkCount());
    }

    @org.junit.Test
    public void testFindAggregates() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.AlertDefinitionEntity definition = new org.apache.ambari.server.orm.entities.AlertDefinitionEntity();
        definition.setDefinitionName("many_per_cluster");
        definition.setServiceName("YARN");
        definition.setComponentName(null);
        definition.setClusterId(m_cluster.getClusterId());
        definition.setHash(java.util.UUID.randomUUID().toString());
        definition.setScheduleInterval(java.lang.Integer.valueOf(60));
        definition.setScope(org.apache.ambari.server.state.alert.Scope.SERVICE);
        definition.setSource("{\"type\" : \"SCRIPT\"}");
        definition.setSourceType(org.apache.ambari.server.state.alert.SourceType.SCRIPT);
        m_definitionDao.create(definition);
        org.apache.ambari.server.orm.entities.AlertHistoryEntity history = new org.apache.ambari.server.orm.entities.AlertHistoryEntity();
        history.setAlertDefinition(definition);
        history.setAlertInstance(null);
        history.setAlertLabel("");
        history.setAlertState(org.apache.ambari.server.state.AlertState.OK);
        history.setAlertText("");
        history.setAlertTimestamp(java.lang.Long.valueOf(1L));
        history.setClusterId(m_cluster.getClusterId());
        history.setComponentName("");
        history.setHostName("h1");
        history.setServiceName("ServiceName");
        org.apache.ambari.server.orm.entities.AlertCurrentEntity current = new org.apache.ambari.server.orm.entities.AlertCurrentEntity();
        current.setAlertHistory(history);
        current.setLatestTimestamp(java.lang.Long.valueOf(1L));
        current.setOriginalTimestamp(java.lang.Long.valueOf(1L));
        m_dao.merge(current);
        history = new org.apache.ambari.server.orm.entities.AlertHistoryEntity();
        history.setAlertDefinition(definition);
        history.setAlertInstance(null);
        history.setAlertLabel("");
        history.setAlertState(org.apache.ambari.server.state.AlertState.OK);
        history.setAlertText("");
        history.setAlertTimestamp(java.lang.Long.valueOf(1L));
        history.setClusterId(m_cluster.getClusterId());
        history.setComponentName("");
        history.setHostName("h2");
        history.setServiceName("ServiceName");
        m_dao.create(history);
        current = new org.apache.ambari.server.orm.entities.AlertCurrentEntity();
        current.setAlertHistory(history);
        current.setLatestTimestamp(java.lang.Long.valueOf(1L));
        current.setOriginalTimestamp(java.lang.Long.valueOf(1L));
        m_dao.merge(current);
        org.apache.ambari.server.orm.dao.AlertSummaryDTO summary = m_dao.findAggregateCounts(m_cluster.getClusterId(), "many_per_cluster");
        org.junit.Assert.assertEquals(2, summary.getOkCount());
        org.junit.Assert.assertEquals(0, summary.getWarningCount());
        org.junit.Assert.assertEquals(0, summary.getCriticalCount());
        org.junit.Assert.assertEquals(0, summary.getUnknownCount());
        org.apache.ambari.server.orm.entities.AlertCurrentEntity c = m_dao.findCurrentByHostAndName(m_cluster.getClusterId(), "h2", "many_per_cluster");
        org.apache.ambari.server.orm.entities.AlertHistoryEntity h = c.getAlertHistory();
        h.setAlertState(org.apache.ambari.server.state.AlertState.CRITICAL);
        m_dao.merge(h);
        summary = m_dao.findAggregateCounts(m_cluster.getClusterId(), "many_per_cluster");
        org.junit.Assert.assertEquals(1, summary.getOkCount());
        org.junit.Assert.assertEquals(0, summary.getWarningCount());
        org.junit.Assert.assertEquals(1, summary.getCriticalCount());
        org.junit.Assert.assertEquals(0, summary.getUnknownCount());
        summary = m_dao.findAggregateCounts(m_cluster.getClusterId(), "foo");
        org.junit.Assert.assertEquals(0, summary.getOkCount());
        org.junit.Assert.assertEquals(0, summary.getWarningCount());
        org.junit.Assert.assertEquals(0, summary.getCriticalCount());
        org.junit.Assert.assertEquals(0, summary.getUnknownCount());
    }

    @org.junit.Test
    public void testJPAInnerEntityStaleness() {
        java.util.List<org.apache.ambari.server.orm.entities.AlertCurrentEntity> currents = m_dao.findCurrent();
        org.apache.ambari.server.orm.entities.AlertCurrentEntity current = currents.get(0);
        org.apache.ambari.server.orm.entities.AlertHistoryEntity oldHistory = current.getAlertHistory();
        org.apache.ambari.server.orm.entities.AlertHistoryEntity newHistory = new org.apache.ambari.server.orm.entities.AlertHistoryEntity();
        newHistory.setAlertDefinition(oldHistory.getAlertDefinition());
        newHistory.setAlertInstance(oldHistory.getAlertInstance());
        newHistory.setAlertLabel(oldHistory.getAlertLabel());
        if (oldHistory.getAlertState() == org.apache.ambari.server.state.AlertState.OK) {
            newHistory.setAlertState(org.apache.ambari.server.state.AlertState.CRITICAL);
        } else {
            newHistory.setAlertState(org.apache.ambari.server.state.AlertState.OK);
        }
        newHistory.setAlertText("New History");
        newHistory.setClusterId(oldHistory.getClusterId());
        newHistory.setAlertTimestamp(java.lang.System.currentTimeMillis());
        newHistory.setComponentName(oldHistory.getComponentName());
        newHistory.setHostName(oldHistory.getHostName());
        newHistory.setServiceName(oldHistory.getServiceName());
        m_dao.create(newHistory);
        org.junit.Assert.assertTrue(newHistory.getAlertId().longValue() != oldHistory.getAlertId().longValue());
        current.setAlertHistory(newHistory);
        m_dao.merge(current);
        org.apache.ambari.server.orm.entities.AlertCurrentEntity newCurrent = m_dao.findCurrentByHostAndName(newHistory.getClusterId(), newHistory.getHostName(), newHistory.getAlertDefinition().getDefinitionName());
        org.junit.Assert.assertEquals(newHistory.getAlertId(), newCurrent.getAlertHistory().getAlertId());
        org.junit.Assert.assertEquals(newHistory.getAlertState(), newCurrent.getAlertHistory().getAlertState());
        newCurrent = m_dao.findCurrentById(current.getAlertId());
        org.junit.Assert.assertEquals(newHistory.getAlertId(), newCurrent.getAlertHistory().getAlertId());
        org.junit.Assert.assertEquals(newHistory.getAlertState(), newCurrent.getAlertHistory().getAlertState());
    }

    @org.junit.Test
    public void testMaintenanceMode() throws java.lang.Exception {
        m_helper.installHdfsService(m_cluster, m_serviceFactory, m_componentFactory, m_schFactory, org.apache.ambari.server.orm.dao.AlertsDAOTest.HOSTNAME);
        java.util.List<org.apache.ambari.server.orm.entities.AlertCurrentEntity> currents = m_dao.findCurrent();
        for (org.apache.ambari.server.orm.entities.AlertCurrentEntity current : currents) {
            m_dao.remove(current);
        }
        org.apache.ambari.server.orm.entities.AlertDefinitionEntity namenode = new org.apache.ambari.server.orm.entities.AlertDefinitionEntity();
        namenode.setDefinitionName("NAMENODE");
        namenode.setServiceName("HDFS");
        namenode.setComponentName("NAMENODE");
        namenode.setClusterId(m_cluster.getClusterId());
        namenode.setHash(java.util.UUID.randomUUID().toString());
        namenode.setScheduleInterval(java.lang.Integer.valueOf(60));
        namenode.setScope(org.apache.ambari.server.state.alert.Scope.ANY);
        namenode.setSource("{\"type\" : \"SCRIPT\"}");
        namenode.setSourceType(org.apache.ambari.server.state.alert.SourceType.SCRIPT);
        m_definitionDao.create(namenode);
        org.apache.ambari.server.orm.entities.AlertDefinitionEntity datanode = new org.apache.ambari.server.orm.entities.AlertDefinitionEntity();
        datanode.setDefinitionName("DATANODE");
        datanode.setServiceName("HDFS");
        datanode.setComponentName("DATANODE");
        datanode.setClusterId(m_cluster.getClusterId());
        datanode.setHash(java.util.UUID.randomUUID().toString());
        datanode.setScheduleInterval(java.lang.Integer.valueOf(60));
        datanode.setScope(org.apache.ambari.server.state.alert.Scope.HOST);
        datanode.setSource("{\"type\" : \"SCRIPT\"}");
        datanode.setSourceType(org.apache.ambari.server.state.alert.SourceType.SCRIPT);
        m_definitionDao.create(datanode);
        org.apache.ambari.server.orm.entities.AlertDefinitionEntity aggregate = new org.apache.ambari.server.orm.entities.AlertDefinitionEntity();
        aggregate.setDefinitionName("DATANODE_UP");
        aggregate.setServiceName("HDFS");
        aggregate.setComponentName(null);
        aggregate.setClusterId(m_cluster.getClusterId());
        aggregate.setHash(java.util.UUID.randomUUID().toString());
        aggregate.setScheduleInterval(java.lang.Integer.valueOf(60));
        aggregate.setScope(org.apache.ambari.server.state.alert.Scope.SERVICE);
        aggregate.setSource("{\"type\" : \"SCRIPT\"}");
        aggregate.setSourceType(org.apache.ambari.server.state.alert.SourceType.SCRIPT);
        m_definitionDao.create(aggregate);
        org.apache.ambari.server.orm.entities.AlertHistoryEntity nnHistory = new org.apache.ambari.server.orm.entities.AlertHistoryEntity();
        nnHistory.setAlertState(org.apache.ambari.server.state.AlertState.OK);
        nnHistory.setServiceName(namenode.getServiceName());
        nnHistory.setComponentName(namenode.getComponentName());
        nnHistory.setClusterId(m_cluster.getClusterId());
        nnHistory.setAlertDefinition(namenode);
        nnHistory.setAlertLabel(namenode.getDefinitionName());
        nnHistory.setAlertText(namenode.getDefinitionName());
        nnHistory.setAlertTimestamp(org.apache.ambari.server.orm.dao.AlertsDAOTest.calendar.getTimeInMillis());
        nnHistory.setHostName(org.apache.ambari.server.orm.dao.AlertsDAOTest.HOSTNAME);
        m_dao.create(nnHistory);
        org.apache.ambari.server.orm.entities.AlertCurrentEntity nnCurrent = new org.apache.ambari.server.orm.entities.AlertCurrentEntity();
        nnCurrent.setAlertHistory(nnHistory);
        nnCurrent.setLatestText(nnHistory.getAlertText());
        nnCurrent.setMaintenanceState(org.apache.ambari.server.state.MaintenanceState.OFF);
        nnCurrent.setOriginalTimestamp(java.lang.System.currentTimeMillis());
        nnCurrent.setLatestTimestamp(java.lang.System.currentTimeMillis());
        m_dao.create(nnCurrent);
        org.apache.ambari.server.orm.entities.AlertHistoryEntity dnHistory = new org.apache.ambari.server.orm.entities.AlertHistoryEntity();
        dnHistory.setAlertState(org.apache.ambari.server.state.AlertState.WARNING);
        dnHistory.setServiceName(datanode.getServiceName());
        dnHistory.setComponentName(datanode.getComponentName());
        dnHistory.setClusterId(m_cluster.getClusterId());
        dnHistory.setAlertDefinition(datanode);
        dnHistory.setAlertLabel(datanode.getDefinitionName());
        dnHistory.setAlertText(datanode.getDefinitionName());
        dnHistory.setAlertTimestamp(org.apache.ambari.server.orm.dao.AlertsDAOTest.calendar.getTimeInMillis());
        dnHistory.setHostName(org.apache.ambari.server.orm.dao.AlertsDAOTest.HOSTNAME);
        m_dao.create(dnHistory);
        org.apache.ambari.server.orm.entities.AlertCurrentEntity dnCurrent = new org.apache.ambari.server.orm.entities.AlertCurrentEntity();
        dnCurrent.setAlertHistory(dnHistory);
        dnCurrent.setLatestText(dnHistory.getAlertText());
        dnCurrent.setMaintenanceState(org.apache.ambari.server.state.MaintenanceState.OFF);
        dnCurrent.setOriginalTimestamp(java.lang.System.currentTimeMillis());
        dnCurrent.setLatestTimestamp(java.lang.System.currentTimeMillis());
        m_dao.create(dnCurrent);
        org.apache.ambari.server.orm.entities.AlertHistoryEntity aggregateHistory = new org.apache.ambari.server.orm.entities.AlertHistoryEntity();
        aggregateHistory.setAlertState(org.apache.ambari.server.state.AlertState.CRITICAL);
        aggregateHistory.setServiceName(aggregate.getServiceName());
        aggregateHistory.setComponentName(aggregate.getComponentName());
        aggregateHistory.setClusterId(m_cluster.getClusterId());
        aggregateHistory.setAlertDefinition(aggregate);
        aggregateHistory.setAlertLabel(aggregate.getDefinitionName());
        aggregateHistory.setAlertText(aggregate.getDefinitionName());
        aggregateHistory.setAlertTimestamp(org.apache.ambari.server.orm.dao.AlertsDAOTest.calendar.getTimeInMillis());
        m_dao.create(aggregateHistory);
        org.apache.ambari.server.orm.entities.AlertCurrentEntity aggregateCurrent = new org.apache.ambari.server.orm.entities.AlertCurrentEntity();
        aggregateCurrent.setAlertHistory(aggregateHistory);
        aggregateCurrent.setLatestText(aggregateHistory.getAlertText());
        aggregateCurrent.setMaintenanceState(org.apache.ambari.server.state.MaintenanceState.OFF);
        aggregateCurrent.setOriginalTimestamp(java.lang.System.currentTimeMillis());
        aggregateCurrent.setLatestTimestamp(java.lang.System.currentTimeMillis());
        m_dao.create(aggregateCurrent);
        currents = m_dao.findCurrent();
        org.junit.Assert.assertEquals(3, currents.size());
        for (org.apache.ambari.server.orm.entities.AlertCurrentEntity current : currents) {
            org.junit.Assert.assertEquals(org.apache.ambari.server.state.MaintenanceState.OFF, current.getMaintenanceState());
        }
        org.apache.ambari.server.state.Service hdfs = m_clusters.getClusterById(m_cluster.getClusterId()).getService("HDFS");
        hdfs.setMaintenanceState(org.apache.ambari.server.state.MaintenanceState.ON);
        currents = m_dao.findCurrent();
        org.junit.Assert.assertEquals(3, currents.size());
        for (org.apache.ambari.server.orm.entities.AlertCurrentEntity current : currents) {
            org.junit.Assert.assertEquals(org.apache.ambari.server.state.MaintenanceState.ON, current.getMaintenanceState());
        }
        hdfs.setMaintenanceState(org.apache.ambari.server.state.MaintenanceState.OFF);
        currents = m_dao.findCurrent();
        org.junit.Assert.assertEquals(3, currents.size());
        for (org.apache.ambari.server.orm.entities.AlertCurrentEntity current : currents) {
            org.junit.Assert.assertEquals(org.apache.ambari.server.state.MaintenanceState.OFF, current.getMaintenanceState());
        }
        org.apache.ambari.server.state.Host host = m_clusters.getHost(org.apache.ambari.server.orm.dao.AlertsDAOTest.HOSTNAME);
        host.setMaintenanceState(m_cluster.getClusterId(), org.apache.ambari.server.state.MaintenanceState.ON);
        currents = m_dao.findCurrent();
        org.junit.Assert.assertEquals(3, currents.size());
        for (org.apache.ambari.server.orm.entities.AlertCurrentEntity current : currents) {
            if (current.getAlertHistory().getComponentName() != null) {
                org.junit.Assert.assertEquals(org.apache.ambari.server.state.MaintenanceState.ON, current.getMaintenanceState());
            } else {
                org.junit.Assert.assertEquals(org.apache.ambari.server.state.MaintenanceState.OFF, current.getMaintenanceState());
            }
        }
        host.setMaintenanceState(m_cluster.getClusterId(), org.apache.ambari.server.state.MaintenanceState.OFF);
        currents = m_dao.findCurrent();
        org.junit.Assert.assertEquals(3, currents.size());
        for (org.apache.ambari.server.orm.entities.AlertCurrentEntity current : currents) {
            org.junit.Assert.assertEquals(org.apache.ambari.server.state.MaintenanceState.OFF, current.getMaintenanceState());
        }
        org.apache.ambari.server.state.ServiceComponentHost nnComponent = null;
        java.util.List<org.apache.ambari.server.state.ServiceComponentHost> schs = m_cluster.getServiceComponentHosts(org.apache.ambari.server.orm.dao.AlertsDAOTest.HOSTNAME);
        for (org.apache.ambari.server.state.ServiceComponentHost sch : schs) {
            if ("NAMENODE".equals(sch.getServiceComponentName())) {
                sch.setMaintenanceState(org.apache.ambari.server.state.MaintenanceState.ON);
                nnComponent = sch;
            }
        }
        org.junit.Assert.assertNotNull(nnComponent);
        currents = m_dao.findCurrent();
        org.junit.Assert.assertEquals(3, currents.size());
        for (org.apache.ambari.server.orm.entities.AlertCurrentEntity current : currents) {
            if ("NAMENODE".equals(current.getAlertHistory().getComponentName())) {
                org.junit.Assert.assertEquals(org.apache.ambari.server.state.MaintenanceState.ON, current.getMaintenanceState());
            } else {
                org.junit.Assert.assertEquals(org.apache.ambari.server.state.MaintenanceState.OFF, current.getMaintenanceState());
            }
        }
    }

    @org.junit.Test
    public void testAlertHistoryPredicate() throws java.lang.Exception {
        m_helper.installHdfsService(m_cluster, m_serviceFactory, m_componentFactory, m_schFactory, org.apache.ambari.server.orm.dao.AlertsDAOTest.HOSTNAME);
        m_alertHelper.populateData(m_cluster);
        org.apache.ambari.server.controller.spi.Predicate clusterPredicate = null;
        org.apache.ambari.server.controller.spi.Predicate hdfsPredicate = null;
        org.apache.ambari.server.controller.spi.Predicate yarnPredicate = null;
        org.apache.ambari.server.controller.spi.Predicate clusterAndHdfsPredicate = null;
        org.apache.ambari.server.controller.spi.Predicate clusterAndHdfsAndCriticalPredicate = null;
        org.apache.ambari.server.controller.spi.Predicate hdfsAndCriticalOrWarningPredicate = null;
        org.apache.ambari.server.controller.spi.Predicate alertNamePredicate = null;
        org.apache.ambari.server.controller.spi.Predicate historyIdPredicate = null;
        clusterPredicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.AlertHistoryResourceProvider.ALERT_HISTORY_CLUSTER_NAME).equals("c1").toPredicate();
        org.apache.ambari.server.controller.AlertHistoryRequest request = new org.apache.ambari.server.controller.AlertHistoryRequest();
        request.Predicate = clusterPredicate;
        java.util.List<org.apache.ambari.server.orm.entities.AlertHistoryEntity> histories = m_dao.findAll(request);
        org.junit.Assert.assertEquals(3, histories.size());
        hdfsPredicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.AlertHistoryResourceProvider.ALERT_HISTORY_SERVICE_NAME).equals("HDFS").toPredicate();
        yarnPredicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.AlertHistoryResourceProvider.ALERT_HISTORY_SERVICE_NAME).equals("YARN").toPredicate();
        clusterAndHdfsPredicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.AlertHistoryResourceProvider.ALERT_HISTORY_CLUSTER_NAME).equals("c1").and().property(org.apache.ambari.server.controller.internal.AlertHistoryResourceProvider.ALERT_HISTORY_SERVICE_NAME).equals("HDFS").toPredicate();
        clusterAndHdfsAndCriticalPredicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.AlertHistoryResourceProvider.ALERT_HISTORY_CLUSTER_NAME).equals("c1").and().property(org.apache.ambari.server.controller.internal.AlertHistoryResourceProvider.ALERT_HISTORY_SERVICE_NAME).equals("HDFS").and().property(org.apache.ambari.server.controller.internal.AlertHistoryResourceProvider.ALERT_HISTORY_STATE).equals(org.apache.ambari.server.state.AlertState.CRITICAL.name()).toPredicate();
        hdfsAndCriticalOrWarningPredicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().begin().property(org.apache.ambari.server.controller.internal.AlertHistoryResourceProvider.ALERT_HISTORY_SERVICE_NAME).equals("HDFS").and().property(org.apache.ambari.server.controller.internal.AlertHistoryResourceProvider.ALERT_HISTORY_STATE).equals(org.apache.ambari.server.state.AlertState.CRITICAL.name()).end().or().property(org.apache.ambari.server.controller.internal.AlertHistoryResourceProvider.ALERT_HISTORY_STATE).equals(org.apache.ambari.server.state.AlertState.WARNING.name()).toPredicate();
        alertNamePredicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.AlertHistoryResourceProvider.ALERT_HISTORY_DEFINITION_NAME).equals("NAMENODE").toPredicate();
        request.Predicate = hdfsPredicate;
        histories = m_dao.findAll(request);
        org.junit.Assert.assertEquals(2, histories.size());
        request.Predicate = yarnPredicate;
        histories = m_dao.findAll(request);
        org.junit.Assert.assertEquals(1, histories.size());
        request.Predicate = clusterAndHdfsPredicate;
        histories = m_dao.findAll(request);
        org.junit.Assert.assertEquals(2, histories.size());
        request.Predicate = clusterAndHdfsAndCriticalPredicate;
        histories = m_dao.findAll(request);
        org.junit.Assert.assertEquals(0, histories.size());
        request.Predicate = hdfsAndCriticalOrWarningPredicate;
        histories = m_dao.findAll(request);
        org.junit.Assert.assertEquals(1, histories.size());
        request.Predicate = alertNamePredicate;
        histories = m_dao.findAll(request);
        org.junit.Assert.assertEquals(1, histories.size());
        historyIdPredicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.AlertHistoryResourceProvider.ALERT_HISTORY_ID).equals(histories.get(0).getAlertId()).toPredicate();
        request.Predicate = historyIdPredicate;
        histories = m_dao.findAll(request);
        org.junit.Assert.assertEquals(1, histories.size());
    }

    @org.junit.Test
    public void testAlertHistoryPagination() throws java.lang.Exception {
        m_helper.installHdfsService(m_cluster, m_serviceFactory, m_componentFactory, m_schFactory, org.apache.ambari.server.orm.dao.AlertsDAOTest.HOSTNAME);
        m_alertHelper.populateData(m_cluster);
        org.apache.ambari.server.controller.AlertHistoryRequest request = new org.apache.ambari.server.controller.AlertHistoryRequest();
        request.Pagination = null;
        java.util.List<org.apache.ambari.server.orm.entities.AlertHistoryEntity> histories = m_dao.findAll(request);
        org.junit.Assert.assertEquals(3, histories.size());
        request.Pagination = new org.apache.ambari.server.controller.internal.PageRequestImpl(org.apache.ambari.server.controller.spi.PageRequest.StartingPoint.Beginning, 2, 0, null, null);
        histories = m_dao.findAll(request);
        org.junit.Assert.assertEquals(2, histories.size());
        request.Pagination = new org.apache.ambari.server.controller.internal.PageRequestImpl(org.apache.ambari.server.controller.spi.PageRequest.StartingPoint.Beginning, 1, 2, null, null);
        histories = m_dao.findAll(request);
        org.junit.Assert.assertEquals(1, histories.size());
        request.Pagination = new org.apache.ambari.server.controller.internal.PageRequestImpl(org.apache.ambari.server.controller.spi.PageRequest.StartingPoint.Beginning, 1, 3, null, null);
        histories = m_dao.findAll(request);
        org.junit.Assert.assertEquals(0, histories.size());
    }

    @org.junit.Test
    public void testAlertHistorySorting() throws java.lang.Exception {
        m_helper.installHdfsService(m_cluster, m_serviceFactory, m_componentFactory, m_schFactory, org.apache.ambari.server.orm.dao.AlertsDAOTest.HOSTNAME);
        m_alertHelper.populateData(m_cluster);
        java.util.List<org.apache.ambari.server.controller.spi.SortRequestProperty> sortProperties = new java.util.ArrayList<>();
        org.apache.ambari.server.controller.spi.SortRequest sortRequest = new org.apache.ambari.server.controller.internal.SortRequestImpl(sortProperties);
        org.apache.ambari.server.controller.AlertHistoryRequest request = new org.apache.ambari.server.controller.AlertHistoryRequest();
        request.Sort = sortRequest;
        org.apache.ambari.server.controller.spi.Predicate clusterPredicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.AlertHistoryResourceProvider.ALERT_HISTORY_CLUSTER_NAME).equals("c1").toPredicate();
        request.Predicate = clusterPredicate;
        sortProperties.add(new org.apache.ambari.server.controller.spi.SortRequestProperty(org.apache.ambari.server.controller.internal.AlertHistoryResourceProvider.ALERT_HISTORY_SERVICE_NAME, org.apache.ambari.server.controller.spi.SortRequest.Order.ASC));
        java.util.List<org.apache.ambari.server.orm.entities.AlertHistoryEntity> histories = m_dao.findAll(request);
        org.junit.Assert.assertEquals(3, histories.size());
        java.lang.String lastServiceName = null;
        for (org.apache.ambari.server.orm.entities.AlertHistoryEntity history : histories) {
            if (null == lastServiceName) {
                lastServiceName = history.getServiceName();
                continue;
            }
            java.lang.String currentServiceName = history.getServiceName();
            org.junit.Assert.assertTrue(lastServiceName.compareTo(currentServiceName) <= 0);
            lastServiceName = currentServiceName;
        }
        sortProperties.clear();
        sortProperties.add(new org.apache.ambari.server.controller.spi.SortRequestProperty(org.apache.ambari.server.controller.internal.AlertHistoryResourceProvider.ALERT_HISTORY_SERVICE_NAME, org.apache.ambari.server.controller.spi.SortRequest.Order.DESC));
        histories = m_dao.findAll(request);
        org.junit.Assert.assertEquals(3, histories.size());
        lastServiceName = null;
        for (org.apache.ambari.server.orm.entities.AlertHistoryEntity history : histories) {
            if (null == lastServiceName) {
                lastServiceName = history.getServiceName();
                continue;
            }
            java.lang.String currentServiceName = history.getServiceName();
            org.junit.Assert.assertTrue(lastServiceName.compareTo(currentServiceName) >= 0);
            lastServiceName = currentServiceName;
        }
    }

    @org.junit.Test
    public void testRemoveCurrenyByService() throws java.lang.Exception {
        java.util.List<org.apache.ambari.server.orm.entities.AlertCurrentEntity> currentAlerts = m_dao.findCurrent();
        org.junit.Assert.assertNotNull(currentAlerts);
        org.junit.Assert.assertEquals(5, currentAlerts.size());
        m_dao.removeCurrentByService(m_cluster.getClusterId(), "HDFS");
        currentAlerts = m_dao.findCurrent();
        org.junit.Assert.assertEquals(5, currentAlerts.size());
        m_dao.removeCurrentByService(m_cluster.getClusterId(), "YARN");
        currentAlerts = m_dao.findCurrent();
        org.junit.Assert.assertEquals(0, currentAlerts.size());
    }

    @org.junit.Test
    public void testRemoveCurrenyByHost() throws java.lang.Exception {
        java.util.List<org.apache.ambari.server.orm.entities.AlertCurrentEntity> currentAlerts = m_dao.findCurrent();
        org.junit.Assert.assertNotNull(currentAlerts);
        org.junit.Assert.assertEquals(5, currentAlerts.size());
        m_dao.removeCurrentByHost("h2");
        currentAlerts = m_dao.findCurrent();
        org.junit.Assert.assertEquals(5, currentAlerts.size());
        m_dao.removeCurrentByHost("h1");
        currentAlerts = m_dao.findCurrent();
        org.junit.Assert.assertEquals(0, currentAlerts.size());
    }

    @org.junit.Test
    public void testRemoveCurrenyByComponentHost() throws java.lang.Exception {
        java.util.List<org.apache.ambari.server.orm.entities.AlertCurrentEntity> currentAlerts = m_dao.findCurrent();
        org.junit.Assert.assertNotNull(currentAlerts);
        org.junit.Assert.assertEquals(5, currentAlerts.size());
        org.apache.ambari.server.orm.entities.AlertCurrentEntity entity = m_dao.findCurrentByHostAndName(m_cluster.getClusterId(), "h1", "Alert Definition 1");
        org.junit.Assert.assertNotNull(entity);
        m_dao.removeCurrentByServiceComponentHost(m_cluster.getClusterId(), entity.getAlertHistory().getServiceName(), entity.getAlertHistory().getComponentName(), entity.getAlertHistory().getHostName());
        currentAlerts = m_dao.findCurrent();
        org.junit.Assert.assertEquals(4, currentAlerts.size());
    }

    @org.junit.Test
    public void testRemoveCurrentDisabled() throws java.lang.Exception {
        java.util.List<org.apache.ambari.server.orm.entities.AlertCurrentEntity> currentAlerts = m_dao.findCurrent();
        org.junit.Assert.assertNotNull(currentAlerts);
        org.junit.Assert.assertEquals(5, currentAlerts.size());
        org.apache.ambari.server.orm.entities.AlertDefinitionEntity definition = currentAlerts.get(0).getAlertHistory().getAlertDefinition();
        definition.setEnabled(false);
        m_definitionDao.merge(definition);
        m_dao.removeCurrentDisabledAlerts();
        currentAlerts = m_dao.findCurrent();
        org.junit.Assert.assertEquals(4, currentAlerts.size());
    }

    private class MockModule implements com.google.inject.Module {
        @java.lang.Override
        public void configure(com.google.inject.Binder binder) {
            org.apache.ambari.server.events.publishers.HostComponentUpdateEventPublisher hostComponentUpdateEventPublisher = org.easymock.EasyMock.createNiceMock(org.apache.ambari.server.events.publishers.HostComponentUpdateEventPublisher.class);
            org.apache.ambari.server.events.publishers.RequestUpdateEventPublisher requestUpdateEventPublisher = org.easymock.EasyMock.createNiceMock(org.apache.ambari.server.events.publishers.RequestUpdateEventPublisher.class);
            org.apache.ambari.server.events.publishers.ServiceUpdateEventPublisher serviceUpdateEventPublisher = org.easymock.EasyMock.createNiceMock(org.apache.ambari.server.events.publishers.ServiceUpdateEventPublisher.class);
            binder.bind(org.apache.ambari.server.events.publishers.HostComponentUpdateEventPublisher.class).toInstance(hostComponentUpdateEventPublisher);
            binder.bind(org.apache.ambari.server.events.publishers.RequestUpdateEventPublisher.class).toInstance(requestUpdateEventPublisher);
            binder.bind(org.apache.ambari.server.events.publishers.ServiceUpdateEventPublisher.class).toInstance(serviceUpdateEventPublisher);
        }
    }
}