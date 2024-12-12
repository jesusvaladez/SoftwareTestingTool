package org.apache.ambari.server.security.authentication.kerberos;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.security.authentication.util.KerberosName;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
@org.springframework.stereotype.Component
public class AmbariAuthToLocalUserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.security.authentication.kerberos.AmbariAuthToLocalUserDetailsService.class);

    private final org.apache.ambari.server.configuration.Configuration configuration;

    private final org.apache.ambari.server.security.authorization.Users users;

    private final java.lang.String authToLocalRules;

    AmbariAuthToLocalUserDetailsService(org.apache.ambari.server.configuration.Configuration configuration, org.apache.ambari.server.security.authorization.Users users) {
        org.apache.ambari.server.security.authentication.kerberos.AmbariKerberosAuthenticationProperties properties = configuration.getKerberosAuthenticationProperties();
        java.lang.String authToLocalRules = properties.getAuthToLocalRules();
        if (org.apache.commons.lang.StringUtils.isEmpty(authToLocalRules)) {
            authToLocalRules = "DEFAULT";
        }
        this.configuration = configuration;
        this.users = users;
        this.authToLocalRules = authToLocalRules;
    }

    @java.lang.Override
    public org.springframework.security.core.userdetails.UserDetails loadUserByUsername(java.lang.String principal) throws org.springframework.security.core.userdetails.UsernameNotFoundException {
        java.lang.String username;
        java.util.Collection<org.apache.ambari.server.orm.entities.UserAuthenticationEntity> entities = users.getUserAuthenticationEntities(org.apache.ambari.server.security.authorization.UserAuthenticationType.KERBEROS, principal);
        if (org.apache.commons.collections.CollectionUtils.isEmpty(entities)) {
            username = translatePrincipalName(principal);
            if (username == null) {
                java.lang.String message = java.lang.String.format("Failed to translate %s to a local username during Kerberos authentication.", principal);
                org.apache.ambari.server.security.authentication.kerberos.AmbariAuthToLocalUserDetailsService.LOG.warn(message);
                throw new org.springframework.security.core.userdetails.UsernameNotFoundException(message);
            }
            org.apache.ambari.server.security.authentication.kerberos.AmbariAuthToLocalUserDetailsService.LOG.info("Translated {} to {} using auth-to-local rules during Kerberos authentication.", principal, username);
            return createUser(username, principal);
        } else if (entities.size() == 1) {
            org.apache.ambari.server.orm.entities.UserEntity userEntity = entities.iterator().next().getUser();
            org.apache.ambari.server.security.authentication.kerberos.AmbariAuthToLocalUserDetailsService.LOG.trace("Found KERBEROS authentication method for {} using principal {}", userEntity.getUserName(), principal);
            return createUserDetails(userEntity);
        } else {
            throw new org.apache.ambari.server.security.authentication.AmbariAuthenticationException("", "Unexpected error due to collisions on the principal name", false);
        }
    }

    private org.springframework.security.core.userdetails.UserDetails createUser(java.lang.String username, java.lang.String principal) throws org.springframework.security.core.AuthenticationException {
        org.apache.ambari.server.orm.entities.UserEntity userEntity = users.getUserEntity(username);
        if (userEntity == null) {
            org.apache.ambari.server.security.authentication.kerberos.AmbariAuthToLocalUserDetailsService.LOG.info("User not found: {} (from {})", username, principal);
            throw new org.apache.ambari.server.security.authentication.UserNotFoundException(username, java.lang.String.format("Cannot find user using Kerberos ticket (%s).", principal));
        } else {
            java.util.List<org.apache.ambari.server.orm.entities.UserAuthenticationEntity> authenticationEntities = userEntity.getAuthenticationEntities();
            boolean hasKerberos = false;
            for (org.apache.ambari.server.orm.entities.UserAuthenticationEntity entity : authenticationEntities) {
                org.apache.ambari.server.security.authorization.UserAuthenticationType authenticationType = entity.getAuthenticationType();
                switch (authenticationType) {
                    case KERBEROS :
                        java.lang.String key = entity.getAuthenticationKey();
                        if (org.apache.commons.lang.StringUtils.isEmpty(key) || key.equals(username)) {
                            org.apache.ambari.server.security.authentication.kerberos.AmbariAuthToLocalUserDetailsService.LOG.trace("Found KERBEROS authentication method for {} where no principal was set. Fixing...", username);
                            try {
                                users.addKerberosAuthentication(userEntity, principal);
                                users.removeAuthentication(userEntity, entity.getUserAuthenticationId());
                            } catch (org.apache.ambari.server.AmbariException e) {
                                org.apache.ambari.server.security.authentication.kerberos.AmbariAuthToLocalUserDetailsService.LOG.warn(java.lang.String.format("Failed to create KERBEROS authentication method entry for %s with principal %s: %s", username, principal, e.getLocalizedMessage()), e);
                            }
                            hasKerberos = true;
                        } else if (principal.equalsIgnoreCase(entity.getAuthenticationKey())) {
                            org.apache.ambari.server.security.authentication.kerberos.AmbariAuthToLocalUserDetailsService.LOG.trace("Found KERBEROS authentication method for {} using principal {}", username, principal);
                            hasKerberos = true;
                        }
                        break;
                }
                if (hasKerberos) {
                    break;
                }
            }
            if (!hasKerberos) {
                try {
                    users.addKerberosAuthentication(userEntity, principal);
                    org.apache.ambari.server.security.authentication.kerberos.AmbariAuthToLocalUserDetailsService.LOG.trace("Added KERBEROS authentication method for {} using principal {}", username, principal);
                } catch (org.apache.ambari.server.AmbariException e) {
                    org.apache.ambari.server.security.authentication.kerberos.AmbariAuthToLocalUserDetailsService.LOG.error(java.lang.String.format("Failed to add the KERBEROS authentication method for %s: %s", principal, e.getLocalizedMessage()), e);
                }
            }
        }
        return createUserDetails(userEntity);
    }

    private org.springframework.security.core.userdetails.UserDetails createUserDetails(org.apache.ambari.server.orm.entities.UserEntity userEntity) {
        java.lang.String username = userEntity.getUserName();
        try {
            users.validateLogin(userEntity, username);
        } catch (org.apache.ambari.server.security.authentication.AccountDisabledException | org.apache.ambari.server.security.authentication.TooManyLoginFailuresException e) {
            if (configuration.showLockedOutUserMessage()) {
                throw e;
            } else {
                throw new org.apache.ambari.server.security.authentication.InvalidUsernamePasswordCombinationException(username, false, e);
            }
        }
        return new org.apache.ambari.server.security.authentication.AmbariUserDetailsImpl(new org.apache.ambari.server.security.authorization.User(userEntity), null, users.getUserAuthorities(userEntity));
    }

    public java.lang.String translatePrincipalName(java.lang.String principalName) {
        if (org.apache.commons.lang.StringUtils.isNotEmpty(principalName) && principalName.contains("@")) {
            try {
                synchronized(org.apache.hadoop.security.authentication.util.KerberosName.class) {
                    org.apache.hadoop.security.authentication.util.KerberosName.setRules(authToLocalRules);
                    return new org.apache.hadoop.security.authentication.util.KerberosName(principalName).getShortName();
                }
            } catch (org.apache.ambari.server.security.authentication.UserNotFoundException e) {
                throw new org.springframework.security.core.userdetails.UsernameNotFoundException(e.getMessage(), e);
            } catch (java.io.IOException e) {
                java.lang.String message = java.lang.String.format("Failed to translate %s to a local username during Kerberos authentication: %s", principalName, e.getLocalizedMessage());
                org.apache.ambari.server.security.authentication.kerberos.AmbariAuthToLocalUserDetailsService.LOG.warn(message);
                throw new org.springframework.security.core.userdetails.UsernameNotFoundException(message, e);
            }
        } else {
            return principalName;
        }
    }
}