package org.apache.ambari.server.controller.metrics.timeline.cache;
import org.apache.hadoop.metrics2.sink.timeline.Precision;
import org.apache.hadoop.metrics2.sink.timeline.TimelineMetric;
import org.apache.hadoop.metrics2.sink.timeline.TimelineMetrics;
public class TimelineMetricsCacheValue {
    private java.lang.Long startTime;

    private java.lang.Long endTime;

    private org.apache.hadoop.metrics2.sink.timeline.TimelineMetrics timelineMetrics = new org.apache.hadoop.metrics2.sink.timeline.TimelineMetrics();

    private org.apache.hadoop.metrics2.sink.timeline.Precision precision;

    public TimelineMetricsCacheValue(java.lang.Long startTime, java.lang.Long endTime, org.apache.hadoop.metrics2.sink.timeline.TimelineMetrics timelineMetrics, org.apache.hadoop.metrics2.sink.timeline.Precision precision) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.timelineMetrics = timelineMetrics;
        this.precision = precision;
    }

    public org.apache.hadoop.metrics2.sink.timeline.TimelineMetrics getTimelineMetrics() {
        return timelineMetrics;
    }

    public void setTimelineMetrics(org.apache.hadoop.metrics2.sink.timeline.TimelineMetrics timelineMetrics) {
        this.timelineMetrics = timelineMetrics;
    }

    public java.lang.Long getStartTime() {
        return startTime;
    }

    public void setStartTime(java.lang.Long startTime) {
        this.startTime = startTime;
    }

    public java.lang.Long getEndTime() {
        return endTime;
    }

    public void setEndTime(java.lang.Long endTime) {
        this.endTime = endTime;
    }

    private long getMillisecondsTime(long time) {
        if (time < 9999999999L) {
            return time * 1000;
        } else {
            return time;
        }
    }

    @java.lang.Override
    public java.lang.String toString() {
        java.lang.StringBuilder sb = new java.lang.StringBuilder((((((("TimelineMetricsCacheValue {" + ", startTime = ") + new java.util.Date(getMillisecondsTime(startTime))) + ", endTime = ") + new java.util.Date(getMillisecondsTime(endTime))) + ", precision = ") + precision) + ", timelineMetrics =");
        for (org.apache.hadoop.metrics2.sink.timeline.TimelineMetric metric : timelineMetrics.getMetrics()) {
            sb.append(" { ");
            sb.append(metric.getMetricName());
            sb.append(", ");
            sb.append(metric.getHostName());
            sb.append(" # ");
            sb.append(metric.getMetricValues().size());
            sb.append(" }");
        }
        sb.append("}");
        return sb.toString();
    }

    public org.apache.hadoop.metrics2.sink.timeline.Precision getPrecision() {
        return precision;
    }

    public void setPrecision(org.apache.hadoop.metrics2.sink.timeline.Precision precision) {
        this.precision = precision;
    }
}