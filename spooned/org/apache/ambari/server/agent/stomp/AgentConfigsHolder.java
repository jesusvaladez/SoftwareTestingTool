package org.apache.ambari.server.agent.stomp;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
@com.google.inject.Singleton
public class AgentConfigsHolder extends org.apache.ambari.server.agent.stomp.AgentHostDataHolder<org.apache.ambari.server.events.AgentConfigsUpdateEvent> {
    public static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.agent.stomp.AgentConfigsHolder.class);

    private final org.apache.ambari.server.security.encryption.Encryptor<org.apache.ambari.server.events.AgentConfigsUpdateEvent> encryptor;

    @com.google.inject.Inject
    private org.apache.ambari.server.state.ConfigHelper configHelper;

    @com.google.inject.Inject
    private com.google.inject.Provider<org.apache.ambari.server.state.Clusters> clusters;

    @com.google.inject.Inject
    private org.apache.ambari.server.utils.ThreadPools threadPools;

    @com.google.inject.Inject
    public AgentConfigsHolder(org.apache.ambari.server.events.publishers.AmbariEventPublisher ambariEventPublisher, @com.google.inject.name.Named("AgentConfigEncryptor")
    org.apache.ambari.server.security.encryption.Encryptor<org.apache.ambari.server.events.AgentConfigsUpdateEvent> encryptor) {
        this.encryptor = encryptor;
        ambariEventPublisher.register(this);
    }

    @java.lang.Override
    public org.apache.ambari.server.events.AgentConfigsUpdateEvent getCurrentData(java.lang.Long hostId) throws org.apache.ambari.server.AmbariException {
        return configHelper.getHostActualConfigs(hostId);
    }

    public org.apache.ambari.server.events.AgentConfigsUpdateEvent getCurrentDataExcludeCluster(java.lang.Long hostId, java.lang.Long clusterId) throws org.apache.ambari.server.AmbariException {
        return configHelper.getHostActualConfigsExcludeCluster(hostId, clusterId);
    }

    @java.lang.Override
    protected org.apache.ambari.server.events.AgentConfigsUpdateEvent handleUpdate(org.apache.ambari.server.events.AgentConfigsUpdateEvent current, org.apache.ambari.server.events.AgentConfigsUpdateEvent update) {
        return update;
    }

    public void updateData(java.lang.Long clusterId, java.util.List<java.lang.Long> hostIds) throws org.apache.ambari.server.AmbariException {
        if (org.apache.commons.collections.CollectionUtils.isEmpty(hostIds)) {
            java.util.Collection<org.apache.ambari.server.state.Host> hosts = clusters.get().getCluster(clusterId).getHosts();
            if (org.apache.commons.collections.CollectionUtils.isEmpty(hosts)) {
                hostIds = clusters.get().getHosts().stream().map(org.apache.ambari.server.state.Host::getHostId).collect(java.util.stream.Collectors.toList());
            } else {
                hostIds = hosts.stream().map(org.apache.ambari.server.state.Host::getHostId).collect(java.util.stream.Collectors.toList());
            }
        }
        for (java.lang.Long hostId : hostIds) {
            org.apache.ambari.server.events.AgentConfigsUpdateEvent agentConfigsUpdateEvent = configHelper.getHostActualConfigs(hostId);
            updateData(agentConfigsUpdateEvent);
        }
    }

    @java.lang.Override
    public org.apache.ambari.server.events.AgentConfigsUpdateEvent getUpdateIfChanged(java.lang.String agentHash, java.lang.Long hostId) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.events.AgentConfigsUpdateEvent update = super.getUpdateIfChanged(agentHash, hostId);
        if (update.getClustersConfigs() == null) {
            update.setTimestamp(getData(hostId).getTimestamp());
        }
        return update;
    }

    @java.lang.Override
    protected void regenerateDataIdentifiers(org.apache.ambari.server.events.AgentConfigsUpdateEvent data) {
        data.setHash(getHash(data, encryptor.getEncryptionKey()));
        encryptor.encryptSensitiveData(data);
        data.setTimestamp(java.lang.System.currentTimeMillis());
    }

    @java.lang.Override
    protected boolean isIdentifierValid(org.apache.ambari.server.events.AgentConfigsUpdateEvent data) {
        return org.apache.commons.lang.StringUtils.isNotEmpty(data.getHash()) && (data.getTimestamp() != null);
    }

    @java.lang.Override
    protected void setIdentifiersToEventUpdate(org.apache.ambari.server.events.AgentConfigsUpdateEvent update, org.apache.ambari.server.events.AgentConfigsUpdateEvent hostData) {
        super.setIdentifiersToEventUpdate(update, hostData);
        update.setTimestamp(hostData.getTimestamp());
    }

    @java.lang.Override
    protected org.apache.ambari.server.events.AgentConfigsUpdateEvent getEmptyData() {
        return org.apache.ambari.server.events.AgentConfigsUpdateEvent.emptyUpdate();
    }
}