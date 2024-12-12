package org.apache.ambari.server.agent;
import org.mockito.ArgumentCaptor;
import static org.apache.ambari.server.agent.ExecutionCommand.KeyNames.HOOKS_FOLDER;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
public class TestHeartbeatMonitor {
    private static com.google.inject.Injector injector;

    private java.lang.String hostname1 = "host1";

    private java.lang.String hostname2 = "host2";

    private java.lang.String clusterName = "cluster1";

    private java.lang.String serviceName = "HDFS";

    private int heartbeatMonitorWakeupIntervalMS = 30;

    private static org.apache.ambari.server.api.services.AmbariMetaInfo ambariMetaInfo;

    private static org.apache.ambari.server.orm.OrmTestHelper helper;

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.agent.TestHeartbeatMonitor.class);

    @org.junit.Before
    public void setup() throws java.lang.Exception {
        org.apache.ambari.server.agent.TestHeartbeatMonitor.injector = com.google.inject.Guice.createInjector(new org.apache.ambari.server.orm.InMemoryDefaultTestModule());
        org.apache.ambari.server.agent.TestHeartbeatMonitor.injector.getInstance(org.apache.ambari.server.orm.GuiceJpaInitializer.class);
        org.apache.ambari.server.agent.TestHeartbeatMonitor.helper = org.apache.ambari.server.agent.TestHeartbeatMonitor.injector.getInstance(org.apache.ambari.server.orm.OrmTestHelper.class);
        org.apache.ambari.server.agent.TestHeartbeatMonitor.ambariMetaInfo = org.apache.ambari.server.agent.TestHeartbeatMonitor.injector.getInstance(org.apache.ambari.server.api.services.AmbariMetaInfo.class);
        org.apache.ambari.server.utils.StageUtils.setTopologyManager(org.apache.ambari.server.agent.TestHeartbeatMonitor.injector.getInstance(org.apache.ambari.server.topology.TopologyManager.class));
        org.apache.ambari.server.utils.StageUtils.setConfiguration(org.apache.ambari.server.agent.TestHeartbeatMonitor.injector.getInstance(org.apache.ambari.server.configuration.Configuration.class));
    }

    @org.junit.After
    public void teardown() throws org.apache.ambari.server.AmbariException, java.sql.SQLException {
        org.apache.ambari.server.H2DatabaseCleaner.clearDatabaseAndStopPersistenceService(org.apache.ambari.server.agent.TestHeartbeatMonitor.injector);
    }

    private void setOsFamily(org.apache.ambari.server.state.Host host, java.lang.String osFamily, java.lang.String osVersion) {
        java.util.Map<java.lang.String, java.lang.String> hostAttributes = new java.util.HashMap<>();
        hostAttributes.put("os_family", osFamily);
        hostAttributes.put("os_release_version", osVersion);
        host.setHostAttributes(hostAttributes);
    }

    @org.junit.Test
    @org.junit.Ignore
    public void testHeartbeatLoss() throws org.apache.ambari.server.AmbariException, java.lang.InterruptedException, org.apache.ambari.server.state.fsm.InvalidStateTransitionException {
        org.apache.ambari.server.state.Clusters fsm = org.apache.ambari.server.agent.TestHeartbeatMonitor.injector.getInstance(org.apache.ambari.server.state.Clusters.class);
        java.lang.String hostname = "host1";
        fsm.addHost(hostname);
        org.apache.ambari.server.actionmanager.ActionManager am = Mockito.mock(org.apache.ambari.server.actionmanager.ActionManager.class);
        org.apache.ambari.server.configuration.Configuration configuration = Mockito.mock(org.apache.ambari.server.configuration.Configuration.class);
        Mockito.when(configuration.getHeartbeatMonitorInterval()).thenReturn(10);
        org.apache.ambari.server.agent.HeartbeatMonitor hm = new org.apache.ambari.server.agent.HeartbeatMonitor(fsm, am, 10, org.apache.ambari.server.agent.TestHeartbeatMonitor.injector);
        org.apache.ambari.server.agent.HeartBeatHandler handler = new org.apache.ambari.server.agent.HeartBeatHandler(configuration, fsm, am, org.apache.ambari.server.security.encryption.Encryptor.NONE, org.apache.ambari.server.agent.TestHeartbeatMonitor.injector);
        org.apache.ambari.server.agent.Register reg = new org.apache.ambari.server.agent.Register();
        reg.setHostname(hostname);
        reg.setResponseId(12);
        reg.setTimestamp(java.lang.System.currentTimeMillis() - 300);
        reg.setAgentVersion(org.apache.ambari.server.agent.TestHeartbeatMonitor.ambariMetaInfo.getServerVersion());
        org.apache.ambari.server.agent.HostInfo hi = new org.apache.ambari.server.agent.HostInfo();
        hi.setOS("Centos5");
        reg.setHardwareProfile(hi);
        handler.handleRegistration(reg);
        org.apache.ambari.server.agent.HeartBeat hb = new org.apache.ambari.server.agent.HeartBeat();
        hb.setHostname(hostname);
        hb.setNodeStatus(new org.apache.ambari.server.agent.HostStatus(org.apache.ambari.server.agent.HostStatus.Status.HEALTHY, "cool"));
        hb.setTimestamp(java.lang.System.currentTimeMillis());
        hb.setResponseId(12);
        handler.handleHeartBeat(hb);
        hm.start();
        org.junit.Assert.assertEquals(fsm.getHost(hostname).getState(), org.apache.ambari.server.state.HostState.HEARTBEAT_LOST);
        hm.shutdown();
    }

    @org.junit.Test
    public void testStateCommandsGeneration() throws org.apache.ambari.server.AmbariException, java.lang.InterruptedException, org.apache.ambari.server.state.fsm.InvalidStateTransitionException {
        org.apache.ambari.server.state.StackId stackId = new org.apache.ambari.server.state.StackId("HDP-0.1");
        org.apache.ambari.server.state.Clusters clusters = org.apache.ambari.server.agent.TestHeartbeatMonitor.injector.getInstance(org.apache.ambari.server.state.Clusters.class);
        clusters.addHost(hostname1);
        setOsFamily(clusters.getHost(hostname1), "redhat", "6.3");
        clusters.addHost(hostname2);
        setOsFamily(clusters.getHost(hostname2), "redhat", "6.3");
        clusters.addCluster(clusterName, stackId);
        org.apache.ambari.server.state.Cluster cluster = clusters.getCluster(clusterName);
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion = org.apache.ambari.server.agent.TestHeartbeatMonitor.helper.getOrCreateRepositoryVersion(stackId, stackId.getStackVersion());
        java.util.Set<java.lang.String> hostNames = new java.util.HashSet<java.lang.String>() {
            {
                add(hostname1);
                add(hostname2);
            }
        };
        org.apache.ambari.server.state.ConfigFactory configFactory = org.apache.ambari.server.agent.TestHeartbeatMonitor.injector.getInstance(org.apache.ambari.server.state.ConfigFactory.class);
        org.apache.ambari.server.state.Config config = configFactory.createNew(cluster, "hadoop-env", "version1", new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put("a", "b");
            }
        }, new java.util.HashMap<>());
        cluster.addDesiredConfig("_test", java.util.Collections.singleton(config));
        clusters.mapAndPublishHostsToCluster(hostNames, clusterName);
        org.apache.ambari.server.state.Service hdfs = cluster.addService(serviceName, repositoryVersion);
        hdfs.addServiceComponent(org.apache.ambari.server.Role.DATANODE.name());
        hdfs.getServiceComponent(org.apache.ambari.server.Role.DATANODE.name()).addServiceComponentHost(hostname1);
        hdfs.addServiceComponent(org.apache.ambari.server.Role.NAMENODE.name());
        hdfs.getServiceComponent(org.apache.ambari.server.Role.NAMENODE.name()).addServiceComponentHost(hostname1);
        hdfs.addServiceComponent(org.apache.ambari.server.Role.SECONDARY_NAMENODE.name());
        hdfs.getServiceComponent(org.apache.ambari.server.Role.SECONDARY_NAMENODE.name()).addServiceComponentHost(hostname1);
        hdfs.getServiceComponent(org.apache.ambari.server.Role.DATANODE.name()).getServiceComponentHost(hostname1).setState(org.apache.ambari.server.state.State.INSTALLED);
        hdfs.getServiceComponent(org.apache.ambari.server.Role.NAMENODE.name()).getServiceComponentHost(hostname1).setState(org.apache.ambari.server.state.State.INSTALLED);
        hdfs.getServiceComponent(org.apache.ambari.server.Role.SECONDARY_NAMENODE.name()).getServiceComponentHost(hostname1).setState(org.apache.ambari.server.state.State.INSTALLED);
        org.apache.ambari.server.actionmanager.ActionManager am = Mockito.mock(org.apache.ambari.server.actionmanager.ActionManager.class);
        org.apache.ambari.server.agent.HeartbeatMonitor hm = new org.apache.ambari.server.agent.HeartbeatMonitor(clusters, am, heartbeatMonitorWakeupIntervalMS, org.apache.ambari.server.agent.TestHeartbeatMonitor.injector);
        org.apache.ambari.server.configuration.Configuration configuration = Mockito.mock(org.apache.ambari.server.configuration.Configuration.class);
        Mockito.when(configuration.getHeartbeatMonitorInterval()).thenReturn(60000);
        org.apache.ambari.server.agent.HeartBeatHandler handler = new org.apache.ambari.server.agent.HeartBeatHandler(configuration, clusters, am, org.apache.ambari.server.security.encryption.Encryptor.NONE, org.apache.ambari.server.agent.TestHeartbeatMonitor.injector);
        org.apache.ambari.server.agent.Register reg = new org.apache.ambari.server.agent.Register();
        reg.setHostname(hostname1);
        reg.setResponseId(12);
        reg.setTimestamp(java.lang.System.currentTimeMillis() - 300);
        reg.setAgentVersion(org.apache.ambari.server.agent.TestHeartbeatMonitor.ambariMetaInfo.getServerVersion());
        org.apache.ambari.server.agent.HostInfo hi = new org.apache.ambari.server.agent.HostInfo();
        hi.setOS("Centos5");
        reg.setHardwareProfile(hi);
        handler.handleRegistration(reg);
        org.apache.ambari.server.agent.HeartBeat hb = new org.apache.ambari.server.agent.HeartBeat();
        hb.setHostname(hostname1);
        hb.setNodeStatus(new org.apache.ambari.server.agent.HostStatus(org.apache.ambari.server.agent.HostStatus.Status.HEALTHY, "cool"));
        hb.setTimestamp(java.lang.System.currentTimeMillis());
        hb.setResponseId(12);
        handler.handleHeartBeat(hb);
        hm.getAgentRequests().setExecutionDetailsRequest(hostname1, "DATANODE", java.lang.Boolean.TRUE.toString());
        java.util.List<org.apache.ambari.server.agent.StatusCommand> cmds = hm.generateStatusCommands(hostname1);
        org.junit.Assert.assertTrue("HeartbeatMonitor should generate StatusCommands for host1", cmds.size() == 3);
        org.junit.Assert.assertEquals("HDFS", cmds.get(0).getServiceName());
        boolean containsDATANODEStatus = false;
        boolean containsNAMENODEStatus = false;
        boolean containsSECONDARY_NAMENODEStatus = false;
        for (org.apache.ambari.server.agent.StatusCommand cmd : cmds) {
            boolean isDataNode = cmd.getComponentName().equals("DATANODE");
            containsDATANODEStatus |= isDataNode;
            containsNAMENODEStatus |= cmd.getComponentName().equals("NAMENODE");
            containsSECONDARY_NAMENODEStatus |= cmd.getComponentName().equals("SECONDARY_NAMENODE");
            org.junit.Assert.assertTrue(cmd.getConfigurations().size() > 0);
            org.apache.ambari.server.agent.ExecutionCommand execCmd = cmd.getExecutionCommand();
            org.junit.Assert.assertEquals(isDataNode, execCmd != null);
            if (execCmd != null) {
                java.util.Map<java.lang.String, java.lang.String> commandParams = execCmd.getCommandParams();
                org.junit.Assert.assertTrue(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.HOOKS_FOLDER + " should be included", commandParams.containsKey(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.HOOKS_FOLDER));
            }
        }
        org.junit.Assert.assertEquals(true, containsDATANODEStatus);
        org.junit.Assert.assertEquals(true, containsNAMENODEStatus);
        org.junit.Assert.assertEquals(true, containsSECONDARY_NAMENODEStatus);
        cmds = hm.generateStatusCommands(hostname2);
        org.junit.Assert.assertTrue("HeartbeatMonitor should not generate StatusCommands for host2 because it has no services", cmds.isEmpty());
    }

    @org.junit.Test
    @org.junit.Ignore
    public void testStatusCommandForAnyComponents() throws java.lang.Exception {
        org.apache.ambari.server.state.StackId stackId = new org.apache.ambari.server.state.StackId("HDP-0.1");
        org.apache.ambari.server.state.Clusters clusters = org.apache.ambari.server.agent.TestHeartbeatMonitor.injector.getInstance(org.apache.ambari.server.state.Clusters.class);
        clusters.addHost(hostname1);
        setOsFamily(clusters.getHost(hostname1), "redhat", "6.3");
        clusters.addHost(hostname2);
        setOsFamily(clusters.getHost(hostname2), "redhat", "6.3");
        clusters.addCluster(clusterName, stackId);
        org.apache.ambari.server.state.Cluster cluster = clusters.getCluster(clusterName);
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion = org.apache.ambari.server.agent.TestHeartbeatMonitor.helper.getOrCreateRepositoryVersion(stackId, stackId.getStackVersion());
        java.util.Set<java.lang.String> hostNames = new java.util.HashSet<java.lang.String>() {
            {
                add(hostname1);
                add(hostname2);
            }
        };
        org.apache.ambari.server.state.ConfigFactory configFactory = org.apache.ambari.server.agent.TestHeartbeatMonitor.injector.getInstance(org.apache.ambari.server.state.ConfigFactory.class);
        org.apache.ambari.server.state.Config hadoopEnvConfig = configFactory.createNew(cluster, "hadoop-env", "version1", new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put("a", "b");
            }
        }, new java.util.HashMap<>());
        org.apache.ambari.server.state.Config hbaseEnvConfig = configFactory.createNew(cluster, "hbase-env", "version1", new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put("a", "b");
            }
        }, new java.util.HashMap<>());
        cluster.addDesiredConfig("_test", java.util.Collections.singleton(hadoopEnvConfig));
        clusters.mapAndPublishHostsToCluster(hostNames, clusterName);
        org.apache.ambari.server.state.Service hdfs = cluster.addService(serviceName, repositoryVersion);
        hdfs.addServiceComponent(org.apache.ambari.server.Role.DATANODE.name());
        hdfs.getServiceComponent(org.apache.ambari.server.Role.DATANODE.name()).addServiceComponentHost(hostname1);
        hdfs.addServiceComponent(org.apache.ambari.server.Role.NAMENODE.name());
        hdfs.getServiceComponent(org.apache.ambari.server.Role.NAMENODE.name()).addServiceComponentHost(hostname1);
        hdfs.addServiceComponent(org.apache.ambari.server.Role.SECONDARY_NAMENODE.name());
        hdfs.getServiceComponent(org.apache.ambari.server.Role.SECONDARY_NAMENODE.name()).addServiceComponentHost(hostname1);
        hdfs.addServiceComponent(org.apache.ambari.server.Role.HDFS_CLIENT.name());
        hdfs.getServiceComponent(org.apache.ambari.server.Role.HDFS_CLIENT.name()).addServiceComponentHost(hostname1);
        hdfs.getServiceComponent(org.apache.ambari.server.Role.HDFS_CLIENT.name()).addServiceComponentHost(hostname2);
        hdfs.getServiceComponent(org.apache.ambari.server.Role.DATANODE.name()).getServiceComponentHost(hostname1).setState(org.apache.ambari.server.state.State.INSTALLED);
        hdfs.getServiceComponent(org.apache.ambari.server.Role.NAMENODE.name()).getServiceComponentHost(hostname1).setState(org.apache.ambari.server.state.State.INSTALLED);
        hdfs.getServiceComponent(org.apache.ambari.server.Role.SECONDARY_NAMENODE.name()).getServiceComponentHost(hostname1).setState(org.apache.ambari.server.state.State.INSTALLED);
        hdfs.getServiceComponent(org.apache.ambari.server.Role.HDFS_CLIENT.name()).getServiceComponentHost(hostname1).setState(org.apache.ambari.server.state.State.INSTALLED);
        hdfs.getServiceComponent(org.apache.ambari.server.Role.HDFS_CLIENT.name()).getServiceComponentHost(hostname2).setState(org.apache.ambari.server.state.State.INSTALLED);
        hdfs.getServiceComponent(org.apache.ambari.server.Role.DATANODE.name()).getServiceComponentHost(hostname1).setDesiredState(org.apache.ambari.server.state.State.INSTALLED);
        hdfs.getServiceComponent(org.apache.ambari.server.Role.NAMENODE.name()).getServiceComponentHost(hostname1).setDesiredState(org.apache.ambari.server.state.State.INSTALLED);
        hdfs.getServiceComponent(org.apache.ambari.server.Role.SECONDARY_NAMENODE.name()).getServiceComponentHost(hostname1).setDesiredState(org.apache.ambari.server.state.State.INSTALLED);
        hdfs.getServiceComponent(org.apache.ambari.server.Role.HDFS_CLIENT.name()).getServiceComponentHost(hostname1).setDesiredState(org.apache.ambari.server.state.State.INSTALLED);
        hdfs.getServiceComponent(org.apache.ambari.server.Role.HDFS_CLIENT.name()).getServiceComponentHost(hostname2).setDesiredState(org.apache.ambari.server.state.State.INSTALLED);
        org.apache.ambari.server.actionmanager.ActionManager am = Mockito.mock(org.apache.ambari.server.actionmanager.ActionManager.class);
        org.apache.ambari.server.agent.HeartbeatMonitor hm = new org.apache.ambari.server.agent.HeartbeatMonitor(clusters, am, heartbeatMonitorWakeupIntervalMS, org.apache.ambari.server.agent.TestHeartbeatMonitor.injector);
        org.apache.ambari.server.configuration.Configuration configuration = Mockito.mock(org.apache.ambari.server.configuration.Configuration.class);
        Mockito.when(configuration.getHeartbeatMonitorInterval()).thenReturn(60000);
        org.apache.ambari.server.agent.HeartBeatHandler handler = new org.apache.ambari.server.agent.HeartBeatHandler(configuration, clusters, am, org.apache.ambari.server.security.encryption.Encryptor.NONE, org.apache.ambari.server.agent.TestHeartbeatMonitor.injector);
        org.apache.ambari.server.agent.Register reg = new org.apache.ambari.server.agent.Register();
        reg.setHostname(hostname1);
        reg.setResponseId(12);
        reg.setTimestamp(java.lang.System.currentTimeMillis() - 300);
        reg.setAgentVersion(org.apache.ambari.server.agent.TestHeartbeatMonitor.ambariMetaInfo.getServerVersion());
        org.apache.ambari.server.agent.HostInfo hi = new org.apache.ambari.server.agent.HostInfo();
        hi.setOS("Centos5");
        reg.setHardwareProfile(hi);
        org.apache.ambari.server.agent.RegistrationResponse registrationResponse = handler.handleRegistration(reg);
        org.apache.ambari.server.agent.HeartBeat hb = new org.apache.ambari.server.agent.HeartBeat();
        hb.setHostname(hostname1);
        hb.setNodeStatus(new org.apache.ambari.server.agent.HostStatus(org.apache.ambari.server.agent.HostStatus.Status.HEALTHY, "cool"));
        hb.setTimestamp(java.lang.System.currentTimeMillis());
        hb.setResponseId(12);
        handler.handleHeartBeat(hb);
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> statusCommandConfig = registrationResponse.getStatusCommands().get(0).getConfigurations();
        org.junit.Assert.assertEquals(statusCommandConfig.size(), 1);
        org.junit.Assert.assertTrue(statusCommandConfig.containsKey("hadoop-env"));
        java.util.List<org.apache.ambari.server.agent.StatusCommand> cmds = hm.generateStatusCommands(hostname1);
        org.junit.Assert.assertTrue("HeartbeatMonitor should generate StatusCommands for host1", cmds.size() == 4);
        org.junit.Assert.assertEquals("HDFS", cmds.get(0).getServiceName());
        boolean containsDATANODEStatus = false;
        boolean containsNAMENODEStatus = false;
        boolean containsSECONDARY_NAMENODEStatus = false;
        boolean containsHDFS_CLIENTStatus = false;
        for (org.apache.ambari.server.agent.StatusCommand cmd : cmds) {
            containsDATANODEStatus |= cmd.getComponentName().equals("DATANODE");
            containsNAMENODEStatus |= cmd.getComponentName().equals("NAMENODE");
            containsSECONDARY_NAMENODEStatus |= cmd.getComponentName().equals("SECONDARY_NAMENODE");
            containsHDFS_CLIENTStatus |= cmd.getComponentName().equals("HDFS_CLIENT");
            org.junit.Assert.assertTrue(cmd.getConfigurations().size() > 0);
            org.junit.Assert.assertEquals(org.apache.ambari.server.state.State.INSTALLED, cmd.getDesiredState());
            org.junit.Assert.assertEquals(false, cmd.getHasStaleConfigs());
        }
        org.junit.Assert.assertTrue(containsDATANODEStatus);
        org.junit.Assert.assertTrue(containsNAMENODEStatus);
        org.junit.Assert.assertTrue(containsSECONDARY_NAMENODEStatus);
        org.junit.Assert.assertTrue(containsHDFS_CLIENTStatus);
        cmds = hm.generateStatusCommands(hostname2);
        org.junit.Assert.assertTrue("HeartbeatMonitor should generate StatusCommands for host2, " + "even if it has only client components", cmds.size() == 1);
        org.junit.Assert.assertTrue(cmds.get(0).getComponentName().equals(org.apache.ambari.server.Role.HDFS_CLIENT.name()));
        org.junit.Assert.assertEquals(hostname2, cmds.get(0).getHostname());
    }

    @org.junit.Test
    @org.junit.Ignore
    public void testHeartbeatStateCommandsEnqueueing() throws org.apache.ambari.server.AmbariException, java.lang.InterruptedException, org.apache.ambari.server.state.fsm.InvalidStateTransitionException {
        org.apache.ambari.server.state.StackId stackId = new org.apache.ambari.server.state.StackId("HDP-0.1");
        org.apache.ambari.server.state.Clusters clusters = org.apache.ambari.server.agent.TestHeartbeatMonitor.injector.getInstance(org.apache.ambari.server.state.Clusters.class);
        clusters.addHost(hostname1);
        setOsFamily(clusters.getHost(hostname1), "redhat", "5.9");
        clusters.addCluster(clusterName, stackId);
        org.apache.ambari.server.state.Cluster cluster = clusters.getCluster(clusterName);
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion = org.apache.ambari.server.agent.TestHeartbeatMonitor.helper.getOrCreateRepositoryVersion(stackId, stackId.getStackVersion());
        java.util.Set<java.lang.String> hostNames = new java.util.HashSet<java.lang.String>() {
            {
                add(hostname1);
            }
        };
        clusters.mapAndPublishHostsToCluster(hostNames, clusterName);
        org.apache.ambari.server.state.Service hdfs = cluster.addService(serviceName, repositoryVersion);
        hdfs.addServiceComponent(org.apache.ambari.server.Role.DATANODE.name());
        hdfs.getServiceComponent(org.apache.ambari.server.Role.DATANODE.name()).addServiceComponentHost(hostname1);
        hdfs.addServiceComponent(org.apache.ambari.server.Role.NAMENODE.name());
        hdfs.getServiceComponent(org.apache.ambari.server.Role.NAMENODE.name()).addServiceComponentHost(hostname1);
        hdfs.addServiceComponent(org.apache.ambari.server.Role.SECONDARY_NAMENODE.name());
        hdfs.getServiceComponent(org.apache.ambari.server.Role.SECONDARY_NAMENODE.name()).addServiceComponentHost(hostname1);
        hdfs.getServiceComponent(org.apache.ambari.server.Role.DATANODE.name()).getServiceComponentHost(hostname1).setState(org.apache.ambari.server.state.State.INSTALLED);
        hdfs.getServiceComponent(org.apache.ambari.server.Role.NAMENODE.name()).getServiceComponentHost(hostname1).setState(org.apache.ambari.server.state.State.INSTALLED);
        hdfs.getServiceComponent(org.apache.ambari.server.Role.SECONDARY_NAMENODE.name()).getServiceComponentHost(hostname1).setState(org.apache.ambari.server.state.State.INSTALLED);
        org.mockito.ArgumentCaptor<org.apache.ambari.server.agent.AgentCommand> commandCaptor = org.mockito.ArgumentCaptor.forClass(org.apache.ambari.server.agent.AgentCommand.class);
        org.apache.ambari.server.actionmanager.ActionManager am = Mockito.mock(org.apache.ambari.server.actionmanager.ActionManager.class);
        org.apache.ambari.server.agent.HeartbeatMonitor hm = new org.apache.ambari.server.agent.HeartbeatMonitor(clusters, am, heartbeatMonitorWakeupIntervalMS, org.apache.ambari.server.agent.TestHeartbeatMonitor.injector);
        org.apache.ambari.server.configuration.Configuration configuration = Mockito.mock(org.apache.ambari.server.configuration.Configuration.class);
        Mockito.when(configuration.getHeartbeatMonitorInterval()).thenReturn(60000);
        org.apache.ambari.server.agent.HeartBeatHandler handler = new org.apache.ambari.server.agent.HeartBeatHandler(configuration, clusters, am, org.apache.ambari.server.security.encryption.Encryptor.NONE, org.apache.ambari.server.agent.TestHeartbeatMonitor.injector);
        org.apache.ambari.server.agent.Register reg = new org.apache.ambari.server.agent.Register();
        reg.setHostname(hostname1);
        reg.setResponseId(12);
        reg.setTimestamp(java.lang.System.currentTimeMillis() - 15);
        reg.setAgentVersion(org.apache.ambari.server.agent.TestHeartbeatMonitor.ambariMetaInfo.getServerVersion());
        org.apache.ambari.server.agent.HostInfo hi = new org.apache.ambari.server.agent.HostInfo();
        hi.setOS("Centos5");
        reg.setHardwareProfile(hi);
        handler.handleRegistration(reg);
        org.apache.ambari.server.agent.HeartBeat hb = new org.apache.ambari.server.agent.HeartBeat();
        hb.setHostname(hostname1);
        hb.setNodeStatus(new org.apache.ambari.server.agent.HostStatus(org.apache.ambari.server.agent.HostStatus.Status.HEALTHY, "cool"));
        hb.setTimestamp(java.lang.System.currentTimeMillis());
        hb.setResponseId(13);
        handler.handleHeartBeat(hb);
        org.apache.ambari.server.agent.TestHeartbeatMonitor.LOG.info("YYY");
        clusters.getHost(hostname1).setLastHeartbeatTime(java.lang.System.currentTimeMillis() - 15);
        hm.start();
        java.lang.Thread.sleep(3 * heartbeatMonitorWakeupIntervalMS);
        hm.shutdown();
        int tryNumber = 0;
        while (hm.isAlive()) {
            hm.join(2 * heartbeatMonitorWakeupIntervalMS);
            tryNumber++;
            if (tryNumber >= 5) {
                org.junit.Assert.fail("HeartbeatMonitor should be already stopped");
            }
        } 
        java.util.List<org.apache.ambari.server.agent.AgentCommand> cmds = commandCaptor.getAllValues();
        org.junit.Assert.assertTrue("HeartbeatMonitor should generate StatusCommands for host1", cmds.size() >= 2);
        for (org.apache.ambari.server.agent.AgentCommand command : cmds) {
            org.junit.Assert.assertEquals("HDFS", ((org.apache.ambari.server.agent.StatusCommand) (command)).getServiceName());
        }
    }

    @org.junit.Test
    @org.junit.Ignore
    public void testHeartbeatLossWithComponent() throws org.apache.ambari.server.AmbariException, java.lang.InterruptedException, org.apache.ambari.server.state.fsm.InvalidStateTransitionException {
        org.apache.ambari.server.state.StackId stackId = new org.apache.ambari.server.state.StackId("HDP-0.1");
        org.apache.ambari.server.state.Clusters clusters = org.apache.ambari.server.agent.TestHeartbeatMonitor.injector.getInstance(org.apache.ambari.server.state.Clusters.class);
        clusters.addHost(hostname1);
        setOsFamily(clusters.getHost(hostname1), "redhat", "6.3");
        clusters.addCluster(clusterName, stackId);
        org.apache.ambari.server.state.Cluster cluster = clusters.getCluster(clusterName);
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion = org.apache.ambari.server.agent.TestHeartbeatMonitor.helper.getOrCreateRepositoryVersion(stackId, stackId.getStackVersion());
        java.util.Set<java.lang.String> hostNames = new java.util.HashSet<java.lang.String>() {
            {
                add(hostname1);
            }
        };
        clusters.mapAndPublishHostsToCluster(hostNames, clusterName);
        org.apache.ambari.server.state.Service hdfs = cluster.addService(serviceName, repositoryVersion);
        hdfs.addServiceComponent(org.apache.ambari.server.Role.DATANODE.name());
        hdfs.getServiceComponent(org.apache.ambari.server.Role.DATANODE.name()).addServiceComponentHost(hostname1);
        hdfs.addServiceComponent(org.apache.ambari.server.Role.NAMENODE.name());
        hdfs.getServiceComponent(org.apache.ambari.server.Role.NAMENODE.name()).addServiceComponentHost(hostname1);
        hdfs.addServiceComponent(org.apache.ambari.server.Role.SECONDARY_NAMENODE.name());
        hdfs.getServiceComponent(org.apache.ambari.server.Role.SECONDARY_NAMENODE.name()).addServiceComponentHost(hostname1);
        hdfs.addServiceComponent(org.apache.ambari.server.Role.HDFS_CLIENT.name());
        hdfs.getServiceComponent(org.apache.ambari.server.Role.HDFS_CLIENT.name()).addServiceComponentHost(hostname1);
        org.apache.ambari.server.actionmanager.ActionManager am = Mockito.mock(org.apache.ambari.server.actionmanager.ActionManager.class);
        org.apache.ambari.server.agent.HeartbeatMonitor hm = new org.apache.ambari.server.agent.HeartbeatMonitor(clusters, am, 10, org.apache.ambari.server.agent.TestHeartbeatMonitor.injector);
        org.apache.ambari.server.configuration.Configuration configuration = Mockito.mock(org.apache.ambari.server.configuration.Configuration.class);
        Mockito.when(configuration.getHeartbeatMonitorInterval()).thenReturn(10);
        org.apache.ambari.server.agent.HeartBeatHandler handler = new org.apache.ambari.server.agent.HeartBeatHandler(configuration, clusters, am, org.apache.ambari.server.security.encryption.Encryptor.NONE, org.apache.ambari.server.agent.TestHeartbeatMonitor.injector);
        org.apache.ambari.server.agent.Register reg = new org.apache.ambari.server.agent.Register();
        reg.setHostname(hostname1);
        reg.setResponseId(12);
        reg.setTimestamp(java.lang.System.currentTimeMillis() - 300);
        reg.setAgentVersion(org.apache.ambari.server.agent.TestHeartbeatMonitor.ambariMetaInfo.getServerVersion());
        org.apache.ambari.server.agent.HostInfo hi = new org.apache.ambari.server.agent.HostInfo();
        hi.setOS("Centos5");
        reg.setHardwareProfile(hi);
        handler.handleRegistration(reg);
        cluster = clusters.getClustersForHost(hostname1).iterator().next();
        for (org.apache.ambari.server.state.ServiceComponentHost sch : cluster.getServiceComponentHosts(hostname1)) {
            if (sch.getServiceComponentName().equals("NAMENODE")) {
                sch.handleEvent(new org.apache.ambari.server.state.svccomphost.ServiceComponentHostInstallEvent(sch.getServiceComponentName(), sch.getHostName(), java.lang.System.currentTimeMillis(), "HDP-0.1"));
                sch.handleEvent(new org.apache.ambari.server.state.svccomphost.ServiceComponentHostOpSucceededEvent(sch.getServiceComponentName(), sch.getHostName(), java.lang.System.currentTimeMillis()));
                sch.handleEvent(new org.apache.ambari.server.state.svccomphost.ServiceComponentHostStartedEvent(sch.getServiceComponentName(), sch.getHostName(), java.lang.System.currentTimeMillis()));
            } else if (sch.getServiceComponentName().equals("DATANODE")) {
                sch.handleEvent(new org.apache.ambari.server.state.svccomphost.ServiceComponentHostInstallEvent(sch.getServiceComponentName(), sch.getHostName(), java.lang.System.currentTimeMillis(), "HDP-0.1"));
            } else if (sch.getServiceComponentName().equals("SECONDARY_NAMENODE")) {
                sch.handleEvent(new org.apache.ambari.server.state.svccomphost.ServiceComponentHostInstallEvent(sch.getServiceComponentName(), sch.getHostName(), java.lang.System.currentTimeMillis(), "HDP-0.1"));
                sch.handleEvent(new org.apache.ambari.server.state.svccomphost.ServiceComponentHostOpSucceededEvent(sch.getServiceComponentName(), sch.getHostName(), java.lang.System.currentTimeMillis()));
                sch.handleEvent(new org.apache.ambari.server.state.svccomphost.ServiceComponentHostDisableEvent(sch.getServiceComponentName(), sch.getHostName(), java.lang.System.currentTimeMillis()));
            }
        }
        org.apache.ambari.server.agent.HeartBeat hb = new org.apache.ambari.server.agent.HeartBeat();
        hb.setHostname(hostname1);
        hb.setNodeStatus(new org.apache.ambari.server.agent.HostStatus(org.apache.ambari.server.agent.HostStatus.Status.HEALTHY, "cool"));
        hb.setTimestamp(java.lang.System.currentTimeMillis());
        hb.setResponseId(12);
        handler.handleHeartBeat(hb);
        hm.start();
        hm.shutdown();
        cluster = clusters.getClustersForHost(hostname1).iterator().next();
        for (org.apache.ambari.server.state.ServiceComponentHost sch : cluster.getServiceComponentHosts(hostname1)) {
            org.apache.ambari.server.state.Service s = cluster.getService(sch.getServiceName());
            org.apache.ambari.server.state.ServiceComponent sc = s.getServiceComponent(sch.getServiceComponentName());
            if (sch.getServiceComponentName().equals("NAMENODE")) {
                org.junit.Assert.assertEquals(sch.getServiceComponentName(), org.apache.ambari.server.state.State.UNKNOWN, sch.getState());
            } else if (sch.getServiceComponentName().equals("DATANODE")) {
                org.junit.Assert.assertEquals(sch.getServiceComponentName(), org.apache.ambari.server.state.State.INSTALLING, sch.getState());
            } else if (sc.isClientComponent()) {
                org.junit.Assert.assertEquals(sch.getServiceComponentName(), org.apache.ambari.server.state.State.INIT, sch.getState());
            } else if (sch.getServiceComponentName().equals("SECONDARY_NAMENODE")) {
                org.junit.Assert.assertEquals(sch.getServiceComponentName(), org.apache.ambari.server.state.State.DISABLED, sch.getState());
            }
        }
    }

    @org.junit.Test
    public void testStateCommandsWithAlertsGeneration() throws org.apache.ambari.server.AmbariException, java.lang.InterruptedException, org.apache.ambari.server.state.fsm.InvalidStateTransitionException {
        org.apache.ambari.server.state.StackId stackId = new org.apache.ambari.server.state.StackId("HDP-2.0.7");
        org.apache.ambari.server.state.Clusters clusters = org.apache.ambari.server.agent.TestHeartbeatMonitor.injector.getInstance(org.apache.ambari.server.state.Clusters.class);
        clusters.addHost(hostname1);
        setOsFamily(clusters.getHost(hostname1), "redhat", "6.3");
        clusters.addHost(hostname2);
        setOsFamily(clusters.getHost(hostname2), "redhat", "6.3");
        clusters.addCluster(clusterName, stackId);
        org.apache.ambari.server.state.Cluster cluster = clusters.getCluster(clusterName);
        cluster.setDesiredStackVersion(stackId);
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion = org.apache.ambari.server.agent.TestHeartbeatMonitor.helper.getOrCreateRepositoryVersion(stackId, stackId.getStackVersion());
        java.util.Set<java.lang.String> hostNames = new java.util.HashSet<java.lang.String>() {
            {
                add(hostname1);
                add(hostname2);
            }
        };
        clusters.mapAndPublishHostsToCluster(hostNames, clusterName);
        org.apache.ambari.server.state.Service hdfs = cluster.addService(serviceName, repositoryVersion);
        hdfs.addServiceComponent(org.apache.ambari.server.Role.DATANODE.name());
        hdfs.getServiceComponent(org.apache.ambari.server.Role.DATANODE.name()).addServiceComponentHost(hostname1);
        hdfs.addServiceComponent(org.apache.ambari.server.Role.NAMENODE.name());
        hdfs.getServiceComponent(org.apache.ambari.server.Role.NAMENODE.name()).addServiceComponentHost(hostname1);
        hdfs.addServiceComponent(org.apache.ambari.server.Role.SECONDARY_NAMENODE.name());
        hdfs.getServiceComponent(org.apache.ambari.server.Role.SECONDARY_NAMENODE.name()).addServiceComponentHost(hostname1);
        hdfs.getServiceComponent(org.apache.ambari.server.Role.DATANODE.name()).getServiceComponentHost(hostname1).setState(org.apache.ambari.server.state.State.INSTALLED);
        hdfs.getServiceComponent(org.apache.ambari.server.Role.NAMENODE.name()).getServiceComponentHost(hostname1).setState(org.apache.ambari.server.state.State.INSTALLED);
        hdfs.getServiceComponent(org.apache.ambari.server.Role.SECONDARY_NAMENODE.name()).getServiceComponentHost(hostname1).setState(org.apache.ambari.server.state.State.INSTALLED);
        org.apache.ambari.server.actionmanager.ActionManager am = Mockito.mock(org.apache.ambari.server.actionmanager.ActionManager.class);
        org.apache.ambari.server.agent.HeartbeatMonitor hm = new org.apache.ambari.server.agent.HeartbeatMonitor(clusters, am, heartbeatMonitorWakeupIntervalMS, org.apache.ambari.server.agent.TestHeartbeatMonitor.injector);
        org.apache.ambari.server.configuration.Configuration configuration = Mockito.mock(org.apache.ambari.server.configuration.Configuration.class);
        Mockito.when(configuration.getHeartbeatMonitorInterval()).thenReturn(60000);
        org.apache.ambari.server.agent.HeartBeatHandler handler = new org.apache.ambari.server.agent.HeartBeatHandler(configuration, clusters, am, org.apache.ambari.server.security.encryption.Encryptor.NONE, org.apache.ambari.server.agent.TestHeartbeatMonitor.injector);
        org.apache.ambari.server.agent.Register reg = new org.apache.ambari.server.agent.Register();
        reg.setHostname(hostname1);
        reg.setResponseId(12);
        reg.setTimestamp(java.lang.System.currentTimeMillis() - 300);
        reg.setAgentVersion(org.apache.ambari.server.agent.TestHeartbeatMonitor.ambariMetaInfo.getServerVersion());
        org.apache.ambari.server.agent.HostInfo hi = new org.apache.ambari.server.agent.HostInfo();
        hi.setOS("Centos5");
        reg.setHardwareProfile(hi);
        handler.handleRegistration(reg);
        org.apache.ambari.server.agent.HeartBeat hb = new org.apache.ambari.server.agent.HeartBeat();
        hb.setHostname(hostname1);
        hb.setNodeStatus(new org.apache.ambari.server.agent.HostStatus(org.apache.ambari.server.agent.HostStatus.Status.HEALTHY, "cool"));
        hb.setTimestamp(java.lang.System.currentTimeMillis());
        hb.setResponseId(12);
        handler.handleHeartBeat(hb);
        java.util.List<org.apache.ambari.server.agent.StatusCommand> cmds = hm.generateStatusCommands(hostname1);
        org.junit.Assert.assertEquals("HeartbeatMonitor should generate StatusCommands for host1", 3, cmds.size());
        org.junit.Assert.assertEquals("HDFS", cmds.get(0).getServiceName());
        cmds = hm.generateStatusCommands(hostname2);
        org.junit.Assert.assertTrue("HeartbeatMonitor should not generate StatusCommands for host2 because it has no services", cmds.isEmpty());
    }
}