package org.apache.ambari.server.security.encryption;
import org.apache.commons.collections.CollectionUtils;
public class PropertiesEncryptor {
    private final java.util.Map<java.lang.Long, java.util.Map<org.apache.ambari.server.state.StackId, java.util.Map<java.lang.String, java.util.Set<java.lang.String>>>> clusterPasswordProperties = new java.util.concurrent.ConcurrentHashMap<>();

    protected final org.apache.ambari.server.security.encryption.EncryptionService encryptionService;

    public PropertiesEncryptor(org.apache.ambari.server.security.encryption.EncryptionService encryptionService) {
        this.encryptionService = encryptionService;
    }

    protected void encrypt(java.util.Map<java.lang.String, java.lang.String> configProperties, org.apache.ambari.server.state.Cluster cluster, java.lang.String configType, java.lang.String encryptionKey) {
        encrypt(configProperties, cluster, configType, value -> encryptAndDecoratePropertyValue(value, encryptionKey));
    }

    protected void encrypt(java.util.Map<java.lang.String, java.lang.String> configProperties, org.apache.ambari.server.state.Cluster cluster, java.lang.String configType) {
        encrypt(configProperties, cluster, configType, value -> encryptAndDecoratePropertyValue(value));
    }

    protected void encrypt(java.util.Map<java.lang.String, java.lang.String> configProperties, org.apache.ambari.server.state.Cluster cluster, java.lang.String configType, java.util.function.Function<java.lang.String, java.lang.String> encryption) {
        final java.util.Set<java.lang.String> passwordProperties = getPasswordProperties(cluster, configType);
        if (org.apache.commons.collections.CollectionUtils.isNotEmpty(passwordProperties)) {
            for (java.util.Map.Entry<java.lang.String, java.lang.String> property : configProperties.entrySet()) {
                if (shouldEncrypt(property, passwordProperties)) {
                    configProperties.put(property.getKey(), encryption.apply(property.getValue()));
                }
            }
        }
    }

    private boolean shouldEncrypt(java.util.Map.Entry<java.lang.String, java.lang.String> property, java.util.Set<java.lang.String> passwordProperties) {
        return passwordProperties.contains(property.getKey()) && (!isEncryptedPassword(property.getValue()));
    }

    private boolean isEncryptedPassword(java.lang.String password) {
        return (password != null) && password.startsWith(org.apache.ambari.server.security.encryption.Encryptor.ENCRYPTED_PROPERTY_PREFIX);
    }

    private java.util.Set<java.lang.String> getPasswordProperties(org.apache.ambari.server.state.Cluster cluster, java.lang.String configType) {
        if (cluster.getCurrentStackVersion().equals(cluster.getDesiredStackVersion())) {
            return getPasswordProperties(cluster, cluster.getCurrentStackVersion(), configType);
        } else {
            return getPasswordProperties(cluster, cluster.getDesiredStackVersion(), configType);
        }
    }

    private java.util.Set<java.lang.String> getPasswordProperties(org.apache.ambari.server.state.Cluster cluster, org.apache.ambari.server.state.StackId stackId, java.lang.String configType) {
        final long clusterId = cluster.getClusterId();
        clusterPasswordProperties.computeIfAbsent(clusterId, v -> new java.util.concurrent.ConcurrentHashMap<>()).computeIfAbsent(stackId, v -> new java.util.concurrent.ConcurrentHashMap<>()).computeIfAbsent(configType, v -> cluster.getConfigPropertiesTypes(configType, stackId).getOrDefault(org.apache.ambari.server.state.PropertyInfo.PropertyType.PASSWORD, new java.util.HashSet<>()));
        return clusterPasswordProperties.get(clusterId).get(stackId).getOrDefault(configType, new java.util.HashSet<>());
    }

    private java.lang.String encryptAndDecoratePropertyValue(java.lang.String propertyValue) {
        final java.lang.String encrypted = encryptionService.encrypt(propertyValue, org.apache.ambari.server.utils.TextEncoding.BIN_HEX);
        return java.lang.String.format(org.apache.ambari.server.security.encryption.Encryptor.ENCRYPTED_PROPERTY_SCHEME, encrypted);
    }

    private java.lang.String encryptAndDecoratePropertyValue(java.lang.String propertyValue, java.lang.String encryptionKey) {
        final java.lang.String encrypted = encryptionService.encrypt(propertyValue, encryptionKey, org.apache.ambari.server.utils.TextEncoding.BIN_HEX);
        return java.lang.String.format(org.apache.ambari.server.security.encryption.Encryptor.ENCRYPTED_PROPERTY_SCHEME, encrypted);
    }

    protected void decrypt(java.util.Map<java.lang.String, java.lang.String> configProperties) {
        for (java.util.Map.Entry<java.lang.String, java.lang.String> property : configProperties.entrySet()) {
            if (isEncryptedPassword(property.getValue())) {
                configProperties.put(property.getKey(), decryptProperty(property.getValue()));
            }
        }
    }

    private java.lang.String decryptProperty(java.lang.String property) {
        final java.lang.String encrypted = property.substring(org.apache.ambari.server.security.encryption.Encryptor.ENCRYPTED_PROPERTY_PREFIX.length(), property.indexOf('}'));
        return encryptionService.decrypt(encrypted, org.apache.ambari.server.utils.TextEncoding.BIN_HEX);
    }
}