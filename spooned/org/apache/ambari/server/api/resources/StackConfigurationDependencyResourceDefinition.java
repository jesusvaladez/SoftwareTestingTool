package org.apache.ambari.server.api.resources;
public class StackConfigurationDependencyResourceDefinition extends org.apache.ambari.server.api.resources.BaseResourceDefinition {
    public StackConfigurationDependencyResourceDefinition(org.apache.ambari.server.controller.spi.Resource.Type resourceType) {
        super(resourceType);
    }

    public StackConfigurationDependencyResourceDefinition() {
        super(org.apache.ambari.server.controller.spi.Resource.Type.StackConfigurationDependency);
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