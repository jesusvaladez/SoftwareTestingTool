package org.apache.ambari.server.orm.entities;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
@javax.persistence.Table(name = "adminresource")
@javax.persistence.Entity
@javax.persistence.TableGenerator(name = "resource_id_generator", table = "ambari_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_value", pkColumnValue = "resource_id_seq", initialValue = 2)
public class ResourceEntity {
    public static final long AMBARI_RESOURCE_ID = 1L;

    @javax.persistence.Id
    @javax.persistence.Column(name = "resource_id")
    @javax.persistence.GeneratedValue(strategy = javax.persistence.GenerationType.TABLE, generator = "resource_id_generator")
    private java.lang.Long id;

    @javax.persistence.ManyToOne
    @javax.persistence.JoinColumns({ @javax.persistence.JoinColumn(name = "resource_type_id", referencedColumnName = "resource_type_id", nullable = false) })
    private org.apache.ambari.server.orm.entities.ResourceTypeEntity resourceType;

    @javax.persistence.OneToMany(cascade = javax.persistence.CascadeType.ALL, mappedBy = "resource")
    private java.util.Collection<org.apache.ambari.server.orm.entities.PrivilegeEntity> privileges = new java.util.HashSet<>();

    public java.lang.Long getId() {
        return id;
    }

    public void setId(java.lang.Long id) {
        this.id = id;
    }

    public org.apache.ambari.server.orm.entities.ResourceTypeEntity getResourceType() {
        return resourceType;
    }

    public void setResourceType(org.apache.ambari.server.orm.entities.ResourceTypeEntity resourceType) {
        this.resourceType = resourceType;
    }

    public java.util.Collection<org.apache.ambari.server.orm.entities.PrivilegeEntity> getPrivileges() {
        return privileges;
    }

    public void setPrivileges(java.util.Collection<org.apache.ambari.server.orm.entities.PrivilegeEntity> privileges) {
        this.privileges = privileges;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o)
            return true;

        if ((o == null) || (getClass() != o.getClass()))
            return false;

        org.apache.ambari.server.orm.entities.ResourceEntity that = ((org.apache.ambari.server.orm.entities.ResourceEntity) (o));
        return (!(id != null ? !id.equals(that.id) : that.id != null)) && (!(resourceType != null ? !resourceType.equals(that.resourceType) : that.resourceType != null));
    }

    @java.lang.Override
    public int hashCode() {
        int result = (id != null) ? id.hashCode() : 0;
        result = (31 * result) + (resourceType != null ? resourceType.hashCode() : 0);
        return result;
    }
}