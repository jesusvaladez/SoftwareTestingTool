package org.apache.ambari.server.security.authentication.kerberos;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.kerberos.authentication.KerberosTicketValidation;
import org.springframework.security.kerberos.authentication.KerberosTicketValidator;
import org.springframework.security.kerberos.authentication.sun.SunJaasKerberosTicketValidator;
@org.springframework.stereotype.Component
public class AmbariKerberosTicketValidator implements org.springframework.security.kerberos.authentication.KerberosTicketValidator , org.springframework.beans.factory.InitializingBean {
    private final org.springframework.security.kerberos.authentication.sun.SunJaasKerberosTicketValidator kerberosTicketValidator;

    public AmbariKerberosTicketValidator(org.apache.ambari.server.configuration.Configuration configuration) {
        org.apache.ambari.server.security.authentication.kerberos.AmbariKerberosAuthenticationProperties properties = (configuration == null) ? null : configuration.getKerberosAuthenticationProperties();
        if ((properties != null) && properties.isKerberosAuthenticationEnabled()) {
            kerberosTicketValidator = new org.springframework.security.kerberos.authentication.sun.SunJaasKerberosTicketValidator();
            kerberosTicketValidator.setServicePrincipal(properties.getSpnegoPrincipalName());
            if (properties.getSpnegoKeytabFilePath() != null) {
                kerberosTicketValidator.setKeyTabLocation(new org.springframework.core.io.FileSystemResource(properties.getSpnegoKeytabFilePath()));
            }
        } else {
            kerberosTicketValidator = null;
        }
    }

    @java.lang.Override
    public void afterPropertiesSet() throws java.lang.Exception {
        if (kerberosTicketValidator != null) {
            kerberosTicketValidator.afterPropertiesSet();
        }
    }

    @java.lang.Override
    public org.springframework.security.kerberos.authentication.KerberosTicketValidation validateTicket(byte[] bytes) throws org.springframework.security.authentication.BadCredentialsException {
        return kerberosTicketValidator == null ? null : kerberosTicketValidator.validateTicket(bytes);
    }

    public void setDebug(boolean debug) {
        if (kerberosTicketValidator != null) {
            kerberosTicketValidator.setDebug(debug);
        }
    }
}