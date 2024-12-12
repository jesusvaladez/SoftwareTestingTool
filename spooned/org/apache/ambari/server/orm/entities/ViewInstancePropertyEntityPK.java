package org.apache.ambari.server.orm.entities;
import javax.persistence.Column;
import javax.persistence.Id;
public class ViewInstancePropertyEntityPK {
    @javax.persistence.Id
    @javax.persistence.Column(name = "view_name", nullable = false, insertable = true, updatable = false, length = 100)
    private java.lang.String viewName;

    @javax.persistence.Id
    @javax.persistence.Column(name = "view_instance_name", nullable = false, insertable = true, updatable = false, length = 100)
    private java.lang.String viewInstanceName;

    @javax.persistence.Id
    @javax.persistence.Column(name = "name", nullable = false, insertable = true, updatable = false, length = 100)
    private java.lang.String name;

    public java.lang.String getViewName() {
        return viewName;
    }

    public void setViewName(java.lang.String viewName) {
        this.viewName = viewName;
    }

    public java.lang.String getViewInstanceName() {
        return viewInstanceName;
    }

    public void setViewInstanceName(java.lang.String viewInstanceName) {
        this.viewInstanceName = viewInstanceName;
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

        org.apache.ambari.server.orm.entities.ViewInstancePropertyEntityPK that = ((org.apache.ambari.server.orm.entities.ViewInstancePropertyEntityPK) (o));
        return (this.viewName.equals(that.viewName) && this.viewInstanceName.equals(that.viewInstanceName)) && this.name.equals(that.name);
    }

    @java.lang.Override
    public int hashCode() {
        return ((31 * viewName.hashCode()) + viewInstanceName.hashCode()) + name.hashCode();
    }
}