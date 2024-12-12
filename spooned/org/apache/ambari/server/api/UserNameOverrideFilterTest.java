package org.apache.ambari.server.api;
import org.easymock.Capture;
import org.easymock.EasyMockRule;
import org.easymock.EasyMockSupport;
import org.easymock.Mock;
import org.easymock.MockType;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.same;
@org.junit.runner.RunWith(org.powermock.modules.junit4.PowerMockRunner.class)
@org.powermock.core.classloader.annotations.PrepareForTest(org.apache.ambari.server.security.authorization.AuthorizationHelper.class)
public class UserNameOverrideFilterTest extends org.easymock.EasyMockSupport {
    @org.junit.Rule
    public org.easymock.EasyMockRule mocks = new org.easymock.EasyMockRule(this);

    @org.easymock.Mock(type = org.easymock.MockType.NICE)
    private javax.servlet.http.HttpServletRequest userRelatedRequest;

    @org.easymock.Mock(type = org.easymock.MockType.NICE)
    private javax.servlet.ServletResponse response;

    @org.easymock.Mock(type = org.easymock.MockType.NICE)
    private javax.servlet.FilterChain filterChain;

    private org.apache.ambari.server.api.UserNameOverrideFilter filter = new org.apache.ambari.server.api.UserNameOverrideFilter();

    @org.junit.Test
    public void testGetUserNameMatcherNoUserNameInUri() throws java.lang.Exception {
        java.lang.String uri = "/aaa/bbb";
        java.util.regex.Matcher m = filter.getUserNameMatcher(uri);
        boolean isMatch = m.matches();
        org.junit.Assert.assertFalse(isMatch);
    }

    @org.junit.Test
    public void testGetUserNameMatcherNoPostInUri() throws java.lang.Exception {
        java.lang.String uri = "/aaa/users/user1@domain";
        java.util.regex.Matcher m = filter.getUserNameMatcher(uri);
        boolean isMatch = m.find();
        java.lang.String pre = (isMatch) ? m.group("pre") : null;
        java.lang.String userName = (isMatch) ? m.group("username") : null;
        java.lang.String post = (isMatch) ? m.group("post") : null;
        org.junit.Assert.assertTrue(isMatch);
        org.junit.Assert.assertEquals("/aaa/users/", pre);
        org.junit.Assert.assertEquals("user1@domain", userName);
        org.junit.Assert.assertEquals("", post);
    }

    @org.junit.Test
    public void testGetUserNameMatcherPostInUri() throws java.lang.Exception {
        java.lang.String uri = "/aaa/users/user1@domain/privileges";
        java.util.regex.Matcher m = filter.getUserNameMatcher(uri);
        boolean isMatch = m.find();
        java.lang.String pre = (isMatch) ? m.group("pre") : null;
        java.lang.String userName = (isMatch) ? m.group("username") : null;
        java.lang.String post = (isMatch) ? m.group("post") : null;
        org.junit.Assert.assertTrue(isMatch);
        org.junit.Assert.assertEquals("/aaa/users/", pre);
        org.junit.Assert.assertEquals("user1@domain", userName);
        org.junit.Assert.assertEquals("/privileges", post);
    }

    @org.junit.Test
    public void testDoFilterNoUserNameInUri() throws java.lang.Exception {
        EasyMock.expect(userRelatedRequest.getRequestURI()).andReturn("/test/test1").anyTimes();
        filterChain.doFilter(EasyMock.same(userRelatedRequest), EasyMock.same(response));
        EasyMock.expectLastCall();
        replayAll();
        filter.doFilter(userRelatedRequest, response, filterChain);
        verifyAll();
    }

    @org.junit.Test
    public void testDoFilterWithUserNameInUri() throws java.lang.Exception {
        EasyMock.expect(userRelatedRequest.getRequestURI()).andReturn("/test/users/testUserName/test1").anyTimes();
        filterChain.doFilter(EasyMock.same(userRelatedRequest), EasyMock.same(response));
        EasyMock.expectLastCall();
        replayAll();
        filter.doFilter(userRelatedRequest, response, filterChain);
        verifyAll();
    }

    @org.junit.Test
    public void testDoFilterWithLoginAliasInUri() throws java.lang.Exception {
        EasyMock.expect(userRelatedRequest.getRequestURI()).andReturn(java.lang.String.format("/test/users/%s/test1", java.net.URLEncoder.encode("testLoginAlias@testdomain.com", "UTF-8"))).anyTimes();
        org.easymock.Capture<javax.servlet.ServletRequest> requestCapture = org.easymock.Capture.newInstance();
        filterChain.doFilter(EasyMock.capture(requestCapture), EasyMock.same(response));
        EasyMock.expectLastCall();
        org.powermock.api.easymock.PowerMock.mockStatic(org.apache.ambari.server.security.authorization.AuthorizationHelper.class);
        EasyMock.expect(org.apache.ambari.server.security.authorization.AuthorizationHelper.resolveLoginAliasToUserName(EasyMock.eq("testLoginAlias@testdomain.com"))).andReturn("testuser1");
        org.powermock.api.easymock.PowerMock.replay(org.apache.ambari.server.security.authorization.AuthorizationHelper.class);
        replayAll();
        filter.doFilter(userRelatedRequest, response, filterChain);
        javax.servlet.http.HttpServletRequest updatedRequest = ((javax.servlet.http.HttpServletRequest) (requestCapture.getValue()));
        org.junit.Assert.assertEquals("testLoginAlias@testdomain.com login alias in the request Uri should be resolved to testuser1 user name !", "/test/users/testuser1/test1", updatedRequest.getRequestURI());
        org.powermock.api.easymock.PowerMock.verify(org.apache.ambari.server.security.authorization.AuthorizationHelper.class);
        verifyAll();
    }

    @org.junit.After
    public void tearDown() throws java.lang.Exception {
        resetAll();
    }
}