package org.apache.ambari.server.state.theme;
import io.swagger.annotations.ApiModelProperty;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;
@org.codehaus.jackson.map.annotate.JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@org.codehaus.jackson.annotate.JsonIgnoreProperties(ignoreUnknown = true)
public class WidgetEntry implements org.apache.ambari.server.controller.ApiModel {
    @org.codehaus.jackson.annotate.JsonProperty("config")
    private java.lang.String config;

    @org.codehaus.jackson.annotate.JsonProperty("widget")
    private org.apache.ambari.server.state.theme.Widget widget;

    @io.swagger.annotations.ApiModelProperty(name = "config")
    public java.lang.String getConfig() {
        return config;
    }

    public void setConfig(java.lang.String config) {
        this.config = config;
    }

    @io.swagger.annotations.ApiModelProperty(name = "widget")
    public org.apache.ambari.server.state.theme.Widget getWidget() {
        return widget;
    }

    public void setWidget(org.apache.ambari.server.state.theme.Widget widget) {
        this.widget = widget;
    }
}