package org.apache.ambari.server.controller.internal;
public abstract class AbstractControllerResourceProvider extends org.apache.ambari.server.controller.internal.AbstractAuthorizedResourceProvider {
    private static org.apache.ambari.server.controller.ResourceProviderFactory resourceProviderFactory;

    private final org.apache.ambari.server.controller.AmbariManagementController managementController;

    AbstractControllerResourceProvider(org.apache.ambari.server.controller.spi.Resource.Type type, java.util.Set<java.lang.String> propertyIds, java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> keyPropertyIds, org.apache.ambari.server.controller.AmbariManagementController managementController) {
        super(type, propertyIds, keyPropertyIds);
        this.managementController = managementController;
    }

    public static void init(org.apache.ambari.server.controller.ResourceProviderFactory factory) {
        org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.resourceProviderFactory = factory;
    }

    protected org.apache.ambari.server.controller.AmbariManagementController getManagementController() {
        return managementController;
    }

    protected java.lang.Long getClusterId(java.lang.String clusterName) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.Cluster cluster = (clusterName == null) ? null : managementController.getClusters().getCluster(clusterName);
        return cluster == null ? null : cluster.getClusterId();
    }

    protected java.lang.Long getClusterResourceId(java.lang.String clusterName) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.Cluster cluster = (clusterName == null) ? null : managementController.getClusters().getCluster(clusterName);
        return cluster == null ? null : cluster.getResourceId();
    }

    protected java.lang.Long getClusterResourceId(java.lang.Long clusterId) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.Cluster cluster = (clusterId == null) ? null : managementController.getClusters().getClusterById(clusterId);
        return cluster == null ? null : cluster.getResourceId();
    }

    public static org.apache.ambari.server.controller.spi.ResourceProvider getResourceProvider(org.apache.ambari.server.controller.spi.Resource.Type type, org.apache.ambari.server.controller.AmbariManagementController managementController) {
        switch (type.getInternalType()) {
            case Cluster :
                return new org.apache.ambari.server.controller.internal.ClusterResourceProvider(managementController);
            case Service :
                return org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.resourceProviderFactory.getServiceResourceProvider(managementController);
            case Component :
                return org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.resourceProviderFactory.getComponentResourceProvider(managementController);
            case Host :
                return org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.resourceProviderFactory.getHostResourceProvider(managementController);
            case HostComponent :
                return org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.resourceProviderFactory.getHostComponentResourceProvider(managementController);
            case Configuration :
                return new org.apache.ambari.server.controller.internal.ConfigurationResourceProvider(managementController);
            case ServiceConfigVersion :
                return new org.apache.ambari.server.controller.internal.ServiceConfigVersionResourceProvider(managementController);
            case Action :
                return new org.apache.ambari.server.controller.internal.ActionResourceProvider(managementController);
            case Request :
                return new org.apache.ambari.server.controller.internal.RequestResourceProvider(managementController);
            case Task :
                return new org.apache.ambari.server.controller.internal.TaskResourceProvider(managementController);
            case User :
                return org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.resourceProviderFactory.getUserResourceProvider(managementController);
            case UserAuthenticationSource :
                return org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.resourceProviderFactory.getUserAuthenticationSourceResourceProvider();
            case Group :
                return new org.apache.ambari.server.controller.internal.GroupResourceProvider(managementController);
            case Member :
                return org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.resourceProviderFactory.getMemberResourceProvider(managementController);
            case Upgrade :
                return org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.resourceProviderFactory.getUpgradeResourceProvider(managementController);
            case Stack :
                return new org.apache.ambari.server.controller.internal.StackResourceProvider(managementController);
            case Mpack :
                return new org.apache.ambari.server.controller.internal.MpackResourceProvider(managementController);
            case StackVersion :
                return new org.apache.ambari.server.controller.internal.StackVersionResourceProvider(managementController);
            case ClusterStackVersion :
                return org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.resourceProviderFactory.getClusterStackVersionResourceProvider(managementController);
            case HostStackVersion :
                return new org.apache.ambari.server.controller.internal.HostStackVersionResourceProvider(managementController);
            case StackService :
                return new org.apache.ambari.server.controller.internal.StackServiceResourceProvider(managementController);
            case StackServiceComponent :
                return new org.apache.ambari.server.controller.internal.StackServiceComponentResourceProvider(managementController);
            case StackConfiguration :
                return new org.apache.ambari.server.controller.internal.StackConfigurationResourceProvider(managementController);
            case StackConfigurationDependency :
                return new org.apache.ambari.server.controller.internal.StackConfigurationDependencyResourceProvider(managementController);
            case StackLevelConfiguration :
                return new org.apache.ambari.server.controller.internal.StackLevelConfigurationResourceProvider(managementController);
            case ExtensionLink :
                return new org.apache.ambari.server.controller.internal.ExtensionLinkResourceProvider(managementController);
            case Extension :
                return new org.apache.ambari.server.controller.internal.ExtensionResourceProvider(managementController);
            case ExtensionVersion :
                return new org.apache.ambari.server.controller.internal.ExtensionVersionResourceProvider(managementController);
            case RootService :
                return new org.apache.ambari.server.controller.internal.RootServiceResourceProvider(managementController);
            case RootServiceComponent :
                return new org.apache.ambari.server.controller.internal.RootServiceComponentResourceProvider(managementController);
            case RootServiceComponentConfiguration :
                return org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.resourceProviderFactory.getRootServiceHostComponentConfigurationResourceProvider();
            case RootServiceHostComponent :
                return new org.apache.ambari.server.controller.internal.RootServiceHostComponentResourceProvider(managementController);
            case ConfigGroup :
                return new org.apache.ambari.server.controller.internal.ConfigGroupResourceProvider(managementController);
            case RequestSchedule :
                return new org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider(managementController);
            case HostComponentProcess :
                return new org.apache.ambari.server.controller.internal.HostComponentProcessResourceProvider(managementController);
            case Blueprint :
                return new org.apache.ambari.server.controller.internal.BlueprintResourceProvider(managementController);
            case KerberosDescriptor :
                return org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.resourceProviderFactory.getKerberosDescriptorResourceProvider(managementController);
            case Recommendation :
                return new org.apache.ambari.server.controller.internal.RecommendationResourceProvider(managementController);
            case Validation :
                return new org.apache.ambari.server.controller.internal.ValidationResourceProvider(managementController);
            case ClientConfig :
                return new org.apache.ambari.server.controller.internal.ClientConfigResourceProvider(managementController);
            case RepositoryVersion :
                return org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.resourceProviderFactory.getRepositoryVersionResourceProvider();
            case CompatibleRepositoryVersion :
                return new org.apache.ambari.server.controller.internal.CompatibleRepositoryVersionResourceProvider(managementController);
            case StackArtifact :
                return new org.apache.ambari.server.controller.internal.StackArtifactResourceProvider(managementController);
            case Theme :
                return new org.apache.ambari.server.controller.internal.ThemeArtifactResourceProvider(managementController);
            case QuickLink :
                return new org.apache.ambari.server.controller.internal.QuickLinkArtifactResourceProvider(managementController);
            case ActiveWidgetLayout :
                return new org.apache.ambari.server.controller.internal.ActiveWidgetLayoutResourceProvider(managementController);
            case WidgetLayout :
                return new org.apache.ambari.server.controller.internal.WidgetLayoutResourceProvider(managementController);
            case Widget :
                return new org.apache.ambari.server.controller.internal.WidgetResourceProvider(managementController);
            case HostKerberosIdentity :
                return org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.resourceProviderFactory.getHostKerberosIdentityResourceProvider(managementController);
            case Credential :
                return org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.resourceProviderFactory.getCredentialResourceProvider(managementController);
            case RoleAuthorization :
                return new org.apache.ambari.server.controller.internal.RoleAuthorizationResourceProvider(managementController);
            case UserAuthorization :
                return new org.apache.ambari.server.controller.internal.UserAuthorizationResourceProvider(managementController);
            case VersionDefinition :
                return new org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider();
            case ClusterKerberosDescriptor :
                return new org.apache.ambari.server.controller.internal.ClusterKerberosDescriptorResourceProvider(managementController);
            case LoggingQuery :
                return new org.apache.ambari.server.controller.internal.LoggingResourceProvider(managementController);
            case AlertTarget :
                return org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.resourceProviderFactory.getAlertTargetResourceProvider();
            case ViewInstance :
                return org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.resourceProviderFactory.getViewInstanceResourceProvider();
            case Auth :
                return org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.resourceProviderFactory.getAuthResourceProvider(managementController);
            default :
                throw new java.lang.IllegalArgumentException("Unknown type " + type);
        }
    }

    public static org.apache.ambari.server.controller.spi.ResourceProvider getResourceProvider(org.apache.ambari.server.controller.spi.Resource.Type type) {
        return ((org.apache.ambari.server.controller.internal.ClusterControllerImpl) (org.apache.ambari.server.controller.utilities.ClusterControllerHelper.getClusterController())).ensureResourceProvider(type);
    }
}