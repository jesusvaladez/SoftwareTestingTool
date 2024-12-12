package org.apache.ambari.server.serveraction.upgrades;
public abstract class AbstractUpgradeServerAction extends org.apache.ambari.server.serveraction.AbstractServerAction {
    public org.apache.ambari.server.state.Clusters getClusters() {
        return m_clusters;
    }

    @com.google.inject.Inject
    protected org.apache.ambari.server.state.Clusters m_clusters;

    @com.google.inject.Inject
    protected org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeHelper m_upgradeHelper;

    @com.google.inject.Inject
    protected org.apache.ambari.server.orm.dao.UpgradeDAO m_upgradeDAO;

    @com.google.inject.Inject
    private org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContextFactory m_upgradeContextFactory;

    @com.google.inject.Inject
    protected org.apache.ambari.server.agent.stomp.AgentConfigsHolder agentConfigsHolder;

    @com.google.inject.Inject
    protected com.google.inject.Provider<org.apache.ambari.server.api.services.AmbariMetaInfo> m_metainfoProvider;

    @com.google.inject.Inject
    protected org.apache.ambari.server.state.ConfigHelper m_configHelper;

    @com.google.inject.Inject
    protected org.apache.ambari.server.controller.AmbariManagementController m_amc;

    protected com.google.gson.Gson getGson() {
        return gson;
    }

    protected org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext getUpgradeContext(org.apache.ambari.server.state.Cluster cluster) {
        org.apache.ambari.server.orm.entities.UpgradeEntity upgrade = cluster.getUpgradeInProgress();
        org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext upgradeContext = m_upgradeContextFactory.create(cluster, upgrade);
        return upgradeContext;
    }
}