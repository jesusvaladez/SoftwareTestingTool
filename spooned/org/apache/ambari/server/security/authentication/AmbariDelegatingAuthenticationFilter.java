package org.apache.ambari.server.security.authentication;
@org.springframework.stereotype.Component
public class AmbariDelegatingAuthenticationFilter implements javax.servlet.Filter {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.security.authentication.AmbariDelegatingAuthenticationFilter.class);

    private final java.util.Collection<org.apache.ambari.server.security.authentication.AmbariAuthenticationFilter> filters;

    public AmbariDelegatingAuthenticationFilter(java.util.Collection<org.apache.ambari.server.security.authentication.AmbariAuthenticationFilter> filters) {
        this.filters = (filters == null) ? java.util.Collections.emptyList() : filters;
        if (this.filters.isEmpty()) {
            org.apache.ambari.server.security.authentication.AmbariDelegatingAuthenticationFilter.LOG.warn("The delegated filters list is empty. No authentication tests will be performed by this " + "authentication filter.");
        } else if (org.apache.ambari.server.security.authentication.AmbariDelegatingAuthenticationFilter.LOG.isDebugEnabled()) {
            java.lang.StringBuffer filterNames = new java.lang.StringBuffer();
            for (org.apache.ambari.server.security.authentication.AmbariAuthenticationFilter filter : this.filters) {
                filterNames.append("\n\t");
                filterNames.append(filter.getClass().getName());
            }
            org.apache.ambari.server.security.authentication.AmbariDelegatingAuthenticationFilter.LOG.debug("This authentication filter will attempt to authenticate a user using one of the " + "following delegated authentication filters: {}", filterNames);
        }
    }

    @java.lang.Override
    public void init(javax.servlet.FilterConfig filterConfig) throws javax.servlet.ServletException {
        for (org.apache.ambari.server.security.authentication.AmbariAuthenticationFilter filter : filters) {
            filter.init(filterConfig);
        }
    }

    @java.lang.Override
    public void doFilter(javax.servlet.ServletRequest servletRequest, javax.servlet.ServletResponse servletResponse, javax.servlet.FilterChain chain) throws java.io.IOException, javax.servlet.ServletException {
        boolean handled = false;
        javax.servlet.http.HttpServletRequest httpServletRequest = ((javax.servlet.http.HttpServletRequest) (servletRequest));
        for (org.apache.ambari.server.security.authentication.AmbariAuthenticationFilter filter : filters) {
            if (org.apache.ambari.server.security.authentication.AmbariDelegatingAuthenticationFilter.LOG.isTraceEnabled()) {
                org.apache.ambari.server.security.authentication.AmbariDelegatingAuthenticationFilter.LOG.trace("Attempting to apply authentication filter {}", filter.getClass().getName());
            }
            if (filter.shouldApply(httpServletRequest)) {
                if (org.apache.ambari.server.security.authentication.AmbariDelegatingAuthenticationFilter.LOG.isDebugEnabled()) {
                    org.apache.ambari.server.security.authentication.AmbariDelegatingAuthenticationFilter.LOG.debug("Using authentication filter {} since it applies", filter.getClass().getName());
                }
                filter.doFilter(servletRequest, servletResponse, chain);
                handled = true;
                break;
            } else if (org.apache.ambari.server.security.authentication.AmbariDelegatingAuthenticationFilter.LOG.isDebugEnabled()) {
                org.apache.ambari.server.security.authentication.AmbariDelegatingAuthenticationFilter.LOG.debug("Filter {} does not apply skipping", filter.getClass().getName());
            }
        }
        if (!handled) {
            org.apache.ambari.server.security.authentication.AmbariDelegatingAuthenticationFilter.LOG.debug("No delegated filters applied while attempting to authenticate a user, continuing with the filter chain.");
            chain.doFilter(servletRequest, servletResponse);
        }
    }

    @java.lang.Override
    public void destroy() {
        for (org.apache.ambari.server.security.authentication.AmbariAuthenticationFilter filter : filters) {
            filter.destroy();
        }
    }
}