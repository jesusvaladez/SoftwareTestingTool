package org.apache.ambari.server.controller.jdbc;
public class TestJDBCResourceProvider extends org.apache.ambari.server.controller.jdbc.JDBCResourceProvider {
    public TestJDBCResourceProvider(org.apache.ambari.server.controller.jdbc.ConnectionFactory connectionFactory, org.apache.ambari.server.controller.spi.Resource.Type type, java.util.Set<java.lang.String> propertyIds, java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> keyPropertyIds) {
        super(connectionFactory, type, propertyIds, keyPropertyIds);
    }
}