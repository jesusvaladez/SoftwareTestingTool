package org.apache.ambari.server.controller;
public class StackVersionRequest extends org.apache.ambari.server.controller.StackRequest {
    public StackVersionRequest(java.lang.String stackName, java.lang.String stackVersion) {
        super(stackName);
        setStackVersion(stackVersion);
    }

    public java.lang.String getStackVersion() {
        return stackVersion;
    }

    public void setStackVersion(java.lang.String stackVersion) {
        this.stackVersion = stackVersion;
    }

    private java.lang.String stackVersion;
}