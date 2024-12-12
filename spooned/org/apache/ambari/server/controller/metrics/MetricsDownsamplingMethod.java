package org.apache.ambari.server.controller.metrics;
import org.apache.hadoop.metrics2.sink.timeline.TimelineMetric;
public abstract class MetricsDownsamplingMethod {
    private static final long OUT_OF_BAND_TIME_ALLOWANCE = 120000;

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.controller.metrics.MetricsDownsamplingMethod.class);

    public abstract java.lang.Number[][] reportMetricData(org.apache.hadoop.metrics2.sink.timeline.TimelineMetric metricData, org.apache.ambari.server.controller.metrics.MetricsDataTransferMethod dataTransferMethod, org.apache.ambari.server.controller.spi.TemporalInfo temporalInfo);

    protected boolean isWithinTemporalQueryRange(java.lang.Long timestamp, org.apache.ambari.server.controller.spi.TemporalInfo temporalInfo) {
        boolean retVal = (temporalInfo == null) || ((timestamp >= (temporalInfo.getStartTimeMillis() - org.apache.ambari.server.controller.metrics.MetricsDownsamplingMethod.OUT_OF_BAND_TIME_ALLOWANCE)) && (timestamp <= temporalInfo.getEndTimeMillis()));
        if ((!retVal) && org.apache.ambari.server.controller.metrics.MetricsDownsamplingMethod.LOG.isTraceEnabled()) {
            org.apache.ambari.server.controller.metrics.MetricsDownsamplingMethod.LOG.trace("Ignoring out of band metric with ts: {}, temporalInfo: startTime = {}, endTime = {}", timestamp, temporalInfo.getStartTimeMillis(), temporalInfo.getEndTimeMillis());
        }
        return retVal;
    }
}