package org.apache.ambari.server.orm.entities;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
@javax.persistence.IdClass(org.apache.ambari.server.orm.entities.ViewParameterEntityPK.class)
@javax.persistence.Table(name = "viewparameter")
@javax.persistence.Entity
public class ViewParameterEntity {
    @javax.persistence.Id
    @javax.persistence.Column(name = "view_name", nullable = false, insertable = false, updatable = false)
    private java.lang.String viewName;

    @javax.persistence.Id
    @javax.persistence.Column(name = "name", nullable = false, insertable = true, updatable = false)
    private java.lang.String name;

    @javax.persistence.Column(name = "description")
    private java.lang.String description;

    @javax.persistence.Column(name = "label")
    private java.lang.String label;

    @javax.persistence.Column(name = "placeholder")
    private java.lang.String placeholder;

    @javax.persistence.Column(name = "default_value")
    private java.lang.String defaultValue;

    @javax.persistence.Column(name = "cluster_config")
    private java.lang.String clusterConfig;

    @javax.persistence.Column
    @javax.persistence.Basic
    private char required;

    @javax.persistence.Column
    @javax.persistence.Basic
    private char masked;

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

    public java.lang.String getDescription() {
        return description;
    }

    public void setDescription(java.lang.String description) {
        this.description = description;
    }

    public boolean isRequired() {
        return (required == 'y') || (required == 'Y');
    }

    public void setRequired(boolean required) {
        this.required = (required) ? 'Y' : 'N';
    }

    public boolean isMasked() {
        return (masked == 'y') || (masked == 'Y');
    }

    public void setMasked(boolean masked) {
        this.masked = (masked) ? 'Y' : 'N';
    }

    public org.apache.ambari.server.orm.entities.ViewEntity getViewEntity() {
        return view;
    }

    public void setViewEntity(org.apache.ambari.server.orm.entities.ViewEntity view) {
        this.view = view;
    }

    public java.lang.String getLabel() {
        return label;
    }

    public void setLabel(java.lang.String label) {
        this.label = label;
    }

    public java.lang.String getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(java.lang.String placeholder) {
        this.placeholder = placeholder;
    }

    public java.lang.String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(java.lang.String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public java.lang.String getClusterConfig() {
        return clusterConfig;
    }

    public void setClusterConfig(java.lang.String clusterConfig) {
        this.clusterConfig = clusterConfig;
    }
}