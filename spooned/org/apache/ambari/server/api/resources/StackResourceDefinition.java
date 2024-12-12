package org.apache.ambari.server.api.resources;
public class StackResourceDefinition extends org.apache.ambari.server.api.resources.BaseResourceDefinition {
    public StackResourceDefinition() {
        super(org.apache.ambari.server.controller.spi.Resource.Type.Stack);
    }

    @java.lang.Override
    public java.lang.String getPluralName() {
        return "stacks";
    }

    @java.lang.Override
    public java.lang.String getSingularName() {
        return "stack";
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.api.resources.SubResourceDefinition> getSubResourceDefinitions() {
        java.util.Set<org.apache.ambari.server.api.resources.SubResourceDefinition> setChildren = new java.util.HashSet<>();
        setChildren.add(new org.apache.ambari.server.api.resources.SubResourceDefinition(org.apache.ambari.server.controller.spi.Resource.Type.StackVersion));
        return setChildren;
    }
}