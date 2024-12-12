package org.apache.ambari.server.security.authentication.jwt;
import org.apache.commons.lang.StringUtils;
import static org.apache.ambari.server.configuration.AmbariServerConfigurationKey.SSO_AUTHENTICATION_ENABLED;
import static org.apache.ambari.server.configuration.AmbariServerConfigurationKey.SSO_JWT_AUDIENCES;
import static org.apache.ambari.server.configuration.AmbariServerConfigurationKey.SSO_JWT_COOKIE_NAME;
import static org.apache.ambari.server.configuration.AmbariServerConfigurationKey.SSO_PROVIDER_CERTIFICATE;
import static org.apache.ambari.server.configuration.AmbariServerConfigurationKey.SSO_PROVIDER_ORIGINAL_URL_PARAM_NAME;
import static org.apache.ambari.server.configuration.AmbariServerConfigurationKey.SSO_PROVIDER_URL;
public class JwtAuthenticationProperties extends org.apache.ambari.server.configuration.AmbariServerConfiguration {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.security.authentication.jwt.JwtAuthenticationPropertiesProvider.class);

    private static final java.lang.String PEM_CERTIFICATE_HEADER = "-----BEGIN CERTIFICATE-----";

    private static final java.lang.String PEM_CERTIFICATE_FOOTER = "-----END CERTIFICATE-----";

    private java.security.interfaces.RSAPublicKey publicKey = null;

    JwtAuthenticationProperties(java.util.Map<java.lang.String, java.lang.String> configurationMap) {
        super(configurationMap);
    }

    @java.lang.Override
    protected org.apache.ambari.server.configuration.AmbariServerConfigurationCategory getCategory() {
        return org.apache.ambari.server.configuration.AmbariServerConfigurationCategory.SSO_CONFIGURATION;
    }

    public java.lang.String getAuthenticationProviderUrl() {
        return getValue(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.SSO_PROVIDER_URL, configurationMap);
    }

    public java.lang.String getCertification() {
        return getValue(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.SSO_PROVIDER_CERTIFICATE, configurationMap);
    }

    public java.security.interfaces.RSAPublicKey getPublicKey() {
        if (publicKey == null) {
            publicKey = createPublicKey(getCertification());
        }
        return publicKey;
    }

    void setPublicKey(java.security.interfaces.RSAPublicKey publicKey) {
        this.publicKey = publicKey;
    }

    public java.util.List<java.lang.String> getAudiences() {
        final java.lang.String audiencesString = getValue(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.SSO_JWT_AUDIENCES, configurationMap);
        final java.util.List<java.lang.String> audiences;
        if (org.apache.commons.lang.StringUtils.isNotEmpty(audiencesString)) {
            java.lang.String[] audArray = audiencesString.split(",");
            audiences = new java.util.ArrayList<>();
            java.util.Collections.addAll(audiences, audArray);
        } else {
            audiences = null;
        }
        return audiences;
    }

    public java.lang.String getCookieName() {
        return getValue(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.SSO_JWT_COOKIE_NAME, configurationMap);
    }

    public java.lang.String getOriginalUrlQueryParam() {
        return getValue(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.SSO_PROVIDER_ORIGINAL_URL_PARAM_NAME, configurationMap);
    }

    public boolean isEnabledForAmbari() {
        return java.lang.Boolean.valueOf(getValue(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.SSO_AUTHENTICATION_ENABLED, configurationMap));
    }

    private java.security.interfaces.RSAPublicKey createPublicKey(java.lang.String certificate) {
        java.security.interfaces.RSAPublicKey publicKey = null;
        if (certificate != null) {
            certificate = certificate.trim();
        }
        if (!org.apache.commons.lang.StringUtils.isEmpty(certificate)) {
            if (!certificate.startsWith(org.apache.ambari.server.security.authentication.jwt.JwtAuthenticationProperties.PEM_CERTIFICATE_HEADER)) {
                certificate = (org.apache.ambari.server.security.authentication.jwt.JwtAuthenticationProperties.PEM_CERTIFICATE_HEADER + "/n") + certificate;
            }
            if (!certificate.endsWith(org.apache.ambari.server.security.authentication.jwt.JwtAuthenticationProperties.PEM_CERTIFICATE_FOOTER)) {
                certificate = (certificate + "/n") + org.apache.ambari.server.security.authentication.jwt.JwtAuthenticationProperties.PEM_CERTIFICATE_FOOTER;
            }
            try {
                publicKey = org.apache.ambari.server.security.encryption.CertificateUtils.getPublicKeyFromString(certificate);
            } catch (java.security.cert.CertificateException | java.io.UnsupportedEncodingException e) {
                org.apache.ambari.server.security.authentication.jwt.JwtAuthenticationProperties.LOG.error("Unable to parse public certificate file. JTW authentication will fail.", e);
            }
        }
        return publicKey;
    }

    @java.lang.Override
    public java.util.Map<java.lang.String, java.lang.String> toMap() {
        return configurationMap;
    }
}