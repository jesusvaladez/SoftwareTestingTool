package org.apache.ambari.server.api.resources;
public class StackDependencyResourceDefinition extends org.apache.ambari.server.api.resources.BaseResourceDefinition {
    public StackDependencyResourceDefinition() {
        super(org.apache.ambari.server.controller.spi.Resource.Type.StackServiceComponentDependency);
    }

    @java.lang.Override
    public java.lang.String getPluralName() {
        return "dependencies";
    }

    @java.lang.Override
    public java.lang.String getSingularName() {
        return "dependency";
    }
}