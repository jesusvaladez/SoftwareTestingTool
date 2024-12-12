package org.apache.ambari.server.checks;
import com.google.inject.persist.PersistService;
public class DatabaseConsistencyChecker {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.checks.DatabaseConsistencyChecker.class);

    private com.google.inject.persist.PersistService persistService;

    private org.apache.ambari.server.orm.DBAccessor dbAccessor;

    private com.google.inject.Injector injector;

    @com.google.inject.Inject
    public DatabaseConsistencyChecker(org.apache.ambari.server.orm.DBAccessor dbAccessor, com.google.inject.Injector injector, com.google.inject.persist.PersistService persistService) {
        this.dbAccessor = dbAccessor;
        this.injector = injector;
        this.persistService = persistService;
    }

    public static class CheckHelperControllerModule extends org.apache.ambari.server.controller.ControllerModule {
        public CheckHelperControllerModule() throws java.lang.Exception {
        }

        @java.lang.Override
        protected void configure() {
            super.configure();
            org.apache.ambari.server.utils.EventBusSynchronizer.synchronizeAmbariEventPublisher(binder());
        }
    }

    public static class CheckHelperAuditModule extends org.apache.ambari.server.audit.AuditLoggerModule {
        public CheckHelperAuditModule() throws java.lang.Exception {
        }

        @java.lang.Override
        protected void configure() {
            super.configure();
        }
    }

    public void startPersistenceService() {
        persistService.start();
    }

    public void stopPersistenceService() {
        persistService.stop();
    }

    public static void main(java.lang.String[] args) throws java.lang.Exception {
        org.apache.ambari.server.checks.DatabaseConsistencyChecker databaseConsistencyChecker = null;
        try {
            com.google.inject.Injector injector = com.google.inject.Guice.createInjector(new org.apache.ambari.server.checks.DatabaseConsistencyChecker.CheckHelperControllerModule(), new org.apache.ambari.server.checks.DatabaseConsistencyChecker.CheckHelperAuditModule(), new org.apache.ambari.server.ldap.LdapModule());
            databaseConsistencyChecker = injector.getInstance(org.apache.ambari.server.checks.DatabaseConsistencyChecker.class);
            databaseConsistencyChecker.startPersistenceService();
            org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.runAllDBChecks(false);
            org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.checkHostComponentStates();
            org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.checkServiceConfigs();
            databaseConsistencyChecker.stopPersistenceService();
        } catch (java.lang.Throwable e) {
            if (e instanceof org.apache.ambari.server.AmbariException) {
                org.apache.ambari.server.checks.DatabaseConsistencyChecker.LOG.error("Exception occurred during database check:", e);
                throw ((org.apache.ambari.server.AmbariException) (e));
            } else {
                org.apache.ambari.server.checks.DatabaseConsistencyChecker.LOG.error("Unexpected error, database check failed", e);
                throw new java.lang.Exception("Unexpected error, database check failed", e);
            }
        } finally {
            org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.closeConnection();
            if (org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.getLastCheckResult().isErrorOrWarning()) {
                java.lang.String ambariDBConsistencyCheckLog = "ambari-server-check-database.log";
                ch.qos.logback.classic.Logger dbConsistencyCheckHelperLogger = ((ch.qos.logback.classic.Logger) (org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.class)));
                for (java.util.Iterator<ch.qos.logback.core.Appender<ch.qos.logback.classic.spi.ILoggingEvent>> index = dbConsistencyCheckHelperLogger.iteratorForAppenders(); index.hasNext();) {
                    ch.qos.logback.core.Appender<ch.qos.logback.classic.spi.ILoggingEvent> appender = index.next();
                    if (appender instanceof ch.qos.logback.core.FileAppender) {
                        ambariDBConsistencyCheckLog = ((ch.qos.logback.core.FileAppender) (appender)).getFile();
                        break;
                    }
                }
                ambariDBConsistencyCheckLog = ambariDBConsistencyCheckLog.replace("//", "/");
                if (org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.getLastCheckResult().isError()) {
                    java.lang.System.out.print(java.lang.String.format("DB configs consistency check failed. Run \"ambari-server start --skip-database-check\" to skip. " + ((("You may try --auto-fix-database flag to attempt to fix issues automatically. " + "If you use this \"--skip-database-check\" option, do not make any changes to your cluster topology ") + "or perform a cluster upgrade until you correct the database consistency issues. See \"%s\" ") + "for more details on the consistency issues."), ambariDBConsistencyCheckLog));
                } else {
                    java.lang.System.out.print(java.lang.String.format("DB configs consistency check found warnings. See \"%s\" " + "for more details.", ambariDBConsistencyCheckLog));
                }
            } else {
                java.lang.System.out.print("No errors and warnings were found.");
            }
        }
    }
}