package org.apache.ambari.server.state;
import org.apache.commons.collections.CollectionUtils;
@com.google.inject.Singleton
public class ConfigMergeHelper {
    private static final java.util.regex.Pattern HEAP_PATTERN = java.util.regex.Pattern.compile("(\\d+)([mgMG])");

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.state.ConfigMergeHelper.class);

    @com.google.inject.Inject
    private com.google.inject.Provider<org.apache.ambari.server.state.Clusters> m_clusters;

    @com.google.inject.Inject
    private com.google.inject.Provider<org.apache.ambari.server.api.services.AmbariMetaInfo> m_ambariMetaInfo;

    @java.lang.SuppressWarnings("unchecked")
    public java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.state.ConfigMergeHelper.ThreeWayValue>> getConflicts(java.lang.String clusterName, org.apache.ambari.server.state.StackId targetStack) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.Cluster cluster = m_clusters.get().getCluster(clusterName);
        org.apache.ambari.server.state.StackId oldStack = cluster.getCurrentStackVersion();
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> oldMap = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> newMap = new java.util.HashMap<>();
        for (java.lang.String serviceName : cluster.getServices().keySet()) {
            try {
                java.util.Set<org.apache.ambari.server.state.PropertyInfo> newStackProperties = m_ambariMetaInfo.get().getServiceProperties(targetStack.getStackName(), targetStack.getStackVersion(), serviceName);
                addToMap(newMap, newStackProperties);
            } catch (org.apache.ambari.server.StackAccessException e) {
                org.apache.ambari.server.state.ConfigMergeHelper.LOG.info("Skipping service {} which is currently installed but does not exist in the target stack {}", serviceName, targetStack);
                continue;
            }
            java.util.Set<org.apache.ambari.server.state.PropertyInfo> oldStackProperties = m_ambariMetaInfo.get().getServiceProperties(oldStack.getStackName(), oldStack.getStackVersion(), serviceName);
            addToMap(oldMap, oldStackProperties);
        }
        java.util.Set<org.apache.ambari.server.state.PropertyInfo> set = m_ambariMetaInfo.get().getStackProperties(oldStack.getStackName(), oldStack.getStackVersion());
        addToMap(oldMap, set);
        set = m_ambariMetaInfo.get().getStackProperties(targetStack.getStackName(), targetStack.getStackVersion());
        addToMap(newMap, set);
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.state.ConfigMergeHelper.ThreeWayValue>> result = new java.util.HashMap<>();
        for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> entry : oldMap.entrySet()) {
            if (!newMap.containsKey(entry.getKey())) {
                org.apache.ambari.server.state.ConfigMergeHelper.LOG.info("Stack {} does not have an equivalent config type {} in {}", oldStack.getStackId(), entry.getKey(), targetStack.getStackId());
                continue;
            }
            java.util.Map<java.lang.String, java.lang.String> oldPairs = entry.getValue();
            java.util.Map<java.lang.String, java.lang.String> newPairs = newMap.get(entry.getKey());
            java.util.Collection<java.lang.String> customValueKeys = null;
            org.apache.ambari.server.state.Config config = cluster.getDesiredConfigByType(entry.getKey());
            if (null != config) {
                java.util.Set<java.lang.String> valueKeys = config.getProperties().keySet();
                customValueKeys = org.apache.commons.collections.CollectionUtils.subtract(valueKeys, oldPairs.keySet());
            }
            if (null != customValueKeys) {
                for (java.lang.String prop : customValueKeys) {
                    java.lang.String newVal = newPairs.get(prop);
                    java.lang.String savedVal = config.getProperties().get(prop);
                    if (((null != newVal) && (null != savedVal)) && (!newVal.equals(savedVal))) {
                        org.apache.ambari.server.state.ConfigMergeHelper.ThreeWayValue twv = new org.apache.ambari.server.state.ConfigMergeHelper.ThreeWayValue();
                        twv.oldStackValue = null;
                        twv.newStackValue = org.apache.ambari.server.state.ConfigMergeHelper.normalizeValue(savedVal, newVal.trim());
                        twv.savedValue = savedVal.trim();
                        if (!result.containsKey(entry.getKey())) {
                            result.put(entry.getKey(), new java.util.HashMap<>());
                        }
                        result.get(entry.getKey()).put(prop, twv);
                    }
                }
            }
            java.util.Collection<java.lang.String> common = org.apache.commons.collections.CollectionUtils.intersection(newPairs.keySet(), oldPairs.keySet());
            for (java.lang.String prop : common) {
                java.lang.String oldStackVal = oldPairs.get(prop);
                java.lang.String newStackVal = newPairs.get(prop);
                java.lang.String savedVal = "";
                if (null != config) {
                    savedVal = config.getProperties().get(prop);
                }
                if (((!((newStackVal == null) || (oldStackVal == null))) && (!newStackVal.equals(savedVal))) && ((!oldStackVal.equals(newStackVal)) || (!oldStackVal.equals(savedVal)))) {
                    org.apache.ambari.server.state.ConfigMergeHelper.ThreeWayValue twv = new org.apache.ambari.server.state.ConfigMergeHelper.ThreeWayValue();
                    twv.oldStackValue = org.apache.ambari.server.state.ConfigMergeHelper.normalizeValue(savedVal, oldStackVal.trim());
                    twv.newStackValue = org.apache.ambari.server.state.ConfigMergeHelper.normalizeValue(savedVal, newStackVal.trim());
                    twv.savedValue = (null == savedVal) ? null : savedVal.trim();
                    if (!result.containsKey(entry.getKey())) {
                        result.put(entry.getKey(), new java.util.HashMap<>());
                    }
                    result.get(entry.getKey()).put(prop, twv);
                }
            }
        }
        return result;
    }

    private void addToMap(java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> map, java.util.Set<org.apache.ambari.server.state.PropertyInfo> stackProperties) {
        for (org.apache.ambari.server.state.PropertyInfo pi : stackProperties) {
            java.lang.String type = org.apache.ambari.server.state.ConfigHelper.fileNameToConfigType(pi.getFilename());
            if (!map.containsKey(type)) {
                map.put(type, new java.util.HashMap<>());
            }
            map.get(type).put(pi.getName(), pi.getValue());
        }
    }

    public static class ThreeWayValue {
        public java.lang.String oldStackValue;

        public java.lang.String newStackValue;

        public java.lang.String savedValue;
    }

    static java.lang.String normalizeValue(java.lang.String templateValue, java.lang.String newRawValue) {
        if (null == templateValue) {
            return newRawValue;
        }
        java.util.regex.Matcher m = org.apache.ambari.server.state.ConfigMergeHelper.HEAP_PATTERN.matcher(templateValue);
        if (m.matches()) {
            return newRawValue + m.group(2);
        }
        return newRawValue;
    }
}