package org.apache.ambari.server.utils;
import org.apache.commons.lang.StringUtils;
public class ShellCommandUtil {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.utils.ShellCommandUtil.class);

    private static final java.lang.Object WindowsProcessLaunchLock = new java.lang.Object();

    private static final java.lang.String PASS_TOKEN = "pass:";

    private static final java.lang.String KEY_TOKEN = "-key ";

    private static final java.lang.String AMBARI_SUDO = "ambari-sudo.sh";

    private static final int MODE_OWNER_READABLE = 400;

    private static final int MODE_OWNER_WRITABLE = 200;

    private static final int MODE_OWNER_EXECUTABLE = 100;

    private static final int MODE_GROUP_READABLE = 40;

    private static final int MODE_GROUP_WRITABLE = 20;

    private static final int MODE_GROUP_EXECUTABLE = 10;

    private static final int MODE_OTHER_READABLE = 4;

    private static final int MODE_OTHER_WRITABLE = 2;

    private static final int MODE_OTHER_EXECUTABLE = 1;

    public static void logOpenSslExitCode(java.lang.String command, int exitCode) {
        if (exitCode == 0) {
            org.apache.ambari.server.utils.ShellCommandUtil.LOG.info(org.apache.ambari.server.utils.ShellCommandUtil.getOpenSslCommandResult(command, exitCode));
        } else {
            org.apache.ambari.server.utils.ShellCommandUtil.LOG.warn(org.apache.ambari.server.utils.ShellCommandUtil.getOpenSslCommandResult(command, exitCode));
        }
    }

    public static java.lang.String hideOpenSslPassword(java.lang.String command) {
        int start;
        if (command.contains(org.apache.ambari.server.utils.ShellCommandUtil.PASS_TOKEN)) {
            start = command.indexOf(org.apache.ambari.server.utils.ShellCommandUtil.PASS_TOKEN) + org.apache.ambari.server.utils.ShellCommandUtil.PASS_TOKEN.length();
        } else if (command.contains(org.apache.ambari.server.utils.ShellCommandUtil.KEY_TOKEN)) {
            start = command.indexOf(org.apache.ambari.server.utils.ShellCommandUtil.KEY_TOKEN) + org.apache.ambari.server.utils.ShellCommandUtil.KEY_TOKEN.length();
        } else {
            return command;
        }
        java.lang.CharSequence cs = command.subSequence(start, command.indexOf(" ", start));
        return command.replace(cs, "****");
    }

    public static java.lang.String getOpenSslCommandResult(java.lang.String command, int exitCode) {
        return new java.lang.StringBuilder().append("Command ").append(org.apache.ambari.server.utils.ShellCommandUtil.hideOpenSslPassword(command)).append(" was finished with exit code: ").append(exitCode).append(" - ").append(org.apache.ambari.server.utils.ShellCommandUtil.getOpenSslExitCodeDescription(exitCode)).toString();
    }

    private static java.lang.String getOpenSslExitCodeDescription(int exitCode) {
        switch (exitCode) {
            case 0 :
                {
                    return "the operation was completely successfully.";
                }
            case 1 :
                {
                    return "an error occurred parsing the command options.";
                }
            case 2 :
                {
                    return "one of the input files could not be read.";
                }
            case 3 :
                {
                    return "an error occurred creating the PKCS#7 file or when reading the MIME message.";
                }
            case 4 :
                {
                    return "an error occurred decrypting or verifying the message.";
                }
            case 5 :
                {
                    return "the message was verified correctly but an error occurred writing out the signers certificates.";
                }
            default :
                return "unsupported code";
        }
    }

    public static final boolean WINDOWS = java.lang.System.getProperty("os.name").startsWith("Windows");

    public static final boolean LINUX = java.lang.System.getProperty("os.name").startsWith("Linux");

    public static final boolean MAC = java.lang.System.getProperty("os.name").startsWith("Mac");

    public static final boolean UNIX_LIKE = org.apache.ambari.server.utils.ShellCommandUtil.LINUX || org.apache.ambari.server.utils.ShellCommandUtil.MAC;

    public static final java.lang.String MASK_OWNER_ONLY_RW = "600";

    public static final java.lang.String MASK_OWNER_ONLY_RWX = "700";

    public static final java.lang.String MASK_EVERYBODY_RWX = "777";

    public static java.lang.String getUnixFilePermissions(java.lang.String path) {
        java.lang.String result = org.apache.ambari.server.utils.ShellCommandUtil.MASK_EVERYBODY_RWX;
        if (org.apache.ambari.server.utils.ShellCommandUtil.LINUX) {
            try {
                result = org.apache.ambari.server.utils.ShellCommandUtil.runCommand(new java.lang.String[]{ "stat", "-c", "%a", path }).getStdout();
            } catch (java.io.IOException | java.lang.InterruptedException e) {
                org.apache.ambari.server.utils.ShellCommandUtil.LOG.warn(java.lang.String.format("Can not perform stat on %s", path), e);
            }
        } else {
            org.apache.ambari.server.utils.ShellCommandUtil.LOG.debug(java.lang.String.format("Not performing stat -s \"%%a\" command on file %s " + "because current OS is not Linux. Returning 777", path));
        }
        return result.trim();
    }

    public static void setUnixFilePermissions(java.lang.String mode, java.lang.String path) {
        if (org.apache.ambari.server.utils.ShellCommandUtil.LINUX) {
            try {
                org.apache.ambari.server.utils.ShellCommandUtil.runCommand(new java.lang.String[]{ "chmod", mode, path });
            } catch (java.io.IOException | java.lang.InterruptedException e) {
                org.apache.ambari.server.utils.ShellCommandUtil.LOG.warn(java.lang.String.format("Can not perform chmod %s %s", mode, path), e);
            }
        } else {
            org.apache.ambari.server.utils.ShellCommandUtil.LOG.debug(java.lang.String.format("Not performing chmod %s command for file %s " + "because current OS is not Linux ", mode, path));
        }
    }

    public static org.apache.ambari.server.utils.ShellCommandUtil.Result setFileOwner(java.lang.String path, java.lang.String ownerName) {
        if (org.apache.ambari.server.utils.ShellCommandUtil.LINUX) {
            if (!org.apache.commons.lang.StringUtils.isEmpty(ownerName)) {
                try {
                    return org.apache.ambari.server.utils.ShellCommandUtil.runCommand(new java.lang.String[]{ "chown", ownerName, path }, null, null, true);
                } catch (java.io.IOException | java.lang.InterruptedException e) {
                    org.apache.ambari.server.utils.ShellCommandUtil.LOG.warn(java.lang.String.format("Can not perform chown %s %s", ownerName, path), e);
                    return new org.apache.ambari.server.utils.ShellCommandUtil.Result(-1, "", "Cannot perform operation: " + e.getLocalizedMessage());
                }
            } else {
                return new org.apache.ambari.server.utils.ShellCommandUtil.Result(0, "", "");
            }
        } else {
            org.apache.ambari.server.utils.ShellCommandUtil.LOG.debug(java.lang.String.format("Not performing chown command for file %s " + "because current OS is not Linux ", path));
            return new org.apache.ambari.server.utils.ShellCommandUtil.Result(-1, "", "Cannot perform operation: The current OS is not Linux");
        }
    }

    public static org.apache.ambari.server.utils.ShellCommandUtil.Result setFileGroup(java.lang.String path, java.lang.String groupName) {
        if (org.apache.ambari.server.utils.ShellCommandUtil.LINUX) {
            if (!org.apache.commons.lang.StringUtils.isEmpty(groupName)) {
                try {
                    return org.apache.ambari.server.utils.ShellCommandUtil.runCommand(new java.lang.String[]{ "chgrp", groupName, path }, null, null, true);
                } catch (java.io.IOException | java.lang.InterruptedException e) {
                    org.apache.ambari.server.utils.ShellCommandUtil.LOG.warn(java.lang.String.format("Can not perform chgrp %s %s", groupName, path), e);
                    return new org.apache.ambari.server.utils.ShellCommandUtil.Result(-1, "", "Cannot perform operation: " + e.getLocalizedMessage());
                }
            } else {
                return new org.apache.ambari.server.utils.ShellCommandUtil.Result(0, "", "");
            }
        } else {
            org.apache.ambari.server.utils.ShellCommandUtil.LOG.debug(java.lang.String.format("Not performing chgrp command for file %s " + "because current OS is not Linux ", path));
            return new org.apache.ambari.server.utils.ShellCommandUtil.Result(-1, "", "Cannot perform operation: The current OS is not Linux");
        }
    }

    public static org.apache.ambari.server.utils.ShellCommandUtil.Result setFileMode(java.lang.String path, boolean ownerReadable, boolean ownerWritable, boolean ownerExecutable, boolean groupReadable, boolean groupWritable, boolean groupExecutable, boolean otherReadable, boolean otherWritable, boolean otherExecutable) {
        if (org.apache.ambari.server.utils.ShellCommandUtil.LINUX) {
            int modeValue = ((((((((ownerReadable ? org.apache.ambari.server.utils.ShellCommandUtil.MODE_OWNER_READABLE : 0) + (ownerWritable ? org.apache.ambari.server.utils.ShellCommandUtil.MODE_OWNER_WRITABLE : 0)) + (ownerExecutable ? org.apache.ambari.server.utils.ShellCommandUtil.MODE_OWNER_EXECUTABLE : 0)) + (groupReadable ? org.apache.ambari.server.utils.ShellCommandUtil.MODE_GROUP_READABLE : 0)) + (groupWritable ? org.apache.ambari.server.utils.ShellCommandUtil.MODE_GROUP_WRITABLE : 0)) + (groupExecutable ? org.apache.ambari.server.utils.ShellCommandUtil.MODE_GROUP_EXECUTABLE : 0)) + (otherReadable ? org.apache.ambari.server.utils.ShellCommandUtil.MODE_OTHER_READABLE : 0)) + (otherWritable ? org.apache.ambari.server.utils.ShellCommandUtil.MODE_OTHER_WRITABLE : 0)) + (otherExecutable ? org.apache.ambari.server.utils.ShellCommandUtil.MODE_OTHER_EXECUTABLE : 0);
            java.lang.String mode = java.lang.String.format("%04d", modeValue);
            try {
                return org.apache.ambari.server.utils.ShellCommandUtil.runCommand(new java.lang.String[]{ "chmod", mode, path }, null, null, true);
            } catch (java.io.IOException | java.lang.InterruptedException e) {
                org.apache.ambari.server.utils.ShellCommandUtil.LOG.warn(java.lang.String.format("Can not perform chmod %s %s", mode, path), e);
                return new org.apache.ambari.server.utils.ShellCommandUtil.Result(-1, "", "Cannot perform operation: " + e.getLocalizedMessage());
            }
        } else {
            org.apache.ambari.server.utils.ShellCommandUtil.LOG.debug(java.lang.String.format("Not performing chmod command for file %s " + "because current OS is not Linux ", path));
            return new org.apache.ambari.server.utils.ShellCommandUtil.Result(-1, "", "Cannot perform operation: The current OS is not Linux");
        }
    }

    public static org.apache.ambari.server.utils.ShellCommandUtil.Result pathExists(java.lang.String path, boolean sudo) throws java.io.IOException, java.lang.InterruptedException {
        java.lang.String[] command = new java.lang.String[]{ org.apache.ambari.server.utils.ShellCommandUtil.WINDOWS ? "dir" : "/bin/ls", path };
        return org.apache.ambari.server.utils.ShellCommandUtil.runCommand(command, null, null, sudo);
    }

    public static org.apache.ambari.server.utils.ShellCommandUtil.Result mkdir(java.lang.String directoryPath, boolean sudo) throws java.io.IOException, java.lang.InterruptedException {
        if (org.apache.ambari.server.utils.ShellCommandUtil.pathExists(directoryPath, sudo).isSuccessful()) {
            return new org.apache.ambari.server.utils.ShellCommandUtil.Result(0, "The directory already exists, skipping.", "");
        } else {
            java.util.ArrayList<java.lang.String> command = new java.util.ArrayList<>();
            command.add("/bin/mkdir");
            if (!org.apache.ambari.server.utils.ShellCommandUtil.WINDOWS) {
                command.add("-p");
            }
            command.add(directoryPath);
            return org.apache.ambari.server.utils.ShellCommandUtil.runCommand(command, null, null, sudo);
        }
    }

    public static org.apache.ambari.server.utils.ShellCommandUtil.Result copyFile(java.lang.String srcFile, java.lang.String destFile, boolean force, boolean sudo) throws java.io.IOException, java.lang.InterruptedException {
        java.util.ArrayList<java.lang.String> command = new java.util.ArrayList<>();
        if (org.apache.ambari.server.utils.ShellCommandUtil.WINDOWS) {
            command.add("copy");
            if (force) {
                command.add("/Y");
            }
        } else {
            command.add("cp");
            command.add("-p");
            if (force) {
                command.add("-f");
            }
        }
        command.add(srcFile);
        command.add(destFile);
        return org.apache.ambari.server.utils.ShellCommandUtil.runCommand(command, null, null, sudo);
    }

    public static org.apache.ambari.server.utils.ShellCommandUtil.Result delete(java.lang.String file, boolean force, boolean sudo) throws java.io.IOException, java.lang.InterruptedException {
        java.util.List<java.lang.String> command = new java.util.ArrayList<>();
        if (org.apache.ambari.server.utils.ShellCommandUtil.WINDOWS) {
            command.add("del");
            if (force) {
                command.add("/f");
            }
        } else {
            command.add("/bin/rm");
            if (force) {
                command.add("-f");
            }
        }
        command.add(file);
        return org.apache.ambari.server.utils.ShellCommandUtil.runCommand(command, null, null, sudo);
    }

    public static org.apache.ambari.server.utils.ShellCommandUtil.Result runCommand(java.util.List<java.lang.String> args, java.util.Map<java.lang.String, java.lang.String> vars, org.apache.ambari.server.utils.ShellCommandUtil.InteractiveHandler interactiveHandler, boolean sudo) throws java.io.IOException, java.lang.InterruptedException {
        return org.apache.ambari.server.utils.ShellCommandUtil.runCommand(args.toArray(new java.lang.String[args.size()]), vars, interactiveHandler, sudo);
    }

    public static org.apache.ambari.server.utils.ShellCommandUtil.Result runCommand(java.lang.String[] args, java.util.Map<java.lang.String, java.lang.String> vars, org.apache.ambari.server.utils.ShellCommandUtil.InteractiveHandler interactiveHandler, boolean sudo) throws java.io.IOException, java.lang.InterruptedException {
        java.lang.String[] processArgs;
        if (sudo) {
            processArgs = new java.lang.String[args.length + 1];
            processArgs[0] = org.apache.ambari.server.utils.ShellCommandUtil.AMBARI_SUDO;
            java.lang.System.arraycopy(args, 0, processArgs, 1, args.length);
        } else {
            processArgs = args;
        }
        java.lang.ProcessBuilder builder = new java.lang.ProcessBuilder(processArgs);
        if (vars != null) {
            java.util.Map<java.lang.String, java.lang.String> env = builder.environment();
            env.putAll(vars);
        }
        org.apache.ambari.server.utils.ShellCommandUtil.LOG.debug("Executing the command {}", java.util.Arrays.toString(processArgs));
        java.lang.Process process;
        if (org.apache.ambari.server.utils.ShellCommandUtil.WINDOWS) {
            synchronized(org.apache.ambari.server.utils.ShellCommandUtil.WindowsProcessLaunchLock) {
                process = builder.start();
            }
        } else {
            process = builder.start();
        }
        java.io.InputStream inputStream = process.getInputStream();
        if (interactiveHandler != null) {
            java.io.BufferedWriter writer = new java.io.BufferedWriter(new java.io.OutputStreamWriter(process.getOutputStream()));
            java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.InputStreamReader(inputStream));
            interactiveHandler.start();
            try {
                while (!interactiveHandler.done()) {
                    java.lang.StringBuilder query = new java.lang.StringBuilder();
                    while (reader.ready()) {
                        query.append(((char) (reader.read())));
                    } 
                    java.lang.String response = interactiveHandler.getResponse(query.toString());
                    if (response != null) {
                        writer.write(response);
                        writer.newLine();
                        writer.flush();
                    }
                } 
            } catch (java.io.IOException ex) {
            } finally {
                writer.close();
            }
        }
        process.waitFor();
        java.lang.String stdout = org.apache.ambari.server.utils.ShellCommandUtil.streamToString(inputStream);
        java.lang.String stderr = org.apache.ambari.server.utils.ShellCommandUtil.streamToString(process.getErrorStream());
        int exitCode = process.exitValue();
        return new org.apache.ambari.server.utils.ShellCommandUtil.Result(exitCode, stdout, stderr);
    }

    public static org.apache.ambari.server.utils.ShellCommandUtil.Result runCommand(java.lang.String[] args, java.util.Map<java.lang.String, java.lang.String> vars) throws java.io.IOException, java.lang.InterruptedException {
        return org.apache.ambari.server.utils.ShellCommandUtil.runCommand(args, vars, null, false);
    }

    public static org.apache.ambari.server.utils.ShellCommandUtil.Result runCommand(java.lang.String[] args) throws java.io.IOException, java.lang.InterruptedException {
        return org.apache.ambari.server.utils.ShellCommandUtil.runCommand(args, null);
    }

    private static java.lang.String streamToString(java.io.InputStream is) throws java.io.IOException {
        java.io.InputStreamReader isr = new java.io.InputStreamReader(is);
        java.io.BufferedReader reader = new java.io.BufferedReader(isr);
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        java.lang.String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        } 
        return sb.toString();
    }

    public static class Result {
        public Result(int exitCode, java.lang.String stdout, java.lang.String stderr) {
            this.exitCode = exitCode;
            this.stdout = stdout;
            this.stderr = stderr;
        }

        private final int exitCode;

        private final java.lang.String stdout;

        private final java.lang.String stderr;

        public int getExitCode() {
            return exitCode;
        }

        public java.lang.String getStdout() {
            return stdout;
        }

        public java.lang.String getStderr() {
            return stderr;
        }

        public boolean isSuccessful() {
            return exitCode == 0;
        }
    }

    public interface InteractiveHandler {
        boolean done();

        java.lang.String getResponse(java.lang.String query);

        void start();
    }
}