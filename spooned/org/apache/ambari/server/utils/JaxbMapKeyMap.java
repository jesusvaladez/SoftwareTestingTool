package org.apache.ambari.server.utils;
import javax.xml.bind.annotation.XmlElement;
public class JaxbMapKeyMap {
    @javax.xml.bind.annotation.XmlElement
    public java.lang.String key;

    @javax.xml.bind.annotation.XmlElement
    public org.apache.ambari.server.utils.JaxbMapKeyVal[] value;

    private JaxbMapKeyMap() {
    }

    public JaxbMapKeyMap(java.lang.String key, org.apache.ambari.server.utils.JaxbMapKeyVal[] value) {
        this.key = key;
        this.value = value;
    }
}