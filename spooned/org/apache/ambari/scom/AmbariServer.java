package org.apache.ambari.scom;
import com.google.inject.persist.Transactional;
import com.google.inject.persist.jpa.JpaPersistModule;
import com.sun.jersey.spi.container.servlet.ServletContainer;
import org.apache.ambari.server.security.authorization.AmbariLocalUserDetailsService;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.server.ssl.SslSelectChannelConnector;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
@com.google.inject.Singleton
public class AmbariServer {
    private org.eclipse.jetty.server.Server server = null;

    @com.google.inject.Inject
    org.apache.ambari.server.configuration.Configuration configuration;

    @com.google.inject.Inject
    com.google.inject.Injector injector;

    static {
        java.lang.System.setProperty("provider.module.class", "org.apache.ambari.scom.SQLProviderModule");
    }

    private static final java.lang.String CONTEXT_PATH = "/";

    private static final java.lang.String SPRING_CONTEXT_LOCATION = "classpath:META-INF/spring-security.xml";

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.scom.AmbariServer.class);

    public static void main(java.lang.String[] args) throws java.lang.Exception {
        com.google.inject.Injector injector = com.google.inject.Guice.createInjector(new org.apache.ambari.scom.AmbariServer.ControllerModule());
        injector.getInstance(org.apache.ambari.server.orm.GuiceJpaInitializer.class);
        org.apache.ambari.scom.AmbariServer ambariServer = null;
        try {
            org.apache.ambari.scom.AmbariServer.LOG.info("Getting the controller");
            ambariServer = injector.getInstance(org.apache.ambari.scom.AmbariServer.class);
            org.apache.ambari.server.configuration.ComponentSSLConfiguration.instance().init(ambariServer.configuration);
            org.apache.ambari.scom.SinkConnectionFactory.instance().init(ambariServer.configuration);
            org.apache.ambari.scom.ClusterDefinitionProvider.instance().init(ambariServer.configuration);
            if (ambariServer != null) {
                ambariServer.run();
            }
        } catch (java.lang.Throwable t) {
            org.apache.ambari.scom.AmbariServer.LOG.error("Failed to run the Ambari Server", t);
            if (ambariServer != null) {
                ambariServer.stop();
            }
            java.lang.System.exit(-1);
        }
    }

    private void run() throws java.lang.Exception {
        addInMemoryUsers();
        server = new org.eclipse.jetty.server.Server();
        try {
            org.springframework.context.support.ClassPathXmlApplicationContext parentSpringAppContext = new org.springframework.context.support.ClassPathXmlApplicationContext();
            parentSpringAppContext.refresh();
            org.springframework.beans.factory.config.ConfigurableListableBeanFactory factory = parentSpringAppContext.getBeanFactory();
            factory.registerSingleton("guiceInjector", injector);
            factory.registerSingleton("passwordEncoder", injector.getInstance(org.springframework.security.crypto.password.PasswordEncoder.class));
            factory.registerSingleton("ambariLocalUserService", injector.getInstance(org.apache.ambari.server.security.authorization.AmbariLocalUserDetailsService.class));
            factory.registerSingleton("ambariLdapAuthenticationProvider", injector.getInstance(org.apache.ambari.server.security.authorization.AmbariLdapAuthenticationProvider.class));
            java.lang.String[] contextLocations = new java.lang.String[]{ org.apache.ambari.scom.AmbariServer.SPRING_CONTEXT_LOCATION };
            org.springframework.context.support.ClassPathXmlApplicationContext springAppContext = new org.springframework.context.support.ClassPathXmlApplicationContext(contextLocations, parentSpringAppContext);
            org.eclipse.jetty.servlet.ServletContextHandler root = new org.eclipse.jetty.servlet.ServletContextHandler(server, org.apache.ambari.scom.AmbariServer.CONTEXT_PATH, org.eclipse.jetty.servlet.ServletContextHandler.SECURITY | org.eclipse.jetty.servlet.ServletContextHandler.SESSIONS);
            root.getSessionHandler().getSessionManager().setSessionCookie("AMBARISESSIONID");
            org.springframework.web.context.support.GenericWebApplicationContext springWebAppContext = new org.springframework.web.context.support.GenericWebApplicationContext();
            springWebAppContext.setServletContext(root.getServletContext());
            springWebAppContext.setParent(springAppContext);
            root.setResourceBase(configuration.getWebAppDir());
            root.getServletContext().setAttribute(org.springframework.web.context.WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, springWebAppContext);
            org.eclipse.jetty.servlet.ServletHolder rootServlet = root.addServlet(org.eclipse.jetty.servlet.DefaultServlet.class, "/");
            rootServlet.setInitOrder(1);
            org.springframework.web.filter.DelegatingFilterProxy springSecurityFilter = new org.springframework.web.filter.DelegatingFilterProxy();
            springSecurityFilter.setTargetBeanName("springSecurityFilterChain");
            root.addFilter(new org.eclipse.jetty.servlet.FilterHolder(injector.getInstance(org.apache.ambari.server.api.AmbariPersistFilter.class)), "/api/*", 1);
            if (configuration.getApiAuthentication()) {
                root.addFilter(new org.eclipse.jetty.servlet.FilterHolder(springSecurityFilter), "/api/*", 1);
            }
            org.eclipse.jetty.server.ssl.SslSelectChannelConnector sslConnectorTwoWay = new org.eclipse.jetty.server.ssl.SslSelectChannelConnector();
            sslConnectorTwoWay.setPort(configuration.getTwoWayAuthPort());
            java.util.Map<java.lang.String, java.lang.String> configsMap = configuration.getConfigsMap();
            java.lang.String keystore = (configsMap.get(org.apache.ambari.server.configuration.Configuration.SRVR_KSTR_DIR_KEY) + java.io.File.separator) + configsMap.get(org.apache.ambari.server.configuration.Configuration.KSTR_NAME_KEY);
            java.lang.String srvrCrtPass = configsMap.get(org.apache.ambari.server.configuration.Configuration.SRVR_CRT_PASS_KEY);
            sslConnectorTwoWay.setKeystore(keystore);
            sslConnectorTwoWay.setTruststore(keystore);
            sslConnectorTwoWay.setPassword(srvrCrtPass);
            sslConnectorTwoWay.setKeyPassword(srvrCrtPass);
            sslConnectorTwoWay.setTrustPassword(srvrCrtPass);
            sslConnectorTwoWay.setKeystoreType("PKCS12");
            sslConnectorTwoWay.setTruststoreType("PKCS12");
            sslConnectorTwoWay.setNeedClientAuth(configuration.getTwoWaySsl());
            org.eclipse.jetty.util.ssl.SslContextFactory contextFactory = new org.eclipse.jetty.util.ssl.SslContextFactory(true);
            contextFactory.setKeyStorePath(keystore);
            contextFactory.setTrustStore(keystore);
            contextFactory.setKeyStorePassword(srvrCrtPass);
            contextFactory.setKeyManagerPassword(srvrCrtPass);
            contextFactory.setTrustStorePassword(srvrCrtPass);
            contextFactory.setKeyStoreType("PKCS12");
            contextFactory.setTrustStoreType("PKCS12");
            contextFactory.setNeedClientAuth(false);
            org.eclipse.jetty.server.ssl.SslSelectChannelConnector sslConnectorOneWay = new org.eclipse.jetty.server.ssl.SslSelectChannelConnector(contextFactory);
            sslConnectorOneWay.setPort(configuration.getOneWayAuthPort());
            sslConnectorOneWay.setAcceptors(2);
            sslConnectorTwoWay.setAcceptors(2);
            org.eclipse.jetty.servlet.ServletHolder sh = new org.eclipse.jetty.servlet.ServletHolder(com.sun.jersey.spi.container.servlet.ServletContainer.class);
            sh.setInitParameter("com.sun.jersey.config.property.resourceConfigClass", "com.sun.jersey.api.core.PackagesResourceConfig");
            sh.setInitParameter("com.sun.jersey.config.property.packages", "org.apache.ambari.server.api.rest;" + (("org.apache.ambari.server.api.services;" + "org.apache.ambari.eventdb.webservice;") + "org.apache.ambari.server.api"));
            sh.setInitParameter("com.sun.jersey.api.json.POJOMappingFeature", "true");
            root.addServlet(sh, "/api/v1/*");
            sh.setInitOrder(2);
            server.setThreadPool(new org.eclipse.jetty.util.thread.QueuedThreadPool(25));
            org.eclipse.jetty.server.nio.SelectChannelConnector apiConnector;
            if (configuration.getApiSSLAuthentication()) {
                java.lang.String httpsKeystore = (configsMap.get(org.apache.ambari.server.configuration.Configuration.CLIENT_API_SSL_KSTR_DIR_NAME_KEY) + java.io.File.separator) + configsMap.get(org.apache.ambari.server.configuration.Configuration.CLIENT_API_SSL_KSTR_NAME_KEY);
                org.apache.ambari.scom.AmbariServer.LOG.info("API SSL Authentication is turned on. Keystore - " + httpsKeystore);
                java.lang.String httpsCrtPass = configsMap.get(org.apache.ambari.server.configuration.Configuration.CLIENT_API_SSL_CRT_PASS_KEY);
                org.eclipse.jetty.server.ssl.SslSelectChannelConnector sapiConnector = new org.eclipse.jetty.server.ssl.SslSelectChannelConnector();
                sapiConnector.setPort(configuration.getClientSSLApiPort());
                sapiConnector.setKeystore(httpsKeystore);
                sapiConnector.setTruststore(httpsKeystore);
                sapiConnector.setPassword(httpsCrtPass);
                sapiConnector.setKeyPassword(httpsCrtPass);
                sapiConnector.setTrustPassword(httpsCrtPass);
                sapiConnector.setKeystoreType("PKCS12");
                sapiConnector.setTruststoreType("PKCS12");
                sapiConnector.setMaxIdleTime(configuration.getConnectionMaxIdleTime());
                apiConnector = sapiConnector;
            } else {
                apiConnector = new org.eclipse.jetty.server.nio.SelectChannelConnector();
                apiConnector.setPort(configuration.getClientApiPort());
                apiConnector.setMaxIdleTime(configuration.getConnectionMaxIdleTime());
            }
            server.addConnector(apiConnector);
            server.setStopAtShutdown(true);
            springAppContext.start();
            java.lang.String osType = configuration.getServerOsType();
            if ((osType == null) || osType.isEmpty()) {
                throw new java.lang.RuntimeException((org.apache.ambari.server.configuration.Configuration.OS_VERSION_KEY + " is not ") + " set in the ambari.properties file");
            }
            server.start();
            org.apache.ambari.scom.AmbariServer.LOG.info("********* Started Server **********");
            server.join();
            org.apache.ambari.scom.AmbariServer.LOG.info("Joined the Server");
        } catch (javax.crypto.BadPaddingException bpe) {
            org.apache.ambari.scom.AmbariServer.LOG.error("Bad keystore or private key password. " + "HTTPS certificate re-importing may be required.");
            throw bpe;
        } catch (java.net.BindException bindException) {
            org.apache.ambari.scom.AmbariServer.LOG.error("Could not bind to server port - instance may already be running. " + "Terminating this instance.", bindException);
            throw bindException;
        }
    }

    @com.google.inject.persist.Transactional
    void addInMemoryUsers() {
        if ((org.apache.ambari.scom.AmbariServer.getPersistenceType(configuration) == org.apache.ambari.server.orm.PersistenceType.IN_MEMORY) && configuration.getApiAuthentication()) {
            org.apache.ambari.scom.AmbariServer.LOG.info("In-memory database is used - creating default users");
            org.apache.ambari.server.security.authorization.Users users = injector.getInstance(org.apache.ambari.server.security.authorization.Users.class);
            try {
                users.createUser("admin", "admin", true, true, false);
                users.createUser("user", "user", true, false, false);
            } catch (org.apache.ambari.server.AmbariException e) {
                throw new java.lang.RuntimeException(e);
            }
        }
    }

    private void stop() throws java.lang.Exception {
        try {
            server.stop();
        } catch (java.lang.Exception e) {
            org.apache.ambari.scom.AmbariServer.LOG.error("Error stopping the server", e);
        }
    }

    private static org.apache.ambari.server.orm.PersistenceType getPersistenceType(org.apache.ambari.server.configuration.Configuration configuration) {
        java.lang.String value = configuration.getProperty(org.apache.ambari.server.configuration.Configuration.SERVER_PERSISTENCE_TYPE_KEY);
        return value == null ? org.apache.ambari.server.orm.PersistenceType.IN_MEMORY : org.apache.ambari.server.orm.PersistenceType.fromString(value);
    }

    private static class ControllerModule extends com.google.inject.AbstractModule {
        private final org.apache.ambari.server.configuration.Configuration configuration;

        private final org.apache.ambari.server.controller.HostsMap hostsMap;

        public ControllerModule() {
            configuration = new org.apache.ambari.server.configuration.Configuration();
            hostsMap = new org.apache.ambari.server.controller.HostsMap(configuration);
        }

        @java.lang.Override
        protected void configure() {
            bind(org.apache.ambari.server.configuration.Configuration.class).toInstance(configuration);
            bind(org.apache.ambari.server.controller.HostsMap.class).toInstance(hostsMap);
            bind(org.springframework.security.crypto.password.PasswordEncoder.class).toInstance(new org.springframework.security.crypto.password.StandardPasswordEncoder());
            install(buildJpaPersistModule());
            bind(com.google.gson.Gson.class).in(com.google.inject.Scopes.SINGLETON);
        }

        private com.google.inject.persist.jpa.JpaPersistModule buildJpaPersistModule() {
            org.apache.ambari.server.orm.PersistenceType persistenceType = org.apache.ambari.scom.AmbariServer.getPersistenceType(configuration);
            com.google.inject.persist.jpa.JpaPersistModule jpaPersistModule = new com.google.inject.persist.jpa.JpaPersistModule(org.apache.ambari.server.configuration.Configuration.JDBC_UNIT_NAME);
            java.util.Properties properties = new java.util.Properties();
            java.lang.String databaseDriver;
            java.lang.String databaseUrl;
            if (persistenceType == org.apache.ambari.server.orm.PersistenceType.LOCAL) {
                databaseDriver = configuration.getLocalDatabaseUrl();
                databaseUrl = org.apache.ambari.server.configuration.Configuration.JDBC_LOCAL_DRIVER;
            } else if (persistenceType == org.apache.ambari.server.orm.PersistenceType.IN_MEMORY) {
                databaseDriver = org.apache.ambari.server.configuration.Configuration.JDBC_IN_MEMROY_DRIVER;
                databaseUrl = org.apache.ambari.server.configuration.Configuration.JDBC_IN_MEMORY_URL;
            } else {
                databaseDriver = configuration.getDatabaseDriver();
                databaseUrl = configuration.getDatabaseUrl();
            }
            if ((databaseDriver != null) && (databaseUrl != null)) {
                properties.setProperty("javax.persistence.jdbc.url", databaseUrl);
                properties.setProperty("javax.persistence.jdbc.driver", databaseDriver);
                properties.setProperty("eclipselink.logging.level", "INFO");
                properties.setProperty("eclipselink.logging.logger", "org.apache.ambari.scom.logging.JpaLogger");
                java.util.Map<java.lang.String, java.lang.String> custom = configuration.getDatabaseCustomProperties();
                if (0 != custom.size()) {
                    for (java.util.Map.Entry<java.lang.String, java.lang.String> entry : custom.entrySet()) {
                        properties.setProperty("eclipselink.jdbc.property." + entry.getKey(), entry.getValue());
                    }
                }
                if (persistenceType == org.apache.ambari.server.orm.PersistenceType.IN_MEMORY) {
                    properties.setProperty("eclipselink.ddl-generation", "drop-and-create-tables");
                    properties.setProperty("eclipselink.orm.throw.exceptions", "true");
                    jpaPersistModule.properties(properties);
                } else {
                    properties.setProperty("javax.persistence.jdbc.user", configuration.getDatabaseUser());
                    properties.setProperty("javax.persistence.jdbc.password", configuration.getProperty(org.apache.ambari.server.configuration.Configuration.SERVER_JDBC_USER_PASSWD_KEY));
                    switch (configuration.getJPATableGenerationStrategy()) {
                        case CREATE :
                            properties.setProperty("eclipselink.ddl-generation", "create-tables");
                            break;
                        case DROP_AND_CREATE :
                            properties.setProperty("eclipselink.ddl-generation", "drop-and-create-tables");
                            break;
                        default :
                            break;
                    }
                    properties.setProperty("eclipselink.ddl-generation.output-mode", "both");
                    properties.setProperty("eclipselink.create-ddl-jdbc-file-name", "DDL-create.jdbc");
                    properties.setProperty("eclipselink.drop-ddl-jdbc-file-name", "DDL-drop.jdbc");
                    jpaPersistModule.properties(properties);
                }
            }
            return jpaPersistModule;
        }
    }
}