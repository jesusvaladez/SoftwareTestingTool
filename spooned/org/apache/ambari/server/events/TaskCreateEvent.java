package org.apache.ambari.server.events;
public class TaskCreateEvent extends org.apache.ambari.server.events.TaskEvent {
    public TaskCreateEvent(java.util.List<org.apache.ambari.server.actionmanager.HostRoleCommand> hostRoleCommandList) {
        super(hostRoleCommandList);
    }
}