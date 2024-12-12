package org.apache.ambari.server.api.resources;
public class WorkflowResourceDefinition extends org.apache.ambari.server.api.resources.BaseResourceDefinition {
    public WorkflowResourceDefinition() {
        super(org.apache.ambari.server.controller.spi.Resource.Type.Workflow);
    }

    @java.lang.Override
    public java.lang.String getPluralName() {
        return "workflows";
    }

    @java.lang.Override
    public java.lang.String getSingularName() {
        return "workflow";
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.api.resources.SubResourceDefinition> getSubResourceDefinitions() {
        java.util.Set<org.apache.ambari.server.api.resources.SubResourceDefinition> setChildren = new java.util.HashSet<>();
        setChildren.add(new org.apache.ambari.server.api.resources.SubResourceDefinition(org.apache.ambari.server.controller.spi.Resource.Type.Job));
        return setChildren;
    }
}