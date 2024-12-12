package org.apache.ambari.server.security.encryption;
import com.google.inject.persist.PersistService;
public class SensitiveDataEncryption {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.security.encryption.SensitiveDataEncryption.class);

    private final com.google.inject.persist.PersistService persistService;

    private final com.google.inject.Injector injector;

    @com.google.inject.Inject
    public SensitiveDataEncryption(com.google.inject.Injector injector, com.google.inject.persist.PersistService persistService) {
        this.injector = injector;
        this.persistService = persistService;
    }

    private static class EncryptionHelperControllerModule extends org.apache.ambari.server.controller.ControllerModule {
        public EncryptionHelperControllerModule() throws java.lang.Exception {
        }

        @java.lang.Override
        protected void configure() {
            super.configure();
            org.apache.ambari.server.utils.EventBusSynchronizer.synchronizeAmbariEventPublisher(binder());
        }
    }

    private static class EncryptionHelperAuditModule extends org.apache.ambari.server.audit.AuditLoggerModule {
        @java.lang.Override
        protected void configure() {
            super.configure();
        }
    }

    public void startPersistenceService() {
        persistService.start();
    }

    public void stopPersistenceService() {
        persistService.stop();
    }

    public static void main(java.lang.String[] args) {
        if ((args.length < 1) || ((!"encryption".equals(args[0])) && (!"decryption".equals(args[0])))) {
            org.apache.ambari.server.security.encryption.SensitiveDataEncryption.LOG.error("The action parameter (\"encryption\" or \"decryption\") is required");
            java.lang.System.exit(-1);
        }
        boolean encrypt = "encryption".equals(args[0]);
        org.apache.ambari.server.security.encryption.SensitiveDataEncryption sensitiveDataEncryption = null;
        try {
            com.google.inject.Injector injector = com.google.inject.Guice.createInjector(new org.apache.ambari.server.security.encryption.SensitiveDataEncryption.EncryptionHelperControllerModule(), new org.apache.ambari.server.security.encryption.SensitiveDataEncryption.EncryptionHelperAuditModule(), new org.apache.ambari.server.ldap.LdapModule());
            sensitiveDataEncryption = injector.getInstance(org.apache.ambari.server.security.encryption.SensitiveDataEncryption.class);
            sensitiveDataEncryption.startPersistenceService();
            sensitiveDataEncryption.doEncryption(encrypt);
        } catch (java.lang.Throwable e) {
            org.apache.ambari.server.security.encryption.SensitiveDataEncryption.LOG.error("Exception occurred during config encryption/decryption:", e);
        } finally {
            if (sensitiveDataEncryption != null) {
                sensitiveDataEncryption.stopPersistenceService();
            }
        }
    }

    public void doEncryption(boolean encrypt) {
        org.apache.ambari.server.controller.AmbariManagementController ambariManagementController = injector.getInstance(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.security.encryption.Encryptor<org.apache.ambari.server.state.Config> configEncryptor = injector.getInstance(org.apache.ambari.server.security.encryption.ConfigPropertiesEncryptor.class);
        org.apache.ambari.server.state.Clusters clusters = ambariManagementController.getClusters();
        if (clusters != null) {
            java.util.Map<java.lang.String, org.apache.ambari.server.state.Cluster> clusterMap = clusters.getClusters();
            if ((clusterMap != null) && (!clusterMap.isEmpty())) {
                for (final org.apache.ambari.server.state.Cluster cluster : clusterMap.values()) {
                    java.util.Collection<org.apache.ambari.server.state.Config> configs = cluster.getAllConfigs();
                    for (org.apache.ambari.server.state.Config config : configs) {
                        if (encrypt) {
                            configEncryptor.encryptSensitiveData(config);
                        } else {
                            configEncryptor.decryptSensitiveData(config);
                        }
                        config.save();
                    }
                }
            }
        }
    }
}