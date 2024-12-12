package org.apache.ambari.server.api.services.stackadvisor;
@com.google.inject.Singleton
public class StackAdvisorRunner {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRunner.class);

    @com.google.inject.Inject
    private org.apache.ambari.server.configuration.Configuration configs;

    public void runScript(org.apache.ambari.server.state.ServiceInfo.ServiceAdvisorType serviceAdvisorType, org.apache.ambari.server.api.services.stackadvisor.commands.StackAdvisorCommandType saCommandType, java.io.File actionDirectory) throws org.apache.ambari.server.api.services.stackadvisor.StackAdvisorException {
        org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRunner.LOG.info(java.lang.String.format("StackAdvisorRunner. serviceAdvisorType=%s, actionDirectory=%s, command=%s", serviceAdvisorType.toString(), actionDirectory, saCommandType));
        java.lang.String outputFile = (actionDirectory + java.io.File.separator) + "stackadvisor.out";
        java.lang.String errorFile = (actionDirectory + java.io.File.separator) + "stackadvisor.err";
        java.lang.String hostsFile = (actionDirectory + java.io.File.separator) + "hosts.json";
        java.lang.String servicesFile = (actionDirectory + java.io.File.separator) + "services.json";
        org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRunner.LOG.info("StackAdvisorRunner. Expected files: hosts.json={}, services.json={}, output={}, error={}", hostsFile, servicesFile, outputFile, errorFile);
        int stackAdvisorReturnCode = -1;
        switch (serviceAdvisorType) {
            case JAVA :
                org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRunner.LOG.info("StackAdvisorRunner.runScript(): Calling Java ServiceAdvisor's run method.");
                stackAdvisorReturnCode = org.apache.ambari.serviceadvisor.ServiceAdvisor.run(saCommandType.toString(), hostsFile, servicesFile, outputFile, errorFile);
                org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRunner.LOG.info(java.lang.String.format("StackAdvisorRunner.runScript(): Java ServiceAdvisor's return code: %d", stackAdvisorReturnCode));
                break;
            case PYTHON :
                org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRunner.LOG.info("StackAdvisorRunner.runScript(): Calling Python Stack Advisor.");
                java.lang.ProcessBuilder builder = prepareShellCommand(org.apache.ambari.server.state.ServiceInfo.ServiceAdvisorType.PYTHON, configs.getStackAdvisorScript(), saCommandType, actionDirectory, outputFile, errorFile);
                builder.environment().put("METADATA_DIR_PATH", configs.getProperty(org.apache.ambari.server.configuration.Configuration.METADATA_DIR_PATH));
                builder.environment().put("BASE_SERVICE_ADVISOR", java.nio.file.Paths.get(configs.getProperty(org.apache.ambari.server.configuration.Configuration.METADATA_DIR_PATH), "service_advisor.py").toString());
                builder.environment().put("BASE_STACK_ADVISOR", java.nio.file.Paths.get(configs.getProperty(org.apache.ambari.server.configuration.Configuration.METADATA_DIR_PATH), "stack_advisor.py").toString());
                stackAdvisorReturnCode = launchProcess(builder);
                break;
        }
        processLogs(stackAdvisorReturnCode, outputFile, errorFile);
    }

    private int launchProcess(java.lang.ProcessBuilder builder) throws org.apache.ambari.server.api.services.stackadvisor.StackAdvisorException {
        int exitCode = -1;
        java.lang.Process process = null;
        try {
            process = builder.start();
            exitCode = process.waitFor();
        } catch (java.lang.Exception ioe) {
            java.lang.String message = "Error executing Stack Advisor: ";
            org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRunner.LOG.error(message, ioe);
            throw new org.apache.ambari.server.api.services.stackadvisor.StackAdvisorException(message + ioe.getMessage());
        } finally {
            if (process != null) {
                process.destroy();
            }
        }
        return exitCode;
    }

    private void processLogs(int exitCode, java.lang.String outputFile, java.lang.String errorFile) throws org.apache.ambari.server.api.services.stackadvisor.StackAdvisorException {
        printMessage("stdout", outputFile);
        java.lang.String errMessage = printMessage("stderr", errorFile);
        try {
            if (exitCode != 0) {
                java.lang.String errorMessage;
                if (errMessage != null) {
                    int index = errMessage.lastIndexOf("\n");
                    if ((index > 0) && (index == (errMessage.length() - 1))) {
                        index = errMessage.lastIndexOf("\n", index - 1);
                    }
                    if (index > (-1)) {
                        errMessage = errMessage.substring(index + 1).trim();
                    }
                    errorMessage = java.lang.String.format("Stack Advisor reported an error. Exit Code: %s. Error: %s ", exitCode, errMessage);
                } else {
                    errorMessage = java.lang.String.format("Error occurred during Stack Advisor execution. Exit Code: %s", exitCode);
                }
                errorMessage += ("\nStdOut file: " + outputFile) + "\n";
                errorMessage += "\nStdErr file: " + errorFile;
                switch (exitCode) {
                    case 1 :
                        throw new org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequestException(errorMessage);
                    case 2 :
                        throw new org.apache.ambari.server.api.services.stackadvisor.StackAdvisorException(errorMessage);
                }
            }
        } catch (org.apache.ambari.server.api.services.stackadvisor.StackAdvisorException ex) {
            throw ex;
        }
    }

    private java.lang.String printMessage(java.lang.String type, java.lang.String file) {
        java.lang.String message = null;
        try {
            message = org.apache.commons.io.FileUtils.readFileToString(new java.io.File(file), java.nio.charset.Charset.defaultCharset()).trim();
            org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRunner.LOG.info("    Advisor script {}: {}", type, message);
        } catch (java.io.IOException io) {
            org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRunner.LOG.error("Error in reading script log files", io);
        }
        return message;
    }

    java.lang.ProcessBuilder prepareShellCommand(org.apache.ambari.server.state.ServiceInfo.ServiceAdvisorType serviceAdvisorType, java.lang.String script, org.apache.ambari.server.api.services.stackadvisor.commands.StackAdvisorCommandType saCommandType, java.io.File actionDirectory, java.lang.String outputFile, java.lang.String errorFile) {
        java.lang.String hostsFile = (actionDirectory + java.io.File.separator) + "hosts.json";
        java.lang.String servicesFile = (actionDirectory + java.io.File.separator) + "services.json";
        java.util.List<java.lang.String> builderParameters = new java.util.ArrayList<>();
        switch (serviceAdvisorType) {
            case PYTHON :
            case JAVA :
                if (java.lang.System.getProperty("os.name").contains("Windows")) {
                    builderParameters.add("cmd");
                    builderParameters.add("/c");
                } else {
                    builderParameters.add("sh");
                    builderParameters.add("-c");
                }
                break;
            default :
                break;
        }
        java.lang.String commandStringParameters[] = new java.lang.String[]{ script, saCommandType.toString(), hostsFile, servicesFile, "1>", outputFile, "2>", errorFile };
        java.lang.StringBuilder commandString = new java.lang.StringBuilder();
        for (java.lang.String command : commandStringParameters) {
            commandString.append(command).append(" ");
        }
        builderParameters.add(commandString.toString());
        org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRunner.LOG.debug("StackAdvisorRunner. Stack advisor command is {}", org.apache.commons.lang3.StringUtils.join(" ", builderParameters));
        return new java.lang.ProcessBuilder(builderParameters);
    }

    public void setConfigs(org.apache.ambari.server.configuration.Configuration configs) {
        this.configs = configs;
    }
}