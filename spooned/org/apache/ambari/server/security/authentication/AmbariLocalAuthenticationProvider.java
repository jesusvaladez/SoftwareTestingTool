package org.apache.ambari.server.security.authentication;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
public class AmbariLocalAuthenticationProvider extends org.apache.ambari.server.security.authentication.AmbariAuthenticationProvider {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.security.authentication.AmbariLocalAuthenticationProvider.class);

    private org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    @com.google.inject.Inject
    public AmbariLocalAuthenticationProvider(org.apache.ambari.server.security.authorization.Users users, org.springframework.security.crypto.password.PasswordEncoder passwordEncoder, org.apache.ambari.server.configuration.Configuration configuration) {
        super(users, configuration);
        this.passwordEncoder = passwordEncoder;
    }

    @java.lang.Override
    public org.springframework.security.core.Authentication authenticate(org.springframework.security.core.Authentication authentication) throws org.springframework.security.core.AuthenticationException {
        if (authentication.getName() == null) {
            org.apache.ambari.server.security.authentication.AmbariLocalAuthenticationProvider.LOG.info("Authentication failed: no username provided");
            throw new org.apache.ambari.server.security.authentication.InvalidUsernamePasswordCombinationException("");
        }
        java.lang.String userName = authentication.getName().trim();
        if (authentication.getCredentials() == null) {
            org.apache.ambari.server.security.authentication.AmbariLocalAuthenticationProvider.LOG.info("Authentication failed: no credentials provided: {}", userName);
            throw new org.apache.ambari.server.security.authentication.InvalidUsernamePasswordCombinationException(userName);
        }
        org.apache.ambari.server.security.authorization.Users users = getUsers();
        org.apache.ambari.server.orm.entities.UserEntity userEntity = users.getUserEntity(userName);
        if (userEntity == null) {
            org.apache.ambari.server.security.authentication.AmbariLocalAuthenticationProvider.LOG.info("User not found: {}", userName);
            throw new org.apache.ambari.server.security.authentication.InvalidUsernamePasswordCombinationException(userName);
        }
        org.apache.ambari.server.orm.entities.UserAuthenticationEntity authenticationEntity = getAuthenticationEntity(userEntity, org.apache.ambari.server.security.authorization.UserAuthenticationType.LOCAL);
        if (authenticationEntity != null) {
            java.lang.String password = authenticationEntity.getAuthenticationKey();
            java.lang.String presentedPassword = authentication.getCredentials().toString();
            if (passwordEncoder.matches(presentedPassword, password)) {
                org.apache.ambari.server.security.authentication.AmbariLocalAuthenticationProvider.LOG.debug("Authentication succeeded - a matching username and password were found: {}", userName);
                try {
                    users.validateLogin(userEntity, userName);
                } catch (org.apache.ambari.server.security.authentication.AccountDisabledException | org.apache.ambari.server.security.authentication.TooManyLoginFailuresException e) {
                    if (getConfiguration().showLockedOutUserMessage()) {
                        throw e;
                    } else {
                        throw new org.apache.ambari.server.security.authentication.InvalidUsernamePasswordCombinationException(userName, false, e);
                    }
                }
                org.apache.ambari.server.security.authentication.AmbariUserDetails userDetails = new org.apache.ambari.server.security.authentication.AmbariUserDetailsImpl(users.getUser(userEntity), password, users.getUserAuthorities(userEntity));
                return new org.apache.ambari.server.security.authentication.AmbariUserAuthentication(password, userDetails, true);
            }
        }
        org.apache.ambari.server.security.authentication.AmbariLocalAuthenticationProvider.LOG.debug("Authentication failed: password does not match stored value: {}", userName);
        throw new org.apache.ambari.server.security.authentication.InvalidUsernamePasswordCombinationException(userName);
    }

    @java.lang.Override
    public boolean supports(java.lang.Class<?> authentication) {
        return org.springframework.security.authentication.UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}