package org.apache.ambari.server.controller.internal;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.http.HttpStatus;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.JsonNodeFactory;
import org.codehaus.jackson.node.ObjectNode;
@org.apache.ambari.server.StaticallyInject
public class VersionDefinitionResourceProvider extends org.apache.ambari.server.controller.internal.AbstractAuthorizedResourceProvider {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.class);

    public static final java.lang.String VERSION_DEF = "VersionDefinition";

    public static final java.lang.String VERSION_DEF_BASE64_PROPERTY = "version_base64";

    public static final java.lang.String VERSION_DEF_STACK_NAME = "VersionDefinition/stack_name";

    public static final java.lang.String VERSION_DEF_STACK_VERSION = "VersionDefinition/stack_version";

    public static final java.lang.String VERSION_DEF_ID = "VersionDefinition/id";

    protected static final java.lang.String VERSION_DEF_TYPE_PROPERTY_ID = "VersionDefinition/type";

    protected static final java.lang.String VERSION_DEF_DEFINITION_URL = "VersionDefinition/version_url";

    public static final java.lang.String VERSION_DEF_AVAILABLE_DEFINITION = "VersionDefinition/available";

    protected static final java.lang.String VERSION_DEF_DEFINITION_BASE64 = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId(org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.VERSION_DEF, org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.VERSION_DEF_BASE64_PROPERTY);

    protected static final java.lang.String VERSION_DEF_FULL_VERSION = "VersionDefinition/repository_version";

    protected static final java.lang.String VERSION_DEF_RELEASE_VERSION = "VersionDefinition/release/version";

    protected static final java.lang.String VERSION_DEF_RELEASE_BUILD = "VersionDefinition/release/build";

    protected static final java.lang.String VERSION_DEF_RELEASE_NOTES = "VersionDefinition/release/notes";

    protected static final java.lang.String VERSION_DEF_RELEASE_COMPATIBLE_WITH = "VersionDefinition/release/compatible_with";

    protected static final java.lang.String VERSION_DEF_AVAILABLE_SERVICES = "VersionDefinition/services";

    protected static final java.lang.String VERSION_DEF_STACK_SERVICES = "VersionDefinition/stack_services";

    protected static final java.lang.String VERSION_DEF_STACK_DEFAULT = "VersionDefinition/stack_default";

    protected static final java.lang.String VERSION_DEF_STACK_REPO_UPDATE_LINK_EXISTS = "VersionDefinition/stack_repo_update_link_exists";

    protected static final java.lang.String VERSION_DEF_DISPLAY_NAME = "VersionDefinition/display_name";

    protected static final java.lang.String VERSION_DEF_VALIDATION = "VersionDefinition/validation";

    protected static final java.lang.String SHOW_AVAILABLE = "VersionDefinition/show_available";

    protected static final java.lang.String VERSION_DEF_MIN_JDK = "VersionDefinition/min_jdk";

    protected static final java.lang.String VERSION_DEF_MAX_JDK = "VersionDefinition/max_jdk";

    public static final java.lang.String DIRECTIVE_SKIP_URL_CHECK = "skip_url_check";

    public static final java.lang.String SUBRESOURCE_OPERATING_SYSTEMS_PROPERTY_ID = new org.apache.ambari.server.api.resources.OperatingSystemResourceDefinition().getPluralName();

    @com.google.inject.Inject
    private static org.apache.ambari.server.orm.dao.RepositoryVersionDAO s_repoVersionDAO;

    @com.google.inject.Inject
    private static com.google.inject.Provider<org.apache.ambari.server.api.services.AmbariMetaInfo> s_metaInfo;

    @com.google.inject.Inject
    private static com.google.inject.Provider<org.apache.ambari.server.stack.upgrade.RepositoryVersionHelper> s_repoVersionHelper;

    @com.google.inject.Inject
    private static org.apache.ambari.server.orm.dao.StackDAO s_stackDAO;

    @com.google.inject.Inject
    private static org.apache.ambari.server.configuration.Configuration s_configuration;

    private static final java.util.Set<java.lang.String> PK_PROPERTY_IDS = com.google.common.collect.Sets.newHashSet(org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.VERSION_DEF_ID, org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.VERSION_DEF_STACK_NAME, org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.VERSION_DEF_STACK_VERSION, org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.VERSION_DEF_FULL_VERSION);

    private static final java.util.Set<java.lang.String> PROPERTY_IDS = com.google.common.collect.Sets.newHashSet(org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.VERSION_DEF_ID, org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.VERSION_DEF_TYPE_PROPERTY_ID, org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.VERSION_DEF_DEFINITION_URL, org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.VERSION_DEF_DEFINITION_BASE64, org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.VERSION_DEF_AVAILABLE_DEFINITION, org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.VERSION_DEF_STACK_NAME, org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.VERSION_DEF_STACK_VERSION, org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.VERSION_DEF_FULL_VERSION, org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.VERSION_DEF_RELEASE_NOTES, org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.VERSION_DEF_RELEASE_COMPATIBLE_WITH, org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.VERSION_DEF_RELEASE_VERSION, org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.VERSION_DEF_RELEASE_BUILD, org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.VERSION_DEF_AVAILABLE_SERVICES, org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.VERSION_DEF_STACK_SERVICES, org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.VERSION_DEF_STACK_DEFAULT, org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.VERSION_DEF_STACK_REPO_UPDATE_LINK_EXISTS, org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.VERSION_DEF_DISPLAY_NAME, org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.VERSION_DEF_VALIDATION, org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.VERSION_DEF_MIN_JDK, org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.VERSION_DEF_MAX_JDK, org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.SUBRESOURCE_OPERATING_SYSTEMS_PROPERTY_ID, org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.SHOW_AVAILABLE);

    private static final java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> KEY_PROPERTY_IDS = java.util.Collections.singletonMap(org.apache.ambari.server.controller.spi.Resource.Type.VersionDefinition, org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.VERSION_DEF_ID);

    VersionDefinitionResourceProvider() {
        super(org.apache.ambari.server.controller.spi.Resource.Type.VersionDefinition, org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.PROPERTY_IDS, org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.KEY_PROPERTY_IDS);
        setRequiredCreateAuthorizations(java.util.EnumSet.of(org.apache.ambari.server.security.authorization.RoleAuthorization.AMBARI_MANAGE_STACK_VERSIONS));
        setRequiredGetAuthorizations(java.util.EnumSet.of(org.apache.ambari.server.security.authorization.RoleAuthorization.AMBARI_MANAGE_STACK_VERSIONS));
    }

    @java.lang.Override
    protected org.apache.ambari.server.controller.spi.RequestStatus createResourcesAuthorized(final org.apache.ambari.server.controller.spi.Request request) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.ResourceAlreadyExistsException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> requestProperties = request.getProperties();
        if (requestProperties.size() > 1) {
            throw new java.lang.IllegalArgumentException("Cannot process more than one file per request");
        }
        final java.util.Map<java.lang.String, java.lang.Object> properties = requestProperties.iterator().next();
        if (((!properties.containsKey(org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.VERSION_DEF_DEFINITION_URL)) && (!properties.containsKey(org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.VERSION_DEF_DEFINITION_BASE64))) && (!properties.containsKey(org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.VERSION_DEF_AVAILABLE_DEFINITION))) {
            throw new java.lang.IllegalArgumentException(java.lang.String.format("Creation method is not known.  %s or %s is required or upload the file directly", org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.VERSION_DEF_DEFINITION_URL, org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.VERSION_DEF_AVAILABLE_DEFINITION));
        }
        if (properties.containsKey(org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.VERSION_DEF_DEFINITION_URL) && properties.containsKey(org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.VERSION_DEF_DEFINITION_BASE64)) {
            throw new java.lang.IllegalArgumentException(java.lang.String.format("Specify ONLY the url with %s or upload the file directly", org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.VERSION_DEF_DEFINITION_URL));
        }
        final java.lang.String definitionUrl = ((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.VERSION_DEF_DEFINITION_URL)));
        final java.lang.String definitionBase64 = ((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.VERSION_DEF_DEFINITION_BASE64)));
        final java.lang.String definitionName = ((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.VERSION_DEF_AVAILABLE_DEFINITION)));
        final java.util.Set<java.lang.String> validations = new java.util.HashSet<>();
        final boolean dryRun = request.isDryRunRequest();
        final boolean skipUrlCheck;
        if (null != request.getRequestInfoProperties()) {
            skipUrlCheck = org.apache.commons.lang.BooleanUtils.toBoolean(request.getRequestInfoProperties().get(org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.DIRECTIVE_SKIP_URL_CHECK));
        } else {
            skipUrlCheck = false;
        }
        org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.XmlHolder xmlHolder = createResources(new org.apache.ambari.server.controller.internal.AbstractResourceProvider.Command<org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.XmlHolder>() {
            @java.lang.Override
            public org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.XmlHolder invoke() throws org.apache.ambari.server.AmbariException {
                org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.XmlHolder holder = null;
                if (null != definitionUrl) {
                    holder = loadXml(definitionUrl);
                } else if (null != definitionBase64) {
                    holder = loadXml(org.apache.commons.codec.binary.Base64.decodeBase64(definitionBase64));
                } else if (null != definitionName) {
                    org.apache.ambari.server.state.repository.VersionDefinitionXml xml = org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.s_metaInfo.get().getVersionDefinition(definitionName);
                    if (null == xml) {
                        throw new org.apache.ambari.server.AmbariException(java.lang.String.format("Version %s not found", definitionName));
                    }
                    holder = new org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.XmlHolder();
                    holder.xml = xml;
                    try {
                        holder.xmlString = xml.toXml();
                    } catch (java.lang.Exception e) {
                        throw new org.apache.ambari.server.AmbariException(java.lang.String.format("The available repository %s does not serialize", definitionName), e);
                    }
                } else {
                    throw new org.apache.ambari.server.AmbariException("Cannot determine creation method");
                }
                toRepositoryVersionEntity(holder);
                try {
                    org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.validateRepositoryVersion(org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.s_repoVersionDAO, org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.s_metaInfo.get(), holder.entity, skipUrlCheck);
                } catch (org.apache.ambari.server.AmbariException e) {
                    if (dryRun) {
                        validations.add(e.getMessage());
                    } else {
                        throw e;
                    }
                }
                checkForParent(holder);
                return holder;
            }
        });
        if (org.apache.commons.lang.StringUtils.isNotBlank(org.apache.commons.lang.ObjectUtils.toString(properties.get(org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.VERSION_DEF_DISPLAY_NAME)))) {
            xmlHolder.xml.release.display = properties.get(org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.VERSION_DEF_DISPLAY_NAME).toString();
            xmlHolder.entity.setDisplayName(properties.get(org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.VERSION_DEF_DISPLAY_NAME).toString());
            xmlHolder.entity.setVersion(properties.get(org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.VERSION_DEF_DISPLAY_NAME).toString());
        }
        if (org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.s_repoVersionDAO.findByDisplayName(xmlHolder.entity.getDisplayName()) != null) {
            java.lang.String err = java.lang.String.format("Repository version with name %s already exists.", xmlHolder.entity.getDisplayName());
            if (dryRun) {
                validations.add(err);
            } else {
                throw new org.apache.ambari.server.controller.spi.ResourceAlreadyExistsException(err);
            }
        }
        if (org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.s_repoVersionDAO.findByStackAndVersion(xmlHolder.entity.getStack(), xmlHolder.entity.getVersion()) != null) {
            java.lang.String err = java.lang.String.format("Repository version for stack %s and version %s already exists.", xmlHolder.entity.getStackId(), xmlHolder.entity.getVersion());
            if (dryRun) {
                validations.add(err);
            } else {
                throw new org.apache.ambari.server.controller.spi.ResourceAlreadyExistsException(err);
            }
        }
        final org.apache.ambari.server.controller.spi.Resource res;
        if (dryRun) {
            java.util.Set<java.lang.String> ids = com.google.common.collect.Sets.newHashSet(org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.VERSION_DEF_TYPE_PROPERTY_ID, org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.VERSION_DEF_FULL_VERSION, org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.VERSION_DEF_RELEASE_BUILD, org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.VERSION_DEF_RELEASE_COMPATIBLE_WITH, org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.VERSION_DEF_RELEASE_NOTES, org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.VERSION_DEF_RELEASE_VERSION, org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.VERSION_DEF_DISPLAY_NAME, org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.VERSION_DEF_AVAILABLE_SERVICES, org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.VERSION_DEF_VALIDATION, org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.VERSION_DEF_MIN_JDK, org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.VERSION_DEF_MAX_JDK, org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.VERSION_DEF_STACK_SERVICES);
            res = toResource(null, xmlHolder.xml, ids, validations);
            if (null != definitionName) {
                res.setProperty(org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.SHOW_AVAILABLE, true);
            }
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(res, org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.VERSION_DEF_FULL_VERSION, xmlHolder.entity.getVersion(), ids);
            addSubresources(res, xmlHolder.entity);
        } else {
            org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.s_repoVersionDAO.create(xmlHolder.entity);
            res = toResource(xmlHolder.entity, java.util.Collections.emptySet());
            notifyCreate(org.apache.ambari.server.controller.spi.Resource.Type.VersionDefinition, request);
        }
        org.apache.ambari.server.controller.internal.RequestStatusImpl status = new org.apache.ambari.server.controller.internal.RequestStatusImpl(null, java.util.Collections.singleton(res));
        return status;
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.controller.spi.Resource> getResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> results = new java.util.HashSet<>();
        java.util.Set<java.lang.String> requestPropertyIds = getRequestPropertyIds(request, predicate);
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> propertyMaps = getPropertyMaps(predicate);
        if (propertyMaps.isEmpty()) {
            java.util.List<org.apache.ambari.server.orm.entities.RepositoryVersionEntity> versions = org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.s_repoVersionDAO.findRepositoriesWithVersionDefinitions();
            for (org.apache.ambari.server.orm.entities.RepositoryVersionEntity entity : versions) {
                results.add(toResource(entity, requestPropertyIds));
            }
        } else {
            for (java.util.Map<java.lang.String, java.lang.Object> propertyMap : propertyMaps) {
                if (propertyMap.containsKey(org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.SHOW_AVAILABLE) && java.lang.Boolean.parseBoolean(propertyMap.get(org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.SHOW_AVAILABLE).toString())) {
                    for (java.util.Map.Entry<java.lang.String, org.apache.ambari.server.state.repository.VersionDefinitionXml> entry : org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.s_metaInfo.get().getVersionDefinitions().entrySet()) {
                        org.apache.ambari.server.controller.spi.Resource res = toResource(entry.getKey(), entry.getValue(), requestPropertyIds, null);
                        res.setProperty(org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.SHOW_AVAILABLE, true);
                        results.add(res);
                    }
                } else {
                    java.lang.String id = ((java.lang.String) (propertyMap.get(org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.VERSION_DEF_ID)));
                    if (null != id) {
                        if (org.apache.commons.lang.math.NumberUtils.isDigits(id)) {
                            org.apache.ambari.server.orm.entities.RepositoryVersionEntity entity = org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.s_repoVersionDAO.findByPK(java.lang.Long.parseLong(id));
                            if (null != entity) {
                                results.add(toResource(entity, requestPropertyIds));
                            }
                        } else {
                            org.apache.ambari.server.state.repository.VersionDefinitionXml xml = org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.s_metaInfo.get().getVersionDefinition(id);
                            if (null == xml) {
                                throw new org.apache.ambari.server.controller.spi.NoSuchResourceException(java.lang.String.format("Could not find version %s", id));
                            }
                            org.apache.ambari.server.controller.spi.Resource res = toResource(id, xml, requestPropertyIds, null);
                            res.setProperty(org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.SHOW_AVAILABLE, true);
                            results.add(res);
                        }
                    } else {
                        java.util.List<org.apache.ambari.server.orm.entities.RepositoryVersionEntity> versions = org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.s_repoVersionDAO.findRepositoriesWithVersionDefinitions();
                        for (org.apache.ambari.server.orm.entities.RepositoryVersionEntity entity : versions) {
                            results.add(toResource(entity, requestPropertyIds));
                        }
                    }
                }
            }
        }
        return results;
    }

    @java.lang.Override
    protected org.apache.ambari.server.controller.spi.RequestStatus updateResourcesAuthorized(final org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        throw new org.apache.ambari.server.controller.spi.SystemException("Cannot update Version Definitions");
    }

    @java.lang.Override
    protected org.apache.ambari.server.controller.spi.RequestStatus deleteResourcesAuthorized(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        throw new org.apache.ambari.server.controller.spi.SystemException("Cannot delete Version Definitions");
    }

    private void checkForParent(org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.XmlHolder holder) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity entity = holder.entity;
        if (entity.getType() == org.apache.ambari.spi.RepositoryType.STANDARD) {
            return;
        }
        java.util.List<org.apache.ambari.server.orm.entities.RepositoryVersionEntity> entities = org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.s_repoVersionDAO.findByStackAndType(entity.getStackId(), org.apache.ambari.spi.RepositoryType.STANDARD);
        if (entities.isEmpty()) {
            throw new java.lang.IllegalArgumentException(java.lang.String.format("Patch %s was uploaded, but there are no repositories for %s", entity.getVersion(), entity.getStackId().toString()));
        }
        java.util.List<org.apache.ambari.server.orm.entities.RepositoryVersionEntity> matching = new java.util.ArrayList<>();
        boolean emptyCompatible = org.apache.commons.lang.StringUtils.isBlank(holder.xml.release.compatibleWith);
        for (org.apache.ambari.server.orm.entities.RepositoryVersionEntity version : entities) {
            java.lang.String baseVersion = version.getVersion();
            if (baseVersion.lastIndexOf('-') > (-1)) {
                baseVersion = baseVersion.substring(0, baseVersion.lastIndexOf('-'));
            }
            if (emptyCompatible) {
                if (baseVersion.equals(holder.xml.release.version)) {
                    matching.add(version);
                }
            } else if (baseVersion.matches(holder.xml.release.compatibleWith)) {
                matching.add(version);
            }
        }
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity parent = null;
        if (matching.isEmpty()) {
            java.lang.String format = "No versions matched pattern %s";
            throw new java.lang.IllegalArgumentException(java.lang.String.format(format, emptyCompatible ? holder.xml.release.version : holder.xml.release.compatibleWith));
        } else if (matching.size() > 1) {
            com.google.common.base.Function<org.apache.ambari.server.orm.entities.RepositoryVersionEntity, java.lang.String> function = new com.google.common.base.Function<org.apache.ambari.server.orm.entities.RepositoryVersionEntity, java.lang.String>() {
                @java.lang.Override
                public java.lang.String apply(org.apache.ambari.server.orm.entities.RepositoryVersionEntity input) {
                    return input.getVersion();
                }
            };
            java.util.Collection<java.lang.String> versions = com.google.common.collect.Collections2.transform(matching, function);
            java.util.List<org.apache.ambari.server.orm.entities.RepositoryVersionEntity> used = org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.s_repoVersionDAO.findByServiceDesiredVersion(matching);
            if (used.isEmpty()) {
                throw new java.lang.IllegalArgumentException(java.lang.String.format("Could not determine which version " + "to associate patch %s. Remove one of %s and try again.", entity.getVersion(), org.apache.commons.lang.StringUtils.join(versions, ", ")));
            } else if (used.size() > 1) {
                java.util.Collection<java.lang.String> usedVersions = com.google.common.collect.Collections2.transform(used, function);
                throw new java.lang.IllegalArgumentException(java.lang.String.format("Patch %s was found to match more " + "than one repository in use: %s. Move all services to a common version and try again.", entity.getVersion(), org.apache.commons.lang.StringUtils.join(usedVersions, ", ")));
            } else {
                parent = used.get(0);
                org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.LOG.warn("Patch {} was found to match more than one repository in {}. " + "Repository {} is in use and will be the parent.", entity.getVersion(), org.apache.commons.lang.StringUtils.join(versions, ", "), parent.getVersion());
            }
        } else {
            parent = matching.get(0);
        }
        if (null == parent) {
            throw new java.lang.IllegalArgumentException(java.lang.String.format("Could not find any parent repository for %s.", entity.getVersion()));
        }
        entity.setParent(parent);
    }

    @java.lang.Override
    protected java.util.Set<java.lang.String> getPKPropertyIds() {
        return org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.PK_PROPERTY_IDS;
    }

    @java.lang.Override
    protected org.apache.ambari.server.security.authorization.ResourceType getResourceType(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) {
        return org.apache.ambari.server.security.authorization.ResourceType.AMBARI;
    }

    private org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.XmlHolder loadXml(byte[] decoded) {
        org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.XmlHolder holder = new org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.XmlHolder();
        try {
            holder.xmlString = new java.lang.String(decoded, "UTF-8");
        } catch (java.io.UnsupportedEncodingException e) {
            holder.xmlString = new java.lang.String(decoded);
        }
        try {
            holder.xml = org.apache.ambari.server.state.repository.VersionDefinitionXml.load(holder.xmlString);
        } catch (java.lang.Exception e) {
            throw new java.lang.IllegalArgumentException(e);
        }
        return holder;
    }

    private org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.XmlHolder loadXml(java.lang.String definitionUrl) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.XmlHolder holder = new org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.XmlHolder();
        holder.url = definitionUrl;
        int connectTimeout = org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.s_configuration.getVersionDefinitionConnectTimeout();
        int readTimeout = org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.s_configuration.getVersionDefinitionReadTimeout();
        try {
            java.net.URI uri = new java.net.URI(definitionUrl);
            if (uri.getScheme().equalsIgnoreCase("file")) {
                if (!org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.s_configuration.areFileVDFAllowed()) {
                    throw new org.apache.ambari.server.AmbariException("File URL for VDF are not enabled");
                }
                java.io.InputStream stream = uri.toURL().openStream();
                holder.xmlString = org.apache.commons.io.IOUtils.toString(stream, "UTF-8");
            } else {
                org.apache.ambari.server.controller.internal.URLRedirectProvider provider = new org.apache.ambari.server.controller.internal.URLRedirectProvider(connectTimeout, readTimeout, true);
                org.apache.ambari.server.controller.internal.URLRedirectProvider.RequestResult requestResult = provider.executeGet(definitionUrl);
                if (requestResult.getCode() != org.apache.http.HttpStatus.SC_OK) {
                    java.lang.String err = java.lang.String.format("Could not load url from '%s' with code '%d'.  %s", definitionUrl, requestResult.getCode(), requestResult.getContent());
                    throw new org.apache.ambari.server.AmbariException(err);
                }
                holder.xmlString = requestResult.getContent();
            }
            holder.xml = org.apache.ambari.server.state.repository.VersionDefinitionXml.load(holder.xmlString);
        } catch (java.lang.Exception e) {
            java.lang.String err = java.lang.String.format("Could not load url from %s.  %s", definitionUrl, e.getMessage());
            throw new org.apache.ambari.server.AmbariException(err, e);
        }
        return holder;
    }

    @org.apache.ambari.annotations.Experimental(feature = org.apache.ambari.annotations.ExperimentalFeature.CUSTOM_SERVICE_REPOS, comment = "Remove logic for handling custom service repos after enabling multi-mpack cluster deployment")
    protected void toRepositoryVersionEntity(org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.XmlHolder holder) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity entity = new org.apache.ambari.server.orm.entities.RepositoryVersionEntity();
        org.apache.ambari.server.state.StackId stackId = new org.apache.ambari.server.state.StackId(holder.xml.release.stackId);
        org.apache.ambari.server.orm.entities.StackEntity stackEntity = org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.s_stackDAO.find(stackId.getStackName(), stackId.getStackVersion());
        entity.setStack(stackEntity);
        java.lang.String credentials = null;
        if (holder.url != null) {
            try {
                java.net.URI uri = new java.net.URI(holder.url);
                credentials = uri.getUserInfo();
            } catch (java.net.URISyntaxException e) {
                throw new org.apache.ambari.server.AmbariException(java.lang.String.format("Could not parse url %s", holder.url), e);
            }
        }
        java.util.List<org.apache.ambari.server.state.RepositoryInfo> repos = holder.xml.repositoryInfo.getRepositories(credentials);
        org.apache.ambari.server.state.StackInfo stack = org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.s_metaInfo.get().getStack(stackId);
        com.google.common.collect.ListMultimap<java.lang.String, org.apache.ambari.server.state.RepositoryInfo> stackReposByOs = stack.getRepositoriesByOs();
        repos.addAll(org.apache.ambari.server.stack.RepoUtil.getServiceRepos(repos, stackReposByOs));
        entity.addRepoOsEntities(org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.s_repoVersionHelper.get().createRepoOsEntities(repos));
        entity.setVersion(holder.xml.release.getFullVersion(stack.getReleaseVersion()));
        if (org.apache.commons.lang.StringUtils.isNotEmpty(holder.xml.release.display)) {
            entity.setDisplayName(holder.xml.release.display);
        } else {
            entity.setDisplayName((stackId.getStackName() + "-") + entity.getVersion());
        }
        entity.setType(holder.xml.release.repositoryType);
        entity.setVersionUrl(holder.url);
        entity.setVersionXml(holder.xmlString);
        entity.setVersionXsd(holder.xml.xsdLocation);
        holder.entity = entity;
    }

    private org.apache.ambari.server.controller.spi.Resource toResource(java.lang.String id, org.apache.ambari.server.state.repository.VersionDefinitionXml xml, java.util.Set<java.lang.String> requestedIds, java.util.Set<java.lang.String> validations) throws org.apache.ambari.server.controller.spi.SystemException {
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.VersionDefinition);
        resource.setProperty(org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.VERSION_DEF_ID, id);
        org.apache.ambari.server.state.StackId stackId = new org.apache.ambari.server.state.StackId(xml.release.stackId);
        resource.setProperty(org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.VERSION_DEF_STACK_NAME, stackId.getStackName());
        resource.setProperty(org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.VERSION_DEF_STACK_VERSION, stackId.getStackVersion());
        org.apache.ambari.server.state.StackInfo stack = null;
        try {
            stack = org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.s_metaInfo.get().getStack(stackId.getStackName(), stackId.getStackVersion());
        } catch (org.apache.ambari.server.AmbariException e) {
            throw new org.apache.ambari.server.controller.spi.SystemException(java.lang.String.format("Could not load stack %s", stackId));
        }
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.VERSION_DEF_TYPE_PROPERTY_ID, xml.release.repositoryType, requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.VERSION_DEF_FULL_VERSION, xml.release.getFullVersion(stack.getReleaseVersion()), requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.VERSION_DEF_RELEASE_BUILD, xml.release.build, requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.VERSION_DEF_RELEASE_COMPATIBLE_WITH, xml.release.compatibleWith, requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.VERSION_DEF_RELEASE_NOTES, xml.release.releaseNotes, requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.VERSION_DEF_RELEASE_VERSION, xml.release.version, requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.VERSION_DEF_STACK_DEFAULT, xml.isStackDefault(), requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.VERSION_DEF_STACK_REPO_UPDATE_LINK_EXISTS, stack.getRepositoryXml().getLatestURI() != null, requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.VERSION_DEF_DISPLAY_NAME, xml.release.display, requestedIds);
        if (null != validations) {
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.VERSION_DEF_VALIDATION, validations, requestedIds);
        }
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.VERSION_DEF_AVAILABLE_SERVICES, xml.getAvailableServices(stack), requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.VERSION_DEF_STACK_SERVICES, xml.getStackServices(stack), requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.VERSION_DEF_MIN_JDK, stack.getMinJdk(), requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.VERSION_DEF_MAX_JDK, stack.getMaxJdk(), requestedIds);
        return resource;
    }

    private org.apache.ambari.server.controller.spi.Resource toResource(org.apache.ambari.server.orm.entities.RepositoryVersionEntity entity, java.util.Set<java.lang.String> requestedIds) throws org.apache.ambari.server.controller.spi.SystemException {
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.VersionDefinition);
        resource.setProperty(org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.VERSION_DEF_ID, entity.getId());
        org.apache.ambari.server.state.repository.VersionDefinitionXml xml = null;
        try {
            xml = entity.getRepositoryXml();
        } catch (java.lang.Exception e) {
            java.lang.String msg = java.lang.String.format("Could not load version definition %s", entity.getId());
            throw new org.apache.ambari.server.controller.spi.SystemException(msg, e);
        }
        org.apache.ambari.server.state.StackId stackId = new org.apache.ambari.server.state.StackId(xml.release.stackId);
        resource.setProperty(org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.VERSION_DEF_STACK_NAME, stackId.getStackName());
        resource.setProperty(org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.VERSION_DEF_STACK_VERSION, stackId.getStackVersion());
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.VERSION_DEF_TYPE_PROPERTY_ID, entity.getType(), requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.VERSION_DEF_DEFINITION_URL, entity.getVersionUrl(), requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.VERSION_DEF_FULL_VERSION, entity.getVersion(), requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.VERSION_DEF_RELEASE_BUILD, xml.release.build, requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.VERSION_DEF_RELEASE_COMPATIBLE_WITH, xml.release.compatibleWith, requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.VERSION_DEF_RELEASE_NOTES, xml.release.releaseNotes, requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.VERSION_DEF_RELEASE_VERSION, xml.release.version, requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.VERSION_DEF_STACK_DEFAULT, xml.isStackDefault(), requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.VERSION_DEF_DISPLAY_NAME, xml.release.display, requestedIds);
        org.apache.ambari.server.state.StackInfo stack = null;
        if (org.apache.ambari.server.controller.internal.BaseProvider.isPropertyRequested(org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.VERSION_DEF_AVAILABLE_SERVICES, requestedIds) || org.apache.ambari.server.controller.internal.BaseProvider.isPropertyRequested(org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.VERSION_DEF_STACK_SERVICES, requestedIds)) {
            try {
                stack = org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.s_metaInfo.get().getStack(stackId.getStackName(), stackId.getStackVersion());
            } catch (org.apache.ambari.server.AmbariException e) {
                throw new org.apache.ambari.server.controller.spi.SystemException(java.lang.String.format("Could not load stack %s", stackId));
            }
        }
        if (null != stack) {
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.VERSION_DEF_AVAILABLE_SERVICES, xml.getAvailableServices(stack), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.VERSION_DEF_STACK_SERVICES, xml.getStackServices(stack), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.VERSION_DEF_MIN_JDK, stack.getMinJdk(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.VERSION_DEF_MAX_JDK, stack.getMaxJdk(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.VERSION_DEF_STACK_REPO_UPDATE_LINK_EXISTS, stack.getRepositoryXml().getLatestURI() != null, requestedIds);
        }
        return resource;
    }

    private void addSubresources(org.apache.ambari.server.controller.spi.Resource res, org.apache.ambari.server.orm.entities.RepositoryVersionEntity entity) {
        org.codehaus.jackson.node.JsonNodeFactory factory = org.codehaus.jackson.node.JsonNodeFactory.instance;
        org.codehaus.jackson.node.ArrayNode subs = factory.arrayNode();
        for (org.apache.ambari.server.orm.entities.RepoOsEntity os : entity.getRepoOsEntities()) {
            org.codehaus.jackson.node.ObjectNode osBase = factory.objectNode();
            org.codehaus.jackson.node.ObjectNode osElement = factory.objectNode();
            osElement.put(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyName(org.apache.ambari.server.controller.internal.OperatingSystemResourceProvider.OPERATING_SYSTEM_AMBARI_MANAGED_REPOS), os.isAmbariManaged());
            osElement.put(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyName(org.apache.ambari.server.controller.internal.OperatingSystemResourceProvider.OPERATING_SYSTEM_OS_TYPE_PROPERTY_ID), os.getFamily());
            osElement.put(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyName(org.apache.ambari.server.controller.internal.OperatingSystemResourceProvider.OPERATING_SYSTEM_STACK_NAME_PROPERTY_ID), entity.getStackName());
            osElement.put(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyName(org.apache.ambari.server.controller.internal.OperatingSystemResourceProvider.OPERATING_SYSTEM_STACK_VERSION_PROPERTY_ID), entity.getStackVersion());
            osBase.put(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyCategory(org.apache.ambari.server.controller.internal.OperatingSystemResourceProvider.OPERATING_SYSTEM_AMBARI_MANAGED_REPOS), osElement);
            org.codehaus.jackson.node.ArrayNode reposArray = factory.arrayNode();
            for (org.apache.ambari.server.orm.entities.RepoDefinitionEntity repo : os.getRepoDefinitionEntities()) {
                org.codehaus.jackson.node.ObjectNode repoBase = factory.objectNode();
                org.codehaus.jackson.node.ObjectNode repoElement = factory.objectNode();
                repoElement.put(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyName(org.apache.ambari.server.controller.internal.RepositoryResourceProvider.REPOSITORY_BASE_URL_PROPERTY_ID), repo.getBaseUrl());
                repoElement.put(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyName(org.apache.ambari.server.controller.internal.RepositoryResourceProvider.REPOSITORY_OS_TYPE_PROPERTY_ID), os.getFamily());
                repoElement.put(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyName(org.apache.ambari.server.controller.internal.RepositoryResourceProvider.REPOSITORY_REPO_ID_PROPERTY_ID), repo.getRepoID());
                repoElement.put(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyName(org.apache.ambari.server.controller.internal.RepositoryResourceProvider.REPOSITORY_REPO_NAME_PROPERTY_ID), repo.getRepoName());
                repoElement.put(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyName(org.apache.ambari.server.controller.internal.RepositoryResourceProvider.REPOSITORY_DISTRIBUTION_PROPERTY_ID), repo.getDistribution());
                repoElement.put(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyName(org.apache.ambari.server.controller.internal.RepositoryResourceProvider.REPOSITORY_COMPONENTS_PROPERTY_ID), repo.getComponents());
                repoElement.put(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyName(org.apache.ambari.server.controller.internal.RepositoryResourceProvider.REPOSITORY_STACK_NAME_PROPERTY_ID), entity.getStackName());
                repoElement.put(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyName(org.apache.ambari.server.controller.internal.RepositoryResourceProvider.REPOSITORY_STACK_VERSION_PROPERTY_ID), entity.getStackVersion());
                @org.apache.ambari.annotations.Experimental(feature = org.apache.ambari.annotations.ExperimentalFeature.CUSTOM_SERVICE_REPOS, comment = "Remove logic for handling custom service repos after enabling multi-mpack cluster deployment")
                org.codehaus.jackson.node.ArrayNode applicableServicesNode = factory.arrayNode();
                if (repo.getApplicableServices() != null) {
                    for (java.lang.String applicableService : repo.getApplicableServices()) {
                        applicableServicesNode.add(applicableService);
                    }
                }
                repoElement.put(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyName(org.apache.ambari.server.controller.internal.RepositoryResourceProvider.REPOSITORY_APPLICABLE_SERVICES_PROPERTY_ID), applicableServicesNode);
                org.codehaus.jackson.node.ArrayNode tagsNode = factory.arrayNode();
                for (org.apache.ambari.server.state.stack.RepoTag repoTag : repo.getTags()) {
                    tagsNode.add(repoTag.toString());
                }
                repoElement.put(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyName(org.apache.ambari.server.controller.internal.RepositoryResourceProvider.REPOSITORY_TAGS_PROPERTY_ID), tagsNode);
                repoBase.put(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyCategory(org.apache.ambari.server.controller.internal.RepositoryResourceProvider.REPOSITORY_BASE_URL_PROPERTY_ID), repoElement);
                reposArray.add(repoBase);
            }
            osBase.put(new org.apache.ambari.server.api.resources.RepositoryResourceDefinition().getPluralName(), reposArray);
            subs.add(osBase);
        }
        res.setProperty(new org.apache.ambari.server.api.resources.OperatingSystemResourceDefinition().getPluralName(), subs);
    }

    private static class XmlHolder {
        java.lang.String url = null;

        java.lang.String xmlString = null;

        org.apache.ambari.server.state.repository.VersionDefinitionXml xml = null;

        org.apache.ambari.server.orm.entities.RepositoryVersionEntity entity = null;
    }
}