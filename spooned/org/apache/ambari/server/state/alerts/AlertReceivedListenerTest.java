package org.apache.ambari.server.state.alerts;
import com.google.inject.persist.UnitOfWork;
import javax.persistence.EntityManager;
@org.junit.experimental.categories.Category({ category.AlertTest.class })
public class AlertReceivedListenerTest {
    private static final java.lang.String ALERT_DEFINITION = "alert_definition_";

    private static final java.lang.String AMBARI_ALERT_DEFINITION = "ambari_server_alert";

    private static final java.lang.String HOST1 = "h1";

    private static final java.lang.String ALERT_LABEL = "My Label";

    private com.google.inject.Injector m_injector;

    private org.apache.ambari.server.orm.dao.AlertsDAO m_dao;

    private org.apache.ambari.server.orm.dao.AlertDefinitionDAO m_definitionDao;

    private org.apache.ambari.server.state.Clusters m_clusters;

    private org.apache.ambari.server.state.Cluster m_cluster;

    private org.apache.ambari.server.orm.OrmTestHelper m_helper;

    private org.apache.ambari.server.state.ServiceFactory m_serviceFactory;

    private org.apache.ambari.server.state.ServiceComponentFactory m_componentFactory;

    private org.apache.ambari.server.state.ServiceComponentHostFactory m_schFactory;

    @org.junit.Before
    public void setup() throws java.lang.Exception {
        m_injector = com.google.inject.Guice.createInjector(new org.apache.ambari.server.orm.InMemoryDefaultTestModule());
        m_injector.getInstance(org.apache.ambari.server.orm.GuiceJpaInitializer.class);
        m_injector.getInstance(com.google.inject.persist.UnitOfWork.class).begin();
        m_helper = m_injector.getInstance(org.apache.ambari.server.orm.OrmTestHelper.class);
        m_clusters = m_injector.getInstance(org.apache.ambari.server.state.Clusters.class);
        m_serviceFactory = m_injector.getInstance(org.apache.ambari.server.state.ServiceFactory.class);
        m_componentFactory = m_injector.getInstance(org.apache.ambari.server.state.ServiceComponentFactory.class);
        m_schFactory = m_injector.getInstance(org.apache.ambari.server.state.ServiceComponentHostFactory.class);
        m_dao = m_injector.getInstance(org.apache.ambari.server.orm.dao.AlertsDAO.class);
        m_definitionDao = m_injector.getInstance(org.apache.ambari.server.orm.dao.AlertDefinitionDAO.class);
        org.apache.ambari.server.utils.EventBusSynchronizer.synchronizeAlertEventPublisher(m_injector);
        org.apache.ambari.server.utils.EventBusSynchronizer.synchronizeAmbariEventPublisher(m_injector);
        m_cluster = m_helper.buildNewCluster(m_clusters, m_serviceFactory, m_componentFactory, m_schFactory, org.apache.ambari.server.state.alerts.AlertReceivedListenerTest.HOST1);
        for (int i = 0; i < 5; i++) {
            java.lang.String serviceName = "HDFS";
            java.lang.String componentName = "DATANODE";
            if (i >= 3) {
                serviceName = "YARN";
                componentName = "RESOURCEMANAGER";
            }
            org.apache.ambari.server.orm.entities.AlertDefinitionEntity definition = new org.apache.ambari.server.orm.entities.AlertDefinitionEntity();
            definition.setDefinitionName(org.apache.ambari.server.state.alerts.AlertReceivedListenerTest.ALERT_DEFINITION + i);
            definition.setServiceName(serviceName);
            definition.setComponentName(componentName);
            definition.setClusterId(m_cluster.getClusterId());
            definition.setHash(java.util.UUID.randomUUID().toString());
            definition.setScheduleInterval(java.lang.Integer.valueOf(60));
            definition.setScope(org.apache.ambari.server.state.alert.Scope.SERVICE);
            definition.setSource("{\"type\" : \"SCRIPT\"}");
            definition.setSourceType(org.apache.ambari.server.state.alert.SourceType.SCRIPT);
            m_definitionDao.create(definition);
        }
        org.apache.ambari.server.orm.entities.AlertDefinitionEntity definition = new org.apache.ambari.server.orm.entities.AlertDefinitionEntity();
        definition.setDefinitionName(org.apache.ambari.server.state.alerts.AlertReceivedListenerTest.AMBARI_ALERT_DEFINITION);
        definition.setServiceName(org.apache.ambari.server.controller.RootService.AMBARI.name());
        definition.setComponentName(org.apache.ambari.server.controller.RootComponent.AMBARI_SERVER.name());
        definition.setClusterId(m_cluster.getClusterId());
        definition.setHash(java.util.UUID.randomUUID().toString());
        definition.setScheduleInterval(java.lang.Integer.valueOf(60));
        definition.setScope(org.apache.ambari.server.state.alert.Scope.SERVICE);
        definition.setSource("{\"type\" : \"SCRIPT\"}");
        definition.setSourceType(org.apache.ambari.server.state.alert.SourceType.SCRIPT);
        m_definitionDao.create(definition);
    }

    @org.junit.After
    public void teardown() throws org.apache.ambari.server.AmbariException, java.sql.SQLException {
        m_injector.getInstance(com.google.inject.persist.UnitOfWork.class).end();
        org.apache.ambari.server.H2DatabaseCleaner.clearDatabase(m_injector.getProvider(javax.persistence.EntityManager.class).get());
        m_injector = null;
    }

    @org.junit.Test
    public void testDisabledAlert() throws org.apache.ambari.server.AmbariException {
        java.lang.String definitionName = org.apache.ambari.server.state.alerts.AlertReceivedListenerTest.ALERT_DEFINITION + "1";
        java.lang.String componentName = "DATANODE";
        org.apache.ambari.server.state.Alert alert1 = new org.apache.ambari.server.state.Alert(definitionName, null, "HDFS", componentName, org.apache.ambari.server.state.alerts.AlertReceivedListenerTest.HOST1, org.apache.ambari.server.state.AlertState.OK);
        alert1.setClusterId(m_cluster.getClusterId());
        alert1.setLabel(org.apache.ambari.server.state.alerts.AlertReceivedListenerTest.ALERT_LABEL);
        alert1.setText(("HDFS " + componentName) + " is OK");
        alert1.setTimestamp(1L);
        org.apache.ambari.server.events.listeners.alerts.AlertReceivedListener listener = m_injector.getInstance(org.apache.ambari.server.events.listeners.alerts.AlertReceivedListener.class);
        org.apache.ambari.server.events.AlertReceivedEvent event1 = new org.apache.ambari.server.events.AlertReceivedEvent(m_cluster.getClusterId(), alert1);
        listener.onAlertEvent(event1);
        java.util.List<org.apache.ambari.server.orm.entities.AlertCurrentEntity> allCurrent = m_dao.findCurrent();
        org.junit.Assert.assertEquals(1, allCurrent.size());
        org.apache.ambari.server.orm.entities.AlertDefinitionEntity definition = m_definitionDao.findByName(m_cluster.getClusterId(), definitionName);
        definition.setEnabled(false);
        m_definitionDao.merge(definition);
        m_dao.removeCurrentDisabledAlerts();
        allCurrent = m_dao.findCurrent();
        org.junit.Assert.assertEquals(0, allCurrent.size());
        listener.onAlertEvent(event1);
        allCurrent = m_dao.findCurrent();
        org.junit.Assert.assertEquals(0, allCurrent.size());
    }

    @org.junit.Test
    public void testInvalidHost() throws org.apache.ambari.server.AmbariException {
        java.lang.String definitionName = org.apache.ambari.server.state.alerts.AlertReceivedListenerTest.ALERT_DEFINITION + "1";
        java.lang.String componentName = "DATANODE";
        org.apache.ambari.server.state.Alert alert = new org.apache.ambari.server.state.Alert(definitionName, null, "HDFS", componentName, org.apache.ambari.server.state.alerts.AlertReceivedListenerTest.HOST1, org.apache.ambari.server.state.AlertState.OK);
        alert.setClusterId(m_cluster.getClusterId());
        alert.setLabel(org.apache.ambari.server.state.alerts.AlertReceivedListenerTest.ALERT_LABEL);
        alert.setText(("HDFS " + componentName) + " is OK");
        alert.setTimestamp(1L);
        org.apache.ambari.server.events.listeners.alerts.AlertReceivedListener listener = m_injector.getInstance(org.apache.ambari.server.events.listeners.alerts.AlertReceivedListener.class);
        org.apache.ambari.server.events.AlertReceivedEvent event = new org.apache.ambari.server.events.AlertReceivedEvent(m_cluster.getClusterId(), alert);
        listener.onAlertEvent(event);
        java.util.List<org.apache.ambari.server.orm.entities.AlertCurrentEntity> allCurrent = m_dao.findCurrent();
        org.junit.Assert.assertEquals(1, allCurrent.size());
        alert.setHostName("INVALID");
        m_dao.removeCurrentByHost(org.apache.ambari.server.state.alerts.AlertReceivedListenerTest.HOST1);
        allCurrent = m_dao.findCurrent();
        org.junit.Assert.assertEquals(0, allCurrent.size());
        listener.onAlertEvent(event);
        allCurrent = m_dao.findCurrent();
        org.junit.Assert.assertEquals(0, allCurrent.size());
    }

    @org.junit.Test
    public void testInvalidAlertDefinition() throws org.apache.ambari.server.AmbariException {
        java.lang.String componentName = "DATANODE";
        org.apache.ambari.server.state.Alert alert = new org.apache.ambari.server.state.Alert("missing_alert_definition_name", null, "HDFS", componentName, org.apache.ambari.server.state.alerts.AlertReceivedListenerTest.HOST1, org.apache.ambari.server.state.AlertState.OK);
        alert.setLabel(org.apache.ambari.server.state.alerts.AlertReceivedListenerTest.ALERT_LABEL);
        alert.setText(("HDFS " + componentName) + " is OK");
        alert.setTimestamp(1L);
        org.apache.ambari.server.events.listeners.alerts.AlertReceivedListener listener = m_injector.getInstance(org.apache.ambari.server.events.listeners.alerts.AlertReceivedListener.class);
        org.apache.ambari.server.events.AlertReceivedEvent event1 = new org.apache.ambari.server.events.AlertReceivedEvent(m_cluster.getClusterId(), alert);
        listener.onAlertEvent(event1);
        java.util.List<org.apache.ambari.server.orm.entities.AlertCurrentEntity> allCurrent = m_dao.findCurrent();
        org.junit.Assert.assertEquals(0, allCurrent.size());
    }

    @org.junit.Test
    public void testInvalidServiceComponentHost() throws org.apache.ambari.server.AmbariException {
        java.lang.String definitionName = org.apache.ambari.server.state.alerts.AlertReceivedListenerTest.ALERT_DEFINITION + "1";
        java.lang.String componentName = "DATANODE";
        org.apache.ambari.server.state.Alert alert = new org.apache.ambari.server.state.Alert(definitionName, null, "HDFS", componentName, org.apache.ambari.server.state.alerts.AlertReceivedListenerTest.HOST1, org.apache.ambari.server.state.AlertState.OK);
        alert.setClusterId(m_cluster.getClusterId());
        alert.setLabel(org.apache.ambari.server.state.alerts.AlertReceivedListenerTest.ALERT_LABEL);
        alert.setText(("HDFS " + componentName) + " is OK");
        alert.setTimestamp(1L);
        org.apache.ambari.server.events.listeners.alerts.AlertReceivedListener listener = m_injector.getInstance(org.apache.ambari.server.events.listeners.alerts.AlertReceivedListener.class);
        org.apache.ambari.server.events.AlertReceivedEvent event1 = new org.apache.ambari.server.events.AlertReceivedEvent(m_cluster.getClusterId(), alert);
        listener.onAlertEvent(event1);
        java.util.List<org.apache.ambari.server.orm.entities.AlertCurrentEntity> allCurrent = m_dao.findCurrent();
        org.junit.Assert.assertEquals(1, allCurrent.size());
        alert.setHostName("invalid_host_name");
        m_dao.removeCurrentByHost(org.apache.ambari.server.state.alerts.AlertReceivedListenerTest.HOST1);
        allCurrent = m_dao.findCurrent();
        org.junit.Assert.assertEquals(0, allCurrent.size());
        listener.onAlertEvent(event1);
        allCurrent = m_dao.findCurrent();
        org.junit.Assert.assertEquals(0, allCurrent.size());
    }

    @org.junit.Test
    public void testMaintenanceModeSet() throws java.lang.Exception {
        java.lang.String definitionName = org.apache.ambari.server.state.alerts.AlertReceivedListenerTest.ALERT_DEFINITION + "1";
        java.lang.String componentName = "DATANODE";
        org.apache.ambari.server.state.Alert alert1 = new org.apache.ambari.server.state.Alert(definitionName, null, "HDFS", componentName, org.apache.ambari.server.state.alerts.AlertReceivedListenerTest.HOST1, org.apache.ambari.server.state.AlertState.CRITICAL);
        alert1.setClusterId(m_cluster.getClusterId());
        alert1.setLabel(org.apache.ambari.server.state.alerts.AlertReceivedListenerTest.ALERT_LABEL);
        alert1.setText(("HDFS " + componentName) + " is OK");
        alert1.setTimestamp(1L);
        org.apache.ambari.server.events.listeners.alerts.AlertReceivedListener listener = m_injector.getInstance(org.apache.ambari.server.events.listeners.alerts.AlertReceivedListener.class);
        org.apache.ambari.server.events.AlertReceivedEvent event = new org.apache.ambari.server.events.AlertReceivedEvent(m_cluster.getClusterId(), alert1);
        listener.onAlertEvent(event);
        java.util.List<org.apache.ambari.server.orm.entities.AlertCurrentEntity> allCurrent = m_dao.findCurrent();
        org.junit.Assert.assertEquals(1, allCurrent.size());
        org.apache.ambari.server.orm.entities.AlertCurrentEntity current = allCurrent.get(0);
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.MaintenanceState.OFF, current.getMaintenanceState());
        m_dao.removeCurrentByService(m_cluster.getClusterId(), "HDFS");
        allCurrent = m_dao.findCurrent();
        org.junit.Assert.assertEquals(0, allCurrent.size());
        m_cluster.getService("HDFS").setMaintenanceState(org.apache.ambari.server.state.MaintenanceState.ON);
        listener.onAlertEvent(event);
        allCurrent = m_dao.findCurrent();
        org.junit.Assert.assertEquals(1, allCurrent.size());
        current = allCurrent.get(0);
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.MaintenanceState.ON, current.getMaintenanceState());
    }

    @org.junit.Test
    public void testAgentAlertFromInvalidHost() throws org.apache.ambari.server.AmbariException {
        java.lang.String definitionName = org.apache.ambari.server.state.alerts.AlertReceivedListenerTest.ALERT_DEFINITION + "1";
        java.lang.String serviceName = org.apache.ambari.server.controller.RootService.AMBARI.name();
        java.lang.String componentName = org.apache.ambari.server.controller.RootComponent.AMBARI_AGENT.name();
        org.apache.ambari.server.state.Alert alert = new org.apache.ambari.server.state.Alert(definitionName, null, serviceName, componentName, org.apache.ambari.server.state.alerts.AlertReceivedListenerTest.HOST1, org.apache.ambari.server.state.AlertState.OK);
        alert.setClusterId(m_cluster.getClusterId());
        alert.setLabel(org.apache.ambari.server.state.alerts.AlertReceivedListenerTest.ALERT_LABEL);
        alert.setText(((serviceName + " ") + componentName) + " is OK");
        alert.setTimestamp(1L);
        org.apache.ambari.server.events.listeners.alerts.AlertReceivedListener listener = m_injector.getInstance(org.apache.ambari.server.events.listeners.alerts.AlertReceivedListener.class);
        org.apache.ambari.server.events.AlertReceivedEvent event = new org.apache.ambari.server.events.AlertReceivedEvent(m_cluster.getClusterId(), alert);
        listener.onAlertEvent(event);
        java.util.List<org.apache.ambari.server.orm.entities.AlertCurrentEntity> allCurrent = m_dao.findCurrent();
        org.junit.Assert.assertEquals(1, allCurrent.size());
        alert.setHostName("INVALID");
        m_dao.removeCurrentByHost(org.apache.ambari.server.state.alerts.AlertReceivedListenerTest.HOST1);
        allCurrent = m_dao.findCurrent();
        org.junit.Assert.assertEquals(0, allCurrent.size());
        listener.onAlertEvent(event);
        allCurrent = m_dao.findCurrent();
        org.junit.Assert.assertEquals(0, allCurrent.size());
    }

    @org.junit.Test
    public void testAmbariServerValidAlerts() throws org.apache.ambari.server.AmbariException {
        java.lang.String definitionName = org.apache.ambari.server.state.alerts.AlertReceivedListenerTest.AMBARI_ALERT_DEFINITION;
        java.lang.String serviceName = org.apache.ambari.server.controller.RootService.AMBARI.name();
        java.lang.String componentName = org.apache.ambari.server.controller.RootComponent.AMBARI_SERVER.name();
        org.apache.ambari.server.state.Alert alert = new org.apache.ambari.server.state.Alert(definitionName, null, serviceName, componentName, org.apache.ambari.server.state.alerts.AlertReceivedListenerTest.HOST1, org.apache.ambari.server.state.AlertState.OK);
        alert.setClusterId(m_cluster.getClusterId());
        alert.setLabel(org.apache.ambari.server.state.alerts.AlertReceivedListenerTest.ALERT_LABEL);
        alert.setText(((serviceName + " ") + componentName) + " is OK");
        alert.setTimestamp(1L);
        org.apache.ambari.server.events.listeners.alerts.AlertReceivedListener listener = m_injector.getInstance(org.apache.ambari.server.events.listeners.alerts.AlertReceivedListener.class);
        org.apache.ambari.server.events.AlertReceivedEvent event = new org.apache.ambari.server.events.AlertReceivedEvent(m_cluster.getClusterId(), alert);
        listener.onAlertEvent(event);
        java.util.List<org.apache.ambari.server.orm.entities.AlertCurrentEntity> allCurrent = m_dao.findCurrent();
        org.junit.Assert.assertEquals(1, allCurrent.size());
        alert.setHostName("INVALID");
        alert.setClusterId(null);
        m_dao.removeCurrentByHost(org.apache.ambari.server.state.alerts.AlertReceivedListenerTest.HOST1);
        allCurrent = m_dao.findCurrent();
        org.junit.Assert.assertEquals(0, allCurrent.size());
        listener.onAlertEvent(event);
        allCurrent = m_dao.findCurrent();
        org.junit.Assert.assertEquals(1, allCurrent.size());
    }

    @org.junit.Test
    public void testMissingClusterAndInvalidHost() throws org.apache.ambari.server.AmbariException {
        java.lang.String definitionName = org.apache.ambari.server.state.alerts.AlertReceivedListenerTest.ALERT_DEFINITION + "1";
        java.lang.String serviceName = org.apache.ambari.server.controller.RootService.AMBARI.name();
        java.lang.String componentName = org.apache.ambari.server.controller.RootComponent.AMBARI_AGENT.name();
        org.apache.ambari.server.state.Alert alert1 = new org.apache.ambari.server.state.Alert(definitionName, null, serviceName, componentName, org.apache.ambari.server.state.alerts.AlertReceivedListenerTest.HOST1, org.apache.ambari.server.state.AlertState.OK);
        alert1.setClusterId(m_cluster.getClusterId());
        alert1.setLabel(org.apache.ambari.server.state.alerts.AlertReceivedListenerTest.ALERT_LABEL);
        alert1.setText(((serviceName + " ") + componentName) + " is OK");
        alert1.setTimestamp(1L);
        org.apache.ambari.server.events.listeners.alerts.AlertReceivedListener listener = m_injector.getInstance(org.apache.ambari.server.events.listeners.alerts.AlertReceivedListener.class);
        org.apache.ambari.server.events.AlertReceivedEvent event = new org.apache.ambari.server.events.AlertReceivedEvent(m_cluster.getClusterId(), alert1);
        listener.onAlertEvent(event);
        java.util.List<org.apache.ambari.server.orm.entities.AlertCurrentEntity> allCurrent = m_dao.findCurrent();
        org.junit.Assert.assertEquals(1, allCurrent.size());
        alert1.setClusterId(null);
        alert1.setHostName("INVALID");
        m_dao.removeCurrentByHost(org.apache.ambari.server.state.alerts.AlertReceivedListenerTest.HOST1);
        allCurrent = m_dao.findCurrent();
        org.junit.Assert.assertEquals(0, allCurrent.size());
        listener.onAlertEvent(event);
        allCurrent = m_dao.findCurrent();
        org.junit.Assert.assertEquals(0, allCurrent.size());
    }

    @org.junit.Test
    public void testSkippedAlertWithNoCurrentAlert() throws org.apache.ambari.server.AmbariException {
        java.lang.String definitionName = org.apache.ambari.server.state.alerts.AlertReceivedListenerTest.ALERT_DEFINITION + "1";
        java.lang.String serviceName = "HDFS";
        java.lang.String componentName = "NAMENODE";
        org.apache.ambari.server.state.Alert alert = new org.apache.ambari.server.state.Alert(definitionName, null, serviceName, componentName, org.apache.ambari.server.state.alerts.AlertReceivedListenerTest.HOST1, org.apache.ambari.server.state.AlertState.SKIPPED);
        alert.setClusterId(m_cluster.getClusterId());
        alert.setLabel(org.apache.ambari.server.state.alerts.AlertReceivedListenerTest.ALERT_LABEL);
        alert.setText(((serviceName + " ") + componentName) + " is OK");
        alert.setTimestamp(1L);
        org.apache.ambari.server.events.listeners.alerts.AlertReceivedListener listener = m_injector.getInstance(org.apache.ambari.server.events.listeners.alerts.AlertReceivedListener.class);
        org.apache.ambari.server.events.AlertReceivedEvent event = new org.apache.ambari.server.events.AlertReceivedEvent(m_cluster.getClusterId(), alert);
        listener.onAlertEvent(event);
        java.util.List<org.apache.ambari.server.orm.entities.AlertCurrentEntity> allCurrent = m_dao.findCurrent();
        org.junit.Assert.assertEquals(0, allCurrent.size());
    }

    @org.junit.Test
    public void testSkippedAlertUpdatesTimestampAndText() throws org.apache.ambari.server.AmbariException {
        java.lang.String definitionName = org.apache.ambari.server.state.alerts.AlertReceivedListenerTest.ALERT_DEFINITION + "1";
        java.lang.String serviceName = "HDFS";
        java.lang.String componentName = "NAMENODE";
        java.lang.String text = ((serviceName + " ") + componentName) + " is OK";
        org.apache.ambari.server.state.Alert alert = new org.apache.ambari.server.state.Alert(definitionName, null, serviceName, componentName, org.apache.ambari.server.state.alerts.AlertReceivedListenerTest.HOST1, org.apache.ambari.server.state.AlertState.OK);
        alert.setClusterId(m_cluster.getClusterId());
        alert.setLabel(org.apache.ambari.server.state.alerts.AlertReceivedListenerTest.ALERT_LABEL);
        alert.setText(text);
        alert.setTimestamp(1L);
        org.apache.ambari.server.events.listeners.alerts.AlertReceivedListener listener = m_injector.getInstance(org.apache.ambari.server.events.listeners.alerts.AlertReceivedListener.class);
        org.apache.ambari.server.events.AlertReceivedEvent event = new org.apache.ambari.server.events.AlertReceivedEvent(m_cluster.getClusterId(), alert);
        listener.onAlertEvent(event);
        java.util.List<org.apache.ambari.server.orm.entities.AlertCurrentEntity> allCurrent = m_dao.findCurrent();
        org.junit.Assert.assertEquals(1, allCurrent.size());
        org.junit.Assert.assertEquals(1L, ((long) (allCurrent.get(0).getOriginalTimestamp())));
        org.junit.Assert.assertEquals(1L, ((long) (allCurrent.get(0).getLatestTimestamp())));
        alert.setState(org.apache.ambari.server.state.AlertState.SKIPPED);
        alert.setTimestamp(2L);
        text = text + " Updated";
        alert.setText(text);
        listener.onAlertEvent(event);
        allCurrent = m_dao.findCurrent();
        org.junit.Assert.assertEquals(1L, ((long) (allCurrent.get(0).getOriginalTimestamp())));
        org.junit.Assert.assertEquals(2L, ((long) (allCurrent.get(0).getLatestTimestamp())));
        org.junit.Assert.assertEquals(text, allCurrent.get(0).getLatestText());
        alert.setText("");
        alert.setTimestamp(3L);
        listener.onAlertEvent(event);
        allCurrent = m_dao.findCurrent();
        org.junit.Assert.assertEquals(1L, ((long) (allCurrent.get(0).getOriginalTimestamp())));
        org.junit.Assert.assertEquals(3L, ((long) (allCurrent.get(0).getLatestTimestamp())));
        org.junit.Assert.assertEquals(text, allCurrent.get(0).getLatestText());
    }

    @org.junit.Test
    public void testAlertOccurrences() throws org.apache.ambari.server.AmbariException {
        java.lang.String definitionName = org.apache.ambari.server.state.alerts.AlertReceivedListenerTest.ALERT_DEFINITION + "1";
        java.lang.String serviceName = "HDFS";
        java.lang.String componentName = "NAMENODE";
        java.lang.String text = ((serviceName + " ") + componentName) + " is OK";
        org.apache.ambari.server.state.Alert alert = new org.apache.ambari.server.state.Alert(definitionName, null, serviceName, componentName, org.apache.ambari.server.state.alerts.AlertReceivedListenerTest.HOST1, org.apache.ambari.server.state.AlertState.OK);
        alert.setClusterId(m_cluster.getClusterId());
        alert.setLabel(org.apache.ambari.server.state.alerts.AlertReceivedListenerTest.ALERT_LABEL);
        alert.setText(text);
        alert.setTimestamp(1L);
        org.apache.ambari.server.events.listeners.alerts.AlertReceivedListener listener = m_injector.getInstance(org.apache.ambari.server.events.listeners.alerts.AlertReceivedListener.class);
        org.apache.ambari.server.events.AlertReceivedEvent event = new org.apache.ambari.server.events.AlertReceivedEvent(m_cluster.getClusterId(), alert);
        listener.onAlertEvent(event);
        java.util.List<org.apache.ambari.server.orm.entities.AlertCurrentEntity> allCurrent = m_dao.findCurrent();
        org.junit.Assert.assertEquals(1, allCurrent.size());
        org.junit.Assert.assertEquals(1, ((long) (allCurrent.get(0).getOccurrences())));
        listener.onAlertEvent(event);
        allCurrent = m_dao.findCurrent();
        org.junit.Assert.assertEquals(2, ((long) (allCurrent.get(0).getOccurrences())));
        alert.setState(org.apache.ambari.server.state.AlertState.WARNING);
        listener.onAlertEvent(event);
        allCurrent = m_dao.findCurrent();
        org.junit.Assert.assertEquals(1, ((long) (allCurrent.get(0).getOccurrences())));
        listener.onAlertEvent(event);
        allCurrent = m_dao.findCurrent();
        org.junit.Assert.assertEquals(2, ((long) (allCurrent.get(0).getOccurrences())));
        alert.setState(org.apache.ambari.server.state.AlertState.CRITICAL);
        listener.onAlertEvent(event);
        allCurrent = m_dao.findCurrent();
        org.junit.Assert.assertEquals(3, ((long) (allCurrent.get(0).getOccurrences())));
    }

    @org.junit.Test
    public void testAlertFirmness() throws java.lang.Exception {
        java.lang.String definitionName = org.apache.ambari.server.state.alerts.AlertReceivedListenerTest.ALERT_DEFINITION + "1";
        java.lang.String serviceName = "HDFS";
        java.lang.String componentName = "NAMENODE";
        java.lang.String text = ((serviceName + " ") + componentName) + " is OK";
        org.apache.ambari.server.state.Alert alert = new org.apache.ambari.server.state.Alert(definitionName, null, serviceName, componentName, org.apache.ambari.server.state.alerts.AlertReceivedListenerTest.HOST1, org.apache.ambari.server.state.AlertState.CRITICAL);
        alert.setClusterId(m_cluster.getClusterId());
        alert.setLabel(org.apache.ambari.server.state.alerts.AlertReceivedListenerTest.ALERT_LABEL);
        alert.setText(text);
        alert.setTimestamp(1L);
        org.apache.ambari.server.events.listeners.alerts.AlertReceivedListener listener = m_injector.getInstance(org.apache.ambari.server.events.listeners.alerts.AlertReceivedListener.class);
        org.apache.ambari.server.events.AlertReceivedEvent event = new org.apache.ambari.server.events.AlertReceivedEvent(m_cluster.getClusterId(), alert);
        listener.onAlertEvent(event);
        java.util.List<org.apache.ambari.server.orm.entities.AlertCurrentEntity> allCurrent = m_dao.findCurrent();
        org.junit.Assert.assertEquals(1, allCurrent.size());
        org.junit.Assert.assertEquals(1, ((long) (allCurrent.get(0).getOccurrences())));
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.AlertFirmness.HARD, allCurrent.get(0).getFirmness());
        org.apache.ambari.server.orm.entities.AlertDefinitionEntity definition = allCurrent.get(0).getAlertHistory().getAlertDefinition();
        definition.setRepeatTolerance(2);
        definition.setRepeatToleranceEnabled(true);
        m_definitionDao.merge(definition);
        alert.setState(org.apache.ambari.server.state.AlertState.OK);
        listener.onAlertEvent(event);
        allCurrent = m_dao.findCurrent();
        org.junit.Assert.assertEquals(1, ((long) (allCurrent.get(0).getOccurrences())));
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.AlertFirmness.HARD, allCurrent.get(0).getFirmness());
        alert.setState(org.apache.ambari.server.state.AlertState.CRITICAL);
        listener.onAlertEvent(event);
        allCurrent = m_dao.findCurrent();
        org.junit.Assert.assertEquals(1, ((long) (allCurrent.get(0).getOccurrences())));
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.AlertFirmness.SOFT, allCurrent.get(0).getFirmness());
        listener.onAlertEvent(event);
        allCurrent = m_dao.findCurrent();
        org.junit.Assert.assertEquals(2, ((long) (allCurrent.get(0).getOccurrences())));
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.AlertFirmness.HARD, allCurrent.get(0).getFirmness());
    }

    @org.junit.Test
    public void testAlertFirmnessWithinNonOKStates() throws java.lang.Exception {
        java.lang.String definitionName = org.apache.ambari.server.state.alerts.AlertReceivedListenerTest.ALERT_DEFINITION + "1";
        java.lang.String serviceName = "HDFS";
        java.lang.String componentName = "NAMENODE";
        java.lang.String text = ((serviceName + " ") + componentName) + " is OK";
        org.apache.ambari.server.state.Alert alert = new org.apache.ambari.server.state.Alert(definitionName, null, serviceName, componentName, org.apache.ambari.server.state.alerts.AlertReceivedListenerTest.HOST1, org.apache.ambari.server.state.AlertState.OK);
        alert.setClusterId(m_cluster.getClusterId());
        alert.setLabel(org.apache.ambari.server.state.alerts.AlertReceivedListenerTest.ALERT_LABEL);
        alert.setText(text);
        alert.setTimestamp(1L);
        org.apache.ambari.server.events.listeners.alerts.AlertReceivedListener listener = m_injector.getInstance(org.apache.ambari.server.events.listeners.alerts.AlertReceivedListener.class);
        org.apache.ambari.server.events.AlertReceivedEvent event = new org.apache.ambari.server.events.AlertReceivedEvent(m_cluster.getClusterId(), alert);
        listener.onAlertEvent(event);
        java.util.List<org.apache.ambari.server.orm.entities.AlertCurrentEntity> allCurrent = m_dao.findCurrent();
        org.junit.Assert.assertEquals(1, allCurrent.size());
        org.junit.Assert.assertEquals(1, ((long) (allCurrent.get(0).getOccurrences())));
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.AlertFirmness.HARD, allCurrent.get(0).getFirmness());
        org.apache.ambari.server.orm.entities.AlertDefinitionEntity definition = allCurrent.get(0).getAlertHistory().getAlertDefinition();
        definition.setRepeatTolerance(4);
        definition.setRepeatToleranceEnabled(true);
        m_definitionDao.merge(definition);
        alert.setState(org.apache.ambari.server.state.AlertState.WARNING);
        listener.onAlertEvent(event);
        allCurrent = m_dao.findCurrent();
        org.junit.Assert.assertEquals(1, ((long) (allCurrent.get(0).getOccurrences())));
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.AlertFirmness.SOFT, allCurrent.get(0).getFirmness());
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.AlertState.WARNING, allCurrent.get(0).getAlertHistory().getAlertState());
        alert.setState(org.apache.ambari.server.state.AlertState.CRITICAL);
        listener.onAlertEvent(event);
        allCurrent = m_dao.findCurrent();
        org.junit.Assert.assertEquals(2, ((long) (allCurrent.get(0).getOccurrences())));
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.AlertFirmness.SOFT, allCurrent.get(0).getFirmness());
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.AlertState.CRITICAL, allCurrent.get(0).getAlertHistory().getAlertState());
        alert.setState(org.apache.ambari.server.state.AlertState.WARNING);
        listener.onAlertEvent(event);
        allCurrent = m_dao.findCurrent();
        org.junit.Assert.assertEquals(3, ((long) (allCurrent.get(0).getOccurrences())));
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.AlertFirmness.SOFT, allCurrent.get(0).getFirmness());
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.AlertState.WARNING, allCurrent.get(0).getAlertHistory().getAlertState());
        alert.setState(org.apache.ambari.server.state.AlertState.CRITICAL);
        listener.onAlertEvent(event);
        allCurrent = m_dao.findCurrent();
        org.junit.Assert.assertEquals(4, ((long) (allCurrent.get(0).getOccurrences())));
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.AlertFirmness.HARD, allCurrent.get(0).getFirmness());
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.AlertState.CRITICAL, allCurrent.get(0).getAlertHistory().getAlertState());
    }

    @org.junit.Test
    public void testAggregateAlertFirmness() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.AlertDefinitionEntity definition = new org.apache.ambari.server.orm.entities.AlertDefinitionEntity();
        definition.setDefinitionName("aggregate-alert-firmness-test");
        definition.setServiceName("HDFS");
        definition.setComponentName("NAMENODE");
        definition.setClusterId(m_cluster.getClusterId());
        definition.setHash(java.util.UUID.randomUUID().toString());
        definition.setScheduleInterval(java.lang.Integer.valueOf(60));
        definition.setScope(org.apache.ambari.server.state.alert.Scope.SERVICE);
        definition.setSource("{\"type\" : \"AGGREGATE\"}");
        definition.setSourceType(org.apache.ambari.server.state.alert.SourceType.AGGREGATE);
        definition.setRepeatTolerance(100);
        definition.setRepeatToleranceEnabled(true);
        m_definitionDao.create(definition);
        org.apache.ambari.server.state.Alert alert = new org.apache.ambari.server.state.Alert(definition.getDefinitionName(), null, definition.getServiceName(), definition.getComponentName(), org.apache.ambari.server.state.alerts.AlertReceivedListenerTest.HOST1, org.apache.ambari.server.state.AlertState.OK);
        alert.setClusterId(m_cluster.getClusterId());
        alert.setLabel(org.apache.ambari.server.state.alerts.AlertReceivedListenerTest.ALERT_LABEL);
        alert.setText("Aggregate alerts are always HARD");
        alert.setTimestamp(1L);
        org.apache.ambari.server.events.listeners.alerts.AlertReceivedListener listener = m_injector.getInstance(org.apache.ambari.server.events.listeners.alerts.AlertReceivedListener.class);
        org.apache.ambari.server.events.AlertReceivedEvent event = new org.apache.ambari.server.events.AlertReceivedEvent(m_cluster.getClusterId(), alert);
        listener.onAlertEvent(event);
        java.util.List<org.apache.ambari.server.orm.entities.AlertCurrentEntity> allCurrent = m_dao.findCurrent();
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.AlertFirmness.HARD, allCurrent.get(0).getFirmness());
        alert.setState(org.apache.ambari.server.state.AlertState.CRITICAL);
        listener.onAlertEvent(event);
        allCurrent = m_dao.findCurrent();
        org.junit.Assert.assertEquals(1, ((long) (allCurrent.get(0).getOccurrences())));
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.AlertFirmness.HARD, allCurrent.get(0).getFirmness());
        alert.setState(org.apache.ambari.server.state.AlertState.WARNING);
        listener.onAlertEvent(event);
        allCurrent = m_dao.findCurrent();
        org.junit.Assert.assertEquals(2, ((long) (allCurrent.get(0).getOccurrences())));
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.AlertFirmness.HARD, allCurrent.get(0).getFirmness());
        alert.setState(org.apache.ambari.server.state.AlertState.OK);
        listener.onAlertEvent(event);
        allCurrent = m_dao.findCurrent();
        org.junit.Assert.assertEquals(1, ((long) (allCurrent.get(0).getOccurrences())));
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.AlertFirmness.HARD, allCurrent.get(0).getFirmness());
    }

    @org.junit.Test
    public void testAlertFirmnessUsingGlobalValue() throws java.lang.Exception {
        java.lang.String definitionName = org.apache.ambari.server.state.alerts.AlertReceivedListenerTest.ALERT_DEFINITION + "1";
        java.lang.String serviceName = "HDFS";
        java.lang.String componentName = "NAMENODE";
        java.lang.String text = ((serviceName + " ") + componentName) + " is OK";
        org.apache.ambari.server.state.Alert alert = new org.apache.ambari.server.state.Alert(definitionName, null, serviceName, componentName, org.apache.ambari.server.state.alerts.AlertReceivedListenerTest.HOST1, org.apache.ambari.server.state.AlertState.OK);
        alert.setClusterId(m_cluster.getClusterId());
        alert.setLabel(org.apache.ambari.server.state.alerts.AlertReceivedListenerTest.ALERT_LABEL);
        alert.setText(text);
        alert.setTimestamp(1L);
        org.apache.ambari.server.events.listeners.alerts.AlertReceivedListener listener = m_injector.getInstance(org.apache.ambari.server.events.listeners.alerts.AlertReceivedListener.class);
        org.apache.ambari.server.events.AlertReceivedEvent event = new org.apache.ambari.server.events.AlertReceivedEvent(m_cluster.getClusterId(), alert);
        listener.onAlertEvent(event);
        java.util.List<org.apache.ambari.server.orm.entities.AlertCurrentEntity> allCurrent = m_dao.findCurrent();
        org.junit.Assert.assertEquals(1, allCurrent.size());
        org.junit.Assert.assertEquals(1, ((long) (allCurrent.get(0).getOccurrences())));
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.AlertFirmness.HARD, allCurrent.get(0).getFirmness());
        org.apache.ambari.server.orm.entities.AlertDefinitionEntity definition = allCurrent.get(0).getAlertHistory().getAlertDefinition();
        definition.setRepeatTolerance(2);
        definition.setRepeatToleranceEnabled(false);
        m_definitionDao.merge(definition);
        alert.setState(org.apache.ambari.server.state.AlertState.CRITICAL);
        listener.onAlertEvent(event);
        allCurrent = m_dao.findCurrent();
        org.junit.Assert.assertEquals(1, ((long) (allCurrent.get(0).getOccurrences())));
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.AlertFirmness.HARD, allCurrent.get(0).getFirmness());
    }

    @org.junit.Test
    @java.lang.SuppressWarnings("serial")
    public void testAlertFirmnessUsingGlobalValueHigherThanOverride() throws java.lang.Exception {
        org.apache.ambari.server.state.ConfigFactory cf = m_injector.getInstance(org.apache.ambari.server.state.ConfigFactory.class);
        org.apache.ambari.server.state.Config config = cf.createNew(m_cluster, org.apache.ambari.server.state.ConfigHelper.CLUSTER_ENV, "version2", new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put(org.apache.ambari.server.state.ConfigHelper.CLUSTER_ENV_ALERT_REPEAT_TOLERANCE, "3");
            }
        }, new java.util.HashMap<>());
        m_cluster.addDesiredConfig("user", java.util.Collections.singleton(config));
        java.lang.String definitionName = org.apache.ambari.server.state.alerts.AlertReceivedListenerTest.ALERT_DEFINITION + "1";
        java.lang.String serviceName = "HDFS";
        java.lang.String componentName = "NAMENODE";
        java.lang.String text = ((serviceName + " ") + componentName) + " is OK";
        org.apache.ambari.server.state.Alert alert = new org.apache.ambari.server.state.Alert(definitionName, null, serviceName, componentName, org.apache.ambari.server.state.alerts.AlertReceivedListenerTest.HOST1, org.apache.ambari.server.state.AlertState.OK);
        alert.setClusterId(m_cluster.getClusterId());
        alert.setLabel(org.apache.ambari.server.state.alerts.AlertReceivedListenerTest.ALERT_LABEL);
        alert.setText(text);
        alert.setTimestamp(1L);
        org.apache.ambari.server.events.listeners.alerts.AlertReceivedListener listener = m_injector.getInstance(org.apache.ambari.server.events.listeners.alerts.AlertReceivedListener.class);
        org.apache.ambari.server.events.AlertReceivedEvent event = new org.apache.ambari.server.events.AlertReceivedEvent(m_cluster.getClusterId(), alert);
        listener.onAlertEvent(event);
        java.util.List<org.apache.ambari.server.orm.entities.AlertCurrentEntity> allCurrent = m_dao.findCurrent();
        org.junit.Assert.assertEquals(1, allCurrent.size());
        org.junit.Assert.assertEquals(1, ((long) (allCurrent.get(0).getOccurrences())));
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.AlertFirmness.HARD, allCurrent.get(0).getFirmness());
        alert.setState(org.apache.ambari.server.state.AlertState.CRITICAL);
        listener.onAlertEvent(event);
        allCurrent = m_dao.findCurrent();
        org.junit.Assert.assertEquals(1, ((long) (allCurrent.get(0).getOccurrences())));
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.AlertFirmness.SOFT, allCurrent.get(0).getFirmness());
        listener.onAlertEvent(event);
        allCurrent = m_dao.findCurrent();
        org.junit.Assert.assertEquals(2, ((long) (allCurrent.get(0).getOccurrences())));
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.AlertFirmness.SOFT, allCurrent.get(0).getFirmness());
        listener.onAlertEvent(event);
        allCurrent = m_dao.findCurrent();
        org.junit.Assert.assertEquals(3, ((long) (allCurrent.get(0).getOccurrences())));
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.AlertFirmness.HARD, allCurrent.get(0).getFirmness());
    }

    @org.junit.Test
    public void testMultipleNewAlertEvents() throws java.lang.Exception {
        org.junit.Assert.assertEquals(0, m_dao.findCurrent().size());
        final java.lang.String definitionName = org.apache.ambari.server.state.alerts.AlertReceivedListenerTest.ALERT_DEFINITION + "1";
        java.util.List<java.lang.Thread> threads = new java.util.ArrayList<>();
        final org.apache.ambari.server.events.listeners.alerts.AlertReceivedListener listener = m_injector.getInstance(org.apache.ambari.server.events.listeners.alerts.AlertReceivedListener.class);
        for (int i = 0; i < 10; i++) {
            java.lang.Thread thread = new java.lang.Thread() {
                @java.lang.Override
                public void run() {
                    org.apache.ambari.server.state.Alert alert = new org.apache.ambari.server.state.Alert(definitionName, null, "HDFS", null, org.apache.ambari.server.state.alerts.AlertReceivedListenerTest.HOST1, org.apache.ambari.server.state.AlertState.OK);
                    alert.setClusterId(m_cluster.getClusterId());
                    alert.setLabel(org.apache.ambari.server.state.alerts.AlertReceivedListenerTest.ALERT_LABEL);
                    alert.setText("HDFS is OK ");
                    alert.setTimestamp(java.lang.System.currentTimeMillis());
                    final org.apache.ambari.server.events.AlertReceivedEvent event = new org.apache.ambari.server.events.AlertReceivedEvent(m_cluster.getClusterId(), alert);
                    try {
                        listener.onAlertEvent(event);
                    } catch (org.apache.ambari.server.AmbariException e) {
                        e.printStackTrace();
                    }
                }
            };
            threads.add(thread);
            thread.start();
        }
        for (java.lang.Thread thread : threads) {
            thread.join();
        }
        org.junit.Assert.assertEquals(1, m_dao.findCurrent().size());
    }
}