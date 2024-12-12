package org.apache.ambari.server.api.resources;
public class TaskResourceDefinition extends org.apache.ambari.server.api.resources.BaseResourceDefinition {
    public TaskResourceDefinition() {
        super(org.apache.ambari.server.controller.spi.Resource.Type.Task);
    }

    @java.lang.Override
    public java.lang.String getPluralName() {
        return "tasks";
    }

    @java.lang.Override
    public java.lang.String getSingularName() {
        return "task";
    }
}