package org.apache.ambari.server.ldap.service.ads;
import org.apache.directory.api.ldap.model.constants.SchemaConstants;
import org.apache.directory.api.ldap.model.entry.Entry;
import org.apache.directory.api.ldap.model.exception.LdapException;
import org.apache.directory.api.ldap.model.message.SearchRequest;
import org.apache.directory.api.ldap.model.message.SearchScope;
import org.apache.directory.api.ldap.model.name.Dn;
import org.apache.directory.ldap.client.api.LdapConnection;
import org.apache.directory.ldap.client.api.search.FilterBuilder;
import org.apache.directory.ldap.client.template.ConnectionCallback;
import org.apache.directory.ldap.client.template.EntryMapper;
import org.apache.directory.ldap.client.template.LdapConnectionTemplate;
@javax.inject.Singleton
public class DefaultLdapConfigurationService implements org.apache.ambari.server.ldap.service.LdapConfigurationService {
    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.ldap.service.ads.DefaultLdapConfigurationService.class);

    @javax.inject.Inject
    private org.apache.ambari.server.ldap.service.ads.LdapConnectionTemplateFactory ldapConnectionTemplateFactory;

    @javax.inject.Inject
    public DefaultLdapConfigurationService() {
    }

    @java.lang.Override
    public void checkConnection(org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration ambariLdapConfiguration) throws org.apache.ambari.server.ldap.service.AmbariLdapException {
        org.apache.ambari.server.ldap.service.ads.DefaultLdapConfigurationService.LOGGER.info("Trying to connect to the LDAP server using provided configuration...");
        org.apache.directory.ldap.client.template.LdapConnectionTemplate ldapConnectionTemplate = ldapConnectionTemplateFactory.create(ambariLdapConfiguration);
        java.lang.Boolean isConnected = ldapConnectionTemplate.execute(new org.apache.directory.ldap.client.template.ConnectionCallback<java.lang.Boolean>() {
            @java.lang.Override
            public java.lang.Boolean doWithConnection(org.apache.directory.ldap.client.api.LdapConnection connection) throws org.apache.directory.api.ldap.model.exception.LdapException {
                return connection.isConnected() && connection.isAuthenticated();
            }
        });
        if (!isConnected) {
            org.apache.ambari.server.ldap.service.ads.DefaultLdapConfigurationService.LOGGER.error("Could not connect to the LDAP server");
            throw new org.apache.ambari.server.ldap.service.AmbariLdapException("Could not connect to the LDAP server. Configuration: " + ambariLdapConfiguration);
        }
        org.apache.ambari.server.ldap.service.ads.DefaultLdapConfigurationService.LOGGER.info("Successfully conencted to the LDAP.");
    }

    @java.lang.Override
    public java.lang.String checkUserAttributes(java.lang.String testUserName, java.lang.String testPassword, org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration ambariLdapConfiguration) throws org.apache.ambari.server.ldap.service.AmbariLdapException {
        java.lang.String userDn;
        try {
            org.apache.ambari.server.ldap.service.ads.DefaultLdapConfigurationService.LOGGER.info("Checking user attributes for user [{}] ...", testUserName);
            java.lang.String filter = org.apache.directory.ldap.client.api.search.FilterBuilder.and(org.apache.directory.ldap.client.api.search.FilterBuilder.equal(SchemaConstants.OBJECT_CLASS_AT, ambariLdapConfiguration.userObjectClass()), org.apache.directory.ldap.client.api.search.FilterBuilder.equal(ambariLdapConfiguration.userNameAttribute(), testUserName)).toString();
            org.apache.ambari.server.ldap.service.ads.DefaultLdapConfigurationService.LOGGER.info("Searching for the user: [{}] using the search filter: [{}]", testUserName, filter);
            userDn = ldapConnectionTemplateFactory.create(ambariLdapConfiguration).searchFirst(new org.apache.directory.api.ldap.model.name.Dn(ambariLdapConfiguration.userSearchBase()), filter, SearchScope.SUBTREE, getUserDnNameEntryMapper(ambariLdapConfiguration));
            if (null == userDn) {
                org.apache.ambari.server.ldap.service.ads.DefaultLdapConfigurationService.LOGGER.info("Could not find test user based on the provided configuration. User attributes may not be complete or the user may not exist.");
                throw new org.apache.ambari.server.ldap.service.AmbariLdapException("Could not find test user based on the provided configuration. User attributes may not be complete or the user may not exist.");
            }
            org.apache.ambari.server.ldap.service.ads.DefaultLdapConfigurationService.LOGGER.info("Attribute validation succeeded. Filter: [{}]", filter);
        } catch (java.lang.Exception e) {
            org.apache.ambari.server.ldap.service.ads.DefaultLdapConfigurationService.LOGGER.error("User attributes validation failed.", e);
            throw new org.apache.ambari.server.ldap.service.AmbariLdapException(e.getMessage(), e);
        }
        return userDn;
    }

    @java.lang.Override
    public java.util.Set<java.lang.String> checkGroupAttributes(java.lang.String userDn, org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration ambariLdapConfiguration) throws org.apache.ambari.server.ldap.service.AmbariLdapException {
        java.util.List<java.lang.String> groups;
        try {
            org.apache.ambari.server.ldap.service.ads.DefaultLdapConfigurationService.LOGGER.info("Checking group attributes for user dn: [{}] ...", userDn);
            java.lang.String filter = org.apache.directory.ldap.client.api.search.FilterBuilder.and(org.apache.directory.ldap.client.api.search.FilterBuilder.equal(SchemaConstants.OBJECT_CLASS_AT, ambariLdapConfiguration.groupObjectClass()), org.apache.directory.ldap.client.api.search.FilterBuilder.equal(ambariLdapConfiguration.groupMemberAttribute(), userDn)).toString();
            org.apache.ambari.server.ldap.service.ads.DefaultLdapConfigurationService.LOGGER.info("Searching for the groups the user dn: [{}] is member of using the search filter: [{}]", userDn, filter);
            org.apache.directory.ldap.client.template.LdapConnectionTemplate ldapConnectionTemplate = ldapConnectionTemplateFactory.create(ambariLdapConfiguration);
            org.apache.directory.api.ldap.model.message.SearchRequest searchRequest = ldapConnectionTemplate.newSearchRequest(new org.apache.directory.api.ldap.model.name.Dn(ambariLdapConfiguration.groupSearchBase()), filter, SearchScope.SUBTREE);
            searchRequest.addAttributes(ambariLdapConfiguration.groupMemberAttribute(), ambariLdapConfiguration.groupNameAttribute());
            groups = ldapConnectionTemplate.search(searchRequest, getGroupNameEntryMapper(ambariLdapConfiguration));
            if ((groups == null) || groups.isEmpty()) {
                org.apache.ambari.server.ldap.service.ads.DefaultLdapConfigurationService.LOGGER.info("No groups found for the user dn. Group attributes configuration is incomplete");
                throw new org.apache.ambari.server.ldap.service.AmbariLdapException("Group attribute ldap configuration is incomplete");
            }
            org.apache.ambari.server.ldap.service.ads.DefaultLdapConfigurationService.LOGGER.info("Group attribute configuration check succeeded.");
        } catch (java.lang.Exception e) {
            org.apache.ambari.server.ldap.service.ads.DefaultLdapConfigurationService.LOGGER.error("User attributes validation failed.", e);
            throw new org.apache.ambari.server.ldap.service.AmbariLdapException(e.getMessage(), e);
        }
        return new java.util.HashSet<>(groups);
    }

    private org.apache.directory.ldap.client.template.EntryMapper<java.lang.String> getGroupNameEntryMapper(org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration ambariLdapConfiguration) {
        org.apache.directory.ldap.client.template.EntryMapper<java.lang.String> entryMapper = new org.apache.directory.ldap.client.template.EntryMapper<java.lang.String>() {
            @java.lang.Override
            public java.lang.String map(org.apache.directory.api.ldap.model.entry.Entry entry) throws org.apache.directory.api.ldap.model.exception.LdapException {
                return entry.get(ambariLdapConfiguration.groupNameAttribute()).get().getValue();
            }
        };
        return entryMapper;
    }

    private org.apache.directory.ldap.client.template.EntryMapper<java.lang.String> getUserDnNameEntryMapper(org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration ambariLdapConfiguration) {
        org.apache.directory.ldap.client.template.EntryMapper<java.lang.String> entryMapper = new org.apache.directory.ldap.client.template.EntryMapper<java.lang.String>() {
            @java.lang.Override
            public java.lang.String map(org.apache.directory.api.ldap.model.entry.Entry entry) throws org.apache.directory.api.ldap.model.exception.LdapException {
                return entry.getDn().getNormName();
            }
        };
        return entryMapper;
    }
}