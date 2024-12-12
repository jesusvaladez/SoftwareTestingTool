package org.apache.ambari.server.controller.spi;
public interface Resource {
    org.apache.ambari.server.controller.spi.Resource.Type getType();

    java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.Object>> getPropertiesMap();

    void setProperty(java.lang.String id, java.lang.Object value);

    void addCategory(java.lang.String id);

    java.lang.Object getPropertyValue(java.lang.String id);

    enum InternalType {

        Cluster,
        Service,
        Setting,
        Host,
        Component,
        HostComponent,
        Configuration,
        ServiceConfigVersion,
        ConfigGroup,
        Action,
        Request,
        RequestSchedule,
        Task,
        User,
        Group,
        Member,
        Mpack,
        Stack,
        StackVersion,
        ExtensionLink,
        Extension,
        ExtensionVersion,
        OperatingSystem,
        Repository,
        StackService,
        StackConfiguration,
        StackConfigurationDependency,
        StackServiceComponent,
        StackServiceComponentDependency,
        DRFeed,
        DRTargetCluster,
        DRInstance,
        Workflow,
        Job,
        TaskAttempt,
        RootService,
        RootServiceComponent,
        RootServiceComponentConfiguration,
        RootServiceHostComponent,
        View,
        ViewURL,
        ViewVersion,
        ViewInstance,
        Blueprint,
        Recommendation,
        Validation,
        HostComponentProcess,
        Permission,
        Alert,
        AlertDefinition,
        AlertHistory,
        AlertGroup,
        AlertTarget,
        AlertNotice,
        AmbariPrivilege,
        ClusterPrivilege,
        ViewPrivilege,
        ViewPermission,
        ClientConfig,
        StackLevelConfiguration,
        LdapSyncEvent,
        UserPrivilege,
        UserAuthenticationSource,
        GroupPrivilege,
        RepositoryVersion,
        CompatibleRepositoryVersion,
        ClusterStackVersion,
        HostStackVersion,
        Upgrade,
        UpgradeGroup,
        UpgradeItem,
        UpgradeSummary,
        PreUpgradeCheck,
        Stage,
        StackArtifact,
        Artifact,
        Widget,
        WidgetLayout,
        ActiveWidgetLayout,
        Theme,
        QuickLink,
        HostKerberosIdentity,
        Credential,
        KerberosDescriptor,
        RoleAuthorization,
        UserAuthorization,
        VersionDefinition,
        ClusterKerberosDescriptor,
        LoggingQuery,
        RemoteCluster,
        Auth;
        private org.apache.ambari.server.controller.spi.Resource.Type getType() {
            java.lang.String name = name();
            org.apache.ambari.server.controller.spi.Resource.Type type = org.apache.ambari.server.controller.spi.Resource.Type.getType(name);
            return type == null ? new org.apache.ambari.server.controller.spi.Resource.Type(name(), ordinal()) : type;
        }
    }

    final class Type implements java.lang.Comparable<org.apache.ambari.server.controller.spi.Resource.Type> {
        private static java.util.Map<java.lang.String, org.apache.ambari.server.controller.spi.Resource.Type> types = new java.util.concurrent.ConcurrentHashMap<>();

        private static int nextOrdinal = 10000;

        public static final org.apache.ambari.server.controller.spi.Resource.Type Cluster = org.apache.ambari.server.controller.spi.Resource.InternalType.Cluster.getType();

        public static final org.apache.ambari.server.controller.spi.Resource.Type Service = org.apache.ambari.server.controller.spi.Resource.InternalType.Service.getType();

        public static final org.apache.ambari.server.controller.spi.Resource.Type Setting = org.apache.ambari.server.controller.spi.Resource.InternalType.Setting.getType();

        public static final org.apache.ambari.server.controller.spi.Resource.Type Host = org.apache.ambari.server.controller.spi.Resource.InternalType.Host.getType();

        public static final org.apache.ambari.server.controller.spi.Resource.Type Component = org.apache.ambari.server.controller.spi.Resource.InternalType.Component.getType();

        public static final org.apache.ambari.server.controller.spi.Resource.Type HostComponent = org.apache.ambari.server.controller.spi.Resource.InternalType.HostComponent.getType();

        public static final org.apache.ambari.server.controller.spi.Resource.Type Configuration = org.apache.ambari.server.controller.spi.Resource.InternalType.Configuration.getType();

        public static final org.apache.ambari.server.controller.spi.Resource.Type ServiceConfigVersion = org.apache.ambari.server.controller.spi.Resource.InternalType.ServiceConfigVersion.getType();

        public static final org.apache.ambari.server.controller.spi.Resource.Type ConfigGroup = org.apache.ambari.server.controller.spi.Resource.InternalType.ConfigGroup.getType();

        public static final org.apache.ambari.server.controller.spi.Resource.Type Action = org.apache.ambari.server.controller.spi.Resource.InternalType.Action.getType();

        public static final org.apache.ambari.server.controller.spi.Resource.Type Request = org.apache.ambari.server.controller.spi.Resource.InternalType.Request.getType();

        public static final org.apache.ambari.server.controller.spi.Resource.Type RequestSchedule = org.apache.ambari.server.controller.spi.Resource.InternalType.RequestSchedule.getType();

        public static final org.apache.ambari.server.controller.spi.Resource.Type Task = org.apache.ambari.server.controller.spi.Resource.InternalType.Task.getType();

        public static final org.apache.ambari.server.controller.spi.Resource.Type User = org.apache.ambari.server.controller.spi.Resource.InternalType.User.getType();

        public static final org.apache.ambari.server.controller.spi.Resource.Type Group = org.apache.ambari.server.controller.spi.Resource.InternalType.Group.getType();

        public static final org.apache.ambari.server.controller.spi.Resource.Type Member = org.apache.ambari.server.controller.spi.Resource.InternalType.Member.getType();

        public static final org.apache.ambari.server.controller.spi.Resource.Type Mpack = org.apache.ambari.server.controller.spi.Resource.InternalType.Mpack.getType();

        public static final org.apache.ambari.server.controller.spi.Resource.Type Stack = org.apache.ambari.server.controller.spi.Resource.InternalType.Stack.getType();

        public static final org.apache.ambari.server.controller.spi.Resource.Type StackVersion = org.apache.ambari.server.controller.spi.Resource.InternalType.StackVersion.getType();

        public static final org.apache.ambari.server.controller.spi.Resource.Type ExtensionLink = org.apache.ambari.server.controller.spi.Resource.InternalType.ExtensionLink.getType();

        public static final org.apache.ambari.server.controller.spi.Resource.Type Extension = org.apache.ambari.server.controller.spi.Resource.InternalType.Extension.getType();

        public static final org.apache.ambari.server.controller.spi.Resource.Type ExtensionVersion = org.apache.ambari.server.controller.spi.Resource.InternalType.ExtensionVersion.getType();

        public static final org.apache.ambari.server.controller.spi.Resource.Type OperatingSystem = org.apache.ambari.server.controller.spi.Resource.InternalType.OperatingSystem.getType();

        public static final org.apache.ambari.server.controller.spi.Resource.Type Repository = org.apache.ambari.server.controller.spi.Resource.InternalType.Repository.getType();

        public static final org.apache.ambari.server.controller.spi.Resource.Type StackService = org.apache.ambari.server.controller.spi.Resource.InternalType.StackService.getType();

        public static final org.apache.ambari.server.controller.spi.Resource.Type StackConfiguration = org.apache.ambari.server.controller.spi.Resource.InternalType.StackConfiguration.getType();

        public static final org.apache.ambari.server.controller.spi.Resource.Type StackConfigurationDependency = org.apache.ambari.server.controller.spi.Resource.InternalType.StackConfigurationDependency.getType();

        public static final org.apache.ambari.server.controller.spi.Resource.Type StackServiceComponent = org.apache.ambari.server.controller.spi.Resource.InternalType.StackServiceComponent.getType();

        public static final org.apache.ambari.server.controller.spi.Resource.Type StackServiceComponentDependency = org.apache.ambari.server.controller.spi.Resource.InternalType.StackServiceComponentDependency.getType();

        public static final org.apache.ambari.server.controller.spi.Resource.Type DRFeed = org.apache.ambari.server.controller.spi.Resource.InternalType.DRFeed.getType();

        public static final org.apache.ambari.server.controller.spi.Resource.Type DRTargetCluster = org.apache.ambari.server.controller.spi.Resource.InternalType.DRTargetCluster.getType();

        public static final org.apache.ambari.server.controller.spi.Resource.Type DRInstance = org.apache.ambari.server.controller.spi.Resource.InternalType.DRInstance.getType();

        public static final org.apache.ambari.server.controller.spi.Resource.Type Workflow = org.apache.ambari.server.controller.spi.Resource.InternalType.Workflow.getType();

        public static final org.apache.ambari.server.controller.spi.Resource.Type Job = org.apache.ambari.server.controller.spi.Resource.InternalType.Job.getType();

        public static final org.apache.ambari.server.controller.spi.Resource.Type TaskAttempt = org.apache.ambari.server.controller.spi.Resource.InternalType.TaskAttempt.getType();

        public static final org.apache.ambari.server.controller.spi.Resource.Type RootService = org.apache.ambari.server.controller.spi.Resource.InternalType.RootService.getType();

        public static final org.apache.ambari.server.controller.spi.Resource.Type RootServiceComponent = org.apache.ambari.server.controller.spi.Resource.InternalType.RootServiceComponent.getType();

        public static final org.apache.ambari.server.controller.spi.Resource.Type RootServiceComponentConfiguration = org.apache.ambari.server.controller.spi.Resource.InternalType.RootServiceComponentConfiguration.getType();

        public static final org.apache.ambari.server.controller.spi.Resource.Type RootServiceHostComponent = org.apache.ambari.server.controller.spi.Resource.InternalType.RootServiceHostComponent.getType();

        public static final org.apache.ambari.server.controller.spi.Resource.Type View = org.apache.ambari.server.controller.spi.Resource.InternalType.View.getType();

        public static final org.apache.ambari.server.controller.spi.Resource.Type ViewURL = org.apache.ambari.server.controller.spi.Resource.InternalType.ViewURL.getType();

        public static final org.apache.ambari.server.controller.spi.Resource.Type ViewVersion = org.apache.ambari.server.controller.spi.Resource.InternalType.ViewVersion.getType();

        public static final org.apache.ambari.server.controller.spi.Resource.Type ViewInstance = org.apache.ambari.server.controller.spi.Resource.InternalType.ViewInstance.getType();

        public static final org.apache.ambari.server.controller.spi.Resource.Type Blueprint = org.apache.ambari.server.controller.spi.Resource.InternalType.Blueprint.getType();

        public static final org.apache.ambari.server.controller.spi.Resource.Type Recommendation = org.apache.ambari.server.controller.spi.Resource.InternalType.Recommendation.getType();

        public static final org.apache.ambari.server.controller.spi.Resource.Type Validation = org.apache.ambari.server.controller.spi.Resource.InternalType.Validation.getType();

        public static final org.apache.ambari.server.controller.spi.Resource.Type HostComponentProcess = org.apache.ambari.server.controller.spi.Resource.InternalType.HostComponentProcess.getType();

        public static final org.apache.ambari.server.controller.spi.Resource.Type Permission = org.apache.ambari.server.controller.spi.Resource.InternalType.Permission.getType();

        public static final org.apache.ambari.server.controller.spi.Resource.Type Alert = org.apache.ambari.server.controller.spi.Resource.InternalType.Alert.getType();

        public static final org.apache.ambari.server.controller.spi.Resource.Type AlertDefinition = org.apache.ambari.server.controller.spi.Resource.InternalType.AlertDefinition.getType();

        public static final org.apache.ambari.server.controller.spi.Resource.Type AlertHistory = org.apache.ambari.server.controller.spi.Resource.InternalType.AlertHistory.getType();

        public static final org.apache.ambari.server.controller.spi.Resource.Type AlertGroup = org.apache.ambari.server.controller.spi.Resource.InternalType.AlertGroup.getType();

        public static final org.apache.ambari.server.controller.spi.Resource.Type AlertTarget = org.apache.ambari.server.controller.spi.Resource.InternalType.AlertTarget.getType();

        public static final org.apache.ambari.server.controller.spi.Resource.Type AlertNotice = org.apache.ambari.server.controller.spi.Resource.InternalType.AlertNotice.getType();

        public static final org.apache.ambari.server.controller.spi.Resource.Type AmbariPrivilege = org.apache.ambari.server.controller.spi.Resource.InternalType.AmbariPrivilege.getType();

        public static final org.apache.ambari.server.controller.spi.Resource.Type ClusterPrivilege = org.apache.ambari.server.controller.spi.Resource.InternalType.ClusterPrivilege.getType();

        public static final org.apache.ambari.server.controller.spi.Resource.Type ViewPrivilege = org.apache.ambari.server.controller.spi.Resource.InternalType.ViewPrivilege.getType();

        public static final org.apache.ambari.server.controller.spi.Resource.Type ViewPermission = org.apache.ambari.server.controller.spi.Resource.InternalType.ViewPermission.getType();

        public static final org.apache.ambari.server.controller.spi.Resource.Type ClientConfig = org.apache.ambari.server.controller.spi.Resource.InternalType.ClientConfig.getType();

        public static final org.apache.ambari.server.controller.spi.Resource.Type StackLevelConfiguration = org.apache.ambari.server.controller.spi.Resource.InternalType.StackLevelConfiguration.getType();

        public static final org.apache.ambari.server.controller.spi.Resource.Type LdapSyncEvent = org.apache.ambari.server.controller.spi.Resource.InternalType.LdapSyncEvent.getType();

        public static final org.apache.ambari.server.controller.spi.Resource.Type UserPrivilege = org.apache.ambari.server.controller.spi.Resource.InternalType.UserPrivilege.getType();

        public static final org.apache.ambari.server.controller.spi.Resource.Type UserAuthenticationSource = org.apache.ambari.server.controller.spi.Resource.InternalType.UserAuthenticationSource.getType();

        public static final org.apache.ambari.server.controller.spi.Resource.Type GroupPrivilege = org.apache.ambari.server.controller.spi.Resource.InternalType.GroupPrivilege.getType();

        public static final org.apache.ambari.server.controller.spi.Resource.Type RepositoryVersion = org.apache.ambari.server.controller.spi.Resource.InternalType.RepositoryVersion.getType();

        public static final org.apache.ambari.server.controller.spi.Resource.Type CompatibleRepositoryVersion = org.apache.ambari.server.controller.spi.Resource.InternalType.CompatibleRepositoryVersion.getType();

        public static final org.apache.ambari.server.controller.spi.Resource.Type ClusterStackVersion = org.apache.ambari.server.controller.spi.Resource.InternalType.ClusterStackVersion.getType();

        public static final org.apache.ambari.server.controller.spi.Resource.Type HostStackVersion = org.apache.ambari.server.controller.spi.Resource.InternalType.HostStackVersion.getType();

        public static final org.apache.ambari.server.controller.spi.Resource.Type Upgrade = org.apache.ambari.server.controller.spi.Resource.InternalType.Upgrade.getType();

        public static final org.apache.ambari.server.controller.spi.Resource.Type UpgradeGroup = org.apache.ambari.server.controller.spi.Resource.InternalType.UpgradeGroup.getType();

        public static final org.apache.ambari.server.controller.spi.Resource.Type UpgradeItem = org.apache.ambari.server.controller.spi.Resource.InternalType.UpgradeItem.getType();

        public static final org.apache.ambari.server.controller.spi.Resource.Type UpgradeSummary = org.apache.ambari.server.controller.spi.Resource.InternalType.UpgradeSummary.getType();

        public static final org.apache.ambari.server.controller.spi.Resource.Type PreUpgradeCheck = org.apache.ambari.server.controller.spi.Resource.InternalType.PreUpgradeCheck.getType();

        public static final org.apache.ambari.server.controller.spi.Resource.Type Stage = org.apache.ambari.server.controller.spi.Resource.InternalType.Stage.getType();

        public static final org.apache.ambari.server.controller.spi.Resource.Type StackArtifact = org.apache.ambari.server.controller.spi.Resource.InternalType.StackArtifact.getType();

        public static final org.apache.ambari.server.controller.spi.Resource.Type Artifact = org.apache.ambari.server.controller.spi.Resource.InternalType.Artifact.getType();

        public static final org.apache.ambari.server.controller.spi.Resource.Type Theme = org.apache.ambari.server.controller.spi.Resource.InternalType.Theme.getType();

        public static final org.apache.ambari.server.controller.spi.Resource.Type QuickLink = org.apache.ambari.server.controller.spi.Resource.InternalType.QuickLink.getType();

        public static final org.apache.ambari.server.controller.spi.Resource.Type Widget = org.apache.ambari.server.controller.spi.Resource.InternalType.Widget.getType();

        public static final org.apache.ambari.server.controller.spi.Resource.Type WidgetLayout = org.apache.ambari.server.controller.spi.Resource.InternalType.WidgetLayout.getType();

        public static final org.apache.ambari.server.controller.spi.Resource.Type ActiveWidgetLayout = org.apache.ambari.server.controller.spi.Resource.InternalType.ActiveWidgetLayout.getType();

        public static final org.apache.ambari.server.controller.spi.Resource.Type HostKerberosIdentity = org.apache.ambari.server.controller.spi.Resource.InternalType.HostKerberosIdentity.getType();

        public static final org.apache.ambari.server.controller.spi.Resource.Type Credential = org.apache.ambari.server.controller.spi.Resource.InternalType.Credential.getType();

        public static final org.apache.ambari.server.controller.spi.Resource.Type KerberosDescriptor = org.apache.ambari.server.controller.spi.Resource.InternalType.KerberosDescriptor.getType();

        public static final org.apache.ambari.server.controller.spi.Resource.Type RoleAuthorization = org.apache.ambari.server.controller.spi.Resource.InternalType.RoleAuthorization.getType();

        public static final org.apache.ambari.server.controller.spi.Resource.Type UserAuthorization = org.apache.ambari.server.controller.spi.Resource.InternalType.UserAuthorization.getType();

        public static final org.apache.ambari.server.controller.spi.Resource.Type VersionDefinition = org.apache.ambari.server.controller.spi.Resource.InternalType.VersionDefinition.getType();

        public static final org.apache.ambari.server.controller.spi.Resource.Type ClusterKerberosDescriptor = org.apache.ambari.server.controller.spi.Resource.InternalType.ClusterKerberosDescriptor.getType();

        public static final org.apache.ambari.server.controller.spi.Resource.Type LoggingQuery = org.apache.ambari.server.controller.spi.Resource.InternalType.LoggingQuery.getType();

        public static final org.apache.ambari.server.controller.spi.Resource.Type RemoteCluster = org.apache.ambari.server.controller.spi.Resource.InternalType.RemoteCluster.getType();

        public static final org.apache.ambari.server.controller.spi.Resource.Type Auth = org.apache.ambari.server.controller.spi.Resource.InternalType.Auth.getType();

        private final java.lang.String name;

        private final int ordinal;

        private Type(java.lang.String name, int ordinal) {
            assert name != null;
            this.name = name;
            this.ordinal = ordinal;
            org.apache.ambari.server.controller.spi.Resource.Type.setType(name, this);
        }

        public Type(java.lang.String name) {
            this(name, org.apache.ambari.server.controller.spi.Resource.Type.getNextOrdinal());
        }

        public final int ordinal() {
            return ordinal;
        }

        public java.lang.String name() {
            return name;
        }

        public boolean isInternalType() {
            return ordinal < org.apache.ambari.server.controller.spi.Resource.InternalType.values().length;
        }

        public org.apache.ambari.server.controller.spi.Resource.InternalType getInternalType() {
            if (isInternalType()) {
                return org.apache.ambari.server.controller.spi.Resource.InternalType.values()[ordinal];
            }
            throw new java.lang.UnsupportedOperationException(name + " is not an internal type.");
        }

        public static org.apache.ambari.server.controller.spi.Resource.Type valueOf(java.lang.String name) {
            org.apache.ambari.server.controller.spi.Resource.Type type = org.apache.ambari.server.controller.spi.Resource.Type.types.get(name);
            if (type == null) {
                throw new java.lang.IllegalArgumentException(name + " is not a type.");
            }
            return type;
        }

        public static org.apache.ambari.server.controller.spi.Resource.Type[] values() {
            return org.apache.ambari.server.controller.spi.Resource.Type.types.values().toArray(new org.apache.ambari.server.controller.spi.Resource.Type[org.apache.ambari.server.controller.spi.Resource.Type.types.size()]);
        }

        @java.lang.Override
        public boolean equals(java.lang.Object o) {
            if (this == o) {
                return true;
            }
            if ((o == null) || (getClass() != o.getClass())) {
                return false;
            }
            org.apache.ambari.server.controller.spi.Resource.Type type = ((org.apache.ambari.server.controller.spi.Resource.Type) (o));
            return (ordinal == type.ordinal) && name.equals(type.name);
        }

        @java.lang.Override
        public int hashCode() {
            int result = name.hashCode();
            result = (31 * result) + ordinal;
            return result;
        }

        @java.lang.Override
        protected java.lang.Object clone() throws java.lang.CloneNotSupportedException {
            throw new java.lang.CloneNotSupportedException();
        }

        @java.lang.Override
        public java.lang.String toString() {
            return name;
        }

        @java.lang.Override
        public int compareTo(org.apache.ambari.server.controller.spi.Resource.Type type) {
            return ordinal - type.ordinal();
        }

        private static synchronized int getNextOrdinal() {
            return org.apache.ambari.server.controller.spi.Resource.Type.nextOrdinal++;
        }

        private static void setType(java.lang.String name, org.apache.ambari.server.controller.spi.Resource.Type type) {
            org.apache.ambari.server.controller.spi.Resource.Type.types.put(name, type);
        }

        private static org.apache.ambari.server.controller.spi.Resource.Type getType(java.lang.String name) {
            return org.apache.ambari.server.controller.spi.Resource.Type.types.get(name);
        }
    }
}