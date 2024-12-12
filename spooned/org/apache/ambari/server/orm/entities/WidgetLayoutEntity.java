package org.apache.ambari.server.orm.entities;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.UniqueConstraint;
@javax.persistence.Entity
@javax.persistence.Table(name = "widget_layout")
@javax.persistence.TableGenerator(name = "widget_layout_id_generator", table = "ambari_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_value", pkColumnValue = "widget_layout_id_seq", initialValue = 0, uniqueConstraints = @javax.persistence.UniqueConstraint(columnNames = { "layout_name", "cluster_id" }))
@javax.persistence.NamedQueries({ @javax.persistence.NamedQuery(name = "WidgetLayoutEntity.findAll", query = "SELECT widgetLayout FROM WidgetLayoutEntity widgetLayout"), @javax.persistence.NamedQuery(name = "WidgetLayoutEntity.findByCluster", query = "SELECT widgetLayout FROM WidgetLayoutEntity widgetLayout WHERE widgetLayout.clusterId = :clusterId"), @javax.persistence.NamedQuery(name = "WidgetLayoutEntity.findBySectionName", query = "SELECT widgetLayout FROM WidgetLayoutEntity widgetLayout WHERE widgetLayout.sectionName = :sectionName"), @javax.persistence.NamedQuery(name = "WidgetLayoutEntity.findByName", query = "SELECT widgetLayout FROM WidgetLayoutEntity widgetLayout WHERE widgetLayout.clusterId = :clusterId AND widgetLayout.layoutName = :layoutName AND widgetLayout.userName = :userName") })
public class WidgetLayoutEntity {
    @javax.persistence.Id
    @javax.persistence.GeneratedValue(strategy = javax.persistence.GenerationType.TABLE, generator = "widget_layout_id_generator")
    @javax.persistence.Column(name = "id", nullable = false, updatable = false)
    private java.lang.Long id;

    @javax.persistence.Column(name = "layout_name", nullable = false, length = 255)
    private java.lang.String layoutName;

    @javax.persistence.Column(name = "section_name", nullable = false, length = 255)
    private java.lang.String sectionName;

    @javax.persistence.Column(name = "cluster_id", nullable = false)
    private java.lang.Long clusterId;

    @javax.persistence.Column(name = "user_name", nullable = false)
    private java.lang.String userName;

    @javax.persistence.Column(name = "scope", nullable = false)
    private java.lang.String scope;

    @javax.persistence.Column(name = "display_name")
    private java.lang.String displayName;

    @javax.persistence.ManyToOne
    @javax.persistence.JoinColumn(name = "cluster_id", referencedColumnName = "cluster_id", nullable = false, updatable = false, insertable = false)
    private org.apache.ambari.server.orm.entities.ClusterEntity clusterEntity;

    @javax.persistence.OneToMany(cascade = javax.persistence.CascadeType.ALL, mappedBy = "widgetLayout", orphanRemoval = true)
    @javax.persistence.OrderBy("widgetOrder")
    private java.util.List<org.apache.ambari.server.orm.entities.WidgetLayoutUserWidgetEntity> listWidgetLayoutUserWidgetEntity;

    public java.lang.Long getId() {
        return id;
    }

    public void setId(java.lang.Long id) {
        this.id = id;
    }

    public java.lang.String getLayoutName() {
        return layoutName;
    }

    public void setLayoutName(java.lang.String layoutName) {
        this.layoutName = layoutName;
    }

    public java.lang.String getSectionName() {
        return sectionName;
    }

    public void setSectionName(java.lang.String sectionName) {
        this.sectionName = sectionName;
    }

    public java.lang.String getUserName() {
        return userName;
    }

    public void setUserName(java.lang.String userName) {
        this.userName = userName;
    }

    public java.lang.String getScope() {
        return scope;
    }

    public void setScope(java.lang.String scope) {
        this.scope = scope;
    }

    public java.lang.String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(java.lang.String displayName) {
        this.displayName = displayName;
    }

    public java.lang.Long getClusterId() {
        return clusterId;
    }

    public void setClusterId(java.lang.Long clusterId) {
        this.clusterId = clusterId;
    }

    public org.apache.ambari.server.orm.entities.ClusterEntity getClusterEntity() {
        return clusterEntity;
    }

    public void setClusterEntity(org.apache.ambari.server.orm.entities.ClusterEntity clusterEntity) {
        this.clusterEntity = clusterEntity;
    }

    public java.util.List<org.apache.ambari.server.orm.entities.WidgetLayoutUserWidgetEntity> getListWidgetLayoutUserWidgetEntity() {
        return listWidgetLayoutUserWidgetEntity;
    }

    public void setListWidgetLayoutUserWidgetEntity(java.util.List<org.apache.ambari.server.orm.entities.WidgetLayoutUserWidgetEntity> listWidgetLayoutUserWidgetEntity) {
        this.listWidgetLayoutUserWidgetEntity = listWidgetLayoutUserWidgetEntity;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o)
            return true;

        if ((o == null) || (getClass() != o.getClass()))
            return false;

        org.apache.ambari.server.orm.entities.WidgetLayoutEntity that = ((org.apache.ambari.server.orm.entities.WidgetLayoutEntity) (o));
        return java.util.Objects.equals(id, that.id);
    }

    @java.lang.Override
    public int hashCode() {
        return java.util.Objects.hashCode(id);
    }
}