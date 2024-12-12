package org.apache.ambari.server.security.encryption;
import com.google.inject.persist.PersistService;
import javax.persistence.EntityManager;
import org.easymock.EasyMockSupport;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
public class SensitiveDataEncryptionTest {
    @org.junit.Rule
    public org.junit.rules.TemporaryFolder tmpFolder = new org.junit.rules.TemporaryFolder();

    @org.junit.Before
    public void setUp() throws java.lang.Exception {
        tmpFolder.create();
    }

    @org.junit.Test
    public void testSensitiveDataEncryption() throws java.lang.Exception {
        org.easymock.EasyMockSupport easyMockSupport = new org.easymock.EasyMockSupport();
        final org.apache.ambari.server.orm.DBAccessor mockDBDbAccessor = easyMockSupport.createNiceMock(org.apache.ambari.server.orm.DBAccessor.class);
        final org.apache.ambari.server.stack.StackManagerFactory mockStackManagerFactory = easyMockSupport.createNiceMock(org.apache.ambari.server.stack.StackManagerFactory.class);
        final javax.persistence.EntityManager mockEntityManager = easyMockSupport.createNiceMock(javax.persistence.EntityManager.class);
        final org.apache.ambari.server.state.Clusters mockClusters = easyMockSupport.createNiceMock(org.apache.ambari.server.state.Clusters.class);
        final org.apache.ambari.server.controller.AmbariManagementController mockAmbariManagementController = easyMockSupport.createNiceMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        final org.apache.ambari.server.state.stack.OsFamily mockOSFamily = easyMockSupport.createNiceMock(org.apache.ambari.server.state.stack.OsFamily.class);
        final org.apache.ambari.server.security.encryption.ConfigPropertiesEncryptor mockConfigPropertiesEncryptor = easyMockSupport.createNiceMock(org.apache.ambari.server.security.encryption.ConfigPropertiesEncryptor.class);
        final com.google.inject.Injector mockInjector = createInjector(mockDBDbAccessor, mockStackManagerFactory, mockEntityManager, mockClusters, mockAmbariManagementController, mockOSFamily, mockConfigPropertiesEncryptor);
        java.util.Map<java.lang.String, org.apache.ambari.server.state.Cluster> clusters = new java.util.HashMap<>();
        org.apache.ambari.server.state.Cluster cluster = easyMockSupport.createStrictMock(org.apache.ambari.server.state.Cluster.class);
        clusters.put("c1", cluster);
        EasyMock.expect(mockAmbariManagementController.getClusters()).andReturn(mockClusters).once();
        EasyMock.expect(mockClusters.getClusters()).andReturn(clusters).once();
        java.util.List<org.apache.ambari.server.state.Config> configs = new java.util.ArrayList<org.apache.ambari.server.state.Config>();
        org.apache.ambari.server.state.Config config = easyMockSupport.createStrictMock(org.apache.ambari.server.state.Config.class);
        configs.add(config);
        EasyMock.expect(cluster.getAllConfigs()).andReturn(configs).once();
        mockConfigPropertiesEncryptor.encryptSensitiveData(config);
        config.save();
        EasyMock.expectLastCall();
        final com.google.inject.persist.PersistService mockPersistService = easyMockSupport.createNiceMock(com.google.inject.persist.PersistService.class);
        org.apache.ambari.server.security.encryption.SensitiveDataEncryption sensitiveDataEncryption = new org.apache.ambari.server.security.encryption.SensitiveDataEncryption(mockInjector, mockPersistService);
        easyMockSupport.replayAll();
        sensitiveDataEncryption.doEncryption(true);
        easyMockSupport.verifyAll();
    }

    @org.junit.Test
    public void testSensitiveDataDecryption() throws java.lang.Exception {
        org.easymock.EasyMockSupport easyMockSupport = new org.easymock.EasyMockSupport();
        final org.apache.ambari.server.orm.DBAccessor mockDBDbAccessor = easyMockSupport.createNiceMock(org.apache.ambari.server.orm.DBAccessor.class);
        final org.apache.ambari.server.stack.StackManagerFactory mockStackManagerFactory = easyMockSupport.createNiceMock(org.apache.ambari.server.stack.StackManagerFactory.class);
        final javax.persistence.EntityManager mockEntityManager = easyMockSupport.createNiceMock(javax.persistence.EntityManager.class);
        final org.apache.ambari.server.state.Clusters mockClusters = easyMockSupport.createNiceMock(org.apache.ambari.server.state.Clusters.class);
        final org.apache.ambari.server.controller.AmbariManagementController mockAmbariManagementController = easyMockSupport.createNiceMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        final org.apache.ambari.server.state.stack.OsFamily mockOSFamily = easyMockSupport.createNiceMock(org.apache.ambari.server.state.stack.OsFamily.class);
        final org.apache.ambari.server.security.encryption.ConfigPropertiesEncryptor mockConfigPropertiesEncryptor = easyMockSupport.createNiceMock(org.apache.ambari.server.security.encryption.ConfigPropertiesEncryptor.class);
        final com.google.inject.Injector mockInjector = createInjector(mockDBDbAccessor, mockStackManagerFactory, mockEntityManager, mockClusters, mockAmbariManagementController, mockOSFamily, mockConfigPropertiesEncryptor);
        java.util.Map<java.lang.String, org.apache.ambari.server.state.Cluster> clusters = new java.util.HashMap<>();
        org.apache.ambari.server.state.Cluster cluster = easyMockSupport.createStrictMock(org.apache.ambari.server.state.Cluster.class);
        clusters.put("c1", cluster);
        EasyMock.expect(mockAmbariManagementController.getClusters()).andReturn(mockClusters).once();
        EasyMock.expect(mockClusters.getClusters()).andReturn(clusters).once();
        java.util.List<org.apache.ambari.server.state.Config> configs = new java.util.ArrayList<org.apache.ambari.server.state.Config>();
        org.apache.ambari.server.state.Config config = easyMockSupport.createStrictMock(org.apache.ambari.server.state.Config.class);
        configs.add(config);
        EasyMock.expect(cluster.getAllConfigs()).andReturn(configs).once();
        mockConfigPropertiesEncryptor.decryptSensitiveData(config);
        config.save();
        EasyMock.expectLastCall();
        final com.google.inject.persist.PersistService mockPersistService = easyMockSupport.createNiceMock(com.google.inject.persist.PersistService.class);
        org.apache.ambari.server.security.encryption.SensitiveDataEncryption sensitiveDataEncryption = new org.apache.ambari.server.security.encryption.SensitiveDataEncryption(mockInjector, mockPersistService);
        easyMockSupport.replayAll();
        sensitiveDataEncryption.doEncryption(false);
        easyMockSupport.verifyAll();
    }

    private com.google.inject.Injector createInjector(org.apache.ambari.server.orm.DBAccessor mockDBDbAccessor, org.apache.ambari.server.stack.StackManagerFactory mockStackManagerFactory, javax.persistence.EntityManager mockEntityManager, org.apache.ambari.server.state.Clusters mockClusters, org.apache.ambari.server.controller.AmbariManagementController mockAmbariManagementController, org.apache.ambari.server.state.stack.OsFamily mockOSFamily, org.apache.ambari.server.security.encryption.ConfigPropertiesEncryptor mockConfigPropertiesEncryptor) {
        return com.google.inject.Guice.createInjector(new com.google.inject.AbstractModule() {
            @java.lang.Override
            protected void configure() {
                bind(org.apache.ambari.server.stack.StackManagerFactory.class).toInstance(mockStackManagerFactory);
                bind(javax.persistence.EntityManager.class).toInstance(mockEntityManager);
                bind(org.apache.ambari.server.orm.DBAccessor.class).toInstance(mockDBDbAccessor);
                bind(org.apache.ambari.server.state.Clusters.class).toInstance(mockClusters);
                bind(org.apache.ambari.server.state.stack.OsFamily.class).toInstance(mockOSFamily);
                bind(org.apache.ambari.server.controller.AmbariManagementController.class).toInstance(mockAmbariManagementController);
                bind(org.apache.ambari.server.security.encryption.ConfigPropertiesEncryptor.class).toInstance(mockConfigPropertiesEncryptor);
            }
        });
    }
}