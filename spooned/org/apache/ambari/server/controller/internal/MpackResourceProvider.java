package org.apache.ambari.server.controller.internal;
import org.apache.commons.lang.Validate;
@org.apache.ambari.server.StaticallyInject
public class MpackResourceProvider extends org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider {
    public static final java.lang.String RESPONSE_KEY = "MpackInfo";

    public static final java.lang.String ALL_PROPERTIES = (org.apache.ambari.server.controller.internal.MpackResourceProvider.RESPONSE_KEY + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + "*";

    public static final java.lang.String MPACK_RESOURCE_ID = (org.apache.ambari.server.controller.internal.MpackResourceProvider.RESPONSE_KEY + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + "id";

    public static final java.lang.String REGISTRY_ID = (org.apache.ambari.server.controller.internal.MpackResourceProvider.RESPONSE_KEY + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + "registry_id";

    public static final java.lang.String MPACK_ID = (org.apache.ambari.server.controller.internal.MpackResourceProvider.RESPONSE_KEY + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + "mpack_id";

    public static final java.lang.String MPACK_NAME = (org.apache.ambari.server.controller.internal.MpackResourceProvider.RESPONSE_KEY + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + "mpack_name";

    public static final java.lang.String MPACK_VERSION = (org.apache.ambari.server.controller.internal.MpackResourceProvider.RESPONSE_KEY + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + "mpack_version";

    public static final java.lang.String MPACK_DESCRIPTION = (org.apache.ambari.server.controller.internal.MpackResourceProvider.RESPONSE_KEY + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + "mpack_description";

    public static final java.lang.String MPACK_DISPLAY_NAME = (org.apache.ambari.server.controller.internal.MpackResourceProvider.RESPONSE_KEY + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + "mpack_display_name";

    public static final java.lang.String MPACK_URI = (org.apache.ambari.server.controller.internal.MpackResourceProvider.RESPONSE_KEY + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + "mpack_uri";

    public static final java.lang.String MODULES = (org.apache.ambari.server.controller.internal.MpackResourceProvider.RESPONSE_KEY + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + "modules";

    public static final java.lang.String STACK_NAME_PROPERTY_ID = (org.apache.ambari.server.controller.internal.MpackResourceProvider.RESPONSE_KEY + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + "stack_name";

    public static final java.lang.String STACK_VERSION_PROPERTY_ID = (org.apache.ambari.server.controller.internal.MpackResourceProvider.RESPONSE_KEY + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + "stack_version";

    private static java.util.Set<java.lang.String> pkPropertyIds = new java.util.HashSet<>(java.util.Arrays.asList(org.apache.ambari.server.controller.internal.MpackResourceProvider.MPACK_RESOURCE_ID, org.apache.ambari.server.controller.internal.MpackResourceProvider.STACK_NAME_PROPERTY_ID, org.apache.ambari.server.controller.internal.MpackResourceProvider.STACK_VERSION_PROPERTY_ID));

    private static final java.util.Set<java.lang.String> PROPERTY_IDS = new java.util.HashSet<>();

    private static final java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> KEY_PROPERTY_IDS = new java.util.HashMap<>();

    @com.google.inject.Inject
    protected static org.apache.ambari.server.orm.dao.MpackDAO mpackDAO;

    @com.google.inject.Inject
    protected static org.apache.ambari.server.orm.dao.StackDAO stackDAO;

    @com.google.inject.Inject
    protected static org.apache.ambari.server.orm.dao.RepositoryVersionDAO repositoryVersionDAO;

    static {
        PROPERTY_IDS.add(MPACK_RESOURCE_ID);
        PROPERTY_IDS.add(REGISTRY_ID);
        PROPERTY_IDS.add(MPACK_ID);
        PROPERTY_IDS.add(MPACK_NAME);
        PROPERTY_IDS.add(MPACK_VERSION);
        PROPERTY_IDS.add(MPACK_URI);
        PROPERTY_IDS.add(MPACK_DESCRIPTION);
        PROPERTY_IDS.add(MODULES);
        PROPERTY_IDS.add(STACK_NAME_PROPERTY_ID);
        PROPERTY_IDS.add(STACK_VERSION_PROPERTY_ID);
        PROPERTY_IDS.add(MPACK_DISPLAY_NAME);
        KEY_PROPERTY_IDS.put(org.apache.ambari.server.controller.spi.Resource.Type.Mpack, MPACK_RESOURCE_ID);
        KEY_PROPERTY_IDS.put(org.apache.ambari.server.controller.spi.Resource.Type.Stack, STACK_NAME_PROPERTY_ID);
        KEY_PROPERTY_IDS.put(org.apache.ambari.server.controller.spi.Resource.Type.StackVersion, STACK_VERSION_PROPERTY_ID);
    }

    MpackResourceProvider(org.apache.ambari.server.controller.AmbariManagementController controller) {
        super(org.apache.ambari.server.controller.spi.Resource.Type.Mpack, org.apache.ambari.server.controller.internal.MpackResourceProvider.PROPERTY_IDS, org.apache.ambari.server.controller.internal.MpackResourceProvider.KEY_PROPERTY_IDS, controller);
    }

    @java.lang.Override
    protected java.util.Set<java.lang.String> getPKPropertyIds() {
        return org.apache.ambari.server.controller.internal.MpackResourceProvider.pkPropertyIds;
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus createResources(final org.apache.ambari.server.controller.spi.Request request) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.ResourceAlreadyExistsException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException, java.lang.IllegalArgumentException {
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> associatedResources = new java.util.HashSet<>();
        try {
            org.apache.ambari.server.controller.MpackRequest mpackRequest = getRequest(request);
            if (mpackRequest == null) {
                throw new org.apache.ambari.server.api.services.parsers.BodyParseException((((("Please provide " + org.apache.ambari.server.controller.internal.MpackResourceProvider.MPACK_NAME) + " ,") + org.apache.ambari.server.controller.internal.MpackResourceProvider.MPACK_VERSION) + " ,") + org.apache.ambari.server.controller.internal.MpackResourceProvider.MPACK_URI);
            }
            validateCreateRequest(mpackRequest);
            org.apache.ambari.server.controller.MpackResponse response = getManagementController().registerMpack(mpackRequest);
            if (response != null) {
                notifyCreate(org.apache.ambari.server.controller.spi.Resource.Type.Mpack, request);
                org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Mpack);
                resource.setProperty(org.apache.ambari.server.controller.internal.MpackResourceProvider.MPACK_RESOURCE_ID, response.getId());
                resource.setProperty(org.apache.ambari.server.controller.internal.MpackResourceProvider.MPACK_ID, response.getMpackId());
                resource.setProperty(org.apache.ambari.server.controller.internal.MpackResourceProvider.MPACK_NAME, response.getMpackName());
                resource.setProperty(org.apache.ambari.server.controller.internal.MpackResourceProvider.MPACK_VERSION, response.getMpackVersion());
                resource.setProperty(org.apache.ambari.server.controller.internal.MpackResourceProvider.MPACK_URI, response.getMpackUri());
                resource.setProperty(org.apache.ambari.server.controller.internal.MpackResourceProvider.MPACK_DESCRIPTION, response.getDescription());
                resource.setProperty(org.apache.ambari.server.controller.internal.MpackResourceProvider.REGISTRY_ID, response.getRegistryId());
                resource.setProperty(org.apache.ambari.server.controller.internal.MpackResourceProvider.MPACK_DISPLAY_NAME, response.getDisplayName());
                associatedResources.add(resource);
                return getRequestStatus(null, associatedResources);
            }
        } catch (java.io.IOException e) {
            e.printStackTrace();
        } catch (org.apache.ambari.server.api.services.parsers.BodyParseException e1) {
            e1.printStackTrace();
        }
        return null;
    }

    private void validateCreateRequest(org.apache.ambari.server.controller.MpackRequest mpackRequest) {
        final java.lang.String mpackName = mpackRequest.getMpackName();
        final java.lang.String mpackUrl = mpackRequest.getMpackUri();
        final java.lang.Long registryId = mpackRequest.getRegistryId();
        final java.lang.String mpackVersion = mpackRequest.getMpackVersion();
        if (registryId == null) {
            org.apache.commons.lang.Validate.isTrue(mpackUrl != null);
            org.apache.ambari.server.controller.internal.AbstractResourceProvider.LOG.info(("Received a createMpack request" + ", mpackUrl=") + mpackUrl);
        } else {
            org.apache.commons.lang.Validate.notNull(mpackName, "MpackName should not be null");
            org.apache.commons.lang.Validate.notNull(mpackVersion, "MpackVersion should not be null");
            org.apache.ambari.server.controller.internal.AbstractResourceProvider.LOG.info(((((("Received a createMpack request" + ", mpackName=") + mpackName) + ", mpackVersion=") + mpackVersion) + ", registryId=") + registryId);
        }
        try {
            java.net.URI uri = new java.net.URI(mpackUrl);
            java.net.URL url = uri.toURL();
        } catch (java.lang.Exception e) {
            org.apache.commons.lang.Validate.isTrue(e == null, e.getMessage() + " is an invalid mpack uri. Please check the download link for the mpack again.");
        }
    }

    public org.apache.ambari.server.controller.MpackRequest getRequest(org.apache.ambari.server.controller.spi.Request request) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.controller.MpackRequest mpackRequest = new org.apache.ambari.server.controller.MpackRequest();
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> properties = request.getProperties();
        for (java.util.Map propertyMap : properties) {
            if ((!propertyMap.containsKey(org.apache.ambari.server.controller.internal.MpackResourceProvider.MPACK_URI)) && (!propertyMap.containsKey(org.apache.ambari.server.controller.internal.MpackResourceProvider.REGISTRY_ID))) {
                return null;
            } else if (!propertyMap.containsKey(org.apache.ambari.server.controller.internal.MpackResourceProvider.MPACK_URI)) {
                mpackRequest.setRegistryId(java.lang.Long.valueOf(((java.lang.String) (propertyMap.get(org.apache.ambari.server.controller.internal.MpackResourceProvider.REGISTRY_ID)))));
                mpackRequest.setMpackName(((java.lang.String) (propertyMap.get(org.apache.ambari.server.controller.internal.MpackResourceProvider.MPACK_NAME))));
                mpackRequest.setMpackVersion(((java.lang.String) (propertyMap.get(org.apache.ambari.server.controller.internal.MpackResourceProvider.MPACK_VERSION))));
            } else {
                mpackRequest.setMpackUri(((java.lang.String) (propertyMap.get(org.apache.ambari.server.controller.internal.MpackResourceProvider.MPACK_URI))));
            }
        }
        return mpackRequest;
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.controller.spi.Resource> getResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> results = new java.util.LinkedHashSet<>();
        java.lang.Long mpackId = null;
        if (predicate == null) {
            java.util.Set<org.apache.ambari.server.controller.MpackResponse> responses = getManagementController().getMpacks();
            if (null == responses) {
                responses = java.util.Collections.emptySet();
            }
            for (org.apache.ambari.server.controller.MpackResponse response : responses) {
                org.apache.ambari.server.controller.spi.Resource resource = setResources(response);
                java.util.Set<java.lang.String> requestIds = getRequestPropertyIds(request, predicate);
                if (requestIds.contains(org.apache.ambari.server.controller.internal.MpackResourceProvider.MODULES)) {
                    java.util.List<org.apache.ambari.server.state.Module> modules = getManagementController().getModules(response.getId());
                    resource.setProperty(org.apache.ambari.server.controller.internal.MpackResourceProvider.MODULES, modules);
                }
                results.add(resource);
            }
        } else {
            java.util.Map<java.lang.String, java.lang.Object> propertyMap = new java.util.HashMap<>(org.apache.ambari.server.controller.utilities.PredicateHelper.getProperties(predicate));
            if (propertyMap.containsKey(org.apache.ambari.server.controller.internal.MpackResourceProvider.MPACK_RESOURCE_ID)) {
                java.lang.Object objMpackId = propertyMap.get(org.apache.ambari.server.controller.internal.MpackResourceProvider.MPACK_RESOURCE_ID);
                if (objMpackId != null) {
                    mpackId = java.lang.Long.valueOf(((java.lang.String) (objMpackId)));
                }
                org.apache.ambari.server.controller.MpackResponse response = getManagementController().getMpack(mpackId);
                if (null != response) {
                    org.apache.ambari.server.controller.spi.Resource resource = setResources(response);
                    java.util.List<org.apache.ambari.server.state.Module> modules = getManagementController().getModules(response.getId());
                    resource.setProperty(org.apache.ambari.server.controller.internal.MpackResourceProvider.MODULES, modules);
                    results.add(resource);
                }
            } else if (propertyMap.containsKey(org.apache.ambari.server.controller.internal.MpackResourceProvider.STACK_NAME_PROPERTY_ID) && propertyMap.containsKey(org.apache.ambari.server.controller.internal.MpackResourceProvider.STACK_VERSION_PROPERTY_ID)) {
                java.lang.String stackName = ((java.lang.String) (propertyMap.get(org.apache.ambari.server.controller.internal.MpackResourceProvider.STACK_NAME_PROPERTY_ID)));
                java.lang.String stackVersion = ((java.lang.String) (propertyMap.get(org.apache.ambari.server.controller.internal.MpackResourceProvider.STACK_VERSION_PROPERTY_ID)));
                org.apache.ambari.server.orm.entities.StackEntity stackEntity = org.apache.ambari.server.controller.internal.MpackResourceProvider.stackDAO.find(stackName, stackVersion);
                mpackId = stackEntity.getMpackId();
                org.apache.ambari.server.controller.MpackResponse response = getManagementController().getMpack(mpackId);
                if (null != response) {
                    org.apache.ambari.server.controller.spi.Resource resource = setResources(response);
                    resource.setProperty(org.apache.ambari.server.controller.internal.MpackResourceProvider.STACK_NAME_PROPERTY_ID, stackName);
                    resource.setProperty(org.apache.ambari.server.controller.internal.MpackResourceProvider.STACK_VERSION_PROPERTY_ID, stackVersion);
                    results.add(resource);
                }
            }
            if (null == mpackId) {
                throw new java.lang.IllegalArgumentException("Either the management pack ID or the stack name and version are required when searching");
            }
            if (results.isEmpty()) {
                throw new org.apache.ambari.server.controller.spi.NoSuchResourceException("The requested resource doesn't exist: " + predicate);
            }
        }
        return results;
    }

    private org.apache.ambari.server.controller.spi.Resource setResources(org.apache.ambari.server.controller.MpackResponse response) {
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Mpack);
        resource.setProperty(org.apache.ambari.server.controller.internal.MpackResourceProvider.MPACK_RESOURCE_ID, response.getId());
        resource.setProperty(org.apache.ambari.server.controller.internal.MpackResourceProvider.MPACK_ID, response.getMpackId());
        resource.setProperty(org.apache.ambari.server.controller.internal.MpackResourceProvider.MPACK_NAME, response.getMpackName());
        resource.setProperty(org.apache.ambari.server.controller.internal.MpackResourceProvider.MPACK_VERSION, response.getMpackVersion());
        resource.setProperty(org.apache.ambari.server.controller.internal.MpackResourceProvider.MPACK_URI, response.getMpackUri());
        resource.setProperty(org.apache.ambari.server.controller.internal.MpackResourceProvider.MPACK_DESCRIPTION, response.getDescription());
        resource.setProperty(org.apache.ambari.server.controller.internal.MpackResourceProvider.REGISTRY_ID, response.getRegistryId());
        resource.setProperty(org.apache.ambari.server.controller.internal.MpackResourceProvider.MPACK_DISPLAY_NAME, response.getDisplayName());
        return resource;
    }

    @java.lang.Override
    protected org.apache.ambari.server.controller.spi.RequestStatus deleteResourcesAuthorized(final org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        final java.lang.Long mpackId;
        java.util.Map<java.lang.String, java.lang.Object> propertyMap = new java.util.HashMap<>(org.apache.ambari.server.controller.utilities.PredicateHelper.getProperties(predicate));
        org.apache.ambari.server.controller.internal.DeleteStatusMetaData deleteStatusMetaData = null;
        if (getManagementController().getClusters().getClusters().size() > 0) {
            throw new org.apache.ambari.server.controller.spi.SystemException("Delete request cannot be completed since there is a cluster deployed");
        } else {
            if (propertyMap.containsKey(org.apache.ambari.server.controller.internal.MpackResourceProvider.MPACK_RESOURCE_ID)) {
                java.lang.Object objMpackId = propertyMap.get(org.apache.ambari.server.controller.internal.MpackResourceProvider.MPACK_RESOURCE_ID);
                if (objMpackId != null) {
                    mpackId = java.lang.Long.valueOf(((java.lang.String) (objMpackId)));
                    org.apache.ambari.server.controller.internal.AbstractResourceProvider.LOG.info("Deleting Mpack, id = " + mpackId.toString());
                    org.apache.ambari.server.orm.entities.MpackEntity mpackEntity = org.apache.ambari.server.controller.internal.MpackResourceProvider.mpackDAO.findById(mpackId);
                    org.apache.ambari.server.orm.entities.StackEntity stackEntity = org.apache.ambari.server.controller.internal.MpackResourceProvider.stackDAO.findByMpack(mpackId);
                    try {
                        getManagementController().removeMpack(mpackEntity, stackEntity);
                        if (mpackEntity != null) {
                            deleteStatusMetaData = modifyResources(new org.apache.ambari.server.controller.internal.AbstractResourceProvider.Command<org.apache.ambari.server.controller.internal.DeleteStatusMetaData>() {
                                @java.lang.Override
                                public org.apache.ambari.server.controller.internal.DeleteStatusMetaData invoke() throws org.apache.ambari.server.AmbariException {
                                    if (stackEntity != null) {
                                        org.apache.ambari.server.controller.internal.MpackResourceProvider.repositoryVersionDAO.removeByStack(new org.apache.ambari.server.state.StackId((stackEntity.getStackName() + "-") + stackEntity.getStackVersion()));
                                        org.apache.ambari.server.controller.internal.MpackResourceProvider.stackDAO.removeByMpack(mpackId);
                                        notifyDelete(org.apache.ambari.server.controller.spi.Resource.Type.Stack, predicate);
                                    }
                                    org.apache.ambari.server.controller.internal.MpackResourceProvider.mpackDAO.removeById(mpackId);
                                    return new org.apache.ambari.server.controller.internal.DeleteStatusMetaData();
                                }
                            });
                            notifyDelete(org.apache.ambari.server.controller.spi.Resource.Type.Mpack, predicate);
                            deleteStatusMetaData.addDeletedKey(mpackId.toString());
                        } else {
                            throw new org.apache.ambari.server.controller.spi.NoSuchResourceException("The requested resource doesn't exist: " + predicate);
                        }
                    } catch (java.io.IOException e) {
                        throw new org.apache.ambari.server.controller.spi.SystemException("There is an issue with the Files");
                    }
                }
            } else {
                throw new org.apache.ambari.server.controller.spi.UnsupportedPropertyException(org.apache.ambari.server.controller.spi.Resource.Type.Mpack, null);
            }
            return getRequestStatus(null, null, deleteStatusMetaData);
        }
    }
}