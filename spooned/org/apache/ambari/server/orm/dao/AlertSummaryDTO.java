package org.apache.ambari.server.orm.dao;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
public class AlertSummaryDTO {
    @com.fasterxml.jackson.annotation.JsonProperty("OK")
    private int okCount;

    @com.fasterxml.jackson.annotation.JsonProperty("WARNING")
    private int warningCount;

    @com.fasterxml.jackson.annotation.JsonProperty("CRITICAL")
    private int criticalCount;

    @com.fasterxml.jackson.annotation.JsonProperty("UNKNOWN")
    private int unknownCount;

    @com.fasterxml.jackson.annotation.JsonProperty("MAINTENANCE")
    private int maintenanceCount;

    public AlertSummaryDTO(java.lang.Number ok, java.lang.Number warning, java.lang.Number critical, java.lang.Number unknown, java.lang.Number maintenance) {
        okCount = (null == ok) ? 0 : ok.intValue();
        warningCount = (null == warning) ? 0 : warning.intValue();
        criticalCount = (null == critical) ? 0 : critical.intValue();
        unknownCount = (null == unknown) ? 0 : unknown.intValue();
        maintenanceCount = (null == maintenance) ? 0 : maintenance.intValue();
    }

    public int getOkCount() {
        return okCount;
    }

    public int getWarningCount() {
        return warningCount;
    }

    public int getCriticalCount() {
        return criticalCount;
    }

    public int getUnknownCount() {
        return unknownCount;
    }

    public int getMaintenanceCount() {
        return maintenanceCount;
    }

    public void setOkCount(int okCount) {
        this.okCount = okCount;
    }

    public void setWarningCount(int warningCount) {
        this.warningCount = warningCount;
    }

    public void setCriticalCount(int criticalCount) {
        this.criticalCount = criticalCount;
    }

    public void setUnknownCount(int unknownCount) {
        this.unknownCount = unknownCount;
    }

    public void setMaintenanceCount(int maintenanceCount) {
        this.maintenanceCount = maintenanceCount;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o)
            return true;

        if (!(o instanceof org.apache.ambari.server.orm.dao.AlertSummaryDTO))
            return false;

        org.apache.ambari.server.orm.dao.AlertSummaryDTO that = ((org.apache.ambari.server.orm.dao.AlertSummaryDTO) (o));
        return new org.apache.commons.lang.builder.EqualsBuilder().append(okCount, that.okCount).append(warningCount, that.warningCount).append(criticalCount, that.criticalCount).append(unknownCount, that.unknownCount).append(maintenanceCount, that.maintenanceCount).isEquals();
    }

    @java.lang.Override
    public int hashCode() {
        return new org.apache.commons.lang.builder.HashCodeBuilder(17, 37).append(okCount).append(warningCount).append(criticalCount).append(unknownCount).append(maintenanceCount).toHashCode();
    }
}