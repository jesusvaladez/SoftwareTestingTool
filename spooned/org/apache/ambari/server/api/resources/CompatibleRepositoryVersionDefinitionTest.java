package org.apache.ambari.server.api.resources;
import static org.apache.ambari.server.controller.internal.CompatibleRepositoryVersionResourceProvider.REPOSITORY_VERSION_ID_PROPERTY_ID;
import static org.apache.ambari.server.controller.internal.CompatibleRepositoryVersionResourceProvider.REPOSITORY_VERSION_STACK_VERSION_PROPERTY_ID;
public class CompatibleRepositoryVersionDefinitionTest {
    private static final java.lang.String ORIGINAL_HREF = "http://host/api/v1/stacks/HDP/versions/2.6/compatible_repository_versions";

    private org.apache.ambari.server.api.resources.CompatibleRepositoryVersionDefinition def = new org.apache.ambari.server.api.resources.CompatibleRepositoryVersionDefinition();

    @org.junit.Test
    public void testHrefReplace() throws java.lang.Exception {
        org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> repoVersionNode = nodeWithIdAndVersion("42", "3.0");
        postProcessHref(repoVersionNode);
        junit.framework.Assert.assertEquals("http://host/api/v1/stacks/HDP/versions/3.0/compatible_repository_versions/42", repoVersionNode.getStringProperty("href"));
    }

    private org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> nodeWithIdAndVersion(java.lang.String id, java.lang.String version) {
        org.apache.ambari.server.controller.internal.ResourceImpl resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.CompatibleRepositoryVersion);
        resource.setProperty(org.apache.ambari.server.controller.internal.CompatibleRepositoryVersionResourceProvider.REPOSITORY_VERSION_ID_PROPERTY_ID, id);
        resource.setProperty(org.apache.ambari.server.controller.internal.CompatibleRepositoryVersionResourceProvider.REPOSITORY_VERSION_STACK_VERSION_PROPERTY_ID, version);
        return new org.apache.ambari.server.api.util.TreeNodeImpl<>(null, resource, "any");
    }

    private void postProcessHref(org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> repoVersionNode) {
        org.apache.ambari.server.api.resources.ResourceDefinition.PostProcessor hrefProcessor = hrefProcessor();
        hrefProcessor.process(null, repoVersionNode, org.apache.ambari.server.api.resources.CompatibleRepositoryVersionDefinitionTest.ORIGINAL_HREF);
    }

    private org.apache.ambari.server.api.resources.ResourceDefinition.PostProcessor hrefProcessor() {
        return def.getPostProcessors().get(1);
    }
}