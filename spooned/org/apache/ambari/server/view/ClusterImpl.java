package org.apache.ambari.server.view;
public class ClusterImpl implements org.apache.ambari.view.cluster.Cluster {
    private final org.apache.ambari.server.state.Cluster cluster;

    public ClusterImpl(org.apache.ambari.server.state.Cluster cluster) {
        this.cluster = cluster;
    }

    @java.lang.Override
    public java.lang.String getName() {
        return cluster.getClusterName();
    }

    @java.lang.Override
    public java.lang.String getConfigurationValue(java.lang.String type, java.lang.String key) {
        org.apache.ambari.server.state.Config config = cluster.getDesiredConfigByType(type);
        return config == null ? null : config.getProperties().get(key);
    }

    @java.lang.Override
    public java.util.Map<java.lang.String, java.lang.String> getConfigByType(java.lang.String type) {
        org.apache.ambari.server.state.Config configs = cluster.getDesiredConfigByType(type);
        return com.google.common.collect.ImmutableMap.copyOf(configs.getProperties());
    }

    @java.lang.Override
    public java.util.List<java.lang.String> getHostsForServiceComponent(java.lang.String serviceName, java.lang.String componentName) {
        java.util.List<org.apache.ambari.server.state.ServiceComponentHost> serviceComponentHosts = cluster.getServiceComponentHosts(serviceName, componentName);
        java.util.List<java.lang.String> hosts = new java.util.ArrayList<>();
        for (org.apache.ambari.server.state.ServiceComponentHost serviceComponentHost : serviceComponentHosts) {
            hosts.add(serviceComponentHost.getHostName());
        }
        return hosts;
    }
}