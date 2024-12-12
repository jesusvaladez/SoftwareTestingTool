package org.apache.ambari.server.security.authentication.tproxy;
@com.google.inject.Singleton
public class AmbariTProxyConfigurationProvider extends org.apache.ambari.server.configuration.AmbariServerConfigurationProvider<org.apache.ambari.server.security.authentication.tproxy.AmbariTProxyConfiguration> {
    @com.google.inject.Inject
    public AmbariTProxyConfigurationProvider(org.apache.ambari.server.events.publishers.AmbariEventPublisher ambariEventPublisher, com.google.inject.persist.jpa.AmbariJpaPersistService persistService) {
        super(org.apache.ambari.server.configuration.AmbariServerConfigurationCategory.TPROXY_CONFIGURATION, ambariEventPublisher, persistService);
    }

    @java.lang.Override
    protected org.apache.ambari.server.security.authentication.tproxy.AmbariTProxyConfiguration loadInstance(java.util.Collection<org.apache.ambari.server.orm.entities.AmbariConfigurationEntity> configurationEntities) {
        return new org.apache.ambari.server.security.authentication.tproxy.AmbariTProxyConfiguration(toProperties(configurationEntities));
    }
}