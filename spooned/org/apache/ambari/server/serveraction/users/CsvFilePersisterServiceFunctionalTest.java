package org.apache.ambari.server.serveraction.users;
import com.google.inject.assistedinject.FactoryModuleBuilder;
public class CsvFilePersisterServiceFunctionalTest {
    private static final java.lang.String TEST_CSV = "/tmp/users.csv";

    private static com.google.inject.Injector injector;

    private static org.apache.ambari.server.serveraction.users.CollectionPersisterServiceFactory serviceFactory;

    private org.apache.ambari.server.serveraction.users.CsvFilePersisterService csvFileCsvFilePersisterService;

    private java.nio.file.Path testCsvPath;

    private static class TestPersistServiceModule extends com.google.inject.AbstractModule {
        @java.lang.Override
        protected void configure() {
            install(new com.google.inject.assistedinject.FactoryModuleBuilder().implement(org.apache.ambari.server.serveraction.users.CollectionPersisterService.class, org.apache.ambari.server.serveraction.users.CsvFilePersisterService.class).build(org.apache.ambari.server.serveraction.users.CollectionPersisterServiceFactory.class));
        }
    }

    @org.junit.BeforeClass
    public static void beforeClass() {
        org.apache.ambari.server.serveraction.users.CsvFilePersisterServiceFunctionalTest.injector = com.google.inject.Guice.createInjector(new org.apache.ambari.server.serveraction.users.CsvFilePersisterServiceFunctionalTest.TestPersistServiceModule());
        org.apache.ambari.server.serveraction.users.CsvFilePersisterServiceFunctionalTest.serviceFactory = ((org.apache.ambari.server.serveraction.users.CollectionPersisterServiceFactory) (org.apache.ambari.server.serveraction.users.CsvFilePersisterServiceFunctionalTest.injector.getInstance(org.apache.ambari.server.serveraction.users.CollectionPersisterServiceFactory.class)));
    }

    @org.junit.Before
    public void before() {
        csvFileCsvFilePersisterService = org.apache.ambari.server.serveraction.users.CsvFilePersisterServiceFunctionalTest.serviceFactory.createCsvFilePersisterService(org.apache.ambari.server.serveraction.users.CsvFilePersisterServiceFunctionalTest.TEST_CSV);
        testCsvPath = java.nio.file.Paths.get(org.apache.ambari.server.serveraction.users.CsvFilePersisterServiceFunctionalTest.TEST_CSV);
    }

    @org.junit.Test
    public void shouldCreateCsvFileWithExpectedPermissions() throws java.io.IOException {
        org.junit.Assert.assertNotNull(csvFileCsvFilePersisterService);
        org.junit.Assert.assertTrue("The generated file couldn't be found", java.nio.file.Files.exists(testCsvPath));
        org.junit.Assert.assertTrue("The generated files doesn't have all the expected permissions", java.nio.file.Files.getPosixFilePermissions(testCsvPath).containsAll(csvFileCsvFilePersisterService.getCsvPermissions()));
        org.junit.Assert.assertFalse("The generated file has more than the required permissions", java.nio.file.Files.getPosixFilePermissions(testCsvPath).contains(java.nio.file.attribute.PosixFilePermission.GROUP_EXECUTE));
    }

    @org.junit.After
    public void after() throws java.io.IOException {
        java.nio.file.Files.deleteIfExists(java.nio.file.Paths.get(org.apache.ambari.server.serveraction.users.CsvFilePersisterServiceFunctionalTest.TEST_CSV));
    }
}