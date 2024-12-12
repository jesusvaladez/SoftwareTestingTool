package org.apache.ambari.server.controller.internal;
import com.google.inject.persist.Transactional;
import static org.easymock.EasyMock.anyLong;
import static org.easymock.EasyMock.anyString;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
public class UpgradeSummaryResourceProviderTest {
    private org.apache.ambari.server.orm.dao.HostDAO hostDAO;

    private org.apache.ambari.server.orm.dao.StackDAO stackDAO;

    private org.apache.ambari.server.orm.dao.RepositoryVersionDAO repoVersionDAO;

    private org.apache.ambari.server.orm.dao.UpgradeDAO upgradeDAO;

    private org.apache.ambari.server.orm.dao.RequestDAO requestDAO;

    private org.apache.ambari.server.orm.dao.StageDAO stageDAO;

    private org.apache.ambari.server.orm.dao.HostRoleCommandDAO hrcDAO;

    @com.google.inject.Inject
    private org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeHelper m_upgradeHelper;

    private com.google.inject.Injector injector;

    private org.apache.ambari.server.state.Clusters clusters;

    private org.apache.ambari.server.orm.OrmTestHelper helper;

    private org.apache.ambari.server.controller.AmbariManagementController amc;

    private java.lang.String clusterName = "c1";

    @org.junit.Before
    public void before() throws java.lang.Exception {
        m_upgradeHelper = EasyMock.createNiceMock(org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeHelper.class);
        injector = com.google.inject.Guice.createInjector(com.google.inject.util.Modules.override(new org.apache.ambari.server.orm.InMemoryDefaultTestModule()).with(new org.apache.ambari.server.controller.internal.UpgradeSummaryResourceProviderTest.MockModule()));
        injector.getInstance(org.apache.ambari.server.orm.GuiceJpaInitializer.class);
        helper = injector.getInstance(org.apache.ambari.server.orm.OrmTestHelper.class);
        amc = injector.getInstance(org.apache.ambari.server.controller.AmbariManagementController.class);
        java.lang.reflect.Field field = org.apache.ambari.server.controller.AmbariServer.class.getDeclaredField("clusterController");
        field.setAccessible(true);
        field.set(null, amc);
        hostDAO = injector.getInstance(org.apache.ambari.server.orm.dao.HostDAO.class);
        stackDAO = injector.getInstance(org.apache.ambari.server.orm.dao.StackDAO.class);
        repoVersionDAO = injector.getInstance(org.apache.ambari.server.orm.dao.RepositoryVersionDAO.class);
        upgradeDAO = injector.getInstance(org.apache.ambari.server.orm.dao.UpgradeDAO.class);
        requestDAO = injector.getInstance(org.apache.ambari.server.orm.dao.RequestDAO.class);
        stageDAO = injector.getInstance(org.apache.ambari.server.orm.dao.StageDAO.class);
        hrcDAO = injector.getInstance(org.apache.ambari.server.orm.dao.HostRoleCommandDAO.class);
    }

    @org.junit.After
    public void after() throws org.apache.ambari.server.AmbariException, java.sql.SQLException {
        org.apache.ambari.server.H2DatabaseCleaner.clearDatabaseAndStopPersistenceService(injector);
        injector = null;
    }

    public void createCluster() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.StackEntity stackEntity = stackDAO.find("HDP", "2.2.0");
        java.util.List<org.apache.ambari.server.orm.entities.RepoOsEntity> osRedhat6 = new java.util.ArrayList<>();
        org.apache.ambari.server.orm.entities.RepoOsEntity repoOsEntity = new org.apache.ambari.server.orm.entities.RepoOsEntity();
        repoOsEntity.setFamily("redhat6");
        repoOsEntity.setAmbariManaged(true);
        osRedhat6.add(repoOsEntity);
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repoVersionEntity = new org.apache.ambari.server.orm.entities.RepositoryVersionEntity();
        repoVersionEntity.setDisplayName("For Stack Version 2.2.0");
        repoVersionEntity.addRepoOsEntities(osRedhat6);
        repoVersionEntity.setStack(stackEntity);
        repoVersionEntity.setVersion("2.2.0.0");
        repoVersionDAO.create(repoVersionEntity);
        clusters = injector.getInstance(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.state.StackId stackId = new org.apache.ambari.server.state.StackId("HDP-2.2.0");
        clusters.addCluster(clusterName, stackId);
        org.apache.ambari.server.state.Cluster cluster = clusters.getCluster("c1");
        helper.getOrCreateRepositoryVersion(stackId, "2.2.0.1-1234");
        clusters.addHost("h1");
        org.apache.ambari.server.state.Host host = clusters.getHost("h1");
        java.util.Map<java.lang.String, java.lang.String> hostAttributes = new java.util.HashMap<>();
        hostAttributes.put("os_family", "redhat");
        hostAttributes.put("os_release_version", "6.4");
        host.setHostAttributes(hostAttributes);
        host.setState(org.apache.ambari.server.state.HostState.HEALTHY);
        clusters.mapHostToCluster("h1", "c1");
        org.apache.ambari.server.state.Service service = cluster.addService("ZOOKEEPER", repoVersionEntity);
        org.apache.ambari.server.state.ServiceComponent component = service.addServiceComponent("ZOOKEEPER_SERVER");
        org.apache.ambari.server.state.ServiceComponentHost sch = component.addServiceComponentHost("h1");
        sch.setVersion("2.2.0.0");
        component = service.addServiceComponent("ZOOKEEPER_CLIENT");
        sch = component.addServiceComponentHost("h1");
        sch.setVersion("2.2.0.0");
    }

    @com.google.inject.persist.Transactional
    void createCommands(org.apache.ambari.server.state.Cluster cluster, java.lang.Long upgradeRequestId, java.lang.Long stageId) {
        org.apache.ambari.server.orm.entities.HostEntity h1 = hostDAO.findByName("h1");
        org.apache.ambari.server.orm.entities.RequestEntity requestEntity = requestDAO.findByPK(upgradeRequestId);
        org.apache.ambari.server.orm.entities.StageEntity stageEntity = new org.apache.ambari.server.orm.entities.StageEntity();
        stageEntity.setRequest(requestEntity);
        stageEntity.setClusterId(cluster.getClusterId());
        stageEntity.setRequestId(upgradeRequestId);
        stageEntity.setStageId(stageId);
        requestEntity.setStages(java.util.Collections.singletonList(stageEntity));
        stageDAO.create(stageEntity);
        requestDAO.merge(requestEntity);
        org.apache.ambari.server.orm.entities.HostRoleCommandEntity hrc1 = new org.apache.ambari.server.orm.entities.HostRoleCommandEntity();
        hrc1.setStage(stageEntity);
        hrc1.setStatus(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED);
        hrc1.setRole(org.apache.ambari.server.Role.ZOOKEEPER_SERVER);
        hrc1.setRoleCommand(org.apache.ambari.server.RoleCommand.RESTART);
        hrc1.setHostEntity(h1);
        stageEntity.setHostRoleCommands(new java.util.ArrayList<>());
        stageEntity.getHostRoleCommands().add(hrc1);
        h1.getHostRoleCommandEntities().add(hrc1);
        hrcDAO.create(hrc1);
        hostDAO.merge(h1);
    }

    @org.junit.Test
    public void testGetUpgradeSummary() throws java.lang.Exception {
        createCluster();
        org.apache.ambari.server.state.Cluster cluster = clusters.getCluster(clusterName);
        org.apache.ambari.server.controller.spi.ResourceProvider upgradeSummaryResourceProvider = createProvider(amc);
        org.apache.ambari.server.controller.spi.Request requestResource = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest();
        org.apache.ambari.server.controller.spi.Predicate pBogus = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.UpgradeSummaryResourceProvider.UPGRADE_SUMMARY_CLUSTER_NAME).equals("bogus name").toPredicate();
        try {
            java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = upgradeSummaryResourceProvider.getResources(requestResource, pBogus);
            org.junit.Assert.assertTrue("Expected exception to be thrown", false);
        } catch (java.lang.Exception e) {
        }
        java.lang.Long upgradeRequestId = 1L;
        org.apache.ambari.server.controller.spi.Predicate p1 = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.UpgradeSummaryResourceProvider.UPGRADE_SUMMARY_CLUSTER_NAME).equals(clusterName).toPredicate();
        org.apache.ambari.server.controller.spi.Predicate p2 = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.UpgradeSummaryResourceProvider.UPGRADE_SUMMARY_REQUEST_ID).equals(upgradeRequestId.toString()).toPredicate();
        org.apache.ambari.server.controller.spi.Predicate p1And2 = new org.apache.ambari.server.controller.predicate.AndPredicate(p1, p2);
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = upgradeSummaryResourceProvider.getResources(requestResource, p1And2);
        org.junit.Assert.assertEquals(0, resources.size());
        org.apache.ambari.server.orm.entities.RequestEntity requestEntity = new org.apache.ambari.server.orm.entities.RequestEntity();
        requestEntity.setRequestId(1L);
        requestEntity.setClusterId(cluster.getClusterId());
        requestEntity.setStatus(org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING);
        requestEntity.setStages(new java.util.ArrayList<>());
        requestDAO.create(requestEntity);
        org.apache.ambari.server.orm.entities.UpgradeEntity upgrade = new org.apache.ambari.server.orm.entities.UpgradeEntity();
        upgrade.setRequestEntity(requestEntity);
        upgrade.setClusterId(cluster.getClusterId());
        upgrade.setId(1L);
        upgrade.setUpgradePackage("some-name");
        upgrade.setUpgradePackStackId(new org.apache.ambari.server.state.StackId(((java.lang.String) (null))));
        upgrade.setUpgradeType(org.apache.ambari.spi.upgrade.UpgradeType.ROLLING);
        upgrade.setDirection(org.apache.ambari.server.stack.upgrade.Direction.UPGRADE);
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion2201 = injector.getInstance(org.apache.ambari.server.orm.dao.RepositoryVersionDAO.class).findByStackNameAndVersion("HDP", "2.2.0.1-1234");
        upgrade.setRepositoryVersion(repositoryVersion2201);
        upgradeDAO.create(upgrade);
        org.apache.ambari.server.controller.spi.Resource r;
        resources = upgradeSummaryResourceProvider.getResources(requestResource, p1And2);
        org.junit.Assert.assertEquals(1, resources.size());
        r = resources.iterator().next();
        junit.framework.Assert.assertNull(r.getPropertyValue(org.apache.ambari.server.controller.internal.UpgradeSummaryResourceProvider.UPGRADE_SUMMARY_FAIL_REASON));
        java.lang.Long currentStageId = 1L;
        createCommands(cluster, upgradeRequestId, currentStageId);
        resources = upgradeSummaryResourceProvider.getResources(requestResource, p1And2);
        org.junit.Assert.assertEquals(1, resources.size());
        r = resources.iterator().next();
        junit.framework.Assert.assertNull(r.getPropertyValue(org.apache.ambari.server.controller.internal.UpgradeSummaryResourceProvider.UPGRADE_SUMMARY_FAIL_REASON));
        requestEntity = requestDAO.findByPK(upgradeRequestId);
        org.apache.ambari.server.orm.entities.HostEntity h1 = hostDAO.findByName("h1");
        org.apache.ambari.server.orm.entities.StageEntity nextStage = new org.apache.ambari.server.orm.entities.StageEntity();
        nextStage.setRequest(requestEntity);
        nextStage.setClusterId(cluster.getClusterId());
        nextStage.setRequestId(upgradeRequestId);
        nextStage.setStageId(++currentStageId);
        requestEntity.getStages().add(nextStage);
        stageDAO.create(nextStage);
        requestDAO.merge(requestEntity);
        org.apache.ambari.server.orm.entities.HostRoleCommandEntity hrc2 = new org.apache.ambari.server.orm.entities.HostRoleCommandEntity();
        hrc2.setStage(nextStage);
        hrc2.setStatus(org.apache.ambari.server.actionmanager.HostRoleStatus.FAILED);
        hrc2.setRole(org.apache.ambari.server.Role.ZOOKEEPER_SERVER);
        hrc2.setRoleCommand(org.apache.ambari.server.RoleCommand.RESTART);
        hrc2.setCommandDetail("Restart ZOOKEEPER_SERVER");
        hrc2.setHostEntity(h1);
        nextStage.setHostRoleCommands(new java.util.ArrayList<>());
        nextStage.getHostRoleCommands().add(hrc2);
        h1.getHostRoleCommandEntities().add(hrc2);
        hrcDAO.create(hrc2);
        hostDAO.merge(h1);
        hrc2.setRequestId(upgradeRequestId);
        hrc2.setStageId(nextStage.getStageId());
        hrcDAO.merge(hrc2);
        org.apache.ambari.server.controller.spi.Resource failedTask = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Task);
        EasyMock.expect(m_upgradeHelper.getTaskResource(EasyMock.anyString(), EasyMock.anyLong(), EasyMock.anyLong(), EasyMock.anyLong())).andReturn(failedTask).anyTimes();
        EasyMock.replay(m_upgradeHelper);
        resources = upgradeSummaryResourceProvider.getResources(requestResource, p1And2);
        org.junit.Assert.assertEquals(1, resources.size());
        r = resources.iterator().next();
        org.junit.Assert.assertEquals("Failed calling Restart ZOOKEEPER_SERVER on host h1", r.getPropertyValue(org.apache.ambari.server.controller.internal.UpgradeSummaryResourceProvider.UPGRADE_SUMMARY_FAIL_REASON));
    }

    private org.apache.ambari.server.controller.internal.UpgradeSummaryResourceProvider createProvider(org.apache.ambari.server.controller.AmbariManagementController amc) {
        return new org.apache.ambari.server.controller.internal.UpgradeSummaryResourceProvider(amc);
    }

    private class MockModule implements com.google.inject.Module {
        @java.lang.Override
        public void configure(com.google.inject.Binder binder) {
            binder.bind(org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeHelper.class).toInstance(m_upgradeHelper);
        }
    }
}