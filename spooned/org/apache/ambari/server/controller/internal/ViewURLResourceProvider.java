package org.apache.ambari.server.controller.internal;
@org.apache.ambari.server.StaticallyInject
public class ViewURLResourceProvider extends org.apache.ambari.server.controller.internal.AbstractAuthorizedResourceProvider {
    public static final java.lang.String VIEW_URL_INFO = "ViewUrlInfo";

    public static final java.lang.String URL_NAME_PROPERTY_ID = "url_name";

    public static final java.lang.String URL_SUFFIX_PROPERTY_ID = "url_suffix";

    public static final java.lang.String VIEW_INSTANCE_VERSION_PROPERTY_ID = "view_instance_version";

    public static final java.lang.String VIEW_INSTANCE_NAME_PROPERTY_ID = "view_instance_name";

    public static final java.lang.String VIEW_INSTANCE_COMMON_NAME_PROPERTY_ID = "view_instance_common_name";

    public static final java.lang.String URL_NAME = (org.apache.ambari.server.controller.internal.ViewURLResourceProvider.VIEW_URL_INFO + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.ViewURLResourceProvider.URL_NAME_PROPERTY_ID;

    public static final java.lang.String URL_SUFFIX = (org.apache.ambari.server.controller.internal.ViewURLResourceProvider.VIEW_URL_INFO + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.ViewURLResourceProvider.URL_SUFFIX_PROPERTY_ID;

    public static final java.lang.String VIEW_INSTANCE_VERSION = (org.apache.ambari.server.controller.internal.ViewURLResourceProvider.VIEW_URL_INFO + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.ViewURLResourceProvider.VIEW_INSTANCE_VERSION_PROPERTY_ID;

    public static final java.lang.String VIEW_INSTANCE_NAME = (org.apache.ambari.server.controller.internal.ViewURLResourceProvider.VIEW_URL_INFO + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.ViewURLResourceProvider.VIEW_INSTANCE_NAME_PROPERTY_ID;

    public static final java.lang.String VIEW_INSTANCE_COMMON_NAME = (org.apache.ambari.server.controller.internal.ViewURLResourceProvider.VIEW_URL_INFO + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.ViewURLResourceProvider.VIEW_INSTANCE_COMMON_NAME_PROPERTY_ID;

    private static final java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> keyPropertyIds = com.google.common.collect.ImmutableMap.<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String>builder().put(org.apache.ambari.server.controller.spi.Resource.Type.ViewURL, org.apache.ambari.server.controller.internal.ViewURLResourceProvider.URL_NAME).build();

    private static final java.util.Set<java.lang.String> propertyIds = com.google.common.collect.Sets.newHashSet(org.apache.ambari.server.controller.internal.ViewURLResourceProvider.URL_NAME, org.apache.ambari.server.controller.internal.ViewURLResourceProvider.URL_SUFFIX, org.apache.ambari.server.controller.internal.ViewURLResourceProvider.VIEW_INSTANCE_VERSION, org.apache.ambari.server.controller.internal.ViewURLResourceProvider.VIEW_INSTANCE_NAME, org.apache.ambari.server.controller.internal.ViewURLResourceProvider.VIEW_INSTANCE_COMMON_NAME);

    @com.google.inject.Inject
    private static org.apache.ambari.server.orm.dao.ViewURLDAO viewURLDAO;

    public ViewURLResourceProvider() {
        super(org.apache.ambari.server.controller.spi.Resource.Type.ViewURL, org.apache.ambari.server.controller.internal.ViewURLResourceProvider.propertyIds, org.apache.ambari.server.controller.internal.ViewURLResourceProvider.keyPropertyIds);
        java.util.EnumSet<org.apache.ambari.server.security.authorization.RoleAuthorization> requiredAuthorizations = java.util.EnumSet.of(org.apache.ambari.server.security.authorization.RoleAuthorization.AMBARI_MANAGE_VIEWS);
        setRequiredCreateAuthorizations(requiredAuthorizations);
        setRequiredDeleteAuthorizations(requiredAuthorizations);
        setRequiredUpdateAuthorizations(requiredAuthorizations);
    }

    @java.lang.Override
    protected org.apache.ambari.server.controller.spi.RequestStatus createResourcesAuthorized(org.apache.ambari.server.controller.spi.Request request) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.ResourceAlreadyExistsException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        for (java.util.Map<java.lang.String, java.lang.Object> properties : request.getProperties()) {
            createResources(getCreateCommand(properties));
        }
        notifyCreate(org.apache.ambari.server.controller.spi.Resource.Type.ViewURL, request);
        return getRequestStatus(null);
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.controller.spi.Resource> getResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = com.google.common.collect.Sets.newHashSet();
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> propertyMaps = getPropertyMaps(predicate);
        for (java.util.Map<java.lang.String, java.lang.Object> propertyMap : propertyMaps) {
            java.lang.String urlNameProperty = ((java.lang.String) (propertyMap.get(org.apache.ambari.server.controller.internal.ViewURLResourceProvider.URL_NAME)));
            if (!com.google.common.base.Strings.isNullOrEmpty(urlNameProperty)) {
                com.google.common.base.Optional<org.apache.ambari.server.orm.entities.ViewURLEntity> urlEntity = org.apache.ambari.server.controller.internal.ViewURLResourceProvider.viewURLDAO.findByName(urlNameProperty);
                if (urlEntity.isPresent()) {
                    resources.add(toResource(urlEntity.get()));
                }
            } else {
                java.util.List<org.apache.ambari.server.orm.entities.ViewURLEntity> urlEntities = org.apache.ambari.server.controller.internal.ViewURLResourceProvider.viewURLDAO.findAll();
                for (org.apache.ambari.server.orm.entities.ViewURLEntity urlEntity : urlEntities) {
                    resources.add(toResource(urlEntity));
                }
            }
        }
        return resources;
    }

    @java.lang.Override
    protected org.apache.ambari.server.controller.spi.RequestStatus updateResourcesAuthorized(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        java.util.Iterator<java.util.Map<java.lang.String, java.lang.Object>> iterator = request.getProperties().iterator();
        if (iterator.hasNext()) {
            for (java.util.Map<java.lang.String, java.lang.Object> propertyMap : getPropertyMaps(iterator.next(), predicate)) {
                modifyResources(getUpdateCommand(propertyMap));
            }
        }
        notifyUpdate(org.apache.ambari.server.controller.spi.Resource.Type.ViewInstance, request, predicate);
        return getRequestStatus(null);
    }

    @java.lang.Override
    protected org.apache.ambari.server.controller.spi.RequestStatus deleteResourcesAuthorized(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        modifyResources(getDeleteCommand(predicate));
        notifyDelete(org.apache.ambari.server.controller.spi.Resource.Type.ViewInstance, predicate);
        return getRequestStatus(null);
    }

    @java.lang.Override
    public java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> getKeyPropertyIds() {
        return org.apache.ambari.server.controller.internal.ViewURLResourceProvider.keyPropertyIds;
    }

    @java.lang.Override
    protected java.util.Set<java.lang.String> getPKPropertyIds() {
        return new java.util.HashSet<>(org.apache.ambari.server.controller.internal.ViewURLResourceProvider.keyPropertyIds.values());
    }

    protected org.apache.ambari.server.controller.spi.Resource toResource(org.apache.ambari.server.orm.entities.ViewURLEntity viewURLEntity) {
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.ViewURL);
        resource.setProperty(org.apache.ambari.server.controller.internal.ViewURLResourceProvider.URL_NAME, viewURLEntity.getUrlName());
        resource.setProperty(org.apache.ambari.server.controller.internal.ViewURLResourceProvider.URL_SUFFIX, viewURLEntity.getUrlSuffix());
        org.apache.ambari.server.orm.entities.ViewInstanceEntity viewInstanceEntity = viewURLEntity.getViewInstanceEntity();
        if (viewInstanceEntity == null)
            return resource;

        org.apache.ambari.server.orm.entities.ViewEntity viewEntity = viewInstanceEntity.getViewEntity();
        java.lang.String viewName = viewEntity.getCommonName();
        java.lang.String version = viewEntity.getVersion();
        java.lang.String name = viewInstanceEntity.getName();
        resource.setProperty(org.apache.ambari.server.controller.internal.ViewURLResourceProvider.VIEW_INSTANCE_NAME, name);
        resource.setProperty(org.apache.ambari.server.controller.internal.ViewURLResourceProvider.VIEW_INSTANCE_VERSION, version);
        resource.setProperty(org.apache.ambari.server.controller.internal.ViewURLResourceProvider.VIEW_INSTANCE_COMMON_NAME, viewName);
        return resource;
    }

    private org.apache.ambari.server.orm.entities.ViewURLEntity toEntity(java.util.Map<java.lang.String, java.lang.Object> properties) throws org.apache.ambari.server.AmbariException {
        java.lang.String name = ((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.ViewURLResourceProvider.URL_NAME)));
        if ((name == null) || name.isEmpty()) {
            throw new java.lang.IllegalArgumentException("The View URL is a required property.");
        }
        java.lang.String suffix = ((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.ViewURLResourceProvider.URL_SUFFIX)));
        java.lang.String commonName = ((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.ViewURLResourceProvider.VIEW_INSTANCE_COMMON_NAME)));
        java.lang.String instanceName = ((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.ViewURLResourceProvider.VIEW_INSTANCE_NAME)));
        java.lang.String instanceVersion = ((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.ViewURLResourceProvider.VIEW_INSTANCE_VERSION)));
        org.apache.ambari.server.view.ViewRegistry viewRegistry = org.apache.ambari.server.view.ViewRegistry.getInstance();
        org.apache.ambari.server.orm.entities.ViewInstanceEntity instanceEntity = viewRegistry.getInstanceDefinition(commonName, instanceVersion, instanceName);
        org.apache.ambari.server.orm.entities.ViewURLEntity urlEntity = new org.apache.ambari.server.orm.entities.ViewURLEntity();
        urlEntity.setUrlName(name);
        urlEntity.setUrlSuffix(suffix);
        urlEntity.setViewInstanceEntity(instanceEntity);
        return urlEntity;
    }

    private org.apache.ambari.server.controller.internal.AbstractResourceProvider.Command<java.lang.Void> getCreateCommand(final java.util.Map<java.lang.String, java.lang.Object> properties) {
        return new org.apache.ambari.server.controller.internal.AbstractResourceProvider.Command<java.lang.Void>() {
            @java.lang.Override
            public java.lang.Void invoke() throws org.apache.ambari.server.AmbariException {
                org.apache.ambari.server.view.ViewRegistry viewRegistry = org.apache.ambari.server.view.ViewRegistry.getInstance();
                org.apache.ambari.server.orm.entities.ViewURLEntity urlEntity = toEntity(properties);
                org.apache.ambari.server.orm.entities.ViewInstanceEntity viewInstanceEntity = urlEntity.getViewInstanceEntity();
                org.apache.ambari.server.orm.entities.ViewEntity viewEntity = viewInstanceEntity.getViewEntity();
                java.lang.String viewName = viewEntity.getCommonName();
                java.lang.String version = viewEntity.getVersion();
                org.apache.ambari.server.orm.entities.ViewEntity view = viewRegistry.getDefinition(viewName, version);
                if (view == null) {
                    throw new java.lang.IllegalStateException(("The view " + viewName) + " is not registered.");
                }
                if (!view.isDeployed()) {
                    throw new java.lang.IllegalStateException(("The view " + viewName) + " is not loaded.");
                }
                if (viewInstanceEntity.getViewUrl() != null) {
                    throw new org.apache.ambari.server.AmbariException("The view instance selected already has a linked URL");
                }
                if (org.apache.ambari.server.controller.internal.ViewURLResourceProvider.viewURLDAO.findByName(urlEntity.getUrlName()).isPresent()) {
                    throw new org.apache.ambari.server.AmbariException("This view URL name exists, URL names should be unique");
                }
                if (org.apache.ambari.server.controller.internal.ViewURLResourceProvider.viewURLDAO.findBySuffix(urlEntity.getUrlSuffix()).isPresent()) {
                    throw new org.apache.ambari.server.AmbariException("This view URL suffix has already been recorded, URL suffixes should be unique");
                }
                org.apache.ambari.server.controller.internal.ViewURLResourceProvider.viewURLDAO.save(urlEntity);
                viewInstanceEntity.setViewUrl(urlEntity);
                try {
                    viewRegistry.updateViewInstance(viewInstanceEntity);
                } catch (org.apache.ambari.server.view.validation.ValidationException e) {
                    throw new java.lang.IllegalArgumentException(e.getMessage(), e);
                } catch (org.apache.ambari.view.SystemException e) {
                    throw new org.apache.ambari.server.AmbariException("Caught exception trying to update view URL.", e);
                }
                viewRegistry.updateView(viewInstanceEntity);
                return null;
            }
        };
    }

    private org.apache.ambari.server.controller.internal.AbstractResourceProvider.Command<java.lang.Void> getUpdateCommand(final java.util.Map<java.lang.String, java.lang.Object> properties) {
        return new org.apache.ambari.server.controller.internal.AbstractResourceProvider.Command<java.lang.Void>() {
            @java.lang.Override
            public java.lang.Void invoke() throws org.apache.ambari.server.AmbariException {
                org.apache.ambari.server.view.ViewRegistry registry = org.apache.ambari.server.view.ViewRegistry.getInstance();
                java.lang.String name = ((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.ViewURLResourceProvider.URL_NAME)));
                java.lang.String suffix = ((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.ViewURLResourceProvider.URL_SUFFIX)));
                com.google.common.base.Optional<org.apache.ambari.server.orm.entities.ViewURLEntity> entity = org.apache.ambari.server.controller.internal.ViewURLResourceProvider.viewURLDAO.findByName(name);
                if (!entity.isPresent()) {
                    throw new org.apache.ambari.server.AmbariException(("URL with name " + name) + "was not found");
                }
                entity.get().setUrlSuffix(suffix);
                org.apache.ambari.server.controller.internal.ViewURLResourceProvider.viewURLDAO.update(entity.get());
                org.apache.ambari.server.orm.entities.ViewInstanceEntity viewInstanceEntity = entity.get().getViewInstanceEntity();
                try {
                    registry.updateViewInstance(viewInstanceEntity);
                } catch (org.apache.ambari.server.view.validation.ValidationException e) {
                    throw new java.lang.IllegalArgumentException(e.getMessage(), e);
                } catch (org.apache.ambari.view.SystemException e) {
                    throw new org.apache.ambari.server.AmbariException("Caught exception trying to update view URL.", e);
                }
                registry.updateView(viewInstanceEntity);
                return null;
            }
        };
    }

    private org.apache.ambari.server.controller.internal.AbstractResourceProvider.Command<java.lang.Void> getDeleteCommand(final org.apache.ambari.server.controller.spi.Predicate predicate) {
        return new org.apache.ambari.server.controller.internal.AbstractResourceProvider.Command<java.lang.Void>() {
            @java.lang.Override
            public java.lang.Void invoke() throws org.apache.ambari.server.AmbariException {
                org.apache.ambari.server.view.ViewRegistry viewRegistry = org.apache.ambari.server.view.ViewRegistry.getInstance();
                java.lang.Comparable deletedUrl = ((org.apache.ambari.server.controller.predicate.EqualsPredicate) (predicate)).getValue();
                java.lang.String toDelete = deletedUrl.toString();
                com.google.common.base.Optional<org.apache.ambari.server.orm.entities.ViewURLEntity> urlEntity = org.apache.ambari.server.controller.internal.ViewURLResourceProvider.viewURLDAO.findByName(toDelete);
                if (!urlEntity.isPresent()) {
                    throw new org.apache.ambari.server.AmbariException(("The URL " + toDelete) + "does not exist");
                }
                org.apache.ambari.server.orm.entities.ViewInstanceEntity viewInstanceEntity = urlEntity.get().getViewInstanceEntity();
                if (viewInstanceEntity != null) {
                    viewInstanceEntity.clearUrl();
                    try {
                        viewRegistry.updateViewInstance(viewInstanceEntity);
                    } catch (org.apache.ambari.server.view.validation.ValidationException e) {
                        throw new java.lang.IllegalArgumentException(e.getMessage(), e);
                    } catch (org.apache.ambari.view.SystemException e) {
                        throw new org.apache.ambari.server.AmbariException("Caught exception trying to update view URL.", e);
                    }
                    viewRegistry.updateView(viewInstanceEntity);
                }
                urlEntity.get().clearEntity();
                org.apache.ambari.server.controller.internal.ViewURLResourceProvider.viewURLDAO.delete(urlEntity.get());
                return null;
            }
        };
    }
}