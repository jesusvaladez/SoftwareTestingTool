package org.apache.ambari.server.security;
@com.google.inject.Singleton
public class AmbariViewsSecurityHeaderFilter extends org.apache.ambari.server.security.AbstractSecurityHeaderFilter {
    @java.lang.Override
    protected boolean checkPrerequisites(javax.servlet.ServletRequest servletRequest) {
        servletRequest.setAttribute(org.apache.ambari.server.security.AbstractSecurityHeaderFilter.DENY_HEADER_OVERRIDES_FLAG, "true");
        return true;
    }

    @java.lang.Override
    protected void processConfig(org.apache.ambari.server.configuration.Configuration configuration) {
        setSslEnabled(configuration.getApiSSLAuthentication());
        setStrictTransportSecurity(configuration.getViewsStrictTransportSecurityHTTPResponseHeader());
        setxFrameOptionsHeader(configuration.getViewsXFrameOptionsHTTPResponseHeader());
        setxXSSProtectionHeader(configuration.getViewsXXSSProtectionHTTPResponseHeader());
        setContentSecurityPolicyHeader(configuration.getViewsContentSecurityPolicyHTTPResponseHeader());
        setXContentTypeHeader(configuration.getViewsXContentTypeHTTPResponseHeader());
        setCacheControlHeader(configuration.getViewsCacheControlHTTPResponseHeader());
        setPragmaHeader(configuration.getViewsPragmaHTTPResponseHeader());
        setCharset(configuration.getViewsCharsetHTTPResponseHeader());
    }
}