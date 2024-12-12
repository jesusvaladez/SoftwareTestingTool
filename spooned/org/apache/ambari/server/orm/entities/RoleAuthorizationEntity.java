package org.apache.ambari.server.orm.entities;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
@javax.persistence.Table(name = "roleauthorization")
@javax.persistence.Entity
@javax.persistence.NamedQueries({ @javax.persistence.NamedQuery(name = "findAll", query = "SELECT a FROM RoleAuthorizationEntity a") })
public class RoleAuthorizationEntity {
    @javax.persistence.Id
    @javax.persistence.Column(name = "authorization_id")
    private java.lang.String authorizationId;

    @javax.persistence.Column(name = "authorization_name")
    private java.lang.String authorizationName;

    public java.lang.String getAuthorizationId() {
        return authorizationId;
    }

    public void setAuthorizationId(java.lang.String authorizationId) {
        this.authorizationId = authorizationId;
    }

    public java.lang.String getAuthorizationName() {
        return authorizationName;
    }

    public void setAuthorizationName(java.lang.String authorizationName) {
        this.authorizationName = authorizationName;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if ((o == null) || (getClass() != o.getClass())) {
            return false;
        }
        org.apache.ambari.server.orm.entities.RoleAuthorizationEntity that = ((org.apache.ambari.server.orm.entities.RoleAuthorizationEntity) (o));
        return (!(authorizationId != null ? !authorizationId.equals(that.authorizationId) : that.authorizationId != null)) && (!(authorizationName != null ? !authorizationName.equals(that.authorizationName) : that.authorizationName != null));
    }

    @java.lang.Override
    public int hashCode() {
        int result = (authorizationId != null) ? authorizationId.hashCode() : 0;
        result = (31 * result) + (authorizationName != null ? authorizationName.hashCode() : 0);
        return result;
    }
}