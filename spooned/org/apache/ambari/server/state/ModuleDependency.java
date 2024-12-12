package org.apache.ambari.server.state;
public class ModuleDependency {
    @com.google.gson.annotations.SerializedName("id")
    private java.lang.String id;

    @com.google.gson.annotations.SerializedName("name")
    private java.lang.String name;

    public enum DependencyType {

        @com.google.gson.annotations.SerializedName("RUNTIME")
        RUNTIME,
        @com.google.gson.annotations.SerializedName("INSTALL")
        INSTALL;}

    @com.google.gson.annotations.SerializedName("dependencyType")
    private org.apache.ambari.server.state.ModuleDependency.DependencyType dependencyType;

    public java.lang.String getName() {
        return name;
    }

    public void setName(java.lang.String name) {
        this.name = name;
    }

    public java.lang.String getId() {
        return id;
    }

    public void setId(java.lang.String id) {
        this.id = id;
    }

    public org.apache.ambari.server.state.ModuleDependency.DependencyType getDependencyType() {
        return dependencyType;
    }

    public void setDependencyType(org.apache.ambari.server.state.ModuleDependency.DependencyType dependencyType) {
        this.dependencyType = dependencyType;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o)
            return true;

        if ((o == null) || (getClass() != o.getClass()))
            return false;

        org.apache.ambari.server.state.ModuleDependency that = ((org.apache.ambari.server.state.ModuleDependency) (o));
        if (id != null ? !id.equals(that.id) : that.id != null)
            return false;

        if (name != null ? !name.equals(that.name) : that.name != null)
            return false;

        return dependencyType != null ? dependencyType.equals(that.dependencyType) : that.dependencyType == null;
    }

    @java.lang.Override
    public int hashCode() {
        int result = (id != null) ? id.hashCode() : 0;
        result = (31 * result) + (name != null ? name.hashCode() : 0);
        result = (31 * result) + (dependencyType != null ? dependencyType.hashCode() : 0);
        return result;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return ((((((((("ModuleDependency{" + "id='") + id) + '\'') + ", name='") + name) + '\'') + ", dependencyType='") + dependencyType) + '\'') + '}';
    }
}