package org.apache.ambari.server.serveraction.upgrades;
import com.google.inject.persist.UnitOfWork;
import javax.persistence.EntityManager;
public class ComponentVersionCheckActionTest {
    private static final java.lang.String HDP_2_1_1_0 = "2.1.1.0-1";

    private static final java.lang.String HDP_2_1_1_1 = "2.1.1.1-2";

    private static final java.lang.String HDP_2_2_1_0 = "2.2.1.0-1";

    private static final org.apache.ambari.server.state.StackId HDP_21_STACK = new org.apache.ambari.server.state.StackId("HDP-2.1.1");

    private static final org.apache.ambari.server.state.StackId HDP_22_STACK = new org.apache.ambari.server.state.StackId("HDP-2.2.0");

    private static final java.lang.String HDP_211_CENTOS6_REPO_URL = "http://public-repo-1.hortonworks.com/HDP/centos6/2.x/updates/2.0.6.0";

    private com.google.inject.Injector m_injector;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.OrmTestHelper m_helper;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.RepositoryVersionDAO repoVersionDAO;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.HostVersionDAO hostVersionDAO;

    @com.google.inject.Inject
    private org.apache.ambari.server.actionmanager.HostRoleCommandFactory hostRoleCommandFactory;

    @com.google.inject.Inject
    private org.apache.ambari.server.state.ServiceFactory serviceFactory;

    @com.google.inject.Inject
    private org.apache.ambari.server.state.ServiceComponentFactory serviceComponentFactory;

    @com.google.inject.Inject
    private org.apache.ambari.server.state.ServiceComponentHostFactory serviceComponentHostFactory;

    @com.google.inject.Inject
    private org.apache.ambari.server.state.ConfigFactory configFactory;

    @org.junit.Before
    public void setup() throws java.lang.Exception {
        m_injector = com.google.inject.Guice.createInjector(new org.apache.ambari.server.orm.InMemoryDefaultTestModule());
        org.apache.ambari.server.utils.EventBusSynchronizer.synchronizeAmbariEventPublisher(m_injector);
        m_injector.getInstance(org.apache.ambari.server.orm.GuiceJpaInitializer.class);
        m_injector.injectMembers(this);
        m_injector.getInstance(com.google.inject.persist.UnitOfWork.class).begin();
    }

    @org.junit.After
    public void teardown() throws java.lang.Exception {
        m_injector.getInstance(com.google.inject.persist.UnitOfWork.class).end();
        org.apache.ambari.server.H2DatabaseCleaner.clearDatabase(m_injector.getProvider(javax.persistence.EntityManager.class).get());
    }

    private void makeUpgradeCluster(org.apache.ambari.server.state.StackId sourceStack, java.lang.String sourceRepo, org.apache.ambari.server.state.StackId targetStack, java.lang.String targetRepo) throws java.lang.Exception {
        java.lang.String clusterName = "c1";
        java.lang.String hostName = "h1";
        m_helper.createStack(sourceStack);
        org.apache.ambari.server.state.Clusters clusters = m_injector.getInstance(org.apache.ambari.server.state.Clusters.class);
        clusters.addCluster(clusterName, sourceStack);
        org.apache.ambari.server.orm.dao.StackDAO stackDAO = m_injector.getInstance(org.apache.ambari.server.orm.dao.StackDAO.class);
        org.apache.ambari.server.orm.dao.RequestDAO requestDAO = m_injector.getInstance(org.apache.ambari.server.orm.dao.RequestDAO.class);
        org.apache.ambari.server.orm.dao.UpgradeDAO upgradeDAO = m_injector.getInstance(org.apache.ambari.server.orm.dao.UpgradeDAO.class);
        org.apache.ambari.server.orm.entities.StackEntity stackEntitySource = stackDAO.find(sourceStack.getStackName(), sourceStack.getStackVersion());
        org.apache.ambari.server.orm.entities.StackEntity stackEntityTarget = stackDAO.find(targetStack.getStackName(), targetStack.getStackVersion());
        org.junit.Assert.assertNotNull(stackEntitySource);
        org.junit.Assert.assertNotNull(stackEntityTarget);
        org.apache.ambari.server.state.Cluster c = clusters.getCluster(clusterName);
        c.setDesiredStackVersion(sourceStack);
        clusters.addHost(hostName);
        org.apache.ambari.server.state.Host host = clusters.getHost(hostName);
        java.util.Map<java.lang.String, java.lang.String> hostAttributes = new java.util.HashMap<>();
        hostAttributes.put("os_family", "redhat");
        hostAttributes.put("os_release_version", "6");
        host.setHostAttributes(hostAttributes);
        m_helper.getOrCreateRepositoryVersion(sourceStack, sourceRepo);
        java.util.List<org.apache.ambari.server.orm.entities.RepoOsEntity> urlInfo = new java.util.ArrayList<>();
        org.apache.ambari.server.orm.entities.RepoDefinitionEntity repoDefinitionEntity1 = new org.apache.ambari.server.orm.entities.RepoDefinitionEntity();
        repoDefinitionEntity1.setRepoID(targetStack.getStackId());
        repoDefinitionEntity1.setBaseUrl("http://foo1");
        repoDefinitionEntity1.setRepoName("HDP");
        org.apache.ambari.server.orm.entities.RepoOsEntity repoOsEntity = new org.apache.ambari.server.orm.entities.RepoOsEntity();
        repoOsEntity.setFamily("redhat6");
        repoOsEntity.setAmbariManaged(true);
        repoOsEntity.addRepoDefinition(repoDefinitionEntity1);
        urlInfo.add(repoOsEntity);
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity toRepositoryVersion = repoVersionDAO.create(stackEntityTarget, targetRepo, java.lang.String.valueOf(java.lang.System.currentTimeMillis()), urlInfo);
        c.setCurrentStackVersion(targetStack);
        org.apache.ambari.server.orm.dao.HostDAO hostDAO = m_injector.getInstance(org.apache.ambari.server.orm.dao.HostDAO.class);
        org.apache.ambari.server.orm.entities.HostVersionEntity entity = new org.apache.ambari.server.orm.entities.HostVersionEntity();
        entity.setHostEntity(hostDAO.findByName(hostName));
        entity.setRepositoryVersion(repoVersionDAO.findByStackAndVersion(targetStack, targetRepo));
        entity.setState(org.apache.ambari.server.state.RepositoryVersionState.INSTALLED);
        hostVersionDAO.create(entity);
        org.apache.ambari.server.orm.entities.RequestEntity requestEntity = new org.apache.ambari.server.orm.entities.RequestEntity();
        requestEntity.setClusterId(c.getClusterId());
        requestEntity.setRequestId(1L);
        requestEntity.setStartTime(java.lang.System.currentTimeMillis());
        requestEntity.setCreateTime(java.lang.System.currentTimeMillis());
        requestDAO.create(requestEntity);
        org.apache.ambari.server.orm.entities.UpgradeEntity upgradeEntity = new org.apache.ambari.server.orm.entities.UpgradeEntity();
        upgradeEntity.setId(1L);
        upgradeEntity.setClusterId(c.getClusterId());
        upgradeEntity.setRequestEntity(requestEntity);
        upgradeEntity.setUpgradePackage("");
        upgradeEntity.setUpgradePackStackId(new org.apache.ambari.server.state.StackId(((java.lang.String) (null))));
        upgradeEntity.setRepositoryVersion(toRepositoryVersion);
        upgradeEntity.setUpgradeType(org.apache.ambari.spi.upgrade.UpgradeType.NON_ROLLING);
        upgradeDAO.create(upgradeEntity);
        c.setUpgradeEntity(upgradeEntity);
    }

    private void makeCrossStackUpgradeCluster(org.apache.ambari.server.state.StackId sourceStack, java.lang.String sourceRepo, org.apache.ambari.server.state.StackId targetStack, java.lang.String targetRepo, java.lang.String clusterName, java.lang.String hostName) throws java.lang.Exception {
        m_helper.createStack(sourceStack);
        m_helper.createStack(targetStack);
        org.apache.ambari.server.state.Clusters clusters = m_injector.getInstance(org.apache.ambari.server.state.Clusters.class);
        clusters.addCluster(clusterName, sourceStack);
        org.apache.ambari.server.orm.dao.StackDAO stackDAO = m_injector.getInstance(org.apache.ambari.server.orm.dao.StackDAO.class);
        org.apache.ambari.server.orm.dao.RequestDAO requestDAO = m_injector.getInstance(org.apache.ambari.server.orm.dao.RequestDAO.class);
        org.apache.ambari.server.orm.dao.UpgradeDAO upgradeDAO = m_injector.getInstance(org.apache.ambari.server.orm.dao.UpgradeDAO.class);
        org.apache.ambari.server.orm.entities.StackEntity stackEntitySource = stackDAO.find(sourceStack.getStackName(), sourceStack.getStackVersion());
        org.apache.ambari.server.orm.entities.StackEntity stackEntityTarget = stackDAO.find(targetStack.getStackName(), targetStack.getStackVersion());
        org.junit.Assert.assertNotNull(stackEntitySource);
        org.junit.Assert.assertNotNull(stackEntityTarget);
        org.apache.ambari.server.state.Cluster c = clusters.getCluster(clusterName);
        c.setCurrentStackVersion(sourceStack);
        c.setDesiredStackVersion(sourceStack);
        clusters.addHost(hostName);
        org.apache.ambari.server.state.Host host = clusters.getHost(hostName);
        java.util.Map<java.lang.String, java.lang.String> hostAttributes = new java.util.HashMap<>();
        hostAttributes.put("os_family", "redhat");
        hostAttributes.put("os_release_version", "6");
        host.setHostAttributes(hostAttributes);
        clusters.mapHostToCluster(hostName, clusterName);
        m_helper.getOrCreateRepositoryVersion(sourceStack, sourceRepo);
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity toRepositoryVersion = m_helper.getOrCreateRepositoryVersion(targetStack, targetRepo);
        org.apache.ambari.server.orm.entities.RequestEntity requestEntity = new org.apache.ambari.server.orm.entities.RequestEntity();
        requestEntity.setClusterId(c.getClusterId());
        requestEntity.setRequestId(1L);
        requestEntity.setStartTime(java.lang.System.currentTimeMillis());
        requestEntity.setCreateTime(java.lang.System.currentTimeMillis());
        requestDAO.create(requestEntity);
        org.apache.ambari.server.orm.entities.UpgradeEntity upgradeEntity = new org.apache.ambari.server.orm.entities.UpgradeEntity();
        upgradeEntity.setId(1L);
        upgradeEntity.setClusterId(c.getClusterId());
        upgradeEntity.setRequestEntity(requestEntity);
        upgradeEntity.setUpgradePackage("");
        upgradeEntity.setUpgradePackStackId(new org.apache.ambari.server.state.StackId(((java.lang.String) (null))));
        upgradeEntity.setRepositoryVersion(toRepositoryVersion);
        upgradeEntity.setUpgradeType(org.apache.ambari.spi.upgrade.UpgradeType.NON_ROLLING);
        upgradeDAO.create(upgradeEntity);
        c.setUpgradeEntity(upgradeEntity);
    }

    private void installRepositoryOnHost(java.lang.String hostName, org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.orm.dao.HostDAO hostDAO = m_injector.getInstance(org.apache.ambari.server.orm.dao.HostDAO.class);
        org.apache.ambari.server.orm.entities.HostVersionEntity entity = new org.apache.ambari.server.orm.entities.HostVersionEntity();
        entity.setHostEntity(hostDAO.findByName(hostName));
        entity.setRepositoryVersion(repositoryVersion);
        entity.setState(org.apache.ambari.server.state.RepositoryVersionState.INSTALLED);
        hostVersionDAO.create(entity);
    }

    @org.junit.Test
    public void testMatchingVersions() throws java.lang.Exception {
        org.apache.ambari.server.state.StackId sourceStack = org.apache.ambari.server.serveraction.upgrades.ComponentVersionCheckActionTest.HDP_21_STACK;
        org.apache.ambari.server.state.StackId targetStack = org.apache.ambari.server.serveraction.upgrades.ComponentVersionCheckActionTest.HDP_21_STACK;
        java.lang.String sourceRepo = org.apache.ambari.server.serveraction.upgrades.ComponentVersionCheckActionTest.HDP_2_1_1_0;
        java.lang.String targetRepo = org.apache.ambari.server.serveraction.upgrades.ComponentVersionCheckActionTest.HDP_2_1_1_1;
        makeUpgradeCluster(sourceStack, sourceRepo, targetStack, targetRepo);
        java.util.Map<java.lang.String, java.lang.String> commandParams = new java.util.HashMap<>();
        org.apache.ambari.server.agent.ExecutionCommand executionCommand = new org.apache.ambari.server.agent.ExecutionCommand();
        executionCommand.setCommandParams(commandParams);
        executionCommand.setClusterName("c1");
        org.apache.ambari.server.actionmanager.HostRoleCommand hostRoleCommand = hostRoleCommandFactory.create(null, null, null, null);
        hostRoleCommand.setExecutionCommandWrapper(new org.apache.ambari.server.actionmanager.ExecutionCommandWrapper(executionCommand));
        org.apache.ambari.server.serveraction.upgrades.ComponentVersionCheckAction action = m_injector.getInstance(org.apache.ambari.server.serveraction.upgrades.ComponentVersionCheckAction.class);
        action.setExecutionCommand(executionCommand);
        action.setHostRoleCommand(hostRoleCommand);
        org.apache.ambari.server.agent.CommandReport report = action.execute(null);
        org.junit.Assert.assertNotNull(report);
        org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED.name(), report.getStatus());
        org.junit.Assert.assertEquals(0, report.getExitCode());
    }

    @org.junit.Test
    public void testMixedComponentVersions() throws java.lang.Exception {
        org.apache.ambari.server.state.StackId sourceStack = org.apache.ambari.server.serveraction.upgrades.ComponentVersionCheckActionTest.HDP_21_STACK;
        org.apache.ambari.server.state.StackId targetStack = org.apache.ambari.server.serveraction.upgrades.ComponentVersionCheckActionTest.HDP_22_STACK;
        java.lang.String sourceVersion = org.apache.ambari.server.serveraction.upgrades.ComponentVersionCheckActionTest.HDP_2_1_1_0;
        java.lang.String targetVersion = org.apache.ambari.server.serveraction.upgrades.ComponentVersionCheckActionTest.HDP_2_2_1_0;
        java.lang.String clusterName = "c1";
        java.lang.String hostName = "h1";
        makeCrossStackUpgradeCluster(sourceStack, sourceVersion, targetStack, targetVersion, clusterName, hostName);
        org.apache.ambari.server.state.Clusters clusters = m_injector.getInstance(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.state.Cluster cluster = clusters.getCluster("c1");
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity sourceRepoVersion = m_helper.getOrCreateRepositoryVersion(org.apache.ambari.server.serveraction.upgrades.ComponentVersionCheckActionTest.HDP_21_STACK, org.apache.ambari.server.serveraction.upgrades.ComponentVersionCheckActionTest.HDP_2_1_1_0);
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity targetRepoVersion = m_helper.getOrCreateRepositoryVersion(org.apache.ambari.server.serveraction.upgrades.ComponentVersionCheckActionTest.HDP_22_STACK, org.apache.ambari.server.serveraction.upgrades.ComponentVersionCheckActionTest.HDP_2_2_1_0);
        org.apache.ambari.server.state.Service service = installService(cluster, "HDFS", sourceRepoVersion);
        addServiceComponent(cluster, service, "NAMENODE");
        addServiceComponent(cluster, service, "DATANODE");
        createNewServiceComponentHost(cluster, "HDFS", "NAMENODE", hostName);
        createNewServiceComponentHost(cluster, "HDFS", "DATANODE", hostName);
        createConfigs(cluster);
        installRepositoryOnHost(hostName, targetRepoVersion);
        cluster.setCurrentStackVersion(sourceStack);
        cluster.setDesiredStackVersion(targetStack);
        org.apache.ambari.server.orm.entities.UpgradeEntity upgrade = cluster.getUpgradeInProgress();
        org.apache.ambari.server.orm.entities.UpgradeHistoryEntity history = new org.apache.ambari.server.orm.entities.UpgradeHistoryEntity();
        history.setUpgrade(upgrade);
        history.setServiceName("HDFS");
        history.setComponentName("NAMENODE");
        history.setFromRepositoryVersion(sourceRepoVersion);
        history.setTargetRepositoryVersion(targetRepoVersion);
        upgrade.addHistory(history);
        history = new org.apache.ambari.server.orm.entities.UpgradeHistoryEntity();
        history.setUpgrade(upgrade);
        history.setServiceName("HDFS");
        history.setComponentName("DATANODE");
        history.setFromRepositoryVersion(sourceRepoVersion);
        history.setTargetRepositoryVersion(targetRepoVersion);
        upgrade.addHistory(history);
        org.apache.ambari.server.orm.dao.UpgradeDAO upgradeDAO = m_injector.getInstance(org.apache.ambari.server.orm.dao.UpgradeDAO.class);
        upgrade = upgradeDAO.merge(upgrade);
        cluster.getServiceComponentHosts("HDFS", "NAMENODE").get(0).setVersion(targetVersion);
        java.util.List<org.apache.ambari.server.orm.entities.HostVersionEntity> hostVersions = hostVersionDAO.findByClusterStackAndVersion("c1", org.apache.ambari.server.serveraction.upgrades.ComponentVersionCheckActionTest.HDP_22_STACK, targetVersion);
        org.junit.Assert.assertTrue(hostVersions.size() > 0);
        for (org.apache.ambari.server.orm.entities.HostVersionEntity hostVersion : hostVersions) {
            org.junit.Assert.assertEquals(org.apache.ambari.server.state.RepositoryVersionState.INSTALLED, hostVersion.getState());
        }
        java.util.Map<java.lang.String, java.lang.String> commandParams = new java.util.HashMap<>();
        org.apache.ambari.server.agent.ExecutionCommand executionCommand = new org.apache.ambari.server.agent.ExecutionCommand();
        executionCommand.setCommandParams(commandParams);
        executionCommand.setClusterName("c1");
        org.apache.ambari.server.actionmanager.HostRoleCommand hostRoleCommand = hostRoleCommandFactory.create(null, null, null, null);
        hostRoleCommand.setExecutionCommandWrapper(new org.apache.ambari.server.actionmanager.ExecutionCommandWrapper(executionCommand));
        org.apache.ambari.server.serveraction.upgrades.ComponentVersionCheckAction action = m_injector.getInstance(org.apache.ambari.server.serveraction.upgrades.ComponentVersionCheckAction.class);
        action.setExecutionCommand(executionCommand);
        action.setHostRoleCommand(hostRoleCommand);
        org.apache.ambari.server.agent.CommandReport report = action.execute(null);
        org.junit.Assert.assertNotNull(report);
        org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.HostRoleStatus.FAILED.name(), report.getStatus());
        org.junit.Assert.assertEquals(-1, report.getExitCode());
        cluster.getServiceComponentHosts("HDFS", "DATANODE").get(0).setVersion(targetVersion);
        report = action.execute(null);
        org.junit.Assert.assertNotNull(report);
        org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED.name(), report.getStatus());
        org.junit.Assert.assertEquals(0, report.getExitCode());
    }

    @org.junit.Test
    public void testMatchingPartialVersions() throws java.lang.Exception {
        org.apache.ambari.server.state.StackId sourceStack = org.apache.ambari.server.serveraction.upgrades.ComponentVersionCheckActionTest.HDP_21_STACK;
        org.apache.ambari.server.state.StackId targetStack = org.apache.ambari.server.serveraction.upgrades.ComponentVersionCheckActionTest.HDP_21_STACK;
        java.lang.String sourceRepo = org.apache.ambari.server.serveraction.upgrades.ComponentVersionCheckActionTest.HDP_2_1_1_0;
        java.lang.String targetRepo = org.apache.ambari.server.serveraction.upgrades.ComponentVersionCheckActionTest.HDP_2_1_1_1;
        makeUpgradeCluster(sourceStack, sourceRepo, targetStack, targetRepo);
        org.apache.ambari.server.state.Clusters clusters = m_injector.getInstance(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.state.Host host = clusters.getHost("h1");
        org.junit.Assert.assertNotNull(host);
        host.setOsInfo("redhat6");
        org.apache.ambari.server.state.Cluster cluster = clusters.getCluster("c1");
        clusters.mapHostToCluster("h1", "c1");
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion2110 = m_helper.getOrCreateRepositoryVersion(org.apache.ambari.server.serveraction.upgrades.ComponentVersionCheckActionTest.HDP_21_STACK, org.apache.ambari.server.serveraction.upgrades.ComponentVersionCheckActionTest.HDP_2_1_1_0);
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion2111 = m_helper.getOrCreateRepositoryVersion(org.apache.ambari.server.serveraction.upgrades.ComponentVersionCheckActionTest.HDP_21_STACK, org.apache.ambari.server.serveraction.upgrades.ComponentVersionCheckActionTest.HDP_2_1_1_1);
        org.apache.ambari.server.state.Service service = installService(cluster, "HDFS", repositoryVersion2110);
        addServiceComponent(cluster, service, "NAMENODE");
        addServiceComponent(cluster, service, "DATANODE");
        org.apache.ambari.server.state.ServiceComponentHost sch = createNewServiceComponentHost(cluster, "HDFS", "NAMENODE", "h1");
        sch.setVersion(org.apache.ambari.server.serveraction.upgrades.ComponentVersionCheckActionTest.HDP_2_1_1_0);
        sch = createNewServiceComponentHost(cluster, "HDFS", "DATANODE", "h1");
        sch.setVersion(org.apache.ambari.server.serveraction.upgrades.ComponentVersionCheckActionTest.HDP_2_1_1_0);
        service = installService(cluster, "ZOOKEEPER", repositoryVersion2111);
        addServiceComponent(cluster, service, "ZOOKEEPER_SERVER");
        sch = createNewServiceComponentHost(cluster, "ZOOKEEPER", "ZOOKEEPER_SERVER", "h1");
        sch.setVersion(org.apache.ambari.server.serveraction.upgrades.ComponentVersionCheckActionTest.HDP_2_1_1_1);
        java.util.Map<java.lang.String, java.lang.String> commandParams = new java.util.HashMap<>();
        org.apache.ambari.server.agent.ExecutionCommand executionCommand = new org.apache.ambari.server.agent.ExecutionCommand();
        executionCommand.setCommandParams(commandParams);
        executionCommand.setClusterName("c1");
        org.apache.ambari.server.actionmanager.HostRoleCommand hostRoleCommand = hostRoleCommandFactory.create(null, null, null, null);
        hostRoleCommand.setExecutionCommandWrapper(new org.apache.ambari.server.actionmanager.ExecutionCommandWrapper(executionCommand));
        org.apache.ambari.server.serveraction.upgrades.ComponentVersionCheckAction action = m_injector.getInstance(org.apache.ambari.server.serveraction.upgrades.ComponentVersionCheckAction.class);
        action.setExecutionCommand(executionCommand);
        action.setHostRoleCommand(hostRoleCommand);
        org.apache.ambari.server.agent.CommandReport report = action.execute(null);
        org.junit.Assert.assertNotNull(report);
        org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED.name(), report.getStatus());
        org.junit.Assert.assertEquals(0, report.getExitCode());
    }

    private org.apache.ambari.server.state.ServiceComponentHost createNewServiceComponentHost(org.apache.ambari.server.state.Cluster cluster, java.lang.String svc, java.lang.String svcComponent, java.lang.String hostName) throws org.apache.ambari.server.AmbariException {
        org.junit.Assert.assertNotNull(cluster.getConfigGroups());
        org.apache.ambari.server.state.Service s = cluster.getService(svc);
        org.apache.ambari.server.state.ServiceComponent sc = addServiceComponent(cluster, s, svcComponent);
        org.apache.ambari.server.state.ServiceComponentHost sch = serviceComponentHostFactory.createNew(sc, hostName);
        sc.addServiceComponentHost(sch);
        sch.setDesiredState(org.apache.ambari.server.state.State.INSTALLED);
        sch.setState(org.apache.ambari.server.state.State.INSTALLED);
        return sch;
    }

    private org.apache.ambari.server.state.Service installService(org.apache.ambari.server.state.Cluster cluster, java.lang.String serviceName, org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.Service service = serviceFactory.createNew(cluster, serviceName, repositoryVersion);
        cluster.addService(service);
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
        configFactory.createNew(cluster, "hdfs-site", "version1", properties, propertiesAttributes);
        properties.put("c", "c1");
        properties.put("d", "d1");
        configFactory.createNew(cluster, "core-site", "version1", properties, propertiesAttributes);
        configFactory.createNew(cluster, "foo-site", "version1", properties, propertiesAttributes);
    }
}