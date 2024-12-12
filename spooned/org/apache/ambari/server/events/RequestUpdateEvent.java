package org.apache.ambari.server.events;
@com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
public class RequestUpdateEvent extends org.apache.ambari.server.events.STOMPEvent {
    private java.lang.String clusterName;

    private java.lang.Long endTime;

    private java.lang.Long requestId;

    private java.lang.Double progressPercent;

    private java.lang.String requestContext;

    private org.apache.ambari.server.actionmanager.HostRoleStatus requestStatus;

    private java.lang.Long startTime;

    private java.lang.String userName;

    @com.fasterxml.jackson.annotation.JsonProperty("Tasks")
    private java.util.Set<org.apache.ambari.server.events.RequestUpdateEvent.HostRoleCommand> hostRoleCommands = new java.util.HashSet<>();

    public RequestUpdateEvent(org.apache.ambari.server.orm.entities.RequestEntity requestEntity, org.apache.ambari.server.orm.dao.HostRoleCommandDAO hostRoleCommandDAO, org.apache.ambari.server.topology.TopologyManager topologyManager, java.lang.String clusterName, java.util.List<org.apache.ambari.server.orm.entities.HostRoleCommandEntity> hostRoleCommandEntities) {
        super(org.apache.ambari.server.events.STOMPEvent.Type.REQUEST);
        this.clusterName = clusterName;
        this.endTime = requestEntity.getEndTime();
        this.requestId = requestEntity.getRequestId();
        this.progressPercent = org.apache.ambari.server.controller.internal.CalculatedStatus.statusFromRequest(hostRoleCommandDAO, topologyManager, requestEntity.getRequestId()).getPercent();
        this.requestContext = requestEntity.getRequestContext();
        this.requestStatus = requestEntity.getStatus();
        this.startTime = requestEntity.getStartTime();
        this.userName = requestEntity.getUserName();
        for (org.apache.ambari.server.orm.entities.HostRoleCommandEntity hostRoleCommandEntity : hostRoleCommandEntities) {
            hostRoleCommands.add(new org.apache.ambari.server.events.RequestUpdateEvent.HostRoleCommand(hostRoleCommandEntity.getTaskId(), hostRoleCommandEntity.getRequestId(), hostRoleCommandEntity.getStatus(), hostRoleCommandEntity.getHostName()));
        }
    }

    public RequestUpdateEvent(java.lang.Long requestId, org.apache.ambari.server.actionmanager.HostRoleStatus requestStatus, java.util.Set<org.apache.ambari.server.events.RequestUpdateEvent.HostRoleCommand> hostRoleCommands) {
        super(org.apache.ambari.server.events.STOMPEvent.Type.REQUEST);
        this.requestId = requestId;
        this.requestStatus = requestStatus;
        this.hostRoleCommands = hostRoleCommands;
    }

    public java.lang.Long getRequestId() {
        return requestId;
    }

    public void setRequestId(java.lang.Long requestId) {
        this.requestId = requestId;
    }

    public java.lang.String getClusterName() {
        return clusterName;
    }

    public void setClusterName(java.lang.String clusterName) {
        this.clusterName = clusterName;
    }

    public java.lang.String getRequestContext() {
        return requestContext;
    }

    public void setRequestContext(java.lang.String requestContext) {
        this.requestContext = requestContext;
    }

    public java.lang.Long getEndTime() {
        return endTime;
    }

    public void setEndTime(java.lang.Long endTime) {
        this.endTime = endTime;
    }

    public java.lang.Double getProgressPercent() {
        return progressPercent;
    }

    public void setProgressPercent(java.lang.Double progressPercent) {
        this.progressPercent = progressPercent;
    }

    public org.apache.ambari.server.actionmanager.HostRoleStatus getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(org.apache.ambari.server.actionmanager.HostRoleStatus requestStatus) {
        this.requestStatus = requestStatus;
    }

    public java.lang.Long getStartTime() {
        return startTime;
    }

    public void setStartTime(java.lang.Long startTime) {
        this.startTime = startTime;
    }

    public java.lang.String getUserName() {
        return userName;
    }

    public void setUserName(java.lang.String userName) {
        this.userName = userName;
    }

    public java.util.Set<org.apache.ambari.server.events.RequestUpdateEvent.HostRoleCommand> getHostRoleCommands() {
        return hostRoleCommands;
    }

    public void setHostRoleCommands(java.util.Set<org.apache.ambari.server.events.RequestUpdateEvent.HostRoleCommand> hostRoleCommands) {
        this.hostRoleCommands = hostRoleCommands;
    }

    public static class HostRoleCommand {
        private java.lang.Long id;

        private java.lang.Long requestId;

        private org.apache.ambari.server.actionmanager.HostRoleStatus status;

        private java.lang.String hostName;

        public HostRoleCommand(java.lang.Long id, java.lang.Long requestId, org.apache.ambari.server.actionmanager.HostRoleStatus status, java.lang.String hostName) {
            this.id = id;
            this.requestId = requestId;
            this.status = status;
            this.hostName = (hostName == null) ? org.apache.ambari.server.utils.StageUtils.getHostName() : hostName;
        }

        public java.lang.Long getId() {
            return id;
        }

        public void setId(java.lang.Long id) {
            this.id = id;
        }

        public java.lang.Long getRequestId() {
            return requestId;
        }

        public void setRequestId(java.lang.Long requestId) {
            this.requestId = requestId;
        }

        public org.apache.ambari.server.actionmanager.HostRoleStatus getStatus() {
            return status;
        }

        public void setStatus(org.apache.ambari.server.actionmanager.HostRoleStatus status) {
            this.status = status;
        }

        public java.lang.String getHostName() {
            return hostName;
        }

        public void setHostName(java.lang.String hostName) {
            this.hostName = hostName;
        }

        @java.lang.Override
        public boolean equals(java.lang.Object o) {
            if (this == o)
                return true;

            if ((o == null) || (getClass() != o.getClass()))
                return false;

            org.apache.ambari.server.events.RequestUpdateEvent.HostRoleCommand that = ((org.apache.ambari.server.events.RequestUpdateEvent.HostRoleCommand) (o));
            if (!id.equals(that.id))
                return false;

            if (!requestId.equals(that.requestId))
                return false;

            return hostName != null ? hostName.equals(that.hostName) : that.hostName == null;
        }

        @java.lang.Override
        public int hashCode() {
            int result = id.hashCode();
            result = (31 * result) + requestId.hashCode();
            result = (31 * result) + (hostName != null ? hostName.hashCode() : 0);
            return result;
        }
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o)
            return true;

        if ((o == null) || (getClass() != o.getClass()))
            return false;

        org.apache.ambari.server.events.RequestUpdateEvent that = ((org.apache.ambari.server.events.RequestUpdateEvent) (o));
        if (clusterName != null ? !clusterName.equals(that.clusterName) : that.clusterName != null)
            return false;

        if (endTime != null ? !endTime.equals(that.endTime) : that.endTime != null)
            return false;

        if (requestId != null ? !requestId.equals(that.requestId) : that.requestId != null)
            return false;

        if (progressPercent != null ? !progressPercent.equals(that.progressPercent) : that.progressPercent != null)
            return false;

        if (requestContext != null ? !requestContext.equals(that.requestContext) : that.requestContext != null)
            return false;

        if (requestStatus != that.requestStatus)
            return false;

        if (startTime != null ? !startTime.equals(that.startTime) : that.startTime != null)
            return false;

        if (userName != null ? !userName.equals(that.userName) : that.userName != null)
            return false;

        return hostRoleCommands != null ? hostRoleCommands.equals(that.hostRoleCommands) : that.hostRoleCommands == null;
    }

    @java.lang.Override
    public int hashCode() {
        int result = (clusterName != null) ? clusterName.hashCode() : 0;
        result = (31 * result) + (endTime != null ? endTime.hashCode() : 0);
        result = (31 * result) + (requestId != null ? requestId.hashCode() : 0);
        result = (31 * result) + (progressPercent != null ? progressPercent.hashCode() : 0);
        result = (31 * result) + (requestContext != null ? requestContext.hashCode() : 0);
        result = (31 * result) + (requestStatus != null ? requestStatus.hashCode() : 0);
        result = (31 * result) + (startTime != null ? startTime.hashCode() : 0);
        result = (31 * result) + (userName != null ? userName.hashCode() : 0);
        result = (31 * result) + (hostRoleCommands != null ? hostRoleCommands.hashCode() : 0);
        return result;
    }
}