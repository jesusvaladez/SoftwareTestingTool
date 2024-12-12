package org.apache.ambari.server.security;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import static org.easymock.EasyMock.createNiceMock;
public class SecurityFilterTest {
    private com.google.inject.Injector injector;

    @org.junit.Before
    public void setUp() throws java.io.IOException {
        injector = com.google.inject.Guice.createInjector(new org.apache.ambari.server.security.SecurityFilterTest.MockModule());
        org.apache.ambari.server.security.SecurityFilter.init(injector.getInstance(org.apache.ambari.server.configuration.Configuration.class));
    }

    @org.junit.Test
    public void mustFilterNonHttpsRequests() throws java.lang.Exception {
        org.apache.ambari.server.security.SecurityFilter filter = new org.apache.ambari.server.security.SecurityFilter();
        org.springframework.mock.web.MockHttpServletRequest request = this.getDefaultRequest();
        request.setRequestURI("/certs/");
        request.setScheme("http");
        org.springframework.mock.web.MockHttpServletResponse response = new org.springframework.mock.web.MockHttpServletResponse();
        request.setLocalPort(8440);
        org.springframework.mock.web.MockFilterChain chain = new org.springframework.mock.web.MockFilterChain();
        filter.doFilter(request, response, chain);
        junit.framework.Assert.assertNull(chain.getRequest());
        junit.framework.Assert.assertNull(chain.getResponse());
    }

    @org.junit.Test
    public void mustAllowSecurePortRequests() throws java.lang.Exception {
        org.apache.ambari.server.security.SecurityFilter filter = new org.apache.ambari.server.security.SecurityFilter();
        org.springframework.mock.web.MockHttpServletResponse response = new org.springframework.mock.web.MockHttpServletResponse();
        org.springframework.mock.web.MockHttpServletRequest request = this.getDefaultRequest();
        request.setServerPort(8441);
        request.setLocalPort(8441);
        request.setRequestURI("/certs/");
        org.springframework.mock.web.MockFilterChain chain = new org.springframework.mock.web.MockFilterChain();
        filter.doFilter(request, response, chain);
        junit.framework.Assert.assertEquals(request, chain.getRequest());
        junit.framework.Assert.assertEquals(response, chain.getResponse());
    }

    @org.junit.Test
    public void mustAllowCertCreationRequests() throws java.lang.Exception {
        org.apache.ambari.server.security.SecurityFilter filter = new org.apache.ambari.server.security.SecurityFilter();
        org.springframework.mock.web.MockHttpServletResponse response = new org.springframework.mock.web.MockHttpServletResponse();
        org.springframework.mock.web.MockHttpServletRequest request = this.getDefaultRequest();
        request.setRequestURI("/certs/www.andromeda-01.com");
        request.setMethod("POST");
        org.springframework.mock.web.MockFilterChain chain = new org.springframework.mock.web.MockFilterChain();
        filter.doFilter(request, response, chain);
        junit.framework.Assert.assertEquals(request, chain.getRequest());
        junit.framework.Assert.assertEquals(response, chain.getResponse());
    }

    @org.junit.Test
    public void mustAllowCertCaGetRequests() throws java.lang.Exception {
        org.apache.ambari.server.security.SecurityFilter filter = new org.apache.ambari.server.security.SecurityFilter();
        org.springframework.mock.web.MockHttpServletResponse response = new org.springframework.mock.web.MockHttpServletResponse();
        org.springframework.mock.web.MockHttpServletRequest request = this.getDefaultRequest();
        request.setRequestURI("/cert/ca/");
        org.springframework.mock.web.MockFilterChain chain = new org.springframework.mock.web.MockFilterChain();
        filter.doFilter(request, response, chain);
        junit.framework.Assert.assertEquals(request, chain.getRequest());
        junit.framework.Assert.assertEquals(response, chain.getResponse());
    }

    private org.springframework.mock.web.MockHttpServletRequest getDefaultRequest() {
        org.springframework.mock.web.MockHttpServletRequest request = new org.springframework.mock.web.MockHttpServletRequest();
        request.setServerPort(8440);
        request.setMethod("GET");
        request.setServerName("www.andromeda-01.com");
        request.setScheme("https");
        return request;
    }

    private class MockModule implements com.google.inject.Module {
        @java.lang.Override
        public void configure(com.google.inject.Binder binder) {
            binder.bind(org.apache.ambari.server.state.stack.OsFamily.class).toInstance(EasyMock.createNiceMock(org.apache.ambari.server.state.stack.OsFamily.class));
        }
    }
}