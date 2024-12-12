package org.apache.ambari.server.orm.entities;
import javax.persistence.Basic;
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
@javax.persistence.Table(name = "viewentity")
@javax.persistence.Entity
@javax.persistence.TableGenerator(name = "viewentity_id_generator", table = "ambari_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_value", pkColumnValue = "viewentity_id_seq", initialValue = 1)
public class ViewEntityEntity {
    @javax.persistence.Column(name = "id")
    @javax.persistence.Id
    @javax.persistence.GeneratedValue(strategy = javax.persistence.GenerationType.TABLE, generator = "viewentity_id_generator")
    private java.lang.Long id;

    @javax.persistence.Column(name = "view_name", nullable = false, insertable = false, updatable = false)
    private java.lang.String viewName;

    @javax.persistence.Column(name = "view_instance_name", nullable = false, insertable = false, updatable = false)
    private java.lang.String viewInstanceName;

    @javax.persistence.Column(name = "class_name", nullable = false)
    @javax.persistence.Basic
    private java.lang.String className;

    @javax.persistence.Column(name = "id_property")
    @javax.persistence.Basic
    private java.lang.String idProperty;

    @javax.persistence.ManyToOne
    @javax.persistence.JoinColumns({ @javax.persistence.JoinColumn(name = "view_name", referencedColumnName = "view_name", nullable = false), @javax.persistence.JoinColumn(name = "view_instance_name", referencedColumnName = "name", nullable = false) })
    private org.apache.ambari.server.orm.entities.ViewInstanceEntity viewInstance;

    public java.lang.Long getId() {
        return id;
    }

    public void setId(java.lang.Long id) {
        this.id = id;
    }

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

    public java.lang.String getClassName() {
        return className;
    }

    public void setClassName(java.lang.String name) {
        this.className = name;
    }

    public java.lang.String getIdProperty() {
        return idProperty;
    }

    public void setIdProperty(java.lang.String idProperty) {
        this.idProperty = idProperty;
    }

    public org.apache.ambari.server.orm.entities.ViewInstanceEntity getViewInstance() {
        return viewInstance;
    }

    public void setViewInstance(org.apache.ambari.server.orm.entities.ViewInstanceEntity viewInstance) {
        this.viewInstance = viewInstance;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o)
            return true;

        if ((o == null) || (getClass() != o.getClass()))
            return false;

        org.apache.ambari.server.orm.entities.ViewEntityEntity that = ((org.apache.ambari.server.orm.entities.ViewEntityEntity) (o));
        if (!className.equals(that.className))
            return false;

        if (id != null ? !id.equals(that.id) : that.id != null)
            return false;

        if (idProperty != null ? !idProperty.equals(that.idProperty) : that.idProperty != null)
            return false;

        if (!viewInstanceName.equals(that.viewInstanceName))
            return false;

        if (!viewName.equals(that.viewName))
            return false;

        return true;
    }

    @java.lang.Override
    public int hashCode() {
        int result = (id != null) ? id.hashCode() : 0;
        result = (31 * result) + viewName.hashCode();
        result = (31 * result) + viewInstanceName.hashCode();
        result = (31 * result) + className.hashCode();
        result = (31 * result) + (idProperty != null ? idProperty.hashCode() : 0);
        return result;
    }
}