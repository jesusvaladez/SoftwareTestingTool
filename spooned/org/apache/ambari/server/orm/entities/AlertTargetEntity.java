package org.apache.ambari.server.orm.entities;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.PreRemove;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
@javax.persistence.Entity
@javax.persistence.Table(name = "alert_target")
@javax.persistence.TableGenerator(name = "alert_target_id_generator", table = "ambari_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_value", pkColumnValue = "alert_target_id_seq", initialValue = 0)
@javax.persistence.NamedQueries({ @javax.persistence.NamedQuery(name = "AlertTargetEntity.findAll", query = "SELECT alertTarget FROM AlertTargetEntity alertTarget"), @javax.persistence.NamedQuery(name = "AlertTargetEntity.findAllGlobal", query = "SELECT alertTarget FROM AlertTargetEntity alertTarget WHERE alertTarget.isGlobal = 1"), @javax.persistence.NamedQuery(name = "AlertTargetEntity.findByName", query = "SELECT alertTarget FROM AlertTargetEntity alertTarget WHERE alertTarget.targetName = :targetName"), @javax.persistence.NamedQuery(name = "AlertTargetEntity.findByIds", query = "SELECT alertTarget FROM AlertTargetEntity alertTarget WHERE alertTarget.targetId IN :targetIds") })
public class AlertTargetEntity {
    @javax.persistence.Id
    @javax.persistence.GeneratedValue(strategy = javax.persistence.GenerationType.TABLE, generator = "alert_target_id_generator")
    @javax.persistence.Column(name = "target_id", nullable = false, updatable = false)
    private java.lang.Long targetId;

    @javax.persistence.Column(length = 1024)
    private java.lang.String description;

    @javax.persistence.Column(name = "notification_type", nullable = false, length = 64)
    private java.lang.String notificationType;

    @javax.persistence.Lob
    @javax.persistence.Basic
    @javax.persistence.Column(length = 32672)
    private java.lang.String properties;

    @javax.persistence.Column(name = "target_name", unique = true, nullable = false, length = 255)
    private java.lang.String targetName;

    @javax.persistence.Column(name = "is_global", nullable = false, length = 1)
    private java.lang.Short isGlobal = java.lang.Short.valueOf(((short) (0)));

    @javax.persistence.Column(name = "is_enabled", nullable = false, length = 1)
    private java.lang.Short isEnabled = java.lang.Short.valueOf(((short) (1)));

    @javax.persistence.ManyToMany(fetch = javax.persistence.FetchType.EAGER, mappedBy = "alertTargets", cascade = { javax.persistence.CascadeType.MERGE, javax.persistence.CascadeType.REFRESH })
    private java.util.Set<org.apache.ambari.server.orm.entities.AlertGroupEntity> alertGroups;

    @javax.persistence.Enumerated(javax.persistence.EnumType.STRING)
    @javax.persistence.ElementCollection(targetClass = org.apache.ambari.server.state.AlertState.class)
    @javax.persistence.CollectionTable(name = "alert_target_states", joinColumns = @javax.persistence.JoinColumn(name = "target_id"))
    @javax.persistence.Column(name = "alert_state")
    private java.util.Set<org.apache.ambari.server.state.AlertState> alertStates = java.util.EnumSet.allOf(org.apache.ambari.server.state.AlertState.class);

    @javax.persistence.OneToMany(cascade = javax.persistence.CascadeType.REMOVE, mappedBy = "alertTarget")
    private java.util.List<org.apache.ambari.server.orm.entities.AlertNoticeEntity> alertNotices;

    public java.lang.Long getTargetId() {
        return targetId;
    }

    public void setTargetId(java.lang.Long targetId) {
        this.targetId = targetId;
    }

    public java.lang.String getDescription() {
        return description;
    }

    public void setDescription(java.lang.String description) {
        this.description = description;
    }

    public java.lang.String getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(java.lang.String notificationType) {
        this.notificationType = notificationType;
    }

    public java.lang.String getProperties() {
        return properties;
    }

    public void setProperties(java.lang.String properties) {
        this.properties = properties;
    }

    public java.lang.String getTargetName() {
        return targetName;
    }

    public boolean isGlobal() {
        return isGlobal == 0 ? false : true;
    }

    public void setGlobal(boolean isGlobal) {
        this.isGlobal = (isGlobal) ? ((short) (1)) : ((short) (0));
    }

    public boolean isEnabled() {
        return isEnabled == 0 ? false : true;
    }

    public void setEnabled(boolean isEnabled) {
        this.isEnabled = (isEnabled) ? ((short) (1)) : ((short) (0));
    }

    public java.util.Set<org.apache.ambari.server.state.AlertState> getAlertStates() {
        return alertStates;
    }

    public void setAlertStates(java.util.Set<org.apache.ambari.server.state.AlertState> alertStates) {
        this.alertStates = alertStates;
    }

    public void setTargetName(java.lang.String targetName) {
        this.targetName = targetName;
    }

    public java.util.Set<org.apache.ambari.server.orm.entities.AlertGroupEntity> getAlertGroups() {
        if (null == alertGroups) {
            return java.util.Collections.emptySet();
        }
        return com.google.common.collect.ImmutableSet.copyOf(alertGroups);
    }

    public void setAlertGroups(java.util.Set<org.apache.ambari.server.orm.entities.AlertGroupEntity> alertGroups) {
        java.util.Set<org.apache.ambari.server.orm.entities.AlertGroupEntity> groups = getAlertGroups();
        for (org.apache.ambari.server.orm.entities.AlertGroupEntity group : groups) {
            group.removeAlertTarget(this);
        }
        this.alertGroups = alertGroups;
        if (null != alertGroups) {
            for (org.apache.ambari.server.orm.entities.AlertGroupEntity group : alertGroups) {
                group.addAlertTarget(this);
            }
        }
    }

    protected void addAlertGroup(org.apache.ambari.server.orm.entities.AlertGroupEntity alertGroup) {
        if (null == alertGroups) {
            alertGroups = new java.util.HashSet<>();
        }
        alertGroups.add(alertGroup);
    }

    protected void removeAlertGroup(org.apache.ambari.server.orm.entities.AlertGroupEntity alertGroup) {
        if (null != alertGroups) {
            alertGroups.remove(alertGroup);
        }
    }

    protected void addAlertNotice(org.apache.ambari.server.orm.entities.AlertNoticeEntity notice) {
        if (null == alertNotices) {
            alertNotices = new java.util.ArrayList<>();
        }
        alertNotices.add(notice);
    }

    public java.util.List<org.apache.ambari.server.orm.entities.AlertNoticeEntity> getAlertNotices() {
        return alertNotices;
    }

    public void setAlertNotices(java.util.List<org.apache.ambari.server.orm.entities.AlertNoticeEntity> alertNotices) {
        this.alertNotices = alertNotices;
    }

    @javax.persistence.PreRemove
    public void preRemove() {
        java.util.Set<org.apache.ambari.server.orm.entities.AlertGroupEntity> groups = getAlertGroups();
        if (!groups.isEmpty()) {
            for (org.apache.ambari.server.orm.entities.AlertGroupEntity group : groups) {
                group.removeAlertTarget(this);
            }
        }
    }

    @java.lang.Override
    public boolean equals(java.lang.Object object) {
        if (this == object) {
            return true;
        }
        if ((object == null) || (getClass() != object.getClass())) {
            return false;
        }
        org.apache.ambari.server.orm.entities.AlertTargetEntity that = ((org.apache.ambari.server.orm.entities.AlertTargetEntity) (object));
        if (null != targetId) {
            return java.util.Objects.equals(targetId, that.targetId);
        }
        return ((((java.util.Objects.equals(targetId, that.targetId) && java.util.Objects.equals(targetName, that.targetName)) && java.util.Objects.equals(notificationType, that.notificationType)) && java.util.Objects.equals(isEnabled, that.isEnabled)) && java.util.Objects.equals(description, that.description)) && java.util.Objects.equals(isGlobal, that.isGlobal);
    }

    @java.lang.Override
    public int hashCode() {
        if (null != targetId) {
            return targetId.hashCode();
        }
        return java.util.Objects.hash(targetId, targetName, notificationType, isEnabled, description, isGlobal);
    }

    @java.lang.Override
    public java.lang.String toString() {
        java.lang.StringBuilder buffer = new java.lang.StringBuilder();
        buffer.append(getClass().getSimpleName());
        buffer.append("{");
        buffer.append("id=").append(targetId);
        buffer.append(", name=").append(targetName);
        buffer.append("}");
        return buffer.toString();
    }
}