package org.apache.ambari.server.controller;
import io.swagger.annotations.ApiModelProperty;
public class ExtensionLinkResponse implements org.apache.ambari.server.stack.Validable , org.apache.ambari.server.controller.ApiModel {
    private org.apache.ambari.server.controller.ExtensionLinkResponse.ExtensionLinkResponseInfo extensionLinkResponseInfo;

    private boolean valid;

    private java.util.Set<java.lang.String> errorSet = new java.util.HashSet<>();

    public ExtensionLinkResponse(java.lang.String linkId, java.lang.String stackName, java.lang.String stackVersion, java.lang.String extensionName, java.lang.String extensionVersion, boolean valid, java.util.Collection<java.lang.String> errorSet) {
        extensionLinkResponseInfo = new org.apache.ambari.server.controller.ExtensionLinkResponse.ExtensionLinkResponseInfo(linkId, stackName, stackVersion, extensionName, extensionVersion, valid, errorSet);
    }

    @io.swagger.annotations.ApiModelProperty(name = "ExtensionLink")
    public org.apache.ambari.server.controller.ExtensionLinkResponse.ExtensionLinkResponseInfo getExtensionLinkResponseInfo() {
        return extensionLinkResponseInfo;
    }

    @java.lang.Override
    @io.swagger.annotations.ApiModelProperty(hidden = true)
    public boolean isValid() {
        return valid;
    }

    @java.lang.Override
    public void setValid(boolean valid) {
        this.valid = valid;
    }

    @java.lang.Override
    public void addError(java.lang.String error) {
        errorSet.add(error);
    }

    @java.lang.Override
    @io.swagger.annotations.ApiModelProperty(hidden = true)
    public java.util.Collection<java.lang.String> getErrors() {
        return errorSet;
    }

    @java.lang.Override
    public void addErrors(java.util.Collection<java.lang.String> errors) {
        this.errorSet.addAll(errors);
    }

    public class ExtensionLinkResponseInfo {
        public ExtensionLinkResponseInfo(java.lang.String linkId, java.lang.String stackName, java.lang.String stackVersion, java.lang.String extensionName, java.lang.String extensionVersion, boolean valid, java.util.Collection<java.lang.String> errorSet) {
            setLinkId(linkId);
            setStackName(stackName);
            setStackVersion(stackVersion);
            setExtensionName(extensionName);
            setExtensionVersion(extensionVersion);
            setValid(valid);
            addErrors(errorSet);
        }

        private java.lang.String linkId;

        private java.lang.String stackName;

        private java.lang.String stackVersion;

        private java.lang.String extensionName;

        private java.lang.String extensionVersion;

        @io.swagger.annotations.ApiModelProperty(name = "link_id")
        public java.lang.String getLinkId() {
            return linkId;
        }

        public void setLinkId(java.lang.String linkId) {
            this.linkId = linkId;
        }

        @io.swagger.annotations.ApiModelProperty(name = "stack_name")
        public java.lang.String getStackName() {
            return stackName;
        }

        public void setStackName(java.lang.String stackName) {
            this.stackName = stackName;
        }

        @io.swagger.annotations.ApiModelProperty(name = "stack_version")
        public java.lang.String getStackVersion() {
            return stackVersion;
        }

        public void setStackVersion(java.lang.String stackVersion) {
            this.stackVersion = stackVersion;
        }

        @io.swagger.annotations.ApiModelProperty(name = "extension_name")
        public java.lang.String getExtensionName() {
            return extensionName;
        }

        public void setExtensionName(java.lang.String extensionName) {
            this.extensionName = extensionName;
        }

        @io.swagger.annotations.ApiModelProperty(name = "extension_version")
        public java.lang.String getExtensionVersion() {
            return extensionVersion;
        }

        public void setExtensionVersion(java.lang.String extensionVersion) {
            this.extensionVersion = extensionVersion;
        }
    }
}