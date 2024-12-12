package org.apache.ambari.server.state.alert;
@com.google.inject.Singleton
public class AggregateDefinitionMapping {
    private java.util.Map<java.lang.Long, java.util.Map<java.lang.String, org.apache.ambari.server.state.alert.AlertDefinition>> m_aggregateMap = new java.util.concurrent.ConcurrentHashMap<>();

    public AggregateDefinitionMapping() {
    }

    public org.apache.ambari.server.state.alert.AlertDefinition getAggregateDefinition(long clusterId, java.lang.String name) {
        java.lang.Long id = java.lang.Long.valueOf(clusterId);
        if (!m_aggregateMap.containsKey(id)) {
            return null;
        }
        if (!m_aggregateMap.get(id).containsKey(name)) {
            return null;
        }
        return m_aggregateMap.get(id).get(name);
    }

    public void registerAggregate(long clusterId, org.apache.ambari.server.state.alert.AlertDefinition definition) {
        java.lang.Long id = java.lang.Long.valueOf(clusterId);
        if (!m_aggregateMap.containsKey(id)) {
            m_aggregateMap.put(id, new java.util.HashMap<>());
        }
        java.util.Map<java.lang.String, org.apache.ambari.server.state.alert.AlertDefinition> map = m_aggregateMap.get(id);
        org.apache.ambari.server.state.alert.AggregateSource as = ((org.apache.ambari.server.state.alert.AggregateSource) (definition.getSource()));
        map.put(as.getAlertName(), definition);
    }

    public void removeAssociatedAggregate(long clusterId, java.lang.String aggregatedDefinitonName) {
        java.lang.Long id = java.lang.Long.valueOf(clusterId);
        if (!m_aggregateMap.containsKey(id)) {
            return;
        }
        java.util.Map<java.lang.String, org.apache.ambari.server.state.alert.AlertDefinition> map = m_aggregateMap.get(id);
        map.remove(aggregatedDefinitonName);
    }

    public java.util.List<org.apache.ambari.server.state.alert.AlertDefinition> getAggregateDefinitions(long clusterId) {
        if (!m_aggregateMap.containsKey(clusterId)) {
            return java.util.Collections.emptyList();
        }
        java.util.Map<java.lang.String, org.apache.ambari.server.state.alert.AlertDefinition> map = m_aggregateMap.get(clusterId);
        return new java.util.ArrayList<>(map.values());
    }

    public java.util.List<java.lang.String> getAlertsWithAggregates(long clusterId) {
        if (!m_aggregateMap.containsKey(clusterId)) {
            return java.util.Collections.emptyList();
        }
        java.util.Map<java.lang.String, org.apache.ambari.server.state.alert.AlertDefinition> map = m_aggregateMap.get(clusterId);
        return new java.util.ArrayList<>(map.keySet());
    }
}