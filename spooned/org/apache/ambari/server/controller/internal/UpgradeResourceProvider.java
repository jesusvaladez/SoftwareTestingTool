package org.apache.ambari.server.controller.internal;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.persist.Transactional;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.annotate.JsonProperty;
@org.apache.ambari.server.StaticallyInject
public class UpgradeResourceProvider extends org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider {
    public static final java.lang.String UPGRADE_CLUSTER_NAME = "Upgrade/cluster_name";

    public static final java.lang.String UPGRADE_REPO_VERSION_ID = "Upgrade/repository_version_id";

    public static final java.lang.String UPGRADE_TYPE = "Upgrade/upgrade_type";

    public static final java.lang.String UPGRADE_PACK = "Upgrade/pack";

    public static final java.lang.String UPGRADE_ID = "Upgrade/upgrade_id";

    public static final java.lang.String UPGRADE_REQUEST_ID = "Upgrade/request_id";

    public static final java.lang.String UPGRADE_ASSOCIATED_VERSION = "Upgrade/associated_version";

    public static final java.lang.String UPGRADE_VERSIONS = "Upgrade/versions";

    public static final java.lang.String UPGRADE_DIRECTION = "Upgrade/direction";

    public static final java.lang.String UPGRADE_DOWNGRADE_ALLOWED = "Upgrade/downgrade_allowed";

    public static final java.lang.String UPGRADE_REQUEST_STATUS = "Upgrade/request_status";

    public static final java.lang.String UPGRADE_SUSPENDED = "Upgrade/suspended";

    public static final java.lang.String UPGRADE_ABORT_REASON = "Upgrade/abort_reason";

    public static final java.lang.String UPGRADE_SKIP_PREREQUISITE_CHECKS = "Upgrade/skip_prerequisite_checks";

    public static final java.lang.String UPGRADE_FAIL_ON_CHECK_WARNINGS = "Upgrade/fail_on_check_warnings";

    public static final java.lang.String UPGRADE_SKIP_FAILURES = "Upgrade/skip_failures";

    public static final java.lang.String UPGRADE_SKIP_SC_FAILURES = "Upgrade/skip_service_check_failures";

    public static final java.lang.String UPGRADE_SKIP_MANUAL_VERIFICATION = "Upgrade/skip_manual_verification";

    public static final java.lang.String UPGRADE_HOST_ORDERED_HOSTS = "Upgrade/host_order";

    public static final java.lang.String UPGRADE_REVERT_UPGRADE_ID = "Upgrade/revert_upgrade_id";

    protected static final java.lang.String EXECUTE_TASK_ROLE = "ru_execute_tasks";

    private static final java.lang.String REQUEST_CONTEXT_ID = "Upgrade/request_context";

    private static final java.lang.String REQUEST_TYPE_ID = "Upgrade/type";

    private static final java.lang.String REQUEST_CREATE_TIME_ID = "Upgrade/create_time";

    private static final java.lang.String REQUEST_START_TIME_ID = "Upgrade/start_time";

    private static final java.lang.String REQUEST_END_TIME_ID = "Upgrade/end_time";

    private static final java.lang.String REQUEST_EXCLUSIVE_ID = "Upgrade/exclusive";

    protected static final java.lang.String REQUEST_PROGRESS_PERCENT_ID = "Upgrade/progress_percent";

    private static final java.lang.String REQUEST_STATUS_PROPERTY_ID = "Upgrade/request_status";

    private static final java.util.Set<java.lang.String> PK_PROPERTY_IDS = new java.util.HashSet<>(java.util.Arrays.asList(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_REQUEST_ID, org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_CLUSTER_NAME));

    private static final java.util.Set<java.lang.String> PROPERTY_IDS = new java.util.HashSet<>();

    private static final java.lang.String DEFAULT_REASON_TEMPLATE = "Aborting upgrade %s";

    private static final java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> KEY_PROPERTY_IDS = new java.util.HashMap<>();

    @com.google.inject.Inject
    static org.apache.ambari.server.orm.dao.UpgradeDAO s_upgradeDAO;

    @com.google.inject.Inject
    private static com.google.inject.Provider<org.apache.ambari.server.api.services.AmbariMetaInfo> s_metaProvider = null;

    @com.google.inject.Inject
    private static com.google.inject.Provider<org.apache.ambari.server.actionmanager.RequestFactory> s_requestFactory;

    @com.google.inject.Inject
    private static com.google.inject.Provider<org.apache.ambari.server.actionmanager.StageFactory> s_stageFactory;

    @com.google.inject.Inject
    private static com.google.inject.Provider<org.apache.ambari.server.state.Clusters> clusters = null;

    @com.google.inject.Inject
    private static com.google.inject.Provider<org.apache.ambari.server.controller.AmbariActionExecutionHelper> s_actionExecutionHelper;

    @com.google.inject.Inject
    private static com.google.inject.Provider<org.apache.ambari.server.controller.AmbariCustomCommandExecutionHelper> s_commandExecutionHelper;

    @com.google.inject.Inject
    private static org.apache.ambari.server.orm.dao.RequestDAO s_requestDAO = null;

    @com.google.inject.Inject
    private static org.apache.ambari.server.orm.dao.HostRoleCommandDAO s_hostRoleCommandDAO = null;

    @com.google.inject.Inject
    private static org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeHelper s_upgradeHelper;

    @com.google.inject.Inject
    private static org.apache.ambari.server.configuration.Configuration s_configuration;

    @com.google.inject.Inject
    private static org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContextFactory s_upgradeContextFactory;

    @com.google.inject.Inject
    private org.apache.ambari.server.events.publishers.STOMPUpdatePublisher STOMPUpdatePublisher;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.HostRoleCommandDAO hostRoleCommandDAO;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.RequestDAO requestDAO;

    @com.google.inject.Inject
    private static com.google.inject.Provider<org.apache.ambari.server.controller.KerberosHelper> s_kerberosHelper;

    static {
        PROPERTY_IDS.add(UPGRADE_CLUSTER_NAME);
        PROPERTY_IDS.add(UPGRADE_REPO_VERSION_ID);
        PROPERTY_IDS.add(UPGRADE_TYPE);
        PROPERTY_IDS.add(UPGRADE_PACK);
        PROPERTY_IDS.add(UPGRADE_ID);
        PROPERTY_IDS.add(UPGRADE_REQUEST_ID);
        PROPERTY_IDS.add(UPGRADE_ASSOCIATED_VERSION);
        PROPERTY_IDS.add(UPGRADE_VERSIONS);
        PROPERTY_IDS.add(UPGRADE_DIRECTION);
        PROPERTY_IDS.add(UPGRADE_DOWNGRADE_ALLOWED);
        PROPERTY_IDS.add(UPGRADE_SUSPENDED);
        PROPERTY_IDS.add(UPGRADE_SKIP_FAILURES);
        PROPERTY_IDS.add(UPGRADE_SKIP_SC_FAILURES);
        PROPERTY_IDS.add(UPGRADE_SKIP_MANUAL_VERIFICATION);
        PROPERTY_IDS.add(UPGRADE_SKIP_PREREQUISITE_CHECKS);
        PROPERTY_IDS.add(UPGRADE_FAIL_ON_CHECK_WARNINGS);
        PROPERTY_IDS.add(UPGRADE_HOST_ORDERED_HOSTS);
        PROPERTY_IDS.add(UPGRADE_REVERT_UPGRADE_ID);
        PROPERTY_IDS.add(REQUEST_CONTEXT_ID);
        PROPERTY_IDS.add(REQUEST_CREATE_TIME_ID);
        PROPERTY_IDS.add(REQUEST_END_TIME_ID);
        PROPERTY_IDS.add(REQUEST_EXCLUSIVE_ID);
        PROPERTY_IDS.add(REQUEST_PROGRESS_PERCENT_ID);
        PROPERTY_IDS.add(REQUEST_START_TIME_ID);
        PROPERTY_IDS.add(REQUEST_STATUS_PROPERTY_ID);
        PROPERTY_IDS.add(REQUEST_TYPE_ID);
        KEY_PROPERTY_IDS.put(org.apache.ambari.server.controller.spi.Resource.Type.Upgrade, UPGRADE_REQUEST_ID);
        KEY_PROPERTY_IDS.put(org.apache.ambari.server.controller.spi.Resource.Type.Cluster, UPGRADE_CLUSTER_NAME);
    }

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.class);

    @com.google.inject.Inject
    public UpgradeResourceProvider(@com.google.inject.assistedinject.Assisted
    org.apache.ambari.server.controller.AmbariManagementController controller) {
        super(org.apache.ambari.server.controller.spi.Resource.Type.Upgrade, org.apache.ambari.server.controller.internal.UpgradeResourceProvider.PROPERTY_IDS, org.apache.ambari.server.controller.internal.UpgradeResourceProvider.KEY_PROPERTY_IDS, controller);
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus createResources(final org.apache.ambari.server.controller.spi.Request request) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.ResourceAlreadyExistsException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> requestMaps = request.getProperties();
        if (requestMaps.size() > 1) {
            throw new org.apache.ambari.server.controller.spi.SystemException("Can only initiate one upgrade per request.");
        }
        final java.util.Map<java.lang.String, java.lang.Object> requestMap = requestMaps.iterator().next();
        final java.lang.String clusterName = ((java.lang.String) (requestMap.get(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_CLUSTER_NAME)));
        final org.apache.ambari.server.state.Cluster cluster;
        try {
            cluster = org.apache.ambari.server.controller.internal.UpgradeResourceProvider.clusters.get().getCluster(clusterName);
        } catch (org.apache.ambari.server.AmbariException e) {
            throw new org.apache.ambari.server.controller.spi.NoSuchParentResourceException(java.lang.String.format("Cluster %s could not be loaded", clusterName));
        }
        if (!org.apache.ambari.server.security.authorization.AuthorizationHelper.isAuthorized(org.apache.ambari.server.security.authorization.ResourceType.CLUSTER, cluster.getResourceId(), java.util.EnumSet.of(org.apache.ambari.server.security.authorization.RoleAuthorization.CLUSTER_UPGRADE_DOWNGRADE_STACK))) {
            throw new org.apache.ambari.server.security.authorization.AuthorizationException("The authenticated user does not have authorization to " + "manage upgrade and downgrade");
        }
        org.apache.ambari.server.orm.entities.UpgradeEntity entity = createResources(new org.apache.ambari.server.controller.internal.AbstractResourceProvider.Command<org.apache.ambari.server.orm.entities.UpgradeEntity>() {
            @java.lang.Override
            public org.apache.ambari.server.orm.entities.UpgradeEntity invoke() throws org.apache.ambari.server.AmbariException, org.apache.ambari.server.security.authorization.AuthorizationException {
                final org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext upgradeContext = org.apache.ambari.server.controller.internal.UpgradeResourceProvider.s_upgradeContextFactory.create(cluster, requestMap);
                try {
                    return createUpgrade(upgradeContext);
                } catch (java.lang.Exception e) {
                    org.apache.ambari.server.controller.internal.UpgradeResourceProvider.LOG.error("Error appears during upgrade task submitting", e);
                    org.apache.ambari.server.controller.internal.UpgradeResourceProvider.clusters.get().invalidate(cluster);
                    throw e;
                }
            }
        });
        if (null == entity) {
            throw new org.apache.ambari.server.controller.spi.SystemException("Could not load upgrade");
        }
        notifyCreate(org.apache.ambari.server.controller.spi.Resource.Type.Upgrade, request);
        org.apache.ambari.server.controller.spi.Resource res = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Upgrade);
        res.setProperty(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_REQUEST_ID, entity.getRequestId());
        return new org.apache.ambari.server.controller.internal.RequestStatusImpl(null, java.util.Collections.singleton(res));
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.controller.spi.Resource> getResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> results = new java.util.HashSet<>();
        java.util.Set<java.lang.String> requestPropertyIds = getRequestPropertyIds(request, predicate);
        for (java.util.Map<java.lang.String, java.lang.Object> propertyMap : getPropertyMaps(predicate)) {
            java.lang.String clusterName = ((java.lang.String) (propertyMap.get(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_CLUSTER_NAME)));
            if ((null == clusterName) || clusterName.isEmpty()) {
                throw new java.lang.IllegalArgumentException("The cluster name is required when querying for upgrades");
            }
            org.apache.ambari.server.state.Cluster cluster;
            try {
                cluster = org.apache.ambari.server.controller.internal.UpgradeResourceProvider.clusters.get().getCluster(clusterName);
            } catch (org.apache.ambari.server.AmbariException e) {
                throw new org.apache.ambari.server.controller.spi.NoSuchResourceException(java.lang.String.format("Cluster %s could not be loaded", clusterName));
            }
            java.util.List<org.apache.ambari.server.orm.entities.UpgradeEntity> upgrades = new java.util.ArrayList<>();
            java.lang.String upgradeIdStr = ((java.lang.String) (propertyMap.get(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_REQUEST_ID)));
            if (null != upgradeIdStr) {
                org.apache.ambari.server.orm.entities.UpgradeEntity upgrade = org.apache.ambari.server.controller.internal.UpgradeResourceProvider.s_upgradeDAO.findUpgradeByRequestId(java.lang.Long.valueOf(upgradeIdStr));
                if (null != upgrade) {
                    upgrades.add(upgrade);
                }
            } else {
                upgrades = org.apache.ambari.server.controller.internal.UpgradeResourceProvider.s_upgradeDAO.findUpgrades(cluster.getClusterId());
            }
            for (org.apache.ambari.server.orm.entities.UpgradeEntity entity : upgrades) {
                org.apache.ambari.server.controller.spi.Resource r = toResource(entity, clusterName, requestPropertyIds);
                results.add(r);
                org.apache.ambari.server.orm.entities.RequestEntity rentity = org.apache.ambari.server.controller.internal.UpgradeResourceProvider.s_requestDAO.findByPK(entity.getRequestId());
                org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(r, org.apache.ambari.server.controller.internal.UpgradeResourceProvider.REQUEST_CONTEXT_ID, rentity.getRequestContext(), requestPropertyIds);
                org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(r, org.apache.ambari.server.controller.internal.UpgradeResourceProvider.REQUEST_TYPE_ID, rentity.getRequestType(), requestPropertyIds);
                org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(r, org.apache.ambari.server.controller.internal.UpgradeResourceProvider.REQUEST_CREATE_TIME_ID, rentity.getCreateTime(), requestPropertyIds);
                org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(r, org.apache.ambari.server.controller.internal.UpgradeResourceProvider.REQUEST_START_TIME_ID, rentity.getStartTime(), requestPropertyIds);
                org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(r, org.apache.ambari.server.controller.internal.UpgradeResourceProvider.REQUEST_END_TIME_ID, rentity.getEndTime(), requestPropertyIds);
                org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(r, org.apache.ambari.server.controller.internal.UpgradeResourceProvider.REQUEST_EXCLUSIVE_ID, rentity.isExclusive(), requestPropertyIds);
                java.util.Map<java.lang.Long, org.apache.ambari.server.orm.dao.HostRoleCommandStatusSummaryDTO> summary = org.apache.ambari.server.controller.internal.UpgradeResourceProvider.s_hostRoleCommandDAO.findAggregateCounts(entity.getRequestId());
                org.apache.ambari.server.controller.internal.CalculatedStatus calc = org.apache.ambari.server.controller.internal.CalculatedStatus.statusFromStageSummary(summary, summary.keySet());
                if ((calc.getStatus() == org.apache.ambari.server.actionmanager.HostRoleStatus.ABORTED) && entity.isSuspended()) {
                    double percent = org.apache.ambari.server.controller.internal.UpgradeResourceProvider.calculateAbortedProgress(summary);
                    org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(r, org.apache.ambari.server.controller.internal.UpgradeResourceProvider.REQUEST_PROGRESS_PERCENT_ID, percent * 100, requestPropertyIds);
                } else {
                    org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(r, org.apache.ambari.server.controller.internal.UpgradeResourceProvider.REQUEST_PROGRESS_PERCENT_ID, calc.getPercent(), requestPropertyIds);
                }
                org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(r, org.apache.ambari.server.controller.internal.UpgradeResourceProvider.REQUEST_STATUS_PROPERTY_ID, calc.getStatus(), requestPropertyIds);
            }
        }
        return results;
    }

    public static double calculateAbortedProgress(java.util.Map<java.lang.Long, org.apache.ambari.server.orm.dao.HostRoleCommandStatusSummaryDTO> summary) {
        java.util.Map<org.apache.ambari.server.actionmanager.HostRoleStatus, java.lang.Integer> countTotals = new java.util.HashMap<>();
        int totalTasks = 0;
        for (org.apache.ambari.server.orm.dao.HostRoleCommandStatusSummaryDTO statusSummary : summary.values()) {
            totalTasks += statusSummary.getTaskTotal();
            for (java.util.Map.Entry<org.apache.ambari.server.actionmanager.HostRoleStatus, java.lang.Integer> entry : statusSummary.getCounts().entrySet()) {
                if (!countTotals.containsKey(entry.getKey())) {
                    countTotals.put(entry.getKey(), java.lang.Integer.valueOf(0));
                }
                countTotals.put(entry.getKey(), countTotals.get(entry.getKey()) + entry.getValue());
            }
        }
        double percent = 0.0;
        for (org.apache.ambari.server.actionmanager.HostRoleStatus status : org.apache.ambari.server.actionmanager.HostRoleStatus.values()) {
            if (!countTotals.containsKey(status)) {
                countTotals.put(status, java.lang.Integer.valueOf(0));
            }
            double countValue = countTotals.get(status);
            switch (status) {
                case ABORTED :
                    break;
                case HOLDING :
                case HOLDING_FAILED :
                case HOLDING_TIMEDOUT :
                case IN_PROGRESS :
                case PENDING :
                    percent += countValue * 0.35;
                    break;
                case QUEUED :
                    percent += countValue * 0.09;
                    break;
                default :
                    if (status.isCompletedState()) {
                        percent += countValue / totalTasks;
                    }
                    break;
            }
        }
        return percent;
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus updateResources(final org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> requestMaps = request.getProperties();
        if (requestMaps.size() > 1) {
            throw new org.apache.ambari.server.controller.spi.SystemException("Can only update one upgrade per request.");
        }
        final java.util.Map<java.lang.String, java.lang.Object> propertyMap = requestMaps.iterator().next();
        final java.lang.String clusterName = ((java.lang.String) (propertyMap.get(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_CLUSTER_NAME)));
        final org.apache.ambari.server.state.Cluster cluster;
        try {
            cluster = org.apache.ambari.server.controller.internal.UpgradeResourceProvider.clusters.get().getCluster(clusterName);
        } catch (org.apache.ambari.server.AmbariException e) {
            throw new org.apache.ambari.server.controller.spi.NoSuchParentResourceException(java.lang.String.format("Cluster %s could not be loaded", clusterName));
        }
        if (!org.apache.ambari.server.security.authorization.AuthorizationHelper.isAuthorized(org.apache.ambari.server.security.authorization.ResourceType.CLUSTER, cluster.getResourceId(), java.util.EnumSet.of(org.apache.ambari.server.security.authorization.RoleAuthorization.CLUSTER_UPGRADE_DOWNGRADE_STACK))) {
            throw new org.apache.ambari.server.security.authorization.AuthorizationException("The authenticated user does not have authorization to " + "manage upgrade and downgrade");
        }
        java.lang.String requestIdProperty = ((java.lang.String) (propertyMap.get(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_REQUEST_ID)));
        if (null == requestIdProperty) {
            throw new java.lang.IllegalArgumentException(java.lang.String.format("%s is required", org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_REQUEST_ID));
        }
        long requestId = java.lang.Long.parseLong(requestIdProperty);
        org.apache.ambari.server.orm.entities.UpgradeEntity upgradeEntity = org.apache.ambari.server.controller.internal.UpgradeResourceProvider.s_upgradeDAO.findUpgradeByRequestId(requestId);
        if (null == upgradeEntity) {
            java.lang.String exceptionMessage = java.text.MessageFormat.format("The upgrade with request ID {0} was not found", requestIdProperty);
            throw new org.apache.ambari.server.controller.spi.NoSuchParentResourceException(exceptionMessage);
        }
        java.util.List<java.lang.String> updatableProperties = com.google.common.collect.Lists.newArrayList(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_REQUEST_STATUS, org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_SKIP_FAILURES, org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_SKIP_SC_FAILURES);
        boolean isRequiredPropertyInRequest = org.apache.commons.collections.CollectionUtils.containsAny(updatableProperties, propertyMap.keySet());
        if (!isRequiredPropertyInRequest) {
            java.lang.String exceptionMessage = java.text.MessageFormat.format("At least one of the following properties is required in the request: {0}", org.apache.commons.lang.StringUtils.join(updatableProperties, ", "));
            throw new java.lang.IllegalArgumentException(exceptionMessage);
        }
        java.lang.String requestStatus = ((java.lang.String) (propertyMap.get(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_REQUEST_STATUS)));
        java.lang.String skipFailuresRequestProperty = ((java.lang.String) (propertyMap.get(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_SKIP_FAILURES)));
        java.lang.String skipServiceCheckFailuresRequestProperty = ((java.lang.String) (propertyMap.get(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_SKIP_SC_FAILURES)));
        if (null != requestStatus) {
            org.apache.ambari.server.actionmanager.HostRoleStatus status = org.apache.ambari.server.actionmanager.HostRoleStatus.valueOf(requestStatus);
            boolean suspended = false;
            if ((status == org.apache.ambari.server.actionmanager.HostRoleStatus.ABORTED) && (!propertyMap.containsKey(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_SUSPENDED))) {
                throw new java.lang.IllegalArgumentException(java.lang.String.format("When changing the state of an upgrade to %s, the %s property is required to be either true or false.", status, org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_SUSPENDED));
            } else if (status == org.apache.ambari.server.actionmanager.HostRoleStatus.ABORTED) {
                suspended = java.lang.Boolean.valueOf(((java.lang.String) (propertyMap.get(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_SUSPENDED))));
            }
            try {
                setUpgradeRequestStatus(cluster, requestId, status, suspended, propertyMap);
            } catch (org.apache.ambari.server.AmbariException ambariException) {
                throw new org.apache.ambari.server.controller.spi.SystemException(ambariException.getMessage(), ambariException);
            }
        }
        if (org.apache.commons.lang.StringUtils.isNotEmpty(skipFailuresRequestProperty) || org.apache.commons.lang.StringUtils.isNotEmpty(skipServiceCheckFailuresRequestProperty)) {
            boolean skipFailures = upgradeEntity.isComponentFailureAutoSkipped();
            boolean skipServiceCheckFailures = upgradeEntity.isServiceCheckFailureAutoSkipped();
            if (null != skipFailuresRequestProperty) {
                skipFailures = java.lang.Boolean.parseBoolean(skipFailuresRequestProperty);
            }
            if (null != skipServiceCheckFailuresRequestProperty) {
                skipServiceCheckFailures = java.lang.Boolean.parseBoolean(skipServiceCheckFailuresRequestProperty);
            }
            org.apache.ambari.server.controller.internal.UpgradeResourceProvider.s_hostRoleCommandDAO.updateAutomaticSkipOnFailure(requestId, skipFailures, skipServiceCheckFailures);
            upgradeEntity.setAutoSkipComponentFailures(skipFailures);
            upgradeEntity.setAutoSkipServiceCheckFailures(skipServiceCheckFailures);
            org.apache.ambari.server.controller.internal.UpgradeResourceProvider.s_upgradeDAO.merge(upgradeEntity);
        }
        return getRequestStatus(null);
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus deleteResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        throw new org.apache.ambari.server.controller.spi.SystemException("Cannot delete Upgrades");
    }

    @java.lang.Override
    protected java.util.Set<java.lang.String> getPKPropertyIds() {
        return org.apache.ambari.server.controller.internal.UpgradeResourceProvider.PK_PROPERTY_IDS;
    }

    private org.apache.ambari.server.controller.spi.Resource toResource(org.apache.ambari.server.orm.entities.UpgradeEntity entity, java.lang.String clusterName, java.util.Set<java.lang.String> requestedIds) {
        org.apache.ambari.server.controller.internal.ResourceImpl resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Upgrade);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_ID, entity.getId(), requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_CLUSTER_NAME, clusterName, requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_TYPE, entity.getUpgradeType(), requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_PACK, entity.getUpgradePackage(), requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_REQUEST_ID, entity.getRequestId(), requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_DIRECTION, entity.getDirection(), requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_SUSPENDED, entity.isSuspended(), requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_DOWNGRADE_ALLOWED, entity.isDowngradeAllowed(), requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_SKIP_FAILURES, entity.isComponentFailureAutoSkipped(), requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_SKIP_SC_FAILURES, entity.isServiceCheckFailureAutoSkipped(), requestedIds);
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion = entity.getRepositoryVersion();
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_ASSOCIATED_VERSION, repositoryVersion.getVersion(), requestedIds);
        java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.UpgradeResourceProvider.RepositoryVersions> repositoryVersions = new java.util.HashMap<>();
        for (org.apache.ambari.server.orm.entities.UpgradeHistoryEntity history : entity.getHistory()) {
            org.apache.ambari.server.controller.internal.UpgradeResourceProvider.RepositoryVersions serviceVersions = repositoryVersions.get(history.getServiceName());
            if (null != serviceVersions) {
                continue;
            }
            serviceVersions = new org.apache.ambari.server.controller.internal.UpgradeResourceProvider.RepositoryVersions(history.getFromReposistoryVersion(), history.getTargetRepositoryVersion());
            repositoryVersions.put(history.getServiceName(), serviceVersions);
        }
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_VERSIONS, repositoryVersions, requestedIds);
        return resource;
    }

    private void injectVariables(org.apache.ambari.server.state.ConfigHelper configHelper, org.apache.ambari.server.state.Cluster cluster, org.apache.ambari.server.orm.entities.UpgradeItemEntity upgradeItem) {
        final java.lang.String regexp = "(\\{\\{.*?\\}\\})";
        java.lang.String task = upgradeItem.getTasks();
        if ((task != null) && (!task.isEmpty())) {
            java.util.regex.Matcher m = java.util.regex.Pattern.compile(regexp).matcher(task);
            while (m.find()) {
                java.lang.String origVar = m.group(1);
                java.lang.String configValue = configHelper.getPlaceholderValueFromDesiredConfigurations(cluster, origVar);
                if (null != configValue) {
                    task = task.replace(origVar, configValue);
                } else {
                    org.apache.ambari.server.controller.internal.UpgradeResourceProvider.LOG.error("Unable to retrieve value for {}", origVar);
                }
            } 
            upgradeItem.setTasks(task);
        }
    }

    protected org.apache.ambari.server.orm.entities.UpgradeEntity createUpgrade(org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext upgradeContext) throws org.apache.ambari.server.AmbariException, org.apache.ambari.server.security.authorization.AuthorizationException {
        org.apache.ambari.server.stack.upgrade.UpgradePack pack = upgradeContext.getUpgradePack();
        org.apache.ambari.server.state.Cluster cluster = upgradeContext.getCluster();
        org.apache.ambari.server.stack.upgrade.Direction direction = upgradeContext.getDirection();
        org.apache.ambari.server.state.ConfigHelper configHelper = getManagementController().getConfigHelper();
        java.util.List<org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeGroupHolder> groups = org.apache.ambari.server.controller.internal.UpgradeResourceProvider.s_upgradeHelper.createSequence(pack, upgradeContext);
        if (groups.isEmpty()) {
            throw new org.apache.ambari.server.AmbariException("There are no groupings available");
        }
        if (pack.getType() == org.apache.ambari.spi.upgrade.UpgradeType.NON_ROLLING) {
            boolean foundUpdateDesiredRepositoryIdGrouping = false;
            for (org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeGroupHolder group : groups) {
                if (group.groupClass == org.apache.ambari.server.stack.upgrade.UpdateStackGrouping.class) {
                    foundUpdateDesiredRepositoryIdGrouping = true;
                    break;
                }
            }
            if (!foundUpdateDesiredRepositoryIdGrouping) {
                throw new org.apache.ambari.server.AmbariException(java.lang.String.format("Express upgrade packs are required to have a group of type %s. The upgrade pack %s is missing this grouping.", "update-stack", pack.getName()));
            }
        }
        java.util.List<org.apache.ambari.server.orm.entities.UpgradeGroupEntity> groupEntities = new java.util.ArrayList<>();
        org.apache.ambari.server.controller.internal.RequestStageContainer req = createRequest(upgradeContext);
        org.apache.ambari.server.orm.entities.UpgradeEntity upgrade = new org.apache.ambari.server.orm.entities.UpgradeEntity();
        upgrade.setRepositoryVersion(upgradeContext.getRepositoryVersion());
        upgrade.setClusterId(cluster.getClusterId());
        upgrade.setDirection(direction);
        upgrade.setUpgradePackage(pack.getName());
        upgrade.setUpgradePackStackId(pack.getOwnerStackId());
        upgrade.setUpgradeType(pack.getType());
        upgrade.setAutoSkipComponentFailures(upgradeContext.isComponentFailureAutoSkipped());
        upgrade.setAutoSkipServiceCheckFailures(upgradeContext.isServiceCheckFailureAutoSkipped());
        upgrade.setDowngradeAllowed(upgradeContext.isDowngradeAllowed());
        upgrade.setOrchestration(upgradeContext.getOrchestrationType());
        addComponentHistoryToUpgrade(cluster, upgrade, upgradeContext);
        if ((pack.getType() == org.apache.ambari.spi.upgrade.UpgradeType.ROLLING) || (pack.getType() == org.apache.ambari.spi.upgrade.UpgradeType.HOST_ORDERED)) {
            if (direction == org.apache.ambari.server.stack.upgrade.Direction.UPGRADE) {
                org.apache.ambari.server.orm.entities.StackEntity targetStack = upgradeContext.getRepositoryVersion().getStack();
                cluster.setDesiredStackVersion(new org.apache.ambari.server.state.StackId(targetStack.getStackName(), targetStack.getStackVersion()));
            }
            org.apache.ambari.server.controller.internal.UpgradeResourceProvider.s_upgradeHelper.updateDesiredRepositoriesAndConfigs(upgradeContext);
            org.apache.ambari.server.controller.internal.UpgradeResourceProvider.s_upgradeHelper.publishDesiredRepositoriesUpdates(upgradeContext);
            if (direction == org.apache.ambari.server.stack.upgrade.Direction.DOWNGRADE) {
                org.apache.ambari.server.state.StackId targetStack = upgradeContext.getCluster().getCurrentStackVersion();
                cluster.setDesiredStackVersion(targetStack);
            }
        }
        org.apache.ambari.server.stack.upgrade.ConfigUpgradePack configUpgradePack = org.apache.ambari.server.controller.internal.UpgradeResourceProvider.ConfigurationPackBuilder.build(upgradeContext);
        org.apache.ambari.server.state.StackId effectiveStack = upgradeContext.getTargetStack();
        if (upgradeContext.getType() == org.apache.ambari.spi.upgrade.UpgradeType.NON_ROLLING) {
            effectiveStack = upgradeContext.getSourceStack();
        }
        for (org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeGroupHolder group : groups) {
            if ((upgradeContext.getType() == org.apache.ambari.spi.upgrade.UpgradeType.NON_ROLLING) && org.apache.ambari.server.stack.upgrade.UpdateStackGrouping.class.equals(group.groupClass)) {
                effectiveStack = upgradeContext.getTargetStack();
            }
            java.util.List<org.apache.ambari.server.orm.entities.UpgradeItemEntity> itemEntities = new java.util.ArrayList<>();
            for (org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper wrapper : group.items) {
                switch (wrapper.getType()) {
                    case SERVER_SIDE_ACTION :
                        {
                            for (org.apache.ambari.server.stack.upgrade.orchestrate.TaskWrapper taskWrapper : wrapper.getTasks()) {
                                for (org.apache.ambari.server.stack.upgrade.Task task : taskWrapper.getTasks()) {
                                    if (upgradeContext.isManualVerificationAutoSkipped() && (task.getType() == org.apache.ambari.server.stack.upgrade.Task.Type.MANUAL)) {
                                        continue;
                                    }
                                    org.apache.ambari.server.orm.entities.UpgradeItemEntity itemEntity = new org.apache.ambari.server.orm.entities.UpgradeItemEntity();
                                    itemEntity.setText(wrapper.getText());
                                    itemEntity.setTasks(wrapper.getTasksJson());
                                    itemEntity.setHosts(wrapper.getHostsJson());
                                    injectVariables(configHelper, cluster, itemEntity);
                                    if (makeServerSideStage(group, upgradeContext, effectiveStack, req, itemEntity, ((org.apache.ambari.server.stack.upgrade.ServerSideActionTask) (task)), configUpgradePack)) {
                                        itemEntities.add(itemEntity);
                                    }
                                }
                            }
                            break;
                        }
                    case REGENERATE_KEYTABS :
                        {
                            try {
                                int stageCount = req.getStages().size();
                                java.util.Map<java.lang.String, java.lang.String> requestProperties = new java.util.HashMap<>();
                                requestProperties.put(org.apache.ambari.server.controller.KerberosHelperImpl.SupportedCustomOperation.REGENERATE_KEYTABS.name().toLowerCase(), "missing");
                                requestProperties.put(org.apache.ambari.server.controller.KerberosHelper.ALLOW_RETRY, java.lang.Boolean.TRUE.toString().toLowerCase());
                                requestProperties.put(org.apache.ambari.server.controller.KerberosHelper.DIRECTIVE_CONFIG_UPDATE_POLICY, org.apache.ambari.server.controller.UpdateConfigurationPolicy.NEW_AND_IDENTITIES.name());
                                req = org.apache.ambari.server.controller.internal.UpgradeResourceProvider.s_kerberosHelper.get().executeCustomOperations(cluster, requestProperties, req, null);
                                java.util.List<org.apache.ambari.server.actionmanager.Stage> stages = req.getStages();
                                int newStageCount = stages.size();
                                for (int i = stageCount; i < newStageCount; i++) {
                                    org.apache.ambari.server.actionmanager.Stage stage = stages.get(i);
                                    stage.setSkippable(group.skippable);
                                    stage.setAutoSkipFailureSupported(group.supportsAutoSkipOnFailure);
                                    org.apache.ambari.server.orm.entities.UpgradeItemEntity itemEntity = new org.apache.ambari.server.orm.entities.UpgradeItemEntity();
                                    itemEntity.setStageId(stage.getStageId());
                                    itemEntity.setText(stage.getRequestContext());
                                    itemEntity.setTasks(wrapper.getTasksJson());
                                    itemEntity.setHosts(wrapper.getHostsJson());
                                    itemEntities.add(itemEntity);
                                    injectVariables(configHelper, cluster, itemEntity);
                                }
                            } catch (org.apache.ambari.server.serveraction.kerberos.KerberosOperationException kerberosOperationException) {
                                throw new org.apache.ambari.server.AmbariException("Unable to build keytab regeneration stage", kerberosOperationException);
                            }
                            break;
                        }
                    default :
                        {
                            org.apache.ambari.server.orm.entities.UpgradeItemEntity itemEntity = new org.apache.ambari.server.orm.entities.UpgradeItemEntity();
                            itemEntity.setText(wrapper.getText());
                            itemEntity.setTasks(wrapper.getTasksJson());
                            itemEntity.setHosts(wrapper.getHostsJson());
                            itemEntities.add(itemEntity);
                            injectVariables(configHelper, cluster, itemEntity);
                            createStage(group, upgradeContext, effectiveStack, req, itemEntity, wrapper);
                            break;
                        }
                }
            }
            if (!itemEntities.isEmpty()) {
                org.apache.ambari.server.orm.entities.UpgradeGroupEntity groupEntity = new org.apache.ambari.server.orm.entities.UpgradeGroupEntity();
                groupEntity.setName(group.name);
                groupEntity.setTitle(group.title);
                groupEntity.setItems(itemEntities);
                groupEntities.add(groupEntity);
            }
        }
        upgrade.setUpgradeGroups(groupEntities);
        req.getRequestStatusResponse();
        return createUpgradeInsideTransaction(cluster, req, upgrade, upgradeContext);
    }

    @com.google.inject.persist.Transactional
    org.apache.ambari.server.orm.entities.UpgradeEntity createUpgradeInsideTransaction(org.apache.ambari.server.state.Cluster cluster, org.apache.ambari.server.controller.internal.RequestStageContainer request, org.apache.ambari.server.orm.entities.UpgradeEntity upgradeEntity, org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext upgradeContext) throws org.apache.ambari.server.AmbariException {
        if (upgradeContext.isPatchRevert()) {
            org.apache.ambari.server.orm.entities.UpgradeEntity upgradeBeingReverted = org.apache.ambari.server.controller.internal.UpgradeResourceProvider.s_upgradeDAO.findUpgrade(upgradeContext.getPatchRevertUpgradeId());
            upgradeBeingReverted.setRevertAllowed(false);
            upgradeBeingReverted = org.apache.ambari.server.controller.internal.UpgradeResourceProvider.s_upgradeDAO.merge(upgradeBeingReverted);
        }
        request.persist();
        org.apache.ambari.server.orm.entities.RequestEntity requestEntity = org.apache.ambari.server.controller.internal.UpgradeResourceProvider.s_requestDAO.findByPK(request.getId());
        upgradeEntity.setRequestEntity(requestEntity);
        org.apache.ambari.server.controller.internal.UpgradeResourceProvider.s_upgradeDAO.create(upgradeEntity);
        STOMPUpdatePublisher.publish(org.apache.ambari.server.events.UpgradeUpdateEvent.formFullEvent(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.s_hostRoleCommandDAO, org.apache.ambari.server.controller.internal.UpgradeResourceProvider.s_requestDAO, upgradeEntity, org.apache.ambari.server.events.UpdateEventType.CREATE));
        cluster.setUpgradeEntity(upgradeEntity);
        return upgradeEntity;
    }

    private org.apache.ambari.server.controller.internal.RequestStageContainer createRequest(org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext upgradeContext) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.actionmanager.ActionManager actionManager = getManagementController().getActionManager();
        org.apache.ambari.server.controller.internal.RequestStageContainer requestStages = new org.apache.ambari.server.controller.internal.RequestStageContainer(actionManager.getNextRequestId(), null, org.apache.ambari.server.controller.internal.UpgradeResourceProvider.s_requestFactory.get(), actionManager);
        org.apache.ambari.server.stack.upgrade.Direction direction = upgradeContext.getDirection();
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion = upgradeContext.getRepositoryVersion();
        requestStages.setRequestContext(java.lang.String.format("%s %s %s", direction.getVerb(true), direction.getPreposition(), repositoryVersion.getVersion()));
        return requestStages;
    }

    private void createStage(org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeGroupHolder group, org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext context, org.apache.ambari.server.state.StackId stackId, org.apache.ambari.server.controller.internal.RequestStageContainer request, org.apache.ambari.server.orm.entities.UpgradeItemEntity entity, org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper wrapper) throws org.apache.ambari.server.AmbariException {
        boolean skippable = group.skippable;
        boolean supportsAutoSkipOnFailure = group.supportsAutoSkipOnFailure;
        boolean allowRetry = group.allowRetry;
        switch (wrapper.getType()) {
            case CONFIGURE :
            case START :
            case STOP :
            case RESTART :
                makeCommandStage(context, request, stackId, entity, wrapper, skippable, supportsAutoSkipOnFailure, allowRetry);
                break;
            case UPGRADE_TASKS :
                makeActionStage(context, request, stackId, entity, wrapper, skippable, supportsAutoSkipOnFailure, allowRetry);
                break;
            case SERVICE_CHECK :
                makeServiceCheckStage(context, request, stackId, entity, wrapper, skippable, supportsAutoSkipOnFailure, allowRetry);
                break;
            default :
                break;
        }
    }

    private void applyAdditionalParameters(org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper wrapper, java.util.Map<java.lang.String, java.lang.String> commandParams) {
        if (wrapper.getParams() != null) {
            for (java.util.Map.Entry<java.lang.String, java.lang.String> pair : wrapper.getParams().entrySet()) {
                if (!commandParams.containsKey(pair.getKey())) {
                    commandParams.put(pair.getKey(), pair.getValue());
                }
            }
        }
    }

    private void makeActionStage(org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext context, org.apache.ambari.server.controller.internal.RequestStageContainer request, org.apache.ambari.server.state.StackId stackId, org.apache.ambari.server.orm.entities.UpgradeItemEntity entity, org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper wrapper, boolean skippable, boolean supportsAutoSkipOnFailure, boolean allowRetry) throws org.apache.ambari.server.AmbariException {
        if (0 == wrapper.getHosts().size()) {
            throw new org.apache.ambari.server.AmbariException(java.lang.String.format("Cannot create action for '%s' with no hosts", wrapper.getText()));
        }
        org.apache.ambari.server.state.Cluster cluster = context.getCluster();
        org.apache.ambari.server.controller.internal.UpgradeResourceProvider.LOG.debug("Analyzing upgrade item {} with tasks: {}.", entity.getText(), entity.getTasks());
        java.lang.String serviceName = null;
        java.lang.String componentName = null;
        if (((wrapper.getTasks() != null) && (wrapper.getTasks().size() > 0)) && (wrapper.getTasks().get(0).getService() != null)) {
            org.apache.ambari.server.stack.upgrade.orchestrate.TaskWrapper taskWrapper = wrapper.getTasks().get(0);
            serviceName = taskWrapper.getService();
            componentName = taskWrapper.getComponent();
        }
        java.util.Map<java.lang.String, java.lang.String> params = getNewParameterMap(request, context);
        params.put(org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext.COMMAND_PARAM_TASKS, entity.getTasks());
        if (context.isScoped(org.apache.ambari.server.stack.upgrade.UpgradeScope.COMPLETE) && (null == componentName)) {
            if (context.getDirection().isUpgrade()) {
                params.put(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.VERSION, context.getRepositoryVersion().getVersion());
            } else {
                org.apache.ambari.server.orm.entities.UpgradeEntity lastUpgrade = org.apache.ambari.server.controller.internal.UpgradeResourceProvider.s_upgradeDAO.findLastUpgradeForCluster(cluster.getClusterId(), org.apache.ambari.server.stack.upgrade.Direction.UPGRADE);
                @org.apache.ambari.annotations.Experimental(feature = org.apache.ambari.annotations.ExperimentalFeature.PATCH_UPGRADES, comment = "Shouldn't be getting the overall downgrade-to version.")
                org.apache.ambari.server.orm.entities.UpgradeHistoryEntity lastHistory = lastUpgrade.getHistory().iterator().next();
                params.put(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.VERSION, lastHistory.getFromReposistoryVersion().getVersion());
            }
        }
        applyAdditionalParameters(wrapper, params);
        org.apache.ambari.server.controller.internal.RequestResourceFilter filter = new org.apache.ambari.server.controller.internal.RequestResourceFilter(serviceName, componentName, new java.util.ArrayList<>(wrapper.getHosts()));
        org.apache.ambari.server.controller.ActionExecutionContext actionContext = buildActionExecutionContext(cluster, context, org.apache.ambari.server.controller.internal.UpgradeResourceProvider.EXECUTE_TASK_ROLE, stackId, java.util.Collections.singletonList(filter), params, allowRetry, wrapper.getMaxTimeout(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.s_configuration));
        org.apache.ambari.server.controller.ExecuteCommandJson jsons = org.apache.ambari.server.controller.internal.UpgradeResourceProvider.s_commandExecutionHelper.get().getCommandJson(actionContext, cluster, stackId, null);
        org.apache.ambari.server.actionmanager.Stage stage = org.apache.ambari.server.controller.internal.UpgradeResourceProvider.s_stageFactory.get().createNew(request.getId().longValue(), "/tmp/ambari", cluster.getClusterName(), cluster.getClusterId(), entity.getText(), jsons.getCommandParamsForStage(), jsons.getHostParamsForStage());
        stage.setSkippable(skippable);
        stage.setAutoSkipFailureSupported(supportsAutoSkipOnFailure);
        long stageId = request.getLastStageId() + 1;
        if (0L == stageId) {
            stageId = 1L;
        }
        stage.setStageId(stageId);
        entity.setStageId(java.lang.Long.valueOf(stageId));
        org.apache.ambari.server.controller.internal.UpgradeResourceProvider.s_actionExecutionHelper.get().addExecutionCommandsToStage(actionContext, stage, null);
        for (java.util.Map<java.lang.String, org.apache.ambari.server.actionmanager.HostRoleCommand> map : stage.getHostRoleCommands().values()) {
            for (org.apache.ambari.server.actionmanager.HostRoleCommand hrc : map.values()) {
                hrc.setCommandDetail(entity.getText());
            }
        }
        request.addStages(java.util.Collections.singletonList(stage));
    }

    private void makeCommandStage(org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext context, org.apache.ambari.server.controller.internal.RequestStageContainer request, org.apache.ambari.server.state.StackId stackId, org.apache.ambari.server.orm.entities.UpgradeItemEntity entity, org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper wrapper, boolean skippable, boolean supportsAutoSkipOnFailure, boolean allowRetry) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.Cluster cluster = context.getCluster();
        java.util.List<org.apache.ambari.server.controller.internal.RequestResourceFilter> filters = new java.util.ArrayList<>();
        for (org.apache.ambari.server.stack.upgrade.orchestrate.TaskWrapper tw : wrapper.getTasks()) {
            java.lang.String serviceName = tw.getService();
            java.lang.String componentName = tw.getComponent();
            filters.add(new org.apache.ambari.server.controller.internal.RequestResourceFilter(serviceName, componentName, new java.util.ArrayList<>(tw.getHosts())));
        }
        java.lang.String function = null;
        switch (wrapper.getType()) {
            case CONFIGURE :
            case START :
            case STOP :
            case RESTART :
                function = wrapper.getType().name();
                break;
            default :
                function = "UNKNOWN";
                break;
        }
        java.util.Map<java.lang.String, java.lang.String> commandParams = getNewParameterMap(request, context);
        applyAdditionalParameters(wrapper, commandParams);
        org.apache.ambari.server.controller.ActionExecutionContext actionContext = buildActionExecutionContext(cluster, context, function, stackId, filters, commandParams, allowRetry, wrapper.getMaxTimeout(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.s_configuration));
        actionContext.setIsFutureCommand(true);
        org.apache.ambari.server.controller.ExecuteCommandJson jsons = org.apache.ambari.server.controller.internal.UpgradeResourceProvider.s_commandExecutionHelper.get().getCommandJson(actionContext, cluster, stackId, null);
        org.apache.ambari.server.actionmanager.Stage stage = org.apache.ambari.server.controller.internal.UpgradeResourceProvider.s_stageFactory.get().createNew(request.getId().longValue(), "/tmp/ambari", cluster.getClusterName(), cluster.getClusterId(), entity.getText(), jsons.getCommandParamsForStage(), jsons.getHostParamsForStage());
        stage.setSkippable(skippable);
        stage.setAutoSkipFailureSupported(supportsAutoSkipOnFailure);
        long stageId = request.getLastStageId() + 1;
        if (0L == stageId) {
            stageId = 1L;
        }
        stage.setStageId(stageId);
        entity.setStageId(java.lang.Long.valueOf(stageId));
        java.util.Map<java.lang.String, java.lang.String> requestParams = new java.util.HashMap<>();
        requestParams.put("command", function);
        if (allowRetry && (context.getType() == org.apache.ambari.spi.upgrade.UpgradeType.HOST_ORDERED)) {
            requestParams.put(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.COMMAND_RETRY_ENABLED, java.lang.Boolean.TRUE.toString().toLowerCase());
        }
        org.apache.ambari.server.controller.internal.UpgradeResourceProvider.s_commandExecutionHelper.get().addExecutionCommandsToStage(actionContext, stage, requestParams, jsons);
        request.addStages(java.util.Collections.singletonList(stage));
    }

    private void makeServiceCheckStage(org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext context, org.apache.ambari.server.controller.internal.RequestStageContainer request, org.apache.ambari.server.state.StackId stackId, org.apache.ambari.server.orm.entities.UpgradeItemEntity entity, org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper wrapper, boolean skippable, boolean supportsAutoSkipOnFailure, boolean allowRetry) throws org.apache.ambari.server.AmbariException {
        java.util.List<org.apache.ambari.server.controller.internal.RequestResourceFilter> filters = new java.util.ArrayList<>();
        for (org.apache.ambari.server.stack.upgrade.orchestrate.TaskWrapper tw : wrapper.getTasks()) {
            java.util.List<java.lang.String> hosts = tw.getHosts().stream().collect(java.util.stream.Collectors.toList());
            filters.add(new org.apache.ambari.server.controller.internal.RequestResourceFilter(tw.getService(), "", hosts));
        }
        org.apache.ambari.server.state.Cluster cluster = context.getCluster();
        java.util.Map<java.lang.String, java.lang.String> commandParams = getNewParameterMap(request, context);
        applyAdditionalParameters(wrapper, commandParams);
        org.apache.ambari.server.controller.ActionExecutionContext actionContext = buildActionExecutionContext(cluster, context, "SERVICE_CHECK", stackId, filters, commandParams, allowRetry, wrapper.getMaxTimeout(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.s_configuration));
        actionContext.setAutoSkipFailures(context.isServiceCheckFailureAutoSkipped());
        org.apache.ambari.server.controller.ExecuteCommandJson jsons = org.apache.ambari.server.controller.internal.UpgradeResourceProvider.s_commandExecutionHelper.get().getCommandJson(actionContext, cluster, stackId, null);
        org.apache.ambari.server.actionmanager.Stage stage = org.apache.ambari.server.controller.internal.UpgradeResourceProvider.s_stageFactory.get().createNew(request.getId().longValue(), "/tmp/ambari", cluster.getClusterName(), cluster.getClusterId(), entity.getText(), jsons.getCommandParamsForStage(), jsons.getHostParamsForStage());
        stage.setSkippable(skippable);
        stage.setAutoSkipFailureSupported(supportsAutoSkipOnFailure);
        long stageId = request.getLastStageId() + 1;
        if (0L == stageId) {
            stageId = 1L;
        }
        stage.setStageId(stageId);
        entity.setStageId(java.lang.Long.valueOf(stageId));
        java.util.Map<java.lang.String, java.lang.String> requestParams = getNewParameterMap(request, context);
        org.apache.ambari.server.controller.internal.UpgradeResourceProvider.s_commandExecutionHelper.get().addExecutionCommandsToStage(actionContext, stage, requestParams, jsons);
        request.addStages(java.util.Collections.singletonList(stage));
    }

    private boolean makeServerSideStage(org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeGroupHolder group, org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext context, org.apache.ambari.server.state.StackId stackId, org.apache.ambari.server.controller.internal.RequestStageContainer request, org.apache.ambari.server.orm.entities.UpgradeItemEntity entity, org.apache.ambari.server.stack.upgrade.ServerSideActionTask task, org.apache.ambari.server.stack.upgrade.ConfigUpgradePack configUpgradePack) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.Cluster cluster = context.getCluster();
        org.apache.ambari.server.stack.upgrade.UpgradePack upgradePack = context.getUpgradePack();
        java.util.Map<java.lang.String, java.lang.String> commandParams = getNewParameterMap(request, context);
        commandParams.put(org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext.COMMAND_PARAM_UPGRADE_PACK, upgradePack.getName());
        java.lang.String itemDetail = entity.getText();
        java.lang.String stageText = org.apache.commons.lang.StringUtils.abbreviate(entity.getText(), 255);
        boolean process = true;
        switch (task.getType()) {
            case SERVER_ACTION :
            case MANUAL :
                {
                    org.apache.ambari.server.stack.upgrade.ServerSideActionTask serverTask = task;
                    if (null != serverTask.summary) {
                        stageText = serverTask.summary;
                    }
                    if (task.getType() == org.apache.ambari.server.stack.upgrade.Task.Type.MANUAL) {
                        org.apache.ambari.server.stack.upgrade.ManualTask mt = ((org.apache.ambari.server.stack.upgrade.ManualTask) (task));
                        if (org.apache.commons.lang.StringUtils.isNotBlank(mt.structuredOut)) {
                            commandParams.put(org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext.COMMAND_PARAM_STRUCT_OUT, mt.structuredOut);
                        }
                    }
                    if (!serverTask.messages.isEmpty()) {
                        com.google.gson.JsonArray messageArray = new com.google.gson.JsonArray();
                        for (java.lang.String message : serverTask.messages) {
                            com.google.gson.JsonObject messageObj = new com.google.gson.JsonObject();
                            messageObj.addProperty("message", message);
                            messageArray.add(messageObj);
                        }
                        itemDetail = messageArray.toString();
                        entity.setText(itemDetail);
                        itemDetail = org.apache.commons.lang.StringUtils.join(serverTask.messages, " ");
                    }
                    break;
                }
            case CONFIGURE :
                {
                    org.apache.ambari.server.stack.upgrade.ConfigureTask ct = ((org.apache.ambari.server.stack.upgrade.ConfigureTask) (task));
                    if (context.getOrchestrationType().isRevertable() && (!ct.supportsPatch)) {
                        process = false;
                    }
                    java.util.Map<java.lang.String, java.lang.String> configurationChanges = ct.getConfigurationChanges(cluster, configUpgradePack);
                    commandParams.putAll(configurationChanges);
                    java.lang.String configType = configurationChanges.get(org.apache.ambari.server.stack.upgrade.ConfigureTask.PARAMETER_CONFIG_TYPE);
                    if (null != configType) {
                        itemDetail = java.lang.String.format("Updating configuration %s", configType);
                    } else {
                        itemDetail = "Skipping Configuration Task " + org.apache.commons.lang.StringUtils.defaultString(ct.id, "(missing id)");
                    }
                    entity.setText(itemDetail);
                    java.lang.String configureTaskSummary = ct.getSummary(configUpgradePack);
                    if (null != configureTaskSummary) {
                        stageText = configureTaskSummary;
                    } else {
                        stageText = itemDetail;
                    }
                    break;
                }
            case CREATE_AND_CONFIGURE :
                {
                    org.apache.ambari.server.stack.upgrade.CreateAndConfigureTask ct = ((org.apache.ambari.server.stack.upgrade.CreateAndConfigureTask) (task));
                    if (context.getOrchestrationType().isRevertable() && (!ct.supportsPatch)) {
                        process = false;
                    }
                    java.util.Map<java.lang.String, java.lang.String> configurationChanges = ct.getConfigurationChanges(cluster, configUpgradePack);
                    commandParams.putAll(configurationChanges);
                    java.lang.String configType = configurationChanges.get(org.apache.ambari.server.stack.upgrade.CreateAndConfigureTask.PARAMETER_CONFIG_TYPE);
                    if (null != configType) {
                        itemDetail = java.lang.String.format("Updating configuration %s", configType);
                    } else {
                        itemDetail = "Skipping Configuration Task " + org.apache.commons.lang.StringUtils.defaultString(ct.id, "(missing id)");
                    }
                    entity.setText(itemDetail);
                    java.lang.String configureTaskSummary = ct.getSummary(configUpgradePack);
                    if (null != configureTaskSummary) {
                        stageText = configureTaskSummary;
                    } else {
                        stageText = itemDetail;
                    }
                    break;
                }
            case ADD_COMPONENT :
                {
                    org.apache.ambari.server.stack.upgrade.AddComponentTask addComponentTask = ((org.apache.ambari.server.stack.upgrade.AddComponentTask) (task));
                    java.lang.String serializedTask = addComponentTask.toJson();
                    commandParams.put(org.apache.ambari.server.stack.upgrade.AddComponentTask.PARAMETER_SERIALIZED_ADD_COMPONENT_TASK, serializedTask);
                    break;
                }
            default :
                break;
        }
        if (!process) {
            return false;
        }
        org.apache.ambari.server.controller.ActionExecutionContext actionContext = buildActionExecutionContext(cluster, context, org.apache.ambari.server.Role.AMBARI_SERVER_ACTION.toString(), stackId, java.util.Collections.emptyList(), commandParams, group.allowRetry, -1);
        org.apache.ambari.server.controller.ExecuteCommandJson jsons = org.apache.ambari.server.controller.internal.UpgradeResourceProvider.s_commandExecutionHelper.get().getCommandJson(actionContext, cluster, context.getRepositoryVersion().getStackId(), null);
        org.apache.ambari.server.actionmanager.Stage stage = org.apache.ambari.server.controller.internal.UpgradeResourceProvider.s_stageFactory.get().createNew(request.getId().longValue(), "/tmp/ambari", cluster.getClusterName(), cluster.getClusterId(), stageText, jsons.getCommandParamsForStage(), jsons.getHostParamsForStage());
        stage.setSkippable(group.skippable);
        stage.setAutoSkipFailureSupported(group.supportsAutoSkipOnFailure);
        long stageId = request.getLastStageId() + 1;
        if (0L == stageId) {
            stageId = 1L;
        }
        stage.setStageId(stageId);
        entity.setStageId(java.lang.Long.valueOf(stageId));
        java.util.Map<java.lang.String, java.lang.String> taskParameters = task.getParameters();
        commandParams.putAll(taskParameters);
        org.apache.ambari.server.state.StackInfo stackInfo = org.apache.ambari.server.controller.internal.UpgradeResourceProvider.s_metaProvider.get().getStack(stackId);
        java.lang.ClassLoader classLoader = stackInfo.getLibraryClassLoader();
        if (null == classLoader) {
            classLoader = org.apache.ambari.server.cleanup.ClasspathScannerUtils.class.getClassLoader();
        }
        final java.lang.String classToSchedule;
        java.lang.String taskClass = task.getImplementationClass();
        try {
            java.lang.Class<?> clazz = classLoader.loadClass(taskClass);
            if (org.apache.ambari.spi.upgrade.UpgradeAction.class.isAssignableFrom(clazz)) {
                classToSchedule = org.apache.ambari.server.serveraction.upgrades.PluginUpgradeServerAction.class.getName();
                commandParams.put(org.apache.ambari.server.serveraction.ServerAction.WRAPPED_CLASS_NAME, taskClass);
            } else if (org.apache.ambari.server.serveraction.ServerAction.class.isAssignableFrom(clazz)) {
                classToSchedule = task.getImplementationClass();
            } else {
                throw new org.apache.ambari.server.AmbariException(("The class " + taskClass) + " was not able to be scheduled during the upgrade because it is not compatible");
            }
        } catch (java.lang.ClassNotFoundException cnfe) {
            org.apache.ambari.server.controller.internal.UpgradeResourceProvider.LOG.error("Unable to load {} specified in the upgrade pack", taskClass, cnfe);
            throw new org.apache.ambari.server.AmbariException(("The class " + taskClass) + " was not able to be scheduled during the upgrade because it was not found");
        }
        stage.addServerActionCommand(classToSchedule, getManagementController().getAuthName(), org.apache.ambari.server.Role.AMBARI_SERVER_ACTION, org.apache.ambari.server.RoleCommand.EXECUTE, cluster.getClusterName(), new org.apache.ambari.server.state.svccomphost.ServiceComponentHostServerActionEvent(null, java.lang.System.currentTimeMillis()), commandParams, itemDetail, null, org.apache.ambari.server.controller.internal.UpgradeResourceProvider.s_configuration.getDefaultServerTaskTimeout(), group.allowRetry, context.isComponentFailureAutoSkipped());
        request.addStages(java.util.Collections.singletonList(stage));
        return true;
    }

    private java.util.Map<java.lang.String, java.lang.String> getNewParameterMap(org.apache.ambari.server.controller.internal.RequestStageContainer requestStageContainer, org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext context) {
        java.util.Map<java.lang.String, java.lang.String> parameters = context.getInitializedCommandParameters();
        parameters.put(org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext.COMMAND_PARAM_REQUEST_ID, java.lang.String.valueOf(requestStageContainer.getId()));
        return parameters;
    }

    @com.google.inject.persist.Transactional
    void setUpgradeRequestStatus(org.apache.ambari.server.state.Cluster cluster, long requestId, org.apache.ambari.server.actionmanager.HostRoleStatus status, boolean suspended, java.util.Map<java.lang.String, java.lang.Object> propertyMap) throws org.apache.ambari.server.AmbariException {
        if ((status != org.apache.ambari.server.actionmanager.HostRoleStatus.ABORTED) && (status != org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING)) {
            throw new java.lang.IllegalArgumentException(java.lang.String.format("Cannot set status %s, only %s is allowed", status, java.util.EnumSet.of(org.apache.ambari.server.actionmanager.HostRoleStatus.ABORTED, org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING)));
        }
        java.lang.String reason = ((java.lang.String) (propertyMap.get(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_ABORT_REASON)));
        if (null == reason) {
            reason = java.lang.String.format(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.DEFAULT_REASON_TEMPLATE, requestId);
        }
        java.util.Map<java.lang.Long, org.apache.ambari.server.orm.dao.HostRoleCommandStatusSummaryDTO> aggregateCounts = org.apache.ambari.server.controller.internal.UpgradeResourceProvider.s_hostRoleCommandDAO.findAggregateCounts(requestId);
        org.apache.ambari.server.controller.internal.CalculatedStatus calculatedStatus = org.apache.ambari.server.controller.internal.CalculatedStatus.statusFromStageSummary(aggregateCounts, aggregateCounts.keySet());
        org.apache.ambari.server.actionmanager.HostRoleStatus internalStatus = calculatedStatus.getStatus();
        if ((org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING == status) && (!((internalStatus == org.apache.ambari.server.actionmanager.HostRoleStatus.ABORTED) || (internalStatus == org.apache.ambari.server.actionmanager.HostRoleStatus.IN_PROGRESS)))) {
            throw new java.lang.IllegalArgumentException(java.lang.String.format("Can only set status to %s when the upgrade is %s (currently %s)", status, org.apache.ambari.server.actionmanager.HostRoleStatus.ABORTED, internalStatus));
        }
        org.apache.ambari.server.actionmanager.ActionManager actionManager = getManagementController().getActionManager();
        if ((org.apache.ambari.server.actionmanager.HostRoleStatus.ABORTED == status) && (!internalStatus.isCompletedState())) {
            actionManager.cancelRequest(requestId, reason);
            org.apache.ambari.server.orm.entities.UpgradeEntity upgradeEntity = org.apache.ambari.server.controller.internal.UpgradeResourceProvider.s_upgradeDAO.findUpgradeByRequestId(requestId);
            if (suspended) {
                upgradeEntity.setSuspended(suspended);
                upgradeEntity = org.apache.ambari.server.controller.internal.UpgradeResourceProvider.s_upgradeDAO.merge(upgradeEntity);
                STOMPUpdatePublisher.publish(org.apache.ambari.server.events.UpgradeUpdateEvent.formUpdateEvent(hostRoleCommandDAO, requestDAO, upgradeEntity));
            } else {
                cluster.setUpgradeEntity(null);
            }
        } else if (status == org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING) {
            java.util.List<java.lang.Long> taskIds = new java.util.ArrayList<>();
            java.util.List<org.apache.ambari.server.orm.entities.HostRoleCommandEntity> hrcEntities = org.apache.ambari.server.controller.internal.UpgradeResourceProvider.s_hostRoleCommandDAO.findByRequestIdAndStatuses(requestId, com.google.common.collect.Sets.newHashSet(org.apache.ambari.server.actionmanager.HostRoleStatus.ABORTED));
            for (org.apache.ambari.server.orm.entities.HostRoleCommandEntity hrcEntity : hrcEntities) {
                taskIds.add(hrcEntity.getTaskId());
            }
            actionManager.resubmitTasks(taskIds);
            org.apache.ambari.server.orm.entities.UpgradeEntity lastUpgradeItemForCluster = org.apache.ambari.server.controller.internal.UpgradeResourceProvider.s_upgradeDAO.findLastUpgradeOrDowngradeForCluster(cluster.getClusterId());
            lastUpgradeItemForCluster.setSuspended(false);
            lastUpgradeItemForCluster = org.apache.ambari.server.controller.internal.UpgradeResourceProvider.s_upgradeDAO.merge(lastUpgradeItemForCluster);
            STOMPUpdatePublisher.publish(org.apache.ambari.server.events.UpgradeUpdateEvent.formUpdateEvent(hostRoleCommandDAO, requestDAO, lastUpgradeItemForCluster));
        }
    }

    private void addComponentHistoryToUpgrade(org.apache.ambari.server.state.Cluster cluster, org.apache.ambari.server.orm.entities.UpgradeEntity upgrade, org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext upgradeContext) throws org.apache.ambari.server.AmbariException {
        java.util.Set<java.lang.String> services = upgradeContext.getSupportedServices();
        for (java.lang.String serviceName : services) {
            org.apache.ambari.server.state.Service service = cluster.getService(serviceName);
            java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceComponent> componentMap = service.getServiceComponents();
            for (org.apache.ambari.server.state.ServiceComponent component : componentMap.values()) {
                org.apache.ambari.server.orm.entities.UpgradeHistoryEntity history = new org.apache.ambari.server.orm.entities.UpgradeHistoryEntity();
                history.setUpgrade(upgrade);
                history.setServiceName(serviceName);
                history.setComponentName(component.getName());
                if (upgradeContext.getDirection() == org.apache.ambari.server.stack.upgrade.Direction.UPGRADE) {
                    history.setFromRepositoryVersion(component.getDesiredRepositoryVersion());
                    history.setTargetRepositoryVersion(upgradeContext.getRepositoryVersion());
                } else {
                    org.apache.ambari.server.orm.entities.RepositoryVersionEntity targetRepositoryVersion = upgradeContext.getTargetRepositoryVersion(serviceName);
                    history.setFromRepositoryVersion(upgradeContext.getRepositoryVersion());
                    history.setTargetRepositoryVersion(targetRepositoryVersion);
                }
                upgrade.addHistory(history);
            }
        }
    }

    private org.apache.ambari.server.controller.ActionExecutionContext buildActionExecutionContext(org.apache.ambari.server.state.Cluster cluster, org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext context, java.lang.String role, org.apache.ambari.server.state.StackId stackId, java.util.List<org.apache.ambari.server.controller.internal.RequestResourceFilter> resourceFilters, java.util.Map<java.lang.String, java.lang.String> commandParams, boolean allowRetry, int timeout) {
        org.apache.ambari.server.controller.ActionExecutionContext actionContext = new org.apache.ambari.server.controller.ActionExecutionContext(cluster.getClusterName(), role, resourceFilters, commandParams);
        actionContext.setStackId(stackId);
        actionContext.setTimeout(timeout);
        actionContext.setRetryAllowed(allowRetry);
        actionContext.setAutoSkipFailures(context.isComponentFailureAutoSkipped());
        actionContext.setMaintenanceModeHostExcluded(true);
        return actionContext;
    }

    public static final class ConfigurationPackBuilder {
        public static org.apache.ambari.server.stack.upgrade.ConfigUpgradePack build(org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext cx) {
            final org.apache.ambari.server.stack.upgrade.UpgradePack upgradePack = cx.getUpgradePack();
            final org.apache.ambari.server.state.StackId stackId;
            if (cx.getDirection() == org.apache.ambari.server.stack.upgrade.Direction.UPGRADE) {
                stackId = cx.getStackIdFromVersions(cx.getSourceVersions());
            } else {
                stackId = cx.getStackIdFromVersions(cx.getTargetVersions());
            }
            java.util.List<org.apache.ambari.server.stack.upgrade.UpgradePack.IntermediateStack> intermediateStacks = upgradePack.getIntermediateStacks();
            org.apache.ambari.server.stack.upgrade.ConfigUpgradePack configUpgradePack = org.apache.ambari.server.controller.internal.UpgradeResourceProvider.s_metaProvider.get().getConfigUpgradePack(stackId.getStackName(), stackId.getStackVersion());
            if (null != intermediateStacks) {
                java.util.ArrayList<org.apache.ambari.server.stack.upgrade.ConfigUpgradePack> configPacksToMerge = com.google.common.collect.Lists.newArrayList(configUpgradePack);
                for (org.apache.ambari.server.stack.upgrade.UpgradePack.IntermediateStack intermediateStack : intermediateStacks) {
                    org.apache.ambari.server.stack.upgrade.ConfigUpgradePack intermediateConfigUpgradePack = org.apache.ambari.server.controller.internal.UpgradeResourceProvider.s_metaProvider.get().getConfigUpgradePack(stackId.getStackName(), intermediateStack.version);
                    configPacksToMerge.add(intermediateConfigUpgradePack);
                }
                configUpgradePack = org.apache.ambari.server.stack.upgrade.ConfigUpgradePack.merge(configPacksToMerge);
            }
            return configUpgradePack;
        }
    }

    static final class RepositoryVersions {
        @org.codehaus.jackson.annotate.JsonProperty("from_repository_id")
        final long fromRepositoryId;

        @org.codehaus.jackson.annotate.JsonProperty("from_repository_version")
        final java.lang.String fromRepositoryVersion;

        @org.codehaus.jackson.annotate.JsonProperty("to_repository_id")
        final long toRepositoryId;

        @org.codehaus.jackson.annotate.JsonProperty("to_repository_version")
        final java.lang.String toRepositoryVersion;

        public RepositoryVersions(org.apache.ambari.server.orm.entities.RepositoryVersionEntity from, org.apache.ambari.server.orm.entities.RepositoryVersionEntity to) {
            fromRepositoryId = from.getId();
            fromRepositoryVersion = from.getVersion();
            toRepositoryId = to.getId();
            toRepositoryVersion = to.getVersion();
        }
    }
}