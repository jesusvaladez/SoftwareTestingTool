package org.apache.ambari.server.metadata;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
public class RoleGraphTest {
    private com.google.inject.Injector injector;

    private org.apache.ambari.server.metadata.RoleCommandOrderProvider roleCommandOrderProvider;

    private org.apache.ambari.server.stageplanner.RoleGraphFactory roleGraphFactory;

    private org.apache.ambari.server.actionmanager.HostRoleCommandFactory hrcFactory;

    @org.junit.Before
    public void setup() throws java.lang.Exception {
        injector = com.google.inject.Guice.createInjector(new org.apache.ambari.server.orm.InMemoryDefaultTestModule());
        injector.getInstance(org.apache.ambari.server.orm.GuiceJpaInitializer.class);
        roleCommandOrderProvider = injector.getInstance(org.apache.ambari.server.metadata.RoleCommandOrderProvider.class);
        roleGraphFactory = injector.getInstance(org.apache.ambari.server.stageplanner.RoleGraphFactory.class);
        hrcFactory = injector.getInstance(org.apache.ambari.server.actionmanager.HostRoleCommandFactory.class);
    }

    @org.junit.After
    public void teardown() throws org.apache.ambari.server.AmbariException, java.sql.SQLException {
        org.apache.ambari.server.H2DatabaseCleaner.clearDatabaseAndStopPersistenceService(injector);
    }

    @org.junit.Test
    public void testValidateOrder() throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.cluster.ClusterImpl cluster = Mockito.mock(org.apache.ambari.server.state.cluster.ClusterImpl.class);
        Mockito.when(cluster.getCurrentStackVersion()).thenReturn(new org.apache.ambari.server.state.StackId("HDP-2.0.6"));
        Mockito.when(cluster.getClusterId()).thenReturn(1L);
        org.apache.ambari.server.state.Service hdfsService = Mockito.mock(org.apache.ambari.server.state.Service.class);
        Mockito.when(hdfsService.getDesiredStackId()).thenReturn(new org.apache.ambari.server.state.StackId("HDP-2.0.6"));
        Mockito.when(cluster.getServices()).thenReturn(com.google.common.collect.ImmutableMap.<java.lang.String, org.apache.ambari.server.state.Service>builder().put("HDFS", hdfsService).build());
        org.apache.ambari.server.metadata.RoleCommandOrder rco = roleCommandOrderProvider.getRoleCommandOrder(cluster);
        org.apache.ambari.server.stageplanner.RoleGraphNode datanode_upgrade = new org.apache.ambari.server.stageplanner.RoleGraphNode(org.apache.ambari.server.Role.DATANODE, org.apache.ambari.server.RoleCommand.UPGRADE);
        org.apache.ambari.server.stageplanner.RoleGraphNode hdfs_client_upgrade = new org.apache.ambari.server.stageplanner.RoleGraphNode(org.apache.ambari.server.Role.HDFS_CLIENT, org.apache.ambari.server.RoleCommand.UPGRADE);
        junit.framework.Assert.assertEquals(-1, rco.order(datanode_upgrade, hdfs_client_upgrade));
        junit.framework.Assert.assertEquals(1, rco.order(hdfs_client_upgrade, datanode_upgrade));
        org.apache.ambari.server.stageplanner.RoleGraphNode namenode_upgrade = new org.apache.ambari.server.stageplanner.RoleGraphNode(org.apache.ambari.server.Role.NAMENODE, org.apache.ambari.server.RoleCommand.UPGRADE);
        org.apache.ambari.server.stageplanner.RoleGraphNode ganglia_server_upgrade = new org.apache.ambari.server.stageplanner.RoleGraphNode(org.apache.ambari.server.Role.GANGLIA_SERVER, org.apache.ambari.server.RoleCommand.UPGRADE);
        junit.framework.Assert.assertEquals(1, rco.order(ganglia_server_upgrade, hdfs_client_upgrade));
        junit.framework.Assert.assertEquals(1, rco.order(ganglia_server_upgrade, datanode_upgrade));
        junit.framework.Assert.assertEquals(-1, rco.order(namenode_upgrade, ganglia_server_upgrade));
        org.apache.ambari.server.stageplanner.RoleGraphNode datanode_start = new org.apache.ambari.server.stageplanner.RoleGraphNode(org.apache.ambari.server.Role.DATANODE, org.apache.ambari.server.RoleCommand.START);
        org.apache.ambari.server.stageplanner.RoleGraphNode datanode_install = new org.apache.ambari.server.stageplanner.RoleGraphNode(org.apache.ambari.server.Role.DATANODE, org.apache.ambari.server.RoleCommand.INSTALL);
        org.apache.ambari.server.stageplanner.RoleGraphNode jobtracker_start = new org.apache.ambari.server.stageplanner.RoleGraphNode(org.apache.ambari.server.Role.JOBTRACKER, org.apache.ambari.server.RoleCommand.START);
        junit.framework.Assert.assertEquals(1, rco.order(datanode_start, datanode_install));
        junit.framework.Assert.assertEquals(1, rco.order(jobtracker_start, datanode_start));
        junit.framework.Assert.assertEquals(0, rco.order(jobtracker_start, jobtracker_start));
        org.apache.ambari.server.stageplanner.RoleGraphNode pig_service_check = new org.apache.ambari.server.stageplanner.RoleGraphNode(org.apache.ambari.server.Role.PIG_SERVICE_CHECK, org.apache.ambari.server.RoleCommand.SERVICE_CHECK);
        org.apache.ambari.server.stageplanner.RoleGraphNode resourcemanager_start = new org.apache.ambari.server.stageplanner.RoleGraphNode(org.apache.ambari.server.Role.RESOURCEMANAGER, org.apache.ambari.server.RoleCommand.START);
        junit.framework.Assert.assertEquals(-1, rco.order(resourcemanager_start, pig_service_check));
        org.apache.ambari.server.stageplanner.RoleGraphNode hdfs_service_check = new org.apache.ambari.server.stageplanner.RoleGraphNode(org.apache.ambari.server.Role.HDFS_SERVICE_CHECK, org.apache.ambari.server.RoleCommand.SERVICE_CHECK);
        org.apache.ambari.server.stageplanner.RoleGraphNode snamenode_start = new org.apache.ambari.server.stageplanner.RoleGraphNode(org.apache.ambari.server.Role.SECONDARY_NAMENODE, org.apache.ambari.server.RoleCommand.START);
        junit.framework.Assert.assertEquals(-1, rco.order(snamenode_start, hdfs_service_check));
        org.apache.ambari.server.stageplanner.RoleGraphNode mapred2_service_check = new org.apache.ambari.server.stageplanner.RoleGraphNode(org.apache.ambari.server.Role.MAPREDUCE2_SERVICE_CHECK, org.apache.ambari.server.RoleCommand.SERVICE_CHECK);
        org.apache.ambari.server.stageplanner.RoleGraphNode rm_start = new org.apache.ambari.server.stageplanner.RoleGraphNode(org.apache.ambari.server.Role.RESOURCEMANAGER, org.apache.ambari.server.RoleCommand.START);
        org.apache.ambari.server.stageplanner.RoleGraphNode nm_start = new org.apache.ambari.server.stageplanner.RoleGraphNode(org.apache.ambari.server.Role.NODEMANAGER, org.apache.ambari.server.RoleCommand.START);
        org.apache.ambari.server.stageplanner.RoleGraphNode hs_start = new org.apache.ambari.server.stageplanner.RoleGraphNode(org.apache.ambari.server.Role.HISTORYSERVER, org.apache.ambari.server.RoleCommand.START);
        junit.framework.Assert.assertEquals(-1, rco.order(rm_start, mapred2_service_check));
        junit.framework.Assert.assertEquals(-1, rco.order(nm_start, mapred2_service_check));
        junit.framework.Assert.assertEquals(-1, rco.order(hs_start, mapred2_service_check));
        junit.framework.Assert.assertEquals(-1, rco.order(hs_start, mapred2_service_check));
        junit.framework.Assert.assertEquals(1, rco.order(nm_start, rm_start));
        org.apache.ambari.server.stageplanner.RoleGraphNode nn_start = new org.apache.ambari.server.stageplanner.RoleGraphNode(org.apache.ambari.server.Role.NAMENODE, org.apache.ambari.server.RoleCommand.START);
        org.apache.ambari.server.stageplanner.RoleGraphNode jn_start = new org.apache.ambari.server.stageplanner.RoleGraphNode(org.apache.ambari.server.Role.JOURNALNODE, org.apache.ambari.server.RoleCommand.START);
        org.apache.ambari.server.stageplanner.RoleGraphNode zk_server_start = new org.apache.ambari.server.stageplanner.RoleGraphNode(org.apache.ambari.server.Role.ZOOKEEPER_SERVER, org.apache.ambari.server.RoleCommand.START);
        org.apache.ambari.server.stageplanner.RoleGraphNode hbase_master_start = new org.apache.ambari.server.stageplanner.RoleGraphNode(org.apache.ambari.server.Role.HBASE_MASTER, org.apache.ambari.server.RoleCommand.START);
        org.apache.ambari.server.stageplanner.RoleGraphNode hive_srv_start = new org.apache.ambari.server.stageplanner.RoleGraphNode(org.apache.ambari.server.Role.HIVE_SERVER, org.apache.ambari.server.RoleCommand.START);
        org.apache.ambari.server.stageplanner.RoleGraphNode hive_ms_start = new org.apache.ambari.server.stageplanner.RoleGraphNode(org.apache.ambari.server.Role.HIVE_METASTORE, org.apache.ambari.server.RoleCommand.START);
        org.apache.ambari.server.stageplanner.RoleGraphNode mysql_start = new org.apache.ambari.server.stageplanner.RoleGraphNode(org.apache.ambari.server.Role.MYSQL_SERVER, org.apache.ambari.server.RoleCommand.START);
        org.apache.ambari.server.stageplanner.RoleGraphNode oozie_srv_start = new org.apache.ambari.server.stageplanner.RoleGraphNode(org.apache.ambari.server.Role.OOZIE_SERVER, org.apache.ambari.server.RoleCommand.START);
        org.apache.ambari.server.stageplanner.RoleGraphNode webhcat_srv_start = new org.apache.ambari.server.stageplanner.RoleGraphNode(org.apache.ambari.server.Role.WEBHCAT_SERVER, org.apache.ambari.server.RoleCommand.START);
        org.apache.ambari.server.stageplanner.RoleGraphNode flume_start = new org.apache.ambari.server.stageplanner.RoleGraphNode(org.apache.ambari.server.Role.FLUME_HANDLER, org.apache.ambari.server.RoleCommand.START);
        org.apache.ambari.server.stageplanner.RoleGraphNode zkfc_start = new org.apache.ambari.server.stageplanner.RoleGraphNode(org.apache.ambari.server.Role.ZKFC, org.apache.ambari.server.RoleCommand.START);
        junit.framework.Assert.assertEquals(0, rco.order(nn_start, jn_start));
        junit.framework.Assert.assertEquals(0, rco.order(nn_start, zk_server_start));
        junit.framework.Assert.assertEquals(0, rco.order(zkfc_start, nn_start));
        junit.framework.Assert.assertEquals(1, rco.order(flume_start, oozie_srv_start));
        junit.framework.Assert.assertEquals(1, rco.order(hbase_master_start, zk_server_start));
        junit.framework.Assert.assertEquals(1, rco.order(hive_srv_start, mysql_start));
        junit.framework.Assert.assertEquals(1, rco.order(hive_ms_start, mysql_start));
        junit.framework.Assert.assertEquals(1, rco.order(webhcat_srv_start, datanode_start));
        org.apache.ambari.server.state.Service hdfsServiceMock = Mockito.mock(org.apache.ambari.server.state.Service.class);
        org.apache.ambari.server.state.ServiceComponent jnComponentMock = Mockito.mock(org.apache.ambari.server.state.ServiceComponent.class);
        Mockito.when(cluster.getService("HDFS")).thenReturn(hdfsServiceMock);
        Mockito.when(hdfsServiceMock.getServiceComponent("JOURNALNODE")).thenReturn(jnComponentMock);
        rco = roleCommandOrderProvider.getRoleCommandOrder(cluster);
        junit.framework.Assert.assertEquals(1, rco.order(nn_start, jn_start));
        junit.framework.Assert.assertEquals(1, rco.order(nn_start, zk_server_start));
        junit.framework.Assert.assertEquals(1, rco.order(zkfc_start, nn_start));
    }

    @org.junit.Test
    public void testGetOrderedHostRoleCommands() throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.cluster.ClusterImpl cluster = Mockito.mock(org.apache.ambari.server.state.cluster.ClusterImpl.class);
        Mockito.when(cluster.getCurrentStackVersion()).thenReturn(new org.apache.ambari.server.state.StackId("HDP-2.0.6"));
        Mockito.when(cluster.getClusterId()).thenReturn(1L);
        org.apache.ambari.server.state.Service hdfsService = Mockito.mock(org.apache.ambari.server.state.Service.class);
        Mockito.when(hdfsService.getDesiredStackId()).thenReturn(new org.apache.ambari.server.state.StackId("HDP-2.0.6"));
        org.apache.ambari.server.state.Service zkService = Mockito.mock(org.apache.ambari.server.state.Service.class);
        Mockito.when(zkService.getDesiredStackId()).thenReturn(new org.apache.ambari.server.state.StackId("HDP-2.0.6"));
        org.apache.ambari.server.state.Service hbaseService = Mockito.mock(org.apache.ambari.server.state.Service.class);
        Mockito.when(hbaseService.getDesiredStackId()).thenReturn(new org.apache.ambari.server.state.StackId("HDP-2.0.6"));
        Mockito.when(cluster.getServices()).thenReturn(com.google.common.collect.ImmutableMap.<java.lang.String, org.apache.ambari.server.state.Service>builder().put("HDFS", hdfsService).put("ZOOKEEPER", zkService).put("HBASE", hbaseService).build());
        org.apache.ambari.server.metadata.RoleCommandOrder rco = roleCommandOrderProvider.getRoleCommandOrder(cluster);
        org.apache.ambari.server.stageplanner.RoleGraph roleGraph = roleGraphFactory.createNew(rco);
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.actionmanager.HostRoleCommand>> unorderedCommands = new java.util.HashMap<>();
        java.util.Map<java.lang.String, org.apache.ambari.server.actionmanager.HostRoleCommand> c6401Commands = new java.util.HashMap<>();
        java.util.Map<java.lang.String, org.apache.ambari.server.actionmanager.HostRoleCommand> c6402Commands = new java.util.HashMap<>();
        java.util.Map<java.lang.String, org.apache.ambari.server.actionmanager.HostRoleCommand> c6403Commands = new java.util.HashMap<>();
        org.apache.ambari.server.actionmanager.HostRoleCommand hrcNameNode = hrcFactory.create("c6041", org.apache.ambari.server.Role.NAMENODE, null, org.apache.ambari.server.RoleCommand.START);
        org.apache.ambari.server.actionmanager.HostRoleCommand hrcZooKeeperHost1 = hrcFactory.create("c6041", org.apache.ambari.server.Role.ZOOKEEPER_SERVER, null, org.apache.ambari.server.RoleCommand.START);
        org.apache.ambari.server.actionmanager.HostRoleCommand hrcHBaseMaster = hrcFactory.create("c6042", org.apache.ambari.server.Role.HBASE_MASTER, null, org.apache.ambari.server.RoleCommand.START);
        org.apache.ambari.server.actionmanager.HostRoleCommand hrcZooKeeperHost3 = hrcFactory.create("c6043", org.apache.ambari.server.Role.ZOOKEEPER_SERVER, null, org.apache.ambari.server.RoleCommand.START);
        c6401Commands.put(hrcNameNode.getRole().name(), hrcNameNode);
        c6401Commands.put(hrcZooKeeperHost1.getRole().name(), hrcZooKeeperHost1);
        c6402Commands.put(hrcHBaseMaster.getRole().name(), hrcHBaseMaster);
        c6403Commands.put(hrcZooKeeperHost3.getRole().name(), hrcZooKeeperHost3);
        unorderedCommands.put("c6401", c6401Commands);
        unorderedCommands.put("c6402", c6402Commands);
        unorderedCommands.put("c6403", c6403Commands);
        java.util.List<java.util.Map<java.lang.String, java.util.List<org.apache.ambari.server.actionmanager.HostRoleCommand>>> stages = roleGraph.getOrderedHostRoleCommands(unorderedCommands);
        junit.framework.Assert.assertEquals(2, stages.size());
        java.util.Map<java.lang.String, java.util.List<org.apache.ambari.server.actionmanager.HostRoleCommand>> stage1 = stages.get(0);
        java.util.Map<java.lang.String, java.util.List<org.apache.ambari.server.actionmanager.HostRoleCommand>> stage2 = stages.get(1);
        junit.framework.Assert.assertEquals(2, stage1.size());
        junit.framework.Assert.assertEquals(1, stage2.size());
        java.util.List<org.apache.ambari.server.actionmanager.HostRoleCommand> stage1CommandsHost1 = stage1.get("c6401");
        java.util.List<org.apache.ambari.server.actionmanager.HostRoleCommand> stage1CommandsHost3 = stage1.get("c6403");
        java.util.List<org.apache.ambari.server.actionmanager.HostRoleCommand> stage2CommandsHost2 = stage2.get("c6402");
        junit.framework.Assert.assertEquals(3, stage1CommandsHost1.size() + stage1CommandsHost3.size());
        junit.framework.Assert.assertEquals(1, stage2CommandsHost2.size());
        java.util.List<org.apache.ambari.server.Role> stage1Roles = com.google.common.collect.Lists.newArrayList(stage1CommandsHost1.get(0).getRole(), stage1CommandsHost1.get(1).getRole(), stage1CommandsHost3.get(0).getRole());
        junit.framework.Assert.assertTrue(stage1Roles.contains(org.apache.ambari.server.Role.NAMENODE));
        junit.framework.Assert.assertTrue(stage1Roles.contains(org.apache.ambari.server.Role.ZOOKEEPER_SERVER));
        junit.framework.Assert.assertEquals(org.apache.ambari.server.Role.ZOOKEEPER_SERVER, stage1CommandsHost3.get(0).getRole());
        junit.framework.Assert.assertEquals(org.apache.ambari.server.Role.HBASE_MASTER, stage2CommandsHost2.get(0).getRole());
    }
}