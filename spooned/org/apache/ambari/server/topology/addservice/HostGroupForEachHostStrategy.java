package org.apache.ambari.server.topology.addservice;
public class HostGroupForEachHostStrategy implements org.apache.ambari.server.topology.addservice.HostGroupStrategy {
    @java.lang.Override
    public java.util.Map<java.lang.String, java.util.Set<java.lang.String>> calculateHostGroups(java.util.Map<java.lang.String, java.util.Set<java.lang.String>> hostComponentMap) {
        return hostComponentMap.keySet().stream().collect(java.util.stream.Collectors.toMap(host -> "host_group_" + host, host -> com.google.common.collect.ImmutableSet.of(host)));
    }
}