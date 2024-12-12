package org.apache.ambari.server;
public class StateRecoveryManager {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.StateRecoveryManager.class);

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.HostVersionDAO hostVersionDAO;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.ServiceComponentDesiredStateDAO serviceComponentDAO;

    public void doWork() {
        checkHostAndClusterVersions();
    }

    void checkHostAndClusterVersions() {
        java.util.List<org.apache.ambari.server.orm.entities.HostVersionEntity> hostVersions = hostVersionDAO.findAll();
        for (org.apache.ambari.server.orm.entities.HostVersionEntity hostVersion : hostVersions) {
            if (hostVersion.getState().equals(org.apache.ambari.server.state.RepositoryVersionState.INSTALLING)) {
                hostVersion.setState(org.apache.ambari.server.state.RepositoryVersionState.INSTALL_FAILED);
                java.lang.String msg = java.lang.String.format("Recovered state of host version %s on host %s from %s to %s", hostVersion.getRepositoryVersion().getDisplayName(), hostVersion.getHostName(), org.apache.ambari.server.state.RepositoryVersionState.INSTALLING, org.apache.ambari.server.state.RepositoryVersionState.INSTALL_FAILED);
                org.apache.ambari.server.StateRecoveryManager.LOG.warn(msg);
                hostVersionDAO.merge(hostVersion);
            }
        }
        java.util.List<org.apache.ambari.server.orm.entities.ServiceComponentDesiredStateEntity> components = serviceComponentDAO.findAll();
        for (org.apache.ambari.server.orm.entities.ServiceComponentDesiredStateEntity component : components) {
            if (org.apache.ambari.server.state.RepositoryVersionState.INSTALLING == component.getRepositoryState()) {
                component.setRepositoryState(org.apache.ambari.server.state.RepositoryVersionState.INSTALL_FAILED);
                serviceComponentDAO.merge(component);
                java.lang.String msg = java.lang.String.format("Recovered state of cluster %s of component %s/%s for version %s from %s to %s", component.getClusterId(), component.getServiceName(), component.getComponentName(), component.getDesiredRepositoryVersion().getDisplayName(), org.apache.ambari.server.state.RepositoryVersionState.INSTALLING, org.apache.ambari.server.state.RepositoryVersionState.INSTALL_FAILED);
                org.apache.ambari.server.StateRecoveryManager.LOG.warn(msg);
            }
        }
    }
}