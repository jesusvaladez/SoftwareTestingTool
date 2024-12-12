package org.apache.ambari.server.events;
public class TaskUpdateEvent extends org.apache.ambari.server.events.TaskEvent {
    public TaskUpdateEvent(java.util.List<org.apache.ambari.server.actionmanager.HostRoleCommand> hostRoleCommandList) {
        super(hostRoleCommandList);
    }
}