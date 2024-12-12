package org.apache.ambari.server.alerts;
import org.easymock.EasyMock;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import static org.easymock.EasyMock.anyLong;
import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.anyString;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.reset;
import static org.easymock.EasyMock.verify;
@org.junit.runner.RunWith(org.powermock.modules.junit4.PowerMockRunner.class)
@org.powermock.core.classloader.annotations.PrepareForTest({ org.apache.ambari.server.alerts.StaleAlertRunnable.class, java.lang.management.ManagementFactory.class })
public class StaleAlertRunnableTest {
    private static final long CLUSTER_ID = 1;

    private static final java.lang.String CLUSTER_NAME = "c1";

    private static final java.lang.String DEFINITION_NAME = "ambari_server_stale_alerts";

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

    private org.apache.ambari.server.state.alert.AlertHelper m_alertHelper;

    private org.apache.ambari.server.state.Host m_host;

    private org.apache.ambari.server.events.publishers.AlertEventPublisher m_eventPublisher;

    private com.google.common.eventbus.EventBus m_synchronizedBus;

    private java.lang.management.RuntimeMXBean m_runtimeMXBean;

    @org.junit.Before
    public void setup() throws java.lang.Exception {
        m_injector = com.google.inject.Guice.createInjector(new org.apache.ambari.server.alerts.StaleAlertRunnableTest.MockModule());
        m_alertsDao = m_injector.getInstance(org.apache.ambari.server.orm.dao.AlertsDAO.class);
        m_definitionDao = m_injector.getInstance(org.apache.ambari.server.orm.dao.AlertDefinitionDAO.class);
        m_clusters = m_injector.getInstance(org.apache.ambari.server.state.Clusters.class);
        m_cluster = m_injector.getInstance(org.apache.ambari.server.state.Cluster.class);
        m_eventPublisher = m_injector.getInstance(org.apache.ambari.server.events.publishers.AlertEventPublisher.class);
        m_listener = m_injector.getInstance(org.apache.ambari.server.events.MockEventListener.class);
        m_definition = org.easymock.EasyMock.createNiceMock(org.apache.ambari.server.orm.entities.AlertDefinitionEntity.class);
        m_alertHelper = m_injector.getInstance(org.apache.ambari.server.state.alert.AlertHelper.class);
        m_synchronizedBus = new com.google.common.eventbus.EventBus();
        java.lang.reflect.Field field = org.apache.ambari.server.events.publishers.AlertEventPublisher.class.getDeclaredField("m_eventBus");
        field.setAccessible(true);
        field.set(m_eventPublisher, m_synchronizedBus);
        m_synchronizedBus.register(m_listener);
        java.util.Map<java.lang.String, org.apache.ambari.server.state.Cluster> clusterMap = new java.util.HashMap<>();
        clusterMap.put(org.apache.ambari.server.alerts.StaleAlertRunnableTest.CLUSTER_NAME, m_cluster);
        EasyMock.expect(m_definition.getDefinitionId()).andReturn(1L).atLeastOnce();
        EasyMock.expect(m_definition.getDefinitionName()).andReturn(org.apache.ambari.server.alerts.StaleAlertRunnableTest.DEFINITION_NAME).atLeastOnce();
        EasyMock.expect(m_definition.getServiceName()).andReturn(org.apache.ambari.server.alerts.StaleAlertRunnableTest.DEFINITION_SERVICE).atLeastOnce();
        EasyMock.expect(m_definition.getComponentName()).andReturn(org.apache.ambari.server.alerts.StaleAlertRunnableTest.DEFINITION_COMPONENT).atLeastOnce();
        EasyMock.expect(m_definition.getLabel()).andReturn(org.apache.ambari.server.alerts.StaleAlertRunnableTest.DEFINITION_LABEL).atLeastOnce();
        EasyMock.expect(m_definition.getEnabled()).andReturn(true).atLeastOnce();
        EasyMock.expect(m_definition.getScheduleInterval()).andReturn(org.apache.ambari.server.alerts.StaleAlertRunnableTest.DEFINITION_INTERVAL).atLeastOnce();
        EasyMock.expect(m_definition.getClusterId()).andReturn(org.apache.ambari.server.alerts.StaleAlertRunnableTest.CLUSTER_ID).atLeastOnce();
        EasyMock.expect(m_definition.getSource()).andReturn("{\"type\" : \"SERVER\"}").anyTimes();
        EasyMock.expect(m_cluster.getClusterId()).andReturn(org.apache.ambari.server.alerts.StaleAlertRunnableTest.CLUSTER_ID).atLeastOnce();
        EasyMock.expect(m_clusters.getClusters()).andReturn(clusterMap).atLeastOnce();
        EasyMock.expect(m_definitionDao.findByName(org.apache.ambari.server.alerts.StaleAlertRunnableTest.CLUSTER_ID, org.apache.ambari.server.alerts.StaleAlertRunnableTest.DEFINITION_NAME)).andReturn(m_definition).atLeastOnce();
        EasyMock.expect(m_alertsDao.findCurrentByCluster(org.apache.ambari.server.alerts.StaleAlertRunnableTest.CLUSTER_ID)).andReturn(m_currentAlerts).atLeastOnce();
        m_host = EasyMock.createNiceMock(org.apache.ambari.server.state.Host.class);
        EasyMock.expect(m_host.getHostId()).andReturn(1L);
        EasyMock.expect(m_host.getState()).andReturn(org.apache.ambari.server.state.HostState.HEALTHY);
        EasyMock.expect(m_cluster.getHost(EasyMock.anyString())).andReturn(m_host).anyTimes();
        m_runtimeMXBean = org.easymock.EasyMock.createNiceMock(java.lang.management.RuntimeMXBean.class);
        org.powermock.api.easymock.PowerMock.mockStatic(java.lang.management.ManagementFactory.class);
        EasyMock.expect(java.lang.management.ManagementFactory.getRuntimeMXBean()).andReturn(m_runtimeMXBean).atLeastOnce();
        org.powermock.api.easymock.PowerMock.replay(java.lang.management.ManagementFactory.class);
        EasyMock.expect(m_runtimeMXBean.getUptime()).andReturn(360000L);
        EasyMock.expect(m_alertHelper.getHostIdsByDefinitionId(EasyMock.anyLong())).andReturn(java.util.Collections.emptyList()).anyTimes();
        EasyMock.expect(m_alertHelper.getWaitFactorMultiplier(EasyMock.anyObject(org.apache.ambari.server.state.alert.AlertDefinition.class))).andReturn(2).anyTimes();
        EasyMock.expect(m_alertHelper.getStaleAlerts(EasyMock.anyLong())).andReturn(java.util.Collections.EMPTY_MAP).anyTimes();
        EasyMock.replay(m_host, m_definition, m_cluster, m_clusters, m_definitionDao, m_alertsDao, m_runtimeMXBean, m_alertHelper);
    }

    @org.junit.After
    public void teardown() throws java.lang.Exception {
    }

    @org.junit.Test
    public void testPrepareHostDefinitions() {
        org.apache.ambari.server.alerts.StaleAlertRunnable runnable = new org.apache.ambari.server.alerts.StaleAlertRunnable(m_definition.getDefinitionName());
        org.apache.ambari.server.agent.stomp.AlertDefinitionsHolder alertDefinitionHolder = m_injector.getInstance(org.apache.ambari.server.agent.stomp.AlertDefinitionsHolder.class);
        java.lang.Long alertDefinitionId1 = 1L;
        java.lang.Long alertDefinitionId2 = 2L;
        java.lang.Long alertDefinitionId3 = 3L;
        java.lang.Long alertDefinitionId4 = 4L;
        java.lang.Long hostId1 = 1L;
        java.lang.Long hostId2 = 2L;
        org.apache.ambari.server.state.alert.AlertDefinition alertDefinition1 = new org.apache.ambari.server.state.alert.AlertDefinition();
        alertDefinition1.setDefinitionId(alertDefinitionId1);
        org.apache.ambari.server.state.alert.AlertDefinition alertDefinition2 = new org.apache.ambari.server.state.alert.AlertDefinition();
        alertDefinition2.setDefinitionId(alertDefinitionId2);
        org.apache.ambari.server.state.alert.AlertDefinition alertDefinition3 = new org.apache.ambari.server.state.alert.AlertDefinition();
        alertDefinition3.setDefinitionId(alertDefinitionId3);
        org.apache.ambari.server.state.alert.AlertDefinition alertDefinition4 = new org.apache.ambari.server.state.alert.AlertDefinition();
        alertDefinition4.setDefinitionId(alertDefinitionId4);
        org.apache.ambari.server.agent.stomp.dto.AlertCluster alertCluster1host1 = new org.apache.ambari.server.agent.stomp.dto.AlertCluster(java.util.Collections.singletonMap(alertDefinitionId1, alertDefinition1), "host1");
        org.apache.ambari.server.agent.stomp.dto.AlertCluster alertCluster2host1 = new org.apache.ambari.server.agent.stomp.dto.AlertCluster(java.util.Collections.singletonMap(alertDefinitionId2, alertDefinition2), "host1");
        org.apache.ambari.server.agent.stomp.dto.AlertCluster alertCluster1host2 = new org.apache.ambari.server.agent.stomp.dto.AlertCluster(new java.util.HashMap() {
            {
                put(alertDefinitionId3, alertDefinition3);
                put(alertDefinitionId1, alertDefinition1);
            }
        }, "host2");
        org.apache.ambari.server.agent.stomp.dto.AlertCluster alertCluster2host2 = new org.apache.ambari.server.agent.stomp.dto.AlertCluster(java.util.Collections.singletonMap(alertDefinitionId4, alertDefinition4), "host2");
        org.apache.ambari.server.events.AlertDefinitionsAgentUpdateEvent hostUpdate1 = new org.apache.ambari.server.events.AlertDefinitionsAgentUpdateEvent(org.apache.ambari.server.events.AlertDefinitionEventType.CREATE, new java.util.HashMap() {
            {
                put(1L, alertCluster1host1);
                put(2L, alertCluster2host1);
            }
        }, "host1", hostId1);
        org.apache.ambari.server.events.AlertDefinitionsAgentUpdateEvent hostUpdate2 = new org.apache.ambari.server.events.AlertDefinitionsAgentUpdateEvent(org.apache.ambari.server.events.AlertDefinitionEventType.CREATE, new java.util.HashMap() {
            {
                put(1L, alertCluster1host2);
                put(2L, alertCluster2host2);
            }
        }, "host2", hostId2);
        alertDefinitionHolder.setData(hostUpdate1, 1L);
        alertDefinitionHolder.setData(hostUpdate2, 2L);
        m_injector.injectMembers(runnable);
        java.util.Map<java.lang.Long, java.util.List<java.lang.Long>> alertDefinitionsToHost = runnable.prepareHostDefinitions(hostId1);
        junit.framework.Assert.assertEquals(2, alertDefinitionsToHost.size());
        junit.framework.Assert.assertNotNull(alertDefinitionsToHost.get(alertDefinitionId1));
        junit.framework.Assert.assertEquals(2, alertDefinitionsToHost.get(alertDefinitionId1).size());
        junit.framework.Assert.assertTrue(alertDefinitionsToHost.get(alertDefinitionId1).contains(hostId1));
        junit.framework.Assert.assertTrue(alertDefinitionsToHost.get(alertDefinitionId1).contains(hostId2));
        junit.framework.Assert.assertNotNull(alertDefinitionsToHost.get(alertDefinitionId3));
        junit.framework.Assert.assertEquals(1, alertDefinitionsToHost.get(alertDefinitionId3).size());
        junit.framework.Assert.assertEquals(java.lang.Long.valueOf(hostId2), alertDefinitionsToHost.get(alertDefinitionId3).get(0));
        alertDefinitionsToHost = runnable.prepareHostDefinitions(hostId2);
        junit.framework.Assert.assertEquals(2, alertDefinitionsToHost.size());
        junit.framework.Assert.assertNotNull(alertDefinitionsToHost.get(alertDefinitionId2));
        junit.framework.Assert.assertEquals(1, alertDefinitionsToHost.get(alertDefinitionId2).size());
        junit.framework.Assert.assertEquals(java.lang.Long.valueOf(hostId1), alertDefinitionsToHost.get(alertDefinitionId2).get(0));
        junit.framework.Assert.assertNotNull(alertDefinitionsToHost.get(alertDefinitionId4));
        junit.framework.Assert.assertEquals(1, alertDefinitionsToHost.get(alertDefinitionId4).size());
        junit.framework.Assert.assertEquals(java.lang.Long.valueOf(hostId2), alertDefinitionsToHost.get(alertDefinitionId4).get(0));
    }

    @org.junit.Test
    public void testAllAlertsAreCurrent() {
        org.apache.ambari.server.orm.entities.AlertDefinitionEntity definition = new org.apache.ambari.server.orm.entities.AlertDefinitionEntity();
        definition.setClusterId(org.apache.ambari.server.alerts.StaleAlertRunnableTest.CLUSTER_ID);
        definition.setDefinitionName("foo-definition");
        definition.setServiceName("HDFS");
        definition.setComponentName("NAMENODE");
        definition.setEnabled(true);
        definition.setScheduleInterval(1);
        org.apache.ambari.server.orm.entities.AlertCurrentEntity current1 = EasyMock.createNiceMock(org.apache.ambari.server.orm.entities.AlertCurrentEntity.class);
        org.apache.ambari.server.orm.entities.AlertHistoryEntity history1 = EasyMock.createNiceMock(org.apache.ambari.server.orm.entities.AlertHistoryEntity.class);
        EasyMock.expect(current1.getAlertHistory()).andReturn(history1).atLeastOnce();
        EasyMock.expect(current1.getDefinitionId()).andReturn(1L).atLeastOnce();
        EasyMock.expect(history1.getAlertDefinition()).andReturn(definition).atLeastOnce();
        EasyMock.expect(current1.getMaintenanceState()).andReturn(org.apache.ambari.server.state.MaintenanceState.OFF).atLeastOnce();
        EasyMock.expect(current1.getLatestTimestamp()).andReturn(java.lang.System.currentTimeMillis()).atLeastOnce();
        EasyMock.replay(current1, history1);
        m_currentAlerts.add(current1);
        junit.framework.Assert.assertEquals(0, m_listener.getAlertEventReceivedCount(org.apache.ambari.server.events.AlertReceivedEvent.class));
        org.apache.ambari.server.alerts.StaleAlertRunnable runnable = new org.apache.ambari.server.alerts.StaleAlertRunnable(m_definition.getDefinitionName());
        m_injector.injectMembers(runnable);
        runnable.run();
        checkSingleEventToState(org.apache.ambari.server.state.AlertState.OK);
        EasyMock.verify(m_cluster, m_clusters, m_definitionDao, m_alertHelper);
    }

    @org.junit.Test
    public void testAmbariStaleAlert() {
        org.apache.ambari.server.orm.entities.AlertDefinitionEntity definition = new org.apache.ambari.server.orm.entities.AlertDefinitionEntity();
        definition.setClusterId(org.apache.ambari.server.alerts.StaleAlertRunnableTest.CLUSTER_ID);
        definition.setDefinitionName("foo-definition");
        definition.setServiceName("HDFS");
        definition.setComponentName("NAMENODE");
        definition.setEnabled(true);
        definition.setScheduleInterval(1);
        org.apache.ambari.server.orm.entities.AlertCurrentEntity current1 = EasyMock.createNiceMock(org.apache.ambari.server.orm.entities.AlertCurrentEntity.class);
        org.apache.ambari.server.orm.entities.AlertHistoryEntity history1 = EasyMock.createNiceMock(org.apache.ambari.server.orm.entities.AlertHistoryEntity.class);
        EasyMock.expect(current1.getAlertHistory()).andReturn(history1).atLeastOnce();
        EasyMock.expect(current1.getDefinitionId()).andReturn(1L).atLeastOnce();
        EasyMock.expect(history1.getAlertDefinition()).andReturn(definition).atLeastOnce();
        EasyMock.expect(current1.getMaintenanceState()).andReturn(org.apache.ambari.server.state.MaintenanceState.OFF).atLeastOnce();
        EasyMock.expect(current1.getLatestTimestamp()).andReturn(1L).atLeastOnce();
        EasyMock.replay(current1, history1);
        m_currentAlerts.add(current1);
        junit.framework.Assert.assertEquals(0, m_listener.getAlertEventReceivedCount(org.apache.ambari.server.events.AlertReceivedEvent.class));
        org.apache.ambari.server.alerts.StaleAlertRunnable runnable = new org.apache.ambari.server.alerts.StaleAlertRunnable(m_definition.getDefinitionName());
        m_injector.injectMembers(runnable);
        runnable.run();
        checkSingleEventToState(org.apache.ambari.server.state.AlertState.CRITICAL);
        EasyMock.verify(m_cluster, m_clusters, m_definitionDao, m_alertHelper);
    }

    @org.junit.Test
    public void testStaleAlertFromAgent() {
        java.lang.Long alertDefinitionId = 1L;
        org.apache.ambari.server.orm.entities.AlertDefinitionEntity definition = new org.apache.ambari.server.orm.entities.AlertDefinitionEntity();
        definition.setClusterId(org.apache.ambari.server.alerts.StaleAlertRunnableTest.CLUSTER_ID);
        definition.setDefinitionName("foo-definition");
        definition.setServiceName("HDFS");
        definition.setComponentName("NAMENODE");
        definition.setEnabled(true);
        definition.setScheduleInterval(1);
        org.apache.ambari.server.orm.entities.AlertCurrentEntity current1 = EasyMock.createNiceMock(org.apache.ambari.server.orm.entities.AlertCurrentEntity.class);
        org.apache.ambari.server.orm.entities.AlertHistoryEntity history1 = EasyMock.createNiceMock(org.apache.ambari.server.orm.entities.AlertHistoryEntity.class);
        EasyMock.expect(current1.getAlertHistory()).andReturn(history1).atLeastOnce();
        EasyMock.expect(current1.getDefinitionId()).andReturn(alertDefinitionId).atLeastOnce();
        EasyMock.expect(history1.getAlertDefinition()).andReturn(definition).atLeastOnce();
        EasyMock.expect(history1.getHostName()).andReturn("host1").atLeastOnce();
        EasyMock.reset(m_alertHelper);
        EasyMock.expect(m_alertHelper.getWaitFactorMultiplier(EasyMock.anyObject(org.apache.ambari.server.state.alert.AlertDefinition.class))).andReturn(2).anyTimes();
        EasyMock.expect(m_alertHelper.getStaleAlerts(EasyMock.anyLong())).andReturn(java.util.Collections.singletonMap(alertDefinitionId, 0L)).atLeastOnce();
        EasyMock.expect(current1.getMaintenanceState()).andReturn(org.apache.ambari.server.state.MaintenanceState.OFF).atLeastOnce();
        EasyMock.expect(current1.getLatestTimestamp()).andReturn(java.lang.System.currentTimeMillis()).atLeastOnce();
        EasyMock.replay(current1, history1, m_alertHelper);
        m_currentAlerts.add(current1);
        junit.framework.Assert.assertEquals(0, m_listener.getAlertEventReceivedCount(org.apache.ambari.server.events.AlertReceivedEvent.class));
        org.apache.ambari.server.alerts.StaleAlertRunnable runnable = new org.apache.ambari.server.alerts.StaleAlertRunnable(m_definition.getDefinitionName());
        m_injector.injectMembers(runnable);
        runnable.run();
        checkSingleEventToState(org.apache.ambari.server.state.AlertState.CRITICAL);
        EasyMock.verify(m_cluster, m_clusters, m_definitionDao, m_alertHelper);
    }

    @org.junit.Test
    public void testStaleAlertHeartbeatLost() {
        java.lang.Long alertDefinitionId = 1L;
        org.apache.ambari.server.orm.entities.AlertDefinitionEntity definition = new org.apache.ambari.server.orm.entities.AlertDefinitionEntity();
        definition.setClusterId(org.apache.ambari.server.alerts.StaleAlertRunnableTest.CLUSTER_ID);
        definition.setDefinitionName("foo-definition");
        definition.setServiceName("HDFS");
        definition.setComponentName("NAMENODE");
        definition.setEnabled(true);
        definition.setScheduleInterval(1);
        org.apache.ambari.server.orm.entities.AlertCurrentEntity current1 = EasyMock.createNiceMock(org.apache.ambari.server.orm.entities.AlertCurrentEntity.class);
        org.apache.ambari.server.orm.entities.AlertHistoryEntity history1 = EasyMock.createNiceMock(org.apache.ambari.server.orm.entities.AlertHistoryEntity.class);
        EasyMock.expect(current1.getAlertHistory()).andReturn(history1).atLeastOnce();
        EasyMock.expect(current1.getDefinitionId()).andReturn(alertDefinitionId).atLeastOnce();
        EasyMock.expect(history1.getAlertDefinition()).andReturn(definition).atLeastOnce();
        EasyMock.expect(history1.getHostName()).andReturn("host1").atLeastOnce();
        EasyMock.expect(current1.getMaintenanceState()).andReturn(org.apache.ambari.server.state.MaintenanceState.OFF).atLeastOnce();
        EasyMock.expect(current1.getLatestTimestamp()).andReturn(java.lang.System.currentTimeMillis()).atLeastOnce();
        EasyMock.reset(m_cluster, m_host);
        m_host = EasyMock.createNiceMock(org.apache.ambari.server.state.Host.class);
        EasyMock.expect(m_host.getHostId()).andReturn(1L);
        EasyMock.expect(m_host.getState()).andReturn(org.apache.ambari.server.state.HostState.HEARTBEAT_LOST);
        EasyMock.expect(m_cluster.getClusterId()).andReturn(org.apache.ambari.server.alerts.StaleAlertRunnableTest.CLUSTER_ID).atLeastOnce();
        EasyMock.expect(m_cluster.getHost(EasyMock.anyString())).andReturn(m_host).anyTimes();
        EasyMock.replay(current1, history1, m_host, m_cluster);
        m_currentAlerts.add(current1);
        junit.framework.Assert.assertEquals(0, m_listener.getAlertEventReceivedCount(org.apache.ambari.server.events.AlertReceivedEvent.class));
        org.apache.ambari.server.alerts.StaleAlertRunnable runnable = new org.apache.ambari.server.alerts.StaleAlertRunnable(m_definition.getDefinitionName());
        m_injector.injectMembers(runnable);
        runnable.run();
        checkSingleEventToState(org.apache.ambari.server.state.AlertState.CRITICAL);
        EasyMock.verify(m_cluster, m_clusters, m_definitionDao, m_alertHelper);
    }

    @org.junit.Test
    public void testStaleAlertWithHostIgnore() {
        java.lang.Long alertDefinitionId = 1L;
        prepareAlertHolderWithHostAlert(alertDefinitionId);
        org.apache.ambari.server.orm.entities.AlertDefinitionEntity definition = new org.apache.ambari.server.orm.entities.AlertDefinitionEntity();
        definition.setClusterId(org.apache.ambari.server.alerts.StaleAlertRunnableTest.CLUSTER_ID);
        definition.setDefinitionName("foo-definition");
        definition.setServiceName("HDFS");
        definition.setComponentName("NAMENODE");
        definition.setEnabled(true);
        definition.setScheduleInterval(1);
        org.apache.ambari.server.orm.entities.AlertCurrentEntity current1 = EasyMock.createNiceMock(org.apache.ambari.server.orm.entities.AlertCurrentEntity.class);
        org.apache.ambari.server.orm.entities.AlertHistoryEntity history1 = EasyMock.createNiceMock(org.apache.ambari.server.orm.entities.AlertHistoryEntity.class);
        EasyMock.expect(current1.getAlertHistory()).andReturn(history1).atLeastOnce();
        EasyMock.expect(current1.getDefinitionId()).andReturn(alertDefinitionId).atLeastOnce();
        EasyMock.expect(history1.getAlertDefinition()).andReturn(definition).atLeastOnce();
        EasyMock.expect(current1.getMaintenanceState()).andReturn(org.apache.ambari.server.state.MaintenanceState.OFF).atLeastOnce();
        EasyMock.expect(current1.getLatestTimestamp()).andReturn(java.lang.System.currentTimeMillis()).atLeastOnce();
        EasyMock.reset(m_cluster);
        org.apache.ambari.server.state.Host host1 = EasyMock.createNiceMock(org.apache.ambari.server.state.Host.class);
        org.apache.ambari.server.state.Host host2 = EasyMock.createNiceMock(org.apache.ambari.server.state.Host.class);
        EasyMock.expect(host1.getHostId()).andReturn(1L);
        EasyMock.expect(host1.getState()).andReturn(org.apache.ambari.server.state.HostState.HEARTBEAT_LOST).atLeastOnce();
        EasyMock.expect(host1.getLastHeartbeatTime()).andReturn(1L);
        EasyMock.expect(host2.getHostId()).andReturn(2L);
        EasyMock.expect(host2.getState()).andReturn(org.apache.ambari.server.state.HostState.HEALTHY).atLeastOnce();
        EasyMock.expect(host2.getLastHeartbeatTime()).andReturn(2L);
        EasyMock.expect(m_cluster.getClusterId()).andReturn(org.apache.ambari.server.alerts.StaleAlertRunnableTest.CLUSTER_ID).atLeastOnce();
        EasyMock.expect(m_cluster.getHost(EasyMock.eq(1L))).andReturn(host1).anyTimes();
        EasyMock.expect(m_cluster.getHost(EasyMock.eq(2L))).andReturn(host2).anyTimes();
        EasyMock.replay(current1, history1, host1, host2, m_cluster);
        m_currentAlerts.add(current1);
        junit.framework.Assert.assertEquals(0, m_listener.getAlertEventReceivedCount(org.apache.ambari.server.events.AlertReceivedEvent.class));
        org.apache.ambari.server.alerts.StaleAlertRunnable runnable = new org.apache.ambari.server.alerts.StaleAlertRunnable(m_definition.getDefinitionName());
        m_injector.injectMembers(runnable);
        runnable.run();
        checkSingleEventToState(org.apache.ambari.server.state.AlertState.OK);
        EasyMock.verify(m_cluster, m_clusters, m_definitionDao, m_alertHelper);
    }

    @org.junit.Test
    public void testStaleAlertWithHostIgnoreCritical() {
        java.lang.Long alertDefinitionId = 1L;
        prepareAlertHolderWithHostAlert(alertDefinitionId);
        org.apache.ambari.server.orm.entities.AlertDefinitionEntity definition = new org.apache.ambari.server.orm.entities.AlertDefinitionEntity();
        definition.setClusterId(org.apache.ambari.server.alerts.StaleAlertRunnableTest.CLUSTER_ID);
        definition.setDefinitionName("foo-definition");
        definition.setServiceName("HDFS");
        definition.setComponentName("NAMENODE");
        definition.setEnabled(true);
        definition.setScheduleInterval(1);
        org.apache.ambari.server.orm.entities.AlertCurrentEntity current1 = EasyMock.createNiceMock(org.apache.ambari.server.orm.entities.AlertCurrentEntity.class);
        org.apache.ambari.server.orm.entities.AlertHistoryEntity history1 = EasyMock.createNiceMock(org.apache.ambari.server.orm.entities.AlertHistoryEntity.class);
        EasyMock.expect(current1.getAlertHistory()).andReturn(history1).atLeastOnce();
        EasyMock.expect(current1.getDefinitionId()).andReturn(alertDefinitionId).atLeastOnce();
        EasyMock.expect(history1.getAlertDefinition()).andReturn(definition).atLeastOnce();
        EasyMock.expect(current1.getMaintenanceState()).andReturn(org.apache.ambari.server.state.MaintenanceState.OFF).atLeastOnce();
        EasyMock.expect(current1.getLatestTimestamp()).andReturn(java.lang.System.currentTimeMillis()).atLeastOnce();
        EasyMock.reset(m_cluster);
        org.apache.ambari.server.state.Host host1 = EasyMock.createNiceMock(org.apache.ambari.server.state.Host.class);
        org.apache.ambari.server.state.Host host2 = EasyMock.createNiceMock(org.apache.ambari.server.state.Host.class);
        EasyMock.expect(host1.getHostId()).andReturn(1L);
        EasyMock.expect(host1.getState()).andReturn(org.apache.ambari.server.state.HostState.HEARTBEAT_LOST).atLeastOnce();
        EasyMock.expect(host1.getLastHeartbeatTime()).andReturn(1L);
        EasyMock.expect(host2.getHostId()).andReturn(2L);
        EasyMock.expect(host2.getState()).andReturn(org.apache.ambari.server.state.HostState.HEARTBEAT_LOST).atLeastOnce();
        EasyMock.expect(host2.getLastHeartbeatTime()).andReturn(2L);
        EasyMock.expect(m_cluster.getClusterId()).andReturn(org.apache.ambari.server.alerts.StaleAlertRunnableTest.CLUSTER_ID).atLeastOnce();
        EasyMock.expect(m_cluster.getHost(EasyMock.eq(1L))).andReturn(host1).anyTimes();
        EasyMock.expect(m_cluster.getHost(EasyMock.eq(2L))).andReturn(host2).anyTimes();
        EasyMock.replay(current1, history1, host1, host2, m_cluster);
        m_currentAlerts.add(current1);
        junit.framework.Assert.assertEquals(0, m_listener.getAlertEventReceivedCount(org.apache.ambari.server.events.AlertReceivedEvent.class));
        org.apache.ambari.server.alerts.StaleAlertRunnable runnable = new org.apache.ambari.server.alerts.StaleAlertRunnable(m_definition.getDefinitionName());
        m_injector.injectMembers(runnable);
        runnable.run();
        checkSingleEventToState(org.apache.ambari.server.state.AlertState.CRITICAL);
        EasyMock.verify(m_cluster, m_clusters, m_definitionDao, m_alertHelper);
    }

    private void prepareAlertHolderWithHostAlert(java.lang.Long alertDefinitionId) {
        org.apache.ambari.server.agent.stomp.AlertDefinitionsHolder alertDefinitionHolder = m_injector.getInstance(org.apache.ambari.server.agent.stomp.AlertDefinitionsHolder.class);
        java.lang.Long hostId1 = 1L;
        java.lang.Long hostId2 = 2L;
        org.apache.ambari.server.state.alert.AlertDefinition alertDefinition1 = new org.apache.ambari.server.state.alert.AlertDefinition();
        alertDefinition1.setDefinitionId(alertDefinitionId);
        org.apache.ambari.server.agent.stomp.dto.AlertCluster alertCluster1host1 = new org.apache.ambari.server.agent.stomp.dto.AlertCluster(java.util.Collections.singletonMap(alertDefinitionId, alertDefinition1), "host1");
        org.apache.ambari.server.agent.stomp.dto.AlertCluster alertCluster1host2 = new org.apache.ambari.server.agent.stomp.dto.AlertCluster(java.util.Collections.singletonMap(alertDefinitionId, alertDefinition1), "host2");
        org.apache.ambari.server.events.AlertDefinitionsAgentUpdateEvent hostUpdate1 = new org.apache.ambari.server.events.AlertDefinitionsAgentUpdateEvent(org.apache.ambari.server.events.AlertDefinitionEventType.CREATE, java.util.Collections.singletonMap(1L, alertCluster1host1), "host1", hostId1);
        org.apache.ambari.server.events.AlertDefinitionsAgentUpdateEvent hostUpdate2 = new org.apache.ambari.server.events.AlertDefinitionsAgentUpdateEvent(org.apache.ambari.server.events.AlertDefinitionEventType.CREATE, java.util.Collections.singletonMap(1L, alertCluster1host2), "host2", hostId2);
        alertDefinitionHolder.setData(hostUpdate1, 1L);
        alertDefinitionHolder.setData(hostUpdate2, 2L);
    }

    @org.junit.Test
    public void testStaleAlertInMaintenaceMode() {
        org.apache.ambari.server.orm.entities.AlertDefinitionEntity definition = new org.apache.ambari.server.orm.entities.AlertDefinitionEntity();
        definition.setClusterId(org.apache.ambari.server.alerts.StaleAlertRunnableTest.CLUSTER_ID);
        definition.setDefinitionName("foo-definition");
        definition.setServiceName("HDFS");
        definition.setComponentName("NAMENODE");
        definition.setEnabled(true);
        definition.setScheduleInterval(1);
        org.apache.ambari.server.orm.entities.AlertCurrentEntity current1 = EasyMock.createNiceMock(org.apache.ambari.server.orm.entities.AlertCurrentEntity.class);
        org.apache.ambari.server.orm.entities.AlertHistoryEntity history1 = EasyMock.createNiceMock(org.apache.ambari.server.orm.entities.AlertHistoryEntity.class);
        org.apache.ambari.server.orm.entities.AlertCurrentEntity current2 = EasyMock.createNiceMock(org.apache.ambari.server.orm.entities.AlertCurrentEntity.class);
        org.apache.ambari.server.orm.entities.AlertHistoryEntity history2 = EasyMock.createNiceMock(org.apache.ambari.server.orm.entities.AlertHistoryEntity.class);
        EasyMock.expect(current1.getAlertHistory()).andReturn(history1).atLeastOnce();
        EasyMock.expect(history1.getAlertDefinition()).andReturn(definition).atLeastOnce();
        EasyMock.expect(current2.getAlertHistory()).andReturn(history2).atLeastOnce();
        EasyMock.expect(history2.getAlertDefinition()).andReturn(definition).atLeastOnce();
        EasyMock.expect(current1.getMaintenanceState()).andReturn(org.apache.ambari.server.state.MaintenanceState.ON).atLeastOnce();
        EasyMock.expect(current1.getLatestTimestamp()).andReturn(1L).atLeastOnce();
        EasyMock.expect(current2.getMaintenanceState()).andReturn(org.apache.ambari.server.state.MaintenanceState.OFF).atLeastOnce();
        EasyMock.expect(current2.getLatestTimestamp()).andReturn(java.lang.System.currentTimeMillis()).atLeastOnce();
        EasyMock.replay(current1, history1, current2, history2);
        m_currentAlerts.add(current1);
        m_currentAlerts.add(current2);
        junit.framework.Assert.assertEquals(0, m_listener.getAlertEventReceivedCount(org.apache.ambari.server.events.AlertReceivedEvent.class));
        org.apache.ambari.server.alerts.StaleAlertRunnable runnable = new org.apache.ambari.server.alerts.StaleAlertRunnable(m_definition.getDefinitionName());
        m_injector.injectMembers(runnable);
        runnable.run();
        checkSingleEventToState(org.apache.ambari.server.state.AlertState.OK);
        EasyMock.verify(m_cluster, m_clusters, m_definitionDao, m_alertHelper);
    }

    private void checkSingleEventToState(org.apache.ambari.server.state.AlertState alertState) {
        junit.framework.Assert.assertEquals(1, m_listener.getAlertEventReceivedCount(org.apache.ambari.server.events.AlertReceivedEvent.class));
        java.util.List<org.apache.ambari.server.events.AlertEvent> events = m_listener.getAlertEventInstances(org.apache.ambari.server.events.AlertReceivedEvent.class);
        junit.framework.Assert.assertEquals(1, events.size());
        org.apache.ambari.server.events.AlertReceivedEvent event = ((org.apache.ambari.server.events.AlertReceivedEvent) (events.get(0)));
        org.apache.ambari.server.state.Alert alert = event.getAlert();
        junit.framework.Assert.assertEquals("AMBARI", alert.getService());
        junit.framework.Assert.assertEquals("AMBARI_SERVER", alert.getComponent());
        junit.framework.Assert.assertEquals(alertState, alert.getState());
        junit.framework.Assert.assertEquals(org.apache.ambari.server.alerts.StaleAlertRunnableTest.DEFINITION_NAME, alert.getName());
    }

    private class MockModule implements com.google.inject.Module {
        @java.lang.Override
        public void configure(com.google.inject.Binder binder) {
            org.apache.ambari.server.testutils.PartialNiceMockBinder.newBuilder().addConfigsBindings().addAlertDefinitionBinding().addLdapBindings().build().configure(binder);
            binder.bind(org.apache.ambari.server.orm.dao.AlertsDAO.class).toInstance(EasyMock.createNiceMock(org.apache.ambari.server.orm.dao.AlertsDAO.class));
            binder.bind(org.apache.ambari.server.orm.dao.HostRoleCommandDAO.class).toInstance(EasyMock.createNiceMock(org.apache.ambari.server.orm.dao.HostRoleCommandDAO.class));
            binder.bind(org.apache.ambari.server.state.alert.AlertHelper.class).toInstance(EasyMock.createNiceMock(org.apache.ambari.server.state.alert.AlertHelper.class));
        }
    }
}