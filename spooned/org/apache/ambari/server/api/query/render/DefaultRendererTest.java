package org.apache.ambari.server.api.query.render;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
public class DefaultRendererTest {
    @org.junit.Test
    public void testFinalizeProperties__instance_noProperties() {
        org.apache.ambari.server.controller.spi.SchemaFactory schemaFactory = EasyMock.createNiceMock(org.apache.ambari.server.controller.spi.SchemaFactory.class);
        org.apache.ambari.server.controller.spi.Schema schema = EasyMock.createNiceMock(org.apache.ambari.server.controller.spi.Schema.class);
        EasyMock.expect(schemaFactory.getSchema(org.apache.ambari.server.controller.spi.Resource.Type.Component)).andReturn(schema).anyTimes();
        EasyMock.expect(schemaFactory.getSchema(org.apache.ambari.server.controller.spi.Resource.Type.Alert)).andReturn(schema).anyTimes();
        EasyMock.expect(schemaFactory.getSchema(org.apache.ambari.server.controller.spi.Resource.Type.Artifact)).andReturn(schema).anyTimes();
        EasyMock.expect(schema.getKeyPropertyId(org.apache.ambari.server.controller.spi.Resource.Type.Component)).andReturn("ServiceComponentInfo/component_name").anyTimes();
        EasyMock.expect(schema.getKeyPropertyId(org.apache.ambari.server.controller.spi.Resource.Type.Service)).andReturn("ServiceComponentInfo/service_name").anyTimes();
        EasyMock.replay(schemaFactory, schema);
        org.apache.ambari.server.api.query.QueryInfo rootQuery = new org.apache.ambari.server.api.query.QueryInfo(new org.apache.ambari.server.api.resources.ServiceResourceDefinition(), new java.util.HashSet<>());
        org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.api.query.QueryInfo> queryTree = new org.apache.ambari.server.api.util.TreeNodeImpl<>(null, rootQuery, "Service");
        org.apache.ambari.server.api.query.render.DefaultRenderer renderer = new org.apache.ambari.server.api.query.render.DefaultRenderer();
        renderer.init(schemaFactory);
        org.apache.ambari.server.api.util.TreeNode<java.util.Set<java.lang.String>> propertyTree = renderer.finalizeProperties(queryTree, false);
        org.junit.Assert.assertTrue(propertyTree.getObject().isEmpty());
        org.junit.Assert.assertEquals(3, propertyTree.getChildren().size());
        org.apache.ambari.server.api.util.TreeNode<java.util.Set<java.lang.String>> componentNode = propertyTree.getChild("Component");
        org.junit.Assert.assertEquals(2, componentNode.getObject().size());
        org.junit.Assert.assertTrue(componentNode.getObject().contains("ServiceComponentInfo/component_name"));
        org.junit.Assert.assertTrue(componentNode.getObject().contains("ServiceComponentInfo/service_name"));
        EasyMock.verify(schemaFactory, schema);
    }

    @org.junit.Test
    public void testFinalizeProperties__instance_properties() {
        org.apache.ambari.server.controller.spi.SchemaFactory schemaFactory = EasyMock.createNiceMock(org.apache.ambari.server.controller.spi.SchemaFactory.class);
        org.apache.ambari.server.controller.spi.Schema schema = EasyMock.createNiceMock(org.apache.ambari.server.controller.spi.Schema.class);
        EasyMock.expect(schemaFactory.getSchema(org.apache.ambari.server.controller.spi.Resource.Type.Service)).andReturn(schema).anyTimes();
        EasyMock.expect(schema.getKeyPropertyId(org.apache.ambari.server.controller.spi.Resource.Type.Service)).andReturn("ServiceInfo/service_name").anyTimes();
        EasyMock.expect(schema.getKeyPropertyId(org.apache.ambari.server.controller.spi.Resource.Type.Cluster)).andReturn("ServiceInfo/cluster_name").anyTimes();
        EasyMock.replay(schemaFactory, schema);
        java.util.HashSet<java.lang.String> serviceProperties = new java.util.HashSet<>();
        serviceProperties.add("foo/bar");
        org.apache.ambari.server.api.query.QueryInfo rootQuery = new org.apache.ambari.server.api.query.QueryInfo(new org.apache.ambari.server.api.resources.ServiceResourceDefinition(), serviceProperties);
        org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.api.query.QueryInfo> queryTree = new org.apache.ambari.server.api.util.TreeNodeImpl<>(null, rootQuery, "Service");
        org.apache.ambari.server.api.query.render.DefaultRenderer renderer = new org.apache.ambari.server.api.query.render.DefaultRenderer();
        renderer.init(schemaFactory);
        org.apache.ambari.server.api.util.TreeNode<java.util.Set<java.lang.String>> propertyTree = renderer.finalizeProperties(queryTree, false);
        org.junit.Assert.assertEquals(3, propertyTree.getObject().size());
        org.junit.Assert.assertTrue(propertyTree.getObject().contains("ServiceInfo/service_name"));
        org.junit.Assert.assertTrue(propertyTree.getObject().contains("ServiceInfo/cluster_name"));
        org.junit.Assert.assertTrue(propertyTree.getObject().contains("foo/bar"));
        org.junit.Assert.assertEquals(0, propertyTree.getChildren().size());
        EasyMock.verify(schemaFactory, schema);
    }

    @org.junit.Test
    public void testFinalizeProperties__collection_noProperties() {
        org.apache.ambari.server.controller.spi.SchemaFactory schemaFactory = EasyMock.createNiceMock(org.apache.ambari.server.controller.spi.SchemaFactory.class);
        org.apache.ambari.server.controller.spi.Schema schema = EasyMock.createNiceMock(org.apache.ambari.server.controller.spi.Schema.class);
        EasyMock.expect(schemaFactory.getSchema(org.apache.ambari.server.controller.spi.Resource.Type.Service)).andReturn(schema).anyTimes();
        EasyMock.expect(schema.getKeyPropertyId(org.apache.ambari.server.controller.spi.Resource.Type.Service)).andReturn("ServiceInfo/service_name").anyTimes();
        EasyMock.expect(schema.getKeyPropertyId(org.apache.ambari.server.controller.spi.Resource.Type.Cluster)).andReturn("ServiceInfo/cluster_name").anyTimes();
        EasyMock.replay(schemaFactory, schema);
        java.util.HashSet<java.lang.String> serviceProperties = new java.util.HashSet<>();
        org.apache.ambari.server.api.query.QueryInfo rootQuery = new org.apache.ambari.server.api.query.QueryInfo(new org.apache.ambari.server.api.resources.ServiceResourceDefinition(), serviceProperties);
        org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.api.query.QueryInfo> queryTree = new org.apache.ambari.server.api.util.TreeNodeImpl<>(null, rootQuery, "Service");
        org.apache.ambari.server.api.query.render.DefaultRenderer renderer = new org.apache.ambari.server.api.query.render.DefaultRenderer();
        renderer.init(schemaFactory);
        org.apache.ambari.server.api.util.TreeNode<java.util.Set<java.lang.String>> propertyTree = renderer.finalizeProperties(queryTree, true);
        org.junit.Assert.assertEquals(2, propertyTree.getObject().size());
        org.junit.Assert.assertTrue(propertyTree.getObject().contains("ServiceInfo/service_name"));
        org.junit.Assert.assertTrue(propertyTree.getObject().contains("ServiceInfo/cluster_name"));
        org.junit.Assert.assertEquals(0, propertyTree.getChildren().size());
        EasyMock.verify(schemaFactory, schema);
    }

    @org.junit.Test
    public void testFinalizeProperties__collection_properties() {
        org.apache.ambari.server.controller.spi.SchemaFactory schemaFactory = EasyMock.createNiceMock(org.apache.ambari.server.controller.spi.SchemaFactory.class);
        org.apache.ambari.server.controller.spi.Schema schema = EasyMock.createNiceMock(org.apache.ambari.server.controller.spi.Schema.class);
        EasyMock.expect(schemaFactory.getSchema(org.apache.ambari.server.controller.spi.Resource.Type.Service)).andReturn(schema).anyTimes();
        EasyMock.expect(schema.getKeyPropertyId(org.apache.ambari.server.controller.spi.Resource.Type.Service)).andReturn("ServiceInfo/service_name").anyTimes();
        EasyMock.expect(schema.getKeyPropertyId(org.apache.ambari.server.controller.spi.Resource.Type.Cluster)).andReturn("ServiceInfo/cluster_name").anyTimes();
        EasyMock.replay(schemaFactory, schema);
        java.util.HashSet<java.lang.String> serviceProperties = new java.util.HashSet<>();
        serviceProperties.add("foo/bar");
        org.apache.ambari.server.api.query.QueryInfo rootQuery = new org.apache.ambari.server.api.query.QueryInfo(new org.apache.ambari.server.api.resources.ServiceResourceDefinition(), serviceProperties);
        org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.api.query.QueryInfo> queryTree = new org.apache.ambari.server.api.util.TreeNodeImpl<>(null, rootQuery, "Service");
        org.apache.ambari.server.api.query.render.DefaultRenderer renderer = new org.apache.ambari.server.api.query.render.DefaultRenderer();
        renderer.init(schemaFactory);
        org.apache.ambari.server.api.util.TreeNode<java.util.Set<java.lang.String>> propertyTree = renderer.finalizeProperties(queryTree, true);
        org.junit.Assert.assertEquals(3, propertyTree.getObject().size());
        org.junit.Assert.assertTrue(propertyTree.getObject().contains("ServiceInfo/service_name"));
        org.junit.Assert.assertTrue(propertyTree.getObject().contains("ServiceInfo/cluster_name"));
        org.junit.Assert.assertTrue(propertyTree.getObject().contains("foo/bar"));
        org.junit.Assert.assertEquals(0, propertyTree.getChildren().size());
        EasyMock.verify(schemaFactory, schema);
    }

    @org.junit.Test
    public void testFinalizeProperties__instance_subResource_noProperties() {
        org.apache.ambari.server.controller.spi.SchemaFactory schemaFactory = EasyMock.createNiceMock(org.apache.ambari.server.controller.spi.SchemaFactory.class);
        org.apache.ambari.server.controller.spi.Schema serviceSchema = EasyMock.createNiceMock(org.apache.ambari.server.controller.spi.Schema.class);
        org.apache.ambari.server.controller.spi.Schema componentSchema = EasyMock.createNiceMock(org.apache.ambari.server.controller.spi.Schema.class);
        EasyMock.expect(schemaFactory.getSchema(org.apache.ambari.server.controller.spi.Resource.Type.Service)).andReturn(serviceSchema).anyTimes();
        EasyMock.expect(serviceSchema.getKeyPropertyId(org.apache.ambari.server.controller.spi.Resource.Type.Service)).andReturn("ServiceInfo/service_name").anyTimes();
        EasyMock.expect(serviceSchema.getKeyPropertyId(org.apache.ambari.server.controller.spi.Resource.Type.Cluster)).andReturn("ServiceInfo/cluster_name").anyTimes();
        EasyMock.expect(schemaFactory.getSchema(org.apache.ambari.server.controller.spi.Resource.Type.Component)).andReturn(componentSchema).anyTimes();
        EasyMock.expect(componentSchema.getKeyPropertyId(org.apache.ambari.server.controller.spi.Resource.Type.Service)).andReturn("ServiceComponentInfo/service_name").anyTimes();
        EasyMock.expect(componentSchema.getKeyPropertyId(org.apache.ambari.server.controller.spi.Resource.Type.Component)).andReturn("ServiceComponentInfo/component_name").anyTimes();
        EasyMock.replay(schemaFactory, serviceSchema, componentSchema);
        java.util.HashSet<java.lang.String> serviceProperties = new java.util.HashSet<>();
        org.apache.ambari.server.api.query.QueryInfo rootQuery = new org.apache.ambari.server.api.query.QueryInfo(new org.apache.ambari.server.api.resources.ServiceResourceDefinition(), serviceProperties);
        org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.api.query.QueryInfo> queryTree = new org.apache.ambari.server.api.util.TreeNodeImpl<>(null, rootQuery, "Service");
        queryTree.addChild(new org.apache.ambari.server.api.query.QueryInfo(new org.apache.ambari.server.api.resources.ComponentResourceDefinition(), new java.util.HashSet<>()), "Component");
        org.apache.ambari.server.api.query.render.DefaultRenderer renderer = new org.apache.ambari.server.api.query.render.DefaultRenderer();
        renderer.init(schemaFactory);
        org.apache.ambari.server.api.util.TreeNode<java.util.Set<java.lang.String>> propertyTree = renderer.finalizeProperties(queryTree, false);
        org.junit.Assert.assertEquals(1, propertyTree.getChildren().size());
        org.junit.Assert.assertEquals(2, propertyTree.getObject().size());
        org.junit.Assert.assertTrue(propertyTree.getObject().contains("ServiceInfo/service_name"));
        org.junit.Assert.assertTrue(propertyTree.getObject().contains("ServiceInfo/cluster_name"));
        org.apache.ambari.server.api.util.TreeNode<java.util.Set<java.lang.String>> componentNode = propertyTree.getChild("Component");
        org.junit.Assert.assertEquals(0, componentNode.getChildren().size());
        org.junit.Assert.assertEquals(2, componentNode.getObject().size());
        org.junit.Assert.assertTrue(componentNode.getObject().contains("ServiceComponentInfo/service_name"));
        org.junit.Assert.assertTrue(componentNode.getObject().contains("ServiceComponentInfo/component_name"));
        EasyMock.verify(schemaFactory, serviceSchema, componentSchema);
    }

    @org.junit.Test
    public void testFinalizeProperties__instance_subResource_properties() {
        org.apache.ambari.server.controller.spi.SchemaFactory schemaFactory = EasyMock.createNiceMock(org.apache.ambari.server.controller.spi.SchemaFactory.class);
        org.apache.ambari.server.controller.spi.Schema serviceSchema = EasyMock.createNiceMock(org.apache.ambari.server.controller.spi.Schema.class);
        org.apache.ambari.server.controller.spi.Schema componentSchema = EasyMock.createNiceMock(org.apache.ambari.server.controller.spi.Schema.class);
        EasyMock.expect(schemaFactory.getSchema(org.apache.ambari.server.controller.spi.Resource.Type.Service)).andReturn(serviceSchema).anyTimes();
        EasyMock.expect(serviceSchema.getKeyPropertyId(org.apache.ambari.server.controller.spi.Resource.Type.Service)).andReturn("ServiceInfo/service_name").anyTimes();
        EasyMock.expect(serviceSchema.getKeyPropertyId(org.apache.ambari.server.controller.spi.Resource.Type.Cluster)).andReturn("ServiceInfo/cluster_name").anyTimes();
        EasyMock.expect(schemaFactory.getSchema(org.apache.ambari.server.controller.spi.Resource.Type.Component)).andReturn(componentSchema).anyTimes();
        EasyMock.expect(componentSchema.getKeyPropertyId(org.apache.ambari.server.controller.spi.Resource.Type.Service)).andReturn("ServiceComponentInfo/service_name").anyTimes();
        EasyMock.expect(componentSchema.getKeyPropertyId(org.apache.ambari.server.controller.spi.Resource.Type.Component)).andReturn("ServiceComponentInfo/component_name").anyTimes();
        EasyMock.replay(schemaFactory, serviceSchema, componentSchema);
        java.util.HashSet<java.lang.String> serviceProperties = new java.util.HashSet<>();
        serviceProperties.add("foo/bar");
        org.apache.ambari.server.api.query.QueryInfo rootQuery = new org.apache.ambari.server.api.query.QueryInfo(new org.apache.ambari.server.api.resources.ServiceResourceDefinition(), serviceProperties);
        org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.api.query.QueryInfo> queryTree = new org.apache.ambari.server.api.util.TreeNodeImpl<>(null, rootQuery, "Service");
        java.util.HashSet<java.lang.String> componentProperties = new java.util.HashSet<>();
        componentProperties.add("goo/car");
        queryTree.addChild(new org.apache.ambari.server.api.query.QueryInfo(new org.apache.ambari.server.api.resources.ComponentResourceDefinition(), componentProperties), "Component");
        org.apache.ambari.server.api.query.render.DefaultRenderer renderer = new org.apache.ambari.server.api.query.render.DefaultRenderer();
        renderer.init(schemaFactory);
        org.apache.ambari.server.api.util.TreeNode<java.util.Set<java.lang.String>> propertyTree = renderer.finalizeProperties(queryTree, false);
        org.junit.Assert.assertEquals(1, propertyTree.getChildren().size());
        org.junit.Assert.assertEquals(3, propertyTree.getObject().size());
        org.junit.Assert.assertTrue(propertyTree.getObject().contains("ServiceInfo/service_name"));
        org.junit.Assert.assertTrue(propertyTree.getObject().contains("ServiceInfo/cluster_name"));
        org.junit.Assert.assertTrue(propertyTree.getObject().contains("foo/bar"));
        org.apache.ambari.server.api.util.TreeNode<java.util.Set<java.lang.String>> componentNode = propertyTree.getChild("Component");
        org.junit.Assert.assertEquals(0, componentNode.getChildren().size());
        org.junit.Assert.assertEquals(3, componentNode.getObject().size());
        org.junit.Assert.assertTrue(componentNode.getObject().contains("ServiceComponentInfo/service_name"));
        org.junit.Assert.assertTrue(componentNode.getObject().contains("ServiceComponentInfo/component_name"));
        org.junit.Assert.assertTrue(componentNode.getObject().contains("goo/car"));
        EasyMock.verify(schemaFactory, serviceSchema, componentSchema);
    }

    @org.junit.Test
    public void testFinalizeProperties__collection_subResource_noProperties() {
        org.apache.ambari.server.controller.spi.SchemaFactory schemaFactory = EasyMock.createNiceMock(org.apache.ambari.server.controller.spi.SchemaFactory.class);
        org.apache.ambari.server.controller.spi.Schema serviceSchema = EasyMock.createNiceMock(org.apache.ambari.server.controller.spi.Schema.class);
        org.apache.ambari.server.controller.spi.Schema componentSchema = EasyMock.createNiceMock(org.apache.ambari.server.controller.spi.Schema.class);
        EasyMock.expect(schemaFactory.getSchema(org.apache.ambari.server.controller.spi.Resource.Type.Service)).andReturn(serviceSchema).anyTimes();
        EasyMock.expect(serviceSchema.getKeyPropertyId(org.apache.ambari.server.controller.spi.Resource.Type.Service)).andReturn("ServiceInfo/service_name").anyTimes();
        EasyMock.expect(serviceSchema.getKeyPropertyId(org.apache.ambari.server.controller.spi.Resource.Type.Cluster)).andReturn("ServiceInfo/cluster_name").anyTimes();
        EasyMock.expect(schemaFactory.getSchema(org.apache.ambari.server.controller.spi.Resource.Type.Component)).andReturn(componentSchema).anyTimes();
        EasyMock.expect(componentSchema.getKeyPropertyId(org.apache.ambari.server.controller.spi.Resource.Type.Service)).andReturn("ServiceComponentInfo/service_name").anyTimes();
        EasyMock.expect(componentSchema.getKeyPropertyId(org.apache.ambari.server.controller.spi.Resource.Type.Component)).andReturn("ServiceComponentInfo/component_name").anyTimes();
        EasyMock.replay(schemaFactory, serviceSchema, componentSchema);
        java.util.HashSet<java.lang.String> serviceProperties = new java.util.HashSet<>();
        org.apache.ambari.server.api.query.QueryInfo rootQuery = new org.apache.ambari.server.api.query.QueryInfo(new org.apache.ambari.server.api.resources.ServiceResourceDefinition(), serviceProperties);
        org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.api.query.QueryInfo> queryTree = new org.apache.ambari.server.api.util.TreeNodeImpl<>(null, rootQuery, "Service");
        queryTree.addChild(new org.apache.ambari.server.api.query.QueryInfo(new org.apache.ambari.server.api.resources.ComponentResourceDefinition(), new java.util.HashSet<>()), "Component");
        org.apache.ambari.server.api.query.render.DefaultRenderer renderer = new org.apache.ambari.server.api.query.render.DefaultRenderer();
        renderer.init(schemaFactory);
        org.apache.ambari.server.api.util.TreeNode<java.util.Set<java.lang.String>> propertyTree = renderer.finalizeProperties(queryTree, true);
        org.junit.Assert.assertEquals(1, propertyTree.getChildren().size());
        org.junit.Assert.assertEquals(2, propertyTree.getObject().size());
        org.junit.Assert.assertTrue(propertyTree.getObject().contains("ServiceInfo/service_name"));
        org.junit.Assert.assertTrue(propertyTree.getObject().contains("ServiceInfo/cluster_name"));
        org.apache.ambari.server.api.util.TreeNode<java.util.Set<java.lang.String>> componentNode = propertyTree.getChild("Component");
        org.junit.Assert.assertEquals(0, componentNode.getChildren().size());
        org.junit.Assert.assertEquals(2, componentNode.getObject().size());
        org.junit.Assert.assertTrue(componentNode.getObject().contains("ServiceComponentInfo/service_name"));
        org.junit.Assert.assertTrue(componentNode.getObject().contains("ServiceComponentInfo/component_name"));
        EasyMock.verify(schemaFactory, serviceSchema, componentSchema);
    }

    @org.junit.Test
    public void testFinalizeProperties__collection_subResource_propertiesTopLevelOnly() {
        org.apache.ambari.server.controller.spi.SchemaFactory schemaFactory = EasyMock.createNiceMock(org.apache.ambari.server.controller.spi.SchemaFactory.class);
        org.apache.ambari.server.controller.spi.Schema serviceSchema = EasyMock.createNiceMock(org.apache.ambari.server.controller.spi.Schema.class);
        org.apache.ambari.server.controller.spi.Schema componentSchema = EasyMock.createNiceMock(org.apache.ambari.server.controller.spi.Schema.class);
        EasyMock.expect(schemaFactory.getSchema(org.apache.ambari.server.controller.spi.Resource.Type.Service)).andReturn(serviceSchema).anyTimes();
        EasyMock.expect(serviceSchema.getKeyPropertyId(org.apache.ambari.server.controller.spi.Resource.Type.Service)).andReturn("ServiceInfo/service_name").anyTimes();
        EasyMock.expect(serviceSchema.getKeyPropertyId(org.apache.ambari.server.controller.spi.Resource.Type.Cluster)).andReturn("ServiceInfo/cluster_name").anyTimes();
        EasyMock.expect(schemaFactory.getSchema(org.apache.ambari.server.controller.spi.Resource.Type.Component)).andReturn(componentSchema).anyTimes();
        EasyMock.expect(componentSchema.getKeyPropertyId(org.apache.ambari.server.controller.spi.Resource.Type.Service)).andReturn("ServiceComponentInfo/service_name").anyTimes();
        EasyMock.expect(componentSchema.getKeyPropertyId(org.apache.ambari.server.controller.spi.Resource.Type.Component)).andReturn("ServiceComponentInfo/component_name").anyTimes();
        EasyMock.replay(schemaFactory, serviceSchema, componentSchema);
        java.util.HashSet<java.lang.String> serviceProperties = new java.util.HashSet<>();
        serviceProperties.add("foo/bar");
        org.apache.ambari.server.api.query.QueryInfo rootQuery = new org.apache.ambari.server.api.query.QueryInfo(new org.apache.ambari.server.api.resources.ServiceResourceDefinition(), serviceProperties);
        org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.api.query.QueryInfo> queryTree = new org.apache.ambari.server.api.util.TreeNodeImpl<>(null, rootQuery, "Service");
        queryTree.addChild(new org.apache.ambari.server.api.query.QueryInfo(new org.apache.ambari.server.api.resources.ComponentResourceDefinition(), new java.util.HashSet<>()), "Component");
        org.apache.ambari.server.api.query.render.DefaultRenderer renderer = new org.apache.ambari.server.api.query.render.DefaultRenderer();
        renderer.init(schemaFactory);
        org.apache.ambari.server.api.util.TreeNode<java.util.Set<java.lang.String>> propertyTree = renderer.finalizeProperties(queryTree, true);
        org.junit.Assert.assertEquals(1, propertyTree.getChildren().size());
        org.junit.Assert.assertEquals(3, propertyTree.getObject().size());
        org.junit.Assert.assertTrue(propertyTree.getObject().contains("ServiceInfo/service_name"));
        org.junit.Assert.assertTrue(propertyTree.getObject().contains("ServiceInfo/cluster_name"));
        org.junit.Assert.assertTrue(propertyTree.getObject().contains("foo/bar"));
        org.apache.ambari.server.api.util.TreeNode<java.util.Set<java.lang.String>> componentNode = propertyTree.getChild("Component");
        org.junit.Assert.assertEquals(0, componentNode.getChildren().size());
        org.junit.Assert.assertEquals(2, componentNode.getObject().size());
        org.junit.Assert.assertTrue(componentNode.getObject().contains("ServiceComponentInfo/service_name"));
        org.junit.Assert.assertTrue(componentNode.getObject().contains("ServiceComponentInfo/component_name"));
        EasyMock.verify(schemaFactory, serviceSchema, componentSchema);
    }

    @org.junit.Test
    public void testFinalizeResult() {
        org.apache.ambari.server.api.services.Result result = EasyMock.createNiceMock(org.apache.ambari.server.api.services.Result.class);
        org.apache.ambari.server.api.query.render.DefaultRenderer renderer = new org.apache.ambari.server.api.query.render.DefaultRenderer();
        org.junit.Assert.assertSame(result, renderer.finalizeResult(result));
    }

    @org.junit.Test
    public void testRequiresInputDefault() throws java.lang.Exception {
        org.apache.ambari.server.api.query.render.Renderer defaultRenderer = new org.apache.ambari.server.api.query.render.DefaultRenderer();
        org.junit.Assert.assertTrue("Default renderer for cluster resources must require property provider input", defaultRenderer.requiresPropertyProviderInput());
    }
}