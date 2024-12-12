package org.apache.ambari.server.cleanup;
public interface CleanupService<T> {
    interface CleanupResult {
        long getAffectedRows();

        int getErrorCount();
    }

    org.apache.ambari.server.cleanup.CleanupService.CleanupResult cleanup(T cleanupPolicy);
}