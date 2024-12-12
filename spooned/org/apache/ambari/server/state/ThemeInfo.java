package org.apache.ambari.server.state;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
@javax.xml.bind.annotation.XmlAccessorType(javax.xml.bind.annotation.XmlAccessType.FIELD)
public class ThemeInfo {
    private java.lang.String fileName;

    @javax.xml.bind.annotation.XmlElement(name = "default")
    private java.lang.Boolean isDefault = false;

    private java.lang.Boolean deleted = false;

    @javax.xml.bind.annotation.XmlTransient
    private java.util.Map<java.lang.String, org.apache.ambari.server.state.theme.Theme> themeMap = null;

    public ThemeInfo() {
    }

    public java.util.Map<java.lang.String, org.apache.ambari.server.state.theme.Theme> getThemeMap() {
        return themeMap;
    }

    public void setThemeMap(java.util.Map<java.lang.String, org.apache.ambari.server.state.theme.Theme> themeMap) {
        this.themeMap = themeMap;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return ((((((((("ThemeInfo{" + "deleted=") + deleted) + ", fileName='") + fileName) + '\'') + ", isDefault=") + isDefault) + ", themeMap=") + themeMap) + '}';
    }

    public java.lang.String getFileName() {
        return fileName;
    }

    public void setFileName(java.lang.String fileName) {
        this.fileName = fileName;
    }

    public java.lang.Boolean getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(java.lang.Boolean isDefault) {
        this.isDefault = isDefault;
    }

    public java.lang.Boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(java.lang.Boolean deleted) {
        this.deleted = deleted;
    }
}