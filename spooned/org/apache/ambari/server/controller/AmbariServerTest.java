package org.apache.ambari.server.controller;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import org.apache.velocity.app.Velocity;
import org.easymock.EasyMock;
import org.easymock.EasyMockSupport;
import org.eclipse.jetty.servlets.GzipFilter;
import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.anyString;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.partialMockBuilder;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
public class AmbariServerTest {
    private com.google.inject.Injector injector;

    @org.junit.Before
    public void setup() throws java.lang.Exception {
        injector = com.google.inject.Guice.createInjector(new org.apache.ambari.server.orm.InMemoryDefaultTestModule());
        injector.getInstance(org.apache.ambari.server.orm.GuiceJpaInitializer.class);
        injector.injectMembers(this);
    }

    @org.junit.After
    public void teardown() throws org.apache.ambari.server.AmbariException, java.sql.SQLException {
        org.apache.ambari.server.H2DatabaseCleaner.clearDatabaseAndStopPersistenceService(injector);
    }

    @org.junit.Test
    public void testVelocityLogger() throws java.lang.Exception {
        new org.apache.ambari.server.controller.AmbariServer();
        org.junit.Assert.assertEquals(org.apache.ambari.server.controller.AmbariServer.VELOCITY_LOG_CATEGORY, org.apache.velocity.app.Velocity.getProperty("runtime.log.logsystem.log4j.logger"));
    }

    @org.junit.Test
    public void testConfigureSessionManager() throws java.lang.Exception {
        org.apache.ambari.server.controller.SessionHandlerConfigurer sessionHandlerConfigurer = new org.apache.ambari.server.controller.SessionHandlerConfigurer();
        org.apache.ambari.server.configuration.Configuration configuration = EasyMock.createNiceMock(org.apache.ambari.server.configuration.Configuration.class);
        org.eclipse.jetty.server.session.SessionHandler sessionHandler = EasyMock.createNiceMock(org.eclipse.jetty.server.session.SessionHandler.class);
        javax.servlet.SessionCookieConfig sessionCookieConfig = EasyMock.createNiceMock(javax.servlet.SessionCookieConfig.class);
        sessionHandlerConfigurer.configuration = configuration;
        EasyMock.expect(sessionHandler.getSessionCookieConfig()).andReturn(sessionCookieConfig).anyTimes();
        EasyMock.expect(configuration.getApiSSLAuthentication()).andReturn(false);
        sessionCookieConfig.setHttpOnly(true);
        EasyMock.expect(configuration.getApiSSLAuthentication()).andReturn(true);
        sessionCookieConfig.setHttpOnly(true);
        sessionCookieConfig.setSecure(true);
        EasyMock.replay(configuration, sessionHandler, sessionCookieConfig);
        sessionHandlerConfigurer.configureSessionHandler(sessionHandler);
        sessionHandlerConfigurer.configureSessionHandler(sessionHandler);
        EasyMock.verify(configuration, sessionHandler, sessionCookieConfig);
    }

    @org.junit.Test
    public void testSystemProperties() throws java.lang.Exception {
        org.apache.ambari.server.configuration.Configuration configuration = org.easymock.EasyMock.createNiceMock(org.apache.ambari.server.configuration.Configuration.class);
        EasyMock.expect(configuration.getServerTempDir()).andReturn("/ambari/server/temp/dir").anyTimes();
        EasyMock.replay(configuration);
        org.apache.ambari.server.controller.AmbariServer.setSystemProperties(configuration);
        org.junit.Assert.assertEquals(java.lang.System.getProperty("java.io.tmpdir"), "/ambari/server/temp/dir");
    }

    @org.junit.Test
    public void testProxyUser() throws java.lang.Exception {
        java.net.PasswordAuthentication pa = java.net.Authenticator.requestPasswordAuthentication(java.net.InetAddress.getLocalHost(), 80, null, null, null);
        org.junit.Assert.assertNull(pa);
        java.lang.System.setProperty("http.proxyUser", "abc");
        java.lang.System.setProperty("http.proxyPassword", "def");
        org.apache.ambari.server.controller.AmbariServer.setupProxyAuth();
        pa = java.net.Authenticator.requestPasswordAuthentication(java.net.InetAddress.getLocalHost(), 80, null, null, null);
        org.junit.Assert.assertNotNull(pa);
        org.junit.Assert.assertEquals("abc", pa.getUserName());
        org.junit.Assert.assertArrayEquals("def".toCharArray(), pa.getPassword());
    }

    @org.junit.Test
    public void testConfigureRootHandler() throws java.lang.Exception {
        final org.eclipse.jetty.servlet.ServletContextHandler handler = org.easymock.EasyMock.createNiceMock(org.eclipse.jetty.servlet.ServletContextHandler.class);
        final org.eclipse.jetty.servlet.FilterHolder filter = org.easymock.EasyMock.createNiceMock(org.eclipse.jetty.servlet.FilterHolder.class);
        handler.setMaxFormContentSize(-1);
        org.easymock.EasyMock.expectLastCall().once();
        org.easymock.EasyMock.expect(handler.addFilter(org.eclipse.jetty.servlets.GzipFilter.class, "/*", java.util.EnumSet.of(javax.servlet.DispatcherType.REQUEST))).andReturn(filter).once();
        org.easymock.EasyMock.expect(handler.getMimeTypes()).andReturn(new org.eclipse.jetty.http.MimeTypes()).anyTimes();
        EasyMock.replay(handler, filter);
        injector.getInstance(org.apache.ambari.server.controller.AmbariServer.class).configureRootHandler(handler);
        org.easymock.EasyMock.verify(handler);
    }

    @org.junit.Test
    public void testConfigureCompression() throws java.lang.Exception {
        final org.eclipse.jetty.servlet.ServletContextHandler handler = org.easymock.EasyMock.createNiceMock(org.eclipse.jetty.servlet.ServletContextHandler.class);
        final org.eclipse.jetty.servlet.FilterHolder filter = org.easymock.EasyMock.createNiceMock(org.eclipse.jetty.servlet.FilterHolder.class);
        org.easymock.EasyMock.expect(handler.addFilter(org.eclipse.jetty.servlets.GzipFilter.class, "/*", java.util.EnumSet.of(javax.servlet.DispatcherType.REQUEST))).andReturn(filter).once();
        filter.setInitParameter(EasyMock.anyObject(java.lang.String.class), EasyMock.anyObject(java.lang.String.class));
        org.easymock.EasyMock.expectLastCall().times(3);
        EasyMock.replay(handler, filter);
        injector.getInstance(org.apache.ambari.server.controller.AmbariServer.class).configureHandlerCompression(handler);
        org.easymock.EasyMock.verify(handler);
    }

    @org.junit.Test
    public void testConfigureContentTypes() throws java.lang.Exception {
        org.eclipse.jetty.servlet.ServletContextHandler handler = org.easymock.EasyMock.createNiceMock(org.eclipse.jetty.servlet.ServletContextHandler.class);
        org.eclipse.jetty.servlet.FilterHolder filter = org.easymock.EasyMock.createNiceMock(org.eclipse.jetty.servlet.FilterHolder.class);
        org.eclipse.jetty.http.MimeTypes expectedMimeTypes = new org.eclipse.jetty.http.MimeTypes();
        org.easymock.EasyMock.expect(handler.getMimeTypes()).andReturn(expectedMimeTypes).anyTimes();
        org.easymock.EasyMock.expect(handler.addFilter(EasyMock.isA(java.lang.Class.class), EasyMock.anyString(), EasyMock.isA(java.util.EnumSet.class))).andReturn(filter).anyTimes();
        EasyMock.replay(handler, filter);
        injector.getInstance(org.apache.ambari.server.controller.AmbariServer.class).configureRootHandler(handler);
        junit.framework.Assert.assertEquals("application/font-woff", expectedMimeTypes.getMimeByExtension("/file.woff").toString());
        junit.framework.Assert.assertEquals("application/font-sfnt", expectedMimeTypes.getMimeByExtension("/file.ttf").toString());
        org.easymock.EasyMock.verify(handler);
    }

    @org.junit.Test
    public void testJettyThreadPoolCalculation() throws java.lang.Exception {
        org.eclipse.jetty.server.Server server = new org.eclipse.jetty.server.Server();
        org.apache.ambari.server.controller.AmbariServer ambariServer = new org.apache.ambari.server.controller.AmbariServer();
        server = ambariServer.configureJettyThreadPool(12, "mock-pool", 25);
        org.junit.Assert.assertEquals(44, ((org.eclipse.jetty.util.thread.QueuedThreadPool) (server.getThreadPool())).getMaxThreads());
        server = ambariServer.configureJettyThreadPool(2, "mock-pool", 25);
        org.junit.Assert.assertEquals(25, ((org.eclipse.jetty.util.thread.QueuedThreadPool) (server.getThreadPool())).getMaxThreads());
        server = ambariServer.configureJettyThreadPool(16, "mock-pool", 35);
        org.junit.Assert.assertEquals(52, ((org.eclipse.jetty.util.thread.QueuedThreadPool) (server.getThreadPool())).getMaxThreads());
    }

    @org.junit.Test
    public void testRunDatabaseConsistencyCheck() throws java.lang.Exception {
        org.easymock.EasyMockSupport easyMockSupport = new org.easymock.EasyMockSupport();
        final org.apache.ambari.server.api.services.AmbariMetaInfo mockAmbariMetainfo = easyMockSupport.createNiceMock(org.apache.ambari.server.api.services.AmbariMetaInfo.class);
        final org.apache.ambari.server.orm.DBAccessor mockDBDbAccessor = easyMockSupport.createNiceMock(org.apache.ambari.server.orm.DBAccessor.class);
        final java.sql.Connection mockConnection = easyMockSupport.createNiceMock(java.sql.Connection.class);
        final java.sql.Statement mockStatement = easyMockSupport.createNiceMock(java.sql.Statement.class);
        final org.apache.ambari.server.state.stack.OsFamily mockOSFamily = easyMockSupport.createNiceMock(org.apache.ambari.server.state.stack.OsFamily.class);
        final javax.persistence.EntityManager mockEntityManager = easyMockSupport.createNiceMock(javax.persistence.EntityManager.class);
        final org.apache.ambari.server.state.Clusters mockClusters = easyMockSupport.createNiceMock(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.controller.AmbariServer ambariServer = new org.apache.ambari.server.controller.AmbariServer();
        final org.apache.ambari.server.configuration.Configuration mockConfiguration = EasyMock.partialMockBuilder(org.apache.ambari.server.configuration.Configuration.class).withConstructor().addMockedMethod("getDatabaseType").createMock();
        final javax.persistence.TypedQuery mockQuery = easyMockSupport.createNiceMock(javax.persistence.TypedQuery.class);
        EasyMock.expect(mockConfiguration.getDatabaseType()).andReturn(null).anyTimes();
        EasyMock.expect(mockEntityManager.createNamedQuery(EasyMock.anyString(), EasyMock.anyObject(java.lang.Class.class))).andReturn(mockQuery);
        EasyMock.expect(mockQuery.getResultList()).andReturn(new java.util.ArrayList());
        EasyMock.replay(mockConfiguration);
        final com.google.inject.Injector mockInjector = createMockInjector(mockAmbariMetainfo, mockDBDbAccessor, mockOSFamily, mockEntityManager, mockClusters, mockConfiguration);
        EasyMock.expect(mockDBDbAccessor.getConnection()).andReturn(mockConnection).atLeastOnce();
        EasyMock.expect(mockConnection.createStatement(java.sql.ResultSet.TYPE_SCROLL_SENSITIVE, java.sql.ResultSet.CONCUR_UPDATABLE)).andReturn(mockStatement).atLeastOnce();
        EasyMock.expect(mockStatement.executeQuery(EasyMock.anyString())).andReturn(null).atLeastOnce();
        org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.setInjector(mockInjector);
        easyMockSupport.replayAll();
        mockAmbariMetainfo.init();
        ambariServer.runDatabaseConsistencyCheck();
        easyMockSupport.verifyAll();
    }

    @org.junit.Test
    public void testRunDatabaseConsistencyCheck_IgnoreDBCheck() throws java.lang.Exception {
        org.apache.ambari.server.controller.AmbariServer ambariServer = new org.apache.ambari.server.controller.AmbariServer();
        java.lang.System.setProperty("skipDatabaseConsistencyCheck", "");
        final com.google.inject.Injector mockInjector = com.google.inject.Guice.createInjector(new com.google.inject.AbstractModule() {
            @java.lang.Override
            protected void configure() {
            }
        });
        org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.setInjector(mockInjector);
        ambariServer.runDatabaseConsistencyCheck();
        java.lang.System.clearProperty("skipDatabaseConsistencyCheck");
    }

    @org.junit.Test
    public void testRunDatabaseConsistencyCheck_ThrowException() throws java.lang.Exception {
        org.easymock.EasyMockSupport easyMockSupport = new org.easymock.EasyMockSupport();
        final org.apache.ambari.server.api.services.AmbariMetaInfo mockAmbariMetainfo = easyMockSupport.createNiceMock(org.apache.ambari.server.api.services.AmbariMetaInfo.class);
        final org.apache.ambari.server.orm.DBAccessor mockDBDbAccessor = easyMockSupport.createNiceMock(org.apache.ambari.server.orm.DBAccessor.class);
        final org.apache.ambari.server.state.stack.OsFamily mockOSFamily = easyMockSupport.createNiceMock(org.apache.ambari.server.state.stack.OsFamily.class);
        final javax.persistence.EntityManager mockEntityManager = easyMockSupport.createNiceMock(javax.persistence.EntityManager.class);
        final org.apache.ambari.server.state.Clusters mockClusters = easyMockSupport.createNiceMock(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.controller.AmbariServer ambariServer = new org.apache.ambari.server.controller.AmbariServer();
        final org.apache.ambari.server.configuration.Configuration mockConfiguration = EasyMock.partialMockBuilder(org.apache.ambari.server.configuration.Configuration.class).withConstructor().addMockedMethod("getDatabaseType").createMock();
        final javax.persistence.TypedQuery mockQuery = easyMockSupport.createNiceMock(javax.persistence.TypedQuery.class);
        EasyMock.expect(mockConfiguration.getDatabaseType()).andReturn(null).anyTimes();
        EasyMock.expect(mockEntityManager.createNamedQuery(EasyMock.anyString(), EasyMock.anyObject(java.lang.Class.class))).andReturn(mockQuery);
        EasyMock.expect(mockQuery.getResultList()).andReturn(new java.util.ArrayList());
        EasyMock.replay(mockConfiguration);
        final com.google.inject.Injector mockInjector = createMockInjector(mockAmbariMetainfo, mockDBDbAccessor, mockOSFamily, mockEntityManager, mockClusters, mockConfiguration);
        EasyMock.expect(mockDBDbAccessor.getConnection()).andReturn(null);
        org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.setInjector(mockInjector);
        easyMockSupport.replayAll();
        mockAmbariMetainfo.init();
        boolean errorOccurred = false;
        try {
            ambariServer.runDatabaseConsistencyCheck();
        } catch (java.lang.Exception e) {
            errorOccurred = true;
        }
        junit.framework.Assert.assertTrue(errorOccurred);
        easyMockSupport.verifyAll();
    }

    private com.google.inject.Injector createMockInjector(final org.apache.ambari.server.api.services.AmbariMetaInfo mockAmbariMetainfo, final org.apache.ambari.server.orm.DBAccessor mockDBDbAccessor, final org.apache.ambari.server.state.stack.OsFamily mockOSFamily, final javax.persistence.EntityManager mockEntityManager, final org.apache.ambari.server.state.Clusters mockClusters, final org.apache.ambari.server.configuration.Configuration mockConfiguration) {
        return com.google.inject.Guice.createInjector(new com.google.inject.AbstractModule() {
            @java.lang.Override
            protected void configure() {
                org.apache.ambari.server.testutils.PartialNiceMockBinder.newBuilder().addClustersBinding().addLdapBindings().build().configure(binder());
                bind(org.apache.ambari.server.stack.StackManagerFactory.class).toInstance(EasyMock.createNiceMock(org.apache.ambari.server.stack.StackManagerFactory.class));
                bind(org.apache.ambari.server.mpack.MpackManagerFactory.class).toInstance(EasyMock.createNiceMock(org.apache.ambari.server.mpack.MpackManagerFactory.class));
                bind(org.apache.ambari.server.api.services.AmbariMetaInfo.class).toInstance(mockAmbariMetainfo);
                bind(org.apache.ambari.server.orm.DBAccessor.class).toInstance(mockDBDbAccessor);
                bind(org.apache.ambari.server.state.stack.OsFamily.class).toInstance(mockOSFamily);
                bind(javax.persistence.EntityManager.class).toInstance(mockEntityManager);
                bind(org.apache.ambari.server.state.Clusters.class).toInstance(mockClusters);
                bind(org.apache.ambari.server.configuration.Configuration.class).toInstance(mockConfiguration);
            }
        });
    }
}