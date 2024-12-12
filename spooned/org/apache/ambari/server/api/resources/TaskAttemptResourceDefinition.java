package org.apache.ambari.server.api.resources;
public class TaskAttemptResourceDefinition extends org.apache.ambari.server.api.resources.BaseResourceDefinition {
    public TaskAttemptResourceDefinition() {
        super(org.apache.ambari.server.controller.spi.Resource.Type.TaskAttempt);
    }

    @java.lang.Override
    public java.lang.String getPluralName() {
        return "taskattempts";
    }

    @java.lang.Override
    public java.lang.String getSingularName() {
        return "taskattempt";
    }
}