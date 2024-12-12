package org.apache.ambari.server.controller.utilities;
public class BufferedThreadPoolExecutorCompletionService<V> extends java.util.concurrent.ExecutorCompletionService<V> {
    private java.util.concurrent.ThreadPoolExecutor executor;

    private java.util.Queue<java.lang.Runnable> overflowQueue;

    public BufferedThreadPoolExecutorCompletionService(java.util.concurrent.ThreadPoolExecutor executor) {
        super(executor);
        this.executor = executor;
        this.overflowQueue = new java.util.concurrent.LinkedBlockingQueue<>();
        this.executor.setRejectedExecutionHandler(new java.util.concurrent.RejectedExecutionHandler() {
            @java.lang.Override
            public void rejectedExecution(java.lang.Runnable r, java.util.concurrent.ThreadPoolExecutor executor) {
                overflowQueue.add(r);
            }
        });
    }

    @java.lang.Override
    public java.util.concurrent.Future<V> take() throws java.lang.InterruptedException {
        java.util.concurrent.Future<V> take = super.take();
        if (((!executor.isTerminating()) && (!overflowQueue.isEmpty())) && (executor.getActiveCount() < executor.getMaximumPoolSize())) {
            java.lang.Runnable overflow = overflowQueue.poll();
            if (overflow != null) {
                executor.execute(overflow);
            }
        }
        return take;
    }

    @java.lang.Override
    public java.util.concurrent.Future<V> poll() {
        java.util.concurrent.Future<V> poll = super.poll();
        if (((!executor.isTerminating()) && (!overflowQueue.isEmpty())) && (executor.getActiveCount() < executor.getMaximumPoolSize())) {
            java.lang.Runnable overflow = overflowQueue.poll();
            if (overflow != null) {
                executor.execute(overflow);
            }
        }
        return poll;
    }

    @java.lang.Override
    public java.util.concurrent.Future<V> poll(long timeout, java.util.concurrent.TimeUnit unit) throws java.lang.InterruptedException {
        java.util.concurrent.Future<V> poll = super.poll();
        if (null != poll) {
            if (((!executor.isTerminating()) && (!overflowQueue.isEmpty())) && (executor.getActiveCount() < executor.getMaximumPoolSize())) {
                java.lang.Runnable overflow = overflowQueue.poll();
                if (overflow != null) {
                    executor.execute(overflow);
                }
            }
            return poll;
        }
        if (executor.getActiveCount() == 0) {
            if ((!executor.isTerminating()) && (!overflowQueue.isEmpty())) {
                java.lang.Runnable overflow = overflowQueue.poll();
                if (overflow != null) {
                    executor.execute(overflow);
                }
            }
        }
        poll = super.poll(timeout, unit);
        return poll;
    }
}