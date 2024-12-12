package org.apache.ambari.server.controller.internal;
@org.apache.ambari.server.StaticallyInject
public class AlertSummaryPropertyProvider extends org.apache.ambari.server.controller.internal.BaseProvider implements org.apache.ambari.server.controller.spi.PropertyProvider {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.controller.internal.AlertSummaryPropertyProvider.class);

    private static final java.lang.String ALERTS_SUMMARY = "alerts_summary";

    private static final java.lang.String ALERTS_SUMMARY_HOSTS = "alerts_summary_hosts";

    @com.google.inject.Inject
    private static com.google.inject.Provider<org.apache.ambari.server.state.Clusters> s_clusters = null;

    @com.google.inject.Inject
    private static org.apache.ambari.server.orm.dao.AlertsDAO s_dao = null;

    private org.apache.ambari.server.controller.spi.Resource.Type m_resourceType = null;

    private java.lang.String m_clusterPropertyId = null;

    private java.lang.String m_typeIdPropertyId = null;

    AlertSummaryPropertyProvider(org.apache.ambari.server.controller.spi.Resource.Type type, java.lang.String clusterPropertyId, java.lang.String typeIdPropertyId) {
        super(com.google.common.collect.ImmutableSet.of(org.apache.ambari.server.controller.internal.AlertSummaryPropertyProvider.ALERTS_SUMMARY, org.apache.ambari.server.controller.internal.AlertSummaryPropertyProvider.ALERTS_SUMMARY_HOSTS));
        m_resourceType = type;
        m_clusterPropertyId = clusterPropertyId;
        m_typeIdPropertyId = typeIdPropertyId;
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.controller.spi.Resource> populateResources(java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources, org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException {
        java.util.Set<java.lang.String> propertyIds = getRequestPropertyIds(request, predicate);
        try {
            java.util.Map<java.lang.Long, java.util.Map<java.lang.String, org.apache.ambari.server.orm.dao.AlertSummaryDTO>> perHostSummaryMap = new java.util.HashMap<>();
            java.util.Map<java.lang.Long, org.apache.ambari.server.orm.dao.AlertHostSummaryDTO> hostsSummaryMap = new java.util.HashMap<>();
            java.util.Map<java.lang.String, org.apache.ambari.server.state.Cluster> resourcesClusterMap = new java.util.HashMap<>();
            for (org.apache.ambari.server.controller.spi.Resource res : resources) {
                java.lang.String clusterName = ((java.lang.String) (res.getPropertyValue(m_clusterPropertyId)));
                if ((clusterName == null) || resourcesClusterMap.containsKey(clusterName)) {
                    continue;
                }
                org.apache.ambari.server.state.Cluster cluster = org.apache.ambari.server.controller.internal.AlertSummaryPropertyProvider.s_clusters.get().getCluster(clusterName);
                resourcesClusterMap.put(clusterName, cluster);
            }
            for (org.apache.ambari.server.state.Cluster cluster : resourcesClusterMap.values()) {
                long clusterId = cluster.getClusterId();
                switch (m_resourceType.getInternalType()) {
                    case Cluster :
                        if (org.apache.ambari.server.controller.internal.BaseProvider.isPropertyRequested(org.apache.ambari.server.controller.internal.AlertSummaryPropertyProvider.ALERTS_SUMMARY_HOSTS, propertyIds)) {
                            hostsSummaryMap.put(clusterId, org.apache.ambari.server.controller.internal.AlertSummaryPropertyProvider.s_dao.findCurrentHostCounts(clusterId));
                        }
                        break;
                    case Host :
                        if (resources.size() > 1) {
                            java.util.Map<java.lang.String, org.apache.ambari.server.orm.dao.AlertSummaryDTO> perHostCounts = org.apache.ambari.server.controller.internal.AlertSummaryPropertyProvider.s_dao.findCurrentPerHostCounts(clusterId);
                            perHostSummaryMap.put(clusterId, perHostCounts);
                        }
                        break;
                    default :
                        break;
                }
            }
            for (org.apache.ambari.server.controller.spi.Resource res : resources) {
                populateResource(res, propertyIds, perHostSummaryMap, hostsSummaryMap);
            }
        } catch (org.apache.ambari.server.AmbariException e) {
            org.apache.ambari.server.controller.internal.AlertSummaryPropertyProvider.LOG.error("Could not load built-in alerts - Executor exception ({})", e.getMessage());
        }
        return resources;
    }

    private void populateResource(org.apache.ambari.server.controller.spi.Resource resource, java.util.Set<java.lang.String> requestedIds, java.util.Map<java.lang.Long, java.util.Map<java.lang.String, org.apache.ambari.server.orm.dao.AlertSummaryDTO>> perHostSummaryMap, java.util.Map<java.lang.Long, org.apache.ambari.server.orm.dao.AlertHostSummaryDTO> hostsSummaryMap) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.orm.dao.AlertSummaryDTO summary = null;
        org.apache.ambari.server.orm.dao.AlertHostSummaryDTO hostSummary = null;
        java.lang.String clusterName = ((java.lang.String) (resource.getPropertyValue(m_clusterPropertyId)));
        if (null == clusterName) {
            return;
        }
        java.lang.String typeId = (null == m_typeIdPropertyId) ? null : ((java.lang.String) (resource.getPropertyValue(m_typeIdPropertyId)));
        org.apache.ambari.server.state.Cluster cluster = org.apache.ambari.server.controller.internal.AlertSummaryPropertyProvider.s_clusters.get().getCluster(clusterName);
        switch (m_resourceType.getInternalType()) {
            case Cluster :
                long clusterId = cluster.getClusterId();
                if (org.apache.ambari.server.controller.internal.BaseProvider.isPropertyRequested(org.apache.ambari.server.controller.internal.AlertSummaryPropertyProvider.ALERTS_SUMMARY, requestedIds)) {
                    summary = org.apache.ambari.server.controller.internal.AlertSummaryPropertyProvider.s_dao.findCurrentCounts(cluster.getClusterId(), null, null);
                }
                if (org.apache.ambari.server.controller.internal.BaseProvider.isPropertyRequested(org.apache.ambari.server.controller.internal.AlertSummaryPropertyProvider.ALERTS_SUMMARY_HOSTS, requestedIds)) {
                    if (hostsSummaryMap.containsKey(cluster.getClusterId())) {
                        hostSummary = hostsSummaryMap.get(cluster.getClusterId());
                    } else {
                        hostSummary = org.apache.ambari.server.controller.internal.AlertSummaryPropertyProvider.s_dao.findCurrentHostCounts(clusterId);
                    }
                }
                break;
            case Service :
                summary = org.apache.ambari.server.controller.internal.AlertSummaryPropertyProvider.s_dao.findCurrentCounts(cluster.getClusterId(), typeId, null);
                break;
            case Host :
                if (perHostSummaryMap.containsKey(cluster.getClusterId()) && perHostSummaryMap.get(cluster.getClusterId()).containsKey(typeId)) {
                    summary = perHostSummaryMap.get(cluster.getClusterId()).get(typeId);
                } else {
                    summary = org.apache.ambari.server.controller.internal.AlertSummaryPropertyProvider.s_dao.findCurrentCounts(cluster.getClusterId(), null, typeId);
                }
                break;
            default :
                break;
        }
        if (null != summary) {
            java.util.Map<java.lang.String, java.lang.Integer> map = new java.util.HashMap<>();
            map.put(org.apache.ambari.server.state.AlertState.OK.name(), java.lang.Integer.valueOf(summary.getOkCount()));
            map.put(org.apache.ambari.server.state.AlertState.WARNING.name(), java.lang.Integer.valueOf(summary.getWarningCount()));
            map.put(org.apache.ambari.server.state.AlertState.CRITICAL.name(), java.lang.Integer.valueOf(summary.getCriticalCount()));
            map.put(org.apache.ambari.server.state.AlertState.UNKNOWN.name(), java.lang.Integer.valueOf(summary.getUnknownCount()));
            map.put("MAINTENANCE", java.lang.Integer.valueOf(summary.getMaintenanceCount()));
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.AlertSummaryPropertyProvider.ALERTS_SUMMARY, map, requestedIds);
        }
        if (null != hostSummary) {
            java.util.Map<org.apache.ambari.server.state.AlertState, java.lang.Integer> map = new java.util.HashMap<>();
            map.put(org.apache.ambari.server.state.AlertState.OK, java.lang.Integer.valueOf(hostSummary.getOkCount()));
            map.put(org.apache.ambari.server.state.AlertState.WARNING, java.lang.Integer.valueOf(hostSummary.getWarningCount()));
            map.put(org.apache.ambari.server.state.AlertState.CRITICAL, java.lang.Integer.valueOf(hostSummary.getCriticalCount()));
            map.put(org.apache.ambari.server.state.AlertState.UNKNOWN, java.lang.Integer.valueOf(hostSummary.getUnknownCount()));
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.AlertSummaryPropertyProvider.ALERTS_SUMMARY_HOSTS, map, requestedIds);
        }
    }

    @java.lang.Override
    public java.util.Set<java.lang.String> checkPropertyIds(java.util.Set<java.lang.String> propertyIds) {
        java.util.Set<java.lang.String> rejects = new java.util.HashSet<>();
        for (java.lang.String id : propertyIds) {
            if (!id.startsWith(org.apache.ambari.server.controller.internal.AlertSummaryPropertyProvider.ALERTS_SUMMARY)) {
                rejects.add(id);
            }
        }
        return rejects;
    }
}