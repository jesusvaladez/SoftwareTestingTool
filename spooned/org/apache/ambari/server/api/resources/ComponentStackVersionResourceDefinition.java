package org.apache.ambari.server.api.resources;
public class ComponentStackVersionResourceDefinition extends org.apache.ambari.server.api.resources.BaseResourceDefinition {
    public ComponentStackVersionResourceDefinition(org.apache.ambari.server.controller.spi.Resource.Type type) {
        super(type);
    }

    @java.lang.Override
    public java.lang.String getPluralName() {
        return "stack_versions";
    }

    @java.lang.Override
    public java.lang.String getSingularName() {
        return "stack_version";
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.api.resources.SubResourceDefinition> getSubResourceDefinitions() {
        final java.util.Set<org.apache.ambari.server.api.resources.SubResourceDefinition> subResourceDefintions = new java.util.HashSet<>();
        subResourceDefintions.add(new org.apache.ambari.server.api.resources.SubResourceDefinition(org.apache.ambari.server.controller.spi.Resource.Type.RepositoryVersion));
        return subResourceDefintions;
    }
}