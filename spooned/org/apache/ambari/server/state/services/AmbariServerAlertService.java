package org.apache.ambari.server.state.services;
@org.apache.ambari.server.AmbariService
public class AmbariServerAlertService extends com.google.common.util.concurrent.AbstractScheduledService {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.state.services.AmbariServerAlertService.class);

    @com.google.inject.Inject
    private com.google.inject.Injector m_injector;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.AlertDefinitionDAO m_dao;

    @com.google.inject.Inject
    private com.google.inject.Provider<org.apache.ambari.server.state.Clusters> m_clustersProvider;

    @com.google.inject.Inject
    private org.apache.ambari.server.state.alert.AlertDefinitionFactory m_alertDefinitionFactory;

    private java.util.concurrent.ScheduledExecutorService m_scheduledExecutorService;

    private final java.util.Map<java.lang.String, org.apache.ambari.server.state.services.AmbariServerAlertService.ScheduledAlert> m_futureMap = new java.util.concurrent.ConcurrentHashMap<>();

    public AmbariServerAlertService() {
    }

    @com.google.inject.Inject
    public void initExecutor(@com.google.inject.name.Named("alertServiceCorePoolSize")
    int alertServiceCorePoolSize) {
        this.m_scheduledExecutorService = java.util.concurrent.Executors.newScheduledThreadPool(alertServiceCorePoolSize);
    }

    @java.lang.Override
    protected com.google.common.util.concurrent.AbstractScheduledService.Scheduler scheduler() {
        return com.google.common.util.concurrent.AbstractScheduledService.Scheduler.newFixedDelaySchedule(1, 1, java.util.concurrent.TimeUnit.MINUTES);
    }

    @java.lang.Override
    protected void startUp() throws java.lang.Exception {
        java.util.Map<java.lang.String, org.apache.ambari.server.state.Cluster> clusterMap = m_clustersProvider.get().getClusters();
        for (org.apache.ambari.server.state.Cluster cluster : clusterMap.values()) {
            for (org.apache.ambari.server.orm.entities.AlertDefinitionEntity entity : m_dao.findBySourceType(cluster.getClusterId(), org.apache.ambari.server.state.alert.SourceType.SERVER)) {
                if (!entity.getEnabled()) {
                    continue;
                }
                scheduleRunnable(entity);
            }
        }
    }

    @java.lang.Override
    protected void runOneIteration() throws java.lang.Exception {
        java.util.Map<java.lang.String, org.apache.ambari.server.state.Cluster> clusterMap = m_clustersProvider.get().getClusters();
        for (org.apache.ambari.server.state.Cluster cluster : clusterMap.values()) {
            java.util.List<org.apache.ambari.server.orm.entities.AlertDefinitionEntity> entities = m_dao.findBySourceType(cluster.getClusterId(), org.apache.ambari.server.state.alert.SourceType.SERVER);
            for (org.apache.ambari.server.orm.entities.AlertDefinitionEntity entity : entities) {
                java.lang.String definitionName = entity.getDefinitionName();
                org.apache.ambari.server.state.services.AmbariServerAlertService.ScheduledAlert scheduledAlert = m_futureMap.get(definitionName);
                java.util.concurrent.ScheduledFuture<?> scheduledFuture = null;
                if (null != scheduledAlert) {
                    scheduledFuture = scheduledAlert.getScheduledFuture();
                }
                if (!entity.getEnabled()) {
                    if (null != scheduledFuture) {
                        unschedule(definitionName, scheduledFuture);
                    }
                    continue;
                }
                if ((null == scheduledAlert) || (null == scheduledFuture)) {
                    scheduleRunnable(entity);
                    continue;
                }
                int scheduledInterval = scheduledAlert.getInterval();
                if (scheduledInterval != entity.getScheduleInterval()) {
                    unschedule(definitionName, scheduledFuture);
                    scheduleRunnable(entity);
                }
            }
        }
    }

    private void unschedule(java.lang.String definitionName, java.util.concurrent.ScheduledFuture<?> scheduledFuture) {
        m_futureMap.remove(definitionName);
        if (null != scheduledFuture) {
            scheduledFuture.cancel(true);
            org.apache.ambari.server.state.services.AmbariServerAlertService.LOG.info("Unscheduled server alert {}", definitionName);
        }
    }

    private void scheduleRunnable(org.apache.ambari.server.orm.entities.AlertDefinitionEntity entity) throws java.lang.ClassNotFoundException, java.lang.IllegalAccessException, java.lang.InstantiationException {
        if (!entity.getEnabled()) {
            return;
        }
        org.apache.ambari.server.state.alert.AlertDefinition definition = m_alertDefinitionFactory.coerce(entity);
        org.apache.ambari.server.state.alert.ServerSource serverSource = ((org.apache.ambari.server.state.alert.ServerSource) (definition.getSource()));
        java.lang.String sourceClass = serverSource.getSourceClass();
        int interval = definition.getInterval();
        try {
            java.lang.Class<?> clazz = java.lang.Class.forName(sourceClass);
            if (!org.apache.ambari.server.alerts.AlertRunnable.class.isAssignableFrom(clazz)) {
                org.apache.ambari.server.state.services.AmbariServerAlertService.LOG.warn("Unable to schedule a server side alert for {} because it is not an {}", sourceClass, org.apache.ambari.server.alerts.AlertRunnable.class);
                return;
            }
            java.lang.reflect.Constructor<? extends org.apache.ambari.server.alerts.AlertRunnable> constructor = clazz.asSubclass(org.apache.ambari.server.alerts.AlertRunnable.class).getConstructor(java.lang.String.class);
            org.apache.ambari.server.alerts.AlertRunnable alertRunnable = constructor.newInstance(entity.getDefinitionName());
            m_injector.injectMembers(alertRunnable);
            java.util.concurrent.ScheduledFuture<?> scheduledFuture = m_scheduledExecutorService.scheduleWithFixedDelay(alertRunnable, interval, interval, java.util.concurrent.TimeUnit.MINUTES);
            java.lang.String definitionName = entity.getDefinitionName();
            org.apache.ambari.server.state.services.AmbariServerAlertService.ScheduledAlert scheduledAlert = new org.apache.ambari.server.state.services.AmbariServerAlertService.ScheduledAlert(scheduledFuture, interval);
            m_futureMap.put(definitionName, scheduledAlert);
            org.apache.ambari.server.state.services.AmbariServerAlertService.LOG.info("Scheduled server alert {} to run every {} minutes", definitionName, interval);
        } catch (java.lang.ClassNotFoundException cnfe) {
            org.apache.ambari.server.state.services.AmbariServerAlertService.LOG.error("Unable to schedule a server side alert for {} because it could not be found in the classpath", sourceClass);
        } catch (java.lang.NoSuchMethodException nsme) {
            org.apache.ambari.server.state.services.AmbariServerAlertService.LOG.error("Unable to schedule a server side alert for {} because it does not have a constructor which takes the proper arguments.", sourceClass);
        } catch (java.lang.reflect.InvocationTargetException ite) {
            org.apache.ambari.server.state.services.AmbariServerAlertService.LOG.error("Unable to schedule a server side alert for {} because an exception occurred while constructing the instance.", sourceClass, ite);
        }
    }

    private static final class ScheduledAlert {
        private final java.util.concurrent.ScheduledFuture<?> m_scheduledFuture;

        private final int m_interval;

        private ScheduledAlert(java.util.concurrent.ScheduledFuture<?> scheduledFuture, int interval) {
            m_scheduledFuture = scheduledFuture;
            m_interval = interval;
        }

        private java.util.concurrent.ScheduledFuture<?> getScheduledFuture() {
            return m_scheduledFuture;
        }

        private int getInterval() {
            return m_interval;
        }
    }
}