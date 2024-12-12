package org.apache.ambari.server.alerts;
import org.easymock.EasyMock;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
@org.junit.runner.RunWith(org.powermock.modules.junit4.PowerMockRunner.class)
@org.powermock.core.classloader.annotations.PrepareForTest({ org.apache.ambari.server.controller.utilities.ClusterControllerHelper.class, org.apache.ambari.server.alerts.AmbariPerformanceRunnable.class, org.apache.ambari.server.alerts.AmbariPerformanceRunnable.PerformanceArea.class })
public class AmbariPerformanceRunnableTest {
    private static final long CLUSTER_ID = 1;

    private static final java.lang.String CLUSTER_NAME = "c1";

    private static final java.lang.String DEFINITION_NAME = "ambari_server_performance";

    private static final java.lang.String DEFINITION_SERVICE = "AMBARI";

    private static final java.lang.String DEFINITION_COMPONENT = "AMBARI_SERVER";

    private static final java.lang.String DEFINITION_LABEL = "Mock Definition";

    private static final int DEFINITION_INTERVAL = 1;

    private org.apache.ambari.server.state.Clusters m_clusters;

    private org.apache.ambari.server.state.Cluster m_cluster;

    private com.google.inject.Injector m_injector;

    private org.apache.ambari.server.orm.dao.AlertsDAO m_alertsDao;

    private org.apache.ambari.server.orm.dao.AlertDefinitionDAO m_definitionDao;

    private org.apache.ambari.server.orm.entities.AlertDefinitionEntity m_definition;

    private java.util.List<org.apache.ambari.server.orm.entities.AlertCurrentEntity> m_currentAlerts = new java.util.ArrayList<>();

    private org.apache.ambari.server.events.MockEventListener m_listener;

    private org.apache.ambari.server.events.publishers.AlertEventPublisher m_eventPublisher;

    private com.google.common.eventbus.EventBus m_synchronizedBus;

    @org.junit.Before
    public void setup() throws java.lang.Exception {
        m_injector = com.google.inject.Guice.createInjector(new org.apache.ambari.server.alerts.AmbariPerformanceRunnableTest.MockModule());
        m_alertsDao = m_injector.getInstance(org.apache.ambari.server.orm.dao.AlertsDAO.class);
        m_definitionDao = m_injector.getInstance(org.apache.ambari.server.orm.dao.AlertDefinitionDAO.class);
        m_clusters = m_injector.getInstance(org.apache.ambari.server.state.Clusters.class);
        m_cluster = m_injector.getInstance(org.apache.ambari.server.state.Cluster.class);
        m_eventPublisher = m_injector.getInstance(org.apache.ambari.server.events.publishers.AlertEventPublisher.class);
        m_listener = m_injector.getInstance(org.apache.ambari.server.events.MockEventListener.class);
        m_definition = org.easymock.EasyMock.createNiceMock(org.apache.ambari.server.orm.entities.AlertDefinitionEntity.class);
        m_synchronizedBus = new com.google.common.eventbus.EventBus();
        java.lang.reflect.Field field = org.apache.ambari.server.events.publishers.AlertEventPublisher.class.getDeclaredField("m_eventBus");
        field.setAccessible(true);
        field.set(m_eventPublisher, m_synchronizedBus);
        m_synchronizedBus.register(m_listener);
        java.util.Map<java.lang.String, org.apache.ambari.server.state.Cluster> clusterMap = new java.util.HashMap<>();
        clusterMap.put(org.apache.ambari.server.alerts.AmbariPerformanceRunnableTest.CLUSTER_NAME, m_cluster);
        EasyMock.expect(m_definition.getDefinitionName()).andReturn(org.apache.ambari.server.alerts.AmbariPerformanceRunnableTest.DEFINITION_NAME).atLeastOnce();
        EasyMock.expect(m_definition.getServiceName()).andReturn(org.apache.ambari.server.alerts.AmbariPerformanceRunnableTest.DEFINITION_SERVICE).atLeastOnce();
        EasyMock.expect(m_definition.getComponentName()).andReturn(org.apache.ambari.server.alerts.AmbariPerformanceRunnableTest.DEFINITION_COMPONENT).atLeastOnce();
        EasyMock.expect(m_definition.getLabel()).andReturn(org.apache.ambari.server.alerts.AmbariPerformanceRunnableTest.DEFINITION_LABEL).atLeastOnce();
        EasyMock.expect(m_definition.getEnabled()).andReturn(true).atLeastOnce();
        EasyMock.expect(m_definition.getScheduleInterval()).andReturn(org.apache.ambari.server.alerts.AmbariPerformanceRunnableTest.DEFINITION_INTERVAL).atLeastOnce();
        EasyMock.expect(m_cluster.getClusterId()).andReturn(org.apache.ambari.server.alerts.AmbariPerformanceRunnableTest.CLUSTER_ID).atLeastOnce();
        EasyMock.expect(m_clusters.getClusters()).andReturn(clusterMap).atLeastOnce();
        EasyMock.expect(m_definitionDao.findByName(org.apache.ambari.server.alerts.AmbariPerformanceRunnableTest.CLUSTER_ID, org.apache.ambari.server.alerts.AmbariPerformanceRunnableTest.DEFINITION_NAME)).andReturn(m_definition).atLeastOnce();
        EasyMock.expect(m_alertsDao.findCurrentByCluster(org.apache.ambari.server.alerts.AmbariPerformanceRunnableTest.CLUSTER_ID)).andReturn(m_currentAlerts).atLeastOnce();
        org.apache.ambari.server.state.alert.AlertDefinition definition = new org.apache.ambari.server.state.alert.AlertDefinition();
        definition.setDefinitionId(1L);
        definition.setName(org.apache.ambari.server.alerts.AmbariPerformanceRunnableTest.DEFINITION_NAME);
        org.apache.ambari.server.state.alert.ServerSource source = new org.apache.ambari.server.state.alert.ServerSource();
        definition.setSource(source);
        org.apache.ambari.server.state.alert.AlertDefinitionFactory factory = m_injector.getInstance(org.apache.ambari.server.state.alert.AlertDefinitionFactory.class);
        EasyMock.expect(factory.coerce(org.easymock.EasyMock.anyObject(org.apache.ambari.server.orm.entities.AlertDefinitionEntity.class))).andReturn(definition).atLeastOnce();
        org.apache.ambari.server.controller.spi.ClusterController clusterController = m_injector.getInstance(org.apache.ambari.server.controller.spi.ClusterController.class);
        org.powermock.api.easymock.PowerMock.mockStatic(org.apache.ambari.server.controller.utilities.ClusterControllerHelper.class);
        EasyMock.expect(org.apache.ambari.server.controller.utilities.ClusterControllerHelper.getClusterController()).andReturn(clusterController);
        org.powermock.api.easymock.PowerMock.replay(org.apache.ambari.server.controller.utilities.ClusterControllerHelper.class);
        EasyMock.replay(m_definition, m_cluster, m_clusters, m_definitionDao, m_alertsDao, factory, clusterController);
    }

    @org.junit.After
    public void teardown() throws java.lang.Exception {
    }

    @org.junit.Test
    public void testAlertFiresOKEvent() {
        org.powermock.api.easymock.PowerMock.mockStatic(org.apache.ambari.server.alerts.AmbariPerformanceRunnable.PerformanceArea.class);
        EasyMock.expect(org.apache.ambari.server.alerts.AmbariPerformanceRunnable.PerformanceArea.values()).andReturn(new org.apache.ambari.server.alerts.AmbariPerformanceRunnable.PerformanceArea[0]);
        org.powermock.api.easymock.PowerMock.replay(org.apache.ambari.server.alerts.AmbariPerformanceRunnable.PerformanceArea.class);
        org.apache.ambari.server.alerts.AmbariPerformanceRunnable runnable = new org.apache.ambari.server.alerts.AmbariPerformanceRunnable(m_definition.getDefinitionName());
        m_injector.injectMembers(runnable);
        runnable.run();
        org.junit.Assert.assertEquals(1, m_listener.getAlertEventReceivedCount(org.apache.ambari.server.events.AlertReceivedEvent.class));
        java.util.List<org.apache.ambari.server.events.AlertEvent> events = m_listener.getAlertEventInstances(org.apache.ambari.server.events.AlertReceivedEvent.class);
        org.junit.Assert.assertEquals(1, events.size());
        org.apache.ambari.server.events.AlertReceivedEvent event = ((org.apache.ambari.server.events.AlertReceivedEvent) (events.get(0)));
        org.apache.ambari.server.state.Alert alert = event.getAlert();
        org.junit.Assert.assertEquals("AMBARI", alert.getService());
        org.junit.Assert.assertEquals("AMBARI_SERVER", alert.getComponent());
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.AlertState.OK, alert.getState());
        org.junit.Assert.assertEquals(org.apache.ambari.server.alerts.AmbariPerformanceRunnableTest.DEFINITION_NAME, alert.getName());
        EasyMock.verify(m_cluster, m_clusters, m_definitionDao);
    }

    @org.junit.Test
    public void testAlertFiresUnknownEvent() {
        org.apache.ambari.server.orm.dao.RequestDAO requestDAO = m_injector.getInstance(org.apache.ambari.server.orm.dao.RequestDAO.class);
        EasyMock.expect(requestDAO.findAllRequestIds(org.easymock.EasyMock.anyInt(), org.easymock.EasyMock.anyBoolean())).andReturn(new java.util.ArrayList<>());
        EasyMock.replay(requestDAO);
        org.apache.ambari.server.alerts.AmbariPerformanceRunnable runnable = new org.apache.ambari.server.alerts.AmbariPerformanceRunnable(m_definition.getDefinitionName());
        m_injector.injectMembers(runnable);
        runnable.run();
        org.junit.Assert.assertEquals(1, m_listener.getAlertEventReceivedCount(org.apache.ambari.server.events.AlertReceivedEvent.class));
        java.util.List<org.apache.ambari.server.events.AlertEvent> events = m_listener.getAlertEventInstances(org.apache.ambari.server.events.AlertReceivedEvent.class);
        org.junit.Assert.assertEquals(1, events.size());
        org.apache.ambari.server.events.AlertReceivedEvent event = ((org.apache.ambari.server.events.AlertReceivedEvent) (events.get(0)));
        org.apache.ambari.server.state.Alert alert = event.getAlert();
        org.junit.Assert.assertEquals("AMBARI", alert.getService());
        org.junit.Assert.assertEquals("AMBARI_SERVER", alert.getComponent());
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.AlertState.UNKNOWN, alert.getState());
        org.junit.Assert.assertEquals(org.apache.ambari.server.alerts.AmbariPerformanceRunnableTest.DEFINITION_NAME, alert.getName());
        org.junit.Assert.assertTrue(alert.getText().contains("(OK)"));
        EasyMock.verify(m_cluster, m_clusters, m_definitionDao);
    }

    private class MockModule implements com.google.inject.Module {
        @java.lang.Override
        public void configure(com.google.inject.Binder binder) {
            org.apache.ambari.server.testutils.PartialNiceMockBinder.newBuilder().addConfigsBindings().addAlertDefinitionBinding().addLdapBindings().build().configure(binder);
            binder.bind(org.apache.ambari.server.orm.dao.AlertsDAO.class).toInstance(EasyMock.createNiceMock(org.apache.ambari.server.orm.dao.AlertsDAO.class));
            binder.bind(org.apache.ambari.server.actionmanager.ActionManager.class).toInstance(EasyMock.createNiceMock(org.apache.ambari.server.actionmanager.ActionManager.class));
            binder.bind(org.apache.ambari.server.orm.dao.HostRoleCommandDAO.class).toInstance(EasyMock.createNiceMock(org.apache.ambari.server.orm.dao.HostRoleCommandDAO.class));
            binder.bind(org.apache.ambari.server.state.alert.AlertDefinitionFactory.class).toInstance(EasyMock.createNiceMock(org.apache.ambari.server.state.alert.AlertDefinitionFactory.class));
            binder.bind(org.apache.ambari.server.controller.internal.ClusterResourceProvider.class).toInstance(EasyMock.createNiceMock(org.apache.ambari.server.controller.internal.ClusterResourceProvider.class));
            binder.bind(org.apache.ambari.server.controller.spi.ClusterController.class).toInstance(EasyMock.createNiceMock(org.apache.ambari.server.controller.spi.ClusterController.class));
            binder.bind(org.apache.ambari.server.orm.dao.RequestDAO.class).toInstance(EasyMock.createNiceMock(org.apache.ambari.server.orm.dao.RequestDAO.class));
        }
    }
}