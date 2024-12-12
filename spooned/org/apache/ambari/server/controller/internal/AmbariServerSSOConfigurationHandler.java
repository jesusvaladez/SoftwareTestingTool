package org.apache.ambari.server.controller.internal;
import static org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequest.StackAdvisorRequestType.SSO_CONFIGURATIONS;
import static org.apache.ambari.server.configuration.AmbariServerConfigurationCategory.SSO_CONFIGURATION;
import static org.apache.ambari.server.configuration.AmbariServerConfigurationKey.SSO_ENABLED_SERVICES;
import static org.apache.ambari.server.configuration.AmbariServerConfigurationKey.SSO_MANAGE_SERVICES;
@com.google.inject.Singleton
public class AmbariServerSSOConfigurationHandler extends org.apache.ambari.server.controller.internal.AmbariServerStackAdvisorAwareConfigurationHandler {
    @com.google.inject.Inject
    public AmbariServerSSOConfigurationHandler(org.apache.ambari.server.state.Clusters clusters, org.apache.ambari.server.state.ConfigHelper configHelper, org.apache.ambari.server.controller.AmbariManagementController managementController, org.apache.ambari.server.api.services.stackadvisor.StackAdvisorHelper stackAdvisorHelper, org.apache.ambari.server.orm.dao.AmbariConfigurationDAO ambariConfigurationDAO, org.apache.ambari.server.events.publishers.AmbariEventPublisher publisher) {
        super(ambariConfigurationDAO, publisher, clusters, configHelper, managementController, stackAdvisorHelper);
    }

    @java.lang.Override
    public void updateComponentCategory(java.lang.String categoryName, java.util.Map<java.lang.String, java.lang.String> properties, boolean removePropertiesIfNotSpecified) throws org.apache.ambari.server.AmbariException {
        super.updateComponentCategory(categoryName, properties, removePropertiesIfNotSpecified);
        final java.util.Map<java.lang.String, java.lang.String> ssoProperties = getConfigurationProperties(org.apache.ambari.server.configuration.AmbariServerConfigurationCategory.SSO_CONFIGURATION.getCategoryName());
        final boolean manageSSOConfigurations = (ssoProperties != null) && "true".equalsIgnoreCase(ssoProperties.get(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.SSO_MANAGE_SERVICES.key()));
        if (manageSSOConfigurations) {
            processClusters(org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequest.StackAdvisorRequestType.SSO_CONFIGURATIONS);
        }
    }

    public java.util.Set<java.lang.String> getSSOEnabledServices() {
        return getEnabledServices(org.apache.ambari.server.configuration.AmbariServerConfigurationCategory.SSO_CONFIGURATION.getCategoryName(), org.apache.ambari.server.configuration.AmbariServerConfigurationKey.SSO_MANAGE_SERVICES.key(), org.apache.ambari.server.configuration.AmbariServerConfigurationKey.SSO_ENABLED_SERVICES.key());
    }

    @java.lang.Override
    protected java.lang.String getServiceVersionNote() {
        return "Ambari-managed single sign-on configurations";
    }
}