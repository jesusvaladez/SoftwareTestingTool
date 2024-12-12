package org.apache.ambari.server.orm.entities;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
@javax.persistence.Table(name = "blueprint")
@javax.persistence.NamedQuery(name = "allBlueprints", query = "SELECT blueprint FROM BlueprintEntity blueprint")
@javax.persistence.Entity
public class BlueprintEntity {
    @javax.persistence.Id
    @javax.persistence.Column(name = "blueprint_name", nullable = false, insertable = true, updatable = false, unique = true, length = 100)
    private java.lang.String blueprintName;

    @javax.persistence.Basic
    @javax.persistence.Enumerated(javax.persistence.EnumType.STRING)
    @javax.persistence.Column(name = "security_type", nullable = false, insertable = true, updatable = true)
    private org.apache.ambari.server.state.SecurityType securityType = org.apache.ambari.server.state.SecurityType.NONE;

    @javax.persistence.Basic
    @javax.persistence.Column(name = "security_descriptor_reference", nullable = true, insertable = true, updatable = true)
    private java.lang.String securityDescriptorReference;

    @javax.persistence.OneToOne
    @javax.persistence.JoinColumn(name = "stack_id", unique = false, nullable = false, insertable = true, updatable = false)
    private org.apache.ambari.server.orm.entities.StackEntity stack;

    @javax.persistence.OneToMany(cascade = javax.persistence.CascadeType.ALL, mappedBy = "blueprint")
    private java.util.Collection<org.apache.ambari.server.orm.entities.HostGroupEntity> hostGroups;

    @javax.persistence.OneToMany(cascade = javax.persistence.CascadeType.ALL, mappedBy = "blueprint")
    private java.util.Collection<org.apache.ambari.server.orm.entities.BlueprintConfigEntity> configurations;

    @javax.persistence.OneToMany(cascade = javax.persistence.CascadeType.ALL, mappedBy = "blueprint")
    private java.util.Collection<org.apache.ambari.server.orm.entities.BlueprintSettingEntity> settings;

    public java.lang.String getBlueprintName() {
        return blueprintName;
    }

    public void setBlueprintName(java.lang.String blueprintName) {
        this.blueprintName = blueprintName;
    }

    public org.apache.ambari.server.orm.entities.StackEntity getStack() {
        return stack;
    }

    public void setStack(org.apache.ambari.server.orm.entities.StackEntity stack) {
        this.stack = stack;
    }

    public java.util.Collection<org.apache.ambari.server.orm.entities.HostGroupEntity> getHostGroups() {
        return hostGroups;
    }

    public void setHostGroups(java.util.Collection<org.apache.ambari.server.orm.entities.HostGroupEntity> hostGroups) {
        this.hostGroups = hostGroups;
    }

    public java.util.Collection<org.apache.ambari.server.orm.entities.BlueprintConfigEntity> getConfigurations() {
        return configurations;
    }

    public void setConfigurations(java.util.Collection<org.apache.ambari.server.orm.entities.BlueprintConfigEntity> configurations) {
        this.configurations = configurations;
    }

    public java.util.Collection<org.apache.ambari.server.orm.entities.BlueprintSettingEntity> getSettings() {
        return settings;
    }

    public void setSettings(java.util.Collection<org.apache.ambari.server.orm.entities.BlueprintSettingEntity> settings) {
        this.settings = settings;
    }

    public org.apache.ambari.server.state.SecurityType getSecurityType() {
        return securityType;
    }

    public void setSecurityType(org.apache.ambari.server.state.SecurityType securityType) {
        this.securityType = securityType;
    }

    public java.lang.String getSecurityDescriptorReference() {
        return securityDescriptorReference;
    }

    public void setSecurityDescriptorReference(java.lang.String securityDescriptorReference) {
        this.securityDescriptorReference = securityDescriptorReference;
    }
}