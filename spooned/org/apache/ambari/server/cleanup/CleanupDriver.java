package org.apache.ambari.server.cleanup;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
public class CleanupDriver {
    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.cleanup.CleanupDriver.class);

    private static final java.lang.String DATE_PATTERN = "yyyy-MM-dd";

    private static final java.lang.String CLUSTER_NAME_ARG = "cluster-name";

    private static final java.lang.String FROM_DATE_ARG = "from-date";

    private static org.apache.commons.cli.Options getOptions() {
        org.apache.commons.cli.Options options = new org.apache.commons.cli.Options();
        options.addOption(org.apache.commons.cli.Option.builder().longOpt(org.apache.ambari.server.cleanup.CleanupDriver.CLUSTER_NAME_ARG).desc("The cluster name").required().type(java.lang.String.class).hasArg().valueSeparator(' ').build());
        options.addOption(org.apache.commons.cli.Option.builder().longOpt(org.apache.ambari.server.cleanup.CleanupDriver.FROM_DATE_ARG).desc("Date up until data will be purged.").required().type(java.lang.String.class).hasArg().valueSeparator(' ').build());
        return options;
    }

    private static org.apache.ambari.server.cleanup.CleanupDriver.CleanupContext processArguments(java.lang.String... args) {
        org.apache.commons.cli.CommandLineParser cmdLineParser = new org.apache.commons.cli.DefaultParser();
        org.apache.commons.cli.HelpFormatter formatter = new org.apache.commons.cli.HelpFormatter();
        java.text.DateFormat df = new java.text.SimpleDateFormat(org.apache.ambari.server.cleanup.CleanupDriver.DATE_PATTERN);
        org.apache.ambari.server.cleanup.CleanupDriver.CleanupContext ctx = null;
        try {
            org.apache.commons.cli.CommandLine line = cmdLineParser.parse(org.apache.ambari.server.cleanup.CleanupDriver.getOptions(), args);
            java.lang.String clusterName = ((java.lang.String) (line.getParsedOptionValue(org.apache.ambari.server.cleanup.CleanupDriver.CLUSTER_NAME_ARG)));
            java.util.Date fromDate = df.parse(line.getOptionValue(org.apache.ambari.server.cleanup.CleanupDriver.FROM_DATE_ARG));
            ctx = new org.apache.ambari.server.cleanup.CleanupDriver.CleanupContext(clusterName, fromDate.getTime());
        } catch (java.lang.Exception exp) {
            java.lang.System.err.println("Parsing failed.  Reason: " + exp.getMessage());
            org.apache.ambari.server.cleanup.CleanupDriver.LOGGER.error("Parsing failed.  Reason: ", exp);
            formatter.printHelp("db-purge-history", org.apache.ambari.server.cleanup.CleanupDriver.getOptions());
            java.lang.System.exit(1);
        }
        return ctx;
    }

    public static void main(java.lang.String... args) throws java.lang.Exception {
        org.apache.ambari.server.cleanup.CleanupDriver.LOGGER.info("DB-PURGE - Starting the database purge process ...");
        org.apache.ambari.server.cleanup.CleanupDriver.CleanupContext cleanupContext = org.apache.ambari.server.cleanup.CleanupDriver.processArguments(args);
        com.google.inject.Injector injector = com.google.inject.Guice.createInjector(new org.apache.ambari.server.controller.ControllerModule(), new org.apache.ambari.server.audit.AuditLoggerModule(), new org.apache.ambari.server.cleanup.CleanupModule(), new org.apache.ambari.server.ldap.LdapModule());
        injector.getInstance(com.google.inject.persist.jpa.AmbariJpaPersistService.class).start();
        org.apache.ambari.server.cleanup.CleanupServiceImpl cleanupService = injector.getInstance(org.apache.ambari.server.cleanup.CleanupServiceImpl.class);
        org.apache.ambari.server.cleanup.CleanupService.CleanupResult result = cleanupService.cleanup(new org.apache.ambari.server.cleanup.TimeBasedCleanupPolicy(cleanupContext.getClusterName(), cleanupContext.getFromDayTimestamp()));
        injector.getInstance(com.google.inject.persist.jpa.AmbariJpaPersistService.class).stop();
        if (result.getErrorCount() > 0) {
            org.apache.ambari.server.cleanup.CleanupDriver.LOGGER.warn("DB-PURGE - completed with error, check Ambari Server log for details ! Number of affected records [{}]", result.getAffectedRows());
            java.lang.System.exit(2);
        }
        org.apache.ambari.server.cleanup.CleanupDriver.LOGGER.info("DB-PURGE - completed. Number of affected records [{}]", result.getAffectedRows());
    }

    private static class CleanupContext {
        private java.lang.String clusterName;

        private java.lang.Long fromDayTimestamp;

        public CleanupContext(java.lang.String clusterName, java.lang.Long fromDayTimestamp) {
            this.clusterName = clusterName;
            this.fromDayTimestamp = fromDayTimestamp;
        }

        public java.lang.String getClusterName() {
            return clusterName;
        }

        public java.lang.Long getFromDayTimestamp() {
            return fromDayTimestamp;
        }
    }
}