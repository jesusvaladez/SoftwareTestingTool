package org.apache.ambari.server.orm.entities;
import javax.persistence.Column;
import javax.persistence.Id;
public class BlueprintConfigEntityPK {
    @javax.persistence.Id
    @javax.persistence.Column(name = "blueprint_name", nullable = false, insertable = true, updatable = false, length = 100)
    private java.lang.String blueprintName;

    @javax.persistence.Id
    @javax.persistence.Column(name = "type_name", nullable = false, insertable = true, updatable = false, length = 100)
    private java.lang.String type;

    public java.lang.String getBlueprintName() {
        return blueprintName;
    }

    public void setBlueprintName(java.lang.String blueprintName) {
        this.blueprintName = blueprintName;
    }

    public java.lang.String getType() {
        return type;
    }

    public void setType(java.lang.String type) {
        this.type = type;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o)
            return true;

        if ((o == null) || (getClass() != o.getClass()))
            return false;

        org.apache.ambari.server.orm.entities.BlueprintConfigEntityPK that = ((org.apache.ambari.server.orm.entities.BlueprintConfigEntityPK) (o));
        return this.blueprintName.equals(that.blueprintName) && this.type.equals(that.type);
    }

    @java.lang.Override
    public int hashCode() {
        return (31 * blueprintName.hashCode()) + type.hashCode();
    }
}