package org.apache.ambari.server.controller.internal;
@com.google.inject.Singleton
public class RootServiceComponentConfigurationHandlerFactory {
    @com.google.inject.Inject
    private org.apache.ambari.server.controller.internal.AmbariServerConfigurationHandler defaultConfigurationHandler;

    @com.google.inject.Inject
    private org.apache.ambari.server.controller.internal.AmbariServerLDAPConfigurationHandler ldapConfigurationHandler;

    @com.google.inject.Inject
    private org.apache.ambari.server.controller.internal.AmbariServerSSOConfigurationHandler ssoConfigurationHandler;

    @com.google.inject.Inject
    private org.apache.ambari.server.controller.internal.AmbariServerConfigurationHandler tproxyConfigurationHandler;

    public org.apache.ambari.server.controller.internal.RootServiceComponentConfigurationHandler getInstance(java.lang.String serviceName, java.lang.String componentName, java.lang.String categoryName) {
        if (org.apache.ambari.server.controller.RootService.AMBARI.name().equals(serviceName)) {
            if (org.apache.ambari.server.controller.RootComponent.AMBARI_SERVER.name().equals(componentName)) {
                if (org.apache.ambari.server.configuration.AmbariServerConfigurationCategory.LDAP_CONFIGURATION.getCategoryName().equals(categoryName)) {
                    return ldapConfigurationHandler;
                } else if (org.apache.ambari.server.configuration.AmbariServerConfigurationCategory.SSO_CONFIGURATION.getCategoryName().equals(categoryName)) {
                    return ssoConfigurationHandler;
                } else if (org.apache.ambari.server.configuration.AmbariServerConfigurationCategory.TPROXY_CONFIGURATION.getCategoryName().equals(categoryName)) {
                    return tproxyConfigurationHandler;
                } else {
                    return defaultConfigurationHandler;
                }
            }
        }
        return null;
    }
}