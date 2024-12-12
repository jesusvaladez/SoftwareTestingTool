package org.apache.ambari.server.api.resources;
public class InstanceResourceDefinition extends org.apache.ambari.server.api.resources.BaseResourceDefinition {
    public InstanceResourceDefinition() {
        super(org.apache.ambari.server.controller.spi.Resource.Type.DRInstance);
    }

    @java.lang.Override
    public java.lang.String getPluralName() {
        return "instances";
    }

    @java.lang.Override
    public java.lang.String getSingularName() {
        return "instance";
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.api.resources.SubResourceDefinition> getSubResourceDefinitions() {
        return java.util.Collections.emptySet();
    }
}