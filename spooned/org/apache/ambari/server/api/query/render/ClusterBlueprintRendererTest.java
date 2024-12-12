package org.apache.ambari.server.api.query.render;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import static org.easymock.EasyMock.anyLong;
import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.createStrictMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.reset;
import static org.easymock.EasyMock.verify;
@java.lang.SuppressWarnings("unchecked")
@org.junit.runner.RunWith(org.powermock.modules.junit4.PowerMockRunner.class)
@org.powermock.core.classloader.annotations.PrepareForTest({ org.apache.ambari.server.topology.AmbariContext.class, org.apache.ambari.server.controller.AmbariServer.class })
public class ClusterBlueprintRendererTest {
    private static final org.apache.ambari.server.topology.ClusterTopology topology = EasyMock.createNiceMock(org.apache.ambari.server.topology.ClusterTopology.class);

    private static final org.apache.ambari.server.controller.spi.ClusterController clusterController = EasyMock.createNiceMock(org.apache.ambari.server.controller.internal.ClusterControllerImpl.class);

    private static final org.apache.ambari.server.topology.AmbariContext ambariContext = EasyMock.createNiceMock(org.apache.ambari.server.topology.AmbariContext.class);

    private static final org.apache.ambari.server.state.Cluster cluster = EasyMock.createNiceMock(org.apache.ambari.server.state.Cluster.class);

    private static final org.apache.ambari.server.state.Clusters clusters = EasyMock.createNiceMock(org.apache.ambari.server.state.cluster.ClustersImpl.class);

    private static final org.apache.ambari.server.controller.AmbariManagementController controller = EasyMock.createNiceMock(org.apache.ambari.server.controller.AmbariManagementControllerImpl.class);

    private static final org.apache.ambari.server.controller.KerberosHelper kerberosHelper = EasyMock.createNiceMock(org.apache.ambari.server.controller.KerberosHelperImpl.class);

    private static final org.apache.ambari.server.state.kerberos.KerberosDescriptor kerberosDescriptor = EasyMock.createNiceMock(org.apache.ambari.server.state.kerberos.KerberosDescriptor.class);

    private static final org.apache.ambari.server.topology.Blueprint blueprint = EasyMock.createNiceMock(org.apache.ambari.server.topology.Blueprint.class);

    private static final org.apache.ambari.server.controller.internal.Stack stack = EasyMock.createNiceMock(org.apache.ambari.server.controller.internal.Stack.class);

    private static final org.apache.ambari.server.topology.HostGroup group1 = EasyMock.createNiceMock(org.apache.ambari.server.topology.HostGroup.class);

    private static final org.apache.ambari.server.topology.HostGroup group2 = EasyMock.createNiceMock(org.apache.ambari.server.topology.HostGroup.class);

    private static final org.apache.ambari.server.topology.Configuration emptyConfiguration = new org.apache.ambari.server.topology.Configuration(new java.util.HashMap<>(), new java.util.HashMap<>());

    private static final java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> clusterProps = new java.util.HashMap<>();

    private static final java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>> clusterAttributes = new java.util.HashMap<>();

    private static final org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(org.apache.ambari.server.api.query.render.ClusterBlueprintRendererTest.clusterProps, org.apache.ambari.server.api.query.render.ClusterBlueprintRendererTest.clusterAttributes);

    private final org.apache.ambari.server.api.query.render.ClusterBlueprintRenderer minimalRenderer = new org.apache.ambari.server.api.query.render.ClusterBlueprintRenderer(org.apache.ambari.server.controller.internal.BlueprintExportType.MINIMAL);

    private final org.apache.ambari.server.api.query.render.ClusterBlueprintRenderer fullRenderer = new org.apache.ambari.server.api.query.render.ClusterBlueprintRenderer(org.apache.ambari.server.controller.internal.BlueprintExportType.FULL);

    @org.junit.Before
    public void setup() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.lang.String> clusterTypeProps = new java.util.HashMap<>();
        org.apache.ambari.server.api.query.render.ClusterBlueprintRendererTest.clusterProps.put("test-type-one", clusterTypeProps);
        clusterTypeProps.put("propertyOne", "valueOne");
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> clusterTypeAttributes = new java.util.HashMap<>();
        org.apache.ambari.server.api.query.render.ClusterBlueprintRendererTest.clusterAttributes.put("test-type-one", clusterTypeAttributes);
        java.util.Map<java.lang.String, java.lang.String> clusterAttributeProps = new java.util.HashMap<>();
        clusterAttributeProps.put("propertyOne", "true");
        clusterTypeAttributes.put("final", clusterAttributeProps);
        java.util.Collection<org.apache.ambari.server.topology.Component> group1Components = java.util.Arrays.asList(new org.apache.ambari.server.topology.Component("JOBTRACKER"), new org.apache.ambari.server.topology.Component("TASKTRACKER"), new org.apache.ambari.server.topology.Component("NAMENODE"), new org.apache.ambari.server.topology.Component("DATANODE"), new org.apache.ambari.server.topology.Component("AMBARI_SERVER"));
        java.util.Collection<org.apache.ambari.server.topology.Component> group2Components = java.util.Arrays.asList(new org.apache.ambari.server.topology.Component("TASKTRACKER"), new org.apache.ambari.server.topology.Component("DATANODE"));
        java.util.Map<java.lang.String, org.apache.ambari.server.topology.HostGroup> hostGroups = new java.util.HashMap<>();
        hostGroups.put("host_group_1", org.apache.ambari.server.api.query.render.ClusterBlueprintRendererTest.group1);
        hostGroups.put("host_group_2", org.apache.ambari.server.api.query.render.ClusterBlueprintRendererTest.group2);
        org.apache.ambari.server.topology.HostGroupInfo group1Info = new org.apache.ambari.server.topology.HostGroupInfo("host_group_1");
        group1Info.addHost("host1");
        group1Info.setConfiguration(org.apache.ambari.server.api.query.render.ClusterBlueprintRendererTest.emptyConfiguration);
        org.apache.ambari.server.topology.HostGroupInfo group2Info = new org.apache.ambari.server.topology.HostGroupInfo("host_group_2");
        java.util.Map<java.lang.String, org.apache.ambari.server.topology.HostGroupInfo> groupInfoMap = new java.util.HashMap<>();
        group2Info.addHosts(java.util.Arrays.asList("host2", "host3"));
        group2Info.setConfiguration(org.apache.ambari.server.api.query.render.ClusterBlueprintRendererTest.emptyConfiguration);
        groupInfoMap.put("host_group_1", group1Info);
        groupInfoMap.put("host_group_2", group2Info);
        EasyMock.expect(org.apache.ambari.server.api.query.render.ClusterBlueprintRendererTest.topology.isNameNodeHAEnabled()).andReturn(false).anyTimes();
        EasyMock.expect(org.apache.ambari.server.api.query.render.ClusterBlueprintRendererTest.topology.getConfiguration()).andReturn(org.apache.ambari.server.api.query.render.ClusterBlueprintRendererTest.clusterConfig).anyTimes();
        EasyMock.expect(org.apache.ambari.server.api.query.render.ClusterBlueprintRendererTest.topology.getBlueprint()).andReturn(org.apache.ambari.server.api.query.render.ClusterBlueprintRendererTest.blueprint).anyTimes();
        EasyMock.expect(org.apache.ambari.server.api.query.render.ClusterBlueprintRendererTest.topology.getHostGroupInfo()).andReturn(groupInfoMap).anyTimes();
        EasyMock.expect(org.apache.ambari.server.api.query.render.ClusterBlueprintRendererTest.blueprint.getStack()).andReturn(org.apache.ambari.server.api.query.render.ClusterBlueprintRendererTest.stack).anyTimes();
        EasyMock.expect(org.apache.ambari.server.api.query.render.ClusterBlueprintRendererTest.blueprint.getHostGroups()).andReturn(hostGroups).anyTimes();
        EasyMock.expect(org.apache.ambari.server.api.query.render.ClusterBlueprintRendererTest.blueprint.getHostGroup("host_group_1")).andReturn(org.apache.ambari.server.api.query.render.ClusterBlueprintRendererTest.group1).anyTimes();
        EasyMock.expect(org.apache.ambari.server.api.query.render.ClusterBlueprintRendererTest.blueprint.getHostGroup("host_group_2")).andReturn(org.apache.ambari.server.api.query.render.ClusterBlueprintRendererTest.group2).anyTimes();
        EasyMock.expect(org.apache.ambari.server.api.query.render.ClusterBlueprintRendererTest.blueprint.getServices()).andReturn(com.google.common.collect.ImmutableSet.of("HDFS", "YARN")).anyTimes();
        EasyMock.expect(org.apache.ambari.server.api.query.render.ClusterBlueprintRendererTest.stack.getName()).andReturn("HDP").anyTimes();
        EasyMock.expect(org.apache.ambari.server.api.query.render.ClusterBlueprintRendererTest.stack.getVersion()).andReturn("1.3.3").anyTimes();
        EasyMock.expect(org.apache.ambari.server.api.query.render.ClusterBlueprintRendererTest.stack.getConfiguration()).andReturn(org.apache.ambari.server.topology.Configuration.newEmpty()).anyTimes();
        EasyMock.expect(org.apache.ambari.server.api.query.render.ClusterBlueprintRendererTest.group1.getName()).andReturn("host_group_1").anyTimes();
        EasyMock.expect(org.apache.ambari.server.api.query.render.ClusterBlueprintRendererTest.group2.getName()).andReturn("host_group_2").anyTimes();
        EasyMock.expect(org.apache.ambari.server.api.query.render.ClusterBlueprintRendererTest.group1.getComponents()).andReturn(group1Components).anyTimes();
        EasyMock.expect(org.apache.ambari.server.api.query.render.ClusterBlueprintRendererTest.group2.getComponents()).andReturn(group2Components).anyTimes();
        EasyMock.expect(org.apache.ambari.server.api.query.render.ClusterBlueprintRendererTest.topology.getAmbariContext()).andReturn(org.apache.ambari.server.api.query.render.ClusterBlueprintRendererTest.ambariContext).anyTimes();
        EasyMock.expect(org.apache.ambari.server.api.query.render.ClusterBlueprintRendererTest.topology.getClusterId()).andReturn(1L).anyTimes();
        org.powermock.api.easymock.PowerMock.mockStatic(org.apache.ambari.server.controller.AmbariServer.class);
        EasyMock.expect(org.apache.ambari.server.controller.AmbariServer.getController()).andReturn(org.apache.ambari.server.api.query.render.ClusterBlueprintRendererTest.controller).anyTimes();
        org.powermock.api.easymock.PowerMock.replay(org.apache.ambari.server.controller.AmbariServer.class);
        EasyMock.expect(org.apache.ambari.server.api.query.render.ClusterBlueprintRendererTest.clusters.getCluster("clusterName")).andReturn(org.apache.ambari.server.api.query.render.ClusterBlueprintRendererTest.cluster).anyTimes();
        EasyMock.expect(org.apache.ambari.server.api.query.render.ClusterBlueprintRendererTest.controller.getKerberosHelper()).andReturn(org.apache.ambari.server.api.query.render.ClusterBlueprintRendererTest.kerberosHelper).anyTimes();
        EasyMock.expect(org.apache.ambari.server.api.query.render.ClusterBlueprintRendererTest.controller.getClusters()).andReturn(org.apache.ambari.server.api.query.render.ClusterBlueprintRendererTest.clusters).anyTimes();
        EasyMock.expect(org.apache.ambari.server.api.query.render.ClusterBlueprintRendererTest.kerberosHelper.getKerberosDescriptor(org.apache.ambari.server.api.query.render.ClusterBlueprintRendererTest.cluster, false)).andReturn(org.apache.ambari.server.api.query.render.ClusterBlueprintRendererTest.kerberosDescriptor).anyTimes();
        java.util.Set<java.lang.String> properties = new java.util.HashSet<>();
        properties.add("core-site/hadoop.security.auth_to_local");
        EasyMock.expect(org.apache.ambari.server.api.query.render.ClusterBlueprintRendererTest.kerberosDescriptor.getAllAuthToLocalProperties()).andReturn(properties).anyTimes();
        EasyMock.expect(org.apache.ambari.server.api.query.render.ClusterBlueprintRendererTest.ambariContext.getClusterName(1L)).andReturn("clusterName").anyTimes();
        EasyMock.replay(org.apache.ambari.server.api.query.render.ClusterBlueprintRendererTest.topology, org.apache.ambari.server.api.query.render.ClusterBlueprintRendererTest.blueprint, org.apache.ambari.server.api.query.render.ClusterBlueprintRendererTest.stack, org.apache.ambari.server.api.query.render.ClusterBlueprintRendererTest.group1, org.apache.ambari.server.api.query.render.ClusterBlueprintRendererTest.group2, org.apache.ambari.server.api.query.render.ClusterBlueprintRendererTest.ambariContext, org.apache.ambari.server.api.query.render.ClusterBlueprintRendererTest.clusters, org.apache.ambari.server.api.query.render.ClusterBlueprintRendererTest.controller, org.apache.ambari.server.api.query.render.ClusterBlueprintRendererTest.kerberosHelper, org.apache.ambari.server.api.query.render.ClusterBlueprintRendererTest.cluster, org.apache.ambari.server.api.query.render.ClusterBlueprintRendererTest.kerberosDescriptor);
    }

    private void setupMocksForKerberosEnabledCluster() throws java.lang.Exception {
        org.apache.ambari.server.topology.AmbariContext ambariContext = EasyMock.createNiceMock(org.apache.ambari.server.topology.AmbariContext.class);
        EasyMock.expect(ambariContext.getClusterName(EasyMock.anyLong())).andReturn("clusterName").anyTimes();
        org.powermock.api.easymock.PowerMock.mockStatic(org.apache.ambari.server.topology.AmbariContext.class);
        EasyMock.expect(org.apache.ambari.server.topology.AmbariContext.getClusterController()).andReturn(org.apache.ambari.server.api.query.render.ClusterBlueprintRendererTest.clusterController).anyTimes();
        EasyMock.expect(org.apache.ambari.server.topology.AmbariContext.getController()).andReturn(org.apache.ambari.server.api.query.render.ClusterBlueprintRendererTest.controller).anyTimes();
        EasyMock.reset(org.apache.ambari.server.api.query.render.ClusterBlueprintRendererTest.topology);
        org.apache.ambari.server.topology.HostGroupInfo group1Info = new org.apache.ambari.server.topology.HostGroupInfo("host_group_1");
        group1Info.addHost("host1");
        group1Info.setConfiguration(org.apache.ambari.server.api.query.render.ClusterBlueprintRendererTest.emptyConfiguration);
        org.apache.ambari.server.topology.HostGroupInfo group2Info = new org.apache.ambari.server.topology.HostGroupInfo("host_group_2");
        java.util.Map<java.lang.String, org.apache.ambari.server.topology.HostGroupInfo> groupInfoMap = new java.util.HashMap<>();
        group2Info.addHosts(java.util.Arrays.asList("host2", "host3"));
        group2Info.setConfiguration(org.apache.ambari.server.api.query.render.ClusterBlueprintRendererTest.emptyConfiguration);
        groupInfoMap.put("host_group_1", group1Info);
        groupInfoMap.put("host_group_2", group2Info);
        EasyMock.expect(org.apache.ambari.server.api.query.render.ClusterBlueprintRendererTest.topology.isNameNodeHAEnabled()).andReturn(false).anyTimes();
        EasyMock.expect(org.apache.ambari.server.api.query.render.ClusterBlueprintRendererTest.topology.getConfiguration()).andReturn(org.apache.ambari.server.api.query.render.ClusterBlueprintRendererTest.clusterConfig).anyTimes();
        EasyMock.expect(org.apache.ambari.server.api.query.render.ClusterBlueprintRendererTest.topology.getBlueprint()).andReturn(org.apache.ambari.server.api.query.render.ClusterBlueprintRendererTest.blueprint).anyTimes();
        EasyMock.expect(org.apache.ambari.server.api.query.render.ClusterBlueprintRendererTest.topology.getHostGroupInfo()).andReturn(groupInfoMap).anyTimes();
        EasyMock.expect(org.apache.ambari.server.api.query.render.ClusterBlueprintRendererTest.topology.getClusterId()).andReturn(1L).anyTimes();
        EasyMock.expect(org.apache.ambari.server.api.query.render.ClusterBlueprintRendererTest.topology.getAmbariContext()).andReturn(ambariContext).anyTimes();
        EasyMock.expect(org.apache.ambari.server.api.query.render.ClusterBlueprintRendererTest.topology.isClusterKerberosEnabled()).andReturn(true).anyTimes();
        org.apache.ambari.server.controller.spi.ResourceProvider resourceProvider = EasyMock.createStrictMock(org.apache.ambari.server.controller.spi.ResourceProvider.class);
        EasyMock.expect(org.apache.ambari.server.api.query.render.ClusterBlueprintRendererTest.clusterController.ensureResourceProvider(org.apache.ambari.server.controller.spi.Resource.Type.Artifact)).andReturn(resourceProvider).once();
        org.apache.ambari.server.controller.spi.Resource resource = EasyMock.createStrictMock(org.apache.ambari.server.controller.spi.Resource.class);
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> result = java.util.Collections.singleton(resource);
        EasyMock.expect(resourceProvider.getResources(EasyMock.anyObject(org.apache.ambari.server.controller.spi.Request.class), EasyMock.anyObject(org.apache.ambari.server.controller.spi.Predicate.class))).andReturn(result).once();
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.Object>> resourcePropertiesMap = new java.util.HashMap<>();
        resourcePropertiesMap.put(org.apache.ambari.server.controller.internal.ArtifactResourceProvider.ARTIFACT_DATA_PROPERTY, java.util.Collections.emptyMap());
        java.util.Map<java.lang.String, java.lang.Object> propertiesMap = new java.util.HashMap<>();
        propertiesMap.put("testProperty", "testValue");
        resourcePropertiesMap.put(org.apache.ambari.server.controller.internal.ArtifactResourceProvider.ARTIFACT_DATA_PROPERTY + "/properties", propertiesMap);
        EasyMock.expect(resource.getPropertiesMap()).andReturn(resourcePropertiesMap).once();
        org.powermock.api.easymock.PowerMock.replay(org.apache.ambari.server.topology.AmbariContext.class);
        EasyMock.replay(ambariContext, org.apache.ambari.server.api.query.render.ClusterBlueprintRendererTest.topology, org.apache.ambari.server.api.query.render.ClusterBlueprintRendererTest.clusterController, resource, resourceProvider);
    }

    @org.junit.After
    public void tearDown() {
        EasyMock.verify(org.apache.ambari.server.api.query.render.ClusterBlueprintRendererTest.topology, org.apache.ambari.server.api.query.render.ClusterBlueprintRendererTest.blueprint, org.apache.ambari.server.api.query.render.ClusterBlueprintRendererTest.stack, org.apache.ambari.server.api.query.render.ClusterBlueprintRendererTest.group1, org.apache.ambari.server.api.query.render.ClusterBlueprintRendererTest.group2, org.apache.ambari.server.api.query.render.ClusterBlueprintRendererTest.ambariContext, org.apache.ambari.server.api.query.render.ClusterBlueprintRendererTest.clusters, org.apache.ambari.server.api.query.render.ClusterBlueprintRendererTest.controller, org.apache.ambari.server.api.query.render.ClusterBlueprintRendererTest.kerberosHelper, org.apache.ambari.server.api.query.render.ClusterBlueprintRendererTest.cluster, org.apache.ambari.server.api.query.render.ClusterBlueprintRendererTest.kerberosDescriptor);
        EasyMock.reset(org.apache.ambari.server.api.query.render.ClusterBlueprintRendererTest.topology, org.apache.ambari.server.api.query.render.ClusterBlueprintRendererTest.blueprint, org.apache.ambari.server.api.query.render.ClusterBlueprintRendererTest.stack, org.apache.ambari.server.api.query.render.ClusterBlueprintRendererTest.group1, org.apache.ambari.server.api.query.render.ClusterBlueprintRendererTest.group2, org.apache.ambari.server.api.query.render.ClusterBlueprintRendererTest.ambariContext, org.apache.ambari.server.api.query.render.ClusterBlueprintRendererTest.clusters, org.apache.ambari.server.api.query.render.ClusterBlueprintRendererTest.controller, org.apache.ambari.server.api.query.render.ClusterBlueprintRendererTest.kerberosHelper, org.apache.ambari.server.api.query.render.ClusterBlueprintRendererTest.cluster, org.apache.ambari.server.api.query.render.ClusterBlueprintRendererTest.kerberosDescriptor);
    }

    @org.junit.Test
    public void testFinalizeProperties__instance() {
        org.apache.ambari.server.api.query.QueryInfo rootQuery = new org.apache.ambari.server.api.query.QueryInfo(new org.apache.ambari.server.api.resources.ClusterResourceDefinition(), new java.util.HashSet<>());
        org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.api.query.QueryInfo> queryTree = new org.apache.ambari.server.api.util.TreeNodeImpl<>(null, rootQuery, "Cluster");
        rootQuery.getProperties().add("foo/bar");
        rootQuery.getProperties().add("prop1");
        org.apache.ambari.server.api.query.QueryInfo hostInfo = new org.apache.ambari.server.api.query.QueryInfo(new org.apache.ambari.server.api.resources.HostResourceDefinition(), new java.util.HashSet<>());
        queryTree.addChild(hostInfo, "Host");
        org.apache.ambari.server.api.query.QueryInfo hostComponentInfo = new org.apache.ambari.server.api.query.QueryInfo(new org.apache.ambari.server.api.resources.HostComponentResourceDefinition(), new java.util.HashSet<>());
        queryTree.getChild("Host").addChild(hostComponentInfo, "HostComponent");
        org.apache.ambari.server.api.util.TreeNode<java.util.Set<java.lang.String>> propertyTree = fullRenderer.finalizeProperties(queryTree, false);
        java.util.Set<java.lang.String> rootProperties = propertyTree.getObject();
        org.junit.Assert.assertEquals(2, rootProperties.size());
        org.junit.Assert.assertNotNull(propertyTree.getChild("Host"));
        org.junit.Assert.assertTrue(propertyTree.getChild("Host").getObject().isEmpty());
        org.junit.Assert.assertNotNull(propertyTree.getChild("Host/HostComponent"));
        org.junit.Assert.assertEquals(1, propertyTree.getChild("Host/HostComponent").getObject().size());
        org.junit.Assert.assertTrue(propertyTree.getChild("Host/HostComponent").getObject().contains("HostRoles/component_name"));
    }

    @org.junit.Test
    public void clusterWithDefaultSettings() {
        org.apache.ambari.server.controller.internal.Stack stack = org.apache.ambari.server.api.query.render.ClusterBlueprintRendererTest.stackForSettingsTest();
        org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> clusterNode = org.apache.ambari.server.api.query.render.ClusterBlueprintRendererTest.clusterWith(stack, stack.getComponents(), org.apache.ambari.server.api.query.render.ClusterBlueprintRendererTest.defaultCredentialStoreSettings(), org.apache.ambari.server.api.query.render.ClusterBlueprintRendererTest.defaultRecoverySettings());
        java.util.Collection<java.util.Map<java.lang.String, java.lang.Object>> settings = fullRenderer.getSettings(clusterNode, stack);
        org.junit.Assert.assertEquals(com.google.common.collect.Lists.newArrayList(com.google.common.collect.ImmutableMap.of(org.apache.ambari.server.api.query.render.ClusterBlueprintRenderer.SERVICE_SETTINGS, com.google.common.collect.ImmutableSet.of()), com.google.common.collect.ImmutableMap.of(org.apache.ambari.server.api.query.render.ClusterBlueprintRenderer.COMPONENT_SETTINGS, com.google.common.collect.ImmutableSet.of())), settings);
        org.junit.Assert.assertEquals(com.google.common.collect.ImmutableList.of(), minimalRenderer.getSettings(clusterNode, stack));
    }

    @org.junit.Test
    public void clusterWithCustomSettings() {
        org.apache.ambari.server.controller.internal.Stack stack = org.apache.ambari.server.api.query.render.ClusterBlueprintRendererTest.stackForSettingsTest();
        org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> clusterNode = org.apache.ambari.server.api.query.render.ClusterBlueprintRendererTest.clusterWith(stack, stack.getComponents(), org.apache.ambari.server.api.query.render.ClusterBlueprintRendererTest.customCredentialStoreSettingFor(stack, "service1", "service2"), org.apache.ambari.server.api.query.render.ClusterBlueprintRendererTest.customRecoverySettingsFor(stack, "component1", "component2"));
        java.util.Collection<java.util.Map<java.lang.String, java.lang.Object>> settings = fullRenderer.getSettings(clusterNode, stack);
        org.junit.Assert.assertEquals(com.google.common.collect.Lists.newArrayList(com.google.common.collect.ImmutableMap.of(org.apache.ambari.server.api.query.render.ClusterBlueprintRenderer.SERVICE_SETTINGS, com.google.common.collect.ImmutableSet.of(com.google.common.collect.ImmutableMap.of("name", "service1", org.apache.ambari.server.api.query.render.ClusterBlueprintRenderer.CREDENTIAL_STORE_ENABLED, org.apache.ambari.server.api.query.render.ClusterBlueprintRenderer.FALSE), com.google.common.collect.ImmutableMap.of("name", "service2", org.apache.ambari.server.api.query.render.ClusterBlueprintRenderer.CREDENTIAL_STORE_ENABLED, org.apache.ambari.server.api.query.render.ClusterBlueprintRenderer.TRUE))), com.google.common.collect.ImmutableMap.of(org.apache.ambari.server.api.query.render.ClusterBlueprintRenderer.COMPONENT_SETTINGS, com.google.common.collect.ImmutableSet.of(com.google.common.collect.ImmutableMap.of("name", "component1", org.apache.ambari.server.api.query.render.ClusterBlueprintRenderer.RECOVERY_ENABLED, org.apache.ambari.server.api.query.render.ClusterBlueprintRenderer.FALSE), com.google.common.collect.ImmutableMap.of("name", "component2", org.apache.ambari.server.api.query.render.ClusterBlueprintRenderer.RECOVERY_ENABLED, org.apache.ambari.server.api.query.render.ClusterBlueprintRenderer.TRUE)))), settings);
        org.junit.Assert.assertEquals(settings, minimalRenderer.getSettings(clusterNode, stack));
    }

    @org.junit.Test
    public void clusterWithRecoveryDisabled() {
        org.apache.ambari.server.controller.internal.Stack stack = org.apache.ambari.server.api.query.render.ClusterBlueprintRendererTest.stackForSettingsTest();
        org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> clusterNode = org.apache.ambari.server.api.query.render.ClusterBlueprintRendererTest.clusterWith(stack, stack.getComponents(), org.apache.ambari.server.api.query.render.ClusterBlueprintRendererTest.defaultCredentialStoreSettings(), org.apache.ambari.server.api.query.render.ClusterBlueprintRendererTest.customRecoverySettingsFor(stack, "component1"));
        java.util.Collection<java.util.Map<java.lang.String, java.lang.Object>> settings = fullRenderer.getSettings(clusterNode, stack);
        org.junit.Assert.assertEquals(com.google.common.collect.Lists.newArrayList(com.google.common.collect.ImmutableMap.of(org.apache.ambari.server.api.query.render.ClusterBlueprintRenderer.SERVICE_SETTINGS, com.google.common.collect.ImmutableSet.of()), com.google.common.collect.ImmutableMap.of(org.apache.ambari.server.api.query.render.ClusterBlueprintRenderer.COMPONENT_SETTINGS, com.google.common.collect.ImmutableSet.of(com.google.common.collect.ImmutableMap.of("name", "component1", org.apache.ambari.server.api.query.render.ClusterBlueprintRenderer.RECOVERY_ENABLED, org.apache.ambari.server.api.query.render.ClusterBlueprintRenderer.FALSE)))), settings);
        org.junit.Assert.assertEquals(com.google.common.collect.Lists.newArrayList(com.google.common.collect.ImmutableMap.of(org.apache.ambari.server.api.query.render.ClusterBlueprintRenderer.COMPONENT_SETTINGS, com.google.common.collect.ImmutableSet.of(com.google.common.collect.ImmutableMap.of("name", "component1", org.apache.ambari.server.api.query.render.ClusterBlueprintRenderer.RECOVERY_ENABLED, org.apache.ambari.server.api.query.render.ClusterBlueprintRenderer.FALSE)))), minimalRenderer.getSettings(clusterNode, stack));
    }

    private static org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> clusterWith(org.apache.ambari.server.controller.internal.Stack stack, java.util.Map<java.lang.String, java.util.Collection<java.lang.String>> componentsByService, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> serviceSettings, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>> componentSettings) {
        org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> clusterTree = new org.apache.ambari.server.api.services.ResultImpl(true).getResultTree();
        org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> servicesTree = clusterTree.addChild(null, "services");
        servicesTree.setProperty("isCollection", "true");
        for (java.util.Map.Entry<java.lang.String, java.util.Collection<java.lang.String>> serviceEntry : componentsByService.entrySet()) {
            java.lang.String serviceName = serviceEntry.getKey();
            java.util.Optional<org.apache.ambari.server.state.ServiceInfo> serviceInfo = stack.getServiceInfo(serviceName);
            if (serviceInfo.isPresent()) {
                org.apache.ambari.server.controller.spi.Resource serviceResource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Service);
                serviceResource.setProperty("ServiceInfo/service_name", serviceName);
                serviceResource.setProperty("ServiceInfo/credential_store_supported", serviceInfo.get().isCredentialStoreSupported());
                serviceResource.setProperty("ServiceInfo/credential_store_enabled", serviceSettings.getOrDefault(serviceName, com.google.common.collect.ImmutableMap.of()).getOrDefault(org.apache.ambari.server.api.query.render.ClusterBlueprintRenderer.CREDENTIAL_STORE_ENABLED, java.lang.String.valueOf(serviceInfo.get().isCredentialStoreEnabled())));
                org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> serviceTree = servicesTree.addChild(serviceResource, serviceName);
                org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> componentsTree = serviceTree.addChild(null, "components");
                componentsTree.setProperty("isCollection", "true");
                for (java.lang.String componentName : serviceEntry.getValue()) {
                    org.apache.ambari.server.state.ComponentInfo componentInfo = serviceInfo.get().getComponentByName(componentName);
                    org.apache.ambari.server.controller.spi.Resource componentResource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Component);
                    componentResource.setProperty("ServiceComponentInfo/component_name", componentName);
                    componentResource.setProperty("ServiceComponentInfo/cluster_name", "testCluster");
                    componentResource.setProperty("ServiceComponentInfo/service_name", serviceName);
                    componentResource.setProperty("ServiceComponentInfo/recovery_enabled", componentSettings.getOrDefault(serviceName, com.google.common.collect.ImmutableMap.of()).getOrDefault(componentName, com.google.common.collect.ImmutableMap.of()).getOrDefault(org.apache.ambari.server.api.query.render.ClusterBlueprintRenderer.RECOVERY_ENABLED, java.lang.String.valueOf(componentInfo.isRecoveryEnabled())));
                    componentsTree.addChild(componentResource, componentName);
                }
            }
        }
        return clusterTree;
    }

    private static java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> defaultCredentialStoreSettings() {
        return com.google.common.collect.ImmutableMap.of();
    }

    private static java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> customCredentialStoreSettingFor(org.apache.ambari.server.controller.internal.Stack stack, java.lang.String... services) {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> result = new java.util.LinkedHashMap<>();
        for (java.lang.String service : services) {
            org.apache.ambari.server.state.ServiceInfo serviceInfo = stack.getServiceInfo(service).orElseThrow(java.lang.IllegalStateException::new);
            result.put(service, com.google.common.collect.ImmutableMap.of(org.apache.ambari.server.api.query.render.ClusterBlueprintRenderer.CREDENTIAL_STORE_ENABLED, java.lang.String.valueOf(!serviceInfo.isCredentialStoreEnabled())));
        }
        return result;
    }

    private static java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>> defaultRecoverySettings() {
        return com.google.common.collect.ImmutableMap.of();
    }

    private static java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>> customRecoverySettingsFor(org.apache.ambari.server.controller.internal.Stack stack, java.lang.String... components) {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>> result = new java.util.LinkedHashMap<>();
        for (java.lang.String component : components) {
            org.apache.ambari.server.state.ComponentInfo componentInfo = stack.getComponentInfo(component);
            java.lang.String service = stack.getServiceForComponent(component);
            result.computeIfAbsent(service, __ -> new java.util.HashMap<>()).put(component, com.google.common.collect.ImmutableMap.of(org.apache.ambari.server.api.query.render.ClusterBlueprintRenderer.RECOVERY_ENABLED, java.lang.String.valueOf(!componentInfo.isRecoveryEnabled())));
        }
        return result;
    }

    private static org.apache.ambari.server.controller.internal.Stack stackForSettingsTest() {
        org.apache.ambari.server.controller.internal.Stack stack = EasyMock.createNiceMock(org.apache.ambari.server.controller.internal.Stack.class);
        org.apache.ambari.server.state.ServiceInfo service1 = EasyMock.createNiceMock(org.apache.ambari.server.state.ServiceInfo.class);
        EasyMock.expect(service1.isCredentialStoreEnabled()).andReturn(true).anyTimes();
        EasyMock.expect(stack.getServiceInfo("service1")).andReturn(java.util.Optional.of(service1)).anyTimes();
        org.apache.ambari.server.state.ComponentInfo component1 = EasyMock.createNiceMock(org.apache.ambari.server.state.ComponentInfo.class);
        EasyMock.expect(component1.isRecoveryEnabled()).andReturn(true).anyTimes();
        EasyMock.expect(service1.getComponentByName("component1")).andReturn(component1).anyTimes();
        EasyMock.expect(stack.getServiceForComponent("component1")).andReturn("service1").anyTimes();
        EasyMock.expect(stack.getComponentInfo("component1")).andReturn(component1).anyTimes();
        org.apache.ambari.server.state.ComponentInfo component2 = EasyMock.createNiceMock(org.apache.ambari.server.state.ComponentInfo.class);
        EasyMock.expect(component2.isRecoveryEnabled()).andReturn(false).anyTimes();
        EasyMock.expect(service1.getComponentByName("component2")).andReturn(component2).anyTimes();
        EasyMock.expect(stack.getServiceForComponent("component2")).andReturn("service1").anyTimes();
        EasyMock.expect(stack.getComponentInfo("component2")).andReturn(component2).anyTimes();
        EasyMock.expect(service1.getComponents()).andReturn(com.google.common.collect.ImmutableList.of(component1, component2)).anyTimes();
        org.apache.ambari.server.state.ServiceInfo service2 = EasyMock.createNiceMock(org.apache.ambari.server.state.ServiceInfo.class);
        EasyMock.expect(service2.isCredentialStoreEnabled()).andReturn(false).anyTimes();
        EasyMock.expect(stack.getServiceInfo("service2")).andReturn(java.util.Optional.of(service2)).anyTimes();
        org.apache.ambari.server.state.ComponentInfo component3 = EasyMock.createNiceMock(org.apache.ambari.server.state.ComponentInfo.class);
        EasyMock.expect(component3.isRecoveryEnabled()).andReturn(false).anyTimes();
        EasyMock.expect(service2.getComponentByName("component3")).andReturn(component3).anyTimes();
        EasyMock.expect(stack.getServiceForComponent("component3")).andReturn("service2").anyTimes();
        EasyMock.expect(stack.getComponentInfo("component3")).andReturn(component3).anyTimes();
        EasyMock.expect(service2.getComponents()).andReturn(com.google.common.collect.ImmutableList.of(component3)).anyTimes();
        EasyMock.expect(stack.getComponents()).andReturn(com.google.common.collect.ImmutableMap.of("service1", com.google.common.collect.ImmutableSet.of("component1", "component2"), "service2", com.google.common.collect.ImmutableSet.of("component3"))).anyTimes();
        EasyMock.replay(stack, service1, service2, component1, component2, component3);
        return stack;
    }

    @org.junit.Test
    public void testFinalizeProperties__instance_noComponentNode() {
        org.apache.ambari.server.api.query.QueryInfo rootQuery = new org.apache.ambari.server.api.query.QueryInfo(new org.apache.ambari.server.api.resources.ClusterResourceDefinition(), new java.util.HashSet<>());
        org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.api.query.QueryInfo> queryTree = new org.apache.ambari.server.api.util.TreeNodeImpl<>(null, rootQuery, "Cluster");
        rootQuery.getProperties().add("foo/bar");
        rootQuery.getProperties().add("prop1");
        org.apache.ambari.server.api.util.TreeNode<java.util.Set<java.lang.String>> propertyTree = fullRenderer.finalizeProperties(queryTree, false);
        java.util.Set<java.lang.String> rootProperties = propertyTree.getObject();
        org.junit.Assert.assertEquals(2, rootProperties.size());
        org.junit.Assert.assertNotNull(propertyTree.getChild("Host"));
        org.junit.Assert.assertTrue(propertyTree.getChild("Host").getObject().isEmpty());
        org.junit.Assert.assertNotNull(propertyTree.getChild("Host/HostComponent"));
        org.junit.Assert.assertEquals(1, propertyTree.getChild("Host/HostComponent").getObject().size());
        org.junit.Assert.assertTrue(propertyTree.getChild("Host/HostComponent").getObject().contains("HostRoles/component_name"));
    }

    @org.junit.Test
    public void testFinalizeResult_kerberos() throws java.lang.Exception {
        setupMocksForKerberosEnabledCluster();
        org.apache.ambari.server.api.services.Result result = new org.apache.ambari.server.api.services.ResultImpl(true);
        createClusterResultTree(result.getResultTree());
        org.apache.ambari.server.api.query.render.ClusterBlueprintRenderer renderer = new org.apache.ambari.server.api.query.render.ClusterBlueprintRendererTest.TestBlueprintRenderer(org.apache.ambari.server.api.query.render.ClusterBlueprintRendererTest.topology, org.apache.ambari.server.controller.internal.BlueprintExportType.FULL);
        org.apache.ambari.server.api.services.Result blueprintResult = renderer.finalizeResult(result);
        org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> blueprintTree = blueprintResult.getResultTree();
        org.junit.Assert.assertNull(blueprintTree.getStringProperty("isCollection"));
        org.junit.Assert.assertEquals(1, blueprintTree.getChildren().size());
        org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> blueprintNode = blueprintTree.getChildren().iterator().next();
        org.junit.Assert.assertEquals(0, blueprintNode.getChildren().size());
        org.apache.ambari.server.controller.spi.Resource blueprintResource = blueprintNode.getObject();
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.Object>> properties = blueprintResource.getPropertiesMap();
        org.junit.Assert.assertEquals("HDP", properties.get("Blueprints").get("stack_name"));
        org.junit.Assert.assertEquals("1.3.3", properties.get("Blueprints").get("stack_version"));
        java.util.Map<java.lang.String, java.lang.Object> securityProperties = ((java.util.Map<java.lang.String, java.lang.Object>) (properties.get("Blueprints").get("security")));
        org.junit.Assert.assertEquals("KERBEROS", securityProperties.get("type"));
        org.junit.Assert.assertNotNull(((java.util.Map<java.lang.String, java.lang.Object>) (securityProperties.get("kerberos_descriptor"))).get("properties"));
    }

    @org.junit.Test
    public void testFinalizeResult() throws java.lang.Exception {
        org.apache.ambari.server.api.services.Result result = new org.apache.ambari.server.api.services.ResultImpl(true);
        createClusterResultTree(result.getResultTree());
        org.apache.ambari.server.api.query.render.ClusterBlueprintRenderer renderer = new org.apache.ambari.server.api.query.render.ClusterBlueprintRendererTest.TestBlueprintRenderer(org.apache.ambari.server.api.query.render.ClusterBlueprintRendererTest.topology, org.apache.ambari.server.controller.internal.BlueprintExportType.FULL);
        org.apache.ambari.server.api.services.Result blueprintResult = renderer.finalizeResult(result);
        org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> blueprintTree = blueprintResult.getResultTree();
        org.junit.Assert.assertNull(blueprintTree.getStringProperty("isCollection"));
        org.junit.Assert.assertEquals(1, blueprintTree.getChildren().size());
        org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> blueprintNode = blueprintTree.getChildren().iterator().next();
        org.junit.Assert.assertEquals(0, blueprintNode.getChildren().size());
        org.apache.ambari.server.controller.spi.Resource blueprintResource = blueprintNode.getObject();
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.Object>> properties = blueprintResource.getPropertiesMap();
        org.junit.Assert.assertEquals("HDP", properties.get("Blueprints").get("stack_name"));
        org.junit.Assert.assertEquals("1.3.3", properties.get("Blueprints").get("stack_version"));
        java.util.Collection<java.util.Map<java.lang.String, java.lang.Object>> host_groups = ((java.util.Collection<java.util.Map<java.lang.String, java.lang.Object>>) (properties.get("").get("host_groups")));
        org.junit.Assert.assertEquals(2, host_groups.size());
        for (java.util.Map<java.lang.String, java.lang.Object> hostGroupProperties : host_groups) {
            java.lang.String host_group_name = ((java.lang.String) (hostGroupProperties.get("name")));
            if (host_group_name.equals("host_group_1")) {
                org.junit.Assert.assertEquals("1", hostGroupProperties.get("cardinality"));
                java.util.Collection<java.util.Map<java.lang.String, java.lang.String>> components = ((java.util.Collection<java.util.Map<java.lang.String, java.lang.String>>) (hostGroupProperties.get("components")));
                org.junit.Assert.assertEquals(5, components.size());
                java.util.Set<java.lang.String> expectedValues = com.google.common.collect.ImmutableSet.of("JOBTRACKER", "TASKTRACKER", "NAMENODE", "DATANODE", "AMBARI_SERVER");
                java.util.Set<java.lang.String> actualValues = new java.util.HashSet<>();
                for (java.util.Map<java.lang.String, java.lang.String> componentProperties : components) {
                    org.junit.Assert.assertEquals(1, componentProperties.size());
                    actualValues.add(componentProperties.get("name"));
                }
                org.junit.Assert.assertEquals(expectedValues, actualValues);
            } else if (host_group_name.equals("host_group_2")) {
                org.junit.Assert.assertEquals("2", hostGroupProperties.get("cardinality"));
                java.util.Collection<java.util.Map<java.lang.String, java.lang.String>> components = ((java.util.Collection<java.util.Map<java.lang.String, java.lang.String>>) (hostGroupProperties.get("components")));
                org.junit.Assert.assertEquals(2, components.size());
                java.util.Set<java.lang.String> expectedValues = com.google.common.collect.ImmutableSet.of("TASKTRACKER", "DATANODE");
                java.util.Set<java.lang.String> actualValues = new java.util.HashSet<>();
                for (java.util.Map<java.lang.String, java.lang.String> componentProperties : components) {
                    org.junit.Assert.assertEquals(1, componentProperties.size());
                    actualValues.add(componentProperties.get("name"));
                }
                org.junit.Assert.assertEquals(expectedValues, actualValues);
            }
        }
    }

    @org.junit.Test
    public void testFinalizeResultWithAttributes() throws java.lang.Exception {
        org.apache.ambari.server.state.ServiceInfo hdfsService = new org.apache.ambari.server.state.ServiceInfo();
        hdfsService.setName("HDFS");
        org.apache.ambari.server.state.ServiceInfo mrService = new org.apache.ambari.server.state.ServiceInfo();
        mrService.setName("MAPREDUCE");
        org.apache.ambari.server.api.services.Result result = new org.apache.ambari.server.api.services.ResultImpl(true);
        java.util.Map<java.lang.String, java.lang.Object> testDesiredConfigMap = new java.util.HashMap<>();
        org.apache.ambari.server.state.DesiredConfig testDesiredConfig = new org.apache.ambari.server.state.DesiredConfig();
        testDesiredConfig.setTag("test-tag-one");
        testDesiredConfigMap.put("test-type-one", testDesiredConfig);
        createClusterResultTree(result.getResultTree(), testDesiredConfigMap);
        org.apache.ambari.server.api.query.render.ClusterBlueprintRenderer renderer = new org.apache.ambari.server.api.query.render.ClusterBlueprintRendererTest.TestBlueprintRenderer(org.apache.ambari.server.api.query.render.ClusterBlueprintRendererTest.topology, org.apache.ambari.server.controller.internal.BlueprintExportType.FULL);
        org.apache.ambari.server.api.services.Result blueprintResult = renderer.finalizeResult(result);
        org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> blueprintTree = blueprintResult.getResultTree();
        org.junit.Assert.assertNull(blueprintTree.getStringProperty("isCollection"));
        org.junit.Assert.assertEquals(1, blueprintTree.getChildren().size());
        org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> blueprintNode = blueprintTree.getChildren().iterator().next();
        org.junit.Assert.assertEquals(0, blueprintNode.getChildren().size());
        org.apache.ambari.server.controller.spi.Resource blueprintResource = blueprintNode.getObject();
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.Object>> properties = blueprintResource.getPropertiesMap();
        org.junit.Assert.assertEquals("HDP", properties.get("Blueprints").get("stack_name"));
        org.junit.Assert.assertEquals("1.3.3", properties.get("Blueprints").get("stack_version"));
        java.util.Collection<java.util.Map<java.lang.String, java.lang.Object>> host_groups = ((java.util.Collection<java.util.Map<java.lang.String, java.lang.Object>>) (properties.get("").get("host_groups")));
        org.junit.Assert.assertEquals(2, host_groups.size());
        for (java.util.Map<java.lang.String, java.lang.Object> hostGroupProperties : host_groups) {
            java.lang.String host_group_name = ((java.lang.String) (hostGroupProperties.get("name")));
            if (host_group_name.equals("host_group_1")) {
                org.junit.Assert.assertEquals("1", hostGroupProperties.get("cardinality"));
                java.util.Collection<java.util.Map<java.lang.String, java.lang.String>> components = ((java.util.Collection<java.util.Map<java.lang.String, java.lang.String>>) (hostGroupProperties.get("components")));
                org.junit.Assert.assertEquals(5, components.size());
                java.util.Set<java.lang.String> expectedValues = com.google.common.collect.ImmutableSet.of("JOBTRACKER", "TASKTRACKER", "NAMENODE", "DATANODE", "AMBARI_SERVER");
                java.util.Set<java.lang.String> actualValues = new java.util.HashSet<>();
                for (java.util.Map<java.lang.String, java.lang.String> componentProperties : components) {
                    org.junit.Assert.assertEquals(1, componentProperties.size());
                    actualValues.add(componentProperties.get("name"));
                }
                org.junit.Assert.assertEquals(expectedValues, actualValues);
            } else if (host_group_name.equals("host_group_2")) {
                org.junit.Assert.assertEquals("2", hostGroupProperties.get("cardinality"));
                java.util.Collection<java.util.Map<java.lang.String, java.lang.String>> components = ((java.util.Collection<java.util.Map<java.lang.String, java.lang.String>>) (hostGroupProperties.get("components")));
                org.junit.Assert.assertEquals(2, components.size());
                java.util.Set<java.lang.String> expectedValues = com.google.common.collect.ImmutableSet.of("TASKTRACKER", "DATANODE");
                java.util.Set<java.lang.String> actualValues = new java.util.HashSet<>();
                for (java.util.Map<java.lang.String, java.lang.String> componentProperties : components) {
                    org.junit.Assert.assertEquals(1, componentProperties.size());
                    actualValues.add(componentProperties.get("name"));
                }
                org.junit.Assert.assertEquals(expectedValues, actualValues);
            }
        }
        java.util.List<java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, ?>>>> configurationsResult = ((java.util.List<java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, ?>>>>) (blueprintResource.getPropertyValue("configurations")));
        org.junit.Assert.assertEquals("Incorrect number of config maps added", 1, configurationsResult.size());
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, ?>> configMap = configurationsResult.iterator().next().get("test-type-one");
        org.junit.Assert.assertNotNull("Expected config map was not included", configMap);
        org.junit.Assert.assertEquals("Incorrect number of maps added under expected type", 2, configMap.size());
        org.junit.Assert.assertTrue("Expected properties map was not found", configMap.containsKey("properties"));
        org.junit.Assert.assertTrue("Expected properties_attributes map was not found", configMap.containsKey("properties_attributes"));
        java.util.Map<java.lang.String, ?> propertiesResult = configMap.get("properties");
        org.junit.Assert.assertEquals("Incorrect number of config properties found", 1, propertiesResult.size());
        java.util.Map<java.lang.String, ?> attributesResult = configMap.get("properties_attributes");
        org.junit.Assert.assertEquals("Incorrect number of config attributes found", 1, attributesResult.size());
        org.junit.Assert.assertEquals("Incorrect property value included", "valueOne", propertiesResult.get("propertyOne"));
        org.junit.Assert.assertNotNull("Expected attribute not found in exported Blueprint", attributesResult.get("final"));
        org.junit.Assert.assertTrue("Attribute type map was not included", attributesResult.get("final") instanceof java.util.Map);
        java.util.Map<java.lang.String, ?> finalMap = ((java.util.Map<java.lang.String, ?>) (attributesResult.get("final")));
        org.junit.Assert.assertEquals("Attribute value is not correct", "true", finalMap.get("propertyOne"));
    }

    @org.junit.Test
    public void testClusterRendererDefaults() {
        org.junit.Assert.assertFalse("ClusterBlueprintRenderer should not require property provider input", fullRenderer.requiresPropertyProviderInput());
    }

    private void createClusterResultTree(org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> resultTree) throws java.lang.Exception {
        createClusterResultTree(resultTree, null);
    }

    private void createClusterResultTree(org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> resultTree, final java.util.Map<java.lang.String, java.lang.Object> desiredConfig) throws java.lang.Exception {
        org.apache.ambari.server.controller.spi.Resource clusterResource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Cluster) {
            @java.lang.Override
            public java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.Object>> getPropertiesMap() {
                java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.Object>> originalMap = super.getPropertiesMap();
                if (desiredConfig == null) {
                    originalMap.put("Clusters/desired_configs", java.util.Collections.emptyMap());
                } else {
                    originalMap.put("Clusters/desired_configs", desiredConfig);
                }
                return originalMap;
            }
        };
        clusterResource.setProperty("Clusters/cluster_name", "testCluster");
        clusterResource.setProperty("Clusters/version", "HDP-1.3.3");
        org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> clusterTree = resultTree.addChild(clusterResource, "Cluster:1");
        org.apache.ambari.server.controller.spi.Resource servicesResource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Service);
        clusterTree.addChild(servicesResource, "services");
        org.apache.ambari.server.controller.spi.Resource configurationsResource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Configuration);
        org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> configurations = clusterTree.addChild(configurationsResource, "configurations");
        org.apache.ambari.server.controller.spi.Resource resourceOne = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Configuration) {
            @java.lang.Override
            public java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.Object>> getPropertiesMap() {
                java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.Object>> originalMap = super.getPropertiesMap();
                originalMap.put("properties", null);
                return originalMap;
            }
        };
        resourceOne.setProperty("type", "mapreduce-log4j");
        configurations.addChild(resourceOne, "resourceOne");
        org.apache.ambari.server.controller.spi.Resource resourceTwo = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Configuration) {
            @java.lang.Override
            public java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.Object>> getPropertiesMap() {
                java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.Object>> originalMap = super.getPropertiesMap();
                originalMap.put("properties", java.util.Collections.singletonMap("propertyOne", "valueOne"));
                originalMap.put("properties_attributes", java.util.Collections.singletonMap("final", java.util.Collections.singletonMap("propertyOne", "true")));
                return originalMap;
            }
        };
        resourceTwo.setProperty("type", "test-type-one");
        resourceTwo.setProperty("tag", "test-tag-one");
        configurations.addChild(resourceTwo, "resourceTwo");
        org.apache.ambari.server.controller.spi.Resource blueprintOne = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Blueprint);
        blueprintOne.setProperty("Blueprints/blueprint_name", "blueprint-testCluster");
        clusterTree.addChild(blueprintOne, "Blueprints");
        org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> hostsTree = clusterTree.addChild(null, "hosts");
        hostsTree.setProperty("isCollection", "true");
        org.apache.ambari.server.controller.spi.Resource hostResource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Host);
        hostResource.setProperty("Hosts/host_name", getLocalHostName());
        org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> hostTree = hostsTree.addChild(hostResource, "Host:1");
        org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> hostComponentsTree = hostTree.addChild(null, "host_components");
        hostComponentsTree.setProperty("isCollection", "true");
        org.apache.ambari.server.controller.spi.Resource nnComponentResource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent);
        nnComponentResource.setProperty("HostRoles/component_name", "NAMENODE");
        org.apache.ambari.server.controller.spi.Resource dnComponentResource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent);
        dnComponentResource.setProperty("HostRoles/component_name", "DATANODE");
        org.apache.ambari.server.controller.spi.Resource jtComponentResource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent);
        jtComponentResource.setProperty("HostRoles/component_name", "JOBTRACKER");
        org.apache.ambari.server.controller.spi.Resource ttComponentResource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent);
        ttComponentResource.setProperty("HostRoles/component_name", "TASKTRACKER");
        hostComponentsTree.addChild(nnComponentResource, "HostComponent:1");
        hostComponentsTree.addChild(dnComponentResource, "HostComponent:2");
        hostComponentsTree.addChild(jtComponentResource, "HostComponent:3");
        hostComponentsTree.addChild(ttComponentResource, "HostComponent:4");
        org.apache.ambari.server.controller.spi.Resource host2Resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Host);
        host2Resource.setProperty("Hosts/host_name", "testHost2");
        org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> host2Tree = hostsTree.addChild(host2Resource, "Host:2");
        org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> host2ComponentsTree = host2Tree.addChild(null, "host_components");
        host2ComponentsTree.setProperty("isCollection", "true");
        host2ComponentsTree.addChild(dnComponentResource, "HostComponent:1");
        host2ComponentsTree.addChild(ttComponentResource, "HostComponent:2");
        org.apache.ambari.server.controller.spi.Resource host3Resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Host);
        host3Resource.setProperty("Hosts/host_name", "testHost3");
        org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> host3Tree = hostsTree.addChild(host3Resource, "Host:3");
        org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> host3ComponentsTree = host3Tree.addChild(null, "host_components");
        host3ComponentsTree.setProperty("isCollection", "true");
        host3ComponentsTree.addChild(dnComponentResource, "HostComponent:1");
        host3ComponentsTree.addChild(ttComponentResource, "HostComponent:2");
    }

    private java.lang.String getLocalHostName() throws java.net.UnknownHostException {
        return java.net.InetAddress.getLocalHost().getHostName();
    }

    private static class TestBlueprintRenderer extends org.apache.ambari.server.api.query.render.ClusterBlueprintRenderer {
        private org.apache.ambari.server.topology.ClusterTopology topology;

        TestBlueprintRenderer(org.apache.ambari.server.topology.ClusterTopology topology, org.apache.ambari.server.controller.internal.BlueprintExportType exportType) {
            super(exportType);
            this.topology = topology;
        }

        @java.lang.Override
        protected org.apache.ambari.server.topology.ClusterTopology createClusterTopology(org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> clusterNode) {
            return topology;
        }
    }
}