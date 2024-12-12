package org.apache.ambari.server.security;
import static org.easymock.EasyMock.expect;
public class AmbariServerSecurityHeaderFilterTest extends org.apache.ambari.server.security.AbstractSecurityHeaderFilterTest {
    private static final java.util.Map<java.lang.String, java.lang.String> PROPERTY_NAME_MAP;

    private static final java.util.Map<java.lang.String, java.lang.String> DEFAULT_PROPERTY_VALUE_MAP;

    static {
        java.util.Map<java.lang.String, java.lang.String> map;
        map = new java.util.HashMap<>();
        map.put(org.apache.ambari.server.security.AbstractSecurityHeaderFilter.STRICT_TRANSPORT_HEADER, org.apache.ambari.server.configuration.Configuration.HTTP_STRICT_TRANSPORT_HEADER_VALUE.getKey());
        map.put(org.apache.ambari.server.security.AbstractSecurityHeaderFilter.X_FRAME_OPTIONS_HEADER, org.apache.ambari.server.configuration.Configuration.HTTP_X_FRAME_OPTIONS_HEADER_VALUE.getKey());
        map.put(org.apache.ambari.server.security.AbstractSecurityHeaderFilter.X_XSS_PROTECTION_HEADER, org.apache.ambari.server.configuration.Configuration.HTTP_X_XSS_PROTECTION_HEADER_VALUE.getKey());
        map.put(org.apache.ambari.server.security.AbstractSecurityHeaderFilter.CONTENT_SECURITY_POLICY_HEADER, org.apache.ambari.server.configuration.Configuration.HTTP_CONTENT_SECURITY_POLICY_HEADER_VALUE.getKey());
        map.put(org.apache.ambari.server.security.AbstractSecurityHeaderFilter.X_CONTENT_TYPE_HEADER, org.apache.ambari.server.configuration.Configuration.HTTP_X_CONTENT_TYPE_HEADER_VALUE.getKey());
        map.put(org.apache.ambari.server.security.AbstractSecurityHeaderFilter.CACHE_CONTROL_HEADER, org.apache.ambari.server.configuration.Configuration.HTTP_CACHE_CONTROL_HEADER_VALUE.getKey());
        map.put(org.apache.ambari.server.security.AbstractSecurityHeaderFilter.PRAGMA_HEADER, org.apache.ambari.server.configuration.Configuration.HTTP_PRAGMA_HEADER_VALUE.getKey());
        PROPERTY_NAME_MAP = java.util.Collections.unmodifiableMap(map);
        map = new java.util.HashMap<>();
        map.put(org.apache.ambari.server.security.AbstractSecurityHeaderFilter.STRICT_TRANSPORT_HEADER, org.apache.ambari.server.configuration.Configuration.HTTP_STRICT_TRANSPORT_HEADER_VALUE.getDefaultValue());
        map.put(org.apache.ambari.server.security.AbstractSecurityHeaderFilter.X_FRAME_OPTIONS_HEADER, org.apache.ambari.server.configuration.Configuration.HTTP_X_FRAME_OPTIONS_HEADER_VALUE.getDefaultValue());
        map.put(org.apache.ambari.server.security.AbstractSecurityHeaderFilter.X_XSS_PROTECTION_HEADER, org.apache.ambari.server.configuration.Configuration.HTTP_X_XSS_PROTECTION_HEADER_VALUE.getDefaultValue());
        map.put(org.apache.ambari.server.security.AbstractSecurityHeaderFilter.CONTENT_SECURITY_POLICY_HEADER, org.apache.ambari.server.configuration.Configuration.HTTP_CONTENT_SECURITY_POLICY_HEADER_VALUE.getDefaultValue());
        map.put(org.apache.ambari.server.security.AbstractSecurityHeaderFilter.X_CONTENT_TYPE_HEADER, org.apache.ambari.server.configuration.Configuration.HTTP_X_CONTENT_TYPE_HEADER_VALUE.getDefaultValue());
        map.put(org.apache.ambari.server.security.AbstractSecurityHeaderFilter.CACHE_CONTROL_HEADER, org.apache.ambari.server.configuration.Configuration.HTTP_CACHE_CONTROL_HEADER_VALUE.getDefaultValue());
        map.put(org.apache.ambari.server.security.AbstractSecurityHeaderFilter.PRAGMA_HEADER, org.apache.ambari.server.configuration.Configuration.HTTP_PRAGMA_HEADER_VALUE.getDefaultValue());
        DEFAULT_PROPERTY_VALUE_MAP = java.util.Collections.unmodifiableMap(map);
    }

    public AmbariServerSecurityHeaderFilterTest() {
        super(org.apache.ambari.server.security.AmbariServerSecurityHeaderFilter.class, org.apache.ambari.server.security.AmbariServerSecurityHeaderFilterTest.PROPERTY_NAME_MAP, org.apache.ambari.server.security.AmbariServerSecurityHeaderFilterTest.DEFAULT_PROPERTY_VALUE_MAP);
    }

    @java.lang.Override
    protected void expectHttpServletRequestMock(javax.servlet.http.HttpServletRequest request) {
        EasyMock.expect(request.getAttribute(org.apache.ambari.server.security.AbstractSecurityHeaderFilter.DENY_HEADER_OVERRIDES_FLAG)).andReturn(null);
    }
}