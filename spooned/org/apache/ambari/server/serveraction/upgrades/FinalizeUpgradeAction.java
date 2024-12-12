package org.apache.ambari.server.serveraction.upgrades;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.text.StrBuilder;
public class FinalizeUpgradeAction extends org.apache.ambari.server.serveraction.upgrades.AbstractUpgradeServerAction {
    public static final java.lang.String PREVIOUS_UPGRADE_NOT_COMPLETED_MSG = "It is possible that a previous upgrade was not finalized. " + "For this reason, Ambari will not remove any configs. Please ensure that all database records are correct.";

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.HostVersionDAO hostVersionDAO;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.HostComponentStateDAO hostComponentStateDAO;

    @com.google.inject.Inject
    private org.apache.ambari.server.api.services.AmbariMetaInfo ambariMetaInfo;

    @com.google.inject.Inject
    private org.apache.ambari.server.events.publishers.VersionEventPublisher versionEventPublisher;

    @java.lang.Override
    public org.apache.ambari.server.agent.CommandReport execute(java.util.concurrent.ConcurrentMap<java.lang.String, java.lang.Object> requestSharedDataContext) throws org.apache.ambari.server.AmbariException, java.lang.InterruptedException {
        java.lang.String clusterName = getExecutionCommand().getClusterName();
        org.apache.ambari.server.state.Cluster cluster = getClusters().getCluster(clusterName);
        org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext upgradeContext = getUpgradeContext(cluster);
        if (upgradeContext.getDirection() == org.apache.ambari.server.stack.upgrade.Direction.UPGRADE) {
            return finalizeUpgrade(upgradeContext);
        } else {
            return finalizeDowngrade(upgradeContext);
        }
    }

    private org.apache.ambari.server.agent.CommandReport finalizeUpgrade(org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext upgradeContext) throws org.apache.ambari.server.AmbariException, java.lang.InterruptedException {
        org.apache.ambari.server.stack.upgrade.Direction direction = upgradeContext.getDirection();
        org.apache.ambari.spi.RepositoryType repositoryType = upgradeContext.getOrchestrationType();
        java.lang.StringBuilder outSB = new java.lang.StringBuilder();
        java.lang.StringBuilder errSB = new java.lang.StringBuilder();
        try {
            org.apache.ambari.server.state.Cluster cluster = upgradeContext.getCluster();
            org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion = upgradeContext.getRepositoryVersion();
            java.lang.String version = repositoryVersion.getVersion();
            java.lang.String message;
            if (upgradeContext.getOrchestrationType() == org.apache.ambari.spi.RepositoryType.STANDARD) {
                message = java.text.MessageFormat.format("Finalizing the upgrade to {0} for all cluster services.", version);
            } else {
                java.util.Set<java.lang.String> servicesInUpgrade = upgradeContext.getSupportedServices();
                message = java.text.MessageFormat.format("Finalizing the upgrade to {0} for the following services: {1}", version, org.apache.commons.lang.StringUtils.join(servicesInUpgrade, ','));
            }
            outSB.append(message).append(java.lang.System.lineSeparator());
            java.util.Set<org.apache.ambari.server.serveraction.upgrades.FinalizeUpgradeAction.InfoTuple> errors = validateComponentVersions(upgradeContext);
            if (!errors.isEmpty()) {
                org.apache.commons.lang.text.StrBuilder messageBuff = new org.apache.commons.lang.text.StrBuilder(java.lang.String.format("The following %d host component(s) " + ("have not been upgraded to version %s. Please install and upgrade " + "the Stack Version on those hosts and try again.\nHost components:"), errors.size(), version)).append(java.lang.System.lineSeparator());
                for (org.apache.ambari.server.serveraction.upgrades.FinalizeUpgradeAction.InfoTuple error : errors) {
                    messageBuff.append(java.lang.String.format("%s on host %s\n", error.componentName, error.hostName));
                }
                throw new org.apache.ambari.server.AmbariException(messageBuff.toString());
            }
            java.util.List<org.apache.ambari.server.orm.entities.HostVersionEntity> hostVersions = hostVersionDAO.findHostVersionByClusterAndRepository(cluster.getClusterId(), repositoryVersion);
            java.util.Set<org.apache.ambari.server.orm.entities.HostVersionEntity> hostVersionsAllowed = new java.util.HashSet<>();
            java.util.Set<java.lang.String> hostsWithoutCorrectVersionState = new java.util.HashSet<>();
            for (org.apache.ambari.server.orm.entities.HostVersionEntity hostVersion : hostVersions) {
                org.apache.ambari.server.state.RepositoryVersionState hostVersionState = hostVersion.getState();
                switch (hostVersionState) {
                    case CURRENT :
                    case NOT_REQUIRED :
                        {
                            hostVersionsAllowed.add(hostVersion);
                            break;
                        }
                    default :
                        {
                            hostsWithoutCorrectVersionState.add(hostVersion.getHostName());
                            break;
                        }
                }
            }
            if (hostsWithoutCorrectVersionState.size() > 0) {
                message = java.lang.String.format("The following %d host(s) have not been upgraded to version %s. " + "Please install and upgrade the Stack Version on those hosts and try again.\nHosts: %s", hostsWithoutCorrectVersionState.size(), version, org.apache.commons.lang.StringUtils.join(hostsWithoutCorrectVersionState, ", "));
                outSB.append(message);
                outSB.append(java.lang.System.lineSeparator());
                throw new org.apache.ambari.server.AmbariException(message);
            }
            outSB.append(java.lang.String.format("Finalizing the upgrade state and repository version for %d host(s).", hostVersionsAllowed.size())).append(java.lang.System.lineSeparator());
            for (org.apache.ambari.server.orm.entities.HostVersionEntity hostVersion : hostVersionsAllowed) {
                java.util.Collection<org.apache.ambari.server.orm.entities.HostComponentStateEntity> hostComponentStates = hostComponentStateDAO.findByHost(hostVersion.getHostName());
                for (org.apache.ambari.server.orm.entities.HostComponentStateEntity hostComponentStateEntity : hostComponentStates) {
                    hostComponentStateEntity.setUpgradeState(org.apache.ambari.server.state.UpgradeState.NONE);
                    hostComponentStateDAO.merge(hostComponentStateEntity);
                }
            }
            finalizeHostVersionsNotDesired(cluster, upgradeContext);
            if (upgradeContext.getOrchestrationType() == org.apache.ambari.spi.RepositoryType.STANDARD) {
                outSB.append(java.lang.String.format("Finalizing the version for cluster %s.\n", cluster.getClusterName()));
                cluster.setCurrentStackVersion(cluster.getDesiredStackVersion());
            }
            if (repositoryType.isRevertable() && (direction == org.apache.ambari.server.stack.upgrade.Direction.UPGRADE)) {
                org.apache.ambari.server.orm.entities.UpgradeEntity upgradeEntity = cluster.getUpgradeInProgress();
                upgradeEntity.setRevertAllowed(true);
                upgradeEntity = m_upgradeDAO.merge(upgradeEntity);
            }
            cluster.setUpgradeEntity(null);
            versionEventPublisher.publish(new org.apache.ambari.server.events.StackUpgradeFinishEvent(cluster));
            message = java.lang.String.format("The upgrade to %s has completed.", version);
            outSB.append(message).append(java.lang.System.lineSeparator());
            return createCommandReport(0, org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, "{}", outSB.toString(), errSB.toString());
        } catch (java.lang.Exception e) {
            errSB.append(e.getMessage());
            return createCommandReport(-1, org.apache.ambari.server.actionmanager.HostRoleStatus.FAILED, "{}", outSB.toString(), errSB.toString());
        }
    }

    private org.apache.ambari.server.agent.CommandReport finalizeDowngrade(org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext upgradeContext) throws org.apache.ambari.server.AmbariException, java.lang.InterruptedException {
        java.lang.StringBuilder outSB = new java.lang.StringBuilder();
        java.lang.StringBuilder errSB = new java.lang.StringBuilder();
        try {
            org.apache.ambari.server.state.Cluster cluster = upgradeContext.getCluster();
            org.apache.ambari.server.orm.entities.RepositoryVersionEntity downgradeFromRepositoryVersion = upgradeContext.getRepositoryVersion();
            java.lang.String downgradeFromVersion = downgradeFromRepositoryVersion.getVersion();
            java.util.Set<java.lang.String> servicesInUpgrade = upgradeContext.getSupportedServices();
            java.lang.String message;
            if (upgradeContext.getOrchestrationType() == org.apache.ambari.spi.RepositoryType.STANDARD) {
                message = java.text.MessageFormat.format("Finalizing the downgrade from {0} for all cluster services.", downgradeFromVersion);
            } else {
                message = java.text.MessageFormat.format("Finalizing the downgrade from {0} for the following services: {1}", downgradeFromVersion, org.apache.commons.lang.StringUtils.join(servicesInUpgrade, ','));
            }
            outSB.append(message).append(java.lang.System.lineSeparator());
            java.util.Set<org.apache.ambari.server.serveraction.upgrades.FinalizeUpgradeAction.InfoTuple> errors = validateComponentVersions(upgradeContext);
            if (!errors.isEmpty()) {
                org.apache.commons.lang.text.StrBuilder messageBuff = new org.apache.commons.lang.text.StrBuilder(java.lang.String.format("The following %d host component(s) have not been downgraded to their desired versions:", errors.size())).append(java.lang.System.lineSeparator());
                for (org.apache.ambari.server.serveraction.upgrades.FinalizeUpgradeAction.InfoTuple error : errors) {
                    messageBuff.append(java.lang.String.format("%s: %s (current = %s, desired = %s)", error.hostName, error.componentName, error.currentVersion, error.targetVersion));
                    messageBuff.append(java.lang.System.lineSeparator());
                }
                throw new org.apache.ambari.server.AmbariException(messageBuff.toString());
            }
            finalizeHostVersionsNotDesired(cluster, upgradeContext);
            java.util.Map<java.lang.String, org.apache.ambari.server.orm.entities.RepositoryVersionEntity> targetVersionsByService = upgradeContext.getTargetVersions();
            java.util.Set<org.apache.ambari.server.orm.entities.RepositoryVersionEntity> targetRepositoryVersions = new java.util.HashSet<>();
            for (java.lang.String service : targetVersionsByService.keySet()) {
                targetRepositoryVersions.add(targetVersionsByService.get(service));
            }
            for (org.apache.ambari.server.orm.entities.RepositoryVersionEntity targetRepositoryVersion : targetRepositoryVersions) {
                java.util.List<org.apache.ambari.server.orm.entities.HostVersionEntity> hostVersions = hostVersionDAO.findHostVersionByClusterAndRepository(cluster.getClusterId(), targetRepositoryVersion);
                outSB.append(java.lang.String.format("Finalizing %d host(s) back to %s", hostVersions.size(), targetRepositoryVersion.getVersion())).append(java.lang.System.lineSeparator());
                for (org.apache.ambari.server.orm.entities.HostVersionEntity hostVersion : hostVersions) {
                    if (hostVersion.getState() != org.apache.ambari.server.state.RepositoryVersionState.CURRENT) {
                        hostVersion.setState(org.apache.ambari.server.state.RepositoryVersionState.CURRENT);
                        hostVersionDAO.merge(hostVersion);
                    }
                    java.util.List<org.apache.ambari.server.orm.entities.HostComponentStateEntity> hostComponentStates = hostComponentStateDAO.findByHost(hostVersion.getHostName());
                    for (org.apache.ambari.server.orm.entities.HostComponentStateEntity hostComponentState : hostComponentStates) {
                        hostComponentState.setUpgradeState(org.apache.ambari.server.state.UpgradeState.NONE);
                        hostComponentStateDAO.merge(hostComponentState);
                    }
                }
            }
            for (java.lang.String serviceName : servicesInUpgrade) {
                org.apache.ambari.server.orm.entities.RepositoryVersionEntity sourceRepositoryVersion = upgradeContext.getSourceRepositoryVersion(serviceName);
                org.apache.ambari.server.orm.entities.RepositoryVersionEntity targetRepositoryVersion = upgradeContext.getTargetRepositoryVersion(serviceName);
                org.apache.ambari.server.state.StackId sourceStackId = sourceRepositoryVersion.getStackId();
                org.apache.ambari.server.state.StackId targetStackId = targetRepositoryVersion.getStackId();
                if (!sourceStackId.equals(targetStackId)) {
                    outSB.append(java.lang.String.format("Removing %s configurations for %s", sourceStackId, serviceName)).append(java.lang.System.lineSeparator());
                    cluster.removeConfigurations(sourceStackId, serviceName);
                }
            }
            versionEventPublisher.publish(new org.apache.ambari.server.events.StackUpgradeFinishEvent(cluster));
            cluster.setUpgradeEntity(null);
            message = java.lang.String.format("The downgrade from %s has completed.", downgradeFromVersion);
            outSB.append(message).append(java.lang.System.lineSeparator());
            return createCommandReport(0, org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, "{}", outSB.toString(), errSB.toString());
        } catch (java.lang.Exception e) {
            java.io.StringWriter sw = new java.io.StringWriter();
            e.printStackTrace(new java.io.PrintWriter(sw));
            errSB.append(sw);
            return createCommandReport(-1, org.apache.ambari.server.actionmanager.HostRoleStatus.FAILED, "{}", outSB.toString(), errSB.toString());
        }
    }

    protected java.util.Set<org.apache.ambari.server.serveraction.upgrades.FinalizeUpgradeAction.InfoTuple> validateComponentVersions(org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext upgradeContext) throws org.apache.ambari.server.AmbariException {
        java.util.Set<org.apache.ambari.server.serveraction.upgrades.FinalizeUpgradeAction.InfoTuple> errors = new java.util.TreeSet<>();
        org.apache.ambari.server.state.Cluster cluster = upgradeContext.getCluster();
        java.util.Set<java.lang.String> servicesParticipating = upgradeContext.getSupportedServices();
        for (java.lang.String serviceName : servicesParticipating) {
            org.apache.ambari.server.state.Service service = cluster.getService(serviceName);
            org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersionEntity = upgradeContext.getTargetRepositoryVersion(serviceName);
            org.apache.ambari.server.state.StackId targetStackId = repositoryVersionEntity.getStackId();
            java.lang.String targetVersion = repositoryVersionEntity.getVersion();
            for (org.apache.ambari.server.state.ServiceComponent serviceComponent : service.getServiceComponents().values()) {
                org.apache.ambari.server.state.ComponentInfo componentInfo = ambariMetaInfo.getComponent(targetStackId.getStackName(), targetStackId.getStackVersion(), service.getName(), serviceComponent.getName());
                if (!componentInfo.isVersionAdvertised()) {
                    continue;
                }
                for (org.apache.ambari.server.state.ServiceComponentHost serviceComponentHost : serviceComponent.getServiceComponentHosts().values()) {
                    if (!org.apache.commons.lang.StringUtils.equals(targetVersion, serviceComponentHost.getVersion())) {
                        errors.add(new org.apache.ambari.server.serveraction.upgrades.FinalizeUpgradeAction.InfoTuple(service.getName(), serviceComponent.getName(), serviceComponentHost.getHostName(), serviceComponentHost.getVersion(), targetVersion));
                    }
                }
            }
        }
        return errors;
    }

    private void finalizeHostVersionsNotDesired(org.apache.ambari.server.state.Cluster cluster, org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext upgradeContext) throws org.apache.ambari.server.AmbariException {
        java.util.Set<org.apache.ambari.server.orm.entities.RepositoryVersionEntity> desiredRepoVersions = new java.util.HashSet<>();
        java.util.Set<java.lang.String> serviceNames = cluster.getServices().keySet();
        for (java.lang.String serviceName : serviceNames) {
            org.apache.ambari.server.state.Service service = cluster.getService(serviceName);
            desiredRepoVersions.add(service.getDesiredRepositoryVersion());
        }
        java.util.List<org.apache.ambari.server.orm.entities.HostVersionEntity> currentHostVersions = hostVersionDAO.findByClusterAndState(cluster.getClusterName(), org.apache.ambari.server.state.RepositoryVersionState.CURRENT);
        for (org.apache.ambari.server.orm.entities.HostVersionEntity hostVersionEntity : currentHostVersions) {
            org.apache.ambari.server.orm.entities.RepositoryVersionEntity hostRepoVersion = hostVersionEntity.getRepositoryVersion();
            if (!desiredRepoVersions.contains(hostRepoVersion)) {
                hostVersionEntity.setState(org.apache.ambari.server.state.RepositoryVersionState.INSTALLED);
                hostVersionEntity = hostVersionDAO.merge(hostVersionEntity);
            }
        }
        if (upgradeContext.isPatchRevert()) {
            org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersionEntity = upgradeContext.getRepositoryVersion();
            final org.apache.ambari.server.state.repository.VersionDefinitionXml vdfXml;
            try {
                vdfXml = repositoryVersionEntity.getRepositoryXml();
            } catch (java.lang.Exception exception) {
                throw new org.apache.ambari.server.AmbariException("The VDF's XML could not be deserialized", exception);
            }
            org.apache.ambari.server.state.StackInfo stack = ambariMetaInfo.getStack(repositoryVersionEntity.getStackId());
            java.util.Collection<org.apache.ambari.server.state.repository.AvailableService> availableServices = vdfXml.getAvailableServices(stack);
            java.util.Set<java.lang.String> participatingServices = upgradeContext.getSupportedServices();
            java.util.Set<java.lang.String> clusterServices = cluster.getServices().keySet();
            boolean resetRepoStateToOutOfSync = false;
            for (org.apache.ambari.server.state.repository.AvailableService availableService : availableServices) {
                if (clusterServices.contains(availableService.getName()) && (!participatingServices.contains(availableService.getName()))) {
                    resetRepoStateToOutOfSync = true;
                    break;
                }
            }
            if (resetRepoStateToOutOfSync) {
                java.util.List<org.apache.ambari.server.orm.entities.HostVersionEntity> hostVersions = hostVersionDAO.findHostVersionByClusterAndRepository(cluster.getClusterId(), repositoryVersionEntity);
                for (org.apache.ambari.server.orm.entities.HostVersionEntity hostVersion : hostVersions) {
                    hostVersion.setState(org.apache.ambari.server.state.RepositoryVersionState.OUT_OF_SYNC);
                    hostVersion = hostVersionDAO.merge(hostVersion);
                }
            }
        }
    }

    protected static class InfoTuple implements java.lang.Comparable<org.apache.ambari.server.serveraction.upgrades.FinalizeUpgradeAction.InfoTuple> {
        protected final java.lang.String serviceName;

        protected final java.lang.String componentName;

        protected final java.lang.String hostName;

        protected final java.lang.String currentVersion;

        protected final java.lang.String targetVersion;

        protected InfoTuple(java.lang.String service, java.lang.String component, java.lang.String host, java.lang.String version, java.lang.String desiredVersion) {
            serviceName = service;
            componentName = component;
            hostName = host;
            currentVersion = version;
            targetVersion = desiredVersion;
        }

        @java.lang.Override
        public int compareTo(org.apache.ambari.server.serveraction.upgrades.FinalizeUpgradeAction.InfoTuple that) {
            int compare = hostName.compareTo(that.hostName);
            if (compare != 0) {
                return compare;
            }
            compare = serviceName.compareTo(that.serviceName);
            if (compare != 0) {
                return compare;
            }
            compare = componentName.compareTo(that.componentName);
            if (compare != 0) {
                return compare;
            }
            return compare;
        }

        @java.lang.Override
        public int hashCode() {
            return java.util.Objects.hash(hostName, serviceName, componentName, currentVersion, targetVersion);
        }

        @java.lang.Override
        public boolean equals(java.lang.Object object) {
            if (this == object) {
                return true;
            }
            if ((object == null) || (getClass() != object.getClass())) {
                return false;
            }
            org.apache.ambari.server.serveraction.upgrades.FinalizeUpgradeAction.InfoTuple that = ((org.apache.ambari.server.serveraction.upgrades.FinalizeUpgradeAction.InfoTuple) (object));
            org.apache.commons.lang.builder.EqualsBuilder equalsBuilder = new org.apache.commons.lang.builder.EqualsBuilder();
            equalsBuilder.append(hostName, that.hostName);
            equalsBuilder.append(serviceName, that.serviceName);
            equalsBuilder.append(componentName, that.componentName);
            equalsBuilder.append(currentVersion, that.currentVersion);
            equalsBuilder.append(targetVersion, that.targetVersion);
            return equalsBuilder.isEquals();
        }

        @java.lang.Override
        public java.lang.String toString() {
            return com.google.common.base.MoreObjects.toStringHelper(this).add("host", hostName).add("component", componentName).add("current", currentVersion).add("target", targetVersion).toString();
        }
    }
}