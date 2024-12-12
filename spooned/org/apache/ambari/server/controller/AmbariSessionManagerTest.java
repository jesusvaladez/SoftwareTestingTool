package org.apache.ambari.server.controller;
import static org.easymock.EasyMock.createMockBuilder;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
public class AmbariSessionManagerTest {
    @org.junit.Test
    public void testGetCurrentSessionId() throws java.lang.Exception {
        javax.servlet.http.HttpSession session = EasyMock.createNiceMock(javax.servlet.http.HttpSession.class);
        org.apache.ambari.server.controller.AmbariSessionManager sessionManager = EasyMock.createMockBuilder(org.apache.ambari.server.controller.AmbariSessionManager.class).addMockedMethod("getHttpSession").createMock();
        EasyMock.expect(sessionManager.getHttpSession()).andReturn(session);
        EasyMock.expect(sessionManager.getHttpSession()).andReturn(null);
        EasyMock.expect(session.getId()).andReturn("SESSION_ID").anyTimes();
        EasyMock.replay(session, sessionManager);
        org.junit.Assert.assertEquals("SESSION_ID", sessionManager.getCurrentSessionId());
        org.junit.Assert.assertNull(sessionManager.getCurrentSessionId());
        EasyMock.verify(session, sessionManager);
    }

    @org.junit.Test
    public void testGetSessionCookie() throws java.lang.Exception {
        org.eclipse.jetty.server.session.SessionHandler sessionHandler = EasyMock.createNiceMock(org.eclipse.jetty.server.session.SessionHandler.class);
        javax.servlet.SessionCookieConfig sessionCookieConfig = EasyMock.createNiceMock(javax.servlet.SessionCookieConfig.class);
        org.apache.ambari.server.controller.AmbariSessionManager ambariSessionManager = new org.apache.ambari.server.controller.AmbariSessionManager();
        ambariSessionManager.sessionHandler = sessionHandler;
        EasyMock.expect(sessionCookieConfig.getName()).andReturn("SESSION_COOKIE").anyTimes();
        EasyMock.expect(sessionHandler.getSessionCookieConfig()).andReturn(sessionCookieConfig).anyTimes();
        EasyMock.replay(sessionHandler, sessionCookieConfig);
        org.junit.Assert.assertEquals("SESSION_COOKIE", ambariSessionManager.getSessionCookie());
        EasyMock.verify(sessionHandler, sessionCookieConfig);
    }

    @org.junit.Test
    public void testSetAttribute() throws java.lang.Exception {
        javax.servlet.http.HttpSession session = EasyMock.createNiceMock(javax.servlet.http.HttpSession.class);
        org.apache.ambari.server.controller.AmbariSessionManager sessionManager = EasyMock.createMockBuilder(org.apache.ambari.server.controller.AmbariSessionManager.class).addMockedMethod("getHttpSession").createMock();
        EasyMock.expect(sessionManager.getHttpSession()).andReturn(session);
        session.setAttribute("foo", "bar");
        EasyMock.replay(session, sessionManager);
        sessionManager.setAttribute("foo", "bar");
        EasyMock.verify(session, sessionManager);
    }

    @org.junit.Test
    public void testGetAttribute() throws java.lang.Exception {
        javax.servlet.http.HttpSession session = EasyMock.createNiceMock(javax.servlet.http.HttpSession.class);
        org.apache.ambari.server.controller.AmbariSessionManager sessionManager = EasyMock.createMockBuilder(org.apache.ambari.server.controller.AmbariSessionManager.class).addMockedMethod("getHttpSession").createMock();
        EasyMock.expect(sessionManager.getHttpSession()).andReturn(session);
        EasyMock.expect(session.getAttribute("foo")).andReturn("bar");
        EasyMock.replay(session, sessionManager);
        org.junit.Assert.assertEquals("bar", sessionManager.getAttribute("foo"));
        EasyMock.verify(session, sessionManager);
    }

    @org.junit.Test
    public void testRemoveAttribute() throws java.lang.Exception {
        javax.servlet.http.HttpSession session = EasyMock.createNiceMock(javax.servlet.http.HttpSession.class);
        org.apache.ambari.server.controller.AmbariSessionManager sessionManager = EasyMock.createMockBuilder(org.apache.ambari.server.controller.AmbariSessionManager.class).addMockedMethod("getHttpSession").createMock();
        EasyMock.expect(sessionManager.getHttpSession()).andReturn(session);
        session.removeAttribute("foo");
        EasyMock.replay(session, sessionManager);
        sessionManager.removeAttribute("foo");
        EasyMock.verify(session, sessionManager);
    }
}