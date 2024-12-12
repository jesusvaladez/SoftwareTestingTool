package org.apache.ambari.server.security.authorization;
import javax.persistence.EntityManager;
import org.easymock.EasyMock;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.easymock.EasyMock.createMockBuilder;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
public class AmbariAuthorizationFilterTest {
    @org.junit.After
    public void clearAuthentication() {
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(null);
    }

    @org.junit.Test
    public void testDoFilter_adminAccess() throws java.lang.Exception {
        final com.google.common.collect.Table<java.lang.String, java.lang.String, java.lang.Boolean> urlTests = com.google.common.collect.HashBasedTable.create();
        urlTests.put("/api/v1/clusters/cluster", "GET", true);
        urlTests.put("/api/v1/clusters/cluster", "POST", true);
        urlTests.put("/api/v1/clusters/cluster/", "GET", true);
        urlTests.put("/api/v1/clusters/cluster/", "POST", true);
        urlTests.put("/api/v1/views", "GET", true);
        urlTests.put("/api/v1/views", "POST", true);
        urlTests.put("/api/v1/persist/SomeValue", "GET", true);
        urlTests.put("/api/v1/persist/SomeValue", "POST", true);
        urlTests.put("/api/v1/clusters/c1/credentials/ambari.credential", "POST", true);
        urlTests.put("/api/v1/clusters/c1/credentials/ambari.credential", "PUT", true);
        urlTests.put("/api/v1/clusters/c1/credentials/ambari.credential", "GET", true);
        urlTests.put("/api/v1/clusters/c1/credentials/ambari.credential", "DELETE", true);
        urlTests.put("/api/v1/clusters/c1/credentials/cluster.credential", "POST", true);
        urlTests.put("/api/v1/clusters/c1/credentials/cluster.credential", "PUT", true);
        urlTests.put("/api/v1/clusters/c1/credentials/cluster.credential", "GET", true);
        urlTests.put("/api/v1/clusters/c1/credentials/cluster.credential", "DELETE", true);
        urlTests.put("/api/v1/clusters/c1/config_groups", "GET", true);
        urlTests.put("/api/v1/clusters/c1/config_groups", "PUT", true);
        urlTests.put("/api/v1/clusters/c1/config_groups", "POST", true);
        urlTests.put("/api/v1/clusters/c1/config_groups", "DELETE", true);
        urlTests.put("/api/v1/clusters/c1/configurations", "GET", true);
        urlTests.put("/api/v1/clusters/c1/configurations", "PUT", true);
        urlTests.put("/api/v1/clusters/c1/configurations", "POST", true);
        urlTests.put("/api/v1/clusters/c1/configurations", "DELETE", true);
        urlTests.put("/views/AllowedView/SomeVersion/SomeInstance", "GET", true);
        urlTests.put("/views/AllowedView/SomeVersion/SomeInstance", "POST", true);
        urlTests.put("/views/DeniedView/AnotherVersion/AnotherInstance", "GET", true);
        urlTests.put("/views/DeniedView/AnotherVersion/AnotherInstance", "POST", true);
        urlTests.put("/api/v1/users/user1", "GET", true);
        urlTests.put("/api/v1/users/user1", "POST", true);
        urlTests.put("/api/v1/users/user2", "GET", true);
        urlTests.put("/api/v1/users/user2", "POST", true);
        urlTests.put("/api/v1/groups", "GET", true);
        urlTests.put("/api/v1/ldap_sync_events", "GET", true);
        urlTests.put("/any/other/URL", "GET", true);
        urlTests.put("/any/other/URL", "POST", true);
        performGeneralDoFilterTest(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator(), urlTests, false);
    }

    @org.junit.Test
    public void testDoFilter_clusterViewerAccess() throws java.lang.Exception {
        final com.google.common.collect.Table<java.lang.String, java.lang.String, java.lang.Boolean> urlTests = com.google.common.collect.HashBasedTable.create();
        urlTests.put("/api/v1/clusters/cluster", "GET", true);
        urlTests.put("/api/v1/clusters/cluster", "POST", true);
        urlTests.put("/api/v1/clusters/cluster/", "GET", true);
        urlTests.put("/api/v1/clusters/cluster/", "POST", true);
        urlTests.put("/api/v1/views", "GET", true);
        urlTests.put("/api/v1/views", "POST", true);
        urlTests.put("/api/v1/persist/SomeValue", "GET", true);
        urlTests.put("/api/v1/persist/SomeValue", "POST", true);
        urlTests.put("/api/v1/clusters/c1/credentials/ambari.credential", "POST", true);
        urlTests.put("/api/v1/clusters/c1/credentials/ambari.credential", "PUT", true);
        urlTests.put("/api/v1/clusters/c1/credentials/ambari.credential", "GET", true);
        urlTests.put("/api/v1/clusters/c1/credentials/ambari.credential", "DELETE", true);
        urlTests.put("/api/v1/clusters/c1/credentials/cluster.credential", "POST", true);
        urlTests.put("/api/v1/clusters/c1/credentials/cluster.credential", "PUT", true);
        urlTests.put("/api/v1/clusters/c1/credentials/cluster.credential", "GET", true);
        urlTests.put("/api/v1/clusters/c1/credentials/cluster.credential", "DELETE", true);
        urlTests.put("/api/v1/clusters/c1/config_groups", "GET", true);
        urlTests.put("/api/v1/clusters/c1/config_groups", "PUT", true);
        urlTests.put("/api/v1/clusters/c1/config_groups", "POST", true);
        urlTests.put("/api/v1/clusters/c1/config_groups", "DELETE", true);
        urlTests.put("/api/v1/clusters/c1/configurations", "GET", true);
        urlTests.put("/api/v1/clusters/c1/configurations", "PUT", true);
        urlTests.put("/api/v1/clusters/c1/configurations", "POST", true);
        urlTests.put("/api/v1/clusters/c1/configurations", "DELETE", true);
        urlTests.put("/views/AllowedView/SomeVersion/SomeInstance", "GET", true);
        urlTests.put("/views/AllowedView/SomeVersion/SomeInstance", "POST", true);
        urlTests.put("/views/DeniedView/AnotherVersion/AnotherInstance", "GET", true);
        urlTests.put("/views/DeniedView/AnotherVersion/AnotherInstance", "POST", true);
        urlTests.put("/api/v1/users/user1", "GET", true);
        urlTests.put("/api/v1/users/user1", "POST", true);
        urlTests.put("/api/v1/users/user2", "GET", true);
        urlTests.put("/api/v1/users/user2", "POST", true);
        urlTests.put("/api/v1/groups", "GET", true);
        urlTests.put("/api/v1/ldap_sync_events", "GET", false);
        urlTests.put("/any/other/URL", "GET", true);
        urlTests.put("/any/other/URL", "POST", false);
        performGeneralDoFilterTest(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterUser(), urlTests, false);
    }

    @org.junit.Test
    public void testDoFilter_clusterOperatorAccess() throws java.lang.Exception {
        final com.google.common.collect.Table<java.lang.String, java.lang.String, java.lang.Boolean> urlTests = com.google.common.collect.HashBasedTable.create();
        urlTests.put("/api/v1/clusters/cluster", "GET", true);
        urlTests.put("/api/v1/clusters/cluster", "POST", true);
        urlTests.put("/api/v1/clusters/cluster/", "GET", true);
        urlTests.put("/api/v1/clusters/cluster/", "POST", true);
        urlTests.put("/api/v1/views", "GET", true);
        urlTests.put("/api/v1/views", "POST", true);
        urlTests.put("/api/v1/persist/SomeValue", "GET", true);
        urlTests.put("/api/v1/persist/SomeValue", "POST", true);
        urlTests.put("/api/v1/clusters/c1/credentials/ambari.credential", "POST", true);
        urlTests.put("/api/v1/clusters/c1/credentials/ambari.credential", "PUT", true);
        urlTests.put("/api/v1/clusters/c1/credentials/ambari.credential", "GET", true);
        urlTests.put("/api/v1/clusters/c1/credentials/ambari.credential", "DELETE", true);
        urlTests.put("/api/v1/clusters/c1/credentials/cluster.credential", "POST", true);
        urlTests.put("/api/v1/clusters/c1/credentials/cluster.credential", "PUT", true);
        urlTests.put("/api/v1/clusters/c1/credentials/cluster.credential", "GET", true);
        urlTests.put("/api/v1/clusters/c1/credentials/cluster.credential", "DELETE", true);
        urlTests.put("/api/v1/clusters/c1/config_groups", "GET", true);
        urlTests.put("/api/v1/clusters/c1/config_groups", "PUT", true);
        urlTests.put("/api/v1/clusters/c1/config_groups", "POST", true);
        urlTests.put("/api/v1/clusters/c1/config_groups", "DELETE", true);
        urlTests.put("/api/v1/clusters/c1/configurations", "GET", true);
        urlTests.put("/api/v1/clusters/c1/configurations", "PUT", true);
        urlTests.put("/api/v1/clusters/c1/configurations", "POST", true);
        urlTests.put("/api/v1/clusters/c1/configurations", "DELETE", true);
        urlTests.put("/views/AllowedView/SomeVersion/SomeInstance", "GET", true);
        urlTests.put("/views/AllowedView/SomeVersion/SomeInstance", "POST", true);
        urlTests.put("/views/DeniedView/AnotherVersion/AnotherInstance", "GET", true);
        urlTests.put("/views/DeniedView/AnotherVersion/AnotherInstance", "POST", true);
        urlTests.put("/api/v1/users/user1", "GET", true);
        urlTests.put("/api/v1/users/user1", "POST", true);
        urlTests.put("/api/v1/users/user2", "GET", true);
        urlTests.put("/api/v1/users/user2", "POST", true);
        urlTests.put("/api/v1/groups", "GET", true);
        urlTests.put("/api/v1/ldap_sync_events", "GET", false);
        urlTests.put("/api/v1/clusters/c1/widgets", "GET", true);
        urlTests.put("/api/v1/clusters/c1/widgets", "PUT", true);
        urlTests.put("/api/v1/clusters/c1/widgets", "POST", true);
        urlTests.put("/any/other/URL", "GET", true);
        urlTests.put("/any/other/URL", "POST", false);
        performGeneralDoFilterTest(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterOperator(), urlTests, false);
    }

    @org.junit.Test
    public void testDoFilter_viewUserAccess() throws java.lang.Exception {
        final com.google.common.collect.Table<java.lang.String, java.lang.String, java.lang.Boolean> urlTests = com.google.common.collect.HashBasedTable.create();
        urlTests.put("/api/v1/clusters/cluster", "GET", true);
        urlTests.put("/api/v1/clusters/cluster", "POST", true);
        urlTests.put("/api/v1/clusters/cluster/", "GET", true);
        urlTests.put("/api/v1/clusters/cluster/", "POST", true);
        urlTests.put("/api/v1/views", "GET", true);
        urlTests.put("/api/v1/views", "POST", true);
        urlTests.put("/api/v1/persist/SomeValue", "GET", true);
        urlTests.put("/api/v1/persist/SomeValue", "POST", true);
        urlTests.put("/api/v1/clusters/c1/credentials/ambari.credential", "POST", true);
        urlTests.put("/api/v1/clusters/c1/credentials/ambari.credential", "PUT", true);
        urlTests.put("/api/v1/clusters/c1/credentials/ambari.credential", "GET", true);
        urlTests.put("/api/v1/clusters/c1/credentials/ambari.credential", "DELETE", true);
        urlTests.put("/api/v1/clusters/c1/credentials/cluster.credential", "POST", true);
        urlTests.put("/api/v1/clusters/c1/credentials/cluster.credential", "PUT", true);
        urlTests.put("/api/v1/clusters/c1/credentials/cluster.credential", "GET", true);
        urlTests.put("/api/v1/clusters/c1/credentials/cluster.credential", "DELETE", true);
        urlTests.put("/api/v1/clusters/c1/config_groups", "GET", true);
        urlTests.put("/api/v1/clusters/c1/config_groups", "PUT", true);
        urlTests.put("/api/v1/clusters/c1/config_groups", "POST", true);
        urlTests.put("/api/v1/clusters/c1/config_groups", "DELETE", true);
        urlTests.put("/api/v1/clusters/c1/configurations", "GET", true);
        urlTests.put("/api/v1/clusters/c1/configurations", "PUT", true);
        urlTests.put("/api/v1/clusters/c1/configurations", "POST", true);
        urlTests.put("/api/v1/clusters/c1/configurations", "DELETE", true);
        urlTests.put("/views/AllowedView/SomeVersion/SomeInstance", "GET", true);
        urlTests.put("/views/AllowedView/SomeVersion/SomeInstance", "POST", true);
        urlTests.put("/views/DeniedView/AnotherVersion/AnotherInstance", "GET", true);
        urlTests.put("/views/DeniedView/AnotherVersion/AnotherInstance", "POST", true);
        urlTests.put("/api/v1/users/user1", "GET", true);
        urlTests.put("/api/v1/users/user1", "POST", true);
        urlTests.put("/api/v1/users/user2", "GET", true);
        urlTests.put("/api/v1/users/user2", "POST", true);
        urlTests.put("/api/v1/groups", "GET", true);
        urlTests.put("/api/v1/ldap_sync_events", "GET", false);
        urlTests.put("/any/other/URL", "GET", true);
        urlTests.put("/any/other/URL", "POST", false);
        performGeneralDoFilterTest(org.apache.ambari.server.security.TestAuthenticationFactory.createViewUser(99L), urlTests, false);
    }

    @org.junit.Test
    public void testDoFilter_userNoPermissionsAccess() throws java.lang.Exception {
        final com.google.common.collect.Table<java.lang.String, java.lang.String, java.lang.Boolean> urlTests = com.google.common.collect.HashBasedTable.create();
        urlTests.put("/api/v1/clusters/cluster", "GET", true);
        urlTests.put("/api/v1/clusters/cluster", "POST", true);
        urlTests.put("/api/v1/clusters/cluster/", "GET", true);
        urlTests.put("/api/v1/clusters/cluster/", "POST", true);
        urlTests.put("/api/v1/views", "GET", true);
        urlTests.put("/api/v1/views", "POST", true);
        urlTests.put("/api/v1/persist/SomeValue", "GET", true);
        urlTests.put("/api/v1/persist/SomeValue", "POST", true);
        urlTests.put("/api/v1/clusters/c1/credentials/ambari.credential", "POST", true);
        urlTests.put("/api/v1/clusters/c1/credentials/ambari.credential", "PUT", true);
        urlTests.put("/api/v1/clusters/c1/credentials/ambari.credential", "GET", true);
        urlTests.put("/api/v1/clusters/c1/credentials/ambari.credential", "DELETE", true);
        urlTests.put("/api/v1/clusters/c1/credentials/cluster.credential", "POST", true);
        urlTests.put("/api/v1/clusters/c1/credentials/cluster.credential", "PUT", true);
        urlTests.put("/api/v1/clusters/c1/credentials/cluster.credential", "GET", true);
        urlTests.put("/api/v1/clusters/c1/credentials/cluster.credential", "DELETE", true);
        urlTests.put("/api/v1/clusters/c1/config_groups", "GET", true);
        urlTests.put("/api/v1/clusters/c1/config_groups", "PUT", true);
        urlTests.put("/api/v1/clusters/c1/config_groups", "POST", true);
        urlTests.put("/api/v1/clusters/c1/config_groups", "DELETE", true);
        urlTests.put("/api/v1/clusters/c1/configurations", "GET", true);
        urlTests.put("/api/v1/clusters/c1/configurations", "PUT", true);
        urlTests.put("/api/v1/clusters/c1/configurations", "POST", true);
        urlTests.put("/api/v1/clusters/c1/configurations", "DELETE", true);
        urlTests.put("/views/AllowedView/SomeVersion/SomeInstance", "GET", true);
        urlTests.put("/views/AllowedView/SomeVersion/SomeInstance", "POST", true);
        urlTests.put("/views/DeniedView/AnotherVersion/AnotherInstance", "GET", true);
        urlTests.put("/views/DeniedView/AnotherVersion/AnotherInstance", "POST", true);
        urlTests.put("/api/v1/users/user1", "GET", true);
        urlTests.put("/api/v1/users/user1", "POST", true);
        urlTests.put("/api/v1/users/user2", "GET", true);
        urlTests.put("/api/v1/users/user2", "POST", true);
        urlTests.put("/any/other/URL", "GET", true);
        urlTests.put("/any/other/URL", "POST", false);
        performGeneralDoFilterTest(org.apache.ambari.server.security.TestAuthenticationFactory.createViewUser(null), urlTests, false);
    }

    @org.junit.Test
    public void testDoFilter_viewNotLoggedIn() throws java.lang.Exception {
        final com.google.common.collect.Table<java.lang.String, java.lang.String, java.lang.Boolean> urlTests = com.google.common.collect.HashBasedTable.create();
        urlTests.put("/views/SomeView/SomeVersion/SomeInstance", "GET", false);
        urlTests.put("/views/SomeView/SomeVersion/SomeInstance?foo=bar", "GET", false);
        performGeneralDoFilterTest(null, urlTests, true);
    }

    @org.junit.Test
    public void testDoFilter_stackAdvisorCalls() throws java.lang.Exception {
        final com.google.common.collect.Table<java.lang.String, java.lang.String, java.lang.Boolean> urlTests = com.google.common.collect.HashBasedTable.create();
        urlTests.put("/api/v1/stacks/HDP/versions/2.3/validations", "POST", true);
        urlTests.put("/api/v1/stacks/HDP/versions/2.3/recommendations", "POST", true);
        performGeneralDoFilterTest(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator(), urlTests, false);
        performGeneralDoFilterTest(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterUser(), urlTests, false);
        performGeneralDoFilterTest(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator(), urlTests, false);
    }

    @org.junit.Test
    public void testDoFilter_NotLoggedIn_UseDefaultUser() throws java.lang.Exception {
        final javax.servlet.FilterChain chain = org.easymock.EasyMock.createStrictMock(javax.servlet.FilterChain.class);
        final javax.servlet.http.HttpServletResponse response = EasyMock.createNiceMock(javax.servlet.http.HttpServletResponse.class);
        final javax.servlet.http.HttpServletRequest request = EasyMock.createNiceMock(javax.servlet.http.HttpServletRequest.class);
        EasyMock.expect(request.getRequestURI()).andReturn("/uri").anyTimes();
        EasyMock.expect(request.getQueryString()).andReturn(null).anyTimes();
        EasyMock.expect(request.getMethod()).andReturn("GET").anyTimes();
        chain.doFilter(org.easymock.EasyMock.anyObject(), org.easymock.EasyMock.anyObject());
        org.easymock.EasyMock.expectLastCall().once();
        final org.apache.ambari.server.configuration.Configuration configuration = org.easymock.EasyMock.createMock(org.apache.ambari.server.configuration.Configuration.class);
        EasyMock.expect(configuration.getDefaultApiAuthenticatedUser()).andReturn("user1").once();
        org.apache.ambari.server.security.authorization.User user = org.easymock.EasyMock.createMock(org.apache.ambari.server.security.authorization.User.class);
        EasyMock.expect(user.getUserName()).andReturn("user1").anyTimes();
        final org.apache.ambari.server.security.authorization.Users users = org.easymock.EasyMock.createMock(org.apache.ambari.server.security.authorization.Users.class);
        EasyMock.expect(users.getUser("user1")).andReturn(user).once();
        EasyMock.expect(users.getUserAuthorities("user1")).andReturn(java.util.Collections.<org.apache.ambari.server.security.authorization.AmbariGrantedAuthority>emptyList()).once();
        EasyMock.replay(request, response, chain, configuration, users, user);
        com.google.inject.Injector injector = com.google.inject.Guice.createInjector(new com.google.inject.AbstractModule() {
            @java.lang.Override
            protected void configure() {
                bind(org.apache.ambari.server.configuration.Configuration.class).toInstance(configuration);
                bind(org.apache.ambari.server.security.authorization.Users.class).toInstance(users);
                bind(javax.persistence.EntityManager.class).toInstance(org.easymock.EasyMock.createMock(javax.persistence.EntityManager.class));
                bind(org.apache.ambari.server.orm.dao.UserDAO.class).toInstance(org.easymock.EasyMock.createMock(org.apache.ambari.server.orm.dao.UserDAO.class));
                bind(org.apache.ambari.server.orm.DBAccessor.class).toInstance(org.easymock.EasyMock.createMock(org.apache.ambari.server.orm.DBAccessor.class));
                bind(org.springframework.security.crypto.password.PasswordEncoder.class).toInstance(org.easymock.EasyMock.createMock(org.springframework.security.crypto.password.PasswordEncoder.class));
                bind(org.apache.ambari.server.state.stack.OsFamily.class).toInstance(org.easymock.EasyMock.createMock(org.apache.ambari.server.state.stack.OsFamily.class));
                bind(org.apache.ambari.server.audit.AuditLogger.class).toInstance(org.easymock.EasyMock.createNiceMock(org.apache.ambari.server.audit.AuditLogger.class));
                bind(org.apache.ambari.server.hooks.HookService.class).toInstance(org.easymock.EasyMock.createMock(org.apache.ambari.server.hooks.HookService.class));
                bind(org.apache.ambari.server.hooks.HookContextFactory.class).toInstance(org.easymock.EasyMock.createMock(org.apache.ambari.server.hooks.HookContextFactory.class));
                bind(new com.google.inject.TypeLiteral<org.apache.ambari.server.security.encryption.Encryptor<org.apache.ambari.server.configuration.AmbariServerConfiguration>>() {}).annotatedWith(com.google.inject.name.Names.named("AmbariServerConfigurationEncryptor")).toInstance(org.apache.ambari.server.security.encryption.Encryptor.NONE);
                bind(org.apache.ambari.server.ldap.service.AmbariLdapConfigurationProvider.class).toInstance(org.easymock.EasyMock.createMock(org.apache.ambari.server.ldap.service.AmbariLdapConfigurationProvider.class));
            }
        });
        org.apache.ambari.server.security.authorization.AmbariAuthorizationFilter filter = new org.apache.ambari.server.security.authorization.AmbariAuthorizationFilter(EasyMock.createNiceMock(org.apache.ambari.server.security.AmbariEntryPoint.class), injector.getInstance(org.apache.ambari.server.configuration.Configuration.class), injector.getInstance(org.apache.ambari.server.security.authorization.Users.class), injector.getInstance(org.apache.ambari.server.audit.AuditLogger.class), injector.getInstance(org.apache.ambari.server.security.authorization.PermissionHelper.class));
        injector.injectMembers(filter);
        filter.doFilter(request, response, chain);
        junit.framework.Assert.assertEquals("user1", org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication().getName());
    }

    private void performGeneralDoFilterTest(org.springframework.security.core.Authentication authentication, com.google.common.collect.Table<java.lang.String, java.lang.String, java.lang.Boolean> urlTests, boolean expectRedirect) throws java.lang.Exception {
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        final javax.servlet.FilterConfig filterConfig = EasyMock.createNiceMock(javax.servlet.FilterConfig.class);
        final org.apache.ambari.server.configuration.Configuration configuration = org.easymock.EasyMock.createMock(org.apache.ambari.server.configuration.Configuration.class);
        EasyMock.expect(configuration.getDefaultApiAuthenticatedUser()).andReturn(null).anyTimes();
        final org.apache.ambari.server.audit.AuditLogger auditLogger = org.easymock.EasyMock.createNiceMock(org.apache.ambari.server.audit.AuditLogger.class);
        EasyMock.expect(auditLogger.isEnabled()).andReturn(false).anyTimes();
        final org.apache.ambari.server.security.authorization.AmbariAuthorizationFilter filter = EasyMock.createMockBuilder(org.apache.ambari.server.security.authorization.AmbariAuthorizationFilter.class).addMockedMethod("getSecurityContext").addMockedMethod("getViewRegistry").withConstructor(EasyMock.createNiceMock(org.apache.ambari.server.security.AmbariEntryPoint.class), configuration, EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.Users.class), auditLogger, EasyMock.createNiceMock(org.apache.ambari.server.security.authorization.PermissionHelper.class)).createMock();
        final org.apache.ambari.server.view.ViewRegistry viewRegistry = EasyMock.createNiceMock(org.apache.ambari.server.view.ViewRegistry.class);
        EasyMock.expect(filterConfig.getInitParameter("realm")).andReturn("AuthFilter").anyTimes();
        EasyMock.expect(filter.getSecurityContext()).andReturn(org.springframework.security.core.context.SecurityContextHolder.getContext()).anyTimes();
        EasyMock.expect(filter.getViewRegistry()).andReturn(viewRegistry).anyTimes();
        EasyMock.expect(viewRegistry.checkPermission(org.easymock.EasyMock.eq("DeniedView"), org.easymock.EasyMock.anyObject(), org.easymock.EasyMock.anyObject(), org.easymock.EasyMock.anyBoolean())).andReturn(false).anyTimes();
        EasyMock.replay(filterConfig, filter, viewRegistry, configuration, auditLogger);
        for (final com.google.common.collect.Table.Cell<java.lang.String, java.lang.String, java.lang.Boolean> urlTest : urlTests.cellSet()) {
            final javax.servlet.FilterChain chain = org.easymock.EasyMock.createStrictMock(javax.servlet.FilterChain.class);
            final javax.servlet.http.HttpServletRequest request = EasyMock.createNiceMock(javax.servlet.http.HttpServletRequest.class);
            final javax.servlet.http.HttpServletResponse response = EasyMock.createNiceMock(javax.servlet.http.HttpServletResponse.class);
            java.lang.String URI = urlTest.getRowKey();
            java.lang.String[] URIParts = URI.split("\\?");
            EasyMock.expect(request.getRequestURI()).andReturn(URIParts[0]).anyTimes();
            EasyMock.expect(request.getQueryString()).andReturn(URIParts.length == 2 ? URIParts[1] : null).anyTimes();
            EasyMock.expect(request.getMethod()).andReturn(urlTest.getColumnKey()).anyTimes();
            if (expectRedirect) {
                java.lang.String redirectURL = org.apache.ambari.server.security.authorization.AmbariAuthorizationFilter.LOGIN_REDIRECT_BASE + urlTest.getRowKey();
                EasyMock.expect(response.encodeRedirectURL(redirectURL)).andReturn(redirectURL);
                response.sendRedirect(redirectURL);
            }
            if (urlTest.getValue()) {
                chain.doFilter(org.easymock.EasyMock.anyObject(), org.easymock.EasyMock.anyObject());
                org.easymock.EasyMock.expectLastCall().once();
            }
            EasyMock.replay(request, response, chain);
            try {
                filter.doFilter(request, response, chain);
            } catch (java.lang.AssertionError error) {
                throw new java.lang.Exception((("doFilter() should not be chained on " + urlTest.getColumnKey()) + " ") + urlTest.getRowKey(), error);
            }
            try {
                EasyMock.verify(chain);
                if (expectRedirect) {
                    EasyMock.verify(response);
                }
            } catch (java.lang.AssertionError error) {
                throw new java.lang.Exception((("verify( failed on " + urlTest.getColumnKey()) + " ") + urlTest.getRowKey(), error);
            }
        }
    }
}