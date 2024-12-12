package org.apache.ambari.server.logging;
import org.easymock.EasyMockSupport;
import static org.easymock.EasyMock.expect;
public class ProfiledReentrantReadWriteLockTest extends org.easymock.EasyMockSupport {
    private static final java.lang.String LABEL = "label";

    @org.junit.Test
    public void lockingReadLockOnlyLocksReadLock() {
        java.util.concurrent.locks.ReentrantReadWriteLock delegate = new java.util.concurrent.locks.ReentrantReadWriteLock();
        org.apache.ambari.server.logging.ProfiledReentrantReadWriteLock testSubject = new org.apache.ambari.server.logging.ProfiledReentrantReadWriteLock(delegate, com.google.common.base.Ticker.systemTicker(), org.apache.ambari.server.logging.ProfiledReentrantReadWriteLockTest.LABEL);
        testSubject.readLock().lock();
        org.junit.Assert.assertEquals(1, delegate.getReadHoldCount());
        org.junit.Assert.assertEquals(0, delegate.getWriteHoldCount());
    }

    @org.junit.Test
    public void lockingWriteLockOnlyLocksWriteLock() {
        java.util.concurrent.locks.ReentrantReadWriteLock delegate = new java.util.concurrent.locks.ReentrantReadWriteLock();
        org.apache.ambari.server.logging.ProfiledReentrantReadWriteLock testSubject = new org.apache.ambari.server.logging.ProfiledReentrantReadWriteLock(delegate, com.google.common.base.Ticker.systemTicker(), org.apache.ambari.server.logging.ProfiledReentrantReadWriteLockTest.LABEL);
        testSubject.writeLock().lock();
        org.junit.Assert.assertEquals(0, delegate.getReadHoldCount());
        org.junit.Assert.assertEquals(1, delegate.getWriteHoldCount());
    }

    @org.junit.Test
    public void timeWaitingForReadLockIsRecorded() {
        com.google.common.base.Ticker ticker = createMock(com.google.common.base.Ticker.class);
        org.apache.ambari.server.logging.ProfiledLock testSubject = new org.apache.ambari.server.logging.ProfiledReentrantReadWriteLock(new java.util.concurrent.locks.ReentrantReadWriteLock(), ticker, org.apache.ambari.server.logging.ProfiledReentrantReadWriteLockTest.LABEL).readLock();
        timeWaitingForLockIsRecorded(testSubject, ticker);
    }

    @org.junit.Test
    public void timeWaitingForWriteLockIsRecorded() {
        com.google.common.base.Ticker ticker = createMock(com.google.common.base.Ticker.class);
        org.apache.ambari.server.logging.ProfiledLock testSubject = new org.apache.ambari.server.logging.ProfiledReentrantReadWriteLock(new java.util.concurrent.locks.ReentrantReadWriteLock(), ticker, org.apache.ambari.server.logging.ProfiledReentrantReadWriteLockTest.LABEL).writeLock();
        timeWaitingForLockIsRecorded(testSubject, ticker);
    }

    private void timeWaitingForLockIsRecorded(org.apache.ambari.server.logging.ProfiledLock testSubject, com.google.common.base.Ticker ticker) {
        EasyMock.expect(ticker.read()).andReturn(java.util.concurrent.TimeUnit.MILLISECONDS.toNanos(1L));
        EasyMock.expect(ticker.read()).andReturn(java.util.concurrent.TimeUnit.MILLISECONDS.toNanos(4L));
        EasyMock.expect(ticker.read()).andReturn(java.util.concurrent.TimeUnit.MILLISECONDS.toNanos(5L));
        replayAll();
        testSubject.lock();
        org.junit.Assert.assertEquals(java.util.Collections.singletonMap(java.lang.Thread.currentThread().getName(), 4L - 1L), testSubject.getTimeSpentWaitingForLock());
        verifyAll();
    }

    @org.junit.Test
    public void timeReadLockSpentLockedIsRecorded() {
        com.google.common.base.Ticker ticker = createMock(com.google.common.base.Ticker.class);
        org.apache.ambari.server.logging.ProfiledLock testSubject = new org.apache.ambari.server.logging.ProfiledReentrantReadWriteLock(new java.util.concurrent.locks.ReentrantReadWriteLock(), ticker, org.apache.ambari.server.logging.ProfiledReentrantReadWriteLockTest.LABEL).readLock();
        timeSpentLockedIsRecorded(ticker, testSubject);
    }

    @org.junit.Test
    public void timeWriteLockSpentLockedIsRecorded() {
        com.google.common.base.Ticker ticker = createMock(com.google.common.base.Ticker.class);
        org.apache.ambari.server.logging.ProfiledLock testSubject = new org.apache.ambari.server.logging.ProfiledReentrantReadWriteLock(new java.util.concurrent.locks.ReentrantReadWriteLock(), ticker, org.apache.ambari.server.logging.ProfiledReentrantReadWriteLockTest.LABEL).writeLock();
        timeSpentLockedIsRecorded(ticker, testSubject);
    }

    @org.junit.Test
    public void timeLockSpentLockedIsRecorded() {
        com.google.common.base.Ticker ticker = createMock(com.google.common.base.Ticker.class);
        org.apache.ambari.server.logging.ProfiledLock testSubject = new org.apache.ambari.server.logging.ProfiledReentrantLock(new java.util.concurrent.locks.ReentrantLock(), ticker, org.apache.ambari.server.logging.ProfiledReentrantReadWriteLockTest.LABEL);
        timeSpentLockedIsRecorded(ticker, testSubject);
    }

    private void timeSpentLockedIsRecorded(com.google.common.base.Ticker ticker, org.apache.ambari.server.logging.ProfiledLock testSubject) {
        EasyMock.expect(ticker.read()).andReturn(java.util.concurrent.TimeUnit.MILLISECONDS.toNanos(0L));
        EasyMock.expect(ticker.read()).andReturn(java.util.concurrent.TimeUnit.MILLISECONDS.toNanos(0L));
        EasyMock.expect(ticker.read()).andReturn(java.util.concurrent.TimeUnit.MILLISECONDS.toNanos(6L));
        EasyMock.expect(ticker.read()).andReturn(java.util.concurrent.TimeUnit.MILLISECONDS.toNanos(13L));
        replayAll();
        testSubject.lock();
        testSubject.unlock();
        org.junit.Assert.assertEquals(java.util.Collections.singletonMap(java.lang.Thread.currentThread().getName(), 13L - 6L), testSubject.getTimeSpentLocked());
        verifyAll();
    }

    @org.junit.Test
    public void onlyOutermostLockUnlockIsProfiledForReadLock() {
        com.google.common.base.Ticker ticker = createMock(com.google.common.base.Ticker.class);
        org.apache.ambari.server.logging.ProfiledLock testSubject = new org.apache.ambari.server.logging.ProfiledReentrantReadWriteLock(new java.util.concurrent.locks.ReentrantReadWriteLock(), ticker, org.apache.ambari.server.logging.ProfiledReentrantReadWriteLockTest.LABEL).readLock();
        onlyOutermostLockUnlockIsProfiled(testSubject, ticker);
    }

    @org.junit.Test
    public void onlyOutermostLockUnlockIsProfiledForWriteLock() {
        com.google.common.base.Ticker ticker = createMock(com.google.common.base.Ticker.class);
        org.apache.ambari.server.logging.ProfiledLock testSubject = new org.apache.ambari.server.logging.ProfiledReentrantReadWriteLock(new java.util.concurrent.locks.ReentrantReadWriteLock(), ticker, org.apache.ambari.server.logging.ProfiledReentrantReadWriteLockTest.LABEL).readLock();
        onlyOutermostLockUnlockIsProfiled(testSubject, ticker);
    }

    @org.junit.Test
    public void onlyOutermostLockUnlockIsProfiled() {
        com.google.common.base.Ticker ticker = createMock(com.google.common.base.Ticker.class);
        org.apache.ambari.server.logging.ProfiledLock testSubject = new org.apache.ambari.server.logging.ProfiledReentrantLock(new java.util.concurrent.locks.ReentrantLock(), ticker, org.apache.ambari.server.logging.ProfiledReentrantReadWriteLockTest.LABEL);
        onlyOutermostLockUnlockIsProfiled(testSubject, ticker);
    }

    private void onlyOutermostLockUnlockIsProfiled(org.apache.ambari.server.logging.ProfiledLock testSubject, com.google.common.base.Ticker ticker) {
        EasyMock.expect(ticker.read()).andReturn(java.util.concurrent.TimeUnit.MILLISECONDS.toNanos(0L));
        EasyMock.expect(ticker.read()).andReturn(java.util.concurrent.TimeUnit.MILLISECONDS.toNanos(0L));
        EasyMock.expect(ticker.read()).andReturn(java.util.concurrent.TimeUnit.MILLISECONDS.toNanos(5L));
        EasyMock.expect(ticker.read()).andReturn(java.util.concurrent.TimeUnit.MILLISECONDS.toNanos(19L));
        replayAll();
        testSubject.lock();
        testSubject.lock();
        testSubject.unlock();
        testSubject.unlock();
        org.junit.Assert.assertEquals(java.util.Collections.singletonMap(java.lang.Thread.currentThread().getName(), 19L - 5L), testSubject.getTimeSpentLocked());
        org.junit.Assert.assertEquals(java.util.Collections.singletonMap(java.lang.Thread.currentThread().getName(), 1), testSubject.getLockCount());
        verifyAll();
    }
}