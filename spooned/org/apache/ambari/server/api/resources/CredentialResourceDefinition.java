package org.apache.ambari.server.api.resources;
public class CredentialResourceDefinition extends org.apache.ambari.server.api.resources.BaseResourceDefinition {
    public CredentialResourceDefinition() {
        super(org.apache.ambari.server.controller.spi.Resource.Type.Credential);
    }

    @java.lang.Override
    public java.lang.String getPluralName() {
        return "credentials";
    }

    @java.lang.Override
    public java.lang.String getSingularName() {
        return "credential";
    }
}