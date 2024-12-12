package org.apache.ambari.msi;
import static org.easymock.EasyMock.*;
public class ClusterDefinitionTest {
    @org.junit.Test
    public void testGetServices() throws java.lang.Exception {
        org.apache.ambari.msi.ClusterDefinition clusterDefinition = new org.apache.ambari.msi.ClusterDefinition(new org.apache.ambari.msi.TestStateProvider(), new org.apache.ambari.scom.TestClusterDefinitionProvider(), new org.apache.ambari.scom.TestHostInfoProvider());
        java.util.Set<java.lang.String> services = clusterDefinition.getServices();
        org.junit.Assert.assertTrue(services.contains("HDFS"));
        org.junit.Assert.assertTrue(services.contains("FLUME"));
        org.junit.Assert.assertTrue(services.contains("OOZIE"));
        org.junit.Assert.assertTrue(services.contains("MAPREDUCE"));
        org.junit.Assert.assertTrue(services.contains("HBASE"));
        org.junit.Assert.assertTrue(services.contains("ZOOKEEPER"));
        org.junit.Assert.assertTrue(services.contains("HIVE"));
    }

    @org.junit.Test
    public void testGetHosts() throws java.lang.Exception {
        org.apache.ambari.msi.ClusterDefinition clusterDefinition = new org.apache.ambari.msi.ClusterDefinition(new org.apache.ambari.msi.TestStateProvider(), new org.apache.ambari.scom.TestClusterDefinitionProvider(), new org.apache.ambari.scom.TestHostInfoProvider());
        java.util.Set<java.lang.String> hosts = clusterDefinition.getHosts();
        org.junit.Assert.assertTrue(hosts.contains("NAMENODE_MASTER.acme.com"));
        org.junit.Assert.assertTrue(hosts.contains("SECONDARY_NAMENODE_MASTER.acme.com"));
        org.junit.Assert.assertTrue(hosts.contains("FLUME_SERVICE1.acme.com"));
        org.junit.Assert.assertTrue(hosts.contains("FLUME_SERVICE2.acme.com"));
        org.junit.Assert.assertTrue(hosts.contains("FLUME_SERVICE3.acme.com"));
        org.junit.Assert.assertTrue(hosts.contains("HBASE_MASTER.acme.com"));
        org.junit.Assert.assertTrue(hosts.contains("HIVE_SERVER_MASTER.acme.com"));
        org.junit.Assert.assertTrue(hosts.contains("JOBTRACKER_MASTER.acme.com"));
        org.junit.Assert.assertTrue(hosts.contains("OOZIE_SERVER_MASTER.acme.com"));
        org.junit.Assert.assertTrue(hosts.contains("slave1.acme.com"));
        org.junit.Assert.assertTrue(hosts.contains("slave2.acme.com"));
        org.junit.Assert.assertTrue(hosts.contains("slave3.acme.com"));
        org.junit.Assert.assertTrue(hosts.contains("WEBHCAT_MASTER.acme.com"));
    }

    @org.junit.Test
    public void testGetComponents() throws java.lang.Exception {
        org.apache.ambari.msi.ClusterDefinition clusterDefinition = new org.apache.ambari.msi.ClusterDefinition(new org.apache.ambari.msi.TestStateProvider(), new org.apache.ambari.scom.TestClusterDefinitionProvider(), new org.apache.ambari.scom.TestHostInfoProvider());
        java.util.Set<java.lang.String> components = clusterDefinition.getComponents("HDFS");
        org.junit.Assert.assertTrue(components.contains("NAMENODE"));
        org.junit.Assert.assertTrue(components.contains("SECONDARY_NAMENODE"));
        org.junit.Assert.assertTrue(components.contains("DATANODE"));
        components = clusterDefinition.getComponents("MAPREDUCE");
        org.junit.Assert.assertTrue(components.contains("JOBTRACKER"));
        org.junit.Assert.assertTrue(components.contains("TASKTRACKER"));
        components = clusterDefinition.getComponents("FLUME");
        org.junit.Assert.assertTrue(components.contains("FLUME_SERVER"));
        components = clusterDefinition.getComponents("OOZIE");
        org.junit.Assert.assertTrue(components.contains("OOZIE_SERVER"));
        components = clusterDefinition.getComponents("HBASE");
        org.junit.Assert.assertTrue(components.contains("HBASE_MASTER"));
        org.junit.Assert.assertTrue(components.contains("HBASE_REGIONSERVER"));
        components = clusterDefinition.getComponents("ZOOKEEPER");
        org.junit.Assert.assertTrue(components.contains("ZOOKEEPER_SERVER"));
        components = clusterDefinition.getComponents("HIVE");
        org.junit.Assert.assertTrue(components.contains("HIVE_SERVER"));
        clusterDefinition = new org.apache.ambari.msi.ClusterDefinition(new org.apache.ambari.msi.TestStateProvider(), new org.apache.ambari.scom.TestClusterDefinitionProvider("clusterproperties_HDP2_HA.txt", "myCluster", "HDP-2.0.6"), new org.apache.ambari.scom.TestHostInfoProvider());
        components = clusterDefinition.getComponents("HDFS");
        org.junit.Assert.assertTrue(components.contains("NAMENODE"));
        org.junit.Assert.assertTrue(components.contains("SECONDARY_NAMENODE"));
        org.junit.Assert.assertTrue(components.contains("DATANODE"));
        org.junit.Assert.assertTrue(components.contains("ZKFC"));
        org.junit.Assert.assertTrue(components.contains("JOURNALNODE"));
        clusterDefinition = new org.apache.ambari.msi.ClusterDefinition(new org.apache.ambari.msi.TestStateProvider(), new org.apache.ambari.scom.TestClusterDefinitionProvider("clusterproperties_HDP21_HA.txt", "myCluster", "HDP-2.1.2"), new org.apache.ambari.scom.TestHostInfoProvider());
        components = clusterDefinition.getComponents("HDFS");
        org.junit.Assert.assertTrue(components.contains("NAMENODE"));
        org.junit.Assert.assertTrue(components.contains("ZKFC"));
        org.junit.Assert.assertTrue(components.contains("JOURNALNODE"));
        components = clusterDefinition.getComponents("YARN");
        org.junit.Assert.assertTrue(components.contains("RESOURCEMANAGER"));
    }

    @org.junit.Test
    public void testGetHostComponents() throws java.lang.Exception {
        org.apache.ambari.msi.ClusterDefinition clusterDefinition = new org.apache.ambari.msi.ClusterDefinition(new org.apache.ambari.msi.TestStateProvider(), new org.apache.ambari.scom.TestClusterDefinitionProvider(), new org.apache.ambari.scom.TestHostInfoProvider());
        java.util.Set<java.lang.String> hostComponents = clusterDefinition.getHostComponents("HDFS", "NAMENODE_MASTER.acme.com");
        org.junit.Assert.assertTrue(hostComponents.contains("NAMENODE"));
        hostComponents = clusterDefinition.getHostComponents("HDFS", "slave1.acme.com");
        org.junit.Assert.assertTrue(hostComponents.contains("DATANODE"));
        hostComponents = clusterDefinition.getHostComponents("HDFS", "slave2.acme.com");
        org.junit.Assert.assertTrue(hostComponents.contains("DATANODE"));
        clusterDefinition = new org.apache.ambari.msi.ClusterDefinition(new org.apache.ambari.msi.TestStateProvider(), new org.apache.ambari.scom.TestClusterDefinitionProvider("clusterproperties_HDP2_HA.txt", "myCluster", "HDP-2.0.6"), new org.apache.ambari.scom.TestHostInfoProvider());
        hostComponents = clusterDefinition.getHostComponents("HDFS", "WINHDP-1");
        org.junit.Assert.assertTrue(hostComponents.contains("NAMENODE"));
        org.junit.Assert.assertTrue(hostComponents.contains("JOURNALNODE"));
        org.junit.Assert.assertTrue(hostComponents.contains("ZKFC"));
        org.junit.Assert.assertFalse(hostComponents.contains("DATANODE"));
        org.junit.Assert.assertFalse(hostComponents.contains("SECONDARY_NAMENODE"));
        hostComponents = clusterDefinition.getHostComponents("HDFS", "WINHDP-2");
        org.junit.Assert.assertTrue(hostComponents.contains("NAMENODE"));
        org.junit.Assert.assertTrue(hostComponents.contains("JOURNALNODE"));
        org.junit.Assert.assertTrue(hostComponents.contains("ZKFC"));
        org.junit.Assert.assertTrue(hostComponents.contains("DATANODE"));
        org.junit.Assert.assertTrue(hostComponents.contains("SECONDARY_NAMENODE"));
        clusterDefinition = new org.apache.ambari.msi.ClusterDefinition(new org.apache.ambari.msi.TestStateProvider(), new org.apache.ambari.scom.TestClusterDefinitionProvider("clusterproperties_HDP21_HA.txt", "myCluster", "HDP-2.1.2"), new org.apache.ambari.scom.TestHostInfoProvider());
        hostComponents = clusterDefinition.getHostComponents("YARN", "WINHDP-1");
        org.junit.Assert.assertTrue(hostComponents.contains("RESOURCEMANAGER"));
        hostComponents = clusterDefinition.getHostComponents("HDFS", "WINHDP-1");
        org.junit.Assert.assertTrue(hostComponents.contains("NAMENODE"));
        org.junit.Assert.assertTrue(hostComponents.contains("JOURNALNODE"));
        hostComponents = clusterDefinition.getHostComponents("YARN", "WINHDP-2");
        org.junit.Assert.assertTrue(hostComponents.contains("RESOURCEMANAGER"));
        hostComponents = clusterDefinition.getHostComponents("HDFS", "WINHDP-2");
        org.junit.Assert.assertTrue(hostComponents.contains("NAMENODE"));
        org.junit.Assert.assertTrue(hostComponents.contains("JOURNALNODE"));
    }

    @org.junit.Test
    public void testGetHostState() throws java.lang.Exception {
        org.apache.ambari.msi.TestStateProvider stateProvider = new org.apache.ambari.msi.TestStateProvider();
        org.apache.ambari.scom.TestClusterDefinitionProvider definitionProvider = new org.apache.ambari.scom.TestClusterDefinitionProvider();
        org.apache.ambari.scom.TestHostInfoProvider hostInfoProvider = new org.apache.ambari.scom.TestHostInfoProvider();
        org.apache.ambari.msi.ClusterDefinition clusterDefinition = new org.apache.ambari.msi.ClusterDefinition(stateProvider, definitionProvider, hostInfoProvider);
        org.junit.Assert.assertEquals("HEALTHY", clusterDefinition.getHostState("NAMENODE_MASTER.acme.com"));
        stateProvider.setState(org.apache.ambari.msi.StateProvider.State.Stopped);
        org.junit.Assert.assertEquals("UNHEALTHY", clusterDefinition.getHostState("NAMENODE_MASTER.acme.com"));
        stateProvider.setState(org.apache.ambari.msi.StateProvider.State.Paused);
        org.junit.Assert.assertEquals("UNHEALTHY", clusterDefinition.getHostState("NAMENODE_MASTER.acme.com"));
        stateProvider.setState(org.apache.ambari.msi.StateProvider.State.Unknown);
        org.junit.Assert.assertEquals("UNHEALTHY", clusterDefinition.getHostState("NAMENODE_MASTER.acme.com"));
    }

    @org.junit.Test
    public void testSetServiceState_IfStateAlreadySetToDesired() {
        org.apache.ambari.msi.StateProvider mockStateProvider = createStrictMock(org.apache.ambari.msi.StateProvider.class);
        org.apache.ambari.scom.ClusterDefinitionProvider mockClusterDefinitionProvider = createStrictMock(org.apache.ambari.scom.ClusterDefinitionProvider.class);
        org.apache.ambari.scom.HostInfoProvider mockHostInfoProvider = createStrictMock(org.apache.ambari.scom.HostInfoProvider.class);
        org.apache.ambari.scom.TestClusterDefinitionProvider testClusterDefinitionProvider = new org.apache.ambari.scom.TestClusterDefinitionProvider();
        expect(mockClusterDefinitionProvider.getClusterName()).andDelegateTo(testClusterDefinitionProvider);
        expect(mockClusterDefinitionProvider.getVersionId()).andDelegateTo(testClusterDefinitionProvider);
        expect(mockClusterDefinitionProvider.getInputStream()).andDelegateTo(testClusterDefinitionProvider);
        expect(mockStateProvider.getRunningState(anyObject(java.lang.String.class), anyObject(java.lang.String.class))).andReturn(org.apache.ambari.msi.StateProvider.State.Running).times(5);
        replay(mockClusterDefinitionProvider, mockHostInfoProvider, mockStateProvider);
        org.apache.ambari.msi.ClusterDefinition clusterDefinition = new org.apache.ambari.msi.ClusterDefinition(mockStateProvider, mockClusterDefinitionProvider, mockHostInfoProvider);
        org.junit.Assert.assertEquals(-1, clusterDefinition.setServiceState("HDFS", "STARTED"));
        verify(mockClusterDefinitionProvider, mockHostInfoProvider, mockStateProvider);
    }

    @org.junit.Test
    public void testSetServiceState_IfStateUnknown() {
        org.apache.ambari.msi.StateProvider mockStateProvider = createStrictMock(org.apache.ambari.msi.StateProvider.class);
        org.apache.ambari.scom.ClusterDefinitionProvider mockClusterDefinitionProvider = createStrictMock(org.apache.ambari.scom.ClusterDefinitionProvider.class);
        org.apache.ambari.scom.HostInfoProvider mockHostInfoProvider = createStrictMock(org.apache.ambari.scom.HostInfoProvider.class);
        org.apache.ambari.scom.TestClusterDefinitionProvider testClusterDefinitionProvider = new org.apache.ambari.scom.TestClusterDefinitionProvider();
        expect(mockClusterDefinitionProvider.getClusterName()).andDelegateTo(testClusterDefinitionProvider);
        expect(mockClusterDefinitionProvider.getVersionId()).andDelegateTo(testClusterDefinitionProvider);
        expect(mockClusterDefinitionProvider.getInputStream()).andDelegateTo(testClusterDefinitionProvider);
        replay(mockClusterDefinitionProvider, mockHostInfoProvider, mockStateProvider);
        org.apache.ambari.msi.ClusterDefinition clusterDefinition = new org.apache.ambari.msi.ClusterDefinition(mockStateProvider, mockClusterDefinitionProvider, mockHostInfoProvider);
        org.junit.Assert.assertEquals(-1, clusterDefinition.setServiceState("HDFS", "UNKNOWN"));
        verify(mockClusterDefinitionProvider, mockHostInfoProvider, mockStateProvider);
    }

    @org.junit.Test
    public void testSetServiceState_FromInstalledToStarted() {
        org.apache.ambari.msi.StateProvider mockStateProvider = createStrictMock(org.apache.ambari.msi.StateProvider.class);
        org.apache.ambari.scom.ClusterDefinitionProvider mockClusterDefinitionProvider = createStrictMock(org.apache.ambari.scom.ClusterDefinitionProvider.class);
        org.apache.ambari.scom.HostInfoProvider mockHostInfoProvider = createStrictMock(org.apache.ambari.scom.HostInfoProvider.class);
        org.apache.ambari.scom.TestClusterDefinitionProvider testClusterDefinitionProvider = new org.apache.ambari.scom.TestClusterDefinitionProvider();
        expect(mockClusterDefinitionProvider.getClusterName()).andDelegateTo(testClusterDefinitionProvider);
        expect(mockClusterDefinitionProvider.getVersionId()).andDelegateTo(testClusterDefinitionProvider);
        expect(mockClusterDefinitionProvider.getInputStream()).andDelegateTo(testClusterDefinitionProvider);
        expect(mockStateProvider.getRunningState(anyObject(java.lang.String.class), anyObject(java.lang.String.class))).andReturn(org.apache.ambari.msi.StateProvider.State.Stopped);
        expect(mockStateProvider.getRunningState(anyObject(java.lang.String.class), anyObject(java.lang.String.class))).andReturn(org.apache.ambari.msi.StateProvider.State.Stopped);
        expect(mockStateProvider.setRunningState(anyObject(java.lang.String.class), anyObject(java.lang.String.class), eq(org.apache.ambari.msi.StateProvider.State.Running))).andReturn(null);
        expect(mockStateProvider.getRunningState(anyObject(java.lang.String.class), anyObject(java.lang.String.class))).andReturn(org.apache.ambari.msi.StateProvider.State.Stopped);
        expect(mockStateProvider.setRunningState(anyObject(java.lang.String.class), anyObject(java.lang.String.class), eq(org.apache.ambari.msi.StateProvider.State.Running))).andReturn(null);
        expect(mockStateProvider.getRunningState(anyObject(java.lang.String.class), anyObject(java.lang.String.class))).andReturn(org.apache.ambari.msi.StateProvider.State.Stopped);
        expect(mockStateProvider.setRunningState(anyObject(java.lang.String.class), anyObject(java.lang.String.class), eq(org.apache.ambari.msi.StateProvider.State.Running))).andReturn(null);
        expect(mockStateProvider.getRunningState(anyObject(java.lang.String.class), anyObject(java.lang.String.class))).andReturn(org.apache.ambari.msi.StateProvider.State.Stopped);
        expect(mockStateProvider.setRunningState(anyObject(java.lang.String.class), anyObject(java.lang.String.class), eq(org.apache.ambari.msi.StateProvider.State.Running))).andReturn(null);
        expect(mockStateProvider.getRunningState(anyObject(java.lang.String.class), anyObject(java.lang.String.class))).andReturn(org.apache.ambari.msi.StateProvider.State.Stopped);
        expect(mockStateProvider.setRunningState(anyObject(java.lang.String.class), anyObject(java.lang.String.class), eq(org.apache.ambari.msi.StateProvider.State.Running))).andReturn(null);
        replay(mockClusterDefinitionProvider, mockHostInfoProvider, mockStateProvider);
        org.apache.ambari.msi.ClusterDefinition clusterDefinition = new org.apache.ambari.msi.ClusterDefinition(mockStateProvider, mockClusterDefinitionProvider, mockHostInfoProvider);
        org.junit.Assert.assertEquals(1, clusterDefinition.setServiceState("HDFS", "STARTED"));
        verify(mockClusterDefinitionProvider, mockHostInfoProvider, mockStateProvider);
    }

    @org.junit.Test
    public void testSetServiceStateFromInstalledToStartedWhenOneOfTheComponentsAlreadyStarted() {
        org.apache.ambari.msi.StateProvider mockStateProvider = createStrictMock(org.apache.ambari.msi.StateProvider.class);
        org.apache.ambari.scom.ClusterDefinitionProvider mockClusterDefinitionProvider = createStrictMock(org.apache.ambari.scom.ClusterDefinitionProvider.class);
        org.apache.ambari.scom.HostInfoProvider mockHostInfoProvider = createStrictMock(org.apache.ambari.scom.HostInfoProvider.class);
        org.apache.ambari.scom.TestClusterDefinitionProvider testClusterDefinitionProvider = new org.apache.ambari.scom.TestClusterDefinitionProvider();
        expect(mockClusterDefinitionProvider.getClusterName()).andDelegateTo(testClusterDefinitionProvider);
        expect(mockClusterDefinitionProvider.getVersionId()).andDelegateTo(testClusterDefinitionProvider);
        expect(mockClusterDefinitionProvider.getInputStream()).andDelegateTo(testClusterDefinitionProvider);
        expect(mockStateProvider.getRunningState(anyObject(java.lang.String.class), anyObject(java.lang.String.class))).andReturn(org.apache.ambari.msi.StateProvider.State.Stopped);
        expect(mockStateProvider.getRunningState(anyObject(java.lang.String.class), anyObject(java.lang.String.class))).andReturn(org.apache.ambari.msi.StateProvider.State.Running).times(4);
        expect(mockStateProvider.getRunningState(anyObject(java.lang.String.class), anyObject(java.lang.String.class))).andReturn(org.apache.ambari.msi.StateProvider.State.Stopped);
        expect(mockStateProvider.setRunningState(anyObject(java.lang.String.class), anyObject(java.lang.String.class), eq(org.apache.ambari.msi.StateProvider.State.Running))).andReturn(null);
        replay(mockClusterDefinitionProvider, mockHostInfoProvider, mockStateProvider);
        org.apache.ambari.msi.ClusterDefinition clusterDefinition = new org.apache.ambari.msi.ClusterDefinition(mockStateProvider, mockClusterDefinitionProvider, mockHostInfoProvider);
        org.junit.Assert.assertEquals(1, clusterDefinition.setServiceState("HDFS", "STARTED"));
        verify(mockClusterDefinitionProvider, mockHostInfoProvider, mockStateProvider);
    }

    @org.junit.Test
    public void testSetHostComponentState_IfStateUnknown() {
        org.apache.ambari.msi.StateProvider mockStateProvider = createStrictMock(org.apache.ambari.msi.StateProvider.class);
        org.apache.ambari.scom.ClusterDefinitionProvider mockClusterDefinitionProvider = createStrictMock(org.apache.ambari.scom.ClusterDefinitionProvider.class);
        org.apache.ambari.scom.HostInfoProvider mockHostInfoProvider = createStrictMock(org.apache.ambari.scom.HostInfoProvider.class);
        org.apache.ambari.scom.TestClusterDefinitionProvider testClusterDefinitionProvider = new org.apache.ambari.scom.TestClusterDefinitionProvider();
        expect(mockClusterDefinitionProvider.getClusterName()).andDelegateTo(testClusterDefinitionProvider);
        expect(mockClusterDefinitionProvider.getVersionId()).andDelegateTo(testClusterDefinitionProvider);
        expect(mockClusterDefinitionProvider.getInputStream()).andDelegateTo(testClusterDefinitionProvider);
        replay(mockClusterDefinitionProvider, mockHostInfoProvider, mockStateProvider);
        org.apache.ambari.msi.ClusterDefinition clusterDefinition = new org.apache.ambari.msi.ClusterDefinition(mockStateProvider, mockClusterDefinitionProvider, mockHostInfoProvider);
        org.junit.Assert.assertEquals(-1, clusterDefinition.setHostComponentState("hostName", "DATANODE", "UNKNOWN"));
        verify(mockClusterDefinitionProvider, mockHostInfoProvider, mockStateProvider);
    }

    @org.junit.Test
    public void testSetHostComponentState_IfStateAlreadySetToDesired() {
        org.apache.ambari.msi.StateProvider mockStateProvider = createStrictMock(org.apache.ambari.msi.StateProvider.class);
        org.apache.ambari.scom.ClusterDefinitionProvider mockClusterDefinitionProvider = createStrictMock(org.apache.ambari.scom.ClusterDefinitionProvider.class);
        org.apache.ambari.scom.HostInfoProvider mockHostInfoProvider = createStrictMock(org.apache.ambari.scom.HostInfoProvider.class);
        org.apache.ambari.scom.TestClusterDefinitionProvider testClusterDefinitionProvider = new org.apache.ambari.scom.TestClusterDefinitionProvider();
        expect(mockClusterDefinitionProvider.getClusterName()).andDelegateTo(testClusterDefinitionProvider);
        expect(mockClusterDefinitionProvider.getVersionId()).andDelegateTo(testClusterDefinitionProvider);
        expect(mockClusterDefinitionProvider.getInputStream()).andDelegateTo(testClusterDefinitionProvider);
        expect(mockStateProvider.getRunningState(isA(java.lang.String.class), isA(java.lang.String.class))).andReturn(org.apache.ambari.msi.StateProvider.State.Running);
        replay(mockClusterDefinitionProvider, mockHostInfoProvider, mockStateProvider);
        org.apache.ambari.msi.ClusterDefinition clusterDefinition = new org.apache.ambari.msi.ClusterDefinition(mockStateProvider, mockClusterDefinitionProvider, mockHostInfoProvider);
        org.junit.Assert.assertEquals(-1, clusterDefinition.setHostComponentState("hostName", "DATANODE", "STARTED"));
        verify(mockClusterDefinitionProvider, mockHostInfoProvider, mockStateProvider);
    }

    @org.junit.Test
    public void testSetHostComponentState_FromInstalledToStarted() {
        org.apache.ambari.msi.StateProvider mockStateProvider = createStrictMock(org.apache.ambari.msi.StateProvider.class);
        org.apache.ambari.scom.ClusterDefinitionProvider mockClusterDefinitionProvider = createStrictMock(org.apache.ambari.scom.ClusterDefinitionProvider.class);
        org.apache.ambari.scom.HostInfoProvider mockHostInfoProvider = createStrictMock(org.apache.ambari.scom.HostInfoProvider.class);
        org.apache.ambari.scom.TestClusterDefinitionProvider testClusterDefinitionProvider = new org.apache.ambari.scom.TestClusterDefinitionProvider();
        expect(mockClusterDefinitionProvider.getClusterName()).andDelegateTo(testClusterDefinitionProvider);
        expect(mockClusterDefinitionProvider.getVersionId()).andDelegateTo(testClusterDefinitionProvider);
        expect(mockClusterDefinitionProvider.getInputStream()).andDelegateTo(testClusterDefinitionProvider);
        expect(mockStateProvider.getRunningState(isA(java.lang.String.class), isA(java.lang.String.class))).andReturn(org.apache.ambari.msi.StateProvider.State.Stopped);
        expect(mockStateProvider.setRunningState(anyObject(java.lang.String.class), anyObject(java.lang.String.class), eq(org.apache.ambari.msi.StateProvider.State.Running))).andReturn(null);
        replay(mockClusterDefinitionProvider, mockHostInfoProvider, mockStateProvider);
        org.apache.ambari.msi.ClusterDefinition clusterDefinition = new org.apache.ambari.msi.ClusterDefinition(mockStateProvider, mockClusterDefinitionProvider, mockHostInfoProvider);
        org.junit.Assert.assertEquals(1, clusterDefinition.setHostComponentState("hostName", "DATANODE", "STARTED"));
        verify(mockClusterDefinitionProvider, mockHostInfoProvider, mockStateProvider);
    }

    @org.junit.Test
    public void testHDP2ServicesAndComponents() {
        org.apache.ambari.msi.StateProvider mockStateProvider = createStrictMock(org.apache.ambari.msi.StateProvider.class);
        org.apache.ambari.scom.ClusterDefinitionProvider mockClusterDefinitionProvider = createStrictMock(org.apache.ambari.scom.ClusterDefinitionProvider.class);
        org.apache.ambari.scom.HostInfoProvider mockHostInfoProvider = createStrictMock(org.apache.ambari.scom.HostInfoProvider.class);
        org.apache.ambari.scom.TestClusterDefinitionProvider testClusterDefinitionProvider = new org.apache.ambari.scom.TestClusterDefinitionProvider("clusterproperties_HDP2.txt", "myCluster", "HDP-2.0.6");
        expect(mockClusterDefinitionProvider.getClusterName()).andDelegateTo(testClusterDefinitionProvider);
        expect(mockClusterDefinitionProvider.getVersionId()).andDelegateTo(testClusterDefinitionProvider);
        expect(mockClusterDefinitionProvider.getInputStream()).andDelegateTo(testClusterDefinitionProvider);
        replay(mockClusterDefinitionProvider, mockHostInfoProvider, mockStateProvider);
        org.apache.ambari.msi.ClusterDefinition clusterDefinition = new org.apache.ambari.msi.ClusterDefinition(mockStateProvider, mockClusterDefinitionProvider, mockHostInfoProvider);
        org.junit.Assert.assertFalse(clusterDefinition.getServices().contains("MAPREDUCE"));
        org.junit.Assert.assertTrue(clusterDefinition.getServices().contains("PIG"));
        org.junit.Assert.assertTrue(clusterDefinition.getServices().contains("SQOOP"));
        org.junit.Assert.assertTrue(clusterDefinition.getServices().contains("YARN"));
        org.junit.Assert.assertTrue(clusterDefinition.getServices().contains("MAPREDUCE2"));
        org.junit.Assert.assertTrue(clusterDefinition.getComponents("MAPREDUCE2").contains("MAPREDUCE2_CLIENT"));
        org.junit.Assert.assertTrue(clusterDefinition.getComponents("YARN").contains("NODEMANAGER"));
        org.junit.Assert.assertTrue(clusterDefinition.getComponents("YARN").contains("RESOURCEMANAGER"));
        org.junit.Assert.assertTrue(clusterDefinition.getComponents("YARN").contains("YARN_CLIENT"));
        verify(mockClusterDefinitionProvider, mockHostInfoProvider, mockStateProvider);
    }

    @org.junit.Test
    public void testSetServiceState_IfServiceIsClientOnly() {
        org.apache.ambari.msi.StateProvider mockStateProvider = createStrictMock(org.apache.ambari.msi.StateProvider.class);
        org.apache.ambari.scom.ClusterDefinitionProvider mockClusterDefinitionProvider = createStrictMock(org.apache.ambari.scom.ClusterDefinitionProvider.class);
        org.apache.ambari.scom.HostInfoProvider mockHostInfoProvider = createStrictMock(org.apache.ambari.scom.HostInfoProvider.class);
        org.apache.ambari.scom.TestClusterDefinitionProvider testClusterDefinitionProvider = new org.apache.ambari.scom.TestClusterDefinitionProvider();
        expect(mockClusterDefinitionProvider.getClusterName()).andDelegateTo(testClusterDefinitionProvider);
        expect(mockClusterDefinitionProvider.getVersionId()).andDelegateTo(testClusterDefinitionProvider);
        expect(mockClusterDefinitionProvider.getInputStream()).andDelegateTo(testClusterDefinitionProvider);
        replay(mockClusterDefinitionProvider, mockHostInfoProvider, mockStateProvider);
        org.apache.ambari.msi.ClusterDefinition clusterDefinition = new org.apache.ambari.msi.ClusterDefinition(mockStateProvider, mockClusterDefinitionProvider, mockHostInfoProvider);
        org.junit.Assert.assertEquals(-1, clusterDefinition.setServiceState("PIG", "STARTED"));
        verify(mockClusterDefinitionProvider, mockHostInfoProvider, mockStateProvider);
    }

    @org.junit.Test
    public void testSetHostComponentState_IfHostComponentIsClientOnly() {
        org.apache.ambari.msi.StateProvider mockStateProvider = createStrictMock(org.apache.ambari.msi.StateProvider.class);
        org.apache.ambari.scom.ClusterDefinitionProvider mockClusterDefinitionProvider = createStrictMock(org.apache.ambari.scom.ClusterDefinitionProvider.class);
        org.apache.ambari.scom.HostInfoProvider mockHostInfoProvider = createStrictMock(org.apache.ambari.scom.HostInfoProvider.class);
        org.apache.ambari.scom.TestClusterDefinitionProvider testClusterDefinitionProvider = new org.apache.ambari.scom.TestClusterDefinitionProvider();
        expect(mockClusterDefinitionProvider.getClusterName()).andDelegateTo(testClusterDefinitionProvider);
        expect(mockClusterDefinitionProvider.getVersionId()).andDelegateTo(testClusterDefinitionProvider);
        expect(mockClusterDefinitionProvider.getInputStream()).andDelegateTo(testClusterDefinitionProvider);
        replay(mockClusterDefinitionProvider, mockHostInfoProvider, mockStateProvider);
        org.apache.ambari.msi.ClusterDefinition clusterDefinition = new org.apache.ambari.msi.ClusterDefinition(mockStateProvider, mockClusterDefinitionProvider, mockHostInfoProvider);
        org.junit.Assert.assertEquals(-1, clusterDefinition.setHostComponentState("hostName", "SQOOP", "STARTED"));
        verify(mockClusterDefinitionProvider, mockHostInfoProvider, mockStateProvider);
    }

    @org.junit.Test
    public void testGetMajorStackVersion() {
        org.apache.ambari.scom.TestClusterDefinitionProvider testClusterDefinitionProvider = new org.apache.ambari.scom.TestClusterDefinitionProvider("clusterproperties_HDP2.txt", "myCluster", "HDP-2.0.6");
        org.apache.ambari.msi.ClusterDefinition clusterDefinition = new org.apache.ambari.msi.ClusterDefinition(new org.apache.ambari.msi.TestStateProvider(), testClusterDefinitionProvider, new org.apache.ambari.scom.TestHostInfoProvider());
        java.lang.Integer majorVersion = clusterDefinition.getMajorStackVersion();
        java.lang.Integer minorVersion = clusterDefinition.getMinorStackVersion();
        org.junit.Assert.assertTrue(2 == majorVersion);
        org.junit.Assert.assertTrue(0 == minorVersion);
    }
}