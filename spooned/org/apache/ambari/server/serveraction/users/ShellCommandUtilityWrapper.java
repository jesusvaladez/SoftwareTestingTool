package org.apache.ambari.server.serveraction.users;
@javax.inject.Singleton
public class ShellCommandUtilityWrapper {
    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.serveraction.users.ShellCommandUtilityWrapper.class);

    @javax.inject.Inject
    public ShellCommandUtilityWrapper() {
        super();
    }

    public org.apache.ambari.server.utils.ShellCommandUtil.Result runCommand(java.lang.String[] args) throws java.io.IOException, java.lang.InterruptedException {
        org.apache.ambari.server.serveraction.users.ShellCommandUtilityWrapper.LOGGER.info("Running command: {}", args);
        return org.apache.ambari.server.utils.ShellCommandUtil.runCommand(args);
    }

    public org.apache.ambari.server.utils.ShellCommandUtil.Result runCommand(java.lang.String[] args, java.util.Map<java.lang.String, java.lang.String> vars) throws java.io.IOException, java.lang.InterruptedException {
        org.apache.ambari.server.serveraction.users.ShellCommandUtilityWrapper.LOGGER.info("Running command: {}, variables: {}", args, vars);
        return org.apache.ambari.server.utils.ShellCommandUtil.runCommand(args, vars);
    }
}