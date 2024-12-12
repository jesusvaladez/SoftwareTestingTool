package org.apache.ambari.server.security;
public class SecurityFilter implements javax.servlet.Filter {
    private static java.lang.String CA = "/ca";

    private static org.apache.ambari.server.configuration.Configuration config;

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.security.SecurityFilter.class);

    @java.lang.Override
    public void destroy() {
    }

    @java.lang.Override
    public void doFilter(javax.servlet.ServletRequest serReq, javax.servlet.ServletResponse serResp, javax.servlet.FilterChain filtCh) throws java.io.IOException, javax.servlet.ServletException {
        javax.servlet.http.HttpServletRequest req = ((javax.servlet.http.HttpServletRequest) (serReq));
        java.lang.String reqUrl = req.getRequestURL().toString();
        org.apache.ambari.server.security.SecurityFilter.LOG.debug("Filtering {} for security purposes", reqUrl);
        if (serReq.getLocalPort() != org.apache.ambari.server.security.SecurityFilter.config.getTwoWayAuthPort()) {
            if (isRequestAllowed(reqUrl)) {
                filtCh.doFilter(serReq, serResp);
            } else {
                org.apache.ambari.server.security.SecurityFilter.LOG.warn("This request is not allowed on this port: " + reqUrl);
            }
        } else {
            org.apache.ambari.server.security.SecurityFilter.LOG.debug("Request can continue on secure port {}", serReq.getLocalPort());
            filtCh.doFilter(serReq, serResp);
        }
    }

    @java.lang.Override
    public void init(javax.servlet.FilterConfig arg0) throws javax.servlet.ServletException {
    }

    private boolean isRequestAllowed(java.lang.String reqUrl) {
        try {
            java.net.URL url = new java.net.URL(reqUrl);
            if (!"https".equals(url.getProtocol())) {
                org.apache.ambari.server.security.SecurityFilter.LOG.warn(java.lang.String.format("Request %s is not using HTTPS", reqUrl));
                return false;
            }
            if (java.util.regex.Pattern.matches("/cert/ca(/?)", url.getPath())) {
                return true;
            }
            if (java.util.regex.Pattern.matches("/connection_info", url.getPath())) {
                return true;
            }
            if (java.util.regex.Pattern.matches("/certs/[^/0-9][^/]*", url.getPath())) {
                return true;
            }
            if (java.util.regex.Pattern.matches("/resources/.*", url.getPath())) {
                return true;
            }
        } catch (java.lang.Exception e) {
            org.apache.ambari.server.security.SecurityFilter.LOG.warn("Exception while validating if request is secure " + e);
        }
        org.apache.ambari.server.security.SecurityFilter.LOG.warn(("Request " + reqUrl) + " doesn't match any pattern.");
        return false;
    }

    public static void init(org.apache.ambari.server.configuration.Configuration instance) {
        org.apache.ambari.server.security.SecurityFilter.config = instance;
    }
}