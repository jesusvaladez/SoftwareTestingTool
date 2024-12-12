package org.apache.ambari.server.state.theme;
import io.swagger.annotations.ApiModelProperty;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;
@org.codehaus.jackson.map.annotate.JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@org.codehaus.jackson.annotate.JsonIgnoreProperties(ignoreUnknown = true)
public class Layout {
    @org.codehaus.jackson.annotate.JsonProperty("name")
    private java.lang.String name;

    @org.codehaus.jackson.annotate.JsonProperty("tabs")
    private java.util.List<org.apache.ambari.server.state.theme.Tab> tabs;

    @io.swagger.annotations.ApiModelProperty(name = "name")
    public java.lang.String getName() {
        return name;
    }

    public void setName(java.lang.String name) {
        this.name = name;
    }

    @io.swagger.annotations.ApiModelProperty(name = "tabs")
    public java.util.List<org.apache.ambari.server.state.theme.Tab> getTabs() {
        return tabs;
    }

    public void setTabs(java.util.List<org.apache.ambari.server.state.theme.Tab> tabs) {
        this.tabs = tabs;
    }

    public void mergeWithParent(org.apache.ambari.server.state.theme.Layout parentLayout) {
        tabs = mergeTabs(parentLayout.tabs, tabs);
    }

    private java.util.List<org.apache.ambari.server.state.theme.Tab> mergeTabs(java.util.List<org.apache.ambari.server.state.theme.Tab> parentTabs, java.util.List<org.apache.ambari.server.state.theme.Tab> childTabs) {
        java.util.Map<java.lang.String, org.apache.ambari.server.state.theme.Tab> mergedTabs = new java.util.HashMap<>();
        for (org.apache.ambari.server.state.theme.Tab parentTab : parentTabs) {
            mergedTabs.put(parentTab.getName(), parentTab);
        }
        for (org.apache.ambari.server.state.theme.Tab childTab : childTabs) {
            if (childTab.getName() != null) {
                if ((childTab.getDisplayName() == null) && (childTab.getTabLayout() == null)) {
                    mergedTabs.remove(childTab.getName());
                } else {
                    org.apache.ambari.server.state.theme.Tab parentTab = mergedTabs.get(childTab.getName());
                    childTab.mergeWithParent(parentTab);
                    mergedTabs.put(childTab.getName(), childTab);
                }
            }
        }
        return new java.util.ArrayList<>(mergedTabs.values());
    }
}