package org.apache.ambari.server.security;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
@org.springframework.stereotype.Component
public class AmbariEntryPoint implements org.springframework.security.web.AuthenticationEntryPoint {
    private final boolean kerberosAuthenticationEnabled;

    public AmbariEntryPoint(org.apache.ambari.server.configuration.Configuration configuration) {
        org.apache.ambari.server.security.authentication.kerberos.AmbariKerberosAuthenticationProperties kerberosAuthenticationProperties = (configuration == null) ? null : configuration.getKerberosAuthenticationProperties();
        kerberosAuthenticationEnabled = (kerberosAuthenticationProperties != null) && kerberosAuthenticationProperties.isKerberosAuthenticationEnabled();
    }

    @java.lang.Override
    public void commence(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response, org.springframework.security.core.AuthenticationException authException) throws java.io.IOException, javax.servlet.ServletException {
        if (kerberosAuthenticationEnabled) {
            response.setHeader("WWW-Authenticate", "Negotiate");
            response.sendError(javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED, "Authentication requested");
        } else {
            response.sendError(javax.servlet.http.HttpServletResponse.SC_FORBIDDEN, authException.getMessage());
        }
    }
}