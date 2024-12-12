package org.apache.ambari.server.controller.metrics.timeline.cache;
import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.EntryUnit;
import org.ehcache.core.internal.statistics.DefaultStatisticsService;
@com.google.inject.Singleton
public class TimelineMetricCacheProvider {
    private org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCache timelineMetricsCache;

    private volatile boolean isCacheInitialized = false;

    public static final java.lang.String TIMELINE_METRIC_CACHE_INSTANCE_NAME = "timelineMetricCache";

    org.apache.ambari.server.configuration.Configuration configuration;

    org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCacheEntryFactory cacheEntryFactory;

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCacheProvider.class);

    @com.google.inject.Inject
    public TimelineMetricCacheProvider(org.apache.ambari.server.configuration.Configuration configuration, org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCacheEntryFactory cacheEntryFactory) {
        this.configuration = configuration;
        this.cacheEntryFactory = cacheEntryFactory;
    }

    private synchronized void initializeCache() {
        if (isCacheInitialized) {
            return;
        }
        org.ehcache.core.internal.statistics.DefaultStatisticsService statisticsService = new org.ehcache.core.internal.statistics.DefaultStatisticsService();
        org.ehcache.CacheManager manager = org.ehcache.config.builders.CacheManagerBuilder.newCacheManagerBuilder().using(statisticsService).build(true);
        org.ehcache.config.builders.CacheConfigurationBuilder<org.apache.ambari.server.controller.metrics.timeline.cache.TimelineAppMetricCacheKey, org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricsCacheValue> cacheConfigurationBuilder = createCacheConfiguration();
        org.ehcache.Cache<org.apache.ambari.server.controller.metrics.timeline.cache.TimelineAppMetricCacheKey, org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricsCacheValue> cache = manager.createCache(org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCacheProvider.TIMELINE_METRIC_CACHE_INSTANCE_NAME, cacheConfigurationBuilder);
        timelineMetricsCache = new org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCache(cache, cacheEntryFactory, statisticsService);
        org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCacheProvider.LOG.info((("Registering metrics cache with provider: name = " + org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCacheProvider.TIMELINE_METRIC_CACHE_INSTANCE_NAME) + ", manager = ") + manager);
        isCacheInitialized = true;
    }

    public org.ehcache.config.builders.CacheConfigurationBuilder createCacheConfiguration() {
        org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCacheProvider.LOG.info((((("Creating Metrics Cache with timeouts => ttl = " + configuration.getMetricCacheTTLSeconds()) + ", idle = ") + configuration.getMetricCacheIdleSeconds()) + ", cache size = ") + configuration.getMetricCacheEntryUnitSize());
        org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCacheCustomExpiry timelineMetricCacheCustomExpiry = new org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCacheCustomExpiry(java.time.Duration.ofSeconds(configuration.getMetricCacheTTLSeconds()), java.time.Duration.ofSeconds(configuration.getMetricCacheIdleSeconds()));
        org.ehcache.config.builders.CacheConfigurationBuilder<org.apache.ambari.server.controller.metrics.timeline.cache.TimelineAppMetricCacheKey, org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricsCacheValue> cacheConfigurationBuilder = org.ehcache.config.builders.CacheConfigurationBuilder.newCacheConfigurationBuilder(org.apache.ambari.server.controller.metrics.timeline.cache.TimelineAppMetricCacheKey.class, org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricsCacheValue.class, org.ehcache.config.builders.ResourcePoolsBuilder.newResourcePoolsBuilder().heap(configuration.getMetricCacheEntryUnitSize(), EntryUnit.ENTRIES)).withKeySerializer(org.apache.ambari.server.controller.metrics.timeline.cache.TimelineAppMetricCacheKeySerializer.class).withValueSerializer(org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricsCacheValueSerializer.class).withLoaderWriter(cacheEntryFactory).withExpiry(timelineMetricCacheCustomExpiry);
        return cacheConfigurationBuilder;
    }

    public org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCache getTimelineMetricsCache() {
        if (configuration.isMetricsCacheDisabled()) {
            return null;
        }
        if (!isCacheInitialized) {
            initializeCache();
        }
        return timelineMetricsCache;
    }
}