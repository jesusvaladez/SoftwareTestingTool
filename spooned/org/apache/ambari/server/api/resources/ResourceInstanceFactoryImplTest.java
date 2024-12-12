package org.apache.ambari.server.api.resources;
public class ResourceInstanceFactoryImplTest {
    @org.junit.Test
    public void testGetStackArtifactDefinition() {
        org.apache.ambari.server.api.resources.ResourceDefinition resourceDefinition = org.apache.ambari.server.api.resources.ResourceInstanceFactoryImpl.getResourceDefinition(org.apache.ambari.server.controller.spi.Resource.Type.StackArtifact, null);
        org.junit.Assert.assertEquals("artifact", resourceDefinition.getSingularName());
        org.junit.Assert.assertEquals("artifacts", resourceDefinition.getPluralName());
        org.junit.Assert.assertEquals(org.apache.ambari.server.controller.spi.Resource.Type.StackArtifact, resourceDefinition.getType());
    }

    @org.junit.Test
    public void testGetArtifactDefinition() {
        org.apache.ambari.server.api.resources.ResourceDefinition resourceDefinition = org.apache.ambari.server.api.resources.ResourceInstanceFactoryImpl.getResourceDefinition(org.apache.ambari.server.controller.spi.Resource.Type.Artifact, null);
        org.junit.Assert.assertEquals("artifact", resourceDefinition.getSingularName());
        org.junit.Assert.assertEquals("artifacts", resourceDefinition.getPluralName());
        org.junit.Assert.assertEquals(org.apache.ambari.server.controller.spi.Resource.Type.Artifact, resourceDefinition.getType());
    }

    @org.junit.Test
    public void testGetHostDefinition() {
        org.apache.ambari.server.api.resources.ResourceInstanceFactoryImpl resourceInstanceFactory = new org.apache.ambari.server.api.resources.ResourceInstanceFactoryImpl();
        java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> mapIds = new java.util.HashMap<>();
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.Host, "TeSTHost1");
        org.apache.ambari.server.api.resources.ResourceInstance resourceInstance = resourceInstanceFactory.createResource(org.apache.ambari.server.controller.spi.Resource.Type.Host, mapIds);
        org.junit.Assert.assertEquals(mapIds.get(org.apache.ambari.server.controller.spi.Resource.Type.Host), "testhost1");
    }

    @org.junit.Test
    public void testGetHostKerberosIdentityDefinition() {
        org.apache.ambari.server.api.resources.ResourceDefinition resourceDefinition = org.apache.ambari.server.api.resources.ResourceInstanceFactoryImpl.getResourceDefinition(org.apache.ambari.server.controller.spi.Resource.Type.HostKerberosIdentity, null);
        org.junit.Assert.assertNotNull(resourceDefinition);
        org.junit.Assert.assertEquals("kerberos_identity", resourceDefinition.getSingularName());
        org.junit.Assert.assertEquals("kerberos_identities", resourceDefinition.getPluralName());
        org.junit.Assert.assertEquals(org.apache.ambari.server.controller.spi.Resource.Type.HostKerberosIdentity, resourceDefinition.getType());
    }

    @org.junit.Test
    public void testGetRoleAuthorizationDefinition() {
        org.apache.ambari.server.api.resources.ResourceDefinition resourceDefinition = org.apache.ambari.server.api.resources.ResourceInstanceFactoryImpl.getResourceDefinition(org.apache.ambari.server.controller.spi.Resource.Type.RoleAuthorization, null);
        org.junit.Assert.assertNotNull(resourceDefinition);
        org.junit.Assert.assertEquals("authorization", resourceDefinition.getSingularName());
        org.junit.Assert.assertEquals("authorizations", resourceDefinition.getPluralName());
        org.junit.Assert.assertEquals(org.apache.ambari.server.controller.spi.Resource.Type.RoleAuthorization, resourceDefinition.getType());
    }

    @org.junit.Test
    public void testGetUserAuthorizationDefinition() {
        org.apache.ambari.server.api.resources.ResourceDefinition resourceDefinition = org.apache.ambari.server.api.resources.ResourceInstanceFactoryImpl.getResourceDefinition(org.apache.ambari.server.controller.spi.Resource.Type.UserAuthorization, null);
        org.junit.Assert.assertNotNull(resourceDefinition);
        org.junit.Assert.assertEquals("authorization", resourceDefinition.getSingularName());
        org.junit.Assert.assertEquals("authorizations", resourceDefinition.getPluralName());
        org.junit.Assert.assertEquals(org.apache.ambari.server.controller.spi.Resource.Type.UserAuthorization, resourceDefinition.getType());
    }

    @org.junit.Test
    public void testGetClusterKerberosDescriptorDefinition() {
        org.apache.ambari.server.api.resources.ResourceDefinition resourceDefinition = org.apache.ambari.server.api.resources.ResourceInstanceFactoryImpl.getResourceDefinition(org.apache.ambari.server.controller.spi.Resource.Type.ClusterKerberosDescriptor, null);
        org.junit.Assert.assertNotNull(resourceDefinition);
        org.junit.Assert.assertEquals("kerberos_descriptor", resourceDefinition.getSingularName());
        org.junit.Assert.assertEquals("kerberos_descriptors", resourceDefinition.getPluralName());
        org.junit.Assert.assertEquals(org.apache.ambari.server.controller.spi.Resource.Type.ClusterKerberosDescriptor, resourceDefinition.getType());
    }
}