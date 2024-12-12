package org.apache.ambari.server.api.resources;
public class AlertHistoryResourceDefinition extends org.apache.ambari.server.api.resources.BaseResourceDefinition {
    public AlertHistoryResourceDefinition() {
        super(org.apache.ambari.server.controller.spi.Resource.Type.AlertHistory);
    }

    @java.lang.Override
    public java.lang.String getPluralName() {
        return "alert_history";
    }

    @java.lang.Override
    public java.lang.String getSingularName() {
        return "alert_history";
    }
}