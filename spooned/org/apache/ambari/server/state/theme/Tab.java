package org.apache.ambari.server.state.theme;
import io.swagger.annotations.ApiModelProperty;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;
@org.codehaus.jackson.map.annotate.JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@org.codehaus.jackson.annotate.JsonIgnoreProperties(ignoreUnknown = true)
public class Tab implements org.apache.ambari.server.controller.ApiModel {
    @org.codehaus.jackson.annotate.JsonProperty("display-name")
    private java.lang.String displayName;

    @org.codehaus.jackson.annotate.JsonProperty("name")
    private java.lang.String name;

    @org.codehaus.jackson.annotate.JsonProperty("layout")
    private org.apache.ambari.server.state.theme.TabLayout tabLayout;

    @io.swagger.annotations.ApiModelProperty(name = "display-name")
    public java.lang.String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(java.lang.String displayName) {
        this.displayName = displayName;
    }

    @io.swagger.annotations.ApiModelProperty(name = "name")
    public java.lang.String getName() {
        return name;
    }

    public void setName(java.lang.String name) {
        this.name = name;
    }

    @io.swagger.annotations.ApiModelProperty(name = "layout")
    public org.apache.ambari.server.state.theme.TabLayout getTabLayout() {
        return tabLayout;
    }

    public void setTabLayout(org.apache.ambari.server.state.theme.TabLayout tabLayout) {
        this.tabLayout = tabLayout;
    }

    public void mergeWithParent(org.apache.ambari.server.state.theme.Tab parentTab) {
        if (displayName == null) {
            displayName = parentTab.displayName;
        }
        if (tabLayout == null) {
            tabLayout = parentTab.tabLayout;
        } else {
            tabLayout.mergeWithParent(parentTab.tabLayout);
        }
    }
}