package org.apache.ambari.server.api.resources;
public class AlertNoticeResourceDefinition extends org.apache.ambari.server.api.resources.BaseResourceDefinition {
    public AlertNoticeResourceDefinition() {
        super(org.apache.ambari.server.controller.spi.Resource.Type.AlertNotice);
    }

    @java.lang.Override
    public java.lang.String getPluralName() {
        return "alert_notices";
    }

    @java.lang.Override
    public java.lang.String getSingularName() {
        return "alert_notice";
    }
}