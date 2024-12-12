package org.apache.ambari.server.security.encryption;
@com.google.inject.Singleton
public class AgentConfigUpdateEncryptor extends org.apache.ambari.server.security.encryption.PropertiesEncryptor implements org.apache.ambari.server.security.encryption.Encryptor<org.apache.ambari.server.events.AgentConfigsUpdateEvent> {
    private final org.apache.ambari.server.agent.AgentEncryptionKey encryptionKey;

    private final com.google.inject.Provider<org.apache.ambari.server.state.Clusters> clusters;

    @com.google.inject.Inject
    public AgentConfigUpdateEncryptor(org.apache.ambari.server.security.encryption.EncryptionService encryptionService, org.apache.ambari.server.security.encryption.CredentialStoreService credentialStore, com.google.inject.Provider<org.apache.ambari.server.state.Clusters> clusters) {
        super(encryptionService);
        this.encryptionKey = org.apache.ambari.server.agent.AgentEncryptionKey.loadFrom(credentialStore, true);
        this.clusters = clusters;
    }

    @java.lang.Override
    public void encryptSensitiveData(org.apache.ambari.server.events.AgentConfigsUpdateEvent event) {
        for (java.util.Map.Entry<java.lang.String, org.apache.ambari.server.agent.stomp.dto.ClusterConfigs> each : event.getClustersConfigs().entrySet()) {
            org.apache.ambari.server.state.Cluster cluster = getCluster(java.lang.Long.parseLong(each.getKey()));
            org.apache.ambari.server.agent.stomp.dto.ClusterConfigs clusterConfigs = each.getValue();
            for (java.util.Map.Entry<java.lang.String, java.util.SortedMap<java.lang.String, java.lang.String>> clusterConfig : clusterConfigs.getConfigurations().entrySet()) {
                encrypt(clusterConfig.getValue(), cluster, clusterConfig.getKey(), encryptionKey.toString());
            }
        }
    }

    @java.lang.Override
    public void decryptSensitiveData(org.apache.ambari.server.events.AgentConfigsUpdateEvent event) {
        throw new java.lang.UnsupportedOperationException("Not supported");
    }

    private org.apache.ambari.server.state.Cluster getCluster(long clusterId) throws org.apache.ambari.server.AmbariRuntimeException {
        try {
            return clusters.get().getCluster(clusterId);
        } catch (org.apache.ambari.server.AmbariException e) {
            throw new org.apache.ambari.server.AmbariRuntimeException("Cannot load cluster: " + clusterId, e);
        }
    }

    @java.lang.Override
    public java.lang.String getEncryptionKey() {
        return encryptionKey.toString();
    }
}