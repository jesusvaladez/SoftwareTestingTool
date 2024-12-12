package org.apache.ambari.server.api.query.render;
public class DefaultRenderer extends org.apache.ambari.server.api.query.render.BaseRenderer implements org.apache.ambari.server.api.query.render.Renderer {
    @java.lang.Override
    public org.apache.ambari.server.api.util.TreeNode<java.util.Set<java.lang.String>> finalizeProperties(org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.api.query.QueryInfo> queryTree, boolean isCollection) {
        org.apache.ambari.server.api.query.QueryInfo queryInfo = queryTree.getObject();
        org.apache.ambari.server.api.util.TreeNode<java.util.Set<java.lang.String>> resultTree = new org.apache.ambari.server.api.util.TreeNodeImpl<>(null, queryInfo.getProperties(), queryTree.getName());
        copyPropertiesToResult(queryTree, resultTree);
        boolean addKeysToEmptyResource = true;
        if ((!isCollection) && isRequestWithNoProperties(queryTree)) {
            addSubResources(queryTree, resultTree);
            addKeysToEmptyResource = false;
        }
        ensureRequiredProperties(resultTree, addKeysToEmptyResource);
        return resultTree;
    }

    @java.lang.Override
    public org.apache.ambari.server.api.services.ResultPostProcessor getResultPostProcessor(org.apache.ambari.server.api.services.Request request) {
        return new org.apache.ambari.server.api.services.ResultPostProcessorImpl(request);
    }

    @java.lang.Override
    public org.apache.ambari.server.api.services.Result finalizeResult(org.apache.ambari.server.api.services.Result queryResult) {
        return queryResult;
    }
}