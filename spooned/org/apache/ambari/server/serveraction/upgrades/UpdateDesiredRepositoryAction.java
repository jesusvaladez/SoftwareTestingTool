package org.apache.ambari.server.serveraction.upgrades;
import com.google.inject.persist.Transactional;
import org.apache.commons.lang.StringUtils;
public class UpdateDesiredRepositoryAction extends org.apache.ambari.server.serveraction.upgrades.AbstractUpgradeServerAction {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.serveraction.upgrades.UpdateDesiredRepositoryAction.class);

    @com.google.inject.Inject
    private org.apache.ambari.server.configuration.Configuration m_configuration;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.HostVersionDAO m_hostVersionDAO;

    @java.lang.Override
    public org.apache.ambari.server.agent.CommandReport execute(java.util.concurrent.ConcurrentMap<java.lang.String, java.lang.Object> requestSharedDataContext) throws org.apache.ambari.server.AmbariException, java.lang.InterruptedException {
        java.lang.String clusterName = getExecutionCommand().getClusterName();
        org.apache.ambari.server.state.Cluster cluster = getClusters().getCluster(clusterName);
        org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext upgradeContext = getUpgradeContext(cluster);
        java.util.Map<java.lang.String, java.lang.String> roleParams = getExecutionCommand().getRoleParams();
        java.lang.String userName;
        if ((roleParams != null) && roleParams.containsKey(org.apache.ambari.server.serveraction.ServerAction.ACTION_USER_NAME)) {
            userName = roleParams.get(org.apache.ambari.server.serveraction.ServerAction.ACTION_USER_NAME);
        } else {
            userName = m_configuration.getAnonymousAuditName();
            org.apache.ambari.server.serveraction.upgrades.UpdateDesiredRepositoryAction.LOG.warn(java.lang.String.format("Did not receive role parameter %s, will save configs using anonymous username %s", org.apache.ambari.server.serveraction.ServerAction.ACTION_USER_NAME, userName));
        }
        org.apache.ambari.server.agent.CommandReport commandReport = updateDesiredRepositoryVersion(cluster, upgradeContext, userName);
        m_upgradeHelper.publishDesiredRepositoriesUpdates(upgradeContext);
        return commandReport;
    }

    @com.google.inject.persist.Transactional
    org.apache.ambari.server.agent.CommandReport updateDesiredRepositoryVersion(org.apache.ambari.server.state.Cluster cluster, org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext upgradeContext, java.lang.String userName) throws org.apache.ambari.server.AmbariException, java.lang.InterruptedException {
        java.lang.StringBuilder out = new java.lang.StringBuilder();
        java.lang.StringBuilder err = new java.lang.StringBuilder();
        try {
            if (upgradeContext.getDirection() == org.apache.ambari.server.stack.upgrade.Direction.UPGRADE) {
                final java.lang.String message;
                org.apache.ambari.server.orm.entities.RepositoryVersionEntity targetRepositoryVersion = upgradeContext.getRepositoryVersion();
                if (upgradeContext.getOrchestrationType() == org.apache.ambari.spi.RepositoryType.STANDARD) {
                    message = java.text.MessageFormat.format("Updating the desired repository version to {0} for all cluster services.", targetRepositoryVersion.getVersion());
                } else {
                    java.util.Set<java.lang.String> servicesInUpgrade = upgradeContext.getSupportedServices();
                    message = java.text.MessageFormat.format("Updating the desired repository version to {0} for the following services: {1}", targetRepositoryVersion.getVersion(), org.apache.commons.lang.StringUtils.join(servicesInUpgrade, ','));
                }
                out.append(message).append(java.lang.System.lineSeparator());
                org.apache.ambari.server.state.StackId targetStackId = targetRepositoryVersion.getStackId();
                cluster.setDesiredStackVersion(targetStackId);
            }
            if (upgradeContext.getDirection() == org.apache.ambari.server.stack.upgrade.Direction.DOWNGRADE) {
                java.lang.String message = "Updating the desired repository back their original values for the following services:";
                out.append(message).append(java.lang.System.lineSeparator());
                java.util.Map<java.lang.String, org.apache.ambari.server.orm.entities.RepositoryVersionEntity> targetVersionsByService = upgradeContext.getTargetVersions();
                for (java.lang.String serviceName : targetVersionsByService.keySet()) {
                    org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion = targetVersionsByService.get(serviceName);
                    message = java.lang.String.format("  %s to %s", serviceName, repositoryVersion.getVersion());
                    out.append(message).append(java.lang.System.lineSeparator());
                }
            }
            m_upgradeHelper.updateDesiredRepositoriesAndConfigs(upgradeContext);
            if (upgradeContext.getDirection() == org.apache.ambari.server.stack.upgrade.Direction.DOWNGRADE) {
                org.apache.ambari.server.orm.entities.RepositoryVersionEntity downgradeFromRepositoryVersion = upgradeContext.getRepositoryVersion();
                out.append(java.lang.String.format("Setting host versions back to %s for repository version %s", org.apache.ambari.server.state.RepositoryVersionState.INSTALLED, downgradeFromRepositoryVersion.getVersion()));
                java.util.List<org.apache.ambari.server.orm.entities.HostVersionEntity> hostVersionsToReset = m_hostVersionDAO.findHostVersionByClusterAndRepository(cluster.getClusterId(), downgradeFromRepositoryVersion);
                for (org.apache.ambari.server.orm.entities.HostVersionEntity hostVersion : hostVersionsToReset) {
                    if (hostVersion.getState() != org.apache.ambari.server.state.RepositoryVersionState.NOT_REQUIRED) {
                        hostVersion.setState(org.apache.ambari.server.state.RepositoryVersionState.INSTALLED);
                    }
                }
                org.apache.ambari.server.state.StackId targetStackId = cluster.getCurrentStackVersion();
                cluster.setDesiredStackVersion(targetStackId);
            }
            return createCommandReport(0, org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, "{}", out.toString(), err.toString());
        } catch (java.lang.Exception e) {
            java.io.StringWriter sw = new java.io.StringWriter();
            e.printStackTrace(new java.io.PrintWriter(sw));
            err.append(sw);
            return createCommandReport(-1, org.apache.ambari.server.actionmanager.HostRoleStatus.FAILED, "{}", out.toString(), err.toString());
        }
    }
}