package org.apache.ambari.server.agent;
public class AgentEncryptionKey implements org.apache.ambari.server.security.credential.Credential {
    private static final java.lang.String ALIAS = "agent.encryption.key";

    private static final org.apache.ambari.server.security.SecurePasswordHelper securePasswordHelper = new org.apache.ambari.server.security.SecurePasswordHelper();

    private final char[] key;

    public static org.apache.ambari.server.agent.AgentEncryptionKey random() {
        return new org.apache.ambari.server.agent.AgentEncryptionKey(org.apache.ambari.server.agent.AgentEncryptionKey.securePasswordHelper.createSecurePassword().toCharArray());
    }

    public static org.apache.ambari.server.agent.AgentEncryptionKey loadFrom(org.apache.ambari.server.security.encryption.CredentialStoreService credentialStoreService, boolean createIfNotFound) {
        try {
            if (!credentialStoreService.containsCredential(null, org.apache.ambari.server.agent.AgentEncryptionKey.ALIAS)) {
                if (createIfNotFound) {
                    org.apache.ambari.server.agent.AgentEncryptionKey encryptionKey = org.apache.ambari.server.agent.AgentEncryptionKey.random();
                    encryptionKey.saveToCredentialStore(credentialStoreService);
                    return org.apache.ambari.server.agent.AgentEncryptionKey.loadKey(credentialStoreService);
                } else {
                    throw new org.apache.ambari.server.AmbariRuntimeException(("AgentEncryptionKey with alias: " + org.apache.ambari.server.agent.AgentEncryptionKey.ALIAS) + " doesn't exist in credential store.");
                }
            } else {
                return org.apache.ambari.server.agent.AgentEncryptionKey.loadKey(credentialStoreService);
            }
        } catch (org.apache.ambari.server.AmbariException e) {
            throw new org.apache.ambari.server.AmbariRuntimeException("Cannot load agent encryption key: " + e.getMessage(), e);
        }
    }

    private static org.apache.ambari.server.agent.AgentEncryptionKey loadKey(org.apache.ambari.server.security.encryption.CredentialStoreService credentialStoreService) throws org.apache.ambari.server.AmbariException {
        return new org.apache.ambari.server.agent.AgentEncryptionKey(credentialStoreService.getCredential(null, org.apache.ambari.server.agent.AgentEncryptionKey.ALIAS).toValue());
    }

    public AgentEncryptionKey(char[] key) {
        this.key = java.util.Arrays.copyOf(key, key.length);
    }

    @java.lang.Override
    public java.lang.String toString() {
        return new java.lang.String(key);
    }

    @java.lang.Override
    public char[] toValue() {
        return key;
    }

    public void saveToCredentialStore(org.apache.ambari.server.security.encryption.CredentialStoreService credentialStoreService) {
        try {
            credentialStoreService.setCredential(null, org.apache.ambari.server.agent.AgentEncryptionKey.ALIAS, this, org.apache.ambari.server.security.encryption.CredentialStoreType.PERSISTED);
        } catch (org.apache.ambari.server.AmbariException e) {
            throw new org.apache.ambari.server.AmbariRuntimeException("Cannot save agent encryption key: " + e.getMessage(), e);
        }
    }
}