package org.apache.ambari.server.controller.internal;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
@org.apache.ambari.server.StaticallyInject
public class ConfigGroupResourceProvider extends org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider implements org.apache.ambari.server.controller.spi.ResourcePredicateEvaluator {
    private static final org.slf4j.Logger configLogger = org.slf4j.LoggerFactory.getLogger("configchange");

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.class);

    public static final java.lang.String CONFIG_GROUP = "ConfigGroup";

    public static final java.lang.String CLUSTER_NAME_PROPERTY_ID = "cluster_name";

    public static final java.lang.String ID_PROPERTY_ID = "id";

    public static final java.lang.String GROUP_NAME_PROPERTY_ID = "group_name";

    public static final java.lang.String TAG_PROPERTY_ID = "tag";

    public static final java.lang.String SERVICE_NAME_PROPERTY_ID = "service_name";

    public static final java.lang.String DESCRIPTION_PROPERTY_ID = "description";

    public static final java.lang.String SERVICE_CONFIG_VERSION_NOTE_PROPERTY_ID = "service_config_version_note";

    public static final java.lang.String HOST_NAME_PROPERTY_ID = "host_name";

    public static final java.lang.String HOSTS_HOSTNAME_PROPERTY_ID = "hosts/host_name";

    public static final java.lang.String HOSTS_PROPERTY_ID = "hosts";

    public static final java.lang.String DESIRED_CONFIGS_PROPERTY_ID = "desired_configs";

    public static final java.lang.String VERSION_TAGS_PROPERTY_ID = "version_tags";

    public static final java.lang.String CLUSTER_NAME = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId(org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.CONFIG_GROUP, org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.CLUSTER_NAME_PROPERTY_ID);

    public static final java.lang.String ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId(org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.CONFIG_GROUP, org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.ID_PROPERTY_ID);

    public static final java.lang.String GROUP_NAME = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId(org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.CONFIG_GROUP, org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.GROUP_NAME_PROPERTY_ID);

    public static final java.lang.String TAG = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId(org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.CONFIG_GROUP, org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.TAG_PROPERTY_ID);

    public static final java.lang.String SERVICE_NAME = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId(org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.CONFIG_GROUP, org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.SERVICE_NAME_PROPERTY_ID);

    public static final java.lang.String DESCRIPTION = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId(org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.CONFIG_GROUP, org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.DESCRIPTION_PROPERTY_ID);

    public static final java.lang.String SERVICE_CONFIG_VERSION_NOTE = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId(org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.CONFIG_GROUP, org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.SERVICE_CONFIG_VERSION_NOTE_PROPERTY_ID);

    public static final java.lang.String HOST_NAME = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId(null, org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.HOST_NAME_PROPERTY_ID);

    public static final java.lang.String HOSTS_HOST_NAME = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId(org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.CONFIG_GROUP, org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.HOSTS_HOSTNAME_PROPERTY_ID);

    public static final java.lang.String HOSTS = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId(org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.CONFIG_GROUP, org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.HOSTS_PROPERTY_ID);

    public static final java.lang.String DESIRED_CONFIGS = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId(org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.CONFIG_GROUP, org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.DESIRED_CONFIGS_PROPERTY_ID);

    public static final java.lang.String VERSION_TAGS = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId(org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.CONFIG_GROUP, org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.VERSION_TAGS_PROPERTY_ID);

    private static final java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> keyPropertyIds = com.google.common.collect.ImmutableMap.<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String>builder().put(org.apache.ambari.server.controller.spi.Resource.Type.Cluster, org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.CLUSTER_NAME).put(org.apache.ambari.server.controller.spi.Resource.Type.ConfigGroup, org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.ID).build();

    private static final java.util.Set<java.lang.String> propertyIds = com.google.common.collect.Sets.newHashSet(org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.CLUSTER_NAME, org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.ID, org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.GROUP_NAME, org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.TAG, org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.SERVICE_NAME, org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.DESCRIPTION, org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.SERVICE_CONFIG_VERSION_NOTE, org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.HOST_NAME, org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.HOSTS_HOST_NAME, org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.HOSTS, org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.DESIRED_CONFIGS, org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.VERSION_TAGS);

    @com.google.inject.Inject
    private static org.apache.ambari.server.orm.dao.HostDAO hostDAO;

    @com.google.inject.Inject
    private static org.apache.ambari.server.state.ConfigFactory configFactory;

    @com.google.inject.Inject
    private static com.google.inject.Provider<org.apache.ambari.server.state.ConfigHelper> m_configHelper;

    protected ConfigGroupResourceProvider(org.apache.ambari.server.controller.AmbariManagementController managementController) {
        super(org.apache.ambari.server.controller.spi.Resource.Type.ConfigGroup, org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.propertyIds, org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.keyPropertyIds, managementController);
        java.util.EnumSet<org.apache.ambari.server.security.authorization.RoleAuthorization> manageGroupsAuthSet = java.util.EnumSet.of(org.apache.ambari.server.security.authorization.RoleAuthorization.SERVICE_MANAGE_CONFIG_GROUPS, org.apache.ambari.server.security.authorization.RoleAuthorization.CLUSTER_MANAGE_CONFIG_GROUPS);
        setRequiredCreateAuthorizations(manageGroupsAuthSet);
        setRequiredDeleteAuthorizations(manageGroupsAuthSet);
        setRequiredUpdateAuthorizations(manageGroupsAuthSet);
        setRequiredGetAuthorizations(java.util.EnumSet.of(org.apache.ambari.server.security.authorization.RoleAuthorization.CLUSTER_VIEW_CONFIGS, org.apache.ambari.server.security.authorization.RoleAuthorization.CLUSTER_MANAGE_CONFIG_GROUPS, org.apache.ambari.server.security.authorization.RoleAuthorization.SERVICE_VIEW_CONFIGS, org.apache.ambari.server.security.authorization.RoleAuthorization.SERVICE_MANAGE_CONFIG_GROUPS, org.apache.ambari.server.security.authorization.RoleAuthorization.SERVICE_COMPARE_CONFIGS));
    }

    @java.lang.Override
    protected java.util.Set<java.lang.String> getPKPropertyIds() {
        return new java.util.HashSet<>(org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.keyPropertyIds.values());
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus createResourcesAuthorized(org.apache.ambari.server.controller.spi.Request request) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.ResourceAlreadyExistsException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        final java.util.Set<org.apache.ambari.server.controller.ConfigGroupRequest> requests = new java.util.HashSet<>();
        for (java.util.Map<java.lang.String, java.lang.Object> propertyMap : request.getProperties()) {
            requests.add(getConfigGroupRequest(propertyMap));
        }
        org.apache.ambari.server.controller.spi.RequestStatus status = createResources(requests);
        notifyCreate(org.apache.ambari.server.controller.spi.Resource.Type.ConfigGroup, request);
        return status;
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.controller.spi.Resource> getResourcesAuthorized(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        final java.util.Set<org.apache.ambari.server.controller.ConfigGroupRequest> requests = new java.util.HashSet<>();
        for (java.util.Map<java.lang.String, java.lang.Object> propertyMap : getPropertyMaps(predicate)) {
            requests.add(getConfigGroupRequest(propertyMap));
        }
        java.util.Set<org.apache.ambari.server.controller.ConfigGroupResponse> responses = getResources(new org.apache.ambari.server.controller.internal.AbstractResourceProvider.Command<java.util.Set<org.apache.ambari.server.controller.ConfigGroupResponse>>() {
            @java.lang.Override
            public java.util.Set<org.apache.ambari.server.controller.ConfigGroupResponse> invoke() throws org.apache.ambari.server.AmbariException {
                return getConfigGroups(requests);
            }
        });
        java.util.Set<java.lang.String> requestedIds = getRequestPropertyIds(request, predicate);
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = new java.util.HashSet<>();
        if (requestedIds.contains(org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.HOSTS_HOST_NAME)) {
            requestedIds.add(org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.HOSTS);
        }
        for (org.apache.ambari.server.controller.ConfigGroupResponse response : responses) {
            org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.ConfigGroup);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.ID, response.getId(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.CLUSTER_NAME, response.getClusterName(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.GROUP_NAME, response.getGroupName(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.TAG, response.getTag(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.DESCRIPTION, response.getDescription(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.HOSTS, response.getHosts(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.DESIRED_CONFIGS, response.getConfigurations(), requestedIds);
            resources.add(resource);
        }
        return resources;
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus updateResourcesAuthorized(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        final java.util.Set<org.apache.ambari.server.controller.ConfigGroupRequest> requests = new java.util.HashSet<>();
        java.util.Iterator<java.util.Map<java.lang.String, java.lang.Object>> iterator = request.getProperties().iterator();
        if (iterator.hasNext()) {
            for (java.util.Map<java.lang.String, java.lang.Object> propertyMap : getPropertyMaps(iterator.next(), predicate)) {
                requests.add(getConfigGroupRequest(propertyMap));
            }
        }
        updateResources(requests);
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> associatedResources = new java.util.HashSet<>();
        for (org.apache.ambari.server.controller.ConfigGroupRequest configGroupRequest : requests) {
            org.apache.ambari.server.controller.ConfigGroupResponse configGroupResponse = getManagementController().getConfigGroupUpdateResults(configGroupRequest);
            org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.ConfigGroup);
            resource.setProperty(org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.ID, configGroupResponse.getId());
            resource.setProperty(org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.CLUSTER_NAME, configGroupResponse.getClusterName());
            resource.setProperty(org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.GROUP_NAME, configGroupResponse.getGroupName());
            resource.setProperty(org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.TAG, configGroupResponse.getTag());
            resource.setProperty(org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.VERSION_TAGS, configGroupResponse.getVersionTags());
            associatedResources.add(resource);
        }
        notifyUpdate(org.apache.ambari.server.controller.spi.Resource.Type.ConfigGroup, request, predicate);
        return getRequestStatus(null, associatedResources);
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus deleteResourcesAuthorized(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        for (java.util.Map<java.lang.String, java.lang.Object> propertyMap : getPropertyMaps(predicate)) {
            final org.apache.ambari.server.controller.ConfigGroupRequest configGroupRequest = getConfigGroupRequest(propertyMap);
            modifyResources(new org.apache.ambari.server.controller.internal.AbstractResourceProvider.Command<java.lang.Void>() {
                @java.lang.Override
                public java.lang.Void invoke() throws org.apache.ambari.server.AmbariException, org.apache.ambari.server.security.authorization.AuthorizationException {
                    deleteConfigGroup(configGroupRequest);
                    return null;
                }
            });
        }
        notifyDelete(org.apache.ambari.server.controller.spi.Resource.Type.ConfigGroup, predicate);
        return getRequestStatus(null);
    }

    @java.lang.Override
    public java.util.Set<java.lang.String> checkPropertyIds(java.util.Set<java.lang.String> propertyIds) {
        java.util.Set<java.lang.String> unsupportedPropertyIds = super.checkPropertyIds(propertyIds);
        for (java.util.Iterator<java.lang.String> iterator = unsupportedPropertyIds.iterator(); iterator.hasNext();) {
            java.lang.String next = iterator.next();
            next = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyName(next);
            if (next.equals("service_config_version_note") || next.equals("/service_config_version_note")) {
                iterator.remove();
            }
        }
        return unsupportedPropertyIds;
    }

    public org.apache.ambari.server.controller.spi.RequestStatus createResources(final java.util.Set<org.apache.ambari.server.controller.ConfigGroupRequest> requests) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.ResourceAlreadyExistsException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        java.util.Set<org.apache.ambari.server.controller.ConfigGroupResponse> responses = createResources(new org.apache.ambari.server.controller.internal.AbstractResourceProvider.Command<java.util.Set<org.apache.ambari.server.controller.ConfigGroupResponse>>() {
            @java.lang.Override
            public java.util.Set<org.apache.ambari.server.controller.ConfigGroupResponse> invoke() throws org.apache.ambari.server.AmbariException, org.apache.ambari.server.security.authorization.AuthorizationException {
                return createConfigGroups(requests);
            }
        });
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> associatedResources = new java.util.HashSet<>();
        for (org.apache.ambari.server.controller.ConfigGroupResponse response : responses) {
            org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.ConfigGroup);
            resource.setProperty(org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.ID, response.getId());
            associatedResources.add(resource);
        }
        return getRequestStatus(null, associatedResources);
    }

    public org.apache.ambari.server.controller.spi.RequestStatus updateResources(final java.util.Set<org.apache.ambari.server.controller.ConfigGroupRequest> requests) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        modifyResources(new org.apache.ambari.server.controller.internal.AbstractResourceProvider.Command<java.lang.Void>() {
            @java.lang.Override
            public java.lang.Void invoke() throws org.apache.ambari.server.AmbariException, org.apache.ambari.server.security.authorization.AuthorizationException {
                updateConfigGroups(requests);
                return null;
            }
        });
        return getRequestStatus(null);
    }

    private synchronized java.util.Set<org.apache.ambari.server.controller.ConfigGroupResponse> getConfigGroups(java.util.Set<org.apache.ambari.server.controller.ConfigGroupRequest> requests) throws org.apache.ambari.server.AmbariException {
        java.util.Set<org.apache.ambari.server.controller.ConfigGroupResponse> responses = new java.util.HashSet<>();
        if (requests != null) {
            for (org.apache.ambari.server.controller.ConfigGroupRequest request : requests) {
                org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.LOG.debug("Received a Config group request with, clusterName = {}, groupId = {}, groupName = {}, tag = {}", request.getClusterName(), request.getId(), request.getGroupName(), request.getTag());
                if (request.getClusterName() == null) {
                    org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.LOG.warn("Cluster name is a required field.");
                    continue;
                }
                org.apache.ambari.server.state.Cluster cluster = getManagementController().getClusters().getCluster(request.getClusterName());
                java.util.Map<java.lang.Long, org.apache.ambari.server.state.configgroup.ConfigGroup> configGroupMap = cluster.getConfigGroups();
                if (request.getId() != null) {
                    org.apache.ambari.server.state.configgroup.ConfigGroup configGroup = configGroupMap.get(request.getId());
                    if (configGroup != null) {
                        responses.add(configGroup.convertToResponse());
                    } else {
                        throw new org.apache.ambari.server.ConfigGroupNotFoundException(cluster.getClusterName(), request.getId().toString());
                    }
                    continue;
                }
                if (request.getGroupName() != null) {
                    for (org.apache.ambari.server.state.configgroup.ConfigGroup configGroup : configGroupMap.values()) {
                        if (configGroup.getName().equals(request.getGroupName())) {
                            responses.add(configGroup.convertToResponse());
                        }
                    }
                    continue;
                }
                if ((request.getTag() != null) && request.getHosts().isEmpty()) {
                    for (org.apache.ambari.server.state.configgroup.ConfigGroup configGroup : configGroupMap.values()) {
                        if (configGroup.getTag().equals(request.getTag())) {
                            responses.add(configGroup.convertToResponse());
                        }
                    }
                    continue;
                }
                if ((!request.getHosts().isEmpty()) && (request.getTag() == null)) {
                    for (java.lang.String hostname : request.getHosts()) {
                        java.util.Map<java.lang.Long, org.apache.ambari.server.state.configgroup.ConfigGroup> groupMap = cluster.getConfigGroupsByHostname(hostname);
                        if (!groupMap.isEmpty()) {
                            for (org.apache.ambari.server.state.configgroup.ConfigGroup configGroup : groupMap.values()) {
                                responses.add(configGroup.convertToResponse());
                            }
                        }
                    }
                    continue;
                }
                if ((request.getTag() != null) && (!request.getHosts().isEmpty())) {
                    for (org.apache.ambari.server.state.configgroup.ConfigGroup configGroup : configGroupMap.values()) {
                        if (configGroup.getTag().equals(request.getTag())) {
                            java.util.List<java.lang.Long> groupHostIds = new java.util.ArrayList<>(configGroup.getHosts().keySet());
                            java.util.Set<java.lang.String> groupHostNames = new java.util.HashSet<>(org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.hostDAO.getHostNamesByHostIds(groupHostIds));
                            groupHostNames.retainAll(request.getHosts());
                            if (!groupHostNames.isEmpty()) {
                                responses.add(configGroup.convertToResponse());
                            }
                        }
                    }
                    continue;
                }
                for (org.apache.ambari.server.state.configgroup.ConfigGroup configGroup : configGroupMap.values()) {
                    responses.add(configGroup.convertToResponse());
                }
            }
        }
        return responses;
    }

    private void verifyConfigs(java.util.Map<java.lang.String, org.apache.ambari.server.state.Config> configs, java.lang.String clusterName) throws org.apache.ambari.server.AmbariException {
        if (configs == null) {
            return;
        }
        org.apache.ambari.server.state.Clusters clusters = getManagementController().getClusters();
        for (java.lang.String key : configs.keySet()) {
            if (!clusters.getCluster(clusterName).isConfigTypeExists(key)) {
                throw new org.apache.ambari.server.AmbariException(((("Trying to add not existent config type to config group:" + " configType = ") + key) + " cluster = ") + clusterName);
            }
        }
    }

    private void verifyHostList(org.apache.ambari.server.state.Cluster cluster, java.util.Map<java.lang.Long, org.apache.ambari.server.state.Host> hosts, org.apache.ambari.server.controller.ConfigGroupRequest request) throws org.apache.ambari.server.AmbariException {
        java.util.Map<java.lang.Long, org.apache.ambari.server.state.configgroup.ConfigGroup> configGroupMap = cluster.getConfigGroups();
        if (configGroupMap != null) {
            for (org.apache.ambari.server.state.configgroup.ConfigGroup configGroup : configGroupMap.values()) {
                if (configGroup.getTag().equals(request.getTag()) && (!configGroup.getId().equals(request.getId()))) {
                    for (org.apache.ambari.server.state.Host host : hosts.values()) {
                        if (configGroup.getHosts().containsKey(host.getHostId())) {
                            throw new org.apache.ambari.server.DuplicateResourceException(((((((("Host is already " + ("associated with a config group" + ", clusterName = ")) + configGroup.getClusterName()) + ", configGroupName = ") + configGroup.getName()) + ", tag = ") + configGroup.getTag()) + ", hostname = ") + host.getHostName());
                        }
                    }
                }
            }
        }
    }

    private synchronized void deleteConfigGroup(org.apache.ambari.server.controller.ConfigGroupRequest request) throws org.apache.ambari.server.AmbariException, org.apache.ambari.server.security.authorization.AuthorizationException {
        if (request.getId() == null) {
            throw new org.apache.ambari.server.AmbariException("Config group id is a required field.");
        }
        org.apache.ambari.server.state.Clusters clusters = getManagementController().getClusters();
        org.apache.ambari.server.state.Cluster cluster;
        try {
            cluster = clusters.getCluster(request.getClusterName());
        } catch (org.apache.ambari.server.ClusterNotFoundException e) {
            throw new org.apache.ambari.server.ParentObjectNotFoundException("Attempted to delete a config group from a cluster which doesn't " + "exist", e);
        }
        org.apache.ambari.server.state.configgroup.ConfigGroup configGroup = cluster.getConfigGroups().get(request.getId());
        if (configGroup == null) {
            throw new org.apache.ambari.server.ConfigGroupNotFoundException(cluster.getClusterName(), request.getId().toString());
        }
        if (org.apache.commons.lang.StringUtils.isEmpty(configGroup.getServiceName())) {
            if (!org.apache.ambari.server.security.authorization.AuthorizationHelper.isAuthorized(org.apache.ambari.server.security.authorization.ResourceType.CLUSTER, cluster.getResourceId(), org.apache.ambari.server.security.authorization.RoleAuthorization.CLUSTER_MANAGE_CONFIG_GROUPS)) {
                throw new org.apache.ambari.server.security.authorization.AuthorizationException("The authenticated user is not authorized to delete config groups");
            }
        } else if (!org.apache.ambari.server.security.authorization.AuthorizationHelper.isAuthorized(org.apache.ambari.server.security.authorization.ResourceType.CLUSTER, cluster.getResourceId(), org.apache.ambari.server.security.authorization.RoleAuthorization.SERVICE_MANAGE_CONFIG_GROUPS)) {
            throw new org.apache.ambari.server.security.authorization.AuthorizationException("The authenticated user is not authorized to delete config groups");
        }
        org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.configLogger.info("(configchange) Deleting configuration group. cluster: '{}', changed by: '{}', config group: '{}', config group id: '{}'", cluster.getClusterName(), getManagementController().getAuthName(), configGroup.getName(), request.getId());
        cluster.deleteConfigGroup(request.getId());
    }

    private void validateRequest(org.apache.ambari.server.controller.ConfigGroupRequest request) {
        if ((((((request.getClusterName() == null) || request.getClusterName().isEmpty()) || (request.getGroupName() == null)) || request.getGroupName().isEmpty()) || (request.getTag() == null)) || request.getTag().isEmpty()) {
            org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.LOG.debug("Received a config group request with cluster name = {}, group name = {}, tag = {}", request.getClusterName(), request.getGroupName(), request.getTag());
            throw new java.lang.IllegalArgumentException("Cluster name, group name and tag need to be provided.");
        }
    }

    private synchronized java.util.Set<org.apache.ambari.server.controller.ConfigGroupResponse> createConfigGroups(java.util.Set<org.apache.ambari.server.controller.ConfigGroupRequest> requests) throws org.apache.ambari.server.AmbariException, org.apache.ambari.server.security.authorization.AuthorizationException {
        if (requests.isEmpty()) {
            org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.LOG.warn("Received an empty requests set");
            return null;
        }
        java.util.Set<org.apache.ambari.server.controller.ConfigGroupResponse> configGroupResponses = new java.util.HashSet<>();
        org.apache.ambari.server.state.Clusters clusters = getManagementController().getClusters();
        org.apache.ambari.server.state.configgroup.ConfigGroupFactory configGroupFactory = getManagementController().getConfigGroupFactory();
        java.util.Set<java.lang.String> updatedClusters = new java.util.HashSet<>();
        for (org.apache.ambari.server.controller.ConfigGroupRequest request : requests) {
            org.apache.ambari.server.state.Cluster cluster;
            try {
                cluster = clusters.getCluster(request.getClusterName());
            } catch (org.apache.ambari.server.ClusterNotFoundException e) {
                throw new org.apache.ambari.server.ParentObjectNotFoundException("Attempted to add a config group to a cluster which doesn't exist", e);
            }
            validateRequest(request);
            java.util.Map<java.lang.Long, org.apache.ambari.server.state.configgroup.ConfigGroup> configGroupMap = cluster.getConfigGroups();
            if (configGroupMap != null) {
                for (org.apache.ambari.server.state.configgroup.ConfigGroup configGroup : configGroupMap.values()) {
                    if (configGroup.getName().equals(request.getGroupName()) && configGroup.getTag().equals(request.getTag())) {
                        throw new org.apache.ambari.server.DuplicateResourceException(((((("Config group already " + ("exists with the same name and tag" + ", clusterName = ")) + request.getClusterName()) + ", groupName = ") + request.getGroupName()) + ", tag = ") + request.getTag());
                    }
                }
            }
            java.util.Map<java.lang.Long, org.apache.ambari.server.state.Host> hosts = new java.util.HashMap<>();
            if ((request.getHosts() != null) && (!request.getHosts().isEmpty())) {
                for (java.lang.String hostname : request.getHosts()) {
                    org.apache.ambari.server.state.Host host = clusters.getHost(hostname);
                    org.apache.ambari.server.orm.entities.HostEntity hostEntity = org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.hostDAO.findByName(hostname);
                    if ((host == null) || (hostEntity == null)) {
                        throw new org.apache.ambari.server.HostNotFoundException(hostname);
                    }
                    hosts.put(hostEntity.getHostId(), host);
                }
            }
            verifyHostList(cluster, hosts, request);
            java.lang.String serviceName = request.getServiceName();
            if ((serviceName == null) && (!org.apache.commons.collections.MapUtils.isEmpty(request.getConfigs()))) {
                try {
                    serviceName = cluster.getServiceForConfigTypes(request.getConfigs().keySet());
                } catch (java.lang.IllegalArgumentException e) {
                }
            }
            if (org.apache.commons.lang.StringUtils.isEmpty(serviceName)) {
                if (!org.apache.ambari.server.security.authorization.AuthorizationHelper.isAuthorized(org.apache.ambari.server.security.authorization.ResourceType.CLUSTER, cluster.getResourceId(), org.apache.ambari.server.security.authorization.RoleAuthorization.CLUSTER_MANAGE_CONFIG_GROUPS)) {
                    throw new org.apache.ambari.server.security.authorization.AuthorizationException("The authenticated user is not authorized to create config groups");
                }
            } else if (!org.apache.ambari.server.security.authorization.AuthorizationHelper.isAuthorized(org.apache.ambari.server.security.authorization.ResourceType.CLUSTER, cluster.getResourceId(), org.apache.ambari.server.security.authorization.RoleAuthorization.SERVICE_MANAGE_CONFIG_GROUPS)) {
                throw new org.apache.ambari.server.security.authorization.AuthorizationException("The authenticated user is not authorized to create config groups");
            }
            org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.configLogger.info("(configchange) Creating new configuration group. cluster: '{}', changed by: '{}', config group: '{}', tag: '{}'", cluster.getClusterName(), getManagementController().getAuthName(), request.getGroupName(), request.getTag());
            verifyConfigs(request.getConfigs(), cluster.getClusterName());
            org.apache.ambari.server.state.configgroup.ConfigGroup configGroup = configGroupFactory.createNew(cluster, serviceName, request.getGroupName(), request.getTag(), request.getDescription(), request.getConfigs(), hosts);
            cluster.addConfigGroup(configGroup);
            if (serviceName != null) {
                cluster.createServiceConfigVersion(serviceName, getManagementController().getAuthName(), request.getServiceConfigVersionNote(), configGroup);
            } else {
                org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.LOG.warn("Could not determine service name for config group {}, service config version not created", configGroup.getId());
            }
            org.apache.ambari.server.controller.ConfigGroupResponse response = new org.apache.ambari.server.controller.ConfigGroupResponse(configGroup.getId(), configGroup.getClusterName(), configGroup.getName(), configGroup.getTag(), configGroup.getDescription(), null, null);
            configGroupResponses.add(response);
            updatedClusters.add(cluster.getClusterName());
        }
        org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.m_configHelper.get().updateAgentConfigs(updatedClusters);
        return configGroupResponses;
    }

    private synchronized void updateConfigGroups(java.util.Set<org.apache.ambari.server.controller.ConfigGroupRequest> requests) throws org.apache.ambari.server.AmbariException, org.apache.ambari.server.security.authorization.AuthorizationException {
        if (requests.isEmpty()) {
            org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.LOG.warn("Received an empty requests set");
            return;
        }
        org.apache.ambari.server.state.Clusters clusters = getManagementController().getClusters();
        java.util.Set<java.lang.String> updatedClusters = new java.util.HashSet<>();
        for (org.apache.ambari.server.controller.ConfigGroupRequest request : requests) {
            org.apache.ambari.server.state.Cluster cluster;
            try {
                cluster = clusters.getCluster(request.getClusterName());
            } catch (org.apache.ambari.server.ClusterNotFoundException e) {
                throw new org.apache.ambari.server.ParentObjectNotFoundException("Attempted to add a config group to a cluster which doesn't exist", e);
            }
            if (request.getId() == null) {
                throw new org.apache.ambari.server.AmbariException("Config group Id is a required parameter.");
            }
            validateRequest(request);
            org.apache.ambari.server.state.configgroup.ConfigGroup configGroup = cluster.getConfigGroups().get(request.getId());
            if (configGroup == null) {
                throw new org.apache.ambari.server.AmbariException(((("Config group not found" + ", clusterName = ") + request.getClusterName()) + ", groupId = ") + request.getId());
            }
            java.lang.String serviceName = configGroup.getServiceName();
            java.lang.String requestServiceName = cluster.getServiceForConfigTypes(request.getConfigs().keySet());
            if (org.apache.commons.lang.StringUtils.isEmpty(serviceName) && org.apache.commons.lang.StringUtils.isEmpty(requestServiceName)) {
                if (!org.apache.ambari.server.security.authorization.AuthorizationHelper.isAuthorized(org.apache.ambari.server.security.authorization.ResourceType.CLUSTER, cluster.getResourceId(), org.apache.ambari.server.security.authorization.RoleAuthorization.CLUSTER_MANAGE_CONFIG_GROUPS)) {
                    throw new org.apache.ambari.server.security.authorization.AuthorizationException("The authenticated user is not authorized to update config groups");
                }
            } else if (!org.apache.ambari.server.security.authorization.AuthorizationHelper.isAuthorized(org.apache.ambari.server.security.authorization.ResourceType.CLUSTER, cluster.getResourceId(), org.apache.ambari.server.security.authorization.RoleAuthorization.SERVICE_MANAGE_CONFIG_GROUPS)) {
                throw new org.apache.ambari.server.security.authorization.AuthorizationException("The authenticated user is not authorized to update config groups");
            }
            if (((serviceName != null) && (requestServiceName != null)) && (!org.apache.commons.lang.StringUtils.equals(serviceName, requestServiceName))) {
                throw new java.lang.IllegalArgumentException(((((("Config group " + configGroup.getId()) + " is mapped to service ") + serviceName) + ", ") + "but request contain configs from service ") + requestServiceName);
            } else if ((serviceName == null) && (requestServiceName != null)) {
                configGroup.setServiceName(requestServiceName);
                serviceName = requestServiceName;
            }
            int numHosts = (null != configGroup.getHosts()) ? configGroup.getHosts().size() : 0;
            org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.configLogger.info("(configchange) Updating configuration group host membership or config value. cluster: '{}', changed by: '{}', " + "service_name: '{}', config group: '{}', tag: '{}', num hosts in config group: '{}', note: '{}'", cluster.getClusterName(), getManagementController().getAuthName(), serviceName, request.getGroupName(), request.getTag(), numHosts, request.getServiceConfigVersionNote());
            if (!request.getConfigs().isEmpty()) {
                java.util.List<java.lang.String> affectedConfigTypeList = new java.util.ArrayList(request.getConfigs().keySet());
                java.util.Collections.sort(affectedConfigTypeList);
                java.lang.String affectedConfigTypesString = ("(" + org.apache.commons.lang.StringUtils.join(affectedConfigTypeList, ", ")) + ")";
                org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.configLogger.info("(configchange)    Affected configs: {}", affectedConfigTypesString);
                for (org.apache.ambari.server.state.Config config : request.getConfigs().values()) {
                    java.util.List<java.lang.String> sortedConfigKeys = new java.util.ArrayList(config.getProperties().keySet());
                    java.util.Collections.sort(sortedConfigKeys);
                    java.lang.String sortedConfigKeysString = org.apache.commons.lang.StringUtils.join(sortedConfigKeys, ", ");
                    org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.configLogger.info("(configchange)    Config type '{}' was  modified with the following keys, {}", config.getType(), sortedConfigKeysString);
                }
            }
            java.util.Map<java.lang.Long, org.apache.ambari.server.state.Host> hosts = new java.util.HashMap<>();
            if ((request.getHosts() != null) && (!request.getHosts().isEmpty())) {
                for (java.lang.String hostname : request.getHosts()) {
                    org.apache.ambari.server.state.Host host = clusters.getHost(hostname);
                    org.apache.ambari.server.orm.entities.HostEntity hostEntity = org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.hostDAO.findById(host.getHostId());
                    if (hostEntity == null) {
                        throw new org.apache.ambari.server.HostNotFoundException(hostname);
                    }
                    hosts.put(hostEntity.getHostId(), host);
                }
            }
            verifyHostList(cluster, hosts, request);
            configGroup.setHosts(hosts);
            verifyConfigs(request.getConfigs(), request.getClusterName());
            configGroup.setConfigurations(request.getConfigs());
            configGroup.setName(request.getGroupName());
            configGroup.setDescription(request.getDescription());
            configGroup.setTag(request.getTag());
            if (serviceName != null) {
                cluster.createServiceConfigVersion(serviceName, getManagementController().getAuthName(), request.getServiceConfigVersionNote(), configGroup);
                org.apache.ambari.server.controller.ConfigGroupResponse configGroupResponse = new org.apache.ambari.server.controller.ConfigGroupResponse(configGroup.getId(), cluster.getClusterName(), configGroup.getName(), request.getTag(), "", new java.util.HashSet<>(), new java.util.HashSet<>());
                java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> versionTags = new java.util.HashSet<>();
                java.util.Map<java.lang.String, java.lang.Object> tagsMap = new java.util.HashMap<>();
                for (org.apache.ambari.server.state.Config config : configGroup.getConfigurations().values()) {
                    tagsMap.put(config.getType(), config.getTag());
                }
                versionTags.add(tagsMap);
                configGroupResponse.setVersionTags(versionTags);
                getManagementController().saveConfigGroupUpdate(request, configGroupResponse);
                updatedClusters.add(cluster.getClusterName());
            } else {
                org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.LOG.warn("Could not determine service name for config group {}, service config version not created", configGroup.getId());
            }
        }
        org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.m_configHelper.get().updateAgentConfigs(updatedClusters);
    }

    @java.lang.SuppressWarnings("unchecked")
    org.apache.ambari.server.controller.ConfigGroupRequest getConfigGroupRequest(java.util.Map<java.lang.String, java.lang.Object> properties) {
        java.lang.Object groupIdObj = properties.get(org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.ID);
        java.lang.Long groupId = null;
        if (groupIdObj != null) {
            groupId = (groupIdObj instanceof java.lang.Long) ? ((java.lang.Long) (groupIdObj)) : java.lang.Long.parseLong(((java.lang.String) (groupIdObj)));
        }
        org.apache.ambari.server.controller.ConfigGroupRequest request = new org.apache.ambari.server.controller.ConfigGroupRequest(groupId, ((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.CLUSTER_NAME))), ((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.GROUP_NAME))), ((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.TAG))), ((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.SERVICE_NAME))), ((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.DESCRIPTION))), null, null);
        request.setServiceConfigVersionNote(((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.SERVICE_CONFIG_VERSION_NOTE))));
        java.util.Map<java.lang.String, org.apache.ambari.server.state.Config> configurations = new java.util.HashMap<>();
        java.util.Set<java.lang.String> hosts = new java.util.HashSet<>();
        java.lang.String hostnameKey = org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.HOST_NAME;
        java.lang.Object hostObj = properties.get(org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.HOSTS);
        if (hostObj == null) {
            hostnameKey = org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.HOSTS_HOST_NAME;
            hostObj = properties.get(org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.HOSTS_HOST_NAME);
        }
        if (hostObj != null) {
            if (hostObj instanceof java.util.HashSet<?>) {
                try {
                    java.util.Set<java.util.Map<java.lang.String, java.lang.String>> hostsSet = ((java.util.Set<java.util.Map<java.lang.String, java.lang.String>>) (hostObj));
                    for (java.util.Map<java.lang.String, java.lang.String> hostMap : hostsSet) {
                        if (hostMap.containsKey(hostnameKey)) {
                            java.lang.String hostname = hostMap.get(hostnameKey);
                            hosts.add(hostname);
                        }
                    }
                } catch (java.lang.Exception e) {
                    org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.LOG.warn("Host json in unparseable format. " + hostObj, e);
                }
            } else if (hostObj instanceof java.lang.String) {
                hosts.add(((java.lang.String) (hostObj)));
            }
        }
        java.lang.Object configObj = properties.get(org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.DESIRED_CONFIGS);
        if ((configObj != null) && (configObj instanceof java.util.HashSet<?>)) {
            try {
                java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> configSet = ((java.util.Set<java.util.Map<java.lang.String, java.lang.Object>>) (configObj));
                for (java.util.Map<java.lang.String, java.lang.Object> configMap : configSet) {
                    java.lang.String type = ((java.lang.String) (configMap.get(org.apache.ambari.server.controller.internal.ConfigurationResourceProvider.TYPE)));
                    java.lang.String tag = ((java.lang.String) (configMap.get(org.apache.ambari.server.controller.internal.ConfigurationResourceProvider.TAG)));
                    java.util.Map<java.lang.String, java.lang.String> configProperties = new java.util.HashMap<>();
                    java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configAttributes = new java.util.HashMap<>();
                    for (java.util.Map.Entry<java.lang.String, java.lang.Object> entry : configMap.entrySet()) {
                        java.lang.String propertyCategory = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyCategory(entry.getKey());
                        if ((propertyCategory != null) && (entry.getValue() != null)) {
                            if ("properties".equals(propertyCategory)) {
                                configProperties.put(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyName(entry.getKey()), entry.getValue().toString());
                            } else if ("properties_attributes".equals(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyCategory(propertyCategory))) {
                                java.lang.String attributeName = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyName(propertyCategory);
                                if (!configAttributes.containsKey(attributeName)) {
                                    configAttributes.put(attributeName, new java.util.HashMap<>());
                                }
                                java.util.Map<java.lang.String, java.lang.String> attributeValues = configAttributes.get(attributeName);
                                attributeValues.put(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyName(entry.getKey()), entry.getValue().toString());
                            }
                        }
                    }
                    org.apache.ambari.server.state.Config config = org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.configFactory.createReadOnly(type, tag, configProperties, configAttributes);
                    configurations.put(config.getType(), config);
                }
            } catch (java.lang.Exception e) {
                org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider.LOG.warn("Config json in unparseable format. " + configObj, e);
            }
        }
        request.setConfigs(configurations);
        request.setHosts(hosts);
        return request;
    }

    @java.lang.Override
    public boolean evaluate(org.apache.ambari.server.controller.spi.Predicate predicate, org.apache.ambari.server.controller.spi.Resource resource) {
        return true;
    }
}