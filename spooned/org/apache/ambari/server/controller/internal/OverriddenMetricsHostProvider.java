package org.apache.ambari.server.controller.internal;
public class OverriddenMetricsHostProvider implements org.apache.ambari.server.controller.metrics.MetricHostProvider {
    private final java.util.Map<java.lang.String, java.lang.String> overriddenHosts;

    private final org.apache.ambari.server.controller.metrics.MetricHostProvider metricHostProvider;

    private final org.apache.ambari.server.state.ConfigHelper configHelper;

    private final org.apache.ambari.server.state.kerberos.VariableReplacementHelper variableReplacer = new org.apache.ambari.server.state.kerberos.VariableReplacementHelper();

    public OverriddenMetricsHostProvider(java.util.Map<java.lang.String, java.lang.String> overriddenHosts, org.apache.ambari.server.controller.metrics.MetricHostProvider metricHostProvider, org.apache.ambari.server.state.ConfigHelper configHelper) {
        this.overriddenHosts = overriddenHosts;
        this.metricHostProvider = metricHostProvider;
        this.configHelper = configHelper;
    }

    @java.lang.Override
    public java.util.Optional<java.lang.String> getExternalHostName(java.lang.String clusterName, java.lang.String componentName) {
        return getOverriddenHost(componentName).map(host -> replaceVariables(clusterName, host));
    }

    @java.lang.Override
    public boolean isCollectorHostExternal(java.lang.String clusterName) {
        return metricHostProvider.isCollectorHostExternal(clusterName);
    }

    private java.util.Optional<java.lang.String> getOverriddenHost(java.lang.String componentName) {
        return java.util.Optional.ofNullable(overriddenHosts.get(componentName));
    }

    private java.lang.String replaceVariables(java.lang.String clusterName, java.lang.String hostName) {
        try {
            return hostName(variableReplacer.replaceVariables(hostName, config(clusterName)));
        } catch (org.apache.ambari.server.AmbariException e) {
            throw new java.lang.RuntimeException(e);
        }
    }

    private java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> config(java.lang.String clusterName) throws org.apache.ambari.server.AmbariException {
        return configHelper.getEffectiveConfigProperties(clusterName, null);
    }

    private java.lang.String hostName(java.lang.String resolvedHost) throws org.apache.ambari.server.AmbariException {
        return hasScheme(resolvedHost) ? java.net.URI.create(resolvedHost).getHost() : java.net.URI.create("any://" + resolvedHost).getHost();
    }

    private boolean hasScheme(java.lang.String host) {
        return host.contains("://");
    }

    @java.lang.Override
    public java.lang.String getHostName(java.lang.String clusterName, java.lang.String componentName) throws org.apache.ambari.server.controller.spi.SystemException {
        return metricHostProvider.getHostName(clusterName, componentName);
    }

    @java.lang.Override
    public java.lang.String getCollectorHostName(java.lang.String clusterName, org.apache.ambari.server.controller.metrics.MetricsServiceProvider.MetricsService service) throws org.apache.ambari.server.controller.spi.SystemException {
        return metricHostProvider.getCollectorHostName(clusterName, service);
    }

    @java.lang.Override
    public java.lang.String getCollectorPort(java.lang.String clusterName, org.apache.ambari.server.controller.metrics.MetricsServiceProvider.MetricsService service) throws org.apache.ambari.server.controller.spi.SystemException {
        return metricHostProvider.getCollectorPort(clusterName, service);
    }

    @java.lang.Override
    public boolean isCollectorHostLive(java.lang.String clusterName, org.apache.ambari.server.controller.metrics.MetricsServiceProvider.MetricsService service) throws org.apache.ambari.server.controller.spi.SystemException {
        return metricHostProvider.isCollectorHostLive(clusterName, service);
    }

    @java.lang.Override
    public boolean isCollectorComponentLive(java.lang.String clusterName, org.apache.ambari.server.controller.metrics.MetricsServiceProvider.MetricsService service) throws org.apache.ambari.server.controller.spi.SystemException {
        return metricHostProvider.isCollectorComponentLive(clusterName, service);
    }
}