package org.apache.ambari.server.orm.entities;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
@javax.persistence.IdClass(org.apache.ambari.server.orm.entities.HostGroupEntityPK.class)
@javax.persistence.Table(name = "hostgroup")
@javax.persistence.Entity
public class HostGroupEntity {
    @javax.persistence.Id
    @javax.persistence.Column(name = "blueprint_name", nullable = false, insertable = false, updatable = false)
    private java.lang.String blueprintName;

    @javax.persistence.Id
    @javax.persistence.Column(name = "name", nullable = false, insertable = true, updatable = false)
    private java.lang.String name;

    @javax.persistence.Column
    @javax.persistence.Basic
    private java.lang.String cardinality = "NOT SPECIFIED";

    @javax.persistence.OneToMany(cascade = javax.persistence.CascadeType.ALL, mappedBy = "hostGroup")
    private java.util.Collection<org.apache.ambari.server.orm.entities.HostGroupComponentEntity> components;

    @javax.persistence.OneToMany(cascade = javax.persistence.CascadeType.ALL, mappedBy = "hostGroup")
    private java.util.Collection<org.apache.ambari.server.orm.entities.HostGroupConfigEntity> configurations;

    @javax.persistence.ManyToOne
    @javax.persistence.JoinColumn(name = "blueprint_name", referencedColumnName = "blueprint_name", nullable = false)
    private org.apache.ambari.server.orm.entities.BlueprintEntity blueprint;

    public java.lang.String getName() {
        return name;
    }

    public void setName(java.lang.String name) {
        this.name = name;
    }

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

    public java.util.Collection<org.apache.ambari.server.orm.entities.HostGroupComponentEntity> getComponents() {
        return components;
    }

    public void setComponents(java.util.Collection<org.apache.ambari.server.orm.entities.HostGroupComponentEntity> components) {
        this.components = components;
    }

    public void addComponent(org.apache.ambari.server.orm.entities.HostGroupComponentEntity component) {
        this.components.add(component);
    }

    public java.util.Collection<org.apache.ambari.server.orm.entities.HostGroupConfigEntity> getConfigurations() {
        return configurations;
    }

    public void setConfigurations(java.util.Collection<org.apache.ambari.server.orm.entities.HostGroupConfigEntity> configurations) {
        this.configurations = configurations;
    }

    public java.lang.String getCardinality() {
        return cardinality;
    }

    public void setCardinality(java.lang.String cardinality) {
        if (cardinality != null) {
            this.cardinality = cardinality;
        }
    }
}