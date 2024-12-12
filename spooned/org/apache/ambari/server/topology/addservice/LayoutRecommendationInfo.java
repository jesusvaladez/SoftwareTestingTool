package org.apache.ambari.server.topology.addservice;
public class LayoutRecommendationInfo {
    private final java.util.Map<java.lang.String, java.util.Set<java.lang.String>> hostGroups;

    private final java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Set<java.lang.String>>> allServiceComponentHosts;

    public LayoutRecommendationInfo(java.util.Map<java.lang.String, java.util.Set<java.lang.String>> hostGroups, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Set<java.lang.String>>> allServiceComponentHosts) {
        this.hostGroups = hostGroups;
        this.allServiceComponentHosts = allServiceComponentHosts;
    }

    public java.util.Map<java.lang.String, java.util.Set<java.lang.String>> getHostGroups() {
        return hostGroups;
    }

    public java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Set<java.lang.String>>> getAllServiceLayouts() {
        return allServiceComponentHosts;
    }

    public java.util.List<java.lang.String> getHosts() {
        return org.apache.ambari.server.topology.addservice.LayoutRecommendationInfo.getHostsFromHostGroups(hostGroups);
    }

    public static java.util.List<java.lang.String> getHostsFromHostGroups(java.util.Map<java.lang.String, java.util.Set<java.lang.String>> hostGroups) {
        return hostGroups.values().stream().flatMap(java.util.Set::stream).collect(java.util.stream.Collectors.toList());
    }
}