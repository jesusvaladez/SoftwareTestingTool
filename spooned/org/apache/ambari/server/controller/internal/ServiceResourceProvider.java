package org.apache.ambari.server.controller.internal;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
public class ServiceResourceProvider extends org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.controller.internal.ServiceResourceProvider.class);

    private static final org.apache.ambari.server.utils.LoggingPreconditions CHECK = new org.apache.ambari.server.utils.LoggingPreconditions(org.apache.ambari.server.controller.internal.ServiceResourceProvider.LOG);

    public static final java.lang.String SERVICE_CLUSTER_NAME_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("ServiceInfo", "cluster_name");

    public static final java.lang.String SERVICE_SERVICE_NAME_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("ServiceInfo", "service_name");

    public static final java.lang.String SERVICE_SERVICE_STATE_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("ServiceInfo", "state");

    public static final java.lang.String SERVICE_MAINTENANCE_STATE_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("ServiceInfo", "maintenance_state");

    public static final java.lang.String SERVICE_CREDENTIAL_STORE_SUPPORTED_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("ServiceInfo", "credential_store_supported");

    public static final java.lang.String SERVICE_CREDENTIAL_STORE_ENABLED_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("ServiceInfo", "credential_store_enabled");

    public static final java.lang.String SERVICE_ATTRIBUTES_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Services", "attributes");

    public static final java.lang.String SERVICE_DESIRED_STACK_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("ServiceInfo", "desired_stack");

    public static final java.lang.String SERVICE_DESIRED_REPO_VERSION_ID_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("ServiceInfo", "desired_repository_version_id");

    private static final java.lang.String SSO_INTEGRATION_SUPPORTED_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("ServiceInfo", "sso_integration_supported");

    private static final java.lang.String SSO_INTEGRATION_ENABLED_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("ServiceInfo", "sso_integration_enabled");

    private static final java.lang.String SSO_INTEGRATION_DESIRED_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("ServiceInfo", "sso_integration_desired");

    private static final java.lang.String SSO_INTEGRATION_REQUIRES_KERBEROS_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("ServiceInfo", "sso_integration_requires_kerberos");

    private static final java.lang.String KERBEROS_ENABLED_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("ServiceInfo", "kerberos_enabled");

    private static final java.lang.String LDAP_INTEGRATION_SUPPORTED_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("ServiceInfo", "ldap_integration_supported");

    private static final java.lang.String LDAP_INTEGRATION_ENABLED_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("ServiceInfo", "ldap_integration_enabled");

    private static final java.lang.String LDAP_INTEGRATION_DESIRED_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("ServiceInfo", "ldap_integration_desired");

    public static final java.lang.String OPERATION_TYPE = "operation_type";

    protected static final java.lang.String SERVICE_REPOSITORY_STATE = "ServiceInfo/repository_state";

    private static final java.lang.String QUERY_PARAMETERS_RUN_SMOKE_TEST_ID = "params/run_smoke_test";

    private static final java.lang.String QUERY_PARAMETERS_RECONFIGURE_CLIENT = "params/reconfigure_client";

    private static final java.lang.String QUERY_PARAMETERS_START_DEPENDENCIES = "params/start_dependencies";

    private static final java.util.Set<java.lang.String> pkPropertyIds = new java.util.HashSet<>(java.util.Arrays.asList(new java.lang.String[]{ org.apache.ambari.server.controller.internal.ServiceResourceProvider.SERVICE_CLUSTER_NAME_PROPERTY_ID, org.apache.ambari.server.controller.internal.ServiceResourceProvider.SERVICE_SERVICE_NAME_PROPERTY_ID }));

    private static final java.util.Set<java.lang.String> PROPERTY_IDS = new java.util.HashSet<>();

    private static final java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> KEY_PROPERTY_IDS = new java.util.HashMap<>();

    static {
        PROPERTY_IDS.add(SERVICE_CLUSTER_NAME_PROPERTY_ID);
        PROPERTY_IDS.add(SERVICE_SERVICE_NAME_PROPERTY_ID);
        PROPERTY_IDS.add(SERVICE_SERVICE_STATE_PROPERTY_ID);
        PROPERTY_IDS.add(SERVICE_MAINTENANCE_STATE_PROPERTY_ID);
        PROPERTY_IDS.add(SERVICE_CREDENTIAL_STORE_SUPPORTED_PROPERTY_ID);
        PROPERTY_IDS.add(SERVICE_CREDENTIAL_STORE_ENABLED_PROPERTY_ID);
        PROPERTY_IDS.add(SERVICE_ATTRIBUTES_PROPERTY_ID);
        PROPERTY_IDS.add(SERVICE_DESIRED_STACK_PROPERTY_ID);
        PROPERTY_IDS.add(SERVICE_DESIRED_REPO_VERSION_ID_PROPERTY_ID);
        PROPERTY_IDS.add(SERVICE_REPOSITORY_STATE);
        PROPERTY_IDS.add(QUERY_PARAMETERS_RUN_SMOKE_TEST_ID);
        PROPERTY_IDS.add(QUERY_PARAMETERS_RECONFIGURE_CLIENT);
        PROPERTY_IDS.add(QUERY_PARAMETERS_START_DEPENDENCIES);
        PROPERTY_IDS.add(SSO_INTEGRATION_SUPPORTED_PROPERTY_ID);
        PROPERTY_IDS.add(SSO_INTEGRATION_ENABLED_PROPERTY_ID);
        PROPERTY_IDS.add(SSO_INTEGRATION_DESIRED_PROPERTY_ID);
        PROPERTY_IDS.add(SSO_INTEGRATION_REQUIRES_KERBEROS_PROPERTY_ID);
        PROPERTY_IDS.add(KERBEROS_ENABLED_PROPERTY_ID);
        PROPERTY_IDS.add(LDAP_INTEGRATION_SUPPORTED_PROPERTY_ID);
        PROPERTY_IDS.add(LDAP_INTEGRATION_ENABLED_PROPERTY_ID);
        PROPERTY_IDS.add(LDAP_INTEGRATION_DESIRED_PROPERTY_ID);
        PROPERTY_IDS.add(OPERATION_TYPE);
        PROPERTY_IDS.add(org.apache.ambari.server.controller.internal.ClusterResourceProvider.SECURITY);
        PROPERTY_IDS.add(org.apache.ambari.server.controller.internal.ClusterResourceProvider.CREDENTIALS);
        KEY_PROPERTY_IDS.put(org.apache.ambari.server.controller.spi.Resource.Type.Service, SERVICE_SERVICE_NAME_PROPERTY_ID);
        KEY_PROPERTY_IDS.put(org.apache.ambari.server.controller.spi.Resource.Type.Cluster, SERVICE_CLUSTER_NAME_PROPERTY_ID);
    }

    private org.apache.ambari.server.controller.MaintenanceStateHelper maintenanceStateHelper;

    @com.google.inject.Inject
    private org.apache.ambari.server.controller.KerberosHelper kerberosHelper;

    @com.google.inject.Inject
    private org.apache.ambari.server.topology.STOMPComponentsDeleteHandler STOMPComponentsDeleteHandler;

    @com.google.inject.Inject
    private org.apache.ambari.server.topology.addservice.AddServiceOrchestrator addServiceOrchestrator;

    private final org.apache.ambari.server.orm.dao.RepositoryVersionDAO repositoryVersionDAO;

    @com.google.inject.assistedinject.AssistedInject
    public ServiceResourceProvider(@com.google.inject.assistedinject.Assisted
    org.apache.ambari.server.controller.AmbariManagementController managementController, org.apache.ambari.server.controller.MaintenanceStateHelper maintenanceStateHelper, org.apache.ambari.server.orm.dao.RepositoryVersionDAO repositoryVersionDAO) {
        super(org.apache.ambari.server.controller.spi.Resource.Type.Service, org.apache.ambari.server.controller.internal.ServiceResourceProvider.PROPERTY_IDS, org.apache.ambari.server.controller.internal.ServiceResourceProvider.KEY_PROPERTY_IDS, managementController);
        this.maintenanceStateHelper = maintenanceStateHelper;
        this.repositoryVersionDAO = repositoryVersionDAO;
        setRequiredCreateAuthorizations(java.util.EnumSet.of(org.apache.ambari.server.security.authorization.RoleAuthorization.SERVICE_ADD_DELETE_SERVICES));
        setRequiredUpdateAuthorizations(org.apache.ambari.server.security.authorization.RoleAuthorization.AUTHORIZATIONS_UPDATE_SERVICE);
        setRequiredGetAuthorizations(org.apache.ambari.server.security.authorization.RoleAuthorization.AUTHORIZATIONS_VIEW_SERVICE);
        setRequiredDeleteAuthorizations(java.util.EnumSet.of(org.apache.ambari.server.security.authorization.RoleAuthorization.SERVICE_ADD_DELETE_SERVICES));
    }

    @java.lang.Override
    protected org.apache.ambari.server.controller.spi.RequestStatus createResourcesAuthorized(org.apache.ambari.server.controller.spi.Request request) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.ResourceAlreadyExistsException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        if (request.getProperties().size() == 1) {
            java.util.Map<java.lang.String, java.lang.Object> requestProperties = request.getProperties().iterator().next();
            if (org.apache.ambari.server.controller.internal.ServiceResourceProvider.isAddServiceRequest(requestProperties)) {
                org.apache.ambari.server.controller.RequestStatusResponse response = processAddServiceRequest(requestProperties, request.getRequestInfoProperties());
                notifyCreate(org.apache.ambari.server.controller.spi.Resource.Type.Service, request);
                return getRequestStatus(response);
            }
        }
        final java.util.Set<org.apache.ambari.server.controller.ServiceRequest> requests = new java.util.HashSet<>();
        for (java.util.Map<java.lang.String, java.lang.Object> propertyMap : request.getProperties()) {
            requests.add(getRequest(propertyMap));
        }
        createResources(new org.apache.ambari.server.controller.internal.AbstractResourceProvider.Command<java.lang.Void>() {
            @java.lang.Override
            public java.lang.Void invoke() throws org.apache.ambari.server.AmbariException, org.apache.ambari.server.security.authorization.AuthorizationException {
                createServices(requests);
                return null;
            }
        });
        notifyCreate(org.apache.ambari.server.controller.spi.Resource.Type.Service, request);
        return getRequestStatus(null);
    }

    @java.lang.Override
    protected java.util.Set<org.apache.ambari.server.controller.spi.Resource> getResourcesAuthorized(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        final java.util.Set<org.apache.ambari.server.controller.ServiceRequest> requests = new java.util.HashSet<>();
        for (java.util.Map<java.lang.String, java.lang.Object> propertyMap : getPropertyMaps(predicate)) {
            requests.add(getRequest(propertyMap));
        }
        java.util.Set<org.apache.ambari.server.controller.ServiceResponse> responses = getResources(new org.apache.ambari.server.controller.internal.AbstractResourceProvider.Command<java.util.Set<org.apache.ambari.server.controller.ServiceResponse>>() {
            @java.lang.Override
            public java.util.Set<org.apache.ambari.server.controller.ServiceResponse> invoke() throws org.apache.ambari.server.AmbariException {
                return getServices(requests);
            }
        });
        java.util.Set<java.lang.String> requestedIds = getRequestPropertyIds(request, predicate);
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = new java.util.HashSet<>();
        for (org.apache.ambari.server.controller.ServiceResponse response : responses) {
            org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Service);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.ServiceResourceProvider.SERVICE_CLUSTER_NAME_PROPERTY_ID, response.getClusterName(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.ServiceResourceProvider.SERVICE_SERVICE_NAME_PROPERTY_ID, response.getServiceName(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.ServiceResourceProvider.SERVICE_SERVICE_STATE_PROPERTY_ID, calculateServiceState(response.getClusterName(), response.getServiceName()), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.ServiceResourceProvider.SERVICE_MAINTENANCE_STATE_PROPERTY_ID, response.getMaintenanceState(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.ServiceResourceProvider.SERVICE_CREDENTIAL_STORE_SUPPORTED_PROPERTY_ID, java.lang.String.valueOf(response.isCredentialStoreSupported()), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.ServiceResourceProvider.SERVICE_CREDENTIAL_STORE_ENABLED_PROPERTY_ID, java.lang.String.valueOf(response.isCredentialStoreEnabled()), requestedIds);
            org.apache.ambari.server.orm.entities.RepositoryVersionEntity repoVersion = repositoryVersionDAO.findByPK(response.getDesiredRepositoryVersionId());
            if (null != repoVersion) {
                org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.ServiceResourceProvider.SERVICE_DESIRED_STACK_PROPERTY_ID, repoVersion.getStackId(), requestedIds);
            }
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.ServiceResourceProvider.SERVICE_DESIRED_REPO_VERSION_ID_PROPERTY_ID, response.getDesiredRepositoryVersionId(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.ServiceResourceProvider.SERVICE_REPOSITORY_STATE, response.getRepositoryVersionState(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.ServiceResourceProvider.SSO_INTEGRATION_SUPPORTED_PROPERTY_ID, response.isSsoIntegrationSupported(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.ServiceResourceProvider.SSO_INTEGRATION_ENABLED_PROPERTY_ID, response.isSsoIntegrationEnabled(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.ServiceResourceProvider.SSO_INTEGRATION_DESIRED_PROPERTY_ID, response.isSsoIntegrationDesired(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.ServiceResourceProvider.SSO_INTEGRATION_REQUIRES_KERBEROS_PROPERTY_ID, response.isSsoIntegrationRequiresKerberos(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.ServiceResourceProvider.KERBEROS_ENABLED_PROPERTY_ID, response.isKerberosEnabled(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.ServiceResourceProvider.LDAP_INTEGRATION_SUPPORTED_PROPERTY_ID, response.isLdapIntegrationSupported(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.ServiceResourceProvider.LDAP_INTEGRATION_ENABLED_PROPERTY_ID, response.isLdapIntegrationEnabled(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.ServiceResourceProvider.LDAP_INTEGRATION_DESIRED_PROPERTY_ID, response.isLdapIntegrationDesired(), requestedIds);
            java.util.Map<java.lang.String, java.lang.Object> serviceSpecificProperties = getServiceSpecificProperties(response.getClusterName(), response.getServiceName(), requestedIds);
            for (java.util.Map.Entry<java.lang.String, java.lang.Object> entry : serviceSpecificProperties.entrySet()) {
                org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, entry.getKey(), entry.getValue(), requestedIds);
            }
            resources.add(resource);
        }
        return resources;
    }

    @java.lang.Override
    protected org.apache.ambari.server.controller.spi.RequestStatus updateResourcesAuthorized(final org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        org.apache.ambari.server.controller.internal.RequestStageContainer requestStages = doUpdateResources(null, request, predicate);
        org.apache.ambari.server.controller.RequestStatusResponse response = null;
        if (requestStages != null) {
            try {
                requestStages.persist();
            } catch (org.apache.ambari.server.AmbariException e) {
                throw new org.apache.ambari.server.controller.spi.SystemException(e.getMessage(), e);
            }
            response = requestStages.getRequestStatusResponse();
        }
        notifyUpdate(org.apache.ambari.server.controller.spi.Resource.Type.Service, request, predicate);
        return getRequestStatus(response);
    }

    @java.lang.Override
    protected org.apache.ambari.server.controller.spi.RequestStatus deleteResourcesAuthorized(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        final java.util.Set<org.apache.ambari.server.controller.ServiceRequest> requests = new java.util.HashSet<>();
        for (java.util.Map<java.lang.String, java.lang.Object> propertyMap : getPropertyMaps(predicate)) {
            requests.add(getRequest(propertyMap));
        }
        org.apache.ambari.server.controller.RequestStatusResponse response = modifyResources(new org.apache.ambari.server.controller.internal.AbstractResourceProvider.Command<org.apache.ambari.server.controller.RequestStatusResponse>() {
            @java.lang.Override
            public org.apache.ambari.server.controller.RequestStatusResponse invoke() throws org.apache.ambari.server.AmbariException, org.apache.ambari.server.security.authorization.AuthorizationException {
                return deleteServices(requests);
            }
        });
        notifyDelete(org.apache.ambari.server.controller.spi.Resource.Type.Service, predicate);
        return getRequestStatus(response);
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
        unsupportedProperties.removeAll(org.apache.ambari.server.topology.addservice.AddServiceRequest.TOP_LEVEL_PROPERTIES);
        return unsupportedProperties;
    }

    @java.lang.Override
    protected java.util.Set<java.lang.String> getPKPropertyIds() {
        return org.apache.ambari.server.controller.internal.ServiceResourceProvider.pkPropertyIds;
    }

    private org.apache.ambari.server.controller.internal.RequestStageContainer doUpdateResources(final org.apache.ambari.server.controller.internal.RequestStageContainer stages, final org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        final java.util.Set<org.apache.ambari.server.controller.ServiceRequest> requests = new java.util.HashSet<>();
        org.apache.ambari.server.controller.internal.RequestStageContainer requestStages = null;
        java.util.Iterator<java.util.Map<java.lang.String, java.lang.Object>> iterator = request.getProperties().iterator();
        if (iterator.hasNext()) {
            for (java.util.Map<java.lang.String, java.lang.Object> propertyMap : getPropertyMaps(iterator.next(), predicate)) {
                requests.add(getRequest(propertyMap));
            }
            final boolean runSmokeTest = "true".equals(org.apache.ambari.server.controller.internal.AbstractResourceProvider.getQueryParameterValue(org.apache.ambari.server.controller.internal.ServiceResourceProvider.QUERY_PARAMETERS_RUN_SMOKE_TEST_ID, predicate));
            final boolean reconfigureClients = !"false".equals(org.apache.ambari.server.controller.internal.AbstractResourceProvider.getQueryParameterValue(org.apache.ambari.server.controller.internal.ServiceResourceProvider.QUERY_PARAMETERS_RECONFIGURE_CLIENT, predicate));
            final boolean startDependencies = "true".equals(org.apache.ambari.server.controller.internal.AbstractResourceProvider.getQueryParameterValue(org.apache.ambari.server.controller.internal.ServiceResourceProvider.QUERY_PARAMETERS_START_DEPENDENCIES, predicate));
            requestStages = modifyResources(new org.apache.ambari.server.controller.internal.AbstractResourceProvider.Command<org.apache.ambari.server.controller.internal.RequestStageContainer>() {
                @java.lang.Override
                public org.apache.ambari.server.controller.internal.RequestStageContainer invoke() throws org.apache.ambari.server.AmbariException, org.apache.ambari.server.security.authorization.AuthorizationException {
                    return updateServices(stages, requests, request.getRequestInfoProperties(), runSmokeTest, reconfigureClients, startDependencies);
                }
            });
        }
        return requestStages;
    }

    private org.apache.ambari.server.controller.ServiceRequest getRequest(java.util.Map<java.lang.String, java.lang.Object> properties) {
        java.lang.String desiredRepoId = ((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.ServiceResourceProvider.SERVICE_DESIRED_REPO_VERSION_ID_PROPERTY_ID)));
        org.apache.ambari.server.controller.ServiceRequest svcRequest = new org.apache.ambari.server.controller.ServiceRequest(((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.ServiceResourceProvider.SERVICE_CLUSTER_NAME_PROPERTY_ID))), ((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.ServiceResourceProvider.SERVICE_SERVICE_NAME_PROPERTY_ID))), null == desiredRepoId ? null : java.lang.Long.valueOf(desiredRepoId), ((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.ServiceResourceProvider.SERVICE_SERVICE_STATE_PROPERTY_ID))), ((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.ServiceResourceProvider.SERVICE_CREDENTIAL_STORE_ENABLED_PROPERTY_ID))));
        java.lang.Object o = properties.get(org.apache.ambari.server.controller.internal.ServiceResourceProvider.SERVICE_MAINTENANCE_STATE_PROPERTY_ID);
        if (null != o) {
            svcRequest.setMaintenanceState(o.toString());
        }
        o = properties.get(org.apache.ambari.server.controller.internal.ServiceResourceProvider.SERVICE_CREDENTIAL_STORE_SUPPORTED_PROPERTY_ID);
        if (null != o) {
            svcRequest.setCredentialStoreSupported(o.toString());
        }
        return svcRequest;
    }

    public void createServices(java.util.Set<org.apache.ambari.server.controller.ServiceRequest> requests) throws org.apache.ambari.server.AmbariException, org.apache.ambari.server.security.authorization.AuthorizationException {
        if (requests.isEmpty()) {
            org.apache.ambari.server.controller.internal.ServiceResourceProvider.LOG.warn("Received an empty requests set");
            return;
        }
        org.apache.ambari.server.state.Clusters clusters = getManagementController().getClusters();
        validateCreateRequests(requests, clusters);
        for (org.apache.ambari.server.controller.ServiceRequest request : requests) {
            org.apache.ambari.server.state.Cluster cluster = clusters.getCluster(request.getClusterName());
            org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion = request.getResolvedRepository();
            if (null == repositoryVersion) {
                throw new org.apache.ambari.server.AmbariException("Could not find any repository on the request.");
            }
            if ((repositoryVersion.getType() != org.apache.ambari.spi.RepositoryType.STANDARD) && (cluster.getProvisioningState() == org.apache.ambari.server.state.State.INIT)) {
                throw new org.apache.ambari.server.AmbariException(java.lang.String.format("Unable to add %s to %s because the cluster is still being provisioned and the repository for the service is not %s: %s", request.getServiceName(), cluster.getClusterName(), org.apache.ambari.spi.RepositoryType.STANDARD, repositoryVersion));
            }
            org.apache.ambari.server.state.Service s = cluster.addService(request.getServiceName(), repositoryVersion);
            org.apache.ambari.server.state.StackId stackId = repositoryVersion.getStackId();
            org.apache.ambari.server.api.services.AmbariMetaInfo ambariMetaInfo = getManagementController().getAmbariMetaInfo();
            org.apache.ambari.server.state.ServiceInfo serviceInfo = ambariMetaInfo.getService(stackId.getStackName(), stackId.getStackVersion(), request.getServiceName());
            boolean credentialStoreSupported = serviceInfo.isCredentialStoreSupported();
            boolean credentialStoreRequired = serviceInfo.isCredentialStoreRequired();
            org.apache.ambari.server.controller.internal.ServiceResourceProvider.LOG.info("Service: {}, credential_store_supported = {} and credential_store_required = {} from stack definition", request.getServiceName(), credentialStoreSupported, credentialStoreRequired);
            if (org.apache.commons.lang.StringUtils.isNotEmpty(request.getCredentialStoreEnabled())) {
                boolean credentialStoreEnabled = java.lang.Boolean.parseBoolean(request.getCredentialStoreEnabled());
                boolean enableCredStore = credentialStoreSupported && (credentialStoreRequired || credentialStoreEnabled);
                s.setCredentialStoreEnabled(enableCredStore);
                org.apache.ambari.server.controller.internal.ServiceResourceProvider.LOG.info("Service: {}, credential_store_enabled = {} from request and resulting" + " credential store enabled status is = {}", request.getServiceName(), credentialStoreEnabled, enableCredStore);
            } else {
                boolean enableCredStore = credentialStoreSupported && (credentialStoreRequired || serviceInfo.isCredentialStoreEnabled());
                s.setCredentialStoreEnabled(enableCredStore);
                org.apache.ambari.server.controller.internal.ServiceResourceProvider.LOG.info("Service: {}, credential_store_enabled = {} from stack definition and resulting" + " credential store enabled status is = {}", s.getName(), serviceInfo.isCredentialStoreEnabled(), enableCredStore);
            }
            getManagementController().initializeWidgetsAndLayouts(cluster, s);
        }
    }

    protected java.util.Set<org.apache.ambari.server.controller.ServiceResponse> getServices(java.util.Set<org.apache.ambari.server.controller.ServiceRequest> requests) throws org.apache.ambari.server.AmbariException {
        java.util.Set<org.apache.ambari.server.controller.ServiceResponse> response = new java.util.HashSet<>();
        for (org.apache.ambari.server.controller.ServiceRequest request : requests) {
            try {
                response.addAll(getServices(request));
            } catch (org.apache.ambari.server.ServiceNotFoundException e) {
                if (requests.size() == 1) {
                    throw e;
                }
            }
        }
        return response;
    }

    private java.util.Set<org.apache.ambari.server.controller.ServiceResponse> getServices(org.apache.ambari.server.controller.ServiceRequest request) throws org.apache.ambari.server.AmbariException {
        if ((request.getClusterName() == null) || request.getClusterName().isEmpty()) {
            throw new org.apache.ambari.server.AmbariException("Invalid arguments, cluster name" + " cannot be null");
        }
        org.apache.ambari.server.state.Clusters clusters = getManagementController().getClusters();
        java.lang.String clusterName = request.getClusterName();
        final org.apache.ambari.server.state.Cluster cluster;
        try {
            cluster = clusters.getCluster(clusterName);
        } catch (org.apache.ambari.server.ObjectNotFoundException e) {
            throw new org.apache.ambari.server.ParentObjectNotFoundException("Parent Cluster resource doesn't exist", e);
        }
        java.util.Set<org.apache.ambari.server.controller.ServiceResponse> response = new java.util.HashSet<>();
        if (request.getServiceName() != null) {
            org.apache.ambari.server.state.Service s = cluster.getService(request.getServiceName());
            response.add(s.convertToResponse());
            return response;
        }
        boolean checkDesiredState = false;
        org.apache.ambari.server.state.State desiredStateToCheck = null;
        if ((request.getDesiredState() != null) && (!request.getDesiredState().isEmpty())) {
            desiredStateToCheck = org.apache.ambari.server.state.State.valueOf(request.getDesiredState());
            if (!desiredStateToCheck.isValidDesiredState()) {
                throw new java.lang.IllegalArgumentException(("Invalid arguments, invalid desired" + " state, desiredState=") + desiredStateToCheck);
            }
            checkDesiredState = true;
        }
        for (org.apache.ambari.server.state.Service s : cluster.getServices().values()) {
            if (checkDesiredState && (desiredStateToCheck != s.getDesiredState())) {
                continue;
            }
            response.add(s.convertToResponse());
        }
        return response;
    }

    protected org.apache.ambari.server.controller.internal.RequestStageContainer updateServices(org.apache.ambari.server.controller.internal.RequestStageContainer requestStages, java.util.Set<org.apache.ambari.server.controller.ServiceRequest> requests, java.util.Map<java.lang.String, java.lang.String> requestProperties, boolean runSmokeTest, boolean reconfigureClients, boolean startDependencies) throws org.apache.ambari.server.AmbariException, org.apache.ambari.server.security.authorization.AuthorizationException {
        org.apache.ambari.server.controller.AmbariManagementController controller = getManagementController();
        if (requests.isEmpty()) {
            org.apache.ambari.server.controller.internal.ServiceResourceProvider.LOG.warn("Received an empty requests set");
            return null;
        }
        java.util.Map<org.apache.ambari.server.state.State, java.util.List<org.apache.ambari.server.state.Service>> changedServices = new java.util.EnumMap<>(org.apache.ambari.server.state.State.class);
        java.util.Map<org.apache.ambari.server.state.State, java.util.List<org.apache.ambari.server.state.ServiceComponent>> changedComps = new java.util.EnumMap<>(org.apache.ambari.server.state.State.class);
        java.util.Map<java.lang.String, java.util.Map<org.apache.ambari.server.state.State, java.util.List<org.apache.ambari.server.state.ServiceComponentHost>>> changedScHosts = new java.util.HashMap<>();
        java.util.Collection<org.apache.ambari.server.state.ServiceComponentHost> ignoredScHosts = new java.util.ArrayList<>();
        java.util.Set<java.lang.String> clusterNames = new java.util.HashSet<>();
        java.util.Map<java.lang.String, java.util.Set<java.lang.String>> serviceNames = new java.util.HashMap<>();
        java.util.Set<org.apache.ambari.server.state.State> seenNewStates = new java.util.HashSet<>();
        java.util.Map<org.apache.ambari.server.state.Service, java.lang.Boolean> serviceCredentialStoreEnabledMap = new java.util.HashMap<>();
        org.apache.ambari.server.controller.spi.Resource.Type reqOpLvl;
        if (requestProperties.containsKey(org.apache.ambari.server.controller.internal.RequestOperationLevel.OPERATION_LEVEL_ID)) {
            org.apache.ambari.server.controller.internal.RequestOperationLevel operationLevel = new org.apache.ambari.server.controller.internal.RequestOperationLevel(requestProperties);
            reqOpLvl = operationLevel.getLevel();
        } else {
            java.lang.String message = "Can not determine request operation level. " + ("Operation level property should " + "be specified for this request.");
            org.apache.ambari.server.controller.internal.ServiceResourceProvider.LOG.warn(message);
            reqOpLvl = org.apache.ambari.server.controller.spi.Resource.Type.Cluster;
        }
        org.apache.ambari.server.state.Clusters clusters = controller.getClusters();
        for (org.apache.ambari.server.controller.ServiceRequest request : requests) {
            if ((((request.getClusterName() == null) || request.getClusterName().isEmpty()) || (request.getServiceName() == null)) || request.getServiceName().isEmpty()) {
                throw new java.lang.IllegalArgumentException("Invalid arguments, cluster name" + " and service name should be provided to update services");
            }
            org.apache.ambari.server.controller.internal.ServiceResourceProvider.LOG.info(((((("Received a updateService request" + ", clusterName=") + request.getClusterName()) + ", serviceName=") + request.getServiceName()) + ", request=") + request);
            clusterNames.add(request.getClusterName());
            if (clusterNames.size() > 1) {
                throw new java.lang.IllegalArgumentException("Updates to multiple clusters is not" + " supported");
            }
            if (!serviceNames.containsKey(request.getClusterName())) {
                serviceNames.put(request.getClusterName(), new java.util.HashSet<>());
            }
            if (serviceNames.get(request.getClusterName()).contains(request.getServiceName())) {
                throw new java.lang.IllegalArgumentException("Invalid request contains duplicate" + " service names");
            }
            serviceNames.get(request.getClusterName()).add(request.getServiceName());
            org.apache.ambari.server.state.Cluster cluster = clusters.getCluster(request.getClusterName());
            org.apache.ambari.server.state.Service s = cluster.getService(request.getServiceName());
            org.apache.ambari.server.state.State oldState = s.getDesiredState();
            org.apache.ambari.server.state.State newState = null;
            if (request.getDesiredState() != null) {
                newState = org.apache.ambari.server.state.State.valueOf(request.getDesiredState());
                if (!newState.isValidDesiredState()) {
                    throw new java.lang.IllegalArgumentException(("Invalid arguments, invalid" + " desired state, desiredState=") + newState);
                }
            }
            if (null != request.getMaintenanceState()) {
                if (!org.apache.ambari.server.security.authorization.AuthorizationHelper.isAuthorized(org.apache.ambari.server.security.authorization.ResourceType.CLUSTER, cluster.getResourceId(), org.apache.ambari.server.security.authorization.RoleAuthorization.SERVICE_TOGGLE_MAINTENANCE)) {
                    throw new org.apache.ambari.server.security.authorization.AuthorizationException("The authenticated user is not authorized to toggle the maintainence state of services");
                }
                org.apache.ambari.server.state.MaintenanceState newMaint = org.apache.ambari.server.state.MaintenanceState.valueOf(request.getMaintenanceState());
                if (newMaint != s.getMaintenanceState()) {
                    if (newMaint.equals(org.apache.ambari.server.state.MaintenanceState.IMPLIED_FROM_HOST) || newMaint.equals(org.apache.ambari.server.state.MaintenanceState.IMPLIED_FROM_SERVICE)) {
                        throw new java.lang.IllegalArgumentException(("Invalid arguments, can only set " + "maintenance state to one of ") + java.util.EnumSet.of(org.apache.ambari.server.state.MaintenanceState.OFF, org.apache.ambari.server.state.MaintenanceState.ON));
                    } else {
                        s.setMaintenanceState(newMaint);
                    }
                }
            }
            if (org.apache.commons.lang.StringUtils.isNotEmpty(request.getCredentialStoreEnabled())) {
                boolean credentialStoreEnabled = java.lang.Boolean.parseBoolean(request.getCredentialStoreEnabled());
                if ((!s.isCredentialStoreSupported()) && credentialStoreEnabled) {
                    throw new java.lang.IllegalArgumentException(("Invalid arguments, cannot enable credential store " + "as it is not supported by the service. Service=") + s.getName());
                }
                if (s.isCredentialStoreRequired() && (!credentialStoreEnabled)) {
                    throw new java.lang.IllegalArgumentException(("Invalid arguments, cannot disable credential store " + "as it is required by the service. Service=") + s.getName());
                }
                serviceCredentialStoreEnabledMap.put(s, credentialStoreEnabled);
                org.apache.ambari.server.controller.internal.ServiceResourceProvider.LOG.info("Service: {}, credential_store_enabled from request: {}", request.getServiceName(), credentialStoreEnabled);
            }
            if (org.apache.commons.lang.StringUtils.isNotEmpty(request.getCredentialStoreSupported())) {
                throw new java.lang.IllegalArgumentException(("Invalid arguments, cannot update credential_store_supported " + "as it is set only via service definition. Service=") + s.getName());
            }
            if (newState == null) {
                if (org.apache.ambari.server.controller.internal.ServiceResourceProvider.LOG.isDebugEnabled()) {
                    org.apache.ambari.server.controller.internal.ServiceResourceProvider.LOG.debug("Nothing to do for new updateService request, clusterName={}, serviceName={}, newDesiredState=null", request.getClusterName(), request.getServiceName());
                }
                continue;
            }
            if (!maintenanceStateHelper.isOperationAllowed(reqOpLvl, s)) {
                org.apache.ambari.server.controller.internal.ServiceResourceProvider.LOG.info((("Operations cannot be applied to service " + s.getName()) + " in the maintenance state of ") + s.getMaintenanceState());
                continue;
            }
            seenNewStates.add(newState);
            if (newState != oldState) {
                if (((newState == org.apache.ambari.server.state.State.INSTALLED) || (newState == org.apache.ambari.server.state.State.STARTED)) && (!org.apache.ambari.server.security.authorization.AuthorizationHelper.isAuthorized(org.apache.ambari.server.security.authorization.ResourceType.CLUSTER, cluster.getResourceId(), org.apache.ambari.server.security.authorization.RoleAuthorization.SERVICE_START_STOP))) {
                    throw new org.apache.ambari.server.security.authorization.AuthorizationException("The authenticated user is not authorized to start or stop services");
                }
                if (!org.apache.ambari.server.state.State.isValidDesiredStateTransition(oldState, newState)) {
                    throw new org.apache.ambari.server.AmbariException(((((((((("Invalid transition for" + (" service" + ", clusterName=")) + cluster.getClusterName()) + ", clusterId=") + cluster.getClusterId()) + ", serviceName=") + s.getName()) + ", currentDesiredState=") + oldState) + ", newDesiredState=") + newState);
                }
                if (!changedServices.containsKey(newState)) {
                    changedServices.put(newState, new java.util.ArrayList<>());
                }
                changedServices.get(newState).add(s);
            }
            updateServiceComponents(requestStages, changedComps, changedScHosts, ignoredScHosts, reqOpLvl, s, newState);
        }
        if (startDependencies && changedServices.containsKey(org.apache.ambari.server.state.State.STARTED)) {
            java.util.HashSet<org.apache.ambari.server.state.Service> depServices = new java.util.HashSet<>();
            for (org.apache.ambari.server.state.Service service : changedServices.get(org.apache.ambari.server.state.State.STARTED)) {
                org.apache.ambari.server.metadata.RoleCommandOrder rco = controller.getRoleCommandOrder(service.getCluster());
                java.util.Set<org.apache.ambari.server.state.Service> dependencies = rco.getTransitiveServices(service, org.apache.ambari.server.RoleCommand.START);
                for (org.apache.ambari.server.state.Service dependency : dependencies) {
                    if (!changedServices.get(org.apache.ambari.server.state.State.STARTED).contains(dependency)) {
                        depServices.add(dependency);
                    }
                }
            }
            for (org.apache.ambari.server.state.Service service : depServices) {
                updateServiceComponents(requestStages, changedComps, changedScHosts, ignoredScHosts, reqOpLvl, service, org.apache.ambari.server.state.State.STARTED);
                changedServices.get(org.apache.ambari.server.state.State.STARTED).add(service);
            }
        }
        if (seenNewStates.size() > 1) {
            throw new java.lang.IllegalArgumentException("Cannot handle different desired state" + " changes for a set of services at the same time");
        }
        for (java.util.Map.Entry<org.apache.ambari.server.state.Service, java.lang.Boolean> serviceCredential : serviceCredentialStoreEnabledMap.entrySet()) {
            org.apache.ambari.server.state.Service service = serviceCredential.getKey();
            java.lang.Boolean credentialStoreEnabled = serviceCredential.getValue();
            service.setCredentialStoreEnabled(credentialStoreEnabled);
        }
        org.apache.ambari.server.state.Cluster cluster = clusters.getCluster(clusterNames.iterator().next());
        return controller.addStages(requestStages, cluster, requestProperties, null, changedServices, changedComps, changedScHosts, ignoredScHosts, runSmokeTest, reconfigureClients, false);
    }

    private void updateServiceComponents(org.apache.ambari.server.controller.internal.RequestStageContainer requestStages, java.util.Map<org.apache.ambari.server.state.State, java.util.List<org.apache.ambari.server.state.ServiceComponent>> changedComps, java.util.Map<java.lang.String, java.util.Map<org.apache.ambari.server.state.State, java.util.List<org.apache.ambari.server.state.ServiceComponentHost>>> changedScHosts, java.util.Collection<org.apache.ambari.server.state.ServiceComponentHost> ignoredScHosts, org.apache.ambari.server.controller.spi.Resource.Type reqOpLvl, org.apache.ambari.server.state.Service service, org.apache.ambari.server.state.State newState) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.Cluster cluster = service.getCluster();
        org.apache.ambari.server.controller.AmbariManagementController controller = getManagementController();
        org.apache.ambari.server.api.services.AmbariMetaInfo ambariMetaInfo = controller.getAmbariMetaInfo();
        for (org.apache.ambari.server.state.ServiceComponent sc : service.getServiceComponents().values()) {
            org.apache.ambari.server.state.State oldScState = sc.getDesiredState();
            if (newState != oldScState) {
                if (sc.isClientComponent() && (!newState.isValidClientComponentState())) {
                    continue;
                }
                if (!org.apache.ambari.server.state.State.isValidDesiredStateTransition(oldScState, newState)) {
                    throw new org.apache.ambari.server.AmbariException(((((((((((("Invalid transition for" + (" servicecomponent" + ", clusterName=")) + cluster.getClusterName()) + ", clusterId=") + cluster.getClusterId()) + ", serviceName=") + sc.getServiceName()) + ", componentName=") + sc.getName()) + ", currentDesiredState=") + oldScState) + ", newDesiredState=") + newState);
                }
                if (!changedComps.containsKey(newState)) {
                    changedComps.put(newState, new java.util.ArrayList<>());
                }
                changedComps.get(newState).add(sc);
            }
            if (org.apache.ambari.server.controller.internal.ServiceResourceProvider.LOG.isDebugEnabled()) {
                org.apache.ambari.server.controller.internal.ServiceResourceProvider.LOG.debug("Handling update to ServiceComponent, clusterName={}, serviceName={}, componentName={}, currentDesiredState={}, newDesiredState={}", cluster.getClusterName(), service.getName(), sc.getName(), oldScState, newState);
            }
            for (org.apache.ambari.server.state.ServiceComponentHost sch : sc.getServiceComponentHosts().values()) {
                org.apache.ambari.server.state.State oldSchState = sch.getState();
                if ((oldSchState == org.apache.ambari.server.state.State.DISABLED) || (oldSchState == org.apache.ambari.server.state.State.UNKNOWN)) {
                    if (org.apache.ambari.server.controller.internal.ServiceResourceProvider.LOG.isDebugEnabled()) {
                        org.apache.ambari.server.controller.internal.ServiceResourceProvider.LOG.debug("Ignoring ServiceComponentHost, clusterName={}, serviceName={}, componentName={}, hostname={}, currentState={}, newDesiredState={}", cluster.getClusterName(), service.getName(), sc.getName(), sch.getHostName(), oldSchState, newState);
                    }
                    continue;
                }
                if (newState == oldSchState) {
                    ignoredScHosts.add(sch);
                    if (org.apache.ambari.server.controller.internal.ServiceResourceProvider.LOG.isDebugEnabled()) {
                        org.apache.ambari.server.controller.internal.ServiceResourceProvider.LOG.debug("Ignoring ServiceComponentHost, clusterName={}, serviceName={}, componentName={}, hostname={}, currentState={}, newDesiredState={}", cluster.getClusterName(), service.getName(), sc.getName(), sch.getHostName(), oldSchState, newState);
                    }
                    continue;
                }
                if (!maintenanceStateHelper.isOperationAllowed(reqOpLvl, sch)) {
                    ignoredScHosts.add(sch);
                    if (org.apache.ambari.server.controller.internal.ServiceResourceProvider.LOG.isDebugEnabled()) {
                        org.apache.ambari.server.controller.internal.ServiceResourceProvider.LOG.debug("Ignoring ServiceComponentHost, clusterName={}, serviceName={}, componentName={}, hostname={}", cluster.getClusterName(), service.getName(), sc.getName(), sch.getHostName());
                    }
                    continue;
                }
                if (sc.isClientComponent() && (!newState.isValidClientComponentState())) {
                    continue;
                }
                if (!isValidStateTransition(requestStages, oldSchState, newState, sch)) {
                    java.lang.String error = ((((((((((((("Invalid transition for" + (" servicecomponenthost" + ", clusterName=")) + cluster.getClusterName()) + ", clusterId=") + cluster.getClusterId()) + ", serviceName=") + sch.getServiceName()) + ", componentName=") + sch.getServiceComponentName()) + ", hostname=") + sch.getHostName()) + ", currentState=") + oldSchState) + ", newDesiredState=") + newState;
                    org.apache.ambari.server.state.StackId sid = service.getDesiredStackId();
                    if (ambariMetaInfo.getComponent(sid.getStackName(), sid.getStackVersion(), sc.getServiceName(), sch.getServiceComponentName()).isMaster()) {
                        throw new java.lang.IllegalArgumentException(error);
                    } else {
                        org.apache.ambari.server.controller.internal.ServiceResourceProvider.LOG.warn("Ignoring: " + error);
                        continue;
                    }
                }
                if (!changedScHosts.containsKey(sc.getName())) {
                    changedScHosts.put(sc.getName(), new java.util.EnumMap<>(org.apache.ambari.server.state.State.class));
                }
                if (!changedScHosts.get(sc.getName()).containsKey(newState)) {
                    changedScHosts.get(sc.getName()).put(newState, new java.util.ArrayList<>());
                }
                if (org.apache.ambari.server.controller.internal.ServiceResourceProvider.LOG.isDebugEnabled()) {
                    org.apache.ambari.server.controller.internal.ServiceResourceProvider.LOG.debug("Handling update to ServiceComponentHost, clusterName={}, serviceName={}, componentName={}, hostname={}, currentState={}, newDesiredState={}", cluster.getClusterName(), service.getName(), sc.getName(), sch.getHostName(), oldSchState, newState);
                }
                changedScHosts.get(sc.getName()).get(newState).add(sch);
            }
        }
    }

    protected org.apache.ambari.server.controller.RequestStatusResponse deleteServices(java.util.Set<org.apache.ambari.server.controller.ServiceRequest> request) throws org.apache.ambari.server.AmbariException, org.apache.ambari.server.security.authorization.AuthorizationException {
        org.apache.ambari.server.state.Clusters clusters = getManagementController().getClusters();
        java.util.Set<org.apache.ambari.server.state.Service> removable = new java.util.HashSet<>();
        for (org.apache.ambari.server.controller.ServiceRequest serviceRequest : request) {
            if (org.apache.commons.lang.StringUtils.isEmpty(serviceRequest.getClusterName()) || org.apache.commons.lang.StringUtils.isEmpty(serviceRequest.getServiceName())) {
                throw new org.apache.ambari.server.AmbariException("invalid arguments");
            } else {
                if (!org.apache.ambari.server.security.authorization.AuthorizationHelper.isAuthorized(org.apache.ambari.server.security.authorization.ResourceType.CLUSTER, getClusterResourceId(serviceRequest.getClusterName()), org.apache.ambari.server.security.authorization.RoleAuthorization.SERVICE_ADD_DELETE_SERVICES)) {
                    throw new org.apache.ambari.server.security.authorization.AuthorizationException("The user is not authorized to delete services");
                }
                org.apache.ambari.server.state.Service service = clusters.getCluster(serviceRequest.getClusterName()).getService(serviceRequest.getServiceName());
                java.util.List<org.apache.ambari.server.state.ServiceComponentHost> nonRemovableComponents = service.getServiceComponents().values().stream().flatMap(sch -> sch.getServiceComponentHosts().values().stream()).filter(sch -> !sch.canBeRemoved()).collect(java.util.stream.Collectors.toList());
                if (!nonRemovableComponents.isEmpty()) {
                    for (org.apache.ambari.server.state.ServiceComponentHost sch : nonRemovableComponents) {
                        java.lang.String msg = java.lang.String.format("Cannot remove %s/%s. %s on %s is in %s state.", serviceRequest.getClusterName(), serviceRequest.getServiceName(), sch.getServiceComponentName(), sch.getHost(), java.lang.String.valueOf(sch.getState()));
                        org.apache.ambari.server.controller.internal.ServiceResourceProvider.LOG.error(msg);
                    }
                    throw new org.apache.ambari.server.AmbariException((((("Cannot remove " + serviceRequest.getClusterName()) + "/") + serviceRequest.getServiceName()) + ". ") + "One or more host components are in a non-removable state.");
                }
                removable.add(service);
            }
        }
        org.apache.ambari.server.controller.internal.DeleteHostComponentStatusMetaData deleteMetaData = new org.apache.ambari.server.controller.internal.DeleteHostComponentStatusMetaData();
        for (org.apache.ambari.server.state.Service service : removable) {
            service.getCluster().deleteService(service.getName(), deleteMetaData);
            STOMPComponentsDeleteHandler.processDeleteByMetaDataException(deleteMetaData);
        }
        STOMPComponentsDeleteHandler.processDeleteByMetaData(deleteMetaData);
        return null;
    }

    private java.lang.String calculateServiceState(java.lang.String clusterName, java.lang.String serviceName) {
        org.apache.ambari.server.controller.utilities.state.ServiceCalculatedState serviceCalculatedState = org.apache.ambari.server.controller.utilities.ServiceCalculatedStateFactory.getServiceStateProvider(serviceName);
        return serviceCalculatedState.getState(clusterName, serviceName).toString();
    }

    private boolean isValidStateTransition(org.apache.ambari.server.controller.internal.RequestStageContainer stages, org.apache.ambari.server.state.State startState, org.apache.ambari.server.state.State desiredState, org.apache.ambari.server.state.ServiceComponentHost host) {
        if (stages != null) {
            org.apache.ambari.server.state.State projectedState = stages.getProjectedState(host.getHostName(), host.getServiceComponentName());
            startState = (projectedState == null) ? startState : projectedState;
        }
        return org.apache.ambari.server.state.State.isValidStateTransition(startState, desiredState);
    }

    private java.util.Map<java.lang.String, java.lang.Object> getServiceSpecificProperties(java.lang.String clusterName, java.lang.String serviceName, java.util.Set<java.lang.String> requestedIds) {
        java.util.Map<java.lang.String, java.lang.Object> serviceSpecificProperties = new java.util.HashMap<>();
        if (serviceName.equals("KERBEROS")) {
            if ((requestedIds.contains(org.apache.ambari.server.controller.internal.ServiceResourceProvider.SERVICE_ATTRIBUTES_PROPERTY_ID) || org.apache.ambari.server.controller.internal.BaseProvider.isPropertyCategoryRequested(org.apache.ambari.server.controller.internal.ServiceResourceProvider.SERVICE_ATTRIBUTES_PROPERTY_ID, requestedIds)) || org.apache.ambari.server.controller.internal.BaseProvider.isPropertyEntryRequested(org.apache.ambari.server.controller.internal.ServiceResourceProvider.SERVICE_ATTRIBUTES_PROPERTY_ID, requestedIds)) {
                java.util.Map<java.lang.String, java.lang.String> kerberosAttributes = new java.util.HashMap<>();
                java.lang.String kdcValidationResult = "OK";
                java.lang.String failureDetails = "";
                try {
                    kerberosHelper.validateKDCCredentials(getManagementController().getClusters().getCluster(clusterName));
                } catch (org.apache.ambari.server.serveraction.kerberos.KerberosInvalidConfigurationException e) {
                    kdcValidationResult = "INVALID_CONFIGURATION";
                    failureDetails = e.getMessage();
                } catch (org.apache.ambari.server.serveraction.kerberos.KerberosAdminAuthenticationException e) {
                    kdcValidationResult = "INVALID_CREDENTIALS";
                    failureDetails = e.getMessage();
                } catch (org.apache.ambari.server.serveraction.kerberos.KerberosMissingAdminCredentialsException e) {
                    kdcValidationResult = "MISSING_CREDENTIALS";
                    failureDetails = e.getMessage();
                } catch (org.apache.ambari.server.AmbariException e) {
                    kdcValidationResult = "VALIDATION_ERROR";
                    failureDetails = e.getMessage();
                }
                kerberosAttributes.put("kdc_validation_result", kdcValidationResult);
                kerberosAttributes.put("kdc_validation_failure_details", failureDetails);
                serviceSpecificProperties.put(org.apache.ambari.server.controller.internal.ServiceResourceProvider.SERVICE_ATTRIBUTES_PROPERTY_ID, kerberosAttributes);
            }
        }
        return serviceSpecificProperties;
    }

    private void validateCreateRequests(java.util.Set<org.apache.ambari.server.controller.ServiceRequest> requests, org.apache.ambari.server.state.Clusters clusters) throws org.apache.ambari.server.security.authorization.AuthorizationException, org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.api.services.AmbariMetaInfo ambariMetaInfo = getManagementController().getAmbariMetaInfo();
        java.util.Map<java.lang.String, java.util.Set<java.lang.String>> serviceNames = new java.util.HashMap<>();
        java.util.Set<java.lang.String> duplicates = new java.util.HashSet<>();
        for (org.apache.ambari.server.controller.ServiceRequest request : requests) {
            final java.lang.String clusterName = request.getClusterName();
            final java.lang.String serviceName = request.getServiceName();
            org.apache.commons.lang.Validate.notEmpty(clusterName, "Cluster name should be provided when creating a service");
            org.apache.commons.lang.Validate.notEmpty(serviceName, "Service name should be provided when creating a service");
            if (org.apache.ambari.server.controller.internal.ServiceResourceProvider.LOG.isDebugEnabled()) {
                org.apache.ambari.server.controller.internal.ServiceResourceProvider.LOG.debug("Received a createService request, clusterName={}, serviceName={}, request={}", clusterName, serviceName, request);
            }
            if (!org.apache.ambari.server.security.authorization.AuthorizationHelper.isAuthorized(org.apache.ambari.server.security.authorization.ResourceType.CLUSTER, getClusterResourceId(clusterName), org.apache.ambari.server.security.authorization.RoleAuthorization.SERVICE_ADD_DELETE_SERVICES)) {
                throw new org.apache.ambari.server.security.authorization.AuthorizationException("The user is not authorized to create services");
            }
            if (!serviceNames.containsKey(clusterName)) {
                serviceNames.put(clusterName, new java.util.HashSet<>());
            }
            if (serviceNames.get(clusterName).contains(serviceName)) {
                duplicates.add(serviceName);
                continue;
            }
            serviceNames.get(clusterName).add(serviceName);
            if (org.apache.commons.lang.StringUtils.isNotEmpty(request.getDesiredState())) {
                org.apache.ambari.server.state.State state = org.apache.ambari.server.state.State.valueOf(request.getDesiredState());
                if ((!state.isValidDesiredState()) || (state != org.apache.ambari.server.state.State.INIT)) {
                    throw new java.lang.IllegalArgumentException(("Invalid desired state" + (" only INIT state allowed during creation" + ", providedDesiredState=")) + request.getDesiredState());
                }
            }
            org.apache.ambari.server.state.Cluster cluster;
            try {
                cluster = clusters.getCluster(clusterName);
            } catch (org.apache.ambari.server.ClusterNotFoundException e) {
                throw new org.apache.ambari.server.ParentObjectNotFoundException("Attempted to add a service to a cluster which doesn't exist", e);
            }
            try {
                org.apache.ambari.server.state.Service s = cluster.getService(serviceName);
                if (s != null) {
                    duplicates.add(serviceName);
                    continue;
                }
            } catch (org.apache.ambari.server.ServiceNotFoundException e) {
            }
            java.lang.Long desiredRepositoryVersion = request.getDesiredRepositoryVersionId();
            if (null == desiredRepositoryVersion) {
                java.util.Set<java.lang.Long> repoIds = new java.util.HashSet<>();
                for (org.apache.ambari.server.state.Service service : cluster.getServices().values()) {
                    org.apache.ambari.server.orm.entities.RepositoryVersionEntity serviceRepo = service.getDesiredRepositoryVersion();
                    if (null != serviceRepo.getParentId()) {
                        repoIds.add(serviceRepo.getParentId());
                    } else {
                        repoIds.add(serviceRepo.getId());
                    }
                }
                org.apache.ambari.server.controller.internal.ServiceResourceProvider.LOG.info("{} was not specified; the following repository ids were found: {}", org.apache.ambari.server.controller.internal.ServiceResourceProvider.SERVICE_DESIRED_REPO_VERSION_ID_PROPERTY_ID, org.apache.commons.lang.StringUtils.join(repoIds, ','));
                if (org.apache.commons.collections.CollectionUtils.isEmpty(repoIds)) {
                    throw new java.lang.IllegalArgumentException("No repositories were found for service installation");
                } else if (repoIds.size() > 1) {
                    throw new java.lang.IllegalArgumentException(java.lang.String.format("%s was not specified, and the cluster " + "contains more than one standard-type repository", org.apache.ambari.server.controller.internal.ServiceResourceProvider.SERVICE_DESIRED_REPO_VERSION_ID_PROPERTY_ID));
                } else {
                    desiredRepositoryVersion = repoIds.iterator().next();
                }
            }
            if (null == desiredRepositoryVersion) {
                throw new java.lang.IllegalArgumentException(java.lang.String.format("%s is required when adding a service.", org.apache.ambari.server.controller.internal.ServiceResourceProvider.SERVICE_DESIRED_REPO_VERSION_ID_PROPERTY_ID));
            }
            org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion = repositoryVersionDAO.findByPK(desiredRepositoryVersion);
            if (null == repositoryVersion) {
                throw new java.lang.IllegalArgumentException(java.lang.String.format("Could not find any repositories defined by %d", desiredRepositoryVersion));
            }
            org.apache.ambari.server.state.StackId stackId = repositoryVersion.getStackId();
            request.setResolvedRepository(repositoryVersion);
            if (!ambariMetaInfo.isValidService(stackId.getStackName(), stackId.getStackVersion(), request.getServiceName())) {
                throw new java.lang.IllegalArgumentException((((("Unsupported or invalid service in stack, clusterName=" + clusterName) + ", serviceName=") + serviceName) + ", stackInfo=") + stackId.getStackId());
            }
            org.apache.ambari.server.state.ServiceInfo serviceInfo = ambariMetaInfo.getService(stackId.getStackName(), stackId.getStackVersion(), request.getServiceName());
            if (org.apache.commons.lang.StringUtils.isNotEmpty(request.getCredentialStoreEnabled())) {
                boolean credentialStoreEnabled = java.lang.Boolean.parseBoolean(request.getCredentialStoreEnabled());
                if ((!serviceInfo.isCredentialStoreSupported()) && credentialStoreEnabled) {
                    throw new java.lang.IllegalArgumentException(("Invalid arguments, cannot enable credential store " + "as it is not supported by the service. Service=") + request.getServiceName());
                }
            }
        }
        if (serviceNames.size() != 1) {
            throw new java.lang.IllegalArgumentException("Invalid arguments, updates allowed" + "on only one cluster at a time");
        }
        if (!duplicates.isEmpty()) {
            java.lang.String clusterName = requests.iterator().next().getClusterName();
            java.lang.String msg = ((("Attempted to create a service which already exists: " + ", clusterName=") + clusterName) + " serviceName=") + org.apache.commons.lang.StringUtils.join(duplicates, ",");
            throw new org.apache.ambari.server.DuplicateResourceException(msg);
        }
    }

    private static boolean isAddServiceRequest(java.util.Map<java.lang.String, java.lang.Object> properties) {
        return org.apache.ambari.server.topology.addservice.AddServiceRequest.OperationType.ADD_SERVICE.name().equals(properties.get(org.apache.ambari.server.controller.internal.ServiceResourceProvider.OPERATION_TYPE));
    }

    private org.apache.ambari.server.controller.RequestStatusResponse processAddServiceRequest(java.util.Map<java.lang.String, java.lang.Object> requestProperties, java.util.Map<java.lang.String, java.lang.String> requestInfoProperties) throws org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        org.apache.ambari.server.topology.addservice.AddServiceRequest request = org.apache.ambari.server.controller.internal.ServiceResourceProvider.createAddServiceRequest(requestInfoProperties);
        java.lang.String clusterName = java.lang.String.valueOf(requestProperties.get(org.apache.ambari.server.controller.internal.ServiceResourceProvider.SERVICE_CLUSTER_NAME_PROPERTY_ID));
        try {
            return addServiceOrchestrator.processAddServiceRequest(getManagementController().getClusters().getCluster(clusterName), request);
        } catch (org.apache.ambari.server.AmbariException e) {
            throw new org.apache.ambari.server.controller.spi.NoSuchParentResourceException(e.getMessage(), e);
        }
    }

    private static org.apache.ambari.server.topology.addservice.AddServiceRequest createAddServiceRequest(java.util.Map<java.lang.String, java.lang.String> requestInfoProperties) {
        try {
            return org.apache.ambari.server.topology.addservice.AddServiceRequest.of(requestInfoProperties.get(org.apache.ambari.server.controller.spi.Request.REQUEST_INFO_BODY_PROPERTY));
        } catch (java.io.UncheckedIOException e) {
            return org.apache.ambari.server.controller.internal.ServiceResourceProvider.CHECK.wrapInUnchecked(e, java.lang.IllegalArgumentException::new, "Could not parse input as valid Add Service request: " + e.getCause().getMessage());
        }
    }
}