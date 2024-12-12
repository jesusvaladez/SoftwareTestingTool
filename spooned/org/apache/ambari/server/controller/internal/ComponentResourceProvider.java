package org.apache.ambari.server.controller.internal;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import com.google.inject.persist.Transactional;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
public class ComponentResourceProvider extends org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.controller.internal.ComponentResourceProvider.class);

    public static final java.lang.String SERVICE_COMPONENT_INFO = "ServiceComponentInfo";

    public static final java.lang.String CLUSTER_NAME_PROPERTY_ID = "cluster_name";

    public static final java.lang.String SERVICE_NAME_PROPERTY_ID = "service_name";

    public static final java.lang.String COMPONENT_NAME_PROPERTY_ID = "component_name";

    public static final java.lang.String DISPLAY_NAME_PROPERTY_ID = "display_name";

    public static final java.lang.String STATE_PROPERTY_ID = "state";

    public static final java.lang.String CATEGORY_PROPERTY_ID = "category";

    public static final java.lang.String TOTAL_COUNT_PROPERTY_ID = "total_count";

    public static final java.lang.String STARTED_COUNT_PROPERTY_ID = "started_count";

    public static final java.lang.String INSTALLED_COUNT_PROPERTY_ID = "installed_count";

    public static final java.lang.String INSTALLED_AND_MAINTENANCE_OFF_COUNT_PROPERTY_ID = "installed_and_maintenance_off_count";

    public static final java.lang.String INIT_COUNT_PROPERTY_ID = "init_count";

    public static final java.lang.String UNKNOWN_COUNT_PROPERTY_ID = "unknown_count";

    public static final java.lang.String INSTALL_FAILED_COUNT_PROPERTY_ID = "install_failed_count";

    public static final java.lang.String RECOVERY_ENABLED_PROPERTY_ID = "recovery_enabled";

    public static final java.lang.String DESIRED_STACK_PROPERTY_ID = "desired_stack";

    public static final java.lang.String DESIRED_VERSION_PROPERTY_ID = "desired_version";

    public static final java.lang.String REPOSITORY_STATE_PROPERTY_ID = "repository_state";

    public static final java.lang.String CLUSTER_NAME = (org.apache.ambari.server.controller.internal.ComponentResourceProvider.SERVICE_COMPONENT_INFO + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.ComponentResourceProvider.CLUSTER_NAME_PROPERTY_ID;

    public static final java.lang.String SERVICE_NAME = (org.apache.ambari.server.controller.internal.ComponentResourceProvider.SERVICE_COMPONENT_INFO + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.ComponentResourceProvider.SERVICE_NAME_PROPERTY_ID;

    public static final java.lang.String COMPONENT_NAME = (org.apache.ambari.server.controller.internal.ComponentResourceProvider.SERVICE_COMPONENT_INFO + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.ComponentResourceProvider.COMPONENT_NAME_PROPERTY_ID;

    public static final java.lang.String DISPLAY_NAME = (org.apache.ambari.server.controller.internal.ComponentResourceProvider.SERVICE_COMPONENT_INFO + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.ComponentResourceProvider.DISPLAY_NAME_PROPERTY_ID;

    public static final java.lang.String STATE = (org.apache.ambari.server.controller.internal.ComponentResourceProvider.SERVICE_COMPONENT_INFO + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.ComponentResourceProvider.STATE_PROPERTY_ID;

    public static final java.lang.String CATEGORY = (org.apache.ambari.server.controller.internal.ComponentResourceProvider.SERVICE_COMPONENT_INFO + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.ComponentResourceProvider.CATEGORY_PROPERTY_ID;

    public static final java.lang.String TOTAL_COUNT = (org.apache.ambari.server.controller.internal.ComponentResourceProvider.SERVICE_COMPONENT_INFO + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.ComponentResourceProvider.TOTAL_COUNT_PROPERTY_ID;

    public static final java.lang.String STARTED_COUNT = (org.apache.ambari.server.controller.internal.ComponentResourceProvider.SERVICE_COMPONENT_INFO + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.ComponentResourceProvider.STARTED_COUNT_PROPERTY_ID;

    public static final java.lang.String INSTALLED_COUNT = (org.apache.ambari.server.controller.internal.ComponentResourceProvider.SERVICE_COMPONENT_INFO + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.ComponentResourceProvider.INSTALLED_COUNT_PROPERTY_ID;

    public static final java.lang.String INSTALLED_AND_MAINTENANCE_OFF_COUNT = (org.apache.ambari.server.controller.internal.ComponentResourceProvider.SERVICE_COMPONENT_INFO + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.ComponentResourceProvider.INSTALLED_AND_MAINTENANCE_OFF_COUNT_PROPERTY_ID;

    public static final java.lang.String INIT_COUNT = (org.apache.ambari.server.controller.internal.ComponentResourceProvider.SERVICE_COMPONENT_INFO + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.ComponentResourceProvider.INIT_COUNT_PROPERTY_ID;

    public static final java.lang.String UNKNOWN_COUNT = (org.apache.ambari.server.controller.internal.ComponentResourceProvider.SERVICE_COMPONENT_INFO + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.ComponentResourceProvider.UNKNOWN_COUNT_PROPERTY_ID;

    public static final java.lang.String INSTALL_FAILED_COUNT = (org.apache.ambari.server.controller.internal.ComponentResourceProvider.SERVICE_COMPONENT_INFO + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.ComponentResourceProvider.INSTALL_FAILED_COUNT_PROPERTY_ID;

    public static final java.lang.String RECOVERY_ENABLED = (org.apache.ambari.server.controller.internal.ComponentResourceProvider.SERVICE_COMPONENT_INFO + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.ComponentResourceProvider.RECOVERY_ENABLED_PROPERTY_ID;

    public static final java.lang.String DESIRED_STACK = (org.apache.ambari.server.controller.internal.ComponentResourceProvider.SERVICE_COMPONENT_INFO + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.ComponentResourceProvider.DESIRED_STACK_PROPERTY_ID;

    public static final java.lang.String DESIRED_VERSION = (org.apache.ambari.server.controller.internal.ComponentResourceProvider.SERVICE_COMPONENT_INFO + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.ComponentResourceProvider.DESIRED_VERSION_PROPERTY_ID;

    public static final java.lang.String REPOSITORY_STATE = (org.apache.ambari.server.controller.internal.ComponentResourceProvider.SERVICE_COMPONENT_INFO + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.ComponentResourceProvider.REPOSITORY_STATE_PROPERTY_ID;

    private static final java.lang.String TRUE = "true";

    private static final java.lang.String QUERY_PARAMETERS_RUN_SMOKE_TEST_ID = "params/run_smoke_test";

    private static final java.util.Set<java.lang.String> pkPropertyIds = com.google.common.collect.Sets.newHashSet(org.apache.ambari.server.controller.internal.ComponentResourceProvider.CLUSTER_NAME, org.apache.ambari.server.controller.internal.ComponentResourceProvider.SERVICE_NAME, org.apache.ambari.server.controller.internal.ComponentResourceProvider.COMPONENT_NAME);

    private static final java.util.Set<java.lang.String> PROPERTY_IDS = new java.util.HashSet<>();

    private static final java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> KEY_PROPERTY_IDS = new java.util.HashMap<>();

    static {
        PROPERTY_IDS.add(CLUSTER_NAME);
        PROPERTY_IDS.add(SERVICE_NAME);
        PROPERTY_IDS.add(COMPONENT_NAME);
        PROPERTY_IDS.add(DISPLAY_NAME);
        PROPERTY_IDS.add(STATE);
        PROPERTY_IDS.add(CATEGORY);
        PROPERTY_IDS.add(TOTAL_COUNT);
        PROPERTY_IDS.add(STARTED_COUNT);
        PROPERTY_IDS.add(INSTALLED_COUNT);
        PROPERTY_IDS.add(INSTALLED_AND_MAINTENANCE_OFF_COUNT);
        PROPERTY_IDS.add(INIT_COUNT);
        PROPERTY_IDS.add(UNKNOWN_COUNT);
        PROPERTY_IDS.add(INSTALL_FAILED_COUNT);
        PROPERTY_IDS.add(RECOVERY_ENABLED);
        PROPERTY_IDS.add(DESIRED_STACK);
        PROPERTY_IDS.add(DESIRED_VERSION);
        PROPERTY_IDS.add(REPOSITORY_STATE);
        PROPERTY_IDS.add(QUERY_PARAMETERS_RUN_SMOKE_TEST_ID);
        KEY_PROPERTY_IDS.put(org.apache.ambari.server.controller.spi.Resource.Type.Component, COMPONENT_NAME);
        KEY_PROPERTY_IDS.put(org.apache.ambari.server.controller.spi.Resource.Type.Service, SERVICE_NAME);
        KEY_PROPERTY_IDS.put(org.apache.ambari.server.controller.spi.Resource.Type.Cluster, CLUSTER_NAME);
    }

    private org.apache.ambari.server.controller.MaintenanceStateHelper maintenanceStateHelper;

    @com.google.inject.Inject
    private org.apache.ambari.server.topology.STOMPComponentsDeleteHandler STOMPComponentsDeleteHandler;

    @com.google.inject.assistedinject.AssistedInject
    ComponentResourceProvider(@com.google.inject.assistedinject.Assisted
    org.apache.ambari.server.controller.AmbariManagementController managementController, org.apache.ambari.server.controller.MaintenanceStateHelper maintenanceStateHelper) {
        super(org.apache.ambari.server.controller.spi.Resource.Type.Component, org.apache.ambari.server.controller.internal.ComponentResourceProvider.PROPERTY_IDS, org.apache.ambari.server.controller.internal.ComponentResourceProvider.KEY_PROPERTY_IDS, managementController);
        this.maintenanceStateHelper = maintenanceStateHelper;
        setRequiredCreateAuthorizations(java.util.EnumSet.of(org.apache.ambari.server.security.authorization.RoleAuthorization.SERVICE_ADD_DELETE_SERVICES, org.apache.ambari.server.security.authorization.RoleAuthorization.HOST_ADD_DELETE_COMPONENTS));
        setRequiredDeleteAuthorizations(java.util.EnumSet.of(org.apache.ambari.server.security.authorization.RoleAuthorization.SERVICE_ADD_DELETE_SERVICES, org.apache.ambari.server.security.authorization.RoleAuthorization.HOST_ADD_DELETE_COMPONENTS));
        setRequiredGetAuthorizations(org.apache.ambari.server.security.authorization.RoleAuthorization.AUTHORIZATIONS_VIEW_SERVICE);
        setRequiredGetAuthorizations(org.apache.ambari.server.security.authorization.RoleAuthorization.AUTHORIZATIONS_VIEW_SERVICE);
        setRequiredUpdateAuthorizations(org.apache.ambari.server.security.authorization.RoleAuthorization.AUTHORIZATIONS_UPDATE_SERVICE);
    }

    @java.lang.Override
    protected org.apache.ambari.server.controller.spi.RequestStatus createResourcesAuthorized(org.apache.ambari.server.controller.spi.Request request) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.ResourceAlreadyExistsException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        final java.util.Set<org.apache.ambari.server.controller.ServiceComponentRequest> requests = new java.util.HashSet<>();
        for (java.util.Map<java.lang.String, java.lang.Object> propertyMap : request.getProperties()) {
            requests.add(getRequest(propertyMap));
        }
        createResources(new org.apache.ambari.server.controller.internal.AbstractResourceProvider.Command<java.lang.Void>() {
            @java.lang.Override
            public java.lang.Void invoke() throws org.apache.ambari.server.AmbariException, org.apache.ambari.server.security.authorization.AuthorizationException {
                createComponents(requests);
                return null;
            }
        });
        notifyCreate(org.apache.ambari.server.controller.spi.Resource.Type.Component, request);
        return getRequestStatus(null);
    }

    @java.lang.Override
    @com.google.inject.persist.Transactional
    public java.util.Set<org.apache.ambari.server.controller.spi.Resource> getResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        final java.util.Set<org.apache.ambari.server.controller.ServiceComponentRequest> requests = new java.util.HashSet<>();
        for (java.util.Map<java.lang.String, java.lang.Object> propertyMap : getPropertyMaps(predicate)) {
            requests.add(getRequest(propertyMap));
        }
        java.util.Set<org.apache.ambari.server.controller.ServiceComponentResponse> responses = getResources(new org.apache.ambari.server.controller.internal.AbstractResourceProvider.Command<java.util.Set<org.apache.ambari.server.controller.ServiceComponentResponse>>() {
            @java.lang.Override
            public java.util.Set<org.apache.ambari.server.controller.ServiceComponentResponse> invoke() throws org.apache.ambari.server.AmbariException {
                return getComponents(requests);
            }
        });
        java.util.Set<java.lang.String> requestedIds = getRequestPropertyIds(request, predicate);
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = new java.util.HashSet<>();
        for (org.apache.ambari.server.controller.ServiceComponentResponse response : responses) {
            org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Component);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.ComponentResourceProvider.CLUSTER_NAME, response.getClusterName(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.ComponentResourceProvider.SERVICE_NAME, response.getServiceName(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.ComponentResourceProvider.COMPONENT_NAME, response.getComponentName(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.ComponentResourceProvider.DISPLAY_NAME, response.getDisplayName(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.ComponentResourceProvider.STATE, response.getDesiredState(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.ComponentResourceProvider.CATEGORY, response.getCategory(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.ComponentResourceProvider.TOTAL_COUNT, response.getServiceComponentStateCount().get("totalCount"), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.ComponentResourceProvider.STARTED_COUNT, response.getServiceComponentStateCount().get("startedCount"), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.ComponentResourceProvider.INSTALLED_COUNT, response.getServiceComponentStateCount().get("installedCount"), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.ComponentResourceProvider.INSTALLED_AND_MAINTENANCE_OFF_COUNT, response.getServiceComponentStateCount().get("installedAndMaintenanceOffCount"), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.ComponentResourceProvider.INSTALL_FAILED_COUNT, response.getServiceComponentStateCount().get("installFailedCount"), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.ComponentResourceProvider.INIT_COUNT, response.getServiceComponentStateCount().get("initCount"), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.ComponentResourceProvider.UNKNOWN_COUNT, response.getServiceComponentStateCount().get("unknownCount"), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.ComponentResourceProvider.RECOVERY_ENABLED, java.lang.String.valueOf(response.isRecoveryEnabled()), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.ComponentResourceProvider.DESIRED_STACK, response.getDesiredStackId(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.ComponentResourceProvider.DESIRED_VERSION, response.getDesiredVersion(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.ComponentResourceProvider.REPOSITORY_STATE, response.getRepositoryState(), requestedIds);
            resources.add(resource);
        }
        return resources;
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus updateResourcesAuthorized(final org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        final java.util.Set<org.apache.ambari.server.controller.ServiceComponentRequest> requests = new java.util.HashSet<>();
        for (java.util.Map<java.lang.String, java.lang.Object> propertyMap : getPropertyMaps(request.getProperties().iterator().next(), predicate)) {
            requests.add(getRequest(propertyMap));
        }
        java.lang.Object queryParameterValue = org.apache.ambari.server.controller.internal.AbstractResourceProvider.getQueryParameterValue(org.apache.ambari.server.controller.internal.ComponentResourceProvider.QUERY_PARAMETERS_RUN_SMOKE_TEST_ID, predicate);
        final boolean runSmokeTest = org.apache.ambari.server.controller.internal.ComponentResourceProvider.TRUE.equals(queryParameterValue);
        org.apache.ambari.server.controller.RequestStatusResponse response = modifyResources(new org.apache.ambari.server.controller.internal.AbstractResourceProvider.Command<org.apache.ambari.server.controller.RequestStatusResponse>() {
            @java.lang.Override
            public org.apache.ambari.server.controller.RequestStatusResponse invoke() throws org.apache.ambari.server.AmbariException, org.apache.ambari.server.security.authorization.AuthorizationException {
                return updateComponents(requests, request.getRequestInfoProperties(), runSmokeTest);
            }
        });
        notifyUpdate(org.apache.ambari.server.controller.spi.Resource.Type.Component, request, predicate);
        return getRequestStatus(response);
    }

    @java.lang.Override
    protected org.apache.ambari.server.controller.spi.RequestStatus deleteResourcesAuthorized(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        final java.util.Set<org.apache.ambari.server.controller.ServiceComponentRequest> requests = new java.util.HashSet<>();
        for (java.util.Map<java.lang.String, java.lang.Object> propertyMap : getPropertyMaps(predicate)) {
            requests.add(getRequest(propertyMap));
        }
        org.apache.ambari.server.controller.RequestStatusResponse response = modifyResources(new org.apache.ambari.server.controller.internal.AbstractResourceProvider.Command<org.apache.ambari.server.controller.RequestStatusResponse>() {
            @java.lang.Override
            public org.apache.ambari.server.controller.RequestStatusResponse invoke() throws org.apache.ambari.server.AmbariException, org.apache.ambari.server.security.authorization.AuthorizationException {
                return deleteComponents(requests);
            }
        });
        notifyDelete(org.apache.ambari.server.controller.spi.Resource.Type.Component, predicate);
        return getRequestStatus(response);
    }

    @java.lang.Override
    protected java.util.Set<java.lang.String> getPKPropertyIds() {
        return org.apache.ambari.server.controller.internal.ComponentResourceProvider.pkPropertyIds;
    }

    private org.apache.ambari.server.controller.ServiceComponentRequest getRequest(java.util.Map<java.lang.String, java.lang.Object> properties) {
        return new org.apache.ambari.server.controller.ServiceComponentRequest(((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.ComponentResourceProvider.CLUSTER_NAME))), ((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.ComponentResourceProvider.SERVICE_NAME))), ((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.ComponentResourceProvider.COMPONENT_NAME))), ((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.ComponentResourceProvider.STATE))), ((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.ComponentResourceProvider.RECOVERY_ENABLED))), ((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.ComponentResourceProvider.CATEGORY))));
    }

    public void createComponents(java.util.Set<org.apache.ambari.server.controller.ServiceComponentRequest> requests) throws org.apache.ambari.server.AmbariException, org.apache.ambari.server.security.authorization.AuthorizationException {
        if (requests.isEmpty()) {
            org.apache.ambari.server.controller.internal.ComponentResourceProvider.LOG.warn("Received an empty requests set");
            return;
        }
        org.apache.ambari.server.state.Clusters clusters = getManagementController().getClusters();
        org.apache.ambari.server.api.services.AmbariMetaInfo ambariMetaInfo = getManagementController().getAmbariMetaInfo();
        org.apache.ambari.server.state.ServiceComponentFactory serviceComponentFactory = getManagementController().getServiceComponentFactory();
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Set<java.lang.String>>> componentNames = new java.util.HashMap<>();
        java.util.Set<java.lang.String> duplicates = new java.util.HashSet<>();
        for (org.apache.ambari.server.controller.ServiceComponentRequest request : requests) {
            org.apache.commons.lang.Validate.notEmpty(request.getComponentName(), "component name should be non-empty");
            org.apache.ambari.server.state.Cluster cluster = getClusterForRequest(request, clusters);
            isAuthorized(cluster, getRequiredCreateAuthorizations());
            setServiceNameIfAbsent(request, cluster, ambariMetaInfo);
            debug("Received a createComponent request: {}", request);
            if (!componentNames.containsKey(request.getClusterName())) {
                componentNames.put(request.getClusterName(), new java.util.HashMap<>());
            }
            java.util.Map<java.lang.String, java.util.Set<java.lang.String>> serviceComponents = componentNames.get(request.getClusterName());
            if (!serviceComponents.containsKey(request.getServiceName())) {
                serviceComponents.put(request.getServiceName(), new java.util.HashSet<>());
            }
            if (serviceComponents.get(request.getServiceName()).contains(request.getComponentName())) {
                duplicates.add(request.toString());
                continue;
            }
            serviceComponents.get(request.getServiceName()).add(request.getComponentName());
            if (org.apache.commons.lang.StringUtils.isNotEmpty(request.getDesiredState())) {
                org.apache.commons.lang.Validate.isTrue(org.apache.ambari.server.state.State.INIT == org.apache.ambari.server.state.State.valueOf(request.getDesiredState()), "Invalid desired state only INIT state allowed during creation, providedDesiredState=" + request.getDesiredState());
            }
            org.apache.ambari.server.state.Service s = getServiceFromCluster(request, cluster);
            try {
                org.apache.ambari.server.state.ServiceComponent sc = s.getServiceComponent(request.getComponentName());
                if (sc != null) {
                    duplicates.add(request.toString());
                    continue;
                }
            } catch (org.apache.ambari.server.AmbariException e) {
            }
            org.apache.ambari.server.state.StackId stackId = s.getDesiredStackId();
            if (!ambariMetaInfo.isValidServiceComponent(stackId.getStackName(), stackId.getStackVersion(), s.getName(), request.getComponentName())) {
                throw new java.lang.IllegalArgumentException(((("Unsupported or invalid component" + " in stack stackInfo=") + stackId.getStackId()) + " request=") + request);
            }
        }
        org.apache.commons.lang.Validate.isTrue(componentNames.size() == 1, "Invalid arguments, updates allowed on only one cluster at a time");
        if (!duplicates.isEmpty()) {
            throw new org.apache.ambari.server.DuplicateResourceException("Attempted to create one or more components which already exist:" + org.apache.commons.lang.StringUtils.join(duplicates, ","));
        }
        for (org.apache.ambari.server.controller.ServiceComponentRequest request : requests) {
            org.apache.ambari.server.state.Cluster cluster = clusters.getCluster(request.getClusterName());
            org.apache.ambari.server.state.Service s = cluster.getService(request.getServiceName());
            org.apache.ambari.server.state.ServiceComponent sc = serviceComponentFactory.createNew(s, request.getComponentName());
            sc.setDesiredRepositoryVersion(s.getDesiredRepositoryVersion());
            if (org.apache.commons.lang.StringUtils.isNotEmpty(request.getDesiredState())) {
                org.apache.ambari.server.state.State state = org.apache.ambari.server.state.State.valueOf(request.getDesiredState());
                sc.setDesiredState(state);
            } else {
                sc.setDesiredState(s.getDesiredState());
            }
            if (org.apache.commons.lang.StringUtils.isNotEmpty(request.getRecoveryEnabled())) {
                boolean recoveryEnabled = java.lang.Boolean.parseBoolean(request.getRecoveryEnabled());
                sc.setRecoveryEnabled(recoveryEnabled);
                org.apache.ambari.server.controller.internal.ComponentResourceProvider.LOG.info("Component: {}, recovery_enabled from request: {}", request.getComponentName(), recoveryEnabled);
            } else {
                org.apache.ambari.server.state.StackId stackId = s.getDesiredStackId();
                org.apache.ambari.server.state.ComponentInfo componentInfo = ambariMetaInfo.getComponent(stackId.getStackName(), stackId.getStackVersion(), s.getName(), request.getComponentName());
                if (componentInfo == null) {
                    throw new org.apache.ambari.server.AmbariException((((("Could not get component information from stack definition: Stack=" + stackId) + ", Service=") + s.getName()) + ", Component=") + request.getComponentName());
                }
                sc.setRecoveryEnabled(componentInfo.isRecoveryEnabled());
                org.apache.ambari.server.controller.internal.ComponentResourceProvider.LOG.info("Component: {}, recovery_enabled from stack definition:{}", componentInfo.getName(), componentInfo.isRecoveryEnabled());
            }
            s.addServiceComponent(sc);
        }
    }

    protected java.util.Set<org.apache.ambari.server.controller.ServiceComponentResponse> getComponents(java.util.Set<org.apache.ambari.server.controller.ServiceComponentRequest> requests) throws org.apache.ambari.server.AmbariException {
        java.util.Set<org.apache.ambari.server.controller.ServiceComponentResponse> response = new java.util.HashSet<>();
        for (org.apache.ambari.server.controller.ServiceComponentRequest request : requests) {
            try {
                response.addAll(getComponents(request));
            } catch (org.apache.ambari.server.ObjectNotFoundException e) {
                if (requests.size() == 1) {
                    throw e;
                }
            }
        }
        return response;
    }

    private java.util.Set<org.apache.ambari.server.controller.ServiceComponentResponse> getComponents(org.apache.ambari.server.controller.ServiceComponentRequest request) throws org.apache.ambari.server.AmbariException {
        final org.apache.ambari.server.api.services.AmbariMetaInfo ambariMetaInfo = getManagementController().getAmbariMetaInfo();
        final org.apache.ambari.server.state.Clusters clusters = getManagementController().getClusters();
        final org.apache.ambari.server.state.Cluster cluster = getCluster(request, clusters);
        java.util.Set<org.apache.ambari.server.controller.ServiceComponentResponse> response = new java.util.HashSet<>();
        java.lang.String category = null;
        if (request.getComponentName() != null) {
            setServiceNameIfAbsent(request, cluster, ambariMetaInfo);
            final org.apache.ambari.server.state.Service s = getServiceFromCluster(request, cluster);
            org.apache.ambari.server.state.ServiceComponent sc = s.getServiceComponent(request.getComponentName());
            org.apache.ambari.server.controller.ServiceComponentResponse serviceComponentResponse = sc.convertToResponse();
            org.apache.ambari.server.state.StackId stackId = sc.getDesiredStackId();
            try {
                org.apache.ambari.server.state.ComponentInfo componentInfo = ambariMetaInfo.getComponent(stackId.getStackName(), stackId.getStackVersion(), s.getName(), request.getComponentName());
                category = componentInfo.getCategory();
                if (category != null) {
                    serviceComponentResponse.setCategory(category);
                }
            } catch (org.apache.ambari.server.ObjectNotFoundException e) {
            }
            response.add(serviceComponentResponse);
            return response;
        }
        java.util.Set<org.apache.ambari.server.state.Service> services = new java.util.HashSet<>();
        if (org.apache.commons.lang.StringUtils.isNotEmpty(request.getServiceName())) {
            services.add(getServiceFromCluster(request, cluster));
        } else {
            services.addAll(cluster.getServices().values());
        }
        final org.apache.ambari.server.state.State desiredStateToCheck = getValidDesiredState(request);
        for (org.apache.ambari.server.state.Service s : services) {
            for (org.apache.ambari.server.state.ServiceComponent sc : s.getServiceComponents().values()) {
                if ((desiredStateToCheck != null) && (desiredStateToCheck != sc.getDesiredState())) {
                    continue;
                }
                org.apache.ambari.server.state.StackId stackId = sc.getDesiredStackId();
                org.apache.ambari.server.controller.ServiceComponentResponse serviceComponentResponse = sc.convertToResponse();
                try {
                    org.apache.ambari.server.state.ComponentInfo componentInfo = ambariMetaInfo.getComponent(stackId.getStackName(), stackId.getStackVersion(), s.getName(), sc.getName());
                    category = componentInfo.getCategory();
                    if (category != null) {
                        serviceComponentResponse.setCategory(category);
                    }
                } catch (org.apache.ambari.server.ObjectNotFoundException e) {
                }
                java.lang.String requestedCategory = request.getComponentCategory();
                if (org.apache.commons.lang.StringUtils.isNotEmpty(requestedCategory) && (!requestedCategory.equalsIgnoreCase(category))) {
                    continue;
                }
                response.add(serviceComponentResponse);
            }
        }
        return response;
    }

    protected org.apache.ambari.server.controller.RequestStatusResponse updateComponents(java.util.Set<org.apache.ambari.server.controller.ServiceComponentRequest> requests, java.util.Map<java.lang.String, java.lang.String> requestProperties, boolean runSmokeTest) throws org.apache.ambari.server.AmbariException, org.apache.ambari.server.security.authorization.AuthorizationException {
        if (requests.isEmpty()) {
            org.apache.ambari.server.controller.internal.ComponentResourceProvider.LOG.warn("Received an empty requests set");
            return null;
        }
        org.apache.ambari.server.state.Clusters clusters = getManagementController().getClusters();
        org.apache.ambari.server.api.services.AmbariMetaInfo ambariMetaInfo = getManagementController().getAmbariMetaInfo();
        java.util.Map<org.apache.ambari.server.state.State, java.util.List<org.apache.ambari.server.state.ServiceComponent>> changedComps = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.util.Map<org.apache.ambari.server.state.State, java.util.List<org.apache.ambari.server.state.ServiceComponentHost>>> changedScHosts = new java.util.HashMap<>();
        java.util.Collection<org.apache.ambari.server.state.ServiceComponentHost> ignoredScHosts = new java.util.ArrayList<>();
        java.util.Set<java.lang.String> clusterNames = new java.util.HashSet<>();
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Set<java.lang.String>>> componentNames = new java.util.HashMap<>();
        java.util.Set<org.apache.ambari.server.state.State> seenNewStates = new java.util.HashSet<>();
        java.util.Collection<org.apache.ambari.server.state.ServiceComponent> recoveryEnabledComponents = new java.util.ArrayList<>();
        java.util.Collection<org.apache.ambari.server.state.ServiceComponent> recoveryDisabledComponents = new java.util.ArrayList<>();
        org.apache.ambari.server.controller.spi.Resource.Type reqOpLvl;
        if (requestProperties.containsKey(org.apache.ambari.server.controller.internal.RequestOperationLevel.OPERATION_LEVEL_ID)) {
            org.apache.ambari.server.controller.internal.RequestOperationLevel operationLevel = new org.apache.ambari.server.controller.internal.RequestOperationLevel(requestProperties);
            reqOpLvl = operationLevel.getLevel();
        } else {
            org.apache.ambari.server.controller.internal.ComponentResourceProvider.LOG.warn("Can not determine request operation level. Operation level property should be specified for this request.");
            reqOpLvl = org.apache.ambari.server.controller.spi.Resource.Type.Cluster;
        }
        for (org.apache.ambari.server.controller.ServiceComponentRequest request : requests) {
            org.apache.commons.lang.Validate.notEmpty(request.getComponentName(), "component name should be non-empty");
            final org.apache.ambari.server.state.Cluster cluster = getClusterForRequest(request, clusters);
            final java.lang.String clusterName = request.getClusterName();
            final java.lang.String componentName = request.getComponentName();
            org.apache.ambari.server.controller.internal.ComponentResourceProvider.LOG.info("Received a updateComponent request: {}", request);
            setServiceNameIfAbsent(request, cluster, ambariMetaInfo);
            final java.lang.String serviceName = request.getServiceName();
            debug("Received a updateComponent request: {}", request);
            clusterNames.add(clusterName);
            org.apache.commons.lang.Validate.isTrue(clusterNames.size() == 1, "Updates to multiple clusters is not supported");
            if (!componentNames.containsKey(clusterName)) {
                componentNames.put(clusterName, new java.util.HashMap<>());
            }
            if (!componentNames.get(clusterName).containsKey(serviceName)) {
                componentNames.get(clusterName).put(serviceName, new java.util.HashSet<>());
            }
            if (componentNames.get(clusterName).get(serviceName).contains(componentName)) {
                throw new java.lang.IllegalArgumentException("Invalid request contains duplicate service components");
            }
            componentNames.get(clusterName).get(serviceName).add(componentName);
            org.apache.ambari.server.state.Service s = cluster.getService(serviceName);
            org.apache.ambari.server.state.ServiceComponent sc = s.getServiceComponent(componentName);
            org.apache.ambari.server.state.State newState = getValidDesiredState(request);
            if (!maintenanceStateHelper.isOperationAllowed(reqOpLvl, s)) {
                org.apache.ambari.server.controller.internal.ComponentResourceProvider.LOG.info((((("Operations cannot be applied to component " + componentName) + " because service ") + serviceName) + " is in the maintenance state of ") + s.getMaintenanceState());
                continue;
            }
            if (!org.apache.commons.lang.StringUtils.isEmpty(request.getRecoveryEnabled())) {
                org.apache.ambari.server.security.authorization.AuthorizationHelper.verifyAuthorization(org.apache.ambari.server.security.authorization.ResourceType.CLUSTER, getClusterResourceId(clusterName), java.util.EnumSet.of(org.apache.ambari.server.security.authorization.RoleAuthorization.CLUSTER_MANAGE_AUTO_START, org.apache.ambari.server.security.authorization.RoleAuthorization.SERVICE_MANAGE_AUTO_START));
                boolean newRecoveryEnabled = java.lang.Boolean.parseBoolean(request.getRecoveryEnabled());
                boolean oldRecoveryEnabled = sc.isRecoveryEnabled();
                org.apache.ambari.server.controller.internal.ComponentResourceProvider.LOG.info("Component: {}, oldRecoveryEnabled: {}, newRecoveryEnabled {}", componentName, oldRecoveryEnabled, newRecoveryEnabled);
                if (newRecoveryEnabled != oldRecoveryEnabled) {
                    if (newRecoveryEnabled) {
                        recoveryEnabledComponents.add(sc);
                    } else {
                        recoveryDisabledComponents.add(sc);
                    }
                }
            }
            if (newState == null) {
                debug("Nothing to do for new updateServiceComponent request, request ={}, newDesiredState=null" + request);
                continue;
            }
            if (sc.isClientComponent() && (!newState.isValidClientComponentState())) {
                throw new org.apache.ambari.server.AmbariException("Invalid desired state for a client component");
            }
            seenNewStates.add(newState);
            org.apache.ambari.server.state.State oldScState = sc.getDesiredState();
            if (newState != oldScState) {
                if ((newState == org.apache.ambari.server.state.State.INSTALLED) || (newState == org.apache.ambari.server.state.State.STARTED)) {
                    isAuthorized(cluster, org.apache.ambari.server.security.authorization.RoleAuthorization.SERVICE_START_STOP);
                }
                if (!org.apache.ambari.server.state.State.isValidDesiredStateTransition(oldScState, newState)) {
                    throw new org.apache.ambari.server.AmbariException(((((((((((((("Invalid transition for" + (" servicecomponent" + ", clusterName=")) + cluster.getClusterName()) + ", clusterId=") + cluster.getClusterId()) + ", serviceName=") + sc.getServiceName()) + ", componentName=") + sc.getName()) + ", recoveryEnabled=") + sc.isRecoveryEnabled()) + ", currentDesiredState=") + oldScState) + ", newDesiredState=") + newState);
                }
                if (!changedComps.containsKey(newState)) {
                    changedComps.put(newState, new java.util.ArrayList<>());
                }
                debug(((((((((((("Handling update to ServiceComponent" + ", clusterName=") + clusterName) + ", serviceName=") + serviceName) + ", componentName=") + sc.getName()) + ", recoveryEnabled=") + sc.isRecoveryEnabled()) + ", currentDesiredState=") + oldScState) + ", newDesiredState=") + newState);
                changedComps.get(newState).add(sc);
            }
            for (org.apache.ambari.server.state.ServiceComponentHost sch : sc.getServiceComponentHosts().values()) {
                org.apache.ambari.server.state.State oldSchState = sch.getState();
                if ((oldSchState == org.apache.ambari.server.state.State.DISABLED) || (oldSchState == org.apache.ambari.server.state.State.UNKNOWN)) {
                    debug(((((((((((((("Ignoring ServiceComponentHost" + ", clusterName=") + clusterName) + ", serviceName=") + serviceName) + ", componentName=") + sc.getName()) + ", recoveryEnabled=") + sc.isRecoveryEnabled()) + ", hostname=") + sch.getHostName()) + ", currentState=") + oldSchState) + ", newDesiredState=") + newState);
                    continue;
                }
                if (newState == oldSchState) {
                    ignoredScHosts.add(sch);
                    debug(((((((((((((("Ignoring ServiceComponentHost" + ", clusterName=") + clusterName) + ", serviceName=") + serviceName) + ", componentName=") + sc.getName()) + ", recoveryEnabled=") + sc.isRecoveryEnabled()) + ", hostname=") + sch.getHostName()) + ", currentState=") + oldSchState) + ", newDesiredState=") + newState);
                    continue;
                }
                if (!maintenanceStateHelper.isOperationAllowed(reqOpLvl, sch)) {
                    ignoredScHosts.add(sch);
                    debug(((((((((("Ignoring ServiceComponentHost in maintenance state" + ", clusterName=") + clusterName) + ", serviceName=") + serviceName) + ", componentName=") + sc.getName()) + ", recoveryEnabled=") + sc.isRecoveryEnabled()) + ", hostname=") + sch.getHostName());
                    continue;
                }
                if (!org.apache.ambari.server.state.State.isValidStateTransition(oldSchState, newState)) {
                    throw new org.apache.ambari.server.AmbariException(((((((((((((((("Invalid transition for" + (" servicecomponenthost" + ", clusterName=")) + cluster.getClusterName()) + ", clusterId=") + cluster.getClusterId()) + ", serviceName=") + sch.getServiceName()) + ", componentName=") + sch.getServiceComponentName()) + ", recoveryEnabled=") + sc.isRecoveryEnabled()) + ", hostname=") + sch.getHostName()) + ", currentState=") + oldSchState) + ", newDesiredState=") + newState);
                }
                if (!changedScHosts.containsKey(sc.getName())) {
                    changedScHosts.put(sc.getName(), new java.util.HashMap<>());
                }
                if (!changedScHosts.get(sc.getName()).containsKey(newState)) {
                    changedScHosts.get(sc.getName()).put(newState, new java.util.ArrayList<>());
                }
                debug(((((((((((((("Handling update to ServiceComponentHost" + ", clusterName=") + clusterName) + ", serviceName=") + serviceName) + ", componentName=") + sc.getName()) + ", recoveryEnabled=") + sc.isRecoveryEnabled()) + ", hostname=") + sch.getHostName()) + ", currentState=") + oldSchState) + ", newDesiredState=") + newState);
                changedScHosts.get(sc.getName()).get(newState).add(sch);
            }
        }
        org.apache.commons.lang.Validate.isTrue(seenNewStates.size() <= 1, "Cannot handle different desired state changes for a set of service components at the same time");
        for (org.apache.ambari.server.state.ServiceComponent sc : recoveryEnabledComponents) {
            sc.setRecoveryEnabled(true);
        }
        for (org.apache.ambari.server.state.ServiceComponent sc : recoveryDisabledComponents) {
            sc.setRecoveryEnabled(false);
        }
        org.apache.ambari.server.state.Cluster cluster = clusters.getCluster(clusterNames.iterator().next());
        return getManagementController().createAndPersistStages(cluster, requestProperties, null, null, changedComps, changedScHosts, ignoredScHosts, runSmokeTest, false);
    }

    protected org.apache.ambari.server.controller.RequestStatusResponse deleteComponents(java.util.Set<org.apache.ambari.server.controller.ServiceComponentRequest> requests) throws org.apache.ambari.server.AmbariException, org.apache.ambari.server.security.authorization.AuthorizationException {
        org.apache.ambari.server.state.Clusters clusters = getManagementController().getClusters();
        org.apache.ambari.server.api.services.AmbariMetaInfo ambariMetaInfo = getManagementController().getAmbariMetaInfo();
        org.apache.ambari.server.controller.internal.DeleteHostComponentStatusMetaData deleteMetaData = new org.apache.ambari.server.controller.internal.DeleteHostComponentStatusMetaData();
        for (org.apache.ambari.server.controller.ServiceComponentRequest request : requests) {
            org.apache.commons.lang.Validate.notEmpty(request.getComponentName(), "component name should be non-empty");
            org.apache.ambari.server.state.Cluster cluster = getClusterForRequest(request, clusters);
            setServiceNameIfAbsent(request, cluster, ambariMetaInfo);
            org.apache.ambari.server.state.Service s = getServiceFromCluster(request, cluster);
            org.apache.ambari.server.state.ServiceComponent sc = s.getServiceComponent(request.getComponentName());
            if (sc != null) {
                deleteHostComponentsForServiceComponent(sc, request, deleteMetaData);
                STOMPComponentsDeleteHandler.processDeleteByMetaDataException(deleteMetaData);
                sc.setDesiredState(org.apache.ambari.server.state.State.DISABLED);
                s.deleteServiceComponent(request.getComponentName(), deleteMetaData);
                STOMPComponentsDeleteHandler.processDeleteByMetaDataException(deleteMetaData);
            }
        }
        STOMPComponentsDeleteHandler.processDeleteByMetaData(deleteMetaData);
        return null;
    }

    private void deleteHostComponentsForServiceComponent(org.apache.ambari.server.state.ServiceComponent sc, org.apache.ambari.server.controller.ServiceComponentRequest request, org.apache.ambari.server.controller.internal.DeleteHostComponentStatusMetaData deleteMetaData) throws org.apache.ambari.server.AmbariException {
        for (org.apache.ambari.server.state.ServiceComponentHost sch : sc.getServiceComponentHosts().values()) {
            if (!sch.getDesiredState().isRemovableState()) {
                deleteMetaData.setAmbariException(new org.apache.ambari.server.AmbariException((((("Found non removable host component when trying to delete service component." + ((" To remove host component, it must be in DISABLED/INIT/INSTALLED/INSTALL_FAILED/UNKNOWN" + "/UNINSTALLED/INSTALLING state.") + ", request=")) + request.toString()) + ", current state=") + sc.getDesiredState()) + "."));
                return;
            }
        }
        for (org.apache.ambari.server.state.ServiceComponentHost sch : sc.getServiceComponentHosts().values()) {
            sch.delete(deleteMetaData);
        }
    }

    private org.apache.ambari.server.state.Cluster getClusterForRequest(final org.apache.ambari.server.controller.ServiceComponentRequest request, final org.apache.ambari.server.state.Clusters clusters) throws org.apache.ambari.server.AmbariException {
        org.apache.commons.lang.Validate.notEmpty(request.getClusterName(), "cluster name should be non-empty");
        try {
            return clusters.getCluster(request.getClusterName());
        } catch (org.apache.ambari.server.ClusterNotFoundException e) {
            throw new org.apache.ambari.server.ParentObjectNotFoundException("Attempted to add a component to a cluster which doesn't exist:", e);
        }
    }

    private org.apache.ambari.server.state.Service getServiceFromCluster(final org.apache.ambari.server.controller.ServiceComponentRequest request, final org.apache.ambari.server.state.Cluster cluster) throws org.apache.ambari.server.AmbariException {
        try {
            return cluster.getService(request.getServiceName());
        } catch (org.apache.ambari.server.ServiceNotFoundException e) {
            throw new org.apache.ambari.server.ParentObjectNotFoundException("Parent Service resource doesn't exist.", e);
        }
    }

    private org.apache.ambari.server.state.Cluster getCluster(final org.apache.ambari.server.controller.ServiceComponentRequest request, final org.apache.ambari.server.state.Clusters clusters) throws org.apache.ambari.server.AmbariException {
        org.apache.commons.lang.Validate.notEmpty(request.getClusterName(), "cluster name should be non-empty");
        try {
            return clusters.getCluster(request.getClusterName());
        } catch (org.apache.ambari.server.ObjectNotFoundException e) {
            throw new org.apache.ambari.server.ParentObjectNotFoundException("Parent Cluster resource doesn't exist", e);
        }
    }

    private void isAuthorized(final org.apache.ambari.server.state.Cluster cluster, final org.apache.ambari.server.security.authorization.RoleAuthorization roleAuthorization) throws org.apache.ambari.server.security.authorization.AuthorizationException {
        isAuthorized(cluster, java.util.EnumSet.of(roleAuthorization));
    }

    private void isAuthorized(final org.apache.ambari.server.state.Cluster cluster, final java.util.Set<org.apache.ambari.server.security.authorization.RoleAuthorization> requiredAuthorizations) throws org.apache.ambari.server.security.authorization.AuthorizationException {
        if (!org.apache.ambari.server.security.authorization.AuthorizationHelper.isAuthorized(org.apache.ambari.server.security.authorization.ResourceType.CLUSTER, cluster.getResourceId(), requiredAuthorizations)) {
            throw new org.apache.ambari.server.security.authorization.AuthorizationException("The user is not authorized to for roles " + requiredAuthorizations);
        }
    }

    private void setServiceNameIfAbsent(final org.apache.ambari.server.controller.ServiceComponentRequest request, final org.apache.ambari.server.state.Cluster cluster, final org.apache.ambari.server.api.services.AmbariMetaInfo ambariMetaInfo) throws org.apache.ambari.server.AmbariException {
        if (org.apache.commons.lang.StringUtils.isEmpty(request.getServiceName())) {
            java.lang.String componentName = request.getComponentName();
            java.lang.String serviceName = getManagementController().findServiceName(cluster, componentName);
            debug("Looking up service name for component, componentName={}, serviceName={}", componentName, serviceName);
            if (org.apache.commons.lang.StringUtils.isEmpty(serviceName)) {
                throw new org.apache.ambari.server.AmbariException(((("Could not find service for component" + ", componentName=") + request.getComponentName()) + ", clusterName=") + cluster.getClusterName());
            }
            request.setServiceName(serviceName);
        }
    }

    private org.apache.ambari.server.state.State getValidDesiredState(org.apache.ambari.server.controller.ServiceComponentRequest request) {
        if (org.apache.commons.lang.StringUtils.isEmpty(request.getDesiredState())) {
            return null;
        }
        final org.apache.ambari.server.state.State desiredStateToCheck = org.apache.ambari.server.state.State.valueOf(request.getDesiredState());
        org.apache.commons.lang.Validate.isTrue(desiredStateToCheck.isValidDesiredState(), "Invalid arguments, invalid desired state, desiredState=" + desiredStateToCheck);
        return desiredStateToCheck;
    }

    private void debug(java.lang.String format, java.lang.Object... arguments) {
        if (org.apache.ambari.server.controller.internal.ComponentResourceProvider.LOG.isDebugEnabled()) {
            org.apache.ambari.server.controller.internal.ComponentResourceProvider.LOG.debug(format, arguments);
        }
    }
}