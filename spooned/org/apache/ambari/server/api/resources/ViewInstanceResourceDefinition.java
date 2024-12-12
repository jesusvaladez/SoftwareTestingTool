package org.apache.ambari.server.api.resources;
public class ViewInstanceResourceDefinition extends org.apache.ambari.server.api.resources.BaseResourceDefinition {
    private final java.util.Set<org.apache.ambari.server.api.resources.SubResourceDefinition> subResourceDefinitions;

    public ViewInstanceResourceDefinition(java.util.Set<org.apache.ambari.server.api.resources.SubResourceDefinition> subResourceDefinitions) {
        super(org.apache.ambari.server.controller.spi.Resource.Type.ViewInstance);
        this.subResourceDefinitions = (subResourceDefinitions == null) ? new java.util.HashSet<>() : new java.util.HashSet<>(subResourceDefinitions);
        this.subResourceDefinitions.add(new org.apache.ambari.server.api.resources.SubResourceDefinition(org.apache.ambari.server.controller.spi.Resource.Type.ViewPrivilege));
    }

    @java.lang.Override
    public java.lang.String getPluralName() {
        return "instances";
    }

    @java.lang.Override
    public java.lang.String getSingularName() {
        return "instance";
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.api.resources.SubResourceDefinition> getSubResourceDefinitions() {
        return subResourceDefinitions;
    }
}