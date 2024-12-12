package org.apache.ambari.server.orm.entities;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.UniqueConstraint;
@javax.persistence.Table(name = "blueprint_setting", uniqueConstraints = @javax.persistence.UniqueConstraint(name = "UQ_blueprint_setting_name", columnNames = { "blueprint_name", "setting_name" }))
@javax.persistence.TableGenerator(name = "blueprint_setting_id_generator", table = "ambari_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_value", pkColumnValue = "blueprint_setting_id_seq", initialValue = 0)
@javax.persistence.Entity
public class BlueprintSettingEntity {
    @javax.persistence.Id
    @javax.persistence.Column(name = "id", nullable = false, insertable = true, updatable = false)
    @javax.persistence.GeneratedValue(strategy = javax.persistence.GenerationType.TABLE, generator = "blueprint_setting_id_generator")
    private long id;

    @javax.persistence.Column(name = "blueprint_name", nullable = false, insertable = false, updatable = false)
    private java.lang.String blueprintName;

    @javax.persistence.Column(name = "setting_name", nullable = false, insertable = true, updatable = false)
    private java.lang.String settingName;

    @javax.persistence.Column(name = "setting_data", nullable = false, insertable = true, updatable = false)
    @javax.persistence.Basic(fetch = javax.persistence.FetchType.LAZY)
    @javax.persistence.Lob
    private java.lang.String settingData;

    @javax.persistence.ManyToOne
    @javax.persistence.JoinColumn(name = "blueprint_name", referencedColumnName = "blueprint_name", nullable = false)
    private org.apache.ambari.server.orm.entities.BlueprintEntity blueprint;

    public org.apache.ambari.server.orm.entities.BlueprintEntity getBlueprintEntity() {
        return blueprint;
    }

    public void setBlueprintEntity(org.apache.ambari.server.orm.entities.BlueprintEntity entity) {
        this.blueprint = entity;
    }

    public java.lang.String getBlueprintName() {
        return blueprintName;
    }

    public void setBlueprintName(java.lang.String blueprintName) {
        this.blueprintName = blueprintName;
    }

    public java.lang.String getSettingName() {
        return settingName;
    }

    public void setSettingName(java.lang.String settingName) {
        this.settingName = settingName;
    }

    public java.lang.String getSettingData() {
        return settingData;
    }

    public void setSettingData(java.lang.String settingData) {
        this.settingData = settingData;
    }
}