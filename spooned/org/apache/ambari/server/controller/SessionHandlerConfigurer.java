package org.apache.ambari.server.controller;
@com.google.inject.Singleton
public class SessionHandlerConfigurer {
    @com.google.inject.Inject
    org.apache.ambari.server.configuration.Configuration configuration;

    protected void configureSessionHandler(org.eclipse.jetty.server.session.SessionHandler sessionHandler) {
        sessionHandler.getSessionCookieConfig().setPath("/");
        sessionHandler.setSessionCookie("AMBARISESSIONID");
        sessionHandler.getSessionCookieConfig().setHttpOnly(true);
        if (configuration.getApiSSLAuthentication()) {
            sessionHandler.getSessionCookieConfig().setSecure(true);
        }
        configureMaxInactiveInterval(sessionHandler);
    }

    protected void configureMaxInactiveInterval(org.eclipse.jetty.server.session.SessionHandler sessionHandler) {
        int sessionInactivityTimeout = configuration.getHttpSessionInactiveTimeout();
        sessionHandler.setMaxInactiveInterval(sessionInactivityTimeout);
    }
}