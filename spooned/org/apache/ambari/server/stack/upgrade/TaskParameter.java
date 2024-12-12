package org.apache.ambari.server.stack.upgrade;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;
public class TaskParameter {
    @javax.xml.bind.annotation.XmlAttribute(name = "name")
    public java.lang.String name;

    @javax.xml.bind.annotation.XmlValue
    public java.lang.String value;
}