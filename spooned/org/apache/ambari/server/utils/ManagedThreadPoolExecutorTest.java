package org.apache.ambari.server.utils;
import org.awaitility.Awaitility;
public class ManagedThreadPoolExecutorTest {
    @org.junit.Test
    public void isStoppedAfterCreation() {
        org.apache.ambari.server.utils.ManagedThreadPoolExecutor executor = org.apache.ambari.server.utils.ManagedThreadPoolExecutorTest.createExecutor(1);
        executor.execute(org.junit.Assert::fail);
        org.junit.Assert.assertFalse(executor.isRunning());
    }

    @org.junit.Test
    public void canBeStartedAndStopped() {
        org.apache.ambari.server.utils.ManagedThreadPoolExecutor executor = org.apache.ambari.server.utils.ManagedThreadPoolExecutorTest.createExecutor(1);
        executor.submit(() -> java.lang.Boolean.TRUE);
        executor.start();
        org.junit.Assert.assertTrue(executor.isRunning());
        executor.stop();
        org.junit.Assert.assertFalse(executor.isRunning());
    }

    @org.junit.Test
    public void retainsTasksUntilStarted() {
        final int taskCount = 60;
        final java.util.concurrent.atomic.AtomicInteger counter = new java.util.concurrent.atomic.AtomicInteger();
        org.apache.ambari.server.utils.ManagedThreadPoolExecutor executor = org.apache.ambari.server.utils.ManagedThreadPoolExecutorTest.createExecutor(10);
        for (int i = 0; i < taskCount; ++i) {
            executor.execute(counter::incrementAndGet);
        }
        executor.start();
        org.awaitility.Awaitility.await().atMost(2, java.util.concurrent.TimeUnit.SECONDS).until(() -> counter.get() == taskCount);
    }

    private static org.apache.ambari.server.utils.ManagedThreadPoolExecutor createExecutor(int poolSize) {
        return new org.apache.ambari.server.utils.ManagedThreadPoolExecutor(poolSize, poolSize, 0L, java.util.concurrent.TimeUnit.MILLISECONDS, new java.util.concurrent.LinkedBlockingQueue<>());
    }
}