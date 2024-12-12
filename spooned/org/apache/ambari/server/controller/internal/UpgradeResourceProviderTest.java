package org.apache.ambari.server.controller.internal;
import org.apache.commons.collections.CollectionUtils;
import org.easymock.Capture;
import org.easymock.EasyMock;
import org.easymock.EasyMockSupport;
import org.springframework.security.core.context.SecurityContextHolder;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
public class UpgradeResourceProviderTest extends org.easymock.EasyMockSupport {
    private org.apache.ambari.server.orm.dao.UpgradeDAO upgradeDao = null;

    private org.apache.ambari.server.orm.dao.RequestDAO requestDao = null;

    private org.apache.ambari.server.orm.dao.RepositoryVersionDAO repoVersionDao = null;

    private com.google.inject.Injector injector;

    private org.apache.ambari.server.state.Clusters clusters;

    private org.apache.ambari.server.state.Cluster cluster;

    private org.apache.ambari.server.controller.AmbariManagementController amc;

    private org.apache.ambari.server.state.ConfigHelper configHelper;

    private org.apache.ambari.server.agent.stomp.AgentConfigsHolder agentConfigsHolder = createNiceMock(org.apache.ambari.server.agent.stomp.AgentConfigsHolder.class);

    private org.apache.ambari.server.orm.dao.StackDAO stackDAO;

    private org.apache.ambari.server.topology.TopologyManager topologyManager;

    private org.apache.ambari.server.state.ConfigFactory configFactory;

    private org.apache.ambari.server.orm.dao.HostRoleCommandDAO hrcDAO;

    private org.apache.ambari.server.controller.KerberosHelper kerberosHelperMock = createNiceMock(org.apache.ambari.server.controller.KerberosHelper.class);

    org.apache.ambari.server.orm.entities.RepositoryVersionEntity repoVersionEntity2110;

    org.apache.ambari.server.orm.entities.RepositoryVersionEntity repoVersionEntity2111;

    org.apache.ambari.server.orm.entities.RepositoryVersionEntity repoVersionEntity2112;

    org.apache.ambari.server.orm.entities.RepositoryVersionEntity repoVersionEntity2200;

    @org.junit.Before
    public void before() throws java.lang.Exception {
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator());
        configHelper = org.easymock.EasyMock.createNiceMock(org.apache.ambari.server.state.ConfigHelper.class);
        EasyMock.expect(configHelper.getPlaceholderValueFromDesiredConfigurations(org.easymock.EasyMock.anyObject(org.apache.ambari.server.state.Cluster.class), org.easymock.EasyMock.eq("{{foo/bar}}"))).andReturn("placeholder-rendered-properly").anyTimes();
        EasyMock.expect(configHelper.getDefaultProperties(org.easymock.EasyMock.anyObject(org.apache.ambari.server.state.StackId.class), org.easymock.EasyMock.anyString())).andReturn(new java.util.HashMap<>()).anyTimes();
        EasyMock.expect(configHelper.getChangedConfigTypes(org.easymock.EasyMock.anyObject(org.apache.ambari.server.state.Cluster.class), org.easymock.EasyMock.anyObject(org.apache.ambari.server.orm.entities.ServiceConfigEntity.class), org.easymock.EasyMock.anyLong(), org.easymock.EasyMock.anyLong(), org.easymock.EasyMock.anyString())).andReturn(java.util.Collections.emptyMap()).anyTimes();
        EasyMock.replay(configHelper);
        org.apache.ambari.server.orm.InMemoryDefaultTestModule module = new org.apache.ambari.server.orm.InMemoryDefaultTestModule();
        injector = com.google.inject.Guice.createInjector(com.google.inject.util.Modules.override(module).with(new org.apache.ambari.server.controller.internal.UpgradeResourceProviderTest.MockModule()));
        org.apache.ambari.server.H2DatabaseCleaner.resetSequences(injector);
        injector.getInstance(org.apache.ambari.server.orm.GuiceJpaInitializer.class);
        amc = injector.getInstance(org.apache.ambari.server.controller.AmbariManagementController.class);
        configFactory = injector.getInstance(org.apache.ambari.server.state.ConfigFactory.class);
        java.lang.reflect.Field field = org.apache.ambari.server.controller.AmbariServer.class.getDeclaredField("clusterController");
        field.setAccessible(true);
        field.set(null, amc);
        stackDAO = injector.getInstance(org.apache.ambari.server.orm.dao.StackDAO.class);
        upgradeDao = injector.getInstance(org.apache.ambari.server.orm.dao.UpgradeDAO.class);
        requestDao = injector.getInstance(org.apache.ambari.server.orm.dao.RequestDAO.class);
        repoVersionDao = injector.getInstance(org.apache.ambari.server.orm.dao.RepositoryVersionDAO.class);
        hrcDAO = injector.getInstance(org.apache.ambari.server.orm.dao.HostRoleCommandDAO.class);
        org.apache.ambari.server.events.publishers.AmbariEventPublisher publisher = org.easymock.EasyMock.createNiceMock(org.apache.ambari.server.events.publishers.AmbariEventPublisher.class);
        EasyMock.replay(publisher);
        org.apache.ambari.server.view.ViewRegistry.initInstance(new org.apache.ambari.server.view.ViewRegistry(publisher));
        org.apache.ambari.server.orm.entities.StackEntity stackEntity211 = stackDAO.find("HDP", "2.1.1");
        org.apache.ambari.server.orm.entities.StackEntity stackEntity220 = stackDAO.find("HDP", "2.2.0");
        org.apache.ambari.server.state.StackId stack211 = new org.apache.ambari.server.state.StackId(stackEntity211);
        repoVersionEntity2110 = new org.apache.ambari.server.orm.entities.RepositoryVersionEntity();
        repoVersionEntity2110.setDisplayName("My New Version 1");
        repoVersionEntity2110.addRepoOsEntities(createTestOperatingSystems());
        repoVersionEntity2110.setStack(stackEntity211);
        repoVersionEntity2110.setVersion("2.1.1.0");
        repoVersionDao.create(repoVersionEntity2110);
        repoVersionEntity2111 = new org.apache.ambari.server.orm.entities.RepositoryVersionEntity();
        repoVersionEntity2111.setDisplayName("My New Version 2 for minor upgrade");
        repoVersionEntity2111.addRepoOsEntities(createTestOperatingSystems());
        repoVersionEntity2111.setStack(stackEntity211);
        repoVersionEntity2111.setVersion("2.1.1.1");
        repoVersionDao.create(repoVersionEntity2111);
        repoVersionEntity2112 = new org.apache.ambari.server.orm.entities.RepositoryVersionEntity();
        repoVersionEntity2112.setDisplayName("My New Version 3 for patch upgrade");
        repoVersionEntity2112.addRepoOsEntities(createTestOperatingSystems());
        repoVersionEntity2112.setStack(stackEntity211);
        repoVersionEntity2112.setVersion("2.1.1.2");
        repoVersionEntity2112.setType(org.apache.ambari.spi.RepositoryType.PATCH);
        repoVersionEntity2112.setVersionXml("");
        repoVersionDao.create(repoVersionEntity2112);
        repoVersionEntity2200 = new org.apache.ambari.server.orm.entities.RepositoryVersionEntity();
        repoVersionEntity2200.setDisplayName("My New Version 4 for major upgrade");
        repoVersionEntity2200.addRepoOsEntities(createTestOperatingSystems());
        repoVersionEntity2200.setStack(stackEntity220);
        repoVersionEntity2200.setVersion("2.2.0.0");
        repoVersionDao.create(repoVersionEntity2200);
        clusters = injector.getInstance(org.apache.ambari.server.state.Clusters.class);
        clusters.addCluster("c1", stack211);
        cluster = clusters.getCluster("c1");
        clusters.addHost("h1");
        org.apache.ambari.server.state.Host host = clusters.getHost("h1");
        java.util.Map<java.lang.String, java.lang.String> hostAttributes = new java.util.HashMap<>();
        hostAttributes.put("os_family", "redhat");
        hostAttributes.put("os_release_version", "6.3");
        host.setHostAttributes(hostAttributes);
        host.setState(org.apache.ambari.server.state.HostState.HEALTHY);
        clusters.mapHostToCluster("h1", "c1");
        org.apache.ambari.server.state.Service service = cluster.addService("ZOOKEEPER", repoVersionEntity2110);
        org.apache.ambari.server.state.ServiceComponent component = service.addServiceComponent("ZOOKEEPER_SERVER");
        org.apache.ambari.server.state.ServiceComponentHost sch = component.addServiceComponentHost("h1");
        sch.setVersion("2.1.1.0");
        component = service.addServiceComponent("ZOOKEEPER_CLIENT");
        sch = component.addServiceComponentHost("h1");
        sch.setVersion("2.1.1.0");
        org.apache.ambari.server.configuration.Configuration configuration = injector.getInstance(org.apache.ambari.server.configuration.Configuration.class);
        configuration.setProperty("upgrade.parameter.zk-server.timeout", "824");
        topologyManager = injector.getInstance(org.apache.ambari.server.topology.TopologyManager.class);
        org.apache.ambari.server.utils.StageUtils.setTopologyManager(topologyManager);
        org.apache.ambari.server.utils.StageUtils.setConfiguration(configuration);
        org.apache.ambari.server.actionmanager.ActionManager.setTopologyManager(topologyManager);
        org.easymock.EasyMock.replay(injector.getInstance(org.apache.ambari.server.audit.AuditLogger.class));
    }

    private java.util.List<org.apache.ambari.server.orm.entities.RepoOsEntity> createTestOperatingSystems() {
        java.util.List<org.apache.ambari.server.orm.entities.RepoOsEntity> operatingSystems = new java.util.ArrayList<>();
        org.apache.ambari.server.orm.entities.RepoDefinitionEntity repoDefinitionEntity1 = new org.apache.ambari.server.orm.entities.RepoDefinitionEntity();
        repoDefinitionEntity1.setRepoID("HDP-UTILS");
        repoDefinitionEntity1.setBaseUrl("");
        repoDefinitionEntity1.setRepoName("HDP-UTILS");
        org.apache.ambari.server.orm.entities.RepoDefinitionEntity repoDefinitionEntity2 = new org.apache.ambari.server.orm.entities.RepoDefinitionEntity();
        repoDefinitionEntity2.setRepoID("HDP");
        repoDefinitionEntity2.setBaseUrl("");
        repoDefinitionEntity2.setRepoName("HDP");
        org.apache.ambari.server.orm.entities.RepoOsEntity repoOsEntity = new org.apache.ambari.server.orm.entities.RepoOsEntity();
        repoOsEntity.setFamily("redhat6");
        repoOsEntity.setAmbariManaged(true);
        repoOsEntity.addRepoDefinition(repoDefinitionEntity1);
        repoOsEntity.addRepoDefinition(repoDefinitionEntity2);
        operatingSystems.add(repoOsEntity);
        return operatingSystems;
    }

    @org.junit.After
    public void after() throws org.apache.ambari.server.AmbariException, java.sql.SQLException {
        org.apache.ambari.server.H2DatabaseCleaner.clearDatabaseAndStopPersistenceService(injector);
        org.easymock.EasyMock.reset(injector.getInstance(org.apache.ambari.server.audit.AuditLogger.class));
        injector = null;
    }

    private long getRequestId(org.apache.ambari.server.controller.spi.RequestStatus requestStatus) {
        org.junit.Assert.assertEquals(1, requestStatus.getAssociatedResources().size());
        org.apache.ambari.server.controller.spi.Resource r = requestStatus.getAssociatedResources().iterator().next();
        java.lang.String id = r.getPropertyValue("Upgrade/request_id").toString();
        return java.lang.Long.parseLong(id);
    }

    @org.junit.Test
    public void testCreateResourcesWithAutoSkipFailures() throws java.lang.Exception {
        org.apache.ambari.server.state.Cluster cluster = clusters.getCluster("c1");
        java.util.Map<java.lang.String, java.lang.Object> requestProps = new java.util.HashMap<>();
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_CLUSTER_NAME, "c1");
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_REPO_VERSION_ID, java.lang.String.valueOf(repoVersionEntity2200.getId()));
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_PACK, "upgrade_test");
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_TYPE, org.apache.ambari.spi.upgrade.UpgradeType.ROLLING.toString());
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_SKIP_FAILURES, java.lang.Boolean.TRUE.toString());
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_SKIP_SC_FAILURES, java.lang.Boolean.TRUE.toString());
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_SKIP_MANUAL_VERIFICATION, java.lang.Boolean.FALSE.toString());
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_SKIP_PREREQUISITE_CHECKS, java.lang.Boolean.TRUE.toString());
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_DIRECTION, org.apache.ambari.server.stack.upgrade.Direction.UPGRADE.name());
        org.apache.ambari.server.controller.spi.ResourceProvider upgradeResourceProvider = createProvider(amc);
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getCreateRequest(java.util.Collections.singleton(requestProps), null);
        upgradeResourceProvider.createResources(request);
        java.util.List<org.apache.ambari.server.orm.entities.UpgradeEntity> upgrades = upgradeDao.findUpgrades(cluster.getClusterId());
        org.junit.Assert.assertEquals(1, upgrades.size());
        org.apache.ambari.server.orm.entities.UpgradeEntity entity = upgrades.get(0);
        org.junit.Assert.assertEquals(cluster.getClusterId(), entity.getClusterId().longValue());
        java.util.List<org.apache.ambari.server.orm.entities.UpgradeGroupEntity> upgradeGroups = entity.getUpgradeGroups();
        org.junit.Assert.assertEquals(3, upgradeGroups.size());
        org.apache.ambari.server.orm.entities.UpgradeGroupEntity preClusterGroup = upgradeGroups.get(0);
        org.junit.Assert.assertEquals("PRE_CLUSTER", preClusterGroup.getName());
        java.util.List<org.apache.ambari.server.orm.entities.UpgradeItemEntity> preClusterUpgradeItems = preClusterGroup.getItems();
        org.junit.Assert.assertEquals(2, preClusterUpgradeItems.size());
        org.junit.Assert.assertEquals("Foo", parseSingleMessage(preClusterUpgradeItems.get(0).getText()));
        org.junit.Assert.assertEquals("Foo", parseSingleMessage(preClusterUpgradeItems.get(1).getText()));
        org.apache.ambari.server.orm.entities.UpgradeGroupEntity zookeeperGroup = upgradeGroups.get(1);
        org.junit.Assert.assertEquals("ZOOKEEPER", zookeeperGroup.getName());
        java.util.List<org.apache.ambari.server.orm.entities.UpgradeItemEntity> zookeeperUpgradeItems = zookeeperGroup.getItems();
        org.junit.Assert.assertEquals(5, zookeeperUpgradeItems.size());
        org.junit.Assert.assertEquals("This is a manual task with a placeholder of placeholder-rendered-properly", parseSingleMessage(zookeeperUpgradeItems.get(0).getText()));
        org.junit.Assert.assertEquals("Restarting ZooKeeper Server on h1", zookeeperUpgradeItems.get(1).getText());
        org.junit.Assert.assertEquals("Updating configuration zookeeper-newconfig", zookeeperUpgradeItems.get(2).getText());
        org.junit.Assert.assertEquals("Service Check ZooKeeper", zookeeperUpgradeItems.get(3).getText());
        org.junit.Assert.assertTrue(zookeeperUpgradeItems.get(4).getText().contains("There are failures that were automatically skipped"));
        org.apache.ambari.server.orm.entities.UpgradeItemEntity skippedFailureCheck = zookeeperUpgradeItems.get(zookeeperUpgradeItems.size() - 1);
        skippedFailureCheck.getTasks().contains(org.apache.ambari.server.serveraction.upgrades.AutoSkipFailedSummaryAction.class.getName());
        org.apache.ambari.server.orm.entities.UpgradeGroupEntity postClusterGroup = upgradeGroups.get(2);
        org.junit.Assert.assertEquals("POST_CLUSTER", postClusterGroup.getName());
        java.util.List<org.apache.ambari.server.orm.entities.UpgradeItemEntity> postClusterUpgradeItems = postClusterGroup.getItems();
        org.junit.Assert.assertEquals(2, postClusterUpgradeItems.size());
        org.junit.Assert.assertEquals("Please confirm you are ready to finalize", parseSingleMessage(postClusterUpgradeItems.get(0).getText()));
        org.junit.Assert.assertEquals("Save Cluster State", postClusterUpgradeItems.get(1).getText());
    }

    @org.junit.Test
    public void testCreateResourcesWithAutoSkipManualVerification() throws java.lang.Exception {
        org.apache.ambari.server.state.Cluster cluster = clusters.getCluster("c1");
        java.util.Map<java.lang.String, java.lang.Object> requestProps = new java.util.HashMap<>();
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_CLUSTER_NAME, "c1");
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_REPO_VERSION_ID, java.lang.String.valueOf(repoVersionEntity2200.getId()));
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_PACK, "upgrade_test");
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_TYPE, org.apache.ambari.spi.upgrade.UpgradeType.ROLLING.toString());
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_SKIP_MANUAL_VERIFICATION, java.lang.Boolean.TRUE.toString());
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_SKIP_PREREQUISITE_CHECKS, java.lang.Boolean.TRUE.toString());
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_DIRECTION, org.apache.ambari.server.stack.upgrade.Direction.UPGRADE.name());
        org.apache.ambari.server.controller.spi.ResourceProvider upgradeResourceProvider = createProvider(amc);
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getCreateRequest(java.util.Collections.singleton(requestProps), null);
        upgradeResourceProvider.createResources(request);
        java.util.List<org.apache.ambari.server.orm.entities.UpgradeEntity> upgrades = upgradeDao.findUpgrades(cluster.getClusterId());
        org.junit.Assert.assertEquals(1, upgrades.size());
        org.apache.ambari.server.orm.entities.UpgradeEntity entity = upgrades.get(0);
        org.junit.Assert.assertEquals(cluster.getClusterId(), entity.getClusterId().longValue());
        java.util.List<org.apache.ambari.server.orm.entities.UpgradeGroupEntity> upgradeGroups = entity.getUpgradeGroups();
        org.junit.Assert.assertEquals(2, upgradeGroups.size());
        org.apache.ambari.server.orm.entities.UpgradeGroupEntity zookeeperGroup = upgradeGroups.get(0);
        org.junit.Assert.assertEquals("ZOOKEEPER", zookeeperGroup.getName());
        java.util.List<org.apache.ambari.server.orm.entities.UpgradeItemEntity> zookeeperUpgradeItems = zookeeperGroup.getItems();
        org.junit.Assert.assertEquals(3, zookeeperUpgradeItems.size());
        org.junit.Assert.assertEquals("Restarting ZooKeeper Server on h1", zookeeperUpgradeItems.get(0).getText());
        org.junit.Assert.assertEquals("Updating configuration zookeeper-newconfig", zookeeperUpgradeItems.get(1).getText());
        org.junit.Assert.assertEquals("Service Check ZooKeeper", zookeeperUpgradeItems.get(2).getText());
        org.apache.ambari.server.orm.entities.UpgradeGroupEntity postClusterGroup = upgradeGroups.get(1);
        org.junit.Assert.assertEquals("POST_CLUSTER", postClusterGroup.getName());
        java.util.List<org.apache.ambari.server.orm.entities.UpgradeItemEntity> postClusterUpgradeItems = postClusterGroup.getItems();
        org.junit.Assert.assertEquals(1, postClusterUpgradeItems.size());
        org.junit.Assert.assertEquals("Save Cluster State", postClusterUpgradeItems.get(0).getText());
    }

    @org.junit.Test
    public void testCreateResourcesWithAutoSkipAll() throws java.lang.Exception {
        org.apache.ambari.server.state.Cluster cluster = clusters.getCluster("c1");
        java.util.Map<java.lang.String, java.lang.Object> requestProps = new java.util.HashMap<>();
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_CLUSTER_NAME, "c1");
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_REPO_VERSION_ID, java.lang.String.valueOf(repoVersionEntity2200.getId()));
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_PACK, "upgrade_test");
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_TYPE, org.apache.ambari.spi.upgrade.UpgradeType.ROLLING.toString());
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_SKIP_FAILURES, java.lang.Boolean.TRUE.toString());
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_SKIP_SC_FAILURES, java.lang.Boolean.TRUE.toString());
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_SKIP_MANUAL_VERIFICATION, java.lang.Boolean.TRUE.toString());
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_SKIP_PREREQUISITE_CHECKS, java.lang.Boolean.TRUE.toString());
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_DIRECTION, org.apache.ambari.server.stack.upgrade.Direction.UPGRADE.name());
        org.apache.ambari.server.controller.spi.ResourceProvider upgradeResourceProvider = createProvider(amc);
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getCreateRequest(java.util.Collections.singleton(requestProps), null);
        upgradeResourceProvider.createResources(request);
        java.util.List<org.apache.ambari.server.orm.entities.UpgradeEntity> upgrades = upgradeDao.findUpgrades(cluster.getClusterId());
        org.junit.Assert.assertEquals(1, upgrades.size());
        org.apache.ambari.server.orm.entities.UpgradeEntity entity = upgrades.get(0);
        org.junit.Assert.assertEquals(cluster.getClusterId(), entity.getClusterId().longValue());
        java.util.List<org.apache.ambari.server.orm.entities.UpgradeGroupEntity> upgradeGroups = entity.getUpgradeGroups();
        org.junit.Assert.assertEquals(2, upgradeGroups.size());
        org.apache.ambari.server.orm.entities.UpgradeGroupEntity zookeeperGroup = upgradeGroups.get(0);
        org.junit.Assert.assertEquals("ZOOKEEPER", zookeeperGroup.getName());
        java.util.List<org.apache.ambari.server.orm.entities.UpgradeItemEntity> zookeeperUpgradeItems = zookeeperGroup.getItems();
        org.junit.Assert.assertEquals(4, zookeeperUpgradeItems.size());
        org.junit.Assert.assertEquals("Restarting ZooKeeper Server on h1", zookeeperUpgradeItems.get(0).getText());
        org.junit.Assert.assertEquals("Updating configuration zookeeper-newconfig", zookeeperUpgradeItems.get(1).getText());
        org.junit.Assert.assertEquals("Service Check ZooKeeper", zookeeperUpgradeItems.get(2).getText());
        org.junit.Assert.assertTrue(zookeeperUpgradeItems.get(3).getText().contains("There are failures that were automatically skipped"));
        org.apache.ambari.server.orm.entities.UpgradeItemEntity skippedFailureCheck = zookeeperUpgradeItems.get(zookeeperUpgradeItems.size() - 1);
        skippedFailureCheck.getTasks().contains(org.apache.ambari.server.serveraction.upgrades.AutoSkipFailedSummaryAction.class.getName());
        org.apache.ambari.server.orm.entities.UpgradeGroupEntity postClusterGroup = upgradeGroups.get(1);
        org.junit.Assert.assertEquals("POST_CLUSTER", postClusterGroup.getName());
        java.util.List<org.apache.ambari.server.orm.entities.UpgradeItemEntity> postClusterUpgradeItems = postClusterGroup.getItems();
        org.junit.Assert.assertEquals(1, postClusterUpgradeItems.size());
        org.junit.Assert.assertEquals("Save Cluster State", postClusterUpgradeItems.get(0).getText());
    }

    @org.junit.Test
    public void testGetResources() throws java.lang.Exception {
        org.apache.ambari.server.controller.spi.RequestStatus status = testCreateResources();
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> createdResources = status.getAssociatedResources();
        org.junit.Assert.assertEquals(1, createdResources.size());
        org.apache.ambari.server.controller.spi.Resource res = createdResources.iterator().next();
        java.lang.Long id = ((java.lang.Long) (res.getPropertyValue("Upgrade/request_id")));
        org.junit.Assert.assertNotNull(id);
        org.junit.Assert.assertEquals(java.lang.Long.valueOf(1), id);
        java.util.Set<java.lang.String> propertyIds = new java.util.HashSet<>();
        propertyIds.add("Upgrade");
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_REQUEST_ID).equals("1").and().property(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_CLUSTER_NAME).equals("c1").toPredicate();
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(propertyIds);
        org.apache.ambari.server.controller.spi.ResourceProvider upgradeResourceProvider = createProvider(amc);
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = upgradeResourceProvider.getResources(request, predicate);
        org.junit.Assert.assertEquals(1, resources.size());
        res = resources.iterator().next();
        org.junit.Assert.assertNotNull(res.getPropertyValue("Upgrade/progress_percent"));
        org.junit.Assert.assertNotNull(res.getPropertyValue(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_DIRECTION));
        org.junit.Assert.assertEquals(org.apache.ambari.server.stack.upgrade.Direction.UPGRADE, res.getPropertyValue(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_DIRECTION));
        org.junit.Assert.assertEquals(false, res.getPropertyValue(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_SKIP_FAILURES));
        org.junit.Assert.assertEquals(false, res.getPropertyValue(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_SKIP_SC_FAILURES));
        org.junit.Assert.assertEquals(org.apache.ambari.spi.upgrade.UpgradeType.ROLLING, res.getPropertyValue(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_TYPE));
        propertyIds.clear();
        propertyIds.add("UpgradeGroup");
        predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.UpgradeGroupResourceProvider.UPGRADE_REQUEST_ID).equals("1").and().property(org.apache.ambari.server.controller.internal.UpgradeGroupResourceProvider.UPGRADE_CLUSTER_NAME).equals("c1").toPredicate();
        request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(propertyIds);
        org.apache.ambari.server.controller.spi.ResourceProvider upgradeGroupResourceProvider = new org.apache.ambari.server.controller.internal.UpgradeGroupResourceProvider(amc);
        resources = upgradeGroupResourceProvider.getResources(request, predicate);
        org.junit.Assert.assertEquals(3, resources.size());
        res = resources.iterator().next();
        org.junit.Assert.assertNotNull(res.getPropertyValue("UpgradeGroup/status"));
        org.junit.Assert.assertNotNull(res.getPropertyValue("UpgradeGroup/group_id"));
        org.junit.Assert.assertNotNull(res.getPropertyValue("UpgradeGroup/total_task_count"));
        org.junit.Assert.assertNotNull(res.getPropertyValue("UpgradeGroup/in_progress_task_count"));
        org.junit.Assert.assertNotNull(res.getPropertyValue("UpgradeGroup/completed_task_count"));
        propertyIds.clear();
        propertyIds.add("UpgradeItem");
        predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.UpgradeItemResourceProvider.UPGRADE_GROUP_ID).equals("1").and().property(org.apache.ambari.server.controller.internal.UpgradeItemResourceProvider.UPGRADE_REQUEST_ID).equals("1").and().property(org.apache.ambari.server.controller.internal.UpgradeItemResourceProvider.UPGRADE_CLUSTER_NAME).equals("c1").toPredicate();
        request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(propertyIds);
        org.apache.ambari.server.controller.spi.ResourceProvider upgradeItemResourceProvider = new org.apache.ambari.server.controller.internal.UpgradeItemResourceProvider(amc);
        resources = upgradeItemResourceProvider.getResources(request, predicate);
        org.junit.Assert.assertEquals(2, resources.size());
        res = resources.iterator().next();
        org.junit.Assert.assertNotNull(res.getPropertyValue("UpgradeItem/status"));
        propertyIds.clear();
        propertyIds.add("UpgradeItem");
        predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.UpgradeItemResourceProvider.UPGRADE_GROUP_ID).equals("3").and().property(org.apache.ambari.server.controller.internal.UpgradeItemResourceProvider.UPGRADE_REQUEST_ID).equals("1").and().property(org.apache.ambari.server.controller.internal.UpgradeItemResourceProvider.UPGRADE_CLUSTER_NAME).equals("c1").toPredicate();
        request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(propertyIds);
        upgradeItemResourceProvider = new org.apache.ambari.server.controller.internal.UpgradeItemResourceProvider(amc);
        resources = upgradeItemResourceProvider.getResources(request, predicate);
        org.junit.Assert.assertEquals(2, resources.size());
        res = resources.iterator().next();
        org.junit.Assert.assertEquals("Confirm Finalize", res.getPropertyValue("UpgradeItem/context"));
        java.lang.String msgStr = res.getPropertyValue("UpgradeItem/text").toString();
        com.google.gson.JsonParser parser = new com.google.gson.JsonParser();
        com.google.gson.JsonArray msgArray = ((com.google.gson.JsonArray) (parser.parse(msgStr)));
        com.google.gson.JsonObject msg = ((com.google.gson.JsonObject) (msgArray.get(0)));
        org.junit.Assert.assertTrue(msg.get("message").getAsString().startsWith("Please confirm"));
    }

    @org.junit.Test
    public void testGetResourcesWithSpecialOptions() throws java.lang.Exception {
        org.apache.ambari.server.state.Cluster cluster = clusters.getCluster("c1");
        java.util.List<org.apache.ambari.server.orm.entities.UpgradeEntity> upgrades = upgradeDao.findUpgrades(cluster.getClusterId());
        org.junit.Assert.assertEquals(0, upgrades.size());
        java.util.Map<java.lang.String, java.lang.Object> requestProps = new java.util.HashMap<>();
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_CLUSTER_NAME, "c1");
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_REPO_VERSION_ID, java.lang.String.valueOf(repoVersionEntity2111.getId()));
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_PACK, "upgrade_test");
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_SKIP_PREREQUISITE_CHECKS, "true");
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_DIRECTION, org.apache.ambari.server.stack.upgrade.Direction.UPGRADE.name());
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_SKIP_FAILURES, "true");
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_SKIP_SC_FAILURES, "true");
        org.apache.ambari.server.controller.spi.ResourceProvider upgradeResourceProvider = createProvider(amc);
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getCreateRequest(java.util.Collections.singleton(requestProps), null);
        org.apache.ambari.server.controller.spi.RequestStatus status = upgradeResourceProvider.createResources(request);
        org.junit.Assert.assertNotNull(status);
        java.util.Set<java.lang.String> propertyIds = new java.util.HashSet<>();
        propertyIds.add("Upgrade");
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_REQUEST_ID).equals("1").and().property(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_CLUSTER_NAME).equals("c1").toPredicate();
        request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(propertyIds);
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = upgradeResourceProvider.getResources(request, predicate);
        org.junit.Assert.assertEquals(1, resources.size());
        org.apache.ambari.server.controller.spi.Resource resource = resources.iterator().next();
        org.junit.Assert.assertEquals(true, resource.getPropertyValue(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_SKIP_FAILURES));
        org.junit.Assert.assertEquals(true, resource.getPropertyValue(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_SKIP_SC_FAILURES));
    }

    @org.junit.Test
    public void testCreatePartialDowngrade() throws java.lang.Exception {
        clusters.addHost("h2");
        org.apache.ambari.server.state.Host host = clusters.getHost("h2");
        java.util.Map<java.lang.String, java.lang.String> hostAttributes = new java.util.HashMap<>();
        hostAttributes.put("os_family", "redhat");
        hostAttributes.put("os_release_version", "6.3");
        host.setHostAttributes(hostAttributes);
        clusters.mapHostToCluster("h2", "c1");
        org.apache.ambari.server.state.Cluster cluster = clusters.getCluster("c1");
        org.apache.ambari.server.state.Service service = cluster.getService("ZOOKEEPER");
        java.util.List<org.apache.ambari.server.orm.entities.UpgradeEntity> upgrades = upgradeDao.findUpgrades(cluster.getClusterId());
        org.junit.Assert.assertEquals(0, upgrades.size());
        org.apache.ambari.server.orm.entities.RequestEntity requestEntity = new org.apache.ambari.server.orm.entities.RequestEntity();
        requestEntity.setRequestId(2L);
        requestEntity.setClusterId(cluster.getClusterId());
        requestEntity.setStages(new java.util.ArrayList<>());
        requestDao.create(requestEntity);
        org.apache.ambari.server.orm.entities.UpgradeEntity upgradeEntity = new org.apache.ambari.server.orm.entities.UpgradeEntity();
        upgradeEntity.setClusterId(cluster.getClusterId());
        upgradeEntity.setDirection(org.apache.ambari.server.stack.upgrade.Direction.UPGRADE);
        upgradeEntity.setRepositoryVersion(repoVersionEntity2200);
        upgradeEntity.setUpgradePackage("upgrade_test");
        upgradeEntity.setUpgradePackStackId(repoVersionEntity2200.getStackId());
        upgradeEntity.setUpgradeType(org.apache.ambari.spi.upgrade.UpgradeType.ROLLING);
        upgradeEntity.setRequestEntity(requestEntity);
        org.apache.ambari.server.orm.entities.UpgradeHistoryEntity history = new org.apache.ambari.server.orm.entities.UpgradeHistoryEntity();
        history.setUpgrade(upgradeEntity);
        history.setFromRepositoryVersion(service.getDesiredRepositoryVersion());
        history.setTargetRepositoryVersion(repoVersionEntity2200);
        history.setServiceName(service.getName());
        history.setComponentName("ZOKKEEPER_SERVER");
        upgradeEntity.addHistory(history);
        history = new org.apache.ambari.server.orm.entities.UpgradeHistoryEntity();
        history.setUpgrade(upgradeEntity);
        history.setFromRepositoryVersion(service.getDesiredRepositoryVersion());
        history.setTargetRepositoryVersion(repoVersionEntity2200);
        history.setServiceName(service.getName());
        history.setComponentName("ZOKKEEPER_CLIENT");
        upgradeEntity.addHistory(history);
        upgradeDao.create(upgradeEntity);
        upgrades = upgradeDao.findUpgrades(cluster.getClusterId());
        org.junit.Assert.assertEquals(1, upgrades.size());
        org.apache.ambari.server.state.ServiceComponent component = service.getServiceComponent("ZOOKEEPER_SERVER");
        org.apache.ambari.server.state.ServiceComponentHost sch = component.addServiceComponentHost("h2");
        sch.setVersion(repoVersionEntity2200.getVersion());
        org.apache.ambari.server.orm.entities.UpgradeEntity lastUpgrade = upgradeDao.findLastUpgradeForCluster(cluster.getClusterId(), org.apache.ambari.server.stack.upgrade.Direction.UPGRADE);
        org.junit.Assert.assertNotNull(lastUpgrade);
        java.util.Map<java.lang.String, java.lang.Object> requestProps = new java.util.HashMap<>();
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_CLUSTER_NAME, "c1");
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_PACK, "upgrade_test");
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_SKIP_PREREQUISITE_CHECKS, "true");
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_DIRECTION, org.apache.ambari.server.stack.upgrade.Direction.DOWNGRADE.name());
        java.util.Map<java.lang.String, java.lang.String> requestInfoProperties = new java.util.HashMap<>();
        org.apache.ambari.server.controller.spi.ResourceProvider upgradeResourceProvider = createProvider(amc);
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getCreateRequest(java.util.Collections.singleton(requestProps), requestInfoProperties);
        upgradeResourceProvider.createResources(request);
        upgrades = upgradeDao.findUpgrades(cluster.getClusterId());
        org.junit.Assert.assertEquals(2, upgrades.size());
        org.apache.ambari.server.orm.entities.UpgradeEntity downgrade = upgrades.get(1);
        org.junit.Assert.assertEquals(cluster.getClusterId(), downgrade.getClusterId().longValue());
        java.util.List<org.apache.ambari.server.orm.entities.UpgradeGroupEntity> upgradeGroups = downgrade.getUpgradeGroups();
        org.junit.Assert.assertEquals(3, upgradeGroups.size());
        org.apache.ambari.server.orm.entities.UpgradeGroupEntity group = upgradeGroups.get(1);
        org.junit.Assert.assertEquals("ZOOKEEPER", group.getName());
        org.junit.Assert.assertEquals(3, group.getItems().size());
    }

    @org.junit.Test
    public void testDowngradeToBase() throws java.lang.Exception {
        org.apache.ambari.server.state.Cluster cluster = clusters.getCluster("c1");
        org.junit.Assert.assertEquals(cluster.getDesiredStackVersion().getStackId(), "HDP-2.1.1");
        java.util.Map<java.lang.String, java.lang.Object> requestProps = new java.util.HashMap<>();
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_CLUSTER_NAME, "c1");
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_REPO_VERSION_ID, java.lang.String.valueOf(repoVersionEntity2200.getId()));
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_PACK, "upgrade_test");
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_SKIP_PREREQUISITE_CHECKS, "true");
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_DIRECTION, org.apache.ambari.server.stack.upgrade.Direction.UPGRADE.name());
        org.apache.ambari.server.controller.spi.ResourceProvider upgradeResourceProvider = createProvider(amc);
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getCreateRequest(java.util.Collections.singleton(requestProps), null);
        upgradeResourceProvider.createResources(request);
        java.util.List<org.apache.ambari.server.orm.entities.UpgradeEntity> upgrades = upgradeDao.findUpgrades(cluster.getClusterId());
        org.junit.Assert.assertEquals(1, upgrades.size());
        org.apache.ambari.server.orm.entities.UpgradeEntity upgrade = upgrades.get(0);
        org.junit.Assert.assertEquals("HDP-2.2.0", cluster.getDesiredStackVersion().getStackId());
        abortUpgrade(upgrade.getRequestId());
        requestProps = new java.util.HashMap<>();
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_CLUSTER_NAME, "c1");
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_REPO_VERSION_ID, "9999");
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_PACK, "upgrade_test");
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_SKIP_PREREQUISITE_CHECKS, "true");
        request = org.apache.ambari.server.controller.utilities.PropertyHelper.getCreateRequest(java.util.Collections.singleton(requestProps), null);
        try {
            upgradeResourceProvider.createResources(request);
            org.junit.Assert.fail("Expected an exception going downgrade with no upgrade pack");
        } catch (java.lang.Exception e) {
        }
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_CLUSTER_NAME, "c1");
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_PACK, "upgrade_test");
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_SKIP_PREREQUISITE_CHECKS, "true");
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_DIRECTION, org.apache.ambari.server.stack.upgrade.Direction.DOWNGRADE.name());
        java.util.Map<java.lang.String, java.lang.String> requestInfoProperties = new java.util.HashMap<>();
        request = org.apache.ambari.server.controller.utilities.PropertyHelper.getCreateRequest(java.util.Collections.singleton(requestProps), requestInfoProperties);
        org.apache.ambari.server.controller.spi.RequestStatus status = upgradeResourceProvider.createResources(request);
        org.junit.Assert.assertEquals(1, status.getAssociatedResources().size());
        org.apache.ambari.server.controller.spi.Resource r = status.getAssociatedResources().iterator().next();
        java.lang.String id = r.getPropertyValue("Upgrade/request_id").toString();
        org.junit.Assert.assertEquals("HDP-2.1.1", cluster.getDesiredStackVersion().getStackId());
        org.apache.ambari.server.orm.entities.UpgradeEntity entity = upgradeDao.findUpgrade(java.lang.Long.parseLong(id));
        org.junit.Assert.assertNotNull(entity);
        org.junit.Assert.assertEquals(org.apache.ambari.server.stack.upgrade.Direction.DOWNGRADE, entity.getDirection());
        org.junit.Assert.assertEquals(repoVersionEntity2200.getVersion(), entity.getRepositoryVersion().getVersion());
        org.junit.Assert.assertEquals(repoVersionEntity2110.getVersion(), entity.getHistory().iterator().next().getTargetVersion());
        org.apache.ambari.server.orm.dao.StageDAO dao = injector.getInstance(org.apache.ambari.server.orm.dao.StageDAO.class);
        java.util.List<org.apache.ambari.server.orm.entities.StageEntity> stages = dao.findByRequestId(entity.getRequestId());
        org.junit.Assert.assertEquals("HDP-2.1.1", cluster.getDesiredStackVersion().getStackId());
        com.google.gson.Gson gson = new com.google.gson.Gson();
        for (org.apache.ambari.server.orm.entities.StageEntity se : stages) {
            java.util.Map<java.lang.String, java.lang.String> map = gson.<java.util.Map<java.lang.String, java.lang.String>>fromJson(se.getCommandParamsStage(), java.util.Map.class);
            org.junit.Assert.assertTrue(map.containsKey("upgrade_direction"));
            org.junit.Assert.assertEquals("downgrade", map.get("upgrade_direction"));
        }
    }

    @org.junit.Test
    public void testNotFullDowngrade() throws java.lang.Exception {
        org.apache.ambari.server.state.Cluster cluster = clusters.getCluster("c1");
        org.apache.ambari.server.state.Service service = cluster.addService("HIVE", repoVersionEntity2110);
        org.apache.ambari.server.state.ServiceComponent component = service.addServiceComponent("HIVE_SERVER");
        org.apache.ambari.server.state.ServiceComponentHost sch = component.addServiceComponentHost("h1");
        sch.setVersion("2.1.1.0");
        java.util.Map<java.lang.String, java.lang.Object> requestProps = new java.util.HashMap<>();
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_CLUSTER_NAME, "c1");
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_REPO_VERSION_ID, java.lang.String.valueOf(repoVersionEntity2200.getId()));
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_PACK, "upgrade_nonrolling_new_stack");
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_TYPE, "NON_ROLLING");
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_SKIP_PREREQUISITE_CHECKS, "true");
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_DIRECTION, org.apache.ambari.server.stack.upgrade.Direction.UPGRADE.name());
        org.apache.ambari.server.controller.spi.ResourceProvider upgradeResourceProvider = createProvider(amc);
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getCreateRequest(java.util.Collections.singleton(requestProps), null);
        upgradeResourceProvider.createResources(request);
        java.util.List<org.apache.ambari.server.orm.entities.UpgradeEntity> upgrades = upgradeDao.findUpgrades(cluster.getClusterId());
        org.junit.Assert.assertEquals(1, upgrades.size());
        org.apache.ambari.server.orm.entities.UpgradeEntity upgrade = upgrades.get(0);
        java.util.List<org.apache.ambari.server.orm.entities.UpgradeGroupEntity> groups = upgrade.getUpgradeGroups();
        boolean isHiveGroupFound = false;
        boolean isZKGroupFound = false;
        for (org.apache.ambari.server.orm.entities.UpgradeGroupEntity group : groups) {
            if (group.getName().equalsIgnoreCase("hive")) {
                isHiveGroupFound = true;
            } else if (group.getName().equalsIgnoreCase("zookeeper")) {
                isZKGroupFound = true;
            }
        }
        org.junit.Assert.assertTrue(isHiveGroupFound);
        org.junit.Assert.assertTrue(isZKGroupFound);
        isHiveGroupFound = false;
        isZKGroupFound = false;
        sch.setVersion("2.2.0.0");
        abortUpgrade(upgrade.getRequestId());
        service.setDesiredRepositoryVersion(repoVersionEntity2200);
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_CLUSTER_NAME, "c1");
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_PACK, "upgrade_nonrolling_new_stack");
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_SKIP_PREREQUISITE_CHECKS, "true");
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_DIRECTION, org.apache.ambari.server.stack.upgrade.Direction.DOWNGRADE.name());
        java.util.Map<java.lang.String, java.lang.String> requestInfoProperties = new java.util.HashMap<>();
        request = org.apache.ambari.server.controller.utilities.PropertyHelper.getCreateRequest(java.util.Collections.singleton(requestProps), requestInfoProperties);
        org.apache.ambari.server.controller.spi.RequestStatus status = upgradeResourceProvider.createResources(request);
        org.apache.ambari.server.orm.entities.UpgradeEntity upgradeEntity = upgradeDao.findUpgradeByRequestId(getRequestId(status));
        for (org.apache.ambari.server.orm.entities.UpgradeGroupEntity group : upgradeEntity.getUpgradeGroups()) {
            if (group.getName().equalsIgnoreCase("hive")) {
                isHiveGroupFound = true;
            } else if (group.getName().equalsIgnoreCase("zookeeper")) {
                isZKGroupFound = true;
            }
        }
        org.junit.Assert.assertTrue(isHiveGroupFound);
        org.junit.Assert.assertFalse(isZKGroupFound);
    }

    @org.junit.Test
    public void testAbortUpgrade() throws java.lang.Exception {
        org.apache.ambari.server.controller.spi.RequestStatus status = testCreateResources();
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> createdResources = status.getAssociatedResources();
        org.junit.Assert.assertEquals(1, createdResources.size());
        org.apache.ambari.server.controller.spi.Resource res = createdResources.iterator().next();
        java.lang.Long id = ((java.lang.Long) (res.getPropertyValue("Upgrade/request_id")));
        org.junit.Assert.assertNotNull(id);
        org.junit.Assert.assertEquals(java.lang.Long.valueOf(1), id);
        java.util.Map<java.lang.String, java.lang.Object> requestProps = new java.util.HashMap<>();
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_REQUEST_ID, id.toString());
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_CLUSTER_NAME, "c1");
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_REQUEST_STATUS, "ABORTED");
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_SUSPENDED, "true");
        org.apache.ambari.server.controller.internal.UpgradeResourceProvider urp = createProvider(amc);
        org.apache.ambari.server.controller.spi.Request req = org.apache.ambari.server.controller.utilities.PropertyHelper.getUpdateRequest(requestProps, null);
        urp.updateResources(req, null);
        java.util.List<org.apache.ambari.server.orm.entities.HostRoleCommandEntity> commands = hrcDAO.findByRequest(id);
        int i = 0;
        for (org.apache.ambari.server.orm.entities.HostRoleCommandEntity command : commands) {
            if (i < 3) {
                command.setStatus(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED);
            } else {
                command.setStatus(org.apache.ambari.server.actionmanager.HostRoleStatus.ABORTED);
            }
            hrcDAO.merge(command);
            i++;
        }
        req = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_CLUSTER_NAME, org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_ID, org.apache.ambari.server.controller.internal.UpgradeResourceProvider.REQUEST_PROGRESS_PERCENT_ID);
        org.apache.ambari.server.controller.spi.Predicate pred = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_REQUEST_ID).equals(id.toString()).and().property(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_CLUSTER_NAME).equals("c1").toPredicate();
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = urp.getResources(req, pred);
        org.junit.Assert.assertEquals(1, resources.size());
        res = resources.iterator().next();
        java.lang.Double value = ((java.lang.Double) (res.getPropertyValue(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.REQUEST_PROGRESS_PERCENT_ID)));
        org.junit.Assert.assertEquals(37.5, value, 0.1);
    }

    @org.junit.Test
    public void testResumeUpgrade() throws java.lang.Exception {
        org.apache.ambari.server.controller.spi.RequestStatus status = testCreateResources();
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> createdResources = status.getAssociatedResources();
        org.junit.Assert.assertEquals(1, createdResources.size());
        org.apache.ambari.server.controller.spi.Resource res = createdResources.iterator().next();
        java.lang.Long id = ((java.lang.Long) (res.getPropertyValue("Upgrade/request_id")));
        org.junit.Assert.assertNotNull(id);
        org.junit.Assert.assertEquals(java.lang.Long.valueOf(1), id);
        java.util.Map<java.lang.String, java.lang.Object> requestProps = new java.util.HashMap<>();
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_REQUEST_ID, id.toString());
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_CLUSTER_NAME, "c1");
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_REQUEST_STATUS, "ABORTED");
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_SUSPENDED, "true");
        org.apache.ambari.server.controller.internal.UpgradeResourceProvider urp = createProvider(amc);
        org.apache.ambari.server.controller.spi.Request req = org.apache.ambari.server.controller.utilities.PropertyHelper.getUpdateRequest(requestProps, null);
        urp.updateResources(req, null);
        org.apache.ambari.server.actionmanager.ActionManager am = injector.getInstance(org.apache.ambari.server.actionmanager.ActionManager.class);
        java.util.List<org.apache.ambari.server.actionmanager.HostRoleCommand> commands = am.getRequestTasks(id);
        boolean foundOne = false;
        for (org.apache.ambari.server.actionmanager.HostRoleCommand hrc : commands) {
            if (hrc.getRole().equals(org.apache.ambari.server.Role.AMBARI_SERVER_ACTION)) {
                org.junit.Assert.assertEquals(-1L, hrc.getHostId());
                org.junit.Assert.assertNull(hrc.getHostName());
                foundOne = true;
            }
        }
        org.junit.Assert.assertTrue("Expected at least one server-side action", foundOne);
        org.junit.Assert.assertTrue(commands.size() > 5);
        org.apache.ambari.server.orm.dao.HostRoleCommandDAO dao = injector.getInstance(org.apache.ambari.server.orm.dao.HostRoleCommandDAO.class);
        for (int i = 0; i < commands.size(); i++) {
            org.apache.ambari.server.actionmanager.HostRoleCommand command = commands.get(i);
            org.apache.ambari.server.orm.entities.HostRoleCommandEntity entity = dao.findByPK(command.getTaskId());
            final org.apache.ambari.server.actionmanager.HostRoleStatus newStatus;
            switch (i) {
                case 0 :
                case 1 :
                    newStatus = org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED;
                    break;
                case 2 :
                    newStatus = org.apache.ambari.server.actionmanager.HostRoleStatus.TIMEDOUT;
                    break;
                case 3 :
                    newStatus = org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED;
                    break;
                case 4 :
                    newStatus = org.apache.ambari.server.actionmanager.HostRoleStatus.FAILED;
                    break;
                default :
                    newStatus = org.apache.ambari.server.actionmanager.HostRoleStatus.ABORTED;
                    break;
            }
            entity.setStatus(newStatus);
            dao.merge(entity);
        }
        requestProps = new java.util.HashMap<>();
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_REQUEST_ID, id.toString());
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_REQUEST_STATUS, "PENDING");
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_CLUSTER_NAME, "c1");
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_SUSPENDED, "false");
        req = org.apache.ambari.server.controller.utilities.PropertyHelper.getUpdateRequest(requestProps, null);
        urp.updateResources(req, null);
        commands = am.getRequestTasks(id);
        for (int i = 0; i < commands.size(); i++) {
            org.apache.ambari.server.actionmanager.HostRoleCommand command = commands.get(i);
            if (i < 5) {
                org.junit.Assert.assertTrue(command.getStatus() != org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING);
            } else {
                org.junit.Assert.assertTrue(command.getStatus() == org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING);
            }
        }
    }

    @org.junit.Test(expected = java.lang.IllegalArgumentException.class)
    public void testAbortWithoutSuspendFlag() throws java.lang.Exception {
        org.apache.ambari.server.controller.spi.RequestStatus status = testCreateResources();
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> createdResources = status.getAssociatedResources();
        org.junit.Assert.assertEquals(1, createdResources.size());
        org.apache.ambari.server.controller.spi.Resource res = createdResources.iterator().next();
        java.lang.Long id = ((java.lang.Long) (res.getPropertyValue("Upgrade/request_id")));
        org.junit.Assert.assertNotNull(id);
        org.junit.Assert.assertEquals(java.lang.Long.valueOf(1), id);
        java.util.Map<java.lang.String, java.lang.Object> requestProps = new java.util.HashMap<>();
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_REQUEST_ID, id.toString());
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_CLUSTER_NAME, "c1");
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_REQUEST_STATUS, "ABORTED");
        org.apache.ambari.server.controller.internal.UpgradeResourceProvider urp = createProvider(amc);
        org.apache.ambari.server.controller.spi.Request req = org.apache.ambari.server.controller.utilities.PropertyHelper.getUpdateRequest(requestProps, null);
        urp.updateResources(req, null);
    }

    @org.junit.Test
    public void testDirectionUpgrade() throws java.lang.Exception {
        org.apache.ambari.server.state.Cluster cluster = clusters.getCluster("c1");
        org.apache.ambari.server.orm.entities.StackEntity stackEntity = stackDAO.find("HDP", "2.1.1");
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repoVersionEntity = new org.apache.ambari.server.orm.entities.RepositoryVersionEntity();
        repoVersionEntity.setDisplayName("My New Version 3");
        repoVersionEntity.addRepoOsEntities(createTestOperatingSystems());
        repoVersionEntity.setStack(stackEntity);
        repoVersionEntity.setVersion("2.2.2.3");
        repoVersionDao.create(repoVersionEntity);
        java.util.Map<java.lang.String, java.lang.Object> requestProps = new java.util.HashMap<>();
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_CLUSTER_NAME, "c1");
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_REPO_VERSION_ID, java.lang.String.valueOf(repoVersionEntity.getId()));
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_PACK, "upgrade_direction");
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_SKIP_PREREQUISITE_CHECKS, "true");
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_DIRECTION, org.apache.ambari.server.stack.upgrade.Direction.UPGRADE.name());
        org.apache.ambari.server.controller.spi.ResourceProvider upgradeResourceProvider = createProvider(amc);
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getCreateRequest(java.util.Collections.singleton(requestProps), null);
        upgradeResourceProvider.createResources(request);
        java.util.List<org.apache.ambari.server.orm.entities.UpgradeEntity> upgrades = upgradeDao.findUpgrades(cluster.getClusterId());
        org.junit.Assert.assertEquals(1, upgrades.size());
        org.apache.ambari.server.orm.entities.UpgradeEntity upgrade = upgrades.get(0);
        java.lang.Long id = upgrade.getRequestId();
        org.junit.Assert.assertEquals(3, upgrade.getUpgradeGroups().size());
        org.apache.ambari.server.orm.entities.UpgradeGroupEntity group = upgrade.getUpgradeGroups().get(2);
        org.junit.Assert.assertEquals("POST_CLUSTER", group.getName());
        org.junit.Assert.assertTrue(!group.getItems().isEmpty());
        for (org.apache.ambari.server.orm.entities.UpgradeItemEntity item : group.getItems()) {
            org.junit.Assert.assertFalse(item.getText().toLowerCase().contains("downgrade"));
        }
        abortUpgrade(upgrade.getRequestId());
        requestProps.clear();
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_CLUSTER_NAME, "c1");
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_PACK, "upgrade_direction");
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_SKIP_PREREQUISITE_CHECKS, "true");
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_DIRECTION, org.apache.ambari.server.stack.upgrade.Direction.DOWNGRADE.name());
        request = org.apache.ambari.server.controller.utilities.PropertyHelper.getCreateRequest(java.util.Collections.singleton(requestProps), null);
        upgradeResourceProvider.createResources(request);
        upgrades = upgradeDao.findUpgrades(cluster.getClusterId());
        org.junit.Assert.assertEquals(2, upgrades.size());
        upgrade = null;
        for (org.apache.ambari.server.orm.entities.UpgradeEntity u : upgrades) {
            if (!u.getRequestId().equals(id)) {
                upgrade = u;
            }
        }
        org.junit.Assert.assertNotNull(upgrade);
        java.util.List<org.apache.ambari.server.orm.entities.UpgradeGroupEntity> groups = upgrade.getUpgradeGroups();
        org.junit.Assert.assertEquals("Downgrade groups reduced from 3 to 2", 1, groups.size());
        group = upgrade.getUpgradeGroups().get(0);
        org.junit.Assert.assertEquals("Execution items increased from 1 to 2", 2, group.getItems().size());
    }

    @org.junit.Test
    public void testPercents() throws java.lang.Exception {
        org.apache.ambari.server.controller.spi.RequestStatus status = testCreateResources();
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> createdResources = status.getAssociatedResources();
        org.junit.Assert.assertEquals(1, createdResources.size());
        org.apache.ambari.server.controller.spi.Resource res = createdResources.iterator().next();
        java.lang.Long id = ((java.lang.Long) (res.getPropertyValue("Upgrade/request_id")));
        org.junit.Assert.assertNotNull(id);
        org.junit.Assert.assertEquals(java.lang.Long.valueOf(1), id);
        org.apache.ambari.server.orm.dao.StageDAO stageDao = injector.getInstance(org.apache.ambari.server.orm.dao.StageDAO.class);
        org.apache.ambari.server.orm.dao.HostRoleCommandDAO hrcDao = injector.getInstance(org.apache.ambari.server.orm.dao.HostRoleCommandDAO.class);
        java.util.List<org.apache.ambari.server.orm.entities.StageEntity> stages = stageDao.findByRequestId(id);
        java.util.List<org.apache.ambari.server.orm.entities.HostRoleCommandEntity> tasks = hrcDao.findByRequest(id);
        java.util.Set<java.lang.Long> stageIds = new java.util.HashSet<>();
        for (org.apache.ambari.server.orm.entities.StageEntity se : stages) {
            stageIds.add(se.getStageId());
        }
        org.apache.ambari.server.controller.internal.CalculatedStatus calc = null;
        int i = 0;
        for (org.apache.ambari.server.orm.entities.HostRoleCommandEntity hrce : tasks) {
            hrce.setStatus(org.apache.ambari.server.actionmanager.HostRoleStatus.IN_PROGRESS);
            hrcDao.merge(hrce);
            calc = org.apache.ambari.server.controller.internal.CalculatedStatus.statusFromStageSummary(hrcDao.findAggregateCounts(id), stageIds);
            org.junit.Assert.assertEquals(((i++) + 1) * 4.375, calc.getPercent(), 0.01);
            org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.HostRoleStatus.IN_PROGRESS, calc.getStatus());
        }
        i = 0;
        for (org.apache.ambari.server.orm.entities.HostRoleCommandEntity hrce : tasks) {
            hrce.setStatus(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED);
            hrcDao.merge(hrce);
            calc = org.apache.ambari.server.controller.internal.CalculatedStatus.statusFromStageSummary(hrcDao.findAggregateCounts(id), stageIds);
            org.junit.Assert.assertEquals(35 + (((i++) + 1) * 8.125), calc.getPercent(), 0.01);
            if (i < 8) {
                org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.HostRoleStatus.IN_PROGRESS, calc.getStatus());
            }
        }
        calc = org.apache.ambari.server.controller.internal.CalculatedStatus.statusFromStageSummary(hrcDao.findAggregateCounts(id), stageIds);
        org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, calc.getStatus());
        org.junit.Assert.assertEquals(100.0, calc.getPercent(), 0.01);
    }

    @org.junit.Test
    public void testCreateCrossStackUpgrade() throws java.lang.Exception {
        org.apache.ambari.server.state.Cluster cluster = clusters.getCluster("c1");
        org.apache.ambari.server.state.StackId oldStack = repoVersionEntity2110.getStackId();
        org.junit.Assert.assertEquals(cluster.getDesiredStackVersion(), oldStack);
        for (org.apache.ambari.server.state.Service s : cluster.getServices().values()) {
            org.junit.Assert.assertEquals(oldStack, s.getDesiredStackId());
            for (org.apache.ambari.server.state.ServiceComponent sc : s.getServiceComponents().values()) {
                org.junit.Assert.assertEquals(oldStack, sc.getDesiredStackId());
                for (org.apache.ambari.server.state.ServiceComponentHost sch : sc.getServiceComponentHosts().values()) {
                    org.junit.Assert.assertEquals(repoVersionEntity2110.getVersion(), sch.getVersion());
                }
            }
        }
        org.apache.ambari.server.state.Config config = configFactory.createNew(cluster, "zoo.cfg", "abcdefg", java.util.Collections.singletonMap("a", "b"), null);
        cluster.addDesiredConfig("admin", java.util.Collections.singleton(config));
        java.util.Map<java.lang.String, java.lang.Object> requestProps = new java.util.HashMap<>();
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_CLUSTER_NAME, "c1");
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_REPO_VERSION_ID, java.lang.String.valueOf(repoVersionEntity2200.getId()));
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_PACK, "upgrade_test");
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_SKIP_PREREQUISITE_CHECKS, "true");
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_DIRECTION, org.apache.ambari.server.stack.upgrade.Direction.UPGRADE.name());
        org.apache.ambari.server.controller.spi.ResourceProvider upgradeResourceProvider = createProvider(amc);
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getCreateRequest(java.util.Collections.singleton(requestProps), null);
        upgradeResourceProvider.createResources(request);
        java.util.List<org.apache.ambari.server.orm.entities.UpgradeEntity> upgrades = upgradeDao.findUpgrades(cluster.getClusterId());
        org.junit.Assert.assertEquals(1, upgrades.size());
        org.apache.ambari.server.orm.entities.UpgradeEntity upgrade = upgrades.get(0);
        org.junit.Assert.assertEquals(3, upgrade.getUpgradeGroups().size());
        org.apache.ambari.server.orm.entities.UpgradeGroupEntity group = upgrade.getUpgradeGroups().get(2);
        org.junit.Assert.assertEquals(2, group.getItems().size());
        group = upgrade.getUpgradeGroups().get(0);
        org.junit.Assert.assertEquals(2, group.getItems().size());
        org.junit.Assert.assertTrue(cluster.getDesiredConfigs().containsKey("zoo.cfg"));
        org.junit.Assert.assertTrue(cluster.getDesiredStackVersion().getStackId().equals("HDP-2.2.0"));
        for (org.apache.ambari.server.state.Service s : cluster.getServices().values()) {
            org.junit.Assert.assertEquals(repoVersionEntity2200, s.getDesiredRepositoryVersion());
            for (org.apache.ambari.server.state.ServiceComponent sc : s.getServiceComponents().values()) {
                org.junit.Assert.assertEquals(repoVersionEntity2200, sc.getDesiredRepositoryVersion());
            }
        }
    }

    private org.apache.ambari.server.controller.internal.UpgradeResourceProvider createProvider(org.apache.ambari.server.controller.AmbariManagementController amc) {
        org.apache.ambari.server.controller.ResourceProviderFactory factory = injector.getInstance(org.apache.ambari.server.controller.ResourceProviderFactory.class);
        org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.init(factory);
        org.apache.ambari.server.controller.spi.Resource.Type type = org.apache.ambari.server.controller.spi.Resource.Type.Upgrade;
        return ((org.apache.ambari.server.controller.internal.UpgradeResourceProvider) (org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.getResourceProvider(type, amc)));
    }

    private org.apache.ambari.server.controller.spi.RequestStatus testCreateResources() throws java.lang.Exception {
        org.apache.ambari.server.state.Cluster cluster = clusters.getCluster("c1");
        java.util.List<org.apache.ambari.server.orm.entities.UpgradeEntity> upgrades = upgradeDao.findUpgrades(cluster.getClusterId());
        org.junit.Assert.assertEquals(0, upgrades.size());
        java.util.Map<java.lang.String, java.lang.Object> requestProps = new java.util.HashMap<>();
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_CLUSTER_NAME, "c1");
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_REPO_VERSION_ID, java.lang.String.valueOf(repoVersionEntity2111.getId()));
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_PACK, "upgrade_test");
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_SKIP_PREREQUISITE_CHECKS, "true");
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_DIRECTION, org.apache.ambari.server.stack.upgrade.Direction.UPGRADE.name());
        org.apache.ambari.server.controller.spi.ResourceProvider upgradeResourceProvider = createProvider(amc);
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getCreateRequest(java.util.Collections.singleton(requestProps), null);
        org.apache.ambari.server.controller.spi.RequestStatus status = upgradeResourceProvider.createResources(request);
        upgrades = upgradeDao.findUpgrades(cluster.getClusterId());
        org.junit.Assert.assertEquals(1, upgrades.size());
        org.apache.ambari.server.orm.entities.UpgradeEntity entity = upgrades.get(0);
        org.junit.Assert.assertEquals(cluster.getClusterId(), entity.getClusterId().longValue());
        org.junit.Assert.assertEquals(org.apache.ambari.spi.upgrade.UpgradeType.ROLLING, entity.getUpgradeType());
        org.apache.ambari.server.orm.dao.StageDAO stageDAO = injector.getInstance(org.apache.ambari.server.orm.dao.StageDAO.class);
        java.util.List<org.apache.ambari.server.orm.entities.StageEntity> stageEntities = stageDAO.findByRequestId(entity.getRequestId());
        com.google.gson.Gson gson = new com.google.gson.Gson();
        for (org.apache.ambari.server.orm.entities.StageEntity se : stageEntities) {
            java.util.Map<java.lang.String, java.lang.String> map = gson.<java.util.Map<java.lang.String, java.lang.String>>fromJson(se.getCommandParamsStage(), java.util.Map.class);
            org.junit.Assert.assertTrue(map.containsKey("upgrade_direction"));
            org.junit.Assert.assertEquals("upgrade", map.get("upgrade_direction"));
            if (map.containsKey("upgrade_type")) {
                org.junit.Assert.assertEquals("rolling_upgrade", map.get("upgrade_type"));
            }
        }
        java.util.List<org.apache.ambari.server.orm.entities.UpgradeGroupEntity> upgradeGroups = entity.getUpgradeGroups();
        org.junit.Assert.assertEquals(3, upgradeGroups.size());
        org.apache.ambari.server.orm.entities.UpgradeGroupEntity group = upgradeGroups.get(1);
        org.junit.Assert.assertEquals(4, group.getItems().size());
        org.junit.Assert.assertTrue(group.getItems().get(0).getText().contains("placeholder of placeholder-rendered-properly"));
        org.junit.Assert.assertTrue(group.getItems().get(1).getText().contains("Restarting"));
        org.junit.Assert.assertTrue(group.getItems().get(2).getText().contains("Updating"));
        org.junit.Assert.assertTrue(group.getItems().get(3).getText().contains("Service Check"));
        org.apache.ambari.server.actionmanager.ActionManager am = injector.getInstance(org.apache.ambari.server.actionmanager.ActionManager.class);
        java.util.List<java.lang.Long> requests = am.getRequestsByStatus(org.apache.ambari.server.actionmanager.RequestStatus.IN_PROGRESS, 100, true);
        org.junit.Assert.assertEquals(1, requests.size());
        org.junit.Assert.assertEquals(requests.get(0), entity.getRequestId());
        java.util.List<org.apache.ambari.server.actionmanager.Stage> stages = am.getRequestStatus(requests.get(0).longValue());
        org.junit.Assert.assertEquals(8, stages.size());
        java.util.List<org.apache.ambari.server.actionmanager.HostRoleCommand> tasks = am.getRequestTasks(requests.get(0).longValue());
        org.junit.Assert.assertEquals(8, tasks.size());
        java.util.Set<java.lang.Long> slaveStageIds = new java.util.HashSet<>();
        org.apache.ambari.server.orm.entities.UpgradeGroupEntity coreSlavesGroup = upgradeGroups.get(1);
        for (org.apache.ambari.server.orm.entities.UpgradeItemEntity itemEntity : coreSlavesGroup.getItems()) {
            slaveStageIds.add(itemEntity.getStageId());
        }
        for (org.apache.ambari.server.actionmanager.Stage stage : stages) {
            org.junit.Assert.assertEquals(slaveStageIds.contains(stage.getStageId()), stage.isSkippable());
            for (java.util.Map<java.lang.String, org.apache.ambari.server.actionmanager.HostRoleCommand> taskMap : stage.getHostRoleCommands().values()) {
                for (org.apache.ambari.server.actionmanager.HostRoleCommand task : taskMap.values()) {
                    org.junit.Assert.assertEquals(!slaveStageIds.contains(stage.getStageId()), task.isRetryAllowed());
                }
            }
        }
        return status;
    }

    @org.junit.Test
    public void testUpdateSkipSCFailures() throws java.lang.Exception {
        org.apache.ambari.server.state.Cluster cluster = clusters.getCluster("c1");
        java.util.Map<java.lang.String, java.lang.Object> requestProps = new java.util.HashMap<>();
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_CLUSTER_NAME, "c1");
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_REPO_VERSION_ID, java.lang.String.valueOf(repoVersionEntity2200.getId()));
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_PACK, "upgrade_test");
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_TYPE, org.apache.ambari.spi.upgrade.UpgradeType.ROLLING.toString());
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_SKIP_FAILURES, java.lang.Boolean.FALSE.toString());
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_SKIP_SC_FAILURES, java.lang.Boolean.TRUE.toString());
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_SKIP_MANUAL_VERIFICATION, java.lang.Boolean.FALSE.toString());
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_SKIP_PREREQUISITE_CHECKS, java.lang.Boolean.TRUE.toString());
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_DIRECTION, org.apache.ambari.server.stack.upgrade.Direction.UPGRADE.name());
        org.apache.ambari.server.controller.spi.ResourceProvider upgradeResourceProvider = createProvider(amc);
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getCreateRequest(java.util.Collections.singleton(requestProps), null);
        upgradeResourceProvider.createResources(request);
        java.util.List<org.apache.ambari.server.orm.entities.UpgradeEntity> upgrades = upgradeDao.findUpgrades(1);
        org.junit.Assert.assertEquals(1, upgrades.size());
        org.apache.ambari.server.orm.entities.UpgradeEntity entity = upgrades.get(0);
        org.apache.ambari.server.orm.dao.HostRoleCommandDAO dao = injector.getInstance(org.apache.ambari.server.orm.dao.HostRoleCommandDAO.class);
        java.util.List<org.apache.ambari.server.orm.entities.HostRoleCommandEntity> tasks = dao.findByRequest(entity.getRequestId());
        for (org.apache.ambari.server.orm.entities.HostRoleCommandEntity task : tasks) {
            if (task.getRoleCommand() == org.apache.ambari.server.RoleCommand.SERVICE_CHECK) {
                org.apache.ambari.server.orm.entities.StageEntity stage = task.getStage();
                if (stage.isSkippable() && stage.isAutoSkipOnFailureSupported()) {
                    org.junit.Assert.assertTrue(task.isFailureAutoSkipped());
                } else {
                    org.junit.Assert.assertFalse(task.isFailureAutoSkipped());
                }
            } else {
                org.junit.Assert.assertFalse(task.isFailureAutoSkipped());
            }
        }
    }

    @org.junit.Test
    public void testUpdateSkipFailures() throws java.lang.Exception {
        testCreateResourcesWithAutoSkipFailures();
        java.util.List<org.apache.ambari.server.orm.entities.UpgradeEntity> upgrades = upgradeDao.findUpgrades(1);
        org.junit.Assert.assertEquals(1, upgrades.size());
        org.apache.ambari.server.orm.entities.UpgradeEntity entity = upgrades.get(0);
        org.apache.ambari.server.orm.dao.HostRoleCommandDAO dao = injector.getInstance(org.apache.ambari.server.orm.dao.HostRoleCommandDAO.class);
        java.util.List<org.apache.ambari.server.orm.entities.HostRoleCommandEntity> tasks = dao.findByRequest(entity.getRequestId());
        for (org.apache.ambari.server.orm.entities.HostRoleCommandEntity task : tasks) {
            org.apache.ambari.server.orm.entities.StageEntity stage = task.getStage();
            if (stage.isSkippable() && stage.isAutoSkipOnFailureSupported()) {
                org.junit.Assert.assertTrue(task.isFailureAutoSkipped());
            } else {
                org.junit.Assert.assertFalse(task.isFailureAutoSkipped());
            }
        }
        java.util.Map<java.lang.String, java.lang.Object> requestProps = new java.util.HashMap<>();
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_CLUSTER_NAME, "c1");
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_REPO_VERSION_ID, java.lang.String.valueOf(repoVersionEntity2200.getId()));
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_SKIP_FAILURES, java.lang.Boolean.TRUE.toString());
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_SKIP_SC_FAILURES, java.lang.Boolean.FALSE.toString());
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_REQUEST_ID, "" + entity.getRequestId());
        org.apache.ambari.server.controller.spi.ResourceProvider upgradeResourceProvider = createProvider(amc);
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getUpdateRequest(requestProps, null);
        upgradeResourceProvider.updateResources(request, null);
        tasks = dao.findByRequest(entity.getRequestId());
        for (org.apache.ambari.server.orm.entities.HostRoleCommandEntity task : tasks) {
            if (task.getRoleCommand() == org.apache.ambari.server.RoleCommand.SERVICE_CHECK) {
                org.junit.Assert.assertFalse(task.isFailureAutoSkipped());
            } else {
                org.apache.ambari.server.orm.entities.StageEntity stage = task.getStage();
                if (stage.isSkippable() && stage.isAutoSkipOnFailureSupported()) {
                    org.junit.Assert.assertTrue(task.isFailureAutoSkipped());
                } else {
                    org.junit.Assert.assertFalse(task.isFailureAutoSkipped());
                }
            }
        }
        requestProps = new java.util.HashMap<>();
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_CLUSTER_NAME, "c1");
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_REPO_VERSION_ID, java.lang.String.valueOf(repoVersionEntity2200.getId()));
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_SKIP_FAILURES, java.lang.Boolean.FALSE.toString());
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_SKIP_SC_FAILURES, java.lang.Boolean.TRUE.toString());
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_REQUEST_ID, "" + entity.getRequestId());
        request = org.apache.ambari.server.controller.utilities.PropertyHelper.getUpdateRequest(requestProps, null);
        upgradeResourceProvider.updateResources(request, null);
        tasks = dao.findByRequest(entity.getRequestId());
        for (org.apache.ambari.server.orm.entities.HostRoleCommandEntity task : tasks) {
            if (task.getRoleCommand() == org.apache.ambari.server.RoleCommand.SERVICE_CHECK) {
                org.apache.ambari.server.orm.entities.StageEntity stage = task.getStage();
                if (stage.isSkippable() && stage.isAutoSkipOnFailureSupported()) {
                    org.junit.Assert.assertTrue(task.isFailureAutoSkipped());
                } else {
                    org.junit.Assert.assertFalse(task.isFailureAutoSkipped());
                }
            } else {
                org.junit.Assert.assertFalse(task.isFailureAutoSkipped());
            }
        }
        requestProps = new java.util.HashMap<>();
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_CLUSTER_NAME, "c1");
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_REPO_VERSION_ID, java.lang.String.valueOf(repoVersionEntity2200.getId()));
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_SKIP_FAILURES, java.lang.Boolean.FALSE.toString());
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_SKIP_SC_FAILURES, java.lang.Boolean.FALSE.toString());
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_REQUEST_ID, "" + entity.getRequestId());
        request = org.apache.ambari.server.controller.utilities.PropertyHelper.getUpdateRequest(requestProps, null);
        upgradeResourceProvider.updateResources(request, null);
        tasks = dao.findByRequest(entity.getRequestId());
        for (org.apache.ambari.server.orm.entities.HostRoleCommandEntity task : tasks) {
            org.junit.Assert.assertFalse(task.isFailureAutoSkipped());
        }
    }

    @org.junit.Test
    public void testRollback() throws java.lang.Exception {
        org.apache.ambari.server.state.Cluster cluster = clusters.getCluster("c1");
        java.util.Map<java.lang.String, java.lang.Object> requestProps = new java.util.HashMap<>();
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_CLUSTER_NAME, "c1");
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_REPO_VERSION_ID, java.lang.String.valueOf(repoVersionEntity2200.getId()));
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_PACK, "upgrade_test");
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_TYPE, org.apache.ambari.spi.upgrade.UpgradeType.ROLLING.toString());
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_SKIP_MANUAL_VERIFICATION, java.lang.Boolean.FALSE.toString());
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_SKIP_PREREQUISITE_CHECKS, java.lang.Boolean.TRUE.toString());
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_DIRECTION, org.apache.ambari.server.stack.upgrade.Direction.UPGRADE.name());
        org.apache.ambari.server.controller.internal.UpgradeResourceProvider upgradeResourceProvider = createProvider(amc);
        org.apache.ambari.server.controller.internal.UpgradeResourceProvider.s_upgradeDAO = null;
        try {
            org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getCreateRequest(java.util.Collections.singleton(requestProps), null);
            upgradeResourceProvider.createResources(request);
            org.junit.Assert.fail("Expected a NullPointerException");
        } catch (java.lang.NullPointerException npe) {
        }
        java.util.List<org.apache.ambari.server.orm.entities.UpgradeEntity> upgrades = upgradeDao.findUpgrades(cluster.getClusterId());
        org.junit.Assert.assertEquals(0, upgrades.size());
        java.util.List<java.lang.Long> requestIds = requestDao.findAllRequestIds(1, true, cluster.getClusterId());
        org.junit.Assert.assertEquals(0, requestIds.size());
    }

    @org.junit.Test
    public void testCreateHostOrderedUpgradeThrowsExceptions() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.lang.Object> requestProps = new java.util.HashMap<>();
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_CLUSTER_NAME, "c1");
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_REPO_VERSION_ID, java.lang.String.valueOf(repoVersionEntity2200.getId()));
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_PACK, "upgrade_test_host_ordered");
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_TYPE, org.apache.ambari.spi.upgrade.UpgradeType.HOST_ORDERED.toString());
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_SKIP_PREREQUISITE_CHECKS, java.lang.Boolean.TRUE.toString());
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_DIRECTION, org.apache.ambari.server.stack.upgrade.Direction.UPGRADE.name());
        org.apache.ambari.server.controller.spi.ResourceProvider upgradeResourceProvider = createProvider(amc);
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getCreateRequest(java.util.Collections.singleton(requestProps), null);
        try {
            upgradeResourceProvider.createResources(request);
            org.junit.Assert.fail("The request should have failed due to the missing Upgrade/host_order property");
        } catch (org.apache.ambari.server.controller.spi.SystemException systemException) {
        }
        java.util.Set<java.util.Map<java.lang.String, java.util.List<java.lang.String>>> hostsOrder = new java.util.LinkedHashSet<>();
        java.util.Map<java.lang.String, java.util.List<java.lang.String>> hostGrouping = new java.util.HashMap<>();
        hostGrouping.put("hosts", com.google.common.collect.Lists.newArrayList("invalid-host"));
        hostsOrder.add(hostGrouping);
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_HOST_ORDERED_HOSTS, hostsOrder);
        try {
            upgradeResourceProvider.createResources(request);
            org.junit.Assert.fail("The request should have failed due to invalid hosts");
        } catch (org.apache.ambari.server.controller.spi.SystemException systemException) {
        }
        hostsOrder = new java.util.LinkedHashSet<>();
        hostGrouping = new java.util.HashMap<>();
        hostGrouping.put("hosts", com.google.common.collect.Lists.newArrayList("h1"));
        hostsOrder.add(hostGrouping);
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_HOST_ORDERED_HOSTS, hostsOrder);
        upgradeResourceProvider.createResources(request);
        org.apache.ambari.server.state.Cluster cluster = clusters.getCluster("c1");
        org.junit.Assert.assertNotNull(cluster);
        org.apache.ambari.server.state.Service service = cluster.getService("ZOOKEEPER");
        org.junit.Assert.assertEquals(repoVersionEntity2200, service.getDesiredRepositoryVersion());
    }

    @org.junit.Test
    public void testCreateUpgradeDowngradeCycleAdvertisingVersion() throws java.lang.Exception {
        org.apache.ambari.server.state.Cluster cluster = clusters.getCluster("c1");
        org.apache.ambari.server.state.Service service = cluster.addService("STORM", repoVersionEntity2110);
        org.apache.ambari.server.state.ServiceComponent component = service.addServiceComponent("DRPC_SERVER");
        org.apache.ambari.server.state.ServiceComponentHost sch = component.addServiceComponentHost("h1");
        sch.setVersion("2.1.1.0");
        org.apache.ambari.server.controller.spi.ResourceProvider upgradeResourceProvider = createProvider(amc);
        java.util.Map<java.lang.String, java.lang.Object> requestProps = new java.util.HashMap<>();
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_CLUSTER_NAME, "c1");
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_REPO_VERSION_ID, java.lang.String.valueOf(repoVersionEntity2200.getId()));
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_PACK, "upgrade_test");
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_SKIP_PREREQUISITE_CHECKS, "true");
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_DIRECTION, org.apache.ambari.server.stack.upgrade.Direction.UPGRADE.name());
        java.util.Map<java.lang.String, java.lang.String> requestInfoProperties = new java.util.HashMap<>();
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getCreateRequest(java.util.Collections.singleton(requestProps), requestInfoProperties);
        org.apache.ambari.server.controller.spi.RequestStatus status = upgradeResourceProvider.createResources(request);
        org.junit.Assert.assertEquals(1, status.getAssociatedResources().size());
        org.apache.ambari.server.controller.spi.Resource r = status.getAssociatedResources().iterator().next();
        java.lang.String id = r.getPropertyValue("Upgrade/request_id").toString();
        component = service.getServiceComponent("DRPC_SERVER");
        org.junit.Assert.assertNotNull(component);
        org.junit.Assert.assertEquals("2.2.0.0", component.getDesiredVersion());
        org.apache.ambari.server.state.ServiceComponentHost hostComponent = component.getServiceComponentHost("h1");
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.UpgradeState.IN_PROGRESS, hostComponent.getUpgradeState());
        abortUpgrade(java.lang.Long.parseLong(id));
        requestProps.clear();
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_CLUSTER_NAME, "c1");
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_PACK, "upgrade_test");
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_SKIP_PREREQUISITE_CHECKS, "true");
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_DIRECTION, org.apache.ambari.server.stack.upgrade.Direction.DOWNGRADE.name());
        request = org.apache.ambari.server.controller.utilities.PropertyHelper.getCreateRequest(java.util.Collections.singleton(requestProps), requestInfoProperties);
        status = upgradeResourceProvider.createResources(request);
        component = service.getServiceComponent("DRPC_SERVER");
        org.junit.Assert.assertNotNull(component);
        org.junit.Assert.assertEquals(repoVersionEntity2110, component.getDesiredRepositoryVersion());
        hostComponent = component.getServiceComponentHost("h1");
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.UpgradeState.NONE, hostComponent.getUpgradeState());
        org.junit.Assert.assertEquals("UNKNOWN", hostComponent.getVersion());
    }

    @org.junit.Test
    public void testEmptyGroupingsDoNotSkipStageIds() throws java.lang.Exception {
        org.apache.ambari.server.orm.dao.StageDAO stageDao = injector.getInstance(org.apache.ambari.server.orm.dao.StageDAO.class);
        org.junit.Assert.assertEquals(0, stageDao.findAll().size());
        org.apache.ambari.server.state.Cluster cluster = clusters.getCluster("c1");
        java.util.List<org.apache.ambari.server.state.ServiceComponentHost> schs = cluster.getServiceComponentHosts("h1");
        for (org.apache.ambari.server.state.ServiceComponentHost sch : schs) {
            if (sch.isClientComponent()) {
                continue;
            }
            cluster.removeServiceComponentHost(sch);
        }
        java.util.Set<java.util.Map<java.lang.String, java.util.List<java.lang.String>>> hostsOrder = new java.util.LinkedHashSet<>();
        java.util.Map<java.lang.String, java.util.List<java.lang.String>> hostGrouping = new java.util.HashMap<>();
        hostGrouping = new java.util.HashMap<>();
        hostGrouping.put("hosts", com.google.common.collect.Lists.newArrayList("h1"));
        hostsOrder.add(hostGrouping);
        java.util.Map<java.lang.String, java.lang.Object> requestProps = new java.util.HashMap<>();
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_CLUSTER_NAME, "c1");
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_REPO_VERSION_ID, java.lang.String.valueOf(repoVersionEntity2200.getId()));
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_PACK, "upgrade_test_host_ordered");
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_TYPE, org.apache.ambari.spi.upgrade.UpgradeType.HOST_ORDERED.toString());
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_SKIP_PREREQUISITE_CHECKS, java.lang.Boolean.TRUE.toString());
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_DIRECTION, org.apache.ambari.server.stack.upgrade.Direction.UPGRADE.name());
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_HOST_ORDERED_HOSTS, hostsOrder);
        org.apache.ambari.server.controller.spi.ResourceProvider upgradeResourceProvider = createProvider(amc);
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getCreateRequest(java.util.Collections.singleton(requestProps), null);
        upgradeResourceProvider.createResources(request);
        java.util.List<org.apache.ambari.server.orm.entities.StageEntity> stages = stageDao.findByRequestId(cluster.getUpgradeInProgress().getRequestId());
        org.junit.Assert.assertEquals(3, stages.size());
        long expectedStageId = 1L;
        for (org.apache.ambari.server.orm.entities.StageEntity stage : stages) {
            org.junit.Assert.assertEquals(expectedStageId++, stage.getStageId().longValue());
        }
    }

    @org.junit.Test
    public void testUpgradeHistory() throws java.lang.Exception {
        org.apache.ambari.server.state.Cluster cluster = clusters.getCluster("c1");
        java.util.Map<java.lang.String, java.lang.Object> requestProps = new java.util.HashMap<>();
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_CLUSTER_NAME, "c1");
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_REPO_VERSION_ID, java.lang.String.valueOf(repoVersionEntity2200.getId()));
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_PACK, "upgrade_test");
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_TYPE, org.apache.ambari.spi.upgrade.UpgradeType.ROLLING.toString());
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_SKIP_MANUAL_VERIFICATION, java.lang.Boolean.TRUE.toString());
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_SKIP_PREREQUISITE_CHECKS, java.lang.Boolean.TRUE.toString());
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_DIRECTION, org.apache.ambari.server.stack.upgrade.Direction.UPGRADE.name());
        org.apache.ambari.server.controller.spi.ResourceProvider upgradeResourceProvider = createProvider(amc);
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getCreateRequest(java.util.Collections.singleton(requestProps), null);
        upgradeResourceProvider.createResources(request);
        java.util.List<org.apache.ambari.server.orm.entities.UpgradeEntity> upgrades = upgradeDao.findUpgrades(cluster.getClusterId());
        org.junit.Assert.assertEquals(1, upgrades.size());
        org.apache.ambari.server.orm.entities.UpgradeEntity upgrade = cluster.getUpgradeInProgress();
        java.util.List<org.apache.ambari.server.orm.entities.UpgradeHistoryEntity> histories = upgrade.getHistory();
        org.junit.Assert.assertEquals(2, histories.size());
        for (org.apache.ambari.server.orm.entities.UpgradeHistoryEntity history : histories) {
            org.junit.Assert.assertEquals("ZOOKEEPER", history.getServiceName());
            org.junit.Assert.assertEquals(repoVersionEntity2110, history.getFromReposistoryVersion());
            org.junit.Assert.assertEquals(repoVersionEntity2200, history.getTargetRepositoryVersion());
        }
        abortUpgrade(upgrade.getRequestId());
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_CLUSTER_NAME, "c1");
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_PACK, "upgrade_nonrolling_new_stack");
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_SKIP_PREREQUISITE_CHECKS, "true");
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_DIRECTION, org.apache.ambari.server.stack.upgrade.Direction.DOWNGRADE.name());
        java.util.Map<java.lang.String, java.lang.String> requestInfoProperties = new java.util.HashMap<>();
        request = org.apache.ambari.server.controller.utilities.PropertyHelper.getCreateRequest(java.util.Collections.singleton(requestProps), requestInfoProperties);
        org.apache.ambari.server.controller.spi.RequestStatus status = upgradeResourceProvider.createResources(request);
        org.apache.ambari.server.orm.entities.UpgradeEntity downgrade = upgradeDao.findUpgradeByRequestId(getRequestId(status));
        org.junit.Assert.assertEquals(org.apache.ambari.server.stack.upgrade.Direction.DOWNGRADE, downgrade.getDirection());
        histories = downgrade.getHistory();
        org.junit.Assert.assertEquals(2, histories.size());
        for (org.apache.ambari.server.orm.entities.UpgradeHistoryEntity history : histories) {
            org.junit.Assert.assertEquals("ZOOKEEPER", history.getServiceName());
            org.junit.Assert.assertEquals(repoVersionEntity2200, history.getFromReposistoryVersion());
            org.junit.Assert.assertEquals(repoVersionEntity2110, history.getTargetRepositoryVersion());
        }
    }

    @org.junit.Test
    public void testCreatePatchRevertUpgrade() throws java.lang.Exception {
        org.apache.ambari.server.state.Cluster cluster = clusters.getCluster("c1");
        org.apache.ambari.server.state.Service service = cluster.addService("HBASE", repoVersionEntity2110);
        org.apache.ambari.server.state.ServiceComponent component = service.addServiceComponent("HBASE_MASTER");
        org.apache.ambari.server.state.ServiceComponentHost sch = component.addServiceComponentHost("h1");
        sch.setVersion("2.1.1.0");
        java.io.File f = new java.io.File("src/test/resources/hbase_version_test.xml");
        repoVersionEntity2112.setType(org.apache.ambari.spi.RepositoryType.PATCH);
        repoVersionEntity2112.setVersionXml(org.apache.commons.io.IOUtils.toString(new java.io.FileInputStream(f)));
        repoVersionEntity2112.setVersionXsd("version_definition.xsd");
        repoVersionDao.merge(repoVersionEntity2112);
        java.util.List<org.apache.ambari.server.orm.entities.UpgradeEntity> upgrades = upgradeDao.findUpgrades(cluster.getClusterId());
        org.junit.Assert.assertEquals(0, upgrades.size());
        java.util.Map<java.lang.String, java.lang.Object> requestProps = new java.util.HashMap<>();
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_CLUSTER_NAME, "c1");
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_REPO_VERSION_ID, java.lang.String.valueOf(repoVersionEntity2112.getId()));
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_PACK, "upgrade_test");
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_SKIP_PREREQUISITE_CHECKS, "true");
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_DIRECTION, org.apache.ambari.server.stack.upgrade.Direction.UPGRADE.name());
        org.apache.ambari.server.controller.spi.ResourceProvider upgradeResourceProvider = createProvider(amc);
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getCreateRequest(java.util.Collections.singleton(requestProps), null);
        upgradeResourceProvider.createResources(request);
        upgrades = upgradeDao.findUpgrades(cluster.getClusterId());
        org.junit.Assert.assertEquals(1, upgrades.size());
        org.apache.ambari.server.orm.entities.UpgradeEntity upgradeEntity = upgrades.get(0);
        org.junit.Assert.assertEquals(org.apache.ambari.spi.RepositoryType.PATCH, upgradeEntity.getOrchestration());
        org.junit.Assert.assertEquals(false, upgradeEntity.isRevertAllowed());
        upgradeEntity.setRevertAllowed(true);
        upgradeEntity = upgradeDao.merge(upgradeEntity);
        cluster.setUpgradeEntity(null);
        requestProps = new java.util.HashMap<>();
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_CLUSTER_NAME, "c1");
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_REVERT_UPGRADE_ID, upgradeEntity.getId());
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_SKIP_PREREQUISITE_CHECKS, java.lang.Boolean.TRUE.toString());
        request = org.apache.ambari.server.controller.utilities.PropertyHelper.getCreateRequest(java.util.Collections.singleton(requestProps), null);
        upgradeResourceProvider.createResources(request);
        upgrades = upgradeDao.findUpgrades(cluster.getClusterId());
        org.junit.Assert.assertEquals(2, upgrades.size());
        boolean found = false;
        com.google.common.base.Function<org.apache.ambari.server.orm.entities.UpgradeHistoryEntity, java.lang.String> function = new com.google.common.base.Function<org.apache.ambari.server.orm.entities.UpgradeHistoryEntity, java.lang.String>() {
            @java.lang.Override
            public java.lang.String apply(org.apache.ambari.server.orm.entities.UpgradeHistoryEntity input) {
                return (input.getServiceName() + "/") + input.getComponentName();
            }
        };
        for (org.apache.ambari.server.orm.entities.UpgradeEntity upgrade : upgrades) {
            if (upgrade.getId() != upgradeEntity.getId()) {
                found = true;
                org.junit.Assert.assertEquals(upgradeEntity.getOrchestration(), upgrade.getOrchestration());
                java.util.Collection<java.lang.String> upgradeEntityStrings = com.google.common.collect.Collections2.transform(upgradeEntity.getHistory(), function);
                java.util.Collection<java.lang.String> upgradeStrings = com.google.common.collect.Collections2.transform(upgrade.getHistory(), function);
                java.util.Collection<?> diff = org.apache.commons.collections.CollectionUtils.disjunction(upgradeEntityStrings, upgradeStrings);
                org.junit.Assert.assertEquals("Verify the same set of components was orchestrated", 0, diff.size());
            }
        }
        org.junit.Assert.assertTrue(found);
    }

    @org.junit.Test(expected = org.apache.ambari.server.controller.spi.SystemException.class)
    public void testRevertFailsWhenNoRevertableUpgradeIsFound() throws java.lang.Exception {
        org.apache.ambari.server.state.Cluster cluster = clusters.getCluster("c1");
        org.apache.ambari.server.state.Service service = cluster.addService("HBASE", repoVersionEntity2110);
        org.apache.ambari.server.state.ServiceComponent component = service.addServiceComponent("HBASE_MASTER");
        org.apache.ambari.server.state.ServiceComponentHost sch = component.addServiceComponentHost("h1");
        sch.setVersion("2.1.1.0");
        java.io.File f = new java.io.File("src/test/resources/hbase_version_test.xml");
        repoVersionEntity2112.setType(org.apache.ambari.spi.RepositoryType.PATCH);
        repoVersionEntity2112.setVersionXml(org.apache.commons.io.IOUtils.toString(new java.io.FileInputStream(f)));
        repoVersionEntity2112.setVersionXsd("version_definition.xsd");
        repoVersionDao.merge(repoVersionEntity2112);
        java.util.List<org.apache.ambari.server.orm.entities.UpgradeEntity> upgrades = upgradeDao.findUpgrades(cluster.getClusterId());
        org.junit.Assert.assertEquals(0, upgrades.size());
        java.util.Map<java.lang.String, java.lang.Object> requestProps = new java.util.HashMap<>();
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_CLUSTER_NAME, "c1");
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_REPO_VERSION_ID, java.lang.String.valueOf(repoVersionEntity2112.getId()));
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_PACK, "upgrade_test");
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_SKIP_PREREQUISITE_CHECKS, "true");
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_DIRECTION, org.apache.ambari.server.stack.upgrade.Direction.UPGRADE.name());
        org.apache.ambari.server.controller.spi.ResourceProvider upgradeResourceProvider = createProvider(amc);
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getCreateRequest(java.util.Collections.singleton(requestProps), null);
        upgradeResourceProvider.createResources(request);
        upgrades = upgradeDao.findUpgrades(cluster.getClusterId());
        org.junit.Assert.assertEquals(1, upgrades.size());
        org.apache.ambari.server.orm.entities.UpgradeEntity upgradeEntity = upgrades.get(0);
        org.junit.Assert.assertEquals(org.apache.ambari.spi.RepositoryType.PATCH, upgradeEntity.getOrchestration());
        cluster.setUpgradeEntity(null);
        requestProps = new java.util.HashMap<>();
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_CLUSTER_NAME, "c1");
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_REVERT_UPGRADE_ID, upgradeEntity.getId());
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_SKIP_PREREQUISITE_CHECKS, java.lang.Boolean.TRUE.toString());
        request = org.apache.ambari.server.controller.utilities.PropertyHelper.getCreateRequest(java.util.Collections.singleton(requestProps), null);
        upgradeResourceProvider.createResources(request);
    }

    @org.junit.Test
    public void testCreatePatchWithConfigChanges() throws java.lang.Exception {
        org.apache.ambari.server.state.Cluster cluster = clusters.getCluster("c1");
        java.io.File f = new java.io.File("src/test/resources/version_definition_test_patch_config.xml");
        repoVersionEntity2112.setType(org.apache.ambari.spi.RepositoryType.PATCH);
        repoVersionEntity2112.setVersionXml(org.apache.commons.io.IOUtils.toString(new java.io.FileInputStream(f)));
        repoVersionEntity2112.setVersionXsd("version_definition.xsd");
        repoVersionDao.merge(repoVersionEntity2112);
        java.util.List<org.apache.ambari.server.orm.entities.UpgradeEntity> upgrades = upgradeDao.findUpgrades(cluster.getClusterId());
        org.junit.Assert.assertEquals(0, upgrades.size());
        java.util.Map<java.lang.String, java.lang.Object> requestProps = new java.util.HashMap<>();
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_CLUSTER_NAME, "c1");
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_REPO_VERSION_ID, java.lang.String.valueOf(repoVersionEntity2112.getId()));
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_PACK, "upgrade_test");
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_SKIP_PREREQUISITE_CHECKS, "true");
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_DIRECTION, org.apache.ambari.server.stack.upgrade.Direction.UPGRADE.name());
        org.apache.ambari.server.controller.spi.ResourceProvider upgradeResourceProvider = createProvider(amc);
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getCreateRequest(java.util.Collections.singleton(requestProps), null);
        org.apache.ambari.server.controller.spi.RequestStatus status = upgradeResourceProvider.createResources(request);
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = status.getAssociatedResources();
        org.junit.Assert.assertEquals(1, resources.size());
        java.lang.Long requestId = ((java.lang.Long) (resources.iterator().next().getPropertyValue("Upgrade/request_id")));
        org.junit.Assert.assertNotNull(requestId);
        org.apache.ambari.server.orm.entities.UpgradeEntity upgradeEntity = upgradeDao.findUpgradeByRequestId(requestId);
        org.junit.Assert.assertEquals(org.apache.ambari.spi.RepositoryType.PATCH, upgradeEntity.getOrchestration());
        org.apache.ambari.server.orm.dao.HostRoleCommandDAO hrcDAO = injector.getInstance(org.apache.ambari.server.orm.dao.HostRoleCommandDAO.class);
        java.util.List<org.apache.ambari.server.orm.entities.HostRoleCommandEntity> commands = hrcDAO.findByRequest(upgradeEntity.getRequestId());
        boolean foundConfigTask = false;
        for (org.apache.ambari.server.orm.entities.HostRoleCommandEntity command : commands) {
            if (org.apache.commons.lang3.StringUtils.isNotBlank(command.getCustomCommandName()) && command.getCustomCommandName().equals(org.apache.ambari.server.serveraction.upgrades.ConfigureAction.class.getName())) {
                foundConfigTask = true;
                break;
            }
        }
        org.junit.Assert.assertFalse(foundConfigTask);
        cluster.setUpgradeEntity(null);
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_PACK, "upgrade_test_force_config_change");
        request = org.apache.ambari.server.controller.utilities.PropertyHelper.getCreateRequest(java.util.Collections.singleton(requestProps), null);
        status = upgradeResourceProvider.createResources(request);
        resources = status.getAssociatedResources();
        org.junit.Assert.assertEquals(1, resources.size());
        requestId = ((java.lang.Long) (resources.iterator().next().getPropertyValue("Upgrade/request_id")));
        org.junit.Assert.assertNotNull(requestId);
        upgradeEntity = upgradeDao.findUpgradeByRequestId(requestId);
        org.junit.Assert.assertEquals(org.apache.ambari.spi.RepositoryType.PATCH, upgradeEntity.getOrchestration());
        commands = hrcDAO.findByRequest(upgradeEntity.getRequestId());
        foundConfigTask = false;
        for (org.apache.ambari.server.orm.entities.HostRoleCommandEntity command : commands) {
            if (org.apache.commons.lang3.StringUtils.isNotBlank(command.getCustomCommandName()) && command.getCustomCommandName().equals(org.apache.ambari.server.serveraction.upgrades.ConfigureAction.class.getName())) {
                foundConfigTask = true;
                org.apache.ambari.server.orm.dao.ExecutionCommandDAO dao = injector.getInstance(org.apache.ambari.server.orm.dao.ExecutionCommandDAO.class);
                org.apache.ambari.server.orm.entities.ExecutionCommandEntity entity = dao.findByPK(command.getTaskId());
                org.apache.ambari.server.actionmanager.ExecutionCommandWrapperFactory factory = injector.getInstance(org.apache.ambari.server.actionmanager.ExecutionCommandWrapperFactory.class);
                org.apache.ambari.server.actionmanager.ExecutionCommandWrapper wrapper = factory.createFromJson(new java.lang.String(entity.getCommand()));
                java.util.Map<java.lang.String, java.lang.String> params = wrapper.getExecutionCommand().getCommandParams();
                org.junit.Assert.assertTrue(params.containsKey(org.apache.ambari.server.stack.upgrade.ConfigureTask.PARAMETER_ASSOCIATED_SERVICE));
                org.junit.Assert.assertEquals("ZOOKEEPER", params.get(org.apache.ambari.server.stack.upgrade.ConfigureTask.PARAMETER_ASSOCIATED_SERVICE));
                break;
            }
        }
        org.junit.Assert.assertTrue(foundConfigTask);
        cluster.setUpgradeEntity(null);
        repoVersionEntity2112.setType(org.apache.ambari.spi.RepositoryType.STANDARD);
        repoVersionDao.merge(repoVersionEntity2112);
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_PACK, "upgrade_test");
        request = org.apache.ambari.server.controller.utilities.PropertyHelper.getCreateRequest(java.util.Collections.singleton(requestProps), null);
        status = upgradeResourceProvider.createResources(request);
        resources = status.getAssociatedResources();
        org.junit.Assert.assertEquals(1, resources.size());
        requestId = ((java.lang.Long) (resources.iterator().next().getPropertyValue("Upgrade/request_id")));
        org.junit.Assert.assertNotNull(requestId);
        upgradeEntity = upgradeDao.findUpgradeByRequestId(requestId);
        org.junit.Assert.assertEquals(org.apache.ambari.spi.RepositoryType.STANDARD, upgradeEntity.getOrchestration());
        commands = hrcDAO.findByRequest(upgradeEntity.getRequestId());
        foundConfigTask = false;
        for (org.apache.ambari.server.orm.entities.HostRoleCommandEntity command : commands) {
            if (org.apache.commons.lang3.StringUtils.isNotBlank(command.getCustomCommandName()) && command.getCustomCommandName().equals(org.apache.ambari.server.serveraction.upgrades.ConfigureAction.class.getName())) {
                foundConfigTask = true;
                break;
            }
        }
        org.junit.Assert.assertTrue(foundConfigTask);
    }

    private java.lang.String parseSingleMessage(java.lang.String msgStr) {
        com.google.gson.JsonParser parser = new com.google.gson.JsonParser();
        com.google.gson.JsonArray msgArray = ((com.google.gson.JsonArray) (parser.parse(msgStr)));
        com.google.gson.JsonObject msg = ((com.google.gson.JsonObject) (msgArray.get(0)));
        return msg.get("message").getAsString();
    }

    private void abortUpgrade(long requestId) throws java.lang.Exception {
        java.util.Map<java.lang.String, java.lang.Object> requestProps = new java.util.HashMap<>();
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_REQUEST_ID, java.lang.String.valueOf(requestId));
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_CLUSTER_NAME, "c1");
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_REQUEST_STATUS, "ABORTED");
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_SUSPENDED, "false");
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getUpdateRequest(requestProps, null);
        org.apache.ambari.server.controller.spi.ResourceProvider upgradeResourceProvider = createProvider(amc);
        upgradeResourceProvider.updateResources(request, null);
        hrcDAO.updateStatusByRequestId(requestId, org.apache.ambari.server.actionmanager.HostRoleStatus.ABORTED, org.apache.ambari.server.actionmanager.HostRoleStatus.IN_PROGRESS_STATUSES);
    }

    @org.junit.Test
    public void testTimeouts() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.StackEntity stackEntity = stackDAO.find("HDP", "2.1.1");
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repoVersionEntity = new org.apache.ambari.server.orm.entities.RepositoryVersionEntity();
        repoVersionEntity.setDisplayName("My New Version 3");
        java.util.List<org.apache.ambari.server.orm.entities.RepoOsEntity> operatingSystems = new java.util.ArrayList<>();
        org.apache.ambari.server.orm.entities.RepoDefinitionEntity repoDefinitionEntity1 = new org.apache.ambari.server.orm.entities.RepoDefinitionEntity();
        repoDefinitionEntity1.setRepoID("HDP-UTILS");
        repoDefinitionEntity1.setBaseUrl("");
        repoDefinitionEntity1.setRepoName("HDP-UTILS");
        org.apache.ambari.server.orm.entities.RepoDefinitionEntity repoDefinitionEntity2 = new org.apache.ambari.server.orm.entities.RepoDefinitionEntity();
        repoDefinitionEntity2.setRepoID("HDP");
        repoDefinitionEntity2.setBaseUrl("");
        repoDefinitionEntity2.setRepoName("HDP");
        org.apache.ambari.server.orm.entities.RepoOsEntity repoOsEntity = new org.apache.ambari.server.orm.entities.RepoOsEntity();
        repoOsEntity.setFamily("redhat6");
        repoOsEntity.setAmbariManaged(true);
        repoOsEntity.addRepoDefinition(repoDefinitionEntity1);
        repoOsEntity.addRepoDefinition(repoDefinitionEntity2);
        operatingSystems.add(repoOsEntity);
        repoVersionEntity.addRepoOsEntities(operatingSystems);
        repoVersionEntity.setStack(stackEntity);
        repoVersionEntity.setVersion("2.2.2.3");
        repoVersionDao.create(repoVersionEntity);
        java.util.Map<java.lang.String, java.lang.Object> requestProps = new java.util.HashMap<>();
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_CLUSTER_NAME, "c1");
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_REPO_VERSION_ID, java.lang.String.valueOf(repoVersionEntity.getId()));
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_PACK, "upgrade_test");
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_SKIP_PREREQUISITE_CHECKS, "true");
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_DIRECTION, org.apache.ambari.server.stack.upgrade.Direction.UPGRADE.name());
        org.apache.ambari.server.controller.spi.ResourceProvider upgradeResourceProvider = createProvider(amc);
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getCreateRequest(java.util.Collections.singleton(requestProps), null);
        org.apache.ambari.server.controller.spi.RequestStatus status = upgradeResourceProvider.createResources(request);
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> createdResources = status.getAssociatedResources();
        org.junit.Assert.assertEquals(1, createdResources.size());
        org.apache.ambari.server.controller.spi.Resource res = createdResources.iterator().next();
        java.lang.Long id = ((java.lang.Long) (res.getPropertyValue("Upgrade/request_id")));
        org.junit.Assert.assertNotNull(id);
        org.junit.Assert.assertEquals(java.lang.Long.valueOf(1), id);
        org.apache.ambari.server.actionmanager.ActionManager am = injector.getInstance(org.apache.ambari.server.actionmanager.ActionManager.class);
        java.util.List<org.apache.ambari.server.actionmanager.HostRoleCommand> commands = am.getRequestTasks(id);
        boolean found = false;
        for (org.apache.ambari.server.actionmanager.HostRoleCommand command : commands) {
            org.apache.ambari.server.actionmanager.ExecutionCommandWrapper wrapper = command.getExecutionCommandWrapper();
            if (command.getRole().equals(org.apache.ambari.server.Role.ZOOKEEPER_SERVER) && command.getRoleCommand().equals(org.apache.ambari.server.RoleCommand.CUSTOM_COMMAND)) {
                java.util.Map<java.lang.String, java.lang.String> commandParams = wrapper.getExecutionCommand().getCommandParams();
                org.junit.Assert.assertTrue(commandParams.containsKey(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.COMMAND_TIMEOUT));
                org.junit.Assert.assertEquals("824", commandParams.get(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.COMMAND_TIMEOUT));
                found = true;
            }
        }
        org.junit.Assert.assertTrue("ZooKeeper timeout override was found", found);
    }

    @org.junit.Test
    public void testExecutionCommandServiceAndComponent() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.lang.Object> requestProps = new java.util.HashMap<>();
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_CLUSTER_NAME, "c1");
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_REPO_VERSION_ID, java.lang.String.valueOf(repoVersionEntity2200.getId()));
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_PACK, "upgrade_execute_task_test");
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_SKIP_PREREQUISITE_CHECKS, "true");
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_DIRECTION, org.apache.ambari.server.stack.upgrade.Direction.UPGRADE.name());
        org.apache.ambari.server.controller.spi.ResourceProvider upgradeResourceProvider = createProvider(amc);
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getCreateRequest(java.util.Collections.singleton(requestProps), null);
        org.apache.ambari.server.controller.spi.RequestStatus status = upgradeResourceProvider.createResources(request);
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> createdResources = status.getAssociatedResources();
        org.junit.Assert.assertEquals(1, createdResources.size());
        org.apache.ambari.server.controller.spi.Resource res = createdResources.iterator().next();
        java.lang.Long id = ((java.lang.Long) (res.getPropertyValue("Upgrade/request_id")));
        org.junit.Assert.assertNotNull(id);
        org.junit.Assert.assertEquals(java.lang.Long.valueOf(1), id);
        org.apache.ambari.server.actionmanager.ActionManager am = injector.getInstance(org.apache.ambari.server.actionmanager.ActionManager.class);
        java.util.List<org.apache.ambari.server.actionmanager.HostRoleCommand> commands = am.getRequestTasks(id);
        boolean foundActionExecuteCommand = false;
        for (org.apache.ambari.server.actionmanager.HostRoleCommand command : commands) {
            org.apache.ambari.server.agent.ExecutionCommand executionCommand = command.getExecutionCommandWrapper().getExecutionCommand();
            if (org.apache.commons.lang3.StringUtils.equals(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.EXECUTE_TASK_ROLE, executionCommand.getRole())) {
                foundActionExecuteCommand = true;
                org.junit.Assert.assertNotNull(executionCommand.getServiceName());
                org.junit.Assert.assertNotNull(executionCommand.getComponentName());
            }
        }
        org.junit.Assert.assertTrue("There was no task found with the role of " + org.apache.ambari.server.controller.internal.UpgradeResourceProvider.EXECUTE_TASK_ROLE, foundActionExecuteCommand);
    }

    @org.junit.Test
    public void testCreateRegenerateKeytabStages() throws java.lang.Exception {
        org.easymock.Capture<java.util.Map<java.lang.String, java.lang.String>> requestPropertyMapCapture = org.easymock.EasyMock.newCapture();
        java.util.Map<java.lang.String, java.lang.Object> requestProps = new java.util.HashMap<>();
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_CLUSTER_NAME, "c1");
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_REPO_VERSION_ID, java.lang.String.valueOf(repoVersionEntity2200.getId()));
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_PACK, "upgrade_test_regenerate_keytabs");
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_SKIP_PREREQUISITE_CHECKS, "true");
        requestProps.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_DIRECTION, org.apache.ambari.server.stack.upgrade.Direction.UPGRADE.name());
        cluster.setSecurityType(org.apache.ambari.server.state.SecurityType.KERBEROS);
        org.apache.ambari.server.controller.internal.RequestStageContainer requestStageContainer = createNiceMock(org.apache.ambari.server.controller.internal.RequestStageContainer.class);
        EasyMock.expect(requestStageContainer.getStages()).andReturn(com.google.common.collect.Lists.newArrayList()).once();
        EasyMock.expect(kerberosHelperMock.executeCustomOperations(EasyMock.eq(cluster), org.easymock.EasyMock.capture(requestPropertyMapCapture), org.easymock.EasyMock.anyObject(org.apache.ambari.server.controller.internal.RequestStageContainer.class), EasyMock.eq(null))).andReturn(requestStageContainer).once();
        replayAll();
        org.apache.ambari.server.controller.spi.ResourceProvider upgradeResourceProvider = createProvider(amc);
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getCreateRequest(java.util.Collections.singleton(requestProps), null);
        try {
            upgradeResourceProvider.createResources(request);
            org.junit.Assert.fail("The mock request stage container should have caused a problem in JPA");
        } catch (java.lang.IllegalArgumentException illegalArgumentException) {
        }
        verifyAll();
        java.util.Map<java.lang.String, java.lang.String> requestPropertyMap = requestPropertyMapCapture.getValue();
        org.junit.Assert.assertEquals("true", requestPropertyMap.get(org.apache.ambari.server.controller.KerberosHelper.ALLOW_RETRY));
        org.junit.Assert.assertEquals("missing", requestPropertyMap.get(org.apache.ambari.server.controller.KerberosHelperImpl.SupportedCustomOperation.REGENERATE_KEYTABS.name().toLowerCase()));
        org.junit.Assert.assertEquals(org.apache.ambari.server.controller.UpdateConfigurationPolicy.NEW_AND_IDENTITIES.name(), requestPropertyMap.get(org.apache.ambari.server.controller.KerberosHelper.DIRECTIVE_CONFIG_UPDATE_POLICY.toLowerCase()));
    }

    private class MockModule implements com.google.inject.Module {
        @java.lang.Override
        public void configure(com.google.inject.Binder binder) {
            binder.bind(org.apache.ambari.server.state.ConfigHelper.class).toInstance(configHelper);
            binder.bind(org.apache.ambari.server.agent.stomp.AgentConfigsHolder.class).toInstance(agentConfigsHolder);
            binder.bind(org.apache.ambari.server.controller.KerberosHelper.class).toInstance(kerberosHelperMock);
        }
    }
}