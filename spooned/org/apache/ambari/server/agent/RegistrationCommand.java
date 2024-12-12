package org.apache.ambari.server.agent;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
@javax.xml.bind.annotation.XmlRootElement
@javax.xml.bind.annotation.XmlAccessorType(javax.xml.bind.annotation.XmlAccessType.FIELD)
@javax.xml.bind.annotation.XmlType(name = "", propOrder = {  })
public class RegistrationCommand extends org.apache.ambari.server.agent.AgentCommand {
    public RegistrationCommand() {
        super(org.apache.ambari.server.agent.AgentCommand.AgentCommandType.REGISTRATION_COMMAND);
    }
}