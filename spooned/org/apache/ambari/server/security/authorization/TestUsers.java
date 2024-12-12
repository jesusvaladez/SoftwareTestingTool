package org.apache.ambari.server.security.authorization;
import org.easymock.EasyMock;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
public class TestUsers {
    private com.google.inject.Injector injector;

    @com.google.inject.Inject
    protected org.apache.ambari.server.security.authorization.Users users;

    @com.google.inject.Inject
    protected org.apache.ambari.server.orm.dao.UserDAO userDAO;

    @com.google.inject.Inject
    protected org.apache.ambari.server.orm.dao.GroupDAO groupDAO;

    @com.google.inject.Inject
    protected org.apache.ambari.server.orm.dao.PermissionDAO permissionDAO;

    @com.google.inject.Inject
    protected org.apache.ambari.server.orm.dao.ResourceDAO resourceDAO;

    @com.google.inject.Inject
    protected org.apache.ambari.server.orm.dao.ResourceTypeDAO resourceTypeDAO;

    @com.google.inject.Inject
    protected org.apache.ambari.server.orm.dao.PrincipalTypeDAO principalTypeDAO;

    @com.google.inject.Inject
    protected org.apache.ambari.server.orm.dao.PrincipalDAO principalDAO;

    @com.google.inject.Inject
    protected org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    @com.google.inject.Inject
    protected org.apache.ambari.server.configuration.Configuration configuration;

    @org.junit.Before
    public void setup() throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.orm.InMemoryDefaultTestModule module = new org.apache.ambari.server.orm.InMemoryDefaultTestModule();
        injector = com.google.inject.Guice.createInjector(module);
        injector.getInstance(org.apache.ambari.server.orm.GuiceJpaInitializer.class);
        injector.injectMembers(this);
        org.apache.ambari.server.orm.entities.ResourceTypeEntity resourceTypeEntity = new org.apache.ambari.server.orm.entities.ResourceTypeEntity();
        resourceTypeEntity.setId(org.apache.ambari.server.security.authorization.ResourceType.AMBARI.getId());
        resourceTypeEntity.setName(org.apache.ambari.server.security.authorization.ResourceType.AMBARI.name());
        resourceTypeDAO.create(resourceTypeEntity);
        org.apache.ambari.server.orm.entities.ResourceEntity resourceEntity = new org.apache.ambari.server.orm.entities.ResourceEntity();
        resourceEntity.setId(org.apache.ambari.server.orm.entities.ResourceEntity.AMBARI_RESOURCE_ID);
        resourceEntity.setResourceType(resourceTypeEntity);
        resourceDAO.create(resourceEntity);
        org.apache.ambari.server.orm.entities.PrincipalTypeEntity principalTypeEntity = new org.apache.ambari.server.orm.entities.PrincipalTypeEntity();
        principalTypeEntity.setName("ROLE");
        principalTypeEntity = principalTypeDAO.merge(principalTypeEntity);
        org.apache.ambari.server.orm.entities.PrincipalEntity principalEntity = new org.apache.ambari.server.orm.entities.PrincipalEntity();
        principalEntity.setPrincipalType(principalTypeEntity);
        principalEntity = principalDAO.merge(principalEntity);
        org.apache.ambari.server.orm.entities.PermissionEntity adminPermissionEntity = new org.apache.ambari.server.orm.entities.PermissionEntity();
        adminPermissionEntity.setId(org.apache.ambari.server.orm.entities.PermissionEntity.AMBARI_ADMINISTRATOR_PERMISSION);
        adminPermissionEntity.setPermissionName(org.apache.ambari.server.orm.entities.PermissionEntity.AMBARI_ADMINISTRATOR_PERMISSION_NAME);
        adminPermissionEntity.setPrincipal(principalEntity);
        adminPermissionEntity.setResourceType(resourceTypeEntity);
        permissionDAO.create(adminPermissionEntity);
    }

    @org.junit.After
    public void tearDown() throws org.apache.ambari.server.AmbariException, java.sql.SQLException {
        org.apache.ambari.server.H2DatabaseCleaner.clearDatabaseAndStopPersistenceService(injector);
    }

    @org.junit.Test
    public void testIsUserCanBeRemoved() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.UserEntity userEntity;
        userEntity = users.createUser("admin", "admin", "admin");
        users.grantAdminPrivilege(userEntity);
        userEntity = users.createUser("admin222", "admin222", "admin22");
        users.grantAdminPrivilege(userEntity);
        junit.framework.Assert.assertTrue(users.isUserCanBeRemoved(userDAO.findUserByName("admin")));
        junit.framework.Assert.assertTrue(users.isUserCanBeRemoved(userDAO.findUserByName("admin222")));
        users.removeUser(users.getUser("admin222"));
        junit.framework.Assert.assertFalse(users.isUserCanBeRemoved(userDAO.findUserByName("admin")));
        users.createUser("user", "user", "user");
        junit.framework.Assert.assertFalse(users.isUserCanBeRemoved(userDAO.findUserByName("admin")));
        userEntity = users.createUser("admin333", "admin333", "admin333");
        users.grantAdminPrivilege(userEntity);
        junit.framework.Assert.assertTrue(users.isUserCanBeRemoved(userDAO.findUserByName("admin")));
        junit.framework.Assert.assertTrue(users.isUserCanBeRemoved(userDAO.findUserByName("admin333")));
    }

    @org.junit.Test
    public void testModifyPassword_UserByAdmin() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.UserEntity userEntity;
        userEntity = users.createUser("admin", "admin", "admin");
        users.grantAdminPrivilege(userEntity);
        users.addLocalAuthentication(userEntity, "test_password");
        setAuthenticatedUser(userEntity);
        userEntity = users.createUser("user", "user", "user");
        users.addLocalAuthentication(userEntity, "test_password");
        org.apache.ambari.server.orm.entities.UserEntity foundUserEntity = userDAO.findUserByName("user");
        org.junit.Assert.assertNotNull(foundUserEntity);
        org.apache.ambari.server.orm.entities.UserAuthenticationEntity foundLocalAuthenticationEntity;
        foundLocalAuthenticationEntity = getAuthenticationEntity(foundUserEntity, org.apache.ambari.server.security.authorization.UserAuthenticationType.LOCAL);
        org.junit.Assert.assertNotNull(foundLocalAuthenticationEntity);
        org.junit.Assert.assertNotSame("test_password", foundLocalAuthenticationEntity.getAuthenticationKey());
        org.junit.Assert.assertTrue(passwordEncoder.matches("test_password", foundLocalAuthenticationEntity.getAuthenticationKey()));
        foundUserEntity = userDAO.findUserByName("admin");
        org.junit.Assert.assertNotNull(foundUserEntity);
        users.modifyAuthentication(foundLocalAuthenticationEntity, "test_password", "new_test_password", false);
        foundUserEntity = userDAO.findUserByName("user");
        org.junit.Assert.assertNotNull(foundUserEntity);
        foundLocalAuthenticationEntity = getAuthenticationEntity(foundUserEntity, org.apache.ambari.server.security.authorization.UserAuthenticationType.LOCAL);
        org.junit.Assert.assertNotNull(foundLocalAuthenticationEntity);
        org.junit.Assert.assertTrue(passwordEncoder.matches("new_test_password", foundLocalAuthenticationEntity.getAuthenticationKey()));
    }

    @org.junit.Test
    public void testModifyPassword_EmptyPassword() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.UserEntity userEntity;
        userEntity = users.createUser("user", "user", "user");
        users.addLocalAuthentication(userEntity, "test_password");
        org.apache.ambari.server.orm.entities.UserEntity foundUserEntity = userDAO.findUserByName("user");
        org.junit.Assert.assertNotNull(foundUserEntity);
        org.apache.ambari.server.orm.entities.UserAuthenticationEntity foundLocalAuthenticationEntity;
        foundLocalAuthenticationEntity = getAuthenticationEntity(foundUserEntity, org.apache.ambari.server.security.authorization.UserAuthenticationType.LOCAL);
        org.junit.Assert.assertNotNull(foundLocalAuthenticationEntity);
        org.junit.Assert.assertNotSame("test_password", foundLocalAuthenticationEntity.getAuthenticationKey());
        org.junit.Assert.assertTrue(passwordEncoder.matches("test_password", foundLocalAuthenticationEntity.getAuthenticationKey()));
        try {
            users.modifyAuthentication(foundLocalAuthenticationEntity, "test_password", null, true);
            org.junit.Assert.fail("Null password should not be allowed");
        } catch (java.lang.IllegalArgumentException e) {
            org.junit.Assert.assertEquals("The password does not meet the password policy requirements", e.getLocalizedMessage());
        }
        try {
            users.modifyAuthentication(foundLocalAuthenticationEntity, "test_password", "", false);
            org.junit.Assert.fail("Empty password should not be allowed");
        } catch (java.lang.IllegalArgumentException e) {
            org.junit.Assert.assertEquals("The password does not meet the password policy requirements", e.getLocalizedMessage());
        }
        configuration.setProperty(org.apache.ambari.server.configuration.Configuration.PASSWORD_POLICY_REGEXP, "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$");
        configuration.setProperty(org.apache.ambari.server.configuration.Configuration.PASSWORD_POLICY_DESCRIPTION, "test description");
        try {
            users.modifyAuthentication(foundLocalAuthenticationEntity, "test_password", "abc123", false);
            org.junit.Assert.fail("Should not pass validation");
        } catch (java.lang.IllegalArgumentException e) {
            org.junit.Assert.assertEquals("The password does not meet the Ambari user password policy : test description", e.getLocalizedMessage());
        }
        users.modifyAuthentication(foundLocalAuthenticationEntity, "test_password", "abcd1234", false);
    }

    @org.junit.Test
    public void testRevokeAdminPrivilege() throws java.lang.Exception {
        final org.apache.ambari.server.orm.entities.UserEntity userEntity = users.createUser("old_admin", "old_admin", "old_admin");
        users.grantAdminPrivilege(userEntity);
        final org.apache.ambari.server.security.authorization.User admin = users.getUser("old_admin");
        users.revokeAdminPrivilege(admin.getUserId());
        junit.framework.Assert.assertFalse(users.getUser("old_admin").isAdmin());
    }

    @org.junit.Test
    public void testGrantAdminPrivilege() throws java.lang.Exception {
        users.createUser("user", "user", "user");
        final org.apache.ambari.server.security.authorization.User user = users.getUser("user");
        users.grantAdminPrivilege(user.getUserId());
        junit.framework.Assert.assertTrue(users.getUser("user").isAdmin());
    }

    @org.junit.Test
    public void testCreateGetRemoveUser() throws java.lang.Exception {
        users.createUser("user1", "user1", null);
        users.createUser("user", "user", null, false);
        org.apache.ambari.server.orm.entities.UserEntity userEntity = users.createUser("user_ldap", "user_ldap", null);
        users.grantAdminPrivilege(userEntity);
        users.addLdapAuthentication(userEntity, "some dn");
        org.apache.ambari.server.security.authorization.User createdUser = users.getUser("user");
        org.apache.ambari.server.security.authorization.User createdUser1 = users.getUser("user1");
        org.apache.ambari.server.security.authorization.User createdLdapUser = users.getUser("user_ldap");
        junit.framework.Assert.assertEquals("user1", createdUser1.getUserName());
        junit.framework.Assert.assertEquals(true, createdUser1.isActive());
        junit.framework.Assert.assertEquals(false, createdUser1.isLdapUser());
        junit.framework.Assert.assertEquals(false, createdUser1.isAdmin());
        junit.framework.Assert.assertEquals("user", createdUser.getUserName());
        junit.framework.Assert.assertEquals(false, createdUser.isActive());
        junit.framework.Assert.assertEquals(false, createdUser.isLdapUser());
        junit.framework.Assert.assertEquals(false, createdUser.isAdmin());
        junit.framework.Assert.assertEquals("user_ldap", createdLdapUser.getUserName());
        junit.framework.Assert.assertEquals(true, createdLdapUser.isActive());
        junit.framework.Assert.assertEquals(true, createdLdapUser.isLdapUser());
        junit.framework.Assert.assertEquals(true, createdLdapUser.isAdmin());
        org.junit.Assert.assertEquals("user", users.getUser("user").getUserName());
        org.junit.Assert.assertEquals("user_ldap", users.getUser("user_ldap").getUserName());
        junit.framework.Assert.assertNull(users.getUser("non_existing"));
        try {
            users.createUser("user1", "user1", null);
            org.junit.Assert.fail("It shouldn't be possible to create duplicate user");
        } catch (org.apache.ambari.server.AmbariException e) {
        }
        try {
            users.createUser("USER1", "user1", null);
            org.junit.Assert.fail("It shouldn't be possible to create duplicate user");
        } catch (org.apache.ambari.server.AmbariException e) {
        }
        java.util.List<org.apache.ambari.server.security.authorization.User> userList = users.getAllUsers();
        junit.framework.Assert.assertEquals(3, userList.size());
        org.junit.Assert.assertEquals("user", users.getUser("USER").getUserName());
        org.junit.Assert.assertEquals("user_ldap", users.getUser("USER_LDAP").getUserName());
        junit.framework.Assert.assertNull(users.getUser("non_existing"));
        org.apache.ambari.server.security.authorization.User userById = users.getUser(createdUser.getUserId());
        org.junit.Assert.assertNotNull(userById);
        org.junit.Assert.assertEquals(createdUser.getUserId(), userById.getUserId());
        org.apache.ambari.server.security.authorization.User userByInvalidId = users.getUser(-1);
        org.junit.Assert.assertNull(userByInvalidId);
        junit.framework.Assert.assertNotNull(users.getUser("user"));
        junit.framework.Assert.assertEquals(3, users.getAllUsers().size());
        users.removeUser(users.getUser("user1"));
        junit.framework.Assert.assertNull(users.getUser("user1"));
        junit.framework.Assert.assertEquals(2, users.getAllUsers().size());
    }

    @org.junit.Test
    public void testSetUserActive() throws java.lang.Exception {
        users.createUser("user", "user", null);
        users.setUserActive("user", false);
        junit.framework.Assert.assertEquals(false, users.getUser("user").isActive());
        users.setUserActive("user", true);
        junit.framework.Assert.assertEquals(true, users.getUser("user").isActive());
        try {
            users.setUserActive("fake user", true);
            org.junit.Assert.fail("It shouldn't be possible to call setUserActive() on non-existing user");
        } catch (java.lang.Exception ex) {
        }
    }

    @org.junit.Test
    public void testSetUserLdap() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.UserEntity userEntity;
        users.createUser("user", "user", null);
        users.addLdapAuthentication(users.getUserEntity("user"), "some dn");
        userEntity = users.createUser("user_ldap", "user_ldap", null);
        users.addLdapAuthentication(userEntity, "some dn");
        junit.framework.Assert.assertEquals(true, users.getUser("user").isLdapUser());
        junit.framework.Assert.assertEquals(true, users.getUser("user_ldap").isLdapUser());
        try {
            users.addLdapAuthentication(users.getUserEntity("fake user"), "some other dn");
            org.junit.Assert.fail("It shouldn't be possible to call setUserLdap() on non-existing user");
        } catch (org.apache.ambari.server.AmbariException ex) {
        }
    }

    @org.junit.Test
    public void testSetGroupLdap() throws java.lang.Exception {
        users.createGroup("group", org.apache.ambari.server.security.authorization.GroupType.LOCAL);
        users.setGroupLdap("group");
        junit.framework.Assert.assertNotNull(users.getGroup("group"));
        junit.framework.Assert.assertTrue(users.getGroup("group").isLdapGroup());
        try {
            users.setGroupLdap("fake group");
            org.junit.Assert.fail("It shouldn't be possible to call setGroupLdap() on non-existing group");
        } catch (org.apache.ambari.server.AmbariException ex) {
        }
    }

    @org.junit.Test
    public void testCreateGetRemoveGroup() throws java.lang.Exception {
        final java.lang.String groupName = "engineering1";
        final java.lang.String groupName2 = "engineering2";
        users.createGroup(groupName, org.apache.ambari.server.security.authorization.GroupType.LOCAL);
        users.createGroup(groupName2, org.apache.ambari.server.security.authorization.GroupType.LOCAL);
        final org.apache.ambari.server.security.authorization.Group group = users.getGroup(groupName);
        org.junit.Assert.assertNotNull(group);
        org.junit.Assert.assertEquals(false, group.isLdapGroup());
        org.junit.Assert.assertEquals(groupName, group.getGroupName());
        org.junit.Assert.assertNotNull(groupDAO.findGroupByName(groupName));
        final java.util.List<org.apache.ambari.server.security.authorization.Group> groupList = users.getAllGroups();
        org.junit.Assert.assertEquals(2, groupList.size());
        org.junit.Assert.assertEquals(2, groupDAO.findAll().size());
        users.removeGroup(group);
        org.junit.Assert.assertNull(users.getGroup(group.getGroupName()));
        org.junit.Assert.assertEquals(1, users.getAllGroups().size());
    }

    @org.junit.Test
    public void testMembers() throws java.lang.Exception {
        final java.lang.String groupName = "engineering";
        final java.lang.String groupName2 = "engineering2";
        users.createGroup(groupName, org.apache.ambari.server.security.authorization.GroupType.LOCAL);
        users.createGroup(groupName2, org.apache.ambari.server.security.authorization.GroupType.LOCAL);
        users.createUser("user1", "user1", null);
        users.createUser("user2", "user2", null);
        users.createUser("user3", "user3", null);
        users.addMemberToGroup(groupName, "user1");
        users.addMemberToGroup(groupName, "user2");
        org.junit.Assert.assertEquals(2, users.getAllMembers(groupName).size());
        org.junit.Assert.assertEquals(0, users.getAllMembers(groupName2).size());
        try {
            users.getAllMembers("non existing");
            org.junit.Assert.fail("It shouldn't be possible to call getAllMembers() on non-existing group");
        } catch (java.lang.Exception ex) {
        }
        org.junit.Assert.assertEquals(users.getGroupMembers("unexisting"), null);
        users.removeMemberFromGroup(groupName, "user1");
        org.junit.Assert.assertEquals(1, groupDAO.findGroupByName(groupName).getMemberEntities().size());
        org.junit.Assert.assertEquals("user2", groupDAO.findGroupByName(groupName).getMemberEntities().iterator().next().getUser().getUserName());
    }

    @org.junit.Test
    public void testModifyPassword_UserByHimselfPasswordOk() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.UserEntity userEntity = users.createUser("user", "user", null);
        users.addLocalAuthentication(userEntity, "test_password");
        userEntity = userDAO.findUserByName("user");
        org.apache.ambari.server.orm.entities.UserAuthenticationEntity localAuthenticationEntity = getAuthenticationEntity(userEntity, org.apache.ambari.server.security.authorization.UserAuthenticationType.LOCAL);
        org.junit.Assert.assertNotNull(localAuthenticationEntity);
        org.junit.Assert.assertNotSame("test_password", localAuthenticationEntity.getAuthenticationKey());
        org.junit.Assert.assertTrue(passwordEncoder.matches("test_password", localAuthenticationEntity.getAuthenticationKey()));
        users.modifyAuthentication(localAuthenticationEntity, "test_password", "new_test_password", true);
        userEntity = userDAO.findUserByName("user");
        localAuthenticationEntity = getAuthenticationEntity(userEntity, org.apache.ambari.server.security.authorization.UserAuthenticationType.LOCAL);
        org.junit.Assert.assertNotNull(localAuthenticationEntity);
        org.junit.Assert.assertTrue(passwordEncoder.matches("new_test_password", localAuthenticationEntity.getAuthenticationKey()));
    }

    @org.junit.Test
    public void testModifyPassword_UserByHimselfPasswordNotOk() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.UserEntity userEntity = users.createUser("user", "user", null);
        users.addLocalAuthentication(userEntity, "test_password");
        userEntity = userDAO.findUserByName("user");
        org.apache.ambari.server.orm.entities.UserAuthenticationEntity foundLocalAuthenticationEntity;
        foundLocalAuthenticationEntity = getAuthenticationEntity(userEntity, org.apache.ambari.server.security.authorization.UserAuthenticationType.LOCAL);
        org.junit.Assert.assertNotNull(foundLocalAuthenticationEntity);
        org.junit.Assert.assertNotSame("test_password", foundLocalAuthenticationEntity.getAuthenticationKey());
        org.junit.Assert.assertTrue(passwordEncoder.matches("test_password", foundLocalAuthenticationEntity.getAuthenticationKey()));
        try {
            users.modifyAuthentication(foundLocalAuthenticationEntity, "incorrect_test_password", "new_test_password", true);
            org.junit.Assert.fail("Exception should be thrown here as password is incorrect");
        } catch (org.apache.ambari.server.AmbariException ex) {
        }
    }

    @org.junit.Test
    public void testAddAndRemoveAuthentication() throws java.lang.Exception {
        users.createUser("user", "user", "user");
        org.apache.ambari.server.orm.entities.UserEntity userEntity = userDAO.findUserByName("user");
        org.junit.Assert.assertNotNull(userEntity);
        org.junit.Assert.assertEquals("user", userEntity.getUserName());
        org.apache.ambari.server.orm.entities.UserEntity userEntity2 = userDAO.findUserByName("user");
        org.junit.Assert.assertNotNull(userEntity2);
        org.junit.Assert.assertEquals("user", userEntity2.getUserName());
        org.junit.Assert.assertEquals(0, users.getUserAuthenticationEntities("user", null).size());
        users.addAuthentication(userEntity, org.apache.ambari.server.security.authorization.UserAuthenticationType.LOCAL, "local_key");
        org.junit.Assert.assertEquals(1, users.getUserAuthenticationEntities("user", null).size());
        org.junit.Assert.assertEquals(1, users.getUserAuthenticationEntities("user", org.apache.ambari.server.security.authorization.UserAuthenticationType.LOCAL).size());
        org.junit.Assert.assertTrue(passwordEncoder.matches("local_key", users.getUserAuthenticationEntities("user", org.apache.ambari.server.security.authorization.UserAuthenticationType.LOCAL).iterator().next().getAuthenticationKey()));
        org.junit.Assert.assertEquals(0, users.getUserAuthenticationEntities("user", org.apache.ambari.server.security.authorization.UserAuthenticationType.KERBEROS).size());
        users.addAuthentication(userEntity, org.apache.ambari.server.security.authorization.UserAuthenticationType.PAM, "pam_key");
        org.junit.Assert.assertEquals(2, users.getUserAuthenticationEntities("user", null).size());
        org.junit.Assert.assertEquals(1, users.getUserAuthenticationEntities("user", org.apache.ambari.server.security.authorization.UserAuthenticationType.PAM).size());
        org.junit.Assert.assertEquals("pam_key", users.getUserAuthenticationEntities("user", org.apache.ambari.server.security.authorization.UserAuthenticationType.PAM).iterator().next().getAuthenticationKey());
        org.junit.Assert.assertEquals(0, users.getUserAuthenticationEntities("user", org.apache.ambari.server.security.authorization.UserAuthenticationType.KERBEROS).size());
        users.addAuthentication(userEntity, org.apache.ambari.server.security.authorization.UserAuthenticationType.JWT, "jwt_key");
        org.junit.Assert.assertEquals(3, users.getUserAuthenticationEntities("user", null).size());
        org.junit.Assert.assertEquals(1, users.getUserAuthenticationEntities("user", org.apache.ambari.server.security.authorization.UserAuthenticationType.JWT).size());
        org.junit.Assert.assertEquals("jwt_key", users.getUserAuthenticationEntities("user", org.apache.ambari.server.security.authorization.UserAuthenticationType.JWT).iterator().next().getAuthenticationKey());
        org.junit.Assert.assertEquals(0, users.getUserAuthenticationEntities("user", org.apache.ambari.server.security.authorization.UserAuthenticationType.KERBEROS).size());
        users.addAuthentication(userEntity, org.apache.ambari.server.security.authorization.UserAuthenticationType.LDAP, "ldap_key");
        org.junit.Assert.assertEquals(4, users.getUserAuthenticationEntities("user", null).size());
        org.junit.Assert.assertEquals(1, users.getUserAuthenticationEntities("user", org.apache.ambari.server.security.authorization.UserAuthenticationType.LDAP).size());
        org.junit.Assert.assertEquals("ldap_key", users.getUserAuthenticationEntities("user", org.apache.ambari.server.security.authorization.UserAuthenticationType.LDAP).iterator().next().getAuthenticationKey());
        org.junit.Assert.assertEquals(0, users.getUserAuthenticationEntities("user", org.apache.ambari.server.security.authorization.UserAuthenticationType.KERBEROS).size());
        users.addAuthentication(userEntity, org.apache.ambari.server.security.authorization.UserAuthenticationType.KERBEROS, "kerberos_key");
        org.junit.Assert.assertEquals(5, users.getUserAuthenticationEntities("user", null).size());
        org.junit.Assert.assertEquals("kerberos_key", users.getUserAuthenticationEntities("user", org.apache.ambari.server.security.authorization.UserAuthenticationType.KERBEROS).iterator().next().getAuthenticationKey());
        org.junit.Assert.assertEquals(1, users.getUserAuthenticationEntities("user", org.apache.ambari.server.security.authorization.UserAuthenticationType.KERBEROS).size());
        org.junit.Assert.assertEquals(5, userEntity.getAuthenticationEntities().size());
        org.junit.Assert.assertEquals(0, userEntity2.getAuthenticationEntities().size());
        userEntity2 = userDAO.findUserByName("user");
        org.junit.Assert.assertEquals(5, userEntity2.getAuthenticationEntities().size());
        java.lang.Long kerberosAuthenticationId = users.getUserAuthenticationEntities("user", org.apache.ambari.server.security.authorization.UserAuthenticationType.KERBEROS).iterator().next().getUserAuthenticationId();
        java.lang.Long pamAuthenticationId = users.getUserAuthenticationEntities("user", org.apache.ambari.server.security.authorization.UserAuthenticationType.PAM).iterator().next().getUserAuthenticationId();
        users.removeAuthentication("user", kerberosAuthenticationId);
        org.junit.Assert.assertEquals(4, users.getUserAuthenticationEntities("user", null).size());
        users.removeAuthentication(userEntity, kerberosAuthenticationId);
        org.junit.Assert.assertEquals(4, users.getUserAuthenticationEntities("user", null).size());
        users.removeAuthentication(userEntity, pamAuthenticationId);
        org.junit.Assert.assertEquals(3, users.getUserAuthenticationEntities("user", null).size());
        org.junit.Assert.assertEquals(3, userEntity2.getAuthenticationEntities().size());
    }

    @org.junit.Test
    public void testProcessLdapSync() {
        org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration ambariLdapConfiguration = org.easymock.EasyMock.createMock(org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration.class);
        org.easymock.EasyMock.expect(ambariLdapConfiguration.groupMappingRules()).andReturn("admins").anyTimes();
        org.apache.ambari.server.ldap.service.AmbariLdapConfigurationProvider ambariLdapConfigurationProvider = injector.getInstance(org.apache.ambari.server.ldap.service.AmbariLdapConfigurationProvider.class);
        org.easymock.EasyMock.expect(ambariLdapConfigurationProvider.get()).andReturn(ambariLdapConfiguration).anyTimes();
        org.easymock.EasyMock.replay(ambariLdapConfigurationProvider, ambariLdapConfiguration);
        org.apache.ambari.server.security.ldap.LdapBatchDto batchInfo = new org.apache.ambari.server.security.ldap.LdapBatchDto();
        org.apache.ambari.server.security.ldap.LdapUserDto userToBeCreated;
        org.apache.ambari.server.security.ldap.LdapGroupDto groupToBeCreated;
        userToBeCreated = new org.apache.ambari.server.security.ldap.LdapUserDto();
        userToBeCreated.setDn("dn=user1");
        userToBeCreated.setUid("user1");
        userToBeCreated.setUserName("User1");
        batchInfo.getUsersToBeCreated().add(userToBeCreated);
        userToBeCreated = new org.apache.ambari.server.security.ldap.LdapUserDto();
        userToBeCreated.setDn("dn=user2");
        userToBeCreated.setUid("user2");
        userToBeCreated.setUserName("User2");
        batchInfo.getUsersToBeCreated().add(userToBeCreated);
        groupToBeCreated = new org.apache.ambari.server.security.ldap.LdapGroupDto();
        groupToBeCreated.setGroupName("admins");
        groupToBeCreated.setMemberAttributes(java.util.Collections.singleton("dn=User1"));
        batchInfo.getGroupsToBeCreated().add(groupToBeCreated);
        groupToBeCreated = new org.apache.ambari.server.security.ldap.LdapGroupDto();
        groupToBeCreated.setGroupName("non-admins");
        groupToBeCreated.setMemberAttributes(java.util.Collections.singleton("dn=User2"));
        batchInfo.getGroupsToBeCreated().add(groupToBeCreated);
        batchInfo.getMembershipToAdd().add(new org.apache.ambari.server.security.ldap.LdapUserGroupMemberDto("admins", "user1"));
        batchInfo.getMembershipToAdd().add(new org.apache.ambari.server.security.ldap.LdapUserGroupMemberDto("non-admins", "user2"));
        users.processLdapSync(batchInfo);
        org.junit.Assert.assertNotNull(users.getUser("user1"));
        org.junit.Assert.assertNotNull(users.getUser("user2"));
        java.util.Collection<org.apache.ambari.server.security.authorization.AmbariGrantedAuthority> authorities;
        authorities = users.getUserAuthorities("user1");
        org.junit.Assert.assertNotNull(authorities);
        org.junit.Assert.assertEquals(1, authorities.size());
        org.junit.Assert.assertEquals("AMBARI.ADMINISTRATOR", authorities.iterator().next().getPrivilegeEntity().getPermission().getPermissionName());
        authorities = users.getUserAuthorities("user2");
        org.junit.Assert.assertNotNull(authorities);
        org.junit.Assert.assertEquals(0, authorities.size());
    }

    private org.apache.ambari.server.orm.entities.UserAuthenticationEntity getAuthenticationEntity(org.apache.ambari.server.orm.entities.UserEntity userEntity, org.apache.ambari.server.security.authorization.UserAuthenticationType type) {
        org.junit.Assert.assertNotNull(userEntity);
        java.util.Collection<org.apache.ambari.server.orm.entities.UserAuthenticationEntity> authenticationEntities = userEntity.getAuthenticationEntities();
        org.junit.Assert.assertNotNull(authenticationEntities);
        for (org.apache.ambari.server.orm.entities.UserAuthenticationEntity authenticationEntity : authenticationEntities) {
            if (authenticationEntity.getAuthenticationType() == type) {
                return authenticationEntity;
            }
        }
        return null;
    }

    private void setAuthenticatedUser(org.apache.ambari.server.orm.entities.UserEntity userEntity) {
        org.apache.ambari.server.security.authentication.AmbariUserDetailsImpl principal = new org.apache.ambari.server.security.authentication.AmbariUserDetailsImpl(new org.apache.ambari.server.security.authorization.User(userEntity), "", java.util.Collections.emptySet());
        org.springframework.security.core.Authentication auth = EasyMock.mock(org.springframework.security.core.Authentication.class);
        EasyMock.expect(auth.getPrincipal()).andReturn(principal).anyTimes();
        org.springframework.security.core.context.SecurityContext securityContext = EasyMock.mock(org.springframework.security.core.context.SecurityContext.class);
        EasyMock.expect(securityContext.getAuthentication()).andReturn(auth).anyTimes();
        EasyMock.replay(auth, securityContext);
        org.springframework.security.core.context.SecurityContextHolder.setContext(securityContext);
    }
}