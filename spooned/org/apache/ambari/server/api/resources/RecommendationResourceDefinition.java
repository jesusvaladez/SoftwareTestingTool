package org.apache.ambari.server.api.resources;
public class RecommendationResourceDefinition extends org.apache.ambari.server.api.resources.BaseResourceDefinition {
    public RecommendationResourceDefinition() {
        super(org.apache.ambari.server.controller.spi.Resource.Type.Recommendation);
    }

    @java.lang.Override
    public java.lang.String getPluralName() {
        return "recommendations";
    }

    @java.lang.Override
    public java.lang.String getSingularName() {
        return "recommendation";
    }

    @java.lang.Override
    public boolean isCreatable() {
        return false;
    }
}