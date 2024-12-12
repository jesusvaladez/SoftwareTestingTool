package org.apache.ambari.server.security;
import org.easymock.EasyMockSupport;
import static org.easymock.EasyMock.expectLastCall;
public abstract class AbstractSecurityHeaderFilterTest extends org.easymock.EasyMockSupport {
    @org.junit.Rule
    public org.junit.rules.TemporaryFolder temporaryFolder = new org.junit.rules.TemporaryFolder();

    private final java.lang.Class<? extends org.apache.ambari.server.security.AbstractSecurityHeaderFilter> filterClass;

    private final java.util.Map<java.lang.String, java.lang.String> propertyNameMap;

    private final java.util.Map<java.lang.String, java.lang.String> defatulPropertyValueMap;

    protected AbstractSecurityHeaderFilterTest(java.lang.Class<? extends org.apache.ambari.server.security.AbstractSecurityHeaderFilter> filterClass, java.util.Map<java.lang.String, java.lang.String> propertyNameMap, java.util.Map<java.lang.String, java.lang.String> defatulPropertyValueMap) {
        this.filterClass = filterClass;
        this.propertyNameMap = propertyNameMap;
        this.defatulPropertyValueMap = defatulPropertyValueMap;
    }

    protected abstract void expectHttpServletRequestMock(javax.servlet.http.HttpServletRequest request);

    @org.junit.Before
    public void setUp() throws java.lang.Exception {
        temporaryFolder.create();
    }

    @org.junit.After
    public void tearDown() throws java.lang.Exception {
        temporaryFolder.delete();
    }

    @org.junit.Test
    public void testDoFilter_DefaultValuesNoSSL() throws java.lang.Exception {
        com.google.inject.Injector injector = com.google.inject.Guice.createInjector(new com.google.inject.AbstractModule() {
            @java.lang.Override
            protected void configure() {
                java.util.Properties properties = new java.util.Properties();
                properties.setProperty(org.apache.ambari.server.configuration.Configuration.API_USE_SSL.getKey(), "false");
                properties.setProperty(org.apache.ambari.server.configuration.Configuration.HTTP_CHARSET.getKey(), org.apache.ambari.server.configuration.Configuration.HTTP_CHARSET.getDefaultValue());
                bind(org.apache.ambari.server.state.stack.OsFamily.class).toInstance(createNiceMock(org.apache.ambari.server.state.stack.OsFamily.class));
                bind(org.apache.ambari.server.configuration.Configuration.class).toInstance(new org.apache.ambari.server.configuration.Configuration(properties));
            }
        });
        javax.servlet.FilterConfig filterConfig = createNiceMock(javax.servlet.FilterConfig.class);
        javax.servlet.http.HttpServletRequest servletRequest = createStrictMock(javax.servlet.http.HttpServletRequest.class);
        expectHttpServletRequestMock(servletRequest);
        javax.servlet.http.HttpServletResponse servletResponse = createStrictMock(javax.servlet.http.HttpServletResponse.class);
        servletResponse.setHeader(org.apache.ambari.server.security.AbstractSecurityHeaderFilter.X_FRAME_OPTIONS_HEADER, defatulPropertyValueMap.get(org.apache.ambari.server.security.AbstractSecurityHeaderFilter.X_FRAME_OPTIONS_HEADER));
        EasyMock.expectLastCall().once();
        servletResponse.setHeader(org.apache.ambari.server.security.AbstractSecurityHeaderFilter.X_XSS_PROTECTION_HEADER, defatulPropertyValueMap.get(org.apache.ambari.server.security.AbstractSecurityHeaderFilter.X_XSS_PROTECTION_HEADER));
        EasyMock.expectLastCall().once();
        servletResponse.setHeader(org.apache.ambari.server.security.AbstractSecurityHeaderFilter.X_CONTENT_TYPE_HEADER, defatulPropertyValueMap.get(org.apache.ambari.server.security.AbstractSecurityHeaderFilter.X_CONTENT_TYPE_HEADER));
        EasyMock.expectLastCall().once();
        servletResponse.setHeader(org.apache.ambari.server.security.AbstractSecurityHeaderFilter.CACHE_CONTROL_HEADER, defatulPropertyValueMap.get(org.apache.ambari.server.security.AbstractSecurityHeaderFilter.CACHE_CONTROL_HEADER));
        EasyMock.expectLastCall().once();
        servletResponse.setHeader(org.apache.ambari.server.security.AbstractSecurityHeaderFilter.PRAGMA_HEADER, defatulPropertyValueMap.get(org.apache.ambari.server.security.AbstractSecurityHeaderFilter.PRAGMA_HEADER));
        EasyMock.expectLastCall().once();
        servletResponse.setCharacterEncoding(org.apache.ambari.server.configuration.Configuration.HTTP_CHARSET.getDefaultValue());
        EasyMock.expectLastCall().once();
        javax.servlet.FilterChain filterChain = createStrictMock(javax.servlet.FilterChain.class);
        filterChain.doFilter(servletRequest, servletResponse);
        EasyMock.expectLastCall().once();
        replayAll();
        org.apache.ambari.server.security.AbstractSecurityHeaderFilter securityFilter = injector.getInstance(filterClass);
        junit.framework.Assert.assertNotNull(securityFilter);
        securityFilter.init(filterConfig);
        securityFilter.doFilter(servletRequest, servletResponse, filterChain);
        verifyAll();
    }

    @org.junit.Test
    public void testDoFilter_DefaultValuesSSL() throws java.lang.Exception {
        final java.io.File httpPassFile = temporaryFolder.newFile();
        com.google.inject.Injector injector = com.google.inject.Guice.createInjector(new com.google.inject.AbstractModule() {
            @java.lang.Override
            protected void configure() {
                java.util.Properties properties = new java.util.Properties();
                properties.setProperty(org.apache.ambari.server.configuration.Configuration.API_USE_SSL.getKey(), "true");
                properties.setProperty(org.apache.ambari.server.configuration.Configuration.CLIENT_API_SSL_KSTR_DIR_NAME.getKey(), httpPassFile.getParent());
                properties.setProperty(org.apache.ambari.server.configuration.Configuration.CLIENT_API_SSL_CRT_PASS_FILE_NAME.getKey(), httpPassFile.getName());
                properties.setProperty(org.apache.ambari.server.configuration.Configuration.HTTP_CHARSET.getKey(), org.apache.ambari.server.configuration.Configuration.HTTP_CHARSET.getDefaultValue());
                bind(org.apache.ambari.server.state.stack.OsFamily.class).toInstance(createNiceMock(org.apache.ambari.server.state.stack.OsFamily.class));
                bind(org.apache.ambari.server.configuration.Configuration.class).toInstance(new org.apache.ambari.server.configuration.Configuration(properties));
            }
        });
        javax.servlet.FilterConfig filterConfig = createNiceMock(javax.servlet.FilterConfig.class);
        javax.servlet.http.HttpServletRequest servletRequest = createStrictMock(javax.servlet.http.HttpServletRequest.class);
        expectHttpServletRequestMock(servletRequest);
        javax.servlet.http.HttpServletResponse servletResponse = createStrictMock(javax.servlet.http.HttpServletResponse.class);
        servletResponse.setHeader(org.apache.ambari.server.security.AbstractSecurityHeaderFilter.STRICT_TRANSPORT_HEADER, defatulPropertyValueMap.get(org.apache.ambari.server.security.AbstractSecurityHeaderFilter.STRICT_TRANSPORT_HEADER));
        EasyMock.expectLastCall().once();
        servletResponse.setHeader(org.apache.ambari.server.security.AbstractSecurityHeaderFilter.X_FRAME_OPTIONS_HEADER, defatulPropertyValueMap.get(org.apache.ambari.server.security.AbstractSecurityHeaderFilter.X_FRAME_OPTIONS_HEADER));
        EasyMock.expectLastCall().once();
        servletResponse.setHeader(org.apache.ambari.server.security.AbstractSecurityHeaderFilter.X_XSS_PROTECTION_HEADER, defatulPropertyValueMap.get(org.apache.ambari.server.security.AbstractSecurityHeaderFilter.X_XSS_PROTECTION_HEADER));
        EasyMock.expectLastCall().once();
        servletResponse.setHeader(org.apache.ambari.server.security.AbstractSecurityHeaderFilter.X_CONTENT_TYPE_HEADER, defatulPropertyValueMap.get(org.apache.ambari.server.security.AbstractSecurityHeaderFilter.X_CONTENT_TYPE_HEADER));
        EasyMock.expectLastCall().once();
        servletResponse.setHeader(org.apache.ambari.server.security.AbstractSecurityHeaderFilter.CACHE_CONTROL_HEADER, defatulPropertyValueMap.get(org.apache.ambari.server.security.AbstractSecurityHeaderFilter.CACHE_CONTROL_HEADER));
        EasyMock.expectLastCall().once();
        servletResponse.setHeader(org.apache.ambari.server.security.AbstractSecurityHeaderFilter.PRAGMA_HEADER, defatulPropertyValueMap.get(org.apache.ambari.server.security.AbstractSecurityHeaderFilter.PRAGMA_HEADER));
        EasyMock.expectLastCall().once();
        servletResponse.setCharacterEncoding(org.apache.ambari.server.configuration.Configuration.HTTP_CHARSET.getDefaultValue());
        EasyMock.expectLastCall().once();
        javax.servlet.FilterChain filterChain = createStrictMock(javax.servlet.FilterChain.class);
        filterChain.doFilter(servletRequest, servletResponse);
        EasyMock.expectLastCall().once();
        replayAll();
        org.apache.ambari.server.security.AbstractSecurityHeaderFilter securityFilter = injector.getInstance(filterClass);
        junit.framework.Assert.assertNotNull(securityFilter);
        securityFilter.init(filterConfig);
        securityFilter.doFilter(servletRequest, servletResponse, filterChain);
        verifyAll();
    }

    @org.junit.Test
    public void testDoFilter_CustomValuesNoSSL() throws java.lang.Exception {
        final java.io.File httpPassFile = temporaryFolder.newFile();
        com.google.inject.Injector injector = com.google.inject.Guice.createInjector(new com.google.inject.AbstractModule() {
            @java.lang.Override
            protected void configure() {
                java.util.Properties properties = new java.util.Properties();
                properties.setProperty(org.apache.ambari.server.configuration.Configuration.CLIENT_API_SSL_KSTR_DIR_NAME.getKey(), httpPassFile.getParent());
                properties.setProperty(org.apache.ambari.server.configuration.Configuration.CLIENT_API_SSL_CRT_PASS_FILE_NAME.getKey(), httpPassFile.getName());
                properties.setProperty(propertyNameMap.get(org.apache.ambari.server.security.AbstractSecurityHeaderFilter.STRICT_TRANSPORT_HEADER), "custom1");
                properties.setProperty(propertyNameMap.get(org.apache.ambari.server.security.AbstractSecurityHeaderFilter.X_FRAME_OPTIONS_HEADER), "custom2");
                properties.setProperty(propertyNameMap.get(org.apache.ambari.server.security.AbstractSecurityHeaderFilter.X_XSS_PROTECTION_HEADER), "custom3");
                properties.setProperty(propertyNameMap.get(org.apache.ambari.server.security.AbstractSecurityHeaderFilter.X_CONTENT_TYPE_HEADER), "custom4");
                properties.setProperty(propertyNameMap.get(org.apache.ambari.server.security.AbstractSecurityHeaderFilter.CACHE_CONTROL_HEADER), "custom5");
                properties.setProperty(propertyNameMap.get(org.apache.ambari.server.security.AbstractSecurityHeaderFilter.PRAGMA_HEADER), "custom6");
                properties.setProperty(org.apache.ambari.server.configuration.Configuration.HTTP_CHARSET.getKey(), "custom7");
                properties.setProperty(org.apache.ambari.server.configuration.Configuration.VIEWS_HTTP_CHARSET.getKey(), "custom7");
                bind(org.apache.ambari.server.state.stack.OsFamily.class).toInstance(createNiceMock(org.apache.ambari.server.state.stack.OsFamily.class));
                bind(org.apache.ambari.server.configuration.Configuration.class).toInstance(new org.apache.ambari.server.configuration.Configuration(properties));
            }
        });
        javax.servlet.FilterConfig filterConfig = createNiceMock(javax.servlet.FilterConfig.class);
        javax.servlet.http.HttpServletRequest servletRequest = createStrictMock(javax.servlet.http.HttpServletRequest.class);
        expectHttpServletRequestMock(servletRequest);
        javax.servlet.http.HttpServletResponse servletResponse = createStrictMock(javax.servlet.http.HttpServletResponse.class);
        servletResponse.setHeader(org.apache.ambari.server.security.AbstractSecurityHeaderFilter.X_FRAME_OPTIONS_HEADER, "custom2");
        EasyMock.expectLastCall().once();
        servletResponse.setHeader(org.apache.ambari.server.security.AbstractSecurityHeaderFilter.X_XSS_PROTECTION_HEADER, "custom3");
        EasyMock.expectLastCall().once();
        servletResponse.setHeader(org.apache.ambari.server.security.AbstractSecurityHeaderFilter.X_CONTENT_TYPE_HEADER, "custom4");
        EasyMock.expectLastCall().once();
        servletResponse.setHeader(org.apache.ambari.server.security.AbstractSecurityHeaderFilter.CACHE_CONTROL_HEADER, "custom5");
        EasyMock.expectLastCall().once();
        servletResponse.setHeader(org.apache.ambari.server.security.AbstractSecurityHeaderFilter.PRAGMA_HEADER, "custom6");
        EasyMock.expectLastCall().once();
        servletResponse.setCharacterEncoding("custom7");
        EasyMock.expectLastCall().once();
        javax.servlet.FilterChain filterChain = createStrictMock(javax.servlet.FilterChain.class);
        filterChain.doFilter(servletRequest, servletResponse);
        EasyMock.expectLastCall().once();
        replayAll();
        org.apache.ambari.server.security.AbstractSecurityHeaderFilter securityFilter = injector.getInstance(filterClass);
        junit.framework.Assert.assertNotNull(securityFilter);
        securityFilter.init(filterConfig);
        securityFilter.doFilter(servletRequest, servletResponse, filterChain);
        verifyAll();
    }

    @org.junit.Test
    public void testDoFilter_CustomValuesSSL() throws java.lang.Exception {
        final java.io.File httpPassFile = temporaryFolder.newFile();
        com.google.inject.Injector injector = com.google.inject.Guice.createInjector(new com.google.inject.AbstractModule() {
            @java.lang.Override
            protected void configure() {
                java.util.Properties properties = new java.util.Properties();
                properties.setProperty(org.apache.ambari.server.configuration.Configuration.API_USE_SSL.getKey(), "true");
                properties.setProperty(org.apache.ambari.server.configuration.Configuration.CLIENT_API_SSL_KSTR_DIR_NAME.getKey(), httpPassFile.getParent());
                properties.setProperty(org.apache.ambari.server.configuration.Configuration.CLIENT_API_SSL_CRT_PASS_FILE_NAME.getKey(), httpPassFile.getName());
                properties.setProperty(propertyNameMap.get(org.apache.ambari.server.security.AbstractSecurityHeaderFilter.STRICT_TRANSPORT_HEADER), "custom1");
                properties.setProperty(propertyNameMap.get(org.apache.ambari.server.security.AbstractSecurityHeaderFilter.X_FRAME_OPTIONS_HEADER), "custom2");
                properties.setProperty(propertyNameMap.get(org.apache.ambari.server.security.AbstractSecurityHeaderFilter.X_XSS_PROTECTION_HEADER), "custom3");
                properties.setProperty(propertyNameMap.get(org.apache.ambari.server.security.AbstractSecurityHeaderFilter.X_CONTENT_TYPE_HEADER), "custom4");
                properties.setProperty(propertyNameMap.get(org.apache.ambari.server.security.AbstractSecurityHeaderFilter.CACHE_CONTROL_HEADER), "custom5");
                properties.setProperty(propertyNameMap.get(org.apache.ambari.server.security.AbstractSecurityHeaderFilter.PRAGMA_HEADER), "custom6");
                properties.setProperty(org.apache.ambari.server.configuration.Configuration.HTTP_CHARSET.getKey(), "custom7");
                properties.setProperty(org.apache.ambari.server.configuration.Configuration.VIEWS_HTTP_CHARSET.getKey(), "custom7");
                bind(org.apache.ambari.server.state.stack.OsFamily.class).toInstance(createNiceMock(org.apache.ambari.server.state.stack.OsFamily.class));
                bind(org.apache.ambari.server.configuration.Configuration.class).toInstance(new org.apache.ambari.server.configuration.Configuration(properties));
            }
        });
        javax.servlet.FilterConfig filterConfig = createNiceMock(javax.servlet.FilterConfig.class);
        javax.servlet.http.HttpServletRequest servletRequest = createStrictMock(javax.servlet.http.HttpServletRequest.class);
        expectHttpServletRequestMock(servletRequest);
        javax.servlet.http.HttpServletResponse servletResponse = createStrictMock(javax.servlet.http.HttpServletResponse.class);
        servletResponse.setHeader(org.apache.ambari.server.security.AbstractSecurityHeaderFilter.STRICT_TRANSPORT_HEADER, "custom1");
        EasyMock.expectLastCall().once();
        servletResponse.setHeader(org.apache.ambari.server.security.AbstractSecurityHeaderFilter.X_FRAME_OPTIONS_HEADER, "custom2");
        EasyMock.expectLastCall().once();
        servletResponse.setHeader(org.apache.ambari.server.security.AbstractSecurityHeaderFilter.X_XSS_PROTECTION_HEADER, "custom3");
        EasyMock.expectLastCall().once();
        servletResponse.setHeader(org.apache.ambari.server.security.AbstractSecurityHeaderFilter.X_CONTENT_TYPE_HEADER, "custom4");
        EasyMock.expectLastCall().once();
        servletResponse.setHeader(org.apache.ambari.server.security.AbstractSecurityHeaderFilter.CACHE_CONTROL_HEADER, "custom5");
        EasyMock.expectLastCall().once();
        servletResponse.setHeader(org.apache.ambari.server.security.AbstractSecurityHeaderFilter.PRAGMA_HEADER, "custom6");
        EasyMock.expectLastCall().once();
        servletResponse.setCharacterEncoding("custom7");
        EasyMock.expectLastCall().once();
        javax.servlet.FilterChain filterChain = createStrictMock(javax.servlet.FilterChain.class);
        filterChain.doFilter(servletRequest, servletResponse);
        EasyMock.expectLastCall().once();
        replayAll();
        org.apache.ambari.server.security.AbstractSecurityHeaderFilter securityFilter = injector.getInstance(filterClass);
        junit.framework.Assert.assertNotNull(securityFilter);
        securityFilter.init(filterConfig);
        securityFilter.doFilter(servletRequest, servletResponse, filterChain);
        verifyAll();
    }

    @org.junit.Test
    public void testDoFilter_EmptyValuesNoSSL() throws java.lang.Exception {
        final java.io.File httpPassFile = temporaryFolder.newFile();
        com.google.inject.Injector injector = com.google.inject.Guice.createInjector(new com.google.inject.AbstractModule() {
            @java.lang.Override
            protected void configure() {
                java.util.Properties properties = new java.util.Properties();
                properties.setProperty(org.apache.ambari.server.configuration.Configuration.CLIENT_API_SSL_KSTR_DIR_NAME.getKey(), httpPassFile.getParent());
                properties.setProperty(org.apache.ambari.server.configuration.Configuration.CLIENT_API_SSL_CRT_PASS_FILE_NAME.getKey(), httpPassFile.getName());
                properties.setProperty(org.apache.ambari.server.configuration.Configuration.HTTP_CHARSET.getKey(), "");
                properties.setProperty(org.apache.ambari.server.configuration.Configuration.VIEWS_HTTP_CHARSET.getKey(), "");
                properties.setProperty(propertyNameMap.get(org.apache.ambari.server.security.AbstractSecurityHeaderFilter.STRICT_TRANSPORT_HEADER), "");
                properties.setProperty(propertyNameMap.get(org.apache.ambari.server.security.AbstractSecurityHeaderFilter.X_FRAME_OPTIONS_HEADER), "");
                properties.setProperty(propertyNameMap.get(org.apache.ambari.server.security.AbstractSecurityHeaderFilter.X_XSS_PROTECTION_HEADER), "");
                properties.setProperty(propertyNameMap.get(org.apache.ambari.server.security.AbstractSecurityHeaderFilter.X_CONTENT_TYPE_HEADER), "");
                properties.setProperty(propertyNameMap.get(org.apache.ambari.server.security.AbstractSecurityHeaderFilter.CACHE_CONTROL_HEADER), "");
                properties.setProperty(propertyNameMap.get(org.apache.ambari.server.security.AbstractSecurityHeaderFilter.PRAGMA_HEADER), "");
                bind(org.apache.ambari.server.state.stack.OsFamily.class).toInstance(createNiceMock(org.apache.ambari.server.state.stack.OsFamily.class));
                bind(org.apache.ambari.server.configuration.Configuration.class).toInstance(new org.apache.ambari.server.configuration.Configuration(properties));
            }
        });
        javax.servlet.FilterConfig filterConfig = createNiceMock(javax.servlet.FilterConfig.class);
        javax.servlet.http.HttpServletRequest servletRequest = createStrictMock(javax.servlet.http.HttpServletRequest.class);
        expectHttpServletRequestMock(servletRequest);
        javax.servlet.http.HttpServletResponse servletResponse = createStrictMock(javax.servlet.http.HttpServletResponse.class);
        javax.servlet.FilterChain filterChain = createStrictMock(javax.servlet.FilterChain.class);
        filterChain.doFilter(servletRequest, servletResponse);
        EasyMock.expectLastCall().once();
        replayAll();
        org.apache.ambari.server.security.AbstractSecurityHeaderFilter securityFilter = injector.getInstance(filterClass);
        junit.framework.Assert.assertNotNull(securityFilter);
        securityFilter.init(filterConfig);
        securityFilter.doFilter(servletRequest, servletResponse, filterChain);
        verifyAll();
    }

    @org.junit.Test
    public void testDoFilter_EmptyValuesSSL() throws java.lang.Exception {
        final java.io.File httpPassFile = temporaryFolder.newFile();
        com.google.inject.Injector injector = com.google.inject.Guice.createInjector(new com.google.inject.AbstractModule() {
            @java.lang.Override
            protected void configure() {
                java.util.Properties properties = new java.util.Properties();
                properties.setProperty(org.apache.ambari.server.configuration.Configuration.API_USE_SSL.getKey(), "true");
                properties.setProperty(org.apache.ambari.server.configuration.Configuration.CLIENT_API_SSL_KSTR_DIR_NAME.getKey(), httpPassFile.getParent());
                properties.setProperty(org.apache.ambari.server.configuration.Configuration.CLIENT_API_SSL_CRT_PASS_FILE_NAME.getKey(), httpPassFile.getName());
                properties.setProperty(org.apache.ambari.server.configuration.Configuration.HTTP_CHARSET.getKey(), "");
                properties.setProperty(org.apache.ambari.server.configuration.Configuration.VIEWS_HTTP_CHARSET.getKey(), "");
                properties.setProperty(propertyNameMap.get(org.apache.ambari.server.security.AbstractSecurityHeaderFilter.STRICT_TRANSPORT_HEADER), "");
                properties.setProperty(propertyNameMap.get(org.apache.ambari.server.security.AbstractSecurityHeaderFilter.X_FRAME_OPTIONS_HEADER), "");
                properties.setProperty(propertyNameMap.get(org.apache.ambari.server.security.AbstractSecurityHeaderFilter.X_XSS_PROTECTION_HEADER), "");
                properties.setProperty(propertyNameMap.get(org.apache.ambari.server.security.AbstractSecurityHeaderFilter.X_CONTENT_TYPE_HEADER), "");
                properties.setProperty(propertyNameMap.get(org.apache.ambari.server.security.AbstractSecurityHeaderFilter.CACHE_CONTROL_HEADER), "");
                properties.setProperty(propertyNameMap.get(org.apache.ambari.server.security.AbstractSecurityHeaderFilter.PRAGMA_HEADER), "");
                bind(org.apache.ambari.server.state.stack.OsFamily.class).toInstance(createNiceMock(org.apache.ambari.server.state.stack.OsFamily.class));
                bind(org.apache.ambari.server.configuration.Configuration.class).toInstance(new org.apache.ambari.server.configuration.Configuration(properties));
            }
        });
        javax.servlet.FilterConfig filterConfig = createNiceMock(javax.servlet.FilterConfig.class);
        javax.servlet.http.HttpServletRequest servletRequest = createStrictMock(javax.servlet.http.HttpServletRequest.class);
        expectHttpServletRequestMock(servletRequest);
        javax.servlet.http.HttpServletResponse servletResponse = createStrictMock(javax.servlet.http.HttpServletResponse.class);
        javax.servlet.FilterChain filterChain = createStrictMock(javax.servlet.FilterChain.class);
        filterChain.doFilter(servletRequest, servletResponse);
        EasyMock.expectLastCall().once();
        replayAll();
        org.apache.ambari.server.security.AbstractSecurityHeaderFilter securityFilter = injector.getInstance(filterClass);
        junit.framework.Assert.assertNotNull(securityFilter);
        securityFilter.init(filterConfig);
        securityFilter.doFilter(servletRequest, servletResponse, filterChain);
        verifyAll();
    }
}