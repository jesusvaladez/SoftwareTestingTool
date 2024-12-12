package org.apache.ambari.server.state;
import org.codehaus.jackson.annotate.JsonProperty;
public class ChangedConfigInfo {
    private java.lang.String type;

    private java.lang.String name;

    @org.codehaus.jackson.annotate.JsonProperty("old_value")
    private java.lang.String oldValue;

    public ChangedConfigInfo() {
    }

    public ChangedConfigInfo(java.lang.String type, java.lang.String name) {
        this(type, name, null);
    }

    public ChangedConfigInfo(java.lang.String type, java.lang.String name, java.lang.String oldValue) {
        this.type = type;
        this.name = name;
        this.oldValue = oldValue;
    }

    public java.lang.String getType() {
        return type;
    }

    public void setType(java.lang.String type) {
        this.type = type;
    }

    public java.lang.String getName() {
        return name;
    }

    public java.lang.String getOldValue() {
        return oldValue;
    }

    public void setOldValue(java.lang.String oldValue) {
        this.oldValue = oldValue;
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

        org.apache.ambari.server.state.ChangedConfigInfo that = ((org.apache.ambari.server.state.ChangedConfigInfo) (o));
        if (type != null ? !type.equals(that.type) : that.type != null)
            return false;

        if (name != null ? !name.equals(that.name) : that.name != null)
            return false;

        return !(oldValue != null ? !oldValue.equals(that.oldValue) : that.oldValue != null);
    }

    @java.lang.Override
    public int hashCode() {
        int result = (type != null) ? type.hashCode() : 0;
        result = (31 * result) + (name != null ? name.hashCode() : 0);
        result = (31 * result) + (oldValue != null ? oldValue.hashCode() : 0);
        return result;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return ((((((((("ChangedConfigInfo{" + "type='") + type) + '\'') + ", name='") + name) + '\'') + ", oldValue='") + oldValue) + '\'') + '}';
    }
}