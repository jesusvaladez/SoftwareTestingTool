package org.apache.ambari.server.state.cluster;
import org.easymock.EasyMock;
public class ClusterDeadlockTest {
    private static final int NUMBER_OF_HOSTS = 40;

    private static final int NUMBER_OF_THREADS = 5;

    private final java.util.concurrent.atomic.AtomicInteger hostNameCounter = new java.util.concurrent.atomic.AtomicInteger(0);

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

    private java.lang.String REPO_VERSION = "0.1-1234";

    private org.apache.ambari.server.state.Cluster cluster;

    private java.util.List<java.lang.String> hostNames = new java.util.ArrayList<>(org.apache.ambari.server.state.cluster.ClusterDeadlockTest.NUMBER_OF_HOSTS);

    @org.junit.Before
    public void setup() throws java.lang.Exception {
        injector = com.google.inject.Guice.createInjector(com.google.inject.util.Modules.override(new org.apache.ambari.server.orm.InMemoryDefaultTestModule()).with(new org.apache.ambari.server.state.cluster.ClusterDeadlockTest.MockModule()));
        injector.getInstance(org.apache.ambari.server.orm.GuiceJpaInitializer.class);
        injector.injectMembers(this);
        helper.createStack(stackId);
        clusters.addCluster("c1", stackId);
        cluster = clusters.getCluster("c1");
        helper.getOrCreateRepositoryVersion(stackId, stackId.getStackVersion());
        org.apache.ambari.server.state.Config config1 = configFactory.createNew(cluster, "test-type1", "version1", new java.util.HashMap<>(), new java.util.HashMap<>());
        org.apache.ambari.server.state.Config config2 = configFactory.createNew(cluster, "test-type2", "version1", new java.util.HashMap<>(), new java.util.HashMap<>());
        cluster.addDesiredConfig("test user", new java.util.HashSet<>(java.util.Arrays.asList(config1, config2)));
        for (int i = 0; i < org.apache.ambari.server.state.cluster.ClusterDeadlockTest.NUMBER_OF_HOSTS; i++) {
            java.lang.String hostName = "c64-" + i;
            hostNames.add(hostName);
            clusters.addHost(hostName);
            setOsFamily(clusters.getHost(hostName), "redhat", "6.4");
            clusters.mapHostToCluster(hostName, "c1");
        }
        org.apache.ambari.server.state.Service service = installService("HDFS");
        addServiceComponent(service, "NAMENODE");
        addServiceComponent(service, "DATANODE");
    }

    @org.junit.After
    public void teardown() throws org.apache.ambari.server.AmbariException, java.sql.SQLException {
        org.apache.ambari.server.H2DatabaseCleaner.clearDatabaseAndStopPersistenceService(injector);
    }

    @org.junit.Test
    public void testDeadlockBetweenImplementations() throws java.lang.Exception {
        org.apache.ambari.server.state.Service service = cluster.getService("HDFS");
        org.apache.ambari.server.state.ServiceComponent nameNodeComponent = service.getServiceComponent("NAMENODE");
        org.apache.ambari.server.state.ServiceComponent dataNodeComponent = service.getServiceComponent("DATANODE");
        org.apache.ambari.server.state.ServiceComponentHost nameNodeSCH = createNewServiceComponentHost("HDFS", "NAMENODE", "c64-0");
        org.apache.ambari.server.state.ServiceComponentHost dataNodeSCH = createNewServiceComponentHost("HDFS", "DATANODE", "c64-0");
        java.util.List<java.lang.Thread> threads = new java.util.ArrayList<>();
        for (int i = 0; i < org.apache.ambari.server.state.cluster.ClusterDeadlockTest.NUMBER_OF_THREADS; i++) {
            org.apache.ambari.server.state.cluster.ClusterDeadlockTest.DeadlockExerciserThread thread = new org.apache.ambari.server.state.cluster.ClusterDeadlockTest.DeadlockExerciserThread();
            thread.setCluster(cluster);
            thread.setService(service);
            thread.setDataNodeComponent(dataNodeComponent);
            thread.setNameNodeComponent(nameNodeComponent);
            thread.setNameNodeSCH(nameNodeSCH);
            thread.setDataNodeSCH(dataNodeSCH);
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

    @org.junit.Test
    public void testAddingHostComponentsWhileReading() throws java.lang.Exception {
        org.apache.ambari.server.state.Service service = cluster.getService("HDFS");
        org.apache.ambari.server.state.ServiceComponent nameNodeComponent = service.getServiceComponent("NAMENODE");
        org.apache.ambari.server.state.ServiceComponent dataNodeComponent = service.getServiceComponent("DATANODE");
        java.util.List<java.lang.Thread> threads = new java.util.ArrayList<>();
        for (int i = 0; i < 5; i++) {
            org.apache.ambari.server.state.cluster.ClusterDeadlockTest.ServiceComponentReaderWriterThread thread = new org.apache.ambari.server.state.cluster.ClusterDeadlockTest.ServiceComponentReaderWriterThread();
            thread.setDataNodeComponent(dataNodeComponent);
            thread.setNameNodeComponent(nameNodeComponent);
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

    @org.junit.Test
    public void testDeadlockWhileRestartingComponents() throws java.lang.Exception {
        java.util.List<org.apache.ambari.server.state.ServiceComponentHost> serviceComponentHosts = new java.util.ArrayList<>();
        for (java.lang.String hostName : hostNames) {
            serviceComponentHosts.add(createNewServiceComponentHost("HDFS", "NAMENODE", hostName));
            serviceComponentHosts.add(createNewServiceComponentHost("HDFS", "DATANODE", hostName));
        }
        java.util.List<java.lang.Thread> threads = new java.util.ArrayList<>();
        for (int i = 0; i < org.apache.ambari.server.state.cluster.ClusterDeadlockTest.NUMBER_OF_THREADS; i++) {
            org.apache.ambari.server.state.cluster.ClusterDeadlockTest.ClusterReaderThread clusterReaderThread = new org.apache.ambari.server.state.cluster.ClusterDeadlockTest.ClusterReaderThread();
            org.apache.ambari.server.state.cluster.ClusterDeadlockTest.ClusterWriterThread clusterWriterThread = new org.apache.ambari.server.state.cluster.ClusterDeadlockTest.ClusterWriterThread();
            org.apache.ambari.server.state.cluster.ClusterDeadlockTest.ServiceComponentRestartThread schWriterThread = new org.apache.ambari.server.state.cluster.ClusterDeadlockTest.ServiceComponentRestartThread(serviceComponentHosts);
            threads.add(clusterReaderThread);
            threads.add(clusterWriterThread);
            threads.add(schWriterThread);
            clusterReaderThread.start();
            clusterWriterThread.start();
            schWriterThread.start();
        }
        org.apache.ambari.server.testing.DeadlockWarningThread wt = new org.apache.ambari.server.testing.DeadlockWarningThread(threads, 20, 1000);
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

    @org.junit.Test
    public void testDeadlockWithConfigsUpdate() throws java.lang.Exception {
        java.util.List<java.lang.Thread> threads = new java.util.ArrayList<>();
        for (int i = 0; i < org.apache.ambari.server.state.cluster.ClusterDeadlockTest.NUMBER_OF_THREADS; i++) {
            org.apache.ambari.server.state.cluster.ClusterDeadlockTest.ClusterDesiredConfigsReaderThread readerThread = null;
            for (int j = 0; j < org.apache.ambari.server.state.cluster.ClusterDeadlockTest.NUMBER_OF_THREADS; j++) {
                readerThread = new org.apache.ambari.server.state.cluster.ClusterDeadlockTest.ClusterDesiredConfigsReaderThread();
                threads.add(readerThread);
            }
            for (org.apache.ambari.server.state.Config config : cluster.getAllConfigs()) {
                org.apache.ambari.server.state.cluster.ClusterDeadlockTest.ConfigUpdaterThread configUpdaterThread = new org.apache.ambari.server.state.cluster.ClusterDeadlockTest.ConfigUpdaterThread(config);
                threads.add(configUpdaterThread);
            }
        }
        for (java.lang.Thread thread : threads) {
            thread.start();
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

    private final class ClusterDesiredConfigsReaderThread extends java.lang.Thread {
        @java.lang.Override
        public void run() {
            for (int i = 0; i < 1000; i++) {
                cluster.getDesiredConfigs();
            }
        }
    }

    private final class ConfigUpdaterThread extends java.lang.Thread {
        private org.apache.ambari.server.state.Config config;

        public ConfigUpdaterThread(org.apache.ambari.server.state.Config config) {
            this.config = config;
        }

        @java.lang.Override
        public void run() {
            for (int i = 0; i < 300; i++) {
                config.save();
            }
        }
    }

    private final class ClusterReaderThread extends java.lang.Thread {
        @java.lang.Override
        public void run() {
            try {
                for (int i = 0; i < 1000; i++) {
                    cluster.convertToResponse();
                    java.lang.Thread.sleep(10);
                }
            } catch (java.lang.Exception exception) {
                throw new java.lang.RuntimeException(exception);
            }
        }
    }

    private final class ClusterWriterThread extends java.lang.Thread {
        @java.lang.Override
        public void run() {
            try {
                for (int i = 0; i < 1500; i++) {
                    cluster.setDesiredStackVersion(stackId);
                    java.lang.Thread.sleep(10);
                }
            } catch (java.lang.Exception exception) {
                throw new java.lang.RuntimeException(exception);
            }
        }
    }

    private final class ServiceComponentRestartThread extends java.lang.Thread {
        private java.util.List<org.apache.ambari.server.state.ServiceComponentHost> serviceComponentHosts;

        private ServiceComponentRestartThread(java.util.List<org.apache.ambari.server.state.ServiceComponentHost> serviceComponentHosts) {
            this.serviceComponentHosts = serviceComponentHosts;
        }

        @java.lang.Override
        public void run() {
            try {
                for (int i = 0; i < 1000; i++) {
                    for (org.apache.ambari.server.state.ServiceComponentHost serviceComponentHost : serviceComponentHosts) {
                        serviceComponentHost.setRestartRequired(true);
                    }
                }
            } catch (java.lang.Exception exception) {
                throw new java.lang.RuntimeException(exception);
            }
        }
    }

    private final class ServiceComponentReaderWriterThread extends java.lang.Thread {
        private org.apache.ambari.server.state.ServiceComponent nameNodeComponent;

        private org.apache.ambari.server.state.ServiceComponent dataNodeComponent;

        public void setNameNodeComponent(org.apache.ambari.server.state.ServiceComponent nameNodeComponent) {
            this.nameNodeComponent = nameNodeComponent;
        }

        public void setDataNodeComponent(org.apache.ambari.server.state.ServiceComponent dataNodeComponent) {
            this.dataNodeComponent = dataNodeComponent;
        }

        @java.lang.Override
        public void run() {
            try {
                for (int i = 0; i < 15; i++) {
                    int hostNumeric = hostNameCounter.getAndIncrement();
                    nameNodeComponent.convertToResponse();
                    createNewServiceComponentHost("HDFS", "NAMENODE", "c64-" + hostNumeric);
                    dataNodeComponent.convertToResponse();
                    createNewServiceComponentHost("HDFS", "DATANODE", "c64-" + hostNumeric);
                    java.lang.Thread.sleep(10);
                }
            } catch (java.lang.Exception exception) {
                throw new java.lang.RuntimeException(exception);
            }
        }
    }

    private static final class DeadlockExerciserThread extends java.lang.Thread {
        private org.apache.ambari.server.state.Cluster cluster;

        private org.apache.ambari.server.state.Service service;

        private org.apache.ambari.server.state.ServiceComponent nameNodeComponent;

        private org.apache.ambari.server.state.ServiceComponent dataNodeComponent;

        private org.apache.ambari.server.state.ServiceComponentHost nameNodeSCH;

        private org.apache.ambari.server.state.ServiceComponentHost dataNodeSCH;

        public void setCluster(org.apache.ambari.server.state.Cluster cluster) {
            this.cluster = cluster;
        }

        public void setService(org.apache.ambari.server.state.Service service) {
            this.service = service;
        }

        public void setNameNodeComponent(org.apache.ambari.server.state.ServiceComponent nameNodeComponent) {
            this.nameNodeComponent = nameNodeComponent;
        }

        public void setDataNodeComponent(org.apache.ambari.server.state.ServiceComponent dataNodeComponent) {
            this.dataNodeComponent = dataNodeComponent;
        }

        public void setNameNodeSCH(org.apache.ambari.server.state.ServiceComponentHost nameNodeSCH) {
            this.nameNodeSCH = nameNodeSCH;
        }

        public void setDataNodeSCH(org.apache.ambari.server.state.ServiceComponentHost dataNodeSCH) {
            this.dataNodeSCH = dataNodeSCH;
        }

        @java.lang.Override
        public void run() {
            try {
                for (int i = 0; i < 10; i++) {
                    cluster.convertToResponse();
                    service.convertToResponse();
                    nameNodeComponent.convertToResponse();
                    dataNodeComponent.convertToResponse();
                    nameNodeSCH.convertToResponse(null);
                    dataNodeSCH.convertToResponse(null);
                    cluster.setProvisioningState(org.apache.ambari.server.state.State.INIT);
                    service.setMaintenanceState(org.apache.ambari.server.state.MaintenanceState.OFF);
                    nameNodeComponent.setDesiredState(org.apache.ambari.server.state.State.STARTED);
                    dataNodeComponent.setDesiredState(org.apache.ambari.server.state.State.INSTALLED);
                    nameNodeSCH.setState(org.apache.ambari.server.state.State.STARTED);
                    dataNodeSCH.setState(org.apache.ambari.server.state.State.INSTALLED);
                    java.lang.Thread.sleep(100);
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
        return sch;
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

    private class MockModule implements com.google.inject.Module {
        @java.lang.Override
        public void configure(com.google.inject.Binder binder) {
            binder.bind(org.apache.ambari.server.events.listeners.upgrade.HostVersionOutOfSyncListener.class).toInstance(org.easymock.EasyMock.createNiceMock(org.apache.ambari.server.events.listeners.upgrade.HostVersionOutOfSyncListener.class));
        }
    }
}