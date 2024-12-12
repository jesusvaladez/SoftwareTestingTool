package org.apache.ambari.server.api.resources;
public class ConfigurationResourceDefinition extends org.apache.ambari.server.api.resources.BaseResourceDefinition {
    public ConfigurationResourceDefinition() {
        super(org.apache.ambari.server.controller.spi.Resource.Type.Configuration);
    }

    @java.lang.Override
    public java.util.List<org.apache.ambari.server.api.resources.ResourceDefinition.PostProcessor> getPostProcessors() {
        java.util.List<org.apache.ambari.server.api.resources.ResourceDefinition.PostProcessor> listProcessors = super.getPostProcessors();
        listProcessors.add(new org.apache.ambari.server.api.resources.ConfigurationResourceDefinition.HrefProcessor());
        return listProcessors;
    }

    @java.lang.Override
    public java.lang.String getPluralName() {
        return "configurations";
    }

    @java.lang.Override
    public java.lang.String getSingularName() {
        return "configuration";
    }

    private class HrefProcessor extends org.apache.ambari.server.api.resources.BaseResourceDefinition.BaseHrefPostProcessor {
        @java.lang.Override
        public void process(org.apache.ambari.server.api.services.Request request, org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> resultNode, java.lang.String href) {
            if (resultNode.getObject().getType() == org.apache.ambari.server.controller.spi.Resource.Type.Configuration) {
                if (!href.endsWith("/")) {
                    href += '/';
                }
                java.lang.String clustersToken = "/clusters";
                int idx = (href.indexOf(clustersToken) + clustersToken.length()) + 1;
                idx = href.indexOf("/", idx) + 1;
                java.lang.String type = ((java.lang.String) (resultNode.getObject().getPropertyValue("type")));
                java.lang.String tag = ((java.lang.String) (resultNode.getObject().getPropertyValue("tag")));
                href = (((href.substring(0, idx) + "configurations?type=") + type) + "&tag=") + tag;
                resultNode.setProperty("href", href);
            } else {
                super.process(request, resultNode, href);
            }
        }
    }
}