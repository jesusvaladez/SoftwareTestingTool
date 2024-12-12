package org.apache.ambari.server.api;
import com.google.inject.persist.UnitOfWork;
@com.google.inject.Singleton
public class AmbariPersistFilter implements javax.servlet.Filter {
    private final com.google.inject.persist.UnitOfWork unitOfWork;

    @com.google.inject.Inject
    public AmbariPersistFilter(com.google.inject.persist.UnitOfWork unitOfWork) {
        this.unitOfWork = unitOfWork;
    }

    @java.lang.Override
    public void init(javax.servlet.FilterConfig filterConfig) throws javax.servlet.ServletException {
    }

    @java.lang.Override
    public void doFilter(javax.servlet.ServletRequest servletRequest, javax.servlet.ServletResponse servletResponse, javax.servlet.FilterChain filterChain) throws java.io.IOException, javax.servlet.ServletException {
        unitOfWork.begin();
        try {
            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            unitOfWork.end();
        }
    }

    @java.lang.Override
    public void destroy() {
    }
}