package org.apache.ambari.server.ldap.domain;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
public class AmbariLdapConfiguration extends org.apache.ambari.server.configuration.AmbariServerConfiguration {
    public AmbariLdapConfiguration() {
        this(null);
    }

    public AmbariLdapConfiguration(java.util.Map<java.lang.String, java.lang.String> configurationMap) {
        super(configurationMap);
    }

    @java.lang.Override
    protected org.apache.ambari.server.configuration.AmbariServerConfigurationCategory getCategory() {
        return org.apache.ambari.server.configuration.AmbariServerConfigurationCategory.LDAP_CONFIGURATION;
    }

    public boolean isAmbariManagesLdapConfiguration() {
        return java.lang.Boolean.valueOf(configValue(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.AMBARI_MANAGES_LDAP_CONFIGURATION));
    }

    public java.lang.String getLdapEnabledServices() {
        return configValue(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.LDAP_ENABLED_SERVICES);
    }

    public boolean ldapEnabled() {
        return java.lang.Boolean.valueOf(configValue(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.LDAP_ENABLED));
    }

    public java.lang.String serverHost() {
        return configValue(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.SERVER_HOST);
    }

    public int serverPort() {
        return java.lang.Integer.parseInt(configValue(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.SERVER_PORT));
    }

    public java.lang.String serverUrl() {
        return (serverHost() + ":") + serverPort();
    }

    public java.lang.String secondaryServerHost() {
        return configValue(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.SECONDARY_SERVER_HOST);
    }

    public int secondaryServerPort() {
        final java.lang.String secondaryServerPort = configValue(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.SECONDARY_SERVER_PORT);
        return secondaryServerPort == null ? 0 : java.lang.Integer.parseInt(secondaryServerPort);
    }

    public java.lang.String secondaryServerUrl() {
        return (secondaryServerHost() + ":") + secondaryServerPort();
    }

    public boolean useSSL() {
        return java.lang.Boolean.valueOf(configValue(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.USE_SSL));
    }

    public java.lang.String trustStore() {
        return configValue(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.TRUST_STORE);
    }

    public java.lang.String trustStoreType() {
        return configValue(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.TRUST_STORE_TYPE);
    }

    public java.lang.String trustStorePath() {
        return configValue(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.TRUST_STORE_PATH);
    }

    public java.lang.String trustStorePassword() {
        return configValue(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.TRUST_STORE_PASSWORD);
    }

    public boolean anonymousBind() {
        return java.lang.Boolean.valueOf(configValue(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.ANONYMOUS_BIND));
    }

    public java.lang.String bindDn() {
        return configValue(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.BIND_DN);
    }

    public java.lang.String bindPassword() {
        return configValue(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.BIND_PASSWORD);
    }

    public java.lang.String attributeDetection() {
        return configValue(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.ATTR_DETECTION);
    }

    public java.lang.String dnAttribute() {
        return configValue(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.DN_ATTRIBUTE);
    }

    public java.lang.String userObjectClass() {
        return configValue(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.USER_OBJECT_CLASS);
    }

    public java.lang.String userNameAttribute() {
        return configValue(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.USER_NAME_ATTRIBUTE);
    }

    public java.lang.String userSearchBase() {
        return configValue(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.USER_SEARCH_BASE);
    }

    public java.lang.String groupObjectClass() {
        return configValue(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.GROUP_OBJECT_CLASS);
    }

    public java.lang.String groupNameAttribute() {
        return configValue(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.GROUP_NAME_ATTRIBUTE);
    }

    public java.lang.String groupMemberAttribute() {
        return configValue(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.GROUP_MEMBER_ATTRIBUTE);
    }

    public java.lang.String groupSearchBase() {
        return configValue(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.GROUP_SEARCH_BASE);
    }

    public java.lang.String groupMappingRules() {
        return configValue(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.GROUP_MAPPING_RULES);
    }

    public java.lang.String userSearchFilter() {
        return configValue(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.USER_SEARCH_FILTER);
    }

    public java.lang.String userMemberReplacePattern() {
        return configValue(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.USER_MEMBER_REPLACE_PATTERN);
    }

    public java.lang.String userMemberFilter() {
        return configValue(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.USER_MEMBER_FILTER);
    }

    public java.lang.String groupSearchFilter() {
        return configValue(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.GROUP_SEARCH_FILTER);
    }

    public java.lang.String groupMemberReplacePattern() {
        return configValue(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.GROUP_MEMBER_REPLACE_PATTERN);
    }

    public java.lang.String groupMemberFilter() {
        return configValue(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.GROUP_MEMBER_FILTER);
    }

    public boolean forceLowerCaseUserNames() {
        return java.lang.Boolean.valueOf(configValue(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.FORCE_LOWERCASE_USERNAMES));
    }

    public boolean paginationEnabled() {
        return java.lang.Boolean.valueOf(configValue(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.PAGINATION_ENABLED));
    }

    public java.lang.String referralHandling() {
        return configValue(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.REFERRAL_HANDLING);
    }

    public boolean disableEndpointIdentification() {
        return java.lang.Boolean.valueOf(configValue(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.DISABLE_ENDPOINT_IDENTIFICATION));
    }

    @java.lang.Override
    public java.util.Map<java.lang.String, java.lang.String> toMap() {
        return new java.util.HashMap<>(configurationMap);
    }

    @java.lang.Override
    public java.lang.String toString() {
        return configurationMap.toString();
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if ((o == null) || (getClass() != o.getClass())) {
            return false;
        }
        org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration that = ((org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration) (o));
        return new org.apache.commons.lang.builder.EqualsBuilder().append(configurationMap, that.configurationMap).isEquals();
    }

    @java.lang.Override
    public int hashCode() {
        return new org.apache.commons.lang.builder.HashCodeBuilder(17, 37).append(configurationMap).toHashCode();
    }

    public boolean isLdapAlternateUserSearchEnabled() {
        return java.lang.Boolean.valueOf(configValue(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.ALTERNATE_USER_SEARCH_ENABLED));
    }

    public org.apache.ambari.server.security.authorization.LdapServerProperties getLdapServerProperties() {
        final org.apache.ambari.server.security.authorization.LdapServerProperties ldapServerProperties = new org.apache.ambari.server.security.authorization.LdapServerProperties();
        ldapServerProperties.setPrimaryUrl(serverUrl());
        if (org.apache.commons.lang.StringUtils.isNotBlank(secondaryServerHost())) {
            ldapServerProperties.setSecondaryUrl(secondaryServerUrl());
        }
        ldapServerProperties.setUseSsl(java.lang.Boolean.parseBoolean(configValue(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.USE_SSL)));
        ldapServerProperties.setAnonymousBind(java.lang.Boolean.parseBoolean(configValue(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.ANONYMOUS_BIND)));
        ldapServerProperties.setManagerDn(configValue(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.BIND_DN));
        ldapServerProperties.setManagerPassword(configValue(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.BIND_PASSWORD));
        ldapServerProperties.setBaseDN(configValue(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.USER_SEARCH_BASE));
        ldapServerProperties.setUsernameAttribute(configValue(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.USER_NAME_ATTRIBUTE));
        ldapServerProperties.setForceUsernameToLowercase(java.lang.Boolean.parseBoolean(configValue(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.FORCE_LOWERCASE_USERNAMES)));
        ldapServerProperties.setUserBase(configValue(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.USER_BASE));
        ldapServerProperties.setUserObjectClass(configValue(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.USER_OBJECT_CLASS));
        ldapServerProperties.setDnAttribute(configValue(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.DN_ATTRIBUTE));
        ldapServerProperties.setGroupBase(configValue(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.GROUP_BASE));
        ldapServerProperties.setGroupObjectClass(configValue(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.GROUP_OBJECT_CLASS));
        ldapServerProperties.setGroupMembershipAttr(configValue(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.GROUP_MEMBER_ATTRIBUTE));
        ldapServerProperties.setGroupNamingAttr(configValue(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.GROUP_NAME_ATTRIBUTE));
        ldapServerProperties.setAdminGroupMappingRules(configValue(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.GROUP_MAPPING_RULES));
        ldapServerProperties.setAdminGroupMappingMemberAttr("");
        ldapServerProperties.setUserSearchFilter(configValue(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.USER_SEARCH_FILTER));
        ldapServerProperties.setAlternateUserSearchFilter(configValue(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.ALTERNATE_USER_SEARCH_FILTER));
        ldapServerProperties.setGroupSearchFilter(configValue(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.GROUP_SEARCH_FILTER));
        ldapServerProperties.setReferralMethod(configValue(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.REFERRAL_HANDLING));
        ldapServerProperties.setSyncUserMemberReplacePattern(configValue(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.USER_MEMBER_REPLACE_PATTERN));
        ldapServerProperties.setSyncGroupMemberReplacePattern(configValue(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.GROUP_MEMBER_REPLACE_PATTERN));
        ldapServerProperties.setSyncUserMemberFilter(configValue(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.USER_MEMBER_FILTER));
        ldapServerProperties.setSyncGroupMemberFilter(configValue(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.GROUP_MEMBER_FILTER));
        ldapServerProperties.setPaginationEnabled(java.lang.Boolean.parseBoolean(configValue(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.PAGINATION_ENABLED)));
        ldapServerProperties.setDisableEndpointIdentification(disableEndpointIdentification());
        if (hasAnyValueWithKey(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.GROUP_BASE, org.apache.ambari.server.configuration.AmbariServerConfigurationKey.GROUP_OBJECT_CLASS, org.apache.ambari.server.configuration.AmbariServerConfigurationKey.GROUP_MEMBER_ATTRIBUTE, org.apache.ambari.server.configuration.AmbariServerConfigurationKey.GROUP_NAME_ATTRIBUTE, org.apache.ambari.server.configuration.AmbariServerConfigurationKey.GROUP_MAPPING_RULES, org.apache.ambari.server.configuration.AmbariServerConfigurationKey.GROUP_SEARCH_FILTER)) {
            ldapServerProperties.setGroupMappingEnabled(true);
        }
        return ldapServerProperties;
    }

    private boolean hasAnyValueWithKey(org.apache.ambari.server.configuration.AmbariServerConfigurationKey... ambariServerConfigurationKey) {
        for (org.apache.ambari.server.configuration.AmbariServerConfigurationKey key : ambariServerConfigurationKey) {
            if (configurationMap.containsKey(key.key())) {
                return true;
            }
        }
        return false;
    }

    public org.apache.ambari.server.configuration.LdapUsernameCollisionHandlingBehavior syncCollisionHandlingBehavior() {
        if ("skip".equalsIgnoreCase(configValue(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.COLLISION_BEHAVIOR))) {
            return org.apache.ambari.server.configuration.LdapUsernameCollisionHandlingBehavior.SKIP;
        }
        return org.apache.ambari.server.configuration.LdapUsernameCollisionHandlingBehavior.CONVERT;
    }

    private java.lang.String configValue(org.apache.ambari.server.configuration.AmbariServerConfigurationKey ambariManagesLdapConfiguration) {
        return getValue(ambariManagesLdapConfiguration, configurationMap);
    }
}