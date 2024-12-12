package org.apache.ambari.server.customactions;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
@javax.xml.bind.annotation.XmlRootElement(name = "actionDefinitions")
public class ActionDefinitionXml {
    @javax.xml.bind.annotation.XmlElement(name = "actionDefinition")
    private java.util.List<org.apache.ambari.server.customactions.ActionDefinitionSpec> actionDefinitions = new java.util.ArrayList<>();

    public java.util.List<org.apache.ambari.server.customactions.ActionDefinitionSpec> actionDefinitions() {
        return actionDefinitions;
    }
}