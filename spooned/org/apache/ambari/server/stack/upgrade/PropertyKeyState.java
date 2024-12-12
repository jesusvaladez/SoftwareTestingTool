package org.apache.ambari.server.stack.upgrade;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
@javax.xml.bind.annotation.XmlEnum
public enum PropertyKeyState {

    @javax.xml.bind.annotation.XmlEnumValue("present")
    PRESENT,
    @javax.xml.bind.annotation.XmlEnumValue("absent")
    ABSENT;}