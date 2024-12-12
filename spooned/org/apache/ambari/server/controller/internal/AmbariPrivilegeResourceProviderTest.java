package org.apache.ambari.server.controller.internal;
import javax.persistence.EntityManager;
import org.easymock.EasyMock;
import org.easymock.EasyMockSupport;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
public class AmbariPrivilegeResourceProviderTest extends org.easymock.EasyMockSupport {
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

    @org.junit.Test
    public void testGetResources_allTypes() throws java.lang.Exception {
        com.google.inject.Injector injector = createInjector();
        org.apache.ambari.server.orm.entities.PrivilegeEntity ambariPrivilegeEntity = createNiceMock(org.apache.ambari.server.orm.entities.PrivilegeEntity.class);
        org.apache.ambari.server.orm.entities.ResourceEntity ambariResourceEntity = createNiceMock(org.apache.ambari.server.orm.entities.ResourceEntity.class);
        org.apache.ambari.server.orm.entities.ResourceTypeEntity ambariResourceTypeEntity = createNiceMock(org.apache.ambari.server.orm.entities.ResourceTypeEntity.class);
        org.apache.ambari.server.orm.entities.UserEntity ambariUserEntity = createNiceMock(org.apache.ambari.server.orm.entities.UserEntity.class);
        org.apache.ambari.server.orm.entities.PrincipalEntity ambariPrincipalEntity = createNiceMock(org.apache.ambari.server.orm.entities.PrincipalEntity.class);
        org.apache.ambari.server.orm.entities.PrincipalTypeEntity ambariPrincipalTypeEntity = createNiceMock(org.apache.ambari.server.orm.entities.PrincipalTypeEntity.class);
        org.apache.ambari.server.orm.entities.PermissionEntity ambariPermissionEntity = createNiceMock(org.apache.ambari.server.orm.entities.PermissionEntity.class);
        EasyMock.expect(ambariPrivilegeEntity.getResource()).andReturn(ambariResourceEntity).anyTimes();
        EasyMock.expect(ambariPrivilegeEntity.getId()).andReturn(31).anyTimes();
        EasyMock.expect(ambariPrivilegeEntity.getPrincipal()).andReturn(ambariPrincipalEntity).anyTimes();
        EasyMock.expect(ambariPrivilegeEntity.getPermission()).andReturn(ambariPermissionEntity).anyTimes();
        EasyMock.expect(ambariResourceEntity.getResourceType()).andReturn(ambariResourceTypeEntity).anyTimes();
        EasyMock.expect(ambariResourceTypeEntity.getId()).andReturn(org.apache.ambari.server.security.authorization.ResourceType.AMBARI.getId()).anyTimes();
        EasyMock.expect(ambariResourceTypeEntity.getName()).andReturn(org.apache.ambari.server.security.authorization.ResourceType.AMBARI.name()).anyTimes();
        EasyMock.expect(ambariPrincipalEntity.getId()).andReturn(1L).anyTimes();
        EasyMock.expect(ambariUserEntity.getPrincipal()).andReturn(ambariPrincipalEntity).anyTimes();
        EasyMock.expect(ambariUserEntity.getUserName()).andReturn("joe").anyTimes();
        EasyMock.expect(ambariPermissionEntity.getPermissionName()).andReturn("AMBARI.ADMINISTRATOR").anyTimes();
        EasyMock.expect(ambariPermissionEntity.getPermissionLabel()).andReturn("Ambari Administrator").anyTimes();
        EasyMock.expect(ambariPrincipalEntity.getPrincipalType()).andReturn(ambariPrincipalTypeEntity).anyTimes();
        EasyMock.expect(ambariPrincipalTypeEntity.getName()).andReturn("USER").anyTimes();
        org.apache.ambari.server.orm.entities.PrivilegeEntity viewPrivilegeEntity = createNiceMock(org.apache.ambari.server.orm.entities.PrivilegeEntity.class);
        org.apache.ambari.server.orm.entities.ResourceEntity viewResourceEntity = createNiceMock(org.apache.ambari.server.orm.entities.ResourceEntity.class);
        org.apache.ambari.server.orm.entities.ViewEntity viewEntity = createNiceMock(org.apache.ambari.server.orm.entities.ViewEntity.class);
        org.apache.ambari.server.orm.entities.ViewInstanceEntity viewInstanceEntity = createNiceMock(org.apache.ambari.server.orm.entities.ViewInstanceEntity.class);
        org.apache.ambari.server.orm.entities.ResourceTypeEntity viewResourceTypeEntity = createNiceMock(org.apache.ambari.server.orm.entities.ResourceTypeEntity.class);
        org.apache.ambari.server.orm.entities.UserEntity viewUserEntity = createNiceMock(org.apache.ambari.server.orm.entities.UserEntity.class);
        org.apache.ambari.server.orm.entities.PrincipalEntity viewPrincipalEntity = createNiceMock(org.apache.ambari.server.orm.entities.PrincipalEntity.class);
        org.apache.ambari.server.orm.entities.PrincipalTypeEntity viewPrincipalTypeEntity = createNiceMock(org.apache.ambari.server.orm.entities.PrincipalTypeEntity.class);
        org.apache.ambari.server.orm.entities.PermissionEntity viewPermissionEntity = createNiceMock(org.apache.ambari.server.orm.entities.PermissionEntity.class);
        EasyMock.expect(viewPrivilegeEntity.getResource()).andReturn(viewResourceEntity).anyTimes();
        EasyMock.expect(viewPrivilegeEntity.getPrincipal()).andReturn(viewPrincipalEntity).anyTimes();
        EasyMock.expect(viewPrivilegeEntity.getPermission()).andReturn(viewPermissionEntity).anyTimes();
        EasyMock.expect(viewPrivilegeEntity.getId()).andReturn(33).anyTimes();
        EasyMock.expect(viewResourceEntity.getResourceType()).andReturn(viewResourceTypeEntity).anyTimes();
        EasyMock.expect(viewResourceTypeEntity.getId()).andReturn(org.apache.ambari.server.security.authorization.ResourceType.VIEW.getId()).anyTimes();
        EasyMock.expect(viewResourceTypeEntity.getName()).andReturn(org.apache.ambari.server.security.authorization.ResourceType.VIEW.name()).anyTimes();
        EasyMock.expect(viewPrincipalEntity.getId()).andReturn(5L).anyTimes();
        EasyMock.expect(viewEntity.getInstances()).andReturn(java.util.Arrays.asList(viewInstanceEntity)).anyTimes();
        EasyMock.expect(viewInstanceEntity.getViewEntity()).andReturn(viewEntity).anyTimes();
        EasyMock.expect(viewEntity.getCommonName()).andReturn("view").anyTimes();
        EasyMock.expect(viewEntity.getVersion()).andReturn("1.0.1").anyTimes();
        EasyMock.expect(viewEntity.isDeployed()).andReturn(true).anyTimes();
        EasyMock.expect(viewInstanceEntity.getName()).andReturn("inst1").anyTimes();
        EasyMock.expect(viewInstanceEntity.getResource()).andReturn(viewResourceEntity).anyTimes();
        EasyMock.expect(viewUserEntity.getPrincipal()).andReturn(viewPrincipalEntity).anyTimes();
        EasyMock.expect(viewUserEntity.getUserName()).andReturn("bob").anyTimes();
        EasyMock.expect(viewPermissionEntity.getPermissionName()).andReturn("VIEW.USER").anyTimes();
        EasyMock.expect(viewPermissionEntity.getPermissionLabel()).andReturn("View User").anyTimes();
        EasyMock.expect(viewPrincipalEntity.getPrincipalType()).andReturn(viewPrincipalTypeEntity).anyTimes();
        EasyMock.expect(viewPrincipalTypeEntity.getName()).andReturn("USER").anyTimes();
        org.apache.ambari.server.orm.entities.PrivilegeEntity clusterPrivilegeEntity = createNiceMock(org.apache.ambari.server.orm.entities.PrivilegeEntity.class);
        org.apache.ambari.server.orm.entities.ResourceEntity clusterResourceEntity = createNiceMock(org.apache.ambari.server.orm.entities.ResourceEntity.class);
        org.apache.ambari.server.orm.entities.ResourceTypeEntity clusterResourceTypeEntity = createNiceMock(org.apache.ambari.server.orm.entities.ResourceTypeEntity.class);
        org.apache.ambari.server.orm.entities.UserEntity clusterUserEntity = createNiceMock(org.apache.ambari.server.orm.entities.UserEntity.class);
        org.apache.ambari.server.orm.entities.PrincipalEntity clusterPrincipalEntity = createNiceMock(org.apache.ambari.server.orm.entities.PrincipalEntity.class);
        org.apache.ambari.server.orm.entities.PrincipalTypeEntity clusterPrincipalTypeEntity = createNiceMock(org.apache.ambari.server.orm.entities.PrincipalTypeEntity.class);
        org.apache.ambari.server.orm.entities.PermissionEntity clusterPermissionEntity = createNiceMock(org.apache.ambari.server.orm.entities.PermissionEntity.class);
        org.apache.ambari.server.orm.entities.ClusterEntity clusterEntity = createNiceMock(org.apache.ambari.server.orm.entities.ClusterEntity.class);
        EasyMock.expect(clusterPrivilegeEntity.getResource()).andReturn(clusterResourceEntity).anyTimes();
        EasyMock.expect(clusterPrivilegeEntity.getPrincipal()).andReturn(clusterPrincipalEntity).anyTimes();
        EasyMock.expect(clusterPrivilegeEntity.getPermission()).andReturn(clusterPermissionEntity).anyTimes();
        EasyMock.expect(clusterPrivilegeEntity.getId()).andReturn(32).anyTimes();
        EasyMock.expect(clusterResourceEntity.getId()).andReturn(7L).anyTimes();
        EasyMock.expect(clusterResourceEntity.getResourceType()).andReturn(clusterResourceTypeEntity).anyTimes();
        EasyMock.expect(clusterResourceTypeEntity.getId()).andReturn(org.apache.ambari.server.security.authorization.ResourceType.CLUSTER.getId()).anyTimes();
        EasyMock.expect(clusterResourceTypeEntity.getName()).andReturn(org.apache.ambari.server.security.authorization.ResourceType.CLUSTER.name()).anyTimes();
        EasyMock.expect(clusterPrincipalEntity.getId()).andReturn(8L).anyTimes();
        EasyMock.expect(clusterUserEntity.getPrincipal()).andReturn(clusterPrincipalEntity).anyTimes();
        EasyMock.expect(clusterUserEntity.getUserName()).andReturn("jeff").anyTimes();
        EasyMock.expect(clusterPermissionEntity.getPermissionName()).andReturn("CLUSTER.USER").anyTimes();
        EasyMock.expect(clusterPermissionEntity.getPermissionLabel()).andReturn("Cluster User").anyTimes();
        EasyMock.expect(clusterPrincipalEntity.getPrincipalType()).andReturn(clusterPrincipalTypeEntity).anyTimes();
        EasyMock.expect(clusterPrincipalTypeEntity.getName()).andReturn("USER").anyTimes();
        EasyMock.expect(clusterEntity.getResource()).andReturn(clusterResourceEntity).anyTimes();
        EasyMock.expect(clusterEntity.getClusterName()).andReturn("cluster1").anyTimes();
        java.util.List<org.apache.ambari.server.orm.entities.UserEntity> userEntities = new java.util.LinkedList<>();
        userEntities.add(ambariUserEntity);
        userEntities.add(viewUserEntity);
        userEntities.add(clusterUserEntity);
        java.util.List<org.apache.ambari.server.orm.entities.PrivilegeEntity> privilegeEntities = new java.util.LinkedList<>();
        privilegeEntities.add(ambariPrivilegeEntity);
        privilegeEntities.add(viewPrivilegeEntity);
        privilegeEntities.add(clusterPrivilegeEntity);
        java.util.List<org.apache.ambari.server.orm.entities.ClusterEntity> clusterEntities = new java.util.LinkedList<>();
        clusterEntities.add(clusterEntity);
        org.apache.ambari.server.orm.dao.ClusterDAO clusterDAO = injector.getInstance(org.apache.ambari.server.orm.dao.ClusterDAO.class);
        EasyMock.expect(clusterDAO.findAll()).andReturn(clusterEntities).atLeastOnce();
        org.apache.ambari.server.orm.dao.PrivilegeDAO privilegeDAO = injector.getInstance(org.apache.ambari.server.orm.dao.PrivilegeDAO.class);
        EasyMock.expect(privilegeDAO.findAll()).andReturn(privilegeEntities).atLeastOnce();
        org.apache.ambari.server.orm.dao.UserDAO userDAO = injector.getInstance(org.apache.ambari.server.orm.dao.UserDAO.class);
        EasyMock.expect(userDAO.findUsersByPrincipal(org.easymock.EasyMock.anyObject())).andReturn(userEntities).atLeastOnce();
        replayAll();
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator("admin"));
        org.apache.ambari.server.controller.spi.ResourceProvider provider = getResourceProvider(injector);
        org.apache.ambari.server.view.ViewRegistry.getInstance().addDefinition(viewEntity);
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = provider.getResources(org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(), null);
        org.junit.Assert.assertEquals(3, resources.size());
        java.util.Map<java.lang.Object, org.apache.ambari.server.controller.spi.Resource> resourceMap = new java.util.HashMap<>();
        for (org.apache.ambari.server.controller.spi.Resource resource : resources) {
            resourceMap.put(resource.getPropertyValue(org.apache.ambari.server.controller.internal.AmbariPrivilegeResourceProvider.PRIVILEGE_ID), resource);
        }
        org.apache.ambari.server.controller.spi.Resource resource1 = resourceMap.get(31);
        org.junit.Assert.assertEquals(6, resource1.getPropertiesMap().get("PrivilegeInfo").size());
        org.junit.Assert.assertEquals("AMBARI.ADMINISTRATOR", resource1.getPropertyValue(org.apache.ambari.server.controller.internal.AmbariPrivilegeResourceProvider.PERMISSION_NAME));
        org.junit.Assert.assertEquals("Ambari Administrator", resource1.getPropertyValue(org.apache.ambari.server.controller.internal.AmbariPrivilegeResourceProvider.PERMISSION_LABEL));
        org.junit.Assert.assertEquals("joe", resource1.getPropertyValue(org.apache.ambari.server.controller.internal.AmbariPrivilegeResourceProvider.PRINCIPAL_NAME));
        org.junit.Assert.assertEquals("USER", resource1.getPropertyValue(org.apache.ambari.server.controller.internal.AmbariPrivilegeResourceProvider.PRINCIPAL_TYPE));
        org.junit.Assert.assertEquals(31, resource1.getPropertyValue(org.apache.ambari.server.controller.internal.AmbariPrivilegeResourceProvider.PRIVILEGE_ID));
        org.junit.Assert.assertEquals("AMBARI", resource1.getPropertyValue(org.apache.ambari.server.controller.internal.AmbariPrivilegeResourceProvider.TYPE));
        org.apache.ambari.server.controller.spi.Resource resource2 = resourceMap.get(32);
        org.junit.Assert.assertEquals(7, resource2.getPropertiesMap().get("PrivilegeInfo").size());
        org.junit.Assert.assertEquals("CLUSTER.USER", resource2.getPropertyValue(org.apache.ambari.server.controller.internal.AmbariPrivilegeResourceProvider.PERMISSION_NAME));
        org.junit.Assert.assertEquals("Cluster User", resource2.getPropertyValue(org.apache.ambari.server.controller.internal.AmbariPrivilegeResourceProvider.PERMISSION_LABEL));
        org.junit.Assert.assertEquals("jeff", resource2.getPropertyValue(org.apache.ambari.server.controller.internal.AmbariPrivilegeResourceProvider.PRINCIPAL_NAME));
        org.junit.Assert.assertEquals("USER", resource2.getPropertyValue(org.apache.ambari.server.controller.internal.AmbariPrivilegeResourceProvider.PRINCIPAL_TYPE));
        org.junit.Assert.assertEquals(32, resource2.getPropertyValue(org.apache.ambari.server.controller.internal.AmbariPrivilegeResourceProvider.PRIVILEGE_ID));
        org.junit.Assert.assertEquals("cluster1", resource2.getPropertyValue(org.apache.ambari.server.controller.internal.ClusterPrivilegeResourceProvider.CLUSTER_NAME));
        org.junit.Assert.assertEquals("CLUSTER", resource2.getPropertyValue(org.apache.ambari.server.controller.internal.AmbariPrivilegeResourceProvider.TYPE));
        org.apache.ambari.server.controller.spi.Resource resource3 = resourceMap.get(33);
        org.junit.Assert.assertEquals(9, resource3.getPropertiesMap().get("PrivilegeInfo").size());
        org.junit.Assert.assertEquals("VIEW.USER", resource3.getPropertyValue(org.apache.ambari.server.controller.internal.AmbariPrivilegeResourceProvider.PERMISSION_NAME));
        org.junit.Assert.assertEquals("View User", resource3.getPropertyValue(org.apache.ambari.server.controller.internal.AmbariPrivilegeResourceProvider.PERMISSION_LABEL));
        org.junit.Assert.assertEquals("bob", resource3.getPropertyValue(org.apache.ambari.server.controller.internal.AmbariPrivilegeResourceProvider.PRINCIPAL_NAME));
        org.junit.Assert.assertEquals("USER", resource3.getPropertyValue(org.apache.ambari.server.controller.internal.AmbariPrivilegeResourceProvider.PRINCIPAL_TYPE));
        org.junit.Assert.assertEquals(33, resource3.getPropertyValue(org.apache.ambari.server.controller.internal.AmbariPrivilegeResourceProvider.PRIVILEGE_ID));
        org.junit.Assert.assertEquals("view", resource3.getPropertyValue(org.apache.ambari.server.controller.internal.ViewPrivilegeResourceProvider.VIEW_NAME));
        org.junit.Assert.assertEquals("1.0.1", resource3.getPropertyValue(org.apache.ambari.server.controller.internal.ViewPrivilegeResourceProvider.VERSION));
        org.junit.Assert.assertEquals("inst1", resource3.getPropertyValue(org.apache.ambari.server.controller.internal.ViewPrivilegeResourceProvider.INSTANCE_NAME));
        org.junit.Assert.assertEquals("VIEW", resource3.getPropertyValue(org.apache.ambari.server.controller.internal.AmbariPrivilegeResourceProvider.TYPE));
        verifyAll();
    }

    @org.junit.Test
    public void testToResource_AMBARI() {
        org.apache.ambari.server.orm.entities.PermissionEntity permissionEntity = createMock(org.apache.ambari.server.orm.entities.PermissionEntity.class);
        EasyMock.expect(permissionEntity.getPermissionName()).andReturn("ADMINISTRATOR").atLeastOnce();
        EasyMock.expect(permissionEntity.getPermissionLabel()).andReturn("Ambari Administrator").atLeastOnce();
        org.apache.ambari.server.orm.entities.PrincipalTypeEntity principalTypeEntity = createMock(org.apache.ambari.server.orm.entities.PrincipalTypeEntity.class);
        EasyMock.expect(principalTypeEntity.getName()).andReturn("USER").atLeastOnce();
        org.apache.ambari.server.orm.entities.PrincipalEntity principalEntity = createMock(org.apache.ambari.server.orm.entities.PrincipalEntity.class);
        EasyMock.expect(principalEntity.getId()).andReturn(1L).atLeastOnce();
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
        EasyMock.replay(permissionEntity, principalTypeEntity, principalEntity, resourceTypeEntity, resourceEntity, privilegeEntity);
        java.util.Map<java.lang.Long, org.apache.ambari.server.orm.entities.UserEntity> userEntities = new java.util.HashMap<>();
        java.util.Map<java.lang.Long, org.apache.ambari.server.orm.entities.GroupEntity> groupEntities = new java.util.HashMap<>();
        java.util.Map<java.lang.Long, org.apache.ambari.server.orm.entities.PermissionEntity> roleEntities = new java.util.HashMap<>();
        java.util.Map<java.lang.Long, java.lang.Object> resourceEntities = new java.util.HashMap<>();
        org.apache.ambari.server.controller.internal.AmbariPrivilegeResourceProvider provider = new org.apache.ambari.server.controller.internal.AmbariPrivilegeResourceProvider();
        org.apache.ambari.server.controller.spi.Resource resource = provider.toResource(privilegeEntity, userEntities, groupEntities, roleEntities, resourceEntities, provider.getPropertyIds());
        org.junit.Assert.assertEquals(org.apache.ambari.server.security.authorization.ResourceType.AMBARI.name(), resource.getPropertyValue(org.apache.ambari.server.controller.internal.AmbariPrivilegeResourceProvider.TYPE));
        EasyMock.verify(permissionEntity, principalTypeEntity, principalEntity, resourceTypeEntity, resourceEntity, privilegeEntity);
    }

    @org.junit.Test
    public void testToResource_CLUSTER() {
        org.apache.ambari.server.orm.entities.PermissionEntity permissionEntity = createMock(org.apache.ambari.server.orm.entities.PermissionEntity.class);
        EasyMock.expect(permissionEntity.getPermissionName()).andReturn("CLUSTER.ADMINISTRATOR").atLeastOnce();
        EasyMock.expect(permissionEntity.getPermissionLabel()).andReturn("Cluster Administrator").atLeastOnce();
        org.apache.ambari.server.orm.entities.PrincipalTypeEntity principalTypeEntity = createMock(org.apache.ambari.server.orm.entities.PrincipalTypeEntity.class);
        EasyMock.expect(principalTypeEntity.getName()).andReturn("USER").atLeastOnce();
        org.apache.ambari.server.orm.entities.PrincipalEntity principalEntity = createMock(org.apache.ambari.server.orm.entities.PrincipalEntity.class);
        EasyMock.expect(principalEntity.getId()).andReturn(1L).atLeastOnce();
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
        EasyMock.replay(permissionEntity, principalTypeEntity, principalEntity, resourceTypeEntity, clusterEntity, resourceEntity, privilegeEntity);
        java.util.Map<java.lang.Long, org.apache.ambari.server.orm.entities.UserEntity> userEntities = new java.util.HashMap<>();
        java.util.Map<java.lang.Long, org.apache.ambari.server.orm.entities.GroupEntity> groupEntities = new java.util.HashMap<>();
        java.util.Map<java.lang.Long, org.apache.ambari.server.orm.entities.PermissionEntity> roleEntities = new java.util.HashMap<>();
        java.util.Map<java.lang.Long, java.lang.Object> resourceEntities = new java.util.HashMap<>();
        resourceEntities.put(resourceEntity.getId(), clusterEntity);
        org.apache.ambari.server.controller.internal.AmbariPrivilegeResourceProvider provider = new org.apache.ambari.server.controller.internal.AmbariPrivilegeResourceProvider();
        org.apache.ambari.server.controller.spi.Resource resource = provider.toResource(privilegeEntity, userEntities, groupEntities, roleEntities, resourceEntities, provider.getPropertyIds());
        org.junit.Assert.assertEquals("TestCluster", resource.getPropertyValue(org.apache.ambari.server.controller.internal.ClusterPrivilegeResourceProvider.CLUSTER_NAME));
        org.junit.Assert.assertEquals(org.apache.ambari.server.security.authorization.ResourceType.CLUSTER.name(), resource.getPropertyValue(org.apache.ambari.server.controller.internal.AmbariPrivilegeResourceProvider.TYPE));
        EasyMock.verify(permissionEntity, principalTypeEntity, principalEntity, resourceTypeEntity, clusterEntity, resourceEntity, privilegeEntity);
    }

    @org.junit.Test
    public void testToResource_VIEW() {
        org.apache.ambari.server.orm.entities.PermissionEntity permissionEntity = createMock(org.apache.ambari.server.orm.entities.PermissionEntity.class);
        EasyMock.expect(permissionEntity.getPermissionName()).andReturn("CLUSTER.ADMINISTRATOR").atLeastOnce();
        EasyMock.expect(permissionEntity.getPermissionLabel()).andReturn("Cluster Administrator").atLeastOnce();
        org.apache.ambari.server.orm.entities.PrincipalTypeEntity principalTypeEntity = createMock(org.apache.ambari.server.orm.entities.PrincipalTypeEntity.class);
        EasyMock.expect(principalTypeEntity.getName()).andReturn("USER").atLeastOnce();
        org.apache.ambari.server.orm.entities.PrincipalEntity principalEntity = createMock(org.apache.ambari.server.orm.entities.PrincipalEntity.class);
        EasyMock.expect(principalEntity.getId()).andReturn(1L).atLeastOnce();
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
        EasyMock.replay(permissionEntity, principalTypeEntity, principalEntity, resourceTypeEntity, viewInstanceEntity, viewEntity, resourceEntity, privilegeEntity);
        java.util.Map<java.lang.Long, org.apache.ambari.server.orm.entities.UserEntity> userEntities = new java.util.HashMap<>();
        java.util.Map<java.lang.Long, org.apache.ambari.server.orm.entities.GroupEntity> groupEntities = new java.util.HashMap<>();
        java.util.Map<java.lang.Long, org.apache.ambari.server.orm.entities.PermissionEntity> roleEntities = new java.util.HashMap<>();
        java.util.Map<java.lang.Long, java.lang.Object> resourceEntities = new java.util.HashMap<>();
        resourceEntities.put(resourceEntity.getId(), viewInstanceEntity);
        org.apache.ambari.server.controller.internal.AmbariPrivilegeResourceProvider provider = new org.apache.ambari.server.controller.internal.AmbariPrivilegeResourceProvider();
        org.apache.ambari.server.controller.spi.Resource resource = provider.toResource(privilegeEntity, userEntities, groupEntities, roleEntities, resourceEntities, provider.getPropertyIds());
        org.junit.Assert.assertEquals("Test View", resource.getPropertyValue(org.apache.ambari.server.controller.internal.ViewPrivilegeResourceProvider.INSTANCE_NAME));
        org.junit.Assert.assertEquals("TestView", resource.getPropertyValue(org.apache.ambari.server.controller.internal.ViewPrivilegeResourceProvider.VIEW_NAME));
        org.junit.Assert.assertEquals("1.2.3.4", resource.getPropertyValue(org.apache.ambari.server.controller.internal.ViewPrivilegeResourceProvider.VERSION));
        org.junit.Assert.assertEquals(org.apache.ambari.server.security.authorization.ResourceType.VIEW.name(), resource.getPropertyValue(org.apache.ambari.server.controller.internal.AmbariPrivilegeResourceProvider.TYPE));
        EasyMock.verify(permissionEntity, principalTypeEntity, principalEntity, resourceTypeEntity, viewInstanceEntity, viewEntity, resourceEntity, privilegeEntity);
    }

    @org.junit.Test
    public void testToResource_SpecificVIEW() {
        org.apache.ambari.server.orm.entities.PermissionEntity permissionEntity = createMock(org.apache.ambari.server.orm.entities.PermissionEntity.class);
        EasyMock.expect(permissionEntity.getPermissionName()).andReturn("CLUSTER.ADMINISTRATOR").atLeastOnce();
        EasyMock.expect(permissionEntity.getPermissionLabel()).andReturn("Cluster Administrator").atLeastOnce();
        org.apache.ambari.server.orm.entities.PrincipalTypeEntity principalTypeEntity = createMock(org.apache.ambari.server.orm.entities.PrincipalTypeEntity.class);
        EasyMock.expect(principalTypeEntity.getName()).andReturn("USER").atLeastOnce();
        org.apache.ambari.server.orm.entities.PrincipalEntity principalEntity = createMock(org.apache.ambari.server.orm.entities.PrincipalEntity.class);
        EasyMock.expect(principalEntity.getId()).andReturn(1L).atLeastOnce();
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
        EasyMock.replay(permissionEntity, principalTypeEntity, principalEntity, resourceTypeEntity, viewInstanceEntity, viewEntity, resourceEntity, privilegeEntity);
        java.util.Map<java.lang.Long, org.apache.ambari.server.orm.entities.UserEntity> userEntities = new java.util.HashMap<>();
        java.util.Map<java.lang.Long, org.apache.ambari.server.orm.entities.GroupEntity> groupEntities = new java.util.HashMap<>();
        java.util.Map<java.lang.Long, org.apache.ambari.server.orm.entities.PermissionEntity> roleEntities = new java.util.HashMap<>();
        java.util.Map<java.lang.Long, java.lang.Object> resourceEntities = new java.util.HashMap<>();
        resourceEntities.put(resourceEntity.getId(), viewInstanceEntity);
        org.apache.ambari.server.controller.internal.AmbariPrivilegeResourceProvider provider = new org.apache.ambari.server.controller.internal.AmbariPrivilegeResourceProvider();
        org.apache.ambari.server.controller.spi.Resource resource = provider.toResource(privilegeEntity, userEntities, groupEntities, roleEntities, resourceEntities, provider.getPropertyIds());
        org.junit.Assert.assertEquals("Test View", resource.getPropertyValue(org.apache.ambari.server.controller.internal.ViewPrivilegeResourceProvider.INSTANCE_NAME));
        org.junit.Assert.assertEquals("TestView", resource.getPropertyValue(org.apache.ambari.server.controller.internal.ViewPrivilegeResourceProvider.VIEW_NAME));
        org.junit.Assert.assertEquals("1.2.3.4", resource.getPropertyValue(org.apache.ambari.server.controller.internal.ViewPrivilegeResourceProvider.VERSION));
        org.junit.Assert.assertEquals(org.apache.ambari.server.security.authorization.ResourceType.VIEW.name(), resource.getPropertyValue(org.apache.ambari.server.controller.internal.AmbariPrivilegeResourceProvider.TYPE));
        EasyMock.verify(permissionEntity, principalTypeEntity, principalEntity, resourceTypeEntity, viewInstanceEntity, viewEntity, resourceEntity, privilegeEntity);
    }

    private void createResourcesTest(org.springframework.security.core.Authentication authentication) throws java.lang.Exception {
        com.google.inject.Injector injector = createInjector();
        org.apache.ambari.server.orm.entities.PrincipalEntity principalEntity = createMockPrincipalEntity(2L, createMockPrincipalTypeEntity("USER"));
        org.apache.ambari.server.orm.entities.ResourceTypeEntity clusterResourceTypeEntity = createMockResourceTypeEntity(org.apache.ambari.server.security.authorization.ResourceType.CLUSTER);
        org.apache.ambari.server.orm.entities.ResourceEntity clusterResourceEntity = createMockResourceEntity(1L, clusterResourceTypeEntity);
        org.apache.ambari.server.orm.entities.PermissionEntity permissionEntity = createMockPermissionEntity("CLUSTER.OPERATOR", "Cluster Operator", clusterResourceTypeEntity);
        org.apache.ambari.server.orm.entities.PrivilegeEntity privilegeEntity = createMockPrivilegeEntity(2, clusterResourceEntity, principalEntity, permissionEntity);
        java.util.Set<org.apache.ambari.server.orm.entities.PrivilegeEntity> privilegeEntities = new java.util.HashSet<>();
        privilegeEntities.add(privilegeEntity);
        EasyMock.expect(principalEntity.getPrivileges()).andReturn(privilegeEntities).atLeastOnce();
        org.apache.ambari.server.orm.entities.UserEntity userEntity = createMockUserEntity(principalEntity, "User1");
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
        EasyMock.expect(clusterDAO.findAll()).andReturn(java.util.Collections.emptyList()).atLeastOnce();
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
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getUpdateRequest(properties, null);
        org.apache.ambari.server.controller.spi.ResourceProvider provider = getResourceProvider(injector);
        provider.createResources(request);
        verifyAll();
    }

    private void getResourcesTest(org.springframework.security.core.Authentication authentication) throws java.lang.Exception {
        com.google.inject.Injector injector = createInjector();
        java.util.List<org.apache.ambari.server.orm.entities.PrivilegeEntity> privilegeEntities = new java.util.LinkedList<>();
        org.apache.ambari.server.orm.entities.PrincipalEntity principalEntity = createMockPrincipalEntity(1L, createMockPrincipalTypeEntity("USER"));
        java.util.List<org.apache.ambari.server.orm.entities.PrincipalEntity> principalEntities = new java.util.LinkedList<>();
        principalEntities.add(principalEntity);
        java.util.List<org.apache.ambari.server.orm.entities.UserEntity> userEntities = new java.util.LinkedList<>();
        userEntities.add(createMockUserEntity(principalEntity, "User1"));
        org.apache.ambari.server.orm.entities.ResourceTypeEntity ambariResourceTypeEntity = createMockResourceTypeEntity(org.apache.ambari.server.security.authorization.ResourceType.AMBARI);
        org.apache.ambari.server.orm.entities.ResourceEntity ambariResourceEntity = createMockResourceEntity(1L, ambariResourceTypeEntity);
        privilegeEntities.add(createMockPrivilegeEntity(1, ambariResourceEntity, principalEntity, createMockPermissionEntity("AMBARI.ADMINISTRATOR", "Ambari Administrator", ambariResourceTypeEntity)));
        org.apache.ambari.server.orm.dao.PrivilegeDAO privilegeDAO = injector.getInstance(org.apache.ambari.server.orm.dao.PrivilegeDAO.class);
        EasyMock.expect(privilegeDAO.findAll()).andReturn(privilegeEntities).atLeastOnce();
        org.apache.ambari.server.orm.dao.UserDAO userDAO = injector.getInstance(org.apache.ambari.server.orm.dao.UserDAO.class);
        EasyMock.expect(userDAO.findUsersByPrincipal(principalEntities)).andReturn(userEntities).atLeastOnce();
        org.apache.ambari.server.orm.dao.ClusterDAO clusterDAO = injector.getInstance(org.apache.ambari.server.orm.dao.ClusterDAO.class);
        EasyMock.expect(clusterDAO.findAll()).andReturn(java.util.Collections.emptyList()).atLeastOnce();
        replayAll();
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        org.apache.ambari.server.controller.spi.ResourceProvider provider = getResourceProvider(injector);
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = provider.getResources(org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(), null);
        org.junit.Assert.assertEquals(1, resources.size());
        org.apache.ambari.server.controller.spi.Resource resource = resources.iterator().next();
        org.junit.Assert.assertEquals("AMBARI.ADMINISTRATOR", resource.getPropertyValue(org.apache.ambari.server.controller.internal.AmbariPrivilegeResourceProvider.PERMISSION_NAME));
        org.junit.Assert.assertEquals("Ambari Administrator", resource.getPropertyValue(org.apache.ambari.server.controller.internal.AmbariPrivilegeResourceProvider.PERMISSION_LABEL));
        org.junit.Assert.assertEquals("User1", resource.getPropertyValue(org.apache.ambari.server.controller.internal.AmbariPrivilegeResourceProvider.PRINCIPAL_NAME));
        org.junit.Assert.assertEquals("USER", resource.getPropertyValue(org.apache.ambari.server.controller.internal.AmbariPrivilegeResourceProvider.PRINCIPAL_TYPE));
        verifyAll();
    }

    private void getResourceTest(org.springframework.security.core.Authentication authentication, java.lang.String requestedUsername) throws java.lang.Exception {
        com.google.inject.Injector injector = createInjector();
        org.apache.ambari.server.orm.entities.PrincipalEntity principalEntity1 = createMockPrincipalEntity(1L, createMockPrincipalTypeEntity("USER"));
        org.apache.ambari.server.orm.entities.PrincipalEntity principalEntity2 = createMockPrincipalEntity(2L, createMockPrincipalTypeEntity("USER"));
        java.util.List<org.apache.ambari.server.orm.entities.PrincipalEntity> principalEntities = new java.util.LinkedList<>();
        principalEntities.add(principalEntity1);
        principalEntities.add(principalEntity2);
        java.util.List<org.apache.ambari.server.orm.entities.UserEntity> userEntities = new java.util.LinkedList<>();
        userEntities.add(createMockUserEntity(principalEntity1, requestedUsername));
        userEntities.add(createMockUserEntity(principalEntity2, "Not" + requestedUsername));
        org.apache.ambari.server.orm.entities.ResourceTypeEntity clusterResourceTypeEntity = createMockResourceTypeEntity(org.apache.ambari.server.security.authorization.ResourceType.CLUSTER);
        org.apache.ambari.server.orm.entities.ResourceEntity clusterResourceEntity = createMockResourceEntity(1L, clusterResourceTypeEntity);
        org.apache.ambari.server.orm.entities.PermissionEntity permissionEntity = createMockPermissionEntity("CLUSTER.OPERATOR", "Cluster Operator", clusterResourceTypeEntity);
        java.util.List<org.apache.ambari.server.orm.entities.PrivilegeEntity> privilegeEntities = new java.util.LinkedList<>();
        privilegeEntities.add(createMockPrivilegeEntity(1, clusterResourceEntity, principalEntity1, permissionEntity));
        privilegeEntities.add(createMockPrivilegeEntity(2, clusterResourceEntity, principalEntity2, permissionEntity));
        org.apache.ambari.server.orm.dao.PrivilegeDAO privilegeDAO = injector.getInstance(org.apache.ambari.server.orm.dao.PrivilegeDAO.class);
        EasyMock.expect(privilegeDAO.findAll()).andReturn(privilegeEntities).atLeastOnce();
        org.apache.ambari.server.orm.dao.UserDAO userDAO = injector.getInstance(org.apache.ambari.server.orm.dao.UserDAO.class);
        EasyMock.expect(userDAO.findUsersByPrincipal(principalEntities)).andReturn(userEntities).atLeastOnce();
        java.util.List<org.apache.ambari.server.orm.entities.ClusterEntity> clusterEntities = new java.util.ArrayList<>();
        clusterEntities.add(createMockClusterEntity("c1", clusterResourceEntity));
        org.apache.ambari.server.orm.dao.ClusterDAO clusterDAO = injector.getInstance(org.apache.ambari.server.orm.dao.ClusterDAO.class);
        EasyMock.expect(clusterDAO.findAll()).andReturn(clusterEntities).atLeastOnce();
        replayAll();
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        org.apache.ambari.server.controller.spi.ResourceProvider provider = getResourceProvider(injector);
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = provider.getResources(org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(), createPredicate(1L));
        org.junit.Assert.assertEquals(1, resources.size());
        org.apache.ambari.server.controller.spi.Resource resource = resources.iterator().next();
        org.junit.Assert.assertEquals("CLUSTER.OPERATOR", resource.getPropertyValue(org.apache.ambari.server.controller.internal.AmbariPrivilegeResourceProvider.PERMISSION_NAME));
        org.junit.Assert.assertEquals("Cluster Operator", resource.getPropertyValue(org.apache.ambari.server.controller.internal.AmbariPrivilegeResourceProvider.PERMISSION_LABEL));
        org.junit.Assert.assertEquals(requestedUsername, resource.getPropertyValue(org.apache.ambari.server.controller.internal.AmbariPrivilegeResourceProvider.PRINCIPAL_NAME));
        org.junit.Assert.assertEquals("USER", resource.getPropertyValue(org.apache.ambari.server.controller.internal.AmbariPrivilegeResourceProvider.PRINCIPAL_TYPE));
        verifyAll();
    }

    private void updateResourcesTest(org.springframework.security.core.Authentication authentication, java.lang.String requestedUsername) throws java.lang.Exception {
        com.google.inject.Injector injector = createInjector();
        org.apache.ambari.server.orm.entities.PrincipalEntity principalEntity1 = createMockPrincipalEntity(1L, createMockPrincipalTypeEntity("USER"));
        org.apache.ambari.server.orm.entities.PrincipalEntity principalEntity2 = createMockPrincipalEntity(2L, createMockPrincipalTypeEntity("USER"));
        org.apache.ambari.server.orm.entities.ResourceTypeEntity clusterResourceTypeEntity = createMockResourceTypeEntity(org.apache.ambari.server.security.authorization.ResourceType.CLUSTER);
        org.apache.ambari.server.orm.entities.ResourceEntity clusterResourceEntity = createMockResourceEntity(1L, clusterResourceTypeEntity);
        org.apache.ambari.server.orm.entities.PermissionEntity permissionEntity = createMockPermissionEntity("CLUSTER.OPERATOR", "Cluster Operator", clusterResourceTypeEntity);
        org.apache.ambari.server.orm.entities.PrivilegeEntity privilegeEntity1 = createMockPrivilegeEntity(1, clusterResourceEntity, principalEntity1, permissionEntity);
        org.apache.ambari.server.orm.entities.PrivilegeEntity privilegeEntity2 = createMockPrivilegeEntity(2, clusterResourceEntity, principalEntity2, permissionEntity);
        java.util.Set<org.apache.ambari.server.orm.entities.PrivilegeEntity> privilege1Entities = new java.util.HashSet<>();
        privilege1Entities.add(privilegeEntity1);
        java.util.Set<org.apache.ambari.server.orm.entities.PrivilegeEntity> privilege2Entities = new java.util.HashSet<>();
        privilege2Entities.add(privilegeEntity2);
        java.util.List<org.apache.ambari.server.orm.entities.PrivilegeEntity> privilegeEntities = new java.util.LinkedList<>();
        privilegeEntities.addAll(privilege1Entities);
        privilegeEntities.addAll(privilege2Entities);
        EasyMock.expect(principalEntity2.getPrivileges()).andReturn(privilege2Entities).atLeastOnce();
        org.apache.ambari.server.orm.entities.UserEntity userEntity = createMockUserEntity(principalEntity1, requestedUsername);
        org.apache.ambari.server.orm.dao.PrivilegeDAO privilegeDAO = injector.getInstance(org.apache.ambari.server.orm.dao.PrivilegeDAO.class);
        EasyMock.expect(privilegeDAO.findByResourceId(1L)).andReturn(privilegeEntities).atLeastOnce();
        privilegeDAO.remove(privilegeEntity2);
        EasyMock.expectLastCall().atLeastOnce();
        org.apache.ambari.server.orm.dao.UserDAO userDAO = injector.getInstance(org.apache.ambari.server.orm.dao.UserDAO.class);
        EasyMock.expect(userDAO.findUserByName(requestedUsername)).andReturn(userEntity).atLeastOnce();
        org.apache.ambari.server.orm.dao.PrincipalDAO principalDAO = injector.getInstance(org.apache.ambari.server.orm.dao.PrincipalDAO.class);
        EasyMock.expect(principalDAO.findById(1L)).andReturn(principalEntity1).atLeastOnce();
        EasyMock.expect(principalDAO.merge(principalEntity2)).andReturn(principalEntity2).atLeastOnce();
        org.apache.ambari.server.orm.dao.ClusterDAO clusterDAO = injector.getInstance(org.apache.ambari.server.orm.dao.ClusterDAO.class);
        EasyMock.expect(clusterDAO.findAll()).andReturn(java.util.Collections.emptyList()).atLeastOnce();
        org.apache.ambari.server.orm.dao.ResourceDAO resourceDAO = injector.getInstance(org.apache.ambari.server.orm.dao.ResourceDAO.class);
        EasyMock.expect(resourceDAO.findById(1L)).andReturn(clusterResourceEntity).atLeastOnce();
        org.apache.ambari.server.orm.dao.PermissionDAO permissionDAO = injector.getInstance(org.apache.ambari.server.orm.dao.PermissionDAO.class);
        EasyMock.expect(permissionDAO.findPermissionByNameAndType("CLUSTER.OPERATOR", clusterResourceTypeEntity)).andReturn(permissionEntity).atLeastOnce();
        replayAll();
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        java.util.Map<java.lang.String, java.lang.Object> properties = new java.util.LinkedHashMap<>();
        properties.put(org.apache.ambari.server.controller.internal.PrivilegeResourceProvider.PERMISSION_NAME, "CLUSTER.OPERATOR");
        properties.put(org.apache.ambari.server.controller.internal.PrivilegeResourceProvider.PRINCIPAL_NAME, requestedUsername);
        properties.put(org.apache.ambari.server.controller.internal.PrivilegeResourceProvider.PRINCIPAL_TYPE, "USER");
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getUpdateRequest(properties, null);
        org.apache.ambari.server.controller.spi.ResourceProvider provider = getResourceProvider(injector);
        provider.updateResources(request, createPredicate(1L));
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

    private org.apache.ambari.server.controller.spi.Predicate createPredicate(java.lang.Long id) {
        return new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.AmbariPrivilegeResourceProvider.PRIVILEGE_ID).equals(id).toPredicate();
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

    private org.apache.ambari.server.orm.entities.ResourceTypeEntity createMockResourceTypeEntity(org.apache.ambari.server.security.authorization.ResourceType resourceType) {
        org.apache.ambari.server.orm.entities.ResourceTypeEntity resourceTypeEntity = createMock(org.apache.ambari.server.orm.entities.ResourceTypeEntity.class);
        EasyMock.expect(resourceTypeEntity.getId()).andReturn(resourceType.getId()).anyTimes();
        EasyMock.expect(resourceTypeEntity.getName()).andReturn(resourceType.name()).anyTimes();
        return resourceTypeEntity;
    }

    private org.apache.ambari.server.orm.entities.ResourceEntity createMockResourceEntity(java.lang.Long id, org.apache.ambari.server.orm.entities.ResourceTypeEntity resourceTypeEntity) {
        org.apache.ambari.server.orm.entities.ResourceEntity resourceEntity = createMock(org.apache.ambari.server.orm.entities.ResourceEntity.class);
        EasyMock.expect(resourceEntity.getId()).andReturn(id).anyTimes();
        EasyMock.expect(resourceEntity.getResourceType()).andReturn(resourceTypeEntity).anyTimes();
        return resourceEntity;
    }

    private org.apache.ambari.server.orm.entities.PrivilegeEntity createMockPrivilegeEntity(java.lang.Integer id, org.apache.ambari.server.orm.entities.ResourceEntity resourceEntity, org.apache.ambari.server.orm.entities.PrincipalEntity principalEntity, org.apache.ambari.server.orm.entities.PermissionEntity permissionEntity) {
        org.apache.ambari.server.orm.entities.PrivilegeEntity privilegeEntity = createMock(org.apache.ambari.server.orm.entities.PrivilegeEntity.class);
        EasyMock.expect(privilegeEntity.getId()).andReturn(id).anyTimes();
        EasyMock.expect(privilegeEntity.getResource()).andReturn(resourceEntity).anyTimes();
        EasyMock.expect(privilegeEntity.getPrincipal()).andReturn(principalEntity).anyTimes();
        EasyMock.expect(privilegeEntity.getPermission()).andReturn(permissionEntity).anyTimes();
        return privilegeEntity;
    }

    private org.apache.ambari.server.controller.spi.ResourceProvider getResourceProvider(com.google.inject.Injector injector) {
        org.apache.ambari.server.view.ViewRegistry.initInstance(org.apache.ambari.server.view.ViewRegistryTest.getRegistry(injector.getInstance(org.apache.ambari.server.orm.dao.ViewDAO.class), injector.getInstance(org.apache.ambari.server.orm.dao.ViewInstanceDAO.class), injector.getInstance(org.apache.ambari.server.orm.dao.UserDAO.class), injector.getInstance(org.apache.ambari.server.orm.dao.MemberDAO.class), injector.getInstance(org.apache.ambari.server.orm.dao.PrivilegeDAO.class), injector.getInstance(org.apache.ambari.server.orm.dao.PermissionDAO.class), injector.getInstance(org.apache.ambari.server.orm.dao.ResourceDAO.class), injector.getInstance(org.apache.ambari.server.orm.dao.ResourceTypeDAO.class), injector.getInstance(org.apache.ambari.server.security.SecurityHelper.class), injector.getInstance(org.apache.ambari.server.view.ViewInstanceHandlerList.class), null, null, null));
        org.apache.ambari.server.controller.internal.PrivilegeResourceProvider.init(injector.getInstance(org.apache.ambari.server.orm.dao.PrivilegeDAO.class), injector.getInstance(org.apache.ambari.server.orm.dao.UserDAO.class), injector.getInstance(org.apache.ambari.server.orm.dao.GroupDAO.class), injector.getInstance(org.apache.ambari.server.orm.dao.PrincipalDAO.class), injector.getInstance(org.apache.ambari.server.orm.dao.PermissionDAO.class), injector.getInstance(org.apache.ambari.server.orm.dao.ResourceDAO.class));
        org.apache.ambari.server.controller.internal.AmbariPrivilegeResourceProvider.init(injector.getInstance(org.apache.ambari.server.orm.dao.ClusterDAO.class));
        return new org.apache.ambari.server.controller.internal.AmbariPrivilegeResourceProvider();
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