package org.apache.ambari.server.events.listeners.upgrade;
import org.apache.commons.lang.StringUtils;
@com.google.inject.Singleton
@org.apache.ambari.server.EagerSingleton
public class DistributeRepositoriesActionListener {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.events.listeners.upgrade.DistributeRepositoriesActionListener.class);

    public static final java.lang.String INSTALL_PACKAGES = "install_packages";

    @com.google.inject.Inject
    private com.google.inject.Provider<org.apache.ambari.server.orm.dao.HostVersionDAO> hostVersionDAO;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.RepositoryVersionDAO repoVersionDAO;

    @com.google.inject.Inject
    private com.google.gson.Gson gson;

    @com.google.inject.Inject
    public DistributeRepositoriesActionListener(org.apache.ambari.server.events.publishers.AmbariEventPublisher publisher) {
        publisher.register(this);
    }

    @com.google.common.eventbus.Subscribe
    public void onActionFinished(org.apache.ambari.server.events.ActionFinalReportReceivedEvent event) {
        if (!event.getRole().equals(org.apache.ambari.server.events.listeners.upgrade.DistributeRepositoriesActionListener.INSTALL_PACKAGES)) {
            return;
        }
        if (org.apache.ambari.server.events.listeners.upgrade.DistributeRepositoriesActionListener.LOG.isDebugEnabled()) {
            org.apache.ambari.server.events.listeners.upgrade.DistributeRepositoriesActionListener.LOG.debug(event.toString());
        }
        java.lang.Long clusterId = event.getClusterId();
        if (clusterId == null) {
            org.apache.ambari.server.events.listeners.upgrade.DistributeRepositoriesActionListener.LOG.error("Distribute Repositories expected a cluster Id for host " + event.getHostname());
            return;
        }
        java.lang.String repositoryVersion = null;
        org.apache.ambari.server.state.RepositoryVersionState newHostState = org.apache.ambari.server.state.RepositoryVersionState.INSTALL_FAILED;
        if (event.getCommandReport() == null) {
            org.apache.ambari.server.events.listeners.upgrade.DistributeRepositoriesActionListener.LOG.error("Command report is null, will set all INSTALLING versions for host {} to INSTALL_FAILED.", event.getHostname());
        } else if (!event.getCommandReport().getStatus().equals(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED.toString())) {
            org.apache.ambari.server.events.listeners.upgrade.DistributeRepositoriesActionListener.LOG.warn("Distribute repositories did not complete, will set all INSTALLING versions for host {} to INSTALL_FAILED.", event.getHostname());
        } else {
            org.apache.ambari.server.events.listeners.upgrade.DistributeRepositoriesActionListener.DistributeRepositoriesStructuredOutput structuredOutput = null;
            try {
                structuredOutput = gson.fromJson(event.getCommandReport().getStructuredOut(), org.apache.ambari.server.events.listeners.upgrade.DistributeRepositoriesActionListener.DistributeRepositoriesStructuredOutput.class);
            } catch (com.google.gson.JsonSyntaxException e) {
                org.apache.ambari.server.events.listeners.upgrade.DistributeRepositoriesActionListener.LOG.error("Cannot parse structured output %s", e);
            }
            if ((null == structuredOutput) || (null == structuredOutput.repositoryVersionId)) {
                org.apache.ambari.server.events.listeners.upgrade.DistributeRepositoriesActionListener.LOG.error("Received an installation reponse, but it did not contain a repository version id");
            } else {
                newHostState = org.apache.ambari.server.state.RepositoryVersionState.INSTALLED;
                java.lang.String actualVersion = structuredOutput.actualVersion;
                org.apache.ambari.server.orm.entities.RepositoryVersionEntity repoVersion = repoVersionDAO.findByPK(structuredOutput.repositoryVersionId);
                if ((null != repoVersion) && org.apache.commons.lang.StringUtils.isNotBlank(actualVersion)) {
                    if (!org.apache.commons.lang.StringUtils.equals(repoVersion.getVersion(), actualVersion)) {
                        repoVersion.setVersion(actualVersion);
                        repoVersion.setResolved(true);
                        repoVersionDAO.merge(repoVersion);
                        repositoryVersion = actualVersion;
                    } else if (!repoVersion.isResolved()) {
                        repoVersion.setResolved(true);
                        repoVersionDAO.merge(repoVersion);
                    }
                }
            }
        }
        java.util.List<org.apache.ambari.server.orm.entities.HostVersionEntity> hostVersions = hostVersionDAO.get().findByHost(event.getHostname());
        for (org.apache.ambari.server.orm.entities.HostVersionEntity hostVersion : hostVersions) {
            if ((!event.isEmulated()) && (!((repositoryVersion == null) || hostVersion.getRepositoryVersion().getVersion().equals(repositoryVersion)))) {
                continue;
            }
            if (hostVersion.getState() == org.apache.ambari.server.state.RepositoryVersionState.INSTALLING) {
                hostVersion.setState(newHostState);
                hostVersionDAO.get().merge(hostVersion);
            }
        }
    }

    private static class DistributeRepositoriesStructuredOutput {
        @com.google.gson.annotations.SerializedName("package_installation_result")
        private java.lang.String packageInstallationResult;

        @com.google.gson.annotations.SerializedName("actual_version")
        private java.lang.String actualVersion;

        @com.google.gson.annotations.SerializedName("repository_version_id")
        private java.lang.Long repositoryVersionId = null;
    }
}