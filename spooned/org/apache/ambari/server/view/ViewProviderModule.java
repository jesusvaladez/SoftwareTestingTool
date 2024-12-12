package org.apache.ambari.server.view;
public class ViewProviderModule implements org.apache.ambari.server.controller.spi.ProviderModule {
    private final org.apache.ambari.server.controller.spi.ProviderModule providerModule;

    private ViewProviderModule(org.apache.ambari.server.controller.spi.ProviderModule providerModule) {
        this.providerModule = providerModule;
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.ResourceProvider getResourceProvider(org.apache.ambari.server.controller.spi.Resource.Type type) {
        java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, org.apache.ambari.server.controller.spi.ResourceProvider> resourceProviders = org.apache.ambari.server.view.ViewRegistry.getInstance().getResourceProviders();
        if (resourceProviders.containsKey(type)) {
            return resourceProviders.get(type);
        }
        return providerModule.getResourceProvider(type);
    }

    @java.lang.Override
    public java.util.List<org.apache.ambari.server.controller.spi.PropertyProvider> getPropertyProviders(org.apache.ambari.server.controller.spi.Resource.Type type) {
        return providerModule.getPropertyProviders(type);
    }

    public static org.apache.ambari.server.view.ViewProviderModule getViewProviderModule(org.apache.ambari.server.controller.spi.ProviderModule module) {
        return new org.apache.ambari.server.view.ViewProviderModule(module);
    }
}