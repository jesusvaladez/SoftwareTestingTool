package org.apache.ambari.server.topology.addservice;
public class GroupByComponentsStrategy implements org.apache.ambari.server.topology.addservice.HostGroupStrategy {
    @java.lang.Override
    public java.util.Map<java.lang.String, java.util.Set<java.lang.String>> calculateHostGroups(java.util.Map<java.lang.String, java.util.Set<java.lang.String>> hostComponentMap) {
        java.util.List<java.util.Set<java.lang.String>> hostGroups = com.google.common.collect.Lists.newArrayList(hostComponentMap.entrySet().stream().collect(java.util.stream.Collectors.groupingBy(java.util.Map.Entry::getValue, java.util.stream.Collectors.mapping(java.util.Map.Entry::getKey, java.util.stream.Collectors.toCollection(java.util.TreeSet::new)))).values());
        hostGroups.sort(java.util.Comparator.comparing(hosts -> hosts.iterator().next()));
        java.util.Map<java.lang.String, java.util.Set<java.lang.String>> hostgroupMap = java.util.stream.IntStream.range(0, hostGroups.size()).boxed().collect(java.util.stream.Collectors.toMap(i -> "host_group_" + (i + 1), i -> hostGroups.get(i)));
        return hostgroupMap;
    }
}