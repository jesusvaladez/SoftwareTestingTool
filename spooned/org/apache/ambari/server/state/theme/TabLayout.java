package org.apache.ambari.server.state.theme;
import io.swagger.annotations.ApiModelProperty;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;
@org.codehaus.jackson.map.annotate.JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@org.codehaus.jackson.annotate.JsonIgnoreProperties(ignoreUnknown = true)
public class TabLayout {
    @org.codehaus.jackson.annotate.JsonProperty("tab-rows")
    private java.lang.String tabRows;

    @org.codehaus.jackson.annotate.JsonProperty("sections")
    private java.util.List<org.apache.ambari.server.state.theme.Section> sections;

    @org.codehaus.jackson.annotate.JsonProperty("tab-columns")
    private java.lang.String tabColumns;

    @io.swagger.annotations.ApiModelProperty(name = "tab-rows")
    public java.lang.String getTabRows() {
        return tabRows;
    }

    public void setTabRows(java.lang.String tabRows) {
        this.tabRows = tabRows;
    }

    @io.swagger.annotations.ApiModelProperty(name = "sections")
    public java.util.List<org.apache.ambari.server.state.theme.Section> getSections() {
        return sections;
    }

    public void setSections(java.util.List<org.apache.ambari.server.state.theme.Section> sections) {
        this.sections = sections;
    }

    @io.swagger.annotations.ApiModelProperty(name = "tab-columns")
    public java.lang.String getTabColumns() {
        return tabColumns;
    }

    public void setTabColumns(java.lang.String tabColumns) {
        this.tabColumns = tabColumns;
    }

    public void mergeWithParent(org.apache.ambari.server.state.theme.TabLayout parent) {
        if (tabColumns == null) {
            tabColumns = parent.tabColumns;
        }
        if (tabRows == null) {
            tabRows = parent.tabRows;
        }
        if (sections == null) {
            sections = parent.sections;
        } else if (parent.sections != null) {
            sections = mergedSections(parent.sections, sections);
        }
    }

    private java.util.List<org.apache.ambari.server.state.theme.Section> mergedSections(java.util.List<org.apache.ambari.server.state.theme.Section> parentSections, java.util.List<org.apache.ambari.server.state.theme.Section> childSections) {
        java.util.Map<java.lang.String, org.apache.ambari.server.state.theme.Section> mergedSections = new java.util.HashMap<>();
        for (org.apache.ambari.server.state.theme.Section parentSection : parentSections) {
            mergedSections.put(parentSection.getName(), parentSection);
        }
        for (org.apache.ambari.server.state.theme.Section childSection : childSections) {
            if (childSection.getName() != null) {
                if (childSection.isRemoved()) {
                    mergedSections.remove(childSection.getName());
                } else {
                    if (mergedSections.containsKey(childSection.getName())) {
                        org.apache.ambari.server.state.theme.Section parentSection = mergedSections.get(childSection.getName());
                        childSection.mergeWithParent(parentSection);
                    } else {
                        childSection.mergeWithParent(childSection);
                    }
                    mergedSections.put(childSection.getName(), childSection);
                }
            }
        }
        return new java.util.ArrayList<>(mergedSections.values());
    }
}