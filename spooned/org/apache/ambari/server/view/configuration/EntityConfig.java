package org.apache.ambari.server.view.configuration;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
@javax.xml.bind.annotation.XmlAccessorType(javax.xml.bind.annotation.XmlAccessType.FIELD)
public class EntityConfig {
    @javax.xml.bind.annotation.XmlElement(name = "class")
    private java.lang.String className;

    @javax.xml.bind.annotation.XmlElement(name = "id-property")
    private java.lang.String idProperty;

    public java.lang.String getClassName() {
        return className;
    }

    public java.lang.String getIdProperty() {
        return idProperty;
    }
}