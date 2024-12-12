package org.apache.ambari.server.controller.internal;
import org.easymock.Capture;
import org.easymock.EasyMock;
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
import static org.easymock.EasyMock.isNull;
import static org.easymock.EasyMock.newCapture;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
public class ServiceResourceProviderTest {
    @org.junit.BeforeClass
    public static void resetDefaultServiceCalculatedState() throws java.lang.NoSuchFieldException, java.lang.IllegalAccessException {
        java.lang.reflect.Field clustersProviderField = org.apache.ambari.server.controller.utilities.state.DefaultServiceCalculatedState.class.getDeclaredField("clustersProvider");
        clustersProviderField.setAccessible(true);
        clustersProviderField.set(null, null);
    }

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
        org.apache.ambari.server.controller.AmbariManagementController managementController = EasyMock.createNiceMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.state.Clusters clusters = EasyMock.createNiceMock(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.state.Cluster cluster = EasyMock.createNiceMock(org.apache.ambari.server.state.Cluster.class);
        org.apache.ambari.server.state.Service service = EasyMock.createNiceMock(org.apache.ambari.server.state.Service.class);
        org.apache.ambari.server.state.StackId stackId = new org.apache.ambari.server.state.StackId("HDP-2.5");
        org.apache.ambari.server.state.ServiceFactory serviceFactory = EasyMock.createNiceMock(org.apache.ambari.server.state.ServiceFactory.class);
        org.apache.ambari.server.api.services.AmbariMetaInfo ambariMetaInfo = EasyMock.createNiceMock(org.apache.ambari.server.api.services.AmbariMetaInfo.class);
        org.apache.ambari.server.state.ServiceInfo serviceInfo = EasyMock.createNiceMock(org.apache.ambari.server.state.ServiceInfo.class);
        EasyMock.expect(managementController.getClusters()).andReturn(clusters).anyTimes();
        EasyMock.expect(managementController.getAmbariMetaInfo()).andReturn(ambariMetaInfo).anyTimes();
        EasyMock.expect(cluster.addService(EasyMock.eq("Service100"), org.easymock.EasyMock.anyObject(org.apache.ambari.server.orm.entities.RepositoryVersionEntity.class))).andReturn(service);
        EasyMock.expect(clusters.getCluster("Cluster100")).andReturn(cluster).anyTimes();
        EasyMock.expect(cluster.getService("Service100")).andReturn(null);
        EasyMock.expect(cluster.getDesiredStackVersion()).andReturn(stackId).anyTimes();
        EasyMock.expect(cluster.getClusterId()).andReturn(2L).anyTimes();
        EasyMock.expect(ambariMetaInfo.isValidService(((java.lang.String) (EasyMock.anyObject())), ((java.lang.String) (EasyMock.anyObject())), ((java.lang.String) (EasyMock.anyObject())))).andReturn(true);
        EasyMock.expect(ambariMetaInfo.getService(((java.lang.String) (EasyMock.anyObject())), ((java.lang.String) (EasyMock.anyObject())), ((java.lang.String) (EasyMock.anyObject())))).andReturn(serviceInfo).anyTimes();
        EasyMock.replay(managementController, clusters, cluster, service, ambariMetaInfo, serviceFactory, serviceInfo);
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        org.apache.ambari.server.controller.spi.ResourceProvider provider = org.apache.ambari.server.controller.internal.ServiceResourceProviderTest.getServiceProvider(managementController, true, null);
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> propertySet = new java.util.LinkedHashSet<>();
        java.util.Map<java.lang.String, java.lang.Object> properties = new java.util.LinkedHashMap<>();
        properties.put(org.apache.ambari.server.controller.internal.ServiceResourceProvider.SERVICE_CLUSTER_NAME_PROPERTY_ID, "Cluster100");
        properties.put(org.apache.ambari.server.controller.internal.ServiceResourceProvider.SERVICE_SERVICE_NAME_PROPERTY_ID, "Service100");
        properties.put(org.apache.ambari.server.controller.internal.ServiceResourceProvider.SERVICE_SERVICE_STATE_PROPERTY_ID, "INIT");
        properties.put(org.apache.ambari.server.controller.internal.ServiceResourceProvider.SERVICE_DESIRED_STACK_PROPERTY_ID, "HDP-1.1");
        properties.put(org.apache.ambari.server.controller.internal.ServiceResourceProvider.SERVICE_DESIRED_REPO_VERSION_ID_PROPERTY_ID, "1");
        propertySet.add(properties);
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getCreateRequest(propertySet, null);
        provider.createResources(request);
        EasyMock.verify(managementController, clusters, cluster, service, ambariMetaInfo, serviceFactory, serviceInfo);
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
        org.apache.ambari.server.controller.AmbariManagementController managementController = EasyMock.createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.state.Clusters clusters = EasyMock.createNiceMock(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.state.Cluster cluster = EasyMock.createNiceMock(org.apache.ambari.server.state.Cluster.class);
        org.apache.ambari.server.state.Service service0 = EasyMock.createNiceMock(org.apache.ambari.server.state.Service.class);
        org.apache.ambari.server.state.Service service1 = EasyMock.createNiceMock(org.apache.ambari.server.state.Service.class);
        org.apache.ambari.server.state.Service service2 = EasyMock.createNiceMock(org.apache.ambari.server.state.Service.class);
        org.apache.ambari.server.state.Service service3 = EasyMock.createNiceMock(org.apache.ambari.server.state.Service.class);
        org.apache.ambari.server.state.Service service4 = EasyMock.createNiceMock(org.apache.ambari.server.state.Service.class);
        org.apache.ambari.server.controller.ServiceResponse serviceResponse0 = EasyMock.createNiceMock(org.apache.ambari.server.controller.ServiceResponse.class);
        org.apache.ambari.server.controller.ServiceResponse serviceResponse1 = EasyMock.createNiceMock(org.apache.ambari.server.controller.ServiceResponse.class);
        org.apache.ambari.server.controller.ServiceResponse serviceResponse2 = EasyMock.createNiceMock(org.apache.ambari.server.controller.ServiceResponse.class);
        org.apache.ambari.server.controller.ServiceResponse serviceResponse3 = EasyMock.createNiceMock(org.apache.ambari.server.controller.ServiceResponse.class);
        org.apache.ambari.server.controller.ServiceResponse serviceResponse4 = EasyMock.createNiceMock(org.apache.ambari.server.controller.ServiceResponse.class);
        org.apache.ambari.server.state.StackId stackId = EasyMock.createNiceMock(org.apache.ambari.server.state.StackId.class);
        org.apache.ambari.server.state.ServiceFactory serviceFactory = EasyMock.createNiceMock(org.apache.ambari.server.state.ServiceFactory.class);
        org.apache.ambari.server.api.services.AmbariMetaInfo ambariMetaInfo = EasyMock.createNiceMock(org.apache.ambari.server.api.services.AmbariMetaInfo.class);
        java.util.Map<java.lang.String, org.apache.ambari.server.state.Service> allResponseMap = new java.util.HashMap<>();
        allResponseMap.put("Service100", service0);
        allResponseMap.put("Service101", service1);
        allResponseMap.put("Service102", service2);
        allResponseMap.put("Service103", service3);
        allResponseMap.put("Service104", service4);
        EasyMock.expect(managementController.getClusters()).andReturn(clusters).anyTimes();
        EasyMock.expect(managementController.getAmbariMetaInfo()).andReturn(ambariMetaInfo).anyTimes();
        EasyMock.expect(managementController.getHostComponents(org.easymock.EasyMock.anyObject())).andReturn(java.util.Collections.emptySet()).anyTimes();
        EasyMock.expect(clusters.getCluster("Cluster100")).andReturn(cluster).anyTimes();
        EasyMock.expect(cluster.getServices()).andReturn(allResponseMap).anyTimes();
        EasyMock.expect(cluster.getService("Service102")).andReturn(service2);
        EasyMock.expect(service0.convertToResponse()).andReturn(serviceResponse0).anyTimes();
        EasyMock.expect(service1.convertToResponse()).andReturn(serviceResponse1).anyTimes();
        EasyMock.expect(service2.convertToResponse()).andReturn(serviceResponse2).anyTimes();
        EasyMock.expect(service3.convertToResponse()).andReturn(serviceResponse3).anyTimes();
        EasyMock.expect(service4.convertToResponse()).andReturn(serviceResponse4).anyTimes();
        EasyMock.expect(service0.getName()).andReturn("Service100").anyTimes();
        EasyMock.expect(service1.getName()).andReturn("Service101").anyTimes();
        EasyMock.expect(service2.getName()).andReturn("Service102").anyTimes();
        EasyMock.expect(service3.getName()).andReturn("Service103").anyTimes();
        EasyMock.expect(service4.getName()).andReturn("Service104").anyTimes();
        EasyMock.expect(service0.getDesiredState()).andReturn(org.apache.ambari.server.state.State.INIT);
        EasyMock.expect(service1.getDesiredState()).andReturn(org.apache.ambari.server.state.State.INSTALLED);
        EasyMock.expect(service2.getDesiredState()).andReturn(org.apache.ambari.server.state.State.INIT);
        EasyMock.expect(service3.getDesiredState()).andReturn(org.apache.ambari.server.state.State.INSTALLED);
        EasyMock.expect(service4.getDesiredState()).andReturn(org.apache.ambari.server.state.State.INIT);
        EasyMock.expect(serviceResponse0.getClusterName()).andReturn("Cluster100").anyTimes();
        EasyMock.expect(serviceResponse0.getServiceName()).andReturn("Service100").anyTimes();
        EasyMock.expect(serviceResponse1.getClusterName()).andReturn("Cluster100").anyTimes();
        EasyMock.expect(serviceResponse1.getServiceName()).andReturn("Service101").anyTimes();
        EasyMock.expect(serviceResponse2.getClusterName()).andReturn("Cluster100").anyTimes();
        EasyMock.expect(serviceResponse2.getServiceName()).andReturn("Service102").anyTimes();
        EasyMock.expect(serviceResponse3.getClusterName()).andReturn("Cluster100").anyTimes();
        EasyMock.expect(serviceResponse3.getServiceName()).andReturn("Service103").anyTimes();
        EasyMock.expect(serviceResponse4.getClusterName()).andReturn("Cluster100").anyTimes();
        EasyMock.expect(serviceResponse4.getServiceName()).andReturn("Service104").anyTimes();
        EasyMock.replay(managementController, clusters, cluster, service0, service1, service2, service3, service4, serviceResponse0, serviceResponse1, serviceResponse2, serviceResponse3, serviceResponse4, ambariMetaInfo, stackId, serviceFactory);
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        org.apache.ambari.server.controller.spi.ResourceProvider provider = org.apache.ambari.server.controller.internal.ServiceResourceProviderTest.getServiceProvider(managementController);
        java.util.Set<java.lang.String> propertyIds = new java.util.HashSet<>();
        propertyIds.add(org.apache.ambari.server.controller.internal.ServiceResourceProvider.SERVICE_CLUSTER_NAME_PROPERTY_ID);
        propertyIds.add(org.apache.ambari.server.controller.internal.ServiceResourceProvider.SERVICE_SERVICE_NAME_PROPERTY_ID);
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.ServiceResourceProvider.SERVICE_CLUSTER_NAME_PROPERTY_ID).equals("Cluster100").toPredicate();
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest("ServiceInfo");
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = provider.getResources(request, predicate);
        org.junit.Assert.assertEquals(5, resources.size());
        java.util.Set<java.lang.String> names = new java.util.HashSet<>();
        for (org.apache.ambari.server.controller.spi.Resource resource : resources) {
            java.lang.String clusterName = ((java.lang.String) (resource.getPropertyValue(org.apache.ambari.server.controller.internal.ServiceResourceProvider.SERVICE_CLUSTER_NAME_PROPERTY_ID)));
            org.junit.Assert.assertEquals("Cluster100", clusterName);
            names.add(((java.lang.String) (resource.getPropertyValue(org.apache.ambari.server.controller.internal.ServiceResourceProvider.SERVICE_SERVICE_NAME_PROPERTY_ID))));
        }
        for (org.apache.ambari.server.state.Service service : allResponseMap.values()) {
            org.junit.Assert.assertTrue(names.contains(service.getName()));
        }
        predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.ServiceResourceProvider.SERVICE_CLUSTER_NAME_PROPERTY_ID).equals("Cluster100").and().property(org.apache.ambari.server.controller.internal.ServiceResourceProvider.SERVICE_SERVICE_NAME_PROPERTY_ID).equals("Service102").toPredicate();
        request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest("ServiceInfo");
        resources = provider.getResources(request, predicate);
        org.junit.Assert.assertEquals(1, resources.size());
        org.junit.Assert.assertEquals("Service102", resources.iterator().next().getPropertyValue(org.apache.ambari.server.controller.internal.ServiceResourceProvider.SERVICE_SERVICE_NAME_PROPERTY_ID));
        predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.ServiceResourceProvider.SERVICE_CLUSTER_NAME_PROPERTY_ID).equals("Cluster100").and().property(org.apache.ambari.server.controller.internal.ServiceResourceProvider.SERVICE_SERVICE_STATE_PROPERTY_ID).equals("INIT").toPredicate();
        request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(propertyIds);
        resources = provider.getResources(request, predicate);
        org.junit.Assert.assertEquals(3, resources.size());
        names = new java.util.HashSet<>();
        for (org.apache.ambari.server.controller.spi.Resource resource : resources) {
            java.lang.String clusterName = ((java.lang.String) (resource.getPropertyValue(org.apache.ambari.server.controller.internal.ServiceResourceProvider.SERVICE_CLUSTER_NAME_PROPERTY_ID)));
            org.junit.Assert.assertEquals("Cluster100", clusterName);
            names.add(((java.lang.String) (resource.getPropertyValue(org.apache.ambari.server.controller.internal.ServiceResourceProvider.SERVICE_SERVICE_NAME_PROPERTY_ID))));
        }
        EasyMock.verify(managementController, clusters, cluster, service0, service1, service2, service3, service4, serviceResponse0, serviceResponse1, serviceResponse2, serviceResponse3, serviceResponse4, ambariMetaInfo, stackId, serviceFactory);
    }

    @org.junit.Test
    public void testGetResources_KerberosSpecificProperties() throws java.lang.Exception {
        org.apache.ambari.server.controller.AmbariManagementController managementController = EasyMock.createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.state.Clusters clusters = EasyMock.createNiceMock(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.state.Cluster cluster = EasyMock.createNiceMock(org.apache.ambari.server.state.Cluster.class);
        org.apache.ambari.server.state.Service service0 = EasyMock.createNiceMock(org.apache.ambari.server.state.Service.class);
        org.apache.ambari.server.controller.ServiceResponse serviceResponse0 = EasyMock.createNiceMock(org.apache.ambari.server.controller.ServiceResponse.class);
        org.apache.ambari.server.state.StackId stackId = EasyMock.createNiceMock(org.apache.ambari.server.state.StackId.class);
        org.apache.ambari.server.state.ServiceFactory serviceFactory = EasyMock.createNiceMock(org.apache.ambari.server.state.ServiceFactory.class);
        org.apache.ambari.server.api.services.AmbariMetaInfo ambariMetaInfo = EasyMock.createNiceMock(org.apache.ambari.server.api.services.AmbariMetaInfo.class);
        org.apache.ambari.server.controller.KerberosHelper kerberosHeper = EasyMock.createStrictMock(org.apache.ambari.server.controller.KerberosHelper.class);
        java.util.Map<java.lang.String, org.apache.ambari.server.state.Service> allResponseMap = new java.util.HashMap<>();
        allResponseMap.put("KERBEROS", service0);
        EasyMock.expect(managementController.getClusters()).andReturn(clusters).anyTimes();
        EasyMock.expect(managementController.getAmbariMetaInfo()).andReturn(ambariMetaInfo).anyTimes();
        EasyMock.expect(managementController.getHostComponents(org.easymock.EasyMock.anyObject())).andReturn(java.util.Collections.emptySet()).anyTimes();
        EasyMock.expect(clusters.getCluster("Cluster100")).andReturn(cluster).anyTimes();
        EasyMock.expect(cluster.getServices()).andReturn(allResponseMap).anyTimes();
        EasyMock.expect(cluster.getService("KERBEROS")).andReturn(service0);
        EasyMock.expect(service0.convertToResponse()).andReturn(serviceResponse0).anyTimes();
        EasyMock.expect(service0.getName()).andReturn("Service100").anyTimes();
        EasyMock.expect(serviceResponse0.getClusterName()).andReturn("Cluster100").anyTimes();
        EasyMock.expect(serviceResponse0.getServiceName()).andReturn("KERBEROS").anyTimes();
        kerberosHeper.validateKDCCredentials(cluster);
        EasyMock.replay(managementController, clusters, cluster, service0, serviceResponse0, ambariMetaInfo, stackId, serviceFactory, kerberosHeper);
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator());
        org.apache.ambari.server.controller.spi.ResourceProvider provider = org.apache.ambari.server.controller.internal.ServiceResourceProviderTest.getServiceProvider(managementController);
        java.lang.Class<?> c = provider.getClass();
        java.lang.reflect.Field f = c.getDeclaredField("kerberosHelper");
        f.setAccessible(true);
        f.set(provider, kerberosHeper);
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.ServiceResourceProvider.SERVICE_CLUSTER_NAME_PROPERTY_ID).equals("Cluster100").and().property(org.apache.ambari.server.controller.internal.ServiceResourceProvider.SERVICE_SERVICE_NAME_PROPERTY_ID).equals("KERBEROS").toPredicate();
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest("ServiceInfo", "Services");
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = provider.getResources(request, predicate);
        org.junit.Assert.assertEquals(1, resources.size());
        for (org.apache.ambari.server.controller.spi.Resource resource : resources) {
            org.junit.Assert.assertEquals("Cluster100", resource.getPropertyValue(org.apache.ambari.server.controller.internal.ServiceResourceProvider.SERVICE_CLUSTER_NAME_PROPERTY_ID));
            org.junit.Assert.assertEquals("KERBEROS", resource.getPropertyValue(org.apache.ambari.server.controller.internal.ServiceResourceProvider.SERVICE_SERVICE_NAME_PROPERTY_ID));
            org.junit.Assert.assertEquals("OK", resource.getPropertyValue("Services/attributes/kdc_validation_result"));
            org.junit.Assert.assertEquals("", resource.getPropertyValue("Services/attributes/kdc_validation_failure_details"));
        }
        EasyMock.verify(managementController, clusters, cluster, service0, serviceResponse0, ambariMetaInfo, stackId, serviceFactory, kerberosHeper);
    }

    @org.junit.Test
    public void testGetResources_KerberosSpecificProperties_NoKDCValidation() throws java.lang.Exception {
        org.apache.ambari.server.controller.AmbariManagementController managementController = EasyMock.createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.state.Clusters clusters = EasyMock.createNiceMock(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.state.Cluster cluster = EasyMock.createNiceMock(org.apache.ambari.server.state.Cluster.class);
        org.apache.ambari.server.state.Service service0 = EasyMock.createNiceMock(org.apache.ambari.server.state.Service.class);
        org.apache.ambari.server.controller.ServiceResponse serviceResponse0 = EasyMock.createNiceMock(org.apache.ambari.server.controller.ServiceResponse.class);
        org.apache.ambari.server.state.StackId stackId = EasyMock.createNiceMock(org.apache.ambari.server.state.StackId.class);
        org.apache.ambari.server.state.ServiceFactory serviceFactory = EasyMock.createNiceMock(org.apache.ambari.server.state.ServiceFactory.class);
        org.apache.ambari.server.api.services.AmbariMetaInfo ambariMetaInfo = EasyMock.createNiceMock(org.apache.ambari.server.api.services.AmbariMetaInfo.class);
        org.apache.ambari.server.controller.KerberosHelper kerberosHelper = EasyMock.createStrictMock(org.apache.ambari.server.controller.KerberosHelper.class);
        java.util.Map<java.lang.String, org.apache.ambari.server.state.Service> allResponseMap = new java.util.HashMap<>();
        allResponseMap.put("KERBEROS", service0);
        EasyMock.expect(managementController.getClusters()).andReturn(clusters).anyTimes();
        EasyMock.expect(managementController.getAmbariMetaInfo()).andReturn(ambariMetaInfo).anyTimes();
        EasyMock.expect(managementController.getHostComponents(org.easymock.EasyMock.anyObject())).andReturn(java.util.Collections.emptySet()).anyTimes();
        EasyMock.expect(clusters.getCluster("Cluster100")).andReturn(cluster).anyTimes();
        EasyMock.expect(cluster.getServices()).andReturn(allResponseMap).anyTimes();
        EasyMock.expect(cluster.getService("KERBEROS")).andReturn(service0);
        EasyMock.expect(service0.convertToResponse()).andReturn(serviceResponse0).anyTimes();
        EasyMock.expect(service0.getName()).andReturn("Service100").anyTimes();
        EasyMock.expect(serviceResponse0.getClusterName()).andReturn("Cluster100").anyTimes();
        EasyMock.expect(serviceResponse0.getServiceName()).andReturn("KERBEROS").anyTimes();
        EasyMock.replay(managementController, clusters, cluster, service0, serviceResponse0, ambariMetaInfo, stackId, serviceFactory, kerberosHelper);
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator());
        org.apache.ambari.server.controller.spi.ResourceProvider provider = org.apache.ambari.server.controller.internal.ServiceResourceProviderTest.getServiceProvider(managementController);
        java.lang.Class<?> c = provider.getClass();
        java.lang.reflect.Field f = c.getDeclaredField("kerberosHelper");
        f.setAccessible(true);
        f.set(provider, kerberosHelper);
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.ServiceResourceProvider.SERVICE_CLUSTER_NAME_PROPERTY_ID).equals("Cluster100").and().property(org.apache.ambari.server.controller.internal.ServiceResourceProvider.SERVICE_SERVICE_NAME_PROPERTY_ID).equals("KERBEROS").toPredicate();
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest("ServiceInfo");
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = provider.getResources(request, predicate);
        org.junit.Assert.assertEquals(1, resources.size());
        for (org.apache.ambari.server.controller.spi.Resource resource : resources) {
            org.junit.Assert.assertEquals("Cluster100", resource.getPropertyValue(org.apache.ambari.server.controller.internal.ServiceResourceProvider.SERVICE_CLUSTER_NAME_PROPERTY_ID));
            org.junit.Assert.assertEquals("KERBEROS", resource.getPropertyValue(org.apache.ambari.server.controller.internal.ServiceResourceProvider.SERVICE_SERVICE_NAME_PROPERTY_ID));
        }
        EasyMock.verify(managementController, clusters, cluster, service0, serviceResponse0, ambariMetaInfo, stackId, serviceFactory, kerberosHelper);
    }

    @org.junit.Test
    public void testGetResources_KerberosSpecificProperties_KDCInvalidCredentials() throws java.lang.Exception {
        org.apache.ambari.server.controller.AmbariManagementController managementController = EasyMock.createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.state.Clusters clusters = EasyMock.createNiceMock(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.state.Cluster cluster = EasyMock.createNiceMock(org.apache.ambari.server.state.Cluster.class);
        org.apache.ambari.server.state.Service service0 = EasyMock.createNiceMock(org.apache.ambari.server.state.Service.class);
        org.apache.ambari.server.controller.ServiceResponse serviceResponse0 = EasyMock.createNiceMock(org.apache.ambari.server.controller.ServiceResponse.class);
        org.apache.ambari.server.state.StackId stackId = EasyMock.createNiceMock(org.apache.ambari.server.state.StackId.class);
        org.apache.ambari.server.state.ServiceFactory serviceFactory = EasyMock.createNiceMock(org.apache.ambari.server.state.ServiceFactory.class);
        org.apache.ambari.server.api.services.AmbariMetaInfo ambariMetaInfo = EasyMock.createNiceMock(org.apache.ambari.server.api.services.AmbariMetaInfo.class);
        org.apache.ambari.server.controller.KerberosHelper kerberosHeper = EasyMock.createStrictMock(org.apache.ambari.server.controller.KerberosHelper.class);
        java.util.Map<java.lang.String, org.apache.ambari.server.state.Service> allResponseMap = new java.util.HashMap<>();
        allResponseMap.put("KERBEROS", service0);
        EasyMock.expect(managementController.getClusters()).andReturn(clusters).anyTimes();
        EasyMock.expect(managementController.getAmbariMetaInfo()).andReturn(ambariMetaInfo).anyTimes();
        EasyMock.expect(managementController.getHostComponents(org.easymock.EasyMock.anyObject())).andReturn(java.util.Collections.emptySet()).anyTimes();
        EasyMock.expect(clusters.getCluster("Cluster100")).andReturn(cluster).anyTimes();
        EasyMock.expect(cluster.getServices()).andReturn(allResponseMap).anyTimes();
        EasyMock.expect(cluster.getService("KERBEROS")).andReturn(service0);
        EasyMock.expect(service0.convertToResponse()).andReturn(serviceResponse0).anyTimes();
        EasyMock.expect(service0.getName()).andReturn("Service100").anyTimes();
        EasyMock.expect(serviceResponse0.getClusterName()).andReturn("Cluster100").anyTimes();
        EasyMock.expect(serviceResponse0.getServiceName()).andReturn("KERBEROS").anyTimes();
        kerberosHeper.validateKDCCredentials(cluster);
        EasyMock.expectLastCall().andThrow(new org.apache.ambari.server.serveraction.kerberos.KerberosAdminAuthenticationException("Invalid KDC administrator credentials."));
        EasyMock.replay(managementController, clusters, cluster, service0, serviceResponse0, ambariMetaInfo, stackId, serviceFactory, kerberosHeper);
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator());
        org.apache.ambari.server.controller.spi.ResourceProvider provider = org.apache.ambari.server.controller.internal.ServiceResourceProviderTest.getServiceProvider(managementController);
        java.lang.Class<?> c = provider.getClass();
        java.lang.reflect.Field f = c.getDeclaredField("kerberosHelper");
        f.setAccessible(true);
        f.set(provider, kerberosHeper);
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.ServiceResourceProvider.SERVICE_CLUSTER_NAME_PROPERTY_ID).equals("Cluster100").and().property(org.apache.ambari.server.controller.internal.ServiceResourceProvider.SERVICE_SERVICE_NAME_PROPERTY_ID).equals("KERBEROS").toPredicate();
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest("ServiceInfo", "Services");
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = provider.getResources(request, predicate);
        org.junit.Assert.assertEquals(1, resources.size());
        for (org.apache.ambari.server.controller.spi.Resource resource : resources) {
            org.junit.Assert.assertEquals("Cluster100", resource.getPropertyValue(org.apache.ambari.server.controller.internal.ServiceResourceProvider.SERVICE_CLUSTER_NAME_PROPERTY_ID));
            org.junit.Assert.assertEquals("KERBEROS", resource.getPropertyValue(org.apache.ambari.server.controller.internal.ServiceResourceProvider.SERVICE_SERVICE_NAME_PROPERTY_ID));
            org.junit.Assert.assertEquals("INVALID_CREDENTIALS", resource.getPropertyValue("Services/attributes/kdc_validation_result"));
            org.junit.Assert.assertEquals("Invalid KDC administrator credentials.", resource.getPropertyValue("Services/attributes/kdc_validation_failure_details"));
        }
        EasyMock.verify(managementController, clusters, cluster, service0, serviceResponse0, ambariMetaInfo, stackId, serviceFactory, kerberosHeper);
    }

    @org.junit.Test
    public void testGetResources_KerberosSpecificProperties_KDCMissingCredentials() throws java.lang.Exception {
        org.apache.ambari.server.controller.AmbariManagementController managementController = EasyMock.createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.state.Clusters clusters = EasyMock.createNiceMock(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.state.Cluster cluster = EasyMock.createNiceMock(org.apache.ambari.server.state.Cluster.class);
        org.apache.ambari.server.state.Service service0 = EasyMock.createNiceMock(org.apache.ambari.server.state.Service.class);
        org.apache.ambari.server.controller.ServiceResponse serviceResponse0 = EasyMock.createNiceMock(org.apache.ambari.server.controller.ServiceResponse.class);
        org.apache.ambari.server.state.StackId stackId = EasyMock.createNiceMock(org.apache.ambari.server.state.StackId.class);
        org.apache.ambari.server.state.ServiceFactory serviceFactory = EasyMock.createNiceMock(org.apache.ambari.server.state.ServiceFactory.class);
        org.apache.ambari.server.api.services.AmbariMetaInfo ambariMetaInfo = EasyMock.createNiceMock(org.apache.ambari.server.api.services.AmbariMetaInfo.class);
        org.apache.ambari.server.controller.KerberosHelper kerberosHeper = EasyMock.createStrictMock(org.apache.ambari.server.controller.KerberosHelper.class);
        java.util.Map<java.lang.String, org.apache.ambari.server.state.Service> allResponseMap = new java.util.HashMap<>();
        allResponseMap.put("KERBEROS", service0);
        EasyMock.expect(managementController.getClusters()).andReturn(clusters).anyTimes();
        EasyMock.expect(managementController.getAmbariMetaInfo()).andReturn(ambariMetaInfo).anyTimes();
        EasyMock.expect(managementController.getHostComponents(org.easymock.EasyMock.anyObject())).andReturn(java.util.Collections.emptySet()).anyTimes();
        EasyMock.expect(clusters.getCluster("Cluster100")).andReturn(cluster).anyTimes();
        EasyMock.expect(cluster.getServices()).andReturn(allResponseMap).anyTimes();
        EasyMock.expect(cluster.getService("KERBEROS")).andReturn(service0);
        EasyMock.expect(service0.convertToResponse()).andReturn(serviceResponse0).anyTimes();
        EasyMock.expect(service0.getName()).andReturn("Service100").anyTimes();
        EasyMock.expect(serviceResponse0.getClusterName()).andReturn("Cluster100").anyTimes();
        EasyMock.expect(serviceResponse0.getServiceName()).andReturn("KERBEROS").anyTimes();
        kerberosHeper.validateKDCCredentials(cluster);
        EasyMock.expectLastCall().andThrow(new org.apache.ambari.server.serveraction.kerberos.KerberosMissingAdminCredentialsException("Missing KDC administrator credentials."));
        EasyMock.replay(managementController, clusters, cluster, service0, serviceResponse0, ambariMetaInfo, stackId, serviceFactory, kerberosHeper);
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator());
        org.apache.ambari.server.controller.spi.ResourceProvider provider = org.apache.ambari.server.controller.internal.ServiceResourceProviderTest.getServiceProvider(managementController);
        java.lang.Class<?> c = provider.getClass();
        java.lang.reflect.Field f = c.getDeclaredField("kerberosHelper");
        f.setAccessible(true);
        f.set(provider, kerberosHeper);
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.ServiceResourceProvider.SERVICE_CLUSTER_NAME_PROPERTY_ID).equals("Cluster100").and().property(org.apache.ambari.server.controller.internal.ServiceResourceProvider.SERVICE_SERVICE_NAME_PROPERTY_ID).equals("KERBEROS").toPredicate();
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest("ServiceInfo", "Services");
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = provider.getResources(request, predicate);
        org.junit.Assert.assertEquals(1, resources.size());
        for (org.apache.ambari.server.controller.spi.Resource resource : resources) {
            org.junit.Assert.assertEquals("Cluster100", resource.getPropertyValue(org.apache.ambari.server.controller.internal.ServiceResourceProvider.SERVICE_CLUSTER_NAME_PROPERTY_ID));
            org.junit.Assert.assertEquals("KERBEROS", resource.getPropertyValue(org.apache.ambari.server.controller.internal.ServiceResourceProvider.SERVICE_SERVICE_NAME_PROPERTY_ID));
            org.junit.Assert.assertEquals("MISSING_CREDENTIALS", resource.getPropertyValue("Services/attributes/kdc_validation_result"));
            org.junit.Assert.assertEquals("Missing KDC administrator credentials.", resource.getPropertyValue("Services/attributes/kdc_validation_failure_details"));
        }
        EasyMock.verify(managementController, clusters, cluster, service0, serviceResponse0, ambariMetaInfo, stackId, serviceFactory, kerberosHeper);
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
        org.apache.ambari.server.controller.MaintenanceStateHelper maintenanceStateHelper = EasyMock.createNiceMock(org.apache.ambari.server.controller.MaintenanceStateHelper.class);
        org.apache.ambari.server.orm.dao.RepositoryVersionDAO repositoryVersionDAO = EasyMock.createNiceMock(org.apache.ambari.server.orm.dao.RepositoryVersionDAO.class);
        org.apache.ambari.server.controller.AmbariManagementController managementController = EasyMock.createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.state.Clusters clusters = EasyMock.createNiceMock(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.state.Cluster cluster = EasyMock.createNiceMock(org.apache.ambari.server.state.Cluster.class);
        org.apache.ambari.server.state.Service service0 = EasyMock.createNiceMock(org.apache.ambari.server.state.Service.class);
        org.apache.ambari.server.state.ServiceFactory serviceFactory = EasyMock.createNiceMock(org.apache.ambari.server.state.ServiceFactory.class);
        org.apache.ambari.server.api.services.AmbariMetaInfo ambariMetaInfo = EasyMock.createNiceMock(org.apache.ambari.server.api.services.AmbariMetaInfo.class);
        org.apache.ambari.server.controller.internal.RequestStageContainer requestStages = EasyMock.createNiceMock(org.apache.ambari.server.controller.internal.RequestStageContainer.class);
        org.apache.ambari.server.controller.RequestStatusResponse requestStatusResponse = EasyMock.createNiceMock(org.apache.ambari.server.controller.RequestStatusResponse.class);
        org.apache.ambari.server.metadata.RoleCommandOrder rco = EasyMock.createNiceMock(org.apache.ambari.server.metadata.RoleCommandOrder.class);
        org.apache.ambari.server.state.StackId stackId = EasyMock.createNiceMock(org.apache.ambari.server.state.StackId.class);
        org.apache.ambari.server.state.ServiceInfo serviceInfo = EasyMock.createNiceMock(org.apache.ambari.server.state.ServiceInfo.class);
        java.util.Map<java.lang.String, java.lang.String> mapRequestProps = new java.util.HashMap<>();
        mapRequestProps.put("context", "Called from a test");
        EasyMock.expect(managementController.getClusters()).andReturn(clusters).anyTimes();
        EasyMock.expect(managementController.getAmbariMetaInfo()).andReturn(ambariMetaInfo).anyTimes();
        EasyMock.expect(clusters.getCluster("Cluster100")).andReturn(cluster).anyTimes();
        EasyMock.expect(cluster.getClusterId()).andReturn(2L).anyTimes();
        EasyMock.expect(cluster.getService("Service102")).andReturn(service0);
        EasyMock.expect(service0.getDesiredState()).andReturn(org.apache.ambari.server.state.State.INSTALLED).anyTimes();
        EasyMock.expect(service0.getServiceComponents()).andReturn(java.util.Collections.emptyMap()).anyTimes();
        EasyMock.expect(stackId.getStackId()).andReturn("HDP-2.5").anyTimes();
        EasyMock.expect(stackId.getStackName()).andReturn("HDP").anyTimes();
        EasyMock.expect(stackId.getStackVersion()).andReturn("2.5").anyTimes();
        EasyMock.expect(service0.getDesiredStackId()).andReturn(stackId).anyTimes();
        EasyMock.expect(service0.getName()).andReturn("Service102").anyTimes();
        EasyMock.expect(serviceInfo.isCredentialStoreSupported()).andReturn(true).anyTimes();
        EasyMock.expect(serviceInfo.isCredentialStoreEnabled()).andReturn(false).anyTimes();
        EasyMock.expect(ambariMetaInfo.getService("HDP", "2.5", "Service102")).andReturn(serviceInfo).anyTimes();
        org.easymock.Capture<java.util.Map<java.lang.String, java.lang.String>> requestPropertiesCapture = EasyMock.newCapture();
        org.easymock.Capture<java.util.Map<org.apache.ambari.server.state.State, java.util.List<org.apache.ambari.server.state.Service>>> changedServicesCapture = EasyMock.newCapture();
        org.easymock.Capture<java.util.Map<org.apache.ambari.server.state.State, java.util.List<org.apache.ambari.server.state.ServiceComponent>>> changedCompsCapture = EasyMock.newCapture();
        org.easymock.Capture<java.util.Map<java.lang.String, java.util.Map<org.apache.ambari.server.state.State, java.util.List<org.apache.ambari.server.state.ServiceComponentHost>>>> changedScHostsCapture = EasyMock.newCapture();
        org.easymock.Capture<java.util.Map<java.lang.String, java.lang.String>> requestParametersCapture = EasyMock.newCapture();
        org.easymock.Capture<java.util.Collection<org.apache.ambari.server.state.ServiceComponentHost>> ignoredScHostsCapture = EasyMock.newCapture();
        org.easymock.Capture<org.apache.ambari.server.state.Cluster> clusterCapture = EasyMock.newCapture();
        EasyMock.expect(managementController.addStages(((org.apache.ambari.server.controller.internal.RequestStageContainer) (EasyMock.isNull())), EasyMock.capture(clusterCapture), EasyMock.capture(requestPropertiesCapture), EasyMock.capture(requestParametersCapture), EasyMock.capture(changedServicesCapture), EasyMock.capture(changedCompsCapture), EasyMock.capture(changedScHostsCapture), EasyMock.capture(ignoredScHostsCapture), EasyMock.anyBoolean(), EasyMock.anyBoolean(), EasyMock.anyBoolean())).andReturn(requestStages);
        requestStages.persist();
        EasyMock.expect(requestStages.getRequestStatusResponse()).andReturn(requestStatusResponse);
        EasyMock.expect(maintenanceStateHelper.isOperationAllowed(EasyMock.anyObject(org.apache.ambari.server.controller.spi.Resource.Type.class), EasyMock.anyObject(org.apache.ambari.server.state.Service.class))).andReturn(true).anyTimes();
        EasyMock.expect(service0.getCluster()).andReturn(cluster).anyTimes();
        EasyMock.expect(managementController.getRoleCommandOrder(cluster)).andReturn(rco).anyTimes();
        EasyMock.expect(rco.getTransitiveServices(EasyMock.eq(service0), EasyMock.eq(org.apache.ambari.server.RoleCommand.START))).andReturn(java.util.Collections.emptySet()).anyTimes();
        EasyMock.replay(managementController, clusters, cluster, rco, maintenanceStateHelper, repositoryVersionDAO, service0, serviceFactory, ambariMetaInfo, requestStages, requestStatusResponse, stackId, serviceInfo);
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        org.apache.ambari.server.controller.internal.ServiceResourceProvider provider = org.apache.ambari.server.controller.internal.ServiceResourceProviderTest.getServiceProvider(managementController, maintenanceStateHelper, repositoryVersionDAO);
        java.util.Map<java.lang.String, java.lang.Object> properties = new java.util.LinkedHashMap<>();
        properties.put(org.apache.ambari.server.controller.internal.ServiceResourceProvider.SERVICE_SERVICE_STATE_PROPERTY_ID, "STARTED");
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getUpdateRequest(properties, mapRequestProps);
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.ServiceResourceProvider.SERVICE_CLUSTER_NAME_PROPERTY_ID).equals("Cluster100").and().property(org.apache.ambari.server.controller.internal.ServiceResourceProvider.SERVICE_SERVICE_NAME_PROPERTY_ID).equals("Service102").toPredicate();
        provider.updateResources(request, predicate);
        EasyMock.verify(managementController, clusters, cluster, maintenanceStateHelper, service0, serviceFactory, ambariMetaInfo, requestStages, requestStatusResponse, stackId, serviceInfo);
    }

    @org.junit.Test
    public void testReconfigureClientsFlagAsAdministrator() throws java.lang.Exception {
        testReconfigureClientsFlag(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator());
    }

    @org.junit.Test
    public void testReconfigureClientsFlagAsClusterAdministrator() throws java.lang.Exception {
        testReconfigureClientsFlag(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator("clusterAdmin"));
    }

    @org.junit.Test
    public void testReconfigureClientsFlagAsServiceAdministrator() throws java.lang.Exception {
        testReconfigureClientsFlag(org.apache.ambari.server.security.TestAuthenticationFactory.createServiceAdministrator());
    }

    private void testReconfigureClientsFlag(org.springframework.security.core.Authentication authentication) throws java.lang.Exception {
        org.apache.ambari.server.controller.MaintenanceStateHelper maintenanceStateHelper = EasyMock.createNiceMock(org.apache.ambari.server.controller.MaintenanceStateHelper.class);
        org.apache.ambari.server.orm.dao.RepositoryVersionDAO repositoryVersionDAO = EasyMock.createNiceMock(org.apache.ambari.server.orm.dao.RepositoryVersionDAO.class);
        org.apache.ambari.server.controller.AmbariManagementController managementController1 = EasyMock.createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.controller.AmbariManagementController managementController2 = EasyMock.createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.state.Clusters clusters = EasyMock.createNiceMock(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.state.Cluster cluster = EasyMock.createNiceMock(org.apache.ambari.server.state.Cluster.class);
        org.apache.ambari.server.state.Service service0 = EasyMock.createNiceMock(org.apache.ambari.server.state.Service.class);
        org.apache.ambari.server.controller.ServiceResponse serviceResponse0 = EasyMock.createNiceMock(org.apache.ambari.server.controller.ServiceResponse.class);
        org.apache.ambari.server.api.services.AmbariMetaInfo ambariMetaInfo = EasyMock.createNiceMock(org.apache.ambari.server.api.services.AmbariMetaInfo.class);
        org.apache.ambari.server.controller.internal.RequestStageContainer requestStages1 = EasyMock.createNiceMock(org.apache.ambari.server.controller.internal.RequestStageContainer.class);
        org.apache.ambari.server.controller.internal.RequestStageContainer requestStages2 = EasyMock.createNiceMock(org.apache.ambari.server.controller.internal.RequestStageContainer.class);
        org.apache.ambari.server.controller.RequestStatusResponse response1 = EasyMock.createNiceMock(org.apache.ambari.server.controller.RequestStatusResponse.class);
        org.apache.ambari.server.controller.RequestStatusResponse response2 = EasyMock.createNiceMock(org.apache.ambari.server.controller.RequestStatusResponse.class);
        org.apache.ambari.server.metadata.RoleCommandOrder rco = EasyMock.createNiceMock(org.apache.ambari.server.metadata.RoleCommandOrder.class);
        org.apache.ambari.server.state.StackId stackId = EasyMock.createNiceMock(org.apache.ambari.server.state.StackId.class);
        org.apache.ambari.server.state.ServiceInfo serviceInfo = EasyMock.createNiceMock(org.apache.ambari.server.state.ServiceInfo.class);
        java.util.Map<java.lang.String, java.lang.String> mapRequestProps = new java.util.HashMap<>();
        mapRequestProps.put("context", "Called from a test");
        EasyMock.expect(managementController1.getHostComponents(org.easymock.EasyMock.anyObject())).andReturn(java.util.Collections.emptySet()).anyTimes();
        EasyMock.expect(managementController2.getHostComponents(org.easymock.EasyMock.anyObject())).andReturn(java.util.Collections.emptySet()).anyTimes();
        EasyMock.expect(clusters.getCluster("Cluster100")).andReturn(cluster).anyTimes();
        EasyMock.expect(managementController1.getClusters()).andReturn(clusters).anyTimes();
        EasyMock.expect(managementController1.getAmbariMetaInfo()).andReturn(ambariMetaInfo).anyTimes();
        EasyMock.expect(managementController2.getClusters()).andReturn(clusters).anyTimes();
        EasyMock.expect(managementController2.getAmbariMetaInfo()).andReturn(ambariMetaInfo).anyTimes();
        EasyMock.expect(cluster.getClusterId()).andReturn(2L).anyTimes();
        EasyMock.expect(cluster.getService("Service102")).andReturn(service0).anyTimes();
        EasyMock.expect(stackId.getStackId()).andReturn("HDP-2.5").anyTimes();
        EasyMock.expect(stackId.getStackName()).andReturn("HDP").anyTimes();
        EasyMock.expect(stackId.getStackVersion()).andReturn("2.5").anyTimes();
        EasyMock.expect(service0.getDesiredStackId()).andReturn(stackId).anyTimes();
        EasyMock.expect(service0.getName()).andReturn("Service102").anyTimes();
        EasyMock.expect(serviceInfo.isCredentialStoreSupported()).andReturn(true).anyTimes();
        EasyMock.expect(serviceInfo.isCredentialStoreEnabled()).andReturn(false).anyTimes();
        EasyMock.expect(ambariMetaInfo.getService("HDP", "2.5", "Service102")).andReturn(serviceInfo).anyTimes();
        EasyMock.expect(service0.convertToResponse()).andReturn(serviceResponse0).anyTimes();
        EasyMock.expect(service0.getDesiredState()).andReturn(org.apache.ambari.server.state.State.INSTALLED).anyTimes();
        EasyMock.expect(service0.getServiceComponents()).andReturn(java.util.Collections.emptyMap()).anyTimes();
        EasyMock.expect(serviceResponse0.getClusterName()).andReturn("Cluster100").anyTimes();
        EasyMock.expect(serviceResponse0.getServiceName()).andReturn("Service102").anyTimes();
        org.easymock.Capture<java.util.Map<java.lang.String, java.lang.String>> requestPropertiesCapture = EasyMock.newCapture();
        org.easymock.Capture<java.util.Map<org.apache.ambari.server.state.State, java.util.List<org.apache.ambari.server.state.Service>>> changedServicesCapture = EasyMock.newCapture();
        org.easymock.Capture<java.util.Map<org.apache.ambari.server.state.State, java.util.List<org.apache.ambari.server.state.ServiceComponent>>> changedCompsCapture = EasyMock.newCapture();
        org.easymock.Capture<java.util.Map<java.lang.String, java.util.Map<org.apache.ambari.server.state.State, java.util.List<org.apache.ambari.server.state.ServiceComponentHost>>>> changedScHostsCapture = EasyMock.newCapture();
        org.easymock.Capture<java.util.Map<java.lang.String, java.lang.String>> requestParametersCapture = EasyMock.newCapture();
        org.easymock.Capture<java.util.Collection<org.apache.ambari.server.state.ServiceComponentHost>> ignoredScHostsCapture = EasyMock.newCapture();
        org.easymock.Capture<org.apache.ambari.server.state.Cluster> clusterCapture = EasyMock.newCapture();
        EasyMock.expect(managementController1.addStages(((org.apache.ambari.server.controller.internal.RequestStageContainer) (EasyMock.isNull())), EasyMock.capture(clusterCapture), EasyMock.capture(requestPropertiesCapture), EasyMock.capture(requestParametersCapture), EasyMock.capture(changedServicesCapture), EasyMock.capture(changedCompsCapture), EasyMock.capture(changedScHostsCapture), EasyMock.capture(ignoredScHostsCapture), EasyMock.anyBoolean(), EasyMock.anyBoolean(), EasyMock.anyBoolean())).andReturn(requestStages1);
        EasyMock.expect(managementController2.addStages(((org.apache.ambari.server.controller.internal.RequestStageContainer) (EasyMock.isNull())), EasyMock.capture(clusterCapture), EasyMock.capture(requestPropertiesCapture), EasyMock.capture(requestParametersCapture), EasyMock.capture(changedServicesCapture), EasyMock.capture(changedCompsCapture), EasyMock.capture(changedScHostsCapture), EasyMock.capture(ignoredScHostsCapture), EasyMock.anyBoolean(), EasyMock.anyBoolean(), EasyMock.anyBoolean())).andReturn(requestStages2);
        requestStages1.persist();
        EasyMock.expect(requestStages1.getRequestStatusResponse()).andReturn(response1);
        requestStages2.persist();
        EasyMock.expect(requestStages2.getRequestStatusResponse()).andReturn(response2);
        EasyMock.expect(maintenanceStateHelper.isOperationAllowed(EasyMock.anyObject(org.apache.ambari.server.controller.spi.Resource.Type.class), EasyMock.anyObject(org.apache.ambari.server.state.Service.class))).andReturn(true).anyTimes();
        EasyMock.expect(service0.getCluster()).andReturn(cluster).anyTimes();
        EasyMock.expect(managementController1.getRoleCommandOrder(cluster)).andReturn(rco).anyTimes();
        EasyMock.expect(managementController2.getRoleCommandOrder(cluster)).andReturn(rco).anyTimes();
        EasyMock.expect(rco.getTransitiveServices(EasyMock.eq(service0), EasyMock.eq(org.apache.ambari.server.RoleCommand.START))).andReturn(java.util.Collections.emptySet()).anyTimes();
        EasyMock.replay(managementController1, response1, managementController2, requestStages1, requestStages2, response2, clusters, cluster, service0, serviceResponse0, ambariMetaInfo, rco, maintenanceStateHelper, repositoryVersionDAO, stackId, serviceInfo);
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        org.apache.ambari.server.controller.internal.ServiceResourceProvider provider1 = org.apache.ambari.server.controller.internal.ServiceResourceProviderTest.getServiceProvider(managementController1, maintenanceStateHelper, repositoryVersionDAO);
        org.apache.ambari.server.controller.internal.ServiceResourceProvider provider2 = org.apache.ambari.server.controller.internal.ServiceResourceProviderTest.getServiceProvider(managementController2, maintenanceStateHelper, repositoryVersionDAO);
        java.util.Map<java.lang.String, java.lang.Object> properties = new java.util.LinkedHashMap<>();
        properties.put(org.apache.ambari.server.controller.internal.ServiceResourceProvider.SERVICE_SERVICE_STATE_PROPERTY_ID, "STARTED");
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getUpdateRequest(properties, mapRequestProps);
        org.apache.ambari.server.controller.spi.Predicate predicate1 = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.ServiceResourceProvider.SERVICE_CLUSTER_NAME_PROPERTY_ID).equals("Cluster100").and().property(org.apache.ambari.server.controller.internal.ServiceResourceProvider.SERVICE_SERVICE_NAME_PROPERTY_ID).equals("Service102").and().property("params/reconfigure_client").equals("true").toPredicate();
        org.apache.ambari.server.controller.spi.Predicate predicate2 = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.ServiceResourceProvider.SERVICE_CLUSTER_NAME_PROPERTY_ID).equals("Cluster100").and().property(org.apache.ambari.server.controller.internal.ServiceResourceProvider.SERVICE_SERVICE_NAME_PROPERTY_ID).equals("Service102").and().property("params/reconfigure_client").equals("false").toPredicate();
        provider1.updateResources(request, predicate1);
        provider2.updateResources(request, predicate2);
        EasyMock.verify(managementController1, response1, managementController2, requestStages1, requestStages2, response2, clusters, cluster, service0, serviceResponse0, ambariMetaInfo, maintenanceStateHelper, stackId, serviceInfo);
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
        org.apache.ambari.server.controller.AmbariManagementController managementController = EasyMock.createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.state.Clusters clusters = EasyMock.createNiceMock(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.state.Cluster cluster = EasyMock.createNiceMock(org.apache.ambari.server.state.Cluster.class);
        org.apache.ambari.server.state.Service service = EasyMock.createNiceMock(org.apache.ambari.server.state.Service.class);
        java.lang.String serviceName = "Service100";
        EasyMock.expect(managementController.getClusters()).andReturn(clusters).anyTimes();
        EasyMock.expect(clusters.getCluster("Cluster100")).andReturn(cluster).anyTimes();
        EasyMock.expect(cluster.getClusterId()).andReturn(2L).anyTimes();
        EasyMock.expect(cluster.getService(serviceName)).andReturn(service).anyTimes();
        EasyMock.expect(service.getDesiredState()).andReturn(org.apache.ambari.server.state.State.INSTALLED).anyTimes();
        EasyMock.expect(service.getName()).andReturn(serviceName).anyTimes();
        EasyMock.expect(service.getServiceComponents()).andReturn(new java.util.HashMap<>());
        EasyMock.expect(service.getCluster()).andReturn(cluster);
        cluster.deleteService(EasyMock.eq(serviceName), EasyMock.anyObject(org.apache.ambari.server.controller.internal.DeleteHostComponentStatusMetaData.class));
        EasyMock.replay(managementController, clusters, cluster, service);
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        org.apache.ambari.server.controller.spi.ResourceProvider provider = org.apache.ambari.server.controller.internal.ServiceResourceProviderTest.getServiceProvider(managementController);
        org.apache.ambari.server.controller.internal.AbstractResourceProviderTest.TestObserver observer = new org.apache.ambari.server.controller.internal.AbstractResourceProviderTest.TestObserver();
        ((org.apache.ambari.server.controller.internal.ObservableResourceProvider) (provider)).addObserver(observer);
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.ServiceResourceProvider.SERVICE_CLUSTER_NAME_PROPERTY_ID).equals("Cluster100").and().property(org.apache.ambari.server.controller.internal.ServiceResourceProvider.SERVICE_SERVICE_NAME_PROPERTY_ID).equals(serviceName).toPredicate();
        provider.deleteResources(new org.apache.ambari.server.controller.internal.RequestImpl(null, null, null, null), predicate);
        org.apache.ambari.server.controller.internal.ResourceProviderEvent lastEvent = observer.getLastEvent();
        org.junit.Assert.assertNotNull(lastEvent);
        org.junit.Assert.assertEquals(org.apache.ambari.server.controller.spi.Resource.Type.Service, lastEvent.getResourceType());
        org.junit.Assert.assertEquals(org.apache.ambari.server.controller.internal.ResourceProviderEvent.Type.Delete, lastEvent.getType());
        org.junit.Assert.assertEquals(predicate, lastEvent.getPredicate());
        org.junit.Assert.assertNull(lastEvent.getRequest());
        EasyMock.verify(managementController, clusters, cluster, service);
    }

    @org.junit.Test
    public void testDeleteResourcesBadServiceState() throws java.lang.Exception {
        org.apache.ambari.server.controller.AmbariManagementController managementController = EasyMock.createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.state.Clusters clusters = EasyMock.createNiceMock(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.state.Cluster cluster = EasyMock.createNiceMock(org.apache.ambari.server.state.Cluster.class);
        org.apache.ambari.server.state.Service service = EasyMock.createNiceMock(org.apache.ambari.server.state.Service.class);
        java.lang.String serviceName = "Service100";
        EasyMock.expect(managementController.getClusters()).andReturn(clusters).anyTimes();
        EasyMock.expect(clusters.getCluster("Cluster100")).andReturn(cluster).anyTimes();
        EasyMock.expect(cluster.getClusterId()).andReturn(2L).anyTimes();
        EasyMock.expect(cluster.getService(serviceName)).andReturn(service).anyTimes();
        EasyMock.expect(service.getDesiredState()).andReturn(org.apache.ambari.server.state.State.STARTED).anyTimes();
        EasyMock.expect(service.getName()).andReturn(serviceName).anyTimes();
        EasyMock.expect(service.getServiceComponents()).andReturn(new java.util.HashMap<>());
        EasyMock.expect(service.getCluster()).andReturn(cluster);
        cluster.deleteService(EasyMock.eq(serviceName), EasyMock.anyObject(org.apache.ambari.server.controller.internal.DeleteHostComponentStatusMetaData.class));
        EasyMock.replay(managementController, clusters, cluster, service);
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator());
        org.apache.ambari.server.controller.spi.ResourceProvider provider = org.apache.ambari.server.controller.internal.ServiceResourceProviderTest.getServiceProvider(managementController);
        org.apache.ambari.server.controller.internal.AbstractResourceProviderTest.TestObserver observer = new org.apache.ambari.server.controller.internal.AbstractResourceProviderTest.TestObserver();
        ((org.apache.ambari.server.controller.internal.ObservableResourceProvider) (provider)).addObserver(observer);
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.ServiceResourceProvider.SERVICE_CLUSTER_NAME_PROPERTY_ID).equals("Cluster100").and().property(org.apache.ambari.server.controller.internal.ServiceResourceProvider.SERVICE_SERVICE_NAME_PROPERTY_ID).equals(serviceName).toPredicate();
        provider.deleteResources(new org.apache.ambari.server.controller.internal.RequestImpl(null, null, null, null), predicate);
        org.apache.ambari.server.controller.internal.ResourceProviderEvent lastEvent = observer.getLastEvent();
        org.junit.Assert.assertNotNull(lastEvent);
        org.junit.Assert.assertEquals(org.apache.ambari.server.controller.spi.Resource.Type.Service, lastEvent.getResourceType());
        org.junit.Assert.assertEquals(org.apache.ambari.server.controller.internal.ResourceProviderEvent.Type.Delete, lastEvent.getType());
        org.junit.Assert.assertEquals(predicate, lastEvent.getPredicate());
        org.junit.Assert.assertNull(lastEvent.getRequest());
        EasyMock.verify(managementController, clusters, cluster, service);
    }

    @org.junit.Test
    public void testDeleteResourcesBadComponentState() throws java.lang.Exception {
        org.apache.ambari.server.controller.AmbariManagementController managementController = EasyMock.createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.state.Clusters clusters = EasyMock.createNiceMock(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.state.Cluster cluster = EasyMock.createNiceMock(org.apache.ambari.server.state.Cluster.class);
        org.apache.ambari.server.state.Service service = EasyMock.createNiceMock(org.apache.ambari.server.state.Service.class);
        org.apache.ambari.server.state.ServiceComponent sc = EasyMock.createNiceMock(org.apache.ambari.server.state.ServiceComponent.class);
        java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceComponent> scMap = new java.util.HashMap<>();
        scMap.put("Component100", sc);
        org.apache.ambari.server.state.State componentState = org.apache.ambari.server.state.State.STARTED;
        org.apache.ambari.server.state.ServiceComponentHost sch = EasyMock.createNiceMock(org.apache.ambari.server.state.ServiceComponentHost.class);
        java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceComponentHost> schMap = new java.util.HashMap<>();
        schMap.put("Host1", sch);
        org.apache.ambari.server.state.State schState = org.apache.ambari.server.state.State.STARTED;
        java.lang.String serviceName = "Service100";
        EasyMock.expect(managementController.getClusters()).andReturn(clusters).anyTimes();
        EasyMock.expect(clusters.getCluster("Cluster100")).andReturn(cluster).anyTimes();
        EasyMock.expect(cluster.getService(serviceName)).andReturn(service).anyTimes();
        EasyMock.expect(service.getDesiredState()).andReturn(org.apache.ambari.server.state.State.INSTALLED).anyTimes();
        EasyMock.expect(service.getName()).andReturn(serviceName).anyTimes();
        EasyMock.expect(service.getServiceComponents()).andReturn(scMap);
        EasyMock.expect(sc.getDesiredState()).andReturn(componentState).anyTimes();
        EasyMock.expect(sc.getName()).andReturn("Component100").anyTimes();
        EasyMock.expect(sc.canBeRemoved()).andReturn(componentState.isRemovableState()).anyTimes();
        EasyMock.expect(sc.getServiceComponentHosts()).andReturn(schMap).anyTimes();
        EasyMock.expect(sch.getDesiredState()).andReturn(schState).anyTimes();
        EasyMock.expect(sch.canBeRemoved()).andReturn(schState.isRemovableState()).anyTimes();
        EasyMock.replay(managementController, clusters, cluster, service, sc, sch);
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator());
        org.apache.ambari.server.controller.spi.ResourceProvider provider = org.apache.ambari.server.controller.internal.ServiceResourceProviderTest.getServiceProvider(managementController);
        org.apache.ambari.server.controller.internal.AbstractResourceProviderTest.TestObserver observer = new org.apache.ambari.server.controller.internal.AbstractResourceProviderTest.TestObserver();
        ((org.apache.ambari.server.controller.internal.ObservableResourceProvider) (provider)).addObserver(observer);
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.ServiceResourceProvider.SERVICE_CLUSTER_NAME_PROPERTY_ID).equals("Cluster100").and().property(org.apache.ambari.server.controller.internal.ServiceResourceProvider.SERVICE_SERVICE_NAME_PROPERTY_ID).equals(serviceName).toPredicate();
        try {
            provider.deleteResources(new org.apache.ambari.server.controller.internal.RequestImpl(null, null, null, null), predicate);
            org.junit.Assert.fail("Expected exception deleting a service in a non-removable state.");
        } catch (org.apache.ambari.server.controller.spi.SystemException e) {
        }
    }

    @org.junit.Test
    public void testDeleteResourcesStoppedHostComponentState() throws java.lang.Exception {
        org.apache.ambari.server.controller.AmbariManagementController managementController = EasyMock.createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.state.Clusters clusters = EasyMock.createNiceMock(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.state.Cluster cluster = EasyMock.createNiceMock(org.apache.ambari.server.state.Cluster.class);
        org.apache.ambari.server.state.Service service = EasyMock.createNiceMock(org.apache.ambari.server.state.Service.class);
        class TestComponent {
            public java.lang.String Name;

            public org.apache.ambari.server.state.ServiceComponent Component;

            public org.apache.ambari.server.state.State DesiredState;

            public TestComponent(java.lang.String name, org.apache.ambari.server.state.ServiceComponent component, org.apache.ambari.server.state.State desiredState) {
                Name = name;
                Component = component;
                DesiredState = desiredState;
            }
        }
        TestComponent component1 = new TestComponent("Component100", EasyMock.createNiceMock(org.apache.ambari.server.state.ServiceComponent.class), org.apache.ambari.server.state.State.STARTED);
        TestComponent component2 = new TestComponent("Component101", EasyMock.createNiceMock(org.apache.ambari.server.state.ServiceComponent.class), org.apache.ambari.server.state.State.STARTED);
        TestComponent component3 = new TestComponent("Component102", EasyMock.createNiceMock(org.apache.ambari.server.state.ServiceComponent.class), org.apache.ambari.server.state.State.STARTED);
        java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceComponent> scMap = new java.util.HashMap<>();
        scMap.put(component1.Name, component1.Component);
        scMap.put(component2.Name, component2.Component);
        scMap.put(component3.Name, component3.Component);
        java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceComponentHost> schMap1 = new java.util.HashMap<>();
        org.apache.ambari.server.state.ServiceComponentHost sch1 = EasyMock.createNiceMock(org.apache.ambari.server.state.ServiceComponentHost.class);
        schMap1.put("Host1", sch1);
        java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceComponentHost> schMap2 = new java.util.HashMap<>();
        org.apache.ambari.server.state.ServiceComponentHost sch2 = EasyMock.createNiceMock(org.apache.ambari.server.state.ServiceComponentHost.class);
        schMap2.put("Host2", sch2);
        java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceComponentHost> schMap3 = new java.util.HashMap<>();
        org.apache.ambari.server.state.ServiceComponentHost sch3 = EasyMock.createNiceMock(org.apache.ambari.server.state.ServiceComponentHost.class);
        schMap3.put("Host3", sch3);
        java.lang.String clusterName = "Cluster100";
        java.lang.String serviceName = "Service100";
        EasyMock.expect(managementController.getClusters()).andReturn(clusters).anyTimes();
        EasyMock.expect(clusters.getCluster(clusterName)).andReturn(cluster).anyTimes();
        EasyMock.expect(cluster.getService(serviceName)).andReturn(service).anyTimes();
        EasyMock.expect(service.getDesiredState()).andReturn(org.apache.ambari.server.state.State.STARTED).anyTimes();
        EasyMock.expect(service.getName()).andReturn(serviceName).anyTimes();
        EasyMock.expect(service.getServiceComponents()).andReturn(scMap).anyTimes();
        EasyMock.expect(component1.Component.getDesiredState()).andReturn(component1.DesiredState).anyTimes();
        EasyMock.expect(component2.Component.getDesiredState()).andReturn(component2.DesiredState).anyTimes();
        EasyMock.expect(component3.Component.getDesiredState()).andReturn(component3.DesiredState).anyTimes();
        EasyMock.expect(component1.Component.canBeRemoved()).andReturn(component1.DesiredState.isRemovableState()).anyTimes();
        EasyMock.expect(component2.Component.canBeRemoved()).andReturn(component2.DesiredState.isRemovableState()).anyTimes();
        EasyMock.expect(component3.Component.canBeRemoved()).andReturn(component3.DesiredState.isRemovableState()).anyTimes();
        EasyMock.expect(component1.Component.getServiceComponentHosts()).andReturn(schMap1).anyTimes();
        EasyMock.expect(component2.Component.getServiceComponentHosts()).andReturn(schMap2).anyTimes();
        EasyMock.expect(component3.Component.getServiceComponentHosts()).andReturn(schMap3).anyTimes();
        org.apache.ambari.server.state.State sch1State = org.apache.ambari.server.state.State.INSTALLED;
        EasyMock.expect(sch1.getDesiredState()).andReturn(sch1State).anyTimes();
        EasyMock.expect(sch1.canBeRemoved()).andReturn(sch1State.isRemovableState()).anyTimes();
        org.apache.ambari.server.state.State sch2State = org.apache.ambari.server.state.State.INSTALLED;
        EasyMock.expect(sch2.getDesiredState()).andReturn(sch2State).anyTimes();
        EasyMock.expect(sch2.canBeRemoved()).andReturn(sch2State.isRemovableState()).anyTimes();
        org.apache.ambari.server.state.State sch3State = org.apache.ambari.server.state.State.INSTALLED;
        EasyMock.expect(sch3.getDesiredState()).andReturn(sch3State).anyTimes();
        EasyMock.expect(sch3.canBeRemoved()).andReturn(sch3State.isRemovableState()).anyTimes();
        EasyMock.expect(service.getCluster()).andReturn(cluster);
        cluster.deleteService(EasyMock.eq(serviceName), EasyMock.anyObject(org.apache.ambari.server.controller.internal.DeleteHostComponentStatusMetaData.class));
        EasyMock.replay(managementController, clusters, cluster, service, component1.Component, component2.Component, component3.Component, sch1, sch2, sch3);
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator());
        org.apache.ambari.server.controller.spi.ResourceProvider provider = org.apache.ambari.server.controller.internal.ServiceResourceProviderTest.getServiceProvider(managementController);
        org.apache.ambari.server.controller.internal.AbstractResourceProviderTest.TestObserver observer = new org.apache.ambari.server.controller.internal.AbstractResourceProviderTest.TestObserver();
        ((org.apache.ambari.server.controller.internal.ObservableResourceProvider) (provider)).addObserver(observer);
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.ServiceResourceProvider.SERVICE_CLUSTER_NAME_PROPERTY_ID).equals(clusterName).and().property(org.apache.ambari.server.controller.internal.ServiceResourceProvider.SERVICE_SERVICE_NAME_PROPERTY_ID).equals(serviceName).toPredicate();
        provider.deleteResources(new org.apache.ambari.server.controller.internal.RequestImpl(null, null, null, null), predicate);
        org.apache.ambari.server.controller.internal.ResourceProviderEvent lastEvent = observer.getLastEvent();
        org.junit.Assert.assertNotNull(lastEvent);
        org.junit.Assert.assertEquals(org.apache.ambari.server.controller.spi.Resource.Type.Service, lastEvent.getResourceType());
        org.junit.Assert.assertEquals(org.apache.ambari.server.controller.internal.ResourceProviderEvent.Type.Delete, lastEvent.getType());
        org.junit.Assert.assertEquals(predicate, lastEvent.getPredicate());
        org.junit.Assert.assertNull(lastEvent.getRequest());
        EasyMock.verify(managementController, clusters, cluster, service, component1.Component, component2.Component, component3.Component, sch1, sch2, sch3);
    }

    @org.junit.Test
    public void testCheckPropertyIds() throws java.lang.Exception {
        org.apache.ambari.server.controller.AmbariManagementController managementController = EasyMock.createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.controller.MaintenanceStateHelper maintenanceStateHelperMock = EasyMock.createNiceMock(org.apache.ambari.server.controller.MaintenanceStateHelper.class);
        org.apache.ambari.server.orm.dao.RepositoryVersionDAO repositoryVersionDAO = EasyMock.createNiceMock(org.apache.ambari.server.orm.dao.RepositoryVersionDAO.class);
        EasyMock.replay(maintenanceStateHelperMock, repositoryVersionDAO);
        org.apache.ambari.server.controller.internal.AbstractResourceProvider provider = new org.apache.ambari.server.controller.internal.ServiceResourceProvider(managementController, maintenanceStateHelperMock, repositoryVersionDAO);
        java.util.Set<java.lang.String> unsupported = provider.checkPropertyIds(java.util.Collections.singleton(org.apache.ambari.server.controller.internal.ServiceResourceProvider.SERVICE_CLUSTER_NAME_PROPERTY_ID));
        org.junit.Assert.assertTrue(unsupported.isEmpty());
        java.lang.String subKey = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId(org.apache.ambari.server.controller.internal.ServiceResourceProvider.SERVICE_CLUSTER_NAME_PROPERTY_ID, "key");
        unsupported = provider.checkPropertyIds(java.util.Collections.singleton(subKey));
        org.junit.Assert.assertTrue(unsupported.isEmpty());
        unsupported = provider.checkPropertyIds(java.util.Collections.singleton("bar"));
        org.junit.Assert.assertEquals(1, unsupported.size());
        org.junit.Assert.assertTrue(unsupported.contains("bar"));
        for (java.lang.String propertyId : provider.getPKPropertyIds()) {
            unsupported = provider.checkPropertyIds(java.util.Collections.singleton(propertyId));
            org.junit.Assert.assertTrue(unsupported.isEmpty());
        }
    }

    @org.junit.Test
    public void testCreateWithNoRepositoryId() throws java.lang.Exception {
        org.apache.ambari.server.controller.AmbariManagementController managementController = EasyMock.createNiceMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.state.Clusters clusters = EasyMock.createNiceMock(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.state.Cluster cluster = EasyMock.createNiceMock(org.apache.ambari.server.state.Cluster.class);
        org.apache.ambari.server.state.Service service1 = EasyMock.createNiceMock(org.apache.ambari.server.state.Service.class);
        org.apache.ambari.server.state.Service service2 = EasyMock.createNiceMock(org.apache.ambari.server.state.Service.class);
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repoVersion = EasyMock.createNiceMock(org.apache.ambari.server.orm.entities.RepositoryVersionEntity.class);
        EasyMock.expect(repoVersion.getId()).andReturn(500L).anyTimes();
        EasyMock.expect(service1.getDesiredRepositoryVersion()).andReturn(repoVersion).atLeastOnce();
        org.apache.ambari.server.state.StackId stackId = new org.apache.ambari.server.state.StackId("HDP-2.5");
        org.apache.ambari.server.state.ServiceFactory serviceFactory = EasyMock.createNiceMock(org.apache.ambari.server.state.ServiceFactory.class);
        org.apache.ambari.server.api.services.AmbariMetaInfo ambariMetaInfo = EasyMock.createNiceMock(org.apache.ambari.server.api.services.AmbariMetaInfo.class);
        org.apache.ambari.server.state.ServiceInfo serviceInfo = EasyMock.createNiceMock(org.apache.ambari.server.state.ServiceInfo.class);
        EasyMock.expect(managementController.getClusters()).andReturn(clusters).anyTimes();
        EasyMock.expect(managementController.getAmbariMetaInfo()).andReturn(ambariMetaInfo).anyTimes();
        EasyMock.expect(cluster.addService(EasyMock.eq("Service200"), org.easymock.EasyMock.anyObject(org.apache.ambari.server.orm.entities.RepositoryVersionEntity.class))).andReturn(service2);
        EasyMock.expect(clusters.getCluster("Cluster100")).andReturn(cluster).anyTimes();
        EasyMock.expect(cluster.getServices()).andReturn(com.google.common.collect.ImmutableMap.<java.lang.String, org.apache.ambari.server.state.Service>builder().put("Service100", service1).build()).atLeastOnce();
        EasyMock.expect(cluster.getDesiredStackVersion()).andReturn(stackId).anyTimes();
        EasyMock.expect(cluster.getClusterId()).andReturn(2L).anyTimes();
        EasyMock.expect(ambariMetaInfo.isValidService(((java.lang.String) (EasyMock.anyObject())), ((java.lang.String) (EasyMock.anyObject())), ((java.lang.String) (EasyMock.anyObject())))).andReturn(true);
        EasyMock.expect(ambariMetaInfo.getService(((java.lang.String) (EasyMock.anyObject())), ((java.lang.String) (EasyMock.anyObject())), ((java.lang.String) (EasyMock.anyObject())))).andReturn(serviceInfo).anyTimes();
        EasyMock.replay(managementController, clusters, cluster, service1, service2, ambariMetaInfo, serviceFactory, serviceInfo, repoVersion);
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator());
        org.easymock.Capture<java.lang.Long> pkCapture = org.easymock.Capture.newInstance();
        org.apache.ambari.server.controller.spi.ResourceProvider provider = org.apache.ambari.server.controller.internal.ServiceResourceProviderTest.getServiceProvider(managementController, true, pkCapture);
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> propertySet = new java.util.LinkedHashSet<>();
        java.util.Map<java.lang.String, java.lang.Object> properties = new java.util.LinkedHashMap<>();
        properties.put(org.apache.ambari.server.controller.internal.ServiceResourceProvider.SERVICE_CLUSTER_NAME_PROPERTY_ID, "Cluster100");
        properties.put(org.apache.ambari.server.controller.internal.ServiceResourceProvider.SERVICE_SERVICE_NAME_PROPERTY_ID, "Service200");
        properties.put(org.apache.ambari.server.controller.internal.ServiceResourceProvider.SERVICE_SERVICE_STATE_PROPERTY_ID, "INIT");
        properties.put(org.apache.ambari.server.controller.internal.ServiceResourceProvider.SERVICE_DESIRED_STACK_PROPERTY_ID, "HDP-1.1");
        propertySet.add(properties);
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getCreateRequest(propertySet, null);
        provider.createResources(request);
        EasyMock.verify(managementController, clusters, cluster, service1, service2, ambariMetaInfo, serviceFactory, serviceInfo);
        org.junit.Assert.assertTrue(pkCapture.hasCaptured());
        org.junit.Assert.assertEquals(java.lang.Long.valueOf(500L), pkCapture.getValue());
    }

    @org.junit.Test
    public void testCreateWithNoRepositoryIdAndPatch() throws java.lang.Exception {
        org.apache.ambari.server.controller.AmbariManagementController managementController = EasyMock.createNiceMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.state.Clusters clusters = EasyMock.createNiceMock(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.state.Cluster cluster = EasyMock.createNiceMock(org.apache.ambari.server.state.Cluster.class);
        org.apache.ambari.server.state.Service service1 = EasyMock.createNiceMock(org.apache.ambari.server.state.Service.class);
        org.apache.ambari.server.state.Service service2 = EasyMock.createNiceMock(org.apache.ambari.server.state.Service.class);
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repoVersion = EasyMock.createNiceMock(org.apache.ambari.server.orm.entities.RepositoryVersionEntity.class);
        EasyMock.expect(repoVersion.getId()).andReturn(500L).anyTimes();
        EasyMock.expect(repoVersion.getParentId()).andReturn(600L).anyTimes();
        EasyMock.expect(repoVersion.getType()).andReturn(org.apache.ambari.spi.RepositoryType.PATCH).anyTimes();
        EasyMock.expect(service1.getDesiredRepositoryVersion()).andReturn(repoVersion).atLeastOnce();
        org.apache.ambari.server.state.StackId stackId = new org.apache.ambari.server.state.StackId("HDP-2.5");
        org.apache.ambari.server.state.ServiceFactory serviceFactory = EasyMock.createNiceMock(org.apache.ambari.server.state.ServiceFactory.class);
        org.apache.ambari.server.api.services.AmbariMetaInfo ambariMetaInfo = EasyMock.createNiceMock(org.apache.ambari.server.api.services.AmbariMetaInfo.class);
        org.apache.ambari.server.state.ServiceInfo serviceInfo = EasyMock.createNiceMock(org.apache.ambari.server.state.ServiceInfo.class);
        EasyMock.expect(managementController.getClusters()).andReturn(clusters).anyTimes();
        EasyMock.expect(managementController.getAmbariMetaInfo()).andReturn(ambariMetaInfo).anyTimes();
        EasyMock.expect(cluster.addService(EasyMock.eq("Service200"), org.easymock.EasyMock.anyObject(org.apache.ambari.server.orm.entities.RepositoryVersionEntity.class))).andReturn(service2);
        EasyMock.expect(clusters.getCluster("Cluster100")).andReturn(cluster).anyTimes();
        EasyMock.expect(cluster.getServices()).andReturn(com.google.common.collect.ImmutableMap.<java.lang.String, org.apache.ambari.server.state.Service>builder().put("Service100", service1).build()).atLeastOnce();
        EasyMock.expect(cluster.getDesiredStackVersion()).andReturn(stackId).anyTimes();
        EasyMock.expect(cluster.getClusterId()).andReturn(2L).anyTimes();
        EasyMock.expect(ambariMetaInfo.isValidService(((java.lang.String) (EasyMock.anyObject())), ((java.lang.String) (EasyMock.anyObject())), ((java.lang.String) (EasyMock.anyObject())))).andReturn(true);
        EasyMock.expect(ambariMetaInfo.getService(((java.lang.String) (EasyMock.anyObject())), ((java.lang.String) (EasyMock.anyObject())), ((java.lang.String) (EasyMock.anyObject())))).andReturn(serviceInfo).anyTimes();
        EasyMock.replay(managementController, clusters, cluster, service1, service2, ambariMetaInfo, serviceFactory, serviceInfo, repoVersion);
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator());
        org.easymock.Capture<java.lang.Long> pkCapture = org.easymock.Capture.newInstance();
        org.apache.ambari.server.controller.spi.ResourceProvider provider = org.apache.ambari.server.controller.internal.ServiceResourceProviderTest.getServiceProvider(managementController, true, pkCapture);
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> propertySet = new java.util.LinkedHashSet<>();
        java.util.Map<java.lang.String, java.lang.Object> properties = new java.util.LinkedHashMap<>();
        properties.put(org.apache.ambari.server.controller.internal.ServiceResourceProvider.SERVICE_CLUSTER_NAME_PROPERTY_ID, "Cluster100");
        properties.put(org.apache.ambari.server.controller.internal.ServiceResourceProvider.SERVICE_SERVICE_NAME_PROPERTY_ID, "Service200");
        properties.put(org.apache.ambari.server.controller.internal.ServiceResourceProvider.SERVICE_SERVICE_STATE_PROPERTY_ID, "INIT");
        properties.put(org.apache.ambari.server.controller.internal.ServiceResourceProvider.SERVICE_DESIRED_STACK_PROPERTY_ID, "HDP-1.1");
        propertySet.add(properties);
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getCreateRequest(propertySet, null);
        provider.createResources(request);
        EasyMock.verify(managementController, clusters, cluster, service1, service2, ambariMetaInfo, serviceFactory, serviceInfo);
        org.junit.Assert.assertTrue(pkCapture.hasCaptured());
        org.junit.Assert.assertEquals(java.lang.Long.valueOf(600L), pkCapture.getValue());
    }

    @org.junit.Test
    public void testCreateWithNoRepositoryIdAndMultiBase() throws java.lang.Exception {
        org.apache.ambari.server.controller.AmbariManagementController managementController = EasyMock.createNiceMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.state.Clusters clusters = EasyMock.createNiceMock(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.state.Cluster cluster = EasyMock.createNiceMock(org.apache.ambari.server.state.Cluster.class);
        org.apache.ambari.server.state.Service service1 = EasyMock.createNiceMock(org.apache.ambari.server.state.Service.class);
        org.apache.ambari.server.state.Service service2 = EasyMock.createNiceMock(org.apache.ambari.server.state.Service.class);
        org.apache.ambari.server.state.Service service3 = EasyMock.createNiceMock(org.apache.ambari.server.state.Service.class);
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repoVersion1 = EasyMock.createNiceMock(org.apache.ambari.server.orm.entities.RepositoryVersionEntity.class);
        EasyMock.expect(repoVersion1.getId()).andReturn(500L).anyTimes();
        EasyMock.expect(service1.getDesiredRepositoryVersion()).andReturn(repoVersion1).atLeastOnce();
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repoVersion2 = EasyMock.createNiceMock(org.apache.ambari.server.orm.entities.RepositoryVersionEntity.class);
        EasyMock.expect(repoVersion2.getId()).andReturn(600L).anyTimes();
        EasyMock.expect(service2.getDesiredRepositoryVersion()).andReturn(repoVersion2).atLeastOnce();
        org.apache.ambari.server.state.StackId stackId = new org.apache.ambari.server.state.StackId("HDP-2.5");
        org.apache.ambari.server.state.ServiceFactory serviceFactory = EasyMock.createNiceMock(org.apache.ambari.server.state.ServiceFactory.class);
        org.apache.ambari.server.api.services.AmbariMetaInfo ambariMetaInfo = EasyMock.createNiceMock(org.apache.ambari.server.api.services.AmbariMetaInfo.class);
        org.apache.ambari.server.state.ServiceInfo serviceInfo = EasyMock.createNiceMock(org.apache.ambari.server.state.ServiceInfo.class);
        EasyMock.expect(managementController.getClusters()).andReturn(clusters).anyTimes();
        EasyMock.expect(managementController.getAmbariMetaInfo()).andReturn(ambariMetaInfo).anyTimes();
        EasyMock.expect(clusters.getCluster("Cluster100")).andReturn(cluster).anyTimes();
        EasyMock.expect(cluster.getServices()).andReturn(com.google.common.collect.ImmutableMap.<java.lang.String, org.apache.ambari.server.state.Service>builder().put("Service100", service1).put("Service200", service2).build()).atLeastOnce();
        EasyMock.expect(cluster.getDesiredStackVersion()).andReturn(stackId).anyTimes();
        EasyMock.expect(cluster.getClusterId()).andReturn(2L).anyTimes();
        EasyMock.replay(managementController, clusters, cluster, service1, service2, service3, ambariMetaInfo, serviceFactory, serviceInfo, repoVersion1, repoVersion2);
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator());
        org.easymock.Capture<java.lang.Long> pkCapture = org.easymock.Capture.newInstance();
        org.apache.ambari.server.controller.spi.ResourceProvider provider = org.apache.ambari.server.controller.internal.ServiceResourceProviderTest.getServiceProvider(managementController, true, pkCapture);
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> propertySet = new java.util.LinkedHashSet<>();
        java.util.Map<java.lang.String, java.lang.Object> properties = new java.util.LinkedHashMap<>();
        properties.put(org.apache.ambari.server.controller.internal.ServiceResourceProvider.SERVICE_CLUSTER_NAME_PROPERTY_ID, "Cluster100");
        properties.put(org.apache.ambari.server.controller.internal.ServiceResourceProvider.SERVICE_SERVICE_NAME_PROPERTY_ID, "Service200");
        properties.put(org.apache.ambari.server.controller.internal.ServiceResourceProvider.SERVICE_SERVICE_STATE_PROPERTY_ID, "INIT");
        properties.put(org.apache.ambari.server.controller.internal.ServiceResourceProvider.SERVICE_DESIRED_STACK_PROPERTY_ID, "HDP-1.1");
        propertySet.add(properties);
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getCreateRequest(propertySet, null);
        try {
            provider.createResources(request);
            org.junit.Assert.fail("Expected an exception when more than one base version was found");
        } catch (java.lang.IllegalArgumentException expected) {
        }
        EasyMock.verify(managementController, clusters, cluster, service1, service2, service3, ambariMetaInfo, serviceFactory, serviceInfo);
    }

    private static org.apache.ambari.server.controller.internal.ServiceResourceProvider getServiceProvider(org.apache.ambari.server.controller.AmbariManagementController managementController, boolean mockFindByStack, org.easymock.Capture<java.lang.Long> pkCapture) throws org.apache.ambari.server.AmbariException, java.lang.NoSuchFieldException, java.lang.IllegalAccessException {
        org.apache.ambari.server.controller.MaintenanceStateHelper maintenanceStateHelperMock = EasyMock.createNiceMock(org.apache.ambari.server.controller.MaintenanceStateHelper.class);
        org.apache.ambari.server.orm.dao.RepositoryVersionDAO repositoryVersionDAO = EasyMock.createNiceMock(org.apache.ambari.server.orm.dao.RepositoryVersionDAO.class);
        EasyMock.expect(maintenanceStateHelperMock.isOperationAllowed(EasyMock.anyObject(org.apache.ambari.server.controller.spi.Resource.Type.class), EasyMock.anyObject(org.apache.ambari.server.state.Service.class))).andReturn(true).anyTimes();
        EasyMock.expect(maintenanceStateHelperMock.isOperationAllowed(EasyMock.anyObject(org.apache.ambari.server.controller.spi.Resource.Type.class), EasyMock.anyObject(org.apache.ambari.server.state.ServiceComponentHost.class))).andReturn(true).anyTimes();
        if (mockFindByStack) {
            org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion = EasyMock.createNiceMock(org.apache.ambari.server.orm.entities.RepositoryVersionEntity.class);
            if (null != pkCapture) {
                EasyMock.expect(repositoryVersionDAO.findByPK(EasyMock.capture(pkCapture))).andReturn(repositoryVersion).atLeastOnce();
            } else {
                EasyMock.expect(repositoryVersionDAO.findByPK(org.easymock.EasyMock.anyLong())).andReturn(repositoryVersion).atLeastOnce();
            }
            EasyMock.expect(repositoryVersion.getStackId()).andReturn(new org.apache.ambari.server.state.StackId("HDP-2.2")).anyTimes();
            EasyMock.replay(repositoryVersion);
        }
        EasyMock.replay(maintenanceStateHelperMock, repositoryVersionDAO);
        return org.apache.ambari.server.controller.internal.ServiceResourceProviderTest.getServiceProvider(managementController, maintenanceStateHelperMock, repositoryVersionDAO);
    }

    public static org.apache.ambari.server.controller.internal.ServiceResourceProvider getServiceProvider(org.apache.ambari.server.controller.AmbariManagementController managementController) throws org.apache.ambari.server.AmbariException, java.lang.NoSuchFieldException, java.lang.IllegalAccessException {
        return org.apache.ambari.server.controller.internal.ServiceResourceProviderTest.getServiceProvider(managementController, false, null);
    }

    public static org.apache.ambari.server.controller.internal.ServiceResourceProvider getServiceProvider(org.apache.ambari.server.controller.AmbariManagementController managementController, org.apache.ambari.server.controller.MaintenanceStateHelper maintenanceStateHelper, org.apache.ambari.server.orm.dao.RepositoryVersionDAO repositoryVersionDAO) throws java.lang.NoSuchFieldException, java.lang.IllegalAccessException {
        org.apache.ambari.server.controller.spi.Resource.Type type = org.apache.ambari.server.controller.spi.Resource.Type.Service;
        org.apache.ambari.server.controller.internal.ServiceResourceProvider serviceResourceProvider = new org.apache.ambari.server.controller.internal.ServiceResourceProvider(managementController, maintenanceStateHelper, repositoryVersionDAO);
        java.lang.reflect.Field STOMPComponentsDeleteHandlerField = org.apache.ambari.server.controller.internal.ServiceResourceProvider.class.getDeclaredField("STOMPComponentsDeleteHandler");
        STOMPComponentsDeleteHandlerField.setAccessible(true);
        org.apache.ambari.server.topology.STOMPComponentsDeleteHandler STOMPComponentsDeleteHandler = EasyMock.createNiceMock(org.apache.ambari.server.topology.STOMPComponentsDeleteHandler.class);
        STOMPComponentsDeleteHandlerField.set(serviceResourceProvider, STOMPComponentsDeleteHandler);
        EasyMock.replay(STOMPComponentsDeleteHandler);
        return serviceResourceProvider;
    }

    public static void createServices(org.apache.ambari.server.controller.AmbariManagementController controller, org.apache.ambari.server.orm.dao.RepositoryVersionDAO repositoryVersionDAO, java.util.Set<org.apache.ambari.server.controller.ServiceRequest> requests) throws org.apache.ambari.server.AmbariException, org.apache.ambari.server.security.authorization.AuthorizationException, java.lang.NoSuchFieldException, java.lang.IllegalAccessException {
        org.apache.ambari.server.controller.MaintenanceStateHelper maintenanceStateHelperMock = EasyMock.createNiceMock(org.apache.ambari.server.controller.MaintenanceStateHelper.class);
        org.apache.ambari.server.controller.internal.ServiceResourceProvider provider = org.apache.ambari.server.controller.internal.ServiceResourceProviderTest.getServiceProvider(controller, maintenanceStateHelperMock, repositoryVersionDAO);
        provider.createServices(requests);
    }

    public static java.util.Set<org.apache.ambari.server.controller.ServiceResponse> getServices(org.apache.ambari.server.controller.AmbariManagementController controller, java.util.Set<org.apache.ambari.server.controller.ServiceRequest> requests) throws org.apache.ambari.server.AmbariException, java.lang.NoSuchFieldException, java.lang.IllegalAccessException {
        org.apache.ambari.server.controller.internal.ServiceResourceProvider provider = org.apache.ambari.server.controller.internal.ServiceResourceProviderTest.getServiceProvider(controller);
        return provider.getServices(requests);
    }

    public static org.apache.ambari.server.controller.RequestStatusResponse updateServices(org.apache.ambari.server.controller.AmbariManagementController controller, java.util.Set<org.apache.ambari.server.controller.ServiceRequest> requests, java.util.Map<java.lang.String, java.lang.String> requestProperties, boolean runSmokeTest, boolean reconfigureClients) throws org.apache.ambari.server.AmbariException, org.apache.ambari.server.security.authorization.AuthorizationException, java.lang.NoSuchFieldException, java.lang.IllegalAccessException {
        return org.apache.ambari.server.controller.internal.ServiceResourceProviderTest.updateServices(controller, requests, requestProperties, runSmokeTest, reconfigureClients, null);
    }

    public static org.apache.ambari.server.controller.RequestStatusResponse updateServices(org.apache.ambari.server.controller.AmbariManagementController controller, java.util.Set<org.apache.ambari.server.controller.ServiceRequest> requests, java.util.Map<java.lang.String, java.lang.String> requestProperties, boolean runSmokeTest, boolean reconfigureClients, org.apache.ambari.server.controller.MaintenanceStateHelper maintenanceStateHelper) throws org.apache.ambari.server.AmbariException, org.apache.ambari.server.security.authorization.AuthorizationException, java.lang.NoSuchFieldException, java.lang.IllegalAccessException {
        org.apache.ambari.server.controller.internal.ServiceResourceProvider provider;
        if (maintenanceStateHelper != null) {
            provider = org.apache.ambari.server.controller.internal.ServiceResourceProviderTest.getServiceProvider(controller, maintenanceStateHelper, null);
        } else {
            provider = org.apache.ambari.server.controller.internal.ServiceResourceProviderTest.getServiceProvider(controller);
        }
        org.apache.ambari.server.controller.internal.RequestStageContainer request = provider.updateServices(null, requests, requestProperties, runSmokeTest, reconfigureClients, true);
        request.persist();
        return request.getRequestStatusResponse();
    }

    public static org.apache.ambari.server.controller.RequestStatusResponse deleteServices(org.apache.ambari.server.controller.AmbariManagementController controller, java.util.Set<org.apache.ambari.server.controller.ServiceRequest> requests) throws org.apache.ambari.server.AmbariException, org.apache.ambari.server.security.authorization.AuthorizationException, java.lang.NoSuchFieldException, java.lang.IllegalAccessException {
        org.apache.ambari.server.controller.internal.ServiceResourceProvider provider = org.apache.ambari.server.controller.internal.ServiceResourceProviderTest.getServiceProvider(controller);
        return provider.deleteServices(requests);
    }
}