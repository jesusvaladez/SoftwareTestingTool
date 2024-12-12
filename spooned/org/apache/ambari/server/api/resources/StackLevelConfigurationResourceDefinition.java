package org.apache.ambari.server.api.resources;
public class StackLevelConfigurationResourceDefinition extends org.apache.ambari.server.api.resources.BaseResourceDefinition {
    public StackLevelConfigurationResourceDefinition(org.apache.ambari.server.controller.spi.Resource.Type resourceType) {
        super(resourceType);
    }

    public StackLevelConfigurationResourceDefinition() {
        super(org.apache.ambari.server.controller.spi.Resource.Type.StackLevelConfiguration);
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