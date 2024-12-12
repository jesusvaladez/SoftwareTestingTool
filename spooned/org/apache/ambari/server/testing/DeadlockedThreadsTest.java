package org.apache.ambari.server.testing;
public class DeadlockedThreadsTest {
    static java.util.Set<java.lang.Thread> threads = new java.util.HashSet<>();

    public void testDeadlocks() {
        java.lang.Object lock1 = new java.lang.String("lock1");
        java.lang.Object lock2 = new java.lang.String("lock2");
        java.lang.Object lock3 = new java.lang.String("lock3");
        org.apache.ambari.server.testing.DeadlockedThreadsTest.threads.add(new org.apache.ambari.server.testing.DeadlockedThreadsTest.DeadlockingThread("t1", lock1, lock2));
        org.apache.ambari.server.testing.DeadlockedThreadsTest.threads.add(new org.apache.ambari.server.testing.DeadlockedThreadsTest.DeadlockingThread("t2", lock2, lock3));
        org.apache.ambari.server.testing.DeadlockedThreadsTest.threads.add(new org.apache.ambari.server.testing.DeadlockedThreadsTest.DeadlockingThread("t3", lock3, lock1));
        java.lang.Object lock4 = new java.lang.String("lock4");
        java.lang.Object lock5 = new java.lang.String("lock5");
        org.apache.ambari.server.testing.DeadlockedThreadsTest.threads.add(new org.apache.ambari.server.testing.DeadlockedThreadsTest.DeadlockingThread("t4", lock4, lock5));
        org.apache.ambari.server.testing.DeadlockedThreadsTest.threads.add(new org.apache.ambari.server.testing.DeadlockedThreadsTest.DeadlockingThread("t5", lock5, lock4));
        org.apache.ambari.server.testing.DeadlockWarningThread wt = new org.apache.ambari.server.testing.DeadlockWarningThread(org.apache.ambari.server.testing.DeadlockedThreadsTest.threads);
        while (true) {
            if (!wt.isAlive()) {
                break;
            }
        } 
        if (wt.isDeadlocked()) {
            org.junit.Assert.assertTrue(wt.getErrorMessages().toString(), wt.isDeadlocked());
            org.junit.Assert.assertFalse(wt.getErrorMessages().toString().equals(""));
        } else {
            org.junit.Assert.assertTrue(wt.getErrorMessages().toString(), wt.isDeadlocked());
        }
    }

    public void testReadWriteDeadlocks() {
        java.lang.Object lock1 = new java.lang.String("lock1");
        java.lang.Object lock2 = new java.lang.String("lock2");
        java.lang.Object lock3 = new java.lang.String("lock3");
        org.apache.ambari.server.testing.DeadlockedThreadsTest.threads.add(new org.apache.ambari.server.testing.DeadlockedThreadsTest.DeadlockingThreadReadWriteLock("t1", lock1, lock2));
        org.apache.ambari.server.testing.DeadlockedThreadsTest.threads.add(new org.apache.ambari.server.testing.DeadlockedThreadsTest.DeadlockingThreadReadWriteLock("t2", lock2, lock3));
        org.apache.ambari.server.testing.DeadlockedThreadsTest.threads.add(new org.apache.ambari.server.testing.DeadlockedThreadsTest.DeadlockingThreadReadWriteLock("t3", lock3, lock1));
        java.lang.Object lock4 = new java.lang.String("lock4");
        java.lang.Object lock5 = new java.lang.String("lock5");
        org.apache.ambari.server.testing.DeadlockedThreadsTest.threads.add(new org.apache.ambari.server.testing.DeadlockedThreadsTest.DeadlockingThreadReadWriteLock("t4", lock4, lock5));
        org.apache.ambari.server.testing.DeadlockedThreadsTest.threads.add(new org.apache.ambari.server.testing.DeadlockedThreadsTest.DeadlockingThreadReadWriteLock("t5", lock5, lock4));
        org.apache.ambari.server.testing.DeadlockWarningThread wt = new org.apache.ambari.server.testing.DeadlockWarningThread(org.apache.ambari.server.testing.DeadlockedThreadsTest.threads);
        while (true) {
            if (!wt.isAlive()) {
                break;
            }
        } 
        if (wt.isDeadlocked()) {
            org.junit.Assert.assertTrue(wt.getErrorMessages().toString(), wt.isDeadlocked());
            org.junit.Assert.assertFalse(wt.getErrorMessages().toString().equals(""));
        } else {
            org.junit.Assert.assertTrue(wt.getErrorMessages().toString(), wt.isDeadlocked());
        }
    }

    private static class DeadlockingThread extends java.lang.Thread {
        private final java.lang.Object lock1;

        private final java.lang.Object lock2;

        public DeadlockingThread(java.lang.String name, java.lang.Object lock1, java.lang.Object lock2) {
            super(name);
            this.lock1 = lock1;
            this.lock2 = lock2;
            start();
        }

        public void run() {
            while (true) {
                f();
            } 
        }

        private void f() {
            synchronized(lock1) {
                g();
            }
        }

        private void g() {
            synchronized(lock2) {
                for (int i = 0; i < (1000 * 1000); i++);
            }
        }
    }

    private static class DeadlockingThreadReadWriteLock extends java.lang.Thread {
        private final java.lang.Object lock1;

        private final java.lang.Object lock2;

        private final java.util.concurrent.locks.ReentrantReadWriteLock rwl = new java.util.concurrent.locks.ReentrantReadWriteLock();

        public final java.util.concurrent.locks.Lock r = rwl.readLock();

        public final java.util.concurrent.locks.Lock w = rwl.writeLock();

        public DeadlockingThreadReadWriteLock(java.lang.String name, java.lang.Object lock1, java.lang.Object lock2) {
            super(name);
            this.lock1 = lock1;
            this.lock2 = lock2;
            start();
        }

        public void run() {
            while (true) {
                f();
            } 
        }

        private void f() {
            w.lock();
            g();
            w.unlock();
        }

        private void g() {
            r.lock();
            for (int i = 0; i < (1000 * 1000); i++);
            r.unlock();
        }
    }
}