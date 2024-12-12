package org.apache.ambari.server.controller.internal;
public class RequestScheduleResourceProvider extends org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.class);

    public static final java.lang.String REQUEST_SCHEDULE = "RequestSchedule";

    public static final java.lang.String BATCH_SETTINGS = "batch_settings";

    public static final java.lang.String BATCH_REQUESTS = "batch_requests";

    public static final java.lang.String ID_PROPERTY_ID = "id";

    public static final java.lang.String CLUSTER_NAME_PROPERTY_ID = "cluster_name";

    public static final java.lang.String DESCRIPTION_PROPERTY_ID = "description";

    public static final java.lang.String STATUS_PROPERTY_ID = "status";

    public static final java.lang.String LAST_EXECUTION_STATUS_PROPERTY_ID = "last_execution_status";

    public static final java.lang.String BATCH_PROPERTY_ID = "batch";

    public static final java.lang.String SCHEDULE_PROPERTY_ID = "schedule";

    public static final java.lang.String CREATE_USER_PROPERTY_ID = "create_user";

    public static final java.lang.String AUTHENTICATED_USER_PROPERTY_ID = "authenticated_user";

    public static final java.lang.String UPDATE_USER_PROPERTY_ID = "update_user";

    public static final java.lang.String CREATE_TIME_PROPERTY_ID = "create_time";

    public static final java.lang.String UPDATE_TIME_PROPERTY_ID = "update_time";

    public static final java.lang.String BATCH_SEPARATION_IN_SECONDS_PROPERTY_ID = "batch_separation_in_seconds";

    public static final java.lang.String TASK_FAILURE_TOLERANCE_PROPERTY_ID = "task_failure_tolerance";

    public static final java.lang.String TASK_FAILURE_TOLERANCE_PER_BATCH_PROPERTY_ID = "task_failure_tolerance_per_batch";

    public static final java.lang.String TASK_FAILURE_TOLERANCE_LIMIT_PROPERTY_ID = "task_failure_tolerance_limit";

    public static final java.lang.String REQUESTS_PROPERTY_ID = "requests";

    public static final java.lang.String PAUSE_AFTER_FIRST_BATCH_PROPERTY_ID = "pause_after_first_batch";

    public static final java.lang.String TYPE_PROPERTY_ID = "type";

    public static final java.lang.String URI_PROPERTY_ID = "uri";

    public static final java.lang.String ORDER_ID_PROPERTY_ID = "order_id";

    public static final java.lang.String REQUEST_TYPE_PROPERTY_ID = "request_type";

    public static final java.lang.String REQUEST_URI_PROPERTY_ID = "request_uri";

    public static final java.lang.String REQUEST_BODY_PROPERTY_ID = "request_body";

    public static final java.lang.String REQUEST_STATUS_PROPERTY_ID = "request_status";

    public static final java.lang.String RETURN_CODE_PROPERTY_ID = "return_code";

    public static final java.lang.String RESPONSE_MESSAGE_PROPERTY_ID = "response_message";

    public static final java.lang.String DAYS_OF_MONTH_PROPERTY_ID = "days_of_month";

    public static final java.lang.String MINUTES_PROPERTY_ID = "minutes";

    public static final java.lang.String HOURS_PROPERTY_ID = "hours";

    public static final java.lang.String YEAR_PROPERTY_ID = "year";

    public static final java.lang.String DAY_OF_WEEK_PROPERTY_ID = "day_of_week";

    public static final java.lang.String MONTH_PROPERTY_ID = "month";

    public static final java.lang.String START_TIME_PROPERTY_ID = "startTime";

    public static final java.lang.String START_TIME_SNAKE_CASE_PROPERTY_ID = "start_time";

    public static final java.lang.String END_TIME_PROPERTY_ID = "endTime";

    public static final java.lang.String END_TIME_SNAKE_CASE_PROPERTY_ID = "end_time";

    public static final java.lang.String ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId(org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.REQUEST_SCHEDULE, org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.ID_PROPERTY_ID);

    public static final java.lang.String CLUSTER_NAME = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId(org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.REQUEST_SCHEDULE, org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.CLUSTER_NAME_PROPERTY_ID);

    public static final java.lang.String DESCRIPTION = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId(org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.REQUEST_SCHEDULE, org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.DESCRIPTION_PROPERTY_ID);

    public static final java.lang.String STATUS = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId(org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.REQUEST_SCHEDULE, org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.STATUS_PROPERTY_ID);

    public static final java.lang.String LAST_EXECUTION_STATUS = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId(org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.REQUEST_SCHEDULE, org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.LAST_EXECUTION_STATUS_PROPERTY_ID);

    public static final java.lang.String BATCH = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId(org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.REQUEST_SCHEDULE, org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.BATCH_PROPERTY_ID);

    public static final java.lang.String SCHEDULE = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId(org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.REQUEST_SCHEDULE, org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.SCHEDULE_PROPERTY_ID);

    public static final java.lang.String CREATE_USER = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId(org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.REQUEST_SCHEDULE, org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.CREATE_USER_PROPERTY_ID);

    public static final java.lang.String AUTHENTICATED_USER = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId(org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.REQUEST_SCHEDULE, org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.AUTHENTICATED_USER_PROPERTY_ID);

    public static final java.lang.String UPDATE_USER = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId(org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.REQUEST_SCHEDULE, org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.UPDATE_USER_PROPERTY_ID);

    public static final java.lang.String CREATE_TIME = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId(org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.REQUEST_SCHEDULE, org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.CREATE_TIME_PROPERTY_ID);

    public static final java.lang.String UPDATE_TIME = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId(org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.REQUEST_SCHEDULE, org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.UPDATE_TIME_PROPERTY_ID);

    public static final java.lang.String BATCH_SEPARATION_IN_SECONDS = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId(org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.BATCH_SETTINGS, org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.BATCH_SEPARATION_IN_SECONDS_PROPERTY_ID);

    public static final java.lang.String TASK_FAILURE_TOLERANCE = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId(org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.BATCH_SETTINGS, org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.TASK_FAILURE_TOLERANCE_PROPERTY_ID);

    public static final java.lang.String TASK_FAILURE_TOLERANCE_PER_BATCH = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId(org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.BATCH_SETTINGS, org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.TASK_FAILURE_TOLERANCE_PER_BATCH_PROPERTY_ID);

    public static final java.lang.String REQUESTS = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId(null, org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.REQUESTS_PROPERTY_ID);

    public static final java.lang.String PAUSE_AFTER_FIRST_BATCH = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId(org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.BATCH_SETTINGS, org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.PAUSE_AFTER_FIRST_BATCH_PROPERTY_ID);

    public static final java.lang.String TYPE = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId(null, org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.TYPE_PROPERTY_ID);

    public static final java.lang.String URI = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId(null, org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.URI_PROPERTY_ID);

    public static final java.lang.String ORDER_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId(null, org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.ORDER_ID_PROPERTY_ID);

    public static final java.lang.String BODY = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId(null, org.apache.ambari.server.api.services.parsers.RequestBodyParser.REQUEST_BLOB_TITLE);

    public static final java.lang.String DAYS_OF_MONTH = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId(org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.SCHEDULE, org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.DAYS_OF_MONTH_PROPERTY_ID);

    public static final java.lang.String MINUTES = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId(org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.SCHEDULE, org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.MINUTES_PROPERTY_ID);

    public static final java.lang.String HOURS = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId(org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.SCHEDULE, org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.HOURS_PROPERTY_ID);

    public static final java.lang.String YEAR = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId(org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.SCHEDULE, org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.YEAR_PROPERTY_ID);

    public static final java.lang.String DAY_OF_WEEK = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId(org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.SCHEDULE, org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.DAY_OF_WEEK_PROPERTY_ID);

    public static final java.lang.String MONTH = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId(org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.SCHEDULE, org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.MONTH_PROPERTY_ID);

    public static final java.lang.String START_TIME = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId(org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.SCHEDULE, org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.START_TIME_PROPERTY_ID);

    public static final java.lang.String END_TIME = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId(org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.SCHEDULE, org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.END_TIME_PROPERTY_ID);

    private static final java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> keyPropertyIds = com.google.common.collect.ImmutableMap.<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String>builder().put(org.apache.ambari.server.controller.spi.Resource.Type.Cluster, org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.CLUSTER_NAME).put(org.apache.ambari.server.controller.spi.Resource.Type.RequestSchedule, org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.ID).build();

    private static final java.util.Set<java.lang.String> propertyIds = com.google.common.collect.Sets.newHashSet(org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.ID, org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.CLUSTER_NAME, org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.DESCRIPTION, org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.STATUS, org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.LAST_EXECUTION_STATUS, org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.BATCH, org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.SCHEDULE, org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.CREATE_USER, org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.AUTHENTICATED_USER, org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.UPDATE_USER, org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.CREATE_TIME, org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.UPDATE_TIME, org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.BATCH_SEPARATION_IN_SECONDS, org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.TASK_FAILURE_TOLERANCE, org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.TASK_FAILURE_TOLERANCE_PER_BATCH, org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.PAUSE_AFTER_FIRST_BATCH, org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.REQUESTS, org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.TYPE, org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.URI, org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.ORDER_ID, org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.BODY, org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.DAYS_OF_MONTH, org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.MINUTES, org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.HOURS, org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.YEAR, org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.DAY_OF_WEEK, org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.MONTH, org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.START_TIME, org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.END_TIME);

    protected RequestScheduleResourceProvider(org.apache.ambari.server.controller.AmbariManagementController managementController) {
        super(org.apache.ambari.server.controller.spi.Resource.Type.RequestSchedule, org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.propertyIds, org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.keyPropertyIds, managementController);
    }

    @java.lang.Override
    protected java.util.Set<java.lang.String> getPKPropertyIds() {
        return new java.util.HashSet<>(org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.keyPropertyIds.values());
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus createResources(org.apache.ambari.server.controller.spi.Request request) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.ResourceAlreadyExistsException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        final java.util.Set<org.apache.ambari.server.controller.RequestScheduleRequest> requests = new java.util.HashSet<>();
        for (java.util.Map<java.lang.String, java.lang.Object> propertyMap : request.getProperties()) {
            requests.add(getRequestScheduleRequest(propertyMap));
        }
        java.util.Set<org.apache.ambari.server.controller.RequestScheduleResponse> responses = createResources(new org.apache.ambari.server.controller.internal.AbstractResourceProvider.Command<java.util.Set<org.apache.ambari.server.controller.RequestScheduleResponse>>() {
            @java.lang.Override
            public java.util.Set<org.apache.ambari.server.controller.RequestScheduleResponse> invoke() throws org.apache.ambari.server.AmbariException {
                return createRequestSchedules(requests);
            }
        });
        notifyCreate(org.apache.ambari.server.controller.spi.Resource.Type.RequestSchedule, request);
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> associatedResources = new java.util.HashSet<>();
        for (org.apache.ambari.server.controller.RequestScheduleResponse response : responses) {
            org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.RequestSchedule);
            resource.setProperty(org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.ID, response.getId());
            associatedResources.add(resource);
        }
        return getRequestStatus(null, associatedResources);
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.controller.spi.Resource> getResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        final java.util.Set<org.apache.ambari.server.controller.RequestScheduleRequest> requests = new java.util.HashSet<>();
        for (java.util.Map<java.lang.String, java.lang.Object> propertyMap : getPropertyMaps(predicate)) {
            requests.add(getRequestScheduleRequest(propertyMap));
        }
        java.util.Set<org.apache.ambari.server.controller.RequestScheduleResponse> responses = getResources(new org.apache.ambari.server.controller.internal.AbstractResourceProvider.Command<java.util.Set<org.apache.ambari.server.controller.RequestScheduleResponse>>() {
            @java.lang.Override
            public java.util.Set<org.apache.ambari.server.controller.RequestScheduleResponse> invoke() throws org.apache.ambari.server.AmbariException {
                return getRequestSchedules(requests);
            }
        });
        java.util.Set<java.lang.String> requestedIds = getRequestPropertyIds(request, predicate);
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = new java.util.HashSet<>();
        for (org.apache.ambari.server.controller.RequestScheduleResponse response : responses) {
            org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.RequestSchedule);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.ID, response.getId(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.CLUSTER_NAME, response.getClusterName(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.DESCRIPTION, response.getDescription(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.STATUS, response.getStatus(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.LAST_EXECUTION_STATUS, response.getLastExecutionStatus(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.BATCH, response.getBatch(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.SCHEDULE, response.getSchedule(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.CREATE_USER, response.getCreateUser(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.AUTHENTICATED_USER, response.getAuthenticatedUserId(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.CREATE_TIME, response.getCreateTime(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.UPDATE_USER, response.getUpdateUser(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.UPDATE_TIME, response.getUpdateTime(), requestedIds);
            resources.add(resource);
        }
        return resources;
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus updateResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        final java.util.Set<org.apache.ambari.server.controller.RequestScheduleRequest> requests = new java.util.HashSet<>();
        java.util.Iterator<java.util.Map<java.lang.String, java.lang.Object>> iterator = request.getProperties().iterator();
        if (iterator.hasNext()) {
            for (java.util.Map<java.lang.String, java.lang.Object> propertyMap : getPropertyMaps(iterator.next(), predicate)) {
                requests.add(getRequestScheduleRequest(propertyMap));
            }
            modifyResources(new org.apache.ambari.server.controller.internal.AbstractResourceProvider.Command<java.lang.Void>() {
                @java.lang.Override
                public java.lang.Void invoke() throws org.apache.ambari.server.AmbariException {
                    updateRequestSchedule(requests);
                    return null;
                }
            });
        }
        notifyUpdate(org.apache.ambari.server.controller.spi.Resource.Type.RequestSchedule, request, predicate);
        return getRequestStatus(null);
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus deleteResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        for (java.util.Map<java.lang.String, java.lang.Object> propertyMap : getPropertyMaps(predicate)) {
            final org.apache.ambari.server.controller.RequestScheduleRequest requestScheduleRequest = getRequestScheduleRequest(propertyMap);
            modifyResources(new org.apache.ambari.server.controller.internal.AbstractResourceProvider.Command<java.lang.Void>() {
                @java.lang.Override
                public java.lang.Void invoke() throws org.apache.ambari.server.AmbariException {
                    deleteRequestSchedule(requestScheduleRequest);
                    return null;
                }
            });
        }
        notifyDelete(org.apache.ambari.server.controller.spi.Resource.Type.RequestSchedule, predicate);
        return getRequestStatus(null);
    }

    private synchronized void deleteRequestSchedule(org.apache.ambari.server.controller.RequestScheduleRequest request) throws org.apache.ambari.server.AmbariException {
        if (request.getId() == null) {
            throw new org.apache.ambari.server.AmbariException("Id is a required field.");
        }
        org.apache.ambari.server.state.Clusters clusters = getManagementController().getClusters();
        org.apache.ambari.server.state.Cluster cluster;
        try {
            cluster = clusters.getCluster(request.getClusterName());
        } catch (org.apache.ambari.server.ClusterNotFoundException e) {
            throw new org.apache.ambari.server.ParentObjectNotFoundException("Attempted to delete a request schedule from a cluster which doesn't " + "exist", e);
        }
        org.apache.ambari.server.state.scheduler.RequestExecution requestExecution = cluster.getAllRequestExecutions().get(request.getId());
        if (requestExecution == null) {
            throw new org.apache.ambari.server.AmbariException(((((("Request Schedule not found " + ", clusterName = ") + request.getClusterName()) + ", description = ") + request.getDescription()) + ", id = ") + request.getId());
        }
        java.lang.String username = getManagementController().getAuthName();
        org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.LOG.info(((((("Disabling Request Schedule " + ", clusterName = ") + request.getClusterName()) + ", id = ") + request.getId()) + ", user = ") + username);
        getManagementController().getExecutionScheduleManager().deleteAllJobs(requestExecution);
        requestExecution.updateStatus(org.apache.ambari.server.state.scheduler.RequestExecution.Status.DISABLED);
    }

    private synchronized void updateRequestSchedule(java.util.Set<org.apache.ambari.server.controller.RequestScheduleRequest> requests) throws org.apache.ambari.server.AmbariException {
        if (requests.isEmpty()) {
            org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.LOG.warn("Received an empty requests set");
            return;
        }
        org.apache.ambari.server.state.Clusters clusters = getManagementController().getClusters();
        for (org.apache.ambari.server.controller.RequestScheduleRequest request : requests) {
            validateRequest(request);
            org.apache.ambari.server.state.Cluster cluster;
            try {
                cluster = clusters.getCluster(request.getClusterName());
            } catch (org.apache.ambari.server.ClusterNotFoundException e) {
                throw new org.apache.ambari.server.ParentObjectNotFoundException("Attempted to add a request schedule to a cluster which doesn't " + "exist", e);
            }
            if (request.getId() == null) {
                throw new org.apache.ambari.server.AmbariException("Id is a required parameter.");
            }
            org.apache.ambari.server.state.scheduler.RequestExecution requestExecution = cluster.getAllRequestExecutions().get(request.getId());
            if (requestExecution == null) {
                throw new org.apache.ambari.server.AmbariException(((((("Request Schedule not found " + ", clusterName = ") + request.getClusterName()) + ", description = ") + request.getDescription()) + ", id = ") + request.getId());
            }
            java.lang.String username = getManagementController().getAuthName();
            java.lang.Integer userId = getManagementController().getAuthId();
            if (request.getDescription() != null) {
                requestExecution.setDescription(request.getDescription());
            }
            if (request.getSchedule() != null) {
                requestExecution.setSchedule(request.getSchedule());
            }
            if (request.getStatus() != null) {
                if (!isValidRequestScheduleStatus(request.getStatus())) {
                    throw new org.apache.ambari.server.AmbariException(((((("Request Schedule status not valid" + ", clusterName = ") + request.getClusterName()) + ", description = ") + request.getDescription()) + ", id = ") + request.getId());
                }
                requestExecution.setStatus(org.apache.ambari.server.state.scheduler.RequestExecution.Status.valueOf(request.getStatus()));
            }
            requestExecution.setUpdateUser(username);
            requestExecution.setAuthenticatedUserId(userId);
            org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.LOG.info(((((((("Persisting updated Request Schedule " + ", clusterName = ") + request.getClusterName()) + ", description = ") + request.getDescription()) + ", status = ") + request.getStatus()) + ", user = ") + username);
            requestExecution.persist();
            getManagementController().getExecutionScheduleManager().updateBatchSchedule(requestExecution);
        }
    }

    private synchronized java.util.Set<org.apache.ambari.server.controller.RequestScheduleResponse> createRequestSchedules(java.util.Set<org.apache.ambari.server.controller.RequestScheduleRequest> requests) throws org.apache.ambari.server.AmbariException {
        if (requests.isEmpty()) {
            org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.LOG.warn("Received an empty requests set");
            return null;
        }
        java.util.Set<org.apache.ambari.server.controller.RequestScheduleResponse> responses = new java.util.HashSet<>();
        org.apache.ambari.server.state.Clusters clusters = getManagementController().getClusters();
        org.apache.ambari.server.state.scheduler.RequestExecutionFactory requestExecutionFactory = getManagementController().getRequestExecutionFactory();
        for (org.apache.ambari.server.controller.RequestScheduleRequest request : requests) {
            validateRequest(request);
            org.apache.ambari.server.state.Cluster cluster;
            try {
                cluster = clusters.getCluster(request.getClusterName());
            } catch (org.apache.ambari.server.ClusterNotFoundException e) {
                throw new org.apache.ambari.server.ParentObjectNotFoundException("Attempted to add a request schedule to a cluster which doesn't " + "exist", e);
            }
            java.lang.String username = getManagementController().getAuthName();
            java.lang.Integer userId = getManagementController().getAuthId();
            org.apache.ambari.server.state.scheduler.RequestExecution requestExecution = requestExecutionFactory.createNew(cluster, request.getBatch(), request.getSchedule());
            requestExecution.setCreateUser(username);
            requestExecution.setUpdateUser(username);
            requestExecution.setAuthenticatedUserId(userId);
            requestExecution.setStatus(org.apache.ambari.server.state.scheduler.RequestExecution.Status.SCHEDULED);
            org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.LOG.info(((((("Persisting new Request Schedule " + ", clusterName = ") + request.getClusterName()) + ", description = ") + request.getDescription()) + ", user = ") + username);
            requestExecution.persist();
            cluster.addRequestExecution(requestExecution);
            getManagementController().getExecutionScheduleManager().scheduleAllBatches(requestExecution);
            org.apache.ambari.server.controller.RequestScheduleResponse response = new org.apache.ambari.server.controller.RequestScheduleResponse(requestExecution.getId(), requestExecution.getClusterName(), requestExecution.getDescription(), requestExecution.getStatus(), requestExecution.getLastExecutionStatus(), requestExecution.getBatch(), request.getSchedule(), requestExecution.getCreateUser(), requestExecution.getCreateTime(), requestExecution.getUpdateUser(), requestExecution.getUpdateTime(), requestExecution.getAuthenticatedUserId());
            responses.add(response);
        }
        return responses;
    }

    private void validateRequest(org.apache.ambari.server.controller.RequestScheduleRequest request) throws org.apache.ambari.server.AmbariException {
        if (request.getClusterName() == null) {
            throw new java.lang.IllegalArgumentException("Cluster name is required.");
        }
        org.apache.ambari.server.state.scheduler.Schedule schedule = request.getSchedule();
        if (schedule != null) {
            getManagementController().getExecutionScheduleManager().validateSchedule(schedule);
        }
        org.apache.ambari.server.state.scheduler.Batch batch = request.getBatch();
        if ((batch != null) && (!batch.getBatchRequests().isEmpty())) {
            java.util.HashSet<java.lang.Long> orderIdSet = new java.util.HashSet<>();
            for (org.apache.ambari.server.state.scheduler.BatchRequest batchRequest : batch.getBatchRequests()) {
                if (batchRequest.getOrderId() == null) {
                    throw new org.apache.ambari.server.AmbariException(("No order id provided for batch request. " + "") + batchRequest);
                }
                if (orderIdSet.contains(batchRequest.getOrderId())) {
                    throw new org.apache.ambari.server.AmbariException(("Duplicate order id provided for batch " + "request. ") + batchRequest);
                }
                orderIdSet.add(batchRequest.getOrderId());
            }
        }
    }

    private synchronized java.util.Set<org.apache.ambari.server.controller.RequestScheduleResponse> getRequestSchedules(java.util.Set<org.apache.ambari.server.controller.RequestScheduleRequest> requests) throws org.apache.ambari.server.AmbariException {
        java.util.Set<org.apache.ambari.server.controller.RequestScheduleResponse> responses = new java.util.HashSet<>();
        if (requests != null) {
            for (org.apache.ambari.server.controller.RequestScheduleRequest request : requests) {
                if (request.getClusterName() == null) {
                    org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.LOG.warn("Cluster name is a required field.");
                    continue;
                }
                org.apache.ambari.server.state.Cluster cluster = getManagementController().getClusters().getCluster(request.getClusterName());
                java.util.Map<java.lang.Long, org.apache.ambari.server.state.scheduler.RequestExecution> allRequestExecutions = cluster.getAllRequestExecutions();
                if (request.getId() != null) {
                    org.apache.ambari.server.state.scheduler.RequestExecution requestExecution = allRequestExecutions.get(request.getId());
                    if (requestExecution != null) {
                        responses.add(requestExecution.convertToResponseWithBody());
                    }
                    continue;
                }
                if (request.getStatus() != null) {
                    for (org.apache.ambari.server.state.scheduler.RequestExecution requestExecution : allRequestExecutions.values()) {
                        if (requestExecution.getStatus().equals(request.getStatus())) {
                            responses.add(requestExecution.convertToResponse());
                        }
                    }
                    continue;
                }
                for (org.apache.ambari.server.state.scheduler.RequestExecution requestExecution : allRequestExecutions.values()) {
                    responses.add(requestExecution.convertToResponse());
                }
            }
        }
        return responses;
    }

    private boolean isValidRequestScheduleStatus(java.lang.String giveStatus) {
        for (org.apache.ambari.server.state.scheduler.RequestExecution.Status status : org.apache.ambari.server.state.scheduler.RequestExecution.Status.values()) {
            if (status.name().equalsIgnoreCase(giveStatus)) {
                return true;
            }
        }
        return false;
    }

    private org.apache.ambari.server.controller.RequestScheduleRequest getRequestScheduleRequest(java.util.Map<java.lang.String, java.lang.Object> properties) {
        java.lang.Object idObj = properties.get(org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.ID);
        java.lang.Long id = null;
        if (idObj != null) {
            id = (idObj instanceof java.lang.Long) ? ((java.lang.Long) (idObj)) : java.lang.Long.parseLong(((java.lang.String) (idObj)));
        }
        org.apache.ambari.server.controller.RequestScheduleRequest requestScheduleRequest = new org.apache.ambari.server.controller.RequestScheduleRequest(id, ((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.CLUSTER_NAME))), ((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.DESCRIPTION))), ((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.STATUS))), null, null);
        org.apache.ambari.server.state.scheduler.Batch batch = new org.apache.ambari.server.state.scheduler.Batch();
        org.apache.ambari.server.state.scheduler.BatchSettings batchSettings = new org.apache.ambari.server.state.scheduler.BatchSettings();
        java.util.List<org.apache.ambari.server.state.scheduler.BatchRequest> batchRequests = new java.util.ArrayList<>();
        java.lang.Object batchObject = properties.get(org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.BATCH);
        if ((batchObject != null) && (batchObject instanceof java.util.HashSet<?>)) {
            try {
                java.util.HashSet<java.util.Map<java.lang.String, java.lang.Object>> batchMap = ((java.util.HashSet<java.util.Map<java.lang.String, java.lang.Object>>) (batchObject));
                for (java.util.Map<java.lang.String, java.lang.Object> batchEntry : batchMap) {
                    if (batchEntry != null) {
                        for (java.util.Map.Entry<java.lang.String, java.lang.Object> batchMapEntry : batchEntry.entrySet()) {
                            if (batchMapEntry.getKey().equals(org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.TASK_FAILURE_TOLERANCE)) {
                                batchSettings.setTaskFailureToleranceLimit(java.lang.Integer.valueOf(((java.lang.String) (batchMapEntry.getValue()))));
                            } else if (batchMapEntry.getKey().equals(org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.TASK_FAILURE_TOLERANCE_PER_BATCH)) {
                                batchSettings.setTaskFailureToleranceLimitPerBatch(java.lang.Integer.valueOf(((java.lang.String) (batchMapEntry.getValue()))));
                            } else if (batchMapEntry.getKey().equals(org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.BATCH_SEPARATION_IN_SECONDS)) {
                                batchSettings.setBatchSeparationInSeconds(java.lang.Integer.valueOf(((java.lang.String) (batchMapEntry.getValue()))));
                            } else if (batchMapEntry.getKey().equals(org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.PAUSE_AFTER_FIRST_BATCH)) {
                                batchSettings.setPauseAfterFirstBatch(java.lang.Boolean.valueOf(((java.lang.String) (batchMapEntry.getValue()))));
                            } else if (batchMapEntry.getKey().equals(org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.REQUESTS)) {
                                java.util.HashSet<java.util.Map<java.lang.String, java.lang.Object>> requestSet = ((java.util.HashSet<java.util.Map<java.lang.String, java.lang.Object>>) (batchMapEntry.getValue()));
                                for (java.util.Map<java.lang.String, java.lang.Object> requestEntry : requestSet) {
                                    if (requestEntry != null) {
                                        org.apache.ambari.server.state.scheduler.BatchRequest batchRequest = new org.apache.ambari.server.state.scheduler.BatchRequest();
                                        for (java.util.Map.Entry<java.lang.String, java.lang.Object> requestMapEntry : requestEntry.entrySet()) {
                                            if (requestMapEntry.getKey().equals(org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.TYPE)) {
                                                batchRequest.setType(org.apache.ambari.server.state.scheduler.BatchRequest.Type.valueOf(((java.lang.String) (requestMapEntry.getValue()))));
                                            } else if (requestMapEntry.getKey().equals(org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.URI)) {
                                                batchRequest.setUri(((java.lang.String) (requestMapEntry.getValue())));
                                            } else if (requestMapEntry.getKey().equals(org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.ORDER_ID)) {
                                                batchRequest.setOrderId(java.lang.Long.parseLong(((java.lang.String) (requestMapEntry.getValue()))));
                                            } else if (requestMapEntry.getKey().equals(org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.BODY)) {
                                                batchRequest.setBody(((java.lang.String) (requestMapEntry.getValue())));
                                            }
                                        }
                                        batchRequests.add(batchRequest);
                                    }
                                }
                            }
                        }
                    }
                }
                batch.getBatchRequests().addAll(batchRequests);
                batch.setBatchSettings(batchSettings);
            } catch (java.lang.Exception e) {
                org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.LOG.warn("Request Schedule batch json is unparseable. " + batchObject, e);
            }
        }
        requestScheduleRequest.setBatch(batch);
        org.apache.ambari.server.state.scheduler.Schedule schedule = new org.apache.ambari.server.state.scheduler.Schedule();
        for (java.util.Map.Entry<java.lang.String, java.lang.Object> propertyEntry : properties.entrySet()) {
            if (propertyEntry.getKey().equals(org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.DAY_OF_WEEK)) {
                schedule.setDayOfWeek(((java.lang.String) (propertyEntry.getValue())));
            } else if (propertyEntry.getKey().equals(org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.DAYS_OF_MONTH)) {
                schedule.setDaysOfMonth(((java.lang.String) (propertyEntry.getValue())));
            } else if (propertyEntry.getKey().equals(org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.END_TIME)) {
                schedule.setEndTime(((java.lang.String) (propertyEntry.getValue())));
            } else if (propertyEntry.getKey().equals(org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.HOURS)) {
                schedule.setHours(((java.lang.String) (propertyEntry.getValue())));
            } else if (propertyEntry.getKey().equals(org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.MINUTES)) {
                schedule.setMinutes(((java.lang.String) (propertyEntry.getValue())));
            } else if (propertyEntry.getKey().equals(org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.MONTH)) {
                schedule.setMonth(((java.lang.String) (propertyEntry.getValue())));
            } else if (propertyEntry.getKey().equals(org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.START_TIME)) {
                schedule.setStartTime(((java.lang.String) (propertyEntry.getValue())));
            } else if (propertyEntry.getKey().equals(org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.YEAR)) {
                schedule.setYear(((java.lang.String) (propertyEntry.getValue())));
            }
        }
        if (!schedule.isEmpty()) {
            requestScheduleRequest.setSchedule(schedule);
        }
        return requestScheduleRequest;
    }
}