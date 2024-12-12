package org.apache.ambari.server.api.query.render;
public interface Renderer {
    void init(org.apache.ambari.server.controller.spi.SchemaFactory schemaFactory);

    org.apache.ambari.server.api.util.TreeNode<java.util.Set<java.lang.String>> finalizeProperties(org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.api.query.QueryInfo> queryProperties, boolean isCollection);

    org.apache.ambari.server.api.services.Result finalizeResult(org.apache.ambari.server.api.services.Result queryResult);

    org.apache.ambari.server.api.services.ResultPostProcessor getResultPostProcessor(org.apache.ambari.server.api.services.Request request);

    boolean requiresPropertyProviderInput();
}