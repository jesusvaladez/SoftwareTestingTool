package org.apache.ambari.server.orm;
import com.google.inject.persist.Transactional;
@com.google.inject.Singleton
public class TransactionalLocks {
    private final org.apache.ambari.server.configuration.Configuration m_configuration;

    private final java.util.concurrent.ConcurrentHashMap<org.apache.ambari.annotations.TransactionalLock.LockArea, java.util.concurrent.locks.ReadWriteLock> m_locks;

    @com.google.inject.Inject
    private TransactionalLocks(org.apache.ambari.server.configuration.Configuration configuration) {
        m_configuration = configuration;
        m_locks = new java.util.concurrent.ConcurrentHashMap<>();
        for (org.apache.ambari.annotations.TransactionalLock.LockArea lockArea : org.apache.ambari.annotations.TransactionalLock.LockArea.values()) {
            final java.util.concurrent.locks.ReadWriteLock lock;
            if (lockArea.isEnabled(m_configuration)) {
                lock = new java.util.concurrent.locks.ReentrantReadWriteLock(true);
            } else {
                lock = new org.apache.ambari.server.orm.TransactionalLocks.NoOperationReadWriteLock();
            }
            m_locks.put(lockArea, lock);
        }
    }

    public java.util.concurrent.locks.ReadWriteLock getLock(org.apache.ambari.annotations.TransactionalLock.LockArea lockArea) {
        return m_locks.get(lockArea);
    }

    private static final class NoOperationReadWriteLock implements java.util.concurrent.locks.ReadWriteLock {
        private final java.util.concurrent.locks.Lock m_readLock = new org.apache.ambari.server.orm.TransactionalLocks.NoOperationLock();

        private final java.util.concurrent.locks.Lock m_writeLock = new org.apache.ambari.server.orm.TransactionalLocks.NoOperationLock();

        @java.lang.Override
        public java.util.concurrent.locks.Lock readLock() {
            return m_readLock;
        }

        @java.lang.Override
        public java.util.concurrent.locks.Lock writeLock() {
            return m_writeLock;
        }
    }

    private static final class NoOperationLock implements java.util.concurrent.locks.Lock {
        @java.lang.Override
        public void lock() {
        }

        @java.lang.Override
        public void lockInterruptibly() throws java.lang.InterruptedException {
        }

        @java.lang.Override
        public boolean tryLock() {
            return true;
        }

        @java.lang.Override
        public boolean tryLock(long time, java.util.concurrent.TimeUnit unit) throws java.lang.InterruptedException {
            return true;
        }

        @java.lang.Override
        public void unlock() {
        }

        @java.lang.Override
        public java.util.concurrent.locks.Condition newCondition() {
            return null;
        }
    }
}