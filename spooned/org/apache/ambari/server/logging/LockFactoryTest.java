package org.apache.ambari.server.logging;
import org.easymock.EasyMockSupport;
import static org.easymock.EasyMock.expect;
public class LockFactoryTest extends org.easymock.EasyMockSupport {
    @org.junit.Test
    public void createsRegularLockIfDebugIsDisabled() {
        org.apache.ambari.server.configuration.Configuration config = createNiceMock(org.apache.ambari.server.configuration.Configuration.class);
        EasyMock.expect(config.isServerLocksProfilingEnabled()).andReturn(false);
        replayAll();
        org.apache.ambari.server.logging.LockFactory factory = new org.apache.ambari.server.logging.LockFactory(config);
        java.util.concurrent.locks.Lock lock = factory.newLock();
        org.junit.Assert.assertTrue(lock instanceof java.util.concurrent.locks.ReentrantLock);
        verifyAll();
    }

    @org.junit.Test
    public void createsRegularReadWriteLockIfDebugIsDisabled() {
        org.apache.ambari.server.configuration.Configuration config = createNiceMock(org.apache.ambari.server.configuration.Configuration.class);
        EasyMock.expect(config.isServerLocksProfilingEnabled()).andReturn(false);
        replayAll();
        org.apache.ambari.server.logging.LockFactory factory = new org.apache.ambari.server.logging.LockFactory(config);
        java.util.concurrent.locks.ReadWriteLock lock = factory.newReadWriteLock();
        org.junit.Assert.assertTrue(lock instanceof java.util.concurrent.locks.ReentrantReadWriteLock);
        verifyAll();
    }

    @org.junit.Test
    public void createsProfiledLockIfProfilingIsEnabled() {
        org.apache.ambari.server.configuration.Configuration config = createNiceMock(org.apache.ambari.server.configuration.Configuration.class);
        EasyMock.expect(config.isServerLocksProfilingEnabled()).andReturn(true);
        replayAll();
        org.apache.ambari.server.logging.LockFactory factory = new org.apache.ambari.server.logging.LockFactory(config);
        java.util.concurrent.locks.Lock lock = factory.newLock();
        org.junit.Assert.assertTrue(lock instanceof org.apache.ambari.server.logging.ProfiledReentrantLock);
        java.lang.String label = ((org.apache.ambari.server.logging.ProfiledLock) (lock)).getLabel();
        org.junit.Assert.assertTrue(label, label.contains("LockFactoryTest.java"));
        verifyAll();
    }

    @org.junit.Test
    public void createsProfiledReadWriteLockIfProfilingIsEnabled() {
        org.apache.ambari.server.configuration.Configuration config = createNiceMock(org.apache.ambari.server.configuration.Configuration.class);
        EasyMock.expect(config.isServerLocksProfilingEnabled()).andReturn(true);
        replayAll();
        org.apache.ambari.server.logging.LockFactory factory = new org.apache.ambari.server.logging.LockFactory(config);
        java.util.concurrent.locks.ReadWriteLock lock = factory.newReadWriteLock();
        org.junit.Assert.assertTrue(lock instanceof org.apache.ambari.server.logging.ProfiledReentrantReadWriteLock);
        java.lang.String readLockLabel = ((org.apache.ambari.server.logging.ProfiledReentrantReadWriteLock) (lock)).readLock().getLabel();
        org.junit.Assert.assertTrue(readLockLabel, readLockLabel.contains("LockFactoryTest.java"));
        java.lang.String writeLockLabel = ((org.apache.ambari.server.logging.ProfiledReentrantReadWriteLock) (lock)).writeLock().getLabel();
        org.junit.Assert.assertTrue(writeLockLabel, writeLockLabel.contains("LockFactoryTest.java"));
        verifyAll();
    }
}