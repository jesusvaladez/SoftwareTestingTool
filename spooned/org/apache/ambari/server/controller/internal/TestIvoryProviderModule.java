package org.apache.ambari.server.controller.internal;
public class TestIvoryProviderModule extends org.apache.ambari.server.controller.internal.DefaultProviderModule {
    org.apache.ambari.server.controller.ivory.IvoryService service = new org.apache.ambari.server.controller.internal.TestIvoryService(null, null, null);

    @java.lang.Override
    protected org.apache.ambari.server.controller.spi.ResourceProvider createResourceProvider(org.apache.ambari.server.controller.spi.Resource.Type type) {
        java.util.Set<java.lang.String> propertyIds = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyIds(type);
        java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> keyPropertyIds = org.apache.ambari.server.controller.utilities.PropertyHelper.getKeyPropertyIds(type);
        switch (type.getInternalType()) {
            case DRFeed :
                return new org.apache.ambari.server.controller.internal.FeedResourceProvider(service);
            case DRTargetCluster :
                return new org.apache.ambari.server.controller.internal.TargetClusterResourceProvider(service);
            case DRInstance :
                return new org.apache.ambari.server.controller.internal.InstanceResourceProvider(service);
        }
        return super.createResourceProvider(type);
    }
}