package org.apache.ambari.server.agent;
@com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY)
public class ExecutionCommand extends org.apache.ambari.server.agent.AgentCommand {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.agent.ExecutionCommand.class);

    public ExecutionCommand() {
        super(org.apache.ambari.server.agent.AgentCommand.AgentCommandType.EXECUTION_COMMAND);
    }

    @com.fasterxml.jackson.annotation.JsonProperty("clusterId")
    private java.lang.String clusterId;

    @com.google.gson.annotations.SerializedName("clusterName")
    @com.fasterxml.jackson.annotation.JsonProperty("clusterName")
    private java.lang.String clusterName;

    @com.google.gson.annotations.SerializedName("requestId")
    @com.fasterxml.jackson.annotation.JsonProperty("requestId")
    private long requestId;

    @com.google.gson.annotations.SerializedName("stageId")
    @com.fasterxml.jackson.annotation.JsonIgnore
    private long stageId;

    @com.google.gson.annotations.SerializedName("taskId")
    @com.fasterxml.jackson.annotation.JsonProperty("taskId")
    private long taskId;

    @com.google.gson.annotations.SerializedName("commandId")
    @com.fasterxml.jackson.annotation.JsonProperty("commandId")
    private java.lang.String commandId;

    @com.google.gson.annotations.SerializedName("hostname")
    @com.fasterxml.jackson.annotation.JsonIgnore
    private java.lang.String hostname;

    @com.google.gson.annotations.SerializedName("role")
    @com.fasterxml.jackson.annotation.JsonProperty("role")
    private java.lang.String role;

    @com.google.gson.annotations.SerializedName("hostLevelParams")
    @com.fasterxml.jackson.annotation.JsonIgnore
    private java.util.Map<java.lang.String, java.lang.String> hostLevelParams = new java.util.HashMap<>();

    @com.google.gson.annotations.SerializedName("clusterLevelParams")
    private java.util.Map<java.lang.String, java.lang.String> clusterLevelParams = new java.util.HashMap<>();

    @com.google.gson.annotations.SerializedName("roleParams")
    @com.fasterxml.jackson.annotation.JsonProperty("roleParams")
    private java.util.Map<java.lang.String, java.lang.String> roleParams = null;

    @com.google.gson.annotations.SerializedName("roleCommand")
    @com.fasterxml.jackson.annotation.JsonProperty("roleCommand")
    private org.apache.ambari.server.RoleCommand roleCommand;

    @com.google.gson.annotations.SerializedName("clusterHostInfo")
    private java.util.Map<java.lang.String, java.util.Set<java.lang.String>> clusterHostInfo = new java.util.HashMap<>();

    @com.google.gson.annotations.SerializedName("configurations")
    private java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configurations;

    @com.google.gson.annotations.SerializedName("forceRefreshConfigTagsBeforeExecution")
    @com.fasterxml.jackson.annotation.JsonIgnore
    private boolean overrideConfigs = false;

    @com.google.gson.annotations.SerializedName("commandParams")
    @com.fasterxml.jackson.annotation.JsonProperty("commandParams")
    private java.util.Map<java.lang.String, java.lang.String> commandParams = new java.util.HashMap<>();

    @com.google.gson.annotations.SerializedName("serviceName")
    @com.fasterxml.jackson.annotation.JsonProperty("serviceName")
    private java.lang.String serviceName;

    @com.google.gson.annotations.SerializedName("serviceType")
    @com.fasterxml.jackson.annotation.JsonIgnore
    private java.lang.String serviceType;

    @com.google.gson.annotations.SerializedName("componentName")
    @com.fasterxml.jackson.annotation.JsonIgnore
    private java.lang.String componentName;

    @com.google.gson.annotations.SerializedName("kerberosCommandParams")
    @com.fasterxml.jackson.annotation.JsonProperty("kerberosCommandParams")
    private java.util.List<java.util.Map<java.lang.String, java.lang.String>> kerberosCommandParams = new java.util.ArrayList<>();

    @com.google.gson.annotations.SerializedName("localComponents")
    @com.fasterxml.jackson.annotation.JsonIgnore
    private java.util.Set<java.lang.String> localComponents = new java.util.HashSet<>();

    @com.google.gson.annotations.SerializedName("availableServices")
    @com.fasterxml.jackson.annotation.JsonIgnore
    private java.util.Map<java.lang.String, java.lang.String> availableServices = new java.util.HashMap<>();

    @com.google.gson.annotations.SerializedName("credentialStoreEnabled")
    @com.fasterxml.jackson.annotation.JsonIgnore
    private java.lang.String credentialStoreEnabled;

    @com.google.gson.annotations.SerializedName("configuration_credentials")
    @com.fasterxml.jackson.annotation.JsonIgnore
    private java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configurationCredentials;

    @com.google.gson.annotations.SerializedName("repositoryFile")
    private org.apache.ambari.server.agent.CommandRepository commandRepository;

    @com.google.gson.annotations.SerializedName("componentVersionMap")
    private java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> componentVersionMap = new java.util.HashMap<>();

    @com.google.gson.annotations.SerializedName("upgradeSummary")
    private org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeSummary upgradeSummary;

    @com.google.gson.annotations.SerializedName("roleParameters")
    private java.util.Map<java.lang.String, java.lang.Object> roleParameters;

    @com.google.gson.annotations.SerializedName("useLatestConfigs")
    private java.lang.Boolean useLatestConfigs = null;

    public void setConfigurationCredentials(java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configurationCredentials) {
        this.configurationCredentials = configurationCredentials;
    }

    public java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> getConfigurationCredentials() {
        return configurationCredentials;
    }

    public java.lang.String getCommandId() {
        return commandId;
    }

    public void setRequestAndStage(long requestId, long stageId) {
        this.requestId = requestId;
        this.stageId = stageId;
        commandId = org.apache.ambari.server.utils.StageUtils.getActionId(requestId, stageId);
    }

    @java.lang.Override
    public boolean equals(java.lang.Object other) {
        if (!(other instanceof org.apache.ambari.server.agent.ExecutionCommand)) {
            return false;
        }
        org.apache.ambari.server.agent.ExecutionCommand o = ((org.apache.ambari.server.agent.ExecutionCommand) (other));
        return ((commandId.equals(o.commandId) && hostname.equals(o.hostname)) && role.equals(o.role)) && roleCommand.equals(o.roleCommand);
    }

    @java.lang.Override
    public java.lang.String toString() {
        try {
            return org.apache.ambari.server.utils.StageUtils.jaxbToString(this);
        } catch (java.lang.Exception ex) {
            org.apache.ambari.server.agent.ExecutionCommand.LOG.warn("Exception in json conversion", ex);
            return "Exception in json conversion";
        }
    }

    @java.lang.Override
    public int hashCode() {
        return ((hostname + commandId) + role).hashCode();
    }

    public long getTaskId() {
        return taskId;
    }

    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }

    public java.lang.String getRole() {
        return role;
    }

    public void setRole(java.lang.String role) {
        this.role = role;
    }

    public java.util.Map<java.lang.String, java.lang.String> getRoleParams() {
        return roleParams;
    }

    public void setRoleParams(java.util.Map<java.lang.String, java.lang.String> roleParams) {
        this.roleParams = roleParams;
    }

    public org.apache.ambari.server.RoleCommand getRoleCommand() {
        return roleCommand;
    }

    public void setRoleCommand(org.apache.ambari.server.RoleCommand cmd) {
        roleCommand = cmd;
    }

    public java.lang.String getClusterName() {
        return clusterName;
    }

    public void setClusterName(java.lang.String clusterName) {
        this.clusterName = clusterName;
    }

    public java.lang.String getHostname() {
        return hostname;
    }

    public void setHostname(java.lang.String hostname) {
        this.hostname = hostname;
    }

    public java.util.Map<java.lang.String, java.lang.String> getHostLevelParams() {
        return hostLevelParams;
    }

    public void setHostLevelParams(java.util.Map<java.lang.String, java.lang.String> params) {
        hostLevelParams = params;
    }

    public java.util.Map<java.lang.String, java.lang.String> getClusterLevelParams() {
        return clusterLevelParams;
    }

    public void setClusterLevelParams(java.util.Map<java.lang.String, java.lang.String> clusterLevelParams) {
        this.clusterLevelParams = clusterLevelParams;
    }

    public java.util.Map<java.lang.String, java.util.Set<java.lang.String>> getClusterHostInfo() {
        return clusterHostInfo;
    }

    public void setClusterHostInfo(java.util.Map<java.lang.String, java.util.Set<java.lang.String>> clusterHostInfo) {
        this.clusterHostInfo = clusterHostInfo;
    }

    public java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> getConfigurations() {
        return configurations;
    }

    public void setConfigurations(java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configurations) {
        this.configurations = configurations;
    }

    public boolean isOverrideConfigs() {
        return overrideConfigs;
    }

    public void setOverrideConfigs(boolean overrideConfigs) {
        this.overrideConfigs = overrideConfigs;
    }

    public java.util.Set<java.lang.String> getLocalComponents() {
        return localComponents;
    }

    public void setLocalComponents(java.util.Set<java.lang.String> localComponents) {
        this.localComponents = localComponents;
    }

    public java.util.Map<java.lang.String, java.lang.String> getCommandParams() {
        return commandParams;
    }

    public void setCommandParams(java.util.Map<java.lang.String, java.lang.String> commandParams) {
        this.commandParams = commandParams;
    }

    public java.lang.String getServiceName() {
        return serviceName;
    }

    public void setServiceName(java.lang.String serviceName) {
        this.serviceName = serviceName;
    }

    public java.lang.String getServiceType() {
        return serviceType;
    }

    public void setServiceType(java.lang.String serviceType) {
        this.serviceType = serviceType;
    }

    public java.lang.String getCredentialStoreEnabled() {
        return credentialStoreEnabled;
    }

    public void setCredentialStoreEnabled(java.lang.String credentialStoreEnabled) {
        this.credentialStoreEnabled = credentialStoreEnabled;
    }

    public java.lang.String getComponentName() {
        return componentName;
    }

    public void setComponentName(java.lang.String componentName) {
        this.componentName = componentName;
    }

    public java.util.List<java.util.Map<java.lang.String, java.lang.String>> getKerberosCommandParams() {
        return kerberosCommandParams;
    }

    public void setKerberosCommandParams(java.util.List<java.util.Map<java.lang.String, java.lang.String>> params) {
        kerberosCommandParams = params;
    }

    public java.lang.String getClusterId() {
        return clusterId;
    }

    public void setClusterId(java.lang.String clusterId) {
        this.clusterId = clusterId;
    }

    public org.apache.ambari.server.agent.CommandRepository getRepositoryFile() {
        return commandRepository;
    }

    public void setRepositoryFile(org.apache.ambari.server.agent.CommandRepository repository) {
        commandRepository = repository;
    }

    public java.util.Map<java.lang.String, java.lang.Object> getRoleParameters() {
        return roleParameters;
    }

    public void setRoleParameters(java.util.Map<java.lang.String, java.lang.Object> params) {
        roleParameters = params;
    }

    public java.lang.Boolean getUseLatestConfigs() {
        return useLatestConfigs;
    }

    public void setUseLatestConfigs(java.lang.Boolean useLatestConfigs) {
        this.useLatestConfigs = useLatestConfigs;
    }

    public interface KeyNames {
        java.lang.String COMMAND_TIMEOUT = "command_timeout";

        java.lang.String SCRIPT = "script";

        java.lang.String SCRIPT_TYPE = "script_type";

        java.lang.String SERVICE_PACKAGE_FOLDER = "service_package_folder";

        java.lang.String HOOKS_FOLDER = "hooks_folder";

        java.lang.String CUSTOM_FOLDER = "custom_folder";

        java.lang.String STACK_NAME = "stack_name";

        java.lang.String SERVICE_TYPE = "service_type";

        java.lang.String STACK_VERSION = "stack_version";

        @java.lang.Deprecated
        @org.apache.ambari.annotations.Experimental(feature = org.apache.ambari.annotations.ExperimentalFeature.PATCH_UPGRADES)
        java.lang.String SERVICE_REPO_INFO = "service_repo_info";

        java.lang.String PACKAGE_LIST = "package_list";

        java.lang.String JDK_LOCATION = "jdk_location";

        java.lang.String JAVA_HOME = "java_home";

        java.lang.String GPL_LICENSE_ACCEPTED = "gpl_license_accepted";

        java.lang.String AMBARI_JAVA_HOME = "ambari_java_home";

        java.lang.String AMBARI_JDK_NAME = "ambari_jdk_name";

        java.lang.String AMBARI_JCE_NAME = "ambari_jce_name";

        java.lang.String AMBARI_JAVA_VERSION = "ambari_java_version";

        java.lang.String JAVA_VERSION = "java_version";

        java.lang.String JDK_NAME = "jdk_name";

        java.lang.String JCE_NAME = "jce_name";

        java.lang.String UNLIMITED_KEY_JCE_REQUIRED = "unlimited_key_jce_required";

        java.lang.String MYSQL_JDBC_URL = "mysql_jdbc_url";

        java.lang.String ORACLE_JDBC_URL = "oracle_jdbc_url";

        java.lang.String DB_DRIVER_FILENAME = "db_driver_filename";

        java.lang.String CLIENTS_TO_UPDATE_CONFIGS = "clientsToUpdateConfigs";

        java.lang.String DB_NAME = "db_name";

        java.lang.String GLOBAL = "global";

        java.lang.String AMBARI_DB_RCA_URL = "ambari_db_rca_url";

        java.lang.String AMBARI_DB_RCA_DRIVER = "ambari_db_rca_driver";

        java.lang.String AMBARI_DB_RCA_USERNAME = "ambari_db_rca_username";

        java.lang.String AMBARI_DB_RCA_PASSWORD = "ambari_db_rca_password";

        java.lang.String COMPONENT_CATEGORY = "component_category";

        java.lang.String USER_LIST = "user_list";

        java.lang.String GROUP_LIST = "group_list";

        java.lang.String USER_GROUPS = "user_groups";

        java.lang.String BLUEPRINT_PROVISIONING_STATE = "blueprint_provisioning_state";

        java.lang.String NOT_MANAGED_HDFS_PATH_LIST = "not_managed_hdfs_path_list";

        java.lang.String REFRESH_TOPOLOGY = "refresh_topology";

        java.lang.String HOST_SYS_PREPPED = "host_sys_prepped";

        java.lang.String MAX_DURATION_OF_RETRIES = "max_duration_for_retries";

        java.lang.String COMMAND_RETRY_ENABLED = "command_retry_enabled";

        java.lang.String AGENT_STACK_RETRY_ON_UNAVAILABILITY = "agent_stack_retry_on_unavailability";

        java.lang.String AGENT_STACK_RETRY_COUNT = "agent_stack_retry_count";

        java.lang.String LOG_OUTPUT = "log_output";

        java.lang.String DFS_TYPE = "dfs_type";

        java.lang.String OVERRIDE_CONFIGS = "overrideConfigs";

        java.lang.String OVERRIDE_STACK_NAME = "overrideStackName";

        java.lang.String SERVICE_CHECK = "SERVICE_CHECK";

        java.lang.String CUSTOM_COMMAND = "custom_command";

        @java.lang.Deprecated
        @org.apache.ambari.annotations.Experimental(feature = org.apache.ambari.annotations.ExperimentalFeature.PATCH_UPGRADES)
        java.lang.String PACKAGE_VERSION = "package_version";

        java.lang.String UPGRADE_SUSPENDED = "upgrade_suspended";

        @java.lang.Deprecated
        @org.apache.ambari.annotations.Experimental(feature = org.apache.ambari.annotations.ExperimentalFeature.PATCH_UPGRADES)
        java.lang.String REPO_VERSION_ID = "repository_version_id";

        java.lang.String CLUSTER_NAME = "cluster_name";

        @org.apache.ambari.annotations.Experimental(feature = org.apache.ambari.annotations.ExperimentalFeature.PATCH_UPGRADES, comment = "Change this to reflect the component version")
        java.lang.String VERSION = "version";

        java.lang.String CLUSTER_VERSION_SUMMARY = "cluster_version_summary";
    }

    public java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> getComponentVersionMap() {
        return componentVersionMap;
    }

    public void setComponentVersions(org.apache.ambari.server.state.Cluster cluster) throws org.apache.ambari.server.AmbariException {
        componentVersionMap = cluster.getComponentVersionMap();
    }

    public void setUpgradeSummary(org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeSummary upgradeSummary) {
        this.upgradeSummary = upgradeSummary;
    }

    public org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeSummary getUpgradeSummary() {
        return upgradeSummary;
    }
}