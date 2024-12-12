package org.apache.ambari.msi;
public class ComponentProvider extends org.apache.ambari.msi.BaseResourceProvider {
    protected static final java.lang.String COMPONENT_CLUSTER_NAME_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("ServiceComponentInfo", "cluster_name");

    protected static final java.lang.String COMPONENT_SERVICE_NAME_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("ServiceComponentInfo", "service_name");

    protected static final java.lang.String COMPONENT_COMPONENT_NAME_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("ServiceComponentInfo", "component_name");

    protected static final java.lang.String COMPONENT_STATE_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("ServiceComponentInfo", "state");

    public ComponentProvider(org.apache.ambari.msi.ClusterDefinition clusterDefinition) {
        super(org.apache.ambari.server.controller.spi.Resource.Type.Component, clusterDefinition);
        initComponentResources();
    }

    @java.lang.Override
    public void updateProperties(org.apache.ambari.server.controller.spi.Resource resource, org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) {
        java.util.Set<java.lang.String> propertyIds = getRequestPropertyIds(request, predicate);
        if (org.apache.ambari.msi.AbstractResourceProvider.contains(propertyIds, org.apache.ambari.msi.ComponentProvider.COMPONENT_STATE_PROPERTY_ID)) {
            java.lang.String serviceName = ((java.lang.String) (resource.getPropertyValue(org.apache.ambari.msi.ComponentProvider.COMPONENT_SERVICE_NAME_PROPERTY_ID)));
            java.lang.String componentName = ((java.lang.String) (resource.getPropertyValue(org.apache.ambari.msi.ComponentProvider.COMPONENT_COMPONENT_NAME_PROPERTY_ID)));
            resource.setProperty(org.apache.ambari.msi.ComponentProvider.COMPONENT_STATE_PROPERTY_ID, getClusterDefinition().getComponentState(serviceName, componentName));
        }
    }

    @java.lang.Override
    public int updateProperties(org.apache.ambari.server.controller.spi.Resource resource, java.util.Map<java.lang.String, java.lang.Object> properties) {
        int requestId = -1;
        if (properties.containsKey(org.apache.ambari.msi.ComponentProvider.COMPONENT_STATE_PROPERTY_ID)) {
            java.lang.String state = ((java.lang.String) (properties.get(org.apache.ambari.msi.ComponentProvider.COMPONENT_STATE_PROPERTY_ID)));
            java.lang.String serviceName = ((java.lang.String) (resource.getPropertyValue(org.apache.ambari.msi.ComponentProvider.COMPONENT_SERVICE_NAME_PROPERTY_ID)));
            java.lang.String componentName = ((java.lang.String) (resource.getPropertyValue(org.apache.ambari.msi.ComponentProvider.COMPONENT_COMPONENT_NAME_PROPERTY_ID)));
            requestId = getClusterDefinition().setComponentState(serviceName, componentName, state);
        }
        return requestId;
    }

    private void initComponentResources() {
        java.lang.String clusterName = getClusterDefinition().getClusterName();
        java.util.Set<java.lang.String> services = getClusterDefinition().getServices();
        for (java.lang.String serviceName : services) {
            java.util.Set<java.lang.String> components = getClusterDefinition().getComponents(serviceName);
            for (java.lang.String componentName : components) {
                org.apache.ambari.server.controller.spi.Resource component = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Component);
                component.setProperty(org.apache.ambari.msi.ComponentProvider.COMPONENT_CLUSTER_NAME_PROPERTY_ID, clusterName);
                component.setProperty(org.apache.ambari.msi.ComponentProvider.COMPONENT_SERVICE_NAME_PROPERTY_ID, serviceName);
                component.setProperty(org.apache.ambari.msi.ComponentProvider.COMPONENT_COMPONENT_NAME_PROPERTY_ID, componentName);
                addResource(component);
            }
        }
    }
}