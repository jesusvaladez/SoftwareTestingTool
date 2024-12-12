package org.apache.hadoop.metrics2.sink;
import org.apache.commons.configuration.SubsetConfiguration;
import org.apache.hadoop.metrics2.MetricsException;
import org.apache.hadoop.metrics2.MetricsRecord;
import org.apache.hadoop.metrics2.MetricsSink;
import org.apache.hadoop.metrics2.MetricsTag;
import org.apache.log4j.Logger;
public abstract class SqlSink implements org.apache.hadoop.metrics2.MetricsSink {
    private static final java.lang.String DATABASE_URL_KEY = "databaseUrl";

    private static final boolean DEBUG = true;

    private final java.lang.String NAMENODE_URL_KEY;

    private static final java.util.regex.Pattern NAME_URL_REGEX = java.util.regex.Pattern.compile("hdfs://([^ :/]*)", java.util.regex.Pattern.CASE_INSENSITIVE);

    private final java.lang.String DFS_BLOCK_SIZE_KEY;

    private int blockSize = -1;

    private java.lang.String currentServiceName = "";

    private java.lang.String databaseUrl;

    private java.sql.Connection conn = null;

    java.lang.StringBuilder tagsListBuffer = new java.lang.StringBuilder();

    java.lang.String nodeName = null;

    java.lang.String nodeIPAddress = null;

    org.apache.hadoop.conf.Configuration hadoopConfig = null;

    java.lang.String clusterName = "localhost";

    static final java.lang.String updateHeartBeatsProc = "uspUpdateHeartBeats";

    static final java.lang.String purgeMetricsProc = "uspPurgeMetrics";

    static final java.lang.String insertMetricProc = "uspInsertMetricValue";

    static final java.lang.String getMetricRecordProc = "uspGetMetricRecord";

    static final java.lang.String getMetricsProc = "ufGetMetrics";

    static final java.lang.String getAggregatedMetricsProc = "ufGetAggregatedServiceMetrics";

    static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(org.apache.hadoop.metrics2.sink.SqlServerSink.class);

    static java.lang.String HADOOP1_NAMENODE_URL_KEY = "fs.default.name";

    static java.lang.String HADOOP2_NAMENODE_URL_KEY = "fs.defaultFS";

    static java.lang.String HADOOP1_DFS_BLOCK_SIZE_KEY = "dfs.block.size";

    static java.lang.String HADOOP2_DFS_BLOCK_SIZE_KEY = "dfs.blocksize";

    public SqlSink(java.lang.String NAMENODE_URL_KEY, java.lang.String DFS_BLOCK_SIZE_KEY) {
        this.NAMENODE_URL_KEY = NAMENODE_URL_KEY;
        this.DFS_BLOCK_SIZE_KEY = DFS_BLOCK_SIZE_KEY;
    }

    @java.lang.Override
    public void init(org.apache.commons.configuration.SubsetConfiguration conf) {
        java.lang.String nameNodeUrl;
        java.lang.String blockSizeString;
        org.apache.hadoop.metrics2.sink.SqlSink.logger.info("Entering init");
        currentServiceName = getFirstConfigPrefix(conf);
        databaseUrl = conf.getString(org.apache.hadoop.metrics2.sink.SqlSink.DATABASE_URL_KEY);
        if (databaseUrl == null)
            throw new org.apache.hadoop.metrics2.MetricsException("databaseUrl required in the metrics2 configuration for SqlServerSink.");

        try {
            java.lang.Class.forName(getDatabaseDriverClassName());
        } catch (java.lang.ClassNotFoundException cnfe) {
            throw new org.apache.hadoop.metrics2.MetricsException("SqlServerSink requires the Microsoft JDBC driver for SQL Server.");
        }
        hadoopConfig = new org.apache.hadoop.conf.Configuration();
        if (hadoopConfig != null) {
            nameNodeUrl = hadoopConfig.get(NAMENODE_URL_KEY);
            if (nameNodeUrl != null) {
                java.util.regex.Matcher matcher = org.apache.hadoop.metrics2.sink.SqlSink.NAME_URL_REGEX.matcher(nameNodeUrl);
                if (matcher.find()) {
                    clusterName = matcher.group(1);
                }
            }
            blockSizeString = hadoopConfig.get(DFS_BLOCK_SIZE_KEY);
            if (blockSizeString != null) {
                try {
                    blockSize = java.lang.Integer.parseInt(blockSizeString);
                    org.apache.hadoop.metrics2.sink.SqlSink.logger.info("blockSize = " + blockSize);
                } catch (java.lang.NumberFormatException nfe) {
                    org.apache.hadoop.metrics2.sink.SqlSink.logger.warn("Exception on init: ", nfe);
                }
            }
        }
        org.apache.hadoop.metrics2.sink.SqlSink.logger.info("Exit init, cluster name = " + clusterName);
    }

    private java.lang.String getFirstConfigPrefix(org.apache.commons.configuration.SubsetConfiguration conf) {
        while (conf.getParent() instanceof org.apache.commons.configuration.SubsetConfiguration) {
            conf = ((org.apache.commons.configuration.SubsetConfiguration) (conf.getParent()));
        } 
        return conf.getPrefix();
    }

    @java.lang.Override
    public abstract void putMetrics(org.apache.hadoop.metrics2.MetricsRecord record);

    @java.lang.Override
    public void flush() {
        try {
            if (conn != null)
                conn.close();

        } catch (java.lang.Exception e) {
        }
        conn = null;
    }

    public java.lang.String getLocalNodeName() {
        if (nodeName == null) {
            try {
                nodeName = java.net.InetAddress.getLocalHost().getCanonicalHostName();
            } catch (java.lang.Exception e) {
                if (org.apache.hadoop.metrics2.sink.SqlSink.DEBUG)
                    org.apache.hadoop.metrics2.sink.SqlSink.logger.info("Error during getLocalHostName: " + e.toString());

            }
            if (nodeName == null)
                nodeName = "Unknown";

        }
        return nodeName;
    }

    public java.lang.String getClusterNodeName() {
        if (clusterName.equalsIgnoreCase("localhost"))
            return getLocalNodeName();

        try {
            return java.net.InetAddress.getByName(clusterName).getCanonicalHostName();
        } catch (java.lang.Exception e) {
            if (org.apache.hadoop.metrics2.sink.SqlSink.DEBUG)
                org.apache.hadoop.metrics2.sink.SqlSink.logger.info("Error during getClusterNodeName: " + e.toString());

        }
        return clusterName;
    }

    public java.lang.String getLocalNodeIPAddress() {
        if (nodeIPAddress == null) {
            try {
                nodeIPAddress = java.net.InetAddress.getLocalHost().getHostAddress();
            } catch (java.lang.Exception e) {
                if (org.apache.hadoop.metrics2.sink.SqlSink.DEBUG)
                    org.apache.hadoop.metrics2.sink.SqlSink.logger.info("Error during getLocalNodeIPAddress: " + e.toString());

            }
        }
        if (nodeIPAddress == null)
            nodeIPAddress = "127.0.0.1";

        return nodeIPAddress;
    }

    public java.lang.String getTagString(java.lang.Iterable<org.apache.hadoop.metrics2.MetricsTag> desiredTags) {
        if (desiredTags == null)
            return null;

        tagsListBuffer.setLength(0);
        tagsListBuffer.append("sourceName:").append(currentServiceName);
        java.lang.String separator = ",";
        for (org.apache.hadoop.metrics2.MetricsTag tag : desiredTags) {
            tagsListBuffer.append(separator);
            tagsListBuffer.append(tag.name());
            tagsListBuffer.append(":");
            tagsListBuffer.append(java.lang.String.valueOf(tag.value()));
        }
        return tagsListBuffer.toString();
    }

    public boolean ensureConnection() {
        if (conn == null) {
            try {
                if (databaseUrl != null) {
                    conn = java.sql.DriverManager.getConnection(databaseUrl);
                }
            } catch (java.lang.Exception e) {
                org.apache.hadoop.metrics2.sink.SqlSink.logger.warn("Error during getConnection: " + e.toString());
            }
        }
        return conn != null;
    }

    public long getMetricRecordID(java.lang.String recordTypeContext, java.lang.String recordTypeName, java.lang.String nodeName, java.lang.String sourceIP, java.lang.String clusterName, java.lang.String serviceName, java.lang.String tagPairs, long recordTimestamp) {
        java.sql.CallableStatement cstmt = null;
        long result;
        org.apache.hadoop.metrics2.sink.SqlSink.logger.trace((((((((((((((("Params: recordTypeContext = " + recordTypeContext) + ", recordTypeName = ") + recordTypeName) + ", nodeName = ") + nodeName) + ", sourceIP = ") + sourceIP) + ", tagPairs = ") + tagPairs) + ", clusterName = ") + clusterName) + ", serviceName = ") + serviceName) + ", recordTimestamp = ") + recordTimestamp);
        if (((((recordTypeContext == null) || (recordTypeName == null)) || (nodeName == null)) || (sourceIP == null)) || (tagPairs == null))
            return -1;

        int colid = 1;
        try {
            if (ensureConnection()) {
                java.lang.String procedureCall = java.lang.String.format("{call %s(?, ?, ?, ?, ?, ?, ?, ?, ?)}", getGetMetricsProcedureName());
                cstmt = conn.prepareCall(procedureCall);
                cstmt.setNString(colid++, recordTypeContext);
                cstmt.setNString(colid++, recordTypeName);
                cstmt.setNString(colid++, nodeName);
                cstmt.setNString(colid++, sourceIP);
                cstmt.setNString(colid++, clusterName);
                cstmt.setNString(colid++, serviceName);
                cstmt.setNString(colid++, tagPairs);
                cstmt.setLong(colid++, recordTimestamp);
                cstmt.registerOutParameter(colid, java.sql.Types.BIGINT);
                cstmt.execute();
                result = cstmt.getLong(colid);
                if (cstmt.wasNull())
                    return -1;

                return result;
            }
        } catch (java.lang.Exception e) {
            if (org.apache.hadoop.metrics2.sink.SqlSink.DEBUG)
                org.apache.hadoop.metrics2.sink.SqlSink.logger.info("Error during getMetricRecordID call sproc: " + e.toString());

            flush();
        } finally {
            if (cstmt != null) {
                try {
                    cstmt.close();
                } catch (java.sql.SQLException se) {
                    if (org.apache.hadoop.metrics2.sink.SqlSink.DEBUG)
                        org.apache.hadoop.metrics2.sink.SqlSink.logger.info("Error during getMetricRecordID close cstmt: " + se.toString());

                }
            }
        }
        return -1;
    }

    public void insertMetricValue(long metricRecordID, java.lang.String metricName, java.lang.String metricValue) {
        java.sql.CallableStatement cstmt = null;
        if (((metricRecordID < 0) || (metricName == null)) || (metricValue == null))
            return;

        try {
            org.apache.hadoop.metrics2.sink.SqlSink.logger.trace((((((((("Insert metricRecordId : " + metricRecordID) + ", ") + "metricName : ") + metricName) + ", metricValue : ") + metricValue) + ", ") + "procedure = ") + getInsertMetricsProcedureName());
            if (ensureConnection()) {
                java.lang.String procedureCall = java.lang.String.format("{call %s(?, ?, ?)}", getInsertMetricsProcedureName());
                cstmt = conn.prepareCall(procedureCall);
                cstmt.setLong(1, metricRecordID);
                cstmt.setNString(2, metricName);
                cstmt.setNString(3, metricValue);
                cstmt.execute();
            }
        } catch (java.lang.Exception e) {
            if (org.apache.hadoop.metrics2.sink.SqlSink.DEBUG)
                org.apache.hadoop.metrics2.sink.SqlSink.logger.info("Error during insertMetricValue call sproc: " + e.toString());

            flush();
        } finally {
            if (cstmt != null) {
                try {
                    cstmt.close();
                } catch (java.sql.SQLException se) {
                    if (org.apache.hadoop.metrics2.sink.SqlSink.DEBUG)
                        org.apache.hadoop.metrics2.sink.SqlSink.logger.info("Error during insertMetricValue close cstmt: " + se.toString());

                }
            }
        }
    }

    public java.lang.String getCurrentServiceName() {
        return currentServiceName;
    }

    public int getBlockSize() {
        return blockSize;
    }

    protected abstract java.lang.String getInsertMetricsProcedureName();

    protected abstract java.lang.String getGetMetricsProcedureName();

    protected abstract java.lang.String getDatabaseDriverClassName();
}