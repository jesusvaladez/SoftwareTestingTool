package org.apache.ambari.server.orm.entities;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
@javax.persistence.Table(name = "setting")
@javax.persistence.TableGenerator(name = "setting_id_generator", table = "ambari_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_value", pkColumnValue = "setting_id_seq", initialValue = 0)
@javax.persistence.NamedQuery(name = "settingByName", query = "SELECT setting FROM SettingEntity setting WHERE setting.name=:name")
@javax.persistence.Entity
public class SettingEntity {
    @javax.persistence.Id
    @javax.persistence.Column(name = "id", nullable = false, insertable = true, updatable = false)
    @javax.persistence.GeneratedValue(strategy = javax.persistence.GenerationType.TABLE, generator = "setting_id_generator")
    private long id;

    @javax.persistence.Column(name = "name", nullable = false, insertable = true, updatable = false, unique = true)
    @javax.persistence.Basic
    private java.lang.String name;

    @javax.persistence.Column(name = "setting_type", nullable = false, insertable = true, updatable = true)
    @javax.persistence.Basic
    private java.lang.String settingType;

    @javax.persistence.Column(name = "content", nullable = false, insertable = true, updatable = true)
    @javax.persistence.Lob
    @javax.persistence.Basic
    private java.lang.String content;

    @javax.persistence.Column(name = "updated_by", nullable = false, insertable = true, updatable = true)
    @javax.persistence.Basic
    private java.lang.String updatedBy;

    @javax.persistence.Column(name = "update_timestamp", nullable = false, insertable = true, updatable = true)
    @javax.persistence.Basic
    private long updateTimestamp;

    public SettingEntity() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public java.lang.String getName() {
        return name;
    }

    public void setName(java.lang.String name) {
        this.name = name;
    }

    public java.lang.String getSettingType() {
        return settingType;
    }

    public void setSettingType(java.lang.String settingType) {
        this.settingType = settingType;
    }

    public java.lang.String getContent() {
        return content;
    }

    public void setContent(java.lang.String content) {
        this.content = content;
    }

    public java.lang.String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(java.lang.String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public long getUpdateTimestamp() {
        return updateTimestamp;
    }

    public void setUpdateTimestamp(long updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }

    @java.lang.Override
    public org.apache.ambari.server.orm.entities.SettingEntity clone() {
        org.apache.ambari.server.orm.entities.SettingEntity cloned = new org.apache.ambari.server.orm.entities.SettingEntity();
        cloned.setId(id);
        cloned.setName(name);
        cloned.setContent(content);
        cloned.setSettingType(settingType);
        cloned.setUpdatedBy(updatedBy);
        cloned.setUpdateTimestamp(updateTimestamp);
        return cloned;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o)
            return true;

        if ((o == null) || (getClass() != o.getClass()))
            return false;

        org.apache.ambari.server.orm.entities.SettingEntity entity = ((org.apache.ambari.server.orm.entities.SettingEntity) (o));
        return (((((id == entity.id) && java.util.Objects.equals(name, entity.name)) && java.util.Objects.equals(settingType, entity.settingType)) && java.util.Objects.equals(content, entity.content)) && java.util.Objects.equals(updatedBy, entity.updatedBy)) && (updateTimestamp == entity.updateTimestamp);
    }

    @java.lang.Override
    public int hashCode() {
        return java.util.Objects.hash(id, name, settingType, content, updatedBy, updateTimestamp);
    }
}