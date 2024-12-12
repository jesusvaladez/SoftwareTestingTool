package org.apache.ambari.server.api.resources;
public class ServiceConfigVersionResourceDefinition extends org.apache.ambari.server.api.resources.BaseResourceDefinition {
    public ServiceConfigVersionResourceDefinition() {
        super(org.apache.ambari.server.controller.spi.Resource.Type.ServiceConfigVersion);
    }

    @java.lang.Override
    public java.util.List<org.apache.ambari.server.api.resources.ResourceDefinition.PostProcessor> getPostProcessors() {
        java.util.List<org.apache.ambari.server.api.resources.ResourceDefinition.PostProcessor> listProcessors = super.getPostProcessors();
        listProcessors.add(new org.apache.ambari.server.api.resources.ServiceConfigVersionResourceDefinition.HrefProcessor());
        return listProcessors;
    }

    @java.lang.Override
    public java.lang.String getPluralName() {
        return "service_config_versions";
    }

    @java.lang.Override
    public java.lang.String getSingularName() {
        return "service_config_version";
    }

    private class HrefProcessor extends org.apache.ambari.server.api.resources.BaseResourceDefinition.BaseHrefPostProcessor {
        @java.lang.Override
        public void process(org.apache.ambari.server.api.services.Request request, org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> resultNode, java.lang.String href) {
            if (resultNode.getObject().getType() == org.apache.ambari.server.controller.spi.Resource.Type.ServiceConfigVersion) {
                if (!href.endsWith("/")) {
                    href += '/';
                }
                java.lang.String clustersToken = "/clusters";
                int idx = (href.indexOf(clustersToken) + clustersToken.length()) + 1;
                idx = href.indexOf("/", idx) + 1;
                java.lang.String serviceName = ((java.lang.String) (resultNode.getObject().getPropertyValue("service_name")));
                java.lang.Long version = ((java.lang.Long) (resultNode.getObject().getPropertyValue("service_config_version")));
                href = (((href.substring(0, idx) + "configurations/service_config_versions?service_name=") + serviceName) + "&service_config_version=") + version;
                resultNode.setProperty("href", href);
            } else {
                super.process(request, resultNode, href);
            }
        }
    }
}