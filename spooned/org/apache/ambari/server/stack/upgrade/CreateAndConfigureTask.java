package org.apache.ambari.server.stack.upgrade;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
@javax.xml.bind.annotation.XmlRootElement
@javax.xml.bind.annotation.XmlAccessorType(javax.xml.bind.annotation.XmlAccessType.FIELD)
@javax.xml.bind.annotation.XmlType(name = "create_and_configure")
public class CreateAndConfigureTask extends org.apache.ambari.server.stack.upgrade.ConfigureTask {
    public static final java.lang.String actionVerb = "CreateAndConfiguring";

    public CreateAndConfigureTask() {
        implClass = org.apache.ambari.server.serveraction.upgrades.CreateAndConfigureAction.class.getName();
    }
}