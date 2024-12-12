package org.apache.ambari.server.api.services.stackadvisor;
import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.reset;
public class StackAdvisorBlueprintProcessorTest {
    private org.apache.ambari.server.api.services.stackadvisor.StackAdvisorBlueprintProcessor underTest = new org.apache.ambari.server.api.services.stackadvisor.StackAdvisorBlueprintProcessor();

    private org.apache.ambari.server.topology.ClusterTopology clusterTopology = EasyMock.createMock(org.apache.ambari.server.topology.ClusterTopology.class);

    private org.apache.ambari.server.topology.BlueprintImpl blueprint = EasyMock.createMock(org.apache.ambari.server.topology.BlueprintImpl.class);

    private org.apache.ambari.server.controller.internal.Stack stack = EasyMock.createMock(org.apache.ambari.server.controller.internal.Stack.class);

    private org.apache.ambari.server.topology.HostGroup hostGroup = EasyMock.createMock(org.apache.ambari.server.topology.HostGroup.class);

    private org.apache.ambari.server.topology.Configuration configuration = EasyMock.createMock(org.apache.ambari.server.topology.Configuration.class);

    private static org.apache.ambari.server.api.services.stackadvisor.StackAdvisorHelper stackAdvisorHelper = EasyMock.createMock(org.apache.ambari.server.api.services.stackadvisor.StackAdvisorHelper.class);

    @org.junit.BeforeClass
    public static void initClass() {
        org.apache.ambari.server.api.services.stackadvisor.StackAdvisorBlueprintProcessor.init(org.apache.ambari.server.api.services.stackadvisor.StackAdvisorBlueprintProcessorTest.stackAdvisorHelper);
    }

    @org.junit.Before
    public void setUp() {
        EasyMock.reset(clusterTopology, blueprint, stack, org.apache.ambari.server.api.services.stackadvisor.StackAdvisorBlueprintProcessorTest.stackAdvisorHelper);
    }

    @org.junit.Test
    public void testAdviseConfiguration() throws org.apache.ambari.server.api.services.stackadvisor.StackAdvisorException, org.apache.ambari.server.controller.internal.ConfigurationTopologyException, org.apache.ambari.server.AmbariException {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> props = createProps();
        java.util.Map<java.lang.String, org.apache.ambari.server.topology.AdvisedConfiguration> advisedConfigurations = new java.util.HashMap<>();
        EasyMock.expect(clusterTopology.getBlueprint()).andReturn(blueprint).anyTimes();
        EasyMock.expect(clusterTopology.getHostGroupInfo()).andReturn(createHostGroupInfo()).anyTimes();
        EasyMock.expect(clusterTopology.getAdvisedConfigurations()).andReturn(advisedConfigurations).anyTimes();
        EasyMock.expect(clusterTopology.getConfiguration()).andReturn(configuration).anyTimes();
        EasyMock.expect(clusterTopology.isClusterKerberosEnabled()).andReturn(false).anyTimes();
        EasyMock.expect(clusterTopology.getConfigRecommendationStrategy()).andReturn(org.apache.ambari.server.topology.ConfigRecommendationStrategy.ALWAYS_APPLY).anyTimes();
        EasyMock.expect(blueprint.getStack()).andReturn(stack).anyTimes();
        EasyMock.expect(stack.getVersion()).andReturn("2.3").anyTimes();
        EasyMock.expect(stack.getName()).andReturn("HDP").anyTimes();
        EasyMock.expect(stack.getConfiguration(java.util.Arrays.asList("HDFS", "YARN", "HIVE"))).andReturn(createStackDefaults()).anyTimes();
        EasyMock.expect(blueprint.getServices()).andReturn(java.util.Arrays.asList("HDFS", "YARN", "HIVE")).anyTimes();
        EasyMock.expect(blueprint.getHostGroups()).andReturn(createHostGroupMap()).anyTimes();
        EasyMock.expect(blueprint.isValidConfigType("core-site")).andReturn(true).anyTimes();
        EasyMock.expect(hostGroup.getComponentNames()).andReturn(java.util.Arrays.asList("comp1", "comp2")).anyTimes();
        EasyMock.expect(org.apache.ambari.server.api.services.stackadvisor.StackAdvisorBlueprintProcessorTest.stackAdvisorHelper.recommend(EasyMock.anyObject(org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequest.class))).andReturn(createRecommendationResponse());
        EasyMock.expect(configuration.getFullProperties()).andReturn(props).anyTimes();
        EasyMock.replay(clusterTopology, blueprint, stack, hostGroup, configuration, org.apache.ambari.server.api.services.stackadvisor.StackAdvisorBlueprintProcessorTest.stackAdvisorHelper);
        underTest.adviseConfiguration(clusterTopology, props);
        org.junit.Assert.assertTrue(advisedConfigurations.get("core-site").getProperties().containsKey("dummyKey1"));
        org.junit.Assert.assertTrue(advisedConfigurations.get("core-site").getProperties().containsKey("dummyKey3"));
        org.junit.Assert.assertTrue(advisedConfigurations.get("core-site").getPropertyValueAttributes().containsKey("dummyKey2"));
        org.junit.Assert.assertTrue(advisedConfigurations.get("core-site").getPropertyValueAttributes().containsKey("dummyKey3"));
        org.junit.Assert.assertEquals("dummyValue", advisedConfigurations.get("core-site").getProperties().get("dummyKey1"));
        org.junit.Assert.assertEquals(java.lang.Boolean.toString(true), advisedConfigurations.get("core-site").getPropertyValueAttributes().get("dummyKey2").getDelete());
    }

    @org.junit.Test
    public void testAdviseConfigurationWithOnlyStackDefaultsApply() throws org.apache.ambari.server.api.services.stackadvisor.StackAdvisorException, org.apache.ambari.server.controller.internal.ConfigurationTopologyException, org.apache.ambari.server.AmbariException {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> props = createProps();
        java.util.Map<java.lang.String, org.apache.ambari.server.topology.AdvisedConfiguration> advisedConfigurations = new java.util.HashMap<>();
        EasyMock.expect(clusterTopology.getBlueprint()).andReturn(blueprint).anyTimes();
        EasyMock.expect(clusterTopology.getHostGroupInfo()).andReturn(createHostGroupInfo()).anyTimes();
        EasyMock.expect(clusterTopology.getAdvisedConfigurations()).andReturn(advisedConfigurations).anyTimes();
        EasyMock.expect(clusterTopology.getConfiguration()).andReturn(configuration).anyTimes();
        EasyMock.expect(clusterTopology.isClusterKerberosEnabled()).andReturn(false).anyTimes();
        EasyMock.expect(clusterTopology.getConfigRecommendationStrategy()).andReturn(org.apache.ambari.server.topology.ConfigRecommendationStrategy.ONLY_STACK_DEFAULTS_APPLY);
        EasyMock.expect(blueprint.getStack()).andReturn(stack).anyTimes();
        EasyMock.expect(stack.getVersion()).andReturn("2.3").anyTimes();
        EasyMock.expect(stack.getName()).andReturn("HDP").anyTimes();
        EasyMock.expect(stack.getConfiguration(java.util.Arrays.asList("HDFS", "YARN", "HIVE"))).andReturn(createStackDefaults()).anyTimes();
        EasyMock.expect(blueprint.getServices()).andReturn(java.util.Arrays.asList("HDFS", "YARN", "HIVE")).anyTimes();
        EasyMock.expect(blueprint.getHostGroups()).andReturn(createHostGroupMap()).anyTimes();
        EasyMock.expect(blueprint.isValidConfigType("core-site")).andReturn(true).anyTimes();
        EasyMock.expect(hostGroup.getComponentNames()).andReturn(java.util.Arrays.asList("comp1", "comp2")).anyTimes();
        EasyMock.expect(org.apache.ambari.server.api.services.stackadvisor.StackAdvisorBlueprintProcessorTest.stackAdvisorHelper.recommend(EasyMock.anyObject(org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequest.class))).andReturn(createRecommendationResponse());
        EasyMock.expect(configuration.getFullProperties()).andReturn(props).anyTimes();
        EasyMock.replay(clusterTopology, blueprint, stack, hostGroup, configuration, org.apache.ambari.server.api.services.stackadvisor.StackAdvisorBlueprintProcessorTest.stackAdvisorHelper);
        underTest.adviseConfiguration(clusterTopology, props);
        org.junit.Assert.assertTrue(advisedConfigurations.get("core-site").getProperties().containsKey("dummyKey1"));
        org.junit.Assert.assertFalse(advisedConfigurations.get("core-site").getProperties().containsKey("dummyKey3"));
        org.junit.Assert.assertTrue(advisedConfigurations.get("core-site").getPropertyValueAttributes().containsKey("dummyKey2"));
        org.junit.Assert.assertFalse(advisedConfigurations.get("core-site").getPropertyValueAttributes().containsKey("dummyKey3"));
        org.junit.Assert.assertEquals("dummyValue", advisedConfigurations.get("core-site").getProperties().get("dummyKey1"));
        org.junit.Assert.assertEquals(java.lang.Boolean.toString(true), advisedConfigurations.get("core-site").getPropertyValueAttributes().get("dummyKey2").getDelete());
    }

    @org.junit.Test
    public void testAdviseConfigurationWithOnlyStackDefaultsApplyWhenNoUserInputForDefault() throws org.apache.ambari.server.api.services.stackadvisor.StackAdvisorException, org.apache.ambari.server.controller.internal.ConfigurationTopologyException, org.apache.ambari.server.AmbariException {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> props = createProps();
        props.get("core-site").put("dummyKey3", "stackDefaultValue");
        java.util.Map<java.lang.String, org.apache.ambari.server.topology.AdvisedConfiguration> advisedConfigurations = new java.util.HashMap<>();
        EasyMock.expect(clusterTopology.getBlueprint()).andReturn(blueprint).anyTimes();
        EasyMock.expect(clusterTopology.getHostGroupInfo()).andReturn(createHostGroupInfo()).anyTimes();
        EasyMock.expect(clusterTopology.getAdvisedConfigurations()).andReturn(advisedConfigurations).anyTimes();
        EasyMock.expect(clusterTopology.getConfiguration()).andReturn(configuration).anyTimes();
        EasyMock.expect(clusterTopology.isClusterKerberosEnabled()).andReturn(false).anyTimes();
        EasyMock.expect(clusterTopology.getConfigRecommendationStrategy()).andReturn(org.apache.ambari.server.topology.ConfigRecommendationStrategy.ONLY_STACK_DEFAULTS_APPLY);
        EasyMock.expect(blueprint.getStack()).andReturn(stack).anyTimes();
        EasyMock.expect(stack.getVersion()).andReturn("2.3").anyTimes();
        EasyMock.expect(stack.getName()).andReturn("HDP").anyTimes();
        EasyMock.expect(stack.getConfiguration(java.util.Arrays.asList("HDFS", "YARN", "HIVE"))).andReturn(createStackDefaults()).anyTimes();
        EasyMock.expect(blueprint.getServices()).andReturn(java.util.Arrays.asList("HDFS", "YARN", "HIVE")).anyTimes();
        EasyMock.expect(blueprint.getHostGroups()).andReturn(createHostGroupMap()).anyTimes();
        EasyMock.expect(blueprint.isValidConfigType("core-site")).andReturn(true).anyTimes();
        EasyMock.expect(hostGroup.getComponentNames()).andReturn(java.util.Arrays.asList("comp1", "comp2")).anyTimes();
        EasyMock.expect(org.apache.ambari.server.api.services.stackadvisor.StackAdvisorBlueprintProcessorTest.stackAdvisorHelper.recommend(EasyMock.anyObject(org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequest.class))).andReturn(createRecommendationResponse());
        EasyMock.expect(configuration.getFullProperties()).andReturn(props).anyTimes();
        EasyMock.replay(clusterTopology, blueprint, stack, hostGroup, configuration, org.apache.ambari.server.api.services.stackadvisor.StackAdvisorBlueprintProcessorTest.stackAdvisorHelper);
        underTest.adviseConfiguration(clusterTopology, props);
        org.junit.Assert.assertTrue(advisedConfigurations.get("core-site").getProperties().containsKey("dummyKey1"));
        org.junit.Assert.assertTrue(advisedConfigurations.get("core-site").getPropertyValueAttributes().containsKey("dummyKey2"));
        org.junit.Assert.assertEquals("dummyValue", advisedConfigurations.get("core-site").getProperties().get("dummyKey1"));
        org.junit.Assert.assertEquals(java.lang.Boolean.toString(true), advisedConfigurations.get("core-site").getPropertyValueAttributes().get("dummyKey2").getDelete());
    }

    @org.junit.Test
    public void testAdviseConfigurationWith_ALWAYS_APPLY_DONT_OVERRIDE_CUSTOM_VALUES() throws org.apache.ambari.server.api.services.stackadvisor.StackAdvisorException, org.apache.ambari.server.controller.internal.ConfigurationTopologyException, org.apache.ambari.server.AmbariException {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> props = createProps();
        java.util.Map<java.lang.String, org.apache.ambari.server.topology.AdvisedConfiguration> advisedConfigurations = new java.util.HashMap<>();
        EasyMock.expect(clusterTopology.getBlueprint()).andReturn(blueprint).anyTimes();
        EasyMock.expect(clusterTopology.getHostGroupInfo()).andReturn(createHostGroupInfo()).anyTimes();
        EasyMock.expect(clusterTopology.getAdvisedConfigurations()).andReturn(advisedConfigurations).anyTimes();
        EasyMock.expect(clusterTopology.getConfiguration()).andReturn(configuration).anyTimes();
        EasyMock.expect(clusterTopology.isClusterKerberosEnabled()).andReturn(false).anyTimes();
        EasyMock.expect(clusterTopology.getConfigRecommendationStrategy()).andReturn(org.apache.ambari.server.topology.ConfigRecommendationStrategy.ALWAYS_APPLY_DONT_OVERRIDE_CUSTOM_VALUES).anyTimes();
        EasyMock.expect(blueprint.getStack()).andReturn(stack).anyTimes();
        EasyMock.expect(stack.getVersion()).andReturn("2.3").anyTimes();
        EasyMock.expect(stack.getName()).andReturn("HDP").anyTimes();
        EasyMock.expect(stack.getConfiguration(java.util.Arrays.asList("HDFS", "YARN", "HIVE"))).andReturn(createStackDefaults()).anyTimes();
        EasyMock.expect(blueprint.getServices()).andReturn(java.util.Arrays.asList("HDFS", "YARN", "HIVE")).anyTimes();
        EasyMock.expect(blueprint.getHostGroups()).andReturn(createHostGroupMap()).anyTimes();
        EasyMock.expect(blueprint.isValidConfigType("core-site")).andReturn(true).anyTimes();
        EasyMock.expect(hostGroup.getComponentNames()).andReturn(java.util.Arrays.asList("comp1", "comp2")).anyTimes();
        EasyMock.expect(org.apache.ambari.server.api.services.stackadvisor.StackAdvisorBlueprintProcessorTest.stackAdvisorHelper.recommend(EasyMock.anyObject(org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequest.class))).andReturn(createRecommendationResponse());
        EasyMock.expect(configuration.getFullProperties()).andReturn(props).anyTimes();
        EasyMock.replay(clusterTopology, blueprint, stack, hostGroup, configuration, org.apache.ambari.server.api.services.stackadvisor.StackAdvisorBlueprintProcessorTest.stackAdvisorHelper);
        underTest.adviseConfiguration(clusterTopology, props);
        org.junit.Assert.assertTrue(advisedConfigurations.get("core-site").getProperties().containsKey("dummyKey1"));
        org.junit.Assert.assertTrue(advisedConfigurations.get("core-site").getPropertyValueAttributes().containsKey("dummyKey2"));
        org.junit.Assert.assertEquals("dummyValue", advisedConfigurations.get("core-site").getProperties().get("dummyKey1"));
        org.junit.Assert.assertEquals(java.lang.Boolean.toString(true), advisedConfigurations.get("core-site").getPropertyValueAttributes().get("dummyKey2").getDelete());
    }

    @org.junit.Test
    public void testAdviseConfigurationWhenConfigurationRecommendFails() throws org.apache.ambari.server.api.services.stackadvisor.StackAdvisorException, org.apache.ambari.server.controller.internal.ConfigurationTopologyException, org.apache.ambari.server.AmbariException {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> props = createProps();
        java.util.Map<java.lang.String, org.apache.ambari.server.topology.AdvisedConfiguration> advisedConfigurations = new java.util.HashMap<>();
        EasyMock.expect(clusterTopology.getBlueprint()).andReturn(blueprint).anyTimes();
        EasyMock.expect(clusterTopology.getHostGroupInfo()).andReturn(createHostGroupInfo()).anyTimes();
        EasyMock.expect(clusterTopology.getAdvisedConfigurations()).andReturn(advisedConfigurations).anyTimes();
        EasyMock.expect(clusterTopology.getConfiguration()).andReturn(configuration).anyTimes();
        EasyMock.expect(clusterTopology.isClusterKerberosEnabled()).andReturn(false).anyTimes();
        EasyMock.expect(blueprint.getStack()).andReturn(stack).anyTimes();
        EasyMock.expect(stack.getVersion()).andReturn("2.3").anyTimes();
        EasyMock.expect(stack.getName()).andReturn("HDP").anyTimes();
        EasyMock.expect(blueprint.getHostGroups()).andReturn(createHostGroupMap()).anyTimes();
        EasyMock.expect(hostGroup.getComponentNames()).andReturn(java.util.Arrays.asList("comp1", "comp2")).anyTimes();
        EasyMock.expect(blueprint.getServices()).andReturn(java.util.Arrays.asList("HDFS", "YARN", "HIVE")).anyTimes();
        EasyMock.expect(org.apache.ambari.server.api.services.stackadvisor.StackAdvisorBlueprintProcessorTest.stackAdvisorHelper.recommend(EasyMock.anyObject(org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequest.class))).andThrow(new org.apache.ambari.server.api.services.stackadvisor.StackAdvisorException("ex"));
        EasyMock.expect(configuration.getFullProperties()).andReturn(props);
        EasyMock.replay(clusterTopology, blueprint, stack, hostGroup, configuration, org.apache.ambari.server.api.services.stackadvisor.StackAdvisorBlueprintProcessorTest.stackAdvisorHelper);
        try {
            underTest.adviseConfiguration(clusterTopology, props);
            org.junit.Assert.fail("Invalid state");
        } catch (org.apache.ambari.server.controller.internal.ConfigurationTopologyException e) {
            org.junit.Assert.assertEquals(org.apache.ambari.server.api.services.stackadvisor.StackAdvisorBlueprintProcessor.RECOMMENDATION_FAILED, e.getMessage());
        }
    }

    @org.junit.Test
    public void testAdviseConfigurationWhenConfigurationRecommendHasInvalidResponse() throws org.apache.ambari.server.api.services.stackadvisor.StackAdvisorException, org.apache.ambari.server.controller.internal.ConfigurationTopologyException, org.apache.ambari.server.AmbariException {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> props = createProps();
        java.util.Map<java.lang.String, org.apache.ambari.server.topology.AdvisedConfiguration> advisedConfigurations = new java.util.HashMap<>();
        EasyMock.expect(clusterTopology.getBlueprint()).andReturn(blueprint).anyTimes();
        EasyMock.expect(clusterTopology.getHostGroupInfo()).andReturn(createHostGroupInfo()).anyTimes();
        EasyMock.expect(clusterTopology.getAdvisedConfigurations()).andReturn(advisedConfigurations).anyTimes();
        EasyMock.expect(clusterTopology.getConfiguration()).andReturn(configuration).anyTimes();
        EasyMock.expect(clusterTopology.isClusterKerberosEnabled()).andReturn(false).anyTimes();
        EasyMock.expect(blueprint.getStack()).andReturn(stack).anyTimes();
        EasyMock.expect(stack.getVersion()).andReturn("2.3").anyTimes();
        EasyMock.expect(stack.getName()).andReturn("HDP").anyTimes();
        EasyMock.expect(blueprint.getServices()).andReturn(java.util.Arrays.asList("HDFS", "YARN", "HIVE")).anyTimes();
        EasyMock.expect(blueprint.getHostGroups()).andReturn(createHostGroupMap()).anyTimes();
        EasyMock.expect(hostGroup.getComponentNames()).andReturn(java.util.Arrays.asList("comp1", "comp2")).anyTimes();
        EasyMock.expect(org.apache.ambari.server.api.services.stackadvisor.StackAdvisorBlueprintProcessorTest.stackAdvisorHelper.recommend(EasyMock.anyObject(org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequest.class))).andReturn(new org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse());
        EasyMock.expect(configuration.getFullProperties()).andReturn(props);
        EasyMock.replay(clusterTopology, blueprint, stack, hostGroup, configuration, org.apache.ambari.server.api.services.stackadvisor.StackAdvisorBlueprintProcessorTest.stackAdvisorHelper);
        try {
            underTest.adviseConfiguration(clusterTopology, props);
            org.junit.Assert.fail("Invalid state");
        } catch (org.apache.ambari.server.controller.internal.ConfigurationTopologyException e) {
            org.junit.Assert.assertEquals(org.apache.ambari.server.api.services.stackadvisor.StackAdvisorBlueprintProcessor.INVALID_RESPONSE, e.getMessage());
        }
    }

    private java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> createProps() {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> props = com.google.common.collect.Maps.newHashMap();
        java.util.Map<java.lang.String, java.lang.String> siteProps = com.google.common.collect.Maps.newHashMap();
        siteProps.put("myprop", "myvalue");
        siteProps.put("dummyKey3", "userinput");
        props.put("core-site", siteProps);
        return props;
    }

    private java.util.Map<java.lang.String, org.apache.ambari.server.topology.HostGroup> createHostGroupMap() {
        java.util.Map<java.lang.String, org.apache.ambari.server.topology.HostGroup> hgMap = com.google.common.collect.Maps.newHashMap();
        hgMap.put("hg1", hostGroup);
        hgMap.put("hg2", hostGroup);
        hgMap.put("hg3", hostGroup);
        return hgMap;
    }

    private java.util.Map<java.lang.String, org.apache.ambari.server.topology.HostGroupInfo> createHostGroupInfo() {
        java.util.Map<java.lang.String, org.apache.ambari.server.topology.HostGroupInfo> hostGroupInfoMap = new java.util.HashMap<>();
        org.apache.ambari.server.topology.HostGroupInfo hgi1 = new org.apache.ambari.server.topology.HostGroupInfo("hostGroup1");
        org.apache.ambari.server.topology.HostGroupInfo hgi2 = new org.apache.ambari.server.topology.HostGroupInfo("hostGroup2");
        hostGroupInfoMap.put("hg1", hgi1);
        hostGroupInfoMap.put("hg2", hgi2);
        return hostGroupInfoMap;
    }

    private org.apache.ambari.server.topology.Configuration createStackDefaults() {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> stackDefaultProps = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> coreSiteDefault = new java.util.HashMap<>();
        coreSiteDefault.put("dummyKey3", "stackDefaultValue");
        stackDefaultProps.put("core-site", coreSiteDefault);
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>> stackDefaultAttributes = new java.util.HashMap<>();
        return new org.apache.ambari.server.topology.Configuration(stackDefaultProps, stackDefaultAttributes);
    }

    private org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse createRecommendationResponse() {
        org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse response = new org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse();
        org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.Recommendation recommendations = new org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.Recommendation();
        org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.Blueprint blueprint = new org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.Blueprint();
        java.util.Map<java.lang.String, org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.BlueprintConfigurations> blueprintConfigurationsMap = new java.util.HashMap<>();
        org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.BlueprintConfigurations blueprintConfig = new org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.BlueprintConfigurations();
        java.util.Map<java.lang.String, java.lang.String> properties = new java.util.HashMap<>();
        properties.put("dummyKey1", "dummyValue");
        properties.put("dummyKey3", "dummyValue-override");
        blueprintConfig.setProperties(properties);
        java.util.Map<java.lang.String, org.apache.ambari.server.state.ValueAttributesInfo> propAttributes = new java.util.HashMap<>();
        org.apache.ambari.server.state.ValueAttributesInfo valueAttributesInfo1 = new org.apache.ambari.server.state.ValueAttributesInfo();
        org.apache.ambari.server.state.ValueAttributesInfo valueAttributesInfo2 = new org.apache.ambari.server.state.ValueAttributesInfo();
        valueAttributesInfo1.setDelete("true");
        valueAttributesInfo2.setDelete("true");
        propAttributes.put("dummyKey2", valueAttributesInfo1);
        propAttributes.put("dummyKey3", valueAttributesInfo2);
        blueprintConfig.setPropertyAttributes(propAttributes);
        blueprintConfigurationsMap.put("core-site", blueprintConfig);
        blueprint.setConfigurations(blueprintConfigurationsMap);
        recommendations.setBlueprint(blueprint);
        response.setRecommendations(recommendations);
        return response;
    }
}