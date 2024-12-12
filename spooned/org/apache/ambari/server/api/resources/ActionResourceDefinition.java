package org.apache.ambari.server.api.resources;
public class ActionResourceDefinition extends org.apache.ambari.server.api.resources.BaseResourceDefinition {
    public ActionResourceDefinition() {
        super(org.apache.ambari.server.controller.spi.Resource.Type.Action);
    }

    @java.lang.Override
    public java.lang.String getPluralName() {
        return "actions";
    }

    @java.lang.Override
    public java.lang.String getSingularName() {
        return "action";
    }
}