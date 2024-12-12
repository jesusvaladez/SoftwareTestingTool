package org.apache.ambari.server.controller.internal;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
@org.apache.ambari.server.StaticallyInject
public class StageResourceProvider extends org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider implements org.apache.ambari.server.controller.spi.ExtendedResourceProvider {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.controller.internal.StageResourceProvider.class);

    @javax.inject.Inject
    private static org.apache.ambari.server.orm.dao.StageDAO dao = null;

    @javax.inject.Inject
    private static org.apache.ambari.server.orm.dao.HostRoleCommandDAO hostRoleCommandDAO = null;

    @javax.inject.Inject
    private static javax.inject.Provider<org.apache.ambari.server.state.Clusters> clustersProvider = null;

    @javax.inject.Inject
    private static org.apache.ambari.server.topology.TopologyManager topologyManager;

    public static final java.lang.String STAGE_STAGE_ID = "Stage/stage_id";

    public static final java.lang.String STAGE_CLUSTER_NAME = "Stage/cluster_name";

    public static final java.lang.String STAGE_REQUEST_ID = "Stage/request_id";

    public static final java.lang.String STAGE_LOG_INFO = "Stage/log_info";

    public static final java.lang.String STAGE_CONTEXT = "Stage/context";

    public static final java.lang.String STAGE_COMMAND_PARAMS = "Stage/command_params";

    public static final java.lang.String STAGE_HOST_PARAMS = "Stage/host_params";

    public static final java.lang.String STAGE_SKIPPABLE = "Stage/skippable";

    public static final java.lang.String STAGE_PROGRESS_PERCENT = "Stage/progress_percent";

    public static final java.lang.String STAGE_STATUS = "Stage/status";

    public static final java.lang.String STAGE_DISPLAY_STATUS = "Stage/display_status";

    public static final java.lang.String STAGE_START_TIME = "Stage/start_time";

    public static final java.lang.String STAGE_END_TIME = "Stage/end_time";

    static final java.util.Set<java.lang.String> PROPERTY_IDS = com.google.common.collect.ImmutableSet.<java.lang.String>builder().add(org.apache.ambari.server.controller.internal.StageResourceProvider.STAGE_STAGE_ID).add(org.apache.ambari.server.controller.internal.StageResourceProvider.STAGE_CLUSTER_NAME).add(org.apache.ambari.server.controller.internal.StageResourceProvider.STAGE_REQUEST_ID).add(org.apache.ambari.server.controller.internal.StageResourceProvider.STAGE_LOG_INFO).add(org.apache.ambari.server.controller.internal.StageResourceProvider.STAGE_CONTEXT).add(org.apache.ambari.server.controller.internal.StageResourceProvider.STAGE_COMMAND_PARAMS).add(org.apache.ambari.server.controller.internal.StageResourceProvider.STAGE_HOST_PARAMS).add(org.apache.ambari.server.controller.internal.StageResourceProvider.STAGE_SKIPPABLE).add(org.apache.ambari.server.controller.internal.StageResourceProvider.STAGE_PROGRESS_PERCENT).add(org.apache.ambari.server.controller.internal.StageResourceProvider.STAGE_STATUS).add(org.apache.ambari.server.controller.internal.StageResourceProvider.STAGE_DISPLAY_STATUS).add(org.apache.ambari.server.controller.internal.StageResourceProvider.STAGE_START_TIME).add(org.apache.ambari.server.controller.internal.StageResourceProvider.STAGE_END_TIME).build();

    private static final java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> KEY_PROPERTY_IDS = com.google.common.collect.ImmutableMap.<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String>builder().put(org.apache.ambari.server.controller.spi.Resource.Type.Stage, org.apache.ambari.server.controller.internal.StageResourceProvider.STAGE_STAGE_ID).put(org.apache.ambari.server.controller.spi.Resource.Type.Cluster, org.apache.ambari.server.controller.internal.StageResourceProvider.STAGE_CLUSTER_NAME).put(org.apache.ambari.server.controller.spi.Resource.Type.Request, org.apache.ambari.server.controller.internal.StageResourceProvider.STAGE_REQUEST_ID).build();

    static {
    }

    static final java.util.Set<java.lang.String> PROPERTIES_TO_MASK_PASSWORD_IN = com.google.common.collect.ImmutableSet.of(org.apache.ambari.server.controller.internal.StageResourceProvider.STAGE_COMMAND_PARAMS, org.apache.ambari.server.controller.internal.StageResourceProvider.STAGE_HOST_PARAMS);

    StageResourceProvider(org.apache.ambari.server.controller.AmbariManagementController managementController) {
        super(org.apache.ambari.server.controller.spi.Resource.Type.Stage, org.apache.ambari.server.controller.internal.StageResourceProvider.PROPERTY_IDS, org.apache.ambari.server.controller.internal.StageResourceProvider.KEY_PROPERTY_IDS, managementController);
    }

    @java.lang.Override
    protected java.util.Set<java.lang.String> getPKPropertyIds() {
        return new java.util.HashSet<>(org.apache.ambari.server.controller.internal.StageResourceProvider.KEY_PROPERTY_IDS.values());
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus createResources(org.apache.ambari.server.controller.spi.Request request) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.ResourceAlreadyExistsException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus updateResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        java.util.Iterator<java.util.Map<java.lang.String, java.lang.Object>> iterator = request.getProperties().iterator();
        if (iterator.hasNext()) {
            java.util.Map<java.lang.String, java.lang.Object> updateProperties = iterator.next();
            java.util.List<org.apache.ambari.server.orm.entities.StageEntity> entities = org.apache.ambari.server.controller.internal.StageResourceProvider.dao.findAll(request, predicate);
            for (org.apache.ambari.server.orm.entities.StageEntity entity : entities) {
                java.lang.String stageStatus = ((java.lang.String) (updateProperties.get(org.apache.ambari.server.controller.internal.StageResourceProvider.STAGE_STATUS)));
                if (stageStatus != null) {
                    org.apache.ambari.server.actionmanager.HostRoleStatus desiredStatus = org.apache.ambari.server.actionmanager.HostRoleStatus.valueOf(stageStatus);
                    org.apache.ambari.server.controller.internal.StageResourceProvider.dao.updateStageStatus(entity, desiredStatus, getManagementController().getActionManager());
                }
            }
        }
        notifyUpdate(org.apache.ambari.server.controller.spi.Resource.Type.Stage, request, predicate);
        return getRequestStatus(null);
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus deleteResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.controller.spi.Resource> getResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> results = new java.util.LinkedHashSet<>();
        java.util.Set<java.lang.String> propertyIds = getRequestPropertyIds(request, predicate);
        java.util.Map<java.lang.Long, java.util.Map<java.lang.Long, org.apache.ambari.server.orm.dao.HostRoleCommandStatusSummaryDTO>> cache = new java.util.HashMap<>();
        java.util.List<org.apache.ambari.server.orm.entities.StageEntity> entities = org.apache.ambari.server.controller.internal.StageResourceProvider.dao.findAll(request, predicate);
        for (org.apache.ambari.server.orm.entities.StageEntity entity : entities) {
            results.add(org.apache.ambari.server.controller.internal.StageResourceProvider.toResource(cache, entity, propertyIds));
        }
        cache.clear();
        java.util.Map<java.lang.String, java.lang.Object> map = org.apache.ambari.server.controller.utilities.PredicateHelper.getProperties(predicate);
        if (map.containsKey(org.apache.ambari.server.controller.internal.StageResourceProvider.STAGE_REQUEST_ID)) {
            java.lang.Long requestId = org.apache.commons.lang.math.NumberUtils.toLong(map.get(org.apache.ambari.server.controller.internal.StageResourceProvider.STAGE_REQUEST_ID).toString());
            org.apache.ambari.server.topology.LogicalRequest lr = org.apache.ambari.server.controller.internal.StageResourceProvider.topologyManager.getRequest(requestId);
            if (null != lr) {
                java.util.Collection<org.apache.ambari.server.orm.entities.StageEntity> topologyManagerStages = lr.getStageEntities();
                java.util.Map<java.lang.Long, org.apache.ambari.server.orm.dao.HostRoleCommandStatusSummaryDTO> summary = org.apache.ambari.server.controller.internal.StageResourceProvider.topologyManager.getStageSummaries(requestId);
                cache.put(requestId, summary);
                for (org.apache.ambari.server.orm.entities.StageEntity entity : topologyManagerStages) {
                    org.apache.ambari.server.controller.spi.Resource stageResource = org.apache.ambari.server.controller.internal.StageResourceProvider.toResource(cache, entity, propertyIds);
                    if (predicate.evaluate(stageResource)) {
                        results.add(stageResource);
                    }
                }
            }
        } else {
            java.util.Collection<org.apache.ambari.server.orm.entities.StageEntity> topologyManagerStages = org.apache.ambari.server.controller.internal.StageResourceProvider.topologyManager.getStages();
            for (org.apache.ambari.server.orm.entities.StageEntity entity : topologyManagerStages) {
                if (!cache.containsKey(entity.getRequestId())) {
                    java.util.Map<java.lang.Long, org.apache.ambari.server.orm.dao.HostRoleCommandStatusSummaryDTO> summary = org.apache.ambari.server.controller.internal.StageResourceProvider.topologyManager.getStageSummaries(entity.getRequestId());
                    cache.put(entity.getRequestId(), summary);
                }
                org.apache.ambari.server.controller.spi.Resource stageResource = org.apache.ambari.server.controller.internal.StageResourceProvider.toResource(cache, entity, propertyIds);
                if (predicate.evaluate(stageResource)) {
                    results.add(stageResource);
                }
            }
        }
        return results;
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.QueryResponse queryForResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> results = getResources(request, predicate);
        return new org.apache.ambari.server.controller.internal.QueryResponseImpl(results, request.getSortRequest() != null, false, results.size());
    }

    static org.apache.ambari.server.controller.spi.Resource toResource(java.util.Map<java.lang.Long, java.util.Map<java.lang.Long, org.apache.ambari.server.orm.dao.HostRoleCommandStatusSummaryDTO>> cache, org.apache.ambari.server.orm.entities.StageEntity entity, java.util.Set<java.lang.String> requestedIds) {
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Stage);
        java.lang.Long clusterId = entity.getClusterId();
        if ((clusterId != null) && (!clusterId.equals(java.lang.Long.valueOf(-1L)))) {
            try {
                org.apache.ambari.server.state.Cluster cluster = org.apache.ambari.server.controller.internal.StageResourceProvider.clustersProvider.get().getClusterById(clusterId);
                org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.StageResourceProvider.STAGE_CLUSTER_NAME, cluster.getClusterName(), requestedIds);
            } catch (java.lang.Exception e) {
                org.apache.ambari.server.controller.internal.StageResourceProvider.LOG.error(("Can not get information for cluster " + clusterId) + ".", e);
            }
        }
        if (!cache.containsKey(entity.getRequestId())) {
            cache.put(entity.getRequestId(), org.apache.ambari.server.controller.internal.StageResourceProvider.hostRoleCommandDAO.findAggregateCounts(entity.getRequestId()));
        }
        java.util.Map<java.lang.Long, org.apache.ambari.server.orm.dao.HostRoleCommandStatusSummaryDTO> summary = cache.get(entity.getRequestId());
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.StageResourceProvider.STAGE_STAGE_ID, entity.getStageId(), requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.StageResourceProvider.STAGE_REQUEST_ID, entity.getRequestId(), requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.StageResourceProvider.STAGE_CONTEXT, entity.getRequestContext(), requestedIds);
        if (org.apache.ambari.server.controller.internal.BaseProvider.isPropertyRequested(org.apache.ambari.server.controller.internal.StageResourceProvider.STAGE_COMMAND_PARAMS, requestedIds)) {
            java.lang.String value = entity.getCommandParamsStage();
            if (!org.apache.commons.lang.StringUtils.isBlank(value)) {
                value = org.apache.ambari.server.utils.SecretReference.maskPasswordInPropertyMap(value);
            }
            resource.setProperty(org.apache.ambari.server.controller.internal.StageResourceProvider.STAGE_COMMAND_PARAMS, value);
        }
        if (org.apache.ambari.server.controller.internal.BaseProvider.isPropertyRequested(org.apache.ambari.server.controller.internal.StageResourceProvider.STAGE_HOST_PARAMS, requestedIds)) {
            java.lang.String value = entity.getHostParamsStage();
            if (!org.apache.commons.lang.StringUtils.isBlank(value)) {
                value = org.apache.ambari.server.utils.SecretReference.maskPasswordInPropertyMap(value);
            }
            resource.setProperty(org.apache.ambari.server.controller.internal.StageResourceProvider.STAGE_HOST_PARAMS, value);
        }
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.StageResourceProvider.STAGE_SKIPPABLE, entity.isSkippable(), requestedIds);
        java.lang.Long startTime = java.lang.Long.MAX_VALUE;
        java.lang.Long endTime = 0L;
        if (summary.containsKey(entity.getStageId())) {
            startTime = summary.get(entity.getStageId()).getStartTime();
            endTime = summary.get(entity.getStageId()).getEndTime();
        }
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.StageResourceProvider.STAGE_START_TIME, startTime, requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.StageResourceProvider.STAGE_END_TIME, endTime, requestedIds);
        org.apache.ambari.server.controller.internal.CalculatedStatus status;
        if (summary.isEmpty()) {
            status = org.apache.ambari.server.controller.internal.CalculatedStatus.COMPLETED;
        } else {
            status = org.apache.ambari.server.controller.internal.CalculatedStatus.statusFromStageSummary(summary, java.util.Collections.singleton(entity.getStageId()));
        }
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.StageResourceProvider.STAGE_PROGRESS_PERCENT, status.getPercent(), requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.StageResourceProvider.STAGE_STATUS, status.getStatus(), requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.StageResourceProvider.STAGE_DISPLAY_STATUS, status.getDisplayStatus(), requestedIds);
        return resource;
    }
}