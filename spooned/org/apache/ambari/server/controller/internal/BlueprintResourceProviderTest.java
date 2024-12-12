package org.apache.ambari.server.controller.internal;
import org.easymock.EasyMock;
import static org.easymock.EasyMock.anyBoolean;
import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.createStrictMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.reset;
import static org.easymock.EasyMock.verify;
@java.lang.SuppressWarnings("unchecked")
public class BlueprintResourceProviderTest {
    private static java.lang.String BLUEPRINT_NAME = "test-blueprint";

    @org.junit.Rule
    public org.junit.rules.ExpectedException expectedException = org.junit.rules.ExpectedException.none();

    private static final org.apache.ambari.server.orm.dao.BlueprintDAO blueprintDao = EasyMock.createStrictMock(org.apache.ambari.server.orm.dao.BlueprintDAO.class);

    private static final org.apache.ambari.server.orm.dao.TopologyRequestDAO topologyRequestDAO = EasyMock.createMock(org.apache.ambari.server.orm.dao.TopologyRequestDAO.class);

    private static final org.apache.ambari.server.orm.dao.StackDAO stackDAO = EasyMock.createNiceMock(org.apache.ambari.server.orm.dao.StackDAO.class);

    private static final org.apache.ambari.server.orm.entities.BlueprintEntity entity = EasyMock.createStrictMock(org.apache.ambari.server.orm.entities.BlueprintEntity.class);

    private static final org.apache.ambari.server.topology.Blueprint blueprint = EasyMock.createMock(org.apache.ambari.server.topology.Blueprint.class);

    private static final org.apache.ambari.server.api.services.AmbariMetaInfo metaInfo = EasyMock.createMock(org.apache.ambari.server.api.services.AmbariMetaInfo.class);

    private static final org.apache.ambari.server.topology.BlueprintFactory blueprintFactory = EasyMock.createMock(org.apache.ambari.server.topology.BlueprintFactory.class);

    private static final org.apache.ambari.server.topology.SecurityConfigurationFactory securityFactory = EasyMock.createMock(org.apache.ambari.server.topology.SecurityConfigurationFactory.class);

    private static final org.apache.ambari.server.controller.internal.BlueprintResourceProvider provider = org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.createProvider();

    private static final com.google.gson.Gson gson = new com.google.gson.Gson();

    @org.junit.BeforeClass
    public static void initClass() {
        org.apache.ambari.server.controller.internal.BlueprintResourceProvider.init(org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.blueprintFactory, org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.blueprintDao, org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.topologyRequestDAO, org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.securityFactory, org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.gson, org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.metaInfo);
        org.apache.ambari.server.orm.entities.StackEntity stackEntity = new org.apache.ambari.server.orm.entities.StackEntity();
        stackEntity.setStackName("test-stack-name");
        stackEntity.setStackVersion("test-stack-version");
        EasyMock.expect(org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.stackDAO.find(EasyMock.anyObject(java.lang.String.class), EasyMock.anyObject(java.lang.String.class))).andReturn(stackEntity).anyTimes();
        EasyMock.replay(org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.stackDAO);
    }

    private java.util.Map<java.lang.String, java.util.Set<java.util.HashMap<java.lang.String, java.lang.String>>> getSettingProperties() {
        return new java.util.HashMap<>();
    }

    @org.junit.Before
    public void resetGlobalMocks() {
        EasyMock.reset(org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.blueprintDao, org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.topologyRequestDAO, org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.metaInfo, org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.blueprintFactory, org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.securityFactory, org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.blueprint, org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.entity);
    }

    @org.junit.Test
    public void testCreateResources() throws java.lang.Exception {
        org.apache.ambari.server.controller.AmbariManagementController managementController = EasyMock.createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.controller.spi.Request request = EasyMock.createMock(org.apache.ambari.server.controller.spi.Request.class);
        org.apache.ambari.server.topology.Setting setting = EasyMock.createStrictMock(org.apache.ambari.server.topology.Setting.class);
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> setProperties = org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.getBlueprintTestProperties();
        java.util.Map<java.lang.String, java.lang.String> requestInfoProperties = getTestRequestInfoProperties();
        java.util.Map<java.lang.String, java.util.Set<java.util.HashMap<java.lang.String, java.lang.String>>> settingProperties = getSettingProperties();
        EasyMock.expect(org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.blueprintFactory.createBlueprint(setProperties.iterator().next(), null)).andReturn(org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.blueprint).once();
        EasyMock.expect(org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.securityFactory.createSecurityConfigurationFromRequest(null, true)).andReturn(null).anyTimes();
        org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.blueprint.validateRequiredProperties();
        org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.blueprint.validateTopology();
        EasyMock.expect(org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.blueprint.getSetting()).andReturn(setting).anyTimes();
        EasyMock.expect(setting.getProperties()).andReturn(settingProperties).anyTimes();
        EasyMock.expect(org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.blueprint.toEntity()).andReturn(org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.entity);
        EasyMock.expect(org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.blueprint.getName()).andReturn(org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.BLUEPRINT_NAME).atLeastOnce();
        EasyMock.expect(request.getProperties()).andReturn(setProperties);
        EasyMock.expect(request.getRequestInfoProperties()).andReturn(requestInfoProperties);
        EasyMock.expect(org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.blueprintDao.findByName(org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.BLUEPRINT_NAME)).andReturn(null);
        org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.blueprintDao.create(org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.entity);
        EasyMock.replay(org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.blueprintDao, org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.entity, org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.metaInfo, org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.blueprintFactory, org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.securityFactory, org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.blueprint, setting, request, managementController);
        org.apache.ambari.server.controller.spi.ResourceProvider provider = org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.getResourceProvider(org.apache.ambari.server.controller.spi.Resource.Type.Blueprint, managementController);
        org.apache.ambari.server.controller.internal.AbstractResourceProviderTest.TestObserver observer = new org.apache.ambari.server.controller.internal.AbstractResourceProviderTest.TestObserver();
        ((org.apache.ambari.server.controller.internal.ObservableResourceProvider) (provider)).addObserver(observer);
        provider.createResources(request);
        org.apache.ambari.server.controller.internal.ResourceProviderEvent lastEvent = observer.getLastEvent();
        org.junit.Assert.assertNotNull(lastEvent);
        org.junit.Assert.assertEquals(org.apache.ambari.server.controller.spi.Resource.Type.Blueprint, lastEvent.getResourceType());
        org.junit.Assert.assertEquals(org.apache.ambari.server.controller.internal.ResourceProviderEvent.Type.Create, lastEvent.getType());
        org.junit.Assert.assertEquals(request, lastEvent.getRequest());
        org.junit.Assert.assertNull(lastEvent.getPredicate());
        EasyMock.verify(org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.blueprintDao, org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.entity, org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.blueprintFactory, org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.securityFactory, org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.metaInfo, request, managementController);
    }

    @org.junit.Test
    public void testCreateResources_ReqestBodyIsEmpty() throws java.lang.Exception {
        org.apache.ambari.server.controller.AmbariManagementController managementController = EasyMock.createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.controller.spi.Request request = EasyMock.createMock(org.apache.ambari.server.controller.spi.Request.class);
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> setProperties = org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.getBlueprintTestProperties();
        java.util.Map<java.lang.String, java.lang.String> requestInfoProperties = new java.util.HashMap<>();
        requestInfoProperties.put(org.apache.ambari.server.controller.spi.Request.REQUEST_INFO_BODY_PROPERTY, null);
        EasyMock.expect(request.getProperties()).andReturn(setProperties);
        EasyMock.expect(request.getRequestInfoProperties()).andReturn(requestInfoProperties);
        EasyMock.replay(request, managementController);
        org.apache.ambari.server.controller.spi.ResourceProvider provider = org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.getResourceProvider(org.apache.ambari.server.controller.spi.Resource.Type.Blueprint, managementController);
        try {
            provider.createResources(request);
            org.junit.Assert.fail("Exception expected");
        } catch (java.lang.IllegalArgumentException e) {
            org.junit.Assert.assertEquals(org.apache.ambari.server.controller.internal.BlueprintResourceProvider.REQUEST_BODY_EMPTY_ERROR_MESSAGE, e.getMessage());
        }
        EasyMock.verify(request, managementController);
    }

    @org.junit.Test
    public void testCreateResources_NoValidation() throws java.lang.Exception {
        org.apache.ambari.server.controller.AmbariManagementController managementController = EasyMock.createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.controller.spi.Request request = EasyMock.createMock(org.apache.ambari.server.controller.spi.Request.class);
        org.apache.ambari.server.topology.Setting setting = EasyMock.createStrictMock(org.apache.ambari.server.topology.Setting.class);
        java.util.Map<java.lang.String, java.util.Set<java.util.HashMap<java.lang.String, java.lang.String>>> settingProperties = getSettingProperties();
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> setProperties = org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.getBlueprintTestProperties();
        java.util.Map<java.lang.String, java.lang.String> requestInfoProperties = getTestRequestInfoProperties();
        requestInfoProperties.put("validate_topology", "false");
        EasyMock.expect(org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.blueprintFactory.createBlueprint(setProperties.iterator().next(), null)).andReturn(org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.blueprint).once();
        org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.blueprint.validateRequiredProperties();
        EasyMock.expect(org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.blueprint.getSetting()).andReturn(setting).anyTimes();
        EasyMock.expect(setting.getProperties()).andReturn(settingProperties).anyTimes();
        EasyMock.expect(org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.blueprint.toEntity()).andReturn(org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.entity);
        EasyMock.expect(org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.blueprint.getName()).andReturn(org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.BLUEPRINT_NAME).atLeastOnce();
        EasyMock.expect(request.getProperties()).andReturn(setProperties);
        EasyMock.expect(request.getRequestInfoProperties()).andReturn(requestInfoProperties);
        EasyMock.expect(org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.blueprintDao.findByName(org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.BLUEPRINT_NAME)).andReturn(null);
        org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.blueprintDao.create(org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.entity);
        EasyMock.replay(org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.blueprintDao, org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.entity, org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.metaInfo, org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.blueprintFactory, org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.blueprint, setting, request, managementController);
        org.apache.ambari.server.controller.spi.ResourceProvider provider = org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.getResourceProvider(org.apache.ambari.server.controller.spi.Resource.Type.Blueprint, managementController);
        org.apache.ambari.server.controller.internal.AbstractResourceProviderTest.TestObserver observer = new org.apache.ambari.server.controller.internal.AbstractResourceProviderTest.TestObserver();
        ((org.apache.ambari.server.controller.internal.ObservableResourceProvider) (provider)).addObserver(observer);
        provider.createResources(request);
        org.apache.ambari.server.controller.internal.ResourceProviderEvent lastEvent = observer.getLastEvent();
        org.junit.Assert.assertNotNull(lastEvent);
        org.junit.Assert.assertEquals(org.apache.ambari.server.controller.spi.Resource.Type.Blueprint, lastEvent.getResourceType());
        org.junit.Assert.assertEquals(org.apache.ambari.server.controller.internal.ResourceProviderEvent.Type.Create, lastEvent.getType());
        org.junit.Assert.assertEquals(request, lastEvent.getRequest());
        org.junit.Assert.assertNull(lastEvent.getPredicate());
        EasyMock.verify(org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.blueprintDao, org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.entity, org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.blueprintFactory, org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.metaInfo, request, managementController);
    }

    @org.junit.Test
    public void testCreateResources_TopologyValidationFails() throws java.lang.Exception {
        org.apache.ambari.server.controller.spi.Request request = EasyMock.createMock(org.apache.ambari.server.controller.spi.Request.class);
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> setProperties = org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.getBlueprintTestProperties();
        java.util.Map<java.lang.String, java.lang.String> requestInfoProperties = getTestRequestInfoProperties();
        EasyMock.expect(org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.blueprintFactory.createBlueprint(setProperties.iterator().next(), null)).andReturn(org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.blueprint).once();
        org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.blueprint.validateRequiredProperties();
        EasyMock.expect(org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.blueprint.getName()).andReturn(org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.BLUEPRINT_NAME).atLeastOnce();
        org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.blueprint.validateTopology();
        EasyMock.expectLastCall().andThrow(new org.apache.ambari.server.topology.InvalidTopologyException("test"));
        EasyMock.expect(request.getProperties()).andReturn(setProperties);
        EasyMock.expect(request.getRequestInfoProperties()).andReturn(requestInfoProperties);
        EasyMock.expect(org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.blueprintDao.findByName(org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.BLUEPRINT_NAME)).andReturn(null);
        EasyMock.replay(org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.blueprintDao, org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.entity, org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.metaInfo, org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.blueprintFactory, org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.blueprint, request);
        org.apache.ambari.server.controller.spi.ResourceProvider provider = org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.getResourceProvider(org.apache.ambari.server.controller.spi.Resource.Type.Blueprint, EasyMock.createMock(org.apache.ambari.server.controller.AmbariManagementController.class));
        org.apache.ambari.server.controller.internal.AbstractResourceProviderTest.TestObserver observer = new org.apache.ambari.server.controller.internal.AbstractResourceProviderTest.TestObserver();
        ((org.apache.ambari.server.controller.internal.ObservableResourceProvider) (provider)).addObserver(observer);
        try {
            provider.createResources(request);
            org.junit.Assert.fail("Expected exception due to topology validation error");
        } catch (java.lang.IllegalArgumentException e) {
        }
        EasyMock.verify(org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.blueprintDao, org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.entity, org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.blueprintFactory, org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.metaInfo, request);
    }

    @org.junit.Test
    public void testCreateResources_withConfiguration() throws java.lang.Exception {
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> setProperties = org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.getBlueprintTestProperties();
        org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.setConfigurationProperties(setProperties);
        org.apache.ambari.server.controller.AmbariManagementController managementController = EasyMock.createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        java.util.Map<java.lang.String, java.lang.String> requestInfoProperties = getTestRequestInfoProperties();
        org.apache.ambari.server.controller.spi.Request request = EasyMock.createMock(org.apache.ambari.server.controller.spi.Request.class);
        org.apache.ambari.server.topology.Setting setting = EasyMock.createStrictMock(org.apache.ambari.server.topology.Setting.class);
        java.util.Map<java.lang.String, java.util.Set<java.util.HashMap<java.lang.String, java.lang.String>>> settingProperties = getSettingProperties();
        EasyMock.expect(org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.blueprintFactory.createBlueprint(setProperties.iterator().next(), null)).andReturn(org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.blueprint).once();
        org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.blueprint.validateRequiredProperties();
        org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.blueprint.validateTopology();
        EasyMock.expect(org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.blueprint.getSetting()).andReturn(setting).anyTimes();
        EasyMock.expect(setting.getProperties()).andReturn(settingProperties).anyTimes();
        EasyMock.expect(org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.blueprint.toEntity()).andReturn(org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.entity);
        EasyMock.expect(org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.blueprint.getName()).andReturn(org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.BLUEPRINT_NAME).atLeastOnce();
        EasyMock.expect(request.getProperties()).andReturn(setProperties);
        EasyMock.expect(request.getRequestInfoProperties()).andReturn(requestInfoProperties);
        EasyMock.expect(org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.blueprintDao.findByName(org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.BLUEPRINT_NAME)).andReturn(null);
        org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.blueprintDao.create(org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.entity);
        EasyMock.replay(org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.blueprintDao, org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.entity, org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.metaInfo, org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.blueprintFactory, org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.blueprint, setting, request, managementController);
        org.apache.ambari.server.controller.spi.ResourceProvider provider = org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.getResourceProvider(org.apache.ambari.server.controller.spi.Resource.Type.Blueprint, managementController);
        org.apache.ambari.server.controller.internal.AbstractResourceProviderTest.TestObserver observer = new org.apache.ambari.server.controller.internal.AbstractResourceProviderTest.TestObserver();
        ((org.apache.ambari.server.controller.internal.ObservableResourceProvider) (provider)).addObserver(observer);
        provider.createResources(request);
        org.apache.ambari.server.controller.internal.ResourceProviderEvent lastEvent = observer.getLastEvent();
        org.junit.Assert.assertNotNull(lastEvent);
        org.junit.Assert.assertEquals(org.apache.ambari.server.controller.spi.Resource.Type.Blueprint, lastEvent.getResourceType());
        org.junit.Assert.assertEquals(org.apache.ambari.server.controller.internal.ResourceProviderEvent.Type.Create, lastEvent.getType());
        org.junit.Assert.assertEquals(request, lastEvent.getRequest());
        org.junit.Assert.assertNull(lastEvent.getPredicate());
        EasyMock.verify(org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.blueprintDao, org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.entity, org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.blueprintFactory, org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.metaInfo, request, managementController);
    }

    @org.junit.Test
    public void testCreateResource_BlueprintFactoryThrowsException() throws java.lang.Exception {
        org.apache.ambari.server.controller.spi.Request request = EasyMock.createMock(org.apache.ambari.server.controller.spi.Request.class);
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> setProperties = org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.getBlueprintTestProperties();
        setProperties.iterator().next().remove(org.apache.ambari.server.controller.internal.BlueprintResourceProvider.HOST_GROUP_PROPERTY_ID);
        java.util.Map<java.lang.String, java.lang.String> requestInfoProperties = getTestRequestInfoProperties();
        EasyMock.expect(org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.blueprintFactory.createBlueprint(setProperties.iterator().next(), null)).andThrow(new java.lang.IllegalArgumentException("Blueprint name must be provided"));
        EasyMock.expect(org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.securityFactory.createSecurityConfigurationFromRequest(null, true)).andReturn(null).anyTimes();
        EasyMock.expect(request.getProperties()).andReturn(setProperties);
        EasyMock.expect(request.getRequestInfoProperties()).andReturn(requestInfoProperties);
        EasyMock.replay(org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.blueprintDao, org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.entity, org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.metaInfo, org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.blueprintFactory, org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.securityFactory, org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.blueprint, request);
        try {
            org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.provider.createResources(request);
            org.junit.Assert.fail("Exception expected");
        } catch (java.lang.IllegalArgumentException e) {
        }
        EasyMock.verify(org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.blueprintDao, org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.entity, org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.blueprintFactory, org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.metaInfo, request);
    }

    @org.junit.Test
    public void testCreateResources_withSecurityConfiguration() throws java.lang.Exception {
        org.apache.ambari.server.controller.AmbariManagementController managementController = EasyMock.createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.controller.spi.Request request = EasyMock.createMock(org.apache.ambari.server.controller.spi.Request.class);
        org.apache.ambari.server.topology.Setting setting = EasyMock.createStrictMock(org.apache.ambari.server.topology.Setting.class);
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> setProperties = org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.getBlueprintTestProperties();
        java.util.Map<java.lang.String, java.lang.String> requestInfoProperties = getTestRequestInfoProperties();
        java.util.Map<java.lang.String, java.util.Set<java.util.HashMap<java.lang.String, java.lang.String>>> settingProperties = getSettingProperties();
        org.apache.ambari.server.topology.SecurityConfiguration securityConfiguration = org.apache.ambari.server.topology.SecurityConfiguration.withReference("testRef");
        EasyMock.expect(org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.securityFactory.createSecurityConfigurationFromRequest(org.easymock.EasyMock.anyObject(), EasyMock.anyBoolean())).andReturn(securityConfiguration).once();
        EasyMock.expect(org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.blueprintFactory.createBlueprint(setProperties.iterator().next(), securityConfiguration)).andReturn(org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.blueprint).once();
        org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.blueprint.validateRequiredProperties();
        org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.blueprint.validateTopology();
        EasyMock.expect(org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.blueprint.getSetting()).andReturn(setting).anyTimes();
        EasyMock.expect(setting.getProperties()).andReturn(settingProperties).anyTimes();
        EasyMock.expect(org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.blueprint.toEntity()).andReturn(org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.entity);
        EasyMock.expect(org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.blueprint.getName()).andReturn(org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.BLUEPRINT_NAME).atLeastOnce();
        EasyMock.expect(request.getProperties()).andReturn(setProperties);
        EasyMock.expect(request.getRequestInfoProperties()).andReturn(requestInfoProperties);
        EasyMock.expect(org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.blueprintDao.findByName(org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.BLUEPRINT_NAME)).andReturn(null);
        org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.blueprintDao.create(org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.entity);
        EasyMock.replay(org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.blueprintDao, org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.entity, org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.metaInfo, org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.blueprintFactory, org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.securityFactory, org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.blueprint, setting, request, managementController);
        org.apache.ambari.server.controller.spi.ResourceProvider provider = org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.getResourceProvider(org.apache.ambari.server.controller.spi.Resource.Type.Blueprint, managementController);
        org.apache.ambari.server.controller.internal.AbstractResourceProviderTest.TestObserver observer = new org.apache.ambari.server.controller.internal.AbstractResourceProviderTest.TestObserver();
        ((org.apache.ambari.server.controller.internal.ObservableResourceProvider) (provider)).addObserver(observer);
        provider.createResources(request);
        org.apache.ambari.server.controller.internal.ResourceProviderEvent lastEvent = observer.getLastEvent();
        org.junit.Assert.assertNotNull(lastEvent);
        org.junit.Assert.assertEquals(org.apache.ambari.server.controller.spi.Resource.Type.Blueprint, lastEvent.getResourceType());
        org.junit.Assert.assertEquals(org.apache.ambari.server.controller.internal.ResourceProviderEvent.Type.Create, lastEvent.getType());
        org.junit.Assert.assertEquals(request, lastEvent.getRequest());
        org.junit.Assert.assertNull(lastEvent.getPredicate());
        EasyMock.verify(org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.blueprintDao, org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.entity, org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.blueprintFactory, org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.metaInfo, request, managementController);
    }

    @org.junit.Test
    public void testGetResourcesNoPredicate() throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException, org.apache.ambari.server.controller.spi.NoSuchResourceException {
        org.apache.ambari.server.controller.spi.Request request = EasyMock.createNiceMock(org.apache.ambari.server.controller.spi.Request.class);
        org.apache.ambari.server.orm.entities.BlueprintEntity entity = createEntity(org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.getBlueprintTestProperties().iterator().next());
        java.util.List<org.apache.ambari.server.orm.entities.BlueprintEntity> results = new java.util.ArrayList<>();
        results.add(entity);
        EasyMock.expect(org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.blueprintDao.findAll()).andReturn(results);
        EasyMock.replay(org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.blueprintDao, request);
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> setResults = org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.provider.getResources(request, null);
        org.junit.Assert.assertEquals(1, setResults.size());
        EasyMock.verify(org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.blueprintDao);
        validateResource(setResults.iterator().next(), false);
    }

    @org.junit.Test
    public void testGetResourcesNoPredicate_withConfiguration() throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.StackInfo info = EasyMock.createMock(org.apache.ambari.server.state.StackInfo.class);
        EasyMock.expect(info.getConfigPropertiesTypes("core-site")).andReturn(new java.util.HashMap<>()).anyTimes();
        EasyMock.expect(org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.metaInfo.getStack("test-stack-name", "test-stack-version")).andReturn(info).anyTimes();
        EasyMock.replay(info, org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.metaInfo);
        org.apache.ambari.server.controller.spi.Request request = EasyMock.createNiceMock(org.apache.ambari.server.controller.spi.Request.class);
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> testProperties = org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.getBlueprintTestProperties();
        org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.setConfigurationProperties(testProperties);
        org.apache.ambari.server.orm.entities.BlueprintEntity entity = createEntity(testProperties.iterator().next());
        java.util.List<org.apache.ambari.server.orm.entities.BlueprintEntity> results = new java.util.ArrayList<>();
        results.add(entity);
        EasyMock.expect(org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.blueprintDao.findAll()).andReturn(results);
        EasyMock.replay(org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.blueprintDao, request);
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> setResults = org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.provider.getResources(request, null);
        org.junit.Assert.assertEquals(1, setResults.size());
        EasyMock.verify(org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.blueprintDao);
        validateResource(setResults.iterator().next(), true);
    }

    @org.junit.Test
    public void testDeleteResources() throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException, org.apache.ambari.server.controller.spi.NoSuchResourceException {
        org.apache.ambari.server.orm.entities.BlueprintEntity blueprintEntity = createEntity(org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.getBlueprintTestProperties().iterator().next());
        EasyMock.expect(org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.blueprintDao.findByName(org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.BLUEPRINT_NAME)).andReturn(blueprintEntity);
        org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.blueprintDao.removeByName(blueprintEntity.getBlueprintName());
        EasyMock.expectLastCall();
        EasyMock.expect(org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.topologyRequestDAO.findAllProvisionRequests()).andReturn(com.google.common.collect.ImmutableList.of());
        EasyMock.replay(org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.blueprintDao, org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.topologyRequestDAO);
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.predicate.EqualsPredicate<>(org.apache.ambari.server.controller.internal.BlueprintResourceProvider.BLUEPRINT_NAME_PROPERTY_ID, org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.BLUEPRINT_NAME);
        org.apache.ambari.server.controller.internal.AbstractResourceProviderTest.TestObserver observer = new org.apache.ambari.server.controller.internal.AbstractResourceProviderTest.TestObserver();
        org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.provider.addObserver(observer);
        org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.provider.deleteResources(new org.apache.ambari.server.controller.internal.RequestImpl(null, null, null, null), predicate);
        org.apache.ambari.server.controller.internal.ResourceProviderEvent lastEvent = observer.getLastEvent();
        org.junit.Assert.assertNotNull(lastEvent);
        org.junit.Assert.assertEquals(org.apache.ambari.server.controller.spi.Resource.Type.Blueprint, lastEvent.getResourceType());
        org.junit.Assert.assertEquals(org.apache.ambari.server.controller.internal.ResourceProviderEvent.Type.Delete, lastEvent.getType());
        org.junit.Assert.assertNotNull(lastEvent.getPredicate());
        EasyMock.verify(org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.blueprintDao);
    }

    @org.junit.Test(expected = java.lang.IllegalArgumentException.class)
    public void testDeleteResources_clusterAlreadyProvisioned() throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException, org.apache.ambari.server.controller.spi.NoSuchResourceException {
        org.apache.ambari.server.orm.entities.BlueprintEntity blueprintEntity = createEntity(org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.getBlueprintTestProperties().iterator().next());
        EasyMock.expect(org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.blueprintDao.findByName(org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.BLUEPRINT_NAME)).andReturn(blueprintEntity);
        org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.blueprintDao.removeByName(blueprintEntity.getBlueprintName());
        EasyMock.expectLastCall();
        org.apache.ambari.server.orm.entities.TopologyRequestEntity topologyRequestEntity = new org.apache.ambari.server.orm.entities.TopologyRequestEntity();
        topologyRequestEntity.setBlueprintName(org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.BLUEPRINT_NAME);
        EasyMock.expect(org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.topologyRequestDAO.findAllProvisionRequests()).andReturn(com.google.common.collect.ImmutableList.of(topologyRequestEntity));
        EasyMock.replay(org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.blueprintDao, org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.topologyRequestDAO);
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.predicate.EqualsPredicate<>(org.apache.ambari.server.controller.internal.BlueprintResourceProvider.BLUEPRINT_NAME_PROPERTY_ID, org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.BLUEPRINT_NAME);
        org.apache.ambari.server.controller.internal.AbstractResourceProviderTest.TestObserver observer = new org.apache.ambari.server.controller.internal.AbstractResourceProviderTest.TestObserver();
        org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.provider.addObserver(observer);
        org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.provider.deleteResources(new org.apache.ambari.server.controller.internal.RequestImpl(null, null, null, null), predicate);
    }

    @org.junit.Test
    public void testCreateResources_withEmptyConfiguration() throws java.lang.Exception {
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> setProperties = org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.getBlueprintTestProperties();
        org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.setConfigurationProperties(setProperties);
        org.apache.ambari.server.controller.AmbariManagementController managementController = EasyMock.createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        java.util.Map<java.lang.String, java.lang.String> requestInfoProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.util.Set<java.util.HashMap<java.lang.String, java.lang.String>>> settingProperties = getSettingProperties();
        requestInfoProperties.put(org.apache.ambari.server.controller.spi.Request.REQUEST_INFO_BODY_PROPERTY, "{\"configurations\":[]}");
        org.apache.ambari.server.controller.spi.Request request = EasyMock.createMock(org.apache.ambari.server.controller.spi.Request.class);
        org.apache.ambari.server.topology.Setting setting = EasyMock.createStrictMock(org.apache.ambari.server.topology.Setting.class);
        EasyMock.expect(org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.blueprintFactory.createBlueprint(setProperties.iterator().next(), null)).andReturn(org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.blueprint).once();
        org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.blueprint.validateRequiredProperties();
        org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.blueprint.validateTopology();
        EasyMock.expect(org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.blueprint.getSetting()).andReturn(setting).anyTimes();
        EasyMock.expect(setting.getProperties()).andReturn(settingProperties).anyTimes();
        EasyMock.expect(org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.blueprint.toEntity()).andReturn(org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.entity);
        EasyMock.expect(org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.blueprint.getName()).andReturn(org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.BLUEPRINT_NAME).atLeastOnce();
        EasyMock.expect(request.getProperties()).andReturn(setProperties);
        EasyMock.expect(request.getRequestInfoProperties()).andReturn(requestInfoProperties);
        EasyMock.expect(org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.blueprintDao.findByName(org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.BLUEPRINT_NAME)).andReturn(null);
        org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.blueprintDao.create(org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.entity);
        EasyMock.replay(org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.blueprintDao, org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.entity, org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.metaInfo, org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.blueprintFactory, org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.blueprint, setting, request, managementController);
        org.apache.ambari.server.controller.spi.ResourceProvider provider = org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.getResourceProvider(org.apache.ambari.server.controller.spi.Resource.Type.Blueprint, managementController);
        org.apache.ambari.server.controller.internal.AbstractResourceProviderTest.TestObserver observer = new org.apache.ambari.server.controller.internal.AbstractResourceProviderTest.TestObserver();
        ((org.apache.ambari.server.controller.internal.ObservableResourceProvider) (provider)).addObserver(observer);
        provider.createResources(request);
        org.apache.ambari.server.controller.internal.ResourceProviderEvent lastEvent = observer.getLastEvent();
        org.junit.Assert.assertNotNull(lastEvent);
        org.junit.Assert.assertEquals(org.apache.ambari.server.controller.spi.Resource.Type.Blueprint, lastEvent.getResourceType());
        org.junit.Assert.assertEquals(org.apache.ambari.server.controller.internal.ResourceProviderEvent.Type.Create, lastEvent.getType());
        org.junit.Assert.assertEquals(request, lastEvent.getRequest());
        org.junit.Assert.assertNull(lastEvent.getPredicate());
        EasyMock.verify(org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.blueprintDao, org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.entity, org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.blueprintFactory, org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.metaInfo, request, managementController);
    }

    @org.junit.Test
    public void testCreateResources_withSingleConfigurationType() throws java.lang.Exception {
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> setProperties = org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.getBlueprintTestProperties();
        org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.setConfigurationProperties(setProperties);
        org.apache.ambari.server.controller.AmbariManagementController managementController = EasyMock.createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        java.util.Map<java.lang.String, java.lang.String> requestInfoProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.util.Set<java.util.HashMap<java.lang.String, java.lang.String>>> settingProperties = getSettingProperties();
        requestInfoProperties.put(org.apache.ambari.server.controller.spi.Request.REQUEST_INFO_BODY_PROPERTY, "{\"configurations\":[{\"configuration-type\":{\"properties\":{\"property\":\"value\"}}}]}");
        org.apache.ambari.server.controller.spi.Request request = EasyMock.createMock(org.apache.ambari.server.controller.spi.Request.class);
        org.apache.ambari.server.topology.Setting setting = EasyMock.createStrictMock(org.apache.ambari.server.topology.Setting.class);
        EasyMock.expect(org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.blueprintFactory.createBlueprint(setProperties.iterator().next(), null)).andReturn(org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.blueprint).once();
        org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.blueprint.validateRequiredProperties();
        org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.blueprint.validateTopology();
        EasyMock.expect(org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.blueprint.getSetting()).andReturn(setting).anyTimes();
        EasyMock.expect(setting.getProperties()).andReturn(settingProperties).anyTimes();
        EasyMock.expect(org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.blueprint.toEntity()).andReturn(org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.entity);
        EasyMock.expect(org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.blueprint.getName()).andReturn(org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.BLUEPRINT_NAME).atLeastOnce();
        EasyMock.expect(request.getProperties()).andReturn(setProperties);
        EasyMock.expect(request.getRequestInfoProperties()).andReturn(requestInfoProperties);
        EasyMock.expect(org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.blueprintDao.findByName(org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.BLUEPRINT_NAME)).andReturn(null);
        org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.blueprintDao.create(org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.entity);
        EasyMock.replay(org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.blueprintDao, org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.entity, org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.metaInfo, org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.blueprintFactory, org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.blueprint, setting, request, managementController);
        org.apache.ambari.server.controller.spi.ResourceProvider provider = org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.getResourceProvider(org.apache.ambari.server.controller.spi.Resource.Type.Blueprint, managementController);
        org.apache.ambari.server.controller.internal.AbstractResourceProviderTest.TestObserver observer = new org.apache.ambari.server.controller.internal.AbstractResourceProviderTest.TestObserver();
        ((org.apache.ambari.server.controller.internal.ObservableResourceProvider) (provider)).addObserver(observer);
        provider.createResources(request);
        org.apache.ambari.server.controller.internal.ResourceProviderEvent lastEvent = observer.getLastEvent();
        org.junit.Assert.assertNotNull(lastEvent);
        org.junit.Assert.assertEquals(org.apache.ambari.server.controller.spi.Resource.Type.Blueprint, lastEvent.getResourceType());
        org.junit.Assert.assertEquals(org.apache.ambari.server.controller.internal.ResourceProviderEvent.Type.Create, lastEvent.getType());
        org.junit.Assert.assertEquals(request, lastEvent.getRequest());
        org.junit.Assert.assertNull(lastEvent.getPredicate());
        EasyMock.verify(org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.blueprintDao, org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.entity, org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.blueprintFactory, org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.metaInfo, request, managementController);
    }

    @org.junit.Test
    public void testCreateResources_wrongConfigurationsStructure_withWrongConfigMapSize() throws org.apache.ambari.server.controller.spi.ResourceAlreadyExistsException, org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        org.apache.ambari.server.controller.spi.Request request = EasyMock.createMock(org.apache.ambari.server.controller.spi.Request.class);
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> setProperties = org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.getBlueprintTestProperties();
        java.util.Map<java.lang.String, java.lang.String> requestInfoProperties = new java.util.HashMap<>();
        java.lang.String configurationData = "{\"configurations\":[{\"config-type1\":{\"properties\" :{\"property\":\"property-value\"}}," + ("\"config-type2\" : {\"properties_attributes\" : {\"property\" : \"property-value\"}, \"properties\" : {\"property\" : \"property-value\"}}}" + "]}");
        requestInfoProperties.put(org.apache.ambari.server.controller.spi.Request.REQUEST_INFO_BODY_PROPERTY, configurationData);
        EasyMock.expect(request.getProperties()).andReturn(setProperties);
        EasyMock.expect(request.getRequestInfoProperties()).andReturn(requestInfoProperties);
        EasyMock.replay(org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.blueprintDao, org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.metaInfo, request);
        try {
            org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.provider.createResources(request);
            org.junit.Assert.fail("Exception expected");
        } catch (java.lang.IllegalArgumentException e) {
            org.junit.Assert.assertEquals(org.apache.ambari.server.controller.internal.BlueprintResourceProvider.CONFIGURATION_MAP_SIZE_CHECK_ERROR_MESSAGE, e.getMessage());
        }
        EasyMock.verify(org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.blueprintDao, org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.metaInfo, request);
    }

    @org.junit.Test
    public void testCreateResources_wrongConfigurationStructure_withoutConfigMaps() throws org.apache.ambari.server.controller.spi.ResourceAlreadyExistsException, org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        org.apache.ambari.server.controller.spi.Request request = EasyMock.createMock(org.apache.ambari.server.controller.spi.Request.class);
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> setProperties = org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.getBlueprintTestProperties();
        java.util.Map<java.lang.String, java.lang.String> requestInfoProperties = new java.util.HashMap<>();
        java.lang.String configurationData = "{\"configurations\":[\"config-type1\", \"config-type2\"]}";
        requestInfoProperties.put(org.apache.ambari.server.controller.spi.Request.REQUEST_INFO_BODY_PROPERTY, configurationData);
        EasyMock.expect(request.getProperties()).andReturn(setProperties);
        EasyMock.expect(request.getRequestInfoProperties()).andReturn(requestInfoProperties);
        EasyMock.replay(org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.blueprintDao, org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.metaInfo, request);
        try {
            org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.provider.createResources(request);
            org.junit.Assert.fail("Exception expected");
        } catch (java.lang.IllegalArgumentException e) {
            org.junit.Assert.assertEquals(org.apache.ambari.server.controller.internal.BlueprintResourceProvider.CONFIGURATION_MAP_CHECK_ERROR_MESSAGE, e.getMessage());
        }
        EasyMock.verify(org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.blueprintDao, org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.metaInfo, request);
    }

    @org.junit.Test
    public void testCreateResources_wrongConfigurationStructure_withoutConfigsList() throws org.apache.ambari.server.controller.spi.ResourceAlreadyExistsException, org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        org.apache.ambari.server.controller.spi.Request request = EasyMock.createMock(org.apache.ambari.server.controller.spi.Request.class);
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> setProperties = org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.getBlueprintTestProperties();
        java.util.Map<java.lang.String, java.lang.String> requestInfoProperties = new java.util.HashMap<>();
        java.lang.String configurationData = "{\"configurations\":{\"config-type1\": \"properties\", \"config-type2\": \"properties\"}}";
        requestInfoProperties.put(org.apache.ambari.server.controller.spi.Request.REQUEST_INFO_BODY_PROPERTY, configurationData);
        EasyMock.expect(request.getProperties()).andReturn(setProperties);
        EasyMock.expect(request.getRequestInfoProperties()).andReturn(requestInfoProperties);
        EasyMock.replay(org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.blueprintDao, org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.metaInfo, request);
        try {
            org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.provider.createResources(request);
            org.junit.Assert.fail("Exception expected");
        } catch (java.lang.IllegalArgumentException e) {
            org.junit.Assert.assertEquals(org.apache.ambari.server.controller.internal.BlueprintResourceProvider.CONFIGURATION_LIST_CHECK_ERROR_MESSAGE, e.getMessage());
        }
        EasyMock.verify(org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.blueprintDao, org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.metaInfo, request);
    }

    public static java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> getBlueprintTestProperties() {
        java.util.Map<java.lang.String, java.lang.String> mapHostGroupComponentProperties = new java.util.HashMap<>();
        mapHostGroupComponentProperties.put(org.apache.ambari.server.controller.internal.BlueprintResourceProvider.COMPONENT_NAME_PROPERTY_ID, "component1");
        java.util.Map<java.lang.String, java.lang.String> mapHostGroupComponentProperties2 = new java.util.HashMap<>();
        mapHostGroupComponentProperties2.put(org.apache.ambari.server.controller.internal.BlueprintResourceProvider.COMPONENT_NAME_PROPERTY_ID, "component2");
        java.util.Set<java.util.Map<java.lang.String, java.lang.String>> setComponentProperties = new java.util.HashSet<>();
        setComponentProperties.add(mapHostGroupComponentProperties);
        setComponentProperties.add(mapHostGroupComponentProperties2);
        java.util.Set<java.util.Map<java.lang.String, java.lang.String>> setComponentProperties2 = new java.util.HashSet<>();
        setComponentProperties2.add(mapHostGroupComponentProperties);
        java.util.Map<java.lang.String, java.lang.Object> mapHostGroupProperties = new java.util.HashMap<>();
        mapHostGroupProperties.put(org.apache.ambari.server.controller.internal.BlueprintResourceProvider.HOST_GROUP_NAME_PROPERTY_ID, "group1");
        mapHostGroupProperties.put(org.apache.ambari.server.controller.internal.BlueprintResourceProvider.HOST_GROUP_CARDINALITY_PROPERTY_ID, "1");
        mapHostGroupProperties.put(org.apache.ambari.server.controller.internal.BlueprintResourceProvider.COMPONENT_PROPERTY_ID, setComponentProperties);
        java.util.Map<java.lang.String, java.lang.Object> mapHostGroupProperties2 = new java.util.HashMap<>();
        mapHostGroupProperties2.put(org.apache.ambari.server.controller.internal.BlueprintResourceProvider.HOST_GROUP_NAME_PROPERTY_ID, "group2");
        mapHostGroupProperties2.put(org.apache.ambari.server.controller.internal.BlueprintResourceProvider.HOST_GROUP_CARDINALITY_PROPERTY_ID, "2");
        mapHostGroupProperties2.put(org.apache.ambari.server.controller.internal.BlueprintResourceProvider.COMPONENT_PROPERTY_ID, setComponentProperties2);
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> setHostGroupProperties = new java.util.HashSet<>();
        setHostGroupProperties.add(mapHostGroupProperties);
        setHostGroupProperties.add(mapHostGroupProperties2);
        java.util.Map<java.lang.String, java.lang.Object> mapProperties = new java.util.HashMap<>();
        mapProperties.put(org.apache.ambari.server.controller.internal.BlueprintResourceProvider.BLUEPRINT_NAME_PROPERTY_ID, org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.BLUEPRINT_NAME);
        mapProperties.put(org.apache.ambari.server.controller.internal.BlueprintResourceProvider.STACK_NAME_PROPERTY_ID, "test-stack-name");
        mapProperties.put(org.apache.ambari.server.controller.internal.BlueprintResourceProvider.STACK_VERSION_PROPERTY_ID, "test-stack-version");
        mapProperties.put(org.apache.ambari.server.controller.internal.BlueprintResourceProvider.HOST_GROUP_PROPERTY_ID, setHostGroupProperties);
        return java.util.Collections.singleton(mapProperties);
    }

    public static java.util.Map<java.lang.String, java.lang.Object> getBlueprintRawBodyProperties() {
        return new java.util.HashMap<>();
    }

    public static void setConfigurationProperties(java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> properties) {
        java.util.Map<java.lang.String, java.lang.String> clusterProperties = new java.util.HashMap<>();
        clusterProperties.put("core-site/properties/fs.trash.interval", "480");
        clusterProperties.put("core-site/properties/ipc.client.idlethreshold", "8500");
        clusterProperties.put("core-site/properties_attributes/final/ipc.client.idlethreshold", "true");
        java.util.Map<java.lang.String, java.lang.Object> mapProperties = properties.iterator().next();
        java.util.Set<java.util.Map<java.lang.String, java.lang.String>> configurations = new java.util.HashSet<>();
        configurations.add(clusterProperties);
        mapProperties.put("configurations", configurations);
        java.util.Map<java.lang.String, java.lang.Object> hostGroupProperties = new java.util.HashMap<>();
        hostGroupProperties.put("core-site/my.custom.hg.property", "anything");
        java.util.Collection<java.util.Map<java.lang.String, java.lang.Object>> hostGroups = ((java.util.Collection<java.util.Map<java.lang.String, java.lang.Object>>) (mapProperties.get(org.apache.ambari.server.controller.internal.BlueprintResourceProvider.HOST_GROUP_PROPERTY_ID)));
        for (java.util.Map<java.lang.String, java.lang.Object> hostGroupProps : hostGroups) {
            if (hostGroupProps.get(org.apache.ambari.server.controller.internal.BlueprintResourceProvider.HOST_GROUP_NAME_PROPERTY_ID).equals("group2")) {
                hostGroupProps.put("configurations", java.util.Collections.singleton(hostGroupProperties));
                break;
            }
        }
    }

    private void validateResource(org.apache.ambari.server.controller.spi.Resource resource, boolean containsConfig) {
        org.junit.Assert.assertEquals(org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.BLUEPRINT_NAME, resource.getPropertyValue(org.apache.ambari.server.controller.internal.BlueprintResourceProvider.BLUEPRINT_NAME_PROPERTY_ID));
        org.junit.Assert.assertEquals("test-stack-name", resource.getPropertyValue(org.apache.ambari.server.controller.internal.BlueprintResourceProvider.STACK_NAME_PROPERTY_ID));
        org.junit.Assert.assertEquals("test-stack-version", resource.getPropertyValue(org.apache.ambari.server.controller.internal.BlueprintResourceProvider.STACK_VERSION_PROPERTY_ID));
        java.util.Collection<java.util.Map<java.lang.String, java.lang.Object>> hostGroupProperties = ((java.util.Collection<java.util.Map<java.lang.String, java.lang.Object>>) (resource.getPropertyValue(org.apache.ambari.server.controller.internal.BlueprintResourceProvider.HOST_GROUP_PROPERTY_ID)));
        org.junit.Assert.assertEquals(2, hostGroupProperties.size());
        for (java.util.Map<java.lang.String, java.lang.Object> hostGroupProps : hostGroupProperties) {
            java.lang.String name = ((java.lang.String) (hostGroupProps.get(org.apache.ambari.server.controller.internal.BlueprintResourceProvider.HOST_GROUP_NAME_PROPERTY_ID)));
            org.junit.Assert.assertTrue(name.equals("group1") || name.equals("group2"));
            java.util.List<java.util.Map<java.lang.String, java.lang.String>> listComponents = ((java.util.List<java.util.Map<java.lang.String, java.lang.String>>) (hostGroupProps.get(org.apache.ambari.server.controller.internal.BlueprintResourceProvider.COMPONENT_PROPERTY_ID)));
            if (name.equals("group1")) {
                org.junit.Assert.assertEquals("1", hostGroupProps.get(org.apache.ambari.server.controller.internal.BlueprintResourceProvider.HOST_GROUP_CARDINALITY_PROPERTY_ID));
                org.junit.Assert.assertEquals(2, listComponents.size());
                java.util.Map<java.lang.String, java.lang.String> mapComponent = listComponents.get(0);
                java.lang.String componentName = mapComponent.get(org.apache.ambari.server.controller.internal.BlueprintResourceProvider.COMPONENT_NAME_PROPERTY_ID);
                org.junit.Assert.assertTrue(componentName.equals("component1") || componentName.equals("component2"));
                mapComponent = listComponents.get(1);
                java.lang.String componentName2 = mapComponent.get(org.apache.ambari.server.controller.internal.BlueprintResourceProvider.COMPONENT_NAME_PROPERTY_ID);
                org.junit.Assert.assertFalse(componentName2.equals(componentName));
                org.junit.Assert.assertTrue(componentName2.equals("component1") || componentName2.equals("component2"));
            } else if (name.equals("group2")) {
                org.junit.Assert.assertEquals("2", hostGroupProps.get(org.apache.ambari.server.controller.internal.BlueprintResourceProvider.HOST_GROUP_CARDINALITY_PROPERTY_ID));
                org.junit.Assert.assertEquals(1, listComponents.size());
                java.util.Map<java.lang.String, java.lang.String> mapComponent = listComponents.get(0);
                java.lang.String componentName = mapComponent.get(org.apache.ambari.server.controller.internal.BlueprintResourceProvider.COMPONENT_NAME_PROPERTY_ID);
                org.junit.Assert.assertEquals("component1", componentName);
            } else {
                org.junit.Assert.fail("Unexpected host group name");
            }
        }
        if (containsConfig) {
            java.util.Collection<java.util.Map<java.lang.String, java.lang.Object>> blueprintConfigurations = ((java.util.Collection<java.util.Map<java.lang.String, java.lang.Object>>) (resource.getPropertyValue(org.apache.ambari.server.controller.internal.BlueprintResourceProvider.CONFIGURATION_PROPERTY_ID)));
            org.junit.Assert.assertEquals(1, blueprintConfigurations.size());
            java.util.Map<java.lang.String, java.lang.Object> typeConfigs = blueprintConfigurations.iterator().next();
            org.junit.Assert.assertEquals(1, typeConfigs.size());
            java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.Object>> coreSiteConfig = ((java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.Object>>) (typeConfigs.get("core-site")));
            org.junit.Assert.assertEquals(2, coreSiteConfig.size());
            org.junit.Assert.assertTrue(coreSiteConfig.containsKey(org.apache.ambari.server.controller.internal.BlueprintResourceProvider.PROPERTIES_PROPERTY_ID));
            java.util.Map<java.lang.String, java.lang.Object> properties = coreSiteConfig.get(org.apache.ambari.server.controller.internal.BlueprintResourceProvider.PROPERTIES_PROPERTY_ID);
            org.junit.Assert.assertNotNull(properties);
            org.junit.Assert.assertEquals("480", properties.get("fs.trash.interval"));
            org.junit.Assert.assertEquals("8500", properties.get("ipc.client.idlethreshold"));
            org.junit.Assert.assertTrue(coreSiteConfig.containsKey(org.apache.ambari.server.controller.internal.BlueprintResourceProvider.PROPERTIES_ATTRIBUTES_PROPERTY_ID));
            java.util.Map<java.lang.String, java.lang.Object> attributes = coreSiteConfig.get(org.apache.ambari.server.controller.internal.BlueprintResourceProvider.PROPERTIES_ATTRIBUTES_PROPERTY_ID);
            org.junit.Assert.assertNotNull(attributes);
            org.junit.Assert.assertEquals(1, attributes.size());
            org.junit.Assert.assertTrue(attributes.containsKey("final"));
            java.util.Map<java.lang.String, java.lang.String> finalAttrs = ((java.util.Map<java.lang.String, java.lang.String>) (attributes.get("final")));
            org.junit.Assert.assertEquals(1, finalAttrs.size());
            org.junit.Assert.assertEquals("true", finalAttrs.get("ipc.client.idlethreshold"));
        }
    }

    private static org.apache.ambari.server.controller.internal.BlueprintResourceProvider createProvider() {
        return new org.apache.ambari.server.controller.internal.BlueprintResourceProvider(null);
    }

    private org.apache.ambari.server.orm.entities.BlueprintEntity createEntity(java.util.Map<java.lang.String, java.lang.Object> properties) {
        org.apache.ambari.server.orm.entities.BlueprintEntity entity = new org.apache.ambari.server.orm.entities.BlueprintEntity();
        entity.setBlueprintName(((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.BlueprintResourceProvider.BLUEPRINT_NAME_PROPERTY_ID))));
        java.lang.String stackName = ((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.BlueprintResourceProvider.STACK_NAME_PROPERTY_ID)));
        java.lang.String stackVersion = ((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.BlueprintResourceProvider.STACK_VERSION_PROPERTY_ID)));
        org.apache.ambari.server.orm.entities.StackEntity stackEntity = new org.apache.ambari.server.orm.entities.StackEntity();
        stackEntity.setStackName(stackName);
        stackEntity.setStackVersion(stackVersion);
        entity.setStack(stackEntity);
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> hostGroupProperties = ((java.util.Set<java.util.Map<java.lang.String, java.lang.Object>>) (properties.get(org.apache.ambari.server.controller.internal.BlueprintResourceProvider.HOST_GROUP_PROPERTY_ID)));
        java.util.Collection<org.apache.ambari.server.orm.entities.HostGroupEntity> hostGroups = new java.util.ArrayList<>();
        for (java.util.Map<java.lang.String, java.lang.Object> groupProperties : hostGroupProperties) {
            org.apache.ambari.server.orm.entities.HostGroupEntity hostGroup = new org.apache.ambari.server.orm.entities.HostGroupEntity();
            hostGroups.add(hostGroup);
            hostGroup.setName(((java.lang.String) (groupProperties.get(org.apache.ambari.server.controller.internal.BlueprintResourceProvider.HOST_GROUP_NAME_PROPERTY_ID))));
            hostGroup.setCardinality(((java.lang.String) (groupProperties.get(org.apache.ambari.server.controller.internal.BlueprintResourceProvider.HOST_GROUP_CARDINALITY_PROPERTY_ID))));
            hostGroup.setConfigurations(new java.util.ArrayList<>());
            java.util.Set<java.util.Map<java.lang.String, java.lang.String>> setComponentProperties = ((java.util.Set<java.util.Map<java.lang.String, java.lang.String>>) (groupProperties.get(org.apache.ambari.server.controller.internal.BlueprintResourceProvider.COMPONENT_PROPERTY_ID)));
            java.util.Collection<org.apache.ambari.server.orm.entities.HostGroupComponentEntity> components = new java.util.ArrayList<>();
            for (java.util.Map<java.lang.String, java.lang.String> compProperties : setComponentProperties) {
                org.apache.ambari.server.orm.entities.HostGroupComponentEntity component = new org.apache.ambari.server.orm.entities.HostGroupComponentEntity();
                components.add(component);
                component.setName(compProperties.get(org.apache.ambari.server.controller.internal.BlueprintResourceProvider.COMPONENT_NAME_PROPERTY_ID));
            }
            hostGroup.setComponents(components);
        }
        entity.setHostGroups(hostGroups);
        java.util.Collection<java.util.Map<java.lang.String, java.lang.String>> configProperties = ((java.util.Collection<java.util.Map<java.lang.String, java.lang.String>>) (properties.get(org.apache.ambari.server.controller.internal.BlueprintResourceProvider.CONFIGURATION_PROPERTY_ID)));
        org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.createProvider().createBlueprintConfigEntities(configProperties, entity);
        return entity;
    }

    private java.util.Map<java.lang.String, java.lang.String> getTestRequestInfoProperties() {
        java.util.Map<java.lang.String, java.lang.String> setPropertiesInfo = new java.util.HashMap<>();
        java.lang.String configurationData = "{\"configurations\":[{\"config-type1\":{\"properties\" :{\"property\":\"property-value\"}}}," + ("{\"config-type2\" : {\"properties_attributes\" : {\"property\" : \"property-value\"}, \"properties\" : {\"property\" : \"property-value\"}}}" + "]}");
        setPropertiesInfo.put(org.apache.ambari.server.controller.spi.Request.REQUEST_INFO_BODY_PROPERTY, configurationData);
        return setPropertiesInfo;
    }

    @org.junit.Test
    public void testPopulateConfigurationEntity_oldSchema() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.lang.String> configuration = new java.util.HashMap<>();
        configuration.put("global/property1", "val1");
        configuration.put("global/property2", "val2");
        org.apache.ambari.server.orm.entities.BlueprintConfiguration config = new org.apache.ambari.server.orm.entities.BlueprintConfigEntity();
        org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.provider.populateConfigurationEntity(configuration, config);
        org.junit.Assert.assertNotNull(config.getConfigData());
        org.junit.Assert.assertNotNull(config.getConfigAttributes());
        java.util.Map<?, ?> configData = org.apache.ambari.server.utils.StageUtils.getGson().fromJson(config.getConfigData(), java.util.Map.class);
        java.util.Map<?, java.util.Map<?, ?>> configAttrs = org.apache.ambari.server.utils.StageUtils.getGson().fromJson(config.getConfigAttributes(), java.util.Map.class);
        org.junit.Assert.assertNotNull(configData);
        org.junit.Assert.assertNotNull(configAttrs);
        org.junit.Assert.assertEquals(2, configData.size());
        org.junit.Assert.assertTrue(configData.containsKey("property1"));
        org.junit.Assert.assertTrue(configData.containsKey("property2"));
        org.junit.Assert.assertEquals("val1", configData.get("property1"));
        org.junit.Assert.assertEquals("val2", configData.get("property2"));
        org.junit.Assert.assertEquals(0, configAttrs.size());
    }

    @org.junit.Test
    public void testPopulateConfigurationEntity_newSchema() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.lang.String> configuration = new java.util.HashMap<>();
        configuration.put("global/properties/property1", "val1");
        configuration.put("global/properties/property2", "val2");
        configuration.put("global/properties_attributes/final/property1", "true");
        configuration.put("global/properties_attributes/final/property2", "false");
        configuration.put("global/properties_attributes/deletable/property1", "true");
        org.apache.ambari.server.orm.entities.BlueprintConfiguration config = new org.apache.ambari.server.orm.entities.BlueprintConfigEntity();
        org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.provider.populateConfigurationEntity(configuration, config);
        org.junit.Assert.assertNotNull(config.getConfigData());
        org.junit.Assert.assertNotNull(config.getConfigAttributes());
        java.util.Map<?, ?> configData = org.apache.ambari.server.utils.StageUtils.getGson().fromJson(config.getConfigData(), java.util.Map.class);
        java.util.Map<?, java.util.Map<?, ?>> configAttrs = org.apache.ambari.server.utils.StageUtils.getGson().fromJson(config.getConfigAttributes(), java.util.Map.class);
        org.junit.Assert.assertNotNull(configData);
        org.junit.Assert.assertNotNull(configAttrs);
        org.junit.Assert.assertEquals(2, configData.size());
        org.junit.Assert.assertTrue(configData.containsKey("property1"));
        org.junit.Assert.assertTrue(configData.containsKey("property2"));
        org.junit.Assert.assertEquals("val1", configData.get("property1"));
        org.junit.Assert.assertEquals("val2", configData.get("property2"));
        org.junit.Assert.assertEquals(2, configAttrs.size());
        org.junit.Assert.assertTrue(configAttrs.containsKey("final"));
        org.junit.Assert.assertTrue(configAttrs.containsKey("deletable"));
        java.util.Map<?, ?> finalAttrs = configAttrs.get("final");
        org.junit.Assert.assertNotNull(finalAttrs);
        org.junit.Assert.assertEquals(2, finalAttrs.size());
        org.junit.Assert.assertTrue(finalAttrs.containsKey("property1"));
        org.junit.Assert.assertTrue(finalAttrs.containsKey("property2"));
        org.junit.Assert.assertEquals("true", finalAttrs.get("property1"));
        org.junit.Assert.assertEquals("false", finalAttrs.get("property2"));
        java.util.Map<?, ?> deletableAttrs = configAttrs.get("deletable");
        org.junit.Assert.assertNotNull(deletableAttrs);
        org.junit.Assert.assertEquals(1, deletableAttrs.size());
        org.junit.Assert.assertTrue(deletableAttrs.containsKey("property1"));
        org.junit.Assert.assertEquals("true", deletableAttrs.get("property1"));
    }

    @org.junit.Test
    public void testPopulateConfigurationEntity_configIsNull() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.lang.String> configuration = null;
        org.apache.ambari.server.orm.entities.BlueprintConfiguration config = new org.apache.ambari.server.orm.entities.BlueprintConfigEntity();
        org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.provider.populateConfigurationEntity(configuration, config);
        org.junit.Assert.assertNotNull(config.getConfigAttributes());
        org.junit.Assert.assertNotNull(config.getConfigData());
    }

    @org.junit.Test
    public void testPopulateConfigurationEntity_configIsEmpty() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.lang.String> configuration = new java.util.HashMap<>();
        org.apache.ambari.server.orm.entities.BlueprintConfiguration config = new org.apache.ambari.server.orm.entities.BlueprintConfigEntity();
        org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.provider.populateConfigurationEntity(configuration, config);
        org.junit.Assert.assertNotNull(config.getConfigAttributes());
        org.junit.Assert.assertNotNull(config.getConfigData());
    }

    @org.junit.Test
    public void testDecidePopulationStrategy_configIsEmpty() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.lang.String> configMap = new java.util.HashMap<>();
        org.apache.ambari.server.controller.internal.BlueprintResourceProvider.BlueprintConfigPopulationStrategy provisioner = org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.provider.decidePopulationStrategy(configMap);
        org.junit.Assert.assertNotNull(provisioner);
        org.junit.Assert.assertTrue(provisioner instanceof org.apache.ambari.server.controller.internal.BlueprintResourceProvider.BlueprintConfigPopulationStrategyV2);
    }

    @org.junit.Test
    public void testDecidePopulationStrategy_configIsNull() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.lang.String> configMap = null;
        org.apache.ambari.server.controller.internal.BlueprintResourceProvider.BlueprintConfigPopulationStrategy provisioner = org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.provider.decidePopulationStrategy(configMap);
        org.junit.Assert.assertNotNull(provisioner);
        org.junit.Assert.assertTrue(provisioner instanceof org.apache.ambari.server.controller.internal.BlueprintResourceProvider.BlueprintConfigPopulationStrategyV2);
    }

    @org.junit.Test
    public void testDecidePopulationStrategy_withOldSchema() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.lang.String> configMap = new java.util.HashMap<>();
        configMap.put("global/hive_database", "db");
        org.apache.ambari.server.controller.internal.BlueprintResourceProvider.BlueprintConfigPopulationStrategy provisioner = org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.provider.decidePopulationStrategy(configMap);
        org.junit.Assert.assertNotNull(provisioner);
        org.junit.Assert.assertTrue(provisioner instanceof org.apache.ambari.server.controller.internal.BlueprintResourceProvider.BlueprintConfigPopulationStrategyV1);
    }

    @org.junit.Test
    public void testDecidePopulationStrategy_withNewSchema_attributes() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.lang.String> configMap = new java.util.HashMap<>();
        configMap.put("global/properties_attributes/final/foo_contact", "true");
        org.apache.ambari.server.controller.internal.BlueprintResourceProvider.BlueprintConfigPopulationStrategy provisioner = org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.provider.decidePopulationStrategy(configMap);
        org.junit.Assert.assertNotNull(provisioner);
        org.junit.Assert.assertTrue(provisioner instanceof org.apache.ambari.server.controller.internal.BlueprintResourceProvider.BlueprintConfigPopulationStrategyV2);
    }

    @org.junit.Test
    public void testDecidePopulationStrategy_withNewSchema_properties() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.lang.String> configMap = new java.util.HashMap<>();
        configMap.put("global/properties/foo_contact", "foo@ffl.dsfds");
        org.apache.ambari.server.controller.internal.BlueprintResourceProvider.BlueprintConfigPopulationStrategy provisioner = org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.provider.decidePopulationStrategy(configMap);
        org.junit.Assert.assertNotNull(provisioner);
        org.junit.Assert.assertTrue(provisioner instanceof org.apache.ambari.server.controller.internal.BlueprintResourceProvider.BlueprintConfigPopulationStrategyV2);
    }

    @org.junit.Test
    public void testDecidePopulationStrategy_unsupportedSchema() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.lang.String> configMap = new java.util.HashMap<>();
        configMap.put("global/properties/lot/foo_contact", "foo@ffl.dsfds");
        expectedException.expect(java.lang.IllegalArgumentException.class);
        expectedException.expectMessage(org.apache.ambari.server.controller.internal.BlueprintResourceProvider.SCHEMA_IS_NOT_SUPPORTED_MESSAGE);
        org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.provider.decidePopulationStrategy(configMap);
    }

    @org.junit.Test
    public void testPopulateConfigurationList() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.StackEntity stackEntity = new org.apache.ambari.server.orm.entities.StackEntity();
        stackEntity.setStackName("test-stack-name");
        stackEntity.setStackVersion("test-stack-version");
        org.apache.ambari.server.orm.entities.BlueprintEntity entity = EasyMock.createMock(org.apache.ambari.server.orm.entities.BlueprintEntity.class);
        EasyMock.expect(entity.getStack()).andReturn(stackEntity).anyTimes();
        java.util.HashMap<org.apache.ambari.server.state.PropertyInfo.PropertyType, java.util.Set<java.lang.String>> pwdProperties = new java.util.HashMap<org.apache.ambari.server.state.PropertyInfo.PropertyType, java.util.Set<java.lang.String>>() {
            {
                put(org.apache.ambari.server.state.PropertyInfo.PropertyType.PASSWORD, new java.util.HashSet<java.lang.String>() {
                    {
                        add("test.password");
                    }
                });
            }
        };
        org.apache.ambari.server.state.StackInfo info = EasyMock.createMock(org.apache.ambari.server.state.StackInfo.class);
        EasyMock.expect(info.getConfigPropertiesTypes("type1")).andReturn(new java.util.HashMap<>()).anyTimes();
        EasyMock.expect(info.getConfigPropertiesTypes("type2")).andReturn(new java.util.HashMap<>()).anyTimes();
        EasyMock.expect(info.getConfigPropertiesTypes("type3")).andReturn(pwdProperties).anyTimes();
        EasyMock.expect(org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.metaInfo.getStack("test-stack-name", "test-stack-version")).andReturn(info).anyTimes();
        EasyMock.replay(info, org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.metaInfo, entity);
        org.apache.ambari.server.orm.entities.BlueprintConfigEntity config1 = new org.apache.ambari.server.orm.entities.BlueprintConfigEntity();
        config1.setType("type1");
        config1.setConfigData("{\"key1\":\"value1\"}");
        config1.setBlueprintEntity(entity);
        org.apache.ambari.server.orm.entities.BlueprintConfigEntity config2 = new org.apache.ambari.server.orm.entities.BlueprintConfigEntity();
        config2.setType("type2");
        config2.setConfigData("{\"key2\":\"value2\"}");
        config2.setConfigAttributes("{}");
        config2.setBlueprintEntity(entity);
        org.apache.ambari.server.orm.entities.BlueprintConfigEntity config3 = new org.apache.ambari.server.orm.entities.BlueprintConfigEntity();
        config3.setType("type3");
        config3.setConfigData("{\"key3\":\"value3\",\"key4\":\"value4\",\"test.password\":\"pwdValue\"}");
        config3.setConfigAttributes("{\"final\":{\"key3\":\"attrValue1\",\"key4\":\"attrValue2\"}}");
        config3.setBlueprintEntity(entity);
        java.util.List<java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.Object>>> configs = org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.provider.populateConfigurationList(java.util.Arrays.asList(config1, config2, config3));
        org.junit.Assert.assertNotNull(configs);
        org.junit.Assert.assertEquals(3, configs.size());
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.Object>> configuration1 = configs.get(0);
        org.junit.Assert.assertNotNull(configuration1);
        org.junit.Assert.assertEquals(1, configuration1.size());
        org.junit.Assert.assertTrue(configuration1.containsKey("type1"));
        java.util.Map<java.lang.String, java.lang.Object> typeConfig1 = configuration1.get("type1");
        org.junit.Assert.assertNotNull(typeConfig1);
        org.junit.Assert.assertEquals(1, typeConfig1.size());
        org.junit.Assert.assertTrue(typeConfig1.containsKey(org.apache.ambari.server.controller.internal.BlueprintResourceProvider.PROPERTIES_PROPERTY_ID));
        java.util.Map<java.lang.String, java.lang.String> confProperties1 = ((java.util.Map<java.lang.String, java.lang.String>) (typeConfig1.get(org.apache.ambari.server.controller.internal.BlueprintResourceProvider.PROPERTIES_PROPERTY_ID)));
        org.junit.Assert.assertNotNull(confProperties1);
        org.junit.Assert.assertEquals(1, confProperties1.size());
        org.junit.Assert.assertEquals("value1", confProperties1.get("key1"));
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.Object>> configuration2 = configs.get(1);
        org.junit.Assert.assertNotNull(configuration2);
        org.junit.Assert.assertEquals(1, configuration2.size());
        org.junit.Assert.assertTrue(configuration2.containsKey("type2"));
        java.util.Map<java.lang.String, java.lang.Object> typeConfig2 = configuration2.get("type2");
        org.junit.Assert.assertNotNull(typeConfig2);
        org.junit.Assert.assertEquals(1, typeConfig2.size());
        org.junit.Assert.assertTrue(typeConfig2.containsKey(org.apache.ambari.server.controller.internal.BlueprintResourceProvider.PROPERTIES_PROPERTY_ID));
        java.util.Map<java.lang.String, java.lang.String> confProperties2 = ((java.util.Map<java.lang.String, java.lang.String>) (typeConfig2.get(org.apache.ambari.server.controller.internal.BlueprintResourceProvider.PROPERTIES_PROPERTY_ID)));
        org.junit.Assert.assertNotNull(confProperties2);
        org.junit.Assert.assertEquals(1, confProperties2.size());
        org.junit.Assert.assertEquals("value2", confProperties2.get("key2"));
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.Object>> configuration3 = configs.get(2);
        org.junit.Assert.assertNotNull(configuration3);
        org.junit.Assert.assertEquals(1, configuration3.size());
        org.junit.Assert.assertTrue(configuration3.containsKey("type3"));
        java.util.Map<java.lang.String, java.lang.Object> typeConfig3 = configuration3.get("type3");
        org.junit.Assert.assertNotNull(typeConfig3);
        org.junit.Assert.assertEquals(2, typeConfig3.size());
        org.junit.Assert.assertTrue(typeConfig3.containsKey(org.apache.ambari.server.controller.internal.BlueprintResourceProvider.PROPERTIES_PROPERTY_ID));
        java.util.Map<java.lang.String, java.lang.String> confProperties3 = ((java.util.Map<java.lang.String, java.lang.String>) (typeConfig3.get(org.apache.ambari.server.controller.internal.BlueprintResourceProvider.PROPERTIES_PROPERTY_ID)));
        org.junit.Assert.assertNotNull(confProperties3);
        org.junit.Assert.assertEquals(3, confProperties3.size());
        org.junit.Assert.assertEquals("value3", confProperties3.get("key3"));
        org.junit.Assert.assertEquals("value4", confProperties3.get("key4"));
        org.junit.Assert.assertEquals("SECRET:type3:-1:test.password", confProperties3.get("test.password"));
        org.junit.Assert.assertTrue(typeConfig3.containsKey(org.apache.ambari.server.controller.internal.BlueprintResourceProvider.PROPERTIES_ATTRIBUTES_PROPERTY_ID));
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> confAttributes3 = ((java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>) (typeConfig3.get(org.apache.ambari.server.controller.internal.BlueprintResourceProvider.PROPERTIES_ATTRIBUTES_PROPERTY_ID)));
        org.junit.Assert.assertNotNull(confAttributes3);
        org.junit.Assert.assertEquals(1, confAttributes3.size());
        org.junit.Assert.assertTrue(confAttributes3.containsKey("final"));
        java.util.Map<java.lang.String, java.lang.String> finalAttrs = confAttributes3.get("final");
        org.junit.Assert.assertEquals(2, finalAttrs.size());
        org.junit.Assert.assertEquals("attrValue1", finalAttrs.get("key3"));
        org.junit.Assert.assertEquals("attrValue2", finalAttrs.get("key4"));
    }

    @org.junit.Test
    public void testPopulateSettingList() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.StackEntity stackEntity = new org.apache.ambari.server.orm.entities.StackEntity();
        stackEntity.setStackName("test-stack-name");
        stackEntity.setStackVersion("test-stack-version");
        org.apache.ambari.server.orm.entities.BlueprintEntity entity = EasyMock.createMock(org.apache.ambari.server.orm.entities.BlueprintEntity.class);
        EasyMock.expect(entity.getStack()).andReturn(stackEntity).anyTimes();
        java.util.HashMap<org.apache.ambari.server.state.PropertyInfo.PropertyType, java.util.Set<java.lang.String>> pwdProperties = new java.util.HashMap<org.apache.ambari.server.state.PropertyInfo.PropertyType, java.util.Set<java.lang.String>>() {
            {
                put(org.apache.ambari.server.state.PropertyInfo.PropertyType.PASSWORD, new java.util.HashSet<java.lang.String>() {
                    {
                        add("test.password");
                    }
                });
            }
        };
        org.apache.ambari.server.state.StackInfo info = EasyMock.createMock(org.apache.ambari.server.state.StackInfo.class);
        EasyMock.expect(info.getConfigPropertiesTypes("type1")).andReturn(new java.util.HashMap<>()).anyTimes();
        EasyMock.expect(info.getConfigPropertiesTypes("type2")).andReturn(new java.util.HashMap<>()).anyTimes();
        EasyMock.expect(info.getConfigPropertiesTypes("type3")).andReturn(pwdProperties).anyTimes();
        EasyMock.expect(org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.metaInfo.getStack("test-stack-name", "test-stack-version")).andReturn(info).anyTimes();
        EasyMock.replay(info, org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.metaInfo, entity);
        org.apache.ambari.server.orm.entities.BlueprintSettingEntity settingEntity1 = new org.apache.ambari.server.orm.entities.BlueprintSettingEntity();
        settingEntity1.setSettingName("recovery_settings");
        settingEntity1.setSettingData("[{\"recovery_enabled\":\"true\"}]");
        settingEntity1.setBlueprintEntity(entity);
        org.apache.ambari.server.orm.entities.BlueprintSettingEntity settingEntity2 = new org.apache.ambari.server.orm.entities.BlueprintSettingEntity();
        settingEntity2.setSettingName("service_settings");
        settingEntity2.setSettingData("[{\"name\":\"HDFS\", \"recovery_enabled\":\"false\"}, " + "{\"name\":\"ZOOKEEPER\", \"recovery_enabled\":\"false\"}]");
        settingEntity2.setBlueprintEntity(entity);
        org.apache.ambari.server.orm.entities.BlueprintSettingEntity settingEntity3 = new org.apache.ambari.server.orm.entities.BlueprintSettingEntity();
        settingEntity3.setSettingName("component_settings");
        settingEntity3.setSettingData("[{\"name\":\"METRICS_MONITOR\", \"recovery_enabled\":\"false\"}," + "{\"name\":\"KAFKA_CLIENT\", \"recovery_enabled\":\"false\"}]");
        settingEntity3.setBlueprintEntity(entity);
        java.util.List<org.apache.ambari.server.orm.entities.BlueprintSettingEntity> settingEntities = new java.util.ArrayList();
        settingEntities.add(settingEntity1);
        settingEntities.add(settingEntity2);
        settingEntities.add(settingEntity3);
        java.util.List<java.util.Map<java.lang.String, java.lang.Object>> settings = org.apache.ambari.server.controller.internal.BlueprintResourceProviderTest.provider.populateSettingList(settingEntities);
        org.junit.Assert.assertNotNull(settings);
        org.junit.Assert.assertEquals(settingEntities.size(), settings.size());
        java.util.Map<java.lang.String, java.lang.Object> setting1 = settings.get(0);
        org.junit.Assert.assertNotNull(setting1);
        org.junit.Assert.assertEquals(1, setting1.size());
        org.junit.Assert.assertTrue(setting1.containsKey("recovery_settings"));
        java.util.List<java.util.Map<java.lang.String, java.lang.String>> setting1value = ((java.util.List<java.util.Map<java.lang.String, java.lang.String>>) (setting1.get("recovery_settings")));
        org.junit.Assert.assertNotNull(setting1value);
        org.junit.Assert.assertEquals(1, setting1value.size());
        org.junit.Assert.assertTrue(setting1value.get(0).containsKey("recovery_enabled"));
        org.junit.Assert.assertEquals(setting1value.get(0).get("recovery_enabled"), "true");
        java.util.Map<java.lang.String, java.lang.Object> setting2 = settings.get(1);
        org.junit.Assert.assertNotNull(setting2);
        org.junit.Assert.assertEquals(1, setting2.size());
        org.junit.Assert.assertTrue(setting2.containsKey("service_settings"));
        java.util.List<java.util.Map<java.lang.String, java.lang.String>> setting2value = ((java.util.List<java.util.Map<java.lang.String, java.lang.String>>) (setting2.get("service_settings")));
        org.junit.Assert.assertNotNull(setting2value);
        org.junit.Assert.assertEquals(2, setting2value.size());
        org.junit.Assert.assertTrue(setting2value.get(0).containsKey("name"));
        org.junit.Assert.assertEquals(setting2value.get(0).get("name"), "HDFS");
        org.junit.Assert.assertTrue(setting2value.get(0).containsKey("recovery_enabled"));
        org.junit.Assert.assertEquals(setting2value.get(0).get("recovery_enabled"), "false");
        org.junit.Assert.assertTrue(setting2value.get(1).containsKey("name"));
        org.junit.Assert.assertEquals(setting2value.get(1).get("name"), "ZOOKEEPER");
        org.junit.Assert.assertTrue(setting2value.get(1).containsKey("recovery_enabled"));
        org.junit.Assert.assertEquals(setting2value.get(1).get("recovery_enabled"), "false");
        java.util.Map<java.lang.String, java.lang.Object> setting3 = settings.get(2);
        org.junit.Assert.assertNotNull(setting3);
        org.junit.Assert.assertEquals(1, setting3.size());
        org.junit.Assert.assertTrue(setting3.containsKey("component_settings"));
        java.util.List<java.util.Map<java.lang.String, java.lang.String>> setting3value = ((java.util.List<java.util.Map<java.lang.String, java.lang.String>>) (setting3.get("component_settings")));
        org.junit.Assert.assertNotNull(setting3value);
        org.junit.Assert.assertEquals(2, setting3value.size());
        org.junit.Assert.assertTrue(setting3value.get(0).containsKey("name"));
        org.junit.Assert.assertEquals(setting3value.get(0).get("name"), "METRICS_MONITOR");
        org.junit.Assert.assertTrue(setting3value.get(0).containsKey("recovery_enabled"));
        org.junit.Assert.assertEquals(setting3value.get(0).get("recovery_enabled"), "false");
        org.junit.Assert.assertTrue(setting3value.get(1).containsKey("name"));
        org.junit.Assert.assertEquals(setting3value.get(1).get("name"), "KAFKA_CLIENT");
        org.junit.Assert.assertTrue(setting3value.get(1).containsKey("recovery_enabled"));
        org.junit.Assert.assertEquals(setting3value.get(1).get("recovery_enabled"), "false");
    }
}