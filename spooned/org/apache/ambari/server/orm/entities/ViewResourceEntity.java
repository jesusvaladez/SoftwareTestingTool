package org.apache.ambari.server.orm.entities;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
@javax.persistence.IdClass(org.apache.ambari.server.orm.entities.ViewResourceEntityPK.class)
@javax.persistence.Table(name = "viewresource")
@javax.persistence.Entity
public class ViewResourceEntity {
    @javax.persistence.Id
    @javax.persistence.Column(name = "view_name", nullable = false, insertable = false, updatable = false)
    private java.lang.String viewName;

    @javax.persistence.Id
    @javax.persistence.Column(name = "name", nullable = false, insertable = true, updatable = false)
    private java.lang.String name;

    @javax.persistence.Column(name = "plural_name")
    @javax.persistence.Basic
    private java.lang.String pluralName;

    @javax.persistence.Column(name = "id_property")
    @javax.persistence.Basic
    private java.lang.String idProperty;

    @javax.persistence.Column(name = "subResource_names")
    @javax.persistence.Basic
    private java.lang.String subResourceNames;

    @javax.persistence.Column
    @javax.persistence.Basic
    private java.lang.String provider;

    @javax.persistence.Column
    @javax.persistence.Basic
    private java.lang.String service;

    @javax.persistence.Column(name = "\"resource\"")
    @javax.persistence.Basic
    private java.lang.String resource;

    @javax.persistence.ManyToOne
    @javax.persistence.JoinColumn(name = "view_name", referencedColumnName = "view_name", nullable = false)
    private org.apache.ambari.server.orm.entities.ViewEntity view;

    public java.lang.String getViewName() {
        return viewName;
    }

    public void setViewName(java.lang.String viewName) {
        this.viewName = viewName;
    }

    public java.lang.String getName() {
        return name;
    }

    public void setName(java.lang.String name) {
        this.name = name;
    }

    public java.lang.String getPluralName() {
        return pluralName;
    }

    public void setPluralName(java.lang.String pluralName) {
        this.pluralName = pluralName;
    }

    public java.lang.String getIdProperty() {
        return idProperty;
    }

    public void setIdProperty(java.lang.String idProperty) {
        this.idProperty = idProperty;
    }

    public java.util.Collection<java.lang.String> getSubResourceNames() {
        return java.util.Arrays.asList(subResourceNames.split("\\s*,\\s*"));
    }

    public void setSubResourceNames(java.util.Collection<java.lang.String> subResourceNames) {
        java.lang.String s = subResourceNames.toString();
        this.subResourceNames = (subResourceNames.size() > 0) ? s.substring(1, s.length() - 1) : null;
    }

    public java.lang.String getProvider() {
        return provider;
    }

    public void setProvider(java.lang.String provider) {
        this.provider = provider;
    }

    public java.lang.String getService() {
        return service;
    }

    public void setService(java.lang.String service) {
        this.service = service;
    }

    public java.lang.String getResource() {
        return resource;
    }

    public void setResource(java.lang.String resource) {
        this.resource = resource;
    }

    public org.apache.ambari.server.orm.entities.ViewEntity getViewEntity() {
        return view;
    }

    public void setViewEntity(org.apache.ambari.server.orm.entities.ViewEntity view) {
        this.view = view;
    }
}