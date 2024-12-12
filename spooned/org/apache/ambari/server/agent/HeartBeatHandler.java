package org.apache.ambari.server.agent;
import org.apache.commons.collections.CollectionUtils;
@com.google.inject.Singleton
public class HeartBeatHandler {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.agent.HeartBeatHandler.class);

    private static final java.util.regex.Pattern DOT_PATTERN = java.util.regex.Pattern.compile("\\.");

    private final org.apache.ambari.server.state.Clusters clusterFsm;

    private final org.apache.ambari.server.security.encryption.Encryptor<org.apache.ambari.server.events.AgentConfigsUpdateEvent> encryptor;

    private org.apache.ambari.server.agent.HeartbeatMonitor heartbeatMonitor;

    private org.apache.ambari.server.agent.HeartbeatProcessor heartbeatProcessor;

    private org.apache.ambari.server.configuration.Configuration config;

    @com.google.inject.Inject
    private org.apache.ambari.server.api.services.AmbariMetaInfo ambariMetaInfo;

    @com.google.inject.Inject
    private org.apache.ambari.server.events.publishers.STOMPUpdatePublisher STOMPUpdatePublisher;

    @com.google.inject.Inject
    private org.apache.ambari.server.agent.AgentSessionManager agentSessionManager;

    @com.google.inject.Inject
    private org.apache.ambari.server.events.publishers.AmbariEventPublisher ambariEventPublisher;

    @com.google.inject.Inject
    private org.apache.ambari.server.state.alert.AlertHelper alertHelper;

    private java.util.Map<java.lang.String, java.lang.Long> hostResponseIds = new java.util.concurrent.ConcurrentHashMap<>();

    private java.util.Map<java.lang.String, org.apache.ambari.server.agent.HeartBeatResponse> hostResponses = new java.util.concurrent.ConcurrentHashMap<>();

    @com.google.inject.Inject
    public HeartBeatHandler(org.apache.ambari.server.configuration.Configuration c, org.apache.ambari.server.state.Clusters fsm, org.apache.ambari.server.actionmanager.ActionManager am, @javax.inject.Named("AgentConfigEncryptor")
    org.apache.ambari.server.security.encryption.Encryptor<org.apache.ambari.server.events.AgentConfigsUpdateEvent> encryptor, com.google.inject.Injector injector) {
        config = c;
        this.clusterFsm = fsm;
        this.encryptor = encryptor;
        heartbeatMonitor = new org.apache.ambari.server.agent.HeartbeatMonitor(fsm, am, config.getHeartbeatMonitorInterval(), injector);
        this.heartbeatProcessor = new org.apache.ambari.server.agent.HeartbeatProcessor(fsm, am, heartbeatMonitor, injector);
        injector.injectMembers(this);
    }

    public void start() {
        heartbeatProcessor.startAsync();
        heartbeatMonitor.start();
    }

    void setHeartbeatMonitor(org.apache.ambari.server.agent.HeartbeatMonitor heartbeatMonitor) {
        this.heartbeatMonitor = heartbeatMonitor;
    }

    public void setHeartbeatProcessor(org.apache.ambari.server.agent.HeartbeatProcessor heartbeatProcessor) {
        this.heartbeatProcessor = heartbeatProcessor;
    }

    public org.apache.ambari.server.agent.HeartbeatProcessor getHeartbeatProcessor() {
        return heartbeatProcessor;
    }

    public org.apache.ambari.server.agent.HeartBeatResponse handleHeartBeat(org.apache.ambari.server.agent.HeartBeat heartbeat) throws org.apache.ambari.server.AmbariException {
        long now = java.lang.System.currentTimeMillis();
        if ((heartbeat.getAgentEnv() != null) && (heartbeat.getAgentEnv().getHostHealth() != null)) {
            heartbeat.getAgentEnv().getHostHealth().setServerTimeStampAtReporting(now);
        }
        java.lang.String hostname = heartbeat.getHostname();
        java.lang.Long currentResponseId = hostResponseIds.get(hostname);
        org.apache.ambari.server.agent.HeartBeatResponse response;
        if (currentResponseId == null) {
            org.apache.ambari.server.agent.HeartBeatHandler.LOG.error(("CurrentResponseId unknown for " + hostname) + " - send register command");
            return createRegisterCommand();
        }
        org.apache.ambari.server.agent.HeartBeatHandler.LOG.debug("Received heartbeat from host, hostname={}, currentResponseId={}, receivedResponseId={}", hostname, currentResponseId, heartbeat.getResponseId());
        response = new org.apache.ambari.server.agent.HeartBeatResponse();
        org.apache.ambari.server.state.Host hostObject;
        try {
            hostObject = clusterFsm.getHost(hostname);
        } catch (org.apache.ambari.server.HostNotFoundException e) {
            org.apache.ambari.server.agent.HeartBeatHandler.LOG.error("Host: {} not found. Agent is still heartbeating.", hostname);
            if (org.apache.ambari.server.agent.HeartBeatHandler.LOG.isDebugEnabled()) {
                org.apache.ambari.server.agent.HeartBeatHandler.LOG.debug("Host associated with the agent heratbeat might have been " + "deleted", e);
            }
            return response;
        }
        if (heartbeat.getResponseId() == (currentResponseId - 1)) {
            org.apache.ambari.server.agent.HeartBeatResponse heartBeatResponse = hostResponses.get(hostname);
            org.apache.ambari.server.agent.HeartBeatHandler.LOG.warn("Old responseId={} received form host {} - response was lost - returning cached response with responseId={}", heartbeat.getResponseId(), hostname, heartBeatResponse.getResponseId());
            return heartBeatResponse;
        } else if (heartbeat.getResponseId() != currentResponseId) {
            org.apache.ambari.server.agent.HeartBeatHandler.LOG.error("Error in responseId sequence - received responseId={} from host {} - sending agent restart command with responseId={}", heartbeat.getResponseId(), hostname, currentResponseId);
            return createRestartCommand(currentResponseId);
        }
        response.setResponseId(++currentResponseId);
        if (hostObject.getState().equals(org.apache.ambari.server.state.HostState.HEARTBEAT_LOST)) {
            org.apache.ambari.server.agent.HeartBeatHandler.LOG.warn("Host {} is in HEARTBEAT_LOST state - sending register command", hostname);
            STOMPUpdatePublisher.publish(new org.apache.ambari.server.events.AgentActionEvent(org.apache.ambari.server.events.AgentActionEvent.AgentAction.RESTART_AGENT, hostObject.getHostId()));
            return createRegisterCommand();
        }
        hostResponseIds.put(hostname, currentResponseId);
        hostResponses.put(hostname, response);
        if (hostObject.getState().equals(org.apache.ambari.server.state.HostState.WAITING_FOR_HOST_STATUS_UPDATES)) {
            try {
                org.apache.ambari.server.agent.HeartBeatHandler.LOG.debug("Got component status updates for host {}", hostname);
                hostObject.handleEvent(new org.apache.ambari.server.state.host.HostStatusUpdatesReceivedEvent(hostname, now));
            } catch (org.apache.ambari.server.state.fsm.InvalidStateTransitionException e) {
                org.apache.ambari.server.agent.HeartBeatHandler.LOG.warn("Failed to notify the host {} about component status updates", hostname, e);
            }
        }
        if (heartbeat.getRecoveryReport() != null) {
            org.apache.ambari.server.agent.RecoveryReport rr = heartbeat.getRecoveryReport();
            processRecoveryReport(rr, hostname);
        }
        if (org.apache.commons.collections.CollectionUtils.isNotEmpty(heartbeat.getStaleAlerts())) {
            alertHelper.addStaleAlerts(hostObject.getHostId(), heartbeat.getStaleAlerts());
        }
        try {
            hostObject.handleEvent(new org.apache.ambari.server.state.host.HostHealthyHeartbeatEvent(hostname, now, heartbeat.getAgentEnv(), heartbeat.getMounts()));
        } catch (org.apache.ambari.server.state.fsm.InvalidStateTransitionException ex) {
            org.apache.ambari.server.agent.HeartBeatHandler.LOG.warn("Asking agent to re-register due to " + ex.getMessage(), ex);
            hostObject.setState(org.apache.ambari.server.state.HostState.INIT);
            return createRegisterCommand();
        }
        heartbeatProcessor.addHeartbeat(heartbeat);
        if (hostObject.getState().equals(org.apache.ambari.server.state.HostState.HEALTHY)) {
            annotateResponse(hostname, response);
        }
        return response;
    }

    public void handleComponentReportStatus(java.util.List<org.apache.ambari.server.agent.ComponentStatus> componentStatuses, java.lang.String hostname) throws org.apache.ambari.server.AmbariException {
        heartbeatProcessor.processStatusReports(componentStatuses, hostname);
        heartbeatProcessor.processHostStatus(componentStatuses, null, hostname);
    }

    public void handleCommandReportStatus(java.util.List<org.apache.ambari.server.agent.CommandReport> reports, java.lang.String hostname) throws org.apache.ambari.server.AmbariException {
        heartbeatProcessor.processCommandReports(reports, hostname, java.lang.System.currentTimeMillis());
        heartbeatProcessor.processHostStatus(null, reports, hostname);
    }

    public void handleHostReportStatus(org.apache.ambari.server.agent.stomp.dto.HostStatusReport hostStatusReport, java.lang.String hostname) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.Host host = clusterFsm.getHost(hostname);
        try {
            host.handleEvent(new org.apache.ambari.server.state.host.HostHealthyHeartbeatEvent(hostname, java.lang.System.currentTimeMillis(), hostStatusReport.getAgentEnv(), hostStatusReport.getMounts()));
        } catch (org.apache.ambari.server.state.fsm.InvalidStateTransitionException ex) {
            org.apache.ambari.server.agent.HeartBeatHandler.LOG.warn("Asking agent to re-register due to " + ex.getMessage(), ex);
            host.setState(org.apache.ambari.server.state.HostState.INIT);
            agentSessionManager.unregisterByHost(host.getHostId());
        }
    }

    public void handleComponentVersionReports(org.apache.ambari.server.agent.stomp.dto.ComponentVersionReports componentVersionReports, java.lang.String hostname) throws org.apache.ambari.server.AmbariException {
        heartbeatProcessor.processVersionReports(componentVersionReports, hostname);
    }

    protected void processRecoveryReport(org.apache.ambari.server.agent.RecoveryReport recoveryReport, java.lang.String hostname) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.agent.HeartBeatHandler.LOG.debug("Received recovery report: {}", recoveryReport);
        org.apache.ambari.server.state.Host host = clusterFsm.getHost(hostname);
        host.setRecoveryReport(recoveryReport);
    }

    public java.lang.String getOsType(java.lang.String os, java.lang.String osRelease) {
        java.lang.String osType = "";
        if (os != null) {
            osType = os;
        }
        if (osRelease != null) {
            java.lang.String[] release = org.apache.ambari.server.agent.HeartBeatHandler.DOT_PATTERN.split(osRelease);
            if (release.length > 0) {
                osType += release[0];
            }
        }
        return osType.toLowerCase();
    }

    public org.apache.ambari.server.agent.HeartBeatResponse createRegisterCommand() {
        org.apache.ambari.server.agent.HeartBeatResponse response = new org.apache.ambari.server.agent.HeartBeatResponse();
        org.apache.ambari.server.agent.RegistrationCommand regCmd = new org.apache.ambari.server.agent.RegistrationCommand();
        response.setResponseId(0);
        response.setRegistrationCommand(regCmd);
        return response;
    }

    protected org.apache.ambari.server.agent.HeartBeatResponse createRestartCommand(java.lang.Long currentResponseId) {
        org.apache.ambari.server.agent.HeartBeatResponse response = new org.apache.ambari.server.agent.HeartBeatResponse();
        response.setRestartAgent(true);
        response.setResponseId(currentResponseId);
        return response;
    }

    public org.apache.ambari.server.agent.RegistrationResponse handleRegistration(org.apache.ambari.server.agent.Register register) throws org.apache.ambari.server.state.fsm.InvalidStateTransitionException, org.apache.ambari.server.AmbariException {
        java.lang.String hostname = register.getHostname();
        int currentPingPort = register.getCurrentPingPort();
        long now = java.lang.System.currentTimeMillis();
        java.lang.String agentVersion = register.getAgentVersion();
        java.lang.String serverVersion = ambariMetaInfo.getServerVersion();
        if (!org.apache.ambari.server.utils.VersionUtils.areVersionsEqual(serverVersion, agentVersion, true)) {
            org.apache.ambari.server.agent.HeartBeatHandler.LOG.warn(((((("Received registration request from host with non compatible" + (" agent version" + ", hostname=")) + hostname) + ", agentVersion=") + agentVersion) + ", serverVersion=") + serverVersion);
            throw new org.apache.ambari.server.AmbariException(((((("Cannot register host with non compatible" + (" agent version" + ", hostname=")) + hostname) + ", agentVersion=") + agentVersion) + ", serverVersion=") + serverVersion);
        }
        java.lang.String agentOsType = getOsType(register.getHardwareProfile().getOS(), register.getHardwareProfile().getOSRelease());
        org.apache.ambari.server.agent.HeartBeatHandler.LOG.info("agentOsType = " + agentOsType);
        if (!ambariMetaInfo.isOsSupported(agentOsType)) {
            org.apache.ambari.server.agent.HeartBeatHandler.LOG.warn(((((("Received registration request from host with not supported" + (" os type" + ", hostname=")) + hostname) + ", serverOsType=") + config.getServerOsType()) + ", agentOsType=") + agentOsType);
            throw new org.apache.ambari.server.AmbariException(((((("Cannot register host with not supported" + (" os type" + ", hostname=")) + hostname) + ", serverOsType=") + config.getServerOsType()) + ", agentOsType=") + agentOsType);
        }
        org.apache.ambari.server.state.Host hostObject;
        try {
            hostObject = clusterFsm.getHost(hostname);
        } catch (org.apache.ambari.server.HostNotFoundException ex) {
            clusterFsm.addHost(hostname);
            hostObject = clusterFsm.getHost(hostname);
        }
        hostObject.setStateMachineState(org.apache.ambari.server.state.HostState.INIT);
        hostObject.setCurrentPingPort(currentPingPort);
        hostObject.setPrefix(register.getPrefix());
        alertHelper.clearStaleAlerts(hostObject.getHostId());
        hostObject.handleEvent(new org.apache.ambari.server.state.host.HostRegistrationRequestEvent(hostname, null != register.getPublicHostname() ? register.getPublicHostname() : hostname, new org.apache.ambari.server.state.AgentVersion(register.getAgentVersion()), now, register.getHardwareProfile(), register.getAgentEnv(), register.getAgentStartTime()));
        org.apache.ambari.server.events.HostRegisteredEvent event = new org.apache.ambari.server.events.HostRegisteredEvent(hostname, hostObject.getHostId());
        ambariEventPublisher.publish(event);
        if (config.shouldEncryptSensitiveData()) {
            org.apache.ambari.server.events.EncryptionKeyUpdateEvent encryptionKeyUpdateEvent = new org.apache.ambari.server.events.EncryptionKeyUpdateEvent(encryptor.getEncryptionKey());
            STOMPUpdatePublisher.publish(encryptionKeyUpdateEvent);
        }
        org.apache.ambari.server.agent.RegistrationResponse response = new org.apache.ambari.server.agent.RegistrationResponse();
        java.lang.Long requestId = 0L;
        hostResponseIds.put(hostname, requestId);
        response.setResponseId(requestId);
        return response;
    }

    private void annotateResponse(java.lang.String hostname, org.apache.ambari.server.agent.HeartBeatResponse response) throws org.apache.ambari.server.AmbariException {
        for (org.apache.ambari.server.state.Cluster cl : clusterFsm.getClustersForHost(hostname)) {
            response.setClusterSize(cl.getClusterSize());
            java.util.List<org.apache.ambari.server.state.ServiceComponentHost> scHosts = cl.getServiceComponentHosts(hostname);
            if ((scHosts != null) && (scHosts.size() > 0)) {
                response.setHasMappedComponents(true);
                break;
            }
        }
    }

    public org.apache.ambari.server.agent.ComponentsResponse handleComponents(java.lang.String clusterName) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.agent.ComponentsResponse response = new org.apache.ambari.server.agent.ComponentsResponse();
        org.apache.ambari.server.state.Cluster cluster = clusterFsm.getCluster(clusterName);
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> componentsMap = new java.util.HashMap<>();
        for (org.apache.ambari.server.state.Service service : cluster.getServices().values()) {
            componentsMap.put(service.getName(), new java.util.HashMap<>());
            for (org.apache.ambari.server.state.ServiceComponent component : service.getServiceComponents().values()) {
                org.apache.ambari.server.state.StackId stackId = component.getDesiredStackId();
                org.apache.ambari.server.state.ComponentInfo componentInfo = ambariMetaInfo.getComponent(stackId.getStackName(), stackId.getStackVersion(), service.getName(), component.getName());
                componentsMap.get(service.getName()).put(component.getName(), componentInfo.getCategory());
            }
        }
        response.setClusterName(clusterName);
        response.setComponents(componentsMap);
        return response;
    }

    public void stop() {
        heartbeatMonitor.shutdown();
        heartbeatProcessor.stopAsync();
    }
}