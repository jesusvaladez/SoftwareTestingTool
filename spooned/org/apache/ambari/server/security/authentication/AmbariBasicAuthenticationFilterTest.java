package org.apache.ambari.server.security.authentication;
import org.easymock.Capture;
import org.easymock.CaptureType;
import org.easymock.EasyMockSupport;
import org.easymock.IAnswer;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.getCurrentArguments;
import static org.easymock.EasyMock.newCapture;
public class AmbariBasicAuthenticationFilterTest extends org.easymock.EasyMockSupport {
    private org.apache.ambari.server.security.authentication.AmbariAuthenticationEventHandler eventHandler;

    private org.apache.ambari.server.security.AmbariEntryPoint entryPoint;

    private org.springframework.security.authentication.AuthenticationManager authenticationManager;

    @org.junit.Before
    public void setUp() {
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(null);
        eventHandler = createMock(org.apache.ambari.server.security.authentication.AmbariAuthenticationEventHandler.class);
        entryPoint = createMock(org.apache.ambari.server.security.AmbariEntryPoint.class);
        authenticationManager = createMock(org.springframework.security.authentication.AuthenticationManager.class);
    }

    @org.junit.Test(expected = java.lang.IllegalArgumentException.class)
    public void ensureNonNullEventHandler() {
        new org.apache.ambari.server.security.authentication.AmbariBasicAuthenticationFilter(authenticationManager, entryPoint, null);
    }

    @org.junit.Test
    public void testDoFilterSuccessful() throws java.io.IOException, javax.servlet.ServletException {
        org.easymock.Capture<? extends org.apache.ambari.server.security.authentication.AmbariAuthenticationFilter> captureFilter = EasyMock.newCapture(CaptureType.ALL);
        javax.servlet.http.HttpServletRequest request = createNiceMock(javax.servlet.http.HttpServletRequest.class);
        javax.servlet.http.HttpServletResponse response = createMock(javax.servlet.http.HttpServletResponse.class);
        javax.servlet.http.HttpSession session = createMock(javax.servlet.http.HttpSession.class);
        javax.servlet.FilterChain filterChain = createMock(javax.servlet.FilterChain.class);
        EasyMock.expect(request.getHeader("Authorization")).andReturn("Basic YWRtaW46YWRtaW4=").once();
        EasyMock.expect(request.getRemoteAddr()).andReturn("1.2.3.4").once();
        EasyMock.expect(request.getSession(false)).andReturn(session).once();
        EasyMock.expect(session.getId()).andReturn("sessionID").once();
        EasyMock.expect(authenticationManager.authenticate(EasyMock.anyObject(org.springframework.security.core.Authentication.class))).andAnswer(new org.easymock.IAnswer<org.springframework.security.core.Authentication>() {
            @java.lang.Override
            public org.springframework.security.core.Authentication answer() throws java.lang.Throwable {
                return ((org.springframework.security.core.Authentication) (EasyMock.getCurrentArguments()[0]));
            }
        }).anyTimes();
        eventHandler.beforeAttemptAuthentication(EasyMock.capture(captureFilter), EasyMock.eq(request), EasyMock.eq(response));
        EasyMock.expectLastCall().once();
        eventHandler.onSuccessfulAuthentication(EasyMock.capture(captureFilter), EasyMock.eq(request), EasyMock.eq(response), EasyMock.anyObject(org.springframework.security.core.Authentication.class));
        EasyMock.expectLastCall().once();
        filterChain.doFilter(request, response);
        EasyMock.expectLastCall().once();
        replayAll();
        org.apache.ambari.server.security.authentication.AmbariAuthenticationFilter filter = new org.apache.ambari.server.security.authentication.AmbariBasicAuthenticationFilter(authenticationManager, entryPoint, eventHandler);
        filter.doFilter(request, response, filterChain);
        verifyAll();
        java.util.List<? extends org.apache.ambari.server.security.authentication.AmbariAuthenticationFilter> capturedFilters = captureFilter.getValues();
        for (org.apache.ambari.server.security.authentication.AmbariAuthenticationFilter capturedFiltered : capturedFilters) {
            org.junit.Assert.assertSame(filter, capturedFiltered);
        }
    }

    @org.junit.Test
    public void testDoFilterUnsuccessful() throws java.io.IOException, javax.servlet.ServletException {
        org.easymock.Capture<? extends org.apache.ambari.server.security.authentication.AmbariAuthenticationFilter> captureFilter = EasyMock.newCapture(CaptureType.ALL);
        javax.servlet.http.HttpServletRequest request = createNiceMock(javax.servlet.http.HttpServletRequest.class);
        javax.servlet.http.HttpServletResponse response = createMock(javax.servlet.http.HttpServletResponse.class);
        javax.servlet.http.HttpSession session = createMock(javax.servlet.http.HttpSession.class);
        javax.servlet.FilterChain filterChain = createMock(javax.servlet.FilterChain.class);
        EasyMock.expect(request.getHeader("Authorization")).andReturn("Basic YWRtaW46YWRtaW4=").once();
        EasyMock.expect(request.getRemoteAddr()).andReturn("1.2.3.4").once();
        EasyMock.expect(request.getSession(false)).andReturn(session).once();
        EasyMock.expect(session.getId()).andReturn("sessionID").once();
        EasyMock.expect(authenticationManager.authenticate(EasyMock.anyObject(org.springframework.security.core.Authentication.class))).andThrow(new org.apache.ambari.server.security.authentication.InvalidUsernamePasswordCombinationException("user")).once();
        eventHandler.beforeAttemptAuthentication(EasyMock.capture(captureFilter), EasyMock.eq(request), EasyMock.eq(response));
        EasyMock.expectLastCall().once();
        eventHandler.onUnsuccessfulAuthentication(EasyMock.capture(captureFilter), EasyMock.eq(request), EasyMock.eq(response), EasyMock.anyObject(org.apache.ambari.server.security.authentication.AmbariAuthenticationException.class));
        EasyMock.expectLastCall().once();
        entryPoint.commence(EasyMock.eq(request), EasyMock.eq(response), EasyMock.anyObject(org.apache.ambari.server.security.authentication.AmbariAuthenticationException.class));
        EasyMock.expectLastCall().once();
        replayAll();
        org.apache.ambari.server.security.authentication.AmbariAuthenticationFilter filter = new org.apache.ambari.server.security.authentication.AmbariBasicAuthenticationFilter(authenticationManager, entryPoint, eventHandler);
        filter.doFilter(request, response, filterChain);
        verifyAll();
        java.util.List<? extends org.apache.ambari.server.security.authentication.AmbariAuthenticationFilter> capturedFilters = captureFilter.getValues();
        for (org.apache.ambari.server.security.authentication.AmbariAuthenticationFilter capturedFiltered : capturedFilters) {
            org.junit.Assert.assertSame(filter, capturedFiltered);
        }
    }
}