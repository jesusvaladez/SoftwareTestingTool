package org.apache.ambari.server.topology.addservice;
public class AutoHostgroupStrategy implements org.apache.ambari.server.topology.addservice.HostGroupStrategy {
    private final int largeClusterThreshold;

    public AutoHostgroupStrategy() {
        this(10);
    }

    public AutoHostgroupStrategy(int largeClusterThreshold) {
        this.largeClusterThreshold = largeClusterThreshold;
    }

    @java.lang.Override
    public java.util.Map<java.lang.String, java.util.Set<java.lang.String>> calculateHostGroups(java.util.Map<java.lang.String, java.util.Set<java.lang.String>> hostComponentMap) {
        org.apache.ambari.server.topology.addservice.HostGroupStrategy strategy = (hostComponentMap.size() <= largeClusterThreshold) ? new org.apache.ambari.server.topology.addservice.HostGroupForEachHostStrategy() : new org.apache.ambari.server.topology.addservice.GroupByComponentsStrategy();
        return strategy.calculateHostGroups(hostComponentMap);
    }

    public int getLargeClusterThreshold() {
        return largeClusterThreshold;
    }
}