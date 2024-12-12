package org.apache.ambari.server.controller.metrics;
class PassThroughTransferMethod extends org.apache.ambari.server.controller.metrics.MetricsDataTransferMethod {
    @java.lang.Override
    public java.lang.Double getData(java.lang.Double data) {
        return data;
    }
}