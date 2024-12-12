package org.apache.ambari.server.topology.addservice;
public class HostGroupStrategyTest {
    public static final java.util.Map<java.lang.String, java.util.Set<java.lang.String>> HOST_COMPONENTS = com.google.common.collect.ImmutableMap.<java.lang.String, java.util.Set<java.lang.String>>builder().put("c7401", com.google.common.collect.ImmutableSet.of("ZOOKEEPER_SERVER, NAMENODE, HDFS_CLIENT")).put("c7402", com.google.common.collect.ImmutableSet.of("ZOOKEEPER_SERVER, NAMENODE, HDFS_CLIENT")).put("c7403", com.google.common.collect.ImmutableSet.of("ZOOKEEPER_SERVER, NAMENODE, HDFS_CLIENT")).put("c7404", com.google.common.collect.ImmutableSet.of("ZOOKEEPER_SERVER, NAMENODE, HDFS_CLIENT, SECONDARY_NAMENODE")).put("c7405", com.google.common.collect.ImmutableSet.of("HIVE_SERVER, KAFKA_BROKER, ZOOKEEPER_CLIENT")).put("c7406", com.google.common.collect.ImmutableSet.of("DATANODE, HDFS_CLIENT, ZOOKEEPER_CLIENT")).put("c7407", com.google.common.collect.ImmutableSet.of("DATANODE, HDFS_CLIENT, ZOOKEEPER_CLIENT")).put("c7408", com.google.common.collect.ImmutableSet.of("DATANODE, HDFS_CLIENT, ZOOKEEPER_CLIENT")).build();

    java.util.Map<java.lang.String, java.util.Set<java.lang.String>> HOST_GROUPS_FOR_EACH_HOST = com.google.common.collect.ImmutableMap.<java.lang.String, java.util.Set<java.lang.String>>builder().put("host_group_c7401", com.google.common.collect.ImmutableSet.of("c7401")).put("host_group_c7402", com.google.common.collect.ImmutableSet.of("c7402")).put("host_group_c7403", com.google.common.collect.ImmutableSet.of("c7403")).put("host_group_c7404", com.google.common.collect.ImmutableSet.of("c7404")).put("host_group_c7405", com.google.common.collect.ImmutableSet.of("c7405")).put("host_group_c7406", com.google.common.collect.ImmutableSet.of("c7406")).put("host_group_c7407", com.google.common.collect.ImmutableSet.of("c7407")).put("host_group_c7408", com.google.common.collect.ImmutableSet.of("c7408")).build();

    java.util.Map<java.lang.String, java.util.Set<java.lang.String>> HOST_GROUPS_BY_COMPONENTS = com.google.common.collect.ImmutableMap.of("host_group_1", com.google.common.collect.ImmutableSet.of("c7401", "c7402", "c7403"), "host_group_2", com.google.common.collect.ImmutableSet.of("c7404"), "host_group_3", com.google.common.collect.ImmutableSet.of("c7405"), "host_group_4", com.google.common.collect.ImmutableSet.of("c7406", "c7407", "c7408"));

    @org.junit.Test
    public void hostGroupForEachHostStrategy() {
        org.junit.Assert.assertEquals(HOST_GROUPS_FOR_EACH_HOST, new org.apache.ambari.server.topology.addservice.HostGroupForEachHostStrategy().calculateHostGroups(org.apache.ambari.server.topology.addservice.HostGroupStrategyTest.HOST_COMPONENTS));
    }

    @org.junit.Test
    public void groupByComponentsStrategy() {
        org.junit.Assert.assertEquals(HOST_GROUPS_BY_COMPONENTS, new org.apache.ambari.server.topology.addservice.GroupByComponentsStrategy().calculateHostGroups(org.apache.ambari.server.topology.addservice.HostGroupStrategyTest.HOST_COMPONENTS));
    }

    @org.junit.Test
    public void autoHostgroupStrategy() {
        org.junit.Assert.assertEquals(HOST_GROUPS_FOR_EACH_HOST, new org.apache.ambari.server.topology.addservice.AutoHostgroupStrategy().calculateHostGroups(org.apache.ambari.server.topology.addservice.HostGroupStrategyTest.HOST_COMPONENTS));
        org.junit.Assert.assertEquals(HOST_GROUPS_BY_COMPONENTS, new org.apache.ambari.server.topology.addservice.AutoHostgroupStrategy(7).calculateHostGroups(org.apache.ambari.server.topology.addservice.HostGroupStrategyTest.HOST_COMPONENTS));
    }
}