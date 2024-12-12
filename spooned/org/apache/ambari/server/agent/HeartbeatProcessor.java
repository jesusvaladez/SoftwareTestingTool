package org.apache.ambari.server.agent;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import static org.apache.ambari.server.controller.KerberosHelperImpl.CHECK_KEYTABS;
import static org.apache.ambari.server.controller.KerberosHelperImpl.SET_KEYTAB;
public class HeartbeatProcessor extends com.google.common.util.concurrent.AbstractService {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.agent.HeartbeatProcessor.class);

    private java.util.concurrent.ScheduledExecutorService executor;

    private java.util.concurrent.ConcurrentLinkedQueue<org.apache.ambari.server.agent.HeartBeat> heartBeatsQueue = new java.util.concurrent.ConcurrentLinkedQueue<>();

    private volatile boolean shouldRun = true;

    private long delay = 5000;

    private long period = 1000;

    private int poolSize = 1;

    private org.apache.ambari.server.state.Clusters clusterFsm;

    private org.apache.ambari.server.agent.HeartbeatMonitor heartbeatMonitor;

    private com.google.inject.Injector injector;

    private org.apache.ambari.server.actionmanager.ActionManager actionManager;

    @com.google.inject.Inject
    org.apache.ambari.server.events.publishers.AlertEventPublisher alertEventPublisher;

    @com.google.inject.Inject
    org.apache.ambari.server.events.publishers.AmbariEventPublisher ambariEventPublisher;

    @com.google.inject.Inject
    org.apache.ambari.server.events.publishers.VersionEventPublisher versionEventPublisher;

    @com.google.inject.Inject
    org.apache.ambari.server.metadata.ActionMetadata actionMetadata;

    @com.google.inject.Inject
    org.apache.ambari.server.controller.MaintenanceStateHelper maintenanceStateHelper;

    @com.google.inject.Inject
    org.apache.ambari.server.api.services.AmbariMetaInfo ambariMetaInfo;

    @com.google.inject.Inject
    org.apache.ambari.server.orm.dao.KerberosKeytabPrincipalDAO kerberosKeytabPrincipalDAO;

    @com.google.inject.Inject
    org.apache.ambari.server.orm.dao.KerberosKeytabDAO kerberosKeytabDAO;

    @com.google.inject.Inject
    com.google.gson.Gson gson;

    @com.google.inject.Inject
    public HeartbeatProcessor(org.apache.ambari.server.state.Clusters clusterFsm, org.apache.ambari.server.actionmanager.ActionManager am, org.apache.ambari.server.agent.HeartbeatMonitor heartbeatMonitor, com.google.inject.Injector injector) {
        injector.injectMembers(this);
        this.injector = injector;
        this.heartbeatMonitor = heartbeatMonitor;
        this.clusterFsm = clusterFsm;
        actionManager = am;
        java.util.concurrent.ThreadFactory threadFactory = new com.google.common.util.concurrent.ThreadFactoryBuilder().setNameFormat("ambari-heartbeat-processor-%d").build();
        executor = java.util.concurrent.Executors.newScheduledThreadPool(poolSize, threadFactory);
    }

    @java.lang.Override
    protected void doStart() {
        org.apache.ambari.server.agent.HeartbeatProcessor.LOG.info("**** Starting heartbeats processing threads ****");
        for (int i = 0; i < poolSize; i++) {
            executor.scheduleAtFixedRate(new org.apache.ambari.server.agent.HeartbeatProcessor.HeartbeatProcessingTask(), delay, period, java.util.concurrent.TimeUnit.MILLISECONDS);
        }
    }

    @java.lang.Override
    protected void doStop() {
        org.apache.ambari.server.agent.HeartbeatProcessor.LOG.info("**** Stopping heartbeats processing threads ****");
        shouldRun = false;
        executor.shutdown();
    }

    public void addHeartbeat(org.apache.ambari.server.agent.HeartBeat heartBeat) {
        heartBeatsQueue.add(heartBeat);
    }

    private org.apache.ambari.server.agent.HeartBeat pollHeartbeat() {
        return heartBeatsQueue.poll();
    }

    private class HeartbeatProcessingTask implements java.lang.Runnable {
        @java.lang.Override
        public void run() {
            while (shouldRun) {
                try {
                    org.apache.ambari.server.agent.HeartBeat heartbeat = pollHeartbeat();
                    if (heartbeat == null) {
                        break;
                    }
                    processHeartbeat(heartbeat);
                } catch (java.lang.Exception e) {
                    org.apache.ambari.server.agent.HeartbeatProcessor.LOG.error("Exception received while processing heartbeat", e);
                } catch (java.lang.Throwable throwable) {
                    org.apache.ambari.server.agent.HeartbeatProcessor.LOG.error("ERROR: ", throwable);
                }
            } 
        }
    }

    public void processHeartbeat(org.apache.ambari.server.agent.HeartBeat heartbeat) throws org.apache.ambari.server.AmbariException {
        long now = java.lang.System.currentTimeMillis();
        processAlerts(heartbeat);
        processStatusReports(heartbeat);
        processCommandReports(heartbeat, now);
        processHostStatus(heartbeat);
    }

    protected void processAlerts(org.apache.ambari.server.agent.HeartBeat heartbeat) {
        if (heartbeat != null) {
            processAlerts(heartbeat.getHostname(), heartbeat.getAlerts());
        }
    }

    public void processAlerts(java.lang.String hostname, java.util.List<org.apache.ambari.server.state.Alert> alerts) {
        if ((alerts != null) && (!alerts.isEmpty())) {
            for (org.apache.ambari.server.state.Alert alert : alerts) {
                if (alert.getHostName() == null) {
                    alert.setHostName(hostname);
                }
            }
            org.apache.ambari.server.events.AlertEvent event = new org.apache.ambari.server.events.AlertReceivedEvent(alerts);
            alertEventPublisher.publish(event);
        }
    }

    protected void processHostStatus(org.apache.ambari.server.agent.HeartBeat heartbeat) throws org.apache.ambari.server.AmbariException {
        processHostStatus(heartbeat.getComponentStatus(), heartbeat.getReports(), heartbeat.getHostname());
    }

    protected void processHostStatus(java.util.List<org.apache.ambari.server.agent.ComponentStatus> componentStatuses, java.util.List<org.apache.ambari.server.agent.CommandReport> reports, java.lang.String hostName) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.Host host = clusterFsm.getHost(hostName);
        org.apache.ambari.server.state.HostHealthStatus.HealthStatus healthStatus = host.getHealthStatus().getHealthStatus();
        if (!healthStatus.equals(org.apache.ambari.server.state.HostHealthStatus.HealthStatus.UNKNOWN)) {
            boolean calculateHostStatus = false;
            java.lang.Long clusterId = null;
            if (org.apache.commons.collections.CollectionUtils.isNotEmpty(componentStatuses)) {
                calculateHostStatus = true;
                clusterId = componentStatuses.stream().findFirst().map(org.apache.ambari.server.agent.ComponentStatus::getClusterId).orElse(null);
            }
            if ((!calculateHostStatus) && org.apache.commons.collections.CollectionUtils.isNotEmpty(reports)) {
                for (org.apache.ambari.server.agent.CommandReport report : reports) {
                    if (org.apache.ambari.server.RoleCommand.ACTIONEXECUTE.toString().equals(report.getRoleCommand())) {
                        continue;
                    }
                    java.lang.String service = report.getServiceName();
                    if (actionMetadata.getActions(service.toLowerCase()).contains(report.getRole())) {
                        continue;
                    }
                    if (report.getStatus().equals("COMPLETED")) {
                        calculateHostStatus = true;
                        clusterId = java.lang.Long.parseLong(report.getClusterId());
                        break;
                    }
                }
            }
            if (calculateHostStatus) {
                host.calculateHostStatus(clusterId);
            }
            if (clusterFsm.getClustersForHost(host.getHostName()).size() == 0) {
                healthStatus = org.apache.ambari.server.state.HostHealthStatus.HealthStatus.HEALTHY;
                host.setStatus(healthStatus.name());
            }
        }
    }

    protected void processCommandReports(org.apache.ambari.server.agent.HeartBeat heartbeat, long now) throws org.apache.ambari.server.AmbariException {
        processCommandReports(heartbeat.getReports(), heartbeat.getHostname(), now);
    }

    protected void processCommandReports(java.util.List<org.apache.ambari.server.agent.CommandReport> reports, java.lang.String hostName, java.lang.Long now) throws org.apache.ambari.server.AmbariException {
        java.util.List<java.lang.Long> taskIds = new java.util.ArrayList<>();
        for (org.apache.ambari.server.agent.CommandReport report : reports) {
            taskIds.add(report.getTaskId());
        }
        java.util.Map<java.lang.Long, org.apache.ambari.server.actionmanager.HostRoleCommand> commands = actionManager.getTasksMap(taskIds);
        for (org.apache.ambari.server.agent.CommandReport report : reports) {
            java.lang.Long clusterId = java.lang.Long.parseLong(report.getClusterId());
            org.apache.ambari.server.agent.HeartbeatProcessor.LOG.debug("Received command report: {}", report);
            org.apache.ambari.server.state.Host host = clusterFsm.getHost(hostName);
            if (host == null) {
                org.apache.ambari.server.agent.HeartbeatProcessor.LOG.error("Received a command report and was unable to retrieve Host for hostname = " + hostName);
                continue;
            }
            if ((org.apache.ambari.server.RoleCommand.valueOf(report.getRoleCommand()) == org.apache.ambari.server.RoleCommand.ACTIONEXECUTE) && org.apache.ambari.server.actionmanager.HostRoleStatus.valueOf(report.getStatus()).isCompletedState()) {
                org.apache.ambari.server.events.ActionFinalReportReceivedEvent event = new org.apache.ambari.server.events.ActionFinalReportReceivedEvent(clusterId, hostName, report, false);
                ambariEventPublisher.publish(event);
            }
            org.apache.ambari.server.actionmanager.HostRoleCommand hostRoleCommand = commands.get(report.getTaskId());
            if (hostRoleCommand == null) {
                org.apache.ambari.server.agent.HeartbeatProcessor.LOG.warn("Can't fetch HostRoleCommand with taskId = " + report.getTaskId());
            } else {
                if (hostRoleCommand.getStatus() == org.apache.ambari.server.actionmanager.HostRoleStatus.ABORTED) {
                    continue;
                }
                if ((hostRoleCommand.getStatus() == org.apache.ambari.server.actionmanager.HostRoleStatus.QUEUED) && report.getStatus().equals("IN_PROGRESS")) {
                    hostRoleCommand.setStartTime(now);
                    if (hostRoleCommand.getOriginalStartTime() == (-1)) {
                        hostRoleCommand.setOriginalStartTime(now);
                    }
                }
            }
            if (((org.apache.ambari.server.state.Service.Type.KERBEROS.name().equalsIgnoreCase(report.getServiceName()) && org.apache.ambari.server.Role.KERBEROS_CLIENT.name().equalsIgnoreCase(report.getRole())) && org.apache.ambari.server.RoleCommand.CUSTOM_COMMAND.name().equalsIgnoreCase(report.getRoleCommand())) && org.apache.ambari.server.state.scheduler.RequestExecution.Status.COMPLETED.name().equalsIgnoreCase(report.getStatus())) {
                java.lang.String customCommand = report.getCustomCommand();
                if (org.apache.ambari.server.controller.KerberosHelperImpl.SET_KEYTAB.equalsIgnoreCase(customCommand)) {
                    org.apache.ambari.server.agent.HeartbeatProcessor.WriteKeytabsStructuredOut writeKeytabsStructuredOut;
                    try {
                        writeKeytabsStructuredOut = gson.fromJson(report.getStructuredOut(), org.apache.ambari.server.agent.HeartbeatProcessor.WriteKeytabsStructuredOut.class);
                    } catch (com.google.gson.JsonSyntaxException ex) {
                        writeKeytabsStructuredOut = null;
                    }
                    if (writeKeytabsStructuredOut != null) {
                        java.util.Map<java.lang.String, java.lang.String> keytabs = writeKeytabsStructuredOut.getKeytabs();
                        if (keytabs != null) {
                            for (java.util.Map.Entry<java.lang.String, java.lang.String> entry : keytabs.entrySet()) {
                                java.lang.String keytabPath = entry.getValue();
                                for (org.apache.ambari.server.orm.entities.KerberosKeytabPrincipalEntity kkpe : kerberosKeytabPrincipalDAO.findByHostAndKeytab(host.getHostId(), keytabPath)) {
                                    kkpe.setDistributed(true);
                                    kerberosKeytabPrincipalDAO.merge(kkpe);
                                }
                            }
                        }
                    }
                } else if (org.apache.ambari.server.controller.KerberosHelperImpl.CHECK_KEYTABS.equalsIgnoreCase(customCommand)) {
                    org.apache.ambari.server.agent.HeartbeatProcessor.ListKeytabsStructuredOut structuredOut = gson.fromJson(report.getStructuredOut(), org.apache.ambari.server.agent.HeartbeatProcessor.ListKeytabsStructuredOut.class);
                    for (org.apache.ambari.server.agent.HeartbeatProcessor.MissingKeytab each : structuredOut.missingKeytabs) {
                        org.apache.ambari.server.agent.HeartbeatProcessor.LOG.info("Missing principal: {} for keytab: {} on host: {}", each.principal, each.keytabFilePath, hostName);
                        org.apache.ambari.server.orm.entities.KerberosKeytabPrincipalEntity kkpe = kerberosKeytabPrincipalDAO.findByHostKeytabAndPrincipal(host.getHostId(), each.keytabFilePath, each.principal);
                        kkpe.setDistributed(false);
                        kerberosKeytabPrincipalDAO.merge(kkpe);
                    }
                }
            }
            if (org.apache.ambari.server.RoleCommand.ACTIONEXECUTE.toString().equals(report.getRoleCommand()) || (org.apache.ambari.server.RoleCommand.CUSTOM_COMMAND.toString().equals(report.getRoleCommand()) && (!(("RESTART".equals(report.getCustomCommand()) || "START".equals(report.getCustomCommand())) || "STOP".equals(report.getCustomCommand()))))) {
                continue;
            }
            org.apache.ambari.server.state.Cluster cl = clusterFsm.getCluster(java.lang.Long.parseLong(report.getClusterId()));
            java.lang.String service = report.getServiceName();
            if ((service == null) || service.isEmpty()) {
                throw new org.apache.ambari.server.AmbariException("Invalid command report, service: " + service);
            }
            if (actionMetadata.getActions(service.toLowerCase()).contains(report.getRole())) {
                org.apache.ambari.server.agent.HeartbeatProcessor.LOG.debug("{} is an action - skip component lookup", report.getRole());
            } else {
                try {
                    org.apache.ambari.server.state.Service svc = cl.getService(service);
                    org.apache.ambari.server.state.ServiceComponent svcComp = svc.getServiceComponent(report.getRole());
                    org.apache.ambari.server.state.ServiceComponentHost scHost = svcComp.getServiceComponentHost(hostName);
                    java.lang.String schName = scHost.getServiceComponentName();
                    if (report.getStatus().equals(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED.toString())) {
                        if (org.apache.commons.lang.StringUtils.isNotBlank(report.getStructuredOut()) && (!org.apache.commons.lang.StringUtils.equals("{}", report.getStructuredOut()))) {
                            org.apache.ambari.server.agent.HeartbeatProcessor.ComponentVersionStructuredOut structuredOutput = null;
                            try {
                                structuredOutput = gson.fromJson(report.getStructuredOut(), org.apache.ambari.server.agent.HeartbeatProcessor.ComponentVersionStructuredOut.class);
                            } catch (com.google.gson.JsonSyntaxException ex) {
                            }
                            java.lang.String newVersion = (structuredOutput == null) ? null : structuredOutput.version;
                            java.lang.Long repoVersionId = (structuredOutput == null) ? null : structuredOutput.repositoryVersionId;
                            org.apache.ambari.server.events.HostComponentVersionAdvertisedEvent event = new org.apache.ambari.server.events.HostComponentVersionAdvertisedEvent(cl, scHost, newVersion, repoVersionId);
                            versionEventPublisher.publish(event);
                        }
                        if ((!scHost.getState().equals(org.apache.ambari.server.state.State.UPGRADING)) && (report.getRoleCommand().equals(org.apache.ambari.server.RoleCommand.START.toString()) || (report.getRoleCommand().equals(org.apache.ambari.server.RoleCommand.CUSTOM_COMMAND.toString()) && ("START".equals(report.getCustomCommand()) || "RESTART".equals(report.getCustomCommand()))))) {
                            scHost.setRestartRequired(false);
                        }
                        if ((org.apache.ambari.server.RoleCommand.INSTALL.toString().equals(report.getRoleCommand()) || (org.apache.ambari.server.RoleCommand.CUSTOM_COMMAND.toString().equals(report.getRoleCommand()) && "INSTALL".equals(report.getCustomCommand()))) && svcComp.isClientComponent()) {
                            scHost.setRestartRequired(false);
                        }
                        if (org.apache.ambari.server.RoleCommand.CUSTOM_COMMAND.toString().equals(report.getRoleCommand()) && (!("START".equals(report.getCustomCommand()) || "STOP".equals(report.getCustomCommand())))) {
                            continue;
                        }
                        if (org.apache.ambari.server.RoleCommand.START.toString().equals(report.getRoleCommand()) || (org.apache.ambari.server.RoleCommand.CUSTOM_COMMAND.toString().equals(report.getRoleCommand()) && "START".equals(report.getCustomCommand()))) {
                            scHost.handleEvent(new org.apache.ambari.server.state.svccomphost.ServiceComponentHostStartedEvent(schName, hostName, now));
                            scHost.setRestartRequired(false);
                        } else if (org.apache.ambari.server.RoleCommand.STOP.toString().equals(report.getRoleCommand()) || (org.apache.ambari.server.RoleCommand.CUSTOM_COMMAND.toString().equals(report.getRoleCommand()) && "STOP".equals(report.getCustomCommand()))) {
                            scHost.handleEvent(new org.apache.ambari.server.state.svccomphost.ServiceComponentHostStoppedEvent(schName, hostName, now));
                        } else {
                            scHost.handleEvent(new org.apache.ambari.server.state.svccomphost.ServiceComponentHostOpSucceededEvent(schName, hostName, now));
                        }
                    } else if (report.getStatus().equals("FAILED")) {
                        if (org.apache.commons.lang.StringUtils.isNotBlank(report.getStructuredOut())) {
                            try {
                                org.apache.ambari.server.agent.HeartbeatProcessor.ComponentVersionStructuredOut structuredOutput = gson.fromJson(report.getStructuredOut(), org.apache.ambari.server.agent.HeartbeatProcessor.ComponentVersionStructuredOut.class);
                                if (null != structuredOutput.upgradeDirection) {
                                    scHost.setUpgradeState(org.apache.ambari.server.state.UpgradeState.FAILED);
                                }
                            } catch (com.google.gson.JsonSyntaxException ex) {
                                org.apache.ambari.server.agent.HeartbeatProcessor.LOG.warn("Structured output was found, but not parseable: {}", report.getStructuredOut());
                            }
                        }
                        org.apache.ambari.server.agent.HeartbeatProcessor.LOG.error((((((("Operation failed - may be retried. Service component host: " + schName) + ", host: ") + hostName) + " Action id ") + report.getActionId()) + " and taskId ") + report.getTaskId());
                        if (actionManager.isInProgressCommand(report)) {
                            scHost.handleEvent(new org.apache.ambari.server.state.svccomphost.ServiceComponentHostOpFailedEvent(schName, hostName, now));
                        } else {
                            org.apache.ambari.server.agent.HeartbeatProcessor.LOG.info("Received report for a command that is no longer active. " + report);
                        }
                    } else if (report.getStatus().equals("IN_PROGRESS")) {
                        scHost.handleEvent(new org.apache.ambari.server.state.svccomphost.ServiceComponentHostOpInProgressEvent(schName, hostName, now));
                    }
                } catch (org.apache.ambari.server.ServiceComponentNotFoundException scnex) {
                    org.apache.ambari.server.agent.HeartbeatProcessor.LOG.warn("Service component not found ", scnex);
                } catch (org.apache.ambari.server.state.fsm.InvalidStateTransitionException ex) {
                    if (org.apache.ambari.server.agent.HeartbeatProcessor.LOG.isDebugEnabled()) {
                        org.apache.ambari.server.agent.HeartbeatProcessor.LOG.warn("State machine exception.", ex);
                    } else {
                        org.apache.ambari.server.agent.HeartbeatProcessor.LOG.warn("State machine exception. " + ex.getMessage());
                    }
                }
            }
        }
        actionManager.processTaskResponse(hostName, reports, commands);
    }

    protected void processStatusReports(org.apache.ambari.server.agent.HeartBeat heartbeat) throws org.apache.ambari.server.AmbariException {
        processStatusReports(heartbeat.getComponentStatus(), heartbeat.getHostname());
    }

    public void processVersionReports(org.apache.ambari.server.agent.stomp.dto.ComponentVersionReports versionReports, java.lang.String hostname) throws org.apache.ambari.server.AmbariException {
        java.util.Set<org.apache.ambari.server.state.Cluster> clusters = clusterFsm.getClustersForHost(hostname);
        for (org.apache.ambari.server.state.Cluster cl : clusters) {
            for (java.util.Map.Entry<java.lang.String, java.util.List<org.apache.ambari.server.agent.stomp.dto.ComponentVersionReport>> status : versionReports.getComponentVersionReports().entrySet()) {
                if (java.lang.Long.valueOf(status.getKey()).equals(cl.getClusterId())) {
                    for (org.apache.ambari.server.agent.stomp.dto.ComponentVersionReport versionReport : status.getValue()) {
                        try {
                            org.apache.ambari.server.state.Service svc = cl.getService(versionReport.getServiceName());
                            java.lang.String componentName = versionReport.getComponentName();
                            if (svc.getServiceComponents().containsKey(componentName)) {
                                org.apache.ambari.server.state.ServiceComponent svcComp = svc.getServiceComponent(componentName);
                                org.apache.ambari.server.state.ServiceComponentHost scHost = svcComp.getServiceComponentHost(hostname);
                                java.lang.String version = versionReport.getVersion();
                                org.apache.ambari.server.events.HostComponentVersionAdvertisedEvent event = new org.apache.ambari.server.events.HostComponentVersionAdvertisedEvent(cl, scHost, version);
                                versionEventPublisher.publish(event);
                            }
                        } catch (org.apache.ambari.server.ServiceNotFoundException e) {
                            org.apache.ambari.server.agent.HeartbeatProcessor.LOG.warn(((("Received a version report for a non-initialized" + (" service" + ", clusterId=")) + versionReport.getClusterId()) + ", serviceName=") + versionReport.getServiceName());
                            continue;
                        } catch (org.apache.ambari.server.ServiceComponentNotFoundException e) {
                            org.apache.ambari.server.agent.HeartbeatProcessor.LOG.warn(((((("Received a version report for a non-initialized" + (" servicecomponent" + ", clusterId=")) + versionReport.getClusterId()) + ", serviceName=") + versionReport.getServiceName()) + ", componentName=") + versionReport.getComponentName());
                            continue;
                        } catch (org.apache.ambari.server.ServiceComponentHostNotFoundException e) {
                            org.apache.ambari.server.agent.HeartbeatProcessor.LOG.warn(((((((("Received a version report for a non-initialized" + (" hostcomponent" + ", clusterId=")) + versionReport.getClusterId()) + ", serviceName=") + versionReport.getServiceName()) + ", componentName=") + versionReport.getComponentName()) + ", hostname=") + hostname);
                            continue;
                        } catch (java.lang.RuntimeException e) {
                            org.apache.ambari.server.agent.HeartbeatProcessor.LOG.warn(((((((((("Received a version report with invalid payload" + (" service" + ", clusterId=")) + versionReport.getClusterId()) + ", serviceName=") + versionReport.getServiceName()) + ", componentName=") + versionReport.getComponentName()) + ", hostname=") + hostname) + ", error=") + e.getMessage());
                            continue;
                        }
                    }
                }
            }
        }
    }

    public void processStatusReports(java.util.List<org.apache.ambari.server.agent.ComponentStatus> componentStatuses, java.lang.String hostname) throws org.apache.ambari.server.AmbariException {
        java.util.Set<org.apache.ambari.server.state.Cluster> clusters = clusterFsm.getClustersForHost(hostname);
        for (org.apache.ambari.server.state.Cluster cl : clusters) {
            for (org.apache.ambari.server.agent.ComponentStatus status : componentStatuses) {
                if (status.getClusterId().equals(cl.getClusterId())) {
                    try {
                        org.apache.ambari.server.state.Service svc = cl.getService(status.getServiceName());
                        java.lang.String componentName = status.getComponentName();
                        if (svc.getServiceComponents().containsKey(componentName)) {
                            org.apache.ambari.server.state.ServiceComponent svcComp = svc.getServiceComponent(componentName);
                            org.apache.ambari.server.state.ServiceComponentHost scHost = svcComp.getServiceComponentHost(hostname);
                            if (status.getStatus() != null) {
                                org.apache.ambari.server.state.State prevState = scHost.getState();
                                org.apache.ambari.server.state.State liveState = org.apache.ambari.server.state.State.valueOf(org.apache.ambari.server.state.State.class, status.getStatus());
                                if ((prevState.equals(org.apache.ambari.server.state.State.INSTALLED) || prevState.equals(org.apache.ambari.server.state.State.STARTED)) || prevState.equals(org.apache.ambari.server.state.State.UNKNOWN)) {
                                    scHost.setState(liveState);
                                    if (!prevState.equals(liveState)) {
                                        org.apache.ambari.server.agent.HeartbeatProcessor.LOG.info(((((((((((("State of service component " + componentName) + " of service ") + status.getServiceName()) + " of cluster ") + status.getClusterId()) + " has changed from ") + prevState) + " to ") + liveState) + " at host ") + hostname) + " according to STATUS_COMMAND report");
                                    }
                                }
                            }
                            java.util.Map<java.lang.String, java.lang.Object> extra = status.getExtra();
                            if ((null != extra) && (!extra.isEmpty())) {
                                try {
                                    if (extra.containsKey("processes")) {
                                        @java.lang.SuppressWarnings("unchecked")
                                        java.util.List<java.util.Map<java.lang.String, java.lang.String>> list = ((java.util.List<java.util.Map<java.lang.String, java.lang.String>>) (extra.get("processes")));
                                        scHost.setProcesses(list);
                                    }
                                } catch (java.lang.Exception e) {
                                    org.apache.ambari.server.agent.HeartbeatProcessor.LOG.error(((((((("Could not access extra JSON for " + scHost.getServiceComponentName()) + " from ") + scHost.getHostName()) + ": ") + status.getExtra()) + " (") + e.getMessage()) + ")");
                                }
                            }
                            heartbeatMonitor.getAgentRequests().setExecutionDetailsRequest(hostname, componentName, status.getSendExecCmdDet());
                        } else {
                        }
                    } catch (org.apache.ambari.server.ServiceNotFoundException e) {
                        org.apache.ambari.server.agent.HeartbeatProcessor.LOG.warn(((("Received a live status update for a non-initialized" + (" service" + ", clusterId=")) + status.getClusterId()) + ", serviceName=") + status.getServiceName());
                        continue;
                    } catch (org.apache.ambari.server.ServiceComponentNotFoundException e) {
                        org.apache.ambari.server.agent.HeartbeatProcessor.LOG.warn(((((("Received a live status update for a non-initialized" + (" servicecomponent" + ", clusterId=")) + status.getClusterId()) + ", serviceName=") + status.getServiceName()) + ", componentName=") + status.getComponentName());
                        continue;
                    } catch (org.apache.ambari.server.ServiceComponentHostNotFoundException e) {
                        org.apache.ambari.server.agent.HeartbeatProcessor.LOG.warn(((((((("Received a live status update for a non-initialized" + (" service" + ", clusterId=")) + status.getClusterId()) + ", serviceName=") + status.getServiceName()) + ", componentName=") + status.getComponentName()) + ", hostname=") + hostname);
                        continue;
                    } catch (java.lang.RuntimeException e) {
                        org.apache.ambari.server.agent.HeartbeatProcessor.LOG.warn(((((((((("Received a live status with invalid payload" + (" service" + ", clusterId=")) + status.getClusterId()) + ", serviceName=") + status.getServiceName()) + ", componentName=") + status.getComponentName()) + ", hostname=") + hostname) + ", error=") + e.getMessage());
                        continue;
                    }
                }
            }
        }
        org.apache.ambari.server.state.Host host = clusterFsm.getHost(hostname);
        long now = java.lang.System.currentTimeMillis();
        if ((componentStatuses.size() > 0) && host.getState().equals(org.apache.ambari.server.state.HostState.WAITING_FOR_HOST_STATUS_UPDATES)) {
            try {
                org.apache.ambari.server.agent.HeartbeatProcessor.LOG.debug("Got component status updates for host {}", hostname);
                host.handleEvent(new org.apache.ambari.server.state.host.HostStatusUpdatesReceivedEvent(hostname, now));
            } catch (org.apache.ambari.server.state.fsm.InvalidStateTransitionException e) {
                org.apache.ambari.server.agent.HeartbeatProcessor.LOG.warn("Failed to notify the host about component status updates for host {}", hostname, e);
            }
        }
    }

    private static class WriteKeytabsStructuredOut {
        @com.google.gson.annotations.SerializedName("keytabs")
        private java.util.Map<java.lang.String, java.lang.String> keytabs;

        @com.google.gson.annotations.SerializedName("removedKeytabs")
        private java.util.Map<java.lang.String, java.lang.String> removedKeytabs;

        public java.util.Map<java.lang.String, java.lang.String> getKeytabs() {
            return keytabs;
        }

        public void setKeytabs(java.util.Map<java.lang.String, java.lang.String> keytabs) {
            this.keytabs = keytabs;
        }

        public java.util.Map<java.lang.String, java.lang.String> getRemovedKeytabs() {
            return removedKeytabs;
        }

        public void setRemovedKeytabs(java.util.Map<java.lang.String, java.lang.String> removedKeytabs) {
            this.removedKeytabs = removedKeytabs;
        }
    }

    private static class ListKeytabsStructuredOut {
        @com.google.gson.annotations.SerializedName("missing_keytabs")
        private final java.util.List<org.apache.ambari.server.agent.HeartbeatProcessor.MissingKeytab> missingKeytabs;

        public ListKeytabsStructuredOut(java.util.List<org.apache.ambari.server.agent.HeartbeatProcessor.MissingKeytab> missingKeytabs) {
            this.missingKeytabs = missingKeytabs;
        }
    }

    private static class MissingKeytab {
        @com.google.gson.annotations.SerializedName("principal")
        private final java.lang.String principal;

        @com.google.gson.annotations.SerializedName("keytab_file_path")
        private final java.lang.String keytabFilePath;

        public MissingKeytab(java.lang.String principal, java.lang.String keytabFilePath) {
            this.principal = principal;
            this.keytabFilePath = keytabFilePath;
        }
    }

    private static class ComponentVersionStructuredOut {
        @com.google.gson.annotations.SerializedName("version")
        private java.lang.String version;

        @com.google.gson.annotations.SerializedName("upgrade_type")
        private org.apache.ambari.spi.upgrade.UpgradeType upgradeType = null;

        @com.google.gson.annotations.SerializedName("direction")
        private org.apache.ambari.server.stack.upgrade.Direction upgradeDirection = null;

        @com.google.gson.annotations.SerializedName(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.REPO_VERSION_ID)
        private java.lang.Long repositoryVersionId;
    }
}