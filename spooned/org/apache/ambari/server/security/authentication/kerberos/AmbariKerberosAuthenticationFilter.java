package org.apache.ambari.server.security.authentication.kerberos;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.kerberos.web.authentication.SpnegoAuthenticationProcessingFilter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
@org.springframework.stereotype.Component
@org.springframework.core.annotation.Order(2)
public class AmbariKerberosAuthenticationFilter extends org.springframework.security.kerberos.web.authentication.SpnegoAuthenticationProcessingFilter implements org.apache.ambari.server.security.authentication.AmbariAuthenticationFilter {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.security.authentication.kerberos.AmbariKerberosAuthenticationFilter.class);

    private final org.apache.ambari.server.security.authentication.AmbariAuthenticationEventHandler eventHandler;

    private final boolean kerberosAuthenticationEnabled;

    public AmbariKerberosAuthenticationFilter(org.springframework.security.authentication.AuthenticationManager authenticationManager, final org.springframework.security.web.AuthenticationEntryPoint entryPoint, org.apache.ambari.server.configuration.Configuration configuration, final org.apache.ambari.server.security.authentication.AmbariAuthenticationEventHandler eventHandler) {
        org.apache.ambari.server.security.authentication.kerberos.AmbariKerberosAuthenticationProperties kerberosAuthenticationProperties = (configuration == null) ? null : configuration.getKerberosAuthenticationProperties();
        kerberosAuthenticationEnabled = (kerberosAuthenticationProperties != null) && kerberosAuthenticationProperties.isKerberosAuthenticationEnabled();
        if (eventHandler == null) {
            throw new java.lang.IllegalArgumentException("The AmbariAuthenticationEventHandler must not be null");
        }
        this.eventHandler = eventHandler;
        setAuthenticationManager(authenticationManager);
        setAuthenticationDetailsSource(new org.apache.ambari.server.security.authentication.tproxy.TrustedProxyAuthenticationDetailsSource());
        setFailureHandler(new org.springframework.security.web.authentication.AuthenticationFailureHandler() {
            @java.lang.Override
            public void onAuthenticationFailure(javax.servlet.http.HttpServletRequest httpServletRequest, javax.servlet.http.HttpServletResponse httpServletResponse, org.springframework.security.core.AuthenticationException e) throws java.io.IOException, javax.servlet.ServletException {
                org.apache.ambari.server.security.authentication.AmbariAuthenticationException cause;
                if (e instanceof org.apache.ambari.server.security.authentication.AmbariAuthenticationException) {
                    cause = ((org.apache.ambari.server.security.authentication.AmbariAuthenticationException) (e));
                } else {
                    cause = new org.apache.ambari.server.security.authentication.AmbariAuthenticationException(null, e.getLocalizedMessage(), false, e);
                }
                eventHandler.onUnsuccessfulAuthentication(AmbariKerberosAuthenticationFilter.this, httpServletRequest, httpServletResponse, cause);
                entryPoint.commence(httpServletRequest, httpServletResponse, e);
            }
        });
        setSuccessHandler(new org.springframework.security.web.authentication.AuthenticationSuccessHandler() {
            @java.lang.Override
            public void onAuthenticationSuccess(javax.servlet.http.HttpServletRequest httpServletRequest, javax.servlet.http.HttpServletResponse httpServletResponse, org.springframework.security.core.Authentication authentication) throws java.io.IOException, javax.servlet.ServletException {
                eventHandler.onSuccessfulAuthentication(AmbariKerberosAuthenticationFilter.this, httpServletRequest, httpServletResponse, authentication);
            }
        });
    }

    @java.lang.Override
    public boolean shouldApply(javax.servlet.http.HttpServletRequest httpServletRequest) {
        if (org.apache.ambari.server.security.authentication.kerberos.AmbariKerberosAuthenticationFilter.LOG.isDebugEnabled()) {
            org.apache.ambari.server.utils.RequestUtils.logRequestHeadersAndQueryParams(httpServletRequest, org.apache.ambari.server.security.authentication.kerberos.AmbariKerberosAuthenticationFilter.LOG);
        }
        if (kerberosAuthenticationEnabled) {
            java.lang.String header = httpServletRequest.getHeader("Authorization");
            return (header != null) && (header.startsWith("Negotiate ") || header.startsWith("Kerberos "));
        } else {
            return false;
        }
    }

    @java.lang.Override
    public boolean shouldIncrementFailureCount() {
        return false;
    }

    @java.lang.Override
    public void doFilter(javax.servlet.ServletRequest servletRequest, javax.servlet.ServletResponse servletResponse, javax.servlet.FilterChain filterChain) throws java.io.IOException, javax.servlet.ServletException {
        if (eventHandler != null) {
            eventHandler.beforeAttemptAuthentication(this, servletRequest, servletResponse);
        }
        super.doFilter(servletRequest, servletResponse, filterChain);
    }
}