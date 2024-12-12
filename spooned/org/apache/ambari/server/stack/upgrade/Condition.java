package org.apache.ambari.server.stack.upgrade;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
@javax.xml.bind.annotation.XmlRootElement
@javax.xml.bind.annotation.XmlSeeAlso({ org.apache.ambari.server.stack.upgrade.SecurityCondition.class, org.apache.ambari.server.stack.upgrade.ConfigurationCondition.class })
public class Condition {
    public boolean isSatisfied(org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext upgradeContext) {
        return false;
    }
}