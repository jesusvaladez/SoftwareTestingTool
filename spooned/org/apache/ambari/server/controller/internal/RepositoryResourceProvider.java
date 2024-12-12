package org.apache.ambari.server.controller.internal;
import org.apache.commons.lang.BooleanUtils;
public class RepositoryResourceProvider extends org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider {
    public static final java.lang.String REPOSITORY_REPO_NAME_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Repositories", "repo_name");

    public static final java.lang.String REPOSITORY_STACK_NAME_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Repositories", "stack_name");

    public static final java.lang.String REPOSITORY_STACK_VERSION_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Repositories", "stack_version");

    public static final java.lang.String REPOSITORY_CLUSTER_STACK_VERSION_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Repositories", "cluster_version_id");

    public static final java.lang.String REPOSITORY_OS_TYPE_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Repositories", "os_type");

    public static final java.lang.String REPOSITORY_BASE_URL_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Repositories", "base_url");

    public static final java.lang.String REPOSITORY_DISTRIBUTION_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Repositories", "distribution");

    public static final java.lang.String REPOSITORY_COMPONENTS_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Repositories", "components");

    public static final java.lang.String REPOSITORY_REPO_ID_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Repositories", "repo_id");

    public static final java.lang.String REPOSITORY_MIRRORS_LIST_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Repositories", "mirrors_list");

    public static final java.lang.String REPOSITORY_DEFAULT_BASE_URL_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Repositories", "default_base_url");

    public static final java.lang.String REPOSITORY_VERIFY_BASE_URL_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Repositories", "verify_base_url");

    public static final java.lang.String REPOSITORY_REPOSITORY_VERSION_ID_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Repositories", "repository_version_id");

    public static final java.lang.String REPOSITORY_VERSION_DEFINITION_ID_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Repositories", "version_definition_id");

    public static final java.lang.String REPOSITORY_UNIQUE_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Repositories", "unique");

    public static final java.lang.String REPOSITORY_TAGS_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Repositories", "tags");

    @org.apache.ambari.annotations.Experimental(feature = org.apache.ambari.annotations.ExperimentalFeature.CUSTOM_SERVICE_REPOS, comment = "Remove logic for handling custom service repos after enabling multi-mpack cluster deployment")
    public static final java.lang.String REPOSITORY_APPLICABLE_SERVICES_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Repositories", "applicable_services");

    @java.lang.SuppressWarnings("serial")
    private static final java.util.Set<java.lang.String> pkPropertyIds = com.google.common.collect.ImmutableSet.<java.lang.String>builder().add(org.apache.ambari.server.controller.internal.RepositoryResourceProvider.REPOSITORY_STACK_NAME_PROPERTY_ID).add(org.apache.ambari.server.controller.internal.RepositoryResourceProvider.REPOSITORY_STACK_VERSION_PROPERTY_ID).add(org.apache.ambari.server.controller.internal.RepositoryResourceProvider.REPOSITORY_OS_TYPE_PROPERTY_ID).add(org.apache.ambari.server.controller.internal.RepositoryResourceProvider.REPOSITORY_REPO_ID_PROPERTY_ID).build();

    @java.lang.SuppressWarnings("serial")
    public static final java.util.Set<java.lang.String> propertyIds = com.google.common.collect.ImmutableSet.<java.lang.String>builder().add(org.apache.ambari.server.controller.internal.RepositoryResourceProvider.REPOSITORY_REPO_NAME_PROPERTY_ID).add(org.apache.ambari.server.controller.internal.RepositoryResourceProvider.REPOSITORY_DISTRIBUTION_PROPERTY_ID).add(org.apache.ambari.server.controller.internal.RepositoryResourceProvider.REPOSITORY_COMPONENTS_PROPERTY_ID).add(org.apache.ambari.server.controller.internal.RepositoryResourceProvider.REPOSITORY_STACK_NAME_PROPERTY_ID).add(org.apache.ambari.server.controller.internal.RepositoryResourceProvider.REPOSITORY_STACK_VERSION_PROPERTY_ID).add(org.apache.ambari.server.controller.internal.RepositoryResourceProvider.REPOSITORY_OS_TYPE_PROPERTY_ID).add(org.apache.ambari.server.controller.internal.RepositoryResourceProvider.REPOSITORY_BASE_URL_PROPERTY_ID).add(org.apache.ambari.server.controller.internal.RepositoryResourceProvider.REPOSITORY_REPO_ID_PROPERTY_ID).add(org.apache.ambari.server.controller.internal.RepositoryResourceProvider.REPOSITORY_MIRRORS_LIST_PROPERTY_ID).add(org.apache.ambari.server.controller.internal.RepositoryResourceProvider.REPOSITORY_DEFAULT_BASE_URL_PROPERTY_ID).add(org.apache.ambari.server.controller.internal.RepositoryResourceProvider.REPOSITORY_VERIFY_BASE_URL_PROPERTY_ID).add(org.apache.ambari.server.controller.internal.RepositoryResourceProvider.REPOSITORY_REPOSITORY_VERSION_ID_PROPERTY_ID).add(org.apache.ambari.server.controller.internal.RepositoryResourceProvider.REPOSITORY_VERSION_DEFINITION_ID_PROPERTY_ID).add(org.apache.ambari.server.controller.internal.RepositoryResourceProvider.REPOSITORY_CLUSTER_STACK_VERSION_PROPERTY_ID).add(org.apache.ambari.server.controller.internal.RepositoryResourceProvider.REPOSITORY_UNIQUE_PROPERTY_ID).add(org.apache.ambari.server.controller.internal.RepositoryResourceProvider.REPOSITORY_TAGS_PROPERTY_ID).add(org.apache.ambari.server.controller.internal.RepositoryResourceProvider.REPOSITORY_APPLICABLE_SERVICES_PROPERTY_ID).build();

    @java.lang.SuppressWarnings("serial")
    public static final java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> keyPropertyIds = com.google.common.collect.ImmutableMap.<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String>builder().put(org.apache.ambari.server.controller.spi.Resource.Type.Stack, org.apache.ambari.server.controller.internal.RepositoryResourceProvider.REPOSITORY_STACK_NAME_PROPERTY_ID).put(org.apache.ambari.server.controller.spi.Resource.Type.StackVersion, org.apache.ambari.server.controller.internal.RepositoryResourceProvider.REPOSITORY_STACK_VERSION_PROPERTY_ID).put(org.apache.ambari.server.controller.spi.Resource.Type.ClusterStackVersion, org.apache.ambari.server.controller.internal.RepositoryResourceProvider.REPOSITORY_CLUSTER_STACK_VERSION_PROPERTY_ID).put(org.apache.ambari.server.controller.spi.Resource.Type.OperatingSystem, org.apache.ambari.server.controller.internal.RepositoryResourceProvider.REPOSITORY_OS_TYPE_PROPERTY_ID).put(org.apache.ambari.server.controller.spi.Resource.Type.Repository, org.apache.ambari.server.controller.internal.RepositoryResourceProvider.REPOSITORY_REPO_ID_PROPERTY_ID).put(org.apache.ambari.server.controller.spi.Resource.Type.RepositoryVersion, org.apache.ambari.server.controller.internal.RepositoryResourceProvider.REPOSITORY_REPOSITORY_VERSION_ID_PROPERTY_ID).put(org.apache.ambari.server.controller.spi.Resource.Type.VersionDefinition, org.apache.ambari.server.controller.internal.RepositoryResourceProvider.REPOSITORY_VERSION_DEFINITION_ID_PROPERTY_ID).build();

    public RepositoryResourceProvider(org.apache.ambari.server.controller.AmbariManagementController managementController) {
        super(org.apache.ambari.server.controller.spi.Resource.Type.Repository, org.apache.ambari.server.controller.internal.RepositoryResourceProvider.propertyIds, org.apache.ambari.server.controller.internal.RepositoryResourceProvider.keyPropertyIds, managementController);
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus updateResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        final java.util.Set<org.apache.ambari.server.controller.RepositoryRequest> requestsToVerifyBaseURLs = new java.util.HashSet<>();
        java.util.Iterator<java.util.Map<java.lang.String, java.lang.Object>> iterator = request.getProperties().iterator();
        if (iterator.hasNext()) {
            for (java.util.Map<java.lang.String, java.lang.Object> propertyMap : getPropertyMaps(iterator.next(), predicate)) {
                org.apache.ambari.server.controller.RepositoryRequest rr = getRequest(propertyMap);
                if (rr.isVerifyBaseUrl()) {
                    requestsToVerifyBaseURLs.add(rr);
                }
            }
        }
        try {
            getManagementController().verifyRepositories(requestsToVerifyBaseURLs);
        } catch (org.apache.ambari.server.AmbariException e) {
            throw new org.apache.ambari.server.controller.spi.SystemException("", e);
        }
        return getRequestStatus(null);
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.controller.spi.Resource> getResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        final java.util.Set<org.apache.ambari.server.controller.RepositoryRequest> requests = new java.util.HashSet<>();
        if (predicate == null) {
            requests.add(getRequest(java.util.Collections.emptyMap()));
        } else {
            for (java.util.Map<java.lang.String, java.lang.Object> propertyMap : getPropertyMaps(predicate)) {
                requests.add(getRequest(propertyMap));
            }
        }
        java.util.Set<java.lang.String> requestedIds = getRequestPropertyIds(request, predicate);
        java.util.Set<org.apache.ambari.server.controller.RepositoryResponse> responses = getResources(new org.apache.ambari.server.controller.internal.AbstractResourceProvider.Command<java.util.Set<org.apache.ambari.server.controller.RepositoryResponse>>() {
            @java.lang.Override
            public java.util.Set<org.apache.ambari.server.controller.RepositoryResponse> invoke() throws org.apache.ambari.server.AmbariException {
                return getManagementController().getRepositories(requests);
            }
        });
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = new java.util.HashSet<>();
        for (org.apache.ambari.server.controller.RepositoryResponse response : responses) {
            org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Repository);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.RepositoryResourceProvider.REPOSITORY_STACK_NAME_PROPERTY_ID, response.getStackName(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.RepositoryResourceProvider.REPOSITORY_STACK_VERSION_PROPERTY_ID, response.getStackVersion(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.RepositoryResourceProvider.REPOSITORY_REPO_NAME_PROPERTY_ID, response.getRepoName(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.RepositoryResourceProvider.REPOSITORY_DISTRIBUTION_PROPERTY_ID, response.getDistribution(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.RepositoryResourceProvider.REPOSITORY_COMPONENTS_PROPERTY_ID, response.getComponents(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.RepositoryResourceProvider.REPOSITORY_BASE_URL_PROPERTY_ID, org.apache.ambari.server.utils.URLCredentialsHider.hideCredentials(response.getBaseUrl()), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.RepositoryResourceProvider.REPOSITORY_OS_TYPE_PROPERTY_ID, response.getOsType(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.RepositoryResourceProvider.REPOSITORY_REPO_ID_PROPERTY_ID, response.getRepoId(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.RepositoryResourceProvider.REPOSITORY_MIRRORS_LIST_PROPERTY_ID, response.getMirrorsList(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.RepositoryResourceProvider.REPOSITORY_DEFAULT_BASE_URL_PROPERTY_ID, org.apache.ambari.server.utils.URLCredentialsHider.hideCredentials(response.getDefaultBaseUrl()), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.RepositoryResourceProvider.REPOSITORY_UNIQUE_PROPERTY_ID, response.isUnique(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.RepositoryResourceProvider.REPOSITORY_TAGS_PROPERTY_ID, response.getTags(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.RepositoryResourceProvider.REPOSITORY_APPLICABLE_SERVICES_PROPERTY_ID, response.getApplicableServices(), requestedIds);
            if (null != response.getClusterVersionId()) {
                org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.RepositoryResourceProvider.REPOSITORY_CLUSTER_STACK_VERSION_PROPERTY_ID, response.getClusterVersionId(), requestedIds);
            }
            if (null != response.getRepositoryVersionId()) {
                org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.RepositoryResourceProvider.REPOSITORY_REPOSITORY_VERSION_ID_PROPERTY_ID, response.getRepositoryVersionId(), requestedIds);
            }
            if (null != response.getVersionDefinitionId()) {
                org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.RepositoryResourceProvider.REPOSITORY_VERSION_DEFINITION_ID_PROPERTY_ID, response.getVersionDefinitionId(), requestedIds);
            }
            resources.add(resource);
        }
        return resources;
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus createResources(org.apache.ambari.server.controller.spi.Request request) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.ResourceAlreadyExistsException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        final java.lang.String validateOnlyProperty = request.getRequestInfoProperties().get(org.apache.ambari.server.api.resources.RepositoryResourceDefinition.VALIDATE_ONLY_DIRECTIVE);
        if (org.apache.commons.lang.BooleanUtils.toBoolean(validateOnlyProperty)) {
            final java.util.Set<org.apache.ambari.server.controller.RepositoryRequest> requests = new java.util.HashSet<>();
            final java.util.Iterator<java.util.Map<java.lang.String, java.lang.Object>> iterator = request.getProperties().iterator();
            if (iterator.hasNext()) {
                for (java.util.Map<java.lang.String, java.lang.Object> propertyMap : request.getProperties()) {
                    requests.add(getRequest(propertyMap));
                }
            }
            createResources(new org.apache.ambari.server.controller.internal.AbstractResourceProvider.Command<java.lang.Void>() {
                @java.lang.Override
                public java.lang.Void invoke() throws org.apache.ambari.server.AmbariException {
                    getManagementController().verifyRepositories(requests);
                    return null;
                }
            });
            return getRequestStatus(null);
        } else {
            throw new org.apache.ambari.server.controller.spi.SystemException("Cannot create repositories.", null);
        }
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus deleteResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        throw new org.apache.ambari.server.controller.spi.SystemException("Cannot delete repositories.", null);
    }

    private org.apache.ambari.server.controller.RepositoryRequest getRequest(java.util.Map<java.lang.String, java.lang.Object> properties) {
        org.apache.ambari.server.controller.RepositoryRequest request = new org.apache.ambari.server.controller.RepositoryRequest(((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.RepositoryResourceProvider.REPOSITORY_STACK_NAME_PROPERTY_ID))), ((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.RepositoryResourceProvider.REPOSITORY_STACK_VERSION_PROPERTY_ID))), ((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.RepositoryResourceProvider.REPOSITORY_OS_TYPE_PROPERTY_ID))), ((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.RepositoryResourceProvider.REPOSITORY_REPO_ID_PROPERTY_ID))), ((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.RepositoryResourceProvider.REPOSITORY_REPO_NAME_PROPERTY_ID))));
        if (properties.containsKey(org.apache.ambari.server.controller.internal.RepositoryResourceProvider.REPOSITORY_REPOSITORY_VERSION_ID_PROPERTY_ID)) {
            request.setRepositoryVersionId(java.lang.Long.parseLong(properties.get(org.apache.ambari.server.controller.internal.RepositoryResourceProvider.REPOSITORY_REPOSITORY_VERSION_ID_PROPERTY_ID).toString()));
        }
        if (properties.containsKey(org.apache.ambari.server.controller.internal.RepositoryResourceProvider.REPOSITORY_VERSION_DEFINITION_ID_PROPERTY_ID)) {
            request.setVersionDefinitionId(properties.get(org.apache.ambari.server.controller.internal.RepositoryResourceProvider.REPOSITORY_VERSION_DEFINITION_ID_PROPERTY_ID).toString());
        }
        if (properties.containsKey(org.apache.ambari.server.controller.internal.RepositoryResourceProvider.REPOSITORY_CLUSTER_STACK_VERSION_PROPERTY_ID)) {
            request.setClusterVersionId(java.lang.Long.parseLong(properties.get(org.apache.ambari.server.controller.internal.RepositoryResourceProvider.REPOSITORY_CLUSTER_STACK_VERSION_PROPERTY_ID).toString()));
        }
        if (properties.containsKey(org.apache.ambari.server.controller.internal.RepositoryResourceProvider.REPOSITORY_BASE_URL_PROPERTY_ID)) {
            request.setBaseUrl(((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.RepositoryResourceProvider.REPOSITORY_BASE_URL_PROPERTY_ID))));
            if (properties.containsKey(org.apache.ambari.server.controller.internal.RepositoryResourceProvider.REPOSITORY_VERIFY_BASE_URL_PROPERTY_ID)) {
                request.setVerifyBaseUrl("true".equalsIgnoreCase(properties.get(org.apache.ambari.server.controller.internal.RepositoryResourceProvider.REPOSITORY_VERIFY_BASE_URL_PROPERTY_ID).toString()));
            }
        }
        if (properties.containsKey(org.apache.ambari.server.controller.internal.RepositoryResourceProvider.REPOSITORY_MIRRORS_LIST_PROPERTY_ID)) {
            request.setMirrorsList(((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.RepositoryResourceProvider.REPOSITORY_MIRRORS_LIST_PROPERTY_ID))));
        }
        return request;
    }

    @java.lang.Override
    public java.util.Set<java.lang.String> getPKPropertyIds() {
        return org.apache.ambari.server.controller.internal.RepositoryResourceProvider.pkPropertyIds;
    }
}