package org.apache.ambari.server.agent;
import static org.apache.ambari.server.agent.ExecutionCommand.KeyNames.COMMAND_TIMEOUT;
import static org.apache.ambari.server.agent.ExecutionCommand.KeyNames.HOOKS_FOLDER;
import static org.apache.ambari.server.agent.ExecutionCommand.KeyNames.JDK_LOCATION;
import static org.apache.ambari.server.agent.ExecutionCommand.KeyNames.SCRIPT;
import static org.apache.ambari.server.agent.ExecutionCommand.KeyNames.SCRIPT_TYPE;
import static org.apache.ambari.server.agent.ExecutionCommand.KeyNames.SERVICE_PACKAGE_FOLDER;
import static org.apache.ambari.server.agent.ExecutionCommand.KeyNames.STACK_NAME;
import static org.apache.ambari.server.agent.ExecutionCommand.KeyNames.STACK_VERSION;
public class HeartbeatMonitor implements java.lang.Runnable {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.agent.HeartbeatMonitor.class);

    private org.apache.ambari.server.state.Clusters clusters;

    private org.apache.ambari.server.actionmanager.ActionManager actionManager;

    private final int threadWakeupInterval;

    private volatile boolean shouldRun = true;

    private java.lang.Thread monitorThread = null;

    private final org.apache.ambari.server.state.ConfigHelper configHelper;

    private final org.apache.ambari.server.api.services.AmbariMetaInfo ambariMetaInfo;

    private final org.apache.ambari.server.controller.AmbariManagementController ambariManagementController;

    private final org.apache.ambari.server.configuration.Configuration configuration;

    private final org.apache.ambari.server.agent.AgentRequests agentRequests;

    private final org.apache.ambari.server.events.publishers.AmbariEventPublisher ambariEventPublisher;

    public HeartbeatMonitor(org.apache.ambari.server.state.Clusters clusters, org.apache.ambari.server.actionmanager.ActionManager am, int threadWakeupInterval, com.google.inject.Injector injector) {
        this.clusters = clusters;
        actionManager = am;
        this.threadWakeupInterval = threadWakeupInterval;
        configHelper = injector.getInstance(org.apache.ambari.server.state.ConfigHelper.class);
        ambariMetaInfo = injector.getInstance(org.apache.ambari.server.api.services.AmbariMetaInfo.class);
        ambariManagementController = injector.getInstance(org.apache.ambari.server.controller.AmbariManagementController.class);
        configuration = injector.getInstance(org.apache.ambari.server.configuration.Configuration.class);
        agentRequests = new org.apache.ambari.server.agent.AgentRequests();
        ambariEventPublisher = injector.getInstance(org.apache.ambari.server.events.publishers.AmbariEventPublisher.class);
        ambariEventPublisher.register(this);
    }

    public void shutdown() {
        shouldRun = false;
    }

    public void start() {
        monitorThread = new java.lang.Thread(this, "ambari-hearbeat-monitor");
        monitorThread.start();
    }

    void join(long millis) throws java.lang.InterruptedException {
        monitorThread.join(millis);
    }

    public boolean isAlive() {
        return monitorThread.isAlive();
    }

    public org.apache.ambari.server.agent.AgentRequests getAgentRequests() {
        return agentRequests;
    }

    @java.lang.Override
    public void run() {
        while (shouldRun) {
            try {
                doWork();
                org.apache.ambari.server.agent.HeartbeatMonitor.LOG.trace("Putting monitor to sleep for {} milliseconds", threadWakeupInterval);
                java.lang.Thread.sleep(threadWakeupInterval);
            } catch (java.lang.InterruptedException ex) {
                org.apache.ambari.server.agent.HeartbeatMonitor.LOG.warn("Scheduler thread is interrupted going to stop", ex);
                shouldRun = false;
            } catch (java.lang.Exception ex) {
                org.apache.ambari.server.agent.HeartbeatMonitor.LOG.warn("Exception received", ex);
            } catch (java.lang.Throwable t) {
                org.apache.ambari.server.agent.HeartbeatMonitor.LOG.warn("ERROR", t);
            }
        } 
    }

    private void doWork() throws org.apache.ambari.server.state.fsm.InvalidStateTransitionException, org.apache.ambari.server.AmbariException {
        java.util.List<org.apache.ambari.server.state.Host> allHosts = clusters.getHosts();
        long now = java.lang.System.currentTimeMillis();
        for (org.apache.ambari.server.state.Host hostObj : allHosts) {
            if (hostObj.getState() == org.apache.ambari.server.state.HostState.HEARTBEAT_LOST) {
                continue;
            }
            java.lang.Long hostId = hostObj.getHostId();
            org.apache.ambari.server.state.HostState hostState = hostObj.getState();
            long lastHeartbeat = 0;
            try {
                lastHeartbeat = clusters.getHostById(hostId).getLastHeartbeatTime();
            } catch (org.apache.ambari.server.AmbariException e) {
                org.apache.ambari.server.agent.HeartbeatMonitor.LOG.warn("Exception in getting host object; Is it fatal?", e);
            }
            if ((lastHeartbeat + (2 * threadWakeupInterval)) < now) {
                handleHeartbeatLost(hostId);
            }
            if (hostState == org.apache.ambari.server.state.HostState.WAITING_FOR_HOST_STATUS_UPDATES) {
                long timeSpentInState = hostObj.getTimeInState();
                if ((timeSpentInState + (5 * threadWakeupInterval)) < now) {
                    org.apache.ambari.server.agent.HeartbeatMonitor.LOG.warn("timeSpentInState + 5*threadWakeupInterval < now, Go back to init");
                    hostObj.setState(org.apache.ambari.server.state.HostState.INIT);
                }
            }
        }
    }

    public java.util.List<org.apache.ambari.server.agent.StatusCommand> generateStatusCommands(java.lang.String hostname) throws org.apache.ambari.server.AmbariException {
        java.util.List<org.apache.ambari.server.agent.StatusCommand> cmds = new java.util.ArrayList<>();
        for (org.apache.ambari.server.state.Cluster cl : clusters.getClustersForHost(hostname)) {
            java.util.Map<java.lang.String, org.apache.ambari.server.state.DesiredConfig> desiredConfigs = cl.getDesiredConfigs();
            for (org.apache.ambari.server.state.ServiceComponentHost sch : cl.getServiceComponentHosts(hostname)) {
                switch (sch.getState()) {
                    case INIT :
                    case INSTALLING :
                    case STARTING :
                    case STOPPING :
                        continue;
                    default :
                        org.apache.ambari.server.agent.StatusCommand statusCmd = createStatusCommand(hostname, cl, sch, desiredConfigs);
                        cmds.add(statusCmd);
                }
            }
        }
        return cmds;
    }

    private org.apache.ambari.server.agent.StatusCommand createStatusCommand(java.lang.String hostname, org.apache.ambari.server.state.Cluster cluster, org.apache.ambari.server.state.ServiceComponentHost sch, java.util.Map<java.lang.String, org.apache.ambari.server.state.DesiredConfig> desiredConfigs) throws org.apache.ambari.server.AmbariException {
        java.lang.String serviceName = sch.getServiceName();
        java.lang.String componentName = sch.getServiceComponentName();
        org.apache.ambari.server.state.StackId stackId = sch.getDesiredStackId();
        org.apache.ambari.server.state.ServiceInfo serviceInfo = ambariMetaInfo.getService(stackId.getStackName(), stackId.getStackVersion(), serviceName);
        org.apache.ambari.server.state.ComponentInfo componentInfo = ambariMetaInfo.getComponent(stackId.getStackName(), stackId.getStackVersion(), serviceName, componentName);
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configurations = new java.util.TreeMap<>();
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>> configurationAttributes = new java.util.TreeMap<>();
        java.util.Collection<org.apache.ambari.server.state.Config> clusterConfigs = cluster.getAllConfigs();
        java.util.Set<java.lang.String> desiredConfigTypes = desiredConfigs.keySet();
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> allConfigTags = configHelper.getEffectiveDesiredTags(cluster, hostname);
        for (org.apache.ambari.server.state.Config clusterConfig : clusterConfigs) {
            java.lang.String configType = clusterConfig.getType();
            if ((!configType.endsWith("-env")) || (!desiredConfigTypes.contains(configType))) {
                continue;
            }
            java.util.Map<java.lang.String, java.lang.String> props = new java.util.HashMap<>(clusterConfig.getProperties());
            java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configTags = new java.util.HashMap<>();
            for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> entry : allConfigTags.entrySet()) {
                if (entry.getKey().equals(clusterConfig.getType())) {
                    configTags.put(clusterConfig.getType(), entry.getValue());
                }
            }
            java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = configHelper.getEffectiveConfigProperties(cluster, configTags);
            if (!properties.isEmpty()) {
                for (java.util.Map<java.lang.String, java.lang.String> propertyMap : properties.values()) {
                    props.putAll(propertyMap);
                }
            }
            configurations.put(clusterConfig.getType(), props);
            java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> attrs = new java.util.TreeMap<>();
            configHelper.cloneAttributesMap(clusterConfig.getPropertiesAttributes(), attrs);
            java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>> attributes = configHelper.getEffectiveConfigAttributes(cluster, configTags);
            for (java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> attributesMap : attributes.values()) {
                configHelper.cloneAttributesMap(attributesMap, attrs);
            }
            configurationAttributes.put(clusterConfig.getType(), attrs);
        }
        org.apache.ambari.server.agent.StatusCommand statusCmd = new org.apache.ambari.server.agent.StatusCommand();
        statusCmd.setClusterName(cluster.getClusterName());
        statusCmd.setServiceName(serviceName);
        statusCmd.setComponentName(componentName);
        statusCmd.setConfigurations(configurations);
        statusCmd.setConfigurationAttributes(configurationAttributes);
        statusCmd.setHostname(hostname);
        statusCmd.setDesiredState(sch.getDesiredState());
        statusCmd.setHasStaleConfigs(configHelper.isStaleConfigs(sch, desiredConfigs));
        if (getAgentRequests().shouldSendExecutionDetails(hostname, componentName)) {
            org.apache.ambari.server.agent.HeartbeatMonitor.LOG.info(((componentName + " is at ") + sch.getState()) + " adding more payload per agent ask");
            statusCmd.setPayloadLevel(org.apache.ambari.server.agent.StatusCommand.StatusCommandPayload.EXECUTION_COMMAND);
        }
        java.util.Map<java.lang.String, java.lang.String> commandParams = statusCmd.getCommandParams();
        java.lang.String commandTimeout = configuration.getDefaultAgentTaskTimeout(false);
        org.apache.ambari.server.state.CommandScriptDefinition script = componentInfo.getCommandScript();
        if (serviceInfo.getSchemaVersion().equals(org.apache.ambari.server.api.services.AmbariMetaInfo.SCHEMA_VERSION_2)) {
            if (script != null) {
                commandParams.put(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.SCRIPT, script.getScript());
                commandParams.put(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.SCRIPT_TYPE, script.getScriptType().toString());
                if (script.getTimeout() > 0) {
                    commandTimeout = java.lang.String.valueOf(script.getTimeout());
                }
            } else {
                java.lang.String message = java.lang.String.format("Component %s of service %s has not " + "command script defined", componentName, serviceName);
                throw new org.apache.ambari.server.AmbariException(message);
            }
        }
        commandParams.put(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.COMMAND_TIMEOUT, commandTimeout);
        commandParams.put(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.SERVICE_PACKAGE_FOLDER, serviceInfo.getServicePackageFolder());
        commandParams.put(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.HOOKS_FOLDER, configuration.getProperty(org.apache.ambari.server.configuration.Configuration.HOOKS_FOLDER));
        java.util.Map<java.lang.String, java.lang.String> hostLevelParams = statusCmd.getHostLevelParams();
        hostLevelParams.put(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.JDK_LOCATION, ambariManagementController.getJdkResourceUrl());
        hostLevelParams.put(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.STACK_NAME, stackId.getStackName());
        hostLevelParams.put(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.STACK_VERSION, stackId.getStackVersion());
        if (statusCmd.getPayloadLevel() == org.apache.ambari.server.agent.StatusCommand.StatusCommandPayload.EXECUTION_COMMAND) {
            org.apache.ambari.server.agent.ExecutionCommand ec = ambariManagementController.getExecutionCommand(cluster, sch, org.apache.ambari.server.RoleCommand.START);
            statusCmd.setExecutionCommand(ec);
            org.apache.ambari.server.agent.HeartbeatMonitor.LOG.debug("{} has more payload for execution command", componentName);
        }
        return statusCmd;
    }

    private void handleHeartbeatLost(java.lang.Long hostId) throws org.apache.ambari.server.AmbariException, org.apache.ambari.server.state.fsm.InvalidStateTransitionException {
        org.apache.ambari.server.state.Host hostObj = clusters.getHostById(hostId);
        java.lang.String host = hostObj.getHostName();
        org.apache.ambari.server.agent.HeartbeatMonitor.LOG.warn("Heartbeat lost from host " + host);
        hostObj.handleEvent(new org.apache.ambari.server.state.host.HostHeartbeatLostEvent(host));
        for (org.apache.ambari.server.state.Cluster cluster : clusters.getClustersForHost(hostObj.getHostName())) {
            for (org.apache.ambari.server.state.ServiceComponentHost sch : cluster.getServiceComponentHosts(hostObj.getHostName())) {
                org.apache.ambari.server.state.Service s = cluster.getService(sch.getServiceName());
                org.apache.ambari.server.state.ServiceComponent sc = s.getServiceComponent(sch.getServiceComponentName());
                if ((((((!sc.isClientComponent()) && (!sch.getState().equals(org.apache.ambari.server.state.State.INIT))) && (!sch.getState().equals(org.apache.ambari.server.state.State.INSTALLING))) && (!sch.getState().equals(org.apache.ambari.server.state.State.INSTALL_FAILED))) && (!sch.getState().equals(org.apache.ambari.server.state.State.UNINSTALLED))) && (!sch.getState().equals(org.apache.ambari.server.state.State.DISABLED))) {
                    org.apache.ambari.server.agent.HeartbeatMonitor.LOG.warn((("Setting component state to UNKNOWN for component " + sc.getName()) + " on ") + host);
                    org.apache.ambari.server.state.State oldState = sch.getState();
                    sch.setState(org.apache.ambari.server.state.State.UNKNOWN);
                }
            }
        }
        actionManager.handleLostHost(host);
    }

    @com.google.common.eventbus.Subscribe
    public void onMessageNotDelivered(org.apache.ambari.server.events.MessageNotDelivered messageNotDelivered) {
        try {
            org.apache.ambari.server.state.Host hostObj = clusters.getHostById(messageNotDelivered.getHostId());
            if (hostObj.getState() == org.apache.ambari.server.state.HostState.HEARTBEAT_LOST) {
                return;
            }
            handleHeartbeatLost(messageNotDelivered.getHostId());
        } catch (java.lang.Exception e) {
            org.apache.ambari.server.agent.HeartbeatMonitor.LOG.error("Error during host to heartbeat lost moving", e);
        }
    }
}