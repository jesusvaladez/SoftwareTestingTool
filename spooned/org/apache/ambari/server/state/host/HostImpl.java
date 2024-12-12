package org.apache.ambari.server.state.host;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.persist.Transactional;
import org.apache.commons.lang.StringUtils;
public class HostImpl implements org.apache.ambari.server.state.Host {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.state.host.HostImpl.class);

    private static final java.lang.String HARDWAREISA = "hardware_isa";

    private static final java.lang.String HARDWAREMODEL = "hardware_model";

    private static final java.lang.String INTERFACES = "interfaces";

    private static final java.lang.String KERNEL = "kernel";

    private static final java.lang.String KERNELMAJOREVERSON = "kernel_majorversion";

    private static final java.lang.String KERNELRELEASE = "kernel_release";

    private static final java.lang.String KERNELVERSION = "kernel_version";

    private static final java.lang.String MACADDRESS = "mac_address";

    private static final java.lang.String NETMASK = "netmask";

    private static final java.lang.String OSFAMILY = "os_family";

    private static final java.lang.String PHYSICALPROCESSORCOUNT = "physicalprocessors_count";

    private static final java.lang.String PROCESSORCOUNT = "processors_count";

    private static final java.lang.String SELINUXENABLED = "selinux_enabled";

    private static final java.lang.String SWAPSIZE = "swap_size";

    private static final java.lang.String SWAPFREE = "swap_free";

    private static final java.lang.String TIMEZONE = "timezone";

    private static final java.lang.String OS_RELEASE_VERSION = "os_release_version";

    @com.google.inject.Inject
    private final com.google.gson.Gson gson;

    private static final java.lang.reflect.Type hostAttributesType = new com.google.gson.reflect.TypeToken<java.util.Map<java.lang.String, java.lang.String>>() {}.getType();

    private static final java.lang.reflect.Type maintMapType = new com.google.gson.reflect.TypeToken<java.util.Map<java.lang.Long, org.apache.ambari.server.state.MaintenanceState>>() {}.getType();

    java.util.concurrent.locks.ReadWriteLock rwLock;

    private final java.util.concurrent.locks.Lock writeLock;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.HostDAO hostDAO;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.HostStateDAO hostStateDAO;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.HostVersionDAO hostVersionDAO;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.ClusterDAO clusterDAO;

    @com.google.inject.Inject
    private org.apache.ambari.server.state.Clusters clusters;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.HostConfigMappingDAO hostConfigMappingDAO;

    @com.google.inject.Inject
    private org.apache.ambari.server.api.services.AmbariMetaInfo ambariMetaInfo;

    @com.google.inject.Inject
    private org.apache.ambari.server.events.publishers.AmbariEventPublisher ambariEventPublisher;

    @com.google.inject.Inject
    private org.apache.ambari.server.controller.MaintenanceStateHelper maintenanceStateHelper;

    private final long hostId;

    private final java.lang.String hostName;

    private long lastHeartbeatTime = 0L;

    private long lastAgentStartTime = 0L;

    private org.apache.ambari.server.agent.AgentEnv lastAgentEnv = null;

    private java.util.List<org.apache.ambari.server.agent.DiskInfo> disksInfo = new java.util.concurrent.CopyOnWriteArrayList<>();

    private org.apache.ambari.server.agent.RecoveryReport recoveryReport = new org.apache.ambari.server.agent.RecoveryReport();

    private java.lang.Integer currentPingPort = null;

    private final org.apache.ambari.server.state.fsm.StateMachine<org.apache.ambari.server.state.HostState, org.apache.ambari.server.state.HostEventType, org.apache.ambari.server.state.HostEvent> stateMachine;

    private final java.util.concurrent.ConcurrentMap<java.lang.Long, org.apache.ambari.server.state.MaintenanceState> maintMap;

    private java.lang.String status = org.apache.ambari.server.state.HostHealthStatus.HealthStatus.UNKNOWN.name();

    private java.lang.String prefix;

    @com.google.inject.Inject
    private org.apache.ambari.server.events.publishers.AmbariEventPublisher eventPublisher;

    @com.google.inject.Inject
    private org.apache.ambari.server.topology.TopologyManager topologyManager;

    private static final org.apache.ambari.server.state.fsm.StateMachineFactory<org.apache.ambari.server.state.host.HostImpl, org.apache.ambari.server.state.HostState, org.apache.ambari.server.state.HostEventType, org.apache.ambari.server.state.HostEvent> stateMachineFactory = new org.apache.ambari.server.state.fsm.StateMachineFactory<org.apache.ambari.server.state.host.HostImpl, org.apache.ambari.server.state.HostState, org.apache.ambari.server.state.HostEventType, org.apache.ambari.server.state.HostEvent>(org.apache.ambari.server.state.HostState.INIT).addTransition(org.apache.ambari.server.state.HostState.INIT, org.apache.ambari.server.state.HostState.WAITING_FOR_HOST_STATUS_UPDATES, org.apache.ambari.server.state.HostEventType.HOST_REGISTRATION_REQUEST, new org.apache.ambari.server.state.host.HostImpl.HostRegistrationReceived()).addTransition(org.apache.ambari.server.state.HostState.INIT, org.apache.ambari.server.state.HostState.HEARTBEAT_LOST, org.apache.ambari.server.state.HostEventType.HOST_HEARTBEAT_LOST, new org.apache.ambari.server.state.host.HostImpl.HostHeartbeatLostTransition()).addTransition(org.apache.ambari.server.state.HostState.WAITING_FOR_HOST_STATUS_UPDATES, org.apache.ambari.server.state.HostState.HEALTHY, org.apache.ambari.server.state.HostEventType.HOST_STATUS_UPDATES_RECEIVED, new org.apache.ambari.server.state.host.HostImpl.HostStatusUpdatesReceivedTransition()).addTransition(org.apache.ambari.server.state.HostState.WAITING_FOR_HOST_STATUS_UPDATES, org.apache.ambari.server.state.HostState.WAITING_FOR_HOST_STATUS_UPDATES, org.apache.ambari.server.state.HostEventType.HOST_HEARTBEAT_HEALTHY).addTransition(org.apache.ambari.server.state.HostState.WAITING_FOR_HOST_STATUS_UPDATES, org.apache.ambari.server.state.HostState.WAITING_FOR_HOST_STATUS_UPDATES, org.apache.ambari.server.state.HostEventType.HOST_HEARTBEAT_UNHEALTHY, new org.apache.ambari.server.state.host.HostImpl.HostBecameUnhealthyTransition()).addTransition(org.apache.ambari.server.state.HostState.WAITING_FOR_HOST_STATUS_UPDATES, org.apache.ambari.server.state.HostState.HEARTBEAT_LOST, org.apache.ambari.server.state.HostEventType.HOST_HEARTBEAT_LOST, new org.apache.ambari.server.state.host.HostImpl.HostHeartbeatLostTransition()).addTransition(org.apache.ambari.server.state.HostState.HEALTHY, org.apache.ambari.server.state.HostState.HEALTHY, org.apache.ambari.server.state.HostEventType.HOST_HEARTBEAT_HEALTHY, new org.apache.ambari.server.state.host.HostImpl.HostHeartbeatReceivedTransition()).addTransition(org.apache.ambari.server.state.HostState.HEALTHY, org.apache.ambari.server.state.HostState.HEARTBEAT_LOST, org.apache.ambari.server.state.HostEventType.HOST_HEARTBEAT_LOST, new org.apache.ambari.server.state.host.HostImpl.HostHeartbeatLostTransition()).addTransition(org.apache.ambari.server.state.HostState.HEALTHY, org.apache.ambari.server.state.HostState.UNHEALTHY, org.apache.ambari.server.state.HostEventType.HOST_HEARTBEAT_UNHEALTHY, new org.apache.ambari.server.state.host.HostImpl.HostBecameUnhealthyTransition()).addTransition(org.apache.ambari.server.state.HostState.HEALTHY, org.apache.ambari.server.state.HostState.WAITING_FOR_HOST_STATUS_UPDATES, org.apache.ambari.server.state.HostEventType.HOST_REGISTRATION_REQUEST, new org.apache.ambari.server.state.host.HostImpl.HostRegistrationReceived()).addTransition(org.apache.ambari.server.state.HostState.UNHEALTHY, org.apache.ambari.server.state.HostState.HEALTHY, org.apache.ambari.server.state.HostEventType.HOST_HEARTBEAT_HEALTHY, new org.apache.ambari.server.state.host.HostImpl.HostBecameHealthyTransition()).addTransition(org.apache.ambari.server.state.HostState.UNHEALTHY, org.apache.ambari.server.state.HostState.UNHEALTHY, org.apache.ambari.server.state.HostEventType.HOST_HEARTBEAT_UNHEALTHY, new org.apache.ambari.server.state.host.HostImpl.HostHeartbeatReceivedTransition()).addTransition(org.apache.ambari.server.state.HostState.UNHEALTHY, org.apache.ambari.server.state.HostState.HEARTBEAT_LOST, org.apache.ambari.server.state.HostEventType.HOST_HEARTBEAT_LOST, new org.apache.ambari.server.state.host.HostImpl.HostHeartbeatLostTransition()).addTransition(org.apache.ambari.server.state.HostState.UNHEALTHY, org.apache.ambari.server.state.HostState.WAITING_FOR_HOST_STATUS_UPDATES, org.apache.ambari.server.state.HostEventType.HOST_REGISTRATION_REQUEST, new org.apache.ambari.server.state.host.HostImpl.HostRegistrationReceived()).addTransition(org.apache.ambari.server.state.HostState.HEARTBEAT_LOST, org.apache.ambari.server.state.HostState.HEARTBEAT_LOST, org.apache.ambari.server.state.HostEventType.HOST_HEARTBEAT_LOST).addTransition(org.apache.ambari.server.state.HostState.HEARTBEAT_LOST, org.apache.ambari.server.state.HostState.WAITING_FOR_HOST_STATUS_UPDATES, org.apache.ambari.server.state.HostEventType.HOST_REGISTRATION_REQUEST, new org.apache.ambari.server.state.host.HostImpl.HostRegistrationReceived()).installTopology();

    @com.google.inject.Inject
    public HostImpl(@com.google.inject.assistedinject.Assisted
    org.apache.ambari.server.orm.entities.HostEntity hostEntity, com.google.gson.Gson gson, org.apache.ambari.server.orm.dao.HostDAO hostDAO, org.apache.ambari.server.orm.dao.HostStateDAO hostStateDAO) {
        this.gson = gson;
        this.hostDAO = hostDAO;
        this.hostStateDAO = hostStateDAO;
        stateMachine = org.apache.ambari.server.state.host.HostImpl.stateMachineFactory.make(this);
        rwLock = new java.util.concurrent.locks.ReentrantReadWriteLock();
        writeLock = rwLock.writeLock();
        org.apache.ambari.server.orm.entities.HostStateEntity hostStateEntity = hostEntity.getHostStateEntity();
        if (hostStateEntity == null) {
            hostStateEntity = new org.apache.ambari.server.orm.entities.HostStateEntity();
            hostStateEntity.setHostEntity(hostEntity);
            hostEntity.setHostStateEntity(hostStateEntity);
            hostStateEntity.setHealthStatus(gson.toJson(new org.apache.ambari.server.state.HostHealthStatus(org.apache.ambari.server.state.HostHealthStatus.HealthStatus.UNKNOWN, "")));
        } else {
            stateMachine.setCurrentState(hostStateEntity.getCurrentState());
        }
        if (null == hostEntity.getHostId()) {
            persistEntities(hostEntity);
            for (org.apache.ambari.server.orm.entities.ClusterEntity clusterEntity : hostEntity.getClusterEntities()) {
                try {
                    clusters.getClusterById(clusterEntity.getClusterId()).refresh();
                } catch (org.apache.ambari.server.AmbariException e) {
                    org.apache.ambari.server.state.host.HostImpl.LOG.error("Error while looking up the cluster", e);
                    throw new java.lang.RuntimeException(("Cluster '" + clusterEntity.getClusterId()) + "' was removed", e);
                }
            }
        }
        hostId = hostEntity.getHostId();
        hostName = hostEntity.getHostName();
        maintMap = ensureMaintMap(hostEntity.getHostStateEntity());
    }

    @java.lang.Override
    public int compareTo(java.lang.Object o) {
        if ((o != null) && (o instanceof org.apache.ambari.server.state.Host)) {
            return getHostName().compareTo(((org.apache.ambari.server.state.Host) (o)).getHostName());
        } else {
            return -1;
        }
    }

    static class HostRegistrationReceived implements org.apache.ambari.server.state.fsm.SingleArcTransition<org.apache.ambari.server.state.host.HostImpl, org.apache.ambari.server.state.HostEvent> {
        @java.lang.Override
        public void transition(org.apache.ambari.server.state.host.HostImpl host, org.apache.ambari.server.state.HostEvent event) {
            org.apache.ambari.server.state.host.HostRegistrationRequestEvent e = ((org.apache.ambari.server.state.host.HostRegistrationRequestEvent) (event));
            host.updateHost(e);
            java.lang.String agentVersion = null;
            if (e.agentVersion != null) {
                agentVersion = e.agentVersion.getVersion();
            }
            org.apache.ambari.server.state.host.HostImpl.LOG.info((((("Received host registration, host=" + e.hostInfo) + ", registrationTime=") + e.registrationTime) + ", agentVersion=") + agentVersion);
            host.clusters.updateHostMappings(host);
            boolean associatedWithCluster = false;
            try {
                associatedWithCluster = host.clusters.getClustersForHost(host.getPublicHostName()).size() > 0;
            } catch (org.apache.ambari.server.HostNotFoundException e1) {
                associatedWithCluster = false;
            } catch (org.apache.ambari.server.AmbariException e1) {
                org.apache.ambari.server.state.host.HostImpl.LOG.error("Unable to determine the clusters for host", e1);
            }
            host.topologyManager.onHostRegistered(host, associatedWithCluster);
            host.setHealthStatus(new org.apache.ambari.server.state.HostHealthStatus(org.apache.ambari.server.state.HostHealthStatus.HealthStatus.HEALTHY, host.getHealthStatus().getHealthReport()));
            host.updateHostTimestamps(e);
        }
    }

    static class HostStatusUpdatesReceivedTransition implements org.apache.ambari.server.state.fsm.SingleArcTransition<org.apache.ambari.server.state.host.HostImpl, org.apache.ambari.server.state.HostEvent> {
        @java.lang.Override
        public void transition(org.apache.ambari.server.state.host.HostImpl host, org.apache.ambari.server.state.HostEvent event) {
            org.apache.ambari.server.state.host.HostStatusUpdatesReceivedEvent e = ((org.apache.ambari.server.state.host.HostStatusUpdatesReceivedEvent) (event));
            org.apache.ambari.server.state.host.HostImpl.LOG.debug("Host transition to host status updates received state, host={}, heartbeatTime={}", e.getHostName(), e.getTimestamp());
            host.setHealthStatus(new org.apache.ambari.server.state.HostHealthStatus(org.apache.ambari.server.state.HostHealthStatus.HealthStatus.HEALTHY, host.getHealthStatus().getHealthReport()));
        }
    }

    static class HostHeartbeatReceivedTransition implements org.apache.ambari.server.state.fsm.SingleArcTransition<org.apache.ambari.server.state.host.HostImpl, org.apache.ambari.server.state.HostEvent> {
        @java.lang.Override
        public void transition(org.apache.ambari.server.state.host.HostImpl host, org.apache.ambari.server.state.HostEvent event) {
            long heartbeatTime = 0;
            switch (event.getType()) {
                case HOST_HEARTBEAT_HEALTHY :
                    org.apache.ambari.server.state.host.HostHealthyHeartbeatEvent hhevent = ((org.apache.ambari.server.state.host.HostHealthyHeartbeatEvent) (event));
                    heartbeatTime = hhevent.getHeartbeatTime();
                    if (null != hhevent.getAgentEnv()) {
                        host.setLastAgentEnv(hhevent.getAgentEnv());
                    }
                    if ((null != hhevent.getMounts()) && (!hhevent.getMounts().isEmpty())) {
                        host.setDisksInfo(hhevent.getMounts());
                    }
                    break;
                case HOST_HEARTBEAT_UNHEALTHY :
                    heartbeatTime = ((org.apache.ambari.server.state.host.HostUnhealthyHeartbeatEvent) (event)).getHeartbeatTime();
                    break;
                default :
                    break;
            }
            if (0 == heartbeatTime) {
                org.apache.ambari.server.state.host.HostImpl.LOG.error("heartbeatTime = 0 !!!");
            }
            host.setLastHeartbeatTime(heartbeatTime);
        }
    }

    static class HostBecameHealthyTransition implements org.apache.ambari.server.state.fsm.SingleArcTransition<org.apache.ambari.server.state.host.HostImpl, org.apache.ambari.server.state.HostEvent> {
        @java.lang.Override
        public void transition(org.apache.ambari.server.state.host.HostImpl host, org.apache.ambari.server.state.HostEvent event) {
            org.apache.ambari.server.state.host.HostHealthyHeartbeatEvent e = ((org.apache.ambari.server.state.host.HostHealthyHeartbeatEvent) (event));
            host.setLastHeartbeatTime(e.getHeartbeatTime());
            org.apache.ambari.server.state.host.HostImpl.LOG.debug("Host transitioned to a healthy state, host={}, heartbeatTime={}", e.getHostName(), e.getHeartbeatTime());
            host.setHealthStatus(new org.apache.ambari.server.state.HostHealthStatus(org.apache.ambari.server.state.HostHealthStatus.HealthStatus.HEALTHY, host.getHealthStatus().getHealthReport()));
        }
    }

    static class HostBecameUnhealthyTransition implements org.apache.ambari.server.state.fsm.SingleArcTransition<org.apache.ambari.server.state.host.HostImpl, org.apache.ambari.server.state.HostEvent> {
        @java.lang.Override
        public void transition(org.apache.ambari.server.state.host.HostImpl host, org.apache.ambari.server.state.HostEvent event) {
            org.apache.ambari.server.state.host.HostUnhealthyHeartbeatEvent e = ((org.apache.ambari.server.state.host.HostUnhealthyHeartbeatEvent) (event));
            host.setLastHeartbeatTime(e.getHeartbeatTime());
            org.apache.ambari.server.state.host.HostImpl.LOG.debug("Host transitioned to an unhealthy state, host={}, heartbeatTime={}, healthStatus={}", e.getHostName(), e.getHeartbeatTime(), e.getHealthStatus());
            host.setHealthStatus(e.getHealthStatus());
        }
    }

    static class HostHeartbeatLostTransition implements org.apache.ambari.server.state.fsm.SingleArcTransition<org.apache.ambari.server.state.host.HostImpl, org.apache.ambari.server.state.HostEvent> {
        @java.lang.Override
        public void transition(org.apache.ambari.server.state.host.HostImpl host, org.apache.ambari.server.state.HostEvent event) {
            org.apache.ambari.server.state.host.HostHeartbeatLostEvent e = ((org.apache.ambari.server.state.host.HostHeartbeatLostEvent) (event));
            org.apache.ambari.server.state.host.HostImpl.LOG.debug("Host transitioned to heartbeat lost state, host={}, lastHeartbeatTime={}", e.getHostName(), host.getLastHeartbeatTime());
            host.setHealthStatus(new org.apache.ambari.server.state.HostHealthStatus(org.apache.ambari.server.state.HostHealthStatus.HealthStatus.UNKNOWN, host.getHealthStatus().getHealthReport()));
            host.setLastAgentStartTime(0);
            host.topologyManager.onHostHeartBeatLost(host);
        }
    }

    @java.lang.Override
    @com.google.inject.persist.Transactional
    public void importHostInfo(org.apache.ambari.server.agent.HostInfo hostInfo) {
        if ((hostInfo.getIPAddress() != null) && (!hostInfo.getIPAddress().isEmpty())) {
            setIPv4(hostInfo.getIPAddress());
            setIPv6(hostInfo.getIPAddress());
        }
        setCpuCount(hostInfo.getProcessorCount());
        setPhCpuCount(hostInfo.getPhysicalProcessorCount());
        setTotalMemBytes(hostInfo.getMemoryTotal());
        setAvailableMemBytes(hostInfo.getFreeMemory());
        if ((hostInfo.getArchitecture() != null) && (!hostInfo.getArchitecture().isEmpty())) {
            setOsArch(hostInfo.getArchitecture());
        }
        if ((hostInfo.getOS() != null) && (!hostInfo.getOS().isEmpty())) {
            java.lang.String osType = hostInfo.getOS();
            if (hostInfo.getOSRelease() != null) {
                java.lang.String[] release = hostInfo.getOSRelease().split("\\.");
                if (release.length > 0) {
                    osType += release[0];
                }
            }
            setOsType(osType.toLowerCase());
        }
        if ((hostInfo.getMounts() != null) && (!hostInfo.getMounts().isEmpty())) {
            setDisksInfo(hostInfo.getMounts());
        }
        setAgentVersion(new org.apache.ambari.server.state.AgentVersion(hostInfo.getAgentUserId()));
        java.util.Map<java.lang.String, java.lang.String> attrs = new java.util.HashMap<>();
        if (hostInfo.getHardwareIsa() != null) {
            attrs.put(org.apache.ambari.server.state.host.HostImpl.HARDWAREISA, hostInfo.getHardwareIsa());
        }
        if (hostInfo.getHardwareModel() != null) {
            attrs.put(org.apache.ambari.server.state.host.HostImpl.HARDWAREMODEL, hostInfo.getHardwareModel());
        }
        if (hostInfo.getInterfaces() != null) {
            attrs.put(org.apache.ambari.server.state.host.HostImpl.INTERFACES, hostInfo.getInterfaces());
        }
        if (hostInfo.getKernel() != null) {
            attrs.put(org.apache.ambari.server.state.host.HostImpl.KERNEL, hostInfo.getKernel());
        }
        if (hostInfo.getKernelMajVersion() != null) {
            attrs.put(org.apache.ambari.server.state.host.HostImpl.KERNELMAJOREVERSON, hostInfo.getKernelMajVersion());
        }
        if (hostInfo.getKernelRelease() != null) {
            attrs.put(org.apache.ambari.server.state.host.HostImpl.KERNELRELEASE, hostInfo.getKernelRelease());
        }
        if (hostInfo.getKernelVersion() != null) {
            attrs.put(org.apache.ambari.server.state.host.HostImpl.KERNELVERSION, hostInfo.getKernelVersion());
        }
        if (hostInfo.getMacAddress() != null) {
            attrs.put(org.apache.ambari.server.state.host.HostImpl.MACADDRESS, hostInfo.getMacAddress());
        }
        if (hostInfo.getNetMask() != null) {
            attrs.put(org.apache.ambari.server.state.host.HostImpl.NETMASK, hostInfo.getNetMask());
        }
        if (hostInfo.getOSFamily() != null) {
            attrs.put(org.apache.ambari.server.state.host.HostImpl.OSFAMILY, hostInfo.getOSFamily());
        }
        if (hostInfo.getPhysicalProcessorCount() != 0) {
            attrs.put(org.apache.ambari.server.state.host.HostImpl.PHYSICALPROCESSORCOUNT, java.lang.Long.toString(hostInfo.getPhysicalProcessorCount()));
        }
        if (hostInfo.getProcessorCount() != 0) {
            attrs.put(org.apache.ambari.server.state.host.HostImpl.PROCESSORCOUNT, java.lang.Long.toString(hostInfo.getProcessorCount()));
        }
        if (java.lang.Boolean.toString(hostInfo.getSeLinux()) != null) {
            attrs.put(org.apache.ambari.server.state.host.HostImpl.SELINUXENABLED, java.lang.Boolean.toString(hostInfo.getSeLinux()));
        }
        if (hostInfo.getSwapSize() != null) {
            attrs.put(org.apache.ambari.server.state.host.HostImpl.SWAPSIZE, hostInfo.getSwapSize());
        }
        if (hostInfo.getSwapFree() != null) {
            attrs.put(org.apache.ambari.server.state.host.HostImpl.SWAPFREE, hostInfo.getSwapFree());
        }
        if (hostInfo.getTimeZone() != null) {
            attrs.put(org.apache.ambari.server.state.host.HostImpl.TIMEZONE, hostInfo.getTimeZone());
        }
        if (hostInfo.getOSRelease() != null) {
            attrs.put(org.apache.ambari.server.state.host.HostImpl.OS_RELEASE_VERSION, hostInfo.getOSRelease());
        }
        setHostAttributes(attrs);
    }

    @java.lang.Override
    public void setLastAgentEnv(org.apache.ambari.server.agent.AgentEnv env) {
        lastAgentEnv = env;
    }

    @java.lang.Override
    public org.apache.ambari.server.agent.AgentEnv getLastAgentEnv() {
        return lastAgentEnv;
    }

    @java.lang.Override
    public org.apache.ambari.server.state.HostState getState() {
        return stateMachine.getCurrentState();
    }

    @java.lang.Override
    public void setState(org.apache.ambari.server.state.HostState state) {
        stateMachine.setCurrentState(state);
        org.apache.ambari.server.orm.entities.HostStateEntity hostStateEntity = getHostStateEntity();
        ambariEventPublisher.publish(new org.apache.ambari.server.events.HostStateUpdateEvent(getHostName(), state));
        if (hostStateEntity != null) {
            hostStateEntity.setCurrentState(state);
            hostStateEntity.setTimeInState(java.lang.System.currentTimeMillis());
            hostStateDAO.merge(hostStateEntity);
        }
    }

    @java.lang.Override
    public void setStateMachineState(org.apache.ambari.server.state.HostState state) {
        stateMachine.setCurrentState(state);
        ambariEventPublisher.publish(new org.apache.ambari.server.events.HostStateUpdateEvent(getHostName(), state));
    }

    @java.lang.Override
    public void handleEvent(org.apache.ambari.server.state.HostEvent event) throws org.apache.ambari.server.state.fsm.InvalidStateTransitionException {
        if (org.apache.ambari.server.state.host.HostImpl.LOG.isDebugEnabled()) {
            org.apache.ambari.server.state.host.HostImpl.LOG.debug("Handling Host event, eventType={}, event={}", event.getType().name(), event);
        }
        org.apache.ambari.server.state.HostState oldState = getState();
        try {
            writeLock.lock();
            try {
                stateMachine.doTransition(event.getType(), event);
            } catch (org.apache.ambari.server.state.fsm.InvalidStateTransitionException e) {
                org.apache.ambari.server.state.host.HostImpl.LOG.error(((((((("Can't handle Host event at current state" + ", host=") + getHostName()) + ", currentState=") + oldState) + ", eventType=") + event.getType()) + ", event=") + event);
                throw e;
            }
        } finally {
            writeLock.unlock();
        }
        if (oldState != getState()) {
            ambariEventPublisher.publish(new org.apache.ambari.server.events.HostStateUpdateEvent(getHostName(), getState()));
            if (org.apache.ambari.server.state.host.HostImpl.LOG.isDebugEnabled()) {
                org.apache.ambari.server.state.host.HostImpl.LOG.debug("Host transitioned to a new state, host={}, oldState={}, currentState={}, eventType={}, event={}", getHostName(), oldState, getState(), event.getType().name(), event);
            }
        }
    }

    @java.lang.Override
    public java.lang.String getHostName() {
        return hostName;
    }

    @java.lang.Override
    public java.lang.Long getHostId() {
        return hostId;
    }

    @java.lang.Override
    public java.lang.Integer getCurrentPingPort() {
        return currentPingPort;
    }

    @java.lang.Override
    public void setCurrentPingPort(java.lang.Integer currentPingPort) {
        this.currentPingPort = currentPingPort;
    }

    @java.lang.Override
    public void setPublicHostName(java.lang.String hostName) {
        org.apache.ambari.server.orm.entities.HostEntity hostEntity = getHostEntity();
        hostEntity.setPublicHostName(hostName);
        hostDAO.merge(hostEntity);
    }

    @java.lang.Override
    public java.lang.String getPublicHostName() {
        return getHostEntity().getPublicHostName();
    }

    @java.lang.Override
    public java.lang.String getIPv4() {
        return getHostEntity().getIpv4();
    }

    @java.lang.Override
    public void setIPv4(java.lang.String ip) {
        org.apache.ambari.server.orm.entities.HostEntity hostEntity = getHostEntity();
        hostEntity.setIpv4(ip);
        hostDAO.merge(hostEntity);
    }

    @java.lang.Override
    public java.lang.String getIPv6() {
        return getHostEntity().getIpv6();
    }

    @java.lang.Override
    public void setIPv6(java.lang.String ip) {
        org.apache.ambari.server.orm.entities.HostEntity hostEntity = getHostEntity();
        hostEntity.setIpv6(ip);
        hostDAO.merge(hostEntity);
    }

    @java.lang.Override
    public int getCpuCount() {
        return getHostEntity().getCpuCount();
    }

    @java.lang.Override
    public void setCpuCount(int cpuCount) {
        org.apache.ambari.server.orm.entities.HostEntity hostEntity = getHostEntity();
        hostEntity.setCpuCount(cpuCount);
        hostDAO.merge(hostEntity);
    }

    @java.lang.Override
    public int getPhCpuCount() {
        return getHostEntity().getPhCpuCount();
    }

    @java.lang.Override
    public void setPhCpuCount(int phCpuCount) {
        org.apache.ambari.server.orm.entities.HostEntity hostEntity = getHostEntity();
        hostEntity.setPhCpuCount(phCpuCount);
        hostDAO.merge(hostEntity);
    }

    @java.lang.Override
    public long getTotalMemBytes() {
        return getHostEntity().getTotalMem();
    }

    @java.lang.Override
    public void setTotalMemBytes(long totalMemBytes) {
        org.apache.ambari.server.orm.entities.HostEntity hostEntity = getHostEntity();
        hostEntity.setTotalMem(totalMemBytes);
        hostDAO.merge(hostEntity);
    }

    @java.lang.Override
    public long getAvailableMemBytes() {
        org.apache.ambari.server.orm.entities.HostStateEntity hostStateEntity = getHostStateEntity();
        return hostStateEntity != null ? hostStateEntity.getAvailableMem() : 0;
    }

    @java.lang.Override
    public void setAvailableMemBytes(long availableMemBytes) {
        org.apache.ambari.server.orm.entities.HostStateEntity hostStateEntity = getHostStateEntity();
        if (hostStateEntity != null) {
            hostStateEntity.setAvailableMem(availableMemBytes);
            hostStateDAO.merge(hostStateEntity);
        }
    }

    @java.lang.Override
    public java.lang.String getOsArch() {
        return getHostEntity().getOsArch();
    }

    @java.lang.Override
    public void setOsArch(java.lang.String osArch) {
        org.apache.ambari.server.orm.entities.HostEntity hostEntity = getHostEntity();
        hostEntity.setOsArch(osArch);
        hostDAO.merge(hostEntity);
    }

    @java.lang.Override
    public java.lang.String getOsInfo() {
        return getHostEntity().getOsInfo();
    }

    @java.lang.Override
    public void setOsInfo(java.lang.String osInfo) {
        org.apache.ambari.server.orm.entities.HostEntity hostEntity = getHostEntity();
        hostEntity.setOsInfo(osInfo);
        hostDAO.merge(hostEntity);
    }

    @java.lang.Override
    public java.lang.String getOsType() {
        return getHostEntity().getOsType();
    }

    @java.lang.Override
    public void setOsType(java.lang.String osType) {
        org.apache.ambari.server.orm.entities.HostEntity hostEntity = getHostEntity();
        hostEntity.setOsType(osType);
        hostDAO.merge(hostEntity);
    }

    @java.lang.Override
    public java.lang.String getOsFamily() {
        java.util.Map<java.lang.String, java.lang.String> hostAttributes = getHostAttributes();
        return getOSFamilyFromHostAttributes(hostAttributes);
    }

    @java.lang.Override
    public java.lang.String getOsFamily(java.util.Map<java.lang.String, java.lang.String> hostAttributes) {
        return getOSFamilyFromHostAttributes(hostAttributes);
    }

    @java.lang.Override
    public java.lang.String getOSFamilyFromHostAttributes(java.util.Map<java.lang.String, java.lang.String> hostAttributes) {
        try {
            java.lang.String majorVersion = hostAttributes.get(org.apache.ambari.server.state.host.HostImpl.OS_RELEASE_VERSION).split("\\.")[0];
            return hostAttributes.get(org.apache.ambari.server.state.host.HostImpl.OSFAMILY) + majorVersion;
        } catch (java.lang.Exception e) {
            org.apache.ambari.server.state.host.HostImpl.LOG.error("Error while getting os family from host attributes:", e);
        }
        return null;
    }

    @java.lang.Override
    public java.util.List<org.apache.ambari.server.agent.DiskInfo> getDisksInfo() {
        return disksInfo;
    }

    @java.lang.Override
    public void setDisksInfo(java.util.List<org.apache.ambari.server.agent.DiskInfo> disksInfo) {
        this.disksInfo = disksInfo;
    }

    @java.lang.Override
    public org.apache.ambari.server.agent.RecoveryReport getRecoveryReport() {
        return recoveryReport;
    }

    @java.lang.Override
    public void setRecoveryReport(org.apache.ambari.server.agent.RecoveryReport recoveryReport) {
        this.recoveryReport = recoveryReport;
    }

    @java.lang.Override
    public org.apache.ambari.server.state.HostHealthStatus getHealthStatus() {
        org.apache.ambari.server.orm.entities.HostStateEntity hostStateEntity = getHostStateEntity();
        if (hostStateEntity != null) {
            return gson.fromJson(hostStateEntity.getHealthStatus(), org.apache.ambari.server.state.HostHealthStatus.class);
        }
        return null;
    }

    @java.lang.Override
    public org.apache.ambari.server.state.HostHealthStatus getHealthStatus(org.apache.ambari.server.orm.entities.HostStateEntity hostStateEntity) {
        if (hostStateEntity != null) {
            return gson.fromJson(hostStateEntity.getHealthStatus(), org.apache.ambari.server.state.HostHealthStatus.class);
        }
        return null;
    }

    @java.lang.Override
    public void setHealthStatus(org.apache.ambari.server.state.HostHealthStatus healthStatus) {
        org.apache.ambari.server.orm.entities.HostStateEntity hostStateEntity = getHostStateEntity();
        if (hostStateEntity != null) {
            hostStateEntity.setHealthStatus(gson.toJson(healthStatus));
            if (healthStatus.getHealthStatus().equals(org.apache.ambari.server.state.HostHealthStatus.HealthStatus.UNKNOWN)) {
                setStatus(org.apache.ambari.server.state.HostHealthStatus.HealthStatus.UNKNOWN.name());
            }
            hostStateDAO.merge(hostStateEntity);
        }
    }

    @java.lang.Override
    public java.lang.String getPrefix() {
        return prefix;
    }

    @java.lang.Override
    public void setPrefix(java.lang.String prefix) {
        if (org.apache.commons.lang.StringUtils.isNotBlank(prefix) && (!org.apache.commons.lang.StringUtils.equals(this.prefix, prefix))) {
            this.prefix = prefix;
        }
    }

    @java.lang.Override
    public java.util.Map<java.lang.String, java.lang.String> getHostAttributes() {
        return gson.fromJson(getHostEntity().getHostAttributes(), org.apache.ambari.server.state.host.HostImpl.hostAttributesType);
    }

    @java.lang.Override
    public java.util.Map<java.lang.String, java.lang.String> getHostAttributes(org.apache.ambari.server.orm.entities.HostEntity hostEntity) {
        return gson.fromJson(hostEntity.getHostAttributes(), org.apache.ambari.server.state.host.HostImpl.hostAttributesType);
    }

    @java.lang.Override
    public void setHostAttributes(java.util.Map<java.lang.String, java.lang.String> hostAttributes) {
        org.apache.ambari.server.orm.entities.HostEntity hostEntity = getHostEntity();
        java.util.Map<java.lang.String, java.lang.String> hostAttrs = gson.fromJson(hostEntity.getHostAttributes(), org.apache.ambari.server.state.host.HostImpl.hostAttributesType);
        if (hostAttrs == null) {
            hostAttrs = new java.util.concurrent.ConcurrentHashMap<>();
        }
        hostAttrs.putAll(hostAttributes);
        hostEntity.setHostAttributes(gson.toJson(hostAttrs, org.apache.ambari.server.state.host.HostImpl.hostAttributesType));
        hostDAO.merge(hostEntity);
    }

    @java.lang.Override
    public java.lang.String getRackInfo() {
        return getHostEntity().getRackInfo();
    }

    @java.lang.Override
    public void setRackInfo(java.lang.String rackInfo) {
        org.apache.ambari.server.orm.entities.HostEntity hostEntity = getHostEntity();
        hostEntity.setRackInfo(rackInfo);
        hostDAO.merge(hostEntity);
    }

    @java.lang.Override
    public long getLastRegistrationTime() {
        return getHostEntity().getLastRegistrationTime();
    }

    @java.lang.Override
    public void setLastRegistrationTime(long lastRegistrationTime) {
        org.apache.ambari.server.orm.entities.HostEntity hostEntity = getHostEntity();
        hostEntity.setLastRegistrationTime(lastRegistrationTime);
        hostDAO.merge(hostEntity);
    }

    @java.lang.Override
    public long getLastHeartbeatTime() {
        return lastHeartbeatTime;
    }

    @java.lang.Override
    public void setLastHeartbeatTime(long lastHeartbeatTime) {
        this.lastHeartbeatTime = lastHeartbeatTime;
    }

    @java.lang.Override
    public long getLastAgentStartTime() {
        return lastAgentStartTime;
    }

    @java.lang.Override
    public void setLastAgentStartTime(long lastAgentStartTime) {
        this.lastAgentStartTime = lastAgentStartTime;
    }

    @java.lang.Override
    public org.apache.ambari.server.state.AgentVersion getAgentVersion() {
        org.apache.ambari.server.orm.entities.HostStateEntity hostStateEntity = getHostStateEntity();
        if (hostStateEntity != null) {
            return gson.fromJson(hostStateEntity.getAgentVersion(), org.apache.ambari.server.state.AgentVersion.class);
        }
        return null;
    }

    @java.lang.Override
    public org.apache.ambari.server.state.AgentVersion getAgentVersion(org.apache.ambari.server.orm.entities.HostStateEntity hostStateEntity) {
        if (hostStateEntity != null) {
            return gson.fromJson(hostStateEntity.getAgentVersion(), org.apache.ambari.server.state.AgentVersion.class);
        }
        return null;
    }

    @java.lang.Override
    public void setAgentVersion(org.apache.ambari.server.state.AgentVersion agentVersion) {
        org.apache.ambari.server.orm.entities.HostStateEntity hostStateEntity = getHostStateEntity();
        if (hostStateEntity != null) {
            hostStateEntity.setAgentVersion(gson.toJson(agentVersion));
            hostStateDAO.merge(hostStateEntity);
        }
    }

    @java.lang.Override
    public long getTimeInState() {
        org.apache.ambari.server.orm.entities.HostStateEntity hostStateEntity = getHostStateEntity();
        java.lang.Long timeInState = (hostStateEntity != null) ? hostStateEntity.getTimeInState() : null;
        return timeInState != null ? timeInState : 0L;
    }

    @java.lang.Override
    public void setTimeInState(long timeInState) {
        org.apache.ambari.server.orm.entities.HostStateEntity hostStateEntity = getHostStateEntity();
        if (hostStateEntity != null) {
            hostStateEntity.setTimeInState(timeInState);
            hostStateDAO.merge(hostStateEntity);
        }
    }

    @java.lang.Override
    public java.lang.String getStatus() {
        return status;
    }

    @java.lang.Override
    public void setStatus(java.lang.String status) {
        if (!java.util.Objects.equals(this.status, status)) {
            ambariEventPublisher.publish(new org.apache.ambari.server.events.HostStatusUpdateEvent(getHostName(), status));
        }
        this.status = status;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if ((o == null) || (getClass() != o.getClass())) {
            return false;
        }
        org.apache.ambari.server.state.Host that = ((org.apache.ambari.server.state.Host) (o));
        return getHostName().equals(that.getHostName());
    }

    @java.lang.Override
    public int hashCode() {
        return null == getHostName() ? 0 : getHostName().hashCode();
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.HostResponse convertToResponse() {
        org.apache.ambari.server.controller.HostResponse r = new org.apache.ambari.server.controller.HostResponse(getHostName());
        org.apache.ambari.server.orm.entities.HostEntity hostEntity = getHostEntity();
        org.apache.ambari.server.orm.entities.HostStateEntity hostStateEntity = getHostStateEntity();
        java.util.Map<java.lang.String, java.lang.String> hostAttributes = getHostAttributes(hostEntity);
        r.setHostAttributes(hostAttributes);
        r.setOsFamily(getOsFamily(hostAttributes));
        r.setAgentVersion(getAgentVersion(hostStateEntity));
        r.setHealthStatus(getHealthStatus(hostStateEntity));
        r.setPhCpuCount(hostEntity.getPhCpuCount());
        r.setCpuCount(hostEntity.getCpuCount());
        r.setIpv4(hostEntity.getIpv4());
        r.setOsArch(hostEntity.getOsArch());
        r.setOsType(hostEntity.getOsType());
        r.setTotalMemBytes(hostEntity.getTotalMem());
        r.setLastRegistrationTime(hostEntity.getLastRegistrationTime());
        r.setPublicHostName(hostEntity.getPublicHostName());
        r.setRackInfo(hostEntity.getRackInfo());
        r.setDisksInfo(getDisksInfo());
        r.setStatus(getStatus());
        r.setLastHeartbeatTime(getLastHeartbeatTime());
        r.setLastAgentEnv(lastAgentEnv);
        r.setRecoveryReport(getRecoveryReport());
        r.setRecoverySummary(getRecoveryReport().getSummary());
        r.setHostState(getState());
        return r;
    }

    @com.google.inject.persist.Transactional
    void persistEntities(org.apache.ambari.server.orm.entities.HostEntity hostEntity) {
        hostDAO.create(hostEntity);
        if (!hostEntity.getClusterEntities().isEmpty()) {
            for (org.apache.ambari.server.orm.entities.ClusterEntity clusterEntity : hostEntity.getClusterEntities()) {
                clusterEntity.getHostEntities().add(hostEntity);
                clusterDAO.merge(clusterEntity);
            }
        }
    }

    @java.lang.Override
    @com.google.inject.persist.Transactional
    public boolean addDesiredConfig(long clusterId, boolean selected, java.lang.String user, org.apache.ambari.server.state.Config config) {
        if (null == user) {
            throw new java.lang.NullPointerException("User must be specified.");
        }
        org.apache.ambari.server.orm.cache.HostConfigMapping exist = getDesiredConfigEntity(clusterId, config.getType());
        if ((null != exist) && exist.getVersion().equals(config.getTag())) {
            if (!selected) {
                exist.setSelected(0);
                hostConfigMappingDAO.merge(exist);
            }
            return false;
        }
        writeLock.lock();
        org.apache.ambari.server.orm.entities.HostEntity hostEntity = getHostEntity();
        try {
            for (org.apache.ambari.server.orm.cache.HostConfigMapping e : hostConfigMappingDAO.findByType(clusterId, hostEntity.getHostId(), config.getType())) {
                e.setSelected(0);
                hostConfigMappingDAO.merge(e);
            }
            org.apache.ambari.server.orm.cache.HostConfigMapping hostConfigMapping = new org.apache.ambari.server.orm.cache.HostConfigMappingImpl();
            hostConfigMapping.setClusterId(clusterId);
            hostConfigMapping.setCreateTimestamp(java.lang.System.currentTimeMillis());
            hostConfigMapping.setHostId(hostEntity.getHostId());
            hostConfigMapping.setSelected(1);
            hostConfigMapping.setUser(user);
            hostConfigMapping.setType(config.getType());
            hostConfigMapping.setVersion(config.getTag());
            hostConfigMappingDAO.create(hostConfigMapping);
        } finally {
            writeLock.unlock();
        }
        hostDAO.merge(hostEntity);
        return true;
    }

    @java.lang.Override
    public java.util.Map<java.lang.String, org.apache.ambari.server.state.DesiredConfig> getDesiredConfigs(long clusterId) {
        java.util.Map<java.lang.String, org.apache.ambari.server.state.DesiredConfig> map = new java.util.HashMap<>();
        for (org.apache.ambari.server.orm.cache.HostConfigMapping e : hostConfigMappingDAO.findSelected(clusterId, getHostId())) {
            org.apache.ambari.server.state.DesiredConfig dc = new org.apache.ambari.server.state.DesiredConfig();
            dc.setTag(e.getVersion());
            dc.setServiceName(e.getServiceName());
            map.put(e.getType(), dc);
        }
        return map;
    }

    @java.lang.Override
    public java.util.Map<java.lang.String, org.apache.ambari.server.state.HostConfig> getDesiredHostConfigs(org.apache.ambari.server.state.Cluster cluster, java.util.Map<java.lang.String, org.apache.ambari.server.state.DesiredConfig> clusterDesiredConfigs) throws org.apache.ambari.server.AmbariException {
        if (null == cluster) {
            clusterDesiredConfigs = new java.util.HashMap<>();
        }
        if (null == clusterDesiredConfigs) {
            clusterDesiredConfigs = cluster.getDesiredConfigs();
        }
        java.util.Map<java.lang.String, org.apache.ambari.server.state.HostConfig> hostConfigMap = clusterDesiredConfigs.entrySet().stream().collect(java.util.stream.Collectors.toMap(java.util.Map.Entry::getKey, desiredConfigEntry -> {
            org.apache.ambari.server.state.HostConfig hostConfig = new org.apache.ambari.server.state.HostConfig();
            hostConfig.setDefaultVersionTag(desiredConfigEntry.getValue().getTag());
            return hostConfig;
        }));
        java.util.Map<java.lang.Long, org.apache.ambari.server.state.configgroup.ConfigGroup> configGroups = (cluster == null) ? new java.util.HashMap<>() : cluster.getConfigGroupsByHostname(getHostName());
        if ((configGroups == null) || configGroups.isEmpty()) {
            return hostConfigMap;
        }
        for (org.apache.ambari.server.state.configgroup.ConfigGroup configGroup : configGroups.values()) {
            for (java.util.Map.Entry<java.lang.String, org.apache.ambari.server.state.Config> configEntry : configGroup.getConfigurations().entrySet()) {
                java.lang.String configType = configEntry.getKey();
                org.apache.ambari.server.state.HostConfig hostConfig = hostConfigMap.get(configType);
                if (hostConfig == null) {
                    hostConfig = new org.apache.ambari.server.state.HostConfig();
                    hostConfigMap.put(configType, hostConfig);
                    org.apache.ambari.server.state.Config conf = cluster.getDesiredConfigByType(configType);
                    if (conf == null) {
                        org.apache.ambari.server.state.host.HostImpl.LOG.error("Config inconsistency exists: unknown configType=" + configType);
                    } else {
                        hostConfig.setDefaultVersionTag(conf.getTag());
                    }
                }
                hostConfig.getConfigGroupOverrides().put(configGroup.getId(), configEntry.getValue().getTag());
            }
        }
        return hostConfigMap;
    }

    private org.apache.ambari.server.orm.cache.HostConfigMapping getDesiredConfigEntity(long clusterId, java.lang.String type) {
        return hostConfigMappingDAO.findSelectedByType(clusterId, getHostId(), type);
    }

    private java.util.concurrent.ConcurrentMap<java.lang.Long, org.apache.ambari.server.state.MaintenanceState> ensureMaintMap(org.apache.ambari.server.orm.entities.HostStateEntity hostStateEntity) {
        if ((null == hostStateEntity) || (null == hostStateEntity.getMaintenanceState())) {
            return new java.util.concurrent.ConcurrentHashMap<>();
        }
        java.lang.String entity = hostStateEntity.getMaintenanceState();
        final java.util.concurrent.ConcurrentMap<java.lang.Long, org.apache.ambari.server.state.MaintenanceState> map;
        try {
            java.util.Map<java.lang.Long, org.apache.ambari.server.state.MaintenanceState> gsonMap = gson.fromJson(entity, org.apache.ambari.server.state.host.HostImpl.maintMapType);
            map = new java.util.concurrent.ConcurrentHashMap<>(gsonMap);
        } catch (java.lang.Exception e) {
            return new java.util.concurrent.ConcurrentHashMap<>();
        }
        return map;
    }

    @java.lang.Override
    public void setMaintenanceState(long clusterId, org.apache.ambari.server.state.MaintenanceState state) {
        maintMap.put(clusterId, state);
        java.lang.String json = gson.toJson(maintMap, org.apache.ambari.server.state.host.HostImpl.maintMapType);
        org.apache.ambari.server.orm.entities.HostStateEntity hostStateEntity = getHostStateEntity();
        if (hostStateEntity != null) {
            hostStateEntity.setMaintenanceState(json);
            hostStateDAO.merge(hostStateEntity);
            org.apache.ambari.server.events.MaintenanceModeEvent event = new org.apache.ambari.server.events.MaintenanceModeEvent(state, clusterId, this);
            eventPublisher.publish(event);
        }
    }

    @java.lang.Override
    public org.apache.ambari.server.state.MaintenanceState getMaintenanceState(long clusterId) {
        if (!maintMap.containsKey(clusterId)) {
            maintMap.put(clusterId, org.apache.ambari.server.state.MaintenanceState.OFF);
        }
        return maintMap.get(clusterId);
    }

    @java.lang.Override
    public java.util.List<org.apache.ambari.server.orm.entities.HostVersionEntity> getAllHostVersions() {
        return hostVersionDAO.findByHost(getHostName());
    }

    @java.lang.Override
    public org.apache.ambari.server.orm.entities.HostEntity getHostEntity() {
        return hostDAO.findById(hostId);
    }

    public org.apache.ambari.server.orm.entities.HostStateEntity getHostStateEntity() {
        return hostStateDAO.findByHostId(hostId);
    }

    @java.lang.Override
    public boolean hasComponentsAdvertisingVersions(org.apache.ambari.server.state.StackId stackId) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.orm.entities.HostEntity hostEntity = getHostEntity();
        for (org.apache.ambari.server.orm.entities.HostComponentStateEntity componentState : hostEntity.getHostComponentStateEntities()) {
            org.apache.ambari.server.state.ComponentInfo component = ambariMetaInfo.getComponent(stackId.getStackName(), stackId.getStackVersion(), componentState.getServiceName(), componentState.getComponentName());
            if (component.isVersionAdvertised()) {
                return true;
            }
        }
        return false;
    }

    @java.lang.Override
    public void calculateHostStatus(java.lang.Long clusterId) throws org.apache.ambari.server.AmbariException {
        int masterCount = 0;
        int mastersRunning = 0;
        int slaveCount = 0;
        int slavesRunning = 0;
        org.apache.ambari.server.state.StackId stackId;
        org.apache.ambari.server.state.Cluster cluster = clusters.getCluster(clusterId);
        stackId = cluster.getDesiredStackVersion();
        java.util.List<org.apache.ambari.server.state.ServiceComponentHost> scHosts = cluster.getServiceComponentHosts(hostName);
        for (org.apache.ambari.server.state.ServiceComponentHost scHost : scHosts) {
            org.apache.ambari.server.state.ComponentInfo componentInfo = ambariMetaInfo.getComponent(stackId.getStackName(), stackId.getStackVersion(), scHost.getServiceName(), scHost.getServiceComponentName());
            java.lang.String status = scHost.getState().name();
            java.lang.String category = componentInfo.getCategory();
            if (category == null) {
                org.apache.ambari.server.state.host.HostImpl.LOG.warn("In stack {}-{} service {} component {} category is null!", stackId.getStackName(), stackId.getStackVersion(), scHost.getServiceName(), scHost.getServiceComponentName());
                continue;
            }
            if (org.apache.ambari.server.state.MaintenanceState.OFF == maintenanceStateHelper.getEffectiveState(scHost, this)) {
                if (java.util.Objects.equals("MASTER", category)) {
                    ++masterCount;
                    if (java.util.Objects.equals("STARTED", status)) {
                        ++mastersRunning;
                    }
                } else if (java.util.Objects.equals("SLAVE", category)) {
                    ++slaveCount;
                    if (java.util.Objects.equals("STARTED", status)) {
                        ++slavesRunning;
                    }
                }
            }
        }
        org.apache.ambari.server.state.HostHealthStatus.HealthStatus healthStatus;
        if ((masterCount == mastersRunning) && (slaveCount == slavesRunning)) {
            healthStatus = org.apache.ambari.server.state.HostHealthStatus.HealthStatus.HEALTHY;
        } else if ((masterCount > 0) && (mastersRunning < masterCount)) {
            healthStatus = org.apache.ambari.server.state.HostHealthStatus.HealthStatus.UNHEALTHY;
        } else {
            healthStatus = org.apache.ambari.server.state.HostHealthStatus.HealthStatus.ALERT;
        }
        setStatus(healthStatus.name());
    }

    @com.google.inject.persist.Transactional
    public void updateHost(org.apache.ambari.server.state.host.HostRegistrationRequestEvent e) {
        importHostInfo(e.hostInfo);
        setLastAgentEnv(e.agentEnv);
        setAgentVersion(e.agentVersion);
        setPublicHostName(e.publicHostName);
        setState(org.apache.ambari.server.state.HostState.INIT);
    }

    @com.google.inject.persist.Transactional
    public void updateHostTimestamps(org.apache.ambari.server.state.host.HostRegistrationRequestEvent e) {
        setLastHeartbeatTime(e.registrationTime);
        setLastRegistrationTime(e.registrationTime);
        setLastAgentStartTime(e.agentStartTime);
        setTimeInState(e.registrationTime);
    }

    @java.lang.Override
    public boolean isRepositoryVersionCorrect(org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.orm.entities.HostEntity hostEntity = getHostEntity();
        java.util.Collection<org.apache.ambari.server.orm.entities.HostComponentStateEntity> hostComponentStates = hostEntity.getHostComponentStateEntities();
        for (org.apache.ambari.server.orm.entities.HostComponentStateEntity hostComponentState : hostComponentStates) {
            org.apache.ambari.server.orm.entities.ServiceComponentDesiredStateEntity desiredComponmentState = hostComponentState.getServiceComponentDesiredStateEntity();
            org.apache.ambari.server.orm.entities.RepositoryVersionEntity desiredRepositoryVersion = desiredComponmentState.getDesiredRepositoryVersion();
            org.apache.ambari.server.state.ComponentInfo componentInfo = ambariMetaInfo.getComponent(desiredRepositoryVersion.getStackName(), desiredRepositoryVersion.getStackVersion(), hostComponentState.getServiceName(), hostComponentState.getComponentName());
            if (!componentInfo.isVersionAdvertised()) {
                continue;
            }
            if (!repositoryVersion.equals(desiredRepositoryVersion)) {
                continue;
            }
            java.lang.String versionAdvertised = hostComponentState.getVersion();
            if ((hostComponentState.getUpgradeState() == org.apache.ambari.server.state.UpgradeState.IN_PROGRESS) || (!org.apache.commons.lang.StringUtils.equals(versionAdvertised, repositoryVersion.getVersion()))) {
                return false;
            }
        }
        return true;
    }
}