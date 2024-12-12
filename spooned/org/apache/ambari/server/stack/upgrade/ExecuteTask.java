package org.apache.ambari.server.stack.upgrade;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
@javax.xml.bind.annotation.XmlRootElement
@javax.xml.bind.annotation.XmlAccessorType(javax.xml.bind.annotation.XmlAccessType.FIELD)
@javax.xml.bind.annotation.XmlType(name = "execute")
public class ExecuteTask extends org.apache.ambari.server.stack.upgrade.Task {
    @javax.xml.bind.annotation.XmlTransient
    private org.apache.ambari.server.stack.upgrade.Task.Type type = org.apache.ambari.server.stack.upgrade.Task.Type.EXECUTE;

    @javax.xml.bind.annotation.XmlAttribute
    public org.apache.ambari.server.stack.upgrade.ExecuteHostType hosts = org.apache.ambari.server.stack.upgrade.ExecuteHostType.ALL;

    @javax.xml.bind.annotation.XmlElement(name = "script")
    public java.lang.String script;

    @javax.xml.bind.annotation.XmlElement(name = "function")
    public java.lang.String function;

    public static final java.lang.String actionVerb = "Executing";

    @java.lang.Override
    public org.apache.ambari.server.stack.upgrade.Task.Type getType() {
        return type;
    }

    @java.lang.Override
    public org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper.Type getStageWrapperType() {
        return org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper.Type.UPGRADE_TASKS;
    }

    @java.lang.Override
    public java.lang.String getActionVerb() {
        return org.apache.ambari.server.stack.upgrade.ExecuteTask.actionVerb;
    }
}