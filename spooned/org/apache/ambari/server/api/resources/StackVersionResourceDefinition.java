package org.apache.ambari.server.api.resources;
public class StackVersionResourceDefinition extends org.apache.ambari.server.api.resources.BaseResourceDefinition {
    public StackVersionResourceDefinition() {
        super(org.apache.ambari.server.controller.spi.Resource.Type.StackVersion);
    }

    @java.lang.Override
    public java.lang.String getPluralName() {
        return "versions";
    }

    @java.lang.Override
    public java.lang.String getSingularName() {
        return "version";
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.api.resources.SubResourceDefinition> getSubResourceDefinitions() {
        java.util.Set<org.apache.ambari.server.api.resources.SubResourceDefinition> children = new java.util.HashSet<>();
        children.add(new org.apache.ambari.server.api.resources.SubResourceDefinition(org.apache.ambari.server.controller.spi.Resource.Type.OperatingSystem));
        children.add(new org.apache.ambari.server.api.resources.SubResourceDefinition(org.apache.ambari.server.controller.spi.Resource.Type.StackService));
        children.add(new org.apache.ambari.server.api.resources.SubResourceDefinition(org.apache.ambari.server.controller.spi.Resource.Type.StackLevelConfiguration));
        children.add(new org.apache.ambari.server.api.resources.SubResourceDefinition(org.apache.ambari.server.controller.spi.Resource.Type.RepositoryVersion));
        children.add(new org.apache.ambari.server.api.resources.SubResourceDefinition(org.apache.ambari.server.controller.spi.Resource.Type.StackArtifact));
        children.add(new org.apache.ambari.server.api.resources.SubResourceDefinition(org.apache.ambari.server.controller.spi.Resource.Type.CompatibleRepositoryVersion));
        children.add(new org.apache.ambari.server.api.resources.SubResourceDefinition(org.apache.ambari.server.controller.spi.Resource.Type.Mpack, null, false));
        return children;
    }

    @java.lang.Override
    public java.util.List<org.apache.ambari.server.api.resources.ResourceDefinition.PostProcessor> getPostProcessors() {
        java.util.List<org.apache.ambari.server.api.resources.ResourceDefinition.PostProcessor> listProcessors = new java.util.ArrayList<>();
        listProcessors.add(new org.apache.ambari.server.api.resources.StackVersionResourceDefinition.StackVersionHrefProcessor());
        listProcessors.add(new org.apache.ambari.server.api.resources.StackVersionResourceDefinition.StackVersionPostProcessor());
        return listProcessors;
    }

    private class StackVersionHrefProcessor extends org.apache.ambari.server.api.resources.BaseResourceDefinition.BaseHrefPostProcessor {
        @java.lang.Override
        public void process(org.apache.ambari.server.api.services.Request request, org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> resultNode, java.lang.String href) {
            if (href.contains("/mpacks/")) {
                org.apache.ambari.server.controller.internal.ResourceImpl mpack = ((org.apache.ambari.server.controller.internal.ResourceImpl) (resultNode.getObject()));
                java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.Object>> mapInfo = mpack.getPropertiesMap();
                java.util.Map<java.lang.String, java.lang.Object> versionInfo = mapInfo.get("Versions");
                int idx = href.indexOf("mpacks/");
                java.lang.String stackName = ((java.lang.String) (versionInfo.get("stack_name")));
                java.lang.String stackVersion = ((java.lang.String) (versionInfo.get("stack_version")));
                href = (((href.substring(0, idx) + "stacks/") + stackName) + "/versions/") + stackVersion;
                resultNode.setProperty("href", href);
            } else {
                super.process(request, resultNode, href);
            }
        }
    }

    private class StackVersionPostProcessor implements org.apache.ambari.server.api.resources.ResourceDefinition.PostProcessor {
        @java.lang.Override
        public void process(org.apache.ambari.server.api.services.Request request, org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> resultNode, java.lang.String href) {
            if (href.contains("/mpacks/")) {
                resultNode.setName("stack");
            }
        }
    }
}