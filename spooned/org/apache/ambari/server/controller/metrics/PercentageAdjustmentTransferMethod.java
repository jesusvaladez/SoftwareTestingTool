package org.apache.ambari.server.controller.metrics;
class PercentageAdjustmentTransferMethod extends org.apache.ambari.server.controller.metrics.MetricsDataTransferMethod {
    @java.lang.Override
    public java.lang.Double getData(java.lang.Double data) {
        return data < 100 ? data : data / 100;
    }
}