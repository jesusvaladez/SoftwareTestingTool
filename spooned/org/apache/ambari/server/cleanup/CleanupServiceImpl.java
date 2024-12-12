package org.apache.ambari.server.cleanup;
@com.google.inject.Singleton
public class CleanupServiceImpl implements org.apache.ambari.server.cleanup.CleanupService<org.apache.ambari.server.cleanup.TimeBasedCleanupPolicy> {
    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.cleanup.CleanupServiceImpl.class);

    class Result implements org.apache.ambari.server.cleanup.CleanupService.CleanupResult {
        private final long affectedRows;

        private final int errorCount;

        public Result(long affectedRows, int errorCount) {
            this.affectedRows = affectedRows;
            this.errorCount = errorCount;
        }

        @java.lang.Override
        public long getAffectedRows() {
            return affectedRows;
        }

        @java.lang.Override
        public int getErrorCount() {
            return errorCount;
        }
    }

    private java.util.Set<org.apache.ambari.server.orm.dao.Cleanable> cleanables;

    @javax.inject.Inject
    protected CleanupServiceImpl(java.util.Set<org.apache.ambari.server.orm.dao.Cleanable> cleanables) {
        this.cleanables = cleanables;
    }

    @java.lang.Override
    public org.apache.ambari.server.cleanup.CleanupService.CleanupResult cleanup(org.apache.ambari.server.cleanup.TimeBasedCleanupPolicy cleanupPolicy) {
        long affectedRows = 0;
        int errorCount = 0;
        for (org.apache.ambari.server.orm.dao.Cleanable cleanable : cleanables) {
            org.apache.ambari.server.cleanup.CleanupServiceImpl.LOGGER.info("Running the purge process for DAO: [{}] with cleanup policy: [{}]", cleanable, cleanupPolicy);
            try {
                affectedRows += cleanable.cleanup(cleanupPolicy);
            } catch (java.lang.Exception ex) {
                org.apache.ambari.server.cleanup.CleanupServiceImpl.LOGGER.error("Running the purge process for DAO: [{}] failed with: {}", cleanable, ex);
                errorCount++;
            }
        }
        return new org.apache.ambari.server.cleanup.CleanupServiceImpl.Result(affectedRows, errorCount);
    }
}