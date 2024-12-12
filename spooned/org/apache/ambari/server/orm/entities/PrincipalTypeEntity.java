package org.apache.ambari.server.orm.entities;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
@javax.persistence.Table(name = "adminprincipaltype")
@javax.persistence.Entity
@javax.persistence.TableGenerator(name = "principal_type_id_generator", table = "ambari_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_value", pkColumnValue = "principal_type_id_seq", initialValue = 100)
@javax.persistence.NamedQueries({ @javax.persistence.NamedQuery(name = "PrincipalTypeEntity.findByName", query = "SELECT p FROM PrincipalTypeEntity p WHERE p.name = :name") })
public class PrincipalTypeEntity {
    public static final int USER_PRINCIPAL_TYPE = 1;

    public static final int GROUP_PRINCIPAL_TYPE = 2;

    public static final int ROLE_PRINCIPAL_TYPE = 8;

    public static final java.lang.String USER_PRINCIPAL_TYPE_NAME = org.apache.ambari.server.orm.entities.PrincipalTypeEntity.PrincipalType.USER.toString();

    public static final java.lang.String GROUP_PRINCIPAL_TYPE_NAME = org.apache.ambari.server.orm.entities.PrincipalTypeEntity.PrincipalType.GROUP.toString();

    public static final java.lang.String ROLE_PRINCIPAL_TYPE_NAME = org.apache.ambari.server.orm.entities.PrincipalTypeEntity.PrincipalType.ROLE.toString();

    public enum PrincipalType {

        USER,
        GROUP,
        ROLE;}

    @javax.persistence.Id
    @javax.persistence.Column(name = "principal_type_id")
    @javax.persistence.GeneratedValue(strategy = javax.persistence.GenerationType.TABLE, generator = "principal_type_id_generator")
    private java.lang.Integer id;

    @javax.persistence.Column(name = "principal_type_name")
    private java.lang.String name;

    public java.lang.Integer getId() {
        return id;
    }

    public void setId(java.lang.Integer id) {
        this.id = id;
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

        org.apache.ambari.server.orm.entities.PrincipalTypeEntity that = ((org.apache.ambari.server.orm.entities.PrincipalTypeEntity) (o));
        if (!id.equals(that.id))
            return false;

        if (name != null ? !name.equals(that.name) : that.name != null)
            return false;

        return true;
    }

    @java.lang.Override
    public int hashCode() {
        int result = id.hashCode();
        result = (31 * result) + (name != null ? name.hashCode() : 0);
        return result;
    }
}