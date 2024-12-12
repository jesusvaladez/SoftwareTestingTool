package org.apache.ambari.annotations;
import com.google.inject.persist.Transactional;
import org.easymock.EasyMock;
public class TransactionalLockInterceptorTest {
    private com.google.inject.Injector m_injector;

    @org.junit.Before
    public void setup() throws java.lang.Exception {
        m_injector = com.google.inject.Guice.createInjector(com.google.inject.util.Modules.override(new org.apache.ambari.server.orm.InMemoryDefaultTestModule()).with(new org.apache.ambari.annotations.TransactionalLockInterceptorTest.MockModule()));
        m_injector.getInstance(org.apache.ambari.server.orm.GuiceJpaInitializer.class);
    }

    @org.junit.After
    public void teardown() throws org.apache.ambari.server.AmbariException, java.sql.SQLException {
        org.apache.ambari.server.H2DatabaseCleaner.clearDatabaseAndStopPersistenceService(m_injector);
    }

    @org.junit.Test
    public void testTransactionalLockInvocation() throws java.lang.Throwable {
        org.apache.ambari.server.orm.TransactionalLocks transactionalLocks = m_injector.getInstance(org.apache.ambari.server.orm.TransactionalLocks.class);
        java.util.concurrent.locks.ReadWriteLock readWriteLock = org.easymock.EasyMock.createStrictMock(java.util.concurrent.locks.ReadWriteLock.class);
        java.util.concurrent.locks.Lock readLock = org.easymock.EasyMock.createStrictMock(java.util.concurrent.locks.Lock.class);
        java.util.concurrent.locks.Lock writeLock = org.easymock.EasyMock.createStrictMock(java.util.concurrent.locks.Lock.class);
        org.easymock.EasyMock.expect(transactionalLocks.getLock(org.apache.ambari.annotations.TransactionalLock.LockArea.HRC_STATUS_CACHE)).andReturn(readWriteLock).times(2);
        org.easymock.EasyMock.expect(readWriteLock.writeLock()).andReturn(writeLock).times(2);
        writeLock.lock();
        org.easymock.EasyMock.expectLastCall().once();
        writeLock.unlock();
        org.easymock.EasyMock.expectLastCall().once();
        org.easymock.EasyMock.replay(transactionalLocks, readWriteLock, readLock, writeLock);
        org.apache.ambari.server.orm.dao.HostRoleCommandDAO hostRoleCommandDAO = m_injector.getInstance(org.apache.ambari.server.orm.dao.HostRoleCommandDAO.class);
        hostRoleCommandDAO.mergeAll(new java.util.ArrayList<>());
        org.easymock.EasyMock.verify(transactionalLocks, readWriteLock, readLock, writeLock);
    }

    @org.junit.Test
    public void testNestedTransactional() throws java.lang.Throwable {
        org.apache.ambari.server.orm.TransactionalLocks transactionalLocks = m_injector.getInstance(org.apache.ambari.server.orm.TransactionalLocks.class);
        java.util.concurrent.locks.ReadWriteLock readWriteLock = org.easymock.EasyMock.createStrictMock(java.util.concurrent.locks.ReadWriteLock.class);
        java.util.concurrent.locks.Lock readLock = org.easymock.EasyMock.createStrictMock(java.util.concurrent.locks.Lock.class);
        java.util.concurrent.locks.Lock writeLock = org.easymock.EasyMock.createStrictMock(java.util.concurrent.locks.Lock.class);
        org.easymock.EasyMock.expect(transactionalLocks.getLock(org.apache.ambari.annotations.TransactionalLock.LockArea.HRC_STATUS_CACHE)).andReturn(readWriteLock).times(2);
        org.easymock.EasyMock.expect(readWriteLock.writeLock()).andReturn(writeLock).times(2);
        writeLock.lock();
        org.easymock.EasyMock.expectLastCall().once();
        writeLock.unlock();
        org.easymock.EasyMock.expectLastCall().once();
        org.easymock.EasyMock.replay(transactionalLocks, readWriteLock, readLock, writeLock);
        org.apache.ambari.annotations.TransactionalLockInterceptorTest.TestObject testObject = m_injector.getInstance(org.apache.ambari.annotations.TransactionalLockInterceptorTest.TestObject.class);
        testObject.testLockMethodAsChildOfActiveTransaction();
        org.easymock.EasyMock.verify(transactionalLocks, readWriteLock, readLock, writeLock);
    }

    @org.junit.Test
    public void testMultipleLocks() throws java.lang.Throwable {
        org.apache.ambari.server.orm.TransactionalLocks transactionalLocks = m_injector.getInstance(org.apache.ambari.server.orm.TransactionalLocks.class);
        java.util.concurrent.locks.ReadWriteLock readWriteLock = org.easymock.EasyMock.createStrictMock(java.util.concurrent.locks.ReadWriteLock.class);
        java.util.concurrent.locks.Lock readLock = org.easymock.EasyMock.createStrictMock(java.util.concurrent.locks.Lock.class);
        java.util.concurrent.locks.Lock writeLock = org.easymock.EasyMock.createStrictMock(java.util.concurrent.locks.Lock.class);
        org.easymock.EasyMock.expect(transactionalLocks.getLock(org.apache.ambari.annotations.TransactionalLock.LockArea.HRC_STATUS_CACHE)).andReturn(readWriteLock).times(2);
        org.easymock.EasyMock.expect(readWriteLock.writeLock()).andReturn(writeLock).times(2);
        writeLock.lock();
        org.easymock.EasyMock.expectLastCall().once();
        writeLock.unlock();
        org.easymock.EasyMock.expectLastCall().once();
        org.easymock.EasyMock.expect(transactionalLocks.getLock(org.apache.ambari.annotations.TransactionalLock.LockArea.HRC_STATUS_CACHE)).andReturn(readWriteLock).times(2);
        org.easymock.EasyMock.expect(readWriteLock.writeLock()).andReturn(writeLock).times(2);
        writeLock.lock();
        org.easymock.EasyMock.expectLastCall().once();
        writeLock.unlock();
        org.easymock.EasyMock.expectLastCall().once();
        org.easymock.EasyMock.replay(transactionalLocks, readWriteLock, readLock, writeLock);
        org.apache.ambari.annotations.TransactionalLockInterceptorTest.TestObject testObject = m_injector.getInstance(org.apache.ambari.annotations.TransactionalLockInterceptorTest.TestObject.class);
        testObject.testMultipleLocks();
        org.easymock.EasyMock.verify(transactionalLocks, readWriteLock, readLock, writeLock);
    }

    @org.junit.Test
    public void testNestedMultipleLocks() throws java.lang.Throwable {
        org.apache.ambari.server.orm.TransactionalLocks transactionalLocks = m_injector.getInstance(org.apache.ambari.server.orm.TransactionalLocks.class);
        java.util.concurrent.locks.ReadWriteLock readWriteLock = org.easymock.EasyMock.createStrictMock(java.util.concurrent.locks.ReadWriteLock.class);
        java.util.concurrent.locks.Lock readLock = org.easymock.EasyMock.createStrictMock(java.util.concurrent.locks.Lock.class);
        java.util.concurrent.locks.Lock writeLock = org.easymock.EasyMock.createStrictMock(java.util.concurrent.locks.Lock.class);
        org.easymock.EasyMock.expect(transactionalLocks.getLock(org.apache.ambari.annotations.TransactionalLock.LockArea.HRC_STATUS_CACHE)).andReturn(readWriteLock).times(2);
        org.easymock.EasyMock.expect(readWriteLock.writeLock()).andReturn(writeLock).times(2);
        writeLock.lock();
        org.easymock.EasyMock.expectLastCall().once();
        writeLock.unlock();
        org.easymock.EasyMock.expectLastCall().once();
        org.easymock.EasyMock.replay(transactionalLocks, readWriteLock, readLock, writeLock);
        org.apache.ambari.annotations.TransactionalLockInterceptorTest.TestObject testObject = m_injector.getInstance(org.apache.ambari.annotations.TransactionalLockInterceptorTest.TestObject.class);
        testObject.testMultipleNestedLocks();
        org.easymock.EasyMock.verify(transactionalLocks, readWriteLock, readLock, writeLock);
    }

    public static class TestObject {
        public void testLockMethodAsChildOfActiveTransaction() {
            transactionMethodCallingAnotherWithLock();
        }

        public void testMultipleLocks() {
            transactionMethodWithLock();
            transactionMethodWithLock();
        }

        public void testMultipleNestedLocks() {
            transactionMethodWithLockCallingAnotherWithLock();
        }

        @com.google.inject.persist.Transactional
        public void transactionMethodCallingAnotherWithLock() {
            transactionMethodWithLock();
        }

        @com.google.inject.persist.Transactional
        @org.apache.ambari.annotations.TransactionalLock(lockArea = org.apache.ambari.annotations.TransactionalLock.LockArea.HRC_STATUS_CACHE, lockType = org.apache.ambari.annotations.TransactionalLock.LockType.WRITE)
        public void transactionMethodWithLock() {
        }

        @com.google.inject.persist.Transactional
        @org.apache.ambari.annotations.TransactionalLock(lockArea = org.apache.ambari.annotations.TransactionalLock.LockArea.HRC_STATUS_CACHE, lockType = org.apache.ambari.annotations.TransactionalLock.LockType.WRITE)
        public void transactionMethodWithLockCallingAnotherWithLock() {
            transactionMethodWithLock();
        }
    }

    private class MockModule implements com.google.inject.Module {
        @java.lang.Override
        public void configure(com.google.inject.Binder binder) {
            binder.bind(org.apache.ambari.server.orm.TransactionalLocks.class).toInstance(org.easymock.EasyMock.createNiceMock(org.apache.ambari.server.orm.TransactionalLocks.class));
        }
    }
}