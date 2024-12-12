package org.apache.ambari.server.security.authorization;
public class AuthorizationTestModuleForLdapDNWithSpace extends com.google.inject.AbstractModule {
    @java.lang.Override
    protected void configure() {
        java.util.Properties properties = new java.util.Properties();
        properties.setProperty(org.apache.ambari.server.configuration.Configuration.CLIENT_SECURITY.getKey(), "ldap");
        properties.setProperty(org.apache.ambari.server.configuration.Configuration.SERVER_PERSISTENCE_TYPE.getKey(), "in-memory");
        properties.setProperty(org.apache.ambari.server.configuration.Configuration.METADATA_DIR_PATH.getKey(), "src/test/resources/stacks");
        properties.setProperty(org.apache.ambari.server.configuration.Configuration.SERVER_VERSION_FILE.getKey(), "src/test/resources/version");
        properties.setProperty(org.apache.ambari.server.configuration.Configuration.OS_VERSION.getKey(), "centos5");
        properties.setProperty(org.apache.ambari.server.configuration.Configuration.SHARED_RESOURCES_DIR.getKey(), "src/test/resources/");
        properties.setProperty(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.USER_SEARCH_BASE.key(), "dc=ambari,dc=the apache,dc=org");
        properties.setProperty(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.GROUP_BASE.key(), "ou=the groups,dc=ambari,dc=the apache,dc=org");
        try {
            install(new org.apache.ambari.server.controller.ControllerModule(properties));
        } catch (java.lang.Exception e) {
            throw new java.lang.RuntimeException(e);
        }
    }
}