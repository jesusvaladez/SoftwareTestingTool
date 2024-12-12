package org.apache.ambari.server.api.resources;
public class ValidationResourceDefinition extends org.apache.ambari.server.api.resources.BaseResourceDefinition {
    public ValidationResourceDefinition() {
        super(org.apache.ambari.server.controller.spi.Resource.Type.Validation);
    }

    @java.lang.Override
    public java.lang.String getPluralName() {
        return "validations";
    }

    @java.lang.Override
    public java.lang.String getSingularName() {
        return "validation";
    }

    @java.lang.Override
    public boolean isCreatable() {
        return false;
    }
}