package org.apache.ambari.server.metadata;
import org.apache.commons.lang.builder.HashCodeBuilder;
@com.google.inject.Singleton
public class CachedRoleCommandOrderProvider implements org.apache.ambari.server.metadata.RoleCommandOrderProvider {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.metadata.CachedRoleCommandOrderProvider.class);

    @com.google.inject.Inject
    private com.google.inject.Injector injector;

    @com.google.inject.Inject
    private org.apache.ambari.server.state.Clusters clusters;

    private java.util.Map<java.lang.Integer, org.apache.ambari.server.metadata.RoleCommandOrder> rcoMap = new java.util.concurrent.ConcurrentHashMap<>();

    @com.google.inject.Inject
    public CachedRoleCommandOrderProvider() {
    }

    @java.lang.Override
    public org.apache.ambari.server.metadata.RoleCommandOrder getRoleCommandOrder(java.lang.Long clusterId) {
        org.apache.ambari.server.state.Cluster cluster = null;
        try {
            cluster = clusters.getCluster(clusterId);
            return getRoleCommandOrder(cluster);
        } catch (org.apache.ambari.server.AmbariException e) {
            return null;
        }
    }

    @java.lang.Override
    public org.apache.ambari.server.metadata.RoleCommandOrder getRoleCommandOrder(org.apache.ambari.server.state.Cluster cluster) {
        boolean hasGLUSTERFS = false;
        boolean isNameNodeHAEnabled = false;
        boolean isResourceManagerHAEnabled = false;
        try {
            if ((cluster != null) && (cluster.getService("GLUSTERFS") != null)) {
                hasGLUSTERFS = true;
            }
        } catch (org.apache.ambari.server.AmbariException ignored) {
        }
        try {
            if (((cluster != null) && (cluster.getService("HDFS") != null)) && (cluster.getService("HDFS").getServiceComponent("JOURNALNODE") != null)) {
                isNameNodeHAEnabled = true;
            }
        } catch (org.apache.ambari.server.AmbariException ignored) {
        }
        try {
            if (((cluster != null) && (cluster.getService("YARN") != null)) && (cluster.getService("YARN").getServiceComponent("RESOURCEMANAGER").getServiceComponentHosts().size() > 1)) {
                isResourceManagerHAEnabled = true;
            }
        } catch (org.apache.ambari.server.AmbariException ignored) {
        }
        int clusterCacheId = new org.apache.commons.lang.builder.HashCodeBuilder().append(cluster != null ? cluster.getClusterId() : -1).append(hasGLUSTERFS).append(isNameNodeHAEnabled).append(isResourceManagerHAEnabled).toHashCode();
        org.apache.ambari.server.metadata.RoleCommandOrder rco = rcoMap.get(clusterCacheId);
        if (rco == null) {
            rco = injector.getInstance(org.apache.ambari.server.metadata.RoleCommandOrder.class);
            java.util.LinkedHashSet<java.lang.String> sectionKeys = new java.util.LinkedHashSet<>();
            if (hasGLUSTERFS) {
                sectionKeys.add(org.apache.ambari.server.metadata.RoleCommandOrder.GLUSTERFS_DEPS_KEY);
            } else {
                sectionKeys.add(org.apache.ambari.server.metadata.RoleCommandOrder.NO_GLUSTERFS_DEPS_KEY);
            }
            if (isNameNodeHAEnabled) {
                sectionKeys.add(org.apache.ambari.server.metadata.RoleCommandOrder.NAMENODE_HA_DEPS_KEY);
            }
            if (isResourceManagerHAEnabled) {
                sectionKeys.add(org.apache.ambari.server.metadata.RoleCommandOrder.RESOURCEMANAGER_HA_DEPS_KEY);
            }
            rco.initialize(cluster, sectionKeys);
            rcoMap.put(clusterCacheId, rco);
        }
        return rco;
    }

    public void clearRoleCommandOrderCache() {
        rcoMap.clear();
    }
}