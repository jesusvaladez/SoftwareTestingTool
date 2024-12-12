package org.apache.ambari.server.api.resources;
public class ViewPermissionResourceDefinition extends org.apache.ambari.server.api.resources.BaseResourceDefinition {
    public ViewPermissionResourceDefinition() {
        super(org.apache.ambari.server.controller.spi.Resource.Type.ViewPermission);
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
        return java.util.Collections.emptySet();
    }
}