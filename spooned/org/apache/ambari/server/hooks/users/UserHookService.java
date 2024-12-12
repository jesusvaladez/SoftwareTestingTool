package org.apache.ambari.server.hooks.users;
import org.codehaus.jackson.map.ObjectMapper;
@javax.inject.Singleton
public class UserHookService implements org.apache.ambari.server.hooks.HookService {
    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.hooks.users.UserHookService.class);

    private static final java.lang.String POST_USER_CREATION_REQUEST_CONTEXT = "Post user creation hook for [ %s ] users";

    private static final java.lang.String INPUT_FILE_PREFIX = "user_hook_input_%s.csv";

    private static final java.lang.String HADOOP_ENV = "hadoop-env";

    private static final java.lang.String HDFS_USER_KEYTAB = "hdfs_user_keytab";

    private static final java.lang.String HDFS_PRINCIPAL_NAME = "hdfs_principal_name";

    @javax.inject.Inject
    private org.apache.ambari.server.hooks.AmbariEventFactory eventFactory;

    @javax.inject.Inject
    private org.apache.ambari.server.events.publishers.AmbariEventPublisher ambariEventPublisher;

    @javax.inject.Inject
    private org.apache.ambari.server.actionmanager.ActionManager actionManager;

    @javax.inject.Inject
    private org.apache.ambari.server.actionmanager.RequestFactory requestFactory;

    @javax.inject.Inject
    private org.apache.ambari.server.actionmanager.StageFactory stageFactory;

    @javax.inject.Inject
    private org.apache.ambari.server.configuration.Configuration configuration;

    @javax.inject.Inject
    private org.apache.ambari.server.state.Clusters clusters;

    @javax.inject.Inject
    private org.codehaus.jackson.map.ObjectMapper objectMapper;

    @javax.inject.Inject
    private void register() {
        ambariEventPublisher.register(this);
    }

    @java.lang.Override
    public boolean execute(org.apache.ambari.server.hooks.HookContext hookContext) {
        org.apache.ambari.server.hooks.users.UserHookService.LOGGER.info("Executing user hook for {}. ", hookContext);
        org.apache.ambari.server.hooks.users.PostUserCreationHookContext hookCtx = validateHookInput(hookContext);
        if (!checkUserHookPrerequisites()) {
            org.apache.ambari.server.hooks.users.UserHookService.LOGGER.warn("Prerequisites for user hook are not satisfied. Hook not triggered");
            return false;
        }
        if (hookCtx.getUserGroups().isEmpty()) {
            org.apache.ambari.server.hooks.users.UserHookService.LOGGER.info("No users found for executing user hook for");
            return false;
        }
        org.apache.ambari.server.hooks.users.UserCreatedEvent userCreatedEvent = ((org.apache.ambari.server.hooks.users.UserCreatedEvent) (eventFactory.newUserCreatedEvent(hookCtx)));
        org.apache.ambari.server.hooks.users.UserHookService.LOGGER.info("Triggering user hook for user: {}", hookContext);
        ambariEventPublisher.publish(userCreatedEvent);
        return true;
    }

    @com.google.common.eventbus.Subscribe
    public void onUserCreatedEvent(org.apache.ambari.server.hooks.users.UserCreatedEvent event) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.hooks.users.UserHookService.LOGGER.info("Preparing hook execution for event: {}", event);
        try {
            org.apache.ambari.server.controller.internal.RequestStageContainer requestStageContainer = new org.apache.ambari.server.controller.internal.RequestStageContainer(actionManager.getNextRequestId(), null, requestFactory, actionManager);
            org.apache.ambari.server.hooks.users.UserHookService.ClusterData clsData = getClusterData();
            org.apache.ambari.server.hooks.users.PostUserCreationHookContext ctx = ((org.apache.ambari.server.hooks.users.PostUserCreationHookContext) (event.getContext()));
            java.lang.String stageContextText = java.lang.String.format(org.apache.ambari.server.hooks.users.UserHookService.POST_USER_CREATION_REQUEST_CONTEXT, ctx.getUserGroups().size());
            org.apache.ambari.server.actionmanager.Stage stage = stageFactory.createNew(requestStageContainer.getId(), (configuration.getServerTempDir() + java.io.File.pathSeparatorChar) + requestStageContainer.getId(), clsData.getClusterName(), clsData.getClusterId(), stageContextText, "{}", "{}");
            stage.setStageId(requestStageContainer.getLastStageId());
            org.apache.ambari.server.state.svccomphost.ServiceComponentHostServerActionEvent serverActionEvent = new org.apache.ambari.server.state.svccomphost.ServiceComponentHostServerActionEvent("ambari-server-host", java.lang.System.currentTimeMillis());
            java.util.Map<java.lang.String, java.lang.String> commandParams = prepareCommandParams(ctx, clsData);
            stage.addServerActionCommand(org.apache.ambari.server.serveraction.users.PostUserCreationHookServerAction.class.getName(), "ambari", org.apache.ambari.server.Role.AMBARI_SERVER_ACTION, org.apache.ambari.server.RoleCommand.EXECUTE, clsData.getClusterName(), serverActionEvent, commandParams, stageContextText, null, null, false, false);
            requestStageContainer.addStages(java.util.Collections.singletonList(stage));
            requestStageContainer.persist();
        } catch (java.io.IOException e) {
            org.apache.ambari.server.hooks.users.UserHookService.LOGGER.error("Failed to assemble stage for server action. Event: {}", event);
            throw new org.apache.ambari.server.AmbariException("Failed to assemble stage for server action", e);
        }
    }

    private java.util.Map<java.lang.String, java.lang.String> prepareCommandParams(org.apache.ambari.server.hooks.users.PostUserCreationHookContext context, org.apache.ambari.server.hooks.users.UserHookService.ClusterData clusterData) throws java.io.IOException {
        java.util.Map<java.lang.String, java.lang.String> commandParams = new java.util.HashMap<>();
        commandParams.put(org.apache.ambari.server.hooks.users.UserHookParams.SCRIPT.param(), configuration.getProperty(org.apache.ambari.server.configuration.Configuration.POST_USER_CREATION_HOOK));
        commandParams.put(org.apache.ambari.server.hooks.users.UserHookParams.CLUSTER_ID.param(), java.lang.String.valueOf(clusterData.getClusterId()));
        commandParams.put(org.apache.ambari.server.hooks.users.UserHookParams.CLUSTER_NAME.param(), clusterData.getClusterName());
        commandParams.put(org.apache.ambari.server.hooks.users.UserHookParams.CLUSTER_SECURITY_TYPE.param(), clusterData.getSecurityType());
        commandParams.put(org.apache.ambari.server.hooks.users.UserHookParams.CMD_HDFS_KEYTAB.param(), clusterData.getKeytab());
        commandParams.put(org.apache.ambari.server.hooks.users.UserHookParams.CMD_HDFS_PRINCIPAL.param(), clusterData.getPrincipal());
        commandParams.put(org.apache.ambari.server.hooks.users.UserHookParams.CMD_HDFS_USER.param(), clusterData.getHdfsUser());
        commandParams.put(org.apache.ambari.server.hooks.users.UserHookParams.CMD_INPUT_FILE.param(), generateInputFileName());
        commandParams.put(org.apache.ambari.server.hooks.users.UserHookParams.PAYLOAD.param(), objectMapper.writeValueAsString(context.getUserGroups()));
        return commandParams;
    }

    private java.lang.String generateInputFileName() {
        java.lang.String inputFileName = java.lang.String.format(org.apache.ambari.server.hooks.users.UserHookService.INPUT_FILE_PREFIX, java.util.Calendar.getInstance().getTimeInMillis());
        org.apache.ambari.server.hooks.users.UserHookService.LOGGER.debug("Command input file name: {}", inputFileName);
        return (configuration.getServerTempDir() + java.io.File.separator) + inputFileName;
    }

    private boolean checkUserHookPrerequisites() {
        if (!configuration.isUserHookEnabled()) {
            org.apache.ambari.server.hooks.users.UserHookService.LOGGER.warn("Post user creation hook disabled.");
            return false;
        }
        if (clusters.getClusters().isEmpty()) {
            org.apache.ambari.server.hooks.users.UserHookService.LOGGER.warn("There's no cluster found. Post user creation hook won't be executed.");
            return false;
        }
        return true;
    }

    private org.apache.ambari.server.hooks.users.PostUserCreationHookContext validateHookInput(org.apache.ambari.server.hooks.HookContext hookContext) {
        return ((org.apache.ambari.server.hooks.users.PostUserCreationHookContext) (hookContext));
    }

    private org.apache.ambari.server.hooks.users.UserHookService.ClusterData getClusterData() {
        java.lang.String keyTab = "NA";
        java.lang.String principal = "NA";
        java.util.Map.Entry<java.lang.String, org.apache.ambari.server.state.Cluster> clustersMapEntry = clusters.getClusters().entrySet().iterator().next();
        org.apache.ambari.server.state.Cluster cluster = clustersMapEntry.getValue();
        switch (cluster.getSecurityType()) {
            case KERBEROS :
                java.util.Map<java.lang.String, java.lang.String> hadoopEnv = cluster.getDesiredConfigByType(org.apache.ambari.server.hooks.users.UserHookService.HADOOP_ENV).getProperties();
                keyTab = hadoopEnv.get(org.apache.ambari.server.hooks.users.UserHookService.HDFS_USER_KEYTAB);
                principal = hadoopEnv.get(org.apache.ambari.server.hooks.users.UserHookService.HDFS_PRINCIPAL_NAME);
                break;
            case NONE :
            default :
                org.apache.ambari.server.hooks.users.UserHookService.LOGGER.debug("The cluster security is not set. Security type: {}", cluster.getSecurityType());
                break;
        }
        return new org.apache.ambari.server.hooks.users.UserHookService.ClusterData(cluster.getClusterName(), cluster.getClusterId(), cluster.getSecurityType().name(), principal, keyTab, getHdfsUser(cluster));
    }

    private java.lang.String getHdfsUser(org.apache.ambari.server.state.Cluster cluster) {
        java.lang.String hdfsUser = cluster.getDesiredConfigByType("hadoop-env").getProperties().get("hdfs_user");
        return hdfsUser;
    }

    private static final class ClusterData {
        private java.lang.String clusterName;

        private java.lang.Long clusterId;

        private java.lang.String securityType;

        private java.lang.String principal;

        private java.lang.String keytab;

        private java.lang.String hdfsUser;

        public ClusterData(java.lang.String clusterName, java.lang.Long clusterId, java.lang.String securityType, java.lang.String principal, java.lang.String keytab, java.lang.String hdfsUser) {
            this.clusterName = clusterName;
            this.clusterId = clusterId;
            this.securityType = securityType;
            this.principal = principal;
            this.keytab = keytab;
            this.hdfsUser = hdfsUser;
        }

        public java.lang.String getClusterName() {
            return clusterName;
        }

        public java.lang.Long getClusterId() {
            return clusterId;
        }

        public java.lang.String getSecurityType() {
            return securityType;
        }

        public java.lang.String getPrincipal() {
            return principal;
        }

        public java.lang.String getKeytab() {
            return keytab;
        }

        public java.lang.String getHdfsUser() {
            return hdfsUser;
        }
    }
}