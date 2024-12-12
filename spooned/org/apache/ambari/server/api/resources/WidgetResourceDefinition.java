package org.apache.ambari.server.api.resources;
public class WidgetResourceDefinition extends org.apache.ambari.server.api.resources.BaseResourceDefinition {
    public WidgetResourceDefinition() {
        super(org.apache.ambari.server.controller.spi.Resource.Type.Widget);
    }

    @java.lang.Override
    public java.lang.String getPluralName() {
        return "widgets";
    }

    @java.lang.Override
    public java.lang.String getSingularName() {
        return "widget";
    }
}