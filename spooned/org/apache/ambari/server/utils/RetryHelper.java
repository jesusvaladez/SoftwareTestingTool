package org.apache.ambari.server.utils;
import org.eclipse.persistence.exceptions.DatabaseException;
public class RetryHelper {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.utils.RetryHelper.class);

    private static org.apache.ambari.server.state.Clusters s_clusters;

    private static java.lang.ThreadLocal<java.util.Set<org.apache.ambari.server.state.Cluster>> affectedClusters = new java.lang.ThreadLocal<java.util.Set<org.apache.ambari.server.state.Cluster>>() {
        @java.lang.Override
        protected java.util.Set<org.apache.ambari.server.state.Cluster> initialValue() {
            return new java.util.HashSet<>();
        }
    };

    private static int operationsRetryAttempts = 0;

    public static void init(org.apache.ambari.server.state.Clusters clusters, int operationsRetryAttempts) {
        org.apache.ambari.server.utils.RetryHelper.s_clusters = clusters;
        org.apache.ambari.server.utils.RetryHelper.operationsRetryAttempts = operationsRetryAttempts;
    }

    public static void addAffectedCluster(org.apache.ambari.server.state.Cluster cluster) {
        if (org.apache.ambari.server.utils.RetryHelper.operationsRetryAttempts > 0) {
            org.apache.ambari.server.utils.RetryHelper.affectedClusters.get().add(cluster);
        }
    }

    public static java.util.Set<org.apache.ambari.server.state.Cluster> getAffectedClusters() {
        return java.util.Collections.unmodifiableSet(org.apache.ambari.server.utils.RetryHelper.affectedClusters.get());
    }

    public static void clearAffectedClusters() {
        if (org.apache.ambari.server.utils.RetryHelper.operationsRetryAttempts > 0) {
            org.apache.ambari.server.utils.RetryHelper.affectedClusters.get().clear();
        }
    }

    public static int getOperationsRetryAttempts() {
        return org.apache.ambari.server.utils.RetryHelper.operationsRetryAttempts;
    }

    public static boolean isDatabaseException(java.lang.Throwable ex) {
        do {
            if (ex instanceof org.eclipse.persistence.exceptions.DatabaseException) {
                return true;
            }
            ex = ex.getCause();
        } while (ex != null );
        return false;
    }

    public static void invalidateAffectedClusters() {
        for (org.apache.ambari.server.state.Cluster cluster : org.apache.ambari.server.utils.RetryHelper.affectedClusters.get()) {
            org.apache.ambari.server.utils.RetryHelper.s_clusters.invalidate(cluster);
            org.apache.ambari.server.utils.RetryHelper.affectedClusters.get().remove(cluster);
        }
    }

    public static <T> T executeWithRetry(java.util.concurrent.Callable<T> command) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.utils.RetryHelper.clearAffectedClusters();
        int retryAttempts = org.apache.ambari.server.utils.RetryHelper.getOperationsRetryAttempts();
        do {
            try {
                return command.call();
            } catch (java.lang.Exception e) {
                if (org.apache.ambari.server.utils.RetryHelper.isDatabaseException(e)) {
                    org.apache.ambari.server.utils.RetryHelper.invalidateAffectedClusters();
                    if (retryAttempts > 0) {
                        org.apache.ambari.server.utils.RetryHelper.LOG.error("Ignoring database exception to perform operation retry, attempts remaining: " + retryAttempts, e);
                        retryAttempts--;
                    } else {
                        org.apache.ambari.server.utils.RetryHelper.clearAffectedClusters();
                        throw new org.apache.ambari.server.AmbariException(e.getMessage(), e);
                    }
                } else {
                    org.apache.ambari.server.utils.RetryHelper.clearAffectedClusters();
                    throw new org.apache.ambari.server.AmbariException(e.getMessage(), e);
                }
            }
        } while (true );
    }
}