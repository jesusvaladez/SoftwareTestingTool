package org.apache.ambari.server.controller.internal;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.verify;
import static org.powermock.api.easymock.PowerMock.createStrictMock;
import static org.powermock.api.easymock.PowerMock.replay;
import static org.powermock.api.easymock.PowerMock.reset;
@java.lang.SuppressWarnings("unchecked")
public class ScaleClusterRequestTest {
    private static final java.lang.String CLUSTER_NAME = "cluster_name";

    private static final java.lang.String BLUEPRINT_NAME = "blueprint_name";

    private static final java.lang.String HOST1_NAME = "host1.test.com";

    private static final java.lang.String HOST2_NAME = "host2.test.com";

    private static final java.lang.String GROUP1_NAME = "group1";

    private static final java.lang.String GROUP2_NAME = "group2";

    private static final java.lang.String GROUP3_NAME = "group3";

    private static final java.lang.String PREDICATE = "test/prop=foo";

    private static final org.apache.ambari.server.topology.BlueprintFactory blueprintFactory = createStrictMock(org.apache.ambari.server.topology.BlueprintFactory.class);

    private static final org.apache.ambari.server.topology.Blueprint blueprint = EasyMock.createNiceMock(org.apache.ambari.server.topology.Blueprint.class);

    private static final org.apache.ambari.server.controller.spi.ResourceProvider hostResourceProvider = EasyMock.createMock(org.apache.ambari.server.controller.spi.ResourceProvider.class);

    private static final org.apache.ambari.server.topology.HostGroup hostGroup1 = EasyMock.createNiceMock(org.apache.ambari.server.topology.HostGroup.class);

    private static final org.apache.ambari.server.topology.Configuration blueprintConfig = new org.apache.ambari.server.topology.Configuration(java.util.Collections.emptyMap(), java.util.Collections.emptyMap());

    @org.junit.Before
    public void setUp() throws java.lang.Exception {
        org.apache.ambari.server.controller.internal.ScaleClusterRequest.init(org.apache.ambari.server.controller.internal.ScaleClusterRequestTest.blueprintFactory);
        java.lang.Class clazz = org.apache.ambari.server.controller.internal.BaseClusterRequest.class;
        java.lang.reflect.Field f = clazz.getDeclaredField("hostResourceProvider");
        f.setAccessible(true);
        f.set(null, org.apache.ambari.server.controller.internal.ScaleClusterRequestTest.hostResourceProvider);
        EasyMock.expect(org.apache.ambari.server.controller.internal.ScaleClusterRequestTest.blueprintFactory.getBlueprint(org.apache.ambari.server.controller.internal.ScaleClusterRequestTest.BLUEPRINT_NAME)).andReturn(org.apache.ambari.server.controller.internal.ScaleClusterRequestTest.blueprint).anyTimes();
        EasyMock.expect(org.apache.ambari.server.controller.internal.ScaleClusterRequestTest.blueprint.getConfiguration()).andReturn(org.apache.ambari.server.controller.internal.ScaleClusterRequestTest.blueprintConfig).anyTimes();
        EasyMock.expect(org.apache.ambari.server.controller.internal.ScaleClusterRequestTest.blueprint.getHostGroup(org.apache.ambari.server.controller.internal.ScaleClusterRequestTest.GROUP1_NAME)).andReturn(org.apache.ambari.server.controller.internal.ScaleClusterRequestTest.hostGroup1).anyTimes();
        EasyMock.expect(org.apache.ambari.server.controller.internal.ScaleClusterRequestTest.blueprint.getHostGroup(org.apache.ambari.server.controller.internal.ScaleClusterRequestTest.GROUP2_NAME)).andReturn(org.apache.ambari.server.controller.internal.ScaleClusterRequestTest.hostGroup1).anyTimes();
        EasyMock.expect(org.apache.ambari.server.controller.internal.ScaleClusterRequestTest.blueprint.getHostGroup(org.apache.ambari.server.controller.internal.ScaleClusterRequestTest.GROUP3_NAME)).andReturn(org.apache.ambari.server.controller.internal.ScaleClusterRequestTest.hostGroup1).anyTimes();
        EasyMock.expect(org.apache.ambari.server.controller.internal.ScaleClusterRequestTest.blueprint.getName()).andReturn(org.apache.ambari.server.controller.internal.ScaleClusterRequestTest.BLUEPRINT_NAME).anyTimes();
        EasyMock.expect(org.apache.ambari.server.controller.internal.ScaleClusterRequestTest.hostResourceProvider.checkPropertyIds(java.util.Collections.singleton("test/prop"))).andReturn(java.util.Collections.emptySet()).once();
        replay(org.apache.ambari.server.controller.internal.ScaleClusterRequestTest.blueprintFactory, org.apache.ambari.server.controller.internal.ScaleClusterRequestTest.blueprint, org.apache.ambari.server.controller.internal.ScaleClusterRequestTest.hostResourceProvider, org.apache.ambari.server.controller.internal.ScaleClusterRequestTest.hostGroup1);
    }

    @org.junit.After
    public void tearDown() {
        EasyMock.verify(org.apache.ambari.server.controller.internal.ScaleClusterRequestTest.blueprintFactory, org.apache.ambari.server.controller.internal.ScaleClusterRequestTest.blueprint, org.apache.ambari.server.controller.internal.ScaleClusterRequestTest.hostResourceProvider, org.apache.ambari.server.controller.internal.ScaleClusterRequestTest.hostGroup1);
        reset(org.apache.ambari.server.controller.internal.ScaleClusterRequestTest.blueprintFactory, org.apache.ambari.server.controller.internal.ScaleClusterRequestTest.blueprint, org.apache.ambari.server.controller.internal.ScaleClusterRequestTest.hostResourceProvider, org.apache.ambari.server.controller.internal.ScaleClusterRequestTest.hostGroup1);
    }

    @org.junit.Test
    public void test_basic_hostName() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.lang.Object> props = org.apache.ambari.server.controller.internal.ScaleClusterRequestTest.createScaleClusterPropertiesGroup1_HostName(org.apache.ambari.server.controller.internal.ScaleClusterRequestTest.CLUSTER_NAME, org.apache.ambari.server.controller.internal.ScaleClusterRequestTest.BLUEPRINT_NAME);
        addSingleHostByName(props);
        addSingleHostByName(org.apache.ambari.server.controller.internal.ScaleClusterRequestTest.replaceWithPlainHostNameKey(props));
    }

    private void addSingleHostByName(java.util.Map<java.lang.String, java.lang.Object> props) throws org.apache.ambari.server.topology.InvalidTopologyTemplateException {
        reset(org.apache.ambari.server.controller.internal.ScaleClusterRequestTest.hostResourceProvider);
        replay(org.apache.ambari.server.controller.internal.ScaleClusterRequestTest.hostResourceProvider);
        org.apache.ambari.server.controller.internal.ScaleClusterRequest scaleClusterRequest = new org.apache.ambari.server.controller.internal.ScaleClusterRequest(java.util.Collections.singleton(props));
        org.junit.Assert.assertEquals(org.apache.ambari.server.topology.TopologyRequest.Type.SCALE, scaleClusterRequest.getType());
        org.junit.Assert.assertEquals(java.lang.String.format("Scale Cluster '%s' (+%s hosts)", org.apache.ambari.server.controller.internal.ScaleClusterRequestTest.CLUSTER_NAME, "1"), scaleClusterRequest.getDescription());
        org.junit.Assert.assertEquals(org.apache.ambari.server.controller.internal.ScaleClusterRequestTest.CLUSTER_NAME, scaleClusterRequest.getClusterName());
        org.junit.Assert.assertSame(org.apache.ambari.server.controller.internal.ScaleClusterRequestTest.blueprint, scaleClusterRequest.getBlueprint());
        java.util.Map<java.lang.String, org.apache.ambari.server.topology.HostGroupInfo> hostGroupInfo = scaleClusterRequest.getHostGroupInfo();
        org.junit.Assert.assertEquals(1, hostGroupInfo.size());
        org.apache.ambari.server.topology.HostGroupInfo group1Info = hostGroupInfo.get(org.apache.ambari.server.controller.internal.ScaleClusterRequestTest.GROUP1_NAME);
        org.junit.Assert.assertEquals(org.apache.ambari.server.controller.internal.ScaleClusterRequestTest.GROUP1_NAME, group1Info.getHostGroupName());
        org.junit.Assert.assertEquals(1, group1Info.getHostNames().size());
        org.junit.Assert.assertTrue(group1Info.getHostNames().contains(org.apache.ambari.server.controller.internal.ScaleClusterRequestTest.HOST1_NAME));
        org.junit.Assert.assertEquals(1, group1Info.getRequestedHostCount());
        org.junit.Assert.assertNull(group1Info.getPredicate());
    }

    @org.junit.Test
    public void testMultipleHostNames() throws java.lang.Exception {
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> propertySet = new java.util.HashSet<>();
        propertySet.add(org.apache.ambari.server.controller.internal.ScaleClusterRequestTest.createScaleClusterPropertiesGroup1_HostName(org.apache.ambari.server.controller.internal.ScaleClusterRequestTest.CLUSTER_NAME, org.apache.ambari.server.controller.internal.ScaleClusterRequestTest.BLUEPRINT_NAME));
        propertySet.add(org.apache.ambari.server.controller.internal.ScaleClusterRequestTest.createScaleClusterPropertiesGroup1_HostName2(org.apache.ambari.server.controller.internal.ScaleClusterRequestTest.CLUSTER_NAME, org.apache.ambari.server.controller.internal.ScaleClusterRequestTest.BLUEPRINT_NAME));
        addMultipleHostsByName(propertySet);
        for (java.util.Map<java.lang.String, java.lang.Object> props : propertySet) {
            org.apache.ambari.server.controller.internal.ScaleClusterRequestTest.replaceWithPlainHostNameKey(props);
        }
        addMultipleHostsByName(propertySet);
    }

    private void addMultipleHostsByName(java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> propertySet) throws org.apache.ambari.server.topology.InvalidTopologyTemplateException {
        reset(org.apache.ambari.server.controller.internal.ScaleClusterRequestTest.hostResourceProvider);
        replay(org.apache.ambari.server.controller.internal.ScaleClusterRequestTest.hostResourceProvider);
        org.apache.ambari.server.controller.internal.ScaleClusterRequest scaleClusterRequest = new org.apache.ambari.server.controller.internal.ScaleClusterRequest(propertySet);
        org.junit.Assert.assertEquals(org.apache.ambari.server.topology.TopologyRequest.Type.SCALE, scaleClusterRequest.getType());
        org.junit.Assert.assertEquals(java.lang.String.format("Scale Cluster '%s' (+%s hosts)", org.apache.ambari.server.controller.internal.ScaleClusterRequestTest.CLUSTER_NAME, "2"), scaleClusterRequest.getDescription());
        org.junit.Assert.assertEquals(org.apache.ambari.server.controller.internal.ScaleClusterRequestTest.CLUSTER_NAME, scaleClusterRequest.getClusterName());
        org.junit.Assert.assertSame(org.apache.ambari.server.controller.internal.ScaleClusterRequestTest.blueprint, scaleClusterRequest.getBlueprint());
        java.util.Map<java.lang.String, org.apache.ambari.server.topology.HostGroupInfo> hostGroupInfo = scaleClusterRequest.getHostGroupInfo();
        org.junit.Assert.assertEquals(1, hostGroupInfo.size());
        org.apache.ambari.server.topology.HostGroupInfo group1Info = hostGroupInfo.get(org.apache.ambari.server.controller.internal.ScaleClusterRequestTest.GROUP1_NAME);
        org.junit.Assert.assertEquals(org.apache.ambari.server.controller.internal.ScaleClusterRequestTest.GROUP1_NAME, group1Info.getHostGroupName());
        org.junit.Assert.assertEquals(2, group1Info.getHostNames().size());
        org.junit.Assert.assertTrue(group1Info.getHostNames().contains(org.apache.ambari.server.controller.internal.ScaleClusterRequestTest.HOST1_NAME));
        org.junit.Assert.assertTrue(group1Info.getHostNames().contains(org.apache.ambari.server.controller.internal.ScaleClusterRequestTest.HOST2_NAME));
        org.junit.Assert.assertEquals(2, group1Info.getRequestedHostCount());
        org.junit.Assert.assertNull(group1Info.getPredicate());
    }

    @org.junit.Test
    public void test_basic_hostCount() throws java.lang.Exception {
        reset(org.apache.ambari.server.controller.internal.ScaleClusterRequestTest.hostResourceProvider);
        replay(org.apache.ambari.server.controller.internal.ScaleClusterRequestTest.hostResourceProvider);
        org.apache.ambari.server.controller.internal.ScaleClusterRequest scaleClusterRequest = new org.apache.ambari.server.controller.internal.ScaleClusterRequest(java.util.Collections.singleton(org.apache.ambari.server.controller.internal.ScaleClusterRequestTest.createScaleClusterPropertiesGroup1_HostCount(org.apache.ambari.server.controller.internal.ScaleClusterRequestTest.CLUSTER_NAME, org.apache.ambari.server.controller.internal.ScaleClusterRequestTest.BLUEPRINT_NAME)));
        org.junit.Assert.assertEquals(org.apache.ambari.server.topology.TopologyRequest.Type.SCALE, scaleClusterRequest.getType());
        org.junit.Assert.assertEquals(java.lang.String.format("Scale Cluster '%s' (+%s hosts)", org.apache.ambari.server.controller.internal.ScaleClusterRequestTest.CLUSTER_NAME, "1"), scaleClusterRequest.getDescription());
        org.junit.Assert.assertEquals(org.apache.ambari.server.controller.internal.ScaleClusterRequestTest.CLUSTER_NAME, scaleClusterRequest.getClusterName());
        org.junit.Assert.assertSame(org.apache.ambari.server.controller.internal.ScaleClusterRequestTest.blueprint, scaleClusterRequest.getBlueprint());
        java.util.Map<java.lang.String, org.apache.ambari.server.topology.HostGroupInfo> hostGroupInfo = scaleClusterRequest.getHostGroupInfo();
        org.junit.Assert.assertEquals(1, hostGroupInfo.size());
        org.apache.ambari.server.topology.HostGroupInfo group2Info = hostGroupInfo.get(org.apache.ambari.server.controller.internal.ScaleClusterRequestTest.GROUP2_NAME);
        org.junit.Assert.assertEquals(org.apache.ambari.server.controller.internal.ScaleClusterRequestTest.GROUP2_NAME, group2Info.getHostGroupName());
        org.junit.Assert.assertEquals(0, group2Info.getHostNames().size());
        org.junit.Assert.assertEquals(1, group2Info.getRequestedHostCount());
        org.junit.Assert.assertNull(group2Info.getPredicate());
    }

    @org.junit.Test
    public void test_basic_hostCount2() throws java.lang.Exception {
        reset(org.apache.ambari.server.controller.internal.ScaleClusterRequestTest.hostResourceProvider);
        replay(org.apache.ambari.server.controller.internal.ScaleClusterRequestTest.hostResourceProvider);
        org.apache.ambari.server.controller.internal.ScaleClusterRequest scaleClusterRequest = new org.apache.ambari.server.controller.internal.ScaleClusterRequest(java.util.Collections.singleton(org.apache.ambari.server.controller.internal.ScaleClusterRequestTest.createScaleClusterPropertiesGroup1_HostCount2(org.apache.ambari.server.controller.internal.ScaleClusterRequestTest.CLUSTER_NAME, org.apache.ambari.server.controller.internal.ScaleClusterRequestTest.BLUEPRINT_NAME)));
        org.junit.Assert.assertEquals(org.apache.ambari.server.topology.TopologyRequest.Type.SCALE, scaleClusterRequest.getType());
        org.junit.Assert.assertEquals(java.lang.String.format("Scale Cluster '%s' (+%s hosts)", org.apache.ambari.server.controller.internal.ScaleClusterRequestTest.CLUSTER_NAME, "2"), scaleClusterRequest.getDescription());
        org.junit.Assert.assertEquals(org.apache.ambari.server.controller.internal.ScaleClusterRequestTest.CLUSTER_NAME, scaleClusterRequest.getClusterName());
        org.junit.Assert.assertSame(org.apache.ambari.server.controller.internal.ScaleClusterRequestTest.blueprint, scaleClusterRequest.getBlueprint());
        java.util.Map<java.lang.String, org.apache.ambari.server.topology.HostGroupInfo> hostGroupInfo = scaleClusterRequest.getHostGroupInfo();
        org.junit.Assert.assertEquals(1, hostGroupInfo.size());
        org.apache.ambari.server.topology.HostGroupInfo group2Info = hostGroupInfo.get(org.apache.ambari.server.controller.internal.ScaleClusterRequestTest.GROUP3_NAME);
        org.junit.Assert.assertEquals(org.apache.ambari.server.controller.internal.ScaleClusterRequestTest.GROUP3_NAME, group2Info.getHostGroupName());
        org.junit.Assert.assertEquals(0, group2Info.getHostNames().size());
        org.junit.Assert.assertEquals(2, group2Info.getRequestedHostCount());
        org.junit.Assert.assertNull(group2Info.getPredicate());
    }

    @org.junit.Test
    public void test_basic_hostCountAndPredicate() throws java.lang.Exception {
        org.apache.ambari.server.controller.internal.ScaleClusterRequest scaleClusterRequest = new org.apache.ambari.server.controller.internal.ScaleClusterRequest(java.util.Collections.singleton(org.apache.ambari.server.controller.internal.ScaleClusterRequestTest.createScaleClusterPropertiesGroup1_HostCountAndPredicate(org.apache.ambari.server.controller.internal.ScaleClusterRequestTest.CLUSTER_NAME, org.apache.ambari.server.controller.internal.ScaleClusterRequestTest.BLUEPRINT_NAME)));
        org.junit.Assert.assertEquals(org.apache.ambari.server.topology.TopologyRequest.Type.SCALE, scaleClusterRequest.getType());
        org.junit.Assert.assertEquals(java.lang.String.format("Scale Cluster '%s' (+%s hosts)", org.apache.ambari.server.controller.internal.ScaleClusterRequestTest.CLUSTER_NAME, "1"), scaleClusterRequest.getDescription());
        org.junit.Assert.assertEquals(org.apache.ambari.server.controller.internal.ScaleClusterRequestTest.CLUSTER_NAME, scaleClusterRequest.getClusterName());
        org.junit.Assert.assertSame(org.apache.ambari.server.controller.internal.ScaleClusterRequestTest.blueprint, scaleClusterRequest.getBlueprint());
        java.util.Map<java.lang.String, org.apache.ambari.server.topology.HostGroupInfo> hostGroupInfo = scaleClusterRequest.getHostGroupInfo();
        org.junit.Assert.assertEquals(1, hostGroupInfo.size());
        org.apache.ambari.server.topology.HostGroupInfo group3Info = hostGroupInfo.get(org.apache.ambari.server.controller.internal.ScaleClusterRequestTest.GROUP3_NAME);
        org.junit.Assert.assertEquals(org.apache.ambari.server.controller.internal.ScaleClusterRequestTest.GROUP3_NAME, group3Info.getHostGroupName());
        org.junit.Assert.assertEquals(0, group3Info.getHostNames().size());
        org.junit.Assert.assertEquals(1, group3Info.getRequestedHostCount());
        org.junit.Assert.assertEquals(org.apache.ambari.server.controller.internal.ScaleClusterRequestTest.PREDICATE, group3Info.getPredicateString());
    }

    @org.junit.Test
    public void testMultipleHostGroups() throws java.lang.Exception {
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> propertySet = new java.util.HashSet<>();
        propertySet.add(org.apache.ambari.server.controller.internal.ScaleClusterRequestTest.createScaleClusterPropertiesGroup1_HostCountAndPredicate(org.apache.ambari.server.controller.internal.ScaleClusterRequestTest.CLUSTER_NAME, org.apache.ambari.server.controller.internal.ScaleClusterRequestTest.BLUEPRINT_NAME));
        propertySet.add(org.apache.ambari.server.controller.internal.ScaleClusterRequestTest.createScaleClusterPropertiesGroup1_HostCount(org.apache.ambari.server.controller.internal.ScaleClusterRequestTest.CLUSTER_NAME, org.apache.ambari.server.controller.internal.ScaleClusterRequestTest.BLUEPRINT_NAME));
        propertySet.add(org.apache.ambari.server.controller.internal.ScaleClusterRequestTest.createScaleClusterPropertiesGroup1_HostName(org.apache.ambari.server.controller.internal.ScaleClusterRequestTest.CLUSTER_NAME, org.apache.ambari.server.controller.internal.ScaleClusterRequestTest.BLUEPRINT_NAME));
        org.apache.ambari.server.controller.internal.ScaleClusterRequest scaleClusterRequest = new org.apache.ambari.server.controller.internal.ScaleClusterRequest(propertySet);
        org.junit.Assert.assertEquals(org.apache.ambari.server.topology.TopologyRequest.Type.SCALE, scaleClusterRequest.getType());
        org.junit.Assert.assertEquals(java.lang.String.format("Scale Cluster '%s' (+%s hosts)", org.apache.ambari.server.controller.internal.ScaleClusterRequestTest.CLUSTER_NAME, "3"), scaleClusterRequest.getDescription());
        org.junit.Assert.assertEquals(org.apache.ambari.server.controller.internal.ScaleClusterRequestTest.CLUSTER_NAME, scaleClusterRequest.getClusterName());
        org.junit.Assert.assertSame(org.apache.ambari.server.controller.internal.ScaleClusterRequestTest.blueprint, scaleClusterRequest.getBlueprint());
        java.util.Map<java.lang.String, org.apache.ambari.server.topology.HostGroupInfo> hostGroupInfo = scaleClusterRequest.getHostGroupInfo();
        org.junit.Assert.assertEquals(3, hostGroupInfo.size());
        org.apache.ambari.server.topology.HostGroupInfo group1Info = hostGroupInfo.get(org.apache.ambari.server.controller.internal.ScaleClusterRequestTest.GROUP1_NAME);
        org.junit.Assert.assertEquals(org.apache.ambari.server.controller.internal.ScaleClusterRequestTest.GROUP1_NAME, group1Info.getHostGroupName());
        org.junit.Assert.assertEquals(1, group1Info.getHostNames().size());
        org.junit.Assert.assertTrue(group1Info.getHostNames().contains(org.apache.ambari.server.controller.internal.ScaleClusterRequestTest.HOST1_NAME));
        org.junit.Assert.assertEquals(1, group1Info.getRequestedHostCount());
        org.junit.Assert.assertNull(group1Info.getPredicate());
        org.apache.ambari.server.topology.HostGroupInfo group2Info = hostGroupInfo.get(org.apache.ambari.server.controller.internal.ScaleClusterRequestTest.GROUP2_NAME);
        org.junit.Assert.assertEquals(org.apache.ambari.server.controller.internal.ScaleClusterRequestTest.GROUP2_NAME, group2Info.getHostGroupName());
        org.junit.Assert.assertEquals(0, group2Info.getHostNames().size());
        org.junit.Assert.assertEquals(1, group2Info.getRequestedHostCount());
        org.junit.Assert.assertNull(group2Info.getPredicate());
        org.apache.ambari.server.topology.HostGroupInfo group3Info = hostGroupInfo.get(org.apache.ambari.server.controller.internal.ScaleClusterRequestTest.GROUP3_NAME);
        org.junit.Assert.assertEquals(org.apache.ambari.server.controller.internal.ScaleClusterRequestTest.GROUP3_NAME, group3Info.getHostGroupName());
        org.junit.Assert.assertEquals(0, group3Info.getHostNames().size());
        org.junit.Assert.assertEquals(1, group3Info.getRequestedHostCount());
        org.junit.Assert.assertEquals(org.apache.ambari.server.controller.internal.ScaleClusterRequestTest.PREDICATE, group3Info.getPredicateString());
    }

    @org.junit.Test(expected = org.apache.ambari.server.topology.InvalidTopologyTemplateException.class)
    public void test_GroupInfoMissingName() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.lang.Object> properties = org.apache.ambari.server.controller.internal.ScaleClusterRequestTest.createScaleClusterPropertiesGroup1_HostName(org.apache.ambari.server.controller.internal.ScaleClusterRequestTest.CLUSTER_NAME, org.apache.ambari.server.controller.internal.ScaleClusterRequestTest.BLUEPRINT_NAME);
        properties.remove("host_group");
        reset(org.apache.ambari.server.controller.internal.ScaleClusterRequestTest.hostResourceProvider);
        replay(org.apache.ambari.server.controller.internal.ScaleClusterRequestTest.hostResourceProvider);
        new org.apache.ambari.server.controller.internal.ScaleClusterRequest(java.util.Collections.singleton(properties));
    }

    @org.junit.Test(expected = org.apache.ambari.server.topology.InvalidTopologyTemplateException.class)
    public void test_NoHostNameOrHostCount() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.lang.Object> properties = org.apache.ambari.server.controller.internal.ScaleClusterRequestTest.createScaleClusterPropertiesGroup1_HostName(org.apache.ambari.server.controller.internal.ScaleClusterRequestTest.CLUSTER_NAME, org.apache.ambari.server.controller.internal.ScaleClusterRequestTest.BLUEPRINT_NAME);
        properties.remove(org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_HOST_NAME_PROPERTY_ID);
        reset(org.apache.ambari.server.controller.internal.ScaleClusterRequestTest.hostResourceProvider);
        replay(org.apache.ambari.server.controller.internal.ScaleClusterRequestTest.hostResourceProvider);
        new org.apache.ambari.server.controller.internal.ScaleClusterRequest(java.util.Collections.singleton(properties));
    }

    @org.junit.Test(expected = org.apache.ambari.server.topology.InvalidTopologyTemplateException.class)
    public void testInvalidPredicateProperty() throws java.lang.Exception {
        reset(org.apache.ambari.server.controller.internal.ScaleClusterRequestTest.hostResourceProvider);
        EasyMock.expect(org.apache.ambari.server.controller.internal.ScaleClusterRequestTest.hostResourceProvider.checkPropertyIds(java.util.Collections.singleton("test/prop"))).andReturn(java.util.Collections.singleton("test/prop"));
        replay(org.apache.ambari.server.controller.internal.ScaleClusterRequestTest.hostResourceProvider);
        new org.apache.ambari.server.controller.internal.ScaleClusterRequest(java.util.Collections.singleton(org.apache.ambari.server.controller.internal.ScaleClusterRequestTest.createScaleClusterPropertiesGroup1_HostCountAndPredicate(org.apache.ambari.server.controller.internal.ScaleClusterRequestTest.CLUSTER_NAME, org.apache.ambari.server.controller.internal.ScaleClusterRequestTest.BLUEPRINT_NAME)));
    }

    @org.junit.Test(expected = org.apache.ambari.server.topology.InvalidTopologyTemplateException.class)
    public void testMultipleBlueprints() throws java.lang.Exception {
        reset(org.apache.ambari.server.controller.internal.ScaleClusterRequestTest.hostResourceProvider);
        replay(org.apache.ambari.server.controller.internal.ScaleClusterRequestTest.hostResourceProvider);
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> propertySet = new java.util.LinkedHashSet<>();
        propertySet.add(org.apache.ambari.server.controller.internal.ScaleClusterRequestTest.createScaleClusterPropertiesGroup1_HostName(org.apache.ambari.server.controller.internal.ScaleClusterRequestTest.CLUSTER_NAME, org.apache.ambari.server.controller.internal.ScaleClusterRequestTest.BLUEPRINT_NAME));
        propertySet.add(org.apache.ambari.server.controller.internal.ScaleClusterRequestTest.createScaleClusterPropertiesGroup1_HostName2(org.apache.ambari.server.controller.internal.ScaleClusterRequestTest.CLUSTER_NAME, "OTHER_BLUEPRINT"));
        new org.apache.ambari.server.controller.internal.ScaleClusterRequest(propertySet);
    }

    public static java.util.Map<java.lang.String, java.lang.Object> createScaleClusterPropertiesGroup1_HostName(java.lang.String clusterName, java.lang.String blueprintName) {
        java.util.Map<java.lang.String, java.lang.Object> properties = new java.util.LinkedHashMap<>();
        properties.put(org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_CLUSTER_NAME_PROPERTY_ID, clusterName);
        properties.put(org.apache.ambari.server.controller.internal.HostResourceProvider.BLUEPRINT_PROPERTY_ID, blueprintName);
        properties.put(org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_GROUP_PROPERTY_ID, org.apache.ambari.server.controller.internal.ScaleClusterRequestTest.GROUP1_NAME);
        properties.put(org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_HOST_NAME_PROPERTY_ID, org.apache.ambari.server.controller.internal.ScaleClusterRequestTest.HOST1_NAME);
        return properties;
    }

    private static java.util.Map<java.lang.String, java.lang.Object> replaceWithPlainHostNameKey(java.util.Map<java.lang.String, java.lang.Object> properties) {
        java.lang.Object value = properties.remove(org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_HOST_NAME_PROPERTY_ID);
        properties.put(org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_NAME_PROPERTY_ID, value);
        return properties;
    }

    public static java.util.Map<java.lang.String, java.lang.Object> createScaleClusterPropertiesGroup1_HostCount(java.lang.String clusterName, java.lang.String blueprintName) {
        java.util.Map<java.lang.String, java.lang.Object> properties = new java.util.LinkedHashMap<>();
        properties.put(org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_CLUSTER_NAME_PROPERTY_ID, clusterName);
        properties.put(org.apache.ambari.server.controller.internal.HostResourceProvider.BLUEPRINT_PROPERTY_ID, blueprintName);
        properties.put(org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_GROUP_PROPERTY_ID, org.apache.ambari.server.controller.internal.ScaleClusterRequestTest.GROUP2_NAME);
        properties.put(org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_COUNT_PROPERTY_ID, 1);
        return properties;
    }

    public static java.util.Map<java.lang.String, java.lang.Object> createScaleClusterPropertiesGroup1_HostCountAndPredicate(java.lang.String clusterName, java.lang.String blueprintName) {
        java.util.Map<java.lang.String, java.lang.Object> properties = new java.util.LinkedHashMap<>();
        properties.put(org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_CLUSTER_NAME_PROPERTY_ID, clusterName);
        properties.put(org.apache.ambari.server.controller.internal.HostResourceProvider.BLUEPRINT_PROPERTY_ID, blueprintName);
        properties.put(org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_GROUP_PROPERTY_ID, org.apache.ambari.server.controller.internal.ScaleClusterRequestTest.GROUP3_NAME);
        properties.put(org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_COUNT_PROPERTY_ID, 1);
        properties.put(org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_PREDICATE_PROPERTY_ID, org.apache.ambari.server.controller.internal.ScaleClusterRequestTest.PREDICATE);
        return properties;
    }

    public static java.util.Map<java.lang.String, java.lang.Object> createScaleClusterPropertiesGroup1_HostCount2(java.lang.String clusterName, java.lang.String blueprintName) {
        java.util.Map<java.lang.String, java.lang.Object> properties = new java.util.LinkedHashMap<>();
        properties.put(org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_CLUSTER_NAME_PROPERTY_ID, clusterName);
        properties.put(org.apache.ambari.server.controller.internal.HostResourceProvider.BLUEPRINT_PROPERTY_ID, blueprintName);
        properties.put(org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_GROUP_PROPERTY_ID, org.apache.ambari.server.controller.internal.ScaleClusterRequestTest.GROUP3_NAME);
        properties.put(org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_COUNT_PROPERTY_ID, 2);
        return properties;
    }

    public static java.util.Map<java.lang.String, java.lang.Object> createScaleClusterPropertiesGroup1_HostName2(java.lang.String clusterName, java.lang.String blueprintName) {
        java.util.Map<java.lang.String, java.lang.Object> properties = new java.util.LinkedHashMap<>();
        properties.put(org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_CLUSTER_NAME_PROPERTY_ID, clusterName);
        properties.put(org.apache.ambari.server.controller.internal.HostResourceProvider.BLUEPRINT_PROPERTY_ID, blueprintName);
        properties.put(org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_GROUP_PROPERTY_ID, org.apache.ambari.server.controller.internal.ScaleClusterRequestTest.GROUP1_NAME);
        properties.put(org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_HOST_NAME_PROPERTY_ID, org.apache.ambari.server.controller.internal.ScaleClusterRequestTest.HOST2_NAME);
        return properties;
    }
}