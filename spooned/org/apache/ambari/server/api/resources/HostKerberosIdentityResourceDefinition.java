package org.apache.ambari.server.api.resources;
public class HostKerberosIdentityResourceDefinition extends org.apache.ambari.server.api.resources.BaseResourceDefinition {
    public HostKerberosIdentityResourceDefinition() {
        super(org.apache.ambari.server.controller.spi.Resource.Type.HostKerberosIdentity);
    }

    @java.lang.Override
    public java.lang.String getPluralName() {
        return "kerberos_identities";
    }

    @java.lang.Override
    public java.lang.String getSingularName() {
        return "kerberos_identity";
    }

    @java.lang.Override
    public org.apache.ambari.server.api.query.render.Renderer getRenderer(java.lang.String name) {
        if ("csv".equalsIgnoreCase(name)) {
            return new org.apache.ambari.server.api.query.render.HostKerberosIdentityCsvRenderer();
        } else {
            return super.getRenderer(name);
        }
    }
}