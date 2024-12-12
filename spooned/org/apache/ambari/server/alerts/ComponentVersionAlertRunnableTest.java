package org.apache.ambari.server.alerts;
import org.easymock.EasyMock;
import org.easymock.EasyMockSupport;
import static org.easymock.EasyMock.expect;
public class ComponentVersionAlertRunnableTest extends org.easymock.EasyMockSupport {
    private static final long CLUSTER_ID = 1;

    private static final java.lang.String CLUSTER_NAME = "c1";

    private static final java.lang.String HOSTNAME_1 = "c6401.ambari.apache.org";

    private static final java.lang.String HOSTNAME_2 = "c6402.ambari.apache.org";

    private static final java.lang.String EXPECTED_VERSION = "2.6.0.0-1234";

    private static final java.lang.String WRONG_VERSION = "9.9.9.9-9999";

    private static final java.lang.String DEFINITION_NAME = "ambari_server_component_version";

    private static final java.lang.String DEFINITION_SERVICE = "AMBARI";

    private static final java.lang.String DEFINITION_COMPONENT = "AMBARI_SERVER";

    private static final java.lang.String DEFINITION_LABEL = "Mock Definition";

    private org.apache.ambari.server.state.Clusters m_clusters;

    private org.apache.ambari.server.state.Cluster m_cluster;

    private com.google.inject.Injector m_injector;

    private org.apache.ambari.server.orm.dao.AlertDefinitionDAO m_definitionDao;

    private org.apache.ambari.server.orm.entities.AlertDefinitionEntity m_definition;

    private org.apache.ambari.server.events.MockEventListener m_listener;

    private org.apache.ambari.server.api.services.AmbariMetaInfo m_metaInfo;

    private org.apache.ambari.server.events.publishers.AlertEventPublisher m_eventPublisher;

    private com.google.common.eventbus.EventBus m_synchronizedBus;

    private java.util.Collection<org.apache.ambari.server.state.Host> m_hosts;

    private java.util.Map<java.lang.String, java.util.List<org.apache.ambari.server.state.ServiceComponentHost>> m_hostComponentMap = new java.util.HashMap<>();

    private org.apache.ambari.server.state.StackId m_desidredStackId;

    @org.junit.Before
    public void setup() throws java.lang.Exception {
        m_injector = com.google.inject.Guice.createInjector(new org.apache.ambari.server.alerts.ComponentVersionAlertRunnableTest.MockModule());
        m_definitionDao = m_injector.getInstance(org.apache.ambari.server.orm.dao.AlertDefinitionDAO.class);
        m_clusters = m_injector.getInstance(org.apache.ambari.server.state.Clusters.class);
        m_cluster = m_injector.getInstance(org.apache.ambari.server.state.Cluster.class);
        m_eventPublisher = m_injector.getInstance(org.apache.ambari.server.events.publishers.AlertEventPublisher.class);
        m_listener = m_injector.getInstance(org.apache.ambari.server.events.MockEventListener.class);
        m_definition = createNiceMock(org.apache.ambari.server.orm.entities.AlertDefinitionEntity.class);
        m_metaInfo = m_injector.getInstance(org.apache.ambari.server.api.services.AmbariMetaInfo.class);
        m_synchronizedBus = new com.google.common.eventbus.EventBus();
        java.lang.reflect.Field field = org.apache.ambari.server.events.publishers.AlertEventPublisher.class.getDeclaredField("m_eventBus");
        field.setAccessible(true);
        field.set(m_eventPublisher, m_synchronizedBus);
        m_synchronizedBus.register(m_listener);
        java.util.Map<java.lang.String, org.apache.ambari.server.state.Cluster> clusterMap = new java.util.HashMap<>();
        clusterMap.put(org.apache.ambari.server.alerts.ComponentVersionAlertRunnableTest.CLUSTER_NAME, m_cluster);
        m_hosts = new java.util.ArrayList<>();
        org.apache.ambari.server.state.Host host1 = createNiceMock(org.apache.ambari.server.state.Host.class);
        org.apache.ambari.server.state.Host host2 = createNiceMock(org.apache.ambari.server.state.Host.class);
        EasyMock.expect(host1.getHostName()).andReturn(org.apache.ambari.server.alerts.ComponentVersionAlertRunnableTest.HOSTNAME_1).atLeastOnce();
        EasyMock.expect(host2.getHostName()).andReturn(org.apache.ambari.server.alerts.ComponentVersionAlertRunnableTest.HOSTNAME_2).atLeastOnce();
        m_hosts.add(host1);
        m_hosts.add(host2);
        m_hostComponentMap.put(org.apache.ambari.server.alerts.ComponentVersionAlertRunnableTest.HOSTNAME_1, new java.util.ArrayList<>());
        m_hostComponentMap.put(org.apache.ambari.server.alerts.ComponentVersionAlertRunnableTest.HOSTNAME_2, new java.util.ArrayList<>());
        m_desidredStackId = createNiceMock(org.apache.ambari.server.state.StackId.class);
        EasyMock.expect(m_desidredStackId.getStackName()).andReturn("SOME-STACK").atLeastOnce();
        EasyMock.expect(m_desidredStackId.getStackVersion()).andReturn("STACK-VERSION").atLeastOnce();
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersionEntity = createNiceMock(org.apache.ambari.server.orm.entities.RepositoryVersionEntity.class);
        EasyMock.expect(repositoryVersionEntity.getVersion()).andReturn(org.apache.ambari.server.alerts.ComponentVersionAlertRunnableTest.EXPECTED_VERSION).anyTimes();
        org.apache.ambari.server.state.Service service = createNiceMock(org.apache.ambari.server.state.Service.class);
        EasyMock.expect(service.getDesiredRepositoryVersion()).andReturn(repositoryVersionEntity).atLeastOnce();
        org.apache.ambari.server.state.ServiceComponent serviceComponent = createNiceMock(org.apache.ambari.server.state.ServiceComponent.class);
        EasyMock.expect(serviceComponent.getDesiredStackId()).andReturn(m_desidredStackId).atLeastOnce();
        EasyMock.expect(service.getServiceComponent(org.easymock.EasyMock.anyString())).andReturn(serviceComponent).atLeastOnce();
        org.apache.ambari.server.state.ServiceComponentHost sch1_1 = createNiceMock(org.apache.ambari.server.state.ServiceComponentHost.class);
        org.apache.ambari.server.state.ServiceComponentHost sch1_2 = createNiceMock(org.apache.ambari.server.state.ServiceComponentHost.class);
        org.apache.ambari.server.state.ServiceComponentHost sch2_1 = createNiceMock(org.apache.ambari.server.state.ServiceComponentHost.class);
        org.apache.ambari.server.state.ServiceComponentHost sch2_2 = createNiceMock(org.apache.ambari.server.state.ServiceComponentHost.class);
        EasyMock.expect(sch1_1.getServiceName()).andReturn("FOO").atLeastOnce();
        EasyMock.expect(sch1_1.getServiceComponentName()).andReturn("FOO_COMPONENT").atLeastOnce();
        EasyMock.expect(sch1_1.getVersion()).andReturn(org.apache.ambari.server.alerts.ComponentVersionAlertRunnableTest.EXPECTED_VERSION).atLeastOnce();
        EasyMock.expect(sch1_2.getServiceName()).andReturn("BAR").atLeastOnce();
        EasyMock.expect(sch1_2.getServiceComponentName()).andReturn("BAR_COMPONENT").atLeastOnce();
        EasyMock.expect(sch1_2.getVersion()).andReturn(org.apache.ambari.server.alerts.ComponentVersionAlertRunnableTest.EXPECTED_VERSION).atLeastOnce();
        EasyMock.expect(sch2_1.getServiceName()).andReturn("FOO").atLeastOnce();
        EasyMock.expect(sch2_1.getServiceComponentName()).andReturn("FOO_COMPONENT").atLeastOnce();
        EasyMock.expect(sch2_1.getVersion()).andReturn(org.apache.ambari.server.alerts.ComponentVersionAlertRunnableTest.EXPECTED_VERSION).atLeastOnce();
        EasyMock.expect(sch2_2.getServiceName()).andReturn("BAZ").atLeastOnce();
        EasyMock.expect(sch2_2.getServiceComponentName()).andReturn("BAZ_COMPONENT").atLeastOnce();
        EasyMock.expect(sch2_2.getVersion()).andReturn(org.apache.ambari.server.alerts.ComponentVersionAlertRunnableTest.EXPECTED_VERSION).atLeastOnce();
        m_hostComponentMap.get(org.apache.ambari.server.alerts.ComponentVersionAlertRunnableTest.HOSTNAME_1).add(sch1_1);
        m_hostComponentMap.get(org.apache.ambari.server.alerts.ComponentVersionAlertRunnableTest.HOSTNAME_1).add(sch1_2);
        m_hostComponentMap.get(org.apache.ambari.server.alerts.ComponentVersionAlertRunnableTest.HOSTNAME_2).add(sch2_1);
        m_hostComponentMap.get(org.apache.ambari.server.alerts.ComponentVersionAlertRunnableTest.HOSTNAME_2).add(sch2_2);
        EasyMock.expect(m_definition.getDefinitionName()).andReturn(org.apache.ambari.server.alerts.ComponentVersionAlertRunnableTest.DEFINITION_NAME).atLeastOnce();
        EasyMock.expect(m_definition.getServiceName()).andReturn(org.apache.ambari.server.alerts.ComponentVersionAlertRunnableTest.DEFINITION_SERVICE).atLeastOnce();
        EasyMock.expect(m_definition.getComponentName()).andReturn(org.apache.ambari.server.alerts.ComponentVersionAlertRunnableTest.DEFINITION_COMPONENT).atLeastOnce();
        EasyMock.expect(m_definition.getLabel()).andReturn(org.apache.ambari.server.alerts.ComponentVersionAlertRunnableTest.DEFINITION_LABEL).atLeastOnce();
        EasyMock.expect(m_definition.getEnabled()).andReturn(true).atLeastOnce();
        EasyMock.expect(m_cluster.getClusterId()).andReturn(org.apache.ambari.server.alerts.ComponentVersionAlertRunnableTest.CLUSTER_ID).atLeastOnce();
        EasyMock.expect(m_cluster.getHosts()).andReturn(m_hosts).atLeastOnce();
        EasyMock.expect(m_cluster.getService(org.easymock.EasyMock.anyString())).andReturn(service).atLeastOnce();
        EasyMock.expect(m_clusters.getClusters()).andReturn(clusterMap).atLeastOnce();
        EasyMock.expect(m_definitionDao.findByName(org.apache.ambari.server.alerts.ComponentVersionAlertRunnableTest.CLUSTER_ID, org.apache.ambari.server.alerts.ComponentVersionAlertRunnableTest.DEFINITION_NAME)).andReturn(m_definition).atLeastOnce();
        m_metaInfo.init();
        org.easymock.EasyMock.expectLastCall().anyTimes();
        EasyMock.expect(m_cluster.getServiceComponentHosts(org.apache.ambari.server.alerts.ComponentVersionAlertRunnableTest.HOSTNAME_1)).andReturn(m_hostComponentMap.get(org.apache.ambari.server.alerts.ComponentVersionAlertRunnableTest.HOSTNAME_1)).once();
        EasyMock.expect(m_cluster.getServiceComponentHosts(org.apache.ambari.server.alerts.ComponentVersionAlertRunnableTest.HOSTNAME_2)).andReturn(m_hostComponentMap.get(org.apache.ambari.server.alerts.ComponentVersionAlertRunnableTest.HOSTNAME_2)).once();
        org.apache.ambari.server.state.ComponentInfo componentInfo = createNiceMock(org.apache.ambari.server.state.ComponentInfo.class);
        EasyMock.expect(componentInfo.isVersionAdvertised()).andReturn(true).atLeastOnce();
        EasyMock.expect(m_metaInfo.getComponent(org.easymock.EasyMock.anyString(), org.easymock.EasyMock.anyString(), org.easymock.EasyMock.anyString(), org.easymock.EasyMock.anyString())).andReturn(componentInfo).atLeastOnce();
    }

    @org.junit.After
    public void teardown() throws java.lang.Exception {
    }

    @org.junit.Test
    public void testUpgradeInProgress() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.UpgradeEntity upgrade = createNiceMock(org.apache.ambari.server.orm.entities.UpgradeEntity.class);
        EasyMock.expect(upgrade.getDirection()).andReturn(org.apache.ambari.server.stack.upgrade.Direction.UPGRADE).atLeastOnce();
        EasyMock.expect(m_cluster.getUpgradeInProgress()).andReturn(upgrade).once();
        replayAll();
        m_metaInfo.init();
        junit.framework.Assert.assertEquals(0, m_listener.getAlertEventReceivedCount(org.apache.ambari.server.events.AlertReceivedEvent.class));
        org.apache.ambari.server.alerts.ComponentVersionAlertRunnable runnable = new org.apache.ambari.server.alerts.ComponentVersionAlertRunnable(m_definition.getDefinitionName());
        m_injector.injectMembers(runnable);
        runnable.run();
        junit.framework.Assert.assertEquals(1, m_listener.getAlertEventReceivedCount(org.apache.ambari.server.events.AlertReceivedEvent.class));
        java.util.List<org.apache.ambari.server.events.AlertEvent> events = m_listener.getAlertEventInstances(org.apache.ambari.server.events.AlertReceivedEvent.class);
        junit.framework.Assert.assertEquals(1, events.size());
        org.apache.ambari.server.events.AlertReceivedEvent event = ((org.apache.ambari.server.events.AlertReceivedEvent) (events.get(0)));
        org.apache.ambari.server.state.Alert alert = event.getAlert();
        junit.framework.Assert.assertEquals("AMBARI", alert.getService());
        junit.framework.Assert.assertEquals("AMBARI_SERVER", alert.getComponent());
        junit.framework.Assert.assertEquals(org.apache.ambari.server.state.AlertState.SKIPPED, alert.getState());
        junit.framework.Assert.assertEquals(org.apache.ambari.server.alerts.ComponentVersionAlertRunnableTest.DEFINITION_NAME, alert.getName());
    }

    @org.junit.Test
    public void testAllComponentVersionsCorrect() throws java.lang.Exception {
        replayAll();
        m_metaInfo.init();
        junit.framework.Assert.assertEquals(0, m_listener.getAlertEventReceivedCount(org.apache.ambari.server.events.AlertReceivedEvent.class));
        org.apache.ambari.server.alerts.ComponentVersionAlertRunnable runnable = new org.apache.ambari.server.alerts.ComponentVersionAlertRunnable(m_definition.getDefinitionName());
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
        junit.framework.Assert.assertEquals(org.apache.ambari.server.alerts.ComponentVersionAlertRunnableTest.DEFINITION_NAME, alert.getName());
        verifyAll();
    }

    @org.junit.Test
    public void testomponentVersionMismatch() throws java.lang.Exception {
        org.apache.ambari.server.state.ServiceComponentHost sch = m_hostComponentMap.get(org.apache.ambari.server.alerts.ComponentVersionAlertRunnableTest.HOSTNAME_1).get(0);
        org.easymock.EasyMock.reset(sch);
        EasyMock.expect(sch.getServiceName()).andReturn("FOO").atLeastOnce();
        EasyMock.expect(sch.getServiceComponentName()).andReturn("FOO_COMPONENT").atLeastOnce();
        EasyMock.expect(sch.getVersion()).andReturn(org.apache.ambari.server.alerts.ComponentVersionAlertRunnableTest.WRONG_VERSION).atLeastOnce();
        replayAll();
        m_metaInfo.init();
        junit.framework.Assert.assertEquals(0, m_listener.getAlertEventReceivedCount(org.apache.ambari.server.events.AlertReceivedEvent.class));
        org.apache.ambari.server.alerts.ComponentVersionAlertRunnable runnable = new org.apache.ambari.server.alerts.ComponentVersionAlertRunnable(m_definition.getDefinitionName());
        m_injector.injectMembers(runnable);
        runnable.run();
        junit.framework.Assert.assertEquals(1, m_listener.getAlertEventReceivedCount(org.apache.ambari.server.events.AlertReceivedEvent.class));
        java.util.List<org.apache.ambari.server.events.AlertEvent> events = m_listener.getAlertEventInstances(org.apache.ambari.server.events.AlertReceivedEvent.class);
        junit.framework.Assert.assertEquals(1, events.size());
        org.apache.ambari.server.events.AlertReceivedEvent event = ((org.apache.ambari.server.events.AlertReceivedEvent) (events.get(0)));
        org.apache.ambari.server.state.Alert alert = event.getAlert();
        junit.framework.Assert.assertEquals("AMBARI", alert.getService());
        junit.framework.Assert.assertEquals("AMBARI_SERVER", alert.getComponent());
        junit.framework.Assert.assertEquals(org.apache.ambari.server.state.AlertState.WARNING, alert.getState());
        junit.framework.Assert.assertEquals(org.apache.ambari.server.alerts.ComponentVersionAlertRunnableTest.DEFINITION_NAME, alert.getName());
        verifyAll();
    }

    private class MockModule implements com.google.inject.Module {
        @java.lang.Override
        public void configure(com.google.inject.Binder binder) {
            org.apache.ambari.server.state.Cluster cluster = createNiceMock(org.apache.ambari.server.state.Cluster.class);
            org.apache.ambari.server.testutils.PartialNiceMockBinder.newBuilder(ComponentVersionAlertRunnableTest.this).addConfigsBindings().addDBAccessorBinding().addFactoriesInstallBinding().addAmbariMetaInfoBinding().addLdapBindings().build().configure(binder);
            binder.bind(org.apache.ambari.server.api.services.AmbariMetaInfo.class).toInstance(createNiceMock(org.apache.ambari.server.api.services.AmbariMetaInfo.class));
            binder.bind(org.apache.ambari.server.state.Cluster.class).toInstance(cluster);
            binder.bind(org.apache.ambari.server.orm.dao.AlertDefinitionDAO.class).toInstance(createNiceMock(org.apache.ambari.server.orm.dao.AlertDefinitionDAO.class));
        }
    }
}