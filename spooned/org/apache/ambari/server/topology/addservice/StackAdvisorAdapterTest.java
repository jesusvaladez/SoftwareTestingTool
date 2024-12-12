package org.apache.ambari.server.topology.addservice;
import org.easymock.EasyMockRunner;
import org.easymock.IExpectationSetters;
import org.easymock.Mock;
import org.easymock.TestSubject;
import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.anyString;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.getCurrentArguments;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
@org.junit.runner.RunWith(org.easymock.EasyMockRunner.class)
public class StackAdvisorAdapterTest {
    @org.easymock.Mock
    private org.apache.ambari.server.controller.AmbariManagementController managementController;

    @org.easymock.Mock
    private org.apache.ambari.server.api.services.stackadvisor.StackAdvisorHelper stackAdvisorHelper;

    @org.easymock.Mock
    private org.apache.ambari.server.configuration.Configuration serverConfig;

    @org.easymock.Mock
    private com.google.inject.Injector injector;

    @org.easymock.Mock
    private org.apache.ambari.server.controller.internal.Stack stack;

    @org.easymock.TestSubject
    private org.apache.ambari.server.topology.addservice.StackAdvisorAdapter adapter = new org.apache.ambari.server.topology.addservice.StackAdvisorAdapter();

    private static final java.util.Map<java.lang.String, java.util.Set<java.lang.String>> COMPONENT_HOST_MAP = com.google.common.collect.ImmutableMap.<java.lang.String, java.util.Set<java.lang.String>>builder().put("NAMENODE", com.google.common.collect.ImmutableSet.of("c7401", "c7402")).put("DATANODE", com.google.common.collect.ImmutableSet.of("c7403", "c7404", "c7405", "c7406")).put("HDFS_CLIENT", com.google.common.collect.ImmutableSet.of("c7403", "c7404", "c7405", "c7406")).put("ZOOKEEPER_SERVER", com.google.common.collect.ImmutableSet.of("c7401", "c7402")).put("ZOOKEEPER_CLIENT", com.google.common.collect.ImmutableSet.of("c7401", "c7402", "c7403", "c7404", "c7405", "c7406")).build();

    private static final java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Set<java.lang.String>>> SERVICE_COMPONENT_HOST_MAP_1 = com.google.common.collect.ImmutableMap.of("HDFS", com.google.common.collect.ImmutableMap.of("NAMENODE", com.google.common.collect.ImmutableSet.of("c7401", "c7402"), "DATANODE", com.google.common.collect.ImmutableSet.of("c7403", "c7404", "c7405", "c7406"), "HDFS_CLIENT", com.google.common.collect.ImmutableSet.of("c7403", "c7404", "c7405", "c7406")), "ZOOKEEPER", com.google.common.collect.ImmutableMap.of("ZOOKEEPER_SERVER", com.google.common.collect.ImmutableSet.of("c7401", "c7402"), "ZOOKEEPER_CLIENT", com.google.common.collect.ImmutableSet.of("c7401", "c7402", "c7403", "c7404", "c7405", "c7406")));

    private static final java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Set<java.lang.String>>> SERVICE_COMPONENT_HOST_MAP_2 = com.google.common.collect.ImmutableMap.<java.lang.String, java.util.Map<java.lang.String, java.util.Set<java.lang.String>>>builder().putAll(org.apache.ambari.server.topology.addservice.StackAdvisorAdapterTest.SERVICE_COMPONENT_HOST_MAP_1).put("HIVE", java.util.Collections.emptyMap()).put("SPARK2", java.util.Collections.emptyMap()).build();

    private static final java.util.Map<java.lang.String, java.util.Set<java.lang.String>> HOST_COMPONENT_MAP = com.google.common.collect.ImmutableMap.<java.lang.String, java.util.Set<java.lang.String>>builder().put("c7401", com.google.common.collect.ImmutableSet.of("NAMENODE", "ZOOKEEPER_SERVER", "ZOOKEEPER_CLIENT")).put("c7402", com.google.common.collect.ImmutableSet.of("NAMENODE", "ZOOKEEPER_SERVER", "ZOOKEEPER_CLIENT")).put("c7403", com.google.common.collect.ImmutableSet.of("DATANODE", "HDFS_CLIENT", "ZOOKEEPER_CLIENT")).put("c7404", com.google.common.collect.ImmutableSet.of("DATANODE", "HDFS_CLIENT", "ZOOKEEPER_CLIENT")).put("c7405", com.google.common.collect.ImmutableSet.of("DATANODE", "HDFS_CLIENT", "ZOOKEEPER_CLIENT")).put("c7406", com.google.common.collect.ImmutableSet.of("DATANODE", "HDFS_CLIENT", "ZOOKEEPER_CLIENT")).build();

    private final org.apache.ambari.server.topology.addservice.AddServiceInfo.Builder addServiceInfoBuilder = new org.apache.ambari.server.topology.addservice.AddServiceInfo.Builder().setClusterName("c1");

    @org.junit.Test
    public void getHostComponentMap() {
        org.junit.Assert.assertEquals(org.apache.ambari.server.topology.addservice.StackAdvisorAdapterTest.HOST_COMPONENT_MAP, org.apache.ambari.server.topology.addservice.StackAdvisorAdapter.getHostComponentMap(org.apache.ambari.server.topology.addservice.StackAdvisorAdapterTest.COMPONENT_HOST_MAP));
    }

    @org.junit.Test
    public void getComponentHostMap() {
        org.junit.Assert.assertEquals(org.apache.ambari.server.topology.addservice.StackAdvisorAdapterTest.COMPONENT_HOST_MAP, org.apache.ambari.server.topology.addservice.StackAdvisorAdapter.getComponentHostMap(org.apache.ambari.server.topology.addservice.StackAdvisorAdapterTest.SERVICE_COMPONENT_HOST_MAP_2));
    }

    @org.junit.Test
    public void getRecommendedLayout() {
        java.util.Map<java.lang.String, java.util.Set<java.lang.String>> hostGroups = com.google.common.collect.ImmutableMap.of("host_group1", com.google.common.collect.ImmutableSet.of("c7401", "c7402"), "host_group2", com.google.common.collect.ImmutableSet.of("c7403", "c7404", "c7405", "c7406"));
        java.util.Map<java.lang.String, java.util.Set<java.lang.String>> hostGroupComponents = com.google.common.collect.ImmutableMap.of("host_group1", com.google.common.collect.ImmutableSet.of("NAMENODE", "ZOOKEEPER_SERVER", "ZOOKEEPER_CLIENT"), "host_group2", com.google.common.collect.ImmutableSet.of("DATANODE", "HDFS_CLIENT", "ZOOKEEPER_CLIENT"));
        java.util.Map<java.lang.String, java.lang.String> serviceToComponent = com.google.common.collect.ImmutableMap.<java.lang.String, java.lang.String>builder().put("NAMENODE", "HDFS").put("DATANODE", "HDFS").put("HDFS_CLIENT", "HDFS").put("ZOOKEEPER_SERVER", "ZOOKEEPER").put("ZOOKEEPER_CLIENT", "ZOOKEEPER").build();
        org.junit.Assert.assertEquals(org.apache.ambari.server.topology.addservice.StackAdvisorAdapterTest.SERVICE_COMPONENT_HOST_MAP_1, org.apache.ambari.server.topology.addservice.StackAdvisorAdapter.getRecommendedLayout(hostGroups, hostGroupComponents, serviceToComponent::get));
    }

    @org.junit.Test
    public void mergeDisjunctMaps() {
        java.util.Map<java.lang.String, java.lang.String> map1 = com.google.common.collect.ImmutableMap.of("key1", "value1", "key2", "value2");
        java.util.Map<java.lang.String, java.lang.String> map2 = com.google.common.collect.ImmutableMap.of("key3", "value3", "key4", "value4");
        org.junit.Assert.assertEquals(com.google.common.collect.ImmutableMap.of("key1", "value1", "key2", "value2", "key3", "value3", "key4", "value4"), org.apache.ambari.server.topology.addservice.StackAdvisorAdapter.mergeDisjunctMaps(map1, map2));
    }

    @org.junit.Test(expected = java.lang.IllegalArgumentException.class)
    public void mergeDisjunctMaps_invalidInput() {
        java.util.Map<java.lang.String, java.lang.String> map1 = com.google.common.collect.ImmutableMap.of("key1", "value1", "key2", "value2");
        java.util.Map<java.lang.String, java.lang.String> map2 = com.google.common.collect.ImmutableMap.of("key2", "value2", "key3", "value3");
        org.apache.ambari.server.topology.addservice.StackAdvisorAdapter.mergeDisjunctMaps(map1, map2);
    }

    @org.junit.Test
    public void keepNewServicesOnly() {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Set<java.lang.String>>> newServices = com.google.common.collect.ImmutableMap.of("KAFKA", java.util.Collections.emptyMap(), "PIG", java.util.Collections.emptyMap());
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Set<java.lang.String>>> expectedNewServiceRecommendations = com.google.common.collect.ImmutableMap.of("KAFKA", com.google.common.collect.ImmutableMap.of("KAFKA_BROKER", com.google.common.collect.ImmutableSet.of("c7405")), "PIG", com.google.common.collect.ImmutableMap.of("PIG_CLIENT", com.google.common.collect.ImmutableSet.of("c7405", "c7406")));
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Set<java.lang.String>>> recommendations = new java.util.HashMap<>(org.apache.ambari.server.topology.addservice.StackAdvisorAdapterTest.SERVICE_COMPONENT_HOST_MAP_1);
        recommendations.putAll(expectedNewServiceRecommendations);
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Set<java.lang.String>>> newServiceRecommendations = org.apache.ambari.server.topology.addservice.StackAdvisorAdapter.keepNewServicesOnly(recommendations, newServices);
        org.junit.Assert.assertEquals(expectedNewServiceRecommendations, newServiceRecommendations);
    }

    @org.junit.Before
    public void setUp() throws java.lang.Exception {
        org.apache.ambari.server.state.Cluster cluster = EasyMock.mock(org.apache.ambari.server.state.Cluster.class);
        EasyMock.expect(cluster.getHostNames()).andReturn(com.google.common.collect.ImmutableSet.of("c7401", "c7402"));
        EasyMock.expect(cluster.getServices()).andReturn(com.google.common.collect.ImmutableMap.of("HDFS", org.apache.ambari.server.topology.addservice.StackAdvisorAdapterTest.service("HDFS", com.google.common.collect.ImmutableMap.of("NAMENODE", com.google.common.collect.ImmutableSet.of("c7401"), "HDFS_CLIENT", com.google.common.collect.ImmutableSet.of("c7401", "c7402"))), "ZOOKEEPER", org.apache.ambari.server.topology.addservice.StackAdvisorAdapterTest.service("ZOOKEEPER", com.google.common.collect.ImmutableMap.of("ZOOKEEPER_SERVER", com.google.common.collect.ImmutableSet.of("c7401"), "ZOOKEEPER_CLIENT", com.google.common.collect.ImmutableSet.of("c7401", "c7402"))), "MAPREDUCE2", org.apache.ambari.server.topology.addservice.StackAdvisorAdapterTest.service("MAPREDUCE2", com.google.common.collect.ImmutableMap.of("HISTORYSERVER", com.google.common.collect.ImmutableSet.of("c7401")))));
        org.apache.ambari.server.state.Clusters clusters = EasyMock.mock(org.apache.ambari.server.state.Clusters.class);
        EasyMock.expect(clusters.getCluster(EasyMock.anyString())).andReturn(cluster).anyTimes();
        EasyMock.expect(managementController.getClusters()).andReturn(clusters).anyTimes();
        EasyMock.replay(clusters, cluster, managementController);
        EasyMock.expect(serverConfig.getGplLicenseAccepted()).andReturn(java.lang.Boolean.FALSE).anyTimes();
        @java.lang.SuppressWarnings("unchecked")
        org.easymock.IExpectationSetters iExpectationSetters = EasyMock.expect(serverConfig.getAddServiceHostGroupStrategyClass()).andReturn(((java.lang.Class) (org.apache.ambari.server.topology.addservice.GroupByComponentsStrategy.class))).anyTimes();
        EasyMock.replay(serverConfig);
        EasyMock.expect(injector.getInstance(org.apache.ambari.server.topology.addservice.GroupByComponentsStrategy.class)).andReturn(new org.apache.ambari.server.topology.addservice.GroupByComponentsStrategy()).anyTimes();
        EasyMock.replay(injector);
        org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.BlueprintClusterBinding binding = org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.BlueprintClusterBinding.fromHostGroupHostMap(com.google.common.collect.ImmutableMap.of("hostgroup-1", com.google.common.collect.ImmutableSet.of("c7401"), "hostgroup-2", com.google.common.collect.ImmutableSet.of("c7402")));
        org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.Blueprint blueprint = new org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.Blueprint();
        blueprint.setHostGroups(org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.HostGroup.fromHostGroupComponents(com.google.common.collect.ImmutableMap.of("hostgroup-1", com.google.common.collect.ImmutableSet.of("NAMENODE", "HDFS_CLIENT", "ZOOKEEPER_SERVER", "ZOOKEEPER_CLIENT", "HISTORYSERVER"), "hostgroup-2", com.google.common.collect.ImmutableSet.of("HDFS_CLIENT", "ZOOKEEPER_CLIENT", "KAFKA_BROKER"))));
        org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse layoutResponse = org.apache.ambari.server.topology.addservice.StackAdvisorAdapterTest.createRecommendation(blueprint, binding);
        org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.Blueprint configBlueprint = new org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.Blueprint();
        org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.BlueprintConfigurations kafkaBroker = org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.BlueprintConfigurations.create(com.google.common.collect.ImmutableMap.of("log.dirs", "/kafka-logs", "offsets.topic.replication.factor", "1"), com.google.common.collect.ImmutableMap.of("maximum", com.google.common.collect.ImmutableMap.of("offsets.topic.replication.factor", "10")));
        org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.BlueprintConfigurations spark2Defaults = org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.BlueprintConfigurations.create(com.google.common.collect.ImmutableMap.of("spark.yarn.queue", "default"), null);
        org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.BlueprintConfigurations mapredSite = org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.BlueprintConfigurations.create(com.google.common.collect.ImmutableMap.of("mapreduce.map.memory.mb", "682", "mapreduce.reduce.memory.mb", "1364"), com.google.common.collect.ImmutableMap.of("minimum", com.google.common.collect.ImmutableMap.of("mapreduce.map.memory.mb", "682", "mapreduce.reduce.memory.mb", "682"), "maximum", com.google.common.collect.ImmutableMap.of("mapreduce.map.memory.mb", "2046", "mapreduce.reduce.memory.mb", "2046")));
        configBlueprint.setConfigurations(org.apache.ambari.server.testutils.TestCollectionUtils.map("kafka-broker", kafkaBroker, "spark2-defaults", spark2Defaults, "mapred-site", mapredSite));
        org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse configResponse = org.apache.ambari.server.topology.addservice.StackAdvisorAdapterTest.createRecommendation(configBlueprint, binding);
        EasyMock.expect(stackAdvisorHelper.recommend(EasyMock.anyObject())).andAnswer(() -> {
            org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequest request = ((org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequest) (getCurrentArguments()[0]));
            assertNotNull(request.getHosts());
            assertNotNull(request.getServices());
            assertNotNull(request.getStackName());
            assertNotNull(request.getStackVersion());
            assertNotNull(request.getConfigurations());
            assertNotNull(request.getHostComponents());
            assertNotNull(request.getComponentHostsMap());
            assertNotNull(request.getHostGroupBindings());
            assertNotNull(request.getLdapConfig());
            assertNotNull(request.getRequestType());
            return request.getRequestType() == HOST_GROUPS ? layoutResponse : configResponse;
        });
        org.apache.ambari.server.api.services.stackadvisor.validations.ValidationResponse validationResponse = new org.apache.ambari.server.api.services.stackadvisor.validations.ValidationResponse();
        validationResponse.setItems(java.util.Collections.emptySet());
        EasyMock.expect(stackAdvisorHelper.validate(EasyMock.anyObject())).andReturn(validationResponse);
        EasyMock.replay(stackAdvisorHelper);
        EasyMock.expect(stack.getStackId()).andReturn(new org.apache.ambari.server.state.StackId("HDP", "3.0")).anyTimes();
        com.google.common.collect.ImmutableMap<java.lang.String, java.lang.String> serviceComponentMap = com.google.common.collect.ImmutableMap.<java.lang.String, java.lang.String>builder().put("KAFKA_BROKER", "KAFKA").put("NAMENODE", "HDFS").put("HDFS_CLIENT", "HDFS").put("ZOOKEEPER_SERVER", "ZOOKEEPER").put("ZOOKEEPER_CLIENT", "ZOOKEEPER").put("HISTORYSERVER", "MAPREDUCE2").build();
        EasyMock.expect(stack.getServiceForComponent(EasyMock.anyString())).andAnswer(() -> serviceComponentMap.get(getCurrentArguments()[0])).anyTimes();
        com.google.common.collect.ImmutableMap<java.lang.String, java.lang.String> configTypeServiceMap = com.google.common.collect.ImmutableMap.<java.lang.String, java.lang.String>builder().put("kafka-broker", "KAFKA").put("spark2-defaults", "SPARK2").put("mapred-site", "MAPREDUCE2").build();
        EasyMock.expect(stack.getServiceForConfigType(EasyMock.anyString())).andAnswer(() -> configTypeServiceMap.get(getCurrentArguments()[0])).anyTimes();
        EasyMock.expect(stack.getConfigurationPropertiesWithMetadata("OOZIE", "oozie-env")).andReturn(com.google.common.collect.ImmutableMap.of("mapreduce.map.memory.mb", org.apache.ambari.server.controller.internal.UnitUpdaterTest.configProperty("mapreduce.map.memory.mb", "MB"), "mapreduce.reduce.memory.mb", org.apache.ambari.server.controller.internal.UnitUpdaterTest.configProperty("mapreduce.reduce.memory.mb", "MB"))).anyTimes();
        EasyMock.replay(stack);
    }

    private static org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse createRecommendation(org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.Blueprint blueprint, org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.BlueprintClusterBinding binding) {
        org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse response = new org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse();
        org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.Recommendation recommendation = new org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.Recommendation();
        response.setRecommendations(recommendation);
        recommendation.setBlueprint(blueprint);
        recommendation.setBlueprintClusterBinding(binding);
        return response;
    }

    private static org.apache.ambari.server.state.Service service(java.lang.String name, com.google.common.collect.ImmutableMap<java.lang.String, com.google.common.collect.ImmutableSet<java.lang.String>> componentHostMap) {
        org.apache.ambari.server.state.Service service = EasyMock.mock(org.apache.ambari.server.state.Service.class);
        EasyMock.expect(service.getName()).andReturn(name).anyTimes();
        java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceComponent> serviceComponents = componentHostMap.entrySet().stream().map(entry -> {
            org.apache.ambari.server.state.ServiceComponent component = EasyMock.mock(org.apache.ambari.server.state.ServiceComponent.class);
            EasyMock.expect(component.getName()).andReturn(entry.getKey()).anyTimes();
            EasyMock.expect(component.getServiceComponentsHosts()).andReturn(entry.getValue()).anyTimes();
            EasyMock.replay(component);
            return org.apache.commons.lang3.tuple.Pair.of(entry.getKey(), component);
        }).collect(java.util.stream.Collectors.toMap(org.apache.commons.lang3.tuple.Pair::getKey, org.apache.commons.lang3.tuple.Pair::getValue));
        EasyMock.expect(service.getServiceComponents()).andReturn(serviceComponents).anyTimes();
        EasyMock.replay(service);
        return service;
    }

    @org.junit.Test
    public void recommendLayout() {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Set<java.lang.String>>> newServices = com.google.common.collect.ImmutableMap.of("KAFKA", com.google.common.collect.ImmutableMap.of("KAFKA_BROKER", java.util.Collections.emptySet()));
        org.apache.ambari.server.topology.addservice.AddServiceInfo info = addServiceInfoBuilder.setStack(stack).setConfig(org.apache.ambari.server.topology.Configuration.newEmpty()).setNewServices(newServices).build();
        org.apache.ambari.server.topology.addservice.AddServiceInfo infoWithRecommendations = adapter.recommendLayout(info);
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Set<java.lang.String>>> expectedNewLayout = com.google.common.collect.ImmutableMap.of("KAFKA", com.google.common.collect.ImmutableMap.of("KAFKA_BROKER", com.google.common.collect.ImmutableSet.of("c7402")));
        org.junit.Assert.assertEquals(expectedNewLayout, infoWithRecommendations.newServices());
    }

    @org.junit.Test
    public void recommendConfigurations_noLayoutInfo() {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Set<java.lang.String>>> newServices = com.google.common.collect.ImmutableMap.of("KAFKA", com.google.common.collect.ImmutableMap.of("KAFKA_BROKER", com.google.common.collect.ImmutableSet.of("c7401")), "SPARK2", com.google.common.collect.ImmutableMap.of("SPARK2_JOBHISTORYSERVER", com.google.common.collect.ImmutableSet.of("c7402"), "SPARK2_CLIENT", com.google.common.collect.ImmutableSet.of("c7403", "c7404")), "OOZIE", com.google.common.collect.ImmutableMap.of("OOZIE_SERVER", com.google.common.collect.ImmutableSet.of("c7401"), "OOZIE_CLIENT", com.google.common.collect.ImmutableSet.of("c7403", "c7404")));
        org.apache.ambari.server.topology.Configuration stackConfig = org.apache.ambari.server.topology.Configuration.newEmpty();
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(org.apache.ambari.server.testutils.TestCollectionUtils.map("oozie-env", org.apache.ambari.server.testutils.TestCollectionUtils.map("oozie_heapsize", "1024", "oozie_permsize", "256")), java.util.Collections.emptyMap());
        org.apache.ambari.server.topology.Configuration userConfig = org.apache.ambari.server.topology.Configuration.newEmpty();
        userConfig.setParentConfiguration(clusterConfig);
        clusterConfig.setParentConfiguration(stackConfig);
        org.apache.ambari.server.topology.addservice.AddServiceRequest request = org.apache.ambari.server.topology.addservice.StackAdvisorAdapterTest.request(org.apache.ambari.server.topology.ConfigRecommendationStrategy.ALWAYS_APPLY);
        org.apache.ambari.server.topology.addservice.AddServiceInfo info = addServiceInfoBuilder.setRequest(request).setStack(stack).setConfig(userConfig).setNewServices(newServices).build();
        org.apache.ambari.server.topology.addservice.AddServiceInfo infoWithConfig = adapter.recommendConfigurations(info);
        org.apache.ambari.server.topology.Configuration recommendedConfig = infoWithConfig.getConfig();
        org.junit.Assert.assertSame(userConfig, recommendedConfig.getParentConfiguration());
        org.junit.Assert.assertSame(clusterConfig, userConfig.getParentConfiguration());
        org.junit.Assert.assertSame(stackConfig, clusterConfig.getParentConfiguration());
        org.junit.Assert.assertEquals(com.google.common.collect.ImmutableMap.of("kafka-broker", com.google.common.collect.ImmutableMap.of("log.dirs", "/kafka-logs", "offsets.topic.replication.factor", "1"), "spark2-defaults", com.google.common.collect.ImmutableMap.of("spark.yarn.queue", "default"), "oozie-env", com.google.common.collect.ImmutableMap.of("oozie_heapsize", "1024m", "oozie_permsize", "256m")), recommendedConfig.getProperties());
        org.junit.Assert.assertEquals(com.google.common.collect.ImmutableMap.of("kafka-broker", com.google.common.collect.ImmutableMap.of("maximum", com.google.common.collect.ImmutableMap.of("offsets.topic.replication.factor", "10"))), recommendedConfig.getAttributes());
    }

    @org.junit.Test
    public void recommendConfigurations_alwaysApply() {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Set<java.lang.String>>> newServices = com.google.common.collect.ImmutableMap.of("KAFKA", com.google.common.collect.ImmutableMap.of("KAFKA_BROKER", com.google.common.collect.ImmutableSet.of("c7401")), "SPARK2", com.google.common.collect.ImmutableMap.of("SPARK2_JOBHISTORYSERVER", com.google.common.collect.ImmutableSet.of("c7402"), "SPARK2_CLIENT", com.google.common.collect.ImmutableSet.of("c7403", "c7404")), "OOZIE", com.google.common.collect.ImmutableMap.of("OOZIE_SERVER", com.google.common.collect.ImmutableSet.of("c7401"), "OOZIE_CLIENT", com.google.common.collect.ImmutableSet.of("c7403", "c7404")));
        org.apache.ambari.server.topology.Configuration stackConfig = org.apache.ambari.server.topology.Configuration.newEmpty();
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(org.apache.ambari.server.testutils.TestCollectionUtils.map("oozie-env", org.apache.ambari.server.testutils.TestCollectionUtils.map("oozie_heapsize", "1024", "oozie_permsize", "256")), java.util.Collections.emptyMap());
        org.apache.ambari.server.topology.Configuration userConfig = org.apache.ambari.server.topology.Configuration.newEmpty();
        userConfig.setParentConfiguration(clusterConfig);
        clusterConfig.setParentConfiguration(stackConfig);
        org.apache.ambari.server.topology.addservice.LayoutRecommendationInfo layoutRecommendationInfo = new org.apache.ambari.server.topology.addservice.LayoutRecommendationInfo(new java.util.HashMap<>(), new java.util.HashMap<>());
        org.apache.ambari.server.topology.addservice.AddServiceRequest request = org.apache.ambari.server.topology.addservice.StackAdvisorAdapterTest.request(org.apache.ambari.server.topology.ConfigRecommendationStrategy.ALWAYS_APPLY);
        org.apache.ambari.server.topology.addservice.AddServiceInfo info = addServiceInfoBuilder.setRequest(request).setStack(stack).setConfig(userConfig).setNewServices(newServices).setRecommendationInfo(layoutRecommendationInfo).build();
        org.apache.ambari.server.topology.addservice.AddServiceInfo infoWithConfig = adapter.recommendConfigurations(info);
        org.apache.ambari.server.topology.Configuration recommendedConfig = infoWithConfig.getConfig();
        org.junit.Assert.assertSame(userConfig, recommendedConfig.getParentConfiguration());
        org.junit.Assert.assertSame(clusterConfig, userConfig.getParentConfiguration());
        org.junit.Assert.assertSame(stackConfig, clusterConfig.getParentConfiguration());
        org.junit.Assert.assertEquals(com.google.common.collect.ImmutableMap.of("kafka-broker", com.google.common.collect.ImmutableMap.of("log.dirs", "/kafka-logs", "offsets.topic.replication.factor", "1"), "spark2-defaults", com.google.common.collect.ImmutableMap.of("spark.yarn.queue", "default"), "oozie-env", com.google.common.collect.ImmutableMap.of("oozie_heapsize", "1024m", "oozie_permsize", "256m")), recommendedConfig.getProperties());
        org.junit.Assert.assertEquals(com.google.common.collect.ImmutableMap.of("kafka-broker", com.google.common.collect.ImmutableMap.of("maximum", com.google.common.collect.ImmutableMap.of("offsets.topic.replication.factor", "10"))), recommendedConfig.getAttributes());
    }

    @org.junit.Test
    public void recommendConfigurations_alwaysDoNotOverrideCustomValues() {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Set<java.lang.String>>> newServices = com.google.common.collect.ImmutableMap.of("KAFKA", com.google.common.collect.ImmutableMap.of("KAFKA_BROKER", com.google.common.collect.ImmutableSet.of("c7401")), "SPARK2", com.google.common.collect.ImmutableMap.of("SPARK2_JOBHISTORYSERVER", com.google.common.collect.ImmutableSet.of("c7402"), "SPARK2_CLIENT", com.google.common.collect.ImmutableSet.of("c7403", "c7404")), "OOZIE", com.google.common.collect.ImmutableMap.of("OOZIE_SERVER", com.google.common.collect.ImmutableSet.of("c7401"), "OOZIE_CLIENT", com.google.common.collect.ImmutableSet.of("c7403", "c7404")));
        org.apache.ambari.server.topology.Configuration stackConfig = org.apache.ambari.server.topology.Configuration.newEmpty();
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(org.apache.ambari.server.testutils.TestCollectionUtils.map("oozie-env", org.apache.ambari.server.testutils.TestCollectionUtils.map("oozie_heapsize", "1024", "oozie_permsize", "256")), java.util.Collections.emptyMap());
        org.apache.ambari.server.topology.Configuration userConfig = org.apache.ambari.server.topology.Configuration.newEmpty();
        userConfig.setParentConfiguration(clusterConfig);
        clusterConfig.setParentConfiguration(stackConfig);
        org.apache.ambari.server.topology.addservice.LayoutRecommendationInfo layoutRecommendationInfo = new org.apache.ambari.server.topology.addservice.LayoutRecommendationInfo(new java.util.HashMap<>(), new java.util.HashMap<>());
        org.apache.ambari.server.topology.addservice.AddServiceRequest request = org.apache.ambari.server.topology.addservice.StackAdvisorAdapterTest.request(org.apache.ambari.server.topology.ConfigRecommendationStrategy.ALWAYS_APPLY_DONT_OVERRIDE_CUSTOM_VALUES);
        org.apache.ambari.server.topology.addservice.AddServiceInfo info = addServiceInfoBuilder.setRequest(request).setStack(stack).setConfig(userConfig).setNewServices(newServices).setRecommendationInfo(layoutRecommendationInfo).build();
        org.apache.ambari.server.topology.addservice.AddServiceInfo infoWithConfig = adapter.recommendConfigurations(info);
        org.junit.Assert.assertSame(userConfig, infoWithConfig.getConfig());
        org.apache.ambari.server.topology.Configuration recommendedConfig = userConfig.getParentConfiguration();
        org.junit.Assert.assertSame(clusterConfig, recommendedConfig.getParentConfiguration());
        org.junit.Assert.assertSame(stackConfig, clusterConfig.getParentConfiguration());
        org.junit.Assert.assertEquals(com.google.common.collect.ImmutableMap.of("kafka-broker", com.google.common.collect.ImmutableMap.of("log.dirs", "/kafka-logs", "offsets.topic.replication.factor", "1"), "spark2-defaults", com.google.common.collect.ImmutableMap.of("spark.yarn.queue", "default")), recommendedConfig.getProperties());
        org.junit.Assert.assertEquals(com.google.common.collect.ImmutableMap.of("oozie-env", com.google.common.collect.ImmutableMap.of("oozie_heapsize", "1024m", "oozie_permsize", "256m")), userConfig.getProperties());
        org.junit.Assert.assertEquals(com.google.common.collect.ImmutableMap.of("kafka-broker", com.google.common.collect.ImmutableMap.of("maximum", com.google.common.collect.ImmutableMap.of("offsets.topic.replication.factor", "10"))), recommendedConfig.getAttributes());
    }

    @org.junit.Test
    public void recommendConfigurations_neverApply() {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Set<java.lang.String>>> newServices = com.google.common.collect.ImmutableMap.of("KAFKA", com.google.common.collect.ImmutableMap.of("KAFKA_BROKER", com.google.common.collect.ImmutableSet.of("c7401")), "SPARK2", com.google.common.collect.ImmutableMap.of("SPARK2_JOBHISTORYSERVER", com.google.common.collect.ImmutableSet.of("c7402"), "SPARK2_CLIENT", com.google.common.collect.ImmutableSet.of("c7403", "c7404")), "OOZIE", com.google.common.collect.ImmutableMap.of("OOZIE_SERVER", com.google.common.collect.ImmutableSet.of("c7401"), "OOZIE_CLIENT", com.google.common.collect.ImmutableSet.of("c7403", "c7404")));
        org.apache.ambari.server.topology.Configuration stackConfig = org.apache.ambari.server.topology.Configuration.newEmpty();
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(org.apache.ambari.server.testutils.TestCollectionUtils.map("oozie-env", org.apache.ambari.server.testutils.TestCollectionUtils.map("oozie_heapsize", "1024", "oozie_permsize", "256")), java.util.Collections.emptyMap());
        org.apache.ambari.server.topology.Configuration userConfig = org.apache.ambari.server.topology.Configuration.newEmpty();
        userConfig.setParentConfiguration(clusterConfig);
        clusterConfig.setParentConfiguration(stackConfig);
        org.apache.ambari.server.topology.addservice.LayoutRecommendationInfo layoutRecommendationInfo = new org.apache.ambari.server.topology.addservice.LayoutRecommendationInfo(new java.util.HashMap<>(), new java.util.HashMap<>());
        org.apache.ambari.server.topology.addservice.AddServiceRequest request = org.apache.ambari.server.topology.addservice.StackAdvisorAdapterTest.request(org.apache.ambari.server.topology.ConfigRecommendationStrategy.NEVER_APPLY);
        org.apache.ambari.server.topology.addservice.AddServiceInfo info = addServiceInfoBuilder.setRequest(request).setStack(stack).setConfig(userConfig).setNewServices(newServices).setRecommendationInfo(layoutRecommendationInfo).build();
        org.apache.ambari.server.topology.addservice.AddServiceInfo infoWithConfig = adapter.recommendConfigurations(info);
        org.junit.Assert.assertSame(userConfig, infoWithConfig.getConfig());
        org.junit.Assert.assertSame(clusterConfig, userConfig.getParentConfiguration());
        org.junit.Assert.assertNotNull(clusterConfig.getParentConfiguration());
        org.junit.Assert.assertEquals(com.google.common.collect.ImmutableMap.of("oozie-env", com.google.common.collect.ImmutableMap.of("oozie_heapsize", "1024m", "oozie_permsize", "256m")), userConfig.getProperties());
    }

    @org.junit.Test
    public void recommendConfigurations_onlyStackDefaultsApply() {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Set<java.lang.String>>> newServices = com.google.common.collect.ImmutableMap.of("KAFKA", com.google.common.collect.ImmutableMap.of("KAFKA_BROKER", com.google.common.collect.ImmutableSet.of("c7401")), "SPARK2", com.google.common.collect.ImmutableMap.of("SPARK2_JOBHISTORYSERVER", com.google.common.collect.ImmutableSet.of("c7402"), "SPARK2_CLIENT", com.google.common.collect.ImmutableSet.of("c7403", "c7404")), "OOZIE", com.google.common.collect.ImmutableMap.of("OOZIE_SERVER", com.google.common.collect.ImmutableSet.of("c7401"), "OOZIE_CLIENT", com.google.common.collect.ImmutableSet.of("c7403", "c7404")));
        org.apache.ambari.server.topology.Configuration stackConfig = new org.apache.ambari.server.topology.Configuration(com.google.common.collect.ImmutableMap.of("kafka-broker", com.google.common.collect.ImmutableMap.of("log.dirs", "/kafka-logs-stackdefault")), com.google.common.collect.ImmutableMap.of());
        org.apache.ambari.server.topology.Configuration clusterConfig = new org.apache.ambari.server.topology.Configuration(com.google.common.collect.ImmutableMap.of("oozie-env", com.google.common.collect.ImmutableMap.of("oozie_heapsize", "1024", "oozie_permsize", "256")), java.util.Collections.emptyMap());
        org.apache.ambari.server.topology.Configuration userConfig = org.apache.ambari.server.topology.Configuration.newEmpty();
        userConfig.setParentConfiguration(clusterConfig);
        clusterConfig.setParentConfiguration(stackConfig);
        org.apache.ambari.server.topology.addservice.LayoutRecommendationInfo layoutRecommendationInfo = new org.apache.ambari.server.topology.addservice.LayoutRecommendationInfo(new java.util.HashMap<>(), new java.util.HashMap<>());
        org.apache.ambari.server.topology.addservice.AddServiceRequest request = org.apache.ambari.server.topology.addservice.StackAdvisorAdapterTest.request(org.apache.ambari.server.topology.ConfigRecommendationStrategy.ONLY_STACK_DEFAULTS_APPLY);
        org.apache.ambari.server.topology.addservice.AddServiceInfo info = addServiceInfoBuilder.setRequest(request).setStack(stack).setConfig(userConfig).setNewServices(newServices).setRecommendationInfo(layoutRecommendationInfo).build();
        org.apache.ambari.server.topology.addservice.AddServiceInfo infoWithConfig = adapter.recommendConfigurations(info);
        org.apache.ambari.server.topology.Configuration recommendedConfig = infoWithConfig.getConfig().getParentConfiguration();
        org.junit.Assert.assertSame(userConfig, infoWithConfig.getConfig());
        org.junit.Assert.assertSame(clusterConfig, recommendedConfig.getParentConfiguration());
        org.junit.Assert.assertSame(stackConfig, clusterConfig.getParentConfiguration());
        org.junit.Assert.assertEquals(com.google.common.collect.ImmutableMap.of("kafka-broker", com.google.common.collect.ImmutableMap.of("log.dirs", "/kafka-logs")), recommendedConfig.getProperties());
        org.junit.Assert.assertEquals(com.google.common.collect.ImmutableMap.of("oozie-env", com.google.common.collect.ImmutableMap.of("oozie_heapsize", "1024m", "oozie_permsize", "256m")), userConfig.getProperties());
    }

    @org.junit.Test
    public void removeNonStackConfigRecommendations() {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> stackProperties = com.google.common.collect.ImmutableMap.of("kafka-broker", com.google.common.collect.ImmutableMap.of("log.dirs", "/kafka-logs", "offsets.topic.replication.factor", "1"), "spark2-defaults", com.google.common.collect.ImmutableMap.of("spark.yarn.queue", "default"));
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>> stackAttributes = com.google.common.collect.ImmutableMap.of("oozie-env", com.google.common.collect.ImmutableMap.of("miniumum", com.google.common.collect.ImmutableMap.of("oozie_heapsize", "1024", "oozie_permsize", "256")));
        org.apache.ambari.server.topology.Configuration stackConfig = new org.apache.ambari.server.topology.Configuration(stackProperties, stackAttributes);
        java.util.Map<java.lang.String, org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.BlueprintConfigurations> recommendedConfigs = org.apache.ambari.server.testutils.TestCollectionUtils.map("hdfs-site", org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.BlueprintConfigurations.create(org.apache.ambari.server.testutils.TestCollectionUtils.map("dfs.namenode.name.dir", "/hadoop/hdfs/namenode"), org.apache.ambari.server.testutils.TestCollectionUtils.map("visible", com.google.common.collect.ImmutableMap.of("dfs.namenode.name.dir", "false"))), "oozie-env", org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.BlueprintConfigurations.create(org.apache.ambari.server.testutils.TestCollectionUtils.map("oozie_heapsize", "2048"), new java.util.HashMap<>()), "spark2-defaults", org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.BlueprintConfigurations.create(org.apache.ambari.server.testutils.TestCollectionUtils.map("spark.yarn.queue", "spark2"), new java.util.HashMap<>()));
        java.util.Map<java.lang.String, org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.BlueprintConfigurations> recommendedConfigsForStackDefaults = com.google.common.collect.ImmutableMap.of("oozie-env", org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.BlueprintConfigurations.create(com.google.common.collect.ImmutableMap.of("oozie_heapsize", "2048"), com.google.common.collect.ImmutableMap.of()), "spark2-defaults", org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.BlueprintConfigurations.create(com.google.common.collect.ImmutableMap.of("spark.yarn.queue", "spark2"), com.google.common.collect.ImmutableMap.of()));
        org.apache.ambari.server.topology.addservice.StackAdvisorAdapter.removeNonStackConfigRecommendations(stackConfig, recommendedConfigs);
        org.junit.Assert.assertEquals(recommendedConfigsForStackDefaults, recommendedConfigs);
    }

    @org.junit.Test
    public void getLayoutRecommendationInfo() {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Set<java.lang.String>>> newServices = com.google.common.collect.ImmutableMap.of("KAFKA", com.google.common.collect.ImmutableMap.of("KAFKA_BROKER", com.google.common.collect.ImmutableSet.of("c7401")), "SPARK2", com.google.common.collect.ImmutableMap.of("SPARK2_JOBHISTORYSERVER", com.google.common.collect.ImmutableSet.of("c7402"), "SPARK2_CLIENT", com.google.common.collect.ImmutableSet.of("c7403", "c7404")), "OOZIE", com.google.common.collect.ImmutableMap.of("OOZIE_SERVER", com.google.common.collect.ImmutableSet.of("c7401"), "OOZIE_CLIENT", com.google.common.collect.ImmutableSet.of("c7403", "c7404")));
        org.apache.ambari.server.topology.addservice.AddServiceRequest request = org.apache.ambari.server.topology.addservice.StackAdvisorAdapterTest.request(org.apache.ambari.server.topology.ConfigRecommendationStrategy.ALWAYS_APPLY);
        org.apache.ambari.server.topology.addservice.AddServiceInfo info = addServiceInfoBuilder.setRequest(request).setStack(stack).setConfig(org.apache.ambari.server.topology.Configuration.newEmpty()).setNewServices(newServices).build();
        org.apache.ambari.server.topology.addservice.LayoutRecommendationInfo layoutRecommendationInfo = adapter.getLayoutRecommendationInfo(info);
        layoutRecommendationInfo.getAllServiceLayouts();
        org.junit.Assert.assertEquals(com.google.common.collect.ImmutableMap.of("host_group_1", com.google.common.collect.ImmutableSet.of("c7401"), "host_group_2", com.google.common.collect.ImmutableSet.of("c7402"), "host_group_3", com.google.common.collect.ImmutableSet.of("c7403", "c7404")), layoutRecommendationInfo.getHostGroups());
        org.junit.Assert.assertEquals(com.google.common.collect.ImmutableMap.<java.lang.String, java.util.Map<java.lang.String, java.util.Set<java.lang.String>>>builder().put("KAFKA", com.google.common.collect.ImmutableMap.of("KAFKA_BROKER", com.google.common.collect.ImmutableSet.of("c7401"))).put("SPARK2", com.google.common.collect.ImmutableMap.of("SPARK2_JOBHISTORYSERVER", com.google.common.collect.ImmutableSet.of("c7402"), "SPARK2_CLIENT", com.google.common.collect.ImmutableSet.of("c7403", "c7404"))).put("OOZIE", com.google.common.collect.ImmutableMap.of("OOZIE_SERVER", com.google.common.collect.ImmutableSet.of("c7401"), "OOZIE_CLIENT", com.google.common.collect.ImmutableSet.of("c7403", "c7404"))).put("HDFS", com.google.common.collect.ImmutableMap.of("NAMENODE", com.google.common.collect.ImmutableSet.of("c7401"), "HDFS_CLIENT", com.google.common.collect.ImmutableSet.of("c7401", "c7402"))).put("ZOOKEEPER", com.google.common.collect.ImmutableMap.of("ZOOKEEPER_SERVER", com.google.common.collect.ImmutableSet.of("c7401"), "ZOOKEEPER_CLIENT", com.google.common.collect.ImmutableSet.of("c7401", "c7402"))).put("MAPREDUCE2", com.google.common.collect.ImmutableMap.of("HISTORYSERVER", com.google.common.collect.ImmutableSet.of("c7401"))).build(), layoutRecommendationInfo.getAllServiceLayouts());
    }

    private static org.apache.ambari.server.topology.addservice.AddServiceRequest request(org.apache.ambari.server.topology.ConfigRecommendationStrategy strategy) {
        return new org.apache.ambari.server.topology.addservice.AddServiceRequest(null, strategy, null, null, null, null, null, null, null, null, null);
    }
}