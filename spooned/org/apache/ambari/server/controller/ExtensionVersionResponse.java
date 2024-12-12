package org.apache.ambari.server.controller;
public class ExtensionVersionResponse implements org.apache.ambari.server.stack.Validable {
    private java.lang.String extensionName;

    private java.lang.String extensionVersion;

    private boolean valid;

    private java.lang.String parentVersion;

    public ExtensionVersionResponse(java.lang.String extensionVersion, java.lang.String parentVersion, boolean valid, java.util.Collection<java.lang.String> errorSet) {
        setExtensionVersion(extensionVersion);
        setParentVersion(parentVersion);
        setValid(valid);
        addErrors(errorSet);
    }

    @java.lang.Override
    public boolean isValid() {
        return valid;
    }

    @java.lang.Override
    public void setValid(boolean valid) {
        this.valid = valid;
    }

    private java.util.Set<java.lang.String> errorSet = new java.util.HashSet<>();

    @java.lang.Override
    public void addError(java.lang.String error) {
        errorSet.add(error);
    }

    @java.lang.Override
    public java.util.Collection<java.lang.String> getErrors() {
        return errorSet;
    }

    @java.lang.Override
    public void addErrors(java.util.Collection<java.lang.String> errors) {
        this.errorSet.addAll(errors);
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

    public java.lang.String getParentVersion() {
        return parentVersion;
    }

    public void setParentVersion(java.lang.String parentVersion) {
        this.parentVersion = parentVersion;
    }
}