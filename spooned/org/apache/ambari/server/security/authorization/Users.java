package org.apache.ambari.server.security.authorization;
import com.google.inject.persist.Transactional;
import javax.persistence.EntityManager;
import javax.persistence.OptimisticLockException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
@com.google.inject.Singleton
public class Users {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.security.authorization.Users.class);

    private static final int MAX_RETRIES = 10;

    @com.google.inject.Inject
    private com.google.inject.Provider<javax.persistence.EntityManager> entityManagerProvider;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.UserDAO userDAO;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.UserAuthenticationDAO userAuthenticationDAO;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.GroupDAO groupDAO;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.MemberDAO memberDAO;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.PrincipalDAO principalDAO;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.PermissionDAO permissionDAO;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.PrivilegeDAO privilegeDAO;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.ResourceDAO resourceDAO;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.PrincipalTypeDAO principalTypeDAO;

    @com.google.inject.Inject
    private org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    @com.google.inject.Inject
    protected org.apache.ambari.server.ldap.service.AmbariLdapConfigurationProvider ldapConfigurationProvider;

    @com.google.inject.Inject
    protected org.apache.ambari.server.configuration.Configuration configuration;

    @com.google.inject.Inject
    private com.google.inject.Provider<org.apache.ambari.server.hooks.HookService> hookServiceProvider;

    @com.google.inject.Inject
    private org.apache.ambari.server.hooks.HookContextFactory hookContextFactory;

    public java.util.List<org.apache.ambari.server.security.authorization.User> getAllUsers() {
        java.util.List<org.apache.ambari.server.orm.entities.UserEntity> userEntities = userDAO.findAll();
        java.util.List<org.apache.ambari.server.security.authorization.User> users = new java.util.ArrayList<>(userEntities.size());
        for (org.apache.ambari.server.orm.entities.UserEntity userEntity : userEntities) {
            users.add(new org.apache.ambari.server.security.authorization.User(userEntity));
        }
        return users;
    }

    public java.util.List<org.apache.ambari.server.orm.entities.UserEntity> getAllUserEntities() {
        return userDAO.findAll();
    }

    public org.apache.ambari.server.orm.entities.UserEntity getUserEntity(java.lang.String userName) {
        return userName == null ? null : userDAO.findUserByName(userName);
    }

    public org.apache.ambari.server.orm.entities.UserEntity getUserEntity(java.lang.Integer userId) {
        return userId == null ? null : userDAO.findByPK(userId);
    }

    public org.apache.ambari.server.security.authorization.User getUser(org.apache.ambari.server.orm.entities.UserEntity userEntity) {
        return null == userEntity ? null : new org.apache.ambari.server.security.authorization.User(userEntity);
    }

    public org.apache.ambari.server.security.authorization.User getUser(java.lang.Integer userId) {
        return getUser(getUserEntity(userId));
    }

    public org.apache.ambari.server.security.authorization.User getUser(java.lang.String userName) {
        return getUser(getUserEntity(userName));
    }

    public synchronized void setUserActive(java.lang.String userName, boolean active) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.orm.entities.UserEntity userEntity = userDAO.findUserByName(userName);
        if (userEntity != null) {
            setUserActive(userEntity, active);
        } else {
            throw new org.apache.ambari.server.AmbariException(("User " + userName) + " doesn't exist");
        }
    }

    public synchronized void setUserActive(org.apache.ambari.server.orm.entities.UserEntity userEntity, final boolean active) throws org.apache.ambari.server.AmbariException {
        if (userEntity != null) {
            org.apache.ambari.server.security.authorization.Users.Command command = new org.apache.ambari.server.security.authorization.Users.Command() {
                @java.lang.Override
                public void perform(org.apache.ambari.server.orm.entities.UserEntity userEntity) {
                    userEntity.setActive(active);
                }
            };
            safelyUpdateUserEntity(userEntity, command, org.apache.ambari.server.security.authorization.Users.MAX_RETRIES);
        }
    }

    public void validateLogin(org.apache.ambari.server.orm.entities.UserEntity userEntity, java.lang.String userName) {
        if (userEntity == null) {
            org.apache.ambari.server.security.authorization.Users.LOG.info("User not found");
            throw new org.apache.ambari.server.security.authentication.UserNotFoundException(userName);
        } else {
            if (!userEntity.getActive()) {
                org.apache.ambari.server.security.authorization.Users.LOG.info("User account is disabled: {}", userName);
                throw new org.apache.ambari.server.security.authentication.AccountDisabledException(userName);
            }
            int maxConsecutiveFailures = configuration.getMaxAuthenticationFailures();
            if ((maxConsecutiveFailures > 0) && (userEntity.getConsecutiveFailures() >= maxConsecutiveFailures)) {
                org.apache.ambari.server.security.authorization.Users.LOG.info("User account is locked out due to too many authentication failures ({}/{}): {}", userEntity.getConsecutiveFailures(), maxConsecutiveFailures, userName);
                throw new org.apache.ambari.server.security.authentication.TooManyLoginFailuresException(userName);
            }
        }
    }

    public synchronized void setGroupLdap(java.lang.String groupName) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.orm.entities.GroupEntity groupEntity = groupDAO.findGroupByName(groupName);
        if (groupEntity != null) {
            groupEntity.setGroupType(org.apache.ambari.server.security.authorization.GroupType.LDAP);
            groupDAO.merge(groupEntity);
        } else {
            throw new org.apache.ambari.server.AmbariException(("Group " + groupName) + " doesn't exist");
        }
    }

    public org.apache.ambari.server.orm.entities.UserEntity createUser(java.lang.String userName, java.lang.String localUserName, java.lang.String displayName) throws org.apache.ambari.server.AmbariException {
        return createUser(userName, localUserName, displayName, true);
    }

    @com.google.inject.persist.Transactional
    public synchronized org.apache.ambari.server.orm.entities.UserEntity createUser(java.lang.String userName, java.lang.String localUserName, java.lang.String displayName, java.lang.Boolean active) throws org.apache.ambari.server.AmbariException {
        java.lang.String validatedUserName = org.apache.ambari.server.security.authorization.UserName.fromString(userName).toString();
        java.lang.String validatedDisplayName = (org.apache.commons.lang.StringUtils.isEmpty(displayName)) ? validatedUserName : org.apache.ambari.server.security.authorization.UserName.fromString(displayName).toString();
        java.lang.String validatedLocalUserName = (org.apache.commons.lang.StringUtils.isEmpty(localUserName)) ? validatedUserName : org.apache.ambari.server.security.authorization.UserName.fromString(localUserName).toString();
        if (userDAO.findUserByName(validatedUserName) != null) {
            throw new org.apache.ambari.server.AmbariException("User already exists");
        }
        org.apache.ambari.server.orm.entities.PrincipalTypeEntity principalTypeEntity = principalTypeDAO.findById(org.apache.ambari.server.orm.entities.PrincipalTypeEntity.USER_PRINCIPAL_TYPE);
        if (principalTypeEntity == null) {
            principalTypeEntity = new org.apache.ambari.server.orm.entities.PrincipalTypeEntity();
            principalTypeEntity.setId(org.apache.ambari.server.orm.entities.PrincipalTypeEntity.USER_PRINCIPAL_TYPE);
            principalTypeEntity.setName(org.apache.ambari.server.orm.entities.PrincipalTypeEntity.USER_PRINCIPAL_TYPE_NAME);
            principalTypeDAO.create(principalTypeEntity);
        }
        org.apache.ambari.server.orm.entities.PrincipalEntity principalEntity = new org.apache.ambari.server.orm.entities.PrincipalEntity();
        principalEntity.setPrincipalType(principalTypeEntity);
        principalDAO.create(principalEntity);
        org.apache.ambari.server.orm.entities.UserEntity userEntity = new org.apache.ambari.server.orm.entities.UserEntity();
        userEntity.setUserName(validatedUserName);
        userEntity.setDisplayName(validatedDisplayName);
        userEntity.setLocalUsername(validatedLocalUserName);
        userEntity.setPrincipal(principalEntity);
        if (active != null) {
            userEntity.setActive(active);
        }
        userDAO.create(userEntity);
        executeUserHook(validatedUserName);
        return userEntity;
    }

    public void executeUserHook(java.lang.String username) {
        hookServiceProvider.get().execute(hookContextFactory.createUserHookContext(username));
    }

    public void executeUserHook(java.util.Map<java.lang.String, java.util.Set<java.lang.String>> userGroupsMap) {
        hookServiceProvider.get().execute(hookContextFactory.createBatchUserHookContext(userGroupsMap));
    }

    @com.google.inject.persist.Transactional
    public synchronized void removeUser(org.apache.ambari.server.security.authorization.User user) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.orm.entities.UserEntity userEntity = userDAO.findByPK(user.getUserId());
        if (userEntity != null) {
            removeUser(userEntity);
        } else {
            throw new org.apache.ambari.server.AmbariException(("User " + user) + " doesn't exist");
        }
    }

    @com.google.inject.persist.Transactional
    public synchronized void removeUser(org.apache.ambari.server.orm.entities.UserEntity userEntity) throws org.apache.ambari.server.AmbariException {
        if (userEntity != null) {
            if (!isUserCanBeRemoved(userEntity)) {
                throw new org.apache.ambari.server.AmbariException(("Could not remove user " + userEntity.getUserName()) + ". System should have at least one administrator.");
            }
            userDAO.remove(userEntity);
        }
    }

    public org.apache.ambari.server.security.authorization.Group getGroup(java.lang.String groupName) {
        final org.apache.ambari.server.orm.entities.GroupEntity groupEntity = groupDAO.findGroupByName(groupName);
        return null == groupEntity ? null : new org.apache.ambari.server.security.authorization.Group(groupEntity);
    }

    public org.apache.ambari.server.security.authorization.Group getGroup(java.lang.String groupName, org.apache.ambari.server.security.authorization.GroupType groupType) {
        final org.apache.ambari.server.orm.entities.GroupEntity groupEntity = getGroupEntity(groupName, groupType);
        return null == groupEntity ? null : new org.apache.ambari.server.security.authorization.Group(groupEntity);
    }

    public org.apache.ambari.server.orm.entities.GroupEntity getGroupEntity(java.lang.String groupName, org.apache.ambari.server.security.authorization.GroupType groupType) {
        return groupDAO.findGroupByNameAndType(groupName, groupType);
    }

    public java.util.Collection<org.apache.ambari.server.security.authorization.User> getGroupMembers(java.lang.String groupName) {
        final org.apache.ambari.server.orm.entities.GroupEntity groupEntity = groupDAO.findGroupByName(groupName);
        if (groupEntity == null) {
            return null;
        } else {
            final java.util.Set<org.apache.ambari.server.security.authorization.User> users = new java.util.HashSet<>();
            for (org.apache.ambari.server.orm.entities.MemberEntity memberEntity : groupEntity.getMemberEntities()) {
                if (memberEntity.getUser() != null) {
                    users.add(new org.apache.ambari.server.security.authorization.User(memberEntity.getUser()));
                } else {
                    org.apache.ambari.server.security.authorization.Users.LOG.error("Wrong state, not found user for member '{}' (group: '{}')", memberEntity.getMemberId(), memberEntity.getGroup().getGroupName());
                }
            }
            return users;
        }
    }

    @com.google.inject.persist.Transactional
    public synchronized org.apache.ambari.server.orm.entities.GroupEntity createGroup(java.lang.String groupName, org.apache.ambari.server.security.authorization.GroupType groupType) {
        org.apache.ambari.server.orm.entities.PrincipalTypeEntity principalTypeEntity = principalTypeDAO.findById(org.apache.ambari.server.orm.entities.PrincipalTypeEntity.GROUP_PRINCIPAL_TYPE);
        if (principalTypeEntity == null) {
            principalTypeEntity = new org.apache.ambari.server.orm.entities.PrincipalTypeEntity();
            principalTypeEntity.setId(org.apache.ambari.server.orm.entities.PrincipalTypeEntity.GROUP_PRINCIPAL_TYPE);
            principalTypeEntity.setName(org.apache.ambari.server.orm.entities.PrincipalTypeEntity.GROUP_PRINCIPAL_TYPE_NAME);
            principalTypeDAO.create(principalTypeEntity);
        }
        org.apache.ambari.server.orm.entities.PrincipalEntity principalEntity = new org.apache.ambari.server.orm.entities.PrincipalEntity();
        principalEntity.setPrincipalType(principalTypeEntity);
        principalDAO.create(principalEntity);
        final org.apache.ambari.server.orm.entities.GroupEntity groupEntity = new org.apache.ambari.server.orm.entities.GroupEntity();
        groupEntity.setGroupName(groupName);
        groupEntity.setPrincipal(principalEntity);
        groupEntity.setGroupType(groupType);
        groupDAO.create(groupEntity);
        return groupEntity;
    }

    public java.util.List<org.apache.ambari.server.security.authorization.Group> getAllGroups() {
        final java.util.List<org.apache.ambari.server.orm.entities.GroupEntity> groupEntities = groupDAO.findAll();
        final java.util.List<org.apache.ambari.server.security.authorization.Group> groups = new java.util.ArrayList<>(groupEntities.size());
        for (org.apache.ambari.server.orm.entities.GroupEntity groupEntity : groupEntities) {
            groups.add(new org.apache.ambari.server.security.authorization.Group(groupEntity));
        }
        return groups;
    }

    public java.util.List<java.lang.String> getAllMembers(java.lang.String groupName) throws org.apache.ambari.server.AmbariException {
        final java.util.List<java.lang.String> members = new java.util.ArrayList<>();
        final org.apache.ambari.server.orm.entities.GroupEntity groupEntity = groupDAO.findGroupByName(groupName);
        if (groupEntity == null) {
            throw new org.apache.ambari.server.AmbariException(("Group " + groupName) + " doesn't exist");
        }
        for (org.apache.ambari.server.orm.entities.MemberEntity member : groupEntity.getMemberEntities()) {
            members.add(member.getUser().getUserName());
        }
        return members;
    }

    @com.google.inject.persist.Transactional
    public synchronized void removeGroup(org.apache.ambari.server.security.authorization.Group group) throws org.apache.ambari.server.AmbariException {
        final org.apache.ambari.server.orm.entities.GroupEntity groupEntity = groupDAO.findByPK(group.getGroupId());
        if (groupEntity != null) {
            groupDAO.remove(groupEntity);
        } else {
            throw new org.apache.ambari.server.AmbariException(("Group " + group) + " doesn't exist");
        }
    }

    public synchronized boolean hasAdminPrivilege(org.apache.ambari.server.orm.entities.UserEntity userEntity) {
        org.apache.ambari.server.orm.entities.PrincipalEntity principalEntity = userEntity.getPrincipal();
        if (principalEntity != null) {
            java.util.Set<org.apache.ambari.server.orm.entities.PrivilegeEntity> roles = principalEntity.getPrivileges();
            if (roles != null) {
                org.apache.ambari.server.orm.entities.PermissionEntity adminPermission = permissionDAO.findAmbariAdminPermission();
                java.lang.Integer adminPermissionId = (adminPermission == null) ? null : adminPermission.getId();
                if (adminPermissionId != null) {
                    for (org.apache.ambari.server.orm.entities.PrivilegeEntity privilegeEntity : roles) {
                        org.apache.ambari.server.orm.entities.PermissionEntity rolePermission = privilegeEntity.getPermission();
                        if ((rolePermission != null) && adminPermissionId.equals(rolePermission.getId())) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public synchronized void grantAdminPrivilege(java.lang.Integer userId) {
        grantAdminPrivilege(userDAO.findByPK(userId));
    }

    public synchronized void grantAdminPrivilege(org.apache.ambari.server.orm.entities.UserEntity userEntity) {
        final org.apache.ambari.server.orm.entities.PrivilegeEntity adminPrivilege = new org.apache.ambari.server.orm.entities.PrivilegeEntity();
        adminPrivilege.setPermission(permissionDAO.findAmbariAdminPermission());
        adminPrivilege.setPrincipal(userEntity.getPrincipal());
        adminPrivilege.setResource(resourceDAO.findAmbariResource());
        if (!userEntity.getPrincipal().getPrivileges().contains(adminPrivilege)) {
            privilegeDAO.create(adminPrivilege);
            userEntity.getPrincipal().getPrivileges().add(adminPrivilege);
            principalDAO.merge(userEntity.getPrincipal());
            userDAO.merge(userEntity);
        }
    }

    public synchronized void grantPrivilegeToGroup(java.lang.Integer groupId, java.lang.Long resourceId, org.apache.ambari.server.security.authorization.ResourceType resourceType, java.lang.String permissionName) {
        final org.apache.ambari.server.orm.entities.GroupEntity group = groupDAO.findByPK(groupId);
        final org.apache.ambari.server.orm.entities.PrivilegeEntity privilege = new org.apache.ambari.server.orm.entities.PrivilegeEntity();
        org.apache.ambari.server.orm.entities.ResourceTypeEntity resourceTypeEntity = new org.apache.ambari.server.orm.entities.ResourceTypeEntity();
        resourceTypeEntity.setId(resourceType.getId());
        resourceTypeEntity.setName(resourceType.name());
        privilege.setPermission(permissionDAO.findPermissionByNameAndType(permissionName, resourceTypeEntity));
        privilege.setPrincipal(group.getPrincipal());
        privilege.setResource(resourceDAO.findById(resourceId));
        if (!group.getPrincipal().getPrivileges().contains(privilege)) {
            privilegeDAO.create(privilege);
            group.getPrincipal().getPrivileges().add(privilege);
            principalDAO.merge(group.getPrincipal());
            groupDAO.merge(group);
            privilegeDAO.merge(privilege);
        }
    }

    public synchronized void revokeAdminPrivilege(java.lang.Integer userId) {
        revokeAdminPrivilege(userDAO.findByPK(userId));
    }

    public synchronized void revokeAdminPrivilege(org.apache.ambari.server.orm.entities.UserEntity userEntity) {
        for (org.apache.ambari.server.orm.entities.PrivilegeEntity privilege : userEntity.getPrincipal().getPrivileges()) {
            if (privilege.getPermission().getPermissionName().equals(org.apache.ambari.server.orm.entities.PermissionEntity.AMBARI_ADMINISTRATOR_PERMISSION_NAME)) {
                userEntity.getPrincipal().getPrivileges().remove(privilege);
                principalDAO.merge(userEntity.getPrincipal());
                userDAO.merge(userEntity);
                privilegeDAO.remove(privilege);
                break;
            }
        }
    }

    @com.google.inject.persist.Transactional
    public synchronized void addMemberToGroup(java.lang.String groupName, java.lang.String userName) throws org.apache.ambari.server.AmbariException {
        final org.apache.ambari.server.orm.entities.GroupEntity groupEntity = groupDAO.findGroupByName(groupName);
        if (groupEntity == null) {
            throw new org.apache.ambari.server.AmbariException(("Group " + groupName) + " doesn't exist");
        }
        org.apache.ambari.server.orm.entities.UserEntity userEntity = userDAO.findUserByName(userName);
        if (userEntity == null) {
            throw new org.apache.ambari.server.AmbariException(("User " + userName) + " doesn't exist");
        }
        addMemberToGroup(groupEntity, userEntity);
    }

    @com.google.inject.persist.Transactional
    public synchronized void addMemberToGroup(org.apache.ambari.server.orm.entities.GroupEntity groupEntity, org.apache.ambari.server.orm.entities.UserEntity userEntity) throws org.apache.ambari.server.AmbariException {
        if (groupEntity == null) {
            throw new java.lang.NullPointerException();
        }
        if (userEntity == null) {
            throw new java.lang.NullPointerException();
        }
        if (!isUserInGroup(userEntity, groupEntity)) {
            final org.apache.ambari.server.orm.entities.MemberEntity memberEntity = new org.apache.ambari.server.orm.entities.MemberEntity();
            memberEntity.setGroup(groupEntity);
            memberEntity.setUser(userEntity);
            userEntity.getMemberEntities().add(memberEntity);
            groupEntity.getMemberEntities().add(memberEntity);
            memberDAO.create(memberEntity);
            userDAO.merge(userEntity);
            groupDAO.merge(groupEntity);
        }
    }

    @com.google.inject.persist.Transactional
    public synchronized void removeMemberFromGroup(java.lang.String groupName, java.lang.String userName) throws org.apache.ambari.server.AmbariException {
        final org.apache.ambari.server.orm.entities.GroupEntity groupEntity = groupDAO.findGroupByName(groupName);
        if (groupEntity == null) {
            throw new org.apache.ambari.server.AmbariException(("Group " + groupName) + " doesn't exist");
        }
        org.apache.ambari.server.orm.entities.UserEntity userEntity = userDAO.findUserByName(userName);
        if (userEntity == null) {
            throw new org.apache.ambari.server.AmbariException(("User " + userName) + " doesn't exist");
        }
        removeMemberFromGroup(groupEntity, userEntity);
    }

    @com.google.inject.persist.Transactional
    public synchronized void removeMemberFromGroup(org.apache.ambari.server.orm.entities.GroupEntity groupEntity, org.apache.ambari.server.orm.entities.UserEntity userEntity) throws org.apache.ambari.server.AmbariException {
        if (isUserInGroup(userEntity, groupEntity)) {
            org.apache.ambari.server.orm.entities.MemberEntity memberEntity = null;
            for (org.apache.ambari.server.orm.entities.MemberEntity entity : userEntity.getMemberEntities()) {
                if (entity.getGroup().equals(groupEntity)) {
                    memberEntity = entity;
                    break;
                }
            }
            userEntity.getMemberEntities().remove(memberEntity);
            groupEntity.getMemberEntities().remove(memberEntity);
            userDAO.merge(userEntity);
            groupDAO.merge(groupEntity);
            memberDAO.remove(memberEntity);
        }
    }

    public synchronized boolean isUserCanBeRemoved(org.apache.ambari.server.orm.entities.UserEntity userEntity) {
        java.util.List<org.apache.ambari.server.orm.entities.PrincipalEntity> adminPrincipals = principalDAO.findByPermissionId(org.apache.ambari.server.orm.entities.PermissionEntity.AMBARI_ADMINISTRATOR_PERMISSION);
        java.util.Set<org.apache.ambari.server.orm.entities.UserEntity> userEntitysSet = new java.util.HashSet<>(userDAO.findUsersByPrincipal(adminPrincipals));
        return userEntitysSet.contains(userEntity) && (userEntitysSet.size() < 2) ? false : true;
    }

    public boolean isUserInGroup(org.apache.ambari.server.orm.entities.UserEntity userEntity, org.apache.ambari.server.orm.entities.GroupEntity groupEntity) {
        for (org.apache.ambari.server.orm.entities.MemberEntity memberEntity : userEntity.getMemberEntities()) {
            if (memberEntity.getGroup().equals(groupEntity)) {
                return true;
            }
        }
        return false;
    }

    public void processLdapSync(org.apache.ambari.server.security.ldap.LdapBatchDto batchInfo) {
        final java.util.Map<java.lang.String, org.apache.ambari.server.orm.entities.UserEntity> allUsers = new java.util.HashMap<>();
        final java.util.Map<java.lang.String, org.apache.ambari.server.orm.entities.GroupEntity> allGroups = new java.util.HashMap<>();
        for (org.apache.ambari.server.orm.entities.UserEntity userEntity : userDAO.findAll()) {
            allUsers.put(userEntity.getUserName(), userEntity);
        }
        for (org.apache.ambari.server.orm.entities.GroupEntity groupEntity : groupDAO.findAll()) {
            allGroups.put(groupEntity.getGroupName(), groupEntity);
        }
        final org.apache.ambari.server.orm.entities.PrincipalTypeEntity groupPrincipalType = principalTypeDAO.ensurePrincipalTypeCreated(org.apache.ambari.server.orm.entities.PrincipalTypeEntity.GROUP_PRINCIPAL_TYPE);
        final java.util.Set<org.apache.ambari.server.orm.entities.UserEntity> usersToRemove = new java.util.HashSet<>();
        final java.util.Set<org.apache.ambari.server.orm.entities.UserAuthenticationEntity> authenticationEntitiesToRemove = new java.util.HashSet<>();
        for (org.apache.ambari.server.security.ldap.LdapUserDto user : batchInfo.getUsersToBeRemoved()) {
            org.apache.ambari.server.orm.entities.UserEntity userEntity = userDAO.findUserByName(user.getUserName());
            if (userEntity != null) {
                java.util.List<org.apache.ambari.server.orm.entities.UserAuthenticationEntity> authenticationEntities = userAuthenticationDAO.findByUser(userEntity);
                java.util.Iterator<org.apache.ambari.server.orm.entities.UserAuthenticationEntity> iterator = authenticationEntities.iterator();
                while (iterator.hasNext()) {
                    org.apache.ambari.server.orm.entities.UserAuthenticationEntity authenticationEntity = iterator.next();
                    if (authenticationEntity.getAuthenticationType() == org.apache.ambari.server.security.authorization.UserAuthenticationType.LDAP) {
                        java.lang.String dn = user.getDn();
                        java.lang.String authenticationKey = authenticationEntity.getAuthenticationKey();
                        if ((org.apache.commons.lang.StringUtils.isEmpty(dn) || org.apache.commons.lang.StringUtils.isEmpty(authenticationKey)) || dn.equalsIgnoreCase(authenticationKey)) {
                            authenticationEntitiesToRemove.add(authenticationEntity);
                        }
                        iterator.remove();
                    }
                } 
                if (authenticationEntities.isEmpty()) {
                    allUsers.remove(userEntity.getUserName());
                    usersToRemove.add(userEntity);
                }
            }
        }
        userAuthenticationDAO.remove(authenticationEntitiesToRemove);
        userDAO.remove(usersToRemove);
        final java.util.Set<org.apache.ambari.server.orm.entities.GroupEntity> groupsToRemove = new java.util.HashSet<>();
        for (org.apache.ambari.server.security.ldap.LdapGroupDto group : batchInfo.getGroupsToBeRemoved()) {
            final org.apache.ambari.server.orm.entities.GroupEntity groupEntity = groupDAO.findGroupByName(group.getGroupName());
            allGroups.remove(groupEntity.getGroupName());
            groupsToRemove.add(groupEntity);
        }
        groupDAO.remove(groupsToRemove);
        final java.util.Set<org.apache.ambari.server.orm.entities.UserEntity> userEntitiesToUpdate = new java.util.HashSet<>();
        for (org.apache.ambari.server.security.ldap.LdapUserDto user : batchInfo.getUsersToBecomeLdap()) {
            java.lang.String userName = user.getUserName();
            org.apache.ambari.server.orm.entities.UserEntity userEntity = userDAO.findUserByName(userName);
            if (userEntity != null) {
                org.apache.ambari.server.security.authorization.Users.LOG.trace("Enabling LDAP authentication for the user account with the username {}.", userName);
                if (configuration.getLdapSyncCollisionHandlingBehavior() == org.apache.ambari.server.configuration.Configuration.LdapUsernameCollisionHandlingBehavior.CONVERT) {
                    java.util.Collection<org.apache.ambari.server.orm.entities.UserAuthenticationEntity> existingEntities = userEntity.getAuthenticationEntities();
                    if (existingEntities != null) {
                        java.util.Iterator<org.apache.ambari.server.orm.entities.UserAuthenticationEntity> iterator = existingEntities.iterator();
                        while (iterator.hasNext()) {
                            org.apache.ambari.server.orm.entities.UserAuthenticationEntity userAuthenticationEntity = iterator.next();
                            if (userAuthenticationEntity.getAuthenticationType() != org.apache.ambari.server.security.authorization.UserAuthenticationType.LDAP) {
                                removeAuthentication(userEntity, userAuthenticationEntity.getUserAuthenticationId());
                                iterator.remove();
                            }
                        } 
                    }
                }
                try {
                    addLdapAuthentication(userEntity, user.getDn(), false);
                    userEntitiesToUpdate.add(userEntity);
                } catch (org.apache.ambari.server.AmbariException e) {
                    org.apache.ambari.server.security.authorization.Users.LOG.warn(java.lang.String.format("Failed to enable LDAP authentication for the user account with the username %s: %s", userName, e.getLocalizedMessage()), e);
                }
            } else {
                org.apache.ambari.server.security.authorization.Users.LOG.warn("Failed to find user account for {} while enabling LDAP authentication for the user.", userName);
            }
        }
        userDAO.merge(userEntitiesToUpdate);
        final java.util.Set<org.apache.ambari.server.orm.entities.GroupEntity> groupsToBecomeLdap = new java.util.HashSet<>();
        for (org.apache.ambari.server.security.ldap.LdapGroupDto group : batchInfo.getGroupsToBecomeLdap()) {
            final org.apache.ambari.server.orm.entities.GroupEntity groupEntity = groupDAO.findGroupByName(group.getGroupName());
            groupEntity.setGroupType(org.apache.ambari.server.security.authorization.GroupType.LDAP);
            allGroups.put(groupEntity.getGroupName(), groupEntity);
            groupsToBecomeLdap.add(groupEntity);
        }
        groupDAO.merge(groupsToBecomeLdap);
        final java.util.List<org.apache.ambari.server.orm.entities.PrincipalEntity> principalsToCreate = new java.util.ArrayList<>();
        for (org.apache.ambari.server.security.ldap.LdapUserDto user : batchInfo.getUsersToBeCreated()) {
            java.lang.String userName = user.getUserName();
            org.apache.ambari.server.orm.entities.UserEntity userEntity;
            try {
                userEntity = createUser(userName, userName, userName, true);
            } catch (org.apache.ambari.server.AmbariException e) {
                org.apache.ambari.server.security.authorization.Users.LOG.error(java.lang.String.format("Failed to create new user: %s", userName), e);
                userEntity = null;
            }
            if (userEntity != null) {
                org.apache.ambari.server.security.authorization.Users.LOG.trace("Enabling LDAP authentication for the user account with the username {}.", userName);
                try {
                    addLdapAuthentication(userEntity, user.getDn(), false);
                } catch (org.apache.ambari.server.AmbariException e) {
                    org.apache.ambari.server.security.authorization.Users.LOG.warn(java.lang.String.format("Failed to enable LDAP authentication for the user account with the username %s: %s", userName, e.getLocalizedMessage()), e);
                }
                userDAO.merge(userEntity);
                allUsers.put(userEntity.getUserName(), userEntity);
            }
        }
        final java.util.Set<org.apache.ambari.server.orm.entities.GroupEntity> groupsToCreate = new java.util.HashSet<>();
        for (org.apache.ambari.server.security.ldap.LdapGroupDto group : batchInfo.getGroupsToBeCreated()) {
            final org.apache.ambari.server.orm.entities.PrincipalEntity principalEntity = new org.apache.ambari.server.orm.entities.PrincipalEntity();
            principalEntity.setPrincipalType(groupPrincipalType);
            principalsToCreate.add(principalEntity);
            final org.apache.ambari.server.orm.entities.GroupEntity groupEntity = new org.apache.ambari.server.orm.entities.GroupEntity();
            groupEntity.setGroupName(group.getGroupName());
            groupEntity.setPrincipal(principalEntity);
            groupEntity.setGroupType(org.apache.ambari.server.security.authorization.GroupType.LDAP);
            allGroups.put(groupEntity.getGroupName(), groupEntity);
            groupsToCreate.add(groupEntity);
        }
        principalDAO.create(principalsToCreate);
        groupDAO.create(groupsToCreate);
        final java.util.Set<org.apache.ambari.server.orm.entities.MemberEntity> membersToCreate = new java.util.HashSet<>();
        final java.util.Set<org.apache.ambari.server.orm.entities.GroupEntity> groupsToUpdate = new java.util.HashSet<>();
        for (org.apache.ambari.server.security.ldap.LdapUserGroupMemberDto member : batchInfo.getMembershipToAdd()) {
            final org.apache.ambari.server.orm.entities.MemberEntity memberEntity = new org.apache.ambari.server.orm.entities.MemberEntity();
            final org.apache.ambari.server.orm.entities.GroupEntity groupEntity = allGroups.get(member.getGroupName());
            memberEntity.setGroup(groupEntity);
            memberEntity.setUser(allUsers.get(member.getUserName()));
            groupEntity.getMemberEntities().add(memberEntity);
            groupsToUpdate.add(groupEntity);
            membersToCreate.add(memberEntity);
        }
        processLdapAdminGroupMappingRules(membersToCreate);
        memberDAO.create(membersToCreate);
        groupDAO.merge(groupsToUpdate);
        final java.util.Set<org.apache.ambari.server.orm.entities.MemberEntity> membersToRemove = new java.util.HashSet<>();
        for (org.apache.ambari.server.security.ldap.LdapUserGroupMemberDto member : batchInfo.getMembershipToRemove()) {
            org.apache.ambari.server.orm.entities.MemberEntity memberEntity = memberDAO.findByUserAndGroup(member.getUserName(), member.getGroupName());
            if (memberEntity != null) {
                membersToRemove.add(memberEntity);
            }
        }
        memberDAO.remove(membersToRemove);
        entityManagerProvider.get().getEntityManagerFactory().getCache().evictAll();
    }

    private void processLdapAdminGroupMappingRules(java.util.Set<org.apache.ambari.server.orm.entities.MemberEntity> membershipsToCreate) {
        if (membershipsToCreate.isEmpty()) {
            org.apache.ambari.server.security.authorization.Users.LOG.debug("There are no new memberships for which to process administrator group mapping rules.");
            return;
        }
        org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration ldapConfiguration = ldapConfigurationProvider.get();
        if (ldapConfiguration == null) {
            org.apache.ambari.server.security.authorization.Users.LOG.warn("The LDAP configuration is not available - no administrator group mappings will be processed.");
            return;
        }
        java.lang.String adminGroupMappings = ldapConfiguration.groupMappingRules();
        if (com.google.common.base.Strings.isNullOrEmpty(adminGroupMappings)) {
            org.apache.ambari.server.security.authorization.Users.LOG.debug("There are no administrator group mappings to be processed.");
            return;
        }
        org.apache.ambari.server.security.authorization.Users.LOG.info("Processing admin group mapping rules [{}]. Membership entry count: [{}]", adminGroupMappings, membershipsToCreate.size());
        java.util.Set<java.lang.String> ldapAdminGroups = com.google.common.collect.Sets.newHashSet(adminGroupMappings.split(","));
        java.util.Set<org.apache.ambari.server.orm.entities.UserEntity> ambariAdminProspects = com.google.common.collect.Sets.newHashSet();
        for (org.apache.ambari.server.orm.entities.MemberEntity memberEntity : membershipsToCreate) {
            if (ldapAdminGroups.contains(memberEntity.getGroup().getGroupName())) {
                org.apache.ambari.server.security.authorization.Users.LOG.debug("Ambari admin user prospect: [{}] ", memberEntity.getUser().getUserName());
                ambariAdminProspects.add(memberEntity.getUser());
            }
        }
        for (org.apache.ambari.server.orm.entities.UserEntity userEntity : ambariAdminProspects) {
            org.apache.ambari.server.security.authorization.Users.LOG.info("Granting ambari admin roles to the user: {}", userEntity.getUserName());
            grantAdminPrivilege(userEntity.getUserId());
        }
    }

    private java.util.Map<java.lang.String, java.util.Set<java.lang.String>> getUsersToGroupMap(java.util.Set<org.apache.ambari.server.orm.entities.UserEntity> usersToCreate) {
        java.util.Map<java.lang.String, java.util.Set<java.lang.String>> usersToGroups = new java.util.HashMap<>();
        for (org.apache.ambari.server.orm.entities.UserEntity userEntity : usersToCreate) {
            userEntity = userDAO.findByPK(userEntity.getUserId());
            usersToGroups.put(userEntity.getUserName(), new java.util.HashSet<>());
            for (org.apache.ambari.server.orm.entities.MemberEntity memberEntity : userEntity.getMemberEntities()) {
                usersToGroups.get(userEntity.getUserName()).add(memberEntity.getGroup().getGroupName());
            }
        }
        return usersToGroups;
    }

    public java.util.Collection<org.apache.ambari.server.orm.entities.PrivilegeEntity> getUserPrivileges(org.apache.ambari.server.orm.entities.UserEntity userEntity) {
        if (userEntity == null) {
            return java.util.Collections.emptyList();
        }
        java.util.List<org.apache.ambari.server.orm.entities.PrincipalEntity> principalEntities = new java.util.LinkedList<>();
        principalEntities.add(userEntity.getPrincipal());
        java.util.List<org.apache.ambari.server.orm.entities.MemberEntity> memberEntities = memberDAO.findAllMembersByUser(userEntity);
        for (org.apache.ambari.server.orm.entities.MemberEntity memberEntity : memberEntities) {
            principalEntities.add(memberEntity.getGroup().getPrincipal());
        }
        java.util.List<org.apache.ambari.server.orm.entities.PrivilegeEntity> explicitPrivilegeEntities = privilegeDAO.findAllByPrincipal(principalEntities);
        java.util.List<org.apache.ambari.server.orm.entities.PrivilegeEntity> implicitPrivilegeEntities = getImplicitPrivileges(explicitPrivilegeEntities);
        java.util.List<org.apache.ambari.server.orm.entities.PrivilegeEntity> privilegeEntities;
        if (implicitPrivilegeEntities.isEmpty()) {
            privilegeEntities = explicitPrivilegeEntities;
        } else {
            privilegeEntities = new java.util.LinkedList<>();
            privilegeEntities.addAll(explicitPrivilegeEntities);
            privilegeEntities.addAll(implicitPrivilegeEntities);
        }
        return privilegeEntities;
    }

    public java.util.Collection<org.apache.ambari.server.orm.entities.PrivilegeEntity> getGroupPrivileges(org.apache.ambari.server.orm.entities.GroupEntity groupEntity) {
        if (groupEntity == null) {
            return java.util.Collections.emptyList();
        }
        java.util.List<org.apache.ambari.server.orm.entities.PrincipalEntity> principalEntities = new java.util.LinkedList<>();
        principalEntities.add(groupEntity.getPrincipal());
        java.util.List<org.apache.ambari.server.orm.entities.PrivilegeEntity> explicitPrivilegeEntities = privilegeDAO.findAllByPrincipal(principalEntities);
        java.util.List<org.apache.ambari.server.orm.entities.PrivilegeEntity> implicitPrivilegeEntities = getImplicitPrivileges(explicitPrivilegeEntities);
        java.util.List<org.apache.ambari.server.orm.entities.PrivilegeEntity> privilegeEntities;
        if (implicitPrivilegeEntities.isEmpty()) {
            privilegeEntities = explicitPrivilegeEntities;
        } else {
            privilegeEntities = new java.util.LinkedList<>();
            privilegeEntities.addAll(explicitPrivilegeEntities);
            privilegeEntities.addAll(implicitPrivilegeEntities);
        }
        return privilegeEntities;
    }

    public java.util.Collection<org.apache.ambari.server.security.authorization.AmbariGrantedAuthority> getUserAuthorities(java.lang.String userName) {
        return getUserAuthorities(getUserEntity(userName));
    }

    public java.util.Collection<org.apache.ambari.server.security.authorization.AmbariGrantedAuthority> getUserAuthorities(org.apache.ambari.server.orm.entities.UserEntity userEntity) {
        if (userEntity == null) {
            return java.util.Collections.emptyList();
        }
        java.util.Collection<org.apache.ambari.server.orm.entities.PrivilegeEntity> privilegeEntities = getUserPrivileges(userEntity);
        java.util.Set<org.apache.ambari.server.security.authorization.AmbariGrantedAuthority> authorities = new java.util.HashSet<>(privilegeEntities.size());
        for (org.apache.ambari.server.orm.entities.PrivilegeEntity privilegeEntity : privilegeEntities) {
            authorities.add(new org.apache.ambari.server.security.authorization.AmbariGrantedAuthority(privilegeEntity));
        }
        return authorities;
    }

    private java.util.List<org.apache.ambari.server.orm.entities.PrivilegeEntity> getImplicitPrivileges(java.util.List<org.apache.ambari.server.orm.entities.PrivilegeEntity> privilegeEntities) {
        if ((privilegeEntities == null) || privilegeEntities.isEmpty()) {
            return java.util.Collections.emptyList();
        }
        java.util.List<org.apache.ambari.server.orm.entities.PrivilegeEntity> implicitPrivileges = new java.util.LinkedList<>();
        java.util.List<org.apache.ambari.server.orm.entities.PrincipalEntity> rolePrincipals = new java.util.ArrayList<>();
        for (org.apache.ambari.server.orm.entities.PrivilegeEntity privilegeEntity : privilegeEntities) {
            org.apache.ambari.server.orm.entities.PrincipalEntity rolePrincipal = privilegeEntity.getPermission().getPrincipal();
            if (rolePrincipal != null) {
                rolePrincipals.add(rolePrincipal);
            }
        }
        if (!rolePrincipals.isEmpty()) {
            implicitPrivileges.addAll(privilegeDAO.findAllByPrincipal(rolePrincipals));
        }
        return implicitPrivileges;
    }

    public java.util.Collection<org.apache.ambari.server.orm.entities.UserAuthenticationEntity> getUserAuthenticationEntities(java.lang.String username, org.apache.ambari.server.security.authorization.UserAuthenticationType authenticationType) {
        org.apache.ambari.server.orm.entities.UserEntity userEntity;
        if (!org.apache.commons.lang.StringUtils.isEmpty(username)) {
            userEntity = userDAO.findUserByName(username);
            if (userEntity == null) {
                return null;
            }
        } else {
            userEntity = null;
        }
        return getUserAuthenticationEntities(userEntity, authenticationType);
    }

    public java.util.Collection<org.apache.ambari.server.orm.entities.UserAuthenticationEntity> getUserAuthenticationEntities(org.apache.ambari.server.orm.entities.UserEntity userEntity, org.apache.ambari.server.security.authorization.UserAuthenticationType authenticationType) {
        if (userEntity == null) {
            if (authenticationType == null) {
                return userAuthenticationDAO.findAll();
            } else {
                return userAuthenticationDAO.findByType(authenticationType);
            }
        } else {
            java.util.List<org.apache.ambari.server.orm.entities.UserAuthenticationEntity> authenticationEntities = userAuthenticationDAO.findByUser(userEntity);
            if (authenticationType == null) {
                return authenticationEntities;
            } else {
                java.util.List<org.apache.ambari.server.orm.entities.UserAuthenticationEntity> pruned = new java.util.ArrayList<>();
                for (org.apache.ambari.server.orm.entities.UserAuthenticationEntity authenticationEntity : authenticationEntities) {
                    if (authenticationEntity.getAuthenticationType() == authenticationType) {
                        pruned.add(authenticationEntity);
                    }
                }
                return pruned;
            }
        }
    }

    public java.util.Collection<org.apache.ambari.server.orm.entities.UserAuthenticationEntity> getUserAuthenticationEntities(org.apache.ambari.server.security.authorization.UserAuthenticationType authenticationType, java.lang.String key) {
        return userAuthenticationDAO.findByTypeAndKey(authenticationType, key);
    }

    @com.google.inject.persist.Transactional
    public synchronized void modifyAuthentication(org.apache.ambari.server.orm.entities.UserAuthenticationEntity userAuthenticationEntity, java.lang.String currentKey, java.lang.String newKey, boolean isSelf) throws org.apache.ambari.server.AmbariException {
        if (userAuthenticationEntity != null) {
            if (userAuthenticationEntity.getAuthenticationType() == org.apache.ambari.server.security.authorization.UserAuthenticationType.LOCAL) {
                java.lang.String expectedCurrentKey = (isSelf) ? userAuthenticationEntity.getAuthenticationKey() : getAuthenticatedUserLocalAuthenticationMethod().orElseThrow(() -> new org.apache.ambari.server.AmbariException("Authentication error")).getAuthenticationKey();
                if (org.apache.commons.lang.StringUtils.isEmpty(currentKey) || (!passwordEncoder.matches(currentKey, expectedCurrentKey))) {
                    throw new org.apache.ambari.server.AmbariException("Wrong current password provided");
                }
                validatePassword(newKey, userAuthenticationEntity.getUser().getUserName(), userAuthenticationEntity.getFullAuthenticationKey());
                userAuthenticationEntity.updateAuthenticationKey(passwordEncoder.encode(newKey), configuration.getPasswordPolicyHistoryCount());
            } else {
                userAuthenticationEntity.updateAuthenticationKey(newKey, configuration.getPasswordPolicyHistoryCount());
            }
            userAuthenticationDAO.merge(userAuthenticationEntity);
        }
    }

    private java.util.Optional<org.apache.ambari.server.security.authorization.AuthenticationMethod> getAuthenticatedUserLocalAuthenticationMethod() {
        org.apache.ambari.server.security.authorization.User authenticatedUser = getUser(org.apache.ambari.server.security.authorization.AuthorizationHelper.getAuthenticatedId());
        return authenticatedUser.getAuthenticationMethods().stream().filter(am -> org.apache.ambari.server.security.authorization.UserAuthenticationType.LOCAL.equals(am.getAuthenticationType())).findAny();
    }

    public void removeAuthentication(java.lang.String username, java.lang.Long authenticationId) {
        removeAuthentication(getUserEntity(username), authenticationId);
    }

    @com.google.inject.persist.Transactional
    public void removeAuthentication(org.apache.ambari.server.orm.entities.UserEntity userEntity, java.lang.Long authenticationId) {
        if ((userEntity != null) && (authenticationId != null)) {
            boolean changed = false;
            userEntity = userDAO.findByPK(userEntity.getUserId());
            java.util.List<org.apache.ambari.server.orm.entities.UserAuthenticationEntity> authenticationEntities = userEntity.getAuthenticationEntities();
            java.util.Iterator<org.apache.ambari.server.orm.entities.UserAuthenticationEntity> iterator = authenticationEntities.iterator();
            while (iterator.hasNext()) {
                org.apache.ambari.server.orm.entities.UserAuthenticationEntity authenticationEntity = iterator.next();
                if (authenticationId.equals(authenticationEntity.getUserAuthenticationId())) {
                    userAuthenticationDAO.remove(authenticationEntity);
                    iterator.remove();
                    changed = true;
                    break;
                }
            } 
            if (changed) {
                userDAO.merge(userEntity);
            }
        }
    }

    public void addAuthentication(org.apache.ambari.server.orm.entities.UserEntity userEntity, org.apache.ambari.server.security.authorization.UserAuthenticationType authenticationType, java.lang.String key) throws org.apache.ambari.server.AmbariException {
        switch (authenticationType) {
            case LOCAL :
                addLocalAuthentication(userEntity, key);
                break;
            case LDAP :
                addLdapAuthentication(userEntity, key);
                break;
            case JWT :
                addJWTAuthentication(userEntity, key);
                break;
            case PAM :
                addPamAuthentication(userEntity, key);
                break;
            case KERBEROS :
                addKerberosAuthentication(userEntity, key);
                break;
            default :
                throw new org.apache.ambari.server.AmbariException("Unexpected user authentication type");
        }
    }

    public void addJWTAuthentication(org.apache.ambari.server.orm.entities.UserEntity userEntity, java.lang.String key) throws org.apache.ambari.server.AmbariException {
        addJWTAuthentication(userEntity, key, true);
    }

    public void addJWTAuthentication(org.apache.ambari.server.orm.entities.UserEntity userEntity, java.lang.String key, boolean persist) throws org.apache.ambari.server.AmbariException {
        addAuthentication(userEntity, org.apache.ambari.server.security.authorization.UserAuthenticationType.JWT, key, (user, authKey) -> {
            java.util.List<org.apache.ambari.server.orm.entities.UserAuthenticationEntity> authenticationEntities = user.getAuthenticationEntities();
            for (org.apache.ambari.server.orm.entities.UserAuthenticationEntity entity : authenticationEntities) {
                if ((entity.getAuthenticationType() == org.apache.ambari.server.security.authorization.UserAuthenticationType.JWT) && (authKey == null ? entity.getAuthenticationKey() == null : authKey.equals(entity.getAuthenticationKey()))) {
                    throw new org.apache.ambari.server.AmbariException("The authentication type already exists for this user");
                }
            }
        }, persist);
    }

    public void addKerberosAuthentication(org.apache.ambari.server.orm.entities.UserEntity userEntity, java.lang.String principalName) throws org.apache.ambari.server.AmbariException {
        addKerberosAuthentication(userEntity, principalName, true);
    }

    public void addKerberosAuthentication(org.apache.ambari.server.orm.entities.UserEntity userEntity, java.lang.String principalName, boolean persist) throws org.apache.ambari.server.AmbariException {
        addAuthentication(userEntity, org.apache.ambari.server.security.authorization.UserAuthenticationType.KERBEROS, principalName, (user, key) -> {
            if (!org.apache.commons.collections.CollectionUtils.isEmpty(userAuthenticationDAO.findByTypeAndKey(org.apache.ambari.server.security.authorization.UserAuthenticationType.KERBEROS, key))) {
                throw new org.apache.ambari.server.AmbariException("The authentication type already exists for this principal");
            }
        }, persist);
    }

    public void addLocalAuthentication(org.apache.ambari.server.orm.entities.UserEntity userEntity, java.lang.String password) throws org.apache.ambari.server.AmbariException {
        addLocalAuthentication(userEntity, password, true);
    }

    public void addLocalAuthentication(org.apache.ambari.server.orm.entities.UserEntity userEntity, java.lang.String password, boolean persist) throws org.apache.ambari.server.AmbariException {
        validatePassword(password, userEntity.getUserName());
        java.lang.String encodedPassword = passwordEncoder.encode(password);
        addAuthentication(userEntity, org.apache.ambari.server.security.authorization.UserAuthenticationType.LOCAL, encodedPassword, (user, key) -> {
            java.util.List<org.apache.ambari.server.orm.entities.UserAuthenticationEntity> authenticationEntities = user.getAuthenticationEntities();
            for (org.apache.ambari.server.orm.entities.UserAuthenticationEntity entity : authenticationEntities) {
                if (entity.getAuthenticationType() == org.apache.ambari.server.security.authorization.UserAuthenticationType.LOCAL) {
                    throw new org.apache.ambari.server.AmbariException("The authentication type already exists for this user");
                }
            }
        }, persist);
    }

    public void addPamAuthentication(org.apache.ambari.server.orm.entities.UserEntity userEntity, java.lang.String userName) throws org.apache.ambari.server.AmbariException {
        addPamAuthentication(userEntity, userName, true);
    }

    public void addPamAuthentication(org.apache.ambari.server.orm.entities.UserEntity userEntity, java.lang.String userName, boolean persist) throws org.apache.ambari.server.AmbariException {
        addAuthentication(userEntity, org.apache.ambari.server.security.authorization.UserAuthenticationType.PAM, userName, (user, key) -> {
            java.util.List<org.apache.ambari.server.orm.entities.UserAuthenticationEntity> authenticationEntities = user.getAuthenticationEntities();
            for (org.apache.ambari.server.orm.entities.UserAuthenticationEntity entity : authenticationEntities) {
                if (entity.getAuthenticationType() == org.apache.ambari.server.security.authorization.UserAuthenticationType.PAM) {
                    throw new org.apache.ambari.server.AmbariException("The authentication type already exists for this user");
                }
            }
        }, persist);
    }

    public void addLdapAuthentication(org.apache.ambari.server.orm.entities.UserEntity userEntity, java.lang.String dn) throws org.apache.ambari.server.AmbariException {
        addLdapAuthentication(userEntity, dn, true);
    }

    public void addLdapAuthentication(org.apache.ambari.server.orm.entities.UserEntity userEntity, java.lang.String dn, boolean persist) throws org.apache.ambari.server.AmbariException {
        addAuthentication(userEntity, org.apache.ambari.server.security.authorization.UserAuthenticationType.LDAP, org.apache.commons.lang.StringUtils.lowerCase(dn), (user, key) -> {
            List<org.apache.ambari.server.orm.entities.UserAuthenticationEntity> authenticationEntities = user.getAuthenticationEntities();
            for (org.apache.ambari.server.orm.entities.UserAuthenticationEntity entity : authenticationEntities) {
                if ((entity.getAuthenticationType() == UserAuthenticationType.LDAP) && (key == null ? entity.getAuthenticationKey() == null : key.equalsIgnoreCase(entity.getAuthenticationKey()))) {
                    throw new org.apache.ambari.server.AmbariException("The authentication type already exists for this user");
                }
            }
        }, persist);
    }

    private void addAuthentication(org.apache.ambari.server.orm.entities.UserEntity userEntity, org.apache.ambari.server.security.authorization.UserAuthenticationType type, java.lang.String key, org.apache.ambari.server.security.authorization.Users.Validator validator, boolean persist) throws org.apache.ambari.server.AmbariException {
        if (userEntity == null) {
            throw new org.apache.ambari.server.AmbariException("Missing user");
        }
        validator.validate(userEntity, key);
        java.util.List<org.apache.ambari.server.orm.entities.UserAuthenticationEntity> authenticationEntities = userAuthenticationDAO.findByUser(userEntity);
        org.apache.ambari.server.orm.entities.UserAuthenticationEntity authenticationEntity = new org.apache.ambari.server.orm.entities.UserAuthenticationEntity();
        authenticationEntity.setUser(userEntity);
        authenticationEntity.setAuthenticationType(type);
        authenticationEntity.setAuthenticationKey(key);
        authenticationEntities.add(authenticationEntity);
        userEntity.setAuthenticationEntities(authenticationEntities);
        if (persist) {
            userDAO.merge(userEntity);
        }
    }

    public java.lang.Integer incrementConsecutiveAuthenticationFailures(java.lang.String username) {
        return incrementConsecutiveAuthenticationFailures(getUserEntity(username));
    }

    public java.lang.Integer incrementConsecutiveAuthenticationFailures(org.apache.ambari.server.orm.entities.UserEntity userEntity) {
        if (userEntity != null) {
            org.apache.ambari.server.security.authorization.Users.Command command = new org.apache.ambari.server.security.authorization.Users.Command() {
                @java.lang.Override
                public void perform(org.apache.ambari.server.orm.entities.UserEntity userEntity) {
                    userEntity.incrementConsecutiveFailures();
                }
            };
            userEntity = safelyUpdateUserEntity(userEntity, command, org.apache.ambari.server.security.authorization.Users.MAX_RETRIES);
        }
        return userEntity == null ? null : userEntity.getConsecutiveFailures();
    }

    public void clearConsecutiveAuthenticationFailures(java.lang.String username) {
        clearConsecutiveAuthenticationFailures(getUserEntity(username));
    }

    public void clearConsecutiveAuthenticationFailures(org.apache.ambari.server.orm.entities.UserEntity userEntity) {
        if (userEntity != null) {
            if (userEntity.getConsecutiveFailures() != 0) {
                org.apache.ambari.server.security.authorization.Users.Command command = new org.apache.ambari.server.security.authorization.Users.Command() {
                    @java.lang.Override
                    public void perform(org.apache.ambari.server.orm.entities.UserEntity userEntity) {
                        userEntity.setConsecutiveFailures(0);
                    }
                };
                safelyUpdateUserEntity(userEntity, command, org.apache.ambari.server.security.authorization.Users.MAX_RETRIES);
            }
        }
    }

    public org.apache.ambari.server.orm.entities.UserEntity safelyUpdateUserEntity(org.apache.ambari.server.orm.entities.UserEntity userEntity, org.apache.ambari.server.security.authorization.Users.Command command) {
        return safelyUpdateUserEntity(userEntity, command, org.apache.ambari.server.security.authorization.Users.MAX_RETRIES);
    }

    public org.apache.ambari.server.orm.entities.UserEntity safelyUpdateUserEntity(org.apache.ambari.server.orm.entities.UserEntity userEntity, org.apache.ambari.server.security.authorization.Users.Command command, int maxRetries) {
        int retriesLeft = maxRetries;
        do {
            try {
                command.perform(userEntity);
                userDAO.merge(userEntity);
                return userEntity;
            } catch (java.lang.Throwable t) {
                java.lang.Throwable cause = t;
                int failSafe = 50;
                do {
                    if (cause instanceof javax.persistence.OptimisticLockException) {
                        java.lang.Integer userID = userEntity.getUserId();
                        userEntity = userDAO.findByPK(userID);
                        if (userEntity == null) {
                            org.apache.ambari.server.security.authorization.Users.LOG.warn("Failed to find user with user id of {}.  The user may have been removed. Aborting.", userID);
                            return null;
                        }
                        retriesLeft--;
                        if (retriesLeft == 0) {
                            org.apache.ambari.server.security.authorization.Users.LOG.error("Failed to update the user's ({}) consecutive failures value due to an OptimisticLockException.  Aborting.", userEntity.getUserName());
                            throw t;
                        } else {
                            org.apache.ambari.server.security.authorization.Users.LOG.warn("Failed to update the user's ({}) consecutive failures value due to an OptimisticLockException.  {} retries left, retrying...", userEntity.getUserName(), retriesLeft);
                        }
                        break;
                    } else {
                        cause = cause.getCause();
                    }
                    failSafe--;
                } while (((cause != null) && (cause != t)) && (failSafe > 0) );
                if (((cause == null) || (cause == t)) || (failSafe == 0)) {
                    throw t;
                }
            }
        } while (retriesLeft > 0 );
        return userEntity;
    }

    public void validatePassword(java.lang.String password) {
        if (org.apache.commons.lang.StringUtils.isEmpty(password)) {
            throw new java.lang.IllegalArgumentException("The password does not meet the password policy requirements");
        }
        java.lang.String regexp = configuration.getPasswordPolicyRegexp();
        if ((!org.apache.commons.lang.StringUtils.isEmpty(regexp)) && (!java.util.regex.Pattern.matches(regexp, password))) {
            final java.lang.String msg = "The password does not meet the Ambari user password policy : " + configuration.getPasswordPolicyDescription();
            throw new java.lang.IllegalArgumentException(msg);
        }
    }

    public void validatePassword(java.lang.String password, java.lang.String userName) {
        validatePassword(password);
        if (org.apache.commons.lang.StringUtils.isNotEmpty(userName) && java.util.regex.Pattern.compile(java.util.regex.Pattern.quote(userName), java.util.regex.Pattern.CASE_INSENSITIVE).matcher(password).find()) {
            final java.lang.String msg = "The password does not meet the Ambari user password policy : password cannot contain the username";
            throw new java.lang.IllegalArgumentException(msg);
        }
    }

    public void validatePassword(java.lang.String password, java.lang.String userName, java.lang.String authenticationKey) {
        validatePassword(password, userName);
        boolean isMatched = false;
        java.lang.String[] previousPasswords = org.apache.commons.lang.StringUtils.split(authenticationKey, ",");
        for (java.lang.String previousPassword : previousPasswords) {
            if (passwordEncoder.matches(password, previousPassword)) {
                isMatched = true;
                break;
            }
        }
        if (isMatched) {
            final java.lang.String msg = ("The password does not meet the Ambari user password policy : new password can not be same as previous " + configuration.getPasswordPolicyHistoryCount()) + " passwords.";
            throw new java.lang.IllegalArgumentException(msg);
        }
    }

    private interface Validator {
        void validate(org.apache.ambari.server.orm.entities.UserEntity userEntity, java.lang.String key) throws org.apache.ambari.server.AmbariException;
    }

    public interface Command {
        void perform(org.apache.ambari.server.orm.entities.UserEntity userEntity);
    }
}