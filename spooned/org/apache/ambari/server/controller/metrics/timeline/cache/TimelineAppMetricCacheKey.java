package org.apache.ambari.server.controller.metrics.timeline.cache;
public class TimelineAppMetricCacheKey {
    private final java.util.Set<java.lang.String> metricNames;

    private final java.lang.String appId;

    private final java.lang.String hostNames;

    private java.lang.String spec;

    private org.apache.ambari.server.controller.spi.TemporalInfo temporalInfo;

    public TimelineAppMetricCacheKey(java.util.Set<java.lang.String> metricNames, java.lang.String appId, java.lang.String hostNames, org.apache.ambari.server.controller.spi.TemporalInfo temporalInfo) {
        this.metricNames = metricNames;
        this.appId = appId;
        this.hostNames = hostNames;
        this.temporalInfo = temporalInfo;
    }

    public TimelineAppMetricCacheKey(java.util.Set<java.lang.String> metricNames, java.lang.String appId, org.apache.ambari.server.controller.spi.TemporalInfo temporalInfo) {
        this(metricNames, appId, null, temporalInfo);
    }

    public java.util.Set<java.lang.String> getMetricNames() {
        return metricNames;
    }

    public org.apache.ambari.server.controller.spi.TemporalInfo getTemporalInfo() {
        return temporalInfo;
    }

    public void setTemporalInfo(org.apache.ambari.server.controller.spi.TemporalInfo temporalInfo) {
        this.temporalInfo = temporalInfo;
    }

    public java.lang.String getAppId() {
        return appId;
    }

    public java.lang.String getHostNames() {
        return hostNames;
    }

    public java.lang.String getSpec() {
        return spec;
    }

    public void setSpec(java.lang.String spec) {
        this.spec = spec;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o)
            return true;

        if ((o == null) || (getClass() != o.getClass()))
            return false;

        org.apache.ambari.server.controller.metrics.timeline.cache.TimelineAppMetricCacheKey that = ((org.apache.ambari.server.controller.metrics.timeline.cache.TimelineAppMetricCacheKey) (o));
        if (!metricNames.equals(that.metricNames))
            return false;

        if (!appId.equals(that.appId))
            return false;

        return !(hostNames != null ? !hostNames.equals(that.hostNames) : that.hostNames != null);
    }

    @java.lang.Override
    public int hashCode() {
        int result = metricNames.hashCode();
        result = (31 * result) + appId.hashCode();
        result = (31 * result) + (hostNames != null ? hostNames.hashCode() : 0);
        return result;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return (((((((((("TimelineAppMetricCacheKey{" + "metricNames=") + metricNames) + ", appId='") + appId) + '\'') + ", hostNames=") + hostNames) + ", spec='") + spec) + '\'') + '}';
    }
}