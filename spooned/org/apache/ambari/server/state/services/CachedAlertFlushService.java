package org.apache.ambari.server.state.services;
@org.apache.ambari.server.AmbariService
@org.apache.ambari.annotations.Experimental(feature = org.apache.ambari.annotations.ExperimentalFeature.ALERT_CACHING)
public class CachedAlertFlushService extends com.google.common.util.concurrent.AbstractScheduledService {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.state.services.CachedAlertFlushService.class);

    @com.google.inject.Inject
    private org.apache.ambari.server.configuration.Configuration m_configuration;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.AlertsDAO m_alertsDAO;

    @java.lang.Override
    protected com.google.common.util.concurrent.AbstractScheduledService.Scheduler scheduler() {
        int flushIntervalInMinutes = m_configuration.getAlertCacheFlushInterval();
        return com.google.common.util.concurrent.AbstractScheduledService.Scheduler.newFixedDelaySchedule(flushIntervalInMinutes, flushIntervalInMinutes, java.util.concurrent.TimeUnit.MINUTES);
    }

    @java.lang.Override
    protected void startUp() throws java.lang.Exception {
        boolean enabled = m_configuration.isAlertCacheEnabled();
        if (!enabled) {
            stopAsync();
        }
    }

    @java.lang.Override
    protected void runOneIteration() throws java.lang.Exception {
        try {
            org.apache.ambari.server.state.services.CachedAlertFlushService.LOG.info("Flushing cached alerts to the database");
            m_alertsDAO.flushCachedEntitiesToJPA();
        } catch (java.lang.Exception exception) {
            org.apache.ambari.server.state.services.CachedAlertFlushService.LOG.error("Unable to flush cached alerts to the database", exception);
        }
    }
}