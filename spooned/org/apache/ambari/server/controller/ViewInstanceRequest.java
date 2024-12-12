package org.apache.ambari.server.controller;
import io.swagger.annotations.ApiModelProperty;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.UriInfo;
public class ViewInstanceRequest implements org.apache.ambari.server.controller.ApiModel {
    private final org.apache.ambari.server.controller.ViewInstanceRequest.ViewInstanceRequestInfo viewInstanceRequestInfo;

    public ViewInstanceRequest(org.apache.ambari.server.controller.ViewInstanceRequest.ViewInstanceRequestInfo viewInstanceRequestInfo) {
        this.viewInstanceRequestInfo = viewInstanceRequestInfo;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.VIEW_INSTANCE_INFO)
    public org.apache.ambari.server.controller.ViewInstanceRequest.ViewInstanceRequestInfo getViewInstanceInfo() {
        return viewInstanceRequestInfo;
    }

    public static class ViewInstanceRequestInfo {
        protected final java.lang.String viewName;

        protected final java.lang.String version;

        protected final java.lang.String instanceName;

        private final java.lang.String label;

        private final java.lang.String description;

        private final boolean visible;

        private final java.lang.String iconPath;

        private final java.lang.String icon64Path;

        private final java.util.Map<java.lang.String, java.lang.String> properties;

        private final java.util.Map<java.lang.String, java.lang.String> instanceData;

        private final java.lang.Integer clusterHandle;

        private final org.apache.ambari.view.ClusterType clusterType;

        public ViewInstanceRequestInfo(java.lang.String viewName, java.lang.String version, java.lang.String instanceName, java.lang.String label, java.lang.String description, boolean visible, java.lang.String iconPath, java.lang.String icon64Path, java.util.Map<java.lang.String, java.lang.String> properties, java.util.Map<java.lang.String, java.lang.String> instanceData, java.lang.Integer clusterHandle, org.apache.ambari.view.ClusterType clusterType) {
            this.viewName = viewName;
            this.version = version;
            this.instanceName = instanceName;
            this.label = label;
            this.description = description;
            this.visible = visible;
            this.iconPath = iconPath;
            this.icon64Path = icon64Path;
            this.properties = properties;
            this.instanceData = instanceData;
            this.clusterHandle = clusterHandle;
            this.clusterType = clusterType;
        }

        @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.VIEW_NAME_PROPERTY_ID, hidden = true)
        public java.lang.String getViewName() {
            return viewName;
        }

        @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.VERSION_PROPERTY_ID, hidden = true)
        public java.lang.String getVersion() {
            return version;
        }

        @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.INSTANCE_NAME_PROPERTY_ID, hidden = true)
        public java.lang.String getInstanceName() {
            return instanceName;
        }

        @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.LABEL_PROPERTY_ID)
        public java.lang.String getLabel() {
            return label;
        }

        @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.DESCRIPTION_PROPERTY_ID)
        public java.lang.String getDescription() {
            return description;
        }

        @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.VISIBLE_PROPERTY_ID)
        public boolean isVisible() {
            return visible;
        }

        @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.ICON_PATH_PROPERTY_ID)
        public java.lang.String getIconPath() {
            return iconPath;
        }

        @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.ICON64_PATH_PROPERTY_ID)
        public java.lang.String getIcon64Path() {
            return icon64Path;
        }

        @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.PROPERTIES_PROPERTY_ID)
        public java.util.Map<java.lang.String, java.lang.String> getProperties() {
            return properties;
        }

        @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.INSTANCE_DATA_PROPERTY_ID)
        public java.util.Map<java.lang.String, java.lang.String> getInstanceData() {
            return instanceData;
        }

        @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.CLUSTER_HANDLE_PROPERTY_ID)
        public java.lang.Integer getClusterHandle() {
            return clusterHandle;
        }

        @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.CLUSTER_TYPE_PROPERTY_ID)
        public org.apache.ambari.view.ClusterType getClusterType() {
            return clusterType;
        }
    }
}