package org.apache.ambari.server.security;
import org.apache.commons.lang.StringUtils;
public abstract class AbstractSecurityHeaderFilter implements javax.servlet.Filter {
    protected static final java.lang.String STRICT_TRANSPORT_HEADER = "Strict-Transport-Security";

    protected static final java.lang.String X_FRAME_OPTIONS_HEADER = "X-Frame-Options";

    protected static final java.lang.String X_XSS_PROTECTION_HEADER = "X-XSS-Protection";

    protected static final java.lang.String CONTENT_SECURITY_POLICY_HEADER = "Content-Security-Policy";

    protected static final java.lang.String X_CONTENT_TYPE_HEADER = "X-Content-Type-Options";

    protected static final java.lang.String CACHE_CONTROL_HEADER = "Cache-Control";

    protected static final java.lang.String PRAGMA_HEADER = "Pragma";

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.security.AbstractSecurityHeaderFilter.class);

    protected static final java.lang.String DENY_HEADER_OVERRIDES_FLAG = "deny.header.overrides.flag";

    @com.google.inject.Inject
    private org.apache.ambari.server.configuration.Configuration configuration;

    private boolean sslEnabled = true;

    private java.lang.String strictTransportSecurity = org.apache.ambari.server.configuration.Configuration.HTTP_STRICT_TRANSPORT_HEADER_VALUE.getDefaultValue();

    private java.lang.String xFrameOptionsHeader = org.apache.ambari.server.configuration.Configuration.HTTP_X_FRAME_OPTIONS_HEADER_VALUE.getDefaultValue();

    private java.lang.String xXSSProtectionHeader = org.apache.ambari.server.configuration.Configuration.HTTP_X_XSS_PROTECTION_HEADER_VALUE.getDefaultValue();

    private java.lang.String contentSecurityPolicyHeader = org.apache.ambari.server.configuration.Configuration.HTTP_CONTENT_SECURITY_POLICY_HEADER_VALUE.getDefaultValue();

    private java.lang.String xContentTypeHeader = org.apache.ambari.server.configuration.Configuration.HTTP_X_CONTENT_TYPE_HEADER_VALUE.getDefaultValue();

    private java.lang.String cacheControlHeader = org.apache.ambari.server.configuration.Configuration.HTTP_CACHE_CONTROL_HEADER_VALUE.getDefaultValue();

    private java.lang.String pragmaHeader = org.apache.ambari.server.configuration.Configuration.HTTP_PRAGMA_HEADER_VALUE.getDefaultValue();

    private java.lang.String charset = org.apache.ambari.server.configuration.Configuration.HTTP_CHARSET.getDefaultValue();

    @java.lang.Override
    public void init(javax.servlet.FilterConfig filterConfig) throws javax.servlet.ServletException {
        org.apache.ambari.server.security.AbstractSecurityHeaderFilter.LOG.debug("Initializing {}", this.getClass().getName());
        if (configuration == null) {
            org.apache.ambari.server.security.AbstractSecurityHeaderFilter.LOG.warn("The Ambari configuration object is not available, all default options will be assumed.");
        } else {
            processConfig(configuration);
        }
    }

    @java.lang.Override
    public void doFilter(javax.servlet.ServletRequest servletRequest, javax.servlet.ServletResponse servletResponse, javax.servlet.FilterChain filterChain) throws java.io.IOException, javax.servlet.ServletException {
        if (checkPrerequisites(servletRequest)) {
            doFilterInternal(servletRequest, servletResponse);
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    protected abstract boolean checkPrerequisites(javax.servlet.ServletRequest servletRequest);

    @java.lang.Override
    public void destroy() {
        org.apache.ambari.server.security.AbstractSecurityHeaderFilter.LOG.debug("Destroying {}", this.getClass().getName());
    }

    protected abstract void processConfig(org.apache.ambari.server.configuration.Configuration configuration);

    protected void setSslEnabled(boolean sslEnabled) {
        this.sslEnabled = sslEnabled;
    }

    protected void setStrictTransportSecurity(java.lang.String strictTransportSecurity) {
        this.strictTransportSecurity = strictTransportSecurity;
    }

    protected void setxFrameOptionsHeader(java.lang.String xFrameOptionsHeader) {
        this.xFrameOptionsHeader = xFrameOptionsHeader;
    }

    protected void setxXSSProtectionHeader(java.lang.String xXSSProtectionHeader) {
        this.xXSSProtectionHeader = xXSSProtectionHeader;
    }

    protected void setContentSecurityPolicyHeader(java.lang.String contentSecurityPolicyHeader) {
        this.contentSecurityPolicyHeader = contentSecurityPolicyHeader;
    }

    protected void setXContentTypeHeader(java.lang.String xContentTypeHeader) {
        this.xContentTypeHeader = xContentTypeHeader;
    }

    protected void setCacheControlHeader(java.lang.String cacheControlHeader) {
        this.cacheControlHeader = cacheControlHeader;
    }

    protected void setPragmaHeader(java.lang.String pragmaHeader) {
        this.pragmaHeader = pragmaHeader;
    }

    protected void setCharset(java.lang.String charset) {
        this.charset = charset;
    }

    private void doFilterInternal(javax.servlet.ServletRequest servletRequest, javax.servlet.ServletResponse servletResponse) {
        if (servletResponse instanceof javax.servlet.http.HttpServletResponse) {
            javax.servlet.http.HttpServletResponse httpServletResponse = ((javax.servlet.http.HttpServletResponse) (servletResponse));
            if (sslEnabled && (!org.apache.commons.lang.StringUtils.isEmpty(strictTransportSecurity))) {
                httpServletResponse.setHeader(org.apache.ambari.server.security.AbstractSecurityHeaderFilter.STRICT_TRANSPORT_HEADER, strictTransportSecurity);
            }
            if (!org.apache.commons.lang.StringUtils.isEmpty(xFrameOptionsHeader)) {
                httpServletResponse.setHeader(org.apache.ambari.server.security.AbstractSecurityHeaderFilter.X_FRAME_OPTIONS_HEADER, xFrameOptionsHeader);
            }
            if (!org.apache.commons.lang.StringUtils.isEmpty(xXSSProtectionHeader)) {
                httpServletResponse.setHeader(org.apache.ambari.server.security.AbstractSecurityHeaderFilter.X_XSS_PROTECTION_HEADER, xXSSProtectionHeader);
            }
            if (!org.apache.commons.lang.StringUtils.isEmpty(contentSecurityPolicyHeader)) {
                httpServletResponse.setHeader(org.apache.ambari.server.security.AbstractSecurityHeaderFilter.CONTENT_SECURITY_POLICY_HEADER, contentSecurityPolicyHeader);
            }
            if (!org.apache.commons.lang.StringUtils.isEmpty(xContentTypeHeader)) {
                httpServletResponse.setHeader(org.apache.ambari.server.security.AbstractSecurityHeaderFilter.X_CONTENT_TYPE_HEADER, xContentTypeHeader);
            }
            if (!org.apache.commons.lang.StringUtils.isEmpty(cacheControlHeader)) {
                httpServletResponse.setHeader(org.apache.ambari.server.security.AbstractSecurityHeaderFilter.CACHE_CONTROL_HEADER, cacheControlHeader);
            }
            if (!org.apache.commons.lang.StringUtils.isEmpty(pragmaHeader)) {
                httpServletResponse.setHeader(org.apache.ambari.server.security.AbstractSecurityHeaderFilter.PRAGMA_HEADER, pragmaHeader);
            }
            if (!org.apache.commons.lang.StringUtils.isEmpty(charset)) {
                httpServletResponse.setCharacterEncoding(charset);
            }
        }
    }
}