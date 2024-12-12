package org.apache.ambari.server.state.theme;
import io.swagger.annotations.ApiModelProperty;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;
@org.codehaus.jackson.map.annotate.JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@org.codehaus.jackson.annotate.JsonIgnoreProperties(ignoreUnknown = true)
public class ConfigPlacement {
    private static final java.lang.String PROPERTY_VALUE_ATTRIBUTES = "property_value_attributes";

    private static final java.lang.String CONFIG = "config";

    private static final java.lang.String SUBSECTION_NAME = "subsection-name";

    private static final java.lang.String SUBSECTION_TAB_NAME = "subsection-tab-name";

    private static final java.lang.String DEPENDS_ON = "depends-on";

    @org.codehaus.jackson.annotate.JsonProperty(org.apache.ambari.server.state.theme.ConfigPlacement.CONFIG)
    private java.lang.String config;

    @org.codehaus.jackson.annotate.JsonProperty(org.apache.ambari.server.state.theme.ConfigPlacement.SUBSECTION_NAME)
    private java.lang.String subsectionName;

    @org.codehaus.jackson.annotate.JsonProperty(org.apache.ambari.server.state.theme.ConfigPlacement.SUBSECTION_TAB_NAME)
    private java.lang.String subsectionTabName;

    @org.codehaus.jackson.annotate.JsonProperty(org.apache.ambari.server.state.theme.ConfigPlacement.PROPERTY_VALUE_ATTRIBUTES)
    private org.apache.ambari.server.state.ValueAttributesInfo propertyValueAttributes;

    @org.codehaus.jackson.annotate.JsonProperty(org.apache.ambari.server.state.theme.ConfigPlacement.DEPENDS_ON)
    private java.util.List<org.apache.ambari.server.state.theme.ConfigCondition> dependsOn;

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.state.theme.ConfigPlacement.CONFIG)
    public java.lang.String getConfig() {
        return config;
    }

    public void setConfig(java.lang.String config) {
        this.config = config;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.state.theme.ConfigPlacement.SUBSECTION_NAME)
    public java.lang.String getSubsectionName() {
        return subsectionName;
    }

    public void setSubsectionName(java.lang.String subsectionName) {
        this.subsectionName = subsectionName;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.state.theme.ConfigPlacement.SUBSECTION_TAB_NAME)
    public java.lang.String getSubsectionTabName() {
        return subsectionTabName;
    }

    public void setSubsectionTabName(java.lang.String subsectionTabName) {
        this.subsectionTabName = subsectionTabName;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.state.theme.ConfigPlacement.PROPERTY_VALUE_ATTRIBUTES)
    public org.apache.ambari.server.state.ValueAttributesInfo getPropertyValueAttributes() {
        return propertyValueAttributes;
    }

    public void setPropertyValueAttributes(org.apache.ambari.server.state.ValueAttributesInfo propertyValueAttributes) {
        this.propertyValueAttributes = propertyValueAttributes;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.state.theme.ConfigPlacement.DEPENDS_ON)
    public java.util.List<org.apache.ambari.server.state.theme.ConfigCondition> getDependsOn() {
        return dependsOn;
    }

    public void setDependsOn(java.util.List<org.apache.ambari.server.state.theme.ConfigCondition> dependsOn) {
        this.dependsOn = dependsOn;
    }

    @io.swagger.annotations.ApiModelProperty(name = "removed")
    public boolean isRemoved() {
        return subsectionName == null;
    }

    public void mergeWithParent(org.apache.ambari.server.state.theme.ConfigPlacement parent) {
        if (subsectionName == null) {
            subsectionName = parent.subsectionName;
        }
    }
}