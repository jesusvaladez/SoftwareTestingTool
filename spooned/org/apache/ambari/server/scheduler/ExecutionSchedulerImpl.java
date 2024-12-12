package org.apache.ambari.server.scheduler;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
@com.google.inject.Singleton
public class ExecutionSchedulerImpl implements org.apache.ambari.server.scheduler.ExecutionScheduler {
    @com.google.inject.Inject
    private org.apache.ambari.server.configuration.Configuration configuration;

    @com.google.inject.Inject
    org.apache.ambari.server.state.scheduler.GuiceJobFactory guiceJobFactory;

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.scheduler.ExecutionSchedulerImpl.class);

    protected static final java.lang.String DEFAULT_SCHEDULER_NAME = "ExecutionScheduler";

    protected org.quartz.Scheduler scheduler;

    protected static volatile boolean isInitialized = false;

    @com.google.inject.Inject
    public ExecutionSchedulerImpl(com.google.inject.Injector injector) {
        injector.injectMembers(this);
    }

    protected ExecutionSchedulerImpl(org.apache.ambari.server.configuration.Configuration configuration) {
        this.configuration = configuration;
    }

    protected synchronized void initializeScheduler() {
        org.quartz.impl.StdSchedulerFactory sf = new org.quartz.impl.StdSchedulerFactory();
        java.util.Properties properties = getQuartzSchedulerProperties();
        try {
            sf.initialize(properties);
        } catch (org.quartz.SchedulerException e) {
            org.apache.ambari.server.scheduler.ExecutionSchedulerImpl.LOG.warn("Failed to initialize Request Execution Scheduler properties !");
            org.apache.ambari.server.scheduler.ExecutionSchedulerImpl.LOG.debug("Scheduler properties: \n{}", properties);
            e.printStackTrace();
            return;
        }
        try {
            scheduler = sf.getScheduler();
            scheduler.setJobFactory(guiceJobFactory);
            org.apache.ambari.server.scheduler.ExecutionSchedulerImpl.isInitialized = true;
        } catch (org.quartz.SchedulerException e) {
            org.apache.ambari.server.scheduler.ExecutionSchedulerImpl.LOG.warn("Failed to create Request Execution scheduler !");
            e.printStackTrace();
        }
    }

    protected java.util.Properties getQuartzSchedulerProperties() {
        java.util.Properties properties = new java.util.Properties();
        properties.setProperty("org.quartz.scheduler.instanceName", org.apache.ambari.server.scheduler.ExecutionSchedulerImpl.DEFAULT_SCHEDULER_NAME);
        properties.setProperty("org.quartz.scheduler.instanceId", "AUTO");
        properties.setProperty("org.quartz.threadPool.class", "org.quartz.simpl.SimpleThreadPool");
        properties.setProperty("org.quartz.threadPool.threadCount", configuration.getExecutionSchedulerThreads());
        properties.setProperty("org.quartz.jobStore.class", "org.quartz.impl.jdbcjobstore.JobStoreTX");
        properties.setProperty("org.quartz.jobStore.isClustered", configuration.isExecutionSchedulerClusterd());
        java.lang.String[] subProps = getQuartzDbDelegateClassAndValidationQuery();
        properties.setProperty("org.quartz.jobStore.driverDelegateClass", subProps[0]);
        properties.setProperty("org.quartz.jobStore.useProperties", "false");
        properties.setProperty("org.quartz.jobStore.dataSource", "myDS");
        properties.setProperty("org.quartz.dataSource.myDS.driver", configuration.getDatabaseDriver());
        properties.setProperty("org.quartz.dataSource.myDS.URL", configuration.getDatabaseUrl());
        properties.setProperty("org.quartz.dataSource.myDS.user", configuration.getDatabaseUser());
        properties.setProperty("org.quartz.dataSource.myDS.password", configuration.getDatabasePassword());
        properties.setProperty("org.quartz.dataSource.myDS.maxConnections", configuration.getExecutionSchedulerConnections());
        properties.setProperty("org.quartz.dataSource.myDS.maxCachedStatementsPerConnection", configuration.getExecutionSchedulerMaxStatementsPerConnection());
        properties.setProperty("org.quartz.dataSource.myDS.validationQuery", subProps[1]);
        properties.setProperty("org.quartz.scheduler.skipUpdateCheck", "true");
        org.apache.ambari.server.scheduler.ExecutionSchedulerImpl.LOG.debug("Using quartz properties: {}", properties);
        return properties;
    }

    protected synchronized boolean isInitialized() {
        return org.apache.ambari.server.scheduler.ExecutionSchedulerImpl.isInitialized;
    }

    protected java.lang.String[] getQuartzDbDelegateClassAndValidationQuery() {
        java.lang.String dbDelegate = "org.quartz.impl.jdbcjobstore.StdJDBCDelegate";
        java.lang.String dbValidate = "select 0";
        org.apache.ambari.server.configuration.Configuration.DatabaseType databaseType = configuration.getDatabaseType();
        if (databaseType == org.apache.ambari.server.configuration.Configuration.DatabaseType.POSTGRES) {
            dbDelegate = "org.quartz.impl.jdbcjobstore.PostgreSQLDelegate";
        } else if (databaseType == org.apache.ambari.server.configuration.Configuration.DatabaseType.ORACLE) {
            dbDelegate = "org.quartz.impl.jdbcjobstore.oracle.OracleDelegate";
            dbValidate = "select 0 from dual";
        }
        return new java.lang.String[]{ dbDelegate, dbValidate };
    }

    @java.lang.Override
    public synchronized void startScheduler(java.lang.Integer delay) throws org.apache.ambari.server.AmbariException {
        try {
            if (!org.apache.ambari.server.scheduler.ExecutionSchedulerImpl.isInitialized) {
                initializeScheduler();
                org.apache.ambari.server.scheduler.ExecutionSchedulerImpl.isInitialized = true;
            }
        } catch (java.lang.Exception e) {
            java.lang.String msg = "Unable to initialize Request Execution scheduler !";
            org.apache.ambari.server.scheduler.ExecutionSchedulerImpl.LOG.warn(msg);
            e.printStackTrace();
            throw new org.apache.ambari.server.AmbariException(msg);
        }
        try {
            if (!scheduler.isStarted()) {
                if (delay != null) {
                    scheduler.startDelayed(delay);
                } else {
                    scheduler.start();
                }
            } else {
                org.apache.ambari.server.scheduler.ExecutionSchedulerImpl.LOG.info(("Scheduler " + scheduler.getSchedulerInstanceId()) + " already started. Skipping start.");
            }
        } catch (org.quartz.SchedulerException e) {
            org.apache.ambari.server.scheduler.ExecutionSchedulerImpl.LOG.error("Failed to start scheduler", e);
            throw new org.apache.ambari.server.AmbariException(e.getMessage());
        }
    }

    @java.lang.Override
    public synchronized void stopScheduler() throws org.apache.ambari.server.AmbariException {
        if (scheduler == null) {
            throw new org.apache.ambari.server.AmbariException("Scheduler not instantiated !");
        }
        try {
            scheduler.shutdown();
        } catch (org.quartz.SchedulerException e) {
            org.apache.ambari.server.scheduler.ExecutionSchedulerImpl.LOG.error("Failed to stop scheduler", e);
            throw new org.apache.ambari.server.AmbariException(e.getMessage());
        }
    }

    @java.lang.Override
    public void scheduleJob(org.quartz.Trigger trigger) throws org.quartz.SchedulerException {
        scheduler.scheduleJob(trigger);
    }

    @java.lang.Override
    public void addJob(org.quartz.JobDetail jobDetail) throws org.quartz.SchedulerException {
        scheduler.addJob(jobDetail, true);
    }

    @java.lang.Override
    public void deleteJob(org.quartz.JobKey jobKey) throws org.quartz.SchedulerException {
        scheduler.deleteJob(jobKey);
    }

    @java.lang.Override
    public org.quartz.JobDetail getJobDetail(org.quartz.JobKey jobKey) throws org.quartz.SchedulerException {
        return scheduler.getJobDetail(jobKey);
    }

    @java.lang.Override
    public java.util.List<? extends org.quartz.Trigger> getTriggersForJob(org.quartz.JobKey jobKey) throws org.quartz.SchedulerException {
        return scheduler.getTriggersOfJob(jobKey);
    }

    @java.lang.Override
    public boolean isSchedulerStarted() throws org.quartz.SchedulerException {
        return scheduler.isStarted();
    }
}