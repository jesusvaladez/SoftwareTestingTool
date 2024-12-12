package org.apache.ambari.server.controller.internal;
import org.easymock.Capture;
import org.easymock.EasyMock;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import static org.apache.ambari.server.controller.internal.HostComponentResourceProvider.STALE_CONFIGS;
import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.newCapture;
import static org.powermock.api.easymock.PowerMock.createMock;
import static org.powermock.api.easymock.PowerMock.createNiceMock;
import static org.powermock.api.easymock.PowerMock.replay;
import static org.powermock.api.easymock.PowerMock.replayAll;
import static org.powermock.api.easymock.PowerMock.reset;
import static org.powermock.api.easymock.PowerMock.resetAll;
import static org.powermock.api.easymock.PowerMock.verify;
import static org.powermock.api.easymock.PowerMock.verifyAll;
@org.junit.runner.RunWith(org.powermock.modules.junit4.PowerMockRunner.class)
@org.powermock.core.classloader.annotations.PrepareForTest({ org.apache.ambari.server.controller.utilities.ClusterControllerHelper.class })
public class RequestResourceProviderTest {
    private org.apache.ambari.server.orm.dao.RequestDAO requestDAO;

    private org.apache.ambari.server.orm.dao.HostRoleCommandDAO hrcDAO;

    private org.apache.ambari.server.topology.TopologyManager topologyManager;

    @org.junit.Before
    public void before() throws java.lang.Exception {
        requestDAO = createNiceMock(org.apache.ambari.server.orm.dao.RequestDAO.class);
        hrcDAO = createNiceMock(org.apache.ambari.server.orm.dao.HostRoleCommandDAO.class);
        topologyManager = createNiceMock(org.apache.ambari.server.topology.TopologyManager.class);
        reset(topologyManager);
        EasyMock.expect(topologyManager.getStageSummaries(org.easymock.EasyMock.<java.lang.Long>anyObject())).andReturn(java.util.Collections.emptyMap()).anyTimes();
        EasyMock.expect(topologyManager.getRequests(org.easymock.EasyMock.anyObject())).andReturn(java.util.Collections.emptyList()).anyTimes();
        replay(topologyManager);
        java.lang.reflect.Field field = org.apache.ambari.server.controller.internal.RequestResourceProvider.class.getDeclaredField("s_requestDAO");
        field.setAccessible(true);
        field.set(null, requestDAO);
        field = org.apache.ambari.server.controller.internal.RequestResourceProvider.class.getDeclaredField("s_hostRoleCommandDAO");
        field.setAccessible(true);
        field.set(null, hrcDAO);
        field = org.apache.ambari.server.controller.internal.RequestResourceProvider.class.getDeclaredField("topologyManager");
        field.setAccessible(true);
        field.set(null, topologyManager);
        field = org.apache.ambari.server.utils.SecretReference.class.getDeclaredField("gson");
        field.setAccessible(true);
        field.set(null, new com.google.gson.Gson());
        org.apache.ambari.server.security.authorization.AuthorizationHelperInitializer.viewInstanceDAOReturningNull();
    }

    @org.junit.After
    public void cleanAuthentication() {
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(null);
    }

    @org.junit.Test
    public void testCreateResources() throws java.lang.Exception {
        org.apache.ambari.server.controller.spi.Resource.Type type = org.apache.ambari.server.controller.spi.Resource.Type.Request;
        org.apache.ambari.server.controller.AmbariManagementController managementController = createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.controller.RequestStatusResponse response = createNiceMock(org.apache.ambari.server.controller.RequestStatusResponse.class);
        replay(managementController, response);
        org.apache.ambari.server.controller.spi.ResourceProvider provider = org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.getResourceProvider(type, managementController);
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> propertySet = new java.util.LinkedHashSet<>();
        java.util.Map<java.lang.String, java.lang.Object> properties = new java.util.LinkedHashMap<>();
        properties.put(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_ID_PROPERTY_ID, "Request100");
        propertySet.add(properties);
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getCreateRequest(propertySet, null);
        try {
            provider.createResources(request);
            org.junit.Assert.fail("Expected an UnsupportedOperationException");
        } catch (java.lang.UnsupportedOperationException e) {
        }
        verify(managementController, response);
    }

    @org.junit.Test
    public void testGetResourcesWithRequestInfo() throws java.lang.Exception {
        org.apache.ambari.server.controller.spi.Resource.Type type = org.apache.ambari.server.controller.spi.Resource.Type.Request;
        EasyMock.expect(requestDAO.findByPks(java.util.Collections.emptyList(), true)).andReturn(java.util.Collections.emptyList()).anyTimes();
        prepareGetAuthorizationExpectations();
        org.apache.ambari.server.actionmanager.ActionManager actionManager = createNiceMock(org.apache.ambari.server.actionmanager.ActionManager.class);
        org.apache.ambari.server.state.Cluster cluster = createNiceMock(org.apache.ambari.server.state.Cluster.class);
        EasyMock.expect(cluster.getClusterId()).andReturn(1L).anyTimes();
        EasyMock.expect(cluster.getResourceId()).andReturn(4L).anyTimes();
        org.apache.ambari.server.state.Clusters clusters = createNiceMock(org.apache.ambari.server.state.Clusters.class);
        EasyMock.expect(clusters.getCluster("foo_cluster")).andReturn(cluster).anyTimes();
        org.apache.ambari.server.controller.AmbariManagementController managementController = createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        EasyMock.expect(managementController.getActionManager()).andReturn(actionManager).anyTimes();
        EasyMock.expect(managementController.getClusters()).andReturn(clusters).anyTimes();
        replay(managementController, clusters, cluster);
        org.apache.ambari.server.controller.spi.ResourceProvider provider = org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.getResourceProvider(type, managementController);
        java.util.Map<java.lang.String, java.lang.String> requestInfoProperties = new java.util.HashMap<>();
        org.apache.ambari.server.controller.spi.Request request;
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_CLUSTER_NAME_PROPERTY_ID).equals("foo_cluster").and().property(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_ID_PROPERTY_ID).equals(null).and().property(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_STATUS_PROPERTY_ID).equals(null).toPredicate();
        request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(new java.util.HashSet<>(), requestInfoProperties, null, null, null);
        EasyMock.expect(requestDAO.findAllRequestIds(org.apache.ambari.server.api.services.BaseRequest.DEFAULT_PAGE_SIZE, false, 1L)).andReturn(java.util.Collections.emptyList()).anyTimes();
        replay(requestDAO);
        provider.getResources(request, predicate);
        verify(requestDAO);
        requestInfoProperties.put(org.apache.ambari.server.api.services.BaseRequest.PAGE_SIZE_PROPERTY_KEY, "20");
        request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(new java.util.HashSet<>(), requestInfoProperties, null, null, null);
        provider.getResources(request, predicate);
        verify(requestDAO);
        reset(requestDAO);
        requestInfoProperties.put(org.apache.ambari.server.api.services.BaseRequest.ASC_ORDER_PROPERTY_KEY, "true");
        request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(new java.util.HashSet<>(), requestInfoProperties, null, null, null);
        EasyMock.expect(requestDAO.findByPks(java.util.Collections.emptyList(), true)).andReturn(java.util.Collections.emptyList()).anyTimes();
        EasyMock.expect(requestDAO.findAllRequestIds(org.apache.ambari.server.api.services.BaseRequest.DEFAULT_PAGE_SIZE, true, 1L)).andReturn(java.util.Collections.emptyList()).anyTimes();
        replay(requestDAO);
        provider.getResources(request, predicate);
        verify(requestDAO);
    }

    private void prepareGetAuthorizationExpectations() {
        prepareGetAuthorizationExpectations(true);
    }

    private void prepareGetAuthorizationExpectations(boolean allowedToAuthorize) {
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(allowedToAuthorize ? org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator() : org.apache.ambari.server.security.TestAuthenticationFactory.createViewUser(1L));
    }

    @org.junit.Test
    public void testGetResources() throws java.lang.Exception {
        org.apache.ambari.server.controller.spi.Resource.Type type = org.apache.ambari.server.controller.spi.Resource.Type.Request;
        java.lang.String storedInputs = "{" + ((((((((" \"hosts\": \"host1\"," + " \"check_execute_list\": \"last_agent_env_check,installed_packages,existing_repos,transparentHugePage\",") + " \"jdk_location\": \"http://ambari_server.home:8080/resources/\",") + " \"threshold\": \"20\",") + " \"password\": \"for your eyes only\",") + " \"foo_password\": \"for your eyes only\",") + " \"passwd\": \"for your eyes only\",") + " \"foo_passwd\": \"for your eyes only\"") + " }");
        java.lang.String cleanedInputs = org.apache.ambari.server.utils.SecretReference.maskPasswordInPropertyMap(storedInputs);
        com.google.gson.Gson gson = new com.google.gson.Gson();
        java.util.Map<java.lang.String, java.lang.String> map = gson.fromJson(cleanedInputs, new com.google.gson.reflect.TypeToken<java.util.Map<java.lang.String, java.lang.String>>() {}.getType());
        for (java.util.Map.Entry<java.lang.String, java.lang.String> entry : map.entrySet()) {
            java.lang.String name = entry.getKey();
            if (name.contains("password") || name.contains("passwd")) {
                org.junit.Assert.assertEquals("SECRET", entry.getValue());
            } else {
                org.junit.Assert.assertFalse("SECRET".equals(entry.getValue()));
            }
        }
        org.apache.ambari.server.controller.AmbariManagementController managementController = createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.actionmanager.ActionManager actionManager = createNiceMock(org.apache.ambari.server.actionmanager.ActionManager.class);
        org.apache.ambari.server.orm.entities.RequestEntity requestMock = createNiceMock(org.apache.ambari.server.orm.entities.RequestEntity.class);
        EasyMock.expect(requestMock.getRequestContext()).andReturn("this is a context").anyTimes();
        EasyMock.expect(requestMock.getRequestId()).andReturn(100L).anyTimes();
        EasyMock.expect(requestMock.getInputs()).andReturn(storedInputs).anyTimes();
        org.easymock.Capture<java.util.Collection<java.lang.Long>> requestIdsCapture = EasyMock.newCapture();
        EasyMock.expect(managementController.getActionManager()).andReturn(actionManager);
        EasyMock.expect(requestDAO.findByPks(EasyMock.capture(requestIdsCapture), EasyMock.eq(true))).andReturn(java.util.Collections.singletonList(requestMock)).anyTimes();
        EasyMock.expect(hrcDAO.findAggregateCounts(((java.lang.Long) (EasyMock.anyObject())))).andReturn(new java.util.HashMap<java.lang.Long, org.apache.ambari.server.orm.dao.HostRoleCommandStatusSummaryDTO>() {
            {
                put(1L, org.apache.ambari.server.orm.dao.HostRoleCommandStatusSummaryDTO.create().inProgress(1));
            }
        }).anyTimes();
        prepareGetAuthorizationExpectations();
        replay(managementController, actionManager, requestDAO, hrcDAO, requestMock);
        org.apache.ambari.server.controller.spi.ResourceProvider provider = org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.getResourceProvider(type, managementController);
        java.util.Set<java.lang.String> propertyIds = new java.util.HashSet<>();
        propertyIds.add(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_ID_PROPERTY_ID);
        propertyIds.add(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_STATUS_PROPERTY_ID);
        propertyIds.add(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_INPUTS_ID);
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_ID_PROPERTY_ID).equals("100").toPredicate();
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(propertyIds);
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = provider.getResources(request, predicate);
        org.junit.Assert.assertEquals(1, resources.size());
        for (org.apache.ambari.server.controller.spi.Resource resource : resources) {
            org.junit.Assert.assertEquals(100L, ((long) ((java.lang.Long) (resource.getPropertyValue(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_ID_PROPERTY_ID)))));
            org.junit.Assert.assertEquals("IN_PROGRESS", resource.getPropertyValue(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_STATUS_PROPERTY_ID));
            org.junit.Assert.assertEquals(cleanedInputs, resource.getPropertyValue(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_INPUTS_ID));
        }
        verify(managementController, actionManager, requestDAO, hrcDAO);
    }

    @org.junit.Test
    public void testGetResourcesWithRequestSchedule() throws java.lang.Exception {
        org.apache.ambari.server.controller.spi.Resource.Type type = org.apache.ambari.server.controller.spi.Resource.Type.Request;
        org.apache.ambari.server.controller.AmbariManagementController managementController = createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.actionmanager.ActionManager actionManager = createNiceMock(org.apache.ambari.server.actionmanager.ActionManager.class);
        org.apache.ambari.server.orm.entities.RequestEntity requestMock = createNiceMock(org.apache.ambari.server.orm.entities.RequestEntity.class);
        EasyMock.expect(requestMock.getRequestContext()).andReturn("this is a context").anyTimes();
        EasyMock.expect(requestMock.getRequestId()).andReturn(100L).anyTimes();
        EasyMock.expect(requestMock.getRequestScheduleId()).andReturn(11L).anyTimes();
        org.easymock.Capture<java.util.Collection<java.lang.Long>> requestIdsCapture = EasyMock.newCapture();
        EasyMock.expect(managementController.getActionManager()).andReturn(actionManager);
        EasyMock.expect(requestDAO.findByPks(EasyMock.capture(requestIdsCapture), EasyMock.eq(true))).andReturn(java.util.Collections.singletonList(requestMock)).anyTimes();
        EasyMock.expect(hrcDAO.findAggregateCounts(((java.lang.Long) (EasyMock.anyObject())))).andReturn(new java.util.HashMap<java.lang.Long, org.apache.ambari.server.orm.dao.HostRoleCommandStatusSummaryDTO>() {
            {
                put(1L, org.apache.ambari.server.orm.dao.HostRoleCommandStatusSummaryDTO.create().inProgress(1));
            }
        }).anyTimes();
        prepareGetAuthorizationExpectations();
        replay(managementController, actionManager, requestDAO, hrcDAO, requestMock);
        org.apache.ambari.server.controller.spi.ResourceProvider provider = org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.getResourceProvider(type, managementController);
        java.util.Set<java.lang.String> propertyIds = new java.util.HashSet<>();
        propertyIds.add(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_ID_PROPERTY_ID);
        propertyIds.add(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_STATUS_PROPERTY_ID);
        propertyIds.add(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_SOURCE_SCHEDULE);
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_ID_PROPERTY_ID).equals("100").toPredicate();
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(propertyIds);
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = provider.getResources(request, predicate);
        org.junit.Assert.assertEquals(1, resources.size());
        for (org.apache.ambari.server.controller.spi.Resource resource : resources) {
            org.junit.Assert.assertEquals(100L, ((long) ((java.lang.Long) (resource.getPropertyValue(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_ID_PROPERTY_ID)))));
            org.junit.Assert.assertEquals("IN_PROGRESS", resource.getPropertyValue(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_STATUS_PROPERTY_ID));
            org.junit.Assert.assertEquals(11L, ((long) ((java.lang.Long) (resource.getPropertyValue(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_SOURCE_SCHEDULE_ID)))));
        }
        verify(managementController, actionManager, requestDAO, hrcDAO);
    }

    @org.junit.Test
    public void testGetResourcesWithoutRequestSchedule() throws java.lang.Exception {
        org.apache.ambari.server.controller.spi.Resource.Type type = org.apache.ambari.server.controller.spi.Resource.Type.Request;
        org.apache.ambari.server.controller.AmbariManagementController managementController = createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.actionmanager.ActionManager actionManager = createNiceMock(org.apache.ambari.server.actionmanager.ActionManager.class);
        org.apache.ambari.server.orm.entities.RequestEntity requestMock = createNiceMock(org.apache.ambari.server.orm.entities.RequestEntity.class);
        EasyMock.expect(requestMock.getRequestContext()).andReturn("this is a context").anyTimes();
        EasyMock.expect(requestMock.getRequestId()).andReturn(100L).anyTimes();
        EasyMock.expect(requestMock.getRequestScheduleId()).andReturn(null).anyTimes();
        org.easymock.Capture<java.util.Collection<java.lang.Long>> requestIdsCapture = EasyMock.newCapture();
        EasyMock.expect(managementController.getActionManager()).andReturn(actionManager);
        EasyMock.expect(requestDAO.findByPks(EasyMock.capture(requestIdsCapture), EasyMock.eq(true))).andReturn(java.util.Collections.singletonList(requestMock)).anyTimes();
        EasyMock.expect(hrcDAO.findAggregateCounts(((java.lang.Long) (EasyMock.anyObject())))).andReturn(new java.util.HashMap<java.lang.Long, org.apache.ambari.server.orm.dao.HostRoleCommandStatusSummaryDTO>() {
            {
                put(1L, org.apache.ambari.server.orm.dao.HostRoleCommandStatusSummaryDTO.create().inProgress(1));
            }
        }).anyTimes();
        prepareGetAuthorizationExpectations();
        replay(managementController, actionManager, requestMock, requestDAO, hrcDAO);
        org.apache.ambari.server.controller.spi.ResourceProvider provider = org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.getResourceProvider(type, managementController);
        java.util.Set<java.lang.String> propertyIds = new java.util.HashSet<>();
        propertyIds.add(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_ID_PROPERTY_ID);
        propertyIds.add(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_STATUS_PROPERTY_ID);
        propertyIds.add(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_SOURCE_SCHEDULE);
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_ID_PROPERTY_ID).equals("100").toPredicate();
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(propertyIds);
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = provider.getResources(request, predicate);
        org.junit.Assert.assertEquals(1, resources.size());
        for (org.apache.ambari.server.controller.spi.Resource resource : resources) {
            org.junit.Assert.assertEquals(100L, ((long) ((java.lang.Long) (resource.getPropertyValue(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_ID_PROPERTY_ID)))));
            org.junit.Assert.assertEquals("IN_PROGRESS", resource.getPropertyValue(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_STATUS_PROPERTY_ID));
            org.junit.Assert.assertEquals(null, resource.getPropertyValue(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_SOURCE_SCHEDULE));
        }
        verify(managementController, actionManager, requestMock, requestDAO, hrcDAO);
    }

    @org.junit.Test
    public void testGetResourcesWithCluster() throws java.lang.Exception {
        org.apache.ambari.server.controller.spi.Resource.Type type = org.apache.ambari.server.controller.spi.Resource.Type.Request;
        org.apache.ambari.server.controller.AmbariManagementController managementController = createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.actionmanager.ActionManager actionManager = createNiceMock(org.apache.ambari.server.actionmanager.ActionManager.class);
        org.apache.ambari.server.state.Clusters clusters = createNiceMock(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.state.Cluster cluster = createNiceMock(org.apache.ambari.server.state.Cluster.class);
        EasyMock.expect(cluster.getClusterId()).andReturn(50L).anyTimes();
        org.apache.ambari.server.orm.entities.RequestEntity requestMock = createNiceMock(org.apache.ambari.server.orm.entities.RequestEntity.class);
        EasyMock.expect(requestMock.getRequestContext()).andReturn("this is a context").anyTimes();
        EasyMock.expect(requestMock.getClusterId()).andReturn(50L).anyTimes();
        EasyMock.expect(requestMock.getRequestId()).andReturn(100L).anyTimes();
        org.easymock.Capture<java.util.Collection<java.lang.Long>> requestIdsCapture = EasyMock.newCapture();
        EasyMock.expect(managementController.getActionManager()).andReturn(actionManager).anyTimes();
        EasyMock.expect(managementController.getClusters()).andReturn(clusters).anyTimes();
        EasyMock.expect(clusters.getCluster("c1")).andReturn(cluster).anyTimes();
        EasyMock.expect(clusters.getCluster("bad-cluster")).andThrow(new org.apache.ambari.server.AmbariException("bad cluster!")).anyTimes();
        EasyMock.expect(requestDAO.findByPks(EasyMock.capture(requestIdsCapture), EasyMock.eq(true))).andReturn(java.util.Collections.singletonList(requestMock));
        EasyMock.expect(hrcDAO.findAggregateCounts(((java.lang.Long) (EasyMock.anyObject())))).andReturn(new java.util.HashMap<java.lang.Long, org.apache.ambari.server.orm.dao.HostRoleCommandStatusSummaryDTO>() {
            {
                put(1L, org.apache.ambari.server.orm.dao.HostRoleCommandStatusSummaryDTO.create().inProgress(1));
            }
        }).anyTimes();
        prepareGetAuthorizationExpectations();
        replay(managementController, actionManager, clusters, cluster, requestMock, requestDAO, hrcDAO);
        org.apache.ambari.server.controller.spi.ResourceProvider provider = org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.getResourceProvider(type, managementController);
        java.util.Set<java.lang.String> propertyIds = new java.util.HashSet<>();
        propertyIds.add(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_ID_PROPERTY_ID);
        propertyIds.add(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_STATUS_PROPERTY_ID);
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_CLUSTER_NAME_PROPERTY_ID).equals("c1").and().property(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_ID_PROPERTY_ID).equals("100").toPredicate();
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(propertyIds);
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = provider.getResources(request, predicate);
        org.junit.Assert.assertEquals(1, resources.size());
        for (org.apache.ambari.server.controller.spi.Resource resource : resources) {
            org.junit.Assert.assertEquals(100L, ((long) ((java.lang.Long) (resource.getPropertyValue(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_ID_PROPERTY_ID)))));
            org.junit.Assert.assertEquals("IN_PROGRESS", resource.getPropertyValue(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_STATUS_PROPERTY_ID));
        }
        predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_CLUSTER_NAME_PROPERTY_ID).equals("bad-cluster").and().property(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_ID_PROPERTY_ID).equals("100").toPredicate();
        try {
            provider.getResources(request, predicate);
        } catch (org.apache.ambari.server.controller.spi.NoSuchParentResourceException e) {
            e.printStackTrace();
        }
        verify(managementController, actionManager, clusters, cluster, requestMock, requestDAO, hrcDAO);
    }

    @org.junit.Test
    public void testGetResourcesOrPredicate() throws java.lang.Exception {
        org.apache.ambari.server.controller.spi.Resource.Type type = org.apache.ambari.server.controller.spi.Resource.Type.Request;
        org.apache.ambari.server.controller.AmbariManagementController managementController = createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.actionmanager.ActionManager actionManager = createNiceMock(org.apache.ambari.server.actionmanager.ActionManager.class);
        org.apache.ambari.server.orm.entities.RequestEntity requestMock = createNiceMock(org.apache.ambari.server.orm.entities.RequestEntity.class);
        EasyMock.expect(requestMock.getRequestContext()).andReturn("this is a context").anyTimes();
        EasyMock.expect(requestMock.getRequestId()).andReturn(100L).anyTimes();
        org.apache.ambari.server.orm.entities.RequestEntity requestMock1 = createNiceMock(org.apache.ambari.server.orm.entities.RequestEntity.class);
        EasyMock.expect(requestMock1.getRequestContext()).andReturn("this is a context").anyTimes();
        EasyMock.expect(requestMock1.getRequestId()).andReturn(101L).anyTimes();
        org.easymock.Capture<java.util.Collection<java.lang.Long>> requestIdsCapture = EasyMock.newCapture();
        EasyMock.expect(managementController.getActionManager()).andReturn(actionManager).anyTimes();
        EasyMock.expect(requestDAO.findByPks(EasyMock.capture(requestIdsCapture), EasyMock.eq(true))).andReturn(java.util.Arrays.asList(requestMock, requestMock1)).anyTimes();
        EasyMock.expect(hrcDAO.findAggregateCounts(((java.lang.Long) (EasyMock.anyObject())))).andReturn(new java.util.HashMap<java.lang.Long, org.apache.ambari.server.orm.dao.HostRoleCommandStatusSummaryDTO>() {
            {
                put(1L, org.apache.ambari.server.orm.dao.HostRoleCommandStatusSummaryDTO.create().inProgress(1));
            }
        }).anyTimes();
        prepareGetAuthorizationExpectations();
        replay(managementController, actionManager, requestMock, requestMock1, requestDAO, hrcDAO);
        org.apache.ambari.server.controller.spi.ResourceProvider provider = org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.getResourceProvider(type, managementController);
        java.util.Set<java.lang.String> propertyIds = new java.util.HashSet<>();
        propertyIds.add(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_ID_PROPERTY_ID);
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_ID_PROPERTY_ID).equals("100").or().property(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_ID_PROPERTY_ID).equals("101").toPredicate();
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(propertyIds);
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = provider.getResources(request, predicate);
        org.junit.Assert.assertEquals(2, resources.size());
        for (org.apache.ambari.server.controller.spi.Resource resource : resources) {
            long id = ((java.lang.Long) (resource.getPropertyValue(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_ID_PROPERTY_ID)));
            org.junit.Assert.assertTrue((id == 100L) || (id == 101L));
        }
        verify(managementController, actionManager, requestMock, requestMock1, requestDAO, hrcDAO);
    }

    @org.junit.Test
    public void testGetResourcesCompleted() throws java.lang.Exception {
        org.apache.ambari.server.controller.spi.Resource.Type type = org.apache.ambari.server.controller.spi.Resource.Type.Request;
        org.apache.ambari.server.controller.AmbariManagementController managementController = createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.actionmanager.ActionManager actionManager = createNiceMock(org.apache.ambari.server.actionmanager.ActionManager.class);
        org.apache.ambari.server.orm.entities.RequestEntity requestMock0 = createNiceMock(org.apache.ambari.server.orm.entities.RequestEntity.class);
        EasyMock.expect(requestMock0.getRequestContext()).andReturn("this is a context").anyTimes();
        EasyMock.expect(requestMock0.getRequestId()).andReturn(100L).anyTimes();
        org.apache.ambari.server.orm.entities.RequestEntity requestMock1 = createNiceMock(org.apache.ambari.server.orm.entities.RequestEntity.class);
        EasyMock.expect(requestMock1.getRequestContext()).andReturn("this is a context").anyTimes();
        EasyMock.expect(requestMock1.getRequestId()).andReturn(101L).anyTimes();
        org.easymock.Capture<java.util.Collection<java.lang.Long>> requestIdsCapture = EasyMock.newCapture();
        EasyMock.expect(managementController.getActionManager()).andReturn(actionManager).anyTimes();
        EasyMock.expect(requestDAO.findByPks(EasyMock.capture(requestIdsCapture), EasyMock.eq(true))).andReturn(java.util.Arrays.asList(requestMock0));
        EasyMock.expect(requestDAO.findByPks(EasyMock.capture(requestIdsCapture), EasyMock.eq(true))).andReturn(java.util.Arrays.asList(requestMock1));
        EasyMock.expect(hrcDAO.findAggregateCounts(((java.lang.Long) (EasyMock.anyObject())))).andReturn(new java.util.HashMap<java.lang.Long, org.apache.ambari.server.orm.dao.HostRoleCommandStatusSummaryDTO>() {
            {
                put(1L, org.apache.ambari.server.orm.dao.HostRoleCommandStatusSummaryDTO.create().completed(2));
            }
        }).anyTimes();
        prepareGetAuthorizationExpectations();
        replay(managementController, actionManager, requestMock0, requestMock1, requestDAO, hrcDAO);
        org.apache.ambari.server.controller.spi.ResourceProvider provider = org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.getResourceProvider(type, managementController);
        java.util.Set<java.lang.String> propertyIds = new java.util.HashSet<>();
        propertyIds.add(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_ID_PROPERTY_ID);
        propertyIds.add(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_STATUS_PROPERTY_ID);
        propertyIds.add(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_TASK_CNT_ID);
        propertyIds.add(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_COMPLETED_TASK_CNT_ID);
        propertyIds.add(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_FAILED_TASK_CNT_ID);
        propertyIds.add(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_PROGRESS_PERCENT_ID);
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_ID_PROPERTY_ID).equals("100").or().property(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_ID_PROPERTY_ID).equals("101").toPredicate();
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(propertyIds);
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = provider.getResources(request, predicate);
        org.junit.Assert.assertEquals(2, resources.size());
        for (org.apache.ambari.server.controller.spi.Resource resource : resources) {
            long id = ((java.lang.Long) (resource.getPropertyValue(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_ID_PROPERTY_ID)));
            org.junit.Assert.assertTrue((id == 100L) || (id == 101L));
            org.junit.Assert.assertEquals("COMPLETED", resource.getPropertyValue(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_STATUS_PROPERTY_ID));
            org.junit.Assert.assertEquals(2, resource.getPropertyValue(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_TASK_CNT_ID));
            org.junit.Assert.assertEquals(0, resource.getPropertyValue(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_FAILED_TASK_CNT_ID));
            org.junit.Assert.assertEquals(2, resource.getPropertyValue(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_COMPLETED_TASK_CNT_ID));
            org.junit.Assert.assertEquals(100.0, resource.getPropertyValue(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_PROGRESS_PERCENT_ID));
        }
        verify(managementController, actionManager, requestMock0, requestMock1, requestDAO, hrcDAO);
    }

    @org.junit.Test
    public void testGetResourcesInProgress() throws java.lang.Exception {
        org.apache.ambari.server.controller.spi.Resource.Type type = org.apache.ambari.server.controller.spi.Resource.Type.Request;
        org.apache.ambari.server.controller.AmbariManagementController managementController = createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.actionmanager.ActionManager actionManager = createNiceMock(org.apache.ambari.server.actionmanager.ActionManager.class);
        org.apache.ambari.server.orm.entities.RequestEntity requestMock0 = createNiceMock(org.apache.ambari.server.orm.entities.RequestEntity.class);
        EasyMock.expect(requestMock0.getRequestContext()).andReturn("this is a context").anyTimes();
        EasyMock.expect(requestMock0.getRequestId()).andReturn(100L).anyTimes();
        org.apache.ambari.server.orm.entities.RequestEntity requestMock1 = createNiceMock(org.apache.ambari.server.orm.entities.RequestEntity.class);
        EasyMock.expect(requestMock1.getRequestContext()).andReturn("this is a context").anyTimes();
        EasyMock.expect(requestMock1.getRequestId()).andReturn(101L).anyTimes();
        org.easymock.Capture<java.util.Collection<java.lang.Long>> requestIdsCapture = org.easymock.EasyMock.newCapture();
        EasyMock.expect(managementController.getActionManager()).andReturn(actionManager).anyTimes();
        EasyMock.expect(requestDAO.findByPks(EasyMock.capture(requestIdsCapture), EasyMock.eq(true))).andReturn(java.util.Arrays.asList(requestMock0));
        EasyMock.expect(requestDAO.findByPks(EasyMock.capture(requestIdsCapture), EasyMock.eq(true))).andReturn(java.util.Arrays.asList(requestMock1));
        EasyMock.expect(hrcDAO.findAggregateCounts(100L)).andReturn(new java.util.HashMap<java.lang.Long, org.apache.ambari.server.orm.dao.HostRoleCommandStatusSummaryDTO>() {
            {
                put(1L, org.apache.ambari.server.orm.dao.HostRoleCommandStatusSummaryDTO.create().inProgress(1).pending(1));
            }
        }).once();
        EasyMock.expect(hrcDAO.findAggregateCounts(101L)).andReturn(new java.util.HashMap<java.lang.Long, org.apache.ambari.server.orm.dao.HostRoleCommandStatusSummaryDTO>() {
            {
                put(1L, org.apache.ambari.server.orm.dao.HostRoleCommandStatusSummaryDTO.create().inProgress(1).queued(1));
            }
        }).once();
        prepareGetAuthorizationExpectations();
        replay(managementController, actionManager, requestMock0, requestMock1, requestDAO, hrcDAO);
        org.apache.ambari.server.controller.spi.ResourceProvider provider = org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.getResourceProvider(type, managementController);
        java.util.Set<java.lang.String> propertyIds = new java.util.HashSet<>();
        propertyIds.add(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_ID_PROPERTY_ID);
        propertyIds.add(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_STATUS_PROPERTY_ID);
        propertyIds.add(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_TASK_CNT_ID);
        propertyIds.add(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_COMPLETED_TASK_CNT_ID);
        propertyIds.add(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_FAILED_TASK_CNT_ID);
        propertyIds.add(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_QUEUED_TASK_CNT_ID);
        propertyIds.add(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_PROGRESS_PERCENT_ID);
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_ID_PROPERTY_ID).equals("100").or().property(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_ID_PROPERTY_ID).equals("101").toPredicate();
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(propertyIds);
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = provider.getResources(request, predicate);
        org.junit.Assert.assertEquals(2, resources.size());
        for (org.apache.ambari.server.controller.spi.Resource resource : resources) {
            long id = ((java.lang.Long) (resource.getPropertyValue(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_ID_PROPERTY_ID)));
            org.junit.Assert.assertTrue((id == 100L) || (id == 101L));
            org.junit.Assert.assertEquals("IN_PROGRESS", resource.getPropertyValue(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_STATUS_PROPERTY_ID));
            org.junit.Assert.assertEquals(2, resource.getPropertyValue(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_TASK_CNT_ID));
            org.junit.Assert.assertEquals(0, resource.getPropertyValue(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_FAILED_TASK_CNT_ID));
            if (id == 100L) {
                org.junit.Assert.assertEquals(0, resource.getPropertyValue(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_QUEUED_TASK_CNT_ID));
                int progressPercent = ((java.lang.Double) (resource.getPropertyValue(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_PROGRESS_PERCENT_ID))).intValue();
                org.junit.Assert.assertEquals(17, progressPercent);
            } else {
                org.junit.Assert.assertEquals(1, resource.getPropertyValue(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_QUEUED_TASK_CNT_ID));
                int progressPercent = ((java.lang.Double) (resource.getPropertyValue(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_PROGRESS_PERCENT_ID))).intValue();
                org.junit.Assert.assertEquals(21, progressPercent);
            }
            org.junit.Assert.assertEquals(0, resource.getPropertyValue(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_COMPLETED_TASK_CNT_ID));
        }
        verify(managementController, actionManager, requestMock0, requestMock1, requestDAO, hrcDAO);
    }

    @org.junit.Test
    public void testGetResourcesFailed() throws java.lang.Exception {
        org.apache.ambari.server.controller.spi.Resource.Type type = org.apache.ambari.server.controller.spi.Resource.Type.Request;
        org.apache.ambari.server.controller.AmbariManagementController managementController = createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.actionmanager.ActionManager actionManager = createNiceMock(org.apache.ambari.server.actionmanager.ActionManager.class);
        org.apache.ambari.server.orm.entities.RequestEntity requestMock0 = createNiceMock(org.apache.ambari.server.orm.entities.RequestEntity.class);
        EasyMock.expect(requestMock0.getRequestContext()).andReturn("this is a context").anyTimes();
        EasyMock.expect(requestMock0.getRequestId()).andReturn(100L).anyTimes();
        org.apache.ambari.server.orm.entities.RequestEntity requestMock1 = createNiceMock(org.apache.ambari.server.orm.entities.RequestEntity.class);
        EasyMock.expect(requestMock1.getRequestContext()).andReturn("this is a context").anyTimes();
        EasyMock.expect(requestMock1.getRequestId()).andReturn(101L).anyTimes();
        org.easymock.Capture<java.util.Collection<java.lang.Long>> requestIdsCapture = org.easymock.EasyMock.newCapture();
        EasyMock.expect(managementController.getActionManager()).andReturn(actionManager).anyTimes();
        EasyMock.expect(requestDAO.findByPks(EasyMock.capture(requestIdsCapture), EasyMock.eq(true))).andReturn(java.util.Arrays.asList(requestMock0));
        EasyMock.expect(requestDAO.findByPks(EasyMock.capture(requestIdsCapture), EasyMock.eq(true))).andReturn(java.util.Arrays.asList(requestMock1));
        EasyMock.expect(hrcDAO.findAggregateCounts(100L)).andReturn(new java.util.HashMap<java.lang.Long, org.apache.ambari.server.orm.dao.HostRoleCommandStatusSummaryDTO>() {
            {
                put(1L, org.apache.ambari.server.orm.dao.HostRoleCommandStatusSummaryDTO.create().failed(1).completed(1));
            }
        }).once();
        EasyMock.expect(hrcDAO.findAggregateCounts(101L)).andReturn(new java.util.HashMap<java.lang.Long, org.apache.ambari.server.orm.dao.HostRoleCommandStatusSummaryDTO>() {
            {
                put(1L, org.apache.ambari.server.orm.dao.HostRoleCommandStatusSummaryDTO.create().aborted(1).timedout(1));
            }
        }).once();
        prepareGetAuthorizationExpectations();
        replay(managementController, actionManager, requestMock0, requestMock1, requestDAO, hrcDAO);
        org.apache.ambari.server.controller.spi.ResourceProvider provider = org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.getResourceProvider(type, managementController);
        java.util.Set<java.lang.String> propertyIds = new java.util.HashSet<>();
        propertyIds.add(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_ID_PROPERTY_ID);
        propertyIds.add(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_STATUS_PROPERTY_ID);
        propertyIds.add(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_TASK_CNT_ID);
        propertyIds.add(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_COMPLETED_TASK_CNT_ID);
        propertyIds.add(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_FAILED_TASK_CNT_ID);
        propertyIds.add(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_ABORTED_TASK_CNT_ID);
        propertyIds.add(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_TIMED_OUT_TASK_CNT_ID);
        propertyIds.add(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_PROGRESS_PERCENT_ID);
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_ID_PROPERTY_ID).equals("100").or().property(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_ID_PROPERTY_ID).equals("101").toPredicate();
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(propertyIds);
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = provider.getResources(request, predicate);
        org.junit.Assert.assertEquals(2, resources.size());
        for (org.apache.ambari.server.controller.spi.Resource resource : resources) {
            long id = ((java.lang.Long) (resource.getPropertyValue(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_ID_PROPERTY_ID)));
            org.junit.Assert.assertTrue((id == 100L) || (id == 101L));
            org.junit.Assert.assertEquals(2, resource.getPropertyValue(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_TASK_CNT_ID));
            if (id == 100L) {
                org.junit.Assert.assertEquals("FAILED", resource.getPropertyValue(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_STATUS_PROPERTY_ID));
                org.junit.Assert.assertEquals(1, resource.getPropertyValue(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_FAILED_TASK_CNT_ID));
                org.junit.Assert.assertEquals(0, resource.getPropertyValue(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_ABORTED_TASK_CNT_ID));
                org.junit.Assert.assertEquals(0, resource.getPropertyValue(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_TIMED_OUT_TASK_CNT_ID));
            } else {
                org.junit.Assert.assertEquals("TIMEDOUT", resource.getPropertyValue(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_STATUS_PROPERTY_ID));
                org.junit.Assert.assertEquals(0, resource.getPropertyValue(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_FAILED_TASK_CNT_ID));
                org.junit.Assert.assertEquals(1, resource.getPropertyValue(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_ABORTED_TASK_CNT_ID));
                org.junit.Assert.assertEquals(1, resource.getPropertyValue(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_TIMED_OUT_TASK_CNT_ID));
            }
            org.junit.Assert.assertEquals(2, resource.getPropertyValue(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_COMPLETED_TASK_CNT_ID));
            org.junit.Assert.assertEquals(100.0, resource.getPropertyValue(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_PROGRESS_PERCENT_ID));
        }
        verify(managementController, actionManager, requestMock0, requestMock1, requestDAO, hrcDAO);
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void shouldThrowAuthorizationErrorInCaseTheAuthenticatedUserDoesNotHaveTheAppropriatePermissions() throws java.lang.Exception {
        prepareGetAuthorizationExpectations(false);
        final org.apache.ambari.server.controller.spi.ResourceProvider provider = org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.getResourceProvider(org.apache.ambari.server.controller.spi.Resource.Type.Request, createMock(org.apache.ambari.server.controller.AmbariManagementController.class));
        final org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getUpdateRequest(new java.util.LinkedHashMap<>(), null);
        final org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_ID_PROPERTY_ID).equals("100").toPredicate();
        provider.getResources(request, predicate);
    }

    @org.junit.Test
    public void testUpdateResources_CancelRequest() throws java.lang.Exception {
        org.apache.ambari.server.controller.spi.Resource.Type type = org.apache.ambari.server.controller.spi.Resource.Type.Request;
        org.apache.ambari.server.controller.AmbariManagementController managementController = createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.actionmanager.ActionManager actionManager = createNiceMock(org.apache.ambari.server.actionmanager.ActionManager.class);
        org.apache.ambari.server.actionmanager.HostRoleCommand hostRoleCommand = createNiceMock(org.apache.ambari.server.actionmanager.HostRoleCommand.class);
        org.apache.ambari.server.actionmanager.Stage stage = createNiceMock(org.apache.ambari.server.actionmanager.Stage.class);
        org.apache.ambari.server.state.Clusters clusters = createNiceMock(org.apache.ambari.server.state.Clusters.class);
        java.util.List<org.apache.ambari.server.actionmanager.HostRoleCommand> hostRoleCommands = new java.util.LinkedList<>();
        hostRoleCommands.add(hostRoleCommand);
        java.util.Collection<org.apache.ambari.server.actionmanager.Stage> stages = new java.util.HashSet<>();
        stages.add(stage);
        org.apache.ambari.server.actionmanager.Request requestMock = createNiceMock(org.apache.ambari.server.actionmanager.Request.class);
        EasyMock.expect(requestMock.getCommands()).andReturn(hostRoleCommands).anyTimes();
        EasyMock.expect(requestMock.getStages()).andReturn(stages).anyTimes();
        EasyMock.expect(stage.getOrderedHostRoleCommands()).andReturn(hostRoleCommands).anyTimes();
        org.easymock.Capture<java.util.Collection<java.lang.Long>> requestIdsCapture = EasyMock.newCapture();
        EasyMock.expect(managementController.getActionManager()).andReturn(actionManager).anyTimes();
        EasyMock.expect(actionManager.getRequests(EasyMock.capture(requestIdsCapture))).andReturn(java.util.Collections.singletonList(requestMock)).anyTimes();
        EasyMock.expect(hostRoleCommand.getStatus()).andReturn(org.apache.ambari.server.actionmanager.HostRoleStatus.IN_PROGRESS).anyTimes();
        org.apache.ambari.server.controller.RequestStatusResponse response = createNiceMock(org.apache.ambari.server.controller.RequestStatusResponse.class);
        prepareGetAuthorizationExpectations();
        replay(managementController, actionManager, hostRoleCommand, clusters, requestMock, response, stage);
        org.apache.ambari.server.controller.spi.ResourceProvider provider = org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.getResourceProvider(type, managementController);
        java.util.Map<java.lang.String, java.lang.Object> properties = new java.util.LinkedHashMap<>();
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getUpdateRequest(properties, null);
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_ID_PROPERTY_ID).equals("100").toPredicate();
        try {
            provider.updateResources(request, predicate);
            org.junit.Assert.fail("Expected an java.lang.IllegalArgumentException: Abort reason can not be empty.");
        } catch (java.lang.IllegalArgumentException e) {
        }
        properties.put(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_ABORT_REASON_PROPERTY_ID, "Some reason");
        request = org.apache.ambari.server.controller.utilities.PropertyHelper.getUpdateRequest(properties, null);
        try {
            provider.updateResources(request, predicate);
            org.junit.Assert.fail("Expected an java.lang.IllegalArgumentException: null is wrong value.");
        } catch (java.lang.IllegalArgumentException e) {
        }
        properties.put(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_STATUS_PROPERTY_ID, "COMPLETED");
        request = org.apache.ambari.server.controller.utilities.PropertyHelper.getUpdateRequest(properties, null);
        try {
            provider.updateResources(request, predicate);
            org.junit.Assert.fail("Expected an java.lang.IllegalArgumentException: COMPLETED is wrong value. " + "The only allowed value for updating request status is ABORTED");
        } catch (java.lang.IllegalArgumentException e) {
        }
        properties.put(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_STATUS_PROPERTY_ID, "ABORTED");
        for (org.apache.ambari.server.actionmanager.HostRoleStatus status : org.apache.ambari.server.actionmanager.HostRoleStatus.values()) {
            reset(hostRoleCommand);
            EasyMock.expect(hostRoleCommand.getStatus()).andReturn(status).anyTimes();
            replay(hostRoleCommand);
            request = org.apache.ambari.server.controller.utilities.PropertyHelper.getUpdateRequest(properties, null);
            if (((((((((((status == org.apache.ambari.server.actionmanager.HostRoleStatus.IN_PROGRESS) || (status == org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING)) || (status == org.apache.ambari.server.actionmanager.HostRoleStatus.HOLDING)) || (status == org.apache.ambari.server.actionmanager.HostRoleStatus.HOLDING_FAILED)) || (status == org.apache.ambari.server.actionmanager.HostRoleStatus.HOLDING_TIMEDOUT)) || (status == org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED)) || (status == org.apache.ambari.server.actionmanager.HostRoleStatus.ABORTED)) || (status == org.apache.ambari.server.actionmanager.HostRoleStatus.FAILED)) || (status == org.apache.ambari.server.actionmanager.HostRoleStatus.TIMEDOUT)) || (status == org.apache.ambari.server.actionmanager.HostRoleStatus.QUEUED)) || (status == org.apache.ambari.server.actionmanager.HostRoleStatus.SKIPPED_FAILED)) {
                provider.updateResources(request, predicate);
            } else {
                try {
                    provider.updateResources(request, predicate);
                    org.junit.Assert.fail("Expected an java.lang.IllegalArgumentException");
                } catch (java.lang.IllegalArgumentException e) {
                }
            }
        }
        verify(managementController, response, stage);
    }

    @org.junit.Test
    public void testDeleteResources() throws java.lang.Exception {
        org.apache.ambari.server.controller.spi.Resource.Type type = org.apache.ambari.server.controller.spi.Resource.Type.Request;
        org.apache.ambari.server.controller.AmbariManagementController managementController = createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        replay(managementController);
        org.apache.ambari.server.controller.spi.ResourceProvider provider = org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.getResourceProvider(type, managementController);
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_ID_PROPERTY_ID).equals("Request100").toPredicate();
        try {
            provider.deleteResources(new org.apache.ambari.server.controller.internal.RequestImpl(null, null, null, null), predicate);
            org.junit.Assert.fail("Expected an UnsupportedOperationException");
        } catch (java.lang.UnsupportedOperationException e) {
        }
        verify(managementController);
    }

    @org.junit.Test
    public void testCreateResourcesForCommandsAsAdministrator() throws java.lang.Exception {
        testCreateResourcesForCommands(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator());
    }

    @org.junit.Test
    public void testCreateResourcesForCommandsAsClusterAdministrator() throws java.lang.Exception {
        testCreateResourcesForCommands(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator());
    }

    @org.junit.Test
    public void testCreateResourcesForCommandsAsServiceAdministrator() throws java.lang.Exception {
        testCreateResourcesForCommands(org.apache.ambari.server.security.TestAuthenticationFactory.createServiceAdministrator());
    }

    @org.junit.Test
    public void testCreateResourcesForCommandsAsServiceOperator() throws java.lang.Exception {
        testCreateResourcesForCommands(org.apache.ambari.server.security.TestAuthenticationFactory.createServiceOperator());
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testCreateResourcesForCommandsAsClusterUser() throws java.lang.Exception {
        testCreateResourcesForCommands(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterUser());
    }

    private void testCreateResourcesForCommands(org.springframework.security.core.Authentication authentication) throws java.lang.Exception {
        org.apache.ambari.server.controller.spi.Resource.Type type = org.apache.ambari.server.controller.spi.Resource.Type.Request;
        org.easymock.Capture<org.apache.ambari.server.controller.ExecuteActionRequest> actionRequest = EasyMock.newCapture();
        org.easymock.Capture<java.util.HashMap<java.lang.String, java.lang.String>> propertyMap = EasyMock.newCapture();
        org.apache.ambari.server.state.Cluster cluster = createMock(org.apache.ambari.server.state.Cluster.class);
        EasyMock.expect(cluster.getClusterId()).andReturn(2L).anyTimes();
        EasyMock.expect(cluster.getResourceId()).andReturn(4L).anyTimes();
        org.apache.ambari.server.state.Clusters clusters = createMock(org.apache.ambari.server.state.Clusters.class);
        EasyMock.expect(clusters.getCluster("c1")).andReturn(cluster).anyTimes();
        org.apache.ambari.server.controller.AmbariManagementController managementController = createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.controller.RequestStatusResponse response = createNiceMock(org.apache.ambari.server.controller.RequestStatusResponse.class);
        EasyMock.expect(managementController.getClusters()).andReturn(clusters).anyTimes();
        EasyMock.expect(managementController.createAction(EasyMock.capture(actionRequest), EasyMock.capture(propertyMap))).andReturn(response).anyTimes();
        EasyMock.expect(response.getMessage()).andReturn("Message").anyTimes();
        replay(cluster, clusters, managementController, response);
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> propertySet = new java.util.LinkedHashSet<>();
        java.util.Map<java.lang.String, java.lang.Object> properties = new java.util.LinkedHashMap<>();
        properties.put(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_CLUSTER_NAME_PROPERTY_ID, "c1");
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> filterSet = new java.util.HashSet<>();
        java.util.Map<java.lang.String, java.lang.Object> filterMap = new java.util.HashMap<>();
        filterMap.put(org.apache.ambari.server.controller.internal.RequestResourceProvider.SERVICE_ID, "HDFS");
        filterSet.add(filterMap);
        properties.put(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_RESOURCE_FILTER_ID, filterSet);
        propertySet.add(properties);
        java.util.Map<java.lang.String, java.lang.String> requestInfoProperties = new java.util.HashMap<>();
        requestInfoProperties.put(org.apache.ambari.server.controller.internal.RequestResourceProvider.COMMAND_ID, "HDFS_SERVICE_CHECK");
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getCreateRequest(propertySet, requestInfoProperties);
        org.apache.ambari.server.controller.spi.ResourceProvider provider = org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.getResourceProvider(type, managementController);
        provider.createResources(request);
        org.apache.ambari.server.controller.ExecuteActionRequest capturedRequest = actionRequest.getValue();
        org.junit.Assert.assertTrue(actionRequest.hasCaptured());
        org.junit.Assert.assertTrue(capturedRequest.isCommand());
        org.junit.Assert.assertEquals(null, capturedRequest.getActionName());
        org.junit.Assert.assertEquals("HDFS_SERVICE_CHECK", capturedRequest.getCommandName());
        org.junit.Assert.assertNotNull(capturedRequest.getResourceFilters());
        org.junit.Assert.assertEquals(1, capturedRequest.getResourceFilters().size());
        org.apache.ambari.server.controller.internal.RequestResourceFilter capturedResourceFilter = capturedRequest.getResourceFilters().get(0);
        org.junit.Assert.assertEquals("HDFS", capturedResourceFilter.getServiceName());
        org.junit.Assert.assertEquals(null, capturedResourceFilter.getComponentName());
        org.junit.Assert.assertNotNull(capturedResourceFilter.getHostNames());
        org.junit.Assert.assertEquals(0, capturedResourceFilter.getHostNames().size());
        org.junit.Assert.assertEquals(1, actionRequest.getValue().getParameters().size());
    }

    @org.junit.Test
    public void testCreateResourcesForCommandsWithParamsAsAdministrator() throws java.lang.Exception {
        testCreateResourcesForCommandsWithParams(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator());
    }

    @org.junit.Test
    public void testCreateResourcesForCommandsWithParamsAsClusterAdministrator() throws java.lang.Exception {
        testCreateResourcesForCommandsWithParams(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator());
    }

    @org.junit.Test
    public void testCreateResourcesForCommandsWithParamsAsServiceAdministrator() throws java.lang.Exception {
        testCreateResourcesForCommandsWithParams(org.apache.ambari.server.security.TestAuthenticationFactory.createServiceAdministrator());
    }

    @org.junit.Test
    public void testCreateResourcesForCommandsWithParamsAsServiceOperator() throws java.lang.Exception {
        testCreateResourcesForCommandsWithParams(org.apache.ambari.server.security.TestAuthenticationFactory.createServiceOperator());
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testCreateResourcesForCommandsWithParamsAsClusterUser() throws java.lang.Exception {
        testCreateResourcesForCommandsWithParams(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterUser());
    }

    private void testCreateResourcesForCommandsWithParams(org.springframework.security.core.Authentication authentication) throws java.lang.Exception {
        org.apache.ambari.server.controller.spi.Resource.Type type = org.apache.ambari.server.controller.spi.Resource.Type.Request;
        org.easymock.Capture<org.apache.ambari.server.controller.ExecuteActionRequest> actionRequest = EasyMock.newCapture();
        org.easymock.Capture<java.util.HashMap<java.lang.String, java.lang.String>> propertyMap = EasyMock.newCapture();
        org.apache.ambari.server.state.Cluster cluster = createMock(org.apache.ambari.server.state.Cluster.class);
        EasyMock.expect(cluster.getClusterId()).andReturn(2L).anyTimes();
        EasyMock.expect(cluster.getResourceId()).andReturn(4L).anyTimes();
        org.apache.ambari.server.state.Clusters clusters = createMock(org.apache.ambari.server.state.Clusters.class);
        EasyMock.expect(clusters.getCluster("c1")).andReturn(cluster).anyTimes();
        org.apache.ambari.server.controller.AmbariManagementController managementController = createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.controller.RequestStatusResponse response = createNiceMock(org.apache.ambari.server.controller.RequestStatusResponse.class);
        EasyMock.expect(managementController.getClusters()).andReturn(clusters).anyTimes();
        EasyMock.expect(managementController.createAction(EasyMock.capture(actionRequest), EasyMock.capture(propertyMap))).andReturn(response).anyTimes();
        EasyMock.expect(response.getMessage()).andReturn("Message").anyTimes();
        replay(cluster, clusters, managementController, response);
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> propertySet = new java.util.LinkedHashSet<>();
        java.util.Map<java.lang.String, java.lang.Object> properties = new java.util.LinkedHashMap<>();
        properties.put(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_CLUSTER_NAME_PROPERTY_ID, "c1");
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> filterSet = new java.util.HashSet<>();
        java.util.Map<java.lang.String, java.lang.Object> filterMap = new java.util.HashMap<>();
        filterMap.put(org.apache.ambari.server.controller.internal.RequestResourceProvider.SERVICE_ID, "HDFS");
        filterMap.put(org.apache.ambari.server.controller.internal.RequestResourceProvider.HOSTS_ID, "host1,host2,host3");
        filterSet.add(filterMap);
        properties.put(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_RESOURCE_FILTER_ID, filterSet);
        propertySet.add(properties);
        java.util.Map<java.lang.String, java.lang.String> requestInfoProperties = new java.util.HashMap<>();
        requestInfoProperties.put("parameters/param1", "value1");
        requestInfoProperties.put("parameters/param2", "value2");
        java.lang.String[] expectedHosts = new java.lang.String[]{ "host1", "host2", "host3" };
        java.util.Map<java.lang.String, java.lang.String> expectedParams = new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put("param1", "value1");
                put("param2", "value2");
            }
        };
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getCreateRequest(propertySet, requestInfoProperties);
        org.apache.ambari.server.controller.spi.ResourceProvider provider = org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.getResourceProvider(type, managementController);
        try {
            provider.createResources(request);
        } catch (java.lang.UnsupportedOperationException ex) {
            org.junit.Assert.assertTrue(ex.getMessage().contains("Either command or action must be specified"));
        }
        requestInfoProperties.put(org.apache.ambari.server.controller.internal.RequestResourceProvider.COMMAND_ID, "HDFS_SERVICE_CHECK");
        requestInfoProperties.put(org.apache.ambari.server.controller.internal.RequestResourceProvider.ACTION_ID, "a1");
        request = org.apache.ambari.server.controller.utilities.PropertyHelper.getCreateRequest(propertySet, requestInfoProperties);
        try {
            provider.createResources(request);
        } catch (java.lang.UnsupportedOperationException ex) {
            org.junit.Assert.assertTrue(ex.getMessage().contains("Both command and action cannot be specified"));
        }
        requestInfoProperties.remove(org.apache.ambari.server.controller.internal.RequestResourceProvider.ACTION_ID);
        request = org.apache.ambari.server.controller.utilities.PropertyHelper.getCreateRequest(propertySet, requestInfoProperties);
        provider.createResources(request);
        org.junit.Assert.assertTrue(actionRequest.hasCaptured());
        org.apache.ambari.server.controller.ExecuteActionRequest capturedRequest = actionRequest.getValue();
        org.junit.Assert.assertTrue(capturedRequest.isCommand());
        org.junit.Assert.assertEquals(null, capturedRequest.getActionName());
        org.junit.Assert.assertEquals("HDFS_SERVICE_CHECK", capturedRequest.getCommandName());
        org.junit.Assert.assertEquals(1, capturedRequest.getResourceFilters().size());
        org.apache.ambari.server.controller.internal.RequestResourceFilter capturedResourceFilter = capturedRequest.getResourceFilters().get(0);
        org.junit.Assert.assertEquals("HDFS", capturedResourceFilter.getServiceName());
        org.junit.Assert.assertEquals(null, capturedResourceFilter.getComponentName());
        org.junit.Assert.assertEquals(3, capturedResourceFilter.getHostNames().size());
        org.junit.Assert.assertArrayEquals(expectedHosts, capturedResourceFilter.getHostNames().toArray());
        org.junit.Assert.assertEquals(3, capturedRequest.getParameters().size());
        for (java.lang.String key : expectedParams.keySet()) {
            org.junit.Assert.assertEquals(expectedParams.get(key), capturedRequest.getParameters().get(key));
        }
    }

    @org.junit.Test
    public void testCreateResourcesForCommandWithHostPredicate() throws java.lang.Exception {
        org.apache.ambari.server.controller.spi.Resource.Type type = org.apache.ambari.server.controller.spi.Resource.Type.Request;
        org.easymock.Capture<org.apache.ambari.server.controller.ExecuteActionRequest> actionRequest = org.easymock.EasyMock.newCapture();
        org.easymock.Capture<java.util.HashMap<java.lang.String, java.lang.String>> propertyMap = org.easymock.EasyMock.newCapture();
        org.easymock.Capture<org.apache.ambari.server.controller.spi.Request> requestCapture = org.easymock.EasyMock.newCapture();
        org.easymock.Capture<org.apache.ambari.server.controller.spi.Predicate> predicateCapture = org.easymock.EasyMock.newCapture();
        org.apache.ambari.server.controller.AmbariManagementController managementController = createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.controller.RequestStatusResponse response = createNiceMock(org.apache.ambari.server.controller.RequestStatusResponse.class);
        org.apache.ambari.server.state.Clusters clusters = createNiceMock(org.apache.ambari.server.state.Clusters.class);
        EasyMock.expect(managementController.createAction(EasyMock.capture(actionRequest), EasyMock.capture(propertyMap))).andReturn(response).anyTimes();
        EasyMock.expect(response.getMessage()).andReturn("Message").anyTimes();
        EasyMock.expect(managementController.getClusters()).andReturn(clusters);
        org.apache.ambari.server.controller.internal.ClusterControllerImpl controller = createNiceMock(org.apache.ambari.server.controller.internal.ClusterControllerImpl.class);
        org.apache.ambari.server.controller.internal.HostComponentProcessResourceProvider hostComponentProcessResourceProvider = createNiceMock(org.apache.ambari.server.controller.internal.HostComponentProcessResourceProvider.class);
        org.powermock.api.easymock.PowerMock.mockStatic(org.apache.ambari.server.controller.utilities.ClusterControllerHelper.class);
        org.apache.ambari.server.controller.spi.Resource resource = createNiceMock(org.apache.ambari.server.controller.spi.Resource.class);
        java.util.Collection<org.apache.ambari.server.controller.spi.Resource> resources = java.util.Collections.singleton(resource);
        java.lang.Iterable<org.apache.ambari.server.controller.spi.Resource> resourceIterable = new java.lang.Iterable<org.apache.ambari.server.controller.spi.Resource>() {
            @java.lang.Override
            public java.util.Iterator<org.apache.ambari.server.controller.spi.Resource> iterator() {
                return resources.iterator();
            }
        };
        EasyMock.expect(org.apache.ambari.server.controller.utilities.ClusterControllerHelper.getClusterController()).andReturn(controller);
        EasyMock.expect(controller.ensureResourceProvider(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent)).andReturn(hostComponentProcessResourceProvider);
        org.apache.ambari.server.controller.spi.QueryResponse queryResponse = createNiceMock(org.apache.ambari.server.controller.spi.QueryResponse.class);
        EasyMock.expect(controller.getResources(EasyMock.eq(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent), EasyMock.capture(requestCapture), EasyMock.capture(predicateCapture))).andReturn(queryResponse);
        EasyMock.expect(controller.getIterable(EasyMock.eq(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent), EasyMock.eq(queryResponse), ((org.apache.ambari.server.controller.spi.Request) (EasyMock.anyObject())), ((org.apache.ambari.server.controller.spi.Predicate) (EasyMock.anyObject())), EasyMock.eq(null), EasyMock.eq(null))).andReturn(resourceIterable);
        replayAll();
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator());
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> propertySet = new java.util.LinkedHashSet<>();
        java.util.Map<java.lang.String, java.lang.Object> properties = new java.util.LinkedHashMap<>();
        properties.put(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_CLUSTER_NAME_PROPERTY_ID, "c1");
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> filterSet = new java.util.HashSet<>();
        java.lang.String predicateProperty = org.apache.ambari.server.controller.internal.HostComponentResourceProvider.STALE_CONFIGS + "=true";
        java.util.Map<java.lang.String, java.lang.Object> filterMap = new java.util.HashMap<>();
        filterMap.put(org.apache.ambari.server.controller.internal.RequestResourceProvider.HOSTS_PREDICATE, predicateProperty);
        filterSet.add(filterMap);
        properties.put(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_RESOURCE_FILTER_ID, filterSet);
        propertySet.add(properties);
        java.util.Map<java.lang.String, java.lang.String> requestInfoProperties = new java.util.HashMap<>();
        requestInfoProperties.put(org.apache.ambari.server.controller.internal.RequestResourceProvider.COMMAND_ID, "RESTART");
        requestInfoProperties.put(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_CONTEXT_ID, "Restart All with Stale Configs");
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getCreateRequest(propertySet, requestInfoProperties);
        org.apache.ambari.server.controller.spi.ResourceProvider provider = org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.getResourceProvider(type, managementController);
        provider.createResources(request);
        org.apache.ambari.server.controller.ExecuteActionRequest capturedRequest = actionRequest.getValue();
        org.junit.Assert.assertTrue(requestCapture.hasCaptured());
        org.junit.Assert.assertTrue(predicateCapture.hasCaptured());
        java.util.Map<java.lang.String, java.lang.Object> predicateProperties = org.apache.ambari.server.controller.utilities.PredicateHelper.getProperties(predicateCapture.getValue());
        java.lang.String propertyIdToAssert = null;
        java.lang.Object propertyValueToAssert = null;
        for (java.util.Map.Entry<java.lang.String, java.lang.Object> predicateEntry : predicateProperties.entrySet()) {
            if (predicateEntry.getKey().equals(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.STALE_CONFIGS)) {
                propertyIdToAssert = predicateEntry.getKey();
                propertyValueToAssert = predicateEntry.getValue();
            }
        }
        org.junit.Assert.assertNotNull(propertyIdToAssert);
        org.junit.Assert.assertEquals("true", propertyValueToAssert);
        org.junit.Assert.assertTrue(capturedRequest.getResourceFilters().isEmpty());
        org.junit.Assert.assertEquals(1, capturedRequest.getParameters().size());
        org.junit.Assert.assertEquals("true", capturedRequest.getParameters().get(org.apache.ambari.server.controller.internal.RequestResourceProvider.HAS_RESOURCE_FILTERS));
    }

    @org.junit.Test
    public void testCreateResourcesForCommandsWithOpLvlAsAdministrator() throws java.lang.Exception {
        testCreateResourcesForCommandsWithOpLvl(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator());
    }

    @org.junit.Test
    public void testCreateResourcesForCommandsWithOpLvlAsClusterAdministrator() throws java.lang.Exception {
        testCreateResourcesForCommandsWithOpLvl(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator());
    }

    @org.junit.Test
    public void testCreateResourcesForCommandsWithOpLvlAsServiceAdministrator() throws java.lang.Exception {
        testCreateResourcesForCommandsWithOpLvl(org.apache.ambari.server.security.TestAuthenticationFactory.createServiceAdministrator());
    }

    @org.junit.Test
    public void testCreateResourcesForCommandsWithOpLvlAsServiceOperator() throws java.lang.Exception {
        testCreateResourcesForCommandsWithOpLvl(org.apache.ambari.server.security.TestAuthenticationFactory.createServiceOperator());
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testCreateResourcesForCommandsWithOpLvlAsClusterUser() throws java.lang.Exception {
        testCreateResourcesForCommandsWithOpLvl(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterUser());
    }

    private void testCreateResourcesForCommandsWithOpLvl(org.springframework.security.core.Authentication authentication) throws java.lang.Exception {
        org.apache.ambari.server.controller.spi.Resource.Type type = org.apache.ambari.server.controller.spi.Resource.Type.Request;
        org.easymock.Capture<org.apache.ambari.server.controller.ExecuteActionRequest> actionRequest = EasyMock.newCapture();
        org.easymock.Capture<java.util.HashMap<java.lang.String, java.lang.String>> propertyMap = EasyMock.newCapture();
        org.apache.ambari.server.state.Cluster cluster = createMock(org.apache.ambari.server.state.Cluster.class);
        EasyMock.expect(cluster.getClusterId()).andReturn(2L).anyTimes();
        EasyMock.expect(cluster.getResourceId()).andReturn(4L).anyTimes();
        org.apache.ambari.server.state.Clusters clusters = createMock(org.apache.ambari.server.state.Clusters.class);
        EasyMock.expect(clusters.getCluster("c1")).andReturn(cluster).anyTimes();
        org.apache.ambari.server.controller.AmbariManagementController managementController = createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.controller.RequestStatusResponse response = createNiceMock(org.apache.ambari.server.controller.RequestStatusResponse.class);
        EasyMock.expect(managementController.getClusters()).andReturn(clusters).anyTimes();
        EasyMock.expect(managementController.createAction(EasyMock.capture(actionRequest), EasyMock.capture(propertyMap))).andReturn(response).anyTimes();
        EasyMock.expect(response.getMessage()).andReturn("Message").anyTimes();
        replay(cluster, clusters, managementController, response);
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> propertySet = new java.util.LinkedHashSet<>();
        java.util.Map<java.lang.String, java.lang.Object> properties = new java.util.LinkedHashMap<>();
        java.lang.String c1 = "c1";
        java.lang.String host_component = "HOST_COMPONENT";
        java.lang.String service_id = "HDFS";
        java.lang.String hostcomponent_id = "Namenode";
        java.lang.String host_name = "host1";
        properties.put(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_CLUSTER_NAME_PROPERTY_ID, c1);
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> filterSet = new java.util.HashSet<>();
        java.util.Map<java.lang.String, java.lang.Object> filterMap = new java.util.HashMap<>();
        filterMap.put(org.apache.ambari.server.controller.internal.RequestResourceProvider.SERVICE_ID, service_id);
        filterMap.put(org.apache.ambari.server.controller.internal.RequestResourceProvider.HOSTS_ID, host_name);
        filterSet.add(filterMap);
        properties.put(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_RESOURCE_FILTER_ID, filterSet);
        propertySet.add(properties);
        java.util.Map<java.lang.String, java.lang.String> requestInfoProperties = new java.util.HashMap<>();
        requestInfoProperties.put(org.apache.ambari.server.controller.internal.RequestResourceProvider.COMMAND_ID, "RESTART");
        requestInfoProperties.put(org.apache.ambari.server.controller.internal.RequestOperationLevel.OPERATION_LEVEL_ID, host_component);
        requestInfoProperties.put(org.apache.ambari.server.controller.internal.RequestOperationLevel.OPERATION_CLUSTER_ID, c1);
        requestInfoProperties.put(org.apache.ambari.server.controller.internal.RequestOperationLevel.OPERATION_SERVICE_ID, service_id);
        requestInfoProperties.put(org.apache.ambari.server.controller.internal.RequestOperationLevel.OPERATION_HOSTCOMPONENT_ID, hostcomponent_id);
        requestInfoProperties.put(org.apache.ambari.server.controller.internal.RequestOperationLevel.OPERATION_HOST_NAME, host_name);
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getCreateRequest(propertySet, requestInfoProperties);
        org.apache.ambari.server.controller.spi.ResourceProvider provider = org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.getResourceProvider(type, managementController);
        requestInfoProperties.put(org.apache.ambari.server.controller.internal.RequestOperationLevel.OPERATION_CLUSTER_ID, c1);
        provider.createResources(request);
        org.junit.Assert.assertTrue(actionRequest.hasCaptured());
        org.apache.ambari.server.controller.ExecuteActionRequest capturedRequest = actionRequest.getValue();
        org.apache.ambari.server.controller.internal.RequestOperationLevel level = capturedRequest.getOperationLevel();
        org.junit.Assert.assertEquals(level.getLevel().toString(), "HostComponent");
        org.junit.Assert.assertEquals(level.getClusterName(), c1);
        org.junit.Assert.assertEquals(level.getServiceName(), service_id);
        org.junit.Assert.assertEquals(level.getHostComponentName(), hostcomponent_id);
        org.junit.Assert.assertEquals(level.getHostName(), host_name);
    }

    @org.junit.Test
    public void testCreateResourcesCheckHostForNonClusterAsAdministrator() throws java.lang.Exception {
        testCreateResources(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator(), null, null, "check_host", java.util.EnumSet.of(org.apache.ambari.server.security.authorization.RoleAuthorization.HOST_ADD_DELETE_HOSTS));
    }

    public void testCreateResourcesCheckHostForNonClusterAsClusterAdministrator() throws java.lang.Exception {
        testCreateResources(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator(), null, null, "check_host", java.util.EnumSet.of(org.apache.ambari.server.security.authorization.RoleAuthorization.HOST_ADD_DELETE_HOSTS));
    }

    public void testCreateResourcesCheckHostForNonClusterAsClusterOperator() throws java.lang.Exception {
        testCreateResources(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterOperator(), null, null, "check_host", java.util.EnumSet.of(org.apache.ambari.server.security.authorization.RoleAuthorization.HOST_ADD_DELETE_HOSTS));
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testCreateResourcesCheckHostForNonClusterAsServiceAdministrator() throws java.lang.Exception {
        testCreateResources(org.apache.ambari.server.security.TestAuthenticationFactory.createServiceAdministrator(), null, null, "check_host", java.util.EnumSet.of(org.apache.ambari.server.security.authorization.RoleAuthorization.HOST_ADD_DELETE_HOSTS));
    }

    @org.junit.Test
    public void testCreateResourcesCheckHostForClusterAsAdministrator() throws java.lang.Exception {
        testCreateResources(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator(), "c1", null, "check_host", java.util.EnumSet.of(org.apache.ambari.server.security.authorization.RoleAuthorization.HOST_ADD_DELETE_HOSTS));
    }

    @org.junit.Test
    public void testCreateResourcesCheckHostForClusterAsClusterAdministrator() throws java.lang.Exception {
        testCreateResources(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator(), "c1", null, "check_host", java.util.EnumSet.of(org.apache.ambari.server.security.authorization.RoleAuthorization.HOST_ADD_DELETE_HOSTS));
    }

    @org.junit.Test
    public void testCreateResourcesCheckHostForClusterAsClusterOperator() throws java.lang.Exception {
        testCreateResources(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterOperator(), "c1", null, "check_host", java.util.EnumSet.of(org.apache.ambari.server.security.authorization.RoleAuthorization.HOST_ADD_DELETE_HOSTS));
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testCreateResourcesCheckHostForClusterAsServiceAdministrator() throws java.lang.Exception {
        testCreateResources(org.apache.ambari.server.security.TestAuthenticationFactory.createServiceAdministrator(), "c1", null, "check_host", java.util.EnumSet.of(org.apache.ambari.server.security.authorization.RoleAuthorization.HOST_ADD_DELETE_HOSTS));
    }

    @org.junit.Test
    public void testCreateResourcesServiceCheckForClusterAsAdministrator() throws java.lang.Exception {
        testCreateResources(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator(), "c1", "SOME_SERVICE_CHECK", null, null);
    }

    @org.junit.Test
    public void testCreateResourcesServiceCheckForClusterAsClusterAdministrator() throws java.lang.Exception {
        testCreateResources(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator(), "c1", "SOME_SERVICE_CHECK", null, null);
    }

    @org.junit.Test
    public void testCreateResourcesServiceCheckForClusterAsClusterOperator() throws java.lang.Exception {
        testCreateResources(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterOperator(), "c1", "SOME_SERVICE_CHECK", null, null);
    }

    @org.junit.Test
    public void testCreateResourcesServiceCheckForClusterAsServiceAdministrator() throws java.lang.Exception {
        testCreateResources(org.apache.ambari.server.security.TestAuthenticationFactory.createServiceAdministrator(), "c1", "SOME_SERVICE_CHECK", null, null);
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testCreateResourcesServiceCheckForClusterAsClusterUser() throws java.lang.Exception {
        testCreateResources(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterUser(), "c1", "SOME_SERVICE_CHECK", null, null);
    }

    @org.junit.Test
    public void testCreateResourcesDecommissionForClusterAsAdministrator() throws java.lang.Exception {
        testCreateResources(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator(), "c1", "SOME_SERVICE_CHECK", null, null);
    }

    @org.junit.Test
    public void testCreateResourcesDecommissionForClusterAsClusterAdministrator() throws java.lang.Exception {
        testCreateResources(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator(), "c1", "SOME_SERVICE_CHECK", null, null);
    }

    @org.junit.Test
    public void testCreateResourcesDecommissionForClusterAsClusterOperator() throws java.lang.Exception {
        testCreateResources(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterOperator(), "c1", "SOME_SERVICE_CHECK", null, null);
    }

    @org.junit.Test
    public void testCreateResourcesDecommissionForClusterAsServiceAdministrator() throws java.lang.Exception {
        testCreateResources(org.apache.ambari.server.security.TestAuthenticationFactory.createServiceAdministrator(), "c1", "SOME_SERVICE_CHECK", null, null);
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testCreateResourcesDecommissionForClusterAsClusterUser() throws java.lang.Exception {
        testCreateResources(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterUser(), "c1", "SOME_SERVICE_CHECK", null, null);
    }

    @org.junit.Test
    public void testCreateResourcesCustomActionNoPrivsForNonClusterAsAdministrator() throws java.lang.Exception {
        testCreateResources(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator(), null, null, "custom_action", null);
    }

    @org.junit.Test
    public void testCreateResourcesCustomActionNoPrivsForNonClusterAsClusterAdministrator() throws java.lang.Exception {
        testCreateResources(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator(), null, null, "custom_action", null);
    }

    @org.junit.Test
    public void testCreateResourcesCustomActionNoPrivsForNonClusterAsClusterOperator() throws java.lang.Exception {
        testCreateResources(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterOperator(), null, null, "custom_action", null);
    }

    @org.junit.Test
    public void testCreateResourcesForNonClusterAsServiceAdministrator() throws java.lang.Exception {
        testCreateResources(org.apache.ambari.server.security.TestAuthenticationFactory.createServiceAdministrator(), null, null, "custom_action", null);
    }

    private void testCreateResources(org.springframework.security.core.Authentication authentication, java.lang.String clusterName, java.lang.String commandName, java.lang.String actionName, java.util.Set<org.apache.ambari.server.security.authorization.RoleAuthorization> permissions) throws java.lang.Exception {
        org.apache.ambari.server.controller.spi.Resource.Type type = org.apache.ambari.server.controller.spi.Resource.Type.Request;
        org.easymock.Capture<org.apache.ambari.server.controller.ExecuteActionRequest> actionRequest = EasyMock.newCapture();
        org.easymock.Capture<java.util.HashMap<java.lang.String, java.lang.String>> propertyMap = EasyMock.newCapture();
        org.apache.ambari.server.controller.AmbariManagementController managementController = createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.api.services.AmbariMetaInfo metaInfo = createMock(org.apache.ambari.server.api.services.AmbariMetaInfo.class);
        org.apache.ambari.server.customactions.ActionDefinition actionDefinition = createMock(org.apache.ambari.server.customactions.ActionDefinition.class);
        org.apache.ambari.server.controller.RequestStatusResponse response = createNiceMock(org.apache.ambari.server.controller.RequestStatusResponse.class);
        EasyMock.expect(managementController.createAction(EasyMock.capture(actionRequest), EasyMock.capture(propertyMap))).andReturn(response).anyTimes();
        EasyMock.expect(managementController.getAmbariMetaInfo()).andReturn(metaInfo).anyTimes();
        EasyMock.expect(metaInfo.getActionDefinition(actionName)).andReturn(actionDefinition).anyTimes();
        EasyMock.expect(actionDefinition.getPermissions()).andReturn(permissions).anyTimes();
        EasyMock.expect(response.getMessage()).andReturn("Message").anyTimes();
        org.apache.ambari.server.state.Cluster cluster = createMock(org.apache.ambari.server.state.Cluster.class);
        org.apache.ambari.server.state.Clusters clusters = createMock(org.apache.ambari.server.state.Clusters.class);
        if (clusterName != null) {
            EasyMock.expect(cluster.getResourceId()).andReturn(4L).anyTimes();
            EasyMock.expect(clusters.getCluster(clusterName)).andReturn(cluster).anyTimes();
            EasyMock.expect(managementController.getClusters()).andReturn(clusters).anyTimes();
        }
        replay(managementController, metaInfo, actionDefinition, response, cluster, clusters);
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> propertySet = new java.util.LinkedHashSet<>();
        java.util.Map<java.lang.String, java.lang.Object> properties = new java.util.LinkedHashMap<>();
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> filterSet = new java.util.HashSet<>();
        java.util.Map<java.lang.String, java.lang.Object> filterMap = new java.util.HashMap<>();
        filterMap.put(org.apache.ambari.server.controller.internal.RequestResourceProvider.HOSTS_ID, "h1,h2");
        filterSet.add(filterMap);
        properties.put(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_RESOURCE_FILTER_ID, filterSet);
        properties.put(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_CLUSTER_NAME_PROPERTY_ID, clusterName);
        propertySet.add(properties);
        java.util.Map<java.lang.String, java.lang.String> requestInfoProperties = new java.util.HashMap<>();
        if (commandName != null) {
            requestInfoProperties.put(org.apache.ambari.server.controller.internal.RequestResourceProvider.COMMAND_ID, commandName);
        }
        if (actionName != null) {
            requestInfoProperties.put(org.apache.ambari.server.controller.internal.RequestResourceProvider.ACTION_ID, actionName);
        }
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getCreateRequest(propertySet, requestInfoProperties);
        org.apache.ambari.server.controller.spi.ResourceProvider provider = org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.getResourceProvider(type, managementController);
        provider.createResources(request);
        org.apache.ambari.server.controller.ExecuteActionRequest capturedRequest = actionRequest.getValue();
        org.junit.Assert.assertTrue(actionRequest.hasCaptured());
        if (actionName != null) {
            org.junit.Assert.assertFalse("expected an action", capturedRequest.isCommand());
            org.junit.Assert.assertEquals(actionName, capturedRequest.getActionName());
        }
        if (commandName != null) {
            org.junit.Assert.assertTrue("expected a command", capturedRequest.isCommand());
            org.junit.Assert.assertEquals(commandName, capturedRequest.getCommandName());
        }
        org.junit.Assert.assertNotNull(capturedRequest.getResourceFilters());
        org.junit.Assert.assertEquals(1, capturedRequest.getResourceFilters().size());
        org.apache.ambari.server.controller.internal.RequestResourceFilter capturedResourceFilter = capturedRequest.getResourceFilters().get(0);
        org.junit.Assert.assertEquals(null, capturedResourceFilter.getServiceName());
        org.junit.Assert.assertEquals(null, capturedResourceFilter.getComponentName());
        org.junit.Assert.assertNotNull(capturedResourceFilter.getHostNames());
        org.junit.Assert.assertEquals(2, capturedResourceFilter.getHostNames().size());
        org.junit.Assert.assertEquals(1, actionRequest.getValue().getParameters().size());
    }

    @org.junit.Test
    public void testGetResourcesWithoutCluster() throws java.lang.Exception {
        org.apache.ambari.server.controller.spi.Resource.Type type = org.apache.ambari.server.controller.spi.Resource.Type.Request;
        org.apache.ambari.server.controller.AmbariManagementController managementController = createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.actionmanager.ActionManager actionManager = createNiceMock(org.apache.ambari.server.actionmanager.ActionManager.class);
        org.apache.ambari.server.state.Clusters clusters = createNiceMock(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.orm.entities.RequestEntity requestMock = createNiceMock(org.apache.ambari.server.orm.entities.RequestEntity.class);
        EasyMock.expect(requestMock.getRequestContext()).andReturn("this is a context").anyTimes();
        EasyMock.expect(requestMock.getRequestId()).andReturn(100L).anyTimes();
        org.easymock.Capture<java.util.Collection<java.lang.Long>> requestIdsCapture = EasyMock.newCapture();
        EasyMock.expect(managementController.getActionManager()).andReturn(actionManager).anyTimes();
        EasyMock.expect(managementController.getClusters()).andReturn(clusters).anyTimes();
        EasyMock.expect(clusters.getCluster(EasyMock.anyObject(java.lang.String.class))).andReturn(null).anyTimes();
        EasyMock.expect(requestDAO.findByPks(EasyMock.capture(requestIdsCapture), EasyMock.eq(true))).andReturn(java.util.Collections.singletonList(requestMock));
        EasyMock.expect(hrcDAO.findAggregateCounts(((java.lang.Long) (EasyMock.anyObject())))).andReturn(new java.util.HashMap<java.lang.Long, org.apache.ambari.server.orm.dao.HostRoleCommandStatusSummaryDTO>() {
            {
                put(1L, org.apache.ambari.server.orm.dao.HostRoleCommandStatusSummaryDTO.create().inProgress(1));
            }
        }).anyTimes();
        prepareGetAuthorizationExpectations();
        replay(managementController, actionManager, clusters, requestMock, requestDAO, hrcDAO);
        org.apache.ambari.server.controller.spi.ResourceProvider provider = org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.getResourceProvider(type, managementController);
        java.util.Set<java.lang.String> propertyIds = new java.util.HashSet<>();
        propertyIds.add(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_ID_PROPERTY_ID);
        propertyIds.add(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_STATUS_PROPERTY_ID);
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_ID_PROPERTY_ID).equals("100").toPredicate();
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(propertyIds);
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = provider.getResources(request, predicate);
        org.junit.Assert.assertEquals(1, resources.size());
        for (org.apache.ambari.server.controller.spi.Resource resource : resources) {
            org.junit.Assert.assertEquals(100L, ((long) ((java.lang.Long) (resource.getPropertyValue(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_ID_PROPERTY_ID)))));
            org.junit.Assert.assertEquals("IN_PROGRESS", resource.getPropertyValue(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_STATUS_PROPERTY_ID));
            org.junit.Assert.assertNull(resource.getPropertyValue(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_CLUSTER_NAME_PROPERTY_ID));
        }
        verify(managementController, actionManager, clusters, requestMock, requestDAO, hrcDAO);
    }

    @org.junit.Test
    public void testRequestStatusWithNoTasks() throws java.lang.Exception {
        org.apache.ambari.server.controller.spi.Resource.Type type = org.apache.ambari.server.controller.spi.Resource.Type.Request;
        org.apache.ambari.server.controller.AmbariManagementController managementController = createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.actionmanager.ActionManager actionManager = createNiceMock(org.apache.ambari.server.actionmanager.ActionManager.class);
        org.apache.ambari.server.state.Clusters clusters = createNiceMock(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.orm.entities.RequestEntity requestMock = createNiceMock(org.apache.ambari.server.orm.entities.RequestEntity.class);
        EasyMock.expect(requestMock.getRequestContext()).andReturn("this is a context").anyTimes();
        EasyMock.expect(requestMock.getRequestId()).andReturn(100L).anyTimes();
        org.easymock.Capture<java.util.Collection<java.lang.Long>> requestIdsCapture = EasyMock.newCapture();
        EasyMock.expect(managementController.getActionManager()).andReturn(actionManager).anyTimes();
        EasyMock.expect(managementController.getClusters()).andReturn(clusters).anyTimes();
        EasyMock.expect(clusters.getCluster(EasyMock.anyObject(java.lang.String.class))).andReturn(null).anyTimes();
        EasyMock.expect(requestDAO.findByPks(EasyMock.capture(requestIdsCapture), EasyMock.eq(true))).andReturn(java.util.Collections.singletonList(requestMock));
        EasyMock.expect(hrcDAO.findAggregateCounts(((java.lang.Long) (EasyMock.anyObject())))).andReturn(java.util.Collections.emptyMap()).anyTimes();
        prepareGetAuthorizationExpectations();
        replay(managementController, actionManager, clusters, requestMock, requestDAO, hrcDAO);
        org.apache.ambari.server.controller.spi.ResourceProvider provider = org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.getResourceProvider(type, managementController);
        java.util.Set<java.lang.String> propertyIds = new java.util.HashSet<>();
        propertyIds.add(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_ID_PROPERTY_ID);
        propertyIds.add(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_STATUS_PROPERTY_ID);
        propertyIds.add(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_PROGRESS_PERCENT_ID);
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_ID_PROPERTY_ID).equals("100").toPredicate();
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(propertyIds);
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = provider.getResources(request, predicate);
        org.junit.Assert.assertEquals(1, resources.size());
        for (org.apache.ambari.server.controller.spi.Resource resource : resources) {
            org.junit.Assert.assertEquals(100L, ((long) ((java.lang.Long) (resource.getPropertyValue(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_ID_PROPERTY_ID)))));
            org.junit.Assert.assertEquals("COMPLETED", resource.getPropertyValue(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_STATUS_PROPERTY_ID));
            org.junit.Assert.assertEquals(100.0, resource.getPropertyValue(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_PROGRESS_PERCENT_ID));
        }
        verify(managementController, actionManager, clusters, requestMock, requestDAO, hrcDAO);
    }

    @org.junit.Test
    @org.powermock.core.classloader.annotations.PrepareForTest(org.apache.ambari.server.controller.AmbariServer.class)
    public void testGetLogicalRequestStatusWithNoTasks() throws java.lang.Exception {
        java.lang.Iterable<org.apache.ambari.server.controller.internal.CalculatedStatus> statusList = com.google.common.collect.ImmutableList.of(org.apache.ambari.server.controller.internal.CalculatedStatus.COMPLETED, org.apache.ambari.server.controller.internal.CalculatedStatus.PENDING, org.apache.ambari.server.controller.internal.CalculatedStatus.ABORTED);
        for (org.apache.ambari.server.controller.internal.CalculatedStatus calculatedStatus : statusList) {
            resetAll();
            org.powermock.api.easymock.PowerMock.mockStatic(org.apache.ambari.server.controller.AmbariServer.class);
            org.apache.ambari.server.controller.AmbariManagementController managementController = createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
            org.apache.ambari.server.actionmanager.ActionManager actionManager = createNiceMock(org.apache.ambari.server.actionmanager.ActionManager.class);
            org.apache.ambari.server.state.Clusters clusters = createNiceMock(org.apache.ambari.server.state.Clusters.class);
            org.apache.ambari.server.state.Cluster cluster = createNiceMock(org.apache.ambari.server.state.Cluster.class);
            org.apache.ambari.server.orm.entities.RequestEntity requestMock = createNiceMock(org.apache.ambari.server.orm.entities.RequestEntity.class);
            org.apache.ambari.server.topology.Blueprint blueprint = createNiceMock(org.apache.ambari.server.topology.Blueprint.class);
            org.apache.ambari.server.topology.ClusterTopology topology = createNiceMock(org.apache.ambari.server.topology.ClusterTopology.class);
            org.apache.ambari.server.topology.HostGroup hostGroup = createNiceMock(org.apache.ambari.server.topology.HostGroup.class);
            org.apache.ambari.server.topology.TopologyRequest topologyRequest = createNiceMock(org.apache.ambari.server.topology.TopologyRequest.class);
            org.apache.ambari.server.topology.LogicalRequest logicalRequest = createNiceMock(org.apache.ambari.server.topology.LogicalRequest.class);
            org.apache.ambari.server.topology.HostRequest hostRequest = createNiceMock(org.apache.ambari.server.topology.HostRequest.class);
            java.lang.Long requestId = 100L;
            java.lang.Long clusterId = 2L;
            java.lang.String clusterName = "cluster1";
            java.lang.String hostGroupName = "host_group_1";
            org.apache.ambari.server.topology.HostGroupInfo hostGroupInfo = new org.apache.ambari.server.topology.HostGroupInfo(hostGroupName);
            hostGroupInfo.setRequestedCount(1);
            java.util.Map<java.lang.String, org.apache.ambari.server.topology.HostGroupInfo> hostGroupInfoMap = com.google.common.collect.ImmutableMap.of(hostGroupName, hostGroupInfo);
            java.util.Collection<org.apache.ambari.server.topology.HostRequest> hostRequests = java.util.Collections.singletonList(hostRequest);
            java.util.Map<java.lang.Long, org.apache.ambari.server.orm.dao.HostRoleCommandStatusSummaryDTO> dtoMap = java.util.Collections.emptyMap();
            EasyMock.expect(org.apache.ambari.server.controller.AmbariServer.getController()).andReturn(managementController).anyTimes();
            EasyMock.expect(requestMock.getRequestContext()).andReturn("this is a context").anyTimes();
            EasyMock.expect(requestMock.getRequestId()).andReturn(requestId).anyTimes();
            EasyMock.expect(hostGroup.getName()).andReturn(hostGroupName).anyTimes();
            EasyMock.expect(blueprint.getHostGroup(hostGroupName)).andReturn(hostGroup).anyTimes();
            EasyMock.expect(topology.getClusterId()).andReturn(2L).anyTimes();
            EasyMock.expect(cluster.getClusterId()).andReturn(clusterId).anyTimes();
            EasyMock.expect(cluster.getClusterName()).andReturn(clusterName).anyTimes();
            EasyMock.expect(managementController.getActionManager()).andReturn(actionManager).anyTimes();
            EasyMock.expect(managementController.getClusters()).andReturn(clusters).anyTimes();
            EasyMock.expect(clusters.getCluster(EasyMock.eq(clusterName))).andReturn(cluster).anyTimes();
            EasyMock.expect(clusters.getClusterById(clusterId)).andReturn(cluster).anyTimes();
            java.util.Collection<java.lang.Long> requestIds = EasyMock.anyObject();
            EasyMock.expect(requestDAO.findByPks(requestIds, EasyMock.eq(true))).andReturn(com.google.common.collect.Lists.newArrayList(requestMock));
            EasyMock.expect(hrcDAO.findAggregateCounts(((java.lang.Long) (EasyMock.anyObject())))).andReturn(dtoMap).anyTimes();
            EasyMock.expect(topologyManager.getRequest(requestId)).andReturn(logicalRequest).anyTimes();
            EasyMock.expect(topologyManager.getRequests(EasyMock.eq(java.util.Collections.singletonList(requestId)))).andReturn(java.util.Collections.singletonList(logicalRequest)).anyTimes();
            EasyMock.expect(topologyManager.getStageSummaries(org.easymock.EasyMock.<java.lang.Long>anyObject())).andReturn(dtoMap).anyTimes();
            EasyMock.expect(topologyRequest.getHostGroupInfo()).andReturn(hostGroupInfoMap).anyTimes();
            EasyMock.expect(topology.getBlueprint()).andReturn(blueprint).anyTimes();
            EasyMock.expect(blueprint.shouldSkipFailure()).andReturn(true).anyTimes();
            EasyMock.expect(logicalRequest.getHostRequests()).andReturn(hostRequests).anyTimes();
            EasyMock.expect(logicalRequest.constructNewPersistenceEntity()).andReturn(requestMock).anyTimes();
            EasyMock.expect(logicalRequest.calculateStatus()).andReturn(calculatedStatus).anyTimes();
            com.google.common.base.Optional<java.lang.String> failureReason = (calculatedStatus == org.apache.ambari.server.controller.internal.CalculatedStatus.ABORTED) ? com.google.common.base.Optional.of("some reason") : com.google.common.base.Optional.<java.lang.String>absent();
            EasyMock.expect(logicalRequest.getFailureReason()).andReturn(failureReason).anyTimes();
            prepareGetAuthorizationExpectations();
            replayAll();
            org.apache.ambari.server.controller.spi.Resource.Type type = org.apache.ambari.server.controller.spi.Resource.Type.Request;
            org.apache.ambari.server.controller.spi.ResourceProvider provider = org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.getResourceProvider(type, managementController);
            java.util.Set<java.lang.String> propertyIds = com.google.common.collect.ImmutableSet.of(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_ID_PROPERTY_ID, org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_STATUS_PROPERTY_ID, org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_PROGRESS_PERCENT_ID, org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_CONTEXT_ID);
            org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_ID_PROPERTY_ID).equals("100").toPredicate();
            org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(propertyIds);
            java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = provider.getResources(request, predicate);
            verifyAll();
            org.junit.Assert.assertEquals(1, resources.size());
            org.apache.ambari.server.controller.spi.Resource resource = com.google.common.collect.Iterables.getOnlyElement(resources);
            org.junit.Assert.assertEquals(requestId, resource.getPropertyValue(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_ID_PROPERTY_ID));
            org.junit.Assert.assertEquals(calculatedStatus.getStatus().toString(), resource.getPropertyValue(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_STATUS_PROPERTY_ID));
            org.junit.Assert.assertEquals(calculatedStatus.getPercent(), resource.getPropertyValue(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_PROGRESS_PERCENT_ID));
            java.lang.Object requestContext = resource.getPropertyValue(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_CONTEXT_ID);
            org.junit.Assert.assertNotNull(requestContext);
            org.junit.Assert.assertTrue((!failureReason.isPresent()) || requestContext.toString().contains(failureReason.get()));
        }
    }
}