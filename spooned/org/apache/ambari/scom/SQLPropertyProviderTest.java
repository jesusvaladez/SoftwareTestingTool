package org.apache.ambari.scom;
import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
public class SQLPropertyProviderTest {
    private static final java.lang.String PROPERTY_ID_1 = "metrics/rpc/RpcQueueTime_avg_time";

    private static final java.lang.String PROPERTY_ID_2 = "metrics/rpc/RpcSlowResponse_num_ops";

    private static final java.lang.String CLUSTER_NAME_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "cluster_name");

    private static final java.lang.String HOST_NAME_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "host_name");

    private static final java.lang.String COMPONENT_NAME_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "component_name");

    private static final java.lang.String SERVICE_NAME_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "service_name");

    @org.junit.Test
    public void testPopulateResources() throws java.lang.Exception {
        org.apache.ambari.server.controller.jdbc.ConnectionFactory connectionFactory = EasyMock.createNiceMock(org.apache.ambari.server.controller.jdbc.ConnectionFactory.class);
        java.sql.Connection connection = EasyMock.createNiceMock(java.sql.Connection.class);
        java.sql.Statement statement = EasyMock.createNiceMock(java.sql.Statement.class);
        java.sql.ResultSet resultSet = EasyMock.createNiceMock(java.sql.ResultSet.class);
        EasyMock.expect(connectionFactory.getConnection()).andReturn(connection).once();
        EasyMock.expect(connection.createStatement()).andReturn(statement).once();
        EasyMock.expect(statement.executeQuery(EasyMock.anyObject(java.lang.String.class))).andReturn(resultSet).once();
        EasyMock.expect(resultSet.next()).andReturn(true);
        EasyMock.expect(resultSet.getLong("RecordTimeStamp")).andReturn(999990L);
        EasyMock.expect(resultSet.getNString("MetricValue")).andReturn("0");
        EasyMock.expect(resultSet.next()).andReturn(true);
        EasyMock.expect(resultSet.getLong("RecordTimeStamp")).andReturn(999991L);
        EasyMock.expect(resultSet.getNString("MetricValue")).andReturn("1");
        EasyMock.expect(resultSet.next()).andReturn(true);
        EasyMock.expect(resultSet.getLong("RecordTimeStamp")).andReturn(999992L);
        EasyMock.expect(resultSet.getNString("MetricValue")).andReturn("2");
        EasyMock.expect(resultSet.next()).andReturn(true);
        EasyMock.expect(resultSet.getLong("RecordTimeStamp")).andReturn(999993L);
        EasyMock.expect(resultSet.getNString("MetricValue")).andReturn("3");
        EasyMock.expect(resultSet.next()).andReturn(false);
        EasyMock.replay(connectionFactory, connection, statement, resultSet);
        org.apache.ambari.scom.SQLPropertyProvider provider = new org.apache.ambari.scom.SQLPropertyProvider(org.apache.ambari.server.controller.utilities.PropertyHelper.getGangliaPropertyIds(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent), new org.apache.ambari.scom.TestHostInfoProvider(), org.apache.ambari.scom.SQLPropertyProviderTest.CLUSTER_NAME_PROPERTY_ID, org.apache.ambari.scom.SQLPropertyProviderTest.HOST_NAME_PROPERTY_ID, org.apache.ambari.scom.SQLPropertyProviderTest.COMPONENT_NAME_PROPERTY_ID, org.apache.ambari.scom.SQLPropertyProviderTest.SERVICE_NAME_PROPERTY_ID, connectionFactory);
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent);
        resource.setProperty(org.apache.ambari.scom.SQLPropertyProviderTest.CLUSTER_NAME_PROPERTY_ID, "c1");
        resource.setProperty(org.apache.ambari.scom.SQLPropertyProviderTest.HOST_NAME_PROPERTY_ID, "domU-12-31-39-0E-34-E1.compute-1.internal");
        resource.setProperty(org.apache.ambari.scom.SQLPropertyProviderTest.COMPONENT_NAME_PROPERTY_ID, "DATANODE");
        resource.setProperty(org.apache.ambari.scom.SQLPropertyProviderTest.SERVICE_NAME_PROPERTY_ID, "HDFS");
        java.util.Map<java.lang.String, org.apache.ambari.server.controller.spi.TemporalInfo> temporalInfoMap = new java.util.HashMap<java.lang.String, org.apache.ambari.server.controller.spi.TemporalInfo>();
        temporalInfoMap.put(org.apache.ambari.scom.SQLPropertyProviderTest.PROPERTY_ID_1, new org.apache.ambari.server.controller.internal.TemporalInfoImpl(10L, 20L, 1L));
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(java.util.Collections.singleton(org.apache.ambari.scom.SQLPropertyProviderTest.PROPERTY_ID_1), temporalInfoMap);
        org.junit.Assert.assertEquals(1, provider.populateResources(java.util.Collections.singleton(resource), request, null).size());
        org.junit.Assert.assertTrue(resource.getPropertyValue(org.apache.ambari.scom.SQLPropertyProviderTest.PROPERTY_ID_1) instanceof java.lang.Number[][]);
        java.lang.Number[][] datapoints = ((java.lang.Number[][]) (resource.getPropertyValue(org.apache.ambari.scom.SQLPropertyProviderTest.PROPERTY_ID_1)));
        for (int i = 0; i < datapoints.length; ++i) {
            org.junit.Assert.assertEquals(((long) (i)), datapoints[i][0]);
            org.junit.Assert.assertEquals((999990L + i) / 1000, datapoints[i][1]);
        }
        EasyMock.verify(connectionFactory, connection, statement, resultSet);
    }

    @org.junit.Test
    public void testPopulateResources_temporalStartTimeOnly() throws java.lang.Exception {
        org.apache.ambari.server.controller.jdbc.ConnectionFactory connectionFactory = EasyMock.createNiceMock(org.apache.ambari.server.controller.jdbc.ConnectionFactory.class);
        java.sql.Connection connection = EasyMock.createNiceMock(java.sql.Connection.class);
        java.sql.Statement statement = EasyMock.createNiceMock(java.sql.Statement.class);
        java.sql.ResultSet resultSet = EasyMock.createNiceMock(java.sql.ResultSet.class);
        EasyMock.expect(connectionFactory.getConnection()).andReturn(connection).once();
        EasyMock.expect(connection.createStatement()).andReturn(statement).once();
        EasyMock.expect(statement.executeQuery(EasyMock.anyObject(java.lang.String.class))).andReturn(resultSet).once();
        EasyMock.expect(resultSet.next()).andReturn(true);
        EasyMock.expect(resultSet.getLong("RecordTimeStamp")).andReturn(999990L);
        EasyMock.expect(resultSet.getNString("MetricValue")).andReturn("0");
        EasyMock.expect(resultSet.next()).andReturn(true);
        EasyMock.expect(resultSet.getLong("RecordTimeStamp")).andReturn(999991L);
        EasyMock.expect(resultSet.getNString("MetricValue")).andReturn("1");
        EasyMock.expect(resultSet.next()).andReturn(true);
        EasyMock.expect(resultSet.getLong("RecordTimeStamp")).andReturn(999992L);
        EasyMock.expect(resultSet.getNString("MetricValue")).andReturn("2");
        EasyMock.expect(resultSet.next()).andReturn(true);
        EasyMock.expect(resultSet.getLong("RecordTimeStamp")).andReturn(999993L);
        EasyMock.expect(resultSet.getNString("MetricValue")).andReturn("3");
        EasyMock.expect(resultSet.next()).andReturn(false);
        EasyMock.replay(connectionFactory, connection, statement, resultSet);
        org.apache.ambari.scom.TestHostInfoProvider hostProvider = new org.apache.ambari.scom.TestHostInfoProvider();
        org.apache.ambari.scom.SQLPropertyProvider provider = new org.apache.ambari.scom.SQLPropertyProvider(org.apache.ambari.server.controller.utilities.PropertyHelper.getGangliaPropertyIds(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent), hostProvider, org.apache.ambari.scom.SQLPropertyProviderTest.CLUSTER_NAME_PROPERTY_ID, org.apache.ambari.scom.SQLPropertyProviderTest.HOST_NAME_PROPERTY_ID, org.apache.ambari.scom.SQLPropertyProviderTest.COMPONENT_NAME_PROPERTY_ID, org.apache.ambari.scom.SQLPropertyProviderTest.SERVICE_NAME_PROPERTY_ID, connectionFactory);
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent);
        resource.setProperty(org.apache.ambari.scom.SQLPropertyProviderTest.CLUSTER_NAME_PROPERTY_ID, "c1");
        resource.setProperty(org.apache.ambari.scom.SQLPropertyProviderTest.HOST_NAME_PROPERTY_ID, "domU-12-31-39-0E-34-E1.compute-1.internal");
        resource.setProperty(org.apache.ambari.scom.SQLPropertyProviderTest.COMPONENT_NAME_PROPERTY_ID, "DATANODE");
        resource.setProperty(org.apache.ambari.scom.SQLPropertyProviderTest.SERVICE_NAME_PROPERTY_ID, "HDFS");
        java.util.Map<java.lang.String, org.apache.ambari.server.controller.spi.TemporalInfo> temporalInfoMap = new java.util.HashMap<java.lang.String, org.apache.ambari.server.controller.spi.TemporalInfo>();
        temporalInfoMap.put(org.apache.ambari.scom.SQLPropertyProviderTest.PROPERTY_ID_1, new org.apache.ambari.server.controller.internal.TemporalInfoImpl(10L, -1L, -1L));
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(java.util.Collections.singleton(org.apache.ambari.scom.SQLPropertyProviderTest.PROPERTY_ID_1), temporalInfoMap);
        org.junit.Assert.assertEquals(1, provider.populateResources(java.util.Collections.singleton(resource), request, null).size());
        org.junit.Assert.assertTrue(resource.getPropertyValue(org.apache.ambari.scom.SQLPropertyProviderTest.PROPERTY_ID_1) instanceof java.lang.Number[][]);
        java.lang.Number[][] datapoints = ((java.lang.Number[][]) (resource.getPropertyValue(org.apache.ambari.scom.SQLPropertyProviderTest.PROPERTY_ID_1)));
        for (int i = 0; i < datapoints.length; ++i) {
            org.junit.Assert.assertEquals(((long) (i)), datapoints[i][0]);
            org.junit.Assert.assertEquals((999990L + i) / 1000, datapoints[i][1]);
        }
        EasyMock.verify(connectionFactory, connection, statement, resultSet);
    }

    @org.junit.Test
    public void testPopulateResources_hostNameProperty() throws java.lang.Exception {
        org.apache.ambari.server.controller.jdbc.ConnectionFactory connectionFactory = EasyMock.createNiceMock(org.apache.ambari.server.controller.jdbc.ConnectionFactory.class);
        java.sql.Connection connection = EasyMock.createNiceMock(java.sql.Connection.class);
        java.sql.Statement statement = EasyMock.createNiceMock(java.sql.Statement.class);
        java.sql.ResultSet resultSet = EasyMock.createNiceMock(java.sql.ResultSet.class);
        EasyMock.expect(connectionFactory.getConnection()).andReturn(connection).once();
        EasyMock.expect(connection.createStatement()).andReturn(statement).once();
        EasyMock.expect(statement.executeQuery(EasyMock.anyObject(java.lang.String.class))).andReturn(resultSet).once();
        EasyMock.expect(resultSet.next()).andReturn(true);
        EasyMock.expect(resultSet.getLong("RecordTimeStamp")).andReturn(999990L);
        EasyMock.expect(resultSet.getNString("MetricValue")).andReturn("0");
        EasyMock.expect(resultSet.next()).andReturn(true);
        EasyMock.expect(resultSet.getLong("RecordTimeStamp")).andReturn(999991L);
        EasyMock.expect(resultSet.getNString("MetricValue")).andReturn("1");
        EasyMock.expect(resultSet.next()).andReturn(true);
        EasyMock.expect(resultSet.getLong("RecordTimeStamp")).andReturn(999992L);
        EasyMock.expect(resultSet.getNString("MetricValue")).andReturn("2");
        EasyMock.expect(resultSet.next()).andReturn(true);
        EasyMock.expect(resultSet.getLong("RecordTimeStamp")).andReturn(999993L);
        EasyMock.expect(resultSet.getNString("MetricValue")).andReturn("3");
        EasyMock.expect(resultSet.next()).andReturn(false);
        EasyMock.replay(connectionFactory, connection, statement, resultSet);
        org.apache.ambari.scom.TestHostInfoProvider hostProvider = new org.apache.ambari.scom.TestHostInfoProvider();
        org.apache.ambari.scom.SQLPropertyProvider provider = new org.apache.ambari.scom.SQLPropertyProvider(org.apache.ambari.server.controller.utilities.PropertyHelper.getGangliaPropertyIds(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent), hostProvider, org.apache.ambari.scom.SQLPropertyProviderTest.CLUSTER_NAME_PROPERTY_ID, org.apache.ambari.scom.SQLPropertyProviderTest.HOST_NAME_PROPERTY_ID, org.apache.ambari.scom.SQLPropertyProviderTest.COMPONENT_NAME_PROPERTY_ID, org.apache.ambari.scom.SQLPropertyProviderTest.SERVICE_NAME_PROPERTY_ID, connectionFactory);
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent);
        resource.setProperty(org.apache.ambari.scom.SQLPropertyProviderTest.CLUSTER_NAME_PROPERTY_ID, "c1");
        resource.setProperty(org.apache.ambari.scom.SQLPropertyProviderTest.HOST_NAME_PROPERTY_ID, "domU-12-31-39-0E-34-E1.compute-1.internal");
        resource.setProperty(org.apache.ambari.scom.SQLPropertyProviderTest.COMPONENT_NAME_PROPERTY_ID, "DATANODE");
        resource.setProperty(org.apache.ambari.scom.SQLPropertyProviderTest.SERVICE_NAME_PROPERTY_ID, "HDFS");
        java.util.Map<java.lang.String, org.apache.ambari.server.controller.spi.TemporalInfo> temporalInfoMap = new java.util.HashMap<java.lang.String, org.apache.ambari.server.controller.spi.TemporalInfo>();
        temporalInfoMap.put(org.apache.ambari.scom.SQLPropertyProviderTest.PROPERTY_ID_1, new org.apache.ambari.server.controller.internal.TemporalInfoImpl(10L, -1L, -1L));
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(java.util.Collections.singleton(org.apache.ambari.scom.SQLPropertyProviderTest.PROPERTY_ID_1), temporalInfoMap);
        provider.populateResources(java.util.Collections.singleton(resource), request, null);
        org.junit.Assert.assertEquals("domU-12-31-39-0E-34-E1.compute-1.internal", hostProvider.getHostId());
        org.junit.Assert.assertNull(hostProvider.getClusterName());
        org.junit.Assert.assertNull(hostProvider.getComponentName());
        EasyMock.verify(connectionFactory, connection, statement, resultSet);
    }

    @org.junit.Test
    public void testPopulateResources_noHostNameProperty() throws java.lang.Exception {
        org.apache.ambari.server.controller.jdbc.ConnectionFactory connectionFactory = EasyMock.createNiceMock(org.apache.ambari.server.controller.jdbc.ConnectionFactory.class);
        java.sql.Connection connection = EasyMock.createNiceMock(java.sql.Connection.class);
        java.sql.Statement statement = EasyMock.createNiceMock(java.sql.Statement.class);
        java.sql.ResultSet resultSet = EasyMock.createNiceMock(java.sql.ResultSet.class);
        EasyMock.expect(connectionFactory.getConnection()).andReturn(connection).once();
        EasyMock.expect(connection.createStatement()).andReturn(statement).once();
        EasyMock.expect(statement.executeQuery(EasyMock.anyObject(java.lang.String.class))).andReturn(resultSet).once();
        EasyMock.expect(resultSet.next()).andReturn(true);
        EasyMock.expect(resultSet.getLong("RecordTimeStamp")).andReturn(999990L);
        EasyMock.expect(resultSet.getNString("MetricValue")).andReturn("0");
        EasyMock.expect(resultSet.next()).andReturn(true);
        EasyMock.expect(resultSet.getLong("RecordTimeStamp")).andReturn(999991L);
        EasyMock.expect(resultSet.getNString("MetricValue")).andReturn("1");
        EasyMock.expect(resultSet.next()).andReturn(true);
        EasyMock.expect(resultSet.getLong("RecordTimeStamp")).andReturn(999992L);
        EasyMock.expect(resultSet.getNString("MetricValue")).andReturn("2");
        EasyMock.expect(resultSet.next()).andReturn(true);
        EasyMock.expect(resultSet.getLong("RecordTimeStamp")).andReturn(999993L);
        EasyMock.expect(resultSet.getNString("MetricValue")).andReturn("3");
        EasyMock.expect(resultSet.next()).andReturn(false);
        EasyMock.replay(connectionFactory, connection, statement, resultSet);
        org.apache.ambari.scom.TestHostInfoProvider hostProvider = new org.apache.ambari.scom.TestHostInfoProvider();
        org.apache.ambari.scom.SQLPropertyProvider provider = new org.apache.ambari.scom.SQLPropertyProvider(org.apache.ambari.server.controller.utilities.PropertyHelper.getGangliaPropertyIds(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent), hostProvider, org.apache.ambari.scom.SQLPropertyProviderTest.CLUSTER_NAME_PROPERTY_ID, null, org.apache.ambari.scom.SQLPropertyProviderTest.COMPONENT_NAME_PROPERTY_ID, org.apache.ambari.scom.SQLPropertyProviderTest.SERVICE_NAME_PROPERTY_ID, connectionFactory);
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent);
        resource.setProperty(org.apache.ambari.scom.SQLPropertyProviderTest.CLUSTER_NAME_PROPERTY_ID, "c1");
        resource.setProperty(org.apache.ambari.scom.SQLPropertyProviderTest.COMPONENT_NAME_PROPERTY_ID, "DATANODE");
        resource.setProperty(org.apache.ambari.scom.SQLPropertyProviderTest.SERVICE_NAME_PROPERTY_ID, "HDFS");
        java.util.Map<java.lang.String, org.apache.ambari.server.controller.spi.TemporalInfo> temporalInfoMap = new java.util.HashMap<java.lang.String, org.apache.ambari.server.controller.spi.TemporalInfo>();
        temporalInfoMap.put(org.apache.ambari.scom.SQLPropertyProviderTest.PROPERTY_ID_1, new org.apache.ambari.server.controller.internal.TemporalInfoImpl(10L, -1L, -1L));
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(java.util.Collections.singleton(org.apache.ambari.scom.SQLPropertyProviderTest.PROPERTY_ID_1), temporalInfoMap);
        provider.populateResources(java.util.Collections.singleton(resource), request, null);
        org.junit.Assert.assertNull(hostProvider.getHostId());
        org.junit.Assert.assertEquals("c1", hostProvider.getClusterName());
        org.junit.Assert.assertEquals("DATANODE", hostProvider.getComponentName());
        EasyMock.verify(connectionFactory, connection, statement, resultSet);
    }

    @org.junit.Test
    public void testPopulateResources_pointInTime() throws java.lang.Exception {
        org.apache.ambari.server.controller.jdbc.ConnectionFactory connectionFactory = EasyMock.createNiceMock(org.apache.ambari.server.controller.jdbc.ConnectionFactory.class);
        java.sql.Connection connection = EasyMock.createNiceMock(java.sql.Connection.class);
        java.sql.Statement statement = EasyMock.createNiceMock(java.sql.Statement.class);
        java.sql.ResultSet resultSet = EasyMock.createNiceMock(java.sql.ResultSet.class);
        EasyMock.expect(connectionFactory.getConnection()).andReturn(connection).once();
        EasyMock.expect(connection.createStatement()).andReturn(statement).once();
        EasyMock.expect(statement.executeQuery(EasyMock.anyObject(java.lang.String.class))).andReturn(resultSet).once();
        EasyMock.expect(resultSet.next()).andReturn(true);
        EasyMock.expect(resultSet.getString("RecordTypeContext")).andReturn("rpc");
        EasyMock.expect(resultSet.getString("RecordTypeName")).andReturn("rpc");
        EasyMock.expect(resultSet.getString("TagPairs")).andReturn("");
        EasyMock.expect(resultSet.getString("MetricName")).andReturn("RpcSlowResponse_num_ops");
        EasyMock.expect(resultSet.getString("ServiceName")).andReturn("datanode");
        EasyMock.expect(resultSet.getString("NodeName")).andReturn("host1");
        EasyMock.expect(resultSet.getNString("MetricValue")).andReturn("0");
        EasyMock.expect(resultSet.getLong("RecordTimeStamp")).andReturn(999990L);
        EasyMock.expect(resultSet.next()).andReturn(true);
        EasyMock.expect(resultSet.getString("RecordTypeContext")).andReturn("rpc");
        EasyMock.expect(resultSet.getString("RecordTypeName")).andReturn("rpc");
        EasyMock.expect(resultSet.getString("TagPairs")).andReturn("");
        EasyMock.expect(resultSet.getString("MetricName")).andReturn("RpcSlowResponse_num_ops");
        EasyMock.expect(resultSet.getString("ServiceName")).andReturn("datanode");
        EasyMock.expect(resultSet.getString("NodeName")).andReturn("host1");
        EasyMock.expect(resultSet.getNString("MetricValue")).andReturn("1");
        EasyMock.expect(resultSet.getLong("RecordTimeStamp")).andReturn(999991L);
        EasyMock.expect(resultSet.next()).andReturn(true);
        EasyMock.expect(resultSet.getString("RecordTypeContext")).andReturn("rpc");
        EasyMock.expect(resultSet.getString("RecordTypeName")).andReturn("rpc");
        EasyMock.expect(resultSet.getString("TagPairs")).andReturn("");
        EasyMock.expect(resultSet.getString("MetricName")).andReturn("RpcSlowResponse_num_ops");
        EasyMock.expect(resultSet.getString("ServiceName")).andReturn("datanode");
        EasyMock.expect(resultSet.getString("NodeName")).andReturn("host1");
        EasyMock.expect(resultSet.getNString("MetricValue")).andReturn("2");
        EasyMock.expect(resultSet.getLong("RecordTimeStamp")).andReturn(999992L);
        EasyMock.expect(resultSet.next()).andReturn(true);
        EasyMock.expect(resultSet.getString("RecordTypeContext")).andReturn("rpc");
        EasyMock.expect(resultSet.getString("RecordTypeName")).andReturn("rpc");
        EasyMock.expect(resultSet.getString("TagPairs")).andReturn("");
        EasyMock.expect(resultSet.getString("MetricName")).andReturn("RpcSlowResponse_num_ops");
        EasyMock.expect(resultSet.getString("ServiceName")).andReturn("datanode");
        EasyMock.expect(resultSet.getString("NodeName")).andReturn("host1");
        EasyMock.expect(resultSet.getNString("MetricValue")).andReturn("3");
        EasyMock.expect(resultSet.getLong("RecordTimeStamp")).andReturn(999993L);
        EasyMock.expect(resultSet.next()).andReturn(false);
        EasyMock.replay(connectionFactory, connection, statement, resultSet);
        org.apache.ambari.scom.SQLPropertyProvider provider = new org.apache.ambari.scom.SQLPropertyProvider(org.apache.ambari.server.controller.utilities.PropertyHelper.getGangliaPropertyIds(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent), new org.apache.ambari.scom.TestHostInfoProvider(), org.apache.ambari.scom.SQLPropertyProviderTest.CLUSTER_NAME_PROPERTY_ID, org.apache.ambari.scom.SQLPropertyProviderTest.HOST_NAME_PROPERTY_ID, org.apache.ambari.scom.SQLPropertyProviderTest.COMPONENT_NAME_PROPERTY_ID, org.apache.ambari.scom.SQLPropertyProviderTest.SERVICE_NAME_PROPERTY_ID, connectionFactory);
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent);
        resource.setProperty(org.apache.ambari.scom.SQLPropertyProviderTest.CLUSTER_NAME_PROPERTY_ID, "c1");
        resource.setProperty(org.apache.ambari.scom.SQLPropertyProviderTest.HOST_NAME_PROPERTY_ID, "domU-12-31-39-0E-34-E1.compute-1.internal");
        resource.setProperty(org.apache.ambari.scom.SQLPropertyProviderTest.COMPONENT_NAME_PROPERTY_ID, "DATANODE");
        resource.setProperty(org.apache.ambari.scom.SQLPropertyProviderTest.SERVICE_NAME_PROPERTY_ID, "HDFS");
        java.util.Map<java.lang.String, org.apache.ambari.server.controller.spi.TemporalInfo> temporalInfoMap = new java.util.HashMap<java.lang.String, org.apache.ambari.server.controller.spi.TemporalInfo>();
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(java.util.Collections.singleton(org.apache.ambari.scom.SQLPropertyProviderTest.PROPERTY_ID_2), temporalInfoMap);
        org.junit.Assert.assertEquals(1, provider.populateResources(java.util.Collections.singleton(resource), request, null).size());
        org.junit.Assert.assertEquals(3L, resource.getPropertyValue(org.apache.ambari.scom.SQLPropertyProviderTest.PROPERTY_ID_2));
        EasyMock.verify(connectionFactory, connection, statement, resultSet);
    }

    @org.junit.Test
    public void testPopulateResources_multi() throws java.lang.Exception {
        org.apache.ambari.server.controller.jdbc.ConnectionFactory connectionFactory = EasyMock.createNiceMock(org.apache.ambari.server.controller.jdbc.ConnectionFactory.class);
        java.sql.Connection connection = EasyMock.createNiceMock(java.sql.Connection.class);
        java.sql.Statement statement = EasyMock.createNiceMock(java.sql.Statement.class);
        java.sql.ResultSet resultSet = EasyMock.createNiceMock(java.sql.ResultSet.class);
        EasyMock.expect(connectionFactory.getConnection()).andReturn(connection).once();
        EasyMock.expect(connection.createStatement()).andReturn(statement).once();
        EasyMock.expect(statement.executeQuery(EasyMock.anyObject(java.lang.String.class))).andReturn(resultSet).once();
        EasyMock.expect(resultSet.next()).andReturn(true);
        EasyMock.expect(resultSet.getString("RecordTypeContext")).andReturn("rpc");
        EasyMock.expect(resultSet.getString("RecordTypeName")).andReturn("rpc");
        EasyMock.expect(resultSet.getString("TagPairs")).andReturn("");
        EasyMock.expect(resultSet.getString("MetricName")).andReturn("RpcQueueTime_avg_time");
        EasyMock.expect(resultSet.getString("ServiceName")).andReturn("datanode");
        EasyMock.expect(resultSet.getString("NodeName")).andReturn("host1");
        EasyMock.expect(resultSet.getLong("RecordTimeStamp")).andReturn(999990L);
        EasyMock.expect(resultSet.getNString("MetricValue")).andReturn("0");
        EasyMock.expect(resultSet.next()).andReturn(true);
        EasyMock.expect(resultSet.getString("RecordTypeContext")).andReturn("rpc");
        EasyMock.expect(resultSet.getString("RecordTypeName")).andReturn("rpc");
        EasyMock.expect(resultSet.getString("TagPairs")).andReturn("");
        EasyMock.expect(resultSet.getString("MetricName")).andReturn("RpcQueueTime_avg_time");
        EasyMock.expect(resultSet.getString("ServiceName")).andReturn("datanode");
        EasyMock.expect(resultSet.getString("NodeName")).andReturn("host1");
        EasyMock.expect(resultSet.getLong("RecordTimeStamp")).andReturn(999991L);
        EasyMock.expect(resultSet.getNString("MetricValue")).andReturn("1");
        EasyMock.expect(resultSet.next()).andReturn(true);
        EasyMock.expect(resultSet.getString("RecordTypeContext")).andReturn("rpc");
        EasyMock.expect(resultSet.getString("RecordTypeName")).andReturn("rpc");
        EasyMock.expect(resultSet.getString("TagPairs")).andReturn("");
        EasyMock.expect(resultSet.getString("MetricName")).andReturn("RpcSlowResponse_num_ops");
        EasyMock.expect(resultSet.getString("ServiceName")).andReturn("datanode");
        EasyMock.expect(resultSet.getString("NodeName")).andReturn("host1");
        EasyMock.expect(resultSet.getLong("RecordTimeStamp")).andReturn(999992L);
        EasyMock.expect(resultSet.getNString("MetricValue")).andReturn("2");
        EasyMock.expect(resultSet.next()).andReturn(true);
        EasyMock.expect(resultSet.getString("RecordTypeContext")).andReturn("rpc");
        EasyMock.expect(resultSet.getString("RecordTypeName")).andReturn("rpc");
        EasyMock.expect(resultSet.getString("TagPairs")).andReturn("");
        EasyMock.expect(resultSet.getString("MetricName")).andReturn("RpcSlowResponse_num_ops");
        EasyMock.expect(resultSet.getString("ServiceName")).andReturn("datanode");
        EasyMock.expect(resultSet.getString("NodeName")).andReturn("host1");
        EasyMock.expect(resultSet.getLong("RecordTimeStamp")).andReturn(999993L);
        EasyMock.expect(resultSet.getNString("MetricValue")).andReturn("3");
        EasyMock.expect(resultSet.next()).andReturn(false);
        EasyMock.replay(connectionFactory, connection, statement, resultSet);
        org.apache.ambari.scom.SQLPropertyProvider provider = new org.apache.ambari.scom.SQLPropertyProvider(org.apache.ambari.server.controller.utilities.PropertyHelper.getGangliaPropertyIds(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent), new org.apache.ambari.scom.TestHostInfoProvider(), org.apache.ambari.scom.SQLPropertyProviderTest.CLUSTER_NAME_PROPERTY_ID, org.apache.ambari.scom.SQLPropertyProviderTest.HOST_NAME_PROPERTY_ID, org.apache.ambari.scom.SQLPropertyProviderTest.COMPONENT_NAME_PROPERTY_ID, org.apache.ambari.scom.SQLPropertyProviderTest.SERVICE_NAME_PROPERTY_ID, connectionFactory);
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent);
        resource.setProperty(org.apache.ambari.scom.SQLPropertyProviderTest.CLUSTER_NAME_PROPERTY_ID, "c1");
        resource.setProperty(org.apache.ambari.scom.SQLPropertyProviderTest.HOST_NAME_PROPERTY_ID, "domU-12-31-39-0E-34-E1.compute-1.internal");
        resource.setProperty(org.apache.ambari.scom.SQLPropertyProviderTest.COMPONENT_NAME_PROPERTY_ID, "DATANODE");
        resource.setProperty(org.apache.ambari.scom.SQLPropertyProviderTest.SERVICE_NAME_PROPERTY_ID, "HDFS");
        java.util.Map<java.lang.String, org.apache.ambari.server.controller.spi.TemporalInfo> temporalInfoMap = new java.util.HashMap<java.lang.String, org.apache.ambari.server.controller.spi.TemporalInfo>();
        temporalInfoMap.put(org.apache.ambari.scom.SQLPropertyProviderTest.PROPERTY_ID_1, new org.apache.ambari.server.controller.internal.TemporalInfoImpl(10L, 20L, 1L));
        java.util.Set<java.lang.String> propertyIds = new java.util.LinkedHashSet<java.lang.String>();
        propertyIds.add(org.apache.ambari.scom.SQLPropertyProviderTest.PROPERTY_ID_1);
        propertyIds.add(org.apache.ambari.scom.SQLPropertyProviderTest.PROPERTY_ID_2);
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(propertyIds, temporalInfoMap);
        org.junit.Assert.assertEquals(1, provider.populateResources(java.util.Collections.singleton(resource), request, null).size());
        org.junit.Assert.assertTrue(resource.getPropertyValue(org.apache.ambari.scom.SQLPropertyProviderTest.PROPERTY_ID_1) instanceof java.lang.Number[][]);
        java.lang.Number[][] datapoints = ((java.lang.Number[][]) (resource.getPropertyValue(org.apache.ambari.scom.SQLPropertyProviderTest.PROPERTY_ID_1)));
        for (int i = 0; i < datapoints.length; ++i) {
            org.junit.Assert.assertEquals(((long) (i)), datapoints[i][0]);
            org.junit.Assert.assertEquals((999990L + i) / 1000, datapoints[i][1]);
        }
        org.junit.Assert.assertEquals(3L, resource.getPropertyValue(org.apache.ambari.scom.SQLPropertyProviderTest.PROPERTY_ID_2));
        EasyMock.verify(connectionFactory, connection, statement, resultSet);
    }
}