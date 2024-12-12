package org.apache.ambari.server.orm.entities;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
@javax.persistence.Entity
@javax.persistence.Table(name = "widget")
@javax.persistence.TableGenerator(name = "widget_id_generator", table = "ambari_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_value", pkColumnValue = "widget_id_seq", initialValue = 0)
@javax.persistence.NamedQueries({ @javax.persistence.NamedQuery(name = "WidgetEntity.findAll", query = "SELECT widget FROM WidgetEntity widget"), @javax.persistence.NamedQuery(name = "WidgetEntity.findByScopeOrAuthor", query = "SELECT widget FROM WidgetEntity widget " + ("WHERE widget.author = :author " + "OR widget.scope = :scope")), @javax.persistence.NamedQuery(name = "WidgetEntity.findByCluster", query = "SELECT widget FROM WidgetEntity widget WHERE widget.clusterId = :clusterId"), @javax.persistence.NamedQuery(name = "WidgetEntity.findByName", query = "SELECT widget FROM WidgetEntity widget " + ((("WHERE widget.clusterId = :clusterId " + "AND widget.widgetName = :widgetName ") + "AND widget.author = :author ") + "AND widget.defaultSectionName = :defaultSectionName")), @javax.persistence.NamedQuery(name = "WidgetEntity.findBySectionName", query = "SELECT widget FROM WidgetEntity widget " + (("INNER JOIN widget.listWidgetLayoutUserWidgetEntity widgetLayoutUserWidget " + "INNER JOIN widgetLayoutUserWidget.widgetLayout  widgetLayout ") + "WHERE widgetLayout.sectionName = :sectionName")) })
public class WidgetEntity {
    public static final java.lang.String CLUSTER_SCOPE = "CLUSTER";

    public static final java.lang.String USER_SCOPE = "USER";

    @javax.persistence.Id
    @javax.persistence.GeneratedValue(strategy = javax.persistence.GenerationType.TABLE, generator = "widget_id_generator")
    @javax.persistence.Column(name = "id", nullable = false, updatable = false)
    private java.lang.Long id;

    @javax.persistence.Column(name = "widget_name", nullable = false, length = 255)
    private java.lang.String widgetName;

    @javax.persistence.Column(name = "widget_type", nullable = false, length = 255)
    private java.lang.String widgetType;

    @javax.persistence.Lob
    @javax.persistence.Basic(fetch = javax.persistence.FetchType.LAZY)
    @javax.persistence.Column(name = "metrics")
    private java.lang.String metrics;

    @javax.persistence.Column(name = "time_created", nullable = false, length = 255)
    private java.lang.Long timeCreated = java.lang.System.currentTimeMillis();

    @javax.persistence.Column(name = "author", length = 255)
    private java.lang.String author;

    @javax.persistence.Column(name = "description", length = 255)
    private java.lang.String description;

    @javax.persistence.Column(name = "default_section_name", length = 255, nullable = true)
    private java.lang.String defaultSectionName;

    @javax.persistence.Column(name = "scope", length = 255)
    private java.lang.String scope;

    @javax.persistence.Lob
    @javax.persistence.Basic(fetch = javax.persistence.FetchType.LAZY)
    @javax.persistence.Column(name = "widget_values")
    private java.lang.String widgetValues;

    @javax.persistence.Lob
    @javax.persistence.Basic(fetch = javax.persistence.FetchType.LAZY)
    @javax.persistence.Column(name = "properties")
    private java.lang.String properties;

    @javax.persistence.Column(name = "cluster_id", nullable = false)
    private java.lang.Long clusterId;

    @javax.persistence.Column(name = "tag", length = 255)
    private java.lang.String tag;

    @javax.persistence.ManyToOne
    @javax.persistence.JoinColumn(name = "cluster_id", referencedColumnName = "cluster_id", nullable = false, updatable = false, insertable = false)
    private org.apache.ambari.server.orm.entities.ClusterEntity clusterEntity;

    @javax.persistence.OneToMany(cascade = javax.persistence.CascadeType.ALL, mappedBy = "widget", orphanRemoval = true)
    private java.util.List<org.apache.ambari.server.orm.entities.WidgetLayoutUserWidgetEntity> listWidgetLayoutUserWidgetEntity;

    public java.lang.Long getId() {
        return id;
    }

    public void setId(java.lang.Long id) {
        this.id = id;
    }

    public java.lang.String getWidgetName() {
        return widgetName;
    }

    public void setWidgetName(java.lang.String widgetName) {
        this.widgetName = widgetName;
    }

    public java.lang.String getWidgetType() {
        return widgetType;
    }

    public void setWidgetType(java.lang.String widgetType) {
        this.widgetType = widgetType;
    }

    public java.lang.String getMetrics() {
        return metrics;
    }

    public void setMetrics(java.lang.String metrics) {
        this.metrics = metrics;
    }

    public java.lang.Long getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(java.lang.Long timeCreated) {
        this.timeCreated = timeCreated;
    }

    public java.lang.String getAuthor() {
        return author;
    }

    public void setAuthor(java.lang.String author) {
        this.author = author;
    }

    public java.lang.String getDescription() {
        return description;
    }

    public void setDescription(java.lang.String description) {
        this.description = description;
    }

    public java.lang.String getDefaultSectionName() {
        return defaultSectionName;
    }

    public void setDefaultSectionName(java.lang.String displayName) {
        this.defaultSectionName = displayName;
    }

    public java.lang.String getScope() {
        return scope;
    }

    public void setScope(java.lang.String scope) {
        this.scope = scope;
    }

    public java.lang.String getWidgetValues() {
        return widgetValues;
    }

    public void setWidgetValues(java.lang.String widgetValues) {
        this.widgetValues = widgetValues;
    }

    public java.lang.String getProperties() {
        return properties;
    }

    public void setProperties(java.lang.String properties) {
        this.properties = properties;
    }

    public org.apache.ambari.server.orm.entities.ClusterEntity getClusterEntity() {
        return clusterEntity;
    }

    public void setClusterEntity(org.apache.ambari.server.orm.entities.ClusterEntity clusterEntity) {
        this.clusterEntity = clusterEntity;
    }

    public java.lang.Long getClusterId() {
        return clusterId;
    }

    public void setClusterId(java.lang.Long clusterId) {
        this.clusterId = clusterId;
    }

    public java.lang.String getTag() {
        return tag;
    }

    public void setTag(java.lang.String tag) {
        this.tag = tag;
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

        org.apache.ambari.server.orm.entities.WidgetEntity that = ((org.apache.ambari.server.orm.entities.WidgetEntity) (o));
        return java.util.Objects.equals(id, that.id);
    }

    @java.lang.Override
    public int hashCode() {
        return java.util.Objects.hashCode(id);
    }
}