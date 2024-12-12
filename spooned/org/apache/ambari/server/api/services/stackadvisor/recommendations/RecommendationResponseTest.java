package org.apache.ambari.server.api.services.stackadvisor.recommendations;
public class RecommendationResponseTest {
    private final org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse response = new org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse();

    @org.junit.Before
    public void setUp() {
        org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.Blueprint blueprint = new org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.Blueprint();
        blueprint.setHostGroups(com.google.common.collect.ImmutableSet.of(org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponseTest.hostGroup("host_group_1", "NAMENODE", "ZOOKEEPER_SERVER"), org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponseTest.hostGroup("host_group_2", "DATANODE", "HDFS_CLIENT", "ZOOKEEPER_CLIENT")));
        org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.BlueprintClusterBinding clusterBinding = new org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.BlueprintClusterBinding();
        clusterBinding.setHostGroups(com.google.common.collect.ImmutableSet.of(org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponseTest.hostGroupBinding("host_group_1", "c7401", "c7402"), org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponseTest.hostGroupBinding("host_group_2", "c7403", "c7404", "c7405")));
        org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.Recommendation recommendation = new org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.Recommendation();
        recommendation.setBlueprint(blueprint);
        recommendation.setBlueprintClusterBinding(clusterBinding);
        response.setRecommendations(recommendation);
    }

    @org.junit.Test
    public void blueprint_getHostgroupComponentMap() {
        com.google.common.collect.ImmutableMap<java.lang.String, java.util.Set<java.lang.String>> expected = com.google.common.collect.ImmutableMap.of("host_group_1", com.google.common.collect.ImmutableSet.of("NAMENODE", "ZOOKEEPER_SERVER"), "host_group_2", com.google.common.collect.ImmutableSet.of("DATANODE", "HDFS_CLIENT", "ZOOKEEPER_CLIENT"));
        org.junit.Assert.assertEquals(expected, response.getRecommendations().getBlueprint().getHostgroupComponentMap());
    }

    @org.junit.Test
    public void hostgGroup_getComponentNames() {
        java.util.Map<java.lang.String, org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.HostGroup> hostGroups = response.getRecommendations().getBlueprint().getHostGroups().stream().collect(java.util.stream.Collectors.toMap(org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.HostGroup::getName, java.util.function.Function.identity()));
        org.junit.Assert.assertEquals(com.google.common.collect.ImmutableSet.of("NAMENODE", "ZOOKEEPER_SERVER"), hostGroups.get("host_group_1").getComponentNames());
        org.junit.Assert.assertEquals(com.google.common.collect.ImmutableSet.of("DATANODE", "HDFS_CLIENT", "ZOOKEEPER_CLIENT"), hostGroups.get("host_group_2").getComponentNames());
    }

    @org.junit.Test
    public void blueprintClusterBinding_getHostgroupHostMap() {
        com.google.common.collect.ImmutableMap<java.lang.String, java.util.Set<java.lang.String>> expected = com.google.common.collect.ImmutableMap.of("host_group_1", com.google.common.collect.ImmutableSet.of("c7401", "c7402"), "host_group_2", com.google.common.collect.ImmutableSet.of("c7403", "c7404", "c7405"));
        org.junit.Assert.assertEquals(expected, response.getRecommendations().getBlueprintClusterBinding().getHostgroupHostMap());
    }

    private static final org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.HostGroup hostGroup(java.lang.String name, java.lang.String... components) {
        org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.HostGroup hostGroup = new org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.HostGroup();
        hostGroup.setName(name);
        java.util.Set<java.util.Map<java.lang.String, java.lang.String>> hostGroupComponents = java.util.Arrays.stream(components).map(comp -> com.google.common.collect.ImmutableMap.of("name", comp)).collect(java.util.stream.Collectors.toSet());
        hostGroup.setComponents(hostGroupComponents);
        return hostGroup;
    }

    private static final org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.BindingHostGroup hostGroupBinding(java.lang.String name, java.lang.String... hosts) {
        org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.BindingHostGroup hostGroup = new org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.BindingHostGroup();
        hostGroup.setName(name);
        java.util.Set<java.util.Map<java.lang.String, java.lang.String>> hostGroupHosts = java.util.Arrays.stream(hosts).map(host -> com.google.common.collect.ImmutableMap.of("fqdn", host)).collect(java.util.stream.Collectors.toSet());
        hostGroup.setHosts(hostGroupHosts);
        return hostGroup;
    }
}