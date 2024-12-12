package org.apache.ambari.server.api.query.render;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
public class MetricsPaddingRendererTest {
    @org.junit.Test
    public void testFinalizeProperties__NullPadding_property() {
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
        org.apache.ambari.server.api.query.render.MetricsPaddingRenderer renderer = new org.apache.ambari.server.api.query.render.MetricsPaddingRenderer("null_padding");
        renderer.init(schemaFactory);
        org.apache.ambari.server.api.util.TreeNode<java.util.Set<java.lang.String>> propertyTree = renderer.finalizeProperties(queryTree, false);
        org.junit.Assert.assertEquals(4, propertyTree.getObject().size());
        org.junit.Assert.assertTrue(propertyTree.getObject().contains("ServiceInfo/service_name"));
        org.junit.Assert.assertTrue(propertyTree.getObject().contains("ServiceInfo/cluster_name"));
        org.junit.Assert.assertTrue(propertyTree.getObject().contains("foo/bar"));
        org.junit.Assert.assertTrue(propertyTree.getObject().contains("params/padding/NULLS"));
        org.junit.Assert.assertEquals(0, propertyTree.getChildren().size());
        EasyMock.verify(schemaFactory, schema);
    }
}