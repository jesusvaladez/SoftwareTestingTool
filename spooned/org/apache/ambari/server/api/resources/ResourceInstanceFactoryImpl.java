package org.apache.ambari.server.api.resources;
public class ResourceInstanceFactoryImpl implements org.apache.ambari.server.api.resources.ResourceInstanceFactory {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.api.resources.ResourceInstanceFactoryImpl.class);

    private static final java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, org.apache.ambari.server.api.resources.ResourceDefinition> resourceDefinitions = new java.util.HashMap<>();

    @java.lang.Override
    public org.apache.ambari.server.api.resources.ResourceInstance createResource(org.apache.ambari.server.controller.spi.Resource.Type type, java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> mapIds) {
        try {
            if (mapIds.containsKey(org.apache.ambari.server.controller.spi.Resource.Type.Host)) {
                java.lang.String hostName = mapIds.get(org.apache.ambari.server.controller.spi.Resource.Type.Host);
                if (hostName != null) {
                    mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.Host, hostName.toLowerCase());
                }
            }
        } catch (java.lang.Exception e) {
            org.apache.ambari.server.api.resources.ResourceInstanceFactoryImpl.LOG.error("Lowercase host name value in resource failed with error:" + e);
        }
        org.apache.ambari.server.api.resources.ResourceDefinition resourceDefinition = org.apache.ambari.server.api.resources.ResourceInstanceFactoryImpl.getResourceDefinition(type, mapIds);
        return new org.apache.ambari.server.api.query.QueryImpl(mapIds, resourceDefinition, org.apache.ambari.server.controller.utilities.ClusterControllerHelper.getClusterController());
    }

    public static void addResourceDefinition(org.apache.ambari.server.controller.spi.Resource.Type type, org.apache.ambari.server.api.resources.ResourceDefinition definition) {
        org.apache.ambari.server.api.resources.ResourceInstanceFactoryImpl.resourceDefinitions.put(type, definition);
    }

    public static org.apache.ambari.server.api.resources.ResourceDefinition getResourceDefinition(org.apache.ambari.server.controller.spi.Resource.Type type, java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> mapIds) {
        org.apache.ambari.server.api.resources.ResourceDefinition resourceDefinition;
        if (org.apache.ambari.server.api.resources.ResourceInstanceFactoryImpl.resourceDefinitions.containsKey(type)) {
            return org.apache.ambari.server.api.resources.ResourceInstanceFactoryImpl.resourceDefinitions.get(type);
        }
        switch (type.getInternalType()) {
            case Cluster :
                resourceDefinition = new org.apache.ambari.server.api.resources.ClusterResourceDefinition();
                break;
            case Service :
                resourceDefinition = new org.apache.ambari.server.api.resources.ServiceResourceDefinition();
                break;
            case Host :
                resourceDefinition = (mapIds.containsKey(org.apache.ambari.server.controller.spi.Resource.Type.Cluster)) ? new org.apache.ambari.server.api.resources.HostResourceDefinition() : new org.apache.ambari.server.api.resources.DetachedHostResourceDefinition();
                break;
            case Component :
                resourceDefinition = new org.apache.ambari.server.api.resources.ComponentResourceDefinition();
                break;
            case HostComponent :
                resourceDefinition = new org.apache.ambari.server.api.resources.HostComponentResourceDefinition();
                break;
            case Action :
                resourceDefinition = new org.apache.ambari.server.api.resources.ActionResourceDefinition();
                break;
            case Configuration :
                resourceDefinition = new org.apache.ambari.server.api.resources.ConfigurationResourceDefinition();
                break;
            case ServiceConfigVersion :
                resourceDefinition = new org.apache.ambari.server.api.resources.ServiceConfigVersionResourceDefinition();
                break;
            case Task :
                resourceDefinition = new org.apache.ambari.server.api.resources.TaskResourceDefinition();
                break;
            case User :
                resourceDefinition = new org.apache.ambari.server.api.resources.UserResourceDefinition();
                break;
            case UserAuthenticationSource :
                resourceDefinition = new org.apache.ambari.server.api.resources.SimpleResourceDefinition(org.apache.ambari.server.controller.spi.Resource.Type.UserAuthenticationSource, "source", "sources");
                break;
            case Group :
                resourceDefinition = new org.apache.ambari.server.api.resources.GroupResourceDefinition();
                break;
            case Member :
                resourceDefinition = new org.apache.ambari.server.api.resources.MemberResourceDefinition();
                break;
            case Mpack :
                resourceDefinition = new org.apache.ambari.server.api.resources.MpackResourceDefinition();
                break;
            case Request :
                resourceDefinition = new org.apache.ambari.server.api.resources.RequestResourceDefinition();
                break;
            case Stack :
                resourceDefinition = new org.apache.ambari.server.api.resources.StackResourceDefinition();
                break;
            case StackVersion :
                resourceDefinition = new org.apache.ambari.server.api.resources.StackVersionResourceDefinition();
                break;
            case StackLevelConfiguration :
                resourceDefinition = new org.apache.ambari.server.api.resources.StackLevelConfigurationResourceDefinition();
                break;
            case StackService :
                resourceDefinition = new org.apache.ambari.server.api.resources.StackServiceResourceDefinition();
                break;
            case StackServiceComponent :
                resourceDefinition = new org.apache.ambari.server.api.resources.StackServiceComponentResourceDefinition();
                break;
            case StackServiceComponentDependency :
                resourceDefinition = new org.apache.ambari.server.api.resources.StackDependencyResourceDefinition();
                break;
            case StackConfiguration :
                resourceDefinition = new org.apache.ambari.server.api.resources.StackConfigurationResourceDefinition();
                break;
            case StackConfigurationDependency :
                resourceDefinition = new org.apache.ambari.server.api.resources.StackConfigurationDependencyResourceDefinition();
                break;
            case Extension :
                resourceDefinition = new org.apache.ambari.server.api.resources.ExtensionResourceDefinition();
                break;
            case ExtensionVersion :
                resourceDefinition = new org.apache.ambari.server.api.resources.ExtensionVersionResourceDefinition();
                break;
            case ExtensionLink :
                resourceDefinition = new org.apache.ambari.server.api.resources.ExtensionLinkResourceDefinition();
                break;
            case OperatingSystem :
                resourceDefinition = new org.apache.ambari.server.api.resources.OperatingSystemResourceDefinition();
                break;
            case Repository :
                resourceDefinition = new org.apache.ambari.server.api.resources.RepositoryResourceDefinition();
                break;
            case DRFeed :
                resourceDefinition = new org.apache.ambari.server.api.resources.FeedResourceDefinition();
                break;
            case DRTargetCluster :
                resourceDefinition = new org.apache.ambari.server.api.resources.TargetClusterResourceDefinition();
                break;
            case DRInstance :
                resourceDefinition = new org.apache.ambari.server.api.resources.InstanceResourceDefinition();
                break;
            case Workflow :
                resourceDefinition = new org.apache.ambari.server.api.resources.WorkflowResourceDefinition();
                break;
            case Job :
                resourceDefinition = new org.apache.ambari.server.api.resources.JobResourceDefinition();
                break;
            case TaskAttempt :
                resourceDefinition = new org.apache.ambari.server.api.resources.TaskAttemptResourceDefinition();
                break;
            case RootService :
                resourceDefinition = new org.apache.ambari.server.api.resources.RootServiceResourceDefinition();
                break;
            case RootServiceComponent :
                resourceDefinition = new org.apache.ambari.server.api.resources.RootServiceComponentResourceDefinition();
                break;
            case RootServiceComponentConfiguration :
                resourceDefinition = new org.apache.ambari.server.api.resources.SimpleResourceDefinition(org.apache.ambari.server.controller.spi.Resource.Type.RootServiceComponentConfiguration, "configuration", "configurations", null, org.apache.ambari.server.api.services.RootServiceComponentConfigurationService.DIRECTIVES_MAP);
                break;
            case RootServiceHostComponent :
                resourceDefinition = new org.apache.ambari.server.api.resources.RootServiceHostComponentResourceDefinition();
                break;
            case ConfigGroup :
                resourceDefinition = new org.apache.ambari.server.api.resources.ConfigGroupResourceDefinition();
                break;
            case RequestSchedule :
                resourceDefinition = new org.apache.ambari.server.api.resources.RequestScheduleResourceDefinition();
                break;
            case View :
                resourceDefinition = new org.apache.ambari.server.api.resources.ViewResourceDefinition();
                break;
            case ViewVersion :
                resourceDefinition = new org.apache.ambari.server.api.resources.ViewVersionResourceDefinition();
                break;
            case ViewInstance :
                java.lang.String viewName = mapIds.get(org.apache.ambari.server.controller.spi.Resource.Type.View);
                java.lang.String version = mapIds.get(org.apache.ambari.server.controller.spi.Resource.Type.ViewVersion);
                java.util.Set<org.apache.ambari.server.api.resources.SubResourceDefinition> subResourceDefinitions = ((viewName == null) || (version == null)) ? java.util.Collections.emptySet() : org.apache.ambari.server.view.ViewRegistry.getInstance().getSubResourceDefinitions(viewName, version);
                resourceDefinition = new org.apache.ambari.server.api.resources.ViewInstanceResourceDefinition(subResourceDefinitions);
                break;
            case ViewURL :
                resourceDefinition = new org.apache.ambari.server.api.resources.ViewUrlResourceDefinition();
                break;
            case Blueprint :
                resourceDefinition = new org.apache.ambari.server.api.resources.BlueprintResourceDefinition();
                break;
            case Recommendation :
                resourceDefinition = new org.apache.ambari.server.api.resources.RecommendationResourceDefinition();
                break;
            case Validation :
                resourceDefinition = new org.apache.ambari.server.api.resources.ValidationResourceDefinition();
                break;
            case HostComponentProcess :
                resourceDefinition = new org.apache.ambari.server.api.resources.HostComponentProcessResourceDefinition();
                break;
            case Permission :
                resourceDefinition = new org.apache.ambari.server.api.resources.PermissionResourceDefinition();
                break;
            case Alert :
                resourceDefinition = new org.apache.ambari.server.api.resources.AlertResourceDefinition();
                break;
            case AlertDefinition :
                resourceDefinition = new org.apache.ambari.server.api.resources.AlertDefResourceDefinition();
                break;
            case AlertHistory :
                resourceDefinition = new org.apache.ambari.server.api.resources.AlertHistoryResourceDefinition();
                break;
            case AlertGroup :
                resourceDefinition = new org.apache.ambari.server.api.resources.AlertGroupResourceDefinition();
                break;
            case AlertTarget :
                resourceDefinition = new org.apache.ambari.server.api.resources.AlertTargetResourceDefinition();
                break;
            case AlertNotice :
                resourceDefinition = new org.apache.ambari.server.api.resources.AlertNoticeResourceDefinition();
                break;
            case AmbariPrivilege :
                resourceDefinition = new org.apache.ambari.server.api.resources.PrivilegeResourceDefinition(org.apache.ambari.server.controller.spi.Resource.Type.AmbariPrivilege);
                break;
            case ClusterPrivilege :
                resourceDefinition = new org.apache.ambari.server.api.resources.PrivilegeResourceDefinition(org.apache.ambari.server.controller.spi.Resource.Type.ClusterPrivilege);
                break;
            case ViewPrivilege :
                resourceDefinition = new org.apache.ambari.server.api.resources.PrivilegeResourceDefinition(org.apache.ambari.server.controller.spi.Resource.Type.ViewPrivilege);
                break;
            case UserPrivilege :
                resourceDefinition = new org.apache.ambari.server.api.resources.PrivilegeResourceDefinition(org.apache.ambari.server.controller.spi.Resource.Type.UserPrivilege);
                break;
            case GroupPrivilege :
                resourceDefinition = new org.apache.ambari.server.api.resources.PrivilegeResourceDefinition(org.apache.ambari.server.controller.spi.Resource.Type.GroupPrivilege);
                break;
            case ViewPermission :
                resourceDefinition = new org.apache.ambari.server.api.resources.ViewPermissionResourceDefinition();
                break;
            case ClientConfig :
                resourceDefinition = new org.apache.ambari.server.api.resources.ClientConfigResourceDefinition();
                break;
            case LdapSyncEvent :
                resourceDefinition = new org.apache.ambari.server.api.resources.LdapSyncEventResourceDefinition();
                break;
            case RepositoryVersion :
                resourceDefinition = new org.apache.ambari.server.api.resources.RepositoryVersionResourceDefinition();
                break;
            case CompatibleRepositoryVersion :
                resourceDefinition = new org.apache.ambari.server.api.resources.CompatibleRepositoryVersionDefinition();
                break;
            case HostStackVersion :
                resourceDefinition = new org.apache.ambari.server.api.resources.ComponentStackVersionResourceDefinition(org.apache.ambari.server.controller.spi.Resource.Type.HostStackVersion);
                break;
            case ClusterStackVersion :
                resourceDefinition = new org.apache.ambari.server.api.resources.ComponentStackVersionResourceDefinition(org.apache.ambari.server.controller.spi.Resource.Type.ClusterStackVersion);
                break;
            case Upgrade :
                resourceDefinition = new org.apache.ambari.server.api.resources.UpgradeResourceDefinition();
                break;
            case UpgradeGroup :
                resourceDefinition = new org.apache.ambari.server.api.resources.SimpleResourceDefinition(org.apache.ambari.server.controller.spi.Resource.Type.UpgradeGroup, "upgrade_group", "upgrade_groups", org.apache.ambari.server.controller.spi.Resource.Type.UpgradeItem);
                break;
            case UpgradeItem :
                resourceDefinition = new org.apache.ambari.server.api.resources.SimpleResourceDefinition(org.apache.ambari.server.controller.spi.Resource.Type.UpgradeItem, "upgrade_item", "upgrade_items", org.apache.ambari.server.controller.spi.Resource.Type.Task);
                break;
            case UpgradeSummary :
                resourceDefinition = new org.apache.ambari.server.api.resources.SimpleResourceDefinition(org.apache.ambari.server.controller.spi.Resource.Type.UpgradeSummary, "upgrade_summary", "upgrade_summary");
                break;
            case PreUpgradeCheck :
                resourceDefinition = new org.apache.ambari.server.api.resources.SimpleResourceDefinition(org.apache.ambari.server.controller.spi.Resource.Type.PreUpgradeCheck, "rolling_upgrade_check", "rolling_upgrade_checks");
                break;
            case Stage :
                resourceDefinition = new org.apache.ambari.server.api.resources.SimpleResourceDefinition(org.apache.ambari.server.controller.spi.Resource.Type.Stage, "stage", "stages", org.apache.ambari.server.controller.spi.Resource.Type.Task);
                break;
            case StackArtifact :
                resourceDefinition = new org.apache.ambari.server.api.resources.SimpleResourceDefinition(org.apache.ambari.server.controller.spi.Resource.Type.StackArtifact, "artifact", "artifacts");
                break;
            case Artifact :
                resourceDefinition = new org.apache.ambari.server.api.resources.SimpleResourceDefinition(org.apache.ambari.server.controller.spi.Resource.Type.Artifact, "artifact", "artifacts");
                break;
            case Theme :
                resourceDefinition = new org.apache.ambari.server.api.resources.SimpleResourceDefinition(org.apache.ambari.server.controller.spi.Resource.Type.Theme, "theme", "themes");
                break;
            case QuickLink :
                resourceDefinition = new org.apache.ambari.server.api.resources.SimpleResourceDefinition(org.apache.ambari.server.controller.spi.Resource.Type.QuickLink, "quicklink", "quicklinks");
                break;
            case Widget :
                resourceDefinition = new org.apache.ambari.server.api.resources.WidgetResourceDefinition();
                break;
            case WidgetLayout :
                resourceDefinition = new org.apache.ambari.server.api.resources.WidgetLayoutResourceDefinition();
                break;
            case ActiveWidgetLayout :
                resourceDefinition = new org.apache.ambari.server.api.resources.ActiveWidgetLayoutResourceDefinition();
                break;
            case HostKerberosIdentity :
                resourceDefinition = new org.apache.ambari.server.api.resources.HostKerberosIdentityResourceDefinition();
                break;
            case KerberosDescriptor :
                resourceDefinition = new org.apache.ambari.server.api.resources.SimpleResourceDefinition(org.apache.ambari.server.controller.spi.Resource.Type.KerberosDescriptor, "kerberos_descriptor", "kerberos_descriptors");
                break;
            case Credential :
                resourceDefinition = new org.apache.ambari.server.api.resources.CredentialResourceDefinition();
                break;
            case RoleAuthorization :
                resourceDefinition = new org.apache.ambari.server.api.resources.SimpleResourceDefinition(org.apache.ambari.server.controller.spi.Resource.Type.RoleAuthorization, "authorization", "authorizations");
                break;
            case UserAuthorization :
                resourceDefinition = new org.apache.ambari.server.api.resources.SimpleResourceDefinition(org.apache.ambari.server.controller.spi.Resource.Type.UserAuthorization, "authorization", "authorizations");
                break;
            case Setting :
                resourceDefinition = new org.apache.ambari.server.api.resources.SimpleResourceDefinition(org.apache.ambari.server.controller.spi.Resource.Type.Setting, "setting", "settings");
                break;
            case VersionDefinition :
                resourceDefinition = new org.apache.ambari.server.api.resources.VersionDefinitionResourceDefinition();
                break;
            case ClusterKerberosDescriptor :
                resourceDefinition = new org.apache.ambari.server.api.resources.SimpleResourceDefinition(org.apache.ambari.server.controller.spi.Resource.Type.ClusterKerberosDescriptor, "kerberos_descriptor", "kerberos_descriptors", null, java.util.Collections.singletonMap(org.apache.ambari.server.api.resources.BaseResourceDefinition.DirectiveType.READ, java.util.Arrays.asList(org.apache.ambari.server.controller.internal.ClusterKerberosDescriptorResourceProvider.DIRECTIVE_EVALUATE_WHEN_CLAUSE, org.apache.ambari.server.controller.internal.ClusterKerberosDescriptorResourceProvider.DIRECTIVE_ADDITIONAL_SERVICES)));
                break;
            case LoggingQuery :
                resourceDefinition = new org.apache.ambari.server.api.resources.LoggingResourceDefinition();
                break;
            case RemoteCluster :
                resourceDefinition = new org.apache.ambari.server.api.resources.RemoteClusterResourceDefinition();
                break;
            case Auth :
                resourceDefinition = new org.apache.ambari.server.api.resources.AuthResourceDefinition();
                break;
            default :
                throw new java.lang.IllegalArgumentException("Unsupported resource type: " + type);
        }
        return resourceDefinition;
    }
}