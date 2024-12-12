package org.apache.ambari.server.state.host;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
public class HostTest {
    private com.google.inject.Injector injector;

    private org.apache.ambari.server.state.Clusters clusters;

    private org.apache.ambari.server.orm.dao.HostDAO hostDAO;

    private org.apache.ambari.server.orm.OrmTestHelper helper;

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.state.host.HostTest.class);

    @org.junit.Before
    public void setup() throws org.apache.ambari.server.AmbariException {
        injector = com.google.inject.Guice.createInjector(new org.apache.ambari.server.orm.InMemoryDefaultTestModule());
        injector.getInstance(org.apache.ambari.server.orm.GuiceJpaInitializer.class);
        clusters = injector.getInstance(org.apache.ambari.server.state.Clusters.class);
        hostDAO = injector.getInstance(org.apache.ambari.server.orm.dao.HostDAO.class);
        helper = injector.getInstance(org.apache.ambari.server.orm.OrmTestHelper.class);
    }

    @org.junit.After
    public void teardown() throws org.apache.ambari.server.AmbariException, java.sql.SQLException {
        org.apache.ambari.server.H2DatabaseCleaner.clearDatabaseAndStopPersistenceService(injector);
    }

    @org.junit.Test
    public void testHostInfoImport() throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.agent.HostInfo info = new org.apache.ambari.server.agent.HostInfo();
        info.setMemorySize(100);
        info.setProcessorCount(10);
        info.setPhysicalProcessorCount(2);
        java.util.List<org.apache.ambari.server.agent.DiskInfo> mounts = new java.util.ArrayList<>();
        mounts.add(new org.apache.ambari.server.agent.DiskInfo("/dev/sda", "/mnt/disk1", "5000000", "4000000", "10%", "size", "fstype"));
        info.setMounts(mounts);
        info.setHostName("foo");
        info.setInterfaces("fip_4");
        info.setArchitecture("os_arch");
        info.setOS("os_type");
        info.setMemoryTotal(10);
        clusters.addHost("foo");
        org.apache.ambari.server.state.Host host = clusters.getHost("foo");
        host.importHostInfo(info);
        org.junit.Assert.assertEquals(info.getHostName(), host.getHostName());
        org.junit.Assert.assertEquals(info.getFreeMemory(), host.getAvailableMemBytes());
        org.junit.Assert.assertEquals(info.getMemoryTotal(), host.getTotalMemBytes());
        org.junit.Assert.assertEquals(info.getProcessorCount(), host.getCpuCount());
        org.junit.Assert.assertEquals(info.getPhysicalProcessorCount(), host.getPhCpuCount());
        org.junit.Assert.assertEquals(info.getMounts().size(), host.getDisksInfo().size());
        org.junit.Assert.assertEquals(info.getArchitecture(), host.getOsArch());
        org.junit.Assert.assertEquals(info.getOS(), host.getOsType());
    }

    private void registerHost(org.apache.ambari.server.state.Host host) throws java.lang.Exception {
        registerHost(host, true);
    }

    @org.junit.Test
    public void testHostOs() throws java.lang.Exception {
        org.apache.ambari.server.state.Clusters clusters = Mockito.mock(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.actionmanager.ActionManager manager = Mockito.mock(org.apache.ambari.server.actionmanager.ActionManager.class);
        com.google.inject.Injector injector = Mockito.mock(com.google.inject.Injector.class);
        org.apache.ambari.server.configuration.Configuration configuration = Mockito.mock(org.apache.ambari.server.configuration.Configuration.class);
        Mockito.doNothing().when(injector).injectMembers(Matchers.any());
        Mockito.when(injector.getInstance(org.apache.ambari.server.events.publishers.AmbariEventPublisher.class)).thenReturn(Mockito.mock(org.apache.ambari.server.events.publishers.AmbariEventPublisher.class));
        org.apache.ambari.server.agent.HeartBeatHandler handler = new org.apache.ambari.server.agent.HeartBeatHandler(configuration, clusters, manager, org.apache.ambari.server.security.encryption.Encryptor.NONE, injector);
        java.lang.String os = handler.getOsType("RedHat", "6.1");
        org.junit.Assert.assertEquals("redhat6", os);
        os = handler.getOsType("RedHat", "6");
        org.junit.Assert.assertEquals("redhat6", os);
        os = handler.getOsType("RedHat6", "");
        org.junit.Assert.assertEquals("redhat6", os);
    }

    private void registerHost(org.apache.ambari.server.state.Host host, boolean firstReg) throws java.lang.Exception {
        org.apache.ambari.server.agent.HostInfo info = new org.apache.ambari.server.agent.HostInfo();
        info.setMemorySize(100);
        info.setProcessorCount(10);
        java.util.List<org.apache.ambari.server.agent.DiskInfo> mounts = new java.util.ArrayList<>();
        mounts.add(new org.apache.ambari.server.agent.DiskInfo("/dev/sda", "/mnt/disk1", "5000000", "4000000", "10%", "size", "fstype"));
        info.setMounts(mounts);
        info.setHostName("foo");
        info.setInterfaces("fip_4");
        info.setArchitecture("os_arch");
        info.setOS("os_type");
        info.setMemoryTotal(10);
        org.apache.ambari.server.state.AgentVersion agentVersion = null;
        long currentTime = java.lang.System.currentTimeMillis();
        org.apache.ambari.server.agent.AgentEnv agentEnv = new org.apache.ambari.server.agent.AgentEnv();
        org.apache.ambari.server.state.host.HostRegistrationRequestEvent e = new org.apache.ambari.server.state.host.HostRegistrationRequestEvent("foo", agentVersion, currentTime, info, agentEnv, currentTime);
        if (!firstReg) {
            org.junit.Assert.assertNotNull(host.getHostId());
        }
        host.handleEvent(e);
        org.junit.Assert.assertEquals(currentTime, host.getLastRegistrationTime());
        org.junit.Assert.assertNotNull(host.getLastAgentEnv());
        org.apache.ambari.server.orm.entities.HostEntity entity = hostDAO.findByName(host.getHostName());
        org.junit.Assert.assertEquals(currentTime, entity.getLastRegistrationTime().longValue());
        org.junit.Assert.assertEquals("os_arch", entity.getOsArch());
        org.junit.Assert.assertEquals("os_type", entity.getOsType());
        org.junit.Assert.assertEquals(10, entity.getTotalMem().longValue());
    }

    private void ensureHostUpdatesReceived(org.apache.ambari.server.state.Host host) throws java.lang.Exception {
        org.apache.ambari.server.state.host.HostStatusUpdatesReceivedEvent e = new org.apache.ambari.server.state.host.HostStatusUpdatesReceivedEvent(host.getHostName(), 1);
        host.handleEvent(e);
    }

    private void verifyHostState(org.apache.ambari.server.state.Host host, org.apache.ambari.server.state.HostState state) {
        org.junit.Assert.assertEquals(state, host.getState());
    }

    private void sendHealthyHeartbeat(org.apache.ambari.server.state.Host host, long counter) throws java.lang.Exception {
        org.apache.ambari.server.state.host.HostHealthyHeartbeatEvent e = new org.apache.ambari.server.state.host.HostHealthyHeartbeatEvent(host.getHostName(), counter, null, null);
        host.handleEvent(e);
    }

    private void sendUnhealthyHeartbeat(org.apache.ambari.server.state.Host host, long counter) throws java.lang.Exception {
        org.apache.ambari.server.state.HostHealthStatus healthStatus = new org.apache.ambari.server.state.HostHealthStatus(org.apache.ambari.server.state.HostHealthStatus.HealthStatus.UNHEALTHY, "Unhealthy server");
        org.apache.ambari.server.state.host.HostUnhealthyHeartbeatEvent e = new org.apache.ambari.server.state.host.HostUnhealthyHeartbeatEvent(host.getHostName(), counter, healthStatus);
        host.handleEvent(e);
    }

    private void timeoutHost(org.apache.ambari.server.state.Host host) throws java.lang.Exception {
        org.apache.ambari.server.state.host.HostHeartbeatLostEvent e = new org.apache.ambari.server.state.host.HostHeartbeatLostEvent(host.getHostName());
        host.handleEvent(e);
    }

    @org.junit.Test
    public void testHostFSMInit() throws org.apache.ambari.server.AmbariException {
        clusters.addHost("foo");
        org.apache.ambari.server.state.Host host = clusters.getHost("foo");
        verifyHostState(host, org.apache.ambari.server.state.HostState.INIT);
    }

    @org.junit.Test
    public void testHostRegistrationFlow() throws java.lang.Exception {
        clusters.addHost("foo");
        org.apache.ambari.server.state.Host host = clusters.getHost("foo");
        registerHost(host);
        verifyHostState(host, org.apache.ambari.server.state.HostState.WAITING_FOR_HOST_STATUS_UPDATES);
        boolean exceptionThrown = false;
        try {
            registerHost(host);
        } catch (java.lang.Exception e) {
            exceptionThrown = true;
        }
        if (!exceptionThrown) {
            org.junit.Assert.fail("Expected invalid transition exception to be thrown");
        }
        ensureHostUpdatesReceived(host);
        verifyHostState(host, org.apache.ambari.server.state.HostState.HEALTHY);
        exceptionThrown = false;
        try {
            ensureHostUpdatesReceived(host);
        } catch (java.lang.Exception e) {
            exceptionThrown = true;
        }
        if (!exceptionThrown) {
            org.junit.Assert.fail("Expected invalid transition exception to be thrown");
        }
    }

    @org.junit.Test
    public void testHostHeartbeatFlow() throws java.lang.Exception {
        clusters.addHost("foo");
        org.apache.ambari.server.state.Host host = clusters.getHost("foo");
        registerHost(host);
        ensureHostUpdatesReceived(host);
        long counter = 0;
        sendHealthyHeartbeat(host, ++counter);
        verifyHostState(host, org.apache.ambari.server.state.HostState.HEALTHY);
        org.junit.Assert.assertEquals(counter, host.getLastHeartbeatTime());
        sendHealthyHeartbeat(host, ++counter);
        verifyHostState(host, org.apache.ambari.server.state.HostState.HEALTHY);
        org.junit.Assert.assertEquals(counter, host.getLastHeartbeatTime());
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.HostHealthStatus.HealthStatus.HEALTHY, host.getHealthStatus().getHealthStatus());
        sendUnhealthyHeartbeat(host, ++counter);
        verifyHostState(host, org.apache.ambari.server.state.HostState.UNHEALTHY);
        org.junit.Assert.assertEquals(counter, host.getLastHeartbeatTime());
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.HostHealthStatus.HealthStatus.UNHEALTHY, host.getHealthStatus().getHealthStatus());
        sendUnhealthyHeartbeat(host, ++counter);
        verifyHostState(host, org.apache.ambari.server.state.HostState.UNHEALTHY);
        org.junit.Assert.assertEquals(counter, host.getLastHeartbeatTime());
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.HostHealthStatus.HealthStatus.UNHEALTHY, host.getHealthStatus().getHealthStatus());
        sendHealthyHeartbeat(host, ++counter);
        verifyHostState(host, org.apache.ambari.server.state.HostState.HEALTHY);
        org.junit.Assert.assertEquals(counter, host.getLastHeartbeatTime());
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.HostHealthStatus.HealthStatus.HEALTHY, host.getHealthStatus().getHealthStatus());
        timeoutHost(host);
        verifyHostState(host, org.apache.ambari.server.state.HostState.HEARTBEAT_LOST);
        org.junit.Assert.assertEquals(counter, host.getLastHeartbeatTime());
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.HostHealthStatus.HealthStatus.UNKNOWN, host.getHealthStatus().getHealthStatus());
        timeoutHost(host);
        verifyHostState(host, org.apache.ambari.server.state.HostState.HEARTBEAT_LOST);
        org.junit.Assert.assertEquals(counter, host.getLastHeartbeatTime());
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.HostHealthStatus.HealthStatus.UNKNOWN, host.getHealthStatus().getHealthStatus());
        try {
            sendUnhealthyHeartbeat(host, ++counter);
            org.junit.Assert.fail("Invalid event should have triggered an exception");
        } catch (java.lang.Exception e) {
        }
        verifyHostState(host, org.apache.ambari.server.state.HostState.HEARTBEAT_LOST);
        try {
            sendHealthyHeartbeat(host, ++counter);
            org.junit.Assert.fail("Invalid event should have triggered an exception");
        } catch (java.lang.Exception e) {
        }
        verifyHostState(host, org.apache.ambari.server.state.HostState.HEARTBEAT_LOST);
    }

    @org.junit.Test
    public void testHostRegistrationsInAnyState() throws java.lang.Exception {
        clusters.addHost("foo");
        org.apache.ambari.server.state.Host host = clusters.getHost("foo");
        host.setIPv4("ipv4");
        host.setIPv6("ipv6");
        long counter = 0;
        registerHost(host);
        ensureHostUpdatesReceived(host);
        registerHost(host, false);
        ensureHostUpdatesReceived(host);
        sendHealthyHeartbeat(host, ++counter);
        verifyHostState(host, org.apache.ambari.server.state.HostState.HEALTHY);
        registerHost(host, false);
        ensureHostUpdatesReceived(host);
        sendUnhealthyHeartbeat(host, ++counter);
        verifyHostState(host, org.apache.ambari.server.state.HostState.UNHEALTHY);
        registerHost(host, false);
        ensureHostUpdatesReceived(host);
        timeoutHost(host);
        verifyHostState(host, org.apache.ambari.server.state.HostState.HEARTBEAT_LOST);
        registerHost(host, false);
        ensureHostUpdatesReceived(host);
        host.setState(org.apache.ambari.server.state.HostState.INIT);
        registerHost(host, false);
    }

    @org.junit.Test
    public void testHostDesiredConfig() throws java.lang.Exception {
        org.apache.ambari.server.api.services.AmbariMetaInfo metaInfo = injector.getInstance(org.apache.ambari.server.api.services.AmbariMetaInfo.class);
        org.apache.ambari.server.state.StackId stackId = new org.apache.ambari.server.state.StackId("HDP-0.1");
        clusters.addCluster("c1", stackId);
        org.apache.ambari.server.state.Cluster c1 = clusters.getCluster("c1");
        helper.getOrCreateRepositoryVersion(stackId, stackId.getStackVersion());
        org.junit.Assert.assertEquals("c1", c1.getClusterName());
        clusters.addHost("h1");
        org.apache.ambari.server.state.Host host = clusters.getHost("h1");
        host.setIPv4("ipv4");
        host.setIPv6("ipv6");
        java.util.Map<java.lang.String, java.lang.String> hostAttributes = new java.util.HashMap<>();
        hostAttributes.put("os_family", "redhat");
        hostAttributes.put("os_release_version", "6.3");
        host.setHostAttributes(hostAttributes);
        c1.setDesiredStackVersion(stackId);
        clusters.mapHostToCluster("h1", "c1");
        org.apache.ambari.server.state.ConfigFactory configFactory = injector.getInstance(org.apache.ambari.server.state.ConfigFactory.class);
        org.apache.ambari.server.state.Config config = configFactory.createNew(c1, "global", "v1", new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put("a", "b");
                put("x", "y");
            }
        }, new java.util.HashMap<>());
        try {
            host.addDesiredConfig(c1.getClusterId(), true, null, config);
            org.junit.Assert.fail("Expect failure when user is not specified.");
        } catch (java.lang.Exception e) {
        }
        host.addDesiredConfig(c1.getClusterId(), true, "_test", config);
        java.util.Map<java.lang.String, org.apache.ambari.server.state.DesiredConfig> map = host.getDesiredConfigs(c1.getClusterId());
        org.junit.Assert.assertTrue("Expect desired config to contain global", map.containsKey("global"));
        config = configFactory.createNew(c1, "global", "v2", new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put("c", "d");
            }
        }, new java.util.HashMap<>());
        host.addDesiredConfig(c1.getClusterId(), true, "_test1", config);
        map = host.getDesiredConfigs(c1.getClusterId());
        org.junit.Assert.assertTrue("Expect desired config to contain global", map.containsKey("global"));
        org.junit.Assert.assertEquals("Expect version to be 'v2'", "v2", map.get("global").getTag());
        host.addDesiredConfig(c1.getClusterId(), false, "_test2", config);
        map = host.getDesiredConfigs(c1.getClusterId());
        org.junit.Assert.assertEquals("Expect no mapping configs", 0, map.size());
    }

    @org.junit.Test
    public void testHostMaintenance() throws java.lang.Exception {
        org.apache.ambari.server.api.services.AmbariMetaInfo metaInfo = injector.getInstance(org.apache.ambari.server.api.services.AmbariMetaInfo.class);
        org.apache.ambari.server.state.StackId stackId = new org.apache.ambari.server.state.StackId("HDP-0.1");
        clusters.addCluster("c1", stackId);
        org.apache.ambari.server.state.Cluster c1 = clusters.getCluster("c1");
        org.junit.Assert.assertEquals("c1", c1.getClusterName());
        clusters.addHost("h1");
        org.apache.ambari.server.state.Host host = clusters.getHost("h1");
        host.setIPv4("ipv4");
        host.setIPv6("ipv6");
        java.util.Map<java.lang.String, java.lang.String> hostAttributes = new java.util.HashMap<>();
        hostAttributes.put("os_family", "redhat");
        hostAttributes.put("os_release_version", "6.3");
        host.setHostAttributes(hostAttributes);
        helper.getOrCreateRepositoryVersion(stackId, stackId.getStackVersion());
        c1.setDesiredStackVersion(stackId);
        clusters.mapHostToCluster("h1", "c1");
        org.apache.ambari.server.orm.entities.HostEntity entity = hostDAO.findByName("h1");
        org.apache.ambari.server.orm.entities.HostStateEntity stateEntity = entity.getHostStateEntity();
        org.junit.Assert.assertNull(stateEntity.getMaintenanceState());
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.MaintenanceState.OFF, host.getMaintenanceState(c1.getClusterId()));
        host.setMaintenanceState(c1.getClusterId(), org.apache.ambari.server.state.MaintenanceState.ON);
        entity = hostDAO.findByName("h1");
        stateEntity = entity.getHostStateEntity();
        org.junit.Assert.assertNotNull(stateEntity.getMaintenanceState());
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.MaintenanceState.ON, host.getMaintenanceState(c1.getClusterId()));
    }

    @org.junit.Test
    public void testHostPersist() throws java.lang.Exception {
        clusters.addHost("foo");
        org.apache.ambari.server.state.Host host = clusters.getHost("foo");
        java.lang.String rackInfo = "rackInfo";
        long lastRegistrationTime = java.lang.System.currentTimeMillis();
        host.setRackInfo(rackInfo);
        host.setLastRegistrationTime(lastRegistrationTime);
        org.junit.Assert.assertEquals(rackInfo, host.getRackInfo());
        org.junit.Assert.assertEquals(lastRegistrationTime, host.getLastRegistrationTime());
    }
}