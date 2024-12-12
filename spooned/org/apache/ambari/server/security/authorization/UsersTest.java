package org.apache.ambari.server.security.authorization;
import javax.annotation.Nullable;
import javax.persistence.EntityManager;
import org.easymock.Capture;
import org.easymock.EasyMock;
import org.easymock.EasyMockSupport;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.easymock.EasyMock.anyInt;
import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.anyString;
import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.newCapture;
public class UsersTest extends org.easymock.EasyMockSupport {
    private static final java.lang.String SERVICEOP_USER_NAME = "serviceopuser";

    private com.google.inject.Injector injector;

    @org.junit.Test
    public void testGetUserAuthorities() throws java.lang.Exception {
        createInjector();
        org.apache.ambari.server.orm.entities.PrincipalEntity userPrincipalEntity = createMock(org.apache.ambari.server.orm.entities.PrincipalEntity.class);
        org.apache.ambari.server.orm.entities.UserEntity userEntity = createMock(org.apache.ambari.server.orm.entities.UserEntity.class);
        EasyMock.expect(userEntity.getPrincipal()).andReturn(userPrincipalEntity).times(1);
        org.apache.ambari.server.orm.dao.UserDAO userDAO = injector.getInstance(org.apache.ambari.server.orm.dao.UserDAO.class);
        EasyMock.expect(userDAO.findUserByName("user1")).andReturn(userEntity).times(1);
        org.apache.ambari.server.orm.entities.PrincipalEntity groupPrincipalEntity = createMock(org.apache.ambari.server.orm.entities.PrincipalEntity.class);
        org.apache.ambari.server.orm.entities.GroupEntity groupEntity = createMock(org.apache.ambari.server.orm.entities.GroupEntity.class);
        EasyMock.expect(groupEntity.getPrincipal()).andReturn(groupPrincipalEntity).times(1);
        org.apache.ambari.server.orm.entities.MemberEntity memberEntity = createMock(org.apache.ambari.server.orm.entities.MemberEntity.class);
        EasyMock.expect(memberEntity.getGroup()).andReturn(groupEntity).times(1);
        org.apache.ambari.server.orm.dao.MemberDAO memberDAO = injector.getInstance(org.apache.ambari.server.orm.dao.MemberDAO.class);
        EasyMock.expect(memberDAO.findAllMembersByUser(userEntity)).andReturn(java.util.Collections.singletonList(memberEntity)).times(1);
        org.apache.ambari.server.orm.entities.PrincipalEntity clusterUserPrivilegePermissionPrincipalEntity = createMock(org.apache.ambari.server.orm.entities.PrincipalEntity.class);
        org.apache.ambari.server.orm.entities.PermissionEntity clusterUserPrivilegePermissionEntity = createMock(org.apache.ambari.server.orm.entities.PermissionEntity.class);
        EasyMock.expect(clusterUserPrivilegePermissionEntity.getPrincipal()).andReturn(clusterUserPrivilegePermissionPrincipalEntity).times(1);
        org.apache.ambari.server.orm.entities.PrivilegeEntity clusterUserPrivilegeEntity = createMock(org.apache.ambari.server.orm.entities.PrivilegeEntity.class);
        EasyMock.expect(clusterUserPrivilegeEntity.getPermission()).andReturn(clusterUserPrivilegePermissionEntity).times(1);
        org.apache.ambari.server.orm.entities.PrincipalEntity clusterOperatorPrivilegePermissionPrincipalEntity = createMock(org.apache.ambari.server.orm.entities.PrincipalEntity.class);
        org.apache.ambari.server.orm.entities.PermissionEntity clusterOperatorPrivilegePermissionEntity = createMock(org.apache.ambari.server.orm.entities.PermissionEntity.class);
        EasyMock.expect(clusterOperatorPrivilegePermissionEntity.getPrincipal()).andReturn(clusterOperatorPrivilegePermissionPrincipalEntity).times(1);
        org.apache.ambari.server.orm.entities.PrivilegeEntity clusterOperatorPrivilegeEntity = createMock(org.apache.ambari.server.orm.entities.PrivilegeEntity.class);
        EasyMock.expect(clusterOperatorPrivilegeEntity.getPermission()).andReturn(clusterOperatorPrivilegePermissionEntity).times(1);
        java.util.List<org.apache.ambari.server.orm.entities.PrivilegeEntity> privilegeEntities = new java.util.ArrayList<>();
        privilegeEntities.add(clusterUserPrivilegeEntity);
        privilegeEntities.add(clusterOperatorPrivilegeEntity);
        org.apache.ambari.server.orm.entities.PrivilegeEntity clusterUserViewUserPrivilegeEntity = createMock(org.apache.ambari.server.orm.entities.PrivilegeEntity.class);
        java.util.List<org.apache.ambari.server.orm.entities.PrivilegeEntity> rolePrivilegeEntities = new java.util.ArrayList<>();
        rolePrivilegeEntities.add(clusterUserViewUserPrivilegeEntity);
        org.easymock.Capture<? extends java.util.List<org.apache.ambari.server.orm.entities.PrincipalEntity>> principalEntitiesCapture = EasyMock.newCapture();
        org.easymock.Capture<? extends java.util.List<org.apache.ambari.server.orm.entities.PrincipalEntity>> rolePrincipalEntitiesCapture = EasyMock.newCapture();
        org.apache.ambari.server.orm.dao.PrivilegeDAO privilegeDAO = injector.getInstance(org.apache.ambari.server.orm.dao.PrivilegeDAO.class);
        EasyMock.expect(privilegeDAO.findAllByPrincipal(EasyMock.capture(principalEntitiesCapture))).andReturn(privilegeEntities).times(1);
        EasyMock.expect(privilegeDAO.findAllByPrincipal(EasyMock.capture(rolePrincipalEntitiesCapture))).andReturn(rolePrivilegeEntities).times(1);
        replayAll();
        org.apache.ambari.server.security.authorization.Users user = injector.getInstance(org.apache.ambari.server.security.authorization.Users.class);
        java.util.Collection<org.apache.ambari.server.security.authorization.AmbariGrantedAuthority> authorities = user.getUserAuthorities("user1");
        verifyAll();
        org.junit.Assert.assertEquals(2, principalEntitiesCapture.getValue().size());
        org.junit.Assert.assertTrue(principalEntitiesCapture.getValue().contains(userPrincipalEntity));
        org.junit.Assert.assertTrue(principalEntitiesCapture.getValue().contains(groupPrincipalEntity));
        org.junit.Assert.assertEquals(2, rolePrincipalEntitiesCapture.getValue().size());
        org.junit.Assert.assertTrue(rolePrincipalEntitiesCapture.getValue().contains(clusterUserPrivilegePermissionPrincipalEntity));
        org.junit.Assert.assertTrue(rolePrincipalEntitiesCapture.getValue().contains(clusterOperatorPrivilegePermissionPrincipalEntity));
        org.junit.Assert.assertEquals(3, authorities.size());
        org.junit.Assert.assertTrue(authorities.contains(new org.apache.ambari.server.security.authorization.AmbariGrantedAuthority(clusterUserPrivilegeEntity)));
        org.junit.Assert.assertTrue(authorities.contains(new org.apache.ambari.server.security.authorization.AmbariGrantedAuthority(clusterOperatorPrivilegeEntity)));
        org.junit.Assert.assertTrue(authorities.contains(new org.apache.ambari.server.security.authorization.AmbariGrantedAuthority(clusterUserViewUserPrivilegeEntity)));
    }

    @org.junit.Test
    public void testCreateUser_NoDuplicates() throws java.lang.Exception {
        initForCreateUser(null);
        org.apache.ambari.server.security.authorization.Users users = injector.getInstance(org.apache.ambari.server.security.authorization.Users.class);
        users.createUser(org.apache.ambari.server.security.authorization.UsersTest.SERVICEOP_USER_NAME, org.apache.ambari.server.security.authorization.UsersTest.SERVICEOP_USER_NAME, org.apache.ambari.server.security.authorization.UsersTest.SERVICEOP_USER_NAME);
    }

    @org.junit.Test(expected = org.apache.ambari.server.AmbariException.class)
    public void testCreateUser_Duplicate() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.UserEntity existing = new org.apache.ambari.server.orm.entities.UserEntity();
        existing.setUserName(org.apache.ambari.server.security.authorization.UserName.fromString(org.apache.ambari.server.security.authorization.UsersTest.SERVICEOP_USER_NAME).toString());
        existing.setUserId(1);
        existing.setMemberEntities(java.util.Collections.emptySet());
        org.apache.ambari.server.orm.entities.PrincipalEntity principal = new org.apache.ambari.server.orm.entities.PrincipalEntity();
        principal.setPrivileges(java.util.Collections.emptySet());
        existing.setPrincipal(principal);
        initForCreateUser(existing);
        org.apache.ambari.server.security.authorization.Users users = injector.getInstance(org.apache.ambari.server.security.authorization.Users.class);
        users.createUser(org.apache.ambari.server.security.authorization.UsersTest.SERVICEOP_USER_NAME, org.apache.ambari.server.security.authorization.UsersTest.SERVICEOP_USER_NAME, org.apache.ambari.server.security.authorization.UsersTest.SERVICEOP_USER_NAME);
    }

    @org.junit.Test
    public void modifyAuthentication_local_bySameUser() throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.orm.entities.UserAuthenticationEntity entity = initForModifyAuthentication();
        org.apache.ambari.server.security.authorization.Users users = injector.getInstance(org.apache.ambari.server.security.authorization.Users.class);
        users.modifyAuthentication(entity, "hello", "world", true);
        org.junit.Assert.assertEquals("world", entity.getAuthenticationKey());
    }

    @org.junit.Test(expected = org.apache.ambari.server.AmbariException.class)
    public void modifyAuthentication_local_bySameUser_wrongPassword() throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.orm.entities.UserAuthenticationEntity entity = initForModifyAuthentication();
        org.apache.ambari.server.security.authorization.Users users = injector.getInstance(org.apache.ambari.server.security.authorization.Users.class);
        users.modifyAuthentication(entity, "12345", "world", true);
    }

    @org.junit.Test
    public void modifyAuthentication_local_byAdminUser() throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.orm.entities.UserAuthenticationEntity entity = initForModifyAuthentication();
        org.apache.ambari.server.security.authorization.Users users = injector.getInstance(org.apache.ambari.server.security.authorization.Users.class);
        users.modifyAuthentication(entity, "admin1234", "world", false);
        org.junit.Assert.assertEquals("world", entity.getAuthenticationKey());
    }

    @org.junit.Test(expected = org.apache.ambari.server.AmbariException.class)
    public void modifyAuthentication_local_byAdminUser_wrongPassword() throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.orm.entities.UserAuthenticationEntity entity = initForModifyAuthentication();
        org.apache.ambari.server.security.authorization.Users users = injector.getInstance(org.apache.ambari.server.security.authorization.Users.class);
        users.modifyAuthentication(entity, "wrong password", "world", false);
    }

    @org.junit.Test(expected = java.lang.IllegalArgumentException.class)
    public void modifyAuthentication_local_byAdminUser_usernameInPassword() throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.orm.entities.UserAuthenticationEntity entity = initForModifyAuthentication();
        org.apache.ambari.server.security.authorization.Users users = injector.getInstance(org.apache.ambari.server.security.authorization.Users.class);
        users.modifyAuthentication(entity, "admin1234", "User123", false);
    }

    @org.junit.Test
    public void modifyAuthentication_local_byAdminUser_usernameNotInPassword() throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.orm.entities.UserAuthenticationEntity entity = initForModifyAuthentication();
        org.apache.ambari.server.security.authorization.Users users = injector.getInstance(org.apache.ambari.server.security.authorization.Users.class);
        users.modifyAuthentication(entity, "admin1234", "User213", false);
    }

    @org.junit.Test(expected = java.lang.IllegalArgumentException.class)
    public void modifyAuthentication_local_byAdminUser_newPasswordSameAsPreviousPassword() throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.orm.entities.UserAuthenticationEntity entity = initForModifyAuthentication();
        org.apache.ambari.server.security.authorization.Users users = injector.getInstance(org.apache.ambari.server.security.authorization.Users.class);
        users.modifyAuthentication(entity, "admin1234", "hello1", false);
        users.modifyAuthentication(entity, "admin1234", "hello", false);
    }

    private void initForCreateUser(@javax.annotation.Nullable
    org.apache.ambari.server.orm.entities.UserEntity existingUser) {
        org.apache.ambari.server.orm.dao.UserDAO userDao = createStrictMock(org.apache.ambari.server.orm.dao.UserDAO.class);
        EasyMock.expect(userDao.findUserByName(EasyMock.anyString())).andReturn(existingUser);
        userDao.create(EasyMock.anyObject(org.apache.ambari.server.orm.entities.UserEntity.class));
        EasyMock.expectLastCall();
        javax.persistence.EntityManager entityManager = createNiceMock(javax.persistence.EntityManager.class);
        EasyMock.expect(entityManager.find(EasyMock.eq(org.apache.ambari.server.orm.entities.PrincipalEntity.class), org.easymock.EasyMock.anyObject())).andReturn(null);
        replayAll();
        createInjector(userDao, entityManager);
    }

    private org.apache.ambari.server.orm.entities.UserAuthenticationEntity initForModifyAuthentication() {
        org.apache.ambari.server.orm.entities.UserEntity user1 = new org.apache.ambari.server.orm.entities.UserEntity();
        user1.setUserId(-1);
        user1.setUserName("user1");
        org.apache.ambari.server.orm.entities.UserAuthenticationEntity userEntity = new org.apache.ambari.server.orm.entities.UserAuthenticationEntity();
        userEntity.setAuthenticationKey("hello");
        userEntity.setAuthenticationType(org.apache.ambari.server.security.authorization.UserAuthenticationType.LOCAL);
        userEntity.setUser(user1);
        user1.setAuthenticationEntities(com.google.common.collect.ImmutableList.of(userEntity));
        javax.persistence.EntityManager manager = mock(javax.persistence.EntityManager.class);
        EasyMock.expect(manager.merge(userEntity)).andReturn(userEntity).once();
        org.apache.ambari.server.orm.dao.UserDAO dao = createMock(org.apache.ambari.server.orm.dao.UserDAO.class);
        org.apache.ambari.server.orm.entities.UserEntity admin = new org.apache.ambari.server.orm.entities.UserEntity();
        admin.setUserId(-1);
        admin.setUserName("admin");
        org.apache.ambari.server.orm.entities.PrincipalEntity principalEntity = new org.apache.ambari.server.orm.entities.PrincipalEntity();
        admin.setPrincipal(principalEntity);
        org.apache.ambari.server.orm.entities.PrivilegeEntity privilegeEntity = new org.apache.ambari.server.orm.entities.PrivilegeEntity();
        principalEntity.setPrivileges(com.google.common.collect.ImmutableSet.of(privilegeEntity));
        org.apache.ambari.server.orm.entities.PermissionEntity permissionEntity = new org.apache.ambari.server.orm.entities.PermissionEntity();
        privilegeEntity.setPermission(permissionEntity);
        permissionEntity.setPermissionName(org.apache.ambari.server.orm.entities.PermissionEntity.AMBARI_ADMINISTRATOR_PERMISSION_NAME);
        org.apache.ambari.server.orm.entities.UserAuthenticationEntity adminAuthentication = new org.apache.ambari.server.orm.entities.UserAuthenticationEntity();
        admin.setAuthenticationEntities(com.google.common.collect.ImmutableList.of(adminAuthentication));
        adminAuthentication.setAuthenticationKey("admin1234");
        EasyMock.expect(dao.findByPK(EasyMock.anyInt())).andReturn(admin).anyTimes();
        createInjector(dao, manager);
        replayAll();
        return userEntity;
    }

    private void createInjector() {
        createInjector(createMock(org.apache.ambari.server.orm.dao.UserDAO.class), createMock(javax.persistence.EntityManager.class));
    }

    private void createInjector(final org.apache.ambari.server.orm.dao.UserDAO mockUserDao, final javax.persistence.EntityManager mockEntityManager) {
        injector = com.google.inject.Guice.createInjector(new com.google.inject.AbstractModule() {
            @java.lang.Override
            protected void configure() {
                bind(javax.persistence.EntityManager.class).toInstance(mockEntityManager);
                bind(org.apache.ambari.server.orm.DBAccessor.class).toInstance(createMock(org.apache.ambari.server.orm.DBAccessor.class));
                bind(org.apache.ambari.server.state.stack.OsFamily.class).toInstance(createNiceMock(org.apache.ambari.server.state.stack.OsFamily.class));
                bind(org.apache.ambari.server.orm.dao.UserDAO.class).toInstance(mockUserDao);
                bind(org.apache.ambari.server.orm.dao.MemberDAO.class).toInstance(createMock(org.apache.ambari.server.orm.dao.MemberDAO.class));
                bind(org.apache.ambari.server.orm.dao.PrivilegeDAO.class).toInstance(createMock(org.apache.ambari.server.orm.dao.PrivilegeDAO.class));
                bind(org.apache.ambari.server.hooks.HookService.class).toInstance(createMock(org.apache.ambari.server.hooks.HookService.class));
                bind(org.apache.ambari.server.hooks.HookContextFactory.class).toInstance(createMock(org.apache.ambari.server.hooks.HookContextFactory.class));
                bind(org.apache.ambari.server.orm.dao.PrincipalDAO.class).toInstance(createMock(org.apache.ambari.server.orm.dao.PrincipalDAO.class));
                bind(new com.google.inject.TypeLiteral<org.apache.ambari.server.security.encryption.Encryptor<org.apache.ambari.server.configuration.AmbariServerConfiguration>>() {}).annotatedWith(com.google.inject.name.Names.named("AmbariServerConfigurationEncryptor")).toInstance(org.apache.ambari.server.security.encryption.Encryptor.NONE);
                bind(org.apache.ambari.server.ldap.service.AmbariLdapConfigurationProvider.class).toInstance(createMock(org.apache.ambari.server.ldap.service.AmbariLdapConfigurationProvider.class));
                org.springframework.security.crypto.password.PasswordEncoder nopEncoder = createMock(org.springframework.security.crypto.password.PasswordEncoder.class);
                EasyMock.expect(nopEncoder.matches(EasyMock.anyString(), EasyMock.anyString())).andAnswer(() -> java.util.Objects.equals(org.easymock.EasyMock.getCurrentArguments()[0], org.easymock.EasyMock.getCurrentArguments()[1])).anyTimes();
                EasyMock.expect(nopEncoder.encode(EasyMock.anyString())).andAnswer(() -> ((java.lang.String) (org.easymock.EasyMock.getCurrentArguments()[0]))).anyTimes();
                bind(org.springframework.security.crypto.password.PasswordEncoder.class).toInstance(nopEncoder);
                org.apache.ambari.server.configuration.Configuration noConfig = createMock(org.apache.ambari.server.configuration.Configuration.class);
                EasyMock.expect(noConfig.getPasswordPolicyHistoryCount()).andReturn(3).anyTimes();
                EasyMock.expect(noConfig.getPasswordPolicyRegexp()).andReturn(".*").anyTimes();
                bind(org.apache.ambari.server.configuration.Configuration.class).toInstance(noConfig);
            }
        });
    }
}