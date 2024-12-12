package org.apache.ambari.server.events;
import org.apache.commons.lang.StringUtils;
public class TaskEvent {
    private java.util.List<org.apache.ambari.server.actionmanager.HostRoleCommand> hostRoleCommands;

    public TaskEvent(java.util.List<org.apache.ambari.server.actionmanager.HostRoleCommand> hostRoleCommands) {
        this.hostRoleCommands = hostRoleCommands;
    }

    public java.util.List<org.apache.ambari.server.actionmanager.HostRoleCommand> getHostRoleCommands() {
        return hostRoleCommands;
    }

    @java.lang.Override
    public java.lang.String toString() {
        java.lang.String hostRoleCommands = org.apache.commons.lang.StringUtils.join(this.hostRoleCommands, ", ");
        java.lang.StringBuilder buffer = new java.lang.StringBuilder("TaskEvent{");
        buffer.append("hostRoleCommands=").append(hostRoleCommands);
        buffer.append("}");
        return buffer.toString();
    }
}