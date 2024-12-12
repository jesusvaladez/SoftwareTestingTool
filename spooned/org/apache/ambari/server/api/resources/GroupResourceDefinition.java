package org.apache.ambari.server.api.resources;
public class GroupResourceDefinition extends org.apache.ambari.server.api.resources.BaseResourceDefinition {
    public GroupResourceDefinition() {
        super(org.apache.ambari.server.controller.spi.Resource.Type.Group);
    }

    @java.lang.Override
    public java.lang.String getPluralName() {
        return "groups";
    }

    @java.lang.Override
    public java.lang.String getSingularName() {
        return "group";
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.api.resources.SubResourceDefinition> getSubResourceDefinitions() {
        final java.util.Set<org.apache.ambari.server.api.resources.SubResourceDefinition> subResourceDefinitions = new java.util.HashSet<>();
        subResourceDefinitions.add(new org.apache.ambari.server.api.resources.SubResourceDefinition(org.apache.ambari.server.controller.spi.Resource.Type.Member));
        subResourceDefinitions.add(new org.apache.ambari.server.api.resources.SubResourceDefinition(org.apache.ambari.server.controller.spi.Resource.Type.GroupPrivilege));
        return subResourceDefinitions;
    }
}