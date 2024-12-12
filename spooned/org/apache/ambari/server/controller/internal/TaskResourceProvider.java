package org.apache.ambari.server.controller.internal;
import org.codehaus.jackson.map.ObjectMapper;
@org.apache.ambari.server.StaticallyInject
public class TaskResourceProvider extends org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.controller.internal.TaskResourceProvider.class);

    public static final java.lang.String TASK_CLUSTER_NAME_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Tasks", "cluster_name");

    public static final java.lang.String TASK_REQUEST_ID_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Tasks", "request_id");

    public static final java.lang.String TASK_ID_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Tasks", "id");

    public static final java.lang.String TASK_STAGE_ID_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Tasks", "stage_id");

    public static final java.lang.String TASK_HOST_NAME_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Tasks", "host_name");

    public static final java.lang.String TASK_ROLE_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Tasks", "role");

    public static final java.lang.String TASK_COMMAND_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Tasks", "command");

    public static final java.lang.String TASK_STATUS_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Tasks", "status");

    public static final java.lang.String TASK_EXIT_CODE_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Tasks", "exit_code");

    public static final java.lang.String TASK_STDERR_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Tasks", "stderr");

    public static final java.lang.String TASK_STOUT_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Tasks", "stdout");

    public static final java.lang.String TASK_OUTPUTLOG_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Tasks", "output_log");

    public static final java.lang.String TASK_ERRORLOG_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Tasks", "error_log");

    public static final java.lang.String TASK_STRUCT_OUT_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Tasks", "structured_out");

    public static final java.lang.String TASK_START_TIME_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Tasks", "start_time");

    public static final java.lang.String TASK_END_TIME_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Tasks", "end_time");

    public static final java.lang.String TASK_ATTEMPT_CNT_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Tasks", "attempt_cnt");

    public static final java.lang.String TASK_COMMAND_DET_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Tasks", "command_detail");

    public static final java.lang.String TASK_CUST_CMD_NAME_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Tasks", "custom_command_name");

    public static final java.lang.String TASK_COMMAND_OPS_DISPLAY_NAME = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Tasks", "ops_display_name");

    static final java.util.Set<java.lang.String> PROPERTY_IDS = new java.util.HashSet<>();

    static {
        PROPERTY_IDS.add(TASK_CLUSTER_NAME_PROPERTY_ID);
        PROPERTY_IDS.add(TASK_REQUEST_ID_PROPERTY_ID);
        PROPERTY_IDS.add(TASK_ID_PROPERTY_ID);
        PROPERTY_IDS.add(TASK_STAGE_ID_PROPERTY_ID);
        PROPERTY_IDS.add(TASK_HOST_NAME_PROPERTY_ID);
        PROPERTY_IDS.add(TASK_ROLE_PROPERTY_ID);
        PROPERTY_IDS.add(TASK_COMMAND_PROPERTY_ID);
        PROPERTY_IDS.add(TASK_STATUS_PROPERTY_ID);
        PROPERTY_IDS.add(TASK_EXIT_CODE_PROPERTY_ID);
        PROPERTY_IDS.add(TASK_STDERR_PROPERTY_ID);
        PROPERTY_IDS.add(TASK_STOUT_PROPERTY_ID);
        PROPERTY_IDS.add(TASK_OUTPUTLOG_PROPERTY_ID);
        PROPERTY_IDS.add(TASK_ERRORLOG_PROPERTY_ID);
        PROPERTY_IDS.add(TASK_STRUCT_OUT_PROPERTY_ID);
        PROPERTY_IDS.add(TASK_START_TIME_PROPERTY_ID);
        PROPERTY_IDS.add(TASK_END_TIME_PROPERTY_ID);
        PROPERTY_IDS.add(TASK_ATTEMPT_CNT_PROPERTY_ID);
        PROPERTY_IDS.add(TASK_COMMAND_DET_PROPERTY_ID);
        PROPERTY_IDS.add(TASK_CUST_CMD_NAME_PROPERTY_ID);
        PROPERTY_IDS.add(TASK_COMMAND_OPS_DISPLAY_NAME);
    }

    private static final java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> keyPropertyIds = com.google.common.collect.ImmutableMap.<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String>builder().put(org.apache.ambari.server.controller.spi.Resource.Type.Cluster, org.apache.ambari.server.controller.internal.TaskResourceProvider.TASK_CLUSTER_NAME_PROPERTY_ID).put(org.apache.ambari.server.controller.spi.Resource.Type.Request, org.apache.ambari.server.controller.internal.TaskResourceProvider.TASK_REQUEST_ID_PROPERTY_ID).put(org.apache.ambari.server.controller.spi.Resource.Type.Upgrade, org.apache.ambari.server.controller.internal.TaskResourceProvider.TASK_REQUEST_ID_PROPERTY_ID).put(org.apache.ambari.server.controller.spi.Resource.Type.Stage, org.apache.ambari.server.controller.internal.TaskResourceProvider.TASK_STAGE_ID_PROPERTY_ID).put(org.apache.ambari.server.controller.spi.Resource.Type.UpgradeItem, org.apache.ambari.server.controller.internal.TaskResourceProvider.TASK_STAGE_ID_PROPERTY_ID).put(org.apache.ambari.server.controller.spi.Resource.Type.Task, org.apache.ambari.server.controller.internal.TaskResourceProvider.TASK_ID_PROPERTY_ID).build();

    @com.google.inject.Inject
    static org.apache.ambari.server.orm.dao.HostRoleCommandDAO s_dao;

    @com.google.inject.Inject
    private static org.apache.ambari.server.actionmanager.HostRoleCommandFactory s_hostRoleCommandFactory;

    @com.google.inject.Inject
    static org.apache.ambari.server.topology.TopologyManager s_topologyManager;

    private static final org.codehaus.jackson.map.ObjectMapper mapper = new org.codehaus.jackson.map.ObjectMapper();

    TaskResourceProvider(org.apache.ambari.server.controller.AmbariManagementController managementController) {
        super(org.apache.ambari.server.controller.spi.Resource.Type.Task, org.apache.ambari.server.controller.internal.TaskResourceProvider.PROPERTY_IDS, org.apache.ambari.server.controller.internal.TaskResourceProvider.keyPropertyIds, managementController);
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus createResources(org.apache.ambari.server.controller.spi.Request request) {
        throw new java.lang.UnsupportedOperationException("Not currently supported.");
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.controller.spi.Resource> getResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> results = new java.util.LinkedHashSet<>();
        java.util.Set<java.lang.String> requestedIds = getRequestPropertyIds(request, predicate);
        java.util.List<org.apache.ambari.server.orm.entities.HostRoleCommandEntity> entities = org.apache.ambari.server.controller.internal.TaskResourceProvider.s_dao.findAll(request, predicate);
        java.lang.String clusterName = null;
        java.lang.Long requestId = null;
        for (java.util.Map<java.lang.String, java.lang.Object> propertyMap : getPropertyMaps(predicate)) {
            clusterName = ((java.lang.String) (propertyMap.get(org.apache.ambari.server.controller.internal.TaskResourceProvider.TASK_CLUSTER_NAME_PROPERTY_ID)));
            java.lang.String requestIdStr = ((java.lang.String) (propertyMap.get(org.apache.ambari.server.controller.internal.TaskResourceProvider.TASK_REQUEST_ID_PROPERTY_ID)));
            requestId = java.lang.Long.parseLong(requestIdStr);
        }
        java.util.Collection<org.apache.ambari.server.actionmanager.HostRoleCommand> commands = new java.util.ArrayList<>(100);
        if (!entities.isEmpty()) {
            for (org.apache.ambari.server.orm.entities.HostRoleCommandEntity entity : entities) {
                commands.add(org.apache.ambari.server.controller.internal.TaskResourceProvider.s_hostRoleCommandFactory.createExisting(entity));
            }
        } else if (requestId != null) {
            commands.addAll(org.apache.ambari.server.controller.internal.TaskResourceProvider.s_topologyManager.getTasks(requestId));
        }
        org.apache.ambari.server.controller.internal.TaskResourceProvider.LOG.debug("Retrieved {} commands for request {}", commands.size(), request);
        for (org.apache.ambari.server.actionmanager.HostRoleCommand hostRoleCommand : commands) {
            org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Task);
            if (null != clusterName) {
                org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.TaskResourceProvider.TASK_CLUSTER_NAME_PROPERTY_ID, clusterName, requestedIds);
            }
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.TaskResourceProvider.TASK_REQUEST_ID_PROPERTY_ID, hostRoleCommand.getRequestId(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.TaskResourceProvider.TASK_ID_PROPERTY_ID, hostRoleCommand.getTaskId(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.TaskResourceProvider.TASK_STAGE_ID_PROPERTY_ID, hostRoleCommand.getStageId(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.TaskResourceProvider.TASK_HOST_NAME_PROPERTY_ID, ensureHostname(hostRoleCommand.getHostName()), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.TaskResourceProvider.TASK_ROLE_PROPERTY_ID, hostRoleCommand.getRole().toString(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.TaskResourceProvider.TASK_COMMAND_PROPERTY_ID, hostRoleCommand.getRoleCommand(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.TaskResourceProvider.TASK_STATUS_PROPERTY_ID, hostRoleCommand.getStatus(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.TaskResourceProvider.TASK_EXIT_CODE_PROPERTY_ID, hostRoleCommand.getExitCode(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.TaskResourceProvider.TASK_STDERR_PROPERTY_ID, hostRoleCommand.getStderr(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.TaskResourceProvider.TASK_STOUT_PROPERTY_ID, hostRoleCommand.getStdout(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.TaskResourceProvider.TASK_OUTPUTLOG_PROPERTY_ID, hostRoleCommand.getOutputLog(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.TaskResourceProvider.TASK_ERRORLOG_PROPERTY_ID, hostRoleCommand.getErrorLog(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.TaskResourceProvider.TASK_STRUCT_OUT_PROPERTY_ID, parseStructuredOutput(hostRoleCommand.getStructuredOut()), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.TaskResourceProvider.TASK_START_TIME_PROPERTY_ID, hostRoleCommand.getStartTime(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.TaskResourceProvider.TASK_END_TIME_PROPERTY_ID, hostRoleCommand.getEndTime(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.TaskResourceProvider.TASK_ATTEMPT_CNT_PROPERTY_ID, hostRoleCommand.getAttemptCount(), requestedIds);
            if (hostRoleCommand.getCustomCommandName() != null) {
                org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.TaskResourceProvider.TASK_CUST_CMD_NAME_PROPERTY_ID, hostRoleCommand.getCustomCommandName(), requestedIds);
            }
            if (hostRoleCommand.getCommandDetail() == null) {
                org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.TaskResourceProvider.TASK_COMMAND_DET_PROPERTY_ID, java.lang.String.format("%s %s", hostRoleCommand.getRole().toString(), hostRoleCommand.getRoleCommand()), requestedIds);
            } else {
                org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.TaskResourceProvider.TASK_COMMAND_DET_PROPERTY_ID, hostRoleCommand.getCommandDetail(), requestedIds);
            }
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.TaskResourceProvider.TASK_COMMAND_OPS_DISPLAY_NAME, hostRoleCommand.getOpsDisplayName(), requestedIds);
            results.add(resource);
        }
        return results;
    }

    java.util.Map<?, ?> parseStructuredOutput(java.lang.String structuredOutput) {
        if ((null == structuredOutput) || structuredOutput.isEmpty()) {
            return null;
        }
        java.util.Map<?, ?> result = null;
        try {
            result = org.apache.ambari.server.controller.internal.TaskResourceProvider.mapper.readValue(structuredOutput, java.util.Map.class);
        } catch (java.lang.Exception excepton) {
            org.apache.ambari.server.controller.internal.TaskResourceProvider.LOG.warn("Unable to parse task structured output: {}", structuredOutput);
        }
        return result;
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus updateResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        throw new java.lang.UnsupportedOperationException("Not currently supported.");
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus deleteResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        throw new java.lang.UnsupportedOperationException("Not currently supported.");
    }

    @java.lang.Override
    protected java.util.Set<java.lang.String> getPKPropertyIds() {
        return new java.util.HashSet<>(org.apache.ambari.server.controller.internal.TaskResourceProvider.keyPropertyIds.values());
    }

    protected java.lang.String ensureHostname(java.lang.String hostName) {
        return hostName == null ? org.apache.ambari.server.utils.StageUtils.getHostName() : hostName;
    }
}