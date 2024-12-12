package org.apache.ambari.server.state.theme;
import io.swagger.annotations.ApiModelProperty;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;
@org.codehaus.jackson.map.annotate.JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@org.codehaus.jackson.annotate.JsonIgnoreProperties(ignoreUnknown = true)
public class Section implements org.apache.ambari.server.controller.ApiModel {
    @org.codehaus.jackson.annotate.JsonProperty("subsections")
    private java.util.List<org.apache.ambari.server.state.theme.Subsection> subsections;

    @org.codehaus.jackson.annotate.JsonProperty("display-name")
    private java.lang.String displayName;

    @org.codehaus.jackson.annotate.JsonProperty("row-index")
    private java.lang.String rowIndex;

    @org.codehaus.jackson.annotate.JsonProperty("section-rows")
    private java.lang.String sectionRows;

    @org.codehaus.jackson.annotate.JsonProperty("name")
    private java.lang.String name;

    @org.codehaus.jackson.annotate.JsonProperty("column-span")
    private java.lang.String columnSpan;

    @org.codehaus.jackson.annotate.JsonProperty("section-columns")
    private java.lang.String sectionColumns;

    @org.codehaus.jackson.annotate.JsonProperty("column-index")
    private java.lang.String columnIndex;

    @org.codehaus.jackson.annotate.JsonProperty("row-span")
    private java.lang.String rowSpan;

    @io.swagger.annotations.ApiModelProperty(name = "subsections")
    public java.util.List<org.apache.ambari.server.state.theme.Subsection> getSubsections() {
        return subsections;
    }

    public void setSubsections(java.util.List<org.apache.ambari.server.state.theme.Subsection> subsections) {
        this.subsections = subsections;
    }

    @io.swagger.annotations.ApiModelProperty(name = "display-name")
    public java.lang.String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(java.lang.String displayName) {
        this.displayName = displayName;
    }

    @io.swagger.annotations.ApiModelProperty(name = "row-index")
    public java.lang.String getRowIndex() {
        return rowIndex;
    }

    public void setRowIndex(java.lang.String rowIndex) {
        this.rowIndex = rowIndex;
    }

    @io.swagger.annotations.ApiModelProperty(name = "section-rows")
    public java.lang.String getSectionRows() {
        return sectionRows;
    }

    public void setSectionRows(java.lang.String sectionRows) {
        this.sectionRows = sectionRows;
    }

    @io.swagger.annotations.ApiModelProperty(name = "name")
    public java.lang.String getName() {
        return name;
    }

    public void setName(java.lang.String name) {
        this.name = name;
    }

    @io.swagger.annotations.ApiModelProperty(name = "column-span")
    public java.lang.String getColumnSpan() {
        return columnSpan;
    }

    public void setColumnSpan(java.lang.String columnSpan) {
        this.columnSpan = columnSpan;
    }

    public java.lang.String getSectionColumns() {
        return sectionColumns;
    }

    @io.swagger.annotations.ApiModelProperty(name = "section-columns")
    public void setSectionColumns(java.lang.String sectionColumns) {
        this.sectionColumns = sectionColumns;
    }

    @io.swagger.annotations.ApiModelProperty(name = "column-index")
    public java.lang.String getColumnIndex() {
        return columnIndex;
    }

    public void setColumnIndex(java.lang.String columnIndex) {
        this.columnIndex = columnIndex;
    }

    @io.swagger.annotations.ApiModelProperty(name = "row-span")
    public java.lang.String getRowSpan() {
        return rowSpan;
    }

    public void setRowSpan(java.lang.String rowSpan) {
        this.rowSpan = rowSpan;
    }

    public boolean isRemoved() {
        return (((((((columnIndex == null) && (columnSpan == null)) && (subsections == null)) && (displayName == null)) && (rowIndex == null)) && (rowSpan == null)) && (sectionRows == null)) && (sectionColumns == null);
    }

    public void mergeWithParent(org.apache.ambari.server.state.theme.Section parentSection) {
        if (displayName == null) {
            displayName = parentSection.displayName;
        }
        if (rowIndex == null) {
            rowIndex = parentSection.rowIndex;
        }
        if (rowSpan == null) {
            rowSpan = parentSection.rowSpan;
        }
        if (sectionRows == null) {
            sectionRows = parentSection.sectionRows;
        }
        if (columnIndex == null) {
            columnIndex = parentSection.columnIndex;
        }
        if (columnSpan == null) {
            columnSpan = parentSection.columnSpan;
        }
        if (sectionColumns == null) {
            sectionColumns = parentSection.sectionColumns;
        }
        if (subsections == null) {
            subsections = parentSection.subsections;
        } else if (parentSection.subsections != null) {
            subsections = mergeSubsections(parentSection.subsections, subsections);
        }
    }

    private java.util.List<org.apache.ambari.server.state.theme.Subsection> mergeSubsections(java.util.List<org.apache.ambari.server.state.theme.Subsection> parentSubsections, java.util.List<org.apache.ambari.server.state.theme.Subsection> childSubsections) {
        java.util.Map<java.lang.String, org.apache.ambari.server.state.theme.Subsection> mergedSubsections = new java.util.HashMap<>();
        for (org.apache.ambari.server.state.theme.Subsection parentSubsection : parentSubsections) {
            mergedSubsections.put(parentSubsection.getName(), parentSubsection);
        }
        for (org.apache.ambari.server.state.theme.Subsection childSubsection : childSubsections) {
            if (childSubsection.getName() != null) {
                if (childSubsection.isRemoved()) {
                    mergedSubsections.remove(childSubsection.getName());
                } else {
                    org.apache.ambari.server.state.theme.Subsection parentSection = mergedSubsections.get(childSubsection.getName());
                    childSubsection.mergeWithParent(parentSection);
                    mergedSubsections.put(childSubsection.getName(), childSubsection);
                }
            }
        }
        return new java.util.ArrayList<>(mergedSubsections.values());
    }
}