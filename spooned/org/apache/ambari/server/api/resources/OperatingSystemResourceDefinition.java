package org.apache.ambari.server.api.resources;
public class OperatingSystemResourceDefinition extends org.apache.ambari.server.api.resources.BaseResourceDefinition {
    public OperatingSystemResourceDefinition(org.apache.ambari.server.controller.spi.Resource.Type resourceType) {
        super(resourceType);
    }

    public OperatingSystemResourceDefinition() {
        super(org.apache.ambari.server.controller.spi.Resource.Type.OperatingSystem);
    }

    @java.lang.Override
    public java.lang.String getPluralName() {
        return "operating_systems";
    }

    @java.lang.Override
    public java.lang.String getSingularName() {
        return "operating_system";
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.api.resources.SubResourceDefinition> getSubResourceDefinitions() {
        return java.util.Collections.singleton(new org.apache.ambari.server.api.resources.SubResourceDefinition(org.apache.ambari.server.controller.spi.Resource.Type.Repository));
    }
}