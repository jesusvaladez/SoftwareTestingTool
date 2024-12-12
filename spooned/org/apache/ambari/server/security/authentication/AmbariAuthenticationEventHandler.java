package org.apache.ambari.server.security.authentication;
import org.springframework.security.core.Authentication;
public interface AmbariAuthenticationEventHandler {
    void onSuccessfulAuthentication(org.apache.ambari.server.security.authentication.AmbariAuthenticationFilter filter, javax.servlet.http.HttpServletRequest servletRequest, javax.servlet.http.HttpServletResponse servletResponse, org.springframework.security.core.Authentication result);

    void onUnsuccessfulAuthentication(org.apache.ambari.server.security.authentication.AmbariAuthenticationFilter filter, javax.servlet.http.HttpServletRequest servletRequest, javax.servlet.http.HttpServletResponse servletResponse, org.apache.ambari.server.security.authentication.AmbariAuthenticationException cause);

    void beforeAttemptAuthentication(org.apache.ambari.server.security.authentication.AmbariAuthenticationFilter filter, javax.servlet.ServletRequest servletRequest, javax.servlet.ServletResponse servletResponse);
}