package org.apache.ambari.server.security.encryption;
public abstract class AbstractCredentialStore implements org.apache.ambari.server.security.encryption.CredentialStore {
    protected static final java.lang.String DEFAULT_STORE_TYPE = "JCEKS";

    private final java.util.concurrent.locks.Lock lock = new java.util.concurrent.locks.ReentrantLock();

    private org.apache.ambari.server.security.encryption.MasterKeyService masterKeyService;

    @java.lang.Override
    public void addCredential(java.lang.String alias, org.apache.ambari.server.security.credential.Credential credential) throws org.apache.ambari.server.AmbariException {
        if ((alias == null) || alias.isEmpty()) {
            throw new java.lang.IllegalArgumentException("Alias cannot be null or empty.");
        }
        lock.lock();
        try {
            java.security.KeyStore ks = loadCredentialStore();
            addCredential(ks, alias, credential);
            persistCredentialStore(ks);
        } finally {
            lock.unlock();
        }
    }

    @java.lang.Override
    public org.apache.ambari.server.security.credential.Credential getCredential(java.lang.String alias) throws org.apache.ambari.server.AmbariException {
        if (alias == null) {
            return null;
        } else {
            lock.lock();
            try {
                return getCredential(loadCredentialStore(), alias);
            } finally {
                lock.unlock();
            }
        }
    }

    @java.lang.Override
    public void removeCredential(java.lang.String alias) throws org.apache.ambari.server.AmbariException {
        if ((alias != null) && (!alias.isEmpty())) {
            lock.lock();
            try {
                java.security.KeyStore ks = loadCredentialStore();
                if (ks != null) {
                    try {
                        ks.deleteEntry(alias);
                        persistCredentialStore(ks);
                    } catch (java.security.KeyStoreException e) {
                        throw new org.apache.ambari.server.AmbariException("Failed to delete the KeyStore entry - the key store may not have been initialized", e);
                    }
                }
            } finally {
                lock.unlock();
            }
        }
    }

    @java.lang.Override
    public java.util.Set<java.lang.String> listCredentials() throws org.apache.ambari.server.AmbariException {
        java.util.Set<java.lang.String> credentials = null;
        lock.lock();
        try {
            java.security.KeyStore ks = loadCredentialStore();
            if (ks != null) {
                try {
                    java.util.Enumeration<java.lang.String> aliases = ks.aliases();
                    if (aliases != null) {
                        credentials = new java.util.HashSet<>();
                        while (aliases.hasMoreElements()) {
                            credentials.add(aliases.nextElement());
                        } 
                    }
                } catch (java.security.KeyStoreException e) {
                    throw new org.apache.ambari.server.AmbariException("Failed to read KeyStore - the key store may not have been initialized", e);
                }
            }
        } finally {
            lock.unlock();
        }
        return credentials;
    }

    @java.lang.Override
    public boolean containsCredential(java.lang.String alias) throws org.apache.ambari.server.AmbariException {
        boolean exists = false;
        if ((alias != null) && (!alias.isEmpty())) {
            lock.lock();
            try {
                java.security.KeyStore ks = loadCredentialStore();
                if (ks != null) {
                    try {
                        exists = ks.containsAlias(alias);
                    } catch (java.security.KeyStoreException e) {
                        throw new org.apache.ambari.server.AmbariException("Failed to search the KeyStore for the requested entry - the key store may not have been initialized", e);
                    }
                }
            } finally {
                lock.unlock();
            }
        }
        return exists;
    }

    @java.lang.Override
    public void setMasterKeyService(org.apache.ambari.server.security.encryption.MasterKeyService masterKeyService) {
        this.masterKeyService = masterKeyService;
    }

    protected void addCredential(java.security.KeyStore keyStore, java.lang.String alias, org.apache.ambari.server.security.credential.Credential credential) throws org.apache.ambari.server.AmbariException {
        if (keyStore != null) {
            try {
                java.security.Key key;
                char[] value = (credential == null) ? null : credential.toValue();
                if ((value == null) || (value.length == 0)) {
                    key = null;
                } else {
                    key = new javax.crypto.spec.SecretKeySpec(toBytes(value), "AES");
                }
                keyStore.setKeyEntry(alias, key, masterKeyService.getMasterSecret(), null);
            } catch (java.security.KeyStoreException e) {
                throw new org.apache.ambari.server.AmbariException("The key store has not been initialized", e);
            }
        }
    }

    protected org.apache.ambari.server.security.credential.Credential getCredential(java.security.KeyStore keyStore, java.lang.String alias) throws org.apache.ambari.server.AmbariException {
        char[] value = null;
        if (keyStore != null) {
            try {
                java.security.Key key = keyStore.getKey(alias, masterKeyService.getMasterSecret());
                if (key != null) {
                    value = toChars(key.getEncoded());
                }
            } catch (java.security.UnrecoverableKeyException e) {
                throw new org.apache.ambari.server.AmbariException("The key cannot be recovered (e.g., the given password is wrong)", e);
            } catch (java.security.KeyStoreException e) {
                throw new org.apache.ambari.server.AmbariException("The key store has not been initialized", e);
            } catch (java.security.NoSuchAlgorithmException e) {
                throw new org.apache.ambari.server.AmbariException(" if the algorithm for recovering the key cannot be found", e);
            }
        }
        return org.apache.ambari.server.security.credential.CredentialFactory.createCredential(value);
    }

    protected abstract void persistCredentialStore(java.security.KeyStore keyStore) throws org.apache.ambari.server.AmbariException;

    protected abstract java.security.KeyStore loadCredentialStore() throws org.apache.ambari.server.AmbariException;

    protected java.util.concurrent.locks.Lock getLock() {
        return lock;
    }

    protected java.security.KeyStore loadKeyStore(java.io.InputStream inputStream, java.lang.String keyStoreType) throws org.apache.ambari.server.AmbariException {
        if (masterKeyService == null) {
            throw new org.apache.ambari.server.AmbariException("Master Key Service is not set for this Credential store.");
        }
        java.security.KeyStore keyStore;
        try {
            keyStore = java.security.KeyStore.getInstance(keyStoreType);
        } catch (java.security.KeyStoreException e) {
            throw new org.apache.ambari.server.AmbariException(java.lang.String.format("No provider supports a key store implementation for the specified type: %s", keyStoreType), e);
        }
        try {
            keyStore.load(inputStream, masterKeyService.getMasterSecret());
        } catch (java.security.cert.CertificateException e) {
            throw new org.apache.ambari.server.AmbariException(java.lang.String.format("One or more credentials from the key store could not be loaded: %s", e.getLocalizedMessage()), e);
        } catch (java.security.NoSuchAlgorithmException e) {
            throw new org.apache.ambari.server.AmbariException(java.lang.String.format("The algorithm used to check the integrity of the key store cannot be found: %s", e.getLocalizedMessage()), e);
        } catch (java.io.IOException e) {
            if (e.getCause() instanceof java.security.UnrecoverableKeyException) {
                throw new org.apache.ambari.server.AmbariException(java.lang.String.format("The password used to decrypt the key store is incorrect: %s", e.getLocalizedMessage()), e);
            } else {
                throw new org.apache.ambari.server.AmbariException(java.lang.String.format("Failed to read the key store: %s", e.getLocalizedMessage()), e);
            }
        }
        return keyStore;
    }

    protected void writeKeyStore(java.security.KeyStore keyStore, java.io.OutputStream outputStream) throws org.apache.ambari.server.AmbariException {
        if (masterKeyService == null) {
            throw new org.apache.ambari.server.AmbariException("Master Key Service is not set for this Credential store.");
        }
        try {
            keyStore.store(outputStream, masterKeyService.getMasterSecret());
        } catch (java.security.cert.CertificateException e) {
            throw new org.apache.ambari.server.AmbariException(java.lang.String.format("A credential within in the key store data could not be stored: %s", e.getLocalizedMessage()), e);
        } catch (java.security.NoSuchAlgorithmException e) {
            throw new org.apache.ambari.server.AmbariException(java.lang.String.format("The appropriate data integrity algorithm could not be found: %s", e.getLocalizedMessage()), e);
        } catch (java.security.KeyStoreException e) {
            throw new org.apache.ambari.server.AmbariException(java.lang.String.format("The key store has not been initialized: %s", e.getLocalizedMessage()), e);
        } catch (java.io.IOException e) {
            throw new org.apache.ambari.server.AmbariException(java.lang.String.format("Failed to write the key store: %s", e.getLocalizedMessage()), e);
        }
    }

    protected byte[] toBytes(char[] chars) {
        if (chars == null) {
            return null;
        } else {
            java.nio.CharBuffer charBuffer = java.nio.CharBuffer.wrap(chars);
            java.nio.ByteBuffer byteBuffer = java.nio.charset.Charset.forName("UTF-8").encode(charBuffer);
            byte[] bytes = java.util.Arrays.copyOfRange(byteBuffer.array(), byteBuffer.position(), byteBuffer.limit());
            java.util.Arrays.fill(charBuffer.array(), '\u0000');
            java.util.Arrays.fill(byteBuffer.array(), ((byte) (0)));
            return bytes;
        }
    }

    protected char[] toChars(byte[] bytes) {
        if (bytes == null) {
            return null;
        } else {
            java.nio.ByteBuffer byteBuffer = java.nio.ByteBuffer.wrap(bytes);
            java.nio.CharBuffer charBuffer = java.nio.charset.Charset.forName("UTF-8").decode(byteBuffer);
            char[] chars = java.util.Arrays.copyOfRange(charBuffer.array(), charBuffer.position(), charBuffer.limit());
            java.util.Arrays.fill(charBuffer.array(), '\u0000');
            java.util.Arrays.fill(byteBuffer.array(), ((byte) (0)));
            return chars;
        }
    }
}