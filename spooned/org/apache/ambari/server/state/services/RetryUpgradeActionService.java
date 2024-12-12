package org.apache.ambari.server.state.services;
import com.google.inject.persist.Transactional;
@org.apache.ambari.server.AmbariService
public class RetryUpgradeActionService extends com.google.common.util.concurrent.AbstractScheduledService {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.state.services.RetryUpgradeActionService.class);

    @com.google.inject.Inject
    private com.google.inject.Injector m_injector;

    @com.google.inject.Inject
    private com.google.inject.Provider<org.apache.ambari.server.state.Clusters> m_clustersProvider;

    @com.google.inject.Inject
    private org.apache.ambari.server.configuration.Configuration m_configuration;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.HostRoleCommandDAO m_hostRoleCommandDAO;

    private final java.util.List<org.apache.ambari.server.actionmanager.HostRoleStatus> HOLDING_STATUSES = java.util.Arrays.asList(org.apache.ambari.server.actionmanager.HostRoleStatus.HOLDING_FAILED, org.apache.ambari.server.actionmanager.HostRoleStatus.HOLDING_TIMEDOUT);

    private java.util.List<java.lang.String> CUSTOM_COMMAND_NAMES_TO_IGNORE;

    private java.util.List<java.lang.String> COMMAND_DETAILS_TO_IGNORE;

    private int MAX_TIMEOUT_MINS;

    private java.lang.Long MAX_TIMEOUT_MS;

    private java.text.DateFormat m_fullDateFormat;

    private java.text.SimpleDateFormat m_deltaDateFormat;

    public RetryUpgradeActionService() {
        m_fullDateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        java.util.TimeZone tz = java.util.TimeZone.getTimeZone("UTC");
        m_deltaDateFormat = new java.text.SimpleDateFormat("HH:mm:ss");
        m_deltaDateFormat.setTimeZone(tz);
    }

    @java.lang.Override
    protected com.google.common.util.concurrent.AbstractScheduledService.Scheduler scheduler() {
        int secs = m_configuration.getStackUpgradeAutoRetryCheckIntervalSecs();
        return com.google.common.util.concurrent.AbstractScheduledService.Scheduler.newFixedDelaySchedule(0, secs, java.util.concurrent.TimeUnit.SECONDS);
    }

    @java.lang.Override
    protected void startUp() throws java.lang.Exception {
        MAX_TIMEOUT_MINS = m_configuration.getStackUpgradeAutoRetryTimeoutMins();
        MAX_TIMEOUT_MS = MAX_TIMEOUT_MINS * 60000L;
        if (MAX_TIMEOUT_MINS < 1) {
            org.apache.ambari.server.state.services.RetryUpgradeActionService.LOG.info("Will not start service {} used to auto-retry failed actions during " + "Stack Upgrade since since the property {} is either invalid/missing or set to {}", this.getClass().getSimpleName(), org.apache.ambari.server.configuration.Configuration.STACK_UPGRADE_AUTO_RETRY_TIMEOUT_MINS.getKey(), MAX_TIMEOUT_MINS);
            stopAsync();
        }
        CUSTOM_COMMAND_NAMES_TO_IGNORE = m_configuration.getStackUpgradeAutoRetryCustomCommandNamesToIgnore();
        COMMAND_DETAILS_TO_IGNORE = m_configuration.getStackUpgradeAutoRetryCommandDetailsToIgnore();
    }

    public void setMaxTimeout(int mins) {
        MAX_TIMEOUT_MINS = mins;
        MAX_TIMEOUT_MS = MAX_TIMEOUT_MINS * 60000L;
    }

    @java.lang.Override
    protected void runOneIteration() throws java.lang.Exception {
        java.util.Map<java.lang.String, org.apache.ambari.server.state.Cluster> clusterMap = m_clustersProvider.get().getClusters();
        for (org.apache.ambari.server.state.Cluster cluster : clusterMap.values()) {
            try {
                org.apache.ambari.server.state.services.RetryUpgradeActionService.LOG.debug("Analyzing tasks for cluster {} that can be retried during Stack Upgrade.", cluster.getClusterName());
                java.lang.Long effectiveRequestId = getActiveUpgradeRequestId(cluster);
                if (effectiveRequestId != null) {
                    org.apache.ambari.server.state.services.RetryUpgradeActionService.LOG.debug("Upgrade is in-progress with request id {}.", effectiveRequestId);
                    retryHoldingCommandsInRequest(effectiveRequestId);
                }
            } catch (java.lang.Exception e) {
                org.apache.ambari.server.state.services.RetryUpgradeActionService.LOG.error("Unable to analyze commands that may be retried for cluster with id {}. Exception: {}", cluster.getClusterId(), e.getMessage());
            }
        }
    }

    private java.lang.Long getActiveUpgradeRequestId(org.apache.ambari.server.state.Cluster cluster) {
        org.apache.ambari.server.orm.entities.UpgradeEntity currentUpgrade = cluster.getUpgradeInProgress();
        if (currentUpgrade == null) {
            org.apache.ambari.server.state.services.RetryUpgradeActionService.LOG.debug("There is no active upgrade in progress. Skip retrying failed tasks.");
            return null;
        }
        org.apache.ambari.server.stack.upgrade.Direction direction = currentUpgrade.getDirection();
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion = currentUpgrade.getRepositoryVersion();
        org.apache.ambari.server.state.services.RetryUpgradeActionService.LOG.debug("Found an active upgrade with id: {}, direction: {}, {} {}", currentUpgrade.getId(), direction, currentUpgrade.getUpgradeType(), direction.getPreposition(), repositoryVersion.getVersion());
        return currentUpgrade.getRequestId();
    }

    @com.google.inject.persist.Transactional
    public void retryHoldingCommandsInRequest(java.lang.Long requestId) {
        if (requestId == null) {
            return;
        }
        long now = java.lang.System.currentTimeMillis();
        java.util.List<org.apache.ambari.server.orm.entities.HostRoleCommandEntity> holdingCommands = m_hostRoleCommandDAO.findByRequestIdAndStatuses(requestId, HOLDING_STATUSES);
        if (holdingCommands.size() > 0) {
            for (org.apache.ambari.server.orm.entities.HostRoleCommandEntity hrc : holdingCommands) {
                org.apache.ambari.server.state.services.RetryUpgradeActionService.LOG.debug("Comparing taskId: {}, attempt count: {}, original start time: {}, now: {}", hrc.getTaskId(), hrc.getAttemptCount(), hrc.getOriginalStartTime(), now);
                if (canRetryCommand(hrc)) {
                    boolean allowRetry = false;
                    if (((hrc.getOriginalStartTime() != null) && (hrc.getOriginalStartTime() > 0)) && (hrc.getOriginalStartTime() < now)) {
                        java.lang.Long retryTimeWindow = hrc.getOriginalStartTime() + MAX_TIMEOUT_MS;
                        java.lang.Long deltaMS = retryTimeWindow - now;
                        if (deltaMS > 0) {
                            java.lang.String originalStartTimeString = m_fullDateFormat.format(new java.util.Date(hrc.getOriginalStartTime()));
                            java.lang.String deltaString = m_deltaDateFormat.format(new java.util.Date(deltaMS));
                            org.apache.ambari.server.state.services.RetryUpgradeActionService.LOG.info("Retrying task with id: {}, attempts: {}, original start time: {}, time til timeout: {}", hrc.getTaskId(), hrc.getAttemptCount(), originalStartTimeString, deltaString);
                            allowRetry = true;
                        }
                    }
                    if ((((hrc.getOriginalStartTime() == null) || (hrc.getOriginalStartTime() == (-1L))) && ((hrc.getStartTime() == null) || (hrc.getStartTime() == (-1L)))) && (hrc.getAttemptCount() == 0)) {
                        org.apache.ambari.server.state.services.RetryUpgradeActionService.LOG.info("Re-scheduling task with id: {} since it has 0 attempts, and null start_time and " + "original_start_time, which likely means the host was not heartbeating when the command was supposed to be scheduled.", hrc.getTaskId());
                        allowRetry = true;
                    }
                    if (allowRetry) {
                        retryHostRoleCommand(hrc);
                    }
                }
            }
        }
    }

    private boolean canRetryCommand(org.apache.ambari.server.orm.entities.HostRoleCommandEntity hrc) {
        if (hrc.isRetryAllowed() && (!hrc.isFailureAutoSkipped())) {
            if (null != hrc.getCustomCommandName()) {
                for (java.lang.String s : CUSTOM_COMMAND_NAMES_TO_IGNORE) {
                    if (hrc.getCustomCommandName().toLowerCase().contains(s)) {
                        return false;
                    }
                }
            }
            if (null != hrc.getCommandDetail()) {
                for (java.lang.String s : COMMAND_DETAILS_TO_IGNORE) {
                    if (hrc.getCommandDetail().toLowerCase().contains(s)) {
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }

    private void retryHostRoleCommand(org.apache.ambari.server.orm.entities.HostRoleCommandEntity hrc) {
        try {
            hrc.setStatus(org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING);
            hrc.setStartTime(-1L);
            hrc.setEndTime(-1L);
            hrc.setLastAttemptTime(-1L);
            m_hostRoleCommandDAO.merge(hrc);
        } catch (java.lang.Exception e) {
            org.apache.ambari.server.state.services.RetryUpgradeActionService.LOG.error("Error while updating hostRoleCommand. Entity: {}", hrc, e);
            throw e;
        }
    }
}