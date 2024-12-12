package org.apache.ambari.server.topology;
import org.easymock.EasyMockSupport;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.powermock.api.easymock.PowerMock.createStrictMock;
import static org.powermock.api.easymock.PowerMock.replay;
import static org.powermock.api.easymock.PowerMock.reset;
import static org.powermock.api.easymock.PowerMock.verify;
@java.lang.SuppressWarnings("unchecked")
public class BlueprintFactoryTest {
    private static final java.lang.String BLUEPRINT_NAME = "test-blueprint";

    org.apache.ambari.server.topology.BlueprintFactory factory = new org.apache.ambari.server.topology.BlueprintFactory();

    org.apache.ambari.server.controller.internal.Stack stack = EasyMock.createNiceMock(org.apache.ambari.server.controller.internal.Stack.class);

    org.apache.ambari.server.topology.BlueprintFactory testFactory = new org.apache.ambari.server.topology.BlueprintFactoryTest.TestBlueprintFactory(stack);

    org.apache.ambari.server.orm.dao.BlueprintDAO dao = createStrictMock(org.apache.ambari.server.orm.dao.BlueprintDAO.class);

    org.apache.ambari.server.orm.entities.BlueprintEntity entity = createStrictMock(org.apache.ambari.server.orm.entities.BlueprintEntity.class);

    org.apache.ambari.server.orm.entities.BlueprintConfigEntity configEntity = createStrictMock(org.apache.ambari.server.orm.entities.BlueprintConfigEntity.class);

    @org.junit.Before
    public void init() throws java.lang.Exception {
        setPrivateField(factory, "blueprintDAO", dao);
        java.util.Map<java.lang.String, java.util.Collection<java.lang.String>> componentMap = new java.util.HashMap<>();
        java.util.Collection<java.lang.String> components1 = new java.util.HashSet<>();
        componentMap.put("test-service1", components1);
        components1.add("component1");
        java.util.Collection<java.lang.String> components2 = new java.util.HashSet<>();
        componentMap.put("test-service2", components2);
        components2.add("component2");
        EasyMock.expect(stack.getComponents()).andReturn(componentMap).anyTimes();
        EasyMock.expect(stack.isMasterComponent("component1")).andReturn(true).anyTimes();
        EasyMock.expect(stack.isMasterComponent("component2")).andReturn(false).anyTimes();
        EasyMock.expect(stack.getServiceForComponent("component1")).andReturn("test-service1").anyTimes();
        EasyMock.expect(stack.getServiceForComponent("component2")).andReturn("test-service2").anyTimes();
    }

    @org.junit.After
    public void tearDown() {
        reset(stack, dao, entity, configEntity);
    }

    @org.junit.Test
    public void testGetBlueprint_NotFound() throws java.lang.Exception {
        EasyMock.expect(dao.findByName(org.apache.ambari.server.topology.BlueprintFactoryTest.BLUEPRINT_NAME)).andReturn(null).once();
        replay(dao, entity, configEntity);
        org.junit.Assert.assertNull(factory.getBlueprint(org.apache.ambari.server.topology.BlueprintFactoryTest.BLUEPRINT_NAME));
    }

    @org.junit.Test
    public void testCreateBlueprint() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.lang.Object> props = org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.getBlueprintTestProperties().iterator().next();
        replay(stack, dao, entity, configEntity);
        org.apache.ambari.server.topology.Blueprint blueprint = testFactory.createBlueprint(props, null);
        org.junit.Assert.assertEquals(org.apache.ambari.server.topology.BlueprintFactoryTest.BLUEPRINT_NAME, blueprint.getName());
        org.junit.Assert.assertSame(stack, blueprint.getStack());
        org.junit.Assert.assertEquals(2, blueprint.getHostGroups().size());
        java.util.Map<java.lang.String, org.apache.ambari.server.topology.HostGroup> hostGroups = blueprint.getHostGroups();
        org.apache.ambari.server.topology.HostGroup group1 = hostGroups.get("group1");
        org.junit.Assert.assertEquals("group1", group1.getName());
        org.junit.Assert.assertEquals("1", group1.getCardinality());
        java.util.Collection<java.lang.String> components = group1.getComponentNames();
        org.junit.Assert.assertEquals(2, components.size());
        org.junit.Assert.assertTrue(components.contains("component1"));
        org.junit.Assert.assertTrue(components.contains("component2"));
        java.util.Collection<java.lang.String> services = group1.getServices();
        org.junit.Assert.assertEquals(2, services.size());
        org.junit.Assert.assertTrue(services.contains("test-service1"));
        org.junit.Assert.assertTrue(services.contains("test-service2"));
        org.junit.Assert.assertTrue(group1.containsMasterComponent());
        org.apache.ambari.server.topology.Configuration configuration = group1.getConfiguration();
        org.junit.Assert.assertTrue(configuration.getProperties().isEmpty());
        org.junit.Assert.assertTrue(configuration.getAttributes().isEmpty());
        org.apache.ambari.server.topology.HostGroup group2 = hostGroups.get("group2");
        org.junit.Assert.assertEquals("group2", group2.getName());
        org.junit.Assert.assertEquals("2", group2.getCardinality());
        components = group2.getComponentNames();
        org.junit.Assert.assertEquals(1, components.size());
        org.junit.Assert.assertTrue(components.contains("component1"));
        services = group2.getServices();
        org.junit.Assert.assertEquals(1, services.size());
        org.junit.Assert.assertTrue(services.contains("test-service1"));
        org.junit.Assert.assertTrue(group2.containsMasterComponent());
        configuration = group2.getConfiguration();
        org.junit.Assert.assertTrue(configuration.getProperties().isEmpty());
        org.junit.Assert.assertTrue(configuration.getAttributes().isEmpty());
        verify(dao, entity, configEntity);
    }

    @org.junit.Test(expected = org.apache.ambari.server.stack.NoSuchStackException.class)
    public void testCreateInvalidStack() throws java.lang.Exception {
        org.easymock.EasyMockSupport mockSupport = new org.easymock.EasyMockSupport();
        org.apache.ambari.server.topology.StackFactory mockStackFactory = mockSupport.createMock(org.apache.ambari.server.topology.StackFactory.class);
        EasyMock.expect(mockStackFactory.createStack("null", "null", null)).andThrow(new org.apache.ambari.server.ObjectNotFoundException("Invalid Stack"));
        mockSupport.replayAll();
        org.apache.ambari.server.topology.BlueprintFactory factoryUnderTest = new org.apache.ambari.server.topology.BlueprintFactory(mockStackFactory);
        factoryUnderTest.createStack(new java.util.HashMap<>());
        mockSupport.verifyAll();
    }

    @org.junit.Test(expected = java.lang.IllegalArgumentException.class)
    public void testCreate_NoBlueprintName() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.lang.Object> props = org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.getBlueprintTestProperties().iterator().next();
        props.remove(org.apache.ambari.server.controller.internal.BlueprintResourceProvider.BLUEPRINT_NAME_PROPERTY_ID);
        replay(stack, dao, entity, configEntity);
        testFactory.createBlueprint(props, null);
    }

    @org.junit.Test(expected = java.lang.IllegalArgumentException.class)
    public void testCreate_NoHostGroups() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.lang.Object> props = org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.getBlueprintTestProperties().iterator().next();
        ((java.util.Set<java.util.Map<java.lang.String, java.lang.Object>>) (props.get(org.apache.ambari.server.controller.internal.BlueprintResourceProvider.HOST_GROUP_PROPERTY_ID))).clear();
        replay(stack, dao, entity, configEntity);
        testFactory.createBlueprint(props, null);
    }

    @org.junit.Test(expected = java.lang.IllegalArgumentException.class)
    public void testCreate_MissingHostGroupName() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.lang.Object> props = org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.getBlueprintTestProperties().iterator().next();
        ((java.util.Set<java.util.Map<java.lang.String, java.lang.Object>>) (props.get(org.apache.ambari.server.controller.internal.BlueprintResourceProvider.HOST_GROUP_PROPERTY_ID))).iterator().next().remove("name");
        replay(stack, dao, entity, configEntity);
        testFactory.createBlueprint(props, null);
    }

    @org.junit.Test(expected = java.lang.IllegalArgumentException.class)
    public void testCreate_HostGroupWithNoComponents() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.lang.Object> props = org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.getBlueprintTestProperties().iterator().next();
        ((java.util.Set<java.util.Map<java.lang.String, java.lang.Object>>) (props.get(org.apache.ambari.server.controller.internal.BlueprintResourceProvider.HOST_GROUP_PROPERTY_ID))).iterator().next().remove(org.apache.ambari.server.controller.internal.BlueprintResourceProvider.COMPONENT_PROPERTY_ID);
        replay(stack, dao, entity, configEntity);
        testFactory.createBlueprint(props, null);
    }

    @org.junit.Test(expected = java.lang.IllegalArgumentException.class)
    public void testCreate_HostGroupWithInvalidComponent() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.lang.Object> props = org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.getBlueprintTestProperties().iterator().next();
        ((java.util.Set<java.util.Map<java.lang.String, java.lang.Object>>) (((java.util.Set<java.util.Map<java.lang.String, java.lang.Object>>) (props.get(org.apache.ambari.server.controller.internal.BlueprintResourceProvider.HOST_GROUP_PROPERTY_ID))).iterator().next().get(org.apache.ambari.server.controller.internal.BlueprintResourceProvider.COMPONENT_PROPERTY_ID))).iterator().next().put("name", "INVALID_COMPONENT");
        replay(stack, dao, entity, configEntity);
        testFactory.createBlueprint(props, null);
    }

    private class TestBlueprintFactory extends org.apache.ambari.server.topology.BlueprintFactory {
        private org.apache.ambari.server.controller.internal.Stack stack;

        public TestBlueprintFactory(org.apache.ambari.server.controller.internal.Stack stack) {
            this.stack = stack;
        }

        @java.lang.Override
        protected org.apache.ambari.server.controller.internal.Stack createStack(java.util.Map<java.lang.String, java.lang.Object> properties) throws org.apache.ambari.server.stack.NoSuchStackException {
            return stack;
        }
    }

    private void setPrivateField(java.lang.Object o, java.lang.String field, java.lang.Object value) throws java.lang.Exception {
        java.lang.Class<?> c = o.getClass();
        java.lang.reflect.Field f = c.getDeclaredField(field);
        f.setAccessible(true);
        f.set(o, value);
    }
}