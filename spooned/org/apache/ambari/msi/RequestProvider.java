package org.apache.ambari.msi;
public class RequestProvider extends org.apache.ambari.msi.AbstractResourceProvider {
    protected static final java.lang.String REQUEST_CLUSTER_NAME_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Requests", "cluster_name");

    protected static final java.lang.String REQUEST_ID_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Requests", "id");

    protected static final java.lang.String REQUEST_CONTEXT_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Requests", "request_context");

    public RequestProvider(org.apache.ambari.msi.ClusterDefinition clusterDefinition) {
        super(org.apache.ambari.server.controller.spi.Resource.Type.Request, clusterDefinition);
    }

    @java.lang.Override
    protected java.util.Set<org.apache.ambari.server.controller.spi.Resource> getResources() {
        return getClusterDefinition().getRequestResources();
    }

    @java.lang.Override
    public void updateProperties(org.apache.ambari.server.controller.spi.Resource resource, org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) {
    }

    @java.lang.Override
    public int updateProperties(org.apache.ambari.server.controller.spi.Resource resource, java.util.Map<java.lang.String, java.lang.Object> properties) {
        return -1;
    }
}