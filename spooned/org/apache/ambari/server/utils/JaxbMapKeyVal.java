package org.apache.ambari.server.utils;
import javax.xml.bind.annotation.XmlElement;
public class JaxbMapKeyVal {
    @javax.xml.bind.annotation.XmlElement
    public java.lang.String key;

    @javax.xml.bind.annotation.XmlElement
    public java.lang.String value;

    public JaxbMapKeyVal() {
    }

    public JaxbMapKeyVal(java.lang.String key, java.lang.String value) {
        this.key = key;
        this.value = value;
    }
}