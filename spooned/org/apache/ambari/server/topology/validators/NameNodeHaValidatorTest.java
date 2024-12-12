package org.apache.ambari.server.topology.validators;
import org.easymock.EasyMockRunner;
import org.easymock.Mock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
@org.junit.runner.RunWith(org.easymock.EasyMockRunner.class)
public class NameNodeHaValidatorTest {
    java.util.Set<java.lang.String> hosts = new java.util.HashSet<>();

    java.util.Set<java.lang.String> nameNodes = new java.util.HashSet<>();

    java.util.Map<java.lang.String, org.apache.ambari.server.topology.HostGroupInfo> hostGroupInfos = new java.util.HashMap<>();

    java.util.Map<java.lang.String, java.lang.String> hdfsSite = new java.util.HashMap<>();

    java.util.Map<java.lang.String, java.lang.String> hadoopEnv = new java.util.HashMap<>();

    java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> fullProperties = com.google.common.collect.ImmutableMap.of("hdfs-site", hdfsSite, "hadoop-env", hadoopEnv);

    org.apache.ambari.server.topology.validators.NameNodeHaValidator validator = new org.apache.ambari.server.topology.validators.NameNodeHaValidator();

    @org.easymock.Mock
    org.apache.ambari.server.topology.Configuration clusterConfig;

    @org.easymock.Mock
    org.apache.ambari.server.topology.ClusterTopology clusterTopology;

    @org.junit.Before
    public void setUp() {
        EasyMock.expect(clusterTopology.getConfiguration()).andReturn(clusterConfig).anyTimes();
        EasyMock.expect(clusterTopology.getAllHosts()).andReturn(hosts).anyTimes();
        EasyMock.expect(clusterTopology.getHostAssignmentsForComponent("NAMENODE")).andReturn(nameNodes).anyTimes();
        EasyMock.expect(clusterTopology.isNameNodeHAEnabled()).andAnswer(() -> org.apache.ambari.server.topology.ClusterTopologyImpl.isNameNodeHAEnabled(fullProperties)).anyTimes();
        EasyMock.expect(clusterTopology.getHostGroupInfo()).andReturn(hostGroupInfos).anyTimes();
        EasyMock.expect(clusterConfig.getFullProperties()).andReturn(fullProperties).anyTimes();
        EasyMock.replay(clusterTopology, clusterConfig);
        createDefaultHdfsSite();
        hosts.addAll(com.google.common.collect.ImmutableSet.of("internalhost1", "internalhost2"));
    }

    private void createDefaultHdfsSite() {
        hdfsSite.put("dfs.nameservices", "demo1, demo2");
        hdfsSite.put("dfs.ha.namenodes.demo1", "nn1, nn2");
        hdfsSite.put("dfs.ha.namenodes.demo2", "nn1, nn2");
    }

    private void addExternalNameNodesToHdfsSite() {
        hdfsSite.put("dfs.namenode.rpc-address.demo1.nn1", "demohost1:8020");
        hdfsSite.put("dfs.namenode.rpc-address.demo1.nn2", "demohost2:8020");
        hdfsSite.put("dfs.namenode.rpc-address.demo2.nn1", "demohost3:8020");
        hdfsSite.put("dfs.namenode.rpc-address.demo2.nn2", "demohost4:8020");
    }

    @org.junit.Test
    public void nonHA() throws java.lang.Exception {
        hdfsSite.clear();
        validator.validate(clusterTopology);
    }

    @org.junit.Test
    public void externalNameNodes_properConfig1() throws java.lang.Exception {
        addExternalNameNodesToHdfsSite();
        validator.validate(clusterTopology);
    }

    @org.junit.Test
    public void externalNameNodes_properConfig2() throws java.lang.Exception {
        addExternalNameNodesToHdfsSite();
        hdfsSite.remove("dfs.nameservices");
        hdfsSite.put("dfs.internal.nameservices", "demo1, demo2");
        validator.validate(clusterTopology);
    }

    @org.junit.Test(expected = org.apache.ambari.server.topology.InvalidTopologyException.class)
    public void externalNameNodes_missingNameNodes() throws java.lang.Exception {
        addExternalNameNodesToHdfsSite();
        hdfsSite.remove("dfs.ha.namenodes.demo2");
        validator.validate(clusterTopology);
    }

    @org.junit.Test(expected = org.apache.ambari.server.topology.InvalidTopologyException.class)
    public void externalNameNodes_missingRpcAddress() throws java.lang.Exception {
        addExternalNameNodesToHdfsSite();
        hdfsSite.remove("dfs.namenode.rpc-address.demo2.nn1");
        validator.validate(clusterTopology);
    }

    @org.junit.Test(expected = org.apache.ambari.server.topology.InvalidTopologyException.class)
    public void externalNameNodes_localhost() throws java.lang.Exception {
        addExternalNameNodesToHdfsSite();
        hdfsSite.put("dfs.namenode.rpc-address.demo2.nn1", "localhost:8020");
        validator.validate(clusterTopology);
    }

    @org.junit.Test(expected = org.apache.ambari.server.topology.InvalidTopologyException.class)
    public void externalNameNodes_hostGroupToken() throws java.lang.Exception {
        addExternalNameNodesToHdfsSite();
        hdfsSite.put("dfs.namenode.rpc-address.demo2.nn1", "%HOSTGROUP::group1%:8020");
        validator.validate(clusterTopology);
    }

    @org.junit.Test(expected = org.apache.ambari.server.topology.InvalidTopologyException.class)
    public void externalNameNodes_missingPort() throws java.lang.Exception {
        addExternalNameNodesToHdfsSite();
        hdfsSite.put("dfs.namenode.rpc-address.demo2.nn1", "demohost3");
        validator.validate(clusterTopology);
    }

    @org.junit.Test(expected = org.apache.ambari.server.topology.InvalidTopologyException.class)
    public void externalNameNodes_internalHost() throws java.lang.Exception {
        addExternalNameNodesToHdfsSite();
        hdfsSite.put("dfs.namenode.rpc-address.demo2.nn1", "internalhost1:8020");
        validator.validate(clusterTopology);
    }

    @org.junit.Test
    public void validationSkippedDueToMissingHostInformation() throws java.lang.Exception {
        nameNodes.add(hosts.iterator().next());
        org.apache.ambari.server.topology.HostGroupInfo groupInfo = new org.apache.ambari.server.topology.HostGroupInfo("group1");
        hostGroupInfos.put(groupInfo.getHostGroupName(), groupInfo);
        validator.validate(clusterTopology);
    }

    @org.junit.Test(expected = org.apache.ambari.server.topology.InvalidTopologyException.class)
    public void notEnoughNameNodesForHa() throws java.lang.Exception {
        nameNodes.add(hosts.iterator().next());
        validator.validate(clusterTopology);
    }

    @org.junit.Test
    public void haNoInitialSetup() throws java.lang.Exception {
        java.util.List<java.lang.String> nameNodeHosts = com.google.common.collect.ImmutableList.copyOf(hosts).subList(0, 2);
        nameNodes.addAll(nameNodeHosts);
        validator.validate(clusterTopology);
    }

    @org.junit.Test
    public void haProperInitialSetup() throws java.lang.Exception {
        java.util.List<java.lang.String> nameNodeHosts = com.google.common.collect.ImmutableList.copyOf(hosts).subList(0, 2);
        nameNodes.addAll(nameNodeHosts);
        hadoopEnv.put("dfs_ha_initial_namenode_active", nameNodeHosts.get(1));
        validator.validate(clusterTopology);
    }

    @org.junit.Test
    public void haProperInitialSetupWithHostGroups() throws java.lang.Exception {
        java.util.List<java.lang.String> nameNodeHosts = com.google.common.collect.ImmutableList.copyOf(hosts).subList(0, 2);
        nameNodes.addAll(nameNodeHosts);
        hadoopEnv.put("dfs_ha_initial_namenode_active", "%HOSTGROUP::group1%");
        hadoopEnv.put("dfs_ha_initial_namenode_standby", "%HOSTGROUP::group2%");
        validator.validate(clusterTopology);
    }

    @org.junit.Test(expected = org.apache.ambari.server.topology.InvalidTopologyException.class)
    public void haInvalidInitialActive() throws java.lang.Exception {
        java.util.List<java.lang.String> nameNodeHosts = com.google.common.collect.ImmutableList.copyOf(hosts).subList(0, 2);
        nameNodes.addAll(nameNodeHosts);
        hadoopEnv.put("dfs_ha_initial_namenode_active", "externalhost");
        hadoopEnv.put("dfs_ha_initial_namenode_standby", nameNodeHosts.get(0));
        validator.validate(clusterTopology);
    }

    @org.junit.Test(expected = org.apache.ambari.server.topology.InvalidTopologyException.class)
    public void haInvalidInitialStandby() throws java.lang.Exception {
        java.util.List<java.lang.String> nameNodeHosts = com.google.common.collect.ImmutableList.copyOf(hosts).subList(0, 2);
        nameNodes.addAll(nameNodeHosts);
        hadoopEnv.put("dfs_ha_initial_namenode_active", nameNodeHosts.get(0));
        hadoopEnv.put("dfs_ha_initial_namenode_standby", "externalhost");
        validator.validate(clusterTopology);
    }
}