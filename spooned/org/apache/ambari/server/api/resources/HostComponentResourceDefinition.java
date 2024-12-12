package org.apache.ambari.server.api.resources;
public class HostComponentResourceDefinition extends org.apache.ambari.server.api.resources.BaseResourceDefinition {
    public HostComponentResourceDefinition() {
        super(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent);
    }

    @java.lang.Override
    public java.lang.String getPluralName() {
        return "host_components";
    }

    @java.lang.Override
    public java.lang.String getSingularName() {
        return "host_component";
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.api.resources.SubResourceDefinition> getSubResourceDefinitions() {
        java.util.Set<org.apache.ambari.server.api.resources.SubResourceDefinition> setSubResources = new java.util.HashSet<>();
        setSubResources.add(new org.apache.ambari.server.api.resources.SubResourceDefinition(org.apache.ambari.server.controller.spi.Resource.Type.Component, java.util.Collections.singleton(org.apache.ambari.server.controller.spi.Resource.Type.Service), false));
        setSubResources.add(new org.apache.ambari.server.api.resources.SubResourceDefinition(org.apache.ambari.server.controller.spi.Resource.Type.HostComponentProcess));
        return setSubResources;
    }

    @java.lang.Override
    public java.util.List<org.apache.ambari.server.api.resources.ResourceDefinition.PostProcessor> getPostProcessors() {
        java.util.List<org.apache.ambari.server.api.resources.ResourceDefinition.PostProcessor> listProcessors = new java.util.ArrayList<>();
        listProcessors.add(new org.apache.ambari.server.api.resources.HostComponentResourceDefinition.HostComponentHrefProcessor());
        listProcessors.add(new org.apache.ambari.server.api.resources.HostComponentResourceDefinition.HostComponentHostProcessor());
        return listProcessors;
    }

    private class HostComponentHrefProcessor extends org.apache.ambari.server.api.resources.BaseResourceDefinition.BaseHrefPostProcessor {
        @java.lang.Override
        public void process(org.apache.ambari.server.api.services.Request request, org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> resultNode, java.lang.String href) {
            if (!href.contains("/hosts/")) {
                org.apache.ambari.server.controller.spi.Resource r = resultNode.getObject();
                org.apache.ambari.server.controller.spi.Schema schema = org.apache.ambari.server.controller.utilities.ClusterControllerHelper.getClusterController().getSchema(r.getType());
                java.lang.Object host = r.getPropertyValue(schema.getKeyPropertyId(org.apache.ambari.server.controller.spi.Resource.Type.Host));
                java.lang.Object hostComponent = r.getPropertyValue(schema.getKeyPropertyId(r.getType()));
                int idx = href.indexOf("clusters/") + "clusters/".length();
                idx = href.indexOf("/", idx) + 1;
                href = (((href.substring(0, idx) + "hosts/") + host) + "/host_components/") + hostComponent;
                resultNode.setProperty("href", href);
            } else {
                super.process(request, resultNode, href);
            }
        }
    }

    private class HostComponentHostProcessor implements org.apache.ambari.server.api.resources.ResourceDefinition.PostProcessor {
        @java.lang.Override
        public void process(org.apache.ambari.server.api.services.Request request, org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> resultNode, java.lang.String href) {
            if (request.getResource().getResourceDefinition().getType() == getType()) {
                java.lang.String nodeHref = resultNode.getStringProperty("href");
                resultNode.getObject().setProperty(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("host", "href"), nodeHref.substring(0, nodeHref.indexOf("/host_components/")));
            }
        }
    }
}