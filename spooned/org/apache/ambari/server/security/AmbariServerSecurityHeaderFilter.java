package org.apache.ambari.server.security;
@com.google.inject.Singleton
public class AmbariServerSecurityHeaderFilter extends org.apache.ambari.server.security.AbstractSecurityHeaderFilter {
    @java.lang.Override
    protected boolean checkPrerequisites(javax.servlet.ServletRequest servletRequest) {
        boolean retVal = false;
        if (null == servletRequest.getAttribute(org.apache.ambari.server.security.AbstractSecurityHeaderFilter.DENY_HEADER_OVERRIDES_FLAG)) {
            retVal = true;
        } else {
            servletRequest.removeAttribute(org.apache.ambari.server.security.AbstractSecurityHeaderFilter.DENY_HEADER_OVERRIDES_FLAG);
        }
        return retVal;
    }

    @java.lang.Override
    protected void processConfig(org.apache.ambari.server.configuration.Configuration configuration) {
        setSslEnabled(configuration.getApiSSLAuthentication());
        setStrictTransportSecurity(configuration.getStrictTransportSecurityHTTPResponseHeader());
        setxFrameOptionsHeader(configuration.getXFrameOptionsHTTPResponseHeader());
        setxXSSProtectionHeader(configuration.getXXSSProtectionHTTPResponseHeader());
        setContentSecurityPolicyHeader(configuration.getContentSecurityPolicyHTTPResponseHeader());
        setXContentTypeHeader(configuration.getXContentTypeHTTPResponseHeader());
        setCacheControlHeader(configuration.getCacheControlHTTPResponseHeader());
        setPragmaHeader(configuration.getPragmaHTTPResponseHeader());
        setCharset(configuration.getCharsetHTTPResponseHeader());
    }
}