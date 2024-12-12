package org.apache.ambari.server.security.authorization;
import org.apache.commons.lang.StringUtils;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.ContextSource;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.BaseLdapPathContextSource;
import org.springframework.ldap.support.LdapUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.ldap.authentication.AbstractLdapAuthenticator;
import org.springframework.security.ldap.search.LdapUserSearch;
public class AmbariLdapBindAuthenticator extends org.springframework.security.ldap.authentication.AbstractLdapAuthenticator {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.security.authorization.AmbariLdapBindAuthenticator.class);

    private static final java.lang.String AMBARI_ADMIN_LDAP_ATTRIBUTE_KEY = "ambari_admin";

    private final org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration ldapConfiguration;

    public AmbariLdapBindAuthenticator(org.springframework.ldap.core.support.BaseLdapPathContextSource contextSource, org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration ldapConfiguration) {
        super(contextSource);
        this.ldapConfiguration = ldapConfiguration;
    }

    @java.lang.Override
    public org.springframework.ldap.core.DirContextOperations authenticate(org.springframework.security.core.Authentication authentication) {
        if (!(authentication instanceof org.springframework.security.authentication.UsernamePasswordAuthenticationToken)) {
            org.apache.ambari.server.security.authorization.AmbariLdapBindAuthenticator.LOG.info("Unexpected authentication token type encountered ({}) - failing authentication.", authentication.getClass().getName());
            throw new org.springframework.security.authentication.BadCredentialsException("Unexpected authentication token type encountered.");
        }
        org.springframework.ldap.core.DirContextOperations user = authenticate(((org.springframework.security.authentication.UsernamePasswordAuthenticationToken) (authentication)));
        org.apache.ambari.server.security.authorization.LdapServerProperties ldapServerProperties = ldapConfiguration.getLdapServerProperties();
        if (org.apache.commons.lang.StringUtils.isNotEmpty(ldapServerProperties.getAdminGroupMappingRules())) {
            setAmbariAdminAttr(user, ldapServerProperties);
        }
        java.lang.String ldapUserName = user.getStringAttribute(ldapServerProperties.getUsernameAttribute());
        java.lang.String loginName = authentication.getName();
        if (ldapUserName == null) {
            org.apache.ambari.server.security.authorization.AmbariLdapBindAuthenticator.LOG.warn("The user data does not contain a value for {}.", ldapServerProperties.getUsernameAttribute());
        } else if (ldapUserName.isEmpty()) {
            org.apache.ambari.server.security.authorization.AmbariLdapBindAuthenticator.LOG.warn("The user data contains an empty value for {}.", ldapServerProperties.getUsernameAttribute());
        } else {
            org.apache.ambari.server.security.authorization.AmbariLdapBindAuthenticator.LOG.info("User with {}='{}' logged in with login alias '{}'", ldapServerProperties.getUsernameAttribute(), ldapUserName, loginName);
            java.lang.String processedLdapUserName;
            if (ldapServerProperties.isForceUsernameToLowercase()) {
                processedLdapUserName = ldapUserName.toLowerCase();
                org.apache.ambari.server.security.authorization.AmbariLdapBindAuthenticator.LOG.info("Forcing ldap username to be lowercase characters: {} ==> {}", ldapUserName, processedLdapUserName);
            } else {
                processedLdapUserName = ldapUserName;
            }
            if (!processedLdapUserName.equals(loginName.toLowerCase())) {
                org.apache.ambari.server.security.authorization.AuthorizationHelper.addLoginNameAlias(processedLdapUserName, loginName.toLowerCase());
            }
        }
        return user;
    }

    private org.springframework.ldap.core.DirContextOperations authenticate(org.springframework.security.authentication.UsernamePasswordAuthenticationToken authentication) {
        org.springframework.ldap.core.DirContextOperations user = null;
        java.lang.String username = authentication.getName();
        java.lang.Object credentials = authentication.getCredentials();
        java.lang.String password = (credentials instanceof java.lang.String) ? ((java.lang.String) (credentials)) : null;
        if (org.apache.commons.lang.StringUtils.isEmpty(username)) {
            org.apache.ambari.server.security.authorization.AmbariLdapBindAuthenticator.LOG.debug("Empty username encountered - failing authentication.");
            throw new org.springframework.security.authentication.BadCredentialsException("Empty username encountered.");
        }
        org.apache.ambari.server.security.authorization.AmbariLdapBindAuthenticator.LOG.debug("Authenticating {}", username);
        if (org.apache.commons.lang.StringUtils.isEmpty(password)) {
            org.apache.ambari.server.security.authorization.AmbariLdapBindAuthenticator.LOG.debug("Empty password encountered - failing authentication.");
            throw new org.springframework.security.authentication.BadCredentialsException("Empty password encountered.");
        }
        org.springframework.security.ldap.search.LdapUserSearch userSearch = getUserSearch();
        if (userSearch == null) {
            org.apache.ambari.server.security.authorization.AmbariLdapBindAuthenticator.LOG.debug("The user search facility has not been set - failing authentication.");
            throw new org.springframework.security.authentication.BadCredentialsException("The user search facility has not been set.");
        } else {
            if (org.apache.ambari.server.security.authorization.AmbariLdapBindAuthenticator.LOG.isTraceEnabled()) {
                org.apache.ambari.server.security.authorization.AmbariLdapBindAuthenticator.LOG.trace("Searching for user with username {}: {}", username, userSearch);
            }
            org.springframework.ldap.core.DirContextOperations userFromSearch = userSearch.searchForUser(username);
            if (userFromSearch == null) {
                org.apache.ambari.server.security.authorization.AmbariLdapBindAuthenticator.LOG.debug("LDAP user object not found for {}", username);
            } else {
                org.apache.ambari.server.security.authorization.AmbariLdapBindAuthenticator.LOG.debug("Found LDAP user for {}: {}", username, userFromSearch.getDn());
                user = bind(userFromSearch, password);
                if (org.apache.ambari.server.security.authorization.AmbariLdapBindAuthenticator.LOG.isTraceEnabled()) {
                    javax.naming.directory.Attributes attributes = user.getAttributes();
                    if (attributes != null) {
                        java.lang.StringBuilder builder = new java.lang.StringBuilder();
                        javax.naming.NamingEnumeration<java.lang.String> ids = attributes.getIDs();
                        try {
                            while (ids.hasMore()) {
                                java.lang.String id = ids.next();
                                builder.append("\n\t");
                                builder.append(attributes.get(id));
                            } 
                        } catch (javax.naming.NamingException e) {
                        }
                        org.apache.ambari.server.security.authorization.AmbariLdapBindAuthenticator.LOG.trace("User Attributes: {}", builder);
                    } else {
                        org.apache.ambari.server.security.authorization.AmbariLdapBindAuthenticator.LOG.trace("User Attributes: not available");
                    }
                }
            }
        }
        if (user == null) {
            org.apache.ambari.server.security.authorization.AmbariLdapBindAuthenticator.LOG.debug("Invalid credentials for {} - failing authentication.", username);
            throw new org.springframework.security.authentication.BadCredentialsException("Invalid credentials.");
        } else {
            org.apache.ambari.server.security.authorization.AmbariLdapBindAuthenticator.LOG.debug("Successfully authenticated {}", username);
        }
        return user;
    }

    private org.springframework.ldap.core.DirContextOperations bind(org.springframework.ldap.core.DirContextOperations user, java.lang.String password) {
        org.springframework.ldap.core.ContextSource contextSource = getContextSource();
        if (contextSource == null) {
            java.lang.String message = "Missing ContextSource - failing authentication.";
            org.apache.ambari.server.security.authorization.AmbariLdapBindAuthenticator.LOG.debug(message);
            throw new org.springframework.security.authentication.InternalAuthenticationServiceException(message);
        }
        if (!(contextSource instanceof org.springframework.ldap.core.support.BaseLdapPathContextSource)) {
            java.lang.String message = java.lang.String.format("Unexpected ContextSource type (%s) - failing authentication.", contextSource.getClass().getName());
            org.apache.ambari.server.security.authorization.AmbariLdapBindAuthenticator.LOG.debug(message);
            throw new org.springframework.security.authentication.InternalAuthenticationServiceException(message);
        }
        org.springframework.ldap.core.support.BaseLdapPathContextSource baseLdapPathContextSource = ((org.springframework.ldap.core.support.BaseLdapPathContextSource) (contextSource));
        javax.naming.Name userDistinguishedName = user.getDn();
        javax.naming.Name fullDn = org.apache.ambari.server.security.authorization.AmbariLdapUtils.getFullDn(userDistinguishedName, baseLdapPathContextSource.getBaseLdapName());
        org.apache.ambari.server.security.authorization.AmbariLdapBindAuthenticator.LOG.debug("Attempting to bind as {}", fullDn);
        javax.naming.directory.DirContext dirContext = null;
        try {
            dirContext = baseLdapPathContextSource.getContext(fullDn.toString(), password);
            return new org.springframework.ldap.core.DirContextAdapter(user.getAttributes(), userDistinguishedName, baseLdapPathContextSource.getBaseLdapName());
        } catch (org.springframework.ldap.AuthenticationException e) {
            java.lang.String message = java.lang.String.format("Failed to bind as %s - %s", user.getDn().toString(), e.getMessage());
            if (org.apache.ambari.server.security.authorization.AmbariLdapBindAuthenticator.LOG.isTraceEnabled()) {
                org.apache.ambari.server.security.authorization.AmbariLdapBindAuthenticator.LOG.trace(message, e);
            } else if (org.apache.ambari.server.security.authorization.AmbariLdapBindAuthenticator.LOG.isDebugEnabled()) {
                org.apache.ambari.server.security.authorization.AmbariLdapBindAuthenticator.LOG.debug(message);
            }
            throw new org.springframework.security.authentication.BadCredentialsException("The username or password is incorrect.");
        } finally {
            org.springframework.ldap.support.LdapUtils.closeContext(dirContext);
        }
    }

    private org.springframework.ldap.core.DirContextOperations setAmbariAdminAttr(org.springframework.ldap.core.DirContextOperations user, org.apache.ambari.server.security.authorization.LdapServerProperties ldapServerProperties) {
        java.lang.String baseDn = ldapServerProperties.getBaseDN().toLowerCase();
        java.lang.String groupBase = ldapServerProperties.getGroupBase().toLowerCase();
        final java.lang.String groupNamingAttribute = ldapServerProperties.getGroupNamingAttr();
        final java.lang.String adminGroupMappingMemberAttr = ldapServerProperties.getAdminGroupMappingMemberAttr();
        int indexOfBaseDn = groupBase.indexOf(baseDn);
        groupBase = (indexOfBaseDn <= 0) ? "" : groupBase.substring(0, indexOfBaseDn - 1);
        java.lang.String memberValue = (org.apache.commons.lang.StringUtils.isNotEmpty(adminGroupMappingMemberAttr)) ? user.getStringAttribute(adminGroupMappingMemberAttr) : user.getNameInNamespace();
        org.apache.ambari.server.security.authorization.AmbariLdapBindAuthenticator.LOG.debug("LDAP login - set '{}' as member attribute for adminGroupMappingRules", memberValue);
        java.lang.String setAmbariAdminAttrFilter = resolveAmbariAdminAttrFilter(ldapServerProperties, memberValue);
        org.apache.ambari.server.security.authorization.AmbariLdapBindAuthenticator.LOG.debug("LDAP login - set admin attr filter: {}", setAmbariAdminAttrFilter);
        org.springframework.ldap.core.AttributesMapper attributesMapper = attrs -> attrs.get(groupNamingAttribute).get();
        org.springframework.ldap.core.LdapTemplate ldapTemplate = new org.springframework.ldap.core.LdapTemplate(getContextSource());
        ldapTemplate.setIgnorePartialResultException(true);
        ldapTemplate.setIgnoreNameNotFoundException(true);
        @java.lang.SuppressWarnings("unchecked")
        java.util.List<java.lang.String> ambariAdminGroups = ldapTemplate.search(groupBase, setAmbariAdminAttrFilter, attributesMapper);
        if (ambariAdminGroups.size() > 0) {
            user.setAttributeValue(org.apache.ambari.server.security.authorization.AmbariLdapBindAuthenticator.AMBARI_ADMIN_LDAP_ATTRIBUTE_KEY, true);
        }
        return user;
    }

    private java.lang.String resolveAmbariAdminAttrFilter(org.apache.ambari.server.security.authorization.LdapServerProperties ldapServerProperties, java.lang.String memberValue) {
        java.lang.String groupMembershipAttr = ldapServerProperties.getGroupMembershipAttr();
        java.lang.String groupObjectClass = ldapServerProperties.getGroupObjectClass();
        java.lang.String adminGroupMappingRules = ldapServerProperties.getAdminGroupMappingRules();
        final java.lang.String groupNamingAttribute = ldapServerProperties.getGroupNamingAttr();
        java.lang.String groupSearchFilter = ldapServerProperties.getGroupSearchFilter();
        java.lang.String setAmbariAdminAttrFilter;
        if (org.apache.commons.lang.StringUtils.isEmpty(groupSearchFilter)) {
            java.lang.String adminGroupMappingRegex = createAdminGroupMappingRegex(adminGroupMappingRules, groupNamingAttribute);
            setAmbariAdminAttrFilter = java.lang.String.format("(&(%s=%s)(objectclass=%s)(|%s))", groupMembershipAttr, memberValue, groupObjectClass, adminGroupMappingRegex);
        } else {
            setAmbariAdminAttrFilter = java.lang.String.format("(&(%s=%s)%s)", groupMembershipAttr, memberValue, groupSearchFilter);
        }
        return setAmbariAdminAttrFilter;
    }

    private java.lang.String createAdminGroupMappingRegex(java.lang.String adminGroupMappingRules, java.lang.String groupNamingAttribute) {
        java.lang.String[] adminGroupMappingRegexs = adminGroupMappingRules.split(",");
        java.lang.StringBuilder builder = new java.lang.StringBuilder("");
        for (java.lang.String adminGroupMappingRegex : adminGroupMappingRegexs) {
            builder.append(java.lang.String.format("(%s=%s)", groupNamingAttribute, adminGroupMappingRegex));
        }
        return builder.toString();
    }
}