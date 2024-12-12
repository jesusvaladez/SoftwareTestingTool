package org.apache.ambari.server.bootstrap;
import static org.apache.ambari.server.utils.VersionUtils.DEV_VERSION;
@com.google.inject.Singleton
public class BootStrapImpl {
    private java.io.File bootStrapDir;

    private java.lang.String bootScript;

    private java.lang.String bootSetupAgentScript;

    private java.lang.String bootSetupAgentPassword;

    private org.apache.ambari.server.bootstrap.BSRunner bsRunner;

    private java.lang.String masterHostname;

    long timeout;

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.bootstrap.BootStrapImpl.class);

    int requestId = 0;

    private org.apache.ambari.server.bootstrap.FifoLinkedHashMap<java.lang.Long, org.apache.ambari.server.bootstrap.BootStrapStatus> bsStatus;

    private final java.lang.String clusterOsType;

    private final java.lang.String clusterOsFamily;

    private java.lang.String projectVersion;

    private int serverPort;

    @com.google.inject.Inject
    public BootStrapImpl(org.apache.ambari.server.configuration.Configuration conf, org.apache.ambari.server.api.services.AmbariMetaInfo ambariMetaInfo) throws java.io.IOException {
        bootStrapDir = conf.getBootStrapDir();
        bootScript = conf.getBootStrapScript();
        bootSetupAgentScript = conf.getBootSetupAgentScript();
        bootSetupAgentPassword = conf.getBootSetupAgentPassword();
        bsStatus = new org.apache.ambari.server.bootstrap.FifoLinkedHashMap<>();
        masterHostname = conf.getMasterHostname(java.net.InetAddress.getLocalHost().getCanonicalHostName());
        clusterOsType = conf.getServerOsType();
        clusterOsFamily = conf.getServerOsFamily();
        projectVersion = ambariMetaInfo.getServerVersion();
        projectVersion = (projectVersion.equals(org.apache.ambari.server.utils.VersionUtils.DEV_VERSION)) ? org.apache.ambari.server.utils.VersionUtils.DEV_VERSION.replace("$", "") : projectVersion;
        serverPort = (conf.getApiSSLAuthentication()) ? conf.getClientSSLApiPort() : conf.getClientApiPort();
    }

    public synchronized org.apache.ambari.server.bootstrap.BootStrapStatus getStatus(long requestId) {
        if (!bsStatus.containsKey(java.lang.Long.valueOf(requestId))) {
            return null;
        }
        return bsStatus.get(java.lang.Long.valueOf(requestId));
    }

    synchronized void updateStatus(long requestId, org.apache.ambari.server.bootstrap.BootStrapStatus status) {
        bsStatus.put(java.lang.Long.valueOf(requestId), status);
    }

    public synchronized void init() throws java.io.IOException {
        if (!bootStrapDir.exists()) {
            boolean mkdirs = bootStrapDir.mkdirs();
            if (!mkdirs) {
                throw new java.io.IOException(("Unable to make directory for " + "bootstrap ") + bootStrapDir);
            }
        }
    }

    public synchronized org.apache.ambari.server.bootstrap.BSResponse runBootStrap(org.apache.ambari.server.bootstrap.SshHostInfo info) {
        org.apache.ambari.server.bootstrap.BSResponse response = new org.apache.ambari.server.bootstrap.BSResponse();
        org.apache.ambari.server.bootstrap.BootStrapImpl.LOG.info("BootStrapping hosts " + info.hostListAsString());
        if (bsRunner != null) {
            response.setLog("BootStrap in Progress: Cannot Run more than one.");
            response.setStatus(org.apache.ambari.server.bootstrap.BSResponse.BSRunStat.ERROR);
            return response;
        }
        requestId++;
        if ((info.getHosts() == null) || info.getHosts().isEmpty()) {
            org.apache.ambari.server.bootstrap.BootStrapStatus status = new org.apache.ambari.server.bootstrap.BootStrapStatus();
            status.setLog("Host list is empty.");
            status.setHostsStatus(new java.util.ArrayList<>());
            status.setStatus(org.apache.ambari.server.bootstrap.BootStrapStatus.BSStat.ERROR);
            updateStatus(requestId, status);
            response.setStatus(org.apache.ambari.server.bootstrap.BSResponse.BSRunStat.OK);
            response.setLog("Host list is empty.");
            response.setRequestId(requestId);
            return response;
        } else {
            bsRunner = new org.apache.ambari.server.bootstrap.BSRunner(this, info, bootStrapDir.toString(), bootScript, bootSetupAgentScript, bootSetupAgentPassword, requestId, 0L, masterHostname, info.isVerbose(), clusterOsFamily, projectVersion, serverPort);
            bsRunner.start();
            response.setStatus(org.apache.ambari.server.bootstrap.BSResponse.BSRunStat.OK);
            response.setLog("Running Bootstrap now.");
            response.setRequestId(requestId);
            return response;
        }
    }

    public synchronized java.util.List<org.apache.ambari.server.bootstrap.BSHostStatus> getHostInfo(java.util.List<java.lang.String> hosts) {
        java.util.List<org.apache.ambari.server.bootstrap.BSHostStatus> statuses = new java.util.ArrayList<>();
        if (((null == hosts) || (0 == hosts.size())) || ((hosts.size() == 1) && hosts.get(0).equals("*"))) {
            for (org.apache.ambari.server.bootstrap.BootStrapStatus status : bsStatus.values()) {
                if (null != status.getHostsStatus()) {
                    statuses.addAll(status.getHostsStatus());
                }
            }
        } else {
            for (org.apache.ambari.server.bootstrap.BootStrapStatus status : bsStatus.values()) {
                for (org.apache.ambari.server.bootstrap.BSHostStatus hostStatus : status.getHostsStatus()) {
                    if ((-1) != hosts.indexOf(hostStatus.getHostName())) {
                        statuses.add(hostStatus);
                    }
                }
            }
        }
        return statuses;
    }

    public synchronized void reset() {
        bsRunner = null;
    }
}