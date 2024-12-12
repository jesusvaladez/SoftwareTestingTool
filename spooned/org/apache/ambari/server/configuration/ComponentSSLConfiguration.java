package org.apache.ambari.server.configuration;
public class ComponentSSLConfiguration {
    private java.lang.String truststorePath;

    private java.lang.String truststorePassword;

    private java.lang.String truststoreType;

    private boolean httpsEnabled;

    private static org.apache.ambari.server.configuration.ComponentSSLConfiguration singleton = new org.apache.ambari.server.configuration.ComponentSSLConfiguration();

    protected ComponentSSLConfiguration() {
    }

    public void init(org.apache.ambari.server.configuration.Configuration configuration) {
        truststorePath = configuration.getProperty(org.apache.ambari.server.configuration.Configuration.SSL_TRUSTSTORE_PATH.getKey());
        truststorePassword = getPassword(configuration);
        truststoreType = configuration.getProperty(org.apache.ambari.server.configuration.Configuration.SSL_TRUSTSTORE_TYPE.getKey());
        httpsEnabled = java.lang.Boolean.parseBoolean(configuration.getProperty(org.apache.ambari.server.configuration.Configuration.AMBARI_METRICS_HTTPS_ENABLED.getKey()));
    }

    public java.lang.String getTruststorePath() {
        return truststorePath;
    }

    public java.lang.String getTruststorePassword() {
        return truststorePassword;
    }

    public java.lang.String getTruststoreType() {
        return truststoreType;
    }

    public boolean isHttpsEnabled() {
        return httpsEnabled;
    }

    public static org.apache.ambari.server.configuration.ComponentSSLConfiguration instance() {
        return org.apache.ambari.server.configuration.ComponentSSLConfiguration.singleton;
    }

    private java.lang.String getPassword(org.apache.ambari.server.configuration.Configuration configuration) {
        java.lang.String rawPassword = configuration.getProperty(org.apache.ambari.server.configuration.Configuration.SSL_TRUSTSTORE_PASSWORD.getKey());
        java.lang.String password = org.apache.ambari.server.utils.PasswordUtils.getInstance().readPasswordFromStore(rawPassword, configuration);
        return password == null ? rawPassword : password;
    }
}