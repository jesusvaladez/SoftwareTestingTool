package org.apache.ambari.server.api.query.render;
public abstract class BaseRenderer implements org.apache.ambari.server.api.query.render.Renderer {
    private org.apache.ambari.server.controller.spi.SchemaFactory m_schemaFactory;

    @java.lang.Override
    public void init(org.apache.ambari.server.controller.spi.SchemaFactory schemaFactory) {
        m_schemaFactory = schemaFactory;
    }

    @java.lang.Override
    public boolean requiresPropertyProviderInput() {
        return true;
    }

    protected org.apache.ambari.server.controller.spi.Schema getSchema(org.apache.ambari.server.controller.spi.Resource.Type type) {
        return m_schemaFactory.getSchema(type);
    }

    protected void copyPropertiesToResult(org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.api.query.QueryInfo> queryTree, org.apache.ambari.server.api.util.TreeNode<java.util.Set<java.lang.String>> propertyTree) {
        for (org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.api.query.QueryInfo> node : queryTree.getChildren()) {
            org.apache.ambari.server.api.util.TreeNode<java.util.Set<java.lang.String>> child = propertyTree.addChild(node.getObject().getProperties(), node.getName());
            copyPropertiesToResult(node, child);
        }
    }

    protected void addPrimaryKey(org.apache.ambari.server.controller.spi.Resource.Type resourceType, java.util.Set<java.lang.String> properties) {
        properties.add(getSchema(resourceType).getKeyPropertyId(resourceType));
    }

    protected void addKeys(org.apache.ambari.server.controller.spi.Resource.Type resourceType, java.util.Set<java.lang.String> properties) {
        org.apache.ambari.server.controller.spi.Schema schema = getSchema(resourceType);
        for (org.apache.ambari.server.controller.spi.Resource.Type type : org.apache.ambari.server.controller.spi.Resource.Type.values()) {
            java.lang.String propertyId = schema.getKeyPropertyId(type);
            if (propertyId != null) {
                properties.add(propertyId);
            }
        }
    }

    protected boolean isRequestWithNoProperties(org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.api.query.QueryInfo> queryNode) {
        return queryNode.getChildren().isEmpty() && (queryNode.getObject().getProperties().size() == 0);
    }

    protected void addSubResources(org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.api.query.QueryInfo> queryTree, org.apache.ambari.server.api.util.TreeNode<java.util.Set<java.lang.String>> propertyTree) {
        org.apache.ambari.server.api.query.QueryInfo queryInfo = queryTree.getObject();
        org.apache.ambari.server.api.resources.ResourceDefinition resource = queryInfo.getResource();
        java.util.Set<org.apache.ambari.server.api.resources.SubResourceDefinition> subResources = resource.getSubResourceDefinitions();
        for (org.apache.ambari.server.api.resources.SubResourceDefinition subResource : subResources) {
            java.util.Set<java.lang.String> resourceProperties = new java.util.HashSet<>();
            populateSubResourceDefaults(subResource, resourceProperties);
            propertyTree.addChild(resourceProperties, subResource.getType().name());
        }
    }

    protected void populateSubResourceDefaults(org.apache.ambari.server.api.resources.SubResourceDefinition subResource, java.util.Set<java.lang.String> properties) {
        org.apache.ambari.server.controller.spi.Schema schema = getSchema(subResource.getType());
        java.util.Set<org.apache.ambari.server.controller.spi.Resource.Type> foreignKeys = subResource.getAdditionalForeignKeys();
        for (org.apache.ambari.server.controller.spi.Resource.Type fk : foreignKeys) {
            properties.add(schema.getKeyPropertyId(fk));
        }
        addPrimaryKey(subResource.getType(), properties);
        addKeys(subResource.getType(), properties);
    }

    protected void ensureRequiredProperties(org.apache.ambari.server.api.util.TreeNode<java.util.Set<java.lang.String>> propertyTree, boolean addIfEmpty) {
        org.apache.ambari.server.controller.spi.Resource.Type type = org.apache.ambari.server.controller.spi.Resource.Type.valueOf(propertyTree.getName());
        java.util.Set<java.lang.String> properties = propertyTree.getObject();
        if ((!properties.isEmpty()) || addIfEmpty) {
            addKeys(type, properties);
        }
        for (org.apache.ambari.server.api.util.TreeNode<java.util.Set<java.lang.String>> child : propertyTree.getChildren()) {
            ensureRequiredProperties(child, addIfEmpty);
        }
    }
}