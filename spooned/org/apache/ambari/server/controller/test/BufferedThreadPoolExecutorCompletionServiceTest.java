package org.apache.ambari.server.controller.test;
public class BufferedThreadPoolExecutorCompletionServiceTest {
    private void longOp() throws java.lang.InterruptedException {
        java.lang.Thread.sleep(700);
        java.lang.System.out.println("Completed " + java.lang.Thread.currentThread());
    }

    @org.junit.Test
    public void testOnlyCorePoolThreadsLaunchedForUnboundedQueue() throws java.lang.InterruptedException {
        int CORE_POOL_SIZE = 2;
        int MAX_POOL_SIZE = 5;
        int TASKS_COUNT = 8;
        java.util.concurrent.LinkedBlockingQueue<java.lang.Runnable> queue = new java.util.concurrent.LinkedBlockingQueue<>();
        java.util.concurrent.ThreadPoolExecutor threadPoolExecutor = new java.util.concurrent.ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE, 30000, java.util.concurrent.TimeUnit.MILLISECONDS, queue);
        org.apache.ambari.server.controller.utilities.BufferedThreadPoolExecutorCompletionService<java.lang.Runnable> service = new org.apache.ambari.server.controller.utilities.BufferedThreadPoolExecutorCompletionService<>(threadPoolExecutor);
        for (int tc = 0; tc < TASKS_COUNT; tc++) {
            service.submit(new java.lang.Runnable() {
                @java.lang.Override
                public void run() {
                    try {
                        longOp();
                    } catch (java.lang.InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }, null);
        }
        java.lang.Thread.sleep(500);
        junit.framework.Assert.assertEquals(CORE_POOL_SIZE, threadPoolExecutor.getActiveCount());
        for (int tc = 0; tc < TASKS_COUNT; tc++) {
            java.util.concurrent.Future<java.lang.Runnable> take = service.take();
            junit.framework.Assert.assertTrue(take.isDone());
            junit.framework.Assert.assertTrue("No more than CORE_POOL_SIZE threads should be launched", threadPoolExecutor.getActiveCount() <= CORE_POOL_SIZE);
        }
        threadPoolExecutor.shutdown();
    }

    @org.junit.Test
    public void testLessThanMaxPoolSizeThreadsLaunched() throws java.lang.InterruptedException {
        int CORE_POOL_SIZE = 2;
        int MAX_POOL_SIZE = 10;
        int TASKS_COUNT = 8;
        java.util.concurrent.LinkedBlockingQueue<java.lang.Runnable> queue = new java.util.concurrent.LinkedBlockingQueue<>(CORE_POOL_SIZE);
        java.util.concurrent.ThreadPoolExecutor threadPoolExecutor = new java.util.concurrent.ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE, 30000, java.util.concurrent.TimeUnit.MILLISECONDS, queue);
        org.apache.ambari.server.controller.utilities.BufferedThreadPoolExecutorCompletionService<java.lang.Runnable> service = new org.apache.ambari.server.controller.utilities.BufferedThreadPoolExecutorCompletionService<>(threadPoolExecutor);
        for (int tc = 0; tc < TASKS_COUNT; tc++) {
            service.submit(new java.lang.Runnable() {
                @java.lang.Override
                public void run() {
                    try {
                        longOp();
                    } catch (java.lang.InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }, null);
        }
        java.lang.Thread.sleep(500);
        junit.framework.Assert.assertEquals(TASKS_COUNT - CORE_POOL_SIZE, threadPoolExecutor.getActiveCount());
        for (int tc = 0; tc < TASKS_COUNT; tc++) {
            java.util.concurrent.Future<java.lang.Runnable> take = service.take();
            junit.framework.Assert.assertTrue(take.isDone());
            junit.framework.Assert.assertTrue("No more than TASKS_COUNT threads should be launched", threadPoolExecutor.getActiveCount() <= TASKS_COUNT);
        }
        threadPoolExecutor.shutdown();
    }

    @org.junit.Test
    public void testMaxPoolSizeThreadsLaunched() throws java.lang.InterruptedException {
        int CORE_POOL_SIZE = 2;
        int MAX_POOL_SIZE = 10;
        int TASKS_COUNT = 24;
        java.util.concurrent.LinkedBlockingQueue<java.lang.Runnable> queue = new java.util.concurrent.LinkedBlockingQueue<>(CORE_POOL_SIZE);
        java.util.concurrent.ThreadPoolExecutor threadPoolExecutor = new java.util.concurrent.ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE, 30000, java.util.concurrent.TimeUnit.MILLISECONDS, queue);
        org.apache.ambari.server.controller.utilities.BufferedThreadPoolExecutorCompletionService<java.lang.Runnable> service = new org.apache.ambari.server.controller.utilities.BufferedThreadPoolExecutorCompletionService<>(threadPoolExecutor);
        for (int tc = 0; tc < TASKS_COUNT; tc++) {
            service.submit(new java.lang.Runnable() {
                @java.lang.Override
                public void run() {
                    try {
                        longOp();
                    } catch (java.lang.InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }, null);
        }
        java.lang.Thread.sleep(500);
        junit.framework.Assert.assertEquals(MAX_POOL_SIZE, threadPoolExecutor.getActiveCount());
        for (int tc = 0; tc < TASKS_COUNT; tc++) {
            java.util.concurrent.Future<java.lang.Runnable> take = service.take();
            junit.framework.Assert.assertTrue(take.isDone());
            junit.framework.Assert.assertTrue("No more than MAX_POOL_SIZE threads should be launched", threadPoolExecutor.getActiveCount() <= MAX_POOL_SIZE);
        }
        threadPoolExecutor.shutdown();
    }

    @org.junit.Test
    public void testScalingThreadPoolExecutor() throws java.lang.InterruptedException {
        int CORE_POOL_SIZE = 2;
        int MAX_POOL_SIZE = 10;
        int TASKS_COUNT = 24;
        java.util.concurrent.ThreadPoolExecutor threadPoolExecutor = new org.apache.ambari.server.controller.utilities.ScalingThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE, 30000, java.util.concurrent.TimeUnit.MILLISECONDS, CORE_POOL_SIZE);
        org.apache.ambari.server.controller.utilities.BufferedThreadPoolExecutorCompletionService<java.lang.Runnable> service = new org.apache.ambari.server.controller.utilities.BufferedThreadPoolExecutorCompletionService<>(threadPoolExecutor);
        for (int tc = 0; tc < TASKS_COUNT; tc++) {
            service.submit(new java.lang.Runnable() {
                @java.lang.Override
                public void run() {
                    try {
                        longOp();
                    } catch (java.lang.InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }, null);
        }
        java.lang.Thread.sleep(500);
        junit.framework.Assert.assertEquals(MAX_POOL_SIZE, threadPoolExecutor.getActiveCount());
        for (int tc = 0; tc < TASKS_COUNT; tc++) {
            java.util.concurrent.Future<java.lang.Runnable> take = service.take();
            junit.framework.Assert.assertTrue(take.isDone());
            junit.framework.Assert.assertTrue("No more than MAX_POOL_SIZE threads should be launched", threadPoolExecutor.getActiveCount() <= MAX_POOL_SIZE);
        }
        threadPoolExecutor.shutdown();
    }
}