package org.apache.ambari.server.api.resources;
public class ActiveWidgetLayoutResourceDefinition extends org.apache.ambari.server.api.resources.BaseResourceDefinition {
    public ActiveWidgetLayoutResourceDefinition() {
        super(org.apache.ambari.server.controller.spi.Resource.Type.ActiveWidgetLayout);
    }

    @java.lang.Override
    public java.lang.String getPluralName() {
        return "widget_layouts";
    }

    @java.lang.Override
    public java.lang.String getSingularName() {
        return "widget_layout";
    }

    @java.lang.Override
    public java.util.List<org.apache.ambari.server.api.resources.ResourceDefinition.PostProcessor> getPostProcessors() {
        java.util.List<org.apache.ambari.server.api.resources.ResourceDefinition.PostProcessor> listProcessors = super.getPostProcessors();
        listProcessors.add(new org.apache.ambari.server.api.resources.ActiveWidgetLayoutResourceDefinition.WidgetLayoutHrefProcessor());
        return listProcessors;
    }

    private class WidgetLayoutHrefProcessor extends org.apache.ambari.server.api.resources.BaseResourceDefinition.BaseHrefPostProcessor {
        @java.lang.Override
        public void process(org.apache.ambari.server.api.services.Request request, org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> resultNode, java.lang.String href) {
            org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> parent = resultNode.getParent();
            for (org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> node : parent.getChildren()) {
                if (node.getObject().getPropertiesMap().get("WidgetLayoutInfo") != null) {
                    java.lang.String layoutId = resultNode.getObject().getPropertyValue("WidgetLayoutInfo/id").toString();
                    java.lang.String clusterName = resultNode.getObject().getPropertyValue("WidgetLayoutInfo/cluster_name").toString();
                    java.lang.String newHref = (((href.substring(0, href.indexOf("/users") + 1) + "clusters/") + clusterName) + "/widget_layouts/") + layoutId;
                    resultNode.setProperty("href", newHref);
                }
                if ((node.getObject().getPropertiesMap().get("WidgetLayoutInfo") != null) && (node.getObject().getPropertiesMap().get("WidgetLayoutInfo").get("WidgetInfo") != null)) {
                    java.util.ArrayList widgetsList = ((java.util.ArrayList) (node.getObject().getPropertiesMap().get("WidgetLayoutInfo").get("WidgetInfo")));
                    for (java.lang.Object widgetObject : widgetsList) {
                        java.util.HashMap<java.lang.String, java.lang.Object> widgetMap = ((java.util.HashMap) (widgetObject));
                        java.lang.String widgetId = ((org.apache.ambari.server.controller.WidgetResponse) (widgetMap.get("Widget"))).getId().toString();
                        java.lang.String clusterName = ((org.apache.ambari.server.controller.WidgetResponse) (widgetMap.get("Widget"))).getClusterName().toString();
                        java.lang.String widgetHref = (((href.substring(0, href.indexOf("/users") + 1) + "clusters/") + clusterName) + "/widgets/") + widgetId;
                        widgetMap.put("href", widgetHref);
                    }
                }
            }
        }
    }
}