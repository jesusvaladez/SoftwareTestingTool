package org.apache.ambari.server.view;
import org.easymock.EasyMock;
import org.easymock.EasyMockSupport;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
@org.junit.runner.RunWith(org.powermock.modules.junit4.PowerMockRunner.class)
@org.powermock.core.classloader.annotations.PrepareForTest({ org.apache.ambari.server.view.ViewThrottleFilter.class, java.util.concurrent.Semaphore.class })
public class ViewThrottleFilterTest extends org.easymock.EasyMockSupport {
    private com.google.inject.Injector m_injector;

    private java.util.concurrent.Semaphore m_mockSemaphore = createStrictMock(java.util.concurrent.Semaphore.class);

    @org.junit.Before
    public void setup() throws java.lang.Exception {
        m_injector = com.google.inject.Guice.createInjector(new org.apache.ambari.server.view.ViewThrottleFilterTest.MockModule());
        org.powermock.api.mockito.PowerMockito.whenNew(java.util.concurrent.Semaphore.class).withAnyArguments().thenReturn(m_mockSemaphore);
    }

    @org.junit.Test
    public void testAcquireInvokesFilterChain() throws java.lang.Exception {
        org.apache.ambari.server.configuration.Configuration configuration = m_injector.getInstance(org.apache.ambari.server.configuration.Configuration.class);
        org.easymock.EasyMock.expect(configuration.getViewRequestThreadPoolMaxSize()).andReturn(1).atLeastOnce();
        org.easymock.EasyMock.expect(configuration.getViewRequestThreadPoolTimeout()).andReturn(2000).atLeastOnce();
        org.easymock.EasyMock.expect(configuration.getClientThreadPoolSize()).andReturn(25).atLeastOnce();
        javax.servlet.http.HttpServletRequest request = createNiceMock(javax.servlet.http.HttpServletRequest.class);
        javax.servlet.http.HttpServletResponse response = createStrictMock(javax.servlet.http.HttpServletResponse.class);
        javax.servlet.FilterChain filterChain = createStrictMock(javax.servlet.FilterChain.class);
        org.easymock.EasyMock.expect(m_mockSemaphore.tryAcquire(2000, java.util.concurrent.TimeUnit.MILLISECONDS)).andReturn(true);
        filterChain.doFilter(request, response);
        org.easymock.EasyMock.expectLastCall().once();
        m_mockSemaphore.release();
        org.easymock.EasyMock.expectLastCall().once();
        replayAll();
        org.apache.ambari.server.view.ViewThrottleFilter filter = new org.apache.ambari.server.view.ViewThrottleFilter();
        m_injector.injectMembers(filter);
        filter.init(null);
        filter.doFilter(request, response, filterChain);
        verifyAll();
    }

    @org.junit.Test
    public void testSemaphorePreventsFilterChain() throws java.lang.Exception {
        org.apache.ambari.server.configuration.Configuration configuration = m_injector.getInstance(org.apache.ambari.server.configuration.Configuration.class);
        org.easymock.EasyMock.expect(configuration.getViewRequestThreadPoolMaxSize()).andReturn(1).atLeastOnce();
        org.easymock.EasyMock.expect(configuration.getViewRequestThreadPoolTimeout()).andReturn(2000).atLeastOnce();
        org.easymock.EasyMock.expect(configuration.getClientThreadPoolSize()).andReturn(25).atLeastOnce();
        javax.servlet.http.HttpServletRequest request = createNiceMock(javax.servlet.http.HttpServletRequest.class);
        javax.servlet.http.HttpServletResponse response = createStrictMock(javax.servlet.http.HttpServletResponse.class);
        javax.servlet.FilterChain filterChain = createStrictMock(javax.servlet.FilterChain.class);
        org.easymock.EasyMock.expect(m_mockSemaphore.tryAcquire(2000, java.util.concurrent.TimeUnit.MILLISECONDS)).andReturn(false);
        response.sendError(org.easymock.EasyMock.eq(javax.servlet.http.HttpServletResponse.SC_SERVICE_UNAVAILABLE), org.easymock.EasyMock.anyString());
        org.easymock.EasyMock.expectLastCall().once();
        replayAll();
        org.apache.ambari.server.view.ViewThrottleFilter filter = new org.apache.ambari.server.view.ViewThrottleFilter();
        m_injector.injectMembers(filter);
        filter.init(null);
        filter.doFilter(request, response, filterChain);
        verifyAll();
    }

    private class MockModule implements com.google.inject.Module {
        @java.lang.Override
        public void configure(com.google.inject.Binder binder) {
            binder.bind(org.apache.ambari.server.configuration.Configuration.class).toInstance(createNiceMock(org.apache.ambari.server.configuration.Configuration.class));
            binder.bind(org.apache.ambari.server.state.stack.OsFamily.class).toInstance(createNiceMock(org.apache.ambari.server.state.stack.OsFamily.class));
        }
    }
}