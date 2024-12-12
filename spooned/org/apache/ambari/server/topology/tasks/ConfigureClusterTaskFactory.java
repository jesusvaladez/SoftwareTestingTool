package org.apache.ambari.server.topology.tasks;
public interface ConfigureClusterTaskFactory {
    org.apache.ambari.server.topology.tasks.ConfigureClusterTask createConfigureClusterTask(org.apache.ambari.server.topology.ClusterTopology topology, org.apache.ambari.server.topology.ClusterConfigurationRequest configRequest, org.apache.ambari.server.events.publishers.AmbariEventPublisher ambariEventPublisher);
}