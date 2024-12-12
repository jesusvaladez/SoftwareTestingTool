package org.apache.ambari.server.bootstrap;
import org.apache.commons.lang.StringUtils;
class BSRunner extends java.lang.Thread {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.bootstrap.BSRunner.class);

    private static final java.lang.String DEFAULT_USER = "root";

    private static final java.lang.String DEFAULT_SSHPORT = "22";

    private boolean finished = false;

    private org.apache.ambari.server.bootstrap.SshHostInfo sshHostInfo;

    private java.io.File bootDir;

    private java.lang.String bsScript;

    private java.io.File requestIdDir;

    private java.io.File sshKeyFile;

    private java.io.File passwordFile;

    private int requestId;

    private java.lang.String agentSetupScript;

    private java.lang.String agentSetupPassword;

    private java.lang.String ambariHostname;

    private boolean verbose;

    private org.apache.ambari.server.bootstrap.BootStrapImpl bsImpl;

    private final java.lang.String clusterOsFamily;

    private java.lang.String projectVersion;

    private int serverPort;

    public BSRunner(org.apache.ambari.server.bootstrap.BootStrapImpl impl, org.apache.ambari.server.bootstrap.SshHostInfo sshHostInfo, java.lang.String bootDir, java.lang.String bsScript, java.lang.String agentSetupScript, java.lang.String agentSetupPassword, int requestId, long timeout, java.lang.String hostName, boolean isVerbose, java.lang.String clusterOsFamily, java.lang.String projectVersion, int serverPort) {
        this.requestId = requestId;
        this.sshHostInfo = sshHostInfo;
        this.bsScript = bsScript;
        this.bootDir = new java.io.File(bootDir);
        this.requestIdDir = new java.io.File(bootDir, java.lang.Integer.toString(requestId));
        this.sshKeyFile = new java.io.File(this.requestIdDir, "sshKey");
        this.agentSetupScript = agentSetupScript;
        this.agentSetupPassword = agentSetupPassword;
        this.ambariHostname = hostName;
        this.verbose = isVerbose;
        this.clusterOsFamily = clusterOsFamily;
        this.projectVersion = projectVersion;
        this.bsImpl = impl;
        this.serverPort = serverPort;
        org.apache.ambari.server.bootstrap.BootStrapStatus status = new org.apache.ambari.server.bootstrap.BootStrapStatus();
        status.setLog("RUNNING");
        status.setStatus(org.apache.ambari.server.bootstrap.BootStrapStatus.BSStat.RUNNING);
        bsImpl.updateStatus(requestId, status);
    }

    private class BSStatusCollector implements java.lang.Runnable {
        @java.lang.Override
        public void run() {
            org.apache.ambari.server.bootstrap.BSHostStatusCollector collector = new org.apache.ambari.server.bootstrap.BSHostStatusCollector(requestIdDir, sshHostInfo.getHosts());
            collector.run();
            java.util.List<org.apache.ambari.server.bootstrap.BSHostStatus> hostStatus = collector.getHostStatus();
            org.apache.ambari.server.bootstrap.BootStrapStatus status = new org.apache.ambari.server.bootstrap.BootStrapStatus();
            status.setHostsStatus(hostStatus);
            status.setLog("");
            status.setStatus(org.apache.ambari.server.bootstrap.BootStrapStatus.BSStat.RUNNING);
            bsImpl.updateStatus(requestId, status);
        }
    }

    private java.lang.String createHostString(java.util.List<java.lang.String> list) {
        return list != null ? java.lang.String.join(",", list) : org.apache.commons.lang.StringUtils.EMPTY;
    }

    private void createRunDir() throws java.io.IOException {
        if (!bootDir.exists()) {
            if (!bootDir.mkdirs()) {
                throw new java.io.IOException("Cannot create " + bootDir);
            }
        }
        if (requestIdDir.exists()) {
            org.apache.commons.io.FileUtils.deleteDirectory(requestIdDir);
        }
        if (!requestIdDir.mkdirs()) {
            throw new java.io.IOException("Cannot create " + requestIdDir);
        }
    }

    private void writeSshKeyFile(java.lang.String data) throws java.io.IOException {
        org.apache.commons.io.FileUtils.writeStringToFile(sshKeyFile, data, java.nio.charset.Charset.defaultCharset());
    }

    private void writePasswordFile(java.lang.String data) throws java.io.IOException {
        org.apache.commons.io.FileUtils.writeStringToFile(passwordFile, data, java.nio.charset.Charset.defaultCharset());
    }

    private boolean waitForProcessTermination(java.lang.Process process, long timeout) throws java.lang.InterruptedException {
        long startTime = java.lang.System.currentTimeMillis();
        do {
            try {
                process.exitValue();
                return true;
            } catch (java.lang.IllegalThreadStateException ignored) {
            }
            java.lang.Thread.sleep(1000);
        } while ((java.lang.System.currentTimeMillis() - startTime) < timeout );
        return false;
    }

    private long calculateBSTimeout(int hostCount) {
        final int PARALLEL_BS_COUNT = 20;
        final long HOST_BS_TIMEOUT = 300000L;
        return java.lang.Math.max(HOST_BS_TIMEOUT, (HOST_BS_TIMEOUT * hostCount) / PARALLEL_BS_COUNT);
    }

    public synchronized void finished() {
        this.finished = true;
    }

    @java.lang.Override
    public void run() {
        java.lang.String hostString = createHostString(sshHostInfo.getHosts());
        long bootstrapTimeout = calculateBSTimeout(sshHostInfo.getHosts().size());
        java.util.concurrent.ScheduledExecutorService scheduler = java.util.concurrent.Executors.newScheduledThreadPool(1);
        org.apache.ambari.server.bootstrap.BSRunner.BSStatusCollector statusCollector = new org.apache.ambari.server.bootstrap.BSRunner.BSStatusCollector();
        java.util.concurrent.ScheduledFuture<?> handle = null;
        org.apache.ambari.server.bootstrap.BSRunner.LOG.info("Kicking off the scheduler for polling on logs in " + this.requestIdDir);
        java.lang.String user = sshHostInfo.getUser();
        java.lang.String userRunAs = sshHostInfo.getUserRunAs();
        if ((user == null) || user.isEmpty()) {
            user = org.apache.ambari.server.bootstrap.BSRunner.DEFAULT_USER;
        }
        java.lang.String sshPort = sshHostInfo.getSshPort();
        if ((sshPort == null) || sshPort.isEmpty()) {
            sshPort = org.apache.ambari.server.bootstrap.BSRunner.DEFAULT_SSHPORT;
        }
        java.lang.String command[] = new java.lang.String[13];
        org.apache.ambari.server.bootstrap.BootStrapStatus.BSStat stat = org.apache.ambari.server.bootstrap.BootStrapStatus.BSStat.RUNNING;
        java.lang.String scriptlog = "";
        try {
            createRunDir();
            handle = scheduler.scheduleWithFixedDelay(statusCollector, 0, 10, java.util.concurrent.TimeUnit.SECONDS);
            if (org.apache.ambari.server.bootstrap.BSRunner.LOG.isDebugEnabled()) {
                org.apache.ambari.server.bootstrap.BSRunner.LOG.debug("Using ssh key=\"{}\"", sshHostInfo.getSshKey());
            }
            java.lang.String password = sshHostInfo.getPassword();
            if ((password != null) && (!password.isEmpty())) {
                this.passwordFile = new java.io.File(this.requestIdDir, "host_pass");
                java.lang.String lineSeparator = java.lang.System.getProperty("line.separator");
                password = password + lineSeparator;
                writePasswordFile(password);
            }
            writeSshKeyFile(sshHostInfo.getSshKey());
            command[0] = this.bsScript;
            command[1] = hostString;
            command[2] = this.requestIdDir.toString();
            command[3] = user;
            command[4] = sshPort;
            command[5] = this.sshKeyFile.toString();
            command[6] = this.agentSetupScript.toString();
            command[7] = this.ambariHostname;
            command[8] = this.clusterOsFamily;
            command[9] = this.projectVersion;
            command[10] = this.serverPort + "";
            command[11] = userRunAs;
            command[12] = (this.passwordFile == null) ? "null" : this.passwordFile.toString();
            java.util.Map<java.lang.String, java.lang.String> envVariables = new java.util.HashMap<>();
            if (java.lang.System.getProperty("os.name").contains("Windows")) {
                java.lang.String command2[] = new java.lang.String[command.length + 1];
                command2[0] = "python";
                java.lang.System.arraycopy(command, 0, command2, 1, command.length);
                command = command2;
                java.util.Map<java.lang.String, java.lang.String> envVarsWin = java.lang.System.getenv();
                if (envVarsWin != null) {
                    envVariables.putAll(envVarsWin);
                }
            }
            org.apache.ambari.server.bootstrap.BSRunner.LOG.info((((((((((((((((((((((("Host= " + hostString) + " bs=") + this.bsScript) + " requestDir=") + requestIdDir) + " user=") + user) + " sshPort=") + sshPort) + " keyfile=") + this.sshKeyFile) + " passwordFile ") + this.passwordFile) + " server=") + this.ambariHostname) + " version=") + projectVersion) + " serverPort=") + this.serverPort) + " userRunAs=") + userRunAs) + " timeout=") + (bootstrapTimeout / 1000));
            envVariables.put("AMBARI_PASSPHRASE", agentSetupPassword);
            if (this.verbose)
                envVariables.put("BS_VERBOSE", "\"-vvv\"");

            if (org.apache.ambari.server.bootstrap.BSRunner.LOG.isDebugEnabled()) {
                org.apache.ambari.server.bootstrap.BSRunner.LOG.debug(java.util.Arrays.toString(command));
            }
            java.lang.String bootStrapOutputFilePath = (requestIdDir + java.io.File.separator) + "bootstrap.out";
            java.lang.String bootStrapErrorFilePath = (requestIdDir + java.io.File.separator) + "bootstrap.err";
            java.lang.ProcessBuilder pb = new java.lang.ProcessBuilder(command);
            pb.redirectOutput(new java.io.File(bootStrapOutputFilePath));
            pb.redirectError(new java.io.File(bootStrapErrorFilePath));
            java.util.Map<java.lang.String, java.lang.String> env = pb.environment();
            env.putAll(envVariables);
            java.lang.Process process = pb.start();
            try {
                java.lang.String logInfoMessage = (((("Bootstrap output, log=" + bootStrapErrorFilePath) + " ") + bootStrapOutputFilePath) + " at ") + this.ambariHostname;
                org.apache.ambari.server.bootstrap.BSRunner.LOG.info(logInfoMessage);
                int exitCode = 1;
                boolean timedOut = false;
                if (waitForProcessTermination(process, bootstrapTimeout)) {
                    exitCode = process.exitValue();
                } else {
                    org.apache.ambari.server.bootstrap.BSRunner.LOG.warn("Bootstrap process timed out. It will be destroyed.");
                    process.destroy();
                    timedOut = true;
                }
                java.lang.String outMesg = "";
                java.lang.String errMesg = "";
                try {
                    outMesg = org.apache.commons.io.FileUtils.readFileToString(new java.io.File(bootStrapOutputFilePath), java.nio.charset.Charset.defaultCharset());
                    errMesg = org.apache.commons.io.FileUtils.readFileToString(new java.io.File(bootStrapErrorFilePath), java.nio.charset.Charset.defaultCharset());
                } catch (java.io.IOException io) {
                    org.apache.ambari.server.bootstrap.BSRunner.LOG.info("Error in reading files ", io);
                }
                scriptlog = (outMesg + "\n\n") + errMesg;
                if (timedOut) {
                    scriptlog += "\n\n Bootstrap process timed out. It was destroyed.";
                }
                org.apache.ambari.server.bootstrap.BSRunner.LOG.info("Script log Mesg " + scriptlog);
                if (exitCode != 0) {
                    stat = org.apache.ambari.server.bootstrap.BootStrapStatus.BSStat.ERROR;
                    interuptSetupAgent(99, scriptlog);
                } else {
                    stat = org.apache.ambari.server.bootstrap.BootStrapStatus.BSStat.SUCCESS;
                }
                scheduler.schedule(new org.apache.ambari.server.bootstrap.BSRunner.BSStatusCollector(), 0, java.util.concurrent.TimeUnit.SECONDS);
                long startTime = java.lang.System.currentTimeMillis();
                while (true) {
                    if (org.apache.ambari.server.bootstrap.BSRunner.LOG.isDebugEnabled()) {
                        org.apache.ambari.server.bootstrap.BSRunner.LOG.debug("Waiting for hosts status to be updated");
                    }
                    boolean pendingHosts = false;
                    org.apache.ambari.server.bootstrap.BootStrapStatus tmpStatus = bsImpl.getStatus(requestId);
                    java.util.List<org.apache.ambari.server.bootstrap.BSHostStatus> hostStatusList = tmpStatus.getHostsStatus();
                    if (hostStatusList != null) {
                        for (org.apache.ambari.server.bootstrap.BSHostStatus status : hostStatusList) {
                            if (status.getStatus().equals("RUNNING")) {
                                pendingHosts = true;
                            }
                        }
                    } else {
                        pendingHosts = true;
                    }
                    if (org.apache.ambari.server.bootstrap.BSRunner.LOG.isDebugEnabled()) {
                        org.apache.ambari.server.bootstrap.BSRunner.LOG.debug("Whether hosts status yet to be updated, pending={}", pendingHosts);
                    }
                    if (!pendingHosts) {
                        break;
                    }
                    try {
                        java.lang.Thread.sleep(1000);
                    } catch (java.lang.InterruptedException e) {
                    }
                    long now = java.lang.System.currentTimeMillis();
                    if (now >= (startTime + 15000)) {
                        org.apache.ambari.server.bootstrap.BSRunner.LOG.warn("Gave up waiting for hosts status to be updated");
                        break;
                    }
                } 
            } catch (java.lang.InterruptedException e) {
                throw new java.io.IOException(e);
            } finally {
                if (handle != null) {
                    handle.cancel(true);
                }
                scheduler.schedule(new org.apache.ambari.server.bootstrap.BSRunner.BSStatusCollector(), 0, java.util.concurrent.TimeUnit.SECONDS);
                scheduler.shutdownNow();
                try {
                    scheduler.awaitTermination(10, java.util.concurrent.TimeUnit.SECONDS);
                } catch (java.lang.InterruptedException e) {
                    org.apache.ambari.server.bootstrap.BSRunner.LOG.info("Interruped while waiting for scheduler");
                }
                process.destroy();
            }
        } catch (java.io.IOException io) {
            org.apache.ambari.server.bootstrap.BSRunner.LOG.info("Error executing bootstrap " + io.getMessage());
            stat = org.apache.ambari.server.bootstrap.BootStrapStatus.BSStat.ERROR;
            interuptSetupAgent(99, io.getMessage());
        } finally {
            org.apache.ambari.server.bootstrap.BootStrapStatus tmpStatus = bsImpl.getStatus(requestId);
            java.util.List<org.apache.ambari.server.bootstrap.BSHostStatus> hostStatusList = tmpStatus.getHostsStatus();
            if (hostStatusList != null) {
                for (org.apache.ambari.server.bootstrap.BSHostStatus hostStatus : hostStatusList) {
                    if ("FAILED".equals(hostStatus.getStatus())) {
                        stat = org.apache.ambari.server.bootstrap.BootStrapStatus.BSStat.ERROR;
                        break;
                    }
                }
            } else {
                stat = org.apache.ambari.server.bootstrap.BootStrapStatus.BSStat.ERROR;
            }
            org.apache.ambari.server.bootstrap.BootStrapStatus newStat = new org.apache.ambari.server.bootstrap.BootStrapStatus();
            newStat.setHostsStatus(hostStatusList);
            newStat.setLog(scriptlog);
            newStat.setStatus(stat);
            try {
                org.apache.commons.io.FileUtils.forceDelete(sshKeyFile);
            } catch (java.io.IOException io) {
                org.apache.ambari.server.bootstrap.BSRunner.LOG.warn(io.getMessage());
            }
            if (passwordFile != null) {
                try {
                    org.apache.commons.io.FileUtils.forceDelete(passwordFile);
                } catch (java.io.IOException io) {
                    org.apache.ambari.server.bootstrap.BSRunner.LOG.warn(io.getMessage());
                }
            }
            bsImpl.updateStatus(requestId, newStat);
            bsImpl.reset();
            finished();
        }
    }

    public synchronized void interuptSetupAgent(int exitCode, java.lang.String errMesg) {
        java.io.PrintWriter setupAgentDoneWriter = null;
        java.io.PrintWriter setupAgentLogWriter = null;
        try {
            for (java.lang.String host : sshHostInfo.getHosts()) {
                java.io.File doneFile = new java.io.File(requestIdDir, host + org.apache.ambari.server.bootstrap.BSHostStatusCollector.doneFileFilter);
                if (!doneFile.exists()) {
                    setupAgentDoneWriter = new java.io.PrintWriter(doneFile);
                    setupAgentDoneWriter.print(exitCode);
                    setupAgentDoneWriter.close();
                }
                java.io.File logFile = new java.io.File(requestIdDir, host + org.apache.ambari.server.bootstrap.BSHostStatusCollector.logFileFilter);
                if (!logFile.exists()) {
                    setupAgentLogWriter = new java.io.PrintWriter(logFile);
                    setupAgentLogWriter.print("Error while bootstrapping:\n" + errMesg);
                    setupAgentLogWriter.close();
                }
            }
        } catch (java.io.FileNotFoundException ex) {
            org.apache.ambari.server.bootstrap.BSRunner.LOG.error(ex.toString());
        } finally {
            if (setupAgentDoneWriter != null) {
                setupAgentDoneWriter.close();
            }
            if (setupAgentLogWriter != null) {
                setupAgentLogWriter.close();
            }
        }
    }

    public synchronized boolean isRunning() {
        return !this.finished;
    }
}