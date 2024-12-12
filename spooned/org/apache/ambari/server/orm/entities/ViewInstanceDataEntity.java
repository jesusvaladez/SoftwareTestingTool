package org.apache.ambari.server.orm.entities;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
@javax.persistence.IdClass(org.apache.ambari.server.orm.entities.ViewInstanceDataEntityPK.class)
@javax.persistence.Table(name = "viewinstancedata")
@javax.persistence.Entity
public class ViewInstanceDataEntity {
    @javax.persistence.Id
    @javax.persistence.Column(name = "view_instance_id", nullable = false, insertable = false, updatable = false)
    private java.lang.Long viewInstanceId;

    @javax.persistence.Column(name = "view_name", nullable = false, insertable = false, updatable = false)
    private java.lang.String viewName;

    @javax.persistence.Column(name = "view_instance_name", nullable = false, insertable = false, updatable = false)
    private java.lang.String viewInstanceName;

    @javax.persistence.Id
    @javax.persistence.Column(name = "name", nullable = false, insertable = true, updatable = false)
    private java.lang.String name;

    @javax.persistence.Id
    @javax.persistence.Column(name = "user_name", nullable = false, insertable = true, updatable = false)
    private java.lang.String user;

    @javax.persistence.Column
    @javax.persistence.Basic
    private java.lang.String value;

    @javax.persistence.ManyToOne
    @javax.persistence.JoinColumns({ @javax.persistence.JoinColumn(name = "view_instance_id", referencedColumnName = "view_instance_id", nullable = false), @javax.persistence.JoinColumn(name = "view_name", referencedColumnName = "view_name", nullable = false), @javax.persistence.JoinColumn(name = "view_instance_name", referencedColumnName = "name", nullable = false) })
    private org.apache.ambari.server.orm.entities.ViewInstanceEntity viewInstance;

    public java.lang.Long getViewInstanceId() {
        return viewInstanceId;
    }

    public void setViewInstanceId(java.lang.Long viewInstanceId) {
        this.viewInstanceId = viewInstanceId;
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

    public java.lang.String getValue() {
        return value;
    }

    public void setValue(java.lang.String value) {
        this.value = value;
    }

    public org.apache.ambari.server.orm.entities.ViewInstanceEntity getViewInstanceEntity() {
        return viewInstance;
    }

    public void setViewInstanceEntity(org.apache.ambari.server.orm.entities.ViewInstanceEntity viewInstance) {
        this.viewInstance = viewInstance;
    }
}