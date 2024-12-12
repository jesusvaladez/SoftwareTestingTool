package org.apache.ambari.server.view;
import org.eclipse.jetty.continuation.Continuation;
@com.google.inject.Singleton
public class ViewThrottleFilter implements javax.servlet.Filter {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.view.ViewThrottleFilter.class);

    @com.google.inject.Inject
    private org.apache.ambari.server.configuration.Configuration m_configuration;

    private java.util.concurrent.Semaphore m_semaphore;

    private int m_timeout;

    @java.lang.Override
    public void init(javax.servlet.FilterConfig filterConfig) throws javax.servlet.ServletException {
        m_timeout = m_configuration.getViewRequestThreadPoolTimeout();
        int clientThreadPoolSize = m_configuration.getClientThreadPoolSize();
        int viewThreadPoolSize = m_configuration.getViewRequestThreadPoolMaxSize();
        int viewSemaphoreCount = clientThreadPoolSize / 2;
        if (viewThreadPoolSize > 0) {
            viewSemaphoreCount = viewThreadPoolSize;
            if (viewThreadPoolSize > clientThreadPoolSize) {
                org.apache.ambari.server.view.ViewThrottleFilter.LOG.warn("The number of view processing threads ({}) cannot be greater than the REST API client threads {{})", viewThreadPoolSize, clientThreadPoolSize);
                viewSemaphoreCount = clientThreadPoolSize;
            }
        }
        org.apache.ambari.server.view.ViewThrottleFilter.LOG.info("Ambari Views will be able to utilize {} concurrent REST API threads", viewSemaphoreCount);
        m_semaphore = new java.util.concurrent.Semaphore(viewSemaphoreCount);
    }

    @java.lang.Override
    public void doFilter(javax.servlet.ServletRequest request, javax.servlet.ServletResponse response, javax.servlet.FilterChain chain) throws java.io.IOException, javax.servlet.ServletException {
        if (!(request instanceof javax.servlet.http.HttpServletRequest)) {
            chain.doFilter(request, response);
            return;
        }
        javax.servlet.http.HttpServletResponse httpResponse = ((javax.servlet.http.HttpServletResponse) (response));
        boolean acquired = false;
        try {
            acquired = m_semaphore.tryAcquire(m_timeout, java.util.concurrent.TimeUnit.MILLISECONDS);
        } catch (java.lang.InterruptedException interruptedException) {
            org.apache.ambari.server.view.ViewThrottleFilter.LOG.warn("While waiting for an available thread, the view request was interrupted");
        }
        if (!acquired) {
            httpResponse.sendError(javax.servlet.http.HttpServletResponse.SC_SERVICE_UNAVAILABLE, "There are no available threads to handle view requests");
            return;
        }
        try {
            chain.doFilter(request, response);
        } finally {
            m_semaphore.release();
        }
    }

    @java.lang.Override
    public void destroy() {
    }
}