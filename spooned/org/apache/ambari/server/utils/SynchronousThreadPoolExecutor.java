package org.apache.ambari.server.utils;
public class SynchronousThreadPoolExecutor extends java.util.concurrent.ThreadPoolExecutor {
    public SynchronousThreadPoolExecutor() {
        super(1, 1, 0L, java.util.concurrent.TimeUnit.MINUTES, new java.util.concurrent.LinkedBlockingQueue<>());
    }

    @java.lang.Override
    public void shutdown() {
    }

    @java.lang.Override
    public java.util.List<java.lang.Runnable> shutdownNow() {
        return null;
    }

    @java.lang.Override
    public boolean isShutdown() {
        return false;
    }

    @java.lang.Override
    public boolean isTerminated() {
        return false;
    }

    @java.lang.Override
    public boolean awaitTermination(long timeout, java.util.concurrent.TimeUnit unit) throws java.lang.InterruptedException {
        return false;
    }

    @java.lang.Override
    public void execute(java.lang.Runnable command) {
        command.run();
    }
}