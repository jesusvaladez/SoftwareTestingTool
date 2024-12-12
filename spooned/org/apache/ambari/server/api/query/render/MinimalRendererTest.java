package org.apache.ambari.server.api.query.render;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
public class MinimalRendererTest {
    @org.junit.Test
    public void testFinalizeProperties__instance_noProperties() {
        org.apache.ambari.server.controller.spi.SchemaFactory schemaFactory = EasyMock.createNiceMock(org.apache.ambari.server.controller.spi.SchemaFactory.class);
        org.apache.ambari.server.controller.spi.Schema schema = EasyMock.createNiceMock(org.apache.ambari.server.controller.spi.Schema.class);
        EasyMock.expect(schemaFactory.getSchema(org.apache.ambari.server.controller.spi.Resource.Type.Component)).andReturn(schema).anyTimes();
        EasyMock.expect(schemaFactory.getSchema(org.apache.ambari.server.controller.spi.Resource.Type.Alert)).andReturn(schema).anyTimes();
        EasyMock.expect(schemaFactory.getSchema(org.apache.ambari.server.controller.spi.Resource.Type.Artifact)).andReturn(schema).anyTimes();
        EasyMock.expect(schema.getKeyPropertyId(org.apache.ambari.server.controller.spi.Resource.Type.Component)).andReturn("ServiceComponentInfo/component_name").anyTimes();
        EasyMock.replay(schemaFactory, schema);
        org.apache.ambari.server.api.query.QueryInfo rootQuery = new org.apache.ambari.server.api.query.QueryInfo(new org.apache.ambari.server.api.resources.ServiceResourceDefinition(), new java.util.HashSet<>());
        org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.api.query.QueryInfo> queryTree = new org.apache.ambari.server.api.util.TreeNodeImpl<>(null, rootQuery, "Service");
        org.apache.ambari.server.api.query.render.MinimalRenderer renderer = new org.apache.ambari.server.api.query.render.MinimalRenderer();
        renderer.init(schemaFactory);
        org.apache.ambari.server.api.util.TreeNode<java.util.Set<java.lang.String>> propertyTree = renderer.finalizeProperties(queryTree, false);
        org.junit.Assert.assertTrue(propertyTree.getObject().isEmpty());
        org.junit.Assert.assertEquals(3, propertyTree.getChildren().size());
        org.apache.ambari.server.api.util.TreeNode<java.util.Set<java.lang.String>> componentNode = propertyTree.getChild("Component");
        org.junit.Assert.assertEquals(1, componentNode.getObject().size());
        org.junit.Assert.assertTrue(componentNode.getObject().contains("ServiceComponentInfo/component_name"));
        EasyMock.verify(schemaFactory, schema);
    }

    @org.junit.Test
    public void testFinalizeProperties__instance_properties() {
        org.apache.ambari.server.controller.spi.SchemaFactory schemaFactory = EasyMock.createNiceMock(org.apache.ambari.server.controller.spi.SchemaFactory.class);
        org.apache.ambari.server.controller.spi.Schema schema = EasyMock.createNiceMock(org.apache.ambari.server.controller.spi.Schema.class);
        EasyMock.expect(schemaFactory.getSchema(org.apache.ambari.server.controller.spi.Resource.Type.Service)).andReturn(schema).anyTimes();
        EasyMock.expect(schema.getKeyPropertyId(org.apache.ambari.server.controller.spi.Resource.Type.Service)).andReturn("ServiceInfo/service_name").anyTimes();
        EasyMock.replay(schemaFactory, schema);
        java.util.HashSet<java.lang.String> serviceProperties = new java.util.HashSet<>();
        serviceProperties.add("foo/bar");
        org.apache.ambari.server.api.query.QueryInfo rootQuery = new org.apache.ambari.server.api.query.QueryInfo(new org.apache.ambari.server.api.resources.ServiceResourceDefinition(), serviceProperties);
        org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.api.query.QueryInfo> queryTree = new org.apache.ambari.server.api.util.TreeNodeImpl<>(null, rootQuery, "Service");
        org.apache.ambari.server.api.query.render.MinimalRenderer renderer = new org.apache.ambari.server.api.query.render.MinimalRenderer();
        renderer.init(schemaFactory);
        org.apache.ambari.server.api.util.TreeNode<java.util.Set<java.lang.String>> propertyTree = renderer.finalizeProperties(queryTree, false);
        org.junit.Assert.assertEquals(2, propertyTree.getObject().size());
        org.junit.Assert.assertTrue(propertyTree.getObject().contains("ServiceInfo/service_name"));
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
        EasyMock.replay(schemaFactory, schema);
        java.util.HashSet<java.lang.String> serviceProperties = new java.util.HashSet<>();
        org.apache.ambari.server.api.query.QueryInfo rootQuery = new org.apache.ambari.server.api.query.QueryInfo(new org.apache.ambari.server.api.resources.ServiceResourceDefinition(), serviceProperties);
        org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.api.query.QueryInfo> queryTree = new org.apache.ambari.server.api.util.TreeNodeImpl<>(null, rootQuery, "Service");
        org.apache.ambari.server.api.query.render.MinimalRenderer renderer = new org.apache.ambari.server.api.query.render.MinimalRenderer();
        renderer.init(schemaFactory);
        org.apache.ambari.server.api.util.TreeNode<java.util.Set<java.lang.String>> propertyTree = renderer.finalizeProperties(queryTree, true);
        org.junit.Assert.assertEquals(1, propertyTree.getObject().size());
        org.junit.Assert.assertTrue(propertyTree.getObject().contains("ServiceInfo/service_name"));
        org.junit.Assert.assertEquals(0, propertyTree.getChildren().size());
        EasyMock.verify(schemaFactory, schema);
    }

    @org.junit.Test
    public void testFinalizeProperties__collection_properties() {
        org.apache.ambari.server.controller.spi.SchemaFactory schemaFactory = EasyMock.createNiceMock(org.apache.ambari.server.controller.spi.SchemaFactory.class);
        org.apache.ambari.server.controller.spi.Schema schema = EasyMock.createNiceMock(org.apache.ambari.server.controller.spi.Schema.class);
        EasyMock.expect(schemaFactory.getSchema(org.apache.ambari.server.controller.spi.Resource.Type.Service)).andReturn(schema).anyTimes();
        EasyMock.expect(schema.getKeyPropertyId(org.apache.ambari.server.controller.spi.Resource.Type.Service)).andReturn("ServiceInfo/service_name").anyTimes();
        EasyMock.replay(schemaFactory, schema);
        java.util.HashSet<java.lang.String> serviceProperties = new java.util.HashSet<>();
        serviceProperties.add("foo/bar");
        org.apache.ambari.server.api.query.QueryInfo rootQuery = new org.apache.ambari.server.api.query.QueryInfo(new org.apache.ambari.server.api.resources.ServiceResourceDefinition(), serviceProperties);
        org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.api.query.QueryInfo> queryTree = new org.apache.ambari.server.api.util.TreeNodeImpl<>(null, rootQuery, "Service");
        org.apache.ambari.server.api.query.render.MinimalRenderer renderer = new org.apache.ambari.server.api.query.render.MinimalRenderer();
        renderer.init(schemaFactory);
        org.apache.ambari.server.api.util.TreeNode<java.util.Set<java.lang.String>> propertyTree = renderer.finalizeProperties(queryTree, true);
        org.junit.Assert.assertEquals(2, propertyTree.getObject().size());
        org.junit.Assert.assertTrue(propertyTree.getObject().contains("ServiceInfo/service_name"));
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
        EasyMock.expect(schemaFactory.getSchema(org.apache.ambari.server.controller.spi.Resource.Type.Component)).andReturn(componentSchema).anyTimes();
        EasyMock.expect(componentSchema.getKeyPropertyId(org.apache.ambari.server.controller.spi.Resource.Type.Component)).andReturn("ServiceComponentInfo/component_name").anyTimes();
        EasyMock.replay(schemaFactory, serviceSchema, componentSchema);
        java.util.HashSet<java.lang.String> serviceProperties = new java.util.HashSet<>();
        org.apache.ambari.server.api.query.QueryInfo rootQuery = new org.apache.ambari.server.api.query.QueryInfo(new org.apache.ambari.server.api.resources.ServiceResourceDefinition(), serviceProperties);
        org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.api.query.QueryInfo> queryTree = new org.apache.ambari.server.api.util.TreeNodeImpl<>(null, rootQuery, "Service");
        queryTree.addChild(new org.apache.ambari.server.api.query.QueryInfo(new org.apache.ambari.server.api.resources.ComponentResourceDefinition(), new java.util.HashSet<>()), "Component");
        org.apache.ambari.server.api.query.render.MinimalRenderer renderer = new org.apache.ambari.server.api.query.render.MinimalRenderer();
        renderer.init(schemaFactory);
        org.apache.ambari.server.api.util.TreeNode<java.util.Set<java.lang.String>> propertyTree = renderer.finalizeProperties(queryTree, false);
        org.junit.Assert.assertEquals(1, propertyTree.getChildren().size());
        org.junit.Assert.assertEquals(1, propertyTree.getObject().size());
        org.junit.Assert.assertTrue(propertyTree.getObject().contains("ServiceInfo/service_name"));
        org.apache.ambari.server.api.util.TreeNode<java.util.Set<java.lang.String>> componentNode = propertyTree.getChild("Component");
        org.junit.Assert.assertEquals(0, componentNode.getChildren().size());
        org.junit.Assert.assertEquals(1, componentNode.getObject().size());
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
        EasyMock.expect(schemaFactory.getSchema(org.apache.ambari.server.controller.spi.Resource.Type.Component)).andReturn(componentSchema).anyTimes();
        EasyMock.expect(componentSchema.getKeyPropertyId(org.apache.ambari.server.controller.spi.Resource.Type.Component)).andReturn("ServiceComponentInfo/component_name").anyTimes();
        EasyMock.replay(schemaFactory, serviceSchema, componentSchema);
        java.util.HashSet<java.lang.String> serviceProperties = new java.util.HashSet<>();
        serviceProperties.add("foo/bar");
        org.apache.ambari.server.api.query.QueryInfo rootQuery = new org.apache.ambari.server.api.query.QueryInfo(new org.apache.ambari.server.api.resources.ServiceResourceDefinition(), serviceProperties);
        org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.api.query.QueryInfo> queryTree = new org.apache.ambari.server.api.util.TreeNodeImpl<>(null, rootQuery, "Service");
        java.util.HashSet<java.lang.String> componentProperties = new java.util.HashSet<>();
        componentProperties.add("goo/car");
        queryTree.addChild(new org.apache.ambari.server.api.query.QueryInfo(new org.apache.ambari.server.api.resources.ComponentResourceDefinition(), componentProperties), "Component");
        org.apache.ambari.server.api.query.render.MinimalRenderer renderer = new org.apache.ambari.server.api.query.render.MinimalRenderer();
        renderer.init(schemaFactory);
        org.apache.ambari.server.api.util.TreeNode<java.util.Set<java.lang.String>> propertyTree = renderer.finalizeProperties(queryTree, false);
        org.junit.Assert.assertEquals(1, propertyTree.getChildren().size());
        org.junit.Assert.assertEquals(2, propertyTree.getObject().size());
        org.junit.Assert.assertTrue(propertyTree.getObject().contains("ServiceInfo/service_name"));
        org.junit.Assert.assertTrue(propertyTree.getObject().contains("foo/bar"));
        org.apache.ambari.server.api.util.TreeNode<java.util.Set<java.lang.String>> componentNode = propertyTree.getChild("Component");
        org.junit.Assert.assertEquals(0, componentNode.getChildren().size());
        org.junit.Assert.assertEquals(2, componentNode.getObject().size());
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
        EasyMock.expect(schemaFactory.getSchema(org.apache.ambari.server.controller.spi.Resource.Type.Component)).andReturn(componentSchema).anyTimes();
        EasyMock.expect(componentSchema.getKeyPropertyId(org.apache.ambari.server.controller.spi.Resource.Type.Component)).andReturn("ServiceComponentInfo/component_name").anyTimes();
        EasyMock.replay(schemaFactory, serviceSchema, componentSchema);
        java.util.HashSet<java.lang.String> serviceProperties = new java.util.HashSet<>();
        org.apache.ambari.server.api.query.QueryInfo rootQuery = new org.apache.ambari.server.api.query.QueryInfo(new org.apache.ambari.server.api.resources.ServiceResourceDefinition(), serviceProperties);
        org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.api.query.QueryInfo> queryTree = new org.apache.ambari.server.api.util.TreeNodeImpl<>(null, rootQuery, "Service");
        queryTree.addChild(new org.apache.ambari.server.api.query.QueryInfo(new org.apache.ambari.server.api.resources.ComponentResourceDefinition(), new java.util.HashSet<>()), "Component");
        org.apache.ambari.server.api.query.render.MinimalRenderer renderer = new org.apache.ambari.server.api.query.render.MinimalRenderer();
        renderer.init(schemaFactory);
        org.apache.ambari.server.api.util.TreeNode<java.util.Set<java.lang.String>> propertyTree = renderer.finalizeProperties(queryTree, true);
        org.junit.Assert.assertEquals(1, propertyTree.getChildren().size());
        org.junit.Assert.assertEquals(1, propertyTree.getObject().size());
        org.junit.Assert.assertTrue(propertyTree.getObject().contains("ServiceInfo/service_name"));
        org.apache.ambari.server.api.util.TreeNode<java.util.Set<java.lang.String>> componentNode = propertyTree.getChild("Component");
        org.junit.Assert.assertEquals(0, componentNode.getChildren().size());
        org.junit.Assert.assertEquals(1, componentNode.getObject().size());
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
        EasyMock.expect(schemaFactory.getSchema(org.apache.ambari.server.controller.spi.Resource.Type.Component)).andReturn(componentSchema).anyTimes();
        EasyMock.expect(componentSchema.getKeyPropertyId(org.apache.ambari.server.controller.spi.Resource.Type.Component)).andReturn("ServiceComponentInfo/component_name").anyTimes();
        EasyMock.replay(schemaFactory, serviceSchema, componentSchema);
        java.util.HashSet<java.lang.String> serviceProperties = new java.util.HashSet<>();
        serviceProperties.add("foo/bar");
        org.apache.ambari.server.api.query.QueryInfo rootQuery = new org.apache.ambari.server.api.query.QueryInfo(new org.apache.ambari.server.api.resources.ServiceResourceDefinition(), serviceProperties);
        org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.api.query.QueryInfo> queryTree = new org.apache.ambari.server.api.util.TreeNodeImpl<>(null, rootQuery, "Service");
        queryTree.addChild(new org.apache.ambari.server.api.query.QueryInfo(new org.apache.ambari.server.api.resources.ComponentResourceDefinition(), new java.util.HashSet<>()), "Component");
        org.apache.ambari.server.api.query.render.MinimalRenderer renderer = new org.apache.ambari.server.api.query.render.MinimalRenderer();
        renderer.init(schemaFactory);
        org.apache.ambari.server.api.util.TreeNode<java.util.Set<java.lang.String>> propertyTree = renderer.finalizeProperties(queryTree, true);
        org.junit.Assert.assertEquals(1, propertyTree.getChildren().size());
        org.junit.Assert.assertEquals(2, propertyTree.getObject().size());
        org.junit.Assert.assertTrue(propertyTree.getObject().contains("ServiceInfo/service_name"));
        org.junit.Assert.assertTrue(propertyTree.getObject().contains("foo/bar"));
        org.apache.ambari.server.api.util.TreeNode<java.util.Set<java.lang.String>> componentNode = propertyTree.getChild("Component");
        org.junit.Assert.assertEquals(0, componentNode.getChildren().size());
        org.junit.Assert.assertEquals(1, componentNode.getObject().size());
        org.junit.Assert.assertTrue(componentNode.getObject().contains("ServiceComponentInfo/component_name"));
        EasyMock.verify(schemaFactory, serviceSchema, componentSchema);
    }

    @org.junit.Test
    public void testFinalizeResult() throws java.lang.Exception {
        org.apache.ambari.server.controller.spi.SchemaFactory schemaFactory = EasyMock.createNiceMock(org.apache.ambari.server.controller.spi.SchemaFactory.class);
        org.apache.ambari.server.controller.spi.Schema clusterSchema = EasyMock.createNiceMock(org.apache.ambari.server.controller.spi.Schema.class);
        org.apache.ambari.server.controller.spi.Schema hostSchema = EasyMock.createNiceMock(org.apache.ambari.server.controller.spi.Schema.class);
        org.apache.ambari.server.controller.spi.Schema hostComponentSchema = EasyMock.createNiceMock(org.apache.ambari.server.controller.spi.Schema.class);
        EasyMock.expect(schemaFactory.getSchema(org.apache.ambari.server.controller.spi.Resource.Type.Cluster)).andReturn(clusterSchema).anyTimes();
        EasyMock.expect(schemaFactory.getSchema(org.apache.ambari.server.controller.spi.Resource.Type.Host)).andReturn(hostSchema).anyTimes();
        EasyMock.expect(schemaFactory.getSchema(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent)).andReturn(hostComponentSchema).anyTimes();
        EasyMock.expect(clusterSchema.getKeyPropertyId(org.apache.ambari.server.controller.spi.Resource.Type.Cluster)).andReturn("Clusters/cluster_name").anyTimes();
        EasyMock.expect(hostSchema.getKeyPropertyId(org.apache.ambari.server.controller.spi.Resource.Type.Cluster)).andReturn("Hosts/cluster_name").anyTimes();
        EasyMock.expect(hostSchema.getKeyPropertyId(org.apache.ambari.server.controller.spi.Resource.Type.Host)).andReturn("Hosts/host_name").anyTimes();
        EasyMock.expect(hostComponentSchema.getKeyPropertyId(org.apache.ambari.server.controller.spi.Resource.Type.Cluster)).andReturn("HostRoles/cluster_name").anyTimes();
        EasyMock.expect(hostComponentSchema.getKeyPropertyId(org.apache.ambari.server.controller.spi.Resource.Type.Host)).andReturn("HostRoles/host_name").anyTimes();
        EasyMock.expect(hostComponentSchema.getKeyPropertyId(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent)).andReturn("HostRoles/component_name").anyTimes();
        EasyMock.replay(schemaFactory, clusterSchema, hostSchema, hostComponentSchema);
        org.apache.ambari.server.api.services.Result result = new org.apache.ambari.server.api.services.ResultImpl(true);
        createResultTree(result.getResultTree());
        org.apache.ambari.server.api.query.render.MinimalRenderer renderer = new org.apache.ambari.server.api.query.render.MinimalRenderer();
        renderer.init(schemaFactory);
        renderer.finalizeProperties(createPropertyTree(), false);
        org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> resultTree = renderer.finalizeResult(result).getResultTree();
        org.junit.Assert.assertNull(resultTree.getStringProperty("isCollection"));
        org.junit.Assert.assertEquals(1, resultTree.getChildren().size());
        org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> clusterNode = resultTree.getChildren().iterator().next();
        org.apache.ambari.server.controller.spi.Resource clusterResource = clusterNode.getObject();
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.Object>> clusterProperties = clusterResource.getPropertiesMap();
        org.junit.Assert.assertEquals(2, clusterProperties.size());
        org.junit.Assert.assertEquals(3, clusterProperties.get("Clusters").size());
        org.junit.Assert.assertEquals("testCluster", clusterProperties.get("Clusters").get("cluster_name"));
        org.junit.Assert.assertEquals("HDP-1.3.3", clusterProperties.get("Clusters").get("version"));
        org.junit.Assert.assertEquals("value1", clusterProperties.get("Clusters").get("prop1"));
        org.junit.Assert.assertEquals(1, clusterProperties.get("").size());
        org.junit.Assert.assertEquals("bar", clusterProperties.get("").get("foo"));
        org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> hosts = clusterNode.getChildren().iterator().next();
        for (org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> hostNode : hosts.getChildren()) {
            org.apache.ambari.server.controller.spi.Resource hostResource = hostNode.getObject();
            java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.Object>> hostProperties = hostResource.getPropertiesMap();
            org.junit.Assert.assertEquals(1, hostProperties.size());
            org.junit.Assert.assertEquals(1, hostProperties.get("Hosts").size());
            org.junit.Assert.assertTrue(hostProperties.get("Hosts").containsKey("host_name"));
            for (org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> componentNode : hostNode.getChildren().iterator().next().getChildren()) {
                org.apache.ambari.server.controller.spi.Resource componentResource = componentNode.getObject();
                java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.Object>> componentProperties = componentResource.getPropertiesMap();
                org.junit.Assert.assertEquals(1, componentProperties.size());
                org.junit.Assert.assertEquals(1, componentProperties.get("HostRoles").size());
                org.junit.Assert.assertTrue(componentProperties.get("HostRoles").containsKey("component_name"));
            }
        }
    }

    @org.junit.Test
    public void testFinalizeResult_propsSetOnSubResource() throws java.lang.Exception {
        org.apache.ambari.server.controller.spi.SchemaFactory schemaFactory = EasyMock.createNiceMock(org.apache.ambari.server.controller.spi.SchemaFactory.class);
        org.apache.ambari.server.controller.spi.Schema clusterSchema = EasyMock.createNiceMock(org.apache.ambari.server.controller.spi.Schema.class);
        org.apache.ambari.server.controller.spi.Schema hostSchema = EasyMock.createNiceMock(org.apache.ambari.server.controller.spi.Schema.class);
        org.apache.ambari.server.controller.spi.Schema hostComponentSchema = EasyMock.createNiceMock(org.apache.ambari.server.controller.spi.Schema.class);
        EasyMock.expect(schemaFactory.getSchema(org.apache.ambari.server.controller.spi.Resource.Type.Cluster)).andReturn(clusterSchema).anyTimes();
        EasyMock.expect(schemaFactory.getSchema(org.apache.ambari.server.controller.spi.Resource.Type.Host)).andReturn(hostSchema).anyTimes();
        EasyMock.expect(schemaFactory.getSchema(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent)).andReturn(hostComponentSchema).anyTimes();
        EasyMock.expect(clusterSchema.getKeyPropertyId(org.apache.ambari.server.controller.spi.Resource.Type.Cluster)).andReturn("Clusters/cluster_name").anyTimes();
        EasyMock.expect(hostSchema.getKeyPropertyId(org.apache.ambari.server.controller.spi.Resource.Type.Cluster)).andReturn("Hosts/cluster_name").anyTimes();
        EasyMock.expect(hostSchema.getKeyPropertyId(org.apache.ambari.server.controller.spi.Resource.Type.Host)).andReturn("Hosts/host_name").anyTimes();
        EasyMock.expect(hostComponentSchema.getKeyPropertyId(org.apache.ambari.server.controller.spi.Resource.Type.Cluster)).andReturn("HostRoles/cluster_name").anyTimes();
        EasyMock.expect(hostComponentSchema.getKeyPropertyId(org.apache.ambari.server.controller.spi.Resource.Type.Host)).andReturn("HostRoles/host_name").anyTimes();
        EasyMock.expect(hostComponentSchema.getKeyPropertyId(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent)).andReturn("HostRoles/component_name").anyTimes();
        EasyMock.replay(schemaFactory, clusterSchema, hostSchema, hostComponentSchema);
        org.apache.ambari.server.api.services.Result result = new org.apache.ambari.server.api.services.ResultImpl(true);
        createResultTree(result.getResultTree());
        org.apache.ambari.server.api.query.render.MinimalRenderer renderer = new org.apache.ambari.server.api.query.render.MinimalRenderer();
        renderer.init(schemaFactory);
        renderer.finalizeProperties(createPropertyTreeWithSubProps(), false);
        org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> resultTree = renderer.finalizeResult(result).getResultTree();
        org.junit.Assert.assertNull(resultTree.getStringProperty("isCollection"));
        org.junit.Assert.assertEquals(1, resultTree.getChildren().size());
        org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> clusterNode = resultTree.getChildren().iterator().next();
        org.apache.ambari.server.controller.spi.Resource clusterResource = clusterNode.getObject();
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.Object>> clusterProperties = clusterResource.getPropertiesMap();
        org.junit.Assert.assertEquals(2, clusterProperties.size());
        org.junit.Assert.assertEquals(3, clusterProperties.get("Clusters").size());
        org.junit.Assert.assertEquals("testCluster", clusterProperties.get("Clusters").get("cluster_name"));
        org.junit.Assert.assertEquals("HDP-1.3.3", clusterProperties.get("Clusters").get("version"));
        org.junit.Assert.assertEquals("value1", clusterProperties.get("Clusters").get("prop1"));
        org.junit.Assert.assertEquals(1, clusterProperties.get("").size());
        org.junit.Assert.assertEquals("bar", clusterProperties.get("").get("foo"));
        org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> hosts = clusterNode.getChildren().iterator().next();
        for (org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> hostNode : hosts.getChildren()) {
            org.apache.ambari.server.controller.spi.Resource hostResource = hostNode.getObject();
            java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.Object>> hostProperties = hostResource.getPropertiesMap();
            org.junit.Assert.assertEquals(2, hostProperties.size());
            org.junit.Assert.assertEquals(1, hostProperties.get("Hosts").size());
            org.junit.Assert.assertTrue(hostProperties.get("Hosts").containsKey("host_name"));
            org.junit.Assert.assertEquals(1, hostProperties.get("").size());
            org.junit.Assert.assertEquals("bar", hostProperties.get("").get("foo"));
            for (org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> componentNode : hostNode.getChildren().iterator().next().getChildren()) {
                org.apache.ambari.server.controller.spi.Resource componentResource = componentNode.getObject();
                java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.Object>> componentProperties = componentResource.getPropertiesMap();
                org.junit.Assert.assertEquals(1, componentProperties.size());
                org.junit.Assert.assertEquals(1, componentProperties.get("HostRoles").size());
                org.junit.Assert.assertTrue(componentProperties.get("HostRoles").containsKey("component_name"));
            }
        }
    }

    private org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.api.query.QueryInfo> createPropertyTree() {
        org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.api.query.QueryInfo> propertyTree = new org.apache.ambari.server.api.util.TreeNodeImpl<>(null, new org.apache.ambari.server.api.query.QueryInfo(new org.apache.ambari.server.api.resources.ClusterResourceDefinition(), new java.util.HashSet<>()), "Cluster");
        java.util.Set<java.lang.String> clusterProperties = propertyTree.getObject().getProperties();
        clusterProperties.add("Clusters/cluster_name");
        clusterProperties.add("Clusters/version");
        clusterProperties.add("Clusters/prop1");
        clusterProperties.add("foo");
        return propertyTree;
    }

    private org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.api.query.QueryInfo> createPropertyTreeWithSubProps() {
        org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.api.query.QueryInfo> propertyTree = new org.apache.ambari.server.api.util.TreeNodeImpl<>(null, new org.apache.ambari.server.api.query.QueryInfo(new org.apache.ambari.server.api.resources.ClusterResourceDefinition(), new java.util.HashSet<>()), "Cluster");
        java.util.Set<java.lang.String> clusterProperties = propertyTree.getObject().getProperties();
        clusterProperties.add("Clusters/cluster_name");
        clusterProperties.add("Clusters/version");
        clusterProperties.add("Clusters/prop1");
        clusterProperties.add("foo");
        propertyTree.addChild(new org.apache.ambari.server.api.query.QueryInfo(new org.apache.ambari.server.api.resources.HostResourceDefinition(), new java.util.HashSet<>()), "Host");
        propertyTree.getChild("Host").getObject().getProperties().add("foo");
        return propertyTree;
    }

    private void createResultTree(org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> resultTree) throws java.lang.Exception {
        org.apache.ambari.server.controller.spi.Resource clusterResource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Cluster);
        clusterResource.setProperty("Clusters/cluster_name", "testCluster");
        clusterResource.setProperty("Clusters/version", "HDP-1.3.3");
        clusterResource.setProperty("Clusters/prop1", "value1");
        clusterResource.setProperty("foo", "bar");
        org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> clusterTree = resultTree.addChild(clusterResource, "Cluster:1");
        org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> hostsTree = clusterTree.addChild(null, "hosts");
        hostsTree.setProperty("isCollection", "true");
        org.apache.ambari.server.controller.spi.Resource hostResource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Host);
        org.apache.ambari.server.controller.utilities.PropertyHelper.setKeyPropertyIds(org.apache.ambari.server.controller.spi.Resource.Type.Host, org.apache.ambari.server.controller.internal.HostResourceProvider.keyPropertyIds);
        org.apache.ambari.server.controller.utilities.PropertyHelper.setKeyPropertyIds(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent, org.apache.ambari.server.controller.internal.HostComponentResourceProvider.keyPropertyIds);
        hostResource.setProperty("Hosts/host_name", "testHost");
        hostResource.setProperty("Hosts/cluster_name", "testCluster");
        hostResource.setProperty("foo", "bar");
        org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> hostTree = hostsTree.addChild(hostResource, "Host:1");
        org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> hostComponentsTree = hostTree.addChild(null, "host_components");
        hostComponentsTree.setProperty("isCollection", "true");
        org.apache.ambari.server.controller.spi.Resource nnComponentResource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent);
        nnComponentResource.setProperty("HostRoles/component_name", "NAMENODE");
        nnComponentResource.setProperty("HostRoles/host_name", "testHost");
        nnComponentResource.setProperty("HostRoles/cluster_name", "testCluster");
        org.apache.ambari.server.controller.spi.Resource dnComponentResource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent);
        dnComponentResource.setProperty("HostRoles/component_name", "DATANODE");
        dnComponentResource.setProperty("HostRoles/host_name", "testHost");
        dnComponentResource.setProperty("HostRoles/cluster_name", "testCluster");
        org.apache.ambari.server.controller.spi.Resource jtComponentResource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent);
        jtComponentResource.setProperty("HostRoles/component_name", "JOBTRACKER");
        jtComponentResource.setProperty("HostRoles/host_name", "testHost");
        jtComponentResource.setProperty("HostRoles/cluster_name", "testCluster");
        org.apache.ambari.server.controller.spi.Resource ttComponentResource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent);
        ttComponentResource.setProperty("HostRoles/component_name", "TASKTRACKER");
        jtComponentResource.setProperty("HostRoles/host_name", "testHost");
        jtComponentResource.setProperty("HostRoles/cluster_name", "testCluster");
        hostComponentsTree.addChild(nnComponentResource, "HostComponent:1");
        hostComponentsTree.addChild(dnComponentResource, "HostComponent:2");
        hostComponentsTree.addChild(jtComponentResource, "HostComponent:3");
        hostComponentsTree.addChild(ttComponentResource, "HostComponent:4");
        org.apache.ambari.server.controller.spi.Resource host2Resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Host);
        host2Resource.setProperty("Hosts/host_name", "testHost2");
        host2Resource.setProperty("Hosts/cluster_name", "testCluster");
        host2Resource.setProperty("foo", "bar");
        org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> host2Tree = hostsTree.addChild(host2Resource, "Host:2");
        org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> host2ComponentsTree = host2Tree.addChild(null, "host_components");
        host2ComponentsTree.setProperty("isCollection", "true");
        host2ComponentsTree.addChild(dnComponentResource, "HostComponent:1");
        host2ComponentsTree.addChild(ttComponentResource, "HostComponent:2");
        org.apache.ambari.server.controller.spi.Resource host3Resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Host);
        host3Resource.setProperty("Hosts/host_name", "testHost3");
        host3Resource.setProperty("Hosts/host_name", "testHost2");
        host3Resource.setProperty("foo", "bar");
        org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> host3Tree = hostsTree.addChild(host3Resource, "Host:3");
        org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> host3ComponentsTree = host3Tree.addChild(null, "host_components");
        host3ComponentsTree.setProperty("isCollection", "true");
        host3ComponentsTree.addChild(dnComponentResource, "HostComponent:1");
        host3ComponentsTree.addChild(ttComponentResource, "HostComponent:2");
    }
}