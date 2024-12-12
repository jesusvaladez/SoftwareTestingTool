package org.apache.ambari.server.controller.metrics.timeline.cache;
import org.apache.hadoop.metrics2.sink.timeline.Precision;
import org.apache.hadoop.metrics2.sink.timeline.TimelineMetric;
import org.apache.hadoop.metrics2.sink.timeline.TimelineMetrics;
import org.apache.http.client.utils.URIBuilder;
import org.ehcache.spi.loaderwriter.BulkCacheLoadingException;
import org.ehcache.spi.loaderwriter.BulkCacheWritingException;
import org.ehcache.spi.loaderwriter.CacheLoaderWriter;
import org.ehcache.spi.loaderwriter.CacheWritingException;
@com.google.inject.Singleton
public class TimelineMetricCacheEntryFactory implements org.ehcache.spi.loaderwriter.CacheLoaderWriter<org.apache.ambari.server.controller.metrics.timeline.cache.TimelineAppMetricCacheKey, org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricsCacheValue> {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCacheEntryFactory.class);

    private org.apache.ambari.server.controller.metrics.timeline.MetricsRequestHelper requestHelperForGets;

    private org.apache.ambari.server.controller.metrics.timeline.MetricsRequestHelper requestHelperForUpdates;

    private final java.lang.Long BUFFER_TIME_DIFF_CATCHUP_INTERVAL;

    @com.google.inject.Inject
    public TimelineMetricCacheEntryFactory(org.apache.ambari.server.configuration.Configuration configuration) {
        requestHelperForGets = new org.apache.ambari.server.controller.metrics.timeline.MetricsRequestHelper(new org.apache.ambari.server.controller.internal.URLStreamProvider(configuration.getMetricsRequestConnectTimeoutMillis(), configuration.getMetricsRequestReadTimeoutMillis(), org.apache.ambari.server.configuration.ComponentSSLConfiguration.instance()));
        requestHelperForUpdates = new org.apache.ambari.server.controller.metrics.timeline.MetricsRequestHelper(new org.apache.ambari.server.controller.internal.URLStreamProvider(configuration.getMetricsRequestConnectTimeoutMillis(), configuration.getMetricsRequestIntervalReadTimeoutMillis(), org.apache.ambari.server.configuration.ComponentSSLConfiguration.instance()));
        BUFFER_TIME_DIFF_CATCHUP_INTERVAL = configuration.getMetricRequestBufferTimeCatchupInterval();
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricsCacheValue load(org.apache.ambari.server.controller.metrics.timeline.cache.TimelineAppMetricCacheKey key) throws java.lang.Exception {
        org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCacheEntryFactory.LOG.debug("Creating cache entry since none exists, key = {}", key);
        org.apache.ambari.server.controller.metrics.timeline.cache.TimelineAppMetricCacheKey metricCacheKey = key;
        org.apache.hadoop.metrics2.sink.timeline.TimelineMetrics timelineMetrics = null;
        try {
            org.apache.http.client.utils.URIBuilder uriBuilder = new org.apache.http.client.utils.URIBuilder(metricCacheKey.getSpec());
            timelineMetrics = requestHelperForGets.fetchTimelineMetrics(uriBuilder, metricCacheKey.getTemporalInfo().getStartTimeMillis(), metricCacheKey.getTemporalInfo().getEndTimeMillis());
        } catch (java.net.URISyntaxException e) {
            org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCacheEntryFactory.LOG.debug("Caught URISyntaxException on fetching metrics. {}", e.getMessage());
            throw new java.lang.RuntimeException(e);
        } catch (java.io.IOException io) {
            org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCacheEntryFactory.LOG.debug("Caught IOException on fetching metrics. {}", io.getMessage());
            throw io;
        }
        org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricsCacheValue value = null;
        if ((timelineMetrics != null) && (!timelineMetrics.getMetrics().isEmpty())) {
            value = new org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricsCacheValue(metricCacheKey.getTemporalInfo().getStartTime(), metricCacheKey.getTemporalInfo().getEndTime(), timelineMetrics, org.apache.hadoop.metrics2.sink.timeline.Precision.getPrecision(metricCacheKey.getTemporalInfo().getStartTimeMillis(), metricCacheKey.getTemporalInfo().getEndTimeMillis()));
            org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCacheEntryFactory.LOG.debug("Created cache entry: {}", value);
        }
        return value;
    }

    @java.lang.Override
    public void write(org.apache.ambari.server.controller.metrics.timeline.cache.TimelineAppMetricCacheKey key, org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricsCacheValue value) throws java.lang.Exception {
        org.apache.ambari.server.controller.metrics.timeline.cache.TimelineAppMetricCacheKey metricCacheKey = key;
        org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricsCacheValue existingMetrics = value;
        org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCacheEntryFactory.LOG.debug("Updating cache entry, key: {}, with value = {}", key, value);
        java.lang.Long existingSeriesStartTime = existingMetrics.getStartTime();
        java.lang.Long existingSeriesEndTime = existingMetrics.getEndTime();
        org.apache.ambari.server.controller.spi.TemporalInfo newTemporalInfo = metricCacheKey.getTemporalInfo();
        java.lang.Long requestedStartTime = newTemporalInfo.getStartTimeMillis();
        java.lang.Long requestedEndTime = newTemporalInfo.getEndTimeMillis();
        org.apache.http.client.utils.URIBuilder uriBuilder = new org.apache.http.client.utils.URIBuilder(metricCacheKey.getSpec());
        org.apache.hadoop.metrics2.sink.timeline.Precision requestedPrecision = org.apache.hadoop.metrics2.sink.timeline.Precision.getPrecision(requestedStartTime, requestedEndTime);
        org.apache.hadoop.metrics2.sink.timeline.Precision currentPrecision = existingMetrics.getPrecision();
        java.lang.Long newStartTime = null;
        java.lang.Long newEndTime = null;
        if (!requestedPrecision.equals(currentPrecision)) {
            org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCacheEntryFactory.LOG.debug("Precision changed from {} to {}", currentPrecision, requestedPrecision);
            newStartTime = requestedStartTime;
            newEndTime = requestedEndTime;
        } else {
            org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCacheEntryFactory.LOG.debug("No change in precision {}", currentPrecision);
            newStartTime = getRefreshRequestStartTime(existingSeriesStartTime, existingSeriesEndTime, requestedStartTime);
            newEndTime = getRefreshRequestEndTime(existingSeriesStartTime, existingSeriesEndTime, requestedEndTime);
        }
        if ((newEndTime > newStartTime) && (!((newStartTime.equals(existingSeriesStartTime) && newEndTime.equals(existingSeriesEndTime)) && requestedPrecision.equals(currentPrecision)))) {
            org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCacheEntryFactory.LOG.debug("Existing cached timeseries startTime = {}, endTime = {}", new java.util.Date(getMillisecondsTime(existingSeriesStartTime)), new java.util.Date(getMillisecondsTime(existingSeriesEndTime)));
            org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCacheEntryFactory.LOG.debug("Requested timeseries startTime = {}, endTime = {}", new java.util.Date(getMillisecondsTime(newStartTime)), new java.util.Date(getMillisecondsTime(newEndTime)));
            uriBuilder.setParameter("startTime", java.lang.String.valueOf(newStartTime));
            uriBuilder.setParameter("endTime", java.lang.String.valueOf(newEndTime));
            uriBuilder.setParameter("precision", requestedPrecision.toString());
            try {
                org.apache.hadoop.metrics2.sink.timeline.TimelineMetrics newTimeSeries = requestHelperForUpdates.fetchTimelineMetrics(uriBuilder, newStartTime, newEndTime);
                updateTimelineMetricsInCache(newTimeSeries, existingMetrics, getMillisecondsTime(requestedStartTime), getMillisecondsTime(requestedEndTime), !currentPrecision.equals(requestedPrecision));
                existingMetrics.setStartTime(requestedStartTime);
                existingMetrics.setEndTime(requestedEndTime);
                existingMetrics.setPrecision(requestedPrecision);
            } catch (java.io.IOException io) {
                if (org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCacheEntryFactory.LOG.isDebugEnabled()) {
                    org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCacheEntryFactory.LOG.debug("Exception retrieving metrics.", io);
                }
                throw io;
            }
        } else {
            org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCacheEntryFactory.LOG.debug("Skip updating cache with new startTime = {}, new endTime = {}", new java.util.Date(getMillisecondsTime(newStartTime)), new java.util.Date(getMillisecondsTime(newEndTime)));
        }
    }

    protected void updateTimelineMetricsInCache(org.apache.hadoop.metrics2.sink.timeline.TimelineMetrics newMetrics, org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricsCacheValue timelineMetricsCacheValue, java.lang.Long requestedStartTime, java.lang.Long requestedEndTime, boolean removeAll) {
        org.apache.hadoop.metrics2.sink.timeline.TimelineMetrics existingTimelineMetrics = timelineMetricsCacheValue.getTimelineMetrics();
        updateExistingMetricValues(existingTimelineMetrics, requestedStartTime, requestedEndTime, removeAll);
        if ((newMetrics != null) && (!newMetrics.getMetrics().isEmpty())) {
            for (org.apache.hadoop.metrics2.sink.timeline.TimelineMetric timelineMetric : newMetrics.getMetrics()) {
                if (org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCacheEntryFactory.LOG.isTraceEnabled()) {
                    java.util.TreeMap<java.lang.Long, java.lang.Double> sortedMetrics = new java.util.TreeMap<>(timelineMetric.getMetricValues());
                    org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCacheEntryFactory.LOG.trace("New metric: {} # {}, startTime = {}, endTime = {}", timelineMetric.getMetricName(), timelineMetric.getMetricValues().size(), sortedMetrics.firstKey(), sortedMetrics.lastKey());
                }
                org.apache.hadoop.metrics2.sink.timeline.TimelineMetric existingMetric = null;
                for (org.apache.hadoop.metrics2.sink.timeline.TimelineMetric metric : existingTimelineMetrics.getMetrics()) {
                    if (metric.equalsExceptTime(timelineMetric)) {
                        existingMetric = metric;
                    }
                }
                if (existingMetric != null) {
                    existingMetric.getMetricValues().putAll(timelineMetric.getMetricValues());
                    if (org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCacheEntryFactory.LOG.isTraceEnabled()) {
                        java.util.TreeMap<java.lang.Long, java.lang.Double> sortedMetrics = new java.util.TreeMap<>(existingMetric.getMetricValues());
                        org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCacheEntryFactory.LOG.trace("Merged metric: {}, Final size: {}, startTime = {}, endTime = {}", timelineMetric.getMetricName(), existingMetric.getMetricValues().size(), sortedMetrics.firstKey(), sortedMetrics.lastKey());
                    }
                } else {
                    existingTimelineMetrics.getMetrics().add(timelineMetric);
                }
            }
        }
    }

    private void updateExistingMetricValues(org.apache.hadoop.metrics2.sink.timeline.TimelineMetrics existingMetrics, java.lang.Long requestedStartTime, java.lang.Long requestedEndTime, boolean removeAll) {
        for (org.apache.hadoop.metrics2.sink.timeline.TimelineMetric existingMetric : existingMetrics.getMetrics()) {
            if (removeAll) {
                existingMetric.setMetricValues(new java.util.TreeMap<>());
            } else {
                java.util.TreeMap<java.lang.Long, java.lang.Double> existingMetricValues = existingMetric.getMetricValues();
                org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCacheEntryFactory.LOG.trace("Existing metric: {} # {}", existingMetric.getMetricName(), existingMetricValues.size());
                existingMetricValues.headMap(requestedStartTime, false).clear();
                existingMetricValues.tailMap(requestedEndTime, false).clear();
            }
        }
    }

    protected java.lang.Long getRefreshRequestStartTime(java.lang.Long existingSeriesStartTime, java.lang.Long existingSeriesEndTime, java.lang.Long requestedStartTime) {
        java.lang.Long diff = requestedStartTime - existingSeriesEndTime;
        java.lang.Long startTime = requestedStartTime;
        if ((diff < 0) && (requestedStartTime > existingSeriesStartTime)) {
            startTime = getTimeShiftedStartTime(existingSeriesEndTime);
        }
        org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCacheEntryFactory.LOG.trace("Requesting timeseries data with new startTime = {}", new java.util.Date(getMillisecondsTime(startTime)));
        return startTime;
    }

    protected java.lang.Long getRefreshRequestEndTime(java.lang.Long existingSeriesStartTime, java.lang.Long existingSeriesEndTime, java.lang.Long requestedEndTime) {
        java.lang.Long endTime = requestedEndTime;
        java.lang.Long diff = requestedEndTime - existingSeriesEndTime;
        if ((diff < 0) && (requestedEndTime > existingSeriesStartTime)) {
            endTime = existingSeriesStartTime;
        }
        org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCacheEntryFactory.LOG.trace("Requesting timeseries data with new endTime = {}", new java.util.Date(getMillisecondsTime(endTime)));
        return endTime;
    }

    private long getTimeShiftedStartTime(long startTime) {
        if (startTime < 9999999999L) {
            return startTime - (BUFFER_TIME_DIFF_CATCHUP_INTERVAL / 1000);
        } else {
            return startTime - BUFFER_TIME_DIFF_CATCHUP_INTERVAL;
        }
    }

    private long getMillisecondsTime(long time) {
        if (time < 9999999999L) {
            return time * 1000;
        } else {
            return time;
        }
    }

    @java.lang.Override
    public void delete(org.apache.ambari.server.controller.metrics.timeline.cache.TimelineAppMetricCacheKey key) throws org.ehcache.spi.loaderwriter.CacheWritingException {
    }

    @java.lang.Override
    public java.util.Map<org.apache.ambari.server.controller.metrics.timeline.cache.TimelineAppMetricCacheKey, org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricsCacheValue> loadAll(java.lang.Iterable<? extends org.apache.ambari.server.controller.metrics.timeline.cache.TimelineAppMetricCacheKey> keys) throws org.ehcache.spi.loaderwriter.BulkCacheLoadingException, java.lang.Exception {
        return null;
    }

    @java.lang.Override
    public void writeAll(java.lang.Iterable<? extends java.util.Map.Entry<? extends org.apache.ambari.server.controller.metrics.timeline.cache.TimelineAppMetricCacheKey, ? extends org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricsCacheValue>> entries) throws org.ehcache.spi.loaderwriter.BulkCacheWritingException, java.lang.Exception {
    }

    @java.lang.Override
    public void deleteAll(java.lang.Iterable<? extends org.apache.ambari.server.controller.metrics.timeline.cache.TimelineAppMetricCacheKey> keys) throws org.ehcache.spi.loaderwriter.BulkCacheWritingException, java.lang.Exception {
    }
}