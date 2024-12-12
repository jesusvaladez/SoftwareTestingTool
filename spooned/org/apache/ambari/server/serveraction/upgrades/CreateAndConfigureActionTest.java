package org.apache.ambari.server.serveraction.upgrades;
import javax.persistence.EntityManager;
import org.apache.commons.lang.StringUtils;
public class CreateAndConfigureActionTest {
    @com.google.inject.Inject
    private com.google.inject.Injector m_injector;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.OrmTestHelper m_helper;

    @com.google.inject.Inject
    private org.apache.ambari.server.actionmanager.HostRoleCommandFactory hostRoleCommandFactory;

    @com.google.inject.Inject
    private org.apache.ambari.server.state.ServiceFactory serviceFactory;

    @com.google.inject.Inject
    private org.apache.ambari.server.state.ConfigHelper m_configHelper;

    @com.google.inject.Inject
    private org.apache.ambari.server.state.Clusters clusters;

    @com.google.inject.Inject
    private org.apache.ambari.server.state.ConfigFactory configFactory;

    @com.google.inject.Inject
    private org.apache.ambari.server.serveraction.upgrades.CreateAndConfigureAction action;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.RequestDAO requestDAO;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.UpgradeDAO upgradeDAO;

    @com.google.inject.Inject
    private org.apache.ambari.server.state.ServiceComponentFactory serviceComponentFactory;

    @com.google.inject.Inject
    private org.apache.ambari.server.state.ServiceComponentHostFactory serviceComponentHostFactory;

    private org.apache.ambari.server.orm.entities.RepositoryVersionEntity repoVersion2110;

    private org.apache.ambari.server.orm.entities.RepositoryVersionEntity repoVersion2111;

    private org.apache.ambari.server.orm.entities.RepositoryVersionEntity repoVersion2200;

    private final java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> NO_ATTRIBUTES = new java.util.HashMap<>();

    @org.junit.Before
    public void setup() throws java.lang.Exception {
        m_injector = com.google.inject.Guice.createInjector(new org.apache.ambari.server.orm.InMemoryDefaultTestModule());
        m_injector.getInstance(org.apache.ambari.server.orm.GuiceJpaInitializer.class);
        m_injector.injectMembers(this);
        repoVersion2110 = m_helper.getOrCreateRepositoryVersion(new org.apache.ambari.server.state.StackId("HDP-2.1.1"), "2.1.1.0-1234");
        repoVersion2111 = m_helper.getOrCreateRepositoryVersion(new org.apache.ambari.server.state.StackId("HDP-2.1.1"), "2.1.1.1-5678");
        repoVersion2200 = m_helper.getOrCreateRepositoryVersion(new org.apache.ambari.server.state.StackId("HDP-2.2.0"), "2.2.0.0-1234");
        makeUpgradeCluster();
    }

    @org.junit.After
    public void teardown() throws java.lang.Exception {
        org.apache.ambari.server.H2DatabaseCleaner.clearDatabase(m_injector.getProvider(javax.persistence.EntityManager.class).get());
    }

    @org.junit.Test
    public void testNewConfigCreatedWhenUpgradingWithoutChaningStack() throws java.lang.Exception {
        org.apache.ambari.server.state.Cluster c = clusters.getCluster("c1");
        org.junit.Assert.assertEquals(1, c.getConfigsByType("zoo.cfg").size());
        java.util.Map<java.lang.String, java.lang.String> properties = new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put("initLimit", "10");
            }
        };
        org.apache.ambari.server.state.Config config = createConfig(c, "zoo.cfg", "version2", properties);
        c.addDesiredConfig("user", java.util.Collections.singleton(config));
        org.junit.Assert.assertEquals(2, c.getConfigsByType("zoo.cfg").size());
        java.util.List<org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.ConfigurationKeyValue> configurations = new java.util.ArrayList<>();
        org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.ConfigurationKeyValue keyValue = new org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.ConfigurationKeyValue();
        configurations.add(keyValue);
        keyValue.key = "initLimit";
        keyValue.value = "11";
        c.setCurrentStackVersion(repoVersion2110.getStackId());
        c.setDesiredStackVersion(repoVersion2111.getStackId());
        createUpgrade(c, repoVersion2111);
        java.util.Map<java.lang.String, java.lang.String> commandParams = new java.util.HashMap<>();
        commandParams.put("clusterName", "c1");
        commandParams.put(org.apache.ambari.server.stack.upgrade.CreateAndConfigureTask.PARAMETER_CONFIG_TYPE, "zoo.cfg");
        commandParams.put(org.apache.ambari.server.stack.upgrade.CreateAndConfigureTask.PARAMETER_KEY_VALUE_PAIRS, new com.google.gson.Gson().toJson(configurations));
        org.apache.ambari.server.agent.ExecutionCommand executionCommand = getExecutionCommand(commandParams);
        org.apache.ambari.server.actionmanager.HostRoleCommand hostRoleCommand = hostRoleCommandFactory.create(null, null, null, null);
        hostRoleCommand.setExecutionCommandWrapper(new org.apache.ambari.server.actionmanager.ExecutionCommandWrapper(executionCommand));
        action.setExecutionCommand(executionCommand);
        action.setHostRoleCommand(hostRoleCommand);
        org.apache.ambari.server.agent.CommandReport report = action.execute(null);
        org.junit.Assert.assertNotNull(report);
        org.junit.Assert.assertEquals(3, c.getConfigsByType("zoo.cfg").size());
        config = c.getDesiredConfigByType("zoo.cfg");
        org.junit.Assert.assertNotNull(config);
        org.junit.Assert.assertFalse(org.apache.commons.lang.StringUtils.equals("version2", config.getTag()));
        org.junit.Assert.assertEquals("11", config.getProperties().get("initLimit"));
    }

    private void makeUpgradeCluster() throws java.lang.Exception {
        java.lang.String clusterName = "c1";
        java.lang.String hostName = "h1";
        clusters.addCluster(clusterName, repoVersion2110.getStackId());
        org.apache.ambari.server.state.Cluster c = clusters.getCluster(clusterName);
        clusters.addHost(hostName);
        org.apache.ambari.server.state.Host host = clusters.getHost(hostName);
        java.util.Map<java.lang.String, java.lang.String> hostAttributes = new java.util.HashMap<>();
        hostAttributes.put("os_family", "redhat");
        hostAttributes.put("os_release_version", "6");
        host.setHostAttributes(hostAttributes);
        clusters.mapHostToCluster(hostName, clusterName);
        clusters.updateHostMappings(host);
        org.apache.ambari.server.state.Service zk = installService(c, "ZOOKEEPER", repoVersion2110);
        addServiceComponent(c, zk, "ZOOKEEPER_SERVER");
        addServiceComponent(c, zk, "ZOOKEEPER_CLIENT");
        createNewServiceComponentHost(c, "ZOOKEEPER", "ZOOKEEPER_SERVER", hostName);
        createNewServiceComponentHost(c, "ZOOKEEPER", "ZOOKEEPER_CLIENT", hostName);
        java.util.Map<java.lang.String, java.lang.String> properties = new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put("initLimit", "10");
            }
        };
        org.apache.ambari.server.state.Config config = createConfig(c, "zoo.cfg", "version1", properties);
        c.addDesiredConfig("user", java.util.Collections.singleton(config));
        java.lang.String tickTime = m_configHelper.getPropertyValueFromStackDefinitions(c, "zoo.cfg", "tickTime");
        org.junit.Assert.assertNotNull(tickTime);
    }

    private org.apache.ambari.server.state.Service installService(org.apache.ambari.server.state.Cluster cluster, java.lang.String serviceName, org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.Service service = null;
        try {
            service = cluster.getService(serviceName);
        } catch (org.apache.ambari.server.ServiceNotFoundException e) {
            service = serviceFactory.createNew(cluster, serviceName, repositoryVersion);
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

    private org.apache.ambari.server.state.ServiceComponentHost createNewServiceComponentHost(org.apache.ambari.server.state.Cluster cluster, java.lang.String serviceName, java.lang.String svcComponent, java.lang.String hostName) throws org.apache.ambari.server.AmbariException {
        org.junit.Assert.assertNotNull(cluster.getConfigGroups());
        org.apache.ambari.server.state.Service s = cluster.getService(serviceName);
        org.apache.ambari.server.state.ServiceComponent sc = addServiceComponent(cluster, s, svcComponent);
        org.apache.ambari.server.state.ServiceComponentHost sch = serviceComponentHostFactory.createNew(sc, hostName);
        sc.addServiceComponentHost(sch);
        sch.setDesiredState(org.apache.ambari.server.state.State.INSTALLED);
        sch.setState(org.apache.ambari.server.state.State.INSTALLED);
        return sch;
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

    private org.apache.ambari.server.agent.ExecutionCommand getExecutionCommand(java.util.Map<java.lang.String, java.lang.String> commandParams) {
        org.apache.ambari.server.agent.ExecutionCommand executionCommand = new org.apache.ambari.server.agent.ExecutionCommand();
        executionCommand.setClusterName("c1");
        executionCommand.setCommandParams(commandParams);
        executionCommand.setRoleParams(new java.util.HashMap<java.lang.String, java.lang.String>());
        executionCommand.getRoleParams().put(org.apache.ambari.server.serveraction.ServerAction.ACTION_USER_NAME, "username");
        return executionCommand;
    }

    private org.apache.ambari.server.state.Config createConfig(org.apache.ambari.server.state.Cluster cluster, java.lang.String type, java.lang.String tag, java.util.Map<java.lang.String, java.lang.String> properties) {
        return configFactory.createNew(cluster, type, tag, properties, NO_ATTRIBUTES);
    }
}