package org.apache.ambari.server.api.resources;
public class WidgetLayoutResourceDefinition extends org.apache.ambari.server.api.resources.BaseResourceDefinition {
    public WidgetLayoutResourceDefinition() {
        super(org.apache.ambari.server.controller.spi.Resource.Type.WidgetLayout);
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
        listProcessors.add(new org.apache.ambari.server.api.resources.WidgetLayoutResourceDefinition.WidgetLayoutHrefProcessor());
        return listProcessors;
    }

    private class WidgetLayoutHrefProcessor extends org.apache.ambari.server.api.resources.BaseResourceDefinition.BaseHrefPostProcessor {
        @java.lang.Override
        public void process(org.apache.ambari.server.api.services.Request request, org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> resultNode, java.lang.String href) {
            org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> parent = resultNode.getParent();
            for (org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> node : parent.getChildren()) {
                if ((node.getObject().getPropertiesMap().get("WidgetLayoutInfo") != null) && (node.getObject().getPropertiesMap().get("WidgetLayoutInfo").get("widgets") != null)) {
                    java.util.ArrayList widgetsList = ((java.util.ArrayList) (node.getObject().getPropertiesMap().get("WidgetLayoutInfo").get("widgets")));
                    for (java.lang.Object widgetObject : widgetsList) {
                        java.util.HashMap<java.lang.String, java.lang.Object> widgetMap = ((java.util.HashMap) (widgetObject));
                        if (((org.apache.ambari.server.controller.WidgetResponse) (widgetMap.get("WidgetInfo"))).getId() != null) {
                            java.lang.String widgetId = ((org.apache.ambari.server.controller.WidgetResponse) (widgetMap.get("WidgetInfo"))).getId().toString();
                            java.lang.String widgetHref = (href.substring(0, href.indexOf("/widget_layouts") + 1) + "widgets/") + widgetId;
                            widgetMap.put("href", widgetHref);
                        }
                    }
                }
            }
        }
    }
}