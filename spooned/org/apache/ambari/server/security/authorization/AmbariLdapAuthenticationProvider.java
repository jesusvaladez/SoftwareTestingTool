package org.apache.ambari.server.security.authorization;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.ldap.authentication.LdapAuthenticationProvider;
import org.springframework.security.ldap.search.FilterBasedLdapUserSearch;
import org.springframework.security.ldap.userdetails.LdapUserDetails;
public class AmbariLdapAuthenticationProvider extends org.apache.ambari.server.security.authentication.AmbariAuthenticationProvider {
    private static final java.lang.String SYSTEM_PROPERTY_DISABLE_ENDPOINT_IDENTIFICATION = "com.sun.jndi.ldap.object.disableEndpointIdentification";

    private static org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.security.authorization.AmbariLdapAuthenticationProvider.class);

    final org.apache.ambari.server.ldap.service.AmbariLdapConfigurationProvider ldapConfigurationProvider;

    private org.apache.ambari.server.security.authorization.AmbariLdapAuthoritiesPopulator authoritiesPopulator;

    private java.lang.ThreadLocal<org.apache.ambari.server.security.authorization.LdapServerProperties> ldapServerProperties = new java.lang.ThreadLocal<>();

    private java.lang.ThreadLocal<org.springframework.security.ldap.authentication.LdapAuthenticationProvider> providerThreadLocal = new java.lang.ThreadLocal<>();

    private java.lang.ThreadLocal<java.lang.String> ldapUserSearchFilterThreadLocal = new java.lang.ThreadLocal<>();

    @com.google.inject.Inject
    public AmbariLdapAuthenticationProvider(org.apache.ambari.server.security.authorization.Users users, org.apache.ambari.server.configuration.Configuration configuration, org.apache.ambari.server.ldap.service.AmbariLdapConfigurationProvider ldapConfigurationProvider, org.apache.ambari.server.security.authorization.AmbariLdapAuthoritiesPopulator authoritiesPopulator) {
        super(users, configuration);
        this.ldapConfigurationProvider = ldapConfigurationProvider;
        this.authoritiesPopulator = authoritiesPopulator;
    }

    @java.lang.Override
    public org.springframework.security.core.Authentication authenticate(org.springframework.security.core.Authentication authentication) throws org.springframework.security.core.AuthenticationException {
        if (isLdapEnabled()) {
            if (authentication.getName() == null) {
                org.apache.ambari.server.security.authorization.AmbariLdapAuthenticationProvider.LOG.info("Authentication failed: no username provided");
                throw new org.apache.ambari.server.security.authentication.InvalidUsernamePasswordCombinationException("");
            }
            java.lang.String username = authentication.getName().trim();
            if (authentication.getCredentials() == null) {
                org.apache.ambari.server.security.authorization.AmbariLdapAuthenticationProvider.LOG.info("Authentication failed: no credentials provided: {}", username);
                throw new org.apache.ambari.server.security.authentication.InvalidUsernamePasswordCombinationException(username);
            }
            try {
                org.springframework.security.core.Authentication auth = loadLdapAuthenticationProvider(username).authenticate(authentication);
                org.apache.ambari.server.orm.entities.UserEntity userEntity = getUserEntity(auth);
                if (userEntity == null) {
                    org.apache.ambari.server.security.authorization.AmbariLdapAuthenticationProvider.LOG.debug("user not found ('{}')", username);
                    throw new org.apache.ambari.server.security.authentication.InvalidUsernamePasswordCombinationException(username);
                } else {
                    org.apache.ambari.server.security.authorization.Users users = getUsers();
                    try {
                        users.validateLogin(userEntity, username);
                    } catch (org.apache.ambari.server.security.authentication.AccountDisabledException | org.apache.ambari.server.security.authentication.TooManyLoginFailuresException e) {
                        if (getConfiguration().showLockedOutUserMessage()) {
                            throw e;
                        } else {
                            throw new org.apache.ambari.server.security.authentication.InvalidUsernamePasswordCombinationException(username, false, e);
                        }
                    }
                    org.apache.ambari.server.security.authentication.AmbariUserDetails userDetails = new org.apache.ambari.server.security.authentication.AmbariUserDetailsImpl(users.getUser(userEntity), null, users.getUserAuthorities(userEntity));
                    return new org.apache.ambari.server.security.authentication.AmbariUserAuthentication(null, userDetails, true);
                }
            } catch (org.springframework.security.core.AuthenticationException e) {
                org.apache.ambari.server.security.authorization.AmbariLdapAuthenticationProvider.LOG.debug("Got exception during LDAP authentication attempt", e);
                java.lang.Throwable cause = e.getCause();
                if ((cause != null) && (cause != e)) {
                    if (cause instanceof org.springframework.ldap.CommunicationException) {
                        if (org.apache.ambari.server.security.authorization.AmbariLdapAuthenticationProvider.LOG.isDebugEnabled()) {
                            org.apache.ambari.server.security.authorization.AmbariLdapAuthenticationProvider.LOG.warn("Failed to communicate with the LDAP server: " + cause.getMessage(), e);
                        } else {
                            org.apache.ambari.server.security.authorization.AmbariLdapAuthenticationProvider.LOG.warn("Failed to communicate with the LDAP server: " + cause.getMessage());
                        }
                    } else if (cause instanceof org.springframework.ldap.AuthenticationException) {
                        org.apache.ambari.server.security.authorization.AmbariLdapAuthenticationProvider.LOG.warn("Looks like LDAP manager credentials (that are used for " + "connecting to LDAP server) are invalid.", e);
                    }
                }
                throw new org.apache.ambari.server.security.authentication.InvalidUsernamePasswordCombinationException(username, e);
            } catch (org.springframework.dao.IncorrectResultSizeDataAccessException multipleUsersFound) {
                java.lang.String message = (ldapConfigurationProvider.get().isLdapAlternateUserSearchEnabled()) ? java.lang.String.format("Login Failed: Please append your domain to your username and try again.  Example: %s@domain", username) : "Login Failed: More than one user with that username found, please work with your Ambari Administrator to adjust your LDAP configuration";
                throw new org.apache.ambari.server.security.authorization.DuplicateLdapUserFoundAuthenticationException(message);
            }
        } else {
            return null;
        }
    }

    @java.lang.Override
    public boolean supports(java.lang.Class<?> authentication) {
        return org.springframework.security.authentication.UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }

    org.springframework.security.ldap.authentication.LdapAuthenticationProvider loadLdapAuthenticationProvider(java.lang.String userName) {
        boolean ldapConfigPropertiesChanged = reloadLdapServerProperties();
        java.lang.String ldapUserSearchFilter = getLdapUserSearchFilter(userName);
        if (ldapConfigPropertiesChanged || (!ldapUserSearchFilter.equals(ldapUserSearchFilterThreadLocal.get()))) {
            org.apache.ambari.server.security.authorization.AmbariLdapAuthenticationProvider.LOG.info("Either LDAP Properties or user search filter changed - rebuilding Context");
            org.springframework.ldap.core.support.LdapContextSource springSecurityContextSource = new org.springframework.ldap.core.support.LdapContextSource();
            java.util.List<java.lang.String> ldapUrls = ldapServerProperties.get().getLdapUrls();
            springSecurityContextSource.setUrls(ldapUrls.toArray(new java.lang.String[ldapUrls.size()]));
            springSecurityContextSource.setBase(ldapServerProperties.get().getBaseDN());
            if (!ldapServerProperties.get().isAnonymousBind()) {
                springSecurityContextSource.setUserDn(ldapServerProperties.get().getManagerDn());
                springSecurityContextSource.setPassword(ldapServerProperties.get().getManagerPassword());
            }
            if (ldapServerProperties.get().isUseSsl() && ldapServerProperties.get().isDisableEndpointIdentification()) {
                java.lang.System.setProperty(org.apache.ambari.server.security.authorization.AmbariLdapAuthenticationProvider.SYSTEM_PROPERTY_DISABLE_ENDPOINT_IDENTIFICATION, "true");
                org.apache.ambari.server.security.authorization.AmbariLdapAuthenticationProvider.LOG.info("Disabled endpoint identification");
            } else {
                java.lang.System.clearProperty(org.apache.ambari.server.security.authorization.AmbariLdapAuthenticationProvider.SYSTEM_PROPERTY_DISABLE_ENDPOINT_IDENTIFICATION);
                org.apache.ambari.server.security.authorization.AmbariLdapAuthenticationProvider.LOG.info("Removed endpoint identification disabling");
            }
            try {
                springSecurityContextSource.afterPropertiesSet();
            } catch (java.lang.Exception e) {
                org.apache.ambari.server.security.authorization.AmbariLdapAuthenticationProvider.LOG.error("LDAP Context Source not loaded ", e);
                throw new org.springframework.security.core.userdetails.UsernameNotFoundException("LDAP Context Source not loaded", e);
            }
            java.lang.String userSearchBase = ldapServerProperties.get().getUserSearchBase();
            org.springframework.security.ldap.search.FilterBasedLdapUserSearch userSearch = new org.springframework.security.ldap.search.FilterBasedLdapUserSearch(userSearchBase, ldapUserSearchFilter, springSecurityContextSource);
            org.apache.ambari.server.security.authorization.AmbariLdapBindAuthenticator bindAuthenticator = new org.apache.ambari.server.security.authorization.AmbariLdapBindAuthenticator(springSecurityContextSource, ldapConfigurationProvider.get());
            bindAuthenticator.setUserSearch(userSearch);
            org.springframework.security.ldap.authentication.LdapAuthenticationProvider authenticationProvider = new org.springframework.security.ldap.authentication.LdapAuthenticationProvider(bindAuthenticator, authoritiesPopulator);
            providerThreadLocal.set(authenticationProvider);
        }
        ldapUserSearchFilterThreadLocal.set(ldapUserSearchFilter);
        return providerThreadLocal.get();
    }

    boolean isLdapEnabled() {
        return getConfiguration().getClientSecurityType() == org.apache.ambari.server.security.ClientSecurityType.LDAP;
    }

    private boolean reloadLdapServerProperties() {
        org.apache.ambari.server.security.authorization.LdapServerProperties properties = ldapConfigurationProvider.get().getLdapServerProperties();
        if (!properties.equals(ldapServerProperties.get())) {
            org.apache.ambari.server.security.authorization.AmbariLdapAuthenticationProvider.LOG.info("Reloading properties");
            ldapServerProperties.set(properties);
            return true;
        }
        return false;
    }

    private java.lang.String getLdapUserSearchFilter(java.lang.String userName) {
        return ldapServerProperties.get().getUserSearchFilter(ldapConfigurationProvider.get().isLdapAlternateUserSearchEnabled() && org.apache.ambari.server.security.authorization.AmbariLdapUtils.isUserPrincipalNameFormat(userName));
    }

    private org.apache.ambari.server.orm.entities.UserEntity getUserEntity(org.springframework.security.core.Authentication authentication) {
        org.apache.ambari.server.orm.entities.UserEntity userEntity = null;
        java.lang.String dn = getUserDN(authentication);
        if (!org.apache.commons.lang.StringUtils.isEmpty(dn)) {
            userEntity = getUserEntityForDN(dn);
        }
        if (userEntity == null) {
            java.lang.String userName = org.apache.ambari.server.security.authorization.AuthorizationHelper.resolveLoginAliasToUserName(authentication.getName());
            userEntity = getUsers().getUserEntity(userName);
            if (userEntity != null) {
                java.util.Collection<org.apache.ambari.server.orm.entities.UserAuthenticationEntity> authenticationEntities = getAuthenticationEntities(userEntity, org.apache.ambari.server.security.authorization.UserAuthenticationType.LDAP);
                org.apache.ambari.server.orm.entities.UserEntity _userEntity = userEntity;
                userEntity = null;
                if (!org.apache.commons.collections.CollectionUtils.isEmpty(authenticationEntities)) {
                    for (org.apache.ambari.server.orm.entities.UserAuthenticationEntity entity : authenticationEntities) {
                        if (org.apache.commons.lang.StringUtils.isEmpty(entity.getAuthenticationKey())) {
                            userEntity = _userEntity;
                            break;
                        }
                    }
                }
            }
        }
        return userEntity;
    }

    private org.apache.ambari.server.orm.entities.UserEntity getUserEntityForDN(java.lang.String dn) {
        java.util.Collection<org.apache.ambari.server.orm.entities.UserAuthenticationEntity> authenticationEntities = getAuthenticationEntities(org.apache.ambari.server.security.authorization.UserAuthenticationType.LDAP, org.apache.commons.lang.StringUtils.lowerCase(dn));
        return (authenticationEntities == null) || (authenticationEntities.size() != 1) ? null : authenticationEntities.iterator().next().getUser();
    }

    private java.lang.String getUserDN(org.springframework.security.core.Authentication authentication) {
        java.lang.Object objectPrincipal = (authentication == null) ? null : authentication.getPrincipal();
        if (objectPrincipal instanceof org.springframework.security.ldap.userdetails.LdapUserDetails) {
            return ((org.springframework.security.ldap.userdetails.LdapUserDetails) (objectPrincipal)).getDn();
        }
        return null;
    }
}