package org.apache.ambari.server.controller.jmx;
public interface JMXHostProvider {
    java.lang.String getPublicHostName(java.lang.String clusterName, java.lang.String hostName);

    java.util.Set<java.lang.String> getHostNames(java.lang.String clusterName, java.lang.String componentName);

    java.lang.String getPort(java.lang.String clusterName, java.lang.String componentName, java.lang.String hostName, boolean httpsEnabled);

    java.lang.String getJMXProtocol(java.lang.String clusterName, java.lang.String componentName);

    java.lang.String getJMXRpcMetricTag(java.lang.String clusterName, java.lang.String componentName, java.lang.String port);
}