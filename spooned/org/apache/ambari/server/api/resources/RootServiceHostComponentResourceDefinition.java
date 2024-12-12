package org.apache.ambari.server.api.resources;
public class RootServiceHostComponentResourceDefinition extends org.apache.ambari.server.api.resources.BaseResourceDefinition {
    public RootServiceHostComponentResourceDefinition(org.apache.ambari.server.controller.spi.Resource.Type resourceType) {
        super(resourceType);
    }

    public RootServiceHostComponentResourceDefinition() {
        super(org.apache.ambari.server.controller.spi.Resource.Type.RootServiceHostComponent);
    }

    @java.lang.Override
    public java.lang.String getPluralName() {
        return "hostComponents";
    }

    @java.lang.Override
    public java.lang.String getSingularName() {
        return "hostComponent";
    }

    @java.lang.Override
    public java.util.List<org.apache.ambari.server.api.resources.ResourceDefinition.PostProcessor> getPostProcessors() {
        java.util.List<org.apache.ambari.server.api.resources.ResourceDefinition.PostProcessor> listProcessors = new java.util.ArrayList<>();
        listProcessors.add(new org.apache.ambari.server.api.resources.RootServiceHostComponentResourceDefinition.RootServiceHostComponentHrefProcessor());
        return listProcessors;
    }

    private class RootServiceHostComponentHrefProcessor extends org.apache.ambari.server.api.resources.BaseResourceDefinition.BaseHrefPostProcessor {
        @java.lang.Override
        public void process(org.apache.ambari.server.api.services.Request request, org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> resultNode, java.lang.String href) {
            org.apache.ambari.server.controller.spi.Resource r = resultNode.getObject();
            org.apache.ambari.server.controller.spi.Schema schema = org.apache.ambari.server.controller.utilities.ClusterControllerHelper.getClusterController().getSchema(r.getType());
            java.lang.Object host = r.getPropertyValue(schema.getKeyPropertyId(org.apache.ambari.server.controller.spi.Resource.Type.Host));
            java.lang.Object hostComponent = r.getPropertyValue(schema.getKeyPropertyId(r.getType()));
            int idx = href.indexOf("services/") + "services/".length();
            idx = href.indexOf("/", idx) + 1;
            href = (((href.substring(0, idx) + "hosts/") + host) + "/hostComponents/") + hostComponent;
            resultNode.setProperty("href", href);
        }
    }
}