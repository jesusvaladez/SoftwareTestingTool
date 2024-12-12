package org.apache.ambari.server.orm.entities;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
@javax.persistence.Table(name = "upgrade_group")
@javax.persistence.Entity
@javax.persistence.TableGenerator(name = "upgrade_group_id_generator", table = "ambari_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_value", pkColumnValue = "upgrade_group_id_seq", initialValue = 0, allocationSize = 200)
public class UpgradeGroupEntity {
    @javax.persistence.Id
    @javax.persistence.Column(name = "upgrade_group_id", nullable = false, insertable = true, updatable = false)
    @javax.persistence.GeneratedValue(strategy = javax.persistence.GenerationType.TABLE, generator = "upgrade_group_id_generator")
    private java.lang.Long upgradeGroupId;

    @javax.persistence.Column(name = "upgrade_id", nullable = false, insertable = false, updatable = false)
    private java.lang.Long upgradeId;

    @javax.persistence.Basic
    @javax.persistence.Column(name = "group_name", length = 255, nullable = false)
    private java.lang.String groupName;

    @javax.persistence.Basic
    @javax.persistence.Column(name = "group_title", length = 1024, nullable = false)
    private java.lang.String groupTitle;

    @javax.persistence.ManyToOne
    @javax.persistence.JoinColumn(name = "upgrade_id", referencedColumnName = "upgrade_id", nullable = false)
    private org.apache.ambari.server.orm.entities.UpgradeEntity upgradeEntity;

    @javax.persistence.OneToMany(mappedBy = "upgradeGroupEntity", cascade = { javax.persistence.CascadeType.ALL })
    private java.util.List<org.apache.ambari.server.orm.entities.UpgradeItemEntity> upgradeItems;

    public java.lang.Long getId() {
        return upgradeGroupId;
    }

    public void setId(java.lang.Long id) {
        upgradeGroupId = id;
    }

    public java.lang.String getName() {
        return groupName;
    }

    public void setName(java.lang.String name) {
        groupName = name;
    }

    public java.lang.String getTitle() {
        return groupTitle;
    }

    public void setTitle(java.lang.String title) {
        groupTitle = title;
    }

    public org.apache.ambari.server.orm.entities.UpgradeEntity getUpgradeEntity() {
        return upgradeEntity;
    }

    public void setUpgradeEntity(org.apache.ambari.server.orm.entities.UpgradeEntity entity) {
        upgradeEntity = entity;
    }

    public java.util.List<org.apache.ambari.server.orm.entities.UpgradeItemEntity> getItems() {
        return upgradeItems;
    }

    public void setItems(java.util.List<org.apache.ambari.server.orm.entities.UpgradeItemEntity> items) {
        for (org.apache.ambari.server.orm.entities.UpgradeItemEntity item : items) {
            item.setGroupEntity(this);
        }
        upgradeItems = items;
    }

    @java.lang.Override
    public java.lang.String toString() {
        java.lang.StringBuilder buffer = new java.lang.StringBuilder("UpgradeGroupEntity{");
        buffer.append("upgradeGroupId=").append(upgradeGroupId);
        buffer.append(", upgradeId=").append(upgradeId);
        buffer.append(", groupName=").append(groupName);
        buffer.append(", groupTitle=").append(groupTitle);
        buffer.append("}");
        return buffer.toString();
    }
}