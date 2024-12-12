package org.apache.ambari.server.controller.internal;
@org.apache.ambari.server.StaticallyInject
public class ArtifactResourceProvider extends org.apache.ambari.server.controller.internal.AbstractResourceProvider {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.controller.internal.ArtifactResourceProvider.class);

    public static final java.lang.String RESPONSE_KEY = "Artifacts";

    public static final java.lang.String ARTIFACT_NAME = "artifact_name";

    public static final java.lang.String CLUSTER_NAME = "cluster_name";

    public static final java.lang.String SERVICE_NAME = "service_name";

    public static final java.lang.String ARTIFACT_DATA_PROPERTY = "artifact_data";

    public static final java.lang.String ARTIFACT_NAME_PROPERTY = (org.apache.ambari.server.controller.internal.ArtifactResourceProvider.RESPONSE_KEY + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.ArtifactResourceProvider.ARTIFACT_NAME;

    public static final java.lang.String CLUSTER_NAME_PROPERTY = (org.apache.ambari.server.controller.internal.ArtifactResourceProvider.RESPONSE_KEY + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.ArtifactResourceProvider.CLUSTER_NAME;

    public static final java.lang.String SERVICE_NAME_PROPERTY = (org.apache.ambari.server.controller.internal.ArtifactResourceProvider.RESPONSE_KEY + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.ArtifactResourceProvider.SERVICE_NAME;

    public static final java.lang.String KERBEROS_DESCRIPTOR = "kerberos_descriptor";

    private static final java.util.Set<java.lang.String> pkPropertyIds = new java.util.HashSet<>();

    private static final java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> keyPropertyIds = new java.util.HashMap<>();

    private static final java.util.Set<java.lang.String> propertyIds = new java.util.HashSet<>();

    private static final java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, org.apache.ambari.server.controller.internal.ArtifactResourceProvider.TypeRegistration> typeRegistrations = new java.util.HashMap<>();

    private static final java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.ArtifactResourceProvider.TypeRegistration> typeRegistrationsByFK = new java.util.HashMap<>();

    private static final java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.ArtifactResourceProvider.TypeRegistration> typeRegistrationsByShortFK = new java.util.HashMap<>();

    private static final com.google.gson.Gson jsonSerializer = new com.google.gson.Gson();

    @com.google.inject.Inject
    private static org.apache.ambari.server.orm.dao.ArtifactDAO artifactDAO;

    static {
        propertyIds.add(ARTIFACT_NAME_PROPERTY);
        propertyIds.add(ARTIFACT_DATA_PROPERTY);
        pkPropertyIds.add(ARTIFACT_NAME_PROPERTY);
        keyPropertyIds.put(org.apache.ambari.server.controller.spi.Resource.Type.Artifact, ARTIFACT_NAME_PROPERTY);
        org.apache.ambari.server.controller.internal.ArtifactResourceProvider.ClusterTypeRegistration clusterTypeRegistration = new org.apache.ambari.server.controller.internal.ArtifactResourceProvider.ClusterTypeRegistration();
        typeRegistrations.put(clusterTypeRegistration.getType(), clusterTypeRegistration);
        org.apache.ambari.server.controller.internal.ArtifactResourceProvider.ServiceTypeRegistration serviceTypeRegistration = new org.apache.ambari.server.controller.internal.ArtifactResourceProvider.ServiceTypeRegistration();
        typeRegistrations.put(serviceTypeRegistration.getType(), serviceTypeRegistration);
        for (org.apache.ambari.server.controller.internal.ArtifactResourceProvider.TypeRegistration registration : typeRegistrations.values()) {
            java.lang.String fkProperty = registration.getFKPropertyName();
            keyPropertyIds.put(registration.getType(), fkProperty);
            propertyIds.add(fkProperty);
            typeRegistrationsByFK.put(fkProperty, registration);
            typeRegistrationsByShortFK.put(registration.getShortFKPropertyName(), registration);
            for (java.util.Map.Entry<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> ancestor : registration.getForeignKeyInfo().entrySet()) {
                org.apache.ambari.server.controller.spi.Resource.Type ancestorType = ancestor.getKey();
                if (!keyPropertyIds.containsKey(ancestorType)) {
                    java.lang.String ancestorFK = ancestor.getValue();
                    keyPropertyIds.put(ancestorType, ancestorFK);
                    propertyIds.add(ancestorFK);
                }
            }
        }
    }

    @com.google.inject.Inject
    protected ArtifactResourceProvider(org.apache.ambari.server.controller.AmbariManagementController controller) {
        super(org.apache.ambari.server.controller.internal.ArtifactResourceProvider.propertyIds, org.apache.ambari.server.controller.internal.ArtifactResourceProvider.keyPropertyIds);
        for (org.apache.ambari.server.controller.internal.ArtifactResourceProvider.TypeRegistration typeRegistration : org.apache.ambari.server.controller.internal.ArtifactResourceProvider.typeRegistrations.values()) {
            typeRegistration.setManagementController(controller);
        }
    }

    @java.lang.Override
    protected java.util.Set<java.lang.String> getPKPropertyIds() {
        return org.apache.ambari.server.controller.internal.ArtifactResourceProvider.pkPropertyIds;
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus createResources(org.apache.ambari.server.controller.spi.Request request) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.ResourceAlreadyExistsException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        for (java.util.Map<java.lang.String, java.lang.Object> properties : request.getProperties()) {
            createResources(getCreateCommand(properties, request.getRequestInfoProperties()));
        }
        notifyCreate(org.apache.ambari.server.controller.spi.Resource.Type.Artifact, request);
        return getRequestStatus(null);
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.controller.spi.Resource> getResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> requestProps = getPropertyMaps(predicate);
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = new java.util.LinkedHashSet<>();
        for (java.util.Map<java.lang.String, java.lang.Object> props : requestProps) {
            resources.addAll(getResources(getGetCommand(request, predicate, props)));
        }
        if (resources.isEmpty() && isInstanceRequest(requestProps)) {
            throw new org.apache.ambari.server.controller.spi.NoSuchResourceException("The requested resource doesn't exist: Artifact not found, " + predicate);
        }
        return resources;
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus updateResources(final org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        for (org.apache.ambari.server.controller.spi.Resource resource : getResources(request, predicate)) {
            modifyResources(getUpdateCommand(request, resource));
        }
        notifyUpdate(org.apache.ambari.server.controller.spi.Resource.Type.Artifact, request, predicate);
        return getRequestStatus(null);
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus deleteResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> setResources = getResources(new org.apache.ambari.server.controller.internal.RequestImpl(null, null, null, null), predicate);
        for (final org.apache.ambari.server.controller.spi.Resource resource : setResources) {
            modifyResources(getDeleteCommand(resource));
        }
        notifyDelete(org.apache.ambari.server.controller.spi.Resource.Type.Artifact, predicate);
        return getRequestStatus(null);
    }

    private org.apache.ambari.server.controller.internal.AbstractResourceProvider.Command<java.lang.Void> getCreateCommand(final java.util.Map<java.lang.String, java.lang.Object> properties, final java.util.Map<java.lang.String, java.lang.String> requestInfoProps) {
        return new org.apache.ambari.server.controller.internal.AbstractResourceProvider.Command<java.lang.Void>() {
            @java.lang.Override
            public java.lang.Void invoke() throws org.apache.ambari.server.AmbariException {
                validateParent(properties);
                java.lang.String artifactName = java.lang.String.valueOf(properties.get(org.apache.ambari.server.controller.internal.ArtifactResourceProvider.ARTIFACT_NAME_PROPERTY));
                java.util.TreeMap<java.lang.String, java.lang.String> foreignKeyMap = createForeignKeyMap(properties);
                if (org.apache.ambari.server.controller.internal.ArtifactResourceProvider.artifactDAO.findByNameAndForeignKeys(artifactName, foreignKeyMap) != null) {
                    throw new org.apache.ambari.server.DuplicateResourceException(java.lang.String.format("Attempted to create an artifact which already exists, artifact_name='%s', foreign_keys='%s'", artifactName, getRequestForeignKeys(properties)));
                }
                org.apache.ambari.server.controller.internal.ArtifactResourceProvider.LOG.debug("Creating Artifact Resource with name '{}'. Parent information: {}", artifactName, getRequestForeignKeys(properties));
                org.apache.ambari.server.controller.internal.ArtifactResourceProvider.artifactDAO.create(toEntity(properties, requestInfoProps.get(org.apache.ambari.server.controller.spi.Request.REQUEST_INFO_BODY_PROPERTY)));
                return null;
            }
        };
    }

    private org.apache.ambari.server.controller.internal.AbstractResourceProvider.Command<java.util.Set<org.apache.ambari.server.controller.spi.Resource>> getGetCommand(final org.apache.ambari.server.controller.spi.Request request, final org.apache.ambari.server.controller.spi.Predicate predicate, final java.util.Map<java.lang.String, java.lang.Object> properties) {
        return new org.apache.ambari.server.controller.internal.AbstractResourceProvider.Command<java.util.Set<org.apache.ambari.server.controller.spi.Resource>>() {
            @java.lang.Override
            public java.util.Set<org.apache.ambari.server.controller.spi.Resource> invoke() throws org.apache.ambari.server.AmbariException {
                java.lang.String name = ((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.ArtifactResourceProvider.ARTIFACT_NAME_PROPERTY)));
                validateParent(properties);
                java.util.Set<org.apache.ambari.server.controller.spi.Resource> matchingResources = new java.util.HashSet<>();
                java.util.TreeMap<java.lang.String, java.lang.String> foreignKeys = createForeignKeyMap(properties);
                java.util.Set<java.lang.String> requestPropertyIds = getRequestPropertyIds(request, predicate);
                if (name != null) {
                    org.apache.ambari.server.orm.entities.ArtifactEntity entity = org.apache.ambari.server.controller.internal.ArtifactResourceProvider.artifactDAO.findByNameAndForeignKeys(name, foreignKeys);
                    if (entity != null) {
                        org.apache.ambari.server.controller.spi.Resource instance = toResource(entity, requestPropertyIds);
                        if (predicate.evaluate(instance)) {
                            matchingResources.add(instance);
                        }
                    }
                } else {
                    java.util.List<org.apache.ambari.server.orm.entities.ArtifactEntity> results = org.apache.ambari.server.controller.internal.ArtifactResourceProvider.artifactDAO.findByForeignKeys(foreignKeys);
                    for (org.apache.ambari.server.orm.entities.ArtifactEntity entity : results) {
                        org.apache.ambari.server.controller.spi.Resource resource = toResource(entity, requestPropertyIds);
                        if (predicate.evaluate(resource)) {
                            matchingResources.add(resource);
                        }
                    }
                }
                return matchingResources;
            }
        };
    }

    private org.apache.ambari.server.controller.internal.AbstractResourceProvider.Command<java.lang.Void> getUpdateCommand(final org.apache.ambari.server.controller.spi.Request request, final org.apache.ambari.server.controller.spi.Resource resource) {
        return new org.apache.ambari.server.controller.internal.AbstractResourceProvider.Command<java.lang.Void>() {
            @java.lang.Override
            public java.lang.Void invoke() throws org.apache.ambari.server.AmbariException {
                java.util.Map<java.lang.String, java.lang.Object> entityUpdateProperties = new java.util.HashMap<>(request.getProperties().iterator().next());
                entityUpdateProperties.put(org.apache.ambari.server.controller.internal.ArtifactResourceProvider.ARTIFACT_NAME_PROPERTY, java.lang.String.valueOf(resource.getPropertyValue(org.apache.ambari.server.controller.internal.ArtifactResourceProvider.ARTIFACT_NAME_PROPERTY)));
                org.apache.ambari.server.controller.internal.ArtifactResourceProvider.artifactDAO.merge(toEntity(entityUpdateProperties, request.getRequestInfoProperties().get(org.apache.ambari.server.controller.spi.Request.REQUEST_INFO_BODY_PROPERTY)));
                return null;
            }
        };
    }

    private org.apache.ambari.server.controller.internal.AbstractResourceProvider.Command<java.lang.Void> getDeleteCommand(final org.apache.ambari.server.controller.spi.Resource resource) {
        return new org.apache.ambari.server.controller.internal.AbstractResourceProvider.Command<java.lang.Void>() {
            @java.lang.Override
            public java.lang.Void invoke() throws org.apache.ambari.server.AmbariException {
                java.util.Map<java.lang.String, java.lang.Object> keyProperties = new java.util.HashMap<>();
                for (java.util.Map.Entry<java.lang.String, java.lang.Object> entry : resource.getPropertiesMap().get(org.apache.ambari.server.controller.internal.ArtifactResourceProvider.RESPONSE_KEY).entrySet()) {
                    keyProperties.put(java.lang.String.format("Artifacts/%s", entry.getKey()), entry.getValue());
                }
                java.lang.String artifactName = java.lang.String.valueOf(resource.getPropertyValue(org.apache.ambari.server.controller.internal.ArtifactResourceProvider.ARTIFACT_NAME_PROPERTY));
                java.util.TreeMap<java.lang.String, java.lang.String> artifactForeignKeys = createForeignKeyMap(keyProperties);
                org.apache.ambari.server.orm.entities.ArtifactEntity entity = org.apache.ambari.server.controller.internal.ArtifactResourceProvider.artifactDAO.findByNameAndForeignKeys(artifactName, artifactForeignKeys);
                if (entity != null) {
                    org.apache.ambari.server.controller.internal.ArtifactResourceProvider.LOG.info("Deleting Artifact: name = {}, foreign keys = {}", entity.getArtifactName(), entity.getForeignKeys());
                    org.apache.ambari.server.controller.internal.ArtifactResourceProvider.artifactDAO.remove(entity);
                } else {
                    org.apache.ambari.server.controller.internal.ArtifactResourceProvider.LOG.info("Cannot find Artifact to delete, ignoring: name = {}, foreign keys = {}", artifactName, artifactForeignKeys);
                }
                return null;
            }
        };
    }

    private void validateParent(java.util.Map<java.lang.String, java.lang.Object> properties) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.controller.spi.Resource.Type parentType = getRequestType(properties);
        if (!org.apache.ambari.server.controller.internal.ArtifactResourceProvider.typeRegistrations.get(parentType).instanceExists(org.apache.ambari.server.controller.internal.ArtifactResourceProvider.keyPropertyIds, properties)) {
            throw new org.apache.ambari.server.ParentObjectNotFoundException(java.lang.String.format("Parent resource doesn't exist: %s", getRequestForeignKeys(properties)));
        }
    }

    private org.apache.ambari.server.controller.spi.Resource.Type getRequestType(java.util.Map<java.lang.String, java.lang.Object> properties) throws org.apache.ambari.server.AmbariException {
        java.util.Set<java.lang.String> requestFKs = getRequestForeignKeys(properties).keySet();
        for (org.apache.ambari.server.controller.internal.ArtifactResourceProvider.TypeRegistration registration : org.apache.ambari.server.controller.internal.ArtifactResourceProvider.typeRegistrations.values()) {
            java.util.Collection<java.lang.String> typeFKs = new java.util.HashSet<>(registration.getForeignKeyInfo().values());
            typeFKs.add(registration.getFKPropertyName());
            if (requestFKs.equals(typeFKs)) {
                return registration.getType();
            }
        }
        throw new org.apache.ambari.server.AmbariException("Couldn't determine resource type based on request properties");
    }

    private java.util.Map<java.lang.String, java.lang.String> getRequestForeignKeys(java.util.Map<java.lang.String, java.lang.Object> properties) {
        java.util.Map<java.lang.String, java.lang.String> requestFKs = new java.util.HashMap<>();
        for (java.lang.String property : properties.keySet()) {
            if ((!property.equals(org.apache.ambari.server.controller.internal.ArtifactResourceProvider.ARTIFACT_NAME_PROPERTY)) && (!property.startsWith(org.apache.ambari.server.controller.internal.ArtifactResourceProvider.ARTIFACT_DATA_PROPERTY))) {
                requestFKs.put(property, java.lang.String.valueOf(properties.get(property)));
            }
        }
        return requestFKs;
    }

    @java.lang.SuppressWarnings("unchecked")
    private org.apache.ambari.server.orm.entities.ArtifactEntity toEntity(java.util.Map<java.lang.String, java.lang.Object> properties, java.lang.String rawRequestBody) throws org.apache.ambari.server.AmbariException {
        java.lang.String name = ((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.ArtifactResourceProvider.ARTIFACT_NAME_PROPERTY)));
        if ((name == null) || name.isEmpty()) {
            throw new java.lang.IllegalArgumentException("Artifact name must be provided");
        }
        org.apache.ambari.server.orm.entities.ArtifactEntity artifact = new org.apache.ambari.server.orm.entities.ArtifactEntity();
        artifact.setArtifactName(name);
        artifact.setForeignKeys(createForeignKeyMap(properties));
        java.util.Map<java.lang.String, java.lang.Object> rawBodyMap = org.apache.ambari.server.controller.internal.ArtifactResourceProvider.jsonSerializer.<java.util.Map<java.lang.String, java.lang.Object>>fromJson(rawRequestBody, java.util.Map.class);
        java.lang.Object artifactData = rawBodyMap.get(org.apache.ambari.server.controller.internal.ArtifactResourceProvider.ARTIFACT_DATA_PROPERTY);
        if (artifactData == null) {
            throw new java.lang.IllegalArgumentException("artifact_data property must be provided");
        }
        if (!(artifactData instanceof java.util.Map)) {
            throw new java.lang.IllegalArgumentException("artifact_data property must be a map");
        }
        artifact.setArtifactData(((java.util.Map<java.lang.String, java.lang.Object>) (artifactData)));
        return artifact;
    }

    private java.util.TreeMap<java.lang.String, java.lang.String> createForeignKeyMap(java.util.Map<java.lang.String, java.lang.Object> properties) throws org.apache.ambari.server.AmbariException {
        java.util.TreeMap<java.lang.String, java.lang.String> foreignKeys = new java.util.TreeMap<>();
        for (java.lang.String keyProperty : org.apache.ambari.server.controller.internal.ArtifactResourceProvider.keyPropertyIds.values()) {
            if (!keyProperty.equals(org.apache.ambari.server.controller.internal.ArtifactResourceProvider.ARTIFACT_NAME_PROPERTY)) {
                java.lang.String origValue = ((java.lang.String) (properties.get(keyProperty)));
                if ((origValue != null) && (!origValue.isEmpty())) {
                    org.apache.ambari.server.controller.internal.ArtifactResourceProvider.TypeRegistration typeRegistration = org.apache.ambari.server.controller.internal.ArtifactResourceProvider.typeRegistrationsByFK.get(keyProperty);
                    foreignKeys.put(typeRegistration.getShortFKPropertyName(), typeRegistration.toPersistId(origValue));
                }
            }
        }
        return foreignKeys;
    }

    private org.apache.ambari.server.controller.spi.Resource toResource(org.apache.ambari.server.orm.entities.ArtifactEntity entity, java.util.Set<java.lang.String> requestedIds) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Artifact);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.ArtifactResourceProvider.ARTIFACT_NAME_PROPERTY, entity.getArtifactName(), requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.ArtifactResourceProvider.ARTIFACT_DATA_PROPERTY, entity.getArtifactData(), requestedIds);
        for (java.util.Map.Entry<java.lang.String, java.lang.String> entry : entity.getForeignKeys().entrySet()) {
            org.apache.ambari.server.controller.internal.ArtifactResourceProvider.TypeRegistration typeRegistration = org.apache.ambari.server.controller.internal.ArtifactResourceProvider.typeRegistrationsByShortFK.get(entry.getKey());
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, typeRegistration.getFKPropertyName(), typeRegistration.fromPersistId(entry.getValue()), requestedIds);
        }
        return resource;
    }

    private boolean isInstanceRequest(java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> requestProps) {
        return (requestProps.size() == 1) && (requestProps.iterator().next().get(org.apache.ambari.server.controller.internal.ArtifactResourceProvider.ARTIFACT_NAME_PROPERTY) != null);
    }

    public static java.lang.String toArtifactDataJson(java.util.Map<?, ?> properties) {
        return java.lang.String.format("{ \"%s\": %s }", org.apache.ambari.server.controller.internal.ArtifactResourceProvider.ARTIFACT_DATA_PROPERTY, org.apache.ambari.server.controller.internal.ArtifactResourceProvider.jsonSerializer.toJson(properties));
    }

    public interface TypeRegistration {
        void setManagementController(org.apache.ambari.server.controller.AmbariManagementController controller);

        org.apache.ambari.server.controller.spi.Resource.Type getType();

        java.lang.String getFKPropertyName();

        java.lang.String getShortFKPropertyName();

        java.lang.String toPersistId(java.lang.String value) throws org.apache.ambari.server.AmbariException;

        java.lang.String fromPersistId(java.lang.String value) throws org.apache.ambari.server.AmbariException;

        java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> getForeignKeyInfo();

        boolean instanceExists(java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> keyMap, java.util.Map<java.lang.String, java.lang.Object> properties) throws org.apache.ambari.server.AmbariException;
    }

    private static class ClusterTypeRegistration implements org.apache.ambari.server.controller.internal.ArtifactResourceProvider.TypeRegistration {
        private org.apache.ambari.server.controller.AmbariManagementController controller = null;

        @java.lang.Override
        public void setManagementController(org.apache.ambari.server.controller.AmbariManagementController controller) {
            this.controller = controller;
        }

        @java.lang.Override
        public org.apache.ambari.server.controller.spi.Resource.Type getType() {
            return org.apache.ambari.server.controller.spi.Resource.Type.Cluster;
        }

        @java.lang.Override
        public java.lang.String getFKPropertyName() {
            return org.apache.ambari.server.controller.internal.ArtifactResourceProvider.CLUSTER_NAME_PROPERTY;
        }

        @java.lang.Override
        public java.lang.String getShortFKPropertyName() {
            return "cluster";
        }

        @java.lang.Override
        public java.lang.String toPersistId(java.lang.String value) throws org.apache.ambari.server.AmbariException {
            return java.lang.String.valueOf(controller.getClusters().getCluster(value).getClusterId());
        }

        @java.lang.Override
        public java.lang.String fromPersistId(java.lang.String value) throws org.apache.ambari.server.AmbariException {
            return controller.getClusters().getClusterById(java.lang.Long.parseLong(value)).getClusterName();
        }

        @java.lang.Override
        public java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> getForeignKeyInfo() {
            return java.util.Collections.emptyMap();
        }

        @java.lang.Override
        public boolean instanceExists(java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> keyMap, java.util.Map<java.lang.String, java.lang.Object> properties) throws org.apache.ambari.server.AmbariException {
            try {
                java.lang.String clusterName = java.lang.String.valueOf(properties.get(org.apache.ambari.server.controller.internal.ArtifactResourceProvider.CLUSTER_NAME_PROPERTY));
                controller.getClusters().getCluster(clusterName);
                return true;
            } catch (org.apache.ambari.server.ObjectNotFoundException e) {
            }
            return false;
        }
    }

    private static class ServiceTypeRegistration implements org.apache.ambari.server.controller.internal.ArtifactResourceProvider.TypeRegistration {
        private org.apache.ambari.server.controller.AmbariManagementController controller = null;

        @java.lang.Override
        public void setManagementController(org.apache.ambari.server.controller.AmbariManagementController controller) {
            this.controller = controller;
        }

        @java.lang.Override
        public org.apache.ambari.server.controller.spi.Resource.Type getType() {
            return org.apache.ambari.server.controller.spi.Resource.Type.Service;
        }

        @java.lang.Override
        public java.lang.String getFKPropertyName() {
            return org.apache.ambari.server.controller.internal.ArtifactResourceProvider.SERVICE_NAME_PROPERTY;
        }

        @java.lang.Override
        public java.lang.String getShortFKPropertyName() {
            return "service";
        }

        @java.lang.Override
        public java.lang.String toPersistId(java.lang.String value) {
            return value;
        }

        @java.lang.Override
        public java.lang.String fromPersistId(java.lang.String value) {
            return value;
        }

        @java.lang.Override
        public java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> getForeignKeyInfo() {
            return java.util.Collections.singletonMap(org.apache.ambari.server.controller.spi.Resource.Type.Cluster, org.apache.ambari.server.controller.internal.ArtifactResourceProvider.CLUSTER_NAME_PROPERTY);
        }

        @java.lang.Override
        public boolean instanceExists(java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> keyMap, java.util.Map<java.lang.String, java.lang.Object> properties) throws org.apache.ambari.server.AmbariException {
            java.lang.String clusterName = java.lang.String.valueOf(properties.get(keyMap.get(org.apache.ambari.server.controller.spi.Resource.Type.Cluster)));
            try {
                org.apache.ambari.server.state.Cluster cluster = controller.getClusters().getCluster(clusterName);
                cluster.getService(java.lang.String.valueOf(properties.get(org.apache.ambari.server.controller.internal.ArtifactResourceProvider.SERVICE_NAME_PROPERTY)));
                return true;
            } catch (org.apache.ambari.server.ObjectNotFoundException e) {
            }
            return false;
        }
    }
}