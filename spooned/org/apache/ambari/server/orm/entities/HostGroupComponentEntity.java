package org.apache.ambari.server.orm.entities;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
@javax.persistence.IdClass(org.apache.ambari.server.orm.entities.HostGroupComponentEntityPK.class)
@javax.persistence.Table(name = "hostgroup_component")
@javax.persistence.Entity
public class HostGroupComponentEntity {
    @javax.persistence.Id
    @javax.persistence.Column(name = "hostgroup_name", nullable = false, insertable = false, updatable = false)
    private java.lang.String hostGroupName;

    @javax.persistence.Id
    @javax.persistence.Column(name = "blueprint_name", nullable = false, insertable = false, updatable = false)
    private java.lang.String blueprintName;

    @javax.persistence.Id
    @javax.persistence.Column(name = "name", nullable = false, insertable = true, updatable = false)
    private java.lang.String name;

    @javax.persistence.Column(name = "provision_action", nullable = true, insertable = true, updatable = false)
    private java.lang.String provisionAction;

    @javax.persistence.ManyToOne
    @javax.persistence.JoinColumns({ @javax.persistence.JoinColumn(name = "hostgroup_name", referencedColumnName = "name", nullable = false), @javax.persistence.JoinColumn(name = "blueprint_name", referencedColumnName = "blueprint_name", nullable = false) })
    private org.apache.ambari.server.orm.entities.HostGroupEntity hostGroup;

    public java.lang.String getName() {
        return name;
    }

    public void setName(java.lang.String name) {
        this.name = name;
    }

    public org.apache.ambari.server.orm.entities.HostGroupEntity getHostGroupEntity() {
        return hostGroup;
    }

    public void setHostGroupEntity(org.apache.ambari.server.orm.entities.HostGroupEntity entity) {
        this.hostGroup = entity;
    }

    public java.lang.String getHostGroupName() {
        return hostGroupName;
    }

    public void setHostGroupName(java.lang.String hostGroupName) {
        this.hostGroupName = hostGroupName;
    }

    public java.lang.String getBlueprintName() {
        return blueprintName;
    }

    public void setBlueprintName(java.lang.String blueprintName) {
        this.blueprintName = blueprintName;
    }

    public java.lang.String getProvisionAction() {
        return provisionAction;
    }

    public void setProvisionAction(java.lang.String provisionAction) {
        this.provisionAction = provisionAction;
    }
}