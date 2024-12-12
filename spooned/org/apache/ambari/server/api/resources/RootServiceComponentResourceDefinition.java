package org.apache.ambari.server.api.resources;
public class RootServiceComponentResourceDefinition extends org.apache.ambari.server.api.resources.BaseResourceDefinition {
    public RootServiceComponentResourceDefinition(org.apache.ambari.server.controller.spi.Resource.Type resourceType) {
        super(resourceType);
    }

    public RootServiceComponentResourceDefinition() {
        super(org.apache.ambari.server.controller.spi.Resource.Type.RootServiceComponent);
    }

    @java.lang.Override
    public java.lang.String getPluralName() {
        return "components";
    }

    @java.lang.Override
    public java.lang.String getSingularName() {
        return "component";
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.api.resources.SubResourceDefinition> getSubResourceDefinitions() {
        java.util.Set<org.apache.ambari.server.api.resources.SubResourceDefinition> definitions = new java.util.HashSet<>();
        definitions.add(new org.apache.ambari.server.api.resources.SubResourceDefinition(org.apache.ambari.server.controller.spi.Resource.Type.RootServiceHostComponent, java.util.Collections.singleton(org.apache.ambari.server.controller.spi.Resource.Type.Host), true));
        definitions.add(new org.apache.ambari.server.api.resources.SubResourceDefinition(org.apache.ambari.server.controller.spi.Resource.Type.RootServiceComponentConfiguration));
        return definitions;
    }
}