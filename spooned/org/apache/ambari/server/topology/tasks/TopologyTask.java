package org.apache.ambari.server.topology.tasks;
public interface TopologyTask extends java.lang.Runnable {
    enum Type {

        RESOURCE_CREATION,
        CONFIGURE,
        INSTALL,
        START() {
            @java.lang.Override
            public java.util.Set<org.apache.ambari.server.RoleCommand> tasksToAbortOnFailure() {
                return com.google.common.collect.ImmutableSet.of(org.apache.ambari.server.RoleCommand.START);
            }
        };
        private static java.util.Set<org.apache.ambari.server.RoleCommand> ALL_TASKS = com.google.common.collect.ImmutableSet.of(org.apache.ambari.server.RoleCommand.INSTALL, org.apache.ambari.server.RoleCommand.START);

        public java.util.Set<org.apache.ambari.server.RoleCommand> tasksToAbortOnFailure() {
            return org.apache.ambari.server.topology.tasks.TopologyTask.Type.ALL_TASKS;
        }
    }

    org.apache.ambari.server.topology.tasks.TopologyTask.Type getType();
}