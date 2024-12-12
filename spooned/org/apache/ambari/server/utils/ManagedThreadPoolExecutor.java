package org.apache.ambari.server.utils;
public class ManagedThreadPoolExecutor extends java.util.concurrent.ThreadPoolExecutor {
    private volatile boolean isStopped;

    private final java.util.concurrent.locks.ReentrantLock pauseLock = new java.util.concurrent.locks.ReentrantLock();

    private final java.util.concurrent.locks.Condition unpaused = pauseLock.newCondition();

    public ManagedThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, java.util.concurrent.TimeUnit unit, java.util.concurrent.BlockingQueue<java.lang.Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, java.util.concurrent.Executors.defaultThreadFactory());
        isStopped = true;
    }

    @java.lang.Override
    protected void beforeExecute(java.lang.Thread t, java.lang.Runnable r) {
        super.beforeExecute(t, r);
        pauseLock.lock();
        try {
            while (isStopped) {
                unpaused.await();
            } 
        } catch (java.lang.InterruptedException ie) {
            t.interrupt();
        } finally {
            pauseLock.unlock();
        }
    }

    public void start() {
        pauseLock.lock();
        try {
            isStopped = false;
            unpaused.signalAll();
        } finally {
            pauseLock.unlock();
        }
    }

    public void stop() {
        pauseLock.lock();
        try {
            isStopped = true;
        } finally {
            pauseLock.unlock();
        }
    }

    public boolean isRunning() {
        return !isStopped;
    }
}