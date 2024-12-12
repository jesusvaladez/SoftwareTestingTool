package org.apache.ambari.server.events.listeners.upgrade;
@com.google.inject.Singleton
@org.apache.ambari.server.EagerSingleton
public class StackUpgradeFinishListener {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.events.listeners.upgrade.StackUpgradeFinishListener.class);

    @com.google.inject.Inject
    com.google.inject.Provider<org.apache.ambari.server.api.services.AmbariMetaInfo> ambariMetaInfo;

    @com.google.inject.Inject
    com.google.inject.Provider<org.apache.ambari.server.metadata.RoleCommandOrderProvider> roleCommandOrderProvider;

    @com.google.inject.Inject
    public StackUpgradeFinishListener(org.apache.ambari.server.events.publishers.VersionEventPublisher eventPublisher) {
        eventPublisher.register(this);
    }

    @com.google.common.eventbus.Subscribe
    @com.google.common.eventbus.AllowConcurrentEvents
    public void onAmbariEvent(org.apache.ambari.server.events.StackUpgradeFinishEvent event) {
        org.apache.ambari.server.events.listeners.upgrade.StackUpgradeFinishListener.LOG.debug("Received event {}", event);
        org.apache.ambari.server.state.Cluster cluster = event.getCluster();
        for (org.apache.ambari.server.state.Service service : cluster.getServices().values()) {
            try {
                service.updateServiceInfo();
                for (org.apache.ambari.server.state.ServiceComponent sc : service.getServiceComponents().values()) {
                    sc.updateComponentInfo();
                }
            } catch (org.apache.ambari.server.AmbariException e) {
                if (org.apache.ambari.server.events.listeners.upgrade.StackUpgradeFinishListener.LOG.isErrorEnabled()) {
                    org.apache.ambari.server.events.listeners.upgrade.StackUpgradeFinishListener.LOG.error("Caught AmbariException when update component info", e);
                }
            }
        }
        if (roleCommandOrderProvider.get() instanceof org.apache.ambari.server.metadata.CachedRoleCommandOrderProvider) {
            org.apache.ambari.server.events.listeners.upgrade.StackUpgradeFinishListener.LOG.info("Clearing RCO cache");
            org.apache.ambari.server.metadata.CachedRoleCommandOrderProvider cachedRcoProvider = ((org.apache.ambari.server.metadata.CachedRoleCommandOrderProvider) (roleCommandOrderProvider.get()));
            cachedRcoProvider.clearRoleCommandOrderCache();
        }
        try {
            ambariMetaInfo.get().reconcileAlertDefinitions(cluster, true);
        } catch (org.apache.ambari.server.AmbariException e) {
            org.apache.ambari.server.events.listeners.upgrade.StackUpgradeFinishListener.LOG.error("Caught AmbariException when update alert definitions", e);
        }
    }
}