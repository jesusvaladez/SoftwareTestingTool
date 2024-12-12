package org.apache.ambari.server.logging;
@com.google.inject.Singleton
public class LockFactory {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.logging.LockFactory.class);

    private final boolean profiling;

    private final java.util.Set<org.apache.ambari.server.logging.ProfiledLock> profiledLocks;

    @com.google.inject.Inject
    public LockFactory(org.apache.ambari.server.configuration.Configuration config) {
        profiling = config.isServerLocksProfilingEnabled();
        profiledLocks = (profiling) ? new java.util.concurrent.CopyOnWriteArraySet<>() : null;
        org.apache.ambari.server.logging.LockFactory.LOG.info("Lock profiling is {}", profiling ? "enabled" : "disabled");
    }

    public java.util.concurrent.locks.Lock newLock() {
        return newLock(org.apache.ambari.server.logging.LockFactory.getDefaultPrefix());
    }

    public java.util.concurrent.locks.Lock newLock(java.lang.String label) {
        java.util.concurrent.locks.ReentrantLock baseLock = new java.util.concurrent.locks.ReentrantLock();
        if (profiling) {
            org.apache.ambari.server.logging.ProfiledReentrantLock profiledLock = new org.apache.ambari.server.logging.ProfiledReentrantLock(baseLock, com.google.common.base.Ticker.systemTicker(), label);
            profiledLocks.add(profiledLock);
            return profiledLock;
        }
        return baseLock;
    }

    public java.util.concurrent.locks.ReadWriteLock newReadWriteLock() {
        return newReadWriteLock(org.apache.ambari.server.logging.LockFactory.getDefaultPrefix());
    }

    public java.util.concurrent.locks.ReadWriteLock newReadWriteLock(java.lang.String label) {
        java.util.concurrent.locks.ReentrantReadWriteLock baseLock = new java.util.concurrent.locks.ReentrantReadWriteLock();
        if (profiling) {
            org.apache.ambari.server.logging.ProfiledReentrantReadWriteLock profiledLock = new org.apache.ambari.server.logging.ProfiledReentrantReadWriteLock(baseLock, com.google.common.base.Ticker.systemTicker(), label);
            profiledLocks.add(profiledLock.readLock());
            profiledLocks.add(profiledLock.writeLock());
            return profiledLock;
        }
        return baseLock;
    }

    public void debugDump(java.lang.StringBuilder sb) {
        if (profiling) {
            sb.append("\n\t\tLocks: [");
            for (org.apache.ambari.server.logging.ProfiledLock lock : profiledLocks) {
                sb.append("\n\t\t\t").append(lock.getLabel()).append(lock).append(" waited: ").append(lock.getTimeSpentWaitingForLock()).append(" held: ").append(lock.getTimeSpentLocked()).append(" times locked: ").append(lock.getLockCount());
            }
            if (!profiledLocks.isEmpty()) {
                sb.append("\n");
            }
            sb.append("]");
        }
    }

    private static java.lang.String getDefaultPrefix() {
        java.lang.StackTraceElement[] stackTrace = java.lang.Thread.currentThread().getStackTrace();
        return stackTrace.length > 3 ? (stackTrace[3].getFileName() + ":") + stackTrace[3].getLineNumber() : "";
    }
}