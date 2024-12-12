package org.apache.ambari.server.orm.entities;
import javax.persistence.Column;
import javax.persistence.Id;
public class HostGroupEntityPK {
    @javax.persistence.Id
    @javax.persistence.Column(name = "blueprint_name", nullable = false, insertable = true, updatable = false, length = 100)
    private java.lang.String blueprintName;

    @javax.persistence.Id
    @javax.persistence.Column(name = "name", nullable = false, insertable = true, updatable = false, length = 100)
    private java.lang.String name;

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

        org.apache.ambari.server.orm.entities.HostGroupEntityPK that = ((org.apache.ambari.server.orm.entities.HostGroupEntityPK) (o));
        return this.blueprintName.equals(that.blueprintName) && this.name.equals(that.name);
    }

    @java.lang.Override
    public int hashCode() {
        return (31 * blueprintName.hashCode()) + name.hashCode();
    }
}