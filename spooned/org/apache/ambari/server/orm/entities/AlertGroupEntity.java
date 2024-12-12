package org.apache.ambari.server.orm.entities;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.UniqueConstraint;
@javax.persistence.Entity
@javax.persistence.Table(name = "alert_group", uniqueConstraints = @javax.persistence.UniqueConstraint(columnNames = { "cluster_id", "group_name" }))
@javax.persistence.TableGenerator(name = "alert_group_id_generator", table = "ambari_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_value", pkColumnValue = "alert_group_id_seq", initialValue = 0)
@javax.persistence.NamedQueries({ @javax.persistence.NamedQuery(name = "AlertGroupEntity.findAll", query = "SELECT alertGroup FROM AlertGroupEntity alertGroup"), @javax.persistence.NamedQuery(name = "AlertGroupEntity.findAllInCluster", query = "SELECT alertGroup FROM AlertGroupEntity alertGroup WHERE alertGroup.clusterId = :clusterId"), @javax.persistence.NamedQuery(name = "AlertGroupEntity.findByNameInCluster", query = "SELECT alertGroup FROM AlertGroupEntity alertGroup WHERE alertGroup.groupName = :groupName AND alertGroup.clusterId = :clusterId"), @javax.persistence.NamedQuery(name = "AlertGroupEntity.findByAssociatedDefinition", query = "SELECT alertGroup FROM AlertGroupEntity alertGroup WHERE :alertDefinition MEMBER OF alertGroup.alertDefinitions"), @javax.persistence.NamedQuery(name = "AlertGroupEntity.findServiceDefaultGroup", query = "SELECT alertGroup FROM AlertGroupEntity alertGroup WHERE alertGroup.clusterId = :clusterId AND alertGroup.serviceName = :serviceName AND alertGroup.isDefault = 1"), @javax.persistence.NamedQuery(name = "AlertGroupEntity.findByIds", query = "SELECT alertGroup FROM AlertGroupEntity alertGroup WHERE alertGroup.groupId IN :groupIds") })
public class AlertGroupEntity {
    @javax.persistence.Id
    @javax.persistence.GeneratedValue(strategy = javax.persistence.GenerationType.TABLE, generator = "alert_group_id_generator")
    @javax.persistence.Column(name = "group_id", nullable = false, updatable = false)
    private java.lang.Long groupId;

    @javax.persistence.Column(name = "cluster_id", nullable = false)
    private java.lang.Long clusterId;

    @javax.persistence.Column(name = "group_name", nullable = false, length = 255)
    private java.lang.String groupName;

    @javax.persistence.Column(name = "is_default", nullable = false)
    private java.lang.Integer isDefault = java.lang.Integer.valueOf(0);

    @javax.persistence.Column(name = "service_name", nullable = true, length = 255)
    private java.lang.String serviceName;

    @javax.persistence.ManyToMany(cascade = javax.persistence.CascadeType.MERGE)
    @javax.persistence.JoinTable(name = "alert_grouping", joinColumns = { @javax.persistence.JoinColumn(name = "group_id", nullable = false) }, inverseJoinColumns = { @javax.persistence.JoinColumn(name = "definition_id", nullable = false) })
    private java.util.Set<org.apache.ambari.server.orm.entities.AlertDefinitionEntity> alertDefinitions;

    @javax.persistence.ManyToMany(fetch = javax.persistence.FetchType.EAGER, cascade = { javax.persistence.CascadeType.MERGE, javax.persistence.CascadeType.REFRESH })
    @javax.persistence.JoinTable(name = "alert_group_target", joinColumns = { @javax.persistence.JoinColumn(name = "group_id", nullable = false) }, inverseJoinColumns = { @javax.persistence.JoinColumn(name = "target_id", nullable = false) })
    private java.util.Set<org.apache.ambari.server.orm.entities.AlertTargetEntity> alertTargets;

    public java.lang.Long getGroupId() {
        return groupId;
    }

    public void setGroupId(java.lang.Long groupId) {
        this.groupId = groupId;
    }

    public java.lang.Long getClusterId() {
        return clusterId;
    }

    public void setClusterId(java.lang.Long clusterId) {
        this.clusterId = clusterId;
    }

    public java.lang.String getGroupName() {
        return groupName;
    }

    public void setGroupName(java.lang.String groupName) {
        this.groupName = groupName;
    }

    public boolean isDefault() {
        return isDefault == 0 ? false : true;
    }

    public void setDefault(boolean isDefault) {
        this.isDefault = (isDefault == false) ? 0 : 1;
    }

    public java.lang.String getServiceName() {
        return serviceName;
    }

    public void setServiceName(java.lang.String serviceName) {
        this.serviceName = serviceName;
    }

    public java.util.Set<org.apache.ambari.server.orm.entities.AlertDefinitionEntity> getAlertDefinitions() {
        if (null == alertDefinitions) {
            alertDefinitions = new java.util.HashSet<>();
        }
        return java.util.Collections.unmodifiableSet(alertDefinitions);
    }

    public void setAlertDefinitions(java.util.Set<org.apache.ambari.server.orm.entities.AlertDefinitionEntity> alertDefinitions) {
        if (null != this.alertDefinitions) {
            for (org.apache.ambari.server.orm.entities.AlertDefinitionEntity definition : this.alertDefinitions) {
                definition.removeAlertGroup(this);
            }
        }
        this.alertDefinitions = alertDefinitions;
        if (null != alertDefinitions) {
            for (org.apache.ambari.server.orm.entities.AlertDefinitionEntity definition : alertDefinitions) {
                definition.addAlertGroup(this);
            }
        }
    }

    public void addAlertDefinition(org.apache.ambari.server.orm.entities.AlertDefinitionEntity definition) {
        if (null == alertDefinitions) {
            alertDefinitions = new java.util.HashSet<>();
        }
        alertDefinitions.add(definition);
        definition.addAlertGroup(this);
    }

    public void removeAlertDefinition(org.apache.ambari.server.orm.entities.AlertDefinitionEntity definition) {
        if (null != alertDefinitions) {
            alertDefinitions.remove(definition);
        }
        definition.removeAlertGroup(this);
    }

    public java.util.Set<org.apache.ambari.server.orm.entities.AlertTargetEntity> getAlertTargets() {
        if (null == alertTargets) {
            return java.util.Collections.emptySet();
        }
        return java.util.Collections.unmodifiableSet(alertTargets);
    }

    public void addAlertTarget(org.apache.ambari.server.orm.entities.AlertTargetEntity alertTarget) {
        if (null == alertTargets) {
            alertTargets = new java.util.HashSet<>();
        }
        alertTargets.add(alertTarget);
        alertTarget.addAlertGroup(this);
    }

    public void removeAlertTarget(org.apache.ambari.server.orm.entities.AlertTargetEntity alertTarget) {
        if (null != alertTargets) {
            alertTargets.remove(alertTarget);
        }
        alertTarget.removeAlertGroup(this);
    }

    public void setAlertTargets(java.util.Set<org.apache.ambari.server.orm.entities.AlertTargetEntity> alertTargets) {
        if (null != this.alertTargets) {
            java.util.Set<org.apache.ambari.server.orm.entities.AlertTargetEntity> copyOfAssociatedTargets = new java.util.HashSet<>(this.alertTargets);
            for (org.apache.ambari.server.orm.entities.AlertTargetEntity target : copyOfAssociatedTargets) {
                target.removeAlertGroup(this);
            }
        }
        if (null != alertTargets) {
            for (org.apache.ambari.server.orm.entities.AlertTargetEntity target : alertTargets) {
                target.addAlertGroup(this);
            }
        }
        this.alertTargets = alertTargets;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object object) {
        if (this == object) {
            return true;
        }
        if ((object == null) || (getClass() != object.getClass())) {
            return false;
        }
        org.apache.ambari.server.orm.entities.AlertGroupEntity that = ((org.apache.ambari.server.orm.entities.AlertGroupEntity) (object));
        if (null != groupId) {
            return java.util.Objects.equals(groupId, that.groupId);
        }
        return ((java.util.Objects.equals(groupId, that.groupId) && java.util.Objects.equals(clusterId, that.clusterId)) && java.util.Objects.equals(groupName, that.groupName)) && java.util.Objects.equals(serviceName, that.serviceName);
    }

    @java.lang.Override
    public int hashCode() {
        if (null != groupId) {
            return groupId.hashCode();
        }
        return java.util.Objects.hash(groupId, clusterId, groupName, serviceName);
    }

    @java.lang.Override
    public java.lang.String toString() {
        java.lang.StringBuilder buffer = new java.lang.StringBuilder();
        buffer.append(getClass().getSimpleName());
        buffer.append("{");
        buffer.append("id=").append(groupId);
        buffer.append(", name=").append(groupName);
        buffer.append(", default=").append(isDefault);
        buffer.append("}");
        return buffer.toString();
    }
}