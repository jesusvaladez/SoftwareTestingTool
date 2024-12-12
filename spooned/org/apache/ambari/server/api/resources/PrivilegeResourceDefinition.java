package org.apache.ambari.server.api.resources;
public class PrivilegeResourceDefinition extends org.apache.ambari.server.api.resources.BaseResourceDefinition {
    public PrivilegeResourceDefinition(org.apache.ambari.server.controller.spi.Resource.Type type) {
        super(type);
    }

    @java.lang.Override
    public java.lang.String getPluralName() {
        return "privileges";
    }

    @java.lang.Override
    public java.lang.String getSingularName() {
        return "privilege";
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.api.resources.SubResourceDefinition> getSubResourceDefinitions() {
        return java.util.Collections.emptySet();
    }
}