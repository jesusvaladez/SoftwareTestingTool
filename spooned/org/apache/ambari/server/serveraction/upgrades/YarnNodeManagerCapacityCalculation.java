package org.apache.ambari.server.serveraction.upgrades;
public class YarnNodeManagerCapacityCalculation extends org.apache.ambari.server.serveraction.upgrades.AbstractUpgradeServerAction {
    private static final java.lang.String YARN_SITE_CONFIG_TYPE = "yarn-site";

    private static final java.lang.String YARN_ENV_CONFIG_TYPE = "yarn-env";

    private static final java.lang.String YARN_HBASE_ENV_CONFIG_TYPE = "yarn-hbase-env";

    private static final java.lang.String CAPACITY_SCHEDULER_CONFIG_TYPE = "capacity-scheduler";

    private static final java.lang.String YARN_SYSTEM_SERVICE_USER_NAME = "yarn_ats_user";

    private static final java.lang.String YARN_SYSTEM_SERVICE_QUEUE_NAME = "yarn-system";

    private static final java.lang.String CAPACITY_SCHEDULER_ROOT_QUEUES = "yarn.scheduler.capacity.root.queues";

    private static final java.lang.String YARN_SYSTEM_SERVICE_QUEUE_PREFIX = "yarn.scheduler.capacity.root." + org.apache.ambari.server.serveraction.upgrades.YarnNodeManagerCapacityCalculation.YARN_SYSTEM_SERVICE_QUEUE_NAME;

    private static final float CLUSTER_CAPACITY_LIMIT_FOR_HBASE_SYSTEM_SERVICE = 51200;

    private static final float NODE_CAPACITY_LIMIT_FOR_HBASE_SYSTEM_SERVICE = 10240;

    private static final java.lang.String YARN_NM_PMEM_MB_PROPERTY_NAME = "yarn.nodemanager.resource.memory-mb";

    private static final java.lang.String YARN_HBASE_SYSTEM_SERVICE_QUEUE_PROPERTY_NAME = "yarn_hbase_system_service_queue_name";

    @java.lang.Override
    public org.apache.ambari.server.agent.CommandReport execute(java.util.concurrent.ConcurrentMap<java.lang.String, java.lang.Object> requestSharedDataContext) throws org.apache.ambari.server.AmbariException, java.lang.InterruptedException {
        java.lang.String clusterName = getExecutionCommand().getClusterName();
        org.apache.ambari.server.state.Cluster cluster = getClusters().getCluster(clusterName);
        org.apache.ambari.server.state.Config yarnSiteConfig = cluster.getDesiredConfigByType(org.apache.ambari.server.serveraction.upgrades.YarnNodeManagerCapacityCalculation.YARN_SITE_CONFIG_TYPE);
        if (yarnSiteConfig == null) {
            return createCommandReport(0, org.apache.ambari.server.actionmanager.HostRoleStatus.FAILED, "{}", java.lang.String.format("Source type %s not found", org.apache.ambari.server.serveraction.upgrades.YarnNodeManagerCapacityCalculation.YARN_SITE_CONFIG_TYPE), "");
        }
        int noOfNMHosts = cluster.getService("YARN").getServiceComponent("NODEMANAGER").getServiceComponentsHosts().size();
        java.lang.String nmMemoryInString = yarnSiteConfig.getProperties().get(org.apache.ambari.server.serveraction.upgrades.YarnNodeManagerCapacityCalculation.YARN_NM_PMEM_MB_PROPERTY_NAME);
        int nmMemory = java.lang.Integer.parseInt(nmMemoryInString);
        int clusterCapacity = noOfNMHosts * nmMemory;
        java.lang.String message = "";
        if ((nmMemory > org.apache.ambari.server.serveraction.upgrades.YarnNodeManagerCapacityCalculation.NODE_CAPACITY_LIMIT_FOR_HBASE_SYSTEM_SERVICE) && (clusterCapacity > org.apache.ambari.server.serveraction.upgrades.YarnNodeManagerCapacityCalculation.CLUSTER_CAPACITY_LIMIT_FOR_HBASE_SYSTEM_SERVICE)) {
            org.apache.ambari.server.state.Config yarnEnvConfig = cluster.getDesiredConfigByType(org.apache.ambari.server.serveraction.upgrades.YarnNodeManagerCapacityCalculation.YARN_ENV_CONFIG_TYPE);
            if (yarnEnvConfig == null) {
                return createCommandReport(0, org.apache.ambari.server.actionmanager.HostRoleStatus.FAILED, "{}", java.lang.String.format("Source type %s not found", org.apache.ambari.server.serveraction.upgrades.YarnNodeManagerCapacityCalculation.YARN_ENV_CONFIG_TYPE), "");
            }
            java.lang.String yarnAtsUser = yarnEnvConfig.getProperties().get(org.apache.ambari.server.serveraction.upgrades.YarnNodeManagerCapacityCalculation.YARN_SYSTEM_SERVICE_USER_NAME);
            org.apache.ambari.server.state.Config hbaseEnvConfig = cluster.getDesiredConfigByType(org.apache.ambari.server.serveraction.upgrades.YarnNodeManagerCapacityCalculation.YARN_HBASE_ENV_CONFIG_TYPE);
            if (hbaseEnvConfig == null) {
                return createCommandReport(0, org.apache.ambari.server.actionmanager.HostRoleStatus.FAILED, "{}", java.lang.String.format("Source type %s not found", org.apache.ambari.server.serveraction.upgrades.YarnNodeManagerCapacityCalculation.YARN_HBASE_ENV_CONFIG_TYPE), "");
            }
            java.util.Map<java.lang.String, java.lang.String> hbaseEnvConfigProperties = hbaseEnvConfig.getProperties();
            java.lang.String oldSystemServiceQueue = hbaseEnvConfigProperties.get(org.apache.ambari.server.serveraction.upgrades.YarnNodeManagerCapacityCalculation.YARN_HBASE_SYSTEM_SERVICE_QUEUE_PROPERTY_NAME);
            org.apache.ambari.server.state.Config csConfig = cluster.getDesiredConfigByType(org.apache.ambari.server.serveraction.upgrades.YarnNodeManagerCapacityCalculation.CAPACITY_SCHEDULER_CONFIG_TYPE);
            if (csConfig == null) {
                return createCommandReport(0, org.apache.ambari.server.actionmanager.HostRoleStatus.FAILED, "{}", java.lang.String.format("Source type %s not found", org.apache.ambari.server.serveraction.upgrades.YarnNodeManagerCapacityCalculation.CAPACITY_SCHEDULER_CONFIG_TYPE), "");
            }
            java.util.Map<java.lang.String, java.lang.String> csProperties = csConfig.getProperties();
            java.lang.String old_root_queues = csProperties.get(org.apache.ambari.server.serveraction.upgrades.YarnNodeManagerCapacityCalculation.CAPACITY_SCHEDULER_ROOT_QUEUES);
            java.util.Set<java.lang.String> queues = com.google.common.collect.Sets.newHashSet(old_root_queues.split(","));
            boolean isYarnSystemQueueExist = false;
            isYarnSystemQueueExist = queues.stream().map(queue -> queue.trim()).filter(queueName -> org.apache.ambari.server.serveraction.upgrades.YarnNodeManagerCapacityCalculation.YARN_SYSTEM_SERVICE_QUEUE_NAME.equals(queueName)).findFirst().isPresent();
            java.lang.String new_root_queues = (old_root_queues + ",") + org.apache.ambari.server.serveraction.upgrades.YarnNodeManagerCapacityCalculation.YARN_SYSTEM_SERVICE_QUEUE_NAME;
            if (!isYarnSystemQueueExist) {
                csProperties.put(org.apache.ambari.server.serveraction.upgrades.YarnNodeManagerCapacityCalculation.CAPACITY_SCHEDULER_ROOT_QUEUES, new_root_queues);
                csProperties.put(org.apache.ambari.server.serveraction.upgrades.YarnNodeManagerCapacityCalculation.YARN_SYSTEM_SERVICE_QUEUE_PREFIX + ".capacity", "0");
                csProperties.put(org.apache.ambari.server.serveraction.upgrades.YarnNodeManagerCapacityCalculation.YARN_SYSTEM_SERVICE_QUEUE_PREFIX + ".maximum-capacity", "100");
                csProperties.put(org.apache.ambari.server.serveraction.upgrades.YarnNodeManagerCapacityCalculation.YARN_SYSTEM_SERVICE_QUEUE_PREFIX + ".user-limit-factor", "1");
                csProperties.put(org.apache.ambari.server.serveraction.upgrades.YarnNodeManagerCapacityCalculation.YARN_SYSTEM_SERVICE_QUEUE_PREFIX + ".minimum-user-limit-percent", "100");
                csProperties.put(org.apache.ambari.server.serveraction.upgrades.YarnNodeManagerCapacityCalculation.YARN_SYSTEM_SERVICE_QUEUE_PREFIX + ".state", "RUNNING");
                csProperties.put(org.apache.ambari.server.serveraction.upgrades.YarnNodeManagerCapacityCalculation.YARN_SYSTEM_SERVICE_QUEUE_PREFIX + ".ordering-policy", "fifo");
                csProperties.put(org.apache.ambari.server.serveraction.upgrades.YarnNodeManagerCapacityCalculation.YARN_SYSTEM_SERVICE_QUEUE_PREFIX + ".acl_submit_applications", yarnAtsUser);
                csProperties.put(org.apache.ambari.server.serveraction.upgrades.YarnNodeManagerCapacityCalculation.YARN_SYSTEM_SERVICE_QUEUE_PREFIX + ".acl_administer_queue", yarnAtsUser);
                csProperties.put(org.apache.ambari.server.serveraction.upgrades.YarnNodeManagerCapacityCalculation.YARN_SYSTEM_SERVICE_QUEUE_PREFIX + ".maximum-am-resource-percent", "0.5");
                csProperties.put(org.apache.ambari.server.serveraction.upgrades.YarnNodeManagerCapacityCalculation.YARN_SYSTEM_SERVICE_QUEUE_PREFIX + ".disable_preemption", "true");
                csProperties.put(org.apache.ambari.server.serveraction.upgrades.YarnNodeManagerCapacityCalculation.YARN_SYSTEM_SERVICE_QUEUE_PREFIX + ".intra-queue-preemption.disable_preemption", "true");
                csProperties.put(org.apache.ambari.server.serveraction.upgrades.YarnNodeManagerCapacityCalculation.YARN_SYSTEM_SERVICE_QUEUE_PREFIX + ".priority", "32768");
                csProperties.put(org.apache.ambari.server.serveraction.upgrades.YarnNodeManagerCapacityCalculation.YARN_SYSTEM_SERVICE_QUEUE_PREFIX + ".maximum-application-lifetime", "-1");
                csProperties.put(org.apache.ambari.server.serveraction.upgrades.YarnNodeManagerCapacityCalculation.YARN_SYSTEM_SERVICE_QUEUE_PREFIX + ".default-application-lifetime", "-1");
                csConfig.setProperties(csProperties);
                csConfig.save();
                hbaseEnvConfigProperties.put(org.apache.ambari.server.serveraction.upgrades.YarnNodeManagerCapacityCalculation.YARN_HBASE_SYSTEM_SERVICE_QUEUE_PROPERTY_NAME, org.apache.ambari.server.serveraction.upgrades.YarnNodeManagerCapacityCalculation.YARN_SYSTEM_SERVICE_QUEUE_NAME);
                hbaseEnvConfig.setProperties(hbaseEnvConfigProperties);
                hbaseEnvConfig.save();
                message = java.lang.String.format("%s was set from %s to %s. %s was set from %s to %s", org.apache.ambari.server.serveraction.upgrades.YarnNodeManagerCapacityCalculation.CAPACITY_SCHEDULER_ROOT_QUEUES, old_root_queues, new_root_queues, org.apache.ambari.server.serveraction.upgrades.YarnNodeManagerCapacityCalculation.YARN_HBASE_SYSTEM_SERVICE_QUEUE_PROPERTY_NAME, oldSystemServiceQueue, org.apache.ambari.server.serveraction.upgrades.YarnNodeManagerCapacityCalculation.YARN_SYSTEM_SERVICE_QUEUE_NAME);
            }
        }
        agentConfigsHolder.updateData(cluster.getClusterId(), cluster.getHosts().stream().map(org.apache.ambari.server.state.Host::getHostId).collect(java.util.stream.Collectors.toList()));
        return createCommandReport(0, org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, "{}", message, "");
    }
}