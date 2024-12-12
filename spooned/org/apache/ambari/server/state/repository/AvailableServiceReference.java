package org.apache.ambari.server.state.repository;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
public class AvailableServiceReference {
    @javax.xml.bind.annotation.XmlAttribute(name = "idref")
    public java.lang.String serviceIdReference;

    @javax.xml.bind.annotation.XmlElement(name = "component")
    public java.util.Set<java.lang.String> components = new java.util.HashSet<>();
}