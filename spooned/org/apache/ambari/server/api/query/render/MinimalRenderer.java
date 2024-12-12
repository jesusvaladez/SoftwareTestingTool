package org.apache.ambari.server.api.query.render;
public class MinimalRenderer extends org.apache.ambari.server.api.query.render.BaseRenderer implements org.apache.ambari.server.api.query.render.Renderer {
    private org.apache.ambari.server.controller.spi.Resource.Type m_rootType;

    private boolean m_isCollection;

    private java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.util.Set<java.lang.String>> m_originalProperties = new java.util.HashMap<>();

    @java.lang.Override
    public org.apache.ambari.server.api.util.TreeNode<java.util.Set<java.lang.String>> finalizeProperties(org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.api.query.QueryInfo> queryTree, boolean isCollection) {
        org.apache.ambari.server.api.query.QueryInfo queryInfo = queryTree.getObject();
        org.apache.ambari.server.api.util.TreeNode<java.util.Set<java.lang.String>> resultTree = new org.apache.ambari.server.api.util.TreeNodeImpl<>(null, queryInfo.getProperties(), queryTree.getName());
        copyPropertiesToResult(queryTree, resultTree);
        m_rootType = queryTree.getObject().getResource().getType();
        m_isCollection = isCollection;
        boolean addKeysToEmptyResource = true;
        if ((!isCollection) && isRequestWithNoProperties(queryTree)) {
            addSubResources(queryTree, resultTree);
            addKeysToEmptyResource = false;
        }
        processRequestedProperties(queryTree);
        ensureRequiredProperties(resultTree, addKeysToEmptyResource);
        return resultTree;
    }

    @java.lang.Override
    public org.apache.ambari.server.api.services.Result finalizeResult(org.apache.ambari.server.api.services.Result queryResult) {
        processResultNode(queryResult.getResultTree());
        return queryResult;
    }

    @java.lang.Override
    public org.apache.ambari.server.api.services.ResultPostProcessor getResultPostProcessor(org.apache.ambari.server.api.services.Request request) {
        return new org.apache.ambari.server.api.query.render.MinimalRenderer.MinimalPostProcessor(request);
    }

    private void processRequestedProperties(org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.api.query.QueryInfo> queryTree) {
        org.apache.ambari.server.api.query.QueryInfo queryInfo = queryTree.getObject();
        if (queryInfo != null) {
            org.apache.ambari.server.controller.spi.Resource.Type type = queryInfo.getResource().getType();
            java.util.Set<java.lang.String> properties = m_originalProperties.get(type);
            if (properties == null) {
                properties = new java.util.HashSet<>();
                m_originalProperties.put(type, properties);
            }
            properties.addAll(queryInfo.getProperties());
            for (org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.api.query.QueryInfo> child : queryTree.getChildren()) {
                processRequestedProperties(child);
            }
        }
    }

    private void processResultNode(org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> node) {
        org.apache.ambari.server.controller.spi.Resource resource = node.getObject();
        if ((resource != null) && ((resource.getType() != m_rootType) || m_isCollection)) {
            org.apache.ambari.server.controller.spi.Resource.Type type = resource.getType();
            java.util.Set<java.lang.String> requestedProperties = m_originalProperties.get(type);
            java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.Object>> properties = resource.getPropertiesMap();
            java.util.Iterator<java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, java.lang.Object>>> iter;
            for (iter = properties.entrySet().iterator(); iter.hasNext();) {
                java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, java.lang.Object>> entry = iter.next();
                java.lang.String categoryName = entry.getKey();
                java.util.Iterator<java.lang.String> valueIter;
                for (valueIter = entry.getValue().keySet().iterator(); valueIter.hasNext();) {
                    java.lang.String propName = valueIter.next();
                    java.lang.String absPropertyName = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId(categoryName, propName);
                    if (((requestedProperties == null) || ((!requestedProperties.contains(absPropertyName)) && (!isSubCategory(requestedProperties, categoryName)))) && (!getPrimaryKeys(type).contains(absPropertyName))) {
                        valueIter.remove();
                    }
                }
                if (entry.getValue().isEmpty()) {
                    iter.remove();
                }
            }
        }
        for (org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> child : node.getChildren()) {
            processResultNode(child);
        }
    }

    private boolean isSubCategory(java.util.Set<java.lang.String> properties, java.lang.String subCategoryName) {
        for (java.lang.String property : properties) {
            if (subCategoryName.startsWith(property)) {
                return true;
            }
        }
        return false;
    }

    private java.util.Set<java.lang.String> getPrimaryKeys(org.apache.ambari.server.controller.spi.Resource.Type type) {
        java.util.Set<java.lang.String> primaryKeys = new java.util.HashSet<>();
        if (type == org.apache.ambari.server.controller.spi.Resource.Type.Configuration) {
            primaryKeys.add("type");
            primaryKeys.add("tag");
        } else {
            java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> keys = org.apache.ambari.server.controller.utilities.PropertyHelper.getKeyPropertyIds(type);
            if (keys != null) {
                java.lang.String pk = org.apache.ambari.server.controller.utilities.PropertyHelper.getKeyPropertyIds(type).get(type);
                if (pk != null) {
                    primaryKeys = java.util.Collections.singleton(pk);
                }
            }
        }
        return primaryKeys;
    }

    private static class MinimalPostProcessor extends org.apache.ambari.server.api.services.ResultPostProcessorImpl {
        private MinimalPostProcessor(org.apache.ambari.server.api.services.Request request) {
            super(request);
        }

        @java.lang.Override
        protected void finalizeNode(org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> node) {
            node.removeProperty("href");
        }
    }
}