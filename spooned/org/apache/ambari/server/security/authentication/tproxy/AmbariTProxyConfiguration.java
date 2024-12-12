package org.apache.ambari.server.security.authentication.tproxy;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
public class AmbariTProxyConfiguration extends org.apache.ambari.server.configuration.AmbariServerConfiguration {
    private static final java.lang.String TEMPLATE_PROXY_USER_ALLOWED_HOSTS = "ambari.tproxy.proxyuser.%s.hosts";

    private static final java.lang.String TEMPLATE_PROXY_USER_ALLOWED_USERS = "ambari.tproxy.proxyuser.%s.users";

    private static final java.lang.String TEMPLATE_PROXY_USER_ALLOWED_GROUPS = "ambari.tproxy.proxyuser.%s.groups";

    AmbariTProxyConfiguration(java.util.Map<java.lang.String, java.lang.String> configurationMap) {
        super(configurationMap);
    }

    @java.lang.Override
    protected org.apache.ambari.server.configuration.AmbariServerConfigurationCategory getCategory() {
        return org.apache.ambari.server.configuration.AmbariServerConfigurationCategory.TPROXY_CONFIGURATION;
    }

    public boolean isEnabled() {
        return java.lang.Boolean.valueOf(getValue(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.TPROXY_AUTHENTICATION_ENABLED, configurationMap));
    }

    public java.lang.String getAllowedHosts(java.lang.String proxyUser) {
        return getValue(java.lang.String.format(org.apache.ambari.server.security.authentication.tproxy.AmbariTProxyConfiguration.TEMPLATE_PROXY_USER_ALLOWED_HOSTS, proxyUser), configurationMap, org.apache.ambari.server.configuration.AmbariServerConfigurationKey.TPROXY_ALLOWED_HOSTS.getDefaultValue());
    }

    public java.lang.String getAllowedUsers(java.lang.String proxyUser) {
        return getValue(java.lang.String.format(org.apache.ambari.server.security.authentication.tproxy.AmbariTProxyConfiguration.TEMPLATE_PROXY_USER_ALLOWED_USERS, proxyUser), configurationMap, org.apache.ambari.server.configuration.AmbariServerConfigurationKey.TPROXY_ALLOWED_USERS.getDefaultValue());
    }

    public java.lang.String getAllowedGroups(java.lang.String proxyUser) {
        return getValue(java.lang.String.format(org.apache.ambari.server.security.authentication.tproxy.AmbariTProxyConfiguration.TEMPLATE_PROXY_USER_ALLOWED_GROUPS, proxyUser), configurationMap, org.apache.ambari.server.configuration.AmbariServerConfigurationKey.TPROXY_ALLOWED_GROUPS.getDefaultValue());
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        return new org.apache.commons.lang.builder.EqualsBuilder().append(configurationMap, ((org.apache.ambari.server.security.authentication.tproxy.AmbariTProxyConfiguration) (o)).configurationMap).isEquals();
    }

    @java.lang.Override
    public int hashCode() {
        return new org.apache.commons.lang.builder.HashCodeBuilder(17, 37).append(configurationMap).toHashCode();
    }
}