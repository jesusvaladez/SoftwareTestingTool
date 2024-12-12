package org.apache.ambari.server.controller.internal;
import org.easymock.Capture;
import org.easymock.EasyMock;
import org.easymock.IAnswer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.anyString;
import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.createStrictMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.newCapture;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
public class ConfigGroupResourceProviderTest {
    private org.apache.ambari.server.orm.dao.HostDAO hostDAO = null;

    @org.junit.Before
    public void setup() throws java.lang.Exception {
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(null);
        hostDAO = EasyMock.createStrictMock(org.apache.ambari.server.orm.dao.HostDAO.class);
        com.google.inject.Guice.createInjector(com.google.inject.util.Modules.override(new org.apache.ambari.server.orm.InMemoryDefaultTestModule()).with(new org.apache.ambari.server.controller.internal.ConfigGroupResourceProviderTest.MockModule()));
    }

    private org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider getConfigGroupResourceProvider(org.apache.ambari.server.controller.AmbariManagementController managementController) throws java.lang.NoSuchFieldException, java.lang.IllegalAccessException {
        org.apache.ambari.server.controller.spi.Resource.Type type = org.apache.ambari.server.controller.spi.Resource.Type.ConfigGroup;
        org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider configGroupResourceProvider = ((org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider) (org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.getResourceProvider(type, managementController)));
        com.google.inject.Provider<org.apache.ambari.server.state.ConfigHelper> configHelperProvider = EasyMock.createNiceMock(com.google.inject.Provider.class);
        EasyMock.expect(configHelperProvider.get()).andReturn(EasyMock.createNiceMock(org.apache.ambari.server.state.ConfigHelper.class));
        EasyMock.replay(configHelperProvider);
        java.lang.reflect.Field m_configHelper = org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.class.getDeclaredField("m_configHelper");
        m_configHelper.setAccessible(true);
        m_configHelper.set(configGroupResourceProvider, configHelperProvider);
        return configGroupResourceProvider;
    }

    private class MockModule implements com.google.inject.Module {
        @java.lang.Override
        public void configure(com.google.inject.Binder binder) {
            binder.bind(org.apache.ambari.server.orm.dao.HostDAO.class).toInstance(hostDAO);
        }
    }

    @org.junit.Test
    public void testCreateConfigGroupAsAmbariAdministrator() throws java.lang.Exception {
        testCreateConfigGroup(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator());
    }

    @org.junit.Test
    public void testCreateConfigGroupAsClusterAdministrator() throws java.lang.Exception {
        testCreateConfigGroup(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator());
    }

    @org.junit.Test
    public void testCreateConfigGroupAsClusterOperator() throws java.lang.Exception {
        testCreateConfigGroup(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterOperator());
    }

    @org.junit.Test
    public void testCreateConfigGroupAsServiceAdministrator() throws java.lang.Exception {
        testCreateConfigGroup(org.apache.ambari.server.security.TestAuthenticationFactory.createServiceAdministrator());
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testCreateConfigGroupAsServiceOperator() throws java.lang.Exception {
        testCreateConfigGroup(org.apache.ambari.server.security.TestAuthenticationFactory.createServiceOperator());
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testCreateConfigGroupAsClusterUser() throws java.lang.Exception {
        testCreateConfigGroup(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterUser());
    }

    private void testCreateConfigGroup(org.springframework.security.core.Authentication authentication) throws java.lang.Exception {
        org.apache.ambari.server.controller.AmbariManagementController managementController = EasyMock.createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.controller.RequestStatusResponse response = EasyMock.createNiceMock(org.apache.ambari.server.controller.RequestStatusResponse.class);
        org.apache.ambari.server.state.Clusters clusters = EasyMock.createNiceMock(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.state.Cluster cluster = EasyMock.createNiceMock(org.apache.ambari.server.state.Cluster.class);
        org.apache.ambari.server.state.Host h1 = EasyMock.createNiceMock(org.apache.ambari.server.state.Host.class);
        org.apache.ambari.server.state.Host h2 = EasyMock.createNiceMock(org.apache.ambari.server.state.Host.class);
        org.apache.ambari.server.orm.entities.HostEntity hostEntity1 = EasyMock.createMock(org.apache.ambari.server.orm.entities.HostEntity.class);
        org.apache.ambari.server.orm.entities.HostEntity hostEntity2 = EasyMock.createMock(org.apache.ambari.server.orm.entities.HostEntity.class);
        org.apache.ambari.server.state.configgroup.ConfigGroupFactory configGroupFactory = EasyMock.createNiceMock(org.apache.ambari.server.state.configgroup.ConfigGroupFactory.class);
        org.apache.ambari.server.state.configgroup.ConfigGroup configGroup = EasyMock.createNiceMock(org.apache.ambari.server.state.configgroup.ConfigGroup.class);
        EasyMock.expect(managementController.getClusters()).andReturn(clusters).anyTimes();
        EasyMock.expect(clusters.getCluster("Cluster100")).andReturn(cluster).anyTimes();
        EasyMock.expect(clusters.getHost("h1")).andReturn(h1);
        EasyMock.expect(clusters.getHost("h2")).andReturn(h2);
        EasyMock.expect(cluster.getClusterName()).andReturn("Cluster100").anyTimes();
        EasyMock.expect(cluster.isConfigTypeExists(EasyMock.anyString())).andReturn(true).anyTimes();
        EasyMock.expect(managementController.getConfigGroupFactory()).andReturn(configGroupFactory);
        EasyMock.expect(managementController.getAuthName()).andReturn("admin").anyTimes();
        EasyMock.expect(hostDAO.findByName("h1")).andReturn(hostEntity1).atLeastOnce();
        EasyMock.expect(hostDAO.findByName("h2")).andReturn(hostEntity2).atLeastOnce();
        EasyMock.expect(hostEntity1.getHostId()).andReturn(1L).atLeastOnce();
        EasyMock.expect(hostEntity2.getHostId()).andReturn(2L).atLeastOnce();
        org.easymock.Capture<org.apache.ambari.server.state.Cluster> clusterCapture = EasyMock.newCapture();
        org.easymock.Capture<java.lang.String> serviceName = EasyMock.newCapture();
        org.easymock.Capture<java.lang.String> captureName = EasyMock.newCapture();
        org.easymock.Capture<java.lang.String> captureDesc = EasyMock.newCapture();
        org.easymock.Capture<java.lang.String> captureTag = EasyMock.newCapture();
        org.easymock.Capture<java.util.Map<java.lang.String, org.apache.ambari.server.state.Config>> captureConfigs = EasyMock.newCapture();
        org.easymock.Capture<java.util.Map<java.lang.Long, org.apache.ambari.server.state.Host>> captureHosts = EasyMock.newCapture();
        EasyMock.expect(configGroupFactory.createNew(EasyMock.capture(clusterCapture), EasyMock.capture(serviceName), EasyMock.capture(captureName), EasyMock.capture(captureTag), EasyMock.capture(captureDesc), EasyMock.capture(captureConfigs), EasyMock.capture(captureHosts))).andReturn(configGroup);
        EasyMock.replay(managementController, clusters, cluster, configGroupFactory, configGroup, response, hostDAO, hostEntity1, hostEntity2);
        org.apache.ambari.server.controller.spi.ResourceProvider provider = getConfigGroupResourceProvider(managementController);
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> propertySet = new java.util.LinkedHashSet<>();
        java.util.Map<java.lang.String, java.lang.Object> properties = new java.util.LinkedHashMap<>();
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> hostSet = new java.util.HashSet<>();
        java.util.Map<java.lang.String, java.lang.Object> host1 = new java.util.HashMap<>();
        host1.put(org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.HOST_NAME, "h1");
        hostSet.add(host1);
        java.util.Map<java.lang.String, java.lang.Object> host2 = new java.util.HashMap<>();
        host2.put(org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.HOST_NAME, "h2");
        hostSet.add(host2);
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> configSet = new java.util.HashSet<>();
        java.util.Map<java.lang.String, java.lang.String> configMap = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.Object> configs = new java.util.HashMap<>();
        configs.put("type", "core-site");
        configs.put("tag", "version100");
        configMap.put("key1", "value1");
        configs.put("properties", configMap);
        configSet.add(configs);
        properties.put(org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.CLUSTER_NAME, "Cluster100");
        properties.put(org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.GROUP_NAME, "test-1");
        properties.put(org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.TAG, "tag-1");
        properties.put(org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.HOSTS, hostSet);
        properties.put(org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.DESIRED_CONFIGS, configSet);
        propertySet.add(properties);
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getCreateRequest(propertySet, null);
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        provider.createResources(request);
        EasyMock.verify(managementController, clusters, cluster, configGroupFactory, configGroup, response, hostDAO, hostEntity1, hostEntity2);
        junit.framework.Assert.assertEquals("version100", captureConfigs.getValue().get("core-site").getTag());
        junit.framework.Assert.assertTrue(captureHosts.getValue().containsKey(1L));
        junit.framework.Assert.assertTrue(captureHosts.getValue().containsKey(2L));
    }

    @org.junit.Test
    public void testDuplicateNameConfigGroupAsAmbariAdministrator() throws java.lang.Exception {
        testDuplicateNameConfigGroup(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator());
    }

    @org.junit.Test
    public void testDuplicateNameConfigGroupAsClusterAdministrator() throws java.lang.Exception {
        testDuplicateNameConfigGroup(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator());
    }

    @org.junit.Test
    public void testDuplicateNameConfigGroupAsClusterOperator() throws java.lang.Exception {
        testDuplicateNameConfigGroup(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterOperator());
    }

    @org.junit.Test
    public void testDuplicateNameConfigGroupAsServiceAdministrator() throws java.lang.Exception {
        testDuplicateNameConfigGroup(org.apache.ambari.server.security.TestAuthenticationFactory.createServiceAdministrator());
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testDuplicateNameConfigGroupAsServiceOperator() throws java.lang.Exception {
        testDuplicateNameConfigGroup(org.apache.ambari.server.security.TestAuthenticationFactory.createServiceOperator());
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testDuplicateNameConfigGroupAsClusterUser() throws java.lang.Exception {
        testDuplicateNameConfigGroup(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterUser());
    }

    private void testDuplicateNameConfigGroup(org.springframework.security.core.Authentication authentication) throws java.lang.Exception {
        org.apache.ambari.server.controller.AmbariManagementController managementController = EasyMock.createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.controller.RequestStatusResponse response = EasyMock.createNiceMock(org.apache.ambari.server.controller.RequestStatusResponse.class);
        org.apache.ambari.server.state.Clusters clusters = EasyMock.createNiceMock(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.state.Cluster cluster = EasyMock.createNiceMock(org.apache.ambari.server.state.Cluster.class);
        org.apache.ambari.server.state.configgroup.ConfigGroupFactory configGroupFactory = EasyMock.createNiceMock(org.apache.ambari.server.state.configgroup.ConfigGroupFactory.class);
        org.apache.ambari.server.state.configgroup.ConfigGroup configGroup = EasyMock.createNiceMock(org.apache.ambari.server.state.configgroup.ConfigGroup.class);
        java.util.Map<java.lang.Long, org.apache.ambari.server.state.configgroup.ConfigGroup> configGroupMap = new java.util.HashMap<>();
        configGroupMap.put(1L, configGroup);
        EasyMock.expect(managementController.getClusters()).andReturn(clusters).anyTimes();
        EasyMock.expect(clusters.getCluster("Cluster100")).andReturn(cluster).anyTimes();
        EasyMock.expect(managementController.getConfigGroupFactory()).andReturn(configGroupFactory).anyTimes();
        EasyMock.expect(managementController.getAuthName()).andReturn("admin").anyTimes();
        EasyMock.expect(cluster.getConfigGroups()).andReturn(configGroupMap);
        EasyMock.expect(configGroupFactory.createNew(((org.apache.ambari.server.state.Cluster) (EasyMock.anyObject())), ((java.lang.String) (EasyMock.anyObject())), ((java.lang.String) (EasyMock.anyObject())), ((java.lang.String) (EasyMock.anyObject())), ((java.lang.String) (EasyMock.anyObject())), org.easymock.EasyMock.anyObject(), org.easymock.EasyMock.anyObject())).andReturn(configGroup).anyTimes();
        EasyMock.expect(configGroup.getClusterName()).andReturn("Cluster100").anyTimes();
        EasyMock.expect(configGroup.getName()).andReturn("test-1").anyTimes();
        EasyMock.expect(configGroup.getTag()).andReturn("tag-1").anyTimes();
        EasyMock.replay(managementController, clusters, cluster, configGroupFactory, configGroup, response);
        org.apache.ambari.server.controller.spi.ResourceProvider provider = getConfigGroupResourceProvider(managementController);
        java.util.Map<java.lang.String, java.lang.Object> properties = new java.util.LinkedHashMap<>();
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> propertySet = new java.util.LinkedHashSet<>();
        properties.put(org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.CLUSTER_NAME, "Cluster100");
        properties.put(org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.GROUP_NAME, "test-1");
        properties.put(org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.TAG, "tag-1");
        propertySet.add(properties);
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getCreateRequest(propertySet, null);
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        java.lang.Exception exception = null;
        try {
            provider.createResources(request);
        } catch (org.apache.ambari.server.security.authorization.AuthorizationException e) {
            throw e;
        } catch (java.lang.Exception e) {
            exception = e;
        }
        EasyMock.verify(managementController, clusters, cluster, configGroupFactory, configGroup, response);
        junit.framework.Assert.assertNotNull(exception);
        junit.framework.Assert.assertTrue(exception instanceof org.apache.ambari.server.controller.spi.ResourceAlreadyExistsException);
    }

    @org.junit.Test
    public void testUpdateConfigGroupWithWrongConfigType() throws java.lang.Exception {
        org.apache.ambari.server.controller.AmbariManagementController managementController = EasyMock.createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.controller.RequestStatusResponse response = EasyMock.createNiceMock(org.apache.ambari.server.controller.RequestStatusResponse.class);
        org.apache.ambari.server.state.ConfigHelper configHelper = EasyMock.createNiceMock(org.apache.ambari.server.state.ConfigHelper.class);
        org.apache.ambari.server.state.Clusters clusters = EasyMock.createNiceMock(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.state.Cluster cluster = EasyMock.createNiceMock(org.apache.ambari.server.state.Cluster.class);
        org.apache.ambari.server.state.Host h1 = EasyMock.createNiceMock(org.apache.ambari.server.state.Host.class);
        org.apache.ambari.server.state.Host h2 = EasyMock.createNiceMock(org.apache.ambari.server.state.Host.class);
        org.apache.ambari.server.orm.entities.HostEntity hostEntity1 = EasyMock.createMock(org.apache.ambari.server.orm.entities.HostEntity.class);
        org.apache.ambari.server.orm.entities.HostEntity hostEntity2 = EasyMock.createMock(org.apache.ambari.server.orm.entities.HostEntity.class);
        final org.apache.ambari.server.state.configgroup.ConfigGroup configGroup = EasyMock.createNiceMock(org.apache.ambari.server.state.configgroup.ConfigGroup.class);
        org.apache.ambari.server.controller.ConfigGroupResponse configGroupResponse = EasyMock.createNiceMock(org.apache.ambari.server.controller.ConfigGroupResponse.class);
        EasyMock.expect(cluster.isConfigTypeExists("core-site")).andReturn(false).anyTimes();
        EasyMock.expect(managementController.getClusters()).andReturn(clusters).anyTimes();
        EasyMock.expect(managementController.getAuthName()).andReturn("admin").anyTimes();
        EasyMock.expect(clusters.getCluster("Cluster100")).andReturn(cluster).anyTimes();
        EasyMock.expect(clusters.getHost("h1")).andReturn(h1);
        EasyMock.expect(clusters.getHost("h2")).andReturn(h2);
        EasyMock.expect(hostDAO.findByName("h1")).andReturn(hostEntity1).anyTimes();
        EasyMock.expect(hostDAO.findById(1L)).andReturn(hostEntity1).anyTimes();
        EasyMock.expect(hostDAO.findByName("h2")).andReturn(hostEntity2).anyTimes();
        EasyMock.expect(hostDAO.findById(2L)).andReturn(hostEntity2).anyTimes();
        EasyMock.expect(hostEntity1.getHostId()).andReturn(1L).atLeastOnce();
        EasyMock.expect(hostEntity2.getHostId()).andReturn(2L).atLeastOnce();
        EasyMock.expect(h1.getHostId()).andReturn(1L).anyTimes();
        EasyMock.expect(h2.getHostId()).andReturn(2L).anyTimes();
        EasyMock.expect(configGroup.getName()).andReturn("test-1").anyTimes();
        EasyMock.expect(configGroup.getId()).andReturn(25L).anyTimes();
        EasyMock.expect(configGroup.getTag()).andReturn("tag-1").anyTimes();
        EasyMock.expect(configGroup.convertToResponse()).andReturn(configGroupResponse).anyTimes();
        EasyMock.expect(configGroupResponse.getClusterName()).andReturn("Cluster100").anyTimes();
        EasyMock.expect(configGroupResponse.getId()).andReturn(25L).anyTimes();
        EasyMock.expect(cluster.getConfigGroups()).andStubAnswer(new org.easymock.IAnswer<java.util.Map<java.lang.Long, org.apache.ambari.server.state.configgroup.ConfigGroup>>() {
            @java.lang.Override
            public java.util.Map<java.lang.Long, org.apache.ambari.server.state.configgroup.ConfigGroup> answer() throws java.lang.Throwable {
                java.util.Map<java.lang.Long, org.apache.ambari.server.state.configgroup.ConfigGroup> configGroupMap = new java.util.HashMap<>();
                configGroupMap.put(configGroup.getId(), configGroup);
                return configGroupMap;
            }
        });
        EasyMock.replay(managementController, clusters, cluster, configGroup, response, configGroupResponse, configHelper, hostDAO, hostEntity1, hostEntity2, h1, h2);
        org.apache.ambari.server.controller.spi.ResourceProvider provider = getConfigGroupResourceProvider(managementController);
        java.util.Map<java.lang.String, java.lang.Object> properties = new java.util.LinkedHashMap<>();
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> hostSet = new java.util.HashSet<>();
        java.util.Map<java.lang.String, java.lang.Object> host1 = new java.util.HashMap<>();
        host1.put(org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.HOST_NAME, "h1");
        hostSet.add(host1);
        java.util.Map<java.lang.String, java.lang.Object> host2 = new java.util.HashMap<>();
        host2.put(org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.HOST_NAME, "h2");
        hostSet.add(host2);
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> configSet = new java.util.HashSet<>();
        java.util.Map<java.lang.String, java.lang.String> configMap = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.Object> configs = new java.util.HashMap<>();
        configs.put("type", "core-site");
        configs.put("tag", "version100");
        configMap.put("key1", "value1");
        configs.put("properties", configMap);
        configSet.add(configs);
        properties.put(org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.CLUSTER_NAME, "Cluster100");
        properties.put(org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.GROUP_NAME, "test-1");
        properties.put(org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.TAG, "tag-1");
        properties.put(org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.HOSTS, hostSet);
        properties.put(org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.DESIRED_CONFIGS, configSet);
        java.util.Map<java.lang.String, java.lang.String> mapRequestProps = new java.util.HashMap<>();
        mapRequestProps.put("context", "Called from a test");
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getUpdateRequest(properties, mapRequestProps);
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.CLUSTER_NAME).equals("Cluster100").and().property(org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.ID).equals(25L).toPredicate();
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator());
        org.apache.ambari.server.controller.spi.SystemException systemException = null;
        try {
            provider.updateResources(request, predicate);
        } catch (org.apache.ambari.server.controller.spi.SystemException e) {
            systemException = e;
        }
        junit.framework.Assert.assertNotNull(systemException);
        EasyMock.verify(managementController, clusters, cluster, configGroup, response, configGroupResponse, configHelper, hostDAO, hostEntity1, hostEntity2, h1, h2);
    }

    @org.junit.Test
    public void testUpdateConfigGroupAsAmbariAdministrator() throws java.lang.Exception {
        testUpdateConfigGroup(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator());
    }

    @org.junit.Test
    public void testUpdateConfigGroupAsClusterAdministrator() throws java.lang.Exception {
        testUpdateConfigGroup(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator());
    }

    @org.junit.Test
    public void testUpdateConfigGroupAsClusterOperator() throws java.lang.Exception {
        testUpdateConfigGroup(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterOperator());
    }

    @org.junit.Test
    public void testUpdateConfigGroupAsServiceAdministrator() throws java.lang.Exception {
        testUpdateConfigGroup(org.apache.ambari.server.security.TestAuthenticationFactory.createServiceAdministrator());
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testUpdateConfigGroupAsServiceOperator() throws java.lang.Exception {
        testUpdateConfigGroup(org.apache.ambari.server.security.TestAuthenticationFactory.createServiceOperator());
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testUpdateConfigGroupAsClusterUser() throws java.lang.Exception {
        testUpdateConfigGroup(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterUser());
    }

    private void testUpdateConfigGroup(org.springframework.security.core.Authentication authentication) throws java.lang.Exception {
        org.apache.ambari.server.controller.AmbariManagementController managementController = EasyMock.createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.controller.RequestStatusResponse response = EasyMock.createNiceMock(org.apache.ambari.server.controller.RequestStatusResponse.class);
        org.apache.ambari.server.state.ConfigHelper configHelper = EasyMock.createNiceMock(org.apache.ambari.server.state.ConfigHelper.class);
        org.apache.ambari.server.state.Clusters clusters = EasyMock.createNiceMock(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.state.Cluster cluster = EasyMock.createNiceMock(org.apache.ambari.server.state.Cluster.class);
        org.apache.ambari.server.state.Host h1 = EasyMock.createNiceMock(org.apache.ambari.server.state.Host.class);
        org.apache.ambari.server.state.Host h2 = EasyMock.createNiceMock(org.apache.ambari.server.state.Host.class);
        org.apache.ambari.server.orm.entities.HostEntity hostEntity1 = EasyMock.createMock(org.apache.ambari.server.orm.entities.HostEntity.class);
        org.apache.ambari.server.orm.entities.HostEntity hostEntity2 = EasyMock.createMock(org.apache.ambari.server.orm.entities.HostEntity.class);
        final org.apache.ambari.server.state.configgroup.ConfigGroup configGroup = EasyMock.createNiceMock(org.apache.ambari.server.state.configgroup.ConfigGroup.class);
        org.apache.ambari.server.controller.ConfigGroupResponse configGroupResponse = EasyMock.createNiceMock(org.apache.ambari.server.controller.ConfigGroupResponse.class);
        EasyMock.expect(cluster.isConfigTypeExists("core-site")).andReturn(true).anyTimes();
        EasyMock.expect(managementController.getClusters()).andReturn(clusters).anyTimes();
        EasyMock.expect(managementController.getAuthName()).andReturn("admin").anyTimes();
        EasyMock.expect(clusters.getCluster("Cluster100")).andReturn(cluster).anyTimes();
        EasyMock.expect(clusters.getHost("h1")).andReturn(h1);
        EasyMock.expect(clusters.getHost("h2")).andReturn(h2);
        EasyMock.expect(hostDAO.findByName("h1")).andReturn(hostEntity1).anyTimes();
        EasyMock.expect(hostDAO.findById(1L)).andReturn(hostEntity1).anyTimes();
        EasyMock.expect(hostDAO.findByName("h2")).andReturn(hostEntity2).anyTimes();
        EasyMock.expect(hostDAO.findById(2L)).andReturn(hostEntity2).anyTimes();
        EasyMock.expect(hostEntity1.getHostId()).andReturn(1L).atLeastOnce();
        EasyMock.expect(hostEntity2.getHostId()).andReturn(2L).atLeastOnce();
        EasyMock.expect(h1.getHostId()).andReturn(1L).anyTimes();
        EasyMock.expect(h2.getHostId()).andReturn(2L).anyTimes();
        EasyMock.expect(managementController.getConfigGroupUpdateResults(((org.apache.ambari.server.controller.ConfigGroupRequest) (EasyMock.anyObject())))).andReturn(new org.apache.ambari.server.controller.ConfigGroupResponse(1L, "", "", "", "", new java.util.HashSet<>(), new java.util.HashSet<>())).atLeastOnce();
        EasyMock.expect(configGroup.getName()).andReturn("test-1").anyTimes();
        EasyMock.expect(configGroup.getId()).andReturn(25L).anyTimes();
        EasyMock.expect(configGroup.getTag()).andReturn("tag-1").anyTimes();
        EasyMock.expect(configGroup.convertToResponse()).andReturn(configGroupResponse).anyTimes();
        EasyMock.expect(configGroupResponse.getClusterName()).andReturn("Cluster100").anyTimes();
        EasyMock.expect(configGroupResponse.getId()).andReturn(25L).anyTimes();
        EasyMock.expect(cluster.getConfigGroups()).andStubAnswer(new org.easymock.IAnswer<java.util.Map<java.lang.Long, org.apache.ambari.server.state.configgroup.ConfigGroup>>() {
            @java.lang.Override
            public java.util.Map<java.lang.Long, org.apache.ambari.server.state.configgroup.ConfigGroup> answer() throws java.lang.Throwable {
                java.util.Map<java.lang.Long, org.apache.ambari.server.state.configgroup.ConfigGroup> configGroupMap = new java.util.HashMap<>();
                configGroupMap.put(configGroup.getId(), configGroup);
                return configGroupMap;
            }
        });
        EasyMock.replay(managementController, clusters, cluster, configGroup, response, configGroupResponse, configHelper, hostDAO, hostEntity1, hostEntity2, h1, h2);
        org.apache.ambari.server.controller.spi.ResourceProvider provider = getConfigGroupResourceProvider(managementController);
        java.util.Map<java.lang.String, java.lang.Object> properties = new java.util.LinkedHashMap<>();
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> hostSet = new java.util.HashSet<>();
        java.util.Map<java.lang.String, java.lang.Object> host1 = new java.util.HashMap<>();
        host1.put(org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.HOST_NAME, "h1");
        hostSet.add(host1);
        java.util.Map<java.lang.String, java.lang.Object> host2 = new java.util.HashMap<>();
        host2.put(org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.HOST_NAME, "h2");
        hostSet.add(host2);
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> configSet = new java.util.HashSet<>();
        java.util.Map<java.lang.String, java.lang.String> configMap = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.Object> configs = new java.util.HashMap<>();
        configs.put("type", "core-site");
        configs.put("tag", "version100");
        configMap.put("key1", "value1");
        configs.put("properties", configMap);
        configSet.add(configs);
        properties.put(org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.CLUSTER_NAME, "Cluster100");
        properties.put(org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.GROUP_NAME, "test-1");
        properties.put(org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.TAG, "tag-1");
        properties.put(org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.HOSTS, hostSet);
        properties.put(org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.DESIRED_CONFIGS, configSet);
        java.util.Map<java.lang.String, java.lang.String> mapRequestProps = new java.util.HashMap<>();
        mapRequestProps.put("context", "Called from a test");
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getUpdateRequest(properties, mapRequestProps);
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.CLUSTER_NAME).equals("Cluster100").and().property(org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.ID).equals(25L).toPredicate();
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        provider.updateResources(request, predicate);
        EasyMock.verify(managementController, clusters, cluster, configGroup, response, configGroupResponse, configHelper, hostDAO, hostEntity1, hostEntity2, h1, h2);
    }

    @org.junit.Test
    public void testGetConfigGroupAsAmbariAdministrator() throws java.lang.Exception {
        testGetConfigGroup(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator());
    }

    @org.junit.Test
    public void testGetConfigGroupAsClusterAdministrator() throws java.lang.Exception {
        testGetConfigGroup(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator());
    }

    @org.junit.Test
    public void testGetConfigGroupAsClusterOperator() throws java.lang.Exception {
        testGetConfigGroup(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterOperator());
    }

    @org.junit.Test
    public void testGetConfigGroupAsServiceAdministrator() throws java.lang.Exception {
        testGetConfigGroup(org.apache.ambari.server.security.TestAuthenticationFactory.createServiceAdministrator());
    }

    @org.junit.Test
    public void testGetConfigGroupAsServiceOperator() throws java.lang.Exception {
        testGetConfigGroup(org.apache.ambari.server.security.TestAuthenticationFactory.createServiceOperator());
    }

    @org.junit.Test
    public void testGetConfigGroupAsClusterUser() throws java.lang.Exception {
        testGetConfigGroup(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterUser());
    }

    @java.lang.SuppressWarnings("unchecked")
    private void testGetConfigGroup(org.springframework.security.core.Authentication authentication) throws java.lang.Exception {
        org.apache.ambari.server.controller.AmbariManagementController managementController = EasyMock.createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.state.Clusters clusters = EasyMock.createNiceMock(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.state.Cluster cluster = EasyMock.createNiceMock(org.apache.ambari.server.state.Cluster.class);
        org.apache.ambari.server.state.Host h1 = EasyMock.createNiceMock(org.apache.ambari.server.state.Host.class);
        final java.lang.Long host1Id = 1L;
        java.util.List<java.lang.Long> hostIds = new java.util.ArrayList<java.lang.Long>() {
            {
                add(host1Id);
            }
        };
        java.util.List<java.lang.String> hostNames = new java.util.ArrayList<java.lang.String>() {
            {
                add("h1");
            }
        };
        org.apache.ambari.server.orm.entities.HostEntity hostEntity1 = EasyMock.createMock(org.apache.ambari.server.orm.entities.HostEntity.class);
        EasyMock.expect(hostDAO.getHostNamesByHostIds(hostIds)).andReturn(hostNames).atLeastOnce();
        EasyMock.expect(hostDAO.findByName("h1")).andReturn(hostEntity1).anyTimes();
        EasyMock.expect(hostEntity1.getHostId()).andReturn(host1Id).anyTimes();
        org.apache.ambari.server.state.configgroup.ConfigGroup configGroup1 = EasyMock.createNiceMock(org.apache.ambari.server.state.configgroup.ConfigGroup.class);
        org.apache.ambari.server.state.configgroup.ConfigGroup configGroup2 = EasyMock.createNiceMock(org.apache.ambari.server.state.configgroup.ConfigGroup.class);
        org.apache.ambari.server.state.configgroup.ConfigGroup configGroup3 = EasyMock.createNiceMock(org.apache.ambari.server.state.configgroup.ConfigGroup.class);
        org.apache.ambari.server.state.configgroup.ConfigGroup configGroup4 = EasyMock.createNiceMock(org.apache.ambari.server.state.configgroup.ConfigGroup.class);
        org.apache.ambari.server.controller.ConfigGroupResponse response1 = EasyMock.createNiceMock(org.apache.ambari.server.controller.ConfigGroupResponse.class);
        org.apache.ambari.server.controller.ConfigGroupResponse response2 = EasyMock.createNiceMock(org.apache.ambari.server.controller.ConfigGroupResponse.class);
        org.apache.ambari.server.controller.ConfigGroupResponse response3 = EasyMock.createNiceMock(org.apache.ambari.server.controller.ConfigGroupResponse.class);
        org.apache.ambari.server.controller.ConfigGroupResponse response4 = EasyMock.createNiceMock(org.apache.ambari.server.controller.ConfigGroupResponse.class);
        java.util.Map<java.lang.Long, org.apache.ambari.server.state.configgroup.ConfigGroup> configGroupMap = new java.util.HashMap<>();
        configGroupMap.put(1L, configGroup1);
        configGroupMap.put(2L, configGroup2);
        configGroupMap.put(3L, configGroup3);
        configGroupMap.put(4L, configGroup4);
        java.util.Map<java.lang.Long, org.apache.ambari.server.state.configgroup.ConfigGroup> configGroupByHostname = new java.util.HashMap<>();
        configGroupByHostname.put(4L, configGroup4);
        EasyMock.expect(configGroup1.convertToResponse()).andReturn(response1).anyTimes();
        EasyMock.expect(configGroup2.convertToResponse()).andReturn(response2).anyTimes();
        EasyMock.expect(configGroup3.convertToResponse()).andReturn(response3).anyTimes();
        EasyMock.expect(configGroup4.convertToResponse()).andReturn(response4).anyTimes();
        EasyMock.expect(managementController.getClusters()).andReturn(clusters).anyTimes();
        EasyMock.expect(clusters.getCluster("Cluster100")).andReturn(cluster).anyTimes();
        EasyMock.expect(cluster.getConfigGroups()).andReturn(configGroupMap).anyTimes();
        EasyMock.expect(cluster.getClusterName()).andReturn("Cluster100").anyTimes();
        EasyMock.expect(configGroup1.getName()).andReturn("g1").anyTimes();
        EasyMock.expect(configGroup2.getName()).andReturn("g2").anyTimes();
        EasyMock.expect(configGroup3.getName()).andReturn("g3").anyTimes();
        EasyMock.expect(configGroup4.getName()).andReturn("g4").anyTimes();
        EasyMock.expect(configGroup1.getTag()).andReturn("t1").anyTimes();
        EasyMock.expect(configGroup2.getTag()).andReturn("t2").anyTimes();
        EasyMock.expect(configGroup3.getTag()).andReturn("t3").anyTimes();
        EasyMock.expect(configGroup4.getTag()).andReturn("t4").anyTimes();
        java.util.Map<java.lang.Long, org.apache.ambari.server.state.Host> hostMap = new java.util.HashMap<>();
        hostMap.put(host1Id, h1);
        EasyMock.expect(configGroup4.getHosts()).andReturn(hostMap).anyTimes();
        EasyMock.expect(response1.getClusterName()).andReturn("Cluster100").anyTimes();
        EasyMock.expect(response2.getClusterName()).andReturn("Cluster100").anyTimes();
        EasyMock.expect(response3.getClusterName()).andReturn("Cluster100").anyTimes();
        EasyMock.expect(response4.getClusterName()).andReturn("Cluster100").anyTimes();
        EasyMock.expect(response1.getId()).andReturn(1L).anyTimes();
        EasyMock.expect(response2.getId()).andReturn(2L).anyTimes();
        EasyMock.expect(response3.getId()).andReturn(3L).anyTimes();
        EasyMock.expect(response4.getId()).andReturn(4L).anyTimes();
        EasyMock.expect(response2.getGroupName()).andReturn("g2").anyTimes();
        EasyMock.expect(response3.getTag()).andReturn("t3").anyTimes();
        EasyMock.expect(cluster.getConfigGroupsByHostname("h1")).andReturn(configGroupByHostname).anyTimes();
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> hostObj = new java.util.HashSet<>();
        java.util.Map<java.lang.String, java.lang.Object> hostnames = new java.util.HashMap<>();
        hostnames.put("host_name", "h1");
        hostObj.add(hostnames);
        EasyMock.expect(response4.getHosts()).andReturn(hostObj).anyTimes();
        EasyMock.replay(managementController, clusters, cluster, hostDAO, hostEntity1, configGroup1, configGroup2, configGroup3, configGroup4, response1, response2, response3, response4);
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        org.apache.ambari.server.controller.spi.ResourceProvider resourceProvider = getConfigGroupResourceProvider(managementController);
        java.util.Set<java.lang.String> propertyIds = new java.util.HashSet<>();
        propertyIds.add(org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.CLUSTER_NAME);
        propertyIds.add(org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.ID);
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.CLUSTER_NAME).equals("Cluster100").toPredicate();
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(propertyIds);
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = resourceProvider.getResources(request, predicate);
        junit.framework.Assert.assertEquals(4, resources.size());
        predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.ID).equals(1L).and().property(org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.CLUSTER_NAME).equals("Cluster100").toPredicate();
        resources = resourceProvider.getResources(request, predicate);
        junit.framework.Assert.assertEquals(1, resources.size());
        junit.framework.Assert.assertEquals(1L, resources.iterator().next().getPropertyValue(org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.ID));
        predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.CLUSTER_NAME).equals("Cluster100").and().property(org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.GROUP_NAME).equals("g2").toPredicate();
        resources = resourceProvider.getResources(request, predicate);
        junit.framework.Assert.assertEquals(1, resources.size());
        junit.framework.Assert.assertEquals("g2", resources.iterator().next().getPropertyValue(org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.GROUP_NAME));
        predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.CLUSTER_NAME).equals("Cluster100").and().property(org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.TAG).equals("t3").toPredicate();
        resources = resourceProvider.getResources(request, predicate);
        junit.framework.Assert.assertEquals(1, resources.size());
        junit.framework.Assert.assertEquals("t3", resources.iterator().next().getPropertyValue(org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.TAG));
        predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.CLUSTER_NAME).equals("Cluster100").and().property(org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.HOSTS).equals("h1").toPredicate();
        resources = resourceProvider.getResources(request, predicate);
        junit.framework.Assert.assertEquals(1, resources.size());
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> hostSet = ((java.util.Set<java.util.Map<java.lang.String, java.lang.Object>>) (resources.iterator().next().getPropertyValue(org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.HOSTS)));
        junit.framework.Assert.assertEquals("h1", hostSet.iterator().next().get(org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.HOST_NAME));
        predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.CLUSTER_NAME).equals("Cluster100").and().property(org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.HOSTS_HOST_NAME).equals("h1").toPredicate();
        resources = resourceProvider.getResources(request, predicate);
        junit.framework.Assert.assertEquals(1, resources.size());
        hostSet = ((java.util.Set<java.util.Map<java.lang.String, java.lang.Object>>) (resources.iterator().next().getPropertyValue(org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.HOSTS)));
        junit.framework.Assert.assertEquals("h1", hostSet.iterator().next().get(org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.HOST_NAME));
        predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.CLUSTER_NAME).equals("Cluster100").and().property(org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.TAG).equals("t4").and().property(org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.HOSTS).equals(host1Id).toPredicate();
        resources = resourceProvider.getResources(request, predicate);
        junit.framework.Assert.assertEquals(1, resources.size());
        hostSet = ((java.util.Set<java.util.Map<java.lang.String, java.lang.Object>>) (resources.iterator().next().getPropertyValue(org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.HOSTS)));
        junit.framework.Assert.assertEquals("h1", hostSet.iterator().next().get(org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.HOST_NAME));
        predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.CLUSTER_NAME).equals("Cluster100").and().property(org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.TAG).equals("t4").and().property(org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.HOSTS_HOST_NAME).equals("h1").toPredicate();
        resources = resourceProvider.getResources(request, predicate);
        junit.framework.Assert.assertEquals(1, resources.size());
        hostSet = ((java.util.Set<java.util.Map<java.lang.String, java.lang.Object>>) (resources.iterator().next().getPropertyValue(org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.HOSTS)));
        junit.framework.Assert.assertEquals("h1", hostSet.iterator().next().get(org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.HOST_NAME));
        predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.ID).equals(11L).and().property(org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.CLUSTER_NAME).equals("Cluster100").toPredicate();
        org.apache.ambari.server.controller.spi.NoSuchResourceException resourceException = null;
        try {
            resourceProvider.getResources(request, predicate);
        } catch (org.apache.ambari.server.controller.spi.NoSuchResourceException ce) {
            resourceException = ce;
        }
        org.junit.Assert.assertNotNull(resourceException);
        EasyMock.verify(managementController, clusters, cluster, hostDAO, hostEntity1, configGroup1, configGroup2, configGroup3, configGroup4, response1, response2, response3, response4);
    }

    @org.junit.Test
    public void testDeleteConfigGroupAsAmbariAdministrator() throws java.lang.Exception {
        testDeleteConfigGroup(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator());
    }

    @org.junit.Test
    public void testDeleteConfigGroupAsClusterAdministrator() throws java.lang.Exception {
        testDeleteConfigGroup(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator());
    }

    @org.junit.Test
    public void testDeleteConfigGroupAsClusterOperator() throws java.lang.Exception {
        testDeleteConfigGroup(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterOperator());
    }

    @org.junit.Test
    public void testDeleteConfigGroupAsServiceAdministrator() throws java.lang.Exception {
        testDeleteConfigGroup(org.apache.ambari.server.security.TestAuthenticationFactory.createServiceAdministrator());
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testDeleteConfigGroupAsServiceOperator() throws java.lang.Exception {
        testDeleteConfigGroup(org.apache.ambari.server.security.TestAuthenticationFactory.createServiceOperator());
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testDeleteConfigGroupAsClusterUser() throws java.lang.Exception {
        testDeleteConfigGroup(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterUser());
    }

    private void testDeleteConfigGroup(org.springframework.security.core.Authentication authentication) throws java.lang.Exception {
        org.apache.ambari.server.controller.AmbariManagementController managementController = EasyMock.createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.state.Clusters clusters = EasyMock.createNiceMock(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.state.Cluster cluster = EasyMock.createNiceMock(org.apache.ambari.server.state.Cluster.class);
        org.apache.ambari.server.state.configgroup.ConfigGroup configGroup = EasyMock.createNiceMock(org.apache.ambari.server.state.configgroup.ConfigGroup.class);
        EasyMock.expect(managementController.getAuthName()).andReturn("admin").anyTimes();
        EasyMock.expect(managementController.getClusters()).andReturn(clusters).anyTimes();
        EasyMock.expect(clusters.getCluster("Cluster100")).andReturn(cluster).anyTimes();
        EasyMock.expect(cluster.getConfigGroups()).andReturn(java.util.Collections.singletonMap(1L, configGroup));
        cluster.deleteConfigGroup(1L);
        EasyMock.replay(managementController, clusters, cluster, configGroup);
        org.apache.ambari.server.controller.spi.ResourceProvider resourceProvider = getConfigGroupResourceProvider(managementController);
        org.apache.ambari.server.controller.internal.AbstractResourceProviderTest.TestObserver observer = new org.apache.ambari.server.controller.internal.AbstractResourceProviderTest.TestObserver();
        ((org.apache.ambari.server.controller.internal.ObservableResourceProvider) (resourceProvider)).addObserver(observer);
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.CLUSTER_NAME).equals("Cluster100").and().property(org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.ID).equals(1L).toPredicate();
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        resourceProvider.deleteResources(new org.apache.ambari.server.controller.internal.RequestImpl(null, null, null, null), predicate);
        org.apache.ambari.server.controller.internal.ResourceProviderEvent lastEvent = observer.getLastEvent();
        org.junit.Assert.assertNotNull(lastEvent);
        org.junit.Assert.assertEquals(org.apache.ambari.server.controller.spi.Resource.Type.ConfigGroup, lastEvent.getResourceType());
        org.junit.Assert.assertEquals(org.apache.ambari.server.controller.internal.ResourceProviderEvent.Type.Delete, lastEvent.getType());
        org.junit.Assert.assertEquals(predicate, lastEvent.getPredicate());
        org.junit.Assert.assertNull(lastEvent.getRequest());
        EasyMock.verify(managementController, clusters, cluster, configGroup);
    }

    @org.junit.Test
    public void testGetConfigGroupRequest_populatesConfigAttributes() throws java.lang.Exception {
        org.apache.ambari.server.controller.AmbariManagementController managementController = EasyMock.createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider resourceProvider = getConfigGroupResourceProvider(managementController);
        java.util.Set<java.util.Map<java.lang.String, java.lang.String>> desiredConfigProperties = new java.util.HashSet<>();
        java.util.Map<java.lang.String, java.lang.String> desiredConfig1 = new java.util.HashMap<>();
        desiredConfig1.put("tag", "version2");
        desiredConfig1.put("type", "type1");
        desiredConfig1.put("properties/key1", "value1");
        desiredConfig1.put("properties/key2", "value2");
        desiredConfig1.put("properties_attributes/attr1/key1", "true");
        desiredConfig1.put("properties_attributes/attr1/key2", "false");
        desiredConfig1.put("properties_attributes/attr2/key1", "15");
        desiredConfigProperties.add(desiredConfig1);
        java.util.Map<java.lang.String, java.lang.Object> properties = new java.util.HashMap<>();
        properties.put("ConfigGroup/hosts", new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put("host_name", "ambari1");
            }
        });
        properties.put("ConfigGroup/cluster_name", "c");
        properties.put("ConfigGroup/desired_configs", desiredConfigProperties);
        org.apache.ambari.server.controller.ConfigGroupRequest request = resourceProvider.getConfigGroupRequest(properties);
        junit.framework.Assert.assertNotNull(request);
        java.util.Map<java.lang.String, org.apache.ambari.server.state.Config> configMap = request.getConfigs();
        junit.framework.Assert.assertNotNull(configMap);
        junit.framework.Assert.assertEquals(1, configMap.size());
        junit.framework.Assert.assertTrue(configMap.containsKey("type1"));
        org.apache.ambari.server.state.Config config = configMap.get("type1");
        junit.framework.Assert.assertEquals("type1", config.getType());
        java.util.Map<java.lang.String, java.lang.String> configProperties = config.getProperties();
        junit.framework.Assert.assertNotNull(configProperties);
        junit.framework.Assert.assertEquals(2, configProperties.size());
        junit.framework.Assert.assertEquals("value1", configProperties.get("key1"));
        junit.framework.Assert.assertEquals("value2", configProperties.get("key2"));
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configAttributes = config.getPropertiesAttributes();
        junit.framework.Assert.assertNotNull(configAttributes);
        junit.framework.Assert.assertEquals(2, configAttributes.size());
        junit.framework.Assert.assertTrue(configAttributes.containsKey("attr1"));
        java.util.Map<java.lang.String, java.lang.String> attr1 = configAttributes.get("attr1");
        junit.framework.Assert.assertNotNull(attr1);
        junit.framework.Assert.assertEquals(2, attr1.size());
        junit.framework.Assert.assertEquals("true", attr1.get("key1"));
        junit.framework.Assert.assertEquals("false", attr1.get("key2"));
        junit.framework.Assert.assertTrue(configAttributes.containsKey("attr2"));
        java.util.Map<java.lang.String, java.lang.String> attr2 = configAttributes.get("attr2");
        junit.framework.Assert.assertNotNull(attr2);
        junit.framework.Assert.assertEquals(1, attr2.size());
        junit.framework.Assert.assertEquals("15", attr2.get("key1"));
    }
}