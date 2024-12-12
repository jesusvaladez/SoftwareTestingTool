package org.apache.ambari.server.orm.entities;
import javax.persistence.Column;
import javax.persistence.Id;
public class ViewInstanceDataEntityPK {
    @javax.persistence.Id
    @javax.persistence.Column(name = "view_instance_id", nullable = false, insertable = false, updatable = false)
    private java.lang.Long viewInstanceId;

    @javax.persistence.Id
    @javax.persistence.Column(name = "name", nullable = false, insertable = true, updatable = false, length = 100)
    private java.lang.String name;

    @javax.persistence.Id
    @javax.persistence.Column(name = "user_name", nullable = false, insertable = true, updatable = false, length = 100)
    private java.lang.String user;

    public java.lang.Long getViewInstanceId() {
        return viewInstanceId;
    }

    public void setViewInstanceId(java.lang.Long viewInstanceId) {
        this.viewInstanceId = viewInstanceId;
    }

    public java.lang.String getName() {
        return name;
    }

    public void setName(java.lang.String name) {
        this.name = name;
    }

    public java.lang.String getUser() {
        return user;
    }

    public void setUser(java.lang.String user) {
        this.user = user;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o)
            return true;

        if ((o == null) || (getClass() != o.getClass()))
            return false;

        org.apache.ambari.server.orm.entities.ViewInstanceDataEntityPK that = ((org.apache.ambari.server.orm.entities.ViewInstanceDataEntityPK) (o));
        return (name.equals(that.name) && user.equals(that.user)) && viewInstanceId.equals(that.viewInstanceId);
    }

    @java.lang.Override
    public int hashCode() {
        int result = viewInstanceId.hashCode();
        result = (31 * result) + name.hashCode();
        result = (31 * result) + user.hashCode();
        return result;
    }
}