package org.apache.ambari.server.controller.internal;
import javax.persistence.EntityManager;
import org.easymock.EasyMockSupport;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.expect;
public class UserPrivilegeResourceProviderTest extends org.easymock.EasyMockSupport {
    @org.junit.Test(expected = org.apache.ambari.server.controller.spi.SystemException.class)
    public void testCreateResources() throws java.lang.Exception {
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator("user1", 2L));
        org.apache.ambari.server.controller.internal.UserPrivilegeResourceProvider resourceProvider = new org.apache.ambari.server.controller.internal.UserPrivilegeResourceProvider();
        resourceProvider.createResources(createNiceMock(org.apache.ambari.server.controller.spi.Request.class));
    }

    @org.junit.Test
    public void testGetResources_Administrator() throws java.lang.Exception {
        getResourcesTest(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator("admin"), "User1");
    }

    @org.junit.Test
    public void testGetResources_NonAdministrator_Self() throws java.lang.Exception {
        getResourcesTest(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator("User1", 2L), "User1");
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testGetResources_NonAdministrator_Other() throws java.lang.Exception {
        getResourcesTest(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator("User1", 2L), "User10");
    }

    @org.junit.Test(expected = org.apache.ambari.server.controller.spi.SystemException.class)
    public void testUpdateResources() throws java.lang.Exception {
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator("user1", 2L));
        org.apache.ambari.server.controller.internal.UserPrivilegeResourceProvider resourceProvider = new org.apache.ambari.server.controller.internal.UserPrivilegeResourceProvider();
        resourceProvider.updateResources(createNiceMock(org.apache.ambari.server.controller.spi.Request.class), createNiceMock(org.apache.ambari.server.controller.spi.Predicate.class));
    }

    @org.junit.Test(expected = org.apache.ambari.server.controller.spi.SystemException.class)
    public void testDeleteResources() throws java.lang.Exception {
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator("user1", 2L));
        org.apache.ambari.server.controller.internal.UserPrivilegeResourceProvider resourceProvider = new org.apache.ambari.server.controller.internal.UserPrivilegeResourceProvider();
        resourceProvider.deleteResources(createNiceMock(org.apache.ambari.server.controller.spi.Request.class), createNiceMock(org.apache.ambari.server.controller.spi.Predicate.class));
    }

    @org.junit.Test
    public void testToResource_AMBARI() {
        org.apache.ambari.server.orm.entities.PermissionEntity permissionEntity = createMock(org.apache.ambari.server.orm.entities.PermissionEntity.class);
        EasyMock.expect(permissionEntity.getPermissionName()).andReturn("ADMINISTRATOR").atLeastOnce();
        EasyMock.expect(permissionEntity.getPermissionLabel()).andReturn("Ambari Administrator").atLeastOnce();
        org.apache.ambari.server.orm.entities.PrincipalTypeEntity principalTypeEntity = createMock(org.apache.ambari.server.orm.entities.PrincipalTypeEntity.class);
        EasyMock.expect(principalTypeEntity.getName()).andReturn("USER").atLeastOnce();
        org.apache.ambari.server.orm.entities.PrincipalEntity principalEntity = createMock(org.apache.ambari.server.orm.entities.PrincipalEntity.class);
        EasyMock.expect(principalEntity.getPrincipalType()).andReturn(principalTypeEntity).atLeastOnce();
        org.apache.ambari.server.orm.entities.ResourceTypeEntity resourceTypeEntity = createMock(org.apache.ambari.server.orm.entities.ResourceTypeEntity.class);
        EasyMock.expect(resourceTypeEntity.getName()).andReturn("AMBARI").atLeastOnce();
        org.apache.ambari.server.orm.entities.ResourceEntity resourceEntity = createMock(org.apache.ambari.server.orm.entities.ResourceEntity.class);
        EasyMock.expect(resourceEntity.getResourceType()).andReturn(resourceTypeEntity).atLeastOnce();
        org.apache.ambari.server.orm.entities.PrivilegeEntity privilegeEntity = createMock(org.apache.ambari.server.orm.entities.PrivilegeEntity.class);
        EasyMock.expect(privilegeEntity.getId()).andReturn(1).atLeastOnce();
        EasyMock.expect(privilegeEntity.getPermission()).andReturn(permissionEntity).atLeastOnce();
        EasyMock.expect(privilegeEntity.getPrincipal()).andReturn(principalEntity).atLeastOnce();
        EasyMock.expect(privilegeEntity.getResource()).andReturn(resourceEntity).atLeastOnce();
        org.apache.ambari.server.orm.entities.UserEntity userEntity = createMock(org.apache.ambari.server.orm.entities.UserEntity.class);
        EasyMock.expect(userEntity.getUserName()).andReturn("jdoe").atLeastOnce();
        org.apache.ambari.server.orm.dao.UserDAO userDAO = createMock(org.apache.ambari.server.orm.dao.UserDAO.class);
        EasyMock.expect(userDAO.findUserByPrincipal(EasyMock.anyObject(org.apache.ambari.server.orm.entities.PrincipalEntity.class))).andReturn(userEntity).anyTimes();
        org.apache.ambari.server.orm.dao.ClusterDAO clusterDAO = createMock(org.apache.ambari.server.orm.dao.ClusterDAO.class);
        org.apache.ambari.server.orm.dao.GroupDAO groupDAO = createMock(org.apache.ambari.server.orm.dao.GroupDAO.class);
        org.apache.ambari.server.orm.dao.ViewInstanceDAO viewInstanceDAO = createMock(org.apache.ambari.server.orm.dao.ViewInstanceDAO.class);
        org.apache.ambari.server.security.authorization.Users users = createNiceMock(org.apache.ambari.server.security.authorization.Users.class);
        replayAll();
        org.apache.ambari.server.controller.internal.UserPrivilegeResourceProvider.init(userDAO, clusterDAO, groupDAO, viewInstanceDAO, users);
        org.apache.ambari.server.controller.internal.UserPrivilegeResourceProvider provider = new org.apache.ambari.server.controller.internal.UserPrivilegeResourceProvider();
        org.apache.ambari.server.controller.UserPrivilegeResponse response = provider.getResponse(privilegeEntity, "jdoe");
        org.apache.ambari.server.controller.spi.Resource resource = provider.toResource(response, provider.getPropertyIds());
        junit.framework.Assert.assertEquals(org.apache.ambari.server.security.authorization.ResourceType.AMBARI.name(), resource.getPropertyValue(org.apache.ambari.server.controller.internal.UserPrivilegeResourceProvider.TYPE));
        verifyAll();
    }

    @org.junit.Test
    public void testToResource_CLUSTER() {
        org.apache.ambari.server.orm.entities.PermissionEntity permissionEntity = createMock(org.apache.ambari.server.orm.entities.PermissionEntity.class);
        EasyMock.expect(permissionEntity.getPermissionName()).andReturn("CLUSTER.ADMINISTRATOR").atLeastOnce();
        EasyMock.expect(permissionEntity.getPermissionLabel()).andReturn("Cluster Administrator").atLeastOnce();
        org.apache.ambari.server.orm.entities.PrincipalTypeEntity principalTypeEntity = createMock(org.apache.ambari.server.orm.entities.PrincipalTypeEntity.class);
        EasyMock.expect(principalTypeEntity.getName()).andReturn("USER").atLeastOnce();
        org.apache.ambari.server.orm.entities.PrincipalEntity principalEntity = createMock(org.apache.ambari.server.orm.entities.PrincipalEntity.class);
        EasyMock.expect(principalEntity.getPrincipalType()).andReturn(principalTypeEntity).atLeastOnce();
        org.apache.ambari.server.orm.entities.ClusterEntity clusterEntity = createMock(org.apache.ambari.server.orm.entities.ClusterEntity.class);
        EasyMock.expect(clusterEntity.getClusterName()).andReturn("TestCluster").atLeastOnce();
        org.apache.ambari.server.orm.entities.ResourceTypeEntity resourceTypeEntity = createMock(org.apache.ambari.server.orm.entities.ResourceTypeEntity.class);
        EasyMock.expect(resourceTypeEntity.getName()).andReturn("CLUSTER").atLeastOnce();
        org.apache.ambari.server.orm.entities.ResourceEntity resourceEntity = createMock(org.apache.ambari.server.orm.entities.ResourceEntity.class);
        EasyMock.expect(resourceEntity.getId()).andReturn(1L).atLeastOnce();
        EasyMock.expect(resourceEntity.getResourceType()).andReturn(resourceTypeEntity).atLeastOnce();
        org.apache.ambari.server.orm.entities.PrivilegeEntity privilegeEntity = createMock(org.apache.ambari.server.orm.entities.PrivilegeEntity.class);
        EasyMock.expect(privilegeEntity.getId()).andReturn(1).atLeastOnce();
        EasyMock.expect(privilegeEntity.getPermission()).andReturn(permissionEntity).atLeastOnce();
        EasyMock.expect(privilegeEntity.getPrincipal()).andReturn(principalEntity).atLeastOnce();
        EasyMock.expect(privilegeEntity.getResource()).andReturn(resourceEntity).atLeastOnce();
        org.apache.ambari.server.orm.entities.UserEntity userEntity = createMock(org.apache.ambari.server.orm.entities.UserEntity.class);
        EasyMock.expect(userEntity.getUserName()).andReturn("jdoe").atLeastOnce();
        org.apache.ambari.server.orm.dao.ClusterDAO clusterDAO = createMock(org.apache.ambari.server.orm.dao.ClusterDAO.class);
        EasyMock.expect(clusterDAO.findByResourceId(1L)).andReturn(clusterEntity).atLeastOnce();
        org.apache.ambari.server.orm.dao.GroupDAO groupDAO = createMock(org.apache.ambari.server.orm.dao.GroupDAO.class);
        org.apache.ambari.server.orm.dao.ViewInstanceDAO viewInstanceDAO = createMock(org.apache.ambari.server.orm.dao.ViewInstanceDAO.class);
        org.apache.ambari.server.orm.dao.UserDAO userDAO = createMock(org.apache.ambari.server.orm.dao.UserDAO.class);
        EasyMock.expect(userDAO.findUserByPrincipal(EasyMock.anyObject(org.apache.ambari.server.orm.entities.PrincipalEntity.class))).andReturn(userEntity).anyTimes();
        org.apache.ambari.server.security.authorization.Users users = createNiceMock(org.apache.ambari.server.security.authorization.Users.class);
        replayAll();
        org.apache.ambari.server.controller.internal.UserPrivilegeResourceProvider.init(userDAO, clusterDAO, groupDAO, viewInstanceDAO, users);
        org.apache.ambari.server.controller.internal.UserPrivilegeResourceProvider provider = new org.apache.ambari.server.controller.internal.UserPrivilegeResourceProvider();
        org.apache.ambari.server.controller.UserPrivilegeResponse response = provider.getResponse(privilegeEntity, "jdoe");
        org.apache.ambari.server.controller.spi.Resource resource = provider.toResource(response, provider.getPropertyIds());
        junit.framework.Assert.assertEquals("TestCluster", resource.getPropertyValue(org.apache.ambari.server.controller.internal.ClusterPrivilegeResourceProvider.CLUSTER_NAME));
        junit.framework.Assert.assertEquals(org.apache.ambari.server.security.authorization.ResourceType.CLUSTER.name(), resource.getPropertyValue(org.apache.ambari.server.controller.internal.UserPrivilegeResourceProvider.TYPE));
        verifyAll();
    }

    @org.junit.Test
    public void testToResource_VIEW() {
        org.apache.ambari.server.orm.entities.PermissionEntity permissionEntity = createMock(org.apache.ambari.server.orm.entities.PermissionEntity.class);
        EasyMock.expect(permissionEntity.getPermissionName()).andReturn("CLUSTER.ADMINISTRATOR").atLeastOnce();
        EasyMock.expect(permissionEntity.getPermissionLabel()).andReturn("Cluster Administrator").atLeastOnce();
        org.apache.ambari.server.orm.entities.PrincipalTypeEntity principalTypeEntity = createMock(org.apache.ambari.server.orm.entities.PrincipalTypeEntity.class);
        EasyMock.expect(principalTypeEntity.getName()).andReturn("USER").atLeastOnce();
        org.apache.ambari.server.orm.entities.PrincipalEntity principalEntity = createMock(org.apache.ambari.server.orm.entities.PrincipalEntity.class);
        EasyMock.expect(principalEntity.getPrincipalType()).andReturn(principalTypeEntity).atLeastOnce();
        org.apache.ambari.server.orm.entities.ViewEntity viewEntity = createMock(org.apache.ambari.server.orm.entities.ViewEntity.class);
        EasyMock.expect(viewEntity.getCommonName()).andReturn("TestView").atLeastOnce();
        EasyMock.expect(viewEntity.getVersion()).andReturn("1.2.3.4").atLeastOnce();
        org.apache.ambari.server.orm.entities.ViewInstanceEntity viewInstanceEntity = createMock(org.apache.ambari.server.orm.entities.ViewInstanceEntity.class);
        EasyMock.expect(viewInstanceEntity.getViewEntity()).andReturn(viewEntity).atLeastOnce();
        EasyMock.expect(viewInstanceEntity.getName()).andReturn("Test View").atLeastOnce();
        org.apache.ambari.server.orm.entities.ResourceTypeEntity resourceTypeEntity = createMock(org.apache.ambari.server.orm.entities.ResourceTypeEntity.class);
        EasyMock.expect(resourceTypeEntity.getName()).andReturn("VIEW").atLeastOnce();
        org.apache.ambari.server.orm.entities.ResourceEntity resourceEntity = createMock(org.apache.ambari.server.orm.entities.ResourceEntity.class);
        EasyMock.expect(resourceEntity.getId()).andReturn(1L).atLeastOnce();
        EasyMock.expect(resourceEntity.getResourceType()).andReturn(resourceTypeEntity).atLeastOnce();
        org.apache.ambari.server.orm.entities.PrivilegeEntity privilegeEntity = createMock(org.apache.ambari.server.orm.entities.PrivilegeEntity.class);
        EasyMock.expect(privilegeEntity.getId()).andReturn(1).atLeastOnce();
        EasyMock.expect(privilegeEntity.getPermission()).andReturn(permissionEntity).atLeastOnce();
        EasyMock.expect(privilegeEntity.getPrincipal()).andReturn(principalEntity).atLeastOnce();
        EasyMock.expect(privilegeEntity.getResource()).andReturn(resourceEntity).atLeastOnce();
        org.apache.ambari.server.orm.entities.UserEntity userEntity = createMock(org.apache.ambari.server.orm.entities.UserEntity.class);
        EasyMock.expect(userEntity.getUserName()).andReturn("jdoe").atLeastOnce();
        org.apache.ambari.server.orm.dao.ClusterDAO clusterDAO = createMock(org.apache.ambari.server.orm.dao.ClusterDAO.class);
        org.apache.ambari.server.orm.dao.GroupDAO groupDAO = createMock(org.apache.ambari.server.orm.dao.GroupDAO.class);
        org.apache.ambari.server.orm.dao.ViewInstanceDAO viewInstanceDAO = createMock(org.apache.ambari.server.orm.dao.ViewInstanceDAO.class);
        EasyMock.expect(viewInstanceDAO.findByResourceId(1L)).andReturn(viewInstanceEntity).atLeastOnce();
        org.apache.ambari.server.orm.dao.UserDAO userDAO = createMock(org.apache.ambari.server.orm.dao.UserDAO.class);
        EasyMock.expect(userDAO.findUserByPrincipal(EasyMock.anyObject(org.apache.ambari.server.orm.entities.PrincipalEntity.class))).andReturn(userEntity).anyTimes();
        org.apache.ambari.server.security.authorization.Users users = createNiceMock(org.apache.ambari.server.security.authorization.Users.class);
        replayAll();
        org.apache.ambari.server.controller.internal.UserPrivilegeResourceProvider.init(userDAO, clusterDAO, groupDAO, viewInstanceDAO, users);
        org.apache.ambari.server.controller.internal.UserPrivilegeResourceProvider provider = new org.apache.ambari.server.controller.internal.UserPrivilegeResourceProvider();
        org.apache.ambari.server.controller.UserPrivilegeResponse response = provider.getResponse(privilegeEntity, "jdoe");
        org.apache.ambari.server.controller.spi.Resource resource = provider.toResource(response, provider.getPropertyIds());
        junit.framework.Assert.assertEquals("Test View", resource.getPropertyValue(org.apache.ambari.server.controller.internal.ViewPrivilegeResourceProvider.INSTANCE_NAME));
        junit.framework.Assert.assertEquals("TestView", resource.getPropertyValue(org.apache.ambari.server.controller.internal.ViewPrivilegeResourceProvider.VIEW_NAME));
        junit.framework.Assert.assertEquals("1.2.3.4", resource.getPropertyValue(org.apache.ambari.server.controller.internal.ViewPrivilegeResourceProvider.VERSION));
        junit.framework.Assert.assertEquals(org.apache.ambari.server.security.authorization.ResourceType.VIEW.name(), resource.getPropertyValue(org.apache.ambari.server.controller.internal.UserPrivilegeResourceProvider.TYPE));
        verifyAll();
    }

    @org.junit.Test
    public void testToResource_SpecificVIEW() {
        org.apache.ambari.server.orm.entities.PermissionEntity permissionEntity = createMock(org.apache.ambari.server.orm.entities.PermissionEntity.class);
        EasyMock.expect(permissionEntity.getPermissionName()).andReturn("CLUSTER.ADMINISTRATOR").atLeastOnce();
        EasyMock.expect(permissionEntity.getPermissionLabel()).andReturn("Cluster Administrator").atLeastOnce();
        org.apache.ambari.server.orm.entities.PrincipalTypeEntity principalTypeEntity = createMock(org.apache.ambari.server.orm.entities.PrincipalTypeEntity.class);
        EasyMock.expect(principalTypeEntity.getName()).andReturn("USER").atLeastOnce();
        org.apache.ambari.server.orm.entities.PrincipalEntity principalEntity = createMock(org.apache.ambari.server.orm.entities.PrincipalEntity.class);
        EasyMock.expect(principalEntity.getPrincipalType()).andReturn(principalTypeEntity).atLeastOnce();
        org.apache.ambari.server.orm.entities.ViewEntity viewEntity = createMock(org.apache.ambari.server.orm.entities.ViewEntity.class);
        EasyMock.expect(viewEntity.getCommonName()).andReturn("TestView").atLeastOnce();
        EasyMock.expect(viewEntity.getVersion()).andReturn("1.2.3.4").atLeastOnce();
        org.apache.ambari.server.orm.entities.ViewInstanceEntity viewInstanceEntity = createMock(org.apache.ambari.server.orm.entities.ViewInstanceEntity.class);
        EasyMock.expect(viewInstanceEntity.getViewEntity()).andReturn(viewEntity).atLeastOnce();
        EasyMock.expect(viewInstanceEntity.getName()).andReturn("Test View").atLeastOnce();
        org.apache.ambari.server.orm.entities.ResourceTypeEntity resourceTypeEntity = createMock(org.apache.ambari.server.orm.entities.ResourceTypeEntity.class);
        EasyMock.expect(resourceTypeEntity.getName()).andReturn("TestView{1.2.3.4}").atLeastOnce();
        org.apache.ambari.server.orm.entities.ResourceEntity resourceEntity = createMock(org.apache.ambari.server.orm.entities.ResourceEntity.class);
        EasyMock.expect(resourceEntity.getId()).andReturn(1L).atLeastOnce();
        EasyMock.expect(resourceEntity.getResourceType()).andReturn(resourceTypeEntity).atLeastOnce();
        org.apache.ambari.server.orm.entities.PrivilegeEntity privilegeEntity = createMock(org.apache.ambari.server.orm.entities.PrivilegeEntity.class);
        EasyMock.expect(privilegeEntity.getId()).andReturn(1).atLeastOnce();
        EasyMock.expect(privilegeEntity.getPermission()).andReturn(permissionEntity).atLeastOnce();
        EasyMock.expect(privilegeEntity.getPrincipal()).andReturn(principalEntity).atLeastOnce();
        EasyMock.expect(privilegeEntity.getResource()).andReturn(resourceEntity).atLeastOnce();
        org.apache.ambari.server.orm.entities.UserEntity userEntity = createMock(org.apache.ambari.server.orm.entities.UserEntity.class);
        EasyMock.expect(userEntity.getUserName()).andReturn("jdoe").atLeastOnce();
        org.apache.ambari.server.orm.dao.ClusterDAO clusterDAO = createMock(org.apache.ambari.server.orm.dao.ClusterDAO.class);
        org.apache.ambari.server.orm.dao.GroupDAO groupDAO = createMock(org.apache.ambari.server.orm.dao.GroupDAO.class);
        org.apache.ambari.server.orm.dao.ViewInstanceDAO viewInstanceDAO = createMock(org.apache.ambari.server.orm.dao.ViewInstanceDAO.class);
        EasyMock.expect(viewInstanceDAO.findByResourceId(1L)).andReturn(viewInstanceEntity).atLeastOnce();
        org.apache.ambari.server.orm.dao.UserDAO userDAO = createMock(org.apache.ambari.server.orm.dao.UserDAO.class);
        EasyMock.expect(userDAO.findUserByPrincipal(EasyMock.anyObject(org.apache.ambari.server.orm.entities.PrincipalEntity.class))).andReturn(userEntity).anyTimes();
        org.apache.ambari.server.security.authorization.Users users = createNiceMock(org.apache.ambari.server.security.authorization.Users.class);
        replayAll();
        org.apache.ambari.server.controller.internal.UserPrivilegeResourceProvider.init(userDAO, clusterDAO, groupDAO, viewInstanceDAO, users);
        org.apache.ambari.server.controller.internal.UserPrivilegeResourceProvider provider = new org.apache.ambari.server.controller.internal.UserPrivilegeResourceProvider();
        org.apache.ambari.server.controller.UserPrivilegeResponse response = provider.getResponse(privilegeEntity, "jdoe");
        org.apache.ambari.server.controller.spi.Resource resource = provider.toResource(response, provider.getPropertyIds());
        junit.framework.Assert.assertEquals("Test View", resource.getPropertyValue(org.apache.ambari.server.controller.internal.ViewPrivilegeResourceProvider.INSTANCE_NAME));
        junit.framework.Assert.assertEquals("TestView", resource.getPropertyValue(org.apache.ambari.server.controller.internal.ViewPrivilegeResourceProvider.VIEW_NAME));
        junit.framework.Assert.assertEquals("1.2.3.4", resource.getPropertyValue(org.apache.ambari.server.controller.internal.ViewPrivilegeResourceProvider.VERSION));
        junit.framework.Assert.assertEquals(org.apache.ambari.server.security.authorization.ResourceType.VIEW.name(), resource.getPropertyValue(org.apache.ambari.server.controller.internal.UserPrivilegeResourceProvider.TYPE));
        verifyAll();
    }

    @org.junit.Test
    public void testToResource_SpecificVIEW_WithClusterInheritedPermission() throws java.lang.Exception {
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator("jdoe", 2L));
        com.google.inject.Injector injector = createInjector();
        final org.apache.ambari.server.controller.internal.UserPrivilegeResourceProvider resourceProvider = new org.apache.ambari.server.controller.internal.UserPrivilegeResourceProvider();
        final org.apache.ambari.server.orm.dao.UserDAO userDAO = injector.getInstance(org.apache.ambari.server.orm.dao.UserDAO.class);
        final org.apache.ambari.server.orm.dao.GroupDAO groupDAO = injector.getInstance(org.apache.ambari.server.orm.dao.GroupDAO.class);
        final org.apache.ambari.server.orm.dao.ClusterDAO clusterDAO = injector.getInstance(org.apache.ambari.server.orm.dao.ClusterDAO.class);
        final org.apache.ambari.server.orm.dao.ViewInstanceDAO viewInstanceDAO = injector.getInstance(org.apache.ambari.server.orm.dao.ViewInstanceDAO.class);
        final org.apache.ambari.server.orm.dao.PrivilegeDAO privilegeDAO = injector.getInstance(org.apache.ambari.server.orm.dao.PrivilegeDAO.class);
        final org.apache.ambari.server.orm.dao.MemberDAO memberDAO = injector.getInstance(org.apache.ambari.server.orm.dao.MemberDAO.class);
        final org.apache.ambari.server.orm.entities.PrincipalTypeEntity rolePrincipalTypeEntity = createMock(org.apache.ambari.server.orm.entities.PrincipalTypeEntity.class);
        EasyMock.expect(rolePrincipalTypeEntity.getName()).andReturn("ROLE").atLeastOnce();
        final org.apache.ambari.server.orm.entities.PrincipalEntity rolePrincipalEntity = createMock(org.apache.ambari.server.orm.entities.PrincipalEntity.class);
        EasyMock.expect(rolePrincipalEntity.getPrincipalType()).andReturn(rolePrincipalTypeEntity).atLeastOnce();
        final org.apache.ambari.server.orm.entities.PermissionEntity permissionEntity = createMock(org.apache.ambari.server.orm.entities.PermissionEntity.class);
        EasyMock.expect(permissionEntity.getPrincipal()).andReturn(rolePrincipalEntity).atLeastOnce();
        EasyMock.expect(permissionEntity.getPermissionName()).andReturn("CLUSTER.ADMINISTRATOR").atLeastOnce();
        EasyMock.expect(permissionEntity.getPermissionLabel()).andReturn("Cluster Administrator").atLeastOnce();
        final org.apache.ambari.server.orm.entities.PrincipalTypeEntity principalTypeEntity = createMock(org.apache.ambari.server.orm.entities.PrincipalTypeEntity.class);
        EasyMock.expect(principalTypeEntity.getName()).andReturn("USER").atLeastOnce();
        final org.apache.ambari.server.orm.entities.PrincipalEntity principalEntity = createMock(org.apache.ambari.server.orm.entities.PrincipalEntity.class);
        EasyMock.expect(principalEntity.getPrincipalType()).andReturn(principalTypeEntity).atLeastOnce();
        final org.apache.ambari.server.orm.entities.ViewEntity viewEntity = createMock(org.apache.ambari.server.orm.entities.ViewEntity.class);
        EasyMock.expect(viewEntity.getCommonName()).andReturn("TestView").atLeastOnce();
        EasyMock.expect(viewEntity.getVersion()).andReturn("1.2.3.4").atLeastOnce();
        final org.apache.ambari.server.orm.entities.ResourceTypeEntity resourceTypeEntity = createMock(org.apache.ambari.server.orm.entities.ResourceTypeEntity.class);
        EasyMock.expect(resourceTypeEntity.getName()).andReturn("TestView{1.2.3.4}").atLeastOnce();
        final org.apache.ambari.server.orm.entities.ResourceEntity resourceEntity = createMock(org.apache.ambari.server.orm.entities.ResourceEntity.class);
        EasyMock.expect(resourceEntity.getId()).andReturn(1L).anyTimes();
        EasyMock.expect(resourceEntity.getResourceType()).andReturn(resourceTypeEntity).anyTimes();
        final org.apache.ambari.server.orm.entities.ViewInstanceEntity viewInstanceEntity = createMock(org.apache.ambari.server.orm.entities.ViewInstanceEntity.class);
        EasyMock.expect(viewInstanceEntity.getViewEntity()).andReturn(viewEntity).atLeastOnce();
        EasyMock.expect(viewInstanceEntity.getName()).andReturn("Test View").atLeastOnce();
        final org.apache.ambari.server.orm.entities.PrivilegeEntity explicitPrivilegeEntity = createMock(org.apache.ambari.server.orm.entities.PrivilegeEntity.class);
        EasyMock.expect(explicitPrivilegeEntity.getId()).andReturn(1).atLeastOnce();
        EasyMock.expect(explicitPrivilegeEntity.getPermission()).andReturn(permissionEntity).atLeastOnce();
        EasyMock.expect(explicitPrivilegeEntity.getPrincipal()).andReturn(principalEntity).atLeastOnce();
        EasyMock.expect(explicitPrivilegeEntity.getResource()).andReturn(resourceEntity).atLeastOnce();
        final org.apache.ambari.server.orm.entities.PrivilegeEntity implicitPrivilegeEntity = createMock(org.apache.ambari.server.orm.entities.PrivilegeEntity.class);
        EasyMock.expect(implicitPrivilegeEntity.getId()).andReturn(2).atLeastOnce();
        EasyMock.expect(implicitPrivilegeEntity.getPermission()).andReturn(permissionEntity).atLeastOnce();
        EasyMock.expect(implicitPrivilegeEntity.getPrincipal()).andReturn(rolePrincipalEntity).atLeastOnce();
        EasyMock.expect(implicitPrivilegeEntity.getResource()).andReturn(resourceEntity).atLeastOnce();
        org.apache.ambari.server.orm.entities.UserEntity userEntity = createMock(org.apache.ambari.server.orm.entities.UserEntity.class);
        EasyMock.expect(userEntity.getUserName()).andReturn("jdoe").atLeastOnce();
        EasyMock.expect(userEntity.getPrincipal()).andReturn(principalEntity).atLeastOnce();
        EasyMock.expect(viewInstanceDAO.findByResourceId(1L)).andReturn(viewInstanceEntity).atLeastOnce();
        EasyMock.expect(userDAO.findUserByName("jdoe")).andReturn(userEntity).anyTimes();
        EasyMock.expect(userDAO.findUserByPrincipal(EasyMock.anyObject(org.apache.ambari.server.orm.entities.PrincipalEntity.class))).andReturn(userEntity).anyTimes();
        EasyMock.expect(userDAO.findAll()).andReturn(java.util.Collections.emptyList()).anyTimes();
        final org.apache.ambari.server.security.authorization.Users users = injector.getInstance(org.apache.ambari.server.security.authorization.Users.class);
        java.util.List<org.apache.ambari.server.orm.entities.PrincipalEntity> rolePrincipals = new java.util.LinkedList<>();
        rolePrincipals.add(rolePrincipalEntity);
        java.util.List<org.apache.ambari.server.orm.entities.PrincipalEntity> userPrincipals = new java.util.LinkedList<>();
        userPrincipals.add(principalEntity);
        EasyMock.expect(privilegeDAO.findAllByPrincipal(userPrincipals)).andReturn(java.util.Collections.singletonList(explicitPrivilegeEntity)).once();
        EasyMock.expect(privilegeDAO.findAllByPrincipal(rolePrincipals)).andReturn(java.util.Collections.singletonList(implicitPrivilegeEntity)).once();
        EasyMock.expect(memberDAO.findAllMembersByUser(userEntity)).andReturn(java.util.Collections.emptyList()).atLeastOnce();
        replayAll();
        final java.util.Set<java.lang.String> propertyIds = new java.util.HashSet<>();
        propertyIds.add(org.apache.ambari.server.controller.internal.UserPrivilegeResourceProvider.USER_NAME);
        final org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.UserPrivilegeResourceProvider.USER_NAME).equals("jdoe").toPredicate();
        org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator("jdoe", 2L);
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(propertyIds);
        org.apache.ambari.server.controller.internal.UserPrivilegeResourceProvider.init(userDAO, clusterDAO, groupDAO, viewInstanceDAO, users);
        org.apache.ambari.server.controller.internal.UserPrivilegeResourceProvider provider = new org.apache.ambari.server.controller.internal.UserPrivilegeResourceProvider();
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = provider.getResources(request, predicate);
        junit.framework.Assert.assertEquals(1, resources.size());
        for (org.apache.ambari.server.controller.spi.Resource resource : resources) {
            java.lang.String userName = ((java.lang.String) (resource.getPropertyValue(org.apache.ambari.server.controller.internal.UserPrivilegeResourceProvider.USER_NAME)));
            junit.framework.Assert.assertEquals("jdoe", userName);
        }
        verifyAll();
    }

    private void getResourcesTest(org.springframework.security.core.Authentication authentication, java.lang.String requestedUsername) throws java.lang.Exception {
        com.google.inject.Injector injector = createInjector();
        final org.apache.ambari.server.controller.internal.UserPrivilegeResourceProvider resourceProvider = new org.apache.ambari.server.controller.internal.UserPrivilegeResourceProvider();
        final org.apache.ambari.server.orm.dao.UserDAO userDAO = injector.getInstance(org.apache.ambari.server.orm.dao.UserDAO.class);
        final org.apache.ambari.server.orm.dao.GroupDAO groupDAO = injector.getInstance(org.apache.ambari.server.orm.dao.GroupDAO.class);
        final org.apache.ambari.server.orm.dao.ClusterDAO clusterDAO = injector.getInstance(org.apache.ambari.server.orm.dao.ClusterDAO.class);
        final org.apache.ambari.server.orm.dao.ViewInstanceDAO viewInstanceDAO = injector.getInstance(org.apache.ambari.server.orm.dao.ViewInstanceDAO.class);
        final org.apache.ambari.server.orm.dao.PrivilegeDAO privilegeDAO = injector.getInstance(org.apache.ambari.server.orm.dao.PrivilegeDAO.class);
        final org.apache.ambari.server.orm.dao.MemberDAO memberDAO = injector.getInstance(org.apache.ambari.server.orm.dao.MemberDAO.class);
        final org.apache.ambari.server.orm.entities.UserEntity userEntity = createNiceMock(org.apache.ambari.server.orm.entities.UserEntity.class);
        final org.apache.ambari.server.orm.entities.PrincipalEntity principalEntity = createNiceMock(org.apache.ambari.server.orm.entities.PrincipalEntity.class);
        final org.apache.ambari.server.orm.entities.PrivilegeEntity privilegeEntity = createNiceMock(org.apache.ambari.server.orm.entities.PrivilegeEntity.class);
        final org.apache.ambari.server.orm.entities.PermissionEntity permissionEntity = createNiceMock(org.apache.ambari.server.orm.entities.PermissionEntity.class);
        final org.apache.ambari.server.orm.entities.PrincipalTypeEntity principalTypeEntity = createNiceMock(org.apache.ambari.server.orm.entities.PrincipalTypeEntity.class);
        final org.apache.ambari.server.orm.entities.ResourceEntity resourceEntity = createNiceMock(org.apache.ambari.server.orm.entities.ResourceEntity.class);
        final org.apache.ambari.server.orm.entities.ResourceTypeEntity resourceTypeEntity = createNiceMock(org.apache.ambari.server.orm.entities.ResourceTypeEntity.class);
        final org.apache.ambari.server.security.authorization.Users users = injector.getInstance(org.apache.ambari.server.security.authorization.Users.class);
        java.util.List<org.apache.ambari.server.orm.entities.PrincipalEntity> userPrincipals = new java.util.LinkedList<>();
        userPrincipals.add(principalEntity);
        EasyMock.expect(privilegeDAO.findAllByPrincipal(userPrincipals)).andReturn(java.util.Collections.singletonList(privilegeEntity)).atLeastOnce();
        EasyMock.expect(memberDAO.findAllMembersByUser(userEntity)).andReturn(java.util.Collections.emptyList()).atLeastOnce();
        EasyMock.expect(userDAO.findAll()).andReturn(java.util.Collections.emptyList()).anyTimes();
        EasyMock.expect(userDAO.findUserByName(requestedUsername)).andReturn(userEntity).anyTimes();
        EasyMock.expect(userEntity.getPrincipal()).andReturn(principalEntity).anyTimes();
        EasyMock.expect(userEntity.getMemberEntities()).andReturn(java.util.Collections.emptySet()).anyTimes();
        EasyMock.expect(privilegeEntity.getPermission()).andReturn(permissionEntity).anyTimes();
        EasyMock.expect(privilegeEntity.getPrincipal()).andReturn(principalEntity).anyTimes();
        EasyMock.expect(principalEntity.getPrincipalType()).andReturn(principalTypeEntity).anyTimes();
        EasyMock.expect(principalTypeEntity.getName()).andReturn(org.apache.ambari.server.orm.entities.PrincipalTypeEntity.USER_PRINCIPAL_TYPE_NAME).anyTimes();
        EasyMock.expect(principalEntity.getPrivileges()).andReturn(new java.util.HashSet<org.apache.ambari.server.orm.entities.PrivilegeEntity>() {
            {
                add(privilegeEntity);
            }
        }).anyTimes();
        EasyMock.expect(userDAO.findUserByPrincipal(EasyMock.anyObject(org.apache.ambari.server.orm.entities.PrincipalEntity.class))).andReturn(userEntity).anyTimes();
        EasyMock.expect(userEntity.getUserName()).andReturn(requestedUsername).anyTimes();
        EasyMock.expect(privilegeEntity.getResource()).andReturn(resourceEntity).anyTimes();
        EasyMock.expect(resourceEntity.getResourceType()).andReturn(resourceTypeEntity).anyTimes();
        EasyMock.expect(resourceTypeEntity.getName()).andReturn(org.apache.ambari.server.security.authorization.ResourceType.AMBARI.name());
        EasyMock.expect(viewInstanceDAO.findAll()).andReturn(new java.util.ArrayList<>()).anyTimes();
        replayAll();
        org.apache.ambari.server.controller.internal.UserPrivilegeResourceProvider.init(userDAO, clusterDAO, groupDAO, viewInstanceDAO, users);
        final java.util.Set<java.lang.String> propertyIds = new java.util.HashSet<>();
        propertyIds.add(org.apache.ambari.server.controller.internal.UserPrivilegeResourceProvider.USER_NAME);
        final org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.UserPrivilegeResourceProvider.USER_NAME).equals(requestedUsername).toPredicate();
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(propertyIds);
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = resourceProvider.getResources(request, predicate);
        junit.framework.Assert.assertEquals(1, resources.size());
        for (org.apache.ambari.server.controller.spi.Resource resource : resources) {
            java.lang.String userName = ((java.lang.String) (resource.getPropertyValue(org.apache.ambari.server.controller.internal.UserPrivilegeResourceProvider.USER_NAME)));
            junit.framework.Assert.assertEquals(requestedUsername, userName);
        }
        verifyAll();
    }

    private com.google.inject.Injector createInjector() {
        return com.google.inject.Guice.createInjector(new com.google.inject.AbstractModule() {
            @java.lang.Override
            protected void configure() {
                bind(org.apache.ambari.server.state.stack.OsFamily.class).toInstance(createNiceMock(org.apache.ambari.server.state.stack.OsFamily.class));
                bind(javax.persistence.EntityManager.class).toInstance(createNiceMock(javax.persistence.EntityManager.class));
                bind(org.apache.ambari.server.orm.DBAccessor.class).toInstance(createNiceMock(org.apache.ambari.server.orm.DBAccessor.class));
                bind(org.springframework.security.crypto.password.PasswordEncoder.class).toInstance(createNiceMock(org.springframework.security.crypto.password.PasswordEncoder.class));
                bind(org.apache.ambari.server.hooks.HookService.class).toInstance(createMock(org.apache.ambari.server.hooks.HookService.class));
                bind(org.apache.ambari.server.hooks.HookContextFactory.class).toInstance(createMock(org.apache.ambari.server.hooks.HookContextFactory.class));
                bind(org.apache.ambari.server.orm.dao.UserDAO.class).toInstance(createNiceMock(org.apache.ambari.server.orm.dao.UserDAO.class));
                bind(org.apache.ambari.server.orm.dao.GroupDAO.class).toInstance(createNiceMock(org.apache.ambari.server.orm.dao.GroupDAO.class));
                bind(org.apache.ambari.server.orm.dao.ClusterDAO.class).toInstance(createNiceMock(org.apache.ambari.server.orm.dao.ClusterDAO.class));
                bind(org.apache.ambari.server.orm.dao.ViewInstanceDAO.class).toInstance(createNiceMock(org.apache.ambari.server.orm.dao.ViewInstanceDAO.class));
                bind(org.apache.ambari.server.orm.dao.PrivilegeDAO.class).toInstance(createMock(org.apache.ambari.server.orm.dao.PrivilegeDAO.class));
                bind(org.apache.ambari.server.orm.dao.MemberDAO.class).toInstance(createMock(org.apache.ambari.server.orm.dao.MemberDAO.class));
                bind(new com.google.inject.TypeLiteral<org.apache.ambari.server.security.encryption.Encryptor<org.apache.ambari.server.configuration.AmbariServerConfiguration>>() {}).annotatedWith(com.google.inject.name.Names.named("AmbariServerConfigurationEncryptor")).toInstance(org.apache.ambari.server.security.encryption.Encryptor.NONE);
                bind(org.apache.ambari.server.ldap.service.AmbariLdapConfigurationProvider.class).toInstance(createMock(org.apache.ambari.server.ldap.service.AmbariLdapConfigurationProvider.class));
            }
        });
    }
}