package org.apache.ambari.server.api.resources;
public class AlertTargetResourceDefinition extends org.apache.ambari.server.api.resources.BaseResourceDefinition {
    public static final java.lang.String VALIDATE_CONFIG_DIRECTIVE = "validate_config";

    public static final java.lang.String OVERWRITE_DIRECTIVE = "overwrite_existing";

    public AlertTargetResourceDefinition() {
        super(org.apache.ambari.server.controller.spi.Resource.Type.AlertTarget);
    }

    @java.lang.Override
    public java.lang.String getPluralName() {
        return "alert_targets";
    }

    @java.lang.Override
    public java.lang.String getSingularName() {
        return "alert_target";
    }

    @java.lang.Override
    public java.util.Collection<java.lang.String> getCreateDirectives() {
        java.util.Collection<java.lang.String> directives = super.getCreateDirectives();
        directives.add(org.apache.ambari.server.api.resources.AlertTargetResourceDefinition.VALIDATE_CONFIG_DIRECTIVE);
        directives.add(org.apache.ambari.server.api.resources.AlertTargetResourceDefinition.OVERWRITE_DIRECTIVE);
        return directives;
    }
}