package org.apache.ambari.server.state.alerts;
import javax.persistence.EntityManager;
import org.easymock.EasyMock;
@org.junit.experimental.categories.Category({ category.AlertTest.class })
public class AggregateAlertListenerTest {
    private com.google.inject.Injector m_injector;

    private org.apache.ambari.server.events.MockEventListener m_listener;

    private org.apache.ambari.server.orm.dao.AlertsDAO m_alertsDao;

    private org.apache.ambari.server.state.alert.AggregateDefinitionMapping m_aggregateMapping;

    @org.junit.Before
    public void setup() throws java.lang.Exception {
        m_injector = com.google.inject.Guice.createInjector(com.google.inject.util.Modules.override(new org.apache.ambari.server.orm.InMemoryDefaultTestModule()).with(new org.apache.ambari.server.state.alerts.AggregateAlertListenerTest.MockModule()));
        m_injector.getInstance(org.apache.ambari.server.orm.GuiceJpaInitializer.class);
        m_listener = m_injector.getInstance(org.apache.ambari.server.events.MockEventListener.class);
        m_alertsDao = m_injector.getInstance(org.apache.ambari.server.orm.dao.AlertsDAO.class);
        org.apache.ambari.server.utils.EventBusSynchronizer.synchronizeAlertEventPublisher(m_injector).register(m_listener);
        org.apache.ambari.server.utils.EventBusSynchronizer.synchronizeAmbariEventPublisher(m_injector).register(m_listener);
    }

    @org.junit.After
    public void teardown() throws java.lang.Exception {
        org.apache.ambari.server.H2DatabaseCleaner.clearDatabase(m_injector.getProvider(javax.persistence.EntityManager.class).get());
        m_injector = null;
    }

    @org.junit.Test
    public void testAlertNoticeCreationFromEvent() throws java.lang.Exception {
        org.apache.ambari.server.state.alert.AlertDefinition aggregateDefinition = getAggregateAlertDefinition();
        org.apache.ambari.server.orm.entities.AlertCurrentEntity currentEntityMock = org.easymock.EasyMock.createNiceMock(org.apache.ambari.server.orm.entities.AlertCurrentEntity.class);
        org.apache.ambari.server.orm.entities.AlertHistoryEntity historyEntityMock = org.easymock.EasyMock.createNiceMock(org.apache.ambari.server.orm.entities.AlertHistoryEntity.class);
        org.easymock.EasyMock.expect(currentEntityMock.getAlertHistory()).andReturn(historyEntityMock).atLeastOnce();
        org.easymock.EasyMock.expect(m_aggregateMapping.getAggregateDefinition(org.easymock.EasyMock.anyLong(), org.easymock.EasyMock.eq("mock-alert"))).andReturn(aggregateDefinition).atLeastOnce();
        org.apache.ambari.server.orm.dao.AlertSummaryDTO summaryDTO = new org.apache.ambari.server.orm.dao.AlertSummaryDTO(5, 0, 0, 0, 0);
        org.easymock.EasyMock.expect(m_alertsDao.findAggregateCounts(org.easymock.EasyMock.anyLong(), org.easymock.EasyMock.eq("mock-aggregate-alert"))).andReturn(summaryDTO).atLeastOnce();
        m_alertsDao.saveEntities(org.easymock.EasyMock.anyObject(), org.easymock.EasyMock.anyObject());
        org.easymock.EasyMock.expectLastCall().anyTimes();
        org.easymock.EasyMock.replay(m_alertsDao, m_aggregateMapping, currentEntityMock);
        junit.framework.Assert.assertEquals(0, m_listener.getAlertEventReceivedCount(org.apache.ambari.server.events.AlertReceivedEvent.class));
        org.apache.ambari.server.state.Alert alert = new org.apache.ambari.server.state.Alert("mock-alert", null, null, null, null, null);
        org.apache.ambari.server.events.listeners.alerts.AlertAggregateListener aggregateListener = m_injector.getInstance(org.apache.ambari.server.events.listeners.alerts.AlertAggregateListener.class);
        org.apache.ambari.server.events.AlertStateChangeEvent event = new org.apache.ambari.server.events.AlertStateChangeEvent(0, alert, currentEntityMock, null, org.apache.ambari.server.state.AlertFirmness.HARD);
        aggregateListener.onAlertStateChangeEvent(event);
        junit.framework.Assert.assertEquals(1, m_listener.getAlertEventReceivedCount(org.apache.ambari.server.events.AlertReceivedEvent.class));
        aggregateListener.onAlertStateChangeEvent(event);
        junit.framework.Assert.assertEquals(1, m_listener.getAlertEventReceivedCount(org.apache.ambari.server.events.AlertReceivedEvent.class));
        summaryDTO.setOkCount(0);
        summaryDTO.setCriticalCount(5);
        aggregateListener.onAlertStateChangeEvent(event);
        junit.framework.Assert.assertEquals(2, m_listener.getAlertEventReceivedCount(org.apache.ambari.server.events.AlertReceivedEvent.class));
    }

    @org.junit.Test
    public void testNoAggregateCalculationOnSoftAlert() throws java.lang.Exception {
        org.apache.ambari.server.state.alert.AlertDefinition aggregateDefinition = getAggregateAlertDefinition();
        org.apache.ambari.server.orm.entities.AlertCurrentEntity currentEntityMock = org.easymock.EasyMock.createNiceMock(org.apache.ambari.server.orm.entities.AlertCurrentEntity.class);
        org.apache.ambari.server.orm.entities.AlertHistoryEntity historyEntityMock = org.easymock.EasyMock.createNiceMock(org.apache.ambari.server.orm.entities.AlertHistoryEntity.class);
        org.easymock.EasyMock.expect(currentEntityMock.getAlertHistory()).andReturn(historyEntityMock).atLeastOnce();
        org.easymock.EasyMock.expect(currentEntityMock.getFirmness()).andReturn(org.apache.ambari.server.state.AlertFirmness.SOFT).atLeastOnce();
        org.easymock.EasyMock.expect(m_aggregateMapping.getAggregateDefinition(org.easymock.EasyMock.anyLong(), org.easymock.EasyMock.eq("mock-alert"))).andReturn(aggregateDefinition).atLeastOnce();
        org.apache.ambari.server.orm.dao.AlertSummaryDTO summaryDTO = new org.apache.ambari.server.orm.dao.AlertSummaryDTO(5, 0, 0, 0, 0);
        org.easymock.EasyMock.expect(m_alertsDao.findAggregateCounts(org.easymock.EasyMock.anyLong(), org.easymock.EasyMock.eq("mock-aggregate-alert"))).andReturn(summaryDTO).atLeastOnce();
        org.easymock.EasyMock.replay(m_alertsDao, m_aggregateMapping, currentEntityMock);
        junit.framework.Assert.assertEquals(0, m_listener.getAlertEventReceivedCount(org.apache.ambari.server.events.AlertReceivedEvent.class));
        org.apache.ambari.server.state.Alert alert = new org.apache.ambari.server.state.Alert("mock-alert", null, null, null, null, null);
        org.apache.ambari.server.events.listeners.alerts.AlertAggregateListener aggregateListener = m_injector.getInstance(org.apache.ambari.server.events.listeners.alerts.AlertAggregateListener.class);
        org.apache.ambari.server.events.AlertStateChangeEvent event = new org.apache.ambari.server.events.AlertStateChangeEvent(0, alert, currentEntityMock, null, org.apache.ambari.server.state.AlertFirmness.HARD);
        aggregateListener.onAlertStateChangeEvent(event);
        junit.framework.Assert.assertEquals(0, m_listener.getAlertEventReceivedCount(org.apache.ambari.server.events.AlertReceivedEvent.class));
    }

    private org.apache.ambari.server.state.alert.AlertDefinition getAggregateAlertDefinition() {
        org.apache.ambari.server.state.alert.AlertDefinition aggregateDefinition = new org.apache.ambari.server.state.alert.AlertDefinition();
        aggregateDefinition.setName("mock-aggregate-alert");
        org.apache.ambari.server.state.alert.AggregateSource aggregateSource = new org.apache.ambari.server.state.alert.AggregateSource();
        aggregateSource.setAlertName("mock-aggregate-alert");
        org.apache.ambari.server.state.alert.Reporting reporting = new org.apache.ambari.server.state.alert.Reporting();
        org.apache.ambari.server.state.alert.Reporting.ReportTemplate criticalTemplate = new org.apache.ambari.server.state.alert.Reporting.ReportTemplate();
        org.apache.ambari.server.state.alert.Reporting.ReportTemplate okTemplate = new org.apache.ambari.server.state.alert.Reporting.ReportTemplate();
        criticalTemplate.setValue(0.05);
        criticalTemplate.setText("CRITICAL");
        okTemplate.setText("OK");
        reporting.setCritical(criticalTemplate);
        reporting.setWarning(criticalTemplate);
        reporting.setOk(okTemplate);
        aggregateSource.setReporting(reporting);
        aggregateDefinition.setSource(aggregateSource);
        return aggregateDefinition;
    }

    private class MockModule implements com.google.inject.Module {
        @java.lang.Override
        public void configure(com.google.inject.Binder binder) {
            m_alertsDao = org.easymock.EasyMock.createMock(org.apache.ambari.server.orm.dao.AlertsDAO.class);
            m_aggregateMapping = org.easymock.EasyMock.createMock(org.apache.ambari.server.state.alert.AggregateDefinitionMapping.class);
            binder.bind(org.apache.ambari.server.orm.dao.AlertsDAO.class).toInstance(m_alertsDao);
            binder.bind(org.apache.ambari.server.state.alert.AggregateDefinitionMapping.class).toInstance(m_aggregateMapping);
        }
    }
}