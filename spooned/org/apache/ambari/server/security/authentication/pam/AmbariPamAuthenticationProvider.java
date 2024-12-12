package org.apache.ambari.server.security.authentication.pam;
import org.apache.commons.lang.StringUtils;
import org.jvnet.libpam.PAM;
import org.jvnet.libpam.PAMException;
import org.jvnet.libpam.UnixUser;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
public class AmbariPamAuthenticationProvider extends org.apache.ambari.server.security.authentication.AmbariAuthenticationProvider {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.security.authentication.pam.AmbariPamAuthenticationProvider.class);

    private final org.apache.ambari.server.security.authentication.pam.PamAuthenticationFactory pamAuthenticationFactory;

    @com.google.inject.Inject
    public AmbariPamAuthenticationProvider(org.apache.ambari.server.security.authorization.Users users, org.apache.ambari.server.security.authentication.pam.PamAuthenticationFactory pamAuthenticationFactory, org.apache.ambari.server.configuration.Configuration configuration) {
        super(users, configuration);
        this.pamAuthenticationFactory = pamAuthenticationFactory;
    }

    @java.lang.Override
    public org.springframework.security.core.Authentication authenticate(org.springframework.security.core.Authentication authentication) throws org.springframework.security.core.AuthenticationException {
        if (isPamEnabled()) {
            if (authentication.getName() == null) {
                org.apache.ambari.server.security.authentication.pam.AmbariPamAuthenticationProvider.LOG.info("Authentication failed: no username provided");
                throw new org.apache.ambari.server.security.authentication.InvalidUsernamePasswordCombinationException("");
            }
            java.lang.String userName = authentication.getName().trim();
            if (authentication.getCredentials() == null) {
                org.apache.ambari.server.security.authentication.pam.AmbariPamAuthenticationProvider.LOG.info("Authentication failed: no credentials provided: {}", userName);
                throw new org.apache.ambari.server.security.authentication.InvalidUsernamePasswordCombinationException(userName);
            }
            org.apache.ambari.server.security.authorization.Users users = getUsers();
            org.apache.ambari.server.orm.entities.UserEntity userEntity = users.getUserEntity(userName);
            java.lang.String password = java.lang.String.valueOf(authentication.getCredentials());
            java.lang.String ambariUsername;
            java.lang.String localUsername;
            if (userEntity == null) {
                ambariUsername = userName;
                localUsername = userName;
            } else {
                org.apache.ambari.server.orm.entities.UserAuthenticationEntity authenticationEntity = getAuthenticationEntity(userEntity, org.apache.ambari.server.security.authorization.UserAuthenticationType.PAM);
                ambariUsername = userEntity.getUserName();
                if (authenticationEntity == null) {
                    localUsername = userEntity.getLocalUsername();
                } else {
                    localUsername = authenticationEntity.getAuthenticationKey();
                    if (org.apache.commons.lang.StringUtils.isEmpty(localUsername)) {
                        localUsername = userEntity.getLocalUsername();
                    }
                }
                if (org.apache.commons.lang.StringUtils.isEmpty(localUsername)) {
                    localUsername = ambariUsername;
                }
            }
            org.jvnet.libpam.UnixUser unixUser = performPAMAuthentication(ambariUsername, localUsername, password);
            if (unixUser != null) {
                if (userEntity == null) {
                    try {
                        userEntity = users.createUser(ambariUsername, unixUser.getUserName(), ambariUsername, true);
                    } catch (org.apache.ambari.server.AmbariException e) {
                        org.apache.ambari.server.security.authentication.pam.AmbariPamAuthenticationProvider.LOG.error(java.lang.String.format("Failed to add the user, %s: %s", ambariUsername, e.getLocalizedMessage()), e);
                        throw new org.apache.ambari.server.security.authentication.AmbariAuthenticationException(ambariUsername, "Unexpected error has occurred", false, e);
                    }
                } else {
                    try {
                        users.validateLogin(userEntity, ambariUsername);
                    } catch (org.apache.ambari.server.security.authentication.AccountDisabledException | org.apache.ambari.server.security.authentication.TooManyLoginFailuresException e) {
                        if (getConfiguration().showLockedOutUserMessage()) {
                            throw e;
                        } else {
                            throw new org.apache.ambari.server.security.authentication.InvalidUsernamePasswordCombinationException(userName, false, e);
                        }
                    }
                }
                org.apache.ambari.server.orm.entities.UserAuthenticationEntity authenticationEntity = getAuthenticationEntity(userEntity, org.apache.ambari.server.security.authorization.UserAuthenticationType.PAM);
                if (authenticationEntity == null) {
                    try {
                        users.addPamAuthentication(userEntity, unixUser.getUserName());
                    } catch (org.apache.ambari.server.AmbariException e) {
                        org.apache.ambari.server.security.authentication.pam.AmbariPamAuthenticationProvider.LOG.error(java.lang.String.format("Failed to add the PAM authentication method for %s: %s", ambariUsername, e.getLocalizedMessage()), e);
                        throw new org.apache.ambari.server.security.authentication.AmbariAuthenticationException(ambariUsername, "Unexpected error has occurred", false, e);
                    }
                }
                if (isAutoGroupCreationAllowed()) {
                    synchronizeGroups(unixUser, userEntity);
                }
                org.apache.ambari.server.security.authentication.AmbariUserDetails userDetails = new org.apache.ambari.server.security.authentication.AmbariUserDetailsImpl(users.getUser(userEntity), null, users.getUserAuthorities(userEntity));
                return new org.apache.ambari.server.security.authentication.AmbariUserAuthentication(password, userDetails, true);
            }
            org.apache.ambari.server.security.authentication.pam.AmbariPamAuthenticationProvider.LOG.debug(java.lang.String.format("Authentication failed: password does not match stored value: %s", localUsername));
            throw new org.apache.ambari.server.security.authentication.InvalidUsernamePasswordCombinationException(ambariUsername);
        } else {
            return null;
        }
    }

    private org.jvnet.libpam.UnixUser performPAMAuthentication(java.lang.String ambariUsername, java.lang.String localUsername, java.lang.String password) {
        org.jvnet.libpam.PAM pam = pamAuthenticationFactory.createInstance(getConfiguration());
        if (pam == null) {
            java.lang.String message = "Failed to authenticate the user using the PAM authentication method: unexpected error";
            org.apache.ambari.server.security.authentication.pam.AmbariPamAuthenticationProvider.LOG.error(message);
            throw new org.apache.ambari.server.security.authentication.AmbariAuthenticationException(ambariUsername, message, false);
        } else {
            if (org.apache.ambari.server.security.authentication.pam.AmbariPamAuthenticationProvider.LOG.isDebugEnabled() && (!ambariUsername.equals(localUsername))) {
                org.apache.ambari.server.security.authentication.pam.AmbariPamAuthenticationProvider.LOG.debug("Authenticating Ambari user {} using the local username {}", ambariUsername, localUsername);
            }
            try {
                return pam.authenticate(localUsername, password);
            } catch (org.jvnet.libpam.PAMException e) {
                org.apache.ambari.server.security.authentication.pam.AmbariPamAuthenticationProvider.LOG.debug(java.lang.String.format("Authentication failed: password does not match stored value: %s", localUsername), e);
                throw new org.apache.ambari.server.security.authentication.InvalidUsernamePasswordCombinationException(ambariUsername, true, e);
            } finally {
                pam.dispose();
            }
        }
    }

    @java.lang.Override
    public boolean supports(java.lang.Class<?> authentication) {
        return org.springframework.security.authentication.UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }

    private boolean isPamEnabled() {
        return getConfiguration().getClientSecurityType() == org.apache.ambari.server.security.ClientSecurityType.PAM;
    }

    private boolean isAutoGroupCreationAllowed() {
        return getConfiguration().getAutoGroupCreation().equals("true");
    }

    private void synchronizeGroups(org.jvnet.libpam.UnixUser unixUser, org.apache.ambari.server.orm.entities.UserEntity userEntity) {
        org.apache.ambari.server.security.authentication.pam.AmbariPamAuthenticationProvider.LOG.debug("Synchronizing groups for PAM user: {}", unixUser.getUserName());
        org.apache.ambari.server.security.authorization.Users users = getUsers();
        try {
            java.util.Set<java.lang.String> unixUserGroups = convertToLowercase(unixUser.getGroups());
            for (java.lang.String group : unixUserGroups) {
                org.apache.ambari.server.orm.entities.GroupEntity groupEntity = users.getGroupEntity(group, org.apache.ambari.server.security.authorization.GroupType.PAM);
                if (groupEntity == null) {
                    org.apache.ambari.server.security.authentication.pam.AmbariPamAuthenticationProvider.LOG.info("Synchronizing groups for {}, adding new PAM group: {}", userEntity.getUserName(), group);
                    groupEntity = users.createGroup(group, org.apache.ambari.server.security.authorization.GroupType.PAM);
                }
                if (!users.isUserInGroup(userEntity, groupEntity)) {
                    org.apache.ambari.server.security.authentication.pam.AmbariPamAuthenticationProvider.LOG.info("Synchronizing groups for {}, adding user to PAM group: {}", userEntity.getUserName(), group);
                    users.addMemberToGroup(groupEntity, userEntity);
                }
            }
            java.util.Set<org.apache.ambari.server.orm.entities.MemberEntity> memberEntities = userEntity.getMemberEntities();
            if (memberEntities != null) {
                java.util.Collection<org.apache.ambari.server.orm.entities.GroupEntity> groupsToRemove = new java.util.ArrayList<>();
                for (org.apache.ambari.server.orm.entities.MemberEntity memberEntity : memberEntities) {
                    org.apache.ambari.server.orm.entities.GroupEntity groupEntity = memberEntity.getGroup();
                    if ((groupEntity.getGroupType() == org.apache.ambari.server.security.authorization.GroupType.PAM) && (!unixUserGroups.contains(groupEntity.getGroupName()))) {
                        groupsToRemove.add(groupEntity);
                    }
                }
                for (org.apache.ambari.server.orm.entities.GroupEntity groupEntity : groupsToRemove) {
                    org.apache.ambari.server.security.authentication.pam.AmbariPamAuthenticationProvider.LOG.info("Synchronizing groups for {}, removing user from PAM group: {}", userEntity.getUserName(), groupEntity.getGroupName());
                    users.removeMemberFromGroup(groupEntity, userEntity);
                }
            }
        } catch (org.apache.ambari.server.AmbariException e) {
            e.printStackTrace();
        }
    }

    private java.util.Set<java.lang.String> convertToLowercase(java.util.Set<java.lang.String> groups) {
        java.util.Set<java.lang.String> lowercaseGroups = new java.util.HashSet<>();
        if (groups != null) {
            for (java.lang.String group : groups) {
                lowercaseGroups.add(group.toLowerCase());
            }
        }
        return lowercaseGroups;
    }
}