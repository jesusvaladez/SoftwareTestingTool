package org.apache.ambari.server.logging;
final class ProfiledReentrantReadWriteLock implements java.util.concurrent.locks.ReadWriteLock {
    private final org.apache.ambari.server.logging.ProfiledReentrantReadWriteLock.ProfiledReadLock readLock;

    private final org.apache.ambari.server.logging.ProfiledReentrantReadWriteLock.ProfiledWriteLock writeLock;

    ProfiledReentrantReadWriteLock(java.util.concurrent.locks.ReentrantReadWriteLock delegate, com.google.common.base.Ticker ticker, java.lang.String label) {
        readLock = new org.apache.ambari.server.logging.ProfiledReentrantReadWriteLock.ProfiledReadLock(delegate, ticker, label);
        writeLock = new org.apache.ambari.server.logging.ProfiledReentrantReadWriteLock.ProfiledWriteLock(delegate, ticker, label);
    }

    @java.lang.Override
    public org.apache.ambari.server.logging.ProfiledLock readLock() {
        return readLock;
    }

    @java.lang.Override
    public org.apache.ambari.server.logging.ProfiledLock writeLock() {
        return writeLock;
    }

    private static class ProfiledReadLock extends java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock implements org.apache.ambari.server.logging.ProfiledLock {
        private final org.apache.ambari.server.logging.LockProfileDelegate helper;

        private final java.util.concurrent.locks.ReentrantReadWriteLock delegate;

        ProfiledReadLock(java.util.concurrent.locks.ReentrantReadWriteLock delegate, com.google.common.base.Ticker ticker, java.lang.String label) {
            super(delegate);
            this.delegate = delegate;
            helper = new org.apache.ambari.server.logging.LockProfileDelegate(ticker, label, this);
        }

        @java.lang.Override
        public void lock() {
            boolean alreadyOwned = helper.logRequest();
            super.lock();
            helper.logRequestCompleted(alreadyOwned, true);
        }

        @java.lang.Override
        public void lockInterruptibly() throws java.lang.InterruptedException {
            boolean alreadyOwned = helper.logRequest();
            super.lockInterruptibly();
            helper.logRequestCompleted(alreadyOwned, true);
        }

        @java.lang.Override
        public boolean tryLock() {
            boolean alreadyOwned = helper.logRequest();
            boolean result = super.tryLock();
            helper.logRequestCompleted(alreadyOwned, result);
            return result;
        }

        @java.lang.Override
        public boolean tryLock(long timeout, java.util.concurrent.TimeUnit unit) throws java.lang.InterruptedException {
            boolean alreadyOwned = helper.logRequest();
            boolean result = super.tryLock(timeout, unit);
            helper.logRequestCompleted(alreadyOwned, result);
            return result;
        }

        @java.lang.Override
        public void unlock() {
            super.unlock();
            helper.logUnlock();
        }

        @java.lang.Override
        public boolean isHeldByCurrentThread() {
            return delegate.getReadHoldCount() > 0;
        }

        @java.lang.Override
        public java.util.Map<java.lang.String, java.lang.Long> getTimeSpentWaitingForLock() {
            return helper.getTimeSpentWaitingForLock();
        }

        @java.lang.Override
        public java.util.Map<java.lang.String, java.lang.Long> getTimeSpentLocked() {
            return helper.getTimeSpentLocked();
        }

        @java.lang.Override
        public java.util.Map<java.lang.String, java.lang.Integer> getLockCount() {
            return helper.getLockCount();
        }

        @java.lang.Override
        public java.lang.String toString() {
            return delegate.readLock().toString();
        }

        @java.lang.Override
        public java.lang.String getLabel() {
            return helper.getLabel();
        }
    }

    private static class ProfiledWriteLock extends java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock implements org.apache.ambari.server.logging.ProfiledLock {
        private final org.apache.ambari.server.logging.LockProfileDelegate helper;

        private final java.util.concurrent.locks.ReentrantReadWriteLock delegate;

        ProfiledWriteLock(java.util.concurrent.locks.ReentrantReadWriteLock delegate, com.google.common.base.Ticker ticker, java.lang.String label) {
            super(delegate);
            this.delegate = delegate;
            helper = new org.apache.ambari.server.logging.LockProfileDelegate(ticker, label, this);
        }

        @java.lang.Override
        public void lock() {
            boolean alreadyOwned = helper.logRequest();
            super.lock();
            helper.logRequestCompleted(alreadyOwned, true);
        }

        @java.lang.Override
        public void lockInterruptibly() throws java.lang.InterruptedException {
            boolean alreadyOwned = helper.logRequest();
            super.lockInterruptibly();
            helper.logRequestCompleted(alreadyOwned, true);
        }

        @java.lang.Override
        public boolean tryLock() {
            boolean alreadyOwned = helper.logRequest();
            boolean result = super.tryLock();
            helper.logRequestCompleted(alreadyOwned, result);
            return result;
        }

        @java.lang.Override
        public boolean tryLock(long timeout, java.util.concurrent.TimeUnit unit) throws java.lang.InterruptedException {
            boolean alreadyOwned = helper.logRequest();
            boolean result = super.tryLock(timeout, unit);
            helper.logRequestCompleted(alreadyOwned, result);
            return result;
        }

        @java.lang.Override
        public void unlock() {
            super.unlock();
            helper.logUnlock();
        }

        @java.lang.Override
        public java.util.Map<java.lang.String, java.lang.Long> getTimeSpentWaitingForLock() {
            return helper.getTimeSpentWaitingForLock();
        }

        @java.lang.Override
        public java.util.Map<java.lang.String, java.lang.Long> getTimeSpentLocked() {
            return helper.getTimeSpentLocked();
        }

        @java.lang.Override
        public java.util.Map<java.lang.String, java.lang.Integer> getLockCount() {
            return helper.getLockCount();
        }

        @java.lang.Override
        public java.lang.String toString() {
            return delegate.writeLock().toString();
        }

        @java.lang.Override
        public java.lang.String getLabel() {
            return helper.getLabel();
        }
    }
}