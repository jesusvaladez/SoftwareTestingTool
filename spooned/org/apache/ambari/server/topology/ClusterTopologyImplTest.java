package org.apache.ambari.server.topology;
import static org.easymock.EasyMock.expect;
import static org.powermock.api.easymock.PowerMock.createNiceMock;
import static org.powermock.api.easymock.PowerMock.replay;
import static org.powermock.api.easymock.PowerMock.reset;
import static org.powermock.api.easymock.PowerMock.verify;
@java.lang.SuppressWarnings("unchecked")
public class ClusterTopologyImplTest {
    private static final java.lang.String CLUSTER_NAME = "cluster_name";

    private static final long CLUSTER_ID = 1L;

    private static final java.lang.String predicate = "Hosts/host_name=foo";

    private final org.apache.ambari.server.topology.Blueprint blueprint = createNiceMock(org.apache.ambari.server.topology.Blueprint.class);

    private final org.apache.ambari.server.topology.HostGroup group1 = createNiceMock(org.apache.ambari.server.topology.HostGroup.class);

    private final org.apache.ambari.server.topology.HostGroup group2 = createNiceMock(org.apache.ambari.server.topology.HostGroup.class);

    private final org.apache.ambari.server.topology.HostGroup group3 = createNiceMock(org.apache.ambari.server.topology.HostGroup.class);

    private final org.apache.ambari.server.topology.HostGroup group4 = createNiceMock(org.apache.ambari.server.topology.HostGroup.class);

    private final java.util.Map<java.lang.String, org.apache.ambari.server.topology.HostGroupInfo> hostGroupInfoMap = new java.util.HashMap<>();

    private final java.util.Map<java.lang.String, org.apache.ambari.server.topology.HostGroup> hostGroupMap = new java.util.HashMap<>();

    private org.apache.ambari.server.topology.Configuration configuration;

    private org.apache.ambari.server.topology.Configuration bpconfiguration;

    @org.junit.Before
    public void setUp() throws java.lang.Exception {
        configuration = new org.apache.ambari.server.topology.Configuration(new java.util.HashMap<>(), new java.util.HashMap<>());
        bpconfiguration = new org.apache.ambari.server.topology.Configuration(new java.util.HashMap<>(), new java.util.HashMap<>());
        org.apache.ambari.server.topology.HostGroupInfo group1Info = new org.apache.ambari.server.topology.HostGroupInfo("group1");
        org.apache.ambari.server.topology.HostGroupInfo group2Info = new org.apache.ambari.server.topology.HostGroupInfo("group2");
        org.apache.ambari.server.topology.HostGroupInfo group3Info = new org.apache.ambari.server.topology.HostGroupInfo("group3");
        org.apache.ambari.server.topology.HostGroupInfo group4Info = new org.apache.ambari.server.topology.HostGroupInfo("group4");
        hostGroupInfoMap.put("group1", group1Info);
        hostGroupInfoMap.put("group2", group2Info);
        hostGroupInfoMap.put("group3", group3Info);
        hostGroupInfoMap.put("group4", group4Info);
        group1Info.setConfiguration(configuration);
        java.util.Collection<java.lang.String> group1Hosts = new java.util.HashSet<>();
        group1Hosts.add("host1");
        group1Hosts.add("host2");
        group1Info.addHosts(group1Hosts);
        group2Info.setConfiguration(configuration);
        java.util.Collection<java.lang.String> group2Hosts = new java.util.HashSet<>();
        group2Hosts.add("host3");
        group2Info.addHosts(group2Hosts);
        java.util.Collection<java.lang.String> group4Hosts = new java.util.HashSet<>();
        group4Hosts.add("host4");
        group4Hosts.add("host5");
        group4Info.addHosts(group4Hosts);
        group3Info.setConfiguration(configuration);
        group3Info.setRequestedCount(5);
        group4Info.setConfiguration(configuration);
        group4Info.setRequestedCount(5);
        group4Info.setPredicate(org.apache.ambari.server.topology.ClusterTopologyImplTest.predicate);
        EasyMock.expect(blueprint.getConfiguration()).andReturn(bpconfiguration).anyTimes();
        hostGroupMap.put("group1", group1);
        hostGroupMap.put("group2", group2);
        hostGroupMap.put("group3", group3);
        hostGroupMap.put("group4", group4);
        java.util.Set<org.apache.ambari.server.topology.Component> group1Components = new java.util.HashSet<>();
        group1Components.add(new org.apache.ambari.server.topology.Component("component1"));
        group1Components.add(new org.apache.ambari.server.topology.Component("component2"));
        java.util.Set<java.lang.String> group1ComponentNames = new java.util.HashSet<>();
        group1ComponentNames.add("component1");
        group1ComponentNames.add("component2");
        java.util.Set<org.apache.ambari.server.topology.Component> group2Components = new java.util.HashSet<>();
        group2Components.add(new org.apache.ambari.server.topology.Component("component3"));
        java.util.Set<org.apache.ambari.server.topology.Component> group3Components = new java.util.HashSet<>();
        group3Components.add(new org.apache.ambari.server.topology.Component("component4"));
        java.util.Set<org.apache.ambari.server.topology.Component> group4Components = new java.util.HashSet<>();
        group4Components.add(new org.apache.ambari.server.topology.Component("component5"));
        EasyMock.expect(blueprint.getHostGroups()).andReturn(hostGroupMap).anyTimes();
        EasyMock.expect(blueprint.getHostGroup("group1")).andReturn(group1).anyTimes();
        EasyMock.expect(blueprint.getHostGroup("group2")).andReturn(group2).anyTimes();
        EasyMock.expect(blueprint.getHostGroup("group3")).andReturn(group3).anyTimes();
        EasyMock.expect(blueprint.getHostGroup("group4")).andReturn(group4).anyTimes();
        EasyMock.expect(group1.getConfiguration()).andReturn(configuration).anyTimes();
        EasyMock.expect(group2.getConfiguration()).andReturn(configuration).anyTimes();
        EasyMock.expect(group3.getConfiguration()).andReturn(configuration).anyTimes();
        EasyMock.expect(group4.getConfiguration()).andReturn(configuration).anyTimes();
        EasyMock.expect(group1.getComponents()).andReturn(group1Components).anyTimes();
        EasyMock.expect(group2.getComponents()).andReturn(group2Components).anyTimes();
        EasyMock.expect(group3.getComponents()).andReturn(group3Components).anyTimes();
        EasyMock.expect(group4.getComponents()).andReturn(group4Components).anyTimes();
        EasyMock.expect(group1.getComponentNames()).andReturn(group1ComponentNames).anyTimes();
        EasyMock.expect(group2.getComponentNames()).andReturn(java.util.Collections.singletonList("component3")).anyTimes();
        EasyMock.expect(group3.getComponentNames()).andReturn(java.util.Collections.singletonList("component4")).anyTimes();
        EasyMock.expect(group4.getComponentNames()).andReturn(java.util.Collections.singletonList("NAMENODE")).anyTimes();
    }

    @org.junit.After
    public void tearDown() {
        verify(blueprint, group1, group2, group3, group4);
        reset(blueprint, group1, group2, group3, group4);
        hostGroupInfoMap.clear();
        hostGroupMap.clear();
    }

    private void replayAll() {
        replay(blueprint, group1, group2, group3, group4);
    }

    @org.junit.Test(expected = org.apache.ambari.server.topology.InvalidTopologyException.class)
    public void testCreate_duplicateHosts() throws java.lang.Exception {
        hostGroupInfoMap.get("group2").addHost("host1");
        org.apache.ambari.server.topology.ClusterTopologyImplTest.TestTopologyRequest request = new org.apache.ambari.server.topology.ClusterTopologyImplTest.TestTopologyRequest(org.apache.ambari.server.topology.TopologyRequest.Type.PROVISION);
        replayAll();
        new org.apache.ambari.server.topology.ClusterTopologyImpl(null, request);
    }

    @org.junit.Test
    public void test_GetHostAssigmentForComponents() throws java.lang.Exception {
        org.apache.ambari.server.topology.ClusterTopologyImplTest.TestTopologyRequest request = new org.apache.ambari.server.topology.ClusterTopologyImplTest.TestTopologyRequest(org.apache.ambari.server.topology.TopologyRequest.Type.PROVISION);
        replayAll();
        new org.apache.ambari.server.topology.ClusterTopologyImpl(null, request).getHostAssignmentsForComponent("component1");
    }

    @org.junit.Test
    public void testDecidingIfComponentIsHadoopCompatible() throws java.lang.Exception {
        EasyMock.expect(blueprint.getServiceInfos()).andReturn(java.util.Arrays.asList(aHCFSWith(aComponent("ONEFS_CLIENT")), aServiceWith(aComponent("ZOOKEEPER_CLIENT")))).anyTimes();
        replayAll();
        org.apache.ambari.server.topology.ClusterTopologyImpl topology = new org.apache.ambari.server.topology.ClusterTopologyImpl(null, new org.apache.ambari.server.topology.ClusterTopologyImplTest.TestTopologyRequest(org.apache.ambari.server.topology.TopologyRequest.Type.PROVISION));
        org.junit.Assert.assertTrue(topology.hasHadoopCompatibleService());
    }

    private org.apache.ambari.server.state.ServiceInfo aHCFSWith(org.apache.ambari.server.state.ComponentInfo... components) {
        org.apache.ambari.server.state.ServiceInfo service = aServiceWith(components);
        service.setServiceType(org.apache.ambari.server.state.ServiceInfo.HADOOP_COMPATIBLE_FS);
        return service;
    }

    private org.apache.ambari.server.state.ServiceInfo aServiceWith(org.apache.ambari.server.state.ComponentInfo... components) {
        org.apache.ambari.server.state.ServiceInfo service = new org.apache.ambari.server.state.ServiceInfo();
        service.getComponents().addAll(java.util.Arrays.asList(components));
        return service;
    }

    private org.apache.ambari.server.state.ComponentInfo aComponent(java.lang.String name) {
        org.apache.ambari.server.state.ComponentInfo component = new org.apache.ambari.server.state.ComponentInfo();
        component.setName(name);
        return component;
    }

    private class TestTopologyRequest implements org.apache.ambari.server.topology.TopologyRequest {
        private org.apache.ambari.server.topology.TopologyRequest.Type type;

        public TestTopologyRequest(org.apache.ambari.server.topology.TopologyRequest.Type type) {
            this.type = type;
        }

        public java.lang.String getClusterName() {
            return org.apache.ambari.server.topology.ClusterTopologyImplTest.CLUSTER_NAME;
        }

        @java.lang.Override
        public java.lang.Long getClusterId() {
            return org.apache.ambari.server.topology.ClusterTopologyImplTest.CLUSTER_ID;
        }

        @java.lang.Override
        public org.apache.ambari.server.topology.TopologyRequest.Type getType() {
            return type;
        }

        @java.lang.Override
        public org.apache.ambari.server.topology.Blueprint getBlueprint() {
            return blueprint;
        }

        @java.lang.Override
        public org.apache.ambari.server.topology.Configuration getConfiguration() {
            return bpconfiguration;
        }

        @java.lang.Override
        public java.util.Map<java.lang.String, org.apache.ambari.server.topology.HostGroupInfo> getHostGroupInfo() {
            return hostGroupInfoMap;
        }

        @java.lang.Override
        public java.lang.String getDescription() {
            return "Test Request";
        }
    }
}