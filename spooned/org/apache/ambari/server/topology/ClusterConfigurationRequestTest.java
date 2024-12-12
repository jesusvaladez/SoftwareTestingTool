package org.apache.ambari.server.topology;
import org.easymock.Capture;
import org.easymock.CaptureType;
import org.easymock.EasyMock;
import org.easymock.EasyMockRule;
import org.easymock.Mock;
import org.easymock.MockType;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import static org.easymock.EasyMock.anyBoolean;
import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.anyString;
import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.newCapture;
import static org.easymock.EasyMock.verify;
@org.junit.runner.RunWith(org.powermock.modules.junit4.PowerMockRunner.class)
@org.powermock.core.classloader.annotations.PrepareForTest({ org.apache.ambari.server.topology.AmbariContext.class })
public class ClusterConfigurationRequestTest {
    @org.junit.Rule
    public org.easymock.EasyMockRule mocks = new org.easymock.EasyMockRule(this);

    @org.easymock.Mock(type = org.easymock.MockType.NICE)
    private org.apache.ambari.server.topology.Blueprint blueprint;

    @org.easymock.Mock(type = org.easymock.MockType.NICE)
    private org.apache.ambari.server.topology.AmbariContext ambariContext;

    @org.easymock.Mock(type = org.easymock.MockType.NICE)
    private org.apache.ambari.server.state.Cluster cluster;

    @org.easymock.Mock(type = org.easymock.MockType.NICE)
    private org.apache.ambari.server.state.Clusters clusters;

    @org.easymock.Mock(type = org.easymock.MockType.NICE)
    private org.apache.ambari.server.topology.ClusterTopology topology;

    @org.easymock.Mock(type = org.easymock.MockType.NICE)
    private org.apache.ambari.server.api.services.stackadvisor.StackAdvisorBlueprintProcessor stackAdvisorBlueprintProcessor;

    @org.easymock.Mock(type = org.easymock.MockType.NICE)
    private org.apache.ambari.server.controller.internal.Stack stack;

    @org.easymock.Mock(type = org.easymock.MockType.NICE)
    private org.apache.ambari.server.controller.AmbariManagementController controller;

    @org.easymock.Mock(type = org.easymock.MockType.NICE)
    private org.apache.ambari.server.controller.KerberosHelper kerberosHelper;

    @org.easymock.Mock(type = org.easymock.MockType.NICE)
    private org.apache.ambari.server.state.ConfigHelper configHelper;

    private final java.lang.String STACK_NAME = "testStack";

    private final java.lang.String STACK_VERSION = "1";

    private final java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> stackProperties = new java.util.HashMap<>();

    private final java.util.Map<java.lang.String, java.lang.String> defaultClusterEnvProperties = new java.util.HashMap<>();

    @org.junit.Before
    public void setup() {
        stackProperties.put(org.apache.ambari.server.state.ConfigHelper.CLUSTER_ENV, defaultClusterEnvProperties);
    }

    @org.junit.Test
    public void testProcessWithKerberos_UpdateKererosConfigProperty_WithNoCustomValue() throws java.lang.Exception {
        org.easymock.Capture<? extends java.util.Set<java.lang.String>> captureUpdatedConfigTypes = testProcessWithKerberos(null, "defaultTestValue", null);
        java.util.Set<java.lang.String> updatedConfigTypes = captureUpdatedConfigTypes.getValue();
        org.junit.Assert.assertEquals(2, updatedConfigTypes.size());
    }

    @org.junit.Test
    public void testProcessWithKerberos_UpdateKererosConfigProperty_WithCustomValueEqualToStackDefault() throws java.lang.Exception {
        org.easymock.Capture<? extends java.util.Set<java.lang.String>> captureUpdatedConfigTypes = testProcessWithKerberos("defaultTestValue", "defaultTestValue", null);
        java.util.Set<java.lang.String> updatedConfigTypes = captureUpdatedConfigTypes.getValue();
        org.junit.Assert.assertEquals(2, updatedConfigTypes.size());
    }

    @org.junit.Test
    public void testProcessWithKerberos_DontUpdateKererosConfigProperty_WithCustomValueDifferentThanStackDefault() throws java.lang.Exception {
        org.easymock.Capture<? extends java.util.Set<java.lang.String>> captureUpdatedConfigTypes = testProcessWithKerberos("testPropertyValue", "defaultTestValue", null);
        java.util.Set<java.lang.String> updatedConfigTypes = captureUpdatedConfigTypes.getValue();
        org.junit.Assert.assertEquals(1, updatedConfigTypes.size());
    }

    @org.junit.Test
    public void testProcessWithKerberos_DontUpdateKererosConfigProperty_WithCustomValueNoStackDefault() throws java.lang.Exception {
        org.easymock.Capture<? extends java.util.Set<java.lang.String>> captureUpdatedConfigTypes = testProcessWithKerberos("testPropertyValue", null, null);
        java.util.Set<java.lang.String> updatedConfigTypes = captureUpdatedConfigTypes.getValue();
        org.junit.Assert.assertEquals(1, updatedConfigTypes.size());
    }

    @org.junit.Test
    public void testProcessWithKerberos_DontUpdateKererosConfigProperty_WithKerberosConfigSameAsDefault() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> kerberosConfig = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> properties = new java.util.HashMap<>();
        properties.put("testProperty", "defaultTestValue");
        kerberosConfig.put("testConfigType", properties);
        org.easymock.Capture<? extends java.util.Set<java.lang.String>> captureUpdatedConfigTypes = testProcessWithKerberos(null, "defaultTestValue", kerberosConfig);
        java.util.Set<java.lang.String> updatedConfigTypes = captureUpdatedConfigTypes.getValue();
        org.junit.Assert.assertEquals(1, updatedConfigTypes.size());
    }

    @org.junit.Test
    public void testProcessWithKerberos_DontUpdateKererosConfigProperty_WithOrphanedKerberosConfigType() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> kerberosConfig = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> properties = new java.util.HashMap<>();
        properties.put("testProperty", "KERBEROStestValue");
        kerberosConfig.put("orphanedTestConfigType", properties);
        org.easymock.Capture<? extends java.util.Set<java.lang.String>> captureUpdatedConfigTypes = testProcessWithKerberos(null, "defaultTestValue", kerberosConfig);
        java.util.Set<java.lang.String> updatedConfigTypes = captureUpdatedConfigTypes.getValue();
        org.junit.Assert.assertEquals(1, updatedConfigTypes.size());
    }

    private org.easymock.Capture<? extends java.util.Set<java.lang.String>> testProcessWithKerberos(java.lang.String blueprintPropertyValue, java.lang.String stackPropertyValue, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> kerberosConfig) throws org.apache.ambari.server.AmbariException, org.apache.ambari.server.serveraction.kerberos.KerberosInvalidConfigurationException, org.apache.ambari.server.controller.internal.ConfigurationTopologyException {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> existingConfig = new java.util.HashMap<>();
        org.apache.ambari.server.topology.Configuration stackDefaultConfig = new org.apache.ambari.server.topology.Configuration(existingConfig, new java.util.HashMap<>());
        if (stackPropertyValue != null) {
            stackDefaultConfig.setProperty("testConfigType", "testProperty", stackPropertyValue);
        }
        org.apache.ambari.server.topology.Configuration blueprintConfig = new org.apache.ambari.server.topology.Configuration(stackDefaultConfig.getFullProperties(), new java.util.HashMap<>());
        if (blueprintPropertyValue != null) {
            blueprintConfig.setProperty("testConfigType", "testProperty", blueprintPropertyValue);
        }
        org.powermock.api.easymock.PowerMock.mockStatic(org.apache.ambari.server.topology.AmbariContext.class);
        org.apache.ambari.server.topology.AmbariContext.getController();
        EasyMock.expectLastCall().andReturn(controller).anyTimes();
        EasyMock.expect(controller.getClusters()).andReturn(clusters).anyTimes();
        EasyMock.expect(controller.getKerberosHelper()).andReturn(kerberosHelper).times(1);
        EasyMock.expect(clusters.getCluster("testCluster")).andReturn(cluster).anyTimes();
        EasyMock.expect(blueprint.getStack()).andReturn(stack).anyTimes();
        EasyMock.expect(stack.getName()).andReturn(STACK_NAME).anyTimes();
        EasyMock.expect(stack.getVersion()).andReturn(STACK_VERSION).anyTimes();
        EasyMock.expect(stack.getServiceForConfigType("testConfigType")).andReturn("KERBEROS").anyTimes();
        EasyMock.expect(stack.getAllConfigurationTypes(EasyMock.anyString())).andReturn(java.util.Collections.singletonList("testConfigType")).anyTimes();
        EasyMock.expect(stack.getExcludedConfigurationTypes(EasyMock.anyString())).andReturn(java.util.Collections.emptySet()).anyTimes();
        EasyMock.expect(stack.getConfigurationPropertiesWithMetadata(EasyMock.anyString(), EasyMock.anyString())).andReturn(java.util.Collections.emptyMap()).anyTimes();
        java.util.Set<java.lang.String> services = new java.util.HashSet<>();
        services.add("HDFS");
        services.add("KERBEROS");
        services.add("ZOOKEPER");
        EasyMock.expect(blueprint.getServices()).andReturn(services).anyTimes();
        EasyMock.expect(stack.getConfiguration(services)).andReturn(stackDefaultConfig).once();
        java.util.List<java.lang.String> hdfsComponents = new java.util.ArrayList<>();
        hdfsComponents.add("NAMENODE");
        java.util.List<java.lang.String> kerberosComponents = new java.util.ArrayList<>();
        kerberosComponents.add("KERBEROS_CLIENT");
        java.util.List<java.lang.String> zookeeperComponents = new java.util.ArrayList<>();
        zookeeperComponents.add("ZOOKEEPER_SERVER");
        EasyMock.expect(blueprint.getComponents("HDFS")).andReturn(hdfsComponents).anyTimes();
        EasyMock.expect(blueprint.getComponents("KERBEROS")).andReturn(kerberosComponents).anyTimes();
        EasyMock.expect(blueprint.getComponents("ZOOKEPER")).andReturn(zookeeperComponents).anyTimes();
        EasyMock.expect(topology.getAmbariContext()).andReturn(ambariContext).anyTimes();
        EasyMock.expect(topology.getConfigRecommendationStrategy()).andReturn(org.apache.ambari.server.topology.ConfigRecommendationStrategy.NEVER_APPLY).anyTimes();
        EasyMock.expect(topology.getBlueprint()).andReturn(blueprint).anyTimes();
        EasyMock.expect(blueprint.isValidConfigType("testConfigType")).andReturn(true).anyTimes();
        EasyMock.expect(topology.getConfiguration()).andReturn(blueprintConfig).anyTimes();
        EasyMock.expect(topology.getHostGroupInfo()).andReturn(java.util.Collections.emptyMap()).anyTimes();
        EasyMock.expect(topology.getClusterId()).andReturn(java.lang.Long.valueOf(1)).anyTimes();
        EasyMock.expect(topology.getHostGroupsForComponent(EasyMock.anyString())).andReturn(java.util.Collections.emptyList()).anyTimes();
        EasyMock.expect(ambariContext.getConfigHelper()).andReturn(configHelper).anyTimes();
        EasyMock.expect(ambariContext.getClusterName(java.lang.Long.valueOf(1))).andReturn("testCluster").anyTimes();
        EasyMock.expect(ambariContext.createConfigurationRequests(org.easymock.EasyMock.anyObject())).andReturn(java.util.Collections.emptyList()).anyTimes();
        EasyMock.expect(configHelper.getDefaultStackProperties(org.easymock.EasyMock.eq(new org.apache.ambari.server.state.StackId(STACK_NAME, STACK_VERSION)))).andReturn(stackProperties).anyTimes();
        if (kerberosConfig == null) {
            kerberosConfig = new java.util.HashMap<>();
            java.util.Map<java.lang.String, java.lang.String> properties = new java.util.HashMap<>();
            properties.put("testProperty", "KERBEROStestValue");
            kerberosConfig.put("testConfigType", properties);
        }
        EasyMock.expect(kerberosHelper.ensureHeadlessIdentities(EasyMock.anyObject(org.apache.ambari.server.state.Cluster.class), org.easymock.EasyMock.anyObject(), org.easymock.EasyMock.anyObject())).andReturn(true).once();
        EasyMock.expect(kerberosHelper.getServiceConfigurationUpdates(EasyMock.anyObject(org.apache.ambari.server.state.Cluster.class), org.easymock.EasyMock.anyObject(), org.easymock.EasyMock.anyObject(), org.easymock.EasyMock.anyObject(), org.easymock.EasyMock.anyObject(), EasyMock.anyBoolean(), EasyMock.eq(false))).andReturn(kerberosConfig).once();
        org.easymock.Capture<? extends java.lang.String> captureClusterName = EasyMock.newCapture(CaptureType.ALL);
        org.easymock.Capture<? extends java.util.Set<java.lang.String>> captureUpdatedConfigTypes = EasyMock.newCapture(CaptureType.ALL);
        ambariContext.waitForConfigurationResolution(EasyMock.capture(captureClusterName), EasyMock.capture(captureUpdatedConfigTypes));
        EasyMock.expectLastCall();
        org.powermock.api.easymock.PowerMock.replay(stack, blueprint, topology, controller, clusters, kerberosHelper, ambariContext, org.apache.ambari.server.topology.AmbariContext.class, configHelper);
        org.apache.ambari.server.topology.ClusterConfigurationRequest clusterConfigurationRequest = new org.apache.ambari.server.topology.ClusterConfigurationRequest(ambariContext, topology, false, stackAdvisorBlueprintProcessor, true);
        clusterConfigurationRequest.process();
        EasyMock.verify(blueprint, topology, ambariContext, controller, kerberosHelper, configHelper);
        java.lang.String clusterName = captureClusterName.getValue();
        org.junit.Assert.assertEquals("testCluster", clusterName);
        return captureUpdatedConfigTypes;
    }

    @org.junit.Test
    public void testProcessClusterConfigRequestDontIncludeKererosConfigs() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> existingConfig = new java.util.HashMap<>();
        org.apache.ambari.server.topology.Configuration stackConfig = new org.apache.ambari.server.topology.Configuration(existingConfig, new java.util.HashMap<>());
        org.powermock.api.easymock.PowerMock.mockStatic(org.apache.ambari.server.topology.AmbariContext.class);
        org.apache.ambari.server.topology.AmbariContext.getController();
        EasyMock.expectLastCall().andReturn(controller).anyTimes();
        EasyMock.expect(controller.getClusters()).andReturn(clusters).anyTimes();
        EasyMock.expect(clusters.getCluster("testCluster")).andReturn(cluster).anyTimes();
        EasyMock.expect(blueprint.getStack()).andReturn(stack).anyTimes();
        EasyMock.expect(stack.getName()).andReturn(STACK_NAME).anyTimes();
        EasyMock.expect(stack.getVersion()).andReturn(STACK_VERSION).anyTimes();
        EasyMock.expect(stack.getAllConfigurationTypes(EasyMock.anyString())).andReturn(java.util.Collections.singletonList("testConfigType")).anyTimes();
        EasyMock.expect(stack.getExcludedConfigurationTypes(EasyMock.anyString())).andReturn(java.util.Collections.emptySet()).anyTimes();
        EasyMock.expect(stack.getConfigurationPropertiesWithMetadata(EasyMock.anyString(), EasyMock.anyString())).andReturn(java.util.Collections.emptyMap()).anyTimes();
        java.util.Set<java.lang.String> services = new java.util.HashSet<>();
        services.add("HDFS");
        services.add("KERBEROS");
        services.add("ZOOKEPER");
        EasyMock.expect(blueprint.getServices()).andReturn(services).anyTimes();
        java.util.List<java.lang.String> hdfsComponents = new java.util.ArrayList<>();
        hdfsComponents.add("NAMENODE");
        java.util.List<java.lang.String> kerberosComponents = new java.util.ArrayList<>();
        kerberosComponents.add("KERBEROS_CLIENT");
        java.util.List<java.lang.String> zookeeperComponents = new java.util.ArrayList<>();
        zookeeperComponents.add("ZOOKEEPER_SERVER");
        EasyMock.expect(blueprint.getComponents("HDFS")).andReturn(hdfsComponents).anyTimes();
        EasyMock.expect(blueprint.getComponents("KERBEROS")).andReturn(kerberosComponents).anyTimes();
        EasyMock.expect(blueprint.getComponents("ZOOKEPER")).andReturn(zookeeperComponents).anyTimes();
        EasyMock.expect(topology.getAmbariContext()).andReturn(ambariContext).anyTimes();
        EasyMock.expect(topology.getConfigRecommendationStrategy()).andReturn(org.apache.ambari.server.topology.ConfigRecommendationStrategy.NEVER_APPLY).anyTimes();
        EasyMock.expect(topology.getBlueprint()).andReturn(blueprint).anyTimes();
        EasyMock.expect(topology.getConfiguration()).andReturn(stackConfig).anyTimes();
        EasyMock.expect(topology.getHostGroupInfo()).andReturn(java.util.Collections.emptyMap()).anyTimes();
        EasyMock.expect(topology.getClusterId()).andReturn(java.lang.Long.valueOf(1)).anyTimes();
        EasyMock.expect(ambariContext.getConfigHelper()).andReturn(configHelper).anyTimes();
        EasyMock.expect(ambariContext.getClusterName(java.lang.Long.valueOf(1))).andReturn("testCluster").anyTimes();
        EasyMock.expect(ambariContext.createConfigurationRequests(org.easymock.EasyMock.anyObject())).andReturn(java.util.Collections.emptyList()).anyTimes();
        EasyMock.expect(configHelper.getDefaultStackProperties(org.easymock.EasyMock.eq(new org.apache.ambari.server.state.StackId(STACK_NAME, STACK_VERSION)))).andReturn(stackProperties).anyTimes();
        org.powermock.api.easymock.PowerMock.replay(stack, blueprint, topology, controller, clusters, ambariContext, org.apache.ambari.server.topology.AmbariContext.class, configHelper);
        org.apache.ambari.server.topology.ClusterConfigurationRequest clusterConfigurationRequest = new org.apache.ambari.server.topology.ClusterConfigurationRequest(ambariContext, topology, false, stackAdvisorBlueprintProcessor);
        clusterConfigurationRequest.process();
        EasyMock.verify(blueprint, topology, ambariContext, controller, configHelper);
    }

    @org.junit.Test
    public void testProcessClusterConfigRequestRemoveUnusedConfigTypes() throws java.lang.Exception {
        org.apache.ambari.server.topology.Configuration configuration = createConfigurations();
        java.util.Set<java.lang.String> services = new java.util.HashSet<>();
        services.add("HDFS");
        services.add("RANGER");
        java.util.Map<java.lang.String, org.apache.ambari.server.topology.HostGroupInfo> hostGroupInfoMap = com.google.common.collect.Maps.newHashMap();
        org.apache.ambari.server.topology.HostGroupInfo hg1 = new org.apache.ambari.server.topology.HostGroupInfo("hg1");
        hg1.setConfiguration(createConfigurationsForHostGroup());
        hostGroupInfoMap.put("hg1", hg1);
        EasyMock.expect(topology.getAmbariContext()).andReturn(ambariContext).anyTimes();
        EasyMock.expect(topology.getConfiguration()).andReturn(configuration).anyTimes();
        EasyMock.expect(topology.getBlueprint()).andReturn(blueprint).anyTimes();
        EasyMock.expect(topology.getHostGroupInfo()).andReturn(hostGroupInfoMap);
        EasyMock.expect(blueprint.getStack()).andReturn(stack).anyTimes();
        EasyMock.expect(blueprint.getServices()).andReturn(services).anyTimes();
        EasyMock.expect(blueprint.isValidConfigType("hdfs-site")).andReturn(true).anyTimes();
        EasyMock.expect(blueprint.isValidConfigType("admin-properties")).andReturn(true).anyTimes();
        EasyMock.expect(blueprint.isValidConfigType("yarn-site")).andReturn(false).anyTimes();
        EasyMock.expect(blueprint.isValidConfigType("cluster-env")).andReturn(true).anyTimes();
        EasyMock.expect(blueprint.isValidConfigType("global")).andReturn(true).anyTimes();
        EasyMock.expect(ambariContext.getConfigHelper()).andReturn(configHelper).anyTimes();
        EasyMock.expect(configHelper.getDefaultStackProperties(org.easymock.EasyMock.eq(new org.apache.ambari.server.state.StackId(STACK_NAME, STACK_VERSION)))).andReturn(stackProperties).anyTimes();
        org.easymock.EasyMock.replay(stack, blueprint, topology, ambariContext, configHelper);
        new org.apache.ambari.server.topology.ClusterConfigurationRequest(ambariContext, topology, false, stackAdvisorBlueprintProcessor);
        org.junit.Assert.assertFalse("YARN service not present in topology config thus 'yarn-site' config type should be removed from config.", configuration.getFullProperties().containsKey("yarn-site"));
        org.junit.Assert.assertTrue("HDFS service is present in topology host group config thus 'hdfs-site' config type should be left in the config.", configuration.getFullAttributes().containsKey("hdfs-site"));
        org.junit.Assert.assertTrue("'cluster-env' config type should not be removed from configuration.", configuration.getFullProperties().containsKey("cluster-env"));
        org.junit.Assert.assertTrue("'global' config type should not be removed from configuration.", configuration.getFullProperties().containsKey("global"));
        org.junit.Assert.assertFalse("SPARK service not present in topology host group config thus 'spark-env' config type should be removed from config.", hg1.getConfiguration().getFullAttributes().containsKey("spark-env"));
        org.junit.Assert.assertTrue("HDFS service is present in topology host group config thus 'hdfs-site' config type should be left in the config.", hg1.getConfiguration().getFullAttributes().containsKey("hdfs-site"));
        EasyMock.verify(stack, blueprint, topology, ambariContext, configHelper);
    }

    @org.junit.Test
    public void testProcessClusterConfigRequestWithOnlyHostGroupConfigRemoveUnusedConfigTypes() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> config = com.google.common.collect.Maps.newHashMap();
        config.put("cluster-env", new java.util.HashMap<>());
        config.put("global", new java.util.HashMap<>());
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>> attributes = com.google.common.collect.Maps.newHashMap();
        org.apache.ambari.server.topology.Configuration configuration = new org.apache.ambari.server.topology.Configuration(config, attributes);
        java.util.Set<java.lang.String> services = new java.util.HashSet<>();
        services.add("HDFS");
        services.add("RANGER");
        java.util.Map<java.lang.String, org.apache.ambari.server.topology.HostGroupInfo> hostGroupInfoMap = com.google.common.collect.Maps.newHashMap();
        org.apache.ambari.server.topology.HostGroupInfo hg1 = new org.apache.ambari.server.topology.HostGroupInfo("hg1");
        hg1.setConfiguration(createConfigurationsForHostGroup());
        hostGroupInfoMap.put("hg1", hg1);
        EasyMock.expect(topology.getAmbariContext()).andReturn(ambariContext).anyTimes();
        EasyMock.expect(topology.getConfiguration()).andReturn(configuration).anyTimes();
        EasyMock.expect(topology.getBlueprint()).andReturn(blueprint).anyTimes();
        EasyMock.expect(topology.getHostGroupInfo()).andReturn(hostGroupInfoMap);
        EasyMock.expect(blueprint.getStack()).andReturn(stack).anyTimes();
        EasyMock.expect(blueprint.getServices()).andReturn(services).anyTimes();
        EasyMock.expect(blueprint.isValidConfigType("hdfs-site")).andReturn(true).anyTimes();
        EasyMock.expect(blueprint.isValidConfigType("cluster-env")).andReturn(true).anyTimes();
        EasyMock.expect(blueprint.isValidConfigType("global")).andReturn(true).anyTimes();
        EasyMock.expect(ambariContext.getConfigHelper()).andReturn(configHelper).anyTimes();
        EasyMock.expect(configHelper.getDefaultStackProperties(org.easymock.EasyMock.eq(new org.apache.ambari.server.state.StackId(STACK_NAME, STACK_VERSION)))).andReturn(stackProperties).anyTimes();
        org.easymock.EasyMock.replay(stack, blueprint, topology, ambariContext, configHelper);
        new org.apache.ambari.server.topology.ClusterConfigurationRequest(ambariContext, topology, false, stackAdvisorBlueprintProcessor);
        org.junit.Assert.assertTrue("'cluster-env' config type should not be removed from configuration.", configuration.getFullProperties().containsKey("cluster-env"));
        org.junit.Assert.assertTrue("'global' config type should not be removed from configuration.", configuration.getFullProperties().containsKey("global"));
        org.junit.Assert.assertFalse("SPARK service not present in topology host group config thus 'spark-env' config type should be removed from config.", hg1.getConfiguration().getFullAttributes().containsKey("spark-env"));
        org.junit.Assert.assertTrue("HDFS service is present in topology host group config thus 'hdfs-site' config type should be left in the config.", hg1.getConfiguration().getFullAttributes().containsKey("hdfs-site"));
        EasyMock.verify(stack, blueprint, topology, ambariContext, configHelper);
    }

    private org.apache.ambari.server.topology.Configuration createConfigurations() {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> firstLevelConfig = com.google.common.collect.Maps.newHashMap();
        firstLevelConfig.put("hdfs-site", new java.util.HashMap<>());
        firstLevelConfig.put("yarn-site", new java.util.HashMap<>());
        firstLevelConfig.put("cluster-env", new java.util.HashMap<>());
        firstLevelConfig.put("global", new java.util.HashMap<>());
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>> firstLevelAttributes = com.google.common.collect.Maps.newHashMap();
        firstLevelAttributes.put("hdfs-site", new java.util.HashMap<>());
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> secondLevelConfig = com.google.common.collect.Maps.newHashMap();
        secondLevelConfig.put("admin-properties", new java.util.HashMap<>());
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>> secondLevelAttributes = com.google.common.collect.Maps.newHashMap();
        secondLevelAttributes.put("admin-properties", new java.util.HashMap<>());
        org.apache.ambari.server.topology.Configuration secondLevelConf = new org.apache.ambari.server.topology.Configuration(secondLevelConfig, secondLevelAttributes);
        return new org.apache.ambari.server.topology.Configuration(firstLevelConfig, firstLevelAttributes, secondLevelConf);
    }

    private org.apache.ambari.server.topology.Configuration createConfigurationsForHostGroup() {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> firstLevelConfig = com.google.common.collect.Maps.newHashMap();
        firstLevelConfig.put("hdfs-site", new java.util.HashMap<>());
        firstLevelConfig.put("spark-env", new java.util.HashMap<>());
        firstLevelConfig.put("cluster-env", new java.util.HashMap<>());
        firstLevelConfig.put("global", new java.util.HashMap<>());
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>> firstLevelAttributes = com.google.common.collect.Maps.newHashMap();
        firstLevelAttributes.put("hdfs-site", new java.util.HashMap<>());
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> secondLevelConfig = com.google.common.collect.Maps.newHashMap();
        secondLevelConfig.put("admin-properties", new java.util.HashMap<>());
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>> secondLevelAttributes = com.google.common.collect.Maps.newHashMap();
        secondLevelAttributes.put("admin-properties", new java.util.HashMap<>());
        org.apache.ambari.server.topology.Configuration secondLevelConf = new org.apache.ambari.server.topology.Configuration(secondLevelConfig, secondLevelAttributes);
        return new org.apache.ambari.server.topology.Configuration(firstLevelConfig, firstLevelAttributes, secondLevelConf);
    }
}