package org.apache.ambari.server.state.alerts;
import org.easymock.EasyMock;
import org.easymock.EasyMockSupport;
@org.junit.experimental.categories.Category({ category.AlertTest.class })
public class AlertStateChangedEventTest extends org.easymock.EasyMockSupport {
    private org.apache.ambari.server.events.publishers.AlertEventPublisher eventPublisher;

    private org.apache.ambari.server.orm.dao.AlertDispatchDAO dispatchDao;

    private com.google.inject.Injector injector;

    private org.apache.ambari.server.events.MockEventListener m_listener;

    @org.junit.Before
    public void setup() throws java.lang.Exception {
        injector = com.google.inject.Guice.createInjector(com.google.inject.util.Modules.override(new org.apache.ambari.server.orm.InMemoryDefaultTestModule()).with(new org.apache.ambari.server.state.alerts.AlertStateChangedEventTest.MockModule()));
        injector.getInstance(org.apache.ambari.server.orm.GuiceJpaInitializer.class);
        m_listener = injector.getInstance(org.apache.ambari.server.events.MockEventListener.class);
        dispatchDao = injector.getInstance(org.apache.ambari.server.orm.dao.AlertDispatchDAO.class);
        org.apache.ambari.server.utils.EventBusSynchronizer.synchronizeAlertEventPublisher(injector).register(m_listener);
        org.apache.ambari.server.utils.EventBusSynchronizer.synchronizeAmbariEventPublisher(injector).register(m_listener);
        eventPublisher = injector.getInstance(org.apache.ambari.server.events.publishers.AlertEventPublisher.class);
    }

    @org.junit.After
    public void teardown() throws java.lang.Exception {
        org.apache.ambari.server.H2DatabaseCleaner.clearDatabaseAndStopPersistenceService(injector);
        injector = null;
    }

    @org.junit.Test
    @java.lang.SuppressWarnings("unchecked")
    public void testAlertNoticeCreationFromEvent() throws java.lang.Exception {
        expectNormalCluster();
        org.apache.ambari.server.orm.entities.AlertTargetEntity alertTarget = createNiceMock(org.apache.ambari.server.orm.entities.AlertTargetEntity.class);
        org.apache.ambari.server.orm.entities.AlertGroupEntity alertGroup = createMock(org.apache.ambari.server.orm.entities.AlertGroupEntity.class);
        java.util.List<org.apache.ambari.server.orm.entities.AlertGroupEntity> groups = new java.util.ArrayList<>();
        java.util.Set<org.apache.ambari.server.orm.entities.AlertTargetEntity> targets = new java.util.HashSet<>();
        targets.add(alertTarget);
        groups.add(alertGroup);
        org.easymock.EasyMock.expect(alertGroup.getAlertTargets()).andReturn(targets).once();
        org.easymock.EasyMock.expect(alertTarget.isEnabled()).andReturn(java.lang.Boolean.TRUE).atLeastOnce();
        org.easymock.EasyMock.expect(alertTarget.getAlertStates()).andReturn(java.util.EnumSet.of(org.apache.ambari.server.state.AlertState.OK, org.apache.ambari.server.state.AlertState.CRITICAL)).atLeastOnce();
        org.easymock.EasyMock.expect(dispatchDao.findGroupsByDefinition(org.easymock.EasyMock.anyObject(org.apache.ambari.server.orm.entities.AlertDefinitionEntity.class))).andReturn(groups).once();
        org.easymock.EasyMock.expect(dispatchDao.createNotices(((java.util.List<org.apache.ambari.server.orm.entities.AlertNoticeEntity>) (org.easymock.EasyMock.anyObject())))).andReturn(new java.util.ArrayList<>()).once();
        org.apache.ambari.server.orm.entities.AlertDefinitionEntity definition = getMockAlertDefinition();
        org.apache.ambari.server.orm.entities.AlertCurrentEntity current = getMockedAlertCurrentEntity();
        org.apache.ambari.server.orm.entities.AlertHistoryEntity history = createNiceMock(org.apache.ambari.server.orm.entities.AlertHistoryEntity.class);
        org.apache.ambari.server.events.AlertStateChangeEvent event = createNiceMock(org.apache.ambari.server.events.AlertStateChangeEvent.class);
        org.apache.ambari.server.state.Alert alert = createNiceMock(org.apache.ambari.server.state.Alert.class);
        org.easymock.EasyMock.expect(current.getAlertHistory()).andReturn(history).anyTimes();
        org.easymock.EasyMock.expect(current.getFirmness()).andReturn(org.apache.ambari.server.state.AlertFirmness.HARD).atLeastOnce();
        org.easymock.EasyMock.expect(history.getClusterId()).andReturn(1L).atLeastOnce();
        org.easymock.EasyMock.expect(history.getAlertState()).andReturn(org.apache.ambari.server.state.AlertState.CRITICAL).atLeastOnce();
        org.easymock.EasyMock.expect(history.getAlertDefinition()).andReturn(definition).atLeastOnce();
        org.easymock.EasyMock.expect(alert.getText()).andReturn("The HDFS Foo Alert Is Not Good").atLeastOnce();
        org.easymock.EasyMock.expect(alert.getState()).andReturn(org.apache.ambari.server.state.AlertState.CRITICAL).atLeastOnce();
        org.easymock.EasyMock.expect(event.getCurrentAlert()).andReturn(current).atLeastOnce();
        org.easymock.EasyMock.expect(event.getNewHistoricalEntry()).andReturn(history).atLeastOnce();
        org.easymock.EasyMock.expect(event.getAlert()).andReturn(alert).atLeastOnce();
        replayAll();
        eventPublisher.publish(event);
        verifyAll();
    }

    @org.junit.Test
    public void testAlertNoticeSkippedForTarget() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.AlertTargetEntity alertTarget = createMock(org.apache.ambari.server.orm.entities.AlertTargetEntity.class);
        org.apache.ambari.server.orm.entities.AlertGroupEntity alertGroup = createMock(org.apache.ambari.server.orm.entities.AlertGroupEntity.class);
        java.util.List<org.apache.ambari.server.orm.entities.AlertGroupEntity> groups = new java.util.ArrayList<>();
        java.util.Set<org.apache.ambari.server.orm.entities.AlertTargetEntity> targets = new java.util.HashSet<>();
        targets.add(alertTarget);
        groups.add(alertGroup);
        org.easymock.EasyMock.expect(alertGroup.getAlertTargets()).andReturn(targets).once();
        org.easymock.EasyMock.expect(alertTarget.isEnabled()).andReturn(java.lang.Boolean.TRUE).atLeastOnce();
        org.easymock.EasyMock.expect(alertTarget.getAlertStates()).andReturn(java.util.EnumSet.of(org.apache.ambari.server.state.AlertState.OK, org.apache.ambari.server.state.AlertState.CRITICAL)).atLeastOnce();
        org.easymock.EasyMock.expect(dispatchDao.findGroupsByDefinition(org.easymock.EasyMock.anyObject(org.apache.ambari.server.orm.entities.AlertDefinitionEntity.class))).andReturn(groups).once();
        org.apache.ambari.server.orm.entities.AlertDefinitionEntity definition = getMockAlertDefinition();
        org.apache.ambari.server.orm.entities.AlertCurrentEntity current = getMockedAlertCurrentEntity();
        org.apache.ambari.server.orm.entities.AlertHistoryEntity history = createNiceMock(org.apache.ambari.server.orm.entities.AlertHistoryEntity.class);
        org.apache.ambari.server.events.AlertStateChangeEvent event = createNiceMock(org.apache.ambari.server.events.AlertStateChangeEvent.class);
        org.apache.ambari.server.state.Alert alert = createNiceMock(org.apache.ambari.server.state.Alert.class);
        org.easymock.EasyMock.expect(current.getAlertHistory()).andReturn(history).anyTimes();
        org.easymock.EasyMock.expect(current.getFirmness()).andReturn(org.apache.ambari.server.state.AlertFirmness.HARD).atLeastOnce();
        org.easymock.EasyMock.expect(history.getAlertState()).andReturn(org.apache.ambari.server.state.AlertState.WARNING).atLeastOnce();
        org.easymock.EasyMock.expect(history.getAlertDefinition()).andReturn(definition).atLeastOnce();
        org.easymock.EasyMock.expect(alert.getText()).andReturn("The HDFS Foo Alert Is Not Good").atLeastOnce();
        org.easymock.EasyMock.expect(alert.getState()).andReturn(org.apache.ambari.server.state.AlertState.WARNING).atLeastOnce();
        org.easymock.EasyMock.expect(event.getCurrentAlert()).andReturn(current).atLeastOnce();
        org.easymock.EasyMock.expect(event.getNewHistoricalEntry()).andReturn(history).atLeastOnce();
        org.easymock.EasyMock.expect(event.getAlert()).andReturn(alert).atLeastOnce();
        replayAll();
        eventPublisher.publish(event);
        verifyAll();
    }

    @org.junit.Test
    public void testAlertNoticeSkippedForDisabledTarget() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.AlertTargetEntity alertTarget = createMock(org.apache.ambari.server.orm.entities.AlertTargetEntity.class);
        org.apache.ambari.server.orm.entities.AlertGroupEntity alertGroup = createMock(org.apache.ambari.server.orm.entities.AlertGroupEntity.class);
        java.util.List<org.apache.ambari.server.orm.entities.AlertGroupEntity> groups = new java.util.ArrayList<>();
        java.util.Set<org.apache.ambari.server.orm.entities.AlertTargetEntity> targets = new java.util.HashSet<>();
        targets.add(alertTarget);
        groups.add(alertGroup);
        org.easymock.EasyMock.expect(alertGroup.getAlertTargets()).andReturn(targets).once();
        org.easymock.EasyMock.expect(alertTarget.isEnabled()).andReturn(java.lang.Boolean.FALSE).atLeastOnce();
        org.easymock.EasyMock.expect(dispatchDao.findGroupsByDefinition(org.easymock.EasyMock.anyObject(org.apache.ambari.server.orm.entities.AlertDefinitionEntity.class))).andReturn(groups).once();
        org.apache.ambari.server.orm.entities.AlertDefinitionEntity definition = getMockAlertDefinition();
        org.apache.ambari.server.orm.entities.AlertCurrentEntity current = getMockedAlertCurrentEntity();
        org.apache.ambari.server.orm.entities.AlertHistoryEntity history = createNiceMock(org.apache.ambari.server.orm.entities.AlertHistoryEntity.class);
        org.apache.ambari.server.events.AlertStateChangeEvent event = createNiceMock(org.apache.ambari.server.events.AlertStateChangeEvent.class);
        org.apache.ambari.server.state.Alert alert = createNiceMock(org.apache.ambari.server.state.Alert.class);
        org.easymock.EasyMock.expect(current.getAlertHistory()).andReturn(history).anyTimes();
        org.easymock.EasyMock.expect(current.getFirmness()).andReturn(org.apache.ambari.server.state.AlertFirmness.HARD).atLeastOnce();
        org.easymock.EasyMock.expect(history.getAlertState()).andReturn(org.apache.ambari.server.state.AlertState.OK).atLeastOnce();
        org.easymock.EasyMock.expect(history.getAlertDefinition()).andReturn(definition).atLeastOnce();
        org.easymock.EasyMock.expect(alert.getText()).andReturn("The HDFS Foo Alert Is Not Good").atLeastOnce();
        org.easymock.EasyMock.expect(alert.getState()).andReturn(org.apache.ambari.server.state.AlertState.WARNING).atLeastOnce();
        org.easymock.EasyMock.expect(event.getCurrentAlert()).andReturn(current).atLeastOnce();
        org.easymock.EasyMock.expect(event.getNewHistoricalEntry()).andReturn(history).atLeastOnce();
        org.easymock.EasyMock.expect(event.getAlert()).andReturn(alert).atLeastOnce();
        replayAll();
        eventPublisher.publish(event);
        verifyAll();
    }

    @org.junit.Test
    public void testSoftAlertDoesNotCreateNotifications() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.AlertDefinitionEntity definition = getMockAlertDefinition();
        org.apache.ambari.server.orm.entities.AlertCurrentEntity current = getMockedAlertCurrentEntity();
        org.apache.ambari.server.orm.entities.AlertHistoryEntity history = createNiceMock(org.apache.ambari.server.orm.entities.AlertHistoryEntity.class);
        org.apache.ambari.server.events.AlertStateChangeEvent event = createNiceMock(org.apache.ambari.server.events.AlertStateChangeEvent.class);
        org.apache.ambari.server.state.Alert alert = createNiceMock(org.apache.ambari.server.state.Alert.class);
        org.easymock.EasyMock.expect(current.getAlertHistory()).andReturn(history).anyTimes();
        org.easymock.EasyMock.expect(current.getFirmness()).andReturn(org.apache.ambari.server.state.AlertFirmness.SOFT).atLeastOnce();
        org.easymock.EasyMock.expect(history.getAlertDefinition()).andReturn(definition).atLeastOnce();
        org.easymock.EasyMock.expect(alert.getText()).andReturn("The HDFS Foo Alert Is Not Good").atLeastOnce();
        org.easymock.EasyMock.expect(alert.getState()).andReturn(org.apache.ambari.server.state.AlertState.CRITICAL).atLeastOnce();
        org.easymock.EasyMock.expect(event.getCurrentAlert()).andReturn(current).atLeastOnce();
        org.easymock.EasyMock.expect(event.getNewHistoricalEntry()).andReturn(history).atLeastOnce();
        org.easymock.EasyMock.expect(event.getAlert()).andReturn(alert).atLeastOnce();
        replayAll();
        eventPublisher.publish(event);
        verifyAll();
    }

    @org.junit.Test
    public void testSoftAlertTransitionToHardOKDoesNotCreateNotification() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.AlertDefinitionEntity definition = getMockAlertDefinition();
        org.apache.ambari.server.orm.entities.AlertCurrentEntity current = getMockedAlertCurrentEntity();
        org.apache.ambari.server.orm.entities.AlertHistoryEntity history = createNiceMock(org.apache.ambari.server.orm.entities.AlertHistoryEntity.class);
        org.apache.ambari.server.events.AlertStateChangeEvent event = createNiceMock(org.apache.ambari.server.events.AlertStateChangeEvent.class);
        org.apache.ambari.server.state.Alert alert = createNiceMock(org.apache.ambari.server.state.Alert.class);
        org.easymock.EasyMock.expect(current.getAlertHistory()).andReturn(history).anyTimes();
        org.easymock.EasyMock.expect(current.getFirmness()).andReturn(org.apache.ambari.server.state.AlertFirmness.HARD).atLeastOnce();
        org.easymock.EasyMock.expect(history.getAlertDefinition()).andReturn(definition).atLeastOnce();
        org.easymock.EasyMock.expect(history.getAlertState()).andReturn(org.apache.ambari.server.state.AlertState.OK).atLeastOnce();
        org.easymock.EasyMock.expect(alert.getText()).andReturn("The HDFS Foo Alert Is Good").atLeastOnce();
        org.easymock.EasyMock.expect(alert.getState()).andReturn(org.apache.ambari.server.state.AlertState.OK).atLeastOnce();
        org.easymock.EasyMock.expect(event.getFromState()).andReturn(org.apache.ambari.server.state.AlertState.CRITICAL).anyTimes();
        org.easymock.EasyMock.expect(event.getFromFirmness()).andReturn(org.apache.ambari.server.state.AlertFirmness.SOFT).atLeastOnce();
        org.easymock.EasyMock.expect(event.getCurrentAlert()).andReturn(current).atLeastOnce();
        org.easymock.EasyMock.expect(event.getNewHistoricalEntry()).andReturn(history).atLeastOnce();
        org.easymock.EasyMock.expect(event.getAlert()).andReturn(alert).atLeastOnce();
        replayAll();
        eventPublisher.publish(event);
        verifyAll();
    }

    private org.apache.ambari.server.orm.entities.AlertDefinitionEntity getMockAlertDefinition() {
        org.apache.ambari.server.orm.entities.AlertDefinitionEntity definition = createNiceMock(org.apache.ambari.server.orm.entities.AlertDefinitionEntity.class);
        org.easymock.EasyMock.expect(definition.getDefinitionId()).andReturn(1L).anyTimes();
        org.easymock.EasyMock.expect(definition.getServiceName()).andReturn("HDFS").anyTimes();
        org.easymock.EasyMock.expect(definition.getLabel()).andReturn("hdfs-foo-alert").anyTimes();
        org.easymock.EasyMock.expect(definition.getDescription()).andReturn("HDFS Foo Alert").anyTimes();
        return definition;
    }

    private org.apache.ambari.server.orm.entities.AlertCurrentEntity getMockedAlertCurrentEntity() {
        org.apache.ambari.server.orm.entities.AlertCurrentEntity current = createNiceMock(org.apache.ambari.server.orm.entities.AlertCurrentEntity.class);
        org.easymock.EasyMock.expect(current.getMaintenanceState()).andReturn(org.apache.ambari.server.state.MaintenanceState.OFF).anyTimes();
        return current;
    }

    @org.junit.Test
    public void testAggregateAlertRecalculateEvent() throws java.lang.Exception {
        java.lang.Class<? extends org.apache.ambari.server.events.AlertEvent> eventClass = org.apache.ambari.server.events.AggregateAlertRecalculateEvent.class;
        junit.framework.Assert.assertFalse(m_listener.isAlertEventReceived(eventClass));
        org.apache.ambari.server.orm.dao.AlertsDAO dao = injector.getInstance(org.apache.ambari.server.orm.dao.AlertsDAO.class);
        dao.removeCurrentByServiceComponentHost(1, "HDFS", "DATANODE", "c6401");
        junit.framework.Assert.assertTrue(m_listener.isAlertEventReceived(eventClass));
        junit.framework.Assert.assertEquals(1, m_listener.getAlertEventReceivedCount(eventClass));
    }

    @org.junit.Test
    public void testUpgradingClusterSkipsAlerts() throws java.lang.Exception {
        expectUpgradingCluster();
        org.apache.ambari.server.orm.entities.AlertTargetEntity alertTarget = createNiceMock(org.apache.ambari.server.orm.entities.AlertTargetEntity.class);
        org.apache.ambari.server.orm.entities.AlertGroupEntity alertGroup = createMock(org.apache.ambari.server.orm.entities.AlertGroupEntity.class);
        java.util.List<org.apache.ambari.server.orm.entities.AlertGroupEntity> groups = new java.util.ArrayList<>();
        java.util.Set<org.apache.ambari.server.orm.entities.AlertTargetEntity> targets = new java.util.HashSet<>();
        targets.add(alertTarget);
        groups.add(alertGroup);
        org.easymock.EasyMock.expect(alertGroup.getAlertTargets()).andReturn(targets).once();
        org.easymock.EasyMock.expect(alertTarget.isEnabled()).andReturn(java.lang.Boolean.TRUE).atLeastOnce();
        org.easymock.EasyMock.expect(alertTarget.getAlertStates()).andReturn(java.util.EnumSet.allOf(org.apache.ambari.server.state.AlertState.class)).atLeastOnce();
        org.easymock.EasyMock.expect(dispatchDao.findGroupsByDefinition(org.easymock.EasyMock.anyObject(org.apache.ambari.server.orm.entities.AlertDefinitionEntity.class))).andReturn(groups).once();
        org.apache.ambari.server.orm.entities.AlertDefinitionEntity definition = getMockAlertDefinition();
        org.apache.ambari.server.orm.entities.AlertCurrentEntity current = getMockedAlertCurrentEntity();
        org.apache.ambari.server.orm.entities.AlertHistoryEntity history = createNiceMock(org.apache.ambari.server.orm.entities.AlertHistoryEntity.class);
        org.apache.ambari.server.events.AlertStateChangeEvent event = createNiceMock(org.apache.ambari.server.events.AlertStateChangeEvent.class);
        org.apache.ambari.server.state.Alert alert = createNiceMock(org.apache.ambari.server.state.Alert.class);
        org.easymock.EasyMock.expect(current.getAlertHistory()).andReturn(history).anyTimes();
        org.easymock.EasyMock.expect(current.getFirmness()).andReturn(org.apache.ambari.server.state.AlertFirmness.HARD).atLeastOnce();
        org.easymock.EasyMock.expect(history.getClusterId()).andReturn(1L).atLeastOnce();
        org.easymock.EasyMock.expect(history.getAlertState()).andReturn(org.apache.ambari.server.state.AlertState.CRITICAL).atLeastOnce();
        org.easymock.EasyMock.expect(history.getAlertDefinition()).andReturn(definition).atLeastOnce();
        org.easymock.EasyMock.expect(alert.getText()).andReturn("The HDFS Foo Alert Is Not Good").atLeastOnce();
        org.easymock.EasyMock.expect(alert.getState()).andReturn(org.apache.ambari.server.state.AlertState.CRITICAL).atLeastOnce();
        org.easymock.EasyMock.expect(event.getCurrentAlert()).andReturn(current).atLeastOnce();
        org.easymock.EasyMock.expect(event.getNewHistoricalEntry()).andReturn(history).atLeastOnce();
        org.easymock.EasyMock.expect(event.getAlert()).andReturn(alert).atLeastOnce();
        replayAll();
        eventPublisher.publish(event);
        verifyAll();
    }

    @org.junit.Test
    public void testUpgradeSuspendedClusterSkipsAlerts() throws java.lang.Exception {
        expectUpgradeSuspendedCluster();
        org.apache.ambari.server.orm.entities.AlertTargetEntity alertTarget = createNiceMock(org.apache.ambari.server.orm.entities.AlertTargetEntity.class);
        org.apache.ambari.server.orm.entities.AlertGroupEntity alertGroup = createMock(org.apache.ambari.server.orm.entities.AlertGroupEntity.class);
        java.util.List<org.apache.ambari.server.orm.entities.AlertGroupEntity> groups = new java.util.ArrayList<>();
        java.util.Set<org.apache.ambari.server.orm.entities.AlertTargetEntity> targets = new java.util.HashSet<>();
        targets.add(alertTarget);
        groups.add(alertGroup);
        org.easymock.EasyMock.expect(alertGroup.getAlertTargets()).andReturn(targets).once();
        org.easymock.EasyMock.expect(alertTarget.isEnabled()).andReturn(java.lang.Boolean.TRUE).atLeastOnce();
        org.easymock.EasyMock.expect(alertTarget.getAlertStates()).andReturn(java.util.EnumSet.allOf(org.apache.ambari.server.state.AlertState.class)).atLeastOnce();
        org.easymock.EasyMock.expect(dispatchDao.findGroupsByDefinition(org.easymock.EasyMock.anyObject(org.apache.ambari.server.orm.entities.AlertDefinitionEntity.class))).andReturn(groups).once();
        org.apache.ambari.server.orm.entities.AlertDefinitionEntity definition = getMockAlertDefinition();
        org.apache.ambari.server.orm.entities.AlertCurrentEntity current = getMockedAlertCurrentEntity();
        org.apache.ambari.server.orm.entities.AlertHistoryEntity history = createNiceMock(org.apache.ambari.server.orm.entities.AlertHistoryEntity.class);
        org.apache.ambari.server.events.AlertStateChangeEvent event = createNiceMock(org.apache.ambari.server.events.AlertStateChangeEvent.class);
        org.apache.ambari.server.state.Alert alert = createNiceMock(org.apache.ambari.server.state.Alert.class);
        org.easymock.EasyMock.expect(current.getAlertHistory()).andReturn(history).anyTimes();
        org.easymock.EasyMock.expect(current.getFirmness()).andReturn(org.apache.ambari.server.state.AlertFirmness.HARD).atLeastOnce();
        org.easymock.EasyMock.expect(history.getClusterId()).andReturn(1L).atLeastOnce();
        org.easymock.EasyMock.expect(history.getAlertState()).andReturn(org.apache.ambari.server.state.AlertState.CRITICAL).atLeastOnce();
        org.easymock.EasyMock.expect(history.getAlertDefinition()).andReturn(definition).atLeastOnce();
        org.easymock.EasyMock.expect(alert.getText()).andReturn("The HDFS Foo Alert Is Not Good").atLeastOnce();
        org.easymock.EasyMock.expect(alert.getState()).andReturn(org.apache.ambari.server.state.AlertState.CRITICAL).atLeastOnce();
        org.easymock.EasyMock.expect(event.getCurrentAlert()).andReturn(current).atLeastOnce();
        org.easymock.EasyMock.expect(event.getNewHistoricalEntry()).andReturn(history).atLeastOnce();
        org.easymock.EasyMock.expect(event.getAlert()).andReturn(alert).atLeastOnce();
        replayAll();
        eventPublisher.publish(event);
        verifyAll();
    }

    @org.junit.Test
    @java.lang.SuppressWarnings("unchecked")
    public void testAmbariAlertsSendDuringUpgrade() throws java.lang.Exception {
        expectUpgradingCluster();
        org.apache.ambari.server.orm.entities.AlertTargetEntity alertTarget = createNiceMock(org.apache.ambari.server.orm.entities.AlertTargetEntity.class);
        org.apache.ambari.server.orm.entities.AlertGroupEntity alertGroup = createMock(org.apache.ambari.server.orm.entities.AlertGroupEntity.class);
        java.util.List<org.apache.ambari.server.orm.entities.AlertGroupEntity> groups = new java.util.ArrayList<>();
        java.util.Set<org.apache.ambari.server.orm.entities.AlertTargetEntity> targets = new java.util.HashSet<>();
        targets.add(alertTarget);
        groups.add(alertGroup);
        org.easymock.EasyMock.expect(alertGroup.getAlertTargets()).andReturn(targets).once();
        org.easymock.EasyMock.expect(alertTarget.isEnabled()).andReturn(java.lang.Boolean.TRUE).atLeastOnce();
        org.easymock.EasyMock.expect(alertTarget.getAlertStates()).andReturn(java.util.EnumSet.allOf(org.apache.ambari.server.state.AlertState.class)).atLeastOnce();
        org.easymock.EasyMock.expect(dispatchDao.findGroupsByDefinition(org.easymock.EasyMock.anyObject(org.apache.ambari.server.orm.entities.AlertDefinitionEntity.class))).andReturn(groups).once();
        org.apache.ambari.server.orm.entities.AlertDefinitionEntity definition = createNiceMock(org.apache.ambari.server.orm.entities.AlertDefinitionEntity.class);
        org.easymock.EasyMock.expect(definition.getDefinitionId()).andReturn(1L).anyTimes();
        org.easymock.EasyMock.expect(definition.getServiceName()).andReturn(org.apache.ambari.server.controller.RootService.AMBARI.name()).anyTimes();
        org.easymock.EasyMock.expect(definition.getLabel()).andReturn("ambari-foo-alert").anyTimes();
        org.easymock.EasyMock.expect(definition.getDescription()).andReturn("Ambari Foo Alert").anyTimes();
        org.easymock.EasyMock.expect(dispatchDao.createNotices(((java.util.List<org.apache.ambari.server.orm.entities.AlertNoticeEntity>) (org.easymock.EasyMock.anyObject())))).andReturn(new java.util.ArrayList<>()).once();
        org.apache.ambari.server.orm.entities.AlertCurrentEntity current = getMockedAlertCurrentEntity();
        org.apache.ambari.server.orm.entities.AlertHistoryEntity history = createNiceMock(org.apache.ambari.server.orm.entities.AlertHistoryEntity.class);
        org.apache.ambari.server.events.AlertStateChangeEvent event = createNiceMock(org.apache.ambari.server.events.AlertStateChangeEvent.class);
        org.apache.ambari.server.state.Alert alert = createNiceMock(org.apache.ambari.server.state.Alert.class);
        org.easymock.EasyMock.expect(current.getAlertHistory()).andReturn(history).anyTimes();
        org.easymock.EasyMock.expect(current.getFirmness()).andReturn(org.apache.ambari.server.state.AlertFirmness.HARD).atLeastOnce();
        org.easymock.EasyMock.expect(history.getClusterId()).andReturn(1L).atLeastOnce();
        org.easymock.EasyMock.expect(history.getAlertState()).andReturn(org.apache.ambari.server.state.AlertState.CRITICAL).atLeastOnce();
        org.easymock.EasyMock.expect(history.getAlertDefinition()).andReturn(definition).atLeastOnce();
        org.easymock.EasyMock.expect(alert.getText()).andReturn("The HDFS Foo Alert Is Not Good").atLeastOnce();
        org.easymock.EasyMock.expect(alert.getState()).andReturn(org.apache.ambari.server.state.AlertState.CRITICAL).atLeastOnce();
        org.easymock.EasyMock.expect(event.getCurrentAlert()).andReturn(current).atLeastOnce();
        org.easymock.EasyMock.expect(event.getNewHistoricalEntry()).andReturn(history).atLeastOnce();
        org.easymock.EasyMock.expect(event.getAlert()).andReturn(alert).atLeastOnce();
        replayAll();
        eventPublisher.publish(event);
        verifyAll();
    }

    private void expectNormalCluster() throws java.lang.Exception {
        org.apache.ambari.server.state.Clusters clusters = injector.getInstance(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.state.Cluster cluster = createMock(org.apache.ambari.server.state.Cluster.class);
        org.easymock.EasyMock.expect(clusters.getClusterById(org.easymock.EasyMock.anyLong())).andReturn(cluster).atLeastOnce();
        org.easymock.EasyMock.expect(cluster.getUpgradeInProgress()).andReturn(null).anyTimes();
        org.easymock.EasyMock.expect(cluster.isUpgradeSuspended()).andReturn(false).anyTimes();
    }

    private void expectUpgradingCluster() throws java.lang.Exception {
        org.apache.ambari.server.state.Clusters clusters = injector.getInstance(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.state.Cluster cluster = createMock(org.apache.ambari.server.state.Cluster.class);
        org.easymock.EasyMock.reset(clusters);
        org.easymock.EasyMock.expect(clusters.getClusterById(org.easymock.EasyMock.anyLong())).andReturn(cluster).atLeastOnce();
        org.easymock.EasyMock.expect(cluster.getUpgradeInProgress()).andReturn(new org.apache.ambari.server.orm.entities.UpgradeEntity()).anyTimes();
        org.easymock.EasyMock.expect(cluster.isUpgradeSuspended()).andReturn(false).anyTimes();
    }

    private void expectUpgradeSuspendedCluster() throws java.lang.Exception {
        org.apache.ambari.server.state.Cluster cluster = createMock(org.apache.ambari.server.state.Cluster.class);
        org.apache.ambari.server.state.Clusters clusters = injector.getInstance(org.apache.ambari.server.state.Clusters.class);
        org.easymock.EasyMock.reset(clusters);
        org.easymock.EasyMock.expect(clusters.getClusterById(org.easymock.EasyMock.anyLong())).andReturn(cluster).atLeastOnce();
        org.easymock.EasyMock.expect(cluster.getUpgradeInProgress()).andReturn(org.easymock.EasyMock.createMock(org.apache.ambari.server.orm.entities.UpgradeEntity.class)).anyTimes();
        org.easymock.EasyMock.expect(cluster.isUpgradeSuspended()).andReturn(true).anyTimes();
    }

    private class MockModule implements com.google.inject.Module {
        @java.lang.Override
        public void configure(com.google.inject.Binder binder) {
            org.apache.ambari.server.state.Clusters clusters = createMock(org.apache.ambari.server.state.Clusters.class);
            binder.bind(org.apache.ambari.server.orm.dao.AlertDispatchDAO.class).toInstance(createMock(org.apache.ambari.server.orm.dao.AlertDispatchDAO.class));
            binder.bind(org.apache.ambari.server.state.Clusters.class).toInstance(clusters);
        }
    }
}