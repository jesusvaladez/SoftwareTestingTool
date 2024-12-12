package org.apache.ambari.server.actionmanager;
@com.google.inject.Singleton
public class HostRoleCommandFactoryImpl implements org.apache.ambari.server.actionmanager.HostRoleCommandFactory {
    private com.google.inject.Injector injector;

    @com.google.inject.Inject
    public HostRoleCommandFactoryImpl(com.google.inject.Injector injector) {
        this.injector = injector;
    }

    @java.lang.Override
    public org.apache.ambari.server.actionmanager.HostRoleCommand create(java.lang.String hostName, org.apache.ambari.server.Role role, org.apache.ambari.server.state.ServiceComponentHostEvent event, org.apache.ambari.server.RoleCommand command) {
        return new org.apache.ambari.server.actionmanager.HostRoleCommand(hostName, role, event, command, injector.getInstance(org.apache.ambari.server.orm.dao.HostDAO.class), injector.getInstance(org.apache.ambari.server.orm.dao.ExecutionCommandDAO.class), injector.getInstance(org.apache.ambari.server.actionmanager.ExecutionCommandWrapperFactory.class));
    }

    @java.lang.Override
    public org.apache.ambari.server.actionmanager.HostRoleCommand create(java.lang.String hostName, org.apache.ambari.server.Role role, org.apache.ambari.server.state.ServiceComponentHostEvent event, org.apache.ambari.server.RoleCommand command, boolean retryAllowed, boolean autoSkipFailure) {
        return new org.apache.ambari.server.actionmanager.HostRoleCommand(hostName, role, event, command, retryAllowed, autoSkipFailure, injector.getInstance(org.apache.ambari.server.orm.dao.HostDAO.class), injector.getInstance(org.apache.ambari.server.orm.dao.ExecutionCommandDAO.class), injector.getInstance(org.apache.ambari.server.actionmanager.ExecutionCommandWrapperFactory.class));
    }

    @java.lang.Override
    public org.apache.ambari.server.actionmanager.HostRoleCommand create(org.apache.ambari.server.state.Host host, org.apache.ambari.server.Role role, org.apache.ambari.server.state.ServiceComponentHostEvent event, org.apache.ambari.server.RoleCommand command, boolean retryAllowed, boolean autoSkipFailure) {
        return new org.apache.ambari.server.actionmanager.HostRoleCommand(host, role, event, command, retryAllowed, autoSkipFailure, injector.getInstance(org.apache.ambari.server.orm.dao.HostDAO.class), injector.getInstance(org.apache.ambari.server.orm.dao.ExecutionCommandDAO.class), injector.getInstance(org.apache.ambari.server.actionmanager.ExecutionCommandWrapperFactory.class));
    }

    @java.lang.Override
    public org.apache.ambari.server.actionmanager.HostRoleCommand createExisting(org.apache.ambari.server.orm.entities.HostRoleCommandEntity hostRoleCommandEntity) {
        return new org.apache.ambari.server.actionmanager.HostRoleCommand(hostRoleCommandEntity, injector.getInstance(org.apache.ambari.server.orm.dao.HostDAO.class), injector.getInstance(org.apache.ambari.server.orm.dao.ExecutionCommandDAO.class), injector.getInstance(org.apache.ambari.server.actionmanager.ExecutionCommandWrapperFactory.class));
    }
}