package org.apache.ambari.server.controller.internal;
import org.easymock.Capture;
import org.easymock.EasyMock;
import org.eclipse.persistence.exceptions.DatabaseException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import static org.easymock.EasyMock.anyBoolean;
import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.createStrictMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.reset;
import static org.easymock.EasyMock.verify;
public class ClusterResourceProviderTest {
    private static final java.lang.String CLUSTER_NAME = "cluster_name";

    private static final java.lang.String BLUEPRINT_NAME = "blueprint_name";

    private org.apache.ambari.server.controller.internal.ClusterResourceProvider provider;

    private static final org.apache.ambari.server.controller.AmbariManagementController controller = EasyMock.createNiceMock(org.apache.ambari.server.controller.AmbariManagementController.class);

    private static final org.apache.ambari.server.controller.spi.Request request = EasyMock.createNiceMock(org.apache.ambari.server.controller.spi.Request.class);

    private static final org.apache.ambari.server.topology.TopologyManager topologyManager = EasyMock.createStrictMock(org.apache.ambari.server.topology.TopologyManager.class);

    private static final org.apache.ambari.server.topology.TopologyRequestFactory topologyFactory = EasyMock.createStrictMock(org.apache.ambari.server.topology.TopologyRequestFactory.class);

    private static final org.apache.ambari.server.topology.SecurityConfigurationFactory securityFactory = EasyMock.createMock(org.apache.ambari.server.topology.SecurityConfigurationFactory.class);

    private static final org.apache.ambari.server.controller.internal.ProvisionClusterRequest topologyRequest = EasyMock.createNiceMock(org.apache.ambari.server.controller.internal.ProvisionClusterRequest.class);

    private static final org.apache.ambari.server.topology.BlueprintFactory blueprintFactory = EasyMock.createStrictMock(org.apache.ambari.server.topology.BlueprintFactory.class);

    private static final org.apache.ambari.server.topology.Blueprint blueprint = EasyMock.createNiceMock(org.apache.ambari.server.topology.Blueprint.class);

    private static final org.apache.ambari.server.controller.RequestStatusResponse requestStatusResponse = EasyMock.createNiceMock(org.apache.ambari.server.controller.RequestStatusResponse.class);

    private static final com.google.gson.Gson gson = new com.google.gson.Gson();

    @org.junit.Before
    public void setup() throws java.lang.Exception {
        org.apache.ambari.server.controller.internal.ClusterResourceProvider.init(org.apache.ambari.server.controller.internal.ClusterResourceProviderTest.topologyManager, org.apache.ambari.server.controller.internal.ClusterResourceProviderTest.topologyFactory, org.apache.ambari.server.controller.internal.ClusterResourceProviderTest.securityFactory, org.apache.ambari.server.controller.internal.ClusterResourceProviderTest.gson);
        org.apache.ambari.server.controller.internal.ProvisionClusterRequest.init(org.apache.ambari.server.controller.internal.ClusterResourceProviderTest.blueprintFactory);
        provider = new org.apache.ambari.server.controller.internal.ClusterResourceProvider(org.apache.ambari.server.controller.internal.ClusterResourceProviderTest.controller);
        EasyMock.expect(org.apache.ambari.server.controller.internal.ClusterResourceProviderTest.blueprintFactory.getBlueprint(org.apache.ambari.server.controller.internal.ClusterResourceProviderTest.BLUEPRINT_NAME)).andReturn(org.apache.ambari.server.controller.internal.ClusterResourceProviderTest.blueprint).anyTimes();
        EasyMock.expect(org.apache.ambari.server.controller.internal.ClusterResourceProviderTest.securityFactory.createSecurityConfigurationFromRequest(null, false)).andReturn(null).anyTimes();
    }

    @org.junit.After
    public void tearDown() {
        EasyMock.reset(org.apache.ambari.server.controller.internal.ClusterResourceProviderTest.request, org.apache.ambari.server.controller.internal.ClusterResourceProviderTest.topologyManager, org.apache.ambari.server.controller.internal.ClusterResourceProviderTest.topologyFactory, org.apache.ambari.server.controller.internal.ClusterResourceProviderTest.topologyRequest, org.apache.ambari.server.controller.internal.ClusterResourceProviderTest.blueprintFactory, org.apache.ambari.server.controller.internal.ClusterResourceProviderTest.securityFactory, org.apache.ambari.server.controller.internal.ClusterResourceProviderTest.requestStatusResponse, org.apache.ambari.server.controller.internal.ClusterResourceProviderTest.blueprint);
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(null);
    }

    private void replayAll() {
        EasyMock.replay(org.apache.ambari.server.controller.internal.ClusterResourceProviderTest.request, org.apache.ambari.server.controller.internal.ClusterResourceProviderTest.topologyManager, org.apache.ambari.server.controller.internal.ClusterResourceProviderTest.topologyFactory, org.apache.ambari.server.controller.internal.ClusterResourceProviderTest.topologyRequest, org.apache.ambari.server.controller.internal.ClusterResourceProviderTest.blueprintFactory, org.apache.ambari.server.controller.internal.ClusterResourceProviderTest.securityFactory, org.apache.ambari.server.controller.internal.ClusterResourceProviderTest.requestStatusResponse, org.apache.ambari.server.controller.internal.ClusterResourceProviderTest.blueprint);
    }

    private void verifyAll() {
        EasyMock.verify(org.apache.ambari.server.controller.internal.ClusterResourceProviderTest.request, org.apache.ambari.server.controller.internal.ClusterResourceProviderTest.topologyManager, org.apache.ambari.server.controller.internal.ClusterResourceProviderTest.topologyFactory, org.apache.ambari.server.controller.internal.ClusterResourceProviderTest.topologyRequest, org.apache.ambari.server.controller.internal.ClusterResourceProviderTest.blueprintFactory, org.apache.ambari.server.controller.internal.ClusterResourceProviderTest.securityFactory, org.apache.ambari.server.controller.internal.ClusterResourceProviderTest.requestStatusResponse, org.apache.ambari.server.controller.internal.ClusterResourceProviderTest.blueprint);
    }

    @org.junit.Test
    public void testCreateResource_blueprint_asAdministrator() throws java.lang.Exception {
        testCreateResource_blueprint(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator());
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testCreateResource_blueprint__NonAdministrator() throws java.lang.Exception {
        testCreateResource_blueprint(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator());
    }

    @org.junit.Test
    public void testCreateResource_blueprint_With_ProvisionAction() throws java.lang.Exception {
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> requestProperties = createBlueprintRequestProperties(org.apache.ambari.server.controller.internal.ClusterResourceProviderTest.CLUSTER_NAME, org.apache.ambari.server.controller.internal.ClusterResourceProviderTest.BLUEPRINT_NAME);
        java.util.Map<java.lang.String, java.lang.Object> properties = requestProperties.iterator().next();
        properties.put(org.apache.ambari.server.controller.internal.BaseClusterRequest.PROVISION_ACTION_PROPERTY, "INSTALL_ONLY");
        java.util.Map<java.lang.String, java.lang.String> requestInfoProperties = new java.util.HashMap<>();
        requestInfoProperties.put(org.apache.ambari.server.controller.spi.Request.REQUEST_INFO_BODY_PROPERTY, "{}");
        EasyMock.expect(org.apache.ambari.server.controller.internal.ClusterResourceProviderTest.request.getProperties()).andReturn(requestProperties).anyTimes();
        EasyMock.expect(org.apache.ambari.server.controller.internal.ClusterResourceProviderTest.request.getRequestInfoProperties()).andReturn(requestInfoProperties).anyTimes();
        EasyMock.expect(org.apache.ambari.server.controller.internal.ClusterResourceProviderTest.securityFactory.createSecurityConfigurationFromRequest(org.easymock.EasyMock.anyObject(), EasyMock.anyBoolean())).andReturn(null).once();
        EasyMock.expect(org.apache.ambari.server.controller.internal.ClusterResourceProviderTest.topologyFactory.createProvisionClusterRequest(properties, null)).andReturn(org.apache.ambari.server.controller.internal.ClusterResourceProviderTest.topologyRequest).once();
        EasyMock.expect(org.apache.ambari.server.controller.internal.ClusterResourceProviderTest.topologyManager.provisionCluster(org.apache.ambari.server.controller.internal.ClusterResourceProviderTest.topologyRequest)).andReturn(org.apache.ambari.server.controller.internal.ClusterResourceProviderTest.requestStatusResponse).once();
        EasyMock.expect(org.apache.ambari.server.controller.internal.ClusterResourceProviderTest.requestStatusResponse.getRequestId()).andReturn(5150L).anyTimes();
        replayAll();
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator());
        org.apache.ambari.server.controller.spi.RequestStatus requestStatus = provider.createResources(org.apache.ambari.server.controller.internal.ClusterResourceProviderTest.request);
        org.junit.Assert.assertEquals(5150L, requestStatus.getRequestResource().getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Requests", "id")));
        org.junit.Assert.assertEquals(org.apache.ambari.server.controller.spi.Resource.Type.Request, requestStatus.getRequestResource().getType());
        org.junit.Assert.assertEquals("Accepted", requestStatus.getRequestResource().getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Requests", "status")));
        verifyAll();
    }

    @org.junit.Test(expected = java.lang.IllegalArgumentException.class)
    public void testCreateResource_blueprint_withInvalidSecurityConfiguration() throws java.lang.Exception {
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> requestProperties = createBlueprintRequestProperties(org.apache.ambari.server.controller.internal.ClusterResourceProviderTest.CLUSTER_NAME, org.apache.ambari.server.controller.internal.ClusterResourceProviderTest.BLUEPRINT_NAME);
        java.util.Map<java.lang.String, java.lang.Object> properties = requestProperties.iterator().next();
        java.util.Map<java.lang.String, java.lang.String> requestInfoProperties = new java.util.HashMap<>();
        requestInfoProperties.put(org.apache.ambari.server.controller.spi.Request.REQUEST_INFO_BODY_PROPERTY, "{\"security\" : {\n\"type\" : \"NONE\"," + ("\n\"kerberos_descriptor_reference\" : " + "\"testRef\"\n}}"));
        org.apache.ambari.server.topology.SecurityConfiguration blueprintSecurityConfiguration = org.apache.ambari.server.topology.SecurityConfiguration.withReference("testRef");
        org.apache.ambari.server.topology.SecurityConfiguration securityConfiguration = org.apache.ambari.server.topology.SecurityConfiguration.NONE;
        EasyMock.expect(org.apache.ambari.server.controller.internal.ClusterResourceProviderTest.request.getProperties()).andReturn(requestProperties).anyTimes();
        EasyMock.expect(org.apache.ambari.server.controller.internal.ClusterResourceProviderTest.request.getRequestInfoProperties()).andReturn(requestInfoProperties).anyTimes();
        EasyMock.expect(org.apache.ambari.server.controller.internal.ClusterResourceProviderTest.securityFactory.createSecurityConfigurationFromRequest(org.easymock.EasyMock.anyObject(), EasyMock.anyBoolean())).andReturn(securityConfiguration).once();
        EasyMock.expect(org.apache.ambari.server.controller.internal.ClusterResourceProviderTest.topologyFactory.createProvisionClusterRequest(properties, securityConfiguration)).andReturn(org.apache.ambari.server.controller.internal.ClusterResourceProviderTest.topologyRequest).once();
        EasyMock.expect(org.apache.ambari.server.controller.internal.ClusterResourceProviderTest.topologyRequest.getBlueprint()).andReturn(org.apache.ambari.server.controller.internal.ClusterResourceProviderTest.blueprint).anyTimes();
        EasyMock.expect(org.apache.ambari.server.controller.internal.ClusterResourceProviderTest.blueprint.getSecurity()).andReturn(blueprintSecurityConfiguration).anyTimes();
        EasyMock.expect(org.apache.ambari.server.controller.internal.ClusterResourceProviderTest.requestStatusResponse.getRequestId()).andReturn(5150L).anyTimes();
        replayAll();
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator());
        org.apache.ambari.server.controller.spi.RequestStatus requestStatus = provider.createResources(org.apache.ambari.server.controller.internal.ClusterResourceProviderTest.request);
    }

    @org.junit.Test
    public void testCreateResource_blueprint_withSecurityConfiguration() throws java.lang.Exception {
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> requestProperties = createBlueprintRequestProperties(org.apache.ambari.server.controller.internal.ClusterResourceProviderTest.CLUSTER_NAME, org.apache.ambari.server.controller.internal.ClusterResourceProviderTest.BLUEPRINT_NAME);
        java.util.Map<java.lang.String, java.lang.Object> properties = requestProperties.iterator().next();
        org.apache.ambari.server.topology.SecurityConfiguration securityConfiguration = org.apache.ambari.server.topology.SecurityConfiguration.withReference("testRef");
        java.util.Map<java.lang.String, java.lang.String> requestInfoProperties = new java.util.HashMap<>();
        requestInfoProperties.put(org.apache.ambari.server.controller.spi.Request.REQUEST_INFO_BODY_PROPERTY, "{\"security\" : {\n\"type\" : \"KERBEROS\",\n\"kerberos_descriptor_reference\" : " + "\"testRef\"\n}}");
        EasyMock.expect(org.apache.ambari.server.controller.internal.ClusterResourceProviderTest.request.getProperties()).andReturn(requestProperties).anyTimes();
        EasyMock.expect(org.apache.ambari.server.controller.internal.ClusterResourceProviderTest.request.getRequestInfoProperties()).andReturn(requestInfoProperties).anyTimes();
        EasyMock.expect(org.apache.ambari.server.controller.internal.ClusterResourceProviderTest.topologyFactory.createProvisionClusterRequest(properties, securityConfiguration)).andReturn(org.apache.ambari.server.controller.internal.ClusterResourceProviderTest.topologyRequest).once();
        EasyMock.expect(org.apache.ambari.server.controller.internal.ClusterResourceProviderTest.securityFactory.createSecurityConfigurationFromRequest(org.easymock.EasyMock.anyObject(), EasyMock.anyBoolean())).andReturn(securityConfiguration).once();
        EasyMock.expect(org.apache.ambari.server.controller.internal.ClusterResourceProviderTest.topologyManager.provisionCluster(org.apache.ambari.server.controller.internal.ClusterResourceProviderTest.topologyRequest)).andReturn(org.apache.ambari.server.controller.internal.ClusterResourceProviderTest.requestStatusResponse).once();
        EasyMock.expect(org.apache.ambari.server.controller.internal.ClusterResourceProviderTest.requestStatusResponse.getRequestId()).andReturn(5150L).anyTimes();
        replayAll();
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator());
        org.apache.ambari.server.controller.spi.RequestStatus requestStatus = provider.createResources(org.apache.ambari.server.controller.internal.ClusterResourceProviderTest.request);
        org.junit.Assert.assertEquals(5150L, requestStatus.getRequestResource().getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Requests", "id")));
        org.junit.Assert.assertEquals(org.apache.ambari.server.controller.spi.Resource.Type.Request, requestStatus.getRequestResource().getType());
        org.junit.Assert.assertEquals("Accepted", requestStatus.getRequestResource().getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Requests", "status")));
        verifyAll();
    }

    @org.junit.Test(expected = java.lang.IllegalArgumentException.class)
    public void testCreateResource_blueprint__InvalidRequest() throws java.lang.Exception {
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> requestProperties = createBlueprintRequestProperties(org.apache.ambari.server.controller.internal.ClusterResourceProviderTest.CLUSTER_NAME, org.apache.ambari.server.controller.internal.ClusterResourceProviderTest.BLUEPRINT_NAME);
        java.util.Map<java.lang.String, java.lang.Object> properties = requestProperties.iterator().next();
        EasyMock.expect(org.apache.ambari.server.controller.internal.ClusterResourceProviderTest.request.getProperties()).andReturn(requestProperties).anyTimes();
        EasyMock.expect(org.apache.ambari.server.controller.internal.ClusterResourceProviderTest.topologyFactory.createProvisionClusterRequest(properties, null)).andThrow(new org.apache.ambari.server.topology.InvalidTopologyException("test"));
        replayAll();
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator());
        provider.createResources(org.apache.ambari.server.controller.internal.ClusterResourceProviderTest.request);
    }

    @org.junit.Test
    public void testCreateResourcesAsAdministrator() throws java.lang.Exception {
        testCreateResources(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator());
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testCreateResourcesAsNonAdministrator() throws java.lang.Exception {
        testCreateResources(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator());
    }

    @org.junit.Test
    public void testCreateResourcesWithRetry() throws java.lang.Exception {
        org.apache.ambari.server.state.Clusters clusters = EasyMock.createMock(org.apache.ambari.server.state.Clusters.class);
        org.easymock.EasyMock.replay(clusters);
        org.apache.ambari.server.utils.RetryHelper.init(clusters, 3);
        org.apache.ambari.server.controller.spi.Resource.Type type = org.apache.ambari.server.controller.spi.Resource.Type.Cluster;
        org.apache.ambari.server.controller.AmbariManagementController managementController = EasyMock.createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.controller.RequestStatusResponse response = EasyMock.createNiceMock(org.apache.ambari.server.controller.RequestStatusResponse.class);
        managementController.createCluster(org.apache.ambari.server.controller.internal.AbstractResourceProviderTest.Matcher.getClusterRequest(null, "Cluster100", "HDP-0.1", null));
        EasyMock.expectLastCall().andThrow(new org.eclipse.persistence.exceptions.DatabaseException("test") {}).once().andVoid().atLeastOnce();
        EasyMock.replay(managementController, response);
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator());
        org.apache.ambari.server.controller.spi.ResourceProvider provider = org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.getResourceProvider(type, managementController);
        org.apache.ambari.server.controller.internal.AbstractResourceProviderTest.TestObserver observer = new org.apache.ambari.server.controller.internal.AbstractResourceProviderTest.TestObserver();
        ((org.apache.ambari.server.controller.internal.ObservableResourceProvider) (provider)).addObserver(observer);
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> propertySet = new java.util.LinkedHashSet<>();
        java.util.Map<java.lang.String, java.lang.Object> properties = new java.util.LinkedHashMap<>();
        properties.put(org.apache.ambari.server.controller.internal.ClusterResourceProvider.CLUSTER_NAME_PROPERTY_ID, "Cluster100");
        properties.put(org.apache.ambari.server.controller.internal.ClusterResourceProvider.CLUSTER_VERSION_PROPERTY_ID, "HDP-0.1");
        propertySet.add(properties);
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getCreateRequest(propertySet, null);
        provider.createResources(request);
        org.apache.ambari.server.controller.internal.ResourceProviderEvent lastEvent = observer.getLastEvent();
        org.junit.Assert.assertNotNull(lastEvent);
        org.junit.Assert.assertEquals(org.apache.ambari.server.controller.spi.Resource.Type.Cluster, lastEvent.getResourceType());
        org.junit.Assert.assertEquals(org.apache.ambari.server.controller.internal.ResourceProviderEvent.Type.Create, lastEvent.getType());
        org.junit.Assert.assertEquals(request, lastEvent.getRequest());
        org.junit.Assert.assertNull(lastEvent.getPredicate());
        EasyMock.verify(managementController, response);
        org.apache.ambari.server.utils.RetryHelper.init(clusters, 0);
    }

    @org.junit.Test
    public void testGetResourcesAsAdministrator() throws java.lang.Exception {
        testGetResources(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator());
    }

    @org.junit.Test
    public void testGetResourcesAsNonAdministrator() throws java.lang.Exception {
        testGetResources(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator());
    }

    public void testGetResources(org.springframework.security.core.Authentication authentication) throws java.lang.Exception {
        org.apache.ambari.server.controller.spi.Resource.Type type = org.apache.ambari.server.controller.spi.Resource.Type.Cluster;
        org.apache.ambari.server.controller.AmbariManagementController managementController = EasyMock.createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.state.Clusters clusters = EasyMock.createMock(org.apache.ambari.server.state.Clusters.class);
        java.util.Set<org.apache.ambari.server.controller.ClusterResponse> allResponse = new java.util.HashSet<>();
        allResponse.add(new org.apache.ambari.server.controller.ClusterResponse(100L, "Cluster100", org.apache.ambari.server.state.State.INSTALLED, org.apache.ambari.server.state.SecurityType.NONE, null, 0, null, null));
        allResponse.add(new org.apache.ambari.server.controller.ClusterResponse(101L, "Cluster101", org.apache.ambari.server.state.State.INSTALLED, org.apache.ambari.server.state.SecurityType.NONE, null, 0, null, null));
        allResponse.add(new org.apache.ambari.server.controller.ClusterResponse(102L, "Cluster102", org.apache.ambari.server.state.State.INSTALLED, org.apache.ambari.server.state.SecurityType.NONE, null, 0, null, null));
        allResponse.add(new org.apache.ambari.server.controller.ClusterResponse(103L, "Cluster103", org.apache.ambari.server.state.State.INSTALLED, org.apache.ambari.server.state.SecurityType.NONE, null, 0, null, null));
        allResponse.add(new org.apache.ambari.server.controller.ClusterResponse(104L, "Cluster104", org.apache.ambari.server.state.State.INSTALLED, org.apache.ambari.server.state.SecurityType.NONE, null, 0, null, null));
        java.util.Set<org.apache.ambari.server.controller.ClusterResponse> nameResponse = new java.util.HashSet<>();
        nameResponse.add(new org.apache.ambari.server.controller.ClusterResponse(102L, "Cluster102", org.apache.ambari.server.state.State.INSTALLED, org.apache.ambari.server.state.SecurityType.NONE, null, 0, null, null));
        java.util.Set<org.apache.ambari.server.controller.ClusterResponse> idResponse = new java.util.HashSet<>();
        idResponse.add(new org.apache.ambari.server.controller.ClusterResponse(103L, "Cluster103", org.apache.ambari.server.state.State.INSTALLED, org.apache.ambari.server.state.SecurityType.NONE, null, 0, null, null));
        org.easymock.Capture<java.util.Set<org.apache.ambari.server.controller.ClusterRequest>> captureClusterRequests = org.easymock.EasyMock.newCapture();
        EasyMock.expect(managementController.getClusters(EasyMock.capture(captureClusterRequests))).andReturn(allResponse).once();
        EasyMock.expect(managementController.getClusters(EasyMock.capture(captureClusterRequests))).andReturn(nameResponse).once();
        EasyMock.expect(managementController.getClusters(EasyMock.capture(captureClusterRequests))).andReturn(idResponse).once();
        EasyMock.expect(managementController.getClusters()).andReturn(clusters).anyTimes();
        EasyMock.replay(managementController, clusters);
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        org.apache.ambari.server.controller.spi.ResourceProvider provider = org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.getResourceProvider(type, managementController);
        java.util.Set<java.lang.String> propertyIds = new java.util.HashSet<>();
        propertyIds.add(org.apache.ambari.server.controller.internal.ClusterResourceProvider.CLUSTER_ID_PROPERTY_ID);
        propertyIds.add(org.apache.ambari.server.controller.internal.ClusterResourceProvider.CLUSTER_NAME_PROPERTY_ID);
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(propertyIds);
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = provider.getResources(request, null);
        org.junit.Assert.assertEquals(5, resources.size());
        for (org.apache.ambari.server.controller.spi.Resource resource : resources) {
            java.lang.Long id = ((java.lang.Long) (resource.getPropertyValue(org.apache.ambari.server.controller.internal.ClusterResourceProvider.CLUSTER_ID_PROPERTY_ID)));
            java.lang.String name = ((java.lang.String) (resource.getPropertyValue(org.apache.ambari.server.controller.internal.ClusterResourceProvider.CLUSTER_NAME_PROPERTY_ID)));
            org.junit.Assert.assertEquals(name, "Cluster" + id);
        }
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.ClusterResourceProvider.CLUSTER_NAME_PROPERTY_ID).equals("Cluster102").toPredicate();
        resources = provider.getResources(request, predicate);
        org.junit.Assert.assertEquals(1, resources.size());
        org.junit.Assert.assertEquals(102L, resources.iterator().next().getPropertyValue(org.apache.ambari.server.controller.internal.ClusterResourceProvider.CLUSTER_ID_PROPERTY_ID));
        org.junit.Assert.assertEquals("Cluster102", resources.iterator().next().getPropertyValue(org.apache.ambari.server.controller.internal.ClusterResourceProvider.CLUSTER_NAME_PROPERTY_ID));
        predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.ClusterResourceProvider.CLUSTER_ID_PROPERTY_ID).equals(103L).toPredicate();
        resources = provider.getResources(request, predicate);
        org.junit.Assert.assertEquals(1, resources.size());
        org.junit.Assert.assertEquals(103L, resources.iterator().next().getPropertyValue(org.apache.ambari.server.controller.internal.ClusterResourceProvider.CLUSTER_ID_PROPERTY_ID));
        org.junit.Assert.assertEquals("Cluster103", resources.iterator().next().getPropertyValue(org.apache.ambari.server.controller.internal.ClusterResourceProvider.CLUSTER_NAME_PROPERTY_ID));
        EasyMock.verify(managementController, clusters);
    }

    @org.junit.Test
    public void testUpdateResourcesAsAdministrator() throws java.lang.Exception {
        testUpdateResources(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator());
    }

    @org.junit.Test
    public void testUpdateResourcesAsClusterAdministrator() throws java.lang.Exception {
        testUpdateResources(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator());
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testUpdateResourcesAsServiceOperator() throws java.lang.Exception {
        testUpdateResources(org.apache.ambari.server.security.TestAuthenticationFactory.createServiceOperator());
    }

    @org.junit.Test
    public void testUpdateWithConfigurationAsAdministrator() throws java.lang.Exception {
        testUpdateWithConfiguration(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator());
    }

    @org.junit.Test
    public void testUpdateWithConfigurationAsClusterAdministrator() throws java.lang.Exception {
        testUpdateWithConfiguration(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator());
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testUpdateWithConfigurationAsServiceOperator() throws java.lang.Exception {
        testUpdateWithConfiguration(org.apache.ambari.server.security.TestAuthenticationFactory.createServiceOperator());
    }

    @org.junit.Test
    public void testDeleteResourcesAsAdministrator() throws java.lang.Exception {
        testDeleteResources(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator());
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testDeleteResourcesAsNonAdministrator() throws java.lang.Exception {
        testDeleteResources(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator());
    }

    private java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> createBlueprintRequestProperties(java.lang.String clusterName, java.lang.String blueprintName) {
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> propertySet = new java.util.LinkedHashSet<>();
        java.util.Map<java.lang.String, java.lang.Object> properties = new java.util.LinkedHashMap<>();
        properties.put(org.apache.ambari.server.controller.internal.ClusterResourceProvider.CLUSTER_NAME_PROPERTY_ID, clusterName);
        properties.put(org.apache.ambari.server.controller.internal.ClusterResourceProvider.BLUEPRINT, blueprintName);
        propertySet.add(properties);
        java.util.Collection<java.util.Map<java.lang.String, java.lang.Object>> hostGroups = new java.util.ArrayList<>();
        java.util.Map<java.lang.String, java.lang.Object> hostGroupProperties = new java.util.HashMap<>();
        hostGroups.add(hostGroupProperties);
        hostGroupProperties.put("name", "group1");
        java.util.Collection<java.util.Map<java.lang.String, java.lang.String>> hostGroupHosts = new java.util.ArrayList<>();
        hostGroupProperties.put("hosts", hostGroupHosts);
        java.util.Map<java.lang.String, java.lang.String> hostGroupHostProperties = new java.util.HashMap<>();
        hostGroupHostProperties.put("fqdn", "host.domain");
        hostGroupHosts.add(hostGroupHostProperties);
        properties.put("host_groups", hostGroups);
        java.util.Map<java.lang.String, java.lang.String> mapGroupConfigProperties = new java.util.HashMap<>();
        mapGroupConfigProperties.put("myGroupProp", "awesomeValue");
        java.util.Map<java.lang.String, java.lang.String> blueprintCoreConfigProperties = new java.util.HashMap<>();
        blueprintCoreConfigProperties.put("property1", "value2");
        blueprintCoreConfigProperties.put("new.property", "new.property.value");
        java.util.Map<java.lang.String, java.lang.String> blueprintGlobalConfigProperties = new java.util.HashMap<>();
        blueprintGlobalConfigProperties.put("hive_database", "New MySQL Database");
        java.util.Map<java.lang.String, java.lang.String> oozieEnvConfigProperties = new java.util.HashMap<>();
        oozieEnvConfigProperties.put("property1", "value2");
        java.util.Map<java.lang.String, java.lang.String> hbaseEnvConfigProperties = new java.util.HashMap<>();
        hbaseEnvConfigProperties.put("property1", "value2");
        java.util.Map<java.lang.String, java.lang.String> falconEnvConfigProperties = new java.util.HashMap<>();
        falconEnvConfigProperties.put("property1", "value2");
        return propertySet;
    }

    private void testCreateResource_blueprint(org.springframework.security.core.Authentication authentication) throws java.lang.Exception {
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> requestProperties = createBlueprintRequestProperties(org.apache.ambari.server.controller.internal.ClusterResourceProviderTest.CLUSTER_NAME, org.apache.ambari.server.controller.internal.ClusterResourceProviderTest.BLUEPRINT_NAME);
        java.util.Map<java.lang.String, java.lang.Object> properties = requestProperties.iterator().next();
        java.util.Map<java.lang.String, java.lang.String> requestInfoProperties = new java.util.HashMap<>();
        requestInfoProperties.put(org.apache.ambari.server.controller.spi.Request.REQUEST_INFO_BODY_PROPERTY, "{}");
        EasyMock.expect(org.apache.ambari.server.controller.internal.ClusterResourceProviderTest.request.getProperties()).andReturn(requestProperties).anyTimes();
        EasyMock.expect(org.apache.ambari.server.controller.internal.ClusterResourceProviderTest.request.getRequestInfoProperties()).andReturn(requestInfoProperties).anyTimes();
        EasyMock.expect(org.apache.ambari.server.controller.internal.ClusterResourceProviderTest.securityFactory.createSecurityConfigurationFromRequest(org.easymock.EasyMock.anyObject(), EasyMock.anyBoolean())).andReturn(null).once();
        EasyMock.expect(org.apache.ambari.server.controller.internal.ClusterResourceProviderTest.topologyFactory.createProvisionClusterRequest(properties, null)).andReturn(org.apache.ambari.server.controller.internal.ClusterResourceProviderTest.topologyRequest).once();
        EasyMock.expect(org.apache.ambari.server.controller.internal.ClusterResourceProviderTest.topologyManager.provisionCluster(org.apache.ambari.server.controller.internal.ClusterResourceProviderTest.topologyRequest)).andReturn(org.apache.ambari.server.controller.internal.ClusterResourceProviderTest.requestStatusResponse).once();
        EasyMock.expect(org.apache.ambari.server.controller.internal.ClusterResourceProviderTest.requestStatusResponse.getRequestId()).andReturn(5150L).anyTimes();
        replayAll();
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        org.apache.ambari.server.controller.spi.RequestStatus requestStatus = provider.createResources(org.apache.ambari.server.controller.internal.ClusterResourceProviderTest.request);
        org.junit.Assert.assertEquals(5150L, requestStatus.getRequestResource().getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Requests", "id")));
        org.junit.Assert.assertEquals(org.apache.ambari.server.controller.spi.Resource.Type.Request, requestStatus.getRequestResource().getType());
        org.junit.Assert.assertEquals("Accepted", requestStatus.getRequestResource().getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Requests", "status")));
        verifyAll();
    }

    private void testCreateResources(org.springframework.security.core.Authentication authentication) throws java.lang.Exception {
        org.apache.ambari.server.controller.spi.Resource.Type type = org.apache.ambari.server.controller.spi.Resource.Type.Cluster;
        org.apache.ambari.server.controller.AmbariManagementController managementController = EasyMock.createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.controller.RequestStatusResponse response = EasyMock.createNiceMock(org.apache.ambari.server.controller.RequestStatusResponse.class);
        managementController.createCluster(org.apache.ambari.server.controller.internal.AbstractResourceProviderTest.Matcher.getClusterRequest(null, "Cluster100", "HDP-0.1", null));
        managementController.createCluster(org.apache.ambari.server.controller.internal.AbstractResourceProviderTest.Matcher.getClusterRequest(99L, null, "HDP-0.1", null));
        EasyMock.replay(managementController, response);
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        org.apache.ambari.server.controller.spi.ResourceProvider provider = org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.getResourceProvider(type, managementController);
        org.apache.ambari.server.controller.internal.AbstractResourceProviderTest.TestObserver observer = new org.apache.ambari.server.controller.internal.AbstractResourceProviderTest.TestObserver();
        ((org.apache.ambari.server.controller.internal.ObservableResourceProvider) (provider)).addObserver(observer);
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> propertySet = new java.util.LinkedHashSet<>();
        java.util.Map<java.lang.String, java.lang.Object> properties = new java.util.LinkedHashMap<>();
        properties.put(org.apache.ambari.server.controller.internal.ClusterResourceProvider.CLUSTER_NAME_PROPERTY_ID, "Cluster100");
        properties.put(org.apache.ambari.server.controller.internal.ClusterResourceProvider.CLUSTER_VERSION_PROPERTY_ID, "HDP-0.1");
        propertySet.add(properties);
        properties = new java.util.LinkedHashMap<>();
        properties.put(org.apache.ambari.server.controller.internal.ClusterResourceProvider.CLUSTER_ID_PROPERTY_ID, 99L);
        properties.put(org.apache.ambari.server.controller.internal.ClusterResourceProvider.CLUSTER_VERSION_PROPERTY_ID, "HDP-0.1");
        propertySet.add(properties);
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getCreateRequest(propertySet, null);
        provider.createResources(request);
        org.apache.ambari.server.controller.internal.ResourceProviderEvent lastEvent = observer.getLastEvent();
        org.junit.Assert.assertNotNull(lastEvent);
        org.junit.Assert.assertEquals(org.apache.ambari.server.controller.spi.Resource.Type.Cluster, lastEvent.getResourceType());
        org.junit.Assert.assertEquals(org.apache.ambari.server.controller.internal.ResourceProviderEvent.Type.Create, lastEvent.getType());
        org.junit.Assert.assertEquals(request, lastEvent.getRequest());
        org.junit.Assert.assertNull(lastEvent.getPredicate());
        EasyMock.verify(managementController, response);
    }

    public void testUpdateResources(org.springframework.security.core.Authentication authentication) throws java.lang.Exception {
        org.apache.ambari.server.controller.spi.Resource.Type type = org.apache.ambari.server.controller.spi.Resource.Type.Cluster;
        org.apache.ambari.server.state.Clusters clusters = EasyMock.createMock(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.controller.AmbariManagementController managementController = EasyMock.createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.controller.RequestStatusResponse response = EasyMock.createNiceMock(org.apache.ambari.server.controller.RequestStatusResponse.class);
        java.util.Set<org.apache.ambari.server.controller.ClusterResponse> nameResponse = new java.util.HashSet<>();
        nameResponse.add(new org.apache.ambari.server.controller.ClusterResponse(102L, "Cluster102", org.apache.ambari.server.state.State.INIT, org.apache.ambari.server.state.SecurityType.NONE, null, 0, null, null));
        java.util.Map<java.lang.String, java.lang.String> mapRequestProps = new java.util.HashMap<>();
        mapRequestProps.put("context", "Called from a test");
        EasyMock.expect(managementController.getClusters(org.easymock.EasyMock.anyObject())).andReturn(nameResponse).once();
        EasyMock.expect(managementController.updateClusters(org.apache.ambari.server.controller.internal.AbstractResourceProviderTest.Matcher.getClusterRequestSet(102L, "Cluster102", org.apache.ambari.server.state.State.INSTALLED.name(), org.apache.ambari.server.state.SecurityType.NONE, "HDP-0.1", null), EasyMock.eq(mapRequestProps))).andReturn(response).once();
        EasyMock.expect(managementController.updateClusters(org.apache.ambari.server.controller.internal.AbstractResourceProviderTest.Matcher.getClusterRequestSet(103L, null, null, null, "HDP-0.1", null), EasyMock.eq(mapRequestProps))).andReturn(response).once();
        EasyMock.expect(managementController.getClusterUpdateResults(EasyMock.anyObject(org.apache.ambari.server.controller.ClusterRequest.class))).andReturn(null).anyTimes();
        EasyMock.expect(managementController.getClusters()).andReturn(clusters).anyTimes();
        EasyMock.replay(managementController, response, clusters);
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        org.apache.ambari.server.controller.spi.ResourceProvider provider = org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.getResourceProvider(type, managementController);
        org.apache.ambari.server.controller.internal.AbstractResourceProviderTest.TestObserver observer = new org.apache.ambari.server.controller.internal.AbstractResourceProviderTest.TestObserver();
        ((org.apache.ambari.server.controller.internal.ObservableResourceProvider) (provider)).addObserver(observer);
        java.util.Map<java.lang.String, java.lang.Object> properties = new java.util.LinkedHashMap<>();
        properties.put(org.apache.ambari.server.controller.internal.ClusterResourceProvider.CLUSTER_VERSION_PROPERTY_ID, "HDP-0.1");
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getUpdateRequest(properties, mapRequestProps);
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.ClusterResourceProvider.CLUSTER_NAME_PROPERTY_ID).equals("Cluster102").toPredicate();
        provider.updateResources(request, predicate);
        predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.ClusterResourceProvider.CLUSTER_ID_PROPERTY_ID).equals(103L).toPredicate();
        provider.updateResources(request, predicate);
        org.apache.ambari.server.controller.internal.ResourceProviderEvent lastEvent = observer.getLastEvent();
        org.junit.Assert.assertNotNull(lastEvent);
        org.junit.Assert.assertEquals(org.apache.ambari.server.controller.spi.Resource.Type.Cluster, lastEvent.getResourceType());
        org.junit.Assert.assertEquals(org.apache.ambari.server.controller.internal.ResourceProviderEvent.Type.Update, lastEvent.getType());
        org.junit.Assert.assertEquals(request, lastEvent.getRequest());
        org.junit.Assert.assertEquals(predicate, lastEvent.getPredicate());
        EasyMock.verify(managementController, response, clusters);
    }

    public void testUpdateWithConfiguration(org.springframework.security.core.Authentication authentication) throws java.lang.Exception {
        org.apache.ambari.server.controller.AmbariManagementController managementController = EasyMock.createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.state.Clusters clusters = EasyMock.createMock(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.controller.RequestStatusResponse response = EasyMock.createNiceMock(org.apache.ambari.server.controller.RequestStatusResponse.class);
        java.util.Set<org.apache.ambari.server.controller.ClusterResponse> nameResponse = new java.util.HashSet<>();
        nameResponse.add(new org.apache.ambari.server.controller.ClusterResponse(100L, "Cluster100", org.apache.ambari.server.state.State.INSTALLED, org.apache.ambari.server.state.SecurityType.NONE, null, 0, null, null));
        java.util.Map<java.lang.String, java.lang.String> mapRequestProps = new java.util.HashMap<>();
        mapRequestProps.put("context", "Called from a test");
        EasyMock.expect(managementController.getClusters(org.easymock.EasyMock.anyObject())).andReturn(nameResponse).times(2);
        EasyMock.expect(managementController.updateClusters(java.util.Collections.singleton(org.easymock.EasyMock.anyObject(org.apache.ambari.server.controller.ClusterRequest.class)), EasyMock.eq(mapRequestProps))).andReturn(response).times(1);
        EasyMock.expect(managementController.getClusterUpdateResults(EasyMock.anyObject(org.apache.ambari.server.controller.ClusterRequest.class))).andReturn(null).anyTimes();
        EasyMock.expect(managementController.getClusters()).andReturn(clusters).anyTimes();
        EasyMock.replay(managementController, response, clusters);
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        java.util.Map<java.lang.String, java.lang.Object> properties = new java.util.LinkedHashMap<>();
        properties.put(org.apache.ambari.server.controller.internal.ClusterResourceProvider.CLUSTER_NAME_PROPERTY_ID, "Cluster100");
        properties.put(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Clusters.desired_config", "type"), "global");
        properties.put(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Clusters.desired_config", "tag"), "version1");
        properties.put(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Clusters.desired_config.properties", "a"), "b");
        properties.put(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Clusters.desired_config.properties", "x"), "y");
        java.util.Map<java.lang.String, java.lang.Object> properties2 = new java.util.LinkedHashMap<>();
        properties2.put(org.apache.ambari.server.controller.internal.ClusterResourceProvider.CLUSTER_NAME_PROPERTY_ID, "Cluster100");
        properties2.put(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Clusters.desired_config", "type"), "mapred-site");
        properties2.put(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Clusters.desired_config", "tag"), "versio99");
        properties2.put(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Clusters.desired_config.properties", "foo"), "A1");
        properties2.put(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Clusters.desired_config.properties", "bar"), "B2");
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> propertySet = new java.util.HashSet<>();
        propertySet.add(properties);
        propertySet.add(properties2);
        org.apache.ambari.server.controller.spi.Request request = new org.apache.ambari.server.controller.internal.RequestImpl(null, propertySet, mapRequestProps, null);
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.ClusterResourceProvider.CLUSTER_NAME_PROPERTY_ID).equals("Cluster100").toPredicate();
        org.apache.ambari.server.controller.spi.ResourceProvider provider = org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.getResourceProvider(org.apache.ambari.server.controller.spi.Resource.Type.Cluster, managementController);
        org.apache.ambari.server.controller.internal.AbstractResourceProviderTest.TestObserver observer = new org.apache.ambari.server.controller.internal.AbstractResourceProviderTest.TestObserver();
        ((org.apache.ambari.server.controller.internal.ObservableResourceProvider) (provider)).addObserver(observer);
        provider.updateResources(request, predicate);
        org.apache.ambari.server.controller.internal.ResourceProviderEvent lastEvent = observer.getLastEvent();
        org.junit.Assert.assertNotNull(lastEvent);
        org.junit.Assert.assertEquals(org.apache.ambari.server.controller.spi.Resource.Type.Cluster, lastEvent.getResourceType());
        org.junit.Assert.assertEquals(org.apache.ambari.server.controller.internal.ResourceProviderEvent.Type.Update, lastEvent.getType());
        org.junit.Assert.assertEquals(request, lastEvent.getRequest());
        org.junit.Assert.assertEquals(predicate, lastEvent.getPredicate());
        EasyMock.verify(managementController, response, clusters);
    }

    public void testDeleteResources(org.springframework.security.core.Authentication authentication) throws java.lang.Exception {
        org.apache.ambari.server.controller.spi.Resource.Type type = org.apache.ambari.server.controller.spi.Resource.Type.Cluster;
        org.apache.ambari.server.controller.AmbariManagementController managementController = EasyMock.createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.state.Clusters clusters = EasyMock.createMock(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.controller.RequestStatusResponse response = EasyMock.createNiceMock(org.apache.ambari.server.controller.RequestStatusResponse.class);
        managementController.deleteCluster(org.apache.ambari.server.controller.internal.AbstractResourceProviderTest.Matcher.getClusterRequest(null, "Cluster102", null, null));
        managementController.deleteCluster(org.apache.ambari.server.controller.internal.AbstractResourceProviderTest.Matcher.getClusterRequest(103L, null, null, null));
        EasyMock.expect(managementController.getClusters()).andReturn(clusters).anyTimes();
        EasyMock.replay(managementController, response, clusters);
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        org.apache.ambari.server.controller.spi.ResourceProvider provider = org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.getResourceProvider(type, managementController);
        org.apache.ambari.server.controller.internal.AbstractResourceProviderTest.TestObserver observer = new org.apache.ambari.server.controller.internal.AbstractResourceProviderTest.TestObserver();
        ((org.apache.ambari.server.controller.internal.ObservableResourceProvider) (provider)).addObserver(observer);
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.ClusterResourceProvider.CLUSTER_NAME_PROPERTY_ID).equals("Cluster102").toPredicate();
        provider.deleteResources(new org.apache.ambari.server.controller.internal.RequestImpl(null, null, null, null), predicate);
        predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.ClusterResourceProvider.CLUSTER_ID_PROPERTY_ID).equals(103L).toPredicate();
        provider.deleteResources(new org.apache.ambari.server.controller.internal.RequestImpl(null, null, null, null), predicate);
        org.apache.ambari.server.controller.internal.ResourceProviderEvent lastEvent = observer.getLastEvent();
        org.junit.Assert.assertNotNull(lastEvent);
        org.junit.Assert.assertEquals(org.apache.ambari.server.controller.spi.Resource.Type.Cluster, lastEvent.getResourceType());
        org.junit.Assert.assertEquals(org.apache.ambari.server.controller.internal.ResourceProviderEvent.Type.Delete, lastEvent.getType());
        org.junit.Assert.assertEquals(predicate, lastEvent.getPredicate());
        org.junit.Assert.assertNull(lastEvent.getRequest());
        EasyMock.verify(managementController, response, clusters);
    }

    @org.junit.Test
    public void testCreateWithRepository() throws java.lang.Exception {
        org.springframework.security.core.Authentication authentication = org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator();
        org.apache.ambari.server.controller.spi.Resource.Type type = org.apache.ambari.server.controller.spi.Resource.Type.Cluster;
        org.apache.ambari.server.controller.AmbariManagementController managementController = EasyMock.createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.easymock.Capture<org.apache.ambari.server.controller.ClusterRequest> cap = org.easymock.Capture.newInstance();
        managementController.createCluster(EasyMock.capture(cap));
        EasyMock.expectLastCall();
        EasyMock.replay(managementController);
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        org.apache.ambari.server.controller.spi.ResourceProvider provider = org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.getResourceProvider(type, managementController);
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> propertySet = new java.util.LinkedHashSet<>();
        java.util.Map<java.lang.String, java.lang.Object> properties = new java.util.LinkedHashMap<>();
        properties.put(org.apache.ambari.server.controller.internal.ClusterResourceProvider.CLUSTER_NAME_PROPERTY_ID, "Cluster100");
        properties.put(org.apache.ambari.server.controller.internal.ClusterResourceProvider.CLUSTER_VERSION_PROPERTY_ID, "HDP-0.1");
        propertySet.add(properties);
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getCreateRequest(propertySet, null);
        provider.createResources(request);
        EasyMock.verify(managementController);
        org.junit.Assert.assertTrue(cap.hasCaptured());
        org.junit.Assert.assertNotNull(cap.getValue());
    }

    @org.junit.Test
    public void testCreateResource_blueprint_withRepoVersion() throws java.lang.Exception {
        org.springframework.security.core.Authentication authentication = org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator();
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> requestProperties = createBlueprintRequestProperties(org.apache.ambari.server.controller.internal.ClusterResourceProviderTest.CLUSTER_NAME, org.apache.ambari.server.controller.internal.ClusterResourceProviderTest.BLUEPRINT_NAME);
        java.util.Map<java.lang.String, java.lang.Object> properties = requestProperties.iterator().next();
        properties.put(org.apache.ambari.server.controller.internal.ProvisionClusterRequest.REPO_VERSION_PROPERTY, "2.1.1");
        java.util.Map<java.lang.String, java.lang.String> requestInfoProperties = new java.util.HashMap<>();
        requestInfoProperties.put(org.apache.ambari.server.controller.spi.Request.REQUEST_INFO_BODY_PROPERTY, "{}");
        EasyMock.expect(org.apache.ambari.server.controller.internal.ClusterResourceProviderTest.request.getProperties()).andReturn(requestProperties).anyTimes();
        EasyMock.expect(org.apache.ambari.server.controller.internal.ClusterResourceProviderTest.request.getRequestInfoProperties()).andReturn(requestInfoProperties).anyTimes();
        EasyMock.expect(org.apache.ambari.server.controller.internal.ClusterResourceProviderTest.securityFactory.createSecurityConfigurationFromRequest(org.easymock.EasyMock.anyObject(), EasyMock.anyBoolean())).andReturn(null).once();
        EasyMock.expect(org.apache.ambari.server.controller.internal.ClusterResourceProviderTest.topologyFactory.createProvisionClusterRequest(properties, null)).andReturn(org.apache.ambari.server.controller.internal.ClusterResourceProviderTest.topologyRequest).once();
        EasyMock.expect(org.apache.ambari.server.controller.internal.ClusterResourceProviderTest.topologyManager.provisionCluster(org.apache.ambari.server.controller.internal.ClusterResourceProviderTest.topologyRequest)).andReturn(org.apache.ambari.server.controller.internal.ClusterResourceProviderTest.requestStatusResponse).once();
        EasyMock.expect(org.apache.ambari.server.controller.internal.ClusterResourceProviderTest.requestStatusResponse.getRequestId()).andReturn(5150L).anyTimes();
        replayAll();
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        org.apache.ambari.server.controller.spi.RequestStatus requestStatus = provider.createResources(org.apache.ambari.server.controller.internal.ClusterResourceProviderTest.request);
        org.junit.Assert.assertEquals(5150L, requestStatus.getRequestResource().getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Requests", "id")));
        org.junit.Assert.assertEquals(org.apache.ambari.server.controller.spi.Resource.Type.Request, requestStatus.getRequestResource().getType());
        org.junit.Assert.assertEquals("Accepted", requestStatus.getRequestResource().getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Requests", "status")));
        verifyAll();
    }
}