package org.apache.ambari.server.controller.internal;
import javax.persistence.EntityManager;
import org.easymock.EasyMockSupport;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.expect;
public class GroupPrivilegeResourceProviderTest extends org.easymock.EasyMockSupport {
    @org.junit.Test(expected = org.apache.ambari.server.controller.spi.SystemException.class)
    public void testCreateResources() throws java.lang.Exception {
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator("user1", 2L));
        org.apache.ambari.server.controller.internal.GroupPrivilegeResourceProvider resourceProvider = new org.apache.ambari.server.controller.internal.GroupPrivilegeResourceProvider();
        resourceProvider.createResources(createNiceMock(org.apache.ambari.server.controller.spi.Request.class));
    }

    @org.junit.Test
    public void testGetResources_Administrator() throws java.lang.Exception {
        getResourcesTest(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator("admin"), "Group1");
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testGetResources_NonAdministrator() throws java.lang.Exception {
        getResourcesTest(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator("user1", 2L), "Group1");
    }

    @org.junit.Test(expected = org.apache.ambari.server.controller.spi.SystemException.class)
    public void testUpdateResources() throws java.lang.Exception {
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator("user1", 2L));
        org.apache.ambari.server.controller.internal.GroupPrivilegeResourceProvider resourceProvider = new org.apache.ambari.server.controller.internal.GroupPrivilegeResourceProvider();
        resourceProvider.updateResources(createNiceMock(org.apache.ambari.server.controller.spi.Request.class), createNiceMock(org.apache.ambari.server.controller.spi.Predicate.class));
    }

    @org.junit.Test(expected = org.apache.ambari.server.controller.spi.SystemException.class)
    public void testDeleteResources() throws java.lang.Exception {
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator("user1", 2L));
        org.apache.ambari.server.controller.internal.GroupPrivilegeResourceProvider resourceProvider = new org.apache.ambari.server.controller.internal.GroupPrivilegeResourceProvider();
        resourceProvider.deleteResources(new org.apache.ambari.server.controller.internal.RequestImpl(null, null, null, null), createNiceMock(org.apache.ambari.server.controller.spi.Predicate.class));
    }

    @org.junit.Test
    public void testToResource_AMBARI() {
        org.apache.ambari.server.orm.entities.PermissionEntity permissionEntity = createMock(org.apache.ambari.server.orm.entities.PermissionEntity.class);
        EasyMock.expect(permissionEntity.getPermissionName()).andReturn("ADMINISTRATOR").atLeastOnce();
        EasyMock.expect(permissionEntity.getPermissionLabel()).andReturn("Ambari Administrator").atLeastOnce();
        org.apache.ambari.server.orm.entities.PrincipalTypeEntity principalTypeEntity = createMock(org.apache.ambari.server.orm.entities.PrincipalTypeEntity.class);
        EasyMock.expect(principalTypeEntity.getName()).andReturn("GROUP").atLeastOnce();
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
        org.apache.ambari.server.orm.entities.GroupEntity groupEntity = createMock(org.apache.ambari.server.orm.entities.GroupEntity.class);
        EasyMock.expect(groupEntity.getGroupName()).andReturn("group1").atLeastOnce();
        org.apache.ambari.server.orm.dao.GroupDAO groupDAO = createMock(org.apache.ambari.server.orm.dao.GroupDAO.class);
        EasyMock.expect(groupDAO.findGroupByPrincipal(EasyMock.anyObject(org.apache.ambari.server.orm.entities.PrincipalEntity.class))).andReturn(groupEntity).anyTimes();
        org.apache.ambari.server.orm.dao.ClusterDAO clusterDAO = createMock(org.apache.ambari.server.orm.dao.ClusterDAO.class);
        org.apache.ambari.server.orm.dao.ViewInstanceDAO viewInstanceDAO = createMock(org.apache.ambari.server.orm.dao.ViewInstanceDAO.class);
        org.apache.ambari.server.security.authorization.Users users = createNiceMock(org.apache.ambari.server.security.authorization.Users.class);
        replayAll();
        org.apache.ambari.server.controller.internal.GroupPrivilegeResourceProvider.init(clusterDAO, groupDAO, viewInstanceDAO, users);
        org.apache.ambari.server.controller.internal.GroupPrivilegeResourceProvider provider = new org.apache.ambari.server.controller.internal.GroupPrivilegeResourceProvider();
        org.apache.ambari.server.controller.GroupPrivilegeResponse response = provider.getResponse(privilegeEntity, "group1");
        org.apache.ambari.server.controller.spi.Resource resource = provider.toResource(response, provider.getPropertyIds());
        junit.framework.Assert.assertEquals(org.apache.ambari.server.security.authorization.ResourceType.AMBARI.name(), resource.getPropertyValue(org.apache.ambari.server.controller.internal.GroupPrivilegeResourceProvider.TYPE));
        verifyAll();
    }

    @org.junit.Test
    public void testToResource_CLUSTER() {
        org.apache.ambari.server.orm.entities.PermissionEntity permissionEntity = createMock(org.apache.ambari.server.orm.entities.PermissionEntity.class);
        EasyMock.expect(permissionEntity.getPermissionName()).andReturn("CLUSTER.ADMINISTRATOR").atLeastOnce();
        EasyMock.expect(permissionEntity.getPermissionLabel()).andReturn("Cluster Administrator").atLeastOnce();
        org.apache.ambari.server.orm.entities.PrincipalTypeEntity principalTypeEntity = createMock(org.apache.ambari.server.orm.entities.PrincipalTypeEntity.class);
        EasyMock.expect(principalTypeEntity.getName()).andReturn("GROUP").atLeastOnce();
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
        org.apache.ambari.server.orm.entities.GroupEntity groupEntity = createMock(org.apache.ambari.server.orm.entities.GroupEntity.class);
        EasyMock.expect(groupEntity.getGroupName()).andReturn("group1").atLeastOnce();
        org.apache.ambari.server.orm.dao.ClusterDAO clusterDAO = createMock(org.apache.ambari.server.orm.dao.ClusterDAO.class);
        EasyMock.expect(clusterDAO.findByResourceId(1L)).andReturn(clusterEntity).atLeastOnce();
        org.apache.ambari.server.orm.dao.ViewInstanceDAO viewInstanceDAO = createMock(org.apache.ambari.server.orm.dao.ViewInstanceDAO.class);
        org.apache.ambari.server.orm.dao.GroupDAO groupDAO = createMock(org.apache.ambari.server.orm.dao.GroupDAO.class);
        EasyMock.expect(groupDAO.findGroupByPrincipal(EasyMock.anyObject(org.apache.ambari.server.orm.entities.PrincipalEntity.class))).andReturn(groupEntity).anyTimes();
        org.apache.ambari.server.security.authorization.Users users = createNiceMock(org.apache.ambari.server.security.authorization.Users.class);
        replayAll();
        org.apache.ambari.server.controller.internal.GroupPrivilegeResourceProvider.init(clusterDAO, groupDAO, viewInstanceDAO, users);
        org.apache.ambari.server.controller.internal.GroupPrivilegeResourceProvider provider = new org.apache.ambari.server.controller.internal.GroupPrivilegeResourceProvider();
        org.apache.ambari.server.controller.GroupPrivilegeResponse response = provider.getResponse(privilegeEntity, "group1");
        org.apache.ambari.server.controller.spi.Resource resource = provider.toResource(response, provider.getPropertyIds());
        junit.framework.Assert.assertEquals("TestCluster", resource.getPropertyValue(org.apache.ambari.server.controller.internal.ClusterPrivilegeResourceProvider.CLUSTER_NAME));
        junit.framework.Assert.assertEquals(org.apache.ambari.server.security.authorization.ResourceType.CLUSTER.name(), resource.getPropertyValue(org.apache.ambari.server.controller.internal.GroupPrivilegeResourceProvider.TYPE));
        verifyAll();
    }

    @org.junit.Test
    public void testToResource_VIEW() {
        org.apache.ambari.server.orm.entities.PermissionEntity permissionEntity = createMock(org.apache.ambari.server.orm.entities.PermissionEntity.class);
        EasyMock.expect(permissionEntity.getPermissionName()).andReturn("CLUSTER.ADMINISTRATOR").atLeastOnce();
        EasyMock.expect(permissionEntity.getPermissionLabel()).andReturn("Cluster Administrator").atLeastOnce();
        org.apache.ambari.server.orm.entities.PrincipalTypeEntity principalTypeEntity = createMock(org.apache.ambari.server.orm.entities.PrincipalTypeEntity.class);
        EasyMock.expect(principalTypeEntity.getName()).andReturn("GROUP").atLeastOnce();
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
        org.apache.ambari.server.orm.entities.GroupEntity groupEntity = createMock(org.apache.ambari.server.orm.entities.GroupEntity.class);
        EasyMock.expect(groupEntity.getGroupName()).andReturn("group1").atLeastOnce();
        org.apache.ambari.server.orm.dao.ClusterDAO clusterDAO = createMock(org.apache.ambari.server.orm.dao.ClusterDAO.class);
        org.apache.ambari.server.orm.dao.ViewInstanceDAO viewInstanceDAO = createMock(org.apache.ambari.server.orm.dao.ViewInstanceDAO.class);
        EasyMock.expect(viewInstanceDAO.findByResourceId(1L)).andReturn(viewInstanceEntity).atLeastOnce();
        org.apache.ambari.server.orm.dao.GroupDAO groupDAO = createMock(org.apache.ambari.server.orm.dao.GroupDAO.class);
        EasyMock.expect(groupDAO.findGroupByPrincipal(EasyMock.anyObject(org.apache.ambari.server.orm.entities.PrincipalEntity.class))).andReturn(groupEntity).anyTimes();
        org.apache.ambari.server.security.authorization.Users users = createNiceMock(org.apache.ambari.server.security.authorization.Users.class);
        replayAll();
        org.apache.ambari.server.controller.internal.GroupPrivilegeResourceProvider.init(clusterDAO, groupDAO, viewInstanceDAO, users);
        org.apache.ambari.server.controller.internal.GroupPrivilegeResourceProvider provider = new org.apache.ambari.server.controller.internal.GroupPrivilegeResourceProvider();
        org.apache.ambari.server.controller.GroupPrivilegeResponse response = provider.getResponse(privilegeEntity, "group1");
        org.apache.ambari.server.controller.spi.Resource resource = provider.toResource(response, provider.getPropertyIds());
        junit.framework.Assert.assertEquals("Test View", resource.getPropertyValue(org.apache.ambari.server.controller.internal.ViewPrivilegeResourceProvider.INSTANCE_NAME));
        junit.framework.Assert.assertEquals("TestView", resource.getPropertyValue(org.apache.ambari.server.controller.internal.ViewPrivilegeResourceProvider.VIEW_NAME));
        junit.framework.Assert.assertEquals("1.2.3.4", resource.getPropertyValue(org.apache.ambari.server.controller.internal.ViewPrivilegeResourceProvider.VERSION));
        junit.framework.Assert.assertEquals(org.apache.ambari.server.security.authorization.ResourceType.VIEW.name(), resource.getPropertyValue(org.apache.ambari.server.controller.internal.GroupPrivilegeResourceProvider.TYPE));
        verifyAll();
    }

    @org.junit.Test
    public void testToResource_SpecificVIEW() {
        org.apache.ambari.server.orm.entities.PermissionEntity permissionEntity = createMock(org.apache.ambari.server.orm.entities.PermissionEntity.class);
        EasyMock.expect(permissionEntity.getPermissionName()).andReturn("CLUSTER.ADMINISTRATOR").atLeastOnce();
        EasyMock.expect(permissionEntity.getPermissionLabel()).andReturn("Cluster Administrator").atLeastOnce();
        org.apache.ambari.server.orm.entities.PrincipalTypeEntity principalTypeEntity = createMock(org.apache.ambari.server.orm.entities.PrincipalTypeEntity.class);
        EasyMock.expect(principalTypeEntity.getName()).andReturn("GROUP").atLeastOnce();
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
        org.apache.ambari.server.orm.entities.GroupEntity groupEntity = createMock(org.apache.ambari.server.orm.entities.GroupEntity.class);
        EasyMock.expect(groupEntity.getGroupName()).andReturn("group1").atLeastOnce();
        org.apache.ambari.server.orm.dao.ClusterDAO clusterDAO = createMock(org.apache.ambari.server.orm.dao.ClusterDAO.class);
        org.apache.ambari.server.orm.dao.ViewInstanceDAO viewInstanceDAO = createMock(org.apache.ambari.server.orm.dao.ViewInstanceDAO.class);
        EasyMock.expect(viewInstanceDAO.findByResourceId(1L)).andReturn(viewInstanceEntity).atLeastOnce();
        org.apache.ambari.server.orm.dao.GroupDAO groupDAO = createMock(org.apache.ambari.server.orm.dao.GroupDAO.class);
        EasyMock.expect(groupDAO.findGroupByPrincipal(EasyMock.anyObject(org.apache.ambari.server.orm.entities.PrincipalEntity.class))).andReturn(groupEntity).anyTimes();
        org.apache.ambari.server.security.authorization.Users users = createNiceMock(org.apache.ambari.server.security.authorization.Users.class);
        replayAll();
        org.apache.ambari.server.controller.internal.GroupPrivilegeResourceProvider.init(clusterDAO, groupDAO, viewInstanceDAO, users);
        org.apache.ambari.server.controller.internal.GroupPrivilegeResourceProvider provider = new org.apache.ambari.server.controller.internal.GroupPrivilegeResourceProvider();
        org.apache.ambari.server.controller.GroupPrivilegeResponse response = provider.getResponse(privilegeEntity, "group1");
        org.apache.ambari.server.controller.spi.Resource resource = provider.toResource(response, provider.getPropertyIds());
        junit.framework.Assert.assertEquals("Test View", resource.getPropertyValue(org.apache.ambari.server.controller.internal.ViewPrivilegeResourceProvider.INSTANCE_NAME));
        junit.framework.Assert.assertEquals("TestView", resource.getPropertyValue(org.apache.ambari.server.controller.internal.ViewPrivilegeResourceProvider.VIEW_NAME));
        junit.framework.Assert.assertEquals("1.2.3.4", resource.getPropertyValue(org.apache.ambari.server.controller.internal.ViewPrivilegeResourceProvider.VERSION));
        junit.framework.Assert.assertEquals(org.apache.ambari.server.security.authorization.ResourceType.VIEW.name(), resource.getPropertyValue(org.apache.ambari.server.controller.internal.GroupPrivilegeResourceProvider.TYPE));
        verifyAll();
    }

    private void getResourcesTest(org.springframework.security.core.Authentication authentication, java.lang.String requestedGroupName) throws java.lang.Exception {
        final org.apache.ambari.server.controller.internal.GroupPrivilegeResourceProvider resourceProvider = new org.apache.ambari.server.controller.internal.GroupPrivilegeResourceProvider();
        final org.apache.ambari.server.orm.dao.GroupDAO groupDAO = createNiceMock(org.apache.ambari.server.orm.dao.GroupDAO.class);
        final org.apache.ambari.server.orm.dao.ClusterDAO clusterDAO = createNiceMock(org.apache.ambari.server.orm.dao.ClusterDAO.class);
        final org.apache.ambari.server.orm.dao.ViewInstanceDAO viewInstanceDAO = createNiceMock(org.apache.ambari.server.orm.dao.ViewInstanceDAO.class);
        final org.apache.ambari.server.orm.entities.GroupEntity groupEntity = createNiceMock(org.apache.ambari.server.orm.entities.GroupEntity.class);
        final org.apache.ambari.server.orm.entities.PrincipalEntity principalEntity = createNiceMock(org.apache.ambari.server.orm.entities.PrincipalEntity.class);
        final org.apache.ambari.server.orm.entities.PrivilegeEntity privilegeEntity = createNiceMock(org.apache.ambari.server.orm.entities.PrivilegeEntity.class);
        final org.apache.ambari.server.orm.entities.PermissionEntity permissionEntity = createNiceMock(org.apache.ambari.server.orm.entities.PermissionEntity.class);
        final org.apache.ambari.server.orm.entities.PrincipalTypeEntity principalTypeEntity = createNiceMock(org.apache.ambari.server.orm.entities.PrincipalTypeEntity.class);
        final org.apache.ambari.server.orm.entities.ResourceEntity resourceEntity = createNiceMock(org.apache.ambari.server.orm.entities.ResourceEntity.class);
        final org.apache.ambari.server.orm.entities.ResourceTypeEntity resourceTypeEntity = createNiceMock(org.apache.ambari.server.orm.entities.ResourceTypeEntity.class);
        final org.apache.ambari.server.orm.dao.PrivilegeDAO privilegeDAO = createMock(org.apache.ambari.server.orm.dao.PrivilegeDAO.class);
        final com.google.inject.Injector injector = com.google.inject.Guice.createInjector(new com.google.inject.AbstractModule() {
            @java.lang.Override
            protected void configure() {
                bind(org.apache.ambari.server.state.stack.OsFamily.class).toInstance(createNiceMock(org.apache.ambari.server.state.stack.OsFamily.class));
                bind(javax.persistence.EntityManager.class).toInstance(createNiceMock(javax.persistence.EntityManager.class));
                bind(org.apache.ambari.server.orm.DBAccessor.class).toInstance(createNiceMock(org.apache.ambari.server.orm.DBAccessor.class));
                bind(org.springframework.security.crypto.password.PasswordEncoder.class).toInstance(createNiceMock(org.springframework.security.crypto.password.PasswordEncoder.class));
                bind(org.apache.ambari.server.hooks.HookService.class).toInstance(createMock(org.apache.ambari.server.hooks.HookService.class));
                bind(org.apache.ambari.server.hooks.HookContextFactory.class).toInstance(createMock(org.apache.ambari.server.hooks.HookContextFactory.class));
                bind(new com.google.inject.TypeLiteral<org.apache.ambari.server.security.encryption.Encryptor<org.apache.ambari.server.configuration.AmbariServerConfiguration>>() {}).annotatedWith(com.google.inject.name.Names.named("AmbariServerConfigurationEncryptor")).toInstance(org.apache.ambari.server.security.encryption.Encryptor.NONE);
                bind(org.apache.ambari.server.ldap.service.AmbariLdapConfigurationProvider.class).toInstance(createMock(org.apache.ambari.server.ldap.service.AmbariLdapConfigurationProvider.class));
                bind(org.apache.ambari.server.orm.dao.GroupDAO.class).toInstance(groupDAO);
                bind(org.apache.ambari.server.orm.dao.ClusterDAO.class).toInstance(clusterDAO);
                bind(org.apache.ambari.server.orm.dao.ViewInstanceDAO.class).toInstance(viewInstanceDAO);
                bind(org.apache.ambari.server.orm.entities.GroupEntity.class).toInstance(groupEntity);
                bind(org.apache.ambari.server.orm.entities.PrincipalEntity.class).toInstance(principalEntity);
                bind(org.apache.ambari.server.orm.entities.PrivilegeEntity.class).toInstance(privilegeEntity);
                bind(org.apache.ambari.server.orm.entities.PermissionEntity.class).toInstance(permissionEntity);
                bind(org.apache.ambari.server.orm.entities.PrincipalTypeEntity.class).toInstance(principalTypeEntity);
                bind(org.apache.ambari.server.orm.entities.ResourceEntity.class).toInstance(resourceEntity);
                bind(org.apache.ambari.server.orm.entities.ResourceTypeEntity.class).toInstance(resourceTypeEntity);
                bind(org.apache.ambari.server.orm.dao.PrivilegeDAO.class).toInstance(privilegeDAO);
            }
        });
        final org.apache.ambari.server.security.authorization.Users users = injector.getInstance(org.apache.ambari.server.security.authorization.Users.class);
        java.util.List<org.apache.ambari.server.orm.entities.PrincipalEntity> groupPrincipals = new java.util.LinkedList<>();
        groupPrincipals.add(principalEntity);
        EasyMock.expect(privilegeDAO.findAllByPrincipal(groupPrincipals)).andReturn(java.util.Collections.singletonList(privilegeEntity)).once();
        EasyMock.expect(groupDAO.findGroupByName(requestedGroupName)).andReturn(groupEntity).atLeastOnce();
        EasyMock.expect(groupEntity.getPrincipal()).andReturn(principalEntity).atLeastOnce();
        EasyMock.expect(privilegeEntity.getPermission()).andReturn(permissionEntity).atLeastOnce();
        EasyMock.expect(privilegeEntity.getPrincipal()).andReturn(principalEntity).atLeastOnce();
        EasyMock.expect(principalEntity.getPrincipalType()).andReturn(principalTypeEntity).atLeastOnce();
        EasyMock.expect(principalTypeEntity.getName()).andReturn(org.apache.ambari.server.orm.entities.PrincipalTypeEntity.GROUP_PRINCIPAL_TYPE_NAME).atLeastOnce();
        EasyMock.expect(groupDAO.findGroupByPrincipal(EasyMock.anyObject(org.apache.ambari.server.orm.entities.PrincipalEntity.class))).andReturn(groupEntity).atLeastOnce();
        EasyMock.expect(groupEntity.getGroupName()).andReturn(requestedGroupName).atLeastOnce();
        EasyMock.expect(privilegeEntity.getResource()).andReturn(resourceEntity).atLeastOnce();
        EasyMock.expect(resourceEntity.getResourceType()).andReturn(resourceTypeEntity).atLeastOnce();
        EasyMock.expect(resourceTypeEntity.getName()).andReturn(org.apache.ambari.server.security.authorization.ResourceType.AMBARI.name());
        replayAll();
        org.apache.ambari.server.controller.internal.GroupPrivilegeResourceProvider.init(clusterDAO, groupDAO, viewInstanceDAO, users);
        final java.util.Set<java.lang.String> propertyIds = new java.util.HashSet<>();
        propertyIds.add(org.apache.ambari.server.controller.internal.GroupPrivilegeResourceProvider.GROUP_NAME);
        final org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.GroupPrivilegeResourceProvider.GROUP_NAME).equals(requestedGroupName).toPredicate();
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(propertyIds);
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = resourceProvider.getResources(request, predicate);
        junit.framework.Assert.assertEquals(1, resources.size());
        for (org.apache.ambari.server.controller.spi.Resource resource : resources) {
            java.lang.String groupName = ((java.lang.String) (resource.getPropertyValue(org.apache.ambari.server.controller.internal.GroupPrivilegeResourceProvider.GROUP_NAME)));
            junit.framework.Assert.assertEquals(requestedGroupName, groupName);
        }
        verifyAll();
    }
}