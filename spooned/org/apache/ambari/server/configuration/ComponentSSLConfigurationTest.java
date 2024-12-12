package org.apache.ambari.server.configuration;
public class ComponentSSLConfigurationTest {
    public static org.apache.ambari.server.configuration.ComponentSSLConfiguration getConfiguration(java.lang.String path, java.lang.String pass, java.lang.String type, boolean isSslEnabled) {
        java.util.Properties ambariProperties = new java.util.Properties();
        ambariProperties.setProperty(org.apache.ambari.server.configuration.Configuration.SSL_TRUSTSTORE_PATH.getKey(), path);
        ambariProperties.setProperty(org.apache.ambari.server.configuration.Configuration.SSL_TRUSTSTORE_PASSWORD.getKey(), pass);
        ambariProperties.setProperty(org.apache.ambari.server.configuration.Configuration.SSL_TRUSTSTORE_TYPE.getKey(), type);
        ambariProperties.setProperty(org.apache.ambari.server.configuration.Configuration.AMBARI_METRICS_HTTPS_ENABLED.getKey(), java.lang.Boolean.toString(isSslEnabled));
        org.apache.ambari.server.configuration.Configuration configuration = new org.apache.ambari.server.configuration.ComponentSSLConfigurationTest.TestConfiguration(ambariProperties);
        org.apache.ambari.server.configuration.ComponentSSLConfiguration sslConfiguration = new org.apache.ambari.server.configuration.ComponentSSLConfiguration();
        sslConfiguration.init(configuration);
        return sslConfiguration;
    }

    @org.junit.Test
    public void testGetTruststorePath() throws java.lang.Exception {
        org.apache.ambari.server.configuration.ComponentSSLConfiguration sslConfiguration = org.apache.ambari.server.configuration.ComponentSSLConfigurationTest.getConfiguration("tspath", "tspass", "tstype", true);
        org.junit.Assert.assertEquals("tspath", sslConfiguration.getTruststorePath());
    }

    @org.junit.Test
    public void testGetTruststorePassword() throws java.lang.Exception {
        org.apache.ambari.server.configuration.ComponentSSLConfiguration sslConfiguration = org.apache.ambari.server.configuration.ComponentSSLConfigurationTest.getConfiguration("tspath", "tspass", "tstype", true);
        org.junit.Assert.assertEquals("tspass", sslConfiguration.getTruststorePassword());
    }

    @org.junit.Test
    public void testGetTruststoreType() throws java.lang.Exception {
        org.apache.ambari.server.configuration.ComponentSSLConfiguration sslConfiguration = org.apache.ambari.server.configuration.ComponentSSLConfigurationTest.getConfiguration("tspath", "tspass", "tstype", true);
        org.junit.Assert.assertEquals("tstype", sslConfiguration.getTruststoreType());
    }

    @org.junit.Test
    public void testIsGangliaSSL() throws java.lang.Exception {
        org.apache.ambari.server.configuration.ComponentSSLConfiguration sslConfiguration = org.apache.ambari.server.configuration.ComponentSSLConfigurationTest.getConfiguration("tspath", "tspass", "tstype", true);
        org.junit.Assert.assertTrue(sslConfiguration.isHttpsEnabled());
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