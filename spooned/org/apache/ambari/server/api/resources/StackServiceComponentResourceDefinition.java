package org.apache.ambari.server.api.resources;
public class StackServiceComponentResourceDefinition extends org.apache.ambari.server.api.resources.BaseResourceDefinition {
    public StackServiceComponentResourceDefinition() {
        super(org.apache.ambari.server.controller.spi.Resource.Type.StackServiceComponent);
    }

    @java.lang.Override
    public java.lang.String getPluralName() {
        return "components";
    }

    @java.lang.Override
    public java.lang.String getSingularName() {
        return "component";
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.api.resources.SubResourceDefinition> getSubResourceDefinitions() {
        return java.util.Collections.singleton(new org.apache.ambari.server.api.resources.SubResourceDefinition(org.apache.ambari.server.controller.spi.Resource.Type.StackServiceComponentDependency));
    }
}