package org.apache.ambari.server.ldap.service.ads;
import org.apache.directory.api.util.Strings;
import org.apache.directory.ldap.client.api.LdapConnectionConfig;
@javax.inject.Singleton
public class DefaultLdapConnectionConfigService implements org.apache.ambari.server.ldap.service.LdapConnectionConfigService {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.ldap.service.ads.DefaultLdapConnectionConfigService.class);

    @javax.inject.Inject
    public DefaultLdapConnectionConfigService() {
    }

    @java.lang.Override
    public org.apache.directory.ldap.client.api.LdapConnectionConfig createLdapConnectionConfig(org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration ambariLdapConfiguration) throws org.apache.ambari.server.ldap.service.AmbariLdapException {
        org.apache.ambari.server.ldap.service.ads.DefaultLdapConnectionConfigService.LOG.debug("Assembling ldap connection config based on: {}", ambariLdapConfiguration);
        org.apache.directory.ldap.client.api.LdapConnectionConfig config = new org.apache.directory.ldap.client.api.LdapConnectionConfig();
        config.setLdapHost(ambariLdapConfiguration.serverHost());
        config.setLdapPort(ambariLdapConfiguration.serverPort());
        config.setName(ambariLdapConfiguration.bindDn());
        config.setCredentials(ambariLdapConfiguration.bindPassword());
        config.setUseSsl(ambariLdapConfiguration.useSSL());
        if ("custom".equals(ambariLdapConfiguration.trustStore())) {
            org.apache.ambari.server.ldap.service.ads.DefaultLdapConnectionConfigService.LOG.info("Using custom trust manager configuration");
            config.setTrustManagers(trustManagers(ambariLdapConfiguration));
        }
        return config;
    }

    private javax.net.ssl.TrustManager[] trustManagers(org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration ambariLdapConfiguration) throws org.apache.ambari.server.ldap.service.AmbariLdapException {
        try {
            javax.net.ssl.TrustManagerFactory tmFactory = javax.net.ssl.TrustManagerFactory.getInstance(javax.net.ssl.TrustManagerFactory.getDefaultAlgorithm());
            tmFactory.init(keyStore(ambariLdapConfiguration));
            return tmFactory.getTrustManagers();
        } catch (java.lang.Exception e) {
            org.apache.ambari.server.ldap.service.ads.DefaultLdapConnectionConfigService.LOG.error("Failed to initialize trust managers", e);
            throw new org.apache.ambari.server.ldap.service.AmbariLdapException(e);
        }
    }

    private java.security.KeyStore keyStore(org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration ambariLdapConfiguration) throws org.apache.ambari.server.ldap.service.AmbariLdapException {
        if (org.apache.directory.api.util.Strings.isEmpty(ambariLdapConfiguration.trustStoreType())) {
            throw new org.apache.ambari.server.ldap.service.AmbariLdapException("Key Store Type must be specified");
        }
        if (org.apache.directory.api.util.Strings.isEmpty(ambariLdapConfiguration.trustStorePath())) {
            throw new org.apache.ambari.server.ldap.service.AmbariLdapException("Key Store Path must be specified");
        }
        try {
            java.security.KeyStore ks = java.security.KeyStore.getInstance(ambariLdapConfiguration.trustStoreType());
            java.io.FileInputStream fis = new java.io.FileInputStream(ambariLdapConfiguration.trustStorePath());
            ks.load(fis, ambariLdapConfiguration.trustStorePassword().toCharArray());
            return ks;
        } catch (java.lang.Exception e) {
            org.apache.ambari.server.ldap.service.ads.DefaultLdapConnectionConfigService.LOG.error("Failed to create keystore", e);
            throw new org.apache.ambari.server.ldap.service.AmbariLdapException(e);
        }
    }
}