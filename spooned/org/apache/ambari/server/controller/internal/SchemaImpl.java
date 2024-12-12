package org.apache.ambari.server.controller.internal;
public class SchemaImpl implements org.apache.ambari.server.controller.spi.Schema {
    private final org.apache.ambari.server.controller.spi.ResourceProvider resourceProvider;

    public SchemaImpl(org.apache.ambari.server.controller.spi.ResourceProvider resourceProvider) {
        this.resourceProvider = resourceProvider;
    }

    @java.lang.Override
    public java.lang.String getKeyPropertyId(org.apache.ambari.server.controller.spi.Resource.Type type) {
        return resourceProvider.getKeyPropertyIds().get(type);
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.controller.spi.Resource.Type> getKeyTypes() {
        return resourceProvider.getKeyPropertyIds().keySet();
    }
}