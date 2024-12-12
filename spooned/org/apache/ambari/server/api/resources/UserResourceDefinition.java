package org.apache.ambari.server.api.resources;
public class UserResourceDefinition extends org.apache.ambari.server.api.resources.BaseResourceDefinition {
    public UserResourceDefinition() {
        super(org.apache.ambari.server.controller.spi.Resource.Type.User);
    }

    @java.lang.Override
    public java.lang.String getPluralName() {
        return "users";
    }

    @java.lang.Override
    public java.lang.String getSingularName() {
        return "user";
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.api.resources.SubResourceDefinition> getSubResourceDefinitions() {
        final java.util.Set<org.apache.ambari.server.api.resources.SubResourceDefinition> subResourceDefinitions = new java.util.HashSet<>();
        subResourceDefinitions.add(new org.apache.ambari.server.api.resources.SubResourceDefinition(org.apache.ambari.server.controller.spi.Resource.Type.UserAuthenticationSource));
        subResourceDefinitions.add(new org.apache.ambari.server.api.resources.SubResourceDefinition(org.apache.ambari.server.controller.spi.Resource.Type.UserPrivilege));
        subResourceDefinitions.add(new org.apache.ambari.server.api.resources.SubResourceDefinition(org.apache.ambari.server.controller.spi.Resource.Type.ActiveWidgetLayout));
        return subResourceDefinitions;
    }
}