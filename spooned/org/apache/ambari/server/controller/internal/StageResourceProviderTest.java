package org.apache.ambari.server.controller.internal;
import org.easymock.EasyMock;
import static org.easymock.EasyMock.anyLong;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.createStrictMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.reset;
import static org.easymock.EasyMock.verify;
public class StageResourceProviderTest {
    private org.apache.ambari.server.orm.dao.StageDAO dao = null;

    private org.apache.ambari.server.state.Clusters clusters = null;

    private org.apache.ambari.server.state.Cluster cluster = null;

    private org.apache.ambari.server.controller.AmbariManagementController managementController = null;

    private com.google.inject.Injector injector;

    private org.apache.ambari.server.orm.dao.HostRoleCommandDAO hrcDao = null;

    private org.apache.ambari.server.topology.TopologyManager topologyManager = null;

    @org.junit.Before
    public void before() {
        dao = EasyMock.createStrictMock(org.apache.ambari.server.orm.dao.StageDAO.class);
        clusters = EasyMock.createStrictMock(org.apache.ambari.server.state.Clusters.class);
        cluster = EasyMock.createStrictMock(org.apache.ambari.server.state.Cluster.class);
        hrcDao = EasyMock.createStrictMock(org.apache.ambari.server.orm.dao.HostRoleCommandDAO.class);
        topologyManager = org.easymock.EasyMock.createNiceMock(org.apache.ambari.server.topology.TopologyManager.class);
        EasyMock.expect(topologyManager.getStages()).andReturn(new java.util.ArrayList<>()).atLeastOnce();
        EasyMock.expect(hrcDao.findAggregateCounts(org.easymock.EasyMock.anyObject(java.lang.Long.class))).andReturn(new java.util.HashMap<java.lang.Long, org.apache.ambari.server.orm.dao.HostRoleCommandStatusSummaryDTO>() {
            {
                put(0L, new org.apache.ambari.server.orm.dao.HostRoleCommandStatusSummaryDTO(0, 1000L, 2500L, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0));
            }
        }).anyTimes();
        EasyMock.replay(hrcDao, topologyManager);
        managementController = EasyMock.createNiceMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        injector = com.google.inject.Guice.createInjector(com.google.inject.util.Modules.override(new org.apache.ambari.server.orm.InMemoryDefaultTestModule()).with(new org.apache.ambari.server.controller.internal.StageResourceProviderTest.MockModule()));
        org.junit.Assert.assertNotNull(injector);
    }

    @org.junit.Test
    public void testCreateResources() throws java.lang.Exception {
        org.apache.ambari.server.controller.internal.StageResourceProvider provider = new org.apache.ambari.server.controller.internal.StageResourceProvider(managementController);
        org.apache.ambari.server.controller.spi.Request request = EasyMock.createNiceMock(org.apache.ambari.server.controller.spi.Request.class);
        try {
            provider.createResources(request);
            org.junit.Assert.fail("Expected UnsupportedOperationException");
        } catch (java.lang.UnsupportedOperationException e) {
        }
    }

    @org.junit.Test
    public void testUpdateResources() throws java.lang.Exception {
        org.apache.ambari.server.controller.internal.StageResourceProvider provider = new org.apache.ambari.server.controller.internal.StageResourceProvider(managementController);
        org.apache.ambari.server.controller.spi.Request request = EasyMock.createNiceMock(org.apache.ambari.server.controller.spi.Request.class);
        org.apache.ambari.server.controller.spi.Predicate predicate = EasyMock.createNiceMock(org.apache.ambari.server.controller.spi.Predicate.class);
        EasyMock.expect(clusters.getClusterById(EasyMock.anyLong())).andReturn(cluster).anyTimes();
        EasyMock.expect(request.getProperties()).andReturn(java.util.Collections.emptySet());
        EasyMock.replay(clusters, cluster, request, predicate);
        provider.updateResources(request, predicate);
        EasyMock.verify(clusters, cluster);
    }

    @org.junit.Test(expected = java.lang.UnsupportedOperationException.class)
    public void testDeleteResources() throws java.lang.Exception {
        org.apache.ambari.server.controller.internal.StageResourceProvider provider = new org.apache.ambari.server.controller.internal.StageResourceProvider(managementController);
        org.apache.ambari.server.controller.spi.Predicate predicate = EasyMock.createNiceMock(org.apache.ambari.server.controller.spi.Predicate.class);
        provider.deleteResources(new org.apache.ambari.server.controller.internal.RequestImpl(null, null, null, null), predicate);
    }

    @org.junit.Test
    public void testGetResources() throws java.lang.Exception {
        org.apache.ambari.server.controller.internal.StageResourceProvider provider = new org.apache.ambari.server.controller.internal.StageResourceProvider(managementController);
        org.apache.ambari.server.controller.spi.Request request = EasyMock.createNiceMock(org.apache.ambari.server.controller.spi.Request.class);
        org.apache.ambari.server.controller.spi.Predicate predicate = EasyMock.createNiceMock(org.apache.ambari.server.controller.spi.Predicate.class);
        java.util.List<org.apache.ambari.server.orm.entities.StageEntity> entities = getStageEntities(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED);
        EasyMock.expect(dao.findAll(request, predicate)).andReturn(entities);
        EasyMock.expect(clusters.getClusterById(EasyMock.anyLong())).andReturn(cluster).anyTimes();
        EasyMock.expect(cluster.getClusterName()).andReturn("c1").anyTimes();
        EasyMock.replay(dao, clusters, cluster, request, predicate);
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = provider.getResources(request, predicate);
        org.junit.Assert.assertEquals(1, resources.size());
        org.apache.ambari.server.controller.spi.Resource resource = resources.iterator().next();
        org.junit.Assert.assertEquals(100.0, resource.getPropertyValue(org.apache.ambari.server.controller.internal.StageResourceProvider.STAGE_PROGRESS_PERCENT));
        org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, resource.getPropertyValue(org.apache.ambari.server.controller.internal.StageResourceProvider.STAGE_STATUS));
        org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, resource.getPropertyValue(org.apache.ambari.server.controller.internal.StageResourceProvider.STAGE_DISPLAY_STATUS));
        org.junit.Assert.assertEquals(1000L, resource.getPropertyValue(org.apache.ambari.server.controller.internal.StageResourceProvider.STAGE_START_TIME));
        org.junit.Assert.assertEquals(2500L, resource.getPropertyValue(org.apache.ambari.server.controller.internal.StageResourceProvider.STAGE_END_TIME));
        EasyMock.verify(dao, clusters, cluster);
    }

    @org.junit.Test
    public void testGetResourcesWithRequest() throws java.lang.Exception {
        org.apache.ambari.server.controller.internal.StageResourceProvider provider = new org.apache.ambari.server.controller.internal.StageResourceProvider(managementController);
        org.apache.ambari.server.controller.spi.Request request = EasyMock.createNiceMock(org.apache.ambari.server.controller.spi.Request.class);
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.StageResourceProvider.STAGE_REQUEST_ID).equals(1L).toPredicate();
        java.util.List<org.apache.ambari.server.orm.entities.StageEntity> entities = getStageEntities(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED);
        EasyMock.expect(dao.findAll(request, predicate)).andReturn(entities);
        EasyMock.expect(clusters.getClusterById(EasyMock.anyLong())).andReturn(cluster).anyTimes();
        EasyMock.expect(cluster.getClusterName()).andReturn("c1").anyTimes();
        EasyMock.reset(topologyManager);
        EasyMock.expect(topologyManager.getRequest(org.easymock.EasyMock.anyLong())).andReturn(null).atLeastOnce();
        EasyMock.replay(topologyManager, dao, clusters, cluster, request);
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = provider.getResources(request, predicate);
        org.junit.Assert.assertEquals(1, resources.size());
        org.apache.ambari.server.controller.spi.Resource resource = resources.iterator().next();
        org.junit.Assert.assertEquals(100.0, resource.getPropertyValue(org.apache.ambari.server.controller.internal.StageResourceProvider.STAGE_PROGRESS_PERCENT));
        org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, resource.getPropertyValue(org.apache.ambari.server.controller.internal.StageResourceProvider.STAGE_STATUS));
        org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, resource.getPropertyValue(org.apache.ambari.server.controller.internal.StageResourceProvider.STAGE_DISPLAY_STATUS));
        org.junit.Assert.assertEquals(1000L, resource.getPropertyValue(org.apache.ambari.server.controller.internal.StageResourceProvider.STAGE_START_TIME));
        org.junit.Assert.assertEquals(2500L, resource.getPropertyValue(org.apache.ambari.server.controller.internal.StageResourceProvider.STAGE_END_TIME));
        EasyMock.verify(topologyManager, dao, clusters, cluster);
    }

    @org.junit.Test
    public void testGetDisplayStatus() throws java.lang.Exception {
        org.easymock.EasyMock.reset(hrcDao);
        EasyMock.expect(hrcDao.findAggregateCounts(org.easymock.EasyMock.anyObject(java.lang.Long.class))).andReturn(new java.util.HashMap<java.lang.Long, org.apache.ambari.server.orm.dao.HostRoleCommandStatusSummaryDTO>() {
            {
                put(0L, new org.apache.ambari.server.orm.dao.HostRoleCommandStatusSummaryDTO(0, 1000L, 2500L, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1));
            }
        }).anyTimes();
        EasyMock.replay(hrcDao);
        org.apache.ambari.server.controller.internal.StageResourceProvider provider = new org.apache.ambari.server.controller.internal.StageResourceProvider(managementController);
        org.apache.ambari.server.controller.spi.Request request = EasyMock.createNiceMock(org.apache.ambari.server.controller.spi.Request.class);
        org.apache.ambari.server.controller.spi.Predicate predicate = EasyMock.createNiceMock(org.apache.ambari.server.controller.spi.Predicate.class);
        java.util.List<org.apache.ambari.server.orm.entities.StageEntity> entities = getStageEntities(org.apache.ambari.server.actionmanager.HostRoleStatus.SKIPPED_FAILED);
        entities.get(0).setSkippable(true);
        EasyMock.expect(dao.findAll(request, predicate)).andReturn(entities);
        EasyMock.expect(clusters.getClusterById(EasyMock.anyLong())).andReturn(cluster).anyTimes();
        EasyMock.expect(cluster.getClusterName()).andReturn("c1").anyTimes();
        EasyMock.replay(dao, clusters, cluster, request, predicate);
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = provider.getResources(request, predicate);
        org.junit.Assert.assertEquals(1, resources.size());
        org.apache.ambari.server.controller.spi.Resource resource = resources.iterator().next();
        org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, resource.getPropertyValue(org.apache.ambari.server.controller.internal.StageResourceProvider.STAGE_STATUS));
        org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.HostRoleStatus.SKIPPED_FAILED, resource.getPropertyValue(org.apache.ambari.server.controller.internal.StageResourceProvider.STAGE_DISPLAY_STATUS));
        EasyMock.verify(dao, clusters, cluster);
    }

    @org.junit.Test
    @org.junit.Ignore
    public void testQueryForResources() throws java.lang.Exception {
        org.apache.ambari.server.controller.internal.StageResourceProvider provider = new org.apache.ambari.server.controller.internal.StageResourceProvider(managementController);
        org.apache.ambari.server.controller.spi.Request request = EasyMock.createNiceMock(org.apache.ambari.server.controller.spi.Request.class);
        org.apache.ambari.server.controller.spi.Predicate predicate = EasyMock.createNiceMock(org.apache.ambari.server.controller.spi.Predicate.class);
        java.util.List<org.apache.ambari.server.orm.entities.StageEntity> entities = getStageEntities(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED);
        EasyMock.expect(dao.findAll(request, predicate)).andReturn(entities);
        EasyMock.expect(clusters.getClusterById(EasyMock.anyLong())).andReturn(cluster).anyTimes();
        EasyMock.expect(cluster.getClusterName()).andReturn("c1").anyTimes();
        EasyMock.replay(dao, clusters, cluster, request, predicate);
        org.apache.ambari.server.controller.spi.QueryResponse response = provider.queryForResources(request, predicate);
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = response.getResources();
        org.junit.Assert.assertEquals(1, resources.size());
        org.junit.Assert.assertFalse(response.isSortedResponse());
        org.junit.Assert.assertFalse(response.isPagedResponse());
        org.junit.Assert.assertEquals(1, response.getTotalResourceCount());
        EasyMock.verify(dao, clusters, cluster);
    }

    @org.junit.Test
    public void testUpdateStageStatus_aborted() throws java.lang.Exception {
        org.apache.ambari.server.controller.internal.StageResourceProvider provider = new org.apache.ambari.server.controller.internal.StageResourceProvider(managementController);
        org.apache.ambari.server.actionmanager.ActionManager actionManager = EasyMock.createNiceMock(org.apache.ambari.server.actionmanager.ActionManager.class);
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.StageResourceProvider.STAGE_STAGE_ID).equals(2L).and().property(org.apache.ambari.server.controller.internal.StageResourceProvider.STAGE_REQUEST_ID).equals(1L).toPredicate();
        java.util.Map<java.lang.String, java.lang.Object> requestProps = new java.util.HashMap<>();
        requestProps.put(org.apache.ambari.server.controller.internal.StageResourceProvider.STAGE_STATUS, org.apache.ambari.server.actionmanager.HostRoleStatus.ABORTED.name());
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getUpdateRequest(requestProps, null);
        java.util.List<org.apache.ambari.server.orm.entities.StageEntity> entities = getStageEntities(org.apache.ambari.server.actionmanager.HostRoleStatus.HOLDING);
        EasyMock.expect(dao.findAll(request, predicate)).andReturn(entities);
        EasyMock.expect(managementController.getActionManager()).andReturn(actionManager).anyTimes();
        dao.updateStageStatus(entities.get(0), org.apache.ambari.server.actionmanager.HostRoleStatus.ABORTED, actionManager);
        org.easymock.EasyMock.expectLastCall().atLeastOnce();
        EasyMock.replay(dao, clusters, cluster, actionManager, managementController);
        provider.updateResources(request, predicate);
        EasyMock.verify(dao, clusters, cluster, actionManager, managementController);
    }

    private java.util.List<org.apache.ambari.server.orm.entities.StageEntity> getStageEntities(org.apache.ambari.server.actionmanager.HostRoleStatus lastTaskStatus) {
        org.apache.ambari.server.orm.entities.StageEntity stage = new org.apache.ambari.server.orm.entities.StageEntity();
        org.apache.ambari.server.orm.entities.HostRoleCommandEntity task1 = new org.apache.ambari.server.orm.entities.HostRoleCommandEntity();
        task1.setStatus(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED);
        task1.setStartTime(1000L);
        task1.setEndTime(2000L);
        org.apache.ambari.server.orm.entities.HostRoleCommandEntity task2 = new org.apache.ambari.server.orm.entities.HostRoleCommandEntity();
        task2.setStatus(lastTaskStatus);
        task2.setStartTime(1500L);
        task2.setEndTime(2500L);
        java.util.Collection<org.apache.ambari.server.orm.entities.HostRoleCommandEntity> tasks = new java.util.HashSet<>();
        tasks.add(task1);
        tasks.add(task2);
        stage.setHostRoleCommands(tasks);
        stage.setRequestId(1L);
        java.util.List<org.apache.ambari.server.orm.entities.StageEntity> entities = new java.util.LinkedList<>();
        entities.add(stage);
        return entities;
    }

    private class MockModule implements com.google.inject.Module {
        @java.lang.Override
        public void configure(com.google.inject.Binder binder) {
            binder.bind(org.apache.ambari.server.orm.dao.StageDAO.class).toInstance(dao);
            binder.bind(org.apache.ambari.server.state.Clusters.class).toInstance(clusters);
            binder.bind(org.apache.ambari.server.state.Cluster.class).toInstance(cluster);
            binder.bind(org.apache.ambari.server.orm.dao.HostRoleCommandDAO.class).toInstance(hrcDao);
            binder.bind(org.apache.ambari.server.controller.AmbariManagementController.class).toInstance(managementController);
            binder.bind(org.apache.ambari.server.metadata.ActionMetadata.class);
            binder.bind(org.apache.ambari.server.topology.TopologyManager.class).toInstance(topologyManager);
        }
    }
}