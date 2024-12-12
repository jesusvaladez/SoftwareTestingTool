package org.apache.ambari.msi;
public class ClusterProvider extends org.apache.ambari.msi.BaseResourceProvider {
    protected static final java.lang.String CLUSTER_NAME_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Clusters", "cluster_name");

    protected static final java.lang.String CLUSTER_VERSION_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Clusters", "version");

    public ClusterProvider(org.apache.ambari.msi.ClusterDefinition clusterDefinition) {
        super(org.apache.ambari.server.controller.spi.Resource.Type.Cluster, clusterDefinition);
        initClusterResources();
    }

    @java.lang.Override
    public void updateProperties(org.apache.ambari.server.controller.spi.Resource resource, org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) {
    }

    @java.lang.Override
    public int updateProperties(org.apache.ambari.server.controller.spi.Resource resource, java.util.Map<java.lang.String, java.lang.Object> properties) {
        return -1;
    }

    private void initClusterResources() {
        org.apache.ambari.server.controller.spi.Resource cluster = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Cluster);
        org.apache.ambari.msi.ClusterDefinition clusterDefinition = getClusterDefinition();
        cluster.setProperty(org.apache.ambari.msi.ClusterProvider.CLUSTER_NAME_PROPERTY_ID, clusterDefinition.getClusterName());
        cluster.setProperty(org.apache.ambari.msi.ClusterProvider.CLUSTER_VERSION_PROPERTY_ID, clusterDefinition.getVersionId());
        addResource(cluster);
    }
}