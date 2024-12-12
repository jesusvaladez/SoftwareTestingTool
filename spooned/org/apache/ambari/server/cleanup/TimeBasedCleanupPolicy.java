package org.apache.ambari.server.cleanup;
public class TimeBasedCleanupPolicy {
    private java.lang.String clusterName;

    private java.lang.Long toDateInMillis;

    public TimeBasedCleanupPolicy(java.lang.String clusterName, java.lang.Long toDateInMillis) {
        this.clusterName = clusterName;
        this.toDateInMillis = toDateInMillis;
    }

    public java.lang.String getClusterName() {
        return clusterName;
    }

    public java.lang.Long getToDateInMillis() {
        return toDateInMillis;
    }

    public org.apache.ambari.server.cleanup.PurgePolicy getPurgePolicy() {
        return org.apache.ambari.server.cleanup.PurgePolicy.DELETE;
    }
}