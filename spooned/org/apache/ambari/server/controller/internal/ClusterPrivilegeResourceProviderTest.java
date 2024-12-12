package org.apache.ambari.server.controller.internal;
import javax.persistence.EntityManager;
import org.easymock.EasyMockSupport;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
public class ClusterPrivilegeResourceProviderTest extends org.easymock.EasyMockSupport {
    @org.junit.Before
    public void resetGlobalMocks() {
        resetAll();
    }

    @org.junit.After
    public void clearAuthentication() {
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(null);
    }

    @org.junit.Test
    public void testCreateResources_Administrator() throws java.lang.Exception {
        createResourcesTest(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator("admin"));
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testCreateResources_NonAdministrator() throws java.lang.Exception {
        createResourcesTest(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator("User1", 2L));
    }

    @org.junit.Test
    public void testGetResources_Administrator() throws java.lang.Exception {
        getResourcesTest(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator("admin"));
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testGetResources_NonAdministrator() throws java.lang.Exception {
        getResourcesTest(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator("User1", 2L));
    }

    @org.junit.Test
    public void testGetResource_Administrator_Self() throws java.lang.Exception {
        getResourceTest(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator("admin"), "admin");
    }

    @org.junit.Test
    public void testGetResource_Administrator_Other() throws java.lang.Exception {
        getResourceTest(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator("admin"), "User1");
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testGetResource_NonAdministrator_Self() throws java.lang.Exception {
        getResourceTest(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator("User1", 2L), "User1");
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testGetResource_NonAdministrator_Other() throws java.lang.Exception {
        getResourceTest(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator("User1", 2L), "User10");
    }

    @org.junit.Test
    public void testUpdateResources_Administrator_Self() throws java.lang.Exception {
        updateResourcesTest(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator("admin"), "admin");
    }

    @org.junit.Test
    public void testUpdateResources_Administrator_Other() throws java.lang.Exception {
        updateResourcesTest(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator("admin"), "User1");
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testUpdateResources_NonAdministrator_Self() throws java.lang.Exception {
        updateResourcesTest(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator("User1", 2L), "User1");
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testUpdateResources_NonAdministrator_Other() throws java.lang.Exception {
        updateResourcesTest(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator("User1", 2L), "User10");
    }

    @org.junit.Test
    public void testDeleteResources_Administrator() throws java.lang.Exception {
        deleteResourcesTest(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator("admin"));
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testDeleteResources_NonAdministrator() throws java.lang.Exception {
        deleteResourcesTest(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator("User1", 2L));
    }

    private void createResourcesTest(org.springframework.security.core.Authentication authentication) throws java.lang.Exception {
        com.google.inject.Injector injector = createInjector();
        org.apache.ambari.server.orm.entities.PrincipalEntity principalEntity = createMockPrincipalEntity(2L, createMockPrincipalTypeEntity("USER"));
        org.apache.ambari.server.orm.entities.ResourceTypeEntity clusterResourceTypeEntity = createMockResourceTypeEntity(org.apache.ambari.server.security.authorization.ResourceType.CLUSTER);
        org.apache.ambari.server.orm.entities.ResourceEntity clusterResourceEntity = createMockResourceEntity(1L, clusterResourceTypeEntity);
        org.apache.ambari.server.orm.entities.PermissionEntity permissionEntity = createMockPermissionEntity("CLUSTER.OPERATOR", "Cluster Operator", clusterResourceTypeEntity);
        org.apache.ambari.server.orm.entities.PrivilegeEntity privilegeEntity = createMockPrivilegeEntity(2, clusterResourceEntity, principalEntity, permissionEntity);
        org.apache.ambari.server.orm.entities.ClusterEntity clusterEntity = createMockClusterEntity("c1", clusterResourceEntity);
        org.apache.ambari.server.orm.entities.UserEntity userEntity = createMockUserEntity(principalEntity, "User1");
        java.util.Set<org.apache.ambari.server.orm.entities.PrivilegeEntity> privilegeEntities = new java.util.HashSet<>();
        privilegeEntities.add(privilegeEntity);
        EasyMock.expect(principalEntity.getPrivileges()).andReturn(privilegeEntities).atLeastOnce();
        org.apache.ambari.server.orm.dao.PrivilegeDAO privilegeDAO = injector.getInstance(org.apache.ambari.server.orm.dao.PrivilegeDAO.class);
        EasyMock.expect(privilegeDAO.exists(EasyMock.anyObject(org.apache.ambari.server.orm.entities.PrivilegeEntity.class))).andReturn(false).atLeastOnce();
        privilegeDAO.create(EasyMock.anyObject(org.apache.ambari.server.orm.entities.PrivilegeEntity.class));
        EasyMock.expectLastCall().once();
        org.apache.ambari.server.orm.dao.UserDAO userDAO = injector.getInstance(org.apache.ambari.server.orm.dao.UserDAO.class);
        EasyMock.expect(userDAO.findUserByName("User1")).andReturn(userEntity).atLeastOnce();
        org.apache.ambari.server.orm.dao.PrincipalDAO principalDAO = injector.getInstance(org.apache.ambari.server.orm.dao.PrincipalDAO.class);
        EasyMock.expect(principalDAO.findById(2L)).andReturn(principalEntity).atLeastOnce();
        EasyMock.expect(principalDAO.merge(principalEntity)).andReturn(principalEntity).once();
        org.apache.ambari.server.orm.dao.ClusterDAO clusterDAO = injector.getInstance(org.apache.ambari.server.orm.dao.ClusterDAO.class);
        EasyMock.expect(clusterDAO.findByName("c1")).andReturn(clusterEntity).atLeastOnce();
        org.apache.ambari.server.orm.dao.ResourceDAO resourceDAO = injector.getInstance(org.apache.ambari.server.orm.dao.ResourceDAO.class);
        EasyMock.expect(resourceDAO.findById(1L)).andReturn(clusterResourceEntity).atLeastOnce();
        org.apache.ambari.server.orm.dao.PermissionDAO permissionDAO = injector.getInstance(org.apache.ambari.server.orm.dao.PermissionDAO.class);
        EasyMock.expect(permissionDAO.findPermissionByNameAndType("CLUSTER.OPERATOR", clusterResourceTypeEntity)).andReturn(permissionEntity).atLeastOnce();
        replayAll();
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        java.util.Map<java.lang.String, java.lang.Object> properties = new java.util.LinkedHashMap<>();
        properties.put(org.apache.ambari.server.controller.internal.PrivilegeResourceProvider.PERMISSION_NAME, "CLUSTER.OPERATOR");
        properties.put(org.apache.ambari.server.controller.internal.PrivilegeResourceProvider.PRINCIPAL_NAME, "User1");
        properties.put(org.apache.ambari.server.controller.internal.PrivilegeResourceProvider.PRINCIPAL_TYPE, "USER");
        properties.put(org.apache.ambari.server.controller.internal.ClusterPrivilegeResourceProvider.CLUSTER_NAME, "c1");
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getUpdateRequest(properties, null);
        org.apache.ambari.server.controller.spi.ResourceProvider provider = getResourceProvider(injector);
        provider.createResources(request);
        verifyAll();
    }

    private void getResourcesTest(org.springframework.security.core.Authentication authentication) throws java.lang.Exception {
        com.google.inject.Injector injector = createInjector();
        org.apache.ambari.server.orm.entities.ResourceTypeEntity resourceTypeEntity = createMockResourceTypeEntity(org.apache.ambari.server.security.authorization.ResourceType.CLUSTER);
        org.apache.ambari.server.orm.entities.ResourceEntity resourceEntity = createMockResourceEntity(20L, resourceTypeEntity);
        org.apache.ambari.server.orm.entities.PrincipalTypeEntity principalTypeEntity = createMockPrincipalTypeEntity("USER");
        org.apache.ambari.server.orm.entities.PrincipalEntity principalEntity = createMockPrincipalEntity(20L, principalTypeEntity);
        org.apache.ambari.server.orm.entities.PermissionEntity permissionEntity = createMockPermissionEntity("CLUSTER.ADMINISTRATOR", "Cluster Administrator", resourceTypeEntity);
        org.apache.ambari.server.orm.entities.PrivilegeEntity privilegeEntity = createMockPrivilegeEntity(1, resourceEntity, principalEntity, permissionEntity);
        org.apache.ambari.server.orm.entities.ClusterEntity clusterEntity = createMockClusterEntity("c1", resourceEntity);
        org.apache.ambari.server.orm.entities.UserEntity userEntity = createMockUserEntity(principalEntity, "joe");
        java.util.List<org.apache.ambari.server.orm.entities.PrincipalEntity> principalEntities = new java.util.LinkedList<>();
        principalEntities.add(principalEntity);
        java.util.List<org.apache.ambari.server.orm.entities.UserEntity> userEntities = new java.util.LinkedList<>();
        userEntities.add(userEntity);
        java.util.List<org.apache.ambari.server.orm.entities.PrivilegeEntity> privilegeEntities = new java.util.LinkedList<>();
        privilegeEntities.add(privilegeEntity);
        org.apache.ambari.server.orm.dao.PrivilegeDAO privilegeDAO = injector.getInstance(org.apache.ambari.server.orm.dao.PrivilegeDAO.class);
        EasyMock.expect(privilegeDAO.findAll()).andReturn(privilegeEntities);
        java.util.List<org.apache.ambari.server.orm.entities.ClusterEntity> clusterEntities = new java.util.LinkedList<>();
        clusterEntities.add(clusterEntity);
        org.apache.ambari.server.orm.dao.ClusterDAO clusterDAO = injector.getInstance(org.apache.ambari.server.orm.dao.ClusterDAO.class);
        EasyMock.expect(clusterDAO.findAll()).andReturn(clusterEntities);
        org.apache.ambari.server.orm.dao.UserDAO userDAO = injector.getInstance(org.apache.ambari.server.orm.dao.UserDAO.class);
        EasyMock.expect(userDAO.findUsersByPrincipal(principalEntities)).andReturn(userEntities);
        replayAll();
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        org.apache.ambari.server.controller.spi.ResourceProvider provider = getResourceProvider(injector);
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = provider.getResources(org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(), null);
        org.junit.Assert.assertEquals(1, resources.size());
        org.apache.ambari.server.controller.spi.Resource resource = resources.iterator().next();
        org.junit.Assert.assertEquals("CLUSTER.ADMINISTRATOR", resource.getPropertyValue(org.apache.ambari.server.controller.internal.AmbariPrivilegeResourceProvider.PERMISSION_NAME));
        org.junit.Assert.assertEquals("Cluster Administrator", resource.getPropertyValue(org.apache.ambari.server.controller.internal.AmbariPrivilegeResourceProvider.PERMISSION_LABEL));
        org.junit.Assert.assertEquals("joe", resource.getPropertyValue(org.apache.ambari.server.controller.internal.AmbariPrivilegeResourceProvider.PRINCIPAL_NAME));
        org.junit.Assert.assertEquals("USER", resource.getPropertyValue(org.apache.ambari.server.controller.internal.AmbariPrivilegeResourceProvider.PRINCIPAL_TYPE));
        verifyAll();
    }

    private void getResourceTest(org.springframework.security.core.Authentication authentication, java.lang.String requestedUsername) throws java.lang.Exception {
        com.google.inject.Injector injector = createInjector();
        org.apache.ambari.server.orm.entities.ResourceTypeEntity resourceTypeEntity = createMockResourceTypeEntity(org.apache.ambari.server.security.authorization.ResourceType.CLUSTER);
        org.apache.ambari.server.orm.entities.ResourceEntity resourceEntity = createMockResourceEntity(20L, resourceTypeEntity);
        org.apache.ambari.server.orm.entities.PrincipalTypeEntity principalTypeEntity = createMockPrincipalTypeEntity("USER");
        org.apache.ambari.server.orm.entities.PrincipalEntity principalEntity = createMockPrincipalEntity(20L, principalTypeEntity);
        org.apache.ambari.server.orm.entities.PermissionEntity permissionEntity = createMockPermissionEntity("CLUSTER.ADMINISTRATOR", "Cluster Administrator", resourceTypeEntity);
        org.apache.ambari.server.orm.entities.PrivilegeEntity privilegeEntity = createMockPrivilegeEntity(1, resourceEntity, principalEntity, permissionEntity);
        org.apache.ambari.server.orm.entities.ClusterEntity clusterEntity = createMockClusterEntity("c1", resourceEntity);
        org.apache.ambari.server.orm.entities.UserEntity userEntity = createMockUserEntity(principalEntity, requestedUsername);
        java.util.List<org.apache.ambari.server.orm.entities.PrincipalEntity> principalEntities = new java.util.LinkedList<>();
        principalEntities.add(principalEntity);
        java.util.List<org.apache.ambari.server.orm.entities.UserEntity> userEntities = new java.util.LinkedList<>();
        userEntities.add(userEntity);
        java.util.List<org.apache.ambari.server.orm.entities.PrivilegeEntity> privilegeEntities = new java.util.LinkedList<>();
        privilegeEntities.add(privilegeEntity);
        org.apache.ambari.server.orm.dao.PrivilegeDAO privilegeDAO = injector.getInstance(org.apache.ambari.server.orm.dao.PrivilegeDAO.class);
        EasyMock.expect(privilegeDAO.findAll()).andReturn(privilegeEntities);
        java.util.List<org.apache.ambari.server.orm.entities.ClusterEntity> clusterEntities = new java.util.LinkedList<>();
        clusterEntities.add(clusterEntity);
        org.apache.ambari.server.orm.dao.ClusterDAO clusterDAO = injector.getInstance(org.apache.ambari.server.orm.dao.ClusterDAO.class);
        EasyMock.expect(clusterDAO.findAll()).andReturn(clusterEntities);
        org.apache.ambari.server.orm.dao.UserDAO userDAO = injector.getInstance(org.apache.ambari.server.orm.dao.UserDAO.class);
        EasyMock.expect(userDAO.findUsersByPrincipal(principalEntities)).andReturn(userEntities);
        replayAll();
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        org.apache.ambari.server.controller.spi.ResourceProvider provider = getResourceProvider(injector);
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = provider.getResources(org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(), null);
        org.junit.Assert.assertEquals(1, resources.size());
        org.apache.ambari.server.controller.spi.Resource resource = resources.iterator().next();
        org.junit.Assert.assertEquals("CLUSTER.ADMINISTRATOR", resource.getPropertyValue(org.apache.ambari.server.controller.internal.AmbariPrivilegeResourceProvider.PERMISSION_NAME));
        org.junit.Assert.assertEquals("Cluster Administrator", resource.getPropertyValue(org.apache.ambari.server.controller.internal.AmbariPrivilegeResourceProvider.PERMISSION_LABEL));
        org.junit.Assert.assertEquals(requestedUsername, resource.getPropertyValue(org.apache.ambari.server.controller.internal.AmbariPrivilegeResourceProvider.PRINCIPAL_NAME));
        org.junit.Assert.assertEquals("USER", resource.getPropertyValue(org.apache.ambari.server.controller.internal.AmbariPrivilegeResourceProvider.PRINCIPAL_TYPE));
        verifyAll();
    }

    private void updateResourcesTest(org.springframework.security.core.Authentication authentication, java.lang.String requestedUsername) throws java.lang.Exception {
        com.google.inject.Injector injector = createInjector();
        org.apache.ambari.server.orm.entities.ResourceTypeEntity resourceTypeEntity = createMockResourceTypeEntity(org.apache.ambari.server.security.authorization.ResourceType.CLUSTER);
        org.apache.ambari.server.orm.entities.PermissionEntity permissionEntity = createMockPermissionEntity("CLUSTER.ADMINISTRATOR", "Cluster Administrator", resourceTypeEntity);
        org.apache.ambari.server.orm.dao.PermissionDAO permissionDAO = injector.getInstance(org.apache.ambari.server.orm.dao.PermissionDAO.class);
        EasyMock.expect(permissionDAO.findPermissionByNameAndType("CLUSTER.OPERATOR", resourceTypeEntity)).andReturn(permissionEntity);
        org.apache.ambari.server.orm.entities.ResourceEntity resourceEntity = createMockResourceEntity(2L, resourceTypeEntity);
        org.apache.ambari.server.orm.entities.ClusterEntity clusterEntity = createMockClusterEntity("c1", resourceEntity);
        java.util.List<org.apache.ambari.server.orm.entities.ClusterEntity> clusterEntities = new java.util.LinkedList<>();
        clusterEntities.add(clusterEntity);
        org.apache.ambari.server.orm.entities.PrincipalTypeEntity principalTypeEntity = createMockPrincipalTypeEntity("USER");
        org.apache.ambari.server.orm.entities.PrincipalEntity principalEntity = createMockPrincipalEntity(2L, principalTypeEntity);
        org.apache.ambari.server.orm.entities.UserEntity userEntity = createMockUserEntity(principalEntity, requestedUsername);
        org.apache.ambari.server.orm.entities.PrivilegeEntity privilegeEntity = createMockPrivilegeEntity(1, resourceEntity, principalEntity, permissionEntity);
        java.util.List<org.apache.ambari.server.orm.entities.PrivilegeEntity> privilegeEntities = new java.util.ArrayList<>();
        privilegeEntities.add(privilegeEntity);
        org.apache.ambari.server.orm.dao.UserDAO userDAO = injector.getInstance(org.apache.ambari.server.orm.dao.UserDAO.class);
        EasyMock.expect(userDAO.findUserByName(requestedUsername)).andReturn(userEntity).atLeastOnce();
        org.apache.ambari.server.orm.dao.ClusterDAO clusterDAO = injector.getInstance(org.apache.ambari.server.orm.dao.ClusterDAO.class);
        EasyMock.expect(clusterDAO.findAll()).andReturn(clusterEntities);
        org.apache.ambari.server.orm.dao.ResourceDAO resourceDAO = injector.getInstance(org.apache.ambari.server.orm.dao.ResourceDAO.class);
        EasyMock.expect(resourceDAO.findById(2L)).andReturn(resourceEntity).atLeastOnce();
        org.apache.ambari.server.orm.dao.PrincipalDAO principalDAO = injector.getInstance(org.apache.ambari.server.orm.dao.PrincipalDAO.class);
        EasyMock.expect(principalDAO.findById(2L)).andReturn(principalEntity).atLeastOnce();
        org.apache.ambari.server.orm.dao.PrivilegeDAO privilegeDAO = injector.getInstance(org.apache.ambari.server.orm.dao.PrivilegeDAO.class);
        EasyMock.expect(privilegeDAO.findByResourceId(2L)).andReturn(privilegeEntities).atLeastOnce();
        replayAll();
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        java.util.Map<java.lang.String, java.lang.Object> properties = new java.util.LinkedHashMap<>();
        properties.put(org.apache.ambari.server.controller.internal.PrivilegeResourceProvider.PERMISSION_NAME, "CLUSTER.OPERATOR");
        properties.put(org.apache.ambari.server.controller.internal.PrivilegeResourceProvider.PRINCIPAL_NAME, requestedUsername);
        properties.put(org.apache.ambari.server.controller.internal.PrivilegeResourceProvider.PRINCIPAL_TYPE, "USER");
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getUpdateRequest(properties, null);
        org.apache.ambari.server.controller.spi.ResourceProvider provider = getResourceProvider(injector);
        provider.updateResources(request, null);
        verifyAll();
    }

    private void deleteResourcesTest(org.springframework.security.core.Authentication authentication) throws java.lang.Exception {
        com.google.inject.Injector injector = createInjector();
        org.apache.ambari.server.orm.entities.PrincipalEntity principalEntity1 = createMockPrincipalEntity(1L, createMockPrincipalTypeEntity("USER"));
        org.apache.ambari.server.orm.entities.ResourceTypeEntity clusterResourceTypeEntity = createMockResourceTypeEntity(org.apache.ambari.server.security.authorization.ResourceType.CLUSTER);
        org.apache.ambari.server.orm.entities.ResourceEntity clusterResourceEntity = createMockResourceEntity(1L, clusterResourceTypeEntity);
        org.apache.ambari.server.orm.entities.PermissionEntity permissionEntity = createMockPermissionEntity("CLUSTER.OPERATOR", "Cluster Operator", clusterResourceTypeEntity);
        org.apache.ambari.server.orm.entities.PrivilegeEntity privilegeEntity1 = createMockPrivilegeEntity(1, clusterResourceEntity, principalEntity1, permissionEntity);
        java.util.Set<org.apache.ambari.server.orm.entities.PrivilegeEntity> privilege1Entities = new java.util.HashSet<>();
        privilege1Entities.add(privilegeEntity1);
        EasyMock.expect(principalEntity1.getPrivileges()).andReturn(privilege1Entities).atLeastOnce();
        org.apache.ambari.server.orm.dao.PrivilegeDAO privilegeDAO = injector.getInstance(org.apache.ambari.server.orm.dao.PrivilegeDAO.class);
        EasyMock.expect(privilegeDAO.findById(1)).andReturn(privilegeEntity1).atLeastOnce();
        privilegeDAO.remove(privilegeEntity1);
        EasyMock.expectLastCall().atLeastOnce();
        org.apache.ambari.server.orm.dao.PrincipalDAO principalDAO = injector.getInstance(org.apache.ambari.server.orm.dao.PrincipalDAO.class);
        EasyMock.expect(principalDAO.merge(principalEntity1)).andReturn(principalEntity1).atLeastOnce();
        replayAll();
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        org.apache.ambari.server.controller.spi.ResourceProvider provider = getResourceProvider(injector);
        provider.deleteResources(new org.apache.ambari.server.controller.internal.RequestImpl(null, null, null, null), createPredicate(1L));
        verifyAll();
    }

    private org.apache.ambari.server.orm.entities.ResourceEntity createMockResourceEntity(java.lang.Long id, org.apache.ambari.server.orm.entities.ResourceTypeEntity resourceTypeEntity) {
        org.apache.ambari.server.orm.entities.ResourceEntity resourceEntity = createMock(org.apache.ambari.server.orm.entities.ResourceEntity.class);
        EasyMock.expect(resourceEntity.getId()).andReturn(id).anyTimes();
        EasyMock.expect(resourceEntity.getResourceType()).andReturn(resourceTypeEntity).anyTimes();
        return resourceEntity;
    }

    private org.apache.ambari.server.orm.entities.ResourceTypeEntity createMockResourceTypeEntity(org.apache.ambari.server.security.authorization.ResourceType resourceType) {
        org.apache.ambari.server.orm.entities.ResourceTypeEntity resourceTypeEntity = createMock(org.apache.ambari.server.orm.entities.ResourceTypeEntity.class);
        EasyMock.expect(resourceTypeEntity.getId()).andReturn(resourceType.getId()).anyTimes();
        EasyMock.expect(resourceTypeEntity.getName()).andReturn(resourceType.name()).anyTimes();
        return resourceTypeEntity;
    }

    private org.apache.ambari.server.orm.entities.PermissionEntity createMockPermissionEntity(java.lang.String name, java.lang.String label, org.apache.ambari.server.orm.entities.ResourceTypeEntity resourceTypeEntity) {
        org.apache.ambari.server.orm.entities.PermissionEntity permissionEntity = createMock(org.apache.ambari.server.orm.entities.PermissionEntity.class);
        EasyMock.expect(permissionEntity.getPermissionName()).andReturn(name).anyTimes();
        EasyMock.expect(permissionEntity.getPermissionLabel()).andReturn(label).anyTimes();
        EasyMock.expect(permissionEntity.getResourceType()).andReturn(resourceTypeEntity).anyTimes();
        return permissionEntity;
    }

    private org.apache.ambari.server.orm.entities.PrincipalTypeEntity createMockPrincipalTypeEntity(java.lang.String typeName) {
        org.apache.ambari.server.orm.entities.PrincipalTypeEntity principalTypeEntity = createMock(org.apache.ambari.server.orm.entities.PrincipalTypeEntity.class);
        EasyMock.expect(principalTypeEntity.getName()).andReturn(typeName).anyTimes();
        return principalTypeEntity;
    }

    private org.apache.ambari.server.orm.entities.PrincipalEntity createMockPrincipalEntity(java.lang.Long id, org.apache.ambari.server.orm.entities.PrincipalTypeEntity principalTypeEntity) {
        org.apache.ambari.server.orm.entities.PrincipalEntity principalEntity = createMock(org.apache.ambari.server.orm.entities.PrincipalEntity.class);
        EasyMock.expect(principalEntity.getId()).andReturn(id).anyTimes();
        EasyMock.expect(principalEntity.getPrincipalType()).andReturn(principalTypeEntity).anyTimes();
        return principalEntity;
    }

    private org.apache.ambari.server.orm.entities.PrivilegeEntity createMockPrivilegeEntity(java.lang.Integer id, org.apache.ambari.server.orm.entities.ResourceEntity resourceEntity, org.apache.ambari.server.orm.entities.PrincipalEntity principalEntity, org.apache.ambari.server.orm.entities.PermissionEntity permissionEntity) {
        org.apache.ambari.server.orm.entities.PrivilegeEntity privilegeEntity = createMock(org.apache.ambari.server.orm.entities.PrivilegeEntity.class);
        EasyMock.expect(privilegeEntity.getId()).andReturn(id).anyTimes();
        EasyMock.expect(privilegeEntity.getResource()).andReturn(resourceEntity).anyTimes();
        EasyMock.expect(privilegeEntity.getPrincipal()).andReturn(principalEntity).anyTimes();
        EasyMock.expect(privilegeEntity.getPermission()).andReturn(permissionEntity).anyTimes();
        return privilegeEntity;
    }

    private org.apache.ambari.server.orm.entities.ClusterEntity createMockClusterEntity(java.lang.String clusterName, org.apache.ambari.server.orm.entities.ResourceEntity resourceEntity) {
        org.apache.ambari.server.orm.entities.ClusterEntity clusterEntity = createMock(org.apache.ambari.server.orm.entities.ClusterEntity.class);
        EasyMock.expect(clusterEntity.getClusterName()).andReturn(clusterName).anyTimes();
        EasyMock.expect(clusterEntity.getResource()).andReturn(resourceEntity).anyTimes();
        return clusterEntity;
    }

    private org.apache.ambari.server.orm.entities.UserEntity createMockUserEntity(org.apache.ambari.server.orm.entities.PrincipalEntity principalEntity, java.lang.String username) {
        org.apache.ambari.server.orm.entities.UserEntity userEntity = createMock(org.apache.ambari.server.orm.entities.UserEntity.class);
        EasyMock.expect(userEntity.getPrincipal()).andReturn(principalEntity).anyTimes();
        EasyMock.expect(userEntity.getUserName()).andReturn(username).anyTimes();
        return userEntity;
    }

    private org.apache.ambari.server.controller.spi.Predicate createPredicate(java.lang.Long id) {
        return new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.ClusterPrivilegeResourceProvider.PRIVILEGE_ID).equals(id).toPredicate();
    }

    private org.apache.ambari.server.controller.spi.ResourceProvider getResourceProvider(com.google.inject.Injector injector) {
        org.apache.ambari.server.controller.internal.PrivilegeResourceProvider.init(injector.getInstance(org.apache.ambari.server.orm.dao.PrivilegeDAO.class), injector.getInstance(org.apache.ambari.server.orm.dao.UserDAO.class), injector.getInstance(org.apache.ambari.server.orm.dao.GroupDAO.class), injector.getInstance(org.apache.ambari.server.orm.dao.PrincipalDAO.class), injector.getInstance(org.apache.ambari.server.orm.dao.PermissionDAO.class), injector.getInstance(org.apache.ambari.server.orm.dao.ResourceDAO.class));
        org.apache.ambari.server.controller.internal.ClusterPrivilegeResourceProvider.init(injector.getInstance(org.apache.ambari.server.orm.dao.ClusterDAO.class));
        return new org.apache.ambari.server.controller.internal.ClusterPrivilegeResourceProvider();
    }

    private com.google.inject.Injector createInjector() throws java.lang.Exception {
        return com.google.inject.Guice.createInjector(new com.google.inject.AbstractModule() {
            @java.lang.Override
            protected void configure() {
                bind(javax.persistence.EntityManager.class).toInstance(createNiceMock(javax.persistence.EntityManager.class));
                bind(org.apache.ambari.server.orm.DBAccessor.class).toInstance(createNiceMock(org.apache.ambari.server.orm.DBAccessor.class));
                bind(org.apache.ambari.server.security.SecurityHelper.class).toInstance(createNiceMock(org.apache.ambari.server.security.SecurityHelper.class));
                bind(org.apache.ambari.server.orm.dao.ViewInstanceDAO.class).toInstance(createNiceMock(org.apache.ambari.server.orm.dao.ViewInstanceDAO.class));
                bind(org.apache.ambari.server.view.ViewInstanceHandlerList.class).toInstance(createNiceMock(org.apache.ambari.server.view.ViewInstanceHandlerList.class));
                bind(org.apache.ambari.server.orm.dao.MemberDAO.class).toInstance(createNiceMock(org.apache.ambari.server.orm.dao.MemberDAO.class));
                bind(org.apache.ambari.server.orm.dao.PrivilegeDAO.class).toInstance(createMock(org.apache.ambari.server.orm.dao.PrivilegeDAO.class));
                bind(org.apache.ambari.server.orm.dao.PrincipalDAO.class).toInstance(createMock(org.apache.ambari.server.orm.dao.PrincipalDAO.class));
                bind(org.apache.ambari.server.orm.dao.PermissionDAO.class).toInstance(createMock(org.apache.ambari.server.orm.dao.PermissionDAO.class));
                bind(org.apache.ambari.server.orm.dao.UserDAO.class).toInstance(createMock(org.apache.ambari.server.orm.dao.UserDAO.class));
                bind(org.apache.ambari.server.orm.dao.GroupDAO.class).toInstance(createMock(org.apache.ambari.server.orm.dao.GroupDAO.class));
                bind(org.apache.ambari.server.orm.dao.ResourceDAO.class).toInstance(createMock(org.apache.ambari.server.orm.dao.ResourceDAO.class));
                bind(org.apache.ambari.server.orm.dao.ClusterDAO.class).toInstance(createMock(org.apache.ambari.server.orm.dao.ClusterDAO.class));
            }
        });
    }
}