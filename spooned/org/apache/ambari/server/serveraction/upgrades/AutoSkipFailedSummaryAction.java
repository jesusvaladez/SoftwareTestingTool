package org.apache.ambari.server.serveraction.upgrades;
public class AutoSkipFailedSummaryAction extends org.apache.ambari.server.serveraction.upgrades.AbstractUpgradeServerAction {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.serveraction.upgrades.AutoSkipFailedSummaryAction.class);

    private static final java.lang.String FAILURE_STD_OUT_TEMPLATE = "There were {0} skipped failure(s) that must be addressed before you can proceed. Please resolve each failure before continuing with the upgrade.";

    private static final java.lang.String SKIPPED_SERVICE_CHECK = "service_check";

    private static final java.lang.String SKIPPED_HOST_COMPONENT = "host_component";

    private static final java.lang.String SKIPPED = "skipped";

    private static final java.lang.String FAILURES = "failures";

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.HostRoleCommandDAO m_hostRoleCommandDAO;

    @com.google.inject.Inject
    private com.google.gson.Gson m_gson;

    @com.google.inject.Inject
    private org.apache.ambari.server.metadata.ActionMetadata actionMetadata;

    private java.util.Map<java.lang.String, java.lang.Object> m_structuredFailures = new java.util.HashMap<>();

    @java.lang.Override
    public org.apache.ambari.server.agent.CommandReport execute(java.util.concurrent.ConcurrentMap<java.lang.String, java.lang.Object> requestSharedDataContext) throws org.apache.ambari.server.AmbariException, java.lang.InterruptedException {
        org.apache.ambari.server.actionmanager.HostRoleCommand hostRoleCommand = getHostRoleCommand();
        long requestId = hostRoleCommand.getRequestId();
        long stageId = hostRoleCommand.getStageId();
        java.lang.String clusterName = hostRoleCommand.getExecutionCommandWrapper().getExecutionCommand().getClusterName();
        org.apache.ambari.server.state.Cluster cluster = getClusters().getCluster(clusterName);
        org.apache.ambari.server.orm.entities.UpgradeItemEntity upgradeItem = m_upgradeDAO.findUpgradeItemByRequestAndStage(requestId, stageId);
        org.apache.ambari.server.orm.entities.UpgradeGroupEntity upgradeGroup = upgradeItem.getGroupEntity();
        long upgradeGroupId = upgradeGroup.getId();
        org.apache.ambari.server.orm.entities.UpgradeGroupEntity upgradeGroupEntity = m_upgradeDAO.findUpgradeGroup(upgradeGroupId);
        java.util.List<org.apache.ambari.server.orm.entities.UpgradeItemEntity> groupUpgradeItems = upgradeGroupEntity.getItems();
        java.util.TreeSet<java.lang.Long> stageIds = new java.util.TreeSet<>();
        for (org.apache.ambari.server.orm.entities.UpgradeItemEntity groupUpgradeItem : groupUpgradeItems) {
            stageIds.add(groupUpgradeItem.getStageId());
        }
        long minStageId = stageIds.first();
        long maxStageId = stageIds.last();
        java.util.List<org.apache.ambari.server.orm.entities.HostRoleCommandEntity> skippedTasks = m_hostRoleCommandDAO.findByStatusBetweenStages(hostRoleCommand.getRequestId(), org.apache.ambari.server.actionmanager.HostRoleStatus.SKIPPED_FAILED, minStageId, maxStageId);
        if (skippedTasks.isEmpty()) {
            return createCommandReport(0, org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, "{}", "There were no skipped failures", null);
        }
        java.lang.StringBuilder buffer = new java.lang.StringBuilder("The following steps failed but were automatically skipped:\n");
        java.util.Set<java.lang.String> skippedCategories = new java.util.HashSet<>();
        java.util.Map<java.lang.String, java.lang.Object> skippedFailures = new java.util.HashMap<>();
        java.util.Set<java.lang.String> skippedServiceChecks = new java.util.HashSet<>();
        java.util.Map<java.lang.String, java.lang.Object> hostComponents = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.util.Set<org.apache.ambari.server.Role>> publishedHostComponents = new java.util.HashMap<>();
        for (org.apache.ambari.server.orm.entities.HostRoleCommandEntity skippedTask : skippedTasks) {
            try {
                java.lang.String skippedCategory;
                if (skippedTask.getRoleCommand().equals(org.apache.ambari.server.RoleCommand.SERVICE_CHECK)) {
                    skippedCategory = org.apache.ambari.server.serveraction.upgrades.AutoSkipFailedSummaryAction.SKIPPED_SERVICE_CHECK;
                    java.lang.String serviceCheckActionName = skippedTask.getRole().toString();
                    java.lang.String service = actionMetadata.getServiceNameByServiceCheckAction(serviceCheckActionName);
                    skippedServiceChecks.add(service);
                    skippedFailures.put(org.apache.ambari.server.serveraction.upgrades.AutoSkipFailedSummaryAction.SKIPPED_SERVICE_CHECK, skippedServiceChecks);
                    m_structuredFailures.put(org.apache.ambari.server.serveraction.upgrades.AutoSkipFailedSummaryAction.FAILURES, skippedFailures);
                } else {
                    skippedCategory = org.apache.ambari.server.serveraction.upgrades.AutoSkipFailedSummaryAction.SKIPPED_HOST_COMPONENT;
                    java.lang.String hostName = skippedTask.getHostName();
                    if (null != hostName) {
                        java.util.List<java.lang.Object> failures = ((java.util.List<java.lang.Object>) (hostComponents.get(hostName)));
                        if (null == failures) {
                            failures = new java.util.ArrayList<>();
                            hostComponents.put(hostName, failures);
                            publishedHostComponents.put(hostName, new java.util.HashSet<>());
                        }
                        java.util.Set<org.apache.ambari.server.Role> publishedHostComponentsOnHost = publishedHostComponents.get(hostName);
                        org.apache.ambari.server.Role role = skippedTask.getRole();
                        if (!publishedHostComponentsOnHost.contains(role)) {
                            java.util.HashMap<java.lang.String, java.lang.String> details = new java.util.HashMap<>();
                            java.lang.String service = cluster.getServiceByComponentName(role.toString()).getName();
                            details.put("service", service);
                            details.put("component", role.toString());
                            failures.add(details);
                        }
                    }
                    skippedFailures.put(org.apache.ambari.server.serveraction.upgrades.AutoSkipFailedSummaryAction.SKIPPED_HOST_COMPONENT, hostComponents);
                    m_structuredFailures.put(org.apache.ambari.server.serveraction.upgrades.AutoSkipFailedSummaryAction.FAILURES, skippedFailures);
                }
                skippedCategories.add(skippedCategory);
                org.apache.ambari.server.actionmanager.ServiceComponentHostEventWrapper eventWrapper = new org.apache.ambari.server.actionmanager.ServiceComponentHostEventWrapper(skippedTask.getEvent());
                org.apache.ambari.server.state.ServiceComponentHostEvent event = eventWrapper.getEvent();
                buffer.append(event.getServiceComponentName());
                if (null != event.getHostName()) {
                    buffer.append(" on ");
                    buffer.append(event.getHostName());
                }
                buffer.append(": ");
                buffer.append(skippedTask.getCommandDetail());
                buffer.append("\n");
            } catch (java.lang.Exception exception) {
                org.apache.ambari.server.serveraction.upgrades.AutoSkipFailedSummaryAction.LOG.warn("Unable to extract failure information for {}", skippedTask);
                buffer.append(": ");
                buffer.append(skippedTask);
            }
        }
        m_structuredFailures.put(org.apache.ambari.server.serveraction.upgrades.AutoSkipFailedSummaryAction.SKIPPED, skippedCategories);
        java.lang.String structuredOutput = m_gson.toJson(m_structuredFailures);
        java.lang.String standardOutput = java.text.MessageFormat.format(org.apache.ambari.server.serveraction.upgrades.AutoSkipFailedSummaryAction.FAILURE_STD_OUT_TEMPLATE, skippedTasks.size());
        java.lang.String standardError = buffer.toString();
        return createCommandReport(0, org.apache.ambari.server.actionmanager.HostRoleStatus.HOLDING, structuredOutput, standardOutput, standardError);
    }
}