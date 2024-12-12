package org.apache.ambari.server.state.theme;
import io.swagger.annotations.ApiModelProperty;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;
@org.codehaus.jackson.map.annotate.JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@org.codehaus.jackson.annotate.JsonIgnoreProperties(ignoreUnknown = true)
public class Widget implements org.apache.ambari.server.controller.ApiModel {
    @org.codehaus.jackson.annotate.JsonProperty("type")
    private java.lang.String type;

    @org.codehaus.jackson.annotate.JsonProperty("units")
    private java.util.List<org.apache.ambari.server.state.theme.Unit> units;

    @org.codehaus.jackson.annotate.JsonProperty("required-properties")
    private java.util.Map<java.lang.String, java.lang.String> requiredProperties;

    @org.codehaus.jackson.annotate.JsonProperty("display-name")
    private java.lang.String displayName;

    @io.swagger.annotations.ApiModelProperty(name = "type")
    public java.lang.String getType() {
        return type;
    }

    public void setType(java.lang.String type) {
        this.type = type;
    }

    @io.swagger.annotations.ApiModelProperty(name = "units")
    public java.util.List<org.apache.ambari.server.state.theme.Unit> getUnits() {
        return units;
    }

    public void setUnits(java.util.List<org.apache.ambari.server.state.theme.Unit> units) {
        this.units = units;
    }

    @io.swagger.annotations.ApiModelProperty(name = "required-properties")
    public java.util.Map<java.lang.String, java.lang.String> getRequiredProperties() {
        return requiredProperties;
    }

    public void setRequiredProperties(java.util.Map<java.lang.String, java.lang.String> requiredProperties) {
        this.requiredProperties = requiredProperties;
    }

    @io.swagger.annotations.ApiModelProperty(name = "display-name")
    public java.lang.String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(java.lang.String displayName) {
        this.displayName = displayName;
    }
}