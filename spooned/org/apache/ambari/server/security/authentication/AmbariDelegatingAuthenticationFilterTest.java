package org.apache.ambari.server.security.authentication;
import org.easymock.EasyMockSupport;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
public class AmbariDelegatingAuthenticationFilterTest extends org.easymock.EasyMockSupport {
    @org.junit.Test
    public void testInit() throws java.lang.Exception {
        javax.servlet.FilterConfig filterConfig = createMock(javax.servlet.FilterConfig.class);
        org.apache.ambari.server.security.authentication.AmbariAuthenticationFilter filter1 = createMock(org.apache.ambari.server.security.authentication.AmbariAuthenticationFilter.class);
        filter1.init(filterConfig);
        EasyMock.expectLastCall().once();
        org.apache.ambari.server.security.authentication.AmbariAuthenticationFilter filter2 = createMock(org.apache.ambari.server.security.authentication.AmbariAuthenticationFilter.class);
        filter2.init(filterConfig);
        EasyMock.expectLastCall().once();
        org.apache.ambari.server.security.authentication.AmbariAuthenticationFilter filter3 = createMock(org.apache.ambari.server.security.authentication.AmbariAuthenticationFilter.class);
        filter3.init(filterConfig);
        EasyMock.expectLastCall().once();
        replayAll();
        javax.servlet.Filter filter = new org.apache.ambari.server.security.authentication.AmbariDelegatingAuthenticationFilter(java.util.Arrays.asList(filter1, filter2, filter3));
        filter.init(filterConfig);
        verifyAll();
    }

    @org.junit.Test
    public void testDoFilterNoneApply() throws java.lang.Exception {
        javax.servlet.http.HttpServletRequest httpServletRequest = createMock(javax.servlet.http.HttpServletRequest.class);
        javax.servlet.http.HttpServletResponse httpServletResponse = createMock(javax.servlet.http.HttpServletResponse.class);
        org.apache.ambari.server.security.authentication.AmbariAuthenticationFilter filter1 = createMock(org.apache.ambari.server.security.authentication.AmbariAuthenticationFilter.class);
        EasyMock.expect(filter1.shouldApply(httpServletRequest)).andReturn(false).once();
        org.apache.ambari.server.security.authentication.AmbariAuthenticationFilter filter2 = createMock(org.apache.ambari.server.security.authentication.AmbariAuthenticationFilter.class);
        EasyMock.expect(filter2.shouldApply(httpServletRequest)).andReturn(false).once();
        org.apache.ambari.server.security.authentication.AmbariAuthenticationFilter filter3 = createMock(org.apache.ambari.server.security.authentication.AmbariAuthenticationFilter.class);
        EasyMock.expect(filter3.shouldApply(httpServletRequest)).andReturn(false).once();
        javax.servlet.FilterChain filterChain = createMock(javax.servlet.FilterChain.class);
        filterChain.doFilter(httpServletRequest, httpServletResponse);
        EasyMock.expectLastCall().once();
        replayAll();
        javax.servlet.Filter filter = new org.apache.ambari.server.security.authentication.AmbariDelegatingAuthenticationFilter(java.util.Arrays.asList(filter1, filter2, filter3));
        filter.doFilter(httpServletRequest, httpServletResponse, filterChain);
        verifyAll();
    }

    @org.junit.Test
    public void testDoFilterFirstApplies() throws java.lang.Exception {
        javax.servlet.http.HttpServletRequest httpServletRequest = createMock(javax.servlet.http.HttpServletRequest.class);
        javax.servlet.http.HttpServletResponse httpServletResponse = createMock(javax.servlet.http.HttpServletResponse.class);
        javax.servlet.FilterChain filterChain = createMock(javax.servlet.FilterChain.class);
        org.apache.ambari.server.security.authentication.AmbariAuthenticationFilter filter1 = createMock(org.apache.ambari.server.security.authentication.AmbariAuthenticationFilter.class);
        EasyMock.expect(filter1.shouldApply(httpServletRequest)).andReturn(true).once();
        filter1.doFilter(httpServletRequest, httpServletResponse, filterChain);
        EasyMock.expectLastCall().once();
        org.apache.ambari.server.security.authentication.AmbariAuthenticationFilter filter2 = createMock(org.apache.ambari.server.security.authentication.AmbariAuthenticationFilter.class);
        org.apache.ambari.server.security.authentication.AmbariAuthenticationFilter filter3 = createMock(org.apache.ambari.server.security.authentication.AmbariAuthenticationFilter.class);
        replayAll();
        javax.servlet.Filter filter = new org.apache.ambari.server.security.authentication.AmbariDelegatingAuthenticationFilter(java.util.Arrays.asList(filter1, filter2, filter3));
        filter.doFilter(httpServletRequest, httpServletResponse, filterChain);
        verifyAll();
    }

    @org.junit.Test
    public void testDoFilterLastApplies() throws java.lang.Exception {
        javax.servlet.http.HttpServletRequest httpServletRequest = createMock(javax.servlet.http.HttpServletRequest.class);
        javax.servlet.http.HttpServletResponse httpServletResponse = createMock(javax.servlet.http.HttpServletResponse.class);
        javax.servlet.FilterChain filterChain = createMock(javax.servlet.FilterChain.class);
        org.apache.ambari.server.security.authentication.AmbariAuthenticationFilter filter1 = createMock(org.apache.ambari.server.security.authentication.AmbariAuthenticationFilter.class);
        EasyMock.expect(filter1.shouldApply(httpServletRequest)).andReturn(false).once();
        org.apache.ambari.server.security.authentication.AmbariAuthenticationFilter filter2 = createMock(org.apache.ambari.server.security.authentication.AmbariAuthenticationFilter.class);
        EasyMock.expect(filter2.shouldApply(httpServletRequest)).andReturn(false).once();
        org.apache.ambari.server.security.authentication.AmbariAuthenticationFilter filter3 = createMock(org.apache.ambari.server.security.authentication.AmbariAuthenticationFilter.class);
        EasyMock.expect(filter3.shouldApply(httpServletRequest)).andReturn(true).once();
        filter3.doFilter(httpServletRequest, httpServletResponse, filterChain);
        EasyMock.expectLastCall().once();
        replayAll();
        javax.servlet.Filter filter = new org.apache.ambari.server.security.authentication.AmbariDelegatingAuthenticationFilter(java.util.Arrays.asList(filter1, filter2, filter3));
        filter.doFilter(httpServletRequest, httpServletResponse, filterChain);
        verifyAll();
    }

    @org.junit.Test
    public void testDoFilterNthApplies() throws java.lang.Exception {
        javax.servlet.http.HttpServletRequest httpServletRequest = createMock(javax.servlet.http.HttpServletRequest.class);
        javax.servlet.http.HttpServletResponse httpServletResponse = createMock(javax.servlet.http.HttpServletResponse.class);
        javax.servlet.FilterChain filterChain = createMock(javax.servlet.FilterChain.class);
        org.apache.ambari.server.security.authentication.AmbariAuthenticationFilter filter1 = createMock(org.apache.ambari.server.security.authentication.AmbariAuthenticationFilter.class);
        EasyMock.expect(filter1.shouldApply(httpServletRequest)).andReturn(false).once();
        org.apache.ambari.server.security.authentication.AmbariAuthenticationFilter filter2 = createMock(org.apache.ambari.server.security.authentication.AmbariAuthenticationFilter.class);
        EasyMock.expect(filter2.shouldApply(httpServletRequest)).andReturn(false).once();
        org.apache.ambari.server.security.authentication.AmbariAuthenticationFilter filterN = createMock(org.apache.ambari.server.security.authentication.AmbariAuthenticationFilter.class);
        EasyMock.expect(filterN.shouldApply(httpServletRequest)).andReturn(true).once();
        filterN.doFilter(httpServletRequest, httpServletResponse, filterChain);
        EasyMock.expectLastCall().once();
        org.apache.ambari.server.security.authentication.AmbariAuthenticationFilter filter3 = createMock(org.apache.ambari.server.security.authentication.AmbariAuthenticationFilter.class);
        replayAll();
        javax.servlet.Filter filter = new org.apache.ambari.server.security.authentication.AmbariDelegatingAuthenticationFilter(java.util.Arrays.asList(filter1, filter2, filterN, filter3));
        filter.doFilter(httpServletRequest, httpServletResponse, filterChain);
        verifyAll();
    }

    @org.junit.Test
    public void testDestroy() throws java.lang.Exception {
        org.apache.ambari.server.security.authentication.AmbariAuthenticationFilter filter1 = createMock(org.apache.ambari.server.security.authentication.AmbariAuthenticationFilter.class);
        filter1.destroy();
        EasyMock.expectLastCall().once();
        org.apache.ambari.server.security.authentication.AmbariAuthenticationFilter filter2 = createMock(org.apache.ambari.server.security.authentication.AmbariAuthenticationFilter.class);
        filter2.destroy();
        EasyMock.expectLastCall().once();
        org.apache.ambari.server.security.authentication.AmbariAuthenticationFilter filter3 = createMock(org.apache.ambari.server.security.authentication.AmbariAuthenticationFilter.class);
        filter3.destroy();
        EasyMock.expectLastCall().once();
        replayAll();
        javax.servlet.Filter filter = new org.apache.ambari.server.security.authentication.AmbariDelegatingAuthenticationFilter(java.util.Arrays.asList(filter1, filter2, filter3));
        filter.destroy();
        verifyAll();
    }
}