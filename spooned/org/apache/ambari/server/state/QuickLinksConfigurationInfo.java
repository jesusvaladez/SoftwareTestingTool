package org.apache.ambari.server.state;
import io.swagger.annotations.ApiModelProperty;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
@javax.xml.bind.annotation.XmlAccessorType(javax.xml.bind.annotation.XmlAccessType.FIELD)
public class QuickLinksConfigurationInfo implements org.apache.ambari.server.controller.ApiModel {
    private java.lang.String fileName;

    @javax.xml.bind.annotation.XmlElement(name = "default")
    private java.lang.Boolean isDefault = false;

    private java.lang.Boolean deleted = false;

    @javax.xml.bind.annotation.XmlTransient
    private java.util.Map<java.lang.String, org.apache.ambari.server.state.quicklinks.QuickLinks> quickLinksConfigurationMap = null;

    public QuickLinksConfigurationInfo() {
    }

    public java.util.Map<java.lang.String, org.apache.ambari.server.state.quicklinks.QuickLinks> getQuickLinksConfigurationMap() {
        return quickLinksConfigurationMap;
    }

    public void setQuickLinksConfigurationMap(java.util.Map<java.lang.String, org.apache.ambari.server.state.quicklinks.QuickLinks> quickLinksConfigurationMap) {
        this.quickLinksConfigurationMap = quickLinksConfigurationMap;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return ((((((((("QuickLinksConfigurationInfo{" + "deleted=") + deleted) + ", fileName='") + fileName) + '\'') + ", isDefault=") + isDefault) + ", quickLinksConfigurationMap=") + quickLinksConfigurationMap) + '}';
    }

    @io.swagger.annotations.ApiModelProperty(name = "file_name")
    public java.lang.String getFileName() {
        return fileName;
    }

    public void setFileName(java.lang.String fileName) {
        this.fileName = fileName;
    }

    @io.swagger.annotations.ApiModelProperty(name = "default")
    public java.lang.Boolean getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(java.lang.Boolean isDefault) {
        this.isDefault = isDefault;
    }

    @io.swagger.annotations.ApiModelProperty(hidden = true)
    public java.lang.Boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(java.lang.Boolean deleted) {
        this.deleted = deleted;
    }
}