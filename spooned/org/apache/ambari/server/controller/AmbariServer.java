package org.apache.ambari.server.controller;
import com.google.inject.persist.Transactional;
import com.sun.jersey.spi.container.servlet.ServletContainer;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpVersion;
import org.apache.log4j.PropertyConfigurator;
import org.apache.velocity.app.Velocity;
import org.eclipse.jetty.servlets.GzipFilter;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.security.core.context.SecurityContextHolder;
@com.google.inject.Singleton
public class AmbariServer {
    public static final java.lang.String VIEWS_URL_PATTERN = "/api/v1/views/*";

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.controller.AmbariServer.class);

    private static final java.lang.String AGENT_THREAD_POOL_NAME = "qtp-ambari-agent";

    private static final java.lang.String CLIENT_THREAD_POOL_NAME = "ambari-client-thread";

    protected static final java.lang.String VELOCITY_LOG_CATEGORY = "VelocityLogger";

    public static final java.util.EnumSet<javax.servlet.DispatcherType> DISPATCHER_TYPES = java.util.EnumSet.of(javax.servlet.DispatcherType.REQUEST);

    private static final int DEFAULT_ACCEPTORS_COUNT = 1;

    private static final java.lang.String[] DEPRECATED_SSL_PROTOCOLS = new java.lang.String[]{ "TLSv1" };

    static {
        org.apache.velocity.app.Velocity.setProperty("runtime.log.logsystem.log4j.logger", VELOCITY_LOG_CATEGORY);
    }

    private static final java.lang.String CLASSPATH_CHECK_CLASS = "org/apache/ambari/server/controller/AmbariServer.class";

    private static final java.lang.String CLASSPATH_SANITY_CHECK_FAILURE_MESSAGE = "%s class is found in multiple jar files. Possible reasons include multiple ambari server jar files in the ambari classpath.\n" + java.lang.String.format("Check for additional ambari server jar files and check that %s matches only one file.", org.apache.ambari.server.utils.AmbariPath.getPath("/usr/lib/ambari-server/ambari-server*.jar"));

    static {
        java.util.Enumeration<java.net.URL> ambariServerClassUrls;
        try {
            ambariServerClassUrls = org.apache.ambari.server.controller.AmbariServer.class.getClassLoader().getResources(CLASSPATH_CHECK_CLASS);
            int ambariServerClassUrlsSize = 0;
            while (ambariServerClassUrls.hasMoreElements()) {
                ambariServerClassUrlsSize++;
                java.net.URL url = ambariServerClassUrls.nextElement();
                LOG.info(java.lang.String.format("Found %s class in %s", CLASSPATH_CHECK_CLASS, url.getPath()));
            } 
            if (ambariServerClassUrlsSize > 1) {
                throw new java.lang.RuntimeException(java.lang.String.format(CLASSPATH_SANITY_CHECK_FAILURE_MESSAGE, CLASSPATH_CHECK_CLASS));
            }
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }

    private org.eclipse.jetty.server.Server server = null;

    public volatile boolean running = true;

    final java.lang.String CONTEXT_PATH = "/";

    final java.lang.String DISABLED_ENTRIES_SPLITTER = "\\|";

    @com.google.inject.Inject
    org.apache.ambari.server.configuration.Configuration configs;

    @com.google.inject.Inject
    org.apache.ambari.server.security.CertificateManager certMan;

    @com.google.inject.Inject
    com.google.inject.Injector injector;

    @com.google.inject.Inject
    org.apache.ambari.server.api.services.AmbariMetaInfo ambariMetaInfo;

    @com.google.inject.Inject
    org.apache.ambari.server.orm.dao.MetainfoDAO metainfoDAO;

    @com.google.inject.Inject
    @com.google.inject.name.Named("dbInitNeeded")
    boolean dbInitNeeded;

    @com.google.inject.Inject
    private com.google.common.util.concurrent.ServiceManager serviceManager;

    @com.google.inject.Inject
    org.apache.ambari.server.view.ViewRegistry viewRegistry;

    @com.google.inject.Inject
    org.apache.ambari.server.controller.AmbariHandlerList handlerList;

    @com.google.inject.Inject
    org.eclipse.jetty.server.session.SessionHandler sessionHandler;

    @com.google.inject.Inject
    org.springframework.web.filter.DelegatingFilterProxy springSecurityFilter;

    @com.google.inject.Inject
    org.apache.ambari.server.view.ViewDirectoryWatcher viewDirectoryWatcher;

    @com.google.inject.Inject
    org.apache.ambari.server.controller.SessionHandlerConfigurer sessionHandlerConfigurer;

    public java.lang.String getServerOsType() {
        return configs.getServerOsType();
    }

    private static org.apache.ambari.server.controller.AmbariManagementController clusterController = null;

    static void setSystemProperties(org.apache.ambari.server.configuration.Configuration configs) {
        java.lang.System.setProperty("java.io.tmpdir", configs.getServerTempDir());
        if (configs.getJavaVersion() >= 8) {
            java.lang.System.setProperty("jdk.tls.ephemeralDHKeySize", java.lang.String.valueOf(configs.getTlsEphemeralDhKeySize()));
        }
    }

    public static org.apache.ambari.server.controller.AmbariManagementController getController() {
        return org.apache.ambari.server.controller.AmbariServer.clusterController;
    }

    public static void setController(org.apache.ambari.server.controller.AmbariManagementController controller) {
        org.apache.ambari.server.controller.AmbariServer.clusterController = controller;
    }

    @java.lang.SuppressWarnings("deprecation")
    public void run() throws java.lang.Exception {
        setupJulLogging();
        performStaticInjection();
        initDB();
        java.lang.Integer clientAcceptors = (configs.getClientApiAcceptors() != null) ? configs.getClientApiAcceptors() : org.apache.ambari.server.controller.AmbariServer.DEFAULT_ACCEPTORS_COUNT;
        server = configureJettyThreadPool(clientAcceptors, org.apache.ambari.server.controller.AmbariServer.CLIENT_THREAD_POOL_NAME, configs.getClientThreadPoolSize());
        final org.eclipse.jetty.server.SessionIdManager sessionIdManager = new org.eclipse.jetty.server.session.DefaultSessionIdManager(server);
        sessionHandler.setSessionIdManager(sessionIdManager);
        server.setSessionIdManager(sessionIdManager);
        java.lang.Integer agentAcceptors = (configs.getAgentApiAcceptors() != null) ? configs.getAgentApiAcceptors() : org.apache.ambari.server.controller.AmbariServer.DEFAULT_ACCEPTORS_COUNT;
        org.eclipse.jetty.server.Server serverForAgent = configureJettyThreadPool(agentAcceptors * 2, org.apache.ambari.server.controller.AmbariServer.AGENT_THREAD_POOL_NAME, configs.getAgentThreadPoolSize());
        org.apache.ambari.server.controller.AmbariServer.setSystemProperties(configs);
        runDatabaseConsistencyCheck();
        try {
            org.springframework.context.support.ClassPathXmlApplicationContext parentSpringAppContext = new org.springframework.context.support.ClassPathXmlApplicationContext();
            parentSpringAppContext.refresh();
            org.springframework.beans.factory.config.ConfigurableListableBeanFactory factory = parentSpringAppContext.getBeanFactory();
            factory.registerSingleton("injector", injector);
            factory.registerSingleton("ambariUsers", injector.getInstance(org.apache.ambari.server.security.authorization.Users.class));
            org.springframework.web.context.support.AnnotationConfigWebApplicationContext apiContext = new org.springframework.web.context.support.AnnotationConfigWebApplicationContext();
            apiContext.setParent(parentSpringAppContext);
            apiContext.register(org.apache.ambari.server.configuration.spring.ApiSecurityConfig.class);
            org.springframework.web.context.support.AnnotationConfigWebApplicationContext apiDispatcherContext = new org.springframework.web.context.support.AnnotationConfigWebApplicationContext();
            apiDispatcherContext.register(org.apache.ambari.server.configuration.spring.ApiStompConfig.class);
            org.springframework.web.servlet.DispatcherServlet apiDispatcherServlet = new org.springframework.web.servlet.DispatcherServlet(apiDispatcherContext);
            org.springframework.web.context.support.AnnotationConfigWebApplicationContext agentDispatcherContext = new org.springframework.web.context.support.AnnotationConfigWebApplicationContext();
            agentDispatcherContext.register(org.apache.ambari.server.configuration.spring.AgentStompConfig.class);
            org.springframework.web.servlet.DispatcherServlet agentDispatcherServlet = new org.springframework.web.servlet.DispatcherServlet(agentDispatcherContext);
            org.eclipse.jetty.servlet.ServletContextHandler root = new org.eclipse.jetty.servlet.ServletContextHandler(org.eclipse.jetty.servlet.ServletContextHandler.SECURITY | org.eclipse.jetty.servlet.ServletContextHandler.SESSIONS);
            configureRootHandler(root);
            sessionHandlerConfigurer.configureSessionHandler(sessionHandler);
            root.setSessionHandler(sessionHandler);
            root.addEventListener(new org.springframework.web.context.ContextLoaderListener(apiContext));
            certMan.initRootCert();
            org.eclipse.jetty.servlet.ServletContextHandler agentroot = new org.eclipse.jetty.servlet.ServletContextHandler(serverForAgent, "/", org.eclipse.jetty.servlet.ServletContextHandler.NO_SESSIONS);
            org.springframework.web.context.support.AnnotationConfigWebApplicationContext agentApiContext = new org.springframework.web.context.support.AnnotationConfigWebApplicationContext();
            agentApiContext.setParent(parentSpringAppContext);
            if (configs.isAgentApiGzipped()) {
                configureHandlerCompression(agentroot);
            }
            agentroot.addEventListener(new org.springframework.web.context.ContextLoaderListener(agentApiContext));
            org.eclipse.jetty.servlet.ServletHolder rootServlet = root.addServlet(org.eclipse.jetty.servlet.DefaultServlet.class, "/");
            rootServlet.setInitParameter("dirAllowed", "false");
            rootServlet.setInitParameter("precompressed", "gzip=.gz");
            rootServlet.setInitOrder(1);
            rootServlet = agentroot.addServlet(org.eclipse.jetty.servlet.DefaultServlet.class, "/");
            rootServlet.setInitOrder(1);
            root.addFilter(new org.eclipse.jetty.servlet.FilterHolder(injector.getInstance(org.apache.ambari.server.security.AmbariServerSecurityHeaderFilter.class)), "/*", org.apache.ambari.server.controller.AmbariServer.DISPATCHER_TYPES);
            root.addFilter(new org.eclipse.jetty.servlet.FilterHolder(injector.getInstance(org.apache.ambari.server.security.AmbariViewsSecurityHeaderFilter.class)), org.apache.ambari.server.controller.AmbariServer.VIEWS_URL_PATTERN, org.apache.ambari.server.controller.AmbariServer.DISPATCHER_TYPES);
            root.addFilter(new org.eclipse.jetty.servlet.FilterHolder(injector.getInstance(org.apache.ambari.server.view.ViewThrottleFilter.class)), org.apache.ambari.server.controller.AmbariServer.VIEWS_URL_PATTERN, org.apache.ambari.server.controller.AmbariServer.DISPATCHER_TYPES);
            root.addFilter(new org.eclipse.jetty.servlet.FilterHolder(injector.getInstance(org.apache.ambari.server.view.AmbariViewsMDCLoggingFilter.class)), org.apache.ambari.server.controller.AmbariServer.VIEWS_URL_PATTERN, org.apache.ambari.server.controller.AmbariServer.DISPATCHER_TYPES);
            root.addFilter(new org.eclipse.jetty.servlet.FilterHolder(injector.getInstance(org.apache.ambari.server.api.AmbariPersistFilter.class)), "/api/*", org.apache.ambari.server.controller.AmbariServer.DISPATCHER_TYPES);
            root.addFilter(new org.eclipse.jetty.servlet.FilterHolder(new org.apache.ambari.server.api.MethodOverrideFilter()), "/api/*", org.apache.ambari.server.controller.AmbariServer.DISPATCHER_TYPES);
            root.addFilter(new org.eclipse.jetty.servlet.FilterHolder(new org.apache.ambari.server.api.ContentTypeOverrideFilter()), "/api/*", org.apache.ambari.server.controller.AmbariServer.DISPATCHER_TYPES);
            root.addEventListener(new org.springframework.web.context.request.RequestContextListener());
            root.addFilter(new org.eclipse.jetty.servlet.FilterHolder(springSecurityFilter), "/api/*", org.apache.ambari.server.controller.AmbariServer.DISPATCHER_TYPES);
            root.addFilter(new org.eclipse.jetty.servlet.FilterHolder(new org.apache.ambari.server.api.UserNameOverrideFilter()), "/api/v1/users/*", org.apache.ambari.server.controller.AmbariServer.DISPATCHER_TYPES);
            agentroot.addFilter(new org.eclipse.jetty.servlet.FilterHolder(injector.getInstance(org.apache.ambari.server.api.AmbariPersistFilter.class)), "/agent/*", org.apache.ambari.server.controller.AmbariServer.DISPATCHER_TYPES);
            agentroot.addFilter(org.apache.ambari.server.security.SecurityFilter.class, "/*", org.apache.ambari.server.controller.AmbariServer.DISPATCHER_TYPES);
            java.util.Map<java.lang.String, java.lang.String> configsMap = configs.getConfigsMap();
            org.eclipse.jetty.server.ServerConnector agentOneWayConnector = createSelectChannelConnectorForAgent(serverForAgent, configs.getOneWayAuthPort(), false, agentAcceptors);
            org.eclipse.jetty.server.ServerConnector agentTwoWayConnector = createSelectChannelConnectorForAgent(serverForAgent, configs.getTwoWayAuthPort(), configs.isTwoWaySsl(), agentAcceptors);
            serverForAgent.addConnector(agentOneWayConnector);
            serverForAgent.addConnector(agentTwoWayConnector);
            org.eclipse.jetty.servlet.ServletHolder sh = new org.eclipse.jetty.servlet.ServletHolder(com.sun.jersey.spi.container.servlet.ServletContainer.class);
            sh.setInitParameter("com.sun.jersey.config.property.resourceConfigClass", "com.sun.jersey.api.core.PackagesResourceConfig");
            sh.setInitParameter("com.sun.jersey.config.property.packages", "org.apache.ambari.server.api.rest;" + (("org.apache.ambari.server.api.services;" + "org.apache.ambari.eventdb.webservice;") + "org.apache.ambari.server.api"));
            sh.setInitParameter("com.sun.jersey.api.json.POJOMappingFeature", "true");
            root.addServlet(sh, "/api/v1/*");
            sh.setInitOrder(2);
            org.eclipse.jetty.servlet.ServletHolder springDispatcherServlet = new org.eclipse.jetty.servlet.ServletHolder("springDispatcherServlet", apiDispatcherServlet);
            springDispatcherServlet.setInitOrder(3);
            root.addServlet(springDispatcherServlet, "/api/stomp/*");
            org.eclipse.jetty.servlet.ServletHolder agentSpringDispatcherServlet = new org.eclipse.jetty.servlet.ServletHolder("agentSpringDispatcherServlet", agentDispatcherServlet);
            agentSpringDispatcherServlet.setInitOrder(2);
            agentroot.addServlet(agentSpringDispatcherServlet, "/agent/stomp/*");
            org.springframework.security.core.context.SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
            viewRegistry.readViewArchives();
            org.apache.ambari.server.controller.AmbariServer.loadRequestlogHandler(handlerList, serverForAgent, configsMap);
            org.apache.ambari.server.controller.AmbariServer.enableLog4jMonitor(configsMap);
            if (configs.isGzipHandlerEnabledForJetty()) {
                org.eclipse.jetty.server.handler.gzip.GzipHandler gzipHandler = new org.eclipse.jetty.server.handler.gzip.GzipHandler();
                gzipHandler.setHandler(root);
                gzipHandler.setIncludedMimeTypes("text/html", "text/plain", "text/xml", "text/css", "application/javascript", "application/x-javascript", "application/xml", "application/x-www-form-urlencoded", "application/json");
                handlerList.addHandler(gzipHandler);
            } else {
                handlerList.addHandler(root);
            }
            server.setHandler(handlerList);
            org.eclipse.jetty.servlet.ServletHolder agent = new org.eclipse.jetty.servlet.ServletHolder(com.sun.jersey.spi.container.servlet.ServletContainer.class);
            agent.setInitParameter("com.sun.jersey.config.property.resourceConfigClass", "com.sun.jersey.api.core.PackagesResourceConfig");
            agent.setInitParameter("com.sun.jersey.config.property.packages", "org.apache.ambari.server.agent.rest;" + "org.apache.ambari.server.api");
            agent.setInitParameter("com.sun.jersey.api.json.POJOMappingFeature", "true");
            agentroot.addServlet(agent, "/agent/v1/*");
            agent.setInitOrder(3);
            injector.getInstance(org.apache.ambari.server.agent.HeartBeatHandler.class).start();
            org.apache.ambari.server.controller.AmbariServer.LOG.info("********** Started Heartbeat handler **********");
            org.eclipse.jetty.servlet.ServletHolder cert = new org.eclipse.jetty.servlet.ServletHolder(com.sun.jersey.spi.container.servlet.ServletContainer.class);
            cert.setInitParameter("com.sun.jersey.config.property.resourceConfigClass", "com.sun.jersey.api.core.PackagesResourceConfig");
            cert.setInitParameter("com.sun.jersey.config.property.packages", "org.apache.ambari.server.security.unsecured.rest;" + "org.apache.ambari.server.api");
            cert.setInitParameter("com.sun.jersey.api.json.POJOMappingFeature", "true");
            agentroot.addServlet(cert, "/*");
            cert.setInitOrder(4);
            java.io.File resourcesDirectory = new java.io.File(configs.getResourceDirPath());
            org.eclipse.jetty.servlet.ServletHolder resources = new org.eclipse.jetty.servlet.ServletHolder(org.eclipse.jetty.servlet.DefaultServlet.class);
            resources.setInitParameter("resourceBase", resourcesDirectory.getParent());
            resources.setInitParameter("dirAllowed", "false");
            root.addServlet(resources, "/resources/*");
            resources.setInitOrder(5);
            if (configs.csrfProtectionEnabled()) {
                sh.setInitParameter("com.sun.jersey.spi.container.ContainerRequestFilters", "org.apache.ambari.server.api.AmbariCsrfProtectionFilter");
            }
            org.eclipse.jetty.server.ServerConnector apiConnector = createSelectChannelConnectorForClient(server, clientAcceptors);
            server.addConnector(apiConnector);
            server.setStopAtShutdown(true);
            serverForAgent.setStopAtShutdown(true);
            java.lang.String osType = getServerOsType();
            if ((osType == null) || osType.isEmpty()) {
                throw new java.lang.RuntimeException((org.apache.ambari.server.configuration.Configuration.OS_VERSION.getKey() + " is not ") + " set in the ambari.properties file");
            }
            org.apache.ambari.server.controller.AmbariServer.LOG.info("********* Initializing Clusters **********");
            org.apache.ambari.server.state.Clusters clusters = injector.getInstance(org.apache.ambari.server.state.Clusters.class);
            java.lang.StringBuilder clusterDump = new java.lang.StringBuilder();
            org.apache.ambari.server.controller.AmbariServer.LOG.info("********* Current Clusters State *********");
            org.apache.ambari.server.controller.AmbariServer.LOG.info(clusterDump.toString());
            org.apache.ambari.server.controller.AmbariServer.LOG.info("********* Reconciling Alert Definitions **********");
            ambariMetaInfo.reconcileAlertDefinitions(clusters, false);
            org.apache.ambari.server.controller.AmbariServer.LOG.info("********* Initializing ActionManager **********");
            org.apache.ambari.server.actionmanager.ActionManager manager = injector.getInstance(org.apache.ambari.server.actionmanager.ActionManager.class);
            org.apache.ambari.server.controller.AmbariServer.LOG.info("********* Initializing Controller **********");
            org.apache.ambari.server.controller.AmbariManagementController controller = injector.getInstance(org.apache.ambari.server.controller.AmbariManagementController.class);
            org.apache.ambari.server.controller.AmbariServer.LOG.info("********* Initializing Scheduled Request Manager **********");
            org.apache.ambari.server.scheduler.ExecutionScheduleManager executionScheduleManager = injector.getInstance(org.apache.ambari.server.scheduler.ExecutionScheduleManager.class);
            org.apache.ambari.server.metrics.system.MetricsService metricsService = injector.getInstance(org.apache.ambari.server.metrics.system.MetricsService.class);
            org.apache.ambari.server.controller.AmbariServer.clusterController = controller;
            org.apache.ambari.server.StateRecoveryManager recoveryManager = injector.getInstance(org.apache.ambari.server.StateRecoveryManager.class);
            recoveryManager.doWork();
            server.start();
            handlerList.shareSessionCacheToViews(sessionHandler.getSessionCache());
            sessionHandlerConfigurer.configureMaxInactiveInterval(sessionHandler);
            serverForAgent.start();
            org.apache.ambari.server.controller.AmbariServer.LOG.info("********* Started Server **********");
            if (!configs.isViewDirectoryWatcherServiceDisabled()) {
                org.apache.ambari.server.controller.AmbariServer.LOG.info("Starting View Directory Watcher");
                viewDirectoryWatcher.start();
            }
            manager.start();
            org.apache.ambari.server.controller.AmbariServer.LOG.info("********* Started ActionManager **********");
            executionScheduleManager.start();
            org.apache.ambari.server.controller.AmbariServer.LOG.info("********* Started Scheduled Request Manager **********");
            serviceManager.startAsync();
            org.apache.ambari.server.controller.AmbariServer.LOG.info("********* Started Services **********");
            if (!configs.isMetricsServiceDisabled()) {
                metricsService.start();
            } else {
                org.apache.ambari.server.controller.AmbariServer.LOG.info("AmbariServer Metrics disabled.");
            }
            server.join();
            org.apache.ambari.server.controller.AmbariServer.LOG.info("Joined the Server");
        } catch (javax.crypto.BadPaddingException bpe) {
            org.apache.ambari.server.controller.AmbariServer.LOG.error("Bad keystore or private key password. " + "HTTPS certificate re-importing may be required.");
            throw bpe;
        } catch (java.net.BindException bindException) {
            org.apache.ambari.server.controller.AmbariServer.LOG.error("Could not bind to server port - instance may already be running. " + "Terminating this instance.", bindException);
            throw bindException;
        }
    }

    @java.lang.SuppressWarnings("deprecation")
    private org.eclipse.jetty.server.ServerConnector createSelectChannelConnectorForAgent(org.eclipse.jetty.server.Server server, int port, boolean needClientAuth, int acceptors) {
        java.util.Map<java.lang.String, java.lang.String> configsMap = configs.getConfigsMap();
        org.eclipse.jetty.server.ServerConnector agentConnector;
        if (configs.getAgentSSLAuthentication()) {
            java.lang.String keystore = (configsMap.get(org.apache.ambari.server.configuration.Configuration.SRVR_KSTR_DIR.getKey()) + java.io.File.separator) + configsMap.get(org.apache.ambari.server.configuration.Configuration.KSTR_NAME.getKey());
            java.lang.String truststore = (configsMap.get(org.apache.ambari.server.configuration.Configuration.SRVR_KSTR_DIR.getKey()) + java.io.File.separator) + configsMap.get(org.apache.ambari.server.configuration.Configuration.TSTR_NAME.getKey());
            java.lang.String srvrCrtPass = configsMap.get(org.apache.ambari.server.configuration.Configuration.SRVR_CRT_PASS.getKey());
            org.eclipse.jetty.server.HttpConfiguration https_config = new org.eclipse.jetty.server.HttpConfiguration();
            https_config.addCustomizer(new org.eclipse.jetty.server.SecureRequestCustomizer());
            https_config.setRequestHeaderSize(configs.getHttpRequestHeaderSize());
            https_config.setResponseHeaderSize(configs.getHttpResponseHeaderSize());
            https_config.setSendServerVersion(false);
            org.eclipse.jetty.util.ssl.SslContextFactory sslContextFactory = new org.eclipse.jetty.util.ssl.SslContextFactory();
            disableInsecureProtocols(sslContextFactory);
            sslContextFactory.setKeyStorePath(keystore);
            sslContextFactory.setTrustStorePath(truststore);
            sslContextFactory.setKeyStorePassword(srvrCrtPass);
            sslContextFactory.setKeyManagerPassword(srvrCrtPass);
            sslContextFactory.setTrustStorePassword(srvrCrtPass);
            sslContextFactory.setKeyStoreType(configsMap.get(org.apache.ambari.server.configuration.Configuration.KSTR_TYPE.getKey()));
            sslContextFactory.setTrustStoreType(configsMap.get(org.apache.ambari.server.configuration.Configuration.TSTR_TYPE.getKey()));
            sslContextFactory.setNeedClientAuth(needClientAuth);
            org.eclipse.jetty.server.ServerConnector agentSslConnector = new org.eclipse.jetty.server.ServerConnector(server, acceptors, -1, new org.eclipse.jetty.server.SslConnectionFactory(sslContextFactory, HttpVersion.HTTP_1_1.toString()), new org.eclipse.jetty.server.HttpConnectionFactory(https_config));
            agentConnector = agentSslConnector;
        } else {
            agentConnector = new org.eclipse.jetty.server.ServerConnector(server, acceptors, -1);
            agentConnector.setIdleTimeout(configs.getConnectionMaxIdleTime());
        }
        agentConnector.setPort(port);
        return agentConnector;
    }

    @java.lang.SuppressWarnings("deprecation")
    private org.eclipse.jetty.server.ServerConnector createSelectChannelConnectorForClient(org.eclipse.jetty.server.Server server, int acceptors) {
        java.util.Map<java.lang.String, java.lang.String> configsMap = configs.getConfigsMap();
        org.eclipse.jetty.server.ServerConnector apiConnector;
        org.eclipse.jetty.server.HttpConfiguration http_config = new org.eclipse.jetty.server.HttpConfiguration();
        http_config.setRequestHeaderSize(configs.getHttpRequestHeaderSize());
        http_config.setResponseHeaderSize(configs.getHttpResponseHeaderSize());
        http_config.setSendServerVersion(false);
        if (configs.getApiSSLAuthentication()) {
            java.lang.String httpsKeystore = (configsMap.get(org.apache.ambari.server.configuration.Configuration.CLIENT_API_SSL_KSTR_DIR_NAME.getKey()) + java.io.File.separator) + configsMap.get(org.apache.ambari.server.configuration.Configuration.CLIENT_API_SSL_KSTR_NAME.getKey());
            java.lang.String httpsTruststore = (configsMap.get(org.apache.ambari.server.configuration.Configuration.CLIENT_API_SSL_KSTR_DIR_NAME.getKey()) + java.io.File.separator) + configsMap.get(org.apache.ambari.server.configuration.Configuration.CLIENT_API_SSL_TSTR_NAME.getKey());
            org.apache.ambari.server.controller.AmbariServer.LOG.info("API SSL Authentication is turned on. Keystore - " + httpsKeystore);
            java.lang.String httpsCrtPass = configsMap.get(org.apache.ambari.server.configuration.Configuration.CLIENT_API_SSL_CRT_PASS.getKey());
            org.eclipse.jetty.server.HttpConfiguration https_config = new org.eclipse.jetty.server.HttpConfiguration(http_config);
            https_config.addCustomizer(new org.eclipse.jetty.server.SecureRequestCustomizer());
            https_config.setSecurePort(configs.getClientSSLApiPort());
            org.eclipse.jetty.util.ssl.SslContextFactory contextFactoryApi = new org.eclipse.jetty.util.ssl.SslContextFactory();
            disableInsecureProtocols(contextFactoryApi);
            contextFactoryApi.setKeyStorePath(httpsKeystore);
            contextFactoryApi.setTrustStorePath(httpsTruststore);
            contextFactoryApi.setKeyStorePassword(httpsCrtPass);
            contextFactoryApi.setKeyManagerPassword(httpsCrtPass);
            contextFactoryApi.setTrustStorePassword(httpsCrtPass);
            contextFactoryApi.setKeyStoreType(configsMap.get(org.apache.ambari.server.configuration.Configuration.CLIENT_API_SSL_KSTR_TYPE.getKey()));
            contextFactoryApi.setTrustStoreType(configsMap.get(org.apache.ambari.server.configuration.Configuration.CLIENT_API_SSL_KSTR_TYPE.getKey()));
            apiConnector = new org.eclipse.jetty.server.ServerConnector(server, acceptors, -1, new org.eclipse.jetty.server.SslConnectionFactory(contextFactoryApi, HttpVersion.HTTP_1_1.toString()), new org.eclipse.jetty.server.HttpConnectionFactory(https_config));
            apiConnector.setPort(configs.getClientSSLApiPort());
        } else {
            apiConnector = new org.eclipse.jetty.server.ServerConnector(server, acceptors, -1, new org.eclipse.jetty.server.HttpConnectionFactory(http_config));
            apiConnector.setPort(configs.getClientApiPort());
        }
        apiConnector.setIdleTimeout(configs.getConnectionMaxIdleTime());
        return apiConnector;
    }

    protected void runDatabaseConsistencyCheck() throws java.lang.Exception {
        if (java.lang.System.getProperty("skipDatabaseConsistencyCheck") == null) {
            boolean fixIssues = java.lang.System.getProperty("fixDatabaseConsistency") != null;
            try {
                org.apache.ambari.server.checks.DatabaseConsistencyCheckResult checkResult = org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.runAllDBChecks(fixIssues);
                java.lang.System.out.println("Database consistency check result: " + checkResult);
                if (checkResult.isError()) {
                    java.lang.System.exit(1);
                }
            } catch (java.lang.Throwable ex) {
                java.lang.System.out.println("Database consistency check result: " + org.apache.ambari.server.checks.DatabaseConsistencyCheckResult.DB_CHECK_ERROR);
                throw new java.lang.Exception(ex);
            }
        }
    }

    private void setupJulLogging() {
        java.util.logging.LogManager.getLogManager().reset();
        org.slf4j.bridge.SLF4JBridgeHandler.install();
    }

    protected org.eclipse.jetty.server.Server configureJettyThreadPool(int acceptorThreads, java.lang.String threadPoolName, int configuredThreadPoolSize) {
        int minumumAvailableThreads = 20;
        int reservedJettyThreads = acceptorThreads * 2;
        if (configuredThreadPoolSize < (reservedJettyThreads + minumumAvailableThreads)) {
            int newThreadPoolSize = reservedJettyThreads + minumumAvailableThreads;
            org.apache.ambari.server.controller.AmbariServer.LOG.warn("The configured Jetty {} thread pool value of {} is not sufficient on a host with {} processors. Increasing the value to {}.", threadPoolName, configuredThreadPoolSize, java.lang.Runtime.getRuntime().availableProcessors(), newThreadPoolSize);
            configuredThreadPoolSize = newThreadPoolSize;
        }
        org.apache.ambari.server.controller.AmbariServer.LOG.info("Jetty is configuring {} with {} reserved acceptors/selectors and a total pool size of {} for {} processors.", threadPoolName, acceptorThreads * 2, configuredThreadPoolSize, java.lang.Runtime.getRuntime().availableProcessors());
        org.eclipse.jetty.util.thread.QueuedThreadPool qtp = new org.eclipse.jetty.util.thread.QueuedThreadPool(configuredThreadPoolSize);
        qtp.setName(threadPoolName);
        return new org.eclipse.jetty.server.Server(qtp);
    }

    private void disableInsecureProtocols(org.eclipse.jetty.util.ssl.SslContextFactory factory) {
        factory.setExcludeProtocols(org.apache.ambari.server.controller.AmbariServer.DEPRECATED_SSL_PROTOCOLS);
        factory.setIncludeProtocols(new java.lang.String[]{ "SSLv2Hello", "SSLv3", "TLSv1.1", "TLSv1.2" });
        if (!configs.getSrvrDisabledCiphers().isEmpty()) {
            java.lang.String[] masks = configs.getSrvrDisabledCiphers().split(DISABLED_ENTRIES_SPLITTER);
            factory.setExcludeCipherSuites(masks);
        }
        if (!configs.getSrvrDisabledProtocols().isEmpty()) {
            java.lang.String[] masks = configs.getSrvrDisabledProtocols().split(DISABLED_ENTRIES_SPLITTER);
            factory.setExcludeProtocols(masks);
        }
    }

    protected void configureRootHandler(org.eclipse.jetty.servlet.ServletContextHandler root) {
        configureHandlerCompression(root);
        configureAdditionalContentTypes(root);
        root.setContextPath(CONTEXT_PATH);
        org.apache.ambari.server.api.AmbariErrorHandler ambariErrorHandler = injector.getInstance(org.apache.ambari.server.api.AmbariErrorHandler.class);
        ambariErrorHandler.setShowStacks(configs.isServerShowErrorStacks());
        root.setErrorHandler(ambariErrorHandler);
        root.setMaxFormContentSize(-1);
        root.setResourceBase(configs.getWebAppDir());
    }

    protected void configureHandlerCompression(org.eclipse.jetty.servlet.ServletContextHandler context) {
        if (configs.isApiGzipped()) {
            org.eclipse.jetty.servlet.FilterHolder gzipFilter = context.addFilter(org.eclipse.jetty.servlets.GzipFilter.class, "/*", java.util.EnumSet.of(javax.servlet.DispatcherType.REQUEST));
            gzipFilter.setInitParameter("methods", "GET,POST,PUT,DELETE");
            gzipFilter.setInitParameter("excludePathPatterns", ".*(\\.woff|\\.ttf|\\.woff2|\\.eot|\\.svg)");
            gzipFilter.setInitParameter("mimeTypes", "text/html,text/plain,text/xml,text/css,application/x-javascript," + ("application/xml,application/x-www-form-urlencoded," + "application/javascript,application/json"));
            gzipFilter.setInitParameter("minGzipSize", configs.getApiGzipMinSize());
        }
    }

    private void configureAdditionalContentTypes(org.eclipse.jetty.servlet.ServletContextHandler root) {
        root.getMimeTypes().addMimeMapping("woff", "application/font-woff");
        root.getMimeTypes().addMimeMapping("ttf", "application/font-sfnt");
    }

    @com.google.inject.persist.Transactional
    protected void initDB() throws org.apache.ambari.server.AmbariException {
        if ((configs.getPersistenceType() == org.apache.ambari.server.orm.PersistenceType.IN_MEMORY) || dbInitNeeded) {
            org.apache.ambari.server.controller.AmbariServer.LOG.info("Database init needed - creating default data");
            org.apache.ambari.server.security.authorization.Users users = injector.getInstance(org.apache.ambari.server.security.authorization.Users.class);
            org.apache.ambari.server.orm.entities.UserEntity userEntity;
            userEntity = users.createUser("admin", "admin", "admin");
            users.addLocalAuthentication(userEntity, "admin");
            users.grantAdminPrivilege(userEntity);
            userEntity = users.createUser("user", "user", "user");
            users.addLocalAuthentication(userEntity, "user");
            org.apache.ambari.server.orm.entities.MetainfoEntity schemaVersion = new org.apache.ambari.server.orm.entities.MetainfoEntity();
            schemaVersion.setMetainfoName(org.apache.ambari.server.configuration.Configuration.SERVER_VERSION_KEY);
            schemaVersion.setMetainfoValue(org.apache.ambari.server.utils.VersionUtils.getVersionSubstring(ambariMetaInfo.getServerVersion()));
            metainfoDAO.create(schemaVersion);
        }
    }

    public void stop() throws java.lang.Exception {
        if (server == null) {
            throw new org.apache.ambari.server.AmbariException("Error stopping the server");
        } else {
            try {
                server.stop();
            } catch (java.lang.Exception e) {
                org.apache.ambari.server.controller.AmbariServer.LOG.error("Error stopping the server", e);
            }
        }
    }

    @java.lang.Deprecated
    public void performStaticInjection() {
        org.apache.ambari.server.security.unsecured.rest.CertificateDownload.init(injector.getInstance(org.apache.ambari.server.security.CertificateManager.class));
        org.apache.ambari.server.security.unsecured.rest.ConnectionInfo.init(injector.getInstance(org.apache.ambari.server.configuration.Configuration.class));
        org.apache.ambari.server.security.unsecured.rest.CertificateSign.init(injector.getInstance(org.apache.ambari.server.security.CertificateManager.class));
        org.apache.ambari.server.resources.api.rest.GetResource.init(injector.getInstance(org.apache.ambari.server.resources.ResourceManager.class));
        org.apache.ambari.server.api.services.PersistKeyValueService.init(injector.getInstance(org.apache.ambari.server.api.services.PersistKeyValueImpl.class));
        org.apache.ambari.server.api.services.KeyService.init(injector.getInstance(org.apache.ambari.server.api.services.PersistKeyValueImpl.class));
        org.apache.ambari.server.api.rest.BootStrapResource.init(injector.getInstance(org.apache.ambari.server.bootstrap.BootStrapImpl.class));
        org.apache.ambari.server.controller.internal.StackAdvisorResourceProvider.init(injector.getInstance(org.apache.ambari.server.api.services.stackadvisor.StackAdvisorHelper.class), injector.getInstance(org.apache.ambari.server.configuration.Configuration.class), injector.getInstance(org.apache.ambari.server.state.Clusters.class), injector.getInstance(org.apache.ambari.server.api.services.AmbariMetaInfo.class));
        org.apache.ambari.server.utils.StageUtils.setGson(injector.getInstance(com.google.gson.Gson.class));
        org.apache.ambari.server.utils.StageUtils.setTopologyManager(injector.getInstance(org.apache.ambari.server.topology.TopologyManager.class));
        org.apache.ambari.server.utils.StageUtils.setConfiguration(injector.getInstance(org.apache.ambari.server.configuration.Configuration.class));
        org.apache.ambari.server.security.SecurityFilter.init(injector.getInstance(org.apache.ambari.server.configuration.Configuration.class));
        org.apache.ambari.server.controller.internal.StackDefinedPropertyProvider.init(injector);
        org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.init(injector.getInstance(org.apache.ambari.server.controller.ResourceProviderFactory.class));
        org.apache.ambari.server.controller.internal.BlueprintResourceProvider.init(injector.getInstance(org.apache.ambari.server.topology.BlueprintFactory.class), injector.getInstance(org.apache.ambari.server.orm.dao.BlueprintDAO.class), injector.getInstance(org.apache.ambari.server.orm.dao.TopologyRequestDAO.class), injector.getInstance(org.apache.ambari.server.topology.SecurityConfigurationFactory.class), injector.getInstance(com.google.gson.Gson.class), ambariMetaInfo);
        org.apache.ambari.server.controller.internal.StackDependencyResourceProvider.init(ambariMetaInfo);
        org.apache.ambari.server.controller.internal.ClusterResourceProvider.init(injector.getInstance(org.apache.ambari.server.topology.TopologyManager.class), injector.getInstance(org.apache.ambari.server.topology.TopologyRequestFactoryImpl.class), injector.getInstance(org.apache.ambari.server.topology.SecurityConfigurationFactory.class), injector.getInstance(com.google.gson.Gson.class));
        org.apache.ambari.server.controller.internal.HostResourceProvider.setTopologyManager(injector.getInstance(org.apache.ambari.server.topology.TopologyManager.class));
        org.apache.ambari.server.topology.BlueprintFactory.init(injector.getInstance(org.apache.ambari.server.orm.dao.BlueprintDAO.class));
        org.apache.ambari.server.controller.internal.BaseClusterRequest.init(injector.getInstance(org.apache.ambari.server.topology.BlueprintFactory.class));
        org.apache.ambari.server.topology.AmbariContext.init(injector.getInstance(org.apache.ambari.server.actionmanager.HostRoleCommandFactory.class));
        org.apache.ambari.server.controller.internal.PermissionResourceProvider.init(injector.getInstance(org.apache.ambari.server.orm.dao.PermissionDAO.class));
        org.apache.ambari.server.controller.internal.ViewPermissionResourceProvider.init(injector.getInstance(org.apache.ambari.server.orm.dao.PermissionDAO.class));
        org.apache.ambari.server.controller.internal.PrivilegeResourceProvider.init(injector.getInstance(org.apache.ambari.server.orm.dao.PrivilegeDAO.class), injector.getInstance(org.apache.ambari.server.orm.dao.UserDAO.class), injector.getInstance(org.apache.ambari.server.orm.dao.GroupDAO.class), injector.getInstance(org.apache.ambari.server.orm.dao.PrincipalDAO.class), injector.getInstance(org.apache.ambari.server.orm.dao.PermissionDAO.class), injector.getInstance(org.apache.ambari.server.orm.dao.ResourceDAO.class));
        org.apache.ambari.server.controller.internal.UserPrivilegeResourceProvider.init(injector.getInstance(org.apache.ambari.server.orm.dao.UserDAO.class), injector.getInstance(org.apache.ambari.server.orm.dao.ClusterDAO.class), injector.getInstance(org.apache.ambari.server.orm.dao.GroupDAO.class), injector.getInstance(org.apache.ambari.server.orm.dao.ViewInstanceDAO.class), injector.getInstance(org.apache.ambari.server.security.authorization.Users.class));
        org.apache.ambari.server.controller.internal.ClusterPrivilegeResourceProvider.init(injector.getInstance(org.apache.ambari.server.orm.dao.ClusterDAO.class));
        org.apache.ambari.server.controller.internal.AmbariPrivilegeResourceProvider.init(injector.getInstance(org.apache.ambari.server.orm.dao.ClusterDAO.class));
        org.apache.ambari.server.actionmanager.ActionManager.setTopologyManager(injector.getInstance(org.apache.ambari.server.topology.TopologyManager.class));
        org.apache.ambari.server.serveraction.kerberos.stageutils.KerberosKeytabController.setKerberosHelper(injector.getInstance(org.apache.ambari.server.controller.KerberosHelper.class));
        org.apache.ambari.server.api.services.stackadvisor.StackAdvisorBlueprintProcessor.init(injector.getInstance(org.apache.ambari.server.api.services.stackadvisor.StackAdvisorHelper.class));
        org.apache.ambari.server.controller.metrics.ThreadPoolEnabledPropertyProvider.init(injector.getInstance(org.apache.ambari.server.configuration.Configuration.class));
        org.apache.ambari.server.api.services.BaseService.init(injector.getInstance(org.apache.ambari.server.audit.request.RequestAuditLogger.class));
        org.apache.ambari.server.utils.RetryHelper.init(injector.getInstance(org.apache.ambari.server.state.Clusters.class), configs.getOperationsRetryAttempts());
        org.apache.ambari.server.controller.utilities.KerberosIdentityCleaner identityCleaner = injector.getInstance(org.apache.ambari.server.controller.utilities.KerberosIdentityCleaner.class);
        identityCleaner.register();
        configureFileWatcher();
    }

    private void configureFileWatcher() {
        org.apache.ambari.server.events.publishers.AmbariEventPublisher ambariEventPublisher = injector.getInstance(org.apache.ambari.server.events.publishers.AmbariEventPublisher.class);
        org.apache.ambari.server.configuration.Configuration config = injector.getInstance(org.apache.ambari.server.configuration.Configuration.class);
        org.apache.ambari.server.configuration.SingleFileWatch watch = new org.apache.ambari.server.configuration.SingleFileWatch(config.getConfigFile(), file -> ambariEventPublisher.publish(new org.apache.ambari.server.events.AmbariPropertiesChangedEvent()));
        watch.start();
    }

    public void initViewRegistry() {
        org.apache.ambari.server.view.ViewRegistry.initInstance(viewRegistry);
    }

    public static void setupProxyAuth() {
        final java.lang.String proxyUser = java.lang.System.getProperty("http.proxyUser");
        final java.lang.String proxyPass = java.lang.System.getProperty("http.proxyPassword");
        if ((null != proxyUser) && (null != proxyPass)) {
            org.apache.ambari.server.controller.AmbariServer.LOG.info("Proxy authentication enabled");
            java.net.Authenticator.setDefault(new java.net.Authenticator() {
                @java.lang.Override
                protected java.net.PasswordAuthentication getPasswordAuthentication() {
                    return new java.net.PasswordAuthentication(proxyUser, proxyPass.toCharArray());
                }
            });
        } else {
            org.apache.ambari.server.controller.AmbariServer.LOG.debug("Proxy authentication not specified");
        }
    }

    private static void logStartup() {
        final java.lang.String linePrefix = "STARTUP_MESSAGE: ";
        final java.lang.String classpathPropertyName = "java.class.path";
        final java.lang.String classpath = java.lang.System.getProperty(classpathPropertyName);
        java.lang.String[] rawMessages = new java.lang.String[]{ linePrefix + "Starting AmbariServer.java executable", (classpathPropertyName + " = ") + classpath };
        org.apache.ambari.server.controller.AmbariServer.LOG.info(com.google.common.base.Joiner.on("\n" + linePrefix).join(rawMessages));
    }

    public static void enableLog4jMonitor(java.util.Map<java.lang.String, java.lang.String> configsMap) {
        java.lang.String log4jpath = org.apache.ambari.server.controller.AmbariServer.class.getResource("/" + org.apache.ambari.server.configuration.Configuration.AMBARI_LOG_FILE).toString();
        java.lang.String monitorDelay = configsMap.get(org.apache.ambari.server.configuration.Configuration.LOG4JMONITOR_DELAY.getKey());
        long monitorDelayLong = org.apache.ambari.server.configuration.Configuration.LOG4JMONITOR_DELAY.getDefaultValue();
        try {
            log4jpath = log4jpath.replace("file:", "");
            if (org.apache.commons.lang.StringUtils.isNotBlank(monitorDelay)) {
                monitorDelayLong = java.lang.Long.parseLong(monitorDelay);
            }
            org.apache.log4j.PropertyConfigurator.configureAndWatch(log4jpath, monitorDelayLong);
        } catch (java.lang.Exception e) {
            org.apache.ambari.server.controller.AmbariServer.LOG.error("Exception in setting log4j monitor delay of {} for {}", monitorDelay, log4jpath, e);
        }
    }

    private static void loadRequestlogHandler(org.apache.ambari.server.controller.AmbariHandlerList handlerList, org.eclipse.jetty.server.Server serverForAgent, java.util.Map<java.lang.String, java.lang.String> configsMap) {
        java.lang.String requestlogpath = configsMap.get(org.apache.ambari.server.configuration.Configuration.REQUEST_LOGPATH.getKey());
        if (!org.apache.commons.lang.StringUtils.isBlank(requestlogpath)) {
            java.lang.String logfullpath = (requestlogpath + "//") + org.apache.ambari.server.configuration.Configuration.REQUEST_LOGNAMEPATTERN.getDefaultValue();
            org.apache.ambari.server.controller.AmbariServer.LOG.info("********* Initializing request access log: " + logfullpath);
            org.eclipse.jetty.server.handler.RequestLogHandler requestLogHandler = new org.eclipse.jetty.server.handler.RequestLogHandler();
            org.eclipse.jetty.server.NCSARequestLog requestLog = new org.eclipse.jetty.server.NCSARequestLog(requestlogpath);
            java.lang.String retaindays = configsMap.get(org.apache.ambari.server.configuration.Configuration.REQUEST_LOG_RETAINDAYS.getKey());
            int retaindaysInt = org.apache.ambari.server.configuration.Configuration.REQUEST_LOG_RETAINDAYS.getDefaultValue();
            if ((retaindays != null) && (!org.apache.commons.lang.StringUtils.isBlank(retaindays))) {
                retaindaysInt = java.lang.Integer.parseInt(retaindays.trim());
            }
            requestLog.setRetainDays(retaindaysInt);
            requestLog.setAppend(true);
            requestLog.setLogLatency(true);
            requestLog.setExtended(true);
            requestLogHandler.setRequestLog(requestLog);
            handlerList.addHandler(requestLogHandler);
            org.eclipse.jetty.server.handler.HandlerCollection handlers = new org.eclipse.jetty.server.handler.HandlerCollection();
            org.eclipse.jetty.server.Handler[] handler = serverForAgent.getHandlers();
            if (handler != null) {
                handlers.setHandlers(((org.eclipse.jetty.server.Handler[]) (handler)));
                handlers.addHandler(requestLogHandler);
                serverForAgent.setHandler(handlers);
            }
        }
    }

    public static void main(java.lang.String[] args) throws java.lang.Exception {
        org.apache.ambari.server.controller.AmbariServer.logStartup();
        com.google.inject.Injector injector = com.google.inject.Guice.createInjector(new org.apache.ambari.server.controller.ControllerModule(), new org.apache.ambari.server.audit.AuditLoggerModule(), new org.apache.ambari.server.ldap.LdapModule());
        org.apache.ambari.server.controller.AmbariServer server = null;
        try {
            org.apache.ambari.server.controller.AmbariServer.LOG.info("Getting the controller");
            org.apache.ambari.server.configuration.Configuration config = injector.getInstance(org.apache.ambari.server.configuration.Configuration.class);
            config.validatePasswordPolicyRegexp();
            if (!config.isActiveInstance()) {
                java.lang.String errMsg = "This instance of ambari server is not designated as active. Cannot start ambari server." + "The property active.instance is set to false in ambari.properties";
                throw new org.apache.ambari.server.AmbariException(errMsg);
            }
            org.apache.ambari.server.controller.AmbariServer.setupProxyAuth();
            org.apache.ambari.server.orm.GuiceJpaInitializer jpaInitializer = injector.getInstance(org.apache.ambari.server.orm.GuiceJpaInitializer.class);
            jpaInitializer.setInitialized();
            org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.checkDBVersionCompatible();
            server = injector.getInstance(org.apache.ambari.server.controller.AmbariServer.class);
            injector.getInstance(org.apache.ambari.server.stack.UpdateActiveRepoVersionOnStartup.class).process();
            org.apache.ambari.server.security.CertificateManager certMan = injector.getInstance(org.apache.ambari.server.security.CertificateManager.class);
            certMan.initRootCert();
            org.apache.ambari.server.controller.utilities.KerberosChecker.checkJaasConfiguration();
            org.apache.ambari.server.view.ViewRegistry.initInstance(server.viewRegistry);
            org.apache.ambari.server.configuration.ComponentSSLConfiguration.instance().init(server.configs);
            server.run();
        } catch (java.lang.Throwable t) {
            java.lang.System.err.println("An unexpected error occured during starting Ambari Server.");
            t.printStackTrace();
            org.apache.ambari.server.controller.AmbariServer.LOG.error("Failed to run the Ambari Server", t);
            if (server != null) {
                server.stop();
            }
            java.lang.System.exit(-1);
        }
    }
}