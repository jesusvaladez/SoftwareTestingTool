package org.apache.ambari.server.serveraction.users;
import org.codehaus.jackson.map.ObjectMapper;
@javax.inject.Singleton
public class PostUserCreationHookServerAction extends org.apache.ambari.server.serveraction.AbstractServerAction {
    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.serveraction.users.PostUserCreationHookServerAction.class);

    private static final int MAX_SYMBOLS_PER_LOG_MESSAGE = 7900;

    @javax.inject.Inject
    private org.apache.ambari.server.serveraction.users.ShellCommandUtilityWrapper shellCommandUtilityWrapper;

    @javax.inject.Inject
    private org.codehaus.jackson.map.ObjectMapper objectMapper;

    @javax.inject.Inject
    private org.apache.ambari.server.serveraction.users.CollectionPersisterServiceFactory collectionPersisterServiceFactory;

    @javax.inject.Inject
    public PostUserCreationHookServerAction() {
        super();
    }

    @java.lang.Override
    public org.apache.ambari.server.agent.CommandReport execute(java.util.concurrent.ConcurrentMap<java.lang.String, java.lang.Object> requestSharedDataContext) throws org.apache.ambari.server.AmbariException, java.lang.InterruptedException {
        org.apache.ambari.server.serveraction.users.PostUserCreationHookServerAction.LOGGER.debug("Executing custom script server action; Context: {}", requestSharedDataContext);
        org.apache.ambari.server.utils.ShellCommandUtil.Result result = null;
        org.apache.ambari.server.agent.CommandReport cmdReport = null;
        try {
            java.util.Map<java.lang.String, java.lang.String> commandParams = getCommandParameters();
            validateCommandParams(commandParams);
            org.apache.ambari.server.serveraction.users.CollectionPersisterService<java.lang.String, java.util.List<java.lang.String>> csvPersisterService = collectionPersisterServiceFactory.createCsvFilePersisterService(commandParams.get(org.apache.ambari.server.hooks.users.UserHookParams.CMD_INPUT_FILE.param()));
            csvPersisterService.persistMap(getPayload(commandParams));
            java.lang.String[] cmd = assembleCommand(commandParams);
            result = shellCommandUtilityWrapper.runCommand(cmd);
            logCommandResult(java.util.Arrays.asList(cmd).toString(), result);
            cmdReport = createCommandReport(result.getExitCode(), result.isSuccessful() ? org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED : org.apache.ambari.server.actionmanager.HostRoleStatus.FAILED, "{}", result.getStdout(), result.getStderr());
            org.apache.ambari.server.serveraction.users.PostUserCreationHookServerAction.LOGGER.debug("Command report: {}", cmdReport);
        } catch (java.lang.InterruptedException e) {
            org.apache.ambari.server.serveraction.users.PostUserCreationHookServerAction.LOGGER.error("The server action thread has been interrupted", e);
            throw e;
        } catch (java.lang.Exception e) {
            org.apache.ambari.server.serveraction.users.PostUserCreationHookServerAction.LOGGER.error("Server action is about to quit due to an exception.", e);
            throw new org.apache.ambari.server.AmbariException("Server action execution failed to complete!", e);
        }
        return cmdReport;
    }

    private void logCommandResult(java.lang.String command, org.apache.ambari.server.utils.ShellCommandUtil.Result result) {
        org.apache.ambari.server.serveraction.users.PostUserCreationHookServerAction.LOGGER.info("Execution of command [ {} ] - {}", command, result.isSuccessful() ? "succeeded" : "failed");
        java.lang.String stringToLog = (result.isSuccessful()) ? result.getStdout() : result.getStderr();
        if (stringToLog == null)
            stringToLog = "";

        java.util.List<java.lang.String> logLines = com.google.common.base.Splitter.fixedLength(org.apache.ambari.server.serveraction.users.PostUserCreationHookServerAction.MAX_SYMBOLS_PER_LOG_MESSAGE).splitToList(stringToLog);
        org.apache.ambari.server.serveraction.users.PostUserCreationHookServerAction.LOGGER.info("BEGIN - {} for command {}", result.isSuccessful() ? "stdout" : "stderr", command);
        for (java.lang.String line : logLines) {
            org.apache.ambari.server.serveraction.users.PostUserCreationHookServerAction.LOGGER.info("command output *** : {}", line);
        }
        org.apache.ambari.server.serveraction.users.PostUserCreationHookServerAction.LOGGER.info("END - {} for command {}", result.isSuccessful() ? "stdout" : "stderr", command);
    }

    private java.lang.String[] assembleCommand(java.util.Map<java.lang.String, java.lang.String> params) {
        java.lang.String[] cmdArray = new java.lang.String[]{ params.get(org.apache.ambari.server.hooks.users.UserHookParams.SCRIPT.param()), params.get(org.apache.ambari.server.hooks.users.UserHookParams.CMD_INPUT_FILE.param()), params.get(org.apache.ambari.server.hooks.users.UserHookParams.CLUSTER_SECURITY_TYPE.param()), params.get(org.apache.ambari.server.hooks.users.UserHookParams.CMD_HDFS_PRINCIPAL.param()), params.get(org.apache.ambari.server.hooks.users.UserHookParams.CMD_HDFS_KEYTAB.param()), params.get(org.apache.ambari.server.hooks.users.UserHookParams.CMD_HDFS_USER.param()) };
        org.apache.ambari.server.serveraction.users.PostUserCreationHookServerAction.LOGGER.debug("Server action command to be executed: {}", cmdArray);
        return cmdArray;
    }

    private void validateCommandParams(java.util.Map<java.lang.String, java.lang.String> commandParams) {
        org.apache.ambari.server.serveraction.users.PostUserCreationHookServerAction.LOGGER.info("Validating command parameters ...");
        if (!commandParams.containsKey(org.apache.ambari.server.hooks.users.UserHookParams.PAYLOAD.param())) {
            org.apache.ambari.server.serveraction.users.PostUserCreationHookServerAction.LOGGER.error("Missing command parameter: {}; Failing the server action.", org.apache.ambari.server.hooks.users.UserHookParams.PAYLOAD.param());
            throw new java.lang.IllegalArgumentException(("Missing command parameter: [" + org.apache.ambari.server.hooks.users.UserHookParams.PAYLOAD.param()) + "]");
        }
        if (!commandParams.containsKey(org.apache.ambari.server.hooks.users.UserHookParams.SCRIPT.param())) {
            org.apache.ambari.server.serveraction.users.PostUserCreationHookServerAction.LOGGER.error("Missing command parameter: {}; Failing the server action.", org.apache.ambari.server.hooks.users.UserHookParams.SCRIPT.param());
            throw new java.lang.IllegalArgumentException(("Missing command parameter: [" + org.apache.ambari.server.hooks.users.UserHookParams.SCRIPT.param()) + "]");
        }
        if (!commandParams.containsKey(org.apache.ambari.server.hooks.users.UserHookParams.CMD_INPUT_FILE.param())) {
            org.apache.ambari.server.serveraction.users.PostUserCreationHookServerAction.LOGGER.error("Missing command parameter: {}; Failing the server action.", org.apache.ambari.server.hooks.users.UserHookParams.CMD_INPUT_FILE.param());
            throw new java.lang.IllegalArgumentException(("Missing command parameter: [" + org.apache.ambari.server.hooks.users.UserHookParams.CMD_INPUT_FILE.param()) + "]");
        }
        if (!commandParams.containsKey(org.apache.ambari.server.hooks.users.UserHookParams.CLUSTER_SECURITY_TYPE.param())) {
            org.apache.ambari.server.serveraction.users.PostUserCreationHookServerAction.LOGGER.error("Missing command parameter: {}; Failing the server action.", org.apache.ambari.server.hooks.users.UserHookParams.CLUSTER_SECURITY_TYPE.param());
            throw new java.lang.IllegalArgumentException(("Missing command parameter: [" + org.apache.ambari.server.hooks.users.UserHookParams.CLUSTER_SECURITY_TYPE.param()) + "]");
        }
        if (!commandParams.containsKey(org.apache.ambari.server.hooks.users.UserHookParams.CMD_HDFS_USER.param())) {
            org.apache.ambari.server.serveraction.users.PostUserCreationHookServerAction.LOGGER.error("Missing command parameter: {}; Failing the server action.", org.apache.ambari.server.hooks.users.UserHookParams.CMD_HDFS_USER.param());
            throw new java.lang.IllegalArgumentException(("Missing command parameter: [" + org.apache.ambari.server.hooks.users.UserHookParams.CMD_HDFS_USER.param()) + "]");
        }
        org.apache.ambari.server.serveraction.users.PostUserCreationHookServerAction.LOGGER.info("Command parameter validation passed.");
    }

    private java.util.Map<java.lang.String, java.util.List<java.lang.String>> getPayload(java.util.Map<java.lang.String, java.lang.String> commandParams) throws java.io.IOException {
        java.util.Map<java.lang.String, java.util.List<java.lang.String>> payload = objectMapper.readValue(commandParams.get(org.apache.ambari.server.hooks.users.UserHookParams.PAYLOAD.param()), java.util.Map.class);
        return payload;
    }
}