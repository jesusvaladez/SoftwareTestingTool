package org.apache.ambari.server.security.ldap;
import org.apache.commons.lang.StringUtils;
import org.springframework.ldap.control.PagedResultsDirContextProcessor;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.ContextMapper;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.filter.Filter;
import org.springframework.ldap.filter.HardcodedFilter;
import org.springframework.ldap.filter.LikeFilter;
import org.springframework.ldap.filter.OrFilter;
import org.springframework.ldap.support.LdapUtils;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
public class AmbariLdapDataPopulator {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.security.ldap.AmbariLdapDataPopulator.class);

    private com.google.inject.Provider<org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration> configurationProvider;

    private org.apache.ambari.server.security.authorization.Users users;

    protected org.apache.ambari.server.security.authorization.LdapServerProperties ldapServerProperties;

    private org.springframework.ldap.core.LdapTemplate ldapTemplate;

    private static final java.lang.String UID_ATTRIBUTE = "uid";

    private static final java.lang.String OBJECT_CLASS_ATTRIBUTE = "objectClass";

    private static final int USERS_PAGE_SIZE = 500;

    private static final java.lang.String SYSTEM_PROPERTY_DISABLE_ENDPOINT_IDENTIFICATION = "com.sun.jndi.ldap.object.disableEndpointIdentification";

    private static final java.lang.String IS_MEMBER_DN_REGEXP = "^(?i)(uid|cn|%s|%s)=.*$";

    private static final java.lang.String MEMBER_ATTRIBUTE_REPLACE_STRING = "${member}";

    private static final java.lang.String MEMBER_ATTRIBUTE_VALUE_PLACEHOLDER = "{member}";

    @com.google.inject.Inject
    public AmbariLdapDataPopulator(com.google.inject.Provider<org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration> configurationProvider, org.apache.ambari.server.security.authorization.Users users) {
        this.configurationProvider = configurationProvider;
        this.users = users;
        this.ldapServerProperties = null;
    }

    private synchronized org.apache.ambari.server.security.authorization.LdapServerProperties getLdapProperties() {
        if (ldapServerProperties == null) {
            ldapServerProperties = getConfiguration().getLdapServerProperties();
        }
        return ldapServerProperties;
    }

    public boolean isLdapEnabled() {
        if (!getConfiguration().ldapEnabled()) {
            return false;
        }
        try {
            final org.springframework.ldap.core.LdapTemplate ldapTemplate = loadLdapTemplate();
            ldapTemplate.search(getLdapProperties().getBaseDN(), "uid=dummy_search", new org.springframework.ldap.core.AttributesMapper() {
                @java.lang.Override
                public java.lang.Object mapFromAttributes(javax.naming.directory.Attributes arg0) throws javax.naming.NamingException {
                    return null;
                }
            });
            return true;
        } catch (java.lang.Exception ex) {
            org.apache.ambari.server.security.ldap.AmbariLdapDataPopulator.LOG.error("Could not connect to LDAP server - " + ex.getMessage());
            return false;
        }
    }

    public org.apache.ambari.server.security.ldap.LdapSyncDto getLdapSyncInfo() {
        final org.apache.ambari.server.security.ldap.LdapSyncDto syncInfo = new org.apache.ambari.server.security.ldap.LdapSyncDto();
        final java.util.Map<java.lang.String, org.apache.ambari.server.security.authorization.Group> internalGroupsMap = getInternalGroups();
        final java.util.Set<org.apache.ambari.server.security.ldap.LdapGroupDto> externalGroups = getExternalLdapGroupInfo();
        for (org.apache.ambari.server.security.ldap.LdapGroupDto externalGroup : externalGroups) {
            if (internalGroupsMap.containsKey(externalGroup.getGroupName()) && internalGroupsMap.get(externalGroup.getGroupName()).isLdapGroup()) {
                externalGroup.setSynced(true);
            } else {
                externalGroup.setSynced(false);
            }
        }
        final java.util.Map<java.lang.String, org.apache.ambari.server.security.authorization.User> internalUsersMap = getInternalUsers();
        final java.util.Set<org.apache.ambari.server.security.ldap.LdapUserDto> externalUsers = getExternalLdapUserInfo();
        for (org.apache.ambari.server.security.ldap.LdapUserDto externalUser : externalUsers) {
            java.lang.String userName = externalUser.getUserName();
            if (internalUsersMap.containsKey(userName) && internalUsersMap.get(userName).isLdapUser()) {
                externalUser.setSynced(true);
            } else {
                externalUser.setSynced(false);
            }
        }
        syncInfo.setGroups(externalGroups);
        syncInfo.setUsers(externalUsers);
        return syncInfo;
    }

    public org.apache.ambari.server.security.ldap.LdapBatchDto synchronizeAllLdapGroups(org.apache.ambari.server.security.ldap.LdapBatchDto batchInfo, boolean collectIgnoredUsers) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.security.ldap.AmbariLdapDataPopulator.LOG.trace("Synchronize All LDAP groups...");
        java.util.Set<org.apache.ambari.server.security.ldap.LdapGroupDto> externalLdapGroupInfo = getExternalLdapGroupInfo();
        final java.util.Map<java.lang.String, org.apache.ambari.server.security.authorization.Group> internalGroupsMap = getInternalGroups();
        final java.util.Map<java.lang.String, org.apache.ambari.server.security.authorization.User> internalUsersMap = getInternalUsers();
        for (org.apache.ambari.server.security.ldap.LdapGroupDto groupDto : externalLdapGroupInfo) {
            addLdapGroup(batchInfo, internalGroupsMap, groupDto);
            refreshGroupMembers(batchInfo, groupDto, internalUsersMap, internalGroupsMap, null, false, collectIgnoredUsers);
        }
        for (java.util.Map.Entry<java.lang.String, org.apache.ambari.server.security.authorization.Group> internalGroup : internalGroupsMap.entrySet()) {
            if (internalGroup.getValue().isLdapGroup()) {
                org.apache.ambari.server.security.ldap.LdapGroupDto groupDto = new org.apache.ambari.server.security.ldap.LdapGroupDto();
                groupDto.setGroupName(internalGroup.getValue().getGroupName());
                batchInfo.getGroupsToBeRemoved().add(groupDto);
            }
        }
        return batchInfo;
    }

    public org.apache.ambari.server.security.ldap.LdapBatchDto synchronizeAllLdapUsers(org.apache.ambari.server.security.ldap.LdapBatchDto batchInfo, boolean collectIgnoredUsers) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.security.ldap.AmbariLdapDataPopulator.LOG.trace("Synchronize All LDAP users...");
        java.util.Set<org.apache.ambari.server.security.ldap.LdapUserDto> externalLdapUserInfo = getExternalLdapUserInfo();
        java.util.Map<java.lang.String, org.apache.ambari.server.security.authorization.User> internalUsersMap = getInternalUsers();
        for (org.apache.ambari.server.security.ldap.LdapUserDto userDto : externalLdapUserInfo) {
            java.lang.String userName = userDto.getUserName();
            if (internalUsersMap.containsKey(userName)) {
                final org.apache.ambari.server.security.authorization.User user = internalUsersMap.get(userName);
                if ((user != null) && (!user.isLdapUser())) {
                    if (org.apache.ambari.server.configuration.LdapUsernameCollisionHandlingBehavior.SKIP == getConfiguration().syncCollisionHandlingBehavior()) {
                        org.apache.ambari.server.security.ldap.AmbariLdapDataPopulator.LOG.info("User '{}' skipped because it is local user", userName);
                        batchInfo.getUsersSkipped().add(userDto);
                    } else {
                        batchInfo.getUsersToBecomeLdap().add(userDto);
                        org.apache.ambari.server.security.ldap.AmbariLdapDataPopulator.LOG.trace("Convert user '{}' to LDAP user.", userName);
                    }
                } else if (collectIgnoredUsers) {
                    batchInfo.getUsersIgnored().add(userDto);
                }
                internalUsersMap.remove(userName);
            } else {
                batchInfo.getUsersToBeCreated().add(userDto);
            }
        }
        for (java.util.Map.Entry<java.lang.String, org.apache.ambari.server.security.authorization.User> internalUser : internalUsersMap.entrySet()) {
            if (internalUser.getValue().isLdapUser()) {
                org.apache.ambari.server.security.ldap.LdapUserDto userDto = new org.apache.ambari.server.security.ldap.LdapUserDto();
                userDto.setUserName(internalUser.getValue().getUserName());
                userDto.setDn(null);
                batchInfo.getUsersToBeRemoved().add(userDto);
            }
        }
        return batchInfo;
    }

    public org.apache.ambari.server.security.ldap.LdapBatchDto synchronizeLdapGroups(java.util.Set<java.lang.String> groups, org.apache.ambari.server.security.ldap.LdapBatchDto batchInfo, boolean collectIgnoredUsers) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.security.ldap.AmbariLdapDataPopulator.LOG.trace("Synchronize LDAP groups...");
        final java.util.Set<org.apache.ambari.server.security.ldap.LdapGroupDto> specifiedGroups = new java.util.HashSet<>();
        for (java.lang.String group : groups) {
            java.util.Set<org.apache.ambari.server.security.ldap.LdapGroupDto> groupDtos = getLdapGroups(group);
            if (groupDtos.isEmpty()) {
                throw new org.apache.ambari.server.AmbariException(("Couldn't sync LDAP group " + group) + ", it doesn't exist");
            }
            specifiedGroups.addAll(groupDtos);
        }
        final java.util.Map<java.lang.String, org.apache.ambari.server.security.authorization.Group> internalGroupsMap = getInternalGroups();
        final java.util.Map<java.lang.String, org.apache.ambari.server.security.authorization.User> internalUsersMap = getInternalUsers();
        for (org.apache.ambari.server.security.ldap.LdapGroupDto groupDto : specifiedGroups) {
            addLdapGroup(batchInfo, internalGroupsMap, groupDto);
            refreshGroupMembers(batchInfo, groupDto, internalUsersMap, internalGroupsMap, null, true, collectIgnoredUsers);
        }
        return batchInfo;
    }

    public org.apache.ambari.server.security.ldap.LdapBatchDto synchronizeLdapUsers(java.util.Set<java.lang.String> users, org.apache.ambari.server.security.ldap.LdapBatchDto batchInfo, boolean collectIgnoredUsers) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.security.ldap.AmbariLdapDataPopulator.LOG.trace("Synchronize LDAP users...");
        final java.util.Set<org.apache.ambari.server.security.ldap.LdapUserDto> specifiedUsers = new java.util.HashSet<>();
        for (java.lang.String user : users) {
            java.util.Set<org.apache.ambari.server.security.ldap.LdapUserDto> userDtos = getLdapUsers(user);
            if (userDtos.isEmpty()) {
                throw new org.apache.ambari.server.AmbariException(("Couldn't sync LDAP user " + user) + ", it doesn't exist");
            }
            specifiedUsers.addAll(userDtos);
        }
        final java.util.Map<java.lang.String, org.apache.ambari.server.security.authorization.User> internalUsersMap = getInternalUsers();
        for (org.apache.ambari.server.security.ldap.LdapUserDto userDto : specifiedUsers) {
            java.lang.String userName = userDto.getUserName();
            if (internalUsersMap.containsKey(userName)) {
                final org.apache.ambari.server.security.authorization.User user = internalUsersMap.get(userName);
                if ((user != null) && (!user.isLdapUser())) {
                    if (org.apache.ambari.server.configuration.LdapUsernameCollisionHandlingBehavior.SKIP == getConfiguration().syncCollisionHandlingBehavior()) {
                        org.apache.ambari.server.security.ldap.AmbariLdapDataPopulator.LOG.info("User '{}' skipped because it is local user", userName);
                        batchInfo.getUsersSkipped().add(userDto);
                    } else {
                        batchInfo.getUsersToBecomeLdap().add(userDto);
                    }
                } else if (collectIgnoredUsers) {
                    batchInfo.getUsersIgnored().add(userDto);
                }
                internalUsersMap.remove(userName);
            } else {
                batchInfo.getUsersToBeCreated().add(userDto);
            }
        }
        return batchInfo;
    }

    public org.apache.ambari.server.security.ldap.LdapBatchDto synchronizeExistingLdapGroups(org.apache.ambari.server.security.ldap.LdapBatchDto batchInfo, boolean collectIgnoredUsers) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.security.ldap.AmbariLdapDataPopulator.LOG.trace("Synchronize Existing LDAP groups...");
        final java.util.Map<java.lang.String, org.apache.ambari.server.security.authorization.Group> internalGroupsMap = getInternalGroups();
        final java.util.Map<java.lang.String, org.apache.ambari.server.security.authorization.User> internalUsersMap = getInternalUsers();
        final java.util.Set<org.apache.ambari.server.security.authorization.Group> internalGroupSet = com.google.common.collect.Sets.newHashSet(internalGroupsMap.values());
        for (org.apache.ambari.server.security.authorization.Group group : internalGroupSet) {
            if (group.isLdapGroup()) {
                java.util.Set<org.apache.ambari.server.security.ldap.LdapGroupDto> groupDtos = getLdapGroups(group.getGroupName());
                if (groupDtos.isEmpty()) {
                    org.apache.ambari.server.security.ldap.LdapGroupDto groupDto = new org.apache.ambari.server.security.ldap.LdapGroupDto();
                    groupDto.setGroupName(group.getGroupName());
                    batchInfo.getGroupsToBeRemoved().add(groupDto);
                } else {
                    org.apache.ambari.server.security.ldap.LdapGroupDto groupDto = groupDtos.iterator().next();
                    refreshGroupMembers(batchInfo, groupDto, internalUsersMap, internalGroupsMap, null, true, collectIgnoredUsers);
                }
            }
        }
        return batchInfo;
    }

    public org.apache.ambari.server.security.ldap.LdapBatchDto synchronizeExistingLdapUsers(org.apache.ambari.server.security.ldap.LdapBatchDto batchInfo, boolean collectIgnoredUsers) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.security.ldap.AmbariLdapDataPopulator.LOG.trace("Synchronize Existing LDAP users...");
        final java.util.Map<java.lang.String, org.apache.ambari.server.security.authorization.User> internalUsersMap = getInternalUsers();
        for (org.apache.ambari.server.security.authorization.User user : internalUsersMap.values()) {
            if (user.isLdapUser()) {
                java.util.Set<org.apache.ambari.server.security.ldap.LdapUserDto> userDtos = getLdapUsers(user.getUserName());
                if (userDtos.isEmpty()) {
                    org.apache.ambari.server.security.ldap.LdapUserDto userDto = new org.apache.ambari.server.security.ldap.LdapUserDto();
                    userDto.setUserName(user.getUserName());
                    userDto.setDn(null);
                    batchInfo.getUsersToBeRemoved().add(userDto);
                } else if (collectIgnoredUsers) {
                    org.apache.ambari.server.security.ldap.LdapUserDto userDto = new org.apache.ambari.server.security.ldap.LdapUserDto();
                    userDto.setUserName(user.getUserName());
                    batchInfo.getUsersIgnored().add(userDto);
                }
            }
        }
        return batchInfo;
    }

    protected void refreshGroupMembers(org.apache.ambari.server.security.ldap.LdapBatchDto batchInfo, org.apache.ambari.server.security.ldap.LdapGroupDto group, java.util.Map<java.lang.String, org.apache.ambari.server.security.authorization.User> internalUsers, java.util.Map<java.lang.String, org.apache.ambari.server.security.authorization.Group> internalGroupsMap, java.util.Set<java.lang.String> groupMemberAttributes, boolean recursive, boolean collectIgnoredUsers) throws org.apache.ambari.server.AmbariException {
        java.util.Set<org.apache.ambari.server.security.ldap.LdapUserDto> externalMembers = new java.util.HashSet<>();
        if (groupMemberAttributes == null) {
            groupMemberAttributes = new java.util.HashSet<>();
        }
        for (java.lang.String memberAttributeValue : group.getMemberAttributes()) {
            org.apache.ambari.server.security.ldap.LdapUserDto groupMember = getLdapUserByMemberAttr(memberAttributeValue);
            if (groupMember != null) {
                externalMembers.add(groupMember);
            } else if (recursive && (!groupMemberAttributes.contains(memberAttributeValue))) {
                org.apache.ambari.server.security.ldap.LdapGroupDto subGroup = getLdapGroupByMemberAttr(memberAttributeValue);
                if (subGroup != null) {
                    groupMemberAttributes.add(memberAttributeValue);
                    addLdapGroup(batchInfo, internalGroupsMap, subGroup);
                    refreshGroupMembers(batchInfo, subGroup, internalUsers, internalGroupsMap, groupMemberAttributes, true, collectIgnoredUsers);
                }
            }
        }
        java.lang.String groupName = group.getGroupName();
        final java.util.Map<java.lang.String, org.apache.ambari.server.security.authorization.User> internalMembers = getInternalMembers(groupName);
        for (org.apache.ambari.server.security.ldap.LdapUserDto externalMember : externalMembers) {
            java.lang.String userName = externalMember.getUserName();
            if (internalUsers.containsKey(userName)) {
                final org.apache.ambari.server.security.authorization.User user = internalUsers.get(userName);
                if (user == null) {
                    if (!internalMembers.containsKey(userName)) {
                        batchInfo.getMembershipToAdd().add(new org.apache.ambari.server.security.ldap.LdapUserGroupMemberDto(groupName, externalMember.getUserName()));
                    }
                    continue;
                }
                if (!user.isLdapUser()) {
                    if (org.apache.ambari.server.configuration.LdapUsernameCollisionHandlingBehavior.SKIP == getConfiguration().syncCollisionHandlingBehavior()) {
                        org.apache.ambari.server.security.ldap.AmbariLdapDataPopulator.LOG.info("User '{}' skipped because it is local user", userName);
                        batchInfo.getUsersSkipped().add(externalMember);
                        continue;
                    } else {
                        batchInfo.getUsersToBecomeLdap().add(externalMember);
                    }
                } else if (collectIgnoredUsers) {
                    batchInfo.getUsersIgnored().add(externalMember);
                }
                if (!internalMembers.containsKey(userName)) {
                    batchInfo.getMembershipToAdd().add(new org.apache.ambari.server.security.ldap.LdapUserGroupMemberDto(groupName, externalMember.getUserName()));
                }
                internalMembers.remove(userName);
            } else {
                batchInfo.getUsersToBeCreated().add(externalMember);
                batchInfo.getMembershipToAdd().add(new org.apache.ambari.server.security.ldap.LdapUserGroupMemberDto(groupName, externalMember.getUserName()));
            }
        }
        for (java.util.Map.Entry<java.lang.String, org.apache.ambari.server.security.authorization.User> userToBeUnsynced : internalMembers.entrySet()) {
            final org.apache.ambari.server.security.authorization.User user = userToBeUnsynced.getValue();
            batchInfo.getMembershipToRemove().add(new org.apache.ambari.server.security.ldap.LdapUserGroupMemberDto(groupName, user.getUserName()));
        }
    }

    protected java.util.Set<org.apache.ambari.server.security.ldap.LdapGroupDto> getLdapGroups(java.lang.String groupName) {
        org.apache.ambari.server.security.authorization.LdapServerProperties ldapServerProperties = getLdapProperties();
        org.springframework.ldap.filter.Filter groupObjectFilter = new org.springframework.ldap.filter.EqualsFilter(org.apache.ambari.server.security.ldap.AmbariLdapDataPopulator.OBJECT_CLASS_ATTRIBUTE, ldapServerProperties.getGroupObjectClass());
        org.springframework.ldap.filter.Filter groupNameFilter = new org.springframework.ldap.filter.LikeFilter(ldapServerProperties.getGroupNamingAttr(), groupName);
        return getFilteredLdapGroups(ldapServerProperties.getBaseDN(), groupObjectFilter, groupNameFilter);
    }

    protected java.util.Set<org.apache.ambari.server.security.ldap.LdapUserDto> getLdapUsers(java.lang.String username) {
        org.apache.ambari.server.security.authorization.LdapServerProperties ldapServerProperties = getLdapProperties();
        org.springframework.ldap.filter.Filter userObjectFilter = new org.springframework.ldap.filter.EqualsFilter(org.apache.ambari.server.security.ldap.AmbariLdapDataPopulator.OBJECT_CLASS_ATTRIBUTE, ldapServerProperties.getUserObjectClass());
        org.springframework.ldap.filter.Filter userNameFilter = new org.springframework.ldap.filter.LikeFilter(ldapServerProperties.getUsernameAttribute(), username);
        return getFilteredLdapUsers(ldapServerProperties.getBaseDN(), userObjectFilter, userNameFilter);
    }

    protected org.apache.ambari.server.security.ldap.LdapUserDto getLdapUserByMemberAttr(java.lang.String memberAttributeValue) {
        org.apache.ambari.server.security.authorization.LdapServerProperties ldapServerProperties = getLdapProperties();
        java.util.Set<org.apache.ambari.server.security.ldap.LdapUserDto> filteredLdapUsers;
        memberAttributeValue = getUniqueIdByMemberPattern(memberAttributeValue, ldapServerProperties.getSyncUserMemberReplacePattern());
        org.springframework.ldap.filter.Filter syncMemberFilter = createCustomMemberFilter(memberAttributeValue, ldapServerProperties.getSyncUserMemberFilter());
        if ((memberAttributeValue != null) && (syncMemberFilter != null)) {
            org.apache.ambari.server.security.ldap.AmbariLdapDataPopulator.LOG.trace("Use custom filter '{}' for getting member user with default baseDN ('{}')", syncMemberFilter.encode(), ldapServerProperties.getBaseDN());
            filteredLdapUsers = getFilteredLdapUsers(ldapServerProperties.getBaseDN(), syncMemberFilter);
        } else if ((memberAttributeValue != null) && isMemberAttributeBaseDn(memberAttributeValue)) {
            org.apache.ambari.server.security.ldap.AmbariLdapDataPopulator.LOG.trace("Member can be used as baseDn: {}", memberAttributeValue);
            org.springframework.ldap.filter.Filter filter = new org.springframework.ldap.filter.EqualsFilter(org.apache.ambari.server.security.ldap.AmbariLdapDataPopulator.OBJECT_CLASS_ATTRIBUTE, ldapServerProperties.getUserObjectClass());
            filteredLdapUsers = getFilteredLdapUsers(memberAttributeValue, filter);
        } else {
            org.apache.ambari.server.security.ldap.AmbariLdapDataPopulator.LOG.trace("Member cannot be used as baseDn: {}", memberAttributeValue);
            org.springframework.ldap.filter.Filter filter = new org.springframework.ldap.filter.AndFilter().and(new org.springframework.ldap.filter.EqualsFilter(org.apache.ambari.server.security.ldap.AmbariLdapDataPopulator.OBJECT_CLASS_ATTRIBUTE, ldapServerProperties.getUserObjectClass())).and(new org.springframework.ldap.filter.EqualsFilter(ldapServerProperties.getUsernameAttribute(), memberAttributeValue));
            filteredLdapUsers = getFilteredLdapUsers(ldapServerProperties.getBaseDN(), filter);
        }
        return filteredLdapUsers.isEmpty() ? null : filteredLdapUsers.iterator().next();
    }

    protected org.apache.ambari.server.security.ldap.LdapGroupDto getLdapGroupByMemberAttr(java.lang.String memberAttributeValue) {
        org.apache.ambari.server.security.authorization.LdapServerProperties ldapServerProperties = getLdapProperties();
        java.util.Set<org.apache.ambari.server.security.ldap.LdapGroupDto> filteredLdapGroups;
        memberAttributeValue = getUniqueIdByMemberPattern(memberAttributeValue, ldapServerProperties.getSyncGroupMemberReplacePattern());
        org.springframework.ldap.filter.Filter syncMemberFilter = createCustomMemberFilter(memberAttributeValue, ldapServerProperties.getSyncGroupMemberFilter());
        if ((memberAttributeValue != null) && (syncMemberFilter != null)) {
            org.apache.ambari.server.security.ldap.AmbariLdapDataPopulator.LOG.trace("Use custom filter '{}' for getting member group with default baseDN ('{}')", syncMemberFilter.encode(), ldapServerProperties.getBaseDN());
            filteredLdapGroups = getFilteredLdapGroups(ldapServerProperties.getBaseDN(), syncMemberFilter);
        } else if ((memberAttributeValue != null) && isMemberAttributeBaseDn(memberAttributeValue)) {
            org.apache.ambari.server.security.ldap.AmbariLdapDataPopulator.LOG.trace("Member can be used as baseDn: {}", memberAttributeValue);
            org.springframework.ldap.filter.Filter filter = new org.springframework.ldap.filter.EqualsFilter(org.apache.ambari.server.security.ldap.AmbariLdapDataPopulator.OBJECT_CLASS_ATTRIBUTE, ldapServerProperties.getGroupObjectClass());
            filteredLdapGroups = getFilteredLdapGroups(memberAttributeValue, filter);
        } else {
            org.apache.ambari.server.security.ldap.AmbariLdapDataPopulator.LOG.trace("Member cannot be used as baseDn: {}", memberAttributeValue);
            filteredLdapGroups = getFilteredLdapGroups(ldapServerProperties.getBaseDN(), new org.springframework.ldap.filter.EqualsFilter(org.apache.ambari.server.security.ldap.AmbariLdapDataPopulator.OBJECT_CLASS_ATTRIBUTE, ldapServerProperties.getGroupObjectClass()), getMemberFilter(memberAttributeValue));
        }
        return filteredLdapGroups.isEmpty() ? null : filteredLdapGroups.iterator().next();
    }

    protected org.springframework.ldap.filter.Filter createCustomMemberFilter(java.lang.String memberAttributeValue, java.lang.String syncMemberFilter) {
        org.springframework.ldap.filter.Filter filter = null;
        if (org.apache.commons.lang.StringUtils.isNotEmpty(syncMemberFilter)) {
            filter = new org.springframework.ldap.filter.HardcodedFilter(syncMemberFilter.replace(org.apache.ambari.server.security.ldap.AmbariLdapDataPopulator.MEMBER_ATTRIBUTE_VALUE_PLACEHOLDER, memberAttributeValue));
        }
        return filter;
    }

    protected java.lang.String getUniqueIdByMemberPattern(java.lang.String memberAttributeValue, java.lang.String pattern) {
        if (org.apache.commons.lang.StringUtils.isNotEmpty(memberAttributeValue) && org.apache.commons.lang.StringUtils.isNotEmpty(pattern)) {
            try {
                java.util.regex.Pattern p = java.util.regex.Pattern.compile(pattern);
                java.util.regex.Matcher m = p.matcher(memberAttributeValue);
                org.apache.ambari.server.security.ldap.AmbariLdapDataPopulator.LOG.debug("Apply replace pattern '{}' on '{}' membership attribbute value.", memberAttributeValue, pattern);
                if (m.matches()) {
                    memberAttributeValue = m.replaceAll(org.apache.ambari.server.security.ldap.AmbariLdapDataPopulator.MEMBER_ATTRIBUTE_REPLACE_STRING);
                    org.apache.ambari.server.security.ldap.AmbariLdapDataPopulator.LOG.debug("Membership attribute value after replace pattern applied: '{}'", memberAttributeValue);
                } else {
                    org.apache.ambari.server.security.ldap.AmbariLdapDataPopulator.LOG.warn("Membership attribute value pattern is not matched ({}) on '{}'", pattern, memberAttributeValue);
                }
            } catch (java.lang.Exception e) {
                org.apache.ambari.server.security.ldap.AmbariLdapDataPopulator.LOG.error("Error during replace memberAttribute '{}' with pattern '{}'", memberAttributeValue, pattern);
            }
        }
        return memberAttributeValue;
    }

    protected void cleanUpLdapUsersWithoutGroup() throws org.apache.ambari.server.AmbariException {
        final java.util.List<org.apache.ambari.server.security.authorization.User> allUsers = users.getAllUsers();
        for (org.apache.ambari.server.security.authorization.User user : allUsers) {
            if (user.isLdapUser() && user.getGroups().isEmpty()) {
                users.removeUser(user);
            }
        }
    }

    protected void addLdapGroup(org.apache.ambari.server.security.ldap.LdapBatchDto batchInfo, java.util.Map<java.lang.String, org.apache.ambari.server.security.authorization.Group> internalGroupsMap, org.apache.ambari.server.security.ldap.LdapGroupDto groupDto) {
        java.lang.String groupName = groupDto.getGroupName();
        if (internalGroupsMap.containsKey(groupName)) {
            final org.apache.ambari.server.security.authorization.Group group = internalGroupsMap.get(groupName);
            if (!group.isLdapGroup()) {
                batchInfo.getGroupsToBecomeLdap().add(groupDto);
                org.apache.ambari.server.security.ldap.AmbariLdapDataPopulator.LOG.trace("Convert group '{}' to LDAP group.", groupName);
            }
            internalGroupsMap.remove(groupName);
            batchInfo.getGroupsProcessedInternal().add(groupDto);
        } else if (!batchInfo.getGroupsProcessedInternal().contains(groupDto)) {
            batchInfo.getGroupsToBeCreated().add(groupDto);
        }
    }

    protected boolean isMemberAttributeBaseDn(java.lang.String memberAttributeValue) {
        org.apache.ambari.server.security.authorization.LdapServerProperties ldapServerProperties = getLdapProperties();
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(java.lang.String.format(org.apache.ambari.server.security.ldap.AmbariLdapDataPopulator.IS_MEMBER_DN_REGEXP, ldapServerProperties.getUsernameAttribute(), ldapServerProperties.getGroupNamingAttr()));
        return pattern.matcher(memberAttributeValue).find();
    }

    protected java.util.Set<org.apache.ambari.server.security.ldap.LdapGroupDto> getExternalLdapGroupInfo() {
        org.apache.ambari.server.security.authorization.LdapServerProperties ldapServerProperties = getLdapProperties();
        org.springframework.ldap.filter.EqualsFilter groupObjectFilter = new org.springframework.ldap.filter.EqualsFilter(org.apache.ambari.server.security.ldap.AmbariLdapDataPopulator.OBJECT_CLASS_ATTRIBUTE, ldapServerProperties.getGroupObjectClass());
        return getFilteredLdapGroups(ldapServerProperties.getBaseDN(), groupObjectFilter);
    }

    private org.springframework.ldap.filter.Filter getMemberFilter(java.lang.String memberAttributeValue) {
        org.apache.ambari.server.security.authorization.LdapServerProperties ldapServerProperties = getLdapProperties();
        java.lang.String dnAttribute = ldapServerProperties.getDnAttribute();
        return new org.springframework.ldap.filter.OrFilter().or(new org.springframework.ldap.filter.EqualsFilter(dnAttribute, memberAttributeValue)).or(new org.springframework.ldap.filter.EqualsFilter(org.apache.ambari.server.security.ldap.AmbariLdapDataPopulator.UID_ATTRIBUTE, memberAttributeValue));
    }

    private java.util.Set<org.apache.ambari.server.security.ldap.LdapGroupDto> getFilteredLdapGroups(java.lang.String baseDn, org.springframework.ldap.filter.Filter... filters) {
        org.springframework.ldap.filter.AndFilter andFilter = new org.springframework.ldap.filter.AndFilter();
        for (org.springframework.ldap.filter.Filter filter : filters) {
            andFilter.and(filter);
        }
        return getFilteredLdapGroups(baseDn, andFilter);
    }

    private java.util.Set<org.apache.ambari.server.security.ldap.LdapGroupDto> getFilteredLdapGroups(java.lang.String baseDn, org.springframework.ldap.filter.Filter filter) {
        final java.util.Set<org.apache.ambari.server.security.ldap.LdapGroupDto> groups = new java.util.HashSet<>();
        final org.springframework.ldap.core.LdapTemplate ldapTemplate = loadLdapTemplate();
        org.apache.ambari.server.security.authorization.LdapServerProperties ldapServerProperties = getLdapProperties();
        org.apache.ambari.server.security.ldap.AmbariLdapDataPopulator.LOG.trace("LDAP Group Query - Base DN: '{}' ; Filter: '{}'", baseDn, filter.encode());
        ldapTemplate.search(baseDn, filter.encode(), new org.apache.ambari.server.security.ldap.AmbariLdapDataPopulator.LdapGroupContextMapper(groups, ldapServerProperties));
        return groups;
    }

    protected java.util.Set<org.apache.ambari.server.security.ldap.LdapUserDto> getExternalLdapUserInfo() {
        org.apache.ambari.server.security.authorization.LdapServerProperties ldapServerProperties = getLdapProperties();
        org.springframework.ldap.filter.EqualsFilter userObjectFilter = new org.springframework.ldap.filter.EqualsFilter(org.apache.ambari.server.security.ldap.AmbariLdapDataPopulator.OBJECT_CLASS_ATTRIBUTE, ldapServerProperties.getUserObjectClass());
        return getFilteredLdapUsers(ldapServerProperties.getBaseDN(), userObjectFilter);
    }

    private java.util.Set<org.apache.ambari.server.security.ldap.LdapUserDto> getFilteredLdapUsers(java.lang.String baseDn, org.springframework.ldap.filter.Filter... filters) {
        org.springframework.ldap.filter.AndFilter andFilter = new org.springframework.ldap.filter.AndFilter();
        for (org.springframework.ldap.filter.Filter filter : filters) {
            andFilter.and(filter);
        }
        return getFilteredLdapUsers(baseDn, andFilter);
    }

    private java.util.Set<org.apache.ambari.server.security.ldap.LdapUserDto> getFilteredLdapUsers(java.lang.String baseDn, org.springframework.ldap.filter.Filter filter) {
        final java.util.Set<org.apache.ambari.server.security.ldap.LdapUserDto> users = new java.util.HashSet<>();
        final org.springframework.ldap.core.LdapTemplate ldapTemplate = loadLdapTemplate();
        org.apache.ambari.server.security.authorization.LdapServerProperties ldapServerProperties = getLdapProperties();
        org.springframework.ldap.control.PagedResultsDirContextProcessor processor = createPagingProcessor();
        javax.naming.directory.SearchControls searchControls = new javax.naming.directory.SearchControls();
        searchControls.setReturningObjFlag(true);
        searchControls.setSearchScope(javax.naming.directory.SearchControls.SUBTREE_SCOPE);
        org.apache.ambari.server.security.ldap.AmbariLdapDataPopulator.LdapUserContextMapper ldapUserContextMapper = new org.apache.ambari.server.security.ldap.AmbariLdapDataPopulator.LdapUserContextMapper(ldapServerProperties);
        java.lang.String encodedFilter = filter.encode();
        do {
            org.apache.ambari.server.security.ldap.AmbariLdapDataPopulator.LOG.trace("LDAP User Query - Base DN: '{}' ; Filter: '{}'", baseDn, encodedFilter);
            java.util.List dtos = (ldapServerProperties.isPaginationEnabled()) ? ldapTemplate.search(org.springframework.ldap.support.LdapUtils.newLdapName(baseDn), encodedFilter, searchControls, ldapUserContextMapper, processor) : ldapTemplate.search(org.springframework.ldap.support.LdapUtils.newLdapName(baseDn), encodedFilter, searchControls, ldapUserContextMapper);
            for (java.lang.Object dto : dtos) {
                if (dto != null) {
                    users.add(((org.apache.ambari.server.security.ldap.LdapUserDto) (dto)));
                }
            }
        } while ((ldapServerProperties.isPaginationEnabled() && (processor.getCookie() != null)) && (processor.getCookie().getCookie() != null) );
        return users;
    }

    protected java.util.Map<java.lang.String, org.apache.ambari.server.security.authorization.Group> getInternalGroups() {
        final java.util.List<org.apache.ambari.server.security.authorization.Group> internalGroups = users.getAllGroups();
        final java.util.Map<java.lang.String, org.apache.ambari.server.security.authorization.Group> internalGroupsMap = new java.util.HashMap<>();
        for (org.apache.ambari.server.security.authorization.Group group : internalGroups) {
            internalGroupsMap.put(group.getGroupName(), group);
        }
        return internalGroupsMap;
    }

    protected java.util.Map<java.lang.String, org.apache.ambari.server.security.authorization.User> getInternalUsers() {
        final java.util.List<org.apache.ambari.server.security.authorization.User> internalUsers = users.getAllUsers();
        final java.util.Map<java.lang.String, org.apache.ambari.server.security.authorization.User> internalUsersMap = new java.util.HashMap<>();
        org.apache.ambari.server.security.ldap.AmbariLdapDataPopulator.LOG.trace("Get all users from Ambari Server.");
        for (org.apache.ambari.server.security.authorization.User user : internalUsers) {
            internalUsersMap.put(user.getUserName(), user);
        }
        return internalUsersMap;
    }

    protected java.util.Map<java.lang.String, org.apache.ambari.server.security.authorization.User> getInternalMembers(java.lang.String groupName) {
        final java.util.Collection<org.apache.ambari.server.security.authorization.User> internalMembers = users.getGroupMembers(groupName);
        if (internalMembers == null) {
            return java.util.Collections.emptyMap();
        }
        final java.util.Map<java.lang.String, org.apache.ambari.server.security.authorization.User> internalMembersMap = new java.util.HashMap<>();
        for (org.apache.ambari.server.security.authorization.User user : internalMembers) {
            internalMembersMap.put(user.getUserName(), user);
        }
        return internalMembersMap;
    }

    protected org.springframework.ldap.core.LdapTemplate loadLdapTemplate() {
        final org.apache.ambari.server.security.authorization.LdapServerProperties properties = getConfiguration().getLdapServerProperties();
        if ((ldapTemplate == null) || (!properties.equals(getLdapProperties()))) {
            org.apache.ambari.server.security.ldap.AmbariLdapDataPopulator.LOG.info("Reloading properties");
            ldapServerProperties = properties;
            final org.springframework.ldap.core.support.LdapContextSource ldapContextSource = createLdapContextSource();
            ldapContextSource.setPooled(true);
            final java.util.List<java.lang.String> ldapUrls = ldapServerProperties.getLdapUrls();
            ldapContextSource.setUrls(ldapUrls.toArray(new java.lang.String[ldapUrls.size()]));
            if (!ldapServerProperties.isAnonymousBind()) {
                ldapContextSource.setUserDn(ldapServerProperties.getManagerDn());
                ldapContextSource.setPassword(ldapServerProperties.getManagerPassword());
            }
            if (ldapServerProperties.isUseSsl() && ldapServerProperties.isDisableEndpointIdentification()) {
                java.lang.System.setProperty(org.apache.ambari.server.security.ldap.AmbariLdapDataPopulator.SYSTEM_PROPERTY_DISABLE_ENDPOINT_IDENTIFICATION, "true");
                org.apache.ambari.server.security.ldap.AmbariLdapDataPopulator.LOG.info("Disabled endpoint identification");
            } else {
                java.lang.System.clearProperty(org.apache.ambari.server.security.ldap.AmbariLdapDataPopulator.SYSTEM_PROPERTY_DISABLE_ENDPOINT_IDENTIFICATION);
                org.apache.ambari.server.security.ldap.AmbariLdapDataPopulator.LOG.info("Removed endpoint identification disabling");
            }
            ldapContextSource.setReferral(ldapServerProperties.getReferralMethod());
            try {
                ldapContextSource.afterPropertiesSet();
            } catch (java.lang.Exception e) {
                org.apache.ambari.server.security.ldap.AmbariLdapDataPopulator.LOG.error("LDAP Context Source not loaded ", e);
                throw new org.springframework.security.core.userdetails.UsernameNotFoundException("LDAP Context Source not loaded", e);
            }
            ldapTemplate = createLdapTemplate(ldapContextSource);
            ldapTemplate.setIgnorePartialResultException(true);
        }
        return ldapTemplate;
    }

    protected org.springframework.ldap.core.support.LdapContextSource createLdapContextSource() {
        return new org.springframework.ldap.core.support.LdapContextSource();
    }

    protected org.springframework.ldap.control.PagedResultsDirContextProcessor createPagingProcessor() {
        return new org.springframework.ldap.control.PagedResultsDirContextProcessor(org.apache.ambari.server.security.ldap.AmbariLdapDataPopulator.USERS_PAGE_SIZE, null);
    }

    protected org.springframework.ldap.core.LdapTemplate createLdapTemplate(org.springframework.ldap.core.support.LdapContextSource ldapContextSource) {
        return new org.springframework.ldap.core.LdapTemplate(ldapContextSource);
    }

    protected static class LdapGroupContextMapper implements org.springframework.ldap.core.ContextMapper {
        private final java.util.Set<org.apache.ambari.server.security.ldap.LdapGroupDto> groups;

        private final org.apache.ambari.server.security.authorization.LdapServerProperties ldapServerProperties;

        public LdapGroupContextMapper(java.util.Set<org.apache.ambari.server.security.ldap.LdapGroupDto> groups, org.apache.ambari.server.security.authorization.LdapServerProperties ldapServerProperties) {
            this.groups = groups;
            this.ldapServerProperties = ldapServerProperties;
        }

        @java.lang.Override
        public java.lang.Object mapFromContext(java.lang.Object ctx) {
            final org.springframework.ldap.core.DirContextAdapter adapter = ((org.springframework.ldap.core.DirContextAdapter) (ctx));
            final java.lang.String groupNameAttribute = adapter.getStringAttribute(ldapServerProperties.getGroupNamingAttr());
            boolean outOfScope = org.apache.ambari.server.security.authorization.AmbariLdapUtils.isLdapObjectOutOfScopeFromBaseDn(adapter, ldapServerProperties.getBaseDN());
            if (outOfScope) {
                org.apache.ambari.server.security.ldap.AmbariLdapDataPopulator.LOG.warn("Group '{}' is out of scope of the base DN. It will be skipped.", groupNameAttribute);
                return null;
            }
            if (groupNameAttribute != null) {
                final org.apache.ambari.server.security.ldap.LdapGroupDto group = new org.apache.ambari.server.security.ldap.LdapGroupDto();
                group.setGroupName(groupNameAttribute.toLowerCase());
                final java.lang.String[] uniqueMembers = adapter.getStringAttributes(ldapServerProperties.getGroupMembershipAttr());
                if (uniqueMembers != null) {
                    for (java.lang.String uniqueMember : uniqueMembers) {
                        group.getMemberAttributes().add(uniqueMember.toLowerCase());
                    }
                }
                groups.add(group);
            }
            return null;
        }
    }

    private org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration getConfiguration() {
        return configurationProvider.get();
    }

    protected static class LdapUserContextMapper implements org.springframework.ldap.core.ContextMapper {
        private final org.apache.ambari.server.security.authorization.LdapServerProperties ldapServerProperties;

        public LdapUserContextMapper(org.apache.ambari.server.security.authorization.LdapServerProperties ldapServerProperties) {
            this.ldapServerProperties = ldapServerProperties;
        }

        @java.lang.Override
        public java.lang.Object mapFromContext(java.lang.Object ctx) {
            final org.springframework.ldap.core.DirContextAdapter adapter = ((org.springframework.ldap.core.DirContextAdapter) (ctx));
            final java.lang.String usernameAttribute = adapter.getStringAttribute(ldapServerProperties.getUsernameAttribute());
            final java.lang.String uidAttribute = adapter.getStringAttribute(org.apache.ambari.server.security.ldap.AmbariLdapDataPopulator.UID_ATTRIBUTE);
            boolean outOfScope = org.apache.ambari.server.security.authorization.AmbariLdapUtils.isLdapObjectOutOfScopeFromBaseDn(adapter, ldapServerProperties.getBaseDN());
            if (outOfScope) {
                org.apache.ambari.server.security.ldap.AmbariLdapDataPopulator.LOG.warn("User '{}' is out of scope of the base DN. It will be skipped.", usernameAttribute);
                return null;
            }
            if ((usernameAttribute != null) || (uidAttribute != null)) {
                final org.apache.ambari.server.security.ldap.LdapUserDto user = new org.apache.ambari.server.security.ldap.LdapUserDto();
                user.setUserName(usernameAttribute != null ? usernameAttribute.toLowerCase() : null);
                user.setUid(uidAttribute != null ? uidAttribute.toLowerCase() : null);
                user.setDn(adapter.getNameInNamespace().toLowerCase());
                return user;
            } else {
                org.apache.ambari.server.security.ldap.AmbariLdapDataPopulator.LOG.warn(((("Ignoring LDAP user " + adapter.getNameInNamespace()) + " as it doesn't have required") + " attributes uid and ") + ldapServerProperties.getUsernameAttribute());
            }
            return null;
        }
    }
}