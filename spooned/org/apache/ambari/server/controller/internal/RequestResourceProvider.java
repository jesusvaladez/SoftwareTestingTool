package org.apache.ambari.server.controller.internal;
import org.apache.commons.lang.StringUtils;
import static org.apache.ambari.server.controller.internal.HostComponentResourceProvider.CLUSTER_NAME;
import static org.apache.ambari.server.controller.internal.HostComponentResourceProvider.COMPONENT_NAME;
import static org.apache.ambari.server.controller.internal.HostComponentResourceProvider.HOST_NAME;
import static org.apache.ambari.server.controller.internal.HostComponentResourceProvider.SERVICE_NAME;
import static org.apache.ambari.server.security.authorization.RoleAuthorization.AMBARI_VIEW_STATUS_INFO;
import static org.apache.ambari.server.security.authorization.RoleAuthorization.CLUSTER_VIEW_STATUS_INFO;
import static org.apache.ambari.server.security.authorization.RoleAuthorization.HOST_VIEW_STATUS_INFO;
import static org.apache.ambari.server.security.authorization.RoleAuthorization.SERVICE_VIEW_STATUS_INFO;
@org.apache.ambari.server.StaticallyInject
public class RequestResourceProvider extends org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.controller.internal.RequestResourceProvider.class);

    @com.google.inject.Inject
    private static org.apache.ambari.server.orm.dao.RequestDAO s_requestDAO = null;

    @com.google.inject.Inject
    private static org.apache.ambari.server.orm.dao.HostRoleCommandDAO s_hostRoleCommandDAO = null;

    @com.google.inject.Inject
    private static org.apache.ambari.server.topology.TopologyManager topologyManager;

    public static final java.lang.String REQUESTS = "Requests";

    public static final java.lang.String REQUEST_INFO = "RequestInfo";

    public static final java.lang.String REQUEST_CLUSTER_NAME_PROPERTY_ID = org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUESTS + "/cluster_name";

    public static final java.lang.String REQUEST_CLUSTER_ID_PROPERTY_ID = org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUESTS + "/cluster_id";

    public static final java.lang.String REQUEST_ID_PROPERTY_ID = org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUESTS + "/id";

    public static final java.lang.String REQUEST_STATUS_PROPERTY_ID = org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUESTS + "/request_status";

    public static final java.lang.String REQUEST_ABORT_REASON_PROPERTY_ID = org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUESTS + "/abort_reason";

    public static final java.lang.String REQUEST_CONTEXT_ID = org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUESTS + "/request_context";

    public static final java.lang.String REQUEST_SOURCE_SCHEDULE = org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUESTS + "/request_schedule";

    public static final java.lang.String REQUEST_SOURCE_SCHEDULE_ID = org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUESTS + "/request_schedule/schedule_id";

    public static final java.lang.String REQUEST_SOURCE_SCHEDULE_HREF = org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUESTS + "/request_schedule/href";

    public static final java.lang.String REQUEST_TYPE_ID = org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUESTS + "/type";

    public static final java.lang.String REQUEST_INPUTS_ID = org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUESTS + "/inputs";

    public static final java.lang.String REQUEST_CLUSTER_HOST_INFO_ID = org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUESTS + "/cluster_host_info";

    public static final java.lang.String REQUEST_RESOURCE_FILTER_ID = org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUESTS + "/resource_filters";

    public static final java.lang.String REQUEST_OPERATION_LEVEL_ID = org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUESTS + "/operation_level";

    public static final java.lang.String REQUEST_CREATE_TIME_ID = org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUESTS + "/create_time";

    public static final java.lang.String REQUEST_START_TIME_ID = org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUESTS + "/start_time";

    public static final java.lang.String REQUEST_END_TIME_ID = org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUESTS + "/end_time";

    public static final java.lang.String REQUEST_EXCLUSIVE_ID = org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUESTS + "/exclusive";

    public static final java.lang.String REQUEST_TASK_CNT_ID = org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUESTS + "/task_count";

    public static final java.lang.String REQUEST_FAILED_TASK_CNT_ID = org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUESTS + "/failed_task_count";

    public static final java.lang.String REQUEST_ABORTED_TASK_CNT_ID = org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUESTS + "/aborted_task_count";

    public static final java.lang.String REQUEST_TIMED_OUT_TASK_CNT_ID = org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUESTS + "/timed_out_task_count";

    public static final java.lang.String REQUEST_COMPLETED_TASK_CNT_ID = org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUESTS + "/completed_task_count";

    public static final java.lang.String REQUEST_QUEUED_TASK_CNT_ID = org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUESTS + "/queued_task_count";

    public static final java.lang.String REQUEST_PROGRESS_PERCENT_ID = org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUESTS + "/progress_percent";

    public static final java.lang.String REQUEST_REMOVE_PENDING_HOST_REQUESTS_ID = org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUESTS + "/remove_pending_host_requests";

    public static final java.lang.String REQUEST_PENDING_HOST_REQUEST_COUNT_ID = org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUESTS + "/pending_host_request_count";

    public static final java.lang.String REQUEST_USER_NAME = org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUESTS + "/user_name";

    public static final java.lang.String COMMAND_ID = "command";

    public static final java.lang.String SERVICE_ID = "service_name";

    public static final java.lang.String COMPONENT_ID = "component_name";

    public static final java.lang.String HOSTS_ID = "hosts";

    public static final java.lang.String HOSTS_PREDICATE = "hosts_predicate";

    public static final java.lang.String ACTION_ID = "action";

    public static final java.lang.String INPUTS_ID = "parameters";

    public static final java.lang.String EXCLUSIVE_ID = "exclusive";

    public static final java.lang.String HAS_RESOURCE_FILTERS = "HAS_RESOURCE_FILTERS";

    public static final java.lang.String CONTEXT = "context";

    private static final java.util.Set<java.lang.String> PK_PROPERTY_IDS = com.google.common.collect.ImmutableSet.of(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_ID_PROPERTY_ID);

    private org.apache.ambari.server.api.predicate.PredicateCompiler predicateCompiler = new org.apache.ambari.server.api.predicate.PredicateCompiler();

    private static final java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> keyPropertyIds = com.google.common.collect.ImmutableMap.<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String>builder().put(org.apache.ambari.server.controller.spi.Resource.Type.Request, org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_ID_PROPERTY_ID).put(org.apache.ambari.server.controller.spi.Resource.Type.Cluster, org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_CLUSTER_NAME_PROPERTY_ID).build();

    static java.util.Set<java.lang.String> PROPERTY_IDS = com.google.common.collect.Sets.newHashSet(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_ID_PROPERTY_ID, org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_CLUSTER_NAME_PROPERTY_ID, org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_CLUSTER_ID_PROPERTY_ID, org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_STATUS_PROPERTY_ID, org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_ABORT_REASON_PROPERTY_ID, org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_CONTEXT_ID, org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_SOURCE_SCHEDULE, org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_SOURCE_SCHEDULE_ID, org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_SOURCE_SCHEDULE_HREF, org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_TYPE_ID, org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_INPUTS_ID, org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_RESOURCE_FILTER_ID, org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_OPERATION_LEVEL_ID, org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_CREATE_TIME_ID, org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_START_TIME_ID, org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_END_TIME_ID, org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_EXCLUSIVE_ID, org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_TASK_CNT_ID, org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_FAILED_TASK_CNT_ID, org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_ABORTED_TASK_CNT_ID, org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_TIMED_OUT_TASK_CNT_ID, org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_COMPLETED_TASK_CNT_ID, org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_QUEUED_TASK_CNT_ID, org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_PROGRESS_PERCENT_ID, org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_REMOVE_PENDING_HOST_REQUESTS_ID, org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_PENDING_HOST_REQUEST_COUNT_ID, org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_CLUSTER_HOST_INFO_ID, org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_USER_NAME);

    RequestResourceProvider(org.apache.ambari.server.controller.AmbariManagementController managementController) {
        super(org.apache.ambari.server.controller.spi.Resource.Type.Request, org.apache.ambari.server.controller.internal.RequestResourceProvider.PROPERTY_IDS, org.apache.ambari.server.controller.internal.RequestResourceProvider.keyPropertyIds, managementController);
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus createResources(org.apache.ambari.server.controller.spi.Request request) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException, org.apache.ambari.server.controller.spi.ResourceAlreadyExistsException {
        if (request.getProperties().size() > 1) {
            throw new java.lang.UnsupportedOperationException("Multiple actions/commands cannot be executed at the same time.");
        }
        final org.apache.ambari.server.controller.ExecuteActionRequest actionRequest = getActionRequest(request);
        final java.util.Map<java.lang.String, java.lang.String> requestInfoProperties = request.getRequestInfoProperties();
        return getRequestStatus(createResources(new org.apache.ambari.server.controller.internal.AbstractResourceProvider.Command<org.apache.ambari.server.controller.RequestStatusResponse>() {
            @java.lang.Override
            public org.apache.ambari.server.controller.RequestStatusResponse invoke() throws org.apache.ambari.server.AmbariException, org.apache.ambari.server.security.authorization.AuthorizationException {
                java.lang.String clusterName = actionRequest.getClusterName();
                org.apache.ambari.server.security.authorization.ResourceType resourceType;
                java.lang.Long resourceId;
                if (org.apache.commons.lang.StringUtils.isEmpty(clusterName)) {
                    resourceType = org.apache.ambari.server.security.authorization.ResourceType.AMBARI;
                    resourceId = null;
                } else {
                    resourceType = org.apache.ambari.server.security.authorization.ResourceType.CLUSTER;
                    resourceId = getClusterResourceId(clusterName);
                }
                if (actionRequest.isCommand()) {
                    java.lang.String commandName = actionRequest.getCommandName();
                    if (org.apache.commons.lang.StringUtils.isEmpty(commandName)) {
                        commandName = "_unknown_command_";
                    }
                    if (commandName.endsWith("_SERVICE_CHECK")) {
                        if (!org.apache.ambari.server.security.authorization.AuthorizationHelper.isAuthorized(resourceType, resourceId, org.apache.ambari.server.security.authorization.RoleAuthorization.SERVICE_RUN_SERVICE_CHECK)) {
                            throw new org.apache.ambari.server.security.authorization.AuthorizationException("The authenticated user is not authorized to execute service checks.");
                        }
                    } else if (commandName.equals("DECOMMISSION")) {
                        if (!org.apache.ambari.server.security.authorization.AuthorizationHelper.isAuthorized(resourceType, resourceId, org.apache.ambari.server.security.authorization.RoleAuthorization.SERVICE_DECOMMISSION_RECOMMISSION)) {
                            throw new org.apache.ambari.server.security.authorization.AuthorizationException("The authenticated user is not authorized to decommission services.");
                        }
                    } else if (!org.apache.ambari.server.security.authorization.AuthorizationHelper.isAuthorized(resourceType, resourceId, org.apache.ambari.server.security.authorization.RoleAuthorization.SERVICE_RUN_CUSTOM_COMMAND)) {
                        throw new org.apache.ambari.server.security.authorization.AuthorizationException(java.lang.String.format("The authenticated user is not authorized to execute the command, %s.", commandName));
                    }
                } else {
                    java.lang.String actionName = actionRequest.getActionName();
                    if (org.apache.commons.lang.StringUtils.isEmpty(actionName)) {
                        actionName = "_unknown_action_";
                    }
                    if (actionName.contains("SERVICE_CHECK")) {
                        if (!org.apache.ambari.server.security.authorization.AuthorizationHelper.isAuthorized(resourceType, resourceId, org.apache.ambari.server.security.authorization.RoleAuthorization.SERVICE_RUN_SERVICE_CHECK)) {
                            throw new org.apache.ambari.server.security.authorization.AuthorizationException("The authenticated user is not authorized to execute service checks.");
                        }
                    } else {
                        org.apache.ambari.server.customactions.ActionDefinition actionDefinition = getManagementController().getAmbariMetaInfo().getActionDefinition(actionName);
                        java.util.Set<org.apache.ambari.server.security.authorization.RoleAuthorization> permissions = (actionDefinition == null) ? null : actionDefinition.getPermissions();
                        org.apache.ambari.server.security.authorization.ResourceType customActionResourceType = resourceType;
                        if (actionName.contains("check_host")) {
                            customActionResourceType = org.apache.ambari.server.security.authorization.ResourceType.CLUSTER;
                        }
                        if (!org.apache.ambari.server.security.authorization.AuthorizationHelper.isAuthorized(customActionResourceType, resourceId, permissions)) {
                            throw new org.apache.ambari.server.security.authorization.AuthorizationException(java.lang.String.format("The authenticated user is not authorized to execute the action %s.", actionName));
                        }
                    }
                }
                return getManagementController().createAction(actionRequest, requestInfoProperties);
            }
        }));
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.controller.spi.Resource> getResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        java.util.Set<java.lang.String> requestedIds = getRequestPropertyIds(request, predicate);
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = new java.util.HashSet<>();
        java.lang.String maxResultsRaw = request.getRequestInfoProperties().get(org.apache.ambari.server.api.services.BaseRequest.PAGE_SIZE_PROPERTY_KEY);
        java.lang.String ascOrderRaw = request.getRequestInfoProperties().get(org.apache.ambari.server.api.services.BaseRequest.ASC_ORDER_PROPERTY_KEY);
        java.lang.Integer maxResults = (maxResultsRaw == null) ? null : java.lang.Integer.parseInt(maxResultsRaw);
        java.lang.Boolean ascOrder = (ascOrderRaw == null) ? null : java.lang.Boolean.parseBoolean(ascOrderRaw);
        if (null == predicate) {
            authorizeGetResources(null);
            resources.addAll(getRequestResources(null, null, null, maxResults, ascOrder, requestedIds));
        } else {
            for (java.util.Map<java.lang.String, java.lang.Object> properties : getPropertyMaps(predicate)) {
                java.lang.String clusterName = ((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_CLUSTER_NAME_PROPERTY_ID)));
                java.lang.Long requestId = null;
                if (properties.get(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_ID_PROPERTY_ID) != null) {
                    requestId = java.lang.Long.valueOf(((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_ID_PROPERTY_ID))));
                }
                java.lang.String requestStatus = null;
                if (properties.get(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_STATUS_PROPERTY_ID) != null) {
                    requestStatus = ((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_STATUS_PROPERTY_ID)));
                }
                authorizeGetResources(clusterName);
                resources.addAll(getRequestResources(clusterName, requestId, requestStatus, maxResults, ascOrder, requestedIds));
            }
        }
        return resources;
    }

    private void authorizeGetResources(java.lang.String clusterName) throws org.apache.ambari.server.controller.spi.NoSuchParentResourceException, org.apache.ambari.server.security.authorization.AuthorizationException {
        final boolean ambariLevelRequest = org.apache.commons.lang.StringUtils.isBlank(clusterName);
        final org.apache.ambari.server.security.authorization.ResourceType resourceType = (ambariLevelRequest) ? org.apache.ambari.server.security.authorization.ResourceType.AMBARI : org.apache.ambari.server.security.authorization.ResourceType.CLUSTER;
        java.lang.Long resourceId;
        try {
            resourceId = (ambariLevelRequest) ? null : getClusterResourceId(clusterName);
        } catch (org.apache.ambari.server.AmbariException e) {
            throw new org.apache.ambari.server.controller.spi.NoSuchParentResourceException("Error while fetching cluster resource ID", e);
        }
        final java.util.Set<org.apache.ambari.server.security.authorization.RoleAuthorization> requiredAuthorizations = (ambariLevelRequest) ? com.google.common.collect.Sets.newHashSet(org.apache.ambari.server.security.authorization.RoleAuthorization.AMBARI_VIEW_STATUS_INFO) : com.google.common.collect.Sets.newHashSet(org.apache.ambari.server.security.authorization.RoleAuthorization.CLUSTER_VIEW_STATUS_INFO, org.apache.ambari.server.security.authorization.RoleAuthorization.HOST_VIEW_STATUS_INFO, org.apache.ambari.server.security.authorization.RoleAuthorization.SERVICE_VIEW_STATUS_INFO);
        if (!org.apache.ambari.server.security.authorization.AuthorizationHelper.isAuthorized(resourceType, resourceId, requiredAuthorizations)) {
            throw new org.apache.ambari.server.security.authorization.AuthorizationException(java.lang.String.format("The authenticated user is not authorized to fetch request related information."));
        }
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus updateResources(org.apache.ambari.server.controller.spi.Request requestInfo, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        org.apache.ambari.server.controller.AmbariManagementController amc = getManagementController();
        final java.util.Set<org.apache.ambari.server.controller.RequestRequest> requests = new java.util.HashSet<>();
        java.util.Iterator<java.util.Map<java.lang.String, java.lang.Object>> iterator = requestInfo.getProperties().iterator();
        if (iterator.hasNext()) {
            for (java.util.Map<java.lang.String, java.lang.Object> propertyMap : getPropertyMaps(iterator.next(), predicate)) {
                requests.add(getRequest(propertyMap));
            }
        }
        java.util.List<org.apache.ambari.server.actionmanager.Request> targets = new java.util.ArrayList<>();
        for (org.apache.ambari.server.controller.RequestRequest updateRequest : requests) {
            org.apache.ambari.server.actionmanager.ActionManager actionManager = amc.getActionManager();
            java.util.List<org.apache.ambari.server.actionmanager.Request> internalRequests = actionManager.getRequests(java.util.Collections.singletonList(updateRequest.getRequestId()));
            if (internalRequests.size() == 0) {
                throw new java.lang.IllegalArgumentException(java.lang.String.format("Request %s does not exist", updateRequest.getRequestId()));
            }
            org.apache.ambari.server.actionmanager.Request internalRequest = internalRequests.get(0);
            if (updateRequest.isRemovePendingHostRequests()) {
                if (internalRequest instanceof org.apache.ambari.server.topology.LogicalRequest) {
                    targets.add(internalRequest);
                } else {
                    throw new java.lang.IllegalArgumentException(("Request with id: " + internalRequest.getRequestId()) + "is not a Logical Request.");
                }
            } else {
                if ((updateRequest.getAbortReason() == null) || updateRequest.getAbortReason().isEmpty()) {
                    throw new java.lang.IllegalArgumentException("Abort reason can not be empty.");
                }
                if (updateRequest.getStatus() != org.apache.ambari.server.actionmanager.HostRoleStatus.ABORTED) {
                    throw new java.lang.IllegalArgumentException(java.lang.String.format("%s is wrong value. The only allowed value " + "for updating request status is ABORTED", updateRequest.getStatus()));
                }
                org.apache.ambari.server.actionmanager.HostRoleStatus internalRequestStatus = org.apache.ambari.server.controller.internal.CalculatedStatus.statusFromStages(internalRequest.getStages()).getStatus();
                if (internalRequestStatus.isCompletedState()) {
                } else {
                    targets.add(internalRequest);
                }
            }
        }
        java.util.Iterator<org.apache.ambari.server.controller.RequestRequest> reqIterator = requests.iterator();
        for (org.apache.ambari.server.actionmanager.Request target : targets) {
            if (target instanceof org.apache.ambari.server.topology.LogicalRequest) {
                org.apache.ambari.server.controller.internal.RequestResourceProvider.topologyManager.removePendingHostRequests(target.getClusterName(), target.getRequestId());
            } else {
                java.lang.String reason = reqIterator.next().getAbortReason();
                amc.getActionManager().cancelRequest(target.getRequestId(), reason);
            }
        }
        return getRequestStatus(null);
    }

    private org.apache.ambari.server.controller.RequestRequest getRequest(java.util.Map<java.lang.String, java.lang.Object> propertyMap) {
        java.lang.String clusterNameStr = ((java.lang.String) (propertyMap.get(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_CLUSTER_NAME_PROPERTY_ID)));
        java.lang.String requestIdStr = ((java.lang.String) (propertyMap.get(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_ID_PROPERTY_ID)));
        long requestId = java.lang.Integer.parseInt(requestIdStr);
        java.lang.String requestStatusStr = ((java.lang.String) (propertyMap.get(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_STATUS_PROPERTY_ID)));
        org.apache.ambari.server.actionmanager.HostRoleStatus requestStatus = null;
        if (requestStatusStr != null) {
            requestStatus = org.apache.ambari.server.actionmanager.HostRoleStatus.valueOf(requestStatusStr);
        }
        java.lang.String abortReason = org.apache.commons.text.StringEscapeUtils.escapeHtml4(((java.lang.String) (propertyMap.get(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_ABORT_REASON_PROPERTY_ID))));
        java.lang.String removePendingHostRequests = ((java.lang.String) (propertyMap.get(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_REMOVE_PENDING_HOST_REQUESTS_ID)));
        org.apache.ambari.server.controller.RequestRequest requestRequest = new org.apache.ambari.server.controller.RequestRequest(clusterNameStr, requestId);
        requestRequest.setStatus(requestStatus);
        requestRequest.setAbortReason(abortReason);
        if (removePendingHostRequests != null) {
            requestRequest.setRemovePendingHostRequests(java.lang.Boolean.valueOf(removePendingHostRequests));
        }
        return requestRequest;
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus deleteResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        throw new java.lang.UnsupportedOperationException("Not currently supported.");
    }

    @java.lang.Override
    protected java.util.Set<java.lang.String> getPKPropertyIds() {
        return org.apache.ambari.server.controller.internal.RequestResourceProvider.PK_PROPERTY_IDS;
    }

    @java.lang.SuppressWarnings("unchecked")
    private org.apache.ambari.server.controller.ExecuteActionRequest getActionRequest(org.apache.ambari.server.controller.spi.Request request) throws java.lang.UnsupportedOperationException, org.apache.ambari.server.controller.spi.SystemException {
        java.util.Map<java.lang.String, java.lang.String> requestInfoProperties = request.getRequestInfoProperties();
        java.util.Map<java.lang.String, java.lang.Object> propertyMap = request.getProperties().iterator().next();
        java.lang.Boolean isCommand = requestInfoProperties.containsKey(org.apache.ambari.server.controller.internal.RequestResourceProvider.COMMAND_ID);
        java.lang.String commandName = null;
        java.lang.String actionName = null;
        if (isCommand) {
            if (requestInfoProperties.containsKey(org.apache.ambari.server.controller.internal.RequestResourceProvider.ACTION_ID)) {
                throw new java.lang.UnsupportedOperationException("Both command and action cannot be specified.");
            }
            commandName = requestInfoProperties.get(org.apache.ambari.server.controller.internal.RequestResourceProvider.COMMAND_ID);
        } else {
            if (!requestInfoProperties.containsKey(org.apache.ambari.server.controller.internal.RequestResourceProvider.ACTION_ID)) {
                throw new java.lang.UnsupportedOperationException("Either command or action must be specified.");
            }
            actionName = requestInfoProperties.get(org.apache.ambari.server.controller.internal.RequestResourceProvider.ACTION_ID);
        }
        java.util.List<org.apache.ambari.server.controller.internal.RequestResourceFilter> resourceFilterList = null;
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> resourceFilters;
        java.util.Map<java.lang.String, java.lang.String> params = new java.util.HashMap<>();
        java.lang.Object resourceFilterObj = propertyMap.get(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_RESOURCE_FILTER_ID);
        if ((resourceFilterObj != null) && (resourceFilterObj instanceof java.util.HashSet)) {
            resourceFilters = ((java.util.HashSet<java.util.Map<java.lang.String, java.lang.Object>>) (resourceFilterObj));
            resourceFilterList = new java.util.ArrayList<>();
            for (java.util.Map<java.lang.String, java.lang.Object> resourceMap : resourceFilters) {
                params.put(org.apache.ambari.server.controller.internal.RequestResourceProvider.HAS_RESOURCE_FILTERS, "true");
                resourceFilterList.addAll(parseRequestResourceFilter(resourceMap, ((java.lang.String) (propertyMap.get(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_CLUSTER_NAME_PROPERTY_ID)))));
            }
            org.apache.ambari.server.controller.internal.RequestResourceProvider.LOG.debug("RequestResourceFilters : {}", resourceFilters);
        }
        org.apache.ambari.server.controller.internal.RequestOperationLevel operationLevel = null;
        if (requestInfoProperties.containsKey(org.apache.ambari.server.controller.internal.RequestOperationLevel.OPERATION_LEVEL_ID)) {
            operationLevel = new org.apache.ambari.server.controller.internal.RequestOperationLevel(requestInfoProperties);
        }
        java.lang.String keyPrefix = org.apache.ambari.server.controller.internal.RequestResourceProvider.INPUTS_ID + "/";
        for (java.lang.String key : requestInfoProperties.keySet()) {
            if (key.startsWith(keyPrefix)) {
                params.put(key.substring(keyPrefix.length()), requestInfoProperties.get(key));
            }
        }
        boolean exclusive = false;
        if (requestInfoProperties.containsKey(org.apache.ambari.server.controller.internal.RequestResourceProvider.EXCLUSIVE_ID)) {
            exclusive = java.lang.Boolean.valueOf(requestInfoProperties.get(org.apache.ambari.server.controller.internal.RequestResourceProvider.EXCLUSIVE_ID).trim());
        }
        return new org.apache.ambari.server.controller.ExecuteActionRequest(((java.lang.String) (propertyMap.get(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_CLUSTER_NAME_PROPERTY_ID))), commandName, actionName, resourceFilterList, operationLevel, params, exclusive);
    }

    private java.util.List<org.apache.ambari.server.controller.internal.RequestResourceFilter> parseRequestResourceFilter(java.util.Map<java.lang.String, java.lang.Object> resourceMap, java.lang.String clusterName) throws org.apache.ambari.server.controller.spi.SystemException {
        java.util.List<org.apache.ambari.server.controller.internal.RequestResourceFilter> resourceFilterList = new java.util.ArrayList<>();
        java.lang.String serviceName = ((java.lang.String) (resourceMap.get(org.apache.ambari.server.controller.internal.RequestResourceProvider.SERVICE_ID)));
        java.lang.String componentName = ((java.lang.String) (resourceMap.get(org.apache.ambari.server.controller.internal.RequestResourceProvider.COMPONENT_ID)));
        java.lang.String hostsPredicate = ((java.lang.String) (resourceMap.get(org.apache.ambari.server.controller.internal.RequestResourceProvider.HOSTS_PREDICATE)));
        java.lang.Object hostListStr = resourceMap.get(org.apache.ambari.server.controller.internal.RequestResourceProvider.HOSTS_ID);
        java.util.List<java.lang.String> hostList = java.util.Collections.emptyList();
        if (hostListStr != null) {
            hostList = new java.util.ArrayList<>();
            for (java.lang.String hostName : ((java.lang.String) (hostListStr)).split(",")) {
                hostList.add(hostName.trim());
            }
            resourceFilterList.add(new org.apache.ambari.server.controller.internal.RequestResourceFilter(serviceName, componentName, hostList));
        } else if (hostsPredicate != null) {
            org.apache.ambari.server.controller.spi.Predicate filterPredicate;
            try {
                filterPredicate = predicateCompiler.compile(hostsPredicate);
            } catch (org.apache.ambari.server.api.predicate.InvalidQueryException e) {
                java.lang.String msg = "Invalid predicate expression provided: " + hostsPredicate;
                org.apache.ambari.server.controller.internal.RequestResourceProvider.LOG.warn(msg, e);
                throw new org.apache.ambari.server.controller.spi.SystemException(msg, e);
            }
            java.util.Set<java.lang.String> propertyIds = new java.util.HashSet<>();
            propertyIds.add(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.CLUSTER_NAME);
            propertyIds.add(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.SERVICE_NAME);
            propertyIds.add(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.COMPONENT_NAME);
            org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(propertyIds);
            try {
                org.apache.ambari.server.controller.spi.ClusterController clusterController = org.apache.ambari.server.controller.utilities.ClusterControllerHelper.getClusterController();
                org.apache.ambari.server.controller.spi.QueryResponse queryResponse = clusterController.getResources(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent, request, filterPredicate);
                java.lang.Iterable<org.apache.ambari.server.controller.spi.Resource> resourceIterable = clusterController.getIterable(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent, queryResponse, request, filterPredicate, null, null);
                java.util.Map<org.apache.ambari.server.controller.internal.RequestResourceProvider.ServiceComponentTuple, java.util.List<java.lang.String>> tupleListMap = new java.util.HashMap<>();
                for (org.apache.ambari.server.controller.spi.Resource resource : resourceIterable) {
                    java.lang.String hostnameStr = ((java.lang.String) (resource.getPropertyValue(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.HOST_NAME)));
                    if (hostnameStr != null) {
                        java.lang.String computedServiceName = ((java.lang.String) (resource.getPropertyValue(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.SERVICE_NAME)));
                        java.lang.String computedComponentName = ((java.lang.String) (resource.getPropertyValue(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.COMPONENT_NAME)));
                        org.apache.ambari.server.controller.internal.RequestResourceProvider.ServiceComponentTuple tuple = new org.apache.ambari.server.controller.internal.RequestResourceProvider.ServiceComponentTuple(computedServiceName, computedComponentName);
                        if (!tupleListMap.containsKey(tuple)) {
                            hostList = new java.util.ArrayList<>();
                            hostList.add(hostnameStr);
                            tupleListMap.put(tuple, hostList);
                        } else {
                            tupleListMap.get(tuple).add(hostnameStr);
                        }
                    }
                }
                if (!tupleListMap.isEmpty()) {
                    for (java.util.Map.Entry<org.apache.ambari.server.controller.internal.RequestResourceProvider.ServiceComponentTuple, java.util.List<java.lang.String>> entry : tupleListMap.entrySet()) {
                        resourceFilterList.add(new org.apache.ambari.server.controller.internal.RequestResourceFilter(entry.getKey().getServiceName(), entry.getKey().getComponentName(), entry.getValue()));
                    }
                }
            } catch (java.lang.Exception e) {
                org.apache.ambari.server.controller.internal.RequestResourceProvider.LOG.warn(((((("Exception finding requested resources with serviceName = " + serviceName) + ", componentName = ") + componentName) + ", hostPredicate") + " = ") + hostsPredicate, e);
            }
        } else {
            resourceFilterList.add(new org.apache.ambari.server.controller.internal.RequestResourceFilter(serviceName, componentName, hostList));
        }
        return resourceFilterList;
    }

    class ServiceComponentTuple {
        final java.lang.String serviceName;

        final java.lang.String componentName;

        ServiceComponentTuple(java.lang.String serviceName, java.lang.String componentName) {
            this.serviceName = serviceName;
            this.componentName = componentName;
        }

        public java.lang.String getServiceName() {
            return serviceName;
        }

        public java.lang.String getComponentName() {
            return componentName;
        }

        @java.lang.Override
        public boolean equals(java.lang.Object o) {
            if (this == o) {
                return true;
            }
            if ((o == null) || (getClass() != o.getClass())) {
                return false;
            }
            org.apache.ambari.server.controller.internal.RequestResourceProvider.ServiceComponentTuple that = ((org.apache.ambari.server.controller.internal.RequestResourceProvider.ServiceComponentTuple) (o));
            if (serviceName != null ? !serviceName.equals(that.serviceName) : that.serviceName != null) {
                return false;
            }
            return !(componentName != null ? !componentName.equals(that.componentName) : that.componentName != null);
        }

        @java.lang.Override
        public int hashCode() {
            int result = (serviceName != null) ? serviceName.hashCode() : 0;
            result = (31 * result) + (componentName != null ? componentName.hashCode() : 0);
            return result;
        }
    }

    private java.util.Set<org.apache.ambari.server.controller.spi.Resource> getRequestResources(java.lang.String clusterName, java.lang.Long requestId, java.lang.String requestStatus, java.lang.Integer maxResults, java.lang.Boolean ascOrder, java.util.Set<java.lang.String> requestedPropertyIds) throws org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> response = new java.util.HashSet<>();
        org.apache.ambari.server.actionmanager.ActionManager actionManager = getManagementController().getActionManager();
        java.lang.Long clusterId = null;
        if (clusterName != null) {
            org.apache.ambari.server.state.Clusters clusters = getManagementController().getClusters();
            try {
                org.apache.ambari.server.state.Cluster cluster = clusters.getCluster(clusterName);
                clusterId = cluster.getClusterId();
            } catch (org.apache.ambari.server.AmbariException e) {
                throw new org.apache.ambari.server.controller.spi.NoSuchParentResourceException(e.getMessage(), e);
            }
        }
        java.util.List<java.lang.Long> requestIds = java.util.Collections.emptyList();
        if (requestId == null) {
            org.apache.ambari.server.actionmanager.RequestStatus status = null;
            if (requestStatus != null) {
                status = org.apache.ambari.server.actionmanager.RequestStatus.valueOf(requestStatus);
            }
            if (org.apache.ambari.server.controller.internal.RequestResourceProvider.LOG.isDebugEnabled()) {
                org.apache.ambari.server.controller.internal.RequestResourceProvider.LOG.debug("Received a Get Request Status request, requestId=null, requestStatus={}", status);
            }
            maxResults = (maxResults != null) ? maxResults : org.apache.ambari.server.api.services.BaseRequest.DEFAULT_PAGE_SIZE;
            ascOrder = (ascOrder != null) ? ascOrder : false;
            if (null == status) {
                if (null != clusterId) {
                    requestIds = org.apache.ambari.server.controller.internal.RequestResourceProvider.s_requestDAO.findAllRequestIds(maxResults, ascOrder, clusterId);
                } else {
                    requestIds = org.apache.ambari.server.controller.internal.RequestResourceProvider.s_requestDAO.findAllRequestIds(maxResults, ascOrder, null);
                }
            } else {
                requestIds = actionManager.getRequestsByStatus(status, maxResults, ascOrder);
            }
            org.apache.ambari.server.controller.internal.RequestResourceProvider.LOG.debug("List<Long> requestIds = actionManager.getRequestsByStatus = {}", requestIds.size());
            response.addAll(getRequestResources(clusterId, clusterName, requestIds, requestedPropertyIds));
        } else {
            java.util.Collection<org.apache.ambari.server.controller.spi.Resource> responses = getRequestResources(clusterId, clusterName, java.util.Collections.singletonList(requestId), requestedPropertyIds);
            if (responses.isEmpty()) {
                throw new org.apache.ambari.server.controller.spi.NoSuchResourceException("Request resource doesn't exist.");
            }
            response.addAll(responses);
        }
        return response;
    }

    private java.util.Collection<org.apache.ambari.server.controller.spi.Resource> getRequestResources(java.lang.Long clusterId, java.lang.String clusterName, java.util.List<java.lang.Long> requestIds, java.util.Set<java.lang.String> requestedPropertyIds) {
        java.util.Map<java.lang.Long, org.apache.ambari.server.controller.spi.Resource> resourceMap = new java.util.HashMap<>();
        java.util.List<org.apache.ambari.server.orm.entities.RequestEntity> requests = org.apache.ambari.server.controller.internal.RequestResourceProvider.s_requestDAO.findByPks(requestIds, true);
        java.util.List<org.apache.ambari.server.orm.entities.RequestEntity> topologyRequestEntities = new java.util.ArrayList<>();
        java.util.Collection<? extends org.apache.ambari.server.actionmanager.Request> topologyRequests = org.apache.ambari.server.controller.internal.RequestResourceProvider.topologyManager.getRequests(requestIds);
        for (org.apache.ambari.server.actionmanager.Request request : topologyRequests) {
            topologyRequestEntities.add(request.constructNewPersistenceEntity());
        }
        if (requests.isEmpty()) {
            requests = new java.util.ArrayList<>();
        }
        requests.addAll(topologyRequestEntities);
        for (org.apache.ambari.server.orm.entities.RequestEntity re : requests) {
            if (((null == clusterId) && ((null == re.getClusterId()) || ((-1L) == re.getClusterId()))) || (((null != clusterId) && (null != re.getRequestId())) && re.getClusterId().equals(clusterId))) {
                org.apache.ambari.server.controller.spi.Resource r = getRequestResource(re, clusterName, requestedPropertyIds);
                resourceMap.put(re.getRequestId(), r);
            }
        }
        return resourceMap.values();
    }

    private org.apache.ambari.server.controller.spi.Resource getRequestResource(org.apache.ambari.server.orm.entities.RequestEntity entity, java.lang.String clusterName, java.util.Set<java.lang.String> requestedPropertyIds) {
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Request);
        if (null != clusterName) {
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_CLUSTER_NAME_PROPERTY_ID, clusterName, requestedPropertyIds);
        } else if ((null != entity.getClusterId()) && ((-1L) != entity.getClusterId())) {
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_CLUSTER_ID_PROPERTY_ID, entity.getClusterId(), requestedPropertyIds);
        }
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_ID_PROPERTY_ID, entity.getRequestId(), requestedPropertyIds);
        java.lang.String requestContext = entity.getRequestContext();
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_CONTEXT_ID, requestContext, requestedPropertyIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_TYPE_ID, entity.getRequestType(), requestedPropertyIds);
        if (org.apache.ambari.server.controller.internal.BaseProvider.isPropertyRequested(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_INPUTS_ID, requestedPropertyIds)) {
            java.lang.String value = entity.getInputs();
            if (!org.apache.commons.lang.StringUtils.isBlank(value)) {
                value = org.apache.ambari.server.utils.SecretReference.maskPasswordInPropertyMap(value);
            }
            resource.setProperty(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_INPUTS_ID, value);
        }
        if (org.apache.ambari.server.controller.internal.BaseProvider.isPropertyRequested(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_CLUSTER_HOST_INFO_ID, requestedPropertyIds)) {
            resource.setProperty(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_CLUSTER_HOST_INFO_ID, entity.getClusterHostInfo());
        }
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_RESOURCE_FILTER_ID, org.apache.ambari.server.actionmanager.Request.filtersFromEntity(entity), requestedPropertyIds);
        org.apache.ambari.server.controller.internal.RequestOperationLevel operationLevel = org.apache.ambari.server.actionmanager.Request.operationLevelFromEntity(entity);
        java.lang.String opLevelStr = null;
        if (operationLevel != null) {
            opLevelStr = org.apache.ambari.server.controller.internal.RequestOperationLevel.getExternalLevelName(operationLevel.getLevel().toString());
        }
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_OPERATION_LEVEL_ID, opLevelStr, requestedPropertyIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_CREATE_TIME_ID, entity.getCreateTime(), requestedPropertyIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_START_TIME_ID, entity.getStartTime(), requestedPropertyIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_END_TIME_ID, entity.getEndTime(), requestedPropertyIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_EXCLUSIVE_ID, entity.isExclusive(), requestedPropertyIds);
        if (entity.getRequestScheduleId() != null) {
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_SOURCE_SCHEDULE_ID, entity.getRequestScheduleId(), requestedPropertyIds);
        } else {
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_SOURCE_SCHEDULE, null, requestedPropertyIds);
        }
        java.util.Map<java.lang.Long, org.apache.ambari.server.orm.dao.HostRoleCommandStatusSummaryDTO> summary = org.apache.ambari.server.controller.internal.RequestResourceProvider.s_hostRoleCommandDAO.findAggregateCounts(entity.getRequestId());
        summary.putAll(org.apache.ambari.server.controller.internal.RequestResourceProvider.topologyManager.getStageSummaries(entity.getRequestId()));
        final org.apache.ambari.server.controller.internal.CalculatedStatus status;
        org.apache.ambari.server.topology.LogicalRequest logicalRequest = org.apache.ambari.server.controller.internal.RequestResourceProvider.topologyManager.getRequest(entity.getRequestId());
        if (summary.isEmpty() && (null != logicalRequest)) {
            status = logicalRequest.calculateStatus();
            if (status == org.apache.ambari.server.controller.internal.CalculatedStatus.ABORTED) {
                com.google.common.base.Optional<java.lang.String> failureReason = logicalRequest.getFailureReason();
                if (failureReason.isPresent()) {
                    requestContext += "\nFAILED: " + failureReason.get();
                    org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_CONTEXT_ID, requestContext, requestedPropertyIds);
                }
            }
        } else {
            status = org.apache.ambari.server.controller.internal.CalculatedStatus.statusFromStageSummary(summary, summary.keySet());
        }
        if (null != logicalRequest) {
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_PENDING_HOST_REQUEST_COUNT_ID, logicalRequest.getPendingHostRequestCount(), requestedPropertyIds);
        }
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_STATUS_PROPERTY_ID, status.getStatus().toString(), requestedPropertyIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_PROGRESS_PERCENT_ID, status.getPercent(), requestedPropertyIds);
        int taskCount = 0;
        for (org.apache.ambari.server.orm.dao.HostRoleCommandStatusSummaryDTO dto : summary.values()) {
            taskCount += dto.getTaskTotal();
        }
        java.util.Map<org.apache.ambari.server.actionmanager.HostRoleStatus, java.lang.Integer> hostRoleStatusCounters = org.apache.ambari.server.controller.internal.CalculatedStatus.calculateTaskStatusCounts(summary, summary.keySet());
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_TASK_CNT_ID, taskCount, requestedPropertyIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_FAILED_TASK_CNT_ID, hostRoleStatusCounters.get(org.apache.ambari.server.actionmanager.HostRoleStatus.FAILED), requestedPropertyIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_ABORTED_TASK_CNT_ID, hostRoleStatusCounters.get(org.apache.ambari.server.actionmanager.HostRoleStatus.ABORTED), requestedPropertyIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_TIMED_OUT_TASK_CNT_ID, hostRoleStatusCounters.get(org.apache.ambari.server.actionmanager.HostRoleStatus.TIMEDOUT), requestedPropertyIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_QUEUED_TASK_CNT_ID, hostRoleStatusCounters.get(org.apache.ambari.server.actionmanager.HostRoleStatus.QUEUED), requestedPropertyIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_COMPLETED_TASK_CNT_ID, hostRoleStatusCounters.get(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED), requestedPropertyIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_USER_NAME, entity.getUserName(), requestedPropertyIds);
        return resource;
    }
}