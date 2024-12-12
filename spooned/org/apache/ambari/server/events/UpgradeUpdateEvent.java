package org.apache.ambari.server.events;
@com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY)
public class UpgradeUpdateEvent extends org.apache.ambari.server.events.STOMPEvent {
    @com.fasterxml.jackson.annotation.JsonProperty("associated_version")
    private java.lang.String associatedVersion;

    @com.fasterxml.jackson.annotation.JsonProperty("cluster_id")
    private java.lang.Long clusterId;

    @com.fasterxml.jackson.annotation.JsonProperty("direction")
    private org.apache.ambari.server.stack.upgrade.Direction direction;

    @com.fasterxml.jackson.annotation.JsonProperty("downgrade_allowed")
    private java.lang.Boolean downgradeAllowed;

    @com.fasterxml.jackson.annotation.JsonProperty("request_id")
    private java.lang.Long requestId;

    @com.fasterxml.jackson.annotation.JsonProperty("request_status")
    private org.apache.ambari.server.actionmanager.HostRoleStatus requestStatus;

    @com.fasterxml.jackson.annotation.JsonProperty("skip_failures")
    private java.lang.Boolean skipFailures;

    @com.fasterxml.jackson.annotation.JsonProperty("skip_service_check_failures")
    private java.lang.Boolean skipServiceCheckFailures;

    @com.fasterxml.jackson.annotation.JsonProperty("upgrade_type")
    private org.apache.ambari.spi.upgrade.UpgradeType upgradeType;

    @com.fasterxml.jackson.annotation.JsonProperty("start_time")
    private java.lang.Long startTime;

    @com.fasterxml.jackson.annotation.JsonProperty("end_time")
    private java.lang.Long endTime;

    @com.fasterxml.jackson.annotation.JsonProperty("upgrade_id")
    private java.lang.Long upgradeId;

    @com.fasterxml.jackson.annotation.JsonProperty("suspended")
    private java.lang.Boolean suspended;

    @com.fasterxml.jackson.annotation.JsonProperty("progress_percent")
    private java.lang.Double progressPercent;

    @com.fasterxml.jackson.annotation.JsonProperty("revert_allowed")
    private java.lang.Boolean revertAllowed;

    @com.fasterxml.jackson.annotation.JsonProperty("type")
    private org.apache.ambari.server.events.UpdateEventType type;

    private UpgradeUpdateEvent(org.apache.ambari.server.events.UpdateEventType type) {
        super(org.apache.ambari.server.events.STOMPEvent.Type.UPGRADE);
        this.type = type;
    }

    public static org.apache.ambari.server.events.UpgradeUpdateEvent formFullEvent(org.apache.ambari.server.orm.dao.HostRoleCommandDAO hostRoleCommandDAO, org.apache.ambari.server.orm.dao.RequestDAO requestDAO, org.apache.ambari.server.orm.entities.UpgradeEntity upgradeEntity, org.apache.ambari.server.events.UpdateEventType type) {
        org.apache.ambari.server.events.UpgradeUpdateEvent upgradeUpdateEvent = new org.apache.ambari.server.events.UpgradeUpdateEvent(org.apache.ambari.server.events.UpdateEventType.CREATE);
        java.util.Map<java.lang.Long, org.apache.ambari.server.orm.dao.HostRoleCommandStatusSummaryDTO> summary = hostRoleCommandDAO.findAggregateCounts(upgradeEntity.getRequestId());
        org.apache.ambari.server.controller.internal.CalculatedStatus calc = org.apache.ambari.server.controller.internal.CalculatedStatus.statusFromStageSummary(summary, summary.keySet());
        double progressPercent;
        if ((calc.getStatus() == org.apache.ambari.server.actionmanager.HostRoleStatus.ABORTED) && upgradeEntity.isSuspended()) {
            double percent = org.apache.ambari.server.controller.internal.UpgradeResourceProvider.calculateAbortedProgress(summary);
            progressPercent = percent * 100;
        } else {
            progressPercent = calc.getPercent();
        }
        org.apache.ambari.server.orm.entities.RequestEntity rentity = requestDAO.findByPK(upgradeEntity.getRequestId());
        upgradeUpdateEvent.setUpgradeId(upgradeEntity.getId());
        upgradeUpdateEvent.setAssociatedVersion(upgradeEntity.getRepositoryVersion().getVersion());
        upgradeUpdateEvent.setClusterId(upgradeEntity.getClusterId());
        upgradeUpdateEvent.setDirection(upgradeEntity.getDirection());
        upgradeUpdateEvent.setDowngradeAllowed(upgradeEntity.isDowngradeAllowed());
        upgradeUpdateEvent.setRequestId(upgradeEntity.getRequestId());
        upgradeUpdateEvent.setRequestStatus(calc.getStatus());
        upgradeUpdateEvent.setSkipFailures(upgradeEntity.isComponentFailureAutoSkipped());
        upgradeUpdateEvent.setSkipServiceCheckFailures(upgradeEntity.isServiceCheckFailureAutoSkipped());
        upgradeUpdateEvent.setUpgradeType(upgradeEntity.getUpgradeType());
        upgradeUpdateEvent.setStartTime(rentity.getStartTime());
        upgradeUpdateEvent.setEndTime(rentity.getEndTime());
        upgradeUpdateEvent.setSuspended(upgradeEntity.isSuspended());
        upgradeUpdateEvent.setProgressPercent(progressPercent);
        upgradeUpdateEvent.setRevertAllowed(upgradeEntity.isRevertAllowed());
        return upgradeUpdateEvent;
    }

    public static org.apache.ambari.server.events.UpgradeUpdateEvent formUpdateEvent(org.apache.ambari.server.orm.dao.HostRoleCommandDAO hostRoleCommandDAO, org.apache.ambari.server.orm.dao.RequestDAO requestDAO, org.apache.ambari.server.orm.entities.UpgradeEntity upgradeEntity) {
        java.util.Map<java.lang.Long, org.apache.ambari.server.orm.dao.HostRoleCommandStatusSummaryDTO> summary = hostRoleCommandDAO.findAggregateCounts(upgradeEntity.getRequestId());
        org.apache.ambari.server.controller.internal.CalculatedStatus calc = org.apache.ambari.server.controller.internal.CalculatedStatus.statusFromStageSummary(summary, summary.keySet());
        double progressPercent;
        if ((calc.getStatus() == org.apache.ambari.server.actionmanager.HostRoleStatus.ABORTED) && upgradeEntity.isSuspended()) {
            double percent = org.apache.ambari.server.controller.internal.UpgradeResourceProvider.calculateAbortedProgress(summary);
            progressPercent = percent * 100;
        } else {
            progressPercent = calc.getPercent();
        }
        org.apache.ambari.server.orm.entities.RequestEntity rentity = requestDAO.findByPK(upgradeEntity.getRequestId());
        org.apache.ambari.server.events.UpgradeUpdateEvent upgradeUpdateEvent = new org.apache.ambari.server.events.UpgradeUpdateEvent(org.apache.ambari.server.events.UpdateEventType.UPDATE);
        upgradeUpdateEvent.setRequestId(upgradeEntity.getRequestId());
        upgradeUpdateEvent.setProgressPercent(progressPercent);
        upgradeUpdateEvent.setSuspended(upgradeEntity.isSuspended());
        upgradeUpdateEvent.setStartTime(rentity.getStartTime());
        upgradeUpdateEvent.setEndTime(rentity.getEndTime());
        upgradeUpdateEvent.setClusterId(upgradeEntity.getClusterId());
        upgradeUpdateEvent.setRequestStatus(calc.getStatus());
        return upgradeUpdateEvent;
    }

    public java.lang.String getAssociatedVersion() {
        return associatedVersion;
    }

    public void setAssociatedVersion(java.lang.String associatedVersion) {
        this.associatedVersion = associatedVersion;
    }

    public java.lang.Long getClusterId() {
        return clusterId;
    }

    public void setClusterId(java.lang.Long clusterId) {
        this.clusterId = clusterId;
    }

    public org.apache.ambari.server.stack.upgrade.Direction getDirection() {
        return direction;
    }

    public void setDirection(org.apache.ambari.server.stack.upgrade.Direction direction) {
        this.direction = direction;
    }

    public java.lang.Boolean getDowngradeAllowed() {
        return downgradeAllowed;
    }

    public void setDowngradeAllowed(java.lang.Boolean downgradeAllowed) {
        this.downgradeAllowed = downgradeAllowed;
    }

    public java.lang.Long getRequestId() {
        return requestId;
    }

    public void setRequestId(java.lang.Long requestId) {
        this.requestId = requestId;
    }

    public org.apache.ambari.server.actionmanager.HostRoleStatus getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(org.apache.ambari.server.actionmanager.HostRoleStatus requestStatus) {
        this.requestStatus = requestStatus;
    }

    public java.lang.Boolean getSkipFailures() {
        return skipFailures;
    }

    public void setSkipFailures(java.lang.Boolean skipFailures) {
        this.skipFailures = skipFailures;
    }

    public java.lang.Boolean getSkipServiceCheckFailures() {
        return skipServiceCheckFailures;
    }

    public void setSkipServiceCheckFailures(java.lang.Boolean skipServiceCheckFailures) {
        this.skipServiceCheckFailures = skipServiceCheckFailures;
    }

    public org.apache.ambari.spi.upgrade.UpgradeType getUpgradeType() {
        return upgradeType;
    }

    public void setUpgradeType(org.apache.ambari.spi.upgrade.UpgradeType upgradeType) {
        this.upgradeType = upgradeType;
    }

    public java.lang.Long getStartTime() {
        return startTime;
    }

    public void setStartTime(java.lang.Long startTime) {
        this.startTime = startTime;
    }

    public java.lang.Long getEndTime() {
        return endTime;
    }

    public void setEndTime(java.lang.Long endTime) {
        this.endTime = endTime;
    }

    public java.lang.Long getUpgradeId() {
        return upgradeId;
    }

    public void setUpgradeId(java.lang.Long upgradeId) {
        this.upgradeId = upgradeId;
    }

    public java.lang.Boolean getSuspended() {
        return suspended;
    }

    public void setSuspended(java.lang.Boolean suspended) {
        this.suspended = suspended;
    }

    public java.lang.Double getProgressPercent() {
        return progressPercent;
    }

    public void setProgressPercent(java.lang.Double progressPercent) {
        this.progressPercent = progressPercent;
    }

    public java.lang.Boolean getRevertAllowed() {
        return revertAllowed;
    }

    public void setRevertAllowed(java.lang.Boolean revertAllowed) {
        this.revertAllowed = revertAllowed;
    }
}