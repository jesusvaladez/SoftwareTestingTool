package org.apache.ambari.server.cleanup;
import org.eclipse.persistence.config.PersistenceUnitProperties;
@org.junit.Ignore("Ignored in order not to run with the unit tests as it's time consuming. Should be part of a functional test suit.")
public class CleanupServiceFunctionalTest {
    private static com.google.inject.Injector injector;

    @org.junit.BeforeClass
    public static void beforeClass() throws java.lang.Exception {
        org.apache.ambari.server.cleanup.CleanupServiceFunctionalTest.injector = com.google.inject.Guice.createInjector(new org.apache.ambari.server.cleanup.CleanupModule(), new org.apache.ambari.server.controller.ControllerModule(org.apache.ambari.server.cleanup.CleanupServiceFunctionalTest.getTestProperties()));
        org.apache.ambari.server.cleanup.CleanupServiceFunctionalTest.injector.getInstance(com.google.inject.persist.jpa.AmbariJpaPersistService.class).start();
    }

    private static com.google.inject.Module getTestPersistModule() {
        com.google.inject.persist.jpa.AmbariJpaPersistModule persistModule = new com.google.inject.persist.jpa.AmbariJpaPersistModule("ambari-server");
        org.apache.ambari.server.configuration.Configuration testConfiguration = new org.apache.ambari.server.configuration.Configuration(org.apache.ambari.server.cleanup.CleanupServiceFunctionalTest.getTestProperties());
        java.util.Properties persistenceProperties = org.apache.ambari.server.controller.ControllerModule.getPersistenceProperties(testConfiguration);
        persistenceProperties.setProperty(PersistenceUnitProperties.SCHEMA_GENERATION_DATABASE_ACTION, PersistenceUnitProperties.NONE);
        persistenceProperties.setProperty(PersistenceUnitProperties.THROW_EXCEPTIONS, "true");
        persistenceProperties.setProperty(PersistenceUnitProperties.JDBC_USER, "ambari");
        persistenceProperties.setProperty(PersistenceUnitProperties.JDBC_PASSWORD, "bigdata");
        return persistModule.properties(persistenceProperties);
    }

    private static java.lang.String getTestDataDDL() {
        return "ddl-func-test/ddl-cleanup-test-data.sql";
    }

    @org.junit.AfterClass
    public static void afterClass() {
        org.apache.ambari.server.cleanup.CleanupServiceFunctionalTest.injector.getInstance(com.google.inject.persist.jpa.AmbariJpaPersistService.class).stop();
    }

    private static java.util.Properties getTestProperties() {
        java.util.Properties properties = new java.util.Properties();
        properties.put("server.jdbc.connection-pool", "internal");
        properties.put("server.persistence.type", "remote");
        properties.put("server.jdbc.driver", "org.postgresql.Driver");
        properties.put("server.jdbc.user.name", "ambari");
        properties.put("server.jdbc.url", "jdbc:postgresql://192.168.59.103:5432/ambari");
        properties.put(org.apache.ambari.server.configuration.Configuration.SHARED_RESOURCES_DIR.getKey(), "/Users/lpuskas/prj/ambari/ambari-server/src/test/resources");
        return properties;
    }

    @org.junit.Before
    public void setUp() throws java.lang.Exception {
    }

    @org.junit.Test
    public void testIOCContext() throws java.lang.Exception {
        org.apache.ambari.server.cleanup.CleanupServiceImpl cleanupService = org.apache.ambari.server.cleanup.CleanupServiceFunctionalTest.injector.getInstance(org.apache.ambari.server.cleanup.CleanupServiceImpl.class);
        junit.framework.Assert.assertNotNull("The cleanupService instance should be present in the IoC context", cleanupService);
    }

    @org.junit.Test
    public void testRunCleanup() throws java.lang.Exception {
        org.apache.ambari.server.cleanup.CleanupService<org.apache.ambari.server.cleanup.TimeBasedCleanupPolicy> cleanupService = org.apache.ambari.server.cleanup.CleanupServiceFunctionalTest.injector.getInstance(org.apache.ambari.server.cleanup.CleanupServiceImpl.class);
        org.apache.ambari.server.cleanup.TimeBasedCleanupPolicy cleanupPolicy = new org.apache.ambari.server.cleanup.TimeBasedCleanupPolicy("cluster-1", 1455891250758L);
        cleanupService.cleanup(cleanupPolicy);
    }

    @org.junit.Test
    public void testServicesShouldBeInSingletonScope() throws java.lang.Exception {
        org.apache.ambari.server.cleanup.CleanupService cleanupService1 = org.apache.ambari.server.cleanup.CleanupServiceFunctionalTest.injector.getInstance(org.apache.ambari.server.cleanup.CleanupServiceImpl.class);
        org.apache.ambari.server.cleanup.CleanupService cleanupService2 = org.apache.ambari.server.cleanup.CleanupServiceFunctionalTest.injector.getInstance(org.apache.ambari.server.cleanup.CleanupServiceImpl.class);
        junit.framework.Assert.assertEquals("The ChainedCleanupService is not in Singleton scope!", cleanupService1, cleanupService2);
    }
}