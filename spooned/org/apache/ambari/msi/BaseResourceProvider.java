package org.apache.ambari.msi;
public abstract class BaseResourceProvider extends org.apache.ambari.msi.AbstractResourceProvider {
    private final java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = new java.util.HashSet<org.apache.ambari.server.controller.spi.Resource>();

    public BaseResourceProvider(org.apache.ambari.server.controller.spi.Resource.Type type, org.apache.ambari.msi.ClusterDefinition clusterDefinition) {
        super(type, clusterDefinition);
    }

    protected java.util.Set<org.apache.ambari.server.controller.spi.Resource> getResources() {
        return resources;
    }

    protected void addResource(org.apache.ambari.server.controller.spi.Resource resource) {
        resources.add(resource);
    }
}