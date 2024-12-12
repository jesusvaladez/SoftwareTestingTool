package org.apache.ambari.server.api.resources;
public class ExtensionResourceDefinition extends org.apache.ambari.server.api.resources.BaseResourceDefinition {
    public ExtensionResourceDefinition() {
        super(org.apache.ambari.server.controller.spi.Resource.Type.Extension);
    }

    @java.lang.Override
    public java.lang.String getPluralName() {
        return "extensions";
    }

    @java.lang.Override
    public java.lang.String getSingularName() {
        return "extension";
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.api.resources.SubResourceDefinition> getSubResourceDefinitions() {
        java.util.Set<org.apache.ambari.server.api.resources.SubResourceDefinition> setChildren = new java.util.HashSet<>();
        setChildren.add(new org.apache.ambari.server.api.resources.SubResourceDefinition(org.apache.ambari.server.controller.spi.Resource.Type.ExtensionVersion));
        return setChildren;
    }
}