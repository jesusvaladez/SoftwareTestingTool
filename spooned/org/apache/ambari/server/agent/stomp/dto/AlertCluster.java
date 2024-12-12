package org.apache.ambari.server.agent.stomp.dto;
@com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
public class AlertCluster {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.agent.stomp.dto.AlertCluster.class);

    private final java.util.Map<java.lang.Long, org.apache.ambari.server.state.alert.AlertDefinition> alertDefinitions;

    private final java.lang.String hostName;

    private java.lang.Integer staleIntervalMultiplier;

    public AlertCluster(org.apache.ambari.server.state.alert.AlertDefinition alertDefinition, java.lang.String hostName, java.lang.Integer staleIntervalMultiplier) {
        this(java.util.Collections.singletonMap(alertDefinition.getDefinitionId(), alertDefinition), hostName, staleIntervalMultiplier);
    }

    public AlertCluster(java.util.Map<java.lang.Long, org.apache.ambari.server.state.alert.AlertDefinition> alertDefinitions, java.lang.String hostName, java.lang.Integer staleIntervalMultiplier) {
        this.alertDefinitions = new java.util.HashMap<>(alertDefinitions);
        this.hostName = hostName;
        this.staleIntervalMultiplier = staleIntervalMultiplier;
    }

    public AlertCluster(org.apache.ambari.server.state.alert.AlertDefinition alertDefinition, java.lang.String hostName) {
        this(java.util.Collections.singletonMap(alertDefinition.getDefinitionId(), alertDefinition), hostName, null);
    }

    public AlertCluster(java.util.Map<java.lang.Long, org.apache.ambari.server.state.alert.AlertDefinition> alertDefinitions, java.lang.String hostName) {
        this.alertDefinitions = new java.util.HashMap<>(alertDefinitions);
        this.hostName = hostName;
        this.staleIntervalMultiplier = null;
    }

    private AlertCluster() {
        alertDefinitions = null;
        hostName = null;
    }

    @com.fasterxml.jackson.annotation.JsonProperty("staleIntervalMultiplier")
    public java.lang.Integer getStaleIntervalMultiplier() {
        return staleIntervalMultiplier;
    }

    @com.fasterxml.jackson.annotation.JsonProperty("alertDefinitions")
    public java.util.Collection<org.apache.ambari.server.state.alert.AlertDefinition> getAlertDefinitions() {
        return alertDefinitions == null ? java.util.Collections.emptyList() : alertDefinitions.values();
    }

    @com.fasterxml.jackson.annotation.JsonProperty("hostName")
    public java.lang.String getHostName() {
        return hostName;
    }

    public org.apache.ambari.server.agent.stomp.dto.AlertCluster handleUpdate(org.apache.ambari.server.events.AlertDefinitionEventType eventType, org.apache.ambari.server.agent.stomp.dto.AlertCluster update) {
        boolean changed = false;
        org.apache.ambari.server.agent.stomp.dto.AlertCluster mergedCluster = null;
        java.util.Map<java.lang.Long, org.apache.ambari.server.state.alert.AlertDefinition> mergedDefinitions = new java.util.HashMap<>();
        java.lang.Integer mergedStaleIntervalMultiplier = null;
        switch (eventType) {
            case CREATE :
            case UPDATE :
                for (java.util.Map.Entry<java.lang.Long, org.apache.ambari.server.state.alert.AlertDefinition> alertDefinitionEntry : alertDefinitions.entrySet()) {
                    java.lang.Long definitionId = alertDefinitionEntry.getKey();
                    if (!update.alertDefinitions.containsKey(definitionId)) {
                        mergedDefinitions.put(definitionId, alertDefinitionEntry.getValue());
                    } else {
                        org.apache.ambari.server.state.alert.AlertDefinition newDefinition = update.alertDefinitions.get(definitionId);
                        org.apache.ambari.server.state.alert.AlertDefinition oldDefinition = alertDefinitionEntry.getValue();
                        if (!oldDefinition.deeplyEquals(newDefinition)) {
                            changed = true;
                        }
                        mergedDefinitions.put(definitionId, newDefinition);
                    }
                }
                if (addNewAlertDefinitions(update, mergedDefinitions)) {
                    changed = true;
                }
                if ((update.getStaleIntervalMultiplier() != null) && (!update.getStaleIntervalMultiplier().equals(staleIntervalMultiplier))) {
                    mergedStaleIntervalMultiplier = update.getStaleIntervalMultiplier();
                    changed = true;
                } else {
                    mergedStaleIntervalMultiplier = staleIntervalMultiplier;
                }
                org.apache.ambari.server.agent.stomp.dto.AlertCluster.LOG.debug("Handled {} of {} alerts, changed = {}", eventType, update.alertDefinitions.size(), changed);
                break;
            case DELETE :
                for (java.util.Map.Entry<java.lang.Long, org.apache.ambari.server.state.alert.AlertDefinition> alertDefinitionEntry : alertDefinitions.entrySet()) {
                    java.lang.Long definitionId = alertDefinitionEntry.getKey();
                    if (!update.alertDefinitions.containsKey(definitionId)) {
                        mergedDefinitions.put(definitionId, alertDefinitionEntry.getValue());
                    } else {
                        changed = true;
                    }
                }
                mergedStaleIntervalMultiplier = staleIntervalMultiplier;
                org.apache.ambari.server.agent.stomp.dto.AlertCluster.LOG.debug("Handled {} of {} alerts", eventType, update.alertDefinitions.size());
                break;
            default :
                org.apache.ambari.server.agent.stomp.dto.AlertCluster.LOG.warn("Unhandled event type {}", eventType);
                break;
        }
        if (changed) {
            mergedCluster = new org.apache.ambari.server.agent.stomp.dto.AlertCluster(mergedDefinitions, hostName, mergedStaleIntervalMultiplier);
        }
        return mergedCluster;
    }

    private boolean addNewAlertDefinitions(org.apache.ambari.server.agent.stomp.dto.AlertCluster update, java.util.Map<java.lang.Long, org.apache.ambari.server.state.alert.AlertDefinition> mergedDefinitions) {
        boolean hasNew = false;
        for (java.util.Map.Entry<java.lang.Long, org.apache.ambari.server.state.alert.AlertDefinition> each : update.alertDefinitions.entrySet()) {
            if (!mergedDefinitions.containsKey(each.getKey())) {
                mergedDefinitions.put(each.getKey(), each.getValue());
                hasNew = true;
            }
        }
        return hasNew;
    }

    public static org.apache.ambari.server.agent.stomp.dto.AlertCluster emptyAlertCluster() {
        return new org.apache.ambari.server.agent.stomp.dto.AlertCluster();
    }
}