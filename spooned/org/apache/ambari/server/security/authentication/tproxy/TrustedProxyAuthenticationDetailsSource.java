package org.apache.ambari.server.security.authentication.tproxy;
import org.springframework.security.authentication.AuthenticationDetailsSource;
public class TrustedProxyAuthenticationDetailsSource implements org.springframework.security.authentication.AuthenticationDetailsSource<javax.servlet.http.HttpServletRequest, org.apache.ambari.server.security.authentication.tproxy.TrustedProxyAuthenticationDetails> {
    @java.lang.Override
    public org.apache.ambari.server.security.authentication.tproxy.TrustedProxyAuthenticationDetails buildDetails(javax.servlet.http.HttpServletRequest httpServletRequest) {
        return new org.apache.ambari.server.security.authentication.tproxy.TrustedProxyAuthenticationDetails(httpServletRequest);
    }
}