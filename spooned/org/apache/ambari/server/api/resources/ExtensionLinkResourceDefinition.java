package org.apache.ambari.server.api.resources;
public class ExtensionLinkResourceDefinition extends org.apache.ambari.server.api.resources.BaseResourceDefinition {
    public ExtensionLinkResourceDefinition() {
        super(org.apache.ambari.server.controller.spi.Resource.Type.ExtensionLink);
    }

    @java.lang.Override
    public java.lang.String getPluralName() {
        return "links";
    }

    @java.lang.Override
    public java.lang.String getSingularName() {
        return "link";
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.api.resources.SubResourceDefinition> getSubResourceDefinitions() {
        java.util.Set<org.apache.ambari.server.api.resources.SubResourceDefinition> setChildren = new java.util.HashSet<>();
        return setChildren;
    }
}