package org.apache.ambari.server.api.resources;
public class ServiceResourceDefinition extends org.apache.ambari.server.api.resources.BaseResourceDefinition {
    public ServiceResourceDefinition() {
        super(org.apache.ambari.server.controller.spi.Resource.Type.Service);
    }

    @java.lang.Override
    public java.lang.String getPluralName() {
        return "services";
    }

    @java.lang.Override
    public java.lang.String getSingularName() {
        return "service";
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.api.resources.SubResourceDefinition> getSubResourceDefinitions() {
        java.util.Set<org.apache.ambari.server.api.resources.SubResourceDefinition> subs = new java.util.HashSet<>();
        subs.add(new org.apache.ambari.server.api.resources.SubResourceDefinition(org.apache.ambari.server.controller.spi.Resource.Type.Component));
        subs.add(new org.apache.ambari.server.api.resources.SubResourceDefinition(org.apache.ambari.server.controller.spi.Resource.Type.Alert));
        subs.add(new org.apache.ambari.server.api.resources.SubResourceDefinition(org.apache.ambari.server.controller.spi.Resource.Type.Artifact));
        return subs;
    }
}