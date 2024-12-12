package org.apache.ambari.server.state.theme;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;
@org.codehaus.jackson.map.annotate.JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@org.codehaus.jackson.annotate.JsonIgnoreProperties(ignoreUnknown = true)
public class Placement {
    @org.codehaus.jackson.annotate.JsonProperty("configs")
    private java.util.List<org.apache.ambari.server.state.theme.ConfigPlacement> configs;

    @org.codehaus.jackson.annotate.JsonProperty("configuration-layout")
    private java.lang.String configurationLayout;

    public java.util.List<org.apache.ambari.server.state.theme.ConfigPlacement> getConfigs() {
        return configs;
    }

    public void setConfigs(java.util.List<org.apache.ambari.server.state.theme.ConfigPlacement> configs) {
        this.configs = configs;
    }

    public java.lang.String getConfigurationLayout() {
        return configurationLayout;
    }

    public void setConfigurationLayout(java.lang.String configurationLayout) {
        this.configurationLayout = configurationLayout;
    }

    public void mergeWithParent(org.apache.ambari.server.state.theme.Placement parent) {
        if (configurationLayout == null) {
            configurationLayout = parent.configurationLayout;
        }
        if (configs == null) {
            configs = parent.configs;
        } else if (parent.configs != null) {
            configs = mergeConfigs(parent.configs, configs);
        }
    }

    private java.util.List<org.apache.ambari.server.state.theme.ConfigPlacement> mergeConfigs(java.util.List<org.apache.ambari.server.state.theme.ConfigPlacement> parentConfigs, java.util.List<org.apache.ambari.server.state.theme.ConfigPlacement> childConfigs) {
        java.util.Map<java.lang.String, org.apache.ambari.server.state.theme.ConfigPlacement> mergedConfigPlacements = new java.util.LinkedHashMap<>();
        for (org.apache.ambari.server.state.theme.ConfigPlacement parentConfigPlacement : parentConfigs) {
            mergedConfigPlacements.put(parentConfigPlacement.getConfig(), parentConfigPlacement);
        }
        for (org.apache.ambari.server.state.theme.ConfigPlacement childConfigPlacement : childConfigs) {
            if (childConfigPlacement.getConfig() != null) {
                if (childConfigPlacement.isRemoved()) {
                    mergedConfigPlacements.remove(childConfigPlacement.getConfig());
                } else {
                    org.apache.ambari.server.state.theme.ConfigPlacement parentConfigPlacement = mergedConfigPlacements.get(childConfigPlacement.getConfig());
                    childConfigPlacement.mergeWithParent(parentConfigPlacement);
                    mergedConfigPlacements.put(childConfigPlacement.getConfig(), childConfigPlacement);
                }
            }
        }
        return new java.util.ArrayList<>(mergedConfigPlacements.values());
    }
}