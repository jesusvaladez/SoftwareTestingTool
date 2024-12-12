package org.apache.ambari.server.metrics.system;
public class SingleMetric {
    java.lang.String metricName;

    double value;

    long timestamp;

    public SingleMetric(java.lang.String metricName, double value, long timestamp) {
        this.metricName = metricName;
        this.value = value;
        this.timestamp = timestamp;
    }

    public java.lang.String getMetricName() {
        return metricName;
    }

    public double getValue() {
        return value;
    }

    public long getTimestamp() {
        return timestamp;
    }
}