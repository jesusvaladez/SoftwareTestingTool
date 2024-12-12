package org.apache.ambari.server.security.authentication.kerberos;
import org.apache.commons.lang.StringUtils;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.kerberos.authentication.KerberosServiceRequestToken;
import org.springframework.security.kerberos.authentication.KerberosTicketValidation;
import org.springframework.security.kerberos.authentication.KerberosTicketValidator;
public class AmbariKerberosAuthenticationProvider implements org.springframework.security.authentication.AuthenticationProvider , org.springframework.beans.factory.InitializingBean {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.security.authentication.kerberos.AmbariKerberosAuthenticationProvider.class);

    private org.apache.ambari.server.security.authentication.kerberos.AmbariAuthToLocalUserDetailsService authToLocalUserDetailsService;

    private org.apache.ambari.server.security.authentication.kerberos.AmbariProxiedUserDetailsService proxiedUserDetailsService;

    private org.springframework.security.kerberos.authentication.KerberosTicketValidator ticketValidator;

    @javax.inject.Inject
    public AmbariKerberosAuthenticationProvider(org.apache.ambari.server.security.authentication.kerberos.AmbariAuthToLocalUserDetailsService authToLocalUserDetailsService, org.apache.ambari.server.security.authentication.kerberos.AmbariProxiedUserDetailsService proxiedUserDetailsService, org.springframework.security.kerberos.authentication.KerberosTicketValidator ticketValidator) {
        this.authToLocalUserDetailsService = authToLocalUserDetailsService;
        this.proxiedUserDetailsService = proxiedUserDetailsService;
        this.ticketValidator = ticketValidator;
    }

    @java.lang.Override
    public org.springframework.security.core.Authentication authenticate(org.springframework.security.core.Authentication authentication) throws org.springframework.security.core.AuthenticationException {
        if (authentication == null) {
            throw new org.springframework.security.authentication.BadCredentialsException("Missing credentials");
        } else if (authentication instanceof org.springframework.security.kerberos.authentication.KerberosServiceRequestToken) {
            org.springframework.security.kerberos.authentication.KerberosServiceRequestToken auth = ((org.springframework.security.kerberos.authentication.KerberosServiceRequestToken) (authentication));
            byte[] token = auth.getToken();
            org.apache.ambari.server.security.authentication.kerberos.AmbariKerberosAuthenticationProvider.LOG.debug("Validating Kerberos token");
            org.springframework.security.kerberos.authentication.KerberosTicketValidation ticketValidation = ticketValidator.validateTicket(token);
            org.apache.ambari.server.security.authentication.kerberos.AmbariKerberosAuthenticationProvider.LOG.debug("Kerberos token validated: {}", ticketValidation.username());
            java.lang.Object requestDetails = authentication.getDetails();
            org.springframework.security.core.userdetails.UserDetails userDetails;
            if (requestDetails instanceof org.apache.ambari.server.security.authentication.tproxy.TrustedProxyAuthenticationDetails) {
                org.apache.ambari.server.security.authentication.tproxy.TrustedProxyAuthenticationDetails trustedProxyAuthenticationDetails = ((org.apache.ambari.server.security.authentication.tproxy.TrustedProxyAuthenticationDetails) (requestDetails));
                java.lang.String proxiedUserName = trustedProxyAuthenticationDetails.getDoAs();
                if (org.apache.commons.lang.StringUtils.isNotEmpty(proxiedUserName)) {
                    java.lang.String localProxyUserName = authToLocalUserDetailsService.translatePrincipalName(ticketValidation.username());
                    userDetails = new org.apache.ambari.server.security.authentication.AmbariProxiedUserDetailsImpl(proxiedUserDetailsService.loadProxiedUser(proxiedUserName, localProxyUserName, trustedProxyAuthenticationDetails), new org.apache.ambari.server.security.authentication.kerberos.AmbariProxyUserKerberosDetailsImpl(ticketValidation.username(), localProxyUserName));
                } else {
                    userDetails = authToLocalUserDetailsService.loadUserByUsername(ticketValidation.username());
                }
            } else {
                userDetails = authToLocalUserDetailsService.loadUserByUsername(ticketValidation.username());
            }
            org.springframework.security.kerberos.authentication.KerberosServiceRequestToken responseAuth = new org.springframework.security.kerberos.authentication.KerberosServiceRequestToken(userDetails, ticketValidation, userDetails.getAuthorities(), token);
            responseAuth.setDetails(requestDetails);
            return responseAuth;
        } else {
            throw new org.springframework.security.authentication.BadCredentialsException(java.lang.String.format("Unexpected Authentication class: %s", authentication.getClass().getName()));
        }
    }

    @java.lang.Override
    public boolean supports(java.lang.Class<? extends java.lang.Object> auth) {
        return org.springframework.security.kerberos.authentication.KerberosServiceRequestToken.class.isAssignableFrom(auth);
    }

    @java.lang.Override
    public void afterPropertiesSet() throws java.lang.Exception {
    }

    public void setAuthToLocalUserDetailsService(org.apache.ambari.server.security.authentication.kerberos.AmbariAuthToLocalUserDetailsService authToLocalUserDetailsService) {
        this.authToLocalUserDetailsService = authToLocalUserDetailsService;
    }

    public void setProxiedUserDetailsService(org.apache.ambari.server.security.authentication.kerberos.AmbariProxiedUserDetailsService proxiedUserDetailsService) {
        this.proxiedUserDetailsService = proxiedUserDetailsService;
    }

    public void setTicketValidator(org.springframework.security.kerberos.authentication.KerberosTicketValidator ticketValidator) {
        this.ticketValidator = ticketValidator;
    }
}