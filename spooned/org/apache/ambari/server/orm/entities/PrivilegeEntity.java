package org.apache.ambari.server.orm.entities;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
@javax.persistence.Table(name = "adminprivilege")
@javax.persistence.Entity
@javax.persistence.TableGenerator(name = "privilege_id_generator", table = "ambari_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_value", pkColumnValue = "privilege_id_seq", initialValue = 1)
public class PrivilegeEntity {
    @javax.persistence.Id
    @javax.persistence.Column(name = "privilege_id")
    @javax.persistence.GeneratedValue(strategy = javax.persistence.GenerationType.TABLE, generator = "privilege_id_generator")
    private java.lang.Integer id;

    @javax.persistence.ManyToOne
    @javax.persistence.JoinColumns({ @javax.persistence.JoinColumn(name = "permission_id", referencedColumnName = "permission_id", nullable = false) })
    private org.apache.ambari.server.orm.entities.PermissionEntity permission;

    @javax.persistence.ManyToOne
    @javax.persistence.JoinColumns({ @javax.persistence.JoinColumn(name = "resource_id", referencedColumnName = "resource_id", nullable = false) })
    private org.apache.ambari.server.orm.entities.ResourceEntity resource;

    @javax.persistence.ManyToOne
    @javax.persistence.JoinColumns({ @javax.persistence.JoinColumn(name = "principal_id", referencedColumnName = "principal_id", nullable = false) })
    private org.apache.ambari.server.orm.entities.PrincipalEntity principal;

    public java.lang.Integer getId() {
        return id;
    }

    public void setId(java.lang.Integer id) {
        this.id = id;
    }

    public org.apache.ambari.server.orm.entities.PermissionEntity getPermission() {
        return permission;
    }

    public void setPermission(org.apache.ambari.server.orm.entities.PermissionEntity permission) {
        this.permission = permission;
    }

    public org.apache.ambari.server.orm.entities.ResourceEntity getResource() {
        return resource;
    }

    public void setResource(org.apache.ambari.server.orm.entities.ResourceEntity resource) {
        this.resource = resource;
    }

    public org.apache.ambari.server.orm.entities.PrincipalEntity getPrincipal() {
        return principal;
    }

    public void setPrincipal(org.apache.ambari.server.orm.entities.PrincipalEntity principal) {
        this.principal = principal;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o)
            return true;

        if ((o == null) || (getClass() != o.getClass()))
            return false;

        org.apache.ambari.server.orm.entities.PrivilegeEntity that = ((org.apache.ambari.server.orm.entities.PrivilegeEntity) (o));
        return ((java.util.Objects.equals(id, that.id) && java.util.Objects.equals(permission, that.permission)) && java.util.Objects.equals(principal, that.principal)) && java.util.Objects.equals(resource, that.resource);
    }

    @java.lang.Override
    public int hashCode() {
        return java.util.Objects.hash(id, permission, resource, principal);
    }

    @java.lang.Override
    public java.lang.String toString() {
        return (((((((("PrivilegeEntity{" + "id=") + id) + ", permission=") + permission) + ", resource=") + resource) + ", principal=") + principal) + '}';
    }
}