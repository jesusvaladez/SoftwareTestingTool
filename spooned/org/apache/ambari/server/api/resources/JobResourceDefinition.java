package org.apache.ambari.server.api.resources;
public class JobResourceDefinition extends org.apache.ambari.server.api.resources.BaseResourceDefinition {
    public JobResourceDefinition() {
        super(org.apache.ambari.server.controller.spi.Resource.Type.Job);
    }

    @java.lang.Override
    public java.lang.String getPluralName() {
        return "jobs";
    }

    @java.lang.Override
    public java.lang.String getSingularName() {
        return "job";
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.api.resources.SubResourceDefinition> getSubResourceDefinitions() {
        java.util.Set<org.apache.ambari.server.api.resources.SubResourceDefinition> setChildren = new java.util.HashSet<>();
        setChildren.add(new org.apache.ambari.server.api.resources.SubResourceDefinition(org.apache.ambari.server.controller.spi.Resource.Type.TaskAttempt));
        return setChildren;
    }
}