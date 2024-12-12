package org.apache.ambari.server.api.resources;
public class PermissionResourceDefinition extends org.apache.ambari.server.api.resources.BaseResourceDefinition {
    public PermissionResourceDefinition() {
        super(org.apache.ambari.server.controller.spi.Resource.Type.Permission);
    }

    @java.lang.Override
    public java.lang.String getPluralName() {
        return "permissions";
    }

    @java.lang.Override
    public java.lang.String getSingularName() {
        return "permission";
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.api.resources.SubResourceDefinition> getSubResourceDefinitions() {
        java.util.Set<org.apache.ambari.server.api.resources.SubResourceDefinition> subResourceDefinitions = new java.util.HashSet<>();
        subResourceDefinitions.add(new org.apache.ambari.server.api.resources.SubResourceDefinition(org.apache.ambari.server.controller.spi.Resource.Type.RoleAuthorization));
        return subResourceDefinitions;
    }
}