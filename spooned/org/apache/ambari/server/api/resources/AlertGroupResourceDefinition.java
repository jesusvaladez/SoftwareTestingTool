package org.apache.ambari.server.api.resources;
public class AlertGroupResourceDefinition extends org.apache.ambari.server.api.resources.BaseResourceDefinition {
    public AlertGroupResourceDefinition() {
        super(org.apache.ambari.server.controller.spi.Resource.Type.AlertGroup);
    }

    @java.lang.Override
    public java.lang.String getPluralName() {
        return "alert_groups";
    }

    @java.lang.Override
    public java.lang.String getSingularName() {
        return "alert_group";
    }
}