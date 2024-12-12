package org.apache.ambari.server.orm.entities;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import org.apache.commons.lang.builder.EqualsBuilder;
@javax.persistence.Entity
@javax.persistence.Table(name = "alert_current")
@javax.persistence.TableGenerator(name = "alert_current_id_generator", table = "ambari_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_value", pkColumnValue = "alert_current_id_seq", initialValue = 0)
@javax.persistence.NamedQueries({ @javax.persistence.NamedQuery(name = "AlertCurrentEntity.findAll", query = "SELECT alert FROM AlertCurrentEntity alert"), @javax.persistence.NamedQuery(name = "AlertCurrentEntity.findByCluster", query = "SELECT alert FROM AlertCurrentEntity alert WHERE alert.alertHistory.clusterId = :clusterId"), @javax.persistence.NamedQuery(name = "AlertCurrentEntity.findByDefinitionId", query = "SELECT alert FROM AlertCurrentEntity alert WHERE alert.alertDefinition.definitionId = :definitionId"), @javax.persistence.NamedQuery(name = "AlertCurrentEntity.findByService", query = "SELECT alert FROM AlertCurrentEntity alert WHERE alert.alertHistory.clusterId = :clusterId AND alert.alertHistory.serviceName = :serviceName AND alert.alertHistory.alertDefinition.scope IN :inlist"), @javax.persistence.NamedQuery(name = "AlertCurrentEntity.findByHostAndName", query = "SELECT alert FROM AlertCurrentEntity alert WHERE alert.alertHistory.clusterId = :clusterId AND alert.alertHistory.alertDefinition.definitionName = :definitionName AND alert.alertHistory.hostName = :hostName"), @javax.persistence.NamedQuery(name = "AlertCurrentEntity.findByNameAndNoHost", query = "SELECT alert FROM AlertCurrentEntity alert WHERE alert.alertHistory.clusterId = :clusterId AND alert.alertHistory.alertDefinition.definitionName = :definitionName AND alert.alertHistory.hostName IS NULL"), @javax.persistence.NamedQuery(name = "AlertCurrentEntity.findByHostComponent", query = "SELECT alert FROM AlertCurrentEntity alert WHERE alert.alertHistory.serviceName = :serviceName AND alert.alertHistory.componentName = :componentName AND alert.alertHistory.hostName = :hostName"), @javax.persistence.NamedQuery(name = "AlertCurrentEntity.findByHost", query = "SELECT alert FROM AlertCurrentEntity alert WHERE alert.alertHistory.hostName = :hostName"), @javax.persistence.NamedQuery(name = "AlertCurrentEntity.findByServiceName", query = "SELECT alert FROM AlertCurrentEntity alert WHERE alert.alertHistory.serviceName = :serviceName"), @javax.persistence.NamedQuery(name = "AlertCurrentEntity.findDisabled", query = "SELECT alert FROM AlertCurrentEntity alert WHERE alert.alertDefinition.enabled = 0"), @javax.persistence.NamedQuery(name = "AlertCurrentEntity.removeByHistoryId", query = "DELETE FROM AlertCurrentEntity alert WHERE alert.historyId = :historyId"), @javax.persistence.NamedQuery(name = "AlertCurrentEntity.removeByHistoryIds", query = "DELETE FROM AlertCurrentEntity alert WHERE alert.historyId IN :historyIds"), @javax.persistence.NamedQuery(name = "AlertCurrentEntity.removeByDefinitionId", query = "DELETE FROM AlertCurrentEntity alert WHERE alert.definitionId = :definitionId") })
public class AlertCurrentEntity {
    @javax.persistence.Id
    @javax.persistence.GeneratedValue(strategy = javax.persistence.GenerationType.TABLE, generator = "alert_current_id_generator")
    @javax.persistence.Column(name = "alert_id", nullable = false, updatable = false)
    private java.lang.Long alertId;

    @javax.persistence.Column(name = "history_id", nullable = false, insertable = false, updatable = false, length = 10)
    private java.lang.Long historyId;

    @javax.persistence.Column(name = "definition_id", nullable = false, insertable = false, updatable = false, length = 10)
    private java.lang.Long definitionId;

    @javax.persistence.Column(name = "latest_timestamp", nullable = false)
    private java.lang.Long latestTimestamp;

    @javax.persistence.Column(name = "maintenance_state", length = 255)
    @javax.persistence.Enumerated(javax.persistence.EnumType.STRING)
    private org.apache.ambari.server.state.MaintenanceState maintenanceState = org.apache.ambari.server.state.MaintenanceState.OFF;

    @javax.persistence.Column(name = "original_timestamp", nullable = false)
    private java.lang.Long originalTimestamp;

    @javax.persistence.Lob
    @javax.persistence.Column(name = "latest_text")
    private java.lang.String latestText = null;

    @javax.persistence.Column(name = "occurrences", nullable = false)
    private java.lang.Long occurrences = java.lang.Long.valueOf(1);

    @javax.persistence.Column(name = "firmness", nullable = false)
    @javax.persistence.Enumerated(javax.persistence.EnumType.STRING)
    private org.apache.ambari.server.state.AlertFirmness firmness = org.apache.ambari.server.state.AlertFirmness.HARD;

    @javax.persistence.OneToOne(cascade = { javax.persistence.CascadeType.PERSIST, javax.persistence.CascadeType.REFRESH })
    @javax.persistence.JoinColumn(name = "history_id", unique = true, nullable = false)
    private org.apache.ambari.server.orm.entities.AlertHistoryEntity alertHistory;

    @javax.persistence.OneToOne
    @javax.persistence.JoinColumn(name = "definition_id", unique = false, nullable = false)
    private org.apache.ambari.server.orm.entities.AlertDefinitionEntity alertDefinition;

    public AlertCurrentEntity() {
    }

    public java.lang.Long getAlertId() {
        return alertId;
    }

    public void setAlertId(java.lang.Long alertId) {
        this.alertId = alertId;
    }

    public java.lang.Long getHistoryId() {
        return historyId;
    }

    public void setHistoryId(java.lang.Long historyId) {
        this.historyId = historyId;
    }

    public java.lang.Long getDefinitionId() {
        return definitionId;
    }

    public void setDefinitionId(java.lang.Long definitionId) {
        this.definitionId = definitionId;
    }

    public java.lang.Long getLatestTimestamp() {
        return latestTimestamp;
    }

    public void setLatestTimestamp(java.lang.Long latestTimestamp) {
        this.latestTimestamp = latestTimestamp;
    }

    public org.apache.ambari.server.state.MaintenanceState getMaintenanceState() {
        return maintenanceState;
    }

    public void setMaintenanceState(org.apache.ambari.server.state.MaintenanceState maintenanceState) {
        this.maintenanceState = maintenanceState;
    }

    public java.lang.Long getOriginalTimestamp() {
        return originalTimestamp;
    }

    public void setOriginalTimestamp(java.lang.Long originalTimestamp) {
        this.originalTimestamp = originalTimestamp;
    }

    public java.lang.String getLatestText() {
        return latestText;
    }

    public void setLatestText(java.lang.String text) {
        latestText = text;
    }

    public java.lang.Long getOccurrences() {
        return occurrences;
    }

    public void setOccurrences(long occurrences) {
        this.occurrences = occurrences;
    }

    public org.apache.ambari.server.state.AlertFirmness getFirmness() {
        return firmness;
    }

    public void setFirmness(org.apache.ambari.server.state.AlertFirmness firmness) {
        this.firmness = firmness;
    }

    public org.apache.ambari.server.orm.entities.AlertHistoryEntity getAlertHistory() {
        return alertHistory;
    }

    public void setAlertHistory(org.apache.ambari.server.orm.entities.AlertHistoryEntity alertHistory) {
        this.alertHistory = alertHistory;
        historyId = alertHistory.getAlertId();
        alertDefinition = alertHistory.getAlertDefinition();
        definitionId = alertHistory.getAlertDefinitionId();
        latestText = alertHistory.getAlertText();
    }

    @java.lang.Override
    public boolean equals(java.lang.Object object) {
        if (this == object) {
            return true;
        }
        if ((object == null) || (getClass() != object.getClass())) {
            return false;
        }
        org.apache.ambari.server.orm.entities.AlertCurrentEntity that = ((org.apache.ambari.server.orm.entities.AlertCurrentEntity) (object));
        org.apache.commons.lang.builder.EqualsBuilder equalsBuilder = new org.apache.commons.lang.builder.EqualsBuilder();
        equalsBuilder.append(alertId, that.alertId);
        equalsBuilder.append(alertHistory, that.alertHistory);
        return equalsBuilder.isEquals();
    }

    @java.lang.Override
    public int hashCode() {
        return java.util.Objects.hash(alertId, alertHistory);
    }

    @java.lang.Override
    public java.lang.String toString() {
        java.lang.StringBuilder buffer = new java.lang.StringBuilder("AlertCurrentEntity{");
        buffer.append("alertId=").append(alertId);
        if (null != alertDefinition) {
            buffer.append(", name=").append(alertDefinition.getDefinitionName());
        }
        if (null != alertHistory) {
            buffer.append(", state=").append(alertHistory.getAlertState());
        }
        buffer.append(", latestTimestamp=").append(latestTimestamp);
        buffer.append("}");
        return buffer.toString();
    }
}