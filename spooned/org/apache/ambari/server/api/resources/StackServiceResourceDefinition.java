package org.apache.ambari.server.api.resources;
public class StackServiceResourceDefinition extends org.apache.ambari.server.api.resources.BaseResourceDefinition {
    public StackServiceResourceDefinition(org.apache.ambari.server.controller.spi.Resource.Type resourceType) {
        super(resourceType);
    }

    public StackServiceResourceDefinition() {
        super(org.apache.ambari.server.controller.spi.Resource.Type.StackService);
    }

    @java.lang.Override
    public java.lang.String getPluralName() {
        return "services";
    }

    @java.lang.Override
    public java.lang.String getSingularName() {
        return "service";
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.api.resources.SubResourceDefinition> getSubResourceDefinitions() {
        java.util.Set<org.apache.ambari.server.api.resources.SubResourceDefinition> setChildren = new java.util.HashSet<>();
        setChildren.add(new org.apache.ambari.server.api.resources.SubResourceDefinition(org.apache.ambari.server.controller.spi.Resource.Type.StackConfiguration));
        setChildren.add(new org.apache.ambari.server.api.resources.SubResourceDefinition(org.apache.ambari.server.controller.spi.Resource.Type.StackServiceComponent));
        setChildren.add(new org.apache.ambari.server.api.resources.SubResourceDefinition(org.apache.ambari.server.controller.spi.Resource.Type.StackArtifact));
        setChildren.add(new org.apache.ambari.server.api.resources.SubResourceDefinition(org.apache.ambari.server.controller.spi.Resource.Type.Theme));
        setChildren.add(new org.apache.ambari.server.api.resources.SubResourceDefinition(org.apache.ambari.server.controller.spi.Resource.Type.QuickLink));
        return setChildren;
    }
}