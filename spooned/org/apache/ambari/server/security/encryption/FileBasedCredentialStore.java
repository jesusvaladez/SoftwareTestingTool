package org.apache.ambari.server.security.encryption;
public class FileBasedCredentialStore extends org.apache.ambari.server.security.encryption.AbstractCredentialStore {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.security.encryption.FileBasedCredentialStore.class);

    private java.io.File keyStoreFile;

    public FileBasedCredentialStore(java.io.File keyStoreLocation) {
        if (keyStoreLocation == null) {
            org.apache.ambari.server.security.encryption.FileBasedCredentialStore.LOG.warn("Writing key store to the current working directory of the running process");
            keyStoreLocation = new java.io.File(org.apache.ambari.server.configuration.Configuration.MASTER_KEYSTORE_FILENAME_DEFAULT);
        } else if (keyStoreLocation.isDirectory()) {
            keyStoreLocation = new java.io.File(keyStoreLocation, org.apache.ambari.server.configuration.Configuration.MASTER_KEYSTORE_FILENAME_DEFAULT);
        }
        if (keyStoreLocation.exists()) {
            if (!keyStoreLocation.canWrite()) {
                org.apache.ambari.server.security.encryption.FileBasedCredentialStore.LOG.warn("The destination file is not writable. Failures may occur when writing the key store to disk: {}", keyStoreLocation.getAbsolutePath());
            }
        } else {
            java.io.File directory = keyStoreLocation.getParentFile();
            if ((directory != null) && (!directory.canWrite())) {
                org.apache.ambari.server.security.encryption.FileBasedCredentialStore.LOG.warn("The destination directory is not writable. Failures may occur when writing the key store to disk: {}", keyStoreLocation.getAbsolutePath());
            }
        }
        this.keyStoreFile = keyStoreLocation;
    }

    public java.io.File getKeyStorePath() {
        return keyStoreFile;
    }

    @java.lang.Override
    protected void persistCredentialStore(java.security.KeyStore keyStore) throws org.apache.ambari.server.AmbariException {
        putKeyStore(keyStore, this.keyStoreFile);
    }

    @java.lang.Override
    protected java.security.KeyStore loadCredentialStore() throws org.apache.ambari.server.AmbariException {
        return getKeyStore(this.keyStoreFile, org.apache.ambari.server.security.encryption.AbstractCredentialStore.DEFAULT_STORE_TYPE);
    }

    private java.security.KeyStore getKeyStore(final java.io.File keyStoreFile, java.lang.String keyStoreType) throws org.apache.ambari.server.AmbariException {
        java.security.KeyStore keyStore;
        java.io.FileInputStream inputStream;
        if (keyStoreFile.exists()) {
            if (keyStoreFile.length() > 0) {
                org.apache.ambari.server.security.encryption.FileBasedCredentialStore.LOG.debug("Reading key store from {}", keyStoreFile.getAbsolutePath());
                try {
                    inputStream = new java.io.FileInputStream(keyStoreFile);
                } catch (java.io.FileNotFoundException e) {
                    throw new org.apache.ambari.server.AmbariException(java.lang.String.format("Failed to open the key store file: %s", e.getLocalizedMessage()), e);
                }
            } else {
                org.apache.ambari.server.security.encryption.FileBasedCredentialStore.LOG.debug("The key store file found in {} is empty. Returning new (non-persisted) KeyStore", keyStoreFile.getAbsolutePath());
                inputStream = null;
            }
        } else {
            org.apache.ambari.server.security.encryption.FileBasedCredentialStore.LOG.debug("Key store file not found in {}. Returning new (non-persisted) KeyStore", keyStoreFile.getAbsolutePath());
            inputStream = null;
        }
        try {
            keyStore = loadKeyStore(inputStream, keyStoreType);
        } finally {
            org.apache.commons.io.IOUtils.closeQuietly(inputStream);
        }
        return keyStore;
    }

    private void putKeyStore(java.security.KeyStore keyStore, java.io.File keyStoreFile) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.security.encryption.FileBasedCredentialStore.LOG.debug("Writing key store to {}", keyStoreFile.getAbsolutePath());
        java.io.FileOutputStream outputStream = null;
        try {
            outputStream = new java.io.FileOutputStream(this.keyStoreFile);
            writeKeyStore(keyStore, outputStream);
        } catch (java.io.FileNotFoundException e) {
            throw new org.apache.ambari.server.AmbariException(java.lang.String.format("Failed to open the key store file: %s", e.getLocalizedMessage()), e);
        } finally {
            org.apache.commons.io.IOUtils.closeQuietly(outputStream);
        }
    }
}