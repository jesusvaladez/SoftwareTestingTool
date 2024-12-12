package org.apache.ambari.server.api.resources;
public class RepositoryResourceDefinition extends org.apache.ambari.server.api.resources.BaseResourceDefinition {
    public static final java.lang.String VALIDATE_ONLY_DIRECTIVE = "validate_only";

    public RepositoryResourceDefinition() {
        super(org.apache.ambari.server.controller.spi.Resource.Type.Repository);
    }

    @java.lang.Override
    public java.lang.String getPluralName() {
        return "repositories";
    }

    @java.lang.Override
    public java.lang.String getSingularName() {
        return "repository";
    }

    @java.lang.Override
    public java.util.Collection<java.lang.String> getCreateDirectives() {
        return com.google.common.collect.Sets.newHashSet(org.apache.ambari.server.api.resources.RepositoryResourceDefinition.VALIDATE_ONLY_DIRECTIVE);
    }
}