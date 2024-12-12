package org.apache.ambari.server.actionmanager;
import org.codehaus.jettison.json.JSONException;
public class ExecutionCommandWrapperTest {
    private static final java.lang.String HOST1 = "dev01.ambari.apache.org";

    private static final java.lang.String CLUSTER1 = "c1";

    private static final java.lang.String CLUSTER_VERSION_TAG = "clusterVersion";

    private static final java.lang.String SERVICE_VERSION_TAG = "serviceVersion";

    private static final java.lang.String HOST_VERSION_TAG = "hostVersion";

    private static final java.lang.String GLOBAL_CONFIG = "global";

    private static final java.lang.String SERVICE_SITE_CONFIG = "service-site";

    private static final java.lang.String SERVICE_SITE_NAME1 = "ssn1";

    private static final java.lang.String SERVICE_SITE_NAME2 = "ssn2";

    private static final java.lang.String SERVICE_SITE_NAME3 = "ssn3";

    private static final java.lang.String SERVICE_SITE_NAME4 = "ssn4";

    private static final java.lang.String SERVICE_SITE_NAME5 = "ssn5";

    private static final java.lang.String SERVICE_SITE_NAME6 = "ssn6";

    private static final java.lang.String SERVICE_SITE_VAL1 = "ssv1";

    private static final java.lang.String SERVICE_SITE_VAL1_S = "ssv1_s";

    private static final java.lang.String SERVICE_SITE_VAL2 = "ssv2";

    private static final java.lang.String SERVICE_SITE_VAL2_H = "ssv2_h";

    private static final java.lang.String SERVICE_SITE_VAL3 = "ssv3";

    private static final java.lang.String SERVICE_SITE_VAL4 = "ssv4";

    private static final java.lang.String SERVICE_SITE_VAL5 = "ssv5";

    private static final java.lang.String SERVICE_SITE_VAL5_S = "ssv5_s";

    private static final java.lang.String SERVICE_SITE_VAL6_H = "ssv6_h";

    private static final java.lang.String GLOBAL_NAME1 = "gn1";

    private static final java.lang.String GLOBAL_NAME2 = "gn2";

    private static final java.lang.String GLOBAL_CLUSTER_VAL1 = "gcv1";

    private static final java.lang.String GLOBAL_CLUSTER_VAL2 = "gcv2";

    private static final java.lang.String GLOBAL_VAL1 = "gv1";

    private static java.util.Map<java.lang.String, java.lang.String> GLOBAL_CLUSTER;

    private static java.util.Map<java.lang.String, java.lang.String> SERVICE_SITE_CLUSTER;

    private static java.util.Map<java.lang.String, java.lang.String> SERVICE_SITE_SERVICE;

    private static java.util.Map<java.lang.String, java.lang.String> SERVICE_SITE_HOST;

    private static java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> CONFIG_ATTRIBUTES;

    private static com.google.inject.Injector injector;

    private static org.apache.ambari.server.state.Clusters clusters;

    private static org.apache.ambari.server.state.ConfigFactory configFactory;

    private static org.apache.ambari.server.state.ConfigHelper configHelper;

    private static org.apache.ambari.server.actionmanager.StageFactory stageFactory;

    private static org.apache.ambari.server.orm.OrmTestHelper ormTestHelper;

    @org.junit.BeforeClass
    public static void setup() throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.actionmanager.ExecutionCommandWrapperTest.injector = com.google.inject.Guice.createInjector(new org.apache.ambari.server.orm.InMemoryDefaultTestModule());
        org.apache.ambari.server.actionmanager.ExecutionCommandWrapperTest.injector.getInstance(org.apache.ambari.server.orm.GuiceJpaInitializer.class);
        org.apache.ambari.server.actionmanager.ExecutionCommandWrapperTest.configHelper = org.apache.ambari.server.actionmanager.ExecutionCommandWrapperTest.injector.getInstance(org.apache.ambari.server.state.ConfigHelper.class);
        org.apache.ambari.server.actionmanager.ExecutionCommandWrapperTest.configFactory = org.apache.ambari.server.actionmanager.ExecutionCommandWrapperTest.injector.getInstance(org.apache.ambari.server.state.ConfigFactory.class);
        org.apache.ambari.server.actionmanager.ExecutionCommandWrapperTest.stageFactory = org.apache.ambari.server.actionmanager.ExecutionCommandWrapperTest.injector.getInstance(org.apache.ambari.server.actionmanager.StageFactory.class);
        org.apache.ambari.server.actionmanager.ExecutionCommandWrapperTest.ormTestHelper = org.apache.ambari.server.actionmanager.ExecutionCommandWrapperTest.injector.getInstance(org.apache.ambari.server.orm.OrmTestHelper.class);
        org.apache.ambari.server.actionmanager.ExecutionCommandWrapperTest.clusters = org.apache.ambari.server.actionmanager.ExecutionCommandWrapperTest.injector.getInstance(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.actionmanager.ExecutionCommandWrapperTest.clusters.addHost(org.apache.ambari.server.actionmanager.ExecutionCommandWrapperTest.HOST1);
        org.apache.ambari.server.actionmanager.ExecutionCommandWrapperTest.clusters.addCluster(org.apache.ambari.server.actionmanager.ExecutionCommandWrapperTest.CLUSTER1, new org.apache.ambari.server.state.StackId("HDP-0.1"));
        org.apache.ambari.server.actionmanager.ExecutionCommandWrapperTest.clusters.mapHostToCluster(org.apache.ambari.server.actionmanager.ExecutionCommandWrapperTest.HOST1, org.apache.ambari.server.actionmanager.ExecutionCommandWrapperTest.CLUSTER1);
        java.util.Map<java.lang.String, java.lang.String> hostAttributes = new java.util.HashMap<>();
        hostAttributes.put("os_family", "redhat");
        hostAttributes.put("os_release_version", "6.4");
        org.apache.ambari.server.state.Host host = org.apache.ambari.server.actionmanager.ExecutionCommandWrapperTest.clusters.getHost(org.apache.ambari.server.actionmanager.ExecutionCommandWrapperTest.HOST1);
        host.setHostAttributes(hostAttributes);
        org.apache.ambari.server.state.Cluster cluster1 = org.apache.ambari.server.actionmanager.ExecutionCommandWrapperTest.clusters.getCluster(org.apache.ambari.server.actionmanager.ExecutionCommandWrapperTest.CLUSTER1);
        org.apache.ambari.server.orm.OrmTestHelper helper = org.apache.ambari.server.actionmanager.ExecutionCommandWrapperTest.injector.getInstance(org.apache.ambari.server.orm.OrmTestHelper.class);
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion = helper.getOrCreateRepositoryVersion(cluster1);
        cluster1.addService("HDFS", repositoryVersion);
        org.apache.ambari.server.actionmanager.ExecutionCommandWrapperTest.SERVICE_SITE_CLUSTER = new java.util.HashMap<>();
        org.apache.ambari.server.actionmanager.ExecutionCommandWrapperTest.SERVICE_SITE_CLUSTER.put(org.apache.ambari.server.actionmanager.ExecutionCommandWrapperTest.SERVICE_SITE_NAME1, org.apache.ambari.server.actionmanager.ExecutionCommandWrapperTest.SERVICE_SITE_VAL1);
        org.apache.ambari.server.actionmanager.ExecutionCommandWrapperTest.SERVICE_SITE_CLUSTER.put(org.apache.ambari.server.actionmanager.ExecutionCommandWrapperTest.SERVICE_SITE_NAME2, org.apache.ambari.server.actionmanager.ExecutionCommandWrapperTest.SERVICE_SITE_VAL2);
        org.apache.ambari.server.actionmanager.ExecutionCommandWrapperTest.SERVICE_SITE_CLUSTER.put(org.apache.ambari.server.actionmanager.ExecutionCommandWrapperTest.SERVICE_SITE_NAME3, org.apache.ambari.server.actionmanager.ExecutionCommandWrapperTest.SERVICE_SITE_VAL3);
        org.apache.ambari.server.actionmanager.ExecutionCommandWrapperTest.SERVICE_SITE_CLUSTER.put(org.apache.ambari.server.actionmanager.ExecutionCommandWrapperTest.SERVICE_SITE_NAME4, org.apache.ambari.server.actionmanager.ExecutionCommandWrapperTest.SERVICE_SITE_VAL4);
        org.apache.ambari.server.actionmanager.ExecutionCommandWrapperTest.SERVICE_SITE_SERVICE = new java.util.HashMap<>();
        org.apache.ambari.server.actionmanager.ExecutionCommandWrapperTest.SERVICE_SITE_SERVICE.put(org.apache.ambari.server.actionmanager.ExecutionCommandWrapperTest.SERVICE_SITE_NAME1, org.apache.ambari.server.actionmanager.ExecutionCommandWrapperTest.SERVICE_SITE_VAL1_S);
        org.apache.ambari.server.actionmanager.ExecutionCommandWrapperTest.SERVICE_SITE_SERVICE.put(org.apache.ambari.server.actionmanager.ExecutionCommandWrapperTest.SERVICE_SITE_NAME5, org.apache.ambari.server.actionmanager.ExecutionCommandWrapperTest.SERVICE_SITE_VAL5_S);
        org.apache.ambari.server.actionmanager.ExecutionCommandWrapperTest.SERVICE_SITE_HOST = new java.util.HashMap<>();
        org.apache.ambari.server.actionmanager.ExecutionCommandWrapperTest.SERVICE_SITE_HOST.put(org.apache.ambari.server.actionmanager.ExecutionCommandWrapperTest.SERVICE_SITE_NAME2, org.apache.ambari.server.actionmanager.ExecutionCommandWrapperTest.SERVICE_SITE_VAL2_H);
        org.apache.ambari.server.actionmanager.ExecutionCommandWrapperTest.SERVICE_SITE_HOST.put(org.apache.ambari.server.actionmanager.ExecutionCommandWrapperTest.SERVICE_SITE_NAME6, org.apache.ambari.server.actionmanager.ExecutionCommandWrapperTest.SERVICE_SITE_VAL6_H);
        org.apache.ambari.server.actionmanager.ExecutionCommandWrapperTest.GLOBAL_CLUSTER = new java.util.HashMap<>();
        org.apache.ambari.server.actionmanager.ExecutionCommandWrapperTest.GLOBAL_CLUSTER.put(org.apache.ambari.server.actionmanager.ExecutionCommandWrapperTest.GLOBAL_NAME1, org.apache.ambari.server.actionmanager.ExecutionCommandWrapperTest.GLOBAL_CLUSTER_VAL1);
        org.apache.ambari.server.actionmanager.ExecutionCommandWrapperTest.GLOBAL_CLUSTER.put(org.apache.ambari.server.actionmanager.ExecutionCommandWrapperTest.GLOBAL_NAME2, org.apache.ambari.server.actionmanager.ExecutionCommandWrapperTest.GLOBAL_CLUSTER_VAL2);
        org.apache.ambari.server.actionmanager.ExecutionCommandWrapperTest.CONFIG_ATTRIBUTES = new java.util.HashMap<>();
        org.apache.ambari.server.actionmanager.ExecutionCommandWrapperTest.configFactory.createNew(cluster1, org.apache.ambari.server.actionmanager.ExecutionCommandWrapperTest.GLOBAL_CONFIG, org.apache.ambari.server.actionmanager.ExecutionCommandWrapperTest.CLUSTER_VERSION_TAG, org.apache.ambari.server.actionmanager.ExecutionCommandWrapperTest.GLOBAL_CLUSTER, org.apache.ambari.server.actionmanager.ExecutionCommandWrapperTest.CONFIG_ATTRIBUTES);
        org.apache.ambari.server.actionmanager.ExecutionCommandWrapperTest.configFactory.createNew(cluster1, org.apache.ambari.server.actionmanager.ExecutionCommandWrapperTest.SERVICE_SITE_CONFIG, org.apache.ambari.server.actionmanager.ExecutionCommandWrapperTest.CLUSTER_VERSION_TAG, org.apache.ambari.server.actionmanager.ExecutionCommandWrapperTest.SERVICE_SITE_CLUSTER, org.apache.ambari.server.actionmanager.ExecutionCommandWrapperTest.CONFIG_ATTRIBUTES);
        org.apache.ambari.server.actionmanager.ExecutionCommandWrapperTest.configFactory.createNew(cluster1, org.apache.ambari.server.actionmanager.ExecutionCommandWrapperTest.SERVICE_SITE_CONFIG, org.apache.ambari.server.actionmanager.ExecutionCommandWrapperTest.SERVICE_VERSION_TAG, org.apache.ambari.server.actionmanager.ExecutionCommandWrapperTest.SERVICE_SITE_SERVICE, org.apache.ambari.server.actionmanager.ExecutionCommandWrapperTest.CONFIG_ATTRIBUTES);
        org.apache.ambari.server.actionmanager.ExecutionCommandWrapperTest.configFactory.createNew(cluster1, org.apache.ambari.server.actionmanager.ExecutionCommandWrapperTest.SERVICE_SITE_CONFIG, org.apache.ambari.server.actionmanager.ExecutionCommandWrapperTest.HOST_VERSION_TAG, org.apache.ambari.server.actionmanager.ExecutionCommandWrapperTest.SERVICE_SITE_HOST, org.apache.ambari.server.actionmanager.ExecutionCommandWrapperTest.CONFIG_ATTRIBUTES);
        org.apache.ambari.server.actionmanager.ActionDBAccessor db = org.apache.ambari.server.actionmanager.ExecutionCommandWrapperTest.injector.getInstance(org.apache.ambari.server.actionmanager.ActionDBAccessorImpl.class);
        org.apache.ambari.server.actionmanager.ExecutionCommandWrapperTest.createTask(db, 1, 1, org.apache.ambari.server.actionmanager.ExecutionCommandWrapperTest.HOST1, org.apache.ambari.server.actionmanager.ExecutionCommandWrapperTest.CLUSTER1);
    }

    private static void createTask(org.apache.ambari.server.actionmanager.ActionDBAccessor db, long requestId, long stageId, java.lang.String hostName, java.lang.String clusterName) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.actionmanager.Stage s = org.apache.ambari.server.actionmanager.ExecutionCommandWrapperTest.stageFactory.createNew(requestId, "/var/log", clusterName, 1L, "execution command wrapper test", "commandParamsStage", "hostParamsStage");
        s.setStageId(stageId);
        s.addHostRoleExecutionCommand(hostName, org.apache.ambari.server.Role.NAMENODE, org.apache.ambari.server.RoleCommand.START, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostStartEvent(org.apache.ambari.server.Role.NAMENODE.toString(), hostName, java.lang.System.currentTimeMillis()), clusterName, "HDFS", false, false);
        java.util.List<org.apache.ambari.server.actionmanager.Stage> stages = new java.util.ArrayList<>();
        stages.add(s);
        org.apache.ambari.server.actionmanager.Request request = new org.apache.ambari.server.actionmanager.Request(stages, "clusterHostInfo", org.apache.ambari.server.actionmanager.ExecutionCommandWrapperTest.clusters);
        db.persistActions(request);
    }

    @org.junit.Test
    public void testGetExecutionCommand() throws org.codehaus.jettison.json.JSONException, org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.agent.ExecutionCommand executionCommand = new org.apache.ambari.server.agent.ExecutionCommand();
        executionCommand.setClusterName(org.apache.ambari.server.actionmanager.ExecutionCommandWrapperTest.CLUSTER1);
        executionCommand.setTaskId(1);
        executionCommand.setRequestAndStage(1, 1);
        executionCommand.setHostname(org.apache.ambari.server.actionmanager.ExecutionCommandWrapperTest.HOST1);
        executionCommand.setRole("NAMENODE");
        executionCommand.setRoleParams(java.util.Collections.emptyMap());
        executionCommand.setRoleCommand(org.apache.ambari.server.RoleCommand.START);
        executionCommand.setServiceName("HDFS");
        executionCommand.setCommandType(org.apache.ambari.server.agent.AgentCommand.AgentCommandType.EXECUTION_COMMAND);
        executionCommand.setCommandParams(java.util.Collections.emptyMap());
        java.lang.String json = org.apache.ambari.server.utils.StageUtils.getGson().toJson(executionCommand, org.apache.ambari.server.agent.ExecutionCommand.class);
        org.apache.ambari.server.actionmanager.ExecutionCommandWrapper execCommWrap = new org.apache.ambari.server.actionmanager.ExecutionCommandWrapper(json);
        org.apache.ambari.server.actionmanager.ExecutionCommandWrapperTest.injector.injectMembers(execCommWrap);
        org.apache.ambari.server.agent.ExecutionCommand processedExecutionCommand = execCommWrap.getExecutionCommand();
        org.junit.Assert.assertNotNull(processedExecutionCommand.getRepositoryFile());
    }

    @org.junit.Test
    public void testGetMergedConfig() {
        java.util.Map<java.lang.String, java.lang.String> baseConfig = new java.util.HashMap<>();
        baseConfig.put(org.apache.ambari.server.actionmanager.ExecutionCommandWrapperTest.SERVICE_SITE_NAME1, org.apache.ambari.server.actionmanager.ExecutionCommandWrapperTest.SERVICE_SITE_VAL1);
        baseConfig.put(org.apache.ambari.server.actionmanager.ExecutionCommandWrapperTest.SERVICE_SITE_NAME2, org.apache.ambari.server.actionmanager.ExecutionCommandWrapperTest.SERVICE_SITE_VAL2);
        baseConfig.put(org.apache.ambari.server.actionmanager.ExecutionCommandWrapperTest.SERVICE_SITE_NAME3, org.apache.ambari.server.actionmanager.ExecutionCommandWrapperTest.SERVICE_SITE_VAL3);
        baseConfig.put(org.apache.ambari.server.actionmanager.ExecutionCommandWrapperTest.SERVICE_SITE_NAME4, org.apache.ambari.server.actionmanager.ExecutionCommandWrapperTest.SERVICE_SITE_VAL4);
        baseConfig.put(org.apache.ambari.server.actionmanager.ExecutionCommandWrapperTest.SERVICE_SITE_NAME5, org.apache.ambari.server.actionmanager.ExecutionCommandWrapperTest.SERVICE_SITE_VAL5);
        java.util.Map<java.lang.String, java.lang.String> overrideConfig = new java.util.HashMap<>();
        overrideConfig.put(org.apache.ambari.server.actionmanager.ExecutionCommandWrapperTest.SERVICE_SITE_NAME2, org.apache.ambari.server.actionmanager.ExecutionCommandWrapperTest.SERVICE_SITE_VAL2_H);
        overrideConfig.put(org.apache.ambari.server.actionmanager.ExecutionCommandWrapperTest.SERVICE_SITE_NAME6, org.apache.ambari.server.actionmanager.ExecutionCommandWrapperTest.SERVICE_SITE_VAL6_H);
        java.util.Map<java.lang.String, java.lang.String> mergedConfig = org.apache.ambari.server.actionmanager.ExecutionCommandWrapperTest.configHelper.getMergedConfig(baseConfig, overrideConfig);
        java.util.Set<java.lang.String> configsKeys = new java.util.HashSet<>();
        configsKeys.addAll(baseConfig.keySet());
        configsKeys.addAll(overrideConfig.keySet());
        org.junit.Assert.assertEquals(configsKeys.size(), mergedConfig.size());
        org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.ExecutionCommandWrapperTest.SERVICE_SITE_VAL1, mergedConfig.get(org.apache.ambari.server.actionmanager.ExecutionCommandWrapperTest.SERVICE_SITE_NAME1));
        org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.ExecutionCommandWrapperTest.SERVICE_SITE_VAL2_H, mergedConfig.get(org.apache.ambari.server.actionmanager.ExecutionCommandWrapperTest.SERVICE_SITE_NAME2));
        org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.ExecutionCommandWrapperTest.SERVICE_SITE_VAL3, mergedConfig.get(org.apache.ambari.server.actionmanager.ExecutionCommandWrapperTest.SERVICE_SITE_NAME3));
        org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.ExecutionCommandWrapperTest.SERVICE_SITE_VAL4, mergedConfig.get(org.apache.ambari.server.actionmanager.ExecutionCommandWrapperTest.SERVICE_SITE_NAME4));
        org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.ExecutionCommandWrapperTest.SERVICE_SITE_VAL5, mergedConfig.get(org.apache.ambari.server.actionmanager.ExecutionCommandWrapperTest.SERVICE_SITE_NAME5));
        org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.ExecutionCommandWrapperTest.SERVICE_SITE_VAL6_H, mergedConfig.get(org.apache.ambari.server.actionmanager.ExecutionCommandWrapperTest.SERVICE_SITE_NAME6));
    }

    @org.junit.Test
    public void testExecutionCommandHasVersionInfoWithoutCurrentClusterVersion() throws org.codehaus.jettison.json.JSONException, org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.Cluster cluster = org.apache.ambari.server.actionmanager.ExecutionCommandWrapperTest.clusters.getCluster(org.apache.ambari.server.actionmanager.ExecutionCommandWrapperTest.CLUSTER1);
        org.apache.ambari.server.state.StackId stackId = cluster.getDesiredStackVersion();
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion = org.apache.ambari.server.actionmanager.ExecutionCommandWrapperTest.ormTestHelper.getOrCreateRepositoryVersion(stackId, "0.1-0000");
        repositoryVersion.setResolved(false);
        org.apache.ambari.server.actionmanager.ExecutionCommandWrapperTest.ormTestHelper.repositoryVersionDAO.merge(repositoryVersion);
        org.apache.ambari.server.state.Service service = cluster.getService("HDFS");
        service.setDesiredRepositoryVersion(repositoryVersion);
        org.apache.ambari.server.agent.ExecutionCommand executionCommand = new org.apache.ambari.server.agent.ExecutionCommand();
        java.util.Map<java.lang.String, java.lang.String> commandParams = new java.util.HashMap<>();
        executionCommand.setClusterName(org.apache.ambari.server.actionmanager.ExecutionCommandWrapperTest.CLUSTER1);
        executionCommand.setTaskId(1);
        executionCommand.setRequestAndStage(1, 1);
        executionCommand.setHostname(org.apache.ambari.server.actionmanager.ExecutionCommandWrapperTest.HOST1);
        executionCommand.setRole("NAMENODE");
        executionCommand.setRoleParams(java.util.Collections.<java.lang.String, java.lang.String>emptyMap());
        executionCommand.setRoleCommand(org.apache.ambari.server.RoleCommand.INSTALL);
        executionCommand.setServiceName("HDFS");
        executionCommand.setCommandType(org.apache.ambari.server.agent.AgentCommand.AgentCommandType.EXECUTION_COMMAND);
        executionCommand.setCommandParams(commandParams);
        java.lang.String json = org.apache.ambari.server.utils.StageUtils.getGson().toJson(executionCommand, org.apache.ambari.server.agent.ExecutionCommand.class);
        org.apache.ambari.server.actionmanager.ExecutionCommandWrapper execCommWrap = new org.apache.ambari.server.actionmanager.ExecutionCommandWrapper(json);
        org.apache.ambari.server.actionmanager.ExecutionCommandWrapperTest.injector.injectMembers(execCommWrap);
        org.apache.ambari.server.agent.ExecutionCommand processedExecutionCommand = execCommWrap.getExecutionCommand();
        commandParams = processedExecutionCommand.getCommandParams();
        org.junit.Assert.assertFalse(commandParams.containsKey(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.VERSION));
        executionCommand = new org.apache.ambari.server.agent.ExecutionCommand();
        commandParams = new java.util.HashMap<>();
        executionCommand.setClusterName(org.apache.ambari.server.actionmanager.ExecutionCommandWrapperTest.CLUSTER1);
        executionCommand.setTaskId(1);
        executionCommand.setRequestAndStage(1, 1);
        executionCommand.setHostname(org.apache.ambari.server.actionmanager.ExecutionCommandWrapperTest.HOST1);
        executionCommand.setRole("NAMENODE");
        executionCommand.setRoleParams(java.util.Collections.<java.lang.String, java.lang.String>emptyMap());
        executionCommand.setRoleCommand(org.apache.ambari.server.RoleCommand.START);
        executionCommand.setServiceName("HDFS");
        executionCommand.setCommandType(org.apache.ambari.server.agent.AgentCommand.AgentCommandType.EXECUTION_COMMAND);
        executionCommand.setCommandParams(commandParams);
        json = org.apache.ambari.server.utils.StageUtils.getGson().toJson(executionCommand, org.apache.ambari.server.agent.ExecutionCommand.class);
        execCommWrap = new org.apache.ambari.server.actionmanager.ExecutionCommandWrapper(json);
        org.apache.ambari.server.actionmanager.ExecutionCommandWrapperTest.injector.injectMembers(execCommWrap);
        processedExecutionCommand = execCommWrap.getExecutionCommand();
        commandParams = processedExecutionCommand.getCommandParams();
        org.junit.Assert.assertFalse(commandParams.containsKey(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.VERSION));
        repositoryVersion.setResolved(true);
        org.apache.ambari.server.actionmanager.ExecutionCommandWrapperTest.ormTestHelper.repositoryVersionDAO.merge(repositoryVersion);
        execCommWrap = new org.apache.ambari.server.actionmanager.ExecutionCommandWrapper(json);
        org.apache.ambari.server.actionmanager.ExecutionCommandWrapperTest.injector.injectMembers(execCommWrap);
        processedExecutionCommand = execCommWrap.getExecutionCommand();
        commandParams = processedExecutionCommand.getCommandParams();
        org.junit.Assert.assertEquals("0.1-0000", commandParams.get(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.VERSION));
    }

    @org.junit.Test
    public void testExecutionCommandNoRepositoryFile() throws java.lang.Exception {
        org.apache.ambari.server.state.Cluster cluster = org.apache.ambari.server.actionmanager.ExecutionCommandWrapperTest.clusters.getCluster(org.apache.ambari.server.actionmanager.ExecutionCommandWrapperTest.CLUSTER1);
        org.apache.ambari.server.state.StackId stackId = cluster.getDesiredStackVersion();
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion = org.apache.ambari.server.actionmanager.ExecutionCommandWrapperTest.ormTestHelper.getOrCreateRepositoryVersion(new org.apache.ambari.server.state.StackId("HDP", "0.2"), "0.2-0000");
        repositoryVersion.setResolved(true);
        org.apache.ambari.server.state.Service service = cluster.addService("HIVE", repositoryVersion);
        service.setDesiredRepositoryVersion(repositoryVersion);
        repositoryVersion.addRepoOsEntities(new java.util.ArrayList<>());
        org.apache.ambari.server.actionmanager.ExecutionCommandWrapperTest.ormTestHelper.repositoryVersionDAO.merge(repositoryVersion);
        org.apache.ambari.server.agent.ExecutionCommand executionCommand = new org.apache.ambari.server.agent.ExecutionCommand();
        java.util.Map<java.lang.String, java.lang.String> commandParams = new java.util.HashMap<>();
        executionCommand.setClusterName(org.apache.ambari.server.actionmanager.ExecutionCommandWrapperTest.CLUSTER1);
        executionCommand.setTaskId(1);
        executionCommand.setRequestAndStage(1, 1);
        executionCommand.setHostname(org.apache.ambari.server.actionmanager.ExecutionCommandWrapperTest.HOST1);
        executionCommand.setRole("HIVE_SERVER");
        executionCommand.setRoleParams(java.util.Collections.<java.lang.String, java.lang.String>emptyMap());
        executionCommand.setRoleCommand(org.apache.ambari.server.RoleCommand.INSTALL);
        executionCommand.setServiceName("HIVE");
        executionCommand.setCommandType(org.apache.ambari.server.agent.AgentCommand.AgentCommandType.EXECUTION_COMMAND);
        executionCommand.setCommandParams(commandParams);
        java.lang.String json = org.apache.ambari.server.utils.StageUtils.getGson().toJson(executionCommand, org.apache.ambari.server.agent.ExecutionCommand.class);
        org.apache.ambari.server.actionmanager.ExecutionCommandWrapper execCommWrap = new org.apache.ambari.server.actionmanager.ExecutionCommandWrapper(json);
        org.apache.ambari.server.actionmanager.ExecutionCommandWrapperTest.injector.injectMembers(execCommWrap);
        org.apache.ambari.server.agent.ExecutionCommand processedExecutionCommand = execCommWrap.getExecutionCommand();
        commandParams = processedExecutionCommand.getCommandParams();
        org.junit.Assert.assertFalse(commandParams.containsKey(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.VERSION));
        executionCommand = new org.apache.ambari.server.agent.ExecutionCommand();
        commandParams = new java.util.HashMap<>();
        executionCommand.setClusterName(org.apache.ambari.server.actionmanager.ExecutionCommandWrapperTest.CLUSTER1);
        executionCommand.setTaskId(1);
        executionCommand.setRequestAndStage(1, 1);
        executionCommand.setHostname(org.apache.ambari.server.actionmanager.ExecutionCommandWrapperTest.HOST1);
        executionCommand.setRole("HIVE_SERVER");
        executionCommand.setRoleParams(java.util.Collections.<java.lang.String, java.lang.String>emptyMap());
        executionCommand.setRoleCommand(org.apache.ambari.server.RoleCommand.START);
        executionCommand.setServiceName("HIVE");
        executionCommand.setCommandType(org.apache.ambari.server.agent.AgentCommand.AgentCommandType.EXECUTION_COMMAND);
        executionCommand.setCommandParams(commandParams);
        json = org.apache.ambari.server.utils.StageUtils.getGson().toJson(executionCommand, org.apache.ambari.server.agent.ExecutionCommand.class);
        execCommWrap = new org.apache.ambari.server.actionmanager.ExecutionCommandWrapper(json);
        org.apache.ambari.server.actionmanager.ExecutionCommandWrapperTest.injector.injectMembers(execCommWrap);
        processedExecutionCommand = execCommWrap.getExecutionCommand();
        commandParams = processedExecutionCommand.getCommandParams();
        org.junit.Assert.assertEquals("0.2-0000", commandParams.get(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.VERSION));
    }

    @org.junit.AfterClass
    public static void tearDown() throws org.apache.ambari.server.AmbariException, java.sql.SQLException {
        org.apache.ambari.server.H2DatabaseCleaner.clearDatabaseAndStopPersistenceService(org.apache.ambari.server.actionmanager.ExecutionCommandWrapperTest.injector);
    }
}