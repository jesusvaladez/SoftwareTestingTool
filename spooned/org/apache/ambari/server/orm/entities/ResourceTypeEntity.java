package org.apache.ambari.server.orm.entities;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
@javax.persistence.Table(name = "adminresourcetype")
@javax.persistence.Entity
@javax.persistence.TableGenerator(name = "resource_type_id_generator", table = "ambari_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_value", pkColumnValue = "resource_type_id_seq", initialValue = 4)
public class ResourceTypeEntity {
    @javax.persistence.Id
    @javax.persistence.Column(name = "resource_type_id")
    @javax.persistence.GeneratedValue(strategy = javax.persistence.GenerationType.TABLE, generator = "resource_type_id_generator")
    private java.lang.Integer id;

    @javax.persistence.Column(name = "resource_type_name")
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

        org.apache.ambari.server.orm.entities.ResourceTypeEntity that = ((org.apache.ambari.server.orm.entities.ResourceTypeEntity) (o));
        return (!(id != null ? !id.equals(that.id) : that.id != null)) && (!(name != null ? !name.equals(that.name) : that.name != null));
    }

    @java.lang.Override
    public int hashCode() {
        int result = (id != null) ? id.hashCode() : 0;
        result = (31 * result) + (name != null ? name.hashCode() : 0);
        return result;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return ((("ResourceTypeEntity [id=" + id) + ", name=") + name) + "]";
    }
}