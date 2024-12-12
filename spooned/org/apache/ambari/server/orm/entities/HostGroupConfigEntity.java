package org.apache.ambari.server.orm.entities;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
@javax.persistence.IdClass(org.apache.ambari.server.orm.entities.HostGroupConfigEntityPK.class)
@javax.persistence.Table(name = "hostgroup_configuration")
@javax.persistence.Entity
public class HostGroupConfigEntity implements org.apache.ambari.server.orm.entities.BlueprintConfiguration {
    @javax.persistence.Id
    @javax.persistence.Column(name = "blueprint_name", nullable = false, insertable = false, updatable = false)
    private java.lang.String blueprintName;

    @javax.persistence.Id
    @javax.persistence.Column(name = "hostgroup_name", nullable = false, insertable = false, updatable = false)
    private java.lang.String hostGroupName;

    @javax.persistence.Id
    @javax.persistence.Column(name = "type_name", nullable = false, insertable = true, updatable = false)
    private java.lang.String type;

    @javax.persistence.Column(name = "config_data", nullable = false, insertable = true, updatable = false)
    @javax.persistence.Basic(fetch = javax.persistence.FetchType.LAZY)
    @javax.persistence.Lob
    private java.lang.String configData;

    @javax.persistence.Column(name = "config_attributes", nullable = true, insertable = true, updatable = false)
    @javax.persistence.Basic(fetch = javax.persistence.FetchType.LAZY)
    @javax.persistence.Lob
    private java.lang.String configAttributes;

    @javax.persistence.ManyToOne
    @javax.persistence.JoinColumns({ @javax.persistence.JoinColumn(name = "hostgroup_name", referencedColumnName = "name", nullable = false), @javax.persistence.JoinColumn(name = "blueprint_name", referencedColumnName = "blueprint_name", nullable = false) })
    private org.apache.ambari.server.orm.entities.HostGroupEntity hostGroup;

    @java.lang.Override
    public java.lang.String getType() {
        return type;
    }

    @java.lang.Override
    public void setType(java.lang.String type) {
        this.type = type;
    }

    public org.apache.ambari.server.orm.entities.HostGroupEntity getHostGroupEntity() {
        return hostGroup;
    }

    public void setHostGroupEntity(org.apache.ambari.server.orm.entities.HostGroupEntity entity) {
        this.hostGroup = entity;
    }

    @java.lang.Override
    public java.lang.String getBlueprintName() {
        return blueprintName;
    }

    @java.lang.Override
    public void setBlueprintName(java.lang.String blueprintName) {
        this.blueprintName = blueprintName;
    }

    public java.lang.String getHostGroupName() {
        return hostGroupName;
    }

    public void setHostGroupName(java.lang.String hostGroupName) {
        this.hostGroupName = hostGroupName;
    }

    @java.lang.Override
    public java.lang.String getConfigData() {
        return configData;
    }

    @java.lang.Override
    public void setConfigData(java.lang.String configData) {
        this.configData = configData;
    }

    @java.lang.Override
    public java.lang.String getConfigAttributes() {
        return configAttributes;
    }

    @java.lang.Override
    public void setConfigAttributes(java.lang.String configAttributes) {
        this.configAttributes = configAttributes;
    }
}