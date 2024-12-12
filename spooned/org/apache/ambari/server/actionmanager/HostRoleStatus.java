package org.apache.ambari.server.actionmanager;
public enum HostRoleStatus {

    PENDING,
    QUEUED,
    IN_PROGRESS,
    HOLDING,
    COMPLETED,
    FAILED,
    HOLDING_FAILED,
    TIMEDOUT,
    HOLDING_TIMEDOUT,
    ABORTED,
    SKIPPED_FAILED;
    private static final java.util.List<org.apache.ambari.server.actionmanager.HostRoleStatus> COMPLETED_STATES = com.google.common.collect.ImmutableList.of(org.apache.ambari.server.actionmanager.HostRoleStatus.FAILED, org.apache.ambari.server.actionmanager.HostRoleStatus.TIMEDOUT, org.apache.ambari.server.actionmanager.HostRoleStatus.ABORTED, org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, org.apache.ambari.server.actionmanager.HostRoleStatus.SKIPPED_FAILED);

    private static final java.util.List<org.apache.ambari.server.actionmanager.HostRoleStatus> HOLDING_STATES = com.google.common.collect.ImmutableList.of(org.apache.ambari.server.actionmanager.HostRoleStatus.HOLDING, org.apache.ambari.server.actionmanager.HostRoleStatus.HOLDING_FAILED, org.apache.ambari.server.actionmanager.HostRoleStatus.HOLDING_TIMEDOUT);

    public static final java.util.List<org.apache.ambari.server.actionmanager.HostRoleStatus> SCHEDULED_STATES = com.google.common.collect.ImmutableList.of(org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING, org.apache.ambari.server.actionmanager.HostRoleStatus.QUEUED, org.apache.ambari.server.actionmanager.HostRoleStatus.IN_PROGRESS);

    public static final java.util.Set<org.apache.ambari.server.actionmanager.HostRoleStatus> FAILED_STATUSES = com.google.common.collect.Sets.immutableEnumSet(org.apache.ambari.server.actionmanager.HostRoleStatus.FAILED, org.apache.ambari.server.actionmanager.HostRoleStatus.TIMEDOUT, org.apache.ambari.server.actionmanager.HostRoleStatus.ABORTED, org.apache.ambari.server.actionmanager.HostRoleStatus.SKIPPED_FAILED);

    public static final java.util.Set<org.apache.ambari.server.actionmanager.HostRoleStatus> NOT_SKIPPABLE_FAILED_STATUSES = com.google.common.collect.Sets.immutableEnumSet(org.apache.ambari.server.actionmanager.HostRoleStatus.FAILED, org.apache.ambari.server.actionmanager.HostRoleStatus.TIMEDOUT, org.apache.ambari.server.actionmanager.HostRoleStatus.ABORTED);

    public static final java.util.Set<org.apache.ambari.server.actionmanager.HostRoleStatus> STACK_UPGRADE_FAILED_STATUSES = com.google.common.collect.Sets.immutableEnumSet(org.apache.ambari.server.actionmanager.HostRoleStatus.FAILED, org.apache.ambari.server.actionmanager.HostRoleStatus.HOLDING_FAILED, org.apache.ambari.server.actionmanager.HostRoleStatus.HOLDING_TIMEDOUT);

    public static final java.util.Set<org.apache.ambari.server.actionmanager.HostRoleStatus> IN_PROGRESS_STATUSES = com.google.common.collect.Sets.immutableEnumSet(org.apache.ambari.server.actionmanager.HostRoleStatus.QUEUED, org.apache.ambari.server.actionmanager.HostRoleStatus.IN_PROGRESS, org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING, org.apache.ambari.server.actionmanager.HostRoleStatus.HOLDING, org.apache.ambari.server.actionmanager.HostRoleStatus.HOLDING_FAILED, org.apache.ambari.server.actionmanager.HostRoleStatus.HOLDING_TIMEDOUT);

    public static final java.util.Set<org.apache.ambari.server.actionmanager.HostRoleStatus> NOT_COMPLETED_STATUSES = com.google.common.collect.ImmutableSet.copyOf(java.util.EnumSet.complementOf(java.util.EnumSet.of(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED)));

    public boolean isFailedState() {
        return org.apache.ambari.server.actionmanager.HostRoleStatus.FAILED_STATUSES.contains(this);
    }

    public boolean isFailedAndNotSkippableState() {
        return org.apache.ambari.server.actionmanager.HostRoleStatus.NOT_SKIPPABLE_FAILED_STATUSES.contains(this);
    }

    public boolean isCompletedState() {
        return org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED_STATES.contains(this);
    }

    public boolean isHoldingState() {
        return org.apache.ambari.server.actionmanager.HostRoleStatus.HOLDING_STATES.contains(this);
    }

    public static java.util.List<org.apache.ambari.server.actionmanager.HostRoleStatus> getCompletedStates() {
        return org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED_STATES;
    }

    public boolean isInProgress() {
        return org.apache.ambari.server.actionmanager.HostRoleStatus.IN_PROGRESS_STATUSES.contains(this);
    }
}