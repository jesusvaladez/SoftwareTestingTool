package org.apache.ambari.server.ldap.service;
@com.google.inject.Singleton
public class AmbariLdapConfigurationProvider extends org.apache.ambari.server.configuration.AmbariServerConfigurationProvider<org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration> {
    @com.google.inject.Inject
    public AmbariLdapConfigurationProvider(org.apache.ambari.server.events.publishers.AmbariEventPublisher publisher, com.google.inject.persist.jpa.AmbariJpaPersistService persistService) {
        super(org.apache.ambari.server.configuration.AmbariServerConfigurationCategory.LDAP_CONFIGURATION, publisher, persistService);
    }

    @java.lang.Override
    protected org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration loadInstance(java.util.Collection<org.apache.ambari.server.orm.entities.AmbariConfigurationEntity> configurationEntities) {
        return new org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration(toProperties(configurationEntities));
    }
}