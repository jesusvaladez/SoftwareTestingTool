package org.apache.ambari.server.api.resources;
public class LoggingResourceDefinition extends org.apache.ambari.server.api.resources.BaseResourceDefinition {
    public LoggingResourceDefinition() {
        super(org.apache.ambari.server.controller.spi.Resource.Type.LoggingQuery);
    }

    @java.lang.Override
    public java.lang.String getPluralName() {
        return "logging";
    }

    @java.lang.Override
    public java.lang.String getSingularName() {
        return "logging";
    }
}