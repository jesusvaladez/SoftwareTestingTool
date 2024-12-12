package org.apache.ambari.server.utils;
public class ScheduledExecutorCompletionService<V> extends java.util.concurrent.ExecutorCompletionService<V> {
    private final java.util.concurrent.ScheduledExecutorService scheduledExecutor;

    private final java.util.concurrent.BlockingQueue<java.util.concurrent.Future<V>> queue;

    private class QueueingFuture extends java.util.concurrent.FutureTask<java.lang.Void> {
        QueueingFuture(java.util.concurrent.RunnableFuture<V> task) {
            super(task, null);
            this.task = task;
        }

        @java.lang.Override
        protected void done() {
            queue.add(task);
        }

        private final java.util.concurrent.Future<V> task;
    }

    public ScheduledExecutorCompletionService(java.util.concurrent.ScheduledExecutorService scheduledExecutor, java.util.concurrent.BlockingQueue<java.util.concurrent.Future<V>> queue) {
        super(scheduledExecutor, queue);
        this.scheduledExecutor = scheduledExecutor;
        this.queue = queue;
    }

    public java.util.concurrent.Future<V> schedule(java.util.concurrent.Callable<V> task, long delay, java.util.concurrent.TimeUnit unit) {
        if (task == null)
            throw new java.lang.NullPointerException();

        java.util.concurrent.RunnableFuture<V> f = newTaskFor(task);
        scheduledExecutor.schedule(new QueueingFuture(f), delay, unit);
        return f;
    }

    private java.util.concurrent.RunnableFuture<V> newTaskFor(java.util.concurrent.Callable<V> task) {
        return new java.util.concurrent.FutureTask<V>(task);
    }
}