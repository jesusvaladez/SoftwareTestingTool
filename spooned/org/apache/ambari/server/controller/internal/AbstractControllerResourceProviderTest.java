package org.apache.ambari.server.controller.internal;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
public class AbstractControllerResourceProviderTest {
    @org.junit.Test
    public void testGetResourceProvider() throws java.lang.Exception {
        org.apache.ambari.server.controller.AmbariManagementController managementController = EasyMock.createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.controller.ResourceProviderFactory factory = EasyMock.createMock(org.apache.ambari.server.controller.ResourceProviderFactory.class);
        org.apache.ambari.server.controller.MaintenanceStateHelper maintenanceStateHelper = EasyMock.createNiceMock(org.apache.ambari.server.controller.MaintenanceStateHelper.class);
        org.apache.ambari.server.orm.dao.RepositoryVersionDAO repositoryVersionDAO = EasyMock.createNiceMock(org.apache.ambari.server.orm.dao.RepositoryVersionDAO.class);
        org.apache.ambari.server.controller.spi.ResourceProvider serviceResourceProvider = new org.apache.ambari.server.controller.internal.ServiceResourceProvider(managementController, maintenanceStateHelper, repositoryVersionDAO);
        EasyMock.expect(factory.getServiceResourceProvider(managementController)).andReturn(serviceResourceProvider);
        org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.init(factory);
        EasyMock.replay(managementController, factory, maintenanceStateHelper, repositoryVersionDAO);
        org.apache.ambari.server.controller.internal.AbstractResourceProvider provider = ((org.apache.ambari.server.controller.internal.AbstractResourceProvider) (org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.getResourceProvider(org.apache.ambari.server.controller.spi.Resource.Type.Service, managementController)));
        junit.framework.Assert.assertTrue(provider instanceof org.apache.ambari.server.controller.internal.ServiceResourceProvider);
    }

    @org.junit.Test
    public void testGetStackArtifactResourceProvider() {
        org.apache.ambari.server.controller.AmbariManagementController managementController = EasyMock.createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.controller.spi.ResourceProvider provider = org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.getResourceProvider(org.apache.ambari.server.controller.spi.Resource.Type.StackArtifact, managementController);
        org.junit.Assert.assertEquals(org.apache.ambari.server.controller.internal.StackArtifactResourceProvider.class, provider.getClass());
    }

    @org.junit.Test
    public void testGetRoleAuthorizationResourceProvider() {
        org.apache.ambari.server.controller.AmbariManagementController managementController = EasyMock.createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        EasyMock.replay(managementController);
        org.apache.ambari.server.controller.spi.ResourceProvider provider = org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.getResourceProvider(org.apache.ambari.server.controller.spi.Resource.Type.RoleAuthorization, managementController);
        EasyMock.verify(managementController);
        org.junit.Assert.assertEquals(org.apache.ambari.server.controller.internal.RoleAuthorizationResourceProvider.class, provider.getClass());
    }

    @org.junit.Test
    public void testGetUserAuthorizationResourceProvider() {
        org.apache.ambari.server.controller.AmbariManagementController managementController = EasyMock.createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        EasyMock.replay(managementController);
        org.apache.ambari.server.controller.spi.ResourceProvider provider = org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.getResourceProvider(org.apache.ambari.server.controller.spi.Resource.Type.UserAuthorization, managementController);
        EasyMock.verify(managementController);
        org.junit.Assert.assertEquals(org.apache.ambari.server.controller.internal.UserAuthorizationResourceProvider.class, provider.getClass());
    }

    @org.junit.Test
    public void testGetClusterKerberosDescriptorResourceProvider() {
        org.apache.ambari.server.controller.AmbariManagementController managementController = EasyMock.createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        EasyMock.replay(managementController);
        org.apache.ambari.server.controller.spi.ResourceProvider provider = org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.getResourceProvider(org.apache.ambari.server.controller.spi.Resource.Type.ClusterKerberosDescriptor, managementController);
        EasyMock.verify(managementController);
        org.junit.Assert.assertEquals(org.apache.ambari.server.controller.internal.ClusterKerberosDescriptorResourceProvider.class, provider.getClass());
    }
}