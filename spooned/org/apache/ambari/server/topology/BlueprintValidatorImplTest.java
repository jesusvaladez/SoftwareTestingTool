package org.apache.ambari.server.topology;
import org.easymock.EasyMock;
import org.easymock.EasyMockRule;
import org.easymock.Mock;
import org.easymock.MockType;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.reset;
import static org.easymock.EasyMock.verify;
public class BlueprintValidatorImplTest {
    private final java.util.Map<java.lang.String, org.apache.ambari.server.topology.HostGroup> hostGroups = new java.util.LinkedHashMap<>();

    @org.junit.Rule
    public org.easymock.EasyMockRule mocks = new org.easymock.EasyMockRule(this);

    @org.easymock.Mock(type = org.easymock.MockType.NICE)
    private org.apache.ambari.server.topology.Blueprint blueprint;

    @org.easymock.Mock(type = org.easymock.MockType.NICE)
    private org.apache.ambari.server.controller.internal.Stack stack;

    @org.easymock.Mock(type = org.easymock.MockType.NICE)
    private org.apache.ambari.server.topology.HostGroup group1;

    @org.easymock.Mock(type = org.easymock.MockType.NICE)
    private org.apache.ambari.server.topology.HostGroup group2;

    @org.easymock.Mock(type = org.easymock.MockType.NICE)
    private org.apache.ambari.server.state.DependencyInfo dependency1;

    @org.easymock.Mock(type = org.easymock.MockType.NICE)
    private org.apache.ambari.server.state.DependencyInfo dependency2;

    @org.easymock.Mock(type = org.easymock.MockType.NICE)
    private org.apache.ambari.server.state.ComponentInfo dependencyComponentInfo;

    @org.easymock.Mock(type = org.easymock.MockType.NICE)
    private org.apache.ambari.server.state.DependencyConditionInfo dependencyConditionInfo1;

    @org.easymock.Mock(type = org.easymock.MockType.NICE)
    private org.apache.ambari.server.state.DependencyConditionInfo dependencyConditionInfo2;

    private final java.util.Collection<java.lang.String> group1Components = new java.util.ArrayList<>();

    private final java.util.Collection<java.lang.String> group2Components = new java.util.ArrayList<>();

    private final java.util.Collection<java.lang.String> services = new java.util.ArrayList<>();

    private java.util.Collection<org.apache.ambari.server.state.DependencyInfo> dependencies1 = new java.util.ArrayList<>();

    private java.util.List<org.apache.ambari.server.state.DependencyConditionInfo> dependenciesConditionInfos1 = new java.util.ArrayList<>();

    private org.apache.ambari.server.state.AutoDeployInfo autoDeploy = new org.apache.ambari.server.state.AutoDeployInfo();

    private java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configProperties = new java.util.HashMap<>();

    private org.apache.ambari.server.topology.Configuration configuration = new org.apache.ambari.server.topology.Configuration(configProperties, java.util.Collections.emptyMap());

    @org.junit.Before
    public void setup() {
        hostGroups.put("group1", group1);
        hostGroups.put("group2", group2);
        autoDeploy.setEnabled(true);
        autoDeploy.setCoLocate("service1/component2");
        EasyMock.expect(blueprint.getStack()).andReturn(stack).anyTimes();
        EasyMock.expect(blueprint.getHostGroups()).andReturn(hostGroups).anyTimes();
        EasyMock.expect(blueprint.getServices()).andReturn(services).anyTimes();
        EasyMock.expect(group1.getComponentNames()).andReturn(group1Components).anyTimes();
        EasyMock.expect(group1.getName()).andReturn("host-group-1").anyTimes();
        EasyMock.expect(group2.getComponentNames()).andReturn(group2Components).anyTimes();
        EasyMock.expect(group2.getName()).andReturn("host-group-2").anyTimes();
        EasyMock.expect(stack.getDependenciesForComponent("component1")).andReturn(dependencies1).anyTimes();
        EasyMock.expect(stack.getDependenciesForComponent("component2")).andReturn(dependencies1).anyTimes();
        EasyMock.expect(stack.getDependenciesForComponent("component3")).andReturn(dependencies1).anyTimes();
        EasyMock.expect(stack.getDependenciesForComponent("component4")).andReturn(dependencies1).anyTimes();
        EasyMock.expect(stack.getCardinality("component1")).andReturn(new org.apache.ambari.server.topology.Cardinality("1"));
        EasyMock.expect(stack.getCardinality("component2")).andReturn(new org.apache.ambari.server.topology.Cardinality("1+"));
        EasyMock.expect(stack.getCardinality("component3")).andReturn(new org.apache.ambari.server.topology.Cardinality("1+"));
        dependenciesConditionInfos1.add(dependencyConditionInfo1);
        dependenciesConditionInfos1.add(dependencyConditionInfo2);
        EasyMock.expect(blueprint.getConfiguration()).andReturn(configuration).anyTimes();
    }

    @org.junit.After
    public void tearDown() {
        EasyMock.reset(blueprint, stack, group1, group2, dependency1, dependency2, dependencyConditionInfo1, dependencyConditionInfo2);
    }

    @org.junit.Test
    public void testValidateTopology_basic() throws java.lang.Exception {
        group1Components.add("component1");
        group1Components.add("component1");
        services.addAll(java.util.Arrays.asList("service1", "service2"));
        EasyMock.expect(stack.getComponents("service1")).andReturn(java.util.Collections.singleton("component1")).anyTimes();
        EasyMock.expect(stack.getComponents("service2")).andReturn(java.util.Collections.singleton("component2")).anyTimes();
        EasyMock.expect(blueprint.getHostGroupsForComponent("component1")).andReturn(java.util.Collections.singleton(group1)).anyTimes();
        EasyMock.expect(blueprint.getHostGroupsForComponent("component2")).andReturn(java.util.Arrays.asList(group1, group2)).anyTimes();
        EasyMock.replay(blueprint, stack, group1, group2, dependency1);
        org.apache.ambari.server.topology.BlueprintValidator validator = new org.apache.ambari.server.topology.BlueprintValidatorImpl(blueprint);
        validator.validateTopology();
    }

    @org.junit.Test(expected = org.apache.ambari.server.topology.InvalidTopologyException.class)
    public void testValidateTopology_basic_negative() throws java.lang.Exception {
        group1Components.add("component2");
        services.addAll(java.util.Collections.singleton("service1"));
        EasyMock.expect(stack.getComponents("service1")).andReturn(java.util.Arrays.asList("component1", "component2")).anyTimes();
        EasyMock.expect(blueprint.getHostGroupsForComponent("component1")).andReturn(java.util.Collections.emptyList()).anyTimes();
        EasyMock.expect(blueprint.getHostGroupsForComponent("component2")).andReturn(java.util.Arrays.asList(group1, group2)).anyTimes();
        EasyMock.replay(blueprint, stack, group1, group2, dependency1);
        org.apache.ambari.server.topology.BlueprintValidator validator = new org.apache.ambari.server.topology.BlueprintValidatorImpl(blueprint);
        validator.validateTopology();
    }

    @org.junit.Test
    public void testValidateTopology_autoDeploy() throws java.lang.Exception {
        group1Components.add("component2");
        services.addAll(java.util.Collections.singleton("service1"));
        EasyMock.expect(blueprint.getHostGroupsForComponent("component1")).andReturn(java.util.Collections.emptyList()).anyTimes();
        EasyMock.expect(blueprint.getHostGroupsForComponent("component2")).andReturn(java.util.Arrays.asList(group1, group2)).anyTimes();
        EasyMock.expect(stack.getComponents("service1")).andReturn(java.util.Arrays.asList("component1", "component2")).anyTimes();
        EasyMock.expect(stack.getAutoDeployInfo("component1")).andReturn(autoDeploy).anyTimes();
        EasyMock.expect(group1.addComponent("component1")).andReturn(true).once();
        EasyMock.replay(blueprint, stack, group1, group2, dependency1);
        org.apache.ambari.server.topology.BlueprintValidator validator = new org.apache.ambari.server.topology.BlueprintValidatorImpl(blueprint);
        validator.validateTopology();
        EasyMock.verify(group1);
    }

    @org.junit.Test(expected = org.apache.ambari.server.topology.InvalidTopologyException.class)
    public void testValidateTopology_exclusiveDependency() throws java.lang.Exception {
        group1Components.add("component2");
        group1Components.add("component3");
        dependencies1.add(dependency1);
        services.addAll(java.util.Collections.singleton("service1"));
        EasyMock.expect(blueprint.getHostGroupsForComponent("component1")).andReturn(java.util.Arrays.asList(group1, group2)).anyTimes();
        EasyMock.expect(blueprint.getHostGroupsForComponent("component2")).andReturn(java.util.Arrays.asList(group1, group2)).anyTimes();
        EasyMock.expect(blueprint.getHostGroupsForComponent("component3")).andReturn(java.util.Arrays.asList(group1, group2)).anyTimes();
        EasyMock.expect(stack.getComponents("service1")).andReturn(java.util.Arrays.asList("component1", "component2")).anyTimes();
        EasyMock.expect(stack.getComponents("service2")).andReturn(java.util.Collections.singleton("component3")).anyTimes();
        EasyMock.expect(stack.getAutoDeployInfo("component1")).andReturn(autoDeploy).anyTimes();
        org.apache.ambari.server.state.AutoDeployInfo dependencyAutoDeploy = new org.apache.ambari.server.state.AutoDeployInfo();
        dependencyAutoDeploy.setEnabled(true);
        dependencyAutoDeploy.setCoLocate("service1/component1");
        EasyMock.expect(dependency1.getScope()).andReturn("host").anyTimes();
        EasyMock.expect(dependency1.getType()).andReturn("exclusive").anyTimes();
        EasyMock.expect(dependency1.getAutoDeploy()).andReturn(dependencyAutoDeploy).anyTimes();
        EasyMock.expect(dependency1.getComponentName()).andReturn("component3").anyTimes();
        EasyMock.expect(dependency1.getServiceName()).andReturn("service1").anyTimes();
        EasyMock.expect(dependency1.getName()).andReturn("dependency1").anyTimes();
        EasyMock.expect(dependencyComponentInfo.isClient()).andReturn(true).anyTimes();
        EasyMock.expect(stack.getComponentInfo("component3")).andReturn(dependencyComponentInfo).anyTimes();
        EasyMock.replay(blueprint, stack, group1, group2, dependency1, dependencyComponentInfo);
        org.apache.ambari.server.topology.BlueprintValidator validator = new org.apache.ambari.server.topology.BlueprintValidatorImpl(blueprint);
        validator.validateTopology();
        EasyMock.verify(group1);
    }

    @org.junit.Test
    public void testValidateTopology_autoDeploy_hasDependency() throws java.lang.Exception {
        group1Components.add("component2");
        dependencies1.add(dependency1);
        services.addAll(java.util.Collections.singleton("service1"));
        EasyMock.expect(blueprint.getHostGroupsForComponent("component1")).andReturn(java.util.Collections.emptyList()).anyTimes();
        EasyMock.expect(blueprint.getHostGroupsForComponent("component2")).andReturn(java.util.Arrays.asList(group1, group2)).anyTimes();
        EasyMock.expect(blueprint.getHostGroupsForComponent("component3")).andReturn(java.util.Collections.emptyList()).anyTimes();
        EasyMock.expect(stack.getComponents("service1")).andReturn(java.util.Arrays.asList("component1", "component2")).anyTimes();
        EasyMock.expect(stack.getComponents("service2")).andReturn(java.util.Collections.singleton("component3")).anyTimes();
        EasyMock.expect(stack.getAutoDeployInfo("component1")).andReturn(autoDeploy).anyTimes();
        org.apache.ambari.server.state.AutoDeployInfo dependencyAutoDeploy = new org.apache.ambari.server.state.AutoDeployInfo();
        dependencyAutoDeploy.setEnabled(true);
        dependencyAutoDeploy.setCoLocate("service1/component1");
        EasyMock.expect(dependency1.getScope()).andReturn("host").anyTimes();
        EasyMock.expect(dependency1.getType()).andReturn("inclusive").anyTimes();
        EasyMock.expect(dependency1.getAutoDeploy()).andReturn(dependencyAutoDeploy).anyTimes();
        EasyMock.expect(dependency1.getComponentName()).andReturn("component3").anyTimes();
        EasyMock.expect(dependency1.getServiceName()).andReturn("service1").anyTimes();
        EasyMock.expect(dependency1.getName()).andReturn("dependency1").anyTimes();
        EasyMock.expect(dependencyComponentInfo.isClient()).andReturn(true).anyTimes();
        EasyMock.expect(stack.getComponentInfo("component3")).andReturn(dependencyComponentInfo).anyTimes();
        EasyMock.expect(group1.addComponent("component1")).andReturn(true).once();
        EasyMock.expect(group1.addComponent("component3")).andReturn(true).once();
        EasyMock.replay(blueprint, stack, group1, group2, dependency1, dependencyComponentInfo);
        org.apache.ambari.server.topology.BlueprintValidator validator = new org.apache.ambari.server.topology.BlueprintValidatorImpl(blueprint);
        validator.validateTopology();
        EasyMock.verify(group1);
    }

    @org.junit.Test(expected = org.apache.ambari.server.topology.InvalidTopologyException.class)
    public void testValidateRequiredProperties_SqlaInHiveStackHdp22() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.lang.String> hiveEnvConfig = new java.util.HashMap<>();
        hiveEnvConfig.put("hive_database", "Existing SQL Anywhere Database");
        configProperties.put("hive-env", hiveEnvConfig);
        group1Components.add("HIVE_METASTORE");
        services.addAll(java.util.Arrays.asList("HIVE"));
        org.apache.ambari.server.configuration.Configuration serverConfig = org.apache.ambari.server.topology.BlueprintImplTest.setupConfigurationWithGPLLicense(true);
        org.apache.ambari.server.topology.Configuration config = new org.apache.ambari.server.topology.Configuration(new java.util.HashMap<>(), new java.util.HashMap<>());
        EasyMock.expect(group1.getConfiguration()).andReturn(config).anyTimes();
        EasyMock.expect(stack.getComponents("HIVE")).andReturn(java.util.Collections.singleton("HIVE_METASTORE")).anyTimes();
        EasyMock.expect(stack.getVersion()).andReturn("2.2").once();
        EasyMock.expect(stack.getName()).andReturn("HDP").once();
        EasyMock.expect(blueprint.getHostGroupsForComponent("HIVE_METASTORE")).andReturn(java.util.Collections.singleton(group1)).anyTimes();
        EasyMock.replay(blueprint, stack, group1, group2, dependency1, serverConfig);
        org.apache.ambari.server.topology.BlueprintValidator validator = new org.apache.ambari.server.topology.BlueprintValidatorImpl(blueprint);
        validator.validateRequiredProperties();
    }

    @org.junit.Test(expected = org.apache.ambari.server.topology.InvalidTopologyException.class)
    public void testValidateRequiredProperties_SqlaInOozieStackHdp22() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.lang.String> hiveEnvConfig = new java.util.HashMap<>();
        hiveEnvConfig.put("oozie_database", "Existing SQL Anywhere Database");
        configProperties.put("oozie-env", hiveEnvConfig);
        group1Components.add("OOZIE_SERVER");
        services.addAll(java.util.Arrays.asList("OOZIE"));
        org.apache.ambari.server.configuration.Configuration serverConfig = org.apache.ambari.server.topology.BlueprintImplTest.setupConfigurationWithGPLLicense(true);
        org.apache.ambari.server.topology.Configuration config = new org.apache.ambari.server.topology.Configuration(new java.util.HashMap<>(), new java.util.HashMap<>());
        EasyMock.expect(group1.getConfiguration()).andReturn(config).anyTimes();
        EasyMock.expect(stack.getComponents("OOZIE")).andReturn(java.util.Collections.singleton("OOZIE_SERVER")).anyTimes();
        EasyMock.expect(stack.getVersion()).andReturn("2.2").once();
        EasyMock.expect(stack.getName()).andReturn("HDP").once();
        EasyMock.expect(blueprint.getHostGroupsForComponent("OOZIE_SERVER")).andReturn(java.util.Collections.singleton(group1)).anyTimes();
        EasyMock.replay(blueprint, stack, group1, group2, dependency1, serverConfig);
        org.apache.ambari.server.topology.BlueprintValidator validator = new org.apache.ambari.server.topology.BlueprintValidatorImpl(blueprint);
        validator.validateRequiredProperties();
    }

    @org.junit.Test
    public void testShouldDependencyBeExcludedWenRelatedServiceIsNotInBlueprint() throws java.lang.Exception {
        hostGroups.clear();
        hostGroups.put("group1", group1);
        group1Components.add("component-1");
        dependencies1.add(dependency1);
        services.addAll(java.util.Collections.singleton("service-1"));
        EasyMock.expect(blueprint.getHostGroupsForComponent("component-1")).andReturn(java.util.Arrays.asList(group1)).anyTimes();
        EasyMock.expect(blueprint.getName()).andReturn("blueprint-1").anyTimes();
        org.apache.ambari.server.topology.Cardinality cardinality = new org.apache.ambari.server.topology.Cardinality("1");
        EasyMock.expect(stack.getComponents("service-1")).andReturn(java.util.Arrays.asList("component-1")).anyTimes();
        EasyMock.expect(stack.getAutoDeployInfo("component-1")).andReturn(autoDeploy).anyTimes();
        EasyMock.expect(stack.getDependenciesForComponent("component-1")).andReturn(dependencies1).anyTimes();
        EasyMock.expect(stack.getCardinality("component-1")).andReturn(cardinality).anyTimes();
        org.apache.ambari.server.state.AutoDeployInfo dependencyAutoDeploy = new org.apache.ambari.server.state.AutoDeployInfo();
        dependencyAutoDeploy.setEnabled(true);
        dependencyAutoDeploy.setCoLocate("service1/component1");
        EasyMock.expect(dependency1.getScope()).andReturn("host").anyTimes();
        EasyMock.expect(dependency1.getAutoDeploy()).andReturn(dependencyAutoDeploy).anyTimes();
        EasyMock.expect(dependency1.getComponentName()).andReturn("component-d").anyTimes();
        EasyMock.expect(dependency1.getServiceName()).andReturn("service-d").anyTimes();
        EasyMock.expect(dependency1.getName()).andReturn("dependency-1").anyTimes();
        EasyMock.expect(dependencyComponentInfo.isClient()).andReturn(true).anyTimes();
        EasyMock.expect(stack.getComponentInfo("component-d")).andReturn(dependencyComponentInfo).anyTimes();
        EasyMock.replay(blueprint, stack, group1, group2, dependency1, dependencyComponentInfo);
        org.apache.ambari.server.topology.BlueprintValidator validator = new org.apache.ambari.server.topology.BlueprintValidatorImpl(blueprint);
        validator.validateTopology();
        EasyMock.verify(group1);
    }

    @org.junit.Test(expected = org.apache.ambari.server.topology.InvalidTopologyException.class)
    public void testShouldThrowErrorWhenDependentComponentIsNotInBlueprint() throws java.lang.Exception {
        hostGroups.clear();
        hostGroups.put("group1", group1);
        group1Components.add("component-1");
        dependencies1.add(dependency1);
        services.addAll(java.util.Collections.singleton("service-1"));
        EasyMock.expect(blueprint.getHostGroupsForComponent("component-1")).andReturn(java.util.Arrays.asList(group1)).anyTimes();
        EasyMock.expect(blueprint.getName()).andReturn("blueprint-1").anyTimes();
        org.apache.ambari.server.topology.Cardinality cardinality = new org.apache.ambari.server.topology.Cardinality("1");
        EasyMock.expect(stack.getComponents("service-1")).andReturn(java.util.Arrays.asList("component-1")).anyTimes();
        EasyMock.expect(stack.getAutoDeployInfo("component-1")).andReturn(autoDeploy).anyTimes();
        EasyMock.expect(stack.getDependenciesForComponent("component-1")).andReturn(dependencies1).anyTimes();
        EasyMock.expect(stack.getCardinality("component-1")).andReturn(cardinality).anyTimes();
        org.apache.ambari.server.state.AutoDeployInfo dependencyAutoDeploy = null;
        EasyMock.expect(dependency1.getScope()).andReturn("host").anyTimes();
        EasyMock.expect(dependency1.getType()).andReturn("inclusive").anyTimes();
        EasyMock.expect(dependency1.getAutoDeploy()).andReturn(dependencyAutoDeploy).anyTimes();
        EasyMock.expect(dependency1.getComponentName()).andReturn("component-d").anyTimes();
        EasyMock.expect(dependency1.getServiceName()).andReturn("service-d").anyTimes();
        EasyMock.expect(dependency1.getName()).andReturn("dependency-1").anyTimes();
        EasyMock.expect(stack.getComponentInfo("component-d")).andReturn(dependencyComponentInfo).anyTimes();
        EasyMock.replay(blueprint, stack, group1, group2, dependency1, dependencyComponentInfo);
        org.apache.ambari.server.topology.BlueprintValidator validator = new org.apache.ambari.server.topology.BlueprintValidatorImpl(blueprint);
        validator.validateTopology();
        EasyMock.verify(group1);
    }

    @org.junit.Test(expected = org.apache.ambari.server.topology.InvalidTopologyException.class)
    public void testWhenComponentIsConditionallyDependentAndOnlyOneOfTheConditionsIsSatisfied() throws java.lang.Exception {
        hostGroups.clear();
        hostGroups.put("group1", group1);
        group1Components.add("component-1");
        dependencies1.add(dependency1);
        dependencies1.add(dependency2);
        services.addAll(java.util.Collections.singleton("service-1"));
        EasyMock.expect(blueprint.getHostGroupsForComponent("component-1")).andReturn(java.util.Arrays.asList(group1)).anyTimes();
        EasyMock.expect(blueprint.getName()).andReturn("blueprint-1").anyTimes();
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> typeProps = new java.util.HashMap<>();
        typeProps.put("yarn.resourcemanager.hostname", "testhost");
        properties.put("yarn-site", typeProps);
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(properties, java.util.Collections.emptyMap());
        org.apache.ambari.server.topology.Cardinality cardinality = new org.apache.ambari.server.topology.Cardinality("1");
        EasyMock.expect(stack.getComponents("service-1")).andReturn(java.util.Arrays.asList("component-1")).anyTimes();
        EasyMock.expect(stack.getAutoDeployInfo("component-1")).andReturn(autoDeploy).anyTimes();
        EasyMock.expect(stack.getDependenciesForComponent("component-1")).andReturn(dependencies1).anyTimes();
        EasyMock.expect(stack.getCardinality("component-1")).andReturn(cardinality).anyTimes();
        org.apache.ambari.server.state.AutoDeployInfo dependencyAutoDeploy = null;
        EasyMock.expect(dependency1.getScope()).andReturn("host").anyTimes();
        EasyMock.expect(dependency1.getType()).andReturn("inclusive").anyTimes();
        EasyMock.expect(dependency1.getAutoDeploy()).andReturn(dependencyAutoDeploy).anyTimes();
        EasyMock.expect(dependency1.getComponentName()).andReturn("component-d").anyTimes();
        EasyMock.expect(dependency1.getServiceName()).andReturn("service-d").anyTimes();
        EasyMock.expect(dependency1.getName()).andReturn("dependency-1").anyTimes();
        EasyMock.expect(dependency1.hasDependencyConditions()).andReturn(true).anyTimes();
        EasyMock.expect(dependency1.getDependencyConditions()).andReturn(dependenciesConditionInfos1).anyTimes();
        EasyMock.expect(dependency2.getScope()).andReturn("host").anyTimes();
        EasyMock.expect(dependency2.getType()).andReturn("inclusive").anyTimes();
        EasyMock.expect(dependency2.getAutoDeploy()).andReturn(dependencyAutoDeploy).anyTimes();
        EasyMock.expect(dependency2.getComponentName()).andReturn("component-d").anyTimes();
        EasyMock.expect(dependency2.getServiceName()).andReturn("service-d").anyTimes();
        EasyMock.expect(dependency2.getName()).andReturn("dependency-2").anyTimes();
        EasyMock.expect(dependency2.hasDependencyConditions()).andReturn(false).anyTimes();
        EasyMock.expect(dependencyConditionInfo1.isResolved(org.easymock.EasyMock.anyObject())).andReturn(true).anyTimes();
        EasyMock.expect(dependencyConditionInfo2.isResolved(org.easymock.EasyMock.anyObject())).andReturn(false).anyTimes();
        EasyMock.expect(dependencyComponentInfo.isClient()).andReturn(false).anyTimes();
        EasyMock.expect(stack.getComponentInfo("component-d")).andReturn(dependencyComponentInfo).anyTimes();
        EasyMock.replay(blueprint, stack, group1, group2, dependency1, dependency2, dependencyComponentInfo, dependencyConditionInfo1, dependencyConditionInfo2);
        org.apache.ambari.server.topology.BlueprintValidator validator = new org.apache.ambari.server.topology.BlueprintValidatorImpl(blueprint);
        validator.validateTopology();
        EasyMock.verify(group1);
    }
}