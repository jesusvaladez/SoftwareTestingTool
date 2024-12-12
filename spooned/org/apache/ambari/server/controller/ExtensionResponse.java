package org.apache.ambari.server.controller;
public class ExtensionResponse {
    private java.lang.String extensionName;

    public ExtensionResponse(java.lang.String extensionName) {
        setExtensionName(extensionName);
    }

    public java.lang.String getExtensionName() {
        return extensionName;
    }

    public void setExtensionName(java.lang.String extensionName) {
        this.extensionName = extensionName;
    }

    @java.lang.Override
    public int hashCode() {
        int result;
        result = 31 + getExtensionName().hashCode();
        return result;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object obj) {
        if (!(obj instanceof org.apache.ambari.server.controller.ExtensionResponse)) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        org.apache.ambari.server.controller.ExtensionResponse extensionResponse = ((org.apache.ambari.server.controller.ExtensionResponse) (obj));
        return getExtensionName().equals(extensionResponse.getExtensionName());
    }
}