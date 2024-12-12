package org.apache.ambari.server.security.authentication.pam;
import org.jvnet.libpam.PAM;
import org.jvnet.libpam.PAMException;
import org.springframework.security.authentication.AuthenticationServiceException;
@javax.inject.Singleton
public class PamAuthenticationFactory {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.security.authentication.pam.PamAuthenticationFactory.class);

    public org.jvnet.libpam.PAM createInstance(org.apache.ambari.server.configuration.Configuration configuration) {
        java.lang.String pamConfig = (configuration == null) ? null : configuration.getPamConfigurationFile();
        return createInstance(pamConfig);
    }

    public org.jvnet.libpam.PAM createInstance(java.lang.String pamConfig) {
        try {
            return new org.jvnet.libpam.PAM(pamConfig);
        } catch (org.jvnet.libpam.PAMException e) {
            java.lang.String message = java.lang.String.format("Unable to Initialize PAM: %s", e.getMessage());
            org.apache.ambari.server.security.authentication.pam.PamAuthenticationFactory.LOG.error(message, e);
            throw new org.springframework.security.authentication.AuthenticationServiceException(message, e);
        }
    }
}