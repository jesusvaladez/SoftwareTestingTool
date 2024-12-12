package org.apache.ambari.server.controller;
import io.swagger.annotations.ApiModelProperty;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.UriInfo;
public class ViewVersionResponse implements org.apache.ambari.server.controller.ApiModel {
    org.apache.ambari.server.controller.ViewVersionResponse.ViewVersionInfo viewVersionInfo;

    public ViewVersionResponse(org.apache.ambari.server.controller.ViewVersionResponse.ViewVersionInfo viewVersionInfo) {
        this.viewVersionInfo = viewVersionInfo;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ViewVersionResourceProvider.VIEW_VERSION_INFO)
    public org.apache.ambari.server.controller.ViewVersionResponse.ViewVersionInfo getViewVersionInfo() {
        return viewVersionInfo;
    }

    public static class ViewVersionInfo implements org.apache.ambari.server.controller.ApiModel {
        private final java.lang.String archive;

        private final java.lang.String buildNumber;

        private final boolean clusterConfigurable;

        private final java.lang.String description;

        private final java.lang.String label;

        private final java.lang.String maskerClass;

        private final java.lang.String maxAmbariVersion;

        private final java.lang.String minAmbariVersion;

        private final java.util.List<org.apache.ambari.server.view.configuration.ParameterConfig> parameters;

        private final org.apache.ambari.view.ViewDefinition.ViewStatus status;

        private final java.lang.String statusDetail;

        private final boolean system;

        private final java.lang.String version;

        private final java.lang.String viewName;

        public ViewVersionInfo(java.lang.String archive, java.lang.String buildNumber, boolean clusterConfigurable, java.lang.String description, java.lang.String label, java.lang.String maskerClass, java.lang.String maxAmbariVersion, java.lang.String minAmbariVersion, java.util.List<org.apache.ambari.server.view.configuration.ParameterConfig> parameters, org.apache.ambari.view.ViewDefinition.ViewStatus status, java.lang.String statusDetail, boolean system, java.lang.String version, java.lang.String viewName) {
            this.archive = archive;
            this.buildNumber = buildNumber;
            this.clusterConfigurable = clusterConfigurable;
            this.description = description;
            this.label = label;
            this.maskerClass = maskerClass;
            this.maxAmbariVersion = maxAmbariVersion;
            this.minAmbariVersion = minAmbariVersion;
            this.parameters = parameters;
            this.status = status;
            this.statusDetail = statusDetail;
            this.system = system;
            this.version = version;
            this.viewName = viewName;
        }

        @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ViewVersionResourceProvider.ARCHIVE_PROPERTY_ID)
        public java.lang.String getArchive() {
            return archive;
        }

        @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ViewVersionResourceProvider.BUILD_NUMBER_PROPERTY_ID)
        public java.lang.String getBuildNumber() {
            return buildNumber;
        }

        @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ViewVersionResourceProvider.CLUSTER_CONFIGURABLE_PROPERTY_ID)
        public boolean isClusterConfigurable() {
            return clusterConfigurable;
        }

        public java.lang.String getDescription() {
            return description;
        }

        @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ViewVersionResourceProvider.LABEL_PROPERTY_ID)
        public java.lang.String getLabel() {
            return label;
        }

        @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ViewVersionResourceProvider.MASKER_CLASS_PROPERTY_ID)
        public java.lang.String getMaskerClass() {
            return maskerClass;
        }

        @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ViewVersionResourceProvider.MAX_AMBARI_VERSION_PROPERTY_ID)
        public java.lang.String getMaxAmbariVersion() {
            return maxAmbariVersion;
        }

        @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ViewVersionResourceProvider.MIN_AMBARI_VERSION_PROPERTY_ID)
        public java.lang.String getMinAmbariVersion() {
            return minAmbariVersion;
        }

        @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ViewVersionResourceProvider.PARAMETERS_PROPERTY_ID)
        public java.util.List<org.apache.ambari.server.view.configuration.ParameterConfig> getParameters() {
            return parameters;
        }

        @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ViewVersionResourceProvider.STATUS_PROPERTY_ID)
        public org.apache.ambari.view.ViewDefinition.ViewStatus getStatus() {
            return status;
        }

        @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ViewVersionResourceProvider.STATUS_DETAIL_PROPERTY_ID)
        public java.lang.String getStatusDetail() {
            return statusDetail;
        }

        @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ViewVersionResourceProvider.SYSTEM_PROPERTY_ID)
        public boolean isSystem() {
            return system;
        }

        @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ViewVersionResourceProvider.VERSION_PROPERTY_ID)
        public java.lang.String getVersion() {
            return version;
        }

        @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ViewVersionResourceProvider.VIEW_NAME_PROPERTY_ID)
        public java.lang.String getViewName() {
            return viewName;
        }
    }
}