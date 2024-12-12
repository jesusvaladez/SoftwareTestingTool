package org.apache.ambari.msi;
public class HostProvider extends org.apache.ambari.msi.BaseResourceProvider {
    protected static final java.lang.String HOST_CLUSTER_NAME_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Hosts", "cluster_name");

    protected static final java.lang.String HOST_NAME_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Hosts", "host_name");

    protected static final java.lang.String HOST_STATE_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Hosts", "host_state");

    protected static final java.lang.String HOST_IP_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Hosts", "ip");

    protected static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.msi.HostProvider.class);

    public HostProvider(org.apache.ambari.msi.ClusterDefinition clusterDefinition) {
        super(org.apache.ambari.server.controller.spi.Resource.Type.Host, clusterDefinition);
        initHostResources();
    }

    @java.lang.Override
    public void updateProperties(org.apache.ambari.server.controller.spi.Resource resource, org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) {
        java.util.Set<java.lang.String> propertyIds = getRequestPropertyIds(request, predicate);
        if (org.apache.ambari.msi.AbstractResourceProvider.contains(propertyIds, org.apache.ambari.msi.HostProvider.HOST_STATE_PROPERTY_ID)) {
            java.lang.String hostName = ((java.lang.String) (resource.getPropertyValue(org.apache.ambari.msi.HostProvider.HOST_NAME_PROPERTY_ID)));
            resource.setProperty(org.apache.ambari.msi.HostProvider.HOST_STATE_PROPERTY_ID, getClusterDefinition().getHostState(hostName));
        }
    }

    @java.lang.Override
    public int updateProperties(org.apache.ambari.server.controller.spi.Resource resource, java.util.Map<java.lang.String, java.lang.Object> properties) {
        return -1;
    }

    private void initHostResources() {
        org.apache.ambari.msi.ClusterDefinition clusterDefinition = getClusterDefinition();
        java.lang.String clusterName = clusterDefinition.getClusterName();
        java.util.Set<java.lang.String> hosts = clusterDefinition.getHosts();
        for (java.lang.String hostName : hosts) {
            org.apache.ambari.server.controller.spi.Resource host = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Host);
            host.setProperty(org.apache.ambari.msi.HostProvider.HOST_CLUSTER_NAME_PROPERTY_ID, clusterName);
            host.setProperty(org.apache.ambari.msi.HostProvider.HOST_NAME_PROPERTY_ID, hostName);
            try {
                host.setProperty(org.apache.ambari.msi.HostProvider.HOST_IP_PROPERTY_ID, clusterDefinition.getHostInfoProvider().getHostAddress(hostName));
            } catch (org.apache.ambari.server.controller.spi.SystemException e) {
                if (org.apache.ambari.msi.HostProvider.LOG.isErrorEnabled()) {
                    org.apache.ambari.msi.HostProvider.LOG.error("Can't set host ip address : caught exception", e);
                }
            }
            addResource(host);
        }
    }
}