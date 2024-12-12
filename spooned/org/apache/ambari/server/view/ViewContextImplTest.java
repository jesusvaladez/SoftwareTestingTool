package org.apache.ambari.server.view;
import static org.easymock.EasyMock.createMockBuilder;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
public class ViewContextImplTest {
    @org.junit.Test
    public void testGetViewName() throws java.lang.Exception {
        org.apache.ambari.server.view.configuration.InstanceConfig instanceConfig = org.apache.ambari.server.view.configuration.InstanceConfigTest.getInstanceConfigs().get(0);
        org.apache.ambari.server.orm.entities.ViewEntity viewDefinition = org.apache.ambari.server.orm.entities.ViewEntityTest.getViewEntity();
        org.apache.ambari.server.orm.entities.ViewInstanceEntity viewInstanceDefinition = new org.apache.ambari.server.orm.entities.ViewInstanceEntity(viewDefinition, instanceConfig);
        org.apache.ambari.server.view.ViewRegistry viewRegistry = EasyMock.createNiceMock(org.apache.ambari.server.view.ViewRegistry.class);
        org.apache.ambari.server.view.ViewContextImpl viewContext = new org.apache.ambari.server.view.ViewContextImpl(viewInstanceDefinition, viewRegistry);
        org.junit.Assert.assertEquals("MY_VIEW", viewContext.getViewName());
    }

    @org.junit.Test
    public void testGetInstanceName() throws java.lang.Exception {
        org.apache.ambari.server.view.configuration.InstanceConfig instanceConfig = org.apache.ambari.server.view.configuration.InstanceConfigTest.getInstanceConfigs().get(0);
        org.apache.ambari.server.orm.entities.ViewEntity viewDefinition = org.apache.ambari.server.orm.entities.ViewEntityTest.getViewEntity();
        org.apache.ambari.server.orm.entities.ViewInstanceEntity viewInstanceDefinition = new org.apache.ambari.server.orm.entities.ViewInstanceEntity(viewDefinition, instanceConfig);
        org.apache.ambari.server.view.ViewRegistry viewRegistry = EasyMock.createNiceMock(org.apache.ambari.server.view.ViewRegistry.class);
        org.apache.ambari.server.view.ViewContextImpl viewContext = new org.apache.ambari.server.view.ViewContextImpl(viewInstanceDefinition, viewRegistry);
        org.junit.Assert.assertEquals("INSTANCE1", viewContext.getInstanceName());
    }

    @org.junit.Test
    public void testGetProperties() throws java.lang.Exception {
        org.apache.ambari.server.view.configuration.InstanceConfig instanceConfig = org.apache.ambari.server.view.configuration.InstanceConfigTest.getInstanceConfigs().get(0);
        org.apache.ambari.server.orm.entities.ViewEntity viewDefinition = org.apache.ambari.server.orm.entities.ViewEntityTest.getViewEntity();
        org.apache.ambari.server.orm.entities.ViewInstanceEntity viewInstanceDefinition = new org.apache.ambari.server.orm.entities.ViewInstanceEntity(viewDefinition, instanceConfig);
        org.apache.ambari.server.view.ViewRegistry viewRegistry = EasyMock.createNiceMock(org.apache.ambari.server.view.ViewRegistry.class);
        EasyMock.expect(viewRegistry.getCluster(viewInstanceDefinition)).andReturn(null).anyTimes();
        viewInstanceDefinition.putProperty("p1", "v1");
        viewInstanceDefinition.putProperty("p2", new org.apache.ambari.server.view.DefaultMasker().mask("v2"));
        viewInstanceDefinition.putProperty("p3", "v3");
        org.apache.ambari.server.view.ViewContextImpl viewContext = new org.apache.ambari.server.view.ViewContextImpl(viewInstanceDefinition, viewRegistry);
        java.util.Map<java.lang.String, java.lang.String> properties = viewContext.getProperties();
        org.junit.Assert.assertEquals(3, properties.size());
        org.junit.Assert.assertEquals("v1", properties.get("p1"));
        org.junit.Assert.assertEquals("v2", properties.get("p2"));
        org.junit.Assert.assertEquals("v3", properties.get("p3"));
    }

    @org.junit.Test
    public void testGetPropertiesWithParameters() throws java.lang.Exception {
        org.apache.ambari.server.view.configuration.InstanceConfig instanceConfig = EasyMock.createNiceMock(org.apache.ambari.server.view.configuration.InstanceConfig.class);
        EasyMock.expect(instanceConfig.getName()).andReturn("Instance").anyTimes();
        EasyMock.replay(instanceConfig);
        org.apache.ambari.server.orm.entities.ViewEntity viewDefinition = EasyMock.createNiceMock(org.apache.ambari.server.orm.entities.ViewEntity.class);
        EasyMock.expect(viewDefinition.getName()).andReturn("View").anyTimes();
        EasyMock.expect(viewDefinition.getCommonName()).andReturn("View").times(2);
        EasyMock.expect(viewDefinition.getClassLoader()).andReturn(org.apache.ambari.server.view.ViewContextImplTest.class.getClassLoader()).anyTimes();
        EasyMock.expect(viewDefinition.getConfiguration()).andReturn(org.apache.ambari.server.view.configuration.ViewConfigTest.getConfig()).anyTimes();
        org.apache.ambari.server.orm.entities.ViewParameterEntity parameter1 = EasyMock.createNiceMock(org.apache.ambari.server.orm.entities.ViewParameterEntity.class);
        EasyMock.expect(parameter1.getName()).andReturn("p1").anyTimes();
        org.apache.ambari.server.orm.entities.ViewParameterEntity parameter2 = EasyMock.createNiceMock(org.apache.ambari.server.orm.entities.ViewParameterEntity.class);
        EasyMock.expect(parameter2.getName()).andReturn("p2").anyTimes();
        EasyMock.expect(viewDefinition.getParameters()).andReturn(java.util.Arrays.asList(parameter1, parameter2)).anyTimes();
        EasyMock.replay(viewDefinition, parameter1, parameter2);
        org.apache.ambari.server.orm.entities.ViewInstanceEntity viewInstanceDefinition = EasyMock.createMockBuilder(org.apache.ambari.server.orm.entities.ViewInstanceEntity.class).addMockedMethod("getUsername").addMockedMethod("getName").addMockedMethod("getViewEntity").withConstructor(viewDefinition, instanceConfig).createMock();
        EasyMock.expect(viewInstanceDefinition.getUsername()).andReturn("User").times(1);
        EasyMock.expect(viewInstanceDefinition.getUsername()).andReturn("User2").times(1);
        EasyMock.expect(viewInstanceDefinition.getName()).andReturn("Instance").anyTimes();
        EasyMock.expect(viewInstanceDefinition.getViewEntity()).andReturn(viewDefinition).anyTimes();
        EasyMock.replay(viewInstanceDefinition);
        org.apache.ambari.server.view.ViewRegistry viewRegistry = EasyMock.createNiceMock(org.apache.ambari.server.view.ViewRegistry.class);
        EasyMock.expect(viewRegistry.getCluster(viewInstanceDefinition)).andReturn(null).anyTimes();
        EasyMock.replay(viewRegistry);
        viewInstanceDefinition.putProperty("p1", "/tmp/some/path/${username}");
        viewInstanceDefinition.putProperty("p2", new org.apache.ambari.server.view.DefaultMasker().mask("/tmp/path/$viewName"));
        viewInstanceDefinition.putProperty("p3", "/path/$instanceName");
        viewInstanceDefinition.putProperty("p4", "/path/to/${unspecified_parameter}");
        viewInstanceDefinition.putProperty("p5", "/path/to/${incorrect_parameter");
        viewInstanceDefinition.putProperty("p6", "/path/to/\\${username}");
        viewInstanceDefinition.putProperty("p7", "/path/to/\\$viewName");
        viewInstanceDefinition.putProperty("p8", null);
        org.apache.ambari.server.view.ViewContextImpl viewContext = new org.apache.ambari.server.view.ViewContextImpl(viewInstanceDefinition, viewRegistry);
        java.util.Map<java.lang.String, java.lang.String> properties = viewContext.getProperties();
        org.junit.Assert.assertEquals(8, properties.size());
        org.junit.Assert.assertEquals("/tmp/some/path/User", properties.get("p1"));
        org.junit.Assert.assertEquals("/tmp/path/View", properties.get("p2"));
        org.junit.Assert.assertEquals("/path/Instance", properties.get("p3"));
        org.junit.Assert.assertEquals("/path/to/${unspecified_parameter}", properties.get("p4"));
        org.junit.Assert.assertEquals("/path/to/${incorrect_parameter", properties.get("p5"));
        org.junit.Assert.assertEquals("/path/to/${username}", properties.get("p6"));
        org.junit.Assert.assertEquals("/path/to/$viewName", properties.get("p7"));
        org.junit.Assert.assertNull(properties.get("p8"));
        properties = viewContext.getProperties();
        org.junit.Assert.assertEquals(8, properties.size());
        org.junit.Assert.assertEquals("/tmp/some/path/User2", properties.get("p1"));
        org.junit.Assert.assertEquals("/tmp/path/View", properties.get("p2"));
        org.junit.Assert.assertEquals("/path/Instance", properties.get("p3"));
        org.junit.Assert.assertEquals("/path/to/${unspecified_parameter}", properties.get("p4"));
        org.junit.Assert.assertEquals("/path/to/${incorrect_parameter", properties.get("p5"));
        org.junit.Assert.assertEquals("/path/to/${username}", properties.get("p6"));
        org.junit.Assert.assertEquals("/path/to/$viewName", properties.get("p7"));
        org.junit.Assert.assertNull(properties.get("p8"));
    }

    @org.junit.Test
    public void testGetResourceProvider() throws java.lang.Exception {
        org.apache.ambari.server.view.configuration.InstanceConfig instanceConfig = org.apache.ambari.server.view.configuration.InstanceConfigTest.getInstanceConfigs().get(0);
        org.apache.ambari.server.orm.entities.ViewEntity viewDefinition = org.apache.ambari.server.orm.entities.ViewEntityTest.getViewEntity();
        org.apache.ambari.server.orm.entities.ViewInstanceEntity viewInstanceDefinition = new org.apache.ambari.server.orm.entities.ViewInstanceEntity(viewDefinition, instanceConfig);
        org.apache.ambari.server.view.ViewRegistry viewRegistry = EasyMock.createNiceMock(org.apache.ambari.server.view.ViewRegistry.class);
        org.apache.ambari.view.ResourceProvider provider = EasyMock.createNiceMock(org.apache.ambari.view.ResourceProvider.class);
        org.apache.ambari.server.controller.spi.Resource.Type type = new org.apache.ambari.server.controller.spi.Resource.Type("MY_VIEW{1.0.0}/myType");
        viewInstanceDefinition.addResourceProvider(type, provider);
        org.apache.ambari.server.view.ViewContextImpl viewContext = new org.apache.ambari.server.view.ViewContextImpl(viewInstanceDefinition, viewRegistry);
        org.junit.Assert.assertEquals(provider, viewContext.getResourceProvider("myType"));
    }

    @org.junit.Test
    public void testGetURLStreamProvider() throws java.lang.Exception {
        org.apache.ambari.server.view.configuration.InstanceConfig instanceConfig = org.apache.ambari.server.view.configuration.InstanceConfigTest.getInstanceConfigs().get(0);
        org.apache.ambari.server.orm.entities.ViewEntity viewDefinition = org.apache.ambari.server.orm.entities.ViewEntityTest.getViewEntity();
        org.apache.ambari.server.orm.entities.ViewInstanceEntity viewInstanceDefinition = new org.apache.ambari.server.orm.entities.ViewInstanceEntity(viewDefinition, instanceConfig);
        org.apache.ambari.server.view.ViewRegistry viewRegistry = EasyMock.createNiceMock(org.apache.ambari.server.view.ViewRegistry.class);
        org.apache.ambari.server.view.ViewURLStreamProvider urlStreamProvider = EasyMock.createNiceMock(org.apache.ambari.server.view.ViewURLStreamProvider.class);
        org.apache.ambari.server.view.ViewURLStreamProvider urlStreamProvider2 = EasyMock.createNiceMock(org.apache.ambari.server.view.ViewURLStreamProvider.class);
        org.apache.ambari.view.ResourceProvider provider = EasyMock.createNiceMock(org.apache.ambari.view.ResourceProvider.class);
        org.apache.ambari.server.controller.spi.Resource.Type type = new org.apache.ambari.server.controller.spi.Resource.Type("MY_VIEW/myType");
        viewInstanceDefinition.addResourceProvider(type, provider);
        org.apache.ambari.server.view.ViewContextImpl viewContext = new org.apache.ambari.server.view.ViewContextImpl(viewInstanceDefinition, viewRegistry);
        EasyMock.expect(viewRegistry.createURLStreamProvider(viewContext)).andReturn(urlStreamProvider);
        EasyMock.expect(viewRegistry.createURLStreamProvider(viewContext)).andReturn(urlStreamProvider2);
        EasyMock.replay(viewRegistry);
        org.junit.Assert.assertEquals(urlStreamProvider, viewContext.getURLStreamProvider());
        org.junit.Assert.assertEquals(urlStreamProvider2, viewContext.getURLStreamProvider());
        EasyMock.verify(viewRegistry);
    }

    @org.junit.Test
    public void testGetURLConnectionProvider() throws java.lang.Exception {
        org.apache.ambari.server.view.configuration.InstanceConfig instanceConfig = org.apache.ambari.server.view.configuration.InstanceConfigTest.getInstanceConfigs().get(0);
        org.apache.ambari.server.orm.entities.ViewEntity viewDefinition = org.apache.ambari.server.orm.entities.ViewEntityTest.getViewEntity();
        org.apache.ambari.server.orm.entities.ViewInstanceEntity viewInstanceDefinition = new org.apache.ambari.server.orm.entities.ViewInstanceEntity(viewDefinition, instanceConfig);
        org.apache.ambari.server.view.ViewRegistry viewRegistry = EasyMock.createNiceMock(org.apache.ambari.server.view.ViewRegistry.class);
        org.apache.ambari.server.view.ViewURLStreamProvider urlStreamProvider = EasyMock.createNiceMock(org.apache.ambari.server.view.ViewURLStreamProvider.class);
        org.apache.ambari.view.ResourceProvider provider = EasyMock.createNiceMock(org.apache.ambari.view.ResourceProvider.class);
        org.apache.ambari.server.controller.spi.Resource.Type type = new org.apache.ambari.server.controller.spi.Resource.Type("MY_VIEW/myType");
        viewInstanceDefinition.addResourceProvider(type, provider);
        org.apache.ambari.view.ViewContext viewContext = new org.apache.ambari.server.view.ViewContextImpl(viewInstanceDefinition, viewRegistry);
        EasyMock.expect(viewRegistry.createURLStreamProvider(viewContext)).andReturn(urlStreamProvider);
        EasyMock.replay(viewRegistry);
        org.junit.Assert.assertEquals(urlStreamProvider, viewContext.getURLConnectionProvider());
        EasyMock.verify(viewRegistry);
    }

    @org.junit.Test
    public void testGetAmbariStreamProvider() throws java.lang.Exception {
        org.apache.ambari.server.view.configuration.InstanceConfig instanceConfig = org.apache.ambari.server.view.configuration.InstanceConfigTest.getInstanceConfigs().get(0);
        org.apache.ambari.server.orm.entities.ViewEntity viewDefinition = org.apache.ambari.server.orm.entities.ViewEntityTest.getViewEntity();
        org.apache.ambari.server.orm.entities.ViewInstanceEntity viewInstanceDefinition = new org.apache.ambari.server.orm.entities.ViewInstanceEntity(viewDefinition, instanceConfig);
        org.apache.ambari.server.view.ViewRegistry viewRegistry = EasyMock.createNiceMock(org.apache.ambari.server.view.ViewRegistry.class);
        org.apache.ambari.server.view.ViewAmbariStreamProvider ambariStreamProvider = EasyMock.createNiceMock(org.apache.ambari.server.view.ViewAmbariStreamProvider.class);
        org.apache.ambari.view.ResourceProvider provider = EasyMock.createNiceMock(org.apache.ambari.view.ResourceProvider.class);
        org.apache.ambari.server.controller.spi.Resource.Type type = new org.apache.ambari.server.controller.spi.Resource.Type("MY_VIEW/myType");
        viewInstanceDefinition.addResourceProvider(type, provider);
        EasyMock.expect(viewRegistry.createAmbariStreamProvider()).andReturn(ambariStreamProvider);
        EasyMock.replay(viewRegistry);
        org.apache.ambari.server.view.ViewContextImpl viewContext = new org.apache.ambari.server.view.ViewContextImpl(viewInstanceDefinition, viewRegistry);
        org.junit.Assert.assertEquals(ambariStreamProvider, viewContext.getAmbariStreamProvider());
        EasyMock.verify(viewRegistry);
    }

    @org.junit.Test
    public void testGetCluster() throws java.lang.Exception {
        org.apache.ambari.server.view.configuration.InstanceConfig instanceConfig = org.apache.ambari.server.view.configuration.InstanceConfigTest.getInstanceConfigs().get(0);
        org.apache.ambari.server.orm.entities.ViewEntity viewDefinition = org.apache.ambari.server.orm.entities.ViewEntityTest.getViewEntity();
        org.apache.ambari.server.orm.entities.ViewInstanceEntity viewInstanceDefinition = new org.apache.ambari.server.orm.entities.ViewInstanceEntity(viewDefinition, instanceConfig);
        org.apache.ambari.server.view.ViewRegistry viewRegistry = EasyMock.createNiceMock(org.apache.ambari.server.view.ViewRegistry.class);
        org.apache.ambari.view.cluster.Cluster cluster = EasyMock.createNiceMock(org.apache.ambari.view.cluster.Cluster.class);
        EasyMock.expect(viewRegistry.getCluster(viewInstanceDefinition)).andReturn(cluster);
        EasyMock.replay(viewRegistry);
        org.apache.ambari.server.view.ViewContextImpl viewContext = new org.apache.ambari.server.view.ViewContextImpl(viewInstanceDefinition, viewRegistry);
        org.junit.Assert.assertEquals(cluster, viewContext.getCluster());
        EasyMock.verify(viewRegistry);
    }
}