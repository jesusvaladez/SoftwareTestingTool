package org.apache.ambari.server.security.authorization;
import org.easymock.Capture;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.newCapture;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
public class AmbariUserAuthenticationFilterTest {
    private static final java.lang.String TEST_INTERNAL_TOKEN = "test token";

    private static final java.lang.String TEST_USER_ID_HEADER = "1";

    private static final java.lang.String TEST_USER_NAME = "userName";

    private static final int TEST_USER_ID = 1;

    @org.junit.Before
    public void setUp() throws java.lang.Exception {
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(null);
    }

    @org.junit.Test
    public void testDoFilterValid() throws java.io.IOException, javax.servlet.ServletException {
        final org.apache.ambari.server.security.authorization.Users users = EasyMock.createMock(org.apache.ambari.server.security.authorization.Users.class);
        javax.servlet.http.HttpServletRequest request = EasyMock.createMock(javax.servlet.http.HttpServletRequest.class);
        javax.servlet.http.HttpServletResponse response = EasyMock.createMock(javax.servlet.http.HttpServletResponse.class);
        javax.servlet.FilterChain chain = EasyMock.createMock(javax.servlet.FilterChain.class);
        org.apache.ambari.server.security.authorization.internal.InternalTokenStorage tokenStorage = EasyMock.createMock(org.apache.ambari.server.security.authorization.internal.InternalTokenStorage.class);
        EasyMock.expect(request.getHeader(org.apache.ambari.server.security.authorization.internal.InternalTokenClientFilter.INTERNAL_TOKEN_HEADER)).andReturn(org.apache.ambari.server.security.authorization.AmbariUserAuthenticationFilterTest.TEST_INTERNAL_TOKEN);
        EasyMock.expect(tokenStorage.isValidInternalToken(org.apache.ambari.server.security.authorization.AmbariUserAuthenticationFilterTest.TEST_INTERNAL_TOKEN)).andReturn(true);
        EasyMock.expect(request.getHeader(org.apache.ambari.server.scheduler.ExecutionScheduleManager.USER_ID_HEADER)).andReturn(org.apache.ambari.server.security.authorization.AmbariUserAuthenticationFilterTest.TEST_USER_ID_HEADER);
        org.apache.ambari.server.orm.entities.UserEntity userEntity = createUserEntity();
        EasyMock.expect(users.getUserEntity(org.apache.ambari.server.security.authorization.AmbariUserAuthenticationFilterTest.TEST_USER_ID)).andReturn(userEntity);
        EasyMock.expect(users.getUserAuthorities(userEntity)).andReturn(new java.util.HashSet<org.apache.ambari.server.security.authorization.AmbariGrantedAuthority>());
        EasyMock.expect(users.getUser(userEntity)).andReturn(new org.apache.ambari.server.security.authorization.User(userEntity));
        org.easymock.Capture<java.lang.String> userHeaderValue = EasyMock.newCapture();
        response.setHeader(EasyMock.eq("User"), EasyMock.capture(userHeaderValue));
        EasyMock.expectLastCall();
        chain.doFilter(request, response);
        EasyMock.expectLastCall();
        EasyMock.replay(users, request, response, chain, tokenStorage);
        org.apache.ambari.server.security.authorization.AmbariUserAuthorizationFilter filter = new org.apache.ambari.server.security.authorization.AmbariUserAuthorizationFilter(tokenStorage, users);
        filter.doFilter(request, response, chain);
        EasyMock.verify(users, request, response, chain, tokenStorage);
        org.springframework.security.core.Authentication authentication = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        org.junit.Assert.assertNotNull(authentication);
        org.junit.Assert.assertEquals(true, authentication.isAuthenticated());
        org.junit.Assert.assertEquals(org.apache.ambari.server.security.authorization.AmbariUserAuthenticationFilterTest.TEST_USER_NAME.toLowerCase(), userHeaderValue.getValue());
    }

    @org.junit.Test
    public void testDoFilterWithoutInternalToken() throws java.io.IOException, javax.servlet.ServletException {
        final org.apache.ambari.server.security.authorization.Users users = EasyMock.createMock(org.apache.ambari.server.security.authorization.Users.class);
        javax.servlet.http.HttpServletRequest request = EasyMock.createMock(javax.servlet.http.HttpServletRequest.class);
        javax.servlet.http.HttpServletResponse response = EasyMock.createMock(javax.servlet.http.HttpServletResponse.class);
        javax.servlet.FilterChain chain = EasyMock.createMock(javax.servlet.FilterChain.class);
        org.apache.ambari.server.security.authorization.internal.InternalTokenStorage tokenStorage = EasyMock.createMock(org.apache.ambari.server.security.authorization.internal.InternalTokenStorage.class);
        EasyMock.expect(request.getHeader(org.apache.ambari.server.security.authorization.internal.InternalTokenClientFilter.INTERNAL_TOKEN_HEADER)).andReturn(null);
        chain.doFilter(request, response);
        EasyMock.expectLastCall();
        EasyMock.replay(users, request, response, chain, tokenStorage);
        org.apache.ambari.server.security.authorization.AmbariUserAuthorizationFilter filter = new org.apache.ambari.server.security.authorization.AmbariUserAuthorizationFilter(tokenStorage, users);
        filter.doFilter(request, response, chain);
        EasyMock.verify(users, request, response, chain, tokenStorage);
        org.springframework.security.core.Authentication authentication = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        org.junit.Assert.assertNull(authentication);
    }

    @org.junit.Test
    public void testDoFilterWithoutUserToken() throws java.io.IOException, javax.servlet.ServletException {
        final org.apache.ambari.server.security.authorization.Users users = EasyMock.createMock(org.apache.ambari.server.security.authorization.Users.class);
        javax.servlet.http.HttpServletRequest request = EasyMock.createMock(javax.servlet.http.HttpServletRequest.class);
        javax.servlet.http.HttpServletResponse response = EasyMock.createMock(javax.servlet.http.HttpServletResponse.class);
        javax.servlet.FilterChain chain = EasyMock.createMock(javax.servlet.FilterChain.class);
        org.apache.ambari.server.security.authorization.internal.InternalTokenStorage tokenStorage = EasyMock.createMock(org.apache.ambari.server.security.authorization.internal.InternalTokenStorage.class);
        EasyMock.expect(request.getHeader(org.apache.ambari.server.security.authorization.internal.InternalTokenClientFilter.INTERNAL_TOKEN_HEADER)).andReturn(org.apache.ambari.server.security.authorization.AmbariUserAuthenticationFilterTest.TEST_INTERNAL_TOKEN);
        EasyMock.expect(tokenStorage.isValidInternalToken(org.apache.ambari.server.security.authorization.AmbariUserAuthenticationFilterTest.TEST_INTERNAL_TOKEN)).andReturn(true);
        EasyMock.expect(request.getHeader(org.apache.ambari.server.scheduler.ExecutionScheduleManager.USER_ID_HEADER)).andReturn(null);
        chain.doFilter(request, response);
        EasyMock.expectLastCall();
        EasyMock.replay(users, request, response, chain, tokenStorage);
        org.apache.ambari.server.security.authorization.AmbariUserAuthorizationFilter filter = new org.apache.ambari.server.security.authorization.AmbariUserAuthorizationFilter(tokenStorage, users);
        filter.doFilter(request, response, chain);
        EasyMock.verify(users, request, response, chain, tokenStorage);
        org.springframework.security.core.Authentication authentication = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        org.junit.Assert.assertNull(authentication);
    }

    @org.junit.Test
    public void testDoFilterWithIncorrectUser() throws java.io.IOException, javax.servlet.ServletException {
        final org.apache.ambari.server.security.authorization.Users users = EasyMock.createMock(org.apache.ambari.server.security.authorization.Users.class);
        javax.servlet.http.HttpServletRequest request = EasyMock.createMock(javax.servlet.http.HttpServletRequest.class);
        javax.servlet.http.HttpServletResponse response = EasyMock.createMock(javax.servlet.http.HttpServletResponse.class);
        javax.servlet.FilterChain chain = EasyMock.createMock(javax.servlet.FilterChain.class);
        org.apache.ambari.server.security.authorization.internal.InternalTokenStorage tokenStorage = EasyMock.createMock(org.apache.ambari.server.security.authorization.internal.InternalTokenStorage.class);
        EasyMock.expect(request.getHeader(org.apache.ambari.server.security.authorization.internal.InternalTokenClientFilter.INTERNAL_TOKEN_HEADER)).andReturn(org.apache.ambari.server.security.authorization.AmbariUserAuthenticationFilterTest.TEST_INTERNAL_TOKEN);
        EasyMock.expect(tokenStorage.isValidInternalToken(org.apache.ambari.server.security.authorization.AmbariUserAuthenticationFilterTest.TEST_INTERNAL_TOKEN)).andReturn(true);
        EasyMock.expect(request.getHeader(org.apache.ambari.server.scheduler.ExecutionScheduleManager.USER_ID_HEADER)).andReturn(org.apache.ambari.server.security.authorization.AmbariUserAuthenticationFilterTest.TEST_USER_ID_HEADER);
        EasyMock.expect(users.getUserEntity(org.apache.ambari.server.security.authorization.AmbariUserAuthenticationFilterTest.TEST_USER_ID)).andReturn(null);
        response.sendError(javax.servlet.http.HttpServletResponse.SC_FORBIDDEN, "Authentication required");
        EasyMock.expectLastCall();
        response.flushBuffer();
        EasyMock.expectLastCall();
        EasyMock.replay(users, request, response, chain, tokenStorage);
        org.apache.ambari.server.security.authorization.AmbariUserAuthorizationFilter filter = new org.apache.ambari.server.security.authorization.AmbariUserAuthorizationFilter(tokenStorage, users);
        filter.doFilter(request, response, chain);
        EasyMock.verify(users, request, response, chain, tokenStorage);
        org.springframework.security.core.Authentication authentication = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        org.junit.Assert.assertNull(authentication);
    }

    @org.junit.Test
    public void testDoFilterWithInvalidUserID() throws java.io.IOException, javax.servlet.ServletException {
        final org.apache.ambari.server.security.authorization.Users users = EasyMock.createMock(org.apache.ambari.server.security.authorization.Users.class);
        javax.servlet.http.HttpServletRequest request = EasyMock.createMock(javax.servlet.http.HttpServletRequest.class);
        javax.servlet.http.HttpServletResponse response = EasyMock.createMock(javax.servlet.http.HttpServletResponse.class);
        javax.servlet.FilterChain chain = EasyMock.createMock(javax.servlet.FilterChain.class);
        org.apache.ambari.server.security.authorization.internal.InternalTokenStorage tokenStorage = EasyMock.createMock(org.apache.ambari.server.security.authorization.internal.InternalTokenStorage.class);
        EasyMock.expect(request.getHeader(org.apache.ambari.server.security.authorization.internal.InternalTokenClientFilter.INTERNAL_TOKEN_HEADER)).andReturn(org.apache.ambari.server.security.authorization.AmbariUserAuthenticationFilterTest.TEST_INTERNAL_TOKEN);
        EasyMock.expect(tokenStorage.isValidInternalToken(org.apache.ambari.server.security.authorization.AmbariUserAuthenticationFilterTest.TEST_INTERNAL_TOKEN)).andReturn(true);
        EasyMock.expect(request.getHeader(org.apache.ambari.server.scheduler.ExecutionScheduleManager.USER_ID_HEADER)).andReturn("admin");
        response.sendError(javax.servlet.http.HttpServletResponse.SC_FORBIDDEN, "Invalid user ID");
        EasyMock.expectLastCall();
        response.flushBuffer();
        EasyMock.expectLastCall();
        EasyMock.replay(users, request, response, chain, tokenStorage);
        org.apache.ambari.server.security.authorization.AmbariUserAuthorizationFilter filter = new org.apache.ambari.server.security.authorization.AmbariUserAuthorizationFilter(tokenStorage, users);
        filter.doFilter(request, response, chain);
        EasyMock.verify(users, request, response, chain, tokenStorage);
        org.springframework.security.core.Authentication authentication = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        org.junit.Assert.assertNull(authentication);
    }

    private org.apache.ambari.server.orm.entities.UserEntity createUserEntity() {
        org.apache.ambari.server.orm.entities.PrincipalEntity principalEntity = new org.apache.ambari.server.orm.entities.PrincipalEntity();
        org.apache.ambari.server.orm.entities.UserEntity userEntity = new org.apache.ambari.server.orm.entities.UserEntity();
        userEntity.setUserId(org.apache.ambari.server.security.authorization.AmbariUserAuthenticationFilterTest.TEST_USER_ID);
        userEntity.setUserName(org.apache.ambari.server.security.authorization.UserName.fromString(org.apache.ambari.server.security.authorization.AmbariUserAuthenticationFilterTest.TEST_USER_NAME).toString());
        userEntity.setPrincipal(principalEntity);
        return userEntity;
    }
}