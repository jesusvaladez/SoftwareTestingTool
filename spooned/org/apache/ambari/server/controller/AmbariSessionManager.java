package org.apache.ambari.server.controller;
public class AmbariSessionManager {
    @javax.inject.Inject
    org.eclipse.jetty.server.session.SessionHandler sessionHandler;

    public java.lang.String getCurrentSessionId() {
        javax.servlet.http.HttpSession session = getHttpSession();
        return session == null ? null : session.getId();
    }

    public java.lang.String getSessionCookie() {
        return sessionHandler.getSessionCookieConfig().getName();
    }

    public void setAttribute(java.lang.String name, java.lang.Object value) {
        javax.servlet.http.HttpSession session = getHttpSession();
        if (session != null) {
            session.setAttribute(name, value);
        }
    }

    public java.lang.Object getAttribute(java.lang.String name) {
        javax.servlet.http.HttpSession session = getHttpSession();
        if (session != null) {
            return session.getAttribute(name);
        }
        return null;
    }

    public void removeAttribute(java.lang.String name) {
        javax.servlet.http.HttpSession session = getHttpSession();
        if (session != null) {
            session.removeAttribute(name);
        }
    }

    protected javax.servlet.http.HttpSession getHttpSession() {
        org.springframework.web.context.request.RequestAttributes requestAttributes = org.springframework.web.context.request.RequestContextHolder.getRequestAttributes();
        if ((requestAttributes != null) && (requestAttributes instanceof org.springframework.web.context.request.ServletRequestAttributes)) {
            javax.servlet.http.HttpServletRequest request = ((org.springframework.web.context.request.ServletRequestAttributes) (requestAttributes)).getRequest();
            return request == null ? null : request.getSession(true);
        }
        return null;
    }
}