package org.apache.ambari.server.controller;
public class ExtensionVersionRequest extends org.apache.ambari.server.controller.ExtensionRequest {
    public ExtensionVersionRequest(java.lang.String extensionName, java.lang.String extensionVersion) {
        super(extensionName);
        setExtensionVersion(extensionVersion);
    }

    public java.lang.String getExtensionVersion() {
        return extensionVersion;
    }

    public void setExtensionVersion(java.lang.String extensionVersion) {
        this.extensionVersion = extensionVersion;
    }

    private java.lang.String extensionVersion;
}