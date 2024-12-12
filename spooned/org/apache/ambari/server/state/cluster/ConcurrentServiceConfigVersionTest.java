package org.apache.ambari.server.state.cluster;
import org.easymock.EasyMock;
public class ConcurrentServiceConfigVersionTest {
    private static final int NUMBER_OF_SERVICE_CONFIG_VERSIONS = 10;

    private static final int NUMBER_OF_THREADS = 2;

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

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.ServiceConfigDAO serviceConfigDAO;

    private org.apache.ambari.server.state.StackId stackId = new org.apache.ambari.server.state.StackId("HDP-0.1");

    private org.apache.ambari.server.state.Cluster cluster;

    private org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion;

    @org.junit.Before
    public void setup() throws java.lang.Exception {
        injector = com.google.inject.Guice.createInjector(com.google.inject.util.Modules.override(new org.apache.ambari.server.orm.InMemoryDefaultTestModule()).with(new org.apache.ambari.server.state.cluster.ConcurrentServiceConfigVersionTest.MockModule()));
        injector.getInstance(org.apache.ambari.server.orm.GuiceJpaInitializer.class);
        injector.injectMembers(this);
        helper.createStack(stackId);
        clusters.addCluster("c1", stackId);
        cluster = clusters.getCluster("c1");
        repositoryVersion = helper.getOrCreateRepositoryVersion(stackId, stackId.getStackVersion());
        java.lang.String hostName = "c6401.ambari.apache.org";
        clusters.addHost(hostName);
        setOsFamily(clusters.getHost(hostName), "redhat", "6.4");
        clusters.mapHostToCluster(hostName, "c1");
        org.apache.ambari.server.state.Service service = installService("HDFS");
        addServiceComponent(service, "NAMENODE");
        addServiceComponent(service, "DATANODE");
        createNewServiceComponentHost("HDFS", "NAMENODE", hostName);
        createNewServiceComponentHost("HDFS", "DATANODE", hostName);
    }

    @org.junit.After
    public void teardown() throws org.apache.ambari.server.AmbariException, java.sql.SQLException {
        org.apache.ambari.server.H2DatabaseCleaner.clearDatabaseAndStopPersistenceService(injector);
    }

    @org.junit.Test
    public void testConcurrentServiceConfigVersions() throws java.lang.Exception {
        long nextVersion = serviceConfigDAO.findNextServiceConfigVersion(cluster.getClusterId(), "HDFS");
        org.junit.Assert.assertEquals(nextVersion, 1);
        java.util.List<java.lang.Thread> threads = new java.util.ArrayList<>();
        for (int i = 0; i < org.apache.ambari.server.state.cluster.ConcurrentServiceConfigVersionTest.NUMBER_OF_THREADS; i++) {
            java.lang.Thread thread = new org.apache.ambari.server.state.cluster.ConcurrentServiceConfigVersionTest.ConcurrentServiceConfigThread(cluster);
            threads.add(thread);
            thread.start();
        }
        for (java.lang.Thread thread : threads) {
            thread.join();
        }
        long maxVersion = org.apache.ambari.server.state.cluster.ConcurrentServiceConfigVersionTest.NUMBER_OF_THREADS * org.apache.ambari.server.state.cluster.ConcurrentServiceConfigVersionTest.NUMBER_OF_SERVICE_CONFIG_VERSIONS;
        nextVersion = serviceConfigDAO.findNextServiceConfigVersion(cluster.getClusterId(), "HDFS");
        org.junit.Assert.assertEquals(maxVersion + 1, nextVersion);
    }

    private static final class ConcurrentServiceConfigThread extends java.lang.Thread {
        private org.apache.ambari.server.state.Cluster cluster = null;

        private ConcurrentServiceConfigThread(org.apache.ambari.server.state.Cluster cluster) {
            this.cluster = cluster;
        }

        @java.lang.Override
        public void run() {
            try {
                for (int i = 0; i < org.apache.ambari.server.state.cluster.ConcurrentServiceConfigVersionTest.NUMBER_OF_SERVICE_CONFIG_VERSIONS; i++) {
                    org.apache.ambari.server.controller.ServiceConfigVersionResponse response = cluster.createServiceConfigVersion("HDFS", null, (getName() + "-serviceConfig") + i, null);
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