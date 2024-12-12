package org.apache.ambari.server.security.encryption;
public interface MasterKeyService {
    char[] getMasterSecret();

    boolean isMasterKeyInitialized();
}