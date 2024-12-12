package org.apache.ambari.server.controller.metrics.ganglia;
public class GangliaHostPropertyProvider extends org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProvider {
    private static final java.util.Set<java.lang.String> GANGLIA_CLUSTER_NAMES = new java.util.HashSet<>();

    static {
        GANGLIA_CLUSTER_NAMES.add("HDPNameNode");
        GANGLIA_CLUSTER_NAMES.add("HDPSlaves");
        GANGLIA_CLUSTER_NAMES.add("HDPJobTracker");
        GANGLIA_CLUSTER_NAMES.add("HDPResourceManager");
        GANGLIA_CLUSTER_NAMES.add("HDPHBaseMaster");
        GANGLIA_CLUSTER_NAMES.add("HDPHistoryServer");
        GANGLIA_CLUSTER_NAMES.add("HDPTaskTracker");
        GANGLIA_CLUSTER_NAMES.add("HDPHBaseRegionServer");
        GANGLIA_CLUSTER_NAMES.add("HDPFlumeServer");
        GANGLIA_CLUSTER_NAMES.add("HDPJournalNode");
    }

    public GangliaHostPropertyProvider(java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo>> componentPropertyInfoMap, org.apache.ambari.server.controller.internal.URLStreamProvider streamProvider, org.apache.ambari.server.configuration.ComponentSSLConfiguration configuration, org.apache.ambari.server.controller.metrics.MetricHostProvider hostProvider, java.lang.String clusterNamePropertyId, java.lang.String hostNamePropertyId) {
        super(componentPropertyInfoMap, streamProvider, configuration, hostProvider, clusterNamePropertyId, hostNamePropertyId, null);
    }

    @java.lang.Override
    protected java.lang.String getHostName(org.apache.ambari.server.controller.spi.Resource resource) {
        return ((java.lang.String) (resource.getPropertyValue(getHostNamePropertyId())));
    }

    @java.lang.Override
    protected java.lang.String getComponentName(org.apache.ambari.server.controller.spi.Resource resource) {
        return "*";
    }

    @java.lang.Override
    protected java.util.Set<java.lang.String> getGangliaClusterNames(org.apache.ambari.server.controller.spi.Resource resource, java.lang.String clusterName) {
        return org.apache.ambari.server.controller.metrics.ganglia.GangliaHostPropertyProvider.GANGLIA_CLUSTER_NAMES;
    }
}