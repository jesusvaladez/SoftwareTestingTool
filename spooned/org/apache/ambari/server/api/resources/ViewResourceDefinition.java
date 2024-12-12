package org.apache.ambari.server.api.resources;
public class ViewResourceDefinition extends org.apache.ambari.server.api.resources.BaseResourceDefinition {
    public ViewResourceDefinition() {
        super(org.apache.ambari.server.controller.spi.Resource.Type.View);
    }

    @java.lang.Override
    public java.lang.String getPluralName() {
        return "views";
    }

    @java.lang.Override
    public java.lang.String getSingularName() {
        return "view";
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.api.resources.SubResourceDefinition> getSubResourceDefinitions() {
        return java.util.Collections.singleton(new org.apache.ambari.server.api.resources.SubResourceDefinition(org.apache.ambari.server.controller.spi.Resource.Type.ViewVersion));
    }
}