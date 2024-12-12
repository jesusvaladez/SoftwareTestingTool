package org.apache.ambari.server.controller;
import io.swagger.annotations.ApiModelProperty;
public class RequestRequest {
    public RequestRequest(java.lang.String clusterName, long requestId) {
        this.clusterName = clusterName;
        this.requestId = requestId;
    }

    private java.lang.String clusterName;

    private long requestId;

    private org.apache.ambari.server.actionmanager.HostRoleStatus status;

    private java.lang.String abortReason;

    private boolean removePendingHostRequests = false;

    @io.swagger.annotations.ApiModelProperty(name = "request_status", notes = "Only valid value is ABORTED.")
    public org.apache.ambari.server.actionmanager.HostRoleStatus getStatus() {
        return status;
    }

    @io.swagger.annotations.ApiModelProperty(name = "cluster_name")
    public java.lang.String getClusterName() {
        return clusterName;
    }

    public void setClusterName(java.lang.String clusterName) {
        this.clusterName = clusterName;
    }

    @io.swagger.annotations.ApiModelProperty(name = "id")
    public long getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public void setStatus(org.apache.ambari.server.actionmanager.HostRoleStatus status) {
        this.status = status;
    }

    @io.swagger.annotations.ApiModelProperty(name = "abort_reason")
    public java.lang.String getAbortReason() {
        return abortReason;
    }

    public void setAbortReason(java.lang.String abortReason) {
        this.abortReason = abortReason;
    }

    public boolean isRemovePendingHostRequests() {
        return removePendingHostRequests;
    }

    public void setRemovePendingHostRequests(boolean removePendingHostRequests) {
        this.removePendingHostRequests = removePendingHostRequests;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return ((((((((((((("RequestRequest{" + "clusterName='") + clusterName) + '\'') + ", requestId=") + requestId) + ", status=") + status) + ", abortReason='") + abortReason) + '\'') + ", removePendingHostRequests='") + removePendingHostRequests) + '\'') + '}';
    }
}