package org.apache.ambari.server.api.resources;
public class MemberResourceDefinition extends org.apache.ambari.server.api.resources.BaseResourceDefinition {
    public MemberResourceDefinition() {
        super(org.apache.ambari.server.controller.spi.Resource.Type.Member);
    }

    @java.lang.Override
    public java.lang.String getPluralName() {
        return "members";
    }

    @java.lang.Override
    public java.lang.String getSingularName() {
        return "member";
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.api.resources.SubResourceDefinition> getSubResourceDefinitions() {
        return java.util.Collections.emptySet();
    }
}