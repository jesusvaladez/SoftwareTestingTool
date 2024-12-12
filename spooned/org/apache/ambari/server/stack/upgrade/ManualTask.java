package org.apache.ambari.server.stack.upgrade;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
@javax.xml.bind.annotation.XmlRootElement
@javax.xml.bind.annotation.XmlAccessorType(javax.xml.bind.annotation.XmlAccessType.FIELD)
@javax.xml.bind.annotation.XmlType(name = "manual")
public class ManualTask extends org.apache.ambari.server.stack.upgrade.ServerSideActionTask {
    public ManualTask() {
        implClass = org.apache.ambari.server.serveraction.upgrades.ManualStageAction.class.getName();
    }

    @javax.xml.bind.annotation.XmlTransient
    private org.apache.ambari.server.stack.upgrade.Task.Type type = org.apache.ambari.server.stack.upgrade.Task.Type.MANUAL;

    @javax.xml.bind.annotation.XmlTransient
    public java.lang.String structuredOut = null;

    @java.lang.Override
    public org.apache.ambari.server.stack.upgrade.Task.Type getType() {
        return type;
    }

    @java.lang.Override
    public org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper.Type getStageWrapperType() {
        return org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper.Type.SERVER_SIDE_ACTION;
    }
}