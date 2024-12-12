package org.apache.ambari.server.security.encryption;
@com.google.inject.Singleton
public class CredentialStoreServiceImpl implements org.apache.ambari.server.security.encryption.CredentialStoreService {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.security.encryption.CredentialStoreServiceImpl.class);

    private org.apache.ambari.server.security.SecurePasswordHelper securePasswordHelper;

    private org.apache.ambari.server.security.encryption.FileBasedCredentialStore persistedCredentialStore = null;

    private org.apache.ambari.server.security.encryption.InMemoryCredentialStore temporaryCredentialStore = null;

    @com.google.inject.Inject
    public CredentialStoreServiceImpl(org.apache.ambari.server.configuration.Configuration configuration, org.apache.ambari.server.security.SecurePasswordHelper securePasswordHelper) {
        this.securePasswordHelper = securePasswordHelper;
        if (configuration != null) {
            try {
                initializeTemporaryCredentialStore(configuration.getTemporaryKeyStoreRetentionMinutes(), java.util.concurrent.TimeUnit.MINUTES, configuration.isActivelyPurgeTemporaryKeyStore());
                org.apache.ambari.server.security.encryption.CredentialStoreServiceImpl.LOG.info("Initialized the temporary credential store. KeyStore entries will be retained for {} minutes and {} be actively purged", configuration.getTemporaryKeyStoreRetentionMinutes(), configuration.isActivelyPurgeTemporaryKeyStore() ? "will" : "will not");
            } catch (org.apache.ambari.server.AmbariException e) {
                org.apache.ambari.server.security.encryption.CredentialStoreServiceImpl.LOG.error("Failed to initialize the temporary credential store.  Storage of temporary credentials will fail.", e);
            }
            org.apache.ambari.server.security.encryption.MasterKeyService masterKeyService = new org.apache.ambari.server.security.encryption.MasterKeyServiceImpl(configuration);
            if (masterKeyService.isMasterKeyInitialized()) {
                try {
                    initializePersistedCredentialStore(configuration.getMasterKeyStoreLocation(), masterKeyService);
                    org.apache.ambari.server.security.encryption.CredentialStoreServiceImpl.LOG.info("Initialized the persistent credential store. Using KeyStore file at {}", persistedCredentialStore.getKeyStorePath().getAbsolutePath());
                } catch (org.apache.ambari.server.AmbariException e) {
                    org.apache.ambari.server.security.encryption.CredentialStoreServiceImpl.LOG.error("Failed to initialize the persistent credential store.  Storage of persisted credentials will fail.", e);
                }
            }
        }
    }

    public synchronized void initializeTemporaryCredentialStore(long retentionDuration, java.util.concurrent.TimeUnit units, boolean activelyPurge) throws org.apache.ambari.server.AmbariException {
        if (isInitialized(org.apache.ambari.server.security.encryption.CredentialStoreType.TEMPORARY)) {
            throw new org.apache.ambari.server.AmbariException("This temporary CredentialStore has already been initialized");
        }
        temporaryCredentialStore = new org.apache.ambari.server.security.encryption.InMemoryCredentialStore(retentionDuration, units, activelyPurge);
        temporaryCredentialStore.setMasterKeyService(new org.apache.ambari.server.security.encryption.MasterKeyServiceImpl(securePasswordHelper.createSecurePassword()));
    }

    public synchronized void initializePersistedCredentialStore(java.io.File credentialStoreLocation, org.apache.ambari.server.security.encryption.MasterKeyService masterKeyService) throws org.apache.ambari.server.AmbariException {
        if (isInitialized(org.apache.ambari.server.security.encryption.CredentialStoreType.PERSISTED)) {
            throw new org.apache.ambari.server.AmbariException("This persisted CredentialStore has already been initialized");
        }
        persistedCredentialStore = new org.apache.ambari.server.security.encryption.FileBasedCredentialStore(credentialStoreLocation);
        persistedCredentialStore.setMasterKeyService(masterKeyService);
    }

    @java.lang.Override
    public void setCredential(java.lang.String clusterName, java.lang.String alias, org.apache.ambari.server.security.credential.Credential credential, org.apache.ambari.server.security.encryption.CredentialStoreType credentialStoreType) throws org.apache.ambari.server.AmbariException {
        validateInitialized(credentialStoreType);
        removeCredential(clusterName, alias);
        getCredentialStore(credentialStoreType).addCredential(org.apache.ambari.server.security.encryption.CredentialStoreServiceImpl.canonicalizeAlias(clusterName, alias), credential);
    }

    @java.lang.Override
    public org.apache.ambari.server.security.credential.Credential getCredential(java.lang.String clusterName, java.lang.String alias) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.security.credential.Credential credential = getCredential(clusterName, alias, org.apache.ambari.server.security.encryption.CredentialStoreType.TEMPORARY);
        if (credential == null) {
            credential = getCredential(clusterName, alias, org.apache.ambari.server.security.encryption.CredentialStoreType.PERSISTED);
        }
        return credential;
    }

    @java.lang.Override
    public org.apache.ambari.server.security.credential.Credential getCredential(java.lang.String clusterName, java.lang.String alias, org.apache.ambari.server.security.encryption.CredentialStoreType credentialStoreType) throws org.apache.ambari.server.AmbariException {
        return isInitialized(credentialStoreType) ? getCredentialStore(credentialStoreType).getCredential(org.apache.ambari.server.security.encryption.CredentialStoreServiceImpl.canonicalizeAlias(clusterName, alias)) : null;
    }

    @java.lang.Override
    public void removeCredential(java.lang.String clusterName, java.lang.String alias) throws org.apache.ambari.server.AmbariException {
        removeCredential(clusterName, alias, org.apache.ambari.server.security.encryption.CredentialStoreType.PERSISTED);
        removeCredential(clusterName, alias, org.apache.ambari.server.security.encryption.CredentialStoreType.TEMPORARY);
    }

    @java.lang.Override
    public void removeCredential(java.lang.String clusterName, java.lang.String alias, org.apache.ambari.server.security.encryption.CredentialStoreType credentialStoreType) throws org.apache.ambari.server.AmbariException {
        if (isInitialized(credentialStoreType)) {
            getCredentialStore(credentialStoreType).removeCredential(org.apache.ambari.server.security.encryption.CredentialStoreServiceImpl.canonicalizeAlias(clusterName, alias));
        }
    }

    @java.lang.Override
    public boolean containsCredential(java.lang.String clusterName, java.lang.String alias) throws org.apache.ambari.server.AmbariException {
        return containsCredential(clusterName, alias, org.apache.ambari.server.security.encryption.CredentialStoreType.TEMPORARY) || containsCredential(clusterName, alias, org.apache.ambari.server.security.encryption.CredentialStoreType.PERSISTED);
    }

    @java.lang.Override
    public boolean containsCredential(java.lang.String clusterName, java.lang.String alias, org.apache.ambari.server.security.encryption.CredentialStoreType credentialStoreType) throws org.apache.ambari.server.AmbariException {
        return isInitialized(credentialStoreType) && getCredentialStore(credentialStoreType).containsCredential(org.apache.ambari.server.security.encryption.CredentialStoreServiceImpl.canonicalizeAlias(clusterName, alias));
    }

    @java.lang.Override
    public org.apache.ambari.server.security.encryption.CredentialStoreType getCredentialStoreType(java.lang.String clusterName, java.lang.String alias) throws org.apache.ambari.server.AmbariException {
        if (containsCredential(clusterName, alias, org.apache.ambari.server.security.encryption.CredentialStoreType.TEMPORARY)) {
            return org.apache.ambari.server.security.encryption.CredentialStoreType.TEMPORARY;
        } else if (containsCredential(clusterName, alias, org.apache.ambari.server.security.encryption.CredentialStoreType.PERSISTED)) {
            return org.apache.ambari.server.security.encryption.CredentialStoreType.PERSISTED;
        } else {
            throw new org.apache.ambari.server.AmbariException("The alias was not found in either the persisted or temporary credential stores");
        }
    }

    @java.lang.Override
    public java.util.Map<java.lang.String, org.apache.ambari.server.security.encryption.CredentialStoreType> listCredentials(java.lang.String clusterName) throws org.apache.ambari.server.AmbariException {
        if (!isInitialized()) {
            throw new org.apache.ambari.server.AmbariException("This CredentialStoreService has not yet been initialized");
        }
        java.util.Collection<java.lang.String> persistedAliases = (isInitialized(org.apache.ambari.server.security.encryption.CredentialStoreType.PERSISTED)) ? persistedCredentialStore.listCredentials() : null;
        java.util.Collection<java.lang.String> temporaryAliases = (isInitialized(org.apache.ambari.server.security.encryption.CredentialStoreType.TEMPORARY)) ? temporaryCredentialStore.listCredentials() : null;
        java.util.Map<java.lang.String, org.apache.ambari.server.security.encryption.CredentialStoreType> map = new java.util.HashMap<>();
        if (persistedAliases != null) {
            for (java.lang.String alias : persistedAliases) {
                if (isAliasRequested(clusterName, alias)) {
                    map.put(org.apache.ambari.server.security.encryption.CredentialStoreServiceImpl.decanonicalizeAlias(clusterName, alias), org.apache.ambari.server.security.encryption.CredentialStoreType.PERSISTED);
                }
            }
        }
        if (temporaryAliases != null) {
            for (java.lang.String alias : temporaryAliases) {
                if (isAliasRequested(clusterName, alias)) {
                    map.put(org.apache.ambari.server.security.encryption.CredentialStoreServiceImpl.decanonicalizeAlias(clusterName, alias), org.apache.ambari.server.security.encryption.CredentialStoreType.TEMPORARY);
                }
            }
        }
        return map;
    }

    @java.lang.Override
    public synchronized boolean isInitialized() {
        return isInitialized(org.apache.ambari.server.security.encryption.CredentialStoreType.PERSISTED) || isInitialized(org.apache.ambari.server.security.encryption.CredentialStoreType.TEMPORARY);
    }

    @java.lang.Override
    public synchronized boolean isInitialized(org.apache.ambari.server.security.encryption.CredentialStoreType credentialStoreType) {
        if (org.apache.ambari.server.security.encryption.CredentialStoreType.PERSISTED == credentialStoreType) {
            return persistedCredentialStore != null;
        } else if (org.apache.ambari.server.security.encryption.CredentialStoreType.TEMPORARY == credentialStoreType) {
            return temporaryCredentialStore != null;
        } else {
            throw new java.lang.IllegalArgumentException("Invalid or unexpected credential store type specified");
        }
    }

    public static java.lang.String canonicalizeAlias(java.lang.String clusterName, java.lang.String alias) {
        java.lang.String canonicaizedAlias;
        if ((((clusterName == null) || clusterName.isEmpty()) || (alias == null)) || alias.isEmpty()) {
            canonicaizedAlias = alias;
        } else {
            java.lang.String prefix = org.apache.ambari.server.security.encryption.CredentialStoreServiceImpl.createAliasPrefix(clusterName);
            if (alias.toLowerCase().startsWith(prefix)) {
                canonicaizedAlias = alias;
            } else {
                canonicaizedAlias = prefix + alias;
            }
        }
        return canonicaizedAlias == null ? null : canonicaizedAlias.toLowerCase();
    }

    public static java.lang.String decanonicalizeAlias(java.lang.String clusterName, java.lang.String canonicaizedAlias) {
        if ((((clusterName == null) || clusterName.isEmpty()) || (canonicaizedAlias == null)) || canonicaizedAlias.isEmpty()) {
            return canonicaizedAlias;
        } else {
            java.lang.String prefix = org.apache.ambari.server.security.encryption.CredentialStoreServiceImpl.createAliasPrefix(clusterName);
            if (canonicaizedAlias.startsWith(prefix)) {
                return canonicaizedAlias.substring(prefix.length());
            } else {
                return canonicaizedAlias;
            }
        }
    }

    private static java.lang.String createAliasPrefix(java.lang.String clusterName) {
        return (("cluster." + clusterName) + ".").toLowerCase();
    }

    private boolean isAliasRequested(java.lang.String clusterName, java.lang.String canonicalizedAlias) {
        return (clusterName == null) || canonicalizedAlias.toLowerCase().startsWith(org.apache.ambari.server.security.encryption.CredentialStoreServiceImpl.createAliasPrefix(clusterName));
    }

    private org.apache.ambari.server.security.encryption.CredentialStore getCredentialStore(org.apache.ambari.server.security.encryption.CredentialStoreType credentialStoreType) {
        if (org.apache.ambari.server.security.encryption.CredentialStoreType.PERSISTED == credentialStoreType) {
            return persistedCredentialStore;
        } else if (org.apache.ambari.server.security.encryption.CredentialStoreType.TEMPORARY == credentialStoreType) {
            return temporaryCredentialStore;
        } else {
            throw new java.lang.IllegalArgumentException("Invalid or unexpected credential store type specified");
        }
    }

    private void validateInitialized(org.apache.ambari.server.security.encryption.CredentialStoreType credentialStoreType) throws org.apache.ambari.server.AmbariException {
        if (!isInitialized(credentialStoreType)) {
            throw new org.apache.ambari.server.AmbariException(java.lang.String.format("The %s CredentialStore for this CredentialStoreService has not yet been initialized", credentialStoreType.name().toLowerCase()));
        }
    }
}