package org.apache.ambari.msi;
public class NoOpProvider extends org.apache.ambari.msi.BaseResourceProvider {
    private final java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> keyPropertyIds = new java.util.HashMap<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String>();

    @java.lang.Override
    public void updateProperties(org.apache.ambari.server.controller.spi.Resource resource, org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) {
    }

    @java.lang.Override
    public int updateProperties(org.apache.ambari.server.controller.spi.Resource resource, java.util.Map<java.lang.String, java.lang.Object> properties) {
        return -1;
    }

    public NoOpProvider(org.apache.ambari.server.controller.spi.Resource.Type type, org.apache.ambari.msi.ClusterDefinition clusterDefinition) {
        super(type, clusterDefinition);
        keyPropertyIds.put(type, "id");
    }

    @java.lang.Override
    public java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> getKeyPropertyIds() {
        return keyPropertyIds;
    }

    @java.lang.Override
    public java.util.Set<java.lang.String> checkPropertyIds(java.util.Set<java.lang.String> propertyIds) {
        return java.util.Collections.emptySet();
    }
}