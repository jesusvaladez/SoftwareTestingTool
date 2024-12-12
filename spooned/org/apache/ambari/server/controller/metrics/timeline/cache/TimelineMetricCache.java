package org.apache.ambari.server.controller.metrics.timeline.cache;
import org.apache.hadoop.metrics2.sink.timeline.TimelineMetrics;
import org.ehcache.Cache;
import org.ehcache.core.internal.statistics.DefaultStatisticsService;
import org.ehcache.core.statistics.CacheStatistics;
import org.ehcache.spi.loaderwriter.CacheLoadingException;
public class TimelineMetricCache {
    private final org.ehcache.Cache<org.apache.ambari.server.controller.metrics.timeline.cache.TimelineAppMetricCacheKey, org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricsCacheValue> cache;

    private final org.ehcache.core.internal.statistics.DefaultStatisticsService statisticsService;

    private final org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCacheEntryFactory cacheEntryFactory;

    public static final java.lang.String TIMELINE_METRIC_CACHE_INSTANCE_NAME = "timelineMetricCache";

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCache.class);

    private static java.util.concurrent.atomic.AtomicInteger printCacheStatsCounter = new java.util.concurrent.atomic.AtomicInteger(0);

    public TimelineMetricCache(org.ehcache.Cache<org.apache.ambari.server.controller.metrics.timeline.cache.TimelineAppMetricCacheKey, org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricsCacheValue> cache, org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCacheEntryFactory cacheEntryFactory, org.ehcache.core.internal.statistics.DefaultStatisticsService statisticsService) {
        this.cache = cache;
        this.cacheEntryFactory = cacheEntryFactory;
        this.statisticsService = statisticsService;
    }

    public org.apache.hadoop.metrics2.sink.timeline.TimelineMetrics getAppTimelineMetricsFromCache(org.apache.ambari.server.controller.metrics.timeline.cache.TimelineAppMetricCacheKey key) throws java.lang.IllegalArgumentException, java.io.IOException {
        if (org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCache.LOG.isDebugEnabled()) {
            org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCache.LOG.debug("Fetching metrics with key: {}", key);
        }
        validateKey(key);
        org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricsCacheValue value = null;
        try {
            value = cache.get(key);
        } catch (org.ehcache.spi.loaderwriter.CacheLoadingException cle) {
            java.lang.Throwable t = cle.getCause();
            if (t instanceof java.net.SocketTimeoutException) {
                throw new java.net.SocketTimeoutException(t.getMessage());
            }
            if (t instanceof java.io.IOException) {
                throw new java.io.IOException(t.getMessage());
            }
            throw cle;
        }
        org.apache.hadoop.metrics2.sink.timeline.TimelineMetrics timelineMetrics = new org.apache.hadoop.metrics2.sink.timeline.TimelineMetrics();
        if (value != null) {
            if (org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCache.LOG.isDebugEnabled()) {
                org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCache.LOG.debug("Returning value from cache: {}", value);
            }
            timelineMetrics = value.getTimelineMetrics();
        }
        if (org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCache.LOG.isDebugEnabled()) {
            if (org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCache.printCacheStatsCounter.getAndIncrement() == 0) {
                org.ehcache.core.statistics.CacheStatistics cacheStatistics = statisticsService.getCacheStatistics(org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCache.TIMELINE_METRIC_CACHE_INSTANCE_NAME);
                if (cacheStatistics == null) {
                    org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCache.LOG.warn("Cache statistics not available.");
                    return timelineMetrics;
                }
                org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCache.LOG.debug("Metrics cache stats => \n, Evictions = {}, Expired = {}, Hits = {}, Misses = {}, Hit ratio = {}, Puts = {}", cacheStatistics.getCacheEvictions(), cacheStatistics.getCacheExpirations(), cacheStatistics.getCacheHits(), cacheStatistics.getCacheMisses(), cacheStatistics.getCacheHitPercentage(), cacheStatistics.getCachePuts());
            } else {
                org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCache.printCacheStatsCounter.compareAndSet(100, 0);
            }
        }
        return timelineMetrics;
    }

    private void validateKey(org.apache.ambari.server.controller.metrics.timeline.cache.TimelineAppMetricCacheKey key) throws java.lang.IllegalArgumentException {
        java.lang.StringBuilder msg = new java.lang.StringBuilder("Invalid metric key requested.");
        boolean throwException = false;
        if (key.getTemporalInfo() == null) {
            msg.append(" No temporal info provided.");
            throwException = true;
        }
        if (key.getSpec() == null) {
            msg.append(" Missing call spec for metric request.");
        }
        if (throwException) {
            throw new java.lang.IllegalArgumentException(msg.toString());
        }
    }
}