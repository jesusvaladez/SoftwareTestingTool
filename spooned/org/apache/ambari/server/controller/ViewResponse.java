package org.apache.ambari.server.controller;
import io.swagger.annotations.ApiModelProperty;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.UriInfo;
public class ViewResponse implements org.apache.ambari.server.controller.ApiModel {
    private org.apache.ambari.server.controller.ViewResponse.ViewInfo viewInfo;

    public ViewResponse(org.apache.ambari.server.controller.ViewResponse.ViewInfo viewInfo) {
        this.viewInfo = viewInfo;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ViewResourceProvider.VIEW_INFO)
    public org.apache.ambari.server.controller.ViewResponse.ViewInfo getViewInfo() {
        return viewInfo;
    }

    private class ViewInfo implements org.apache.ambari.server.controller.ApiModel {
        private java.lang.String viewName;

        public ViewInfo(java.lang.String viewName) {
            this.viewName = viewName;
        }

        @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ViewResourceProvider.VIEW_NAME_PROPERTY_ID)
        public java.lang.String getViewName() {
            return viewName;
        }

        public void setViewName(java.lang.String viewName) {
            this.viewName = viewName;
        }
    }
}