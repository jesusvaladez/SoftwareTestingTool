package org.apache.ambari.server.state;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
@javax.xml.bind.annotation.XmlEnum
public enum SecurityType {

    @javax.xml.bind.annotation.XmlEnumValue("none")
    NONE,
    @javax.xml.bind.annotation.XmlEnumValue("kerberos")
    KERBEROS;}