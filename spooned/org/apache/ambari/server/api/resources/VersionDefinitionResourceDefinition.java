package org.apache.ambari.server.api.resources;
public class VersionDefinitionResourceDefinition extends org.apache.ambari.server.api.resources.BaseResourceDefinition {
    public VersionDefinitionResourceDefinition() {
        super(org.apache.ambari.server.controller.spi.Resource.Type.VersionDefinition);
    }

    @java.lang.Override
    public java.lang.String getPluralName() {
        return "version_definitions";
    }

    @java.lang.Override
    public java.lang.String getSingularName() {
        return "version_definition";
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.api.resources.SubResourceDefinition> getSubResourceDefinitions() {
        return java.util.Collections.singleton(new org.apache.ambari.server.api.resources.SubResourceDefinition(org.apache.ambari.server.controller.spi.Resource.Type.OperatingSystem));
    }

    @java.lang.Override
    public java.util.Collection<java.lang.String> getCreateDirectives() {
        return com.google.common.collect.Sets.newHashSet(org.apache.ambari.server.controller.spi.Request.DIRECTIVE_DRY_RUN, org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.DIRECTIVE_SKIP_URL_CHECK);
    }
}