package org.apache.ambari.server.controller.internal;
public class ConfigBasedJmxHostProvider implements org.apache.ambari.server.controller.jmx.JMXHostProvider {
    private final java.util.Map<java.lang.String, org.apache.ambari.server.state.UriInfo> overriddenJmxUris;

    private final org.apache.ambari.server.controller.jmx.JMXHostProvider defaultProvider;

    private final org.apache.ambari.server.state.ConfigHelper configHelper;

    public ConfigBasedJmxHostProvider(java.util.Map<java.lang.String, org.apache.ambari.server.state.UriInfo> overriddenJmxUris, org.apache.ambari.server.controller.jmx.JMXHostProvider defaultProvider, org.apache.ambari.server.state.ConfigHelper configHelper) {
        this.overriddenJmxUris = overriddenJmxUris;
        this.defaultProvider = defaultProvider;
        this.configHelper = configHelper;
    }

    @java.lang.Override
    public java.lang.String getPublicHostName(java.lang.String clusterName, java.lang.String hostName) {
        return defaultProvider.getPublicHostName(clusterName, hostName);
    }

    @java.lang.Override
    public java.util.Set<java.lang.String> getHostNames(java.lang.String clusterName, java.lang.String componentName) {
        return overridenJmxUri(componentName).map(uri -> java.util.Collections.singleton(resolve(uri, clusterName).getHost())).orElseGet(() -> defaultProvider.getHostNames(clusterName, componentName));
    }

    private java.net.URI resolve(org.apache.ambari.server.state.UriInfo uri, java.lang.String clusterName) {
        try {
            return uri.resolve(config(clusterName));
        } catch (org.apache.ambari.server.AmbariException e) {
            throw new java.lang.RuntimeException(e);
        }
    }

    @java.lang.Override
    public java.lang.String getPort(java.lang.String clusterName, java.lang.String componentName, java.lang.String hostName, boolean httpsEnabled) {
        return overridenJmxUri(componentName).map(uri -> java.lang.String.valueOf(resolve(uri, clusterName).getPort())).orElseGet(() -> defaultProvider.getPort(clusterName, componentName, hostName, httpsEnabled));
    }

    @java.lang.Override
    public java.lang.String getJMXProtocol(java.lang.String clusterName, java.lang.String componentName) {
        return overridenJmxUri(componentName).map(uri -> resolve(uri, clusterName).getScheme()).orElseGet(() -> defaultProvider.getJMXProtocol(clusterName, componentName));
    }

    @java.lang.Override
    public java.lang.String getJMXRpcMetricTag(java.lang.String clusterName, java.lang.String componentName, java.lang.String port) {
        return defaultProvider.getJMXRpcMetricTag(clusterName, componentName, port);
    }

    private java.util.Optional<org.apache.ambari.server.state.UriInfo> overridenJmxUri(java.lang.String component) {
        return java.util.Optional.ofNullable(overriddenJmxUris.get(component));
    }

    private java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> config(java.lang.String clusterName) throws org.apache.ambari.server.AmbariException {
        return configHelper.getEffectiveConfigProperties(clusterName, null);
    }
}