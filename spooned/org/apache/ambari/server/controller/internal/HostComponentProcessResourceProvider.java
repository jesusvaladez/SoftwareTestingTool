package org.apache.ambari.server.controller.internal;
public class HostComponentProcessResourceProvider extends org.apache.ambari.server.controller.internal.ReadOnlyResourceProvider {
    public static final java.lang.String HOST_COMPONENT_PROCESS = "HostComponentProcess";

    public static final java.lang.String NAME_PROPERTY_ID = "name";

    public static final java.lang.String STATUS_PROPERTY_ID = "status";

    public static final java.lang.String CLUSTER_NAME_PROPERTY_ID = "cluster_name";

    public static final java.lang.String HOST_NAME_PROPERTY_ID = "host_name";

    public static final java.lang.String COMPONENT_NAME_PROPERTY_ID = "component_name";

    public static final java.lang.String NAME = (org.apache.ambari.server.controller.internal.HostComponentProcessResourceProvider.HOST_COMPONENT_PROCESS + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.HostComponentProcessResourceProvider.NAME_PROPERTY_ID;

    public static final java.lang.String STATUS = (org.apache.ambari.server.controller.internal.HostComponentProcessResourceProvider.HOST_COMPONENT_PROCESS + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.HostComponentProcessResourceProvider.STATUS_PROPERTY_ID;

    public static final java.lang.String CLUSTER_NAME = (org.apache.ambari.server.controller.internal.HostComponentProcessResourceProvider.HOST_COMPONENT_PROCESS + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.HostComponentProcessResourceProvider.CLUSTER_NAME_PROPERTY_ID;

    public static final java.lang.String HOST_NAME = (org.apache.ambari.server.controller.internal.HostComponentProcessResourceProvider.HOST_COMPONENT_PROCESS + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.HostComponentProcessResourceProvider.HOST_NAME_PROPERTY_ID;

    public static final java.lang.String COMPONENT_NAME = (org.apache.ambari.server.controller.internal.HostComponentProcessResourceProvider.HOST_COMPONENT_PROCESS + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.HostComponentProcessResourceProvider.COMPONENT_NAME_PROPERTY_ID;

    private static final java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> keyPropertyIds = com.google.common.collect.ImmutableMap.<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String>builder().put(org.apache.ambari.server.controller.spi.Resource.Type.Cluster, org.apache.ambari.server.controller.internal.HostComponentProcessResourceProvider.CLUSTER_NAME).put(org.apache.ambari.server.controller.spi.Resource.Type.Host, org.apache.ambari.server.controller.internal.HostComponentProcessResourceProvider.HOST_NAME).put(org.apache.ambari.server.controller.spi.Resource.Type.Component, org.apache.ambari.server.controller.internal.HostComponentProcessResourceProvider.COMPONENT_NAME).put(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent, org.apache.ambari.server.controller.internal.HostComponentProcessResourceProvider.COMPONENT_NAME).put(org.apache.ambari.server.controller.spi.Resource.Type.HostComponentProcess, org.apache.ambari.server.controller.internal.HostComponentProcessResourceProvider.NAME).build();

    private static final java.util.Set<java.lang.String> propertyIds = com.google.common.collect.Sets.newHashSet(org.apache.ambari.server.controller.internal.HostComponentProcessResourceProvider.NAME, org.apache.ambari.server.controller.internal.HostComponentProcessResourceProvider.STATUS, org.apache.ambari.server.controller.internal.HostComponentProcessResourceProvider.CLUSTER_NAME, org.apache.ambari.server.controller.internal.HostComponentProcessResourceProvider.HOST_NAME, org.apache.ambari.server.controller.internal.HostComponentProcessResourceProvider.COMPONENT_NAME);

    HostComponentProcessResourceProvider(org.apache.ambari.server.controller.AmbariManagementController amc) {
        super(org.apache.ambari.server.controller.spi.Resource.Type.HostComponentProcess, org.apache.ambari.server.controller.internal.HostComponentProcessResourceProvider.propertyIds, org.apache.ambari.server.controller.internal.HostComponentProcessResourceProvider.keyPropertyIds, amc);
    }

    @java.lang.Override
    protected java.util.Set<java.lang.String> getPKPropertyIds() {
        return new java.util.HashSet<>(org.apache.ambari.server.controller.internal.HostComponentProcessResourceProvider.keyPropertyIds.values());
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.controller.spi.Resource> getResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        final java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> requestMaps = getPropertyMaps(predicate);
        java.util.Set<java.lang.String> requestedIds = getRequestPropertyIds(request, predicate);
        java.util.Set<org.apache.ambari.server.controller.HostComponentProcessResponse> responses = getResources(new org.apache.ambari.server.controller.internal.AbstractResourceProvider.Command<java.util.Set<org.apache.ambari.server.controller.HostComponentProcessResponse>>() {
            @java.lang.Override
            public java.util.Set<org.apache.ambari.server.controller.HostComponentProcessResponse> invoke() throws org.apache.ambari.server.AmbariException {
                return getHostComponentProcesses(requestMaps);
            }
        });
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = new java.util.HashSet<>();
        for (org.apache.ambari.server.controller.HostComponentProcessResponse response : responses) {
            org.apache.ambari.server.controller.spi.Resource r = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.HostComponentProcess);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(r, org.apache.ambari.server.controller.internal.HostComponentProcessResourceProvider.CLUSTER_NAME, response.getCluster(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(r, org.apache.ambari.server.controller.internal.HostComponentProcessResourceProvider.HOST_NAME, response.getHost(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(r, org.apache.ambari.server.controller.internal.HostComponentProcessResourceProvider.COMPONENT_NAME, response.getComponent(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(r, org.apache.ambari.server.controller.internal.HostComponentProcessResourceProvider.NAME, response.getValueMap().get("name"), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(r, org.apache.ambari.server.controller.internal.HostComponentProcessResourceProvider.STATUS, response.getValueMap().get("status"), requestedIds);
            for (java.util.Map.Entry<java.lang.String, java.lang.String> entry : response.getValueMap().entrySet()) {
                if (entry.getKey().equals("name") || entry.getKey().equals("status"))
                    continue;

                org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(r, "HostComponentProcess/" + entry.getKey(), entry.getValue(), requestedIds);
            }
            resources.add(r);
        }
        return resources;
    }

    private java.util.Set<org.apache.ambari.server.controller.HostComponentProcessResponse> getHostComponentProcesses(java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> requestMaps) throws org.apache.ambari.server.AmbariException {
        java.util.Set<org.apache.ambari.server.controller.HostComponentProcessResponse> results = new java.util.HashSet<>();
        org.apache.ambari.server.state.Clusters clusters = getManagementController().getClusters();
        for (java.util.Map<java.lang.String, java.lang.Object> requestMap : requestMaps) {
            java.lang.String cluster = ((java.lang.String) (requestMap.get(org.apache.ambari.server.controller.internal.HostComponentProcessResourceProvider.CLUSTER_NAME)));
            java.lang.String component = ((java.lang.String) (requestMap.get(org.apache.ambari.server.controller.internal.HostComponentProcessResourceProvider.COMPONENT_NAME)));
            java.lang.String host = ((java.lang.String) (requestMap.get(org.apache.ambari.server.controller.internal.HostComponentProcessResourceProvider.HOST_NAME)));
            org.apache.ambari.server.state.Cluster c = clusters.getCluster(cluster);
            java.util.Collection<org.apache.ambari.server.state.ServiceComponentHost> schs = c.getServiceComponentHosts(host);
            for (org.apache.ambari.server.state.ServiceComponentHost sch : schs) {
                if (!sch.getServiceComponentName().equals(component))
                    continue;

                for (java.util.Map<java.lang.String, java.lang.String> proc : sch.getProcesses()) {
                    results.add(new org.apache.ambari.server.controller.HostComponentProcessResponse(cluster, sch.getHostName(), component, proc));
                }
            }
        }
        return results;
    }
}