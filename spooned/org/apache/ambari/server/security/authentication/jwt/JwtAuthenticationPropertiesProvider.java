package org.apache.ambari.server.security.authentication.jwt;
import static org.apache.ambari.server.configuration.AmbariServerConfigurationCategory.SSO_CONFIGURATION;
@com.google.inject.Singleton
public class JwtAuthenticationPropertiesProvider extends org.apache.ambari.server.configuration.AmbariServerConfigurationProvider<org.apache.ambari.server.security.authentication.jwt.JwtAuthenticationProperties> {
    @com.google.inject.Inject
    public JwtAuthenticationPropertiesProvider(org.apache.ambari.server.events.publishers.AmbariEventPublisher ambariEventPublisher, com.google.inject.persist.jpa.AmbariJpaPersistService ambariJpaPersistService) {
        super(org.apache.ambari.server.configuration.AmbariServerConfigurationCategory.SSO_CONFIGURATION, ambariEventPublisher, ambariJpaPersistService);
    }

    @java.lang.Override
    protected org.apache.ambari.server.security.authentication.jwt.JwtAuthenticationProperties loadInstance(java.util.Collection<org.apache.ambari.server.orm.entities.AmbariConfigurationEntity> configurationEntities) {
        return new org.apache.ambari.server.security.authentication.jwt.JwtAuthenticationProperties(toProperties(configurationEntities));
    }
}