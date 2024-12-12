package org.apache.ambari.server.security.encryption;
public class AmbariServerConfigurationEncryptor implements org.apache.ambari.server.security.encryption.Encryptor<org.apache.ambari.server.configuration.AmbariServerConfiguration> {
    private final java.util.Set<java.lang.String> passwordConfigurations = org.apache.ambari.server.configuration.AmbariServerConfigurationKey.findPasswordConfigurations();

    private final org.apache.ambari.server.security.encryption.EncryptionService encryptionService;

    @com.google.inject.Inject
    public AmbariServerConfigurationEncryptor(org.apache.ambari.server.security.encryption.EncryptionService encryptionService) {
        this.encryptionService = encryptionService;
    }

    @java.lang.Override
    public void encryptSensitiveData(org.apache.ambari.server.configuration.AmbariServerConfiguration encryptible) {
        encryptible.toMap().entrySet().stream().filter(f -> shouldEncrypt(f)).forEach(entry -> encryptible.setValueFor(entry.getKey(), encryptAndDecorateConfigValue(entry.getValue())));
    }

    private boolean shouldEncrypt(java.util.Map.Entry<java.lang.String, java.lang.String> config) {
        return passwordConfigurations.contains(config.getKey()) && (!isEncryptedPassword(config.getValue()));
    }

    private java.lang.String encryptAndDecorateConfigValue(java.lang.String propertyValue) {
        final java.lang.String encrypted = encryptionService.encrypt(propertyValue, org.apache.ambari.server.utils.TextEncoding.BIN_HEX);
        return java.lang.String.format(org.apache.ambari.server.security.encryption.Encryptor.ENCRYPTED_PROPERTY_SCHEME, encrypted);
    }

    @java.lang.Override
    public void decryptSensitiveData(org.apache.ambari.server.configuration.AmbariServerConfiguration decryptible) {
        decryptible.toMap().entrySet().stream().filter(f -> passwordConfigurations.contains(f.getKey())).filter(f -> isEncryptedPassword(f.getValue())).forEach(entry -> decryptible.setValueFor(entry.getKey(), decryptConfig(entry.getValue())));
    }

    private boolean isEncryptedPassword(java.lang.String password) {
        return (password != null) && password.startsWith(org.apache.ambari.server.security.encryption.Encryptor.ENCRYPTED_PROPERTY_PREFIX);
    }

    private java.lang.String decryptConfig(java.lang.String property) {
        final java.lang.String encrypted = property.substring(org.apache.ambari.server.security.encryption.Encryptor.ENCRYPTED_PROPERTY_PREFIX.length(), property.indexOf('}'));
        return encryptionService.decrypt(encrypted, org.apache.ambari.server.utils.TextEncoding.BIN_HEX);
    }

    @java.lang.Override
    public java.lang.String getEncryptionKey() {
        return encryptionService.getAmbariMasterKey();
    }
}