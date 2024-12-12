package org.apache.ambari.server.state.services;
import org.easymock.EasyMock;
import org.easymock.EasyMockSupport;
import org.mockito.internal.verification.Times;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import static org.easymock.EasyMock.expect;
@org.junit.runner.RunWith(org.powermock.modules.junit4.PowerMockRunner.class)
@org.powermock.core.classloader.annotations.PrepareForTest(org.apache.ambari.server.state.services.CachedAlertFlushService.class)
public class CachedAlertFlushServiceTest extends org.easymock.EasyMockSupport {
    private com.google.inject.Injector m_injector;

    @org.junit.Before
    public void before() {
        m_injector = com.google.inject.Guice.createInjector(new org.apache.ambari.server.state.services.CachedAlertFlushServiceTest.MockModule());
        org.apache.ambari.server.configuration.Configuration configuration = m_injector.getInstance(org.apache.ambari.server.configuration.Configuration.class);
        org.easymock.EasyMock.reset(configuration);
    }

    @org.junit.Test
    public void testServiceIsDisabled() throws java.lang.Exception {
        org.apache.ambari.server.configuration.Configuration configuration = m_injector.getInstance(org.apache.ambari.server.configuration.Configuration.class);
        org.easymock.EasyMock.expect(configuration.isAlertCacheEnabled()).andReturn(java.lang.Boolean.FALSE).atLeastOnce();
        org.apache.ambari.server.state.services.CachedAlertFlushService service = org.powermock.api.mockito.PowerMockito.spy(new org.apache.ambari.server.state.services.CachedAlertFlushService());
        org.powermock.api.mockito.PowerMockito.doReturn(null).when(service).stopAsync();
        replayAll();
        m_injector.injectMembers(service);
        service.startUp();
        org.powermock.api.mockito.PowerMockito.verifyPrivate(service).invoke("stopAsync");
        verifyAll();
    }

    @org.junit.Test
    public void testServiceIsEnabled() throws java.lang.Exception {
        org.apache.ambari.server.configuration.Configuration configuration = m_injector.getInstance(org.apache.ambari.server.configuration.Configuration.class);
        org.easymock.EasyMock.expect(configuration.isAlertCacheEnabled()).andReturn(java.lang.Boolean.TRUE).atLeastOnce();
        org.apache.ambari.server.orm.dao.AlertsDAO alertsDAO = m_injector.getInstance(org.apache.ambari.server.orm.dao.AlertsDAO.class);
        alertsDAO.flushCachedEntitiesToJPA();
        org.easymock.EasyMock.expectLastCall().once();
        org.apache.ambari.server.state.services.CachedAlertFlushService service = org.powermock.api.mockito.PowerMockito.spy(new org.apache.ambari.server.state.services.CachedAlertFlushService());
        org.powermock.api.mockito.PowerMockito.doReturn(null).when(service).stopAsync();
        replayAll();
        m_injector.injectMembers(service);
        service.startUp();
        service.runOneIteration();
        org.powermock.api.mockito.PowerMockito.verifyPrivate(service, new org.mockito.internal.verification.Times(0)).invoke("stopAsync");
        verifyAll();
    }

    private class MockModule implements com.google.inject.Module {
        @java.lang.Override
        public void configure(com.google.inject.Binder binder) {
            org.apache.ambari.server.state.Cluster cluster = org.easymock.EasyMock.createNiceMock(org.apache.ambari.server.state.Cluster.class);
            org.apache.ambari.server.configuration.Configuration configuration = createNiceMock(org.apache.ambari.server.configuration.Configuration.class);
            EasyMock.expect(configuration.getAlertEventPublisherCorePoolSize()).andReturn(java.lang.Integer.valueOf(org.apache.ambari.server.configuration.Configuration.ALERTS_EXECUTION_SCHEDULER_THREADS_CORE_SIZE.getDefaultValue())).anyTimes();
            EasyMock.expect(configuration.getAlertEventPublisherMaxPoolSize()).andReturn(java.lang.Integer.valueOf(org.apache.ambari.server.configuration.Configuration.ALERTS_EXECUTION_SCHEDULER_THREADS_MAX_SIZE.getDefaultValue())).anyTimes();
            EasyMock.expect(configuration.getAlertEventPublisherWorkerQueueSize()).andReturn(java.lang.Integer.valueOf(org.apache.ambari.server.configuration.Configuration.ALERTS_EXECUTION_SCHEDULER_WORKER_QUEUE_SIZE.getDefaultValue())).anyTimes();
            org.easymock.EasyMock.replay(configuration);
            org.apache.ambari.server.testutils.PartialNiceMockBinder.newBuilder().addDBAccessorBinding().addAlertDefinitionDAOBinding().addLdapBindings().build().configure(binder);
            binder.bind(org.apache.ambari.server.configuration.Configuration.class).toInstance(configuration);
            binder.bind(org.apache.ambari.server.state.Cluster.class).toInstance(cluster);
            binder.bind(org.apache.ambari.server.orm.dao.AlertDefinitionDAO.class).toInstance(createNiceMock(org.apache.ambari.server.orm.dao.AlertDefinitionDAO.class));
            binder.bind(org.apache.ambari.server.orm.dao.AlertsDAO.class).toInstance(createNiceMock(org.apache.ambari.server.orm.dao.AlertsDAO.class));
        }
    }
}