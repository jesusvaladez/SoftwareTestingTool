package org.apache.ambari.server.stack.upgrade;
import javax.xml.bind.annotation.XmlElement;
public class Batch {
    @javax.xml.bind.annotation.XmlElement(name = "percent")
    public int percent;

    @javax.xml.bind.annotation.XmlElement(name = "message")
    public java.lang.String message;

    @javax.xml.bind.annotation.XmlElement(name = "summary")
    public java.lang.String summary;
}