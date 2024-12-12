package org.apache.ambari.server.api.resources;
public class AuthResourceDefinition extends org.apache.ambari.server.api.resources.BaseResourceDefinition {
    public AuthResourceDefinition() {
        super(org.apache.ambari.server.controller.spi.Resource.Type.Auth);
    }

    @java.lang.Override
    public java.lang.String getPluralName() {
        return "auths";
    }

    @java.lang.Override
    public java.lang.String getSingularName() {
        return "auth";
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.api.resources.SubResourceDefinition> getSubResourceDefinitions() {
        return java.util.Collections.emptySet();
    }
}