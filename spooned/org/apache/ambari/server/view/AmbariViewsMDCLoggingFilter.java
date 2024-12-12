package org.apache.ambari.server.view;
@com.google.inject.Singleton
public class AmbariViewsMDCLoggingFilter implements javax.servlet.Filter {
    private static final java.lang.String patternStr = "/api/v1/views/(.*)/versions/(.*)/instances/([^/]+).*";

    private static final java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(org.apache.ambari.server.view.AmbariViewsMDCLoggingFilter.patternStr);

    @java.lang.Override
    public void init(javax.servlet.FilterConfig filterConfig) throws javax.servlet.ServletException {
    }

    @java.lang.Override
    public void doFilter(javax.servlet.ServletRequest servletRequest, javax.servlet.ServletResponse servletResponse, javax.servlet.FilterChain filterChain) throws java.io.IOException, javax.servlet.ServletException {
        buildMDC(servletRequest);
        try {
            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            clear();
        }
    }

    private void buildMDC(javax.servlet.ServletRequest request) {
        if ((request instanceof javax.servlet.http.HttpServletRequest) && (org.slf4j.MDC.getMDCAdapter() != null)) {
            java.lang.String url = ((javax.servlet.http.HttpServletRequest) (request)).getRequestURI();
            java.util.regex.Matcher matcher = org.apache.ambari.server.view.AmbariViewsMDCLoggingFilter.pattern.matcher(url);
            if (matcher.find()) {
                org.slf4j.MDC.put("viewName", matcher.group(1));
                org.slf4j.MDC.put("viewVersion", matcher.group(2));
                org.slf4j.MDC.put("viewInstanceName", matcher.group(3));
            }
        }
    }

    private void clear() {
        org.slf4j.MDC.clear();
    }

    @java.lang.Override
    public void destroy() {
    }
}