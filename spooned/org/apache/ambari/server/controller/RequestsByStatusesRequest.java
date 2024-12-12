package org.apache.ambari.server.controller;
public class RequestsByStatusesRequest {
    java.util.Set<java.lang.String> statuses;

    public RequestsByStatusesRequest() {
        statuses = new java.util.HashSet<>();
        statuses.add(org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING.toString());
        statuses.add(org.apache.ambari.server.actionmanager.HostRoleStatus.QUEUED.toString());
        statuses.add(org.apache.ambari.server.actionmanager.HostRoleStatus.IN_PROGRESS.toString());
    }

    public RequestsByStatusesRequest(java.util.Set<java.lang.String> statuses) {
        this.statuses = statuses;
    }

    public java.util.Set<java.lang.String> getStatuses() {
        return statuses;
    }

    public void setStatuses(java.util.Set<java.lang.String> statuses) {
        this.statuses = statuses;
    }
}