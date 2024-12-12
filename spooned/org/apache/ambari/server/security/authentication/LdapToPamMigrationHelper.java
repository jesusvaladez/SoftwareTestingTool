package org.apache.ambari.server.security.authentication;
public class LdapToPamMigrationHelper {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.security.authentication.LdapToPamMigrationHelper.class);

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.DBAccessor dbAccessor;

    private void migrateLdapUsersGroups() throws java.sql.SQLException {
        if (dbAccessor.getDbType() != org.apache.ambari.server.orm.DBAccessor.DbType.ORACLE) {
            dbAccessor.executeQuery("UPDATE users SET user_type='PAM',ldap_user=0 WHERE ldap_user=1 and user_name not in (select user_name from (select user_name from users where user_type = 'PAM') as a)");
            dbAccessor.executeQuery("UPDATE groups SET group_type='PAM',ldap_group=0 WHERE ldap_group=1 and group_name not in (select group_name from (select group_name from groups where group_type = 'PAM') as a)");
        } else {
            dbAccessor.executeQuery("UPDATE users SET user_type='PAM',ldap_user=0 WHERE ldap_user=1 and user_name not in (select user_name from users where user_type = 'PAM')");
            dbAccessor.executeQuery("UPDATE groups SET group_type='PAM',ldap_group=0 WHERE ldap_group=1 and group_name not in (select group_name from groups where group_type = 'PAM')");
        }
    }

    public static void main(java.lang.String[] args) {
        try {
            com.google.inject.Injector injector = com.google.inject.Guice.createInjector(new org.apache.ambari.server.controller.ControllerModule(), new org.apache.ambari.server.audit.AuditLoggerModule());
            org.apache.ambari.server.security.authentication.LdapToPamMigrationHelper migrationHelper = injector.getInstance(org.apache.ambari.server.security.authentication.LdapToPamMigrationHelper.class);
            migrationHelper.migrateLdapUsersGroups();
        } catch (java.lang.Throwable t) {
            org.apache.ambari.server.security.authentication.LdapToPamMigrationHelper.LOG.error("Caught exception on migration. Exiting...", t);
            java.lang.System.exit(1);
        }
    }
}