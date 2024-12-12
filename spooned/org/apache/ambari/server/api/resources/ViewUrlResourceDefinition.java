package org.apache.ambari.server.api.resources;
public class ViewUrlResourceDefinition extends org.apache.ambari.server.api.resources.BaseResourceDefinition {
    public ViewUrlResourceDefinition() {
        super(org.apache.ambari.server.controller.spi.Resource.Type.ViewURL);
    }

    @java.lang.Override
    public java.lang.String getPluralName() {
        return "view_urls";
    }

    @java.lang.Override
    public java.lang.String getSingularName() {
        return "view_url";
    }
}