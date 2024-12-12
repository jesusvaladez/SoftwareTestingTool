package org.apache.ambari.server.bootstrap;
class BSHostStatusCollector {
    private java.io.File requestIdDir;

    private java.util.List<org.apache.ambari.server.bootstrap.BSHostStatus> hostStatus;

    public static final java.lang.String logFileFilter = ".log";

    public static final java.lang.String doneFileFilter = ".done";

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.bootstrap.BSHostStatusCollector.class);

    private java.util.List<java.lang.String> hosts;

    public BSHostStatusCollector(java.io.File requestIdDir, java.util.List<java.lang.String> hosts) {
        this.requestIdDir = requestIdDir;
        this.hosts = hosts;
    }

    public java.util.List<org.apache.ambari.server.bootstrap.BSHostStatus> getHostStatus() {
        return hostStatus;
    }

    public void run() {
        org.apache.ambari.server.bootstrap.BSHostStatusCollector.LOG.info("Request directory " + requestIdDir);
        hostStatus = new java.util.ArrayList<>();
        if (hosts == null) {
            return;
        }
        java.io.File done;
        java.io.File log;
        org.apache.ambari.server.bootstrap.BSHostStatusCollector.LOG.info("HostList for polling on " + hosts);
        for (java.lang.String host : hosts) {
            org.apache.ambari.server.bootstrap.BSHostStatus status = new org.apache.ambari.server.bootstrap.BSHostStatus();
            status.setHostName(host);
            done = new java.io.File(requestIdDir, host + org.apache.ambari.server.bootstrap.BSHostStatusCollector.doneFileFilter);
            log = new java.io.File(requestIdDir, host + org.apache.ambari.server.bootstrap.BSHostStatusCollector.logFileFilter);
            if (org.apache.ambari.server.bootstrap.BSHostStatusCollector.LOG.isDebugEnabled()) {
                org.apache.ambari.server.bootstrap.BSHostStatusCollector.LOG.debug("Polling bootstrap status for host, requestDir={}, host={}, doneFileExists={}, logFileExists={}", requestIdDir, host, done.exists(), log.exists());
            }
            if (!done.exists()) {
                status.setStatus("RUNNING");
            } else {
                status.setStatus("FAILED");
                try {
                    java.lang.String statusCode = org.apache.commons.io.FileUtils.readFileToString(done, java.nio.charset.Charset.defaultCharset()).trim();
                    if (statusCode.equals("0")) {
                        status.setStatus("DONE");
                    }
                    updateStatus(status, statusCode);
                } catch (java.io.IOException e) {
                    org.apache.ambari.server.bootstrap.BSHostStatusCollector.LOG.info("Error reading done file " + done);
                }
            }
            if (!log.exists()) {
                status.setLog("");
            } else {
                java.lang.String logString = "";
                java.io.BufferedReader reader = null;
                try {
                    java.lang.StringBuilder sb = new java.lang.StringBuilder();
                    reader = new java.io.BufferedReader(new java.io.FileReader(log));
                    java.lang.String line = null;
                    while (null != (line = reader.readLine())) {
                        if (line.startsWith("tcgetattr:") || line.startsWith("tput:"))
                            continue;

                        if ((0 != sb.length()) || (0 == line.length()))
                            sb.append('\n');

                        if ((-1) != line.indexOf("\\n"))
                            sb.append(line.replace("\\n", "\n"));
                        else
                            sb.append(line);

                    } 
                    logString = sb.toString();
                } catch (java.io.IOException e) {
                    org.apache.ambari.server.bootstrap.BSHostStatusCollector.LOG.info(("Error reading log file " + log) + ". Log file may be have not created yet");
                } finally {
                    org.apache.ambari.server.utils.Closeables.closeSilently(reader);
                }
                status.setLog(logString);
            }
            hostStatus.add(status);
        }
    }

    private void updateStatus(org.apache.ambari.server.bootstrap.BSHostStatus status, java.lang.String statusCode) {
        status.setStatusCode(statusCode);
        int reason = -1;
        try {
            reason = java.lang.Integer.parseInt(statusCode);
        } catch (java.lang.Exception ignored) {
        }
        switch (reason) {
            case 2 :
                status.setStatusAction("Processing could not continue because the file was not found.");
                break;
            case 255 :
            default :
                if (null != status.getLog()) {
                    java.lang.String lowerLog = status.getLog().toLowerCase();
                    if (((-1) != lowerLog.indexOf("permission denied")) && ((-1) != lowerLog.indexOf("publickey"))) {
                        status.setStatusAction("Use correct SSH key");
                    } else if ((-1) != lowerLog.indexOf("connect to host")) {
                        status.setStatusAction(("Please verify that the hostname '" + status.getHostName()) + "' is correct.");
                    }
                }
                break;
        }
    }
}