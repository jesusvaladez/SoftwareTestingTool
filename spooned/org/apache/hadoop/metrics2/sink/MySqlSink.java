package org.apache.hadoop.metrics2.sink;
public abstract class MySqlSink extends org.apache.hadoop.metrics2.sink.SqlSink {
    public MySqlSink(java.lang.String NAMENODE_URL_KEY, java.lang.String DFS_BLOCK_SIZE_KEY) {
        super(NAMENODE_URL_KEY, DFS_BLOCK_SIZE_KEY);
    }

    @java.lang.Override
    protected java.lang.String getInsertMetricsProcedureName() {
        return org.apache.hadoop.metrics2.sink.SqlSink.insertMetricProc;
    }

    @java.lang.Override
    protected java.lang.String getGetMetricsProcedureName() {
        return org.apache.hadoop.metrics2.sink.SqlSink.getMetricRecordProc;
    }

    @java.lang.Override
    protected java.lang.String getDatabaseDriverClassName() {
        return "com.mysql.jdbc.Driver";
    }
}