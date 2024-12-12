package org.apache.ambari.server.controller.metrics.timeline.cache;
import org.ehcache.ValueSupplier;
import org.ehcache.expiry.Duration;
import org.ehcache.expiry.Expiry;
public class TimelineMetricCacheCustomExpiry implements org.ehcache.expiry.Expiry<org.apache.ambari.server.controller.metrics.timeline.cache.TimelineAppMetricCacheKey, org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricsCacheValue> {
    private final org.ehcache.expiry.Duration timeToLive;

    private final org.ehcache.expiry.Duration timeToIdle;

    public TimelineMetricCacheCustomExpiry(java.time.Duration timeToLive, java.time.Duration timeToIdle) {
        this.timeToLive = convertJavaDurationToEhcacheDuration(timeToLive);
        this.timeToIdle = convertJavaDurationToEhcacheDuration(timeToIdle);
    }

    @java.lang.Override
    public org.ehcache.expiry.Duration getExpiryForCreation(org.apache.ambari.server.controller.metrics.timeline.cache.TimelineAppMetricCacheKey key, org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricsCacheValue value) {
        return timeToLive;
    }

    @java.lang.Override
    public org.ehcache.expiry.Duration getExpiryForAccess(org.apache.ambari.server.controller.metrics.timeline.cache.TimelineAppMetricCacheKey key, org.ehcache.ValueSupplier<? extends org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricsCacheValue> value) {
        return timeToIdle;
    }

    @java.lang.Override
    public org.ehcache.expiry.Duration getExpiryForUpdate(org.apache.ambari.server.controller.metrics.timeline.cache.TimelineAppMetricCacheKey key, org.ehcache.ValueSupplier<? extends org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricsCacheValue> oldValue, org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricsCacheValue newValue) {
        return timeToLive;
    }

    private org.ehcache.expiry.Duration convertJavaDurationToEhcacheDuration(java.time.Duration javaDuration) {
        return org.ehcache.expiry.Duration.of(javaDuration.toNanos(), java.util.concurrent.TimeUnit.NANOSECONDS);
    }
}