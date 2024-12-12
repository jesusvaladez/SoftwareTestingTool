package org.apache.ambari.server.api.resources;
public class BlueprintResourceDefinition extends org.apache.ambari.server.api.resources.BaseResourceDefinition {
    public BlueprintResourceDefinition() {
        super(org.apache.ambari.server.controller.spi.Resource.Type.Blueprint);
    }

    @java.lang.Override
    public java.lang.String getPluralName() {
        return "blueprints";
    }

    @java.lang.Override
    public java.lang.String getSingularName() {
        return "blueprint";
    }

    @java.lang.Override
    public java.util.Collection<java.lang.String> getCreateDirectives() {
        java.util.Collection<java.lang.String> directives = super.getCreateDirectives();
        directives.add("validate_topology");
        return directives;
    }
}