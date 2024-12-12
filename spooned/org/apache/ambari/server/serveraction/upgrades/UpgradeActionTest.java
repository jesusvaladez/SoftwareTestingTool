package org.apache.ambari.server.serveraction.upgrades;
import com.google.inject.persist.UnitOfWork;
import javax.persistence.EntityManager;
import org.apache.commons.lang.StringUtils;
public class UpgradeActionTest {
    private static final java.lang.String clusterName = "c1";

    private static final java.lang.String HDP_2_1_1_0 = "2.1.1.0-1";

    private static final java.lang.String HDP_2_1_1_1 = "2.1.1.1-2";

    private static final java.lang.String HDP_2_2_0_1 = "2.2.0.1-3";

    private static final org.apache.ambari.server.state.StackId HDP_21_STACK = new org.apache.ambari.server.state.StackId("HDP-2.1.1");

    private static final org.apache.ambari.server.state.StackId HDP_22_STACK = new org.apache.ambari.server.state.StackId("HDP-2.2.0");

    private org.apache.ambari.server.orm.entities.RepositoryVersionEntity sourceRepositoryVersion;

    private com.google.inject.Injector m_injector;

    private org.apache.ambari.server.controller.AmbariManagementController amc;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.OrmTestHelper m_helper;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.RepositoryVersionDAO repoVersionDAO;

    @com.google.inject.Inject
    private org.apache.ambari.server.state.Clusters clusters;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.HostVersionDAO hostVersionDAO;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.HostDAO hostDAO;

    @com.google.inject.Inject
    private org.apache.ambari.server.actionmanager.HostRoleCommandFactory hostRoleCommandFactory;

    @com.google.inject.Inject
    private org.apache.ambari.server.state.ServiceFactory serviceFactory;

    @com.google.inject.Inject
    private org.apache.ambari.server.state.ServiceComponentFactory serviceComponentFactory;

    @com.google.inject.Inject
    private org.apache.ambari.server.state.ServiceComponentHostFactory serviceComponentHostFactory;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.RequestDAO requestDAO;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.UpgradeDAO upgradeDAO;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.StackDAO stackDAO;

    @com.google.inject.Inject
    private org.apache.ambari.server.api.services.AmbariMetaInfo ambariMetaInfo;

    @com.google.inject.Inject
    private org.apache.ambari.server.serveraction.upgrades.FinalizeUpgradeAction finalizeUpgradeAction;

    @com.google.inject.Inject
    private org.apache.ambari.server.state.ConfigFactory configFactory;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.RepositoryVersionDAO repositoryVersionDAO;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.HostComponentStateDAO hostComponentStateDAO;

    private org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion2110;

    private org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion2111;

    private org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion2201;

    @org.junit.Before
    public void setup() throws java.lang.Exception {
        m_injector = com.google.inject.Guice.createInjector(new org.apache.ambari.server.orm.InMemoryDefaultTestModule());
        org.apache.ambari.server.utils.EventBusSynchronizer.synchronizeAmbariEventPublisher(m_injector);
        m_injector.getInstance(org.apache.ambari.server.orm.GuiceJpaInitializer.class);
        m_injector.injectMembers(this);
        m_injector.getInstance(com.google.inject.persist.UnitOfWork.class).begin();
        amc = m_injector.getInstance(org.apache.ambari.server.controller.AmbariManagementController.class);
        java.lang.reflect.Field field = org.apache.ambari.server.controller.AmbariServer.class.getDeclaredField("clusterController");
        field.setAccessible(true);
        field.set(null, amc);
        repositoryVersion2110 = m_helper.getOrCreateRepositoryVersion(org.apache.ambari.server.serveraction.upgrades.UpgradeActionTest.HDP_21_STACK, org.apache.ambari.server.serveraction.upgrades.UpgradeActionTest.HDP_2_1_1_0);
        repositoryVersion2111 = m_helper.getOrCreateRepositoryVersion(org.apache.ambari.server.serveraction.upgrades.UpgradeActionTest.HDP_21_STACK, org.apache.ambari.server.serveraction.upgrades.UpgradeActionTest.HDP_2_1_1_1);
        repositoryVersion2201 = m_helper.getOrCreateRepositoryVersion(org.apache.ambari.server.serveraction.upgrades.UpgradeActionTest.HDP_22_STACK, org.apache.ambari.server.serveraction.upgrades.UpgradeActionTest.HDP_2_2_0_1);
    }

    @org.junit.After
    public void teardown() throws java.lang.Exception {
        m_injector.getInstance(com.google.inject.persist.UnitOfWork.class).end();
        org.apache.ambari.server.H2DatabaseCleaner.clearDatabase(m_injector.getProvider(javax.persistence.EntityManager.class).get());
    }

    private void makeDowngradeCluster(org.apache.ambari.server.orm.entities.RepositoryVersionEntity sourceRepoVersion, org.apache.ambari.server.orm.entities.RepositoryVersionEntity targetRepoVersion) throws java.lang.Exception {
        java.lang.String hostName = "h1";
        clusters.addCluster(org.apache.ambari.server.serveraction.upgrades.UpgradeActionTest.clusterName, sourceRepoVersion.getStackId());
        clusters.addHost(hostName);
        org.apache.ambari.server.state.Host host = clusters.getHost(hostName);
        java.util.Map<java.lang.String, java.lang.String> hostAttributes = new java.util.HashMap<>();
        hostAttributes.put("os_family", "redhat");
        hostAttributes.put("os_release_version", "6");
        host.setHostAttributes(hostAttributes);
        org.apache.ambari.server.orm.entities.HostVersionEntity entity = new org.apache.ambari.server.orm.entities.HostVersionEntity();
        entity.setHostEntity(hostDAO.findByName(hostName));
        entity.setRepositoryVersion(targetRepoVersion);
        entity.setState(org.apache.ambari.server.state.RepositoryVersionState.INSTALLING);
        hostVersionDAO.create(entity);
    }

    private org.apache.ambari.server.state.Cluster createUpgradeCluster(org.apache.ambari.server.orm.entities.RepositoryVersionEntity sourceRepoVersion, java.lang.String hostName) throws java.lang.Exception {
        clusters.addCluster(org.apache.ambari.server.serveraction.upgrades.UpgradeActionTest.clusterName, sourceRepoVersion.getStackId());
        org.apache.ambari.server.state.Cluster cluster = clusters.getCluster(org.apache.ambari.server.serveraction.upgrades.UpgradeActionTest.clusterName);
        clusters.addHost(hostName);
        org.apache.ambari.server.state.Host host = clusters.getHost(hostName);
        java.util.Map<java.lang.String, java.lang.String> hostAttributes = new java.util.HashMap<>();
        hostAttributes.put("os_family", "redhat");
        hostAttributes.put("os_release_version", "6");
        host.setHostAttributes(hostAttributes);
        clusters.mapHostToCluster(hostName, org.apache.ambari.server.serveraction.upgrades.UpgradeActionTest.clusterName);
        org.apache.ambari.server.orm.entities.HostVersionEntity entity = new org.apache.ambari.server.orm.entities.HostVersionEntity(hostDAO.findByName(hostName), sourceRepoVersion, org.apache.ambari.server.state.RepositoryVersionState.INSTALLED);
        hostVersionDAO.create(entity);
        return cluster;
    }

    private void createHostVersions(org.apache.ambari.server.orm.entities.RepositoryVersionEntity targetRepoVersion, java.lang.String hostName) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.Cluster c = clusters.getCluster(org.apache.ambari.server.serveraction.upgrades.UpgradeActionTest.clusterName);
        org.apache.ambari.server.orm.dao.HostDAO hostDAO = m_injector.getInstance(org.apache.ambari.server.orm.dao.HostDAO.class);
        org.apache.ambari.server.orm.entities.HostVersionEntity entity = new org.apache.ambari.server.orm.entities.HostVersionEntity(hostDAO.findByName(hostName), targetRepoVersion, org.apache.ambari.server.state.RepositoryVersionState.INSTALLED);
        hostVersionDAO.create(entity);
        java.util.List<org.apache.ambari.server.orm.entities.HostVersionEntity> hostVersions = hostVersionDAO.findHostVersionByClusterAndRepository(c.getClusterId(), targetRepoVersion);
        org.junit.Assert.assertEquals(1, hostVersions.size());
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.RepositoryVersionState.INSTALLED, hostVersions.get(0).getState());
    }

    private void makeCrossStackUpgradeClusterAndSourceRepo(org.apache.ambari.server.state.StackId sourceStack, java.lang.String sourceRepo, java.lang.String hostName) throws java.lang.Exception {
        clusters.addCluster(org.apache.ambari.server.serveraction.upgrades.UpgradeActionTest.clusterName, sourceStack);
        org.apache.ambari.server.orm.entities.StackEntity stackEntitySource = stackDAO.find(sourceStack.getStackName(), sourceStack.getStackVersion());
        org.junit.Assert.assertNotNull(stackEntitySource);
        org.apache.ambari.server.state.Cluster c = clusters.getCluster(org.apache.ambari.server.serveraction.upgrades.UpgradeActionTest.clusterName);
        c.setCurrentStackVersion(sourceStack);
        c.setDesiredStackVersion(sourceStack);
        clusters.addHost(hostName);
        org.apache.ambari.server.state.Host host = clusters.getHost(hostName);
        java.util.Map<java.lang.String, java.lang.String> hostAttributes = new java.util.HashMap<>();
        hostAttributes.put("os_family", "redhat");
        hostAttributes.put("os_release_version", "6");
        host.setHostAttributes(hostAttributes);
        clusters.mapHostToCluster(hostName, org.apache.ambari.server.serveraction.upgrades.UpgradeActionTest.clusterName);
        clusters.updateHostMappings(host);
        sourceRepositoryVersion = m_helper.getOrCreateRepositoryVersion(sourceStack, sourceRepo);
    }

    private void makeCrossStackUpgradeTargetRepo(org.apache.ambari.server.state.StackId targetStack, java.lang.String targetRepo, java.lang.String hostName) throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.StackEntity stackEntityTarget = stackDAO.find(targetStack.getStackName(), targetStack.getStackVersion());
        org.junit.Assert.assertNotNull(stackEntityTarget);
        m_helper.getOrCreateRepositoryVersion(new org.apache.ambari.server.state.StackId(stackEntityTarget), targetRepo);
        org.apache.ambari.server.orm.dao.HostDAO hostDAO = m_injector.getInstance(org.apache.ambari.server.orm.dao.HostDAO.class);
        org.apache.ambari.server.orm.entities.HostVersionEntity entity = new org.apache.ambari.server.orm.entities.HostVersionEntity();
        entity.setHostEntity(hostDAO.findByName(hostName));
        entity.setRepositoryVersion(repoVersionDAO.findByStackAndVersion(targetStack, targetRepo));
        entity.setState(org.apache.ambari.server.state.RepositoryVersionState.INSTALLED);
        hostVersionDAO.create(entity);
    }

    @org.junit.Test
    public void testExpressUpgradeUpdateDesiredRepositoryAction() throws java.lang.Exception {
        org.apache.ambari.server.state.StackId sourceStack = org.apache.ambari.server.serveraction.upgrades.UpgradeActionTest.HDP_21_STACK;
        org.apache.ambari.server.state.StackId targetStack = org.apache.ambari.server.serveraction.upgrades.UpgradeActionTest.HDP_22_STACK;
        java.lang.String sourceRepo = org.apache.ambari.server.serveraction.upgrades.UpgradeActionTest.HDP_2_1_1_0;
        java.lang.String hostName = "h1";
        java.lang.String upgradePackName = "upgrade_nonrolling_new_stack";
        java.util.Map<java.lang.String, org.apache.ambari.server.stack.upgrade.UpgradePack> packs = ambariMetaInfo.getUpgradePacks(sourceStack.getStackName(), sourceStack.getStackVersion());
        org.junit.Assert.assertTrue(packs.containsKey(upgradePackName));
        makeCrossStackUpgradeClusterAndSourceRepo(sourceStack, sourceRepo, hostName);
        org.apache.ambari.server.state.Cluster cluster = clusters.getCluster(org.apache.ambari.server.serveraction.upgrades.UpgradeActionTest.clusterName);
        org.apache.ambari.server.state.Service zk = installService(cluster, "ZOOKEEPER", repositoryVersion2110);
        addServiceComponent(cluster, zk, "ZOOKEEPER_SERVER");
        addServiceComponent(cluster, zk, "ZOOKEEPER_CLIENT");
        createNewServiceComponentHost(cluster, "ZOOKEEPER", "ZOOKEEPER_SERVER", "h1");
        createNewServiceComponentHost(cluster, "ZOOKEEPER", "ZOOKEEPER_CLIENT", "h1");
        org.apache.ambari.server.state.Service hdfs = installService(cluster, "HDFS", repositoryVersion2110);
        addServiceComponent(cluster, hdfs, "NAMENODE");
        addServiceComponent(cluster, hdfs, "DATANODE");
        createNewServiceComponentHost(cluster, "HDFS", "NAMENODE", "h1");
        createNewServiceComponentHost(cluster, "HDFS", "DATANODE", "h1");
        makeCrossStackUpgradeTargetRepo(targetStack, repositoryVersion2201.getVersion(), hostName);
        createUpgrade(cluster, repositoryVersion2201);
        org.junit.Assert.assertNotNull(repositoryVersion2201);
        createConfigs(cluster);
        java.util.Collection<org.apache.ambari.server.state.Config> configs = cluster.getAllConfigs();
        org.junit.Assert.assertFalse(configs.isEmpty());
        java.util.Map<java.lang.String, java.lang.String> commandParams = new java.util.HashMap<>();
        org.apache.ambari.server.agent.ExecutionCommand executionCommand = new org.apache.ambari.server.agent.ExecutionCommand();
        executionCommand.setCommandParams(commandParams);
        java.util.Map<java.lang.String, java.lang.String> roleParams = new java.util.HashMap<>();
        java.lang.String userName = "admin";
        roleParams.put(org.apache.ambari.server.serveraction.ServerAction.ACTION_USER_NAME, userName);
        executionCommand.setRoleParams(roleParams);
        executionCommand.setClusterName(org.apache.ambari.server.serveraction.upgrades.UpgradeActionTest.clusterName);
        org.apache.ambari.server.actionmanager.HostRoleCommand hostRoleCommand = hostRoleCommandFactory.create(null, null, null, null);
        hostRoleCommand.setExecutionCommandWrapper(new org.apache.ambari.server.actionmanager.ExecutionCommandWrapper(executionCommand));
        org.apache.ambari.server.serveraction.upgrades.UpdateDesiredRepositoryAction action = m_injector.getInstance(org.apache.ambari.server.serveraction.upgrades.UpdateDesiredRepositoryAction.class);
        action.setExecutionCommand(executionCommand);
        action.setHostRoleCommand(hostRoleCommand);
        java.util.List<org.apache.ambari.server.controller.ServiceConfigVersionResponse> configVersionsBefore = cluster.getServiceConfigVersions();
        org.apache.ambari.server.agent.CommandReport report = action.execute(null);
        org.junit.Assert.assertNotNull(report);
        org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED.name(), report.getStatus());
        java.util.List<org.apache.ambari.server.controller.ServiceConfigVersionResponse> configVersionsAfter = cluster.getServiceConfigVersions();
        org.junit.Assert.assertFalse(configVersionsAfter.isEmpty());
        org.junit.Assert.assertTrue((configVersionsAfter.size() - configVersionsBefore.size()) >= 1);
    }

    @org.junit.Test
    public void testFinalizeDowngrade() throws java.lang.Exception {
        makeDowngradeCluster(repositoryVersion2110, repositoryVersion2111);
        org.apache.ambari.server.state.Cluster cluster = clusters.getCluster(org.apache.ambari.server.serveraction.upgrades.UpgradeActionTest.clusterName);
        createUpgrade(cluster, repositoryVersion2111);
        java.util.Map<java.lang.String, java.lang.String> commandParams = new java.util.HashMap<>();
        org.apache.ambari.server.agent.ExecutionCommand executionCommand = new org.apache.ambari.server.agent.ExecutionCommand();
        executionCommand.setCommandParams(commandParams);
        executionCommand.setClusterName(org.apache.ambari.server.serveraction.upgrades.UpgradeActionTest.clusterName);
        org.apache.ambari.server.actionmanager.HostRoleCommand hostRoleCommand = hostRoleCommandFactory.create(null, null, null, null);
        hostRoleCommand.setExecutionCommandWrapper(new org.apache.ambari.server.actionmanager.ExecutionCommandWrapper(executionCommand));
        finalizeUpgradeAction.setExecutionCommand(executionCommand);
        finalizeUpgradeAction.setHostRoleCommand(hostRoleCommand);
        org.apache.ambari.server.agent.CommandReport report = finalizeUpgradeAction.execute(null);
        org.junit.Assert.assertNotNull(report);
        org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED.name(), report.getStatus());
        for (org.apache.ambari.server.orm.entities.HostVersionEntity entity : hostVersionDAO.findByClusterAndHost(org.apache.ambari.server.serveraction.upgrades.UpgradeActionTest.clusterName, "h1")) {
            if (org.apache.commons.lang.StringUtils.equals(entity.getRepositoryVersion().getVersion(), repositoryVersion2110.getVersion())) {
                org.junit.Assert.assertEquals(org.apache.ambari.server.state.RepositoryVersionState.CURRENT, entity.getState());
            } else {
                org.junit.Assert.assertEquals(org.apache.ambari.server.state.RepositoryVersionState.INSTALLED, entity.getState());
            }
        }
    }

    @org.junit.Test
    public void testFinalizeUpgrade() throws java.lang.Exception {
        java.lang.String hostName = "h1";
        createUpgradeCluster(repositoryVersion2110, hostName);
        createHostVersions(repositoryVersion2111, hostName);
        org.apache.ambari.server.state.Cluster cluster = clusters.getCluster(org.apache.ambari.server.serveraction.upgrades.UpgradeActionTest.clusterName);
        createUpgrade(cluster, repositoryVersion2111);
        java.util.Map<java.lang.String, java.lang.String> commandParams = new java.util.HashMap<>();
        org.apache.ambari.server.agent.ExecutionCommand executionCommand = new org.apache.ambari.server.agent.ExecutionCommand();
        executionCommand.setCommandParams(commandParams);
        executionCommand.setClusterName(org.apache.ambari.server.serveraction.upgrades.UpgradeActionTest.clusterName);
        org.apache.ambari.server.actionmanager.HostRoleCommand hostRoleCommand = hostRoleCommandFactory.create(null, null, null, null);
        hostRoleCommand.setExecutionCommandWrapper(new org.apache.ambari.server.actionmanager.ExecutionCommandWrapper(executionCommand));
        finalizeUpgradeAction.setExecutionCommand(executionCommand);
        finalizeUpgradeAction.setHostRoleCommand(hostRoleCommand);
        org.apache.ambari.server.agent.CommandReport report = finalizeUpgradeAction.execute(null);
        org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.HostRoleStatus.FAILED.name(), report.getStatus());
        java.util.List<org.apache.ambari.server.orm.entities.HostVersionEntity> hostVersions = hostVersionDAO.findHostVersionByClusterAndRepository(cluster.getClusterId(), repositoryVersion2111);
        for (org.apache.ambari.server.orm.entities.HostVersionEntity hostVersion : hostVersions) {
            hostVersion.setState(org.apache.ambari.server.state.RepositoryVersionState.CURRENT);
        }
        report = finalizeUpgradeAction.execute(null);
        org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED.name(), report.getStatus());
        hostVersions = hostVersionDAO.findHostVersionByClusterAndRepository(cluster.getClusterId(), repositoryVersion2111);
        for (org.apache.ambari.server.orm.entities.HostVersionEntity hostVersion : hostVersions) {
            java.util.Collection<org.apache.ambari.server.orm.entities.HostComponentStateEntity> hostComponentStates = hostComponentStateDAO.findByHost(hostVersion.getHostName());
            for (org.apache.ambari.server.orm.entities.HostComponentStateEntity hostComponentStateEntity : hostComponentStates) {
                org.junit.Assert.assertEquals(org.apache.ambari.server.state.UpgradeState.NONE, hostComponentStateEntity.getUpgradeState());
            }
        }
    }

    @org.junit.Test
    public void testFinalizeWithHostsAlreadyCurrent() throws java.lang.Exception {
        java.lang.String hostName = "h1";
        createUpgradeCluster(repositoryVersion2110, hostName);
        createHostVersions(repositoryVersion2111, hostName);
        java.util.List<org.apache.ambari.server.orm.entities.HostVersionEntity> hostVersions = hostVersionDAO.findAll();
        for (org.apache.ambari.server.orm.entities.HostVersionEntity hostVersion : hostVersions) {
            if (hostVersion.getState() == org.apache.ambari.server.state.RepositoryVersionState.CURRENT) {
                hostVersion.setState(org.apache.ambari.server.state.RepositoryVersionState.INSTALLED);
            } else {
                hostVersion.setState(org.apache.ambari.server.state.RepositoryVersionState.CURRENT);
            }
            hostVersionDAO.merge(hostVersion);
        }
        org.apache.ambari.server.state.Cluster cluster = clusters.getCluster(org.apache.ambari.server.serveraction.upgrades.UpgradeActionTest.clusterName);
        createUpgrade(cluster, repositoryVersion2111);
        java.util.Map<java.lang.String, java.lang.String> commandParams = new java.util.HashMap<>();
        org.apache.ambari.server.agent.ExecutionCommand executionCommand = new org.apache.ambari.server.agent.ExecutionCommand();
        executionCommand.setCommandParams(commandParams);
        executionCommand.setClusterName(org.apache.ambari.server.serveraction.upgrades.UpgradeActionTest.clusterName);
        org.apache.ambari.server.actionmanager.HostRoleCommand hostRoleCommand = hostRoleCommandFactory.create(null, null, null, null);
        hostRoleCommand.setExecutionCommandWrapper(new org.apache.ambari.server.actionmanager.ExecutionCommandWrapper(executionCommand));
        finalizeUpgradeAction.setExecutionCommand(executionCommand);
        finalizeUpgradeAction.setHostRoleCommand(hostRoleCommand);
        org.apache.ambari.server.agent.CommandReport report = finalizeUpgradeAction.execute(null);
        org.junit.Assert.assertNotNull(report);
        org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED.name(), report.getStatus());
    }

    @org.junit.Test
    public void testHostVersionsAfterUpgrade() throws java.lang.Exception {
        java.lang.String hostName = "h1";
        org.apache.ambari.server.state.Cluster cluster = createUpgradeCluster(repositoryVersion2110, hostName);
        createHostVersions(repositoryVersion2111, hostName);
        createHostVersions(repositoryVersion2201, hostName);
        org.apache.ambari.server.state.Service zk = installService(cluster, "ZOOKEEPER", repositoryVersion2110);
        addServiceComponent(cluster, zk, "ZOOKEEPER_SERVER");
        addServiceComponent(cluster, zk, "ZOOKEEPER_CLIENT");
        createNewServiceComponentHost(cluster, "ZOOKEEPER", "ZOOKEEPER_SERVER", hostName);
        createNewServiceComponentHost(cluster, "ZOOKEEPER", "ZOOKEEPER_CLIENT", hostName);
        java.util.List<org.apache.ambari.server.orm.entities.HostVersionEntity> hostVersions = hostVersionDAO.findAll();
        org.junit.Assert.assertEquals(3, hostVersions.size());
        for (org.apache.ambari.server.orm.entities.HostVersionEntity hostVersion : hostVersions) {
            org.apache.ambari.server.orm.entities.RepositoryVersionEntity hostRepoVersion = hostVersion.getRepositoryVersion();
            if (repositoryVersion2110.equals(hostRepoVersion)) {
                hostVersion.setState(org.apache.ambari.server.state.RepositoryVersionState.CURRENT);
            } else if (repositoryVersion2111.equals(hostRepoVersion)) {
                hostVersion.setState(org.apache.ambari.server.state.RepositoryVersionState.CURRENT);
            } else {
                hostVersion.setState(org.apache.ambari.server.state.RepositoryVersionState.NOT_REQUIRED);
            }
            hostVersionDAO.merge(hostVersion);
        }
        createUpgrade(cluster, repositoryVersion2111);
        java.util.Map<java.lang.String, org.apache.ambari.server.state.Service> services = cluster.getServices();
        org.junit.Assert.assertTrue(services.size() > 0);
        for (org.apache.ambari.server.state.Service service : services.values()) {
            service.setDesiredRepositoryVersion(repositoryVersion2111);
        }
        java.util.List<org.apache.ambari.server.orm.entities.HostComponentStateEntity> hostComponentStates = hostComponentStateDAO.findByHost(hostName);
        for (org.apache.ambari.server.orm.entities.HostComponentStateEntity hostComponentState : hostComponentStates) {
            hostComponentState.setVersion(repositoryVersion2111.getVersion());
            hostComponentStateDAO.merge(hostComponentState);
        }
        java.util.Map<java.lang.String, java.lang.String> commandParams = new java.util.HashMap<>();
        org.apache.ambari.server.agent.ExecutionCommand executionCommand = new org.apache.ambari.server.agent.ExecutionCommand();
        executionCommand.setCommandParams(commandParams);
        executionCommand.setClusterName(org.apache.ambari.server.serveraction.upgrades.UpgradeActionTest.clusterName);
        org.apache.ambari.server.actionmanager.HostRoleCommand hostRoleCommand = hostRoleCommandFactory.create(null, null, null, null);
        hostRoleCommand.setExecutionCommandWrapper(new org.apache.ambari.server.actionmanager.ExecutionCommandWrapper(executionCommand));
        finalizeUpgradeAction.setExecutionCommand(executionCommand);
        finalizeUpgradeAction.setHostRoleCommand(hostRoleCommand);
        org.apache.ambari.server.agent.CommandReport report = finalizeUpgradeAction.execute(null);
        org.junit.Assert.assertNotNull(report);
        org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED.name(), report.getStatus());
        for (org.apache.ambari.server.orm.entities.HostVersionEntity hostVersion : hostVersions) {
            org.apache.ambari.server.orm.entities.RepositoryVersionEntity hostRepoVersion = hostVersion.getRepositoryVersion();
            if (repositoryVersion2110.equals(hostRepoVersion)) {
                org.junit.Assert.assertEquals(org.apache.ambari.server.state.RepositoryVersionState.INSTALLED, hostVersion.getState());
            } else if (repositoryVersion2111.equals(hostRepoVersion)) {
                org.junit.Assert.assertEquals(org.apache.ambari.server.state.RepositoryVersionState.CURRENT, hostVersion.getState());
            } else {
                org.junit.Assert.assertEquals(org.apache.ambari.server.state.RepositoryVersionState.NOT_REQUIRED, hostVersion.getState());
            }
        }
    }

    @org.junit.Test
    public void testHostVersionsOutOfSyncAfterRevert() throws java.lang.Exception {
        java.lang.String hostName = "h1";
        org.apache.ambari.server.state.Cluster cluster = createUpgradeCluster(repositoryVersion2110, hostName);
        createHostVersions(repositoryVersion2111, hostName);
        org.apache.ambari.server.state.Service zk = installService(cluster, "ZOOKEEPER", repositoryVersion2110);
        addServiceComponent(cluster, zk, "ZOOKEEPER_SERVER");
        addServiceComponent(cluster, zk, "ZOOKEEPER_CLIENT");
        createNewServiceComponentHost(cluster, "ZOOKEEPER", "ZOOKEEPER_SERVER", hostName);
        createNewServiceComponentHost(cluster, "ZOOKEEPER", "ZOOKEEPER_CLIENT", hostName);
        java.util.List<org.apache.ambari.server.orm.entities.HostVersionEntity> hostVersions = hostVersionDAO.findAll();
        org.junit.Assert.assertEquals(2, hostVersions.size());
        for (org.apache.ambari.server.orm.entities.HostVersionEntity hostVersion : hostVersions) {
            hostVersion.setState(org.apache.ambari.server.state.RepositoryVersionState.CURRENT);
            hostVersionDAO.merge(hostVersion);
        }
        repositoryVersion2111.setParent(repositoryVersion2110);
        repositoryVersion2111.setType(org.apache.ambari.spi.RepositoryType.PATCH);
        repositoryVersion2111.setVersionXml(hostName);
        repositoryVersion2111.setVersionXsd("version_definition.xsd");
        java.io.File patchVdfFile = new java.io.File("src/test/resources/hbase_version_test.xml");
        repositoryVersion2111.setVersionXml(org.apache.commons.io.IOUtils.toString(new java.io.FileInputStream(patchVdfFile), java.nio.charset.Charset.defaultCharset()));
        repositoryVersion2111 = repositoryVersionDAO.merge(repositoryVersion2111);
        org.apache.ambari.server.orm.entities.UpgradeEntity upgrade = createUpgrade(cluster, repositoryVersion2111);
        upgrade.setOrchestration(org.apache.ambari.spi.RepositoryType.PATCH);
        upgrade.setRevertAllowed(true);
        upgrade = upgradeDAO.merge(upgrade);
        org.apache.ambari.server.state.Service hbase = installService(cluster, "HBASE", repositoryVersion2110);
        addServiceComponent(cluster, hbase, "HBASE_MASTER");
        createNewServiceComponentHost(cluster, "HBASE", "HBASE_MASTER", hostName);
        org.apache.ambari.server.orm.entities.UpgradeEntity revert = createRevert(cluster, upgrade);
        org.junit.Assert.assertEquals(org.apache.ambari.spi.RepositoryType.PATCH, revert.getOrchestration());
        java.util.Map<java.lang.String, org.apache.ambari.server.state.Service> services = cluster.getServices();
        org.junit.Assert.assertTrue(services.size() > 0);
        for (org.apache.ambari.server.state.Service service : services.values()) {
            service.setDesiredRepositoryVersion(repositoryVersion2110);
        }
        java.util.List<org.apache.ambari.server.orm.entities.HostComponentStateEntity> hostComponentStates = hostComponentStateDAO.findByHost(hostName);
        for (org.apache.ambari.server.orm.entities.HostComponentStateEntity hostComponentState : hostComponentStates) {
            hostComponentState.setVersion(repositoryVersion2110.getVersion());
            hostComponentStateDAO.merge(hostComponentState);
        }
        java.util.Map<java.lang.String, java.lang.String> commandParams = new java.util.HashMap<>();
        org.apache.ambari.server.agent.ExecutionCommand executionCommand = new org.apache.ambari.server.agent.ExecutionCommand();
        executionCommand.setCommandParams(commandParams);
        executionCommand.setClusterName(org.apache.ambari.server.serveraction.upgrades.UpgradeActionTest.clusterName);
        org.apache.ambari.server.actionmanager.HostRoleCommand hostRoleCommand = hostRoleCommandFactory.create(null, null, null, null);
        hostRoleCommand.setExecutionCommandWrapper(new org.apache.ambari.server.actionmanager.ExecutionCommandWrapper(executionCommand));
        finalizeUpgradeAction.setExecutionCommand(executionCommand);
        finalizeUpgradeAction.setHostRoleCommand(hostRoleCommand);
        org.apache.ambari.server.agent.CommandReport report = finalizeUpgradeAction.execute(null);
        org.junit.Assert.assertNotNull(report);
        org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED.name(), report.getStatus());
        for (org.apache.ambari.server.orm.entities.HostVersionEntity hostVersion : hostVersions) {
            org.apache.ambari.server.orm.entities.RepositoryVersionEntity hostRepoVersion = hostVersion.getRepositoryVersion();
            if (repositoryVersion2110.equals(hostRepoVersion)) {
                org.junit.Assert.assertEquals(org.apache.ambari.server.state.RepositoryVersionState.CURRENT, hostVersion.getState());
            } else {
                org.junit.Assert.assertEquals(org.apache.ambari.server.state.RepositoryVersionState.OUT_OF_SYNC, hostVersion.getState());
            }
        }
    }

    private org.apache.ambari.server.state.ServiceComponentHost createNewServiceComponentHost(org.apache.ambari.server.state.Cluster cluster, java.lang.String svc, java.lang.String svcComponent, java.lang.String hostName) throws org.apache.ambari.server.AmbariException {
        org.junit.Assert.assertNotNull(cluster.getConfigGroups());
        org.apache.ambari.server.state.Service s = installService(cluster, svc, sourceRepositoryVersion);
        org.apache.ambari.server.state.ServiceComponent sc = addServiceComponent(cluster, s, svcComponent);
        org.apache.ambari.server.state.ServiceComponentHost sch = serviceComponentHostFactory.createNew(sc, hostName);
        sc.addServiceComponentHost(sch);
        sch.setDesiredState(org.apache.ambari.server.state.State.INSTALLED);
        sch.setState(org.apache.ambari.server.state.State.INSTALLED);
        return sch;
    }

    private org.apache.ambari.server.state.Service installService(org.apache.ambari.server.state.Cluster cluster, java.lang.String serviceName, org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersionEntity) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.Service service = null;
        try {
            service = cluster.getService(serviceName);
        } catch (org.apache.ambari.server.ServiceNotFoundException e) {
            service = serviceFactory.createNew(cluster, serviceName, repositoryVersionEntity);
            cluster.addService(service);
        }
        return service;
    }

    private org.apache.ambari.server.state.ServiceComponent addServiceComponent(org.apache.ambari.server.state.Cluster cluster, org.apache.ambari.server.state.Service service, java.lang.String componentName) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.ServiceComponent serviceComponent = null;
        try {
            serviceComponent = service.getServiceComponent(componentName);
        } catch (org.apache.ambari.server.ServiceComponentNotFoundException e) {
            serviceComponent = serviceComponentFactory.createNew(service, componentName);
            service.addServiceComponent(serviceComponent);
            serviceComponent.setDesiredState(org.apache.ambari.server.state.State.INSTALLED);
        }
        return serviceComponent;
    }

    private void createConfigs(org.apache.ambari.server.state.Cluster cluster) {
        java.util.Map<java.lang.String, java.lang.String> properties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> propertiesAttributes = new java.util.HashMap<>();
        properties.put("a", "a1");
        properties.put("b", "b1");
        configFactory.createNew(cluster, "zookeeper-env", "version-" + java.lang.System.currentTimeMillis(), properties, propertiesAttributes);
        properties.put("zookeeper_a", "value_1");
        properties.put("zookeeper_b", "value_2");
        configFactory.createNew(cluster, "hdfs-site", "version-" + java.lang.System.currentTimeMillis(), properties, propertiesAttributes);
        properties.put("hdfs_a", "value_3");
        properties.put("hdfs_b", "value_4");
        configFactory.createNew(cluster, "core-site", "version-" + java.lang.System.currentTimeMillis(), properties, propertiesAttributes);
        configFactory.createNew(cluster, "foo-site", "version-" + java.lang.System.currentTimeMillis(), properties, propertiesAttributes);
    }

    private org.apache.ambari.server.orm.entities.UpgradeEntity createUpgrade(org.apache.ambari.server.state.Cluster cluster, org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion) throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.RequestEntity requestEntity = new org.apache.ambari.server.orm.entities.RequestEntity();
        requestEntity.setClusterId(cluster.getClusterId());
        requestEntity.setRequestId(1L);
        requestEntity.setStartTime(java.lang.System.currentTimeMillis());
        requestEntity.setCreateTime(java.lang.System.currentTimeMillis());
        requestDAO.create(requestEntity);
        org.apache.ambari.server.orm.entities.UpgradeEntity upgradeEntity = new org.apache.ambari.server.orm.entities.UpgradeEntity();
        upgradeEntity.setId(1L);
        upgradeEntity.setClusterId(cluster.getClusterId());
        upgradeEntity.setRequestEntity(requestEntity);
        upgradeEntity.setUpgradePackage("");
        upgradeEntity.setUpgradePackStackId(new org.apache.ambari.server.state.StackId(((java.lang.String) (null))));
        upgradeEntity.setRepositoryVersion(repositoryVersion);
        upgradeEntity.setUpgradeType(org.apache.ambari.spi.upgrade.UpgradeType.NON_ROLLING);
        java.util.Map<java.lang.String, org.apache.ambari.server.state.Service> services = cluster.getServices();
        for (java.lang.String serviceName : services.keySet()) {
            org.apache.ambari.server.state.Service service = services.get(serviceName);
            java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceComponent> components = service.getServiceComponents();
            for (java.lang.String componentName : components.keySet()) {
                org.apache.ambari.server.orm.entities.UpgradeHistoryEntity history = new org.apache.ambari.server.orm.entities.UpgradeHistoryEntity();
                history.setUpgrade(upgradeEntity);
                history.setServiceName(serviceName);
                history.setComponentName(componentName);
                history.setFromRepositoryVersion(service.getDesiredRepositoryVersion());
                history.setTargetRepositoryVersion(repositoryVersion);
                upgradeEntity.addHistory(history);
            }
        }
        upgradeDAO.create(upgradeEntity);
        cluster.setUpgradeEntity(upgradeEntity);
        return upgradeEntity;
    }

    private org.apache.ambari.server.orm.entities.UpgradeEntity createRevert(org.apache.ambari.server.state.Cluster cluster, org.apache.ambari.server.orm.entities.UpgradeEntity upgradeToRevert) throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.RequestEntity requestEntity = new org.apache.ambari.server.orm.entities.RequestEntity();
        requestEntity.setClusterId(cluster.getClusterId());
        requestEntity.setRequestId(2L);
        requestEntity.setStartTime(java.lang.System.currentTimeMillis());
        requestEntity.setCreateTime(java.lang.System.currentTimeMillis());
        requestDAO.create(requestEntity);
        org.apache.ambari.server.orm.entities.UpgradeEntity revert = new org.apache.ambari.server.orm.entities.UpgradeEntity();
        revert.setId(2L);
        revert.setDirection(org.apache.ambari.server.stack.upgrade.Direction.DOWNGRADE);
        revert.setClusterId(cluster.getClusterId());
        revert.setRequestEntity(requestEntity);
        revert.setUpgradePackage("");
        revert.setUpgradePackStackId(new org.apache.ambari.server.state.StackId(((java.lang.String) (null))));
        revert.setRepositoryVersion(upgradeToRevert.getRepositoryVersion());
        revert.setUpgradeType(upgradeToRevert.getUpgradeType());
        revert.setOrchestration(upgradeToRevert.getOrchestration());
        for (org.apache.ambari.server.orm.entities.UpgradeHistoryEntity historyToRevert : upgradeToRevert.getHistory()) {
            org.apache.ambari.server.orm.entities.UpgradeHistoryEntity history = new org.apache.ambari.server.orm.entities.UpgradeHistoryEntity();
            history.setUpgrade(revert);
            history.setServiceName(historyToRevert.getServiceName());
            history.setComponentName(historyToRevert.getComponentName());
            history.setFromRepositoryVersion(upgradeToRevert.getRepositoryVersion());
            history.setTargetRepositoryVersion(historyToRevert.getFromReposistoryVersion());
            revert.addHistory(history);
        }
        upgradeDAO.create(revert);
        cluster.setUpgradeEntity(revert);
        return revert;
    }
}