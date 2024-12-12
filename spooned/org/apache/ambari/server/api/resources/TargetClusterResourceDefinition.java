package org.apache.ambari.server.api.resources;
public class TargetClusterResourceDefinition extends org.apache.ambari.server.api.resources.BaseResourceDefinition {
    public TargetClusterResourceDefinition() {
        super(org.apache.ambari.server.controller.spi.Resource.Type.DRTargetCluster);
    }

    @java.lang.Override
    public java.lang.String getPluralName() {
        return "targets";
    }

    @java.lang.Override
    public java.lang.String getSingularName() {
        return "target";
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.api.resources.SubResourceDefinition> getSubResourceDefinitions() {
        return java.util.Collections.emptySet();
    }
}