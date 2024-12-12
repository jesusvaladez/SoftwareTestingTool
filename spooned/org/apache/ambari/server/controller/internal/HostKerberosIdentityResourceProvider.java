package org.apache.ambari.server.controller.internal;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
public class HostKerberosIdentityResourceProvider extends org.apache.ambari.server.controller.internal.ReadOnlyResourceProvider {
    protected static final java.lang.String KERBEROS_IDENTITY_CLUSTER_NAME_PROPERTY_ID = "KerberosIdentity/cluster_name";

    protected static final java.lang.String KERBEROS_IDENTITY_HOST_NAME_PROPERTY_ID = "KerberosIdentity/host_name";

    protected static final java.lang.String KERBEROS_IDENTITY_DESCRIPTION_PROPERTY_ID = "KerberosIdentity/description";

    protected static final java.lang.String KERBEROS_IDENTITY_PRINCIPAL_NAME_PROPERTY_ID = "KerberosIdentity/principal_name";

    protected static final java.lang.String KERBEROS_IDENTITY_PRINCIPAL_TYPE_PROPERTY_ID = "KerberosIdentity/principal_type";

    protected static final java.lang.String KERBEROS_IDENTITY_PRINCIPAL_LOCAL_USERNAME_PROPERTY_ID = "KerberosIdentity/principal_local_username";

    protected static final java.lang.String KERBEROS_IDENTITY_KEYTAB_FILE_PATH_PROPERTY_ID = "KerberosIdentity/keytab_file_path";

    protected static final java.lang.String KERBEROS_IDENTITY_KEYTAB_FILE_OWNER_PROPERTY_ID = "KerberosIdentity/keytab_file_owner";

    protected static final java.lang.String KERBEROS_IDENTITY_KEYTAB_FILE_OWNER_ACCESS_PROPERTY_ID = "KerberosIdentity/keytab_file_owner_access";

    protected static final java.lang.String KERBEROS_IDENTITY_KEYTAB_FILE_GROUP_PROPERTY_ID = "KerberosIdentity/keytab_file_group";

    protected static final java.lang.String KERBEROS_IDENTITY_KEYTAB_FILE_GROUP_ACCESS_PROPERTY_ID = "KerberosIdentity/keytab_file_group_access";

    protected static final java.lang.String KERBEROS_IDENTITY_KEYTAB_FILE_MODE_PROPERTY_ID = "KerberosIdentity/keytab_file_mode";

    protected static final java.lang.String KERBEROS_IDENTITY_KEYTAB_FILE_INSTALLED_PROPERTY_ID = "KerberosIdentity/keytab_file_installed";

    protected static final java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> PK_PROPERTY_MAP = java.util.Collections.unmodifiableMap(new java.util.HashMap<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String>() {
        {
            put(org.apache.ambari.server.controller.spi.Resource.Type.Cluster, org.apache.ambari.server.controller.internal.HostKerberosIdentityResourceProvider.KERBEROS_IDENTITY_CLUSTER_NAME_PROPERTY_ID);
            put(org.apache.ambari.server.controller.spi.Resource.Type.Host, org.apache.ambari.server.controller.internal.HostKerberosIdentityResourceProvider.KERBEROS_IDENTITY_HOST_NAME_PROPERTY_ID);
            put(org.apache.ambari.server.controller.spi.Resource.Type.HostKerberosIdentity, org.apache.ambari.server.controller.internal.HostKerberosIdentityResourceProvider.KERBEROS_IDENTITY_PRINCIPAL_NAME_PROPERTY_ID);
        }
    });

    protected static final java.util.Set<java.lang.String> PK_PROPERTY_IDS = java.util.Collections.unmodifiableSet(new java.util.HashSet<>(org.apache.ambari.server.controller.internal.HostKerberosIdentityResourceProvider.PK_PROPERTY_MAP.values()));

    protected static final java.util.Set<java.lang.String> PROPERTY_IDS = java.util.Collections.unmodifiableSet(new java.util.HashSet<java.lang.String>() {
        {
            add(org.apache.ambari.server.controller.internal.HostKerberosIdentityResourceProvider.KERBEROS_IDENTITY_CLUSTER_NAME_PROPERTY_ID);
            add(org.apache.ambari.server.controller.internal.HostKerberosIdentityResourceProvider.KERBEROS_IDENTITY_HOST_NAME_PROPERTY_ID);
            add(org.apache.ambari.server.controller.internal.HostKerberosIdentityResourceProvider.KERBEROS_IDENTITY_DESCRIPTION_PROPERTY_ID);
            add(org.apache.ambari.server.controller.internal.HostKerberosIdentityResourceProvider.KERBEROS_IDENTITY_PRINCIPAL_NAME_PROPERTY_ID);
            add(org.apache.ambari.server.controller.internal.HostKerberosIdentityResourceProvider.KERBEROS_IDENTITY_PRINCIPAL_TYPE_PROPERTY_ID);
            add(org.apache.ambari.server.controller.internal.HostKerberosIdentityResourceProvider.KERBEROS_IDENTITY_PRINCIPAL_LOCAL_USERNAME_PROPERTY_ID);
            add(org.apache.ambari.server.controller.internal.HostKerberosIdentityResourceProvider.KERBEROS_IDENTITY_KEYTAB_FILE_PATH_PROPERTY_ID);
            add(org.apache.ambari.server.controller.internal.HostKerberosIdentityResourceProvider.KERBEROS_IDENTITY_KEYTAB_FILE_OWNER_PROPERTY_ID);
            add(org.apache.ambari.server.controller.internal.HostKerberosIdentityResourceProvider.KERBEROS_IDENTITY_KEYTAB_FILE_OWNER_ACCESS_PROPERTY_ID);
            add(org.apache.ambari.server.controller.internal.HostKerberosIdentityResourceProvider.KERBEROS_IDENTITY_KEYTAB_FILE_GROUP_PROPERTY_ID);
            add(org.apache.ambari.server.controller.internal.HostKerberosIdentityResourceProvider.KERBEROS_IDENTITY_KEYTAB_FILE_GROUP_ACCESS_PROPERTY_ID);
            add(org.apache.ambari.server.controller.internal.HostKerberosIdentityResourceProvider.KERBEROS_IDENTITY_KEYTAB_FILE_MODE_PROPERTY_ID);
            add(org.apache.ambari.server.controller.internal.HostKerberosIdentityResourceProvider.KERBEROS_IDENTITY_KEYTAB_FILE_INSTALLED_PROPERTY_ID);
        }
    });

    @com.google.inject.Inject
    private org.apache.ambari.server.controller.KerberosHelper kerberosHelper;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.KerberosPrincipalDAO kerberosPrincipalDAO;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.HostDAO hostDAO;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.KerberosKeytabPrincipalDAO kerberosKeytabPrincipalDAO;

    @com.google.inject.assistedinject.AssistedInject
    HostKerberosIdentityResourceProvider(@com.google.inject.assistedinject.Assisted
    org.apache.ambari.server.controller.AmbariManagementController managementController) {
        super(org.apache.ambari.server.controller.spi.Resource.Type.HostKerberosIdentity, org.apache.ambari.server.controller.internal.HostKerberosIdentityResourceProvider.PROPERTY_IDS, org.apache.ambari.server.controller.internal.HostKerberosIdentityResourceProvider.PK_PROPERTY_MAP, managementController);
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.controller.spi.Resource> getResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        return getResources(new org.apache.ambari.server.controller.internal.HostKerberosIdentityResourceProvider.GetResourcesCommand(getPropertyMaps(predicate), getRequestPropertyIds(request, predicate)));
    }

    @java.lang.Override
    protected java.util.Set<java.lang.String> getPKPropertyIds() {
        return org.apache.ambari.server.controller.internal.HostKerberosIdentityResourceProvider.PK_PROPERTY_IDS;
    }

    private class GetResourcesCommand implements org.apache.ambari.server.controller.internal.AbstractResourceProvider.Command<java.util.Set<org.apache.ambari.server.controller.spi.Resource>> {
        private final java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> propertyMaps;

        private final java.util.Set<java.lang.String> requestPropertyIds;

        public GetResourcesCommand(java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> propertyMaps, java.util.Set<java.lang.String> requestPropertyIds) {
            this.propertyMaps = propertyMaps;
            this.requestPropertyIds = requestPropertyIds;
        }

        @java.lang.Override
        public java.util.Set<org.apache.ambari.server.controller.spi.Resource> invoke() throws org.apache.ambari.server.AmbariException {
            java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = new java.util.HashSet<>();
            for (java.util.Map<java.lang.String, java.lang.Object> propertyMap : propertyMaps) {
                java.lang.String clusterName = ((java.lang.String) (propertyMap.get(org.apache.ambari.server.controller.internal.HostKerberosIdentityResourceProvider.KERBEROS_IDENTITY_CLUSTER_NAME_PROPERTY_ID)));
                java.lang.String hostName = ((java.lang.String) (propertyMap.get(org.apache.ambari.server.controller.internal.HostKerberosIdentityResourceProvider.KERBEROS_IDENTITY_HOST_NAME_PROPERTY_ID)));
                org.apache.ambari.server.state.Clusters clusters = getManagementController().getClusters();
                org.apache.ambari.server.state.Cluster cluster = clusters.getCluster(clusterName);
                org.apache.ambari.server.state.kerberos.KerberosDescriptor kerberosDescriptor = kerberosHelper.getKerberosDescriptor(cluster, false);
                java.util.Map<java.lang.String, java.util.Collection<org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor>> hostDescriptors = kerberosHelper.getActiveIdentities(clusterName, hostName, null, null, true, null, kerberosDescriptor, null);
                if (hostDescriptors != null) {
                    for (java.util.Map.Entry<java.lang.String, java.util.Collection<org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor>> entry : hostDescriptors.entrySet()) {
                        java.util.Collection<org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor> descriptors = entry.getValue();
                        if (descriptors != null) {
                            java.lang.String currentHostName = entry.getKey();
                            org.apache.ambari.server.orm.entities.HostEntity host = hostDAO.findByName(currentHostName);
                            java.lang.Long hostId = (host == null) ? null : host.getHostId();
                            for (org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor descriptor : descriptors) {
                                org.apache.ambari.server.state.kerberos.KerberosPrincipalDescriptor principalDescriptor = descriptor.getPrincipalDescriptor();
                                if (principalDescriptor != null) {
                                    java.lang.String principal = principalDescriptor.getValue();
                                    if ((principal != null) && (!principal.isEmpty())) {
                                        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.HostKerberosIdentity);
                                        org.apache.ambari.server.state.kerberos.KerberosPrincipalType principalType = principalDescriptor.getType();
                                        if (principalType == null) {
                                            principalType = org.apache.ambari.server.state.kerberos.KerberosPrincipalType.SERVICE;
                                        }
                                        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.HostKerberosIdentityResourceProvider.KERBEROS_IDENTITY_CLUSTER_NAME_PROPERTY_ID, clusterName, requestPropertyIds);
                                        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.HostKerberosIdentityResourceProvider.KERBEROS_IDENTITY_HOST_NAME_PROPERTY_ID, currentHostName, requestPropertyIds);
                                        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.HostKerberosIdentityResourceProvider.KERBEROS_IDENTITY_PRINCIPAL_NAME_PROPERTY_ID, principal, requestPropertyIds);
                                        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.HostKerberosIdentityResourceProvider.KERBEROS_IDENTITY_PRINCIPAL_TYPE_PROPERTY_ID, principalType, requestPropertyIds);
                                        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.HostKerberosIdentityResourceProvider.KERBEROS_IDENTITY_PRINCIPAL_LOCAL_USERNAME_PROPERTY_ID, principalDescriptor.getLocalUsername(), requestPropertyIds);
                                        org.apache.ambari.server.state.kerberos.KerberosKeytabDescriptor keytabDescriptor = descriptor.getKeytabDescriptor();
                                        java.lang.String installedStatus;
                                        if ((hostId != null) && kerberosPrincipalDAO.exists(principal)) {
                                            if (keytabDescriptor != null) {
                                                org.apache.ambari.server.orm.entities.KerberosKeytabPrincipalEntity entity = kerberosKeytabPrincipalDAO.findByNaturalKey(hostId, keytabDescriptor.getFile(), principal);
                                                if ((entity != null) && entity.isDistributed()) {
                                                    installedStatus = "true";
                                                } else {
                                                    installedStatus = "false";
                                                }
                                            } else {
                                                installedStatus = "false";
                                            }
                                        } else {
                                            installedStatus = "unknown";
                                        }
                                        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.HostKerberosIdentityResourceProvider.KERBEROS_IDENTITY_KEYTAB_FILE_INSTALLED_PROPERTY_ID, installedStatus, requestPropertyIds);
                                        if (keytabDescriptor != null) {
                                            java.lang.String ownerAccess = keytabDescriptor.getOwnerAccess();
                                            java.lang.String groupAccess = keytabDescriptor.getGroupAccess();
                                            int mode = 0;
                                            if ("rw".equals(ownerAccess)) {
                                                mode += 600;
                                            } else if ("r".equals(ownerAccess)) {
                                                mode += 400;
                                            }
                                            if ("rw".equals(groupAccess)) {
                                                mode += 60;
                                            } else if ("r".equals(groupAccess)) {
                                                mode += 40;
                                            }
                                            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.HostKerberosIdentityResourceProvider.KERBEROS_IDENTITY_KEYTAB_FILE_PATH_PROPERTY_ID, keytabDescriptor.getFile(), requestPropertyIds);
                                            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.HostKerberosIdentityResourceProvider.KERBEROS_IDENTITY_KEYTAB_FILE_OWNER_PROPERTY_ID, keytabDescriptor.getOwnerName(), requestPropertyIds);
                                            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.HostKerberosIdentityResourceProvider.KERBEROS_IDENTITY_KEYTAB_FILE_OWNER_ACCESS_PROPERTY_ID, ownerAccess, requestPropertyIds);
                                            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.HostKerberosIdentityResourceProvider.KERBEROS_IDENTITY_KEYTAB_FILE_GROUP_PROPERTY_ID, keytabDescriptor.getGroupName(), requestPropertyIds);
                                            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.HostKerberosIdentityResourceProvider.KERBEROS_IDENTITY_KEYTAB_FILE_GROUP_ACCESS_PROPERTY_ID, groupAccess, requestPropertyIds);
                                            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.HostKerberosIdentityResourceProvider.KERBEROS_IDENTITY_KEYTAB_FILE_MODE_PROPERTY_ID, new java.text.DecimalFormat("000").format(mode), requestPropertyIds);
                                        }
                                        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.HostKerberosIdentityResourceProvider.KERBEROS_IDENTITY_DESCRIPTION_PROPERTY_ID, descriptor.getName(), requestPropertyIds);
                                        resources.add(resource);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            return resources;
        }
    }
}