package org.apache.ambari.server.state.alerts;
import javax.persistence.EntityManager;
@org.junit.experimental.categories.Category({ category.AlertTest.class })
public class InitialAlertEventTest {
    private org.apache.ambari.server.orm.dao.AlertsDAO m_alertsDao;

    private org.apache.ambari.server.events.publishers.AlertEventPublisher m_eventPublisher;

    private com.google.inject.Injector m_injector;

    private org.apache.ambari.server.events.MockEventListener m_listener;

    private org.apache.ambari.server.orm.dao.AlertDefinitionDAO m_definitionDao;

    private org.apache.ambari.server.state.Clusters m_clusters;

    private org.apache.ambari.server.state.Cluster m_cluster;

    private java.lang.String m_clusterName;

    private org.apache.ambari.server.state.ServiceFactory m_serviceFactory;

    private org.apache.ambari.server.orm.OrmTestHelper m_helper;

    private final java.lang.String STACK_VERSION = "2.0.6";

    private final java.lang.String REPO_VERSION = "2.0.6-1234";

    private final org.apache.ambari.server.state.StackId STACK_ID = new org.apache.ambari.server.state.StackId("HDP", STACK_VERSION);

    private org.apache.ambari.server.orm.entities.RepositoryVersionEntity m_repositoryVersion;

    @org.junit.Before
    public void setup() throws java.lang.Exception {
        m_injector = com.google.inject.Guice.createInjector(com.google.inject.util.Modules.override(new org.apache.ambari.server.orm.InMemoryDefaultTestModule()).with(new org.apache.ambari.server.state.alerts.InitialAlertEventTest.MockModule()));
        m_injector.getInstance(org.apache.ambari.server.orm.GuiceJpaInitializer.class);
        m_listener = m_injector.getInstance(org.apache.ambari.server.events.MockEventListener.class);
        m_eventPublisher = m_injector.getInstance(org.apache.ambari.server.events.publishers.AlertEventPublisher.class);
        com.google.common.eventbus.EventBus synchronizedBus = org.apache.ambari.server.utils.EventBusSynchronizer.synchronizeAlertEventPublisher(m_injector);
        synchronizedBus.register(m_listener);
        m_definitionDao = m_injector.getInstance(org.apache.ambari.server.orm.dao.AlertDefinitionDAO.class);
        m_clusters = m_injector.getInstance(org.apache.ambari.server.state.Clusters.class);
        m_serviceFactory = m_injector.getInstance(org.apache.ambari.server.state.ServiceFactory.class);
        m_alertsDao = m_injector.getInstance(org.apache.ambari.server.orm.dao.AlertsDAO.class);
        m_helper = m_injector.getInstance(org.apache.ambari.server.orm.OrmTestHelper.class);
        m_repositoryVersion = m_helper.getOrCreateRepositoryVersion(STACK_ID, REPO_VERSION);
        m_clusterName = "c1";
        m_clusters.addCluster(m_clusterName, STACK_ID);
        m_cluster = m_clusters.getCluster(m_clusterName);
        junit.framework.Assert.assertNotNull(m_cluster);
        installHdfsService();
        junit.framework.Assert.assertEquals(1, m_cluster.getServices().size());
        junit.framework.Assert.assertEquals(6, m_definitionDao.findAll().size());
    }

    @org.junit.After
    public void teardown() throws java.lang.Exception {
        org.apache.ambari.server.H2DatabaseCleaner.clearDatabase(m_injector.getProvider(javax.persistence.EntityManager.class).get());
        m_injector = null;
    }

    @org.junit.Test
    public void testInitialAlertEvent() throws java.lang.Exception {
        junit.framework.Assert.assertEquals(0, m_alertsDao.findAll().size());
        junit.framework.Assert.assertEquals(0, m_listener.getAlertEventReceivedCount(org.apache.ambari.server.events.InitialAlertEvent.class));
        org.apache.ambari.server.orm.entities.AlertDefinitionEntity definition = m_definitionDao.findAll(m_cluster.getClusterId()).get(0);
        org.apache.ambari.server.state.Alert alert = new org.apache.ambari.server.state.Alert(definition.getDefinitionName(), null, definition.getServiceName(), definition.getComponentName(), null, org.apache.ambari.server.state.AlertState.CRITICAL);
        alert.setClusterId(m_cluster.getClusterId());
        org.apache.ambari.server.events.AlertReceivedEvent event = new org.apache.ambari.server.events.AlertReceivedEvent(m_cluster.getClusterId(), alert);
        m_eventPublisher.publish(event);
        junit.framework.Assert.assertEquals(1, m_alertsDao.findAll().size());
        java.util.List<org.apache.ambari.server.orm.entities.AlertCurrentEntity> currentAlerts = m_alertsDao.findCurrent();
        junit.framework.Assert.assertEquals(1, currentAlerts.size());
        junit.framework.Assert.assertEquals(org.apache.ambari.server.state.AlertFirmness.HARD, currentAlerts.get(0).getFirmness());
        junit.framework.Assert.assertEquals(org.apache.ambari.server.state.AlertState.CRITICAL, currentAlerts.get(0).getAlertHistory().getAlertState());
        junit.framework.Assert.assertEquals(1, m_listener.getAlertEventReceivedCount(org.apache.ambari.server.events.InitialAlertEvent.class));
        m_listener.reset();
        alert.setState(org.apache.ambari.server.state.AlertState.WARNING);
        m_eventPublisher.publish(event);
        junit.framework.Assert.assertEquals(0, m_listener.getAlertEventReceivedCount(org.apache.ambari.server.events.InitialAlertEvent.class));
    }

    private void installHdfsService() throws java.lang.Exception {
        java.lang.String serviceName = "HDFS";
        m_serviceFactory.createNew(m_cluster, serviceName, m_repositoryVersion);
        junit.framework.Assert.assertNotNull(m_cluster.getService(serviceName));
    }

    private static class MockModule implements com.google.inject.Module {
        @java.lang.Override
        public void configure(com.google.inject.Binder binder) {
            org.apache.ambari.server.utils.EventBusSynchronizer.synchronizeAmbariEventPublisher(binder);
        }
    }
}