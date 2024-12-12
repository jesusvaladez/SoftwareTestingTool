package org.apache.hadoop.metrics2.sink;
import org.apache.commons.configuration.SubsetConfiguration;
import org.apache.hadoop.metrics2.AbstractMetric;
import org.apache.hadoop.metrics2.MetricsRecord;
import org.apache.hadoop.metrics2.MetricsTag;
import static org.easymock.EasyMock.*;
public class SqlServerSinkHadoop2Test extends org.apache.hadoop.metrics2.sink.SqlServerSinkTest<org.apache.hadoop.metrics2.sink.SqlServerSinkHadoop2> {
    @java.lang.Override
    public org.apache.hadoop.metrics2.sink.SqlServerSinkHadoop2 createInstance() throws java.lang.InstantiationException, java.lang.IllegalAccessException {
        return new org.apache.hadoop.metrics2.sink.SqlServerSinkHadoop2();
    }

    @java.lang.Override
    @org.junit.Test
    public void testPutMetrics() throws java.lang.Exception {
        org.apache.commons.configuration.SubsetConfiguration configuration = createNiceMock(org.apache.commons.configuration.SubsetConfiguration.class);
        java.sql.Connection connection = createNiceMock(java.sql.Connection.class);
        java.sql.CallableStatement cstmt = createNiceMock(java.sql.CallableStatement.class);
        org.apache.hadoop.metrics2.MetricsRecord record = createNiceMock(org.apache.hadoop.metrics2.MetricsRecord.class);
        org.apache.hadoop.metrics2.AbstractMetric metric = createNiceMock(org.apache.hadoop.metrics2.AbstractMetric.class);
        expect(configuration.getParent()).andReturn(null);
        expect(configuration.getPrefix()).andReturn("prefix");
        expect(configuration.getString("databaseUrl")).andReturn("url");
        expect(record.context()).andReturn("context");
        expect(record.name()).andReturn("typeName");
        expect(record.tags()).andReturn(new java.util.HashSet<org.apache.hadoop.metrics2.MetricsTag>());
        expect(record.timestamp()).andReturn(9999L);
        expect(record.metrics()).andReturn(java.util.Collections.singleton(metric));
        expect(metric.name()).andReturn("name").anyTimes();
        expect(metric.value()).andReturn(1234);
        expect(connection.prepareCall("{call dbo.uspGetMetricRecord(?, ?, ?, ?, ?, ?, ?, ?, ?)}")).andReturn(cstmt);
        cstmt.setNString(1, "context");
        cstmt.setNString(2, "typeName");
        cstmt.setNString(eq(3), ((java.lang.String) (anyObject())));
        cstmt.setNString(eq(4), ((java.lang.String) (anyObject())));
        cstmt.setNString(eq(5), ((java.lang.String) (anyObject())));
        cstmt.setNString(6, "prefix");
        cstmt.setNString(7, "sourceName:prefix");
        cstmt.setLong(8, 9999L);
        cstmt.registerOutParameter(9, java.sql.Types.BIGINT);
        expect(cstmt.execute()).andReturn(true);
        expect(cstmt.getLong(9)).andReturn(99L);
        expect(cstmt.wasNull()).andReturn(false);
        expect(connection.prepareCall("{call dbo.uspInsertMetricValue(?, ?, ?)}")).andReturn(cstmt);
        cstmt.setLong(1, 99L);
        cstmt.setNString(2, "name");
        cstmt.setNString(3, "1234");
        expect(cstmt.execute()).andReturn(true);
        replay(configuration, connection, cstmt, record, metric);
        org.apache.hadoop.metrics2.sink.SqlServerSink sink = createInstance();
        sink.init(configuration);
        com.microsoft.sqlserver.jdbc.SQLServerDriver.setConnection(connection);
        sink.putMetrics(record);
        verify(configuration, connection, cstmt, record, metric);
    }
}