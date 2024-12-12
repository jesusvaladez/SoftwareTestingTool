package org.apache.ambari.server.controller.metrics.timeline.cache;
import org.apache.hadoop.metrics2.sink.timeline.TimelineMetric;
import org.apache.hadoop.metrics2.sink.timeline.TimelineMetrics;
import org.apache.http.client.utils.URIBuilder;
import org.easymock.EasyMock;
import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.EntryUnit;
import org.ehcache.core.internal.statistics.DefaultStatisticsService;
import static org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCacheProvider.TIMELINE_METRIC_CACHE_INSTANCE_NAME;
import static org.easymock.EasyMock.anyLong;
import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.createMockBuilder;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
public class TimelineMetricCacheTest {
    @org.junit.Test
    public void testTimelineMetricCache() throws java.lang.Exception {
        org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCacheEntryFactory cacheEntryFactory = EasyMock.createMock(org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCacheEntryFactory.class);
        final long now = java.lang.System.currentTimeMillis();
        org.apache.hadoop.metrics2.sink.timeline.TimelineMetrics metrics = new org.apache.hadoop.metrics2.sink.timeline.TimelineMetrics();
        org.apache.hadoop.metrics2.sink.timeline.TimelineMetric timelineMetric = new org.apache.hadoop.metrics2.sink.timeline.TimelineMetric();
        timelineMetric.setMetricName("cpu_user");
        timelineMetric.setAppId("app1");
        java.util.TreeMap<java.lang.Long, java.lang.Double> metricValues = new java.util.TreeMap<>();
        metricValues.put(now + 100, 1.0);
        metricValues.put(now + 200, 2.0);
        metricValues.put(now + 300, 3.0);
        timelineMetric.setMetricValues(metricValues);
        metrics.getMetrics().add(timelineMetric);
        org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricsCacheValue testValue = new org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricsCacheValue(now, now + 1000, metrics, null);
        java.util.Set<java.lang.String> metricNames = new java.util.HashSet<>(java.util.Arrays.asList("metric1", "metric2"));
        java.lang.String appId = "appId1";
        java.lang.String instanceId = "instanceId1";
        org.apache.ambari.server.controller.spi.TemporalInfo temporalInfo = new org.apache.ambari.server.controller.internal.TemporalInfoImpl(100L, 200L, 1);
        org.apache.ambari.server.controller.metrics.timeline.cache.TimelineAppMetricCacheKey testKey = new org.apache.ambari.server.controller.metrics.timeline.cache.TimelineAppMetricCacheKey(metricNames, appId, instanceId, temporalInfo);
        EasyMock.expect(cacheEntryFactory.load(testKey)).andReturn(testValue).anyTimes();
        EasyMock.replay(cacheEntryFactory);
        org.apache.ambari.server.configuration.Configuration configuration = EasyMock.createNiceMock(org.apache.ambari.server.configuration.Configuration.class);
        EasyMock.expect(configuration.getMetricCacheTTLSeconds()).andReturn(3600);
        EasyMock.expect(configuration.getMetricCacheIdleSeconds()).andReturn(1800);
        EasyMock.expect(configuration.getMetricCacheEntryUnitSize()).andReturn(100).anyTimes();
        EasyMock.replay(configuration);
        org.ehcache.core.internal.statistics.DefaultStatisticsService statisticsService = new org.ehcache.core.internal.statistics.DefaultStatisticsService();
        org.ehcache.CacheManager manager = org.ehcache.config.builders.CacheManagerBuilder.newCacheManagerBuilder().using(statisticsService).build(true);
        org.ehcache.config.builders.CacheConfigurationBuilder<org.apache.ambari.server.controller.metrics.timeline.cache.TimelineAppMetricCacheKey, org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricsCacheValue> cacheConfigurationBuilder = createTestCacheConfiguration(configuration, cacheEntryFactory);
        org.ehcache.Cache<org.apache.ambari.server.controller.metrics.timeline.cache.TimelineAppMetricCacheKey, org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricsCacheValue> cache = manager.createCache(org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCacheProvider.TIMELINE_METRIC_CACHE_INSTANCE_NAME, cacheConfigurationBuilder);
        org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCache testCache = new org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCache(cache, cacheEntryFactory, statisticsService);
        org.apache.hadoop.metrics2.sink.timeline.TimelineMetrics testTimelineMetrics = testCache.getAppTimelineMetricsFromCache(testKey);
        junit.framework.Assert.assertEquals(metrics, testTimelineMetrics);
        EasyMock.verify(cacheEntryFactory);
    }

    private org.ehcache.config.builders.CacheConfigurationBuilder createTestCacheConfiguration(org.apache.ambari.server.configuration.Configuration configuration, org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCacheEntryFactory cacheEntryFactory) {
        org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCacheCustomExpiry timelineMetricCacheCustomExpiry = new org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCacheCustomExpiry(java.time.Duration.ofSeconds(configuration.getMetricCacheTTLSeconds()), java.time.Duration.ofSeconds(configuration.getMetricCacheIdleSeconds()));
        org.ehcache.config.builders.CacheConfigurationBuilder<org.apache.ambari.server.controller.metrics.timeline.cache.TimelineAppMetricCacheKey, org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricsCacheValue> cacheConfigurationBuilder = org.ehcache.config.builders.CacheConfigurationBuilder.newCacheConfigurationBuilder(org.apache.ambari.server.controller.metrics.timeline.cache.TimelineAppMetricCacheKey.class, org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricsCacheValue.class, org.ehcache.config.builders.ResourcePoolsBuilder.newResourcePoolsBuilder().heap(configuration.getMetricCacheEntryUnitSize(), EntryUnit.ENTRIES)).withKeySerializer(org.apache.ambari.server.controller.metrics.timeline.cache.TimelineAppMetricCacheKeySerializer.class).withValueSerializer(org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricsCacheValueSerializer.class).withLoaderWriter(cacheEntryFactory).withExpiry(timelineMetricCacheCustomExpiry);
        return cacheConfigurationBuilder;
    }

    @org.junit.Test
    public void testTimelineMetricCacheProviderGets() throws java.lang.Exception {
        org.apache.ambari.server.configuration.Configuration configuration = EasyMock.createNiceMock(org.apache.ambari.server.configuration.Configuration.class);
        EasyMock.expect(configuration.getMetricCacheTTLSeconds()).andReturn(3600);
        EasyMock.expect(configuration.getMetricCacheIdleSeconds()).andReturn(1800);
        EasyMock.expect(configuration.getMetricCacheEntryUnitSize()).andReturn(100).anyTimes();
        EasyMock.replay(configuration);
        final long now = java.lang.System.currentTimeMillis();
        org.apache.hadoop.metrics2.sink.timeline.TimelineMetrics metrics = new org.apache.hadoop.metrics2.sink.timeline.TimelineMetrics();
        org.apache.hadoop.metrics2.sink.timeline.TimelineMetric timelineMetric = new org.apache.hadoop.metrics2.sink.timeline.TimelineMetric();
        timelineMetric.setMetricName("cpu_user");
        timelineMetric.setAppId("app1");
        java.util.TreeMap<java.lang.Long, java.lang.Double> metricValues = new java.util.TreeMap<>();
        metricValues.put(now + 100, 1.0);
        metricValues.put(now + 200, 2.0);
        metricValues.put(now + 300, 3.0);
        timelineMetric.setMetricValues(metricValues);
        metrics.getMetrics().add(timelineMetric);
        org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCacheEntryFactory cacheEntryFactory = EasyMock.createMock(org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCacheEntryFactory.class);
        org.apache.ambari.server.controller.metrics.timeline.cache.TimelineAppMetricCacheKey queryKey = new org.apache.ambari.server.controller.metrics.timeline.cache.TimelineAppMetricCacheKey(java.util.Collections.singleton("cpu_user"), "app1", new org.apache.ambari.server.controller.internal.TemporalInfoImpl(now, now + 1000, 1));
        org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricsCacheValue value = new org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricsCacheValue(now, now + 1000, metrics, null);
        org.apache.ambari.server.controller.metrics.timeline.cache.TimelineAppMetricCacheKey testKey = new org.apache.ambari.server.controller.metrics.timeline.cache.TimelineAppMetricCacheKey(java.util.Collections.singleton("cpu_user"), "app1", new org.apache.ambari.server.controller.internal.TemporalInfoImpl(now, now + 2000, 1));
        EasyMock.expect(cacheEntryFactory.load(EasyMock.anyObject())).andReturn(value);
        EasyMock.replay(cacheEntryFactory);
        org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCacheProvider cacheProvider = EasyMock.createMockBuilder(org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCacheProvider.class).addMockedMethod("createCacheConfiguration").withConstructor(configuration, cacheEntryFactory).createNiceMock();
        EasyMock.expect(cacheProvider.createCacheConfiguration()).andReturn(createTestCacheConfiguration(configuration, cacheEntryFactory)).anyTimes();
        EasyMock.replay(cacheProvider);
        org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCache cache = cacheProvider.getTimelineMetricsCache();
        metrics = cache.getAppTimelineMetricsFromCache(queryKey);
        java.util.List<org.apache.hadoop.metrics2.sink.timeline.TimelineMetric> metricsList = metrics.getMetrics();
        junit.framework.Assert.assertEquals(1, metricsList.size());
        org.apache.hadoop.metrics2.sink.timeline.TimelineMetric metric = metricsList.iterator().next();
        junit.framework.Assert.assertEquals("cpu_user", metric.getMetricName());
        junit.framework.Assert.assertEquals("app1", metric.getAppId());
        junit.framework.Assert.assertSame(metricValues, metric.getMetricValues());
        metrics = cache.getAppTimelineMetricsFromCache(testKey);
        metricsList = metrics.getMetrics();
        junit.framework.Assert.assertEquals(1, metricsList.size());
        junit.framework.Assert.assertEquals("cpu_user", metric.getMetricName());
        junit.framework.Assert.assertEquals("app1", metric.getAppId());
        junit.framework.Assert.assertSame(metricValues, metric.getMetricValues());
        EasyMock.verify(configuration, cacheEntryFactory);
    }

    @org.junit.Test
    @java.lang.SuppressWarnings("all")
    public void testCacheUpdateBoundsOnVariousRequestScenarios() throws java.lang.Exception {
        org.apache.ambari.server.configuration.Configuration configuration = EasyMock.createNiceMock(org.apache.ambari.server.configuration.Configuration.class);
        EasyMock.expect(configuration.getMetricsRequestConnectTimeoutMillis()).andReturn(10000);
        EasyMock.expect(configuration.getMetricsRequestReadTimeoutMillis()).andReturn(10000);
        EasyMock.expect(configuration.getMetricsRequestIntervalReadTimeoutMillis()).andReturn(10000);
        EasyMock.expect(configuration.getMetricRequestBufferTimeCatchupInterval()).andReturn(0L);
        EasyMock.replay(configuration);
        org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCacheEntryFactory factory = EasyMock.createMockBuilder(org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCacheEntryFactory.class).withConstructor(configuration).createMock();
        EasyMock.replay(factory);
        long now = java.lang.System.currentTimeMillis();
        final long existingSeriesStartTime = now - (3600 * 1000);
        final long existingSeriesEndTime = now;
        long requestedStartTime = existingSeriesStartTime + 60000;
        long requestedEndTime = existingSeriesEndTime + 60000;
        long newStartTime = factory.getRefreshRequestStartTime(existingSeriesStartTime, existingSeriesEndTime, requestedStartTime);
        long newEndTime = factory.getRefreshRequestEndTime(existingSeriesStartTime, existingSeriesEndTime, requestedEndTime);
        junit.framework.Assert.assertEquals(existingSeriesEndTime, newStartTime);
        junit.framework.Assert.assertEquals(requestedEndTime, newEndTime);
        requestedStartTime = existingSeriesEndTime + 60000;
        requestedEndTime = (existingSeriesEndTime + 60000) + 3600000;
        newStartTime = factory.getRefreshRequestStartTime(existingSeriesStartTime, existingSeriesEndTime, requestedStartTime);
        newEndTime = factory.getRefreshRequestEndTime(existingSeriesStartTime, existingSeriesEndTime, requestedEndTime);
        junit.framework.Assert.assertEquals(requestedStartTime, newStartTime);
        junit.framework.Assert.assertEquals(requestedEndTime, newEndTime);
        requestedStartTime = existingSeriesStartTime - 60000;
        requestedEndTime = existingSeriesEndTime + 60000;
        newStartTime = factory.getRefreshRequestStartTime(existingSeriesStartTime, existingSeriesEndTime, requestedStartTime);
        newEndTime = factory.getRefreshRequestEndTime(existingSeriesStartTime, existingSeriesEndTime, requestedEndTime);
        junit.framework.Assert.assertEquals(requestedStartTime, newStartTime);
        junit.framework.Assert.assertEquals(requestedEndTime, newEndTime);
        requestedStartTime = (existingSeriesStartTime - 3600000) - 60000;
        requestedEndTime = existingSeriesStartTime - 60000;
        newStartTime = factory.getRefreshRequestStartTime(existingSeriesStartTime, existingSeriesEndTime, requestedStartTime);
        newEndTime = factory.getRefreshRequestEndTime(existingSeriesStartTime, existingSeriesEndTime, requestedEndTime);
        junit.framework.Assert.assertEquals(requestedStartTime, newStartTime);
        junit.framework.Assert.assertEquals(requestedEndTime, newEndTime);
        requestedStartTime = existingSeriesStartTime + 60000;
        requestedEndTime = existingSeriesEndTime - 60000;
        newStartTime = factory.getRefreshRequestStartTime(existingSeriesStartTime, existingSeriesEndTime, requestedStartTime);
        newEndTime = factory.getRefreshRequestEndTime(existingSeriesStartTime, existingSeriesEndTime, requestedEndTime);
        junit.framework.Assert.assertEquals(newStartTime, existingSeriesEndTime);
        junit.framework.Assert.assertEquals(newEndTime, existingSeriesStartTime);
        EasyMock.verify(configuration, factory);
    }

    @org.junit.Test
    public void testTimelineMetricCacheTimeseriesUpdates() throws java.lang.Exception {
        org.apache.ambari.server.configuration.Configuration configuration = EasyMock.createNiceMock(org.apache.ambari.server.configuration.Configuration.class);
        EasyMock.expect(configuration.getMetricsRequestConnectTimeoutMillis()).andReturn(10000);
        EasyMock.expect(configuration.getMetricsRequestReadTimeoutMillis()).andReturn(10000);
        EasyMock.expect(configuration.getMetricsRequestIntervalReadTimeoutMillis()).andReturn(10000);
        EasyMock.expect(configuration.getMetricRequestBufferTimeCatchupInterval()).andReturn(0L);
        EasyMock.replay(configuration);
        org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCacheEntryFactory factory = EasyMock.createMockBuilder(org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCacheEntryFactory.class).withConstructor(configuration).createMock();
        EasyMock.replay(factory);
        long now = java.lang.System.currentTimeMillis();
        final org.apache.hadoop.metrics2.sink.timeline.TimelineMetric timelineMetric1 = new org.apache.hadoop.metrics2.sink.timeline.TimelineMetric();
        timelineMetric1.setMetricName("cpu_user");
        timelineMetric1.setAppId("app1");
        java.util.TreeMap<java.lang.Long, java.lang.Double> metricValues = new java.util.TreeMap<>();
        metricValues.put(now - 100, 1.0);
        metricValues.put(now - 200, 2.0);
        metricValues.put(now - 300, 3.0);
        timelineMetric1.setMetricValues(metricValues);
        final org.apache.hadoop.metrics2.sink.timeline.TimelineMetric timelineMetric2 = new org.apache.hadoop.metrics2.sink.timeline.TimelineMetric();
        timelineMetric2.setMetricName("cpu_nice");
        timelineMetric2.setAppId("app1");
        metricValues = new java.util.TreeMap<>();
        metricValues.put(now + 400, 1.0);
        metricValues.put(now + 500, 2.0);
        metricValues.put(now + 600, 3.0);
        timelineMetric2.setMetricValues(metricValues);
        org.apache.hadoop.metrics2.sink.timeline.TimelineMetrics existingMetrics = new org.apache.hadoop.metrics2.sink.timeline.TimelineMetrics();
        existingMetrics.getMetrics().add(timelineMetric1);
        existingMetrics.getMetrics().add(timelineMetric2);
        org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricsCacheValue existingMetricValue = new org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricsCacheValue(now - 1000, now + 1000, existingMetrics, null);
        org.apache.hadoop.metrics2.sink.timeline.TimelineMetrics newMetrics = new org.apache.hadoop.metrics2.sink.timeline.TimelineMetrics();
        org.apache.hadoop.metrics2.sink.timeline.TimelineMetric timelineMetric3 = new org.apache.hadoop.metrics2.sink.timeline.TimelineMetric();
        timelineMetric3.setMetricName("cpu_user");
        timelineMetric3.setAppId("app1");
        metricValues = new java.util.TreeMap<>();
        metricValues.put(now + 1400, 1.0);
        metricValues.put(now + 1500, 2.0);
        metricValues.put(now + 1600, 3.0);
        timelineMetric3.setMetricValues(metricValues);
        newMetrics.getMetrics().add(timelineMetric3);
        factory.updateTimelineMetricsInCache(newMetrics, existingMetricValue, now, now + 2000, false);
        junit.framework.Assert.assertEquals(2, existingMetricValue.getTimelineMetrics().getMetrics().size());
        org.apache.hadoop.metrics2.sink.timeline.TimelineMetric newMetric1 = null;
        org.apache.hadoop.metrics2.sink.timeline.TimelineMetric newMetric2 = null;
        for (org.apache.hadoop.metrics2.sink.timeline.TimelineMetric metric : existingMetricValue.getTimelineMetrics().getMetrics()) {
            if (metric.getMetricName().equals("cpu_user")) {
                newMetric1 = metric;
            }
            if (metric.getMetricName().equals("cpu_nice")) {
                newMetric2 = metric;
            }
        }
        junit.framework.Assert.assertNotNull(newMetric1);
        junit.framework.Assert.assertNotNull(newMetric2);
        junit.framework.Assert.assertEquals(3, newMetric1.getMetricValues().size());
        junit.framework.Assert.assertEquals(3, newMetric2.getMetricValues().size());
        java.util.Map<java.lang.Long, java.lang.Double> newMetricsMap = newMetric1.getMetricValues();
        java.util.Iterator<java.lang.Long> metricKeyIterator = newMetricsMap.keySet().iterator();
        junit.framework.Assert.assertEquals(now + 1400, metricKeyIterator.next().longValue());
        junit.framework.Assert.assertEquals(now + 1500, metricKeyIterator.next().longValue());
        junit.framework.Assert.assertEquals(now + 1600, metricKeyIterator.next().longValue());
        EasyMock.verify(configuration, factory);
    }

    @org.junit.Test
    public void testEqualsOnKeys() {
        long now = java.lang.System.currentTimeMillis();
        org.apache.ambari.server.controller.spi.TemporalInfo temporalInfo = new org.apache.ambari.server.controller.internal.TemporalInfoImpl(now - 1000, now, 1);
        org.apache.ambari.server.controller.metrics.timeline.cache.TimelineAppMetricCacheKey key1 = new org.apache.ambari.server.controller.metrics.timeline.cache.TimelineAppMetricCacheKey(new java.util.HashSet<java.lang.String>() {
            {
                add("cpu_num._avg");
                add("proc_run._avg");
            }
        }, "HOST", temporalInfo);
        org.apache.ambari.server.controller.metrics.timeline.cache.TimelineAppMetricCacheKey key2 = new org.apache.ambari.server.controller.metrics.timeline.cache.TimelineAppMetricCacheKey(new java.util.HashSet<java.lang.String>() {
            {
                add("cpu_num._avg");
            }
        }, "HOST", temporalInfo);
        junit.framework.Assert.assertFalse(key1.equals(key2));
        junit.framework.Assert.assertFalse(key2.equals(key1));
        key2.getMetricNames().add("proc_run._avg");
        junit.framework.Assert.assertTrue(key1.equals(key2));
    }

    @org.junit.Test
    public void testTimelineMetricCachePrecisionUpdates() throws java.lang.Exception {
        org.apache.ambari.server.configuration.Configuration configuration = EasyMock.createNiceMock(org.apache.ambari.server.configuration.Configuration.class);
        EasyMock.expect(configuration.getMetricCacheTTLSeconds()).andReturn(3600);
        EasyMock.expect(configuration.getMetricCacheIdleSeconds()).andReturn(1800);
        EasyMock.expect(configuration.getMetricCacheEntryUnitSize()).andReturn(100).anyTimes();
        EasyMock.expect(configuration.getMetricRequestBufferTimeCatchupInterval()).andReturn(1000L).anyTimes();
        EasyMock.replay(configuration);
        final long now = java.lang.System.currentTimeMillis();
        long second = 1000;
        long min = 60 * second;
        long hour = 60 * min;
        long day = 24 * hour;
        long year = 365 * day;
        java.util.Map<java.lang.String, org.apache.hadoop.metrics2.sink.timeline.TimelineMetric> valueMap = new java.util.HashMap<>();
        org.apache.hadoop.metrics2.sink.timeline.TimelineMetric timelineMetric = new org.apache.hadoop.metrics2.sink.timeline.TimelineMetric();
        timelineMetric.setMetricName("cpu_user1");
        timelineMetric.setAppId("app1");
        java.util.TreeMap<java.lang.Long, java.lang.Double> metricValues = new java.util.TreeMap<>();
        for (long i = (1 * year) - (1 * day); i >= 0; i -= 1 * day) {
            metricValues.put(now - i, 1.0);
        }
        timelineMetric.setMetricValues(metricValues);
        valueMap.put("cpu_user1", timelineMetric);
        java.util.List<org.apache.hadoop.metrics2.sink.timeline.TimelineMetric> timelineMetricList = new java.util.ArrayList<>();
        timelineMetricList.add(timelineMetric);
        org.apache.hadoop.metrics2.sink.timeline.TimelineMetrics metrics = new org.apache.hadoop.metrics2.sink.timeline.TimelineMetrics();
        metrics.setMetrics(timelineMetricList);
        org.apache.ambari.server.controller.metrics.timeline.cache.TimelineAppMetricCacheKey key = new org.apache.ambari.server.controller.metrics.timeline.cache.TimelineAppMetricCacheKey(java.util.Collections.singleton("cpu_user1"), "app1", new org.apache.ambari.server.controller.internal.TemporalInfoImpl(now - (1 * year), now, 1));
        key.setSpec("");
        java.util.Map<java.lang.String, org.apache.hadoop.metrics2.sink.timeline.TimelineMetric> newValueMap = new java.util.HashMap<>();
        org.apache.hadoop.metrics2.sink.timeline.TimelineMetric newTimelineMetric = new org.apache.hadoop.metrics2.sink.timeline.TimelineMetric();
        newTimelineMetric.setMetricName("cpu_user2");
        newTimelineMetric.setAppId("app2");
        java.util.TreeMap<java.lang.Long, java.lang.Double> newMetricValues = new java.util.TreeMap<>();
        for (long i = 1 * hour; i <= (2 * day); i += hour) {
            newMetricValues.put((now - (1 * day)) + i, 2.0);
        }
        newTimelineMetric.setMetricValues(newMetricValues);
        newValueMap.put("cpu_user2", newTimelineMetric);
        java.util.List<org.apache.hadoop.metrics2.sink.timeline.TimelineMetric> newTimelineMetricList = new java.util.ArrayList<>();
        newTimelineMetricList.add(newTimelineMetric);
        org.apache.hadoop.metrics2.sink.timeline.TimelineMetrics newMetrics = new org.apache.hadoop.metrics2.sink.timeline.TimelineMetrics();
        newMetrics.setMetrics(newTimelineMetricList);
        org.apache.ambari.server.controller.metrics.timeline.cache.TimelineAppMetricCacheKey newKey = new org.apache.ambari.server.controller.metrics.timeline.cache.TimelineAppMetricCacheKey(java.util.Collections.singleton("cpu_user2"), "app2", new org.apache.ambari.server.controller.internal.TemporalInfoImpl(now - (1 * day), now + (2 * day), 1));
        newKey.setSpec("");
        org.apache.ambari.server.controller.metrics.timeline.MetricsRequestHelper metricsRequestHelperForGets = EasyMock.createMock(org.apache.ambari.server.controller.metrics.timeline.MetricsRequestHelper.class);
        EasyMock.expect(metricsRequestHelperForGets.fetchTimelineMetrics(org.easymock.EasyMock.isA(org.apache.http.client.utils.URIBuilder.class), EasyMock.anyLong(), EasyMock.anyLong())).andReturn(metrics).andReturn(newMetrics);
        EasyMock.replay(metricsRequestHelperForGets);
        org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCacheEntryFactory cacheEntryFactory = EasyMock.createMockBuilder(org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCacheEntryFactory.class).withConstructor(configuration).createMock();
        java.lang.reflect.Field requestHelperField = org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCacheEntryFactory.class.getDeclaredField("requestHelperForGets");
        requestHelperField.setAccessible(true);
        requestHelperField.set(cacheEntryFactory, metricsRequestHelperForGets);
        requestHelperField = org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCacheEntryFactory.class.getDeclaredField("requestHelperForUpdates");
        requestHelperField.setAccessible(true);
        requestHelperField.set(cacheEntryFactory, metricsRequestHelperForGets);
        EasyMock.replay(cacheEntryFactory);
        org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCacheProvider cacheProvider = EasyMock.createMockBuilder(org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCacheProvider.class).addMockedMethod("createCacheConfiguration").withConstructor(configuration, cacheEntryFactory).createNiceMock();
        EasyMock.expect(cacheProvider.createCacheConfiguration()).andReturn(createTestCacheConfiguration(configuration, cacheEntryFactory)).anyTimes();
        EasyMock.replay(cacheProvider);
        org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCache cache = cacheProvider.getTimelineMetricsCache();
        metrics = cache.getAppTimelineMetricsFromCache(key);
        java.util.List<org.apache.hadoop.metrics2.sink.timeline.TimelineMetric> metricsList = metrics.getMetrics();
        junit.framework.Assert.assertEquals(1, metricsList.size());
        org.apache.hadoop.metrics2.sink.timeline.TimelineMetric metric = metricsList.iterator().next();
        junit.framework.Assert.assertEquals("cpu_user1", metric.getMetricName());
        junit.framework.Assert.assertEquals("app1", metric.getAppId());
        junit.framework.Assert.assertEquals(metricValues, metric.getMetricValues());
        java.lang.System.out.println("first call values: " + metric.getMetricValues());
        java.lang.System.out.println();
        metrics = cache.getAppTimelineMetricsFromCache(newKey);
        metricsList = metrics.getMetrics();
        junit.framework.Assert.assertEquals(1, metricsList.size());
        metric = metricsList.iterator().next();
        junit.framework.Assert.assertEquals("cpu_user2", metric.getMetricName());
        junit.framework.Assert.assertEquals("app2", metric.getAppId());
        java.lang.System.out.println("Second call values: " + metric.getMetricValues());
        junit.framework.Assert.assertEquals(newMetricValues, metric.getMetricValues());
        EasyMock.verify(configuration, metricsRequestHelperForGets, cacheEntryFactory);
    }
}