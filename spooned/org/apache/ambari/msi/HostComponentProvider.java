package org.apache.ambari.msi;
public class HostComponentProvider extends org.apache.ambari.msi.BaseResourceProvider {
    protected static final java.lang.String HOST_COMPONENT_CLUSTER_NAME_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "cluster_name");

    protected static final java.lang.String HOST_COMPONENT_SERVICE_NAME_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "service_name");

    protected static final java.lang.String HOST_COMPONENT_COMPONENT_NAME_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "component_name");

    protected static final java.lang.String HOST_COMPONENT_HOST_NAME_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "host_name");

    protected static final java.lang.String HOST_COMPONENT_STATE_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "state");

    protected static final java.lang.String HOST_COMPONENT_DESIRED_STATE_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "desired_state");

    public HostComponentProvider(org.apache.ambari.msi.ClusterDefinition clusterDefinition) {
        super(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent, clusterDefinition);
        initHostComponentResources();
    }

    @java.lang.Override
    public void updateProperties(org.apache.ambari.server.controller.spi.Resource resource, org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) {
        java.util.Set<java.lang.String> propertyIds = getRequestPropertyIds(request, predicate);
        if (org.apache.ambari.msi.AbstractResourceProvider.contains(propertyIds, org.apache.ambari.msi.HostComponentProvider.HOST_COMPONENT_STATE_PROPERTY_ID) || org.apache.ambari.msi.AbstractResourceProvider.contains(propertyIds, org.apache.ambari.msi.HostComponentProvider.HOST_COMPONENT_DESIRED_STATE_PROPERTY_ID)) {
            java.lang.String componentName = ((java.lang.String) (resource.getPropertyValue(org.apache.ambari.msi.HostComponentProvider.HOST_COMPONENT_COMPONENT_NAME_PROPERTY_ID)));
            java.lang.String hostName = ((java.lang.String) (resource.getPropertyValue(org.apache.ambari.msi.HostComponentProvider.HOST_COMPONENT_HOST_NAME_PROPERTY_ID)));
            java.lang.String hostComponentState = getClusterDefinition().getHostComponentState(hostName, componentName);
            resource.setProperty(org.apache.ambari.msi.HostComponentProvider.HOST_COMPONENT_STATE_PROPERTY_ID, hostComponentState);
            resource.setProperty(org.apache.ambari.msi.HostComponentProvider.HOST_COMPONENT_DESIRED_STATE_PROPERTY_ID, hostComponentState);
        }
    }

    @java.lang.Override
    public int updateProperties(org.apache.ambari.server.controller.spi.Resource resource, java.util.Map<java.lang.String, java.lang.Object> properties) {
        int requestId = -1;
        if (properties.containsKey(org.apache.ambari.msi.HostComponentProvider.HOST_COMPONENT_STATE_PROPERTY_ID)) {
            java.lang.String state = ((java.lang.String) (properties.get(org.apache.ambari.msi.HostComponentProvider.HOST_COMPONENT_STATE_PROPERTY_ID)));
            java.lang.String componentName = ((java.lang.String) (resource.getPropertyValue(org.apache.ambari.msi.HostComponentProvider.HOST_COMPONENT_COMPONENT_NAME_PROPERTY_ID)));
            java.lang.String hostName = ((java.lang.String) (resource.getPropertyValue(org.apache.ambari.msi.HostComponentProvider.HOST_COMPONENT_HOST_NAME_PROPERTY_ID)));
            requestId = getClusterDefinition().setHostComponentState(hostName, componentName, state);
        }
        return requestId;
    }

    private void initHostComponentResources() {
        java.lang.String clusterName = getClusterDefinition().getClusterName();
        java.util.Set<java.lang.String> services = getClusterDefinition().getServices();
        for (java.lang.String serviceName : services) {
            java.util.Set<java.lang.String> hosts = getClusterDefinition().getHosts();
            for (java.lang.String hostName : hosts) {
                java.util.Set<java.lang.String> hostComponents = getClusterDefinition().getHostComponents(serviceName, hostName);
                for (java.lang.String componentName : hostComponents) {
                    org.apache.ambari.server.controller.spi.Resource hostComponent = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent);
                    hostComponent.setProperty(org.apache.ambari.msi.HostComponentProvider.HOST_COMPONENT_CLUSTER_NAME_PROPERTY_ID, clusterName);
                    hostComponent.setProperty(org.apache.ambari.msi.HostComponentProvider.HOST_COMPONENT_SERVICE_NAME_PROPERTY_ID, serviceName);
                    hostComponent.setProperty(org.apache.ambari.msi.HostComponentProvider.HOST_COMPONENT_COMPONENT_NAME_PROPERTY_ID, componentName);
                    hostComponent.setProperty(org.apache.ambari.msi.HostComponentProvider.HOST_COMPONENT_HOST_NAME_PROPERTY_ID, hostName);
                    addResource(hostComponent);
                }
            }
        }
    }
}