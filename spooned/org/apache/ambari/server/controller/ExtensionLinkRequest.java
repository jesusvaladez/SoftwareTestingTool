package org.apache.ambari.server.controller;
public class ExtensionLinkRequest {
    private java.lang.String linkId;

    private java.lang.String stackName;

    private java.lang.String stackVersion;

    private java.lang.String extensionName;

    private java.lang.String extensionVersion;

    public ExtensionLinkRequest(java.lang.String linkId, java.lang.String stackName, java.lang.String stackVersion, java.lang.String extensionName, java.lang.String extensionVersion) {
        this.setLinkId(linkId);
        this.setStackName(stackName);
        this.setStackVersion(stackVersion);
        this.setExtensionName(extensionName);
        this.setExtensionVersion(extensionVersion);
    }

    public java.lang.String getLinkId() {
        return linkId;
    }

    public void setLinkId(java.lang.String linkId) {
        this.linkId = linkId;
    }

    public java.lang.String getStackName() {
        return stackName;
    }

    public void setStackName(java.lang.String stackName) {
        this.stackName = stackName;
    }

    public java.lang.String getStackVersion() {
        return stackVersion;
    }

    public void setStackVersion(java.lang.String stackVersion) {
        this.stackVersion = stackVersion;
    }

    public java.lang.String getExtensionName() {
        return extensionName;
    }

    public void setExtensionName(java.lang.String extensionName) {
        this.extensionName = extensionName;
    }

    public java.lang.String getExtensionVersion() {
        return extensionVersion;
    }

    public void setExtensionVersion(java.lang.String extensionVersion) {
        this.extensionVersion = extensionVersion;
    }
}