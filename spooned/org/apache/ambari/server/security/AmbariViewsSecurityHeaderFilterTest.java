package org.apache.ambari.server.security;
import static org.easymock.EasyMock.expectLastCall;
public class AmbariViewsSecurityHeaderFilterTest extends org.apache.ambari.server.security.AbstractSecurityHeaderFilterTest {
    private static final java.util.Map<java.lang.String, java.lang.String> PROPERTY_NAME_MAP;

    private static final java.util.Map<java.lang.String, java.lang.String> DEFAULT_PROPERTY_VALUE_MAP;

    static {
        java.util.Map<java.lang.String, java.lang.String> map;
        map = new java.util.HashMap<>();
        map.put(org.apache.ambari.server.security.AbstractSecurityHeaderFilter.STRICT_TRANSPORT_HEADER, org.apache.ambari.server.configuration.Configuration.VIEWS_HTTP_STRICT_TRANSPORT_HEADER_VALUE.getKey());
        map.put(org.apache.ambari.server.security.AbstractSecurityHeaderFilter.X_FRAME_OPTIONS_HEADER, org.apache.ambari.server.configuration.Configuration.VIEWS_HTTP_X_FRAME_OPTIONS_HEADER_VALUE.getKey());
        map.put(org.apache.ambari.server.security.AbstractSecurityHeaderFilter.X_XSS_PROTECTION_HEADER, org.apache.ambari.server.configuration.Configuration.VIEWS_HTTP_X_XSS_PROTECTION_HEADER_VALUE.getKey());
        map.put(org.apache.ambari.server.security.AbstractSecurityHeaderFilter.X_CONTENT_TYPE_HEADER, org.apache.ambari.server.configuration.Configuration.VIEWS_HTTP_X_CONTENT_TYPE_HEADER_VALUE.getKey());
        map.put(org.apache.ambari.server.security.AbstractSecurityHeaderFilter.CACHE_CONTROL_HEADER, org.apache.ambari.server.configuration.Configuration.VIEWS_HTTP_CACHE_CONTROL_HEADER_VALUE.getKey());
        map.put(org.apache.ambari.server.security.AbstractSecurityHeaderFilter.PRAGMA_HEADER, org.apache.ambari.server.configuration.Configuration.VIEWS_HTTP_PRAGMA_HEADER_VALUE.getKey());
        PROPERTY_NAME_MAP = java.util.Collections.unmodifiableMap(map);
        map = new java.util.HashMap<>();
        map.put(org.apache.ambari.server.security.AbstractSecurityHeaderFilter.STRICT_TRANSPORT_HEADER, org.apache.ambari.server.configuration.Configuration.VIEWS_HTTP_STRICT_TRANSPORT_HEADER_VALUE.getDefaultValue());
        map.put(org.apache.ambari.server.security.AbstractSecurityHeaderFilter.X_FRAME_OPTIONS_HEADER, org.apache.ambari.server.configuration.Configuration.VIEWS_HTTP_X_FRAME_OPTIONS_HEADER_VALUE.getDefaultValue());
        map.put(org.apache.ambari.server.security.AbstractSecurityHeaderFilter.X_XSS_PROTECTION_HEADER, org.apache.ambari.server.configuration.Configuration.VIEWS_HTTP_X_XSS_PROTECTION_HEADER_VALUE.getDefaultValue());
        map.put(org.apache.ambari.server.security.AbstractSecurityHeaderFilter.X_CONTENT_TYPE_HEADER, org.apache.ambari.server.configuration.Configuration.VIEWS_HTTP_X_CONTENT_TYPE_HEADER_VALUE.getDefaultValue());
        map.put(org.apache.ambari.server.security.AbstractSecurityHeaderFilter.CACHE_CONTROL_HEADER, org.apache.ambari.server.configuration.Configuration.VIEWS_HTTP_CACHE_CONTROL_HEADER_VALUE.getDefaultValue());
        map.put(org.apache.ambari.server.security.AbstractSecurityHeaderFilter.PRAGMA_HEADER, org.apache.ambari.server.configuration.Configuration.VIEWS_HTTP_PRAGMA_HEADER_VALUE.getDefaultValue());
        DEFAULT_PROPERTY_VALUE_MAP = java.util.Collections.unmodifiableMap(map);
    }

    public AmbariViewsSecurityHeaderFilterTest() {
        super(org.apache.ambari.server.security.AmbariViewsSecurityHeaderFilter.class, org.apache.ambari.server.security.AmbariViewsSecurityHeaderFilterTest.PROPERTY_NAME_MAP, org.apache.ambari.server.security.AmbariViewsSecurityHeaderFilterTest.DEFAULT_PROPERTY_VALUE_MAP);
    }

    @java.lang.Override
    protected void expectHttpServletRequestMock(javax.servlet.http.HttpServletRequest request) {
        request.setAttribute(org.apache.ambari.server.security.AbstractSecurityHeaderFilter.DENY_HEADER_OVERRIDES_FLAG, "true");
        EasyMock.expectLastCall();
    }
}