package org.apache.ambari.server.controller.internal;
import org.easymock.EasyMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
public class RepositoryResourceProviderTest {
    private static final java.lang.String VAL_STACK_NAME = "HDP";

    private static final java.lang.String VAL_STACK_VERSION = "0.2";

    private static final java.lang.String VAL_OS = "centos6";

    private static final java.lang.String VAL_REPO_ID = "HDP-0.2";

    private static final java.lang.String VAL_REPO_NAME = "HDP1";

    private static final java.lang.String VAL_BASE_URL = "http://foo.com";

    private static final java.lang.String VAL_DISTRIBUTION = "mydist";

    private static final java.lang.String VAL_COMPONENT_NAME = "mycomponentname";

    @org.junit.Test
    public void testGetResources() throws java.lang.Exception {
        org.apache.ambari.server.controller.AmbariManagementController managementController = org.easymock.EasyMock.createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.controller.RepositoryResponse rr = new org.apache.ambari.server.controller.RepositoryResponse(org.apache.ambari.server.controller.internal.RepositoryResourceProviderTest.VAL_BASE_URL, org.apache.ambari.server.controller.internal.RepositoryResourceProviderTest.VAL_OS, org.apache.ambari.server.controller.internal.RepositoryResourceProviderTest.VAL_REPO_ID, org.apache.ambari.server.controller.internal.RepositoryResourceProviderTest.VAL_REPO_NAME, org.apache.ambari.server.controller.internal.RepositoryResourceProviderTest.VAL_DISTRIBUTION, org.apache.ambari.server.controller.internal.RepositoryResourceProviderTest.VAL_COMPONENT_NAME, null, null, java.util.Collections.<org.apache.ambari.server.state.stack.RepoTag>emptySet(), java.util.Collections.emptyList());
        rr.setStackName(org.apache.ambari.server.controller.internal.RepositoryResourceProviderTest.VAL_STACK_NAME);
        rr.setStackVersion(org.apache.ambari.server.controller.internal.RepositoryResourceProviderTest.VAL_STACK_VERSION);
        java.util.Set<org.apache.ambari.server.controller.RepositoryResponse> allResponse = new java.util.HashSet<>();
        allResponse.add(rr);
        EasyMock.expect(managementController.getRepositories(org.easymock.EasyMock.anyObject())).andReturn(allResponse).times(2);
        EasyMock.replay(managementController);
        org.apache.ambari.server.controller.spi.ResourceProvider provider = new org.apache.ambari.server.controller.internal.RepositoryResourceProvider(managementController);
        java.util.Set<java.lang.String> propertyIds = new java.util.HashSet<>();
        propertyIds.add(org.apache.ambari.server.controller.internal.RepositoryResourceProvider.REPOSITORY_STACK_NAME_PROPERTY_ID);
        propertyIds.add(org.apache.ambari.server.controller.internal.RepositoryResourceProvider.REPOSITORY_STACK_VERSION_PROPERTY_ID);
        propertyIds.add(org.apache.ambari.server.controller.internal.RepositoryResourceProvider.REPOSITORY_REPO_NAME_PROPERTY_ID);
        propertyIds.add(org.apache.ambari.server.controller.internal.RepositoryResourceProvider.REPOSITORY_BASE_URL_PROPERTY_ID);
        propertyIds.add(org.apache.ambari.server.controller.internal.RepositoryResourceProvider.REPOSITORY_OS_TYPE_PROPERTY_ID);
        propertyIds.add(org.apache.ambari.server.controller.internal.RepositoryResourceProvider.REPOSITORY_REPO_ID_PROPERTY_ID);
        propertyIds.add(org.apache.ambari.server.controller.internal.RepositoryResourceProvider.REPOSITORY_CLUSTER_STACK_VERSION_PROPERTY_ID);
        propertyIds.add(org.apache.ambari.server.controller.internal.RepositoryResourceProvider.REPOSITORY_DISTRIBUTION_PROPERTY_ID);
        propertyIds.add(org.apache.ambari.server.controller.internal.RepositoryResourceProvider.REPOSITORY_COMPONENTS_PROPERTY_ID);
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.RepositoryResourceProvider.REPOSITORY_STACK_NAME_PROPERTY_ID).equals(org.apache.ambari.server.controller.internal.RepositoryResourceProviderTest.VAL_STACK_NAME).and().property(org.apache.ambari.server.controller.internal.RepositoryResourceProvider.REPOSITORY_STACK_VERSION_PROPERTY_ID).equals(org.apache.ambari.server.controller.internal.RepositoryResourceProviderTest.VAL_STACK_VERSION).toPredicate();
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(propertyIds);
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = provider.getResources(request, predicate);
        org.junit.Assert.assertEquals(allResponse.size(), resources.size());
        for (org.apache.ambari.server.controller.spi.Resource resource : resources) {
            java.lang.Object o = resource.getPropertyValue(org.apache.ambari.server.controller.internal.RepositoryResourceProvider.REPOSITORY_STACK_NAME_PROPERTY_ID);
            org.junit.Assert.assertEquals(org.apache.ambari.server.controller.internal.RepositoryResourceProviderTest.VAL_STACK_NAME, o);
            o = resource.getPropertyValue(org.apache.ambari.server.controller.internal.RepositoryResourceProvider.REPOSITORY_STACK_VERSION_PROPERTY_ID);
            org.junit.Assert.assertEquals(org.apache.ambari.server.controller.internal.RepositoryResourceProviderTest.VAL_STACK_VERSION, o);
            o = resource.getPropertyValue(org.apache.ambari.server.controller.internal.RepositoryResourceProvider.REPOSITORY_REPO_NAME_PROPERTY_ID);
            org.junit.Assert.assertEquals(o, org.apache.ambari.server.controller.internal.RepositoryResourceProviderTest.VAL_REPO_NAME);
            o = resource.getPropertyValue(org.apache.ambari.server.controller.internal.RepositoryResourceProvider.REPOSITORY_BASE_URL_PROPERTY_ID);
            org.junit.Assert.assertEquals(o, org.apache.ambari.server.controller.internal.RepositoryResourceProviderTest.VAL_BASE_URL);
            o = resource.getPropertyValue(org.apache.ambari.server.controller.internal.RepositoryResourceProvider.REPOSITORY_OS_TYPE_PROPERTY_ID);
            org.junit.Assert.assertEquals(o, org.apache.ambari.server.controller.internal.RepositoryResourceProviderTest.VAL_OS);
            o = resource.getPropertyValue(org.apache.ambari.server.controller.internal.RepositoryResourceProvider.REPOSITORY_REPO_ID_PROPERTY_ID);
            org.junit.Assert.assertEquals(o, org.apache.ambari.server.controller.internal.RepositoryResourceProviderTest.VAL_REPO_ID);
            o = resource.getPropertyValue(org.apache.ambari.server.controller.internal.RepositoryResourceProvider.REPOSITORY_CLUSTER_STACK_VERSION_PROPERTY_ID);
            org.junit.Assert.assertNull(o);
            o = resource.getPropertyValue(org.apache.ambari.server.controller.internal.RepositoryResourceProvider.REPOSITORY_DISTRIBUTION_PROPERTY_ID);
            org.junit.Assert.assertEquals(o, org.apache.ambari.server.controller.internal.RepositoryResourceProviderTest.VAL_DISTRIBUTION);
            o = resource.getPropertyValue(org.apache.ambari.server.controller.internal.RepositoryResourceProvider.REPOSITORY_COMPONENTS_PROPERTY_ID);
            org.junit.Assert.assertEquals(o, org.apache.ambari.server.controller.internal.RepositoryResourceProviderTest.VAL_COMPONENT_NAME);
        }
        rr.setClusterVersionId(525L);
        resources = provider.getResources(request, predicate);
        org.junit.Assert.assertEquals(allResponse.size(), resources.size());
        for (org.apache.ambari.server.controller.spi.Resource resource : resources) {
            java.lang.Object o = resource.getPropertyValue(org.apache.ambari.server.controller.internal.RepositoryResourceProvider.REPOSITORY_STACK_NAME_PROPERTY_ID);
            org.junit.Assert.assertEquals(org.apache.ambari.server.controller.internal.RepositoryResourceProviderTest.VAL_STACK_NAME, o);
            o = resource.getPropertyValue(org.apache.ambari.server.controller.internal.RepositoryResourceProvider.REPOSITORY_STACK_VERSION_PROPERTY_ID);
            org.junit.Assert.assertEquals(org.apache.ambari.server.controller.internal.RepositoryResourceProviderTest.VAL_STACK_VERSION, o);
            o = resource.getPropertyValue(org.apache.ambari.server.controller.internal.RepositoryResourceProvider.REPOSITORY_REPO_NAME_PROPERTY_ID);
            org.junit.Assert.assertEquals(o, org.apache.ambari.server.controller.internal.RepositoryResourceProviderTest.VAL_REPO_NAME);
            o = resource.getPropertyValue(org.apache.ambari.server.controller.internal.RepositoryResourceProvider.REPOSITORY_BASE_URL_PROPERTY_ID);
            org.junit.Assert.assertEquals(o, org.apache.ambari.server.controller.internal.RepositoryResourceProviderTest.VAL_BASE_URL);
            o = resource.getPropertyValue(org.apache.ambari.server.controller.internal.RepositoryResourceProvider.REPOSITORY_OS_TYPE_PROPERTY_ID);
            org.junit.Assert.assertEquals(o, org.apache.ambari.server.controller.internal.RepositoryResourceProviderTest.VAL_OS);
            o = resource.getPropertyValue(org.apache.ambari.server.controller.internal.RepositoryResourceProvider.REPOSITORY_REPO_ID_PROPERTY_ID);
            org.junit.Assert.assertEquals(o, org.apache.ambari.server.controller.internal.RepositoryResourceProviderTest.VAL_REPO_ID);
            o = resource.getPropertyValue(org.apache.ambari.server.controller.internal.RepositoryResourceProvider.REPOSITORY_CLUSTER_STACK_VERSION_PROPERTY_ID);
            org.junit.Assert.assertEquals(525L, o);
            o = resource.getPropertyValue(org.apache.ambari.server.controller.internal.RepositoryResourceProvider.REPOSITORY_DISTRIBUTION_PROPERTY_ID);
            org.junit.Assert.assertEquals(o, org.apache.ambari.server.controller.internal.RepositoryResourceProviderTest.VAL_DISTRIBUTION);
            o = resource.getPropertyValue(org.apache.ambari.server.controller.internal.RepositoryResourceProvider.REPOSITORY_COMPONENTS_PROPERTY_ID);
            org.junit.Assert.assertEquals(o, org.apache.ambari.server.controller.internal.RepositoryResourceProviderTest.VAL_COMPONENT_NAME);
        }
        EasyMock.verify(managementController);
    }

    @org.junit.Test
    public void testUpdateResources() throws java.lang.Exception {
        org.apache.ambari.server.controller.spi.Resource.Type type = org.apache.ambari.server.controller.spi.Resource.Type.Repository;
        org.apache.ambari.server.controller.AmbariManagementController managementController = org.easymock.EasyMock.createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.controller.RepositoryResponse rr = new org.apache.ambari.server.controller.RepositoryResponse(org.apache.ambari.server.controller.internal.RepositoryResourceProviderTest.VAL_BASE_URL, org.apache.ambari.server.controller.internal.RepositoryResourceProviderTest.VAL_OS, org.apache.ambari.server.controller.internal.RepositoryResourceProviderTest.VAL_REPO_ID, org.apache.ambari.server.controller.internal.RepositoryResourceProviderTest.VAL_REPO_NAME, null, null, null, null, java.util.Collections.<org.apache.ambari.server.state.stack.RepoTag>emptySet(), java.util.Collections.emptyList());
        java.util.Set<org.apache.ambari.server.controller.RepositoryResponse> allResponse = new java.util.HashSet<>();
        allResponse.add(rr);
        EasyMock.expect(managementController.getRepositories(org.easymock.EasyMock.anyObject())).andReturn(allResponse).times(1);
        managementController.verifyRepositories(org.easymock.EasyMock.<java.util.Set<org.apache.ambari.server.controller.RepositoryRequest>>anyObject());
        EasyMock.replay(managementController);
        org.apache.ambari.server.controller.spi.ResourceProvider provider = new org.apache.ambari.server.controller.internal.RepositoryResourceProvider(managementController);
        java.util.Map<java.lang.String, java.lang.Object> properties = new java.util.LinkedHashMap<>();
        properties.put(org.apache.ambari.server.controller.internal.RepositoryResourceProvider.REPOSITORY_BASE_URL_PROPERTY_ID, "http://garbage.eu.co");
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getUpdateRequest(properties, null);
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.RepositoryResourceProvider.REPOSITORY_STACK_NAME_PROPERTY_ID).equals(org.apache.ambari.server.controller.internal.RepositoryResourceProviderTest.VAL_STACK_NAME).and().property(org.apache.ambari.server.controller.internal.RepositoryResourceProvider.REPOSITORY_STACK_VERSION_PROPERTY_ID).equals(org.apache.ambari.server.controller.internal.RepositoryResourceProviderTest.VAL_STACK_VERSION).toPredicate();
        provider.updateResources(request, predicate);
        EasyMock.verify(managementController);
    }
}