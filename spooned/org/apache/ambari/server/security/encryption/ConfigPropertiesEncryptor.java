package org.apache.ambari.server.security.encryption;
@com.google.inject.Singleton
public class ConfigPropertiesEncryptor extends org.apache.ambari.server.security.encryption.PropertiesEncryptor implements org.apache.ambari.server.security.encryption.Encryptor<org.apache.ambari.server.state.Config> {
    @com.google.inject.Inject
    public ConfigPropertiesEncryptor(org.apache.ambari.server.security.encryption.EncryptionService encryptionService) {
        super(encryptionService);
    }

    @java.lang.Override
    public void encryptSensitiveData(org.apache.ambari.server.state.Config config) {
        java.util.Map<java.lang.String, java.lang.String> properties = config.getProperties();
        encrypt(properties, config.getCluster(), config.getType());
        config.setProperties(properties);
    }

    @java.lang.Override
    public void decryptSensitiveData(org.apache.ambari.server.state.Config config) {
        java.util.Map<java.lang.String, java.lang.String> properties = config.getProperties();
        decrypt(properties);
        config.setProperties(properties);
    }

    @java.lang.Override
    public java.lang.String getEncryptionKey() {
        return encryptionService.getAmbariMasterKey();
    }
}