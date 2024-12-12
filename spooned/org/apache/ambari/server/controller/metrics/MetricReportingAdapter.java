package org.apache.ambari.server.controller.metrics;
import org.apache.hadoop.metrics2.sink.timeline.TimelineMetric;
public class MetricReportingAdapter {
    private org.apache.ambari.server.controller.metrics.MetricsDownsamplingMethod downsamplingMethod;

    private org.apache.ambari.server.controller.metrics.MetricsDataTransferMethod dataTransferMethod;

    public MetricReportingAdapter(org.apache.hadoop.metrics2.sink.timeline.TimelineMetric metricDecl) {
        downsamplingMethod = org.apache.ambari.server.controller.metrics.MetricsDownsamplingMethodFactory.detectDownsamplingMethod(metricDecl);
        dataTransferMethod = org.apache.ambari.server.controller.metrics.MetricsDataTransferMethodFactory.detectDataTransferMethod(metricDecl);
    }

    public java.lang.Number[][] reportMetricData(org.apache.hadoop.metrics2.sink.timeline.TimelineMetric metricData, org.apache.ambari.server.controller.spi.TemporalInfo temporalInfo) {
        return downsamplingMethod.reportMetricData(metricData, dataTransferMethod, temporalInfo);
    }
}