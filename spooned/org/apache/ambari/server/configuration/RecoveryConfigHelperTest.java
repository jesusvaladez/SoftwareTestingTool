package org.apache.ambari.server.configuration;
import static org.apache.ambari.server.agent.DummyHeartbeatConstants.DATANODE;
import static org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1;
import static org.apache.ambari.server.agent.DummyHeartbeatConstants.HDFS;
import static org.apache.ambari.server.agent.DummyHeartbeatConstants.NAMENODE;
public class RecoveryConfigHelperTest {
    private com.google.inject.Injector injector;

    private org.apache.ambari.server.orm.InMemoryDefaultTestModule module;

    @com.google.inject.Inject
    private org.apache.ambari.server.agent.HeartbeatTestHelper heartbeatTestHelper;

    @com.google.inject.Inject
    private org.apache.ambari.server.agent.RecoveryConfigHelper recoveryConfigHelper;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.RepositoryVersionDAO repositoryVersionDAO;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.OrmTestHelper helper;

    private final java.lang.String STACK_VERSION = "0.1";

    private final java.lang.String REPO_VERSION = "0.1-1234";

    private final org.apache.ambari.server.state.StackId stackId = new org.apache.ambari.server.state.StackId("HDP", STACK_VERSION);

    private final java.lang.String dummyClusterName = "cluster1";

    private final java.lang.Long dummyClusterId = 1L;

    @org.junit.Before
    public void setup() throws java.lang.Exception {
        module = org.apache.ambari.server.agent.HeartbeatTestHelper.getTestModule();
        injector = com.google.inject.Guice.createInjector(module);
        injector.getInstance(org.apache.ambari.server.orm.GuiceJpaInitializer.class);
        injector.injectMembers(this);
        com.google.common.eventbus.EventBus synchronizedBus = org.apache.ambari.server.utils.EventBusSynchronizer.synchronizeAmbariEventPublisher(injector);
        synchronizedBus.register(recoveryConfigHelper);
    }

    @org.junit.After
    public void teardown() throws java.lang.Exception {
        org.apache.ambari.server.H2DatabaseCleaner.clearDatabaseAndStopPersistenceService(injector);
    }

    @org.junit.Test
    public void testServiceComponentInstalled() throws java.lang.Exception {
        org.apache.ambari.server.state.Cluster cluster = heartbeatTestHelper.getDummyCluster();
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion = helper.getOrCreateRepositoryVersion(cluster);
        org.apache.ambari.server.state.Service hdfs = cluster.addService(org.apache.ambari.server.agent.DummyHeartbeatConstants.HDFS, repositoryVersion);
        hdfs.addServiceComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.DATANODE).setRecoveryEnabled(true);
        hdfs.getServiceComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.DATANODE).addServiceComponentHost(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        org.apache.ambari.server.agent.RecoveryConfig recoveryConfig = recoveryConfigHelper.getRecoveryConfig(cluster.getClusterName(), org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        org.junit.Assert.assertEquals(com.google.common.collect.Lists.newArrayList(new org.apache.ambari.server.agent.RecoveryConfigComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.DATANODE, org.apache.ambari.server.agent.DummyHeartbeatConstants.HDFS, org.apache.ambari.server.state.State.INIT)), recoveryConfig.getEnabledComponents());
        hdfs.addServiceComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.NAMENODE).setRecoveryEnabled(true);
        hdfs.getServiceComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.NAMENODE).addServiceComponentHost(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        recoveryConfig = recoveryConfigHelper.getRecoveryConfig(cluster.getClusterName(), org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        org.junit.Assert.assertEquals(com.google.common.collect.Lists.newArrayList(new org.apache.ambari.server.agent.RecoveryConfigComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.DATANODE, org.apache.ambari.server.agent.DummyHeartbeatConstants.HDFS, org.apache.ambari.server.state.State.INIT), new org.apache.ambari.server.agent.RecoveryConfigComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.NAMENODE, org.apache.ambari.server.agent.DummyHeartbeatConstants.HDFS, org.apache.ambari.server.state.State.INIT)), recoveryConfig.getEnabledComponents());
    }

    @org.junit.Test
    public void testServiceComponentUninstalled() throws java.lang.Exception {
        org.apache.ambari.server.state.Cluster cluster = heartbeatTestHelper.getDummyCluster();
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion = helper.getOrCreateRepositoryVersion(cluster);
        org.apache.ambari.server.state.Service hdfs = cluster.addService(org.apache.ambari.server.agent.DummyHeartbeatConstants.HDFS, repositoryVersion);
        hdfs.addServiceComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.DATANODE).setRecoveryEnabled(true);
        hdfs.getServiceComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.DATANODE).addServiceComponentHost(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        hdfs.addServiceComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.NAMENODE).setRecoveryEnabled(true);
        hdfs.getServiceComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.NAMENODE).addServiceComponentHost(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        org.apache.ambari.server.agent.RecoveryConfig recoveryConfig = recoveryConfigHelper.getRecoveryConfig(cluster.getClusterName(), org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        org.junit.Assert.assertEquals(com.google.common.collect.Lists.newArrayList(new org.apache.ambari.server.agent.RecoveryConfigComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.DATANODE, org.apache.ambari.server.agent.DummyHeartbeatConstants.HDFS, org.apache.ambari.server.state.State.INIT), new org.apache.ambari.server.agent.RecoveryConfigComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.NAMENODE, org.apache.ambari.server.agent.DummyHeartbeatConstants.HDFS, org.apache.ambari.server.state.State.INIT)), recoveryConfig.getEnabledComponents());
        hdfs.getServiceComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.DATANODE).getServiceComponentHost(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1).delete(new org.apache.ambari.server.controller.internal.DeleteHostComponentStatusMetaData());
        recoveryConfig = recoveryConfigHelper.getRecoveryConfig(cluster.getClusterName(), org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        org.junit.Assert.assertEquals(com.google.common.collect.Lists.newArrayList(new org.apache.ambari.server.agent.RecoveryConfigComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.NAMENODE, org.apache.ambari.server.agent.DummyHeartbeatConstants.HDFS, org.apache.ambari.server.state.State.INIT)), recoveryConfig.getEnabledComponents());
    }

    @org.junit.Test
    public void testMaintenanceModeChanged() throws java.lang.Exception {
        org.apache.ambari.server.state.Cluster cluster = heartbeatTestHelper.getDummyCluster();
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion = helper.getOrCreateRepositoryVersion(cluster);
        org.apache.ambari.server.state.Service hdfs = cluster.addService(org.apache.ambari.server.agent.DummyHeartbeatConstants.HDFS, repositoryVersion);
        hdfs.addServiceComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.DATANODE).setRecoveryEnabled(true);
        hdfs.getServiceComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.DATANODE).addServiceComponentHost(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        hdfs.addServiceComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.NAMENODE).setRecoveryEnabled(true);
        hdfs.getServiceComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.NAMENODE).addServiceComponentHost(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        org.apache.ambari.server.agent.RecoveryConfig recoveryConfig = recoveryConfigHelper.getRecoveryConfig(cluster.getClusterName(), org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        org.junit.Assert.assertEquals(com.google.common.collect.Lists.newArrayList(new org.apache.ambari.server.agent.RecoveryConfigComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.DATANODE, org.apache.ambari.server.agent.DummyHeartbeatConstants.HDFS, org.apache.ambari.server.state.State.INIT), new org.apache.ambari.server.agent.RecoveryConfigComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.NAMENODE, org.apache.ambari.server.agent.DummyHeartbeatConstants.HDFS, org.apache.ambari.server.state.State.INIT)), recoveryConfig.getEnabledComponents());
        hdfs.getServiceComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.DATANODE).getServiceComponentHost(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1).setMaintenanceState(org.apache.ambari.server.state.MaintenanceState.ON);
        recoveryConfig = recoveryConfigHelper.getRecoveryConfig(cluster.getClusterName(), org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        org.junit.Assert.assertEquals(com.google.common.collect.Lists.newArrayList(new org.apache.ambari.server.agent.RecoveryConfigComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.NAMENODE, org.apache.ambari.server.agent.DummyHeartbeatConstants.HDFS, org.apache.ambari.server.state.State.INIT)), recoveryConfig.getEnabledComponents());
    }

    @org.junit.Test
    public void testServiceComponentRecoveryChanged() throws java.lang.Exception {
        org.apache.ambari.server.state.Cluster cluster = heartbeatTestHelper.getDummyCluster();
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion = helper.getOrCreateRepositoryVersion(cluster);
        org.apache.ambari.server.state.Service hdfs = cluster.addService(org.apache.ambari.server.agent.DummyHeartbeatConstants.HDFS, repositoryVersion);
        hdfs.addServiceComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.DATANODE).setRecoveryEnabled(true);
        hdfs.getServiceComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.DATANODE).addServiceComponentHost(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        org.apache.ambari.server.agent.RecoveryConfig recoveryConfig = recoveryConfigHelper.getRecoveryConfig(cluster.getClusterName(), org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        org.junit.Assert.assertEquals(com.google.common.collect.Lists.newArrayList(new org.apache.ambari.server.agent.RecoveryConfigComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.DATANODE, org.apache.ambari.server.agent.DummyHeartbeatConstants.HDFS, org.apache.ambari.server.state.State.INIT)), recoveryConfig.getEnabledComponents());
        hdfs.getServiceComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.DATANODE).setRecoveryEnabled(false);
        recoveryConfig = recoveryConfigHelper.getRecoveryConfig(cluster.getClusterName(), org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        org.junit.Assert.assertEquals(new java.util.ArrayList<org.apache.ambari.server.agent.RecoveryConfigComponent>(), recoveryConfig.getEnabledComponents());
    }

    @org.junit.Test
    public void testMultiNodeCluster() throws java.lang.Exception {
        java.util.Set<java.lang.String> hostNames = new java.util.HashSet<java.lang.String>() {
            {
                add("Host1");
                add("Host2");
            }
        };
        org.apache.ambari.server.state.Cluster cluster = getDummyCluster(hostNames);
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion = helper.getOrCreateRepositoryVersion(cluster);
        org.apache.ambari.server.state.Service hdfs = cluster.addService(org.apache.ambari.server.agent.DummyHeartbeatConstants.HDFS, repositoryVersion);
        hdfs.addServiceComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.DATANODE).setRecoveryEnabled(true);
        hdfs.getServiceComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.DATANODE).addServiceComponentHost("Host1");
        hdfs.getServiceComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.DATANODE).addServiceComponentHost("Host2");
        org.apache.ambari.server.agent.RecoveryConfig recoveryConfig = recoveryConfigHelper.getRecoveryConfig(cluster.getClusterName(), "Host1");
        org.junit.Assert.assertEquals(com.google.common.collect.Lists.newArrayList(new org.apache.ambari.server.agent.RecoveryConfigComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.DATANODE, org.apache.ambari.server.agent.DummyHeartbeatConstants.HDFS, org.apache.ambari.server.state.State.INIT)), recoveryConfig.getEnabledComponents());
        boolean isConfigStale = recoveryConfigHelper.isConfigStale(cluster.getClusterName(), "Host2", -1);
        org.junit.Assert.assertTrue(isConfigStale);
    }

    private org.apache.ambari.server.state.Cluster getDummyCluster(java.util.Set<java.lang.String> hostNames) throws java.lang.Exception {
        java.util.Map<java.lang.String, java.lang.String> configProperties = new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put(org.apache.ambari.server.agent.RecoveryConfigHelper.RECOVERY_ENABLED_KEY, "true");
                put(org.apache.ambari.server.agent.RecoveryConfigHelper.RECOVERY_TYPE_KEY, "AUTO_START");
                put(org.apache.ambari.server.agent.RecoveryConfigHelper.RECOVERY_MAX_COUNT_KEY, "4");
                put(org.apache.ambari.server.agent.RecoveryConfigHelper.RECOVERY_LIFETIME_MAX_COUNT_KEY, "10");
                put(org.apache.ambari.server.agent.RecoveryConfigHelper.RECOVERY_WINDOW_IN_MIN_KEY, "23");
                put(org.apache.ambari.server.agent.RecoveryConfigHelper.RECOVERY_RETRY_GAP_KEY, "2");
            }
        };
        org.apache.ambari.server.state.Cluster cluster = heartbeatTestHelper.getDummyCluster(dummyClusterName, dummyClusterId, stackId, REPO_VERSION, configProperties, hostNames);
        return cluster;
    }
}