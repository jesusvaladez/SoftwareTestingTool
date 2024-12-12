package org.apache.ambari.annotations;
public class LockAreaTest {
    private com.google.inject.Injector m_injector;

    @org.junit.Before
    public void setup() throws java.lang.Exception {
        m_injector = com.google.inject.Guice.createInjector(new org.apache.ambari.server.orm.InMemoryDefaultTestModule());
        m_injector.getInstance(org.apache.ambari.server.orm.GuiceJpaInitializer.class);
    }

    @org.junit.After
    public void teardown() throws org.apache.ambari.server.AmbariException, java.sql.SQLException {
        org.apache.ambari.server.H2DatabaseCleaner.clearDatabaseAndStopPersistenceService(m_injector);
    }

    @org.junit.Test
    public void testTransactionalLockInstantiation() {
        org.apache.ambari.server.orm.TransactionalLocks locks = m_injector.getInstance(org.apache.ambari.server.orm.TransactionalLocks.class);
        java.util.List<java.util.concurrent.locks.ReadWriteLock> lockList = new java.util.ArrayList<>();
        java.util.Set<org.apache.ambari.annotations.TransactionalLock.LockArea> lockAreas = java.util.EnumSet.allOf(org.apache.ambari.annotations.TransactionalLock.LockArea.class);
        for (org.apache.ambari.annotations.TransactionalLock.LockArea lockArea : lockAreas) {
            java.util.concurrent.locks.ReadWriteLock lock = locks.getLock(lockArea);
            junit.framework.Assert.assertNotNull(lock);
            lockList.add(lock);
        }
        for (org.apache.ambari.annotations.TransactionalLock.LockArea lockArea : lockAreas) {
            junit.framework.Assert.assertTrue(lockList.contains(locks.getLock(lockArea)));
        }
    }
}