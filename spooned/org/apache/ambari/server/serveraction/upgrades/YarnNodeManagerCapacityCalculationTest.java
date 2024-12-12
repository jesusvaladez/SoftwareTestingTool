package org.apache.ambari.server.serveraction.upgrades;
import org.easymock.EasyMock;
import org.mockito.Mockito;
import org.powermock.modules.junit4.PowerMockRunner;
import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
@org.junit.runner.RunWith(org.powermock.modules.junit4.PowerMockRunner.class)
public class YarnNodeManagerCapacityCalculationTest {
    private static final java.lang.String YARN_SITE_CONFIG_TYPE = "yarn-site";

    private static final java.lang.String YARN_ENV_CONFIG_TYPE = "yarn-env";

    private static final java.lang.String YARN_HBASE_ENV_CONFIG_TYPE = "yarn-hbase-env";

    private static final java.lang.String CAPACITY_SCHEDULER_CONFIG_TYPE = "capacity-scheduler";

    private static final java.lang.String YARN_SYSTEM_SERVICE_USER_NAME = "yarn_ats_user";

    private static final java.lang.String YARN_DEFAULT_QUEUE = "default";

    private static final java.lang.String YARN_SYSTEM_SERVICE_QUEUE_NAME = "yarn-system";

    private static final java.lang.String CAPACITY_SCHEDULER_ROOT_QUEUES = "yarn.scheduler.capacity.root.queues";

    private static final java.lang.String YARN_SYSTEM_SERVICE_QUEUE_PREFIX = "yarn.scheduler.capacity.root." + org.apache.ambari.server.serveraction.upgrades.YarnNodeManagerCapacityCalculationTest.YARN_SYSTEM_SERVICE_QUEUE_NAME;

    private static final java.lang.String YARN_NM_PMEM_MB_PROPERTY_NAME = "yarn.nodemanager.resource.memory-mb";

    private static final java.lang.String YARN_HBASE_SYSTEM_SERVICE_QUEUE_PROPERTY_NAME = "yarn_hbase_system_service_queue_name";

    private static final java.lang.String YARN_HBASE_SYSTEM_SERVICE_LAUNCH_PROPERTY_NAME = "is_hbase_system_service_launch";

    private static final java.lang.String CLUSTER_NAME = "C1";

    private static final java.lang.String ats_user = "test1";

    private com.google.inject.Injector m_injector;

    private org.apache.ambari.server.state.Clusters m_clusters;

    private org.apache.ambari.server.agent.stomp.AgentConfigsHolder agentConfigsHolder;

    private org.apache.ambari.server.state.Cluster cluster;

    private java.lang.reflect.Field clusterField;

    private java.lang.reflect.Field agentConfigsHolderField;

    private org.apache.ambari.server.state.ServiceComponent serviceComponent;

    private org.apache.ambari.server.state.Service service;

    private java.util.Set<java.lang.String> hosts;

    @org.junit.Before
    public void setup() throws java.lang.Exception {
        m_injector = org.easymock.EasyMock.createMock(com.google.inject.Injector.class);
        m_clusters = org.easymock.EasyMock.createMock(org.apache.ambari.server.state.Clusters.class);
        agentConfigsHolder = EasyMock.createMock(org.apache.ambari.server.agent.stomp.AgentConfigsHolder.class);
        cluster = org.easymock.EasyMock.createMock(org.apache.ambari.server.state.Cluster.class);
        java.util.Map<java.lang.String, java.lang.String> mockYarnProperties = new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put(org.apache.ambari.server.serveraction.upgrades.YarnNodeManagerCapacityCalculationTest.YARN_NM_PMEM_MB_PROPERTY_NAME, "20480");
            }
        };
        org.apache.ambari.server.state.Config yarnConfig = org.easymock.EasyMock.createNiceMock(org.apache.ambari.server.state.Config.class);
        EasyMock.expect(yarnConfig.getType()).andReturn(org.apache.ambari.server.serveraction.upgrades.YarnNodeManagerCapacityCalculationTest.YARN_SITE_CONFIG_TYPE).anyTimes();
        EasyMock.expect(yarnConfig.getProperties()).andReturn(mockYarnProperties).anyTimes();
        EasyMock.expect(cluster.getDesiredConfigByType(org.apache.ambari.server.serveraction.upgrades.YarnNodeManagerCapacityCalculationTest.YARN_SITE_CONFIG_TYPE)).andReturn(yarnConfig).anyTimes();
        java.util.Map<java.lang.String, java.lang.String> mockHbaseEnvProps = new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put(org.apache.ambari.server.serveraction.upgrades.YarnNodeManagerCapacityCalculationTest.YARN_HBASE_SYSTEM_SERVICE_LAUNCH_PROPERTY_NAME, "false");
                put(org.apache.ambari.server.serveraction.upgrades.YarnNodeManagerCapacityCalculationTest.YARN_HBASE_SYSTEM_SERVICE_QUEUE_PROPERTY_NAME, org.apache.ambari.server.serveraction.upgrades.YarnNodeManagerCapacityCalculationTest.YARN_DEFAULT_QUEUE);
            }
        };
        org.apache.ambari.server.state.Config hbaseEnvConfig = org.easymock.EasyMock.createNiceMock(org.apache.ambari.server.state.Config.class);
        EasyMock.expect(hbaseEnvConfig.getType()).andReturn(org.apache.ambari.server.serveraction.upgrades.YarnNodeManagerCapacityCalculationTest.YARN_HBASE_ENV_CONFIG_TYPE).anyTimes();
        EasyMock.expect(hbaseEnvConfig.getProperties()).andReturn(mockHbaseEnvProps).anyTimes();
        EasyMock.expect(cluster.getDesiredConfigByType(org.apache.ambari.server.serveraction.upgrades.YarnNodeManagerCapacityCalculationTest.YARN_HBASE_ENV_CONFIG_TYPE)).andReturn(hbaseEnvConfig).anyTimes();
        java.util.Map<java.lang.String, java.lang.String> mockYarnEnvProps = new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put(org.apache.ambari.server.serveraction.upgrades.YarnNodeManagerCapacityCalculationTest.YARN_SYSTEM_SERVICE_USER_NAME, org.apache.ambari.server.serveraction.upgrades.YarnNodeManagerCapacityCalculationTest.ats_user);
            }
        };
        org.apache.ambari.server.state.Config yarnEnvConfig = org.easymock.EasyMock.createNiceMock(org.apache.ambari.server.state.Config.class);
        EasyMock.expect(yarnEnvConfig.getType()).andReturn(org.apache.ambari.server.serveraction.upgrades.YarnNodeManagerCapacityCalculationTest.YARN_ENV_CONFIG_TYPE).anyTimes();
        EasyMock.expect(yarnEnvConfig.getProperties()).andReturn(mockYarnEnvProps).anyTimes();
        EasyMock.expect(cluster.getDesiredConfigByType(org.apache.ambari.server.serveraction.upgrades.YarnNodeManagerCapacityCalculationTest.YARN_ENV_CONFIG_TYPE)).andReturn(yarnEnvConfig).anyTimes();
        java.util.Map<java.lang.String, java.lang.String> mockCsProps = new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put(org.apache.ambari.server.serveraction.upgrades.YarnNodeManagerCapacityCalculationTest.CAPACITY_SCHEDULER_ROOT_QUEUES, org.apache.ambari.server.serveraction.upgrades.YarnNodeManagerCapacityCalculationTest.YARN_DEFAULT_QUEUE);
            }
        };
        org.apache.ambari.server.state.Config yarnCsConfig = org.easymock.EasyMock.createNiceMock(org.apache.ambari.server.state.Config.class);
        EasyMock.expect(yarnCsConfig.getType()).andReturn(org.apache.ambari.server.serveraction.upgrades.YarnNodeManagerCapacityCalculationTest.CAPACITY_SCHEDULER_CONFIG_TYPE).anyTimes();
        EasyMock.expect(yarnCsConfig.getProperties()).andReturn(mockCsProps).anyTimes();
        EasyMock.expect(cluster.getDesiredConfigByType(org.apache.ambari.server.serveraction.upgrades.YarnNodeManagerCapacityCalculationTest.CAPACITY_SCHEDULER_CONFIG_TYPE)).andReturn(yarnCsConfig).anyTimes();
        EasyMock.expect(m_clusters.getCluster(((java.lang.String) (EasyMock.anyObject())))).andReturn(cluster).anyTimes();
        EasyMock.expect(m_injector.getInstance(org.apache.ambari.server.state.Clusters.class)).andReturn(m_clusters).atLeastOnce();
        EasyMock.expect(cluster.getClusterId()).andReturn(1L).atLeastOnce();
        EasyMock.expect(cluster.getHosts()).andReturn(java.util.Collections.emptyList()).atLeastOnce();
        hosts = org.mockito.Mockito.mock(java.util.Set.class);
        service = org.mockito.Mockito.mock(org.apache.ambari.server.state.Service.class);
        serviceComponent = org.mockito.Mockito.mock(org.apache.ambari.server.state.ServiceComponent.class);
        EasyMock.expect(cluster.getService("YARN")).andReturn(service);
        org.mockito.Mockito.when(service.getServiceComponent("NODEMANAGER")).thenReturn(serviceComponent);
        agentConfigsHolder.updateData(EasyMock.eq(1L), EasyMock.eq(java.util.Collections.emptyList()));
        EasyMock.expectLastCall().atLeastOnce();
        EasyMock.replay(m_injector, m_clusters, yarnConfig, hbaseEnvConfig, yarnEnvConfig, yarnCsConfig, agentConfigsHolder);
        clusterField = org.apache.ambari.server.serveraction.upgrades.AbstractUpgradeServerAction.class.getDeclaredField("m_clusters");
        clusterField.setAccessible(true);
        agentConfigsHolderField = org.apache.ambari.server.serveraction.upgrades.AbstractUpgradeServerAction.class.getDeclaredField("agentConfigsHolder");
        agentConfigsHolderField.setAccessible(true);
    }

    @org.junit.Test
    public void testActionForNotCreatingYarnSystemServiceQueueWhenClusterIsSmall() throws java.lang.Exception {
        org.mockito.Mockito.when(serviceComponent.getServiceComponentsHosts()).thenReturn(hosts);
        org.mockito.Mockito.when(hosts.size()).thenReturn(2);
        org.apache.ambari.server.serveraction.upgrades.YarnNodeManagerCapacityCalculation action = getYarnNodeManagerCapacityCalculation();
        org.apache.ambari.server.state.Cluster c = m_clusters.getCluster(org.apache.ambari.server.serveraction.upgrades.YarnNodeManagerCapacityCalculationTest.CLUSTER_NAME);
        org.apache.ambari.server.state.Config hbaseEnvConfig = c.getDesiredConfigByType(org.apache.ambari.server.serveraction.upgrades.YarnNodeManagerCapacityCalculationTest.YARN_HBASE_ENV_CONFIG_TYPE);
        validateYarnHBaseEnvProperties(hbaseEnvConfig, false, org.apache.ambari.server.serveraction.upgrades.YarnNodeManagerCapacityCalculationTest.YARN_DEFAULT_QUEUE);
        org.apache.ambari.server.agent.CommandReport report = action.execute(null);
        org.junit.Assert.assertNotNull(report);
        validateYarnHBaseEnvProperties(hbaseEnvConfig, false, org.apache.ambari.server.serveraction.upgrades.YarnNodeManagerCapacityCalculationTest.YARN_DEFAULT_QUEUE);
    }

    @org.junit.Test
    public void testActionForCreatingYarnSystemServiceQueue() throws java.lang.Exception {
        org.mockito.Mockito.when(serviceComponent.getServiceComponentsHosts()).thenReturn(hosts);
        org.mockito.Mockito.when(hosts.size()).thenReturn(3);
        org.apache.ambari.server.serveraction.upgrades.YarnNodeManagerCapacityCalculation action = getYarnNodeManagerCapacityCalculation();
        org.apache.ambari.server.state.Cluster c = m_clusters.getCluster(org.apache.ambari.server.serveraction.upgrades.YarnNodeManagerCapacityCalculationTest.CLUSTER_NAME);
        org.apache.ambari.server.state.Config hbaseEnvConfig = c.getDesiredConfigByType(org.apache.ambari.server.serveraction.upgrades.YarnNodeManagerCapacityCalculationTest.YARN_HBASE_ENV_CONFIG_TYPE);
        validateYarnHBaseEnvProperties(hbaseEnvConfig, false, org.apache.ambari.server.serveraction.upgrades.YarnNodeManagerCapacityCalculationTest.YARN_DEFAULT_QUEUE);
        org.apache.ambari.server.state.Config csConfig = c.getDesiredConfigByType(org.apache.ambari.server.serveraction.upgrades.YarnNodeManagerCapacityCalculationTest.CAPACITY_SCHEDULER_CONFIG_TYPE);
        validateYarnCapacitySchedulerProperties(csConfig, true);
        org.apache.ambari.server.agent.CommandReport report = action.execute(null);
        org.junit.Assert.assertNotNull(report);
        validateYarnHBaseEnvProperties(hbaseEnvConfig, false, org.apache.ambari.server.serveraction.upgrades.YarnNodeManagerCapacityCalculationTest.YARN_SYSTEM_SERVICE_QUEUE_NAME);
        validateYarnCapacitySchedulerProperties(csConfig, false);
    }

    private void validateYarnCapacitySchedulerProperties(org.apache.ambari.server.state.Config csConfig, boolean before) {
        java.util.Map<java.lang.String, java.lang.String> csProps = csConfig.getProperties();
        org.junit.Assert.assertTrue(csProps.containsKey(org.apache.ambari.server.serveraction.upgrades.YarnNodeManagerCapacityCalculationTest.CAPACITY_SCHEDULER_ROOT_QUEUES));
        java.lang.String[] split = csProps.get(org.apache.ambari.server.serveraction.upgrades.YarnNodeManagerCapacityCalculationTest.CAPACITY_SCHEDULER_ROOT_QUEUES).split(",");
        if (before) {
            org.junit.Assert.assertEquals(1, split.length);
            org.junit.Assert.assertEquals(org.apache.ambari.server.serveraction.upgrades.YarnNodeManagerCapacityCalculationTest.YARN_DEFAULT_QUEUE, split[0]);
        } else {
            org.junit.Assert.assertEquals(2, split.length);
            org.junit.Assert.assertEquals(org.apache.ambari.server.serveraction.upgrades.YarnNodeManagerCapacityCalculationTest.YARN_DEFAULT_QUEUE, split[0]);
            org.junit.Assert.assertEquals(org.apache.ambari.server.serveraction.upgrades.YarnNodeManagerCapacityCalculationTest.YARN_SYSTEM_SERVICE_QUEUE_NAME, split[1]);
            org.junit.Assert.assertEquals("0", csProps.get(org.apache.ambari.server.serveraction.upgrades.YarnNodeManagerCapacityCalculationTest.YARN_SYSTEM_SERVICE_QUEUE_PREFIX + ".capacity"));
            org.junit.Assert.assertEquals("100", csProps.get(org.apache.ambari.server.serveraction.upgrades.YarnNodeManagerCapacityCalculationTest.YARN_SYSTEM_SERVICE_QUEUE_PREFIX + ".maximum-capacity"));
            org.junit.Assert.assertEquals("1", csProps.get(org.apache.ambari.server.serveraction.upgrades.YarnNodeManagerCapacityCalculationTest.YARN_SYSTEM_SERVICE_QUEUE_PREFIX + ".user-limit-factor"));
            org.junit.Assert.assertEquals("100", csProps.get(org.apache.ambari.server.serveraction.upgrades.YarnNodeManagerCapacityCalculationTest.YARN_SYSTEM_SERVICE_QUEUE_PREFIX + ".minimum-user-limit-percent"));
            org.junit.Assert.assertEquals("RUNNING", csProps.get(org.apache.ambari.server.serveraction.upgrades.YarnNodeManagerCapacityCalculationTest.YARN_SYSTEM_SERVICE_QUEUE_PREFIX + ".state"));
            org.junit.Assert.assertEquals("fifo", csProps.get(org.apache.ambari.server.serveraction.upgrades.YarnNodeManagerCapacityCalculationTest.YARN_SYSTEM_SERVICE_QUEUE_PREFIX + ".ordering-policy"));
            org.junit.Assert.assertEquals(org.apache.ambari.server.serveraction.upgrades.YarnNodeManagerCapacityCalculationTest.ats_user, csProps.get(org.apache.ambari.server.serveraction.upgrades.YarnNodeManagerCapacityCalculationTest.YARN_SYSTEM_SERVICE_QUEUE_PREFIX + ".acl_submit_applications"));
            org.junit.Assert.assertEquals(org.apache.ambari.server.serveraction.upgrades.YarnNodeManagerCapacityCalculationTest.ats_user, csProps.get(org.apache.ambari.server.serveraction.upgrades.YarnNodeManagerCapacityCalculationTest.YARN_SYSTEM_SERVICE_QUEUE_PREFIX + ".acl_administer_queue"));
            org.junit.Assert.assertEquals("0.5", csProps.get(org.apache.ambari.server.serveraction.upgrades.YarnNodeManagerCapacityCalculationTest.YARN_SYSTEM_SERVICE_QUEUE_PREFIX + ".maximum-am-resource-percent"));
            org.junit.Assert.assertEquals("true", csProps.get(org.apache.ambari.server.serveraction.upgrades.YarnNodeManagerCapacityCalculationTest.YARN_SYSTEM_SERVICE_QUEUE_PREFIX + ".disable_preemption"));
            org.junit.Assert.assertEquals("true", csProps.get(org.apache.ambari.server.serveraction.upgrades.YarnNodeManagerCapacityCalculationTest.YARN_SYSTEM_SERVICE_QUEUE_PREFIX + ".intra-queue-preemption.disable_preemption"));
            org.junit.Assert.assertEquals("32768", csProps.get(org.apache.ambari.server.serveraction.upgrades.YarnNodeManagerCapacityCalculationTest.YARN_SYSTEM_SERVICE_QUEUE_PREFIX + ".priority"));
            org.junit.Assert.assertEquals("-1", csProps.get(org.apache.ambari.server.serveraction.upgrades.YarnNodeManagerCapacityCalculationTest.YARN_SYSTEM_SERVICE_QUEUE_PREFIX + ".maximum-application-lifetime"));
            org.junit.Assert.assertEquals("-1", csProps.get(org.apache.ambari.server.serveraction.upgrades.YarnNodeManagerCapacityCalculationTest.YARN_SYSTEM_SERVICE_QUEUE_PREFIX + ".default-application-lifetime"));
        }
    }

    private org.apache.ambari.server.serveraction.upgrades.YarnNodeManagerCapacityCalculation getYarnNodeManagerCapacityCalculation() throws java.lang.IllegalAccessException {
        org.apache.ambari.server.agent.ExecutionCommand executionCommand = new org.apache.ambari.server.agent.ExecutionCommand();
        java.util.Map<java.lang.String, java.lang.String> commandParams = new java.util.HashMap<>();
        org.apache.ambari.server.actionmanager.HostRoleCommand hrc = org.easymock.EasyMock.createMock(org.apache.ambari.server.actionmanager.HostRoleCommand.class);
        org.apache.ambari.server.serveraction.upgrades.YarnNodeManagerCapacityCalculation action = new org.apache.ambari.server.serveraction.upgrades.YarnNodeManagerCapacityCalculation();
        commandParams.put("clusterName", org.apache.ambari.server.serveraction.upgrades.YarnNodeManagerCapacityCalculationTest.CLUSTER_NAME);
        executionCommand.setCommandParams(commandParams);
        executionCommand.setClusterName(org.apache.ambari.server.serveraction.upgrades.YarnNodeManagerCapacityCalculationTest.CLUSTER_NAME);
        EasyMock.expect(hrc.getRequestId()).andReturn(1L).anyTimes();
        EasyMock.expect(hrc.getStageId()).andReturn(2L).anyTimes();
        EasyMock.expect(hrc.getExecutionCommandWrapper()).andReturn(new org.apache.ambari.server.actionmanager.ExecutionCommandWrapper(executionCommand)).anyTimes();
        EasyMock.replay(cluster, hrc);
        clusterField.set(action, m_clusters);
        agentConfigsHolderField.set(action, agentConfigsHolder);
        action.setExecutionCommand(executionCommand);
        action.setHostRoleCommand(hrc);
        return action;
    }

    private void validateYarnHBaseEnvProperties(org.apache.ambari.server.state.Config hbaseEnvConfig, boolean expected, java.lang.String queueName) {
        java.util.Map<java.lang.String, java.lang.String> map = hbaseEnvConfig.getProperties();
        org.junit.Assert.assertTrue(map.containsKey(org.apache.ambari.server.serveraction.upgrades.YarnNodeManagerCapacityCalculationTest.YARN_HBASE_SYSTEM_SERVICE_LAUNCH_PROPERTY_NAME));
        org.junit.Assert.assertEquals(expected, java.lang.Boolean.parseBoolean(map.get(org.apache.ambari.server.serveraction.upgrades.YarnNodeManagerCapacityCalculationTest.YARN_HBASE_SYSTEM_SERVICE_LAUNCH_PROPERTY_NAME)));
        org.junit.Assert.assertTrue(map.containsKey(org.apache.ambari.server.serveraction.upgrades.YarnNodeManagerCapacityCalculationTest.YARN_HBASE_SYSTEM_SERVICE_QUEUE_PROPERTY_NAME));
        org.junit.Assert.assertEquals(queueName, map.get(org.apache.ambari.server.serveraction.upgrades.YarnNodeManagerCapacityCalculationTest.YARN_HBASE_SYSTEM_SERVICE_QUEUE_PROPERTY_NAME));
    }
}