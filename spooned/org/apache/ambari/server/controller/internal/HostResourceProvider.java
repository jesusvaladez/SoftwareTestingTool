package org.apache.ambari.server.controller.internal;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import com.google.inject.persist.Transactional;
import org.apache.commons.lang.StringUtils;
public class HostResourceProvider extends org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.controller.internal.HostResourceProvider.class);

    public static final java.lang.String RESPONSE_KEY = "Hosts";

    public static final java.lang.String ALL_PROPERTIES = (org.apache.ambari.server.controller.internal.HostResourceProvider.RESPONSE_KEY + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + "*";

    public static final java.lang.String CLUSTER_NAME_PROPERTY_ID = "cluster_name";

    public static final java.lang.String CPU_COUNT_PROPERTY_ID = "cpu_count";

    public static final java.lang.String DESIRED_CONFIGS_PROPERTY_ID = "desired_configs";

    public static final java.lang.String DISK_INFO_PROPERTY_ID = "disk_info";

    public static final java.lang.String HOST_HEALTH_REPORT_PROPERTY_ID = "host_health_report";

    public static final java.lang.String HOST_NAME_PROPERTY_ID = "host_name";

    public static final java.lang.String HOST_STATUS_PROPERTY_ID = "host_status";

    public static final java.lang.String IP_PROPERTY_ID = "ip";

    public static final java.lang.String LAST_AGENT_ENV_PROPERTY_ID = "last_agent_env";

    public static final java.lang.String LAST_HEARTBEAT_TIME_PROPERTY_ID = "last_heartbeat_time";

    public static final java.lang.String LAST_REGISTRATION_TIME_PROPERTY_ID = "last_registration_time";

    public static final java.lang.String MAINTENANCE_STATE_PROPERTY_ID = "maintenance_state";

    public static final java.lang.String OS_ARCH_PROPERTY_ID = "os_arch";

    public static final java.lang.String OS_FAMILY_PROPERTY_ID = "os_family";

    public static final java.lang.String OS_TYPE_PROPERTY_ID = "os_type";

    public static final java.lang.String PHYSICAL_CPU_COUNT_PROPERTY_ID = "ph_cpu_count";

    public static final java.lang.String PUBLIC_NAME_PROPERTY_ID = "public_host_name";

    public static final java.lang.String RACK_INFO_PROPERTY_ID = "rack_info";

    public static final java.lang.String RECOVERY_REPORT_PROPERTY_ID = "recovery_report";

    public static final java.lang.String RECOVERY_SUMMARY_PROPERTY_ID = "recovery_summary";

    public static final java.lang.String STATE_PROPERTY_ID = "host_state";

    public static final java.lang.String TOTAL_MEM_PROPERTY_ID = "total_mem";

    public static final java.lang.String ATTRIBUTES_PROPERTY_ID = "attributes";

    public static final java.lang.String HOST_CLUSTER_NAME_PROPERTY_ID = (org.apache.ambari.server.controller.internal.HostResourceProvider.RESPONSE_KEY + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.HostResourceProvider.CLUSTER_NAME_PROPERTY_ID;

    public static final java.lang.String HOST_CPU_COUNT_PROPERTY_ID = (org.apache.ambari.server.controller.internal.HostResourceProvider.RESPONSE_KEY + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.HostResourceProvider.CPU_COUNT_PROPERTY_ID;

    public static final java.lang.String HOST_DESIRED_CONFIGS_PROPERTY_ID = (org.apache.ambari.server.controller.internal.HostResourceProvider.RESPONSE_KEY + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.HostResourceProvider.DESIRED_CONFIGS_PROPERTY_ID;

    public static final java.lang.String HOST_DISK_INFO_PROPERTY_ID = (org.apache.ambari.server.controller.internal.HostResourceProvider.RESPONSE_KEY + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.HostResourceProvider.DISK_INFO_PROPERTY_ID;

    public static final java.lang.String HOST_HOST_HEALTH_REPORT_PROPERTY_ID = (org.apache.ambari.server.controller.internal.HostResourceProvider.RESPONSE_KEY + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_HEALTH_REPORT_PROPERTY_ID;

    public static final java.lang.String HOST_HOST_STATUS_PROPERTY_ID = (org.apache.ambari.server.controller.internal.HostResourceProvider.RESPONSE_KEY + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_STATUS_PROPERTY_ID;

    public static final java.lang.String HOST_IP_PROPERTY_ID = (org.apache.ambari.server.controller.internal.HostResourceProvider.RESPONSE_KEY + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.HostResourceProvider.IP_PROPERTY_ID;

    public static final java.lang.String HOST_LAST_AGENT_ENV_PROPERTY_ID = (org.apache.ambari.server.controller.internal.HostResourceProvider.RESPONSE_KEY + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.HostResourceProvider.LAST_AGENT_ENV_PROPERTY_ID;

    public static final java.lang.String HOST_LAST_HEARTBEAT_TIME_PROPERTY_ID = (org.apache.ambari.server.controller.internal.HostResourceProvider.RESPONSE_KEY + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.HostResourceProvider.LAST_HEARTBEAT_TIME_PROPERTY_ID;

    public static final java.lang.String HOST_LAST_REGISTRATION_TIME_PROPERTY_ID = (org.apache.ambari.server.controller.internal.HostResourceProvider.RESPONSE_KEY + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.HostResourceProvider.LAST_REGISTRATION_TIME_PROPERTY_ID;

    public static final java.lang.String HOST_MAINTENANCE_STATE_PROPERTY_ID = (org.apache.ambari.server.controller.internal.HostResourceProvider.RESPONSE_KEY + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.HostResourceProvider.MAINTENANCE_STATE_PROPERTY_ID;

    public static final java.lang.String HOST_HOST_NAME_PROPERTY_ID = (org.apache.ambari.server.controller.internal.HostResourceProvider.RESPONSE_KEY + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_NAME_PROPERTY_ID;

    public static final java.lang.String HOST_OS_ARCH_PROPERTY_ID = (org.apache.ambari.server.controller.internal.HostResourceProvider.RESPONSE_KEY + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.HostResourceProvider.OS_ARCH_PROPERTY_ID;

    public static final java.lang.String HOST_OS_FAMILY_PROPERTY_ID = (org.apache.ambari.server.controller.internal.HostResourceProvider.RESPONSE_KEY + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.HostResourceProvider.OS_FAMILY_PROPERTY_ID;

    public static final java.lang.String HOST_OS_TYPE_PROPERTY_ID = (org.apache.ambari.server.controller.internal.HostResourceProvider.RESPONSE_KEY + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.HostResourceProvider.OS_TYPE_PROPERTY_ID;

    public static final java.lang.String HOST_PHYSICAL_CPU_COUNT_PROPERTY_ID = (org.apache.ambari.server.controller.internal.HostResourceProvider.RESPONSE_KEY + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.HostResourceProvider.PHYSICAL_CPU_COUNT_PROPERTY_ID;

    public static final java.lang.String HOST_PUBLIC_NAME_PROPERTY_ID = (org.apache.ambari.server.controller.internal.HostResourceProvider.RESPONSE_KEY + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.HostResourceProvider.PUBLIC_NAME_PROPERTY_ID;

    public static final java.lang.String HOST_RACK_INFO_PROPERTY_ID = (org.apache.ambari.server.controller.internal.HostResourceProvider.RESPONSE_KEY + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.HostResourceProvider.RACK_INFO_PROPERTY_ID;

    public static final java.lang.String HOST_RECOVERY_REPORT_PROPERTY_ID = (org.apache.ambari.server.controller.internal.HostResourceProvider.RESPONSE_KEY + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.HostResourceProvider.RECOVERY_REPORT_PROPERTY_ID;

    public static final java.lang.String HOST_RECOVERY_SUMMARY_PROPERTY_ID = (org.apache.ambari.server.controller.internal.HostResourceProvider.RESPONSE_KEY + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.HostResourceProvider.RECOVERY_SUMMARY_PROPERTY_ID;

    public static final java.lang.String HOST_STATE_PROPERTY_ID = (org.apache.ambari.server.controller.internal.HostResourceProvider.RESPONSE_KEY + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.HostResourceProvider.STATE_PROPERTY_ID;

    public static final java.lang.String HOST_TOTAL_MEM_PROPERTY_ID = (org.apache.ambari.server.controller.internal.HostResourceProvider.RESPONSE_KEY + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.HostResourceProvider.TOTAL_MEM_PROPERTY_ID;

    public static final java.lang.String HOST_ATTRIBUTES_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId(org.apache.ambari.server.controller.internal.HostResourceProvider.RESPONSE_KEY, org.apache.ambari.server.controller.internal.HostResourceProvider.ATTRIBUTES_PROPERTY_ID);

    public static final java.lang.String BLUEPRINT_PROPERTY_ID = "blueprint";

    public static final java.lang.String HOST_GROUP_PROPERTY_ID = "host_group";

    public static final java.lang.String HOST_COUNT_PROPERTY_ID = "host_count";

    public static final java.lang.String HOST_PREDICATE_PROPERTY_ID = "host_predicate";

    public static final java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> keyPropertyIds = com.google.common.collect.ImmutableMap.<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String>builder().put(org.apache.ambari.server.controller.spi.Resource.Type.Host, org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_HOST_NAME_PROPERTY_ID).put(org.apache.ambari.server.controller.spi.Resource.Type.Cluster, org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_CLUSTER_NAME_PROPERTY_ID).build();

    public static final java.util.Set<java.lang.String> propertyIds = com.google.common.collect.ImmutableSet.of(org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_CLUSTER_NAME_PROPERTY_ID, org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_CPU_COUNT_PROPERTY_ID, org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_DESIRED_CONFIGS_PROPERTY_ID, org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_DISK_INFO_PROPERTY_ID, org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_HOST_HEALTH_REPORT_PROPERTY_ID, org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_HOST_STATUS_PROPERTY_ID, org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_IP_PROPERTY_ID, org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_LAST_AGENT_ENV_PROPERTY_ID, org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_LAST_HEARTBEAT_TIME_PROPERTY_ID, org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_LAST_REGISTRATION_TIME_PROPERTY_ID, org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_MAINTENANCE_STATE_PROPERTY_ID, org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_HOST_NAME_PROPERTY_ID, org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_OS_ARCH_PROPERTY_ID, org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_OS_FAMILY_PROPERTY_ID, org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_OS_TYPE_PROPERTY_ID, org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_PHYSICAL_CPU_COUNT_PROPERTY_ID, org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_PUBLIC_NAME_PROPERTY_ID, org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_RACK_INFO_PROPERTY_ID, org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_RECOVERY_REPORT_PROPERTY_ID, org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_RECOVERY_SUMMARY_PROPERTY_ID, org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_STATE_PROPERTY_ID, org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_TOTAL_MEM_PROPERTY_ID, org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_ATTRIBUTES_PROPERTY_ID);

    @com.google.inject.Inject
    private org.apache.ambari.server.state.stack.OsFamily osFamily;

    @com.google.inject.Inject
    private static org.apache.ambari.server.topology.TopologyManager topologyManager;

    @com.google.inject.Inject
    private org.apache.ambari.server.agent.stomp.TopologyHolder topologyHolder;

    @com.google.inject.Inject
    private org.apache.ambari.server.agent.stomp.HostLevelParamsHolder hostLevelParamsHolder;

    @com.google.inject.Inject
    private org.apache.ambari.server.agent.RecoveryConfigHelper recoveryConfigHelper;

    @com.google.inject.assistedinject.AssistedInject
    HostResourceProvider(@com.google.inject.assistedinject.Assisted
    org.apache.ambari.server.controller.AmbariManagementController managementController) {
        super(org.apache.ambari.server.controller.spi.Resource.Type.Host, org.apache.ambari.server.controller.internal.HostResourceProvider.propertyIds, org.apache.ambari.server.controller.internal.HostResourceProvider.keyPropertyIds, managementController);
        java.util.Set<org.apache.ambari.server.security.authorization.RoleAuthorization> authorizationsAddDelete = java.util.EnumSet.of(org.apache.ambari.server.security.authorization.RoleAuthorization.HOST_ADD_DELETE_HOSTS);
        setRequiredCreateAuthorizations(authorizationsAddDelete);
        setRequiredDeleteAuthorizations(authorizationsAddDelete);
        setRequiredGetAuthorizations(org.apache.ambari.server.security.authorization.RoleAuthorization.AUTHORIZATIONS_VIEW_CLUSTER);
        setRequiredUpdateAuthorizations(org.apache.ambari.server.security.authorization.RoleAuthorization.AUTHORIZATIONS_UPDATE_CLUSTER);
    }

    @java.lang.Override
    protected org.apache.ambari.server.controller.spi.RequestStatus createResourcesAuthorized(final org.apache.ambari.server.controller.spi.Request request) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.ResourceAlreadyExistsException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        org.apache.ambari.server.controller.RequestStatusResponse createResponse = null;
        if (isHostGroupRequest(request)) {
            createResponse = submitHostRequests(request);
        } else {
            createResources(((org.apache.ambari.server.controller.internal.AbstractResourceProvider.Command<java.lang.Void>) (() -> {
                createHosts(request);
                return null;
            })));
        }
        notifyCreate(org.apache.ambari.server.controller.spi.Resource.Type.Host, request);
        return getRequestStatus(createResponse);
    }

    @java.lang.Override
    protected java.util.Set<org.apache.ambari.server.controller.spi.Resource> getResourcesAuthorized(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        final java.util.Set<org.apache.ambari.server.controller.HostRequest> requests = new java.util.HashSet<>();
        if (predicate == null) {
            requests.add(getRequest(null));
        } else {
            for (java.util.Map<java.lang.String, java.lang.Object> propertyMap : getPropertyMaps(predicate)) {
                requests.add(getRequest(propertyMap));
            }
        }
        java.util.Set<org.apache.ambari.server.controller.HostResponse> responses = getResources(() -> getHosts(requests));
        java.util.Set<java.lang.String> requestedIds = getRequestPropertyIds(request, predicate);
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = new java.util.HashSet<>();
        for (org.apache.ambari.server.controller.HostResponse response : responses) {
            org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Host);
            if ((response.getClusterName() != null) && (!response.getClusterName().isEmpty())) {
                org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_CLUSTER_NAME_PROPERTY_ID, response.getClusterName(), requestedIds);
            }
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_HOST_NAME_PROPERTY_ID, response.getHostname(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_PUBLIC_NAME_PROPERTY_ID, response.getPublicHostName(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_IP_PROPERTY_ID, response.getIpv4(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_TOTAL_MEM_PROPERTY_ID, response.getTotalMemBytes(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_CPU_COUNT_PROPERTY_ID, response.getCpuCount(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_PHYSICAL_CPU_COUNT_PROPERTY_ID, response.getPhCpuCount(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_OS_ARCH_PROPERTY_ID, response.getOsArch(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_OS_TYPE_PROPERTY_ID, response.getOsType(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_OS_FAMILY_PROPERTY_ID, response.getOsFamily(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_RACK_INFO_PROPERTY_ID, response.getRackInfo(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_LAST_HEARTBEAT_TIME_PROPERTY_ID, response.getLastHeartbeatTime(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_LAST_AGENT_ENV_PROPERTY_ID, response.getLastAgentEnv(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_LAST_REGISTRATION_TIME_PROPERTY_ID, response.getLastRegistrationTime(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_HOST_STATUS_PROPERTY_ID, response.getStatus(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_HOST_HEALTH_REPORT_PROPERTY_ID, response.getHealthReport(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_RECOVERY_REPORT_PROPERTY_ID, response.getRecoveryReport(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_RECOVERY_SUMMARY_PROPERTY_ID, response.getRecoverySummary(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_DISK_INFO_PROPERTY_ID, response.getDisksInfo(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_STATE_PROPERTY_ID, response.getHostState(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_DESIRED_CONFIGS_PROPERTY_ID, response.getDesiredHostConfigs(), requestedIds);
            if (null != response.getMaintenanceState()) {
                org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_MAINTENANCE_STATE_PROPERTY_ID, response.getMaintenanceState(), requestedIds);
            }
            resources.add(resource);
        }
        return resources;
    }

    @java.lang.Override
    protected org.apache.ambari.server.controller.spi.RequestStatus updateResourcesAuthorized(final org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        final java.util.Set<org.apache.ambari.server.controller.HostRequest> requests = new java.util.HashSet<>();
        for (java.util.Map<java.lang.String, java.lang.Object> propertyMap : getPropertyMaps(request.getProperties().iterator().next(), predicate)) {
            requests.add(getRequest(propertyMap));
        }
        modifyResources(((org.apache.ambari.server.controller.internal.AbstractResourceProvider.Command<java.lang.Void>) (() -> {
            updateHosts(requests);
            return null;
        })));
        notifyUpdate(org.apache.ambari.server.controller.spi.Resource.Type.Host, request, predicate);
        return getRequestStatus(null);
    }

    @java.lang.Override
    protected org.apache.ambari.server.controller.spi.RequestStatus deleteResourcesAuthorized(final org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        final java.util.Set<org.apache.ambari.server.controller.HostRequest> requests = new java.util.HashSet<>();
        for (java.util.Map<java.lang.String, java.lang.Object> propertyMap : getPropertyMaps(predicate)) {
            requests.add(getRequest(propertyMap));
        }
        org.apache.ambari.server.controller.internal.DeleteStatusMetaData deleteStatusMetaData = modifyResources(() -> deleteHosts(requests, request.isDryRunRequest()));
        if (!request.isDryRunRequest()) {
            notifyDelete(org.apache.ambari.server.controller.spi.Resource.Type.Host, predicate);
        }
        return getRequestStatus(null, null, deleteStatusMetaData);
    }

    @java.lang.Override
    public java.util.Set<java.lang.String> checkPropertyIds(java.util.Set<java.lang.String> propertyIds) {
        java.util.Set<java.lang.String> baseUnsupported = super.checkPropertyIds(propertyIds);
        baseUnsupported.remove(org.apache.ambari.server.controller.internal.HostResourceProvider.BLUEPRINT_PROPERTY_ID);
        baseUnsupported.remove(org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_GROUP_PROPERTY_ID);
        baseUnsupported.remove(org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_NAME_PROPERTY_ID);
        baseUnsupported.remove(org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_COUNT_PROPERTY_ID);
        baseUnsupported.remove(org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_PREDICATE_PROPERTY_ID);
        baseUnsupported.remove(org.apache.ambari.server.controller.internal.HostResourceProvider.RACK_INFO_PROPERTY_ID);
        return checkConfigPropertyIds(baseUnsupported, "Hosts");
    }

    @java.lang.Override
    protected java.util.Set<java.lang.String> getPKPropertyIds() {
        return new java.util.HashSet<>(org.apache.ambari.server.controller.internal.HostResourceProvider.keyPropertyIds.values());
    }

    private boolean isHostGroupRequest(org.apache.ambari.server.controller.spi.Request request) {
        boolean isHostGroupRequest = false;
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> properties = request.getProperties();
        if ((properties != null) && (!properties.isEmpty())) {
            java.lang.String hgName = ((java.lang.String) (properties.iterator().next().get(org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_GROUP_PROPERTY_ID)));
            isHostGroupRequest = (hgName != null) && (!hgName.isEmpty());
        }
        return isHostGroupRequest;
    }

    private org.apache.ambari.server.controller.HostRequest getRequest(java.util.Map<java.lang.String, java.lang.Object> properties) {
        if (properties == null) {
            return new org.apache.ambari.server.controller.HostRequest(null, null);
        }
        org.apache.ambari.server.controller.HostRequest hostRequest = new org.apache.ambari.server.controller.HostRequest(org.apache.ambari.server.controller.internal.HostResourceProvider.getHostNameFromProperties(properties), ((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_CLUSTER_NAME_PROPERTY_ID))));
        hostRequest.setPublicHostName(((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_PUBLIC_NAME_PROPERTY_ID))));
        java.lang.String rackInfo = ((java.lang.String) ((null != properties.get(org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_RACK_INFO_PROPERTY_ID)) ? properties.get(org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_RACK_INFO_PROPERTY_ID) : properties.get(org.apache.ambari.server.controller.internal.HostResourceProvider.RACK_INFO_PROPERTY_ID)));
        hostRequest.setRackInfo(rackInfo);
        hostRequest.setBlueprintName(((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.HostResourceProvider.BLUEPRINT_PROPERTY_ID))));
        hostRequest.setHostGroupName(((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_GROUP_PROPERTY_ID))));
        java.lang.Object o = properties.get(org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_MAINTENANCE_STATE_PROPERTY_ID);
        if (null != o) {
            hostRequest.setMaintenanceState(o.toString());
        }
        java.util.List<org.apache.ambari.server.controller.ConfigurationRequest> cr = org.apache.ambari.server.controller.internal.AbstractResourceProvider.getConfigurationRequests("Hosts", properties);
        hostRequest.setDesiredConfigs(cr);
        return hostRequest;
    }

    public synchronized void createHosts(org.apache.ambari.server.controller.spi.Request request) throws org.apache.ambari.server.AmbariException, org.apache.ambari.server.security.authorization.AuthorizationException {
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> propertySet = request.getProperties();
        if ((propertySet == null) || propertySet.isEmpty()) {
            org.apache.ambari.server.controller.internal.HostResourceProvider.LOG.warn("Received a create host request with no associated property sets");
            return;
        }
        org.apache.ambari.server.controller.AmbariManagementController controller = getManagementController();
        org.apache.ambari.server.state.Clusters clusters = controller.getClusters();
        java.util.Set<java.lang.String> duplicates = new java.util.HashSet<>();
        java.util.Set<java.lang.String> unknowns = new java.util.HashSet<>();
        java.util.Set<java.lang.String> allHosts = new java.util.HashSet<>();
        java.util.Set<org.apache.ambari.server.controller.HostRequest> hostRequests = new java.util.HashSet<>();
        for (java.util.Map<java.lang.String, java.lang.Object> propertyMap : propertySet) {
            org.apache.ambari.server.controller.HostRequest hostRequest = getRequest(propertyMap);
            hostRequests.add(hostRequest);
            if (!propertyMap.containsKey(org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_GROUP_PROPERTY_ID)) {
                createHostResource(clusters, duplicates, unknowns, allHosts, hostRequest);
            }
        }
        if (!duplicates.isEmpty()) {
            throw new java.lang.IllegalArgumentException(("Invalid request contains duplicate hostnames" + ", hostnames=") + java.lang.String.join(",", duplicates));
        }
        if (!unknowns.isEmpty()) {
            throw new java.lang.IllegalArgumentException(("Attempted to add unknown hosts to a cluster.  " + "These hosts have not been registered with the server: ") + java.lang.String.join(",", unknowns));
        }
        java.util.Map<java.lang.String, java.util.Set<java.lang.String>> hostClustersMap = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> hostAttributes = new java.util.HashMap<>();
        java.util.Set<java.lang.String> allClusterSet = new java.util.HashSet<>();
        java.util.TreeMap<java.lang.String, org.apache.ambari.server.agent.stomp.dto.TopologyCluster> addedTopologies = new java.util.TreeMap<>();
        java.util.List<org.apache.ambari.server.events.HostLevelParamsUpdateEvent> hostLevelParamsUpdateEvents = new java.util.ArrayList<>();
        for (org.apache.ambari.server.controller.HostRequest hostRequest : hostRequests) {
            if ((((hostRequest.getHostname() != null) && (!hostRequest.getHostname().isEmpty())) && (hostRequest.getClusterName() != null)) && (!hostRequest.getClusterName().isEmpty())) {
                java.util.Set<java.lang.String> clusterSet = new java.util.HashSet<>();
                clusterSet.add(hostRequest.getClusterName());
                allClusterSet.add(hostRequest.getClusterName());
                hostClustersMap.put(hostRequest.getHostname(), clusterSet);
                org.apache.ambari.server.state.Cluster cl = clusters.getCluster(hostRequest.getClusterName());
                java.lang.String clusterId = java.lang.Long.toString(cl.getClusterId());
                if (!addedTopologies.containsKey(clusterId)) {
                    addedTopologies.put(clusterId, new org.apache.ambari.server.agent.stomp.dto.TopologyCluster());
                }
                org.apache.ambari.server.state.Host addedHost = clusters.getHost(hostRequest.getHostname());
                addedTopologies.get(clusterId).addTopologyHost(new org.apache.ambari.server.agent.stomp.dto.TopologyHost(addedHost.getHostId(), addedHost.getHostName(), addedHost.getRackInfo(), addedHost.getIPv4()));
                org.apache.ambari.server.events.HostLevelParamsUpdateEvent hostLevelParamsUpdateEvent = new org.apache.ambari.server.events.HostLevelParamsUpdateEvent(addedHost.getHostId(), clusterId, new org.apache.ambari.server.agent.stomp.dto.HostLevelParamsCluster(recoveryConfigHelper.getRecoveryConfig(cl.getClusterName(), addedHost.getHostName()), getManagementController().getBlueprintProvisioningStates(cl.getClusterId(), addedHost.getHostId())));
                hostLevelParamsUpdateEvents.add(hostLevelParamsUpdateEvent);
            }
        }
        clusters.updateHostWithClusterAndAttributes(hostClustersMap, hostAttributes);
        updateHostRackInfoIfChanged(clusters, hostRequests);
        for (org.apache.ambari.server.events.HostLevelParamsUpdateEvent hostLevelParamsUpdateEvent : hostLevelParamsUpdateEvents) {
            hostLevelParamsHolder.updateData(hostLevelParamsUpdateEvent);
        }
        org.apache.ambari.server.events.TopologyUpdateEvent topologyUpdateEvent = new org.apache.ambari.server.events.TopologyUpdateEvent(addedTopologies, org.apache.ambari.server.events.UpdateEventType.UPDATE);
        topologyHolder.updateData(topologyUpdateEvent);
    }

    private void updateHostRackInfoIfChanged(org.apache.ambari.server.state.Clusters clusters, java.util.Set<org.apache.ambari.server.controller.HostRequest> hostRequests) throws org.apache.ambari.server.AmbariException, org.apache.ambari.server.security.authorization.AuthorizationException {
        java.util.HashSet<java.lang.String> rackChangeAffectedClusters = new java.util.HashSet<>();
        for (org.apache.ambari.server.controller.HostRequest hostRequest : hostRequests) {
            java.lang.String clusterName = hostRequest.getClusterName();
            if (org.apache.commons.lang.StringUtils.isNotBlank(clusterName)) {
                org.apache.ambari.server.state.Cluster cluster = clusters.getCluster(clusterName);
                org.apache.ambari.server.state.Host host = clusters.getHost(hostRequest.getHostname());
                if (updateHostRackInfoIfChanged(cluster, host, hostRequest))
                    rackChangeAffectedClusters.add(clusterName);

            }
        }
        for (java.lang.String clusterName : rackChangeAffectedClusters) {
            getManagementController().registerRackChange(clusterName);
        }
    }

    private boolean updateHostRackInfoIfChanged(org.apache.ambari.server.state.Cluster cluster, org.apache.ambari.server.state.Host host, org.apache.ambari.server.controller.HostRequest hostRequest) throws org.apache.ambari.server.AmbariException, org.apache.ambari.server.security.authorization.AuthorizationException {
        java.lang.Long resourceId = cluster.getResourceId();
        java.lang.String hostRackInfo = host.getRackInfo();
        java.lang.String requestRackInfo = hostRequest.getRackInfo();
        boolean rackChange = (requestRackInfo != null) && (!requestRackInfo.equals(hostRackInfo));
        if (rackChange) {
            if (!org.apache.ambari.server.security.authorization.AuthorizationHelper.isAuthorized(org.apache.ambari.server.security.authorization.ResourceType.CLUSTER, resourceId, org.apache.ambari.server.security.authorization.RoleAuthorization.HOST_ADD_DELETE_HOSTS)) {
                throw new org.apache.ambari.server.security.authorization.AuthorizationException("The authenticated user is not authorized to update host rack information");
            }
            host.setRackInfo(requestRackInfo);
        }
        return rackChange;
    }

    private void createHostResource(org.apache.ambari.server.state.Clusters clusters, java.util.Set<java.lang.String> duplicates, java.util.Set<java.lang.String> unknowns, java.util.Set<java.lang.String> allHosts, org.apache.ambari.server.controller.HostRequest request) throws org.apache.ambari.server.AmbariException {
        if ((request.getHostname() == null) || request.getHostname().isEmpty()) {
            throw new java.lang.IllegalArgumentException("Invalid arguments, hostname" + " cannot be null");
        }
        if (org.apache.ambari.server.controller.internal.HostResourceProvider.LOG.isDebugEnabled()) {
            org.apache.ambari.server.controller.internal.HostResourceProvider.LOG.debug("Received a createHost request, hostname={}, request={}", request.getHostname(), request);
        }
        if (allHosts.contains(request.getHostname())) {
            duplicates.add(request.getHostname());
            return;
        }
        allHosts.add(request.getHostname());
        try {
            clusters.getHost(request.getHostname());
        } catch (org.apache.ambari.server.HostNotFoundException e) {
            unknowns.add(request.getHostname());
            return;
        }
        if (request.getClusterName() != null) {
            try {
                clusters.getCluster(request.getClusterName());
            } catch (org.apache.ambari.server.ClusterNotFoundException e) {
                throw new org.apache.ambari.server.ParentObjectNotFoundException(("Attempted to add a host to a cluster which doesn't exist: " + " clusterName=") + request.getClusterName());
            }
        }
    }

    public org.apache.ambari.server.controller.RequestStatusResponse install(final java.lang.String cluster, final java.lang.String hostname, java.util.Collection<java.lang.String> skipInstallForComponents, java.util.Collection<java.lang.String> dontSkipInstallForComponents, final boolean skipFailure, boolean useClusterHostInfo) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException {
        return ((org.apache.ambari.server.controller.internal.HostComponentResourceProvider) (org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.getResourceProvider(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent))).install(cluster, hostname, skipInstallForComponents, dontSkipInstallForComponents, skipFailure, useClusterHostInfo);
    }

    public org.apache.ambari.server.controller.RequestStatusResponse start(final java.lang.String cluster, final java.lang.String hostname) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException {
        return ((org.apache.ambari.server.controller.internal.HostComponentResourceProvider) (org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.getResourceProvider(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent))).start(cluster, hostname);
    }

    protected java.util.Set<org.apache.ambari.server.controller.HostResponse> getHosts(java.util.Set<org.apache.ambari.server.controller.HostRequest> requests) throws org.apache.ambari.server.AmbariException {
        java.util.Set<org.apache.ambari.server.controller.HostResponse> response = new java.util.HashSet<>();
        org.apache.ambari.server.controller.AmbariManagementController controller = getManagementController();
        for (org.apache.ambari.server.controller.HostRequest request : requests) {
            try {
                response.addAll(org.apache.ambari.server.controller.internal.HostResourceProvider.getHosts(controller, request, osFamily));
            } catch (org.apache.ambari.server.HostNotFoundException e) {
                if (requests.size() == 1) {
                    throw e;
                }
            }
        }
        return response;
    }

    protected static java.util.Set<org.apache.ambari.server.controller.HostResponse> getHosts(org.apache.ambari.server.controller.AmbariManagementController controller, org.apache.ambari.server.controller.HostRequest request, org.apache.ambari.server.state.stack.OsFamily osFamily) throws org.apache.ambari.server.AmbariException {
        java.util.List<org.apache.ambari.server.state.Host> hosts;
        java.util.Set<org.apache.ambari.server.controller.HostResponse> response = new java.util.HashSet<>();
        org.apache.ambari.server.state.Cluster cluster = null;
        org.apache.ambari.server.state.Clusters clusters = controller.getClusters();
        java.lang.String clusterName = request.getClusterName();
        java.lang.String hostName = request.getHostname();
        if (clusterName != null) {
            try {
                cluster = clusters.getCluster(clusterName);
            } catch (org.apache.ambari.server.ObjectNotFoundException e) {
                throw new org.apache.ambari.server.ParentObjectNotFoundException("Parent Cluster resource doesn't exist", e);
            }
        }
        if (hostName == null) {
            hosts = clusters.getHosts();
        } else {
            hosts = new java.util.ArrayList<>();
            try {
                hosts.add(clusters.getHost(request.getHostname()));
            } catch (org.apache.ambari.server.HostNotFoundException e) {
                throw new org.apache.ambari.server.HostNotFoundException(clusterName, hostName);
            }
        }
        java.util.Map<java.lang.String, org.apache.ambari.server.state.DesiredConfig> desiredConfigs = null;
        if (null != cluster) {
            desiredConfigs = cluster.getDesiredConfigs();
        }
        for (org.apache.ambari.server.state.Host h : hosts) {
            if (clusterName != null) {
                if (clusters.getClustersForHost(h.getHostName()).contains(cluster)) {
                    org.apache.ambari.server.controller.HostResponse r = h.convertToResponse();
                    r.setClusterName(clusterName);
                    r.setDesiredHostConfigs(h.getDesiredHostConfigs(cluster, desiredConfigs));
                    r.setMaintenanceState(h.getMaintenanceState(cluster.getClusterId()));
                    if (osFamily != null) {
                        java.lang.String hostOsFamily = osFamily.find(r.getOsType());
                        if (hostOsFamily == null) {
                            org.apache.ambari.server.controller.internal.HostResourceProvider.LOG.error("Can not find host OS family. For OS type = '{}' and host name = '{}'", r.getOsType(), r.getHostname());
                        }
                        r.setOsFamily(hostOsFamily);
                    }
                    response.add(r);
                } else if (hostName != null) {
                    throw new org.apache.ambari.server.HostNotFoundException(clusterName, hostName);
                }
            } else {
                org.apache.ambari.server.controller.HostResponse r = h.convertToResponse();
                java.util.Set<org.apache.ambari.server.state.Cluster> clustersForHost = clusters.getClustersForHost(h.getHostName());
                if ((clustersForHost != null) && (clustersForHost.size() != 0)) {
                    org.apache.ambari.server.state.Cluster clusterForHost = clustersForHost.iterator().next();
                    r.setClusterName(clusterForHost.getClusterName());
                    r.setDesiredHostConfigs(h.getDesiredHostConfigs(clusterForHost, null));
                    r.setMaintenanceState(h.getMaintenanceState(clusterForHost.getClusterId()));
                }
                response.add(r);
            }
        }
        return response;
    }

    protected synchronized void updateHosts(java.util.Set<org.apache.ambari.server.controller.HostRequest> requests) throws org.apache.ambari.server.AmbariException, org.apache.ambari.server.security.authorization.AuthorizationException {
        if (requests.isEmpty()) {
            org.apache.ambari.server.controller.internal.HostResourceProvider.LOG.warn("Received an empty requests set");
            return;
        }
        org.apache.ambari.server.controller.AmbariManagementController controller = getManagementController();
        org.apache.ambari.server.state.Clusters clusters = controller.getClusters();
        for (org.apache.ambari.server.controller.HostRequest request : requests) {
            if ((request.getHostname() == null) || request.getHostname().isEmpty()) {
                throw new java.lang.IllegalArgumentException("Invalid arguments, hostname should be provided");
            }
        }
        java.util.TreeMap<java.lang.String, org.apache.ambari.server.agent.stomp.dto.TopologyCluster> topologyUpdates = new java.util.TreeMap<>();
        for (org.apache.ambari.server.controller.HostRequest request : requests) {
            if (org.apache.ambari.server.controller.internal.HostResourceProvider.LOG.isDebugEnabled()) {
                org.apache.ambari.server.controller.internal.HostResourceProvider.LOG.debug("Received an updateHost request, hostname={}, request={}", request.getHostname(), request);
            }
            org.apache.ambari.server.state.Host host = clusters.getHost(request.getHostname());
            java.lang.String clusterName = request.getClusterName();
            org.apache.ambari.server.state.Cluster cluster = clusters.getCluster(clusterName);
            java.lang.Long clusterId = cluster.getClusterId();
            java.lang.Long resourceId = cluster.getResourceId();
            org.apache.ambari.server.agent.stomp.dto.TopologyHost topologyHost = new org.apache.ambari.server.agent.stomp.dto.TopologyHost(host.getHostId(), host.getHostName());
            try {
                clusters.mapAndPublishHostsToCluster(new java.util.HashSet<>(java.util.Arrays.asList(request.getHostname())), clusterName);
            } catch (org.apache.ambari.server.DuplicateResourceException e) {
            }
            boolean rackChange = updateHostRackInfoIfChanged(cluster, host, request);
            if (rackChange) {
                topologyHost.setRackName(host.getRackInfo());
            }
            if (null != request.getPublicHostName()) {
                if (!org.apache.ambari.server.security.authorization.AuthorizationHelper.isAuthorized(org.apache.ambari.server.security.authorization.ResourceType.CLUSTER, resourceId, org.apache.ambari.server.security.authorization.RoleAuthorization.HOST_ADD_DELETE_HOSTS)) {
                    throw new org.apache.ambari.server.security.authorization.AuthorizationException("The authenticated user is not authorized to update host attributes");
                }
                host.setPublicHostName(request.getPublicHostName());
                topologyHost.setHostName(request.getPublicHostName());
            }
            if ((null != clusterName) && (null != request.getMaintenanceState())) {
                if (!org.apache.ambari.server.security.authorization.AuthorizationHelper.isAuthorized(org.apache.ambari.server.security.authorization.ResourceType.CLUSTER, resourceId, org.apache.ambari.server.security.authorization.RoleAuthorization.HOST_TOGGLE_MAINTENANCE)) {
                    throw new org.apache.ambari.server.security.authorization.AuthorizationException("The authenticated user is not authorized to update host maintenance state");
                }
                org.apache.ambari.server.state.MaintenanceState newState = org.apache.ambari.server.state.MaintenanceState.valueOf(request.getMaintenanceState());
                org.apache.ambari.server.state.MaintenanceState oldState = host.getMaintenanceState(clusterId);
                if (!newState.equals(oldState)) {
                    if (newState.equals(org.apache.ambari.server.state.MaintenanceState.IMPLIED_FROM_HOST) || newState.equals(org.apache.ambari.server.state.MaintenanceState.IMPLIED_FROM_SERVICE)) {
                        throw new java.lang.IllegalArgumentException(("Invalid arguments, can only set " + "maintenance state to one of ") + java.util.EnumSet.of(org.apache.ambari.server.state.MaintenanceState.OFF, org.apache.ambari.server.state.MaintenanceState.ON));
                    } else {
                        host.setMaintenanceState(clusterId, newState);
                    }
                }
            }
            if ((null != clusterName) && (null != request.getDesiredConfigs())) {
                if (clusters.getHostsForCluster(clusterName).containsKey(host.getHostName())) {
                    for (org.apache.ambari.server.controller.ConfigurationRequest cr : request.getDesiredConfigs()) {
                        if ((null != cr.getProperties()) && (cr.getProperties().size() > 0)) {
                            org.apache.ambari.server.controller.internal.HostResourceProvider.LOG.info(java.text.MessageFormat.format("Applying configuration with tag ''{0}'' to host ''{1}'' in cluster ''{2}''", cr.getVersionTag(), request.getHostname(), clusterName));
                            cr.setClusterName(cluster.getClusterName());
                            controller.createConfiguration(cr);
                        }
                        org.apache.ambari.server.state.Config baseConfig = cluster.getConfig(cr.getType(), cr.getVersionTag());
                        if (null != baseConfig) {
                            java.lang.String authName = controller.getAuthName();
                            org.apache.ambari.server.state.DesiredConfig oldConfig = host.getDesiredConfigs(clusterId).get(cr.getType());
                            if (host.addDesiredConfig(clusterId, cr.isSelected(), authName, baseConfig)) {
                                org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger("configchange");
                                logger.info(((((((((((((((((("(configchange) cluster '" + cluster.getClusterName()) + "', ") + "host '") + host.getHostName()) + "' ") + "changed by: '") + authName) + "'; ") + "type='") + baseConfig.getType()) + "' ") + "version='") + baseConfig.getVersion()) + "'") + "tag='") + baseConfig.getTag()) + "'") + (null == oldConfig ? "" : (", from='" + oldConfig.getTag()) + "'"));
                            }
                        }
                    }
                }
            }
            if (org.apache.commons.lang.StringUtils.isNotBlank(clusterName) && rackChange) {
                controller.registerRackChange(clusterName);
            }
            if (!topologyUpdates.containsKey(clusterId.toString())) {
                topologyUpdates.put(clusterId.toString(), new org.apache.ambari.server.agent.stomp.dto.TopologyCluster());
            }
            topologyUpdates.get(clusterId.toString()).addTopologyHost(topologyHost);
            org.apache.ambari.server.events.TopologyUpdateEvent topologyUpdateEvent = new org.apache.ambari.server.events.TopologyUpdateEvent(topologyUpdates, org.apache.ambari.server.events.UpdateEventType.UPDATE);
            topologyHolder.updateData(topologyUpdateEvent);
        }
    }

    @com.google.inject.persist.Transactional
    protected org.apache.ambari.server.controller.internal.DeleteStatusMetaData deleteHosts(java.util.Set<org.apache.ambari.server.controller.HostRequest> requests, boolean dryRun) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.controller.AmbariManagementController controller = getManagementController();
        org.apache.ambari.server.state.Clusters clusters = controller.getClusters();
        org.apache.ambari.server.controller.internal.DeleteStatusMetaData deleteStatusMetaData = new org.apache.ambari.server.controller.internal.DeleteStatusMetaData();
        java.util.List<org.apache.ambari.server.controller.HostRequest> okToRemove = new java.util.ArrayList<>();
        for (org.apache.ambari.server.controller.HostRequest hostRequest : requests) {
            java.lang.String hostName = hostRequest.getHostname();
            if (null == hostName) {
                continue;
            }
            try {
                validateHostInDeleteFriendlyState(hostRequest, clusters);
                okToRemove.add(hostRequest);
            } catch (java.lang.Exception ex) {
                deleteStatusMetaData.addException(hostName, ex);
            }
        }
        if (dryRun) {
            for (org.apache.ambari.server.controller.HostRequest request : okToRemove) {
                deleteStatusMetaData.addDeletedKey(request.getHostname());
            }
        } else {
            processDeleteHostRequests(okToRemove, clusters, deleteStatusMetaData);
        }
        if (!dryRun) {
            if ((deleteStatusMetaData.getDeletedKeys().size() + deleteStatusMetaData.getExceptionForKeys().size()) == 1) {
                if (deleteStatusMetaData.getDeletedKeys().size() == 1) {
                    return null;
                }
                for (java.util.Map.Entry<java.lang.String, java.lang.Exception> entry : deleteStatusMetaData.getExceptionForKeys().entrySet()) {
                    java.lang.Exception ex = entry.getValue();
                    if (ex instanceof org.apache.ambari.server.AmbariException) {
                        throw ((org.apache.ambari.server.AmbariException) (ex));
                    } else {
                        throw new org.apache.ambari.server.AmbariException(ex.getMessage(), ex);
                    }
                }
            }
        }
        return deleteStatusMetaData;
    }

    private void processDeleteHostRequests(java.util.List<org.apache.ambari.server.controller.HostRequest> requests, org.apache.ambari.server.state.Clusters clusters, org.apache.ambari.server.controller.internal.DeleteStatusMetaData deleteStatusMetaData) throws org.apache.ambari.server.AmbariException {
        java.util.Set<java.lang.String> hostsClusters = new java.util.HashSet<>();
        java.util.Set<java.lang.String> hostNames = new java.util.HashSet<>();
        java.util.Set<java.lang.Long> hostIds = new java.util.HashSet<>();
        java.util.TreeMap<java.lang.String, org.apache.ambari.server.agent.stomp.dto.TopologyCluster> topologyUpdates = new java.util.TreeMap<>();
        for (org.apache.ambari.server.controller.HostRequest hostRequest : requests) {
            java.lang.String hostname = hostRequest.getHostname();
            java.lang.Long hostId = clusters.getHost(hostname).getHostId();
            hostNames.add(hostname);
            hostIds.add(hostId);
            if (hostRequest.getClusterName() != null) {
                hostsClusters.add(hostRequest.getClusterName());
            }
            org.apache.ambari.server.controller.internal.HostResourceProvider.LOG.info("Received Delete request for host {} from cluster {}.", hostname, hostRequest.getClusterName());
            java.util.Set<org.apache.ambari.server.controller.ServiceComponentHostRequest> schrs = new java.util.HashSet<>();
            for (org.apache.ambari.server.state.Cluster cluster : clusters.getClustersForHost(hostname)) {
                java.util.List<org.apache.ambari.server.state.ServiceComponentHost> list = cluster.getServiceComponentHosts(hostname);
                for (org.apache.ambari.server.state.ServiceComponentHost sch : list) {
                    org.apache.ambari.server.controller.ServiceComponentHostRequest schr = new org.apache.ambari.server.controller.ServiceComponentHostRequest(cluster.getClusterName(), sch.getServiceName(), sch.getServiceComponentName(), sch.getHostName(), null);
                    schrs.add(schr);
                }
            }
            org.apache.ambari.server.controller.internal.DeleteStatusMetaData componentDeleteStatus = null;
            if (schrs.size() > 0) {
                try {
                    componentDeleteStatus = getManagementController().deleteHostComponents(schrs);
                } catch (java.lang.Exception ex) {
                    deleteStatusMetaData.addException(hostname, ex);
                }
            }
            if (componentDeleteStatus != null) {
                for (java.lang.String key : componentDeleteStatus.getDeletedKeys()) {
                    deleteStatusMetaData.addDeletedKey(key);
                }
                for (java.lang.String key : componentDeleteStatus.getExceptionForKeys().keySet()) {
                    deleteStatusMetaData.addException(key, componentDeleteStatus.getExceptionForKeys().get(key));
                }
            }
            if (hostRequest.getClusterName() != null) {
                hostsClusters.add(hostRequest.getClusterName());
            }
            try {
                java.util.Set<org.apache.ambari.server.state.Cluster> hostClusters = new java.util.HashSet<>(clusters.getClustersForHost(hostname));
                clusters.deleteHost(hostname);
                for (org.apache.ambari.server.state.Cluster cluster : hostClusters) {
                    java.lang.String clusterId = java.lang.Long.toString(cluster.getClusterId());
                    if (!topologyUpdates.containsKey(clusterId)) {
                        topologyUpdates.put(clusterId, new org.apache.ambari.server.agent.stomp.dto.TopologyCluster());
                    }
                    topologyUpdates.get(clusterId).getTopologyHosts().add(new org.apache.ambari.server.agent.stomp.dto.TopologyHost(hostId, hostname));
                }
                deleteStatusMetaData.addDeletedKey(hostname);
            } catch (java.lang.Exception ex) {
                deleteStatusMetaData.addException(hostname, ex);
            }
            removeHostFromClusterTopology(clusters, hostRequest);
            for (org.apache.ambari.server.topology.LogicalRequest logicalRequest : org.apache.ambari.server.controller.internal.HostResourceProvider.topologyManager.getRequests(java.util.Collections.emptyList())) {
                logicalRequest.removeHostRequestByHostName(hostname);
            }
        }
        clusters.publishHostsDeletion(hostIds, hostNames);
        org.apache.ambari.server.events.TopologyUpdateEvent topologyUpdateEvent = new org.apache.ambari.server.events.TopologyUpdateEvent(topologyUpdates, org.apache.ambari.server.events.UpdateEventType.DELETE);
        topologyHolder.updateData(topologyUpdateEvent);
    }

    private void validateHostInDeleteFriendlyState(org.apache.ambari.server.controller.HostRequest hostRequest, org.apache.ambari.server.state.Clusters clusters) throws org.apache.ambari.server.AmbariException {
        java.util.Set<java.lang.String> clusterNamesForHost = new java.util.HashSet<>();
        java.lang.String hostName = hostRequest.getHostname();
        if (null != hostRequest.getClusterName()) {
            clusterNamesForHost.add(hostRequest.getClusterName());
        } else {
            java.util.Set<org.apache.ambari.server.state.Cluster> clustersForHost = clusters.getClustersForHost(hostRequest.getHostname());
            if (null != clustersForHost) {
                for (org.apache.ambari.server.state.Cluster c : clustersForHost) {
                    clusterNamesForHost.add(c.getClusterName());
                }
            }
        }
        for (java.lang.String clusterName : clusterNamesForHost) {
            org.apache.ambari.server.state.Cluster cluster = clusters.getCluster(clusterName);
            java.util.List<org.apache.ambari.server.state.ServiceComponentHost> list = cluster.getServiceComponentHosts(hostName);
            if (!list.isEmpty()) {
                java.util.List<java.lang.String> componentsStarted = new java.util.ArrayList<>();
                for (org.apache.ambari.server.state.ServiceComponentHost sch : list) {
                    if (!sch.canBeRemoved()) {
                        componentsStarted.add(sch.getServiceComponentName());
                    }
                }
                if (!componentsStarted.isEmpty()) {
                    java.lang.StringBuilder reason = new java.lang.StringBuilder("Cannot remove host ").append(hostName).append(" from ").append(hostRequest.getClusterName()).append(".  The following roles exist, and these components are not in the removable state: ");
                    reason.append(org.apache.commons.lang.StringUtils.join(componentsStarted, ", "));
                    throw new org.apache.ambari.server.AmbariException(reason.toString());
                }
            }
        }
    }

    private void removeHostFromClusterTopology(org.apache.ambari.server.state.Clusters clusters, org.apache.ambari.server.controller.HostRequest hostRequest) throws org.apache.ambari.server.AmbariException {
        if (hostRequest.getClusterName() == null) {
            for (org.apache.ambari.server.state.Cluster c : clusters.getClusters().values()) {
                removeHostFromClusterTopology(c.getClusterId(), hostRequest.getHostname());
            }
        } else {
            long clusterId = clusters.getCluster(hostRequest.getClusterName()).getClusterId();
            removeHostFromClusterTopology(clusterId, hostRequest.getHostname());
        }
    }

    private void removeHostFromClusterTopology(long clusterId, java.lang.String hostname) {
        org.apache.ambari.server.topology.ClusterTopology clusterTopology = org.apache.ambari.server.controller.internal.HostResourceProvider.topologyManager.getClusterTopology(clusterId);
        if (clusterTopology != null) {
            clusterTopology.removeHost(hostname);
        }
    }

    public static java.lang.String getHostNameFromProperties(java.util.Map<java.lang.String, java.lang.Object> properties) {
        java.lang.String hostname = ((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_HOST_NAME_PROPERTY_ID)));
        return hostname != null ? hostname : ((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_NAME_PROPERTY_ID)));
    }

    private org.apache.ambari.server.controller.RequestStatusResponse submitHostRequests(org.apache.ambari.server.controller.spi.Request request) throws org.apache.ambari.server.controller.spi.SystemException {
        org.apache.ambari.server.controller.internal.ScaleClusterRequest requestRequest;
        try {
            requestRequest = new org.apache.ambari.server.controller.internal.ScaleClusterRequest(request.getProperties());
        } catch (org.apache.ambari.server.topology.InvalidTopologyTemplateException e) {
            throw new java.lang.IllegalArgumentException("Invalid Add Hosts Template: " + e, e);
        }
        try {
            return org.apache.ambari.server.controller.internal.HostResourceProvider.topologyManager.scaleHosts(requestRequest);
        } catch (org.apache.ambari.server.topology.InvalidTopologyException e) {
            throw new java.lang.IllegalArgumentException("Topology validation failed: " + e, e);
        } catch (org.apache.ambari.server.AmbariException e) {
            e.printStackTrace();
            throw new org.apache.ambari.server.controller.spi.SystemException("Unable to add hosts", e);
        }
    }

    public static void setTopologyManager(org.apache.ambari.server.topology.TopologyManager topologyManager) {
        org.apache.ambari.server.controller.internal.HostResourceProvider.topologyManager = topologyManager;
    }
}