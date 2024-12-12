package org.apache.ambari.server.controller.internal;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import org.apache.commons.lang.StringUtils;
import static org.apache.ambari.server.controller.AmbariManagementControllerImpl.CLUSTER_PHASE_INITIAL_INSTALL;
import static org.apache.ambari.server.controller.AmbariManagementControllerImpl.CLUSTER_PHASE_INITIAL_START;
import static org.apache.ambari.server.controller.AmbariManagementControllerImpl.CLUSTER_PHASE_PROPERTY;
import static org.apache.ambari.server.controller.internal.RequestResourceProvider.CONTEXT;
public class HostComponentResourceProvider extends org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.class);

    public static final java.lang.String HOST_ROLES = "HostRoles";

    public static final java.lang.String HOST = "host";

    public static final java.lang.String SERVICE_COMPONENT_INFO = "ServiceComponentInfo";

    public static final java.lang.String ROLE_ID_PROPERTY_ID = "role_id";

    public static final java.lang.String CLUSTER_NAME_PROPERTY_ID = "cluster_name";

    public static final java.lang.String SERVICE_NAME_PROPERTY_ID = "service_name";

    public static final java.lang.String COMPONENT_NAME_PROPERTY_ID = "component_name";

    public static final java.lang.String DISPLAY_NAME_PROPERTY_ID = "display_name";

    public static final java.lang.String HOST_NAME_PROPERTY_ID = "host_name";

    public static final java.lang.String PUBLIC_HOST_NAME_PROPERTY_ID = "public_host_name";

    public static final java.lang.String STATE_PROPERTY_ID = "state";

    public static final java.lang.String DESIRED_STATE_PROPERTY_ID = "desired_state";

    public static final java.lang.String VERSION_PROPERTY_ID = "version";

    public static final java.lang.String DESIRED_STACK_ID_PROPERTY_ID = "desired_stack_id";

    public static final java.lang.String DESIRED_REPOSITORY_VERSION_PROPERTY_ID = "desired_repository_version";

    public static final java.lang.String ACTUAL_CONFIGS_PROPERTY_ID = "actual_configs";

    public static final java.lang.String STALE_CONFIGS_PROPERTY_ID = "stale_configs";

    public static final java.lang.String RELOAD_CONFIGS_PROPERTY_ID = "reload_configs";

    public static final java.lang.String DESIRED_ADMIN_STATE_PROPERTY_ID = "desired_admin_state";

    public static final java.lang.String MAINTENANCE_STATE_PROPERTY_ID = "maintenance_state";

    public static final java.lang.String UPGRADE_STATE_PROPERTY_ID = "upgrade_state";

    public static final java.lang.String HOST_PROPERTY_ID = "host";

    public static final java.lang.String HREF_PROPERTY_ID = "href";

    public static final java.lang.String COMPONENT_PROPERTY_ID = "component";

    public static final java.lang.String METRICS_PROPERTY_ID = "metrics";

    public static final java.lang.String PROCESSES_PROPERTY_ID = "processes";

    public static final java.lang.String ROLE_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.HOST_ROLES, org.apache.ambari.server.controller.internal.HostComponentResourceProvider.ROLE_ID_PROPERTY_ID);

    public static final java.lang.String CLUSTER_NAME = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.HOST_ROLES, org.apache.ambari.server.controller.internal.HostComponentResourceProvider.CLUSTER_NAME_PROPERTY_ID);

    public static final java.lang.String SERVICE_NAME = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.HOST_ROLES, org.apache.ambari.server.controller.internal.HostComponentResourceProvider.SERVICE_NAME_PROPERTY_ID);

    public static final java.lang.String COMPONENT_NAME = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.HOST_ROLES, org.apache.ambari.server.controller.internal.HostComponentResourceProvider.COMPONENT_NAME_PROPERTY_ID);

    public static final java.lang.String DISPLAY_NAME = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.HOST_ROLES, org.apache.ambari.server.controller.internal.HostComponentResourceProvider.DISPLAY_NAME_PROPERTY_ID);

    public static final java.lang.String HOST_NAME = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.HOST_ROLES, org.apache.ambari.server.controller.internal.HostComponentResourceProvider.HOST_NAME_PROPERTY_ID);

    public static final java.lang.String PUBLIC_HOST_NAME = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.HOST_ROLES, org.apache.ambari.server.controller.internal.HostComponentResourceProvider.PUBLIC_HOST_NAME_PROPERTY_ID);

    public static final java.lang.String STATE = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.HOST_ROLES, org.apache.ambari.server.controller.internal.HostComponentResourceProvider.STATE_PROPERTY_ID);

    public static final java.lang.String DESIRED_STATE = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.HOST_ROLES, org.apache.ambari.server.controller.internal.HostComponentResourceProvider.DESIRED_STATE_PROPERTY_ID);

    public static final java.lang.String VERSION = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.HOST_ROLES, org.apache.ambari.server.controller.internal.HostComponentResourceProvider.VERSION_PROPERTY_ID);

    public static final java.lang.String DESIRED_STACK_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.HOST_ROLES, org.apache.ambari.server.controller.internal.HostComponentResourceProvider.DESIRED_STACK_ID_PROPERTY_ID);

    public static final java.lang.String DESIRED_REPOSITORY_VERSION = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.HOST_ROLES, org.apache.ambari.server.controller.internal.HostComponentResourceProvider.DESIRED_REPOSITORY_VERSION_PROPERTY_ID);

    public static final java.lang.String ACTUAL_CONFIGS = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.HOST_ROLES, org.apache.ambari.server.controller.internal.HostComponentResourceProvider.ACTUAL_CONFIGS_PROPERTY_ID);

    public static final java.lang.String STALE_CONFIGS = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.HOST_ROLES, org.apache.ambari.server.controller.internal.HostComponentResourceProvider.STALE_CONFIGS_PROPERTY_ID);

    public static final java.lang.String RELOAD_CONFIGS = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.HOST_ROLES, org.apache.ambari.server.controller.internal.HostComponentResourceProvider.RELOAD_CONFIGS_PROPERTY_ID);

    public static final java.lang.String DESIRED_ADMIN_STATE = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.HOST_ROLES, org.apache.ambari.server.controller.internal.HostComponentResourceProvider.DESIRED_ADMIN_STATE_PROPERTY_ID);

    public static final java.lang.String MAINTENANCE_STATE = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.HOST_ROLES, org.apache.ambari.server.controller.internal.HostComponentResourceProvider.MAINTENANCE_STATE_PROPERTY_ID);

    public static final java.lang.String UPGRADE_STATE = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.HOST_ROLES, org.apache.ambari.server.controller.internal.HostComponentResourceProvider.UPGRADE_STATE_PROPERTY_ID);

    private static final java.lang.String QUERY_PARAMETERS_RUN_SMOKE_TEST_ID = "params/run_smoke_test";

    public static final java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> keyPropertyIds = com.google.common.collect.ImmutableMap.<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String>builder().put(org.apache.ambari.server.controller.spi.Resource.Type.Cluster, org.apache.ambari.server.controller.internal.HostComponentResourceProvider.CLUSTER_NAME).put(org.apache.ambari.server.controller.spi.Resource.Type.Host, org.apache.ambari.server.controller.internal.HostComponentResourceProvider.HOST_NAME).put(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent, org.apache.ambari.server.controller.internal.HostComponentResourceProvider.COMPONENT_NAME).put(org.apache.ambari.server.controller.spi.Resource.Type.Component, org.apache.ambari.server.controller.internal.HostComponentResourceProvider.COMPONENT_NAME).build();

    protected static final java.util.Set<java.lang.String> propertyIds = com.google.common.collect.ImmutableSet.of(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.ROLE_ID, org.apache.ambari.server.controller.internal.HostComponentResourceProvider.CLUSTER_NAME, org.apache.ambari.server.controller.internal.HostComponentResourceProvider.SERVICE_NAME, org.apache.ambari.server.controller.internal.HostComponentResourceProvider.COMPONENT_NAME, org.apache.ambari.server.controller.internal.HostComponentResourceProvider.DISPLAY_NAME, org.apache.ambari.server.controller.internal.HostComponentResourceProvider.HOST_NAME, org.apache.ambari.server.controller.internal.HostComponentResourceProvider.PUBLIC_HOST_NAME, org.apache.ambari.server.controller.internal.HostComponentResourceProvider.STATE, org.apache.ambari.server.controller.internal.HostComponentResourceProvider.DESIRED_STATE, org.apache.ambari.server.controller.internal.HostComponentResourceProvider.VERSION, org.apache.ambari.server.controller.internal.HostComponentResourceProvider.DESIRED_STACK_ID, org.apache.ambari.server.controller.internal.HostComponentResourceProvider.DESIRED_REPOSITORY_VERSION, org.apache.ambari.server.controller.internal.HostComponentResourceProvider.ACTUAL_CONFIGS, org.apache.ambari.server.controller.internal.HostComponentResourceProvider.STALE_CONFIGS, org.apache.ambari.server.controller.internal.HostComponentResourceProvider.RELOAD_CONFIGS, org.apache.ambari.server.controller.internal.HostComponentResourceProvider.DESIRED_ADMIN_STATE, org.apache.ambari.server.controller.internal.HostComponentResourceProvider.MAINTENANCE_STATE, org.apache.ambari.server.controller.internal.HostComponentResourceProvider.UPGRADE_STATE, org.apache.ambari.server.controller.internal.HostComponentResourceProvider.QUERY_PARAMETERS_RUN_SMOKE_TEST_ID);

    public static final java.lang.String SKIP_INSTALL_FOR_COMPONENTS = "skipInstallForComponents";

    public static final java.lang.String DO_NOT_SKIP_INSTALL_FOR_COMPONENTS = "dontSkipInstallForComponents";

    public static final java.lang.String ALL_COMPONENTS = "ALL";

    public static final java.lang.String FOR_ALL_COMPONENTS = org.apache.ambari.server.controller.internal.HostComponentResourceProvider.joinComponentList(com.google.common.collect.ImmutableSet.of(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.ALL_COMPONENTS));

    public static final java.lang.String FOR_NO_COMPONENTS = org.apache.ambari.server.controller.internal.HostComponentResourceProvider.joinComponentList(com.google.common.collect.ImmutableSet.of());

    @com.google.inject.Inject
    private org.apache.ambari.server.controller.MaintenanceStateHelper maintenanceStateHelper;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.HostVersionDAO hostVersionDAO;

    @com.google.inject.assistedinject.AssistedInject
    public HostComponentResourceProvider(@com.google.inject.assistedinject.Assisted
    org.apache.ambari.server.controller.AmbariManagementController managementController) {
        super(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent, org.apache.ambari.server.controller.internal.HostComponentResourceProvider.propertyIds, org.apache.ambari.server.controller.internal.HostComponentResourceProvider.keyPropertyIds, managementController);
        setRequiredCreateAuthorizations(java.util.EnumSet.of(org.apache.ambari.server.security.authorization.RoleAuthorization.SERVICE_ADD_DELETE_SERVICES, org.apache.ambari.server.security.authorization.RoleAuthorization.HOST_ADD_DELETE_COMPONENTS));
        setRequiredDeleteAuthorizations(java.util.EnumSet.of(org.apache.ambari.server.security.authorization.RoleAuthorization.SERVICE_ADD_DELETE_SERVICES, org.apache.ambari.server.security.authorization.RoleAuthorization.HOST_ADD_DELETE_COMPONENTS));
    }

    @java.lang.Override
    protected org.apache.ambari.server.controller.spi.RequestStatus createResourcesAuthorized(org.apache.ambari.server.controller.spi.Request request) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.ResourceAlreadyExistsException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        final java.util.Set<org.apache.ambari.server.controller.ServiceComponentHostRequest> requests = new java.util.HashSet<>();
        for (java.util.Map<java.lang.String, java.lang.Object> propertyMap : request.getProperties()) {
            requests.add(changeRequest(propertyMap));
        }
        createResources(new org.apache.ambari.server.controller.internal.AbstractResourceProvider.Command<java.lang.Void>() {
            @java.lang.Override
            public java.lang.Void invoke() throws org.apache.ambari.server.AmbariException, org.apache.ambari.server.security.authorization.AuthorizationException {
                getManagementController().createHostComponents(requests);
                return null;
            }
        });
        notifyCreate(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent, request);
        return getRequestStatus(null);
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.controller.spi.Resource> getResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        final java.util.Set<org.apache.ambari.server.controller.ServiceComponentHostRequest> requests = new java.util.HashSet<>();
        for (java.util.Map<java.lang.String, java.lang.Object> propertyMap : getPropertyMaps(predicate)) {
            requests.add(getRequest(propertyMap));
        }
        return findResources(request, predicate, requests);
    }

    private java.util.Set<org.apache.ambari.server.controller.spi.Resource> getResourcesForUpdate(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        final java.util.Set<org.apache.ambari.server.controller.ServiceComponentHostRequest> requests = new java.util.HashSet<>();
        for (java.util.Map<java.lang.String, java.lang.Object> propertyMap : getPropertyMaps(predicate)) {
            requests.add(getRequest(propertyMap));
        }
        return findResources(request, predicate, requests);
    }

    private java.util.Set<org.apache.ambari.server.controller.spi.Resource> findResources(org.apache.ambari.server.controller.spi.Request request, final org.apache.ambari.server.controller.spi.Predicate predicate, final java.util.Set<org.apache.ambari.server.controller.ServiceComponentHostRequest> requests) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = new java.util.HashSet<>();
        java.util.Set<java.lang.String> requestedIds = getRequestPropertyIds(request, predicate);
        requestedIds.add(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.HOST_NAME);
        java.util.Set<org.apache.ambari.server.controller.ServiceComponentHostResponse> responses = getResources(new org.apache.ambari.server.controller.internal.AbstractResourceProvider.Command<java.util.Set<org.apache.ambari.server.controller.ServiceComponentHostResponse>>() {
            @java.lang.Override
            public java.util.Set<org.apache.ambari.server.controller.ServiceComponentHostResponse> invoke() throws org.apache.ambari.server.AmbariException {
                return getManagementController().getHostComponents(requests);
            }
        });
        for (org.apache.ambari.server.controller.ServiceComponentHostResponse response : responses) {
            org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.HostComponentResourceProvider.CLUSTER_NAME, response.getClusterName(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.HostComponentResourceProvider.SERVICE_NAME, response.getServiceName(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.HostComponentResourceProvider.COMPONENT_NAME, response.getComponentName(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.HostComponentResourceProvider.DISPLAY_NAME, response.getDisplayName(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.HostComponentResourceProvider.HOST_NAME, response.getHostname(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.HostComponentResourceProvider.PUBLIC_HOST_NAME, response.getPublicHostname(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.HostComponentResourceProvider.STATE, response.getLiveState(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.HostComponentResourceProvider.DESIRED_STATE, response.getDesiredState(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.HostComponentResourceProvider.VERSION, response.getVersion(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.HostComponentResourceProvider.DESIRED_STACK_ID, response.getDesiredStackVersion(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.HostComponentResourceProvider.ACTUAL_CONFIGS, response.getActualConfigs(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.HostComponentResourceProvider.STALE_CONFIGS, response.isStaleConfig(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.HostComponentResourceProvider.RELOAD_CONFIGS, response.isReloadConfig(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.HostComponentResourceProvider.UPGRADE_STATE, response.getUpgradeState(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.HostComponentResourceProvider.DESIRED_REPOSITORY_VERSION, response.getDesiredRepositoryVersion(), requestedIds);
            if (response.getAdminState() != null) {
                org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.HostComponentResourceProvider.DESIRED_ADMIN_STATE, response.getAdminState(), requestedIds);
            }
            if (null != response.getMaintenanceState()) {
                org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.HostComponentResourceProvider.MAINTENANCE_STATE, response.getMaintenanceState(), requestedIds);
            }
            resources.add(resource);
        }
        return resources;
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus updateResources(final org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        if (request.getProperties().isEmpty()) {
            throw new java.lang.IllegalArgumentException("Received an update request with no properties");
        }
        org.apache.ambari.server.controller.internal.RequestStageContainer requestStages = doUpdateResources(null, request, predicate, false, false, false);
        org.apache.ambari.server.controller.RequestStatusResponse response = null;
        if (requestStages != null) {
            try {
                requestStages.persist();
            } catch (org.apache.ambari.server.AmbariException e) {
                throw new org.apache.ambari.server.controller.spi.SystemException(e.getMessage(), e);
            }
            response = requestStages.getRequestStatusResponse();
            notifyUpdate(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent, request, predicate);
        }
        return getRequestStatus(response);
    }

    @java.lang.Override
    protected org.apache.ambari.server.controller.spi.RequestStatus deleteResourcesAuthorized(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        final java.util.Set<org.apache.ambari.server.controller.ServiceComponentHostRequest> requests = new java.util.HashSet<>();
        for (java.util.Map<java.lang.String, java.lang.Object> propertyMap : getPropertyMaps(predicate)) {
            requests.add(changeRequest(propertyMap));
        }
        org.apache.ambari.server.controller.internal.DeleteStatusMetaData deleteStatusMetaData = modifyResources(new org.apache.ambari.server.controller.internal.AbstractResourceProvider.Command<org.apache.ambari.server.controller.internal.DeleteStatusMetaData>() {
            @java.lang.Override
            public org.apache.ambari.server.controller.internal.DeleteStatusMetaData invoke() throws org.apache.ambari.server.AmbariException, org.apache.ambari.server.security.authorization.AuthorizationException {
                return getManagementController().deleteHostComponents(requests);
            }
        });
        notifyDelete(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent, predicate);
        return getRequestStatus(null, null, deleteStatusMetaData);
    }

    @java.lang.Override
    public java.util.Set<java.lang.String> checkPropertyIds(java.util.Set<java.lang.String> propertyIds) {
        propertyIds = super.checkPropertyIds(propertyIds);
        if (propertyIds.isEmpty()) {
            return propertyIds;
        }
        java.util.Set<java.lang.String> unsupportedProperties = new java.util.HashSet<>();
        for (java.lang.String propertyId : propertyIds) {
            if (!propertyId.equals("config")) {
                java.lang.String propertyCategory = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyCategory(propertyId);
                if ((propertyCategory == null) || (!propertyCategory.equals("config"))) {
                    unsupportedProperties.add(propertyId);
                }
            }
        }
        return unsupportedProperties;
    }

    public org.apache.ambari.server.controller.RequestStatusResponse install(java.lang.String cluster, java.lang.String hostname, java.util.Collection<java.lang.String> skipInstallForComponents, java.util.Collection<java.lang.String> dontSkipInstallForComponents, boolean skipFailure, boolean useClusterHostInfo) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        org.apache.ambari.server.controller.internal.RequestStageContainer requestStages;
        java.util.Map<java.lang.String, java.lang.Object> installProperties = new java.util.HashMap<>();
        installProperties.put(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.DESIRED_STATE, "INSTALLED");
        java.util.Map<java.lang.String, java.lang.String> requestInfo = new java.util.HashMap<>();
        requestInfo.put(org.apache.ambari.server.controller.internal.RequestResourceProvider.CONTEXT, java.lang.String.format("Install components on host %s", hostname));
        requestInfo.put(org.apache.ambari.server.controller.AmbariManagementControllerImpl.CLUSTER_PHASE_PROPERTY, org.apache.ambari.server.controller.AmbariManagementControllerImpl.CLUSTER_PHASE_INITIAL_INSTALL);
        requestInfo.putAll(org.apache.ambari.server.controller.internal.RequestOperationLevel.propertiesFor(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent, cluster));
        org.apache.ambari.server.controller.internal.HostComponentResourceProvider.addProvisionActionProperties(skipInstallForComponents, dontSkipInstallForComponents, requestInfo);
        org.apache.ambari.server.controller.spi.Request installRequest = org.apache.ambari.server.controller.utilities.PropertyHelper.getUpdateRequest(installProperties, requestInfo);
        org.apache.ambari.server.controller.spi.Predicate statePredicate = new org.apache.ambari.server.controller.predicate.EqualsPredicate<>(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.STATE, "INIT");
        org.apache.ambari.server.controller.spi.Predicate clusterPredicate = new org.apache.ambari.server.controller.predicate.EqualsPredicate<>(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.CLUSTER_NAME, cluster);
        org.apache.ambari.server.controller.spi.Predicate hostPredicate = new org.apache.ambari.server.controller.predicate.EqualsPredicate<>(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.HOST_NAME, hostname);
        org.apache.ambari.server.controller.spi.Predicate hostAndStatePredicate = new org.apache.ambari.server.controller.predicate.AndPredicate(statePredicate, hostPredicate);
        org.apache.ambari.server.controller.spi.Predicate installPredicate = new org.apache.ambari.server.controller.predicate.AndPredicate(hostAndStatePredicate, clusterPredicate);
        try {
            org.apache.ambari.server.controller.internal.HostComponentResourceProvider.LOG.info("Installing all components on host: " + hostname);
            requestStages = doUpdateResources(null, installRequest, installPredicate, true, true, useClusterHostInfo);
            notifyUpdate(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent, installRequest, installPredicate);
            try {
                requestStages.persist();
            } catch (org.apache.ambari.server.AmbariException e) {
                throw new org.apache.ambari.server.controller.spi.SystemException(e.getMessage(), e);
            }
        } catch (org.apache.ambari.server.controller.spi.NoSuchResourceException e) {
            throw new org.apache.ambari.server.controller.spi.SystemException("An unexpected exception occurred while processing install hosts", e);
        }
        return requestStages.getRequestStatusResponse();
    }

    @com.google.common.annotations.VisibleForTesting
    static void addProvisionActionProperties(java.util.Collection<java.lang.String> skipInstallForComponents, java.util.Collection<java.lang.String> dontSkipInstallForComponents, java.util.Map<java.lang.String, java.lang.String> requestInfo) {
        requestInfo.put(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.SKIP_INSTALL_FOR_COMPONENTS, org.apache.ambari.server.controller.internal.HostComponentResourceProvider.joinComponentList(skipInstallForComponents));
        requestInfo.put(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.DO_NOT_SKIP_INSTALL_FOR_COMPONENTS, org.apache.ambari.server.controller.internal.HostComponentResourceProvider.joinComponentList(dontSkipInstallForComponents));
    }

    public static java.lang.String joinComponentList(java.util.Collection<java.lang.String> components) {
        return components != null ? (";" + java.lang.String.join(";", components)) + ";" : "";
    }

    public static boolean shouldSkipInstallTaskForComponent(java.lang.String componentName, boolean isClientComponent, java.util.Map<java.lang.String, java.lang.String> requestProperties) {
        java.lang.String skipInstallForComponents = requestProperties.get(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.SKIP_INSTALL_FOR_COMPONENTS);
        java.lang.String searchString = org.apache.ambari.server.controller.internal.HostComponentResourceProvider.joinComponentList(com.google.common.collect.ImmutableSet.of(componentName));
        return (((!isClientComponent) && org.apache.ambari.server.controller.AmbariManagementControllerImpl.CLUSTER_PHASE_INITIAL_INSTALL.equals(requestProperties.get(org.apache.ambari.server.controller.AmbariManagementControllerImpl.CLUSTER_PHASE_PROPERTY))) && (skipInstallForComponents != null)) && (skipInstallForComponents.contains(searchString) || (skipInstallForComponents.equals(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.FOR_ALL_COMPONENTS) && (!requestProperties.get(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.DO_NOT_SKIP_INSTALL_FOR_COMPONENTS).contains(searchString))));
    }

    public org.apache.ambari.server.controller.RequestStatusResponse start(java.lang.String cluster, java.lang.String hostName) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        return this.start(cluster, hostName, java.util.Collections.emptySet(), false, false);
    }

    public org.apache.ambari.server.controller.RequestStatusResponse start(java.lang.String cluster, java.lang.String hostName, java.util.Collection<java.lang.String> installOnlyComponents, boolean skipFailure, boolean useClusterHostInfo) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        java.util.Map<java.lang.String, java.lang.String> requestInfo = new java.util.HashMap<>();
        requestInfo.put(org.apache.ambari.server.controller.internal.RequestResourceProvider.CONTEXT, java.lang.String.format("Start components on host %s", hostName));
        requestInfo.put(org.apache.ambari.server.controller.AmbariManagementControllerImpl.CLUSTER_PHASE_PROPERTY, org.apache.ambari.server.controller.AmbariManagementControllerImpl.CLUSTER_PHASE_INITIAL_START);
        requestInfo.putAll(org.apache.ambari.server.controller.internal.RequestOperationLevel.propertiesFor(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent, cluster));
        requestInfo.put(org.apache.ambari.server.topology.Setting.SETTING_NAME_SKIP_FAILURE, java.lang.Boolean.toString(skipFailure));
        org.apache.ambari.server.controller.spi.Predicate clusterPredicate = new org.apache.ambari.server.controller.predicate.EqualsPredicate<>(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.CLUSTER_NAME, cluster);
        org.apache.ambari.server.controller.spi.Predicate hostPredicate = new org.apache.ambari.server.controller.predicate.EqualsPredicate<>(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.HOST_NAME, hostName);
        org.apache.ambari.server.controller.internal.RequestStageContainer requestStages;
        try {
            java.util.Map<java.lang.String, java.lang.Object> startProperties = new java.util.HashMap<>();
            startProperties.put(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.DESIRED_STATE, "STARTED");
            org.apache.ambari.server.controller.spi.Request startRequest = org.apache.ambari.server.controller.utilities.PropertyHelper.getUpdateRequest(startProperties, requestInfo);
            org.apache.ambari.server.controller.spi.Predicate installedStatePredicate = new org.apache.ambari.server.controller.predicate.EqualsPredicate<>(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.DESIRED_STATE, "INSTALLED");
            org.apache.ambari.server.controller.spi.Predicate notClientPredicate = new org.apache.ambari.server.controller.predicate.NotPredicate(new org.apache.ambari.server.controller.internal.HostComponentResourceProvider.ClientComponentPredicate());
            org.apache.ambari.server.controller.spi.Predicate clusterAndClientPredicate = new org.apache.ambari.server.controller.predicate.AndPredicate(clusterPredicate, notClientPredicate);
            org.apache.ambari.server.controller.spi.Predicate hostAndStatePredicate = new org.apache.ambari.server.controller.predicate.AndPredicate(installedStatePredicate, hostPredicate);
            org.apache.ambari.server.controller.spi.Predicate startPredicate;
            if (installOnlyComponents.isEmpty()) {
                startPredicate = new org.apache.ambari.server.controller.predicate.AndPredicate(clusterAndClientPredicate, hostAndStatePredicate);
                org.apache.ambari.server.controller.internal.HostComponentResourceProvider.LOG.info("Starting all non-client components on host: " + hostName);
            } else {
                java.util.List<org.apache.ambari.server.controller.spi.Predicate> listOfComponentPredicates = new java.util.ArrayList<>();
                for (java.lang.String installOnlyComponent : installOnlyComponents) {
                    org.apache.ambari.server.controller.spi.Predicate componentNameEquals = new org.apache.ambari.server.controller.predicate.EqualsPredicate<>(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.COMPONENT_NAME, installOnlyComponent);
                    listOfComponentPredicates.add(new org.apache.ambari.server.controller.predicate.NotPredicate(componentNameEquals));
                }
                org.apache.ambari.server.controller.spi.Predicate[] arrayOfInstallOnlyPredicates = new org.apache.ambari.server.controller.spi.Predicate[listOfComponentPredicates.size()];
                org.apache.ambari.server.controller.spi.Predicate installOnlyComponentsPredicate = new org.apache.ambari.server.controller.predicate.AndPredicate(listOfComponentPredicates.toArray(arrayOfInstallOnlyPredicates));
                startPredicate = new org.apache.ambari.server.controller.predicate.AndPredicate(clusterAndClientPredicate, hostAndStatePredicate, installOnlyComponentsPredicate);
                org.apache.ambari.server.controller.internal.HostComponentResourceProvider.LOG.info((("Starting all non-client components on host: " + hostName) + ", except for the INSTALL_ONLY components specified: ") + installOnlyComponents);
            }
            requestStages = doUpdateResources(null, startRequest, startPredicate, true, true, useClusterHostInfo);
            notifyUpdate(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent, startRequest, startPredicate);
            try {
                requestStages.persist();
            } catch (org.apache.ambari.server.AmbariException e) {
                throw new org.apache.ambari.server.controller.spi.SystemException(e.getMessage(), e);
            }
        } catch (org.apache.ambari.server.controller.spi.NoSuchResourceException e) {
            throw new org.apache.ambari.server.controller.spi.SystemException("An unexpected exception occurred while processing start hosts", e);
        }
        return requestStages.getRequestStatusResponse();
    }

    protected org.apache.ambari.server.controller.internal.RequestStageContainer updateHostComponents(org.apache.ambari.server.controller.internal.RequestStageContainer stages, java.util.Set<org.apache.ambari.server.controller.ServiceComponentHostRequest> requests, java.util.Map<java.lang.String, java.lang.String> requestProperties, boolean runSmokeTest, boolean useGeneratedConfigs, boolean useClusterHostInfo) throws org.apache.ambari.server.AmbariException, org.apache.ambari.server.security.authorization.AuthorizationException {
        org.apache.ambari.server.state.Clusters clusters = getManagementController().getClusters();
        java.util.Map<java.lang.String, java.util.Map<org.apache.ambari.server.state.State, java.util.List<org.apache.ambari.server.state.ServiceComponentHost>>> changedScHosts = new java.util.HashMap<>();
        java.util.Collection<org.apache.ambari.server.state.ServiceComponentHost> ignoredScHosts = new java.util.ArrayList<>();
        java.util.Set<java.lang.String> clusterNames = new java.util.HashSet<>();
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Set<java.lang.String>>>> requestClusters = new java.util.HashMap<>();
        java.util.Map<org.apache.ambari.server.state.ServiceComponentHost, org.apache.ambari.server.state.State> directTransitionScHosts = new java.util.HashMap<>();
        org.apache.ambari.server.controller.spi.Resource.Type reqOpLvl = determineOperationLevel(requestProperties);
        java.lang.String clusterName = requestProperties.get(org.apache.ambari.server.controller.internal.RequestOperationLevel.OPERATION_CLUSTER_ID);
        if ((clusterName != null) && (!clusterName.isEmpty())) {
            clusterNames.add(clusterName);
        }
        for (org.apache.ambari.server.controller.ServiceComponentHostRequest request : requests) {
            validateServiceComponentHostRequest(request);
            org.apache.ambari.server.state.Cluster cluster = clusters.getCluster(request.getClusterName());
            if (runSmokeTest) {
                if (!org.apache.ambari.server.security.authorization.AuthorizationHelper.isAuthorized(org.apache.ambari.server.security.authorization.ResourceType.CLUSTER, cluster.getResourceId(), org.apache.ambari.server.security.authorization.RoleAuthorization.SERVICE_RUN_SERVICE_CHECK)) {
                    throw new org.apache.ambari.server.security.authorization.AuthorizationException("The authenticated user is not authorized to run service checks");
                }
            }
            if (org.apache.commons.lang.StringUtils.isEmpty(request.getServiceName())) {
                request.setServiceName(getManagementController().findServiceName(cluster, request.getComponentName()));
            }
            org.apache.ambari.server.state.ServiceComponent sc = getServiceComponent(request.getClusterName(), request.getServiceName(), request.getComponentName());
            logRequestInfo("Received a updateHostComponent request", request);
            if (((clusterName == null) || clusterName.isEmpty()) && ((request.getClusterName() != null) && (!request.getClusterName().isEmpty()))) {
                clusterNames.add(request.getClusterName());
            }
            if (clusterNames.size() > 1) {
                throw new java.lang.IllegalArgumentException("Updates to multiple clusters is not" + " supported");
            }
            java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Set<java.lang.String>>> clusterServices = requestClusters.get(request.getClusterName());
            if (clusterServices == null) {
                clusterServices = new java.util.HashMap<>();
                requestClusters.put(request.getClusterName(), clusterServices);
            }
            java.util.Map<java.lang.String, java.util.Set<java.lang.String>> serviceComponents = clusterServices.get(request.getServiceName());
            if (serviceComponents == null) {
                serviceComponents = new java.util.HashMap<>();
                clusterServices.put(request.getServiceName(), serviceComponents);
            }
            java.util.Set<java.lang.String> componentHosts = serviceComponents.get(request.getComponentName());
            if (componentHosts == null) {
                componentHosts = new java.util.HashSet<>();
                serviceComponents.put(request.getComponentName(), componentHosts);
            }
            if (componentHosts.contains(request.getHostname())) {
                throw new java.lang.IllegalArgumentException("Invalid request contains duplicate hostcomponents");
            }
            componentHosts.add(request.getHostname());
            org.apache.ambari.server.state.ServiceComponentHost sch = sc.getServiceComponentHost(request.getHostname());
            org.apache.ambari.server.state.State oldState = sch.getState();
            org.apache.ambari.server.state.State newState = null;
            if (request.getDesiredState() != null) {
                newState = org.apache.ambari.server.state.State.valueOf(request.getDesiredState());
                if (!newState.isValidDesiredState()) {
                    throw new java.lang.IllegalArgumentException(("Invalid arguments, invalid" + " desired state, desiredState=") + newState);
                }
            }
            if (null != request.getMaintenanceState()) {
                org.apache.ambari.server.state.MaintenanceState newMaint = org.apache.ambari.server.state.MaintenanceState.valueOf(request.getMaintenanceState());
                org.apache.ambari.server.state.MaintenanceState oldMaint = maintenanceStateHelper.getEffectiveState(sch);
                if (newMaint != oldMaint) {
                    if (sc.isClientComponent()) {
                        throw new java.lang.IllegalArgumentException("Invalid arguments, cannot set maintenance state on a client component");
                    } else if (newMaint.equals(org.apache.ambari.server.state.MaintenanceState.IMPLIED_FROM_HOST) || newMaint.equals(org.apache.ambari.server.state.MaintenanceState.IMPLIED_FROM_SERVICE)) {
                        throw new java.lang.IllegalArgumentException("Invalid arguments, can only set maintenance state to one of " + java.util.EnumSet.of(org.apache.ambari.server.state.MaintenanceState.OFF, org.apache.ambari.server.state.MaintenanceState.ON));
                    } else {
                        sch.setMaintenanceState(newMaint);
                    }
                }
            }
            if (newState == null) {
                org.apache.ambari.server.controller.internal.HostComponentResourceProvider.LOG.info(getServiceComponentRequestInfoLogMessage("Nothing to do for new updateServiceComponentHost", request, oldState, null));
                continue;
            }
            if (!org.apache.ambari.server.security.authorization.AuthorizationHelper.isAuthorized(org.apache.ambari.server.security.authorization.ResourceType.CLUSTER, cluster.getResourceId(), java.util.EnumSet.of(org.apache.ambari.server.security.authorization.RoleAuthorization.SERVICE_START_STOP, org.apache.ambari.server.security.authorization.RoleAuthorization.SERVICE_ADD_DELETE_SERVICES, org.apache.ambari.server.security.authorization.RoleAuthorization.HOST_ADD_DELETE_COMPONENTS, org.apache.ambari.server.security.authorization.RoleAuthorization.HOST_ADD_DELETE_HOSTS))) {
                throw new org.apache.ambari.server.security.authorization.AuthorizationException("The authenticated user is not authorized to change the state of service components");
            }
            if ((sc.isClientComponent() && (newState == org.apache.ambari.server.state.State.STARTED)) && (!requestProperties.containsKey(sch.getServiceComponentName().toLowerCase()))) {
                ignoredScHosts.add(sch);
                org.apache.ambari.server.controller.internal.HostComponentResourceProvider.LOG.info(getServiceComponentRequestInfoLogMessage("Ignoring ServiceComponentHost as STARTED new desired state for client components is not valid", request, sch.getState(), newState));
                continue;
            }
            if (sc.isClientComponent() && (!newState.isValidClientComponentState())) {
                throw new java.lang.IllegalArgumentException("Invalid desired state for a client" + " component");
            }
            org.apache.ambari.server.state.State oldSchState = sch.getState();
            if (((newState == oldSchState) && (!sc.isClientComponent())) && (!requestProperties.containsKey(sch.getServiceComponentName().toLowerCase()))) {
                ignoredScHosts.add(sch);
                org.apache.ambari.server.controller.internal.HostComponentResourceProvider.LOG.info(getServiceComponentRequestInfoLogMessage("Ignoring ServiceComponentHost as the current state matches the new desired state", request, oldState, newState));
                continue;
            }
            if (!maintenanceStateHelper.isOperationAllowed(reqOpLvl, sch)) {
                ignoredScHosts.add(sch);
                org.apache.ambari.server.controller.internal.HostComponentResourceProvider.LOG.info(getServiceComponentRequestInfoLogMessage("Ignoring ServiceComponentHost as operation is not allowed", request, oldState, newState));
                continue;
            }
            if (!isValidStateTransition(stages, oldSchState, newState, sch)) {
                throw new org.apache.ambari.server.AmbariException(((((((((((((("Invalid state transition for host component" + ", clusterName=") + cluster.getClusterName()) + ", clusterId=") + cluster.getClusterId()) + ", serviceName=") + sch.getServiceName()) + ", componentName=") + sch.getServiceComponentName()) + ", hostname=") + sch.getHostName()) + ", currentState=") + oldSchState) + ", newDesiredState=") + newState);
            }
            if (isDirectTransition(oldSchState, newState)) {
                org.apache.ambari.server.controller.internal.HostComponentResourceProvider.LOG.info(getServiceComponentRequestInfoLogMessage("Handling direct transition update to host component", request, oldState, newState));
                directTransitionScHosts.put(sch, newState);
            } else {
                if (!changedScHosts.containsKey(sc.getName())) {
                    changedScHosts.put(sc.getName(), new java.util.EnumMap<>(org.apache.ambari.server.state.State.class));
                }
                if (!changedScHosts.get(sc.getName()).containsKey(newState)) {
                    changedScHosts.get(sc.getName()).put(newState, new java.util.ArrayList<>());
                }
                org.apache.ambari.server.controller.internal.HostComponentResourceProvider.LOG.info(getServiceComponentRequestInfoLogMessage("Handling update to host component", request, oldState, newState));
                changedScHosts.get(sc.getName()).get(newState).add(sch);
            }
        }
        doDirectTransitions(directTransitionScHosts);
        org.apache.ambari.server.state.Cluster cluster = clusters.getCluster(clusterNames.iterator().next());
        return getManagementController().addStages(stages, cluster, requestProperties, null, null, null, changedScHosts, ignoredScHosts, runSmokeTest, false, useGeneratedConfigs, useClusterHostInfo);
    }

    @java.lang.Override
    protected java.util.Set<java.lang.String> getPKPropertyIds() {
        return new java.util.HashSet<>(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.keyPropertyIds.values());
    }

    private org.apache.ambari.server.controller.ServiceComponentHostRequest getRequest(java.util.Map<java.lang.String, java.lang.Object> properties) {
        org.apache.ambari.server.controller.ServiceComponentHostRequest serviceComponentHostRequest = new org.apache.ambari.server.controller.ServiceComponentHostRequest(((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.CLUSTER_NAME))), ((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.SERVICE_NAME))), ((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.COMPONENT_NAME))), ((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.HOST_NAME))), ((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.DESIRED_STATE))));
        serviceComponentHostRequest.setState(((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.STATE))));
        if (properties.get(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.STALE_CONFIGS) != null) {
            serviceComponentHostRequest.setStaleConfig(properties.get(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.STALE_CONFIGS).toString().toLowerCase());
        }
        if (properties.get(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.DESIRED_ADMIN_STATE) != null) {
            serviceComponentHostRequest.setAdminState(properties.get(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.DESIRED_ADMIN_STATE).toString());
        }
        if (properties.get(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.PUBLIC_HOST_NAME) != null) {
            serviceComponentHostRequest.setPublicHostname(properties.get(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.PUBLIC_HOST_NAME).toString());
        }
        java.lang.Object o = properties.get(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.MAINTENANCE_STATE);
        if (null != o) {
            serviceComponentHostRequest.setMaintenanceState(o.toString());
        }
        return serviceComponentHostRequest;
    }

    private org.apache.ambari.server.controller.ServiceComponentHostRequest changeRequest(java.util.Map<java.lang.String, java.lang.Object> properties) {
        org.apache.ambari.server.controller.ServiceComponentHostRequest serviceComponentHostRequest = new org.apache.ambari.server.controller.ServiceComponentHostRequest(((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.CLUSTER_NAME))), ((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.SERVICE_NAME))), ((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.COMPONENT_NAME))), ((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.HOST_NAME))), ((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.STATE))));
        if (properties.get(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.DESIRED_STATE) != null) {
            serviceComponentHostRequest.setDesiredState(((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.DESIRED_STATE))));
        }
        if (properties.get(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.STALE_CONFIGS) != null) {
            serviceComponentHostRequest.setStaleConfig(properties.get(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.STALE_CONFIGS).toString().toLowerCase());
        }
        if (properties.get(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.DESIRED_ADMIN_STATE) != null) {
            serviceComponentHostRequest.setAdminState(properties.get(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.DESIRED_ADMIN_STATE).toString());
        }
        java.lang.Object o = properties.get(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.MAINTENANCE_STATE);
        if (null != o) {
            serviceComponentHostRequest.setMaintenanceState(o.toString());
        }
        return serviceComponentHostRequest;
    }

    public org.apache.ambari.server.controller.internal.RequestStageContainer doUpdateResources(final org.apache.ambari.server.controller.internal.RequestStageContainer stages, final org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate, boolean performQueryEvaluation, boolean useGeneratedConfigs, boolean useClusterHostInfo) throws org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        final java.util.Set<org.apache.ambari.server.controller.ServiceComponentHostRequest> requests = new java.util.HashSet<>();
        final boolean runSmokeTest = "true".equals(org.apache.ambari.server.controller.internal.AbstractResourceProvider.getQueryParameterValue(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.QUERY_PARAMETERS_RUN_SMOKE_TEST_ID, predicate));
        java.util.Set<java.lang.String> queryIds = java.util.Collections.singleton(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.COMPONENT_NAME);
        org.apache.ambari.server.controller.spi.Request queryRequest = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(queryIds);
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> matchingResources = getResourcesForUpdate(queryRequest, predicate);
        for (org.apache.ambari.server.controller.spi.Resource queryResource : matchingResources) {
            if ((!performQueryEvaluation) || predicate.evaluate(queryResource)) {
                java.util.Map<java.lang.String, java.lang.Object> updateRequestProperties = new java.util.HashMap<>();
                updateRequestProperties.putAll(org.apache.ambari.server.controller.utilities.PropertyHelper.getProperties(queryResource));
                if ((request.getProperties() != null) && (request.getProperties().size() != 0)) {
                    updateRequestProperties.putAll(request.getProperties().iterator().next());
                }
                requests.add(changeRequest(updateRequestProperties));
            }
        }
        if (requests.isEmpty()) {
            java.lang.String msg = java.lang.String.format("Skipping updating hosts: no matching requests for %s", predicate);
            org.apache.ambari.server.controller.internal.HostComponentResourceProvider.LOG.info(msg);
            throw new org.apache.ambari.server.controller.spi.NoSuchResourceException(msg);
        }
        org.apache.ambari.server.controller.internal.RequestStageContainer requestStages = modifyResources(new org.apache.ambari.server.controller.internal.AbstractResourceProvider.Command<org.apache.ambari.server.controller.internal.RequestStageContainer>() {
            @java.lang.Override
            public org.apache.ambari.server.controller.internal.RequestStageContainer invoke() throws org.apache.ambari.server.AmbariException {
                org.apache.ambari.server.controller.internal.RequestStageContainer stageContainer = null;
                try {
                    stageContainer = updateHostComponents(stages, requests, request.getRequestInfoProperties(), runSmokeTest, useGeneratedConfigs, useClusterHostInfo);
                } catch (java.lang.Exception e) {
                    org.apache.ambari.server.controller.internal.HostComponentResourceProvider.LOG.info("Caught an exception while updating host components, will not try again: {}", e.getMessage(), e);
                    if (e instanceof java.lang.IllegalArgumentException) {
                        throw ((java.lang.IllegalArgumentException) (e));
                    } else {
                        throw new java.lang.RuntimeException("Update Host request submission failed: " + e, e);
                    }
                }
                return stageContainer;
            }
        });
        notifyUpdate(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent, request, predicate);
        return requestStages;
    }

    private boolean isValidStateTransition(org.apache.ambari.server.controller.internal.RequestStageContainer stages, org.apache.ambari.server.state.State startState, org.apache.ambari.server.state.State desiredState, org.apache.ambari.server.state.ServiceComponentHost host) {
        return true;
    }

    public boolean isDirectTransition(org.apache.ambari.server.state.State oldState, org.apache.ambari.server.state.State newState) {
        switch (newState) {
            case INSTALLED :
                if (oldState == org.apache.ambari.server.state.State.DISABLED) {
                    return true;
                }
                break;
            case DISABLED :
                if (((oldState == org.apache.ambari.server.state.State.INSTALLED) || (oldState == org.apache.ambari.server.state.State.INSTALL_FAILED)) || (oldState == org.apache.ambari.server.state.State.UNKNOWN)) {
                    return true;
                }
                break;
            default :
                break;
        }
        return false;
    }

    private org.apache.ambari.server.state.ServiceComponent getServiceComponent(java.lang.String clusterName, java.lang.String serviceName, java.lang.String componentName) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.Clusters clusters = getManagementController().getClusters();
        return clusters.getCluster(clusterName).getService(serviceName).getServiceComponent(componentName);
    }

    private void doDirectTransitions(java.util.Map<org.apache.ambari.server.state.ServiceComponentHost, org.apache.ambari.server.state.State> directTransitionScHosts) throws org.apache.ambari.server.AmbariException {
        for (java.util.Map.Entry<org.apache.ambari.server.state.ServiceComponentHost, org.apache.ambari.server.state.State> entry : directTransitionScHosts.entrySet()) {
            org.apache.ambari.server.state.ServiceComponentHost componentHost = entry.getKey();
            org.apache.ambari.server.state.State newState = entry.getValue();
            long timestamp = java.lang.System.currentTimeMillis();
            org.apache.ambari.server.state.ServiceComponentHostEvent event;
            componentHost.setDesiredState(newState);
            switch (newState) {
                case DISABLED :
                    event = new org.apache.ambari.server.state.svccomphost.ServiceComponentHostDisableEvent(componentHost.getServiceComponentName(), componentHost.getHostName(), timestamp);
                    break;
                case INSTALLED :
                    event = new org.apache.ambari.server.state.svccomphost.ServiceComponentHostRestoreEvent(componentHost.getServiceComponentName(), componentHost.getHostName(), timestamp);
                    break;
                default :
                    throw new org.apache.ambari.server.AmbariException(((("Direct transition from " + componentHost.getState()) + " to ") + newState) + " not supported");
            }
            try {
                componentHost.handleEvent(event);
            } catch (org.apache.ambari.server.state.fsm.InvalidStateTransitionException e) {
                throw new org.apache.ambari.server.AmbariException("Internal error - not supported transition", e);
            }
        }
    }

    private void logRequestInfo(java.lang.String msg, org.apache.ambari.server.controller.ServiceComponentHostRequest request) {
        org.apache.ambari.server.controller.internal.HostComponentResourceProvider.LOG.info("{}, clusterName={}, serviceName={}, componentName={}, hostname={}, request={}", msg, request.getClusterName(), request.getServiceName(), request.getComponentName(), request.getHostname(), request);
    }

    private java.lang.String getServiceComponentRequestInfoLogMessage(java.lang.String msg, org.apache.ambari.server.controller.ServiceComponentHostRequest request, org.apache.ambari.server.state.State oldState, org.apache.ambari.server.state.State newDesiredState) {
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        sb.append(msg).append(", clusterName=").append(request.getClusterName()).append(", serviceName=").append(request.getServiceName()).append(", componentName=").append(request.getComponentName()).append(", hostname=").append(request.getHostname()).append(", currentState=").append(oldState == null ? "null" : oldState).append(", newDesiredState=").append(newDesiredState == null ? "null" : newDesiredState);
        return sb.toString();
    }

    private org.apache.ambari.server.controller.spi.Resource.Type determineOperationLevel(java.util.Map<java.lang.String, java.lang.String> requestProperties) {
        org.apache.ambari.server.controller.spi.Resource.Type reqOpLvl;
        if (requestProperties.containsKey(org.apache.ambari.server.controller.internal.RequestOperationLevel.OPERATION_LEVEL_ID)) {
            reqOpLvl = new org.apache.ambari.server.controller.internal.RequestOperationLevel(requestProperties).getLevel();
        } else {
            java.lang.String message = "Can not determine request operation level. " + ("Operation level property should " + "be specified for this request.");
            org.apache.ambari.server.controller.internal.HostComponentResourceProvider.LOG.warn(message);
            reqOpLvl = org.apache.ambari.server.controller.spi.Resource.Type.Cluster;
        }
        return reqOpLvl;
    }

    private void validateServiceComponentHostRequest(org.apache.ambari.server.controller.ServiceComponentHostRequest request) {
        if ((((((request.getClusterName() == null) || request.getClusterName().isEmpty()) || (request.getComponentName() == null)) || request.getComponentName().isEmpty()) || (request.getHostname() == null)) || request.getHostname().isEmpty()) {
            throw new java.lang.IllegalArgumentException("Invalid arguments" + (", cluster name, component name and host name should be" + " provided"));
        }
        if (request.getAdminState() != null) {
            throw new java.lang.IllegalArgumentException("Property adminState cannot be modified through update. Use service " + "specific DECOMMISSION action to decommision/recommission components.");
        }
    }

    private class ClientComponentPredicate implements org.apache.ambari.server.controller.spi.Predicate {
        @java.lang.Override
        public boolean evaluate(org.apache.ambari.server.controller.spi.Resource resource) {
            boolean isClient = false;
            java.lang.String componentName = ((java.lang.String) (resource.getPropertyValue(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.COMPONENT_NAME)));
            try {
                if ((componentName != null) && (!componentName.isEmpty())) {
                    org.apache.ambari.server.controller.AmbariManagementController managementController = getManagementController();
                    java.lang.String clusterName = ((java.lang.String) (resource.getPropertyValue(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.CLUSTER_NAME)));
                    java.lang.String serviceName = ((java.lang.String) (resource.getPropertyValue(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.SERVICE_NAME)));
                    if (org.apache.commons.lang.StringUtils.isEmpty(serviceName)) {
                        org.apache.ambari.server.state.Cluster cluster = managementController.getClusters().getCluster(clusterName);
                        serviceName = managementController.findServiceName(cluster, componentName);
                    }
                    org.apache.ambari.server.state.ServiceComponent sc = getServiceComponent(((java.lang.String) (resource.getPropertyValue(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.CLUSTER_NAME))), serviceName, componentName);
                    isClient = sc.isClientComponent();
                }
            } catch (org.apache.ambari.server.AmbariException e) {
                throw new java.lang.RuntimeException("An unexpected exception occurred while trying to determine if a component is a client", e);
            }
            return isClient;
        }
    }
}