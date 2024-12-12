package org.apache.ambari.server.controller.internal;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import org.apache.commons.lang.StringUtils;
@org.apache.ambari.server.StaticallyInject
public class CredentialResourceProvider extends org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.controller.internal.CredentialResourceProvider.class);

    public static final java.lang.String CREDENTIAL_CLUSTER_NAME_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Credential", "cluster_name");

    public static final java.lang.String CREDENTIAL_ALIAS_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Credential", "alias");

    public static final java.lang.String CREDENTIAL_PRINCIPAL_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Credential", "principal");

    public static final java.lang.String CREDENTIAL_KEY_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Credential", "key");

    public static final java.lang.String CREDENTIAL_TYPE_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Credential", "type");

    private static final java.util.Set<java.lang.String> PK_PROPERTY_IDS;

    private static final java.util.Set<java.lang.String> PROPERTY_IDS;

    private static final java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> KEY_PROPERTY_IDS;

    static {
        java.util.Set<java.lang.String> set;
        set = new java.util.HashSet<>();
        set.add(CREDENTIAL_CLUSTER_NAME_PROPERTY_ID);
        set.add(CREDENTIAL_ALIAS_PROPERTY_ID);
        PK_PROPERTY_IDS = java.util.Collections.unmodifiableSet(set);
        set = new java.util.HashSet<>();
        set.add(CREDENTIAL_CLUSTER_NAME_PROPERTY_ID);
        set.add(CREDENTIAL_ALIAS_PROPERTY_ID);
        set.add(CREDENTIAL_PRINCIPAL_PROPERTY_ID);
        set.add(CREDENTIAL_KEY_PROPERTY_ID);
        set.add(CREDENTIAL_TYPE_PROPERTY_ID);
        PROPERTY_IDS = java.util.Collections.unmodifiableSet(set);
        java.util.HashMap<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> map = new java.util.HashMap<>();
        map.put(org.apache.ambari.server.controller.spi.Resource.Type.Cluster, CREDENTIAL_CLUSTER_NAME_PROPERTY_ID);
        map.put(org.apache.ambari.server.controller.spi.Resource.Type.Credential, CREDENTIAL_ALIAS_PROPERTY_ID);
        KEY_PROPERTY_IDS = java.util.Collections.unmodifiableMap(map);
    }

    @com.google.inject.Inject
    private org.apache.ambari.server.security.encryption.CredentialStoreService credentialStoreService;

    @com.google.inject.assistedinject.AssistedInject
    public CredentialResourceProvider(@com.google.inject.assistedinject.Assisted
    org.apache.ambari.server.controller.AmbariManagementController managementController) {
        super(org.apache.ambari.server.controller.spi.Resource.Type.Credential, org.apache.ambari.server.controller.internal.CredentialResourceProvider.PROPERTY_IDS, org.apache.ambari.server.controller.internal.CredentialResourceProvider.KEY_PROPERTY_IDS, managementController);
        java.util.EnumSet<org.apache.ambari.server.security.authorization.RoleAuthorization> authorizations = java.util.EnumSet.of(org.apache.ambari.server.security.authorization.RoleAuthorization.CLUSTER_MANAGE_CREDENTIALS, org.apache.ambari.server.security.authorization.RoleAuthorization.CLUSTER_TOGGLE_KERBEROS);
        setRequiredCreateAuthorizations(authorizations);
        setRequiredGetAuthorizations(authorizations);
        setRequiredUpdateAuthorizations(authorizations);
        setRequiredDeleteAuthorizations(authorizations);
    }

    @java.lang.Override
    protected org.apache.ambari.server.controller.spi.RequestStatus createResourcesAuthorized(final org.apache.ambari.server.controller.spi.Request request) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.ResourceAlreadyExistsException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        for (final java.util.Map<java.lang.String, java.lang.Object> properties : request.getProperties()) {
            createResources(new org.apache.ambari.server.controller.internal.CredentialResourceProvider.CreateResourcesCommand(properties));
        }
        notifyCreate(org.apache.ambari.server.controller.spi.Resource.Type.Credential, request);
        return getRequestStatus(null);
    }

    @java.lang.Override
    protected java.util.Set<org.apache.ambari.server.controller.spi.Resource> getResourcesAuthorized(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        java.util.Set<java.lang.String> requestedIds = getRequestPropertyIds(request, predicate);
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = new java.util.HashSet<>();
        boolean sendNotFoundErrorIfEmpty = false;
        for (java.util.Map<java.lang.String, java.lang.Object> propertyMap : getPropertyMaps(predicate)) {
            java.lang.String clusterName = ((java.lang.String) (propertyMap.get(org.apache.ambari.server.controller.internal.CredentialResourceProvider.CREDENTIAL_CLUSTER_NAME_PROPERTY_ID)));
            if ((null == clusterName) || clusterName.isEmpty()) {
                throw new java.lang.IllegalArgumentException("Invalid argument, cluster name is required");
            }
            java.lang.String alias = ((java.lang.String) (propertyMap.get(org.apache.ambari.server.controller.internal.CredentialResourceProvider.CREDENTIAL_ALIAS_PROPERTY_ID)));
            if (!org.apache.commons.lang.StringUtils.isEmpty(alias)) {
                try {
                    if (credentialStoreService.containsCredential(clusterName, alias)) {
                        resources.add(toResource(clusterName, alias, credentialStoreService.getCredentialStoreType(clusterName, alias), requestedIds));
                    } else {
                        sendNotFoundErrorIfEmpty = true;
                    }
                } catch (org.apache.ambari.server.AmbariException e) {
                    throw new org.apache.ambari.server.controller.spi.SystemException(e.getLocalizedMessage(), e);
                }
            } else {
                try {
                    java.util.Map<java.lang.String, org.apache.ambari.server.security.encryption.CredentialStoreType> results = credentialStoreService.listCredentials(clusterName);
                    if (results != null) {
                        for (java.util.Map.Entry<java.lang.String, org.apache.ambari.server.security.encryption.CredentialStoreType> entry : results.entrySet()) {
                            resources.add(toResource(clusterName, entry.getKey(), entry.getValue(), requestedIds));
                        }
                    }
                } catch (org.apache.ambari.server.AmbariException e) {
                    throw new org.apache.ambari.server.controller.spi.SystemException(e.getLocalizedMessage(), e);
                }
            }
        }
        if (sendNotFoundErrorIfEmpty && resources.isEmpty()) {
            throw new org.apache.ambari.server.controller.spi.NoSuchResourceException("The requested resource doesn't exist: Credential not found, " + predicate);
        }
        return resources;
    }

    @java.lang.Override
    protected org.apache.ambari.server.controller.spi.RequestStatus updateResourcesAuthorized(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        for (java.util.Map<java.lang.String, java.lang.Object> requestPropMap : request.getProperties()) {
            for (java.util.Map<java.lang.String, java.lang.Object> propertyMap : getPropertyMaps(requestPropMap, predicate)) {
                if (modifyResources(new org.apache.ambari.server.controller.internal.CredentialResourceProvider.ModifyResourcesCommand(propertyMap)) == null) {
                    throw new org.apache.ambari.server.controller.spi.NoSuchResourceException("The requested resource doesn't exist: Credential not found, " + getAlias(propertyMap));
                }
            }
        }
        notifyUpdate(org.apache.ambari.server.controller.spi.Resource.Type.Credential, request, predicate);
        return getRequestStatus(null);
    }

    @java.lang.Override
    protected org.apache.ambari.server.controller.spi.RequestStatus deleteResourcesAuthorized(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        final java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> propertyMaps = getPropertyMaps(predicate);
        for (final java.util.Map<java.lang.String, java.lang.Object> properties : propertyMaps) {
            modifyResources(new org.apache.ambari.server.controller.internal.CredentialResourceProvider.DeleteResourcesCommand(properties));
        }
        notifyDelete(org.apache.ambari.server.controller.spi.Resource.Type.Credential, predicate);
        return getRequestStatus(null);
    }

    @java.lang.Override
    protected java.util.Set<java.lang.String> getPKPropertyIds() {
        return org.apache.ambari.server.controller.internal.CredentialResourceProvider.PK_PROPERTY_IDS;
    }

    private org.apache.ambari.server.security.credential.Credential createCredential(java.util.Map<java.lang.String, java.lang.Object> properties) throws java.lang.IllegalArgumentException {
        java.lang.String principal;
        java.lang.String key;
        if (properties.get(org.apache.ambari.server.controller.internal.CredentialResourceProvider.CREDENTIAL_PRINCIPAL_PROPERTY_ID) == null) {
            throw new java.lang.IllegalArgumentException(("Property " + org.apache.ambari.server.controller.internal.CredentialResourceProvider.CREDENTIAL_PRINCIPAL_PROPERTY_ID) + " must be provided");
        } else {
            principal = java.lang.String.valueOf(properties.get(org.apache.ambari.server.controller.internal.CredentialResourceProvider.CREDENTIAL_PRINCIPAL_PROPERTY_ID));
        }
        if (properties.get(org.apache.ambari.server.controller.internal.CredentialResourceProvider.CREDENTIAL_KEY_PROPERTY_ID) == null) {
            org.apache.ambari.server.controller.internal.CredentialResourceProvider.LOG.warn("The credential is being added without a key");
            key = null;
        } else {
            key = java.lang.String.valueOf(properties.get(org.apache.ambari.server.controller.internal.CredentialResourceProvider.CREDENTIAL_KEY_PROPERTY_ID));
        }
        return new org.apache.ambari.server.security.credential.PrincipalKeyCredential(principal, key);
    }

    private org.apache.ambari.server.security.encryption.CredentialStoreType getCredentialStoreType(java.util.Map<java.lang.String, java.lang.Object> properties) throws java.lang.IllegalArgumentException {
        java.lang.Object propertyValue = properties.get(org.apache.ambari.server.controller.internal.CredentialResourceProvider.CREDENTIAL_TYPE_PROPERTY_ID);
        if (propertyValue == null) {
            throw new java.lang.IllegalArgumentException(("Property " + org.apache.ambari.server.controller.internal.CredentialResourceProvider.CREDENTIAL_TYPE_PROPERTY_ID) + " must be provided");
        } else if (propertyValue instanceof java.lang.String) {
            try {
                return org.apache.ambari.server.security.encryption.CredentialStoreType.valueOf(((java.lang.String) (propertyValue)).toUpperCase());
            } catch (java.lang.IllegalArgumentException e) {
                throw new java.lang.IllegalArgumentException(("Property " + org.apache.ambari.server.controller.internal.CredentialResourceProvider.CREDENTIAL_TYPE_PROPERTY_ID) + " must be either 'persisted' or 'temporary'", e);
            }
        } else {
            throw new java.lang.IllegalArgumentException(("Property " + org.apache.ambari.server.controller.internal.CredentialResourceProvider.CREDENTIAL_TYPE_PROPERTY_ID) + " must be a String");
        }
    }

    private java.lang.String getClusterName(java.util.Map<java.lang.String, java.lang.Object> properties) throws java.lang.IllegalArgumentException {
        if (properties.get(org.apache.ambari.server.controller.internal.CredentialResourceProvider.CREDENTIAL_CLUSTER_NAME_PROPERTY_ID) == null) {
            throw new java.lang.IllegalArgumentException(("Property " + org.apache.ambari.server.controller.internal.CredentialResourceProvider.CREDENTIAL_CLUSTER_NAME_PROPERTY_ID) + " must be provided");
        } else {
            return java.lang.String.valueOf(properties.get(org.apache.ambari.server.controller.internal.CredentialResourceProvider.CREDENTIAL_CLUSTER_NAME_PROPERTY_ID));
        }
    }

    private java.lang.String getAlias(java.util.Map<java.lang.String, java.lang.Object> properties) throws java.lang.IllegalArgumentException {
        if (properties.get(org.apache.ambari.server.controller.internal.CredentialResourceProvider.CREDENTIAL_ALIAS_PROPERTY_ID) == null) {
            throw new java.lang.IllegalArgumentException(("Property " + org.apache.ambari.server.controller.internal.CredentialResourceProvider.CREDENTIAL_ALIAS_PROPERTY_ID) + " must be provided");
        } else {
            return java.lang.String.valueOf(properties.get(org.apache.ambari.server.controller.internal.CredentialResourceProvider.CREDENTIAL_ALIAS_PROPERTY_ID));
        }
    }

    private void validateForCreateOrModify(org.apache.ambari.server.security.encryption.CredentialStoreType credentialStoreType) throws java.lang.IllegalArgumentException {
        if (!credentialStoreService.isInitialized(credentialStoreType)) {
            if (org.apache.ambari.server.security.encryption.CredentialStoreType.PERSISTED == credentialStoreType) {
                throw new java.lang.IllegalArgumentException("Credentials cannot be stored in Ambari's persistent secure " + ("credential store since secure persistent storage has not yet be configured.  " + "Use ambari-server setup-security to enable this feature."));
            } else if (org.apache.ambari.server.security.encryption.CredentialStoreType.TEMPORARY == credentialStoreType) {
                throw new java.lang.IllegalArgumentException("Credentials cannot be stored in Ambari's temporary secure " + "credential store since secure temporary storage has not yet be configured.");
            }
        }
    }

    private org.apache.ambari.server.controller.spi.Resource toResource(java.lang.String clusterName, java.lang.String alias, org.apache.ambari.server.security.encryption.CredentialStoreType credentialStoreType, java.util.Set<java.lang.String> requestedIds) {
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Credential);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.CredentialResourceProvider.CREDENTIAL_CLUSTER_NAME_PROPERTY_ID, clusterName, requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.CredentialResourceProvider.CREDENTIAL_ALIAS_PROPERTY_ID, alias, requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.CredentialResourceProvider.CREDENTIAL_TYPE_PROPERTY_ID, credentialStoreType.name().toLowerCase(), requestedIds);
        return resource;
    }

    private class CreateResourcesCommand implements org.apache.ambari.server.controller.internal.AbstractResourceProvider.Command<java.lang.String> {
        private final java.util.Map<java.lang.String, java.lang.Object> properties;

        public CreateResourcesCommand(java.util.Map<java.lang.String, java.lang.Object> properties) {
            this.properties = properties;
        }

        @java.lang.Override
        public java.lang.String invoke() throws org.apache.ambari.server.AmbariException {
            org.apache.ambari.server.security.encryption.CredentialStoreType credentialStoreType = getCredentialStoreType(properties);
            validateForCreateOrModify(credentialStoreType);
            java.lang.String clusterName = getClusterName(properties);
            java.lang.String alias = getAlias(properties);
            if (credentialStoreService.containsCredential(clusterName, alias)) {
                throw new org.apache.ambari.server.DuplicateResourceException(("A credential with the alias of " + alias) + " already exists");
            }
            credentialStoreService.setCredential(clusterName, alias, createCredential(properties), credentialStoreType);
            return alias;
        }
    }

    private class ModifyResourcesCommand implements org.apache.ambari.server.controller.internal.AbstractResourceProvider.Command<java.lang.String> {
        private final java.util.Map<java.lang.String, java.lang.Object> properties;

        public ModifyResourcesCommand(java.util.Map<java.lang.String, java.lang.Object> properties) {
            this.properties = properties;
        }

        @java.lang.Override
        public java.lang.String invoke() throws org.apache.ambari.server.AmbariException {
            java.lang.String clusterName = getClusterName(properties);
            java.lang.String alias = getAlias(properties);
            org.apache.ambari.server.security.encryption.CredentialStoreType credentialStoreType = (properties.containsKey(org.apache.ambari.server.controller.internal.CredentialResourceProvider.CREDENTIAL_TYPE_PROPERTY_ID)) ? getCredentialStoreType(properties) : credentialStoreService.getCredentialStoreType(clusterName, alias);
            validateForCreateOrModify(credentialStoreType);
            org.apache.ambari.server.security.credential.Credential credential = credentialStoreService.getCredential(clusterName, alias);
            if (credential instanceof org.apache.ambari.server.security.credential.PrincipalKeyCredential) {
                org.apache.ambari.server.security.credential.PrincipalKeyCredential principalKeyCredential = ((org.apache.ambari.server.security.credential.PrincipalKeyCredential) (credential));
                java.util.Map<java.lang.String, java.lang.Object> credentialProperties = new java.util.HashMap<>();
                credentialStoreService.removeCredential(clusterName, alias);
                if (properties.containsKey(org.apache.ambari.server.controller.internal.CredentialResourceProvider.CREDENTIAL_PRINCIPAL_PROPERTY_ID)) {
                    credentialProperties.put(org.apache.ambari.server.controller.internal.CredentialResourceProvider.CREDENTIAL_PRINCIPAL_PROPERTY_ID, properties.get(org.apache.ambari.server.controller.internal.CredentialResourceProvider.CREDENTIAL_PRINCIPAL_PROPERTY_ID));
                } else {
                    credentialProperties.put(org.apache.ambari.server.controller.internal.CredentialResourceProvider.CREDENTIAL_PRINCIPAL_PROPERTY_ID, principalKeyCredential.getPrincipal());
                }
                if (properties.containsKey(org.apache.ambari.server.controller.internal.CredentialResourceProvider.CREDENTIAL_KEY_PROPERTY_ID)) {
                    credentialProperties.put(org.apache.ambari.server.controller.internal.CredentialResourceProvider.CREDENTIAL_KEY_PROPERTY_ID, properties.get(org.apache.ambari.server.controller.internal.CredentialResourceProvider.CREDENTIAL_KEY_PROPERTY_ID));
                } else {
                    char[] credentialKey = principalKeyCredential.getKey();
                    if (credentialKey != null) {
                        credentialProperties.put(org.apache.ambari.server.controller.internal.CredentialResourceProvider.CREDENTIAL_KEY_PROPERTY_ID, java.lang.String.valueOf(credentialKey));
                    }
                }
                credentialStoreService.setCredential(clusterName, alias, createCredential(credentialProperties), credentialStoreType);
                return alias;
            } else {
                return null;
            }
        }
    }

    private class DeleteResourcesCommand implements org.apache.ambari.server.controller.internal.AbstractResourceProvider.Command<java.lang.String> {
        private final java.util.Map<java.lang.String, java.lang.Object> properties;

        public DeleteResourcesCommand(java.util.Map<java.lang.String, java.lang.Object> properties) {
            this.properties = properties;
        }

        @java.lang.Override
        public java.lang.String invoke() throws org.apache.ambari.server.AmbariException {
            java.lang.String clusterName = getClusterName(properties);
            java.lang.String alias = getAlias(properties);
            credentialStoreService.removeCredential(clusterName, alias);
            return alias;
        }
    }
}