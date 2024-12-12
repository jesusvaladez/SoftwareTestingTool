package org.apache.ambari.server.api;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import org.easymock.EasyMockRule;
import org.easymock.EasyMockSupport;
import org.easymock.Mock;
import org.easymock.MockType;
import static org.easymock.EasyMock.expect;
public class ContentTypeOverrideFilterTest extends org.easymock.EasyMockSupport {
    private class FilterChainMock implements javax.servlet.FilterChain {
        javax.servlet.http.HttpServletResponse response;

        javax.servlet.http.HttpServletRequest request;

        @java.lang.Override
        public void doFilter(javax.servlet.ServletRequest request, javax.servlet.ServletResponse response) throws java.io.IOException, javax.servlet.ServletException {
            this.request = ((javax.servlet.http.HttpServletRequest) (request));
            this.response = ((javax.servlet.http.HttpServletResponse) (response));
        }
    }

    @org.junit.Rule
    public org.easymock.EasyMockRule mock = new org.easymock.EasyMockRule(this);

    @org.easymock.Mock(type = org.easymock.MockType.NICE)
    private javax.servlet.http.HttpServletRequest request;

    @org.easymock.Mock(type = org.easymock.MockType.NICE)
    private javax.servlet.http.HttpServletResponse response;

    private final org.apache.ambari.server.api.ContentTypeOverrideFilter filter = new org.apache.ambari.server.api.ContentTypeOverrideFilter();

    @org.junit.Test
    public void testJSONContentTypeRequest() throws java.lang.Exception {
        java.util.Vector<java.lang.String> headers = new java.util.Vector<>(1);
        headers.add(MediaType.APPLICATION_JSON);
        EasyMock.expect(request.getContentType()).andReturn(MediaType.APPLICATION_JSON).atLeastOnce();
        EasyMock.expect(request.getHeader(HttpHeaders.CONTENT_TYPE)).andReturn(MediaType.APPLICATION_JSON).atLeastOnce();
        EasyMock.expect(request.getHeaders(HttpHeaders.CONTENT_TYPE)).andReturn(headers.elements()).atLeastOnce();
        replayAll();
        org.apache.ambari.server.api.ContentTypeOverrideFilterTest.FilterChainMock chain = new org.apache.ambari.server.api.ContentTypeOverrideFilterTest.FilterChainMock();
        filter.doFilter(request, response, chain);
        junit.framework.Assert.assertEquals(MediaType.TEXT_PLAIN, chain.request.getHeader(HttpHeaders.CONTENT_TYPE));
        junit.framework.Assert.assertEquals(MediaType.TEXT_PLAIN, chain.request.getHeaders(HttpHeaders.CONTENT_TYPE).nextElement());
        verifyAll();
    }
}