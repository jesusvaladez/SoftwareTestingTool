package org.apache.ambari.server.agent;
public class ComponentStatus {
    private java.lang.String componentName;

    private java.lang.String msg;

    private java.lang.String status;

    private java.lang.String sendExecCmdDet = "False";

    private java.lang.String serviceName;

    private java.lang.Long clusterId;

    private java.lang.String stackVersion;

    private java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configurationTags;

    private java.util.Map<java.lang.String, java.lang.Object> extra;

    public java.lang.String getComponentName() {
        return componentName;
    }

    public void setComponentName(java.lang.String componentName) {
        this.componentName = componentName;
    }

    public java.lang.String getMessage() {
        return msg;
    }

    public void setMessage(java.lang.String msg) {
        this.msg = msg;
    }

    public java.lang.String getStatus() {
        return status;
    }

    public void setSendExecCmdDet(java.lang.String sendExecCmdDet) {
        this.sendExecCmdDet = sendExecCmdDet;
    }

    public java.lang.String getSendExecCmdDet() {
        return this.sendExecCmdDet;
    }

    public void setStatus(java.lang.String status) {
        this.status = status;
    }

    public java.lang.String getStackVersion() {
        return stackVersion;
    }

    public void setStackVersion(java.lang.String stackVersion) {
        this.stackVersion = stackVersion;
    }

    public java.lang.String getMsg() {
        return msg;
    }

    public void setMsg(java.lang.String msg) {
        this.msg = msg;
    }

    public java.lang.String getServiceName() {
        return serviceName;
    }

    public void setServiceName(java.lang.String serviceName) {
        this.serviceName = serviceName;
    }

    public java.lang.Long getClusterId() {
        return clusterId;
    }

    public void setClusterId(java.lang.Long clusterId) {
        this.clusterId = clusterId;
    }

    public void setConfigTags(java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> tags) {
        configurationTags = tags;
    }

    public java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> getConfigTags() {
        return configurationTags;
    }

    public void setExtra(java.util.Map<java.lang.String, java.lang.Object> info) {
        extra = info;
    }

    public java.util.Map<java.lang.String, java.lang.Object> getExtra() {
        return extra;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return ((((((((((((((("ComponentStatus [componentName=" + componentName) + ", msg=") + msg) + ", status=") + status) + ", serviceName=") + serviceName) + ", clusterId=") + clusterId) + ", stackVersion=") + stackVersion) + ", configurationTags=") + configurationTags) + ", extra=") + extra) + "]";
    }
}