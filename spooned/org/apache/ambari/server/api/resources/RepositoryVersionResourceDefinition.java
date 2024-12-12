package org.apache.ambari.server.api.resources;
public class RepositoryVersionResourceDefinition extends org.apache.ambari.server.api.resources.BaseResourceDefinition {
    public RepositoryVersionResourceDefinition() {
        super(org.apache.ambari.server.controller.spi.Resource.Type.RepositoryVersion);
    }

    @java.lang.Override
    public java.lang.String getPluralName() {
        return "repository_versions";
    }

    @java.lang.Override
    public java.lang.String getSingularName() {
        return "repository_version";
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.api.resources.SubResourceDefinition> getSubResourceDefinitions() {
        return java.util.Collections.singleton(new org.apache.ambari.server.api.resources.SubResourceDefinition(org.apache.ambari.server.controller.spi.Resource.Type.OperatingSystem));
    }
}