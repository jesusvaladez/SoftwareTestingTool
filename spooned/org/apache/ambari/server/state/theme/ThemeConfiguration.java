package org.apache.ambari.server.state.theme;
import io.swagger.annotations.ApiModelProperty;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;
@org.codehaus.jackson.map.annotate.JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@org.codehaus.jackson.annotate.JsonIgnoreProperties(ignoreUnknown = true)
public class ThemeConfiguration implements org.apache.ambari.server.controller.ApiModel {
    @org.codehaus.jackson.annotate.JsonProperty("placement")
    private org.apache.ambari.server.state.theme.Placement placement;

    @org.codehaus.jackson.annotate.JsonProperty("widgets")
    private java.util.List<org.apache.ambari.server.state.theme.WidgetEntry> widgets;

    @org.codehaus.jackson.annotate.JsonProperty("layouts")
    private java.util.List<org.apache.ambari.server.state.theme.Layout> layouts;

    @io.swagger.annotations.ApiModelProperty(name = "placement")
    public org.apache.ambari.server.state.theme.Placement getPlacement() {
        return placement;
    }

    public void setPlacement(org.apache.ambari.server.state.theme.Placement placement) {
        this.placement = placement;
    }

    @io.swagger.annotations.ApiModelProperty(name = "widgets")
    public java.util.List<org.apache.ambari.server.state.theme.WidgetEntry> getWidgets() {
        return widgets;
    }

    public void setWidgets(java.util.List<org.apache.ambari.server.state.theme.WidgetEntry> widgets) {
        this.widgets = widgets;
    }

    @io.swagger.annotations.ApiModelProperty(name = "layouts")
    public java.util.List<org.apache.ambari.server.state.theme.Layout> getLayouts() {
        return layouts;
    }

    public void setLayouts(java.util.List<org.apache.ambari.server.state.theme.Layout> layouts) {
        this.layouts = layouts;
    }

    public void mergeWithParent(org.apache.ambari.server.state.theme.ThemeConfiguration parent) {
        if (parent == null) {
            return;
        }
        if (placement == null) {
            placement = parent.placement;
        } else {
            placement.mergeWithParent(parent.placement);
        }
        if (widgets == null) {
            widgets = parent.widgets;
        } else if (parent.widgets != null) {
            widgets = mergeWidgets(parent.widgets, widgets);
        }
        if (layouts == null) {
            layouts = parent.layouts;
        } else if (parent.layouts != null) {
            layouts = mergeLayouts(parent.layouts, layouts);
        }
    }

    private java.util.List<org.apache.ambari.server.state.theme.Layout> mergeLayouts(java.util.List<org.apache.ambari.server.state.theme.Layout> parentLayouts, java.util.List<org.apache.ambari.server.state.theme.Layout> childLayouts) {
        java.util.Map<java.lang.String, org.apache.ambari.server.state.theme.Layout> mergedLayouts = new java.util.LinkedHashMap<>();
        for (org.apache.ambari.server.state.theme.Layout parentLayout : parentLayouts) {
            mergedLayouts.put(parentLayout.getName(), parentLayout);
        }
        for (org.apache.ambari.server.state.theme.Layout childLayout : childLayouts) {
            if (childLayout.getName() != null) {
                if (childLayout.getTabs() == null) {
                    mergedLayouts.remove(childLayout.getName());
                } else {
                    org.apache.ambari.server.state.theme.Layout parentLayout = mergedLayouts.get(childLayout.getName());
                    childLayout.mergeWithParent(parentLayout);
                    mergedLayouts.put(childLayout.getName(), childLayout);
                }
            }
        }
        return new java.util.ArrayList<>(mergedLayouts.values());
    }

    private java.util.List<org.apache.ambari.server.state.theme.WidgetEntry> mergeWidgets(java.util.List<org.apache.ambari.server.state.theme.WidgetEntry> parentWidgets, java.util.List<org.apache.ambari.server.state.theme.WidgetEntry> childWidgets) {
        java.util.Map<java.lang.String, org.apache.ambari.server.state.theme.WidgetEntry> mergedWidgets = new java.util.LinkedHashMap<>();
        for (org.apache.ambari.server.state.theme.WidgetEntry widgetEntry : parentWidgets) {
            mergedWidgets.put(widgetEntry.getConfig(), widgetEntry);
        }
        for (org.apache.ambari.server.state.theme.WidgetEntry widgetEntry : childWidgets) {
            if (widgetEntry.getConfig() != null) {
                if (widgetEntry.getWidget() == null) {
                    mergedWidgets.remove(widgetEntry.getConfig());
                } else {
                    mergedWidgets.put(widgetEntry.getConfig(), widgetEntry);
                }
            }
        }
        return new java.util.ArrayList<>(mergedWidgets.values());
    }
}