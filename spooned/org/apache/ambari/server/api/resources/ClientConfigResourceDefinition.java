package org.apache.ambari.server.api.resources;
public class ClientConfigResourceDefinition extends org.apache.ambari.server.api.resources.BaseResourceDefinition {
    public ClientConfigResourceDefinition() {
        super(org.apache.ambari.server.controller.spi.Resource.Type.ClientConfig);
    }

    @java.lang.Override
    public java.lang.String getPluralName() {
        return "configurations";
    }

    @java.lang.Override
    public java.lang.String getSingularName() {
        return "configuration";
    }

    @java.lang.Override
    public org.apache.ambari.server.api.query.render.Renderer getRenderer(java.lang.String name) {
        if ((name != null) && name.equals("client_config_tar")) {
            return new org.apache.ambari.server.api.query.render.DefaultRenderer();
        } else {
            return super.getRenderer(name);
        }
    }
}