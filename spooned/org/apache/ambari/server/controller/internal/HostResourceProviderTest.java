package org.apache.ambari.server.controller.internal;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.persist.UnitOfWork;
import javax.persistence.EntityManager;
import org.easymock.Capture;
import org.easymock.EasyMock;
import org.easymock.EasyMockSupport;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import static org.easymock.EasyMock.anyLong;
import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.reset;
public class HostResourceProviderTest extends org.easymock.EasyMockSupport {
    @org.junit.After
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
        org.apache.ambari.server.security.authorization.AuthorizationHelperInitializer.viewInstanceDAOReturningNull();
        org.apache.ambari.server.controller.spi.Resource.Type type = org.apache.ambari.server.controller.spi.Resource.Type.Host;
        com.google.inject.Injector injector = createInjector();
        org.apache.ambari.server.controller.AmbariManagementController managementController = injector.getInstance(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.easymock.Capture<java.lang.String> rackChangeAffectedClusterName = org.easymock.EasyMock.newCapture();
        managementController.registerRackChange(EasyMock.capture(rackChangeAffectedClusterName));
        org.easymock.EasyMock.expectLastCall().once();
        EasyMock.expect(managementController.getBlueprintProvisioningStates(EasyMock.anyLong(), EasyMock.anyLong())).andReturn(java.util.Collections.EMPTY_MAP).anyTimes();
        org.apache.ambari.server.state.Clusters clusters = injector.getInstance(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.state.Cluster cluster = createMock(org.apache.ambari.server.state.Cluster.class);
        org.apache.ambari.server.state.Host host = createMock(org.apache.ambari.server.state.Host.class);
        org.apache.ambari.server.controller.ResourceProviderFactory resourceProviderFactory = createNiceMock(org.apache.ambari.server.controller.ResourceProviderFactory.class);
        org.apache.ambari.server.controller.spi.ResourceProvider hostResourceProvider = getHostProvider(injector);
        org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.init(resourceProviderFactory);
        EasyMock.expect(cluster.getClusterId()).andReturn(2L).anyTimes();
        EasyMock.expect(cluster.getClusterName()).andReturn("Cluster100").anyTimes();
        EasyMock.expect(cluster.getResourceId()).andReturn(4L).anyTimes();
        EasyMock.expect(cluster.getDesiredConfigs()).andReturn(new java.util.HashMap<>()).anyTimes();
        EasyMock.expect(clusters.getCluster("Cluster100")).andReturn(cluster).atLeastOnce();
        EasyMock.expect(clusters.getHost("Host100")).andReturn(host).atLeastOnce();
        clusters.updateHostWithClusterAndAttributes(org.easymock.EasyMock.anyObject(), org.easymock.EasyMock.anyObject());
        org.easymock.EasyMock.expectLastCall().once();
        EasyMock.expect(host.getRackInfo()).andReturn("/default-rack").anyTimes();
        EasyMock.expect(host.getHostId()).andReturn(1L).anyTimes();
        EasyMock.expect(host.getHostName()).andReturn("Host100").anyTimes();
        EasyMock.expect(host.getIPv4()).andReturn("0.0.0.0").anyTimes();
        org.easymock.Capture<java.lang.String> newRack = org.easymock.EasyMock.newCapture();
        host.setRackInfo(EasyMock.capture(newRack));
        org.easymock.EasyMock.expectLastCall().once();
        EasyMock.expect(managementController.getClusters()).andReturn(clusters).atLeastOnce();
        EasyMock.expect(resourceProviderFactory.getHostResourceProvider(EasyMock.eq(managementController))).andReturn(hostResourceProvider).anyTimes();
        replayAll();
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        org.apache.ambari.server.controller.spi.ResourceProvider provider = org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.getResourceProvider(type, managementController);
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> propertySet = new java.util.LinkedHashSet<>();
        java.util.Map<java.lang.String, java.lang.Object> properties = new java.util.LinkedHashMap<>();
        properties.put(org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_CLUSTER_NAME_PROPERTY_ID, "Cluster100");
        properties.put(org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_HOST_NAME_PROPERTY_ID, "Host100");
        properties.put(org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_RACK_INFO_PROPERTY_ID, "/test-rack");
        propertySet.add(properties);
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getCreateRequest(propertySet, null);
        provider.createResources(request);
        verifyAll();
        org.junit.Assert.assertEquals("Cluster100", rackChangeAffectedClusterName.getValue());
        org.junit.Assert.assertEquals("/test-rack", newRack.getValue());
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
        org.apache.ambari.server.security.authorization.AuthorizationHelperInitializer.viewInstanceDAOReturningNull();
        org.apache.ambari.server.controller.spi.Resource.Type type = org.apache.ambari.server.controller.spi.Resource.Type.Host;
        com.google.inject.Injector injector = createInjector();
        org.apache.ambari.server.controller.AmbariManagementController managementController = injector.getInstance(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.state.Clusters clusters = injector.getInstance(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.state.Cluster cluster = createMock(org.apache.ambari.server.state.Cluster.class);
        org.apache.ambari.server.state.Host host100 = createMockHost("Host100", "Cluster100", null, "HEALTHY", "RECOVERABLE", null);
        org.apache.ambari.server.state.Host host101 = createMockHost("Host101", "Cluster100", null, "HEALTHY", "RECOVERABLE", null);
        org.apache.ambari.server.state.Host host102 = createMockHost("Host102", "Cluster100", null, "HEALTHY", "RECOVERABLE", null);
        org.apache.ambari.server.state.HostHealthStatus healthStatus = createNiceMock(org.apache.ambari.server.state.HostHealthStatus.class);
        org.apache.ambari.server.api.services.AmbariMetaInfo ambariMetaInfo = createNiceMock(org.apache.ambari.server.api.services.AmbariMetaInfo.class);
        org.apache.ambari.server.controller.ResourceProviderFactory resourceProviderFactory = createNiceMock(org.apache.ambari.server.controller.ResourceProviderFactory.class);
        org.apache.ambari.server.controller.spi.ResourceProvider hostResourceProvider = getHostProvider(injector);
        org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.init(resourceProviderFactory);
        java.util.List<org.apache.ambari.server.state.Host> hosts = new java.util.LinkedList<>();
        hosts.add(host100);
        hosts.add(host101);
        hosts.add(host102);
        java.util.Set<org.apache.ambari.server.state.Cluster> clusterSet = new java.util.HashSet<>();
        clusterSet.add(cluster);
        EasyMock.expect(host100.getMaintenanceState(2)).andReturn(org.apache.ambari.server.state.MaintenanceState.OFF).anyTimes();
        EasyMock.expect(host101.getMaintenanceState(2)).andReturn(org.apache.ambari.server.state.MaintenanceState.OFF).anyTimes();
        EasyMock.expect(host102.getMaintenanceState(2)).andReturn(org.apache.ambari.server.state.MaintenanceState.OFF).anyTimes();
        EasyMock.expect(managementController.getClusters()).andReturn(clusters).anyTimes();
        EasyMock.expect(managementController.getAmbariMetaInfo()).andReturn(ambariMetaInfo).anyTimes();
        EasyMock.expect(managementController.getHostComponents(org.easymock.EasyMock.anyObject())).andReturn(java.util.Collections.emptySet()).anyTimes();
        EasyMock.expect(resourceProviderFactory.getHostResourceProvider(EasyMock.eq(managementController))).andReturn(hostResourceProvider).anyTimes();
        EasyMock.expect(clusters.getHosts()).andReturn(hosts).anyTimes();
        EasyMock.expect(clusters.getCluster("Cluster100")).andReturn(cluster).anyTimes();
        EasyMock.expect(clusters.getClustersForHost("Host100")).andReturn(clusterSet).anyTimes();
        EasyMock.expect(clusters.getClustersForHost("Host101")).andReturn(clusterSet).anyTimes();
        EasyMock.expect(clusters.getClustersForHost("Host102")).andReturn(clusterSet).anyTimes();
        EasyMock.expect(cluster.getClusterId()).andReturn(2L).anyTimes();
        EasyMock.expect(cluster.getDesiredConfigs()).andReturn(new java.util.HashMap<>()).anyTimes();
        EasyMock.expect(healthStatus.getHealthStatus()).andReturn(org.apache.ambari.server.state.HostHealthStatus.HealthStatus.HEALTHY).anyTimes();
        EasyMock.expect(healthStatus.getHealthReport()).andReturn("HEALTHY").anyTimes();
        java.util.Set<java.lang.String> propertyIds = new java.util.HashSet<>();
        propertyIds.add(org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_CLUSTER_NAME_PROPERTY_ID);
        propertyIds.add(org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_HOST_NAME_PROPERTY_ID);
        propertyIds.add(org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_MAINTENANCE_STATE_PROPERTY_ID);
        org.apache.ambari.server.controller.spi.Predicate predicate = buildPredicate("Cluster100", null);
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(propertyIds);
        replayAll();
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        org.apache.ambari.server.controller.spi.ResourceProvider provider = org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.getResourceProvider(type, managementController);
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = provider.getResources(request, predicate);
        org.junit.Assert.assertEquals(3, resources.size());
        for (org.apache.ambari.server.controller.spi.Resource resource : resources) {
            java.lang.String clusterName = ((java.lang.String) (resource.getPropertyValue(org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_CLUSTER_NAME_PROPERTY_ID)));
            org.junit.Assert.assertEquals("Cluster100", clusterName);
            org.apache.ambari.server.state.MaintenanceState maintenanceState = ((org.apache.ambari.server.state.MaintenanceState) (resource.getPropertyValue(org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_MAINTENANCE_STATE_PROPERTY_ID)));
            org.junit.Assert.assertEquals(org.apache.ambari.server.state.MaintenanceState.OFF, maintenanceState);
        }
        verifyAll();
    }

    @org.junit.Test
    public void testGetResources_Status_NoCluster() throws java.lang.Exception {
        org.apache.ambari.server.controller.spi.Resource.Type type = org.apache.ambari.server.controller.spi.Resource.Type.Host;
        com.google.inject.Injector injector = createInjector();
        org.apache.ambari.server.controller.AmbariManagementController managementController = injector.getInstance(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.state.Clusters clusters = injector.getInstance(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.state.Cluster cluster = createMock(org.apache.ambari.server.state.Cluster.class);
        org.apache.ambari.server.state.HostHealthStatus healthStatus = createNiceMock(org.apache.ambari.server.state.HostHealthStatus.class);
        org.apache.ambari.server.api.services.AmbariMetaInfo ambariMetaInfo = createNiceMock(org.apache.ambari.server.api.services.AmbariMetaInfo.class);
        org.apache.ambari.server.state.ComponentInfo componentInfo = createNiceMock(org.apache.ambari.server.state.ComponentInfo.class);
        org.apache.ambari.server.state.Host host100 = createMockHost("Host100", "Cluster100", null, "HEALTHY", "RECOVERABLE", null);
        org.apache.ambari.server.controller.HostResponse hostResponse1 = createNiceMock(org.apache.ambari.server.controller.HostResponse.class);
        org.apache.ambari.server.controller.ResourceProviderFactory resourceProviderFactory = createNiceMock(org.apache.ambari.server.controller.ResourceProviderFactory.class);
        org.apache.ambari.server.controller.spi.ResourceProvider hostResourceProvider = getHostProvider(injector);
        org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.init(resourceProviderFactory);
        java.util.Set<org.apache.ambari.server.state.Cluster> clusterSet = new java.util.HashSet<>();
        clusterSet.add(cluster);
        org.apache.ambari.server.controller.ServiceComponentHostResponse shr1 = new org.apache.ambari.server.controller.ServiceComponentHostResponse("Cluster100", "Service100", "Component100", "Component 100", "Host100", "Host100", "STARTED", "", null, null, null, null);
        org.apache.ambari.server.controller.ServiceComponentHostResponse shr2 = new org.apache.ambari.server.controller.ServiceComponentHostResponse("Cluster100", "Service100", "Component102", "Component 102", "Host100", "Host100", "STARTED", "", null, null, null, null);
        org.apache.ambari.server.controller.ServiceComponentHostResponse shr3 = new org.apache.ambari.server.controller.ServiceComponentHostResponse("Cluster100", "Service100", "Component103", "Component 103", "Host100", "Host100", "STARTED", "", null, null, null, null);
        java.util.Set<org.apache.ambari.server.controller.ServiceComponentHostResponse> responses = new java.util.HashSet<>();
        responses.add(shr1);
        responses.add(shr2);
        responses.add(shr3);
        EasyMock.expect(host100.getMaintenanceState(2)).andReturn(org.apache.ambari.server.state.MaintenanceState.OFF).anyTimes();
        EasyMock.expect(managementController.getClusters()).andReturn(clusters).anyTimes();
        EasyMock.expect(managementController.getAmbariMetaInfo()).andReturn(ambariMetaInfo).anyTimes();
        EasyMock.expect(managementController.getHostComponents(org.easymock.EasyMock.anyObject())).andReturn(responses).anyTimes();
        EasyMock.expect(clusters.getCluster("Cluster100")).andReturn(cluster).anyTimes();
        EasyMock.expect(clusters.getClustersForHost("Host100")).andReturn(clusterSet).anyTimes();
        EasyMock.expect(clusters.getHosts()).andReturn(java.util.Arrays.asList(host100)).anyTimes();
        EasyMock.expect(cluster.getClusterId()).andReturn(2L).anyTimes();
        EasyMock.expect(cluster.getDesiredConfigs()).andReturn(new java.util.HashMap<>()).anyTimes();
        EasyMock.expect(hostResponse1.getClusterName()).andReturn("").anyTimes();
        EasyMock.expect(hostResponse1.getHostname()).andReturn("Host100").anyTimes();
        EasyMock.expect(hostResponse1.getStatus()).andReturn(org.apache.ambari.server.state.HostHealthStatus.HealthStatus.HEALTHY.name()).anyTimes();
        EasyMock.expect(hostResponse1.getHealthReport()).andReturn("HEALTHY").anyTimes();
        EasyMock.expect(ambariMetaInfo.getComponent(((java.lang.String) (EasyMock.anyObject())), ((java.lang.String) (EasyMock.anyObject())), ((java.lang.String) (EasyMock.anyObject())), ((java.lang.String) (EasyMock.anyObject())))).andReturn(componentInfo).anyTimes();
        EasyMock.expect(componentInfo.getCategory()).andReturn("MASTER").anyTimes();
        EasyMock.expect(resourceProviderFactory.getHostResourceProvider(EasyMock.eq(managementController))).andReturn(hostResourceProvider).anyTimes();
        java.util.Set<java.lang.String> propertyIds = new java.util.HashSet<>();
        propertyIds.add(org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_CLUSTER_NAME_PROPERTY_ID);
        propertyIds.add(org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_HOST_NAME_PROPERTY_ID);
        propertyIds.add(org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_HOST_STATUS_PROPERTY_ID);
        org.apache.ambari.server.controller.spi.Predicate predicate = buildPredicate("Cluster100", null);
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(propertyIds);
        replayAll();
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator());
        org.apache.ambari.server.controller.spi.ResourceProvider provider = org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.getResourceProvider(type, managementController);
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = provider.getResources(request, predicate);
        org.junit.Assert.assertEquals(1, resources.size());
        for (org.apache.ambari.server.controller.spi.Resource resource : resources) {
            java.lang.String clusterName = ((java.lang.String) (resource.getPropertyValue(org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_CLUSTER_NAME_PROPERTY_ID)));
            org.junit.Assert.assertEquals("Cluster100", clusterName);
            java.lang.String status = ((java.lang.String) (resource.getPropertyValue(org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_HOST_STATUS_PROPERTY_ID)));
            org.junit.Assert.assertEquals("HEALTHY", status);
        }
        verifyAll();
    }

    @org.junit.Test
    public void testGetResources_Status_Healthy() throws java.lang.Exception {
        org.apache.ambari.server.controller.spi.Resource.Type type = org.apache.ambari.server.controller.spi.Resource.Type.Host;
        com.google.inject.Injector injector = createInjector();
        org.apache.ambari.server.controller.AmbariManagementController managementController = injector.getInstance(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.state.Clusters clusters = injector.getInstance(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.state.Cluster cluster = createMock(org.apache.ambari.server.state.Cluster.class);
        org.apache.ambari.server.state.HostHealthStatus healthStatus = createNiceMock(org.apache.ambari.server.state.HostHealthStatus.class);
        org.apache.ambari.server.api.services.AmbariMetaInfo ambariMetaInfo = createNiceMock(org.apache.ambari.server.api.services.AmbariMetaInfo.class);
        org.apache.ambari.server.state.ComponentInfo componentInfo = createNiceMock(org.apache.ambari.server.state.ComponentInfo.class);
        org.apache.ambari.server.state.Host host100 = createMockHost("Host100", "Cluster100", null, "HEALTHY", "RECOVERABLE", null);
        org.apache.ambari.server.controller.ResourceProviderFactory resourceProviderFactory = createNiceMock(org.apache.ambari.server.controller.ResourceProviderFactory.class);
        org.apache.ambari.server.controller.spi.ResourceProvider hostResourceProvider = getHostProvider(injector);
        org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.init(resourceProviderFactory);
        java.util.Set<org.apache.ambari.server.state.Cluster> clusterSet = new java.util.HashSet<>();
        clusterSet.add(cluster);
        org.apache.ambari.server.controller.ServiceComponentHostResponse shr1 = new org.apache.ambari.server.controller.ServiceComponentHostResponse("Cluster100", "Service100", "Component100", "Component 100", "Host100", "Host100", "STARTED", "", null, null, null, null);
        org.apache.ambari.server.controller.ServiceComponentHostResponse shr2 = new org.apache.ambari.server.controller.ServiceComponentHostResponse("Cluster100", "Service100", "Component102", "Component 102", "Host100", "Host100", "STARTED", "", null, null, null, null);
        org.apache.ambari.server.controller.ServiceComponentHostResponse shr3 = new org.apache.ambari.server.controller.ServiceComponentHostResponse("Cluster100", "Service100", "Component103", "Component 103", "Host100", "Host100", "STARTED", "", null, null, null, null);
        java.util.Set<org.apache.ambari.server.controller.ServiceComponentHostResponse> responses = new java.util.HashSet<>();
        responses.add(shr1);
        responses.add(shr2);
        responses.add(shr3);
        EasyMock.expect(managementController.getClusters()).andReturn(clusters).anyTimes();
        EasyMock.expect(managementController.getAmbariMetaInfo()).andReturn(ambariMetaInfo).anyTimes();
        EasyMock.expect(managementController.getHostComponents(org.easymock.EasyMock.anyObject())).andReturn(responses).anyTimes();
        EasyMock.expect(host100.getMaintenanceState(2)).andReturn(org.apache.ambari.server.state.MaintenanceState.OFF).anyTimes();
        EasyMock.expect(clusters.getCluster("Cluster100")).andReturn(cluster).anyTimes();
        EasyMock.expect(clusters.getClustersForHost("Host100")).andReturn(clusterSet).anyTimes();
        EasyMock.expect(clusters.getHosts()).andReturn(java.util.Arrays.asList(host100)).anyTimes();
        EasyMock.expect(cluster.getClusterId()).andReturn(2L).anyTimes();
        EasyMock.expect(cluster.getDesiredConfigs()).andReturn(new java.util.HashMap<>()).anyTimes();
        EasyMock.expect(healthStatus.getHealthStatus()).andReturn(org.apache.ambari.server.state.HostHealthStatus.HealthStatus.HEALTHY).anyTimes();
        EasyMock.expect(healthStatus.getHealthReport()).andReturn("HEALTHY").anyTimes();
        EasyMock.expect(ambariMetaInfo.getComponent(((java.lang.String) (EasyMock.anyObject())), ((java.lang.String) (EasyMock.anyObject())), ((java.lang.String) (EasyMock.anyObject())), ((java.lang.String) (EasyMock.anyObject())))).andReturn(componentInfo).anyTimes();
        EasyMock.expect(componentInfo.getCategory()).andReturn("MASTER").anyTimes();
        EasyMock.expect(resourceProviderFactory.getHostResourceProvider(EasyMock.eq(managementController))).andReturn(hostResourceProvider).anyTimes();
        java.util.Set<java.lang.String> propertyIds = new java.util.HashSet<>();
        propertyIds.add(org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_CLUSTER_NAME_PROPERTY_ID);
        propertyIds.add(org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_HOST_NAME_PROPERTY_ID);
        propertyIds.add(org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_HOST_STATUS_PROPERTY_ID);
        org.apache.ambari.server.controller.spi.Predicate predicate = buildPredicate("Cluster100", null);
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(propertyIds);
        replayAll();
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator());
        org.apache.ambari.server.controller.spi.ResourceProvider provider = org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.getResourceProvider(type, managementController);
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = provider.getResources(request, predicate);
        org.junit.Assert.assertEquals(1, resources.size());
        for (org.apache.ambari.server.controller.spi.Resource resource : resources) {
            java.lang.String clusterName = ((java.lang.String) (resource.getPropertyValue(org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_CLUSTER_NAME_PROPERTY_ID)));
            org.junit.Assert.assertEquals("Cluster100", clusterName);
            java.lang.String status = ((java.lang.String) (resource.getPropertyValue(org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_HOST_STATUS_PROPERTY_ID)));
            org.junit.Assert.assertEquals("HEALTHY", status);
        }
        verifyAll();
    }

    @org.junit.Test
    public void testGetResources_Status_Unhealthy() throws java.lang.Exception {
        org.apache.ambari.server.controller.spi.Resource.Type type = org.apache.ambari.server.controller.spi.Resource.Type.Host;
        com.google.inject.Injector injector = createInjector();
        org.apache.ambari.server.controller.AmbariManagementController managementController = injector.getInstance(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.state.Clusters clusters = injector.getInstance(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.state.Cluster cluster = createMock(org.apache.ambari.server.state.Cluster.class);
        org.apache.ambari.server.api.services.AmbariMetaInfo ambariMetaInfo = createNiceMock(org.apache.ambari.server.api.services.AmbariMetaInfo.class);
        org.apache.ambari.server.state.ComponentInfo componentInfo = createNiceMock(org.apache.ambari.server.state.ComponentInfo.class);
        org.apache.ambari.server.controller.HostResponse hostResponse1 = createNiceMock(org.apache.ambari.server.controller.HostResponse.class);
        org.apache.ambari.server.controller.ResourceProviderFactory resourceProviderFactory = createNiceMock(org.apache.ambari.server.controller.ResourceProviderFactory.class);
        org.apache.ambari.server.controller.spi.ResourceProvider hostResourceProvider = getHostProvider(injector);
        org.apache.ambari.server.state.Host host100 = createMockHost("Host100", "Cluster100", null, "UNHEALTHY", "RECOVERABLE", null);
        org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.init(resourceProviderFactory);
        java.util.Set<org.apache.ambari.server.state.Cluster> clusterSet = new java.util.HashSet<>();
        clusterSet.add(cluster);
        org.apache.ambari.server.controller.ServiceComponentHostResponse shr1 = new org.apache.ambari.server.controller.ServiceComponentHostResponse("Cluster100", "Service100", "Component100", "Component 100", "Host100", "Host100", "STARTED", "", null, null, null, null);
        org.apache.ambari.server.controller.ServiceComponentHostResponse shr2 = new org.apache.ambari.server.controller.ServiceComponentHostResponse("Cluster100", "Service100", "Component102", "Component 102", "Host100", "Host100", "INSTALLED", "", null, null, null, null);
        org.apache.ambari.server.controller.ServiceComponentHostResponse shr3 = new org.apache.ambari.server.controller.ServiceComponentHostResponse("Cluster100", "Service100", "Component103", "Component 103", "Host100", "Host100", "STARTED", "", null, null, null, null);
        java.util.Set<org.apache.ambari.server.controller.ServiceComponentHostResponse> responses = new java.util.HashSet<>();
        responses.add(shr1);
        responses.add(shr2);
        responses.add(shr3);
        EasyMock.expect(host100.getMaintenanceState(2)).andReturn(org.apache.ambari.server.state.MaintenanceState.OFF).anyTimes();
        EasyMock.expect(managementController.getClusters()).andReturn(clusters).anyTimes();
        EasyMock.expect(managementController.getAmbariMetaInfo()).andReturn(ambariMetaInfo).anyTimes();
        EasyMock.expect(managementController.getHostComponents(org.easymock.EasyMock.anyObject())).andReturn(responses).anyTimes();
        EasyMock.expect(clusters.getCluster("Cluster100")).andReturn(cluster).anyTimes();
        EasyMock.expect(clusters.getClustersForHost("Host100")).andReturn(clusterSet).anyTimes();
        EasyMock.expect(clusters.getHosts()).andReturn(java.util.Arrays.asList(host100)).anyTimes();
        EasyMock.expect(cluster.getClusterId()).andReturn(2L).anyTimes();
        EasyMock.expect(cluster.getDesiredConfigs()).andReturn(new java.util.HashMap<>()).anyTimes();
        EasyMock.expect(hostResponse1.getClusterName()).andReturn("Cluster100").anyTimes();
        EasyMock.expect(hostResponse1.getHostname()).andReturn("Host100").anyTimes();
        EasyMock.expect(hostResponse1.getStatus()).andReturn(org.apache.ambari.server.state.HostHealthStatus.HealthStatus.UNHEALTHY.name()).anyTimes();
        EasyMock.expect(hostResponse1.getHealthReport()).andReturn("HEALTHY").anyTimes();
        EasyMock.expect(ambariMetaInfo.getComponent(((java.lang.String) (EasyMock.anyObject())), ((java.lang.String) (EasyMock.anyObject())), ((java.lang.String) (EasyMock.anyObject())), ((java.lang.String) (EasyMock.anyObject())))).andReturn(componentInfo).anyTimes();
        EasyMock.expect(componentInfo.getCategory()).andReturn("MASTER").anyTimes();
        EasyMock.expect(resourceProviderFactory.getHostResourceProvider(EasyMock.eq(managementController))).andReturn(hostResourceProvider).anyTimes();
        java.util.Set<java.lang.String> propertyIds = new java.util.HashSet<>();
        propertyIds.add(org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_CLUSTER_NAME_PROPERTY_ID);
        propertyIds.add(org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_HOST_NAME_PROPERTY_ID);
        propertyIds.add(org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_HOST_STATUS_PROPERTY_ID);
        org.apache.ambari.server.controller.spi.Predicate predicate = buildPredicate("Cluster100", null);
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(propertyIds);
        replayAll();
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator());
        org.apache.ambari.server.controller.spi.ResourceProvider provider = org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.getResourceProvider(type, managementController);
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = provider.getResources(request, predicate);
        org.junit.Assert.assertEquals(1, resources.size());
        for (org.apache.ambari.server.controller.spi.Resource resource : resources) {
            java.lang.String clusterName = ((java.lang.String) (resource.getPropertyValue(org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_CLUSTER_NAME_PROPERTY_ID)));
            org.junit.Assert.assertEquals("Cluster100", clusterName);
            java.lang.String status = ((java.lang.String) (resource.getPropertyValue(org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_HOST_STATUS_PROPERTY_ID)));
            org.junit.Assert.assertEquals("UNHEALTHY", status);
        }
        verifyAll();
    }

    @org.junit.Test
    public void testGetResources_Status_Unknown() throws java.lang.Exception {
        org.apache.ambari.server.controller.spi.Resource.Type type = org.apache.ambari.server.controller.spi.Resource.Type.Host;
        com.google.inject.Injector injector = createInjector();
        org.apache.ambari.server.controller.AmbariManagementController managementController = injector.getInstance(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.state.Clusters clusters = injector.getInstance(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.state.Cluster cluster = createMock(org.apache.ambari.server.state.Cluster.class);
        org.apache.ambari.server.api.services.AmbariMetaInfo ambariMetaInfo = createNiceMock(org.apache.ambari.server.api.services.AmbariMetaInfo.class);
        org.apache.ambari.server.controller.HostResponse hostResponse1 = createNiceMock(org.apache.ambari.server.controller.HostResponse.class);
        org.apache.ambari.server.controller.ResourceProviderFactory resourceProviderFactory = createNiceMock(org.apache.ambari.server.controller.ResourceProviderFactory.class);
        org.apache.ambari.server.controller.spi.ResourceProvider hostResourceProvider = getHostProvider(injector);
        org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.init(resourceProviderFactory);
        org.apache.ambari.server.state.Host host100 = createMockHost("Host100", "Cluster100", null, "UNKNOWN", "RECOVERABLE", null);
        java.util.Set<org.apache.ambari.server.state.Cluster> clusterSet = new java.util.HashSet<>();
        clusterSet.add(cluster);
        EasyMock.expect(host100.getMaintenanceState(2)).andReturn(org.apache.ambari.server.state.MaintenanceState.OFF).anyTimes();
        EasyMock.expect(managementController.getClusters()).andReturn(clusters).anyTimes();
        EasyMock.expect(managementController.getAmbariMetaInfo()).andReturn(ambariMetaInfo).anyTimes();
        EasyMock.expect(clusters.getCluster("Cluster100")).andReturn(cluster).anyTimes();
        EasyMock.expect(clusters.getClustersForHost("Host100")).andReturn(clusterSet).anyTimes();
        EasyMock.expect(clusters.getHosts()).andReturn(java.util.Arrays.asList(host100)).anyTimes();
        EasyMock.expect(cluster.getClusterId()).andReturn(2L).anyTimes();
        EasyMock.expect(cluster.getDesiredConfigs()).andReturn(new java.util.HashMap<>()).anyTimes();
        EasyMock.expect(hostResponse1.getClusterName()).andReturn("Cluster100").anyTimes();
        EasyMock.expect(hostResponse1.getHostname()).andReturn("Host100").anyTimes();
        EasyMock.expect(hostResponse1.getStatus()).andReturn(org.apache.ambari.server.state.HostHealthStatus.HealthStatus.UNKNOWN.name()).anyTimes();
        EasyMock.expect(hostResponse1.getHealthReport()).andReturn("UNKNOWN").anyTimes();
        EasyMock.expect(resourceProviderFactory.getHostResourceProvider(EasyMock.eq(managementController))).andReturn(hostResourceProvider).anyTimes();
        java.util.Set<java.lang.String> propertyIds = new java.util.HashSet<>();
        propertyIds.add(org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_CLUSTER_NAME_PROPERTY_ID);
        propertyIds.add(org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_HOST_NAME_PROPERTY_ID);
        propertyIds.add(org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_HOST_STATUS_PROPERTY_ID);
        org.apache.ambari.server.controller.spi.Predicate predicate = buildPredicate("Cluster100", null);
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(propertyIds);
        replayAll();
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator());
        org.apache.ambari.server.controller.spi.ResourceProvider provider = org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.getResourceProvider(type, managementController);
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = provider.getResources(request, predicate);
        org.junit.Assert.assertEquals(1, resources.size());
        for (org.apache.ambari.server.controller.spi.Resource resource : resources) {
            java.lang.String clusterName = ((java.lang.String) (resource.getPropertyValue(org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_CLUSTER_NAME_PROPERTY_ID)));
            org.junit.Assert.assertEquals("Cluster100", clusterName);
            java.lang.String status = ((java.lang.String) (resource.getPropertyValue(org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_HOST_STATUS_PROPERTY_ID)));
            org.junit.Assert.assertEquals("UNKNOWN", status);
        }
        verifyAll();
    }

    @org.junit.Test
    public void testGetRecoveryReportAsAdministrator() throws java.lang.Exception {
        testGetRecoveryReport(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator());
    }

    @org.junit.Test
    public void testGetRecoveryReportAsClusterAdministrator() throws java.lang.Exception {
        testGetRecoveryReport(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator());
    }

    @org.junit.Test
    public void testGetRecoveryReportAsServiceAdministrator() throws java.lang.Exception {
        testGetRecoveryReport(org.apache.ambari.server.security.TestAuthenticationFactory.createServiceAdministrator());
    }

    private void testGetRecoveryReport(org.springframework.security.core.Authentication authentication) throws java.lang.Exception {
        org.apache.ambari.server.controller.spi.Resource.Type type = org.apache.ambari.server.controller.spi.Resource.Type.Host;
        com.google.inject.Injector injector = createInjector();
        org.apache.ambari.server.controller.AmbariManagementController managementController = injector.getInstance(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.state.Clusters clusters = injector.getInstance(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.state.Cluster cluster = createMock(org.apache.ambari.server.state.Cluster.class);
        org.apache.ambari.server.api.services.AmbariMetaInfo ambariMetaInfo = createNiceMock(org.apache.ambari.server.api.services.AmbariMetaInfo.class);
        org.apache.ambari.server.state.ComponentInfo componentInfo = createNiceMock(org.apache.ambari.server.state.ComponentInfo.class);
        org.apache.ambari.server.controller.ResourceProviderFactory resourceProviderFactory = createNiceMock(org.apache.ambari.server.controller.ResourceProviderFactory.class);
        org.apache.ambari.server.controller.spi.ResourceProvider hostResourceProvider = getHostProvider(injector);
        org.apache.ambari.server.agent.RecoveryReport rr = new org.apache.ambari.server.agent.RecoveryReport();
        rr.setSummary("RECOVERABLE");
        java.util.List<org.apache.ambari.server.agent.ComponentRecoveryReport> compRecReports = new java.util.ArrayList<>();
        org.apache.ambari.server.agent.ComponentRecoveryReport compRecReport = new org.apache.ambari.server.agent.ComponentRecoveryReport();
        compRecReport.setLimitReached(java.lang.Boolean.FALSE);
        compRecReport.setName("DATANODE");
        compRecReport.setNumAttempts(2);
        compRecReports.add(compRecReport);
        rr.setComponentReports(compRecReports);
        org.apache.ambari.server.state.Host host100 = createMockHost("Host100", "Cluster100", null, "HEALTHY", "RECOVERABLE", rr);
        org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.init(resourceProviderFactory);
        java.util.Set<org.apache.ambari.server.state.Cluster> clusterSet = new java.util.HashSet<>();
        clusterSet.add(cluster);
        org.apache.ambari.server.controller.ServiceComponentHostResponse shr1 = new org.apache.ambari.server.controller.ServiceComponentHostResponse("Cluster100", "Service100", "Component100", "Component 100", "Host100", "Host100", "STARTED", "", null, null, null, null);
        java.util.Set<org.apache.ambari.server.controller.ServiceComponentHostResponse> responses = new java.util.HashSet<>();
        responses.add(shr1);
        EasyMock.expect(host100.getMaintenanceState(2)).andReturn(org.apache.ambari.server.state.MaintenanceState.OFF).anyTimes();
        EasyMock.expect(managementController.getClusters()).andReturn(clusters).anyTimes();
        EasyMock.expect(managementController.getAmbariMetaInfo()).andReturn(ambariMetaInfo).anyTimes();
        EasyMock.expect(managementController.getHostComponents(org.easymock.EasyMock.anyObject())).andReturn(responses).anyTimes();
        EasyMock.expect(clusters.getCluster("Cluster100")).andReturn(cluster).anyTimes();
        EasyMock.expect(clusters.getClustersForHost("Host100")).andReturn(clusterSet).anyTimes();
        EasyMock.expect(clusters.getHosts()).andReturn(java.util.Arrays.asList(host100)).anyTimes();
        EasyMock.expect(cluster.getClusterId()).andReturn(2L).anyTimes();
        EasyMock.expect(cluster.getDesiredConfigs()).andReturn(new java.util.HashMap<>()).anyTimes();
        EasyMock.expect(ambariMetaInfo.getComponent(((java.lang.String) (EasyMock.anyObject())), ((java.lang.String) (EasyMock.anyObject())), ((java.lang.String) (EasyMock.anyObject())), ((java.lang.String) (EasyMock.anyObject())))).andReturn(componentInfo).anyTimes();
        EasyMock.expect(componentInfo.getCategory()).andReturn("SLAVE").anyTimes();
        EasyMock.expect(resourceProviderFactory.getHostResourceProvider(EasyMock.eq(managementController))).andReturn(hostResourceProvider).anyTimes();
        java.util.Set<java.lang.String> propertyIds = new java.util.HashSet<>();
        propertyIds.add(org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_CLUSTER_NAME_PROPERTY_ID);
        propertyIds.add(org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_HOST_NAME_PROPERTY_ID);
        propertyIds.add(org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_RECOVERY_REPORT_PROPERTY_ID);
        propertyIds.add(org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_RECOVERY_SUMMARY_PROPERTY_ID);
        org.apache.ambari.server.controller.spi.Predicate predicate = buildPredicate("Cluster100", null);
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(propertyIds);
        replayAll();
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        org.apache.ambari.server.controller.spi.ResourceProvider provider = org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.getResourceProvider(type, managementController);
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = provider.getResources(request, predicate);
        org.junit.Assert.assertEquals(1, resources.size());
        for (org.apache.ambari.server.controller.spi.Resource resource : resources) {
            java.lang.String clusterName = ((java.lang.String) (resource.getPropertyValue(org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_CLUSTER_NAME_PROPERTY_ID)));
            org.junit.Assert.assertEquals("Cluster100", clusterName);
            java.lang.String recovery = ((java.lang.String) (resource.getPropertyValue(org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_RECOVERY_SUMMARY_PROPERTY_ID)));
            org.junit.Assert.assertEquals("RECOVERABLE", recovery);
            org.apache.ambari.server.agent.RecoveryReport recRep = ((org.apache.ambari.server.agent.RecoveryReport) (resource.getPropertyValue(org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_RECOVERY_REPORT_PROPERTY_ID)));
            org.junit.Assert.assertEquals("RECOVERABLE", recRep.getSummary());
            org.junit.Assert.assertEquals(1, recRep.getComponentReports().size());
            org.junit.Assert.assertEquals(2, recRep.getComponentReports().get(0).getNumAttempts());
        }
        verifyAll();
    }

    @org.junit.Test
    public void testGetResources_Status_Alert() throws java.lang.Exception {
        org.apache.ambari.server.controller.spi.Resource.Type type = org.apache.ambari.server.controller.spi.Resource.Type.Host;
        com.google.inject.Injector injector = createInjector();
        org.apache.ambari.server.controller.AmbariManagementController managementController = injector.getInstance(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.state.Clusters clusters = injector.getInstance(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.state.Cluster cluster = createMock(org.apache.ambari.server.state.Cluster.class);
        org.apache.ambari.server.state.HostHealthStatus healthStatus = createNiceMock(org.apache.ambari.server.state.HostHealthStatus.class);
        org.apache.ambari.server.api.services.AmbariMetaInfo ambariMetaInfo = createNiceMock(org.apache.ambari.server.api.services.AmbariMetaInfo.class);
        org.apache.ambari.server.state.ComponentInfo componentInfo = createNiceMock(org.apache.ambari.server.state.ComponentInfo.class);
        org.apache.ambari.server.controller.HostResponse hostResponse1 = createNiceMock(org.apache.ambari.server.controller.HostResponse.class);
        org.apache.ambari.server.controller.ResourceProviderFactory resourceProviderFactory = createNiceMock(org.apache.ambari.server.controller.ResourceProviderFactory.class);
        org.apache.ambari.server.controller.spi.ResourceProvider hostResourceProvider = getHostProvider(injector);
        org.apache.ambari.server.state.Host host100 = createMockHost("Host100", "Cluster100", null, "ALERT", "RECOVERABLE", null);
        org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.init(resourceProviderFactory);
        java.util.Set<org.apache.ambari.server.state.Cluster> clusterSet = new java.util.HashSet<>();
        clusterSet.add(cluster);
        org.apache.ambari.server.controller.ServiceComponentHostResponse shr1 = new org.apache.ambari.server.controller.ServiceComponentHostResponse("Cluster100", "Service100", "Component100", "Component 100", "Host100", "Host100", "STARTED", "", null, null, null, null);
        org.apache.ambari.server.controller.ServiceComponentHostResponse shr2 = new org.apache.ambari.server.controller.ServiceComponentHostResponse("Cluster100", "Service100", "Component102", "Component 102", "Host100", "Host100", "INSTALLED", "", null, null, null, null);
        org.apache.ambari.server.controller.ServiceComponentHostResponse shr3 = new org.apache.ambari.server.controller.ServiceComponentHostResponse("Cluster100", "Service100", "Component103", "Component 103", "Host100", "Host100", "STARTED", "", null, null, null, null);
        java.util.Set<org.apache.ambari.server.controller.ServiceComponentHostResponse> responses = new java.util.HashSet<>();
        responses.add(shr1);
        responses.add(shr2);
        responses.add(shr3);
        EasyMock.expect(host100.getMaintenanceState(2)).andReturn(org.apache.ambari.server.state.MaintenanceState.OFF).anyTimes();
        EasyMock.expect(managementController.getClusters()).andReturn(clusters).anyTimes();
        EasyMock.expect(managementController.getAmbariMetaInfo()).andReturn(ambariMetaInfo).anyTimes();
        EasyMock.expect(managementController.getHostComponents(org.easymock.EasyMock.anyObject())).andReturn(responses).anyTimes();
        EasyMock.expect(clusters.getCluster("Cluster100")).andReturn(cluster).anyTimes();
        EasyMock.expect(clusters.getClustersForHost("Host100")).andReturn(clusterSet).anyTimes();
        EasyMock.expect(clusters.getHosts()).andReturn(java.util.Arrays.asList(host100)).anyTimes();
        EasyMock.expect(cluster.getClusterId()).andReturn(2L).anyTimes();
        EasyMock.expect(cluster.getDesiredConfigs()).andReturn(new java.util.HashMap<>()).anyTimes();
        EasyMock.expect(hostResponse1.getClusterName()).andReturn("Cluster100").anyTimes();
        EasyMock.expect(hostResponse1.getHostname()).andReturn("Host100").anyTimes();
        EasyMock.expect(hostResponse1.getStatus()).andReturn(org.apache.ambari.server.state.HostHealthStatus.HealthStatus.ALERT.name()).anyTimes();
        EasyMock.expect(hostResponse1.getHealthReport()).andReturn("HEALTHY").anyTimes();
        EasyMock.expect(ambariMetaInfo.getComponent(((java.lang.String) (EasyMock.anyObject())), ((java.lang.String) (EasyMock.anyObject())), ((java.lang.String) (EasyMock.anyObject())), ((java.lang.String) (EasyMock.anyObject())))).andReturn(componentInfo).anyTimes();
        EasyMock.expect(componentInfo.getCategory()).andReturn("SLAVE").anyTimes();
        EasyMock.expect(resourceProviderFactory.getHostResourceProvider(EasyMock.eq(managementController))).andReturn(hostResourceProvider).anyTimes();
        java.util.Set<java.lang.String> propertyIds = new java.util.HashSet<>();
        propertyIds.add(org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_CLUSTER_NAME_PROPERTY_ID);
        propertyIds.add(org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_HOST_NAME_PROPERTY_ID);
        propertyIds.add(org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_HOST_STATUS_PROPERTY_ID);
        org.apache.ambari.server.controller.spi.Predicate predicate = buildPredicate("Cluster100", null);
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(propertyIds);
        replayAll();
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator());
        org.apache.ambari.server.controller.spi.ResourceProvider provider = org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.getResourceProvider(type, managementController);
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = provider.getResources(request, predicate);
        org.junit.Assert.assertEquals(1, resources.size());
        for (org.apache.ambari.server.controller.spi.Resource resource : resources) {
            java.lang.String clusterName = ((java.lang.String) (resource.getPropertyValue(org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_CLUSTER_NAME_PROPERTY_ID)));
            org.junit.Assert.assertEquals("Cluster100", clusterName);
            java.lang.String status = ((java.lang.String) (resource.getPropertyValue(org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_HOST_STATUS_PROPERTY_ID)));
            org.junit.Assert.assertEquals("ALERT", status);
        }
        verifyAll();
    }

    @org.junit.Test
    public void testUpdateDesiredConfigAsAdministrator() throws java.lang.Exception {
        testUpdateDesiredConfig(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator());
    }

    @org.junit.Test
    public void testUpdateDesiredConfigAsClusterAdministrator() throws java.lang.Exception {
        testUpdateDesiredConfig(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator());
    }

    @org.junit.Test
    public void testUpdateDesiredConfigAsServiceAdministrator() throws java.lang.Exception {
        testUpdateDesiredConfig(org.apache.ambari.server.security.TestAuthenticationFactory.createServiceAdministrator());
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testUpdateDesiredConfigAsServiceOperator() throws java.lang.Exception {
        testUpdateDesiredConfig(org.apache.ambari.server.security.TestAuthenticationFactory.createServiceOperator());
    }

    private void testUpdateDesiredConfig(org.springframework.security.core.Authentication authentication) throws java.lang.Exception {
        com.google.inject.Injector injector = createInjector();
        org.apache.ambari.server.controller.AmbariManagementController managementController = injector.getInstance(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.state.Clusters clusters = injector.getInstance(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.state.Cluster cluster = createMock(org.apache.ambari.server.state.Cluster.class);
        org.apache.ambari.server.api.services.AmbariMetaInfo ambariMetaInfo = createNiceMock(org.apache.ambari.server.api.services.AmbariMetaInfo.class);
        org.apache.ambari.server.controller.HostResponse hostResponse1 = createNiceMock(org.apache.ambari.server.controller.HostResponse.class);
        org.apache.ambari.server.controller.ResourceProviderFactory resourceProviderFactory = createNiceMock(org.apache.ambari.server.controller.ResourceProviderFactory.class);
        org.apache.ambari.server.controller.spi.ResourceProvider hostResourceProvider = getHostProvider(injector);
        org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.init(resourceProviderFactory);
        java.util.Set<org.apache.ambari.server.state.Cluster> clusterSet = new java.util.HashSet<>();
        clusterSet.add(cluster);
        org.apache.ambari.server.state.Host host100 = createMockHost("Host100", "Cluster100", null, "HEALTHY", "RECOVERABLE", null);
        EasyMock.expect(managementController.getClusters()).andReturn(clusters).anyTimes();
        EasyMock.expect(managementController.getAmbariMetaInfo()).andReturn(ambariMetaInfo).anyTimes();
        EasyMock.expect(managementController.getHostComponents(org.easymock.EasyMock.anyObject())).andReturn(java.util.Collections.emptySet()).anyTimes();
        EasyMock.expect(clusters.getCluster("Cluster100")).andReturn(cluster).anyTimes();
        EasyMock.expect(clusters.getClustersForHost("Host100")).andReturn(clusterSet).anyTimes();
        EasyMock.expect(clusters.getHosts()).andReturn(java.util.Arrays.asList(host100)).anyTimes();
        EasyMock.expect(clusters.getHostsForCluster("Cluster100")).andReturn(java.util.Collections.singletonMap("Host100", host100)).anyTimes();
        EasyMock.expect(clusters.getHost("Host100")).andReturn(host100).anyTimes();
        clusters.mapAndPublishHostsToCluster(java.util.Collections.singleton("Host100"), "Cluster100");
        EasyMock.expectLastCall().anyTimes();
        EasyMock.expect(cluster.getClusterId()).andReturn(2L).anyTimes();
        EasyMock.expect(cluster.getResourceId()).andReturn(4L).anyTimes();
        EasyMock.expect(cluster.getDesiredConfigs()).andReturn(new java.util.HashMap<>()).anyTimes();
        EasyMock.expect(hostResponse1.getClusterName()).andReturn("Cluster100").anyTimes();
        EasyMock.expect(hostResponse1.getHostname()).andReturn("Host100").anyTimes();
        EasyMock.expect(hostResponse1.getHealthReport()).andReturn("HEALTHY").anyTimes();
        EasyMock.expect(resourceProviderFactory.getHostResourceProvider(EasyMock.eq(managementController))).andReturn(hostResourceProvider).anyTimes();
        replayAll();
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        java.util.Map<java.lang.String, java.lang.Object> properties = new java.util.LinkedHashMap<>();
        properties.put(org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_CLUSTER_NAME_PROPERTY_ID, "Cluster100");
        properties.put(org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_HOST_NAME_PROPERTY_ID, "Host100");
        properties.put(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Hosts.desired_config", "type"), "global");
        properties.put(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Hosts.desired_config", "tag"), "version1");
        properties.put(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Hosts.desired_config.properties", "a"), "b");
        properties.put(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Hosts.desired_config.properties", "x"), "y");
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getUpdateRequest(properties, null);
        org.apache.ambari.server.controller.spi.Predicate predicate = buildPredicate("Cluster100", "Host100");
        org.apache.ambari.server.controller.spi.ResourceProvider provider = org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.getResourceProvider(org.apache.ambari.server.controller.spi.Resource.Type.Host, managementController);
        provider.updateResources(request, predicate);
        verifyAll();
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
    public void testUpdateResourcesAsServiceAdministrator() throws java.lang.Exception {
        testUpdateResources(org.apache.ambari.server.security.TestAuthenticationFactory.createServiceAdministrator());
    }

    private void testUpdateResources(org.springframework.security.core.Authentication authentication) throws java.lang.Exception {
        org.apache.ambari.server.security.authorization.AuthorizationHelperInitializer.viewInstanceDAOReturningNull();
        org.apache.ambari.server.controller.spi.Resource.Type type = org.apache.ambari.server.controller.spi.Resource.Type.Host;
        com.google.inject.Injector injector = createInjector();
        org.apache.ambari.server.controller.AmbariManagementController managementController = injector.getInstance(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.state.Clusters clusters = injector.getInstance(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.state.Cluster cluster = createMock(org.apache.ambari.server.state.Cluster.class);
        org.apache.ambari.server.api.services.AmbariMetaInfo ambariMetaInfo = createNiceMock(org.apache.ambari.server.api.services.AmbariMetaInfo.class);
        org.apache.ambari.server.controller.HostResponse hostResponse1 = createNiceMock(org.apache.ambari.server.controller.HostResponse.class);
        org.apache.ambari.server.controller.ResourceProviderFactory resourceProviderFactory = createNiceMock(org.apache.ambari.server.controller.ResourceProviderFactory.class);
        org.apache.ambari.server.controller.spi.ResourceProvider hostResourceProvider = getHostProvider(injector);
        org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.init(resourceProviderFactory);
        java.util.Set<org.apache.ambari.server.state.Cluster> clusterSet = new java.util.HashSet<>();
        clusterSet.add(cluster);
        org.apache.ambari.server.state.Host host100 = createMockHost("Host100", "Cluster100", null, "HEALTHY", "RECOVERABLE", null);
        EasyMock.expect(managementController.getClusters()).andReturn(clusters).anyTimes();
        EasyMock.expect(managementController.getAmbariMetaInfo()).andReturn(ambariMetaInfo).anyTimes();
        EasyMock.expect(managementController.getHostComponents(org.easymock.EasyMock.anyObject())).andReturn(java.util.Collections.emptySet()).anyTimes();
        managementController.registerRackChange("Cluster100");
        EasyMock.expectLastCall().anyTimes();
        EasyMock.expect(clusters.getCluster("Cluster100")).andReturn(cluster).anyTimes();
        EasyMock.expect(clusters.getClustersForHost("Host100")).andReturn(clusterSet).anyTimes();
        EasyMock.expect(clusters.getHost("Host100")).andReturn(host100).anyTimes();
        EasyMock.expect(clusters.getHostsForCluster("Cluster100")).andReturn(java.util.Collections.singletonMap("Host100", host100)).anyTimes();
        clusters.mapAndPublishHostsToCluster(java.util.Collections.singleton("Host100"), "Cluster100");
        EasyMock.expectLastCall().anyTimes();
        EasyMock.expect(cluster.getClusterId()).andReturn(2L).anyTimes();
        EasyMock.expect(cluster.getResourceId()).andReturn(4L).anyTimes();
        EasyMock.expect(cluster.getDesiredConfigs()).andReturn(new java.util.HashMap<>()).anyTimes();
        EasyMock.expect(hostResponse1.getClusterName()).andReturn("Cluster100").anyTimes();
        EasyMock.expect(hostResponse1.getHostname()).andReturn("Host100").anyTimes();
        EasyMock.expect(hostResponse1.getHealthReport()).andReturn("HEALTHY").anyTimes();
        EasyMock.expect(resourceProviderFactory.getHostResourceProvider(EasyMock.eq(managementController))).andReturn(hostResourceProvider).anyTimes();
        replayAll();
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        org.apache.ambari.server.controller.spi.ResourceProvider provider = org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.getResourceProvider(type, managementController);
        java.util.Map<java.lang.String, java.lang.Object> properties = new java.util.LinkedHashMap<>();
        properties.put(org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_RACK_INFO_PROPERTY_ID, "rack info");
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getUpdateRequest(properties, null);
        org.apache.ambari.server.controller.spi.Predicate predicate = buildPredicate("Cluster100", "Host100");
        provider.updateResources(request, predicate);
        verifyAll();
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
        com.google.inject.Injector injector = createInjector();
        org.apache.ambari.server.controller.AmbariManagementController managementController = injector.getInstance(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.state.Clusters clusters = injector.getInstance(org.apache.ambari.server.state.Clusters.class);
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        org.apache.ambari.server.state.Cluster cluster = createMock(org.apache.ambari.server.state.Cluster.class);
        org.apache.ambari.server.state.Host host1 = createNiceMock(org.apache.ambari.server.state.Host.class);
        org.apache.ambari.server.controller.spi.ResourceProvider provider = getHostProvider(injector);
        org.apache.ambari.server.state.HostHealthStatus healthStatus = createNiceMock(org.apache.ambari.server.state.HostHealthStatus.class);
        org.apache.ambari.server.topology.TopologyManager topologyManager = createNiceMock(org.apache.ambari.server.topology.TopologyManager.class);
        org.apache.ambari.server.controller.internal.HostResourceProvider.setTopologyManager(topologyManager);
        java.util.Set<org.apache.ambari.server.state.Cluster> clusterSet = new java.util.HashSet<>();
        EasyMock.expect(managementController.getClusters()).andReturn(clusters).anyTimes();
        EasyMock.expect(clusters.getHosts()).andReturn(java.util.Arrays.asList(host1)).anyTimes();
        EasyMock.expect(clusters.getHost("Host100")).andReturn(host1).anyTimes();
        EasyMock.expect(clusters.getCluster("Cluster100")).andReturn(cluster).anyTimes();
        EasyMock.expect(clusters.getClustersForHost("Host100")).andReturn(clusterSet).anyTimes();
        EasyMock.expect(cluster.getServiceComponentHosts("Host100")).andReturn(java.util.Collections.emptyList());
        EasyMock.expect(cluster.getClusterId()).andReturn(100L).anyTimes();
        EasyMock.expect(cluster.getDesiredConfigs()).andReturn(new java.util.HashMap<>()).anyTimes();
        clusters.deleteHost("Host100");
        clusters.publishHostsDeletion(java.util.Collections.singleton(1L), java.util.Collections.singleton("Host100"));
        EasyMock.expect(host1.getHostName()).andReturn("Host100").anyTimes();
        EasyMock.expect(host1.getHostId()).andReturn(1L).anyTimes();
        EasyMock.expect(healthStatus.getHealthStatus()).andReturn(org.apache.ambari.server.state.HostHealthStatus.HealthStatus.HEALTHY).anyTimes();
        EasyMock.expect(healthStatus.getHealthReport()).andReturn("HEALTHY").anyTimes();
        EasyMock.expect(topologyManager.getRequests(java.util.Collections.emptyList())).andReturn(java.util.Collections.emptyList()).anyTimes();
        replayAll();
        org.apache.ambari.server.controller.internal.AbstractResourceProviderTest.TestObserver observer = new org.apache.ambari.server.controller.internal.AbstractResourceProviderTest.TestObserver();
        ((org.apache.ambari.server.controller.internal.ObservableResourceProvider) (provider)).addObserver(observer);
        org.apache.ambari.server.controller.spi.Predicate predicate = buildPredicate("Cluster100", "Host100");
        provider.deleteResources(new org.apache.ambari.server.controller.internal.RequestImpl(null, null, null, null), predicate);
        org.apache.ambari.server.controller.internal.ResourceProviderEvent lastEvent = observer.getLastEvent();
        org.junit.Assert.assertNotNull(lastEvent);
        org.junit.Assert.assertEquals(org.apache.ambari.server.controller.spi.Resource.Type.Host, lastEvent.getResourceType());
        org.junit.Assert.assertEquals(org.apache.ambari.server.controller.internal.ResourceProviderEvent.Type.Delete, lastEvent.getType());
        org.junit.Assert.assertEquals(predicate, lastEvent.getPredicate());
        org.junit.Assert.assertNull(lastEvent.getRequest());
        verifyAll();
    }

    public static org.apache.ambari.server.controller.internal.HostResourceProvider getHostProvider(org.apache.ambari.server.controller.AmbariManagementController managementController) throws java.lang.NoSuchFieldException, java.lang.IllegalAccessException {
        org.apache.ambari.server.controller.spi.Resource.Type type = org.apache.ambari.server.controller.spi.Resource.Type.Host;
        org.apache.ambari.server.agent.stomp.TopologyHolder topologyHolder = org.easymock.EasyMock.createNiceMock(org.apache.ambari.server.agent.stomp.TopologyHolder.class);
        org.apache.ambari.server.agent.RecoveryConfigHelper recoveryConfigHelper = org.easymock.EasyMock.createNiceMock(org.apache.ambari.server.agent.RecoveryConfigHelper.class);
        org.apache.ambari.server.agent.stomp.HostLevelParamsHolder hostLevelParamsHolder = org.easymock.EasyMock.createNiceMock(org.apache.ambari.server.agent.stomp.HostLevelParamsHolder.class);
        org.apache.ambari.server.controller.internal.HostResourceProvider hostResourceProvider = new org.apache.ambari.server.controller.internal.HostResourceProvider(managementController);
        EasyMock.replay(topologyHolder, recoveryConfigHelper, hostLevelParamsHolder);
        java.lang.reflect.Field topologyHolderField = org.apache.ambari.server.controller.internal.HostResourceProvider.class.getDeclaredField("topologyHolder");
        topologyHolderField.setAccessible(true);
        topologyHolderField.set(hostResourceProvider, topologyHolder);
        java.lang.reflect.Field recoveryConfigHelperField = org.apache.ambari.server.controller.internal.HostResourceProvider.class.getDeclaredField("recoveryConfigHelper");
        recoveryConfigHelperField.setAccessible(true);
        recoveryConfigHelperField.set(hostResourceProvider, recoveryConfigHelper);
        java.lang.reflect.Field hostLevelParamsHolderField = org.apache.ambari.server.controller.internal.HostResourceProvider.class.getDeclaredField("hostLevelParamsHolder");
        hostLevelParamsHolderField.setAccessible(true);
        hostLevelParamsHolderField.set(hostResourceProvider, hostLevelParamsHolder);
        return hostResourceProvider;
    }

    @org.junit.Test
    public void testGetHostsAsAdministrator() throws java.lang.Exception {
        testGetHosts(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator());
    }

    @org.junit.Test
    public void testGetHostsAsClusterAdministrator() throws java.lang.Exception {
        testGetHosts(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator());
    }

    @org.junit.Test
    public void testGetHostsAsServiceAdministrator() throws java.lang.Exception {
        testGetHosts(org.apache.ambari.server.security.TestAuthenticationFactory.createServiceAdministrator());
    }

    @org.junit.Test
    public void testGetHostsAsServiceOperator() throws java.lang.Exception {
        testGetHosts(org.apache.ambari.server.security.TestAuthenticationFactory.createServiceOperator());
    }

    private void testGetHosts(org.springframework.security.core.Authentication authentication) throws java.lang.Exception {
        com.google.inject.Injector injector = createInjector();
        org.apache.ambari.server.controller.AmbariManagementController managementController = injector.getInstance(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.state.Clusters clusters = injector.getInstance(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.state.Cluster cluster = createMock(org.apache.ambari.server.state.Cluster.class);
        org.apache.ambari.server.state.Host host = createNiceMock(org.apache.ambari.server.state.Host.class);
        org.apache.ambari.server.controller.HostResponse response = createNiceMock(org.apache.ambari.server.controller.HostResponse.class);
        java.util.Set<org.apache.ambari.server.state.Cluster> setCluster = java.util.Collections.singleton(cluster);
        java.util.Map<java.lang.String, org.apache.ambari.server.state.DesiredConfig> desiredConfigs = new java.util.HashMap<>();
        java.util.Map<java.lang.String, org.apache.ambari.server.state.HostConfig> desiredHostConfigs = new java.util.HashMap<>();
        org.apache.ambari.server.controller.HostRequest request1 = new org.apache.ambari.server.controller.HostRequest("host1", "cluster1");
        java.util.Set<org.apache.ambari.server.controller.HostRequest> setRequests = new java.util.HashSet<>();
        setRequests.add(request1);
        EasyMock.expect(managementController.getClusters()).andReturn(clusters).atLeastOnce();
        EasyMock.expect(clusters.getCluster("cluster1")).andReturn(cluster);
        EasyMock.expect(clusters.getHost("host1")).andReturn(host);
        EasyMock.expect(clusters.getClustersForHost("host1")).andReturn(setCluster);
        EasyMock.expect(clusters.getCluster("Cluster100")).andReturn(cluster).anyTimes();
        EasyMock.expect(cluster.getClusterId()).andReturn(2L).anyTimes();
        EasyMock.expect(cluster.getDesiredConfigs()).andReturn(desiredConfigs);
        EasyMock.expect(host.getDesiredHostConfigs(cluster, desiredConfigs)).andReturn(desiredHostConfigs);
        EasyMock.expect(host.getHostName()).andReturn("host1").anyTimes();
        EasyMock.expect(host.convertToResponse()).andReturn(response);
        response.setClusterName("cluster1");
        replayAll();
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        java.util.Set<org.apache.ambari.server.controller.HostResponse> setResponses = org.apache.ambari.server.controller.internal.HostResourceProviderTest.getHosts(managementController, setRequests);
        org.junit.Assert.assertEquals(1, setResponses.size());
        org.junit.Assert.assertTrue(setResponses.contains(response));
        verifyAll();
    }

    @org.junit.Test(expected = org.apache.ambari.server.HostNotFoundException.class)
    public void testGetHosts___HostNotFoundException() throws java.lang.Exception {
        com.google.inject.Injector injector = createInjector();
        org.apache.ambari.server.controller.AmbariManagementController managementController = injector.getInstance(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.state.Clusters clusters = injector.getInstance(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.state.Cluster cluster = createMock(org.apache.ambari.server.state.Cluster.class);
        org.apache.ambari.server.controller.HostRequest request1 = new org.apache.ambari.server.controller.HostRequest("host1", "cluster1");
        java.util.Set<org.apache.ambari.server.controller.HostRequest> setRequests = java.util.Collections.singleton(request1);
        EasyMock.expect(managementController.getClusters()).andReturn(clusters).anyTimes();
        EasyMock.expect(clusters.getCluster("cluster1")).andReturn(cluster);
        EasyMock.expect(clusters.getHost("host1")).andThrow(new org.apache.ambari.server.HostNotFoundException("host1"));
        replayAll();
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator());
        org.apache.ambari.server.controller.internal.HostResourceProviderTest.getHosts(managementController, setRequests);
        verifyAll();
    }

    @org.junit.Test(expected = org.apache.ambari.server.HostNotFoundException.class)
    public void testGetHosts___HostNotFoundException_HostNotAssociatedWithCluster() throws java.lang.Exception {
        com.google.inject.Injector injector = createInjector();
        org.apache.ambari.server.controller.AmbariManagementController managementController = injector.getInstance(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.state.Clusters clusters = injector.getInstance(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.state.Cluster cluster = createMock(org.apache.ambari.server.state.Cluster.class);
        org.apache.ambari.server.state.Host host = createNiceMock(org.apache.ambari.server.state.Host.class);
        org.apache.ambari.server.controller.HostRequest request1 = new org.apache.ambari.server.controller.HostRequest("host1", "cluster1");
        java.util.Set<org.apache.ambari.server.controller.HostRequest> setRequests = java.util.Collections.singleton(request1);
        EasyMock.expect(managementController.getClusters()).andReturn(clusters).anyTimes();
        EasyMock.expect(clusters.getCluster("cluster1")).andReturn(cluster);
        EasyMock.expect(clusters.getHost("host1")).andReturn(host);
        EasyMock.expect(cluster.getDesiredConfigs()).andReturn(new java.util.HashMap<>()).anyTimes();
        EasyMock.expect(host.getHostName()).andReturn("host1").anyTimes();
        EasyMock.expect(clusters.getClustersForHost("host1")).andReturn(java.util.Collections.emptySet());
        replayAll();
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator());
        org.apache.ambari.server.controller.internal.HostResourceProviderTest.getHosts(managementController, setRequests);
        verifyAll();
    }

    @org.junit.Test
    public void testGetHosts___OR_Predicate_HostNotFoundException() throws java.lang.Exception {
        com.google.inject.Injector injector = createInjector();
        org.apache.ambari.server.controller.AmbariManagementController managementController = injector.getInstance(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.state.Clusters clusters = injector.getInstance(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.state.Cluster cluster = createMock(org.apache.ambari.server.state.Cluster.class);
        org.apache.ambari.server.state.Host host1 = createNiceMock(org.apache.ambari.server.state.Host.class);
        org.apache.ambari.server.state.Host host2 = createNiceMock(org.apache.ambari.server.state.Host.class);
        org.apache.ambari.server.controller.HostResponse response = createNiceMock(org.apache.ambari.server.controller.HostResponse.class);
        org.apache.ambari.server.controller.HostResponse response2 = createNiceMock(org.apache.ambari.server.controller.HostResponse.class);
        org.apache.ambari.server.controller.HostRequest request1 = new org.apache.ambari.server.controller.HostRequest("host1", "cluster1");
        org.apache.ambari.server.controller.HostRequest request2 = new org.apache.ambari.server.controller.HostRequest("host2", "cluster1");
        org.apache.ambari.server.controller.HostRequest request3 = new org.apache.ambari.server.controller.HostRequest("host3", "cluster1");
        org.apache.ambari.server.controller.HostRequest request4 = new org.apache.ambari.server.controller.HostRequest("host4", "cluster1");
        java.util.Set<org.apache.ambari.server.controller.HostRequest> setRequests = new java.util.HashSet<>();
        setRequests.add(request1);
        setRequests.add(request2);
        setRequests.add(request3);
        setRequests.add(request4);
        EasyMock.expect(managementController.getClusters()).andReturn(clusters).anyTimes();
        EasyMock.expect(clusters.getCluster("cluster1")).andReturn(cluster).times(4);
        EasyMock.expect(clusters.getHost("host1")).andReturn(host1);
        EasyMock.expect(host1.getHostName()).andReturn("host1").anyTimes();
        EasyMock.expect(clusters.getClustersForHost("host1")).andReturn(java.util.Collections.singleton(cluster));
        EasyMock.expect(host1.convertToResponse()).andReturn(response);
        response.setClusterName("cluster1");
        EasyMock.expect(clusters.getHost("host2")).andReturn(host2);
        EasyMock.expect(host2.getHostName()).andReturn("host2").anyTimes();
        EasyMock.expect(clusters.getClustersForHost("host2")).andReturn(java.util.Collections.singleton(cluster));
        EasyMock.expect(host2.convertToResponse()).andReturn(response2);
        response2.setClusterName("cluster1");
        EasyMock.expect(clusters.getHost("host3")).andThrow(new org.apache.ambari.server.HostNotFoundException("host3"));
        EasyMock.expect(clusters.getHost("host4")).andThrow(new org.apache.ambari.server.HostNotFoundException("host4"));
        EasyMock.expect(cluster.getClusterId()).andReturn(2L).anyTimes();
        EasyMock.expect(cluster.getDesiredConfigs()).andReturn(new java.util.HashMap<>()).anyTimes();
        replayAll();
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator());
        java.util.Set<org.apache.ambari.server.controller.HostResponse> setResponses = org.apache.ambari.server.controller.internal.HostResourceProviderTest.getHosts(managementController, setRequests);
        org.junit.Assert.assertEquals(2, setResponses.size());
        org.junit.Assert.assertTrue(setResponses.contains(response));
        org.junit.Assert.assertTrue(setResponses.contains(response2));
        verifyAll();
    }

    public static void createHosts(org.apache.ambari.server.controller.AmbariManagementController controller, java.util.Set<org.apache.ambari.server.controller.HostRequest> requests) throws org.apache.ambari.server.AmbariException, org.apache.ambari.server.security.authorization.AuthorizationException, java.lang.NoSuchFieldException, java.lang.IllegalAccessException {
        org.apache.ambari.server.controller.internal.HostResourceProvider provider = org.apache.ambari.server.controller.internal.HostResourceProviderTest.getHostProvider(controller);
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> properties = new java.util.HashSet<>();
        for (org.apache.ambari.server.controller.HostRequest request : requests) {
            java.util.Map<java.lang.String, java.lang.Object> requestProperties = new java.util.HashMap<>();
            requestProperties.put(org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_HOST_NAME_PROPERTY_ID, request.getHostname());
            requestProperties.put(org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_CLUSTER_NAME_PROPERTY_ID, request.getClusterName());
            if (null != request.getRackInfo()) {
                requestProperties.put(org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_RACK_INFO_PROPERTY_ID, java.util.UUID.randomUUID().toString());
            }
            properties.add(requestProperties);
        }
        provider.createHosts(org.apache.ambari.server.controller.utilities.PropertyHelper.getCreateRequest(properties, java.util.Collections.emptyMap()));
    }

    public static java.util.Set<org.apache.ambari.server.controller.HostResponse> getHosts(org.apache.ambari.server.controller.AmbariManagementController controller, java.util.Set<org.apache.ambari.server.controller.HostRequest> requests) throws org.apache.ambari.server.AmbariException, java.lang.NoSuchFieldException, java.lang.IllegalAccessException {
        org.apache.ambari.server.controller.internal.HostResourceProvider provider = org.apache.ambari.server.controller.internal.HostResourceProviderTest.getHostProvider(controller);
        return provider.getHosts(requests);
    }

    public static void deleteHosts(org.apache.ambari.server.controller.AmbariManagementController controller, java.util.Set<org.apache.ambari.server.controller.HostRequest> requests) throws org.apache.ambari.server.AmbariException, java.lang.NoSuchFieldException, java.lang.IllegalAccessException {
        org.apache.ambari.server.topology.TopologyManager topologyManager = org.easymock.EasyMock.createNiceMock(org.apache.ambari.server.topology.TopologyManager.class);
        EasyMock.expect(topologyManager.getRequests(java.util.Collections.emptyList())).andReturn(java.util.Collections.emptyList()).anyTimes();
        EasyMock.replay(topologyManager);
        org.apache.ambari.server.controller.internal.HostResourceProvider provider = org.apache.ambari.server.controller.internal.HostResourceProviderTest.getHostProvider(controller);
        org.apache.ambari.server.controller.internal.HostResourceProvider.setTopologyManager(topologyManager);
        provider.deleteHosts(requests, false);
    }

    public static org.apache.ambari.server.controller.internal.DeleteStatusMetaData deleteHosts(org.apache.ambari.server.controller.AmbariManagementController controller, java.util.Set<org.apache.ambari.server.controller.HostRequest> requests, boolean dryRun) throws org.apache.ambari.server.AmbariException, java.lang.NoSuchFieldException, java.lang.IllegalAccessException {
        org.apache.ambari.server.topology.TopologyManager topologyManager = org.easymock.EasyMock.createNiceMock(org.apache.ambari.server.topology.TopologyManager.class);
        EasyMock.expect(topologyManager.getRequests(java.util.Collections.emptyList())).andReturn(java.util.Collections.emptyList()).anyTimes();
        EasyMock.replay(topologyManager);
        org.apache.ambari.server.controller.internal.HostResourceProvider provider = org.apache.ambari.server.controller.internal.HostResourceProviderTest.getHostProvider(controller);
        org.apache.ambari.server.controller.internal.HostResourceProvider.setTopologyManager(topologyManager);
        return provider.deleteHosts(requests, dryRun);
    }

    public static void updateHosts(org.apache.ambari.server.controller.AmbariManagementController controller, java.util.Set<org.apache.ambari.server.controller.HostRequest> requests) throws org.apache.ambari.server.AmbariException, org.apache.ambari.server.security.authorization.AuthorizationException, java.lang.NoSuchFieldException, java.lang.IllegalAccessException {
        org.apache.ambari.server.controller.internal.HostResourceProvider provider = org.apache.ambari.server.controller.internal.HostResourceProviderTest.getHostProvider(controller);
        provider.updateHosts(requests);
    }

    private com.google.inject.Injector createInjector() throws java.lang.Exception {
        com.google.inject.Injector injector = com.google.inject.Guice.createInjector(new com.google.inject.AbstractModule() {
            @java.lang.Override
            protected void configure() {
                org.apache.ambari.server.testutils.PartialNiceMockBinder.newBuilder().addConfigsBindings().addFactoriesInstallBinding().addPasswordEncryptorBindings().addLdapBindings().build().configure(binder());
                bind(javax.persistence.EntityManager.class).toInstance(createNiceMock(javax.persistence.EntityManager.class));
                bind(org.apache.ambari.server.orm.DBAccessor.class).toInstance(createNiceMock(org.apache.ambari.server.orm.DBAccessor.class));
                bind(org.apache.ambari.server.actionmanager.ActionDBAccessor.class).to(org.apache.ambari.server.actionmanager.ActionDBAccessorImpl.class);
                bind(org.apache.ambari.server.scheduler.ExecutionScheduler.class).toInstance(createNiceMock(org.apache.ambari.server.scheduler.ExecutionScheduler.class));
                bind(org.apache.ambari.server.state.stack.OsFamily.class).toInstance(createNiceMock(org.apache.ambari.server.state.stack.OsFamily.class));
                bind(org.apache.ambari.server.state.Clusters.class).toInstance(createMock(org.apache.ambari.server.state.Clusters.class));
                bind(org.apache.ambari.server.controller.AmbariManagementController.class).toInstance(createMock(org.apache.ambari.server.controller.AmbariManagementController.class));
                bind(org.apache.ambari.server.api.services.AmbariMetaInfo.class).toInstance(createNiceMock(org.apache.ambari.server.api.services.AmbariMetaInfo.class));
                bind(com.google.gson.Gson.class).toInstance(new com.google.gson.Gson());
                bind(org.apache.ambari.server.controller.MaintenanceStateHelper.class).toInstance(createNiceMock(org.apache.ambari.server.controller.MaintenanceStateHelper.class));
                bind(org.apache.ambari.server.controller.KerberosHelper.class).toInstance(createNiceMock(org.apache.ambari.server.controller.KerberosHelper.class));
                bind(com.google.inject.persist.UnitOfWork.class).toInstance(createNiceMock(com.google.inject.persist.UnitOfWork.class));
                bind(org.apache.ambari.server.topology.PersistedState.class).to(org.apache.ambari.server.topology.PersistedStateImpl.class);
                bind(org.apache.ambari.server.actionmanager.HostRoleCommandFactory.class).to(org.apache.ambari.server.actionmanager.HostRoleCommandFactoryImpl.class);
                bind(org.apache.ambari.server.metadata.RoleCommandOrderProvider.class).to(org.apache.ambari.server.metadata.CachedRoleCommandOrderProvider.class);
                bind(org.springframework.security.crypto.password.PasswordEncoder.class).toInstance(new org.springframework.security.crypto.password.StandardPasswordEncoder());
                bind(org.apache.ambari.server.hooks.HookService.class).to(org.apache.ambari.server.hooks.users.UserHookService.class);
                bind(org.apache.ambari.server.actionmanager.StageFactory.class).to(org.apache.ambari.server.actionmanager.StageFactoryImpl.class);
                bind(org.apache.ambari.server.controller.AbstractRootServiceResponseFactory.class).to(org.apache.ambari.server.controller.RootServiceResponseFactory.class);
                bind(org.apache.ambari.server.security.encryption.CredentialStoreService.class).to(org.apache.ambari.server.security.encryption.CredentialStoreServiceImpl.class);
                bind(org.apache.ambari.server.audit.AuditLogger.class).toInstance(createMock(org.apache.ambari.server.audit.AuditLogger.class));
                bind(org.apache.ambari.server.orm.dao.StageDAO.class).toInstance(createNiceMock(org.apache.ambari.server.orm.dao.StageDAO.class));
                bind(org.apache.ambari.server.agent.stomp.HostLevelParamsHolder.class).toInstance(createNiceMock(org.apache.ambari.server.agent.stomp.HostLevelParamsHolder.class));
                bind(org.apache.ambari.server.agent.stomp.TopologyHolder.class).toInstance(createNiceMock(org.apache.ambari.server.agent.stomp.TopologyHolder.class));
                bind(org.apache.ambari.server.agent.RecoveryConfigHelper.class).toInstance(createNiceMock(org.apache.ambari.server.agent.RecoveryConfigHelper.class));
                bind(org.apache.ambari.server.mpack.MpackManagerFactory.class).toInstance(createNiceMock(org.apache.ambari.server.mpack.MpackManagerFactory.class));
                install(new com.google.inject.assistedinject.FactoryModuleBuilder().build(org.apache.ambari.server.stack.StackManagerFactory.class));
                install(new com.google.inject.assistedinject.FactoryModuleBuilder().implement(org.apache.ambari.server.state.ServiceComponentHost.class, org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl.class).build(org.apache.ambari.server.state.ServiceComponentHostFactory.class));
            }
        });
        org.apache.ambari.server.orm.dao.StageDAO stageDAO = injector.getInstance(org.apache.ambari.server.orm.dao.StageDAO.class);
        org.apache.ambari.server.api.services.AmbariMetaInfo ambariMetaInfo = injector.getInstance(org.apache.ambari.server.api.services.AmbariMetaInfo.class);
        EasyMock.reset(stageDAO, ambariMetaInfo);
        return injector;
    }

    private org.apache.ambari.server.controller.internal.HostResourceProvider getHostProvider(com.google.inject.Injector injector) throws java.lang.NoSuchFieldException, java.lang.IllegalAccessException {
        org.apache.ambari.server.controller.internal.HostResourceProvider provider = org.apache.ambari.server.controller.internal.HostResourceProviderTest.getHostProvider(injector.getInstance(org.apache.ambari.server.controller.AmbariManagementController.class));
        injector.injectMembers(provider);
        return provider;
    }

    private org.apache.ambari.server.state.Host createMockHost(java.lang.String hostName, java.lang.String clusterName, java.util.Map<java.lang.String, org.apache.ambari.server.state.HostConfig> desiredConfigs, java.lang.String status, java.lang.String recoverySummary, org.apache.ambari.server.agent.RecoveryReport recoveryReport) {
        org.apache.ambari.server.state.Host host = createMock(org.apache.ambari.server.state.Host.class);
        org.apache.ambari.server.state.HostHealthStatus hostHealthStatus = new org.apache.ambari.server.state.HostHealthStatus(org.apache.ambari.server.state.HostHealthStatus.HealthStatus.HEALTHY, "");
        org.apache.ambari.server.controller.HostResponse hostResponse = new org.apache.ambari.server.controller.HostResponse(hostName, clusterName, null, 1, 1, null, "centos6", 1024, null, 1, 1, null, null, null, hostHealthStatus, org.apache.ambari.server.state.HostState.HEALTHY, status);
        hostResponse.setRecoverySummary(recoverySummary);
        hostResponse.setRecoveryReport(recoveryReport);
        EasyMock.expect(host.convertToResponse()).andReturn(hostResponse).anyTimes();
        try {
            EasyMock.expect(host.getDesiredHostConfigs(org.easymock.EasyMock.anyObject(), org.easymock.EasyMock.anyObject())).andReturn(desiredConfigs).anyTimes();
        } catch (org.apache.ambari.server.AmbariException e) {
            org.junit.Assert.fail(e.getMessage());
        }
        EasyMock.expect(host.getHostName()).andReturn(hostName).anyTimes();
        EasyMock.expect(host.getHostId()).andReturn(1L).anyTimes();
        EasyMock.expect(host.getRackInfo()).andReturn("rackInfo").anyTimes();
        host.setRackInfo(org.easymock.EasyMock.anyObject());
        EasyMock.expectLastCall().anyTimes();
        return host;
    }

    private org.apache.ambari.server.controller.spi.Predicate buildPredicate(java.lang.String clusterName, java.lang.String hostName) {
        org.apache.ambari.server.controller.utilities.PredicateBuilder builder = new org.apache.ambari.server.controller.utilities.PredicateBuilder();
        if ((clusterName != null) && (hostName != null)) {
            return builder.property(org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_CLUSTER_NAME_PROPERTY_ID).equals(clusterName).and().property(org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_HOST_NAME_PROPERTY_ID).equals(hostName).toPredicate();
        }
        return clusterName != null ? builder.property(org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_CLUSTER_NAME_PROPERTY_ID).equals(clusterName).toPredicate() : builder.property(org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_HOST_NAME_PROPERTY_ID).equals(hostName).toPredicate();
    }
}