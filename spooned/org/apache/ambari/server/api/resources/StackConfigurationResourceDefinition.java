package org.apache.ambari.server.api.resources;
public class StackConfigurationResourceDefinition extends org.apache.ambari.server.api.resources.BaseResourceDefinition {
    public StackConfigurationResourceDefinition(org.apache.ambari.server.controller.spi.Resource.Type resourceType) {
        super(resourceType);
    }

    public StackConfigurationResourceDefinition() {
        super(org.apache.ambari.server.controller.spi.Resource.Type.StackConfiguration);
    }

    @java.lang.Override
    public java.lang.String getPluralName() {
        return "configurations";
    }

    @java.lang.Override
    public java.lang.String getSingularName() {
        return "configuration";
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.api.resources.SubResourceDefinition> getSubResourceDefinitions() {
        java.util.Set<org.apache.ambari.server.api.resources.SubResourceDefinition> subs = new java.util.HashSet<>();
        subs.add(new org.apache.ambari.server.api.resources.SubResourceDefinition(org.apache.ambari.server.controller.spi.Resource.Type.StackConfigurationDependency));
        return subs;
    }
}