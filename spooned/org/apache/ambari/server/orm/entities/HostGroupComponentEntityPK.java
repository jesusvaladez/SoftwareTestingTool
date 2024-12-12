package org.apache.ambari.server.orm.entities;
import javax.persistence.Column;
import javax.persistence.Id;
public class HostGroupComponentEntityPK implements java.io.Serializable {
    @javax.persistence.Id
    @javax.persistence.Column(name = "hostgroup_name", nullable = false, insertable = true, updatable = false, length = 100)
    private java.lang.String hostGroupName;

    @javax.persistence.Id
    @javax.persistence.Column(name = "blueprint_name", nullable = false, insertable = true, updatable = false, length = 100)
    private java.lang.String blueprintName;

    @javax.persistence.Id
    @javax.persistence.Column(name = "name", nullable = false, insertable = true, updatable = false, length = 100)
    private java.lang.String name;

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

    public java.lang.String getName() {
        return name;
    }

    public void setName(java.lang.String name) {
        this.name = name;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o)
            return true;

        if ((o == null) || (getClass() != o.getClass()))
            return false;

        org.apache.ambari.server.orm.entities.HostGroupComponentEntityPK that = ((org.apache.ambari.server.orm.entities.HostGroupComponentEntityPK) (o));
        return (this.hostGroupName.equals(that.hostGroupName) && this.name.equals(that.name)) && this.blueprintName.equals(that.blueprintName);
    }

    @java.lang.Override
    public int hashCode() {
        int result = hostGroupName.hashCode();
        result = (31 * result) + blueprintName.hashCode();
        result = (31 * result) + name.hashCode();
        return result;
    }
}