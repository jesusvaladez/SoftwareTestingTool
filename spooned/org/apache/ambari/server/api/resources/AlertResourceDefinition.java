package org.apache.ambari.server.api.resources;
public class AlertResourceDefinition extends org.apache.ambari.server.api.resources.BaseResourceDefinition {
    public AlertResourceDefinition() {
        super(org.apache.ambari.server.controller.spi.Resource.Type.Alert);
    }

    @java.lang.Override
    public java.lang.String getPluralName() {
        return "alerts";
    }

    @java.lang.Override
    public java.lang.String getSingularName() {
        return "alert";
    }

    @java.lang.Override
    public org.apache.ambari.server.api.query.render.Renderer getRenderer(java.lang.String name) {
        if (null == name) {
            return super.getRenderer(name);
        }
        if (name.equals("summary")) {
            return new org.apache.ambari.server.api.query.render.AlertSummaryRenderer();
        } else if (name.equals("groupedSummary")) {
            return new org.apache.ambari.server.api.query.render.AlertSummaryGroupedRenderer();
        } else {
            return super.getRenderer(name);
        }
    }
}