package org.apache.ambari.server.security.authentication.jwt;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
public class AmbariJwtAuthenticationProvider extends org.apache.ambari.server.security.authentication.AmbariAuthenticationProvider {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.security.authentication.jwt.AmbariJwtAuthenticationProvider.class);

    @com.google.inject.Inject
    public AmbariJwtAuthenticationProvider(org.apache.ambari.server.security.authorization.Users users, org.apache.ambari.server.configuration.Configuration configuration) {
        super(users, configuration);
    }

    @java.lang.Override
    public org.springframework.security.core.Authentication authenticate(org.springframework.security.core.Authentication authentication) throws org.springframework.security.core.AuthenticationException {
        if (authentication.getName() == null) {
            org.apache.ambari.server.security.authentication.jwt.AmbariJwtAuthenticationProvider.LOG.info("Authentication failed: no username provided");
            throw new org.apache.ambari.server.security.authentication.AmbariAuthenticationException(null, "Unexpected error due to missing username", false);
        }
        java.lang.String userName = authentication.getName().trim();
        if (authentication.getCredentials() == null) {
            org.apache.ambari.server.security.authentication.jwt.AmbariJwtAuthenticationProvider.LOG.info("Authentication failed: no credentials provided: {}", userName);
            throw new org.apache.ambari.server.security.authentication.AmbariAuthenticationException(userName, "Unexpected error due to missing JWT token", false);
        }
        org.apache.ambari.server.security.authorization.Users users = getUsers();
        org.apache.ambari.server.orm.entities.UserEntity userEntity = users.getUserEntity(userName);
        if (userEntity == null) {
            org.apache.ambari.server.security.authentication.jwt.AmbariJwtAuthenticationProvider.LOG.info("User not found: {}", userName);
            throw new org.apache.ambari.server.security.authentication.UserNotFoundException(userName, "Cannot find user from JWT. Please, ensure LDAP is configured and users are synced.");
        }
        boolean authOK = false;
        org.apache.ambari.server.orm.entities.UserAuthenticationEntity authenticationEntity = getAuthenticationEntity(userEntity, org.apache.ambari.server.security.authorization.UserAuthenticationType.JWT);
        if (authenticationEntity != null) {
            authOK = true;
        } else {
            authenticationEntity = getAuthenticationEntity(userEntity, org.apache.ambari.server.security.authorization.UserAuthenticationType.LDAP);
            if (authenticationEntity != null) {
                try {
                    users.addJWTAuthentication(userEntity, userName);
                    authOK = true;
                } catch (org.apache.ambari.server.AmbariException e) {
                    org.apache.ambari.server.security.authentication.jwt.AmbariJwtAuthenticationProvider.LOG.error(java.lang.String.format("Failed to add the JWT authentication method for %s: %s", userName, e.getLocalizedMessage()), e);
                    throw new org.apache.ambari.server.security.authentication.AmbariAuthenticationException(userName, "Unexpected error has occurred", false, e);
                }
            }
        }
        if (authOK) {
            org.apache.ambari.server.security.authentication.jwt.AmbariJwtAuthenticationProvider.LOG.debug("Authentication succeeded - a matching user was found: {}", userName);
            try {
                users.validateLogin(userEntity, userName);
            } catch (org.apache.ambari.server.security.authentication.AccountDisabledException | org.apache.ambari.server.security.authentication.TooManyLoginFailuresException e) {
                if (getConfiguration().showLockedOutUserMessage()) {
                    throw e;
                } else {
                    throw new org.apache.ambari.server.security.authentication.AmbariAuthenticationException(userName, "Unexpected error due to missing JWT token", false);
                }
            }
            org.apache.ambari.server.security.authentication.AmbariUserDetails userDetails = new org.apache.ambari.server.security.authentication.AmbariUserDetailsImpl(users.getUser(userEntity), null, users.getUserAuthorities(userEntity));
            java.lang.String jwtTokenName = userDetails.getUsername().trim();
            if (!userName.equals(jwtTokenName)) {
                org.apache.ambari.server.security.authorization.AuthorizationHelper.addLoginNameAlias(userName, jwtTokenName);
            }
            return new org.apache.ambari.server.security.authentication.AmbariUserAuthentication(authentication.getCredentials().toString(), userDetails, true);
        } else {
            org.apache.ambari.server.security.authentication.jwt.AmbariJwtAuthenticationProvider.LOG.debug("Authentication failed: password does not match stored value: {}", userName);
            throw new org.apache.ambari.server.security.authentication.UserNotFoundException(userName, "Cannot find user from JWT. Please, ensure LDAP is configured and users are synced.");
        }
    }

    @java.lang.Override
    public boolean supports(java.lang.Class<?> authentication) {
        return org.apache.ambari.server.security.authentication.jwt.JwtAuthenticationToken.class.isAssignableFrom(authentication);
    }
}