package org.apache.ambari.server.security.encryption;
public class InMemoryCredentialStore extends org.apache.ambari.server.security.encryption.AbstractCredentialStore {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.security.encryption.InMemoryCredentialStore.class);

    private final com.google.common.cache.Cache<java.lang.String, java.security.KeyStore> cache;

    public InMemoryCredentialStore() {
        this(0, java.util.concurrent.TimeUnit.MINUTES, false);
    }

    public InMemoryCredentialStore(final long retentionDuration, final java.util.concurrent.TimeUnit units, boolean activelyPurge) {
        com.google.common.cache.CacheBuilder<java.lang.Object, java.lang.Object> builder = com.google.common.cache.CacheBuilder.newBuilder();
        if (retentionDuration > 0) {
            if (activelyPurge) {
                java.util.concurrent.ThreadFactory threadFactory = new java.util.concurrent.ThreadFactory() {
                    @java.lang.Override
                    public java.lang.Thread newThread(java.lang.Runnable runnable) {
                        java.lang.Thread t = java.util.concurrent.Executors.defaultThreadFactory().newThread(runnable);
                        if (t != null) {
                            t.setName(java.lang.String.format("%s active cleanup timer", org.apache.ambari.server.security.encryption.InMemoryCredentialStore.class.getSimpleName()));
                            t.setDaemon(true);
                        }
                        return t;
                    }
                };
                java.lang.Runnable runnable = new java.lang.Runnable() {
                    @java.lang.Override
                    public void run() {
                        if (org.apache.ambari.server.security.encryption.InMemoryCredentialStore.LOG.isDebugEnabled()) {
                            org.apache.ambari.server.security.encryption.InMemoryCredentialStore.LOG.debug("Cleaning up cache due to retention timeout of {} milliseconds", units.toMillis(retentionDuration));
                        }
                        cache.cleanUp();
                    }
                };
                java.util.concurrent.Executors.newSingleThreadScheduledExecutor(threadFactory).schedule(runnable, 1, java.util.concurrent.TimeUnit.MINUTES);
            }
            builder.expireAfterWrite(retentionDuration, units);
        }
        cache = builder.build();
    }

    @java.lang.Override
    public void addCredential(java.lang.String alias, org.apache.ambari.server.security.credential.Credential credential) throws org.apache.ambari.server.AmbariException {
        if ((alias == null) || alias.isEmpty()) {
            throw new java.lang.IllegalArgumentException("Alias cannot be null or empty.");
        }
        java.util.concurrent.locks.Lock lock = getLock();
        lock.lock();
        try {
            java.security.KeyStore keyStore = loadKeyStore(null, org.apache.ambari.server.security.encryption.AbstractCredentialStore.DEFAULT_STORE_TYPE);
            addCredential(keyStore, alias, credential);
            cache.put(alias, keyStore);
        } finally {
            lock.unlock();
        }
    }

    @java.lang.Override
    public org.apache.ambari.server.security.credential.Credential getCredential(java.lang.String alias) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.security.credential.Credential credential = null;
        if ((alias != null) && (!alias.isEmpty())) {
            java.util.concurrent.locks.Lock lock = getLock();
            lock.lock();
            try {
                java.security.KeyStore keyStore = cache.getIfPresent(alias);
                if (keyStore != null) {
                    credential = getCredential(keyStore, alias);
                }
            } finally {
                lock.unlock();
            }
        }
        return credential;
    }

    @java.lang.Override
    public void removeCredential(java.lang.String alias) throws org.apache.ambari.server.AmbariException {
        if (alias != null) {
            java.util.concurrent.locks.Lock lock = getLock();
            lock.lock();
            try {
                cache.invalidate(alias);
            } finally {
                lock.unlock();
            }
        }
    }

    @java.lang.Override
    public java.util.Set<java.lang.String> listCredentials() throws org.apache.ambari.server.AmbariException {
        java.util.concurrent.locks.Lock lock = getLock();
        lock.lock();
        try {
            return new java.util.HashSet<>(cache.asMap().keySet());
        } finally {
            lock.unlock();
        }
    }

    @java.lang.Override
    public boolean containsCredential(java.lang.String alias) throws org.apache.ambari.server.AmbariException {
        java.util.concurrent.locks.Lock lock = getLock();
        lock.lock();
        try {
            return ((alias != null) && (!alias.isEmpty())) && (cache.getIfPresent(alias) != null);
        } finally {
            lock.unlock();
        }
    }

    @java.lang.Override
    protected void persistCredentialStore(java.security.KeyStore keyStore) throws org.apache.ambari.server.AmbariException {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    protected java.security.KeyStore loadCredentialStore() throws org.apache.ambari.server.AmbariException {
        throw new java.lang.UnsupportedOperationException();
    }
}