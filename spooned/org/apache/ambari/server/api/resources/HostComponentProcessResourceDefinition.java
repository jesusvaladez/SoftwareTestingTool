package org.apache.ambari.server.api.resources;
public class HostComponentProcessResourceDefinition extends org.apache.ambari.server.api.resources.BaseResourceDefinition {
    public HostComponentProcessResourceDefinition() {
        super(org.apache.ambari.server.controller.spi.Resource.Type.HostComponentProcess);
    }

    @java.lang.Override
    public java.lang.String getPluralName() {
        return "processes";
    }

    @java.lang.Override
    public java.lang.String getSingularName() {
        return "process";
    }
}