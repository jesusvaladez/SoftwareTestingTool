package org.apache.ambari.server.utils;
@com.google.inject.Singleton
public class ThreadPools {
    public interface ThreadPoolFutureResult<T> {
        java.lang.Boolean waitForNextTask(T taskResult);
    }

    private static final java.lang.String AGENT_COMMAND_PUBLISHER_POOL_NAME = "agent-command-publisher";

    private static final java.lang.String DEFAULT_FORK_JOIN_POOL_NAME = "default-fork-join-pool";

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.utils.ThreadPools.class);

    private final org.apache.ambari.server.configuration.Configuration configuration;

    private java.util.concurrent.ForkJoinPool agentPublisherCommandsPool;

    private java.util.concurrent.ForkJoinPool defaultForkJoinPool;

    @com.google.inject.Inject
    public ThreadPools(org.apache.ambari.server.configuration.Configuration configuration) {
        this.configuration = configuration;
    }

    private void logThreadPoolCreation(java.lang.String name, int size) {
        org.apache.ambari.server.utils.ThreadPools.LOG.info(java.lang.String.format("Creating '%s' thread pool with configured size %d", name, size));
    }

    private java.util.concurrent.ForkJoinPool.ForkJoinWorkerThreadFactory createNamedFactory(java.lang.String name) {
        return pool -> {
            java.util.concurrent.ForkJoinWorkerThread worker = java.util.concurrent.ForkJoinPool.defaultForkJoinWorkerThreadFactory.newThread(pool);
            worker.setName((name + "-") + worker.getPoolIndex());
            return worker;
        };
    }

    private java.lang.Boolean forkJoinPoolShutdown(java.util.concurrent.ForkJoinPool pool, boolean forced) {
        if (pool == null) {
            return true;
        }
        if (forced) {
            pool.shutdownNow();
        } else {
            pool.shutdown();
        }
        return pool.isShutdown();
    }

    public java.util.concurrent.ForkJoinPool getAgentPublisherCommandsPool() {
        if (agentPublisherCommandsPool == null) {
            logThreadPoolCreation(org.apache.ambari.server.utils.ThreadPools.AGENT_COMMAND_PUBLISHER_POOL_NAME, configuration.getAgentCommandPublisherThreadPoolSize());
            agentPublisherCommandsPool = new java.util.concurrent.ForkJoinPool(configuration.getAgentCommandPublisherThreadPoolSize(), createNamedFactory(org.apache.ambari.server.utils.ThreadPools.AGENT_COMMAND_PUBLISHER_POOL_NAME), (t, e) -> {
                org.apache.ambari.server.utils.ThreadPools.LOG.error("Unexpected exception in thread: " + t, e);
                throw new java.lang.RuntimeException(e);
            }, false);
        }
        return agentPublisherCommandsPool;
    }

    public java.util.concurrent.ForkJoinPool getDefaultForkJoinPool() {
        if (defaultForkJoinPool == null) {
            logThreadPoolCreation(org.apache.ambari.server.utils.ThreadPools.DEFAULT_FORK_JOIN_POOL_NAME, configuration.getDefaultForkJoinPoolSize());
            defaultForkJoinPool = new java.util.concurrent.ForkJoinPool(configuration.getDefaultForkJoinPoolSize(), createNamedFactory(org.apache.ambari.server.utils.ThreadPools.DEFAULT_FORK_JOIN_POOL_NAME), (t, e) -> {
                org.apache.ambari.server.utils.ThreadPools.LOG.error("Unexpected exception in thread: " + t, e);
                throw new java.lang.RuntimeException(e);
            }, false);
        }
        return defaultForkJoinPool;
    }

    public void shutdownDefaultForkJoinPool(boolean force) {
        if (forkJoinPoolShutdown(defaultForkJoinPool, force)) {
            defaultForkJoinPool = null;
        }
    }

    public void shutdownAgentPublisherCommandsPool(boolean force) {
        if (forkJoinPoolShutdown(agentPublisherCommandsPool, force)) {
            agentPublisherCommandsPool = null;
        }
    }

    public <T> void parallelOperation(java.lang.String factoryName, int threadPoolSize, java.lang.String operation, java.util.List<java.util.concurrent.Callable<T>> tasks, org.apache.ambari.server.utils.ThreadPools.ThreadPoolFutureResult<T> taskResultFunc) throws java.lang.Exception {
        logThreadPoolCreation(factoryName, threadPoolSize);
        java.util.concurrent.ThreadFactory threadFactory = new com.google.common.util.concurrent.ThreadFactoryBuilder().setNameFormat(factoryName).build();
        java.util.concurrent.ExecutorService executorService = java.util.concurrent.Executors.newFixedThreadPool(threadPoolSize, threadFactory);
        java.util.concurrent.CompletionService<T> completionService = new java.util.concurrent.ExecutorCompletionService<>(executorService);
        java.util.List<java.util.concurrent.Future<T>> futures = tasks.stream().map(completionService::submit).collect(java.util.stream.Collectors.toList());
        org.apache.ambari.server.utils.ThreadPools.LOG.info("Processing {} {} concurrently...", futures.size(), operation);
        T t;
        try {
            for (int i = 0; i < futures.size(); i++) {
                java.util.concurrent.Future<T> future = completionService.take();
                t = future.get();
                if (!taskResultFunc.waitForNextTask(t)) {
                    break;
                }
            }
        } finally {
            futures.stream().filter(x -> (!x.isCancelled()) && (!x.isDone())).forEach(x -> x.cancel(true));
            executorService.shutdown();
        }
    }

    public static java.util.concurrent.ExecutorService getSingleThreadedExecutor(java.lang.String threadPoolName) {
        return java.util.concurrent.Executors.newSingleThreadExecutor(new com.google.common.util.concurrent.ThreadFactoryBuilder().setNameFormat(threadPoolName + "-%d").build());
    }

    @java.lang.Override
    protected void finalize() throws java.lang.Throwable {
        shutdownAgentPublisherCommandsPool(true);
        shutdownDefaultForkJoinPool(true);
        super.finalize();
    }
}