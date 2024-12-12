package org.apache.ambari.view.cluster;
public interface Cluster {
    public java.lang.String getName();

    public java.lang.String getConfigurationValue(java.lang.String type, java.lang.String key);

    public java.util.Map<java.lang.String, java.lang.String> getConfigByType(java.lang.String type);

    public java.util.List<java.lang.String> getHostsForServiceComponent(java.lang.String serviceName, java.lang.String componentName);
}