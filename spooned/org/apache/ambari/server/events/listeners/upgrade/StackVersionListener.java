package org.apache.ambari.server.events.listeners.upgrade;
import org.apache.commons.lang.StringUtils;
@com.google.inject.Singleton
@org.apache.ambari.server.EagerSingleton
public class StackVersionListener {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.events.listeners.upgrade.StackVersionListener.class);

    public static final java.lang.String UNKNOWN_VERSION = org.apache.ambari.server.state.State.UNKNOWN.toString();

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.RepositoryVersionDAO repositoryVersionDAO;

    private com.google.inject.Provider<org.apache.ambari.server.api.services.AmbariMetaInfo> ambariMetaInfoProvider;

    private final com.google.inject.Provider<org.apache.ambari.server.agent.stomp.HostLevelParamsHolder> m_hostLevelParamsHolder;

    @com.google.inject.Inject
    public StackVersionListener(org.apache.ambari.server.events.publishers.VersionEventPublisher eventPublisher, com.google.inject.Provider<org.apache.ambari.server.api.services.AmbariMetaInfo> ambariMetaInfoProvider, com.google.inject.Provider<org.apache.ambari.server.agent.stomp.HostLevelParamsHolder> m_hostLevelParamsHolder) {
        this.ambariMetaInfoProvider = ambariMetaInfoProvider;
        this.m_hostLevelParamsHolder = m_hostLevelParamsHolder;
        eventPublisher.register(this);
    }

    @com.google.common.eventbus.Subscribe
    public void onAmbariEvent(org.apache.ambari.server.events.HostComponentVersionAdvertisedEvent event) {
        org.apache.ambari.server.events.listeners.upgrade.StackVersionListener.LOG.debug("Received event {}", event);
        org.apache.ambari.server.state.Cluster cluster = event.getCluster();
        org.apache.ambari.server.state.ServiceComponentHost sch = event.getServiceComponentHost();
        java.lang.String newVersion = event.getVersion();
        if (org.apache.commons.lang.StringUtils.isEmpty(newVersion)) {
            return;
        }
        if ((null != event.getRepositoryVersionId()) && (null == cluster.getUpgradeInProgress())) {
            org.apache.ambari.server.orm.entities.RepositoryVersionEntity rve = repositoryVersionDAO.findByPK(event.getRepositoryVersionId());
            if (null != rve) {
                java.lang.String currentRepoVersion = rve.getVersion();
                if (!org.apache.commons.lang.StringUtils.equals(currentRepoVersion, newVersion)) {
                    rve.setVersion(newVersion);
                    rve.setResolved(false);
                }
                if (!rve.isResolved()) {
                    rve.setResolved(true);
                    repositoryVersionDAO.merge(rve);
                }
            }
        }
        try {
            org.apache.ambari.server.state.StackId desiredStackId = sch.getDesiredStackId();
            org.apache.ambari.server.api.services.AmbariMetaInfo ambariMetaInfo = ambariMetaInfoProvider.get();
            org.apache.ambari.server.state.ComponentInfo componentInfo = ambariMetaInfo.getComponent(desiredStackId.getStackName(), desiredStackId.getStackVersion(), sch.getServiceName(), sch.getServiceComponentName());
            if (!componentInfo.isVersionAdvertised()) {
                if (!org.apache.commons.lang.StringUtils.equalsIgnoreCase(org.apache.ambari.server.events.listeners.upgrade.StackVersionListener.UNKNOWN_VERSION, newVersion)) {
                    org.apache.ambari.server.events.listeners.upgrade.StackVersionListener.LOG.warn("ServiceComponent {} doesn't advertise version, however ServiceHostComponent {} on host {} advertised version as {}. Skipping version update", sch.getServiceComponentName(), sch.getServiceComponentName(), sch.getHostName(), newVersion);
                }
                return;
            }
            org.apache.ambari.server.state.ServiceComponent sc = cluster.getService(sch.getServiceName()).getServiceComponent(sch.getServiceComponentName());
            if (org.apache.commons.lang.StringUtils.equalsIgnoreCase(org.apache.ambari.server.events.listeners.upgrade.StackVersionListener.UNKNOWN_VERSION, newVersion)) {
                processUnknownDesiredVersion(cluster, sc, sch, newVersion);
                return;
            }
            processComponentAdvertisedVersion(cluster, sc, sch, newVersion);
        } catch (java.lang.Exception e) {
            org.apache.ambari.server.events.listeners.upgrade.StackVersionListener.LOG.error("Unable to propagate version for ServiceHostComponent on component: {}, host: {}. Error: {}", sch.getServiceComponentName(), sch.getHostName(), e.getMessage());
        }
    }

    private void processComponentAdvertisedVersion(org.apache.ambari.server.state.Cluster cluster, org.apache.ambari.server.state.ServiceComponent sc, org.apache.ambari.server.state.ServiceComponentHost sch, java.lang.String newVersion) throws org.apache.ambari.server.AmbariException {
        if (org.apache.commons.lang.StringUtils.isBlank(newVersion)) {
            return;
        }
        java.lang.String previousVersion = sch.getVersion();
        java.lang.String desiredVersion = sc.getDesiredVersion();
        org.apache.ambari.server.state.UpgradeState upgradeState = sch.getUpgradeState();
        boolean versionIsCorrect = org.apache.commons.lang.StringUtils.equals(desiredVersion, newVersion);
        if (!org.apache.commons.lang.StringUtils.equals(previousVersion, newVersion)) {
            sch.setVersion(newVersion);
        }
        if ((previousVersion == null) || org.apache.commons.lang.StringUtils.equalsIgnoreCase(org.apache.ambari.server.events.listeners.upgrade.StackVersionListener.UNKNOWN_VERSION, previousVersion)) {
            sch.setUpgradeState(org.apache.ambari.server.state.UpgradeState.NONE);
            sch.recalculateHostVersionState();
        } else if (versionIsCorrect) {
            boolean isUpgradeInProgressForThisComponent = (null != cluster.getUpgradeInProgress()) && (upgradeState != org.apache.ambari.server.state.UpgradeState.NONE);
            if (isUpgradeInProgressForThisComponent) {
                setUpgradeStateAndRecalculateHostVersions(sch, org.apache.ambari.server.state.UpgradeState.COMPLETE);
            } else {
                setUpgradeStateAndRecalculateHostVersions(sch, org.apache.ambari.server.state.UpgradeState.NONE);
            }
        } else {
            setUpgradeStateAndRecalculateHostVersions(sch, org.apache.ambari.server.state.UpgradeState.VERSION_MISMATCH);
        }
        sc.updateRepositoryState(newVersion);
    }

    private void processUnknownDesiredVersion(org.apache.ambari.server.state.Cluster cluster, org.apache.ambari.server.state.ServiceComponent sc, org.apache.ambari.server.state.ServiceComponentHost sch, java.lang.String newVersion) throws org.apache.ambari.server.AmbariException {
        sch.setUpgradeState(org.apache.ambari.server.state.UpgradeState.NONE);
        sch.setVersion(newVersion);
        sch.recalculateHostVersionState();
    }

    private void setUpgradeStateAndRecalculateHostVersions(org.apache.ambari.server.state.ServiceComponentHost sch, org.apache.ambari.server.state.UpgradeState upgradeState) throws org.apache.ambari.server.AmbariException {
        if (sch.getUpgradeState() == upgradeState) {
            return;
        }
        sch.setUpgradeState(upgradeState);
        sch.recalculateHostVersionState();
    }
}