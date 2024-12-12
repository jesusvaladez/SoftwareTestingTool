package org.apache.ambari.server.controller.metrics;
class MetricNoDownsampling extends org.apache.ambari.server.controller.metrics.MetricsDownsamplingMethod {
    @java.lang.Override
    public java.lang.Number[][] reportMetricData(org.apache.hadoop.metrics2.sink.timeline.TimelineMetric metricData, org.apache.ambari.server.controller.metrics.MetricsDataTransferMethod dataTransferMethod, org.apache.ambari.server.controller.spi.TemporalInfo temporalInfo) {
        java.lang.Number[][] datapointsArray = new java.lang.Number[metricData.getMetricValues().size()][2];
        int cnt = 0;
        for (java.util.Map.Entry<java.lang.Long, java.lang.Double> metricEntry : metricData.getMetricValues().entrySet()) {
            if (isWithinTemporalQueryRange(metricEntry.getKey(), temporalInfo)) {
                datapointsArray[cnt][0] = dataTransferMethod.getData(metricEntry.getValue());
                datapointsArray[cnt][1] = metricEntry.getKey();
                cnt++;
            }
        }
        return datapointsArray;
    }
}