package org.apache.ambari.server.events;
import javax.persistence.EntityManager;
public class EventsTest {
    private static final java.lang.String HOSTNAME = "c6401.ambari.apache.org";

    private org.apache.ambari.server.state.Clusters m_clusters;

    private org.apache.ambari.server.state.Cluster m_cluster;

    private java.lang.String m_clusterName;

    private com.google.inject.Injector m_injector;

    private org.apache.ambari.server.state.ServiceFactory m_serviceFactory;

    private org.apache.ambari.server.state.ServiceComponentFactory m_componentFactory;

    private org.apache.ambari.server.state.ServiceComponentHostFactory m_schFactory;

    private org.apache.ambari.server.events.MockEventListener m_listener;

    private org.apache.ambari.server.orm.OrmTestHelper m_helper;

    private org.apache.ambari.server.orm.dao.AlertDefinitionDAO m_definitionDao;

    private org.apache.ambari.server.orm.dao.AlertDispatchDAO m_alertDispatchDao;

    private final java.lang.String STACK_VERSION = "2.0.6";

    private final java.lang.String REPO_VERSION = "2.0.6-1234";

    private org.apache.ambari.server.orm.entities.RepositoryVersionEntity m_repositoryVersion;

    @org.junit.Before
    public void setup() throws java.lang.Exception {
        m_injector = com.google.inject.Guice.createInjector(new org.apache.ambari.server.orm.InMemoryDefaultTestModule());
        m_injector.getInstance(org.apache.ambari.server.orm.GuiceJpaInitializer.class);
        m_helper = m_injector.getInstance(org.apache.ambari.server.orm.OrmTestHelper.class);
        com.google.common.eventbus.EventBus synchronizedBus = org.apache.ambari.server.utils.EventBusSynchronizer.synchronizeAmbariEventPublisher(m_injector);
        m_listener = m_injector.getInstance(org.apache.ambari.server.events.MockEventListener.class);
        synchronizedBus.register(m_listener);
        m_clusters = m_injector.getInstance(org.apache.ambari.server.state.Clusters.class);
        m_serviceFactory = m_injector.getInstance(org.apache.ambari.server.state.ServiceFactory.class);
        m_componentFactory = m_injector.getInstance(org.apache.ambari.server.state.ServiceComponentFactory.class);
        m_schFactory = m_injector.getInstance(org.apache.ambari.server.state.ServiceComponentHostFactory.class);
        m_definitionDao = m_injector.getInstance(org.apache.ambari.server.orm.dao.AlertDefinitionDAO.class);
        m_alertDispatchDao = m_injector.getInstance(org.apache.ambari.server.orm.dao.AlertDispatchDAO.class);
        m_clusterName = "foo";
        org.apache.ambari.server.state.StackId stackId = new org.apache.ambari.server.state.StackId("HDP", STACK_VERSION);
        m_helper.createStack(stackId);
        m_clusters.addCluster(m_clusterName, stackId);
        m_clusters.addHost(org.apache.ambari.server.events.EventsTest.HOSTNAME);
        org.apache.ambari.server.state.Host host = m_clusters.getHost(org.apache.ambari.server.events.EventsTest.HOSTNAME);
        java.util.Map<java.lang.String, java.lang.String> hostAttributes = new java.util.HashMap<>();
        hostAttributes.put("os_family", "redhat");
        hostAttributes.put("os_release_version", "6.4");
        host.setHostAttributes(hostAttributes);
        host.setState(org.apache.ambari.server.state.HostState.HEALTHY);
        m_cluster = m_clusters.getCluster(m_clusterName);
        junit.framework.Assert.assertNotNull(m_cluster);
        m_cluster.setDesiredStackVersion(stackId);
        m_repositoryVersion = m_helper.getOrCreateRepositoryVersion(stackId, REPO_VERSION);
        m_clusters.mapHostToCluster(org.apache.ambari.server.events.EventsTest.HOSTNAME, m_clusterName);
        m_clusters.updateHostMappings(host);
    }

    @org.junit.After
    public void teardown() throws java.lang.Exception {
        org.apache.ambari.server.H2DatabaseCleaner.clearDatabase(m_injector.getProvider(javax.persistence.EntityManager.class).get());
        m_injector = null;
    }

    @org.junit.Test
    public void testServiceInstalledEvent() throws java.lang.Exception {
        java.lang.Class<? extends org.apache.ambari.server.events.AmbariEvent> eventClass = org.apache.ambari.server.events.ServiceInstalledEvent.class;
        junit.framework.Assert.assertFalse(m_listener.isAmbariEventReceived(eventClass));
        installHdfsService();
        junit.framework.Assert.assertTrue(m_listener.isAmbariEventReceived(eventClass));
    }

    @org.junit.Test
    public void testServiceRemovedEvent() throws java.lang.Exception {
        java.lang.Class<? extends org.apache.ambari.server.events.AmbariEvent> eventClass = org.apache.ambari.server.events.ServiceRemovedEvent.class;
        junit.framework.Assert.assertFalse(m_listener.isAmbariEventReceived(eventClass));
        installHdfsService();
        m_cluster.deleteAllServices();
        junit.framework.Assert.assertTrue(m_listener.isAmbariEventReceived(eventClass));
    }

    @org.junit.Test
    public void testServiceRemovedEventForAlerts() throws java.lang.Exception {
        java.lang.Class<? extends org.apache.ambari.server.events.AmbariEvent> eventClass = org.apache.ambari.server.events.ServiceRemovedEvent.class;
        junit.framework.Assert.assertFalse(m_listener.isAmbariEventReceived(eventClass));
        installHdfsService();
        org.apache.ambari.server.orm.entities.AlertGroupEntity group = m_alertDispatchDao.findGroupByName(m_cluster.getClusterId(), "HDFS");
        junit.framework.Assert.assertNotNull(group);
        junit.framework.Assert.assertTrue(group.isDefault());
        junit.framework.Assert.assertTrue(m_definitionDao.findAll(m_cluster.getClusterId()).size() > 0);
        java.util.List<org.apache.ambari.server.orm.entities.AlertDefinitionEntity> hdfsDefinitions = m_definitionDao.findByService(m_cluster.getClusterId(), "HDFS");
        junit.framework.Assert.assertTrue(hdfsDefinitions.size() > 0);
        org.apache.ambari.server.orm.entities.AlertDefinitionEntity definition = hdfsDefinitions.get(0);
        m_cluster.getService("HDFS").delete(new org.apache.ambari.server.controller.internal.DeleteHostComponentStatusMetaData());
        junit.framework.Assert.assertTrue(m_listener.isAmbariEventReceived(eventClass));
        hdfsDefinitions = m_definitionDao.findByService(m_cluster.getClusterId(), "HDFS");
        junit.framework.Assert.assertEquals(0, hdfsDefinitions.size());
        group = m_alertDispatchDao.findGroupByName(m_cluster.getClusterId(), "HDFS");
        junit.framework.Assert.assertNull(group);
    }

    @org.junit.Test
    public void testServiceRemovedEventForDefaultAlertGroup() throws java.lang.Exception {
        java.lang.Class<? extends org.apache.ambari.server.events.AmbariEvent> eventClass = org.apache.ambari.server.events.ServiceRemovedEvent.class;
        junit.framework.Assert.assertFalse(m_listener.isAmbariEventReceived(eventClass));
        installHdfsService();
        org.apache.ambari.server.orm.entities.AlertGroupEntity group = m_alertDispatchDao.findGroupByName(m_cluster.getClusterId(), "HDFS");
        junit.framework.Assert.assertNotNull(group);
        junit.framework.Assert.assertTrue(group.isDefault());
        java.util.List<org.apache.ambari.server.orm.entities.AlertDefinitionEntity> hdfsDefinitions = m_definitionDao.findByService(m_cluster.getClusterId(), "HDFS");
        for (org.apache.ambari.server.orm.entities.AlertDefinitionEntity definition : hdfsDefinitions) {
            m_definitionDao.remove(definition);
        }
        hdfsDefinitions = m_definitionDao.findByService(m_cluster.getClusterId(), "HDFS");
        junit.framework.Assert.assertEquals(0, hdfsDefinitions.size());
        m_cluster.getService("HDFS").delete(new org.apache.ambari.server.controller.internal.DeleteHostComponentStatusMetaData());
        junit.framework.Assert.assertTrue(m_listener.isAmbariEventReceived(eventClass));
        group = m_alertDispatchDao.findGroupByName(m_cluster.getClusterId(), "HDFS");
        junit.framework.Assert.assertNull(group);
    }

    @org.junit.Test
    public void testServiceRemovedEventForAlertDefinitions() throws java.lang.Exception {
        java.lang.Class<? extends org.apache.ambari.server.events.AmbariEvent> eventClass = org.apache.ambari.server.events.ServiceRemovedEvent.class;
        junit.framework.Assert.assertFalse(m_listener.isAmbariEventReceived(eventClass));
        installHdfsService();
        org.apache.ambari.server.orm.entities.AlertGroupEntity group = m_alertDispatchDao.findGroupByName(m_cluster.getClusterId(), "HDFS");
        junit.framework.Assert.assertNotNull(group);
        junit.framework.Assert.assertTrue(group.isDefault());
        junit.framework.Assert.assertTrue(m_definitionDao.findAll(m_cluster.getClusterId()).size() > 0);
        java.util.List<org.apache.ambari.server.orm.entities.AlertDefinitionEntity> hdfsDefinitions = m_definitionDao.findByService(m_cluster.getClusterId(), "HDFS");
        junit.framework.Assert.assertTrue(hdfsDefinitions.size() > 0);
        m_alertDispatchDao.remove(group);
        group = m_alertDispatchDao.findGroupByName(m_cluster.getClusterId(), "HDFS");
        junit.framework.Assert.assertNull(group);
        m_cluster.getService("HDFS").delete(new org.apache.ambari.server.controller.internal.DeleteHostComponentStatusMetaData());
        junit.framework.Assert.assertTrue(m_listener.isAmbariEventReceived(eventClass));
        hdfsDefinitions = m_definitionDao.findByService(m_cluster.getClusterId(), "HDFS");
        junit.framework.Assert.assertEquals(0, hdfsDefinitions.size());
    }

    @org.junit.Test
    public void testServiceComponentUninstalledEvent() throws java.lang.Exception {
        java.lang.Class<? extends org.apache.ambari.server.events.AmbariEvent> eventClass = org.apache.ambari.server.events.ServiceComponentUninstalledEvent.class;
        installHdfsService();
        junit.framework.Assert.assertFalse(m_listener.isAmbariEventReceived(eventClass));
        m_cluster.getServiceComponentHosts(org.apache.ambari.server.events.EventsTest.HOSTNAME).get(0).delete(new org.apache.ambari.server.controller.internal.DeleteHostComponentStatusMetaData());
        junit.framework.Assert.assertTrue(m_listener.isAmbariEventReceived(eventClass));
    }

    @org.junit.Test
    public void testMaintenanceModeEvents() throws java.lang.Exception {
        installHdfsService();
        org.apache.ambari.server.state.Service service = m_cluster.getService("HDFS");
        java.lang.Class<? extends org.apache.ambari.server.events.AmbariEvent> eventClass = org.apache.ambari.server.events.MaintenanceModeEvent.class;
        junit.framework.Assert.assertFalse(m_listener.isAmbariEventReceived(eventClass));
        service.setMaintenanceState(org.apache.ambari.server.state.MaintenanceState.ON);
        junit.framework.Assert.assertTrue(m_listener.isAmbariEventReceived(eventClass));
        junit.framework.Assert.assertEquals(1, m_listener.getAmbariEventReceivedCount(eventClass));
        m_listener.reset();
        junit.framework.Assert.assertFalse(m_listener.isAmbariEventReceived(eventClass));
        java.util.List<org.apache.ambari.server.state.ServiceComponentHost> componentHosts = m_cluster.getServiceComponentHosts(org.apache.ambari.server.events.EventsTest.HOSTNAME);
        org.apache.ambari.server.state.ServiceComponentHost componentHost = componentHosts.get(0);
        componentHost.setMaintenanceState(org.apache.ambari.server.state.MaintenanceState.OFF);
        junit.framework.Assert.assertTrue(m_listener.isAmbariEventReceived(eventClass));
        junit.framework.Assert.assertEquals(1, m_listener.getAmbariEventReceivedCount(eventClass));
        m_listener.reset();
        junit.framework.Assert.assertFalse(m_listener.isAmbariEventReceived(eventClass));
        org.apache.ambari.server.state.Host host = m_clusters.getHost(org.apache.ambari.server.events.EventsTest.HOSTNAME);
        host.setMaintenanceState(m_cluster.getClusterId(), org.apache.ambari.server.state.MaintenanceState.ON);
        host.setMaintenanceState(m_cluster.getClusterId(), org.apache.ambari.server.state.MaintenanceState.OFF);
        junit.framework.Assert.assertTrue(m_listener.isAmbariEventReceived(eventClass));
        junit.framework.Assert.assertEquals(2, m_listener.getAmbariEventReceivedCount(eventClass));
    }

    @org.junit.Test
    public void testClusterRenameEvent() throws java.lang.Exception {
        java.lang.Class<? extends org.apache.ambari.server.events.AmbariEvent> eventClass = org.apache.ambari.server.events.ClusterEvent.class;
        installHdfsService();
        junit.framework.Assert.assertFalse(m_listener.isAmbariEventReceived(eventClass));
        m_cluster.setClusterName(java.util.UUID.randomUUID().toString());
        junit.framework.Assert.assertTrue(m_listener.isAmbariEventReceived(eventClass));
        java.util.List<org.apache.ambari.server.events.AmbariEvent> ambariEvents = m_listener.getAmbariEventInstances(eventClass);
        junit.framework.Assert.assertEquals(1, ambariEvents.size());
        junit.framework.Assert.assertEquals(org.apache.ambari.server.events.AmbariEvent.AmbariEventType.CLUSTER_RENAME, ambariEvents.get(0).getType());
    }

    private void installHdfsService() throws java.lang.Exception {
        java.lang.String serviceName = "HDFS";
        org.apache.ambari.server.state.Service service = m_serviceFactory.createNew(m_cluster, serviceName, m_repositoryVersion);
        service = m_cluster.getService(serviceName);
        junit.framework.Assert.assertNotNull(service);
        org.apache.ambari.server.state.ServiceComponent component = m_componentFactory.createNew(service, "DATANODE");
        service.addServiceComponent(component);
        component.setDesiredState(org.apache.ambari.server.state.State.INSTALLED);
        org.apache.ambari.server.state.ServiceComponentHost sch = m_schFactory.createNew(component, org.apache.ambari.server.events.EventsTest.HOSTNAME);
        component.addServiceComponentHost(sch);
        sch.setDesiredState(org.apache.ambari.server.state.State.INSTALLED);
        sch.setState(org.apache.ambari.server.state.State.INSTALLED);
    }
}