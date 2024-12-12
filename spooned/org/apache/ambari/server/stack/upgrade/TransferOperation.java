package org.apache.ambari.server.stack.upgrade;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
@javax.xml.bind.annotation.XmlEnum
public enum TransferOperation {

    @javax.xml.bind.annotation.XmlEnumValue("delete")
    DELETE,
    @javax.xml.bind.annotation.XmlEnumValue("move")
    MOVE,
    @javax.xml.bind.annotation.XmlEnumValue("copy")
    COPY;}