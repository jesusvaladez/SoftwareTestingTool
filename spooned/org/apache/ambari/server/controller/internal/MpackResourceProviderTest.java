package org.apache.ambari.server.controller.internal;
import javax.persistence.EntityManager;
import org.easymock.EasyMock;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
public class MpackResourceProviderTest {
    private org.apache.ambari.server.orm.dao.MpackDAO m_dao;

    private com.google.inject.Injector m_injector;

    private org.apache.ambari.server.controller.AmbariManagementController m_amc;

    @org.junit.Before
    public void before() throws java.lang.Exception {
        m_dao = org.easymock.EasyMock.createNiceMock(org.apache.ambari.server.orm.dao.MpackDAO.class);
        m_amc = org.easymock.EasyMock.createNiceMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        m_injector = com.google.inject.Guice.createInjector(com.google.inject.util.Modules.override(new org.apache.ambari.server.orm.InMemoryDefaultTestModule()).with(new org.apache.ambari.server.controller.internal.MpackResourceProviderTest.MockModule()));
    }

    @org.junit.Test
    public void testGetResourcesMpacks() throws java.lang.Exception {
        org.apache.ambari.server.controller.spi.Resource.Type type = org.apache.ambari.server.controller.spi.Resource.Type.Mpack;
        org.apache.ambari.server.controller.spi.Resource resourceExpected1 = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Mpack);
        resourceExpected1.setProperty(org.apache.ambari.server.controller.internal.MpackResourceProvider.MPACK_RESOURCE_ID, ((long) (1)));
        resourceExpected1.setProperty(org.apache.ambari.server.controller.internal.MpackResourceProvider.MPACK_NAME, "TestMpack1");
        resourceExpected1.setProperty(org.apache.ambari.server.controller.internal.MpackResourceProvider.MPACK_VERSION, "3.0");
        resourceExpected1.setProperty(org.apache.ambari.server.controller.internal.MpackResourceProvider.MPACK_URI, "abcd.tar.gz");
        resourceExpected1.setProperty(org.apache.ambari.server.controller.internal.MpackResourceProvider.REGISTRY_ID, null);
        org.apache.ambari.server.controller.spi.Resource resourceExpected2 = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Mpack);
        resourceExpected2.setProperty(org.apache.ambari.server.controller.internal.MpackResourceProvider.MPACK_RESOURCE_ID, ((long) (2)));
        resourceExpected2.setProperty(org.apache.ambari.server.controller.internal.MpackResourceProvider.MPACK_NAME, "TestMpack2");
        resourceExpected2.setProperty(org.apache.ambari.server.controller.internal.MpackResourceProvider.MPACK_VERSION, "3.0");
        resourceExpected2.setProperty(org.apache.ambari.server.controller.internal.MpackResourceProvider.MPACK_URI, "abc.tar.gz");
        resourceExpected2.setProperty(org.apache.ambari.server.controller.internal.MpackResourceProvider.REGISTRY_ID, ((long) (1)));
        java.util.Set<org.apache.ambari.server.controller.MpackResponse> entities = new java.util.HashSet<>();
        org.apache.ambari.server.state.Mpack entity = new org.apache.ambari.server.state.Mpack();
        entity.setResourceId(new java.lang.Long(1));
        entity.setMpackId("1");
        entity.setMpackUri("abcd.tar.gz");
        entity.setName("TestMpack1");
        entity.setVersion("3.0");
        org.apache.ambari.server.controller.MpackResponse mr1 = new org.apache.ambari.server.controller.MpackResponse(entity);
        entities.add(mr1);
        entity = new org.apache.ambari.server.state.Mpack();
        entity.setResourceId(new java.lang.Long(2));
        entity.setMpackId("2");
        entity.setMpackUri("abc.tar.gz");
        entity.setName("TestMpack2");
        entity.setVersion("3.0");
        entity.setRegistryId(new java.lang.Long(1));
        org.apache.ambari.server.controller.MpackResponse mr2 = new org.apache.ambari.server.controller.MpackResponse(entity);
        entities.add(mr2);
        org.easymock.EasyMock.expect(m_amc.getMpacks()).andReturn(entities).anyTimes();
        EasyMock.replay(m_amc);
        org.apache.ambari.server.controller.spi.ResourceProvider provider = org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.getResourceProvider(type, m_amc);
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest();
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = provider.getResources(request, null);
        org.junit.Assert.assertEquals(2, resources.size());
        for (org.apache.ambari.server.controller.spi.Resource resource : resources) {
            java.lang.Long mpackId = ((java.lang.Long) (resource.getPropertyValue(org.apache.ambari.server.controller.internal.MpackResourceProvider.MPACK_RESOURCE_ID)));
            if (mpackId == ((long) (1))) {
                org.junit.Assert.assertEquals(resourceExpected1.getPropertyValue(org.apache.ambari.server.controller.internal.MpackResourceProvider.MPACK_NAME), ((java.lang.String) (resource.getPropertyValue(org.apache.ambari.server.controller.internal.MpackResourceProvider.MPACK_NAME))));
                org.junit.Assert.assertEquals(resourceExpected1.getPropertyValue(org.apache.ambari.server.controller.internal.MpackResourceProvider.MPACK_VERSION), ((java.lang.String) (resource.getPropertyValue(org.apache.ambari.server.controller.internal.MpackResourceProvider.MPACK_VERSION))));
                org.junit.Assert.assertEquals(resourceExpected1.getPropertyValue(org.apache.ambari.server.controller.internal.MpackResourceProvider.MPACK_URI), ((java.lang.String) (resource.getPropertyValue(org.apache.ambari.server.controller.internal.MpackResourceProvider.MPACK_URI))));
                org.junit.Assert.assertEquals(resourceExpected1.getPropertyValue(org.apache.ambari.server.controller.internal.MpackResourceProvider.REGISTRY_ID), ((java.lang.Long) (resource.getPropertyValue(org.apache.ambari.server.controller.internal.MpackResourceProvider.REGISTRY_ID))));
            } else if (mpackId == ((long) (2))) {
                org.junit.Assert.assertEquals(resourceExpected2.getPropertyValue(org.apache.ambari.server.controller.internal.MpackResourceProvider.MPACK_NAME), ((java.lang.String) (resource.getPropertyValue(org.apache.ambari.server.controller.internal.MpackResourceProvider.MPACK_NAME))));
                org.junit.Assert.assertEquals(resourceExpected2.getPropertyValue(org.apache.ambari.server.controller.internal.MpackResourceProvider.MPACK_VERSION), ((java.lang.String) (resource.getPropertyValue(org.apache.ambari.server.controller.internal.MpackResourceProvider.MPACK_VERSION))));
                org.junit.Assert.assertEquals(resourceExpected2.getPropertyValue(org.apache.ambari.server.controller.internal.MpackResourceProvider.MPACK_URI), ((java.lang.String) (resource.getPropertyValue(org.apache.ambari.server.controller.internal.MpackResourceProvider.MPACK_URI))));
                org.junit.Assert.assertEquals(resourceExpected2.getPropertyValue(org.apache.ambari.server.controller.internal.MpackResourceProvider.REGISTRY_ID), ((java.lang.Long) (resource.getPropertyValue(org.apache.ambari.server.controller.internal.MpackResourceProvider.REGISTRY_ID))));
            } else {
                org.junit.Assert.assertTrue(false);
            }
        }
        EasyMock.verify(m_amc);
    }

    @org.junit.Test
    public void testGetResourcesMpackId() throws java.lang.Exception {
        org.apache.ambari.server.controller.spi.Resource.Type type = org.apache.ambari.server.controller.spi.Resource.Type.Mpack;
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.MpackResourceProvider.MPACK_RESOURCE_ID).equals(java.lang.Long.valueOf(1).toString()).toPredicate();
        org.apache.ambari.server.orm.entities.MpackEntity entity = new org.apache.ambari.server.orm.entities.MpackEntity();
        entity.setId(((long) (1)));
        entity.setMpackUri("abcd.tar.gz");
        entity.setMpackName("TestMpack1");
        entity.setMpackVersion("3.0");
        org.apache.ambari.server.state.Mpack mpack = new org.apache.ambari.server.state.Mpack();
        mpack.setResourceId(((long) (1)));
        mpack.setMpackId("1");
        mpack.setMpackUri("abcd.tar.gz");
        mpack.setName("TestMpack1");
        mpack.setVersion("3.0");
        org.apache.ambari.server.controller.MpackResponse mpackResponse = new org.apache.ambari.server.controller.MpackResponse(mpack);
        java.util.ArrayList<org.apache.ambari.server.state.Module> packletArrayList = new java.util.ArrayList<>();
        org.apache.ambari.server.state.Module module = new org.apache.ambari.server.state.Module();
        module.setName("testService");
        module.setDefinition("testDir");
        module.setVersion("3.0");
        packletArrayList.add(module);
        org.apache.ambari.server.controller.spi.Resource resourceExpected1 = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Mpack);
        resourceExpected1.setProperty(org.apache.ambari.server.controller.internal.MpackResourceProvider.MPACK_RESOURCE_ID, ((long) (1)));
        resourceExpected1.setProperty(org.apache.ambari.server.controller.internal.MpackResourceProvider.MPACK_NAME, "TestMpack1");
        resourceExpected1.setProperty(org.apache.ambari.server.controller.internal.MpackResourceProvider.MPACK_VERSION, "3.0");
        resourceExpected1.setProperty(org.apache.ambari.server.controller.internal.MpackResourceProvider.MPACK_URI, "abcd.tar.gz");
        resourceExpected1.setProperty(org.apache.ambari.server.controller.internal.MpackResourceProvider.REGISTRY_ID, null);
        resourceExpected1.setProperty(org.apache.ambari.server.controller.internal.MpackResourceProvider.MODULES, packletArrayList);
        org.easymock.EasyMock.expect(m_dao.findById(((long) (1)))).andReturn(entity).anyTimes();
        org.easymock.EasyMock.expect(m_amc.getModules(((long) (1)))).andReturn(packletArrayList).anyTimes();
        org.easymock.EasyMock.expect(m_amc.getMpack(((long) (1)))).andReturn(mpackResponse).anyTimes();
        EasyMock.replay(m_dao, m_amc);
        org.apache.ambari.server.controller.spi.ResourceProvider provider = org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.getResourceProvider(org.apache.ambari.server.controller.spi.Resource.Type.Mpack, m_amc);
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest();
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = provider.getResources(request, predicate);
        org.junit.Assert.assertEquals(1, resources.size());
        for (org.apache.ambari.server.controller.spi.Resource resource : resources) {
            org.junit.Assert.assertEquals(resourceExpected1.getPropertyValue(org.apache.ambari.server.controller.internal.MpackResourceProvider.MPACK_NAME), ((java.lang.String) (resource.getPropertyValue(org.apache.ambari.server.controller.internal.MpackResourceProvider.MPACK_NAME))));
            org.junit.Assert.assertEquals(resourceExpected1.getPropertyValue(org.apache.ambari.server.controller.internal.MpackResourceProvider.MPACK_VERSION), ((java.lang.String) (resource.getPropertyValue(org.apache.ambari.server.controller.internal.MpackResourceProvider.MPACK_VERSION))));
            org.junit.Assert.assertEquals(resourceExpected1.getPropertyValue(org.apache.ambari.server.controller.internal.MpackResourceProvider.MPACK_URI), ((java.lang.String) (resource.getPropertyValue(org.apache.ambari.server.controller.internal.MpackResourceProvider.MPACK_URI))));
            org.junit.Assert.assertEquals(resourceExpected1.getPropertyValue(org.apache.ambari.server.controller.internal.MpackResourceProvider.REGISTRY_ID), ((java.lang.Long) (resource.getPropertyValue(org.apache.ambari.server.controller.internal.MpackResourceProvider.REGISTRY_ID))));
            org.junit.Assert.assertEquals(resourceExpected1.getPropertyValue(org.apache.ambari.server.controller.internal.MpackResourceProvider.MODULES), ((java.util.ArrayList) (resource.getPropertyValue(org.apache.ambari.server.controller.internal.MpackResourceProvider.MODULES))));
        }
        EasyMock.verify(m_dao, m_amc);
    }

    @org.junit.Test
    public void testCreateResources() throws java.lang.Exception {
        org.apache.ambari.server.controller.MpackRequest mpackRequest = new org.apache.ambari.server.controller.MpackRequest();
        java.lang.String mpackUri = java.nio.file.Paths.get("src/test/resources/mpacks-v2/abc.tar.gz").toUri().toURL().toString();
        mpackRequest.setMpackUri(mpackUri);
        org.apache.ambari.server.controller.spi.Request request = EasyMock.createMock(org.apache.ambari.server.controller.spi.Request.class);
        org.apache.ambari.server.controller.MpackResponse response = new org.apache.ambari.server.controller.MpackResponse(setupMpack());
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> properties = new java.util.HashSet<>();
        java.util.Map propertyMap = new java.util.HashMap();
        propertyMap.put(org.apache.ambari.server.controller.internal.MpackResourceProvider.MPACK_URI, mpackUri);
        properties.add(propertyMap);
        org.easymock.EasyMock.expect(m_amc.registerMpack(mpackRequest)).andReturn(response).anyTimes();
        org.easymock.EasyMock.expect(request.getProperties()).andReturn(properties).anyTimes();
        EasyMock.replay(m_amc, request);
        org.apache.ambari.server.controller.internal.MpackResourceProvider provider = ((org.apache.ambari.server.controller.internal.MpackResourceProvider) (org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.getResourceProvider(org.apache.ambari.server.controller.spi.Resource.Type.Mpack, m_amc)));
        org.apache.ambari.server.controller.internal.AbstractResourceProviderTest.TestObserver observer = new org.apache.ambari.server.controller.internal.AbstractResourceProviderTest.TestObserver();
        ((org.apache.ambari.server.controller.internal.ObservableResourceProvider) (provider)).addObserver(observer);
        org.apache.ambari.server.controller.internal.RequestStatusImpl requestStatus = ((org.apache.ambari.server.controller.internal.RequestStatusImpl) (provider.createResources(request)));
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> associatedResources = requestStatus.getAssociatedResources();
        org.junit.Assert.assertEquals(1, associatedResources.size());
        for (org.apache.ambari.server.controller.spi.Resource r : associatedResources) {
            org.junit.Assert.assertEquals(((long) (100)), r.getPropertyValue(org.apache.ambari.server.controller.internal.MpackResourceProvider.MPACK_RESOURCE_ID));
            org.junit.Assert.assertEquals("testMpack", r.getPropertyValue(org.apache.ambari.server.controller.internal.MpackResourceProvider.MPACK_NAME));
            org.junit.Assert.assertEquals("3.0", r.getPropertyValue(org.apache.ambari.server.controller.internal.MpackResourceProvider.MPACK_VERSION));
            org.junit.Assert.assertEquals("../../../../../../../resources/mpacks-v2/abc.tar.gz", r.getPropertyValue(org.apache.ambari.server.controller.internal.MpackResourceProvider.MPACK_URI));
        }
        org.apache.ambari.server.controller.internal.ResourceProviderEvent lastEvent = observer.getLastEvent();
        org.junit.Assert.assertNotNull(lastEvent);
        org.junit.Assert.assertEquals(org.apache.ambari.server.controller.spi.Resource.Type.Mpack, lastEvent.getResourceType());
        org.junit.Assert.assertEquals(org.apache.ambari.server.controller.internal.ResourceProviderEvent.Type.Create, lastEvent.getType());
        org.junit.Assert.assertEquals(request, lastEvent.getRequest());
        org.junit.Assert.assertNull(lastEvent.getPredicate());
        EasyMock.verify(m_amc, request);
    }

    public org.apache.ambari.server.state.Mpack setupMpack() {
        org.apache.ambari.server.state.Mpack mpack = new org.apache.ambari.server.state.Mpack();
        mpack.setResourceId(((long) (100)));
        mpack.setModules(new java.util.ArrayList<org.apache.ambari.server.state.Module>());
        mpack.setPrerequisites(new java.util.HashMap<java.lang.String, java.lang.String>());
        mpack.setRegistryId(new java.lang.Long(100));
        mpack.setVersion("3.0");
        mpack.setMpackUri("../../../../../../../resources/mpacks-v2/abc.tar.gz");
        mpack.setDescription("Test mpack");
        mpack.setName("testMpack");
        return mpack;
    }

    private class MockModule implements com.google.inject.Module {
        @java.lang.Override
        public void configure(com.google.inject.Binder binder) {
            binder.bind(javax.persistence.EntityManager.class).toInstance(org.easymock.EasyMock.createMock(javax.persistence.EntityManager.class));
            binder.bind(org.apache.ambari.server.orm.dao.MpackDAO.class).toInstance(m_dao);
            binder.bind(org.apache.ambari.server.controller.AmbariManagementController.class).toInstance(m_amc);
        }
    }
}