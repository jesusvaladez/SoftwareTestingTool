package org.apache.ambari.server.utils;
import javax.xml.bind.annotation.XmlElement;
public class JaxbMapKeyList {
    @javax.xml.bind.annotation.XmlElement
    public java.lang.String key;

    @javax.xml.bind.annotation.XmlElement
    public java.util.List<java.lang.String> value;

    private JaxbMapKeyList() {
    }

    public JaxbMapKeyList(java.lang.String key, java.util.List<java.lang.String> value) {
        this.key = key;
        this.value = value;
    }
}