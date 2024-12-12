package org.apache.ambari.server.state;
import io.swagger.annotations.ApiModelProperty;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import org.codehaus.jackson.map.annotate.JsonSerialize;
@javax.xml.bind.annotation.XmlAccessorType(javax.xml.bind.annotation.XmlAccessType.FIELD)
@org.codehaus.jackson.map.annotate.JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ValueEntryInfo implements org.apache.ambari.server.controller.ApiModel {
    private java.lang.String value;

    private java.lang.String label;

    private java.lang.String description;

    @io.swagger.annotations.ApiModelProperty(name = "value")
    public java.lang.String getValue() {
        return value;
    }

    public void setValue(java.lang.String value) {
        this.value = value;
    }

    @io.swagger.annotations.ApiModelProperty(name = "label")
    public java.lang.String getLabel() {
        return label;
    }

    public void setLabel(java.lang.String label) {
        this.label = label;
    }

    @io.swagger.annotations.ApiModelProperty(name = "description")
    public java.lang.String getDescription() {
        return description;
    }

    public void setDescription(java.lang.String description) {
        this.description = description;
    }
}