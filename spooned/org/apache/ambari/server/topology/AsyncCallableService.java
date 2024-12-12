package org.apache.ambari.server.topology;
public class AsyncCallableService<T> implements java.util.concurrent.Callable<T> {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.topology.AsyncCallableService.class);

    private final java.util.concurrent.ScheduledExecutorService executorService;

    private final java.util.concurrent.Callable<T> task;

    private final java.lang.String taskName;

    private final long timeout;

    private final long retryDelay;

    private final java.util.function.Consumer<java.lang.Throwable> onError;

    public AsyncCallableService(java.util.concurrent.Callable<T> task, long timeout, long retryDelay, java.lang.String taskName, java.util.function.Consumer<java.lang.Throwable> onError) {
        this(task, timeout, retryDelay, taskName, java.util.concurrent.Executors.newScheduledThreadPool(1), onError);
    }

    public AsyncCallableService(java.util.concurrent.Callable<T> task, long timeout, long retryDelay, java.lang.String taskName, java.util.concurrent.ScheduledExecutorService executorService, java.util.function.Consumer<java.lang.Throwable> onError) {
        com.google.common.base.Preconditions.checkArgument(retryDelay > 0, "retryDelay should be positive");
        this.task = task;
        this.executorService = executorService;
        this.timeout = timeout;
        this.retryDelay = retryDelay;
        this.taskName = taskName;
        this.onError = onError;
    }

    @java.lang.Override
    public T call() throws java.lang.Exception {
        long startTime = java.lang.System.currentTimeMillis();
        long timeLeft = timeout;
        java.util.concurrent.Future<T> future = executorService.submit(task);
        org.apache.ambari.server.topology.AsyncCallableService.LOG.info("Task {} execution started at {}", taskName, startTime);
        java.lang.Throwable lastError = null;
        while (true) {
            try {
                org.apache.ambari.server.topology.AsyncCallableService.LOG.debug("Task {} waiting for result at most {} ms", taskName, timeLeft);
                T taskResult = future.get(timeLeft, java.util.concurrent.TimeUnit.MILLISECONDS);
                org.apache.ambari.server.topology.AsyncCallableService.LOG.info("Task {} successfully completed with result: {}", taskName, taskResult);
                return taskResult;
            } catch (java.util.concurrent.TimeoutException e) {
                org.apache.ambari.server.topology.AsyncCallableService.LOG.debug("Task {} timeout", taskName);
                if (lastError == null) {
                    lastError = e;
                }
                timeLeft = 0;
            } catch (java.util.concurrent.ExecutionException e) {
                java.lang.Throwable cause = com.google.common.base.Throwables.getRootCause(e);
                if (!(cause instanceof org.apache.ambari.server.topology.AsyncCallableService.RetryTaskSilently)) {
                    org.apache.ambari.server.topology.AsyncCallableService.LOG.info(java.lang.String.format("Task %s exception during execution", taskName), cause);
                }
                lastError = cause;
                timeLeft = (timeout - (java.lang.System.currentTimeMillis() - startTime)) - retryDelay;
            }
            if (timeLeft <= 0) {
                attemptToCancel(future);
                org.apache.ambari.server.topology.AsyncCallableService.LOG.warn("Task {} timeout exceeded, no more retries", taskName);
                onError.accept(lastError);
                return null;
            }
            org.apache.ambari.server.topology.AsyncCallableService.LOG.debug("Task {} retrying execution in {} milliseconds", taskName, retryDelay);
            future = executorService.schedule(task, retryDelay, java.util.concurrent.TimeUnit.MILLISECONDS);
        } 
    }

    private void attemptToCancel(java.util.concurrent.Future<?> future) {
        org.apache.ambari.server.topology.AsyncCallableService.LOG.debug("Task {} timeout exceeded, cancelling", taskName);
        if ((!future.isDone()) && future.cancel(true)) {
            org.apache.ambari.server.topology.AsyncCallableService.LOG.debug("Task {} cancelled", taskName);
        } else {
            org.apache.ambari.server.topology.AsyncCallableService.LOG.debug("Task {} already done", taskName);
        }
    }

    public static class RetryTaskSilently extends java.lang.RuntimeException {
        public RetryTaskSilently() {
            super();
        }

        public RetryTaskSilently(java.lang.String message) {
            super(message);
        }
    }
}