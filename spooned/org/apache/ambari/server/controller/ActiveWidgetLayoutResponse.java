package org.apache.ambari.server.controller;
import io.swagger.annotations.ApiModelProperty;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.UriInfo;
public class ActiveWidgetLayoutResponse implements org.apache.ambari.server.controller.ApiModel {
    private final java.lang.Long id;

    private final java.lang.String clusterName;

    private final java.lang.String displayName;

    private final java.lang.String layoutName;

    private final java.lang.String scope;

    private final java.lang.String sectionName;

    private final java.lang.String userName;

    private java.util.List<java.util.HashMap<java.lang.String, org.apache.ambari.server.controller.WidgetResponse>> widgets;

    public ActiveWidgetLayoutResponse(java.lang.Long id, java.lang.String clusterName, java.lang.String displayName, java.lang.String layoutName, java.lang.String sectionName, java.lang.String scope, java.lang.String userName, java.util.List<java.util.HashMap<java.lang.String, org.apache.ambari.server.controller.WidgetResponse>> widgets) {
        this.id = id;
        this.clusterName = clusterName;
        this.displayName = displayName;
        this.layoutName = layoutName;
        this.sectionName = sectionName;
        this.scope = scope;
        this.userName = userName;
        this.widgets = widgets;
    }

    @io.swagger.annotations.ApiModelProperty(name = "WidgetLayoutInfo/id", hidden = true)
    public java.lang.Long getId() {
        return id;
    }

    @io.swagger.annotations.ApiModelProperty(name = "WidgetLayoutInfo/cluster_name")
    public java.lang.String getClusterName() {
        return clusterName;
    }

    @io.swagger.annotations.ApiModelProperty(name = "WidgetLayoutInfo/display_name")
    public java.lang.String getDisplayName() {
        return displayName;
    }

    @io.swagger.annotations.ApiModelProperty(name = "WidgetLayoutInfo/layout_name")
    public java.lang.String getLayoutName() {
        return layoutName;
    }

    @io.swagger.annotations.ApiModelProperty(name = "WidgetLayoutInfo/scope")
    public java.lang.String getScope() {
        return scope;
    }

    @io.swagger.annotations.ApiModelProperty(name = "WidgetLayoutInfo/section_name")
    public java.lang.String getSectionName() {
        return sectionName;
    }

    @io.swagger.annotations.ApiModelProperty(name = "WidgetLayoutInfo/user_name")
    public java.lang.String getUserName() {
        return userName;
    }

    @io.swagger.annotations.ApiModelProperty(name = "WidgetLayoutInfo/widgets")
    public java.util.List<java.util.HashMap<java.lang.String, org.apache.ambari.server.controller.WidgetResponse>> getWidgets() {
        return widgets;
    }

    public void setWidgets(java.util.List<java.util.HashMap<java.lang.String, org.apache.ambari.server.controller.WidgetResponse>> widgets) {
        this.widgets = widgets;
    }
}