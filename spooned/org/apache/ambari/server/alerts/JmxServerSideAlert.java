package org.apache.ambari.server.alerts;
public class JmxServerSideAlert extends org.apache.ambari.server.alerts.AlertRunnable {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.alerts.JmxServerSideAlert.class);

    @com.google.inject.Inject
    private org.apache.ambari.server.state.alert.AlertDefinitionFactory definitionFactory;

    @com.google.inject.Inject
    private org.apache.ambari.server.state.services.MetricsRetrievalService metricsRetrievalService;

    @com.google.inject.Inject
    private org.apache.ambari.server.state.ConfigHelper configHelper;

    public JmxServerSideAlert(java.lang.String definitionName) {
        super(definitionName);
    }

    @java.lang.Override
    java.util.List<org.apache.ambari.server.state.Alert> execute(org.apache.ambari.server.state.Cluster cluster, org.apache.ambari.server.orm.entities.AlertDefinitionEntity entity) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.alert.AlertDefinition alertDef = definitionFactory.coerce(entity);
        org.apache.ambari.server.state.alert.ServerSource serverSource = ((org.apache.ambari.server.state.alert.ServerSource) (alertDef.getSource()));
        return buildAlert(jmxMetric(serverSource, cluster), serverSource.getJmxInfo(), alertDef).map(alert -> java.util.Collections.singletonList(alert)).orElse(java.util.Collections.emptyList());
    }

    public java.util.Optional<org.apache.ambari.server.state.Alert> buildAlert(java.util.Optional<org.apache.ambari.server.controller.jmx.JMXMetricHolder> metricHolder, org.apache.ambari.server.state.alert.MetricSource.JmxInfo jmxInfo, org.apache.ambari.server.state.alert.AlertDefinition alertDef) {
        return metricHolder.flatMap(metric -> buildAlert(metric, jmxInfo, alertDef));
    }

    private java.util.Optional<org.apache.ambari.server.state.Alert> buildAlert(org.apache.ambari.server.controller.jmx.JMXMetricHolder metricHolder, org.apache.ambari.server.state.alert.MetricSource.JmxInfo jmxInfo, org.apache.ambari.server.state.alert.AlertDefinition alertDef) {
        java.util.List<java.lang.Object> allMetrics = metricHolder.findAll(jmxInfo.getPropertyList());
        return jmxInfo.eval(metricHolder).map(val -> alertDef.buildAlert(val.doubleValue(), allMetrics));
    }

    private java.util.Optional<org.apache.ambari.server.controller.jmx.JMXMetricHolder> jmxMetric(org.apache.ambari.server.state.alert.ServerSource serverSource, org.apache.ambari.server.state.Cluster cluster) throws org.apache.ambari.server.AmbariException {
        java.net.URI jmxUri = jmxUrl(cluster, serverSource);
        org.apache.ambari.server.controller.internal.URLStreamProvider streamProvider = new org.apache.ambari.server.controller.internal.URLStreamProvider(serverSource.getUri().getConnectionTimeoutMsec(), serverSource.getUri().getReadTimeoutMsec(), org.apache.ambari.server.configuration.ComponentSSLConfiguration.instance());
        metricsRetrievalService.submitRequest(org.apache.ambari.server.state.services.MetricsRetrievalService.MetricSourceType.JMX, streamProvider, jmxUri.toString());
        return java.util.Optional.ofNullable(metricsRetrievalService.getCachedJMXMetric(jmxUri.toString()));
    }

    private java.net.URI jmxUrl(org.apache.ambari.server.state.Cluster cluster, org.apache.ambari.server.state.alert.ServerSource serverSource) throws org.apache.ambari.server.AmbariException {
        return serverSource.getUri().resolve(config(cluster)).resolve(serverSource.getJmxInfo().getUrlSuffix());
    }

    private java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> config(org.apache.ambari.server.state.Cluster cluster) throws org.apache.ambari.server.AmbariException {
        return configHelper.getEffectiveConfigProperties(cluster, configHelper.getEffectiveDesiredTags(cluster, null));
    }
}