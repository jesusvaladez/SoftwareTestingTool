package org.apache.ambari.server.state.cluster;
import com.google.inject.persist.UnitOfWork;
import javax.persistence.EntityManager;
import org.apache.commons.lang.StringUtils;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
public class ClusterTest {
    private static final java.util.EnumSet<org.apache.ambari.server.state.RepositoryVersionState> TERMINAL_VERSION_STATES = java.util.EnumSet.of(org.apache.ambari.server.state.RepositoryVersionState.CURRENT, org.apache.ambari.server.state.RepositoryVersionState.INSTALLED);

    private org.apache.ambari.server.state.Clusters clusters;

    private org.apache.ambari.server.state.Cluster c1;

    private com.google.inject.Injector injector;

    private org.apache.ambari.server.state.ServiceFactory serviceFactory;

    private org.apache.ambari.server.state.ServiceComponentFactory serviceComponentFactory;

    private org.apache.ambari.server.state.ServiceComponentHostFactory serviceComponentHostFactory;

    private org.apache.ambari.server.api.services.AmbariMetaInfo metaInfo;

    private org.apache.ambari.server.state.ConfigFactory configFactory;

    private org.apache.ambari.server.state.configgroup.ConfigGroupFactory configGroupFactory;

    private org.apache.ambari.server.orm.OrmTestHelper helper;

    private org.apache.ambari.server.orm.dao.StackDAO stackDAO;

    private org.apache.ambari.server.orm.dao.ClusterDAO clusterDAO;

    private org.apache.ambari.server.orm.dao.HostDAO hostDAO;

    private org.apache.ambari.server.orm.dao.HostVersionDAO hostVersionDAO;

    private org.apache.ambari.server.orm.dao.HostComponentStateDAO hostComponentStateDAO;

    private org.apache.ambari.server.orm.dao.RepositoryVersionDAO repositoryVersionDAO;

    private com.google.gson.Gson gson;

    private static class MockModule extends com.google.inject.AbstractModule {
        @java.lang.Override
        protected void configure() {
            org.apache.ambari.server.utils.EventBusSynchronizer.synchronizeAmbariEventPublisher(binder());
        }
    }

    @org.junit.Before
    public void setup() throws java.lang.Exception {
        injector = com.google.inject.Guice.createInjector(com.google.inject.util.Modules.override(new org.apache.ambari.server.orm.InMemoryDefaultTestModule()).with(new org.apache.ambari.server.state.cluster.ClusterTest.MockModule()));
        injector.getInstance(org.apache.ambari.server.orm.GuiceJpaInitializer.class);
        org.apache.ambari.server.utils.EventBusSynchronizer.synchronizeAmbariEventPublisher(injector);
        clusters = injector.getInstance(org.apache.ambari.server.state.Clusters.class);
        serviceFactory = injector.getInstance(org.apache.ambari.server.state.ServiceFactory.class);
        configGroupFactory = injector.getInstance(org.apache.ambari.server.state.configgroup.ConfigGroupFactory.class);
        serviceComponentFactory = injector.getInstance(org.apache.ambari.server.state.ServiceComponentFactory.class);
        serviceComponentHostFactory = injector.getInstance(org.apache.ambari.server.state.ServiceComponentHostFactory.class);
        configFactory = injector.getInstance(org.apache.ambari.server.state.ConfigFactory.class);
        metaInfo = injector.getInstance(org.apache.ambari.server.api.services.AmbariMetaInfo.class);
        helper = injector.getInstance(org.apache.ambari.server.orm.OrmTestHelper.class);
        stackDAO = injector.getInstance(org.apache.ambari.server.orm.dao.StackDAO.class);
        clusterDAO = injector.getInstance(org.apache.ambari.server.orm.dao.ClusterDAO.class);
        hostDAO = injector.getInstance(org.apache.ambari.server.orm.dao.HostDAO.class);
        hostVersionDAO = injector.getInstance(org.apache.ambari.server.orm.dao.HostVersionDAO.class);
        hostComponentStateDAO = injector.getInstance(org.apache.ambari.server.orm.dao.HostComponentStateDAO.class);
        repositoryVersionDAO = injector.getInstance(org.apache.ambari.server.orm.dao.RepositoryVersionDAO.class);
        gson = injector.getInstance(com.google.gson.Gson.class);
        injector.getInstance(com.google.inject.persist.UnitOfWork.class).begin();
    }

    @org.junit.After
    public void teardown() throws org.apache.ambari.server.AmbariException, java.sql.SQLException {
        injector.getInstance(com.google.inject.persist.UnitOfWork.class).end();
        org.apache.ambari.server.H2DatabaseCleaner.clearDatabaseAndStopPersistenceService(injector);
    }

    private void createDefaultCluster() throws java.lang.Exception {
        createDefaultCluster(com.google.common.collect.Sets.newHashSet("h1", "h2"));
    }

    private void createDefaultCluster(java.util.Set<java.lang.String> hostNames) throws java.lang.Exception {
        org.apache.ambari.server.state.StackId stackId = new org.apache.ambari.server.state.StackId("HDP", "0.1");
        createDefaultCluster(hostNames, stackId);
    }

    private void createDefaultCluster(java.util.Set<java.lang.String> hostNames, org.apache.ambari.server.state.StackId stackId) throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.StackEntity stackEntity = stackDAO.find(stackId.getStackName(), stackId.getStackVersion());
        org.junit.Assert.assertNotNull(stackEntity);
        java.lang.String clusterName = "c1";
        clusters.addCluster(clusterName, stackId);
        java.util.Map<java.lang.String, java.lang.String> hostAttributes = new java.util.HashMap<>();
        hostAttributes.put("os_family", "redhat");
        hostAttributes.put("os_release_version", "5.9");
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion = helper.getOrCreateRepositoryVersion(stackId, stackId.getStackVersion());
        for (java.lang.String hostName : hostNames) {
            clusters.addHost(hostName);
            org.apache.ambari.server.orm.entities.HostEntity hostEntity = hostDAO.findByName(hostName);
            hostEntity.setIpv4("ipv4");
            hostEntity.setIpv6("ipv6");
            hostEntity.setHostAttributes(gson.toJson(hostAttributes));
            hostDAO.merge(hostEntity);
        }
        clusters.mapAndPublishHostsToCluster(hostNames, clusterName);
        c1 = clusters.getCluster(clusterName);
    }

    public org.apache.ambari.server.orm.entities.ClusterEntity createDummyData() {
        org.apache.ambari.server.orm.entities.StackEntity stackEntity = new org.apache.ambari.server.orm.entities.StackEntity();
        stackEntity.setStackName("HDP");
        stackEntity.setStackVersion("0.1");
        org.apache.ambari.server.orm.entities.ClusterEntity clusterEntity = new org.apache.ambari.server.orm.entities.ClusterEntity();
        clusterEntity.setClusterId(1L);
        clusterEntity.setClusterName("test_cluster1");
        clusterEntity.setClusterInfo("test_cluster_info1");
        clusterEntity.setDesiredStack(stackEntity);
        org.apache.ambari.server.orm.entities.HostEntity host1 = new org.apache.ambari.server.orm.entities.HostEntity();
        org.apache.ambari.server.orm.entities.HostEntity host2 = new org.apache.ambari.server.orm.entities.HostEntity();
        org.apache.ambari.server.orm.entities.HostEntity host3 = new org.apache.ambari.server.orm.entities.HostEntity();
        host1.setHostName("test_host1");
        host2.setHostName("test_host2");
        host3.setHostName("test_host3");
        host1.setIpv4("192.168.0.1");
        host2.setIpv4("192.168.0.2");
        host3.setIpv4("192.168.0.3");
        java.util.List<org.apache.ambari.server.orm.entities.HostEntity> hostEntities = new java.util.ArrayList<>();
        hostEntities.add(host1);
        hostEntities.add(host2);
        clusterEntity.setHostEntities(hostEntities);
        clusterEntity.setClusterConfigEntities(java.util.Collections.emptyList());
        host1.setClusterEntities(java.util.Arrays.asList(clusterEntity));
        host2.setClusterEntities(java.util.Arrays.asList(clusterEntity));
        org.apache.ambari.server.orm.entities.HostStateEntity hostStateEntity1 = new org.apache.ambari.server.orm.entities.HostStateEntity();
        hostStateEntity1.setCurrentState(org.apache.ambari.server.state.HostState.HEARTBEAT_LOST);
        hostStateEntity1.setHostEntity(host1);
        org.apache.ambari.server.orm.entities.HostStateEntity hostStateEntity2 = new org.apache.ambari.server.orm.entities.HostStateEntity();
        hostStateEntity2.setCurrentState(org.apache.ambari.server.state.HostState.HEALTHY);
        hostStateEntity2.setHostEntity(host2);
        host1.setHostStateEntity(hostStateEntity1);
        host2.setHostStateEntity(hostStateEntity2);
        org.apache.ambari.server.orm.entities.ClusterServiceEntity clusterServiceEntity = new org.apache.ambari.server.orm.entities.ClusterServiceEntity();
        clusterServiceEntity.setServiceName("HDFS");
        clusterServiceEntity.setClusterEntity(clusterEntity);
        clusterServiceEntity.setServiceComponentDesiredStateEntities(java.util.Collections.emptyList());
        org.apache.ambari.server.orm.entities.ServiceDesiredStateEntity stateEntity = Mockito.mock(org.apache.ambari.server.orm.entities.ServiceDesiredStateEntity.class);
        Mockito.when(stateEntity.getDesiredStack()).thenReturn(stackEntity);
        clusterServiceEntity.setServiceDesiredStateEntity(stateEntity);
        java.util.List<org.apache.ambari.server.orm.entities.ClusterServiceEntity> clusterServiceEntities = new java.util.ArrayList<>();
        clusterServiceEntities.add(clusterServiceEntity);
        clusterEntity.setClusterServiceEntities(clusterServiceEntities);
        return clusterEntity;
    }

    private org.apache.ambari.server.state.Cluster createClusterForRU(java.lang.String clusterName, org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion, java.util.Map<java.lang.String, java.lang.String> hostAttributes) throws java.lang.Exception {
        org.apache.ambari.server.state.StackId stackId = repositoryVersion.getStackId();
        clusters.addCluster(clusterName, stackId);
        org.apache.ambari.server.state.Cluster cluster = clusters.getCluster(clusterName);
        junit.framework.Assert.assertEquals(clusterName, cluster.getClusterName());
        java.util.List<java.lang.String> hostNames = new java.util.ArrayList<java.lang.String>() {
            {
                add("h-1");
                add("h-2");
                add("h-3");
            }
        };
        for (java.lang.String hostName : hostNames) {
            addHost(hostName, hostAttributes);
        }
        cluster.setDesiredStackVersion(stackId);
        cluster.setCurrentStackVersion(stackId);
        for (java.lang.String hostName : hostNames) {
            clusters.mapHostToCluster(hostName, clusterName);
        }
        for (org.apache.ambari.server.state.Host host : cluster.getHosts()) {
            host.setState(org.apache.ambari.server.state.HostState.HEALTHY);
        }
        org.apache.ambari.server.state.Service s1 = serviceFactory.createNew(cluster, "HDFS", repositoryVersion);
        org.apache.ambari.server.state.Service s2 = serviceFactory.createNew(cluster, "ZOOKEEPER", repositoryVersion);
        org.apache.ambari.server.state.Service s3 = serviceFactory.createNew(cluster, "GANGLIA", repositoryVersion);
        cluster.addService(s1);
        cluster.addService(s2);
        cluster.addService(s3);
        org.apache.ambari.server.state.ServiceComponent sc1CompA = serviceComponentFactory.createNew(s1, "NAMENODE");
        org.apache.ambari.server.state.ServiceComponent sc1CompB = serviceComponentFactory.createNew(s1, "DATANODE");
        org.apache.ambari.server.state.ServiceComponent sc1CompC = serviceComponentFactory.createNew(s1, "HDFS_CLIENT");
        s1.addServiceComponent(sc1CompA);
        s1.addServiceComponent(sc1CompB);
        s1.addServiceComponent(sc1CompC);
        org.apache.ambari.server.state.ServiceComponent sc2CompA = serviceComponentFactory.createNew(s2, "ZOOKEEPER_SERVER");
        org.apache.ambari.server.state.ServiceComponent sc2CompB = serviceComponentFactory.createNew(s2, "ZOOKEEPER_CLIENT");
        s2.addServiceComponent(sc2CompA);
        s2.addServiceComponent(sc2CompB);
        org.apache.ambari.server.state.ServiceComponent sc3CompA = serviceComponentFactory.createNew(s3, "GANGLIA_SERVER");
        org.apache.ambari.server.state.ServiceComponent sc3CompB = serviceComponentFactory.createNew(s3, "GANGLIA_MONITOR");
        s3.addServiceComponent(sc3CompA);
        s3.addServiceComponent(sc3CompB);
        org.apache.ambari.server.state.ServiceComponentHost schHost1Serv1CompA = serviceComponentHostFactory.createNew(sc1CompA, "h-1");
        org.apache.ambari.server.state.ServiceComponentHost schHost1Serv1CompB = serviceComponentHostFactory.createNew(sc1CompB, "h-1");
        org.apache.ambari.server.state.ServiceComponentHost schHost1Serv1CompC = serviceComponentHostFactory.createNew(sc1CompC, "h-1");
        org.apache.ambari.server.state.ServiceComponentHost schHost1Serv2CompA = serviceComponentHostFactory.createNew(sc2CompA, "h-1");
        org.apache.ambari.server.state.ServiceComponentHost schHost1Serv2CompB = serviceComponentHostFactory.createNew(sc2CompB, "h-1");
        org.apache.ambari.server.state.ServiceComponentHost schHost1Serv3CompA = serviceComponentHostFactory.createNew(sc3CompA, "h-1");
        org.apache.ambari.server.state.ServiceComponentHost schHost1Serv3CompB = serviceComponentHostFactory.createNew(sc3CompB, "h-1");
        sc1CompA.addServiceComponentHost(schHost1Serv1CompA);
        sc1CompB.addServiceComponentHost(schHost1Serv1CompB);
        sc1CompC.addServiceComponentHost(schHost1Serv1CompC);
        sc2CompA.addServiceComponentHost(schHost1Serv2CompA);
        sc2CompB.addServiceComponentHost(schHost1Serv2CompB);
        sc3CompA.addServiceComponentHost(schHost1Serv3CompA);
        sc3CompB.addServiceComponentHost(schHost1Serv3CompB);
        org.apache.ambari.server.state.ServiceComponentHost schHost2Serv2CompB = serviceComponentHostFactory.createNew(sc2CompB, "h-2");
        org.apache.ambari.server.state.ServiceComponentHost schHost2Serv3CompB = serviceComponentHostFactory.createNew(sc3CompB, "h-2");
        sc2CompB.addServiceComponentHost(schHost2Serv2CompB);
        sc3CompB.addServiceComponentHost(schHost2Serv3CompB);
        org.apache.ambari.server.state.ServiceComponentHost schHost3Serv3CompB = serviceComponentHostFactory.createNew(sc3CompB, "h-3");
        sc3CompB.addServiceComponentHost(schHost3Serv3CompB);
        java.util.List<org.apache.ambari.server.state.ServiceComponentHost> scHost1 = cluster.getServiceComponentHosts("h-1");
        junit.framework.Assert.assertEquals(7, scHost1.size());
        java.util.List<org.apache.ambari.server.state.ServiceComponentHost> scHost2 = cluster.getServiceComponentHosts("h-2");
        junit.framework.Assert.assertEquals(2, scHost2.size());
        java.util.List<org.apache.ambari.server.state.ServiceComponentHost> scHost3 = cluster.getServiceComponentHosts("h-3");
        junit.framework.Assert.assertEquals(1, scHost3.size());
        java.util.HashMap<java.lang.String, java.util.Set<java.lang.String>> componentsThatAdvertiseVersion = new java.util.HashMap<>();
        java.util.HashMap<java.lang.String, java.util.Set<java.lang.String>> componentsThatDontAdvertiseVersion = new java.util.HashMap<>();
        java.util.Set<java.lang.String> hdfsComponents = new java.util.HashSet<java.lang.String>() {
            {
                add("NAMENODE");
                add("DATANODE");
                add("HDFS_CLIENT");
            }
        };
        java.util.Set<java.lang.String> zkComponents = new java.util.HashSet<java.lang.String>() {
            {
                add("ZOOKEEPER_SERVER");
                add("ZOOKEEPER_CLIENT");
            }
        };
        java.util.Set<java.lang.String> gangliaComponents = new java.util.HashSet<java.lang.String>() {
            {
                add("GANGLIA_SERVER");
                add("GANGLIA_MONITOR");
            }
        };
        componentsThatAdvertiseVersion.put("HDFS", hdfsComponents);
        componentsThatAdvertiseVersion.put("ZOOKEEPER", zkComponents);
        componentsThatDontAdvertiseVersion.put("GANGLIA", gangliaComponents);
        for (java.lang.String service : componentsThatAdvertiseVersion.keySet()) {
            java.util.Set<java.lang.String> components = componentsThatAdvertiseVersion.get(service);
            for (java.lang.String componentName : components) {
                org.apache.ambari.server.state.ComponentInfo component = metaInfo.getComponent(stackId.getStackName(), stackId.getStackVersion(), service, componentName);
                junit.framework.Assert.assertTrue(component.isVersionAdvertised());
            }
        }
        for (java.lang.String service : componentsThatDontAdvertiseVersion.keySet()) {
            java.util.Set<java.lang.String> components = componentsThatDontAdvertiseVersion.get(service);
            for (java.lang.String componentName : components) {
                org.apache.ambari.server.state.ComponentInfo component = metaInfo.getComponent(stackId.getStackName(), stackId.getStackVersion(), service, componentName);
                junit.framework.Assert.assertFalse(component.isVersionAdvertised());
            }
        }
        return cluster;
    }

    private org.apache.ambari.server.state.Host addHost(java.lang.String hostName, java.util.Map<java.lang.String, java.lang.String> hostAttributes) throws java.lang.Exception {
        clusters.addHost(hostName);
        org.apache.ambari.server.state.Host host = clusters.getHost(hostName);
        host.setIPv4("ipv4");
        host.setIPv6("ipv6");
        host.setHostAttributes(hostAttributes);
        return host;
    }

    private void simulateStackVersionListener(org.apache.ambari.server.state.StackId stackId, java.lang.String version, org.apache.ambari.server.state.Cluster cluster, java.util.List<org.apache.ambari.server.orm.entities.HostComponentStateEntity> hostComponentStates) throws java.lang.Exception {
        for (int i = 0; i < hostComponentStates.size(); i++) {
            org.apache.ambari.server.orm.entities.HostComponentStateEntity hce = hostComponentStates.get(i);
            org.apache.ambari.server.state.ComponentInfo compInfo = metaInfo.getComponent(stackId.getStackName(), stackId.getStackVersion(), hce.getServiceName(), hce.getComponentName());
            if (compInfo.isVersionAdvertised()) {
                hce.setVersion(version);
                hostComponentStateDAO.merge(hce);
            }
            helper.getOrCreateRepositoryVersion(stackId, version);
            org.apache.ambari.server.state.Service svc = cluster.getService(hce.getServiceName());
            org.apache.ambari.server.state.ServiceComponent svcComp = svc.getServiceComponent(hce.getComponentName());
            org.apache.ambari.server.state.ServiceComponentHost scHost = svcComp.getServiceComponentHost(hce.getHostName());
            scHost.recalculateHostVersionState();
        }
    }

    @org.junit.Test
    public void testAddHost() throws java.lang.Exception {
        createDefaultCluster();
        clusters.addHost("h3");
        try {
            clusters.addHost("h3");
            org.junit.Assert.fail("Duplicate add should fail");
        } catch (org.apache.ambari.server.AmbariException e) {
        }
    }

    @org.junit.Test
    public void testGetHostState() throws java.lang.Exception {
        createDefaultCluster();
        junit.framework.Assert.assertEquals(org.apache.ambari.server.state.HostState.INIT, clusters.getHost("h1").getState());
    }

    @org.junit.Test
    public void testSetHostState() throws java.lang.Exception {
        createDefaultCluster();
        clusters.getHost("h1").setState(org.apache.ambari.server.state.HostState.HEARTBEAT_LOST);
        junit.framework.Assert.assertEquals(org.apache.ambari.server.state.HostState.HEARTBEAT_LOST, clusters.getHost("h1").getState());
    }

    @org.junit.Test
    public void testHostEvent() throws java.lang.Exception, org.apache.ambari.server.state.fsm.InvalidStateTransitionException {
        createDefaultCluster();
        org.apache.ambari.server.agent.HostInfo hostInfo = new org.apache.ambari.server.agent.HostInfo();
        hostInfo.setHostName("h1");
        hostInfo.setInterfaces("fip_4");
        hostInfo.setArchitecture("os_arch");
        hostInfo.setOS("os_type");
        hostInfo.setMemoryTotal(10);
        hostInfo.setMemorySize(100);
        hostInfo.setProcessorCount(10);
        java.util.List<org.apache.ambari.server.agent.DiskInfo> mounts = new java.util.ArrayList<>();
        mounts.add(new org.apache.ambari.server.agent.DiskInfo("/dev/sda", "/mnt/disk1", "5000000", "4000000", "10%", "size", "fstype"));
        hostInfo.setMounts(mounts);
        org.apache.ambari.server.agent.AgentEnv agentEnv = new org.apache.ambari.server.agent.AgentEnv();
        org.apache.ambari.server.agent.AgentEnv.Directory dir1 = new org.apache.ambari.server.agent.AgentEnv.Directory();
        dir1.setName("/etc/hadoop");
        dir1.setType("not_exist");
        org.apache.ambari.server.agent.AgentEnv.Directory dir2 = new org.apache.ambari.server.agent.AgentEnv.Directory();
        dir2.setName("/var/log/hadoop");
        dir2.setType("not_exist");
        agentEnv.setStackFoldersAndFiles(new org.apache.ambari.server.agent.AgentEnv.Directory[]{ dir1, dir2 });
        org.apache.ambari.server.state.AgentVersion agentVersion = new org.apache.ambari.server.state.AgentVersion("0.0.x");
        long currentTime = 1001;
        clusters.getHost("h1").handleEvent(new org.apache.ambari.server.state.host.HostRegistrationRequestEvent("h1", agentVersion, currentTime, hostInfo, agentEnv, currentTime));
        junit.framework.Assert.assertEquals(org.apache.ambari.server.state.HostState.WAITING_FOR_HOST_STATUS_UPDATES, clusters.getHost("h1").getState());
        clusters.getHost("h1").setState(org.apache.ambari.server.state.HostState.HEARTBEAT_LOST);
        try {
            clusters.getHost("h1").handleEvent(new org.apache.ambari.server.state.host.HostHealthyHeartbeatEvent("h1", currentTime, null, null));
            org.junit.Assert.fail("Exception should be thrown on invalid event");
        } catch (org.apache.ambari.server.state.fsm.InvalidStateTransitionException e) {
        }
    }

    @org.junit.Test
    public void testBasicClusterSetup() throws java.lang.Exception {
        org.apache.ambari.server.state.StackId stackVersion = new org.apache.ambari.server.state.StackId("HDP-1.2.0");
        createDefaultCluster();
        java.lang.String clusterName = "c2";
        try {
            clusters.getCluster(clusterName);
            org.junit.Assert.fail("Exception expected for invalid cluster");
        } catch (java.lang.Exception e) {
        }
        clusters.addCluster(clusterName, stackVersion);
        org.apache.ambari.server.state.Cluster c2 = clusters.getCluster(clusterName);
        junit.framework.Assert.assertNotNull(c2);
        junit.framework.Assert.assertEquals(clusterName, c2.getClusterName());
        c2.setClusterName("foo2");
        junit.framework.Assert.assertEquals("foo2", c2.getClusterName());
        junit.framework.Assert.assertNotNull(c2.getDesiredStackVersion());
        junit.framework.Assert.assertEquals("HDP-1.2.0", c2.getDesiredStackVersion().getStackId());
    }

    @org.junit.Test
    public void testAddAndGetServices() throws java.lang.Exception {
        createDefaultCluster();
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion = helper.getOrCreateRepositoryVersion(c1);
        serviceFactory.createNew(c1, "HDFS", repositoryVersion);
        serviceFactory.createNew(c1, "MAPREDUCE", repositoryVersion);
        org.apache.ambari.server.state.Service s = c1.getService("HDFS");
        junit.framework.Assert.assertNotNull(s);
        junit.framework.Assert.assertEquals("HDFS", s.getName());
        junit.framework.Assert.assertEquals(c1.getClusterId(), s.getClusterId());
        try {
            c1.getService("HBASE");
            org.junit.Assert.fail("Expected error for unknown service");
        } catch (java.lang.Exception e) {
        }
        java.util.Map<java.lang.String, org.apache.ambari.server.state.Service> services = c1.getServices();
        junit.framework.Assert.assertEquals(2, services.size());
        junit.framework.Assert.assertTrue(services.containsKey("HDFS"));
        junit.framework.Assert.assertTrue(services.containsKey("MAPREDUCE"));
    }

    @org.junit.Test
    public void testGetServiceComponentHosts() throws java.lang.Exception {
        createDefaultCluster();
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion = helper.getOrCreateRepositoryVersion(c1);
        org.apache.ambari.server.state.Service s = serviceFactory.createNew(c1, "HDFS", repositoryVersion);
        c1.addService(s);
        org.apache.ambari.server.state.ServiceComponent sc = serviceComponentFactory.createNew(s, "NAMENODE");
        s.addServiceComponent(sc);
        org.apache.ambari.server.state.ServiceComponentHost sch = serviceComponentHostFactory.createNew(sc, "h1");
        sc.addServiceComponentHost(sch);
        java.util.List<org.apache.ambari.server.state.ServiceComponentHost> scHosts = c1.getServiceComponentHosts("h1");
        junit.framework.Assert.assertEquals(1, scHosts.size());
        java.util.Iterator<org.apache.ambari.server.state.ServiceComponentHost> iterator = scHosts.iterator();
        try {
            while (iterator.hasNext()) {
                iterator.next();
                org.apache.ambari.server.state.Service s1 = serviceFactory.createNew(c1, "PIG", repositoryVersion);
                c1.addService(s1);
                org.apache.ambari.server.state.ServiceComponent sc1 = serviceComponentFactory.createNew(s1, "PIG");
                s1.addServiceComponent(sc1);
                org.apache.ambari.server.state.ServiceComponentHost sch1 = serviceComponentHostFactory.createNew(sc1, "h1");
                sc1.addServiceComponentHost(sch1);
            } 
        } catch (java.util.ConcurrentModificationException e) {
            junit.framework.Assert.assertTrue("Failed to work concurrently with sch", false);
        }
        scHosts = c1.getServiceComponentHosts("h1");
        junit.framework.Assert.assertEquals(2, scHosts.size());
    }

    @org.junit.Test
    public void testGetServiceComponentHosts_ForService() throws java.lang.Exception {
        createDefaultCluster();
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion = helper.getOrCreateRepositoryVersion(c1);
        org.apache.ambari.server.state.Service s = serviceFactory.createNew(c1, "HDFS", repositoryVersion);
        c1.addService(s);
        org.apache.ambari.server.state.ServiceComponent scNN = serviceComponentFactory.createNew(s, "NAMENODE");
        s.addServiceComponent(scNN);
        org.apache.ambari.server.state.ServiceComponentHost schNNH1 = serviceComponentHostFactory.createNew(scNN, "h1");
        scNN.addServiceComponentHost(schNNH1);
        org.apache.ambari.server.state.ServiceComponent scDN = serviceComponentFactory.createNew(s, "DATANODE");
        s.addServiceComponent(scDN);
        org.apache.ambari.server.state.ServiceComponentHost scDNH1 = serviceComponentHostFactory.createNew(scDN, "h1");
        scDN.addServiceComponentHost(scDNH1);
        org.apache.ambari.server.state.ServiceComponentHost scDNH2 = serviceComponentHostFactory.createNew(scDN, "h2");
        scDN.addServiceComponentHost(scDNH2);
        java.util.List<org.apache.ambari.server.state.ServiceComponentHost> scHosts;
        scHosts = c1.getServiceComponentHosts("HDFS", null);
        junit.framework.Assert.assertEquals(3, scHosts.size());
        scHosts = c1.getServiceComponentHosts("UNKNOWN SERVICE", null);
        junit.framework.Assert.assertEquals(0, scHosts.size());
    }

    @org.junit.Test
    public void testGetServiceComponentHosts_ForServiceComponent() throws java.lang.Exception {
        createDefaultCluster();
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion = helper.getOrCreateRepositoryVersion(c1);
        org.apache.ambari.server.state.Service s = serviceFactory.createNew(c1, "HDFS", repositoryVersion);
        c1.addService(s);
        org.apache.ambari.server.state.ServiceComponent scNN = serviceComponentFactory.createNew(s, "NAMENODE");
        s.addServiceComponent(scNN);
        org.apache.ambari.server.state.ServiceComponentHost schNNH1 = serviceComponentHostFactory.createNew(scNN, "h1");
        scNN.addServiceComponentHost(schNNH1);
        org.apache.ambari.server.state.ServiceComponent scDN = serviceComponentFactory.createNew(s, "DATANODE");
        s.addServiceComponent(scDN);
        org.apache.ambari.server.state.ServiceComponentHost scDNH1 = serviceComponentHostFactory.createNew(scDN, "h1");
        scDN.addServiceComponentHost(scDNH1);
        org.apache.ambari.server.state.ServiceComponentHost scDNH2 = serviceComponentHostFactory.createNew(scDN, "h2");
        scDN.addServiceComponentHost(scDNH2);
        java.util.List<org.apache.ambari.server.state.ServiceComponentHost> scHosts;
        scHosts = c1.getServiceComponentHosts("HDFS", "DATANODE");
        junit.framework.Assert.assertEquals(2, scHosts.size());
        scHosts = c1.getServiceComponentHosts("HDFS", "UNKNOWN COMPONENT");
        junit.framework.Assert.assertEquals(0, scHosts.size());
        scHosts = c1.getServiceComponentHosts("UNKNOWN SERVICE", "DATANODE");
        junit.framework.Assert.assertEquals(0, scHosts.size());
        scHosts = c1.getServiceComponentHosts("UNKNOWN SERVICE", "UNKNOWN COMPONENT");
        junit.framework.Assert.assertEquals(0, scHosts.size());
    }

    @org.junit.Test
    public void testGetServiceComponentHostMap() throws java.lang.Exception {
        createDefaultCluster();
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion = helper.getOrCreateRepositoryVersion(c1);
        org.apache.ambari.server.state.Service s = serviceFactory.createNew(c1, "HDFS", repositoryVersion);
        c1.addService(s);
        org.apache.ambari.server.state.ServiceComponent scNN = serviceComponentFactory.createNew(s, "NAMENODE");
        s.addServiceComponent(scNN);
        org.apache.ambari.server.state.ServiceComponentHost schNNH1 = serviceComponentHostFactory.createNew(scNN, "h1");
        scNN.addServiceComponentHost(schNNH1);
        org.apache.ambari.server.state.ServiceComponent scDN = serviceComponentFactory.createNew(s, "DATANODE");
        s.addServiceComponent(scDN);
        org.apache.ambari.server.state.ServiceComponentHost scDNH1 = serviceComponentHostFactory.createNew(scDN, "h1");
        scDN.addServiceComponentHost(scDNH1);
        org.apache.ambari.server.state.ServiceComponentHost scDNH2 = serviceComponentHostFactory.createNew(scDN, "h2");
        scDN.addServiceComponentHost(scDNH2);
        java.util.Map<java.lang.String, java.util.Set<java.lang.String>> componentHostMap;
        componentHostMap = c1.getServiceComponentHostMap(null, null);
        junit.framework.Assert.assertEquals(2, componentHostMap.size());
        junit.framework.Assert.assertEquals(1, componentHostMap.get("NAMENODE").size());
        junit.framework.Assert.assertTrue(componentHostMap.get("NAMENODE").contains("h1"));
        junit.framework.Assert.assertEquals(2, componentHostMap.get("DATANODE").size());
        junit.framework.Assert.assertTrue(componentHostMap.get("DATANODE").contains("h1"));
        junit.framework.Assert.assertTrue(componentHostMap.get("DATANODE").contains("h2"));
    }

    @org.junit.Test
    public void testGetServiceComponentHostMap_ForService() throws java.lang.Exception {
        createDefaultCluster();
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion = helper.getOrCreateRepositoryVersion(c1);
        org.apache.ambari.server.state.Service sfHDFS = serviceFactory.createNew(c1, "HDFS", repositoryVersion);
        c1.addService(sfHDFS);
        org.apache.ambari.server.state.Service sfMR = serviceFactory.createNew(c1, "MAPREDUCE", repositoryVersion);
        c1.addService(sfMR);
        org.apache.ambari.server.state.ServiceComponent scNN = serviceComponentFactory.createNew(sfHDFS, "NAMENODE");
        sfHDFS.addServiceComponent(scNN);
        org.apache.ambari.server.state.ServiceComponentHost schNNH1 = serviceComponentHostFactory.createNew(scNN, "h1");
        scNN.addServiceComponentHost(schNNH1);
        org.apache.ambari.server.state.ServiceComponent scDN = serviceComponentFactory.createNew(sfHDFS, "DATANODE");
        sfHDFS.addServiceComponent(scDN);
        org.apache.ambari.server.state.ServiceComponentHost scDNH1 = serviceComponentHostFactory.createNew(scDN, "h1");
        scDN.addServiceComponentHost(scDNH1);
        org.apache.ambari.server.state.ServiceComponentHost scDNH2 = serviceComponentHostFactory.createNew(scDN, "h2");
        scDN.addServiceComponentHost(scDNH2);
        org.apache.ambari.server.state.ServiceComponent scJT = serviceComponentFactory.createNew(sfMR, "JOBTRACKER");
        sfMR.addServiceComponent(scJT);
        org.apache.ambari.server.state.ServiceComponentHost schJTH1 = serviceComponentHostFactory.createNew(scJT, "h1");
        scJT.addServiceComponentHost(schJTH1);
        java.util.Map<java.lang.String, java.util.Set<java.lang.String>> componentHostMap;
        componentHostMap = c1.getServiceComponentHostMap(null, java.util.Collections.singleton("HDFS"));
        junit.framework.Assert.assertEquals(2, componentHostMap.size());
        junit.framework.Assert.assertEquals(1, componentHostMap.get("NAMENODE").size());
        junit.framework.Assert.assertTrue(componentHostMap.get("NAMENODE").contains("h1"));
        junit.framework.Assert.assertEquals(2, componentHostMap.get("DATANODE").size());
        junit.framework.Assert.assertTrue(componentHostMap.get("DATANODE").contains("h1"));
        junit.framework.Assert.assertTrue(componentHostMap.get("DATANODE").contains("h2"));
        componentHostMap = c1.getServiceComponentHostMap(null, java.util.Collections.singleton("MAPREDUCE"));
        junit.framework.Assert.assertEquals(1, componentHostMap.size());
        junit.framework.Assert.assertEquals(1, componentHostMap.get("JOBTRACKER").size());
        junit.framework.Assert.assertTrue(componentHostMap.get("JOBTRACKER").contains("h1"));
        componentHostMap = c1.getServiceComponentHostMap(null, new java.util.HashSet<>(java.util.Arrays.asList("HDFS", "MAPREDUCE")));
        junit.framework.Assert.assertEquals(3, componentHostMap.size());
        junit.framework.Assert.assertEquals(1, componentHostMap.get("NAMENODE").size());
        junit.framework.Assert.assertTrue(componentHostMap.get("NAMENODE").contains("h1"));
        junit.framework.Assert.assertEquals(2, componentHostMap.get("DATANODE").size());
        junit.framework.Assert.assertTrue(componentHostMap.get("DATANODE").contains("h1"));
        junit.framework.Assert.assertTrue(componentHostMap.get("DATANODE").contains("h2"));
        junit.framework.Assert.assertEquals(1, componentHostMap.get("JOBTRACKER").size());
        junit.framework.Assert.assertTrue(componentHostMap.get("JOBTRACKER").contains("h1"));
        componentHostMap = c1.getServiceComponentHostMap(null, java.util.Collections.singleton("UNKNOWN"));
        junit.framework.Assert.assertEquals(0, componentHostMap.size());
    }

    @org.junit.Test
    public void testGetServiceComponentHostMap_ForHost() throws java.lang.Exception {
        createDefaultCluster();
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion = helper.getOrCreateRepositoryVersion(c1);
        org.apache.ambari.server.state.Service sfHDFS = serviceFactory.createNew(c1, "HDFS", repositoryVersion);
        c1.addService(sfHDFS);
        org.apache.ambari.server.state.Service sfMR = serviceFactory.createNew(c1, "MAPREDUCE", repositoryVersion);
        c1.addService(sfMR);
        org.apache.ambari.server.state.ServiceComponent scNN = serviceComponentFactory.createNew(sfHDFS, "NAMENODE");
        sfHDFS.addServiceComponent(scNN);
        org.apache.ambari.server.state.ServiceComponentHost schNNH1 = serviceComponentHostFactory.createNew(scNN, "h1");
        scNN.addServiceComponentHost(schNNH1);
        org.apache.ambari.server.state.ServiceComponent scDN = serviceComponentFactory.createNew(sfHDFS, "DATANODE");
        sfHDFS.addServiceComponent(scDN);
        org.apache.ambari.server.state.ServiceComponentHost scDNH1 = serviceComponentHostFactory.createNew(scDN, "h1");
        scDN.addServiceComponentHost(scDNH1);
        org.apache.ambari.server.state.ServiceComponentHost scDNH2 = serviceComponentHostFactory.createNew(scDN, "h2");
        scDN.addServiceComponentHost(scDNH2);
        org.apache.ambari.server.state.ServiceComponent scJT = serviceComponentFactory.createNew(sfMR, "JOBTRACKER");
        sfMR.addServiceComponent(scJT);
        org.apache.ambari.server.state.ServiceComponentHost schJTH1 = serviceComponentHostFactory.createNew(scJT, "h1");
        scJT.addServiceComponentHost(schJTH1);
        java.util.Map<java.lang.String, java.util.Set<java.lang.String>> componentHostMap;
        componentHostMap = c1.getServiceComponentHostMap(java.util.Collections.singleton("h1"), null);
        junit.framework.Assert.assertEquals(3, componentHostMap.size());
        junit.framework.Assert.assertEquals(1, componentHostMap.get("NAMENODE").size());
        junit.framework.Assert.assertTrue(componentHostMap.get("NAMENODE").contains("h1"));
        junit.framework.Assert.assertEquals(1, componentHostMap.get("DATANODE").size());
        junit.framework.Assert.assertTrue(componentHostMap.get("DATANODE").contains("h1"));
        junit.framework.Assert.assertEquals(1, componentHostMap.get("JOBTRACKER").size());
        junit.framework.Assert.assertTrue(componentHostMap.get("JOBTRACKER").contains("h1"));
        componentHostMap = c1.getServiceComponentHostMap(java.util.Collections.singleton("h2"), null);
        junit.framework.Assert.assertEquals(1, componentHostMap.size());
        junit.framework.Assert.assertEquals(1, componentHostMap.get("DATANODE").size());
        junit.framework.Assert.assertTrue(componentHostMap.get("DATANODE").contains("h2"));
        componentHostMap = c1.getServiceComponentHostMap(new java.util.HashSet<>(java.util.Arrays.asList("h1", "h2", "h3")), null);
        junit.framework.Assert.assertEquals(3, componentHostMap.size());
        junit.framework.Assert.assertEquals(1, componentHostMap.get("NAMENODE").size());
        junit.framework.Assert.assertTrue(componentHostMap.get("NAMENODE").contains("h1"));
        junit.framework.Assert.assertEquals(2, componentHostMap.get("DATANODE").size());
        junit.framework.Assert.assertTrue(componentHostMap.get("DATANODE").contains("h1"));
        junit.framework.Assert.assertTrue(componentHostMap.get("DATANODE").contains("h2"));
        junit.framework.Assert.assertEquals(1, componentHostMap.get("JOBTRACKER").size());
        junit.framework.Assert.assertTrue(componentHostMap.get("JOBTRACKER").contains("h1"));
        componentHostMap = c1.getServiceComponentHostMap(java.util.Collections.singleton("unknown"), null);
        junit.framework.Assert.assertEquals(0, componentHostMap.size());
    }

    @org.junit.Test
    public void testGetServiceComponentHostMap_ForHostAndService() throws java.lang.Exception {
        createDefaultCluster();
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion = helper.getOrCreateRepositoryVersion(c1);
        org.apache.ambari.server.state.Service sfHDFS = serviceFactory.createNew(c1, "HDFS", repositoryVersion);
        c1.addService(sfHDFS);
        org.apache.ambari.server.state.Service sfMR = serviceFactory.createNew(c1, "MAPREDUCE", repositoryVersion);
        c1.addService(sfMR);
        org.apache.ambari.server.state.ServiceComponent scNN = serviceComponentFactory.createNew(sfHDFS, "NAMENODE");
        sfHDFS.addServiceComponent(scNN);
        org.apache.ambari.server.state.ServiceComponentHost schNNH1 = serviceComponentHostFactory.createNew(scNN, "h1");
        scNN.addServiceComponentHost(schNNH1);
        org.apache.ambari.server.state.ServiceComponent scDN = serviceComponentFactory.createNew(sfHDFS, "DATANODE");
        sfHDFS.addServiceComponent(scDN);
        org.apache.ambari.server.state.ServiceComponentHost scDNH1 = serviceComponentHostFactory.createNew(scDN, "h1");
        scDN.addServiceComponentHost(scDNH1);
        org.apache.ambari.server.state.ServiceComponentHost scDNH2 = serviceComponentHostFactory.createNew(scDN, "h2");
        scDN.addServiceComponentHost(scDNH2);
        org.apache.ambari.server.state.ServiceComponent scJT = serviceComponentFactory.createNew(sfMR, "JOBTRACKER");
        sfMR.addServiceComponent(scJT);
        org.apache.ambari.server.state.ServiceComponentHost schJTH1 = serviceComponentHostFactory.createNew(scJT, "h1");
        scJT.addServiceComponentHost(schJTH1);
        java.util.Map<java.lang.String, java.util.Set<java.lang.String>> componentHostMap;
        componentHostMap = c1.getServiceComponentHostMap(java.util.Collections.singleton("h1"), java.util.Collections.singleton("HDFS"));
        junit.framework.Assert.assertEquals(2, componentHostMap.size());
        junit.framework.Assert.assertEquals(1, componentHostMap.get("DATANODE").size());
        junit.framework.Assert.assertTrue(componentHostMap.get("DATANODE").contains("h1"));
        junit.framework.Assert.assertEquals(1, componentHostMap.get("NAMENODE").size());
        junit.framework.Assert.assertTrue(componentHostMap.get("NAMENODE").contains("h1"));
        componentHostMap = c1.getServiceComponentHostMap(java.util.Collections.singleton("h2"), java.util.Collections.singleton("HDFS"));
        junit.framework.Assert.assertEquals(1, componentHostMap.size());
        junit.framework.Assert.assertEquals(1, componentHostMap.get("DATANODE").size());
        junit.framework.Assert.assertTrue(componentHostMap.get("DATANODE").contains("h2"));
        componentHostMap = c1.getServiceComponentHostMap(java.util.Collections.singleton("h3"), java.util.Collections.singleton("HDFS"));
        junit.framework.Assert.assertEquals(0, componentHostMap.size());
    }

    @org.junit.Test
    public void testGetAndSetConfigs() throws java.lang.Exception {
        createDefaultCluster();
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> c1PropAttributes = new java.util.HashMap<>();
        c1PropAttributes.put("final", new java.util.HashMap<>());
        c1PropAttributes.get("final").put("a", "true");
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> c2PropAttributes = new java.util.HashMap<>();
        c2PropAttributes.put("final", new java.util.HashMap<>());
        c2PropAttributes.get("final").put("x", "true");
        org.apache.ambari.server.state.Config config1 = configFactory.createNew(c1, "global", "version1", new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put("a", "b");
            }
        }, c1PropAttributes);
        org.apache.ambari.server.state.Config config2 = configFactory.createNew(c1, "global", "version2", new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put("x", "y");
            }
        }, c2PropAttributes);
        configFactory.createNew(c1, "core-site", "version2", new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put("x", "y");
            }
        }, new java.util.HashMap<>());
        c1.addDesiredConfig("_test", java.util.Collections.singleton(config1));
        org.apache.ambari.server.state.Config res = c1.getDesiredConfigByType("global");
        junit.framework.Assert.assertNotNull("Expected non-null config", res);
        junit.framework.Assert.assertEquals("true", res.getPropertiesAttributes().get("final").get("a"));
        res = c1.getDesiredConfigByType("core-site");
        junit.framework.Assert.assertNull("Expected null config", res);
        java.lang.Thread.sleep(1);
        c1.addDesiredConfig("_test", java.util.Collections.singleton(config2));
        res = c1.getDesiredConfigByType("global");
        junit.framework.Assert.assertEquals("Expected version tag to be 'version2'", "version2", res.getTag());
        junit.framework.Assert.assertEquals("true", res.getPropertiesAttributes().get("final").get("x"));
    }

    @org.junit.Test
    public void testDesiredConfigs() throws java.lang.Exception {
        createDefaultCluster();
        org.apache.ambari.server.state.Config config1 = configFactory.createNew(c1, "global", "version1", new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put("a", "b");
            }
        }, new java.util.HashMap<>());
        org.apache.ambari.server.state.Config config2 = configFactory.createNew(c1, "global", "version2", new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put("x", "y");
            }
        }, new java.util.HashMap<>());
        org.apache.ambari.server.state.Config config3 = configFactory.createNew(c1, "core-site", "version2", new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put("x", "y");
            }
        }, new java.util.HashMap<>());
        try {
            c1.addDesiredConfig(null, java.util.Collections.singleton(config1));
            org.junit.Assert.fail("Cannot set a null user with config");
        } catch (java.lang.Exception e) {
        }
        c1.addDesiredConfig("_test1", java.util.Collections.singleton(config1));
        java.lang.Thread.sleep(1);
        c1.addDesiredConfig("_test3", java.util.Collections.singleton(config3));
        java.util.Map<java.lang.String, org.apache.ambari.server.state.DesiredConfig> desiredConfigs = c1.getDesiredConfigs();
        junit.framework.Assert.assertFalse("Expect desired config not contain 'mapred-site'", desiredConfigs.containsKey("mapred-site"));
        junit.framework.Assert.assertTrue("Expect desired config contain " + config1.getType(), desiredConfigs.containsKey("global"));
        junit.framework.Assert.assertTrue("Expect desired config contain " + config3.getType(), desiredConfigs.containsKey("core-site"));
        junit.framework.Assert.assertEquals("Expect desired config for global should be " + config1.getTag(), config1.getTag(), desiredConfigs.get(config1.getType()).getTag());
        org.apache.ambari.server.state.DesiredConfig dc = desiredConfigs.get(config1.getType());
        junit.framework.Assert.assertTrue("Expect no host-level overrides", (null == dc.getHostOverrides()) || (dc.getHostOverrides().size() == 0));
        java.lang.Thread.sleep(1);
        c1.addDesiredConfig("_test2", java.util.Collections.singleton(config2));
        java.lang.Thread.sleep(1);
        c1.addDesiredConfig("_test1", java.util.Collections.singleton(config1));
        org.apache.ambari.server.state.Host host = clusters.getHost("h1");
        host.addDesiredConfig(c1.getClusterId(), true, "_test2", config2);
        desiredConfigs = c1.getDesiredConfigs();
        dc = desiredConfigs.get(config1.getType());
        junit.framework.Assert.assertNotNull("Expect host-level overrides", dc.getHostOverrides());
        junit.framework.Assert.assertEquals("Expect one host-level override", 1, dc.getHostOverrides().size());
    }

    @org.junit.Test
    public void testConvertToResponse() throws java.lang.Exception {
        createDefaultCluster();
        org.apache.ambari.server.controller.ClusterResponse r = c1.convertToResponse();
        junit.framework.Assert.assertEquals(c1.getClusterId(), r.getClusterId());
        junit.framework.Assert.assertEquals(c1.getClusterName(), r.getClusterName());
        junit.framework.Assert.assertEquals(2, r.getTotalHosts());
        junit.framework.Assert.assertEquals(0, r.getClusterHealthReport().getAlertStatusHosts());
        junit.framework.Assert.assertEquals(0, r.getClusterHealthReport().getHealthyStatusHosts());
        junit.framework.Assert.assertEquals(0, r.getClusterHealthReport().getUnhealthyStatusHosts());
        junit.framework.Assert.assertEquals(2, r.getClusterHealthReport().getUnknownStatusHosts());
        junit.framework.Assert.assertEquals(0, r.getClusterHealthReport().getStaleConfigsHosts());
        junit.framework.Assert.assertEquals(0, r.getClusterHealthReport().getMaintenanceStateHosts());
        junit.framework.Assert.assertEquals(0, r.getClusterHealthReport().getHealthyStateHosts());
        junit.framework.Assert.assertEquals(0, r.getClusterHealthReport().getHeartbeatLostStateHosts());
        junit.framework.Assert.assertEquals(2, r.getClusterHealthReport().getInitStateHosts());
        junit.framework.Assert.assertEquals(0, r.getClusterHealthReport().getUnhealthyStateHosts());
        clusters.addHost("h3");
        org.apache.ambari.server.state.Host host = clusters.getHost("h3");
        host.setIPv4("ipv4");
        host.setIPv6("ipv6");
        java.util.Map<java.lang.String, java.lang.String> hostAttributes = new java.util.HashMap<>();
        hostAttributes.put("os_family", "redhat");
        hostAttributes.put("os_release_version", "5.9");
        host.setHostAttributes(hostAttributes);
        host.setState(org.apache.ambari.server.state.HostState.HEALTHY);
        host.setHealthStatus(new org.apache.ambari.server.state.HostHealthStatus(org.apache.ambari.server.state.HostHealthStatus.HealthStatus.HEALTHY, ""));
        host.setStatus(host.getHealthStatus().getHealthStatus().name());
        c1.setDesiredStackVersion(new org.apache.ambari.server.state.StackId("HDP-2.0.6"));
        clusters.mapHostToCluster("h3", "c1");
        r = c1.convertToResponse();
        junit.framework.Assert.assertEquals(3, r.getTotalHosts());
        junit.framework.Assert.assertEquals(0, r.getClusterHealthReport().getAlertStatusHosts());
        junit.framework.Assert.assertEquals(1, r.getClusterHealthReport().getHealthyStatusHosts());
        junit.framework.Assert.assertEquals(0, r.getClusterHealthReport().getUnhealthyStatusHosts());
        junit.framework.Assert.assertEquals(2, r.getClusterHealthReport().getUnknownStatusHosts());
        junit.framework.Assert.assertEquals(0, r.getClusterHealthReport().getStaleConfigsHosts());
        junit.framework.Assert.assertEquals(0, r.getClusterHealthReport().getMaintenanceStateHosts());
        junit.framework.Assert.assertEquals(1, r.getClusterHealthReport().getHealthyStateHosts());
        junit.framework.Assert.assertEquals(0, r.getClusterHealthReport().getHeartbeatLostStateHosts());
        junit.framework.Assert.assertEquals(2, r.getClusterHealthReport().getInitStateHosts());
        junit.framework.Assert.assertEquals(0, r.getClusterHealthReport().getUnhealthyStateHosts());
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        c1.debugDump(sb);
    }

    @org.junit.Test
    public void testDeleteService() throws java.lang.Exception {
        createDefaultCluster();
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion = helper.getOrCreateRepositoryVersion(c1);
        c1.addService("MAPREDUCE", repositoryVersion);
        org.apache.ambari.server.state.Service hdfs = c1.addService("HDFS", repositoryVersion);
        hdfs.addServiceComponent("NAMENODE");
        org.junit.Assert.assertEquals(2, c1.getServices().size());
        org.junit.Assert.assertEquals(2, injector.getProvider(javax.persistence.EntityManager.class).get().createQuery("SELECT service FROM ClusterServiceEntity service").getResultList().size());
        c1.deleteService("HDFS", new org.apache.ambari.server.controller.internal.DeleteHostComponentStatusMetaData());
        org.junit.Assert.assertEquals(1, c1.getServices().size());
        org.junit.Assert.assertEquals(1, injector.getProvider(javax.persistence.EntityManager.class).get().createQuery("SELECT service FROM ClusterServiceEntity service").getResultList().size());
    }

    @org.junit.Test
    public void testDeleteServiceWithConfigHistory() throws java.lang.Exception {
        createDefaultCluster();
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion = helper.getOrCreateRepositoryVersion(c1);
        c1.addService("HDFS", repositoryVersion);
        org.apache.ambari.server.state.Config config1 = configFactory.createNew(c1, "hdfs-site", "version1", new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put("a", "b");
            }
        }, new java.util.HashMap<>());
        org.apache.ambari.server.state.Config config2 = configFactory.createNew(c1, "core-site", "version2", new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put("x", "y");
            }
        }, new java.util.HashMap<>());
        java.util.Set<org.apache.ambari.server.state.Config> configs = new java.util.HashSet<>();
        configs.add(config1);
        configs.add(config2);
        c1.addDesiredConfig("admin", configs);
        java.util.List<org.apache.ambari.server.controller.ServiceConfigVersionResponse> serviceConfigVersions = c1.getServiceConfigVersions();
        junit.framework.Assert.assertNotNull(serviceConfigVersions);
        junit.framework.Assert.assertEquals(1, serviceConfigVersions.size());
        junit.framework.Assert.assertEquals(java.lang.Long.valueOf(1), serviceConfigVersions.get(0).getVersion());
        junit.framework.Assert.assertEquals(2, c1.getDesiredConfigs().size());
        junit.framework.Assert.assertEquals("version1", c1.getDesiredConfigByType("hdfs-site").getTag());
        junit.framework.Assert.assertEquals("version2", c1.getDesiredConfigByType("core-site").getTag());
        java.util.Map<java.lang.String, java.util.Collection<org.apache.ambari.server.controller.ServiceConfigVersionResponse>> activeServiceConfigVersions = c1.getActiveServiceConfigVersions();
        junit.framework.Assert.assertEquals(1, activeServiceConfigVersions.size());
        c1.deleteService("HDFS", new org.apache.ambari.server.controller.internal.DeleteHostComponentStatusMetaData());
        junit.framework.Assert.assertEquals(0, c1.getServices().size());
        junit.framework.Assert.assertEquals(0, c1.getServiceConfigVersions().size());
        javax.persistence.EntityManager em = injector.getProvider(javax.persistence.EntityManager.class).get();
        junit.framework.Assert.assertEquals(0, em.createQuery("SELECT serviceConfig from ServiceConfigEntity serviceConfig").getResultList().size());
        java.util.List<org.apache.ambari.server.orm.entities.ClusterConfigEntity> clusterConfigs = em.createQuery("SELECT config from ClusterConfigEntity config", org.apache.ambari.server.orm.entities.ClusterConfigEntity.class).getResultList();
        junit.framework.Assert.assertEquals(2, clusterConfigs.size());
        for (org.apache.ambari.server.orm.entities.ClusterConfigEntity configEntity : clusterConfigs) {
            if (org.apache.commons.lang.StringUtils.equals(configEntity.getType(), "core-site")) {
                org.junit.Assert.assertEquals("core-site is not part of HDFS in test stack, should remain mapped to cluster", true, configEntity.isSelected());
            }
            if (org.apache.commons.lang.StringUtils.equals(configEntity.getType(), "hdfs-site")) {
                org.junit.Assert.assertEquals("hdfs-site should be unmapped from cluster when HDFS service is removed", false, configEntity.isSelected());
            }
        }
        junit.framework.Assert.assertEquals(0, em.createNativeQuery("SELECT * from serviceconfigmapping").getResultList().size());
    }

    @org.junit.Test
    public void testGetHostsDesiredConfigs() throws java.lang.Exception {
        createDefaultCluster();
        org.apache.ambari.server.state.Host host1 = clusters.getHost("h1");
        org.apache.ambari.server.orm.entities.HostEntity hostEntity1 = hostDAO.findByName("h1");
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> propAttributes = new java.util.HashMap<>();
        propAttributes.put("final", new java.util.HashMap<>());
        propAttributes.get("final").put("test", "true");
        org.apache.ambari.server.state.Config config = configFactory.createNew(c1, "hdfs-site", "1", new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put("test", "test");
            }
        }, propAttributes);
        host1.addDesiredConfig(c1.getClusterId(), true, "test", config);
        java.util.Map<java.lang.Long, java.util.Map<java.lang.String, org.apache.ambari.server.state.DesiredConfig>> configs = c1.getAllHostsDesiredConfigs();
        org.junit.Assert.assertTrue(configs.containsKey(hostEntity1.getHostId()));
        org.junit.Assert.assertEquals(1, configs.get(hostEntity1.getHostId()).size());
        java.util.List<java.lang.Long> hostIds = new java.util.ArrayList<>();
        hostIds.add(hostEntity1.getHostId());
        configs = c1.getHostsDesiredConfigs(hostIds);
        org.junit.Assert.assertTrue(configs.containsKey(hostEntity1.getHostId()));
        org.junit.Assert.assertEquals(1, configs.get(hostEntity1.getHostId()).size());
    }

    @org.junit.Test
    public void testProvisioningState() throws java.lang.Exception {
        createDefaultCluster();
        c1.setProvisioningState(org.apache.ambari.server.state.State.INIT);
        junit.framework.Assert.assertEquals(org.apache.ambari.server.state.State.INIT, c1.getProvisioningState());
        c1.setProvisioningState(org.apache.ambari.server.state.State.INSTALLED);
        junit.framework.Assert.assertEquals(org.apache.ambari.server.state.State.INSTALLED, c1.getProvisioningState());
    }

    @org.junit.Test
    public void testServiceConfigVersions() throws java.lang.Exception {
        createDefaultCluster();
        c1.addService("HDFS", helper.getOrCreateRepositoryVersion(new org.apache.ambari.server.state.StackId("HDP", "0.1"), "0.1"));
        org.apache.ambari.server.state.Config config1 = configFactory.createNew(c1, "hdfs-site", "version1", new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put("a", "b");
            }
        }, new java.util.HashMap<>());
        org.apache.ambari.server.state.Config config2 = configFactory.createNew(c1, "hdfs-site", "version2", new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put("x", "y");
            }
        }, new java.util.HashMap<>());
        c1.addDesiredConfig("admin", java.util.Collections.singleton(config1));
        java.util.List<org.apache.ambari.server.controller.ServiceConfigVersionResponse> serviceConfigVersions = c1.getServiceConfigVersions();
        junit.framework.Assert.assertNotNull(serviceConfigVersions);
        junit.framework.Assert.assertEquals(1, serviceConfigVersions.size());
        java.util.Map<java.lang.String, java.util.Collection<org.apache.ambari.server.controller.ServiceConfigVersionResponse>> activeServiceConfigVersions = c1.getActiveServiceConfigVersions();
        junit.framework.Assert.assertEquals(1, activeServiceConfigVersions.size());
        org.apache.ambari.server.controller.ServiceConfigVersionResponse hdfsResponse = activeServiceConfigVersions.get("HDFS").iterator().next();
        junit.framework.Assert.assertEquals("HDFS", hdfsResponse.getServiceName());
        junit.framework.Assert.assertEquals("c1", hdfsResponse.getClusterName());
        junit.framework.Assert.assertEquals("admin", hdfsResponse.getUserName());
        junit.framework.Assert.assertEquals("Default", hdfsResponse.getGroupName());
        junit.framework.Assert.assertEquals(java.lang.Long.valueOf(-1), hdfsResponse.getGroupId());
        junit.framework.Assert.assertEquals(java.lang.Long.valueOf(1), hdfsResponse.getVersion());
        c1.addDesiredConfig("admin", java.util.Collections.singleton(config2));
        serviceConfigVersions = c1.getServiceConfigVersions();
        junit.framework.Assert.assertNotNull(serviceConfigVersions);
        junit.framework.Assert.assertEquals(2, serviceConfigVersions.size());
        activeServiceConfigVersions = c1.getActiveServiceConfigVersions();
        junit.framework.Assert.assertEquals(1, activeServiceConfigVersions.size());
        hdfsResponse = activeServiceConfigVersions.get("HDFS").iterator().next();
        junit.framework.Assert.assertEquals("HDFS", hdfsResponse.getServiceName());
        junit.framework.Assert.assertEquals("c1", hdfsResponse.getClusterName());
        junit.framework.Assert.assertEquals("admin", hdfsResponse.getUserName());
        org.junit.Assert.assertEquals(java.lang.Long.valueOf(2), hdfsResponse.getVersion());
        c1.setServiceConfigVersion("HDFS", 1L, "admin", "test_note");
        serviceConfigVersions = c1.getServiceConfigVersions();
        junit.framework.Assert.assertNotNull(serviceConfigVersions);
        junit.framework.Assert.assertEquals(3, serviceConfigVersions.size());
        activeServiceConfigVersions = c1.getActiveServiceConfigVersions();
        junit.framework.Assert.assertEquals(1, activeServiceConfigVersions.size());
        hdfsResponse = activeServiceConfigVersions.get("HDFS").iterator().next();
        junit.framework.Assert.assertEquals("HDFS", hdfsResponse.getServiceName());
        junit.framework.Assert.assertEquals("c1", hdfsResponse.getClusterName());
        junit.framework.Assert.assertEquals("admin", hdfsResponse.getUserName());
        org.junit.Assert.assertEquals(java.lang.Long.valueOf(3), hdfsResponse.getVersion());
    }

    @org.junit.Test
    public void testSingleServiceVersionForMultipleConfigs() throws java.lang.Exception {
        createDefaultCluster();
        c1.addService("HDFS", helper.getOrCreateRepositoryVersion(new org.apache.ambari.server.state.StackId("HDP", "0.1"), "0.1"));
        org.apache.ambari.server.state.Config config1 = configFactory.createNew(c1, "hdfs-site", "version1", new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put("a", "b");
            }
        }, new java.util.HashMap<>());
        org.apache.ambari.server.state.Config config2 = configFactory.createNew(c1, "core-site", "version2", new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put("x", "y");
            }
        }, new java.util.HashMap<>());
        java.util.Set<org.apache.ambari.server.state.Config> configs = new java.util.HashSet<>();
        configs.add(config1);
        configs.add(config2);
        c1.addDesiredConfig("admin", configs);
        java.util.List<org.apache.ambari.server.controller.ServiceConfigVersionResponse> serviceConfigVersions = c1.getServiceConfigVersions();
        junit.framework.Assert.assertNotNull(serviceConfigVersions);
        junit.framework.Assert.assertEquals(1, serviceConfigVersions.size());
        junit.framework.Assert.assertEquals(java.lang.Long.valueOf(1), serviceConfigVersions.get(0).getVersion());
        junit.framework.Assert.assertEquals(2, c1.getDesiredConfigs().size());
        junit.framework.Assert.assertEquals("version1", c1.getDesiredConfigByType("hdfs-site").getTag());
        junit.framework.Assert.assertEquals("version2", c1.getDesiredConfigByType("core-site").getTag());
        java.util.Map<java.lang.String, java.util.Collection<org.apache.ambari.server.controller.ServiceConfigVersionResponse>> activeServiceConfigVersions = c1.getActiveServiceConfigVersions();
        junit.framework.Assert.assertEquals(1, activeServiceConfigVersions.size());
    }

    @org.junit.Test
    public void testServiceConfigVersionsForGroups() throws java.lang.Exception {
        createDefaultCluster();
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion = helper.getOrCreateRepositoryVersion(c1);
        c1.addService("HDFS", repositoryVersion);
        org.apache.ambari.server.state.Config config1 = configFactory.createNew(c1, "hdfs-site", "version1", new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put("a", "b");
            }
        }, new java.util.HashMap<>());
        org.apache.ambari.server.controller.ServiceConfigVersionResponse scvResponse = c1.addDesiredConfig("admin", java.util.Collections.singleton(config1));
        org.junit.Assert.assertEquals("SCV 1 should be created", java.lang.Long.valueOf(1), scvResponse.getVersion());
        java.util.Map<java.lang.String, java.util.Collection<org.apache.ambari.server.controller.ServiceConfigVersionResponse>> activeServiceConfigVersions = c1.getActiveServiceConfigVersions();
        junit.framework.Assert.assertEquals("Only one scv should be active", 1, activeServiceConfigVersions.get("HDFS").size());
        org.apache.ambari.server.state.Config config2 = configFactory.createNew(c1, "hdfs-site", "version2", new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put("a", "c");
            }
        }, new java.util.HashMap<>());
        org.apache.ambari.server.state.configgroup.ConfigGroup configGroup = configGroupFactory.createNew(c1, "HDFS", "test group", "HDFS", "descr", java.util.Collections.singletonMap("hdfs-site", config2), java.util.Collections.emptyMap());
        c1.addConfigGroup(configGroup);
        scvResponse = c1.createServiceConfigVersion("HDFS", "admin", "test note", configGroup);
        org.junit.Assert.assertEquals("SCV 2 should be created", java.lang.Long.valueOf(2), scvResponse.getVersion());
        activeServiceConfigVersions = c1.getActiveServiceConfigVersions();
        junit.framework.Assert.assertEquals("Two service config versions should be active, for default and test groups", 2, activeServiceConfigVersions.get("HDFS").size());
        org.apache.ambari.server.state.Config config3 = configFactory.createNew(c1, "hdfs-site", "version3", new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put("a", "d");
            }
        }, new java.util.HashMap<>());
        configGroup.setConfigurations(java.util.Collections.singletonMap("hdfs-site", config3));
        scvResponse = c1.createServiceConfigVersion("HDFS", "admin", "test note", configGroup);
        org.junit.Assert.assertEquals("SCV 3 should be created", java.lang.Long.valueOf(3), scvResponse.getVersion());
        activeServiceConfigVersions = c1.getActiveServiceConfigVersions();
        junit.framework.Assert.assertEquals("Two service config versions should be active, for default and test groups", 2, activeServiceConfigVersions.get("HDFS").size());
        org.junit.Assert.assertEquals(3, c1.getServiceConfigVersions().size());
        scvResponse = c1.setServiceConfigVersion("HDFS", 2L, "admin", "group rollback");
        org.junit.Assert.assertEquals("SCV 4 should be created", java.lang.Long.valueOf(4), scvResponse.getVersion());
        configGroup = c1.getConfigGroups().get(configGroup.getId());
        activeServiceConfigVersions = c1.getActiveServiceConfigVersions();
        junit.framework.Assert.assertEquals("Two service config versions should be active, for default and test groups", 2, activeServiceConfigVersions.get("HDFS").size());
        org.junit.Assert.assertEquals(4, c1.getServiceConfigVersions().size());
        java.util.Map<java.lang.String, java.lang.String> configProperties = configGroup.getConfigurations().get("hdfs-site").getProperties();
        org.junit.Assert.assertEquals("Configurations should be rolled back to a:c ", "c", configProperties.get("a"));
        org.apache.ambari.server.state.Config config4 = configFactory.createReadOnly("hdfs-site", "version4", java.util.Collections.singletonMap("a", "b"), null);
        org.apache.ambari.server.state.configgroup.ConfigGroup configGroup2 = configGroupFactory.createNew(c1, "HDFS", "test group 2", "HDFS", "descr", new java.util.HashMap<>(java.util.Collections.singletonMap("hdfs-site", config4)), java.util.Collections.emptyMap());
        c1.addConfigGroup(configGroup2);
        scvResponse = c1.createServiceConfigVersion("HDFS", "admin", "test note", configGroup2);
        org.junit.Assert.assertEquals("SCV 5 should be created", java.lang.Long.valueOf(5), scvResponse.getVersion());
        activeServiceConfigVersions = c1.getActiveServiceConfigVersions();
        junit.framework.Assert.assertEquals("Three service config versions should be active, for default and test groups", 3, activeServiceConfigVersions.get("HDFS").size());
        org.junit.Assert.assertEquals("Five total scvs", 5, c1.getServiceConfigVersions().size());
    }

    @org.junit.Test
    public void testAllServiceConfigVersionsWithConfigGroups() throws java.lang.Exception {
        createDefaultCluster();
        c1.addService("HDFS", helper.getOrCreateRepositoryVersion(new org.apache.ambari.server.state.StackId("HDP", "0.1"), "0.1"));
        org.apache.ambari.server.state.Config hdfsSiteConfigV1 = configFactory.createNew(c1, "hdfs-site", "version1", com.google.common.collect.ImmutableMap.of("p1", "v1"), com.google.common.collect.ImmutableMap.of());
        org.apache.ambari.server.controller.ServiceConfigVersionResponse hdfsSiteConfigResponseV1 = c1.addDesiredConfig("admin", java.util.Collections.singleton(hdfsSiteConfigV1));
        java.util.List<org.apache.ambari.server.controller.ConfigurationResponse> configResponsesDefaultGroup = java.util.Collections.singletonList(new org.apache.ambari.server.controller.ConfigurationResponse(c1.getClusterName(), hdfsSiteConfigV1.getStackId(), hdfsSiteConfigV1.getType(), hdfsSiteConfigV1.getTag(), hdfsSiteConfigV1.getVersion(), hdfsSiteConfigV1.getProperties(), hdfsSiteConfigV1.getPropertiesAttributes(), hdfsSiteConfigV1.getPropertiesTypes()));
        hdfsSiteConfigResponseV1.setConfigurations(configResponsesDefaultGroup);
        org.apache.ambari.server.state.Config hdfsSiteConfigV2 = configFactory.createNew(c1, "hdfs-site", "version2", com.google.common.collect.ImmutableMap.of("p1", "v2"), com.google.common.collect.ImmutableMap.of());
        org.apache.ambari.server.state.configgroup.ConfigGroup configGroup = configGroupFactory.createNew(c1, "HDFS", "configGroup1", "version1", "test description", com.google.common.collect.ImmutableMap.of(hdfsSiteConfigV2.getType(), hdfsSiteConfigV2), com.google.common.collect.ImmutableMap.of());
        c1.addConfigGroup(configGroup);
        org.apache.ambari.server.controller.ServiceConfigVersionResponse hdfsSiteConfigResponseV2 = c1.createServiceConfigVersion("HDFS", "admin", "test note", configGroup);
        hdfsSiteConfigResponseV2.setConfigurations(java.util.Collections.singletonList(new org.apache.ambari.server.controller.ConfigurationResponse(c1.getClusterName(), hdfsSiteConfigV2.getStackId(), hdfsSiteConfigV2.getType(), hdfsSiteConfigV2.getTag(), hdfsSiteConfigV2.getVersion(), hdfsSiteConfigV2.getProperties(), hdfsSiteConfigV2.getPropertiesAttributes(), hdfsSiteConfigV2.getPropertiesTypes())));
        hdfsSiteConfigResponseV2.setIsCurrent(true);
        org.apache.ambari.server.controller.ServiceConfigVersionResponse hdfsSiteConfigResponseV3 = c1.createServiceConfigVersion("HDFS", "admin", "new config in default group", null);
        hdfsSiteConfigResponseV3.setConfigurations(configResponsesDefaultGroup);
        hdfsSiteConfigResponseV3.setIsCurrent(true);
        java.util.List<org.apache.ambari.server.controller.ServiceConfigVersionResponse> expectedServiceConfigResponses = com.google.common.collect.ImmutableList.of(hdfsSiteConfigResponseV1, hdfsSiteConfigResponseV2, hdfsSiteConfigResponseV3);
        java.util.List<org.apache.ambari.server.controller.ServiceConfigVersionResponse> allServiceConfigResponses = c1.getServiceConfigVersions();
        java.util.Collections.sort(allServiceConfigResponses, new java.util.Comparator<org.apache.ambari.server.controller.ServiceConfigVersionResponse>() {
            @java.lang.Override
            public int compare(org.apache.ambari.server.controller.ServiceConfigVersionResponse o1, org.apache.ambari.server.controller.ServiceConfigVersionResponse o2) {
                return o1.getVersion().compareTo(o2.getVersion());
            }
        });
        org.junit.Assert.assertThat(allServiceConfigResponses, org.hamcrest.CoreMatchers.is(expectedServiceConfigResponses));
    }

    @org.junit.Test
    public void testAllServiceConfigVersionsWithDeletedConfigGroups() throws java.lang.Exception {
        createDefaultCluster();
        c1.addService("HDFS", helper.getOrCreateRepositoryVersion(new org.apache.ambari.server.state.StackId("HDP", "0.1"), "0.1"));
        org.apache.ambari.server.state.Config hdfsSiteConfigV1 = configFactory.createNew(c1, "hdfs-site", "version1", com.google.common.collect.ImmutableMap.of("p1", "v1"), com.google.common.collect.ImmutableMap.of());
        org.apache.ambari.server.controller.ServiceConfigVersionResponse hdfsSiteConfigResponseV1 = c1.addDesiredConfig("admin", java.util.Collections.singleton(hdfsSiteConfigV1));
        java.util.List<org.apache.ambari.server.controller.ConfigurationResponse> configResponsesDefaultGroup = java.util.Collections.singletonList(new org.apache.ambari.server.controller.ConfigurationResponse(c1.getClusterName(), hdfsSiteConfigV1.getStackId(), hdfsSiteConfigV1.getType(), hdfsSiteConfigV1.getTag(), hdfsSiteConfigV1.getVersion(), hdfsSiteConfigV1.getProperties(), hdfsSiteConfigV1.getPropertiesAttributes(), hdfsSiteConfigV1.getPropertiesTypes()));
        hdfsSiteConfigResponseV1.setConfigurations(configResponsesDefaultGroup);
        org.apache.ambari.server.state.Config hdfsSiteConfigV2 = configFactory.createNew(c1, "hdfs-site", "version2", com.google.common.collect.ImmutableMap.of("p1", "v2"), com.google.common.collect.ImmutableMap.of());
        org.apache.ambari.server.state.configgroup.ConfigGroup configGroup = configGroupFactory.createNew(c1, "HDFS", "configGroup1", "version1", "test description", com.google.common.collect.ImmutableMap.of(hdfsSiteConfigV2.getType(), hdfsSiteConfigV2), com.google.common.collect.ImmutableMap.of());
        c1.addConfigGroup(configGroup);
        org.apache.ambari.server.controller.ServiceConfigVersionResponse hdfsSiteConfigResponseV2 = c1.createServiceConfigVersion("HDFS", "admin", "test note", configGroup);
        hdfsSiteConfigResponseV2.setConfigurations(java.util.Collections.singletonList(new org.apache.ambari.server.controller.ConfigurationResponse(c1.getClusterName(), hdfsSiteConfigV2.getStackId(), hdfsSiteConfigV2.getType(), hdfsSiteConfigV2.getTag(), hdfsSiteConfigV2.getVersion(), hdfsSiteConfigV2.getProperties(), hdfsSiteConfigV2.getPropertiesAttributes(), hdfsSiteConfigV2.getPropertiesTypes())));
        c1.deleteConfigGroup(configGroup.getId());
        org.apache.ambari.server.controller.ServiceConfigVersionResponse hdfsSiteConfigResponseV3 = c1.createServiceConfigVersion("HDFS", "admin", "new config in default group", null);
        hdfsSiteConfigResponseV3.setConfigurations(configResponsesDefaultGroup);
        hdfsSiteConfigResponseV3.setIsCurrent(true);
        java.util.List<org.apache.ambari.server.controller.ServiceConfigVersionResponse> allServiceConfigResponses = c1.getServiceConfigVersions();
        java.util.Collections.sort(allServiceConfigResponses, new java.util.Comparator<org.apache.ambari.server.controller.ServiceConfigVersionResponse>() {
            @java.lang.Override
            public int compare(org.apache.ambari.server.controller.ServiceConfigVersionResponse o1, org.apache.ambari.server.controller.ServiceConfigVersionResponse o2) {
                return o1.getVersion().compareTo(o2.getVersion());
            }
        });
        org.junit.Assert.assertEquals(3, allServiceConfigResponses.size());
        org.junit.Assert.assertEquals(false, allServiceConfigResponses.get(0).getIsCurrent());
        org.junit.Assert.assertEquals(org.apache.ambari.server.controller.ServiceConfigVersionResponse.DEFAULT_CONFIG_GROUP_NAME, allServiceConfigResponses.get(0).getGroupName());
        org.junit.Assert.assertEquals(true, allServiceConfigResponses.get(2).getIsCurrent());
        org.junit.Assert.assertEquals(org.apache.ambari.server.controller.ServiceConfigVersionResponse.DEFAULT_CONFIG_GROUP_NAME, allServiceConfigResponses.get(2).getGroupName());
        org.junit.Assert.assertEquals(false, allServiceConfigResponses.get(1).getIsCurrent());
        org.junit.Assert.assertEquals(org.apache.ambari.server.controller.ServiceConfigVersionResponse.DELETED_CONFIG_GROUP_NAME, allServiceConfigResponses.get(1).getGroupName());
    }

    @org.junit.Test
    public void testTransitionHostsToInstalling() throws java.lang.Exception {
        org.apache.ambari.server.state.StackId originalStackId = new org.apache.ambari.server.state.StackId("HDP", "2.0.5");
        createDefaultCluster(com.google.common.collect.Sets.newHashSet("h1", "h2"), originalStackId);
        java.util.List<org.apache.ambari.server.orm.entities.HostVersionEntity> hostVersionsH1Before = hostVersionDAO.findByClusterAndHost("c1", "h1");
        org.junit.Assert.assertEquals(1, hostVersionsH1Before.size());
        org.apache.ambari.server.state.StackId stackId = new org.apache.ambari.server.state.StackId("HDP", "2.0.6");
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion = helper.getOrCreateRepositoryVersion(stackId, stackId.getStackVersion());
        c1.transitionHostsToInstalling(repositoryVersion, null, false);
        java.util.List<org.apache.ambari.server.orm.entities.HostVersionEntity> hostVersionsH1After = hostVersionDAO.findByClusterAndHost("c1", "h1");
        org.junit.Assert.assertEquals(2, hostVersionsH1After.size());
        boolean checked = false;
        for (org.apache.ambari.server.orm.entities.HostVersionEntity entity : hostVersionsH1After) {
            org.apache.ambari.server.orm.entities.StackEntity repoVersionStackEntity = entity.getRepositoryVersion().getStack();
            if (repoVersionStackEntity.getStackName().equals("HDP") && repoVersionStackEntity.getStackVersion().equals("2.0.6")) {
                org.junit.Assert.assertEquals(org.apache.ambari.server.state.RepositoryVersionState.NOT_REQUIRED, entity.getState());
                checked = true;
                break;
            }
        }
        org.junit.Assert.assertTrue(checked);
        org.apache.ambari.server.state.Service hdfs = serviceFactory.createNew(c1, "HDFS", repositoryVersion);
        c1.addService(hdfs);
        org.apache.ambari.server.state.ServiceComponent datanode = serviceComponentFactory.createNew(hdfs, "NAMENODE");
        org.apache.ambari.server.state.ServiceComponent namenode = serviceComponentFactory.createNew(hdfs, "DATANODE");
        hdfs.addServiceComponent(datanode);
        hdfs.addServiceComponent(namenode);
        org.apache.ambari.server.state.ServiceComponentHost namenodeHost1 = serviceComponentHostFactory.createNew(namenode, "h1");
        org.apache.ambari.server.state.ServiceComponentHost datanodeHost2 = serviceComponentHostFactory.createNew(datanode, "h2");
        org.junit.Assert.assertNotNull(namenodeHost1);
        org.junit.Assert.assertNotNull(datanodeHost2);
        c1.transitionHostsToInstalling(repositoryVersion, null, false);
        hostVersionsH1After = hostVersionDAO.findByClusterAndHost("c1", "h1");
        org.junit.Assert.assertEquals(2, hostVersionsH1After.size());
        checked = false;
        for (org.apache.ambari.server.orm.entities.HostVersionEntity entity : hostVersionsH1After) {
            org.apache.ambari.server.orm.entities.StackEntity repoVersionStackEntity = entity.getRepositoryVersion().getStack();
            if (repoVersionStackEntity.getStackName().equals("HDP") && repoVersionStackEntity.getStackVersion().equals("2.0.6")) {
                org.junit.Assert.assertEquals(org.apache.ambari.server.state.RepositoryVersionState.INSTALLING, entity.getState());
                checked = true;
                break;
            }
        }
        org.junit.Assert.assertTrue(checked);
        java.util.List<org.apache.ambari.server.orm.entities.HostVersionEntity> hostVersionEntities = hostVersionDAO.findAll();
        for (org.apache.ambari.server.orm.entities.HostVersionEntity hostVersionEntity : hostVersionEntities) {
            hostVersionEntity.setState(org.apache.ambari.server.state.RepositoryVersionState.INSTALL_FAILED);
            hostVersionDAO.merge(hostVersionEntity);
        }
        hostVersionEntities = hostVersionDAO.findAll();
        for (org.apache.ambari.server.orm.entities.HostVersionEntity hostVersionEntity : hostVersionEntities) {
            org.junit.Assert.assertEquals(org.apache.ambari.server.state.RepositoryVersionState.INSTALL_FAILED, hostVersionEntity.getState());
        }
        java.util.Collection<org.apache.ambari.server.state.Host> hosts = c1.getHosts();
        java.util.Iterator<org.apache.ambari.server.state.Host> iterator = hosts.iterator();
        org.apache.ambari.server.state.Host hostInMaintenanceMode = iterator.next();
        org.apache.ambari.server.state.Host hostNotInMaintenanceMode = iterator.next();
        hostInMaintenanceMode.setMaintenanceState(c1.getClusterId(), org.apache.ambari.server.state.MaintenanceState.ON);
        c1.transitionHostsToInstalling(repositoryVersion, null, false);
        java.util.List<org.apache.ambari.server.orm.entities.HostVersionEntity> hostInMaintModeVersions = hostVersionDAO.findByClusterAndHost("c1", hostInMaintenanceMode.getHostName());
        java.util.List<org.apache.ambari.server.orm.entities.HostVersionEntity> otherHostVersions = hostVersionDAO.findByClusterAndHost("c1", hostNotInMaintenanceMode.getHostName());
        for (org.apache.ambari.server.orm.entities.HostVersionEntity hostVersionEntity : hostInMaintModeVersions) {
            org.apache.ambari.server.orm.entities.StackEntity repoVersionStackEntity = hostVersionEntity.getRepositoryVersion().getStack();
            if (repoVersionStackEntity.getStackName().equals("HDP") && repoVersionStackEntity.getStackVersion().equals("2.0.6")) {
                org.junit.Assert.assertEquals(org.apache.ambari.server.state.RepositoryVersionState.OUT_OF_SYNC, hostVersionEntity.getState());
            }
        }
        for (org.apache.ambari.server.orm.entities.HostVersionEntity hostVersionEntity : otherHostVersions) {
            org.apache.ambari.server.orm.entities.StackEntity repoVersionStackEntity = hostVersionEntity.getRepositoryVersion().getStack();
            if (repoVersionStackEntity.getStackName().equals("HDP") && repoVersionStackEntity.getStackVersion().equals("2.0.6")) {
                org.junit.Assert.assertEquals(org.apache.ambari.server.state.RepositoryVersionState.INSTALLING, hostVersionEntity.getState());
            }
        }
    }

    @org.junit.Test
    public void testTransitionHostVersionAdvanced() throws java.lang.Exception {
        java.lang.String clusterName = "c1";
        java.lang.String v1 = "2.2.0-123";
        org.apache.ambari.server.state.StackId stackId = new org.apache.ambari.server.state.StackId("HDP-2.2.0");
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity rv1 = helper.getOrCreateRepositoryVersion(stackId, v1);
        java.util.Map<java.lang.String, java.lang.String> hostAttributes = new java.util.HashMap<>();
        hostAttributes.put("os_family", "redhat");
        hostAttributes.put("os_release_version", "6.4");
        org.apache.ambari.server.state.Cluster cluster = createClusterForRU(clusterName, rv1, hostAttributes);
        int versionedComponentCount = 0;
        java.util.List<org.apache.ambari.server.orm.entities.HostComponentStateEntity> hostComponentStates = hostComponentStateDAO.findAll();
        for (int i = 0; i < hostComponentStates.size(); i++) {
            org.apache.ambari.server.orm.entities.HostComponentStateEntity hce = hostComponentStates.get(i);
            org.apache.ambari.server.state.ComponentInfo compInfo = metaInfo.getComponent(stackId.getStackName(), stackId.getStackVersion(), hce.getServiceName(), hce.getComponentName());
            if (compInfo.isVersionAdvertised()) {
                hce.setVersion(v1);
                hostComponentStateDAO.merge(hce);
                versionedComponentCount++;
            }
            org.apache.ambari.server.state.Service svc = cluster.getService(hce.getServiceName());
            org.apache.ambari.server.state.ServiceComponent svcComp = svc.getServiceComponent(hce.getComponentName());
            org.apache.ambari.server.state.ServiceComponentHost scHost = svcComp.getServiceComponentHost(hce.getHostName());
            scHost.recalculateHostVersionState();
            if (versionedComponentCount > 0) {
                org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion = repositoryVersionDAO.findByStackAndVersion(stackId, v1);
                junit.framework.Assert.assertNotNull(repositoryVersion);
            }
        }
        addHost("h-4", hostAttributes);
        clusters.mapHostToCluster("h-4", clusterName);
        org.apache.ambari.server.state.Service svc2 = cluster.getService("ZOOKEEPER");
        org.apache.ambari.server.state.Service svc3 = cluster.getService("GANGLIA");
        org.apache.ambari.server.state.ServiceComponent sc2CompA = svc2.getServiceComponent("ZOOKEEPER_SERVER");
        org.apache.ambari.server.state.ServiceComponent sc2CompB = svc2.getServiceComponent("ZOOKEEPER_CLIENT");
        org.apache.ambari.server.state.ServiceComponent sc3CompB = svc3.getServiceComponent("GANGLIA_MONITOR");
        org.apache.ambari.server.state.ServiceComponentHost schHost4Serv2CompA = serviceComponentHostFactory.createNew(sc2CompA, "h-4");
        org.apache.ambari.server.state.ServiceComponentHost schHost4Serv2CompB = serviceComponentHostFactory.createNew(sc2CompB, "h-4");
        org.apache.ambari.server.state.ServiceComponentHost schHost4Serv3CompB = serviceComponentHostFactory.createNew(sc3CompB, "h-4");
        sc2CompA.addServiceComponentHost(schHost4Serv2CompA);
        sc2CompB.addServiceComponentHost(schHost4Serv2CompB);
        sc3CompB.addServiceComponentHost(schHost4Serv3CompB);
        simulateStackVersionListener(stackId, v1, cluster, hostComponentStateDAO.findByHost("h-4"));
        java.util.Collection<org.apache.ambari.server.orm.entities.HostVersionEntity> hostVersions = hostVersionDAO.findAll();
        junit.framework.Assert.assertEquals(hostVersions.size(), clusters.getHosts().size());
        org.apache.ambari.server.orm.entities.HostVersionEntity h4Version1 = hostVersionDAO.findByClusterStackVersionAndHost(clusterName, stackId, v1, "h-4");
        junit.framework.Assert.assertNotNull(h4Version1);
        junit.framework.Assert.assertEquals(h4Version1.getState(), org.apache.ambari.server.state.RepositoryVersionState.CURRENT);
        java.lang.String v2 = "2.2.0-456";
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity rv2 = helper.getOrCreateRepositoryVersion(stackId, v2);
        for (java.lang.String hostName : clusters.getHostsForCluster(clusterName).keySet()) {
            org.apache.ambari.server.orm.entities.HostEntity host = hostDAO.findByName(hostName);
            org.apache.ambari.server.orm.entities.HostVersionEntity hve = new org.apache.ambari.server.orm.entities.HostVersionEntity(host, rv2, org.apache.ambari.server.state.RepositoryVersionState.INSTALLED);
            hostVersionDAO.create(hve);
        }
        org.apache.ambari.server.state.Host host5 = addHost("h-5", hostAttributes);
        clusters.mapAndPublishHostsToCluster(java.util.Collections.singleton("h-5"), clusterName);
        org.apache.ambari.server.orm.entities.HostVersionEntity h5Version1 = hostVersionDAO.findHostVersionByHostAndRepository(host5.getHostEntity(), rv1);
        org.apache.ambari.server.orm.entities.HostVersionEntity h5Version2 = hostVersionDAO.findHostVersionByHostAndRepository(host5.getHostEntity(), rv2);
        junit.framework.Assert.assertEquals(org.apache.ambari.server.state.RepositoryVersionState.NOT_REQUIRED, h5Version1.getState());
        junit.framework.Assert.assertEquals(org.apache.ambari.server.state.RepositoryVersionState.NOT_REQUIRED, h5Version2.getState());
        org.apache.ambari.server.state.ServiceComponentHost schHost5Serv3CompB = serviceComponentHostFactory.createNew(sc3CompB, "h-5");
        sc3CompB.addServiceComponentHost(schHost5Serv3CompB);
        h5Version2 = hostVersionDAO.findByClusterStackVersionAndHost(clusterName, stackId, v2, "h-5");
        junit.framework.Assert.assertNotNull(h5Version2);
        junit.framework.Assert.assertEquals(org.apache.ambari.server.state.RepositoryVersionState.NOT_REQUIRED, h5Version2.getState());
        h5Version2.setState(org.apache.ambari.server.state.RepositoryVersionState.INSTALLED);
        hostVersionDAO.merge(h5Version2);
        versionedComponentCount = 0;
        hostComponentStates = hostComponentStateDAO.findAll();
        for (int i = 0; i < hostComponentStates.size(); i++) {
            org.apache.ambari.server.orm.entities.HostComponentStateEntity hce = hostComponentStates.get(i);
            org.apache.ambari.server.state.ComponentInfo compInfo = metaInfo.getComponent(stackId.getStackName(), stackId.getStackVersion(), hce.getServiceName(), hce.getComponentName());
            if (compInfo.isVersionAdvertised()) {
                hce.setVersion(v2);
                hostComponentStateDAO.merge(hce);
                versionedComponentCount++;
            }
            org.apache.ambari.server.state.Service svc = cluster.getService(hce.getServiceName());
            org.apache.ambari.server.state.ServiceComponent svcComp = svc.getServiceComponent(hce.getComponentName());
            org.apache.ambari.server.state.ServiceComponentHost scHost = svcComp.getServiceComponentHost(hce.getHostName());
            scHost.recalculateHostVersionState();
            if (versionedComponentCount > 0) {
                org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion = repositoryVersionDAO.findByStackAndVersion(stackId, v2);
                junit.framework.Assert.assertNotNull(repositoryVersion);
            }
        }
        java.util.Collection<org.apache.ambari.server.orm.entities.HostVersionEntity> v2HostVersions = hostVersionDAO.findByClusterStackAndVersion(clusterName, stackId, v2);
        junit.framework.Assert.assertEquals(v2HostVersions.size(), clusters.getHostsForCluster(clusterName).size());
        for (org.apache.ambari.server.orm.entities.HostVersionEntity hve : v2HostVersions) {
            junit.framework.Assert.assertTrue(org.apache.ambari.server.state.cluster.ClusterTest.TERMINAL_VERSION_STATES.contains(hve.getState()));
        }
    }

    @org.junit.Test
    public void testBootstrapHostVersion() throws java.lang.Exception {
        java.lang.String clusterName = "c1";
        java.lang.String v1 = "2.2.0-123";
        org.apache.ambari.server.state.StackId stackId = new org.apache.ambari.server.state.StackId("HDP-2.2.0");
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity rv1 = helper.getOrCreateRepositoryVersion(stackId, v1);
        java.util.Map<java.lang.String, java.lang.String> hostAttributes = new java.util.HashMap<>();
        hostAttributes.put("os_family", "redhat");
        hostAttributes.put("os_release_version", "6.4");
        org.apache.ambari.server.state.Cluster cluster = createClusterForRU(clusterName, rv1, hostAttributes);
        org.apache.ambari.server.state.Host deadHost = cluster.getHosts().iterator().next();
        deadHost.setState(org.apache.ambari.server.state.HostState.UNHEALTHY);
        int versionedComponentCount = 0;
        java.util.List<org.apache.ambari.server.orm.entities.HostComponentStateEntity> hostComponentStates = hostComponentStateDAO.findAll();
        for (int i = 0; i < hostComponentStates.size(); i++) {
            org.apache.ambari.server.orm.entities.HostComponentStateEntity hce = hostComponentStates.get(i);
            org.apache.ambari.server.state.ComponentInfo compInfo = metaInfo.getComponent(stackId.getStackName(), stackId.getStackVersion(), hce.getServiceName(), hce.getComponentName());
            if (hce.getHostName().equals(deadHost.getHostName())) {
                continue;
            }
            if (compInfo.isVersionAdvertised()) {
                hce.setVersion(v1);
                hostComponentStateDAO.merge(hce);
                versionedComponentCount++;
            }
            org.apache.ambari.server.state.Service svc = cluster.getService(hce.getServiceName());
            org.apache.ambari.server.state.ServiceComponent svcComp = svc.getServiceComponent(hce.getComponentName());
            org.apache.ambari.server.state.ServiceComponentHost scHost = svcComp.getServiceComponentHost(hce.getHostName());
            scHost.recalculateHostVersionState();
            if (versionedComponentCount > 0) {
                org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion = repositoryVersionDAO.findByStackAndVersion(stackId, v1);
                junit.framework.Assert.assertNotNull(repositoryVersion);
            }
        }
    }

    @org.junit.Test
    public void testTransitionNonReportableHost() throws java.lang.Exception {
        org.apache.ambari.server.state.StackId stackId = new org.apache.ambari.server.state.StackId("HDP-2.0.5");
        java.lang.String clusterName = "c1";
        clusters.addCluster(clusterName, stackId);
        org.apache.ambari.server.state.Cluster c1 = clusters.getCluster(clusterName);
        junit.framework.Assert.assertEquals(clusterName, c1.getClusterName());
        clusters.addHost("h-1");
        clusters.addHost("h-2");
        clusters.addHost("h-3");
        for (java.lang.String hostName : new java.lang.String[]{ "h-1", "h-2", "h-3" }) {
            org.apache.ambari.server.state.Host h = clusters.getHost(hostName);
            h.setIPv4("ipv4");
            h.setIPv6("ipv6");
            java.util.Map<java.lang.String, java.lang.String> hostAttributes = new java.util.HashMap<>();
            hostAttributes.put("os_family", "redhat");
            hostAttributes.put("os_release_version", "5.9");
            h.setHostAttributes(hostAttributes);
        }
        java.lang.String v1 = "2.0.5-1";
        java.lang.String v2 = "2.0.5-2";
        c1.setDesiredStackVersion(stackId);
        helper.getOrCreateRepositoryVersion(stackId, v1);
        helper.getOrCreateRepositoryVersion(stackId, v2);
        c1.setCurrentStackVersion(stackId);
        clusters.mapHostToCluster("h-1", clusterName);
        clusters.mapHostToCluster("h-2", clusterName);
        clusters.mapHostToCluster("h-3", clusterName);
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion = helper.getOrCreateRepositoryVersion(c1);
        org.apache.ambari.server.state.Service service = c1.addService("ZOOKEEPER", repositoryVersion);
        org.apache.ambari.server.state.ServiceComponent sc = service.addServiceComponent("ZOOKEEPER_SERVER");
        sc.addServiceComponentHost("h-1");
        sc.addServiceComponentHost("h-2");
        service = c1.addService("SQOOP", repositoryVersion);
        sc = service.addServiceComponent("SQOOP");
        sc.addServiceComponentHost("h-3");
        org.apache.ambari.server.orm.entities.HostEntity hostEntity = hostDAO.findByName("h-3");
        org.junit.Assert.assertNotNull(hostEntity);
        java.util.List<org.apache.ambari.server.orm.entities.HostVersionEntity> entities = hostVersionDAO.findByClusterAndHost(clusterName, "h-3");
        org.junit.Assert.assertTrue("Expected no host versions", (null == entities) || (0 == entities.size()));
        java.util.List<org.apache.ambari.server.state.ServiceComponentHost> componentsOnHost3 = c1.getServiceComponentHosts("h-3");
        componentsOnHost3.iterator().next().recalculateHostVersionState();
        entities = hostVersionDAO.findByClusterAndHost(clusterName, "h-3");
        org.junit.Assert.assertEquals(1, entities.size());
    }

    @org.junit.Test
    public void testClusterConfigMergingWithoutNewVersion() throws java.lang.Exception {
        createDefaultCluster();
        org.apache.ambari.server.state.Cluster cluster = clusters.getCluster("c1");
        org.apache.ambari.server.orm.entities.ClusterEntity clusterEntity = clusterDAO.findByName("c1");
        org.junit.Assert.assertEquals(0, clusterEntity.getClusterConfigEntities().size());
        final org.apache.ambari.server.state.Config originalConfig = configFactory.createNew(cluster, "foo-site", "version3", new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put("one", "two");
            }
        }, new java.util.HashMap<>());
        org.apache.ambari.server.state.configgroup.ConfigGroup configGroup = configGroupFactory.createNew(cluster, "HDFS", "g1", "t1", "", new java.util.HashMap<java.lang.String, org.apache.ambari.server.state.Config>() {
            {
                put("foo-site", originalConfig);
            }
        }, java.util.Collections.emptyMap());
        cluster.addConfigGroup(configGroup);
        clusterEntity = clusterDAO.findByName("c1");
        org.junit.Assert.assertEquals(1, clusterEntity.getClusterConfigEntities().size());
        java.util.Map<java.lang.String, org.apache.ambari.server.state.Config> configsByType = cluster.getConfigsByType("foo-site");
        org.apache.ambari.server.state.Config config = configsByType.entrySet().iterator().next().getValue();
        java.util.Map<java.lang.String, java.lang.String> properties = config.getProperties();
        properties.put("three", "four");
        config.setProperties(properties);
        config.save();
        clusterEntity = clusterDAO.findByName("c1");
        org.junit.Assert.assertEquals(1, clusterEntity.getClusterConfigEntities().size());
        org.apache.ambari.server.orm.entities.ClusterConfigEntity clusterConfigEntity = clusterEntity.getClusterConfigEntities().iterator().next();
        org.junit.Assert.assertTrue(clusterConfigEntity.getData().contains("one"));
        org.junit.Assert.assertTrue(clusterConfigEntity.getData().contains("two"));
        org.junit.Assert.assertTrue(clusterConfigEntity.getData().contains("three"));
        org.junit.Assert.assertTrue(clusterConfigEntity.getData().contains("four"));
        cluster.refresh();
        clusterEntity = clusterDAO.findByName("c1");
        org.junit.Assert.assertEquals(1, clusterEntity.getClusterConfigEntities().size());
        clusterConfigEntity = clusterEntity.getClusterConfigEntities().iterator().next();
        org.junit.Assert.assertTrue(clusterConfigEntity.getData().contains("one"));
        org.junit.Assert.assertTrue(clusterConfigEntity.getData().contains("two"));
        org.junit.Assert.assertTrue(clusterConfigEntity.getData().contains("three"));
        org.junit.Assert.assertTrue(clusterConfigEntity.getData().contains("four"));
    }

    @org.junit.Test
    public void testApplyLatestConfigurations() throws java.lang.Exception {
        org.apache.ambari.server.state.StackId stackId = new org.apache.ambari.server.state.StackId("HDP-2.0.6");
        org.apache.ambari.server.state.StackId newStackId = new org.apache.ambari.server.state.StackId("HDP-2.2.0");
        createDefaultCluster(com.google.common.collect.Sets.newHashSet("host-1"), stackId);
        org.apache.ambari.server.state.Cluster cluster = clusters.getCluster("c1");
        org.apache.ambari.server.orm.entities.ClusterEntity clusterEntity = clusterDAO.findByName("c1");
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repoVersion220 = helper.getOrCreateRepositoryVersion(newStackId, "2.2.0-1234");
        org.apache.ambari.server.orm.entities.StackEntity currentStack = stackDAO.find(stackId.getStackName(), stackId.getStackVersion());
        org.apache.ambari.server.orm.entities.StackEntity newStack = stackDAO.find(newStackId.getStackName(), newStackId.getStackVersion());
        junit.framework.Assert.assertFalse(stackId.equals(newStackId));
        java.lang.String serviceName = "ZOOKEEPER";
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion = helper.getOrCreateRepositoryVersion(c1);
        org.apache.ambari.server.state.Service service = cluster.addService(serviceName, repositoryVersion);
        java.lang.String configType = "zoo.cfg";
        org.apache.ambari.server.orm.entities.ClusterConfigEntity clusterConfig1 = new org.apache.ambari.server.orm.entities.ClusterConfigEntity();
        clusterConfig1.setClusterEntity(clusterEntity);
        clusterConfig1.setConfigId(1L);
        clusterConfig1.setStack(currentStack);
        clusterConfig1.setTag("version-1");
        clusterConfig1.setData("{}");
        clusterConfig1.setType(configType);
        clusterConfig1.setTimestamp(1L);
        clusterConfig1.setVersion(1L);
        clusterConfig1.setSelected(true);
        clusterDAO.createConfig(clusterConfig1);
        clusterEntity.getClusterConfigEntities().add(clusterConfig1);
        clusterEntity = clusterDAO.merge(clusterEntity);
        org.apache.ambari.server.state.Config config = configFactory.createExisting(cluster, clusterConfig1);
        cluster.addConfig(config);
        cluster.createServiceConfigVersion(serviceName, "", "version-1", null);
        org.apache.ambari.server.orm.entities.ClusterConfigEntity clusterConfig2 = new org.apache.ambari.server.orm.entities.ClusterConfigEntity();
        clusterConfig2.setClusterEntity(clusterEntity);
        clusterConfig2.setConfigId(2L);
        clusterConfig2.setStack(newStack);
        clusterConfig2.setTag("version-2");
        clusterConfig2.setData("{}");
        clusterConfig2.setType(configType);
        clusterConfig2.setTimestamp(2L);
        clusterConfig2.setVersion(2L);
        clusterConfig2.setSelected(false);
        clusterDAO.createConfig(clusterConfig2);
        clusterEntity.getClusterConfigEntities().add(clusterConfig2);
        clusterEntity = clusterDAO.merge(clusterEntity);
        config = configFactory.createExisting(cluster, clusterConfig1);
        cluster.addConfig(config);
        service.setDesiredRepositoryVersion(repoVersion220);
        cluster.createServiceConfigVersion(serviceName, "", "version-2", null);
        java.util.Collection<org.apache.ambari.server.orm.entities.ClusterConfigEntity> clusterConfigs = clusterEntity.getClusterConfigEntities();
        junit.framework.Assert.assertEquals(2, clusterConfigs.size());
        for (org.apache.ambari.server.orm.entities.ClusterConfigEntity clusterConfig : clusterConfigs) {
            if (clusterConfig.getTag().equals("version-1")) {
                junit.framework.Assert.assertTrue(clusterConfig.isSelected());
            } else {
                junit.framework.Assert.assertFalse(clusterConfig.isSelected());
            }
        }
        cluster.applyLatestConfigurations(newStackId, serviceName);
        clusterEntity = clusterDAO.findByName("c1");
        clusterConfigs = clusterEntity.getClusterConfigEntities();
        junit.framework.Assert.assertEquals(2, clusterConfigs.size());
        for (org.apache.ambari.server.orm.entities.ClusterConfigEntity clusterConfig : clusterConfigs) {
            if (clusterConfig.getTag().equals("version-1")) {
                junit.framework.Assert.assertFalse(clusterConfig.isSelected());
            } else {
                junit.framework.Assert.assertTrue(clusterConfig.isSelected());
            }
        }
    }

    @org.junit.Test
    public void testApplyLatestConfigurationsToPreviousStack() throws java.lang.Exception {
        org.apache.ambari.server.state.StackId stackId = new org.apache.ambari.server.state.StackId("HDP-2.0.6");
        org.apache.ambari.server.state.StackId newStackId = new org.apache.ambari.server.state.StackId("HDP-2.2.0");
        createDefaultCluster(com.google.common.collect.Sets.newHashSet("host-1"), stackId);
        org.apache.ambari.server.state.Cluster cluster = clusters.getCluster("c1");
        org.apache.ambari.server.orm.entities.ClusterEntity clusterEntity = clusterDAO.findByName("c1");
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repoVersion220 = helper.getOrCreateRepositoryVersion(newStackId, "2.2.0-1234");
        org.apache.ambari.server.orm.entities.StackEntity currentStack = stackDAO.find(stackId.getStackName(), stackId.getStackVersion());
        org.apache.ambari.server.orm.entities.StackEntity newStack = stackDAO.find(newStackId.getStackName(), newStackId.getStackVersion());
        junit.framework.Assert.assertFalse(stackId.equals(newStackId));
        java.lang.String serviceName = "ZOOKEEPER";
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion = helper.getOrCreateRepositoryVersion(c1);
        org.apache.ambari.server.state.Service service = cluster.addService(serviceName, repositoryVersion);
        java.lang.String configType = "zoo.cfg";
        for (int i = 1; i <= 5; i++) {
            org.apache.ambari.server.orm.entities.ClusterConfigEntity clusterConfig = new org.apache.ambari.server.orm.entities.ClusterConfigEntity();
            clusterConfig.setClusterEntity(clusterEntity);
            clusterConfig.setConfigId(java.lang.Long.valueOf(i));
            clusterConfig.setStack(currentStack);
            clusterConfig.setTag("version-" + i);
            clusterConfig.setData("{}");
            clusterConfig.setType(configType);
            clusterConfig.setTimestamp(java.lang.System.currentTimeMillis());
            clusterConfig.setVersion(java.lang.Long.valueOf(i));
            clusterConfig.setSelected(true);
            clusterConfig.setSelected(false);
            clusterDAO.createConfig(clusterConfig);
            clusterEntity.getClusterConfigEntities().add(clusterConfig);
            java.lang.Thread.sleep(5);
        }
        clusterEntity = clusterDAO.merge(clusterEntity);
        cluster.createServiceConfigVersion(serviceName, "", "version-1", null);
        org.apache.ambari.server.orm.entities.ClusterConfigEntity clusterConfigNewStack = new org.apache.ambari.server.orm.entities.ClusterConfigEntity();
        clusterConfigNewStack.setClusterEntity(clusterEntity);
        clusterConfigNewStack.setConfigId(6L);
        clusterConfigNewStack.setStack(newStack);
        clusterConfigNewStack.setTag("version-6");
        clusterConfigNewStack.setData("{}");
        clusterConfigNewStack.setType(configType);
        clusterConfigNewStack.setTimestamp(java.lang.System.currentTimeMillis());
        clusterConfigNewStack.setVersion(6L);
        clusterConfigNewStack.setSelected(true);
        clusterDAO.createConfig(clusterConfigNewStack);
        clusterEntity.getClusterConfigEntities().add(clusterConfigNewStack);
        clusterEntity = clusterDAO.merge(clusterEntity);
        org.apache.ambari.server.state.Config config = configFactory.createExisting(cluster, clusterConfigNewStack);
        cluster.addConfig(config);
        service.setDesiredRepositoryVersion(repoVersion220);
        cluster.createServiceConfigVersion(serviceName, "", "version-2", null);
        org.apache.ambari.server.orm.entities.ClusterConfigEntity clusterConfig = clusterDAO.findEnabledConfigByType(clusterEntity.getClusterId(), configType);
        junit.framework.Assert.assertTrue(clusterConfig.isSelected());
        junit.framework.Assert.assertEquals(clusterConfigNewStack.getTag(), clusterConfig.getTag());
        cluster.applyLatestConfigurations(stackId, serviceName);
        clusterEntity = clusterDAO.findByName("c1");
        clusterConfig = clusterDAO.findEnabledConfigByType(clusterEntity.getClusterId(), configType);
        junit.framework.Assert.assertTrue(clusterConfig.isSelected());
        junit.framework.Assert.assertEquals("version-5", clusterConfig.getTag());
    }

    @org.junit.Test
    public void testDesiredConfigurationsAfterApplyingLatestForStack() throws java.lang.Exception {
        org.apache.ambari.server.state.StackId stackId = new org.apache.ambari.server.state.StackId("HDP-2.0.6");
        org.apache.ambari.server.state.StackId newStackId = new org.apache.ambari.server.state.StackId("HDP-2.2.0");
        createDefaultCluster(com.google.common.collect.Sets.newHashSet("host-1"), stackId);
        org.apache.ambari.server.state.Cluster cluster = clusters.getCluster("c1");
        cluster.setCurrentStackVersion(stackId);
        cluster.setDesiredStackVersion(stackId);
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repoVersion220 = helper.getOrCreateRepositoryVersion(newStackId, "2.2.0-1234");
        org.apache.ambari.server.state.ConfigHelper configHelper = injector.getInstance(org.apache.ambari.server.state.ConfigHelper.class);
        junit.framework.Assert.assertFalse(stackId.equals(newStackId));
        java.lang.String serviceName = "ZOOKEEPER";
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion = helper.getOrCreateRepositoryVersion(c1);
        org.apache.ambari.server.state.Service service = cluster.addService(serviceName, repositoryVersion);
        java.lang.String configType = "zoo.cfg";
        java.util.Map<java.lang.String, java.lang.String> properties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> propertiesAttributes = new java.util.HashMap<>();
        properties.put("foo-property-1", "foo-value-1");
        org.apache.ambari.server.state.Config c1 = configFactory.createNew(stackId, cluster, configType, "version-1", properties, propertiesAttributes);
        cluster.addDesiredConfig("admin", com.google.common.collect.Sets.newHashSet(c1), "note-1");
        service.setDesiredRepositoryVersion(repoVersion220);
        cluster.setDesiredStackVersion(newStackId);
        properties.put("foo-property-2", "foo-value-2");
        org.apache.ambari.server.state.Config c2 = configFactory.createNew(newStackId, cluster, configType, "version-2", properties, propertiesAttributes);
        cluster.addDesiredConfig("admin", com.google.common.collect.Sets.newHashSet(c2), "note-2");
        java.util.Map<java.lang.String, org.apache.ambari.server.state.DesiredConfig> desiredConfigs = cluster.getDesiredConfigs();
        org.apache.ambari.server.state.DesiredConfig desiredConfig = desiredConfigs.get(configType);
        org.junit.Assert.assertNotNull(desiredConfig);
        org.junit.Assert.assertEquals(java.lang.Long.valueOf(2), desiredConfig.getVersion());
        org.junit.Assert.assertEquals("version-2", desiredConfig.getTag());
        java.lang.String hostName = cluster.getHosts().iterator().next().getHostName();
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> effectiveDesiredTags = configHelper.getEffectiveDesiredTags(cluster, hostName);
        org.junit.Assert.assertEquals("version-2", effectiveDesiredTags.get(configType).get("tag"));
        service.setDesiredRepositoryVersion(repositoryVersion);
        cluster.applyLatestConfigurations(stackId, serviceName);
        effectiveDesiredTags = configHelper.getEffectiveDesiredTags(cluster, hostName);
        org.junit.Assert.assertEquals("version-1", effectiveDesiredTags.get(configType).get("tag"));
        desiredConfigs = cluster.getDesiredConfigs();
        desiredConfig = desiredConfigs.get(configType);
        org.junit.Assert.assertNotNull(desiredConfig);
        org.junit.Assert.assertEquals(java.lang.Long.valueOf(1), desiredConfig.getVersion());
        org.junit.Assert.assertEquals("version-1", desiredConfig.getTag());
    }

    @org.junit.Test
    public void testRemoveConfigurations() throws java.lang.Exception {
        org.apache.ambari.server.state.StackId stackId = new org.apache.ambari.server.state.StackId("HDP-2.0.6");
        org.apache.ambari.server.state.StackId newStackId = new org.apache.ambari.server.state.StackId("HDP-2.2.0");
        createDefaultCluster(com.google.common.collect.Sets.newHashSet("host-1"), stackId);
        org.apache.ambari.server.state.Cluster cluster = clusters.getCluster("c1");
        org.apache.ambari.server.orm.entities.ClusterEntity clusterEntity = clusterDAO.findByName("c1");
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repoVersion220 = helper.getOrCreateRepositoryVersion(newStackId, "2.2.0-1234");
        org.apache.ambari.server.orm.entities.StackEntity currentStack = stackDAO.find(stackId.getStackName(), stackId.getStackVersion());
        org.apache.ambari.server.orm.entities.StackEntity newStack = stackDAO.find(newStackId.getStackName(), newStackId.getStackVersion());
        junit.framework.Assert.assertFalse(stackId.equals(newStackId));
        java.lang.String serviceName = "ZOOKEEPER";
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion = helper.getOrCreateRepositoryVersion(c1);
        org.apache.ambari.server.state.Service service = cluster.addService(serviceName, repositoryVersion);
        java.lang.String configType = "zoo.cfg";
        org.apache.ambari.server.orm.entities.ClusterConfigEntity clusterConfig = new org.apache.ambari.server.orm.entities.ClusterConfigEntity();
        clusterConfig.setClusterEntity(clusterEntity);
        clusterConfig.setConfigId(1L);
        clusterConfig.setStack(currentStack);
        clusterConfig.setTag("version-1");
        clusterConfig.setData("{}");
        clusterConfig.setType(configType);
        clusterConfig.setTimestamp(1L);
        clusterConfig.setVersion(1L);
        clusterConfig.setSelected(true);
        clusterDAO.createConfig(clusterConfig);
        clusterEntity.getClusterConfigEntities().add(clusterConfig);
        clusterEntity = clusterDAO.merge(clusterEntity);
        org.apache.ambari.server.state.Config config = configFactory.createExisting(cluster, clusterConfig);
        cluster.addConfig(config);
        cluster.createServiceConfigVersion(serviceName, "", "version-1", null);
        clusterConfig.setSelected(false);
        clusterConfig = clusterDAO.merge(clusterConfig);
        org.apache.ambari.server.orm.entities.ClusterConfigEntity newClusterConfig = new org.apache.ambari.server.orm.entities.ClusterConfigEntity();
        newClusterConfig.setClusterEntity(clusterEntity);
        newClusterConfig.setConfigId(2L);
        newClusterConfig.setStack(newStack);
        newClusterConfig.setTag("version-2");
        newClusterConfig.setData("{}");
        newClusterConfig.setType(configType);
        newClusterConfig.setTimestamp(2L);
        newClusterConfig.setVersion(2L);
        newClusterConfig.setSelected(true);
        clusterDAO.createConfig(newClusterConfig);
        clusterEntity.getClusterConfigEntities().add(newClusterConfig);
        clusterEntity = clusterDAO.merge(clusterEntity);
        config = configFactory.createExisting(cluster, newClusterConfig);
        cluster.addConfig(config);
        service.setDesiredRepositoryVersion(repoVersion220);
        cluster.createServiceConfigVersion(serviceName, "", "version-2", null);
        cluster.applyLatestConfigurations(newStackId, serviceName);
        java.util.List<org.apache.ambari.server.orm.entities.ClusterConfigEntity> clusterConfigs = clusterDAO.getAllConfigurations(cluster.getClusterId(), newStackId);
        junit.framework.Assert.assertEquals(1, clusterConfigs.size());
        cluster.removeConfigurations(newStackId, serviceName);
        clusterConfigs = clusterDAO.getAllConfigurations(cluster.getClusterId(), newStackId);
        junit.framework.Assert.assertEquals(0, clusterConfigs.size());
    }

    @org.junit.Test
    public void testCachedClusterProperties() throws java.lang.Exception {
        org.apache.ambari.server.utils.EventBusSynchronizer.synchronizeAmbariEventPublisher(injector);
        org.apache.ambari.server.events.publishers.AmbariEventPublisher publisher = injector.getInstance(org.apache.ambari.server.events.publishers.AmbariEventPublisher.class);
        createDefaultCluster();
        org.apache.ambari.server.state.Cluster cluster = clusters.getCluster("c1");
        org.junit.Assert.assertFalse(((org.apache.ambari.server.state.cluster.ClusterImpl) (cluster)).isClusterPropertyCached("foo"));
        java.lang.String property = cluster.getClusterProperty("foo", "bar");
        org.junit.Assert.assertEquals("bar", property);
        org.junit.Assert.assertTrue(((org.apache.ambari.server.state.cluster.ClusterImpl) (cluster)).isClusterPropertyCached("foo"));
        org.apache.ambari.server.events.ClusterConfigChangedEvent event = new org.apache.ambari.server.events.ClusterConfigChangedEvent(cluster.getClusterName(), org.apache.ambari.server.state.ConfigHelper.CLUSTER_ENV, null, 1L);
        publisher.publish(event);
        org.junit.Assert.assertFalse(((org.apache.ambari.server.state.cluster.ClusterImpl) (cluster)).isClusterPropertyCached("foo"));
    }
}