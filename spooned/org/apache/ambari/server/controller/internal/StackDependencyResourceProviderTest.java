package org.apache.ambari.server.controller.internal;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.reset;
import static org.easymock.EasyMock.verify;
public class StackDependencyResourceProviderTest {
    private static final org.apache.ambari.server.api.services.AmbariMetaInfo metaInfo = EasyMock.createMock(org.apache.ambari.server.api.services.AmbariMetaInfo.class);

    @org.junit.BeforeClass
    public static void initClass() {
        org.apache.ambari.server.controller.internal.StackDependencyResourceProvider.init(org.apache.ambari.server.controller.internal.StackDependencyResourceProviderTest.metaInfo);
    }

    @org.junit.Before
    public void resetGlobalMocks() {
        EasyMock.reset(org.apache.ambari.server.controller.internal.StackDependencyResourceProviderTest.metaInfo);
    }

    @org.junit.Test
    public void testGetResources() throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.controller.spi.Request request = EasyMock.createNiceMock(org.apache.ambari.server.controller.spi.Request.class);
        org.apache.ambari.server.state.DependencyInfo dependencyInfo = new org.apache.ambari.server.state.DependencyInfo();
        dependencyInfo.setName("service_name/comp_name");
        dependencyInfo.setScope("cluster");
        org.apache.ambari.server.controller.spi.Predicate namePredicate = new org.apache.ambari.server.controller.predicate.EqualsPredicate<>(org.apache.ambari.server.controller.internal.StackDependencyResourceProvider.COMPONENT_NAME_ID, "comp_name");
        org.apache.ambari.server.controller.spi.Predicate depServicePredicate = new org.apache.ambari.server.controller.predicate.EqualsPredicate<>(org.apache.ambari.server.controller.internal.StackDependencyResourceProvider.DEPENDENT_SERVICE_NAME_ID, "dep_service_name");
        org.apache.ambari.server.controller.spi.Predicate depCompPredicate = new org.apache.ambari.server.controller.predicate.EqualsPredicate<>(org.apache.ambari.server.controller.internal.StackDependencyResourceProvider.DEPENDENT_COMPONENT_NAME_ID, "dep_comp_name");
        org.apache.ambari.server.controller.spi.Predicate stackNamePredicate = new org.apache.ambari.server.controller.predicate.EqualsPredicate<>(org.apache.ambari.server.controller.internal.StackDependencyResourceProvider.STACK_NAME_ID, "stack_name");
        org.apache.ambari.server.controller.spi.Predicate stackVersionPredicate = new org.apache.ambari.server.controller.predicate.EqualsPredicate<>(org.apache.ambari.server.controller.internal.StackDependencyResourceProvider.STACK_VERSION_ID, "stack_version");
        org.apache.ambari.server.controller.spi.Predicate andPredicate = new org.apache.ambari.server.controller.predicate.AndPredicate(namePredicate, depServicePredicate, depCompPredicate, stackNamePredicate, stackVersionPredicate);
        EasyMock.expect(org.apache.ambari.server.controller.internal.StackDependencyResourceProviderTest.metaInfo.getComponentDependency("stack_name", "stack_version", "dep_service_name", "dep_comp_name", "comp_name")).andReturn(dependencyInfo);
        EasyMock.replay(org.apache.ambari.server.controller.internal.StackDependencyResourceProviderTest.metaInfo, request);
        org.apache.ambari.server.controller.spi.ResourceProvider provider = createProvider();
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = provider.getResources(request, andPredicate);
        EasyMock.verify(org.apache.ambari.server.controller.internal.StackDependencyResourceProviderTest.metaInfo);
        org.junit.Assert.assertEquals(1, resources.size());
        org.apache.ambari.server.controller.spi.Resource resource = resources.iterator().next();
        org.junit.Assert.assertEquals("cluster", resource.getPropertyValue(org.apache.ambari.server.controller.internal.StackDependencyResourceProvider.SCOPE_ID));
        org.junit.Assert.assertEquals("comp_name", resource.getPropertyValue(org.apache.ambari.server.controller.internal.StackDependencyResourceProvider.COMPONENT_NAME_ID));
        org.junit.Assert.assertEquals("service_name", resource.getPropertyValue(org.apache.ambari.server.controller.internal.StackDependencyResourceProvider.SERVICE_NAME_ID));
        org.junit.Assert.assertEquals("dep_service_name", resource.getPropertyValue(org.apache.ambari.server.controller.internal.StackDependencyResourceProvider.DEPENDENT_SERVICE_NAME_ID));
        org.junit.Assert.assertEquals("dep_comp_name", resource.getPropertyValue(org.apache.ambari.server.controller.internal.StackDependencyResourceProvider.DEPENDENT_COMPONENT_NAME_ID));
        org.junit.Assert.assertEquals("stack_name", resource.getPropertyValue(org.apache.ambari.server.controller.internal.StackDependencyResourceProvider.STACK_NAME_ID));
        org.junit.Assert.assertEquals("stack_version", resource.getPropertyValue(org.apache.ambari.server.controller.internal.StackDependencyResourceProvider.STACK_VERSION_ID));
    }

    @org.junit.Test
    public void testGetResources_Query() throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.controller.spi.Request request = EasyMock.createNiceMock(org.apache.ambari.server.controller.spi.Request.class);
        org.apache.ambari.server.state.DependencyInfo dependencyInfo = new org.apache.ambari.server.state.DependencyInfo();
        dependencyInfo.setName("service_name/comp_name");
        dependencyInfo.setScope("cluster");
        org.apache.ambari.server.controller.spi.Predicate namePredicate = new org.apache.ambari.server.controller.predicate.EqualsPredicate<>(org.apache.ambari.server.controller.internal.StackDependencyResourceProvider.COMPONENT_NAME_ID, "comp_name");
        org.apache.ambari.server.controller.spi.Predicate name2Predicate = new org.apache.ambari.server.controller.predicate.EqualsPredicate<>(org.apache.ambari.server.controller.internal.StackDependencyResourceProvider.COMPONENT_NAME_ID, "comp_name2");
        org.apache.ambari.server.controller.spi.Predicate depServicePredicate = new org.apache.ambari.server.controller.predicate.EqualsPredicate<>(org.apache.ambari.server.controller.internal.StackDependencyResourceProvider.DEPENDENT_SERVICE_NAME_ID, "dep_service_name");
        org.apache.ambari.server.controller.spi.Predicate depCompPredicate = new org.apache.ambari.server.controller.predicate.EqualsPredicate<>(org.apache.ambari.server.controller.internal.StackDependencyResourceProvider.DEPENDENT_COMPONENT_NAME_ID, "dep_comp_name");
        org.apache.ambari.server.controller.spi.Predicate stackNamePredicate = new org.apache.ambari.server.controller.predicate.EqualsPredicate<>(org.apache.ambari.server.controller.internal.StackDependencyResourceProvider.STACK_NAME_ID, "stack_name");
        org.apache.ambari.server.controller.spi.Predicate stackVersionPredicate = new org.apache.ambari.server.controller.predicate.EqualsPredicate<>(org.apache.ambari.server.controller.internal.StackDependencyResourceProvider.STACK_VERSION_ID, "stack_version");
        org.apache.ambari.server.controller.spi.Predicate andPredicate1 = new org.apache.ambari.server.controller.predicate.AndPredicate(namePredicate, depServicePredicate, depCompPredicate, stackNamePredicate, stackVersionPredicate);
        org.apache.ambari.server.controller.spi.Predicate andPredicate2 = new org.apache.ambari.server.controller.predicate.AndPredicate(name2Predicate, depServicePredicate, depCompPredicate, stackNamePredicate, stackVersionPredicate);
        org.apache.ambari.server.controller.spi.Predicate orPredicate = new org.apache.ambari.server.controller.predicate.OrPredicate(andPredicate1, andPredicate2);
        EasyMock.expect(org.apache.ambari.server.controller.internal.StackDependencyResourceProviderTest.metaInfo.getComponentDependency("stack_name", "stack_version", "dep_service_name", "dep_comp_name", "comp_name")).andReturn(dependencyInfo);
        EasyMock.expect(org.apache.ambari.server.controller.internal.StackDependencyResourceProviderTest.metaInfo.getComponentDependency("stack_name", "stack_version", "dep_service_name", "dep_comp_name", "comp_name2")).andThrow(new org.apache.ambari.server.StackAccessException("test"));
        EasyMock.replay(org.apache.ambari.server.controller.internal.StackDependencyResourceProviderTest.metaInfo, request);
        org.apache.ambari.server.controller.spi.ResourceProvider provider = createProvider();
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = provider.getResources(request, orPredicate);
        EasyMock.verify(org.apache.ambari.server.controller.internal.StackDependencyResourceProviderTest.metaInfo);
        org.junit.Assert.assertEquals(1, resources.size());
        org.apache.ambari.server.controller.spi.Resource resource = resources.iterator().next();
        org.junit.Assert.assertEquals("cluster", resource.getPropertyValue(org.apache.ambari.server.controller.internal.StackDependencyResourceProvider.SCOPE_ID));
        org.junit.Assert.assertEquals("comp_name", resource.getPropertyValue(org.apache.ambari.server.controller.internal.StackDependencyResourceProvider.COMPONENT_NAME_ID));
        org.junit.Assert.assertEquals("service_name", resource.getPropertyValue(org.apache.ambari.server.controller.internal.StackDependencyResourceProvider.SERVICE_NAME_ID));
        org.junit.Assert.assertEquals("dep_service_name", resource.getPropertyValue(org.apache.ambari.server.controller.internal.StackDependencyResourceProvider.DEPENDENT_SERVICE_NAME_ID));
        org.junit.Assert.assertEquals("dep_comp_name", resource.getPropertyValue(org.apache.ambari.server.controller.internal.StackDependencyResourceProvider.DEPENDENT_COMPONENT_NAME_ID));
        org.junit.Assert.assertEquals("stack_name", resource.getPropertyValue(org.apache.ambari.server.controller.internal.StackDependencyResourceProvider.STACK_NAME_ID));
        org.junit.Assert.assertEquals("stack_version", resource.getPropertyValue(org.apache.ambari.server.controller.internal.StackDependencyResourceProvider.STACK_VERSION_ID));
    }

    private org.apache.ambari.server.controller.internal.StackDependencyResourceProvider createProvider() {
        return new org.apache.ambari.server.controller.internal.StackDependencyResourceProvider();
    }
}