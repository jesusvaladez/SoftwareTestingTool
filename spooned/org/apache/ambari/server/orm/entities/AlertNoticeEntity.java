package org.apache.ambari.server.orm.entities;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import org.apache.commons.lang.builder.EqualsBuilder;
@javax.persistence.Entity
@javax.persistence.Table(name = "alert_notice")
@javax.persistence.TableGenerator(name = "alert_notice_id_generator", table = "ambari_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_value", pkColumnValue = "alert_notice_id_seq", initialValue = 0)
@javax.persistence.NamedQueries({ @javax.persistence.NamedQuery(name = "AlertNoticeEntity.findAll", query = "SELECT notice FROM AlertNoticeEntity notice"), @javax.persistence.NamedQuery(name = "AlertNoticeEntity.findByState", query = "SELECT notice FROM AlertNoticeEntity notice WHERE notice.notifyState = :notifyState  ORDER BY  notice.notificationId"), @javax.persistence.NamedQuery(name = "AlertNoticeEntity.findByUuid", query = "SELECT notice FROM AlertNoticeEntity notice WHERE notice.uuid = :uuid"), @javax.persistence.NamedQuery(name = "AlertNoticeEntity.findByHistoryIds", query = "SELECT notice FROM AlertNoticeEntity notice WHERE notice.historyId IN :historyIds"), @javax.persistence.NamedQuery(name = "AlertNoticeEntity.removeByHistoryIds", query = "DELETE FROM AlertNoticeEntity notice WHERE notice.historyId IN :historyIds") })
public class AlertNoticeEntity {
    @javax.persistence.Id
    @javax.persistence.GeneratedValue(strategy = javax.persistence.GenerationType.TABLE, generator = "alert_notice_id_generator")
    @javax.persistence.Column(name = "notification_id", nullable = false, updatable = false)
    private java.lang.Long notificationId;

    @javax.persistence.Enumerated(javax.persistence.EnumType.STRING)
    @javax.persistence.Column(name = "notify_state", nullable = false, length = 255)
    private org.apache.ambari.server.state.NotificationState notifyState;

    @javax.persistence.Basic
    @javax.persistence.Column(nullable = false, length = 64, unique = true)
    private java.lang.String uuid;

    @javax.persistence.ManyToOne
    @javax.persistence.JoinColumn(name = "history_id", nullable = false)
    private org.apache.ambari.server.orm.entities.AlertHistoryEntity alertHistory;

    @javax.persistence.Column(name = "history_id", nullable = false, insertable = false, updatable = false, length = 10)
    private java.lang.Long historyId;

    @javax.persistence.ManyToOne(cascade = { javax.persistence.CascadeType.MERGE, javax.persistence.CascadeType.REFRESH })
    @javax.persistence.JoinColumn(name = "target_id", nullable = false)
    private org.apache.ambari.server.orm.entities.AlertTargetEntity alertTarget;

    public AlertNoticeEntity() {
    }

    public java.lang.Long getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(java.lang.Long notificationId) {
        this.notificationId = notificationId;
    }

    public org.apache.ambari.server.state.NotificationState getNotifyState() {
        return notifyState;
    }

    public void setNotifyState(org.apache.ambari.server.state.NotificationState notifyState) {
        this.notifyState = notifyState;
    }

    public java.lang.String getUuid() {
        return uuid;
    }

    public void setUuid(java.lang.String uuid) {
        this.uuid = uuid;
    }

    public org.apache.ambari.server.orm.entities.AlertHistoryEntity getAlertHistory() {
        return alertHistory;
    }

    public void setAlertHistory(org.apache.ambari.server.orm.entities.AlertHistoryEntity alertHistory) {
        this.alertHistory = alertHistory;
        historyId = alertHistory.getAlertId();
    }

    public java.lang.Long getHistoryId() {
        return historyId;
    }

    public void setHistoryId(java.lang.Long historyId) {
        this.historyId = historyId;
    }

    public org.apache.ambari.server.orm.entities.AlertTargetEntity getAlertTarget() {
        return alertTarget;
    }

    public void setAlertTarget(org.apache.ambari.server.orm.entities.AlertTargetEntity alertTarget) {
        this.alertTarget = alertTarget;
        alertTarget.addAlertNotice(this);
    }

    @java.lang.Override
    public boolean equals(java.lang.Object object) {
        if (this == object) {
            return true;
        }
        if ((object == null) || (getClass() != object.getClass())) {
            return false;
        }
        org.apache.ambari.server.orm.entities.AlertNoticeEntity that = ((org.apache.ambari.server.orm.entities.AlertNoticeEntity) (object));
        org.apache.commons.lang.builder.EqualsBuilder equalsBuilder = new org.apache.commons.lang.builder.EqualsBuilder();
        equalsBuilder.append(uuid, that.uuid);
        return equalsBuilder.isEquals();
    }

    @java.lang.Override
    public int hashCode() {
        int result = (null != uuid) ? uuid.hashCode() : 0;
        return result;
    }
}