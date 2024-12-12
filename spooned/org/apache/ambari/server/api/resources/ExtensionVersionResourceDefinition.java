package org.apache.ambari.server.api.resources;
public class ExtensionVersionResourceDefinition extends org.apache.ambari.server.api.resources.BaseResourceDefinition {
    public ExtensionVersionResourceDefinition() {
        super(org.apache.ambari.server.controller.spi.Resource.Type.ExtensionVersion);
    }

    @java.lang.Override
    public java.lang.String getPluralName() {
        return "versions";
    }

    @java.lang.Override
    public java.lang.String getSingularName() {
        return "version";
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.api.resources.SubResourceDefinition> getSubResourceDefinitions() {
        java.util.Set<org.apache.ambari.server.api.resources.SubResourceDefinition> children = new java.util.HashSet<>();
        return children;
    }
}