package org.apache.ambari.server.api.resources;
public class FeedResourceDefinition extends org.apache.ambari.server.api.resources.BaseResourceDefinition {
    public FeedResourceDefinition() {
        super(org.apache.ambari.server.controller.spi.Resource.Type.DRFeed);
    }

    @java.lang.Override
    public java.lang.String getPluralName() {
        return "feeds";
    }

    @java.lang.Override
    public java.lang.String getSingularName() {
        return "feed";
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.api.resources.SubResourceDefinition> getSubResourceDefinitions() {
        java.util.Set<org.apache.ambari.server.api.resources.SubResourceDefinition> setChildren = new java.util.HashSet<>();
        setChildren.add(new org.apache.ambari.server.api.resources.SubResourceDefinition(org.apache.ambari.server.controller.spi.Resource.Type.DRInstance));
        return setChildren;
    }
}