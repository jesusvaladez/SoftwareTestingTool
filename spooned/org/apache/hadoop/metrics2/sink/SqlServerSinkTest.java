package org.apache.hadoop.metrics2.sink;
import org.apache.commons.configuration.SubsetConfiguration;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
public abstract class SqlServerSinkTest<T extends org.apache.hadoop.metrics2.sink.SqlServerSink> {
    public abstract T createInstance() throws java.lang.InstantiationException, java.lang.IllegalAccessException;

    @org.junit.Test
    public void testInit() throws java.lang.Exception {
        org.apache.commons.configuration.SubsetConfiguration configuration = org.apache.hadoop.metrics2.sink.SqlServerSinkTest.createNiceMock(org.apache.commons.configuration.SubsetConfiguration.class);
        EasyMock.expect(configuration.getParent()).andReturn(null);
        EasyMock.expect(configuration.getPrefix()).andReturn("prefix");
        EasyMock.expect(configuration.getString("databaseUrl")).andReturn("url");
        EasyMock.replay(configuration);
        org.apache.hadoop.metrics2.sink.SqlServerSink sink = createInstance();
        sink.init(configuration);
        EasyMock.verify(configuration);
    }

    @org.junit.Test
    public void testEnsureConnection() throws java.lang.Exception {
        org.apache.commons.configuration.SubsetConfiguration configuration = org.apache.hadoop.metrics2.sink.SqlServerSinkTest.createNiceMock(org.apache.commons.configuration.SubsetConfiguration.class);
        java.sql.Connection connection = org.apache.hadoop.metrics2.sink.SqlServerSinkTest.createNiceMock(java.sql.Connection.class);
        EasyMock.expect(configuration.getParent()).andReturn(null);
        EasyMock.expect(configuration.getPrefix()).andReturn("prefix");
        EasyMock.expect(configuration.getString("databaseUrl")).andReturn("url");
        EasyMock.replay(configuration, connection);
        org.apache.hadoop.metrics2.sink.SqlServerSink sink = createInstance();
        sink.init(configuration);
        org.junit.Assert.assertTrue(sink.ensureConnection());
        com.microsoft.sqlserver.jdbc.SQLServerDriver.setConnection(connection);
        org.junit.Assert.assertTrue(sink.ensureConnection());
        EasyMock.verify(configuration, connection);
    }

    @org.junit.Test
    public void testFlush() throws java.lang.Exception {
        org.apache.commons.configuration.SubsetConfiguration configuration = org.apache.hadoop.metrics2.sink.SqlServerSinkTest.createNiceMock(org.apache.commons.configuration.SubsetConfiguration.class);
        java.sql.Connection connection = org.apache.hadoop.metrics2.sink.SqlServerSinkTest.createNiceMock(java.sql.Connection.class);
        EasyMock.expect(configuration.getParent()).andReturn(null);
        EasyMock.expect(configuration.getPrefix()).andReturn("prefix");
        EasyMock.expect(configuration.getString("databaseUrl")).andReturn("url");
        connection.close();
        EasyMock.replay(configuration, connection);
        org.apache.hadoop.metrics2.sink.SqlServerSink sink = createInstance();
        sink.init(configuration);
        com.microsoft.sqlserver.jdbc.SQLServerDriver.setConnection(connection);
        sink.ensureConnection();
        sink.flush();
        EasyMock.verify(configuration, connection);
    }

    @org.junit.Test
    public void testGetMetricRecordID() throws java.lang.Exception {
        org.apache.commons.configuration.SubsetConfiguration configuration = org.apache.hadoop.metrics2.sink.SqlServerSinkTest.createNiceMock(org.apache.commons.configuration.SubsetConfiguration.class);
        java.sql.Connection connection = org.apache.hadoop.metrics2.sink.SqlServerSinkTest.createNiceMock(java.sql.Connection.class);
        java.sql.CallableStatement cstmt = org.apache.hadoop.metrics2.sink.SqlServerSinkTest.createNiceMock(java.sql.CallableStatement.class);
        EasyMock.expect(configuration.getParent()).andReturn(null);
        EasyMock.expect(configuration.getPrefix()).andReturn("prefix");
        EasyMock.expect(configuration.getString("databaseUrl")).andReturn("url");
        org.apache.hadoop.metrics2.sink.SqlServerSinkTest.expect(connection.prepareCall("{call dbo.uspGetMetricRecord(?, ?, ?, ?, ?, ?, ?, ?, ?)}")).andReturn(cstmt);
        cstmt.setNString(1, "context");
        cstmt.setNString(2, "typeName");
        cstmt.setNString(3, "nodeName");
        cstmt.setNString(4, "ip");
        cstmt.setNString(5, "clusterName");
        cstmt.setNString(6, "serviceName");
        cstmt.setNString(7, "tagPairs");
        cstmt.setLong(8, 9999L);
        cstmt.registerOutParameter(9, java.sql.Types.BIGINT);
        org.apache.hadoop.metrics2.sink.SqlServerSinkTest.expect(cstmt.execute()).andReturn(true);
        org.apache.hadoop.metrics2.sink.SqlServerSinkTest.expect(cstmt.getLong(9)).andReturn(99L);
        org.apache.hadoop.metrics2.sink.SqlServerSinkTest.expect(cstmt.wasNull()).andReturn(false);
        EasyMock.replay(configuration, connection, cstmt);
        org.apache.hadoop.metrics2.sink.SqlServerSink sink = createInstance();
        sink.init(configuration);
        com.microsoft.sqlserver.jdbc.SQLServerDriver.setConnection(connection);
        org.junit.Assert.assertEquals(99, sink.getMetricRecordID("context", "typeName", "nodeName", "ip", "clusterName", "serviceName", "tagPairs", 9999L));
        EasyMock.verify(configuration, connection, cstmt);
    }

    @org.junit.Test
    public void testGetMetricRecordID_nullReturn() throws java.lang.Exception {
        org.apache.commons.configuration.SubsetConfiguration configuration = org.apache.hadoop.metrics2.sink.SqlServerSinkTest.createNiceMock(org.apache.commons.configuration.SubsetConfiguration.class);
        java.sql.Connection connection = org.apache.hadoop.metrics2.sink.SqlServerSinkTest.createNiceMock(java.sql.Connection.class);
        java.sql.CallableStatement cstmt = org.apache.hadoop.metrics2.sink.SqlServerSinkTest.createNiceMock(java.sql.CallableStatement.class);
        EasyMock.expect(configuration.getParent()).andReturn(null);
        EasyMock.expect(configuration.getPrefix()).andReturn("prefix");
        EasyMock.expect(configuration.getString("databaseUrl")).andReturn("url");
        org.apache.hadoop.metrics2.sink.SqlServerSinkTest.expect(connection.prepareCall("{call dbo.uspGetMetricRecord(?, ?, ?, ?, ?, ?, ?, ?, ?)}")).andReturn(cstmt);
        cstmt.setNString(1, "context");
        cstmt.setNString(2, "typeName");
        cstmt.setNString(3, "nodeName");
        cstmt.setNString(4, "ip");
        cstmt.setNString(5, "clusterName");
        cstmt.setNString(6, "serviceName");
        cstmt.setNString(7, "tagPairs");
        cstmt.setLong(8, 9999L);
        cstmt.registerOutParameter(9, java.sql.Types.BIGINT);
        org.apache.hadoop.metrics2.sink.SqlServerSinkTest.expect(cstmt.execute()).andReturn(true);
        org.apache.hadoop.metrics2.sink.SqlServerSinkTest.expect(cstmt.getLong(9)).andReturn(99L);
        org.apache.hadoop.metrics2.sink.SqlServerSinkTest.expect(cstmt.wasNull()).andReturn(true);
        EasyMock.replay(configuration, connection, cstmt);
        org.apache.hadoop.metrics2.sink.SqlServerSink sink = createInstance();
        sink.init(configuration);
        com.microsoft.sqlserver.jdbc.SQLServerDriver.setConnection(connection);
        org.junit.Assert.assertEquals(-1, sink.getMetricRecordID("context", "typeName", "nodeName", "ip", "clusterName", "serviceName", "tagPairs", 9999L));
        EasyMock.verify(configuration, connection, cstmt);
    }

    @org.junit.Test
    public void testInsertMetricValue() throws java.lang.Exception {
        org.apache.commons.configuration.SubsetConfiguration configuration = org.apache.hadoop.metrics2.sink.SqlServerSinkTest.createNiceMock(org.apache.commons.configuration.SubsetConfiguration.class);
        java.sql.Connection connection = org.apache.hadoop.metrics2.sink.SqlServerSinkTest.createNiceMock(java.sql.Connection.class);
        java.sql.CallableStatement cstmt = org.apache.hadoop.metrics2.sink.SqlServerSinkTest.createNiceMock(java.sql.CallableStatement.class);
        EasyMock.expect(configuration.getParent()).andReturn(null);
        EasyMock.expect(configuration.getPrefix()).andReturn("prefix");
        EasyMock.expect(configuration.getString("databaseUrl")).andReturn("url");
        org.apache.hadoop.metrics2.sink.SqlServerSinkTest.expect(connection.prepareCall("{call dbo.uspInsertMetricValue(?, ?, ?)}")).andReturn(cstmt);
        cstmt.setLong(1, 9999L);
        cstmt.setNString(2, "metricName");
        cstmt.setNString(3, "metricValue");
        org.apache.hadoop.metrics2.sink.SqlServerSinkTest.expect(cstmt.execute()).andReturn(true);
        EasyMock.replay(configuration, connection, cstmt);
        org.apache.hadoop.metrics2.sink.SqlServerSink sink = createInstance();
        sink.init(configuration);
        com.microsoft.sqlserver.jdbc.SQLServerDriver.setConnection(connection);
        sink.insertMetricValue(9999L, "metricName", "metricValue");
        EasyMock.verify(configuration, connection, cstmt);
    }

    public abstract void testPutMetrics() throws java.lang.Exception;
}