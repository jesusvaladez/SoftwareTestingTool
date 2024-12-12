package org.apache.ambari.server.controller.internal;
import org.easymock.EasyMock;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import static org.apache.ambari.server.controller.AmbariManagementControllerImpl.CLUSTER_PHASE_INITIAL_INSTALL;
import static org.apache.ambari.server.controller.AmbariManagementControllerImpl.CLUSTER_PHASE_PROPERTY;
import static org.apache.ambari.server.controller.internal.HostComponentResourceProvider.ALL_COMPONENTS;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
public class HostComponentResourceProviderTest {
    @org.junit.Before
    public void clearAuthentication() {
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(null);
    }

    @org.junit.Test
    public void testCreateResourcesAsAdministrator() throws java.lang.Exception {
        testCreateResources(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator());
    }

    @org.junit.Test
    public void testCreateResourcesAsClusterAdministrator() throws java.lang.Exception {
        testCreateResources(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator());
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testCreateResourcesAsServiceAdministrator() throws java.lang.Exception {
        testCreateResources(org.apache.ambari.server.security.TestAuthenticationFactory.createServiceAdministrator());
    }

    private void testCreateResources(org.springframework.security.core.Authentication authentication) throws java.lang.Exception {
        org.apache.ambari.server.controller.spi.Resource.Type type = org.apache.ambari.server.controller.spi.Resource.Type.HostComponent;
        org.apache.ambari.server.controller.AmbariManagementController managementController = EasyMock.createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.controller.RequestStatusResponse response = EasyMock.createNiceMock(org.apache.ambari.server.controller.RequestStatusResponse.class);
        org.apache.ambari.server.controller.ResourceProviderFactory resourceProviderFactory = EasyMock.createNiceMock(org.apache.ambari.server.controller.ResourceProviderFactory.class);
        com.google.inject.Injector injector = EasyMock.createNiceMock(com.google.inject.Injector.class);
        org.apache.ambari.server.controller.internal.HostComponentResourceProvider hostComponentResourceProvider = new org.apache.ambari.server.controller.internal.HostComponentResourceProvider(managementController);
        org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.init(resourceProviderFactory);
        managementController.createHostComponents(org.apache.ambari.server.controller.internal.AbstractResourceProviderTest.Matcher.getHostComponentRequestSet("Cluster100", "Service100", "Component100", "Host100", null, null));
        EasyMock.expect(resourceProviderFactory.getHostComponentResourceProvider(EasyMock.eq(managementController))).andReturn(hostComponentResourceProvider).anyTimes();
        EasyMock.replay(managementController, response, resourceProviderFactory);
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        org.apache.ambari.server.controller.spi.ResourceProvider provider = org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.getResourceProvider(type, managementController);
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> propertySet = new java.util.LinkedHashSet<>();
        java.util.Map<java.lang.String, java.lang.Object> properties = new java.util.LinkedHashMap<>();
        properties.put(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.CLUSTER_NAME, "Cluster100");
        properties.put(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.SERVICE_NAME, "Service100");
        properties.put(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.COMPONENT_NAME, "Component100");
        properties.put(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.HOST_NAME, "Host100");
        propertySet.add(properties);
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getCreateRequest(propertySet, null);
        provider.createResources(request);
        EasyMock.verify(managementController, response, resourceProviderFactory);
    }

    @org.junit.Test
    public void testGetResourcesAsAdministrator() throws java.lang.Exception {
        testGetResources(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator());
    }

    @org.junit.Test
    public void testGetResourcesAsClusterAdministrator() throws java.lang.Exception {
        testGetResources(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator());
    }

    @org.junit.Test
    public void testGetResourcesAsServiceAdministrator() throws java.lang.Exception {
        testGetResources(org.apache.ambari.server.security.TestAuthenticationFactory.createServiceAdministrator());
    }

    private void testGetResources(org.springframework.security.core.Authentication authentication) throws java.lang.Exception {
        org.apache.ambari.server.controller.spi.Resource.Type type = org.apache.ambari.server.controller.spi.Resource.Type.HostComponent;
        org.apache.ambari.server.controller.AmbariManagementController managementController = EasyMock.createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.controller.ResourceProviderFactory resourceProviderFactory = EasyMock.createNiceMock(org.apache.ambari.server.controller.ResourceProviderFactory.class);
        org.apache.ambari.server.controller.spi.ResourceProvider hostComponentResourceProvider = EasyMock.createNiceMock(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.class);
        org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.init(resourceProviderFactory);
        java.util.Set<org.apache.ambari.server.controller.ServiceComponentHostResponse> allResponse = new java.util.HashSet<>();
        org.apache.ambari.server.state.StackId stackId = new org.apache.ambari.server.state.StackId("HDP-0.1");
        org.apache.ambari.server.state.StackId stackId2 = new org.apache.ambari.server.state.StackId("HDP-0.2");
        java.lang.String repositoryVersion2 = "0.2-1234";
        allResponse.add(new org.apache.ambari.server.controller.ServiceComponentHostResponse("Cluster100", "Service100", "Component100", "Component 100", "Host100", "Host100", org.apache.ambari.server.state.State.INSTALLED.toString(), stackId.getStackId(), org.apache.ambari.server.state.State.STARTED.toString(), stackId2.getStackId(), repositoryVersion2, null));
        allResponse.add(new org.apache.ambari.server.controller.ServiceComponentHostResponse("Cluster100", "Service100", "Component101", "Component 101", "Host100", "Host100", org.apache.ambari.server.state.State.INSTALLED.toString(), stackId.getStackId(), org.apache.ambari.server.state.State.STARTED.toString(), stackId2.getStackId(), repositoryVersion2, null));
        allResponse.add(new org.apache.ambari.server.controller.ServiceComponentHostResponse("Cluster100", "Service100", "Component102", "Component 102", "Host100", "Host100", org.apache.ambari.server.state.State.INSTALLED.toString(), stackId.getStackId(), org.apache.ambari.server.state.State.STARTED.toString(), stackId2.getStackId(), repositoryVersion2, null));
        java.util.Map<java.lang.String, java.lang.String> expectedNameValues = new java.util.HashMap<>();
        expectedNameValues.put(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.CLUSTER_NAME, "Cluster100");
        expectedNameValues.put(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.STATE, org.apache.ambari.server.state.State.INSTALLED.toString());
        expectedNameValues.put(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.VERSION, repositoryVersion2);
        expectedNameValues.put(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.DESIRED_REPOSITORY_VERSION, repositoryVersion2);
        expectedNameValues.put(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.DESIRED_STATE, org.apache.ambari.server.state.State.STARTED.toString());
        expectedNameValues.put(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.DESIRED_STACK_ID, stackId2.getStackId());
        expectedNameValues.put(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.UPGRADE_STATE, org.apache.ambari.server.state.UpgradeState.NONE.name());
        EasyMock.expect(resourceProviderFactory.getHostComponentResourceProvider(EasyMock.eq(managementController))).andReturn(hostComponentResourceProvider).anyTimes();
        java.util.Set<java.lang.String> propertyIds = new java.util.HashSet<>();
        propertyIds.add(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.CLUSTER_NAME);
        propertyIds.add(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.COMPONENT_NAME);
        propertyIds.add(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.STATE);
        propertyIds.add(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.VERSION);
        propertyIds.add(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.DESIRED_REPOSITORY_VERSION);
        propertyIds.add(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.DESIRED_STATE);
        propertyIds.add(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.DESIRED_STACK_ID);
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.CLUSTER_NAME).equals("Cluster100").toPredicate();
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(propertyIds);
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> hostsComponentResources = new java.util.HashSet<>();
        org.apache.ambari.server.controller.spi.Resource hostsComponentResource1 = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent);
        hostsComponentResource1.setProperty(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.CLUSTER_NAME, "Cluster100");
        hostsComponentResource1.setProperty(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.HOST_NAME, "Host100");
        hostsComponentResource1.setProperty(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.SERVICE_NAME, "Service100");
        hostsComponentResource1.setProperty(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.COMPONENT_NAME, "Component100");
        hostsComponentResource1.setProperty(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.STATE, org.apache.ambari.server.state.State.INSTALLED.name());
        hostsComponentResource1.setProperty(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.DESIRED_STATE, org.apache.ambari.server.state.State.STARTED.name());
        hostsComponentResource1.setProperty(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.VERSION, repositoryVersion2);
        hostsComponentResource1.setProperty(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.DESIRED_STACK_ID, stackId2.getStackId());
        hostsComponentResource1.setProperty(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.UPGRADE_STATE, org.apache.ambari.server.state.UpgradeState.NONE.name());
        hostsComponentResource1.setProperty(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.DESIRED_REPOSITORY_VERSION, repositoryVersion2);
        org.apache.ambari.server.controller.spi.Resource hostsComponentResource2 = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent);
        hostsComponentResource2.setProperty(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.CLUSTER_NAME, "Cluster100");
        hostsComponentResource2.setProperty(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.HOST_NAME, "Host100");
        hostsComponentResource2.setProperty(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.SERVICE_NAME, "Service100");
        hostsComponentResource2.setProperty(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.COMPONENT_NAME, "Component101");
        hostsComponentResource2.setProperty(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.STATE, org.apache.ambari.server.state.State.INSTALLED.name());
        hostsComponentResource2.setProperty(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.DESIRED_STATE, org.apache.ambari.server.state.State.STARTED.name());
        hostsComponentResource2.setProperty(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.VERSION, repositoryVersion2);
        hostsComponentResource2.setProperty(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.DESIRED_STACK_ID, stackId2.getStackId());
        hostsComponentResource2.setProperty(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.UPGRADE_STATE, org.apache.ambari.server.state.UpgradeState.NONE.name());
        hostsComponentResource2.setProperty(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.DESIRED_REPOSITORY_VERSION, repositoryVersion2);
        org.apache.ambari.server.controller.spi.Resource hostsComponentResource3 = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent);
        hostsComponentResource3.setProperty(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.CLUSTER_NAME, "Cluster100");
        hostsComponentResource3.setProperty(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.HOST_NAME, "Host100");
        hostsComponentResource3.setProperty(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.SERVICE_NAME, "Service100");
        hostsComponentResource3.setProperty(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.COMPONENT_NAME, "Component102");
        hostsComponentResource3.setProperty(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.STATE, org.apache.ambari.server.state.State.INSTALLED.name());
        hostsComponentResource3.setProperty(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.DESIRED_STATE, org.apache.ambari.server.state.State.STARTED.name());
        hostsComponentResource3.setProperty(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.VERSION, repositoryVersion2);
        hostsComponentResource3.setProperty(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.DESIRED_STACK_ID, stackId2.getStackId());
        hostsComponentResource3.setProperty(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.UPGRADE_STATE, org.apache.ambari.server.state.UpgradeState.NONE.name());
        hostsComponentResource3.setProperty(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.DESIRED_REPOSITORY_VERSION, repositoryVersion2);
        hostsComponentResources.add(hostsComponentResource1);
        hostsComponentResources.add(hostsComponentResource2);
        hostsComponentResources.add(hostsComponentResource3);
        EasyMock.expect(hostComponentResourceProvider.getResources(EasyMock.eq(request), EasyMock.eq(predicate))).andReturn(hostsComponentResources).anyTimes();
        EasyMock.replay(managementController, resourceProviderFactory, hostComponentResourceProvider);
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        org.apache.ambari.server.controller.spi.ResourceProvider provider = org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.getResourceProvider(type, managementController);
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = provider.getResources(request, predicate);
        org.junit.Assert.assertEquals(3, resources.size());
        java.util.Set<java.lang.String> names = new java.util.HashSet<>();
        for (org.apache.ambari.server.controller.spi.Resource resource : resources) {
            for (java.lang.String key : expectedNameValues.keySet()) {
                org.junit.Assert.assertEquals(expectedNameValues.get(key), resource.getPropertyValue(key));
            }
            names.add(((java.lang.String) (resource.getPropertyValue(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.COMPONENT_NAME))));
        }
        for (org.apache.ambari.server.controller.ServiceComponentHostResponse response : allResponse) {
            org.junit.Assert.assertTrue(names.contains(response.getComponentName()));
        }
        EasyMock.verify(managementController, resourceProviderFactory, hostComponentResourceProvider);
    }

    @org.junit.Test
    public void testUpdateResourcesAsAdministrator() throws java.lang.Exception {
        testUpdateResources(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator());
    }

    @org.junit.Test
    public void testUpdateResourcesAsClusterAdministrator() throws java.lang.Exception {
        testUpdateResources(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator());
    }

    @org.junit.Test
    public void testUpdateResourcesAsServiceAdministrator() throws java.lang.Exception {
        testUpdateResources(org.apache.ambari.server.security.TestAuthenticationFactory.createServiceAdministrator());
    }

    private void testUpdateResources(org.springframework.security.core.Authentication authentication) throws java.lang.Exception {
        org.apache.ambari.server.controller.spi.Resource.Type type = org.apache.ambari.server.controller.spi.Resource.Type.HostComponent;
        org.apache.ambari.server.controller.AmbariManagementController managementController = EasyMock.createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.orm.dao.HostVersionDAO hostVersionDAO = EasyMock.createMock(org.apache.ambari.server.orm.dao.HostVersionDAO.class);
        org.apache.ambari.server.controller.RequestStatusResponse response = EasyMock.createNiceMock(org.apache.ambari.server.controller.RequestStatusResponse.class);
        org.apache.ambari.server.controller.ResourceProviderFactory resourceProviderFactory = EasyMock.createNiceMock(org.apache.ambari.server.controller.ResourceProviderFactory.class);
        com.google.inject.Injector injector = EasyMock.createNiceMock(com.google.inject.Injector.class);
        org.apache.ambari.server.state.Clusters clusters = EasyMock.createNiceMock(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.state.Cluster cluster = EasyMock.createNiceMock(org.apache.ambari.server.state.Cluster.class);
        org.apache.ambari.server.state.Service service = EasyMock.createNiceMock(org.apache.ambari.server.state.Service.class);
        org.apache.ambari.server.state.ServiceComponent component = EasyMock.createNiceMock(org.apache.ambari.server.state.ServiceComponent.class);
        org.apache.ambari.server.state.ServiceComponentHost componentHost = EasyMock.createNiceMock(org.apache.ambari.server.state.ServiceComponentHost.class);
        org.apache.ambari.server.controller.internal.RequestStageContainer stageContainer = EasyMock.createNiceMock(org.apache.ambari.server.controller.internal.RequestStageContainer.class);
        org.apache.ambari.server.controller.MaintenanceStateHelper maintenanceStateHelper = EasyMock.createNiceMock(org.apache.ambari.server.controller.MaintenanceStateHelper.class);
        java.util.Map<java.lang.String, java.lang.String> mapRequestProps = new java.util.HashMap<>();
        mapRequestProps.put("context", "Called from a test");
        java.util.Set<org.apache.ambari.server.controller.ServiceComponentHostResponse> nameResponse = new java.util.HashSet<>();
        nameResponse.add(new org.apache.ambari.server.controller.ServiceComponentHostResponse("Cluster102", "Service100", "Component100", "Component 100", "Host100", "Host100", "INSTALLED", "", "", "", "", null));
        EasyMock.expect(managementController.getClusters()).andReturn(clusters).anyTimes();
        EasyMock.expect(managementController.findServiceName(cluster, "Component100")).andReturn("Service100").anyTimes();
        EasyMock.expect(clusters.getCluster("Cluster102")).andReturn(cluster).anyTimes();
        EasyMock.expect(cluster.getClusterId()).andReturn(2L).anyTimes();
        EasyMock.expect(cluster.getService("Service100")).andReturn(service).anyTimes();
        EasyMock.expect(service.getServiceComponent("Component100")).andReturn(component).anyTimes();
        EasyMock.expect(component.getServiceComponentHost("Host100")).andReturn(componentHost).anyTimes();
        EasyMock.expect(component.getName()).andReturn("Component100").anyTimes();
        EasyMock.expect(componentHost.getState()).andReturn(org.apache.ambari.server.state.State.INSTALLED).anyTimes();
        EasyMock.expect(response.getMessage()).andReturn("response msg").anyTimes();
        EasyMock.expect(response.getRequestId()).andReturn(1000L);
        EasyMock.expect(maintenanceStateHelper.isOperationAllowed(org.apache.ambari.server.controller.spi.Resource.Type.Cluster, componentHost)).andReturn(true).anyTimes();
        EasyMock.expect(managementController.getHostComponents(org.easymock.EasyMock.anyObject())).andReturn(nameResponse).once();
        java.util.Map<java.lang.String, java.util.Map<org.apache.ambari.server.state.State, java.util.List<org.apache.ambari.server.state.ServiceComponentHost>>> changedHosts = new java.util.HashMap<>();
        java.util.List<org.apache.ambari.server.state.ServiceComponentHost> changedComponentHosts = new java.util.ArrayList<>();
        changedComponentHosts.add(componentHost);
        changedHosts.put("Component100", java.util.Collections.singletonMap(org.apache.ambari.server.state.State.STARTED, changedComponentHosts));
        EasyMock.expect(managementController.addStages(null, cluster, mapRequestProps, null, null, null, changedHosts, java.util.Collections.emptyList(), false, false, false, false)).andReturn(stageContainer).once();
        stageContainer.persist();
        EasyMock.expect(stageContainer.getRequestStatusResponse()).andReturn(response).once();
        org.apache.ambari.server.controller.internal.HostComponentResourceProviderTest.TestHostComponentResourceProvider provider = new org.apache.ambari.server.controller.internal.HostComponentResourceProviderTest.TestHostComponentResourceProvider(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyIds(type), org.apache.ambari.server.controller.utilities.PropertyHelper.getKeyPropertyIds(type), managementController, injector);
        provider.setFieldValue("maintenanceStateHelper", maintenanceStateHelper);
        provider.setFieldValue("hostVersionDAO", hostVersionDAO);
        EasyMock.expect(resourceProviderFactory.getHostComponentResourceProvider(EasyMock.eq(managementController))).andReturn(provider).anyTimes();
        EasyMock.replay(managementController, response, resourceProviderFactory, clusters, cluster, service, component, componentHost, stageContainer, maintenanceStateHelper);
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        java.util.Map<java.lang.String, java.lang.Object> properties = new java.util.LinkedHashMap<>();
        properties.put(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.STATE, "STARTED");
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getUpdateRequest(properties, mapRequestProps);
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.CLUSTER_NAME).equals("Cluster102").and().property(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.STATE).equals("INSTALLED").and().property(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.COMPONENT_NAME).equals("Component100").toPredicate();
        org.apache.ambari.server.controller.spi.RequestStatus requestStatus = provider.updateResources(request, predicate);
        org.apache.ambari.server.controller.spi.Resource responseResource = requestStatus.getRequestResource();
        org.junit.Assert.assertEquals("response msg", responseResource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Requests", "message")));
        org.junit.Assert.assertEquals(1000L, responseResource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Requests", "id")));
        org.junit.Assert.assertEquals("Accepted", responseResource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Requests", "status")));
        org.junit.Assert.assertTrue(requestStatus.getAssociatedResources().isEmpty());
        EasyMock.verify(managementController, response, resourceProviderFactory, stageContainer);
    }

    @org.junit.Test
    public void testDeleteResourcesAsAdministrator() throws java.lang.Exception {
        testDeleteResources(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator());
    }

    @org.junit.Test
    public void testDeleteResourcesAsClusterAdministrator() throws java.lang.Exception {
        testDeleteResources(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator());
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testDeleteResourcesAsServiceAdministrator() throws java.lang.Exception {
        testDeleteResources(org.apache.ambari.server.security.TestAuthenticationFactory.createServiceAdministrator());
    }

    private void testDeleteResources(org.springframework.security.core.Authentication authentication) throws java.lang.Exception {
        org.apache.ambari.server.controller.spi.Resource.Type type = org.apache.ambari.server.controller.spi.Resource.Type.HostComponent;
        org.apache.ambari.server.controller.AmbariManagementController managementController = EasyMock.createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.controller.internal.DeleteStatusMetaData deleteStatusMetaData = EasyMock.createNiceMock(org.apache.ambari.server.controller.internal.DeleteStatusMetaData.class);
        com.google.inject.Injector injector = EasyMock.createNiceMock(com.google.inject.Injector.class);
        org.apache.ambari.server.controller.internal.HostComponentResourceProvider provider = new org.apache.ambari.server.controller.internal.HostComponentResourceProvider(managementController);
        EasyMock.expect(managementController.deleteHostComponents(org.apache.ambari.server.controller.internal.AbstractResourceProviderTest.Matcher.getHostComponentRequestSet(null, null, "Component100", "Host100", null, null))).andReturn(deleteStatusMetaData);
        EasyMock.replay(managementController, deleteStatusMetaData);
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        org.apache.ambari.server.controller.internal.AbstractResourceProviderTest.TestObserver observer = new org.apache.ambari.server.controller.internal.AbstractResourceProviderTest.TestObserver();
        provider.addObserver(observer);
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.COMPONENT_NAME).equals("Component100").and().property(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.HOST_NAME).equals("Host100").toPredicate();
        provider.deleteResources(new org.apache.ambari.server.controller.internal.RequestImpl(null, null, null, null), predicate);
        org.apache.ambari.server.controller.internal.ResourceProviderEvent lastEvent = observer.getLastEvent();
        org.junit.Assert.assertNotNull(lastEvent);
        org.junit.Assert.assertEquals(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent, lastEvent.getResourceType());
        org.junit.Assert.assertEquals(org.apache.ambari.server.controller.internal.ResourceProviderEvent.Type.Delete, lastEvent.getType());
        org.junit.Assert.assertEquals(predicate, lastEvent.getPredicate());
        org.junit.Assert.assertNull(lastEvent.getRequest());
        EasyMock.verify(managementController, deleteStatusMetaData);
    }

    @org.junit.Test
    public void testCheckPropertyIds() throws java.lang.Exception {
        org.apache.ambari.server.controller.AmbariManagementController managementController = EasyMock.createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        com.google.inject.Injector injector = EasyMock.createNiceMock(com.google.inject.Injector.class);
        org.apache.ambari.server.controller.internal.HostComponentResourceProvider provider = new org.apache.ambari.server.controller.internal.HostComponentResourceProvider(managementController);
        java.util.Set<java.lang.String> unsupported = provider.checkPropertyIds(java.util.Collections.singleton(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "cluster_name")));
        org.junit.Assert.assertTrue(unsupported.isEmpty());
        org.junit.Assert.assertTrue(provider.checkPropertyIds(java.util.Collections.singleton(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles/service_name", "key"))).isEmpty());
        unsupported = provider.checkPropertyIds(java.util.Collections.singleton("bar"));
        org.junit.Assert.assertEquals(1, unsupported.size());
        org.junit.Assert.assertTrue(unsupported.contains("bar"));
        unsupported = provider.checkPropertyIds(java.util.Collections.singleton(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "component_name")));
        org.junit.Assert.assertTrue(unsupported.isEmpty());
        unsupported = provider.checkPropertyIds(java.util.Collections.singleton("HostRoles"));
        org.junit.Assert.assertTrue(unsupported.isEmpty());
        unsupported = provider.checkPropertyIds(java.util.Collections.singleton("config"));
        org.junit.Assert.assertTrue(unsupported.isEmpty());
        unsupported = provider.checkPropertyIds(java.util.Collections.singleton("config/unknown_property"));
        org.junit.Assert.assertTrue(unsupported.isEmpty());
    }

    @org.junit.Test
    public void testUpdateResourcesNothingToUpdate() throws java.lang.Exception {
        org.springframework.security.core.Authentication authentication = org.apache.ambari.server.security.TestAuthenticationFactory.createServiceAdministrator();
        org.apache.ambari.server.controller.spi.Resource.Type type = org.apache.ambari.server.controller.spi.Resource.Type.HostComponent;
        org.apache.ambari.server.controller.AmbariManagementController managementController = EasyMock.createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.orm.dao.HostVersionDAO hostVersionDAO = EasyMock.createMock(org.apache.ambari.server.orm.dao.HostVersionDAO.class);
        org.apache.ambari.server.controller.ResourceProviderFactory resourceProviderFactory = EasyMock.createNiceMock(org.apache.ambari.server.controller.ResourceProviderFactory.class);
        com.google.inject.Injector injector = EasyMock.createNiceMock(com.google.inject.Injector.class);
        org.apache.ambari.server.state.Clusters clusters = EasyMock.createNiceMock(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.state.Cluster cluster = EasyMock.createNiceMock(org.apache.ambari.server.state.Cluster.class);
        org.apache.ambari.server.state.Service service = EasyMock.createNiceMock(org.apache.ambari.server.state.Service.class);
        org.apache.ambari.server.state.ServiceComponent component = EasyMock.createNiceMock(org.apache.ambari.server.state.ServiceComponent.class);
        org.apache.ambari.server.state.ServiceComponentHost componentHost = EasyMock.createNiceMock(org.apache.ambari.server.state.ServiceComponentHost.class);
        org.apache.ambari.server.controller.internal.RequestStageContainer stageContainer = EasyMock.createNiceMock(org.apache.ambari.server.controller.internal.RequestStageContainer.class);
        org.apache.ambari.server.controller.MaintenanceStateHelper maintenanceStateHelper = EasyMock.createNiceMock(org.apache.ambari.server.controller.MaintenanceStateHelper.class);
        java.util.Map<java.lang.String, java.lang.String> mapRequestProps = new java.util.HashMap<>();
        mapRequestProps.put("context", "Called from a test");
        java.util.Set<org.apache.ambari.server.controller.ServiceComponentHostResponse> nameResponse = new java.util.HashSet<>();
        nameResponse.add(new org.apache.ambari.server.controller.ServiceComponentHostResponse("Cluster102", "Service100", "Component100", "Component 100", "Host100", "Host100", "INSTALLED", "", "", "", "", null));
        EasyMock.expect(managementController.getClusters()).andReturn(clusters).anyTimes();
        EasyMock.expect(managementController.findServiceName(cluster, "Component100")).andReturn("Service100").anyTimes();
        EasyMock.expect(clusters.getCluster("Cluster102")).andReturn(cluster).anyTimes();
        EasyMock.expect(cluster.getClusterId()).andReturn(2L).anyTimes();
        EasyMock.expect(cluster.getService("Service100")).andReturn(service).anyTimes();
        EasyMock.expect(service.getServiceComponent("Component100")).andReturn(component).anyTimes();
        EasyMock.expect(component.getServiceComponentHost("Host100")).andReturn(componentHost).anyTimes();
        EasyMock.expect(component.getName()).andReturn("Component100").anyTimes();
        EasyMock.expect(componentHost.getState()).andReturn(org.apache.ambari.server.state.State.INSTALLED).anyTimes();
        EasyMock.expect(maintenanceStateHelper.isOperationAllowed(org.apache.ambari.server.controller.spi.Resource.Type.Cluster, componentHost)).andReturn(true).anyTimes();
        EasyMock.expect(managementController.getHostComponents(org.easymock.EasyMock.anyObject())).andReturn(java.util.Collections.emptySet()).once();
        java.util.Map<java.lang.String, java.util.Map<org.apache.ambari.server.state.State, java.util.List<org.apache.ambari.server.state.ServiceComponentHost>>> changedHosts = new java.util.HashMap<>();
        java.util.List<org.apache.ambari.server.state.ServiceComponentHost> changedComponentHosts = new java.util.ArrayList<>();
        changedComponentHosts.add(componentHost);
        changedHosts.put("Component100", java.util.Collections.singletonMap(org.apache.ambari.server.state.State.STARTED, changedComponentHosts));
        org.apache.ambari.server.controller.internal.HostComponentResourceProviderTest.TestHostComponentResourceProvider provider = new org.apache.ambari.server.controller.internal.HostComponentResourceProviderTest.TestHostComponentResourceProvider(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyIds(type), org.apache.ambari.server.controller.utilities.PropertyHelper.getKeyPropertyIds(type), managementController, injector);
        provider.setFieldValue("maintenanceStateHelper", maintenanceStateHelper);
        provider.setFieldValue("hostVersionDAO", hostVersionDAO);
        EasyMock.expect(resourceProviderFactory.getHostComponentResourceProvider(EasyMock.eq(managementController))).andReturn(provider).anyTimes();
        EasyMock.replay(managementController, resourceProviderFactory, clusters, cluster, service, component, componentHost, stageContainer, maintenanceStateHelper);
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        java.util.Map<java.lang.String, java.lang.Object> properties = new java.util.LinkedHashMap<>();
        properties.put(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.STATE, "STARTED");
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getUpdateRequest(properties, mapRequestProps);
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.CLUSTER_NAME).equals("Cluster102").and().property(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.STATE).equals("INSTALLED").and().property(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.COMPONENT_NAME).equals("Component100").toPredicate();
        try {
            provider.updateResources(request, predicate);
            org.junit.Assert.fail("Expected exception when no resources are found to be updatable");
        } catch (org.apache.ambari.server.controller.spi.NoSuchResourceException e) {
        }
        EasyMock.verify(managementController, resourceProviderFactory, stageContainer);
    }

    @org.junit.Test
    public void doesNotSkipInstallTaskForClient() {
        java.lang.String component = "SOME_COMPONENT";
        org.junit.Assert.assertFalse(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.shouldSkipInstallTaskForComponent(component, true, new org.apache.ambari.server.controller.internal.HostComponentResourceProviderTest.RequestInfoBuilder().skipInstall(component).build()));
        org.junit.Assert.assertFalse(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.shouldSkipInstallTaskForComponent(component, true, new org.apache.ambari.server.controller.internal.HostComponentResourceProviderTest.RequestInfoBuilder().skipInstall(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.ALL_COMPONENTS).build()));
    }

    @org.junit.Test
    public void doesNotSkipInstallTaskForOtherPhase() {
        java.lang.String component = "SOME_COMPONENT";
        org.apache.ambari.server.controller.internal.HostComponentResourceProviderTest.RequestInfoBuilder requestInfoBuilder = new org.apache.ambari.server.controller.internal.HostComponentResourceProviderTest.RequestInfoBuilder().phase("INSTALL");
        org.junit.Assert.assertFalse(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.shouldSkipInstallTaskForComponent(component, false, requestInfoBuilder.skipInstall(component).build()));
        org.junit.Assert.assertFalse(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.shouldSkipInstallTaskForComponent(component, false, requestInfoBuilder.skipInstall(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.ALL_COMPONENTS).build()));
    }

    @org.junit.Test
    public void doesNotSkipInstallTaskForExplicitException() {
        java.lang.String component = "SOME_COMPONENT";
        org.apache.ambari.server.controller.internal.HostComponentResourceProviderTest.RequestInfoBuilder requestInfoBuilder = new org.apache.ambari.server.controller.internal.HostComponentResourceProviderTest.RequestInfoBuilder().skipInstall(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.ALL_COMPONENTS).doNotSkipInstall(component);
        org.junit.Assert.assertFalse(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.shouldSkipInstallTaskForComponent(component, false, requestInfoBuilder.build()));
    }

    @org.junit.Test
    public void skipsInstallTaskIfRequested() {
        java.lang.String component = "SOME_COMPONENT";
        org.apache.ambari.server.controller.internal.HostComponentResourceProviderTest.RequestInfoBuilder requestInfoBuilder = new org.apache.ambari.server.controller.internal.HostComponentResourceProviderTest.RequestInfoBuilder().skipInstall(component);
        org.junit.Assert.assertTrue(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.shouldSkipInstallTaskForComponent(component, false, requestInfoBuilder.build()));
    }

    @org.junit.Test
    public void skipsInstallTaskForAll() {
        org.apache.ambari.server.controller.internal.HostComponentResourceProviderTest.RequestInfoBuilder requestInfoBuilder = new org.apache.ambari.server.controller.internal.HostComponentResourceProviderTest.RequestInfoBuilder().skipInstall(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.ALL_COMPONENTS);
        org.junit.Assert.assertTrue(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.shouldSkipInstallTaskForComponent("ANY_COMPONENT", false, requestInfoBuilder.build()));
    }

    @org.junit.Test
    public void doesNotSkipInstallOfPrefixedComponent() {
        java.lang.String prefix = "HIVE_SERVER";
        java.lang.String component = prefix + "_INTERACTIVE";
        java.util.Map<java.lang.String, java.lang.String> requestInfo = new org.apache.ambari.server.controller.internal.HostComponentResourceProviderTest.RequestInfoBuilder().skipInstall(component).build();
        org.junit.Assert.assertTrue(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.shouldSkipInstallTaskForComponent(component, false, requestInfo));
        org.junit.Assert.assertFalse(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.shouldSkipInstallTaskForComponent(prefix, false, requestInfo));
    }

    private static class RequestInfoBuilder {
        private java.lang.String phase = org.apache.ambari.server.controller.AmbariManagementControllerImpl.CLUSTER_PHASE_INITIAL_INSTALL;

        private final java.util.Collection<java.lang.String> skipInstall = new java.util.LinkedList<>();

        private final java.util.Collection<java.lang.String> doNotSkipInstall = new java.util.LinkedList<>();

        public org.apache.ambari.server.controller.internal.HostComponentResourceProviderTest.RequestInfoBuilder skipInstall(java.lang.String... components) {
            skipInstall.clear();
            skipInstall.addAll(java.util.Arrays.asList(components));
            return this;
        }

        public org.apache.ambari.server.controller.internal.HostComponentResourceProviderTest.RequestInfoBuilder doNotSkipInstall(java.lang.String... components) {
            doNotSkipInstall.clear();
            doNotSkipInstall.addAll(java.util.Arrays.asList(components));
            return this;
        }

        public org.apache.ambari.server.controller.internal.HostComponentResourceProviderTest.RequestInfoBuilder phase(java.lang.String phase) {
            this.phase = phase;
            return this;
        }

        public java.util.Map<java.lang.String, java.lang.String> build() {
            java.util.Map<java.lang.String, java.lang.String> info = new java.util.HashMap<>();
            if (phase != null) {
                info.put(org.apache.ambari.server.controller.AmbariManagementControllerImpl.CLUSTER_PHASE_PROPERTY, phase);
            }
            org.apache.ambari.server.controller.internal.HostComponentResourceProvider.addProvisionActionProperties(skipInstall, doNotSkipInstall, info);
            return info;
        }
    }

    public static org.apache.ambari.server.controller.RequestStatusResponse updateHostComponents(org.apache.ambari.server.controller.AmbariManagementController controller, com.google.inject.Injector injector, java.util.Set<org.apache.ambari.server.controller.ServiceComponentHostRequest> requests, java.util.Map<java.lang.String, java.lang.String> requestProperties, boolean runSmokeTest) throws java.lang.Exception {
        org.apache.ambari.server.controller.spi.Resource.Type type = org.apache.ambari.server.controller.spi.Resource.Type.HostComponent;
        org.apache.ambari.server.controller.internal.HostComponentResourceProviderTest.TestHostComponentResourceProvider provider = new org.apache.ambari.server.controller.internal.HostComponentResourceProviderTest.TestHostComponentResourceProvider(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyIds(type), org.apache.ambari.server.controller.utilities.PropertyHelper.getKeyPropertyIds(type), controller, injector);
        provider.setFieldValue("maintenanceStateHelper", injector.getInstance(org.apache.ambari.server.controller.MaintenanceStateHelper.class));
        provider.setFieldValue("hostVersionDAO", injector.getInstance(org.apache.ambari.server.orm.dao.HostVersionDAO.class));
        org.apache.ambari.server.controller.internal.RequestStageContainer requestStages = provider.updateHostComponents(null, requests, requestProperties, runSmokeTest, false, false);
        requestStages.persist();
        return requestStages.getRequestStatusResponse();
    }

    private static class TestHostComponentResourceProvider extends org.apache.ambari.server.controller.internal.HostComponentResourceProvider {
        public TestHostComponentResourceProvider(java.util.Set<java.lang.String> propertyIds, java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> keyPropertyIds, org.apache.ambari.server.controller.AmbariManagementController managementController, com.google.inject.Injector injector) throws java.lang.Exception {
            super(managementController);
        }

        public void setFieldValue(java.lang.String fieldName, java.lang.Object fieldValue) throws java.lang.Exception {
            java.lang.Class<?> c = getClass().getSuperclass();
            java.lang.reflect.Field f = c.getDeclaredField(fieldName);
            f.setAccessible(true);
            f.set(this, fieldValue);
        }
    }
}