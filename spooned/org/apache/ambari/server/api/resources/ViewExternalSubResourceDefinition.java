package org.apache.ambari.server.api.resources;
public class ViewExternalSubResourceDefinition extends org.apache.ambari.server.api.resources.BaseResourceDefinition {
    public ViewExternalSubResourceDefinition(org.apache.ambari.server.controller.spi.Resource.Type type) {
        super(type);
    }

    @java.lang.Override
    public java.lang.String getPluralName() {
        return "resources";
    }

    @java.lang.Override
    public java.lang.String getSingularName() {
        return "resource";
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.api.resources.SubResourceDefinition> getSubResourceDefinitions() {
        return java.util.Collections.emptySet();
    }
}