package org.apache.ambari.server.utils;
public class CommandUtils {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.utils.CommandUtils.class);

    public static java.util.Map<java.lang.Long, org.apache.ambari.server.actionmanager.HostRoleCommand> convertToTaskIdCommandMap(java.util.Collection<org.apache.ambari.server.actionmanager.HostRoleCommand> commands) {
        if ((commands == null) || commands.isEmpty()) {
            return java.util.Collections.emptyMap();
        }
        java.util.Map<java.lang.Long, org.apache.ambari.server.actionmanager.HostRoleCommand> result = new java.util.HashMap<>();
        for (org.apache.ambari.server.actionmanager.HostRoleCommand command : commands) {
            result.put(command.getTaskId(), command);
        }
        return result;
    }
}