package org.apache.ambari.server.controller;
import org.codehaus.jackson.annotate.JsonProperty;
public class WidgetResponse {
    private java.lang.Long id;

    private java.lang.String widgetName;

    private java.lang.String widgetType;

    private java.lang.String metrics;

    private java.lang.Long timeCreated;

    private java.lang.String author;

    private java.lang.String description;

    private java.lang.String displayName;

    private java.lang.String scope;

    private java.lang.String widgetValues;

    private java.lang.String properties;

    private java.lang.String clusterName;

    private java.lang.String tag;

    @org.codehaus.jackson.annotate.JsonProperty("id")
    public java.lang.Long getId() {
        return id;
    }

    public void setId(java.lang.Long id) {
        this.id = id;
    }

    @org.codehaus.jackson.annotate.JsonProperty("widget_name")
    public java.lang.String getWidgetName() {
        return widgetName;
    }

    public void setWidgetName(java.lang.String widgetName) {
        this.widgetName = widgetName;
    }

    @org.codehaus.jackson.annotate.JsonProperty("widget_type")
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

    @org.codehaus.jackson.annotate.JsonProperty("time_created")
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

    @org.codehaus.jackson.annotate.JsonProperty("display_name")
    public java.lang.String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(java.lang.String displayName) {
        this.displayName = displayName;
    }

    public java.lang.String getScope() {
        return scope;
    }

    public void setScope(java.lang.String scope) {
        this.scope = scope;
    }

    @org.codehaus.jackson.annotate.JsonProperty("values")
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

    @org.codehaus.jackson.annotate.JsonProperty("cluster_name")
    public java.lang.String getClusterName() {
        return clusterName;
    }

    public void setClusterName(java.lang.String clusterName) {
        this.clusterName = clusterName;
    }

    public java.lang.String getTag() {
        return tag;
    }

    public void setTag(java.lang.String tag) {
        this.tag = tag;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return widgetName;
    }

    public static org.apache.ambari.server.controller.WidgetResponse coerce(org.apache.ambari.server.orm.entities.WidgetEntity entity) {
        if (null == entity) {
            return null;
        }
        org.apache.ambari.server.controller.WidgetResponse response = new org.apache.ambari.server.controller.WidgetResponse();
        response.setId(entity.getId());
        response.setWidgetName(entity.getWidgetName());
        response.setWidgetType(entity.getWidgetType());
        response.setDescription(entity.getDescription());
        response.setMetrics(entity.getMetrics());
        response.setTimeCreated(entity.getTimeCreated());
        response.setAuthor(entity.getAuthor());
        response.setDisplayName(entity.getDefaultSectionName());
        response.setScope(entity.getScope());
        response.setWidgetValues(entity.getWidgetValues());
        response.setProperties(entity.getProperties());
        java.lang.String clusterName = (entity.getClusterEntity() != null) ? entity.getClusterEntity().getClusterName() : null;
        response.setClusterName(clusterName);
        response.setTag(entity.getTag());
        return response;
    }
}