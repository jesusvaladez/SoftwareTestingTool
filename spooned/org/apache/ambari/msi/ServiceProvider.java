package org.apache.ambari.msi;
public class ServiceProvider extends org.apache.ambari.msi.BaseResourceProvider {
    protected static final java.lang.String SERVICE_CLUSTER_NAME_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("ServiceInfo", "cluster_name");

    protected static final java.lang.String SERVICE_SERVICE_NAME_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("ServiceInfo", "service_name");

    protected static final java.lang.String SERVICE_SERVICE_STATE_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("ServiceInfo", "state");

    public ServiceProvider(org.apache.ambari.msi.ClusterDefinition clusterDefinition) {
        super(org.apache.ambari.server.controller.spi.Resource.Type.Service, clusterDefinition);
        initServiceResources();
    }

    @java.lang.Override
    public void updateProperties(org.apache.ambari.server.controller.spi.Resource resource, org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) {
        java.util.Set<java.lang.String> propertyIds = getRequestPropertyIds(request, predicate);
        if (org.apache.ambari.msi.AbstractResourceProvider.contains(propertyIds, org.apache.ambari.msi.ServiceProvider.SERVICE_SERVICE_STATE_PROPERTY_ID)) {
            java.lang.String serviceName = ((java.lang.String) (resource.getPropertyValue(org.apache.ambari.msi.ServiceProvider.SERVICE_SERVICE_NAME_PROPERTY_ID)));
            resource.setProperty(org.apache.ambari.msi.ServiceProvider.SERVICE_SERVICE_STATE_PROPERTY_ID, getClusterDefinition().getServiceState(serviceName));
        }
    }

    @java.lang.Override
    public int updateProperties(org.apache.ambari.server.controller.spi.Resource resource, java.util.Map<java.lang.String, java.lang.Object> properties) {
        int requestId = -1;
        if (properties.containsKey(org.apache.ambari.msi.ServiceProvider.SERVICE_SERVICE_STATE_PROPERTY_ID)) {
            java.lang.String state = ((java.lang.String) (properties.get(org.apache.ambari.msi.ServiceProvider.SERVICE_SERVICE_STATE_PROPERTY_ID)));
            java.lang.String serviceName = ((java.lang.String) (resource.getPropertyValue(org.apache.ambari.msi.ServiceProvider.SERVICE_SERVICE_NAME_PROPERTY_ID)));
            requestId = getClusterDefinition().setServiceState(serviceName, state);
        }
        return requestId;
    }

    private void initServiceResources() {
        java.lang.String clusterName = getClusterDefinition().getClusterName();
        java.util.Set<java.lang.String> services = getClusterDefinition().getServices();
        for (java.lang.String serviceName : services) {
            org.apache.ambari.server.controller.spi.Resource service = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Service);
            service.setProperty(org.apache.ambari.msi.ServiceProvider.SERVICE_CLUSTER_NAME_PROPERTY_ID, clusterName);
            service.setProperty(org.apache.ambari.msi.ServiceProvider.SERVICE_SERVICE_NAME_PROPERTY_ID, serviceName);
            addResource(service);
        }
    }
}