package org.apache.ambari.server.controller;
import io.swagger.annotations.ApiModelProperty;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.UriInfo;
public class ActiveWidgetLayoutRequest implements org.apache.ambari.server.controller.ApiModel {
    private java.util.List<org.apache.ambari.server.controller.ActiveWidgetLayoutRequest.WidgetLayoutIdWrapper> widgetLayouts;

    @io.swagger.annotations.ApiModelProperty(name = "WidgetLayouts")
    public java.util.List<org.apache.ambari.server.controller.ActiveWidgetLayoutRequest.WidgetLayoutIdWrapper> getWidgetLayouts() {
        return widgetLayouts;
    }

    private class WidgetLayoutIdWrapper {
        private java.lang.Long id;

        public java.lang.Long getId() {
            return id;
        }

        public void setId(java.lang.Long id) {
            this.id = id;
        }
    }
}