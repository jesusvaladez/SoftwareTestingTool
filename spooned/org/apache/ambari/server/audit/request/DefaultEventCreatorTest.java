package org.apache.ambari.server.audit.request;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
public class DefaultEventCreatorTest {
    private org.apache.ambari.server.audit.request.eventcreator.DefaultEventCreator defaultEventCreator;

    private org.apache.ambari.server.api.services.RequestFactory requestFactory = new org.apache.ambari.server.api.services.RequestFactory();

    @org.junit.BeforeClass
    public static void beforeClass() {
        org.springframework.security.core.context.SecurityContextHolder.setContext(new org.springframework.security.core.context.SecurityContext() {
            @java.lang.Override
            public org.springframework.security.core.Authentication getAuthentication() {
                return new org.springframework.security.core.Authentication() {
                    @java.lang.Override
                    public java.util.Collection<? extends org.springframework.security.core.GrantedAuthority> getAuthorities() {
                        return null;
                    }

                    @java.lang.Override
                    public java.lang.Object getCredentials() {
                        return null;
                    }

                    @java.lang.Override
                    public java.lang.Object getDetails() {
                        return null;
                    }

                    @java.lang.Override
                    public java.lang.Object getPrincipal() {
                        return new org.springframework.security.core.userdetails.User("testuser", "password", java.util.Collections.emptyList());
                    }

                    @java.lang.Override
                    public boolean isAuthenticated() {
                        return true;
                    }

                    @java.lang.Override
                    public void setAuthenticated(boolean b) throws java.lang.IllegalArgumentException {
                    }

                    @java.lang.Override
                    public java.lang.String getName() {
                        return ((org.springframework.security.core.userdetails.User) (getPrincipal())).getUsername();
                    }
                };
            }

            @java.lang.Override
            public void setAuthentication(org.springframework.security.core.Authentication authentication) {
            }
        });
        org.apache.ambari.server.audit.request.DefaultEventCreatorTest.setHttpRequest();
    }

    private static void setHttpRequest() {
        org.springframework.web.context.request.RequestContextHolder.setRequestAttributes(new org.springframework.web.context.request.ServletRequestAttributes(new javax.servlet.http.HttpServletRequest() {
            @java.lang.Override
            public java.lang.String getAuthType() {
                return null;
            }

            @java.lang.Override
            public javax.servlet.http.Cookie[] getCookies() {
                return new javax.servlet.http.Cookie[0];
            }

            @java.lang.Override
            public long getDateHeader(java.lang.String s) {
                return 0;
            }

            @java.lang.Override
            public java.lang.String getHeader(java.lang.String s) {
                return null;
            }

            @java.lang.Override
            public java.util.Enumeration<java.lang.String> getHeaders(java.lang.String s) {
                return null;
            }

            @java.lang.Override
            public java.util.Enumeration<java.lang.String> getHeaderNames() {
                return null;
            }

            @java.lang.Override
            public int getIntHeader(java.lang.String s) {
                return 0;
            }

            @java.lang.Override
            public java.lang.String getMethod() {
                return null;
            }

            @java.lang.Override
            public java.lang.String getPathInfo() {
                return null;
            }

            @java.lang.Override
            public java.lang.String getPathTranslated() {
                return null;
            }

            @java.lang.Override
            public java.lang.String getContextPath() {
                return null;
            }

            @java.lang.Override
            public java.lang.String getQueryString() {
                return null;
            }

            @java.lang.Override
            public java.lang.String getRemoteUser() {
                return null;
            }

            @java.lang.Override
            public boolean isUserInRole(java.lang.String s) {
                return false;
            }

            @java.lang.Override
            public java.security.Principal getUserPrincipal() {
                return null;
            }

            @java.lang.Override
            public java.lang.String getRequestedSessionId() {
                return null;
            }

            @java.lang.Override
            public java.lang.String getRequestURI() {
                return null;
            }

            @java.lang.Override
            public java.lang.StringBuffer getRequestURL() {
                return null;
            }

            @java.lang.Override
            public java.lang.String getServletPath() {
                return null;
            }

            @java.lang.Override
            public javax.servlet.http.HttpSession getSession(boolean b) {
                return null;
            }

            @java.lang.Override
            public javax.servlet.http.HttpSession getSession() {
                return null;
            }

            public java.lang.String changeSessionId() {
                return null;
            }

            @java.lang.Override
            public boolean isRequestedSessionIdValid() {
                return false;
            }

            @java.lang.Override
            public boolean isRequestedSessionIdFromCookie() {
                return false;
            }

            @java.lang.Override
            public boolean isRequestedSessionIdFromURL() {
                return false;
            }

            @java.lang.Override
            public boolean isRequestedSessionIdFromUrl() {
                return false;
            }

            @java.lang.Override
            public boolean authenticate(javax.servlet.http.HttpServletResponse httpServletResponse) throws java.io.IOException, javax.servlet.ServletException {
                return false;
            }

            @java.lang.Override
            public void login(java.lang.String s, java.lang.String s1) throws javax.servlet.ServletException {
            }

            @java.lang.Override
            public void logout() throws javax.servlet.ServletException {
            }

            @java.lang.Override
            public java.util.Collection<javax.servlet.http.Part> getParts() throws java.io.IOException, javax.servlet.ServletException {
                return null;
            }

            @java.lang.Override
            public javax.servlet.http.Part getPart(java.lang.String s) throws java.io.IOException, javax.servlet.ServletException {
                return null;
            }

            public <T extends javax.servlet.http.HttpUpgradeHandler> T upgrade(java.lang.Class<T> aClass) throws java.io.IOException, javax.servlet.ServletException {
                return null;
            }

            @java.lang.Override
            public java.lang.Object getAttribute(java.lang.String s) {
                return null;
            }

            @java.lang.Override
            public java.util.Enumeration<java.lang.String> getAttributeNames() {
                return null;
            }

            @java.lang.Override
            public java.lang.String getCharacterEncoding() {
                return null;
            }

            @java.lang.Override
            public void setCharacterEncoding(java.lang.String s) throws java.io.UnsupportedEncodingException {
            }

            @java.lang.Override
            public int getContentLength() {
                return 0;
            }

            public long getContentLengthLong() {
                return 0;
            }

            @java.lang.Override
            public java.lang.String getContentType() {
                return null;
            }

            @java.lang.Override
            public javax.servlet.ServletInputStream getInputStream() throws java.io.IOException {
                return null;
            }

            @java.lang.Override
            public java.lang.String getParameter(java.lang.String s) {
                return null;
            }

            @java.lang.Override
            public java.util.Enumeration<java.lang.String> getParameterNames() {
                return null;
            }

            @java.lang.Override
            public java.lang.String[] getParameterValues(java.lang.String s) {
                return new java.lang.String[0];
            }

            @java.lang.Override
            public java.util.Map<java.lang.String, java.lang.String[]> getParameterMap() {
                return null;
            }

            @java.lang.Override
            public java.lang.String getProtocol() {
                return null;
            }

            @java.lang.Override
            public java.lang.String getScheme() {
                return null;
            }

            @java.lang.Override
            public java.lang.String getServerName() {
                return null;
            }

            @java.lang.Override
            public int getServerPort() {
                return 0;
            }

            @java.lang.Override
            public java.io.BufferedReader getReader() throws java.io.IOException {
                return null;
            }

            @java.lang.Override
            public java.lang.String getRemoteAddr() {
                return "1.2.3.4";
            }

            @java.lang.Override
            public java.lang.String getRemoteHost() {
                return null;
            }

            @java.lang.Override
            public void setAttribute(java.lang.String s, java.lang.Object o) {
            }

            @java.lang.Override
            public void removeAttribute(java.lang.String s) {
            }

            @java.lang.Override
            public java.util.Locale getLocale() {
                return null;
            }

            @java.lang.Override
            public java.util.Enumeration<java.util.Locale> getLocales() {
                return null;
            }

            @java.lang.Override
            public boolean isSecure() {
                return false;
            }

            @java.lang.Override
            public javax.servlet.RequestDispatcher getRequestDispatcher(java.lang.String s) {
                return null;
            }

            @java.lang.Override
            public java.lang.String getRealPath(java.lang.String s) {
                return null;
            }

            @java.lang.Override
            public int getRemotePort() {
                return 0;
            }

            @java.lang.Override
            public java.lang.String getLocalName() {
                return null;
            }

            @java.lang.Override
            public java.lang.String getLocalAddr() {
                return null;
            }

            @java.lang.Override
            public int getLocalPort() {
                return 0;
            }

            @java.lang.Override
            public javax.servlet.ServletContext getServletContext() {
                return null;
            }

            @java.lang.Override
            public javax.servlet.AsyncContext startAsync() throws java.lang.IllegalStateException {
                return null;
            }

            @java.lang.Override
            public javax.servlet.AsyncContext startAsync(javax.servlet.ServletRequest servletRequest, javax.servlet.ServletResponse servletResponse) throws java.lang.IllegalStateException {
                return null;
            }

            @java.lang.Override
            public boolean isAsyncStarted() {
                return false;
            }

            @java.lang.Override
            public boolean isAsyncSupported() {
                return false;
            }

            @java.lang.Override
            public javax.servlet.AsyncContext getAsyncContext() {
                return null;
            }

            @java.lang.Override
            public javax.servlet.DispatcherType getDispatcherType() {
                return null;
            }
        }));
    }

    @org.junit.Before
    public void before() {
        defaultEventCreator = new org.apache.ambari.server.audit.request.eventcreator.DefaultEventCreator();
    }

    @org.junit.Test
    public void defaultEventCreatorTest__okWithMessage() {
        org.apache.ambari.server.api.resources.ResourceInstance resource = new org.apache.ambari.server.api.query.QueryImpl(new java.util.HashMap<>(), new org.apache.ambari.server.api.resources.HostComponentResourceDefinition(), null);
        org.apache.ambari.server.api.services.Request request = requestFactory.createRequest(null, new org.apache.ambari.server.api.services.RequestBody(), new org.apache.ambari.server.api.services.LocalUriInfo("http://apache.org"), org.apache.ambari.server.api.services.Request.Type.POST, resource);
        org.apache.ambari.server.api.services.Result result = new org.apache.ambari.server.api.services.ResultImpl(new org.apache.ambari.server.api.services.ResultStatus(org.apache.ambari.server.api.services.ResultStatus.STATUS.OK, "message"));
        java.lang.String actual = defaultEventCreator.createAuditEvent(request, result).getAuditMessage();
        java.lang.String expected = "User(testuser), RemoteIp(1.2.3.4), RequestType(POST), url(http://apache.org), ResultStatus(200 OK)";
        junit.framework.Assert.assertEquals(expected, actual);
    }

    @org.junit.Test
    public void defaultEventCreatorTest__errorWithMessage() {
        org.apache.ambari.server.api.resources.ResourceInstance resource = new org.apache.ambari.server.api.query.QueryImpl(new java.util.HashMap<>(), new org.apache.ambari.server.api.resources.HostComponentResourceDefinition(), null);
        org.apache.ambari.server.api.services.Request request = requestFactory.createRequest(null, new org.apache.ambari.server.api.services.RequestBody(), new org.apache.ambari.server.api.services.LocalUriInfo("http://apache.org"), org.apache.ambari.server.api.services.Request.Type.POST, resource);
        org.apache.ambari.server.api.services.Result result = new org.apache.ambari.server.api.services.ResultImpl(new org.apache.ambari.server.api.services.ResultStatus(org.apache.ambari.server.api.services.ResultStatus.STATUS.BAD_REQUEST, "message"));
        java.lang.String actual = defaultEventCreator.createAuditEvent(request, result).getAuditMessage();
        java.lang.String expected = "User(testuser), RemoteIp(1.2.3.4), RequestType(POST), url(http://apache.org), ResultStatus(400 Bad Request), Reason(message)";
        junit.framework.Assert.assertEquals(expected, actual);
    }

    @org.junit.Test
    public void defaultEventCreatorTest__okWithoutMessage() {
        org.apache.ambari.server.api.resources.ResourceInstance resource = new org.apache.ambari.server.api.query.QueryImpl(new java.util.HashMap<>(), new org.apache.ambari.server.api.resources.HostComponentResourceDefinition(), null);
        org.apache.ambari.server.api.services.Request request = requestFactory.createRequest(null, new org.apache.ambari.server.api.services.RequestBody(), new org.apache.ambari.server.api.services.LocalUriInfo("http://apache.org"), org.apache.ambari.server.api.services.Request.Type.POST, resource);
        org.apache.ambari.server.api.services.Result result = new org.apache.ambari.server.api.services.ResultImpl(new org.apache.ambari.server.api.services.ResultStatus(org.apache.ambari.server.api.services.ResultStatus.STATUS.OK));
        java.lang.String actual = defaultEventCreator.createAuditEvent(request, result).getAuditMessage();
        java.lang.String expected = "User(testuser), RemoteIp(1.2.3.4), RequestType(POST), url(http://apache.org), ResultStatus(200 OK)";
        junit.framework.Assert.assertEquals(expected, actual);
    }
}