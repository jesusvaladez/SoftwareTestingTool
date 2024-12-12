package org.apache.ambari.server.state.theme;
import io.swagger.annotations.ApiModelProperty;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;
@org.codehaus.jackson.map.annotate.JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@org.codehaus.jackson.annotate.JsonIgnoreProperties(ignoreUnknown = true)
public class Unit implements org.apache.ambari.server.controller.ApiModel {
    @org.codehaus.jackson.annotate.JsonProperty("unit-name")
    private java.lang.String unitName;

    @io.swagger.annotations.ApiModelProperty(name = "unit-name")
    public java.lang.String getUnitName() {
        return unitName;
    }

    public void setUnitName(java.lang.String unitName) {
        this.unitName = unitName;
    }
}