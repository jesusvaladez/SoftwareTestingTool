package org.apache.ambari.server.actionmanager;
public interface HostRoleCommandFactory {
    org.apache.ambari.server.actionmanager.HostRoleCommand create(java.lang.String hostName, org.apache.ambari.server.Role role, org.apache.ambari.server.state.ServiceComponentHostEvent event, org.apache.ambari.server.RoleCommand command);

    org.apache.ambari.server.actionmanager.HostRoleCommand create(java.lang.String hostName, org.apache.ambari.server.Role role, org.apache.ambari.server.state.ServiceComponentHostEvent event, org.apache.ambari.server.RoleCommand command, boolean retryAllowed, boolean autoSkipFailure);

    org.apache.ambari.server.actionmanager.HostRoleCommand create(org.apache.ambari.server.state.Host host, org.apache.ambari.server.Role role, org.apache.ambari.server.state.ServiceComponentHostEvent event, org.apache.ambari.server.RoleCommand command, boolean retryAllowed, boolean autoSkipFailure);

    org.apache.ambari.server.actionmanager.HostRoleCommand createExisting(org.apache.ambari.server.orm.entities.HostRoleCommandEntity hostRoleCommandEntity);
}