package org.apache.ambari.server.controller.internal;
import com.google.inject.persist.Transactional;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
public class RepositoryVersionResourceProvider extends org.apache.ambari.server.controller.internal.AbstractAuthorizedResourceProvider {
    public static final java.lang.String REPOSITORY_VERSION = "RepositoryVersions";

    public static final java.lang.String REPOSITORY_VERSION_ID_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("RepositoryVersions", "id");

    public static final java.lang.String REPOSITORY_VERSION_STACK_NAME_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("RepositoryVersions", "stack_name");

    public static final java.lang.String REPOSITORY_VERSION_STACK_VERSION_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("RepositoryVersions", "stack_version");

    public static final java.lang.String REPOSITORY_VERSION_REPOSITORY_VERSION_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("RepositoryVersions", "repository_version");

    public static final java.lang.String REPOSITORY_VERSION_DISPLAY_NAME_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("RepositoryVersions", "display_name");

    public static final java.lang.String REPOSITORY_VERSION_HIDDEN_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("RepositoryVersions", "hidden");

    public static final java.lang.String REPOSITORY_VERSION_RESOLVED_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("RepositoryVersions", "resolved");

    public static final java.lang.String SUBRESOURCE_OPERATING_SYSTEMS_PROPERTY_ID = new org.apache.ambari.server.api.resources.OperatingSystemResourceDefinition().getPluralName();

    public static final java.lang.String SUBRESOURCE_REPOSITORIES_PROPERTY_ID = new org.apache.ambari.server.api.resources.RepositoryResourceDefinition().getPluralName();

    public static final java.lang.String REPOSITORY_VERSION_TYPE_PROPERTY_ID = "RepositoryVersions/type";

    public static final java.lang.String REPOSITORY_VERSION_RELEASE_VERSION = "RepositoryVersions/release/version";

    public static final java.lang.String REPOSITORY_VERSION_RELEASE_BUILD = "RepositoryVersions/release/build";

    public static final java.lang.String REPOSITORY_VERSION_RELEASE_NOTES = "RepositoryVersions/release/notes";

    public static final java.lang.String REPOSITORY_VERSION_RELEASE_COMPATIBLE_WITH = "RepositoryVersions/release/compatible_with";

    public static final java.lang.String REPOSITORY_VERSION_AVAILABLE_SERVICES = "RepositoryVersions/services";

    public static final java.lang.String REPOSITORY_VERSION_STACK_SERVICES = "RepositoryVersions/stack_services";

    public static final java.lang.String REPOSITORY_VERSION_PARENT_ID = "RepositoryVersions/parent_id";

    public static final java.lang.String REPOSITORY_VERSION_HAS_CHILDREN = "RepositoryVersions/has_children";

    @java.lang.SuppressWarnings("serial")
    private static final java.util.Set<java.lang.String> pkPropertyIds = com.google.common.collect.ImmutableSet.of(org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.REPOSITORY_VERSION_ID_PROPERTY_ID);

    @java.lang.SuppressWarnings("serial")
    public static final java.util.Set<java.lang.String> propertyIds = com.google.common.collect.ImmutableSet.of(org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.REPOSITORY_VERSION_ID_PROPERTY_ID, org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.REPOSITORY_VERSION_REPOSITORY_VERSION_PROPERTY_ID, org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.REPOSITORY_VERSION_DISPLAY_NAME_PROPERTY_ID, org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.REPOSITORY_VERSION_HIDDEN_PROPERTY_ID, org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.REPOSITORY_VERSION_STACK_NAME_PROPERTY_ID, org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.REPOSITORY_VERSION_STACK_VERSION_PROPERTY_ID, org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.SUBRESOURCE_OPERATING_SYSTEMS_PROPERTY_ID, org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.REPOSITORY_VERSION_TYPE_PROPERTY_ID, org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.REPOSITORY_VERSION_RELEASE_BUILD, org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.REPOSITORY_VERSION_RELEASE_COMPATIBLE_WITH, org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.REPOSITORY_VERSION_RELEASE_NOTES, org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.REPOSITORY_VERSION_RELEASE_VERSION, org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.REPOSITORY_VERSION_PARENT_ID, org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.REPOSITORY_VERSION_HAS_CHILDREN, org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.REPOSITORY_VERSION_AVAILABLE_SERVICES, org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.REPOSITORY_VERSION_STACK_SERVICES, org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.REPOSITORY_VERSION_RESOLVED_PROPERTY_ID);

    @java.lang.SuppressWarnings("serial")
    public static final java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> keyPropertyIds = new com.google.common.collect.ImmutableMap.Builder<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String>().put(org.apache.ambari.server.controller.spi.Resource.Type.Stack, org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.REPOSITORY_VERSION_STACK_NAME_PROPERTY_ID).put(org.apache.ambari.server.controller.spi.Resource.Type.StackVersion, org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.REPOSITORY_VERSION_STACK_VERSION_PROPERTY_ID).put(org.apache.ambari.server.controller.spi.Resource.Type.RepositoryVersion, org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.REPOSITORY_VERSION_ID_PROPERTY_ID).build();

    @com.google.inject.Inject
    private com.google.gson.Gson gson;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.RepositoryVersionDAO repositoryVersionDAO;

    @com.google.inject.Inject
    private org.apache.ambari.server.api.services.AmbariMetaInfo ambariMetaInfo;

    @com.google.inject.Inject
    private org.apache.ambari.server.stack.upgrade.RepositoryVersionHelper repositoryVersionHelper;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.StackDAO stackDAO;

    @com.google.inject.Inject
    org.apache.ambari.server.orm.dao.HostVersionDAO hostVersionDAO;

    public RepositoryVersionResourceProvider() {
        super(org.apache.ambari.server.controller.spi.Resource.Type.RepositoryVersion, org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.propertyIds, org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.keyPropertyIds);
        setRequiredCreateAuthorizations(java.util.EnumSet.of(org.apache.ambari.server.security.authorization.RoleAuthorization.AMBARI_MANAGE_STACK_VERSIONS));
        setRequiredDeleteAuthorizations(java.util.EnumSet.of(org.apache.ambari.server.security.authorization.RoleAuthorization.AMBARI_MANAGE_STACK_VERSIONS));
        setRequiredUpdateAuthorizations(java.util.EnumSet.of(org.apache.ambari.server.security.authorization.RoleAuthorization.AMBARI_MANAGE_STACK_VERSIONS, org.apache.ambari.server.security.authorization.RoleAuthorization.AMBARI_EDIT_STACK_REPOS));
        setRequiredGetAuthorizations(java.util.EnumSet.of(org.apache.ambari.server.security.authorization.RoleAuthorization.AMBARI_MANAGE_STACK_VERSIONS, org.apache.ambari.server.security.authorization.RoleAuthorization.AMBARI_EDIT_STACK_REPOS, org.apache.ambari.server.security.authorization.RoleAuthorization.CLUSTER_VIEW_STACK_DETAILS, org.apache.ambari.server.security.authorization.RoleAuthorization.CLUSTER_UPGRADE_DOWNGRADE_STACK));
    }

    @java.lang.Override
    protected org.apache.ambari.server.controller.spi.RequestStatus createResourcesAuthorized(final org.apache.ambari.server.controller.spi.Request request) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.ResourceAlreadyExistsException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        for (final java.util.Map<java.lang.String, java.lang.Object> properties : request.getProperties()) {
            createResources(new org.apache.ambari.server.controller.internal.AbstractResourceProvider.Command<java.lang.Void>() {
                @java.lang.Override
                public java.lang.Void invoke() throws org.apache.ambari.server.AmbariException {
                    final java.lang.String[] requiredProperties = new java.lang.String[]{ org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.REPOSITORY_VERSION_DISPLAY_NAME_PROPERTY_ID, org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.SUBRESOURCE_OPERATING_SYSTEMS_PROPERTY_ID, org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.REPOSITORY_VERSION_STACK_NAME_PROPERTY_ID, org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.REPOSITORY_VERSION_STACK_VERSION_PROPERTY_ID, org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.REPOSITORY_VERSION_REPOSITORY_VERSION_PROPERTY_ID };
                    for (java.lang.String propertyName : requiredProperties) {
                        if (properties.get(propertyName) == null) {
                            throw new org.apache.ambari.server.AmbariException(("Property " + propertyName) + " should be provided");
                        }
                    }
                    org.apache.ambari.server.orm.entities.RepositoryVersionEntity entity = toRepositoryVersionEntity(properties);
                    if (repositoryVersionDAO.findByDisplayName(entity.getDisplayName()) != null) {
                        throw new org.apache.ambari.server.DuplicateResourceException(("Repository version with name " + entity.getDisplayName()) + " already exists");
                    }
                    if (repositoryVersionDAO.findByStackAndVersion(entity.getStack(), entity.getVersion()) != null) {
                        throw new org.apache.ambari.server.DuplicateResourceException(((("Repository version for stack " + entity.getStack()) + " and version ") + entity.getVersion()) + " already exists");
                    }
                    org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.validateRepositoryVersion(repositoryVersionDAO, ambariMetaInfo, entity);
                    repositoryVersionDAO.create(entity);
                    notifyCreate(org.apache.ambari.server.controller.spi.Resource.Type.RepositoryVersion, request);
                    return null;
                }
            });
        }
        return getRequestStatus(null);
    }

    @java.lang.Override
    protected java.util.Set<org.apache.ambari.server.controller.spi.Resource> getResourcesAuthorized(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        final java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = new java.util.HashSet<>();
        final java.util.Set<java.lang.String> requestedIds = getRequestPropertyIds(request, predicate);
        final java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> propertyMaps = getPropertyMaps(predicate);
        java.util.List<org.apache.ambari.server.orm.entities.RepositoryVersionEntity> requestedEntities = new java.util.ArrayList<>();
        for (java.util.Map<java.lang.String, java.lang.Object> propertyMap : propertyMaps) {
            final org.apache.ambari.server.state.StackId stackId = getStackInformationFromUrl(propertyMap);
            if (((stackId != null) && (propertyMaps.size() == 1)) && (propertyMap.get(org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.REPOSITORY_VERSION_ID_PROPERTY_ID) == null)) {
                requestedEntities.addAll(repositoryVersionDAO.findByStack(stackId));
            } else {
                final java.lang.Long id;
                try {
                    id = java.lang.Long.parseLong(propertyMap.get(org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.REPOSITORY_VERSION_ID_PROPERTY_ID).toString());
                } catch (java.lang.Exception ex) {
                    throw new org.apache.ambari.server.controller.spi.SystemException("Repository version should have numerical id");
                }
                final org.apache.ambari.server.orm.entities.RepositoryVersionEntity entity = repositoryVersionDAO.findByPK(id);
                if (entity == null) {
                    throw new org.apache.ambari.server.controller.spi.NoSuchResourceException("There is no repository version with id " + id);
                } else {
                    requestedEntities.add(entity);
                }
            }
        }
        for (org.apache.ambari.server.orm.entities.RepositoryVersionEntity entity : requestedEntities) {
            final org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.RepositoryVersion);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.REPOSITORY_VERSION_ID_PROPERTY_ID, entity.getId(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.REPOSITORY_VERSION_STACK_NAME_PROPERTY_ID, entity.getStackName(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.REPOSITORY_VERSION_STACK_VERSION_PROPERTY_ID, entity.getStackVersion(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.REPOSITORY_VERSION_DISPLAY_NAME_PROPERTY_ID, entity.getDisplayName(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.REPOSITORY_VERSION_HIDDEN_PROPERTY_ID, entity.isHidden(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.REPOSITORY_VERSION_REPOSITORY_VERSION_PROPERTY_ID, entity.getVersion(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.REPOSITORY_VERSION_TYPE_PROPERTY_ID, entity.getType(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.REPOSITORY_VERSION_RESOLVED_PROPERTY_ID, entity.isResolved(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.REPOSITORY_VERSION_PARENT_ID, entity.getParentId(), requestedIds);
            java.util.List<org.apache.ambari.server.orm.entities.RepositoryVersionEntity> children = entity.getChildren();
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.REPOSITORY_VERSION_HAS_CHILDREN, (null != children) && (!children.isEmpty()), requestedIds);
            final org.apache.ambari.server.state.repository.VersionDefinitionXml xml;
            try {
                xml = entity.getRepositoryXml();
            } catch (java.lang.Exception e) {
                throw new org.apache.ambari.server.controller.spi.SystemException(java.lang.String.format("Could not load xml for Repository %s", entity.getId()), e);
            }
            final org.apache.ambari.server.state.StackInfo stack;
            try {
                stack = ambariMetaInfo.getStack(entity.getStackName(), entity.getStackVersion());
            } catch (org.apache.ambari.server.AmbariException e) {
                throw new org.apache.ambari.server.controller.spi.SystemException(java.lang.String.format("Could not load stack %s for Repository %s", entity.getStackId().toString(), entity.getId()));
            }
            final java.util.List<org.apache.ambari.server.state.repository.ManifestServiceInfo> stackServices;
            if (null != xml) {
                org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.REPOSITORY_VERSION_RELEASE_VERSION, xml.release.version, requestedIds);
                org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.REPOSITORY_VERSION_RELEASE_BUILD, xml.release.build, requestedIds);
                org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.REPOSITORY_VERSION_RELEASE_COMPATIBLE_WITH, xml.release.compatibleWith, requestedIds);
                org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.REPOSITORY_VERSION_RELEASE_NOTES, xml.release.releaseNotes, requestedIds);
                org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.REPOSITORY_VERSION_AVAILABLE_SERVICES, xml.getAvailableServices(stack), requestedIds);
                stackServices = xml.getStackServices(stack);
            } else {
                stackServices = new java.util.ArrayList<>();
                for (org.apache.ambari.server.state.ServiceInfo si : stack.getServices()) {
                    stackServices.add(new org.apache.ambari.server.state.repository.ManifestServiceInfo(si.getName(), si.getDisplayName(), si.getComment(), java.util.Collections.singleton(si.getVersion())));
                }
            }
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.REPOSITORY_VERSION_STACK_SERVICES, stackServices, requestedIds);
            resources.add(resource);
        }
        return resources;
    }

    @java.lang.Override
    @com.google.inject.persist.Transactional
    protected org.apache.ambari.server.controller.spi.RequestStatus updateResourcesAuthorized(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        final java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> propertyMaps = request.getProperties();
        modifyResources(new org.apache.ambari.server.controller.internal.AbstractResourceProvider.Command<java.lang.Void>() {
            @java.lang.Override
            public java.lang.Void invoke() throws org.apache.ambari.server.AmbariException, org.apache.ambari.server.security.authorization.AuthorizationException {
                for (java.util.Map<java.lang.String, java.lang.Object> propertyMap : propertyMaps) {
                    final java.lang.Long id;
                    try {
                        id = java.lang.Long.parseLong(propertyMap.get(org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.REPOSITORY_VERSION_ID_PROPERTY_ID).toString());
                    } catch (java.lang.Exception ex) {
                        throw new org.apache.ambari.server.AmbariException("Repository version should have numerical id");
                    }
                    final org.apache.ambari.server.orm.entities.RepositoryVersionEntity entity = repositoryVersionDAO.findByPK(id);
                    if (entity == null) {
                        throw new org.apache.ambari.server.ObjectNotFoundException("There is no repository version with id " + id);
                    }
                    if (org.apache.commons.lang.StringUtils.isNotBlank(org.apache.commons.lang.ObjectUtils.toString(propertyMap.get(org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.SUBRESOURCE_OPERATING_SYSTEMS_PROPERTY_ID)))) {
                        if (!org.apache.ambari.server.security.authorization.AuthorizationHelper.isAuthorized(org.apache.ambari.server.security.authorization.ResourceType.AMBARI, null, org.apache.ambari.server.security.authorization.RoleAuthorization.AMBARI_EDIT_STACK_REPOS)) {
                            throw new org.apache.ambari.server.security.authorization.AuthorizationException("The authenticated user does not have authorization to modify stack repositories");
                        }
                        final java.lang.Object operatingSystems = propertyMap.get(org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.SUBRESOURCE_OPERATING_SYSTEMS_PROPERTY_ID);
                        final java.lang.String operatingSystemsJson = gson.toJson(operatingSystems);
                        try {
                            entity.addRepoOsEntities(repositoryVersionHelper.parseOperatingSystems(operatingSystemsJson));
                        } catch (java.lang.Exception ex) {
                            throw new org.apache.ambari.server.AmbariException("Json structure for operating systems is incorrect", ex);
                        }
                    }
                    if (org.apache.commons.lang.StringUtils.isNotBlank(org.apache.commons.lang.ObjectUtils.toString(propertyMap.get(org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.REPOSITORY_VERSION_DISPLAY_NAME_PROPERTY_ID)))) {
                        entity.setDisplayName(propertyMap.get(org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.REPOSITORY_VERSION_DISPLAY_NAME_PROPERTY_ID).toString());
                    }
                    if (org.apache.commons.lang.StringUtils.isNotBlank(org.apache.commons.lang.ObjectUtils.toString(propertyMap.get(org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.REPOSITORY_VERSION_HIDDEN_PROPERTY_ID)))) {
                        boolean isHidden = java.lang.Boolean.valueOf(org.apache.commons.lang.ObjectUtils.toString(propertyMap.get(org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.REPOSITORY_VERSION_HIDDEN_PROPERTY_ID)));
                        entity.setHidden(isHidden);
                    }
                    org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.validateRepositoryVersion(repositoryVersionDAO, ambariMetaInfo, entity);
                    repositoryVersionDAO.merge(entity);
                }
                return null;
            }
        });
        return getRequestStatus(null);
    }

    @java.lang.Override
    protected org.apache.ambari.server.controller.spi.RequestStatus deleteResourcesAuthorized(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        final java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> propertyMaps = getPropertyMaps(predicate);
        final java.util.List<org.apache.ambari.server.orm.entities.RepositoryVersionEntity> entitiesToBeRemoved = new java.util.ArrayList<>();
        for (java.util.Map<java.lang.String, java.lang.Object> propertyMap : propertyMaps) {
            final java.lang.Long id;
            try {
                id = java.lang.Long.parseLong(propertyMap.get(org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.REPOSITORY_VERSION_ID_PROPERTY_ID).toString());
            } catch (java.lang.Exception ex) {
                throw new org.apache.ambari.server.controller.spi.SystemException("Repository version should have numerical id");
            }
            final org.apache.ambari.server.orm.entities.RepositoryVersionEntity entity = repositoryVersionDAO.findByPK(id);
            if (entity == null) {
                throw new org.apache.ambari.server.controller.spi.NoSuchResourceException("There is no repository version with id " + id);
            }
            final java.util.Set<org.apache.ambari.server.state.RepositoryVersionState> forbiddenToDeleteStates = com.google.common.collect.Sets.newHashSet(org.apache.ambari.server.state.RepositoryVersionState.CURRENT, org.apache.ambari.server.state.RepositoryVersionState.INSTALLED, org.apache.ambari.server.state.RepositoryVersionState.INSTALLING);
            java.util.List<org.apache.ambari.server.orm.entities.HostVersionEntity> hostVersions = hostVersionDAO.findByRepositoryAndStates(entity, forbiddenToDeleteStates);
            if (org.apache.commons.collections.CollectionUtils.isNotEmpty(hostVersions)) {
                java.util.Map<org.apache.ambari.server.state.RepositoryVersionState, java.util.Set<java.lang.String>> hostsInUse = new java.util.HashMap<>();
                for (org.apache.ambari.server.orm.entities.HostVersionEntity hostVersion : hostVersions) {
                    if (!hostsInUse.containsKey(hostVersion.getState())) {
                        hostsInUse.put(hostVersion.getState(), new java.util.HashSet<>());
                    }
                    hostsInUse.get(hostVersion.getState()).add(hostVersion.getHostName());
                }
                java.util.Set<java.lang.String> errors = new java.util.HashSet<>();
                for (java.util.Map.Entry<org.apache.ambari.server.state.RepositoryVersionState, java.util.Set<java.lang.String>> entry : hostsInUse.entrySet()) {
                    errors.add(java.lang.String.format("%s on %s", entry.getKey(), org.apache.commons.lang.StringUtils.join(entry.getValue(), ", ")));
                }
                throw new org.apache.ambari.server.controller.spi.SystemException(java.lang.String.format("Repository version can't be deleted as it is used by the following hosts: %s", org.apache.commons.lang.StringUtils.join(errors, ';')));
            }
            entitiesToBeRemoved.add(entity);
        }
        for (org.apache.ambari.server.orm.entities.RepositoryVersionEntity entity : entitiesToBeRemoved) {
            repositoryVersionDAO.remove(entity);
        }
        return getRequestStatus(null);
    }

    @java.lang.Override
    protected java.util.Set<java.lang.String> getPKPropertyIds() {
        return org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.pkPropertyIds;
    }

    protected static void validateRepositoryVersion(org.apache.ambari.server.orm.dao.RepositoryVersionDAO dao, org.apache.ambari.server.api.services.AmbariMetaInfo metaInfo, org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.validateRepositoryVersion(dao, metaInfo, repositoryVersion, false);
    }

    protected static void validateRepositoryVersion(org.apache.ambari.server.orm.dao.RepositoryVersionDAO dao, org.apache.ambari.server.api.services.AmbariMetaInfo metaInfo, org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion, boolean skipUrlCheck) throws org.apache.ambari.server.AmbariException {
        final org.apache.ambari.server.state.StackId requiredStack = new org.apache.ambari.server.state.StackId(repositoryVersion.getStack());
        final java.lang.String requiredStackName = requiredStack.getStackName();
        final java.lang.String requiredStackVersion = requiredStack.getStackVersion();
        final java.lang.String requiredStackId = requiredStack.getStackId();
        java.util.Set<java.lang.String> existingRepoUrls = new java.util.HashSet<>();
        java.util.List<org.apache.ambari.server.orm.entities.RepositoryVersionEntity> existingRepoVersions = dao.findByStack(requiredStack);
        for (org.apache.ambari.server.orm.entities.RepositoryVersionEntity existingRepoVersion : existingRepoVersions) {
            for (org.apache.ambari.server.orm.entities.RepoOsEntity operatingSystemEntity : existingRepoVersion.getRepoOsEntities()) {
                for (org.apache.ambari.server.orm.entities.RepoDefinitionEntity repositoryEntity : operatingSystemEntity.getRepoDefinitionEntities()) {
                    if (repositoryEntity.isUnique() && (!existingRepoVersion.getId().equals(repositoryVersion.getId()))) {
                        existingRepoUrls.add(repositoryEntity.getBaseUrl());
                    }
                }
            }
        }
        final java.util.Set<java.lang.String> osSupported = new java.util.HashSet<>();
        for (org.apache.ambari.server.state.OperatingSystemInfo osInfo : metaInfo.getOperatingSystems(requiredStackName, requiredStackVersion)) {
            osSupported.add(osInfo.getOsType());
        }
        final java.util.Set<java.lang.String> osRepositoryVersion = new java.util.HashSet<>();
        for (org.apache.ambari.server.orm.entities.RepoOsEntity os : repositoryVersion.getRepoOsEntities()) {
            osRepositoryVersion.add(os.getFamily());
            for (org.apache.ambari.server.orm.entities.RepoDefinitionEntity repositoryEntity : os.getRepoDefinitionEntities()) {
                java.lang.String baseUrl = repositoryEntity.getBaseUrl();
                if (((!skipUrlCheck) && os.isAmbariManaged()) && existingRepoUrls.contains(baseUrl)) {
                    throw new org.apache.ambari.server.DuplicateResourceException((("Base url " + org.apache.ambari.server.utils.URLCredentialsHider.hideCredentials(baseUrl)) + " is already defined for another repository version. ") + "Setting up base urls that contain the same versions of components will cause stack upgrade to fail.");
                }
            }
        }
        if (osRepositoryVersion.isEmpty()) {
            throw new org.apache.ambari.server.AmbariException("At least one set of repositories for OS should be provided");
        }
        for (java.lang.String os : osRepositoryVersion) {
            if (!osSupported.contains(os)) {
                throw new org.apache.ambari.server.AmbariException((("Operating system type " + os) + " is not supported by stack ") + requiredStackId);
            }
        }
        if (!org.apache.ambari.server.orm.entities.RepositoryVersionEntity.isVersionInStack(repositoryVersion.getStackId(), repositoryVersion.getVersion())) {
            throw new org.apache.ambari.server.AmbariException(java.text.MessageFormat.format("Version {0} needs to belong to stack {1}", repositoryVersion.getVersion(), (repositoryVersion.getStackName() + "-") + repositoryVersion.getStackVersion()));
        }
    }

    protected org.apache.ambari.server.orm.entities.RepositoryVersionEntity toRepositoryVersionEntity(java.util.Map<java.lang.String, java.lang.Object> properties) throws org.apache.ambari.server.AmbariException {
        final org.apache.ambari.server.orm.entities.RepositoryVersionEntity entity = new org.apache.ambari.server.orm.entities.RepositoryVersionEntity();
        final java.lang.String stackName = properties.get(org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.REPOSITORY_VERSION_STACK_NAME_PROPERTY_ID).toString();
        final java.lang.String stackVersion = properties.get(org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.REPOSITORY_VERSION_STACK_VERSION_PROPERTY_ID).toString();
        org.apache.ambari.server.orm.entities.StackEntity stackEntity = stackDAO.find(stackName, stackVersion);
        entity.setDisplayName(properties.get(org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.REPOSITORY_VERSION_DISPLAY_NAME_PROPERTY_ID).toString());
        entity.setStack(stackEntity);
        entity.setVersion(properties.get(org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.REPOSITORY_VERSION_REPOSITORY_VERSION_PROPERTY_ID).toString());
        final java.lang.Object operatingSystems = properties.get(org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.SUBRESOURCE_OPERATING_SYSTEMS_PROPERTY_ID);
        final java.lang.String operatingSystemsJson = gson.toJson(operatingSystems);
        try {
            entity.addRepoOsEntities(repositoryVersionHelper.parseOperatingSystems(operatingSystemsJson));
        } catch (java.lang.Exception ex) {
            throw new org.apache.ambari.server.AmbariException("Json structure for operating systems is incorrect", ex);
        }
        return entity;
    }

    protected org.apache.ambari.server.state.StackId getStackInformationFromUrl(java.util.Map<java.lang.String, java.lang.Object> propertyMap) {
        if (propertyMap.containsKey(org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.REPOSITORY_VERSION_STACK_NAME_PROPERTY_ID) && propertyMap.containsKey(org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.REPOSITORY_VERSION_STACK_VERSION_PROPERTY_ID)) {
            return new org.apache.ambari.server.state.StackId(propertyMap.get(org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.REPOSITORY_VERSION_STACK_NAME_PROPERTY_ID).toString(), propertyMap.get(org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.REPOSITORY_VERSION_STACK_VERSION_PROPERTY_ID).toString());
        }
        return null;
    }

    @java.lang.Override
    protected org.apache.ambari.server.security.authorization.ResourceType getResourceType(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) {
        return null;
    }
}