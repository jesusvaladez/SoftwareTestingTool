package org.apache.ambari.server.state.stack;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
public class MetricDefinition {
    private static final java.lang.String OVERRIDDEN_HOST_PROP = "overridden_host";

    private java.lang.String type = null;

    private java.util.Map<java.lang.String, java.lang.String> properties = null;

    private java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.state.stack.Metric>> metrics = null;

    @com.google.gson.annotations.SerializedName("jmx_source_uri")
    private org.apache.ambari.server.state.UriInfo jmxSourceUri;

    public MetricDefinition(java.lang.String type, java.util.Map<java.lang.String, java.lang.String> properties, java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.state.stack.Metric>> metrics) {
        this.type = type;
        this.properties = properties;
        this.metrics = metrics;
    }

    public java.lang.String getType() {
        return type;
    }

    public java.util.Map<java.lang.String, java.lang.String> getProperties() {
        return properties;
    }

    @org.codehaus.jackson.annotate.JsonProperty("metrics")
    public java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.state.stack.Metric>> getMetricsByCategory() {
        return metrics;
    }

    @org.codehaus.jackson.annotate.JsonIgnore
    public java.util.Map<java.lang.String, org.apache.ambari.server.state.stack.Metric> getMetrics() {
        java.util.Map<java.lang.String, org.apache.ambari.server.state.stack.Metric> metricMap = new java.util.HashMap<>();
        for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.state.stack.Metric>> metricMapEntry : metrics.entrySet()) {
            metricMap.putAll(metricMapEntry.getValue());
        }
        return metricMap;
    }

    @java.lang.Override
    public java.lang.String toString() {
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        sb.append("{type=").append(type);
        sb.append(";properties=").append(properties);
        sb.append(";metric_count=").append(getMetrics().size());
        sb.append('}');
        return sb.toString();
    }

    public java.util.Optional<java.lang.String> getOverriddenHosts() {
        return properties == null ? java.util.Optional.empty() : java.util.Optional.ofNullable(properties.get(org.apache.ambari.server.state.stack.MetricDefinition.OVERRIDDEN_HOST_PROP));
    }

    public java.util.Optional<org.apache.ambari.server.state.UriInfo> getJmxSourceUri() {
        return !"jmx".equalsIgnoreCase(type) ? java.util.Optional.empty() : java.util.Optional.ofNullable(jmxSourceUri);
    }
}