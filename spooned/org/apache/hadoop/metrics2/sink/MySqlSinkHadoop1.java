package org.apache.hadoop.metrics2.sink;
import org.apache.hadoop.metrics2.Metric;
import org.apache.hadoop.metrics2.MetricsRecord;
public class MySqlSinkHadoop1 extends org.apache.hadoop.metrics2.sink.MySqlSink {
    public MySqlSinkHadoop1() {
        super(org.apache.hadoop.metrics2.sink.SqlSink.HADOOP1_NAMENODE_URL_KEY, org.apache.hadoop.metrics2.sink.SqlSink.HADOOP1_DFS_BLOCK_SIZE_KEY);
    }

    @java.lang.Override
    public void putMetrics(org.apache.hadoop.metrics2.MetricsRecord record) {
        long metricRecordID = getMetricRecordID(record.context(), record.name(), getLocalNodeName(), getLocalNodeIPAddress(), getClusterNodeName(), getCurrentServiceName(), getTagString(record.tags()), record.timestamp());
        if (metricRecordID < 0)
            return;

        for (org.apache.hadoop.metrics2.Metric metric : record.metrics()) {
            insertMetricValue(metricRecordID, metric.name(), java.lang.String.valueOf(metric.value()));
            if (metric.name().equals("BlockCapacity")) {
                insertMetricValue(metricRecordID, "BlockSize", java.lang.Integer.toString(getBlockSize()));
            }
        }
    }
}