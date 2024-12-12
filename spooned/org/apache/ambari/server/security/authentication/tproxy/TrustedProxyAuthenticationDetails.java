package org.apache.ambari.server.security.authentication.tproxy;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
public class TrustedProxyAuthenticationDetails extends org.springframework.security.web.authentication.WebAuthenticationDetails {
    private final java.lang.String doAs;

    private final java.lang.String xForwardedContext;

    private final java.lang.String xForwardedProto;

    private final java.lang.String xForwardedHost;

    private final java.lang.String xForwardedFor;

    private final java.lang.String xForwardedPort;

    private final java.lang.String xForwardedServer;

    TrustedProxyAuthenticationDetails(javax.servlet.http.HttpServletRequest context) {
        super(context);
        this.doAs = org.apache.ambari.server.utils.RequestUtils.getQueryStringParameterValue(context, "doAs");
        this.xForwardedContext = context.getHeader("X-Forwarded-Context");
        this.xForwardedProto = context.getHeader("X-Forwarded-Proto");
        this.xForwardedHost = context.getHeader("X-Forwarded-Host");
        this.xForwardedFor = context.getHeader("X-Forwarded-For");
        this.xForwardedPort = context.getHeader("X-Forwarded-Port");
        this.xForwardedServer = context.getHeader("X-Forwarded-Server");
    }

    public java.lang.String getDoAs() {
        return doAs;
    }

    public java.lang.String getXForwardedContext() {
        return xForwardedContext;
    }

    public java.lang.String getXForwardedProto() {
        return xForwardedProto;
    }

    public java.lang.String getXForwardedHost() {
        return xForwardedHost;
    }

    public java.lang.String getXForwardedFor() {
        return xForwardedFor;
    }

    public java.lang.String getXForwardedPort() {
        return xForwardedPort;
    }

    public java.lang.String getXForwardedServer() {
        return xForwardedServer;
    }
}