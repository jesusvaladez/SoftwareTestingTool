package org.apache.ambari.server.security.authorization;
import org.easymock.EasyMockRule;
import org.easymock.EasyMockSupport;
import org.easymock.Mock;
import org.easymock.MockType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.reset;
import static org.easymock.EasyMock.verify;
public class AuthorizationHelperTest extends org.easymock.EasyMockSupport {
    @org.junit.Rule
    public org.easymock.EasyMockRule mocks = new org.easymock.EasyMockRule(this);

    @org.easymock.Mock(type = org.easymock.MockType.NICE)
    private org.springframework.web.context.request.ServletRequestAttributes servletRequestAttributes;

    @org.junit.Before
    public void setup() {
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(null);
    }

    @org.junit.After
    public void cleanup() {
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(null);
    }

    @org.junit.Test
    public void testConvertPrivilegesToAuthorities() throws java.lang.Exception {
        java.util.Collection<org.apache.ambari.server.orm.entities.PrivilegeEntity> privilegeEntities = new java.util.ArrayList<>();
        org.apache.ambari.server.orm.entities.ResourceTypeEntity resourceTypeEntity = new org.apache.ambari.server.orm.entities.ResourceTypeEntity();
        resourceTypeEntity.setId(1);
        resourceTypeEntity.setName("CLUSTER");
        org.apache.ambari.server.orm.entities.ResourceEntity resourceEntity = new org.apache.ambari.server.orm.entities.ResourceEntity();
        resourceEntity.setId(1L);
        resourceEntity.setResourceType(resourceTypeEntity);
        org.apache.ambari.server.orm.entities.PrincipalTypeEntity principalTypeEntity = new org.apache.ambari.server.orm.entities.PrincipalTypeEntity();
        principalTypeEntity.setId(1);
        principalTypeEntity.setName("USER");
        org.apache.ambari.server.orm.entities.PrincipalEntity principalEntity = new org.apache.ambari.server.orm.entities.PrincipalEntity();
        principalEntity.setPrincipalType(principalTypeEntity);
        principalEntity.setId(1L);
        org.apache.ambari.server.orm.entities.PermissionEntity permissionEntity1 = new org.apache.ambari.server.orm.entities.PermissionEntity();
        permissionEntity1.setPermissionName("Permission1");
        permissionEntity1.setResourceType(resourceTypeEntity);
        permissionEntity1.setId(2);
        permissionEntity1.setPermissionName("CLUSTER.USER");
        org.apache.ambari.server.orm.entities.PermissionEntity permissionEntity2 = new org.apache.ambari.server.orm.entities.PermissionEntity();
        permissionEntity2.setPermissionName("Permission1");
        permissionEntity2.setResourceType(resourceTypeEntity);
        permissionEntity2.setId(3);
        permissionEntity2.setPermissionName("CLUSTER.ADMINISTRATOR");
        org.apache.ambari.server.orm.entities.PrivilegeEntity privilegeEntity1 = new org.apache.ambari.server.orm.entities.PrivilegeEntity();
        privilegeEntity1.setId(1);
        privilegeEntity1.setPermission(permissionEntity1);
        privilegeEntity1.setPrincipal(principalEntity);
        privilegeEntity1.setResource(resourceEntity);
        org.apache.ambari.server.orm.entities.PrivilegeEntity privilegeEntity2 = new org.apache.ambari.server.orm.entities.PrivilegeEntity();
        privilegeEntity2.setId(1);
        privilegeEntity2.setPermission(permissionEntity2);
        privilegeEntity2.setPrincipal(principalEntity);
        privilegeEntity2.setResource(resourceEntity);
        privilegeEntities.add(privilegeEntity1);
        privilegeEntities.add(privilegeEntity2);
        java.util.Collection<org.springframework.security.core.GrantedAuthority> authorities = new org.apache.ambari.server.security.authorization.AuthorizationHelper().convertPrivilegesToAuthorities(privilegeEntities);
        org.junit.Assert.assertEquals("Wrong number of authorities", 2, authorities.size());
        java.util.Set<java.lang.String> authorityNames = new java.util.HashSet<>();
        for (org.springframework.security.core.GrantedAuthority authority : authorities) {
            authorityNames.add(authority.getAuthority());
        }
        org.junit.Assert.assertTrue(authorityNames.contains("CLUSTER.USER@1"));
        org.junit.Assert.assertTrue(authorityNames.contains("CLUSTER.ADMINISTRATOR@1"));
    }

    @org.junit.Test
    public void testAuthName() throws java.lang.Exception {
        java.lang.String user = org.apache.ambari.server.security.authorization.AuthorizationHelper.getAuthenticatedName();
        org.junit.Assert.assertNull(user);
        org.springframework.security.core.Authentication auth = new org.springframework.security.authentication.UsernamePasswordAuthenticationToken("admin", null);
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(auth);
        user = org.apache.ambari.server.security.authorization.AuthorizationHelper.getAuthenticatedName();
        org.junit.Assert.assertEquals("admin", user);
    }

    @org.junit.Test
    public void testAuthId() throws java.lang.Exception {
        java.lang.Integer userId = org.apache.ambari.server.security.authorization.AuthorizationHelper.getAuthenticatedId();
        org.junit.Assert.assertEquals(java.lang.Integer.valueOf(-1), userId);
        org.apache.ambari.server.orm.entities.PrincipalEntity principalEntity = new org.apache.ambari.server.orm.entities.PrincipalEntity();
        org.apache.ambari.server.orm.entities.UserEntity userEntity = new org.apache.ambari.server.orm.entities.UserEntity();
        userEntity.setUserId(1);
        userEntity.setPrincipal(principalEntity);
        org.apache.ambari.server.security.authorization.User user = new org.apache.ambari.server.security.authorization.User(userEntity);
        org.springframework.security.core.Authentication auth = new org.apache.ambari.server.security.authentication.AmbariUserAuthentication(null, new org.apache.ambari.server.security.authentication.AmbariUserDetailsImpl(user, null, null));
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(auth);
        userId = org.apache.ambari.server.security.authorization.AuthorizationHelper.getAuthenticatedId();
        org.junit.Assert.assertEquals(java.lang.Integer.valueOf(1), userId);
    }

    @org.junit.Test
    public void testAuthWithoutId() throws java.lang.Exception {
        org.springframework.security.core.Authentication auth = new org.springframework.security.authentication.UsernamePasswordAuthenticationToken("admin", null);
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(auth);
        java.lang.Integer userId = org.apache.ambari.server.security.authorization.AuthorizationHelper.getAuthenticatedId();
        org.junit.Assert.assertEquals(java.lang.Integer.valueOf(-1), userId);
    }

    @org.junit.Test
    public void testIsAuthorized() {
        com.google.inject.Provider viewInstanceDAOProvider = createNiceMock(com.google.inject.Provider.class);
        com.google.inject.Provider privilegeDAOProvider = createNiceMock(com.google.inject.Provider.class);
        org.apache.ambari.server.orm.dao.ViewInstanceDAO viewInstanceDAO = createNiceMock(org.apache.ambari.server.orm.dao.ViewInstanceDAO.class);
        org.apache.ambari.server.orm.dao.PrivilegeDAO privilegeDAO = createNiceMock(org.apache.ambari.server.orm.dao.PrivilegeDAO.class);
        EasyMock.expect(viewInstanceDAOProvider.get()).andReturn(viewInstanceDAO).anyTimes();
        EasyMock.expect(privilegeDAOProvider.get()).andReturn(privilegeDAO).anyTimes();
        replayAll();
        org.apache.ambari.server.security.authorization.AuthorizationHelper.viewInstanceDAOProvider = viewInstanceDAOProvider;
        org.apache.ambari.server.security.authorization.AuthorizationHelper.privilegeDAOProvider = privilegeDAOProvider;
        org.apache.ambari.server.orm.entities.RoleAuthorizationEntity readOnlyRoleAuthorizationEntity = new org.apache.ambari.server.orm.entities.RoleAuthorizationEntity();
        readOnlyRoleAuthorizationEntity.setAuthorizationId(org.apache.ambari.server.security.authorization.RoleAuthorization.CLUSTER_VIEW_METRICS.getId());
        org.apache.ambari.server.orm.entities.RoleAuthorizationEntity privilegedRoleAuthorizationEntity = new org.apache.ambari.server.orm.entities.RoleAuthorizationEntity();
        privilegedRoleAuthorizationEntity.setAuthorizationId(org.apache.ambari.server.security.authorization.RoleAuthorization.CLUSTER_TOGGLE_KERBEROS.getId());
        org.apache.ambari.server.orm.entities.RoleAuthorizationEntity administratorRoleAuthorizationEntity = new org.apache.ambari.server.orm.entities.RoleAuthorizationEntity();
        administratorRoleAuthorizationEntity.setAuthorizationId(org.apache.ambari.server.security.authorization.RoleAuthorization.AMBARI_MANAGE_USERS.getId());
        org.apache.ambari.server.orm.entities.ResourceTypeEntity ambariResourceTypeEntity = new org.apache.ambari.server.orm.entities.ResourceTypeEntity();
        ambariResourceTypeEntity.setId(1);
        ambariResourceTypeEntity.setName(org.apache.ambari.server.security.authorization.ResourceType.AMBARI.name());
        org.apache.ambari.server.orm.entities.ResourceTypeEntity clusterResourceTypeEntity = new org.apache.ambari.server.orm.entities.ResourceTypeEntity();
        clusterResourceTypeEntity.setId(1);
        clusterResourceTypeEntity.setName(org.apache.ambari.server.security.authorization.ResourceType.CLUSTER.name());
        org.apache.ambari.server.orm.entities.ResourceTypeEntity cluster2ResourceTypeEntity = new org.apache.ambari.server.orm.entities.ResourceTypeEntity();
        cluster2ResourceTypeEntity.setId(2);
        cluster2ResourceTypeEntity.setName(org.apache.ambari.server.security.authorization.ResourceType.CLUSTER.name());
        org.apache.ambari.server.orm.entities.ResourceEntity ambariResourceEntity = new org.apache.ambari.server.orm.entities.ResourceEntity();
        ambariResourceEntity.setResourceType(ambariResourceTypeEntity);
        ambariResourceEntity.setId(1L);
        org.apache.ambari.server.orm.entities.ResourceEntity clusterResourceEntity = new org.apache.ambari.server.orm.entities.ResourceEntity();
        clusterResourceEntity.setResourceType(clusterResourceTypeEntity);
        clusterResourceEntity.setId(1L);
        org.apache.ambari.server.orm.entities.ResourceEntity cluster2ResourceEntity = new org.apache.ambari.server.orm.entities.ResourceEntity();
        cluster2ResourceEntity.setResourceType(cluster2ResourceTypeEntity);
        cluster2ResourceEntity.setId(2L);
        org.apache.ambari.server.orm.entities.PermissionEntity readOnlyPermissionEntity = new org.apache.ambari.server.orm.entities.PermissionEntity();
        readOnlyPermissionEntity.addAuthorization(readOnlyRoleAuthorizationEntity);
        org.apache.ambari.server.orm.entities.PermissionEntity privilegedPermissionEntity = new org.apache.ambari.server.orm.entities.PermissionEntity();
        privilegedPermissionEntity.addAuthorization(readOnlyRoleAuthorizationEntity);
        privilegedPermissionEntity.addAuthorization(privilegedRoleAuthorizationEntity);
        org.apache.ambari.server.orm.entities.PermissionEntity administratorPermissionEntity = new org.apache.ambari.server.orm.entities.PermissionEntity();
        administratorPermissionEntity.addAuthorization(readOnlyRoleAuthorizationEntity);
        administratorPermissionEntity.addAuthorization(privilegedRoleAuthorizationEntity);
        administratorPermissionEntity.addAuthorization(administratorRoleAuthorizationEntity);
        org.apache.ambari.server.orm.entities.PrivilegeEntity readOnlyPrivilegeEntity = new org.apache.ambari.server.orm.entities.PrivilegeEntity();
        readOnlyPrivilegeEntity.setPermission(readOnlyPermissionEntity);
        readOnlyPrivilegeEntity.setResource(clusterResourceEntity);
        org.apache.ambari.server.orm.entities.PrivilegeEntity readOnly2PrivilegeEntity = new org.apache.ambari.server.orm.entities.PrivilegeEntity();
        readOnly2PrivilegeEntity.setPermission(readOnlyPermissionEntity);
        readOnly2PrivilegeEntity.setResource(cluster2ResourceEntity);
        org.apache.ambari.server.orm.entities.PrivilegeEntity privilegedPrivilegeEntity = new org.apache.ambari.server.orm.entities.PrivilegeEntity();
        privilegedPrivilegeEntity.setPermission(privilegedPermissionEntity);
        privilegedPrivilegeEntity.setResource(clusterResourceEntity);
        org.apache.ambari.server.orm.entities.PrivilegeEntity privileged2PrivilegeEntity = new org.apache.ambari.server.orm.entities.PrivilegeEntity();
        privileged2PrivilegeEntity.setPermission(privilegedPermissionEntity);
        privileged2PrivilegeEntity.setResource(cluster2ResourceEntity);
        org.apache.ambari.server.orm.entities.PrivilegeEntity administratorPrivilegeEntity = new org.apache.ambari.server.orm.entities.PrivilegeEntity();
        administratorPrivilegeEntity.setPermission(administratorPermissionEntity);
        administratorPrivilegeEntity.setResource(ambariResourceEntity);
        org.springframework.security.core.GrantedAuthority readOnlyAuthority = new org.apache.ambari.server.security.authorization.AmbariGrantedAuthority(readOnlyPrivilegeEntity);
        org.springframework.security.core.GrantedAuthority readOnly2Authority = new org.apache.ambari.server.security.authorization.AmbariGrantedAuthority(readOnly2PrivilegeEntity);
        org.springframework.security.core.GrantedAuthority privilegedAuthority = new org.apache.ambari.server.security.authorization.AmbariGrantedAuthority(privilegedPrivilegeEntity);
        org.springframework.security.core.GrantedAuthority privileged2Authority = new org.apache.ambari.server.security.authorization.AmbariGrantedAuthority(privileged2PrivilegeEntity);
        org.springframework.security.core.GrantedAuthority administratorAuthority = new org.apache.ambari.server.security.authorization.AmbariGrantedAuthority(administratorPrivilegeEntity);
        org.springframework.security.core.Authentication noAccessUser = new org.apache.ambari.server.security.authorization.AuthorizationHelperTest.TestAuthentication(java.util.Collections.<org.apache.ambari.server.security.authorization.AmbariGrantedAuthority>emptyList());
        org.springframework.security.core.Authentication readOnlyUser = new org.apache.ambari.server.security.authorization.AuthorizationHelperTest.TestAuthentication(java.util.Collections.singleton(readOnlyAuthority));
        org.springframework.security.core.Authentication privilegedUser = new org.apache.ambari.server.security.authorization.AuthorizationHelperTest.TestAuthentication(java.util.Arrays.asList(readOnlyAuthority, privilegedAuthority));
        org.springframework.security.core.Authentication privileged2User = new org.apache.ambari.server.security.authorization.AuthorizationHelperTest.TestAuthentication(java.util.Arrays.asList(readOnly2Authority, privileged2Authority));
        org.springframework.security.core.Authentication administratorUser = new org.apache.ambari.server.security.authorization.AuthorizationHelperTest.TestAuthentication(java.util.Collections.singleton(administratorAuthority));
        org.springframework.security.core.context.SecurityContext context = org.springframework.security.core.context.SecurityContextHolder.getContext();
        org.junit.Assert.assertFalse(org.apache.ambari.server.security.authorization.AuthorizationHelper.isAuthorized(null, org.apache.ambari.server.security.authorization.ResourceType.CLUSTER, 1L, java.util.EnumSet.of(org.apache.ambari.server.security.authorization.RoleAuthorization.CLUSTER_VIEW_METRICS)));
        org.junit.Assert.assertFalse(org.apache.ambari.server.security.authorization.AuthorizationHelper.isAuthorized(org.apache.ambari.server.security.authorization.ResourceType.CLUSTER, 1L, java.util.EnumSet.of(org.apache.ambari.server.security.authorization.RoleAuthorization.CLUSTER_VIEW_METRICS)));
        org.junit.Assert.assertFalse(org.apache.ambari.server.security.authorization.AuthorizationHelper.isAuthorized(noAccessUser, org.apache.ambari.server.security.authorization.ResourceType.CLUSTER, 1L, java.util.EnumSet.of(org.apache.ambari.server.security.authorization.RoleAuthorization.CLUSTER_VIEW_METRICS)));
        org.junit.Assert.assertFalse(org.apache.ambari.server.security.authorization.AuthorizationHelper.isAuthorized(noAccessUser, org.apache.ambari.server.security.authorization.ResourceType.CLUSTER, 1L, java.util.EnumSet.of(org.apache.ambari.server.security.authorization.RoleAuthorization.CLUSTER_TOGGLE_KERBEROS)));
        org.junit.Assert.assertFalse(org.apache.ambari.server.security.authorization.AuthorizationHelper.isAuthorized(noAccessUser, org.apache.ambari.server.security.authorization.ResourceType.CLUSTER, 1L, java.util.EnumSet.of(org.apache.ambari.server.security.authorization.RoleAuthorization.AMBARI_MANAGE_USERS)));
        org.junit.Assert.assertTrue(org.apache.ambari.server.security.authorization.AuthorizationHelper.isAuthorized(readOnlyUser, org.apache.ambari.server.security.authorization.ResourceType.CLUSTER, 1L, java.util.EnumSet.of(org.apache.ambari.server.security.authorization.RoleAuthorization.CLUSTER_VIEW_METRICS)));
        org.junit.Assert.assertFalse(org.apache.ambari.server.security.authorization.AuthorizationHelper.isAuthorized(readOnlyUser, org.apache.ambari.server.security.authorization.ResourceType.CLUSTER, 1L, java.util.EnumSet.of(org.apache.ambari.server.security.authorization.RoleAuthorization.CLUSTER_TOGGLE_KERBEROS)));
        org.junit.Assert.assertFalse(org.apache.ambari.server.security.authorization.AuthorizationHelper.isAuthorized(readOnlyUser, org.apache.ambari.server.security.authorization.ResourceType.CLUSTER, 1L, java.util.EnumSet.of(org.apache.ambari.server.security.authorization.RoleAuthorization.AMBARI_MANAGE_USERS)));
        org.junit.Assert.assertTrue(org.apache.ambari.server.security.authorization.AuthorizationHelper.isAuthorized(privilegedUser, org.apache.ambari.server.security.authorization.ResourceType.CLUSTER, 1L, java.util.EnumSet.of(org.apache.ambari.server.security.authorization.RoleAuthorization.CLUSTER_VIEW_METRICS)));
        org.junit.Assert.assertTrue(org.apache.ambari.server.security.authorization.AuthorizationHelper.isAuthorized(privilegedUser, org.apache.ambari.server.security.authorization.ResourceType.CLUSTER, 1L, java.util.EnumSet.of(org.apache.ambari.server.security.authorization.RoleAuthorization.CLUSTER_TOGGLE_KERBEROS)));
        org.junit.Assert.assertFalse(org.apache.ambari.server.security.authorization.AuthorizationHelper.isAuthorized(privilegedUser, org.apache.ambari.server.security.authorization.ResourceType.CLUSTER, 1L, java.util.EnumSet.of(org.apache.ambari.server.security.authorization.RoleAuthorization.AMBARI_MANAGE_USERS)));
        org.junit.Assert.assertFalse(org.apache.ambari.server.security.authorization.AuthorizationHelper.isAuthorized(privileged2User, org.apache.ambari.server.security.authorization.ResourceType.CLUSTER, 1L, java.util.EnumSet.of(org.apache.ambari.server.security.authorization.RoleAuthorization.CLUSTER_VIEW_METRICS)));
        org.junit.Assert.assertFalse(org.apache.ambari.server.security.authorization.AuthorizationHelper.isAuthorized(privileged2User, org.apache.ambari.server.security.authorization.ResourceType.CLUSTER, 1L, java.util.EnumSet.of(org.apache.ambari.server.security.authorization.RoleAuthorization.CLUSTER_TOGGLE_KERBEROS)));
        org.junit.Assert.assertFalse(org.apache.ambari.server.security.authorization.AuthorizationHelper.isAuthorized(privileged2User, org.apache.ambari.server.security.authorization.ResourceType.CLUSTER, 1L, java.util.EnumSet.of(org.apache.ambari.server.security.authorization.RoleAuthorization.AMBARI_MANAGE_USERS)));
        org.junit.Assert.assertTrue(org.apache.ambari.server.security.authorization.AuthorizationHelper.isAuthorized(administratorUser, org.apache.ambari.server.security.authorization.ResourceType.CLUSTER, 1L, java.util.EnumSet.of(org.apache.ambari.server.security.authorization.RoleAuthorization.CLUSTER_VIEW_METRICS)));
        org.junit.Assert.assertTrue(org.apache.ambari.server.security.authorization.AuthorizationHelper.isAuthorized(administratorUser, org.apache.ambari.server.security.authorization.ResourceType.CLUSTER, 1L, java.util.EnumSet.of(org.apache.ambari.server.security.authorization.RoleAuthorization.CLUSTER_TOGGLE_KERBEROS)));
        org.junit.Assert.assertTrue(org.apache.ambari.server.security.authorization.AuthorizationHelper.isAuthorized(administratorUser, org.apache.ambari.server.security.authorization.ResourceType.CLUSTER, 1L, java.util.EnumSet.of(org.apache.ambari.server.security.authorization.RoleAuthorization.AMBARI_MANAGE_USERS)));
        context.setAuthentication(noAccessUser);
        org.junit.Assert.assertFalse(org.apache.ambari.server.security.authorization.AuthorizationHelper.isAuthorized(org.apache.ambari.server.security.authorization.ResourceType.CLUSTER, 1L, java.util.EnumSet.of(org.apache.ambari.server.security.authorization.RoleAuthorization.CLUSTER_VIEW_METRICS)));
        org.junit.Assert.assertFalse(org.apache.ambari.server.security.authorization.AuthorizationHelper.isAuthorized(org.apache.ambari.server.security.authorization.ResourceType.CLUSTER, 1L, java.util.EnumSet.of(org.apache.ambari.server.security.authorization.RoleAuthorization.CLUSTER_TOGGLE_KERBEROS)));
        org.junit.Assert.assertFalse(org.apache.ambari.server.security.authorization.AuthorizationHelper.isAuthorized(org.apache.ambari.server.security.authorization.ResourceType.CLUSTER, 1L, java.util.EnumSet.of(org.apache.ambari.server.security.authorization.RoleAuthorization.CLUSTER_TOGGLE_KERBEROS)));
        context.setAuthentication(readOnlyUser);
        org.junit.Assert.assertTrue(org.apache.ambari.server.security.authorization.AuthorizationHelper.isAuthorized(org.apache.ambari.server.security.authorization.ResourceType.CLUSTER, 1L, java.util.EnumSet.of(org.apache.ambari.server.security.authorization.RoleAuthorization.CLUSTER_VIEW_METRICS)));
        org.junit.Assert.assertFalse(org.apache.ambari.server.security.authorization.AuthorizationHelper.isAuthorized(org.apache.ambari.server.security.authorization.ResourceType.CLUSTER, 1L, java.util.EnumSet.of(org.apache.ambari.server.security.authorization.RoleAuthorization.CLUSTER_TOGGLE_KERBEROS)));
        org.junit.Assert.assertFalse(org.apache.ambari.server.security.authorization.AuthorizationHelper.isAuthorized(org.apache.ambari.server.security.authorization.ResourceType.CLUSTER, 1L, java.util.EnumSet.of(org.apache.ambari.server.security.authorization.RoleAuthorization.AMBARI_MANAGE_USERS)));
        context.setAuthentication(privilegedUser);
        org.junit.Assert.assertTrue(org.apache.ambari.server.security.authorization.AuthorizationHelper.isAuthorized(org.apache.ambari.server.security.authorization.ResourceType.CLUSTER, 1L, java.util.EnumSet.of(org.apache.ambari.server.security.authorization.RoleAuthorization.CLUSTER_VIEW_METRICS)));
        org.junit.Assert.assertTrue(org.apache.ambari.server.security.authorization.AuthorizationHelper.isAuthorized(org.apache.ambari.server.security.authorization.ResourceType.CLUSTER, 1L, java.util.EnumSet.of(org.apache.ambari.server.security.authorization.RoleAuthorization.CLUSTER_TOGGLE_KERBEROS)));
        org.junit.Assert.assertFalse(org.apache.ambari.server.security.authorization.AuthorizationHelper.isAuthorized(org.apache.ambari.server.security.authorization.ResourceType.CLUSTER, 1L, java.util.EnumSet.of(org.apache.ambari.server.security.authorization.RoleAuthorization.AMBARI_MANAGE_USERS)));
        context.setAuthentication(privileged2User);
        org.junit.Assert.assertFalse(org.apache.ambari.server.security.authorization.AuthorizationHelper.isAuthorized(org.apache.ambari.server.security.authorization.ResourceType.CLUSTER, 1L, java.util.EnumSet.of(org.apache.ambari.server.security.authorization.RoleAuthorization.CLUSTER_VIEW_METRICS)));
        org.junit.Assert.assertFalse(org.apache.ambari.server.security.authorization.AuthorizationHelper.isAuthorized(org.apache.ambari.server.security.authorization.ResourceType.CLUSTER, 1L, java.util.EnumSet.of(org.apache.ambari.server.security.authorization.RoleAuthorization.CLUSTER_TOGGLE_KERBEROS)));
        org.junit.Assert.assertFalse(org.apache.ambari.server.security.authorization.AuthorizationHelper.isAuthorized(org.apache.ambari.server.security.authorization.ResourceType.CLUSTER, 1L, java.util.EnumSet.of(org.apache.ambari.server.security.authorization.RoleAuthorization.AMBARI_MANAGE_USERS)));
        context.setAuthentication(administratorUser);
        org.junit.Assert.assertTrue(org.apache.ambari.server.security.authorization.AuthorizationHelper.isAuthorized(org.apache.ambari.server.security.authorization.ResourceType.CLUSTER, 1L, java.util.EnumSet.of(org.apache.ambari.server.security.authorization.RoleAuthorization.CLUSTER_VIEW_METRICS)));
        org.junit.Assert.assertTrue(org.apache.ambari.server.security.authorization.AuthorizationHelper.isAuthorized(org.apache.ambari.server.security.authorization.ResourceType.CLUSTER, 1L, java.util.EnumSet.of(org.apache.ambari.server.security.authorization.RoleAuthorization.CLUSTER_TOGGLE_KERBEROS)));
        org.junit.Assert.assertTrue(org.apache.ambari.server.security.authorization.AuthorizationHelper.isAuthorized(org.apache.ambari.server.security.authorization.ResourceType.CLUSTER, 1L, java.util.EnumSet.of(org.apache.ambari.server.security.authorization.RoleAuthorization.AMBARI_MANAGE_USERS)));
    }

    @org.junit.Test
    public void testIsAuthorizedForSpecificView() {
        org.apache.ambari.server.orm.entities.RoleAuthorizationEntity readOnlyRoleAuthorizationEntity = new org.apache.ambari.server.orm.entities.RoleAuthorizationEntity();
        readOnlyRoleAuthorizationEntity.setAuthorizationId(org.apache.ambari.server.security.authorization.RoleAuthorization.CLUSTER_VIEW_METRICS.getId());
        org.apache.ambari.server.orm.entities.RoleAuthorizationEntity viewUseRoleAuthorizationEntity = new org.apache.ambari.server.orm.entities.RoleAuthorizationEntity();
        viewUseRoleAuthorizationEntity.setAuthorizationId(org.apache.ambari.server.security.authorization.RoleAuthorization.VIEW_USE.getId());
        org.apache.ambari.server.orm.entities.RoleAuthorizationEntity administratorRoleAuthorizationEntity = new org.apache.ambari.server.orm.entities.RoleAuthorizationEntity();
        administratorRoleAuthorizationEntity.setAuthorizationId(org.apache.ambari.server.security.authorization.RoleAuthorization.AMBARI_MANAGE_USERS.getId());
        org.apache.ambari.server.orm.entities.ResourceTypeEntity ambariResourceTypeEntity = new org.apache.ambari.server.orm.entities.ResourceTypeEntity();
        ambariResourceTypeEntity.setId(1);
        ambariResourceTypeEntity.setName(org.apache.ambari.server.security.authorization.ResourceType.AMBARI.name());
        org.apache.ambari.server.orm.entities.ResourceTypeEntity clusterResourceTypeEntity = new org.apache.ambari.server.orm.entities.ResourceTypeEntity();
        clusterResourceTypeEntity.setId(1);
        clusterResourceTypeEntity.setName(org.apache.ambari.server.security.authorization.ResourceType.CLUSTER.name());
        org.apache.ambari.server.orm.entities.ResourceTypeEntity viewResourceTypeEntity = new org.apache.ambari.server.orm.entities.ResourceTypeEntity();
        viewResourceTypeEntity.setId(30);
        viewResourceTypeEntity.setName(org.apache.ambari.server.security.authorization.ResourceType.VIEW.name());
        org.apache.ambari.server.orm.entities.ResourceEntity ambariResourceEntity = new org.apache.ambari.server.orm.entities.ResourceEntity();
        ambariResourceEntity.setResourceType(ambariResourceTypeEntity);
        ambariResourceEntity.setId(1L);
        org.apache.ambari.server.orm.entities.ResourceEntity clusterResourceEntity = new org.apache.ambari.server.orm.entities.ResourceEntity();
        clusterResourceEntity.setResourceType(clusterResourceTypeEntity);
        clusterResourceEntity.setId(1L);
        org.apache.ambari.server.orm.entities.ResourceEntity viewResourceEntity = new org.apache.ambari.server.orm.entities.ResourceEntity();
        viewResourceEntity.setResourceType(viewResourceTypeEntity);
        viewResourceEntity.setId(53L);
        org.apache.ambari.server.orm.entities.PermissionEntity readOnlyPermissionEntity = new org.apache.ambari.server.orm.entities.PermissionEntity();
        readOnlyPermissionEntity.addAuthorization(readOnlyRoleAuthorizationEntity);
        org.apache.ambari.server.orm.entities.PermissionEntity viewUsePermissionEntity = new org.apache.ambari.server.orm.entities.PermissionEntity();
        viewUsePermissionEntity.addAuthorization(readOnlyRoleAuthorizationEntity);
        viewUsePermissionEntity.addAuthorization(viewUseRoleAuthorizationEntity);
        org.apache.ambari.server.orm.entities.PermissionEntity administratorPermissionEntity = new org.apache.ambari.server.orm.entities.PermissionEntity();
        administratorPermissionEntity.addAuthorization(readOnlyRoleAuthorizationEntity);
        administratorPermissionEntity.addAuthorization(viewUseRoleAuthorizationEntity);
        administratorPermissionEntity.addAuthorization(administratorRoleAuthorizationEntity);
        org.apache.ambari.server.orm.entities.PrivilegeEntity readOnlyPrivilegeEntity = new org.apache.ambari.server.orm.entities.PrivilegeEntity();
        readOnlyPrivilegeEntity.setPermission(readOnlyPermissionEntity);
        readOnlyPrivilegeEntity.setResource(clusterResourceEntity);
        org.apache.ambari.server.orm.entities.PrivilegeEntity viewUsePrivilegeEntity = new org.apache.ambari.server.orm.entities.PrivilegeEntity();
        viewUsePrivilegeEntity.setPermission(viewUsePermissionEntity);
        viewUsePrivilegeEntity.setResource(viewResourceEntity);
        org.apache.ambari.server.orm.entities.PrivilegeEntity administratorPrivilegeEntity = new org.apache.ambari.server.orm.entities.PrivilegeEntity();
        administratorPrivilegeEntity.setPermission(administratorPermissionEntity);
        administratorPrivilegeEntity.setResource(ambariResourceEntity);
        org.springframework.security.core.GrantedAuthority readOnlyAuthority = new org.apache.ambari.server.security.authorization.AmbariGrantedAuthority(readOnlyPrivilegeEntity);
        org.springframework.security.core.GrantedAuthority viewUseAuthority = new org.apache.ambari.server.security.authorization.AmbariGrantedAuthority(viewUsePrivilegeEntity);
        org.springframework.security.core.GrantedAuthority administratorAuthority = new org.apache.ambari.server.security.authorization.AmbariGrantedAuthority(administratorPrivilegeEntity);
        org.springframework.security.core.Authentication readOnlyUser = new org.apache.ambari.server.security.authorization.AuthorizationHelperTest.TestAuthentication(java.util.Collections.singleton(readOnlyAuthority));
        org.springframework.security.core.Authentication viewUser = new org.apache.ambari.server.security.authorization.AuthorizationHelperTest.TestAuthentication(java.util.Arrays.asList(readOnlyAuthority, viewUseAuthority));
        org.springframework.security.core.Authentication administratorUser = new org.apache.ambari.server.security.authorization.AuthorizationHelperTest.TestAuthentication(java.util.Collections.singleton(administratorAuthority));
        org.springframework.security.core.context.SecurityContext context = org.springframework.security.core.context.SecurityContextHolder.getContext();
        java.util.Set<org.apache.ambari.server.security.authorization.RoleAuthorization> permissionsViewUse = java.util.EnumSet.of(org.apache.ambari.server.security.authorization.RoleAuthorization.VIEW_USE);
        context.setAuthentication(readOnlyUser);
        org.junit.Assert.assertFalse(org.apache.ambari.server.security.authorization.AuthorizationHelper.isAuthorized(org.apache.ambari.server.security.authorization.ResourceType.VIEW, 53L, permissionsViewUse));
        org.junit.Assert.assertFalse(org.apache.ambari.server.security.authorization.AuthorizationHelper.isAuthorized(org.apache.ambari.server.security.authorization.ResourceType.VIEW, 50L, permissionsViewUse));
        context.setAuthentication(viewUser);
        org.junit.Assert.assertTrue(org.apache.ambari.server.security.authorization.AuthorizationHelper.isAuthorized(org.apache.ambari.server.security.authorization.ResourceType.VIEW, 53L, permissionsViewUse));
        org.junit.Assert.assertFalse(org.apache.ambari.server.security.authorization.AuthorizationHelper.isAuthorized(org.apache.ambari.server.security.authorization.ResourceType.VIEW, 50L, permissionsViewUse));
        context.setAuthentication(administratorUser);
        org.junit.Assert.assertTrue(org.apache.ambari.server.security.authorization.AuthorizationHelper.isAuthorized(org.apache.ambari.server.security.authorization.ResourceType.VIEW, 53L, permissionsViewUse));
        org.junit.Assert.assertTrue(org.apache.ambari.server.security.authorization.AuthorizationHelper.isAuthorized(org.apache.ambari.server.security.authorization.ResourceType.VIEW, 50L, permissionsViewUse));
    }

    public void testAddLoginNameAlias() throws java.lang.Exception {
        EasyMock.reset(servletRequestAttributes);
        org.springframework.web.context.request.RequestContextHolder.setRequestAttributes(servletRequestAttributes);
        servletRequestAttributes.setAttribute(EasyMock.eq("loginAlias"), EasyMock.eq("user"), EasyMock.eq(org.springframework.web.context.request.RequestAttributes.SCOPE_SESSION));
        EasyMock.expectLastCall().once();
        EasyMock.replay(servletRequestAttributes);
        org.apache.ambari.server.security.authorization.AuthorizationHelper.addLoginNameAlias("user", "loginAlias");
        EasyMock.verify(servletRequestAttributes);
    }

    @org.junit.Test
    public void testResolveLoginAliasToUserName() throws java.lang.Exception {
        EasyMock.reset(servletRequestAttributes);
        org.springframework.web.context.request.RequestContextHolder.setRequestAttributes(servletRequestAttributes);
        EasyMock.expect(servletRequestAttributes.getAttribute(EasyMock.eq("loginAlias1"), EasyMock.eq(org.springframework.web.context.request.RequestAttributes.SCOPE_SESSION))).andReturn("user1").atLeastOnce();
        EasyMock.replay(servletRequestAttributes);
        java.lang.String user = org.apache.ambari.server.security.authorization.AuthorizationHelper.resolveLoginAliasToUserName("loginAlias1");
        EasyMock.verify(servletRequestAttributes);
        org.junit.Assert.assertEquals("user1", user);
    }

    @org.junit.Test
    public void testResolveNoLoginAliasToUserName() throws java.lang.Exception {
        EasyMock.reset(servletRequestAttributes);
        org.springframework.web.context.request.RequestContextHolder.setRequestAttributes(null);
        org.junit.Assert.assertEquals("user", org.apache.ambari.server.security.authorization.AuthorizationHelper.resolveLoginAliasToUserName("user"));
        org.springframework.web.context.request.RequestContextHolder.setRequestAttributes(servletRequestAttributes);
        EasyMock.expect(servletRequestAttributes.getAttribute(EasyMock.eq("nosuchalias"), EasyMock.eq(org.springframework.web.context.request.RequestAttributes.SCOPE_SESSION))).andReturn(null).atLeastOnce();
        EasyMock.replay(servletRequestAttributes);
        java.lang.String user = org.apache.ambari.server.security.authorization.AuthorizationHelper.resolveLoginAliasToUserName("nosuchalias");
        EasyMock.verify(servletRequestAttributes);
        org.junit.Assert.assertEquals("nosuchalias", user);
    }

    private class TestAuthentication implements org.springframework.security.core.Authentication {
        private final java.util.Collection<? extends org.springframework.security.core.GrantedAuthority> grantedAuthorities;

        public TestAuthentication(java.util.Collection<? extends org.springframework.security.core.GrantedAuthority> grantedAuthorities) {
            this.grantedAuthorities = grantedAuthorities;
        }

        @java.lang.Override
        public java.util.Collection<? extends org.springframework.security.core.GrantedAuthority> getAuthorities() {
            return grantedAuthorities;
        }

        @java.lang.Override
        public java.lang.Object getCredentials() {
            return null;
        }

        @java.lang.Override
        public java.lang.Object getDetails() {
            return null;
        }

        @java.lang.Override
        public java.lang.Object getPrincipal() {
            return null;
        }

        @java.lang.Override
        public boolean isAuthenticated() {
            return true;
        }

        @java.lang.Override
        public void setAuthenticated(boolean isAuthenticated) throws java.lang.IllegalArgumentException {
        }

        @java.lang.Override
        public java.lang.String getName() {
            return null;
        }
    }
}