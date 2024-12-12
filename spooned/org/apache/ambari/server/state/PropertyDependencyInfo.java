package org.apache.ambari.server.state;
public class PropertyDependencyInfo {
    private java.lang.String type;

    private java.lang.String name;

    public PropertyDependencyInfo() {
    }

    public PropertyDependencyInfo(java.lang.String type, java.lang.String name) {
        this.type = type;
        this.name = name;
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

    public void setName(java.lang.String name) {
        this.name = name;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o)
            return true;

        if ((o == null) || (getClass() != o.getClass()))
            return false;

        org.apache.ambari.server.state.PropertyDependencyInfo that = ((org.apache.ambari.server.state.PropertyDependencyInfo) (o));
        if (name != null ? !name.equals(that.name) : that.name != null)
            return false;

        if (type != null ? !type.equals(that.type) : that.type != null)
            return false;

        return true;
    }

    @java.lang.Override
    public int hashCode() {
        int result = (type != null) ? type.hashCode() : 0;
        result = (31 * result) + (name != null ? name.hashCode() : 0);
        return result;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return (((((("PropertyDependencyInfo{" + "type='") + type) + '\'') + ", name='") + name) + '\'') + '}';
    }

    public org.apache.ambari.server.controller.StackConfigurationDependencyResponse convertToResponse() {
        return new org.apache.ambari.server.controller.StackConfigurationDependencyResponse(getName(), getType());
    }
}