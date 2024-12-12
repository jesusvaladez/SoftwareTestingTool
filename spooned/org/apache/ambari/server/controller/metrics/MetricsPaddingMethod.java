package org.apache.ambari.server.controller.metrics;
import org.apache.hadoop.metrics2.sink.timeline.Precision;
import org.apache.hadoop.metrics2.sink.timeline.TimelineMetric;
public class MetricsPaddingMethod {
    private final org.apache.ambari.server.controller.metrics.MetricsPaddingMethod.PADDING_STRATEGY strategy;

    public static final java.lang.String ZERO_PADDING_PARAM = "params/padding";

    private static final long MINIMUM_STEP_INTERVAL = 999L;

    public enum PADDING_STRATEGY {

        ZEROS,
        NULLS,
        NONE;}

    public MetricsPaddingMethod(org.apache.ambari.server.controller.metrics.MetricsPaddingMethod.PADDING_STRATEGY strategy) {
        this.strategy = strategy;
    }

    public void applyPaddingStrategy(org.apache.hadoop.metrics2.sink.timeline.TimelineMetric metric, org.apache.ambari.server.controller.spi.TemporalInfo temporalInfo) {
        if (strategy.equals(org.apache.ambari.server.controller.metrics.MetricsPaddingMethod.PADDING_STRATEGY.NONE) || (temporalInfo == null)) {
            return;
        }
        java.util.TreeMap<java.lang.Long, java.lang.Double> values = metric.getMetricValues();
        if ((values == null) || values.isEmpty()) {
            return;
        }
        long intervalStartTime = longToMillis(temporalInfo.getStartTime());
        long intervalEndTime = longToMillis(temporalInfo.getEndTime());
        long dataStartTime = longToMillis(values.firstKey());
        long dataEndTime = longToMillis(values.lastKey());
        long dataInterval = getTimelineMetricInterval(values, intervalStartTime, intervalEndTime);
        if ((dataInterval == (-1)) || (dataInterval < org.apache.ambari.server.controller.metrics.MetricsPaddingMethod.MINIMUM_STEP_INTERVAL)) {
            dataInterval = (temporalInfo.getStep() != null) ? temporalInfo.getStep() : -1;
        }
        if (dataInterval == (-1)) {
            return;
        }
        java.lang.Double paddingValue = 0.0;
        if (strategy.equals(org.apache.ambari.server.controller.metrics.MetricsPaddingMethod.PADDING_STRATEGY.NULLS)) {
            paddingValue = null;
        }
        for (long counter = intervalStartTime; counter < dataStartTime; counter += dataInterval) {
            values.put(counter, paddingValue);
        }
        for (long counter = dataEndTime + dataInterval; counter <= intervalEndTime; counter += dataInterval) {
            values.put(counter, paddingValue);
        }
        metric.setMetricValues(values);
    }

    private long longToMillis(long time) {
        if (time < 9999999999L) {
            return time * 1000;
        }
        return time;
    }

    private long getTimelineMetricInterval(java.util.TreeMap<java.lang.Long, java.lang.Double> values, long startTime, long endTime) {
        org.apache.hadoop.metrics2.sink.timeline.Precision precision = org.apache.hadoop.metrics2.sink.timeline.Precision.getPrecision(startTime, endTime);
        long interval;
        if (precision.equals(Precision.DAYS)) {
            interval = java.util.concurrent.TimeUnit.DAYS.toMillis(1);
        } else if (precision.equals(Precision.HOURS)) {
            interval = java.util.concurrent.TimeUnit.HOURS.toMillis(1);
        } else if (precision.equals(Precision.MINUTES)) {
            interval = java.util.concurrent.TimeUnit.MINUTES.toMillis(1);
        } else if ((values != null) && (values.size() > 1)) {
            java.util.Iterator<java.lang.Long> tsValuesIterator = values.descendingKeySet().iterator();
            long lastValue = tsValuesIterator.next();
            long secondToLastValue = tsValuesIterator.next();
            interval = java.lang.Math.abs(lastValue - secondToLastValue);
        } else {
            interval = -1;
        }
        return interval;
    }
}