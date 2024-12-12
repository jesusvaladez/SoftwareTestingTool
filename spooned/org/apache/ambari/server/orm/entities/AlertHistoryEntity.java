package org.apache.ambari.server.orm.entities;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
@javax.persistence.Entity
@javax.persistence.Table(name = "alert_history")
@javax.persistence.TableGenerator(name = "alert_history_id_generator", table = "ambari_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_value", pkColumnValue = "alert_history_id_seq", initialValue = 0)
@javax.persistence.NamedQueries({ @javax.persistence.NamedQuery(name = "AlertHistoryEntity.findAll", query = "SELECT alertHistory FROM AlertHistoryEntity alertHistory"), @javax.persistence.NamedQuery(name = "AlertHistoryEntity.findAllInCluster", query = "SELECT alertHistory FROM AlertHistoryEntity alertHistory WHERE alertHistory.clusterId = :clusterId"), @javax.persistence.NamedQuery(name = "AlertHistoryEntity.findAllInClusterWithState", query = "SELECT alertHistory FROM AlertHistoryEntity alertHistory WHERE alertHistory.clusterId = :clusterId AND alertHistory.alertState IN :alertStates"), @javax.persistence.NamedQuery(name = "AlertHistoryEntity.findAllInClusterBetweenDates", query = "SELECT alertHistory FROM AlertHistoryEntity alertHistory WHERE alertHistory.clusterId = :clusterId AND alertHistory.alertTimestamp BETWEEN :startDate AND :endDate"), @javax.persistence.NamedQuery(name = "AlertHistoryEntity.findAllInClusterBeforeDate", query = "SELECT alertHistory FROM AlertHistoryEntity alertHistory WHERE alertHistory.clusterId = :clusterId AND alertHistory.alertTimestamp <= :beforeDate"), @javax.persistence.NamedQuery(name = "AlertHistoryEntity.findAllIdsInClusterBeforeDate", query = "SELECT alertHistory.alertId FROM AlertHistoryEntity alertHistory WHERE alertHistory.clusterId = :clusterId AND alertHistory.alertTimestamp <= :beforeDate"), @javax.persistence.NamedQuery(name = "AlertHistoryEntity.findAllInClusterAfterDate", query = "SELECT alertHistory FROM AlertHistoryEntity alertHistory WHERE alertHistory.clusterId = :clusterId AND alertHistory.alertTimestamp >= :afterDate"), @javax.persistence.NamedQuery(name = "AlertHistoryEntity.removeByDefinitionId", query = "DELETE FROM AlertHistoryEntity alertHistory WHERE alertHistory.alertDefinitionId = :definitionId"), @javax.persistence.NamedQuery(name = "AlertHistoryEntity.removeInClusterBeforeDate", query = "DELETE FROM AlertHistoryEntity alertHistory WHERE alertHistory.clusterId = :clusterId AND alertHistory.alertTimestamp <= :beforeDate"), @javax.persistence.NamedQuery(name = "AlertHistoryEntity.findHistoryIdsByDefinitionId", query = "SELECT alertHistory.alertId FROM AlertHistoryEntity alertHistory WHERE alertHistory.alertDefinitionId = :definitionId ORDER BY alertHistory.alertId") })
public class AlertHistoryEntity {
    @javax.persistence.Id
    @javax.persistence.GeneratedValue(strategy = javax.persistence.GenerationType.TABLE, generator = "alert_history_id_generator")
    @javax.persistence.Column(name = "alert_id", nullable = false, updatable = false)
    private java.lang.Long alertId;

    @javax.persistence.Column(name = "alert_instance", length = 255)
    private java.lang.String alertInstance;

    @javax.persistence.Column(name = "alert_label", length = 1024)
    private java.lang.String alertLabel;

    @javax.persistence.Enumerated(javax.persistence.EnumType.STRING)
    @javax.persistence.Column(name = "alert_state", nullable = false, length = 255)
    private org.apache.ambari.server.state.AlertState alertState;

    @javax.persistence.Lob
    @javax.persistence.Column(name = "alert_text")
    private java.lang.String alertText;

    @javax.persistence.Column(name = "alert_timestamp", nullable = false)
    private java.lang.Long alertTimestamp;

    @javax.persistence.Column(name = "cluster_id", nullable = false)
    private java.lang.Long clusterId;

    @javax.persistence.Column(name = "component_name", length = 255)
    private java.lang.String componentName;

    @javax.persistence.Column(name = "host_name", length = 255)
    private java.lang.String hostName;

    @javax.persistence.Column(name = "service_name", nullable = false, length = 255)
    private java.lang.String serviceName;

    @javax.persistence.ManyToOne
    @javax.persistence.JoinColumn(name = "alert_definition_id", nullable = false)
    private org.apache.ambari.server.orm.entities.AlertDefinitionEntity alertDefinition;

    @javax.persistence.Column(name = "alert_definition_id", nullable = false, insertable = false, updatable = false, length = 10)
    private java.lang.Long alertDefinitionId;

    public AlertHistoryEntity() {
    }

    public java.lang.Long getAlertId() {
        return alertId;
    }

    public void setAlertId(java.lang.Long alertId) {
        this.alertId = alertId;
    }

    public java.lang.String getAlertInstance() {
        return alertInstance;
    }

    public void setAlertInstance(java.lang.String alertInstance) {
        this.alertInstance = alertInstance;
    }

    public java.lang.String getAlertLabel() {
        return alertLabel;
    }

    public void setAlertLabel(java.lang.String alertLabel) {
        this.alertLabel = alertLabel;
    }

    public org.apache.ambari.server.state.AlertState getAlertState() {
        return alertState;
    }

    public void setAlertState(org.apache.ambari.server.state.AlertState alertState) {
        this.alertState = alertState;
    }

    public java.lang.String getAlertText() {
        return alertText;
    }

    public void setAlertText(java.lang.String alertText) {
        this.alertText = alertText;
    }

    public java.lang.Long getAlertTimestamp() {
        return alertTimestamp;
    }

    public void setAlertTimestamp(java.lang.Long alertTimestamp) {
        this.alertTimestamp = alertTimestamp;
    }

    public java.lang.Long getClusterId() {
        return clusterId;
    }

    public void setClusterId(java.lang.Long clusterId) {
        this.clusterId = clusterId;
    }

    public java.lang.String getComponentName() {
        return componentName;
    }

    public void setComponentName(java.lang.String componentName) {
        this.componentName = componentName;
    }

    public java.lang.String getHostName() {
        return hostName;
    }

    public void setHostName(java.lang.String hostName) {
        this.hostName = hostName;
    }

    public java.lang.String getServiceName() {
        return serviceName;
    }

    public void setServiceName(java.lang.String serviceName) {
        this.serviceName = serviceName;
    }

    public org.apache.ambari.server.orm.entities.AlertDefinitionEntity getAlertDefinition() {
        return alertDefinition;
    }

    public void setAlertDefinition(org.apache.ambari.server.orm.entities.AlertDefinitionEntity alertDefinition) {
        this.alertDefinition = alertDefinition;
        alertDefinitionId = alertDefinition.getDefinitionId();
    }

    public java.lang.Long getAlertDefinitionId() {
        return alertDefinitionId;
    }

    public void setAlertDefinitionId(java.lang.Long alertDefinitionId) {
        this.alertDefinitionId = alertDefinitionId;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object object) {
        if (this == object) {
            return true;
        }
        if ((object == null) || (getClass() != object.getClass())) {
            return false;
        }
        org.apache.ambari.server.orm.entities.AlertHistoryEntity that = ((org.apache.ambari.server.orm.entities.AlertHistoryEntity) (object));
        if (null != alertId) {
            return java.util.Objects.equals(alertId, that.alertId);
        }
        return (((((((((java.util.Objects.equals(alertId, that.alertId) && java.util.Objects.equals(clusterId, that.clusterId)) && java.util.Objects.equals(alertInstance, that.alertInstance)) && java.util.Objects.equals(alertLabel, that.alertLabel)) && java.util.Objects.equals(alertState, that.alertState)) && java.util.Objects.equals(alertText, that.alertText)) && java.util.Objects.equals(alertTimestamp, that.alertTimestamp)) && java.util.Objects.equals(serviceName, that.serviceName)) && java.util.Objects.equals(componentName, that.componentName)) && java.util.Objects.equals(hostName, that.hostName)) && java.util.Objects.equals(alertDefinition, that.alertDefinition);
    }

    @java.lang.Override
    public int hashCode() {
        if (null != alertId) {
            return alertId.hashCode();
        }
        return java.util.Objects.hash(alertId, clusterId, alertInstance, alertLabel, alertState, alertText, alertTimestamp, serviceName, componentName, hostName, alertDefinition);
    }

    @java.lang.Override
    public java.lang.String toString() {
        java.lang.StringBuilder buffer = new java.lang.StringBuilder();
        buffer.append(getClass().getSimpleName());
        buffer.append("{");
        buffer.append("id=").append(alertId);
        buffer.append(", serviceName=").append(serviceName);
        buffer.append(", componentName=").append(componentName);
        buffer.append(", state=").append(alertState);
        buffer.append(", label=").append(alertLabel);
        buffer.append("}");
        return buffer.toString();
    }

    public int getAlertDefinitionHash() {
        return this.getAlertDefinition().hashCode();
    }
}