package org.apache.ambari.server.controller.spi;
public interface ProviderModule {
    org.apache.ambari.server.controller.spi.ResourceProvider getResourceProvider(org.apache.ambari.server.controller.spi.Resource.Type type);

    java.util.List<org.apache.ambari.server.controller.spi.PropertyProvider> getPropertyProviders(org.apache.ambari.server.controller.spi.Resource.Type type);
}