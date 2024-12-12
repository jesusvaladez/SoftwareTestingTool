package org.apache.ambari.server.security.authentication;
import org.apache.commons.lang.StringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
@org.springframework.stereotype.Component
@org.springframework.core.annotation.Order(3)
public class AmbariBasicAuthenticationFilter extends org.springframework.security.web.authentication.www.BasicAuthenticationFilter implements org.apache.ambari.server.security.authentication.AmbariAuthenticationFilter {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.security.authentication.AmbariBasicAuthenticationFilter.class);

    private final org.apache.ambari.server.security.authentication.AmbariAuthenticationEventHandler eventHandler;

    public AmbariBasicAuthenticationFilter(org.springframework.security.authentication.AuthenticationManager authenticationManager, org.apache.ambari.server.security.AmbariEntryPoint ambariEntryPoint, org.apache.ambari.server.security.authentication.AmbariAuthenticationEventHandler eventHandler) {
        super(authenticationManager, ambariEntryPoint);
        if (eventHandler == null) {
            throw new java.lang.IllegalArgumentException("The AmbariAuthenticationEventHandler must not be null");
        }
        this.eventHandler = eventHandler;
    }

    @java.lang.Override
    public boolean shouldApply(javax.servlet.http.HttpServletRequest httpServletRequest) {
        if (org.apache.ambari.server.security.authentication.AmbariBasicAuthenticationFilter.LOG.isDebugEnabled()) {
            org.apache.ambari.server.utils.RequestUtils.logRequestHeadersAndQueryParams(httpServletRequest, org.apache.ambari.server.security.authentication.AmbariBasicAuthenticationFilter.LOG);
        }
        java.lang.String header = httpServletRequest.getHeader("Authorization");
        if ((header != null) && header.startsWith("Basic ")) {
            java.lang.String doAsQueryParameterValue = org.apache.ambari.server.utils.RequestUtils.getQueryStringParameterValue(httpServletRequest, "doAs");
            if (org.apache.commons.lang.StringUtils.isEmpty(doAsQueryParameterValue)) {
                return true;
            } else {
                org.apache.ambari.server.security.authentication.AmbariBasicAuthenticationFilter.LOG.warn("The 'doAs' query parameter was provided; however, the BasicAuth header is found. " + "Ignoring the BasicAuth header hoping to negotiate Kerberos authentication.");
                return false;
            }
        } else {
            return false;
        }
    }

    @java.lang.Override
    public boolean shouldIncrementFailureCount() {
        return true;
    }

    @java.lang.Override
    public void doFilterInternal(javax.servlet.http.HttpServletRequest httpServletRequest, javax.servlet.http.HttpServletResponse httpServletResponse, javax.servlet.FilterChain filterChain) throws java.io.IOException, javax.servlet.ServletException {
        if (eventHandler != null) {
            eventHandler.beforeAttemptAuthentication(this, httpServletRequest, httpServletResponse);
        }
        super.doFilterInternal(httpServletRequest, httpServletResponse, filterChain);
    }

    @java.lang.Override
    protected void onSuccessfulAuthentication(javax.servlet.http.HttpServletRequest servletRequest, javax.servlet.http.HttpServletResponse servletResponse, org.springframework.security.core.Authentication authResult) throws java.io.IOException {
        if (eventHandler != null) {
            eventHandler.onSuccessfulAuthentication(this, servletRequest, servletResponse, authResult);
        }
    }

    @java.lang.Override
    protected void onUnsuccessfulAuthentication(javax.servlet.http.HttpServletRequest servletRequest, javax.servlet.http.HttpServletResponse servletResponse, org.springframework.security.core.AuthenticationException authException) throws java.io.IOException {
        if (eventHandler != null) {
            org.apache.ambari.server.security.authentication.AmbariAuthenticationException cause;
            if (authException instanceof org.apache.ambari.server.security.authentication.AmbariAuthenticationException) {
                cause = ((org.apache.ambari.server.security.authentication.AmbariAuthenticationException) (authException));
            } else {
                java.lang.String header = servletRequest.getHeader("Authorization");
                java.lang.String username = null;
                try {
                    username = getUsernameFromAuth(header, getCredentialsCharset(servletRequest));
                } catch (java.lang.Exception e) {
                    org.apache.ambari.server.security.authentication.AmbariBasicAuthenticationFilter.LOG.warn("Error occurred during decoding authorization header.", e);
                }
                cause = new org.apache.ambari.server.security.authentication.AmbariAuthenticationException(username, authException.getMessage(), false, authException);
            }
            eventHandler.onUnsuccessfulAuthentication(this, servletRequest, servletResponse, cause);
        }
    }

    private java.lang.String getUsernameFromAuth(java.lang.String authenticationValue, java.lang.String charSet) throws java.io.IOException {
        byte[] base64Token = authenticationValue.substring(6).getBytes("UTF-8");
        byte[] decoded;
        try {
            decoded = org.springframework.security.crypto.codec.Base64.decode(base64Token);
        } catch (java.lang.IllegalArgumentException ex) {
            throw new org.springframework.security.authentication.BadCredentialsException("Failed to decode basic authentication token");
        }
        java.lang.String token = new java.lang.String(decoded, charSet);
        int delimiter = token.indexOf(":");
        if (delimiter == (-1)) {
            throw new org.springframework.security.authentication.BadCredentialsException("Invalid basic authentication token");
        } else {
            return token.substring(0, delimiter);
        }
    }
}