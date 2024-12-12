package org.apache.ambari.server.api.resources;
public class ClusterResourceDefinition extends org.apache.ambari.server.api.resources.BaseResourceDefinition {
    public ClusterResourceDefinition() {
        super(org.apache.ambari.server.controller.spi.Resource.Type.Cluster);
    }

    @java.lang.Override
    public java.lang.String getPluralName() {
        return "clusters";
    }

    @java.lang.Override
    public java.lang.String getSingularName() {
        return "cluster";
    }

    @java.lang.Override
    public org.apache.ambari.server.api.query.render.Renderer getRenderer(java.lang.String name) {
        java.util.Optional<org.apache.ambari.server.controller.internal.BlueprintExportType> blueprintExportType = org.apache.ambari.server.controller.internal.BlueprintExportType.parse(name);
        return blueprintExportType.isPresent() ? new org.apache.ambari.server.api.query.render.ClusterBlueprintRenderer(blueprintExportType.get()) : super.getRenderer(name);
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.api.resources.SubResourceDefinition> getSubResourceDefinitions() {
        java.util.Set<org.apache.ambari.server.api.resources.SubResourceDefinition> setChildren = new java.util.HashSet<>();
        setChildren.add(new org.apache.ambari.server.api.resources.SubResourceDefinition(org.apache.ambari.server.controller.spi.Resource.Type.Service));
        setChildren.add(new org.apache.ambari.server.api.resources.SubResourceDefinition(org.apache.ambari.server.controller.spi.Resource.Type.Host));
        setChildren.add(new org.apache.ambari.server.api.resources.SubResourceDefinition(org.apache.ambari.server.controller.spi.Resource.Type.Configuration));
        setChildren.add(new org.apache.ambari.server.api.resources.SubResourceDefinition(org.apache.ambari.server.controller.spi.Resource.Type.ServiceConfigVersion));
        setChildren.add(new org.apache.ambari.server.api.resources.SubResourceDefinition(org.apache.ambari.server.controller.spi.Resource.Type.Request));
        setChildren.add(new org.apache.ambari.server.api.resources.SubResourceDefinition(org.apache.ambari.server.controller.spi.Resource.Type.Workflow));
        setChildren.add(new org.apache.ambari.server.api.resources.SubResourceDefinition(org.apache.ambari.server.controller.spi.Resource.Type.ConfigGroup));
        setChildren.add(new org.apache.ambari.server.api.resources.SubResourceDefinition(org.apache.ambari.server.controller.spi.Resource.Type.ClusterPrivilege));
        setChildren.add(new org.apache.ambari.server.api.resources.SubResourceDefinition(org.apache.ambari.server.controller.spi.Resource.Type.AlertDefinition));
        setChildren.add(new org.apache.ambari.server.api.resources.SubResourceDefinition(org.apache.ambari.server.controller.spi.Resource.Type.Alert));
        setChildren.add(new org.apache.ambari.server.api.resources.SubResourceDefinition(org.apache.ambari.server.controller.spi.Resource.Type.ClusterStackVersion));
        setChildren.add(new org.apache.ambari.server.api.resources.SubResourceDefinition(org.apache.ambari.server.controller.spi.Resource.Type.ClusterKerberosDescriptor));
        setChildren.add(new org.apache.ambari.server.api.resources.SubResourceDefinition(org.apache.ambari.server.controller.spi.Resource.Type.Artifact));
        return setChildren;
    }

    @java.lang.Override
    public java.util.Collection<java.lang.String> getUpdateDirectives() {
        java.util.Collection<java.lang.String> directives = super.getUpdateDirectives();
        directives.add(org.apache.ambari.server.controller.KerberosHelper.DIRECTIVE_REGENERATE_KEYTABS);
        directives.add(org.apache.ambari.server.controller.KerberosHelper.DIRECTIVE_MANAGE_KERBEROS_IDENTITIES);
        directives.add(org.apache.ambari.server.controller.KerberosHelper.DIRECTIVE_FORCE_TOGGLE_KERBEROS);
        directives.add(org.apache.ambari.server.controller.KerberosHelper.DIRECTIVE_HOSTS);
        directives.add(org.apache.ambari.server.controller.KerberosHelper.DIRECTIVE_COMPONENTS);
        directives.add(org.apache.ambari.server.controller.KerberosHelper.DIRECTIVE_IGNORE_CONFIGS);
        directives.add(org.apache.ambari.server.controller.KerberosHelper.DIRECTIVE_CONFIG_UPDATE_POLICY);
        return directives;
    }
}