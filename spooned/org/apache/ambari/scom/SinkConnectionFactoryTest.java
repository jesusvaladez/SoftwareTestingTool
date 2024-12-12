package org.apache.ambari.scom;
public class SinkConnectionFactoryTest {
    public static org.apache.ambari.scom.SinkConnectionFactory getFactory(java.lang.String url, java.lang.String driver) {
        java.util.Properties ambariProperties = new java.util.Properties();
        ambariProperties.setProperty(org.apache.ambari.scom.SinkConnectionFactory.SCOM_SINK_DB_URL, url);
        ambariProperties.setProperty(org.apache.ambari.scom.SinkConnectionFactory.SCOM_SINK_DB_DRIVER, driver);
        org.apache.ambari.server.configuration.Configuration configuration = new org.apache.ambari.scom.SinkConnectionFactoryTest.TestConfiguration(ambariProperties);
        org.apache.ambari.scom.SinkConnectionFactory connectionFactory = new org.apache.ambari.scom.SinkConnectionFactory();
        connectionFactory.init(configuration);
        return connectionFactory;
    }

    @org.junit.Test
    public void testGetDatabaseUrl() throws java.lang.Exception {
        org.apache.ambari.scom.SinkConnectionFactory factory = org.apache.ambari.scom.SinkConnectionFactoryTest.getFactory("myURL", "myDriver");
        org.junit.Assert.assertEquals("myURL", factory.getDatabaseUrl());
    }

    @org.junit.Test
    public void testGetDatabaseDriver() throws java.lang.Exception {
        org.apache.ambari.scom.SinkConnectionFactory factory = org.apache.ambari.scom.SinkConnectionFactoryTest.getFactory("myURL", "myDriver");
        org.junit.Assert.assertEquals("myDriver", factory.getDatabaseDriver());
    }

    private static class TestConfiguration extends org.apache.ambari.server.configuration.Configuration {
        private TestConfiguration(java.util.Properties properties) {
            super(properties);
        }

        @java.lang.Override
        protected void loadSSLParams() {
        }
    }
}