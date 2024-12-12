package org.apache.ambari.server.orm.dao;
public class HostRoleCommandStatusSummaryDTO {
    private java.lang.Long m_stageId = java.lang.Long.valueOf(0L);

    private java.lang.Long m_minTime = java.lang.Long.valueOf(0L);

    private java.lang.Long m_maxTime = java.lang.Long.valueOf(java.lang.Long.MAX_VALUE);

    private boolean m_skippable = false;

    private java.util.Map<org.apache.ambari.server.actionmanager.HostRoleStatus, java.lang.Integer> m_counts = new java.util.HashMap<>();

    private java.util.List<org.apache.ambari.server.actionmanager.HostRoleStatus> m_tasksStatus = new java.util.ArrayList<>();

    public HostRoleCommandStatusSummaryDTO(java.lang.Number skippable, java.lang.Number minStartTime, java.lang.Number maxEndTime, java.lang.Number stageId, java.lang.Number aborted, java.lang.Number completed, java.lang.Number failed, java.lang.Number holding, java.lang.Number holdingFailed, java.lang.Number holdingTimedout, java.lang.Number inProgress, java.lang.Number pending, java.lang.Number queued, java.lang.Number timedout, java.lang.Number skippedFailed) {
        m_stageId = java.lang.Long.valueOf(null == stageId ? 0L : stageId.longValue());
        if (null != skippable) {
            m_skippable = 1 == skippable.intValue();
        }
        if (null != minStartTime) {
            m_minTime = java.lang.Long.valueOf(minStartTime.longValue());
        }
        if (null != maxEndTime) {
            m_maxTime = java.lang.Long.valueOf(maxEndTime.longValue());
        }
        put(org.apache.ambari.server.actionmanager.HostRoleStatus.ABORTED, aborted);
        put(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, completed);
        put(org.apache.ambari.server.actionmanager.HostRoleStatus.FAILED, failed);
        put(org.apache.ambari.server.actionmanager.HostRoleStatus.HOLDING, holding);
        put(org.apache.ambari.server.actionmanager.HostRoleStatus.HOLDING_FAILED, holdingFailed);
        put(org.apache.ambari.server.actionmanager.HostRoleStatus.HOLDING_TIMEDOUT, holdingTimedout);
        put(org.apache.ambari.server.actionmanager.HostRoleStatus.IN_PROGRESS, inProgress);
        put(org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING, pending);
        put(org.apache.ambari.server.actionmanager.HostRoleStatus.QUEUED, queued);
        put(org.apache.ambari.server.actionmanager.HostRoleStatus.TIMEDOUT, timedout);
        put(org.apache.ambari.server.actionmanager.HostRoleStatus.SKIPPED_FAILED, skippedFailed);
    }

    @java.lang.SuppressWarnings("boxing")
    private void put(org.apache.ambari.server.actionmanager.HostRoleStatus status, java.lang.Number number) {
        if (null != number) {
            m_counts.put(status, number.intValue());
            for (int i = 0; i < number.intValue(); i++) {
                m_tasksStatus.add(status);
            }
        } else {
            m_counts.put(status, 0);
        }
    }

    java.lang.Long getStageId() {
        return m_stageId;
    }

    public java.util.Map<org.apache.ambari.server.actionmanager.HostRoleStatus, java.lang.Integer> getCounts() {
        return m_counts;
    }

    public java.util.List<org.apache.ambari.server.actionmanager.HostRoleStatus> getTaskStatuses() {
        return m_tasksStatus;
    }

    public int getTaskTotal() {
        return m_tasksStatus.size();
    }

    public boolean isStageSkippable() {
        return m_skippable;
    }

    public java.lang.Long getStartTime() {
        return m_minTime;
    }

    public java.lang.Long getEndTime() {
        return m_maxTime;
    }

    public static org.apache.ambari.server.orm.dao.HostRoleCommandStatusSummaryDTO create() {
        return new org.apache.ambari.server.orm.dao.HostRoleCommandStatusSummaryDTO(0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L);
    }

    public org.apache.ambari.server.orm.dao.HostRoleCommandStatusSummaryDTO completed(int count) {
        put(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, java.lang.Integer.valueOf(count));
        return this;
    }

    public org.apache.ambari.server.orm.dao.HostRoleCommandStatusSummaryDTO failed(int count) {
        put(org.apache.ambari.server.actionmanager.HostRoleStatus.FAILED, java.lang.Integer.valueOf(count));
        return this;
    }

    public org.apache.ambari.server.orm.dao.HostRoleCommandStatusSummaryDTO aborted(int count) {
        put(org.apache.ambari.server.actionmanager.HostRoleStatus.ABORTED, count);
        return this;
    }

    public org.apache.ambari.server.orm.dao.HostRoleCommandStatusSummaryDTO timedout(int count) {
        put(org.apache.ambari.server.actionmanager.HostRoleStatus.TIMEDOUT, count);
        return this;
    }

    public org.apache.ambari.server.orm.dao.HostRoleCommandStatusSummaryDTO inProgress(int count) {
        put(org.apache.ambari.server.actionmanager.HostRoleStatus.IN_PROGRESS, count);
        return this;
    }

    public org.apache.ambari.server.orm.dao.HostRoleCommandStatusSummaryDTO pending(int count) {
        put(org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING, count);
        return this;
    }

    public org.apache.ambari.server.orm.dao.HostRoleCommandStatusSummaryDTO queued(int count) {
        put(org.apache.ambari.server.actionmanager.HostRoleStatus.QUEUED, count);
        return this;
    }

    public org.apache.ambari.server.orm.dao.HostRoleCommandStatusSummaryDTO skippedFailed(int count) {
        put(org.apache.ambari.server.actionmanager.HostRoleStatus.SKIPPED_FAILED, count);
        return this;
    }
}