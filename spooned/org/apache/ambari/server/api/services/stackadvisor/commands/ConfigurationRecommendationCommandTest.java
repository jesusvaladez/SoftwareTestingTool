package org.apache.ambari.server.api.services.stackadvisor.commands;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
public class ConfigurationRecommendationCommandTest {
    @org.junit.Test
    public void testProcessHostGroups() throws java.lang.Exception {
        org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRunner saRunner = Mockito.mock(org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRunner.class);
        java.io.File file = Mockito.mock(java.io.File.class);
        org.apache.ambari.server.api.services.AmbariMetaInfo metaInfo = Mockito.mock(org.apache.ambari.server.api.services.AmbariMetaInfo.class);
        org.apache.ambari.server.api.services.stackadvisor.commands.ConfigurationRecommendationCommand command = new org.apache.ambari.server.api.services.stackadvisor.commands.ConfigurationRecommendationCommand(org.apache.ambari.server.api.services.stackadvisor.commands.StackAdvisorCommandType.RECOMMEND_CONFIGURATIONS, file, "1w", org.apache.ambari.server.state.ServiceInfo.ServiceAdvisorType.PYTHON, 1, saRunner, metaInfo, null, null);
        org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequest request = Mockito.mock(org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequest.class);
        java.util.SortedMap<java.lang.String, java.util.SortedSet<java.lang.String>> componentHostGroupMap = new java.util.TreeMap<>();
        java.util.SortedSet<java.lang.String> components1 = new java.util.TreeSet<>();
        components1.add("component1");
        components1.add("component4");
        components1.add("component5");
        componentHostGroupMap.put("group1", components1);
        java.util.SortedSet<java.lang.String> components2 = new java.util.TreeSet<>();
        components2.add("component2");
        components2.add("component3");
        componentHostGroupMap.put("group2", components2);
        Mockito.doReturn(componentHostGroupMap).when(request).getHostComponents();
        java.util.Set<org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.HostGroup> hostGroups = command.processHostGroups(request);
        org.junit.Assert.assertNotNull(hostGroups);
        org.junit.Assert.assertEquals(2, hostGroups.size());
        java.util.Map<java.lang.String, org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.HostGroup> hostGroupMap = new java.util.HashMap<>();
        for (org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.HostGroup hostGroup : hostGroups) {
            hostGroupMap.put(hostGroup.getName(), hostGroup);
        }
        org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.HostGroup hostGroup1 = hostGroupMap.get("group1");
        org.junit.Assert.assertNotNull(hostGroup1);
        java.util.Set<java.util.Map<java.lang.String, java.lang.String>> host1Components = hostGroup1.getComponents();
        org.junit.Assert.assertNotNull(host1Components);
        org.junit.Assert.assertEquals(3, host1Components.size());
        java.util.Set<java.lang.String> componentNames1 = new java.util.HashSet<>();
        for (java.util.Map<java.lang.String, java.lang.String> host1Component : host1Components) {
            org.junit.Assert.assertNotNull(host1Component);
            org.junit.Assert.assertEquals(1, host1Component.size());
            java.lang.String componentName = host1Component.get("name");
            org.junit.Assert.assertNotNull(componentName);
            componentNames1.add(componentName);
        }
        org.junit.Assert.assertEquals(3, componentNames1.size());
        org.junit.Assert.assertTrue(componentNames1.contains("component1"));
        org.junit.Assert.assertTrue(componentNames1.contains("component4"));
        org.junit.Assert.assertTrue(componentNames1.contains("component5"));
        org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.HostGroup hostGroup2 = hostGroupMap.get("group2");
        org.junit.Assert.assertNotNull(hostGroup2);
        java.util.Set<java.util.Map<java.lang.String, java.lang.String>> host2Components = hostGroup2.getComponents();
        org.junit.Assert.assertNotNull(host2Components);
        org.junit.Assert.assertEquals(2, host2Components.size());
        java.util.Set<java.lang.String> componentNames2 = new java.util.HashSet<>();
        for (java.util.Map<java.lang.String, java.lang.String> host2Component : host2Components) {
            org.junit.Assert.assertNotNull(host2Component);
            org.junit.Assert.assertEquals(1, host2Component.size());
            java.lang.String componentName = host2Component.get("name");
            org.junit.Assert.assertNotNull(componentName);
            componentNames2.add(componentName);
        }
        org.junit.Assert.assertEquals(2, componentNames2.size());
        org.junit.Assert.assertTrue(componentNames2.contains("component2"));
        org.junit.Assert.assertTrue(componentNames2.contains("component3"));
    }
}