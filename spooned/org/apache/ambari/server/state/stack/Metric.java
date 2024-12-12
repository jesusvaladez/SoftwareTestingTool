package org.apache.ambari.server.state.stack;
public class Metric {
    private java.lang.String metric = null;

    private boolean pointInTime = false;

    private boolean temporal = false;

    private boolean amsHostMetric = false;

    private java.lang.String unit = "unitless";

    public Metric() {
    }

    public Metric(java.lang.String metric, boolean pointInTime, boolean temporal, boolean amsHostMetric, java.lang.String unit) {
        this.metric = metric;
        this.pointInTime = pointInTime;
        this.temporal = temporal;
        this.amsHostMetric = amsHostMetric;
        this.unit = unit;
    }

    public java.lang.String getName() {
        return metric;
    }

    public boolean isPointInTime() {
        return pointInTime;
    }

    public boolean isTemporal() {
        return temporal;
    }

    public boolean isAmsHostMetric() {
        return amsHostMetric;
    }

    public java.lang.String getUnit() {
        return unit;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return (((((((((((("Metric{" + "metric='") + metric) + '\'') + ", pointInTime=") + pointInTime) + ", temporal=") + temporal) + ", amsHostMetric=") + amsHostMetric) + ", unit='") + unit) + '\'') + '}';
    }
}