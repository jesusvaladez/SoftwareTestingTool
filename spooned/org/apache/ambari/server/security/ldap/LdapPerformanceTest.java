package org.apache.ambari.server.security.ldap;
@org.junit.Ignore
public class LdapPerformanceTest {
    private static com.google.inject.Injector injector;

    @com.google.inject.Inject
    private org.apache.ambari.server.security.ldap.AmbariLdapDataPopulator populator;

    @com.google.inject.Inject
    private org.apache.ambari.server.security.authorization.Users users;

    @com.google.inject.Inject
    org.apache.ambari.server.configuration.Configuration configuration;

    @com.google.inject.Inject
    org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration ldapConfiguration;

    final java.lang.String SPRING_CONTEXT_LOCATION = "classpath:webapp/WEB-INF/spring-security.xml";

    @org.junit.Before
    public void setUp() {
        org.apache.ambari.server.security.ldap.LdapPerformanceTest.injector = com.google.inject.Guice.createInjector(new org.apache.ambari.server.security.authorization.AuthorizationTestModule());
        org.apache.ambari.server.security.ldap.LdapPerformanceTest.injector.injectMembers(this);
        org.apache.ambari.server.security.ldap.LdapPerformanceTest.injector.getInstance(org.apache.ambari.server.orm.GuiceJpaInitializer.class);
        configuration.setClientSecurityType(org.apache.ambari.server.security.ClientSecurityType.LDAP);
        ldapConfiguration.setValueFor(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.SERVER_HOST, "c6402.ambari.apache.org");
        ldapConfiguration.setValueFor(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.SERVER_PORT, "389");
        ldapConfiguration.setValueFor(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.USER_OBJECT_CLASS, "posixAccount");
        ldapConfiguration.setValueFor(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.USER_NAME_ATTRIBUTE, "uid");
        ldapConfiguration.setValueFor(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.GROUP_OBJECT_CLASS, "posixGroup");
        ldapConfiguration.setValueFor(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.GROUP_NAME_ATTRIBUTE, "cn");
        ldapConfiguration.setValueFor(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.GROUP_MEMBER_ATTRIBUTE, "memberUid");
        ldapConfiguration.setValueFor(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.USER_SEARCH_BASE, "dc=apache,dc=org");
        ldapConfiguration.setValueFor(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.ANONYMOUS_BIND, "false");
        ldapConfiguration.setValueFor(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.BIND_DN, "uid=hdfs,ou=people,ou=dev,dc=apache,dc=org");
        ldapConfiguration.setValueFor(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.BIND_PASSWORD, "hdfs");
    }

    @org.junit.After
    public void tearDown() throws org.apache.ambari.server.AmbariException, java.sql.SQLException {
        org.apache.ambari.server.H2DatabaseCleaner.clearDatabaseAndStopPersistenceService(org.apache.ambari.server.security.ldap.LdapPerformanceTest.injector);
    }

    @org.junit.Test
    public void testLdapSync() throws org.apache.ambari.server.AmbariException, java.lang.InterruptedException {
        long time = java.lang.System.currentTimeMillis();
        java.util.Set<org.apache.ambari.server.security.ldap.LdapGroupDto> groups = populator.getExternalLdapGroupInfo();
        java.util.Set<org.apache.ambari.server.security.ldap.LdapUserDto> users = populator.getExternalLdapUserInfo();
        java.util.Set<java.lang.String> userNames = new java.util.HashSet<>();
        for (org.apache.ambari.server.security.ldap.LdapUserDto user : users) {
            userNames.add(user.getUserName());
        }
        java.util.Set<java.lang.String> groupNames = new java.util.HashSet<>();
        for (org.apache.ambari.server.security.ldap.LdapGroupDto group : groups) {
            groupNames.add(group.getGroupName());
        }
        java.lang.System.out.println("Data fetch: " + (java.lang.System.currentTimeMillis() - time));
        time = java.lang.System.currentTimeMillis();
        org.apache.ambari.server.security.ldap.LdapBatchDto batchDto = new org.apache.ambari.server.security.ldap.LdapBatchDto();
        populator.synchronizeLdapUsers(userNames, batchDto, false);
        populator.synchronizeLdapGroups(groupNames, batchDto, false);
        this.users.processLdapSync(batchDto);
        java.lang.System.out.println("Initial sync: " + (java.lang.System.currentTimeMillis() - time));
        time = java.lang.System.currentTimeMillis();
        batchDto = new org.apache.ambari.server.security.ldap.LdapBatchDto();
        populator.synchronizeLdapUsers(userNames, batchDto, false);
        populator.synchronizeLdapGroups(groupNames, batchDto, false);
        this.users.processLdapSync(batchDto);
        java.lang.System.out.println("Subsequent sync: " + (java.lang.System.currentTimeMillis() - time));
    }
}