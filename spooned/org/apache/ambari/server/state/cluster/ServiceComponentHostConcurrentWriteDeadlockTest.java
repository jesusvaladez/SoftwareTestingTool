package org.apache.ambari.server.state.cluster;
import org.easymock.EasyMock;
public class ServiceComponentHostConcurrentWriteDeadlockTest {
    private static final int NUMBER_OF_THREADS = 3;

    @com.google.inject.Inject
    private com.google.inject.Injector injector;

    @com.google.inject.Inject
    private org.apache.ambari.server.state.Clusters clusters;

    @com.google.inject.Inject
    private org.apache.ambari.server.state.ServiceFactory serviceFactory;

    @com.google.inject.Inject
    private org.apache.ambari.server.state.ServiceComponentFactory serviceComponentFactory;

    @com.google.inject.Inject
    private org.apache.ambari.server.state.ServiceComponentHostFactory serviceComponentHostFactory;

    @com.google.inject.Inject
    private org.apache.ambari.server.state.ConfigFactory configFactory;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.OrmTestHelper helper;

    private org.apache.ambari.server.state.StackId stackId = new org.apache.ambari.server.state.StackId("HDP-0.1");

    private final java.lang.String REPO_VERSION = "0.1-1234";

    private org.apache.ambari.server.orm.entities.RepositoryVersionEntity m_repositoryVersion;

    private org.apache.ambari.server.state.Cluster cluster;

    @org.junit.Before
    public void setup() throws java.lang.Exception {
        injector = com.google.inject.Guice.createInjector(com.google.inject.util.Modules.override(new org.apache.ambari.server.orm.InMemoryDefaultTestModule()).with(new org.apache.ambari.server.state.cluster.ServiceComponentHostConcurrentWriteDeadlockTest.MockModule()));
        injector.getInstance(org.apache.ambari.server.orm.GuiceJpaInitializer.class);
        injector.injectMembers(this);
        org.apache.ambari.server.orm.OrmTestHelper helper = injector.getInstance(org.apache.ambari.server.orm.OrmTestHelper.class);
        helper.createStack(stackId);
        clusters.addCluster("c1", stackId);
        cluster = clusters.getCluster("c1");
        m_repositoryVersion = helper.getOrCreateRepositoryVersion(stackId, REPO_VERSION);
        org.apache.ambari.server.state.Config config1 = configFactory.createNew(cluster, "test-type1", null, new java.util.HashMap<>(), new java.util.HashMap<>());
        org.apache.ambari.server.state.Config config2 = configFactory.createNew(cluster, "test-type2", null, new java.util.HashMap<>(), new java.util.HashMap<>());
        cluster.addDesiredConfig("test user", new java.util.HashSet<>(java.util.Arrays.asList(config1, config2)));
        java.lang.String hostName = "c6401";
        clusters.addHost(hostName);
        setOsFamily(clusters.getHost(hostName), "redhat", "6.4");
        clusters.mapHostToCluster(hostName, "c1");
        org.apache.ambari.server.state.Service service = installService("HDFS");
        addServiceComponent(service, "NAMENODE");
    }

    @org.junit.After
    public void teardown() throws org.apache.ambari.server.AmbariException, java.sql.SQLException {
        org.apache.ambari.server.H2DatabaseCleaner.clearDatabaseAndStopPersistenceService(injector);
    }

    @org.junit.Test
    public void testConcurrentWriteDeadlock() throws java.lang.Exception {
        org.apache.ambari.server.state.ServiceComponentHost nameNodeSCH = createNewServiceComponentHost("HDFS", "NAMENODE", "c6401");
        org.apache.ambari.server.state.ServiceComponentHost dataNodeSCH = createNewServiceComponentHost("HDFS", "DATANODE", "c6401");
        java.util.List<org.apache.ambari.server.state.ServiceComponentHost> serviceComponentHosts = new java.util.ArrayList<>();
        serviceComponentHosts.add(nameNodeSCH);
        serviceComponentHosts.add(dataNodeSCH);
        java.util.List<java.lang.Thread> threads = new java.util.ArrayList<>();
        for (int i = 0; i < org.apache.ambari.server.state.cluster.ServiceComponentHostConcurrentWriteDeadlockTest.NUMBER_OF_THREADS; i++) {
            org.apache.ambari.server.state.cluster.ServiceComponentHostConcurrentWriteDeadlockTest.ServiceComponentHostDeadlockWriter thread = new org.apache.ambari.server.state.cluster.ServiceComponentHostConcurrentWriteDeadlockTest.ServiceComponentHostDeadlockWriter();
            thread.setServiceComponentHosts(serviceComponentHosts);
            thread.start();
            threads.add(thread);
        }
        org.apache.ambari.server.testing.DeadlockWarningThread wt = new org.apache.ambari.server.testing.DeadlockWarningThread(threads);
        while (true) {
            if (!wt.isAlive()) {
                break;
            }
        } 
        if (wt.isDeadlocked()) {
            org.junit.Assert.assertFalse(wt.getErrorMessages().toString(), wt.isDeadlocked());
        } else {
            org.junit.Assert.assertFalse(wt.isDeadlocked());
        }
    }

    private static final class ServiceComponentHostDeadlockWriter extends java.lang.Thread {
        private java.util.List<org.apache.ambari.server.state.ServiceComponentHost> serviceComponentHosts;

        public void setServiceComponentHosts(java.util.List<org.apache.ambari.server.state.ServiceComponentHost> serviceComponentHosts) {
            this.serviceComponentHosts = serviceComponentHosts;
        }

        @java.lang.Override
        public void run() {
            try {
                for (int i = 0; i < 1000; i++) {
                    org.apache.ambari.server.state.State state = ((i % 2) == 0) ? org.apache.ambari.server.state.State.INSTALLING : org.apache.ambari.server.state.State.INSTALL_FAILED;
                    java.lang.String version = ((i % 2) == 0) ? "UNKNOWN" : "2.2.0.0-1234";
                    for (org.apache.ambari.server.state.ServiceComponentHost serviceComponentHost : serviceComponentHosts) {
                        serviceComponentHost.setState(state);
                        serviceComponentHost.setVersion(version);
                    }
                    java.lang.Thread.sleep(10);
                }
            } catch (java.lang.Exception exception) {
                throw new java.lang.RuntimeException(exception);
            }
        }
    }

    private void setOsFamily(org.apache.ambari.server.state.Host host, java.lang.String osFamily, java.lang.String osVersion) {
        java.util.Map<java.lang.String, java.lang.String> hostAttributes = new java.util.HashMap<>(2);
        hostAttributes.put("os_family", osFamily);
        hostAttributes.put("os_release_version", osVersion);
        host.setHostAttributes(hostAttributes);
    }

    private org.apache.ambari.server.state.ServiceComponentHost createNewServiceComponentHost(java.lang.String svc, java.lang.String svcComponent, java.lang.String hostName) throws org.apache.ambari.server.AmbariException {
        org.junit.Assert.assertNotNull(cluster.getConfigGroups());
        org.apache.ambari.server.state.Service s = installService(svc);
        org.apache.ambari.server.state.ServiceComponent sc = addServiceComponent(s, svcComponent);
        org.apache.ambari.server.state.ServiceComponentHost sch = serviceComponentHostFactory.createNew(sc, hostName);
        sc.addServiceComponentHost(sch);
        sch.setDesiredState(org.apache.ambari.server.state.State.INSTALLED);
        sch.setState(org.apache.ambari.server.state.State.INSTALLED);
        sch.setVersion(REPO_VERSION);
        return sch;
    }

    private org.apache.ambari.server.state.Service installService(java.lang.String serviceName) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.Service service = null;
        try {
            service = cluster.getService(serviceName);
        } catch (org.apache.ambari.server.ServiceNotFoundException e) {
            service = serviceFactory.createNew(cluster, serviceName, m_repositoryVersion);
            cluster.addService(service);
        }
        return service;
    }

    private org.apache.ambari.server.state.ServiceComponent addServiceComponent(org.apache.ambari.server.state.Service service, java.lang.String componentName) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.ServiceComponent serviceComponent = null;
        try {
            serviceComponent = service.getServiceComponent(componentName);
        } catch (org.apache.ambari.server.ServiceComponentNotFoundException e) {
            serviceComponent = serviceComponentFactory.createNew(service, componentName);
            service.addServiceComponent(serviceComponent);
            serviceComponent.setDesiredState(org.apache.ambari.server.state.State.INSTALLED);
        }
        return serviceComponent;
    }

    private class MockModule implements com.google.inject.Module {
        @java.lang.Override
        public void configure(com.google.inject.Binder binder) {
            binder.bind(org.apache.ambari.server.events.listeners.upgrade.HostVersionOutOfSyncListener.class).toInstance(org.easymock.EasyMock.createNiceMock(org.apache.ambari.server.events.listeners.upgrade.HostVersionOutOfSyncListener.class));
        }
    }
}