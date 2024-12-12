package org.apache.ambari.server.api.resources;
public class RootServiceResourceDefinition extends org.apache.ambari.server.api.resources.BaseResourceDefinition {
    public RootServiceResourceDefinition(org.apache.ambari.server.controller.spi.Resource.Type resourceType) {
        super(resourceType);
    }

    public RootServiceResourceDefinition() {
        super(org.apache.ambari.server.controller.spi.Resource.Type.RootService);
    }

    @java.lang.Override
    public java.lang.String getPluralName() {
        return "rootServices";
    }

    @java.lang.Override
    public java.lang.String getSingularName() {
        return "rootService";
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.api.resources.SubResourceDefinition> getSubResourceDefinitions() {
        java.util.Set<org.apache.ambari.server.api.resources.SubResourceDefinition> setChildren = new java.util.HashSet<>();
        setChildren.add(new org.apache.ambari.server.api.resources.SubResourceDefinition(org.apache.ambari.server.controller.spi.Resource.Type.RootServiceComponent));
        return setChildren;
    }
}