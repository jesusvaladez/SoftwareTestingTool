package org.apache.ambari.server.alerts;
import org.easymock.EasyMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
public class AgentHeartbeatAlertRunnableTest {
    private static final long CLUSTER_ID = 1;

    private static final java.lang.String CLUSTER_NAME = "c1";

    private static final java.lang.String HOSTNAME = "c6401.ambari.apache.org";

    private static final java.lang.String DEFINITION_NAME = "ambari_server_agent_heartbeat";

    private static final java.lang.String DEFINITION_SERVICE = "AMBARI";

    private static final java.lang.String DEFINITION_COMPONENT = "AMBARI_SERVER";

    private static final java.lang.String DEFINITION_LABEL = "Mock Definition";

    private org.apache.ambari.server.state.Clusters m_clusters;

    private org.apache.ambari.server.state.Cluster m_cluster;

    private org.apache.ambari.server.state.Host m_host;

    private com.google.inject.Injector m_injector;

    private org.apache.ambari.server.orm.dao.AlertDefinitionDAO m_definitionDao;

    private org.apache.ambari.server.orm.entities.AlertDefinitionEntity m_definition;

    private org.apache.ambari.server.events.MockEventListener m_listener;

    private org.apache.ambari.server.events.publishers.AlertEventPublisher m_eventPublisher;

    private com.google.common.eventbus.EventBus m_synchronizedBus;

    @org.junit.Before
    public void setup() throws java.lang.Exception {
        m_injector = com.google.inject.Guice.createInjector(new org.apache.ambari.server.alerts.AgentHeartbeatAlertRunnableTest.MockModule());
        m_definitionDao = m_injector.getInstance(org.apache.ambari.server.orm.dao.AlertDefinitionDAO.class);
        m_clusters = m_injector.getInstance(org.apache.ambari.server.state.Clusters.class);
        m_cluster = m_injector.getInstance(org.apache.ambari.server.state.Cluster.class);
        m_eventPublisher = m_injector.getInstance(org.apache.ambari.server.events.publishers.AlertEventPublisher.class);
        m_listener = m_injector.getInstance(org.apache.ambari.server.events.MockEventListener.class);
        m_definition = org.easymock.EasyMock.createNiceMock(org.apache.ambari.server.orm.entities.AlertDefinitionEntity.class);
        m_host = org.easymock.EasyMock.createNiceMock(org.apache.ambari.server.state.Host.class);
        m_synchronizedBus = new com.google.common.eventbus.EventBus();
        java.lang.reflect.Field field = org.apache.ambari.server.events.publishers.AlertEventPublisher.class.getDeclaredField("m_eventBus");
        field.setAccessible(true);
        field.set(m_eventPublisher, m_synchronizedBus);
        m_synchronizedBus.register(m_listener);
        java.util.Map<java.lang.String, org.apache.ambari.server.state.Cluster> clusterMap = new java.util.HashMap<>();
        clusterMap.put(org.apache.ambari.server.alerts.AgentHeartbeatAlertRunnableTest.CLUSTER_NAME, m_cluster);
        java.util.Map<java.lang.String, org.apache.ambari.server.state.Host> hostMap = new java.util.HashMap<>();
        hostMap.put(org.apache.ambari.server.alerts.AgentHeartbeatAlertRunnableTest.HOSTNAME, m_host);
        EasyMock.expect(m_definition.getDefinitionName()).andReturn(org.apache.ambari.server.alerts.AgentHeartbeatAlertRunnableTest.DEFINITION_NAME).atLeastOnce();
        EasyMock.expect(m_definition.getServiceName()).andReturn(org.apache.ambari.server.alerts.AgentHeartbeatAlertRunnableTest.DEFINITION_SERVICE).atLeastOnce();
        EasyMock.expect(m_definition.getComponentName()).andReturn(org.apache.ambari.server.alerts.AgentHeartbeatAlertRunnableTest.DEFINITION_COMPONENT).atLeastOnce();
        EasyMock.expect(m_definition.getLabel()).andReturn(org.apache.ambari.server.alerts.AgentHeartbeatAlertRunnableTest.DEFINITION_LABEL).atLeastOnce();
        EasyMock.expect(m_definition.getEnabled()).andReturn(true).atLeastOnce();
        EasyMock.expect(m_host.getState()).andReturn(org.apache.ambari.server.state.HostState.HEALTHY);
        EasyMock.expect(m_cluster.getClusterId()).andReturn(org.apache.ambari.server.alerts.AgentHeartbeatAlertRunnableTest.CLUSTER_ID).atLeastOnce();
        EasyMock.expect(m_cluster.getHosts()).andReturn(java.util.Collections.singleton(m_host)).atLeastOnce();
        EasyMock.expect(m_clusters.getClusters()).andReturn(clusterMap).atLeastOnce();
        EasyMock.expect(m_definitionDao.findByName(org.apache.ambari.server.alerts.AgentHeartbeatAlertRunnableTest.CLUSTER_ID, org.apache.ambari.server.alerts.AgentHeartbeatAlertRunnableTest.DEFINITION_NAME)).andReturn(m_definition).atLeastOnce();
        org.easymock.EasyMock.replay(m_definition, m_host, m_cluster, m_clusters, m_definitionDao);
    }

    @org.junit.After
    public void teardown() throws java.lang.Exception {
    }

    @org.junit.Test
    public void testHealthyHostAlert() {
        junit.framework.Assert.assertEquals(0, m_listener.getAlertEventReceivedCount(org.apache.ambari.server.events.AlertReceivedEvent.class));
        org.apache.ambari.server.alerts.AgentHeartbeatAlertRunnable runnable = new org.apache.ambari.server.alerts.AgentHeartbeatAlertRunnable(m_definition.getDefinitionName());
        m_injector.injectMembers(runnable);
        runnable.run();
        junit.framework.Assert.assertEquals(1, m_listener.getAlertEventReceivedCount(org.apache.ambari.server.events.AlertReceivedEvent.class));
        java.util.List<org.apache.ambari.server.events.AlertEvent> events = m_listener.getAlertEventInstances(org.apache.ambari.server.events.AlertReceivedEvent.class);
        junit.framework.Assert.assertEquals(1, events.size());
        org.apache.ambari.server.events.AlertReceivedEvent event = ((org.apache.ambari.server.events.AlertReceivedEvent) (events.get(0)));
        org.apache.ambari.server.state.Alert alert = event.getAlert();
        junit.framework.Assert.assertEquals("AMBARI", alert.getService());
        junit.framework.Assert.assertEquals("AMBARI_SERVER", alert.getComponent());
        junit.framework.Assert.assertEquals(org.apache.ambari.server.state.AlertState.OK, alert.getState());
        junit.framework.Assert.assertEquals(org.apache.ambari.server.alerts.AgentHeartbeatAlertRunnableTest.DEFINITION_NAME, alert.getName());
        EasyMock.verify(m_definition, m_host, m_cluster, m_clusters, m_definitionDao);
    }

    @org.junit.Test
    public void testLostHeartbeatAlert() {
        org.easymock.EasyMock.reset(m_host);
        EasyMock.expect(m_host.getState()).andReturn(org.apache.ambari.server.state.HostState.HEARTBEAT_LOST).atLeastOnce();
        EasyMock.replay(m_host);
        junit.framework.Assert.assertEquals(0, m_listener.getAlertEventReceivedCount(org.apache.ambari.server.events.AlertReceivedEvent.class));
        org.apache.ambari.server.alerts.AgentHeartbeatAlertRunnable runnable = new org.apache.ambari.server.alerts.AgentHeartbeatAlertRunnable(m_definition.getDefinitionName());
        m_injector.injectMembers(runnable);
        runnable.run();
        junit.framework.Assert.assertEquals(1, m_listener.getAlertEventReceivedCount(org.apache.ambari.server.events.AlertReceivedEvent.class));
        java.util.List<org.apache.ambari.server.events.AlertEvent> events = m_listener.getAlertEventInstances(org.apache.ambari.server.events.AlertReceivedEvent.class);
        junit.framework.Assert.assertEquals(1, events.size());
        org.apache.ambari.server.events.AlertReceivedEvent event = ((org.apache.ambari.server.events.AlertReceivedEvent) (events.get(0)));
        org.apache.ambari.server.state.Alert alert = event.getAlert();
        junit.framework.Assert.assertEquals("AMBARI", alert.getService());
        junit.framework.Assert.assertEquals("AMBARI_SERVER", alert.getComponent());
        junit.framework.Assert.assertEquals(org.apache.ambari.server.state.AlertState.CRITICAL, alert.getState());
        junit.framework.Assert.assertEquals(org.apache.ambari.server.alerts.AgentHeartbeatAlertRunnableTest.DEFINITION_NAME, alert.getName());
        EasyMock.verify(m_definition, m_host, m_cluster, m_clusters, m_definitionDao);
    }

    @org.junit.Test
    public void testUnhealthyHostAlert() {
        org.easymock.EasyMock.reset(m_host);
        EasyMock.expect(m_host.getState()).andReturn(org.apache.ambari.server.state.HostState.UNHEALTHY).atLeastOnce();
        EasyMock.replay(m_host);
        junit.framework.Assert.assertEquals(0, m_listener.getAlertEventReceivedCount(org.apache.ambari.server.events.AlertReceivedEvent.class));
        org.apache.ambari.server.alerts.AgentHeartbeatAlertRunnable runnable = new org.apache.ambari.server.alerts.AgentHeartbeatAlertRunnable(m_definition.getDefinitionName());
        m_injector.injectMembers(runnable);
        runnable.run();
        junit.framework.Assert.assertEquals(1, m_listener.getAlertEventReceivedCount(org.apache.ambari.server.events.AlertReceivedEvent.class));
        java.util.List<org.apache.ambari.server.events.AlertEvent> events = m_listener.getAlertEventInstances(org.apache.ambari.server.events.AlertReceivedEvent.class);
        junit.framework.Assert.assertEquals(1, events.size());
        org.apache.ambari.server.events.AlertReceivedEvent event = ((org.apache.ambari.server.events.AlertReceivedEvent) (events.get(0)));
        org.apache.ambari.server.state.Alert alert = event.getAlert();
        junit.framework.Assert.assertEquals("AMBARI", alert.getService());
        junit.framework.Assert.assertEquals("AMBARI_SERVER", alert.getComponent());
        junit.framework.Assert.assertEquals(org.apache.ambari.server.state.AlertState.CRITICAL, alert.getState());
        junit.framework.Assert.assertEquals(org.apache.ambari.server.alerts.AgentHeartbeatAlertRunnableTest.DEFINITION_NAME, alert.getName());
        EasyMock.verify(m_definition, m_host, m_cluster, m_clusters, m_definitionDao);
    }

    private class MockModule implements com.google.inject.Module {
        @java.lang.Override
        public void configure(com.google.inject.Binder binder) {
            org.apache.ambari.server.testutils.PartialNiceMockBinder.newBuilder().addConfigsBindings().addAlertDefinitionBinding().addLdapBindings().build().configure(binder);
        }
    }
}