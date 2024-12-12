package org.apache.ambari.server.controller;
import io.swagger.annotations.ApiModelProperty;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.UriInfo;
public class ViewPermissionResponse implements org.apache.ambari.server.controller.ApiModel {
    private final org.apache.ambari.server.controller.ViewPermissionResponse.ViewPermissionInfo viewPermissionInfo;

    public ViewPermissionResponse(org.apache.ambari.server.controller.ViewPermissionResponse.ViewPermissionInfo viewPermissionInfo) {
        this.viewPermissionInfo = viewPermissionInfo;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ViewPermissionResourceProvider.PERMISSION_INFO)
    public org.apache.ambari.server.controller.ViewPermissionResponse.ViewPermissionInfo getViewPermissionInfo() {
        return viewPermissionInfo;
    }

    public static class ViewPermissionInfo {
        private final java.lang.String viewName;

        private final java.lang.String version;

        private final java.lang.Integer permissionId;

        private final java.lang.String permissionName;

        private final java.lang.String resourceName;

        public ViewPermissionInfo(java.lang.String viewName, java.lang.String version, java.lang.Integer permissionId, java.lang.String permissionName, java.lang.String resourceName) {
            this.viewName = viewName;
            this.version = version;
            this.permissionId = permissionId;
            this.permissionName = permissionName;
            this.resourceName = resourceName;
        }

        @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ViewPermissionResourceProvider.VIEW_NAME_PROPERTY_ID)
        public java.lang.String getViewName() {
            return viewName;
        }

        @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ViewPermissionResourceProvider.VERSION_PROPERTY_ID)
        public java.lang.String getVersion() {
            return version;
        }

        @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ViewPermissionResourceProvider.PERMISSION_ID_PROPERTY_ID)
        public java.lang.Integer getPermissionId() {
            return permissionId;
        }

        @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ViewPermissionResourceProvider.PERMISSION_NAME_PROPERTY_ID)
        public java.lang.String getPermissionName() {
            return permissionName;
        }

        @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ViewPermissionResourceProvider.RESOURCE_NAME_PROPERTY_ID)
        public java.lang.String getResourceName() {
            return resourceName;
        }
    }
}