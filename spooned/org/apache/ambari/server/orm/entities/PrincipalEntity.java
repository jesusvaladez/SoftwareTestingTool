package org.apache.ambari.server.orm.entities;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
@javax.persistence.Table(name = "adminprincipal")
@javax.persistence.Entity
@javax.persistence.TableGenerator(name = "principal_id_generator", table = "ambari_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_value", pkColumnValue = "principal_id_seq", initialValue = 100, allocationSize = 500)
@javax.persistence.NamedQueries({ @javax.persistence.NamedQuery(name = "principalByPrivilegeId", query = "SELECT principal FROM PrincipalEntity principal JOIN principal.privileges privilege WHERE privilege.permission.id=:permission_id"), @javax.persistence.NamedQuery(name = "principalByPrincipalType", query = "SELECT principal FROM PrincipalEntity principal WHERE principal.principalType.name = :principal_type") })
public class PrincipalEntity {
    @javax.persistence.Id
    @javax.persistence.Column(name = "principal_id")
    @javax.persistence.GeneratedValue(strategy = javax.persistence.GenerationType.TABLE, generator = "principal_id_generator")
    private java.lang.Long id;

    @javax.persistence.ManyToOne
    @javax.persistence.JoinColumns({ @javax.persistence.JoinColumn(name = "principal_type_id", referencedColumnName = "principal_type_id", nullable = false) })
    private org.apache.ambari.server.orm.entities.PrincipalTypeEntity principalType;

    @javax.persistence.OneToMany(mappedBy = "principal")
    private java.util.Set<org.apache.ambari.server.orm.entities.PrivilegeEntity> privileges = new java.util.HashSet<>();

    public java.lang.Long getId() {
        return id;
    }

    public void setId(java.lang.Long id) {
        this.id = id;
    }

    public org.apache.ambari.server.orm.entities.PrincipalTypeEntity getPrincipalType() {
        return principalType;
    }

    public void setPrincipalType(org.apache.ambari.server.orm.entities.PrincipalTypeEntity principalType) {
        this.principalType = principalType;
    }

    public java.util.Set<org.apache.ambari.server.orm.entities.PrivilegeEntity> getPrivileges() {
        return privileges;
    }

    public void setPrivileges(java.util.Set<org.apache.ambari.server.orm.entities.PrivilegeEntity> privileges) {
        this.privileges = privileges;
    }

    public void removePrivilege(org.apache.ambari.server.orm.entities.PrivilegeEntity privilege) {
        privileges.remove(privilege);
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o)
            return true;

        if ((o == null) || (getClass() != o.getClass()))
            return false;

        org.apache.ambari.server.orm.entities.PrincipalEntity that = ((org.apache.ambari.server.orm.entities.PrincipalEntity) (o));
        return id.equals(that.id) && (!(principalType != null ? !principalType.equals(that.principalType) : that.principalType != null));
    }

    @java.lang.Override
    public int hashCode() {
        int result = id.hashCode();
        result = (31 * result) + (principalType != null ? principalType.hashCode() : 0);
        return result;
    }
}