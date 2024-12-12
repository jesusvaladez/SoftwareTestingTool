package org.apache.ambari.server.serveraction.upgrades;
import javax.persistence.EntityManager;
public class ConfigureActionTest {
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
    private org.apache.ambari.server.serveraction.upgrades.ConfigureAction action;

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
    public void testNewConfigCreatedWhenUpgradingAcrossStacks() throws java.lang.Exception {
        org.apache.ambari.server.state.Cluster c = clusters.getCluster("c1");
        org.junit.Assert.assertEquals(1, c.getConfigsByType("zoo.cfg").size());
        java.util.Map<java.lang.String, java.lang.String> properties = new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put("initLimit", "10");
            }
        };
        org.apache.ambari.server.state.Config config = createConfig(c, repoVersion2110, "zoo.cfg", "version2", properties);
        c.addDesiredConfig("user", java.util.Collections.singleton(config));
        org.junit.Assert.assertEquals(2, c.getConfigsByType("zoo.cfg").size());
        java.util.List<org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.ConfigurationKeyValue> configurations = new java.util.ArrayList<>();
        org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.ConfigurationKeyValue keyValue = new org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.ConfigurationKeyValue();
        configurations.add(keyValue);
        keyValue.key = "initLimit";
        keyValue.value = "11";
        createUpgrade(c, repoVersion2200);
        java.util.Map<java.lang.String, java.lang.String> commandParams = new java.util.HashMap<>();
        commandParams.put("clusterName", "c1");
        commandParams.put(org.apache.ambari.server.stack.upgrade.ConfigureTask.PARAMETER_CONFIG_TYPE, "zoo.cfg");
        commandParams.put(org.apache.ambari.server.stack.upgrade.ConfigureTask.PARAMETER_KEY_VALUE_PAIRS, new com.google.gson.Gson().toJson(configurations));
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
        org.junit.Assert.assertFalse(org.apache.commons.lang3.StringUtils.equals("version2", config.getTag()));
        org.junit.Assert.assertEquals("11", config.getProperties().get("initLimit"));
    }

    @org.junit.Test
    public void testConfigurationWithTargetStackUsed() throws java.lang.Exception {
        org.apache.ambari.server.state.Cluster c = clusters.getCluster("c1");
        org.junit.Assert.assertEquals(1, c.getConfigsByType("zoo.cfg").size());
        java.util.Map<java.lang.String, java.lang.String> properties = new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put("initLimit", "10");
            }
        };
        org.apache.ambari.server.state.Config config = createConfig(c, repoVersion2200, "zoo.cfg", "version2", properties);
        c.addDesiredConfig("user", java.util.Collections.singleton(config));
        org.junit.Assert.assertEquals(2, c.getConfigsByType("zoo.cfg").size());
        java.util.List<org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.ConfigurationKeyValue> configurations = new java.util.ArrayList<>();
        org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.ConfigurationKeyValue keyValue = new org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.ConfigurationKeyValue();
        configurations.add(keyValue);
        keyValue.key = "initLimit";
        keyValue.value = "11";
        createUpgrade(c, repoVersion2200);
        java.util.Map<java.lang.String, java.lang.String> commandParams = new java.util.HashMap<>();
        commandParams.put("clusterName", "c1");
        commandParams.put(org.apache.ambari.server.stack.upgrade.ConfigureTask.PARAMETER_CONFIG_TYPE, "zoo.cfg");
        commandParams.put(org.apache.ambari.server.stack.upgrade.ConfigureTask.PARAMETER_KEY_VALUE_PAIRS, new com.google.gson.Gson().toJson(configurations));
        org.apache.ambari.server.agent.ExecutionCommand executionCommand = getExecutionCommand(commandParams);
        org.apache.ambari.server.actionmanager.HostRoleCommand hostRoleCommand = hostRoleCommandFactory.create(null, null, null, null);
        hostRoleCommand.setExecutionCommandWrapper(new org.apache.ambari.server.actionmanager.ExecutionCommandWrapper(executionCommand));
        action.setExecutionCommand(executionCommand);
        action.setHostRoleCommand(hostRoleCommand);
        org.apache.ambari.server.agent.CommandReport report = action.execute(null);
        org.junit.Assert.assertNotNull(report);
        org.junit.Assert.assertEquals(2, c.getConfigsByType("zoo.cfg").size());
        config = c.getDesiredConfigByType("zoo.cfg");
        org.junit.Assert.assertNotNull(config);
        org.junit.Assert.assertEquals("version2", config.getTag());
        org.junit.Assert.assertEquals("11", config.getProperties().get("initLimit"));
    }

    @org.junit.Test
    public void testDeletePreserveChanges() throws java.lang.Exception {
        org.apache.ambari.server.state.Cluster c = clusters.getCluster("c1");
        org.junit.Assert.assertEquals(1, c.getConfigsByType("zoo.cfg").size());
        java.util.Map<java.lang.String, java.lang.String> properties = new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put("tickTime", "2000");
                put("foo", "bar");
            }
        };
        org.apache.ambari.server.state.Config config = createConfig(c, repoVersion2110, "zoo.cfg", "version2", properties);
        c.addDesiredConfig("user", java.util.Collections.singleton(config));
        org.junit.Assert.assertEquals(2, c.getConfigsByType("zoo.cfg").size());
        java.util.Map<java.lang.String, java.lang.String> commandParams = new java.util.HashMap<>();
        commandParams.put("clusterName", "c1");
        commandParams.put(org.apache.ambari.server.stack.upgrade.ConfigureTask.PARAMETER_CONFIG_TYPE, "zoo.cfg");
        createUpgrade(c, repoVersion2111);
        java.util.List<org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Transfer> transfers = new java.util.ArrayList<>();
        org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Transfer transfer = new org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Transfer();
        transfer.operation = org.apache.ambari.server.stack.upgrade.TransferOperation.DELETE;
        transfer.deleteKey = "*";
        transfer.preserveEdits = true;
        transfers.add(transfer);
        commandParams.put(org.apache.ambari.server.stack.upgrade.ConfigureTask.PARAMETER_TRANSFERS, new com.google.gson.Gson().toJson(transfers));
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
        org.junit.Assert.assertFalse("version2".equals(config.getTag()));
        java.util.Map<java.lang.String, java.lang.String> map = config.getProperties();
        org.junit.Assert.assertEquals("bar", map.get("foo"));
        org.junit.Assert.assertFalse(map.containsKey("tickTime"));
    }

    @org.junit.Test
    public void testConfigTransferCopy() throws java.lang.Exception {
        org.apache.ambari.server.state.Cluster c = clusters.getCluster("c1");
        org.junit.Assert.assertEquals(1, c.getConfigsByType("zoo.cfg").size());
        java.util.Map<java.lang.String, java.lang.String> properties = new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put("initLimit", "10");
                put("copyIt", "10");
                put("moveIt", "10");
                put("deleteIt", "10");
            }
        };
        org.apache.ambari.server.state.Config config = createConfig(c, repoVersion2110, "zoo.cfg", "version2", properties);
        c.addDesiredConfig("user", java.util.Collections.singleton(config));
        org.junit.Assert.assertEquals(2, c.getConfigsByType("zoo.cfg").size());
        java.util.List<org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.ConfigurationKeyValue> configurations = new java.util.ArrayList<>();
        org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.ConfigurationKeyValue keyValue = new org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.ConfigurationKeyValue();
        configurations.add(keyValue);
        keyValue.key = "initLimit";
        keyValue.value = "11";
        createUpgrade(c, repoVersion2111);
        java.util.Map<java.lang.String, java.lang.String> commandParams = new java.util.HashMap<>();
        commandParams.put("clusterName", "c1");
        commandParams.put(org.apache.ambari.server.stack.upgrade.ConfigureTask.PARAMETER_CONFIG_TYPE, "zoo.cfg");
        commandParams.put(org.apache.ambari.server.stack.upgrade.ConfigureTask.PARAMETER_KEY_VALUE_PAIRS, new com.google.gson.Gson().toJson(configurations));
        java.util.List<org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Transfer> transfers = new java.util.ArrayList<>();
        org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Transfer transfer = new org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Transfer();
        transfer.operation = org.apache.ambari.server.stack.upgrade.TransferOperation.COPY;
        transfer.fromKey = "copyIt";
        transfer.toKey = "copyKey";
        transfers.add(transfer);
        transfer = new org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Transfer();
        transfer.operation = org.apache.ambari.server.stack.upgrade.TransferOperation.COPY;
        transfer.fromKey = "copiedFromMissingKeyWithDefault";
        transfer.toKey = "copiedToMissingKeyWithDefault";
        transfer.defaultValue = "defaultValue";
        transfers.add(transfer);
        transfer = new org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Transfer();
        transfer.operation = org.apache.ambari.server.stack.upgrade.TransferOperation.MOVE;
        transfer.fromKey = "moveIt";
        transfer.toKey = "movedKey";
        transfers.add(transfer);
        transfer = new org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Transfer();
        transfer.operation = org.apache.ambari.server.stack.upgrade.TransferOperation.MOVE;
        transfer.fromKey = "movedFromKeyMissingWithDefault";
        transfer.toKey = "movedToMissingWithDefault";
        transfer.defaultValue = "defaultValue2";
        transfer.mask = true;
        transfers.add(transfer);
        transfer = new org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Transfer();
        transfer.operation = org.apache.ambari.server.stack.upgrade.TransferOperation.DELETE;
        transfer.deleteKey = "deleteIt";
        transfers.add(transfer);
        commandParams.put(org.apache.ambari.server.stack.upgrade.ConfigureTask.PARAMETER_TRANSFERS, new com.google.gson.Gson().toJson(transfers));
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
        org.junit.Assert.assertFalse("version2".equals(config.getTag()));
        java.util.Map<java.lang.String, java.lang.String> map = config.getProperties();
        org.junit.Assert.assertEquals("11", map.get("initLimit"));
        org.junit.Assert.assertEquals("10", map.get("copyIt"));
        org.junit.Assert.assertTrue(map.containsKey("copyKey"));
        org.junit.Assert.assertEquals(map.get("copyIt"), map.get("copyKey"));
        org.junit.Assert.assertFalse(map.containsKey("moveIt"));
        org.junit.Assert.assertTrue(map.containsKey("movedKey"));
        org.junit.Assert.assertFalse(map.containsKey("deletedKey"));
        org.junit.Assert.assertTrue(map.containsKey("copiedToMissingKeyWithDefault"));
        org.junit.Assert.assertEquals("defaultValue", map.get("copiedToMissingKeyWithDefault"));
        org.junit.Assert.assertTrue(map.containsKey("movedToMissingWithDefault"));
        org.junit.Assert.assertEquals("defaultValue2", map.get("movedToMissingWithDefault"));
        transfers.clear();
        transfer = new org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Transfer();
        transfer.operation = org.apache.ambari.server.stack.upgrade.TransferOperation.DELETE;
        transfer.deleteKey = "*";
        transfer.preserveEdits = true;
        transfer.keepKeys.add("copyKey");
        transfer.keepKeys.add("keyNotExisting");
        transfer.keepKeys.add(null);
        transfers.add(transfer);
        commandParams.put(org.apache.ambari.server.stack.upgrade.ConfigureTask.PARAMETER_TRANSFERS, new com.google.gson.Gson().toJson(transfers));
        report = action.execute(null);
        org.junit.Assert.assertNotNull(report);
        org.junit.Assert.assertEquals(4, c.getConfigsByType("zoo.cfg").size());
        config = c.getDesiredConfigByType("zoo.cfg");
        map = config.getProperties();
        org.junit.Assert.assertEquals(6, map.size());
        org.junit.Assert.assertTrue(map.containsKey("initLimit"));
        org.junit.Assert.assertTrue(map.containsKey("copyKey"));
        org.junit.Assert.assertFalse(map.containsKey("keyNotExisting"));
        org.junit.Assert.assertFalse(map.containsKey(null));
    }

    @org.junit.Test
    public void testCoerceValueOnCopy() throws java.lang.Exception {
        org.apache.ambari.server.state.Cluster c = clusters.getCluster("c1");
        org.junit.Assert.assertEquals(1, c.getConfigsByType("zoo.cfg").size());
        java.util.Map<java.lang.String, java.lang.String> properties = new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put("zoo.server.csv", "c6401,c6402,  c6403");
            }
        };
        org.apache.ambari.server.state.Config config = createConfig(c, repoVersion2110, "zoo.cfg", "version2", properties);
        c.addDesiredConfig("user", java.util.Collections.singleton(config));
        org.junit.Assert.assertEquals(2, c.getConfigsByType("zoo.cfg").size());
        createUpgrade(c, repoVersion2111);
        java.util.Map<java.lang.String, java.lang.String> commandParams = new java.util.HashMap<>();
        commandParams.put("clusterName", "c1");
        commandParams.put(org.apache.ambari.server.stack.upgrade.ConfigureTask.PARAMETER_CONFIG_TYPE, "zoo.cfg");
        java.util.List<org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Transfer> transfers = new java.util.ArrayList<>();
        org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Transfer transfer = new org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Transfer();
        transfer.operation = org.apache.ambari.server.stack.upgrade.TransferOperation.COPY;
        transfer.coerceTo = org.apache.ambari.server.stack.upgrade.TransferCoercionType.YAML_ARRAY;
        transfer.fromKey = "zoo.server.csv";
        transfer.toKey = "zoo.server.array";
        transfer.defaultValue = "['foo','bar']";
        transfers.add(transfer);
        commandParams.put(org.apache.ambari.server.stack.upgrade.ConfigureTask.PARAMETER_TRANSFERS, new com.google.gson.Gson().toJson(transfers));
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
        org.junit.Assert.assertFalse("version2".equals(config.getTag()));
        java.util.Map<java.lang.String, java.lang.String> map = config.getProperties();
        org.junit.Assert.assertEquals("c6401,c6402,  c6403", map.get("zoo.server.csv"));
        org.junit.Assert.assertEquals("['c6401','c6402','c6403']", map.get("zoo.server.array"));
    }

    @org.junit.Test
    public void testValueReplacement() throws java.lang.Exception {
        org.apache.ambari.server.state.Cluster c = clusters.getCluster("c1");
        org.junit.Assert.assertEquals(1, c.getConfigsByType("zoo.cfg").size());
        java.util.Map<java.lang.String, java.lang.String> properties = new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put("key_to_replace", "My New Cat");
                put("key_with_no_match", "WxyAndZ");
            }
        };
        org.apache.ambari.server.state.Config config = createConfig(c, repoVersion2110, "zoo.cfg", "version2", properties);
        c.addDesiredConfig("user", java.util.Collections.singleton(config));
        org.junit.Assert.assertEquals(2, c.getConfigsByType("zoo.cfg").size());
        createUpgrade(c, repoVersion2111);
        java.util.Map<java.lang.String, java.lang.String> commandParams = new java.util.HashMap<>();
        commandParams.put("clusterName", "c1");
        commandParams.put(org.apache.ambari.server.stack.upgrade.ConfigureTask.PARAMETER_CONFIG_TYPE, "zoo.cfg");
        java.util.List<org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Replace> replacements = new java.util.ArrayList<>();
        org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Replace replace = new org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Replace();
        replace.key = "key_to_replace";
        replace.find = "New Cat";
        replace.replaceWith = "Wet Dog";
        replacements.add(replace);
        replace = new org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Replace();
        replace.key = "key_with_no_match";
        replace.find = "abc";
        replace.replaceWith = "def";
        replacements.add(replace);
        commandParams.put(org.apache.ambari.server.stack.upgrade.ConfigureTask.PARAMETER_REPLACEMENTS, new com.google.gson.Gson().toJson(replacements));
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
        org.junit.Assert.assertFalse("version2".equals(config.getTag()));
        org.junit.Assert.assertEquals("My Wet Dog", config.getProperties().get("key_to_replace"));
        org.junit.Assert.assertEquals("WxyAndZ", config.getProperties().get("key_with_no_match"));
    }

    @org.junit.Test
    public void testValueReplacementWithMissingConfigurations() throws java.lang.Exception {
        org.apache.ambari.server.state.Cluster c = clusters.getCluster("c1");
        org.junit.Assert.assertEquals(1, c.getConfigsByType("zoo.cfg").size());
        java.util.Map<java.lang.String, java.lang.String> properties = new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put("existing", "This exists!");
                put("missing", null);
            }
        };
        org.apache.ambari.server.state.Config config = createConfig(c, repoVersion2110, "zoo.cfg", "version2", properties);
        c.addDesiredConfig("user", java.util.Collections.singleton(config));
        org.junit.Assert.assertEquals(2, c.getConfigsByType("zoo.cfg").size());
        createUpgrade(c, repoVersion2111);
        java.util.Map<java.lang.String, java.lang.String> commandParams = new java.util.HashMap<>();
        commandParams.put("clusterName", "c1");
        commandParams.put(org.apache.ambari.server.stack.upgrade.ConfigureTask.PARAMETER_CONFIG_TYPE, "zoo.cfg");
        java.util.List<org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Replace> replacements = new java.util.ArrayList<>();
        org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Replace replace = new org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Replace();
        replace.key = "missing";
        replace.find = "foo";
        replace.replaceWith = "bar";
        replacements.add(replace);
        commandParams.put(org.apache.ambari.server.stack.upgrade.ConfigureTask.PARAMETER_REPLACEMENTS, new com.google.gson.Gson().toJson(replacements));
        org.apache.ambari.server.agent.ExecutionCommand executionCommand = new org.apache.ambari.server.agent.ExecutionCommand();
        executionCommand.setCommandParams(commandParams);
        executionCommand.setClusterName("c1");
        executionCommand.setRoleParams(new java.util.HashMap<>());
        executionCommand.getRoleParams().put(org.apache.ambari.server.serveraction.ServerAction.ACTION_USER_NAME, "username");
        org.apache.ambari.server.actionmanager.HostRoleCommand hostRoleCommand = hostRoleCommandFactory.create(null, null, null, null);
        hostRoleCommand.setExecutionCommandWrapper(new org.apache.ambari.server.actionmanager.ExecutionCommandWrapper(executionCommand));
        action.setExecutionCommand(executionCommand);
        action.setHostRoleCommand(hostRoleCommand);
        org.apache.ambari.server.agent.CommandReport report = action.execute(null);
        org.junit.Assert.assertNotNull(report);
        org.junit.Assert.assertEquals(3, c.getConfigsByType("zoo.cfg").size());
        config = c.getDesiredConfigByType("zoo.cfg");
        org.junit.Assert.assertEquals(null, config.getProperties().get("missing"));
    }

    @org.junit.Test
    public void testMultipleKeyValuesPerTask() throws java.lang.Exception {
        org.apache.ambari.server.state.Cluster c = clusters.getCluster("c1");
        org.junit.Assert.assertEquals(1, c.getConfigsByType("zoo.cfg").size());
        java.util.Map<java.lang.String, java.lang.String> properties = new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put("fooKey", "barValue");
            }
        };
        org.apache.ambari.server.state.Config config = createConfig(c, repoVersion2200, "zoo.cfg", "version2", properties);
        c.addDesiredConfig("user", java.util.Collections.singleton(config));
        org.junit.Assert.assertEquals(2, c.getConfigsByType("zoo.cfg").size());
        java.util.List<org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.ConfigurationKeyValue> configurations = new java.util.ArrayList<>();
        org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.ConfigurationKeyValue fooKey2 = new org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.ConfigurationKeyValue();
        configurations.add(fooKey2);
        fooKey2.key = "fooKey2";
        fooKey2.value = "barValue2";
        org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.ConfigurationKeyValue fooKey3 = new org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.ConfigurationKeyValue();
        configurations.add(fooKey3);
        fooKey3.key = "fooKey3";
        fooKey3.value = "barValue3";
        fooKey3.mask = true;
        createUpgrade(c, repoVersion2200);
        java.util.Map<java.lang.String, java.lang.String> commandParams = new java.util.HashMap<>();
        commandParams.put("clusterName", "c1");
        commandParams.put(org.apache.ambari.server.stack.upgrade.ConfigureTask.PARAMETER_CONFIG_TYPE, "zoo.cfg");
        commandParams.put(org.apache.ambari.server.stack.upgrade.ConfigureTask.PARAMETER_KEY_VALUE_PAIRS, new com.google.gson.Gson().toJson(configurations));
        org.apache.ambari.server.agent.ExecutionCommand executionCommand = getExecutionCommand(commandParams);
        org.apache.ambari.server.actionmanager.HostRoleCommand hostRoleCommand = hostRoleCommandFactory.create(null, null, null, null);
        hostRoleCommand.setExecutionCommandWrapper(new org.apache.ambari.server.actionmanager.ExecutionCommandWrapper(executionCommand));
        action.setExecutionCommand(executionCommand);
        action.setHostRoleCommand(hostRoleCommand);
        org.apache.ambari.server.agent.CommandReport report = action.execute(null);
        org.junit.Assert.assertNotNull(report);
        org.junit.Assert.assertEquals(2, c.getConfigsByType("zoo.cfg").size());
        config = c.getDesiredConfigByType("zoo.cfg");
        org.junit.Assert.assertNotNull(config);
        org.junit.Assert.assertEquals("barValue", config.getProperties().get("fooKey"));
        org.junit.Assert.assertEquals("barValue2", config.getProperties().get("fooKey2"));
        org.junit.Assert.assertEquals("barValue3", config.getProperties().get("fooKey3"));
        org.junit.Assert.assertTrue(report.getStdOut().contains("******"));
    }

    @org.junit.Test
    public void testAllowedSet() throws java.lang.Exception {
        org.apache.ambari.server.state.Cluster c = clusters.getCluster("c1");
        org.junit.Assert.assertEquals(1, c.getConfigsByType("zoo.cfg").size());
        java.util.Map<java.lang.String, java.lang.String> properties = new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put("set.key.1", "s1");
                put("set.key.2", "s2");
                put("set.key.3", "s3");
                put("set.key.4", "s4");
            }
        };
        org.apache.ambari.server.state.Config config = createConfig(c, repoVersion2200, "zoo.cfg", "version2", properties);
        c.addDesiredConfig("user", java.util.Collections.singleton(config));
        org.junit.Assert.assertEquals(2, c.getConfigsByType("zoo.cfg").size());
        java.util.List<org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.ConfigurationKeyValue> configurations = new java.util.ArrayList<>();
        org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.ConfigurationKeyValue fooKey1 = new org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.ConfigurationKeyValue();
        configurations.add(fooKey1);
        fooKey1.key = "fooKey1";
        fooKey1.value = "barValue1";
        org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.ConfigurationKeyValue fooKey2 = new org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.ConfigurationKeyValue();
        configurations.add(fooKey2);
        fooKey2.key = "fooKey2";
        fooKey2.value = "barValue2";
        org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.ConfigurationKeyValue fooKey3 = new org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.ConfigurationKeyValue();
        configurations.add(fooKey3);
        fooKey3.key = "fooKey3";
        fooKey3.value = "barValue3";
        fooKey3.ifKey = "set.key.1";
        fooKey3.ifType = "zoo.cfg";
        fooKey3.ifValue = "s1";
        org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.ConfigurationKeyValue fooKey4 = new org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.ConfigurationKeyValue();
        configurations.add(fooKey4);
        fooKey4.key = "fooKey4";
        fooKey4.value = "barValue4";
        fooKey4.ifKey = "set.key.2";
        fooKey4.ifType = "zoo.cfg";
        fooKey4.ifKeyState = org.apache.ambari.server.stack.upgrade.PropertyKeyState.PRESENT;
        org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.ConfigurationKeyValue fooKey5 = new org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.ConfigurationKeyValue();
        configurations.add(fooKey5);
        fooKey5.key = "fooKey5";
        fooKey5.value = "barValue5";
        fooKey5.ifKey = "abc";
        fooKey5.ifType = "zoo.cfg";
        fooKey5.ifKeyState = org.apache.ambari.server.stack.upgrade.PropertyKeyState.ABSENT;
        createUpgrade(c, repoVersion2200);
        java.util.Map<java.lang.String, java.lang.String> commandParams = new java.util.HashMap<>();
        commandParams.put("clusterName", "c1");
        commandParams.put(org.apache.ambari.server.stack.upgrade.ConfigureTask.PARAMETER_CONFIG_TYPE, "zoo.cfg");
        commandParams.put(org.apache.ambari.server.stack.upgrade.ConfigureTask.PARAMETER_KEY_VALUE_PAIRS, new com.google.gson.Gson().toJson(configurations));
        org.apache.ambari.server.agent.ExecutionCommand executionCommand = getExecutionCommand(commandParams);
        org.apache.ambari.server.actionmanager.HostRoleCommand hostRoleCommand = hostRoleCommandFactory.create(null, null, null, null);
        hostRoleCommand.setExecutionCommandWrapper(new org.apache.ambari.server.actionmanager.ExecutionCommandWrapper(executionCommand));
        action.setExecutionCommand(executionCommand);
        action.setHostRoleCommand(hostRoleCommand);
        org.apache.ambari.server.agent.CommandReport report = action.execute(null);
        org.junit.Assert.assertNotNull(report);
        org.junit.Assert.assertEquals(2, c.getConfigsByType("zoo.cfg").size());
        config = c.getDesiredConfigByType("zoo.cfg");
        org.junit.Assert.assertNotNull(config);
        org.junit.Assert.assertEquals("barValue1", config.getProperties().get("fooKey1"));
        org.junit.Assert.assertEquals("barValue2", config.getProperties().get("fooKey2"));
        org.junit.Assert.assertEquals("barValue3", config.getProperties().get("fooKey3"));
        org.junit.Assert.assertEquals("barValue4", config.getProperties().get("fooKey4"));
        org.junit.Assert.assertEquals("barValue5", config.getProperties().get("fooKey5"));
        org.junit.Assert.assertEquals("s1", config.getProperties().get("set.key.1"));
        org.junit.Assert.assertEquals("s2", config.getProperties().get("set.key.2"));
        org.junit.Assert.assertEquals("s3", config.getProperties().get("set.key.3"));
        org.junit.Assert.assertEquals("s4", config.getProperties().get("set.key.4"));
    }

    @org.junit.Test
    public void testDisallowedSet() throws java.lang.Exception {
        org.apache.ambari.server.state.Cluster c = clusters.getCluster("c1");
        org.junit.Assert.assertEquals(1, c.getConfigsByType("zoo.cfg").size());
        java.util.Map<java.lang.String, java.lang.String> properties = new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put("set.key.1", "s1");
                put("set.key.2", "s2");
                put("set.key.3", "s3");
                put("set.key.4", "s4");
            }
        };
        org.apache.ambari.server.state.Config config = createConfig(c, repoVersion2200, "zoo.cfg", "version2", properties);
        c.addDesiredConfig("user", java.util.Collections.singleton(config));
        org.junit.Assert.assertEquals(2, c.getConfigsByType("zoo.cfg").size());
        java.util.List<org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.ConfigurationKeyValue> configurations = new java.util.ArrayList<>();
        org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.ConfigurationKeyValue fooKey3 = new org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.ConfigurationKeyValue();
        configurations.add(fooKey3);
        fooKey3.key = "fooKey3";
        fooKey3.value = "barValue3";
        fooKey3.ifKey = "set.key.1";
        fooKey3.ifType = "zoo.cfg";
        fooKey3.ifValue = "no-such-value";
        org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.ConfigurationKeyValue fooKey4 = new org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.ConfigurationKeyValue();
        configurations.add(fooKey4);
        fooKey4.key = "fooKey4";
        fooKey4.value = "barValue4";
        fooKey4.ifKey = "set.key.2";
        fooKey4.ifType = "zoo.cfg";
        fooKey4.ifKeyState = org.apache.ambari.server.stack.upgrade.PropertyKeyState.ABSENT;
        org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.ConfigurationKeyValue fooKey5 = new org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.ConfigurationKeyValue();
        configurations.add(fooKey5);
        fooKey5.key = "fooKey5";
        fooKey5.value = "barValue5";
        fooKey5.ifKey = "abc";
        fooKey5.ifType = "zoo.cfg";
        fooKey5.ifKeyState = org.apache.ambari.server.stack.upgrade.PropertyKeyState.PRESENT;
        createUpgrade(c, repoVersion2200);
        java.util.Map<java.lang.String, java.lang.String> commandParams = new java.util.HashMap<>();
        commandParams.put("clusterName", "c1");
        commandParams.put(org.apache.ambari.server.stack.upgrade.ConfigureTask.PARAMETER_CONFIG_TYPE, "zoo.cfg");
        commandParams.put(org.apache.ambari.server.stack.upgrade.ConfigureTask.PARAMETER_KEY_VALUE_PAIRS, new com.google.gson.Gson().toJson(configurations));
        org.apache.ambari.server.agent.ExecutionCommand executionCommand = getExecutionCommand(commandParams);
        org.apache.ambari.server.actionmanager.HostRoleCommand hostRoleCommand = hostRoleCommandFactory.create(null, null, null, null);
        hostRoleCommand.setExecutionCommandWrapper(new org.apache.ambari.server.actionmanager.ExecutionCommandWrapper(executionCommand));
        action.setExecutionCommand(executionCommand);
        action.setHostRoleCommand(hostRoleCommand);
        org.apache.ambari.server.agent.CommandReport report = action.execute(null);
        org.junit.Assert.assertNotNull(report);
        org.junit.Assert.assertEquals(2, c.getConfigsByType("zoo.cfg").size());
        config = c.getDesiredConfigByType("zoo.cfg");
        org.junit.Assert.assertNotNull(config);
        org.junit.Assert.assertEquals("s1", config.getProperties().get("set.key.1"));
        org.junit.Assert.assertEquals("s2", config.getProperties().get("set.key.2"));
        org.junit.Assert.assertEquals("s3", config.getProperties().get("set.key.3"));
        org.junit.Assert.assertEquals("s4", config.getProperties().get("set.key.4"));
        org.junit.Assert.assertFalse(config.getProperties().containsKey("fooKey3"));
        org.junit.Assert.assertFalse(config.getProperties().containsKey("fooKey4"));
        org.junit.Assert.assertFalse(config.getProperties().containsKey("fooKey5"));
    }

    @org.junit.Test
    public void testAllowedReplacment() throws java.lang.Exception {
        org.apache.ambari.server.state.Cluster c = clusters.getCluster("c1");
        org.junit.Assert.assertEquals(1, c.getConfigsByType("zoo.cfg").size());
        java.util.Map<java.lang.String, java.lang.String> properties = new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put("replace.key.1", "r1");
                put("replace.key.2", "r2");
                put("replace.key.3", "r3a1");
                put("replace.key.4", "r4");
                put("replace.key.5", "r5");
            }
        };
        org.apache.ambari.server.state.Config config = createConfig(c, repoVersion2200, "zoo.cfg", "version2", properties);
        c.addDesiredConfig("user", java.util.Collections.singleton(config));
        org.junit.Assert.assertEquals(2, c.getConfigsByType("zoo.cfg").size());
        java.util.List<org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Replace> replacements = new java.util.ArrayList<>();
        org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Replace replace = new org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Replace();
        replace.key = "replace.key.3";
        replace.find = "a";
        replace.replaceWith = "A";
        replacements.add(replace);
        org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Replace replace2 = new org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Replace();
        replacements.add(replace2);
        replace2.key = "replace.key.4";
        replace2.find = "r";
        replace2.replaceWith = "R";
        replace2.ifKey = "replace.key.1";
        replace2.ifType = "zoo.cfg";
        replace2.ifValue = "r1";
        replacements.add(replace2);
        org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Replace replace3 = new org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Replace();
        replacements.add(replace3);
        replace3.key = "replace.key.2";
        replace3.find = "r";
        replace3.replaceWith = "R";
        replace3.ifKey = "replace.key.1";
        replace3.ifType = "zoo.cfg";
        replace3.ifKeyState = org.apache.ambari.server.stack.upgrade.PropertyKeyState.PRESENT;
        replacements.add(replace3);
        org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Replace replace4 = new org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Replace();
        replacements.add(replace3);
        replace4.key = "replace.key.5";
        replace4.find = "r";
        replace4.replaceWith = "R";
        replace4.ifKey = "no.such.key";
        replace4.ifType = "zoo.cfg";
        replace4.ifKeyState = org.apache.ambari.server.stack.upgrade.PropertyKeyState.ABSENT;
        replacements.add(replace4);
        createUpgrade(c, repoVersion2200);
        java.util.Map<java.lang.String, java.lang.String> commandParams = new java.util.HashMap<>();
        commandParams.put("clusterName", "c1");
        commandParams.put(org.apache.ambari.server.stack.upgrade.ConfigureTask.PARAMETER_CONFIG_TYPE, "zoo.cfg");
        commandParams.put(org.apache.ambari.server.stack.upgrade.ConfigureTask.PARAMETER_REPLACEMENTS, new com.google.gson.Gson().toJson(replacements));
        org.apache.ambari.server.agent.ExecutionCommand executionCommand = getExecutionCommand(commandParams);
        org.apache.ambari.server.actionmanager.HostRoleCommand hostRoleCommand = hostRoleCommandFactory.create(null, null, null, null);
        hostRoleCommand.setExecutionCommandWrapper(new org.apache.ambari.server.actionmanager.ExecutionCommandWrapper(executionCommand));
        action.setExecutionCommand(executionCommand);
        action.setHostRoleCommand(hostRoleCommand);
        org.apache.ambari.server.agent.CommandReport report = action.execute(null);
        org.junit.Assert.assertNotNull(report);
        org.junit.Assert.assertEquals(2, c.getConfigsByType("zoo.cfg").size());
        config = c.getDesiredConfigByType("zoo.cfg");
        org.junit.Assert.assertNotNull(config);
        org.junit.Assert.assertEquals("r1", config.getProperties().get("replace.key.1"));
        org.junit.Assert.assertEquals("R2", config.getProperties().get("replace.key.2"));
        org.junit.Assert.assertEquals("r3A1", config.getProperties().get("replace.key.3"));
        org.junit.Assert.assertEquals("R4", config.getProperties().get("replace.key.4"));
        org.junit.Assert.assertEquals("R5", config.getProperties().get("replace.key.5"));
    }

    @org.junit.Test
    public void testDisallowedReplacment() throws java.lang.Exception {
        org.apache.ambari.server.state.Cluster c = clusters.getCluster("c1");
        org.junit.Assert.assertEquals(1, c.getConfigsByType("zoo.cfg").size());
        java.util.Map<java.lang.String, java.lang.String> properties = new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put("replace.key.1", "r1");
                put("replace.key.2", "r2");
                put("replace.key.3", "r3a1");
                put("replace.key.4", "r4");
                put("replace.key.5", "r5");
            }
        };
        org.apache.ambari.server.state.Config config = createConfig(c, repoVersion2110, "zoo.cfg", "version2", properties);
        c.addDesiredConfig("user", java.util.Collections.singleton(config));
        org.junit.Assert.assertEquals(2, c.getConfigsByType("zoo.cfg").size());
        java.util.List<org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Replace> replacements = new java.util.ArrayList<>();
        org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Replace replace2 = new org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Replace();
        replacements.add(replace2);
        replace2.key = "replace.key.4";
        replace2.find = "r";
        replace2.replaceWith = "R";
        replace2.ifKey = "replace.key.1";
        replace2.ifType = "zoo.cfg";
        replace2.ifValue = "not-this-value";
        replacements.add(replace2);
        org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Replace replace3 = new org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Replace();
        replacements.add(replace3);
        replace3.key = "replace.key.2";
        replace3.find = "r";
        replace3.replaceWith = "R";
        replace3.ifKey = "replace.key.1";
        replace3.ifType = "zoo.cfg";
        replace3.ifKeyState = org.apache.ambari.server.stack.upgrade.PropertyKeyState.ABSENT;
        replacements.add(replace3);
        org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Replace replace4 = new org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Replace();
        replacements.add(replace3);
        replace4.key = "replace.key.5";
        replace4.find = "r";
        replace4.replaceWith = "R";
        replace4.ifKey = "no.such.key";
        replace4.ifType = "zoo.cfg";
        replace4.ifKeyState = org.apache.ambari.server.stack.upgrade.PropertyKeyState.PRESENT;
        replacements.add(replace4);
        createUpgrade(c, repoVersion2200);
        java.util.Map<java.lang.String, java.lang.String> commandParams = new java.util.HashMap<>();
        commandParams.put("clusterName", "c1");
        commandParams.put(org.apache.ambari.server.stack.upgrade.ConfigureTask.PARAMETER_CONFIG_TYPE, "zoo.cfg");
        commandParams.put(org.apache.ambari.server.stack.upgrade.ConfigureTask.PARAMETER_REPLACEMENTS, new com.google.gson.Gson().toJson(replacements));
        org.apache.ambari.server.agent.ExecutionCommand executionCommand = getExecutionCommand(commandParams);
        org.apache.ambari.server.actionmanager.HostRoleCommand hostRoleCommand = hostRoleCommandFactory.create(null, null, null, null);
        hostRoleCommand.setExecutionCommandWrapper(new org.apache.ambari.server.actionmanager.ExecutionCommandWrapper(executionCommand));
        action.setExecutionCommand(executionCommand);
        action.setHostRoleCommand(hostRoleCommand);
        org.apache.ambari.server.agent.CommandReport report = action.execute(null);
        org.junit.Assert.assertNotNull(report);
        org.junit.Assert.assertEquals(2, c.getConfigsByType("zoo.cfg").size());
        config = c.getDesiredConfigByType("zoo.cfg");
        org.junit.Assert.assertNotNull(config);
        org.junit.Assert.assertEquals("r1", config.getProperties().get("replace.key.1"));
        org.junit.Assert.assertEquals("r2", config.getProperties().get("replace.key.2"));
        org.junit.Assert.assertEquals("r3a1", config.getProperties().get("replace.key.3"));
        org.junit.Assert.assertEquals("r4", config.getProperties().get("replace.key.4"));
        org.junit.Assert.assertEquals("r5", config.getProperties().get("replace.key.5"));
    }

    @org.junit.Test
    public void testAllowedTransferCopy() throws java.lang.Exception {
        org.apache.ambari.server.state.Cluster c = clusters.getCluster("c1");
        org.junit.Assert.assertEquals(1, c.getConfigsByType("zoo.cfg").size());
        java.util.Map<java.lang.String, java.lang.String> properties = new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put("initLimit", "10");
                put("copy.key.1", "c1");
                put("copy.key.2", "c2");
            }
        };
        org.apache.ambari.server.state.Config config = createConfig(c, repoVersion2110, "zoo.cfg", "version2", properties);
        c.addDesiredConfig("user", java.util.Collections.singleton(config));
        org.junit.Assert.assertEquals(2, c.getConfigsByType("zoo.cfg").size());
        java.util.List<org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.ConfigurationKeyValue> configurations = new java.util.ArrayList<>();
        org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.ConfigurationKeyValue keyValue = new org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.ConfigurationKeyValue();
        configurations.add(keyValue);
        keyValue.key = "initLimit";
        keyValue.value = "11";
        createUpgrade(c, repoVersion2200);
        java.util.Map<java.lang.String, java.lang.String> commandParams = new java.util.HashMap<>();
        commandParams.put("clusterName", "c1");
        commandParams.put(org.apache.ambari.server.stack.upgrade.ConfigureTask.PARAMETER_CONFIG_TYPE, "zoo.cfg");
        commandParams.put(org.apache.ambari.server.stack.upgrade.ConfigureTask.PARAMETER_KEY_VALUE_PAIRS, new com.google.gson.Gson().toJson(configurations));
        java.util.List<org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Transfer> transfers = new java.util.ArrayList<>();
        org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Transfer transfer1 = new org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Transfer();
        transfer1.operation = org.apache.ambari.server.stack.upgrade.TransferOperation.COPY;
        transfer1.fromKey = "copy.key.1";
        transfer1.toKey = "copy.to.key.1";
        transfers.add(transfer1);
        org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Transfer transfer2 = new org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Transfer();
        transfer2.operation = org.apache.ambari.server.stack.upgrade.TransferOperation.COPY;
        transfer2.fromKey = "copy.key.no.need.to.exit.1";
        transfer2.toKey = "copy.to.key.with.default.1";
        transfer2.defaultValue = "defaultValue";
        transfers.add(transfer2);
        org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Transfer transfer3 = new org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Transfer();
        transfer3.operation = org.apache.ambari.server.stack.upgrade.TransferOperation.COPY;
        transfer3.fromKey = "copy.key.2";
        transfer3.toKey = "copy.to.key.2";
        transfer3.ifKey = "initLimit";
        transfer3.ifType = "zoo.cfg";
        transfer3.ifValue = "10";
        transfers.add(transfer3);
        org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Transfer transfer4 = new org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Transfer();
        transfer4.operation = org.apache.ambari.server.stack.upgrade.TransferOperation.COPY;
        transfer4.fromKey = "copy.key.2";
        transfer4.toKey = "copy.to.key.3";
        transfer4.ifKey = "initLimit";
        transfer4.ifType = "zoo.cfg";
        transfer4.ifKeyState = org.apache.ambari.server.stack.upgrade.PropertyKeyState.PRESENT;
        transfers.add(transfer4);
        org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Transfer transfer5 = new org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Transfer();
        transfer5.operation = org.apache.ambari.server.stack.upgrade.TransferOperation.COPY;
        transfer5.fromKey = "copy.key.no.need.to.exist.2";
        transfer5.toKey = "copy.to.key.with.default.2";
        transfer5.defaultValue = "defaultValue2";
        transfer5.ifKey = "no.such.key";
        transfer5.ifType = "zoo.cfg";
        transfer5.ifKeyState = org.apache.ambari.server.stack.upgrade.PropertyKeyState.ABSENT;
        transfers.add(transfer5);
        commandParams.put(org.apache.ambari.server.stack.upgrade.ConfigureTask.PARAMETER_TRANSFERS, new com.google.gson.Gson().toJson(transfers));
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
        org.junit.Assert.assertFalse("version2".equals(config.getTag()));
        java.util.Map<java.lang.String, java.lang.String> map = config.getProperties();
        org.junit.Assert.assertEquals(8, map.size());
        org.junit.Assert.assertEquals("11", map.get("initLimit"));
        org.junit.Assert.assertEquals(map.get("copy.key.1"), map.get("copy.to.key.1"));
        org.junit.Assert.assertTrue(!map.containsKey("copy.key.no.need.to.exit.1"));
        org.junit.Assert.assertEquals("defaultValue", map.get("copy.to.key.with.default.1"));
        org.junit.Assert.assertTrue(!map.containsKey("copy.key.no.need.to.exit.2"));
        org.junit.Assert.assertEquals("defaultValue2", map.get("copy.to.key.with.default.2"));
        org.junit.Assert.assertEquals(map.get("copy.key.2"), map.get("copy.to.key.2"));
        org.junit.Assert.assertEquals(map.get("copy.key.2"), map.get("copy.to.key.3"));
    }

    @org.junit.Test
    public void testDisallowedTransferCopy() throws java.lang.Exception {
        org.apache.ambari.server.state.Cluster c = clusters.getCluster("c1");
        org.junit.Assert.assertEquals(1, c.getConfigsByType("zoo.cfg").size());
        java.util.Map<java.lang.String, java.lang.String> properties = new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put("initLimit", "10");
                put("copy.key.1", "c1");
                put("copy.key.2", "c2");
            }
        };
        org.apache.ambari.server.state.Config config = createConfig(c, repoVersion2110, "zoo.cfg", "version2", properties);
        c.addDesiredConfig("user", java.util.Collections.singleton(config));
        org.junit.Assert.assertEquals(2, c.getConfigsByType("zoo.cfg").size());
        java.util.List<org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.ConfigurationKeyValue> configurations = new java.util.ArrayList<>();
        org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.ConfigurationKeyValue keyValue = new org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.ConfigurationKeyValue();
        configurations.add(keyValue);
        keyValue.key = "initLimit";
        keyValue.value = "11";
        createUpgrade(c, repoVersion2111);
        java.util.Map<java.lang.String, java.lang.String> commandParams = new java.util.HashMap<>();
        commandParams.put("clusterName", "c1");
        commandParams.put(org.apache.ambari.server.stack.upgrade.ConfigureTask.PARAMETER_CONFIG_TYPE, "zoo.cfg");
        commandParams.put(org.apache.ambari.server.stack.upgrade.ConfigureTask.PARAMETER_KEY_VALUE_PAIRS, new com.google.gson.Gson().toJson(configurations));
        java.util.List<org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Transfer> transfers = new java.util.ArrayList<>();
        org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Transfer transfer = new org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Transfer();
        transfer.operation = org.apache.ambari.server.stack.upgrade.TransferOperation.COPY;
        transfer.fromKey = "copy.key.2";
        transfer.toKey = "copy.to.key.2";
        transfer.ifKey = "initLimit";
        transfer.ifType = "zoo.cfg";
        transfer.ifValue = "not-the-real-value";
        transfers.add(transfer);
        transfer = new org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Transfer();
        transfer.operation = org.apache.ambari.server.stack.upgrade.TransferOperation.COPY;
        transfer.fromKey = "copy.key.2";
        transfer.toKey = "copy.to.key.3";
        transfer.ifKey = "initLimit";
        transfer.ifType = "zoo.cfg";
        transfer.ifKeyState = org.apache.ambari.server.stack.upgrade.PropertyKeyState.ABSENT;
        transfers.add(transfer);
        transfer = new org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Transfer();
        transfer.operation = org.apache.ambari.server.stack.upgrade.TransferOperation.COPY;
        transfer.fromKey = "copy.key.no.need.to.exist.2";
        transfer.toKey = "copy.to.key.with.default.2";
        transfer.defaultValue = "defaultValue2";
        transfer.ifKey = "no.such.key";
        transfer.ifType = "zoo.cfg";
        transfer.ifKeyState = org.apache.ambari.server.stack.upgrade.PropertyKeyState.PRESENT;
        transfers.add(transfer);
        commandParams.put(org.apache.ambari.server.stack.upgrade.ConfigureTask.PARAMETER_TRANSFERS, new com.google.gson.Gson().toJson(transfers));
        org.apache.ambari.server.agent.ExecutionCommand executionCommand = new org.apache.ambari.server.agent.ExecutionCommand();
        executionCommand.setCommandParams(commandParams);
        executionCommand.setClusterName("c1");
        executionCommand.setRoleParams(new java.util.HashMap<>());
        executionCommand.getRoleParams().put(org.apache.ambari.server.serveraction.ServerAction.ACTION_USER_NAME, "username");
        org.apache.ambari.server.actionmanager.HostRoleCommand hostRoleCommand = hostRoleCommandFactory.create(null, null, null, null);
        hostRoleCommand.setExecutionCommandWrapper(new org.apache.ambari.server.actionmanager.ExecutionCommandWrapper(executionCommand));
        action.setExecutionCommand(executionCommand);
        action.setHostRoleCommand(hostRoleCommand);
        org.apache.ambari.server.agent.CommandReport report = action.execute(null);
        org.junit.Assert.assertNotNull(report);
        org.junit.Assert.assertEquals(3, c.getConfigsByType("zoo.cfg").size());
        config = c.getDesiredConfigByType("zoo.cfg");
        org.junit.Assert.assertNotNull(config);
        org.junit.Assert.assertFalse("version2".equals(config.getTag()));
        java.util.Map<java.lang.String, java.lang.String> map = config.getProperties();
        org.junit.Assert.assertEquals(3, map.size());
        org.junit.Assert.assertEquals("11", map.get("initLimit"));
        org.junit.Assert.assertEquals("c1", map.get("copy.key.1"));
        org.junit.Assert.assertEquals("c2", map.get("copy.key.2"));
    }

    @org.junit.Test
    public void testAllowedTransferMove() throws java.lang.Exception {
        org.apache.ambari.server.state.Cluster c = clusters.getCluster("c1");
        org.junit.Assert.assertEquals(1, c.getConfigsByType("zoo.cfg").size());
        java.util.Map<java.lang.String, java.lang.String> properties = new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put("initLimit", "10");
                put("move.key.1", "m1");
                put("move.key.2", "m2");
                put("move.key.3", "m3");
                put("move.key.4", "m4");
            }
        };
        org.apache.ambari.server.state.Config config = createConfig(c, repoVersion2110, "zoo.cfg", "version2", properties);
        c.addDesiredConfig("user", java.util.Collections.singleton(config));
        org.junit.Assert.assertEquals(2, c.getConfigsByType("zoo.cfg").size());
        java.util.List<org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.ConfigurationKeyValue> configurations = new java.util.ArrayList<>();
        org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.ConfigurationKeyValue keyValue = new org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.ConfigurationKeyValue();
        configurations.add(keyValue);
        keyValue.key = "initLimit";
        keyValue.value = "11";
        createUpgrade(c, repoVersion2111);
        java.util.Map<java.lang.String, java.lang.String> commandParams = new java.util.HashMap<>();
        commandParams.put("clusterName", "c1");
        commandParams.put(org.apache.ambari.server.stack.upgrade.ConfigureTask.PARAMETER_CONFIG_TYPE, "zoo.cfg");
        commandParams.put(org.apache.ambari.server.stack.upgrade.ConfigureTask.PARAMETER_KEY_VALUE_PAIRS, new com.google.gson.Gson().toJson(configurations));
        java.util.List<org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Transfer> transfers = new java.util.ArrayList<>();
        org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Transfer transfer1 = new org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Transfer();
        transfer1.operation = org.apache.ambari.server.stack.upgrade.TransferOperation.MOVE;
        transfer1.fromKey = "move.key.1";
        transfer1.toKey = "move.to.key.1";
        transfers.add(transfer1);
        org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Transfer transfer2 = new org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Transfer();
        transfer2.operation = org.apache.ambari.server.stack.upgrade.TransferOperation.MOVE;
        transfer2.fromKey = "move.key.2";
        transfer2.toKey = "move.to.key.2";
        transfer2.ifKey = "initLimit";
        transfer2.ifType = "zoo.cfg";
        transfer2.ifValue = "10";
        transfers.add(transfer2);
        org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Transfer transfer3 = new org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Transfer();
        transfer3.operation = org.apache.ambari.server.stack.upgrade.TransferOperation.MOVE;
        transfer3.fromKey = "move.key.3";
        transfer3.toKey = "move.to.key.3";
        transfer3.ifKey = "initLimit";
        transfer3.ifType = "zoo.cfg";
        transfer3.ifKeyState = org.apache.ambari.server.stack.upgrade.PropertyKeyState.PRESENT;
        transfers.add(transfer3);
        org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Transfer transfer4 = new org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Transfer();
        transfer4.operation = org.apache.ambari.server.stack.upgrade.TransferOperation.MOVE;
        transfer4.fromKey = "move.key.4";
        transfer4.toKey = "move.to.key.4";
        transfer4.ifKey = "no.such.key";
        transfer4.ifType = "zoo.cfg";
        transfer4.ifKeyState = org.apache.ambari.server.stack.upgrade.PropertyKeyState.ABSENT;
        transfers.add(transfer4);
        commandParams.put(org.apache.ambari.server.stack.upgrade.ConfigureTask.PARAMETER_TRANSFERS, new com.google.gson.Gson().toJson(transfers));
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
        org.junit.Assert.assertFalse("version2".equals(config.getTag()));
        java.util.Map<java.lang.String, java.lang.String> map = config.getProperties();
        org.junit.Assert.assertEquals(5, map.size());
        java.lang.String[] shouldNotExitKeys = new java.lang.String[]{ "move.key.1", "move.key.2", "move.key.3", "move.key.4" };
        java.lang.String[] shouldExitKeys = new java.lang.String[]{ "move.to.key.1", "move.to.key.2", "move.to.key.3", "move.to.key.4" };
        for (java.lang.String key : shouldNotExitKeys) {
            org.junit.Assert.assertFalse(map.containsKey(key));
        }
        for (java.lang.String key : shouldExitKeys) {
            org.junit.Assert.assertTrue(map.containsKey(key));
        }
    }

    @org.junit.Test
    public void testDisallowedTransferMove() throws java.lang.Exception {
        org.apache.ambari.server.state.Cluster c = clusters.getCluster("c1");
        org.junit.Assert.assertEquals(1, c.getConfigsByType("zoo.cfg").size());
        java.util.Map<java.lang.String, java.lang.String> properties = new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put("initLimit", "10");
                put("move.key.1", "m1");
                put("move.key.2", "m2");
                put("move.key.3", "m3");
                put("move.key.4", "m4");
            }
        };
        org.apache.ambari.server.state.Config config = createConfig(c, repoVersion2110, "zoo.cfg", "version2", properties);
        c.addDesiredConfig("user", java.util.Collections.singleton(config));
        org.junit.Assert.assertEquals(2, c.getConfigsByType("zoo.cfg").size());
        java.util.List<org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.ConfigurationKeyValue> configurations = new java.util.ArrayList<>();
        org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.ConfigurationKeyValue keyValue = new org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.ConfigurationKeyValue();
        configurations.add(keyValue);
        keyValue.key = "initLimit";
        keyValue.value = "11";
        createUpgrade(c, repoVersion2111);
        java.util.Map<java.lang.String, java.lang.String> commandParams = new java.util.HashMap<>();
        commandParams.put("clusterName", "c1");
        commandParams.put(org.apache.ambari.server.stack.upgrade.ConfigureTask.PARAMETER_CONFIG_TYPE, "zoo.cfg");
        commandParams.put(org.apache.ambari.server.stack.upgrade.ConfigureTask.PARAMETER_KEY_VALUE_PAIRS, new com.google.gson.Gson().toJson(configurations));
        java.util.List<org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Transfer> transfers = new java.util.ArrayList<>();
        org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Transfer transfer2 = new org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Transfer();
        transfer2.operation = org.apache.ambari.server.stack.upgrade.TransferOperation.MOVE;
        transfer2.fromKey = "move.key.2";
        transfer2.toKey = "move.to.key.2";
        transfer2.ifKey = "initLimit";
        transfer2.ifType = "zoo.cfg";
        transfer2.ifValue = "not-real-value";
        transfers.add(transfer2);
        org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Transfer transfer3 = new org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Transfer();
        transfer3.operation = org.apache.ambari.server.stack.upgrade.TransferOperation.MOVE;
        transfer3.fromKey = "move.key.3";
        transfer3.toKey = "move.to.key.3";
        transfer3.ifKey = "initLimit";
        transfer3.ifType = "zoo.cfg";
        transfer3.ifKeyState = org.apache.ambari.server.stack.upgrade.PropertyKeyState.ABSENT;
        transfers.add(transfer3);
        org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Transfer transfer4 = new org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Transfer();
        transfer4.operation = org.apache.ambari.server.stack.upgrade.TransferOperation.MOVE;
        transfer4.fromKey = "move.key.4";
        transfer4.toKey = "move.to.key.4";
        transfer4.ifKey = "no.such.key";
        transfer4.ifType = "zoo.cfg";
        transfer4.ifKeyState = org.apache.ambari.server.stack.upgrade.PropertyKeyState.PRESENT;
        transfers.add(transfer3);
        commandParams.put(org.apache.ambari.server.stack.upgrade.ConfigureTask.PARAMETER_TRANSFERS, new com.google.gson.Gson().toJson(transfers));
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
        org.junit.Assert.assertFalse("version2".equals(config.getTag()));
        java.util.Map<java.lang.String, java.lang.String> map = config.getProperties();
        org.junit.Assert.assertEquals(5, map.size());
        java.lang.String[] shouldExitKeys = new java.lang.String[]{ "move.key.1", "move.key.2", "move.key.3", "move.key.4" };
        java.lang.String[] shouldNotExitKeys = new java.lang.String[]{ "move.to.key.1", "move.to.key.2", "move.to.key.3", "move.to.key.4" };
        for (java.lang.String key : shouldNotExitKeys) {
            org.junit.Assert.assertFalse(map.containsKey(key));
        }
        for (java.lang.String key : shouldExitKeys) {
            org.junit.Assert.assertTrue(map.containsKey(key));
        }
    }

    @org.junit.Test
    public void testAllowedTransferDelete() throws java.lang.Exception {
        org.apache.ambari.server.state.Cluster c = clusters.getCluster("c1");
        org.junit.Assert.assertEquals(1, c.getConfigsByType("zoo.cfg").size());
        java.util.Map<java.lang.String, java.lang.String> properties = new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put("initLimit", "10");
                put("delete.key.1", "d1");
                put("delete.key.2", "d2");
                put("delete.key.3", "d3");
                put("delete.key.4", "d4");
            }
        };
        org.apache.ambari.server.state.Config config = createConfig(c, repoVersion2110, "zoo.cfg", "version2", properties);
        c.addDesiredConfig("user", java.util.Collections.singleton(config));
        org.junit.Assert.assertEquals(2, c.getConfigsByType("zoo.cfg").size());
        java.util.List<org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.ConfigurationKeyValue> configurations = new java.util.ArrayList<>();
        org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.ConfigurationKeyValue keyValue = new org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.ConfigurationKeyValue();
        configurations.add(keyValue);
        keyValue.key = "initLimit";
        keyValue.value = "11";
        createUpgrade(c, repoVersion2111);
        java.util.Map<java.lang.String, java.lang.String> commandParams = new java.util.HashMap<>();
        commandParams.put("clusterName", "c1");
        commandParams.put(org.apache.ambari.server.stack.upgrade.ConfigureTask.PARAMETER_CONFIG_TYPE, "zoo.cfg");
        commandParams.put(org.apache.ambari.server.stack.upgrade.ConfigureTask.PARAMETER_KEY_VALUE_PAIRS, new com.google.gson.Gson().toJson(configurations));
        java.util.List<org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Transfer> transfers = new java.util.ArrayList<>();
        org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Transfer transfer1 = new org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Transfer();
        transfer1.operation = org.apache.ambari.server.stack.upgrade.TransferOperation.DELETE;
        transfer1.deleteKey = "delete.key.1";
        transfers.add(transfer1);
        org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Transfer transfer2 = new org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Transfer();
        transfer2.operation = org.apache.ambari.server.stack.upgrade.TransferOperation.DELETE;
        transfer2.deleteKey = "delete.key.2";
        transfer2.ifKey = "initLimit";
        transfer2.ifType = "zoo.cfg";
        transfer2.ifValue = "10";
        transfers.add(transfer2);
        org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Transfer transfer3 = new org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Transfer();
        transfer3.operation = org.apache.ambari.server.stack.upgrade.TransferOperation.DELETE;
        transfer3.deleteKey = "delete.key.3";
        transfer3.ifKey = "initLimit";
        transfer3.ifType = "zoo.cfg";
        transfer3.ifKeyState = org.apache.ambari.server.stack.upgrade.PropertyKeyState.PRESENT;
        transfers.add(transfer3);
        org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Transfer transfer4 = new org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Transfer();
        transfer4.operation = org.apache.ambari.server.stack.upgrade.TransferOperation.DELETE;
        transfer4.deleteKey = "delete.key.4";
        transfer4.ifKey = "no.such.key";
        transfer4.ifType = "zoo.cfg";
        transfer4.ifKeyState = org.apache.ambari.server.stack.upgrade.PropertyKeyState.ABSENT;
        transfers.add(transfer4);
        commandParams.put(org.apache.ambari.server.stack.upgrade.ConfigureTask.PARAMETER_TRANSFERS, new com.google.gson.Gson().toJson(transfers));
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
        org.junit.Assert.assertFalse("version2".equals(config.getTag()));
        java.util.Map<java.lang.String, java.lang.String> map = config.getProperties();
        org.junit.Assert.assertEquals(1, map.size());
        org.junit.Assert.assertEquals("11", map.get("initLimit"));
        java.lang.String[] shouldNotExitKeys = new java.lang.String[]{ "delete.key.1", "delete.key.2", "delete.key.3", "delete.key.4" };
        for (java.lang.String key : shouldNotExitKeys) {
            org.junit.Assert.assertFalse(map.containsKey(key));
        }
    }

    @org.junit.Test
    public void testDisallowedTransferDelete() throws java.lang.Exception {
        org.apache.ambari.server.state.Cluster c = clusters.getCluster("c1");
        org.junit.Assert.assertEquals(1, c.getConfigsByType("zoo.cfg").size());
        java.util.Map<java.lang.String, java.lang.String> properties = new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put("initLimit", "10");
                put("delete.key.1", "d1");
                put("delete.key.2", "d2");
                put("delete.key.3", "d3");
                put("delete.key.4", "d4");
            }
        };
        org.apache.ambari.server.state.Config config = createConfig(c, repoVersion2110, "zoo.cfg", "version2", properties);
        c.addDesiredConfig("user", java.util.Collections.singleton(config));
        org.junit.Assert.assertEquals(2, c.getConfigsByType("zoo.cfg").size());
        java.util.List<org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.ConfigurationKeyValue> configurations = new java.util.ArrayList<>();
        org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.ConfigurationKeyValue keyValue = new org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.ConfigurationKeyValue();
        configurations.add(keyValue);
        keyValue.key = "initLimit";
        keyValue.value = "11";
        createUpgrade(c, repoVersion2111);
        java.util.Map<java.lang.String, java.lang.String> commandParams = new java.util.HashMap<>();
        commandParams.put("clusterName", "c1");
        commandParams.put(org.apache.ambari.server.stack.upgrade.ConfigureTask.PARAMETER_CONFIG_TYPE, "zoo.cfg");
        commandParams.put(org.apache.ambari.server.stack.upgrade.ConfigureTask.PARAMETER_KEY_VALUE_PAIRS, new com.google.gson.Gson().toJson(configurations));
        java.util.List<org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Transfer> transfers = new java.util.ArrayList<>();
        org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Transfer transfer2 = new org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Transfer();
        transfer2.operation = org.apache.ambari.server.stack.upgrade.TransferOperation.DELETE;
        transfer2.deleteKey = "delete.key.2";
        transfer2.ifKey = "initLimit";
        transfer2.ifType = "zoo.cfg";
        transfer2.ifValue = "not.real.value";
        transfers.add(transfer2);
        org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Transfer transfer3 = new org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Transfer();
        transfer3.operation = org.apache.ambari.server.stack.upgrade.TransferOperation.DELETE;
        transfer3.deleteKey = "delete.key.3";
        transfer3.ifKey = "initLimit";
        transfer3.ifType = "zoo.cfg";
        transfer3.ifKeyState = org.apache.ambari.server.stack.upgrade.PropertyKeyState.ABSENT;
        transfers.add(transfer3);
        org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Transfer transfer4 = new org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Transfer();
        transfer4.operation = org.apache.ambari.server.stack.upgrade.TransferOperation.DELETE;
        transfer4.deleteKey = "delete.key.4";
        transfer4.ifKey = "no.such.key";
        transfer4.ifType = "zoo.cfg";
        transfer4.ifKeyState = org.apache.ambari.server.stack.upgrade.PropertyKeyState.PRESENT;
        transfers.add(transfer4);
        commandParams.put(org.apache.ambari.server.stack.upgrade.ConfigureTask.PARAMETER_TRANSFERS, new com.google.gson.Gson().toJson(transfers));
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
        org.junit.Assert.assertFalse("version2".equals(config.getTag()));
        java.util.Map<java.lang.String, java.lang.String> map = config.getProperties();
        org.junit.Assert.assertEquals(5, map.size());
        org.junit.Assert.assertEquals("11", map.get("initLimit"));
        java.lang.String[] shouldExitKeys = new java.lang.String[]{ "delete.key.1", "delete.key.2", "delete.key.3", "delete.key.4" };
        for (java.lang.String key : shouldExitKeys) {
            org.junit.Assert.assertTrue(map.containsKey(key));
        }
    }

    @org.junit.Test
    public void testInsert() throws java.lang.Exception {
        org.apache.ambari.server.state.Cluster c = clusters.getCluster("c1");
        org.junit.Assert.assertEquals(1, c.getConfigsByType("zoo.cfg").size());
        java.util.Map<java.lang.String, java.lang.String> properties = new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put("key_to_append", "append");
                put("key_to_prepend", "prepend");
            }
        };
        org.apache.ambari.server.state.Config config = createConfig(c, repoVersion2110, "zoo.cfg", "version2", properties);
        c.addDesiredConfig("user", java.util.Collections.singleton(config));
        org.junit.Assert.assertEquals(2, c.getConfigsByType("zoo.cfg").size());
        createUpgrade(c, repoVersion2111);
        java.util.Map<java.lang.String, java.lang.String> commandParams = new java.util.HashMap<>();
        commandParams.put("clusterName", "c1");
        commandParams.put(org.apache.ambari.server.stack.upgrade.ConfigureTask.PARAMETER_CONFIG_TYPE, "zoo.cfg");
        final java.lang.String prependValue = "This should be on a newline";
        final java.lang.String appendValue = " this will be after...";
        java.util.List<org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Insert> insertions = new java.util.ArrayList<>();
        org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Insert prepend = new org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Insert();
        prepend.insertType = org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.InsertType.PREPEND;
        prepend.key = "key_to_prepend";
        prepend.value = prependValue;
        prepend.newlineBefore = false;
        prepend.newlineAfter = true;
        org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Insert append = new org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Insert();
        append.insertType = org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.InsertType.APPEND;
        append.key = "key_to_append";
        append.value = appendValue;
        append.newlineBefore = false;
        append.newlineAfter = false;
        insertions.add(prepend);
        insertions.add(append);
        insertions.add(prepend);
        insertions.add(append);
        commandParams.put(org.apache.ambari.server.stack.upgrade.ConfigureTask.PARAMETER_INSERTIONS, new com.google.gson.Gson().toJson(insertions));
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
        org.junit.Assert.assertFalse("version2".equals(config.getTag()));
        java.lang.String expectedPrepend = (prependValue + java.lang.System.lineSeparator()) + "prepend";
        java.lang.String expectedAppend = "append" + appendValue;
        org.junit.Assert.assertEquals(expectedPrepend, config.getProperties().get("key_to_prepend"));
        org.junit.Assert.assertEquals(expectedAppend, config.getProperties().get("key_to_append"));
    }

    @org.junit.Test
    public void testInsertWithCondition() throws java.lang.Exception {
        org.apache.ambari.server.state.Cluster c = clusters.getCluster("c1");
        org.junit.Assert.assertEquals(1, c.getConfigsByType("zoo.cfg").size());
        final java.lang.String lineSample = "This is before";
        java.util.Map<java.lang.String, java.lang.String> properties = new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put("item_not_inserted", lineSample);
                put("item_inserted", lineSample);
                put("item_partial_inserted", lineSample);
                put("item_partial_not_inserted", lineSample);
                put("item_value_not_inserted", lineSample);
                put("item_partial_value_not_inserted", lineSample);
                put("item_value_not_not_inserted", lineSample);
                put("item_partial_value_not_not_inserted", lineSample);
            }
        };
        org.apache.ambari.server.state.Config config = createConfig(c, repoVersion2110, "zoo.cfg", "version2", properties);
        c.addDesiredConfig("user", java.util.Collections.singleton(config));
        org.junit.Assert.assertEquals(2, c.getConfigsByType("zoo.cfg").size());
        createUpgrade(c, repoVersion2111);
        java.util.Map<java.lang.String, java.lang.String> commandParams = new java.util.HashMap<>();
        commandParams.put("clusterName", "c1");
        commandParams.put(org.apache.ambari.server.stack.upgrade.ConfigureTask.PARAMETER_CONFIG_TYPE, "zoo.cfg");
        final java.lang.String appendValue = " this will be after...";
        java.util.List<org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Insert> insertions = new java.util.ArrayList<>();
        org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Insert in1 = new org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Insert();
        org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Insert in2 = new org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Insert();
        org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Insert in3 = new org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Insert();
        org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Insert in4 = new org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Insert();
        org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Insert in5 = new org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Insert();
        org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Insert in6 = new org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Insert();
        org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Insert in7 = new org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Insert();
        org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Insert in8 = new org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.Insert();
        in1.insertType = org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.InsertType.APPEND;
        in1.key = "item_not_inserted";
        in1.value = appendValue;
        in1.newlineBefore = false;
        in1.newlineAfter = false;
        in1.ifType = "zoo.cfg";
        in1.ifKey = "item_not_inserted";
        in1.ifValue = "multiline";
        in1.ifValueMatchType = org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.IfValueMatchType.EXACT;
        in2.insertType = org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.InsertType.APPEND;
        in2.key = "item_inserted";
        in2.value = appendValue;
        in2.newlineBefore = false;
        in2.newlineAfter = false;
        in2.ifType = "zoo.cfg";
        in2.ifKey = "item_inserted";
        in2.ifValue = lineSample;
        in2.ifValueMatchType = org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.IfValueMatchType.EXACT;
        in3.insertType = org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.InsertType.APPEND;
        in3.key = "item_partial_inserted";
        in3.value = appendValue;
        in3.newlineBefore = false;
        in3.newlineAfter = false;
        in3.ifType = "zoo.cfg";
        in3.ifKey = "item_partial_inserted";
        in3.ifValue = "before";
        in3.ifValueMatchType = org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.IfValueMatchType.PARTIAL;
        in4.insertType = org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.InsertType.APPEND;
        in4.key = "item_partial_not_inserted";
        in4.value = appendValue;
        in4.newlineBefore = false;
        in4.newlineAfter = false;
        in4.ifType = "zoo.cfg";
        in4.ifKey = "item_partial_not_inserted";
        in4.ifValue = "wrong word";
        in4.ifValueMatchType = org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.IfValueMatchType.PARTIAL;
        in5.insertType = org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.InsertType.APPEND;
        in5.key = "item_value_not_inserted";
        in5.value = appendValue;
        in5.newlineBefore = false;
        in5.newlineAfter = false;
        in5.ifType = "zoo.cfg";
        in5.ifKey = "item_value_not_inserted";
        in5.ifValue = "wrong word";
        in5.ifValueMatchType = org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.IfValueMatchType.EXACT;
        in5.ifValueNotMatched = true;
        in6.insertType = org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.InsertType.APPEND;
        in6.key = "item_partial_value_not_inserted";
        in6.value = appendValue;
        in6.newlineBefore = false;
        in6.newlineAfter = false;
        in6.ifType = "zoo.cfg";
        in6.ifKey = "item_partial_value_not_inserted";
        in6.ifValue = "wrong word";
        in6.ifValueMatchType = org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.IfValueMatchType.PARTIAL;
        in6.ifValueNotMatched = true;
        in7.insertType = org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.InsertType.APPEND;
        in7.key = "item_value_not_not_inserted";
        in7.value = appendValue;
        in7.newlineBefore = false;
        in7.newlineAfter = false;
        in7.ifType = "zoo.cfg";
        in7.ifKey = "item_value_not_not_inserted";
        in7.ifValue = lineSample;
        in7.ifValueMatchType = org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.IfValueMatchType.EXACT;
        in7.ifValueNotMatched = true;
        in8.insertType = org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.InsertType.APPEND;
        in8.key = "item_partial_value_not_not_inserted";
        in8.value = appendValue;
        in8.newlineBefore = false;
        in8.newlineAfter = false;
        in8.ifType = "zoo.cfg";
        in8.ifKey = "item_partial_value_not_not_inserted";
        in8.ifValue = "before";
        in8.ifValueMatchType = org.apache.ambari.server.stack.upgrade.ConfigUpgradeChangeDefinition.IfValueMatchType.PARTIAL;
        in8.ifValueNotMatched = true;
        insertions.add(in1);
        insertions.add(in2);
        insertions.add(in3);
        insertions.add(in4);
        insertions.add(in5);
        insertions.add(in6);
        insertions.add(in7);
        insertions.add(in8);
        commandParams.put(org.apache.ambari.server.stack.upgrade.ConfigureTask.PARAMETER_INSERTIONS, new com.google.gson.Gson().toJson(insertions));
        org.apache.ambari.server.agent.ExecutionCommand executionCommand = getExecutionCommand(commandParams);
        org.apache.ambari.server.actionmanager.HostRoleCommand hostRoleCommand = hostRoleCommandFactory.create(null, null, null, null);
        hostRoleCommand.setExecutionCommandWrapper(new org.apache.ambari.server.actionmanager.ExecutionCommandWrapper(executionCommand));
        action.setExecutionCommand(executionCommand);
        action.setHostRoleCommand(hostRoleCommand);
        org.apache.ambari.server.agent.CommandReport report = action.execute(null);
        org.junit.Assert.assertNotNull(report);
        config = c.getDesiredConfigByType("zoo.cfg");
        org.junit.Assert.assertNotNull(config);
        java.lang.String expectedAppend = lineSample + appendValue;
        org.junit.Assert.assertEquals(expectedAppend, config.getProperties().get("item_inserted"));
        org.junit.Assert.assertEquals(expectedAppend, config.getProperties().get("item_partial_inserted"));
        org.junit.Assert.assertEquals(expectedAppend, config.getProperties().get("item_value_not_inserted"));
        org.junit.Assert.assertEquals(expectedAppend, config.getProperties().get("item_partial_value_not_inserted"));
        org.junit.Assert.assertTrue(lineSample.equalsIgnoreCase(config.getProperties().get("item_not_inserted")));
        org.junit.Assert.assertTrue(lineSample.equalsIgnoreCase(config.getProperties().get("item_partial_not_inserted")));
        org.junit.Assert.assertTrue(lineSample.equalsIgnoreCase(config.getProperties().get("item_value_not_not_inserted")));
        org.junit.Assert.assertTrue(lineSample.equalsIgnoreCase(config.getProperties().get("item_partial_value_not_not_inserted")));
    }

    private void makeUpgradeCluster() throws java.lang.Exception {
        java.lang.String clusterName = "c1";
        java.lang.String hostName = "h1";
        java.lang.Long hostId = 1L;
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
        org.apache.ambari.server.state.Config config = createConfig(c, repoVersion2110, "zoo.cfg", "version1", properties);
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
        executionCommand.setRoleParams(new java.util.HashMap<>());
        executionCommand.getRoleParams().put(org.apache.ambari.server.serveraction.ServerAction.ACTION_USER_NAME, "username");
        return executionCommand;
    }

    private org.apache.ambari.server.state.Config createConfig(org.apache.ambari.server.state.Cluster cluster, org.apache.ambari.server.orm.entities.RepositoryVersionEntity repoVersion, java.lang.String type, java.lang.String tag, java.util.Map<java.lang.String, java.lang.String> properties) {
        return configFactory.createNew(repoVersion.getStackId(), cluster, type, tag, properties, NO_ATTRIBUTES);
    }
}