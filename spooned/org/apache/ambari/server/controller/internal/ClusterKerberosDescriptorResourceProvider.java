package org.apache.ambari.server.controller.internal;
import org.apache.commons.lang.StringUtils;
public class ClusterKerberosDescriptorResourceProvider extends org.apache.ambari.server.controller.internal.ReadOnlyResourceProvider {
    public static final java.lang.String DIRECTIVE_EVALUATE_WHEN_CLAUSE = "evaluate_when";

    public static final java.lang.String DIRECTIVE_ADDITIONAL_SERVICES = "additional_services";

    public static final java.lang.String CLUSTER_KERBEROS_DESCRIPTOR_CLUSTER_NAME_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("KerberosDescriptor", "cluster_name");

    public static final java.lang.String CLUSTER_KERBEROS_DESCRIPTOR_TYPE_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("KerberosDescriptor", "type");

    public static final java.lang.String CLUSTER_KERBEROS_DESCRIPTOR_DESCRIPTOR_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("KerberosDescriptor", "kerberos_descriptor");

    private static final java.util.Set<java.lang.String> PK_PROPERTY_IDS;

    private static final java.util.Set<java.lang.String> PROPERTY_IDS;

    private static final java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> KEY_PROPERTY_IDS;

    private static final java.util.Set<org.apache.ambari.server.security.authorization.RoleAuthorization> REQUIRED_GET_AUTHORIZATIONS = java.util.EnumSet.of(org.apache.ambari.server.security.authorization.RoleAuthorization.CLUSTER_TOGGLE_KERBEROS, org.apache.ambari.server.security.authorization.RoleAuthorization.CLUSTER_VIEW_CONFIGS, org.apache.ambari.server.security.authorization.RoleAuthorization.HOST_VIEW_CONFIGS, org.apache.ambari.server.security.authorization.RoleAuthorization.SERVICE_VIEW_CONFIGS);

    static {
        java.util.Set<java.lang.String> set;
        set = new java.util.HashSet<>();
        set.add(CLUSTER_KERBEROS_DESCRIPTOR_CLUSTER_NAME_PROPERTY_ID);
        set.add(CLUSTER_KERBEROS_DESCRIPTOR_TYPE_PROPERTY_ID);
        PK_PROPERTY_IDS = java.util.Collections.unmodifiableSet(set);
        set = new java.util.HashSet<>();
        set.add(CLUSTER_KERBEROS_DESCRIPTOR_CLUSTER_NAME_PROPERTY_ID);
        set.add(CLUSTER_KERBEROS_DESCRIPTOR_TYPE_PROPERTY_ID);
        set.add(CLUSTER_KERBEROS_DESCRIPTOR_DESCRIPTOR_PROPERTY_ID);
        PROPERTY_IDS = java.util.Collections.unmodifiableSet(set);
        java.util.HashMap<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> map = new java.util.HashMap<>();
        map.put(org.apache.ambari.server.controller.spi.Resource.Type.Cluster, CLUSTER_KERBEROS_DESCRIPTOR_CLUSTER_NAME_PROPERTY_ID);
        map.put(org.apache.ambari.server.controller.spi.Resource.Type.ClusterKerberosDescriptor, CLUSTER_KERBEROS_DESCRIPTOR_TYPE_PROPERTY_ID);
        KEY_PROPERTY_IDS = java.util.Collections.unmodifiableMap(map);
    }

    public ClusterKerberosDescriptorResourceProvider(org.apache.ambari.server.controller.AmbariManagementController managementController) {
        super(org.apache.ambari.server.controller.spi.Resource.Type.ClusterKerberosDescriptor, org.apache.ambari.server.controller.internal.ClusterKerberosDescriptorResourceProvider.PROPERTY_IDS, org.apache.ambari.server.controller.internal.ClusterKerberosDescriptorResourceProvider.KEY_PROPERTY_IDS, managementController);
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.controller.spi.Resource> getResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        org.apache.ambari.server.security.authorization.AuthorizationHelper.verifyAuthorization(org.apache.ambari.server.security.authorization.ResourceType.CLUSTER, null, org.apache.ambari.server.controller.internal.ClusterKerberosDescriptorResourceProvider.REQUIRED_GET_AUTHORIZATIONS);
        java.util.Set<java.lang.String> requestedIds = getRequestPropertyIds(request, predicate);
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = new java.util.HashSet<>();
        org.apache.ambari.server.controller.AmbariManagementController managementController = getManagementController();
        org.apache.ambari.server.state.Clusters clusters = managementController.getClusters();
        for (java.util.Map<java.lang.String, java.lang.Object> propertyMap : getPropertyMaps(predicate)) {
            java.lang.String clusterName = getClusterName(propertyMap);
            org.apache.ambari.server.state.Cluster cluster;
            try {
                cluster = clusters.getCluster(clusterName);
                if (cluster == null) {
                    throw new org.apache.ambari.server.controller.spi.NoSuchParentResourceException(java.lang.String.format("A cluster with the name %s does not exist.", clusterName));
                }
            } catch (org.apache.ambari.server.AmbariException e) {
                throw new org.apache.ambari.server.controller.spi.NoSuchParentResourceException(java.lang.String.format("A cluster with the name %s does not exist.", clusterName));
            }
            org.apache.ambari.server.security.authorization.AuthorizationHelper.verifyAuthorization(org.apache.ambari.server.security.authorization.ResourceType.CLUSTER, cluster.getResourceId(), org.apache.ambari.server.controller.internal.ClusterKerberosDescriptorResourceProvider.REQUIRED_GET_AUTHORIZATIONS);
            org.apache.ambari.server.controller.KerberosHelper.KerberosDescriptorType kerberosDescriptorType = getKerberosDescriptorType(propertyMap);
            if (kerberosDescriptorType == null) {
                for (org.apache.ambari.server.controller.KerberosHelper.KerberosDescriptorType type : org.apache.ambari.server.controller.KerberosHelper.KerberosDescriptorType.values()) {
                    resources.add(toResource(clusterName, type, null, requestedIds));
                }
            } else {
                org.apache.ambari.server.state.kerberos.KerberosDescriptor kerberosDescriptor;
                try {
                    org.apache.ambari.server.controller.KerberosHelper kerberosHelper = getManagementController().getKerberosHelper();
                    java.util.Map<java.lang.String, java.lang.String> requestInfoProperties = request.getRequestInfoProperties();
                    kerberosDescriptor = kerberosHelper.getKerberosDescriptor(kerberosDescriptorType, cluster, getEvaluateWhen(requestInfoProperties), getAdditionalServices(requestInfoProperties), false, null, null);
                } catch (org.apache.ambari.server.AmbariException e) {
                    throw new org.apache.ambari.server.controller.spi.SystemException("An unexpected error occurred building the cluster's composite Kerberos Descriptor", e);
                }
                if (kerberosDescriptor != null) {
                    resources.add(toResource(clusterName, kerberosDescriptorType, kerberosDescriptor, requestedIds));
                }
            }
        }
        return resources;
    }

    @java.lang.Override
    protected java.util.Set<java.lang.String> getPKPropertyIds() {
        return org.apache.ambari.server.controller.internal.ClusterKerberosDescriptorResourceProvider.PK_PROPERTY_IDS;
    }

    private java.lang.String getClusterName(java.util.Map<java.lang.String, java.lang.Object> propertyMap) {
        java.lang.String clusterName = ((java.lang.String) (propertyMap.get(org.apache.ambari.server.controller.internal.ClusterKerberosDescriptorResourceProvider.CLUSTER_KERBEROS_DESCRIPTOR_CLUSTER_NAME_PROPERTY_ID)));
        if (org.apache.commons.lang.StringUtils.isEmpty(clusterName)) {
            throw new java.lang.IllegalArgumentException("Invalid argument, cluster name is required");
        }
        return clusterName;
    }

    private org.apache.ambari.server.controller.KerberosHelper.KerberosDescriptorType getKerberosDescriptorType(java.util.Map<java.lang.String, java.lang.Object> propertyMap) {
        java.lang.String type = ((java.lang.String) (propertyMap.get(org.apache.ambari.server.controller.internal.ClusterKerberosDescriptorResourceProvider.CLUSTER_KERBEROS_DESCRIPTOR_TYPE_PROPERTY_ID)));
        org.apache.ambari.server.controller.KerberosHelper.KerberosDescriptorType kerberosDescriptorType = null;
        if (!org.apache.commons.lang.StringUtils.isEmpty(type)) {
            try {
                kerberosDescriptorType = org.apache.ambari.server.controller.KerberosHelper.KerberosDescriptorType.valueOf(type.trim().toUpperCase());
            } catch (java.lang.IllegalArgumentException e) {
                throw new java.lang.IllegalArgumentException("Invalid argument, kerberos descriptor type of 'STACK', 'USER', or 'COMPOSITE' is required");
            }
        }
        return kerberosDescriptorType;
    }

    private boolean getEvaluateWhen(java.util.Map<java.lang.String, java.lang.String> requestInfoProperties) {
        return (requestInfoProperties != null) && "true".equalsIgnoreCase(requestInfoProperties.get(org.apache.ambari.server.controller.internal.ClusterKerberosDescriptorResourceProvider.DIRECTIVE_EVALUATE_WHEN_CLAUSE));
    }

    private java.util.Collection<java.lang.String> getAdditionalServices(java.util.Map<java.lang.String, java.lang.String> requestInfoProperties) {
        if (requestInfoProperties != null) {
            java.lang.String value = requestInfoProperties.get(org.apache.ambari.server.controller.internal.ClusterKerberosDescriptorResourceProvider.DIRECTIVE_ADDITIONAL_SERVICES);
            if (!org.apache.commons.lang.StringUtils.isEmpty(value)) {
                return java.util.Arrays.asList(value.split("\\s*,\\s*"));
            }
        }
        return null;
    }

    private org.apache.ambari.server.controller.spi.Resource toResource(java.lang.String clusterName, org.apache.ambari.server.controller.KerberosHelper.KerberosDescriptorType kerberosDescriptorType, org.apache.ambari.server.state.kerberos.KerberosDescriptor kerberosDescriptor, java.util.Set<java.lang.String> requestedIds) {
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.ClusterKerberosDescriptor);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.ClusterKerberosDescriptorResourceProvider.CLUSTER_KERBEROS_DESCRIPTOR_CLUSTER_NAME_PROPERTY_ID, clusterName, requestedIds);
        if (kerberosDescriptorType != null) {
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.ClusterKerberosDescriptorResourceProvider.CLUSTER_KERBEROS_DESCRIPTOR_TYPE_PROPERTY_ID, kerberosDescriptorType.name(), requestedIds);
        }
        if (kerberosDescriptor != null) {
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.ClusterKerberosDescriptorResourceProvider.CLUSTER_KERBEROS_DESCRIPTOR_DESCRIPTOR_PROPERTY_ID, kerberosDescriptor.toMap(), requestedIds);
        }
        return resource;
    }
}