package org.apache.ambari.server.api.resources;
public class AlertDefResourceDefinition extends org.apache.ambari.server.api.resources.BaseResourceDefinition {
    public static final java.lang.String EXECUTE_IMMEDIATE_DIRECTIVE = "run_now";

    public AlertDefResourceDefinition() {
        super(org.apache.ambari.server.controller.spi.Resource.Type.AlertDefinition);
    }

    @java.lang.Override
    public java.lang.String getPluralName() {
        return "alert_definitions";
    }

    @java.lang.Override
    public java.lang.String getSingularName() {
        return "alert_definition";
    }

    @java.lang.Override
    public java.util.Collection<java.lang.String> getUpdateDirectives() {
        java.util.Collection<java.lang.String> directives = super.getCreateDirectives();
        directives.add(org.apache.ambari.server.api.resources.AlertDefResourceDefinition.EXECUTE_IMMEDIATE_DIRECTIVE);
        return directives;
    }
}