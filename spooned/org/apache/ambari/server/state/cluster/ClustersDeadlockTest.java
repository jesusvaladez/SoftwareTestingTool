package org.apache.ambari.server.state.cluster;
import org.easymock.EasyMock;
public class ClustersDeadlockTest {
    private static final java.lang.String CLUSTER_NAME = "c1";

    private static final int NUMBER_OF_HOSTS = 100;

    private static final int NUMBER_OF_THREADS = 3;

    private final java.util.concurrent.atomic.AtomicInteger hostNameCounter = new java.util.concurrent.atomic.AtomicInteger(0);

    private java.util.concurrent.CountDownLatch writerStoppedSignal;

    private java.util.concurrent.CountDownLatch readerStoppedSignal;

    private org.apache.ambari.server.state.StackId stackId = new org.apache.ambari.server.state.StackId("HDP-0.1");

    private java.lang.String REPO_VERSION = "0.1-1234";

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
    private org.apache.ambari.server.orm.OrmTestHelper helper;

    private org.apache.ambari.server.state.Cluster cluster;

    @org.junit.Before
    public void setup() throws java.lang.Exception {
        injector = com.google.inject.Guice.createInjector(com.google.inject.util.Modules.override(new org.apache.ambari.server.orm.InMemoryDefaultTestModule()).with(new org.apache.ambari.server.state.cluster.ClustersDeadlockTest.MockModule()));
        injector.getInstance(org.apache.ambari.server.orm.GuiceJpaInitializer.class);
        injector.injectMembers(this);
        org.apache.ambari.server.state.StackId stackId = new org.apache.ambari.server.state.StackId("HDP-0.1");
        helper.createStack(stackId);
        clusters.addCluster(org.apache.ambari.server.state.cluster.ClustersDeadlockTest.CLUSTER_NAME, stackId);
        cluster = clusters.getCluster(org.apache.ambari.server.state.cluster.ClustersDeadlockTest.CLUSTER_NAME);
        helper.getOrCreateRepositoryVersion(stackId, stackId.getStackVersion());
        installService("HDFS");
        writerStoppedSignal = new java.util.concurrent.CountDownLatch(org.apache.ambari.server.state.cluster.ClustersDeadlockTest.NUMBER_OF_THREADS);
        readerStoppedSignal = new java.util.concurrent.CountDownLatch(org.apache.ambari.server.state.cluster.ClustersDeadlockTest.NUMBER_OF_THREADS);
    }

    @org.junit.After
    public void teardown() throws org.apache.ambari.server.AmbariException, java.sql.SQLException {
        org.apache.ambari.server.H2DatabaseCleaner.clearDatabaseAndStopPersistenceService(injector);
    }

    private void doLoadTest(com.google.inject.Provider<? extends java.lang.Thread> readerProvider, com.google.inject.Provider<? extends java.lang.Thread> writerProvider, final int numberOfThreads, java.util.concurrent.CountDownLatch writerStoppedSignal, java.util.concurrent.CountDownLatch readerStoppedSignal) throws java.lang.Exception {
        java.util.List<java.lang.Thread> writerThreads = new java.util.ArrayList<>();
        for (int i = 0; i < numberOfThreads; i++) {
            java.lang.Thread readerThread = readerProvider.get();
            java.lang.Thread writerThread = writerProvider.get();
            writerThreads.add(writerThread);
            readerThread.start();
            writerThread.start();
        }
        for (java.lang.Thread writerThread : writerThreads) {
            writerThread.join();
            writerStoppedSignal.countDown();
        }
        readerStoppedSignal.await();
    }

    @org.junit.Test(timeout = 40000)
    public void testDeadlockWhileMappingHosts() throws java.lang.Exception {
        com.google.inject.Provider<org.apache.ambari.server.state.cluster.ClustersDeadlockTest.ClustersHostMapperThread> clustersHostMapperThreadFactory = new com.google.inject.Provider<org.apache.ambari.server.state.cluster.ClustersDeadlockTest.ClustersHostMapperThread>() {
            @java.lang.Override
            public org.apache.ambari.server.state.cluster.ClustersDeadlockTest.ClustersHostMapperThread get() {
                return new org.apache.ambari.server.state.cluster.ClustersDeadlockTest.ClustersHostMapperThread();
            }
        };
        doLoadTest(new org.apache.ambari.server.state.cluster.ClustersDeadlockTest.ClusterReaderThreadFactory(), clustersHostMapperThreadFactory, org.apache.ambari.server.state.cluster.ClustersDeadlockTest.NUMBER_OF_THREADS, writerStoppedSignal, readerStoppedSignal);
        junit.framework.Assert.assertEquals(org.apache.ambari.server.state.cluster.ClustersDeadlockTest.NUMBER_OF_THREADS * org.apache.ambari.server.state.cluster.ClustersDeadlockTest.NUMBER_OF_HOSTS, clusters.getHostsForCluster(org.apache.ambari.server.state.cluster.ClustersDeadlockTest.CLUSTER_NAME).size());
    }

    @org.junit.Test(timeout = 40000)
    public void testDeadlockWhileMappingHostsWithExistingServices() throws java.lang.Exception {
        com.google.inject.Provider<org.apache.ambari.server.state.cluster.ClustersDeadlockTest.ClustersHostAndComponentMapperThread> clustersHostAndComponentMapperThreadFactory = new com.google.inject.Provider<org.apache.ambari.server.state.cluster.ClustersDeadlockTest.ClustersHostAndComponentMapperThread>() {
            @java.lang.Override
            public org.apache.ambari.server.state.cluster.ClustersDeadlockTest.ClustersHostAndComponentMapperThread get() {
                return new org.apache.ambari.server.state.cluster.ClustersDeadlockTest.ClustersHostAndComponentMapperThread();
            }
        };
        doLoadTest(new org.apache.ambari.server.state.cluster.ClustersDeadlockTest.ClusterReaderThreadFactory(), clustersHostAndComponentMapperThreadFactory, org.apache.ambari.server.state.cluster.ClustersDeadlockTest.NUMBER_OF_THREADS, writerStoppedSignal, readerStoppedSignal);
    }

    @org.junit.Test(timeout = 40000)
    public void testDeadlockWhileUnmappingHosts() throws java.lang.Exception {
        com.google.inject.Provider<org.apache.ambari.server.state.cluster.ClustersDeadlockTest.ClustersHostUnMapperThread> clustersHostUnMapperThreadFactory = new com.google.inject.Provider<org.apache.ambari.server.state.cluster.ClustersDeadlockTest.ClustersHostUnMapperThread>() {
            @java.lang.Override
            public org.apache.ambari.server.state.cluster.ClustersDeadlockTest.ClustersHostUnMapperThread get() {
                return new org.apache.ambari.server.state.cluster.ClustersDeadlockTest.ClustersHostUnMapperThread();
            }
        };
        doLoadTest(new org.apache.ambari.server.state.cluster.ClustersDeadlockTest.ClusterReaderThreadFactory(), clustersHostUnMapperThreadFactory, org.apache.ambari.server.state.cluster.ClustersDeadlockTest.NUMBER_OF_THREADS, writerStoppedSignal, readerStoppedSignal);
        junit.framework.Assert.assertEquals(0, clusters.getHostsForCluster(org.apache.ambari.server.state.cluster.ClustersDeadlockTest.CLUSTER_NAME).size());
    }

    private final class ClusterReaderThreadFactory implements com.google.inject.Provider<org.apache.ambari.server.state.cluster.ClustersDeadlockTest.ClusterReaderThread> {
        @java.lang.Override
        public org.apache.ambari.server.state.cluster.ClustersDeadlockTest.ClusterReaderThread get() {
            return new org.apache.ambari.server.state.cluster.ClustersDeadlockTest.ClusterReaderThread();
        }
    }

    private final class ClusterReaderThread extends java.lang.Thread {
        @java.lang.Override
        public void run() {
            try {
                while (true) {
                    if (writerStoppedSignal.getCount() == 0) {
                        break;
                    }
                    cluster.convertToResponse();
                    java.lang.Thread.sleep(10);
                } 
            } catch (java.lang.Exception exception) {
                throw new java.lang.RuntimeException(exception);
            } finally {
                readerStoppedSignal.countDown();
            }
        }
    }

    private final class ClustersHostMapperThread extends java.lang.Thread {
        @java.lang.Override
        public void run() {
            try {
                for (int i = 0; i < org.apache.ambari.server.state.cluster.ClustersDeadlockTest.NUMBER_OF_HOSTS; i++) {
                    java.lang.String hostName = "c64-" + hostNameCounter.getAndIncrement();
                    clusters.addHost(hostName);
                    setOsFamily(clusters.getHost(hostName), "redhat", "6.4");
                    clusters.mapHostToCluster(hostName, org.apache.ambari.server.state.cluster.ClustersDeadlockTest.CLUSTER_NAME);
                    java.lang.Thread.sleep(10);
                }
            } catch (java.lang.Exception exception) {
                throw new java.lang.RuntimeException(exception);
            }
        }
    }

    private final class ClustersHostAndComponentMapperThread extends java.lang.Thread {
        @java.lang.Override
        public void run() {
            try {
                for (int i = 0; i < org.apache.ambari.server.state.cluster.ClustersDeadlockTest.NUMBER_OF_HOSTS; i++) {
                    java.lang.String hostName = "c64-" + hostNameCounter.getAndIncrement();
                    clusters.addHost(hostName);
                    setOsFamily(clusters.getHost(hostName), "redhat", "6.4");
                    clusters.mapHostToCluster(hostName, org.apache.ambari.server.state.cluster.ClustersDeadlockTest.CLUSTER_NAME);
                    createNewServiceComponentHost("HDFS", "DATANODE", hostName);
                    java.lang.Thread.sleep(10);
                }
            } catch (java.lang.Exception exception) {
                throw new java.lang.RuntimeException(exception);
            }
        }
    }

    private final class ClustersHostUnMapperThread extends java.lang.Thread {
        @java.lang.Override
        public void run() {
            java.util.List<java.lang.String> hostNames = new java.util.ArrayList<>(100);
            try {
                for (int i = 0; i < org.apache.ambari.server.state.cluster.ClustersDeadlockTest.NUMBER_OF_HOSTS; i++) {
                    java.lang.String hostName = "c64-" + hostNameCounter.getAndIncrement();
                    hostNames.add(hostName);
                    clusters.addHost(hostName);
                    setOsFamily(clusters.getHost(hostName), "redhat", "6.4");
                    clusters.mapHostToCluster(hostName, org.apache.ambari.server.state.cluster.ClustersDeadlockTest.CLUSTER_NAME);
                }
                for (java.lang.String hostName : hostNames) {
                    clusters.unmapHostFromCluster(hostName, org.apache.ambari.server.state.cluster.ClustersDeadlockTest.CLUSTER_NAME);
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

    private org.apache.ambari.server.state.Service installService(java.lang.String serviceName) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.Service service = null;
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion = helper.getOrCreateRepositoryVersion(stackId, REPO_VERSION);
        try {
            service = cluster.getService(serviceName);
        } catch (org.apache.ambari.server.ServiceNotFoundException e) {
            service = serviceFactory.createNew(cluster, serviceName, repositoryVersion);
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

    private org.apache.ambari.server.state.ServiceComponentHost createNewServiceComponentHost(java.lang.String svc, java.lang.String svcComponent, java.lang.String hostName) throws org.apache.ambari.server.AmbariException {
        junit.framework.Assert.assertNotNull(cluster.getConfigGroups());
        org.apache.ambari.server.state.Service s = installService(svc);
        org.apache.ambari.server.state.ServiceComponent sc = addServiceComponent(s, svcComponent);
        org.apache.ambari.server.state.ServiceComponentHost sch = serviceComponentHostFactory.createNew(sc, hostName);
        sc.addServiceComponentHost(sch);
        sch.setDesiredState(org.apache.ambari.server.state.State.INSTALLED);
        sch.setState(org.apache.ambari.server.state.State.INSTALLED);
        return sch;
    }

    private class MockModule implements com.google.inject.Module {
        @java.lang.Override
        public void configure(com.google.inject.Binder binder) {
            binder.bind(org.apache.ambari.server.events.listeners.upgrade.HostVersionOutOfSyncListener.class).toInstance(org.easymock.EasyMock.createNiceMock(org.apache.ambari.server.events.listeners.upgrade.HostVersionOutOfSyncListener.class));
        }
    }
}