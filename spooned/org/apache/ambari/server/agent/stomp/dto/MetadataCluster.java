package org.apache.ambari.server.agent.stomp.dto;
@com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY)
public class MetadataCluster {
    private final java.util.concurrent.locks.Lock lock = new java.util.concurrent.locks.ReentrantLock();

    @com.fasterxml.jackson.annotation.JsonProperty("status_commands_to_run")
    private final java.util.Set<java.lang.String> statusCommandsToRun;

    private final boolean fullServiceLevelMetadata;

    private java.util.SortedMap<java.lang.String, org.apache.ambari.server.agent.stomp.dto.MetadataServiceInfo> serviceLevelParams;

    private java.util.SortedMap<java.lang.String, java.lang.String> clusterLevelParams;

    private java.util.SortedMap<java.lang.String, java.util.SortedMap<java.lang.String, java.lang.String>> agentConfigs;

    public MetadataCluster(org.apache.ambari.server.state.SecurityType securityType, java.util.SortedMap<java.lang.String, org.apache.ambari.server.agent.stomp.dto.MetadataServiceInfo> serviceLevelParams, boolean fullServiceLevelMetadata, java.util.SortedMap<java.lang.String, java.lang.String> clusterLevelParams, java.util.SortedMap<java.lang.String, java.util.SortedMap<java.lang.String, java.lang.String>> agentConfigs) {
        this.statusCommandsToRun = new java.util.HashSet<>();
        if (securityType != null) {
            this.statusCommandsToRun.add("STATUS");
        }
        this.fullServiceLevelMetadata = fullServiceLevelMetadata;
        this.serviceLevelParams = serviceLevelParams;
        this.clusterLevelParams = clusterLevelParams;
        this.agentConfigs = agentConfigs;
    }

    public static org.apache.ambari.server.agent.stomp.dto.MetadataCluster emptyMetadataCluster() {
        return new org.apache.ambari.server.agent.stomp.dto.MetadataCluster(null, null, false, null, null);
    }

    public static org.apache.ambari.server.agent.stomp.dto.MetadataCluster serviceLevelParamsMetadataCluster(org.apache.ambari.server.state.SecurityType securityType, java.util.SortedMap<java.lang.String, org.apache.ambari.server.agent.stomp.dto.MetadataServiceInfo> serviceLevelParams, boolean fullServiceLevelMetadata) {
        return new org.apache.ambari.server.agent.stomp.dto.MetadataCluster(securityType, serviceLevelParams, fullServiceLevelMetadata, null, null);
    }

    public static org.apache.ambari.server.agent.stomp.dto.MetadataCluster clusterLevelParamsMetadataCluster(org.apache.ambari.server.state.SecurityType securityType, java.util.SortedMap<java.lang.String, java.lang.String> clusterLevelParams) {
        return new org.apache.ambari.server.agent.stomp.dto.MetadataCluster(securityType, null, false, clusterLevelParams, null);
    }

    public java.util.Set<java.lang.String> getStatusCommandsToRun() {
        return statusCommandsToRun;
    }

    public java.util.SortedMap<java.lang.String, org.apache.ambari.server.agent.stomp.dto.MetadataServiceInfo> getServiceLevelParams() {
        return serviceLevelParams;
    }

    public java.util.SortedMap<java.lang.String, java.lang.String> getClusterLevelParams() {
        return clusterLevelParams;
    }

    public java.util.SortedMap<java.lang.String, java.util.SortedMap<java.lang.String, java.lang.String>> getAgentConfigs() {
        return agentConfigs;
    }

    public boolean isFullServiceLevelMetadata() {
        return fullServiceLevelMetadata;
    }

    public boolean updateServiceLevelParams(java.util.SortedMap<java.lang.String, org.apache.ambari.server.agent.stomp.dto.MetadataServiceInfo> update, boolean fullMetadataInUpdatedMap) {
        if (update != null) {
            try {
                lock.lock();
                if (this.serviceLevelParams == null) {
                    this.serviceLevelParams = new java.util.TreeMap<>();
                }
                return updateMapIfNeeded(this.serviceLevelParams, update, fullMetadataInUpdatedMap);
            } finally {
                lock.unlock();
            }
        }
        return false;
    }

    public boolean updateClusterLevelParams(java.util.SortedMap<java.lang.String, java.lang.String> update) {
        if (update != null) {
            try {
                lock.lock();
                if (this.clusterLevelParams == null) {
                    this.clusterLevelParams = new java.util.TreeMap<>();
                }
                return updateMapIfNeeded(this.clusterLevelParams, update, true);
            } finally {
                lock.unlock();
            }
        }
        return false;
    }

    private <T> boolean updateMapIfNeeded(java.util.Map<java.lang.String, T> currentMap, java.util.Map<java.lang.String, T> updatedMap, boolean fullMetadataInUpdatedMap) {
        boolean changed = false;
        if (fullMetadataInUpdatedMap) {
            changed = !java.util.Objects.equals(currentMap, updatedMap);
            if (changed) {
                currentMap.clear();
                currentMap.putAll(updatedMap);
            }
        } else {
            for (java.lang.String key : updatedMap.keySet()) {
                if ((!currentMap.containsKey(key)) || (!currentMap.get(key).equals(updatedMap.get(key)))) {
                    currentMap.put(key, updatedMap.get(key));
                    changed = true;
                }
            }
        }
        return changed;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o)
            return true;

        if ((o == null) || (getClass() != o.getClass()))
            return false;

        org.apache.ambari.server.agent.stomp.dto.MetadataCluster that = ((org.apache.ambari.server.agent.stomp.dto.MetadataCluster) (o));
        return (java.util.Objects.equals(statusCommandsToRun, that.statusCommandsToRun) && java.util.Objects.equals(serviceLevelParams, that.serviceLevelParams)) && java.util.Objects.equals(clusterLevelParams, that.clusterLevelParams);
    }

    @java.lang.Override
    public int hashCode() {
        return java.util.Objects.hash(statusCommandsToRun, serviceLevelParams, clusterLevelParams);
    }
}