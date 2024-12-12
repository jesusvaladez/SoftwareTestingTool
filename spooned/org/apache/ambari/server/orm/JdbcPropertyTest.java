package org.apache.ambari.server.orm;
public class JdbcPropertyTest {
    java.util.Properties properties;

    private com.google.inject.Injector injector;

    @org.junit.Before
    public void configure() {
        properties = new java.util.Properties();
        properties.setProperty(org.apache.ambari.server.configuration.Configuration.SERVER_PERSISTENCE_TYPE.getKey(), "in-memory");
        properties.setProperty(org.apache.ambari.server.configuration.Configuration.METADATA_DIR_PATH.getKey(), "src/test/resources/stacks");
        properties.setProperty(org.apache.ambari.server.configuration.Configuration.SERVER_VERSION_FILE.getKey(), "src/test/resources/version");
        properties.setProperty(org.apache.ambari.server.configuration.Configuration.OS_VERSION.getKey(), "centos5");
        properties.setProperty(org.apache.ambari.server.configuration.Configuration.SHARED_RESOURCES_DIR.getKey(), "src/test/resources/");
    }

    @org.junit.After
    public void tearDown() throws java.lang.Exception {
        org.apache.ambari.server.H2DatabaseCleaner.clearDatabaseAndStopPersistenceService(injector);
    }

    @org.junit.Test
    public void testNormal() throws java.lang.Exception {
        injector = com.google.inject.Guice.createInjector(new org.apache.ambari.server.audit.AuditLoggerModule(), new org.apache.ambari.server.controller.ControllerModule(properties), new org.apache.ambari.server.ldap.LdapModule());
        injector.getInstance(org.apache.ambari.server.orm.GuiceJpaInitializer.class);
        injector.getInstance(org.apache.ambari.server.state.Clusters.class);
    }

    @org.junit.Test
    public void testJdbcProperty() throws java.lang.Exception {
        properties.setProperty(org.apache.ambari.server.configuration.Configuration.SERVER_JDBC_PROPERTIES_PREFIX + "shutdown", "true");
        injector = com.google.inject.Guice.createInjector(new org.apache.ambari.server.audit.AuditLoggerModule(), new org.apache.ambari.server.controller.ControllerModule(properties), new org.apache.ambari.server.ldap.LdapModule());
        injector.getInstance(org.apache.ambari.server.orm.GuiceJpaInitializer.class);
        try {
            injector.getInstance(org.apache.ambari.server.state.Clusters.class);
            org.junit.Assert.fail("Expected in-memory to fail because property 'shutdown' specified.");
        } catch (java.lang.Throwable t) {
        }
    }
}