package org.apache.ambari.server.stack.upgrade;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
@javax.xml.bind.annotation.XmlEnum
public enum ExecuteHostType {

    @javax.xml.bind.annotation.XmlEnumValue("master")
    MASTER,
    @javax.xml.bind.annotation.XmlEnumValue("any")
    ANY,
    @javax.xml.bind.annotation.XmlEnumValue("first")
    FIRST,
    @javax.xml.bind.annotation.XmlEnumValue("all")
    ALL;}