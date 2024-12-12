package org.apache.ambari.server.api.resources;
public class MpackResourceDefinition extends org.apache.ambari.server.api.resources.BaseResourceDefinition {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.api.resources.MpackResourceDefinition.class);

    public MpackResourceDefinition() {
        super(org.apache.ambari.server.controller.spi.Resource.Type.Mpack);
    }

    @java.lang.Override
    public java.lang.String getPluralName() {
        return "mpacks";
    }

    @java.lang.Override
    public java.lang.String getSingularName() {
        return "mpack";
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.api.resources.SubResourceDefinition> getSubResourceDefinitions() {
        java.util.Set<org.apache.ambari.server.api.resources.SubResourceDefinition> setChildren = new java.util.HashSet<>();
        setChildren.add(new org.apache.ambari.server.api.resources.SubResourceDefinition(org.apache.ambari.server.controller.spi.Resource.Type.StackVersion, null, false));
        return setChildren;
    }

    @java.lang.Override
    public java.util.List<org.apache.ambari.server.api.resources.ResourceDefinition.PostProcessor> getPostProcessors() {
        java.util.List<org.apache.ambari.server.api.resources.ResourceDefinition.PostProcessor> listProcessors = new java.util.ArrayList<>();
        listProcessors.add(new org.apache.ambari.server.api.resources.MpackResourceDefinition.MpackHrefProcessor());
        listProcessors.add(new org.apache.ambari.server.api.resources.MpackResourceDefinition.MpackPostProcessor());
        return listProcessors;
    }

    private class MpackHrefProcessor extends org.apache.ambari.server.api.resources.BaseResourceDefinition.BaseHrefPostProcessor {
        @java.lang.Override
        public void process(org.apache.ambari.server.api.services.Request request, org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> resultNode, java.lang.String href) {
            if (href.contains("/stacks/")) {
                org.apache.ambari.server.controller.internal.ResourceImpl mpack = ((org.apache.ambari.server.controller.internal.ResourceImpl) (resultNode.getObject()));
                java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.Object>> mapInfo = mpack.getPropertiesMap();
                java.util.Map<java.lang.String, java.lang.Object> mpackInfo = mapInfo.get("MpackInfo");
                int idx = href.indexOf("stacks/");
                java.lang.Long mpackId = ((java.lang.Long) (mpackInfo.get("id")));
                href = (href.substring(0, idx) + "mpacks/") + mpackId;
                resultNode.setProperty("href", href);
            } else {
                super.process(request, resultNode, href);
            }
        }
    }

    private class MpackPostProcessor implements org.apache.ambari.server.api.resources.ResourceDefinition.PostProcessor {
        @java.lang.Override
        public void process(org.apache.ambari.server.api.services.Request request, org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> resultNode, java.lang.String href) {
            if (href.contains("/stacks/")) {
                resultNode.setName("mpack");
            }
        }
    }
}