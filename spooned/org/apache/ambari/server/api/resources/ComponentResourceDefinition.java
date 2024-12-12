package org.apache.ambari.server.api.resources;
public class ComponentResourceDefinition extends org.apache.ambari.server.api.resources.BaseResourceDefinition {
    public ComponentResourceDefinition() {
        super(org.apache.ambari.server.controller.spi.Resource.Type.Component);
    }

    @java.lang.Override
    public java.lang.String getPluralName() {
        return "components";
    }

    @java.lang.Override
    public java.lang.String getSingularName() {
        return "component";
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.api.resources.SubResourceDefinition> getSubResourceDefinitions() {
        return java.util.Collections.singleton(new org.apache.ambari.server.api.resources.SubResourceDefinition(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent, java.util.Collections.singleton(org.apache.ambari.server.controller.spi.Resource.Type.Host), true));
    }

    @java.lang.Override
    public java.util.List<org.apache.ambari.server.api.resources.ResourceDefinition.PostProcessor> getPostProcessors() {
        java.util.List<org.apache.ambari.server.api.resources.ResourceDefinition.PostProcessor> listProcessors = super.getPostProcessors();
        listProcessors.add(new org.apache.ambari.server.api.resources.ComponentResourceDefinition.ComponentHrefProcessor());
        return listProcessors;
    }

    private class ComponentHrefProcessor extends org.apache.ambari.server.api.resources.BaseResourceDefinition.BaseHrefPostProcessor {
        @java.lang.Override
        public void process(org.apache.ambari.server.api.services.Request request, org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> resultNode, java.lang.String href) {
            org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> parent = resultNode.getParent();
            if ((parent.getParent() != null) && (parent.getParent().getObject().getType() == org.apache.ambari.server.controller.spi.Resource.Type.HostComponent)) {
                org.apache.ambari.server.controller.spi.Resource r = resultNode.getObject();
                org.apache.ambari.server.controller.spi.Schema schema = org.apache.ambari.server.controller.utilities.ClusterControllerHelper.getClusterController().getSchema(r.getType());
                java.lang.Object serviceId = r.getPropertyValue(schema.getKeyPropertyId(org.apache.ambari.server.controller.spi.Resource.Type.Service));
                java.lang.Object componentId = r.getPropertyValue(schema.getKeyPropertyId(r.getType()));
                href = (((href.substring(0, href.indexOf("/hosts/") + 1) + "services/") + serviceId) + "/components/") + componentId;
                resultNode.setProperty("href", href);
            } else {
                super.process(request, resultNode, href);
            }
        }
    }
}