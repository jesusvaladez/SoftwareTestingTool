package org.apache.ambari.server.state.svccomphost;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import com.google.inject.persist.Transactional;
public class ServiceComponentHostImpl implements org.apache.ambari.server.state.ServiceComponentHost {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl.class);

    private final java.util.concurrent.locks.ReadWriteLock readWriteLock = new java.util.concurrent.locks.ReentrantReadWriteLock();

    private final java.util.concurrent.locks.Lock writeLock = readWriteLock.writeLock();

    private final org.apache.ambari.server.state.ServiceComponent serviceComponent;

    private final org.apache.ambari.server.state.Host host;

    private final org.apache.ambari.server.orm.dao.HostComponentStateDAO hostComponentStateDAO;

    private final org.apache.ambari.server.orm.dao.HostComponentDesiredStateDAO hostComponentDesiredStateDAO;

    private final org.apache.ambari.server.orm.dao.HostDAO hostDAO;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.RepositoryVersionDAO repositoryVersionDAO;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.HostVersionDAO hostVersionDAO;

    private final org.apache.ambari.server.orm.dao.ServiceComponentDesiredStateDAO serviceComponentDesiredStateDAO;

    private final org.apache.ambari.server.state.Clusters clusters;

    @com.google.inject.Inject
    private org.apache.ambari.server.state.ConfigHelper helper;

    @com.google.inject.Inject
    private org.apache.ambari.server.api.services.AmbariMetaInfo ambariMetaInfo;

    @com.google.inject.Inject
    private org.apache.ambari.server.stack.upgrade.RepositoryVersionHelper repositoryVersionHelper;

    @com.google.inject.Inject
    org.apache.ambari.server.events.publishers.STOMPUpdatePublisher STOMPUpdatePublisher;

    @com.google.inject.Inject
    private com.google.inject.Provider<org.apache.ambari.server.agent.stomp.TopologyHolder> m_topologyHolder;

    @com.google.inject.Inject
    private com.google.inject.Provider<org.apache.ambari.server.agent.stomp.HostLevelParamsHolder> m_hostLevelParamsHolder;

    @com.google.inject.Inject
    private org.apache.ambari.server.state.alert.AlertDefinitionHash alertDefinitionHash;

    @com.google.inject.Inject
    private com.google.inject.Provider<org.apache.ambari.server.controller.AmbariManagementController> controller;

    private final org.apache.ambari.server.events.publishers.AmbariEventPublisher eventPublisher;

    private final org.apache.ambari.server.orm.dao.StackDAO stackDAO;

    private final java.lang.Long desiredStateEntityId;

    private final java.lang.Long hostComponentStateId;

    private long lastOpStartTime;

    private long lastOpEndTime;

    private long lastOpLastUpdateTime;

    private java.util.concurrent.atomic.AtomicReference<org.apache.ambari.server.state.MaintenanceState> maintenanceState = new java.util.concurrent.atomic.AtomicReference<>();

    private java.util.concurrent.ConcurrentMap<java.lang.String, org.apache.ambari.server.state.HostConfig> actualConfigs = new java.util.concurrent.ConcurrentHashMap<>();

    private com.google.common.collect.ImmutableList<java.util.Map<java.lang.String, java.lang.String>> processes = com.google.common.collect.ImmutableList.of();

    private static final com.google.common.util.concurrent.Striped<java.util.concurrent.locks.Lock> HOST_VERSION_LOCK = com.google.common.util.concurrent.Striped.lazyWeakLock(20);

    private final java.lang.String hostName;

    private static final org.apache.ambari.server.state.fsm.StateMachineFactory<org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl, org.apache.ambari.server.state.State, org.apache.ambari.server.state.ServiceComponentHostEventType, org.apache.ambari.server.state.ServiceComponentHostEvent> daemonStateMachineFactory = new org.apache.ambari.server.state.fsm.StateMachineFactory<org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl, org.apache.ambari.server.state.State, org.apache.ambari.server.state.ServiceComponentHostEventType, org.apache.ambari.server.state.ServiceComponentHostEvent>(org.apache.ambari.server.state.State.INIT).addTransition(org.apache.ambari.server.state.State.INIT, org.apache.ambari.server.state.State.INSTALLING, org.apache.ambari.server.state.ServiceComponentHostEventType.HOST_SVCCOMP_INSTALL, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl.ServiceComponentHostOpStartedTransition()).addTransition(org.apache.ambari.server.state.State.INSTALLING, org.apache.ambari.server.state.State.INSTALLED, org.apache.ambari.server.state.ServiceComponentHostEventType.HOST_SVCCOMP_OP_SUCCEEDED, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl.ServiceComponentHostOpCompletedTransition()).addTransition(org.apache.ambari.server.state.State.INSTALLING, org.apache.ambari.server.state.State.INSTALLED, org.apache.ambari.server.state.ServiceComponentHostEventType.HOST_SVCCOMP_OP_SUCCEEDED, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl.AlertDefinitionCommandTransition()).addTransition(org.apache.ambari.server.state.State.INSTALLED, org.apache.ambari.server.state.State.INSTALLED, org.apache.ambari.server.state.ServiceComponentHostEventType.HOST_SVCCOMP_OP_SUCCEEDED, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl.ServiceComponentHostOpCompletedTransition()).addTransition(org.apache.ambari.server.state.State.INSTALLED, org.apache.ambari.server.state.State.INSTALLED, org.apache.ambari.server.state.ServiceComponentHostEventType.HOST_SVCCOMP_OP_FAILED, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl.ServiceComponentHostOpCompletedTransition()).addTransition(org.apache.ambari.server.state.State.INSTALLING, org.apache.ambari.server.state.State.INSTALLING, org.apache.ambari.server.state.ServiceComponentHostEventType.HOST_SVCCOMP_OP_IN_PROGRESS, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl.ServiceComponentHostOpInProgressTransition()).addTransition(org.apache.ambari.server.state.State.INSTALLING, org.apache.ambari.server.state.State.INSTALLING, org.apache.ambari.server.state.ServiceComponentHostEventType.HOST_SVCCOMP_INSTALL, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl.ServiceComponentHostOpStartedTransition()).addTransition(org.apache.ambari.server.state.State.INSTALLING, org.apache.ambari.server.state.State.INSTALL_FAILED, org.apache.ambari.server.state.ServiceComponentHostEventType.HOST_SVCCOMP_OP_FAILED, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl.ServiceComponentHostOpCompletedTransition()).addTransition(org.apache.ambari.server.state.State.INSTALL_FAILED, org.apache.ambari.server.state.State.INSTALLING, org.apache.ambari.server.state.ServiceComponentHostEventType.HOST_SVCCOMP_OP_RESTART, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl.ServiceComponentHostOpStartedTransition()).addTransition(org.apache.ambari.server.state.State.INSTALL_FAILED, org.apache.ambari.server.state.State.INSTALLING, org.apache.ambari.server.state.ServiceComponentHostEventType.HOST_SVCCOMP_INSTALL, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl.ServiceComponentHostOpStartedTransition()).addTransition(org.apache.ambari.server.state.State.INSTALL_FAILED, org.apache.ambari.server.state.State.INSTALL_FAILED, org.apache.ambari.server.state.ServiceComponentHostEventType.HOST_SVCCOMP_OP_FAILED, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl.ServiceComponentHostOpCompletedTransition()).addTransition(org.apache.ambari.server.state.State.INSTALLED, org.apache.ambari.server.state.State.STARTING, org.apache.ambari.server.state.ServiceComponentHostEventType.HOST_SVCCOMP_START, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl.ServiceComponentHostOpStartedTransition()).addTransition(org.apache.ambari.server.state.State.INSTALLED, org.apache.ambari.server.state.State.UNINSTALLING, org.apache.ambari.server.state.ServiceComponentHostEventType.HOST_SVCCOMP_UNINSTALL, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl.ServiceComponentHostOpStartedTransition()).addTransition(org.apache.ambari.server.state.State.INSTALLED, org.apache.ambari.server.state.State.INSTALLING, org.apache.ambari.server.state.ServiceComponentHostEventType.HOST_SVCCOMP_INSTALL, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl.ServiceComponentHostOpStartedTransition()).addTransition(org.apache.ambari.server.state.State.INSTALLED, org.apache.ambari.server.state.State.STOPPING, org.apache.ambari.server.state.ServiceComponentHostEventType.HOST_SVCCOMP_STOP, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl.ServiceComponentHostOpStartedTransition()).addTransition(org.apache.ambari.server.state.State.INSTALLED, org.apache.ambari.server.state.State.UPGRADING, org.apache.ambari.server.state.ServiceComponentHostEventType.HOST_SVCCOMP_UPGRADE, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl.ServiceComponentHostOpStartedTransition()).addTransition(org.apache.ambari.server.state.State.INSTALLED, org.apache.ambari.server.state.State.INSTALLED, org.apache.ambari.server.state.ServiceComponentHostEventType.HOST_SVCCOMP_OP_IN_PROGRESS, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl.ServiceComponentHostOpInProgressTransition()).addTransition(org.apache.ambari.server.state.State.INSTALLED, org.apache.ambari.server.state.State.STARTED, org.apache.ambari.server.state.ServiceComponentHostEventType.HOST_SVCCOMP_STARTED, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl.ServiceComponentHostOpCompletedTransition()).addTransition(org.apache.ambari.server.state.State.INSTALLED, org.apache.ambari.server.state.State.INSTALLED, org.apache.ambari.server.state.ServiceComponentHostEventType.HOST_SVCCOMP_STOPPED, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl.ServiceComponentHostOpCompletedTransition()).addTransition(org.apache.ambari.server.state.State.STARTING, org.apache.ambari.server.state.State.STARTING, org.apache.ambari.server.state.ServiceComponentHostEventType.HOST_SVCCOMP_OP_IN_PROGRESS, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl.ServiceComponentHostOpInProgressTransition()).addTransition(org.apache.ambari.server.state.State.STARTING, org.apache.ambari.server.state.State.STARTING, org.apache.ambari.server.state.ServiceComponentHostEventType.HOST_SVCCOMP_START, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl.ServiceComponentHostOpStartedTransition()).addTransition(org.apache.ambari.server.state.State.STARTING, org.apache.ambari.server.state.State.STARTED, org.apache.ambari.server.state.ServiceComponentHostEventType.HOST_SVCCOMP_STARTED, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl.ServiceComponentHostOpCompletedTransition()).addTransition(org.apache.ambari.server.state.State.STARTING, org.apache.ambari.server.state.State.INSTALLED, org.apache.ambari.server.state.ServiceComponentHostEventType.HOST_SVCCOMP_OP_FAILED, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl.ServiceComponentHostOpCompletedTransition()).addTransition(org.apache.ambari.server.state.State.INSTALLED, org.apache.ambari.server.state.State.STARTING, org.apache.ambari.server.state.ServiceComponentHostEventType.HOST_SVCCOMP_OP_RESTART, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl.ServiceComponentHostOpStartedTransition()).addTransition(org.apache.ambari.server.state.State.STARTED, org.apache.ambari.server.state.State.STARTED, org.apache.ambari.server.state.ServiceComponentHostEventType.HOST_SVCCOMP_STARTED, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl.ServiceComponentHostOpCompletedTransition()).addTransition(org.apache.ambari.server.state.State.STARTED, org.apache.ambari.server.state.State.STOPPING, org.apache.ambari.server.state.ServiceComponentHostEventType.HOST_SVCCOMP_STOP, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl.ServiceComponentHostOpStartedTransition()).addTransition(org.apache.ambari.server.state.State.STARTED, org.apache.ambari.server.state.State.STARTED, org.apache.ambari.server.state.ServiceComponentHostEventType.HOST_SVCCOMP_OP_IN_PROGRESS, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl.ServiceComponentHostOpInProgressTransition()).addTransition(org.apache.ambari.server.state.State.STARTED, org.apache.ambari.server.state.State.STARTED, org.apache.ambari.server.state.ServiceComponentHostEventType.HOST_SVCCOMP_OP_FAILED, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl.ServiceComponentHostOpCompletedTransition()).addTransition(org.apache.ambari.server.state.State.STARTED, org.apache.ambari.server.state.State.INSTALLED, org.apache.ambari.server.state.ServiceComponentHostEventType.HOST_SVCCOMP_STOPPED, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl.ServiceComponentHostOpCompletedTransition()).addTransition(org.apache.ambari.server.state.State.STOPPING, org.apache.ambari.server.state.State.STOPPING, org.apache.ambari.server.state.ServiceComponentHostEventType.HOST_SVCCOMP_OP_IN_PROGRESS, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl.ServiceComponentHostOpInProgressTransition()).addTransition(org.apache.ambari.server.state.State.STOPPING, org.apache.ambari.server.state.State.INSTALLED, org.apache.ambari.server.state.ServiceComponentHostEventType.HOST_SVCCOMP_STOPPED, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl.ServiceComponentHostOpCompletedTransition()).addTransition(org.apache.ambari.server.state.State.STOPPING, org.apache.ambari.server.state.State.STARTED, org.apache.ambari.server.state.ServiceComponentHostEventType.HOST_SVCCOMP_OP_FAILED, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl.ServiceComponentHostOpCompletedTransition()).addTransition(org.apache.ambari.server.state.State.STARTED, org.apache.ambari.server.state.State.STOPPING, org.apache.ambari.server.state.ServiceComponentHostEventType.HOST_SVCCOMP_OP_RESTART, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl.ServiceComponentHostOpStartedTransition()).addTransition(org.apache.ambari.server.state.State.UNINSTALLING, org.apache.ambari.server.state.State.UNINSTALLING, org.apache.ambari.server.state.ServiceComponentHostEventType.HOST_SVCCOMP_OP_IN_PROGRESS, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl.ServiceComponentHostOpInProgressTransition()).addTransition(org.apache.ambari.server.state.State.UNINSTALLING, org.apache.ambari.server.state.State.UNINSTALLED, org.apache.ambari.server.state.ServiceComponentHostEventType.HOST_SVCCOMP_OP_SUCCEEDED, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl.ServiceComponentHostOpCompletedTransition()).addTransition(org.apache.ambari.server.state.State.UNINSTALLING, org.apache.ambari.server.state.State.UNINSTALLING, org.apache.ambari.server.state.ServiceComponentHostEventType.HOST_SVCCOMP_OP_FAILED, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl.ServiceComponentHostOpCompletedTransition()).addTransition(org.apache.ambari.server.state.State.UPGRADING, org.apache.ambari.server.state.State.UPGRADING, org.apache.ambari.server.state.ServiceComponentHostEventType.HOST_SVCCOMP_OP_IN_PROGRESS, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl.ServiceComponentHostOpInProgressTransition()).addTransition(org.apache.ambari.server.state.State.UPGRADING, org.apache.ambari.server.state.State.INSTALLED, org.apache.ambari.server.state.ServiceComponentHostEventType.HOST_SVCCOMP_OP_SUCCEEDED, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl.ServiceComponentHostOpCompletedTransition()).addTransition(org.apache.ambari.server.state.State.UPGRADING, org.apache.ambari.server.state.State.UPGRADING, org.apache.ambari.server.state.ServiceComponentHostEventType.HOST_SVCCOMP_OP_FAILED, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl.ServiceComponentHostOpCompletedTransition()).addTransition(org.apache.ambari.server.state.State.UPGRADING, org.apache.ambari.server.state.State.UPGRADING, org.apache.ambari.server.state.ServiceComponentHostEventType.HOST_SVCCOMP_UPGRADE, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl.ServiceComponentHostOpStartedTransition()).addTransition(org.apache.ambari.server.state.State.UPGRADING, org.apache.ambari.server.state.State.UPGRADING, org.apache.ambari.server.state.ServiceComponentHostEventType.HOST_SVCCOMP_UPGRADE, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl.ServiceComponentHostOpInProgressTransition()).addTransition(org.apache.ambari.server.state.State.UNINSTALLING, org.apache.ambari.server.state.State.UNINSTALLING, org.apache.ambari.server.state.ServiceComponentHostEventType.HOST_SVCCOMP_OP_RESTART, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl.ServiceComponentHostOpStartedTransition()).addTransition(org.apache.ambari.server.state.State.UNINSTALLING, org.apache.ambari.server.state.State.UNINSTALLING, org.apache.ambari.server.state.ServiceComponentHostEventType.HOST_SVCCOMP_UNINSTALL, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl.ServiceComponentHostOpStartedTransition()).addTransition(org.apache.ambari.server.state.State.UNINSTALLED, org.apache.ambari.server.state.State.INSTALLING, org.apache.ambari.server.state.ServiceComponentHostEventType.HOST_SVCCOMP_INSTALL, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl.ServiceComponentHostOpStartedTransition()).addTransition(org.apache.ambari.server.state.State.UNINSTALLED, org.apache.ambari.server.state.State.WIPING_OUT, org.apache.ambari.server.state.ServiceComponentHostEventType.HOST_SVCCOMP_WIPEOUT, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl.ServiceComponentHostOpStartedTransition()).addTransition(org.apache.ambari.server.state.State.WIPING_OUT, org.apache.ambari.server.state.State.WIPING_OUT, org.apache.ambari.server.state.ServiceComponentHostEventType.HOST_SVCCOMP_OP_IN_PROGRESS, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl.ServiceComponentHostOpInProgressTransition()).addTransition(org.apache.ambari.server.state.State.WIPING_OUT, org.apache.ambari.server.state.State.INIT, org.apache.ambari.server.state.ServiceComponentHostEventType.HOST_SVCCOMP_OP_SUCCEEDED, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl.ServiceComponentHostOpCompletedTransition()).addTransition(org.apache.ambari.server.state.State.WIPING_OUT, org.apache.ambari.server.state.State.WIPING_OUT, org.apache.ambari.server.state.ServiceComponentHostEventType.HOST_SVCCOMP_OP_FAILED, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl.ServiceComponentHostOpCompletedTransition()).addTransition(org.apache.ambari.server.state.State.WIPING_OUT, org.apache.ambari.server.state.State.WIPING_OUT, org.apache.ambari.server.state.ServiceComponentHostEventType.HOST_SVCCOMP_OP_RESTART, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl.ServiceComponentHostOpStartedTransition()).addTransition(org.apache.ambari.server.state.State.WIPING_OUT, org.apache.ambari.server.state.State.WIPING_OUT, org.apache.ambari.server.state.ServiceComponentHostEventType.HOST_SVCCOMP_WIPEOUT, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl.ServiceComponentHostOpStartedTransition()).addTransition(org.apache.ambari.server.state.State.INSTALLED, org.apache.ambari.server.state.State.DISABLED, org.apache.ambari.server.state.ServiceComponentHostEventType.HOST_SVCCOMP_DISABLE, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl.ServiceComponentHostOpCompletedTransition()).addTransition(org.apache.ambari.server.state.State.DISABLED, org.apache.ambari.server.state.State.DISABLED, org.apache.ambari.server.state.ServiceComponentHostEventType.HOST_SVCCOMP_DISABLE, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl.ServiceComponentHostOpCompletedTransition()).addTransition(org.apache.ambari.server.state.State.UNKNOWN, org.apache.ambari.server.state.State.DISABLED, org.apache.ambari.server.state.ServiceComponentHostEventType.HOST_SVCCOMP_DISABLE, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl.ServiceComponentHostOpCompletedTransition()).addTransition(org.apache.ambari.server.state.State.UNKNOWN, org.apache.ambari.server.state.State.UNKNOWN, org.apache.ambari.server.state.ServiceComponentHostEventType.HOST_SVCCOMP_OP_IN_PROGRESS, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl.ServiceComponentHostOpCompletedTransition()).addTransition(org.apache.ambari.server.state.State.INSTALL_FAILED, org.apache.ambari.server.state.State.DISABLED, org.apache.ambari.server.state.ServiceComponentHostEventType.HOST_SVCCOMP_DISABLE, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl.ServiceComponentHostOpCompletedTransition()).addTransition(org.apache.ambari.server.state.State.DISABLED, org.apache.ambari.server.state.State.INSTALLED, org.apache.ambari.server.state.ServiceComponentHostEventType.HOST_SVCCOMP_RESTORE, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl.ServiceComponentHostOpCompletedTransition()).installTopology();

    private static final org.apache.ambari.server.state.fsm.StateMachineFactory<org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl, org.apache.ambari.server.state.State, org.apache.ambari.server.state.ServiceComponentHostEventType, org.apache.ambari.server.state.ServiceComponentHostEvent> clientStateMachineFactory = new org.apache.ambari.server.state.fsm.StateMachineFactory<org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl, org.apache.ambari.server.state.State, org.apache.ambari.server.state.ServiceComponentHostEventType, org.apache.ambari.server.state.ServiceComponentHostEvent>(org.apache.ambari.server.state.State.INIT).addTransition(org.apache.ambari.server.state.State.INIT, org.apache.ambari.server.state.State.INSTALLING, org.apache.ambari.server.state.ServiceComponentHostEventType.HOST_SVCCOMP_INSTALL, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl.ServiceComponentHostOpStartedTransition()).addTransition(org.apache.ambari.server.state.State.INSTALLING, org.apache.ambari.server.state.State.INSTALLED, org.apache.ambari.server.state.ServiceComponentHostEventType.HOST_SVCCOMP_OP_SUCCEEDED, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl.ServiceComponentHostOpCompletedTransition()).addTransition(org.apache.ambari.server.state.State.INSTALLING, org.apache.ambari.server.state.State.INSTALLING, org.apache.ambari.server.state.ServiceComponentHostEventType.HOST_SVCCOMP_INSTALL, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl.ServiceComponentHostOpStartedTransition()).addTransition(org.apache.ambari.server.state.State.INSTALLING, org.apache.ambari.server.state.State.INSTALLING, org.apache.ambari.server.state.ServiceComponentHostEventType.HOST_SVCCOMP_OP_IN_PROGRESS, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl.ServiceComponentHostOpInProgressTransition()).addTransition(org.apache.ambari.server.state.State.INSTALLED, org.apache.ambari.server.state.State.INSTALLED, org.apache.ambari.server.state.ServiceComponentHostEventType.HOST_SVCCOMP_OP_IN_PROGRESS, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl.ServiceComponentHostOpInProgressTransition()).addTransition(org.apache.ambari.server.state.State.INSTALLED, org.apache.ambari.server.state.State.INSTALLED, org.apache.ambari.server.state.ServiceComponentHostEventType.HOST_SVCCOMP_OP_FAILED, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl.ServiceComponentHostOpCompletedTransition()).addTransition(org.apache.ambari.server.state.State.INSTALLING, org.apache.ambari.server.state.State.INSTALL_FAILED, org.apache.ambari.server.state.ServiceComponentHostEventType.HOST_SVCCOMP_OP_FAILED, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl.ServiceComponentHostOpCompletedTransition()).addTransition(org.apache.ambari.server.state.State.INSTALL_FAILED, org.apache.ambari.server.state.State.INSTALLING, org.apache.ambari.server.state.ServiceComponentHostEventType.HOST_SVCCOMP_OP_RESTART, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl.ServiceComponentHostOpStartedTransition()).addTransition(org.apache.ambari.server.state.State.INSTALL_FAILED, org.apache.ambari.server.state.State.INSTALLING, org.apache.ambari.server.state.ServiceComponentHostEventType.HOST_SVCCOMP_INSTALL, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl.ServiceComponentHostOpStartedTransition()).addTransition(org.apache.ambari.server.state.State.INSTALL_FAILED, org.apache.ambari.server.state.State.INSTALL_FAILED, org.apache.ambari.server.state.ServiceComponentHostEventType.HOST_SVCCOMP_OP_FAILED, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl.ServiceComponentHostOpCompletedTransition()).addTransition(org.apache.ambari.server.state.State.INSTALL_FAILED, org.apache.ambari.server.state.State.INSTALL_FAILED, org.apache.ambari.server.state.ServiceComponentHostEventType.HOST_SVCCOMP_OP_IN_PROGRESS, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl.ServiceComponentHostOpInProgressTransition()).addTransition(org.apache.ambari.server.state.State.INSTALLED, org.apache.ambari.server.state.State.INSTALLED, org.apache.ambari.server.state.ServiceComponentHostEventType.HOST_SVCCOMP_OP_SUCCEEDED, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl.ServiceComponentHostOpCompletedTransition()).addTransition(org.apache.ambari.server.state.State.INSTALLED, org.apache.ambari.server.state.State.UNINSTALLING, org.apache.ambari.server.state.ServiceComponentHostEventType.HOST_SVCCOMP_UNINSTALL, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl.ServiceComponentHostOpStartedTransition()).addTransition(org.apache.ambari.server.state.State.INSTALLED, org.apache.ambari.server.state.State.INSTALLING, org.apache.ambari.server.state.ServiceComponentHostEventType.HOST_SVCCOMP_INSTALL, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl.ServiceComponentHostOpStartedTransition()).addTransition(org.apache.ambari.server.state.State.INSTALLED, org.apache.ambari.server.state.State.UPGRADING, org.apache.ambari.server.state.ServiceComponentHostEventType.HOST_SVCCOMP_UPGRADE, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl.ServiceComponentHostOpStartedTransition()).addTransition(org.apache.ambari.server.state.State.UPGRADING, org.apache.ambari.server.state.State.UPGRADING, org.apache.ambari.server.state.ServiceComponentHostEventType.HOST_SVCCOMP_OP_IN_PROGRESS, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl.ServiceComponentHostOpInProgressTransition()).addTransition(org.apache.ambari.server.state.State.UPGRADING, org.apache.ambari.server.state.State.INSTALLED, org.apache.ambari.server.state.ServiceComponentHostEventType.HOST_SVCCOMP_OP_SUCCEEDED, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl.ServiceComponentHostOpCompletedTransition()).addTransition(org.apache.ambari.server.state.State.UPGRADING, org.apache.ambari.server.state.State.UPGRADING, org.apache.ambari.server.state.ServiceComponentHostEventType.HOST_SVCCOMP_OP_FAILED, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl.ServiceComponentHostOpCompletedTransition()).addTransition(org.apache.ambari.server.state.State.UPGRADING, org.apache.ambari.server.state.State.UPGRADING, org.apache.ambari.server.state.ServiceComponentHostEventType.HOST_SVCCOMP_UPGRADE, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl.ServiceComponentHostOpStartedTransition()).addTransition(org.apache.ambari.server.state.State.UPGRADING, org.apache.ambari.server.state.State.UPGRADING, org.apache.ambari.server.state.ServiceComponentHostEventType.HOST_SVCCOMP_UPGRADE, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl.ServiceComponentHostOpInProgressTransition()).addTransition(org.apache.ambari.server.state.State.UNINSTALLING, org.apache.ambari.server.state.State.UNINSTALLING, org.apache.ambari.server.state.ServiceComponentHostEventType.HOST_SVCCOMP_OP_IN_PROGRESS, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl.ServiceComponentHostOpInProgressTransition()).addTransition(org.apache.ambari.server.state.State.UNINSTALLING, org.apache.ambari.server.state.State.UNINSTALLED, org.apache.ambari.server.state.ServiceComponentHostEventType.HOST_SVCCOMP_OP_SUCCEEDED, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl.ServiceComponentHostOpCompletedTransition()).addTransition(org.apache.ambari.server.state.State.UNINSTALLING, org.apache.ambari.server.state.State.UNINSTALLING, org.apache.ambari.server.state.ServiceComponentHostEventType.HOST_SVCCOMP_OP_FAILED, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl.ServiceComponentHostOpCompletedTransition()).addTransition(org.apache.ambari.server.state.State.UNINSTALLING, org.apache.ambari.server.state.State.UNINSTALLING, org.apache.ambari.server.state.ServiceComponentHostEventType.HOST_SVCCOMP_OP_RESTART, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl.ServiceComponentHostOpStartedTransition()).addTransition(org.apache.ambari.server.state.State.UNINSTALLING, org.apache.ambari.server.state.State.UNINSTALLING, org.apache.ambari.server.state.ServiceComponentHostEventType.HOST_SVCCOMP_UNINSTALL, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl.ServiceComponentHostOpStartedTransition()).addTransition(org.apache.ambari.server.state.State.UNINSTALLED, org.apache.ambari.server.state.State.INSTALLING, org.apache.ambari.server.state.ServiceComponentHostEventType.HOST_SVCCOMP_INSTALL, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl.ServiceComponentHostOpStartedTransition()).addTransition(org.apache.ambari.server.state.State.UNINSTALLED, org.apache.ambari.server.state.State.WIPING_OUT, org.apache.ambari.server.state.ServiceComponentHostEventType.HOST_SVCCOMP_WIPEOUT, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl.ServiceComponentHostOpStartedTransition()).addTransition(org.apache.ambari.server.state.State.WIPING_OUT, org.apache.ambari.server.state.State.WIPING_OUT, org.apache.ambari.server.state.ServiceComponentHostEventType.HOST_SVCCOMP_OP_IN_PROGRESS, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl.ServiceComponentHostOpInProgressTransition()).addTransition(org.apache.ambari.server.state.State.WIPING_OUT, org.apache.ambari.server.state.State.INIT, org.apache.ambari.server.state.ServiceComponentHostEventType.HOST_SVCCOMP_OP_SUCCEEDED, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl.ServiceComponentHostOpCompletedTransition()).addTransition(org.apache.ambari.server.state.State.WIPING_OUT, org.apache.ambari.server.state.State.WIPING_OUT, org.apache.ambari.server.state.ServiceComponentHostEventType.HOST_SVCCOMP_OP_FAILED, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl.ServiceComponentHostOpCompletedTransition()).addTransition(org.apache.ambari.server.state.State.WIPING_OUT, org.apache.ambari.server.state.State.WIPING_OUT, org.apache.ambari.server.state.ServiceComponentHostEventType.HOST_SVCCOMP_OP_RESTART, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl.ServiceComponentHostOpStartedTransition()).addTransition(org.apache.ambari.server.state.State.WIPING_OUT, org.apache.ambari.server.state.State.WIPING_OUT, org.apache.ambari.server.state.ServiceComponentHostEventType.HOST_SVCCOMP_WIPEOUT, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl.ServiceComponentHostOpStartedTransition()).installTopology();

    private final org.apache.ambari.server.state.fsm.StateMachine<org.apache.ambari.server.state.State, org.apache.ambari.server.state.ServiceComponentHostEventType, org.apache.ambari.server.state.ServiceComponentHostEvent> stateMachine;

    static class ServiceComponentHostOpCompletedTransition implements org.apache.ambari.server.state.fsm.SingleArcTransition<org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl, org.apache.ambari.server.state.ServiceComponentHostEvent> {
        @java.lang.Override
        public void transition(org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl impl, org.apache.ambari.server.state.ServiceComponentHostEvent event) {
            impl.updateLastOpInfo(event.getType(), event.getOpTimestamp());
        }
    }

    static class AlertDefinitionCommandTransition implements org.apache.ambari.server.state.fsm.SingleArcTransition<org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl, org.apache.ambari.server.state.ServiceComponentHostEvent> {
        @java.lang.Override
        public void transition(org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl impl, org.apache.ambari.server.state.ServiceComponentHostEvent event) {
            if (event.getType() != org.apache.ambari.server.state.ServiceComponentHostEventType.HOST_SVCCOMP_OP_SUCCEEDED) {
                return;
            }
            java.lang.String hostName = impl.getHostName();
            impl.alertDefinitionHash.invalidate(impl.getClusterName(), hostName);
            org.apache.ambari.server.events.AlertHashInvalidationEvent hashInvalidationEvent = new org.apache.ambari.server.events.AlertHashInvalidationEvent(impl.getClusterId(), java.util.Collections.singletonList(hostName));
            impl.eventPublisher.publish(hashInvalidationEvent);
            impl.updateLastOpInfo(event.getType(), event.getOpTimestamp());
        }
    }

    static class ServiceComponentHostOpStartedTransition implements org.apache.ambari.server.state.fsm.SingleArcTransition<org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl, org.apache.ambari.server.state.ServiceComponentHostEvent> {
        @java.lang.Override
        public void transition(org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl impl, org.apache.ambari.server.state.ServiceComponentHostEvent event) {
            impl.updateLastOpInfo(event.getType(), event.getOpTimestamp());
            if (event.getType() == org.apache.ambari.server.state.ServiceComponentHostEventType.HOST_SVCCOMP_INSTALL) {
                org.apache.ambari.server.state.svccomphost.ServiceComponentHostInstallEvent e = ((org.apache.ambari.server.state.svccomphost.ServiceComponentHostInstallEvent) (event));
                if (org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl.LOG.isDebugEnabled()) {
                    org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl.LOG.debug("Updating live stack version during INSTALL event, new stack version={}", e.getStackId());
                }
            }
        }
    }

    static class ServiceComponentHostOpInProgressTransition implements org.apache.ambari.server.state.fsm.SingleArcTransition<org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl, org.apache.ambari.server.state.ServiceComponentHostEvent> {
        @java.lang.Override
        public void transition(org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl impl, org.apache.ambari.server.state.ServiceComponentHostEvent event) {
            impl.updateLastOpInfo(event.getType(), event.getOpTimestamp());
        }
    }

    private void resetLastOpInfo() {
        setLastOpStartTime(-1);
        setLastOpLastUpdateTime(-1);
        setLastOpEndTime(-1);
    }

    private void updateLastOpInfo(org.apache.ambari.server.state.ServiceComponentHostEventType eventType, long time) {
        try {
            writeLock.lock();
            switch (eventType) {
                case HOST_SVCCOMP_INSTALL :
                case HOST_SVCCOMP_START :
                case HOST_SVCCOMP_STOP :
                case HOST_SVCCOMP_UNINSTALL :
                case HOST_SVCCOMP_WIPEOUT :
                case HOST_SVCCOMP_OP_RESTART :
                    resetLastOpInfo();
                    setLastOpStartTime(time);
                    break;
                case HOST_SVCCOMP_OP_FAILED :
                case HOST_SVCCOMP_OP_SUCCEEDED :
                case HOST_SVCCOMP_STOPPED :
                case HOST_SVCCOMP_STARTED :
                    setLastOpLastUpdateTime(time);
                    setLastOpEndTime(time);
                    break;
                case HOST_SVCCOMP_OP_IN_PROGRESS :
                    setLastOpLastUpdateTime(time);
                    break;
            }
        } finally {
            writeLock.unlock();
        }
    }

    @com.google.inject.assistedinject.AssistedInject
    public ServiceComponentHostImpl(@com.google.inject.assistedinject.Assisted
    org.apache.ambari.server.state.ServiceComponent serviceComponent, @com.google.inject.assistedinject.Assisted
    java.lang.String hostName, @com.google.inject.assistedinject.Assisted
    org.apache.ambari.server.orm.entities.ServiceComponentDesiredStateEntity serviceComponentDesiredStateEntity, org.apache.ambari.server.state.Clusters clusters, org.apache.ambari.server.orm.dao.StackDAO stackDAO, org.apache.ambari.server.orm.dao.HostDAO hostDAO, org.apache.ambari.server.orm.dao.ServiceComponentDesiredStateDAO serviceComponentDesiredStateDAO, org.apache.ambari.server.orm.dao.HostComponentStateDAO hostComponentStateDAO, org.apache.ambari.server.orm.dao.HostComponentDesiredStateDAO hostComponentDesiredStateDAO, org.apache.ambari.server.events.publishers.AmbariEventPublisher eventPublisher) {
        this.serviceComponent = serviceComponent;
        this.hostName = hostName;
        this.clusters = clusters;
        this.stackDAO = stackDAO;
        this.hostDAO = hostDAO;
        this.serviceComponentDesiredStateDAO = serviceComponentDesiredStateDAO;
        this.hostComponentStateDAO = hostComponentStateDAO;
        this.hostComponentDesiredStateDAO = hostComponentDesiredStateDAO;
        this.eventPublisher = eventPublisher;
        if (serviceComponent.isClientComponent()) {
            stateMachine = org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl.clientStateMachineFactory.make(this);
        } else {
            stateMachine = org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl.daemonStateMachineFactory.make(this);
        }
        org.apache.ambari.server.orm.entities.HostEntity hostEntity = null;
        try {
            host = clusters.getHost(hostName);
            hostEntity = hostDAO.findByName(hostName);
            if (hostEntity == null) {
                throw new org.apache.ambari.server.AmbariException("Could not find host " + hostName);
            }
        } catch (org.apache.ambari.server.AmbariException e) {
            org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl.LOG.error("Host '{}' was not found" + hostName);
            throw new java.lang.RuntimeException(e);
        }
        org.apache.ambari.server.state.StackId stackId = serviceComponent.getDesiredStackId();
        org.apache.ambari.server.orm.entities.StackEntity stackEntity = stackDAO.find(stackId.getStackName(), stackId.getStackVersion());
        org.apache.ambari.server.orm.entities.HostComponentStateEntity stateEntity = new org.apache.ambari.server.orm.entities.HostComponentStateEntity();
        stateEntity.setClusterId(serviceComponent.getClusterId());
        stateEntity.setComponentName(serviceComponent.getName());
        stateEntity.setServiceName(serviceComponent.getServiceName());
        stateEntity.setVersion(org.apache.ambari.server.state.State.UNKNOWN.toString());
        stateEntity.setHostEntity(hostEntity);
        stateEntity.setCurrentState(stateMachine.getCurrentState());
        stateEntity.setUpgradeState(org.apache.ambari.server.state.UpgradeState.NONE);
        org.apache.ambari.server.orm.entities.HostComponentDesiredStateEntity desiredStateEntity = new org.apache.ambari.server.orm.entities.HostComponentDesiredStateEntity();
        desiredStateEntity.setClusterId(serviceComponent.getClusterId());
        desiredStateEntity.setComponentName(serviceComponent.getName());
        desiredStateEntity.setServiceName(serviceComponent.getServiceName());
        desiredStateEntity.setHostEntity(hostEntity);
        desiredStateEntity.setDesiredState(org.apache.ambari.server.state.State.INIT);
        if ((!serviceComponent.isMasterComponent()) && (!serviceComponent.isClientComponent())) {
            desiredStateEntity.setAdminState(org.apache.ambari.server.state.HostComponentAdminState.INSERVICE);
        } else {
            desiredStateEntity.setAdminState(null);
        }
        persistEntities(hostEntity, stateEntity, desiredStateEntity, serviceComponentDesiredStateEntity);
        org.apache.ambari.server.events.ServiceComponentInstalledEvent event = new org.apache.ambari.server.events.ServiceComponentInstalledEvent(getClusterId(), stackId.getStackName(), stackId.getStackVersion(), getServiceName(), getServiceComponentName(), getHostName(), isRecoveryEnabled(), serviceComponent.isMasterComponent());
        eventPublisher.publish(event);
        desiredStateEntityId = desiredStateEntity.getId();
        hostComponentStateId = stateEntity.getId();
        resetLastOpInfo();
    }

    @com.google.inject.assistedinject.AssistedInject
    public ServiceComponentHostImpl(@com.google.inject.assistedinject.Assisted
    org.apache.ambari.server.state.ServiceComponent serviceComponent, @com.google.inject.assistedinject.Assisted
    java.lang.String hostName, org.apache.ambari.server.state.Clusters clusters, org.apache.ambari.server.orm.dao.StackDAO stackDAO, org.apache.ambari.server.orm.dao.HostDAO hostDAO, org.apache.ambari.server.orm.dao.ServiceComponentDesiredStateDAO serviceComponentDesiredStateDAO, org.apache.ambari.server.orm.dao.HostComponentStateDAO hostComponentStateDAO, org.apache.ambari.server.orm.dao.HostComponentDesiredStateDAO hostComponentDesiredStateDAO, org.apache.ambari.server.events.publishers.AmbariEventPublisher eventPublisher) {
        this(serviceComponent, hostName, null, clusters, stackDAO, hostDAO, serviceComponentDesiredStateDAO, hostComponentStateDAO, hostComponentDesiredStateDAO, eventPublisher);
    }

    @com.google.inject.assistedinject.AssistedInject
    public ServiceComponentHostImpl(@com.google.inject.assistedinject.Assisted
    org.apache.ambari.server.state.ServiceComponent serviceComponent, @com.google.inject.assistedinject.Assisted
    org.apache.ambari.server.orm.entities.HostComponentStateEntity stateEntity, @com.google.inject.assistedinject.Assisted
    org.apache.ambari.server.orm.entities.HostComponentDesiredStateEntity desiredStateEntity, org.apache.ambari.server.state.Clusters clusters, org.apache.ambari.server.orm.dao.StackDAO stackDAO, org.apache.ambari.server.orm.dao.HostDAO hostDAO, org.apache.ambari.server.orm.dao.ServiceComponentDesiredStateDAO serviceComponentDesiredStateDAO, org.apache.ambari.server.orm.dao.HostComponentStateDAO hostComponentStateDAO, org.apache.ambari.server.orm.dao.HostComponentDesiredStateDAO hostComponentDesiredStateDAO, org.apache.ambari.server.events.publishers.AmbariEventPublisher eventPublisher) {
        hostName = stateEntity.getHostName();
        this.serviceComponent = serviceComponent;
        this.clusters = clusters;
        this.stackDAO = stackDAO;
        this.hostDAO = hostDAO;
        this.serviceComponentDesiredStateDAO = serviceComponentDesiredStateDAO;
        this.hostComponentStateDAO = hostComponentStateDAO;
        this.hostComponentDesiredStateDAO = hostComponentDesiredStateDAO;
        this.eventPublisher = eventPublisher;
        desiredStateEntityId = desiredStateEntity.getId();
        hostComponentStateId = stateEntity.getId();
        if (serviceComponent.isClientComponent()) {
            stateMachine = org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl.clientStateMachineFactory.make(this);
        } else {
            stateMachine = org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl.daemonStateMachineFactory.make(this);
        }
        stateMachine.setCurrentState(stateEntity.getCurrentState());
        try {
            host = clusters.getHost(stateEntity.getHostName());
        } catch (org.apache.ambari.server.AmbariException e) {
            org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl.LOG.error("Host '{}' was not found " + stateEntity.getHostName());
            throw new java.lang.RuntimeException(e);
        }
    }

    @java.lang.Override
    public org.apache.ambari.server.state.State getState() {
        return stateMachine.getCurrentState();
    }

    @java.lang.Override
    @com.google.inject.persist.Transactional
    public void setState(org.apache.ambari.server.state.State state) {
        org.apache.ambari.server.state.State oldState = getState();
        stateMachine.setCurrentState(state);
        org.apache.ambari.server.orm.entities.HostComponentStateEntity stateEntity = getStateEntity();
        if (stateEntity != null) {
            stateEntity.setCurrentState(state);
            stateEntity = hostComponentStateDAO.merge(stateEntity);
            if (!oldState.equals(state)) {
                STOMPUpdatePublisher.publish(new org.apache.ambari.server.events.HostComponentsUpdateEvent(java.util.Collections.singletonList(org.apache.ambari.server.events.HostComponentUpdate.createHostComponentStatusUpdate(stateEntity, oldState))));
            }
        } else {
            org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl.LOG.warn(((((((("Setting a member on an entity object that may have been " + "previously deleted, serviceName = ") + getServiceName()) + ", ") + "componentName = ") + getServiceComponentName()) + ", ") + "hostName = ") + getHostName());
        }
    }

    @java.lang.Override
    public java.lang.String getVersion() {
        org.apache.ambari.server.orm.entities.HostComponentStateEntity stateEntity = getStateEntity();
        if (stateEntity != null) {
            return stateEntity.getVersion();
        } else {
            org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl.LOG.warn(((((((("Trying to fetch a member from an entity object that may " + "have been previously deleted, serviceName = ") + getServiceName()) + ", ") + "componentName = ") + getServiceComponentName()) + ", ") + "hostName = ") + getHostName());
        }
        return null;
    }

    @java.lang.Override
    @com.google.inject.persist.Transactional
    public void setVersion(java.lang.String version) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.orm.entities.HostComponentStateEntity stateEntity = getStateEntity();
        if (stateEntity != null) {
            stateEntity.setVersion(version);
            stateEntity = hostComponentStateDAO.merge(stateEntity);
            org.apache.ambari.server.controller.ServiceComponentHostRequest serviceComponentHostRequest = new org.apache.ambari.server.controller.ServiceComponentHostRequest(serviceComponent.getClusterName(), serviceComponent.getServiceName(), serviceComponent.getName(), hostName, getDesiredState().name());
            org.apache.ambari.server.events.TopologyUpdateEvent updateEvent = controller.get().getAddedComponentsTopologyEvent(java.util.Collections.singleton(serviceComponentHostRequest));
            m_topologyHolder.get().updateData(updateEvent);
        } else {
            org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl.LOG.warn(((((((("Setting a member on an entity object that may have been " + "previously deleted, serviceName = ") + getServiceName()) + ", ") + "componentName = ") + getServiceComponentName()) + ", ") + "hostName = ") + getHostName());
        }
    }

    @java.lang.Override
    @com.google.inject.persist.Transactional
    public void setUpgradeState(org.apache.ambari.server.state.UpgradeState upgradeState) {
        org.apache.ambari.server.orm.entities.HostComponentStateEntity stateEntity = getStateEntity();
        if (stateEntity != null) {
            stateEntity.setUpgradeState(upgradeState);
            stateEntity = hostComponentStateDAO.merge(stateEntity);
        } else {
            org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl.LOG.warn(((((((("Setting a member on an entity object that may have been " + "previously deleted, serviceName = ") + getServiceName()) + ", ") + "componentName = ") + getServiceComponentName()) + ", ") + "hostName = ") + getHostName());
        }
    }

    @java.lang.Override
    public org.apache.ambari.server.state.UpgradeState getUpgradeState() {
        org.apache.ambari.server.orm.entities.HostComponentStateEntity stateEntity = getStateEntity();
        if (stateEntity != null) {
            return stateEntity.getUpgradeState();
        } else {
            org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl.LOG.warn(((((((("Trying to fetch a state entity from an object that may " + "have been previously deleted, serviceName = ") + getServiceName()) + ", ") + "componentName = ") + getServiceComponentName()) + ", ") + "hostName = ") + getHostName());
        }
        return org.apache.ambari.server.state.UpgradeState.NONE;
    }

    @java.lang.Override
    @com.google.inject.persist.Transactional
    public void handleEvent(org.apache.ambari.server.state.ServiceComponentHostEvent event) throws org.apache.ambari.server.state.fsm.InvalidStateTransitionException {
        if (org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl.LOG.isDebugEnabled()) {
            org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl.LOG.debug("Handling ServiceComponentHostEvent event, eventType={}, event={}", event.getType().name(), event);
        }
        org.apache.ambari.server.state.State oldState = getState();
        try {
            writeLock.lock();
            try {
                stateMachine.doTransition(event.getType(), event);
                org.apache.ambari.server.orm.entities.HostComponentStateEntity stateEntity = getStateEntity();
                boolean statusUpdated = !stateEntity.getCurrentState().equals(stateMachine.getCurrentState());
                stateEntity.setCurrentState(stateMachine.getCurrentState());
                stateEntity = hostComponentStateDAO.merge(stateEntity);
                if (statusUpdated) {
                    STOMPUpdatePublisher.publish(new org.apache.ambari.server.events.HostComponentsUpdateEvent(java.util.Collections.singletonList(org.apache.ambari.server.events.HostComponentUpdate.createHostComponentStatusUpdate(stateEntity, oldState))));
                }
                if (event.getType().equals(org.apache.ambari.server.state.ServiceComponentHostEventType.HOST_SVCCOMP_STARTED)) {
                    org.apache.ambari.server.orm.entities.HostComponentDesiredStateEntity desiredStateEntity = getDesiredStateEntity();
                    if (desiredStateEntity.getBlueprintProvisioningState() == org.apache.ambari.server.state.BlueprintProvisioningState.IN_PROGRESS) {
                        desiredStateEntity.setBlueprintProvisioningState(org.apache.ambari.server.state.BlueprintProvisioningState.FINISHED);
                        hostComponentDesiredStateDAO.merge(desiredStateEntity);
                        m_hostLevelParamsHolder.get().updateData(m_hostLevelParamsHolder.get().getCurrentData(getHost().getHostId()));
                    }
                }
            } catch (org.apache.ambari.server.state.fsm.InvalidStateTransitionException e) {
                org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl.LOG.error(((((((((("Can't handle ServiceComponentHostEvent event at" + (" current state" + ", serviceComponentName=")) + getServiceComponentName()) + ", hostName=") + getHostName()) + ", currentState=") + oldState) + ", eventType=") + event.getType()) + ", event=") + event);
                throw e;
            } catch (org.apache.ambari.server.AmbariException e) {
                org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl.LOG.error(((((((((("Can't update topology on hosts on ServiceComponentHostEvent event: " + "serviceComponentName=") + getServiceComponentName()) + ", hostName=") + getHostName()) + ", currentState=") + oldState) + ", eventType=") + event.getType()) + ", event=") + event);
            }
        } finally {
            writeLock.unlock();
        }
        if (!oldState.equals(getState())) {
            org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl.LOG.info(((((((("Host role transitioned to a new state" + ", serviceComponentName=") + getServiceComponentName()) + ", hostName=") + getHostName()) + ", oldState=") + oldState) + ", currentState=") + getState());
            if (org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl.LOG.isDebugEnabled()) {
                org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl.LOG.debug("ServiceComponentHost transitioned to a new state, serviceComponentName={}, hostName={}, oldState={}, currentState={}, eventType={}, event={}", getServiceComponentName(), getHostName(), oldState, getState(), event.getType().name(), event);
            }
        }
    }

    @java.lang.Override
    public java.lang.String getServiceComponentName() {
        return serviceComponent.getName();
    }

    @java.lang.Override
    public java.lang.String getHostName() {
        return host.getHostName();
    }

    @java.lang.Override
    public java.lang.String getPublicHostName() {
        return host.getPublicHostName();
    }

    @java.lang.Override
    public org.apache.ambari.server.state.Host getHost() {
        return host;
    }

    public long getLastOpStartTime() {
        return lastOpStartTime;
    }

    public void setLastOpStartTime(long lastOpStartTime) {
        this.lastOpStartTime = lastOpStartTime;
    }

    public long getLastOpEndTime() {
        return lastOpEndTime;
    }

    public void setLastOpEndTime(long lastOpEndTime) {
        this.lastOpEndTime = lastOpEndTime;
    }

    public long getLastOpLastUpdateTime() {
        return lastOpLastUpdateTime;
    }

    public void setLastOpLastUpdateTime(long lastOpLastUpdateTime) {
        this.lastOpLastUpdateTime = lastOpLastUpdateTime;
    }

    @java.lang.Override
    public long getClusterId() {
        return serviceComponent.getClusterId();
    }

    @java.lang.Override
    public java.lang.String getServiceName() {
        return serviceComponent.getServiceName();
    }

    @java.lang.Override
    public boolean isClientComponent() {
        return serviceComponent.isClientComponent();
    }

    @java.lang.Override
    public org.apache.ambari.server.state.State getDesiredState() {
        org.apache.ambari.server.orm.entities.HostComponentDesiredStateEntity desiredStateEntity = getDesiredStateEntity();
        if (desiredStateEntity != null) {
            return desiredStateEntity.getDesiredState();
        } else {
            org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl.LOG.warn(((((((("Trying to fetch a member from an entity object that may " + "have been previously deleted, serviceName = ") + getServiceName()) + ", ") + "componentName = ") + getServiceComponentName()) + ", ") + "hostName = ") + getHostName());
        }
        return null;
    }

    @java.lang.Override
    public void setDesiredState(org.apache.ambari.server.state.State state) {
        org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl.LOG.debug("Set DesiredState on serviceName = {} componentName = {} hostName = {} to {} ", getServiceName(), getServiceComponentName(), getHostName(), state);
        org.apache.ambari.server.orm.entities.HostComponentDesiredStateEntity desiredStateEntity = getDesiredStateEntity();
        if (desiredStateEntity != null) {
            desiredStateEntity.setDesiredState(state);
            hostComponentDesiredStateDAO.merge(desiredStateEntity);
        } else {
            org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl.LOG.warn((((((("Setting a member on an entity object that may have been " + "previously deleted, serviceName = ") + getServiceName()) + ", ") + "componentName = ") + getServiceComponentName()) + "hostName = ") + getHostName());
        }
    }

    @java.lang.Override
    public org.apache.ambari.server.state.HostComponentAdminState getComponentAdminState() {
        org.apache.ambari.server.orm.entities.HostComponentDesiredStateEntity desiredStateEntity = getDesiredStateEntity();
        return getComponentAdminStateFromDesiredStateEntity(desiredStateEntity);
    }

    private org.apache.ambari.server.state.HostComponentAdminState getComponentAdminStateFromDesiredStateEntity(org.apache.ambari.server.orm.entities.HostComponentDesiredStateEntity desiredStateEntity) {
        if (desiredStateEntity != null) {
            org.apache.ambari.server.state.HostComponentAdminState adminState = desiredStateEntity.getAdminState();
            if (((adminState == null) && (!serviceComponent.isClientComponent())) && (!serviceComponent.isMasterComponent())) {
                adminState = org.apache.ambari.server.state.HostComponentAdminState.INSERVICE;
            }
            return adminState;
        }
        return null;
    }

    @java.lang.Override
    public void setComponentAdminState(org.apache.ambari.server.state.HostComponentAdminState attribute) {
        org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl.LOG.debug("Set ComponentAdminState on serviceName = {} componentName = {} hostName = {} to {}", getServiceName(), getServiceComponentName(), getHostName(), attribute);
        org.apache.ambari.server.orm.entities.HostComponentDesiredStateEntity desiredStateEntity = getDesiredStateEntity();
        if (desiredStateEntity != null) {
            desiredStateEntity.setAdminState(attribute);
            hostComponentDesiredStateDAO.merge(desiredStateEntity);
        } else {
            org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl.LOG.warn((((((("Setting a member on an entity object that may have been " + "previously deleted, serviceName = ") + getServiceName()) + ", ") + "componentName = ") + getServiceComponentName()) + "hostName = ") + getHostName());
        }
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.ServiceComponentHostResponse convertToResponse(java.util.Map<java.lang.String, org.apache.ambari.server.state.DesiredConfig> desiredConfigs) {
        org.apache.ambari.server.orm.entities.HostComponentStateEntity hostComponentStateEntity = getStateEntity();
        org.apache.ambari.server.orm.entities.HostEntity hostEntity = hostComponentStateEntity.getHostEntity();
        org.apache.ambari.server.orm.entities.HostComponentDesiredStateEntity hostComponentDesiredStateEntity = getDesiredStateEntity();
        java.lang.String clusterName = serviceComponent.getClusterName();
        java.lang.String serviceName = serviceComponent.getServiceName();
        java.lang.String serviceComponentName = serviceComponent.getName();
        java.lang.String hostName = getHostName();
        java.lang.String publicHostName = hostEntity.getPublicHostName();
        java.lang.String state = getState().toString();
        java.lang.String desiredState = (hostComponentDesiredStateEntity == null) ? null : hostComponentDesiredStateEntity.getDesiredState().toString();
        java.lang.String desiredStackId = serviceComponent.getDesiredStackId().getStackId();
        org.apache.ambari.server.state.HostComponentAdminState componentAdminState = getComponentAdminStateFromDesiredStateEntity(hostComponentDesiredStateEntity);
        org.apache.ambari.server.state.UpgradeState upgradeState = hostComponentStateEntity.getUpgradeState();
        java.lang.String displayName = null;
        try {
            org.apache.ambari.server.state.StackId stackVersion = serviceComponent.getDesiredStackId();
            org.apache.ambari.server.state.ComponentInfo compInfo = ambariMetaInfo.getComponent(stackVersion.getStackName(), stackVersion.getStackVersion(), serviceName, serviceComponentName);
            displayName = compInfo.getDisplayName();
        } catch (org.apache.ambari.server.AmbariException e) {
            displayName = serviceComponentName;
        }
        java.lang.String desiredRepositoryVersion = null;
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion = serviceComponent.getDesiredRepositoryVersion();
        if (null != repositoryVersion) {
            desiredRepositoryVersion = repositoryVersion.getVersion();
        }
        org.apache.ambari.server.controller.ServiceComponentHostResponse r = new org.apache.ambari.server.controller.ServiceComponentHostResponse(clusterName, serviceName, serviceComponentName, displayName, hostName, publicHostName, state, getVersion(), desiredState, desiredStackId, desiredRepositoryVersion, componentAdminState);
        r.setActualConfigs(actualConfigs);
        r.setUpgradeState(upgradeState);
        try {
            r.setStaleConfig(helper.isStaleConfigs(this, desiredConfigs, hostComponentDesiredStateEntity));
        } catch (java.lang.Exception e) {
            org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl.LOG.error("Could not determine stale config", e);
        }
        try {
            org.apache.ambari.server.state.Cluster cluster = clusters.getCluster(clusterName);
            org.apache.ambari.server.state.ServiceComponent serviceComponent = cluster.getService(serviceName).getServiceComponent(serviceComponentName);
            org.apache.ambari.server.state.ServiceComponentHost sch = serviceComponent.getServiceComponentHost(hostName);
            java.lang.String refreshConfigsCommand = helper.getRefreshConfigsCommand(cluster, sch);
            r.setReloadConfig(refreshConfigsCommand != null);
        } catch (java.lang.Exception e) {
            org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl.LOG.error("Could not determine reload config flag", e);
        }
        return r;
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.ServiceComponentHostResponse convertToResponseStatusOnly(java.util.Map<java.lang.String, org.apache.ambari.server.state.DesiredConfig> desiredConfigs, boolean collectStaleConfigsStatus) {
        java.lang.String clusterName = serviceComponent.getClusterName();
        java.lang.String serviceName = serviceComponent.getServiceName();
        java.lang.String serviceComponentName = serviceComponent.getName();
        java.lang.String state = getState().toString();
        org.apache.ambari.server.controller.ServiceComponentHostResponse r = new org.apache.ambari.server.controller.ServiceComponentHostResponse(clusterName, serviceName, serviceComponentName, null, hostName, null, state, null, null, null, null, null);
        if (collectStaleConfigsStatus) {
            try {
                org.apache.ambari.server.orm.entities.HostComponentDesiredStateEntity hostComponentDesiredStateEntity = getDesiredStateEntity();
                r.setStaleConfig(helper.isStaleConfigs(this, desiredConfigs, hostComponentDesiredStateEntity));
            } catch (java.lang.Exception e) {
                org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl.LOG.error("Could not determine stale config", e);
            }
        } else {
            r.setStaleConfig(false);
        }
        return r;
    }

    @java.lang.Override
    public java.lang.String getClusterName() {
        return serviceComponent.getClusterName();
    }

    @java.lang.Override
    public void debugDump(java.lang.StringBuilder sb) {
        sb.append("ServiceComponentHost={ hostname=").append(getHostName()).append(", serviceComponentName=").append(serviceComponent.getName()).append(", clusterName=").append(serviceComponent.getClusterName()).append(", serviceName=").append(serviceComponent.getServiceName()).append(", desiredStackVersion=").append(serviceComponent.getDesiredStackId()).append(", desiredState=").append(getDesiredState()).append(", version=").append(getVersion()).append(", state=").append(getState()).append(" }");
    }

    @com.google.inject.persist.Transactional
    void persistEntities(org.apache.ambari.server.orm.entities.HostEntity hostEntity, org.apache.ambari.server.orm.entities.HostComponentStateEntity stateEntity, org.apache.ambari.server.orm.entities.HostComponentDesiredStateEntity desiredStateEntity, org.apache.ambari.server.orm.entities.ServiceComponentDesiredStateEntity serviceComponentDesiredStateEntity) {
        if (serviceComponentDesiredStateEntity == null) {
            serviceComponentDesiredStateEntity = serviceComponentDesiredStateDAO.findByName(serviceComponent.getClusterId(), serviceComponent.getServiceName(), serviceComponent.getName());
        }
        desiredStateEntity.setServiceComponentDesiredStateEntity(serviceComponentDesiredStateEntity);
        desiredStateEntity.setHostEntity(hostEntity);
        desiredStateEntity.setHostId(hostEntity.getHostId());
        stateEntity.setServiceComponentDesiredStateEntity(serviceComponentDesiredStateEntity);
        stateEntity.setHostEntity(hostEntity);
        hostComponentStateDAO.create(stateEntity);
        hostComponentDesiredStateDAO.create(desiredStateEntity);
        serviceComponentDesiredStateEntity.getHostComponentDesiredStateEntities().add(desiredStateEntity);
        serviceComponentDesiredStateEntity = serviceComponentDesiredStateDAO.merge(serviceComponentDesiredStateEntity);
        hostEntity.addHostComponentStateEntity(stateEntity);
        hostEntity.addHostComponentDesiredStateEntity(desiredStateEntity);
        hostEntity = hostDAO.merge(hostEntity);
    }

    @java.lang.Override
    public boolean canBeRemoved() {
        return getState().isRemovableState();
    }

    @java.lang.Override
    public void delete(org.apache.ambari.server.controller.internal.DeleteHostComponentStatusMetaData deleteMetaData) {
        boolean fireRemovalEvent = false;
        writeLock.lock();
        java.lang.String version = getVersion();
        try {
            removeEntities();
            fireRemovalEvent = true;
            clusters.getCluster(getClusterName()).removeServiceComponentHost(this);
        } catch (org.apache.ambari.server.AmbariException ex) {
            org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl.LOG.error("Unable to remove a service component from a host", ex);
        } finally {
            writeLock.unlock();
        }
        if (fireRemovalEvent) {
            long clusterId = getClusterId();
            org.apache.ambari.server.state.StackId stackId = serviceComponent.getDesiredStackId();
            java.lang.String stackVersion = stackId.getStackVersion();
            java.lang.String stackName = stackId.getStackName();
            java.lang.String serviceName = getServiceName();
            java.lang.String componentName = getServiceComponentName();
            java.lang.String hostName = getHostName();
            org.apache.ambari.server.state.State lastComponentState = getState();
            boolean recoveryEnabled = isRecoveryEnabled();
            boolean masterComponent = serviceComponent.isMasterComponent();
            org.apache.ambari.server.events.ServiceComponentUninstalledEvent event = new org.apache.ambari.server.events.ServiceComponentUninstalledEvent(clusterId, stackName, stackVersion, serviceName, componentName, hostName, recoveryEnabled, masterComponent, host.getHostId());
            eventPublisher.publish(event);
            deleteMetaData.addDeletedHostComponent(componentName, serviceName, hostName, getHost().getHostId(), java.lang.Long.toString(clusterId), version, lastComponentState);
        }
    }

    @com.google.inject.persist.Transactional
    protected void removeEntities() {
        org.apache.ambari.server.orm.entities.HostComponentStateEntity stateEntity = getStateEntity();
        if (stateEntity != null) {
            org.apache.ambari.server.orm.entities.HostEntity hostEntity = stateEntity.getHostEntity();
            org.apache.ambari.server.orm.entities.HostComponentDesiredStateEntity desiredStateEntity = getDesiredStateEntity();
            hostEntity.removeHostComponentStateEntity(stateEntity);
            hostEntity.removeHostComponentDesiredStateEntity(desiredStateEntity);
            hostDAO.merge(hostEntity);
            hostComponentDesiredStateDAO.remove(desiredStateEntity);
            hostComponentStateDAO.remove(stateEntity);
        }
    }

    @java.lang.Override
    public java.util.Map<java.lang.String, org.apache.ambari.server.state.HostConfig> getActualConfigs() {
        return actualConfigs;
    }

    @java.lang.Override
    public org.apache.ambari.server.state.HostState getHostState() {
        return host.getState();
    }

    @java.lang.Override
    public boolean isRecoveryEnabled() {
        return serviceComponent.isRecoveryEnabled();
    }

    @java.lang.Override
    public void setMaintenanceState(org.apache.ambari.server.state.MaintenanceState state) {
        org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl.LOG.debug("Set MaintenanceState on serviceName = {} componentName = {} hostName = {} to {}", getServiceName(), getServiceComponentName(), getHostName(), state);
        org.apache.ambari.server.orm.entities.HostComponentDesiredStateEntity desiredStateEntity = getDesiredStateEntity();
        if (desiredStateEntity != null) {
            desiredStateEntity.setMaintenanceState(state);
            maintenanceState.set(hostComponentDesiredStateDAO.merge(desiredStateEntity).getMaintenanceState());
            org.apache.ambari.server.events.MaintenanceModeEvent event = new org.apache.ambari.server.events.MaintenanceModeEvent(state, this);
            eventPublisher.publish(event);
        } else {
            org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl.LOG.warn((((((("Setting a member on an entity object that may have been " + "previously deleted, serviceName = ") + getServiceName()) + ", ") + "componentName = ") + getServiceComponentName()) + ", hostName = ") + getHostName());
        }
    }

    @java.lang.Override
    public org.apache.ambari.server.state.MaintenanceState getMaintenanceState() {
        if (maintenanceState.get() == null) {
            maintenanceState.set(getDesiredStateEntity().getMaintenanceState());
        }
        return maintenanceState.get();
    }

    @java.lang.Override
    public void setProcesses(java.util.List<java.util.Map<java.lang.String, java.lang.String>> procs) {
        processes = com.google.common.collect.ImmutableList.copyOf(procs);
    }

    @java.lang.Override
    public java.util.List<java.util.Map<java.lang.String, java.lang.String>> getProcesses() {
        return processes;
    }

    @java.lang.Override
    public boolean isRestartRequired() {
        return getDesiredStateEntity().isRestartRequired();
    }

    @java.lang.Override
    public boolean isRestartRequired(org.apache.ambari.server.orm.entities.HostComponentDesiredStateEntity hostComponentDesiredStateEntity) {
        return hostComponentDesiredStateEntity.isRestartRequired();
    }

    @java.lang.Override
    @com.google.inject.persist.Transactional
    public void setRestartRequired(boolean restartRequired) {
        if (setRestartRequiredWithoutEventPublishing(restartRequired)) {
            eventPublisher.publish(new org.apache.ambari.server.events.StaleConfigsUpdateEvent(this, restartRequired));
        }
    }

    @java.lang.Override
    @com.google.inject.persist.Transactional
    public boolean setRestartRequiredWithoutEventPublishing(boolean restartRequired) {
        org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl.LOG.debug("Set RestartRequired on serviceName = {} componentName = {} hostName = {} to {}", getServiceName(), getServiceComponentName(), getHostName(), restartRequired);
        org.apache.ambari.server.orm.entities.HostComponentDesiredStateEntity desiredStateEntity = getDesiredStateEntity();
        if (desiredStateEntity != null) {
            desiredStateEntity.setRestartRequired(restartRequired);
            hostComponentDesiredStateDAO.merge(desiredStateEntity);
            return true;
        } else {
            org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl.LOG.warn((((((("Setting a member on an entity object that may have been " + "previously deleted, serviceName = ") + getServiceName()) + ", ") + "componentName = ") + getServiceComponentName()) + ", hostName = ") + getHostName());
        }
        return false;
    }

    @com.google.inject.persist.Transactional
    org.apache.ambari.server.orm.entities.RepositoryVersionEntity createRepositoryVersion(java.lang.String version, final org.apache.ambari.server.state.StackId stackId, final org.apache.ambari.server.state.StackInfo stackInfo) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl.LOG.info((("Creating new repository version " + stackId.getStackName()) + "-") + version);
        org.apache.ambari.server.orm.entities.StackEntity stackEntity = stackDAO.find(stackId.getStackName(), stackId.getStackVersion());
        if (null == version) {
            throw new org.apache.ambari.server.AmbariException(java.text.MessageFormat.format("Cannot create Repository Version for Stack {0}-{1} if the version is empty", stackId.getStackName(), stackId.getStackVersion()));
        }
        return repositoryVersionDAO.create(stackEntity, version, (stackId.getStackName() + "-") + version, repositoryVersionHelper.createRepoOsEntities(stackInfo.getRepositories()));
    }

    @java.lang.Override
    @com.google.inject.persist.Transactional
    public org.apache.ambari.server.orm.entities.HostVersionEntity recalculateHostVersionState() throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.orm.entities.HostEntity hostEntity = host.getHostEntity();
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion = serviceComponent.getDesiredRepositoryVersion();
        org.apache.ambari.server.orm.entities.HostVersionEntity hostVersionEntity = hostVersionDAO.findHostVersionByHostAndRepository(hostEntity, repositoryVersion);
        java.util.concurrent.locks.Lock lock = org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl.HOST_VERSION_LOCK.get(host.getHostName());
        lock.lock();
        try {
            if (hostVersionEntity == null) {
                hostVersionEntity = new org.apache.ambari.server.orm.entities.HostVersionEntity(hostEntity, repositoryVersion, org.apache.ambari.server.state.RepositoryVersionState.INSTALLING);
                org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl.LOG.info("Creating host version for {}, state={}, repo={} (repo_id={})", hostVersionEntity.getHostName(), hostVersionEntity.getState(), hostVersionEntity.getRepositoryVersion().getVersion(), hostVersionEntity.getRepositoryVersion().getId());
                hostVersionDAO.create(hostVersionEntity);
            }
            if (hostVersionEntity.getState() != org.apache.ambari.server.state.RepositoryVersionState.CURRENT) {
                if (host.isRepositoryVersionCorrect(repositoryVersion)) {
                    hostVersionEntity.setState(org.apache.ambari.server.state.RepositoryVersionState.CURRENT);
                    hostVersionEntity = hostVersionDAO.merge(hostVersionEntity);
                }
            }
        } finally {
            lock.unlock();
        }
        return hostVersionEntity;
    }

    @java.lang.Override
    public org.apache.ambari.server.orm.entities.HostComponentDesiredStateEntity getDesiredStateEntity() {
        return hostComponentDesiredStateDAO.findById(desiredStateEntityId);
    }

    private org.apache.ambari.server.orm.entities.HostComponentStateEntity getStateEntity() {
        return hostComponentStateDAO.findById(hostComponentStateId);
    }

    @java.lang.Override
    public org.apache.ambari.server.state.ServiceComponent getServiceComponent() {
        return serviceComponent;
    }

    @java.lang.Override
    public org.apache.ambari.server.state.StackId getDesiredStackId() {
        return serviceComponent.getDesiredStackId();
    }
}