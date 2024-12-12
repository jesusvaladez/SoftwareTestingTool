package org.apache.ambari.server.events.listeners.upgrade;
import com.google.inject.persist.UnitOfWork;
public class HostVersionOutOfSyncListenerTest {
    private final java.lang.String stackId = "HDP-2.2.0";

    private final java.lang.String yetAnotherStackId = "HDP-2.1.1";

    private final java.lang.String CURRENT_VERSION = "2.2.0-2086";

    private com.google.inject.Injector injector;

    @com.google.inject.Inject
    private org.apache.ambari.server.state.Clusters clusters;

    private org.apache.ambari.server.state.Cluster c1;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.OrmTestHelper helper;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.HostVersionDAO hostVersionDAO;

    @com.google.inject.Inject
    private org.apache.ambari.server.state.ServiceComponentHostFactory serviceComponentHostFactory;

    @com.google.inject.Inject
    private org.apache.ambari.server.events.publishers.AmbariEventPublisher m_eventPublisher;

    @org.junit.Before
    public void setup() throws java.lang.Exception {
        injector = com.google.inject.Guice.createInjector(new org.apache.ambari.server.orm.InMemoryDefaultTestModule());
        injector.getInstance(org.apache.ambari.server.orm.GuiceJpaInitializer.class);
        org.apache.ambari.server.utils.EventBusSynchronizer.synchronizeAmbariEventPublisher(injector);
        injector.injectMembers(this);
        org.apache.ambari.server.state.StackId stackId = new org.apache.ambari.server.state.StackId(this.stackId);
        helper.createStack(stackId);
        clusters.addCluster("c1", stackId);
        c1 = clusters.getCluster("c1");
        addHost("h1");
        helper.getOrCreateRepositoryVersion(stackId, stackId.getStackVersion());
        clusters.mapHostToCluster("h1", "c1");
    }

    @org.junit.After
    public void teardown() throws org.apache.ambari.server.AmbariException, java.sql.SQLException {
        org.apache.ambari.server.H2DatabaseCleaner.clearDatabaseAndStopPersistenceService(injector);
    }

    private org.apache.ambari.server.orm.entities.RepositoryVersionEntity createClusterAndHosts(java.lang.String INSTALLED_VERSION, org.apache.ambari.server.state.StackId stackId) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.Host h1 = clusters.getHost("h1");
        h1.setState(org.apache.ambari.server.state.HostState.HEALTHY);
        addHost("h2");
        clusters.mapHostToCluster("h2", "c1");
        addHost("h3");
        clusters.mapHostToCluster("h3", "c1");
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersionEntity = helper.getOrCreateRepositoryVersion(stackId, INSTALLED_VERSION);
        c1.setCurrentStackVersion(stackId);
        assertRepoVersionState(INSTALLED_VERSION, org.apache.ambari.server.state.RepositoryVersionState.INSTALLING);
        assertRepoVersionState(CURRENT_VERSION, org.apache.ambari.server.state.RepositoryVersionState.CURRENT);
        java.util.List<java.lang.String> hostList = new java.util.ArrayList<>();
        hostList.add("h1");
        hostList.add("h2");
        hostList.add("h3");
        java.util.Map<java.lang.String, java.util.List<java.lang.Integer>> zkTopology = new java.util.HashMap<>();
        java.util.List<java.lang.Integer> zkServerHosts = java.util.Arrays.asList(0, 1, 2);
        zkTopology.put("ZOOKEEPER_SERVER", new java.util.ArrayList<>(zkServerHosts));
        addService(c1, hostList, zkTopology, "ZOOKEEPER", repositoryVersionEntity);
        helper.createHostVersion("h1", repositoryVersionEntity, org.apache.ambari.server.state.RepositoryVersionState.INSTALLED);
        helper.createHostVersion("h2", repositoryVersionEntity, org.apache.ambari.server.state.RepositoryVersionState.INSTALLED);
        helper.createHostVersion("h3", repositoryVersionEntity, org.apache.ambari.server.state.RepositoryVersionState.INSTALLED);
        assertRepoVersionState(INSTALLED_VERSION, org.apache.ambari.server.state.RepositoryVersionState.INSTALLED);
        assertRepoVersionState(CURRENT_VERSION, org.apache.ambari.server.state.RepositoryVersionState.CURRENT);
        java.util.List<org.apache.ambari.server.orm.entities.HostVersionEntity> h2Versions = hostVersionDAO.findAll();
        for (org.apache.ambari.server.orm.entities.HostVersionEntity hostVersionEntity : h2Versions) {
            if (hostVersionEntity.getRepositoryVersion().getVersion().equals(INSTALLED_VERSION)) {
                org.junit.Assert.assertEquals(hostVersionEntity.getState(), org.apache.ambari.server.state.RepositoryVersionState.INSTALLED);
            }
        }
        return repositoryVersionEntity;
    }

    private org.apache.ambari.server.orm.entities.RepositoryVersionEntity addRepoVersion(java.lang.String INSTALLED_VERSION, org.apache.ambari.server.state.StackId stackId) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersionEntity = helper.getOrCreateRepositoryVersion(stackId, INSTALLED_VERSION);
        helper.createHostVersion("h1", repositoryVersionEntity, org.apache.ambari.server.state.RepositoryVersionState.INSTALLED);
        assertRepoVersionState(INSTALLED_VERSION, org.apache.ambari.server.state.RepositoryVersionState.INSTALLED);
        java.util.List<org.apache.ambari.server.orm.entities.HostVersionEntity> h2Versions = hostVersionDAO.findAll();
        for (org.apache.ambari.server.orm.entities.HostVersionEntity hostVersionEntity : h2Versions) {
            if (hostVersionEntity.getRepositoryVersion().getVersion().equals(INSTALLED_VERSION)) {
                org.junit.Assert.assertEquals(hostVersionEntity.getState(), org.apache.ambari.server.state.RepositoryVersionState.INSTALLED);
            }
        }
        return repositoryVersionEntity;
    }

    @org.junit.Test
    public void testOnServiceEvent() throws org.apache.ambari.server.AmbariException {
        java.lang.String INSTALLED_VERSION = "2.2.0-1000";
        java.lang.String INSTALLED_VERSION_2 = "2.1.1-2000";
        org.apache.ambari.server.state.StackId stackId = new org.apache.ambari.server.state.StackId(this.stackId);
        org.apache.ambari.server.state.StackId yaStackId = new org.apache.ambari.server.state.StackId(yetAnotherStackId);
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion = createClusterAndHosts(INSTALLED_VERSION, stackId);
        addRepoVersion(INSTALLED_VERSION_2, yaStackId);
        assertRepoVersionState(INSTALLED_VERSION, org.apache.ambari.server.state.RepositoryVersionState.INSTALLED);
        assertRepoVersionState(INSTALLED_VERSION_2, org.apache.ambari.server.state.RepositoryVersionState.INSTALLED);
        assertRepoVersionState(CURRENT_VERSION, org.apache.ambari.server.state.RepositoryVersionState.CURRENT);
        java.util.List<java.lang.String> hostList = new java.util.ArrayList<>();
        hostList.add("h1");
        hostList.add("h2");
        hostList.add("h3");
        java.util.Map<java.lang.String, java.util.List<java.lang.Integer>> hdfsTopology = new java.util.HashMap<>();
        hdfsTopology.put("NAMENODE", java.util.Collections.singletonList(0));
        hdfsTopology.put("SECONDARY_NAMENODE", java.util.Collections.singletonList(1));
        java.util.List<java.lang.Integer> datanodeHosts = java.util.Arrays.asList(0, 1);
        hdfsTopology.put("DATANODE", new java.util.ArrayList<>(datanodeHosts));
        addService(c1, hostList, hdfsTopology, "HDFS", repositoryVersion);
        java.util.Set<java.lang.String> changedHosts = new java.util.HashSet<>();
        changedHosts.add("h1");
        changedHosts.add("h2");
        java.util.List<org.apache.ambari.server.orm.entities.HostVersionEntity> hostVersions = hostVersionDAO.findAll();
        for (org.apache.ambari.server.orm.entities.HostVersionEntity hostVersionEntity : hostVersions) {
            if (hostVersionEntity.getRepositoryVersion().getVersion().equals(INSTALLED_VERSION) || hostVersionEntity.getRepositoryVersion().getVersion().equals(INSTALLED_VERSION_2)) {
                if (changedHosts.contains(hostVersionEntity.getHostName())) {
                    org.junit.Assert.assertEquals(hostVersionEntity.getState(), org.apache.ambari.server.state.RepositoryVersionState.OUT_OF_SYNC);
                } else {
                    org.junit.Assert.assertEquals(hostVersionEntity.getState(), org.apache.ambari.server.state.RepositoryVersionState.INSTALLED);
                }
            }
        }
        assertRepoVersionState(INSTALLED_VERSION, org.apache.ambari.server.state.RepositoryVersionState.OUT_OF_SYNC);
        assertRepoVersionState(INSTALLED_VERSION_2, org.apache.ambari.server.state.RepositoryVersionState.OUT_OF_SYNC);
        assertRepoVersionState(CURRENT_VERSION, org.apache.ambari.server.state.RepositoryVersionState.CURRENT);
    }

    @org.junit.Test
    public void testOnServiceEvent_component_does_not_advertise_version() throws org.apache.ambari.server.AmbariException {
        java.lang.String INSTALLED_VERSION = "2.2.0-1000";
        org.apache.ambari.server.state.StackId stackId = new org.apache.ambari.server.state.StackId(this.stackId);
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion = createClusterAndHosts(INSTALLED_VERSION, stackId);
        java.util.List<java.lang.String> hostList = new java.util.ArrayList<>();
        hostList.add("h1");
        hostList.add("h2");
        hostList.add("h3");
        java.util.Map<java.lang.String, java.util.List<java.lang.Integer>> hdfsTopology = new java.util.HashMap<>();
        hdfsTopology.put("GANGLIA_SERVER", java.util.Collections.singletonList(0));
        java.util.List<java.lang.Integer> monitorHosts = java.util.Arrays.asList(0, 1);
        hdfsTopology.put("GANGLIA_MONITOR", new java.util.ArrayList<>(monitorHosts));
        addService(c1, hostList, hdfsTopology, "GANGLIA", repositoryVersion);
        java.util.Set<java.lang.String> changedHosts = new java.util.HashSet<>();
        changedHosts.add("h1");
        changedHosts.add("h2");
        java.util.List<org.apache.ambari.server.orm.entities.HostVersionEntity> hostVersions = hostVersionDAO.findAll();
        assertRepoVersionState(INSTALLED_VERSION, org.apache.ambari.server.state.RepositoryVersionState.INSTALLED);
        for (org.apache.ambari.server.orm.entities.HostVersionEntity hostVersionEntity : hostVersions) {
            if (hostVersionEntity.getRepositoryVersion().getVersion().equals(INSTALLED_VERSION)) {
                org.junit.Assert.assertEquals(hostVersionEntity.getState(), org.apache.ambari.server.state.RepositoryVersionState.INSTALLED);
            }
        }
    }

    @org.junit.Test
    public void testOnServiceComponentEvent() throws org.apache.ambari.server.AmbariException {
        java.lang.String INSTALLED_VERSION = "2.2.0-1000";
        java.lang.String INSTALLED_VERSION_2 = "2.1.1-2000";
        org.apache.ambari.server.state.StackId stackId = new org.apache.ambari.server.state.StackId(this.stackId);
        org.apache.ambari.server.state.StackId yaStackId = new org.apache.ambari.server.state.StackId(yetAnotherStackId);
        createClusterAndHosts(INSTALLED_VERSION, stackId);
        addRepoVersion(INSTALLED_VERSION_2, yaStackId);
        assertRepoVersionState(INSTALLED_VERSION, org.apache.ambari.server.state.RepositoryVersionState.INSTALLED);
        assertRepoVersionState(INSTALLED_VERSION_2, org.apache.ambari.server.state.RepositoryVersionState.INSTALLED);
        java.util.List<java.lang.String> hostList = new java.util.ArrayList<>();
        hostList.add("h1");
        hostList.add("h2");
        hostList.add("h3");
        addServiceComponent(c1, hostList, "ZOOKEEPER", "ZOOKEEPER_CLIENT");
        java.util.Set<java.lang.String> changedHosts = new java.util.HashSet<>();
        changedHosts.add("h1");
        changedHosts.add("h2");
        changedHosts.add("h3");
        assertRepoVersionState(INSTALLED_VERSION, org.apache.ambari.server.state.RepositoryVersionState.OUT_OF_SYNC);
        java.util.List<org.apache.ambari.server.orm.entities.HostVersionEntity> hostVersions = hostVersionDAO.findAll();
        for (org.apache.ambari.server.orm.entities.HostVersionEntity hostVersionEntity : hostVersions) {
            org.apache.ambari.server.orm.entities.RepositoryVersionEntity repoVersion = hostVersionEntity.getRepositoryVersion();
            if (repoVersion.getVersion().equals(INSTALLED_VERSION_2)) {
                org.junit.Assert.assertEquals(org.apache.ambari.server.state.RepositoryVersionState.INSTALLED, hostVersionEntity.getState());
            } else if (repoVersion.getVersion().equals(INSTALLED_VERSION)) {
                org.junit.Assert.assertTrue(changedHosts.contains(hostVersionEntity.getHostName()));
                org.junit.Assert.assertEquals(org.apache.ambari.server.state.RepositoryVersionState.OUT_OF_SYNC, hostVersionEntity.getState());
            }
        }
    }

    @org.junit.Test
    public void testOnHostEvent() throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.Host h1 = clusters.getHost("h1");
        h1.setState(org.apache.ambari.server.state.HostState.HEALTHY);
        org.apache.ambari.server.state.StackId stackId = new org.apache.ambari.server.state.StackId(this.stackId);
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersionEntity = helper.getOrCreateRepositoryVersion(stackId, "2.2.0-1000");
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersionEntity2 = helper.getOrCreateRepositoryVersion(stackId, "2.2.0-2000");
        c1.setCurrentStackVersion(stackId);
        assertRepoVersionState("2.2.0-1000", org.apache.ambari.server.state.RepositoryVersionState.INSTALLING);
        assertRepoVersionState("2.2.0-2086", org.apache.ambari.server.state.RepositoryVersionState.CURRENT);
        helper.createHostVersion("h1", repositoryVersionEntity, org.apache.ambari.server.state.RepositoryVersionState.INSTALLED);
        helper.createHostVersion("h1", repositoryVersionEntity2, org.apache.ambari.server.state.RepositoryVersionState.INSTALLED);
        assertRepoVersionState("2.2.0-1000", org.apache.ambari.server.state.RepositoryVersionState.INSTALLED);
        assertRepoVersionState("2.2.0-2000", org.apache.ambari.server.state.RepositoryVersionState.INSTALLED);
        assertRepoVersionState("2.2.0-2086", org.apache.ambari.server.state.RepositoryVersionState.CURRENT);
        addHost("h2");
        clusters.mapHostToCluster("h2", "c1");
        java.util.List<org.apache.ambari.server.orm.entities.HostVersionEntity> h2Versions = hostVersionDAO.findByHost("h2");
        for (org.apache.ambari.server.orm.entities.HostVersionEntity hostVersionEntity : h2Versions) {
            if (hostVersionEntity.getRepositoryVersion().toString().equals("2.2.0-2086")) {
                org.junit.Assert.assertEquals(hostVersionEntity.getState(), org.apache.ambari.server.state.RepositoryVersionState.CURRENT);
            } else {
                org.junit.Assert.assertEquals(hostVersionEntity.getState(), org.apache.ambari.server.state.RepositoryVersionState.OUT_OF_SYNC);
            }
        }
    }

    @org.junit.Test
    public void testOnHostRemovedEvent() throws org.apache.ambari.server.AmbariException {
        addHost("h2");
        clusters.mapHostToCluster("h2", "c1");
        org.apache.ambari.server.state.Host host = clusters.getHost("h2");
        java.lang.Long hostId = host.getHostId();
        host.setState(org.apache.ambari.server.state.HostState.HEALTHY);
        org.apache.ambari.server.state.StackId stackId = new org.apache.ambari.server.state.StackId(this.stackId);
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersionEntity = helper.getOrCreateRepositoryVersion(stackId, "2.2.9-9999");
        c1.setCurrentStackVersion(stackId);
        assertRepoVersionState("2.2.0", org.apache.ambari.server.state.RepositoryVersionState.CURRENT);
        assertRepoVersionState("2.2.9-9999", org.apache.ambari.server.state.RepositoryVersionState.INSTALLING);
        org.apache.ambari.server.orm.entities.HostVersionEntity hv1 = helper.createHostVersion("h1", repositoryVersionEntity, org.apache.ambari.server.state.RepositoryVersionState.INSTALLED);
        org.apache.ambari.server.orm.entities.HostVersionEntity hv2 = helper.createHostVersion("h2", repositoryVersionEntity, org.apache.ambari.server.state.RepositoryVersionState.INSTALLED);
        assertRepoVersionState("2.2.0", org.apache.ambari.server.state.RepositoryVersionState.CURRENT);
        assertRepoVersionState("2.2.9-9999", org.apache.ambari.server.state.RepositoryVersionState.INSTALLED);
        hv1.setState(org.apache.ambari.server.state.RepositoryVersionState.INSTALLED);
        hv2.setState(org.apache.ambari.server.state.RepositoryVersionState.INSTALLING);
        hostVersionDAO.merge(hv1);
        hostVersionDAO.merge(hv2);
        assertRepoVersionState("2.2.0", org.apache.ambari.server.state.RepositoryVersionState.CURRENT);
        assertRepoVersionState("2.2.9-9999", org.apache.ambari.server.state.RepositoryVersionState.INSTALLING);
        injector.getInstance(com.google.inject.persist.UnitOfWork.class).begin();
        clusters.deleteHost("h2");
        clusters.publishHostsDeletion(java.util.Collections.singleton(hostId), java.util.Collections.singleton("h2"));
        injector.getInstance(com.google.inject.persist.UnitOfWork.class).end();
        assertRepoVersionState("2.2.0", org.apache.ambari.server.state.RepositoryVersionState.CURRENT);
        assertRepoVersionState("2.2.9-9999", org.apache.ambari.server.state.RepositoryVersionState.INSTALLED);
    }

    @org.junit.Test
    public void testComponentHostVersionNotRequired() throws java.lang.Exception {
        java.lang.String clusterName = java.util.UUID.randomUUID().toString();
        java.lang.String host1 = "host1";
        java.lang.String host2 = "host2";
        java.lang.String host3 = "host3";
        java.util.List<java.lang.String> allHosts = com.google.common.collect.Lists.newArrayList(host1, host2, host3);
        org.apache.ambari.server.state.StackId stackId = new org.apache.ambari.server.state.StackId(this.stackId);
        clusters.addCluster(clusterName, stackId);
        c1 = clusters.getCluster(clusterName);
        addHost(host1);
        addHost(host2);
        addHost(host3);
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repo = helper.getOrCreateRepositoryVersion(stackId, stackId.getStackVersion());
        clusters.mapHostToCluster(host1, clusterName);
        clusters.mapHostToCluster(host2, clusterName);
        clusters.mapHostToCluster(host3, clusterName);
        helper.createHostVersion(host1, repo, org.apache.ambari.server.state.RepositoryVersionState.INSTALLED);
        helper.createHostVersion(host2, repo, org.apache.ambari.server.state.RepositoryVersionState.INSTALLED);
        helper.createHostVersion(host3, repo, org.apache.ambari.server.state.RepositoryVersionState.INSTALLED);
        java.util.Map<java.lang.String, java.util.List<java.lang.Integer>> topology = new com.google.common.collect.ImmutableMap.Builder<java.lang.String, java.util.List<java.lang.Integer>>().put("NAMENODE", com.google.common.collect.Lists.newArrayList(0)).put("DATANODE", com.google.common.collect.Lists.newArrayList(1)).build();
        addService(c1, allHosts, topology, "HDFS", repo);
        topology = new com.google.common.collect.ImmutableMap.Builder<java.lang.String, java.util.List<java.lang.Integer>>().put("GANGLIA_SERVER", com.google.common.collect.Lists.newArrayList(0)).put("GANGLIA_MONITOR", com.google.common.collect.Lists.newArrayList(2)).build();
        addService(c1, allHosts, topology, "GANGLIA", repo);
        java.util.List<org.apache.ambari.server.orm.entities.HostVersionEntity> hostVersions = hostVersionDAO.findAll();
        org.junit.Assert.assertEquals(3, hostVersions.size());
        for (org.apache.ambari.server.orm.entities.HostVersionEntity hve : hostVersions) {
            if (hve.getHostName().equals(host3)) {
                org.junit.Assert.assertEquals(org.apache.ambari.server.state.RepositoryVersionState.NOT_REQUIRED, hve.getState());
            } else {
                org.junit.Assert.assertEquals(org.apache.ambari.server.state.RepositoryVersionState.OUT_OF_SYNC, hve.getState());
            }
        }
        addServiceComponent(c1, java.util.Collections.singletonList(host3), "HDFS", "DATANODE");
        hostVersions = hostVersionDAO.findAll();
        for (org.apache.ambari.server.orm.entities.HostVersionEntity hve : hostVersions) {
            org.junit.Assert.assertEquals(org.apache.ambari.server.state.RepositoryVersionState.OUT_OF_SYNC, hve.getState());
        }
        java.util.List<org.apache.ambari.server.state.ServiceComponentHost> hostComponents = c1.getServiceComponentHosts(host3);
        for (org.apache.ambari.server.state.ServiceComponentHost sch : hostComponents) {
            if (sch.getServiceName().equals("HDFS")) {
                sch.delete(new org.apache.ambari.server.controller.internal.DeleteHostComponentStatusMetaData());
                org.apache.ambari.server.state.StackId clusterStackId = c1.getDesiredStackVersion();
                org.apache.ambari.server.events.ServiceComponentUninstalledEvent event = new org.apache.ambari.server.events.ServiceComponentUninstalledEvent(c1.getClusterId(), clusterStackId.getStackName(), clusterStackId.getStackVersion(), "HDFS", "DATANODE", sch.getHostName(), false, false, -1L);
                m_eventPublisher.publish(event);
            }
        }
        hostVersions = hostVersionDAO.findAll();
        for (org.apache.ambari.server.orm.entities.HostVersionEntity hve : hostVersions) {
            if (hve.getHostName().equals(host3)) {
                org.junit.Assert.assertEquals(org.apache.ambari.server.state.RepositoryVersionState.NOT_REQUIRED, hve.getState());
            } else {
                org.junit.Assert.assertEquals(org.apache.ambari.server.state.RepositoryVersionState.OUT_OF_SYNC, hve.getState());
            }
        }
    }

    private void addHost(java.lang.String hostname) throws org.apache.ambari.server.AmbariException {
        clusters.addHost(hostname);
        org.apache.ambari.server.state.Host host1 = clusters.getHost(hostname);
        host1.setIPv4("ipv4");
        host1.setIPv6("ipv6");
        java.util.Map<java.lang.String, java.lang.String> hostAttributes = new java.util.HashMap<>();
        hostAttributes.put("os_family", "redhat");
        hostAttributes.put("os_release_version", "6.4");
        host1.setHostAttributes(hostAttributes);
    }

    private void addService(org.apache.ambari.server.state.Cluster cl, java.util.List<java.lang.String> hostList, java.util.Map<java.lang.String, java.util.List<java.lang.Integer>> topology, java.lang.String serviceName, org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersionEntity) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.StackId stackIdObj = new org.apache.ambari.server.state.StackId(stackId);
        cl.setDesiredStackVersion(stackIdObj);
        cl.addService(serviceName, repositoryVersionEntity);
        for (java.util.Map.Entry<java.lang.String, java.util.List<java.lang.Integer>> component : topology.entrySet()) {
            java.lang.String componentName = component.getKey();
            cl.getService(serviceName).addServiceComponent(componentName);
            for (java.lang.Integer hostIndex : component.getValue()) {
                cl.getService(serviceName).getServiceComponent(componentName).addServiceComponentHost(serviceComponentHostFactory.createNew(cl.getService(serviceName).getServiceComponent(componentName), hostList.get(hostIndex)));
            }
            org.apache.ambari.server.events.ServiceInstalledEvent event = new org.apache.ambari.server.events.ServiceInstalledEvent(cl.getClusterId(), stackIdObj.getStackName(), stackIdObj.getStackVersion(), serviceName);
            m_eventPublisher.publish(event);
        }
    }

    private void addServiceComponent(org.apache.ambari.server.state.Cluster cl, java.util.List<java.lang.String> hostList, java.lang.String serviceName, java.lang.String componentName) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.StackId stackIdObj = new org.apache.ambari.server.state.StackId(stackId);
        org.apache.ambari.server.state.Service service = cl.getService(serviceName);
        if (!service.getServiceComponents().containsKey(componentName)) {
            service.addServiceComponent(componentName);
        }
        org.apache.ambari.server.state.ServiceComponent component = service.getServiceComponent(componentName);
        for (java.lang.String hostName : hostList) {
            component.addServiceComponentHost(serviceComponentHostFactory.createNew(cl.getService(serviceName).getServiceComponent(componentName), hostName));
            org.apache.ambari.server.events.ServiceComponentInstalledEvent event = new org.apache.ambari.server.events.ServiceComponentInstalledEvent(cl.getClusterId(), stackIdObj.getStackName(), stackIdObj.getStackVersion(), serviceName, componentName, hostName, false, false);
            m_eventPublisher.publish(event);
        }
    }

    private void assertRepoVersionState(java.lang.String version, org.apache.ambari.server.state.RepositoryVersionState state) {
        for (org.apache.ambari.server.state.Host host : c1.getHosts()) {
            for (org.apache.ambari.server.orm.entities.HostVersionEntity hostVersionEntity : host.getAllHostVersions()) {
                if (hostVersionEntity.getRepositoryVersion().getVersion().equals("version")) {
                    org.junit.Assert.assertEquals(state, hostVersionEntity.getState());
                }
            }
        }
    }
}