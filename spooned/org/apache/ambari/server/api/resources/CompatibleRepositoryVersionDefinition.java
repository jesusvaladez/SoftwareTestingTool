package org.apache.ambari.server.api.resources;
import static org.apache.ambari.server.controller.internal.CompatibleRepositoryVersionResourceProvider.REPOSITORY_VERSION_ID_PROPERTY_ID;
import static org.apache.ambari.server.controller.internal.CompatibleRepositoryVersionResourceProvider.REPOSITORY_VERSION_STACK_VERSION_PROPERTY_ID;
public class CompatibleRepositoryVersionDefinition extends org.apache.ambari.server.api.resources.SimpleResourceDefinition {
    public CompatibleRepositoryVersionDefinition() {
        super(org.apache.ambari.server.controller.spi.Resource.Type.CompatibleRepositoryVersion, "compatible_repository_version", "compatible_repository_versions", org.apache.ambari.server.controller.spi.Resource.Type.OperatingSystem);
    }

    @java.lang.Override
    public java.util.List<org.apache.ambari.server.api.resources.ResourceDefinition.PostProcessor> getPostProcessors() {
        java.util.List<org.apache.ambari.server.api.resources.ResourceDefinition.PostProcessor> listProcessors = super.getPostProcessors();
        listProcessors.add(new org.apache.ambari.server.api.resources.CompatibleRepositoryVersionDefinition.CompatibleRepositoryVersionHrefProcessor());
        return listProcessors;
    }

    private class CompatibleRepositoryVersionHrefProcessor extends org.apache.ambari.server.api.resources.BaseResourceDefinition.BaseHrefPostProcessor {
        @java.lang.Override
        public void process(org.apache.ambari.server.api.services.Request request, org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> resultNode, java.lang.String href) {
            if (resultNode.getObject().getType() == org.apache.ambari.server.controller.spi.Resource.Type.CompatibleRepositoryVersion) {
                org.apache.ambari.server.controller.spi.Resource node = resultNode.getObject();
                java.lang.Object id = node.getPropertyValue(org.apache.ambari.server.controller.internal.CompatibleRepositoryVersionResourceProvider.REPOSITORY_VERSION_ID_PROPERTY_ID);
                java.lang.Object stackVersion = node.getPropertyValue(org.apache.ambari.server.controller.internal.CompatibleRepositoryVersionResourceProvider.REPOSITORY_VERSION_STACK_VERSION_PROPERTY_ID);
                if ((id != null) && (stackVersion != null)) {
                    resultNode.setProperty("href", fixHref(href, id, stackVersion));
                    return;
                }
            }
            super.process(request, resultNode, href);
        }

        private java.lang.String fixHref(java.lang.String href, java.lang.Object id, java.lang.Object stackVersion) {
            href = href.replaceAll("/versions/[^/]+/", ("/versions/" + stackVersion) + "/");
            if (!href.endsWith("/" + id)) {
                href += "/" + id;
            }
            return href;
        }
    }
}