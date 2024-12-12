package org.apache.ambari.scom;
public class SinkConnectionFactory implements org.apache.ambari.server.controller.jdbc.ConnectionFactory {
    private java.lang.String databaseUrl;

    private java.lang.String databaseDriver;

    private boolean connectionInitialized = false;

    private static org.apache.ambari.scom.SinkConnectionFactory singleton = new org.apache.ambari.scom.SinkConnectionFactory();

    protected static final java.lang.String SCOM_SINK_DB_URL = "scom.sink.db.url";

    protected static final java.lang.String SCOM_SINK_DB_DRIVER = "scom.sink.db.driver";

    protected SinkConnectionFactory() {
    }

    public void init(org.apache.ambari.server.configuration.Configuration configuration) {
        this.databaseUrl = configuration.getProperty(org.apache.ambari.scom.SinkConnectionFactory.SCOM_SINK_DB_URL);
        this.databaseDriver = configuration.getProperty(org.apache.ambari.scom.SinkConnectionFactory.SCOM_SINK_DB_DRIVER);
    }

    public static org.apache.ambari.scom.SinkConnectionFactory instance() {
        return org.apache.ambari.scom.SinkConnectionFactory.singleton;
    }

    public java.lang.String getDatabaseUrl() {
        return databaseUrl;
    }

    public java.lang.String getDatabaseDriver() {
        return databaseDriver;
    }

    @java.lang.Override
    public java.sql.Connection getConnection() throws java.sql.SQLException {
        synchronized(this) {
            if (!connectionInitialized) {
                connectionInitialized = true;
                try {
                    java.lang.Class.forName(databaseDriver);
                } catch (java.lang.ClassNotFoundException e) {
                    throw new java.sql.SQLException("Can't load the driver class.", e);
                }
            }
        }
        return java.sql.DriverManager.getConnection(databaseUrl);
    }
}