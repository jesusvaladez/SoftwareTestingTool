package org.apache.ambari.server.events.listeners.upgrade;
import javax.persistence.EntityManager;
import org.easymock.Capture;
import org.easymock.EasyMock;
import static org.easymock.EasyMock.createNiceMock;
public class AlertMaintenanceModeListenerTest {
    private static final java.lang.String HOSTNAME = "c6401.ambari.apache.org";

    private static final java.lang.String SERVICE = "HDFS";

    private static final java.lang.String COMPONENT = "NAMENODE";

    private static final java.lang.Long CLUSTER_ID = 1L;

    private static final java.lang.String DEFINITION_NAME = "definition_name";

    @com.google.inject.Inject
    private org.apache.ambari.server.events.publishers.AmbariEventPublisher m_eventPublisher;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.AlertsDAO m_alertsDAO;

    private com.google.inject.Injector injector;

    @org.junit.Before
    public void setup() throws java.lang.Exception {
        injector = com.google.inject.Guice.createInjector(com.google.inject.util.Modules.override(new org.apache.ambari.server.orm.InMemoryDefaultTestModule()).with(new org.apache.ambari.server.events.listeners.upgrade.AlertMaintenanceModeListenerTest.MockModule()));
        org.apache.ambari.server.utils.EventBusSynchronizer.synchronizeAmbariEventPublisher(injector);
        injector.injectMembers(this);
    }

    @org.junit.Test
    public void testHostMaintenanceMode() throws java.lang.Exception {
        java.util.List<org.apache.ambari.server.orm.entities.AlertCurrentEntity> alerts = getMockAlerts("HOST");
        org.apache.ambari.server.orm.entities.AlertCurrentEntity hostAlert = alerts.get(0);
        org.apache.ambari.server.orm.entities.AlertCurrentEntity serviceAlert = alerts.get(1);
        org.apache.ambari.server.orm.entities.AlertCurrentEntity componentAlert = alerts.get(2);
        org.easymock.EasyMock.expect(hostAlert.getMaintenanceState()).andReturn(org.apache.ambari.server.state.MaintenanceState.OFF).atLeastOnce();
        hostAlert.setMaintenanceState(org.apache.ambari.server.state.MaintenanceState.ON);
        org.easymock.EasyMock.expectLastCall().once();
        org.easymock.EasyMock.expect(m_alertsDAO.merge(hostAlert)).andReturn(hostAlert).once();
        org.apache.ambari.server.state.Host host = org.easymock.EasyMock.createNiceMock(org.apache.ambari.server.state.Host.class);
        org.easymock.EasyMock.expect(host.getHostName()).andReturn(org.apache.ambari.server.events.listeners.upgrade.AlertMaintenanceModeListenerTest.HOSTNAME).atLeastOnce();
        org.apache.ambari.server.events.publishers.STOMPUpdatePublisher stompUpdatePublisher = injector.getInstance(org.apache.ambari.server.events.publishers.STOMPUpdatePublisher.class);
        org.easymock.Capture<org.apache.ambari.server.events.AlertUpdateEvent> alertUpdateEventCapture = org.easymock.EasyMock.newCapture();
        stompUpdatePublisher.publish(org.easymock.EasyMock.capture(alertUpdateEventCapture));
        org.easymock.EasyMock.replay(hostAlert, serviceAlert, componentAlert, host, m_alertsDAO, stompUpdatePublisher);
        org.apache.ambari.server.events.MaintenanceModeEvent hostEvent = new org.apache.ambari.server.events.MaintenanceModeEvent(org.apache.ambari.server.state.MaintenanceState.ON, 1, host);
        m_eventPublisher.publish(hostEvent);
        org.easymock.EasyMock.verify(hostAlert, serviceAlert, componentAlert, host, m_alertsDAO);
        org.apache.ambari.server.events.AlertUpdateEvent alertUpdateEvent = alertUpdateEventCapture.getValue();
        org.junit.Assert.assertNotNull(alertUpdateEvent);
        org.junit.Assert.assertEquals(1, alertUpdateEvent.getSummaries().size());
        org.junit.Assert.assertTrue(alertUpdateEvent.getSummaries().containsKey(org.apache.ambari.server.events.listeners.upgrade.AlertMaintenanceModeListenerTest.CLUSTER_ID));
        org.junit.Assert.assertEquals(1, alertUpdateEvent.getSummaries().get(org.apache.ambari.server.events.listeners.upgrade.AlertMaintenanceModeListenerTest.CLUSTER_ID).size());
        org.junit.Assert.assertTrue(alertUpdateEvent.getSummaries().get(org.apache.ambari.server.events.listeners.upgrade.AlertMaintenanceModeListenerTest.CLUSTER_ID).containsKey(org.apache.ambari.server.events.listeners.upgrade.AlertMaintenanceModeListenerTest.DEFINITION_NAME));
        org.junit.Assert.assertEquals(1, alertUpdateEvent.getSummaries().get(org.apache.ambari.server.events.listeners.upgrade.AlertMaintenanceModeListenerTest.CLUSTER_ID).get(org.apache.ambari.server.events.listeners.upgrade.AlertMaintenanceModeListenerTest.DEFINITION_NAME).State.Ok.MaintenanceCount);
    }

    @org.junit.Test
    public void testServiceMaintenanceMode() throws java.lang.Exception {
        java.util.List<org.apache.ambari.server.orm.entities.AlertCurrentEntity> alerts = getMockAlerts("SERVICE");
        org.apache.ambari.server.orm.entities.AlertCurrentEntity hostAlert = alerts.get(0);
        org.apache.ambari.server.orm.entities.AlertCurrentEntity serviceAlert = alerts.get(1);
        org.apache.ambari.server.orm.entities.AlertCurrentEntity componentAlert = alerts.get(2);
        org.easymock.EasyMock.expect(serviceAlert.getMaintenanceState()).andReturn(org.apache.ambari.server.state.MaintenanceState.OFF).atLeastOnce();
        serviceAlert.setMaintenanceState(org.apache.ambari.server.state.MaintenanceState.ON);
        org.easymock.EasyMock.expectLastCall().once();
        org.easymock.EasyMock.expect(m_alertsDAO.merge(serviceAlert)).andReturn(serviceAlert).once();
        org.apache.ambari.server.state.Service service = org.easymock.EasyMock.createNiceMock(org.apache.ambari.server.state.Service.class);
        org.easymock.EasyMock.expect(service.getName()).andReturn(org.apache.ambari.server.events.listeners.upgrade.AlertMaintenanceModeListenerTest.SERVICE).atLeastOnce();
        org.apache.ambari.server.events.publishers.STOMPUpdatePublisher stompUpdatePublisher = injector.getInstance(org.apache.ambari.server.events.publishers.STOMPUpdatePublisher.class);
        org.easymock.Capture<org.apache.ambari.server.events.AlertUpdateEvent> alertUpdateEventCapture = org.easymock.EasyMock.newCapture();
        stompUpdatePublisher.publish(org.easymock.EasyMock.capture(alertUpdateEventCapture));
        org.easymock.EasyMock.replay(hostAlert, serviceAlert, componentAlert, service, m_alertsDAO, stompUpdatePublisher);
        org.apache.ambari.server.events.MaintenanceModeEvent serviceEvent = new org.apache.ambari.server.events.MaintenanceModeEvent(org.apache.ambari.server.state.MaintenanceState.ON, service);
        m_eventPublisher.publish(serviceEvent);
        org.easymock.EasyMock.verify(hostAlert, serviceAlert, componentAlert, service, m_alertsDAO);
        org.apache.ambari.server.events.AlertUpdateEvent alertUpdateEvent = alertUpdateEventCapture.getValue();
        org.junit.Assert.assertNotNull(alertUpdateEvent);
        org.junit.Assert.assertEquals(1, alertUpdateEvent.getSummaries().size());
        org.junit.Assert.assertTrue(alertUpdateEvent.getSummaries().containsKey(org.apache.ambari.server.events.listeners.upgrade.AlertMaintenanceModeListenerTest.CLUSTER_ID));
        org.junit.Assert.assertEquals(1, alertUpdateEvent.getSummaries().get(org.apache.ambari.server.events.listeners.upgrade.AlertMaintenanceModeListenerTest.CLUSTER_ID).size());
        org.junit.Assert.assertTrue(alertUpdateEvent.getSummaries().get(org.apache.ambari.server.events.listeners.upgrade.AlertMaintenanceModeListenerTest.CLUSTER_ID).containsKey(org.apache.ambari.server.events.listeners.upgrade.AlertMaintenanceModeListenerTest.DEFINITION_NAME));
        org.junit.Assert.assertEquals(1, alertUpdateEvent.getSummaries().get(org.apache.ambari.server.events.listeners.upgrade.AlertMaintenanceModeListenerTest.CLUSTER_ID).get(org.apache.ambari.server.events.listeners.upgrade.AlertMaintenanceModeListenerTest.DEFINITION_NAME).State.Ok.MaintenanceCount);
    }

    @org.junit.Test
    public void testComponentMaintenanceMode() throws java.lang.Exception {
        java.util.List<org.apache.ambari.server.orm.entities.AlertCurrentEntity> alerts = getMockAlerts("SCH");
        org.apache.ambari.server.orm.entities.AlertCurrentEntity hostAlert = alerts.get(0);
        org.apache.ambari.server.orm.entities.AlertCurrentEntity serviceAlert = alerts.get(1);
        org.apache.ambari.server.orm.entities.AlertCurrentEntity componentAlert = alerts.get(2);
        org.easymock.EasyMock.expect(componentAlert.getMaintenanceState()).andReturn(org.apache.ambari.server.state.MaintenanceState.OFF).atLeastOnce();
        componentAlert.setMaintenanceState(org.apache.ambari.server.state.MaintenanceState.ON);
        org.easymock.EasyMock.expectLastCall().once();
        org.easymock.EasyMock.expect(m_alertsDAO.merge(componentAlert)).andReturn(componentAlert).once();
        org.apache.ambari.server.state.ServiceComponentHost serviceComponentHost = org.easymock.EasyMock.createNiceMock(org.apache.ambari.server.state.ServiceComponentHost.class);
        org.easymock.EasyMock.expect(serviceComponentHost.getHostName()).andReturn(org.apache.ambari.server.events.listeners.upgrade.AlertMaintenanceModeListenerTest.HOSTNAME).atLeastOnce();
        org.easymock.EasyMock.expect(serviceComponentHost.getServiceName()).andReturn(org.apache.ambari.server.events.listeners.upgrade.AlertMaintenanceModeListenerTest.SERVICE).atLeastOnce();
        org.easymock.EasyMock.expect(serviceComponentHost.getServiceComponentName()).andReturn(org.apache.ambari.server.events.listeners.upgrade.AlertMaintenanceModeListenerTest.COMPONENT).atLeastOnce();
        org.apache.ambari.server.events.publishers.STOMPUpdatePublisher stompUpdatePublisher = injector.getInstance(org.apache.ambari.server.events.publishers.STOMPUpdatePublisher.class);
        org.easymock.Capture<org.apache.ambari.server.events.AlertUpdateEvent> alertUpdateEventCapture = org.easymock.EasyMock.newCapture();
        stompUpdatePublisher.publish(org.easymock.EasyMock.capture(alertUpdateEventCapture));
        org.easymock.EasyMock.replay(hostAlert, serviceAlert, componentAlert, serviceComponentHost, m_alertsDAO, stompUpdatePublisher);
        org.apache.ambari.server.events.MaintenanceModeEvent serviceComponentHostEvent = new org.apache.ambari.server.events.MaintenanceModeEvent(org.apache.ambari.server.state.MaintenanceState.ON, serviceComponentHost);
        m_eventPublisher.publish(serviceComponentHostEvent);
        org.easymock.EasyMock.verify(hostAlert, serviceAlert, componentAlert, serviceComponentHost, m_alertsDAO);
        org.apache.ambari.server.events.AlertUpdateEvent alertUpdateEvent = alertUpdateEventCapture.getValue();
        org.junit.Assert.assertNotNull(alertUpdateEvent);
        org.junit.Assert.assertEquals(1, alertUpdateEvent.getSummaries().size());
        org.junit.Assert.assertTrue(alertUpdateEvent.getSummaries().containsKey(org.apache.ambari.server.events.listeners.upgrade.AlertMaintenanceModeListenerTest.CLUSTER_ID));
        org.junit.Assert.assertEquals(1, alertUpdateEvent.getSummaries().get(org.apache.ambari.server.events.listeners.upgrade.AlertMaintenanceModeListenerTest.CLUSTER_ID).size());
        org.junit.Assert.assertTrue(alertUpdateEvent.getSummaries().get(org.apache.ambari.server.events.listeners.upgrade.AlertMaintenanceModeListenerTest.CLUSTER_ID).containsKey(org.apache.ambari.server.events.listeners.upgrade.AlertMaintenanceModeListenerTest.DEFINITION_NAME));
        org.junit.Assert.assertEquals(1, alertUpdateEvent.getSummaries().get(org.apache.ambari.server.events.listeners.upgrade.AlertMaintenanceModeListenerTest.CLUSTER_ID).get(org.apache.ambari.server.events.listeners.upgrade.AlertMaintenanceModeListenerTest.DEFINITION_NAME).State.Ok.MaintenanceCount);
    }

    private java.util.List<org.apache.ambari.server.orm.entities.AlertCurrentEntity> getMockAlerts(java.lang.String testType) {
        org.apache.ambari.server.orm.entities.AlertCurrentEntity hostAlert = org.easymock.EasyMock.createStrictMock(org.apache.ambari.server.orm.entities.AlertCurrentEntity.class);
        org.apache.ambari.server.orm.entities.AlertCurrentEntity serviceAlert = org.easymock.EasyMock.createStrictMock(org.apache.ambari.server.orm.entities.AlertCurrentEntity.class);
        org.apache.ambari.server.orm.entities.AlertCurrentEntity componentAlert = org.easymock.EasyMock.createStrictMock(org.apache.ambari.server.orm.entities.AlertCurrentEntity.class);
        org.apache.ambari.server.orm.entities.AlertHistoryEntity hostHistory = org.easymock.EasyMock.createStrictMock(org.apache.ambari.server.orm.entities.AlertHistoryEntity.class);
        org.apache.ambari.server.orm.entities.AlertHistoryEntity serviceHistory = org.easymock.EasyMock.createStrictMock(org.apache.ambari.server.orm.entities.AlertHistoryEntity.class);
        org.apache.ambari.server.orm.entities.AlertHistoryEntity componentHistory = org.easymock.EasyMock.createStrictMock(org.apache.ambari.server.orm.entities.AlertHistoryEntity.class);
        org.apache.ambari.server.orm.entities.AlertDefinitionEntity alertDefinition = org.easymock.EasyMock.createStrictMock(org.apache.ambari.server.orm.entities.AlertDefinitionEntity.class);
        org.easymock.EasyMock.expect(hostAlert.getAlertHistory()).andReturn(hostHistory).atLeastOnce();
        org.easymock.EasyMock.expect(serviceAlert.getAlertHistory()).andReturn(serviceHistory).atLeastOnce();
        org.easymock.EasyMock.expect(componentAlert.getAlertHistory()).andReturn(componentHistory).atLeastOnce();
        org.easymock.EasyMock.expect(hostHistory.getHostName()).andReturn(org.apache.ambari.server.events.listeners.upgrade.AlertMaintenanceModeListenerTest.HOSTNAME).atLeastOnce();
        org.easymock.EasyMock.expect(hostHistory.getServiceName()).andReturn(null).atLeastOnce();
        org.easymock.EasyMock.expect(hostHistory.getComponentName()).andReturn(null).atLeastOnce();
        org.easymock.EasyMock.expect(hostHistory.getAlertDefinition()).andReturn(alertDefinition).atLeastOnce();
        org.easymock.EasyMock.expect(hostHistory.getAlertState()).andReturn(org.apache.ambari.server.state.AlertState.OK).atLeastOnce();
        org.easymock.EasyMock.expect(hostHistory.getClusterId()).andReturn(org.apache.ambari.server.events.listeners.upgrade.AlertMaintenanceModeListenerTest.CLUSTER_ID).atLeastOnce();
        org.easymock.EasyMock.expect(serviceHistory.getHostName()).andReturn(null).atLeastOnce();
        org.easymock.EasyMock.expect(serviceHistory.getServiceName()).andReturn(org.apache.ambari.server.events.listeners.upgrade.AlertMaintenanceModeListenerTest.SERVICE).atLeastOnce();
        org.easymock.EasyMock.expect(serviceHistory.getComponentName()).andReturn(null).atLeastOnce();
        org.easymock.EasyMock.expect(serviceHistory.getAlertDefinition()).andReturn(alertDefinition).atLeastOnce();
        org.easymock.EasyMock.expect(serviceHistory.getAlertState()).andReturn(org.apache.ambari.server.state.AlertState.OK).atLeastOnce();
        org.easymock.EasyMock.expect(serviceHistory.getClusterId()).andReturn(org.apache.ambari.server.events.listeners.upgrade.AlertMaintenanceModeListenerTest.CLUSTER_ID).atLeastOnce();
        org.easymock.EasyMock.expect(alertDefinition.getDefinitionId()).andReturn(1L).atLeastOnce();
        org.easymock.EasyMock.expect(alertDefinition.getDefinitionName()).andReturn(org.apache.ambari.server.events.listeners.upgrade.AlertMaintenanceModeListenerTest.DEFINITION_NAME).atLeastOnce();
        if (testType.equals("SCH")) {
            org.easymock.EasyMock.expect(componentHistory.getHostName()).andReturn(org.apache.ambari.server.events.listeners.upgrade.AlertMaintenanceModeListenerTest.HOSTNAME).atLeastOnce();
            org.easymock.EasyMock.expect(componentHistory.getServiceName()).andReturn(org.apache.ambari.server.events.listeners.upgrade.AlertMaintenanceModeListenerTest.SERVICE).atLeastOnce();
            org.easymock.EasyMock.expect(componentHistory.getComponentName()).andReturn(org.apache.ambari.server.events.listeners.upgrade.AlertMaintenanceModeListenerTest.COMPONENT).atLeastOnce();
        } else {
            org.easymock.EasyMock.expect(componentHistory.getHostName()).andReturn(null).atLeastOnce();
            org.easymock.EasyMock.expect(componentHistory.getServiceName()).andReturn(null).atLeastOnce();
            org.easymock.EasyMock.expect(componentHistory.getComponentName()).andReturn(org.apache.ambari.server.events.listeners.upgrade.AlertMaintenanceModeListenerTest.COMPONENT).atLeastOnce();
        }
        org.easymock.EasyMock.expect(componentHistory.getAlertDefinition()).andReturn(alertDefinition).atLeastOnce();
        org.easymock.EasyMock.expect(componentHistory.getAlertState()).andReturn(org.apache.ambari.server.state.AlertState.OK).atLeastOnce();
        org.easymock.EasyMock.expect(componentHistory.getClusterId()).andReturn(org.apache.ambari.server.events.listeners.upgrade.AlertMaintenanceModeListenerTest.CLUSTER_ID).atLeastOnce();
        java.util.List<org.apache.ambari.server.orm.entities.AlertCurrentEntity> currentAlerts = new java.util.ArrayList<>();
        currentAlerts.add(hostAlert);
        currentAlerts.add(serviceAlert);
        currentAlerts.add(componentAlert);
        org.easymock.EasyMock.expect(m_alertsDAO.findCurrent()).andReturn(currentAlerts).atLeastOnce();
        org.easymock.EasyMock.replay(hostHistory, serviceHistory, componentHistory, alertDefinition);
        return currentAlerts;
    }

    private class MockModule implements com.google.inject.Module {
        @java.lang.Override
        public void configure(com.google.inject.Binder binder) {
            org.apache.ambari.server.state.Cluster cluster = org.easymock.EasyMock.createNiceMock(org.apache.ambari.server.state.Cluster.class);
            binder.bind(org.apache.ambari.server.state.Clusters.class).toInstance(EasyMock.createNiceMock(org.apache.ambari.server.state.Clusters.class));
            binder.bind(org.apache.ambari.server.orm.DBAccessor.class).toInstance(EasyMock.createNiceMock(org.apache.ambari.server.orm.DBAccessor.class));
            binder.bind(org.apache.ambari.server.state.Cluster.class).toInstance(cluster);
            binder.bind(org.apache.ambari.server.orm.dao.AlertDefinitionDAO.class).toInstance(EasyMock.createNiceMock(org.apache.ambari.server.orm.dao.AlertDefinitionDAO.class));
            binder.bind(org.apache.ambari.server.orm.dao.AlertsDAO.class).toInstance(EasyMock.createNiceMock(org.apache.ambari.server.orm.dao.AlertsDAO.class));
            binder.bind(javax.persistence.EntityManager.class).toInstance(EasyMock.createNiceMock(javax.persistence.EntityManager.class));
            binder.bind(org.apache.ambari.server.events.publishers.STOMPUpdatePublisher.class).toInstance(EasyMock.createNiceMock(org.apache.ambari.server.events.publishers.STOMPUpdatePublisher.class));
        }
    }
}