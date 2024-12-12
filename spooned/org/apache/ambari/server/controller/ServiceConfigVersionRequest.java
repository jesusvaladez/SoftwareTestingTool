package org.apache.ambari.server.controller;
import io.swagger.annotations.ApiModelProperty;
public class ServiceConfigVersionRequest {
    private java.lang.String clusterName;

    private java.lang.String serviceName;

    private java.lang.Long version;

    private java.lang.Long createTime;

    private java.lang.Long applyTime;

    private java.lang.String userName;

    private java.lang.String note;

    private java.lang.Boolean isCurrent;

    public ServiceConfigVersionRequest() {
    }

    public ServiceConfigVersionRequest(java.lang.String clusterName, java.lang.String serviceName, java.lang.Long version, java.lang.Long createTime, java.lang.Long applyTime, java.lang.String userName, java.lang.Boolean isCurrent) {
        this.clusterName = clusterName;
        this.serviceName = serviceName;
        this.version = version;
        this.createTime = createTime;
        this.applyTime = applyTime;
        this.userName = userName;
        this.isCurrent = isCurrent;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ServiceConfigVersionResourceProvider.SERVICE_NAME_PROPERTY_ID)
    public java.lang.String getServiceName() {
        return serviceName;
    }

    public void setServiceName(java.lang.String serviceName) {
        this.serviceName = serviceName;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ServiceConfigVersionResourceProvider.SERVICE_CONFIG_VERSION_PROPERTY_ID)
    public java.lang.Long getVersion() {
        return version;
    }

    public void setVersion(java.lang.Long version) {
        this.version = version;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ServiceConfigVersionResourceProvider.CREATE_TIME_PROPERTY_ID)
    public java.lang.Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(java.lang.Long createTime) {
        this.createTime = createTime;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ServiceConfigVersionResourceProvider.APPLIED_TIME_PROPERTY_ID)
    public java.lang.Long getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(java.lang.Long applyTime) {
        this.applyTime = applyTime;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ServiceConfigVersionResourceProvider.USER_PROPERTY_ID)
    public java.lang.String getUserName() {
        return userName;
    }

    public void setUserName(java.lang.String userName) {
        this.userName = userName;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ServiceConfigVersionResourceProvider.CLUSTER_NAME_PROPERTY_ID)
    public java.lang.String getClusterName() {
        return clusterName;
    }

    public void setClusterName(java.lang.String clusterName) {
        this.clusterName = clusterName;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ServiceConfigVersionResourceProvider.SERVICE_CONFIG_VERSION_NOTE_PROPERTY_ID)
    public java.lang.String getNote() {
        return note;
    }

    public void setNote(java.lang.String note) {
        this.note = note;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ServiceConfigVersionResourceProvider.IS_CURRENT_PROPERTY_ID)
    public java.lang.Boolean getIsCurrent() {
        return isCurrent;
    }

    public void setIsCurrent(java.lang.Boolean isCurrent) {
        this.isCurrent = isCurrent;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return (((((((((((((((((((("ServiceConfigVersionRequest{" + "clusterName='") + clusterName) + '\'') + ", serviceName='") + serviceName) + '\'') + ", version=") + version) + ", createTime=") + createTime) + ", applyTime=") + applyTime) + ", userName='") + userName) + '\'') + ", note='") + note) + '\'') + ", isCurrent=") + isCurrent) + '}';
    }
}