package org.apache.ambari.server.controller.internal;
public class DefaultProviderModule extends org.apache.ambari.server.controller.internal.AbstractProviderModule {
    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.controller.internal.DefaultProviderModule.class);

    public DefaultProviderModule() {
        super();
    }

    @java.lang.Override
    protected org.apache.ambari.server.controller.spi.ResourceProvider createResourceProvider(org.apache.ambari.server.controller.spi.Resource.Type type) {
        org.apache.ambari.server.controller.internal.DefaultProviderModule.LOGGER.debug("Creating resource provider for the type: {}", type);
        switch (type.getInternalType()) {
            case Workflow :
                return new org.apache.ambari.server.controller.internal.WorkflowResourceProvider();
            case Job :
                return new org.apache.ambari.server.controller.internal.JobResourceProvider();
            case TaskAttempt :
                return new org.apache.ambari.server.controller.internal.TaskAttemptResourceProvider();
            case View :
                return new org.apache.ambari.server.controller.internal.ViewResourceProvider();
            case ViewVersion :
                return new org.apache.ambari.server.controller.internal.ViewVersionResourceProvider();
            case ViewURL :
                return new org.apache.ambari.server.controller.internal.ViewURLResourceProvider();
            case StackServiceComponentDependency :
                return new org.apache.ambari.server.controller.internal.StackDependencyResourceProvider();
            case Permission :
                return new org.apache.ambari.server.controller.internal.PermissionResourceProvider();
            case AmbariPrivilege :
                return new org.apache.ambari.server.controller.internal.AmbariPrivilegeResourceProvider();
            case ViewPrivilege :
                return new org.apache.ambari.server.controller.internal.ViewPrivilegeResourceProvider();
            case ViewPermission :
                return new org.apache.ambari.server.controller.internal.ViewPermissionResourceProvider();
            case ClusterPrivilege :
                return new org.apache.ambari.server.controller.internal.ClusterPrivilegeResourceProvider();
            case LdapSyncEvent :
                return new org.apache.ambari.server.controller.internal.LdapSyncEventResourceProvider(managementController);
            case UserPrivilege :
                return new org.apache.ambari.server.controller.internal.UserPrivilegeResourceProvider();
            case GroupPrivilege :
                return new org.apache.ambari.server.controller.internal.GroupPrivilegeResourceProvider();
            case Alert :
                return new org.apache.ambari.server.controller.internal.AlertResourceProvider(managementController);
            case Mpack :
                return new org.apache.ambari.server.controller.internal.MpackResourceProvider(managementController);
            case AlertDefinition :
                return new org.apache.ambari.server.controller.internal.AlertDefinitionResourceProvider(managementController);
            case AlertHistory :
                return new org.apache.ambari.server.controller.internal.AlertHistoryResourceProvider(managementController);
            case AlertGroup :
                return new org.apache.ambari.server.controller.internal.AlertGroupResourceProvider(managementController);
            case AlertNotice :
                return new org.apache.ambari.server.controller.internal.AlertNoticeResourceProvider(managementController);
            case UpgradeGroup :
                return new org.apache.ambari.server.controller.internal.UpgradeGroupResourceProvider(managementController);
            case UpgradeItem :
                return new org.apache.ambari.server.controller.internal.UpgradeItemResourceProvider(managementController);
            case UpgradeSummary :
                return new org.apache.ambari.server.controller.internal.UpgradeSummaryResourceProvider(managementController);
            case PreUpgradeCheck :
                return new org.apache.ambari.server.controller.internal.PreUpgradeCheckResourceProvider(managementController);
            case HostStackVersion :
                return new org.apache.ambari.server.controller.internal.HostStackVersionResourceProvider(managementController);
            case Stage :
                return new org.apache.ambari.server.controller.internal.StageResourceProvider(managementController);
            case OperatingSystem :
                return new org.apache.ambari.server.controller.internal.OperatingSystemResourceProvider(managementController);
            case Repository :
                return new org.apache.ambari.server.controller.internal.RepositoryResourceProvider(managementController);
            case Setting :
                return new org.apache.ambari.server.controller.internal.SettingResourceProvider();
            case Artifact :
                return new org.apache.ambari.server.controller.internal.ArtifactResourceProvider(managementController);
            case RemoteCluster :
                return new org.apache.ambari.server.controller.internal.RemoteClusterResourceProvider();
            default :
                org.apache.ambari.server.controller.internal.DefaultProviderModule.LOGGER.debug("Delegating creation of resource provider for: {} to the AbstractControllerResourceProvider", type.getInternalType());
                return org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.getResourceProvider(type, managementController);
        }
    }
}