package org.apache.ambari.server.state.stack;
import org.apache.commons.collections.map.MultiValueMap;
public class StackRoleCommandOrder {
    private static final java.lang.String GENERAL_DEPS_KEY = "general_deps";

    private static final java.lang.String GLUSTERFS_DEPS_KEY = "optional_glusterfs";

    private static final java.lang.String NO_GLUSTERFS_DEPS_KEY = "optional_no_glusterfs";

    private static final java.lang.String NAMENODE_HA_DEPS_KEY = "namenode_optional_ha";

    private static final java.lang.String RESOURCEMANAGER_HA_DEPS_KEY = "resourcemanager_optional_ha";

    private static final java.lang.String HOST_ORDERED_UPGRADES_DEPS_KEY = "host_ordered_upgrade";

    private java.util.HashMap<java.lang.String, java.lang.Object> content;

    public StackRoleCommandOrder() {
    }

    public StackRoleCommandOrder(java.util.HashMap<java.lang.String, java.lang.Object> content) {
        this.content = content;
    }

    public java.util.HashMap<java.lang.String, java.lang.Object> getContent() {
        return content;
    }

    public void setContent(java.util.HashMap<java.lang.String, java.lang.Object> content) {
        this.content = content;
    }

    public void merge(org.apache.ambari.server.state.stack.StackRoleCommandOrder parent) {
        merge(parent, false);
    }

    public void merge(org.apache.ambari.server.state.stack.StackRoleCommandOrder parent, boolean mergeProperties) {
        java.util.HashMap<java.lang.String, java.lang.Object> mergedRoleCommandOrders = new java.util.HashMap<>();
        java.util.HashMap<java.lang.String, java.lang.Object> parentData = parent.getContent();
        java.util.List<java.lang.String> keys = java.util.Arrays.asList(org.apache.ambari.server.state.stack.StackRoleCommandOrder.GENERAL_DEPS_KEY, org.apache.ambari.server.state.stack.StackRoleCommandOrder.GLUSTERFS_DEPS_KEY, org.apache.ambari.server.state.stack.StackRoleCommandOrder.NO_GLUSTERFS_DEPS_KEY, org.apache.ambari.server.state.stack.StackRoleCommandOrder.NAMENODE_HA_DEPS_KEY, org.apache.ambari.server.state.stack.StackRoleCommandOrder.RESOURCEMANAGER_HA_DEPS_KEY, org.apache.ambari.server.state.stack.StackRoleCommandOrder.HOST_ORDERED_UPGRADES_DEPS_KEY);
        for (java.lang.String key : keys) {
            if (parentData.containsKey(key) && content.containsKey(key)) {
                java.util.Map<java.lang.String, java.lang.Object> result = new java.util.HashMap<>();
                java.util.Map<java.lang.String, java.lang.Object> parentProperties = ((java.util.Map<java.lang.String, java.lang.Object>) (parentData.get(key)));
                java.util.Map<java.lang.String, java.lang.Object> childProperties = ((java.util.Map<java.lang.String, java.lang.Object>) (content.get(key)));
                org.apache.commons.collections.map.MultiValueMap childAndParentProperties = null;
                childAndParentProperties = new org.apache.commons.collections.map.MultiValueMap();
                childAndParentProperties.putAll(childProperties);
                childAndParentProperties.putAll(parentProperties);
                for (java.lang.Object property : childAndParentProperties.keySet()) {
                    java.util.List propertyValues = ((java.util.List) (childAndParentProperties.get(property)));
                    java.lang.Object values = propertyValues.get(0);
                    if (mergeProperties) {
                        java.util.List<java.lang.String> valueList = new java.util.ArrayList<>();
                        for (java.lang.Object value : propertyValues) {
                            if (value instanceof java.util.List) {
                                valueList.addAll(((java.util.List<java.lang.String>) (value)));
                            } else {
                                valueList.add(value.toString());
                            }
                        }
                        values = valueList;
                    }
                    result.put(((java.lang.String) (property)), values);
                }
                mergedRoleCommandOrders.put(key, result);
            } else if (content.containsKey(key)) {
                mergedRoleCommandOrders.put(key, content.get(key));
            } else if (parentData.containsKey(key)) {
                mergedRoleCommandOrders.put(key, parentData.get(key));
            }
        }
        content = mergedRoleCommandOrders;
    }

    public void printRoleCommandOrder(org.slf4j.Logger LOG) {
        java.util.Map<java.lang.String, java.lang.Object> map = getContent();
        java.util.List<java.lang.String> keys = java.util.Arrays.asList(org.apache.ambari.server.state.stack.StackRoleCommandOrder.GENERAL_DEPS_KEY, org.apache.ambari.server.state.stack.StackRoleCommandOrder.GLUSTERFS_DEPS_KEY, org.apache.ambari.server.state.stack.StackRoleCommandOrder.NO_GLUSTERFS_DEPS_KEY, org.apache.ambari.server.state.stack.StackRoleCommandOrder.NAMENODE_HA_DEPS_KEY, org.apache.ambari.server.state.stack.StackRoleCommandOrder.RESOURCEMANAGER_HA_DEPS_KEY);
        for (java.lang.String key : keys) {
            LOG.debug(key);
            java.lang.Object value = map.get(key);
            if (value instanceof java.util.Map) {
                java.util.Map<java.lang.String, java.lang.Object> deps = ((java.util.Map<java.lang.String, java.lang.Object>) (map.get(key)));
                for (java.lang.String depKey : deps.keySet()) {
                    java.lang.Object depValue = deps.get(depKey);
                    if (depValue instanceof java.util.Collection) {
                        java.lang.StringBuilder buffer = new java.lang.StringBuilder();
                        for (java.lang.Object o : ((java.util.Collection) (depValue))) {
                            if (buffer.length() > 0) {
                                buffer.append(",");
                            }
                            buffer.append(o);
                        }
                        depValue = buffer.toString();
                    }
                    LOG.debug("{} => {}", depKey, depValue);
                }
            }
        }
    }
}