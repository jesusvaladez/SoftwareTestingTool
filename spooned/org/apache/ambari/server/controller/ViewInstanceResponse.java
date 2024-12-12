package org.apache.ambari.server.controller;
import io.swagger.annotations.ApiModelProperty;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.UriInfo;
public class ViewInstanceResponse implements org.apache.ambari.server.controller.ApiModel {
    private final org.apache.ambari.server.controller.ViewInstanceResponse.ViewInstanceResponseInfo viewInstanceResponseInfo;

    public ViewInstanceResponse(org.apache.ambari.server.controller.ViewInstanceResponse.ViewInstanceResponseInfo viewInstanceResponseInfo) {
        this.viewInstanceResponseInfo = viewInstanceResponseInfo;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.VIEW_INSTANCE_INFO)
    public org.apache.ambari.server.controller.ViewInstanceResponse.ViewInstanceResponseInfo getViewInstanceInfo() {
        return viewInstanceResponseInfo;
    }

    public class ViewInstanceResponseInfo extends org.apache.ambari.server.controller.ViewInstanceRequest.ViewInstanceRequestInfo {
        private final java.lang.String contextPath;

        private final boolean staticDriven;

        private java.lang.String shortUrl;

        private java.lang.String shortUrlName;

        private org.apache.ambari.view.validation.ValidationResult validationResult;

        private java.util.Map<java.lang.String, org.apache.ambari.view.validation.ValidationResult> propertyValidationResults;

        public ViewInstanceResponseInfo(java.lang.String viewName, java.lang.String version, java.lang.String instanceName, java.lang.String label, java.lang.String description, boolean visible, java.lang.String iconPath, java.lang.String icon64Path, java.util.Map<java.lang.String, java.lang.String> properties, java.util.Map<java.lang.String, java.lang.String> instanceData, java.lang.Integer clusterHandle, org.apache.ambari.view.ClusterType clusterType, java.lang.String contextPath, boolean staticDriven) {
            super(viewName, version, instanceName, label, description, visible, iconPath, icon64Path, properties, instanceData, clusterHandle, clusterType);
            this.contextPath = contextPath;
            this.staticDriven = staticDriven;
        }

        @java.lang.Override
        @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.VIEW_NAME_PROPERTY_ID)
        public java.lang.String getViewName() {
            return viewName;
        }

        @java.lang.Override
        @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.VERSION_PROPERTY_ID)
        public java.lang.String getVersion() {
            return version;
        }

        @java.lang.Override
        @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.INSTANCE_NAME_PROPERTY_ID)
        public java.lang.String getInstanceName() {
            return instanceName;
        }

        @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.CONTEXT_PATH_PROPERTY_ID)
        public java.lang.String getContextPath() {
            return contextPath;
        }

        @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.STATIC_PROPERTY_ID)
        public boolean isStaticDriven() {
            return staticDriven;
        }

        @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.SHORT_URL_PROPERTY_ID)
        public java.lang.String getShortUrl() {
            return shortUrl;
        }

        public void setShortUrl(java.lang.String shortUrl) {
            this.shortUrl = shortUrl;
        }

        @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.SHORT_URL_NAME_PROPERTY_ID)
        public java.lang.String getShortUrlName() {
            return shortUrlName;
        }

        public void setShortUrlName(java.lang.String shortUrlName) {
            this.shortUrlName = shortUrlName;
        }

        @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.VALIDATION_RESULT_PROPERTY_ID)
        public org.apache.ambari.view.validation.ValidationResult getValidationResult() {
            return validationResult;
        }

        public void setValidationResult(org.apache.ambari.view.validation.ValidationResult validationResult) {
            this.validationResult = validationResult;
        }

        @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.PROPERTY_VALIDATION_RESULTS_PROPERTY_ID)
        public java.util.Map<java.lang.String, org.apache.ambari.view.validation.ValidationResult> getPropertyValidationResults() {
            return propertyValidationResults;
        }

        public void setPropertyValidationResults(java.util.Map<java.lang.String, org.apache.ambari.view.validation.ValidationResult> propertyValidationResults) {
            this.propertyValidationResults = propertyValidationResults;
        }
    }
}