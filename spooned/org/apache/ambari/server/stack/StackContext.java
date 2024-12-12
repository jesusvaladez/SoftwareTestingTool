package org.apache.ambari.server.stack;
import org.apache.commons.collections.MapUtils;
public class StackContext {
    private org.apache.ambari.server.orm.dao.MetainfoDAO metaInfoDAO;

    private org.apache.ambari.server.metadata.ActionMetadata actionMetaData;

    private org.apache.ambari.server.stack.StackContext.LatestRepoQueryExecutor repoUpdateExecutor;

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.stack.StackContext.class);

    private static final int THREAD_COUNT = 10;

    public StackContext(org.apache.ambari.server.orm.dao.MetainfoDAO metaInfoDAO, org.apache.ambari.server.metadata.ActionMetadata actionMetaData, org.apache.ambari.server.state.stack.OsFamily osFamily) {
        this.metaInfoDAO = metaInfoDAO;
        this.actionMetaData = actionMetaData;
        repoUpdateExecutor = new org.apache.ambari.server.stack.StackContext.LatestRepoQueryExecutor(osFamily);
    }

    public void registerServiceCheck(java.lang.String serviceName) {
        actionMetaData.addServiceCheckAction(serviceName);
    }

    public void registerRepoUpdateTask(java.net.URI uri, org.apache.ambari.server.stack.StackModule stack) {
        repoUpdateExecutor.addTask(uri, stack);
    }

    public void executeRepoTasks() {
        repoUpdateExecutor.execute();
    }

    public boolean haveAllRepoTasksCompleted() {
        return repoUpdateExecutor.hasCompleted();
    }

    public static class LatestRepoQueryExecutor {
        private java.util.Map<java.net.URI, org.apache.ambari.server.state.stack.RepoUrlInfoCallable> tasks = new java.util.HashMap<>();

        java.util.Collection<java.util.concurrent.Future<?>> futures = new java.util.ArrayList<>();

        private java.util.concurrent.ExecutorService executor = java.util.concurrent.Executors.newFixedThreadPool(org.apache.ambari.server.stack.StackContext.THREAD_COUNT, new java.util.concurrent.ThreadFactory() {
            @java.lang.Override
            public java.lang.Thread newThread(java.lang.Runnable r) {
                return new java.lang.Thread(r, "Stack Version Loading Thread");
            }
        });

        private org.apache.ambari.server.state.stack.OsFamily m_family;

        private LatestRepoQueryExecutor(org.apache.ambari.server.state.stack.OsFamily family) {
            m_family = family;
        }

        public void addTask(java.net.URI uri, org.apache.ambari.server.stack.StackModule stackModule) {
            org.apache.ambari.server.state.stack.RepoUrlInfoCallable callable = null;
            if (tasks.containsKey(uri)) {
                callable = tasks.get(uri);
            } else {
                callable = new org.apache.ambari.server.state.stack.RepoUrlInfoCallable(uri);
                tasks.put(uri, callable);
            }
            callable.addStack(stackModule);
        }

        public void execute() {
            long currentTime = java.lang.System.nanoTime();
            java.util.List<java.util.concurrent.Future<java.util.Map<org.apache.ambari.server.stack.StackModule, org.apache.ambari.server.state.stack.RepoUrlInfoCallable.RepoUrlInfoResult>>> results;
            try {
                results = executor.invokeAll(tasks.values(), 2, java.util.concurrent.TimeUnit.MINUTES);
            } catch (java.lang.InterruptedException e) {
                org.apache.ambari.server.stack.StackContext.LOG.warn("Could not load urlinfo as the executor was interrupted", e);
                return;
            } finally {
                org.apache.ambari.server.stack.StackContext.LOG.info(("Loaded urlinfo in " + java.util.concurrent.TimeUnit.NANOSECONDS.toMillis(java.lang.System.nanoTime() - currentTime)) + "ms");
            }
            java.util.List<java.util.Map<org.apache.ambari.server.stack.StackModule, org.apache.ambari.server.state.stack.RepoUrlInfoCallable.RepoUrlInfoResult>> urlInfoResults = new java.util.ArrayList<>();
            for (java.util.concurrent.Future<java.util.Map<org.apache.ambari.server.stack.StackModule, org.apache.ambari.server.state.stack.RepoUrlInfoCallable.RepoUrlInfoResult>> future : results) {
                try {
                    urlInfoResults.add(future.get());
                } catch (java.lang.Exception e) {
                    org.apache.ambari.server.stack.StackContext.LOG.error("Could not load repo results", e.getCause());
                }
            }
            currentTime = java.lang.System.nanoTime();
            for (java.util.Map<org.apache.ambari.server.stack.StackModule, org.apache.ambari.server.state.stack.RepoUrlInfoCallable.RepoUrlInfoResult> urlInfoResult : urlInfoResults) {
                for (java.util.Map.Entry<org.apache.ambari.server.stack.StackModule, org.apache.ambari.server.state.stack.RepoUrlInfoCallable.RepoUrlInfoResult> entry : urlInfoResult.entrySet()) {
                    org.apache.ambari.server.stack.StackModule stackModule = entry.getKey();
                    org.apache.ambari.server.state.stack.RepoUrlInfoCallable.RepoUrlInfoResult result = entry.getValue();
                    if (null != result) {
                        if (org.apache.commons.collections.MapUtils.isNotEmpty(result.getManifest())) {
                            for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, java.net.URI>> manifestEntry : result.getManifest().entrySet()) {
                                futures.add(executor.submit(new org.apache.ambari.server.state.stack.RepoVdfCallable(stackModule, manifestEntry.getKey(), manifestEntry.getValue(), m_family)));
                            }
                        }
                        if (org.apache.commons.collections.MapUtils.isNotEmpty(result.getLatestVdf())) {
                            futures.add(executor.submit(new org.apache.ambari.server.state.stack.RepoVdfCallable(stackModule, result.getLatestVdf(), m_family)));
                        }
                    }
                }
            }
            executor.shutdown();
            try {
                executor.awaitTermination(2, java.util.concurrent.TimeUnit.MINUTES);
            } catch (java.lang.InterruptedException e) {
                org.apache.ambari.server.stack.StackContext.LOG.warn("Loading all VDF was interrupted", e.getCause());
            } finally {
                org.apache.ambari.server.stack.StackContext.LOG.info(("Loaded all VDF in " + java.util.concurrent.TimeUnit.NANOSECONDS.toMillis(java.lang.System.nanoTime() - currentTime)) + "ms");
            }
        }

        public boolean hasCompleted() {
            for (java.util.concurrent.Future<?> f : futures) {
                if (!f.isDone()) {
                    return false;
                }
            }
            return true;
        }
    }
}