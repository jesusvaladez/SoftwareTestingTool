package org.apache.ambari.server.controller;
public class StackRequest {
    public StackRequest(java.lang.String stackName) {
        this.setStackName(stackName);
    }

    public java.lang.String getStackName() {
        return stackName;
    }

    public void setStackName(java.lang.String stackName) {
        this.stackName = stackName;
    }

    private java.lang.String stackName;
}