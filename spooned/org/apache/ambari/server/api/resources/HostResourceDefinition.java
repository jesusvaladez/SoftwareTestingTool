package org.apache.ambari.server.api.resources;
public class HostResourceDefinition extends org.apache.ambari.server.api.resources.BaseResourceDefinition {
    public HostResourceDefinition() {
        super(org.apache.ambari.server.controller.spi.Resource.Type.Host);
    }

    @java.lang.Override
    public java.lang.String getPluralName() {
        return "hosts";
    }

    @java.lang.Override
    public java.lang.String getSingularName() {
        return "host";
    }

    @java.lang.Override
    public java.util.Collection<java.lang.String> getDeleteDirectives() {
        return java.util.Collections.singleton(org.apache.ambari.server.controller.spi.Request.DIRECTIVE_DRY_RUN);
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.api.resources.SubResourceDefinition> getSubResourceDefinitions() {
        java.util.Set<org.apache.ambari.server.api.resources.SubResourceDefinition> subs = new java.util.HashSet<>();
        subs.add(new org.apache.ambari.server.api.resources.SubResourceDefinition(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent));
        subs.add(new org.apache.ambari.server.api.resources.SubResourceDefinition(org.apache.ambari.server.controller.spi.Resource.Type.Alert));
        subs.add(new org.apache.ambari.server.api.resources.SubResourceDefinition(org.apache.ambari.server.controller.spi.Resource.Type.HostStackVersion));
        subs.add(new org.apache.ambari.server.api.resources.SubResourceDefinition(org.apache.ambari.server.controller.spi.Resource.Type.HostKerberosIdentity));
        return subs;
    }
}