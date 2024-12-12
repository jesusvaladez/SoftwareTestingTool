package org.apache.ambari.server.state;
public class ModuleComponent {
    @com.google.gson.annotations.SerializedName("id")
    private java.lang.String id;

    @com.google.gson.annotations.SerializedName("name")
    private java.lang.String name;

    @com.google.gson.annotations.SerializedName("category")
    private java.lang.String category;

    @com.google.gson.annotations.SerializedName("isExternal")
    private java.lang.Boolean isExternal;

    @com.google.gson.annotations.SerializedName("version")
    private java.lang.String version;

    @com.fasterxml.jackson.annotation.JsonIgnore
    private transient org.apache.ambari.server.state.Module module;

    public java.lang.String getId() {
        return id;
    }

    public void setId(java.lang.String id) {
        this.id = id;
    }

    public java.lang.String getCategory() {
        return category;
    }

    public void setCategory(java.lang.String category) {
        this.category = category;
    }

    public java.lang.String getVersion() {
        return version;
    }

    public void setVersion(java.lang.String version) {
        this.version = version;
    }

    public java.lang.String getName() {
        return name;
    }

    public void setName(java.lang.String name) {
        this.name = name;
    }

    public java.lang.Boolean getIsExternal() {
        return isExternal;
    }

    public void setIsExternal(java.lang.Boolean isExternal) {
        this.isExternal = isExternal;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o)
            return true;

        if ((o == null) || (getClass() != o.getClass()))
            return false;

        org.apache.ambari.server.state.ModuleComponent that = ((org.apache.ambari.server.state.ModuleComponent) (o));
        if (id != null ? !id.equals(that.id) : that.id != null)
            return false;

        if (name != null ? !name.equals(that.name) : that.name != null)
            return false;

        if (category != null ? !category.equals(that.category) : that.category != null)
            return false;

        if (isExternal != null ? !isExternal.equals(that.isExternal) : that.isExternal != null)
            return false;

        return version != null ? version.equals(that.version) : that.version == null;
    }

    @java.lang.Override
    public int hashCode() {
        int result = (id != null) ? id.hashCode() : 0;
        result = (31 * result) + (name != null ? name.hashCode() : 0);
        result = (31 * result) + (category != null ? category.hashCode() : 0);
        result = (31 * result) + (isExternal != null ? isExternal.hashCode() : 0);
        result = (31 * result) + (version != null ? version.hashCode() : 0);
        return result;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return (((((((((((((("ModuleComponent{" + "id='") + id) + '\'') + ", name='") + name) + '\'') + ", category='") + category) + '\'') + ", isExternal=") + isExternal) + ", version='") + version) + '\'') + '}';
    }

    public void setModule(org.apache.ambari.server.state.Module module) {
        this.module = module;
    }
}