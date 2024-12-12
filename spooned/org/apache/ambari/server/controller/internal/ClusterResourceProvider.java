package org.apache.ambari.server.controller.internal;
import org.springframework.security.core.Authentication;
public class ClusterResourceProvider extends org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.controller.internal.ClusterResourceProvider.class);

    public static final java.lang.String RESPONSE_KEY = "Clusters";

    public static final java.lang.String ALL_PROPERTIES = (org.apache.ambari.server.controller.internal.ClusterResourceProvider.RESPONSE_KEY + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + "*";

    public static final java.lang.String CLUSTER_ID = "cluster_id";

    public static final java.lang.String CLUSTER_NAME = "cluster_name";

    public static final java.lang.String VERSION = "version";

    public static final java.lang.String PROVISIONING_STATE = "provisioning_state";

    public static final java.lang.String SECURITY_TYPE = "security_type";

    public static final java.lang.String DESIRED_CONFIGS = "desired_configs";

    public static final java.lang.String DESIRED_SERVICE_CONFIG_VERSIONS = "desired_service_config_versions";

    public static final java.lang.String TOTAL_HOSTS = "total_hosts";

    public static final java.lang.String HEALTH_REPORT = "health_report";

    public static final java.lang.String CREDENTIAL_STORE_PROPERTIES = "credential_store_properties";

    public static final java.lang.String REPO_VERSION = "repository_version";

    public static final java.lang.String CLUSTER_ID_PROPERTY_ID = (org.apache.ambari.server.controller.internal.ClusterResourceProvider.RESPONSE_KEY + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.ClusterResourceProvider.CLUSTER_ID;

    public static final java.lang.String CLUSTER_NAME_PROPERTY_ID = (org.apache.ambari.server.controller.internal.ClusterResourceProvider.RESPONSE_KEY + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.ClusterResourceProvider.CLUSTER_NAME;

    public static final java.lang.String CLUSTER_VERSION_PROPERTY_ID = (org.apache.ambari.server.controller.internal.ClusterResourceProvider.RESPONSE_KEY + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.ClusterResourceProvider.VERSION;

    public static final java.lang.String CLUSTER_PROVISIONING_STATE_PROPERTY_ID = (org.apache.ambari.server.controller.internal.ClusterResourceProvider.RESPONSE_KEY + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.ClusterResourceProvider.PROVISIONING_STATE;

    public static final java.lang.String CLUSTER_SECURITY_TYPE_PROPERTY_ID = (org.apache.ambari.server.controller.internal.ClusterResourceProvider.RESPONSE_KEY + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.ClusterResourceProvider.SECURITY_TYPE;

    public static final java.lang.String CLUSTER_DESIRED_CONFIGS_PROPERTY_ID = (org.apache.ambari.server.controller.internal.ClusterResourceProvider.RESPONSE_KEY + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.ClusterResourceProvider.DESIRED_CONFIGS;

    public static final java.lang.String CLUSTER_DESIRED_SERVICE_CONFIG_VERSIONS_PROPERTY_ID = (org.apache.ambari.server.controller.internal.ClusterResourceProvider.RESPONSE_KEY + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.ClusterResourceProvider.DESIRED_SERVICE_CONFIG_VERSIONS;

    public static final java.lang.String CLUSTER_TOTAL_HOSTS_PROPERTY_ID = (org.apache.ambari.server.controller.internal.ClusterResourceProvider.RESPONSE_KEY + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.ClusterResourceProvider.TOTAL_HOSTS;

    public static final java.lang.String CLUSTER_HEALTH_REPORT_PROPERTY_ID = (org.apache.ambari.server.controller.internal.ClusterResourceProvider.RESPONSE_KEY + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.ClusterResourceProvider.HEALTH_REPORT;

    public static final java.lang.String CLUSTER_CREDENTIAL_STORE_PROPERTIES_PROPERTY_ID = (org.apache.ambari.server.controller.internal.ClusterResourceProvider.RESPONSE_KEY + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.ClusterResourceProvider.CREDENTIAL_STORE_PROPERTIES;

    public static final java.lang.String CLUSTER_STATE_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Clusters", "state");

    static final java.lang.String BLUEPRINT = "blueprint";

    public static final java.lang.String SECURITY = "security";

    public static final java.lang.String CREDENTIALS = "credentials";

    private static final java.lang.String QUICKLINKS_PROFILE = "quicklinks_profile";

    private static final java.lang.String SESSION_ATTRIBUTES = "session_attributes";

    private static final java.lang.String SESSION_ATTRIBUTES_PROPERTY_PREFIX = org.apache.ambari.server.controller.internal.ClusterResourceProvider.SESSION_ATTRIBUTES + "/";

    public static final java.lang.String GET_IGNORE_PERMISSIONS_PROPERTY_ID = "get_resource/ignore_permissions";

    private static org.apache.ambari.server.topology.TopologyManager topologyManager;

    private static org.apache.ambari.server.topology.TopologyRequestFactory topologyRequestFactory;

    private static org.apache.ambari.server.topology.SecurityConfigurationFactory securityConfigurationFactory;

    protected static final java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> keyPropertyIds = com.google.common.collect.ImmutableMap.<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String>builder().put(org.apache.ambari.server.controller.spi.Resource.Type.Cluster, org.apache.ambari.server.controller.internal.ClusterResourceProvider.CLUSTER_NAME_PROPERTY_ID).build();

    protected static final java.util.Set<java.lang.String> propertyIds = com.google.common.collect.ImmutableSet.<java.lang.String>builder().add(org.apache.ambari.server.controller.internal.ClusterResourceProvider.CLUSTER_ID_PROPERTY_ID).add(org.apache.ambari.server.controller.internal.ClusterResourceProvider.CLUSTER_NAME_PROPERTY_ID).add(org.apache.ambari.server.controller.internal.ClusterResourceProvider.CLUSTER_VERSION_PROPERTY_ID).add(org.apache.ambari.server.controller.internal.ClusterResourceProvider.CLUSTER_PROVISIONING_STATE_PROPERTY_ID).add(org.apache.ambari.server.controller.internal.ClusterResourceProvider.CLUSTER_SECURITY_TYPE_PROPERTY_ID).add(org.apache.ambari.server.controller.internal.ClusterResourceProvider.CLUSTER_DESIRED_CONFIGS_PROPERTY_ID).add(org.apache.ambari.server.controller.internal.ClusterResourceProvider.CLUSTER_DESIRED_SERVICE_CONFIG_VERSIONS_PROPERTY_ID).add(org.apache.ambari.server.controller.internal.ClusterResourceProvider.CLUSTER_TOTAL_HOSTS_PROPERTY_ID).add(org.apache.ambari.server.controller.internal.ClusterResourceProvider.CLUSTER_HEALTH_REPORT_PROPERTY_ID).add(org.apache.ambari.server.controller.internal.ClusterResourceProvider.CLUSTER_CREDENTIAL_STORE_PROPERTIES_PROPERTY_ID).add(org.apache.ambari.server.controller.internal.ClusterResourceProvider.BLUEPRINT).add(org.apache.ambari.server.controller.internal.ClusterResourceProvider.SESSION_ATTRIBUTES).add(org.apache.ambari.server.controller.internal.ClusterResourceProvider.SECURITY).add(org.apache.ambari.server.controller.internal.ClusterResourceProvider.CREDENTIALS).add(org.apache.ambari.server.controller.internal.ClusterResourceProvider.QUICKLINKS_PROFILE).add(org.apache.ambari.server.controller.internal.ClusterResourceProvider.CLUSTER_STATE_PROPERTY_ID).build();

    private static com.google.gson.Gson jsonSerializer;

    ClusterResourceProvider(org.apache.ambari.server.controller.AmbariManagementController managementController) {
        super(org.apache.ambari.server.controller.spi.Resource.Type.Cluster, org.apache.ambari.server.controller.internal.ClusterResourceProvider.propertyIds, org.apache.ambari.server.controller.internal.ClusterResourceProvider.keyPropertyIds, managementController);
        setRequiredCreateAuthorizations(java.util.EnumSet.of(org.apache.ambari.server.security.authorization.RoleAuthorization.AMBARI_ADD_DELETE_CLUSTERS));
        setRequiredDeleteAuthorizations(java.util.EnumSet.of(org.apache.ambari.server.security.authorization.RoleAuthorization.AMBARI_ADD_DELETE_CLUSTERS));
        setRequiredGetAuthorizations(org.apache.ambari.server.security.authorization.RoleAuthorization.AUTHORIZATIONS_VIEW_CLUSTER);
        setRequiredUpdateAuthorizations(org.apache.ambari.server.security.authorization.RoleAuthorization.AUTHORIZATIONS_UPDATE_CLUSTER);
    }

    @java.lang.Override
    protected java.util.Set<java.lang.String> getPKPropertyIds() {
        return new java.util.HashSet<>(java.util.Collections.singletonList(org.apache.ambari.server.controller.internal.ClusterResourceProvider.CLUSTER_ID_PROPERTY_ID));
    }

    @java.lang.Override
    public java.util.Set<java.lang.String> checkPropertyIds(java.util.Set<java.lang.String> propertyIds) {
        java.util.Set<java.lang.String> baseUnsupported = super.checkPropertyIds(propertyIds);
        baseUnsupported.remove("blueprint");
        baseUnsupported.remove("host_groups");
        baseUnsupported.remove("default_password");
        baseUnsupported.remove("configurations");
        baseUnsupported.remove("credentials");
        baseUnsupported.remove("config_recommendation_strategy");
        baseUnsupported.remove("provision_action");
        baseUnsupported.remove(org.apache.ambari.server.controller.internal.ProvisionClusterRequest.REPO_VERSION_PROPERTY);
        baseUnsupported.remove(org.apache.ambari.server.controller.internal.ProvisionClusterRequest.REPO_VERSION_ID_PROPERTY);
        return checkConfigPropertyIds(baseUnsupported, "Clusters");
    }

    @java.lang.Override
    protected boolean isAuthorizedToCreateResources(org.springframework.security.core.Authentication authentication, org.apache.ambari.server.controller.spi.Request request) {
        return org.apache.ambari.server.security.authorization.AuthorizationHelper.isAuthorized(authentication, org.apache.ambari.server.security.authorization.ResourceType.AMBARI, null, getRequiredCreateAuthorizations());
    }

    @java.lang.Override
    protected boolean isAuthorizedToDeleteResources(org.springframework.security.core.Authentication authentication, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException {
        return org.apache.ambari.server.security.authorization.AuthorizationHelper.isAuthorized(authentication, org.apache.ambari.server.security.authorization.ResourceType.AMBARI, null, getRequiredDeleteAuthorizations());
    }

    @java.lang.Override
    protected org.apache.ambari.server.controller.spi.RequestStatus createResourcesAuthorized(org.apache.ambari.server.controller.spi.Request request) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.ResourceAlreadyExistsException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        org.apache.ambari.server.controller.RequestStatusResponse createResponse = null;
        for (final java.util.Map<java.lang.String, java.lang.Object> properties : request.getProperties()) {
            if (isCreateFromBlueprint(properties)) {
                createResponse = processBlueprintCreate(properties, request.getRequestInfoProperties());
            } else {
                createClusterResource(properties);
            }
        }
        notifyCreate(org.apache.ambari.server.controller.spi.Resource.Type.Cluster, request);
        return getRequestStatus(createResponse);
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.controller.spi.Resource> getResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        final java.util.Set<org.apache.ambari.server.controller.ClusterRequest> requests = new java.util.HashSet<>();
        if (predicate == null) {
            requests.add(getRequest(java.util.Collections.emptyMap()));
        } else {
            for (java.util.Map<java.lang.String, java.lang.Object> propertyMap : getPropertyMaps(predicate)) {
                requests.add(getRequest(propertyMap));
            }
        }
        java.util.Set<java.lang.String> requestedIds = getRequestPropertyIds(request, predicate);
        java.util.Set<org.apache.ambari.server.controller.ClusterResponse> responses = getResources(new org.apache.ambari.server.controller.internal.AbstractResourceProvider.Command<java.util.Set<org.apache.ambari.server.controller.ClusterResponse>>() {
            @java.lang.Override
            public java.util.Set<org.apache.ambari.server.controller.ClusterResponse> invoke() throws org.apache.ambari.server.AmbariException, org.apache.ambari.server.security.authorization.AuthorizationException {
                return getManagementController().getClusters(requests);
            }
        });
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = new java.util.HashSet<>();
        if (org.apache.ambari.server.controller.internal.ClusterResourceProvider.LOG.isDebugEnabled()) {
            org.apache.ambari.server.controller.internal.ClusterResourceProvider.LOG.debug("Found clusters matching getClusters request, clusterResponseCount={}", responses.size());
        }
        for (org.apache.ambari.server.controller.ClusterResponse response : responses) {
            org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Cluster);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.ClusterResourceProvider.CLUSTER_ID_PROPERTY_ID, response.getClusterId(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.ClusterResourceProvider.CLUSTER_NAME_PROPERTY_ID, response.getClusterName(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.ClusterResourceProvider.CLUSTER_PROVISIONING_STATE_PROPERTY_ID, response.getProvisioningState().name(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.ClusterResourceProvider.CLUSTER_SECURITY_TYPE_PROPERTY_ID, response.getSecurityType().name(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.ClusterResourceProvider.CLUSTER_DESIRED_CONFIGS_PROPERTY_ID, response.getDesiredConfigs(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.ClusterResourceProvider.CLUSTER_DESIRED_SERVICE_CONFIG_VERSIONS_PROPERTY_ID, response.getDesiredServiceConfigVersions(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.ClusterResourceProvider.CLUSTER_TOTAL_HOSTS_PROPERTY_ID, response.getTotalHosts(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.ClusterResourceProvider.CLUSTER_HEALTH_REPORT_PROPERTY_ID, response.getClusterHealthReport(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.ClusterResourceProvider.CLUSTER_CREDENTIAL_STORE_PROPERTIES_PROPERTY_ID, response.getCredentialStoreServiceProperties(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.ClusterResourceProvider.CLUSTER_VERSION_PROPERTY_ID, response.getDesiredStackVersion(), requestedIds);
            if (org.apache.ambari.server.controller.internal.ClusterResourceProvider.LOG.isDebugEnabled()) {
                org.apache.ambari.server.controller.internal.ClusterResourceProvider.LOG.debug("Adding ClusterResponse to resource, clusterResponse={}", response);
            }
            resources.add(resource);
        }
        return resources;
    }

    @java.lang.Override
    protected org.apache.ambari.server.controller.spi.RequestStatus updateResourcesAuthorized(final org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        final java.util.Set<org.apache.ambari.server.controller.ClusterRequest> requests = new java.util.HashSet<>();
        org.apache.ambari.server.controller.RequestStatusResponse response;
        for (java.util.Map<java.lang.String, java.lang.Object> requestPropertyMap : request.getProperties()) {
            java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> propertyMaps = getPropertyMaps(requestPropertyMap, predicate);
            for (java.util.Map<java.lang.String, java.lang.Object> propertyMap : propertyMaps) {
                org.apache.ambari.server.controller.ClusterRequest clusterRequest = getRequest(propertyMap);
                requests.add(clusterRequest);
            }
        }
        response = modifyResources(new org.apache.ambari.server.controller.internal.AbstractResourceProvider.Command<org.apache.ambari.server.controller.RequestStatusResponse>() {
            @java.lang.Override
            public org.apache.ambari.server.controller.RequestStatusResponse invoke() throws org.apache.ambari.server.AmbariException, org.apache.ambari.server.security.authorization.AuthorizationException {
                return getManagementController().updateClusters(requests, request.getRequestInfoProperties());
            }
        });
        notifyUpdate(org.apache.ambari.server.controller.spi.Resource.Type.Cluster, request, predicate);
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> associatedResources = null;
        for (org.apache.ambari.server.controller.ClusterRequest clusterRequest : requests) {
            org.apache.ambari.server.controller.ClusterResponse updateResults = getManagementController().getClusterUpdateResults(clusterRequest);
            if (updateResults != null) {
                java.util.Map<java.lang.String, java.util.Collection<org.apache.ambari.server.controller.ServiceConfigVersionResponse>> serviceConfigVersions = updateResults.getDesiredServiceConfigVersions();
                if (serviceConfigVersions != null) {
                    associatedResources = new java.util.HashSet<>();
                    for (java.util.Collection<org.apache.ambari.server.controller.ServiceConfigVersionResponse> scvCollection : serviceConfigVersions.values()) {
                        for (org.apache.ambari.server.controller.ServiceConfigVersionResponse serviceConfigVersionResponse : scvCollection) {
                            org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.ServiceConfigVersion);
                            resource.setProperty(org.apache.ambari.server.controller.internal.ServiceConfigVersionResourceProvider.SERVICE_NAME_PROPERTY_ID, serviceConfigVersionResponse.getServiceName());
                            resource.setProperty(org.apache.ambari.server.controller.internal.ServiceConfigVersionResourceProvider.SERVICE_CONFIG_VERSION_PROPERTY_ID, serviceConfigVersionResponse.getVersion());
                            resource.setProperty(org.apache.ambari.server.controller.internal.ServiceConfigVersionResourceProvider.SERVICE_CONFIG_VERSION_NOTE_PROPERTY_ID, serviceConfigVersionResponse.getNote());
                            resource.setProperty(org.apache.ambari.server.controller.internal.ServiceConfigVersionResourceProvider.GROUP_ID_PROPERTY_ID, serviceConfigVersionResponse.getGroupId());
                            resource.setProperty(org.apache.ambari.server.controller.internal.ServiceConfigVersionResourceProvider.GROUP_NAME_PROPERTY_ID, serviceConfigVersionResponse.getGroupName());
                            if (serviceConfigVersionResponse.getConfigurations() != null) {
                                resource.setProperty(org.apache.ambari.server.controller.internal.ServiceConfigVersionResourceProvider.CONFIGURATIONS_PROPERTY_ID, serviceConfigVersionResponse.getConfigurations());
                            }
                            associatedResources.add(resource);
                        }
                    }
                }
            }
        }
        return getRequestStatus(response, associatedResources);
    }

    @java.lang.Override
    protected org.apache.ambari.server.controller.spi.RequestStatus deleteResourcesAuthorized(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        for (java.util.Map<java.lang.String, java.lang.Object> propertyMap : getPropertyMaps(predicate)) {
            final org.apache.ambari.server.controller.ClusterRequest clusterRequest = getRequest(propertyMap);
            modifyResources(new org.apache.ambari.server.controller.internal.AbstractResourceProvider.Command<java.lang.Void>() {
                @java.lang.Override
                public java.lang.Void invoke() throws org.apache.ambari.server.AmbariException {
                    getManagementController().deleteCluster(clusterRequest);
                    return null;
                }
            });
        }
        notifyDelete(org.apache.ambari.server.controller.spi.Resource.Type.Cluster, predicate);
        return getRequestStatus(null);
    }

    public static void init(org.apache.ambari.server.topology.TopologyManager manager, org.apache.ambari.server.topology.TopologyRequestFactory requestFactory, org.apache.ambari.server.topology.SecurityConfigurationFactory securityFactory, com.google.gson.Gson instance) {
        org.apache.ambari.server.controller.internal.ClusterResourceProvider.topologyManager = manager;
        org.apache.ambari.server.controller.internal.ClusterResourceProvider.topologyRequestFactory = requestFactory;
        org.apache.ambari.server.controller.internal.ClusterResourceProvider.securityConfigurationFactory = securityFactory;
        org.apache.ambari.server.controller.internal.ClusterResourceProvider.jsonSerializer = instance;
    }

    private org.apache.ambari.server.controller.ClusterRequest getRequest(java.util.Map<java.lang.String, java.lang.Object> properties) {
        org.apache.ambari.server.state.SecurityType securityType;
        java.lang.String requestedSecurityType = ((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.ClusterResourceProvider.CLUSTER_SECURITY_TYPE_PROPERTY_ID)));
        if (requestedSecurityType == null)
            securityType = null;
        else {
            try {
                securityType = org.apache.ambari.server.state.SecurityType.valueOf(requestedSecurityType.toUpperCase());
            } catch (java.lang.IllegalArgumentException e) {
                throw new java.lang.IllegalArgumentException(java.lang.String.format("Cannot set cluster security type to invalid value: %s", requestedSecurityType));
            }
        }
        org.apache.ambari.server.controller.ClusterRequest cr = new org.apache.ambari.server.controller.ClusterRequest(((java.lang.Long) (properties.get(org.apache.ambari.server.controller.internal.ClusterResourceProvider.CLUSTER_ID_PROPERTY_ID))), ((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.ClusterResourceProvider.CLUSTER_NAME_PROPERTY_ID))), ((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.ClusterResourceProvider.CLUSTER_PROVISIONING_STATE_PROPERTY_ID))), securityType, ((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.ClusterResourceProvider.CLUSTER_VERSION_PROPERTY_ID))), null, getSessionAttributes(properties));
        java.util.List<org.apache.ambari.server.controller.ConfigurationRequest> configRequests = org.apache.ambari.server.controller.internal.AbstractResourceProvider.getConfigurationRequests(org.apache.ambari.server.controller.internal.ClusterResourceProvider.RESPONSE_KEY, properties);
        if (!configRequests.isEmpty()) {
            cr.setDesiredConfig(configRequests);
        }
        org.apache.ambari.server.controller.ServiceConfigVersionRequest serviceConfigVersionRequest = org.apache.ambari.server.controller.internal.ClusterResourceProvider.getServiceConfigVersionRequest(org.apache.ambari.server.controller.internal.ClusterResourceProvider.RESPONSE_KEY, properties);
        if (serviceConfigVersionRequest != null) {
            cr.setServiceConfigVersionRequest(serviceConfigVersionRequest);
        }
        return cr;
    }

    private java.util.Map<java.lang.String, java.lang.Object> getSessionAttributes(java.util.Map<java.lang.String, java.lang.Object> properties) {
        java.util.Map<java.lang.String, java.lang.Object> sessionAttributes = new java.util.HashMap<>();
        for (java.util.Map.Entry<java.lang.String, java.lang.Object> entry : properties.entrySet()) {
            java.lang.String property = entry.getKey();
            if (property.startsWith(org.apache.ambari.server.controller.internal.ClusterResourceProvider.SESSION_ATTRIBUTES_PROPERTY_PREFIX)) {
                java.lang.String attributeName = property.substring(org.apache.ambari.server.controller.internal.ClusterResourceProvider.SESSION_ATTRIBUTES_PROPERTY_PREFIX.length());
                sessionAttributes.put(attributeName, entry.getValue());
            }
        }
        return sessionAttributes;
    }

    protected static org.apache.ambari.server.controller.ServiceConfigVersionRequest getServiceConfigVersionRequest(java.lang.String parentCategory, java.util.Map<java.lang.String, java.lang.Object> properties) {
        org.apache.ambari.server.controller.ServiceConfigVersionRequest serviceConfigVersionRequest = null;
        for (java.util.Map.Entry<java.lang.String, java.lang.Object> entry : properties.entrySet()) {
            java.lang.String absCategory = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyCategory(entry.getKey());
            java.lang.String propName = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyName(entry.getKey());
            if ((absCategory != null) && absCategory.startsWith(parentCategory + "/desired_service_config_version")) {
                serviceConfigVersionRequest = (serviceConfigVersionRequest == null) ? new org.apache.ambari.server.controller.ServiceConfigVersionRequest() : serviceConfigVersionRequest;
                if (propName != null) {
                    switch (propName) {
                        case "service_name" :
                            {
                                serviceConfigVersionRequest.setServiceName(entry.getValue().toString());
                                break;
                            }
                        case "service_config_version" :
                            {
                                serviceConfigVersionRequest.setVersion(java.lang.Long.valueOf(entry.getValue().toString()));
                                break;
                            }
                        case "service_config_version_note" :
                            {
                                serviceConfigVersionRequest.setNote(entry.getValue().toString());
                                break;
                            }
                    }
                }
            }
        }
        return serviceConfigVersionRequest;
    }

    private boolean isCreateFromBlueprint(java.util.Map<java.lang.String, java.lang.Object> properties) {
        return properties.get("blueprint") != null;
    }

    @java.lang.SuppressWarnings("unchecked")
    private org.apache.ambari.server.controller.RequestStatusResponse processBlueprintCreate(java.util.Map<java.lang.String, java.lang.Object> properties, java.util.Map<java.lang.String, java.lang.String> requestInfoProperties) throws org.apache.ambari.server.controller.spi.ResourceAlreadyExistsException, org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        org.apache.ambari.server.controller.internal.ClusterResourceProvider.LOG.info(((("Creating Cluster '" + properties.get(org.apache.ambari.server.controller.internal.ClusterResourceProvider.CLUSTER_NAME_PROPERTY_ID)) + "' based on blueprint '") + java.lang.String.valueOf(properties.get(org.apache.ambari.server.controller.internal.ClusterResourceProvider.BLUEPRINT))) + "'.");
        java.lang.String rawRequestBody = requestInfoProperties.get(org.apache.ambari.server.controller.spi.Request.REQUEST_INFO_BODY_PROPERTY);
        java.util.Map<java.lang.String, java.lang.Object> rawBodyMap = org.apache.ambari.server.controller.internal.ClusterResourceProvider.jsonSerializer.<java.util.Map<java.lang.String, java.lang.Object>>fromJson(rawRequestBody, java.util.Map.class);
        org.apache.ambari.server.topology.SecurityConfiguration securityConfiguration = org.apache.ambari.server.controller.internal.ClusterResourceProvider.securityConfigurationFactory.createSecurityConfigurationFromRequest(rawBodyMap, false);
        org.apache.ambari.server.controller.internal.ProvisionClusterRequest createClusterRequest;
        try {
            createClusterRequest = org.apache.ambari.server.controller.internal.ClusterResourceProvider.topologyRequestFactory.createProvisionClusterRequest(properties, securityConfiguration);
        } catch (org.apache.ambari.server.topology.InvalidTopologyTemplateException e) {
            throw new java.lang.IllegalArgumentException("Invalid Cluster Creation Template: " + e, e);
        }
        if ((((securityConfiguration != null) && (securityConfiguration.getType() == org.apache.ambari.server.state.SecurityType.NONE)) && (createClusterRequest.getBlueprint().getSecurity() != null)) && (createClusterRequest.getBlueprint().getSecurity().getType() == org.apache.ambari.server.state.SecurityType.KERBEROS)) {
            throw new java.lang.IllegalArgumentException("Setting security to NONE is not allowed as security type in blueprint is set to KERBEROS!");
        }
        try {
            return org.apache.ambari.server.controller.internal.ClusterResourceProvider.topologyManager.provisionCluster(createClusterRequest);
        } catch (org.apache.ambari.server.topology.InvalidTopologyException e) {
            throw new java.lang.IllegalArgumentException("Topology validation failed: " + e, e);
        } catch (org.apache.ambari.server.AmbariException e) {
            throw new org.apache.ambari.server.controller.spi.SystemException("Unknown exception when asking TopologyManager to provision cluster", e);
        } catch (java.lang.RuntimeException e) {
            throw new org.apache.ambari.server.controller.spi.SystemException("An exception occurred during cluster provisioning: " + e.getMessage(), e);
        }
    }

    private void createClusterResource(final java.util.Map<java.lang.String, java.lang.Object> properties) throws org.apache.ambari.server.controller.spi.ResourceAlreadyExistsException, org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        createResources(new org.apache.ambari.server.controller.internal.AbstractResourceProvider.Command<java.lang.Void>() {
            @java.lang.Override
            public java.lang.Void invoke() throws org.apache.ambari.server.AmbariException, org.apache.ambari.server.security.authorization.AuthorizationException {
                getManagementController().createCluster(getRequest(properties));
                return null;
            }
        });
    }
}