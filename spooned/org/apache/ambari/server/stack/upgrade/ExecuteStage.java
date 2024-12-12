package org.apache.ambari.server.stack.upgrade;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import org.apache.commons.lang.StringUtils;
public class ExecuteStage {
    @javax.xml.bind.annotation.XmlAttribute(name = "title")
    public java.lang.String title;

    @javax.xml.bind.annotation.XmlAttribute(name = "id")
    public java.lang.String id;

    @javax.xml.bind.annotation.XmlElement(name = "direction")
    public org.apache.ambari.server.stack.upgrade.Direction intendedDirection = null;

    @javax.xml.bind.annotation.XmlAttribute(name = "service")
    public java.lang.String service;

    @javax.xml.bind.annotation.XmlAttribute(name = "component")
    public java.lang.String component;

    @javax.xml.bind.annotation.XmlElement(name = "task")
    public org.apache.ambari.server.stack.upgrade.Task task;

    @javax.xml.bind.annotation.XmlElement(name = "scope")
    public org.apache.ambari.server.stack.upgrade.UpgradeScope scope = org.apache.ambari.server.stack.upgrade.UpgradeScope.ANY;

    @javax.xml.bind.annotation.XmlElement(name = "condition")
    public org.apache.ambari.server.stack.upgrade.Condition condition;

    @java.lang.Override
    public java.lang.String toString() {
        return com.google.common.base.MoreObjects.toStringHelper(this).add("id", id).add("title", title).omitNullValues().toString();
    }

    void afterUnmarshal(javax.xml.bind.Unmarshaller unmarshaller, java.lang.Object parent) {
        if (task.getType().equals(org.apache.ambari.server.stack.upgrade.Task.Type.CONFIGURE) && org.apache.commons.lang.StringUtils.isNotEmpty(service)) {
            ((org.apache.ambari.server.stack.upgrade.ConfigureTask) (task)).associatedService = service;
        } else if (task.getType().equals(org.apache.ambari.server.stack.upgrade.Task.Type.CREATE_AND_CONFIGURE) && org.apache.commons.lang.StringUtils.isNotEmpty(service)) {
            ((org.apache.ambari.server.stack.upgrade.CreateAndConfigureTask) (task)).associatedService = service;
        }
    }
}