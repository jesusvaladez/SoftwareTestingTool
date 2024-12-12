package org.apache.ambari.server.controller;
public class ExtensionRequest {
    public ExtensionRequest(java.lang.String extensionName) {
        this.setExtensionName(extensionName);
    }

    public java.lang.String getExtensionName() {
        return extensionName;
    }

    public void setExtensionName(java.lang.String extensionName) {
        this.extensionName = extensionName;
    }

    private java.lang.String extensionName;
}