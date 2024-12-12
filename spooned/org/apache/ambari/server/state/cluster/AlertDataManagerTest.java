package org.apache.ambari.server.state.cluster;
import com.google.inject.persist.UnitOfWork;
import javax.persistence.EntityManager;
public class AlertDataManagerTest {
    private static final java.lang.String ALERT_DEFINITION = "Alert Definition 1";

    private static final java.lang.String SERVICE = "HDFS";

    private static final java.lang.String COMPONENT = "DATANODE";

    private static final java.lang.String HOST1 = "h1";

    private static final java.lang.String HOST2 = "h2";

    private static final java.lang.String ALERT_LABEL = "My Label";

    private com.google.inject.Injector m_injector;

    private org.apache.ambari.server.orm.OrmTestHelper m_helper;

    private org.apache.ambari.server.state.Clusters m_clusters;

    private org.apache.ambari.server.state.Cluster m_cluster;

    private org.apache.ambari.server.orm.dao.AlertsDAO m_dao;

    private org.apache.ambari.server.orm.dao.AlertDispatchDAO m_dispatchDao;

    private org.apache.ambari.server.orm.dao.AlertDefinitionDAO m_definitionDao;

    private org.apache.ambari.server.state.ServiceFactory m_serviceFactory;

    private org.apache.ambari.server.state.ServiceComponentFactory m_componentFactory;

    private org.apache.ambari.server.state.ServiceComponentHostFactory m_schFactory;

    @org.junit.Before
    public void setup() throws java.lang.Exception {
        m_injector = com.google.inject.Guice.createInjector(new org.apache.ambari.server.orm.InMemoryDefaultTestModule());
        org.apache.ambari.server.utils.EventBusSynchronizer.synchronizeAlertEventPublisher(m_injector);
        org.apache.ambari.server.utils.EventBusSynchronizer.synchronizeAmbariEventPublisher(m_injector);
        m_injector.getInstance(org.apache.ambari.server.orm.GuiceJpaInitializer.class);
        m_injector.getInstance(com.google.inject.persist.UnitOfWork.class).begin();
        m_helper = m_injector.getInstance(org.apache.ambari.server.orm.OrmTestHelper.class);
        m_dao = m_injector.getInstance(org.apache.ambari.server.orm.dao.AlertsDAO.class);
        m_dispatchDao = m_injector.getInstance(org.apache.ambari.server.orm.dao.AlertDispatchDAO.class);
        m_definitionDao = m_injector.getInstance(org.apache.ambari.server.orm.dao.AlertDefinitionDAO.class);
        m_clusters = m_injector.getInstance(org.apache.ambari.server.state.Clusters.class);
        m_serviceFactory = m_injector.getInstance(org.apache.ambari.server.state.ServiceFactory.class);
        m_componentFactory = m_injector.getInstance(org.apache.ambari.server.state.ServiceComponentFactory.class);
        m_schFactory = m_injector.getInstance(org.apache.ambari.server.state.ServiceComponentHostFactory.class);
        m_cluster = m_helper.buildNewCluster(m_clusters, m_serviceFactory, m_componentFactory, m_schFactory, org.apache.ambari.server.state.cluster.AlertDataManagerTest.HOST1);
        m_helper.addHost(m_clusters, m_cluster, org.apache.ambari.server.state.cluster.AlertDataManagerTest.HOST2);
        m_helper.addHostComponent(m_cluster, org.apache.ambari.server.state.cluster.AlertDataManagerTest.HOST2, org.apache.ambari.server.state.cluster.AlertDataManagerTest.SERVICE, org.apache.ambari.server.state.cluster.AlertDataManagerTest.COMPONENT);
        for (int i = 0; i < 5; i++) {
            org.apache.ambari.server.orm.entities.AlertDefinitionEntity definition = new org.apache.ambari.server.orm.entities.AlertDefinitionEntity();
            definition.setDefinitionName("Alert Definition " + i);
            definition.setServiceName(org.apache.ambari.server.state.cluster.AlertDataManagerTest.SERVICE);
            definition.setComponentName(org.apache.ambari.server.state.cluster.AlertDataManagerTest.COMPONENT);
            definition.setClusterId(m_cluster.getClusterId());
            definition.setHash(java.util.UUID.randomUUID().toString());
            definition.setScheduleInterval(java.lang.Integer.valueOf(60));
            definition.setScope(org.apache.ambari.server.state.alert.Scope.SERVICE);
            definition.setSource("{\"type\" : \"SCRIPT\"}");
            definition.setSourceType(org.apache.ambari.server.state.alert.SourceType.SCRIPT);
            definition.setLabel(org.apache.ambari.server.state.cluster.AlertDataManagerTest.ALERT_LABEL);
            m_definitionDao.create(definition);
        }
    }

    @org.junit.After
    public void teardown() throws org.apache.ambari.server.AmbariException, java.sql.SQLException {
        m_injector.getInstance(com.google.inject.persist.UnitOfWork.class).end();
        org.apache.ambari.server.H2DatabaseCleaner.clearDatabase(m_injector.getProvider(javax.persistence.EntityManager.class).get());
        m_injector = null;
    }

    @org.junit.Test
    public void testAlertRecords() throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.Alert alert1 = new org.apache.ambari.server.state.Alert(org.apache.ambari.server.state.cluster.AlertDataManagerTest.ALERT_DEFINITION, null, org.apache.ambari.server.state.cluster.AlertDataManagerTest.SERVICE, org.apache.ambari.server.state.cluster.AlertDataManagerTest.COMPONENT, org.apache.ambari.server.state.cluster.AlertDataManagerTest.HOST1, org.apache.ambari.server.state.AlertState.OK);
        alert1.setLabel(org.apache.ambari.server.state.cluster.AlertDataManagerTest.ALERT_LABEL);
        alert1.setText("Component component1 is OK");
        alert1.setTimestamp(1L);
        alert1.setClusterId(m_cluster.getClusterId());
        org.apache.ambari.server.state.Alert alert2 = new org.apache.ambari.server.state.Alert(org.apache.ambari.server.state.cluster.AlertDataManagerTest.ALERT_DEFINITION, null, org.apache.ambari.server.state.cluster.AlertDataManagerTest.SERVICE, org.apache.ambari.server.state.cluster.AlertDataManagerTest.COMPONENT, org.apache.ambari.server.state.cluster.AlertDataManagerTest.HOST2, org.apache.ambari.server.state.AlertState.CRITICAL);
        alert2.setLabel(org.apache.ambari.server.state.cluster.AlertDataManagerTest.ALERT_LABEL);
        alert2.setText("Component component2 is not OK");
        alert2.setClusterId(m_cluster.getClusterId());
        org.apache.ambari.server.events.listeners.alerts.AlertReceivedListener listener = m_injector.getInstance(org.apache.ambari.server.events.listeners.alerts.AlertReceivedListener.class);
        org.apache.ambari.server.events.AlertReceivedEvent event1 = new org.apache.ambari.server.events.AlertReceivedEvent(m_cluster.getClusterId(), alert1);
        org.apache.ambari.server.events.AlertReceivedEvent event2 = new org.apache.ambari.server.events.AlertReceivedEvent(m_cluster.getClusterId(), alert2);
        listener.onAlertEvent(event1);
        listener.onAlertEvent(event2);
        java.util.List<org.apache.ambari.server.orm.entities.AlertCurrentEntity> allCurrent = m_dao.findCurrentByService(m_cluster.getClusterId(), org.apache.ambari.server.state.cluster.AlertDataManagerTest.SERVICE);
        org.junit.Assert.assertEquals(2, allCurrent.size());
        java.util.List<org.apache.ambari.server.orm.entities.AlertHistoryEntity> allHistory = m_dao.findAll(m_cluster.getClusterId());
        org.junit.Assert.assertEquals(2, allHistory.size());
        org.apache.ambari.server.orm.entities.AlertCurrentEntity current = m_dao.findCurrentByHostAndName(m_cluster.getClusterId(), org.apache.ambari.server.state.cluster.AlertDataManagerTest.HOST1, org.apache.ambari.server.state.cluster.AlertDataManagerTest.ALERT_DEFINITION);
        org.junit.Assert.assertNotNull(current);
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.cluster.AlertDataManagerTest.HOST1, current.getAlertHistory().getHostName());
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.cluster.AlertDataManagerTest.ALERT_DEFINITION, current.getAlertHistory().getAlertDefinition().getDefinitionName());
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.cluster.AlertDataManagerTest.ALERT_LABEL, current.getAlertHistory().getAlertLabel());
        org.junit.Assert.assertEquals("Component component1 is OK", current.getAlertHistory().getAlertText());
        org.junit.Assert.assertEquals(current.getAlertHistory().getAlertState(), org.apache.ambari.server.state.AlertState.OK);
        org.junit.Assert.assertEquals(1L, current.getOriginalTimestamp().longValue());
        org.junit.Assert.assertEquals(1L, current.getLatestTimestamp().longValue());
        java.lang.Long currentId = current.getAlertId();
        java.lang.Long historyId = current.getAlertHistory().getAlertId();
        org.apache.ambari.server.state.Alert alert3 = new org.apache.ambari.server.state.Alert(org.apache.ambari.server.state.cluster.AlertDataManagerTest.ALERT_DEFINITION, null, org.apache.ambari.server.state.cluster.AlertDataManagerTest.SERVICE, org.apache.ambari.server.state.cluster.AlertDataManagerTest.COMPONENT, org.apache.ambari.server.state.cluster.AlertDataManagerTest.HOST1, org.apache.ambari.server.state.AlertState.OK);
        alert3.setLabel(org.apache.ambari.server.state.cluster.AlertDataManagerTest.ALERT_LABEL);
        alert3.setText("Component component1 is OK");
        alert3.setTimestamp(2L);
        alert3.setClusterId(m_cluster.getClusterId());
        org.apache.ambari.server.events.AlertReceivedEvent event3 = new org.apache.ambari.server.events.AlertReceivedEvent(m_cluster.getClusterId(), alert3);
        listener.onAlertEvent(event3);
        current = m_dao.findCurrentByHostAndName(m_cluster.getClusterId(), org.apache.ambari.server.state.cluster.AlertDataManagerTest.HOST1, org.apache.ambari.server.state.cluster.AlertDataManagerTest.ALERT_DEFINITION);
        org.junit.Assert.assertNotNull(current);
        org.junit.Assert.assertEquals(currentId, current.getAlertId());
        org.junit.Assert.assertEquals(historyId, current.getAlertHistory().getAlertId());
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.cluster.AlertDataManagerTest.HOST1, current.getAlertHistory().getHostName());
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.cluster.AlertDataManagerTest.ALERT_DEFINITION, current.getAlertHistory().getAlertDefinition().getDefinitionName());
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.cluster.AlertDataManagerTest.ALERT_LABEL, current.getAlertHistory().getAlertLabel());
        org.junit.Assert.assertEquals("Component component1 is OK", current.getAlertHistory().getAlertText());
        org.junit.Assert.assertEquals(current.getAlertHistory().getAlertState(), org.apache.ambari.server.state.AlertState.OK);
        org.junit.Assert.assertEquals(1L, current.getOriginalTimestamp().longValue());
        org.junit.Assert.assertEquals(2L, current.getLatestTimestamp().longValue());
        allCurrent = m_dao.findCurrentByService(m_cluster.getClusterId(), org.apache.ambari.server.state.cluster.AlertDataManagerTest.SERVICE);
        org.junit.Assert.assertEquals(2, allCurrent.size());
        allHistory = m_dao.findAll(m_cluster.getClusterId());
        org.junit.Assert.assertEquals(2, allHistory.size());
        org.apache.ambari.server.state.Alert alert4 = new org.apache.ambari.server.state.Alert(org.apache.ambari.server.state.cluster.AlertDataManagerTest.ALERT_DEFINITION, null, org.apache.ambari.server.state.cluster.AlertDataManagerTest.SERVICE, org.apache.ambari.server.state.cluster.AlertDataManagerTest.COMPONENT, org.apache.ambari.server.state.cluster.AlertDataManagerTest.HOST1, org.apache.ambari.server.state.AlertState.WARNING);
        alert4.setLabel(org.apache.ambari.server.state.cluster.AlertDataManagerTest.ALERT_LABEL);
        alert4.setText("Component component1 is about to go down");
        alert4.setTimestamp(3L);
        alert4.setClusterId(m_cluster.getClusterId());
        org.apache.ambari.server.events.AlertReceivedEvent event4 = new org.apache.ambari.server.events.AlertReceivedEvent(m_cluster.getClusterId(), alert4);
        listener.onAlertEvent(event4);
        current = m_dao.findCurrentByHostAndName(m_cluster.getClusterId(), org.apache.ambari.server.state.cluster.AlertDataManagerTest.HOST1, org.apache.ambari.server.state.cluster.AlertDataManagerTest.ALERT_DEFINITION);
        org.junit.Assert.assertNotNull(current);
        org.junit.Assert.assertEquals(current.getAlertId(), currentId);
        org.junit.Assert.assertFalse(historyId.equals(current.getAlertHistory().getAlertId()));
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.cluster.AlertDataManagerTest.HOST1, current.getAlertHistory().getHostName());
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.cluster.AlertDataManagerTest.ALERT_DEFINITION, current.getAlertHistory().getAlertDefinition().getDefinitionName());
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.cluster.AlertDataManagerTest.ALERT_LABEL, current.getAlertHistory().getAlertLabel());
        org.junit.Assert.assertEquals("Component component1 is about to go down", current.getAlertHistory().getAlertText());
        org.junit.Assert.assertEquals(current.getAlertHistory().getAlertState(), org.apache.ambari.server.state.AlertState.WARNING);
        org.junit.Assert.assertEquals(3L, current.getOriginalTimestamp().longValue());
        org.junit.Assert.assertEquals(3L, current.getLatestTimestamp().longValue());
        allCurrent = m_dao.findCurrentByService(m_cluster.getClusterId(), org.apache.ambari.server.state.cluster.AlertDataManagerTest.SERVICE);
        org.junit.Assert.assertEquals(2, allCurrent.size());
        allHistory = m_dao.findAll(m_cluster.getClusterId());
        org.junit.Assert.assertEquals(3, allHistory.size());
    }

    @org.junit.Test
    public void testAlertNotices() throws java.lang.Exception {
        java.util.List<org.apache.ambari.server.orm.entities.AlertNoticeEntity> notices = m_dispatchDao.findAllNotices();
        org.junit.Assert.assertEquals(0, notices.size());
        java.util.List<org.apache.ambari.server.orm.entities.AlertDefinitionEntity> definitions = m_definitionDao.findAll(m_cluster.getClusterId());
        org.apache.ambari.server.orm.entities.AlertDefinitionEntity definition = definitions.get(0);
        org.apache.ambari.server.orm.entities.AlertHistoryEntity history = new org.apache.ambari.server.orm.entities.AlertHistoryEntity();
        history.setServiceName(definition.getServiceName());
        history.setClusterId(m_cluster.getClusterId());
        history.setAlertDefinition(definition);
        history.setAlertLabel(definition.getDefinitionName());
        history.setAlertText(definition.getDefinitionName());
        history.setAlertTimestamp(java.lang.System.currentTimeMillis());
        history.setHostName(org.apache.ambari.server.state.cluster.AlertDataManagerTest.HOST1);
        history.setAlertState(org.apache.ambari.server.state.AlertState.OK);
        m_dao.create(history);
        java.util.List<org.apache.ambari.server.orm.entities.AlertHistoryEntity> histories = m_dao.findAll(m_cluster.getClusterId());
        org.junit.Assert.assertEquals(1, histories.size());
        org.apache.ambari.server.orm.entities.AlertCurrentEntity currentAlert = new org.apache.ambari.server.orm.entities.AlertCurrentEntity();
        currentAlert.setAlertHistory(histories.get(0));
        currentAlert.setMaintenanceState(org.apache.ambari.server.state.MaintenanceState.OFF);
        currentAlert.setOriginalTimestamp(java.lang.System.currentTimeMillis());
        currentAlert.setLatestTimestamp(java.lang.System.currentTimeMillis());
        m_dao.create(currentAlert);
        org.apache.ambari.server.orm.entities.AlertTargetEntity target = m_helper.createAlertTarget();
        java.util.Set<org.apache.ambari.server.orm.entities.AlertTargetEntity> targets = new java.util.HashSet<>();
        targets.add(target);
        org.apache.ambari.server.orm.entities.AlertGroupEntity group = m_helper.createAlertGroup(m_cluster.getClusterId(), targets);
        group.addAlertDefinition(definitions.get(0));
        m_dispatchDao.merge(group);
        org.apache.ambari.server.state.Alert alert1 = new org.apache.ambari.server.state.Alert(org.apache.ambari.server.state.cluster.AlertDataManagerTest.ALERT_DEFINITION, null, org.apache.ambari.server.state.cluster.AlertDataManagerTest.SERVICE, org.apache.ambari.server.state.cluster.AlertDataManagerTest.COMPONENT, org.apache.ambari.server.state.cluster.AlertDataManagerTest.HOST1, org.apache.ambari.server.state.AlertState.OK);
        org.apache.ambari.server.events.AlertStateChangeEvent event = new org.apache.ambari.server.events.AlertStateChangeEvent(m_cluster.getClusterId(), alert1, currentAlert, org.apache.ambari.server.state.AlertState.CRITICAL, org.apache.ambari.server.state.AlertFirmness.HARD);
        org.apache.ambari.server.events.listeners.alerts.AlertStateChangedListener listener = m_injector.getInstance(org.apache.ambari.server.events.listeners.alerts.AlertStateChangedListener.class);
        listener.onAlertEvent(event);
        notices = m_dispatchDao.findAllNotices();
        org.junit.Assert.assertEquals(1, notices.size());
    }

    @org.junit.Test
    public void testAggregateAlerts() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.AlertDefinitionEntity definition = new org.apache.ambari.server.orm.entities.AlertDefinitionEntity();
        definition.setDefinitionName("to_aggregate");
        definition.setLabel("My Label");
        definition.setLabel("My Description");
        definition.setServiceName(org.apache.ambari.server.state.cluster.AlertDataManagerTest.SERVICE);
        definition.setComponentName(null);
        definition.setClusterId(m_cluster.getClusterId());
        definition.setHash(java.util.UUID.randomUUID().toString());
        definition.setScheduleInterval(java.lang.Integer.valueOf(60));
        definition.setScope(org.apache.ambari.server.state.alert.Scope.HOST);
        definition.setSource("{\"type\" : \"SCRIPT\"}");
        definition.setSourceType(org.apache.ambari.server.state.alert.SourceType.SCRIPT);
        m_definitionDao.create(definition);
        org.apache.ambari.server.orm.entities.AlertDefinitionEntity aggDef = new org.apache.ambari.server.orm.entities.AlertDefinitionEntity();
        aggDef.setDefinitionName("aggregate_test");
        aggDef.setServiceName(org.apache.ambari.server.state.cluster.AlertDataManagerTest.SERVICE);
        aggDef.setComponentName(null);
        aggDef.setClusterId(m_cluster.getClusterId());
        aggDef.setHash(java.util.UUID.randomUUID().toString());
        aggDef.setScheduleInterval(java.lang.Integer.valueOf(60));
        aggDef.setScope(org.apache.ambari.server.state.alert.Scope.SERVICE);
        org.apache.ambari.server.state.alert.AggregateSource source = new org.apache.ambari.server.state.alert.AggregateSource();
        source.setAlertName("to_aggregate");
        java.lang.reflect.Field field = org.apache.ambari.server.state.alert.Source.class.getDeclaredField("type");
        field.setAccessible(true);
        field.set(source, org.apache.ambari.server.state.alert.SourceType.AGGREGATE);
        org.apache.ambari.server.state.alert.Reporting reporting = new org.apache.ambari.server.state.alert.Reporting();
        org.apache.ambari.server.state.alert.Reporting.ReportTemplate template = new org.apache.ambari.server.state.alert.Reporting.ReportTemplate();
        template.setText("You are good {1}/{0}");
        reporting.setOk(template);
        template = new org.apache.ambari.server.state.alert.Reporting.ReportTemplate();
        template.setText("Going bad {1}/{0}");
        template.setValue(java.lang.Double.valueOf(0.33));
        reporting.setWarning(template);
        template = new org.apache.ambari.server.state.alert.Reporting.ReportTemplate();
        template.setText("On fire! {1}/{0}");
        template.setValue(java.lang.Double.valueOf(0.66));
        reporting.setCritical(template);
        source.setReporting(reporting);
        com.google.gson.Gson gson = new com.google.gson.Gson();
        aggDef.setSource(gson.toJson(source));
        aggDef.setSourceType(org.apache.ambari.server.state.alert.SourceType.AGGREGATE);
        m_definitionDao.create(aggDef);
        for (int i = 0; i < 4; i++) {
            org.apache.ambari.server.orm.entities.AlertHistoryEntity history = new org.apache.ambari.server.orm.entities.AlertHistoryEntity();
            history.setAlertDefinition(definition);
            history.setAlertInstance(null);
            history.setAlertLabel(definition.getLabel());
            history.setAlertState(org.apache.ambari.server.state.AlertState.OK);
            history.setAlertText("OK");
            history.setAlertTimestamp(java.lang.Long.valueOf(1));
            history.setClusterId(m_cluster.getClusterId());
            history.setComponentName(definition.getComponentName());
            history.setHostName("h" + (i + 1));
            history.setServiceName(definition.getServiceName());
            m_dao.create(history);
            org.apache.ambari.server.orm.entities.AlertCurrentEntity current = new org.apache.ambari.server.orm.entities.AlertCurrentEntity();
            current.setAlertHistory(history);
            current.setLatestText(history.getAlertText());
            current.setLatestTimestamp(java.lang.Long.valueOf(1L));
            current.setOriginalTimestamp(java.lang.Long.valueOf(1L));
            m_dao.merge(current);
        }
        org.apache.ambari.server.events.publishers.AlertEventPublisher publisher = m_injector.getInstance(org.apache.ambari.server.events.publishers.AlertEventPublisher.class);
        org.apache.ambari.server.utils.EventBusSynchronizer.synchronizeAlertEventPublisher(m_injector);
        final java.util.concurrent.atomic.AtomicReference<org.apache.ambari.server.state.Alert> ref = new java.util.concurrent.atomic.AtomicReference<>();
        publisher.register(new org.apache.ambari.server.state.cluster.AlertDataManagerTest.TestListener() {
            @java.lang.Override
            @com.google.common.eventbus.Subscribe
            public void catchIt(org.apache.ambari.server.events.AlertReceivedEvent event) {
                ref.set(event.getAlert());
            }
        });
        org.apache.ambari.server.events.listeners.alerts.AlertAggregateListener listener = m_injector.getInstance(org.apache.ambari.server.events.listeners.alerts.AlertAggregateListener.class);
        org.apache.ambari.server.state.alert.AlertDefinitionFactory factory = new org.apache.ambari.server.state.alert.AlertDefinitionFactory();
        org.apache.ambari.server.state.alert.AggregateDefinitionMapping aggregateMapping = m_injector.getInstance(org.apache.ambari.server.state.alert.AggregateDefinitionMapping.class);
        org.apache.ambari.server.state.alert.AlertDefinition aggregateDefinition = factory.coerce(aggDef);
        aggregateMapping.registerAggregate(m_cluster.getClusterId(), aggregateDefinition);
        junit.framework.Assert.assertEquals(aggregateDefinition, aggregateMapping.getAggregateDefinitions(m_cluster.getClusterId()).get(0));
        junit.framework.Assert.assertEquals(definition.getDefinitionName(), aggregateMapping.getAlertsWithAggregates(m_cluster.getClusterId()).get(0));
        org.apache.ambari.server.state.alert.AggregateSource as = ((org.apache.ambari.server.state.alert.AggregateSource) (aggregateDefinition.getSource()));
        org.apache.ambari.server.state.alert.AlertDefinition aggregatedDefinition = aggregateMapping.getAggregateDefinition(m_cluster.getClusterId(), as.getAlertName());
        org.junit.Assert.assertNotNull(aggregatedDefinition);
        org.apache.ambari.server.state.Alert alert = new org.apache.ambari.server.state.Alert(definition.getDefinitionName(), null, definition.getServiceName(), definition.getComponentName(), "h1", org.apache.ambari.server.state.AlertState.OK);
        org.apache.ambari.server.orm.entities.AlertCurrentEntity current = m_dao.findCurrentByHostAndName(m_cluster.getClusterId(), "h1", definition.getDefinitionName());
        org.apache.ambari.server.events.AlertStateChangeEvent event = new org.apache.ambari.server.events.AlertStateChangeEvent(m_cluster.getClusterId(), alert, current, org.apache.ambari.server.state.AlertState.OK, org.apache.ambari.server.state.AlertFirmness.HARD);
        listener.onAlertStateChangeEvent(event);
        org.junit.Assert.assertNotNull(ref.get());
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.AlertState.OK, ref.get().getState());
        org.junit.Assert.assertTrue(ref.get().getText().indexOf("0/4") > (-1));
        current.getAlertHistory().setAlertState(org.apache.ambari.server.state.AlertState.CRITICAL);
        m_dao.merge(current.getAlertHistory());
        listener.onAlertStateChangeEvent(event);
        org.junit.Assert.assertEquals("aggregate_test", ref.get().getName());
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.AlertState.OK, ref.get().getState());
        org.junit.Assert.assertTrue(ref.get().getText().indexOf("1/4") > (-1));
        current = m_dao.findCurrentByHostAndName(m_cluster.getClusterId(), "h2", definition.getDefinitionName());
        current.getAlertHistory().setAlertState(org.apache.ambari.server.state.AlertState.WARNING);
        m_dao.merge(current.getAlertHistory());
        listener.onAlertStateChangeEvent(event);
        org.junit.Assert.assertEquals("aggregate_test", ref.get().getName());
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.AlertState.WARNING, ref.get().getState());
        org.junit.Assert.assertTrue(ref.get().getText().indexOf("2/4") > (-1));
        current = m_dao.findCurrentByHostAndName(m_cluster.getClusterId(), "h3", definition.getDefinitionName());
        current.getAlertHistory().setAlertState(org.apache.ambari.server.state.AlertState.CRITICAL);
        m_dao.merge(current.getAlertHistory());
        listener.onAlertStateChangeEvent(event);
        org.junit.Assert.assertEquals("aggregate_test", ref.get().getName());
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.AlertState.CRITICAL, ref.get().getState());
        org.junit.Assert.assertTrue(ref.get().getText().indexOf("3/4") > (-1));
    }

    private interface TestListener {
        void catchIt(org.apache.ambari.server.events.AlertReceivedEvent event);
    }
}