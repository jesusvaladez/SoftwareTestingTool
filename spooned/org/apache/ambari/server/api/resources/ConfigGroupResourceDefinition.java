package org.apache.ambari.server.api.resources;
public class ConfigGroupResourceDefinition extends org.apache.ambari.server.api.resources.BaseResourceDefinition {
    public ConfigGroupResourceDefinition() {
        super(org.apache.ambari.server.controller.spi.Resource.Type.ConfigGroup);
    }

    @java.lang.Override
    public java.lang.String getPluralName() {
        return "config_groups";
    }

    @java.lang.Override
    public java.lang.String getSingularName() {
        return "config_group";
    }

    @java.lang.Override
    public java.util.List<org.apache.ambari.server.api.resources.ResourceDefinition.PostProcessor> getPostProcessors() {
        java.util.List<org.apache.ambari.server.api.resources.ResourceDefinition.PostProcessor> listProcessors = super.getPostProcessors();
        listProcessors.add(new org.apache.ambari.server.api.resources.ConfigGroupResourceDefinition.ConfigGroupHrefProcessor());
        return listProcessors;
    }

    private class ConfigGroupHrefProcessor extends org.apache.ambari.server.api.resources.BaseResourceDefinition.BaseHrefPostProcessor {
        @java.lang.Override
        public void process(org.apache.ambari.server.api.services.Request request, org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> resultNode, java.lang.String href) {
            if (resultNode.getObject().getType() == org.apache.ambari.server.controller.spi.Resource.Type.ConfigGroup) {
                org.apache.ambari.server.controller.spi.Resource r = resultNode.getObject();
                org.apache.ambari.server.controller.spi.Schema schema = org.apache.ambari.server.controller.utilities.ClusterControllerHelper.getClusterController().getSchema(r.getType());
                java.lang.Object clusterId = r.getPropertyValue(schema.getKeyPropertyId(org.apache.ambari.server.controller.spi.Resource.Type.Cluster));
                java.util.Map<java.lang.String, java.lang.Object> configGroup = r.getPropertiesMap().get("ConfigGroup");
                java.lang.String partialUrl = href.substring(0, href.indexOf("/clusters/") + "/clusters/".length()) + clusterId;
                for (java.util.Map.Entry<java.lang.String, java.lang.Object> entry : configGroup.entrySet()) {
                    if (entry.getKey().contains("hosts") && (entry.getValue() != null)) {
                        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> hostSet = ((java.util.Set<java.util.Map<java.lang.String, java.lang.Object>>) (entry.getValue()));
                        for (java.util.Map<java.lang.String, java.lang.Object> hostMap : hostSet) {
                            java.lang.String idx = (partialUrl + "/hosts/") + hostMap.get("host_name");
                            hostMap.put("href", idx);
                        }
                    } else if (entry.getKey().contains("desired_configs") && (entry.getValue() != null)) {
                        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> configSet = ((java.util.Set<java.util.Map<java.lang.String, java.lang.Object>>) (entry.getValue()));
                        for (java.util.Map<java.lang.String, java.lang.Object> configMap : configSet) {
                            java.lang.String idx = ((((partialUrl + "/configurations?") + "type=") + configMap.get("type")) + "&tag=") + configMap.get("tag");
                            configMap.put("href", idx);
                        }
                    }
                }
            } else {
                super.process(request, resultNode, href);
            }
        }
    }
}