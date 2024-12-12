package org.apache.ambari.server.controller.internal;
public abstract class AbstractDRResourceProvider extends org.apache.ambari.server.controller.internal.AbstractResourceProvider {
    private final org.apache.ambari.server.controller.ivory.IvoryService ivoryService;

    protected AbstractDRResourceProvider(java.util.Set<java.lang.String> propertyIds, java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> keyPropertyIds, org.apache.ambari.server.controller.ivory.IvoryService ivoryService) {
        super(propertyIds, keyPropertyIds);
        this.ivoryService = ivoryService;
    }

    protected org.apache.ambari.server.controller.ivory.IvoryService getService() {
        return ivoryService;
    }

    public static org.apache.ambari.server.controller.spi.ResourceProvider getResourceProvider(org.apache.ambari.server.controller.spi.Resource.Type type, org.apache.ambari.server.controller.ivory.IvoryService service) {
        switch (type.getInternalType()) {
            case DRFeed :
                return new org.apache.ambari.server.controller.internal.FeedResourceProvider(service);
            case DRTargetCluster :
                return new org.apache.ambari.server.controller.internal.TargetClusterResourceProvider(service);
            case DRInstance :
                return new org.apache.ambari.server.controller.internal.InstanceResourceProvider(service);
            default :
                throw new java.lang.IllegalArgumentException("Unknown type " + type);
        }
    }
}