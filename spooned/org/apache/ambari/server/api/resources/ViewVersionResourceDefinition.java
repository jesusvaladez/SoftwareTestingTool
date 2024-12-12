package org.apache.ambari.server.api.resources;
public class ViewVersionResourceDefinition extends org.apache.ambari.server.api.resources.BaseResourceDefinition {
    public ViewVersionResourceDefinition() {
        super(org.apache.ambari.server.controller.spi.Resource.Type.ViewVersion);
    }

    @java.lang.Override
    public java.lang.String getPluralName() {
        return "versions";
    }

    @java.lang.Override
    public java.lang.String getSingularName() {
        return "version";
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.api.resources.SubResourceDefinition> getSubResourceDefinitions() {
        java.util.Set<org.apache.ambari.server.api.resources.SubResourceDefinition> subResourceDefinitions = new java.util.HashSet<>();
        subResourceDefinitions.add(new org.apache.ambari.server.api.resources.SubResourceDefinition(org.apache.ambari.server.controller.spi.Resource.Type.ViewInstance));
        subResourceDefinitions.add(new org.apache.ambari.server.api.resources.SubResourceDefinition(org.apache.ambari.server.controller.spi.Resource.Type.ViewPermission));
        return subResourceDefinitions;
    }
}