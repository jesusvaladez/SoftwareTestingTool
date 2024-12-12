package org.apache.ambari.server.orm.entities;
import javax.persistence.Basic;
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
@javax.persistence.Table(name = "upgrade_item")
@javax.persistence.Entity
@javax.persistence.TableGenerator(name = "upgrade_item_id_generator", table = "ambari_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_value", pkColumnValue = "upgrade_item_id_seq", initialValue = 0, allocationSize = 1000)
@javax.persistence.NamedQueries({ @javax.persistence.NamedQuery(name = "UpgradeItemEntity.findAllStageIds", query = "SELECT upgradeItem.stageId FROM UpgradeItemEntity upgradeItem") })
public class UpgradeItemEntity {
    @javax.persistence.Id
    @javax.persistence.Column(name = "upgrade_item_id", nullable = false, insertable = true, updatable = false)
    @javax.persistence.GeneratedValue(strategy = javax.persistence.GenerationType.TABLE, generator = "upgrade_item_id_generator")
    private java.lang.Long upgradeItemId;

    @javax.persistence.Column(name = "upgrade_group_id", nullable = false, insertable = false, updatable = false)
    private java.lang.Long upgradeGroupId;

    @javax.persistence.Enumerated(javax.persistence.EnumType.STRING)
    @javax.persistence.Column(name = "state", length = 255, nullable = false)
    private org.apache.ambari.server.state.UpgradeState state = org.apache.ambari.server.state.UpgradeState.NONE;

    @javax.persistence.Basic
    @javax.persistence.Column(name = "hosts")
    private java.lang.String hosts = null;

    @javax.persistence.Basic
    @javax.persistence.Column(name = "tasks", length = 4096)
    private java.lang.String tasks = null;

    @javax.persistence.Basic
    @javax.persistence.Column(name = "item_text")
    private java.lang.String itemText = null;

    @javax.persistence.Basic
    @javax.persistence.Column(name = "stage_id", nullable = false)
    private java.lang.Long stageId = java.lang.Long.valueOf(0L);

    @javax.persistence.ManyToOne
    @javax.persistence.JoinColumn(name = "upgrade_group_id", referencedColumnName = "upgrade_group_id", nullable = false)
    private org.apache.ambari.server.orm.entities.UpgradeGroupEntity upgradeGroupEntity;

    public java.lang.Long getId() {
        return upgradeItemId;
    }

    public void setId(java.lang.Long id) {
        upgradeItemId = id;
    }

    public org.apache.ambari.server.state.UpgradeState getState() {
        return state;
    }

    public void setState(org.apache.ambari.server.state.UpgradeState state) {
        this.state = state;
    }

    public java.lang.String getTasks() {
        return tasks;
    }

    public void setTasks(java.lang.String json) {
        tasks = json;
    }

    public java.lang.String getHosts() {
        return hosts;
    }

    public void setHosts(java.lang.String json) {
        hosts = json;
    }

    public java.lang.String getText() {
        return itemText;
    }

    public void setText(java.lang.String text) {
        itemText = text;
    }

    public org.apache.ambari.server.orm.entities.UpgradeGroupEntity getGroupEntity() {
        return upgradeGroupEntity;
    }

    public void setGroupEntity(org.apache.ambari.server.orm.entities.UpgradeGroupEntity entity) {
        upgradeGroupEntity = entity;
    }

    public java.lang.Long getStageId() {
        return stageId;
    }

    public void setStageId(java.lang.Long id) {
        stageId = id;
    }
}