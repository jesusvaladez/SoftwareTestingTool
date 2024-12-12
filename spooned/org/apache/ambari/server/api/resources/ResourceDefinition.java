package org.apache.ambari.server.api.resources;
public interface ResourceDefinition {
    java.lang.String getPluralName();

    java.lang.String getSingularName();

    org.apache.ambari.server.controller.spi.Resource.Type getType();

    java.util.Set<org.apache.ambari.server.api.resources.SubResourceDefinition> getSubResourceDefinitions();

    java.util.List<org.apache.ambari.server.api.resources.ResourceDefinition.PostProcessor> getPostProcessors();

    org.apache.ambari.server.api.query.render.Renderer getRenderer(java.lang.String name) throws java.lang.IllegalArgumentException;

    java.util.Collection<java.lang.String> getReadDirectives();

    java.util.Collection<java.lang.String> getCreateDirectives();

    java.util.Collection<java.lang.String> getUpdateDirectives();

    java.util.Collection<java.lang.String> getDeleteDirectives();

    boolean isCreatable();

    interface PostProcessor {
        void process(org.apache.ambari.server.api.services.Request request, org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> resultNode, java.lang.String href);
    }
}