package org.apache.ambari.server.logging;
final class ProfiledReentrantLock implements org.apache.ambari.server.logging.ProfiledLock {
    private final java.util.concurrent.locks.ReentrantLock delegate;

    private final org.apache.ambari.server.logging.LockProfileDelegate helper;

    ProfiledReentrantLock(java.util.concurrent.locks.ReentrantLock delegate, com.google.common.base.Ticker ticker, java.lang.String label) {
        this.delegate = delegate;
        helper = new org.apache.ambari.server.logging.LockProfileDelegate(ticker, label, this);
    }

    @java.lang.Override
    public void lock() {
        boolean alreadyOwned = helper.logRequest();
        delegate.lock();
        helper.logRequestCompleted(alreadyOwned, true);
    }

    @java.lang.Override
    public void lockInterruptibly() throws java.lang.InterruptedException {
        boolean alreadyOwned = helper.logRequest();
        delegate.lockInterruptibly();
        helper.logRequestCompleted(alreadyOwned, true);
    }

    @java.lang.Override
    public boolean tryLock() {
        boolean alreadyOwned = helper.logRequest();
        boolean result = delegate.tryLock();
        helper.logRequestCompleted(alreadyOwned, result);
        return result;
    }

    @java.lang.Override
    public boolean tryLock(long timeout, java.util.concurrent.TimeUnit unit) throws java.lang.InterruptedException {
        boolean alreadyOwned = helper.logRequest();
        boolean result = delegate.tryLock(timeout, unit);
        helper.logRequestCompleted(alreadyOwned, result);
        return result;
    }

    @java.lang.Override
    public void unlock() {
        delegate.unlock();
        helper.logUnlock();
    }

    @java.lang.Override
    public java.util.concurrent.locks.Condition newCondition() {
        return delegate.newCondition();
    }

    @java.lang.Override
    public boolean isHeldByCurrentThread() {
        return delegate.isHeldByCurrentThread();
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
    public java.lang.String getLabel() {
        return helper.getLabel();
    }

    @java.lang.Override
    public java.lang.String toString() {
        return delegate.toString();
    }
}