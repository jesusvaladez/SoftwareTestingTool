package org.apache.ambari.serviceadvisor;
import org.apache.commons.lang.StringUtils;
public class ServiceAdvisor {
    protected static org.apache.commons.logging.Log LOG = org.apache.commons.logging.LogFactory.getLog(org.apache.ambari.serviceadvisor.ServiceAdvisor.class);

    private static java.lang.String USAGE = "Usage: java -jar serviceadvisor.jar [ACTION] [HOSTS_FILE.json] [SERVICES_FILE.json] [OUTPUT.txt] [ERRORS.txt]";

    private static java.lang.String PYTHON_STACK_ADVISOR_SCRIPT = "/var/lib/ambari-server/resources/scripts/stack_advisor.py";

    public static void main(java.lang.String[] args) {
        if (args.length != 5) {
            java.lang.System.err.println(java.lang.String.format("Wrong number of arguments. %s", org.apache.ambari.serviceadvisor.ServiceAdvisor.USAGE));
            java.lang.System.exit(1);
        }
        java.lang.String action = args[0];
        java.lang.String hostsFile = args[1];
        java.lang.String servicesFile = args[2];
        java.lang.String outputFile = args[3];
        java.lang.String errorFile = args[4];
        int exitCode = org.apache.ambari.serviceadvisor.ServiceAdvisor.run(action, hostsFile, servicesFile, outputFile, errorFile);
        java.lang.System.exit(exitCode);
    }

    public static int run(java.lang.String action, java.lang.String hostsFile, java.lang.String servicesFile, java.lang.String outputFile, java.lang.String errorFile) {
        org.apache.ambari.serviceadvisor.ServiceAdvisor.LOG.info(java.lang.String.format("ServiceAdvisor. Received arguments. Action: %s, Hosts File: %s, Services File: %s", action, hostsFile, servicesFile));
        int returnCode = -1;
        try {
            org.apache.ambari.serviceadvisor.ServiceAdvisorCommandType commandType = org.apache.ambari.serviceadvisor.ServiceAdvisorCommandType.getEnum(action);
            java.lang.ProcessBuilder builder = org.apache.ambari.serviceadvisor.ServiceAdvisor.preparePythonShellCommand(commandType, hostsFile, servicesFile, outputFile, errorFile);
            returnCode = org.apache.ambari.serviceadvisor.ServiceAdvisor.launchProcess(builder);
        } catch (java.lang.IllegalArgumentException e) {
            java.util.List<org.apache.ambari.serviceadvisor.ServiceAdvisorCommandType> values = org.apache.commons.lang3.EnumUtils.getEnumList(org.apache.ambari.serviceadvisor.ServiceAdvisorCommandType.class);
            java.util.List<java.lang.String> stringValues = new java.util.ArrayList<java.lang.String>();
            for (org.apache.ambari.serviceadvisor.ServiceAdvisorCommandType value : values) {
                stringValues.add(value.toString());
            }
            org.apache.ambari.serviceadvisor.ServiceAdvisor.LOG.error("ServiceAdvisor. Illegal Argument. Action must be one of " + org.apache.commons.lang.StringUtils.join(stringValues.toArray(), ", "));
            return -1;
        } catch (java.lang.Exception e) {
            org.apache.ambari.serviceadvisor.ServiceAdvisor.LOG.error("ServiceAdvisor. Failed with " + e.getMessage());
            return -1;
        }
        return returnCode;
    }

    private static java.lang.ProcessBuilder preparePythonShellCommand(org.apache.ambari.serviceadvisor.ServiceAdvisorCommandType commandType, java.lang.String hostsFile, java.lang.String servicesFile, java.lang.String outputFile, java.lang.String errorFile) {
        java.util.List<java.lang.String> builderParameters = new java.util.ArrayList<java.lang.String>();
        if (java.lang.System.getProperty("os.name").contains("Windows")) {
            builderParameters.add("cmd");
            builderParameters.add("/c");
        } else {
            builderParameters.add("sh");
            builderParameters.add("-c");
        }
        java.lang.StringBuilder commandString = new java.lang.StringBuilder();
        commandString.append(org.apache.ambari.serviceadvisor.ServiceAdvisor.PYTHON_STACK_ADVISOR_SCRIPT + " ");
        commandString.append(commandType.toString()).append(" ");
        commandString.append(hostsFile).append(" ");
        commandString.append(servicesFile).append(" ");
        commandString.append("1> ");
        commandString.append(outputFile).append(" ");
        commandString.append("2>");
        commandString.append(errorFile).append(" ");
        builderParameters.add(commandString.toString());
        org.apache.ambari.serviceadvisor.ServiceAdvisor.LOG.info("ServiceAdvisor. Python command is: " + builderParameters.toString());
        return new java.lang.ProcessBuilder(builderParameters);
    }

    private static int launchProcess(java.lang.ProcessBuilder builder) throws java.lang.Exception {
        int exitCode = -1;
        java.lang.Process process = null;
        try {
            process = builder.start();
            exitCode = process.waitFor();
        } catch (java.lang.Exception ioe) {
            java.lang.String message = "Error executing Service Advisor: ";
            org.apache.ambari.serviceadvisor.ServiceAdvisor.LOG.error(message, ioe);
            throw new java.lang.Exception(message + ioe.getMessage());
        } finally {
            if (process != null) {
                process.destroy();
            }
        }
        return exitCode;
    }
}