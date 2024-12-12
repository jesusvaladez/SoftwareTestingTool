package org.apache.ambari.server.api.resources;
public class DetachedHostResourceDefinition extends org.apache.ambari.server.api.resources.BaseResourceDefinition {
    public DetachedHostResourceDefinition() {
        super(org.apache.ambari.server.controller.spi.Resource.Type.Host);
    }

    @java.lang.Override
    public java.lang.String getPluralName() {
        return "hosts";
    }

    @java.lang.Override
    public java.lang.String getSingularName() {
        return "host";
    }
}