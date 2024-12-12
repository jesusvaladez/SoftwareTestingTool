package org.apache.ambari.server.checks;
import com.google.inject.persist.PersistService;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
public class MpackInstallChecker {
    private static final java.lang.String MPACK_STACKS_ARG = "mpack-stacks";

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.checks.MpackInstallChecker.class);

    private com.google.inject.persist.PersistService persistService;

    private org.apache.ambari.server.orm.DBAccessor dbAccessor;

    private com.google.inject.Injector injector;

    private java.sql.Connection connection;

    private boolean errorsFound = false;

    private static org.apache.commons.cli.Options getOptions() {
        org.apache.commons.cli.Options options = new org.apache.commons.cli.Options();
        options.addOption(org.apache.commons.cli.Option.builder().longOpt(org.apache.ambari.server.checks.MpackInstallChecker.MPACK_STACKS_ARG).desc("List of stacks defined in the management pack").required().type(java.lang.String.class).hasArg().valueSeparator(' ').build());
        return options;
    }

    private static org.apache.ambari.server.checks.MpackInstallChecker.MpackContext processArguments(java.lang.String... args) {
        org.apache.commons.cli.CommandLineParser cmdLineParser = new org.apache.commons.cli.DefaultParser();
        org.apache.ambari.server.checks.MpackInstallChecker.MpackContext ctx = null;
        try {
            org.apache.commons.cli.CommandLine line = cmdLineParser.parse(org.apache.ambari.server.checks.MpackInstallChecker.getOptions(), args);
            java.lang.String mpackStacksStr = ((java.lang.String) (line.getParsedOptionValue(org.apache.ambari.server.checks.MpackInstallChecker.MPACK_STACKS_ARG)));
            java.util.HashSet<java.lang.String> stacksInMpack = new java.util.HashSet<>(java.util.Arrays.asList(mpackStacksStr.split(",")));
            ctx = new org.apache.ambari.server.checks.MpackInstallChecker.MpackContext(stacksInMpack);
        } catch (java.lang.Exception exp) {
            java.lang.System.err.println("Parsing failed. Reason: " + exp.getMessage());
            org.apache.ambari.server.checks.MpackInstallChecker.LOG.error("Parsing failed. Reason: ", exp);
            java.lang.System.exit(1);
        }
        return ctx;
    }

    public boolean isErrorsFound() {
        return errorsFound;
    }

    @com.google.inject.Inject
    public MpackInstallChecker(org.apache.ambari.server.orm.DBAccessor dbAccessor, com.google.inject.Injector injector, com.google.inject.persist.PersistService persistService) {
        this.dbAccessor = dbAccessor;
        this.injector = injector;
        this.persistService = persistService;
    }

    public static class MpackCheckerAuditModule extends org.apache.ambari.server.audit.AuditLoggerModule {
        public MpackCheckerAuditModule() throws java.lang.Exception {
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

    public java.sql.Connection getConnection() {
        if (connection == null) {
            if (dbAccessor == null) {
                dbAccessor = injector.getInstance(org.apache.ambari.server.orm.DBAccessor.class);
            }
            connection = dbAccessor.getConnection();
        }
        return connection;
    }

    public void checkClusters(java.util.HashSet<java.lang.String> stacksInMpack) {
        java.sql.ResultSet rs = null;
        java.sql.Statement statement = null;
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> clusterStackInfo = new java.util.HashMap<>();
        java.lang.String GET_STACK_NAME_VERSION_QUERY = "select c.cluster_name, s.stack_name, s.stack_version from clusters c " + "join stack s on c.desired_stack_id = s.stack_id";
        java.sql.Connection conn = getConnection();
        try {
            statement = conn.createStatement(java.sql.ResultSet.TYPE_SCROLL_SENSITIVE, java.sql.ResultSet.CONCUR_UPDATABLE);
            rs = statement.executeQuery(GET_STACK_NAME_VERSION_QUERY);
            if (rs != null) {
                while (rs.next()) {
                    java.util.Map<java.lang.String, java.lang.String> stackInfoMap = new java.util.HashMap<>();
                    stackInfoMap.put(rs.getString("stack_name"), rs.getString("stack_version"));
                    clusterStackInfo.put(rs.getString("cluster_name"), stackInfoMap);
                } 
            }
            for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> clusterStackInfoEntry : clusterStackInfo.entrySet()) {
                java.lang.String clusterName = clusterStackInfoEntry.getKey();
                java.util.Map<java.lang.String, java.lang.String> stackInfo = clusterStackInfoEntry.getValue();
                java.lang.String stackName = stackInfo.keySet().iterator().next();
                java.lang.String stackVersion = stackInfo.get(stackName);
                if (!stacksInMpack.contains(stackName)) {
                    java.lang.String errorMsg = java.lang.String.format("This Ambari instance is already managing the cluster %s that has the " + ((("%s-%s stack installed on it. The management pack you are attempting to install only contains stack " + "definitions for %s. Since this management pack does not contain a stack that has already being ") + "deployed by Ambari, the --purge option would cause your existing Ambari installation to be unusable. ") + "Due to that we cannot install this management pack."), clusterName, stackName, stackVersion, stacksInMpack.toString());
                    org.apache.ambari.server.checks.MpackInstallChecker.LOG.error(errorMsg);
                    java.lang.System.err.println(errorMsg);
                    errorsFound = true;
                }
            }
        } catch (java.sql.SQLException e) {
            java.lang.System.err.println("SQL Exception occured during check for validating installed clusters. Reason: " + e.getMessage());
            org.apache.ambari.server.checks.MpackInstallChecker.LOG.error("SQL Exception occured during check for validating installed clusters. Reason: ", e);
            errorsFound = true;
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (java.sql.SQLException e) {
                    java.lang.System.err.println("SQL Exception occurred during result set closing procedure. Reason: " + e.getMessage());
                    org.apache.ambari.server.checks.MpackInstallChecker.LOG.error("SQL Exception occurred during result set closing procedure. Reason: ", e);
                    errorsFound = true;
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (java.sql.SQLException e) {
                    java.lang.System.err.println("SQL Exception occurred during statement closing procedure. Reason: " + e.getMessage());
                    org.apache.ambari.server.checks.MpackInstallChecker.LOG.error("SQL Exception occurred during statement closing procedure. Reason: ", e);
                    errorsFound = true;
                }
            }
        }
    }

    public static void main(java.lang.String[] args) throws java.lang.Exception {
        com.google.inject.Injector injector = com.google.inject.Guice.createInjector(new org.apache.ambari.server.controller.ControllerModule(), new org.apache.ambari.server.checks.MpackInstallChecker.MpackCheckerAuditModule(), new org.apache.ambari.server.ldap.LdapModule());
        org.apache.ambari.server.checks.MpackInstallChecker mpackInstallChecker = injector.getInstance(org.apache.ambari.server.checks.MpackInstallChecker.class);
        org.apache.ambari.server.checks.MpackInstallChecker.MpackContext mpackContext = org.apache.ambari.server.checks.MpackInstallChecker.processArguments(args);
        mpackInstallChecker.startPersistenceService();
        mpackInstallChecker.checkClusters(mpackContext.getStacksInMpack());
        mpackInstallChecker.stopPersistenceService();
        if (mpackInstallChecker.isErrorsFound()) {
            org.apache.ambari.server.checks.MpackInstallChecker.LOG.error("Mpack installation checker failed!");
            java.lang.System.err.println("Mpack installation checker failed!");
            java.lang.System.exit(1);
        } else {
            org.apache.ambari.server.checks.MpackInstallChecker.LOG.info("No errors found");
            java.lang.System.out.println("No errors found");
        }
    }

    private static class MpackContext {
        private java.util.HashSet<java.lang.String> stacksInMpack;

        public MpackContext(java.util.HashSet<java.lang.String> stacksInMpack) {
            this.stacksInMpack = stacksInMpack;
        }

        public java.util.HashSet<java.lang.String> getStacksInMpack() {
            return stacksInMpack;
        }
    }
}